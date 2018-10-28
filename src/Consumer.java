import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public class Consumer extends Agent {
	private EnergyProducer[] energyProducers;
	private Balance balance = new Balance();
	private ConsumerType type;

	public enum ConsumerType {
		Single, Double, One_Child_Family, Two_Child_Family, Three_Child_Family, Zombies
	};

	private static final double normalAverage = 4000; // kWh, 0.457 kW per hour
														// (https://www.ovoenergy.com/guides/energy-guides/how-much-electricity-does-a-home-use.html)
	private static final Map<ConsumerType, Double> typeToAvgConsumption = Map.of(ConsumerType.Zombies, 0.0,
			ConsumerType.Single, 1800d / normalAverage, ConsumerType.Double, 2950d / normalAverage,
			ConsumerType.One_Child_Family, 4000d / normalAverage, ConsumerType.Two_Child_Family, 4600d / normalAverage,
			ConsumerType.Three_Child_Family, 5300d / normalAverage);
	// ramp up from 5 to 7, steady between 8 and 17:30, ramp to peak at 19:30,
	// decline until 1
	private static final double[] timeFactor = new double[] { 0.8315, 0.6127, 0.54705, 0.48134, 0.54705, 0.49234,
			0.56843, 0.87527, 1.08315, 1.08315, 1.06127, 1.01751, 1.07221, 1.03939, 1.00656, 1.02845, 1.09409, 1.40044,
			1.55361, 1.55361, 1.52079, 1.43326, 1.26915, 1.09401 };

	public Consumer(EnergyProducer[] producers, ConsumerType typ) {
		type = typ;
		this.energyProducers = producers;
		subAgents.addAll((Collection<? extends Agent>) Arrays.asList(producers));
	}

	public void act() {
		super.act();
		balance.generated = produceHour();
		balance.consumed = consumeHour();
		Simulation.currentSimulation.getCentralBox().register(this);
	}

	private double produceHour() {
		double value = 0;
		for (EnergyProducer ep : energyProducers)
			value += ep.value();
		return value;
	}

	private double consumeHour() {
		return typeToAvgConsumption.get(type) * timeFactor[Simulation.currentSimulation.getCurrentDateTime().getHour()]
				* 1000; // kW to W
	}

	public Balance getBalance() {
		return balance;
	}

	@Override
	public String toString() {
		return Util.round(produceHour() / 1000, 2) + " / " + Util.round(consumeHour() / 1000, 2) + " kWh\n"
				+ Util.round(Util.ctToEU(balance.money), 2) + " €";
	}

}

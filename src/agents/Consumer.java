package agents;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import environmemnt.NetworkManager;
import environmemnt.PriceFactor;
import main.Simulation;
import main.Strategy;
import main.Util;
import ui.History;

public class Consumer extends Agent {
	private EnergyProducer[] energyProducers;
	private CentralBox centralBox;
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

	public Consumer(Consumer copy) {
		this(copy.energyProducers, copy.type);
	}

	private void baseRepositAct(Battery bat) {
		double eTotal = balance.generated - balance.consumed;
		if (eTotal > 0) {
			if (bat.getEnergyStored() + eTotal < bat.getMaxStorage()) {
				bat.setEnergyStored(bat.getEnergyStored() + eTotal);
			} else {
				double sellAmount = bat.getEnergyStored() + eTotal - bat.getMaxStorage();
				double earning = Simulation.currentSimulation.getNetworkManager().sell(sellAmount);
				balance.money += earning;
				bat.setEnergyStored(bat.getMaxStorage());

				Simulation.currentSimulation.hist.nextStep[History.CT_EARNED + centralBox.index * 6] += earning;
				Simulation.currentSimulation.hist.nextStep[History.W_SOLD + centralBox.index * 6] += sellAmount;

			}
		} else {
			if (bat.getEnergyStored() < -eTotal) {
				double buyAmount = eTotal + bat.getEnergyStored();
				double spending = Simulation.currentSimulation.getNetworkManager().buy(buyAmount);
				balance.money += spending;
				bat.setEnergyStored(0);

				Simulation.currentSimulation.hist.nextStep[History.CT_SPENT + centralBox.index * 6] -= spending;
				Simulation.currentSimulation.hist.nextStep[History.W_BOUGHT + centralBox.index * 6] -= buyAmount;

			} else {
				bat.setEnergyStored(bat.getEnergyStored() + eTotal);
			}
		}
	}

	public void act() {
		super.act();
		balance.generated = produceHour();
		balance.consumed = consumeHour();

		Strategy s = centralBox.getStrategy();
		if (s.equals(Strategy.VPP) || s.equals(Strategy.Communal))
			centralBox.register(this);
		else {
			Battery bat = Util.firstInstanceOfType(Battery.class, subAgents);
			baseRepositAct(bat);
			int hour = Simulation.currentSimulation.getCurrentDateTime().getHour();
			boolean low = PriceFactor.lowTimes.contains(hour), peak = PriceFactor.peakTimes.contains(hour);
			NetworkManager manager = Simulation.currentSimulation.getNetworkManager();
			if (s.equals(Strategy.Reposit_Aggressive)) {
				if (peak) {
					double earned = manager.sell(bat.getEnergyStored());
					balance.money += earned;
					bat.setEnergyStored(0);

					Simulation.currentSimulation.hist.nextStep[History.CT_EARNED + centralBox.index * 6] += earned;
					Simulation.currentSimulation.hist.nextStep[History.W_SOLD + centralBox.index * 6] += bat
							.getEnergyStored();

				}
			} else if (s.equals(Strategy.Reposit_Defensive)) {
				if (low) {
					double spent = manager.buy(bat.getMaxStorage() - bat.getEnergyStored());
					balance.money -= spent;
					bat.setEnergyStored(bat.getMaxStorage());

					Simulation.currentSimulation.hist.nextStep[History.CT_SPENT + centralBox.index * 6] += spent;
					Simulation.currentSimulation.hist.nextStep[History.W_BOUGHT + centralBox.index * 6] += bat
							.getMaxStorage() - bat.getEnergyStored();

				}
			} else {
				if (peak && bat.getEnergyStored() / bat.getMaxStorage() > bat.getMinStorageRel()) {
					double sold = bat.getEnergyStored() - bat.getMaxStorage() * bat.getMinStorageRel();
					double earned = manager.sell(sold);
					balance.money += earned;
					bat.setEnergyStored(bat.getMaxStorage() * bat.getMinStorageRel());

					Simulation.currentSimulation.hist.nextStep[History.CT_EARNED + centralBox.index * 6] += earned;
					Simulation.currentSimulation.hist.nextStep[History.W_SOLD + centralBox.index * 6] += sold;

				} else if (low && bat.getEnergyStored() / bat.getMaxStorage() < bat.getMinStorageRel()) {
					double bought = bat.getMaxStorage() * bat.getMinStorageRel() - bat.getEnergyStored();
					double spent = manager.buy(bought);
					balance.money -= spent;
					bat.setEnergyStored(bat.getMaxStorage() * bat.getMinStorageRel());

					Simulation.currentSimulation.hist.nextStep[History.CT_SPENT + centralBox.index * 6] += spent;
					Simulation.currentSimulation.hist.nextStep[History.W_BOUGHT + centralBox.index * 6] += bought;

				}
			}
		}
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

	public void setCentralBox(CentralBox centralBox) {
		this.centralBox = centralBox;
		Strategy s = centralBox.getStrategy();
		if (s.equals(Strategy.Reposit_Aggressive) || s.equals(Strategy.Reposit_Normal)
				|| s.equals(Strategy.Reposit_Defensive))
			subAgents.add(new Battery());
	}

}

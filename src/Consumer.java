import java.util.Map;
import java.util.Map.Entry;

public class Consumer {
	private EnergyProducer[] energyProducers;
	private Balance balance = new Balance();
	private ConsumerType type;
	
	private enum ConsumerType {
		SINGLE, DOUBLE, ONE_CHILD_FAMILY, TWO_CHILD_FAMILY, THREE_CHILD_FAMILY
	};

	private static final Map<ConsumerType, Double> typeToAvgConsumption = Map.of(ConsumerType.SINGLE, 1800d,
			ConsumerType.DOUBLE, 2950d, ConsumerType.ONE_CHILD_FAMILY, 4000d, ConsumerType.TWO_CHILD_FAMILY, 4600d,
			ConsumerType.THREE_CHILD_FAMILY, 5300d);

	public Consumer(EnergyProducer[] producers, ConsumerType typ) {
		type = typ;
		energyProducers = producers;
	}

	public void act() {
		for (EnergyProducer ep : energyProducers)
			balance.energy += ep.produce();
		balance.energy -= consume();
	}
	
	private double consume() {
		return 1;
	}
	
	public Balance getBalance() {
		return balance;
	}

}

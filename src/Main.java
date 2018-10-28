public class Main {
	public static void main(String[] args) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Simulation.currentSimulation.start(Strategy.VPP, 1, new Consumer[] {
						new Consumer(new EnergyProducer[] {new SolarPanel(8, 270)}, Consumer.ConsumerType.Zombies),
						new Consumer(new EnergyProducer[] {}, Consumer.ConsumerType.One_Child_Family),
						new Consumer(new EnergyProducer[] {}, Consumer.ConsumerType.Three_Child_Family),
						new Consumer(new EnergyProducer[] {new WindTurbine(WindTurbine.PredefinedType.Aeolos_300)}, Consumer.ConsumerType.Two_Child_Family),
						new Consumer(new EnergyProducer[] {new WindTurbine(WindTurbine.PredefinedType.Aeolos_10000)}, Consumer.ConsumerType.Double),
						new Consumer(new EnergyProducer[] {new SolarPanel(8, 260)}, Consumer.ConsumerType.Three_Child_Family),
						new Consumer(new EnergyProducer[] {new SolarPanel(6, 270), new WindTurbine(WindTurbine.PredefinedType.Windleaf)}, Consumer.ConsumerType.Two_Child_Family)
				}, "2018-01-01-00", new View());
			}
		}).start();
	}
}

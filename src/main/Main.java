package main;

import agents.Community;
import agents.Consumer;
import agents.EnergyProducer;
import agents.SolarPanel;
import agents.WindTurbine;
import ui.View;

public class Main {
	public static void main(String[] args) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				Consumer[][] consumerAgentsLists = Util.getIdenticalLists(new Consumer[] {
						new Consumer(new EnergyProducer[] { new SolarPanel(8, 270) }, Consumer.ConsumerType.Zombies),
						new Consumer(new EnergyProducer[] {}, Consumer.ConsumerType.One_Child_Family),
						new Consumer(new EnergyProducer[] {}, Consumer.ConsumerType.Three_Child_Family),
						new Consumer(new EnergyProducer[] { new WindTurbine(WindTurbine.PredefinedType.Aeolos_300) },
								Consumer.ConsumerType.Two_Child_Family),
						new Consumer(new EnergyProducer[] { new WindTurbine(WindTurbine.PredefinedType.Aeolos_10000) },
								Consumer.ConsumerType.Double),
						new Consumer(new EnergyProducer[] { new SolarPanel(8, 260) }, Consumer.ConsumerType.Single),
						new Consumer(
								new EnergyProducer[] { new SolarPanel(6, 270),
										new WindTurbine(WindTurbine.PredefinedType.Windleaf) },
								Consumer.ConsumerType.Two_Child_Family) },
						3);
				Simulation.currentSimulation.start(1,
						new Community[] { new Community(Strategy.Reposit_Normal, consumerAgentsLists[0], 0),
								new Community(Strategy.Reposit_Aggressive, consumerAgentsLists[1], 1),
								new Community(Strategy.Reposit_Defensive, consumerAgentsLists[2], 2),
								new Community(Strategy.VPP, consumerAgentsLists[3], 3) },
						"2018-01-01-00", new View());
			}
		}).start();
	}
}

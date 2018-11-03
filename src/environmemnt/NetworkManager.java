package environmemnt;

import main.Simulation;

public class NetworkManager {

	private static double tax = 15.5 / 1000; // energy tax + btw -> convert from kW to W

	public double buy(double W) {
		return W * tax + W * (Double) Simulation.currentSimulation.environmentValue(PriceFactor.class);
	}

	public double sell(double W) {
		return W * (Double) Simulation.currentSimulation.environmentValue(PriceFactor.class);
	}

}

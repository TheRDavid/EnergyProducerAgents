public class NetworkManager {

	private static double tax = 15.5; // energy tax + btw

	public double buy(double kWh) {
		return kWh * tax + kWh * (Double) Simulation.currentSimulation.environmentValue(PriceFactor.class);
	}

	public double sell(double kWh) {
		return kWh * (Double) Simulation.currentSimulation.environmentValue(PriceFactor.class);
	}
	
	public double trade(double kWh) {
		return kWh < 0 ? buy(kWh) : sell(kWh);
	}
	
}

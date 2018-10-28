
public class SolarPanel extends Agent implements EnergyProducer {
	private double squareMeterArea; // A
	private double avgYearlyOutputPerIrradiation; // H
	private double value = 0;

	public SolarPanel(double sqm, double avgPerIrradiation) {
		squareMeterArea = sqm;
		avgYearlyOutputPerIrradiation = avgPerIrradiation;
	}

	@Override
	public void act() {
		value = squareMeterArea * avgYearlyOutputPerIrradiation
				* (Double) Simulation.currentSimulation.environmentValue(SunFactor.class);
	}

	@Override
	public double value() {
		return value;
	}

	@Override
	public String toString() {
		return squareMeterArea + " m² * " + avgYearlyOutputPerIrradiation + " W * "
				+ Util.round((double) Simulation.currentSimulation.environmentValue(SunFactor.class), 1) + "\n= "
				+ Util.round(value, 2) + " W";
	}
}

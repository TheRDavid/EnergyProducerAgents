
public class SolarPanel implements EnergyProducer {
	private double squareMeterArea; // A
	private double avgYearlyOutputPerIrradiation; // H

	public SolarPanel(double sqm, double avgPerIrradiation) {
		squareMeterArea = sqm;
		avgYearlyOutputPerIrradiation = avgPerIrradiation;
	}

	@Override
	public double produce() {
		return squareMeterArea * avgYearlyOutputPerIrradiation
				* (Double) Simulation.currentSimulation.environmentValue(SunFactor.class);
	}
}

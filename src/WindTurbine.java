// Take values from http://www.windturbinestar.com/300w-vertical-wind-turbine.html
// https://windchallenge.com/the-windleaf/#specs
public class WindTurbine extends Agent implements EnergyProducer {
	private double ratedPower, ratedWindSpeed, startWindSpeed;
	private double value = 0;

	public enum PredefinedType {
		Aeolos_300, Aeolos_600, Aeolos_10000, Windleaf
	}

	public WindTurbine(PredefinedType type) {
		switch (type) {
		case Aeolos_300:
			ratedPower = 300;
			ratedWindSpeed = Util.mshToKmh(10);
			startWindSpeed = Util.mshToKmh(1.5);
			break;
		case Aeolos_10000:
			ratedPower = 10000;
			ratedWindSpeed = Util.mshToKmh(11);
			startWindSpeed = Util.mshToKmh(2.5);
			break;
		case Aeolos_600:
			ratedPower = 600;
			ratedWindSpeed = Util.mshToKmh(10);
			startWindSpeed = Util.mshToKmh(1.5);
			break;
		case Windleaf:
			ratedPower = 700;
			ratedWindSpeed = Util.mshToKmh(9.5);
			startWindSpeed = Util.mshToKmh(1.5);
			break;
		}
	}

	/**
	 * @param rPower           Rated power in watts
	 * @param rWindSpeed       Rated wind speed in km/h
	 * @param startupWindSpeed Startup wind speed in km/h
	 */
	public WindTurbine(double rPower, double rWindSpeed, double startupWindSpeed) {
		ratedPower = rPower;
		ratedWindSpeed = rWindSpeed;
		startWindSpeed = startupWindSpeed;
	}

	@Override
	public void act() {
		double windSpeed = (double) Simulation.currentSimulation.environmentValue(WindFactor.class);
		if (windSpeed < startWindSpeed)
			value = 0;
		double range = ratedWindSpeed - startWindSpeed;
		double achieved = windSpeed - startWindSpeed;
		double percentagePower = achieved / range;
		value = percentagePower * ratedPower;
		System.out.println("@" + windSpeed + "km/h of rated speed (" + ratedWindSpeed + " km/h) we get"
				+ percentagePower + "% of " + ratedPower + " W => " + value + " W");
	}

	@Override
	public double value() {
		return value;
	}

	@Override
	public String toString() {
		return ratedPower + " W @ " + ratedWindSpeed + " km/h\n= " + Util.round(value, 2);
	}

}

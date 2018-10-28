
public class Util {
	public static final double round(double value, int places) {
		return Math.round(value * Math.pow(10, places)) / Math.pow(10, places);
	}

	/**
	 * Merely there to make code more readable
	 * 
	 * @param cents
	 * @return euro
	 */
	public static final double ctToEU(double cents) {
		return cents / 100.0;
	}

	/**
	 * Merely there to make code more readable
	 * 
	 * @param speedKmh
	 * @return meters a second
	 */
	public static final double kmhToMs(double speedKmh) {
		return speedKmh / 3.6;
	}

	/**
	 * Merely there to make code more readable
	 * 
	 * @param speedMs
	 * @return kilometers per hour
	 */
	public static final double mshToKmh(double speedMs) {
		return speedMs * 3.6;
	}
}

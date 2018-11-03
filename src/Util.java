import java.util.Collection;

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

	public static Consumer[][] getIdenticalLists(Consumer[] consumers, int additionals) {
		Consumer[][] lists = new Consumer[1 + additionals][];
		lists[0] = consumers;
		for (int i = 0; i < additionals; i++) {
			lists[1 + i] = new Consumer[consumers.length];
			for (int j = 0; j < consumers.length; j++) {
				lists[1 + i][j] = new Consumer(consumers[j]);
			}
		}
		return lists;
	}

	public static <E> E firstInstanceOfType(Class<E> cl, Collection c) {
		for (Object o : c)
			if (o.getClass().equals(cl))
				return (E) o;
		return null;
	}
}

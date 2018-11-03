package environmemnt;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import main.Simulation;

public class PriceFactor extends EnvironmentFactor<Double> {

	public static final Set<Integer> lowTimes = new HashSet<>(Arrays.asList(3, 4, 5));
	public static final Set<Integer> peakTimes = new HashSet<>(Arrays.asList(9, 10, 20));

	private HashMap<Integer, Double> timeToCtPerW = new HashMap<>() {
		{
			put(0, 0.00225);
			put(1, 0.00225);
			put(2, 0.00226);
			put(3, 0.00218); // low from 3
			put(4, 0.002d); // low
			put(5, 0.002d); // low to 5 AM
			put(6, 0.0028);
			put(7, 0.005d);
			put(8, 0.0082);
			put(9, 0.0088); // peak
			put(10, 0.0087); // peak
			put(11, 0.00834);
			put(12, 0.007d);
			put(13, 0.0057);
			put(14, 0.0043);
			put(15, 0.0041);
			put(16, 0.00415);
			put(17, 0.0052);
			put(18, 0.0078);
			put(19, 0.0083);
			put(20, 0.0086); // peak
			put(21, 0.00795);
			put(22, 0.00592);
			put(23, 0.00395);
		}
	};

	public PriceFactor() {
		value = 0.0;
	}

	@Override
	public void update() {
		value = timeToCtPerW.get(Simulation.currentSimulation.getCurrentDateTime().getHour());
	}

}

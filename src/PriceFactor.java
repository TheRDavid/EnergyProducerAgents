import java.util.HashMap;

public class PriceFactor implements EnvironmentFactor<Double>{

	private HashMap<Integer, Double> timeToCtPerW = new HashMap<>() {
		{
			put(0, 0.00225);
			put(1, 0.00225);
			put(2, 0.00226);
			put(3, 0.00218);
			put(4, 0.002d);
			put(5, 0.002d);
			put(6, 0.0028);
			put(7, 0.005d);
			put(8, 0.0082);
			put(9, 0.0088);
			put(10, 0.0087);
			put(11, 0.00834);
			put(12, 0.007d);
			put(13, 0.0057);
			put(14, 0.0043);
			put(15, 0.0041);
			put(16, 0.00415);
			put(17, 0.0052);
			put(18, 0.0078);
			put(19, 0.0083);
			put(20, 0.0086);
			put(21, 0.00795);
			put(22, 0.00592);
			put(23, 0.00395);
		}
	};
	
	@Override
	public Double value() {
		return timeToCtPerW.get(Simulation.currentSimulation.getCurrentDateTime().getHour());
	}

}

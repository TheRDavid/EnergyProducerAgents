import java.util.HashMap;

public class PriceFactor implements EnvironmentFactor<Double>{

	private HashMap<Integer, Double> timeToPrice = new HashMap<>() {
		{
			put(0, 2.25);
			put(1, 2.25);
			put(2, 2.26);
			put(3, 2.18);
			put(4, 2d);
			put(5, 2d);
			put(6, 2.8);
			put(7, 5d);
			put(8, 8.2);
			put(9, 8.8);
			put(10, 8.7);
			put(11, 8.34);
			put(12, 7d);
			put(13, 5.7);
			put(14, 4.3);
			put(15, 4.1);
			put(16, 4.15);
			put(17, 5.2);
			put(18, 7.8);
			put(19, 8.3);
			put(20, 8.6);
			put(21, 7.95);
			put(22, 5.92);
			put(23, 3.95);
		}
	};
	
	@Override
	public Double value() {
		return timeToPrice.get(Simulation.currentSimulation.getCurrentDateTime().getHour());
	}

}

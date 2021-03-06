package environmemnt;
import java.time.LocalDateTime;

public class WindFactor extends EnvironmentFactor<Double>{
	private double averageStrength = 18, maxVariation = 12;
	private double currentStrength = averageStrength, trend = 2 * Math.random() - 1;
public WindFactor() {
	value = 0.0;
}
	/**
	 * Returns speed in kmh
	 * 
	 * @param currentDate
	 * @return new wind speed
	 */
	public void update() {
		double deviation = currentStrength - averageStrength;
		trend = trend > 0 ? Math.random() : -Math.random();
		boolean oppositeSign = (trend < 0 && deviation > 0) || (trend > 0 && deviation < 0);
		// the higher the deviation from average, the larger the probability of the
		// trend to go back
		trend *=  Math.random() / (oppositeSign ? 1.0 : 2.0) < Math.abs(deviation) / maxVariation ? -1 : 1;
		currentStrength += trend;
		value = currentStrength;
	}

}

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Simulation implements Runnable {
	private LocalDateTime currentDateTime;
	private List<EnvironmentFactor<?>> environmentFactors = new ArrayList<>();

	private Strategy strategy;
	public static final Simulation currentSimulation = new Simulation();
	private Consumer[] consumerAgents;
	public boolean running = false, stopped = false;
	public double speed;
	private CentralBox centralBox;
	private NetworkManager networkManager = new NetworkManager();

	private Simulation() { // singleton
	}

	public void start(Strategy s, double speed, Consumer[] consumers, String startDate) {
		if (s.equals(Strategy.VPP))
			centralBox = new VPPBox();
		consumerAgents = consumers;
		this.speed = speed;
		strategy = s;
		currentDateTime = LocalDateTime.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd-HH"));
		environmentFactors.add(new WindFactor());
		environmentFactors.add(new PriceFactor());
		environmentFactors.add(new SunFactor());
		running = true;
		new Thread(this).run();
	}

	@Override
	public void run() {
		while (!stopped) { // keep trying until stopped
			while (running) { // keep running until paused
				try {
					Thread.sleep((int) (1000 / speed));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				currentDateTime = currentDateTime.plusHours(1);
				System.out.println("Time: " + currentDateTime.toString());

				for (Consumer c : consumerAgents) {
					c.act();
				}
				if (!strategy.equals(Strategy.REPOSIT)) {
					centralBox.process();
				}

			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public Strategy getStrategy() {
		return strategy;
	}

	public LocalDateTime getCurrentDateTime() {
		return currentDateTime;
	}

	public NetworkManager getNetworkManager() {
		return networkManager;
	}

	public void step() {
		currentDateTime = currentDateTime.plusHours(1);
	}

	public Object environmentValue(Class factorType) {
		for (EnvironmentFactor<?> factor : environmentFactors)
			if (factor.getClass().equals(factorType))
				return factor.value();
		return null;
	}
}

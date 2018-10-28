import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Simulation implements Runnable {
	private LocalDateTime currentDateTime;
	private List<EnvironmentFactor<?>> environmentFactors = new ArrayList<>();

	private View view;
	private Strategy strategy;
	public static final Simulation currentSimulation = new Simulation();
	private Consumer[] consumerAgents;
	private boolean running = false, stopped = false;
	private double speed;
	private CentralBox centralBox = CentralBox.noneBox;
	private NetworkManager networkManager = new NetworkManager();

	private Simulation() { // singleton
	}

	public void start(Strategy s, double speed, Consumer[] consumers, String startDate, View v) {
		view = v;
		if (s.equals(Strategy.VPP))
			centralBox = new VPPBox();
		if (s.equals(Strategy.COMMUNAL))
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

				for (Consumer c : consumerAgents) {
					c.act();
				}
				centralBox.act();
				view.updateSim();
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

	public double getSpeed() {
		return speed;
	}

	public LocalDateTime getCurrentDateTime() {
		return currentDateTime;
	}

	public NetworkManager getNetworkManager() {
		return networkManager;
	}

	public CentralBox getCentralBox() {
		return centralBox;
	}

	public Consumer[] getConsumerAgents() {
		return consumerAgents;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
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

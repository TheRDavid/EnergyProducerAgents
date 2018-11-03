package main;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import agents.Community;
import environmemnt.EnvironmentFactor;
import environmemnt.NetworkManager;
import environmemnt.PriceFactor;
import environmemnt.SunFactor;
import environmemnt.WindFactor;
import ui.History;
import ui.View;

public class Simulation implements Runnable {
	private LocalDateTime currentDateTime;
	private List<EnvironmentFactor<?>> environmentFactors = new ArrayList<>();

	private View view;
	public static final Simulation currentSimulation = new Simulation();
	private boolean running = false, stopped = false;
	private double speed;
	private NetworkManager networkManager = new NetworkManager();
	private Community[] communities;
	public History hist;

	private Simulation() { // singleton
	}

	public void start(double speed, Community[] coms, String startDate, View v) {
		communities = coms;
		view = v;
		this.speed = speed;
		currentDateTime = LocalDateTime.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd-HH"));
		environmentFactors.add(new WindFactor());
		environmentFactors.add(new PriceFactor());
		environmentFactors.add(new SunFactor());
		running = true;
		hist = new History();
		view.setVisible(true);
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
				for (EnvironmentFactor<?> f : environmentFactors)
					f.update();
				for (Community c : communities) {
					c.act();
				}
				hist.step();
				view.updateSim();
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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

	public void setSpeed(double speed) {
		if (speed == 0)
			running = false;
		else
			running = true;
		this.speed = speed;
	}

	public void step() {
		currentDateTime = currentDateTime.plusHours(1);
	}

	public Community[] getCommunities() {
		return communities;
	}

	public Object environmentValue(Class factorType) {
		for (EnvironmentFactor<?> factor : environmentFactors)
			if (factor.getClass().equals(factorType))
				return factor.value();
		return 0;
	}
}

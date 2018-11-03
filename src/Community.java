
public class Community extends Agent {

	private Strategy strategy;
	private Consumer[] consumerAgents;
	private CentralBox centralBox;
	public final int index;

	public Community(Strategy strategy, Consumer[] agents, int index) {
		this.index = index;
		this.strategy = strategy;
		this.consumerAgents = agents;
		if (this.strategy.equals(Strategy.VPP))
			centralBox = new VPPBox(index);
		else if (this.strategy.equals(Strategy.Communal))
			centralBox = new VPPBox(index);
		else
			centralBox = new RepositBox(strategy, index);
		consumerAgents = agents;
		centralBox.strategy = strategy;
		for (Consumer c : consumerAgents)
			c.setCentralBox(centralBox);
	}

	@Override
	public void act() {
		for (Consumer c : consumerAgents)
			c.act();
		centralBox.act();
	}

	public CentralBox getCentralBox() {
		return centralBox;
	}

	public Consumer[] getConsumerAgents() {
		return consumerAgents;
	}

	public Strategy getStrategy() {
		return strategy;
	}
}

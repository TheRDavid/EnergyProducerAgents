package agents;

import java.util.ArrayList;

abstract public class Agent {
	protected ArrayList<Agent> subAgents = new ArrayList<>();

	public void act() {
		for (Agent a : subAgents)
			a.act();
	}

	public ArrayList<Agent> getSubAgents() {
		return subAgents;
	}
}

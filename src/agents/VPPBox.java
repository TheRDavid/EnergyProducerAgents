package agents;

import main.Simulation;
import main.Strategy;
import ui.History;

public class VPPBox extends CentralBox {
	public VPPBox(int index) {
		super(Strategy.VPP, index);
	}

	@Override
	public void act() {
		double eTotal = totalBalance.generated - totalBalance.consumed;
		// don't sell what we have! Distribute it, just buy what we need. Selling is
		// loosing (tax 'n shit)!
		if (eTotal > 0) {
			totalBalance.money = Simulation.currentSimulation.getNetworkManager().sell(eTotal);
			Simulation.currentSimulation.hist.nextStep[History.CT_EARNED+index * 6] = totalBalance.money;
			Simulation.currentSimulation.hist.nextStep[History.W_SOLD+index * 6] = eTotal;
		} else {
			totalBalance.money = -Simulation.currentSimulation.getNetworkManager()
					.buy(totalBalance.consumed - totalBalance.generated);
			Simulation.currentSimulation.hist.nextStep[History.CT_SPENT+index * 6] = -totalBalance.money;
			Simulation.currentSimulation.hist.nextStep[History.W_BOUGHT+index * 6] = totalBalance.consumed
					- totalBalance.generated;
		}
		noteBalance();

		for (Consumer c : consumers) {
			double share = (c.getBalance().generated - c.getBalance().consumed) / eTotal * totalBalance.money;
			c.getBalance().money += share;
			c.getBalance().generated = 0;
			c.getBalance().consumed = 0;
		}

		totalBalance.generated = 0;
		totalBalance.consumed = 0;
		totalBalance.money = 0;
		consumers.clear();
	}

	@Override
	public String toString() {
		return super.toString();
	}

}

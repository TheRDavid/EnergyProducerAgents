package agents;
import java.util.ArrayList;
import java.util.List;

import main.Strategy;
import main.Util;

public abstract class CentralBox extends Agent {
	private double sold = 0, bought = 0, moneyBalance = 0;
	protected Balance totalBalance = new Balance();
	protected List<Consumer> consumers = new ArrayList<>();
	protected Strategy strategy;
	public final int index;

	public CentralBox(Strategy s, int index) {
		strategy = s;
		this.index = index;
	}

	public void register(Consumer c) {
		consumers.add(c);
		totalBalance.generated += c.getBalance().generated;
		totalBalance.consumed += c.getBalance().consumed;
	}

	protected void noteBalance() {
		if (totalBalance.money > 0) {
			sold += totalBalance.generated - totalBalance.consumed;
		} else {
			bought += totalBalance.consumed - totalBalance.generated;
		}
		moneyBalance += totalBalance.money;
	}

	public Strategy getStrategy() {
		return strategy;
	}

	@Override
	public String toString() {
		return "Sold / Bought: \n" + Util.round(sold / 1000, 2) + " / " + Util.round(bought / 1000, 2) + " kW/h\n"
				+ Util.round(Util.ctToEU(moneyBalance), 2) + " €";
	}

}

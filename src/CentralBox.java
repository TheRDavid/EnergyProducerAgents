import java.util.ArrayList;
import java.util.List;

public abstract class CentralBox {
	private Balance positivePastBalance = new Balance();
	private Balance negativePastBalance = new Balance();
	protected Balance totalBalance = new Balance();
	protected List<Consumer> consumers = new ArrayList<>();

	public void register(Consumer c) {
		consumers.add(c);
		totalBalance.energy += c.getBalance().energy;
	}

	protected void noteBalance() {
		if (totalBalance.energy > 0)
			positivePastBalance.energy += totalBalance.energy;
		else {
			negativePastBalance.energy -= totalBalance.energy;
		}
		if (totalBalance.money > 0)
			positivePastBalance.money += totalBalance.money;
		else {
			negativePastBalance.money -= totalBalance.money;
		}
		System.out.println("ENRG: " + positivePastBalance.energy + " - " + negativePastBalance.energy);
		System.out.println("EURO: " + positivePastBalance.money + " - " + negativePastBalance.money);
	}

	public abstract void process();

}

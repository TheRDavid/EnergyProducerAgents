
public class VPPBox extends CentralBox {

	@Override
	public void process() {
		totalBalance.money = Simulation.currentSimulation.getNetworkManager().trade(totalBalance.energy);
		noteBalance();

		for (Consumer c : consumers) {
			double relativeShare = c.getBalance().energy + totalBalance.energy / consumers.size();
			c.getBalance().money += relativeShare * totalBalance.money;
		}

		totalBalance.energy = 0;
		totalBalance.money = 0;
		consumers.clear();
	}

}

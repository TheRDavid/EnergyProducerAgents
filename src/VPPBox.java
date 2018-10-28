
public class VPPBox extends CentralBox {

	@Override
	public void act() {
		double eTotal = totalBalance.generated - totalBalance.consumed;
		// don't sell what we have! Distribute it, just buy what we need. Selling is
		// loosing (tax 'n shit)!
		if (eTotal > 0)
			totalBalance.money = Simulation.currentSimulation.getNetworkManager().sell(eTotal);
		else
			totalBalance.money = -Simulation.currentSimulation.getNetworkManager()
					.buy(totalBalance.consumed - totalBalance.generated);
		System.out.println("VPP €: " + totalBalance.money);
		noteBalance();

		for (Consumer c : consumers) {
			double share = (c.getBalance().generated - c.getBalance().consumed) / eTotal * totalBalance.money;
			if (c.subAgents.size() > 0)
				System.out.println("Share: " + share);
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
		// TODO Auto-generated method stub
		return super.toString();
	}

}

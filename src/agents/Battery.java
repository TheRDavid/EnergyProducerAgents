package agents;

import main.Util;

public class Battery extends Agent {

	private double energyStored = 0, maxStorage = 3000, minStorageRel = 0.25;

	@Override
	public void act() {
		// passive agent so far
	}

	public double getEnergyStored() {
		return energyStored;
	}

	public double getMaxStorage() {
		return maxStorage;
	}

	public double getMinStorageRel() {
		return minStorageRel;
	}

	public void setEnergyStored(double energyStored) {
		this.energyStored = energyStored;
	}

	@Override
	public String toString() {
		return Util.round(energyStored, 2) + " / " + maxStorage + " W\n"
				+ Util.round(energyStored / maxStorage * 100, 2) + " %";
	}
}

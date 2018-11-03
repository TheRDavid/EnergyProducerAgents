package ui;
import java.util.ArrayList;
import java.util.List;

import main.Simulation;

public class History {
	public static final int W_BOUGHT = 0, W_SOLD = 1, CT_SPENT = 2, CT_EARNED = 3, W_BALANCE = 4, CT_BALANCE = 5;
	// ebought, esold, mneg, mpos
	private static final int histCapacity = 10 * 24*365; // 10 yrs
	private List<double[]> entries = new ArrayList<>();
	public double[] nextStep;

	public History() {
		nextStep = new double[Simulation.currentSimulation.getCommunities().length * 6];
	}

	public void step() {
		for (int i = 0; i < Simulation.currentSimulation.getCommunities().length; i++) {
			double prevEBalance = 0, prevMBalance = 0;
			if (!entries.isEmpty()) {
				prevEBalance = entries.get(entries.size() - 1)[i * 6 + W_BALANCE];
				prevMBalance = entries.get(entries.size() - 1)[i * 6 + CT_BALANCE];
			}
			nextStep[i * 6 + W_BALANCE] = prevEBalance - nextStep[i * 6 + W_BOUGHT] + nextStep[i * 6 + W_SOLD];
			nextStep[i * 6 + CT_BALANCE] = prevMBalance + nextStep[i * 6 + CT_EARNED] - nextStep[i * 6 + CT_SPENT];
		}

		entries.add(nextStep);
		nextStep = new double[Simulation.currentSimulation.getCommunities().length * 6];
	}

	public List<double[]> dataCopy() {
		return new ArrayList<>(entries);
	}
}

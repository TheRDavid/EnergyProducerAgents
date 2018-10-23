public class Main {
	public static void main(String[] args) {
		Simulation.currentSimulation.start(Strategy.VPP, 3, new Consumer[] {}, "2018-01-01-00");
	}
}

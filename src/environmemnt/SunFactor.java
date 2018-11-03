package environmemnt;
import java.util.ArrayList;

import main.Simulation;

public class SunFactor extends EnvironmentFactor<Double> {
	private ArrayList<MonthSunTime> monthlySunTimes = new ArrayList<>();

	public SunFactor() {
		monthlySunTimes.add(new MonthSunTime(31, new double[] { 8 * 60 + 50, 16 * 60 + 37, 8 * 60 + 24, 17 * 60 + 23 },
				new double[] { 7 * 60 + 25, 18 * 60 + 02, 7 * 60 + 06, 18 * 60 + 41 }));
		monthlySunTimes.add(new MonthSunTime(28, new double[] { 8 * 60 + 21, 17 * 60 + 26, 7 * 60 + 29, 18 * 60 + 17 },
				new double[] { 7 * 60 + 03, 18 * 60 + 44, 6 * 60 + 15, 19 * 60 + 31 }));
		monthlySunTimes.add(new MonthSunTime(31, new double[] { 7 * 60 + 27, 18 * 60 + 19, 7 * 60 + 17, 20 * 60 + 12 },
				new double[] { 6 * 60 + 13, 19 * 60 + 33, 6 * 60 + 01, 21 * 60 + 29 }));
		monthlySunTimes.add(new MonthSunTime(30, new double[] { 7 * 60 + 15, 20 * 60 + 14, 6 * 60 + 11, 21 * 60 + 04 },
				new double[] { 5 * 60 + 59, 21 * 60 + 31, 4 * 60 + 42, 22 * 60 + 34 }));
		monthlySunTimes.add(new MonthSunTime(31, new double[] { 6 * 60 + 9, 21 * 60 + 06, 5 * 60 + 25, 21 * 60 + 51 },
				new double[] { 4 * 60 + 39, 22 * 60 + 37, 3 * 60 + 27, 23 * 60 + 50 }));
		monthlySunTimes.add(new MonthSunTime(30, new double[] { 5 * 60 + 24, 21 * 60 + 52, 5 * 60 + 21, 22 * 60 + 05 },
				new double[] { 3 * 60 + 25, 23 * 60 + 52, 3 * 60 + 12, 23 * 60 + 59 }));
		monthlySunTimes.add(new MonthSunTime(31, new double[] { 5 * 60 + 22, 22 * 60 + 05, 5 * 60 + 58, 21 * 60 + 33 },
				new double[] { 3 * 60 + 14, 23 * 60 + 59, 4 * 60 + 18, 23 * 60 + 12 }));
		monthlySunTimes.add(new MonthSunTime(31, new double[] { 6 * 60 + 00, 21 * 60 + 31, 6 * 60 + 49, 20 * 60 + 30 },
				new double[] { 4 * 60 + 21, 23 * 60 + 10, 5 * 60 + 29, 21 * 60 + 50 }));
		monthlySunTimes.add(new MonthSunTime(30, new double[] { 6 * 60 + 51, 20 * 60 + 28, 7 * 60 + 39, 19 * 60 + 20 },
				new double[] { 5 * 60 + 31, 21 * 60 + 47, 6 * 60 + 25, 20 * 60 + 33 }));
		monthlySunTimes.add(new MonthSunTime(31, new double[] { 7 * 60 + 41, 19 * 60 + 17, 7 * 60 + 34, 17 * 60 + 13 },
				new double[] { 6 * 60 + 27, 20 * 60 + 31, 6 * 60 + 18, 18 * 60 + 29 }));
		monthlySunTimes.add(new MonthSunTime(30, new double[] { 7 * 60 + 36, 17 * 60 + 11, 8 * 60 + 26, 16 * 60 + 31 },
				new double[] { 6 * 60 + 19, 18 * 60 + 27, 7 * 60 + 3, 17 * 60 + 54 }));
		monthlySunTimes.add(new MonthSunTime(31, new double[] { 8 * 60 + 27, 16 * 60 + 30, 8 * 60 + 50, 16 * 60 + 36 },
				new double[] { 7 * 60 + 04, 17 * 60 + 54, 7 * 60 + 25, 18 * 60 + 01 }));
		value = 0.0;
	}

	@Override
	public void update() {
		value = monthlySunTimes.get(Simulation.currentSimulation.getCurrentDateTime().getMonthValue() - 1)
				.getIntensity();
	}

	public class MonthSunTime {
		private double[][] dayLightTimes;
		private double[][] twilightTimes;
		private int days;

		/**
		 * All times are taken as a double in the form of minutes (hours * 60 +
		 * minutes), etc.)
		 * 
		 * @param name     of the month
		 * @param days     number
		 * @param dayLight {dayLight start at 1st, dayLight end at 1st, dayLight start
		 *                 at last, dayLight end at last}
		 * @param twilight {twilight start at 1st, twilight end at 1st, twilight start
		 *                 at last, twilight end at last}
		 */
		public MonthSunTime(int days, double[] dayLight, double[] twilight) {
			dayLightTimes = new double[days][2];
			twilightTimes = new double[days][2];
			this.days = days;
			for (int i = 0; i < days; i++) {
				dayLightTimes[i][0] = dayLight[0] + (dayLight[2] - dayLight[0]) / days * i;
				twilightTimes[i][0] = twilight[0] + (twilight[2] - twilight[0]) / days * i;

				dayLightTimes[i][1] = dayLight[1] + (dayLight[3] - dayLight[1]) / days * i;
				twilightTimes[i][1] = twilight[1] + (twilight[3] - twilight[1]) / days * i;
			}
		}

		public int getDays() {
			return days;
		}

		/**
		 * Returns
		 * 
		 * @param date
		 * @return
		 */
		public double getIntensity() {
			int hour = Simulation.currentSimulation.getCurrentDateTime().getHour();
			int day = hour;
			int minute = hour * 60 + Simulation.currentSimulation.getCurrentDateTime().getMinute();
			int monthValue = Simulation.currentSimulation.getCurrentDateTime().getMonthValue();
			double intensity = 0;
			if (dayLightTimes[day][0] <= minute && minute <= dayLightTimes[day][1])
				intensity = 1.0;
			if (twilightTimes[day][0] <= minute && minute <= dayLightTimes[day][0])
				intensity = 1 - (dayLightTimes[day][0] - minute) / (dayLightTimes[day][0] - twilightTimes[day][0]);
			if (dayLightTimes[day][1] <= minute && minute <= twilightTimes[day][1])
				intensity = 1 - (dayLightTimes[day][1] - minute) / (dayLightTimes[day][1] - twilightTimes[day][1]);
			// Consider cloudy months
			switch (monthValue) {
			case 1:
				intensity *= .5; // Jan
				break;
			case 3:
				intensity *= .5; // Mar
				break;
			case 9:
				intensity *= .5; // Sep
				break;
			case 10:
				intensity *= .5; // Oct
				break;
			case 11:
				intensity *= .5; // Nov
				break;
			case 12:
				intensity *= .5; // Dec
				break;
			}
			return intensity;
		}

	}

	public MonthSunTime getMonth(int index) {
		return monthlySunTimes.get(index);
	}
}

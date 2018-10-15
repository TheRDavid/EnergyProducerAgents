import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ManualTesting {
	static double avg = 0;
	static double num = 0;

	public static void main(String[] args) {
		// testWind();
		new SunFactorVisualTester();
	}

	static void testWind() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				WindFactor f = new WindFactor();
				LocalDateTime d = LocalDateTime.now();
				while (true) {
					try {
						Thread.sleep(25);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					double v = f.next(d);
					avg += v;
					System.out.println(v + " \t->\t" + avg / ++num);
				}
			}
		}).start();
	}

	static class SunFactorVisualTester extends JFrame {
		private SunFactor factor = new SunFactor();

		public SunFactorVisualTester() {
			setSize(800, 300);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			add(new MainPanel(), BorderLayout.CENTER);
			setVisible(true);
		}

		class MainPanel extends JPanel {
			@Override
			protected void paintComponent(Graphics gr) {
				super.paintComponent(gr);
				Graphics2D g = (Graphics2D) gr;
				for (int x = 0; x < getWidth(); x++) {
					for (int y = 0; y < getHeight(); y++) {
						int intensityRGB = (int) (dataFromCoords(x, y)[4] * 255);
						g.setColor(new Color(intensityRGB, intensityRGB, intensityRGB));
						g.fillRect(x, y, 1, 1);
					}
				}
			}

			private double[] dataFromCoords(int x, int y) {
				int dayOfYear = (int) (x / (getWidth() / 365.0)) + 1;
				int minute = (int) (y / (getHeight() / 24.0 / 60.0));
				int hour = minute / 60;
				int currentMonthDays = 31;
				int month = 12;
				int daysT = 365;
				int day = currentMonthDays - (daysT - dayOfYear);
				if (dayOfYear <= daysT - currentMonthDays) {
					daysT -= currentMonthDays;
					currentMonthDays = 30;
					month = 11;
					day = currentMonthDays - (daysT - dayOfYear);
				}
				if (dayOfYear <= daysT - currentMonthDays) {
					daysT -= currentMonthDays;
					currentMonthDays = 31;
					month = 10;
					day = currentMonthDays - (daysT - dayOfYear);
				}
				if (dayOfYear <= daysT - currentMonthDays) {
					daysT -= currentMonthDays;
					currentMonthDays = 30;
					month = 9;
					day = currentMonthDays - (daysT - dayOfYear);
				}
				if (dayOfYear <= daysT - currentMonthDays) {
					daysT -= currentMonthDays;
					currentMonthDays = 31;
					month = 8;
					day = currentMonthDays - (daysT - dayOfYear);
				}
				if (dayOfYear <= daysT - currentMonthDays) {
					daysT -= currentMonthDays;
					currentMonthDays = 31;
					month = 7;
					day = currentMonthDays - (daysT - dayOfYear);
				}
				if (dayOfYear <= daysT - currentMonthDays) {
					daysT -= currentMonthDays;
					currentMonthDays = 30;
					month = 6;
					day = currentMonthDays - (daysT - dayOfYear);
				}
				if (dayOfYear <= daysT - currentMonthDays) {
					daysT -= currentMonthDays;
					currentMonthDays = 31;
					month = 5;
					day = currentMonthDays - (daysT - dayOfYear);
				}
				if (dayOfYear <= daysT - currentMonthDays) {
					daysT -= currentMonthDays;
					currentMonthDays = 30;
					month = 4;
					day = currentMonthDays - (daysT - dayOfYear);
				}
				if (dayOfYear <= daysT - currentMonthDays) {
					daysT -= currentMonthDays;
					currentMonthDays = 31;
					month = 3;
					day = currentMonthDays - (daysT - dayOfYear);
				}
				if (dayOfYear <= daysT - currentMonthDays) {
					daysT -= currentMonthDays;
					currentMonthDays = 28;
					month = 2;
					day = currentMonthDays - (daysT - dayOfYear);
				}
				if (dayOfYear <= daysT - currentMonthDays) {
					daysT -= currentMonthDays;
					currentMonthDays = 31;
					month = 1;
					day = currentMonthDays - (daysT - dayOfYear);
				}
				minute %= 60;
				String str = "2018" + (month < 10 ? "-0" : "-") + month + (day < 10 ? "-0" : "-") + day + " "
						+ (hour < 10 ? "0" : "") + hour + ":" + (minute < 10 ? "0" : "") + minute % 60;
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
				LocalDateTime dateTime = LocalDateTime.parse(str, formatter);
				return new double[] { month, day, hour, minute, factor.getMonth(month - 1).getIntensity(dateTime) };
			}

			public MainPanel() {
				addMouseMotionListener(new MouseMotionAdapter() {
					@Override
					public void mouseMoved(MouseEvent e) {
						// TODO Auto-generated method stub
						double[] data = dataFromCoords(e.getX(), e.getY());
						setToolTipText(data[0] + "/" + data[1] + " " + data[2] + ":" + data[3] + " = " + data[4]);
						super.mouseMoved(e);
					}
				});
			}
		}
	}

}

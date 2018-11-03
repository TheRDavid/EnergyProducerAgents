import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.time.LocalDateTime;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ManualTesting {
	static double avg = 0;
	static double num = 0;

	public static void main(String[] args) {
		new VisualTester<Double>(new SunFactor(), 4, 30) {
			@Override
			public void visualize(int x, int y, int width, int height, Graphics g, Double value) {
				int intensityRGB = (int) (value * 255);
				g.setColor(new Color(intensityRGB, intensityRGB, intensityRGB));
				g.fillRect(x, y, width, height);
			}
		};
		new VisualTester<Double>(new WindFactor(), 4, 45) {
			double lines = 5, currentLine = 0, currentX = -1;

			@Override
			public void visualize(int x, int y, int width, int height, Graphics g, Double value) {
				if (currentX != x) {
					currentX = x;
					currentLine = currentLine == lines - 1 ? 0 : currentLine + 1;
				}
				int yP = (int) (y + currentLine * height / lines);
				g.drawString("" + Math.round(value), x, yP);
			}
		};
	}

	abstract static class VisualTester<E> extends JFrame {
		private EnvironmentFactor<E> factor;
		private HashMap<Integer, String> dataCoords = new HashMap<>();
		protected int xS, yS;

		public VisualTester(EnvironmentFactor<E> f, int xScale, int yScale) {
			factor = f;
			xS = xScale;
			yS = yScale;
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			add(new MainPanel(), BorderLayout.CENTER);
			pack();
			setResizable(false);
			setVisible(true);
		}

		public abstract void visualize(int x, int y, int width, int height, Graphics g, E value);

		class MainPanel extends JPanel {

			public MainPanel() {
				setSize(365 * xS, 24 * yS);
				setPreferredSize(new Dimension(365 * xS, 24 * yS));
				setMinimumSize(new Dimension(365 * xS, 24 * yS));
				setMaximumSize(new Dimension(365 * xS, 24 * yS));
				addMouseMotionListener(new MouseMotionAdapter() {
					@Override
					public void mouseMoved(MouseEvent e) {
						// TODO Auto-generated method stub
						int x = e.getX();
						int y = e.getY();
						setToolTipText(dataCoords.get(x * 42 + y * 91 + x * y));
						super.mouseMoved(e);
					}
				});
			}

			@Override
			protected void paintComponent(Graphics gr) {
				super.paintComponent(gr);
				Graphics2D g = (Graphics2D) gr;
				int yStep = getHeight() / 24;
				int xStep = getWidth() / 365;
				for (int x = 0; x < getWidth(); x += xStep) {
					for (int y = 0; y < getHeight(); y += yStep) {
						for (int x0 = x; x0 < x + xStep; x0++) {
							for (int y0 = y; y0 < y + yStep; y0++) {
								dataCoords.put(x0 * 42 + y0 * 91 + x0 * y0,
										Simulation.currentSimulation.getCurrentDateTime().toString() + " -> "
												+ factor.value().toString());
							}
						}
						visualize(x, y, xStep, yStep, g, factor.value());
						Simulation.currentSimulation.step();
					}
				}
			}
		}
	}
}

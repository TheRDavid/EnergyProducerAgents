import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;

public class View extends JFrame {

	private static final Font timeFont = new Font("Arial", Font.BOLD, 24);
	private SimPanel simPanel = new SimPanel();
	private StatusPanel statusPanel = new StatusPanel();
	private ControlPanel controlPanel = new ControlPanel();

	public View() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(statusPanel, BorderLayout.NORTH);
		add(new JScrollPane(simPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
		add(controlPanel, BorderLayout.SOUTH);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private class ControlPanel extends JPanel {
		public ControlPanel() {
			setLayout(new FlowLayout());
			add(new JSlider(0, 100, 3) {
				@Override
				public Dimension getPreferredSize() {
					return new Dimension(400, 30);
				}

				@Override
				protected void processMouseMotionEvent(MouseEvent e) {
					super.processMouseMotionEvent(e);
					Simulation.currentSimulation.setSpeed(getValue());
				}
			});
		}
	}

	private class StatusPanel extends JPanel {
		public StatusPanel() {
			setPreferredSize(new Dimension(1280, 30));
		}

		@Override
		protected void paintComponent(Graphics gr) {
			Graphics2D g = (Graphics2D) gr;
			double sunlight = (double) Simulation.currentSimulation.environmentValue(SunFactor.class);
			double windSpeed = (double) Simulation.currentSimulation.environmentValue(WindFactor.class);
			int c = (int) (230 * sunlight);
			setBackground(new Color(c, c, c));
			super.paintComponent(g);
			g.setColor(Color.WHITE);
			g.setFont(timeFont);
			LocalDateTime dt = Simulation.currentSimulation.getCurrentDateTime();
			g.drawString(
					dt.getDayOfMonth() + "." + dt.getMonthValue() + "." + dt.getYear() + (dt.getHour() < 10 ? " 0" : " ") + dt.getHour() + ":00"
							+ " / "+Util.round(windSpeed, 2) + " km/h",
					5, 25);
		}
	}

	private class SimPanel extends JPanel {

		private PaintJob pJob = new PaintJob(100, 120, 1650);

		public SimPanel() {
			setPreferredSize(new Dimension(1650, 720));
		}

		@Override
		protected void paintComponent(Graphics gr) {
			Graphics2D g = (Graphics2D) gr;
			super.paintComponent(g);
			for (Consumer consumer : Simulation.currentSimulation.getConsumerAgents())
				pJob.paintAgent(g, consumer, 1);
			setPreferredSize(new Dimension(getPreferredSize().width, pJob.paintAgent(g, Simulation.currentSimulation.getCentralBox(), 1)));
			pJob.reset(getParent().getWidth());
		}
	}

	public void updateSim() {
		simPanel.repaint();
		statusPanel.repaint();
	}
}

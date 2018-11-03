import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
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
		add(new JScrollPane(simPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER),
				BorderLayout.CENTER);
		add(controlPanel, BorderLayout.SOUTH);
		pack();
		setLocationRelativeTo(null);
	}

	private class ControlPanel extends JPanel {
		public ControlPanel() {
			setLayout(new FlowLayout());
			add(new JSlider(0, 100, 1) {
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
			JButton overviewButton = new JButton("Overview");
			overviewButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("0");
					new HistoryView(Simulation.currentSimulation.hist.dataCopy());
					System.out.println("0.2");
				}
			});
			add(overviewButton);
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
			List<String> statusMessages = new ArrayList<>();
			// Display Date & Time
			statusMessages.add(dt.getDayOfMonth() + "." + dt.getMonthValue() + "." + dt.getYear()
					+ (dt.getHour() < 10 ? " 0" : " ") + dt.getHour() + ":00");
			// Display Wind Speed
			statusMessages.add(Util.round(windSpeed, 2) + " km/h");
			// Display Current Price
			statusMessages.add(
					(Util.round((double) Simulation.currentSimulation.environmentValue(PriceFactor.class) * 1000, 2))
							+ "ct / kW");
			// Display Peak / Low Time in Price
			statusMessages.add(PriceFactor.peakTimes.contains(dt.getHour()) ? "Peak Price!"
					: (PriceFactor.lowTimes.contains(dt.getHour()) ? "Lowest Price!" : "Normal Price"));
			String statusMessage = "";
			for (String s : statusMessages) {
				statusMessage += s + " | ";
			}
			g.drawString(statusMessage, 5, 25);
		}
	}

	private class SimPanel extends JPanel {

		private Map<Community, PaintJob> paintJobs = new HashMap<Community, PaintJob>();
		private boolean initPaintJobs = true;

		public SimPanel() {
			setPreferredSize(new Dimension(1650, 720));
		}

		@Override
		protected void paintComponent(Graphics gr) {
			super.paintComponent(gr);
			if (initPaintJobs) {
				for (Community community : Simulation.currentSimulation.getCommunities()) {
					paintJobs.put(community, new PaintJob(100, 120));
				}
				initPaintJobs = false;
			}
			Graphics2D g = (Graphics2D) gr;
			int baseY = 0;
			for (Community community : Simulation.currentSimulation.getCommunities()) {
				PaintJob pj = paintJobs.get(community);
				pj.reset(getParent().getWidth(), baseY, g, community.getStrategy());
				for (Consumer c : community.getConsumerAgents())
					pj.paintAgent(g, c, 1);
				int lastY = pj.paintAgent(g, community.getCentralBox(), 1);
				baseY = lastY + 50;
			}
			PaintJob.resetBG();
			setPreferredSize(new Dimension(getPreferredSize().width, baseY));
			revalidate();
		}
	}

	public void updateSim() {
		simPanel.repaint();
		statusPanel.repaint();
	}
}

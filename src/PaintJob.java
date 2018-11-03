import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class PaintJob {
	private int currentX, currentY, margin, maxX, y_extra, y_extra_mx, iconSize, baseY;
	private static final Font agentFont = new Font("Arial", Font.PLAIN, 18);
	private static final Font strategyFont = new Font("Arial", Font.BOLD, 22);
	private Color bga = Color.WHITE, bgb = new Color(235,235,235);
	private static boolean bgab = true;

	public PaintJob(int size, int margin) {
		this.margin = margin;
		this.iconSize = size;
	}

	public int paintAgent(Graphics2D g, Agent a, double shrink) {
		g.setColor(Color.DARK_GRAY);
		g.setFont(new Font(agentFont.getFontName(), agentFont.getStyle(), (int) (agentFont.getSize() * shrink)));
		if (currentX >= maxX - iconSize - margin)
			nextRow();
		int iSize = (int) (iconSize * shrink);
		g.drawImage(IconManager.getImage(a.getClass()), currentX, currentY + y_extra, iSize, iSize, null);
		y_extra += iSize;
		String infoText = a.toString();
		for (String line : infoText.split("\n")) {
			g.drawString(line, currentX + 5, currentY + 25 + y_extra);
			y_extra += 30;
		}

		for (Agent sub : a.subAgents) {
			paintAgent(g, sub, shrink * .75);
		}
		if (shrink == 1) {
			currentX += iSize + margin;
			if (y_extra_mx < y_extra)
				y_extra_mx = y_extra;
			y_extra = 0;
		}
		return currentY + iconSize + y_extra_mx;

	}

	private void nextRow() {
		currentX = margin / 3;
		currentY += y_extra_mx;

		y_extra = 0;
		y_extra_mx = 0;
	}

	public static void resetBG() {
		bgab = true;
	}

	public void reset(int maximumX, int baseY, Graphics g, Strategy s) {
		this.baseY = baseY;
		this.maxX = maximumX;
		currentX = margin / 3;
		currentY = margin / 3 + baseY;
		y_extra = 0;
		y_extra_mx = 0;
		g.setColor(bgab ? bga : bgb);
		bgab = !bgab;
		g.fillRect(0, baseY, maxX, 5000);
		g.setColor(Color.blue);
		g.setFont(strategyFont);
		g.drawString(s.toString(), currentX, currentY - margin / 6);
	}
}

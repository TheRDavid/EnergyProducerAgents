import java.awt.Font;
import java.awt.Graphics2D;

public class PaintJob {
	private int currentX, currentY, margin, maxX, y_extra, y_extra_mx, iconSize;
	private static final Font agentFont = new Font("Arial", Font.PLAIN, 18);

	public PaintJob(int size, int margin, int maximumX) {
		this.margin = margin;
		this.iconSize = size;
		reset(maximumX);
	}

	public int paintAgent(Graphics2D g, Agent a, double shrink) {
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

	public void reset(int maximumX) {
		this.maxX = maximumX;
		currentX = margin / 3;
		currentY = margin / 3;
		y_extra = 0;
		y_extra_mx = 0;
	}
}

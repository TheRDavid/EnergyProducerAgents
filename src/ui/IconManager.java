package ui;
import java.awt.Image;
import java.util.Map;

import javax.swing.ImageIcon;

import agents.Balance;
import agents.Battery;
import agents.Consumer;
import agents.RepositBox;
import agents.SolarPanel;
import agents.VPPBox;
import agents.WindTurbine;

public class IconManager {

	private static final Map<Class, ImageIcon> IconNameToPath = Map.of(Balance.class, new ImageIcon("img/balance.png"),
			Integer.class, new ImageIcon("img/euro.png"), Double.class, new ImageIcon("img/hand-shake.png"),
			Consumer.class, new ImageIcon("img/house.png"), VPPBox.class, new ImageIcon("img/management.png"),
			Boolean.class, new ImageIcon("img/mountains.png"), Float.class, new ImageIcon("img/secret-agent.png"),
			SolarPanel.class, new ImageIcon("img/solar-panel.png"), Battery.class, new ImageIcon("img/battery.png"),
			WindTurbine.class, new ImageIcon("img/wind-turbine.png"));

	public static Image getImage(Class c) {
		if (c.equals(RepositBox.class))
			return new ImageIcon("img/cross.png").getImage(); // NO idea why the map can't handle this case
		return IconNameToPath.get(c).getImage();
	}
}

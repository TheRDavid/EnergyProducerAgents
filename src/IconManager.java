import java.awt.Image;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;


public class IconManager {

	private static final Map<Class, ImageIcon> IconNameToPath = Map.of(
			Balance.class, new ImageIcon("img/balance.png"),
			Integer.class, new ImageIcon("img/euro.png"),
			EnergyProducer.class, new ImageIcon("img/factory.png"),
			Double.class, new ImageIcon("img/hand-shake.png"),
			Consumer.class, new ImageIcon("img/house.png"),
			VPPBox.class, new ImageIcon("img/management.png"),
			Boolean.class, new ImageIcon("img/mountains.png"),
			Float.class, new ImageIcon("img/secret-agent.png"),
			SolarPanel.class, new ImageIcon("img/solar-panel.png"),
			WindTurbine.class, new ImageIcon("img/wind-turbine.png")
			);

	public static Image getImage(Class c) {
		return IconNameToPath.get(c).getImage();
	}
}

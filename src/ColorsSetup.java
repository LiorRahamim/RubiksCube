import java.awt.Color;

/*
 * 
 * this class will setup colors using rgb presentation
 */

public class ColorsSetup { 

	private static final Color[] colors = 
	{ 	
			new Color(255, 255, 255), // white
			new Color(255, 255, 0), // yellow
			new Color(255, 96, 32), // orange
			new Color(208, 0, 0), // red
			new Color(32, 64, 208), // blue
			new Color(0, 144, 0), // green
	};

	public static Color[] getColors() {
		return colors;
	}

}

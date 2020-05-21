/*
 * this class contains the physics
 * to show the cube on the screen
 */
public class Physics {


	// current twisted layer
	private static int twistedLayer; 
	private static int twistedMode;
	// cube facelets
	private static int[][] cube = new int[6][9];
	
	public static int getTwistedLayer() {
		return twistedLayer;
	}
	public static void setTwistedLayer(int twistedLayer1) {
		twistedLayer = twistedLayer1;
	}
	public static int getTwistedMode() {
		return twistedMode;
	}
	public static void setTwistedMode(int twistedMode1) {
		twistedMode = twistedMode1;
	}
	public static int[][] getCube() {
		return cube;
	}
	public static void setCube(int[][] cube) {
		Physics.cube = cube;
	}
	
	
	
}

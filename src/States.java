/*
 * this class contains all types of variables
 * 
 * these variables contain current states of this run
 * 
 */

public class States {

	private static boolean mirrored; // reverse view
	private static boolean editable; // Changeable cube
	private static boolean animating; // animation run
	private static boolean dragging; // progress bar is controlled
	private static boolean demo; // demo mode
	private static int persp; // perspective deformation
	private static boolean natural = true; // layers are not twisted
	private static int align; // cube alignment (top, center, bottom)
	private static boolean scrambled; // is the cube scrambled
	private static boolean hint;
	private static double faceShift;
	private static boolean toTwist; // layer can be twisted
	private static boolean interrupted; // thread was interrupted
	private static boolean restarted; // animation was stopped
	private static boolean twisting; // a user twists a cube layer
	private static boolean spinning; // an animation twists a cube layer
	private static double scale; // cube scale

	
	public static boolean isScrambled() {
		return scrambled;
	}

	public static void setScrambled(boolean scrambled) {
		States.scrambled = scrambled;
	}

	public static boolean isMirrored() {
		return mirrored;
	}

	public static void setMirrored(boolean mirrored) {
		States.mirrored = mirrored;
	}

	public static boolean isEditable() {
		return editable;
	}

	public static void setEditable(boolean editable) {
		States.editable = editable;
	}

	public static boolean isAnimating() {
		return animating;
	}

	public static void setAnimating(boolean animating) {
		States.animating = animating;
	}

	public static boolean isDragging() {
		return dragging;
	}

	public static void setDragging(boolean dragging) {
		States.dragging = dragging;
	}

	public static boolean isDemo() {
		return demo;
	}

	public static void setDemo(boolean demo) {
		States.demo = demo;
	}

	public static int getPersp() {
		return persp;
	}

	public static void setPersp(int persp) {
		States.persp = persp;
	}

	public static boolean isNatural() {
		return natural;
	}

	public static void setNatural(boolean natural) {
		States.natural = natural;
	}

	public static int getAlign() {
		return align;
	}

	public static void setAlign(int align) {
		States.align = align;
	}

	public static boolean isHint() {
		return hint;
	}

	public static void setHint(boolean hint) {
		States.hint = hint;
	}

	public static double getFaceShift() {
		return faceShift;
	}

	public static void setFaceShift(double faceShift) {
		States.faceShift = faceShift;
	}

	public static boolean isToTwist() {
		return toTwist;
	}

	public static void setToTwist(boolean toTwist) {
		States.toTwist = toTwist;
	}

	public static boolean isInterrupted() {
		return interrupted;
	}

	public static void setInterrupted(boolean interrupted) {
		States.interrupted = interrupted;
	}

	public static boolean isRestarted() {
		return restarted;
	}

	public static void setRestarted(boolean restarted) {
		States.restarted = restarted;
	}

	public static boolean isTwisting() {
		return twisting;
	}

	public static void setTwisting(boolean twisting) {
		States.twisting = twisting;
	}

	public static boolean isSpinning() {
		return spinning;
	}

	public static void setSpinning(boolean spinning) {
		States.spinning = spinning;
	}

	public static double getScale() {
		return scale;
	}

	public static void setScale(double scale) {
		States.scale = scale;
	}

}


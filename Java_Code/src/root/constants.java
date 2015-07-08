package root;


public class constants {
	
	// SIMULATION
	
	public static final double CELLSIDES_M = 0.4; //in meters
	public static final double CELLDIAGONALE_M = Math.sqrt(2 * Math.pow(CELLSIDES_M, 2));
	public static final boolean FFM = true;
	
	// GUI
	
	public static int CELLWIDTH = 10; // in pixels
	public static int CELLHEIGHT = 10;
	
	public static final int SLEEPMILLIS = 80;
	public static final int SHOWLABELTIME = 8000;
	public static final int OFFSET = 40;
	public static final int INFOSPACE = 150;
	public static final int NULLSPACE = 40;
	
	public static final boolean MINIONMODE = true;
	
	// MATH
	
	// Normal Distribution
	public static final double MEAN = 1.45873;
	public static final double VARIANCE = 0.1825022;
	
	// Repulsion
	public static double width = 1.5;
	public static double height = 1.0;
	
}

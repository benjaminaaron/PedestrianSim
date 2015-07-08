package root;

public class utils {	
		
	public final static String DIR = System.getProperty("user.dir");
	public final static String SEPARATOR = System.getProperty("file.separator");
	
	public static final String COMMA_DELIMITER = ",";
	public static final String NEW_LINE_SEPARATOR = "\n";
	
	public enum Algorithm {
		EUCLIDEAN,
		DIJKSTRA,
		FASTMARCHING
	}
	
	public enum CellType {
		FREE,
		WALKED,
		OBSTACLE,
		AGENT,
		GOAL,
		SOURCE,
		UNDEFINED
	}
	
	public static String CellTypeToChar(CellType type){
		switch(type){
			case FREE:
				return " ";
			case WALKED:
				return ".";
			case OBSTACLE:
				return "#";
			case AGENT:
				return "O";
			case GOAL:
				return "X";
			case SOURCE:
				return "S";
			case UNDEFINED:
				return "!";				
			default:
				return "!!";
		}
	}
	
	public static int[][] neighbourDirections(boolean FFM){
		if(!FFM)
		{
			int[][] directions = {
				    {  0 , -1 }, // north
				    {  1 , -1 }, // northeast
				    {  1 ,  0 }, // east
				    {  1 ,  1 }, // southeast
				    {  0 ,  1 }, // south
				    { -1 ,  1 }, // southwest
				    { -1 ,  0 }, // west
				    { -1 , -1 } // northwest
				};
			return directions;	
		}
		else
		{
			int[][] directions = {
				    {  0 , -1 }, // north
//				    {  0 , -2 }, // northnorth
				    {  1 ,  0 }, // east
//				    {  2 ,  0 }, // easteast
				    {  0 ,  1 }, // south
//				    {  0 ,  2 }, // southsouth
				    { -1 ,  0 } // west
//				    { -2 ,  0 }  // westwest
				};
			return directions;	
		}
	}
	
	public enum EventType {
		MOVE,
		SOURCE,
		TIMEWATCH,
		UNDEFINED
	}	
	
	public static String round(double val){
		String[] pieces = Double.toString(val).split("\\.");
		String afterpoint = pieces[1];
		if(afterpoint.length() > 2)
			return String.format("%.2f", val);
		else
			if(afterpoint.equals("0"))
				return pieces[0];
			else
				return String.format("%." + afterpoint.length() + "f", val);
	}
	
	/*
	public static int randInt(int min, int max) {
		Random rand = new Random();
		return rand.nextInt((max - min) + 1) + min;
	}
	*/
	
}

package root.view;


public class InkerRep implements InkerInterface {
	
	private static double MAXV;
	
	public int[] getInkCodex(double repulsion)
	{
		int r = 255;
		int g = 255;
		int b = 255;
		
		double factor = repulsion/MAXV;
		g -= g * factor;
		b -= b* factor;
		int[] ret = {r,g,b};
		return ret;
	}
	
	public static void setMaxV(double max){
		MAXV = max;
	}
}
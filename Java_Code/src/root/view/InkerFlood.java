package root.view;


public class InkerFlood implements InkerInterface {
	
	private static double MAXV;
	
	public int[] getInkCodex(double flood)
	{
		int r = 255;
		int g = 255;
		int b = 255;
		
		double factor = flood/MAXV;
		if(factor > 0)
		{
			g -= g * factor/2;
			b -= b* factor;
		}
		int[] ret = {r,g,b};
		return ret;
	}
	
	public static void setMaxV(double max){
		MAXV = max;
	}
}
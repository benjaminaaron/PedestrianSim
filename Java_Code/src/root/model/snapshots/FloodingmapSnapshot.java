package root.model.snapshots;


public class FloodingmapSnapshot {
	
	public double time;
	public int[][] floodingmap;
	public String pathToGoal;

	public FloodingmapSnapshot(double time, int[][] floodingmap, String pathToGoal){
		this.time = time;
		this.floodingmap = floodingmap;		
		this.pathToGoal = pathToGoal;
	}
	
	public boolean hasData(){
		return floodingmap != null;
	}
	
	public int getMax(){
		int max = 0;
		for(int y = 0; y < floodingmap[0].length; y++)
			for(int x = 0; x < floodingmap.length; x++)
				if(floodingmap[x][y] > max)
					max = floodingmap[x][y];		
		return max;
	}
	
	public String floodingmapToString(){
		if(hasData()){
			int columns = floodingmap.length;
			int rows = floodingmap[0].length;
			String str = "";
			for(int y = 0; y < rows; y++){
				for(int x = 0; x < columns; x++){
					int val = floodingmap[x][y];
					str += "[" + (val >= 0 && val < 10 ? " " : "") + val + "]";
				}
				str += "\n";
			}
			return str;
		}
		else
			return "no floodingmap";
	}
}

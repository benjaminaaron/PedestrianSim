package root.model.snapshots;


public class RepulsionmapSnapshot {
	
	public double time;
	public double[][] valuemap;

	public RepulsionmapSnapshot(double time, double[][] valuemap){
		this.time = time;
		this.valuemap = valuemap;		
	}
	
	public String valuemapToString(){
		int columns = valuemap.length;
		int rows = valuemap[0].length;
		
		String str = "";
		for(int x = 0; x < columns; x++){
			for(int y = 0; y < rows; y++)
				str += "[" + valuemap[x][y] + "]";
			str += "\n";
		}
		return str;
	}
}

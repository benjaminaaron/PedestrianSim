package root.model.snapshots;

import root.utils.CellType;


public class MapSnapshot {
	
	public double time;
	public CellType[][] mapPure;
	
	public MapSnapshot(double time, CellType[][] mapPure){
		this.time = time;		
		this.mapPure = mapPure;
	}
	
}

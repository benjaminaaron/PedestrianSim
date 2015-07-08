package root.model.snapshots;

import root.model.mapobjects.Cell;
import root.utils;

public class NeighbourSnapshot {
	
	public Cell cell;
	public double NUTZEN;
	public double repulsion;
	public double euclideanDistToGoal;
	public int dijkstraHops;	
	public double fastmarchingTime;
	public boolean isInBestChoices = false;
	public boolean isChosenCell = false;
	
	public NeighbourSnapshot(Cell cell, double NUTZEN, double repulsion, double euclideanDistToGoal, int dijkstraHops, double fastmarchingTime){
		this.cell = cell;		
		this.NUTZEN = NUTZEN;
		this.repulsion = repulsion;
		this.euclideanDistToGoal = euclideanDistToGoal;
		this.dijkstraHops = dijkstraHops;
		this.fastmarchingTime = fastmarchingTime;
	}

	public void isInBestChoices() {
		isInBestChoices = true;
	}
	
	public void isChosenCell(){
		isChosenCell = true;
	}
	
	public String toString(){
		String opening = "";
		String closing = "";
		if(isInBestChoices)
			if(isChosenCell){
				opening = "<i><b>";
				closing = "</i></b>";
			}
			else{
				opening = "<i>";
				closing = "</i>";
			}
		return opening + cell.coordsStr() + "[" + utils.round(euclideanDistToGoal) + "+" + utils.round(repulsion) + "+" + dijkstraHops + "+" + utils.round(fastmarchingTime) + "=" + utils.round(NUTZEN) + "]:" + (isInBestChoices ? "1": "0") + (isChosenCell ? "1" : "0") + closing;
	}

}

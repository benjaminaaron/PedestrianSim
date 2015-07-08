package root.model.snapshots;

import java.util.ArrayList;

import root.model.mapobjects.Cell;

public class AgentSnapshot {

	public Cell prevCell, nowCell;
	public double cellarrivaltime; // it will beam there right away...
	public double celldeparturetime; //(=waiting time)... but then has to wait for the next step as long as it would have taken him to walk to this cell (this is how we agreed to doing it i guess?)	
	public ArrayList<NeighbourSnapshot> evaluatedNeighbours;
		
	public AgentSnapshot(Cell prevCell, Cell nowCell, double cellarrivaltime, double celldeparturetime, ArrayList<NeighbourSnapshot> evaluatedNeighbours){
		this.prevCell = prevCell;
		this.nowCell = nowCell;
		this.cellarrivaltime = cellarrivaltime;
		this.celldeparturetime = celldeparturetime;
		this.evaluatedNeighbours = evaluatedNeighbours;
	}
	
	public String toString(){
		if(cellarrivaltime == 0 && celldeparturetime == 0)
			return "no computation has been done up to this point";
		return evaluatedNeighboursToString();
	}

	public String evaluatedNeighboursToString() {
		if(evaluatedNeighbours != null){
			String str = "i stood on " + prevCell.coordsStr() + " and evaluated these neighbours:<br>";
			for(NeighbourSnapshot neighbourSnapshot : evaluatedNeighbours)
				str += neighbourSnapshot + "<br>";	
			return str.substring(0, str.length() - 1);
		}
		else
			return "no neighbour cells were free";
	}
	
	public NeighbourSnapshot getChosenOne(){
		if(evaluatedNeighbours == null)
			return null;
		else
			for(NeighbourSnapshot neighbour: evaluatedNeighbours)
				if(neighbour.isChosenCell)
					return neighbour;
		return null;
	}
	
}

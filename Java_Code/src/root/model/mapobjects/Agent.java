package root.model.mapobjects;

import java.util.ArrayList;
import java.util.Random;

import root.constants;
import root.model.io.Log;
import root.model.math.Distribution;
import root.model.math.dijkstra.Graph;
import root.model.events.Event;
import root.model.events.MoveEvent;
import root.model.snapshots.AgentSnapshot;
import root.model.snapshots.NeighbourSnapshot;
import root.model.Map;
import root.model.Simulation;
import root.utils;
import root.utils.Algorithm;

public class Agent {
	
	public double speed;
	public int ID; 
	public Cell mycell;
	public Cell chosenGoal;
	public Map map;
	public ArrayList<AgentSnapshot> snapshots = new ArrayList<>();
	public Graph graph;
	public boolean wasMeasured = false;
	
	
	public Agent(int ID, Cell mycell, Map map){
		this.ID = ID;
		this.mycell = mycell;
		this.chosenGoal = null;
		this.map = map;
		this.speed = Distribution.getVelocityValue(); // free-flow velocity from normal distribution in m/s
		if(Simulation.algo == Algorithm.DIJKSTRA)
			graph = new Graph();
		snapshots.add(new AgentSnapshot(null, mycell, 0, 0, null));
	}
	
	public String toString(){
		return ID + "_" + mycell.col + "/" + mycell.row + "_" + utils.round(speed);
	}
	
	public Event getNextMoveEvent(MoveEvent prevMoveEvent) {
		double cellarrivaltime = prevMoveEvent.primaryvalue;
		ArrayList<Cell> neighbours = map.getWalkableNeighbourCells(mycell);	
		
		if(Simulation.algo != Algorithm.FASTMARCHING)
			if(chosenGoal == null){
				chosenGoal = getClosestGoal(mycell);
				if(Log.ON)
					Log.write("agent " + this + " chooses goal " + chosenGoal.coordsStr());
			}	
			
		if(neighbours.size() > 0){ //cells to step on do exist
			
			ArrayList<NeighbourSnapshot> evaluatedNeighbours = new ArrayList<>();
			ArrayList<Integer> equallyBestChoicesIndize = new ArrayList<>();
			double maxNutzen = - Integer.MAX_VALUE;
						
			for(int i = 0; i < neighbours.size(); i++){
				Cell cell = neighbours.get(i);
				
				double repulsion = 0; 	
				double euclideanDistToGoal = 0;
				int dijkstraHops = 0;
				double fastmarchingTime = 0;
				
				switch(Simulation.algo){
					case EUCLIDEAN:
						euclideanDistToGoal = getDist(chosenGoal, cell);
						repulsion = map.getRepulsionValue(this, cell);
						break;
					case DIJKSTRA:
						graph.reset();
						dijkstraHops = graph.getHopsToGoal(cell, chosenGoal, map.getPureMap(), false, ID, false);
						if(dijkstraHops == 1) // fallback plan if dijkstra can't deliver
							euclideanDistToGoal = getDist(chosenGoal, cell);
						repulsion = map.getRepulsionValue(this, cell);
						break;
					case FASTMARCHING:
						double[][] potentialMap = Simulation.fastMarchingObj.getPotentialMapOfFFM();
						fastmarchingTime = - potentialMap[cell.col][cell.row];
						break;
					default:
						System.out.println("error: unkown choice of algorithm");
						break;					
				}
				
				double NUTZEN = repulsion + euclideanDistToGoal + dijkstraHops + fastmarchingTime;
				
				evaluatedNeighbours.add(new NeighbourSnapshot(cell, NUTZEN, repulsion, euclideanDistToGoal, dijkstraHops, fastmarchingTime));	
				
				if(NUTZEN > maxNutzen){
					maxNutzen = NUTZEN;
					equallyBestChoicesIndize.clear();
				}	
				if(NUTZEN == maxNutzen)
					equallyBestChoicesIndize.add(i);
			}
								
			int chosenCellIndex = equallyBestChoicesIndize.get(0);
			if(equallyBestChoicesIndize.size() > 1)
				chosenCellIndex = equallyBestChoicesIndize.get(new Random().nextInt(equallyBestChoicesIndize.size()));	
					
			for(int i = 0; i < equallyBestChoicesIndize.size(); i++)
				evaluatedNeighbours.get(equallyBestChoicesIndize.get(i)).isInBestChoices();	
			evaluatedNeighbours.get(chosenCellIndex).isChosenCell();
			
			Cell chosenCell = neighbours.get(chosenCellIndex);	
			
			boolean isDiagonal = Math.abs(chosenCell.col - mycell.col) + Math.abs(chosenCell.row - mycell.row) == 2;
			
			double distance = isDiagonal ? constants.CELLDIAGONALE_M : constants.CELLSIDES_M;
			double timeNeededForMove = distance / speed;
			double celldeparturetime = cellarrivaltime + timeNeededForMove;
			
			if(Log.ON)
				Log.write("agent " + this + " chooses cell " + chosenCell.coordsStr() + ", diagonal: " + isDiagonal + ", he needs " + utils.round(timeNeededForMove) + "s = " + utils.round(speed) + "m/s : " + utils.round(distance) + "m, so next move is at " + utils.round(celldeparturetime) + "s");
			
			// make changes in map
			map.agentLeftCell(mycell);
			map.makeAgentCell(chosenCell);
			
			if(Log.ON)
				Log.write("\n" + map);
			
			snapshots.add(new AgentSnapshot(mycell, chosenCell, cellarrivaltime, celldeparturetime, evaluatedNeighbours));
					
			
			if(Simulation.algo == Algorithm.FASTMARCHING){
				if(Simulation.isGoalCell(chosenCell))
					prevMoveEvent.isGoalReachingEvent = true;
			}
			else 
				if(chosenCell == chosenGoal)
					prevMoveEvent.isGoalReachingEvent = true;
			
			mycell = chosenCell;
			prevMoveEvent.primaryvalue = celldeparturetime;
			
			return prevMoveEvent;
		}
		else { // no cells to step on	
			double idletime = constants.CELLSIDES_M / speed; // waiting time set to the time it would take to move to a non-diagonal cell
			double idletimeover = cellarrivaltime + idletime;
			
			snapshots.add(new AgentSnapshot(mycell, mycell, cellarrivaltime, idletimeover, null));
			
			prevMoveEvent.primaryvalue = idletimeover;	
			return prevMoveEvent;	
		}
	}
	
	private double getDist(Cell cell1, Cell cell2){
		return - Math.sqrt(Math.pow(cell1.col - cell2.col, 2) + Math.pow(cell1.row - cell2.row, 2));
	}
	
	private Cell getClosestGoal(Cell pos){		
		ArrayList<Cell> equallyCloseGoals = new ArrayList<>();
		double maxDist = - Integer.MAX_VALUE;
		for(Cell goalcell : map.goals){
			double dist = getDist(pos, goalcell);
			if(dist > maxDist){
				maxDist = dist;
				equallyCloseGoals.clear();
			}
			if(dist == maxDist)
				equallyCloseGoals.add(goalcell);
		}
		Cell chosenGoal = equallyCloseGoals.get(0);
		if(equallyCloseGoals.size() > 1)
			chosenGoal = equallyCloseGoals.get(new Random().nextInt(equallyCloseGoals.size()));	
		return chosenGoal;
	}
	
	public AgentSnapshot getMatchingSnapshot(int col, int row, double currenttime){
		for(AgentSnapshot snapshot : snapshots)
			if(snapshot.nowCell.col == col && snapshot.nowCell.row == row)			
				if(snapshot.cellarrivaltime <= currenttime && snapshot.celldeparturetime >= currenttime)
					return snapshot;
		return null;		
	}
/*
	public String showHistory() {
		String str = "history of agent " + this + "\n";	
		for(AgentSnapshot snapshot : snapshots)
			str += snapshot + "\n";
		return str;
	}
*/ 
}

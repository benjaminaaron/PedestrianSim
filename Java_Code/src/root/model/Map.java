package root.model;

import java.io.IOException;
import java.util.ArrayList;

import root.model.mapobjects.Agent;
import root.model.mapobjects.Cell;
import root.model.math.Repulsion;
import root.constants;
import root.utils;
import root.utils.CellType;
import root.model.io.MapImporter;
import root.model.snapshots.MapSnapshot;
import root.model.snapshots.RepulsionmapSnapshot;

public class Map {
		
	public Cell[][] cells;
	public int columns, rows;
	
	public ArrayList<Cell> agents = new ArrayList<>();
	public ArrayList<Cell> sources = new ArrayList<>();
	public ArrayList<Cell> goals = new ArrayList<>();
	
	//public ArrayList<MapSnapshot> mapSnapshots = new ArrayList<>();
	public ArrayList<MapSnapshot> timedSnapshots = new ArrayList<>();
	
	public ArrayList<RepulsionmapSnapshot> repulsionmapSnapshots = new ArrayList<>();
	public double globalRepulsionMinimum = 0;
	
	
	public Map(String mapname){	
		CellType[][] mapImported = null;
		try {	
			mapImported = MapImporter.readBMP(mapname);
		} catch (IOException e) {e.printStackTrace();}		
		
		columns = mapImported.length;
		rows = mapImported[0].length;
		cells = new Cell[columns][rows];
			
		for(int x = 0; x < columns; x++)
			for(int y = 0; y < rows; y++){
				cells[x][y] = new Cell(mapImported[x][y], x, y); // switch-case might be nicer?				
				if(mapImported[x][y] == CellType.AGENT)
					agents.add(cells[x][y]);
				if(mapImported[x][y] == CellType.SOURCE)
					sources.add(cells[x][y]); 
				if(mapImported[x][y] == CellType.GOAL)
					goals.add(cells[x][y]); 
			}
	}
	
	public void agentLeftCell(Cell cell) {
		if(Simulation.isGoalCell(cell))
			cell.type = CellType.GOAL;
		else
			if(Simulation.isSourceCell(cell))
				cell.type = CellType.SOURCE;
			else
				cell.type = CellType.WALKED;
	}

	public void makeAgentCell(Cell cell) {
		cell.type = CellType.AGENT;
	}
	
	/*
	public void takeSnapshot(double time, HashMap<String, String> latestAgentThoughts){		
		mapSnapshots.add(new MapSnapshot(time, getPureMap(), latestAgentThoughts));
	}
	*/
	
	public void takeTimedSnapshot(double time){	
		timedSnapshots.add(new MapSnapshot(time, getPureMap()));
		takeRepulsionmapSnapshot(time);
	}
	
	public CellType[][] getPureMap(){
		CellType[][] mapPure = new CellType[columns][rows];	
		for(int x = 0; x < columns; x++)
			for(int y = 0; y < rows; y++)
				mapPure[x][y] = cells[x][y].type;	
		return mapPure;
	}
	
	public double getLastTimedSnapshotTime(){
		return timedSnapshots.get(timedSnapshots.size() - 1).time;
	}
	
	public void takeRepulsionmapSnapshot(double time){
		double[][] repulsionMap = new double[columns][rows];	
		for(int x = 0; x < columns; x++)
			for(int y = 0; y < rows; y++){
				Cell cell = cells[x][y];
				if(cell.type == CellType.OBSTACLE){
					repulsionMap[x][y] = 0.0;
				} else {
					double nutzen = getRepulsionValue(null, cell);
					if(nutzen < globalRepulsionMinimum)
						globalRepulsionMinimum = nutzen;
					repulsionMap[x][y] = nutzen;
				}
			}
		repulsionmapSnapshots.add(new RepulsionmapSnapshot(time, repulsionMap));		
	}
	
	// Abstaende und Abstossung zu jedem anderen Agent berechnen
	public double getRepulsionValue(Agent caller, Cell cell) {
		// cell ist die aktuelle Position vom Agent der grad dran ist
		double repelValue = 0;
		// hier for-schleife ueber die Agentsliste, Agent rausholen und dessen Ort, Abstand berechnen -> dann Abstossung berechnen
		int positionCol, positionRow, myCol, myRow, xValue, yValue;
		double xVal, yVal, dist;
		for(Agent agent : Simulation.activeAgents){
			if(agent != caller){
				positionCol = agent.mycell.col;
				positionRow = agent.mycell.row;
				myCol = cell.col;
				myRow = cell.row;
				xValue = Math.abs(positionCol - myCol);
				yValue = Math.abs(positionRow - myRow);
				xVal = constants.CELLSIDES_M * xValue;
				yVal = constants.CELLSIDES_M * yValue;
				dist = Math.sqrt(Math.pow(xVal, 2) + Math.pow(yVal,2));
				repelValue += Repulsion.repelPotential(dist);
				//System.out.println("Repel: " + repelValue);
			}
		}
		return repelValue;		
	}
		
	public boolean isGoalCell(Cell chosenCell) {
		for(Cell cell : goals)
			if(cell == chosenCell)
				return true;		
		return false;
	}
	
	public String toString(){
		String str = "    ";
		for(int x = 0; x < columns; x++)
			str += x + (x < 10 ? "  " : " ");
		str += "\n";	
		for(int y = 0; y < rows; y++){
			str += (y < 10 ? " " : "") + y + " ";
			for(int x = 0; x < columns; x++)
				str += "[" + cells[x][y] + "]";
			str += "\n";
		}
		return str;
	}
	
	public boolean hasAtLeastOneFreeNeighbourCells(Cell cell){
		int x = cell.col;
		int y = cell.row;
		int[][] dir = utils.neighbourDirections(false);
		for(int i = 0; i < 8; i++){
			int nx = x + dir[i][0];
			int ny = y + dir[i][1];	
			if(nx >= 0 && nx < columns && ny >= 0 && ny < rows){
				Cell c = cells[nx][ny];
				if(c.type == CellType.FREE || c.type == CellType.WALKED || c.type == CellType.GOAL)
					return true;
			}
		
		}
		return false;
	}
	
	public ArrayList<Cell> getWalkableNeighbourCells(Cell cell) {
		ArrayList<Cell> neighbours = new ArrayList<>();
		int x = cell.col;
		int y = cell.row;
		int[][] dir = utils.neighbourDirections(false);
		for(int i = 0; i < dir.length; i++){
			int nx = x + dir[i][0];
			int ny = y + dir[i][1];
			if(nx >= 0 && nx < columns && ny >= 0 && ny < rows){
				Cell c = cells[nx][ny];
				if(c.type == CellType.FREE || c.type == CellType.WALKED || c.type == CellType.GOAL)
					neighbours.add(c);
			}
		}
		return neighbours;
	}
	
	public ArrayList<Cell> doSomeMagic(Cell cell) {
		ArrayList<Cell> neighbours = new ArrayList<>();
		int x = cell.col;
		int y = cell.row;
		int[][] dir = utils.neighbourDirections(constants.FFM);
		for(int i = 0; i < dir.length; i++){
			int nx = x + dir[i][0];
			int ny = y + dir[i][1];
			if(nx >= 0 && nx < columns && ny >= 0 && ny < rows){
				Cell c = cells[nx][ny];
				if(c.type != CellType.OBSTACLE)
					neighbours.add(cells[nx][ny]);
			}
		}
		return neighbours;
	}

}

package root.model.math.dijkstra;

import java.util.ArrayList;
import java.util.Collections;

import root.constants;
import root.utils;
import root.utils.CellType;
import root.model.mapobjects.Cell;
import root.model.snapshots.FloodingmapSnapshot;


public class Graph {
	
	public CellType[][] cells;
	public int columns, rows, goalcol, goalrow;
	public ArrayList<Node> nodes = new ArrayList<>();
	public ArrayList<Edge> edges = new ArrayList<>();
	public Fringe fringe = new Fringe();
	public boolean goalFound = false;
	public Node goal = null;
	public boolean export;
	public int[][] floodingmap = null;
	public String pathToGoal = "no path to the goal";
	
	public void reset(){
		nodes.clear();
		if(export)
			edges.clear();
		fringe.reset();
		goalFound = false;
		goal = null;
		floodingmap = null;
		pathToGoal = "no path to the goal";
	}
	
	public FloodingmapSnapshot getFloodingmapSnapshot(double time, Cell agentsChosenGoal, int agentID, Cell startcell, CellType[][] cells){				
		reset();
		getHopsToGoal(startcell, agentsChosenGoal, cells, false, agentID, true);
		return new FloodingmapSnapshot(time, floodingmap, pathToGoal);
	}
	
	public int getHopsToGoal(Cell startcell, Cell goalcell, CellType[][] cells, boolean export, int agentID, boolean recordValuemap) {
		if(startcell == goalcell)
			return 0; // best choice, GO THERE RIGHT NOW you fool :)
		
		this.goalcol = goalcell.col;
		this.goalrow = goalcell.row;
		this.cells = cells;
		this.export = export;
		
		columns = cells.length;
		rows = cells[0].length;
		
		int i = 0;
		Node root = new Node(i, nodes.size(), null, startcell.col, startcell.row, 0);
		root.isRoot = true;
		root.isOnPath = true; // just for completeness :)
		nodes.add(root);
		fringe.add(root);
		
		while(!goalFound && !fringe.isEmpty())
			processChildren(++ i, fringe.getNext());
		
		if(fringe.isEmpty())
			return 1;
		
		if(export)
			Exporter.doExport(nodes, edges, "dijkstra_agent" + agentID + "_" + nodes.size() + "nodes");
		
		if(recordValuemap){
			floodingmap = new int[columns][rows];
			for(int y = 0; y < rows; y++)
				for(int x = 0; x < columns; x++)
					floodingmap[x][y] = -1;
			for(Node node : nodes)
				floodingmap[node.col][node.row] = node.level;		
			ArrayList<Node> nodePath= new ArrayList<>();	
			Node node = goal;
			nodePath.add(goal);
			while(node.parent.parent != null){
				node = node.parent;
				nodePath.add(node);
			}
			Collections.reverse(nodePath);
			pathToGoal = root.coords() + " > ";
			for(Node n : nodePath)
				pathToGoal += n.coords() + " > ";
			pathToGoal = pathToGoal.substring(0, pathToGoal.length() - 3);			
		}

		return - goal.level;
		
		/*
		//bonus-cell approach
		int coldiff = node.col - root.col; // node is first after start after while-loop
		int rowdiff = node.row - root.row;
		return utils.getDirection(coldiff, rowdiff);
		 
		//nodePath approach
		ArrayList<Node> nodePath= new ArrayList<>();	
		Node node = goal;
		nodePath.add(goal);
		int hops = 0;
		while(node.parent.parent != null){
			node = node.parent;
			nodePath.add(node);
			hops ++;
		}
		
		System.out.println("agent_" + agentID + " hops: " + hops + " from cell " + start.col + "/" + start.row);

		Collections.reverse(nodePath);
		
		for(Node n : nodePath)
			System.out.println(n);
		
		ArrayList<Direction> path = new ArrayList<>();	
		
		for(int j = 0; j < nodePath.size() - 1; j++){	
			Node on = nodePath.get(j);
			on.isOnPath = true; // root-color overwrites this
			Node next = nodePath.get(j + 1);
			int coldiff = next.col - on.col;
			int rowdiff = next.row - on.row;
			path.add(utils.getDirection(coldiff, rowdiff));
		}
		
		if(export)
			Exporter.doExport(nodes, edges, "dijkstra_" + nodes.size() + "nodes");
		
		return path.get(0);
		
		
		// this was in utils for the direction-stuff
		
		public enum Direction{
			NORTH,
			NORTHEAST,
			EAST,
			SOUTHEAST,
			SOUTH,
			SOUTHWEST,
			WEST,
			NORTHWEST,
			GOALNOTFOUND,
			UNDEFINED
		}
		
		public static Direction getDirection(int col, int row){
			if(col == 0 && row == -1)
				return Direction.NORTH;
			if(col == 1 && row == -1)
				return Direction.NORTHEAST;
		    if(col == 1 && row == 0)
		    	return Direction.EAST;
		    if(col == 1 && row == 1)
		    	return Direction.SOUTHEAST;
		    if(col == 0 && row == 1)
		    	return Direction.SOUTH;
		    if(col == -1 && row == 1)
		    	return Direction.SOUTHWEST;
		    if(col == -1 && row == 0)
		    	return Direction.WEST;
		    if(col == -1 && row == -1)
		    	return Direction.NORTHWEST;		
			return Direction.UNDEFINED;
		}
		
		*/
	}

	public boolean contains(int col, int row) {
		for(Node n : nodes)
			if(n.col == col && n.row == row)
				return true;		
		return false;
	}

	private void processChildren(int step, Node parent){
		int[][] dir = utils.neighbourDirections(false);
		int col = parent.col;
		int row = parent.row;
		
		for(int i = 0; i < 8; i++){
			int newcol = col + dir[i][0];
			int newrow = row + dir[i][1];	
			if(newcol >= 0 && newcol < columns && newrow >= 0 && newrow < rows){
				CellType ct = cells[newcol][newrow];
				if(ct == CellType.FREE || ct == CellType.WALKED || ct == CellType.GOAL || (newcol == goalcol && newrow == goalrow)){ // stepping over another goal (not the chosen one) is allowed, if our goal is below an agent we still want to target it
					if(!contains(newcol, newrow)){
						boolean isDiagonal = Math.abs(newcol - col) + Math.abs(newrow - row) == 2;		
						double dist = isDiagonal ? constants.CELLDIAGONALE_M : constants.CELLSIDES_M;	
						Node child = new Node(step, nodes.size(), parent, newcol, newrow, parent.nodevalue + dist); 
						if(export)
							edges.add(new Edge(parent, child, dist));
						nodes.add(child);
						fringe.add(child);
						if(newcol == goalcol && newrow == goalrow){
							goalFound = true;
							child.isGoal = true;
							goal = child;
						}	
					}
				}
			}
		}
	}
}
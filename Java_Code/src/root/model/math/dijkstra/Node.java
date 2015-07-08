package root.model.math.dijkstra;

import root.utils;


public class Node {
	
	public int stepadded;
	public int ID;
	public Node parent;
	public int col, row;
	public double nodevalue;
	public boolean isRoot = false;
	public boolean isGoal = false;
	public boolean isOnPath = false;
	public int level = 0;
	
	public Node(int stepadded, int ID, Node parent, int col, int row, double nodevalue){
		this.stepadded = stepadded;
		this.ID = ID;
		this.parent = parent;
		if(parent != null)
			level = parent.level + 1;
		this.col = col;
		this.row = row;
		this.nodevalue = nodevalue;
	}
	
	public String coords(){
		return "[" + col + "/" + row + "]";
	}
	
	public String graphmlColor(){
		if(isRoot)
			return "#FFFFAA";
		if(isGoal)
			return "#AA0000";
		if(isOnPath)
			return "#FF0000";
		return "#FFFF00";
	}
	
	public String graphmlLabel(){
		return col + "/" + row + "\n" + utils.round(nodevalue) + "\n" + level;
	}
	
	public String toString(){
		return "node" + ID + ": " + col + "/" + row + " " + utils.round(nodevalue) + " " + (isRoot ? "1" : "0") + (isGoal ? "1" : "0");
	}
}
package root.model.math.dijkstra;

import java.util.ArrayList;
import java.util.Random;


public class Fringe {
	
	ArrayList<Node> nodes = new ArrayList<>();

	public void add(Node node){
		int insertIndex = nodes.size();
		int i = 0;
		boolean spotFound = false;
		while(!spotFound && i < nodes.size()){
			if(node.nodevalue < nodes.get(i).nodevalue){
				insertIndex = i;
				spotFound = true;
			}
			i++;
		}
		nodes.add(insertIndex, node);
	}
	
	public ArrayList<Node> getCheapestNodes(){
		ArrayList<Node> arr = new ArrayList<>();
		Node node = nodes.get(0);
		double minCost = node.nodevalue;
		int i = 0;
		boolean done = false;
		while(!done && i < nodes.size()){
			node = nodes.get(i);
			if(node.nodevalue == minCost)
				arr.add(node);
			else
				done = true;
			i++;
		}
		return arr;
	}
	
	public Node getNext(){ // get one of the equally cheapest nodes
		ArrayList<Node> cheapestNodes = getCheapestNodes();
		return nodes.remove(nodes.indexOf(cheapestNodes.get(new Random().nextInt(cheapestNodes.size()))));	
		//return nodes.remove(0);
	}
	
	public void reset(){
		nodes.clear();
	}
	
	public boolean isEmpty(){
		return nodes.size() == 0;
	}

}

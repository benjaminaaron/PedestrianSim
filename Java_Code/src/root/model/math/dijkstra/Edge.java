package root.model.math.dijkstra;


public class Edge {
	
	public Node parent;
	public Node child;
	public double edgevalue;
	
	public Edge(Node parent, Node child, double edgevalue){
		this.parent = parent;
		this.child = child;
		this.edgevalue = edgevalue;
	}
	
}
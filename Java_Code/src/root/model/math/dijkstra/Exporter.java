package root.model.math.dijkstra;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import root.utils;


public class Exporter {

    private static PrintWriter output;

    public static void doExport(ArrayList<Node> nodes, ArrayList<Edge> edges, String filename) {
    	
        try {
			output = new PrintWriter(filename + ".graphml");
		} catch (FileNotFoundException e) {e.printStackTrace();}

        output.println("<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:y=\"http://www.yworks.com/xml/graphml\" xmlns:yed=\"http://www.yworks.com/xml/yed/3\" xsi:schemaLocation=\"http://graphml.graphdrawing.org/xmlns http://www.yworks.com/xml/schema/graphml/1.1/ygraphml.xsd\">");
        output.println("\t<key attr.name=\"description\" attr.type=\"string\" for=\"node\" id=\"d5\"/>");
        output.println("\t<key for=\"node\" id=\"d6\" yfiles.type=\"nodegraphics\"/>");
        output.println("\t<key attr.name=\"description\" attr.type=\"string\" for=\"edge\" id=\"d9\"/>");
        output.println("\t<key for=\"edge\" id=\"d10\" yfiles.type=\"edgegraphics\"/>");
        output.println("<graph>");

        for (Node node : nodes)
            node("" + node.ID, node.graphmlLabel(), node.graphmlColor());
        
        for (Edge edge : edges)
            edge(edge.hashCode(), "" + edge.parent.ID, "" + edge.child.ID, "" + utils.round(edge.edgevalue));

        output.println("</graph>");
        output.println("</graphml>");
        output.close();
    }

    private static void node(String ID, String value, String color) {
        output.println("<node id=" + '"' + ID + '"' + ">" +
                "<data key=\"d5\"><![CDATA[Node ID: " + ID + "]]></data>" +
                "<data key=\"d6\">" +
                "<y:ShapeNode><y:Fill color=\"" + color + "\" transparent=\"false\"/>" +
                "<y:BorderStyle color=\"#FF0000\" type=\"line\" width=\"1.0\"/>" +
                "<y:NodeLabel textColor=\"#000000\">" + value + "</y:NodeLabel>" +
                "<y:Shape type=\"ellipse\"/></y:ShapeNode></data></node>");
    }

    private static void edge(int ID, String sourceID, String targetID, String edgelabel) {
        output.println("<edge id=" + '"' + ID + '"' + " source=" + '"' + sourceID + '"' + " target=" + '"' + targetID + '"' + ">" +
                "<data key=\"d9\"><![CDATA[Edge ID: " + ID + "]]></data>" +
                "<data key=\"d10\"><y:BezierEdge>" +
                "<y:LineStyle color=\"#000000\" type=\"line\" width=\"1.0\"/>" +
                "<y:Arrows source=\"none\" target=\"standard\"/>" +
                "<y:EdgeLabel>" + edgelabel + "</y:EdgeLabel>" +
                "</y:BezierEdge></data></edge>");
        output.println("");
    }
}
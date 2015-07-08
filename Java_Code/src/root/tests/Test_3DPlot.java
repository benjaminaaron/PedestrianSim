package root.tests;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import root.utils;
import root.model.mapobjects.Cell;
import root.utils.Algorithm;

public class Test_3DPlot extends AbstractTest {
		
	public Test_3DPlot(){
		mapname = "200x200_3dPlot_mitAgents";
		algo = Algorithm.EUCLIDEAN;
		withPostvis = false;
		maxEvents = 1;
		logON = false;
		
		start();	
	}

	public void afterCompletion() {
		ArrayList<Cell> goal = sim.map.goals;
		
		double goalX = goal.get(0).col;
		double goalY = goal.get(0).row;
		double length = sim.map.columns;
		double height = sim.map.rows;
		
		writeFile(length, height, goalX, goalY, "3D_plot_M");
	}
	
	public static void writeFile(double length, double height, double goalX, double goalY, String filename) {
		FileWriter writer = null;
		
		try {
			String file = utils.DIR + utils.SEPARATOR + filename + ".csv";
			writer = new FileWriter(file);
			
			writer.append(String.valueOf(length));
			writer.append(utils.NEW_LINE_SEPARATOR);
			writer.append(String.valueOf(height));
			writer.append(utils.NEW_LINE_SEPARATOR);
			writer.append(String.valueOf(goalX));
			writer.append(utils.NEW_LINE_SEPARATOR);
			writer.append(String.valueOf(goalY));
			writer.append(utils.NEW_LINE_SEPARATOR);

		} catch (Exception e) { e.printStackTrace(); } 
		finally {
			try {
				writer.flush();
				writer.close();
				System.out.println("exported " + filename + ".csv");
			} catch (IOException e){ e.printStackTrace(); }
		}
	}
	
}

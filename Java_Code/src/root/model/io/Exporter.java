package root.model.io;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import root.model.mapobjects.Agent;
import root.utils;
import root.model.snapshots.AgentSnapshot;

public class Exporter {
	
	public static void doExport(ArrayList<Agent> agents, String filename){
		FileWriter writer = null;
		try {
			String file = utils.DIR + utils.SEPARATOR + filename + ".csv";			
			writer = new FileWriter(file);	
			
			int cols = agents.size();
			int rows = 0;
			for (Agent agent : agents)			
				if(agent.snapshots.size() > rows)
					rows = agent.snapshots.size(); // to find maximum
			rows = rows * 4 + 2;
			
			String[][] grid = new String[rows][cols + 1];
			for(int row = 0; row < rows; row ++)
				for(int col = 0; col < cols; col ++)
					grid[row][col] = "-";
			
			grid[0][0] = "agent_id";
			grid[1][0] = "agent_speed";
			
			for (int col = 1; col < agents.size() + 1; col++){	
				Agent agent = agents.get(col - 1);
				grid[0][col] = "" + agent.ID;
				grid[1][col] = "" + agent.speed;	
				int row = 1;
				for(int i = 0; i< agent.snapshots.size(); i++){
					AgentSnapshot snapshot = agent.snapshots.get(i);
					grid[++row][col] = "" + snapshot.nowCell.col;
					if(col == 1)
						grid[row][0] = "cell_col";
					grid[++row][col] = "" + snapshot.nowCell.row;
					if(col == 1)
						grid[row][0] = "cell_row";
					grid[++row][col] = "" + snapshot.cellarrivaltime;
					if(col == 1)
						grid[row][0] = "cellarrivaltime";
					grid[++row][col] = "" + snapshot.celldeparturetime;
					if(col == 1)
						grid[row][0] = "celldeparturetime";
				}
			}
						
			for(int row = 0; row < rows; row ++){
				for(int col = 0; col < cols; col ++)
					writer.append(grid[row][col] + (col == cols - 1 ? "" : utils.COMMA_DELIMITER));
				writer.append(utils.NEW_LINE_SEPARATOR);
			}

		} catch (Exception e) { e.printStackTrace(); } 
		finally {
			try {
				writer.flush();
				writer.close();
			}catch (IOException e){
				e.printStackTrace();
			}
		}
	}
	
	//TODO
	public static void exportCSV(StringBuilder sb, String filename){
		FileWriter writer = null;
		try {
			String file = utils.DIR + utils.SEPARATOR + filename + ".csv";			
			writer = new FileWriter(file);				
			writer.append(sb.toString());	
		} catch (Exception e) { e.printStackTrace(); } 
		finally {
			try {
				writer.flush();
				writer.close();
			}catch (IOException e){
				e.printStackTrace();
			}
		}
	}
}
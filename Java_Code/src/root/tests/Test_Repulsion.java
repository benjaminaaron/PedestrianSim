package root.tests;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import root.model.Simulation;
import root.model.mapobjects.Agent;
import root.utils;
import root.model.snapshots.AgentSnapshot;
import root.model.snapshots.NeighbourSnapshot;
import root.utils.Algorithm;


public class Test_Repulsion extends AbstractTest {
	
	public Test_Repulsion() {
		mapname = "trapped_sources_goals_test";
		algo = Algorithm.EUCLIDEAN;
		withPostvis = true;
		maxEvents = 1000;
		logON = false;
		
		start();
	}
	
	@Override
	public void afterCompletion() {
		
		ArrayList<Agent> allAgents = Simulation.allAgents;
		
		Agent agent0 = allAgents.get(0);
		Agent agent5 = allAgents.get(5);
		Agent agent4 = allAgents.get(4);
		Agent agent6 = allAgents.get(6);
		
		ArrayList<AgentSnapshot> as4 = agent4.snapshots;
		ArrayList<AgentSnapshot> as6 = agent6.snapshots;
		ArrayList<AgentSnapshot> as0 = agent0.snapshots;
		ArrayList<AgentSnapshot> as5 = agent5.snapshots;
		
		ArrayList<Double> dist0 = new ArrayList<Double>();
		ArrayList<Double> repel0 = new ArrayList<Double>();
		ArrayList<Double> dist5 = new ArrayList<Double>();
		ArrayList<Double> repel5 = new ArrayList<Double>();
		
		ArrayList<Double> dist4 = new ArrayList<Double>();
		ArrayList<Double> repel4 = new ArrayList<Double>();
		ArrayList<Double> dist6 = new ArrayList<Double>();
		ArrayList<Double> repel6 = new ArrayList<Double>();
		
		for (int i = 0; i < as0.size(); i++) {
			NeighbourSnapshot snapshot = as0.get(i).getChosenOne();
			if (snapshot != null) {
				dist0.add(snapshot.euclideanDistToGoal);
				repel0.add(snapshot.repulsion);
			}
		}
		
		for (int i = 0; i < as5.size(); i++) {
			NeighbourSnapshot snapshot = as5.get(i).getChosenOne();
			if (snapshot != null){
				dist5.add(snapshot.euclideanDistToGoal);
				repel5.add(snapshot.repulsion);
			}
		}
		
		for (int i = 0; i < as4.size(); i++) {
			NeighbourSnapshot snapshot = as4.get(i).getChosenOne();
			if (snapshot != null){
				dist4.add(snapshot.euclideanDistToGoal);
				repel4.add(snapshot.repulsion);
			}
		}
		
		for (int i = 0; i < as6.size(); i++) {
			NeighbourSnapshot snapshot = as6.get(i).getChosenOne();
			if (snapshot != null){
				dist6.add(snapshot.euclideanDistToGoal);
				repel6.add(snapshot.repulsion);
			}
		}
		
		writeFile(dist0, repel0, "agent0");
		writeFile(dist5, repel5, "agent5");
		writeFile(dist4, repel4, "agent4");
		writeFile(dist6, repel6, "agent6");
	}
	
	public static void writeFile(ArrayList<Double> distances, ArrayList<Double> repulsion, String filename) {
		FileWriter writer = null;
		
		try {
			String file = utils.DIR + utils.SEPARATOR + filename + ".csv";
			writer = new FileWriter(file);
			
			writer.append(String.valueOf(distances.size()));
			writer.append(utils.COMMA_DELIMITER);
			writer.append(String.valueOf(repulsion.size()));
			writer.append(utils.NEW_LINE_SEPARATOR);
			
			for(int i = 0; i < distances.size(); i++) {
				writer.append(String.valueOf(distances.get(i)));
				writer.append(utils.COMMA_DELIMITER);
				writer.append(String.valueOf(repulsion.get(i)));
				writer.append(utils.NEW_LINE_SEPARATOR);
			}

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

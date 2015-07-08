package root.tests.movement;

import java.util.ArrayList;

import root.constants;
import root.model.Simulation;
import root.model.mapobjects.Agent;
import root.model.mapobjects.Cell;
import root.model.snapshots.AgentSnapshot;
import root.tests.AbstractTest;
import root.utils.Algorithm;

public class MovementSpeedCalculator extends AbstractTest{
	
	public MovementSpeedCalculator(String mapname){
		withPostvis = true;
		algo = Algorithm.EUCLIDEAN;
		maxEvents = 1000;
		logON = false;
		
		start();
	}
	
	
	@Override
	public void afterCompletion() {
		
		ArrayList<Agent> allAgents = Simulation.allAgents;
		
		for(Agent agent : allAgents){
			ArrayList<AgentSnapshot> records = agent.snapshots;
			AgentSnapshot startRecord = records.get(0);
			AgentSnapshot endRecord = records.get(records.size() - 1);
				
			Cell startCell = startRecord.nowCell;
			double starttime = startRecord.cellarrivaltime;
						
			Cell endCell = endRecord.nowCell;
			double endtime = endRecord.celldeparturetime;
			
			double freeFlowVelocity = agent.speed;
			double distance = calcDistance(startCell, endCell);
			double totalTime = endtime - starttime;
			
			double speed = (distance / 100) / totalTime;
				
			System.out.println("Unterschied zwischen Wunsch und echter Geschwindigkeit " + (freeFlowVelocity - speed));
		}		
	}

	private static double calcDistance(Cell startCell, Cell endCell) {
		double xDist = Math.abs(endCell.col - startCell.col) * constants.CELLWIDTH;
		double yDist =  Math.abs(endCell.row - startCell.row) * constants.CELLHEIGHT;
		double absDist = Math.sqrt(xDist * xDist + yDist * yDist);				
		return absDist;
	}
	
}
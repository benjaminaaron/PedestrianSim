package root.tests;

import root.constants;
import root.utils;
import root.model.Simulation;
import root.model.mapobjects.Agent;
import root.model.snapshots.AgentSnapshot;
import root.utils.Algorithm;


public class Test_FluxDensity extends AbstractTest {
	
	public Test_FluxDensity() {
		mapname = "65x12m_163x32pixel_fundamentaldiagramm";
		algo = Algorithm.EUCLIDEAN;
		withPostvis = true;
		maxEvents = 100000;
		logON = false;
		
		start();
	}
	
	@Override
	public void afterCompletion() {
		int colMeasurement = 7; // TODO in m? 
		
		double interval = 1; 
		double endtime = sim.queue.currenttime;
		
		int totalcount = 0;
		double firstInterval = 10.0;
		
		for(double intervalBegins = firstInterval ; intervalBegins <= endtime; intervalBegins += interval){
			int counter = 0;
			double intervalEnds = intervalBegins + interval;
		
			for(Agent agent: Simulation.allAgents)
				for(AgentSnapshot snapshot: agent.snapshots)
					if(snapshot.nowCell.col == colMeasurement && !agent.wasMeasured)
						if(snapshot.cellarrivaltime > intervalBegins && snapshot.cellarrivaltime <= intervalEnds){
							counter++;
							agent.wasMeasured = true;
						}	
			double per1m = counter / (sim.map.rows * constants.CELLSIDES_M);
			System.out.println("between " + utils.round(intervalBegins) + "s and " + utils.round(intervalEnds) + "s, " + counter + " (" + utils.round(per1m) + "/m) agents reached column " + colMeasurement);	
			totalcount += counter;
		}	
		System.out.println( totalcount + " of " + Simulation.allAgents.size() + " past the measurement line");
	
		double timePassed = endtime - firstInterval;
		
		double agentsPerSecond = totalcount / timePassed;
		
		double agentsPerSecondAndMeter = agentsPerSecond/12;
		
		System.out.println(agentsPerSecondAndMeter);
		
	
	}
	
}

package root.tests;

import root.utils.Algorithm;


public class Default extends AbstractTest {
	
	public Default(){
		mapname = "map30x20";
		algo = Algorithm.DIJKSTRA;
		withPostvis = true;
		maxEvents = -1;
		logON = false;
		
		start();
	}

	@Override
	public void afterCompletion() {
		System.out.println("default run has ended");
	}

}

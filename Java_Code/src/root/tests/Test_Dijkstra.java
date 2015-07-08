package root.tests;

import root.utils.Algorithm;


public class Test_Dijkstra extends AbstractTest {
	
	public Test_Dijkstra() {
		mapname = "1source1goal";
		algo = Algorithm.DIJKSTRA;
		withPostvis = true;
		maxEvents = -1;
		logON = false;
		
		start();
	}
	
	@Override
	public void afterCompletion() {
		System.out.println("dijkstra test completed");
	}

}

package root.tests;

import root.utils.Algorithm;


public class Test_FastMarching extends AbstractTest {
	
	public Test_FastMarching(){
		mapname = "labyrinth";
		algo = Algorithm.FASTMARCHING;
		withPostvis = true;
		maxEvents = -1;																																																																																																																					;
		logON = false;
			
		start();
	}

	@Override
	public void afterCompletion() {
	}

}
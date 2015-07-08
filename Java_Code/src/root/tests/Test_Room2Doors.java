package root.tests;

import root.utils.Algorithm;


public class Test_Room2Doors extends AbstractTest {
	
	public Test_Room2Doors(int roomNr){
		if (roomNr == 1)
			mapname = "room1door";
		else if (roomNr == 2)
			mapname = "room2doors";
		else if (roomNr == 3)
			mapname = "room2doorsFull";
		else if (roomNr == 4)
			mapname = "room4doorsFull";
		else if (roomNr == 5)
			mapname = "room4doorsFull2";
		algo = Algorithm.FASTMARCHING;
		withPostvis = true;
		maxEvents = 10000;
		logON = false;
		
		start();

	}

	@Override
	public void afterCompletion() {
		//System.out.println("" + Simulation.fastMarchingObj.toString());
	}

}

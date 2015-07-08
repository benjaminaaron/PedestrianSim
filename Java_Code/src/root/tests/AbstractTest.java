package root.tests;

import root.utils.Algorithm;
import root.model.Simulation;
import root.model.io.Log;

public abstract class AbstractTest {

	public String mapname = "map30x20";
	public boolean withPostvis = true;
	public boolean logON = false;
	public int maxEvents = -1;
	public Algorithm algo = Algorithm.EUCLIDEAN;
	public Simulation sim;
	
	public void start(){
		Log.ON = logON;
		sim = new Simulation(this, algo, mapname, withPostvis, maxEvents);
		sim.start();
	}

	public abstract void afterCompletion();
	
}
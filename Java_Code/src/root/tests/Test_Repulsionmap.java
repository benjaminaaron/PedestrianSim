package root.tests;

import root.model.io.RepulsionmapExporter;
import root.utils.Algorithm;

public class Test_Repulsionmap extends AbstractTest {
	
	public Test_Repulsionmap() {
		mapname = "200x200_3dPlot_mitAgents";
		algo = Algorithm.EUCLIDEAN;
		withPostvis = true;
		maxEvents = -1;
		logON = false;
		
		start();
	}
	
	@Override
	public void afterCompletion() {
		int size = sim.map.repulsionmapSnapshots.size();
		System.out.println(" exporting " + size + " RepulsionmapSnapshots");
		RepulsionmapExporter.doExport(sim.map, "RepulsionmapSnapshots_" + size);
	}

}

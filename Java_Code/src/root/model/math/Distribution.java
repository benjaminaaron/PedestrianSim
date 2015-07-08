package root.model.math;

import java.util.ArrayList;

public class Distribution {
	
	// NORMAL DISTRIBUTION
	
	public static final double MEAN = 1.45873;
	public static final double VARIANCE = 0.1825022;
	
	public static ArrayList<Double> getVelocities(int n) { // for testing
		ArrayList<Double> liste = new ArrayList<Double>();
		// velocity = rand.nextGaussian();
		for (int i = 0; i < n; i++)
			liste.add(getVelocityValue());
		return liste;
	}
	
	public static double getVelocityValue(){		
		while(true) {
			double a1 = 2 * Math.random() - 1;
			double a2 = 2 * Math.random() - 1;
			double q = a1 * a1 + a2 * a2;
			if (q > 0 && q < 1) {
				double temp = -2 * Math.log(q); 
				double temp2 = temp / q;
				double p = Math.sqrt(temp2);
				return a1 * p * VARIANCE + MEAN; 
			}
		}		
	}
	
}

// APACHE NORMAL DISTRIBUTION: commons-math3-3.0.jar

/*package model.distribution;
import java.util.ArrayList;

import org.apache.commons.math3.distribution.NormalDistribution;

public class NormalDistApache {
	
		ArrayList<Double> velocities = new ArrayList<Double>();
		final double MEAN = 1.454869;
		final double DEVIATION = 0.1825022;
		
		public ArrayList<Double> getVelocities(int n) {
			for (int i = 0; i < n; i++) {
				NormalDistribution dist = new NormalDistribution(MEAN,DEVIATION);
				velocities.add(dist.sample());
			}
			return velocities;
		}
}
*/
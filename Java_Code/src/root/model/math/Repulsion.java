package root.model.math;

import root.constants;

public class Repulsion {
	
	public static double repelPotential(double distance) {
		// distance in m (cells * 0.4)
		double potential = 0;
		double temp = Math.pow(distance / constants.width, 2);
		temp = temp - 1;
		if (Math.abs(distance) < constants.width)
			potential = - constants.height * Math.exp(1 / temp);
		else
			potential = 0;
		return potential;
	}
	
}
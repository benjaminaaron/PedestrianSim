package root.tests;
import java.util.ArrayList;

import root.constants;
import root.model.math.Distribution;


public class Test_Distribution {
	
	public static void main(String[] args) {
		
		int n = 1000;
		
		ArrayList<Double> vel1 = Distribution.getVelocities(n);
		
		double sum = 0;
		for(Double val : vel1)
			sum += val;
		double avg = sum / vel1.size();
		
		System.out.println("" + avg);
		System.out.println("" + constants.MEAN);
		
		
		/*
		NormalDistApache apache = new NormalDistApache();
		ArrayList<Double> vel2 = apache.getVelocities(n);
		
		sum = 0;
		for(Double val : vel2)
			sum += val;
		avg = sum / vel2.size();
		System.out.println(avg);
		*/		
	}

}

package root.model.events;

import root.utils;


public class TimewatchEvent extends Event{

	public double continousTimeRhythm = 0.5;
	public boolean keepgoing = true;
	
	public TimewatchEvent(double snapshotTime) {
		super(snapshotTime, 0);
	}
	
	public TimewatchEvent increment(){
		primaryvalue += continousTimeRhythm;		
		return this;
	}
	
	public String toString(){
		return "timewatch \t" + utils.round(primaryvalue) + "\t" + keepgoing;
	}
	
}

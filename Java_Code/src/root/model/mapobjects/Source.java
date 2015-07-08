package root.model.mapobjects;

import java.util.ArrayList;

import root.model.events.SourceEvent;
import root.model.snapshots.SourceGoalSnapshot;


public class Source{

	public int ID;
	public Cell cell;
	public int releases;
	public int currentRelease = 0;
	public double releaseRhythm;
	public SourceEvent mySourceEvent;
	public ArrayList<SourceGoalSnapshot> snapshots = new ArrayList<>();
	
	public Source(int ID, Cell cell, double releaseRhythm, int releases) {
		this.ID = ID;
		this.cell = cell;
		this.releaseRhythm = releaseRhythm;	
		this.releases = releases;
		mySourceEvent = new SourceEvent(this);		
	}

	public SourceEvent getSourceEvent() {
		currentRelease += 1;
		snapshots.add(new SourceGoalSnapshot(0, currentRelease));
		return mySourceEvent;
	}
	
	public SourceEvent increment(boolean incrementRelease){
		mySourceEvent.primaryvalue += releaseRhythm;
		if(incrementRelease)
			currentRelease += 1;
		snapshots.add(new SourceGoalSnapshot(mySourceEvent.primaryvalue, currentRelease));
		return mySourceEvent;		
	}

	public String ifThatsYouWhatsYourStats(int col, int row, double currenttime) {
		if(cell.col == col && cell.row == row){		
			SourceGoalSnapshot closestSnapshot = null;	
			//for(SourceGoalSnapshot snapshot : snapshots)
			//	System.out.println(snapshot.time + "  " + currenttime);		
			for(int i = 0; i < snapshots.size() - 1; i ++){
				double thisTime = snapshots.get(i).time;
				double nextTime = snapshots.get(i + 1).time;
				//System.out.println(i + ":" + thisTime + " / " + (i+1) + ":" + nextTime);
				if((currenttime == thisTime) || (currenttime > thisTime && currenttime < nextTime)){
					closestSnapshot = snapshots.get(i);
					break;
				}
			}
			if(closestSnapshot == null)
				closestSnapshot = snapshots.get(snapshots.size() - 1);
			//System.out.println(">> " + snapshots.indexOf(closestSnapshot));
			return "<br><b>source_" + ID + "</b>:<br> up to " + currenttime + "s i created " + closestSnapshot.quantity + " agents, the last one at " + closestSnapshot.time + "s";		
		}
		return "";
	}
	
}
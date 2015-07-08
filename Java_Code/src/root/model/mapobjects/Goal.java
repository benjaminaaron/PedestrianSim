package root.model.mapobjects;

import java.util.ArrayList;

import root.utils;
import root.model.snapshots.SourceGoalSnapshot;


public class Goal{

	public int ID;
	public Cell goalcell;
	public int agentCount = 0; // counting agents that reached me
	public ArrayList<SourceGoalSnapshot> snapshots = new ArrayList<>();

	public Goal(int ID, Cell goalcell){
		this.ID = ID;
		this.goalcell = goalcell;
		snapshots.add(new SourceGoalSnapshot(0, agentCount));
	}
	
	public void agentReachedMe(double time) {
		++ agentCount;
		snapshots.add(new SourceGoalSnapshot(time, agentCount));
	}
	
	public String whatsYourStats(double currenttime) {		
		SourceGoalSnapshot closestSnapshot = null;
		for(int i = 0; i < snapshots.size() - 1; i ++){
			double thisTime = snapshots.get(i).time;
			double nextTime = snapshots.get(i + 1).time;
			if((currenttime == thisTime) || (currenttime > thisTime && currenttime < nextTime)){
				closestSnapshot = snapshots.get(i);
				break;
			}
		}
		if(closestSnapshot == null)
			closestSnapshot = snapshots.get(snapshots.size() - 1);
		return "<br><b>goal_" + ID + "</b>:<br> up to " + currenttime + "s " + closestSnapshot.quantity + " agent" + (closestSnapshot.quantity == 1 ? "" : "s") + " reached me, the last one at " + utils.round(closestSnapshot.time) + "s";		
	}
	
}
package root.model.events;

import root.model.mapobjects.Agent;
import root.utils;

public class MoveEvent extends Event{

	public Agent agent;
	public boolean isGoalReachingEvent = false;
	
	public MoveEvent(Agent agent, double movetime){
		super(movetime, agent.speed);
		this.agent = agent;
	}
	
	public String toString(){
		return agent + "\t" + utils.round(primaryvalue) + "\t" + utils.round(secondaryvalue) + "\t" + isGoalReachingEvent;
	}
	
}

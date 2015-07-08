package root.model;

import java.util.ArrayList;

import root.utils.EventType;
import root.model.events.Event;
import root.model.events.MoveEvent;
import root.model.events.SourceEvent;
import root.model.events.TimewatchEvent;
import root.model.io.Log;
import root.model.mapobjects.Agent;
import root.model.mapobjects.Cell;

public class EventQueue {

	private ArrayList<Event> events = new ArrayList<>();
	private Simulation sim;
	public int eventCounter = 0;
	public int moveEventCounter = 0;
	public int sourceEventCounter = 0;
	public int timewatchEventCounter = 0;
	private int maxEvents;
	public double currenttime = 0;
	public boolean cancelled = false;
	
	public EventQueue(Simulation sim, int maxEvents){
		this.sim = sim;
		this.maxEvents = maxEvents;
	};
	
	public void addEvent(Event newEvent){				
		int insertIndex = 0;
		
		if(events.size() > 0){
			
			insertIndex = 0;
			boolean equalsFound = false;
			boolean done = false;
			int i = 0;		
			
			while(!done){
				Event event = events.get(i);
				
				double newEventVal = 0, eVal = 0;
						
				if(event.primaryvalue == newEvent.primaryvalue)
					equalsFound = true;
				
				if(!equalsFound){
					newEventVal = newEvent.primaryvalue;
					eVal = event.primaryvalue;
				}
				
				if(equalsFound){
					if(event.primaryvalue != newEvent.primaryvalue)
						done = true;
					newEventVal = newEvent.secondaryvalue;
					eVal = event.secondaryvalue;
				}
				
				if(!done)
					if(eVal <= newEventVal)
						insertIndex = i + 1;
					else
						done = true;
				
				i++;
				if(i == events.size())
					done = true;
			}	
			events.add(insertIndex, newEvent);
			
		} 
		else
			events.add(newEvent);
		
		if(Log.ON)
			Log.write("event added to queue: " + newEvent + ", at index: " + insertIndex + "\n\n");
	}

	public Event getNext(){	
		return events.remove(0);
	}

	public String show() {
		String str = "[index]\tagentID(ffv)\ttime\tisGoalReachingEvent\n";
		for(int i=0; i < events.size(); i++)
			str += "[" + i + "] \t" + events.get(i) + "\n";
		return str;
	}

	public boolean action() {
		eventCounter ++;
		
		if(Log.ON)
			Log.write("eventQueue currently looks like the following, applying (and then removing it from the queue) event[0] next: \n\n" + show());
		
		Event nextEvent = getNext();
		currenttime = nextEvent.primaryvalue;
				
		switch(nextEvent.getType()){
		case MOVE:
			moveEventCounter ++;
			MoveEvent moveEvent = (MoveEvent) nextEvent;
			Agent agent = moveEvent.agent;
			if(!moveEvent.isGoalReachingEvent)
				addEvent(agent.getNextMoveEvent(moveEvent));
			else
				sim.agentReachedGoal(agent, moveEvent.primaryvalue);
			break;
		case SOURCE:
			sourceEventCounter ++;
			SourceEvent sourceEvent = (SourceEvent) nextEvent;	
			Cell sourcecell = sourceEvent.source.cell;
			if(sim.map.hasAtLeastOneFreeNeighbourCells(sourcecell)){
				Agent newAgent = new Agent(Simulation.allAgents.size(), sourcecell, sim.map);
				sim.addAgent(newAgent);	
				addEvent(newAgent.getNextMoveEvent(new MoveEvent(newAgent, sourceEvent.primaryvalue)));	
				if(sourceEvent.source.currentRelease < sourceEvent.source.releases)
					addEvent(sourceEvent.source.increment(true));	
			}
			else
				addEvent(sourceEvent.source.increment(false));
			break;
		case TIMEWATCH:
			timewatchEventCounter ++;
			TimewatchEvent timewatchEvent = (TimewatchEvent) nextEvent;
			sim.takeTimedSnapshot(timewatchEvent.primaryvalue);
			if(!Log.ON)
				System.out.println("events processed: " + eventCounter);
			if(timewatchEvent.keepgoing)
				addEvent(timewatchEvent.increment());
			break;
		case UNDEFINED:
		default:
			break;
		}
		
		if(events.size() == 1)
			if(events.get(0).getType() == EventType.TIMEWATCH)
				((TimewatchEvent) events.get(0)).keepgoing = false;
				
		if(maxEvents != -1 && eventCounter >= maxEvents)
			cancelled = true;
			
		if(events.size() > 0 && !cancelled)
			return true;
		
		return false;
	}
	
}

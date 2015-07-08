package root.model.events;

import root.utils.EventType;


public abstract class Event {

	public double primaryvalue; // primary value for ordering
	public double secondaryvalue; // secondary value for ordering
	
	public Event(double movetime, double secondval){		
		this.primaryvalue = movetime;
		this.secondaryvalue = secondval;
	}
	
	public EventType getType(){	
		if(this instanceof MoveEvent)
			return EventType.MOVE;
		if(this instanceof SourceEvent)
			return EventType.SOURCE;
		if(this instanceof TimewatchEvent)
			return EventType.TIMEWATCH;
		return EventType.UNDEFINED;		
	}
	
}

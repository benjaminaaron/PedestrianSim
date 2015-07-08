package root.model.events;

import root.model.mapobjects.Source;
import root.utils;

public class SourceEvent extends Event{

	public Source source;
	
	public SourceEvent(Source source){
		super(0, 0);
		this.source = source;	
	}
	
	public String toString(){
		return "source \t" + utils.round(primaryvalue) + "\t" + source.currentRelease + "/" + source.releases;
	}
	
}

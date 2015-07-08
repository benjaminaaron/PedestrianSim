package root.model;

import java.util.ArrayList;
import java.util.HashMap;

import root.tests.AbstractTest;
import root.view.Gui;
import root.view.View;
import root.utils;
import root.utils.Algorithm;
import root.model.events.MoveEvent;
import root.model.events.TimewatchEvent;
import root.model.io.Log;
import root.model.mapobjects.Agent;
import root.model.mapobjects.Cell;
import root.model.mapobjects.Goal;
import root.model.mapobjects.Source;
import root.model.math.fastmarching.FastMarching;

public class Simulation {

	private AbstractTest caller;
	public Map map;
	public static ArrayList<Agent> allAgents = new ArrayList<>();
	public static ArrayList<Agent> activeAgents = new ArrayList<>();
	public static ArrayList<Source> sources = new ArrayList<>();
	public static HashMap<String, Goal> goals = new HashMap<>();
	public EventQueue queue;
	public boolean withPostvis;
	public boolean finished = false;
	public double starttime;
	public static Algorithm algo;
	public static FastMarching fastMarchingObj;
	
	
	public Simulation(AbstractTest caller, Algorithm algo, String mapname, boolean withPostvis, int maxEvents){
		this.caller = caller;
		Simulation.algo = algo;
		map = new Map(mapname);
		
		if(algo == Algorithm.FASTMARCHING)
			fastMarchingObj = new FastMarching(map);
		
		this.withPostvis = withPostvis;
		
		collectAgents();	
		
		if(Log.ON){
			Log.write("map: \n" + map);
			Log.write("agents: \n" + showActiveAgents() + "\n\n");
		}
		
		queue = new EventQueue(this, maxEvents);
	}
	
	public static boolean isGoalCell(Cell cell){
		return goals.get(cell.col + "/" + cell.row) != null;
	}
	
	public static boolean isSourceCell(Cell cell){
		for(Source source : sources)
			if(source.cell == cell)
				return true;
		return false;
	}

	public void addAgent(Agent agent){
		allAgents.add(agent);
		activeAgents.add(agent);
		if(Log.ON)
			Log.write("added agent " + agent);
	}
	
	private void collectAgents() {
		for(Cell agentcell : map.agents)
			addAgent(new Agent(allAgents.size(), agentcell, map));					
	}
	
	private String showActiveAgents(){
		String str = "";
		for(Agent agent : activeAgents)
			str += agent + "\n";
		return str;
	}

	public void start(){
		System.out.println("computing...");

		starttime = System.currentTimeMillis();
		
		if(Log.ON)
			Log.write(">> seeding the eventQueue now\n");
		
		queue.addEvent(new TimewatchEvent(0.0));
		takeTimedSnapshot(0.0);
		
		for(Cell sourcecell : map.sources){
			Source source = new Source(sources.size(), sourcecell, 01.5, 3);
			sources.add(source);
			queue.addEvent(source.getSourceEvent());
		}	
		
		for(Agent agent : getSpeedorderedActiveAgents()){			
			MoveEvent moveEvent = new MoveEvent(agent, 0);
			queue.addEvent(agent.getNextMoveEvent(moveEvent));
		}
		
		for(Cell goalcell : map.goals)
			goals.put(goalcell.col + "/" + goalcell.row, new Goal(goals.size(), goalcell));

		if(Log.ON)
			Log.write(">> seeding the eventQueue is completed, setting it into ACTION-mode now\n");	
		
		while(queue.action()); // because not from within action() because it creates a "recursive drilling hole" (noticed by Daniel)
		
		end();
	}
	
	private ArrayList<Agent> getSpeedorderedActiveAgents() {
		ArrayList<Agent> ordered = new ArrayList<>();
		ArrayList<Agent> temp = new ArrayList<>();
		temp.addAll(activeAgents);
		while(temp.size() > 0)
			ordered.add(temp.remove(getIndexOfFastest(temp)));	
		return ordered;
	}
	
	private int getIndexOfFastest(ArrayList<Agent> list){
		double max = 0;
		int indexOfFastest = 0;
		for(Agent agent: list)
			if(agent.speed > max){
				max = agent.speed;
				indexOfFastest = list.indexOf(agent);
			}
		return indexOfFastest;
	}

	public void takeTimedSnapshot(double time) {	
		map.takeTimedSnapshot(time);
	}
	
	/*
	public void takeSnapshot(double time){
		map.takeSnapshot(time);	
	}
	*/

	public void agentReachedGoal(Agent agent, double time){
		if(Log.ON)
			Log.write("agent " + agent + " has reached the goal after " + utils.round(time) + "s");
		map.agentLeftCell(agent.mycell);
		activeAgents.remove(activeAgents.indexOf(agent));
		goals.get(agent.mycell.col + "/" + agent.mycell.row).agentReachedMe(time);	
	}
	
	public void end(){	
		//Exporter.doExport(allAgents, "AgentData");
		
		double endtime = System.currentTimeMillis();
		String computationtimeSec = utils.round((endtime - starttime) / 1000) + "s";
		String simulationtimeSec = utils.round(queue.currenttime) + "s";
		
		String cancelledORcompleted = "";
		if(queue.cancelled){
			cancelledORcompleted = "CANCELLED";
			if(Log.ON)
				Log.write(">> the simulation was cancelled before all agents reached a goal");
		}
		else{
			cancelledORcompleted = "COMPLETED";
			if(Log.ON)
				Log.write(">> all agents have reached the goal, the simulation has ended\n");
		}
		System.out.println("simulation " + cancelledORcompleted + " after " + computationtimeSec + " of computation time and " + simulationtimeSec + " of simulated time (" + queue.eventCounter + " events were processed: " + queue.moveEventCounter + "M, " + queue.sourceEventCounter + "S, " + queue.timewatchEventCounter + "T)");
		
		if(Log.ON)
			Log.write("" + map);
		
		/*
		if(Log.ON){
		Log.write("records of all agents:\n");
		for(Agent agent : allAgents)
			Log.write(agent.showHistory());
		}*/
		
		finished = true;
			
		if(withPostvis)
			replaySimulation();
		
		caller.afterCompletion();
	}
	
	
	public void replaySimulation(){
		System.out.println("starting postvis with " + map.timedSnapshots.size() + " frames");
		Gui gui = new Gui(map.rows, map.columns);
		View view = new View(gui, gui.getWidth(), gui.getHeight(), map);
		gui.getContentPane().add(view);
	}

}

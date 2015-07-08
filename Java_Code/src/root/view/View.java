package root.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import root.constants;
import root.utils;
import root.utils.Algorithm;
import root.utils.CellType;
import root.model.Map;
import root.model.Simulation;
import root.model.io.Log;
import root.model.mapobjects.Agent;
import root.model.mapobjects.Goal;
import root.model.mapobjects.Source;
import root.model.snapshots.AgentSnapshot;
import root.model.snapshots.FloodingmapSnapshot;
import root.model.snapshots.MapSnapshot;

@SuppressWarnings("serial")
public class View extends JPanel {
	
	private Timer timer;
	private MapSnapshot currentSnapshot;
	private Map map;
	private int currentSnapshotIndex = 0;
	private CellWrapper[][] cellsWrapped;
	private Gui gui;
	private FloodingmapSnapshot flooding = null;
	private double[][] FFM = null;
	private Image minion;
	private Image banana;
	
	public View(final Gui gui, int width, int height, Map map){
		setBounds(0, 0, width, height);
		setLayout(null);
		
		try {
			minion = gui.resizeToBig(ImageIO.read(new File(utils.DIR + utils.SEPARATOR + "icons" + utils.SEPARATOR  + "Minion.png")), constants.CELLWIDTH, constants.CELLHEIGHT);
			banana = gui.resizeToBig(ImageIO.read(new File(utils.DIR + utils.SEPARATOR + "icons" + utils.SEPARATOR  + "banana.png")), constants.CELLWIDTH, constants.CELLHEIGHT);
		} catch (IOException e) {}
		
		this.map = map;
		this.gui = gui;
		
		InkerRep.setMaxV(map.globalRepulsionMinimum);
		cellsWrapped = new CellWrapper[map.columns][map.rows];
		
		for(int row = 0; row < map.rows; row++)
			for(int col = 0; col < map.columns; col++)
				cellsWrapped[col][row] = new CellWrapper(map.cells[col][row],width,gui.getWidth(),height,gui.getHeight());	
		
		final ArrayList<MapSnapshot> mapSnapshots = map.timedSnapshots;
		
		currentSnapshot = mapSnapshots.get(0);
		gui.getSlider().setMinimum(0);
		gui.getSlider().setMaximum(mapSnapshots.size() - 1);
		gui.getSlider().setPaintTicks(true);
		gui.getSlider().setMajorTickSpacing(mapSnapshots.size() / 10);
		
		timer = new Timer(constants.SLEEPMILLIS, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(currentSnapshotIndex < mapSnapshots.size()-1) {
					currentSnapshot = mapSnapshots.get(++ currentSnapshotIndex);
					gui.getSlider().setValue(currentSnapshotIndex);
					DecimalFormat df = new DecimalFormat("0.00");
					gui.getInfoBottomLabel().setText(String.valueOf(df.format(currentSnapshot.time)));	
					gui.repaint();
				}
				if(currentSnapshotIndex == mapSnapshots.size() - 1)
					timer.stop();	
			}
		});
		
		gui.getPlay().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				timer.start();
			}
		});
		
		gui.getPause().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				timer.stop();
			}
		});
		
		gui.getForward().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(currentSnapshotIndex < mapSnapshots.size()-1) {
					currentSnapshot = mapSnapshots.get(++currentSnapshotIndex);
					gui.getSlider().setValue(currentSnapshotIndex);
					DecimalFormat df = new DecimalFormat("0.00");
					gui.getInfoBottomLabel().setText(String.valueOf(df.format(currentSnapshot.time)));	
				}
				gui.repaint();
			}
		});
		
		gui.getBackward().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(currentSnapshotIndex > 0) {
					currentSnapshot = mapSnapshots.get(--currentSnapshotIndex);
					gui.getSlider().setValue(currentSnapshotIndex);
					DecimalFormat df = new DecimalFormat("0.00");
					gui.getInfoBottomLabel().setText(String.valueOf(df.format(currentSnapshot.time)));	
				}
				gui.repaint();
			}
		});
		
		gui.getReset().addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				currentSnapshotIndex = 0;
				gui.getSlider().setValue(currentSnapshotIndex);
				currentSnapshot = mapSnapshots.get(0);
				DecimalFormat df = new DecimalFormat("0.00");
				gui.getInfoBottomLabel().setText(String.valueOf(df.format(0)));
				gui.repaint();
			}
		});
		
		gui.getSwitcher().addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if(gui.getSwitcher().isSelected())
					gui.getSwitcher().setIcon(new ImageIcon(gui.getOff()));
				else
					gui.getSwitcher().setIcon(new ImageIcon(gui.getOn()));
				gui.getFlood().setSelected(false);
				gui.repaint();
			}
		});
		
		gui.getFlood().addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if(gui.getFlood().isSelected())
					gui.getFlood().setIcon(new ImageIcon(gui.getOff()));
				else
					gui.getFlood().setIcon(new ImageIcon(gui.getOn()));
				gui.getSwitcher().setSelected(false);
				gui.repaint();
			}
		});
				
		addMouseListener(new Mouselistener(this, map.columns, map.rows));	
		gui.repaint();
	}
	
	public void paint(Graphics g){	
		for(int y = 0; y < map.rows; y ++){
			for(int x = 0; x < map.columns; x ++){	
				InkerRep ir = new InkerRep();
				InkerFlood iflood = new InkerFlood();
				if(flooding != null)
					InkerFlood.setMaxV(flooding.getMax());
				if(FFM != null)
					InkerFlood.setMaxV(Simulation.fastMarchingObj.maxElement);
				
				switch(currentSnapshot.mapPure[x][y]){
					case WALKED:
						if(gui.getSwitcher().isSelected()){
							int[] rgb = ir.getInkCodex(map.repulsionmapSnapshots.get(currentSnapshotIndex).valuemap[x][y]);
							g.setColor(new Color(rgb[0], rgb[1], rgb[2]));
							g.fillPolygon(cellsWrapped[x][y].getPolygonCell());
							g.setColor(Color.BLACK);
							g.drawPolygon(cellsWrapped[x][y].getPolygonCell());
						} 
						else if(gui.getFlood().isSelected() && flooding != null && flooding.floodingmap != null)
						{
							int[] rgb = iflood.getInkCodex(flooding.floodingmap[x][y]);
							g.setColor(new Color(rgb[0], rgb[1], rgb[2]));
							g.fillPolygon(cellsWrapped[x][y].getPolygonCell());
							if(flooding.pathToGoal.contains("[" + x + "/" + y + "]"))
							{
								g.setColor(new Color(139, 90, 43, 255));
								g.drawPolygon(cellsWrapped[x][y].getPolygonCell());
							}
							else
							{
								g.setColor(new Color(230, 230, 230));
								g.fillPolygon(cellsWrapped[x][y].getPolygonCell());
								g.setColor(Color.BLACK);
								g.drawPolygon(cellsWrapped[x][y].getPolygonCell());
							}
						}
						else if(gui.getFlood().isSelected() && FFM != null)
						{
							int[] rgb = iflood.getInkCodex(FFM[x][y]);
							g.setColor(new Color(rgb[0], rgb[1], rgb[2]));
							g.fillPolygon(cellsWrapped[x][y].getPolygonCell());
						}
						else{
							g.setColor(new Color(230, 230, 230));
							g.fillPolygon(cellsWrapped[x][y].getPolygonCell());
							g.setColor(Color.BLACK);
							g.drawPolygon(cellsWrapped[x][y].getPolygonCell());
						}
						break;
					case OBSTACLE:
						g.setColor(Color.BLACK);
						g.fillPolygon(cellsWrapped[x][y].getPolygonCell());
						break;
					case AGENT:
						if(constants.MINIONMODE) {
							g.drawImage(minion, x * constants.CELLWIDTH+constants.OFFSET, y * constants.CELLHEIGHT + constants.INFOSPACE, this);
							g.setColor(Color.BLACK);
							g.drawPolygon(cellsWrapped[x][y].getPolygonCell());
						} else {
							g.setColor(Color.BLUE);
							g.fillPolygon(cellsWrapped[x][y].getPolygonCell());
							g.setColor(Color.BLACK);
							g.drawPolygon(cellsWrapped[x][y].getPolygonCell());
						}
						break;
					case GOAL:
						if(constants.MINIONMODE) {
							g.drawImage(banana, x * constants.CELLWIDTH+constants.OFFSET, y * constants.CELLHEIGHT + constants.INFOSPACE, this);
							g.setColor(Color.BLACK);
							g.drawPolygon(cellsWrapped[x][y].getPolygonCell());
						} else {
							g.setColor(Color.YELLOW);
							g.fillPolygon(cellsWrapped[x][y].getPolygonCell());
							g.setColor(Color.BLACK);
							g.drawPolygon(cellsWrapped[x][y].getPolygonCell());
						}
						break;
					case SOURCE:
						g.setColor(Color.RED);
						g.fillPolygon(cellsWrapped[x][y].getPolygonCell());
						g.setColor(Color.BLACK);
						g.drawPolygon(cellsWrapped[x][y].getPolygonCell());
						break;
					default:
						if(gui.getSwitcher().isSelected()){
							int[] rgb = ir.getInkCodex(map.repulsionmapSnapshots.get(currentSnapshotIndex).valuemap[x][y]);
							g.setColor(new Color(rgb[0], rgb[1], rgb[2]));
							g.fillPolygon(cellsWrapped[x][y].getPolygonCell());
							g.setColor(Color.BLACK);
							g.drawPolygon(cellsWrapped[x][y].getPolygonCell());
						}
						else if(gui.getFlood().isSelected() && flooding != null && flooding.floodingmap != null)
						{
							int[] rgb = iflood.getInkCodex(flooding.floodingmap[x][y]);
							g.setColor(new Color(rgb[0], rgb[1], rgb[2]));
							g.fillPolygon(cellsWrapped[x][y].getPolygonCell());
							if(flooding.pathToGoal.contains("[" + x + "/" + y + "]"))
							{
								g.setColor(new Color(139, 90, 43, 255));
								g.fillPolygon(cellsWrapped[x][y].getPolygonCell());
								g.setColor(Color.BLACK);
								g.drawPolygon(cellsWrapped[x][y].getPolygonCell());
							}
							else
							{
								g.setColor(Color.BLACK);
								g.drawPolygon(cellsWrapped[x][y].getPolygonCell());
							}
						}
						else if(gui.getFlood().isSelected() && FFM != null)
						{
							int[] rgb = iflood.getInkCodex(FFM[x][y]);
							g.setColor(new Color(rgb[0], rgb[1], rgb[2]));
							g.fillPolygon(cellsWrapped[x][y].getPolygonCell());
						}
						else{
							g.setColor(Color.WHITE);
							g.fillPolygon(cellsWrapped[x][y].getPolygonCell());
							g.setColor(Color.BLACK);
							g.drawPolygon(cellsWrapped[x][y].getPolygonCell());
						}
				}
			}
		}
		g.setColor(Color.BLACK);
		int h = (int)(Math.log10(map.rows));
		int l = (int)(Math.log10(map.columns));
		Font font = new Font("SansSerif", Font.BOLD, constants.CELLHEIGHT / 2);
		g.setFont(font);
		FontMetrics fm = g.getFontMetrics(font);
		int beginX = 2 * constants.OFFSET / 3 - h * fm.charWidth('7');
		int beginY = constants.INFOSPACE + constants.CELLHEIGHT / 2 + font.getSize() / 2;
		for(int y = 0; y < map.rows; y ++)
			g.drawString(String.valueOf(y), beginX, beginY + y * constants.CELLHEIGHT);
		beginX = constants.OFFSET + constants.CELLWIDTH / 2 - l * fm.charWidth('7') / 2;
		beginY = constants.INFOSPACE + map.rows * constants.CELLHEIGHT + constants.OFFSET / 3 + font.getSize() / 2;
		for(int x = 0; x < map.columns; x ++)
			g.drawString(String.valueOf(x), beginX + x * constants.CELLWIDTH, beginY);
	}
	
	public void showInsight(int col, int row){	
		String str = "<html>that object isn't storing data";
		
		double currenttime = currentSnapshot.time;
		CellType type = currentSnapshot.mapPure[col][row];
		
		//System.out.println(col + "/" + row + ": " + type + " at time " + currenttime);
		
		switch(type){
			case AGENT:
				str = "<html>";
				Agent matchingAgent = null;
				AgentSnapshot matchingAgentSnapshot = null;
				for(Agent agent : Simulation.allAgents){
					matchingAgent = agent;
					matchingAgentSnapshot = agent.getMatchingSnapshot(col, row, currenttime);
					if(matchingAgentSnapshot != null)
						break;						
				}				
				str += "<br><b>agent_" + matchingAgent.ID + "</b>:<br>i was on " + col + "/" + row + " at " + utils.round(currenttime) + "s<br>(arrived at " + utils.round(matchingAgentSnapshot.cellarrivaltime) + "s, left at " + utils.round(matchingAgentSnapshot.celldeparturetime) + "s), <br>" + matchingAgentSnapshot + "<br>";
				
				if(Simulation.algo == Algorithm.DIJKSTRA){
					// hops-values for all cells touched by dijkstra at this point
					flooding = matchingAgent.graph.getFloodingmapSnapshot(currenttime, matchingAgent.chosenGoal, matchingAgent.ID, matchingAgentSnapshot.nowCell, currentSnapshot.mapPure);	
					str += "<br>Ideal path to the goal from here:<br>" + flooding.pathToGoal;
					if(Log.ON)
						Log.write("floodingmap for agent_" + matchingAgent.ID + " at time " + currenttime + ":\n" + flooding.floodingmapToString());
				}			
				break;
			case SOURCE:
				str = "<html>";
				for(Source source : Simulation.sources)
					str += source.ifThatsYouWhatsYourStats(col, row, currenttime); //TODO make like agent
				break;
			case GOAL:
				str = "<html>";
				Goal goal = Simulation.goals.get(col + "/" + row);
				str += goal.whatsYourStats(currenttime);  //TODO make like agent	
				if(Simulation.algo == Algorithm.FASTMARCHING)
				{
					FFM = Simulation.fastMarchingObj.getPotentialMapOfFFM();
				}
				break;
			default:
				break;
		}
		gui.getInfoTopLabel().setText(str + "</html>");	
	}

	
	public MapSnapshot getMapSnapshot(){
		return currentSnapshot;
	}
	
	public Gui getGui(){
		return gui;
	}
	
	public void setFlooding(FloodingmapSnapshot shot){
		this.flooding = shot;
	}
}

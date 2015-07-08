package root.model.math.fastmarching;


import java.awt.Point;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

import root.constants;
import root.utils.CellType;
import root.model.Map;
import root.model.io.Log;
import root.model.mapobjects.Cell;

public class FastMarching
{
	private Map map;
	private double[][] potentialField;
	private boolean[][] frozenDjogurt;
	private PriorityQueue<Cell> H;
	public double maxElement = 0;
	
	public FastMarching(Map map)
	{
		
		this.H = new PriorityQueue<>(new Comparator<Cell>() {
			
			@Override
			public int compare(Cell o1, Cell o2) {
				if(potentialField[o1.col][o1.row] < potentialField[o2.col][o2.row])
				{
					return -1;
				} else if(potentialField[o1.col][o1.row] == potentialField[o2.col][o2.row])
				{
					return 0;
				} else {
					return 1;
				}
			}
		});
		
		this.map = map;
		this.frozenDjogurt = new boolean[map.columns][map.rows];
		this.potentialField = new double[map.columns][map.rows];
		for(double[] r: potentialField)
		{
			for(int i = 0; i < r.length; i++)
			{
				r[i] = Double.MAX_VALUE;
			}
		}
		for(Cell goal: map.goals)
		{
			potentialField[goal.col][goal.row] = 0;
			frozenDjogurt[goal.col][goal.row] = true;
		}
		initialization();
		Loop();
	}
	
	private double F()
	{
		return Math.pow(constants.CELLWIDTH*constants.CELLDIAGONALE_M,2);
//		return 1;
	}
	
	private void initialization()
	{
		for(Cell v: map.goals)
		{
			frozenDjogurt[v.col][v.row] = true;
			for(Cell vn :map.doSomeMagic(v))
			{
				double d = computeArrivalTime(vn);
				if(!frozenDjogurt[vn.col][vn.row])
					if(!H.contains(vn))
					{
						potentialField[vn.col][vn.row] = d;
						H.add(vn);
					}
					else
					{
						H.remove(vn);
						potentialField[vn.col][vn.row] = d;
						H.add(vn);
					}
				
			}
		}
	}
	
	private void Loop()
	{
		while(!H.isEmpty())
		{
			Cell v = H.poll();
			if(Log.ON)
				Log.write("popped: " + potentialField[v.col][v.row]);
			frozenDjogurt[v.col][v.row] = true;
			for(Cell vn: map.doSomeMagic(v))
			{
				if(Log.ON){
					Log.write("\n" + toString(v));
					Log.write("\n" + printStack());
				}
				if(!(map.cells[vn.col][vn.row].type == CellType.OBSTACLE))
				{
					if(!frozenDjogurt[vn.col][vn.row])
					{
						double d = computeArrivalTime(vn);
						if(d > maxElement)
							maxElement = d;
						if(!H.contains(vn))
						{
							
							potentialField[vn.col][vn.row] = d;
							H.add(vn);
						}
						else
						{
//							if(potentialField[vn.col][vn.row] > d)
//							{
								H.remove(vn);
								potentialField[vn.col][vn.row] = d;
								H.add(vn);
//							}
						}
					}
				}
			}
		}
	}
	
	private double computeArrivalTime(Cell vn)
	{
		ArrayList<Point> coords = new ArrayList<>();
		
		for(Cell neighbour : map.doSomeMagic(vn))
		{
			if(frozenDjogurt[neighbour.col][neighbour.row])
			{
				coords.add(new Point(neighbour.col,neighbour.row));
			}
		}
		
		double[] res;
		if(coords.size() >= 2)
		{
			ArrayList<Double> sizeX = new ArrayList<>();
			ArrayList<Double> sizeY = new ArrayList<>();
			for(Point p: coords)
			{
				if(Math.abs(p.x-vn.col) == 1)
					sizeX.add(potentialField[p.x][p.y]);
				else if(Math.abs(p.y-vn.row) == 1)
					sizeY.add(potentialField[p.x][p.y]);
			}
			if(sizeX.size() == 1 && sizeY.size() == 1)
			{
				res = caseTwo(sizeX.get(0),sizeY.get(0));
			} else if((sizeX.size() == 2 && sizeY.size() == 0))
			{
				res = caseOne(Math.min(sizeX.get(0),sizeX.get(1)));
			} else if(sizeX.size() == 0 && sizeY.size() == 2)
			{
				res = caseOne(Math.min(sizeY.get(0),sizeY.get(1)));
			} else if((sizeX.size() == 2 && sizeY.size() == 1))
			{
				res = caseTwo(Math.min(sizeX.get(0),sizeX.get(1)),sizeY.get(0));
			} else if((sizeX.size() == 1 && sizeY.size() == 2))
			{
				res = caseTwo(sizeX.get(0),Math.min(sizeY.get(0),sizeY.get(1)));
			}else if(sizeX.size() == 2 && sizeY.size() == 2)
			{
				res = caseTwo(Math.min(sizeX.get(0),sizeX.get(1)),Math.min(sizeY.get(0),sizeY.get(1)));
			} else
			{
				res = new double[2];
			}
			return Math.max(res[0],res[1]);
		} else {
			Point p = coords.get(0);
			res = caseOne(potentialField[p.x][p.y]);
		}
		return Math.max(res[0], res[1]);
	}
	
	private double[] caseOne(double a)
	{
		double[] res = new double[2];
		res[0] = a + Math.sqrt(F());
		res[1] = a - Math.sqrt(F());
		return res;
	}
	
	private double[] caseTwo(double a, double b)
	{
		double[] res = new double[2];
		res[0] = (a+b)/2 + (0.5 * Math.sqrt(-Math.pow(a, 2)-Math.pow(b, 2)+2*a*b + 2*F()));
		res[1] = (a+b)/2 - (0.5 * Math.sqrt(-Math.pow(a, 2)-Math.pow(b, 2)+2*a*b + 2*F()));
		return res;
	}
	
	public double[][] getPotentialMapOfFFM()
	{
		return potentialField;
	}
	
	public String toString(Cell vn)
	{
		DecimalFormat df = new DecimalFormat("#.0");
		String s = "";
		for(int i = 0; i < map.rows; i++)
		{
			for(int j = 0; j < map.columns; j++)
			{
				if(potentialField[j][i] == Double.MAX_VALUE) {
					s += "[\tXXX\t]";
				} else {
					
					String frozen;
					if(vn.col == j && vn.row == i)
						frozen =(frozenDjogurt[j][i])? "+#":"";
					else
						frozen =(frozenDjogurt[j][i])? "#":"";
					s += "[\t"+frozen+df.format(potentialField[j][i])+"\t]";
				}
			}
			s += "\n";
		}
		return s;
	}
	
	public String printStack()
	{
		DecimalFormat df = new DecimalFormat("#.0");
		String s = "";
		for(Cell c: H)
		{
			s += "| "+df.format(potentialField[c.col][c.row]) + " |\n";
		}
		s+= "\n\n";
		return s;
	}
}
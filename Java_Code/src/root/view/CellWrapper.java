package root.view;

import java.awt.Point;
import java.awt.Polygon;

import root.constants;
import root.model.mapobjects.Cell;


public class CellWrapper{
	
	public Cell cell;
	public Polygon polygonCell; 
	public Point middle;
	
	public CellWrapper(Cell cell, int width, int guiWidth, int height, int guiHeight){
		this.cell = cell;
		
		int xPos = (guiWidth > width) ? width * cell.col * constants.CELLWIDTH / guiWidth : guiWidth * cell.col * constants.CELLWIDTH / width ;
		int yPos = (guiHeight > height) ? height * (cell.row * constants.CELLHEIGHT + constants.INFOSPACE) / guiHeight : guiHeight * (cell.row * constants.CELLHEIGHT + constants.INFOSPACE) / height;
		xPos = xPos + constants.OFFSET;
		polygonCell = new Polygon();
		polygonCell.addPoint(xPos, yPos);
		polygonCell.addPoint(xPos + constants.CELLWIDTH, yPos);
		polygonCell.addPoint(xPos + constants.CELLWIDTH, yPos + constants.CELLHEIGHT);
		polygonCell.addPoint(xPos, yPos + constants.CELLHEIGHT);
		middle = new Point(xPos + constants.CELLWIDTH / 2, yPos + constants.CELLHEIGHT / 2);
	}
	
	public Point getMiddle(){
		return middle;
	}
	
	public Polygon getPolygonCell(){
		return polygonCell;
	}
	
}
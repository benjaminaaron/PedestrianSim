package root.model.mapobjects;

import root.utils;
import root.utils.CellType;

public class Cell {
	
	public int row, col;
	public CellType type;
	
	public Cell(CellType type, int col, int row){
		this.type = type;
		this.row = row;
		this.col = col;
	}
	
	public String toString(){
		return utils.CellTypeToChar(type);
	}
	
	public String coordsStr(){
		return col + "/" + row;
	}
}
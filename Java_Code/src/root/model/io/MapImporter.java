package root.model.io;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import root.utils;
import root.utils.CellType;


public class MapImporter {
	
	public static CellType[][] readBMP(String mapname) throws IOException {
		File bmp = new File(utils.DIR + utils.SEPARATOR + "maps" + utils.SEPARATOR + mapname + ".bmp");			
		
		BufferedImage image = ImageIO.read(bmp);
	    
	    CellType[][] cells = new CellType[image.getWidth()][image.getHeight()];

	    for(int y = 0; y < image.getHeight(); y++)
	    	for(int x = 0; x < image.getWidth(); x++){
	    			    		
	    		int rgb = image.getRGB(x, y); 
	    		/*
	    		int red =   (rgb >> 16) & 0xFF;
	    		int green = (rgb >>  8) & 0xFF;
	    		int blue =  (rgb      ) & 0xFF;	
	            Log.write(x + "/" + y + ": " + red + "/" + green + "/" + blue);
	            */
	            
	    		CellType type = CellType.UNDEFINED;
	    		
	    		if(rgb == Color.WHITE.getRGB()) // FREE
	    			type = CellType.FREE;
	    		if(rgb == Color.BLACK.getRGB()) // OBSTACLE
	    			type = CellType.OBSTACLE;
	    		if(rgb == Color.BLUE.getRGB()) // AGENT
	    			type = CellType.AGENT;
	    		if(rgb == Color.YELLOW.getRGB()) // GOAL
	    			type = CellType.GOAL;
	    		if(rgb == Color.RED.getRGB()) // SOURCE
	    			type = CellType.SOURCE;
	     		cells[x][y] = type;  	
	    	}
	    
		return cells;  		    
	}
}
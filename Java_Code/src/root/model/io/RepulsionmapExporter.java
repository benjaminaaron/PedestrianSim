package root.model.io;

import java.io.FileWriter;
import java.io.IOException;

import root.utils;
import root.model.Map;
import root.model.snapshots.RepulsionmapSnapshot;

public class RepulsionmapExporter {

	public static void doExport(Map map, String filename) {
		FileWriter writer = null;
		try {
			String file = utils.DIR + utils.SEPARATOR + filename + ".csv";			
			writer = new FileWriter(file);	
			
			String commaChain = "";
			for(int i = 0; i < map.columns - 2; i ++)
				commaChain += utils.COMMA_DELIMITER;
				
			writer.append("cols" + utils.COMMA_DELIMITER + map.columns + commaChain + utils.NEW_LINE_SEPARATOR);
			writer.append("rows" + utils.COMMA_DELIMITER + map.rows + commaChain + utils.NEW_LINE_SEPARATOR);
						
			for(RepulsionmapSnapshot snapshot : map.repulsionmapSnapshots){
				writer.append("time" + utils.COMMA_DELIMITER + snapshot.time + commaChain + utils.NEW_LINE_SEPARATOR);
				
				for(int y = 0; y < map.rows; y++){
					for(int x = 0; x < map.columns; x++)
						writer.append(snapshot.valuemap[x][y] + (x == map.columns - 1 ? "" : utils.COMMA_DELIMITER));
					writer.append(utils.NEW_LINE_SEPARATOR);
				}
			}

		} catch (Exception e) { e.printStackTrace(); } 
		finally {
			try {
				writer.flush();
				writer.close();
			}catch (IOException e){
				e.printStackTrace();
			}
		}
	}
	
}
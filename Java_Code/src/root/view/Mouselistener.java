package root.view;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Timer;
import java.util.TimerTask;

import root.constants;

public class Mouselistener implements MouseListener{

	private View view;
	private int columns, rows;
	private Timer timer = new Timer();
	
	public Mouselistener(View view, int columns, int rows){
		this.view = view;
		this.columns = columns;
		this.rows = rows;
	}
	
	private class Counter extends TimerTask{
		@Override
		public void run() {
			view.getGui().getInfoTopLabel().setText("");
			view.setFlooding(null);
			view.getGui().repaint();
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		int x = Math.max((e.getX() - constants.OFFSET), 0) / constants.CELLWIDTH;
		int y = Math.max((e.getY() - constants.INFOSPACE), 0) / constants.CELLHEIGHT;
		if(x < columns && y < rows){	
			timer.schedule(new Counter(), constants.SHOWLABELTIME);
			view.showInsight(x, y);
			view.getGui().repaint();
		}
	}
	
}

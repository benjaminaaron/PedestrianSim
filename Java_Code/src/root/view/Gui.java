package root.view;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import root.constants;
import root.utils;


@SuppressWarnings("serial")
public class Gui extends JFrame{
	
	private JButton play;
	private JButton pause;
	private JButton stepF;
	private JButton stepB;
	private JButton reset;
	private JSlider slider;
	private JToggleButton switcher;
	private JToggleButton floodSwitcher;
	private JPanel rightInfoPanel;
	private JLabel rightInfoTop;
	private JLabel rightInfoBottom;
	private Image on;
	private Image off;
	
	public Gui(int row, int col){
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(dim);
		setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
		int width = getWidth() - constants.OFFSET - 2 * constants.INFOSPACE - 16; 
		int height = getHeight() - constants.OFFSET - constants.INFOSPACE - 39;
		constants.CELLWIDTH = (width/col);
		constants.CELLHEIGHT = (height/row);
		constants.CELLWIDTH = constants.CELLHEIGHT = Math.min(constants.CELLWIDTH, constants.CELLHEIGHT);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setTitle("pedestrian simulation");
		setLayout(null);
		rightInfoPanel = new JPanel();
		rightInfoTop = new JLabel("", SwingConstants.CENTER);
		rightInfoBottom = new JLabel("", SwingConstants.CENTER);
		
		rightInfoPanel.setBounds(width, 0, 2 * constants.INFOSPACE, height + constants.INFOSPACE);
		rightInfoPanel.setBackground(Color.WHITE);
		rightInfoPanel.setBorder(new LineBorder(Color.BLACK));
		rightInfoPanel.setLayout(null);
		
		rightInfoTop.setBounds(0, 0, 2 * constants.INFOSPACE, height);
		rightInfoTop.setVerticalAlignment(SwingConstants.TOP);
		rightInfoTop.setBorder(new LineBorder(Color.BLACK));
		rightInfoTop.setFont(new Font("SansSerif", Font.PLAIN, 16));
		
		rightInfoBottom.setBounds(0, height, 2 * constants.INFOSPACE, constants.INFOSPACE);
		rightInfoBottom.setVerticalAlignment(SwingConstants.CENTER);
		rightInfoBottom.setBorder(new LineBorder(Color.BLACK));
		rightInfoBottom.setFont(new Font("SansSerif", Font.BOLD, 36));
		
		play = new JButton();
		pause = new JButton();
		stepF = new JButton();
		stepB = new JButton();
		reset = new JButton();
		switcher = new JToggleButton();
		floodSwitcher = new JToggleButton();
		
		switcher.setBackground(Color.WHITE);
		floodSwitcher.setBackground(Color.WHITE);
		
		int size = (width / 8 > constants.INFOSPACE / 3) ? constants.INFOSPACE / 3 : width / 8;
		
		play.setBounds(constants.NULLSPACE, 10, size, size);
		pause.setBounds(constants.NULLSPACE + size, 10,size, size);
		stepF.setBounds(constants.NULLSPACE + 3 * size, 10, size, size);
		stepB.setBounds(constants.NULLSPACE + 2 * size, 10, size, size);
		reset.setBounds(constants.NULLSPACE + 9 * size / 2, 10, size, size);
		switcher.setBounds(constants.NULLSPACE + 6 * size, 10, size, size);
		floodSwitcher.setBounds(constants.NULLSPACE + 7 * size, 10, size, size);
		
		Image img, imgP, imgR, imgF, imgB;
		
		try {
			img = resizeToBig(ImageIO.read(new File(utils.DIR + utils.SEPARATOR + "icons" + utils.SEPARATOR  + "PlayButton.png")),size,size);
			imgP = resizeToBig(ImageIO.read(new File(utils.DIR + utils.SEPARATOR + "icons" + utils.SEPARATOR  + "PauseButton.png")),size,size);
			imgR = resizeToBig(ImageIO.read(new File(utils.DIR + utils.SEPARATOR + "icons" + utils.SEPARATOR  + "ResetButton.png")),size,size);
			imgF = resizeToBig(ImageIO.read(new File(utils.DIR + utils.SEPARATOR + "icons" + utils.SEPARATOR  + "Forward.png")),size,size);
			imgB = resizeToBig(ImageIO.read(new File(utils.DIR + utils.SEPARATOR + "icons" + utils.SEPARATOR  + "Backward.png")),size,size);
			on = resizeToBig(ImageIO.read(new File(utils.DIR + utils.SEPARATOR + "icons" + utils.SEPARATOR  + "on.png")), size-8, size-8);
			off = resizeToBig(ImageIO.read(new File(utils.DIR + utils.SEPARATOR + "icons" + utils.SEPARATOR  + "off.png")), size-8, size-8);
			play.setIcon(new ImageIcon(img));
			pause.setIcon(new ImageIcon(imgP));
			reset.setIcon(new ImageIcon(imgR));
			stepF.setIcon(new ImageIcon(imgF));
			stepB.setIcon(new ImageIcon(imgB));
			switcher.setIcon(new ImageIcon(on));
			floodSwitcher.setIcon(new ImageIcon(on));
		} catch (IOException e) { e.printStackTrace(); }
		
		slider = new JSlider();
		slider.setValue(0);
		slider.setBounds(constants.NULLSPACE, 3*size/2, 5 * width / 6, size);

		getContentPane().add(rightInfoPanel);
		rightInfoPanel.add(rightInfoTop);
		rightInfoPanel.add(rightInfoBottom);
		getContentPane().add(slider);
		getContentPane().add(pause);
		getContentPane().add(play);
		getContentPane().add(reset);
		getContentPane().add(stepF);
		getContentPane().add(stepB);
		getContentPane().add(switcher);
		getContentPane().add(floodSwitcher);
	}
	
	public JSlider getSlider(){
		return slider;
	}
	
	public JButton getPlay(){
		return play;
	}
	
	public JButton getPause(){
		return pause;
	}
	
	public JButton getForward(){
		return stepF;
	}
	
	public JButton getBackward(){
		return stepB;
	}
	
	public JButton getReset(){
		return reset;
	}
	
	public JToggleButton getSwitcher(){
		return switcher;
	}

	public JToggleButton getFlood(){
		return floodSwitcher;
	}
	
	public JLabel getInfoTopLabel(){
		return rightInfoTop;
	}
	
	public JLabel getInfoBottomLabel(){
		return rightInfoBottom;
	}
	
	public JPanel getRightInfoPanel() {
		return rightInfoPanel;
	}
	
	public Image getOn()
	{
		return on;
	}
	
	public Image getOff()
	{
		return off;
	}
	
	public Image resizeToBig(Image originalImage, int biggerWidth, int biggerHeight) {
	    int type = BufferedImage.TYPE_INT_ARGB;

	    BufferedImage resizedImage = new BufferedImage(biggerWidth, biggerHeight, type);
	    Graphics2D g = resizedImage.createGraphics();

	    g.setComposite(AlphaComposite.Src);
	    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	    g.drawImage(originalImage, 0, 0, biggerWidth, biggerHeight, this);
	    g.dispose();

	    return resizedImage;
	}
}

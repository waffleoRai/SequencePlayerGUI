package waffleoRai_seqplayerGUI.channeldisp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

public class ChannelIndexDisplay extends JPanel{

	/*----- Constants -----*/
	
	private static final long serialVersionUID = -7143374910734875302L;
	
	public static final int WIDTH = 55;
	public static final int HEIGHT = 85;
	public static final int HEIGHT_SHRUNK = 55;
	
	public static final Font DEFO_FONT = new Font("Courier New", Font.BOLD, 48);
	public static final int OFF_X = 14;
	public static final int OFF_Y = 55;
	public static final int OFF_Y_SHRUNK = 43;

	/*----- Instance Variables -----*/
	
	private boolean expanded;
	private int value; //Right now, up to one hex digit
	
	/*----- Construction -----*/
	
	public ChannelIndexDisplay(){
		
		//Stand-in version
		setBackground(Color.DARK_GRAY);
		setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		setMinimumSize(new Dimension(WIDTH, HEIGHT));
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		
		value = -1;
		expanded = true;
		
		updateImage();
	}
	
	/*----- Update -----*/
	
	public void setExpanded(){
		expanded = true;
		updateImage();
	}
	
	public void setContracted(){
		expanded = false;
		updateImage();
	}
	
	public void setValue(int i){
		value = i;
		updateImage();
	}
	
	public void updateImage(){
		if(expanded)
		{
			setMinimumSize(new Dimension(WIDTH, HEIGHT));
			setPreferredSize(new Dimension(WIDTH, HEIGHT));
		}
		else
		{
			setMinimumSize(new Dimension(WIDTH, HEIGHT_SHRUNK));
			setPreferredSize(new Dimension(WIDTH, HEIGHT_SHRUNK));
		}
		repaint();
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.setColor(getBackground());
		g.drawRect(0, 0, WIDTH, HEIGHT);
		
		Font f = g.getFont();
		g.setFont(DEFO_FONT);
		g.setColor(Color.RED);
		
		String disp = "-";
		if(value >= 0)
		{
			disp = String.format("%1X", value & 0xF);
		}
		
		if(expanded) g.drawString(disp, OFF_X, OFF_Y);
		else g.drawString(disp, OFF_X, OFF_Y_SHRUNK);
		
		g.setFont(f);
	}
	
	
}

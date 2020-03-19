package waffleoRai_seqplayerGUI.channeldisp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

public class ProgramNumberDisplay extends JPanel{

	/*----- Constants -----*/
	
	private static final long serialVersionUID = -7143374910734875302L;
	
	public static final int WIDTH = 50;
	public static final int HEIGHT = 43;
	
	public static final Font DEFO_FONT = new Font("Courier New", Font.BOLD, 24);
	public static final int OFF_X = 4;
	public static final int OFF_Y = 28;

	/*----- Instance Variables -----*/
	
	private int value; //Right now, up to three decimal digits
	
	/*----- Construction -----*/
	
	public ProgramNumberDisplay(){
		
		//Stand-in version
		setBackground(Color.DARK_GRAY);
		setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		setMinimumSize(new Dimension(WIDTH, HEIGHT));
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		
		value = -1;
		
		updateImage();
	}
	
	/*----- Update -----*/
	
	public void setValue(int i){
		value = i;
		updateImage();
	}
	
	public void updateImage(){
		setMinimumSize(new Dimension(WIDTH, HEIGHT));
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
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
			disp = String.format("%03d", value);
		}
		
		g.drawString(disp, OFF_X, OFF_Y);
		g.setFont(f);
	}
	

}

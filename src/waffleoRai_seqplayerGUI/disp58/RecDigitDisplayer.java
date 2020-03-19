package waffleoRai_seqplayerGUI.disp58;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class RecDigitDisplayer extends JPanel{

	private static final long serialVersionUID = -8264041032992029844L;
	
	public static final int PIX_SIZE = 4;
	public static final int SPACE_SIZE = 1;
	
	public static final Color COLOR_OFF = new Color(11, 36, 45, 255);
	public static final Color COLOR_ON = new Color(133, 205, 242, 255);
	
	private boolean[][] pattern;
	
	private int p_width;
	private int p_height;
	
	public RecDigitDisplayer(int width, int height)
	{
		pattern = new boolean[width][height];
		p_width = width;
		p_height = height;
		
		//Calculate my size...
		int w = (width * (PIX_SIZE + SPACE_SIZE)) - 1;
		int h = (height * (PIX_SIZE + SPACE_SIZE)) - 1;
		
		setMinimumSize(new Dimension(w, h));
		setPreferredSize(new Dimension(w, h));
	}
	
	public void paint(Graphics g)
	{
		int x = 0;
		int y = 0;
		for(int r = 0; r < p_height; r++)
		{
			for(int l = 0; l < p_width; l++)
			{
				if(pattern == null) g.setColor(COLOR_OFF);
				else{
					if(pattern[l][r]) g.setColor(COLOR_ON);
					else g.setColor(COLOR_OFF);
				}
				g.fillRect(x, y, PIX_SIZE, PIX_SIZE);
				x += PIX_SIZE + SPACE_SIZE;
			}
			x = 0;
			y += PIX_SIZE + SPACE_SIZE;
		}
		
	}
	
	public boolean setPattern(boolean[][] p)
	{
		if(p == null){pattern = null; repaint(); return true;}
		if(p.length != p_width) return false;
		if(p[0].length != p_height) return false;
		pattern = p;
		repaint();
		return true;
	}

}

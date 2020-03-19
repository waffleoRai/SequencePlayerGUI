package waffleoRai_seqplayerGUI.disp58;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

public class StringDisplayer58 extends JPanel implements ActionListener{
	
	private static final long serialVersionUID = -1071581289480066124L;
	
	public static final int BETWEEN_PIX = 2;
	
	private boolean scroll;
	private RecDigitDisplayer[] digits;
	
	private String str;
	private int startchar;
	private int startdig;
	
	private int millis = 300;
	private int hold_cycles = 6;
	private int c_ctr;
	private Timer timer;
	
	public StringDisplayer58(int size, boolean scroll)
	{
		digits = new RecDigitDisplayer[size];
		this.scroll = scroll;
		
		int x = 0;
		for(int i = 0; i < size; i++){
			digits[i] = new RecDigitDisplayer(5,8);
			digits[i].setBounds(x, 0, digits[i].getWidth(), digits[i].getHeight());
			add(digits[i]);
			x += digits[i].getWidth() + BETWEEN_PIX;
		}
		
		//Determine & set size
		int h = (8 * (RecDigitDisplayer.PIX_SIZE + RecDigitDisplayer.SPACE_SIZE)) - RecDigitDisplayer.SPACE_SIZE;
		int w = (size * ((5 * (RecDigitDisplayer.PIX_SIZE + RecDigitDisplayer.SPACE_SIZE)) - RecDigitDisplayer.SPACE_SIZE) + BETWEEN_PIX) - BETWEEN_PIX;
		
		setMinimumSize(new Dimension(w, h));
		setPreferredSize(new Dimension(w, h));
		
		if(this.scroll){timer = new Timer(millis, this); timer.start();}
		startdig = size-1;
	}
	
	public void setString(String s)
	{
		str = s;
		startchar = 0;
		updateDigits();
	}
	
	private void updateDigits()
	{
		if(startchar >= 0)
		{
			//Normal scrolling
			int slen = str.length();
			for(int i = 0; i < digits.length; i++)
			{
				int idx = i+startchar;
				if(idx < slen)
				{
					char c = str.charAt(idx);
					digits[i].setPattern(AsciiSquareDispPatterns.getPatternFor(c));
				}
				else digits[i].setPattern(null);
			}
		}
		else
		{
			//Front is scrolling back in
			int slen = str.length();
			for(int i = startdig; i < digits.length; i++)
			{
				int idx = i-startdig;
				if(idx < slen)
				{
					char c = str.charAt(idx);
					digits[i].setPattern(AsciiSquareDispPatterns.getPatternFor(c));
				}
				else digits[i].setPattern(null);
			}
		}
		repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if((startchar == 0) && (c_ctr++ < hold_cycles)) return;
		c_ctr = 0;

		updateDigits();
		
		//Advance counters
		if(startchar < 0)
		{
			startdig--;
			if(startdig <= 0)
			{
				startchar = 0;
			}
		}
		else
		{
			startchar++;
			if(startchar >= str.length())
			{
				startchar = -1;
				startdig = digits.length-1;
			}
		}
		
	}
	
	public void dispose()
	{
		if(timer != null) timer.stop();
	}

}

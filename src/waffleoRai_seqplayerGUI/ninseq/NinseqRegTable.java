package waffleoRai_seqplayerGUI.ninseq;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class NinseqRegTable extends JPanel{

	private static final long serialVersionUID = -4513734262546291101L;
	
	private static final Font font = new Font("Courier New", Font.PLAIN, 10);
	private static final Font font_hdr = new Font("Courier New", Font.BOLD, 11);
	
	public static final int COLHDR_HEIGHT = 15;
	public static final int ROWHDR_WIDTH = 20;
	
	public static final int ROW_HEIGHT = 20;
	public static final int COL_WIDTH = 35;
	public static final int SPACING = 3;
	
	public static final int TOP_INSET = 5;
	public static final int LEFT_INSET = 5;
	public static final int BOTTOM_INSET = 5;
	public static final int RIGHT_INSET = 5;
	
	private boolean dec_mode; //If true decimal, if false hex
	
	private JLabel[] col_headers; //Up to 16
	private JLabel[] row_headers; //Up to 26
	private JTextField[] boxes; //256
	
	public NinseqRegTable(){

		col_headers = new JLabel[16];
		row_headers = new JLabel[26];
		boxes = new JTextField[256];
		
		for(int i = 0; i < 16; i++){
			JLabel lbl = new JLabel(Integer.toString(i));
			lbl.setHorizontalTextPosition(SwingConstants.CENTER);
			lbl.setFont(font_hdr);
			col_headers[i] = lbl;
		}
		
		for(int i = 0; i < 26; i++){
			JLabel lbl = new JLabel(Integer.toString(i));
			lbl.setHorizontalTextPosition(SwingConstants.RIGHT);
			lbl.setFont(font_hdr);
			row_headers[i] = lbl;
		}
		
		for(int i = 0; i < 256; i++){
			JTextField txt = new JTextField("0");
			txt.setFont(font);
			boxes[i] = txt;
		}
		
		setLayout(null);
		setDecimalMode();
	}
	
	private void drawDecMode(){

		short[] values = getValues();
		this.removeAll();
		
		int x = 0;
		int y = TOP_INSET;
		
		//Draw column headers
		x = LEFT_INSET + SPACING + ROWHDR_WIDTH;
		for(int l = 0; l < 10; l++){
			JLabel hdr = col_headers[l];
			hdr.setText(Integer.toString(l));
			hdr.setBounds(x, y, COL_WIDTH, COLHDR_HEIGHT);
			this.add(hdr);
			hdr.repaint();
			x += COL_WIDTH + SPACING;
		}
		y += COLHDR_HEIGHT + SPACING;
		
		//10 columns, 26 rows
		for(int r = 0; r < 26; r++){
			x = LEFT_INSET;
			
			//Row header
			JLabel hdr = row_headers[r];
			hdr.setText(String.format("%02d", r));
			hdr.setBounds(x, y, ROWHDR_WIDTH, ROW_HEIGHT);
			this.add(hdr);
			hdr.repaint();
			x += ROWHDR_WIDTH + SPACING;
			
			//Boxes
			int z = r*10;
			for(int l = 0; l < 10; l++){
				int i = z+l;
				if(i < 256){
					JTextField txt = boxes[i];	
					txt.setBounds(x, y, COL_WIDTH, ROW_HEIGHT);
					add(txt);
					txt.repaint();
				}
				x += COL_WIDTH + SPACING;
			}
			
			y += ROW_HEIGHT + SPACING;
		}
		
		loadValues(values);
		
		//Set panel size
		setMinimumSize(new Dimension(x,y));
		setPreferredSize(new Dimension(x,y));
	}
	
	private void drawHexMode(){
		
		short[] values = getValues();
		this.removeAll();
		
		int x = 0;
		int y = TOP_INSET;
		
		//Draw column headers
		x = LEFT_INSET + SPACING + ROWHDR_WIDTH;
		for(int l = 0; l < 16; l++){
			JLabel hdr = col_headers[l];
			hdr.setText(Integer.toHexString(l));
			hdr.setBounds(x, y, COL_WIDTH, COLHDR_HEIGHT);
			this.add(hdr);
			hdr.repaint();
			x += COL_WIDTH + SPACING;
		}
		y += COLHDR_HEIGHT + SPACING;
		
		for(int r = 0; r < 16; r++){
			x = LEFT_INSET;
			
			//Row header
			JLabel hdr = row_headers[r];
			hdr.setText(Integer.toHexString(r));
			hdr.setBounds(x, y, ROWHDR_WIDTH, ROW_HEIGHT);
			this.add(hdr);
			hdr.repaint();
			x += ROWHDR_WIDTH + SPACING;
			
			//Boxes
			int z = r*16;
			for(int l = 0; l < 16; l++){
				int i = z+l;
				if(i < 256){
					JTextField txt = boxes[i];	
					txt.setBounds(x, y, COL_WIDTH, ROW_HEIGHT);
					add(txt);
					txt.repaint();
				}
				x += COL_WIDTH + SPACING;
			}
			
			y += ROW_HEIGHT + SPACING;
		}
		
		loadValues(values);
		
		//Set panel size
		setMinimumSize(new Dimension(x,y));
		setPreferredSize(new Dimension(x,y));
	}
	
	public void setDecimalMode(){
		dec_mode = true;
		drawDecMode();
	}
	
	public void setHexMode(){
		dec_mode = false;
		drawHexMode();
	}
	
	public short[] getValues(){

		short[] out = new short[256];
		for(int i = 0; i < 256; i++){
			String s = boxes[i].getText();
			int v = 0;
			try{
				if(dec_mode) v = Integer.parseInt(s);
				else v = Integer.parseUnsignedInt(s, 16);
			}
			catch(NumberFormatException e){
				boxes[i].setText("0");
			}
			
			//Saturate
			if(dec_mode){
				if(v > 0x7FFF) v = 0x7FFF;
				else if (v < 0xFFFF8000) v = 0xFFFF8000;
			}
			else{
				if(v > 0xFFFF) v = 0xFFFF;
				if(v < 0) v = 0;
			}
			
			out[i] = (short)v;
		}
		
		return out;
	}
	
	public void loadValues(short[] values){
		if(values == null) return;
		
		for(int i = 0; i < 256; i++){
			if(i >= values.length){
				continue;
			}
			if(dec_mode){
				int v = (int)values[i];
				boxes[i].setText(Integer.toString(v));
			}
			else{
				int v = Short.toUnsignedInt(values[i]);
				boxes[i].setText(String.format("%04x", v));
			}
			boxes[i].repaint();
		}
		
	}
	
	public void setValue(int i, short val){
		if(i < 0 || i >= 256) return;
		
		if(dec_mode){
			int v = (int)val;
			boxes[i].setText(Integer.toString(v));
		}
		else{
			int v = Short.toUnsignedInt(val);
			boxes[i].setText(String.format("%04x", v));
		}
		boxes[i].repaint();
	}

}

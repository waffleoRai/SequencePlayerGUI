package waffleoRai_seqplayerGUI.channeldisp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import waffleoRai_SeqSound.MIDI;

public class ActiveVoicesDisplay extends JPanel{

	/*----- Constants -----*/
	
	private static final long serialVersionUID = -6694225575033220677L;
	
	public static final int WIDTH = 275;
	public static final int HEIGHT = 22;
	
	public static final Font DEFO_FONT = new Font("Courier New", Font.BOLD, 11);
	
	public static final int MAX_VOICES_DISPLAYED = 8;
	
	/*----- Instance Variables -----*/
	
	private int on_voices;
	private Map<Byte, JLabel> onmap;
	private Queue<Byte> pending;
	private JLabel plus;
	
	/*----- Construction -----*/
	
	public ActiveVoicesDisplay(){
		
		//Stand-in version
		setBackground(Color.DARK_GRAY);
		setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		
		setLayout(new FlowLayout(FlowLayout.LEFT, 5, 2));
		
		setMinimumSize(new Dimension(WIDTH, HEIGHT));
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		
		on_voices = 0;
		onmap = new ConcurrentHashMap<Byte, JLabel>();
		pending = new ConcurrentLinkedQueue<Byte>();
	}
	
	/*----- Control -----*/
	
	public void voiceOn(byte key){
		String s = "+";
		
		//Toss if key is already being displayed
		if(onmap.containsKey(key)) return;
		if(pending.contains(key)) return;
		
		if(on_voices >= MAX_VOICES_DISPLAYED){
			pending.add(key);
			if(plus == null)
			{
				JLabel l = new JLabel(s);
				l.setForeground(Color.CYAN);
				l.setFont(DEFO_FONT);
				this.add(l);
				plus = l;
			}
		}
		else{
			s = MIDI.getNoteName(key);
			
			JLabel l = new JLabel(s);
			l.setForeground(Color.CYAN);
			l.setFont(DEFO_FONT);
			this.add(l);
			onmap.put(key, l);
			on_voices++;
		}
		updateUI();
		repaint();
	}
	
	public void voiceOff(byte key){
		
		//Check for it in the main map...
		JLabel lbl = onmap.remove(key); on_voices--;
		if(lbl != null)
		{
			//See if any other voices are queued for that spot...
			remove(lbl);
			if(!pending.isEmpty())
			{
				byte k = pending.poll();
				voiceOn(k);
				if(plus != null && pending.isEmpty()) {remove(plus); plus = null;}
			}
		}
		else
		{
			//Pull from pending
			boolean b = pending.remove(key);
			if(b)
			{
				if(plus != null && pending.isEmpty()) {remove(plus); plus = null;}
			}
		}
		updateUI();
		repaint();
		
	}
	
	public void clear(){
		onmap.clear();
		pending.clear();
		plus = null;
		on_voices = 0;
		this.removeAll();
		this.updateUI();
	}
	
}

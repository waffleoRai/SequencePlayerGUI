package waffleoRai_seqplayerGUI;

import javax.swing.JPanel;
import javax.swing.border.SoftBevelBorder;

import waffleoRai_SoundSynth.ChannelStateListener;
import waffleoRai_SoundSynth.SynthPlayer;
import waffleoRai_seqplayerGUI.channeldisp.ActiveVoicesDisplay;
import waffleoRai_seqplayerGUI.channeldisp.ChannelIndexDisplay;
import waffleoRai_seqplayerGUI.channeldisp.ProgramNumberDisplay;

import javax.swing.border.BevelBorder;
import javax.swing.JProgressBar;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import java.awt.Font;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.JSeparator;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

public class ChannelViewPanel extends JPanel implements ChannelStateListener, ActionListener{
	
	/*----- Constants -----*/
	
	private static final long serialVersionUID = 6038125972849067770L;
	
	public static final int WIDTH = 365;
	public static final int HEIGHT_NORMAL = 165;
	public static final int HEIGHT_SHRUNK = 80;
	
	public static final int CHIDX_DISPLAY_WIDTH = 55;
	public static final int CHIDX_DISPLAY_HEIGHT = 85;
	public static final int CHIDX_DISPLAY_HEIGHT_SHRUNK = 55;
	
	public static final int REFRESH_RATE_HZ = 25;
	public static final int REFRESH_RATE_MILLIS = 40;
	
	public static final int EVENTTYPE_LEVELCHANGE = 1;
	public static final int EVENTTYPE_NOTE_ON = 2;
	public static final int EVENTTYPE_NOTE_OFF = 3;
	public static final int EVENTTYPE_PROGRAM_CHANGE = 4;
	public static final int EVENTTYPE_PITCH_WHEEL = 5;
	public static final int EVENTTYPE_MOD_WHEEL = 6;
	public static final int EVENTTYPE_VOL_CHANGE = 7;
	public static final int EVENTTYPE_PAN_CHANGE = 8;
	
	/*----- Instance Variables -----*/
	
	//---- GUI State
	private boolean expanded;
	
	//---- GUI Elements
	private JButton btnSize;
	
	private ChannelIndexDisplay pnlChidx;
	private ActiveVoicesDisplay pnlVox;
	private ProgramNumberDisplay pnlProg;
	
	private JProgressBar barLeft;
	private JProgressBar barRight;
	
	private JLabel lblLeft;
	private JLabel lblRight;
	
	private JButton btnSolo;
	private JButton btnMute;
	
	private JSlider sldPitch;
	private JSlider sldMod;
	private JSlider sldVol;
	private JSlider sldPan;
	private JPanel pnlEfx;
	
	//---Channel
	private SynthPlayer player;
	private int channel_idx;
	private double max_level;
	
	private boolean muted;
	private boolean soloed;
	
	private Timer timer; //50hz (20ms)
	private int samplerate;
	private long mytime;
	private int rctr; //Refreshes in this current second
	private int rctr2;
	
	private ConcurrentMap<Long, Queue<CallbackEvent>> callbacks;
	
	private double mleft;
	private double mright;
	
	/*----- Construction -----*/
	
	public ChannelViewPanel(SynthPlayer p, int ch) {
		expanded = true;
		callbacks = new ConcurrentHashMap<Long, Queue<CallbackEvent>>();
		
		initGUI();
		
		timer = new Timer(REFRESH_RATE_MILLIS, this);
		setPlayer(p, ch);
		
	}

	private void initGUI(){
		setMinimumSize(new Dimension(WIDTH, HEIGHT_NORMAL));
		setPreferredSize(new Dimension(WIDTH, HEIGHT_NORMAL));
		
		setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		setLayout(null);
		
		barLeft = new JProgressBar();
		barLeft.setBounds(87, 36, 146, 14);
		add(barLeft);
		
		pnlChidx = new ChannelIndexDisplay();
		//pnlChidx.setBackground(Color.DARK_GRAY);
		//pnlChidx.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		pnlChidx.setBounds(2, 23, CHIDX_DISPLAY_WIDTH, CHIDX_DISPLAY_HEIGHT);
		//pnlChidx.setValue(0);
		add(pnlChidx);
		
		JLabel lblChannel = new JLabel("CHANNEL");
		lblChannel.setFont(new Font("Courier New", Font.BOLD, 11));
		lblChannel.setHorizontalAlignment(SwingConstants.CENTER);
		lblChannel.setBounds(1, 5, 55, 14);
		add(lblChannel);
		
		pnlVox = new ActiveVoicesDisplay();
		//pnlVox.setBackground(Color.DARK_GRAY);
		//pnlVox.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		pnlVox.setBounds(55, 3, ActiveVoicesDisplay.WIDTH, ActiveVoicesDisplay.HEIGHT);
		add(pnlVox);
		
		lblLeft = new JLabel("-Inf. dB");
		lblLeft.setBounds(240, 36, 55, 14);
		add(lblLeft);
		
		JLabel lblL = new JLabel("L");
		lblL.setFont(new Font("Courier New", Font.BOLD, 11));
		lblL.setHorizontalAlignment(SwingConstants.CENTER);
		lblL.setBounds(65, 36, 15, 14);
		add(lblL);
		
		JLabel lblR = new JLabel("R");
		lblR.setHorizontalAlignment(SwingConstants.CENTER);
		lblR.setFont(new Font("Courier New", Font.BOLD, 11));
		lblR.setBounds(65, 55, 15, 14);
		add(lblR);
		
		barRight = new JProgressBar();
		barRight.setBounds(87, 55, 146, 14);
		add(barRight);
		
		lblRight = new JLabel("-Inf. dB");
		lblRight.setBounds(240, 55, 55, 14);
		add(lblRight);
		
		btnSolo = new JButton("S");
		btnSolo.setToolTipText("Solo");
		btnSolo.setBounds(2, 112, 55, 23);
		add(btnSolo);
		btnSolo.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				onSoloChannel();
			}
			
		});
		
		btnMute = new JButton("M");
		btnMute.setToolTipText("Mute");
		btnMute.setBounds(2, 137, 55, 23);
		add(btnMute);
		btnMute.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				onMuteChannel();
			}
			
		});
		
		sldPitch = new JSlider();
		sldPitch.setToolTipText("Pitch Wheel");
		sldPitch.setMinimum(-100);
		sldPitch.setValue(0);
		sldPitch.setFont(new Font("Tahoma", Font.PLAIN, 9));
		sldPitch.setBounds(67, 84, 101, 26);
		add(sldPitch);
		
		sldMod = new JSlider();
		sldMod.setToolTipText("Mod Wheel");
		sldMod.setValue(0);
		sldMod.setMinimum(-100);
		sldMod.setBounds(194, 84, 101, 26);
		add(sldMod);
		
		sldVol = new JSlider();
		sldVol.setToolTipText("Channel Volume");
		sldVol.setBounds(65, 112, 230, 26);
		add(sldVol);
		
		sldPan = new JSlider();
		sldPan.setToolTipText("Channel Pan");
		sldPan.setMinimum(-100);
		sldPan.setValue(0);
		sldPan.setBounds(65, 137, 230, 26);
		add(sldPan);
		
		pnlEfx = new JPanel();
		pnlEfx.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		pnlEfx.setBackground(Color.DARK_GRAY);
		pnlEfx.setBounds(307, 84, 50, 76);
		add(pnlEfx);
		pnlEfx.setLayout(null);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(67, 80, 288, 2);
		add(separator);
		
		pnlProg = new ProgramNumberDisplay();
		//pnlProg.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		//pnlProg.setBackground(Color.DARK_GRAY);
		pnlProg.setBounds(307, 30, ProgramNumberDisplay.WIDTH, ProgramNumberDisplay.HEIGHT);
		add(pnlProg);
		//pnlProg.setValue(127);
		//pnlProg.setLayout(null);
		
		btnSize = new JButton("-");
		btnSize.setMargin(new Insets(2, 2, 2, 2));
		btnSize.setFont(new Font("Tahoma", Font.PLAIN, 8));
		btnSize.setBounds(333, 6, 24, 19);
		add(btnSize);
		btnSize.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(expanded) setContracted();
				else setExpanded();
			}
			
		});
	}
	
	/*----- Layout -----*/
	
	public void setContracted(){
		if(!expanded) return; //Already contracted
		
		//Set hidden components to invisible/disabled
		btnSolo.setVisible(false); btnSolo.setEnabled(false);
		btnMute.setVisible(false); btnMute.setEnabled(false);
		sldPitch.setVisible(false); sldPitch.setEnabled(false);
		sldMod.setVisible(false); sldMod.setEnabled(false);
		sldVol.setVisible(false); sldVol.setEnabled(false);
		sldPan.setVisible(false); sldPan.setEnabled(false);
		pnlEfx.setVisible(false);
		
		//Shrink components that need it
		pnlChidx.setContracted();
		
		//Shrink panel
		setMinimumSize(new Dimension(WIDTH, HEIGHT_SHRUNK));
		setPreferredSize(new Dimension(WIDTH, HEIGHT_SHRUNK));
		
		//Update button appearance
		btnSize.setText("+");
		
		expanded = false;
		repaint();
	}
	
	public void setExpanded(){
		if(expanded) return; //Already expanded
		
		//Set hidden components to visible
		btnSolo.setVisible(true); btnSolo.setEnabled(true);
		btnMute.setVisible(true); btnMute.setEnabled(true);
		sldPitch.setVisible(true); sldPitch.setEnabled(true);
		sldMod.setVisible(true); sldMod.setEnabled(true);
		sldVol.setVisible(true); sldVol.setEnabled(true);
		sldPan.setVisible(true); sldPan.setEnabled(true);
		pnlEfx.setVisible(true);
		
		//Expand components that need it
		pnlChidx.setExpanded();
				
		//Expand panel
		setMinimumSize(new Dimension(WIDTH, HEIGHT_NORMAL));
		setPreferredSize(new Dimension(WIDTH, HEIGHT_NORMAL));
				
		//Update button appearance
		btnSize.setText("-");
		
		expanded = true;
		repaint();
	}

	/*----- GUI Update -----*/
	
	private double[] convertLevels(int[] in){
		double[] out = new double[2]; //Stereo display
		if(in == null) return out;
		
		int v = Math.abs(in[0]);
		out[0] = (double)v / max_level;
		
		if(in.length < 2) out[1] = out[0];
		else
		{
			v = Math.abs(in[1]);
			out[1] = (double)v / max_level;
		}
		
		return out;
	}
	
	private void setBarLevels(double[] vals){
		//Left
		barLeft.setValue(Math.min((int)Math.round(vals[0] * 100.0), 100));
		barLeft.repaint();
		if(vals[0] > mleft) mleft = vals[0];
		
		//Right
		barRight.setValue(Math.min((int)Math.round(vals[1] * 100.0), 100));
		barRight.repaint();
		if(vals[1] > mright) mright = vals[1];
		
		
		if(mleft > 0.0)
		{
			double db = 20.0 * Math.log10(mleft);
			if(mleft > 1.0) lblLeft.setForeground(Color.RED);
			else lblLeft.setForeground(Color.BLACK);
			lblLeft.setText(String.format("%.1f", db) + " dB");
		}
		else
		{
			lblLeft.setForeground(Color.BLACK);
			lblLeft.setText("-Inf dB");
		}
		lblLeft.repaint();
		
		if(mright > 0.0)
		{
			double db = 20.0 * Math.log10(mright);
			if(mright > 1.0) lblRight.setForeground(Color.RED);
			else lblRight.setForeground(Color.BLACK);
			lblRight.setText(String.format("%.1f", db) + " dB");
		}
		else
		{
			lblRight.setForeground(Color.BLACK);
			lblRight.setText("-Inf dB");
		}
		lblRight.repaint();
				
	}
	
	public void actionPerformed(ActionEvent e){
		long nxttime = player.getPlaybackPosition();
		
		if(++rctr == REFRESH_RATE_HZ)
		{
			rctr = 0;
			if(++rctr2 == 5)
			{
				rctr2 = 0;
				mleft = 0.0;
				mright = 0.0;	
			}
		}
		
		Map<Integer, CallbackEvent> events = new HashMap<Integer, CallbackEvent>();
		CallbackEvent bestlvl = null;
		for(long i = mytime; i < nxttime; i++)
		{
			Queue<CallbackEvent> elist = callbacks.remove(i);
			if(elist != null)
			{
				for(CallbackEvent v : elist)
				{
					if(v.multi()) v.complete();
					else
					{
						if(v.getEventType() == EVENTTYPE_LEVELCHANGE){
							if(bestlvl == null) bestlvl = v;
							else{
								if(v.getValue() >= bestlvl.getValue()) bestlvl = v;
							}
						}
						else{
							events.put(v.getEventType(), v);
						}
					}
				}
			}
		}
		
		//Execute level update
		if(bestlvl == null) setBarLevels(new double[2]);
		else bestlvl.complete();
		
		//Execute other events
		for(CallbackEvent v : events.values()) v.complete();
		
		mytime = nxttime;
		
	}
	
	/*----- Getters -----*/
	
	public int getSetSamplerate(){
		return samplerate;
	}
	
	/*----- Setters -----*/
	
	private void setNullPlayer(){
		channel_idx = -1;
		max_level = 0x7FFFFFFF;
		
		muted = false;
		soloed = false;
		
		btnMute.setForeground(Color.BLACK); btnMute.setEnabled(false); btnMute.repaint();
		btnSolo.setForeground(Color.BLACK); btnSolo.setEnabled(false); btnSolo.repaint();
		
		//pnlChidx.setValue(-1);
		//pnlProg.setValue(-1);
	}
	
	public void setPlayer(SynthPlayer p, int channel){
		reset();
		if(p == null) {setNullPlayer(); return;}
		if(player != null) player.removeChannelListener(channel_idx, this);
		
		channel_idx = channel;
		player = p;
		samplerate = (int)player.getSampleRate();
		int bd = p.getBitDepth();
		
		switch(bd)
		{
		case 8: max_level = 0x7F; break;
		case 16: max_level = 0x7FFF; break;
		case 24: max_level = 0x7FFFFF; break;
		case 32: max_level = 0x7FFFFFFF; break;
		default: max_level = 0x7FFFFFFF; break;
		}
		
		muted = p.channelMuted(channel);
		soloed = p.channelSoloed(channel);
		
		btnMute.setEnabled(true);
		btnSolo.setEnabled(true);
		if(muted) btnMute.setForeground(Color.BLUE);
		else btnMute.setForeground(Color.BLACK);
		btnMute.repaint();
		if(soloed) btnSolo.setForeground(Color.BLUE);
		else btnSolo.setForeground(Color.BLACK);
		btnSolo.repaint();
		
		pnlChidx.setValue(channel);
		
		p.addChannelListener(channel, this);
	}
	
	public void setChannel(int channel){
		reset();
		player.removeChannelListener(channel_idx, this);
		
		channel_idx = channel;
		muted = player.channelMuted(channel);
		soloed = player.channelSoloed(channel);
		
		if(muted) btnMute.setForeground(Color.BLUE);
		else btnMute.setForeground(Color.BLACK);
		btnMute.repaint();
		if(soloed) btnSolo.setForeground(Color.BLUE);
		else btnSolo.setForeground(Color.BLACK);
		btnSolo.repaint();
		
		pnlChidx.setValue(channel);
		
		player.addChannelListener(channel, this);
	}
	
	/*----- Callbacks -----*/
	
	private interface CallbackEvent{
		public void complete();
		public int getEventType();
		public int getValue();
		public boolean multi();
	}
	
	private class LevelChangeCallback implements CallbackEvent{

		private int[] value;
		
		public LevelChangeCallback(int[] rawvals){
			value = rawvals;
		}
		
		@Override
		public void complete() {
			setBarLevels(convertLevels(value));
		}

		@Override
		public int getEventType() {
			return EVENTTYPE_LEVELCHANGE;
		}

		@Override
		public int getValue() {
			if(value == null) return 0;
			int v0 = Math.abs(value[0]);
			if(value.length < 2) return v0;
			
			return (v0 + Math.abs(value[1]))/2;
		}
		
		public boolean multi(){return false;}
	}

	private class NoteOnCallback implements CallbackEvent{

		private byte value;
		
		public NoteOnCallback(byte key){
			value = key;
		}
		
		@Override
		public void complete() {
			pnlVox.voiceOn(value);
		}

		@Override
		public int getEventType() {
			return EVENTTYPE_NOTE_ON;
		}

		@Override
		public int getValue() {
			return value;
		}
		
		public boolean multi(){return true;}
	}
	
	private class NoteOffCallback implements CallbackEvent{

		private byte value;
		
		public NoteOffCallback(byte key){
			value = key;
		}
		
		@Override
		public void complete() {
			pnlVox.voiceOff(value);
		}

		@Override
		public int getEventType() {
			return EVENTTYPE_NOTE_OFF;
		}

		@Override
		public int getValue() {
			return value;
		}
		
		public boolean multi(){return true;}
		
	}
	
	private class ProgramChangeCallback implements CallbackEvent{

		private int value;
		
		public ProgramChangeCallback(int val){
			value = val;
		}
		
		@Override
		public void complete() {
			pnlProg.setValue(value);
			pnlProg.repaint();
		}

		@Override
		public int getEventType() {
			return EVENTTYPE_PROGRAM_CHANGE;
		}

		@Override
		public int getValue() {
			return value;
		}
		
		public boolean multi(){return false;}
	}
	
	private class PitchWheelCallback implements CallbackEvent{

		private short value;
		
		public PitchWheelCallback(short val){
			value = val;
		}
		
		@Override
		public void complete() {
			double ratio = (double)value/(double)0x7FFF;
			sldPitch.setValue((int)Math.round(ratio * 100.0));
			sldPitch.repaint();
		}

		@Override
		public int getEventType() {
			return EVENTTYPE_PITCH_WHEEL;
		}

		@Override
		public int getValue() {
			return value;
		}
		
		public boolean multi(){return false;}
	}

	private class ModWheelCallback implements CallbackEvent{

		private short value;
		
		public ModWheelCallback(short val){
			value = val;
		}
		
		@Override
		public void complete() {
			double ratio = (double)value/(double)0x7FFF;
			sldMod.setValue((int)Math.round(ratio * 100.0));
			sldMod.repaint();
		}

		@Override
		public int getEventType() {
			return EVENTTYPE_MOD_WHEEL;
		}

		@Override
		public int getValue() {
			return value;
		}
		
		public boolean multi(){return false;}
	}
	
	private class VolumeCallback implements CallbackEvent{

		private double value;
		
		public VolumeCallback(double val){
			value = val;
		}
		
		@Override
		public void complete() {
			sldVol.setValue((int)Math.round(value * 100.0));
			sldVol.repaint();
		}

		@Override
		public int getEventType() {
			return EVENTTYPE_VOL_CHANGE;
		}

		@Override
		public int getValue() {
			return (int)value;
		}
		
		public boolean multi(){return false;}
	}
	
	private class PanCallback implements CallbackEvent{

		private byte value;
		
		public PanCallback(byte val){
			value = val;
		}
		
		@Override
		public void complete() {
			if(value == 0x40) sldPan.setValue(0);
			else
			{
				int raw = value - 0x40;
				double ratio = (double)raw/(double)0x3F;
				sldPan.setValue((int)Math.round(ratio * 100.0));
			}
			
			sldPan.repaint();
		}

		@Override
		public int getEventType() {
			return EVENTTYPE_PAN_CHANGE;
		}

		@Override
		public int getValue() {
			return (int)value;
		}
		
		public boolean multi(){return false;}
	}
	
	/*----- Control -----*/
	
	public void reset(){
		if(timer.isRunning()) timer.stop();
		mleft = 0.0; mright = 0.0;
		setBarLevels(new double[2]);
		callbacks.clear();
		muted = false;
		soloed = false;
		
		pnlVox.clear();
		pnlChidx.setValue(-1);
		pnlProg.setValue(-1);
		
		sldPan.setValue(0);
		sldVol.setValue(100);
		sldPitch.setValue(0);
		sldMod.setValue(0);
		
		mytime = 0;
		rctr = 0;
		rctr2 = 0;
		
		pnlChidx.setValue(channel_idx);
	}
	
	public void startTimer(){
		timer.start();
	}
	
	public void stopTimer(){
		timer.stop();
	}
	
	private void addToMap(long time, CallbackEvent e){
		Queue<CallbackEvent> q = callbacks.get(time);
		if(q == null){
			q = new ConcurrentLinkedQueue<CallbackEvent>();
			callbacks.put(time, q);
		}
		q.add(e);
	}
	
	@Override
	public void sendLevel(int[] level, long time) {
		addToMap(time, new LevelChangeCallback(level));
	}

	@Override
	public void onNoteOn(byte note, long time) {
		addToMap(time, new NoteOnCallback(note));
	}

	@Override
	public void onNoteOff(byte note, long time) {
		addToMap(time, new NoteOffCallback(note));
	}

	@Override
	public void setProgram(int bank, int program, long time) {
		addToMap(time, new ProgramChangeCallback(program));
	}

	@Override
	public void onPitchWheelSet(short value, long time) {
		addToMap(time, new PitchWheelCallback(value));
	}

	@Override
	public void onModWheelSet(short value, long time) {
		addToMap(time, new ModWheelCallback(value));
	}

	@Override
	public void onVolumeSet(double level, long time) {
		addToMap(time, new VolumeCallback(level));
	}

	@Override
	public void onPanSet(byte pan, long time) {
		addToMap(time, new PanCallback(pan));
	}

	@Override
	public void onEffectSet(int effect, boolean b, long time) {
		// TODO Auto-generated method stub
		
	}
	
	public void onMuteChannel(){
		muted = !muted;
		player.setChannelMute(channel_idx, muted);
		
		if(muted) btnMute.setForeground(Color.BLUE);
		else btnMute.setForeground(Color.BLACK);
		btnMute.repaint();
	}
	
	public void onSoloChannel(){
		soloed = !soloed;
		player.setChannelSolo(channel_idx, soloed);
		
		if(soloed) btnSolo.setForeground(Color.BLUE);
		else btnSolo.setForeground(Color.BLACK);
		btnSolo.repaint();
	}
	

	
}

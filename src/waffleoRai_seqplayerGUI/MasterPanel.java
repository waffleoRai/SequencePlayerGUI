package waffleoRai_seqplayerGUI;

import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JSlider;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import waffleoRai_SoundSynth.PlayerListener;
import waffleoRai_SoundSynth.SynthPlayer;

import javax.sound.midi.MidiUnavailableException;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class MasterPanel extends JPanel implements PlayerListener, ActionListener{

	/*----- Constants -----*/
	
	private static final long serialVersionUID = -7592313629434342920L;
	
	private static final int EVENT_TYPE_TEMPO = 1;
	private static final int EVENT_TYPE_TICK = 2;
	private static final int EVENT_TYPE_LEVEL = 3;
	
	public static final int WIDTH = 310;
	public static final int HEIGHT = 440;
	
	public static final int REFRESH_RATE_HZ = 25;
	public static final int REFRESH_RATE_MILLIS = 40;
	
	/*----- Instance Variables -----*/
	
	private JLabel lblTick;
	private JLabel lblTempo;
	
	private JTextField txtSeqName;
	private JTextField txtBankName;
	private JTextField txtSampleRate;
	private JTextField txtBitDepth;
	private JTextField txtTypeInfo;
	
	private JProgressBar barLeft;
	private JProgressBar barRight;
	
	private JButton btnPlay;
	private JButton btnRewind;
	private JButton btnStop;
	
	private JLabel lblMasterLevel;
	private JLabel lblLeft;
	private JLabel lblRight;
	private JSlider sldMaster;
	
	private Icon ico_play;
	private Icon ico_pause;
	private Icon ico_stop;
	private Icon ico_rewind;
	
	private SynthPlayer player;
	private double max_level;
	private volatile boolean playing;
	
	private Timer timer; //50hz (20ms)
	private int samplerate;
	private long mytime;
	private int rctr; //Refreshes in this current second
	private int rctr2;
	
	private ConcurrentMap<Long, Queue<CallbackEvent>> callbacks;
	//private Queue<int[]> levels;
	
	private double mleft;
	private double mright;
	
	private List<PlayControlListener> listeners;
	
	/*----- Construction -----*/
	
	public MasterPanel(SynthPlayer plr){
		
		//Load icon images...
		ico_play = new ImageIcon(MasterPanel.class.getResource("/waffleoRai_seqplayerGUI/res/seqbasic_play_med.png"));
		ico_pause = new ImageIcon(MasterPanel.class.getResource("/waffleoRai_seqplayerGUI/res/seqbasic_pause_med.png"));
		ico_stop = new ImageIcon(MasterPanel.class.getResource("/waffleoRai_seqplayerGUI/res/seqbasic_stop_med.png"));
		ico_rewind = new ImageIcon(MasterPanel.class.getResource("/waffleoRai_seqplayerGUI/res/seqbasic_rewind_med.png"));
		
		callbacks = new ConcurrentHashMap<Long, Queue<CallbackEvent>>();
		//levels = new ConcurrentLinkedQueue<int[]>();
		
		timer = new Timer(REFRESH_RATE_MILLIS, this);
		
		initGUI();
		
		setPlayer(plr);
		listeners = new LinkedList<PlayControlListener>();
	}
	
	private void initGUI(){
		
		setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		setLayout(null);
		setMinimumSize(new Dimension(WIDTH, HEIGHT));
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		
		JLabel lblNowPlaying = new JLabel("NOW PLAYING");
		lblNowPlaying.setFont(new Font("Courier New", Font.BOLD, 12));
		lblNowPlaying.setBounds(10, 15, 83, 14);
		add(lblNowPlaying);
		
		JLabel lblOnBank = new JLabel("ON BANK");
		lblOnBank.setFont(new Font("Courier New", Font.BOLD, 12));
		lblOnBank.setBounds(10, 42, 59, 14);
		add(lblOnBank);
		
		txtSeqName = new JTextField();
		txtSeqName.setBounds(99, 11, 201, 20);
		add(txtSeqName);
		txtSeqName.setColumns(10);
		
		txtBankName = new JTextField();
		txtBankName.setBounds(99, 39, 201, 20);
		add(txtBankName);
		txtBankName.setColumns(10);
		
		JPanel panel = new JPanel();
		panel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		panel.setBounds(10, 127, 290, 231);
		add(panel);
		panel.setLayout(null);
		
		barLeft = new JProgressBar();
		barLeft.setBounds(10, 27, 27, 161);
		panel.add(barLeft);
		barLeft.setOrientation(SwingConstants.VERTICAL);
		
		barRight = new JProgressBar();
		barRight.setBounds(195, 27, 27, 161);
		panel.add(barRight);
		barRight.setOrientation(SwingConstants.VERTICAL);
		
		sldMaster = new JSlider();
		sldMaster.setBounds(243, 27, 27, 161);
		panel.add(sldMaster);
		sldMaster.setOrientation(SwingConstants.VERTICAL);
		sldMaster.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent e) {
				onMasterVolSliderPull();
				//if(!sldMaster.getValueIsAdjusting()) onMasterVolumeChange(sldMaster.getValue());
			}
			
		});
		
		btnRewind = new JButton("");
		btnRewind.setIcon(ico_rewind);
		btnRewind.setBounds(47, 38, 64, 64);
		panel.add(btnRewind);
		btnRewind.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				onRewind();
			}
			
		});
		
		btnStop = new JButton("");
		btnStop.setIcon(ico_stop);
		btnStop.setBounds(47, 113, 64, 64);
		panel.add(btnStop);
		btnStop.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				onStop();
			}
			
		});
		
		btnPlay = new JButton("");
		btnPlay.setIcon(ico_play);
		btnPlay.setBounds(121, 70, 64, 64);
		panel.add(btnPlay);
		btnPlay.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(playing) onPause();
				else onPlay();
			}
			
		});
		
		JLabel lblL = new JLabel("L");
		lblL.setHorizontalAlignment(SwingConstants.CENTER);
		lblL.setBounds(16, 189, 17, 23);
		panel.add(lblL);
		lblL.setFont(new Font("Tahoma", Font.BOLD, 20));
		
		JLabel lblR = new JLabel("R");
		lblR.setHorizontalAlignment(SwingConstants.CENTER);
		lblR.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblR.setBounds(200, 189, 17, 23);
		panel.add(lblR);
		
		JLabel lblMaster = new JLabel("Master");
		lblMaster.setHorizontalAlignment(SwingConstants.CENTER);
		lblMaster.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblMaster.setBounds(235, 189, 46, 14);
		panel.add(lblMaster);
		
		lblMasterLevel = new JLabel("-6.0 dB");
		lblMasterLevel.setHorizontalAlignment(SwingConstants.CENTER);
		lblMasterLevel.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblMasterLevel.setBounds(235, 16, 46, 14);
		panel.add(lblMasterLevel);
		
		lblLeft = new JLabel("-Inf dB");
		lblLeft.setHorizontalAlignment(SwingConstants.CENTER);
		lblLeft.setBounds(0, 10, 46, 14);
		panel.add(lblLeft);
		
		lblRight = new JLabel("-Inf dB");
		lblRight.setHorizontalAlignment(SwingConstants.CENTER);
		lblRight.setBounds(187, 10, 46, 14);
		panel.add(lblRight);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		panel_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_1.setBounds(10, 70, 290, 46);
		add(panel_1);
		panel_1.setLayout(null);
		
		lblTempo = new JLabel("120");
		lblTempo.setBounds(10, 11, 39, 23);
		panel_1.add(lblTempo);
		lblTempo.setFont(new Font("Courier New", Font.BOLD, 18));
		
		JLabel lblBpm = new JLabel("bpm");
		lblBpm.setBounds(47, 17, 28, 14);
		panel_1.add(lblBpm);
		lblBpm.setFont(new Font("Courier New", Font.PLAIN, 11));
		
		lblTick = new JLabel("Tick: 0");
		lblTick.setBounds(118, 16, 136, 14);
		panel_1.add(lblTick);
		lblTick.setFont(new Font("Courier New", Font.BOLD, 13));
		
		txtSampleRate = new JTextField();
		txtSampleRate.setBounds(10, 369, 141, 20);
		add(txtSampleRate);
		txtSampleRate.setColumns(10);
		
		txtBitDepth = new JTextField();
		txtBitDepth.setBounds(161, 369, 139, 20);
		add(txtBitDepth);
		txtBitDepth.setColumns(10);
		
		txtTypeInfo = new JTextField();
		txtTypeInfo.setBounds(10, 400, 290, 20);
		add(txtTypeInfo);
		txtTypeInfo.setColumns(10);
	}
	
	/*----- Getters -----*/
	
	public SynthPlayer getPlayer(){return player;}
	
	/*----- Setters -----*/
	
	private void setNullPlayer(){
		txtSeqName.setText("<Empty>"); txtSeqName.repaint();
		txtBankName.setText("<Empty>"); txtBankName.repaint();
	
		samplerate = 44100;
		txtSampleRate.setText("<N/A>"); txtSampleRate.repaint();
		txtBitDepth.setText("<N/A>"); txtBitDepth.repaint();
		txtTypeInfo.setText("<N/A>"); txtTypeInfo.repaint();
		
		//samples_per_refresh = samplerate/REFRESH_RATE_HZ;
		//extra_samples_per_second = samplerate%REFRESH_RATE_HZ;
		
		max_level = 0x7FFF;
	}
	
	public void setPlayer(SynthPlayer p){
		rewind();
		player = p;
		if(p == null){setNullPlayer(); return;}
		
		txtSeqName.setText(p.getSequenceName()); txtSeqName.repaint();
		txtBankName.setText(p.getBankName()); txtBankName.repaint();
	
		samplerate = (int)p.getSampleRate();
		int bd = p.getBitDepth();
		
		if(player.isCapturable())
		{
			txtSampleRate.setText(samplerate + " hz"); txtSampleRate.repaint();
			txtBitDepth.setText(bd + " bits"); txtBitDepth.repaint();
		}
		else
		{
			txtSampleRate.setText("<SYS>"); txtSampleRate.repaint();
			txtBitDepth.setText("<SYS>"); txtBitDepth.repaint();
		}
			
		txtTypeInfo.setText(p.getTypeInfoString()); txtTypeInfo.repaint();
		//samples_per_refresh = samplerate/REFRESH_RATE_HZ;
		//extra_samples_per_second = samplerate%REFRESH_RATE_HZ;
		
		switch(bd)
		{
		case 8: max_level = 0x7F; break;
		case 16: max_level = 0x7FFF; break;
		case 24: max_level = 0x7FFFFF; break;
		case 32: max_level = 0x7FFFFFFF; break;
		default: max_level = 0x7FFFFFFF; break;
		}
		
		sldMaster.setEnabled(player.isCapturable());
		
		player.addListener(this);
	}
	
	/*----- Callbacks -----*/
	
	private interface CallbackEvent{
		public void complete();
		public int getEventType();
		public int getValue();
	}
	
	private class TempoChangeCallback implements CallbackEvent{
		
		private int bpm;
		public TempoChangeCallback(int val){
			bpm = val;
		}
		
		public int getEventType(){return EVENT_TYPE_TEMPO;}
		
		public void complete(){
			//System.err.println("Changing tempo to " + bpm);
			lblTempo.setText(Integer.toString(bpm));
			lblTempo.repaint();
		}
		
		public int getValue(){return bpm;}
	}
	
	private class TickChangeCallback implements CallbackEvent{
		
		private long value;
		public TickChangeCallback(long val){
			value = val;
		}
		
		public int getEventType(){return EVENT_TYPE_TICK;}
		
		public void complete(){
			lblTick.setText("Tick: " + value);
			lblTick.repaint();
		}
	
		public int getValue(){return (int)value;}
	}
	
	public class LevelChangeCallback implements CallbackEvent{
		
		private int[] value;
		public LevelChangeCallback(int[] val){
			value = val;
		}
		
		public int getValue(){
			if(value == null) return 0;
			int v0 = Math.abs(value[0]);
			if(value.length < 2) return v0;
			return (v0 + Math.abs(value[1]))/2;
		}
		
		public int getEventType(){return EVENT_TYPE_LEVEL;}
		
		public void complete(){
			setBarLevels(convertLevels(value));
		}
	}
	
	/*----- GUI Update -----*/
	
	public void setBarLevels(double[] vals){
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
	
	public void refresh(){
		txtSeqName.setText(player.getSequenceName());
		txtSeqName.repaint();
		
		txtBankName.setText(player.getBankName());
		txtBankName.repaint();
	}
	
	/*----- Listeners -----*/
	
	private void addToMap(long time, CallbackEvent e){
		Queue<CallbackEvent> q = callbacks.get(time);
		if(q == null){
			q = new ConcurrentLinkedQueue<CallbackEvent>();
			callbacks.put(time, q);
		}
		q.add(e);
	}
	
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
	
	public void onTempoChange(int usPerBeat, long time){
		//System.err.println("Tempo change submitted time = " + time);
		int bpm = (int)Math.round(60000000.0/(double)usPerBeat);
		addToMap(time, new TempoChangeCallback(bpm));
	}
	
	public void onTick(long tick, long time){
		addToMap(time, new TickChangeCallback(tick));
	}
	
	public void sendLevel(int[] level, long time){
		addToMap(time, new LevelChangeCallback(level));
	}
		
	public void actionPerformed(ActionEvent e) {
		//long nxttime = mytime + samples_per_refresh;
		long nxttime = player.getPlaybackPosition();
		
		if(++rctr == REFRESH_RATE_HZ)
		{
			//nxttime += extra_samples_per_second;
			rctr = 0;
			if(++rctr2 == 5)
			{
				rctr2 = 0;
				mleft = 0.0;
				mright = 0.0;	
			}
			//System.err.println("Refresh: " + OffsetDateTime.now().toString());
		}
		
		//Grab latest tick & tempo change (remove the rest)
		CallbackEvent tempochange = null;
		CallbackEvent tickchange = null;
		CallbackEvent levelchange = null;
		//System.err.println("mytime = " + mytime + "; nexttime = " + nxttime);
		for(long i = nxttime-1; i >= mytime; i--)
		{
			Queue<CallbackEvent> events = callbacks.remove(i);
			if(events != null)
			{
				for(CallbackEvent ev : events)
				{
					if(ev.getEventType() == EVENT_TYPE_TEMPO && tempochange == null) tempochange = ev;
					else if(ev.getEventType() == EVENT_TYPE_TICK && tickchange == null) tickchange = ev;
					else if (ev.getEventType() == EVENT_TYPE_LEVEL)
					{
						if(levelchange == null) levelchange = ev;
						else
						{
							//Only replace if value is higher
							if(ev.getValue() > levelchange.getValue()) levelchange = ev;
						}
					}
				
				}
			}
		}
		mytime = nxttime;
		if(tempochange != null) tempochange.complete();
		if(tickchange != null) tickchange.complete();
		if(levelchange != null) levelchange.complete();
		else setBarLevels(new double[2]);
	}

	public void addListener(PlayControlListener l){
		listeners.add(l);
	}
	
	public void clearListeners(){
		listeners.clear();
	}
	
	public void onSequenceEnd() {
		playing = false;
		timer.stop();
		
		player.rewind();
		rewind();
		btnPlay.setIcon(ico_play);
		btnPlay.repaint();
		for(PlayControlListener l : listeners) l.onStop();
	}
	
	/*----- Actions -----*/
	
	private void rewind(){
		if(timer.isRunning()) timer.stop();
		playing = false;
		mytime = 0;
		rctr = 0;
		rctr2 = 0;
		callbacks.clear();
		//levels.clear();
		
		lblTick.setText("Tick: 0"); lblTick.repaint();
		setBarLevels(new double[2]);
	}
	
	public void onPlay(){
		//Resumes or starts play wherever player is sitting
		//System.err.println("Play");
		btnPlay.setIcon(ico_pause);
		btnPlay.repaint();
		sldMaster.setValue(Math.min(100, (int)Math.round(player.getMasterAttenuation() * 100.0)));
		
		playing = true;
		//timer.start();
		if(!player.isPaused())
		{
			try {
				player.startAsyncPlaybackToDefaultOutputDevice();
			} catch (LineUnavailableException e) {
				e.printStackTrace();
				showError("Playback to default device failed! (See stderr)");
			} catch (MidiUnavailableException e) {
				e.printStackTrace();
				showError("Default MIDI device could not be initialized! (See stderr)");
			}
		}
		else{player.unpause();}
		//start_time = OffsetDateTime.now();
		timer.start();
		for(PlayControlListener l : listeners) l.onPlay();
	}
	
	public void onStop(){
		//System.err.println("Stop");
		if(!playing) return; 
		
		playing = false;
		player.stop();
		timer.stop();
		player.rewind();
		rewind();
		btnPlay.setIcon(ico_play);
		btnPlay.repaint();
		for(PlayControlListener l : listeners) l.onStop();
		//System.err.println("onStop() returning");
	}
	
	public void onRewind(){
		//System.err.println("Rewind");
		if(playing) onStop();
		else
		{
			playing = false;
			player.rewind();
			rewind();
		}
		for(PlayControlListener l : listeners) l.onRewind();
	}
	
	public void onPause(){
		//System.err.println("Pause");
		if(!playing) return; 
		
		playing = false;
		player.pause();
		timer.stop();
		btnPlay.setIcon(ico_play);
		btnPlay.repaint();
		setBarLevels(new double[2]);
		for(PlayControlListener l : listeners) l.onPause();
	}
	
	private void onMasterVolSliderPull(){
		int value = sldMaster.getValue();
		double val = (double)value/100.0;
		if(val == 0.0)
		{
			lblMasterLevel.setText("-Inf dB");
			lblMasterLevel.repaint();
			return;
		}
		
		double db = 20.0 * Math.log10(val);
		
		lblMasterLevel.setText(String.format("%.1f", db) + " dB");
		lblMasterLevel.repaint();
		onMasterVolumeChange(value);
	}
	
	public void onMasterVolumeChange(int value){
		double val = (double)value/100.0;
		player.setMasterAttenuation(val);
		//onMasterVolSliderPull();
		//System.err.println("Volume set to: " + player.getMasterAttenuation());
	}

	/*----- Cleanup -----*/
	
	public void dispose(){
		if(timer.isRunning()) timer.stop();
		if(callbacks != null){
			for(Queue<CallbackEvent> v : callbacks.values()) v.clear();
		}
		callbacks.clear();
		
		//Delist listeners
		if(player != null)player.removeListener(this);
	}
	
	/*----- Error -----*/
	
	public void showError(String text){
		JOptionPane.showMessageDialog(this, text, "Error", JOptionPane.ERROR_MESSAGE);
	}

	


	
}

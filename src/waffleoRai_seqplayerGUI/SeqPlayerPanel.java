package waffleoRai_seqplayerGUI;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;

import waffleoRai_SoundSynth.SynthPlayer;

import javax.swing.BoxLayout;

public class SeqPlayerPanel extends JPanel implements PlayControlListener{

	/*--- Constants ---*/
	
	private static final long serialVersionUID = 3491386903025201146L;
	
	public static final int MIN_WIDTH = 700;
	public static final int HEIGHT = 440;

	/*----- Instance Variables -----*/
	
	private MasterPanel pnlMid;
	
	private JScrollPane spRight;
	private JPanel pnlRight;
	
	private ChannelViewPanel[] chpnls;
	
	/*----- Construction -----*/
	
	public SeqPlayerPanel(SynthPlayer p){
		initGUI(p);
		
	}
	
	private void initGUI(SynthPlayer p){
		
		setMinimumSize(new Dimension(MIN_WIDTH, HEIGHT));
		setPreferredSize(new Dimension(MIN_WIDTH, HEIGHT));
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {0, 310, 365, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JPanel pnlLeft = new JPanel();
		GridBagConstraints gbc_pnlLeft = new GridBagConstraints();
		gbc_pnlLeft.weightx = 0.1;
		gbc_pnlLeft.insets = new Insets(0, 0, 0, 5);
		gbc_pnlLeft.fill = GridBagConstraints.BOTH;
		gbc_pnlLeft.gridx = 0;
		gbc_pnlLeft.gridy = 0;
		add(pnlLeft, gbc_pnlLeft);
		
		pnlMid = new MasterPanel(p);
		pnlMid.setMaximumSize(pnlMid.getPreferredSize());
		GridBagConstraints gbc_pnlMid = new GridBagConstraints();
		gbc_pnlMid.weightx = 0.7;
		gbc_pnlMid.insets = new Insets(0, 0, 0, 5);
		gbc_pnlMid.gridx = 1;
		gbc_pnlMid.gridy = 0;
		add(pnlMid, gbc_pnlMid);
		pnlMid.addListener(this);
		
		spRight = new JScrollPane();
		spRight.setViewportBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		GridBagConstraints gbc_spRight = new GridBagConstraints();
		gbc_spRight.weightx = 0.7;
		gbc_spRight.fill = GridBagConstraints.BOTH;
		gbc_spRight.gridx = 2;
		gbc_spRight.gridy = 0;
		add(spRight, gbc_spRight);
		
		pnlRight = new JPanel();
		spRight.setViewportView(pnlRight);
		pnlRight.setLayout(new BoxLayout(pnlRight, BoxLayout.Y_AXIS));
		//pnlRight.setMinimumSize(new Dimension(ChannelViewPanel.WIDTH+ 10, HEIGHT));
		//pnlRight.setPreferredSize(new Dimension(ChannelViewPanel.WIDTH+ 10, HEIGHT));
		
		addChannelPanels(p);
	}

	private void addChannelPanels(SynthPlayer p){
		pnlRight.removeAll();
		int chcount = 16;
		if(p != null) chcount = p.getSynthChannelCount();
		chpnls = new ChannelViewPanel[chcount];
		
		for(int i = 0; i < chcount; i++){
			ChannelViewPanel pnl = new ChannelViewPanel(p, i);
			pnl.setContracted();
			chpnls[i] = pnl;
			pnlRight.add(pnl);
		}
		
		pnlRight.updateUI();
		pnlRight.repaint();
		spRight.repaint();
	}

	@Override
	public void onPlay() {
		for(ChannelViewPanel p : chpnls) p.startTimer();
	}

	@Override
	public void onStop() {
		for(ChannelViewPanel p : chpnls) {p.stopTimer(); p.reset();}
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRewind() {
		for(ChannelViewPanel p : chpnls) p.reset();
	}
	
}

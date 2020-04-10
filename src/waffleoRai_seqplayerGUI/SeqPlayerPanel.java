
package waffleoRai_seqplayerGUI;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

import waffleoRai_SoundSynth.SynthPlayer;

import javax.swing.BoxLayout;

public class SeqPlayerPanel extends JPanel implements PlayControlListener{

	/*--- Constants ---*/
	
	private static final long serialVersionUID = 3491386903025201146L;
	
	public static final int MIN_WIDTH = 700;
	public static final int HEIGHT = 440;

	/*----- Instance Variables -----*/
	
	private JScrollPane spLeft;
	private JPanel pnlLeft;
	
	private MasterPanel pnlMaster;
	private JPanel pnlCustom; //Can set custom panel
	
	private JScrollPane spRight;
	private JPanel pnlRight;
	
	private ChannelViewPanel[] chpnls;
	
	/*----- Construction -----*/
	
	/**
	 * @wbp.parser.constructor
	 */
	public SeqPlayerPanel(SynthPlayer p){
		initGUI(p, null);
		
	}
	
	public SeqPlayerPanel(SynthPlayer p, JPanel customPanel){
		initGUI(p, customPanel);
		
	}
	
	private void initGUI(SynthPlayer p, JPanel custom){
		
		setMinimumSize(new Dimension(MIN_WIDTH, HEIGHT));
		setPreferredSize(new Dimension(MIN_WIDTH, HEIGHT));
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {365, 310, 365, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 4.9E-324};
		setLayout(gridBagLayout);
		
		spLeft = new JScrollPane();
		spLeft.setViewportBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		GridBagConstraints gbc_spLeft = new GridBagConstraints();
		gbc_spLeft.weightx = 0.7;
		gbc_spLeft.fill = GridBagConstraints.BOTH;
		gbc_spLeft.gridx = 0;
		gbc_spLeft.gridy = 0;
		gbc_spLeft.gridheight = 2;
		add(spLeft, gbc_spLeft);
		
		pnlLeft = new JPanel();
		spLeft.setViewportView(pnlLeft);
		pnlLeft.setLayout(new BoxLayout(pnlLeft, BoxLayout.Y_AXIS));
		
		pnlMaster = new MasterPanel(p);
		pnlMaster.setMaximumSize(pnlMaster.getPreferredSize());
		GridBagConstraints gbc_pnlMid = new GridBagConstraints();
		gbc_pnlMid.weightx = 0.7;
		gbc_pnlMid.insets = new Insets(10, 0, 0, 5);
		gbc_pnlMid.gridx = 1;
		gbc_pnlMid.gridy = 0;
		add(pnlMaster, gbc_pnlMid);
		pnlMaster.addListener(this);
		
		if(custom == null) custom = new JPanel();
		pnlCustom = custom;
		pnlCustom.setBorder(new EtchedBorder());
		GridBagConstraints gbc_pnlCustom = new GridBagConstraints();
		gbc_pnlCustom.weightx = 0.7;
		gbc_pnlCustom.insets = new Insets(0, 0, 0, 5);
		gbc_pnlCustom.gridx = 1;
		gbc_pnlCustom.gridy = 1;
		gbc_pnlCustom.fill = GridBagConstraints.BOTH;
		add(pnlCustom, gbc_pnlCustom);
		
		spRight = new JScrollPane();
		spRight.setViewportBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		GridBagConstraints gbc_spRight = new GridBagConstraints();
		gbc_spRight.weightx = 0.7;
		gbc_spRight.fill = GridBagConstraints.BOTH;
		gbc_spRight.gridx = 2;
		gbc_spRight.gridy = 0;
		gbc_spRight.gridheight = 2;
		add(spRight, gbc_spRight);
		
		pnlRight = new JPanel();
		spRight.setViewportView(pnlRight);
		pnlRight.setLayout(new BoxLayout(pnlRight, BoxLayout.Y_AXIS));
		//pnlRight.setMinimumSize(new Dimension(ChannelViewPanel.WIDTH+ 10, HEIGHT));
		//pnlRight.setPreferredSize(new Dimension(ChannelViewPanel.WIDTH+ 10, HEIGHT));
		
		addChannelPanels(p);
	}

	private void addChannelPanels(SynthPlayer p){
		
		pnlLeft.removeAll();
		int chcount = 16;
		if(p != null) chcount = p.getSynthChannelCount();
		chpnls = new ChannelViewPanel[chcount];
		
		int half = chcount/2;
		for(int i = 0; i < half; i++){
			ChannelViewPanel pnl = new ChannelViewPanel(p, i);
			pnl.setContracted();
			chpnls[i] = pnl;
			pnlLeft.add(pnl);
		}
		
		pnlLeft.updateUI();
		pnlLeft.repaint();
		spLeft.repaint();
		
		
		pnlRight.removeAll();
		for(int i = half; i < chcount; i++){
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

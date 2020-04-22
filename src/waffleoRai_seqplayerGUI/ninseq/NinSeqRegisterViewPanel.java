package waffleoRai_seqplayerGUI.ninseq;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;

import waffleoRai_GUITools.RadioButtonGroup;
import waffleoRai_SeqSound.ninseq.NinSeqPlayerValueChangeListener;
import waffleoRai_SeqSound.ninseq.NinSeqSynthPlayer;

import javax.swing.JRadioButton;
import javax.swing.JButton;

public class NinSeqRegisterViewPanel extends JPanel implements NinSeqPlayerValueChangeListener{

	private static final long serialVersionUID = 7491531616432865568L;
	
	private RadioButtonGroup rbg;
	private NinseqRegTable pnlTable;
	private NinSeqSynthPlayer player;

	public NinSeqRegisterViewPanel(){
		rbg = new RadioButtonGroup(2);
		initGUI();	
	}
	
	private void initGUI(){
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JPanel pnlControl = new JPanel();
		GridBagConstraints gbc_pnlControl = new GridBagConstraints();
		gbc_pnlControl.insets = new Insets(5, 5, 5, 5);
		gbc_pnlControl.fill = GridBagConstraints.BOTH;
		gbc_pnlControl.gridx = 0;
		gbc_pnlControl.gridy = 0;
		add(pnlControl, gbc_pnlControl);
		GridBagLayout gbl_pnlControl = new GridBagLayout();
		gbl_pnlControl.columnWidths = new int[] {0, 0, 0, 0};
		gbl_pnlControl.rowHeights = new int[] {0};
		gbl_pnlControl.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0};
		gbl_pnlControl.rowWeights = new double[]{0.0};
		pnlControl.setLayout(gbl_pnlControl);
		
		JRadioButton rbDec = new JRadioButton("Decimal");
		GridBagConstraints gbc_rbDec = new GridBagConstraints();
		gbc_rbDec.insets = new Insets(0, 10, 0, 5);
		gbc_rbDec.gridx = 0;
		gbc_rbDec.gridy = 0;
		pnlControl.add(rbDec, gbc_rbDec);
		rbg.addButton(rbDec, 0);
		rbDec.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				rbg.select(0);
				onSelectDec();
			}
			
		});
		
		JRadioButton rbHex = new JRadioButton("Hex");
		GridBagConstraints gbc_rbHex = new GridBagConstraints();
		gbc_rbHex.insets = new Insets(0, 0, 0, 5);
		gbc_rbHex.gridx = 1;
		gbc_rbHex.gridy = 0;
		pnlControl.add(rbHex, gbc_rbHex);
		rbg.addButton(rbHex, 1);
		rbHex.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				rbg.select(1);
				onSelectHex();
			}
			
		});
		
		rbg.select(0);
		
		JButton btnApplyChanges = new JButton("Apply Changes");
		GridBagConstraints gbc_btnApplyChanges = new GridBagConstraints();
		gbc_btnApplyChanges.insets = new Insets(0, 0, 0, 10);
		gbc_btnApplyChanges.gridx = 3;
		gbc_btnApplyChanges.gridy = 0;
		pnlControl.add(btnApplyChanges, gbc_btnApplyChanges);
		btnApplyChanges.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				onApplyChanges();
			}
			
		});
		
		JPanel pnlBot = new JPanel();
		GridBagConstraints gbc_pnlBot = new GridBagConstraints();
		gbc_pnlBot.weighty = 1.0;
		gbc_pnlBot.fill = GridBagConstraints.BOTH;
		gbc_pnlBot.gridx = 0;
		gbc_pnlBot.gridy = 1;
		add(pnlBot, gbc_pnlBot);
		GridBagLayout gbl_pnlBot = new GridBagLayout();
		gbl_pnlBot.columnWidths = new int[]{0, 0};
		gbl_pnlBot.rowHeights = new int[]{0, 0};
		gbl_pnlBot.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_pnlBot.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		pnlBot.setLayout(gbl_pnlBot);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		pnlBot.add(scrollPane, gbc_scrollPane);
		
		pnlTable = new NinseqRegTable();
		scrollPane.setViewportView(pnlTable);
	}

	public void loadPlayer(NinSeqSynthPlayer p){

		if(player != null) player.clearValueChangeListeners();
		player = p;
		
		if(player != null){
			player.addValueChangeListener(this);
			
			for(int i = 0; i < 256; i++){
				pnlTable.setValue(i, player.getVariableValue(i));
			}	
		}
		
	}

	public void onValueChanged(int i, int v) {
		pnlTable.setValue(i, (short)v);
	}
	
	private void onSelectDec(){
		pnlTable.setDecimalMode();
	}
	
	private void onSelectHex(){
		pnlTable.setHexMode();
	}
	
	private void onApplyChanges(){

		if(player == null) return;
		short[] vals = pnlTable.getValues();
		for(int i = 0; i < 256; i++){
			player.setVariableValue(i, vals[i]);
		}
	}
	
	public short[] getValues(){return pnlTable.getValues();}
	
}

package waffleoRai_seqplayerGUI;

import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import waffleoRai_Containers.nintendo.sar.DSSoundArchive;
import waffleoRai_SeqSound.ninseq.DSSeq;
import waffleoRai_SeqSound.ninseq.NinSeqSynthPlayer;
import waffleoRai_SeqSound.psx.SEQP;
import waffleoRai_SeqSound.psx.SeqpPlayer;
import waffleoRai_Sound.nintendo.DSWarc;
import waffleoRai_Utils.FileBuffer;
import waffleoRai_seqplayerGUI.ninseq.NinSeqRegisterViewPanel;
import waffleoRai_soundbank.nintendo.DSBank;
import waffleoRai_soundbank.sf2.SF2;
import waffleoRai_soundbank.vab.PSXVAB;

public class Test {

	public static void main(String[] args) {
		
		String seqstem = "C:\\Users\\Blythe\\Documents\\Game Stuff\\PSX\\GameData\\SLPM87176\\BGM\\BGM_";
		String inseq = seqstem + "050.seqp";
		
		String inbank_head = "C:\\Users\\Blythe\\Documents\\Game Stuff\\PSX\\GameData\\SLPM87176\\SE\\SE_000.bnkp\\SE_000_vab.vh"; //VAB
		String inbank_body = "C:\\Users\\Blythe\\Documents\\Game Stuff\\PSX\\GameData\\SLPM87176\\SE\\SE_000.bnkp\\SE_000_vab.vb";
		
		//Awright, let's try DS...
		String insdat = "C:\\Users\\Blythe\\Documents\\Desktop\\Notes\\pkmn_pearl.sdat";
		String outmid = "C:\\Users\\Blythe\\Documents\\Desktop\\out\\ds_test\\dp_wildpoke.mid";
		String outsf2 = "C:\\Users\\Blythe\\Documents\\Desktop\\out\\ds_test\\dp_battle.sf2";
		
		try
		{
			/*SEQP myseq = new SEQP(inseq);
			PSXVAB mybank = new PSXVAB(FileBuffer.createBuffer(inbank_head, false), FileBuffer.createBuffer(inbank_body, false));
		
			SeqpPlayer player = new SeqpPlayer(myseq, mybank);
			
			//MasterPanel pnl = new MasterPanel(player);
			SeqPlayerPanel pnl = new SeqPlayerPanel(player);*/
			
			DSSoundArchive arc = DSSoundArchive.readSDAT(insdat);
			arc.setSourceLocation(insdat, 0);
			
			//Figure out where the files of interest are
			
			//String sseq_name = "SEQ_BA_POKE";
			//System.err.println("Index: " + arc.getSSEQIndex(sseq_name));
			//System.exit(2);
			
			int sseq_idx = 1116;
			int sbnk_idx = 1002;
			int swar0_idx = 1000;
			int swar1_idx = 1002;
			
			DSWarc swar0 = arc.getSWAR(swar0_idx);
			DSWarc swar1 = arc.getSWAR(swar1_idx);
			DSBank sbnk = arc.getSBNK(sbnk_idx);
			DSSeq seq = arc.getSSEQ(sseq_idx);
			DSWarc[] warcs = {swar0, swar1};
			
			//arc.printTypeViewToStdOut();
			//sbnk.printInfo();
			//SF2.writeSF2(sbnk.toSoundbank(warcs, 0, "DPBattle"), "NTDExplorer", false, outsf2);
			//seq.writeMIDI(outmid, true);
			//System.exit(2);
			
			NinSeqSynthPlayer player = new NinSeqSynthPlayer(seq.getSequenceData(), sbnk.generatePlayableBank(warcs, 0), 0);
			//player.play();
			
			player.debugTagChannel(2, true);
			//player.setVariableValue(0, (short)17);
			
			//Generate custom panel
			NinSeqRegisterViewPanel nspnl = new NinSeqRegisterViewPanel();
			nspnl.loadPlayer(player);
			
			SeqPlayerPanel pnl = new SeqPlayerPanel(player, nspnl);
			
			JFrame frame = new JFrame();
			frame.add(pnl);
			frame.setMinimumSize(new Dimension(SeqPlayerPanel.WIDTH+10, SeqPlayerPanel.HEIGHT+25));
			frame.setPreferredSize(new Dimension(SeqPlayerPanel.WIDTH+10, SeqPlayerPanel.HEIGHT+25));
			frame.addWindowListener(new WindowListener(){

				@Override
				public void windowOpened(WindowEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}

				@Override
				public void windowClosed(WindowEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void windowIconified(WindowEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void windowDeiconified(WindowEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void windowActivated(WindowEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void windowDeactivated(WindowEvent e) {
					// TODO Auto-generated method stub
					
				}
				
			});
			
			SwingUtilities.invokeLater(new Runnable() 
	        {
	            public void run() 
	            {
	            	frame.pack();
	            	frame.setVisible(true);
	            }
	        });
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}

	}

}

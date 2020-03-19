package waffleoRai_seqplayerGUI;

import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import waffleoRai_SeqSound.psx.SEQP;
import waffleoRai_SeqSound.psx.SeqpPlayer;
import waffleoRai_Utils.FileBuffer;
import waffleoRai_soundbank.vab.PSXVAB;

public class Test {

	public static void main(String[] args) {
		
		String seqstem = "C:\\Users\\Blythe\\Documents\\Game Stuff\\PSX\\GameData\\SLPM87176\\BGM\\BGM_";
		String inseq = seqstem + "050.seqp";
		
		String inbank_head = "C:\\Users\\Blythe\\Documents\\Game Stuff\\PSX\\GameData\\SLPM87176\\SE\\SE_000.bnkp\\SE_000_vab.vh"; //VAB
		String inbank_body = "C:\\Users\\Blythe\\Documents\\Game Stuff\\PSX\\GameData\\SLPM87176\\SE\\SE_000.bnkp\\SE_000_vab.vb";
		
		try
		{
			SEQP myseq = new SEQP(inseq);
			PSXVAB mybank = new PSXVAB(FileBuffer.createBuffer(inbank_head, false), FileBuffer.createBuffer(inbank_body, false));
		
			SeqpPlayer player = new SeqpPlayer(myseq, mybank);
			
			//MasterPanel pnl = new MasterPanel(player);
			SeqPlayerPanel pnl = new SeqPlayerPanel(player);
			
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

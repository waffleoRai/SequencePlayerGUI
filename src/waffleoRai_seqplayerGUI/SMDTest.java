package waffleoRai_seqplayerGUI;

import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedWriter;
import java.io.FileWriter;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import waffleoRai_SeqSound.misc.SMD;
import waffleoRai_SeqSound.misc.smd.SMDPlayer;
import waffleoRai_SeqSound.misc.smd.SMDSynthChannel;
import waffleoRai_SoundSynth.SynthProgram;
import waffleoRai_Utils.FileBuffer;
import waffleoRai_soundbank.procyon.SWD;

public class SMDTest {
	
	public static void testplayback(SynthProgram prog, byte note) throws LineUnavailableException, InterruptedException{
		
		SMDSynthChannel testchan = new SMDSynthChannel(44100, 16);
		SourceDataLine line = AudioSystem.getSourceDataLine(new AudioFormat(44100, 16, 2, true, false));
		
		line.open();
		line.start();
		
		testchan.setProgram(prog);
		testchan.noteOn(note, (byte)127);
		System.err.println("test note on");
		
		for(int i = 0; i < 88200; i++){
			//System.err.println("sample " + i);
			int[] s = testchan.nextSample();
			byte[] b = new byte[4];
			b[0] = (byte)(s[0] & 0xFF);
			b[1] = (byte)((s[0] >>> 8)  & 0xFF);
			b[2] = (byte)(s[1] & 0xFF);
			b[3] = (byte)((s[1] >>> 8)  & 0xFF);
			line.write(b, 0, 4);
		}
		
		Thread.sleep(2000);
		
		testchan.noteOff(note, (byte)0);
		System.err.println("test note off");
		
		line.stop();
		line.close();
	}

	public static void main(String[] args) {

		String dirpath = "C:\\Users\\Blythe\\Documents\\Desktop\\out\\ds_test\\pokedun_bgm";
		
		String smd_path = dirpath + "\\bgm0024.smd";
		String swd_path = dirpath + "\\bgm0024.swd";
		String swd_warc_path = dirpath + "\\bgm.swd";
		
		try{
		
			FileBuffer dat = FileBuffer.createBuffer(swd_warc_path, false);
			SWD warc = SWD.readSWD(dat, 0);
			
			//Dump tuning table (debug)
			/*String tblpath = dirpath + "\\bgm_wavi.tsv";
			BufferedWriter bw = new BufferedWriter(new FileWriter(tblpath));
			warc.dumpWaviTable(bw);
			bw.close();
			System.exit(2);*/
			//warc.debugPrintWaviPositions();
			//System.exit(2);
			
			dat = FileBuffer.createBuffer(swd_path, false);
			SWD bank = SWD.readSWD(dat, 0);
			bank.loadSoundDataFrom(warc);
			//bank.debugPrint();
			//bank.writeSF2(dirpath + "\\bgm0024_debug.sf2");
			//System.exit(2);
			
			dat = FileBuffer.createBuffer(smd_path, false);
			SMD seq = new SMD(dat, 0);
			//seq.printToStderr();
			
			//I'm going to manually test the looping for program 25
			//SynthProgram prog = bank.getProgram(0, 25);
			//testplayback(prog, (byte)60);
			
			SMDPlayer player = new SMDPlayer(seq, bank);
			//player.getChannel(8).tagMe(true, 8);
			//System.err.println("yo");
			//player.writeMixdownTo(dirpath + "\\bassapples2.wav", 1);
			//System.exit(2);
			
			SeqPlayerPanel pnl = new SeqPlayerPanel(player, new JPanel());
			
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
		catch(Exception e){
			e.printStackTrace();
			System.exit(1);
		}

	}

}

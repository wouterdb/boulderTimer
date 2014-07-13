package bvkb.boulder;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JDialog;
import javax.swing.JTextField;
import javax.swing.Timer;

import jfontchooser.JFontChooser;

public class ControlPannelActive extends ControlPannel {
	
	private Clip coin;
	private Clip air;
	private LargeScreen screen;
	private Timer refreshtimer;
	
	public ControlPannelActive() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		coin = SoundAction.load("coin.wav");
		air = SoundAction.load("airraid.wav");
		font= new Font(getFont().getName(), Font.PLAIN, 100);
		refreshtimer = new Timer(15, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				updateValues();
				
			}
		});
		refreshtimer.start();
	}

	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
		// <editor-fold defaultstate="collapsed"
		// desc=" Look and feel setting code (optional) ">
		/*
		 * If Nimbus (introduced in Java SE 6) is not available, stay with the
		 * default look and feel. For details see
		 * http://download.oracle.com/javase
		 * /tutorial/uiswing/lookandfeel/plaf.html
		 */
		
		
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager
					.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(ControlPannel.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(ControlPannel.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(ControlPannel.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(ControlPannel.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		}
		// </editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new ControlPannelActive().setVisible(true);
				} catch (UnsupportedAudioFileException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	private SportTimer timer;
	private Font font;

	@Override
	protected void start(ActionEvent evt) {
		try {
			int time = getInt(inTime, "total time must be an integer number");
			float warningTime = getFloat(inWarn,
					"warning time must be a number");
			int warningCount = getInt(inWarnCount,
					"warning count must be integer");
			int repeats = getInt(inRepeats,
					"number of repeats time must be integer");
			
			outTime.setText(""+time);
			outWarn.setText(""+warningTime);
			outWarnCount.setText(""+warningCount);
			outRepeats.setText(""+repeats);
			
			setTimer(time*6000,warningTime*100,warningCount,repeats);
		
			if(screen==null || !screen.isVisible()){
				screen = new LargeScreen(timer);
				screen.setVisible(true);
				screen.setFont(font);
			}else{
				screen.setTimer(timer);
				screen.setFont(font);
			}
			
			timer.start();
			
		} catch (IllegalArgumentException e) {
			notifier.setText(e.getMessage());
		}
	}
	
	private void updateValues(){
		if(timer!=null){
			int time = timer.getTimeLeft();
			outTime.setText(String.format("%02d:%02d", time/100,time%100));
			//outRepeats.setText(""+repeats);
		}
	}
	

	
	@Override
	protected void fontButtonActionPerformed(ActionEvent evt) {
		Font nfont = com.l2fprod.common.swing.JFontChooser.showDialog(this, "choose font", font);
		if(nfont!=null){
			screen.setFont(nfont);
			this.font=nfont;
		}
	}

	private void setTimer(int time, float warningTime, int warningCount,
			int repeats) {
		if(timer!=null)
			timer.stop();
		timer= new SportTimer(time);
		
		for(int i = 1; i<=warningCount;i++)
			timer.add(new SoundAction(coin, (int)(time-(warningTime*i))));
		
		timer.add(new SoundAction(air, time));
		
	}

	private float getFloat(JTextField in, String msg) {
		try {
			return Float.parseFloat(in.getText());
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(msg);
		}
		
	}

	private int getInt(JTextField in, String msg) {
		try {
			return Integer.parseInt(in.getText());
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(msg);
		}
		
	}

}

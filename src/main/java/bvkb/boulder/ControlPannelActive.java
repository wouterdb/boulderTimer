package bvkb.boulder;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.Timer;

public class ControlPannelActive extends ControlPannel {

	private Clip beep;
	private Clip gong;
	private LargeScreen screen;
	private Timer refreshtimer;

	public ControlPannelActive() throws UnsupportedAudioFileException,
			IOException, LineUnavailableException {
		beep = SoundAction.load("beep.wav");
		System.out.println(beep.getMicrosecondLength());
		gong = SoundAction.load("bell.wav");
		font = new Font(getFont().getName(), Font.PLAIN, 100);
		refreshtimer = new Timer(15, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				updateValues();

			}
		});
		refreshtimer.start();
	}

	private void updateValues() {
		if (timer != null) {
			int time = timer.getTimeLeft();
			outTime.setText(PresentationUtil.formatTime(time));
		}
		if (repeater != null) {
			outRepeats.setText("" + repeater.getRepeatsLeft());
		} else {
			outRepeats.setText("");
		}

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
	private RepeatAction repeater;

	@Override
	protected void start(ActionEvent evt) {
		if (timer!=null && timer.getTimeLeft() > 0)
			notifier.setText("already running");
		else
			try {
				long time = PresentationUtil
						.getTime(inTime.getText(),
								"total time must be an integer number followed by m,s or h");
				int repeats = getInt(inRepeats,
						"number of repeats time must be integer");

				outTime.setText("" + time);
				outRepeats.setText("" + repeats);

				setTimer((int) time, 100, 3, repeats);

				if (screen == null || !screen.isVisible()) {
					screen = new LargeScreen(timer);
					screen.setVisible(true);
					screen.setFont(font);
				} else {
					screen.setTimer(timer);
					screen.setFont(font);
				}

				timer.start();

			} catch (IllegalArgumentException e) {
				notifier.setText(e.getMessage());
			}
	}

	protected void stop(java.awt.event.ActionEvent evt) {
		if(JOptionPane.showConfirmDialog(this, "STOP?")==JOptionPane.OK_OPTION){
			timer.stop();
			
		}
	}

	protected void stopRepeat(java.awt.event.ActionEvent evt) {
		if (repeater != null) {
			repeater.stop();
		}
	}

	@Override
	protected void fontButtonActionPerformed(ActionEvent evt) {
		Font nfont = com.l2fprod.common.swing.JFontChooser.showDialog(this,
				"choose font", font);
		if (nfont != null) {
			screen.setFont(nfont);
			this.font = nfont;
		}
	}

	private void setTimer(int time, float warningTime, int warningCount,
			int repeats) {
		if (timer != null)
			timer.stop();
		timer = new SportTimer(time);

		for (int i = 0; i < warningCount; i++)
			timer.add(new SoundAction(beep, (int) (time - (warningTime * i))));

		if (time > 6000)
			timer.add(new SoundAction(gong, time - 6000));

		if (repeats > 0) {
			repeater = new RepeatAction(timer, time, repeats);
			timer.add(repeater);
		} else
			repeater = null;

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

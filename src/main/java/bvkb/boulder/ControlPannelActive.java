package bvkb.boulder;

import org.apache.commons.lang3.tuple.Pair;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Vector;


/**
 * @author w.deborger@gmail.com
 *
 * Class adding functionality to the control panel, which is generated using netbeans
 */
public class ControlPannelActive extends ControlPannel {

	/**
	 * Class for correctly rendering a combobox containing Pair<String, Clip>
	 * 
	 * based on sun tutorial
	 */
	public class ClipRenderer extends JLabel implements
			ListCellRenderer<Pair<String, Clip>> {

		@Override
		public Component getListCellRendererComponent(
				JList<? extends Pair<String, Clip>> list,
				Pair<String, Clip> value, int index, boolean isSelected,
				boolean cellHasFocus) {

			String svalue = String.format("%s (%.2f)", value.getLeft(), value
					.getRight().getMicrosecondLength() / 1000000.0);

			setComponentOrientation(list.getComponentOrientation());

			Color bg = null;
			Color fg = null;

			JList.DropLocation dropLocation = list.getDropLocation();
			if (dropLocation != null && !dropLocation.isInsert()
					&& dropLocation.getIndex() == index) {

				bg = javax.swing.UIManager.getColor("List.dropCellBackground");
				fg = javax.swing.UIManager.getColor("List.dropCellForeground");

				isSelected = true;
			}

			if (isSelected) {
				setBackground(bg == null ? list.getSelectionBackground() : bg);
				setForeground(fg == null ? list.getSelectionForeground() : fg);
			} else {
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}

			setIcon(null);
			setText(svalue);

			setEnabled(list.isEnabled());
			setFont(list.getFont());

			return this;
		}

	}

	/**
	 * sounds played at respecitvely 1m, 1 and 2 s and 0s to end of rotation 
	 */
	private Clip sound1m;
	private Clip sound1s;
	private Clip sound0s;

		
	/**
	 * timer driving all action 
	 */
	private SportTimer timer;
	
	/**
	 * action responsible for restarting the timer
	 */
	private RepeatAction repeater;
	
	
	
	/**
	 * large window displaying the time 
	 */
	private LargeScreen screen;
	
	/**
	 * font to use in said window
	 */
	private Font font;
	
	
	/**
	 * timer updating this window to track the running timer
	 */
	private final Timer refreshtimer;

	
	/**
	 * all sound related stuff
	 */
	private final SoundManager soundmanager = new SoundManager();

	public ControlPannelActive() throws UnsupportedAudioFileException,
			IOException, LineUnavailableException {
		initComponents();
		
		setPreset(soundmanager.getPreset(0));
		setSound(soundmanager.getPreset(0));
		
		font = new Font(getFont().getName(), Font.PLAIN, 400);

		refreshtimer = new Timer(15, e -> updateValues());
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

	public static void main(String[] args) {
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager
					.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException |
                 IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(ControlPannel.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		}
        // </editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(() -> {
            try {
                new ControlPannelActive().setVisible(true);
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
});
	}

	

	/**
	 * start the timer!
	 */
	@Override
	protected void start(ActionEvent evt) {
		if (timer != null && timer.getTimeLeft() > 0) {
			//already running!
			if (!screen.isVisible())
				screen.setVisible(true);
			else
				notifier.setText("already running");
		}else
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
		if (JOptionPane.showConfirmDialog(this, "STOP?") == JOptionPane.OK_OPTION) {
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

	protected void play1MActionPerformed(java.awt.event.ActionEvent evt) {
		play(select1min);
	}

	protected void play1SecActionPerformed(java.awt.event.ActionEvent evt) {
		play(select2sec);
	}

	protected void playFinalActionPerformed(java.awt.event.ActionEvent evt) {
		play(select0sec);
	}

	protected void presetActionPerformed(java.awt.event.ActionEvent evt) {
		setPreset(soundmanager.getPreset(preset.getSelectedIndex()));
	}

	private void setPreset(SoundSet preset) {
		setBoxto(select1min, preset.getC1minName());
		setBoxto(select2sec, preset.getC1secName());
		setBoxto(select0sec, preset.getC0secName());
	}

	private void setBoxto(JComboBox<Pair<String, Clip>> box, String name) {
		for (int i = 0; i < box.getItemCount(); i++) {
			if (box.getItemAt(i).getLeft().endsWith(name)) {
				box.setSelectedIndex(i);
				return;
			}
		}

	}

	protected void setSoundsActionPerformed(java.awt.event.ActionEvent evt) {
		setSound(collectIntoPreset());
	}

	private SoundSet collectIntoPreset() {
		Pair<String,Clip> c1m =  ((Pair<String,Clip>)select1min.getSelectedItem());
		Pair<String,Clip> c1s =  ((Pair<String,Clip>)select2sec.getSelectedItem());
		Pair<String,Clip> c0s =  ((Pair<String,Clip>)select0sec.getSelectedItem());
		return new SoundSet("",c1m,c1s,c0s);
		
	}

	private void setSound(SoundSet preset) {
		old1min.setText(preset.getC1minName());
		old1sec.setText(preset.getC1secName());
		old0sec.setText(preset.getC0secName());

		sound1m = preset.getC1min();
		sound1s = preset.getC1sec();
		sound0s = preset.getC0sec();
	}

	private void play(JComboBox<Pair<String,Clip>> selector) {
		Pair<String,Clip> s = (Pair<String, Clip>) selector.getSelectedItem();
		Clip clip = s.getValue();
		clip.setFramePosition(0);
		clip.start();

	}

	private void setTimer(int time, float warningTime, int warningCount,
			int repeats) {
		if (timer != null)
			timer.stop();
		timer = new SportTimer(time);

		for (int i = 1; i < warningCount; i++)
			timer.add(new SoundAction(sound1s, (int) (time - (warningTime * i))));

		timer.add(new SoundAction(sound0s, time));

		if (time > 6000)
			timer.add(new SoundAction(sound1m, time - 6000));

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

	@Override
	protected ComboBoxModel<String> getPresetModel() {
		return new DefaultComboBoxModel<String>(soundmanager.getPresetNames());
	}

	protected ComboBoxModel<Pair<String, Clip>> getSoundModel() {
		return new DefaultComboBoxModel<>(new Vector<>(soundmanager.getSounds()));
	}

	protected ListCellRenderer<Pair<String, Clip>> getPresetRenderer() {
		return new ClipRenderer();
	}

}

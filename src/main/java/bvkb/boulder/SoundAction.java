package bvkb.boulder;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream.GetField;
import java.net.URL;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.SystemMenuBar;

public class SoundAction extends Action{

	

	private Clip clip;
	
	public SoundAction(Clip clip, int at) {
		super(at);
		this.clip = clip;
	}

	@Override
	public void run() {
		clip.setFramePosition(0);
		clip.start();
		System.out.println(System.currentTimeMillis());
	}

}

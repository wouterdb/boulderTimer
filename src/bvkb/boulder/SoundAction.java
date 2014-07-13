package bvkb.boulder;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream.GetField;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundAction extends Action{

	public static Clip load(String file) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		InputStream inps = SoundAction.class.getClassLoader()
				.getResourceAsStream(file);

		AudioInputStream audioInputStream = AudioSystem
				.getAudioInputStream(inps);

		if (audioInputStream == null)
			throw new IllegalArgumentException("file not found: " + file);

		AudioFormat format = audioInputStream.getFormat();
		DataLine.Info info = new DataLine.Info(Clip.class, format);
		Clip m_clip = (Clip) AudioSystem.getLine(info);
		m_clip.open(audioInputStream);
		return m_clip;

	}

	private Clip clip;
	
	public SoundAction(Clip clip, int at) {
		super(at);
		this.clip = clip;
	}

	@Override
	public void run() {
		clip.setFramePosition(0);
		clip.start();
	}

}

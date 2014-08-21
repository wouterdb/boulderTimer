package bvkb.boulder;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.commons.lang3.tuple.Pair;

/**
 * @author w.deborger@gmail.com
 *
 *         Class responible for sound related functions
 * 
 *         1-preload all sound files listed in sound.idx 2-preload all SoundSets
 *         from presets.idx
 */
public class SoundManager {

	// all preset soundsets
	public List<SoundSet> preset = new LinkedList<>();

	// all available sounds, same data, different form
	public List<Pair<String, Clip>> sounds = new LinkedList<>();
	public Map<String, Clip> soundDict = new HashMap<>();

	public SoundManager() {
		init();
	}

	private void init() {
		loadClips();
		loadSets();
	}

	private void loadSets() {
		BufferedReader br = new BufferedReader(new InputStreamReader(getClass()
				.getClassLoader().getResourceAsStream("presets.idx")));

		try {
			String line = br.readLine();
			while (line != null) {
				String[] parts = line.split(" ");
				loadPresets(parts[0], parts[1], parts[2], parts[3]);
				line = br.readLine();
			}
		} catch (IOException e) {
			ExceptionReporter.report(e, "presets not loaded correctly");
		}

	}

	private void loadPresets(String name, String c1m, String c1s, String c0s) {
		preset.add(new SoundSet(name, c1m, c1s, c0s, soundDict.get(c1m),
				soundDict.get(c1s), soundDict.get(c0s)));

	}

	private void loadClips() {
		BufferedReader br = new BufferedReader(new InputStreamReader(getClass()
				.getClassLoader().getResourceAsStream("sounds.idx")));
		try {
			String line = br.readLine();
			while (line != null) {
				String[] parts = line.split(" ");
				loadClip(parts[1], parts[0]);
				line = br.readLine();
			}
		} catch (IOException e) {
			ExceptionReporter.report(e, "sounds not loaded correctly");
		}
	}

	private void loadClip(String name, String filename) {
		try {
			Clip c = load(filename);
			sounds.add(Pair.of(name, c));
			soundDict.put(name, c);
		} catch (UnsupportedAudioFileException | IOException
				| LineUnavailableException e) {
			ExceptionReporter.report(e, "sound not loaded correctly: " +name +" " +filename);
		}

	}

	public String[] getPresetNames() {
		String[] names = new String[preset.size()];
		for (int i = 0; i < names.length; i++) {
			names[i] = preset.get(i).getName();
		}
		return names;
	}

	public static Clip load(String file) throws UnsupportedAudioFileException,
			IOException, LineUnavailableException {
		// InputStream inps = .getResourceAsStream(file);

		URL url = SoundAction.class.getClassLoader().getResource(file);

		AudioInputStream audioInputStream = AudioSystem
				.getAudioInputStream(url);

		if (audioInputStream == null)
			throw new FileNotFoundException(file);

		AudioFormat format = audioInputStream.getFormat();
		DataLine.Info info = new DataLine.Info(Clip.class, format);
		Clip m_clip = (Clip) AudioSystem.getLine(info);
		m_clip.open(audioInputStream);
		return m_clip;

	}

	public Clip getSound(String name) {
		return soundDict.get(name);
	}

	public List<Pair<String, Clip>> getSounds() {
		return sounds;
	}

	public SoundSet getPreset(int selectedIndex) {
		return preset.get(selectedIndex);
	}
}

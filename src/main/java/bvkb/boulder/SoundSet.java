package bvkb.boulder;

import javax.sound.sampled.Clip;

import org.apache.commons.lang3.tuple.Pair;

public class SoundSet {

	private final String name;
	
	private final Clip c1sec;
	private final Clip c1min;
	private final Clip c0sec;
	
	private final String c1secName;
	private final String c1minName;
	private final String c0secName;
	
	

	public String getC1secName() {
		return c1secName;
	}

	public String getC1minName() {
		return c1minName;
	}

	public String getC0secName() {
		return c0secName;
	}

	public SoundSet(String name, String c1minName, String c1secName,
			String c0secName, Clip c1min, Clip c1sec, Clip c0sec) {
		super();
		this.name = name;
		this.c1minName = c1minName;
		this.c1secName = c1secName;
		this.c0secName = c0secName;
		this.c1min = c1min;
		this.c1sec = c1sec;
		this.c0sec = c0sec;
	}
	
	public SoundSet(SoundManager sm,String name, String c1minName, String c1secName,
			String c0secName) {
		super();
		this.name = name;
		this.c1minName = c1minName;
		this.c1secName = c1secName;
		this.c0secName = c0secName;
		this.c1min = sm.getSound(c1minName);
		this.c1sec = sm.getSound(c1secName);
		this.c0sec = sm.getSound(c0secName);
	}

	public SoundSet(String name, Pair<String, Clip> c1m,
			Pair<String, Clip> c1s, Pair<String, Clip> c0s) {
		super();
		this.name = name;
		this.c1minName = c1m.getKey();
		this.c1secName = c1s.getKey();
		this.c0secName = c0s.getKey();
		this.c1min = c1m.getValue();
		this.c1sec = c1s.getValue();
		this.c0sec = c0s.getValue();
	}

	public String getName() {
		return name;
	}

	public Clip getC1sec() {
		return c1sec;
	}

	public Clip getC1min() {
		return c1min;
	}

	public Clip getC0sec() {
		return c0sec;
	}
	
	

}

package bvkb.boulder;

import javax.sound.sampled.Clip;

public class SoundAction extends Action{

	

	private final Clip clip;
	
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

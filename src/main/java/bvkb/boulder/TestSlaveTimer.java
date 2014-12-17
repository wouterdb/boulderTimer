package bvkb.boulder;

public class TestSlaveTimer implements SlaveTimer{

	@Override
	public void close() {
		System.out.println("close");
		
	}

	@Override
	public void open() {
		System.out.println("open");
		
	}

	@Override
	public void runFrom(long stoptime) {
		
		System.out.println("run from: " + PresentationUtil.formatTime((int) (stoptime-System.currentTimeMillis())/10));
		
	}

	@Override
	public void halt(int value) {
		System.out.println("halt on: "+ value);
		
	}

}

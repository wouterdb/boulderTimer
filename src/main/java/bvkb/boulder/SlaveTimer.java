package bvkb.boulder;

public interface SlaveTimer {

	void close();

	void open();

	void runFrom(long stoptime);

	void halt(int value);

}

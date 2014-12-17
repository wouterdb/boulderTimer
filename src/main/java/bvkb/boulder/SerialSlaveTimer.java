package bvkb.boulder;

import bvkb.display.DisplayProtcol;
import jssc.SerialPort;
import jssc.SerialPortException;

public class SerialSlaveTimer implements SlaveTimer {

	private String name;
	private SerialPort port;
	private DisplayProtcol dpp;

	public SerialSlaveTimer(String name) {
		this.name = name;
	}

	@Override
	public void close() {
		System.out.println("close");

	}

	@Override
	public void open() {

		try {
			this.port = new SerialPort(name);
			this.port.openPort();
			this.dpp = new bvkb.display.DisplayProtcol(this.port);
		} catch (SerialPortException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void runFrom(long stoptime) {
		if (port == null)
			return;
		try {
			dpp.setDisplayToSeconds();
			dpp.runDown((int) (stoptime - System.currentTimeMillis()) / 10);
		} catch (SerialPortException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void halt(int value) {
		if (port == null)
			return;
		try {
			dpp.setDisplayToSeconds();
			dpp.set(value);
		} catch (SerialPortException e) {
			e.printStackTrace();
		}

	}

}

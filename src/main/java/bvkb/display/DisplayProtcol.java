package bvkb.display;


import java.nio.ByteBuffer;
import java.util.Arrays;

import jssc.SerialPort;
import jssc.SerialPortException;

public class DisplayProtcol {
	
	private SerialPort scp;
	
	public DisplayProtcol(SerialPort p) throws SerialPortException {
		this.scp = p;
		writeValue(0);
	}
	
	public void writeValue(int value) throws SerialPortException{
		scp.writeByte((byte) 'L');
		System.out.println(Arrays.toString(ByteBuffer.allocate(4).putInt(value).array()));
		scp.writeBytes(ByteBuffer.allocate(4).putInt(value).array());
	}

	public void runDown(int startvalue) throws SerialPortException{
		writeValue(startvalue);
		scp.writeByte((byte) 'r');
	}
	
	public void runUp(int startvalue) throws SerialPortException{
		writeValue(startvalue);
		scp.writeByte((byte) 'R');
	}
	
	public void halt() throws SerialPortException{
		scp.writeByte((byte) 'H');
	}
	
	public void set(int value) throws SerialPortException{
		writeValue(value);
		scp.writeByte((byte) 'D');
	}
	
	public void setDisplayToRaw() throws SerialPortException{
		scp.writeByte((byte) 'X');
	}
	
	public void setDisplayToSeconds() throws SerialPortException{
		scp.writeByte((byte) 'G');
	}
	
	public void setDisplayToHundreths() throws SerialPortException{
		scp.writeByte((byte) 'g');
	}
	
	
}

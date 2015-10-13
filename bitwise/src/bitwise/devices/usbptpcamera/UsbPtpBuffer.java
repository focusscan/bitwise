package bitwise.devices.usbptpcamera;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public final class UsbPtpBuffer {
	private boolean measureMode;
	private int length;
	private ByteBuffer buffer;
	
	public UsbPtpBuffer() {
		measureMode = true;
		length = 0;
		buffer = null;
	}
	
	public UsbPtpBuffer(byte[] in) {
		measureMode = false;
		length = 0;
		buffer = ByteBuffer.wrap(in);
	}
	
	public boolean isMeasureMode() {
		return measureMode;
	}
	
	public int getLength() {
		return length;
	}
	
	public byte[] getArray() {
		return buffer.array();
	}
	
	public boolean disableMeasureMode() {
		if (measureMode) {
			buffer = ByteBuffer.allocate(length);
			measureMode = false;
			return true;
		}
		return false;
	}
	
	public void put(byte in) {
		if (measureMode)
			length++;
		else
			buffer.put(in);
	}
	
	public void put(short in) {
		if (measureMode)
			length += 2;
		else {
			buffer.put((byte) (in));
			buffer.put((byte) (in >> 8));
		}
	}
	
	public void put(int in) {
		if (measureMode)
			length += 4;
		else {
			buffer.put((byte) (in));
			buffer.put((byte) (in >> 8));
			buffer.put((byte) (in >> 16));
			buffer.put((byte) (in >> 24));
		}
	}
	
	public void put(long in) {
		if (measureMode)
			length += 4;
		else {
			buffer.put((byte) (in));
			buffer.put((byte) (in >> 8));
			buffer.put((byte) (in >> 16));
			buffer.put((byte) (in >> 24));
			buffer.put((byte) (in >> 32));
			buffer.put((byte) (in >> 40));
			buffer.put((byte) (in >> 48));
			buffer.put((byte) (in >> 52));
		}
	}
	
	public byte getByte() {
		return buffer.get();
	}
	
	public short getShort() {
		int v0 = 0xff & (short) getByte();
		int v1 = 0xff & (short) getByte();
		return (short) (v0 | (v1 << 8));
	}
	
	public int getInt() {
		int v0 = 0xff & (int) getByte();
		int v1 = 0xff & (int) getByte();
		int v2 = 0xff & (int) getByte();
		int v3 = 0xff & (int) getByte();
		return v0 | (v1 << 8) | (v2 << 16) | (v3 << 24);
	}
	
	public long getLong() {
		long v0 = 0xff & (long) getByte();
		long v1 = 0xff & (long) getByte();
		long v2 = 0xff & (long) getByte();
		long v3 = 0xff & (long) getByte();
		long v4 = 0xff & (long) getByte();
		long v5 = 0xff & (long) getByte();
		long v6 = 0xff & (long) getByte();
		long v7 = 0xff & (long) getByte();
		return v0 | (v1 << 8) | (v2 << 16) | (v3 << 24)
				| (v4 << 32) | (v5 << 40) | (v6 << 48) | (v7 << 52);
	}
	
	public String getString() {
		int length = 0xff & (int) getByte();
		if (0 == length)
			return "";
		byte[] str = new byte[(length - 1) * 2];
		for (int i = 0; i < str.length; i++)
			str[i] = getByte();
		String ret = new String(str, StandardCharsets.UTF_16LE);
		// Consume the null character
		getShort();
		return ret;
	}
	
	public byte[] getByteArray() {
		int length = getInt();
		byte[] ret = new byte[length];
		for (int i = 0; i < ret.length; i++)
			ret[i] = getByte();
		return ret;
	}
	
	public short[] getShortArray() {
		int length = getInt();
		short[] ret = new short[length];
		for (int i = 0; i < ret.length; i++)
			ret[i] = getShort();
		return ret;
	}
	
	public int[] getIntArray() {
		int length = getInt();
		int [] ret = new int[length];
		for (int i = 0; i < ret.length; i++)
			ret[i] = getInt();
		return ret;
	}
	
	public long[] getLongArray() {
		int length = getInt();
		long [] ret = new long[length];
		for (int i = 0; i < ret.length; i++)
			ret[i] = getLong();
		return ret;
	}
}

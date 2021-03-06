package bitwise.devices.usbptpcamera.coder;

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
	
	public UsbPtpBuffer(UsbPtpBuffer that) {
		measureMode = false;
		length = 0;
		buffer = that.buffer.slice();
	}
	
	public boolean hasRemaining() {
		return buffer.hasRemaining();
	}
	
	public boolean isMeasureMode() {
		return measureMode;
	}
	
	public int getLength() {
		return length;
	}
	
	public byte[] getArray() throws UsbPtpCoderException {
		try {
			return buffer.array();
		} catch (Exception e) {
			throw new UsbPtpCoderException(e);
		}
	}
	
	public byte[] getRemainingArray() throws UsbPtpCoderException {
		try {
			byte[] ret = new byte[buffer.remaining()];
			for (int i = 0; i < ret.length; i++)
				ret[i] = buffer.get();
			return ret;
		} catch (Exception e) {
			throw new UsbPtpCoderException(e);
		}
	}
	
	public boolean disableMeasureMode() {
		if (measureMode) {
			buffer = ByteBuffer.allocate(length);
			measureMode = false;
			return true;
		}
		return false;
	}
	
	public void put(byte in) throws UsbPtpCoderException {
		try {
			if (measureMode)
				length++;
			else
				buffer.put(in);
		} catch (Exception e) {
			throw new UsbPtpCoderException(e);
		}
	}
	
	public void put(short in) throws UsbPtpCoderException {
		try {
			if (measureMode)
				length += 2;
			else {
				buffer.put((byte) (in));
				buffer.put((byte) (in >> 8));
			}
		} catch (Exception e) {
			throw new UsbPtpCoderException(e);
		}
	}
	
	public void put(int in) throws UsbPtpCoderException {
		try {
			if (measureMode)
				length += 4;
			else {
				buffer.put((byte) (in));
				buffer.put((byte) (in >> 8));
				buffer.put((byte) (in >> 16));
				buffer.put((byte) (in >> 24));
			}
		} catch (Exception e) {
			throw new UsbPtpCoderException(e);
		}
	}
	
	public void put(long in) throws UsbPtpCoderException {
		try {
			if (measureMode)
				length += 8;
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
		} catch (Exception e) {
			throw new UsbPtpCoderException(e);
		}
	}
	
	public void put(Int128 in) throws UsbPtpCoderException {
		put(in.value_lo);
		put(in.value_hi);
	}
	
	public void put(String in) throws UsbPtpCoderException {
		if (in.length() > 254)
			return;	// TODO: should probably throw or something
		byte length = (byte) in.length();
		put(length);
		if (0 < length) {
			byte[] encoded = in.getBytes(StandardCharsets.UTF_16LE);
			for (byte b : encoded)
				put(b);
			put((byte) 0x00);
			put((byte) 0x00);
		}
	}
	
	public void put(byte[] in) throws UsbPtpCoderException {
		put(in.length);
		for (byte b : in)
			put(b);
	}

	public void put(short[] in) throws UsbPtpCoderException {
		put(in.length);
		for (short b : in)
			put(b);
	}

	public void put(int[] in) throws UsbPtpCoderException {
		put(in.length);
		for (int b : in)
			put(b);
	}

	public void put(long[] in) throws UsbPtpCoderException {
		put(in.length);
		for (long b : in)
			put(b);
	}

	public void put(Int128[] in) throws UsbPtpCoderException {
		put(in.length);
		for (Int128 b : in)
			put(b);
	}
	
	public void put(ByteLenAInt32 in) throws UsbPtpCoderException {
		put((in.value.length + 1) * 4);
		for (int b : in.value)
			put(b);
	}
	
	public byte getByte() throws UsbPtpCoderException {
		try {
			return buffer.get();
		} catch (Exception e) {
			throw new UsbPtpCoderException(e);
		}
	}
	
	public short getShort() throws UsbPtpCoderException {
		int v0 = 0xff & (short) getByte();
		int v1 = 0xff & (short) getByte();
		return (short) (v0 | (v1 << 8));
	}
	
	public int getInt() throws UsbPtpCoderException {
		int v0 = 0xff & (int) getByte();
		int v1 = 0xff & (int) getByte();
		int v2 = 0xff & (int) getByte();
		int v3 = 0xff & (int) getByte();
		return v0 | (v1 << 8) | (v2 << 16) | (v3 << 24);
	}
	
	public long getLong() throws UsbPtpCoderException {
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
	
	public Int128 getVeryLong() throws UsbPtpCoderException {
		long lo = getLong();
		long hi = getLong();
		return new Int128(lo, hi);
	}
	
	public String getString() throws UsbPtpCoderException {
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
	
	public byte[] getByteArray() throws UsbPtpCoderException {
		int length = getInt();
		byte[] ret = new byte[length];
		for (int i = 0; i < ret.length; i++)
			ret[i] = getByte();
		return ret;
	}
	
	public short[] getShortArray() throws UsbPtpCoderException {
		int length = getInt();
		short[] ret = new short[length];
		for (int i = 0; i < ret.length; i++)
			ret[i] = getShort();
		return ret;
	}
	
	public int[] getIntArray() throws UsbPtpCoderException {
		int length = getInt();
		int [] ret = new int[length];
		for (int i = 0; i < ret.length; i++)
			ret[i] = getInt();
		return ret;
	}
	
	public long[] getLongArray() throws UsbPtpCoderException {
		int length = getInt();
		long [] ret = new long[length];
		for (int i = 0; i < ret.length; i++)
			ret[i] = getLong();
		return ret;
	}
	
	public Int128[] getVeryLongArray() throws UsbPtpCoderException {
		int length = getInt();
		Int128[] ret = new Int128[length];
		for (int i = 0; i < ret.length; i++)
			ret[i] = getVeryLong();
		return ret;
	}
	
	public UsbPtpPrimType getPrimType(short dataType) throws UsbPtpCoderException {
		switch (dataType) {
		case (short) 0x0001:
		case (short) 0x0002:
			return new Int8(getByte());
		case (short) 0x0003:
		case (short) 0x0004:
			return new Int16(getShort());
		case (short) 0x0005:
		case (short) 0x0006:
			return new Int32(getInt());
		case (short) 0x0007:
		case (short) 0x0008:
			return new Int64(getLong());
		case (short) 0x0009:
		case (short) 0x000a:
			return getVeryLong();
		case (short) 0x4001:
		case (short) 0x4002:
			return new AInt8(getByteArray());
		case (short) 0x4003:
		case (short) 0x4004:
			return new AInt16(getShortArray());
		case (short) 0x4005:
		case (short) 0x4006:
			return new AInt32(getIntArray());
		case (short) 0x4007:
		case (short) 0x4008:
			return new AInt64(getLongArray());
		case (short) 0x4009:
		case (short) 0x400a:
			return new AInt128(getVeryLongArray());
		case (short) 0xffff:
			return new Str(getString());
		default:
			return null;
		}
	}
}

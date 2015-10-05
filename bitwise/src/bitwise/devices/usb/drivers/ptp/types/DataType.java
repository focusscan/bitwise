package bitwise.devices.usb.drivers.ptp.types;

import java.nio.ByteBuffer;

import bitwise.devices.usb.drivers.ptp.types.prim.Decoder;
import bitwise.devices.usb.drivers.ptp.types.prim.Int128;
import bitwise.devices.usb.drivers.ptp.types.prim.Int16;
import bitwise.devices.usb.drivers.ptp.types.prim.Int32;
import bitwise.devices.usb.drivers.ptp.types.prim.Int64;
import bitwise.devices.usb.drivers.ptp.types.prim.Int8;
import bitwise.devices.usb.drivers.ptp.types.prim.Str;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt128;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt16;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt32;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt64;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt8;

public class DataType extends UInt16 {
	public DataType(ByteBuffer in) {
		super(in);
	}
	
	public DataType(short in_value) {
		super(in_value);
	}
	
	public Decoder<?> getDecoder() {
		switch (getValue()) {
		case (short) 0x0000: return null;
		case (short) 0x0001: return Int8.decoder;
		case (short) 0x0002: return UInt8.decoder;
		case (short) 0x0003: return Int16.decoder;
		case (short) 0x0004: return UInt16.decoder;
		case (short) 0x0005: return Int32.decoder;
		case (short) 0x0006: return UInt32.decoder;
		case (short) 0x0007: return Int64.decoder;
		case (short) 0x0008: return UInt64.decoder;
		case (short) 0x0009: return Int128.decoder;
		case (short) 0x000a: return UInt128.decoder;
		case (short) 0x4001: return Int8.arrayDecoder;
		case (short) 0x4002: return UInt8.arrayDecoder;
		case (short) 0x4003: return Int16.arrayDecoder;
		case (short) 0x4004: return UInt16.arrayDecoder;
		case (short) 0x4005: return Int32.arrayDecoder;
		case (short) 0x4006: return UInt32.arrayDecoder;
		case (short) 0x4007: return Int64.arrayDecoder;
		case (short) 0x4008: return UInt64.arrayDecoder;
		case (short) 0x4009: return Int128.arrayDecoder;
		case (short) 0x400a: return UInt128.arrayDecoder;
		case (short) 0xffff: return Str.decoder;
		}
		return null;
	}
}

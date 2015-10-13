package bitwise.devices.usbptpcamera.operations;

import bitwise.devices.usbptpcamera.UsbPtpBuffer;

public class DeviceInfo {
	public final short standardVersion;
	public final int vendorExtensionID;
	public final short vendorExtensionVersion;
	public final String vendorExtensionDesc;
	public final short functionalMode;
	public final short[] operationsSupported;
	public final short[] eventsSupported;
	public final short[] devicePropertiesSupported;
	public final short[] captureFormats;
	public final short[] imageFormats;
	public final String manufacturer;
	public final String model;
	public final String deviceVersion;
	public final String serialNumber;
	
	public DeviceInfo(UsbPtpBuffer buf) {
		standardVersion = buf.getShort();
		vendorExtensionID = buf.getInt();
		vendorExtensionVersion = buf.getShort();
		vendorExtensionDesc = buf.getString();
		functionalMode = buf.getShort();
		operationsSupported = buf.getShortArray();
		eventsSupported = buf.getShortArray();
		devicePropertiesSupported = buf.getShortArray();
		captureFormats = buf.getShortArray();
		imageFormats = buf.getShortArray();
		manufacturer = buf.getString();
		model = buf.getString();
		deviceVersion = buf.getString();
		serialNumber = buf.getString();
	}
}

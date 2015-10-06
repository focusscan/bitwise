package bitwise.devices.usb.drivers.ptp.types.responses;

import java.nio.ByteBuffer;

import bitwise.devices.usb.drivers.ptp.types.ObjectFormatCode;
import bitwise.devices.usb.drivers.ptp.types.deviceproperties.DevicePropCode;
import bitwise.devices.usb.drivers.ptp.types.events.EventCode;
import bitwise.devices.usb.drivers.ptp.types.operations.OperationCode;
import bitwise.devices.usb.drivers.ptp.types.prim.*;

public class DeviceInfo implements Response {
	public static final Decoder<DeviceInfo> decoder = new Decoder<DeviceInfo>() {
		@Override
		public DeviceInfo decode(ByteBuffer in) {
			return new DeviceInfo(in);
		}
	};
	
	private UInt16 standardVersion = null;
	private UInt32 vendorExtensionID = null;
	private UInt16 vendorExtensionVersion = null;
	private Str vendorExtensionDesc = null;
	private UInt16 functionalMode = null;
	private Arr<OperationCode> operationsSupported = null;
	private Arr<EventCode> eventsSupported = null;
	private Arr<DevicePropCode> devicePropertiesSupported = null;
	private Arr<ObjectFormatCode> captureFormats = null;
	private Arr<ObjectFormatCode> imageFormats = null;
	private Str manufacturer = null;
	private Str model = null;
	private Str deviceVersion = null;
	private Str serialNumber = null;
	
	public DeviceInfo(ByteBuffer in) {
		System.out.println("DeviceInfo");
		standardVersion = new UInt16(in);
		System.out.println(String.format(" version %d", standardVersion.getValue()));
		vendorExtensionID = new UInt32(in);
		System.out.println(String.format(" vendorExtensionID %d", vendorExtensionID.getValue()));
		vendorExtensionVersion = new UInt16(in);
		System.out.println(String.format(" vendorExtensionVersion %d", vendorExtensionVersion.getValue()));
		vendorExtensionDesc = new Str(in);
		System.out.println(String.format(" vendorExtensionDesc %s", vendorExtensionDesc.getValue()));
		functionalMode = new UInt16(in);
		System.out.println(String.format(" functionalMode %s", functionalMode));
		operationsSupported = new Arr<>(OperationCode.decoder, in);
		for (OperationCode code : operationsSupported.getValue())
			System.out.println(String.format(" operationsSupported %s", code));
		eventsSupported = new Arr<>(EventCode.decoder, in);
		for (EventCode code : eventsSupported.getValue())
			System.out.println(String.format(" eventsSupported %s", code));
		devicePropertiesSupported = new Arr<>(DevicePropCode.decoder, in);
		for (DevicePropCode code : devicePropertiesSupported.getValue())
			System.out.println(String.format(" devicePropertiesSupported %s", code));
		captureFormats = new Arr<>(ObjectFormatCode.decoder, in);
		for (ObjectFormatCode code : captureFormats.getValue())
			System.out.println(String.format(" captureFormats %s", code));
		imageFormats = new Arr<>(ObjectFormatCode.decoder, in);
		for (ObjectFormatCode code : imageFormats.getValue())
			System.out.println(String.format(" imageFormats %s", code));
		manufacturer = new Str(in);
		System.out.println(String.format(" manufacturer %s", manufacturer));
		model = new Str(in);
		System.out.println(String.format(" model %s", model));
		deviceVersion = new Str(in);
		System.out.println(String.format(" deviceVersion %s", deviceVersion));
		serialNumber = new Str(in);
		System.out.println(String.format(" serialNumber %s", serialNumber));
	}
	
	public UInt16 getStandardVersion() {
		return standardVersion;
	}
	
	public UInt32 getVendorExtensionID() {
		return vendorExtensionID;
	}
	
	public UInt16 getVendorExtensionVersion() {
		return vendorExtensionVersion;
	}
	
	public Str getVendorExtensionDesc() {
		return vendorExtensionDesc;
	}
	public UInt16 getFunctionalMode() {
		return functionalMode;
	}
	
	public Arr<OperationCode> getOperationsSupported() {
		return operationsSupported;
	}
	
	public Arr<EventCode> getEventsSupported() {
		return eventsSupported;
	}
	
	public Arr<DevicePropCode> getDevicePropertiesSupported() {
		return devicePropertiesSupported;
	}
	
	public Arr<ObjectFormatCode> getCaptureFormats() {
		return captureFormats;
	}
	
	public Arr<ObjectFormatCode> getImageFormats() {
		return imageFormats;
	}
	
	public Str getManufacturer() {
		return manufacturer;
	}
	
	public Str getModel() {
		return model;
	}
	
	public Str getDeviceVersion() {
		return deviceVersion;
	}
	
	public Str getSerialNumber() {
		return serialNumber;
	}
}

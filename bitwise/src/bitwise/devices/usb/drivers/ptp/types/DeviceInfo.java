package bitwise.devices.usb.drivers.ptp.types;

import bitwise.devices.usb.drivers.ptp.types.prim.*;

public class DeviceInfo {
	private UInt16 standardVersion;
	private UInt32 vendorExtensionID;
	private UInt16 vendorExtensionVersion;
	private Str vendorExtensionDesc;
	private UInt16 functionalMode;
	private Arr<OperationCode> operationsSupported;
	private Arr<EventCode> eventsSupported;
	private Arr<DevicePropCode> devicePropertiesSupported;
	private Arr<ObjectFormatCode> captureFormats;
	private Arr<ObjectFormatCode> imageFormats;
	private Str manufacturer;
	private Str model;
	private Str deviceVersion;
	private Str serialNumber;
	
	public DeviceInfo() {
		
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

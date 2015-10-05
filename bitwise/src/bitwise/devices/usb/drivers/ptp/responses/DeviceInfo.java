package bitwise.devices.usb.drivers.ptp.responses;

import java.nio.ByteBuffer;

import bitwise.devices.usb.drivers.ptp.operations.GetDeviceInfo;
import bitwise.devices.usb.drivers.ptp.types.DevicePropCode;
import bitwise.devices.usb.drivers.ptp.types.EventCode;
import bitwise.devices.usb.drivers.ptp.types.ObjectFormatCode;
import bitwise.devices.usb.drivers.ptp.types.OperationCode;
import bitwise.devices.usb.drivers.ptp.types.prim.*;

public class DeviceInfo extends BaseResponse {
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
		super(in);
		if (!getCode().equals(GetDeviceInfo.operationCode))
			return;
		standardVersion = new UInt16(in);
		System.out.println(String.format("version %d", standardVersion.getValue()));
		vendorExtensionID = new UInt32(in);
		System.out.println(String.format("vendorExtensionID %d", vendorExtensionID.getValue()));
		vendorExtensionVersion = new UInt16(in);
		System.out.println(String.format("vendorExtensionVersion %d", vendorExtensionVersion.getValue()));
		vendorExtensionDesc = new Str(in);
		System.out.println(String.format("vendorExtensionDesc %s", vendorExtensionDesc.getValue()));
		functionalMode = new UInt16(in);
		System.out.println(String.format("functionalMode %04x", functionalMode.getValue()));
		operationsSupported = new Arr<>(OperationCode.decoder, in);
		for (OperationCode code : operationsSupported.getValue())
			System.out.println(String.format("operationsSupported %04x", code.getValue()));
		eventsSupported = new Arr<>(EventCode.decoder, in);
		for (EventCode code : eventsSupported.getValue())
			System.out.println(String.format("eventsSupported %04x", code.getValue()));
		devicePropertiesSupported = new Arr<>(DevicePropCode.decoder, in);
		for (DevicePropCode code : devicePropertiesSupported.getValue())
			System.out.println(String.format("devicePropertiesSupported %04x", code.getValue()));
		captureFormats = new Arr<>(ObjectFormatCode.decoder, in);
		for (ObjectFormatCode code : captureFormats.getValue())
			System.out.println(String.format("captureFormats %04x", code.getValue()));
		imageFormats = new Arr<>(ObjectFormatCode.decoder, in);
		for (ObjectFormatCode code : imageFormats.getValue())
			System.out.println(String.format("imageFormats %04x", code.getValue()));
		manufacturer = new Str(in);
		System.out.println(String.format("manufacturer %s", manufacturer.getValue()));
		model = new Str(in);
		System.out.println(String.format("model %s", model.getValue()));
		deviceVersion = new Str(in);
		System.out.println(String.format("deviceVersion %s", deviceVersion.getValue()));
		serialNumber = new Str(in);
		System.out.println(String.format("serialNumber %s", serialNumber.getValue()));
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

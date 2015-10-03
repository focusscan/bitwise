package bitwise.devices.usb.drivers.ptp.operations;

public enum XXDELETEOperationName {
	/*
	Undefined,
	GetDeviceInfo,
	OpenSession,
	CloseSession,
	GetStorageIDs,
	GetStorageInfo,
	GetNumObjects,
	GetObjectHandles,
	GetObjectInfo,
	GetObject,
	GetThumb,
	DeleteObject,
	SendObjectInfo,
	SendObject,
	InitiateCapture,
	FormatStore,
	ResetDevice,
	SelfTest,
	SetObjectProtection,
	PowerDown,
	GetDevicePropDesc,
	GetDevicePropValue,
	SetDevicePropValue,
	ResetDevicePropValue,
	TerminateOpenCapture,
	MoveObject,
	CopyObject,
	GetPartialObject,
	InitiateOpenCapture,
	Reserved,
	VendorExtendedOperation;
	
	public static XXDELETEOperationName getOperationName(short in) {
		switch (in) {
		case 0x1000: return Undefined;
		case 0x1001: return GetDeviceInfo;
		case 0x1002: return OpenSession;
		case 0x1003: return CloseSession;
		case 0x1004: return GetStorageIDs;
		case 0x1005: return GetStorageInfo;
		case 0x1006: return GetNumObjects;
		case 0x1007: return GetObjectHandles;
		case 0x1008: return GetObjectInfo;
		case 0x1009: return GetObject;
		case 0x100a: return GetThumb;
		case 0x100b: return DeleteObject;
		case 0x100c: return SendObjectInfo;
		case 0x100d: return SendObject;
		case 0x100e: return InitiateCapture;
		case 0x100f: return FormatStore;
		case 0x1010: return ResetDevice;
		case 0x1011: return SelfTest;
		case 0x1012: return SetObjectProtection;
		case 0x1013: return PowerDown;
		case 0x1014: return GetDevicePropDesc;
		case 0x1015: return GetDevicePropValue;
		case 0x1016: return SetDevicePropValue;
		case 0x1017: return ResetDevicePropValue;
		case 0x1018: return TerminateOpenCapture;
		case 0x1019: return MoveObject;
		case 0x101a: return CopyObject;
		case 0x101b: return GetPartialObject;
		case 0x101c: return InitiateOpenCapture;
		}
		int msn = 0x0f & (in >> 24);
		// MSN 0001
		if (0x1 == msn)
			return Reserved;
		// MSN 1001
		if (0x9 == msn)
			return VendorExtendedOperation;
		return null;
	}
	
	public static short getOperationCode(XXDELETEOperationName in) {
		switch (in) {
		case Undefined: return 0x1000;
		case GetDeviceInfo: return 0x1001;
		case OpenSession: return 0x1002;
		case CloseSession: return 0x1003;
		case GetStorageIDs: return 0x1004;
		case GetStorageInfo: return 0x1005;
		case GetNumObjects: return 0x1006;
		case GetObjectHandles: return 0x1007;
		case GetObjectInfo: return 0x1008;
		case GetObject: return 0x1009;
		case GetThumb: return 0x100a;
		case DeleteObject: return 0x100b;
		case SendObjectInfo: return 0x100c;
		case SendObject: return 0x100d;
		case InitiateCapture: return 0x100e;
		case FormatStore: return 0x100f;
		case ResetDevice: return 0x1010;
		case SelfTest: return 0x1011;
		case SetObjectProtection: return 0x1012;
		case PowerDown: return 0x1013;
		case GetDevicePropDesc: return 0x1014;
		case GetDevicePropValue: return 0x1015;
		case SetDevicePropValue: return 0x1016;
		case ResetDevicePropValue: return 0x1017;
		case TerminateOpenCapture: return 0x1018;
		case MoveObject: return 0x1019;
		case CopyObject: return 0x101a;
		case GetPartialObject: return 0x101b;
		case InitiateOpenCapture: return 0x101c;
		default: return 0x0000;
		}
	}
	
	public static int getArity(XXDELETEOperationName in) {
		switch (in) {
		case Undefined: return 0;
		case GetDeviceInfo: return 0;
		case OpenSession: return 1;
		case CloseSession: return 0;
		case GetStorageIDs: return 0;
		case GetStorageInfo: return 1;
		case GetNumObjects: return 3;
		case GetObjectHandles: return 3;
		case GetObjectInfo: return 1;
		case GetObject: return 1;
		case GetThumb: return 1;
		case DeleteObject: return 2;
		case SendObjectInfo: return 2;
		case SendObject: return 0;
		case InitiateCapture: return 2;
		case FormatStore: return 2;
		case ResetDevice: return 0;
		case SelfTest: return 1;
		case SetObjectProtection: return 2;
		case PowerDown: return 0;
		case GetDevicePropDesc: return 1;
		case GetDevicePropValue: return 1;
		case SetDevicePropValue: return 1;
		case ResetDevicePropValue: return 1;
		case TerminateOpenCapture: return 1;
		case MoveObject: return 3;
		case CopyObject: return 3;
		case GetPartialObject: return 3;
		case InitiateOpenCapture: return 2;
		default: return 0x0000;
		}
	}
	*/
}

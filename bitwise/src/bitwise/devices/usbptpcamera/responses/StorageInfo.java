package bitwise.devices.usbptpcamera.responses;

import bitwise.devices.usbptpcamera.coder.UsbPtpBuffer;
import bitwise.devices.usbptpcamera.coder.UsbPtpCoderException;

public class StorageInfo {
	public static enum StorageType {
		FixedROM,
		RemovableROM,
		FixedRAM,
		RemovableRAM
	}
	
	public static enum FilesystemType {
		GenericFlat,
		GenericHierarchical,
		DCF
	}
	
	public static enum AccessCapability {
		ReadWrite,
		ReadOnlyNoDelete,
		ReadOnlyDelete
	}
	
	public final short storageType;
	public final short filesystemType;
	public final short accessCapability;
	public final long maxCapacity;
	public final long freeSpaceInBytes;
	public final int freeSpaceInImages;
	public final String storageDescription;
	public final String volumeLabel;
	
	public StorageInfo(UsbPtpBuffer buf) throws UsbPtpCoderException {
		storageType = buf.getShort();
		filesystemType = buf.getShort();
		accessCapability = buf.getShort();
		maxCapacity = buf.getLong();
		freeSpaceInBytes = buf.getLong();
		freeSpaceInImages = buf.getInt();
		storageDescription = buf.getString();
		volumeLabel = buf.getString();
	}
	
	public StorageType getStorageTypeEnum() {
		switch (storageType) {
		case (short) 0x0001:
			return StorageType.FixedROM;
		case (short) 0x0002:
			return StorageType.RemovableROM;
		case (short) 0x0003:
			return StorageType.FixedRAM;
		case (short) 0x0004:
			return StorageType.RemovableRAM;
		default:
			return null;
		}
	}
	
	public FilesystemType getFilesystemTypeEnum() {
		switch (filesystemType) {
		case (short) 0x0001:
			return FilesystemType.GenericFlat;
		case (short) 0x0002:
			return FilesystemType.GenericHierarchical;
		case (short) 0x0003:
			return FilesystemType.DCF;
		default:
			return null;
		}
	}
	
	public AccessCapability getAccessCapabilityEnum() {
		switch (accessCapability) {
		case (short) 0x0000:
			return AccessCapability.ReadWrite;
		case (short) 0x0001:
			return AccessCapability.ReadOnlyNoDelete;
		case (short) 0x0002:
			return AccessCapability.ReadOnlyDelete;
		default:
			return null;
		}
	}
}

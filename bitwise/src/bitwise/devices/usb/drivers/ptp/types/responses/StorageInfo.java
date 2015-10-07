package bitwise.devices.usb.drivers.ptp.types.responses;

import java.nio.ByteBuffer;

import bitwise.devices.usb.drivers.ptp.types.prim.*;

public class StorageInfo implements Response {
	public static final Decoder<StorageInfo> decoder = new Decoder<StorageInfo>() {
		@Override
		public StorageInfo decode(ByteBuffer in) {
			return new StorageInfo(in);
		}
	};
	
	private final UInt16 storageType;
	private final UInt16 filesystemType;
	private final UInt16 accessCapability;
	private final UInt64 maxCapacity;
	private final UInt64 freeSpaceInBytes;
	private final UInt32 freeSpaceInImages;
	private final Str storageDescription;
	private final Str volumeLabel;
	
	public StorageInfo(ByteBuffer in) {
		System.out.println("StorageInfo");
		storageType = UInt16.decoder.decode(in);
		System.out.println(String.format(" storageType %s", storageType));
		filesystemType = UInt16.decoder.decode(in);
		System.out.println(String.format(" filesystemType %s", filesystemType));
		accessCapability = UInt16.decoder.decode(in);
		System.out.println(String.format(" accessCapability %s", accessCapability));
		maxCapacity = UInt64.decoder.decode(in);
		System.out.println(String.format(" maxCapacity %s", maxCapacity));
		freeSpaceInBytes = UInt64.decoder.decode(in);
		System.out.println(String.format(" freeSpaceInBytes %s", freeSpaceInBytes));
		freeSpaceInImages = UInt32.decoder.decode(in);
		System.out.println(String.format(" freeSpaceInImages %s", freeSpaceInImages));
		storageDescription = Str.decoder.decode(in);
		System.out.println(String.format(" storageDescription %s", storageDescription));
		volumeLabel = Str.decoder.decode(in);
		System.out.println(String.format(" volumeLabel %s", volumeLabel));
	}
	
	public UInt16 getStorageType() {
		return storageType;
	}
	
	public UInt16 getFilesystemType() {
		return filesystemType;
	}
	
	public UInt16 getAccessCapability() {
		return accessCapability;
	}
	
	public UInt64 getMaxCapacity() {
		return maxCapacity;
	}
	
	public UInt64 getFreeSpaceInBytes() {
		return freeSpaceInBytes;
	}
	
	public UInt32 getFreeSpaceInImages() {
		return freeSpaceInImages;
	}
	
	public Str getStorageDescription() {
		return storageDescription;
	}
	
	public Str getVolumeLabel() {
		return volumeLabel;
	}
}

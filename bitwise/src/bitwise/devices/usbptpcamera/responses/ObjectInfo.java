package bitwise.devices.usbptpcamera.responses;

import bitwise.devices.usbptpcamera.coder.UsbPtpBuffer;
import bitwise.devices.usbptpcamera.coder.UsbPtpCoderException;

public class ObjectInfo {
	public final int storageID;
	public final short objectFormat;
	public final short protectionStatus;
	public final int objectCompressedSize;
	public final short thumbFormat;
	public final int thumbCompressedSize;
	public final int thumbPixWidth;
	public final int thumbPixHeight;
	public final int imagePixWidth;
	public final int imageBitDepth;
	public final int parentObject;
	public final short associationType;
	public final int associationDesc;
	public final int sequenceNumber;
	public final String filename;
	public final String captureDate;
	public final String modificationDate;
	public final String keywords;
	
	public ObjectInfo(UsbPtpBuffer buf) throws UsbPtpCoderException {
		storageID = buf.getInt();
		objectFormat = buf.getShort();
		protectionStatus = buf.getShort();
		objectCompressedSize = buf.getInt();
		thumbFormat = buf.getShort();
		thumbCompressedSize = buf.getInt();
		thumbPixWidth = buf.getInt();
		thumbPixHeight = buf.getInt();
		imagePixWidth = buf.getInt();
		imageBitDepth = buf.getInt();
		parentObject = buf.getInt();
		associationType = buf.getShort();
		associationDesc = buf.getInt();
		sequenceNumber = buf.getInt();
		filename = buf.getString();
		captureDate = buf.getString();
		modificationDate = buf.getString();
		keywords = buf.getString();
	}
}

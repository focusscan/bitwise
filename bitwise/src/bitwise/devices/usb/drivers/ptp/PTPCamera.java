package bitwise.devices.usb.drivers.ptp;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

import javax.usb.UsbDisconnectedException;
import javax.usb.UsbException;
import javax.usb.UsbInterface;
import javax.usb.UsbInterfacePolicy;
import javax.usb.UsbNotActiveException;
import javax.usb.UsbNotClaimedException;
import javax.usb.UsbNotOpenException;

import bitwise.apps.App;
import bitwise.devices.kinds.FullCamera;
import bitwise.devices.usb.UsbContext;
import bitwise.devices.usb.drivers.UsbDriver;
import bitwise.devices.usb.drivers.ptp.operations.CloseSession;
import bitwise.devices.usb.drivers.ptp.operations.GetDeviceInfo;
import bitwise.devices.usb.drivers.ptp.operations.OpenSession;
import bitwise.devices.usb.drivers.ptp.operations.Operation;
import bitwise.devices.usb.drivers.ptp.responses.BaseResponse;
import bitwise.devices.usb.drivers.ptp.responses.DeviceInfo;
import bitwise.devices.usb.drivers.ptp.responses.Response;

public abstract class PTPCamera extends UsbDriver implements FullCamera {
	private final byte interfaceAddr;
	private final byte dataOutEPNum;
	private final byte dataInEPNum;
	private final byte interruptEPNum;
	private javax.usb.UsbConfiguration activeConfig;
	private javax.usb.UsbInterface ptpInterface;
	private javax.usb.UsbEndpoint dataOutEP;
	private javax.usb.UsbEndpoint dataInEP;
	private javax.usb.UsbEndpoint interruptEP;
	private javax.usb.UsbPipe dataOutPipe;
	private javax.usb.UsbPipe dataInPipe;
	private javax.usb.UsbPipe interruptPipe;
	private DeviceInfo deviceInfo;
	
	public PTPCamera(byte in_interfaceAddr, byte in_dataOutEPNum, byte in_dataInEPNum, byte in_interruptEPNum, App in_app) {
		super(in_app);
		interfaceAddr = in_interfaceAddr;
		dataOutEPNum = in_dataInEPNum;
		dataInEPNum = in_dataOutEPNum;
		interruptEPNum = in_interruptEPNum;
	}
	
	public DeviceInfo getDeviceInfo() {
		return deviceInfo;
	}
	
	protected abstract boolean onPtpInitialize(UsbContext ctx) throws InterruptedException;
	protected abstract void onPtpDisable();
	
	@Override
	protected boolean onDriverInitialize(UsbContext ctx) throws InterruptedException {
		javax.usb.UsbDevice platformDevice = getDevice().getPlatformDevice();
		activeConfig = platformDevice.getActiveUsbConfiguration();
		ptpInterface = activeConfig.getUsbInterface(interfaceAddr);
		
		try {
			ptpInterface.claim(new UsbInterfacePolicy() {
				@Override
				public boolean forceClaim(UsbInterface iface) {
					return true;
				}
			});
		} catch (UsbNotActiveException | UsbDisconnectedException | UsbException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		}
		
		dataOutEP = ptpInterface.getUsbEndpoint(dataOutEPNum);
		dataInEP = ptpInterface.getUsbEndpoint(dataInEPNum);
		interruptEP = ptpInterface.getUsbEndpoint(interruptEPNum);
		assert(null != dataOutEP);
		assert(null != dataInEP);
		assert(null != interruptEP);
		System.out.println("PTP driver initializing:");
		System.out.println(String.format("  dataInEP: %02x, type %02x", dataOutEP.getUsbEndpointDescriptor().bEndpointAddress(), dataOutEP.getType()));
		System.out.println(String.format("  dataOutEP: %02x, type %02x", dataInEP.getUsbEndpointDescriptor().bEndpointAddress(), dataInEP.getType()));
		System.out.println(String.format("  interruptEP: %02x, type %02x", interruptEP.getUsbEndpointDescriptor().bEndpointAddress(), interruptEP.getType()));
		
		dataOutPipe = dataOutEP.getUsbPipe();
		dataInPipe = dataInEP.getUsbPipe();
		interruptPipe = interruptEP.getUsbPipe();
		try {
			dataOutPipe.open();
			dataInPipe.open();
			interruptPipe.open();
		} catch (UsbNotActiveException | UsbNotClaimedException | UsbDisconnectedException | UsbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		try {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			Operation getDeviceInfo = new GetDeviceInfo();
			getDeviceInfo.serialize(stream);
			ptpSend(stream.toByteArray());
			byte[] recv = ptpRecv();
			deviceInfo = new DeviceInfo(ByteBuffer.wrap(recv));
			System.out.println(String.format("deviceInfo type: %04x code: %04x", deviceInfo.getType().getValue(), deviceInfo.getCode().getValue()));
		} catch (UsbNotActiveException | UsbNotOpenException | IllegalArgumentException | UsbDisconnectedException
				| UsbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		try {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			Operation operation = new OpenSession();
			operation.serialize(stream);
			ptpSend(stream.toByteArray());
			byte[] recv = ptpRecv();
			Response response = new BaseResponse(ByteBuffer.wrap(recv));
			System.out.println(String.format("openSession type %04x code: %04x", response.getType().getValue(), response.getCode().getValue()));
		} catch (UsbNotActiveException | UsbNotOpenException | IllegalArgumentException | UsbDisconnectedException
				| UsbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return onPtpInitialize(ctx);
	}
		
	protected void onDriverDisable() {
		onPtpDisable();
		
		try {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			Operation operation = new CloseSession();
			operation.serialize(stream);
			ptpSend(stream.toByteArray());
			byte[] recv = ptpRecv();
			Response response = new BaseResponse(ByteBuffer.wrap(recv));
			System.out.println(String.format("closeSession type %04x code: %04x", response.getType().getValue(), response.getCode().getValue()));
		} catch (InterruptedException | UsbNotActiveException | UsbNotOpenException | IllegalArgumentException | UsbDisconnectedException
				| UsbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			if (null != dataOutPipe && dataOutPipe.isOpen())
				dataOutPipe.close();
		} catch (UsbNotActiveException | UsbNotOpenException | UsbDisconnectedException | UsbException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
		if (null != dataOutPipe && dataInPipe.isOpen())
			dataInPipe.close();
		} catch (UsbNotActiveException | UsbNotOpenException | UsbDisconnectedException | UsbException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
		if (null != dataOutPipe && interruptPipe.isOpen())
			interruptPipe.close();
		} catch (UsbNotActiveException | UsbNotOpenException | UsbDisconnectedException | UsbException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			ptpInterface.release();
		} catch (UsbNotActiveException | UsbDisconnectedException | UsbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void ptpSend(byte[] data) throws InterruptedException, UsbNotActiveException, UsbNotOpenException, UsbDisconnectedException, UsbException {
		javax.usb.UsbIrp irp = dataOutPipe.asyncSubmit(data);
		while (!irp.isComplete())
			Thread.sleep(20);
	}
	
	protected byte[] ptpRecv() throws InterruptedException, UsbNotActiveException, UsbNotOpenException, UsbDisconnectedException, UsbException {
		byte[] buf = new byte[dataInEP.getUsbEndpointDescriptor().wMaxPacketSize()];
		javax.usb.UsbIrp irp = dataInPipe.asyncSubmit(buf);
		while (!irp.isComplete())
			Thread.sleep(20);
		return buf;
	}
}

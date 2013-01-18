package primayer.android.delta;

import org.apache.http.util.EncodingUtils;

import primayer.android.delta.commands.CommandParseException;
import primayer.android.delta.commands.DeltaCommand;
import primayer.android.delta.commands.LoggerErrorException;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;

public class DeltaDevice {
	private static final String TAG = "DeltaUSB_Device";

	public static Boolean IsCompatibleDevice(UsbDevice device)
	{
		int pid = device.getProductId();
		int vid = device.getVendorId();
		
		if( vid == 5824 && 
				(pid == 1073 ||	//xilog+
				pid == 1074)){	//xstream
			return true;
		}
		
		return false;
	}
	
	private static int TIMEOUT = 500;
	
	private UsbDevice mDevice = null;
	private UsbEndpoint mBulkIn = null;
	private UsbEndpoint mBulkOut = null;
	private UsbInterface mInterface = null;
	private UsbDeviceConnection mConnection = null;
	private UsbManager mManager = null;
	
	public DeltaDevice(UsbManager manager, UsbDevice device){
		
		mManager = manager;
		mDevice = device;
		
		//get interfaces and endpoints
		mInterface = device.getInterface(0);
	}
	
	/**
	 * @return The UsbDevice instance wrapped by this DeltaDevice.
	 */
	public UsbDevice getUsbDevice(){
		return mDevice;
	}
	
	/**Writes a string to the device.
	 * @param Message to write.
	 * @return Returns true if successful.
	 */
	public boolean writeString(String message){
		int count = 0;
		
		if(mConnection != null){
			byte[] data = EncodingUtils.getAsciiBytes(message + "\r\n");
			
			count = mConnection.bulkTransfer(mBulkOut, data, data.length, TIMEOUT);
		}
		else return false;
		
		return count >= 0;
	}
	
	public String readLine(){
		if(mConnection != null){
			
			String result = "";
			
			byte[] buffer = new byte[64];
			int count = 0;
			while((count = mConnection.bulkTransfer(mBulkIn, buffer, 64, TIMEOUT)) > 0)
			{
				if(count < 0)
				{
					return null;
				}
				
				String t = EncodingUtils.getAsciiString(buffer);
				
				if(t.length() > 0)
				{
					int index = t.indexOf("\0");
					if(index >= 0)
						result += t.substring(0, index);
					else
						result += t;
				}
			}
			return result;
		}
		return null;
	}
	
	public boolean isConnectionOpen(){
		return mConnection != null;
	}
	
	public boolean isDeviceAttached(){
		if(mManager != null){
			return mManager.getDeviceList().containsValue(mDevice);	
		}
		
		return false;
	}
	public void sendCommand(DeltaCommand command, boolean changesOnly) throws CommandParseException, LoggerErrorException{
		String cs = command.toString(changesOnly);
		writeString(cs);
		
		String rs = readLine();
		StringBuilder b = new StringBuilder(rs.trim());
		
		if(rs.length() > 0){
			b.setLength(b.length() - 5);
		}
		command.parse(b);
	}
	public void start(){	
		for(int i = 0; i < mInterface.getEndpointCount(); i++){
			
			UsbEndpoint ep = mInterface.getEndpoint(i);
			
			switch(ep.getDirection())
			{
				case UsbConstants.USB_DIR_IN:
					mBulkIn = ep;
					break;
				case UsbConstants.USB_DIR_OUT:
					mBulkOut = ep;
					break;
			}
		}
		
		mConnection = mManager.openDevice(mDevice);
		mConnection.claimInterface(mInterface, false);
		
		this.writeString("#LI\r\n"); //wake the logger up
		this.readLine();
	}
	
	public void close(){
		if(mConnection != null)
		{
			mConnection.releaseInterface(mInterface);
			mConnection.close();
			mConnection = null;
		}
	}

}

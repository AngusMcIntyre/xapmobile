package primayer.android.delta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;


/**
 * @author angus.mcintyre
 *
 */
public class DeltaUsbManager{
	public static final String ACTION_DELTADEVICE_ATTACHED = "primayer.android.delta.ACTION_DELTADEVICE_ATTACHED";
	public static final String ACTION_DELTADEVICE_DETACHED = "primayer.android.delta.ACTION_DELTADEVICE_DETACHED";
	public static final String EXTRA_DELTADEVICE = "primayer.android.delta.EXTRA_DELTADEVICED";
	private static final String TAG = "DeltaUSB_Manager";
	
	List<DeltaDevice> mDevices = new ArrayList<DeltaDevice>(); 
	UsbManager mUsbManager = null;
	Context mContext = null;
	boolean mIsStarted = false;
	Object mPermissionWait = new Object(); 
	
	public DeltaUsbManager(Context context) {
		mContext = context;
		mUsbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
	}
	
	/**
	 * @return true if manager has been started.
	 */
	public boolean getIsStarted(){
		return mIsStarted;
	}
	
	public UsbManager getUsbManager()
	{
		return mUsbManager;
	}

	public HashMap<String, UsbDevice> GetConnectedDevices(){
		
		HashMap<String, UsbDevice> devices = new HashMap<String, UsbDevice>();
		
		for(Entry<String, UsbDevice> entry: mUsbManager.getDeviceList().entrySet())
		{
			if(DeltaDevice.IsCompatibleDevice(entry.getValue())){
				devices.put(entry.getKey(), entry.getValue());
			}
		}
		
		return devices;
	}
}

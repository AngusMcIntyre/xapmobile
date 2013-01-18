package primayer.android.delta;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbManager;
import android.os.Bundle;

public class ManagerTestActivity extends Activity {

	private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent) {			
			synchronized(this)
			{
				String action = intent.getAction();
				
				if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
					
				}
				else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
		        }
			}
		}
	};
	
	DeltaUsbManager mManager = null;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.registerListeners();
		
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		unregisterListeners();
	}
	
	private void registerListeners(){
		//set up permission requests for usb devices that are already connected
		IntentFilter filter = new IntentFilter();
		filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
		filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
		registerReceiver(mUsbReceiver, filter);
	}
	
	private void unregisterListeners(){
		this.unregisterReceiver(mUsbReceiver);
	}

}

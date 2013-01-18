package primayer.android.delta;

import android.app.Activity;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;

/**
 * Invisible activity responsible for catching and forwarding delta device connections
 * @author angus.mcintyre
 */
public class DeltaDeviceCaptureActivity extends Activity {

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = this.getIntent();
		UsbDevice device = (UsbDevice)intent.getExtras().get(UsbManager.EXTRA_DEVICE);
		
		if(DeltaDevice.IsCompatibleDevice(device)){
			//send delta device attached event
			Intent i = new Intent();
			i.setAction(DeltaUsbManager.ACTION_DELTADEVICE_ATTACHED);
			i.putExtra(DeltaUsbManager.EXTRA_DELTADEVICE, device);
 			this.sendBroadcast(i);
		}
		
		//shutdown
		this.finish();
	}
}
package primayer.android.delta;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class UsbDeviceReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		
		//resend this intent
		//TODO - make this local only
		//Intent ni = (Intent)intent.clone();
		//ni.setPackage(context.getApplicationInfo().name);
		//context.sendBroadcast(ni);
	}

}

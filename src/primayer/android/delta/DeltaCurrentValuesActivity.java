package primayer.android.delta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import primayer.android.delta.commands.CommandACAL;
import primayer.android.delta.commands.CommandIMV;
import primayer.android.delta.commands.CommandParseException;
import primayer.android.delta.commands.LoggerErrorException;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.InflateException;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class DeltaCurrentValuesActivity extends ListActivity {
	
	private static final String TAG = "primayer.android.delta.CurrentValuesActivity";
	private ScheduledExecutorService mScheduleTaskExecutor = null;
	private ScheduledFuture<?> mFuture = null;
	
	private DeltaDevice mDevice = null;
	private DeltaUsbManager mManager = null;
	private SimpleAdapter mAdapter = null;
	
	static final String KEY_LABEL = "LABEL";
	static final String KEY_VALUE = "VALUE";
	static final String KEY_DESCRIPTION = "DESC";
	static final String KEY_ISANALOG = "ISANALOG";
	static final String KEY_CANAUTOZERO = "CANAUTOZERO";
	static final String KEY_CHANINDEX = "CHANINDEX";
	
	String[] from = new String[] { KEY_LABEL, KEY_VALUE };
	int[] to = new int[] { android.R.id.text1, android.R.id.text2 };
	ArrayList<HashMap<String,String>> mDataSet = new ArrayList<HashMap<String,String>>();
	boolean mRequestingPermission = false;
	
	private static final String ACTION_USB_PERMISSION = TAG + ".USB_PERMISSION";
	
	private final BroadcastReceiver mPermissionReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent) {		
			String action = intent.getAction();
			
			if (ACTION_USB_PERMISSION.equals(action)) {
	            UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
	            
	            if(device != null){
	            	if(mDevice == null && intent.getExtras().getBoolean(UsbManager.EXTRA_PERMISSION_GRANTED))
	            	{
	            		mDevice = new DeltaDevice(mManager.getUsbManager(), device);
	            		mDevice.start();	//start the device now we know we have permission
	            		DoTask();	//force refresh of data asap
	            	}
	            }
	            
	            mRequestingPermission = false;
			}
		}
	};
	
    /*Called when the activity is first created.*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.monitor);
        
        mManager = new DeltaUsbManager(this);
        mScheduleTaskExecutor = Executors.newScheduledThreadPool(10);
        
        mAdapter = new SimpleAdapter(this, mDataSet, android.R.layout.simple_list_item_2, from, to);
		this.setListAdapter(mAdapter);
		
		this.registerForContextMenu(getListView());
    } 

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
	
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
		
		HashMap<String, String> data = GetData(info);
		
		if(data != null){
			if(data.containsKey(KEY_CANAUTOZERO)){
				menu.setHeaderTitle(data.get(KEY_LABEL));
				menu.add(Menu.NONE, R.string.menuItem_Autozero, Menu.NONE, R.string.menuItem_Autozero);
			}
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if(R.string.menuItem_Autozero == item.getItemId()){
			
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
			final HashMap<String, String> data = GetData(info);
			
				this.mScheduleTaskExecutor.execute(new Runnable(){

					public void run() {
						try {	
						if(data.containsKey(KEY_CHANINDEX))
							DeltaCurrentValuesActivity.this.mDevice.sendCommand(new CommandACAL((String)data.get(KEY_CHANINDEX)), true);		
						} catch (CommandParseException e) {
							e.printStackTrace();
						} catch (LoggerErrorException e){
							e.printStackTrace();
						}
					}
				});
				
			}
			return true;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		try {
			this.getMenuInflater().inflate(R.menu.activity_currentvalues, menu);
			return true;
		} catch (InflateException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.menu_item_about){
			Intent intent = new Intent(this, AboutActivity.class);
			this.startActivity(intent);
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}

	HashMap<String,String> GetData(AdapterView.AdapterContextMenuInfo info)
	{
		try
		{
			@SuppressWarnings("unchecked")
			HashMap<String, String> data = (HashMap<String,String>)mAdapter.getItem(info.position);
			
			return data;
		}
		catch(ClassCastException ex){
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();
		
		this.registerReceiver(mPermissionReceiver, new IntentFilter(ACTION_USB_PERMISSION));
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		//TODO - thread this off.
		
		if(mDevice !=null && mDevice.isDeviceAttached()){
				if(!mDevice.isConnectionOpen())
					mDevice.start();
		} else mDevice = null;
		
		//start recurring update task in onResume so that the activity can be paused and still restart it
		try
		{
			mFuture = mScheduleTaskExecutor.scheduleAtFixedRate(new Runnable(){
	
				public void run() {
						DoTask();			
				}}, 0, 2, TimeUnit.SECONDS);
		}
		catch(Exception ex)
		{
			Log.e(TAG, Log.getStackTraceString(ex));
		}
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		super.onPause();
		
		if(mFuture != null)	//cancel scheduled task
			mFuture.cancel(false);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		super.onStop();
		
		this.unregisterReceiver(mPermissionReceiver);
		
		if(mDevice != null)
			mDevice.close();
	}
	
	synchronized void DoTask(){
		try {
			//find a device if we dont have one
			if(mDevice == null && !mRequestingPermission){
				findDevice();
			}
			
			if(mDevice != null &&
					mDevice.isDeviceAttached() &&
					mDevice.isConnectionOpen()){
				//send an IMV
				final CommandIMV imvCom = new CommandIMV();
				
				try
				{
					mDevice.sendCommand(imvCom, true);
				}
				catch(CommandParseException ex){}
				catch(LoggerErrorException ex){}

				
				this.runOnUiThread(new Runnable()
				{
					public void run() {
						//tx.setText(postRes);
						setValuesInAdapter(imvCom);
					}
				});
			}
			else if(mDevice != null){
				mDevice.close();
				mDevice = null;	//attempt to aquire a device next loop
				onDeviceDisconnected();
			}
		} catch (Exception e) {
			Log.e(TAG, "Exception in value loop", e);
		}
	}

	void setValuesInAdapter(CommandIMV command)
	{
		HashMap<String,String> map = new HashMap<String,String>();
		String format = this.getResources().getString(R.string.digital_format);
		mDataSet.clear();
		
		//digital 1a
		if(command.Digital1AHasValue){
			String chanIndex = "1a";
			map.put(KEY_CHANINDEX, chanIndex);
			map.put(KEY_LABEL, String.format(format, chanIndex));
			map.put(KEY_VALUE, command.Digital1AValue.getValue() + " " + command.Digital1AUnit.getValue().suffix);
			mDataSet.add(map);
		}
		
		//digital 1b
		map = new HashMap<String, String>();
		if(command.Digital1BHasValue){
			String chanIndex = "1b";
			map.put(KEY_CHANINDEX, chanIndex);
			map.put(KEY_LABEL, String.format(format, chanIndex));
			map.put(KEY_VALUE, command.Digital1BValue.getValue() + " " + command.Digital1BUnit.getValue().suffix);
			mDataSet.add(map);
		}
			
		//analogue channels
		format = this.getResources().getString(R.string.analogue_format);
		for(int i = 1; i <= 7; i++){		
			if(command.hasAnalogueChannelGotValue(i)){
				
				map = new HashMap<String, String>();
			
				String chanIndex = Integer.toString(i);
				map.put(KEY_CHANINDEX, chanIndex);
				map.put(KEY_LABEL, String.format(format, chanIndex));
				map.put(KEY_CANAUTOZERO, "1");
				map.put(KEY_VALUE, command.getAnalogueValue(i).getValue() + " " + command.getAnalogueUnit(i).getValue().suffix);
				
				mDataSet.add(map);
			}
		}
		
//		//other stats
		map = new HashMap<String, String>();
		map.put(KEY_LABEL, this.getResources().getString(R.string.battery_voltage));
		map.put(KEY_VALUE, command.InternalBatteryVoltage.getValue().toString() + "V");
		mDataSet.add(map);
		
		map = new HashMap<String, String>();
		map.put(KEY_LABEL, this.getResources().getString(R.string.externalpower_voltage));
		map.put(KEY_VALUE, command.ExternalPowerVoltage.getValue().toString() + "V");
		mDataSet.add(map);
		
		map = new HashMap<String, String>();
		map.put(KEY_LABEL, this.getResources().getString(R.string.gsm_signalstrange));
		map.put(KEY_VALUE, command.GSMSignalStrength.getValue().toString() + "%");
		mDataSet.add(map);
		
		mAdapter.notifyDataSetChanged();
	}
	
	void findDevice(){
		if(mManager.GetConnectedDevices().size() > 0)
		{
			UsbDevice dev =  mManager.GetConnectedDevices().values().iterator().next();
			
			if(dev != null){
				PendingIntent pi = PendingIntent.getBroadcast(getBaseContext(), 0, new Intent(ACTION_USB_PERMISSION), 0);
				
				mRequestingPermission = true;
				mManager.mUsbManager.requestPermission(dev, pi);
			}
		}
	}
	
	synchronized void onDeviceDisconnected()
	{
		this.runOnUiThread(new Runnable()
		{
			public void run() {
				//throw up a toast to notify of disconnection
				Toast.makeText(DeltaCurrentValuesActivity.this, R.string.logger_disconnected, Toast.LENGTH_LONG).show();
				mDataSet.clear();
				mAdapter.notifyDataSetChanged();
			}
		});
	}
}
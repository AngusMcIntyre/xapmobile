package primayer.android.delta;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends Activity {

	SparseArray<Uri> mLocationMap = new SparseArray<Uri>();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        //mLocationMap.put(R.id.uk_viewonmap, Uri.parse("geo://primayer uk"));
        mLocationMap.put(R.id.uk_viewonmap, Uri.parse("geo:50.894987,-1.068855?q=50.894987,-1.068855(Primayer+Ltd)"));
        mLocationMap.put(R.id.france_viewonmap, Uri.parse("geo:45.794392,4.792204?q=45.794392,4.792204(Primayer+SAS)"));
        mLocationMap.put(R.id.malaysia_viewonmap, Uri.parse("geo:3.005029,101.536036?q=3.005029,101.536036(Primayer+Sdn+Bhd)"));
        
        //update version number display
        TextView versionView = (TextView)this.findViewById(R.id.textView_version);
        String vName;
		try {
			vName = this.getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
			versionView.setText(versionView.getText() + " " + vName);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			Log.w(this.getClass().toString(), "Error getting package version number");
			e.printStackTrace();
		}
        		
        //check if mapping is avaliable
        if(!CheckMappingExists()){
        	View v = this.findViewById(R.id.uk_viewonmap);
        	if(v != null)
        		v.setVisibility(View.GONE);
        	
        	v = this.findViewById(R.id.france_viewonmap);
        	if(v != null)
        		v.setVisibility(View.GONE);
        	
        	v = this.findViewById(R.id.malaysia_viewonmap);
        	if(v != null)
        		v.setVisibility(View.GONE);
        }
    }
    
    boolean CheckMappingExists()
    {
    	final PackageManager packageManager = this.getPackageManager();
    	Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0"));
        List<ResolveInfo> resolveInfo =
                packageManager.queryIntentActivities(i,
                        PackageManager.MATCH_DEFAULT_ONLY);
       if (resolveInfo.size() > 0) {
         return true;
        }
       
       return false;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void onViewOnMapClicked(View view)
    {
    	Intent i = null;
    	Uri uri = mLocationMap.get(view.getId(), null);
    	
    	if(uri != null)
    	{  	
	    	i = new Intent(Intent.ACTION_VIEW, mLocationMap.get(view.getId()));
	    	
	    	if(i != null){
		    	try {
					this.startActivity(i);
				} catch (ActivityNotFoundException e) {
					e.printStackTrace();
				}
	    	}
    	}
    }

}

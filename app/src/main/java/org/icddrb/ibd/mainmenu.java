package org.icddrb.ibd;


import java.io.File;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.AdapterView.OnItemClickListener; 
import android.widget.TextView;
import Common.*;

public class mainmenu extends Activity{
	public static String Res="";
	public static String D[]=null;
	Connection C;
	Global g1;
	boolean netwoekAvailable=false;
	
	Location currentLocation; 
	double currentLatitude,currentLongitude; 
	
	Location currentLocationNet; 
	double currentLatitudeNet,currentLongitudeNet; 


	//Disabled Back/Home key
	//--------------------------------------------------------------------------------------------------	
	@Override 
	public boolean onKeyDown(int iKeyCode, KeyEvent event)
	{
	    if(iKeyCode == KeyEvent.KEYCODE_BACK || iKeyCode == KeyEvent.KEYCODE_HOME) 
	         { return false; }
	    else { return true;  }
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu);
        
        C  = new Connection(this);        
        g1 = Global.getInstance();
        
        //TextView lblRoundNo = (TextView) findViewById(R.id.lblRoundNo);
        //lblRoundNo.setText("Round No: "+ g1.getRoundNo());

        turnGPSOn();
        
        //GPS Location
        FindLocation();
        FindLocationNet();
        
		//Check for Internet connectivity
        //*******************************************************************************
    	if (Connection.haveNetworkConnection(this)) {    		 
    		netwoekAvailable=true;
    	 
    	} else {     	 
    		netwoekAvailable=false;
    	} 
        
        //Main Menu
        //*******************************************************************************      
        GridView g=(GridView) findViewById(R.id.grid);
		g.setAdapter(new menuAdapter(this));
		g.setOnItemClickListener(new OnItemClickListener() { 
			 
		
         public void onItemClick(AdapterView<?> parent, View v, int position, long id) { 
          
        	 try
        	 {
                 Bundle IDbundle = new Bundle();

                //Birth
                if(position==0)
             	{
                    /*IDbundle.putString("birthdeath", "birth");
	    			Intent f1 = new Intent(getApplicationContext(),BirthDeath.class);
                    f1.putExtras(IDbundle);
					startActivity(f1);*/
             	}
             	
             	//Death
             	//*******************************************************************************
               	else if(position==1)
            	{
                    /*IDbundle.putString("birthdeath", "death");
	    			Intent f1 = new Intent(getApplicationContext(),BirthDeath.class);
                    f1.putExtras(IDbundle);
					startActivity(f1);*/
            	}
                //List of visit
                 //*******************************************************************************
                 else if(position==2)
                 {
                     /*//IDbundle.putString("birthdeath", "death");
                     Intent f1 = new Intent(getApplicationContext(),BirthDeathList.class);
                     //f1.putExtras(IDbundle);
                     startActivity(f1);*/
                 }

            	//Upload data to central server
            	//*******************************************************************************
               	else if(position==3)
            	{            		
               		AlertDialog.Builder builder = new AlertDialog.Builder(mainmenu.this);
        	    	builder
        			    	.setTitle("Message")	    			    	
        			    	.setMessage("আপনি কি তথ্য ডাটা বেজ সার্ভারে আপলোড/ডাউনলোড করতে চান[হ্যাঁ/না]?")
        			    	.setIcon(android.R.drawable.ic_dialog_alert)
        			    	.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
    		    	    	   public void onClick(DialogInterface dialog, int which) {		
    		    	    	    	switch (which){
    		    	    	        case DialogInterface.BUTTON_POSITIVE:
    		    	    	        	
    		    	    	        	if(netwoekAvailable==false)
    		    	                	{
    		    	                		Connection.MessageBox(mainmenu.this, "Internet connection is not avialable.");
    		    	                		return;
    		    	                	}
    		    	            		
    		    	            		try
    		    	            		{    		    	            			
    		    	            			String ResponseString="Status:";

        		    				        final ProgressDialog progDailog = ProgressDialog.show( 
        		    				        		mainmenu.this, "", "অপেক্ষা করুন ...", true); 

        		    				        new Thread() { 
        		    				            public void run() { 
        		    				            	String ResponseString="Status:";
        		    				            	String response;
        		    				            	
        		    				                try {
        		    		    	    	        	String VariableList;
        		    		    	    	        	String TableName;

        		    		    	    	        	//String Cluster = g.getClusterCode();

        		    		    	    	        	//Upload/Download Status: 09 Apr 2014
        		    		    	    	        	//String r = C.ExecuteCommandOnServer("Insert into UploadMonitor(Dist, DeviceNo)Values('"+ g1.getCurrentDistrict() +"','"+ g1.getDeviceNo() +"')");

        		    		    	    	        	//Upload total record in local database

        		        		    	            	//Upload
        		        		    	            	//**************************************************************************************************************************
        		    		    	    	        	//Table: HHListing
	    		    		    	            		TableName = "HHListing";
	    		        		    	    	        VariableList = "Dist, Upz, UN, Vill, Indx, ELCO, NearVill, FSCode, BariName, HH, HHHead, VDate, CMWRA, CMWRAInt, RDW, RDWInt, RoundNo, UserId, EnDt, Upload";
	    		        		    	    	        String[] H = C.GenerateArrayList(VariableList, TableName);
	    		        		    	            	//response = C.UploadData(H, TableName , VariableList , "Dist, Upz, UN, Vill, Indx, HH, RoundNo");

	    		        		    	            	Connection.MessageBox(mainmenu.this, "তথ্য ডাটাবেজ সার্ভারে সম্পূর্ণ ভাবে আপলোড হয়েছে। ");
	    		        		    	            	
        		    				                } catch (Exception e) { 
        		    				                	
        		    				                } 
        		    				                progDailog.dismiss(); 
        		    				                
        		    				            } 
        		    				        }.start(); 	
    		    				        		    				        
    		    	            		}
    		    	            		catch(Exception ex)
    		    	            		{
    		    	            			Connection.MessageBox(mainmenu.this, ex.getMessage());
    		    	            		}
    		    	    	        	
    		    	    	            break;
    		
    		    	    	        case DialogInterface.BUTTON_NEGATIVE:
    		    	    	            //No button clicked
    		    	    	            break;
    		    	    	        }
    		    	    	    }
        			    })
        	    	.setNegativeButton("No", null)	//Do nothing on no
        	    	.show(); 	    		
       	    	   	    	             		    
            	} 


            	//Exit from the system
            	//*******************************************************************************
            	else if(position==4)
            	{
    				
    				AlertDialog.Builder builder = new AlertDialog.Builder(mainmenu.this);
        	    	builder
        			    	.setTitle("Message")	    			    	
        			    	.setMessage("Do you want to exit from the system[Y/N]?")
        			    	.setIcon(android.R.drawable.ic_dialog_alert)
        			    	.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
    		    	    	   public void onClick(DialogInterface dialog, int which) {		
    		    	    	    	switch (which){
    		    	    	        case DialogInterface.BUTTON_POSITIVE:
    		    	    	        	turnGPSOff();
	    		    	    	        finish();
	    		    	    			System.exit(0);
    		    	    	            break;
    		
    		    	    	        case DialogInterface.BUTTON_NEGATIVE:
    		    	    	            //No button clicked
    		    	    	            break;
    		    	    	        }
    		    	    	    }
        			    })
        	    	.setNegativeButton("No", null)	//Do nothing on no
        	    	.show();     				
            	}
            	
             }
             catch(Exception ex)
             {
            	 Connection.MessageBox(mainmenu.this, ex.getMessage());
             }            	
            		
            } 

        }); 

		

    }
    
    
    
    
    
    //GPS Reading
    //.....................................................................................................
    public void FindLocation() { 
        LocationManager locationManager = (LocationManager) this 
                .getSystemService(Context.LOCATION_SERVICE); 
 
        LocationListener locationListener = new LocationListener() { 
            public void onLocationChanged(Location location) { 
                updateLocation(location);                  
                } 
 
            public void onStatusChanged(String provider, int status, 
                    Bundle extras) { 
            } 
 
            public void onProviderEnabled(String provider) { 
            } 
 
            public void onProviderDisabled(String provider) { 
            } 
        }; 
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }
    
    void updateLocation(Location location) { 
        currentLocation = location; 
        currentLatitude = currentLocation.getLatitude(); 
        currentLongitude = currentLocation.getLongitude(); 
    } 
 
    
    //Location from network provider	            
    public void FindLocationNet() { 
        LocationManager locationManager = (LocationManager) this 
                .getSystemService(Context.LOCATION_SERVICE); 
 
        LocationListener locationListener = new LocationListener() { 
            public void onLocationChanged(Location location) { 
                updateLocationNet(location);                  
                } 
 
            public void onStatusChanged(String provider, int status, Bundle extras) { 
            } 
 
            public void onProviderEnabled(String provider) { 
            } 
 
            public void onProviderDisabled(String provider) { 
            } 
        }; 
        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        //locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
    }
    
    void updateLocationNet(Location location1) { 
        currentLocationNet = location1; 
        currentLatitudeNet = currentLocationNet.getLatitude(); 
        currentLongitudeNet = currentLocationNet.getLongitude(); 
    } 	            


    
    
    
    public class menuAdapter extends BaseAdapter {
    	private Context mContext;

    	public menuAdapter(Context c) {
    		mContext = c;
    	}

    	public int getCount() {
    		return mThumbIds.length;
    	}

    	public Object getItem(int position) {
    		return null;
    	}

    	public long getItemId(int position) {
    		return 0;
    	}

    	// create a new ImageView for each item referenced by the Adapter
    	public View getView(int position, View convertView, ViewGroup parent) {
    		View MyView = convertView;
            if (convertView == null) { 
                LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
                MyView = li.inflate(R.layout.mainmenugriditem, null);

                // Add The Image!!!           
                ImageView iv = (ImageView)MyView.findViewById(R.id.album_image);
                iv.setImageResource(mThumbIds[position]);
                
                // Add The Text!!!
                TextView tv = (TextView)MyView.findViewById(R.id.image_name);
                tv.setTextSize(20);   
                tv.setText(desc[position] );
            }
            return MyView;
    	}

    	private String[] desc={			
    			"Birth",
    			"Death",
                "Registration List",
    			"Upload",
    			"Exit"};
    	
    	
    	//references to our images
    	private Integer[] mThumbIds = {
    			R.drawable.list,
				R.drawable.list,
                R.drawable.list,
    			R.drawable.sync,
				R.drawable.exit
    	};
    }   
    
    
    // Method to turn on GPS
    public void turnGPSOn(){
        try
        {
	        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
	
	        if(!provider.contains("gps")){ //if gps is disabled
	            final Intent poke = new Intent();
	            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
	            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
	            poke.setData(Uri.parse("3"));
	            sendBroadcast(poke);
	        }
        }
        catch (Exception e) {

        }
    }
    
    // Method to turn off the GPS
    public void turnGPSOff(){
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if(provider.contains("gps")){ //if gps is enabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }
    }

    // turning off the GPS if its in on state. to avoid the battery drain.
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        turnGPSOff();
    }

}

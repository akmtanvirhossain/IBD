package org.icddrb.ibd;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import Common.Connection;
import Common.Global;

/**
 * Created by user on 18/03/2015.
 */

public class VHWReports extends Activity {
    boolean netwoekAvailable = false;
    Location currentLocation;
    double currentLatitude, currentLongitude;
    Location currentLocationNet;
    double currentLatitudeNet, currentLongitudeNet;


    //Disabled Back/Home key
//--------------------------------------------------------------------------------------------------
    @Override
    public boolean onKeyDown(int iKeyCode, KeyEvent event) {
        if (iKeyCode == KeyEvent.KEYCODE_BACK || iKeyCode == KeyEvent.KEYCODE_HOME) {
            return false;
        } else {
            return true;
        }
    }

    //Top menu
//--------------------------------------------------------------------------------------------------
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mnuclose, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog.Builder adb = new AlertDialog.Builder(VHWReports.this);
        switch (item.getItemId()) {
            case R.id.menuClose:
                adb.setMessage("আপনি কি এই ফর্ম থেকে বের হতে চান?");
                adb.setNegativeButton("না", null);
                adb.setPositiveButton("হ্যাঁ", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();

                    }});
                adb.show();
                return true;
        }
        return false;
    }

    String VariableID;
    private int hour;
    private int minute;
    private int mDay;
    private int mMonth;
    private int mYear;
    static final int DATE_DIALOG = 1;
    static final int TIME_DIALOG = 2;

    Connection C;
    Global g;

    private boolean IsUserHA(String provtype)
    {
        if(provtype.equalsIgnoreCase("02"))
            return true;
        else
            return false;


    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.webreports);
            final WebView wv = (WebView)findViewById(R.id.webview);
            g = Global.getInstance();
            // Enable Javascript
            WebSettings webSettings = wv.getSettings();
            webSettings.setJavaScriptEnabled(true);

            wv.loadUrl("http://203.190.254.42/ibdweb/report_vhw.aspx?cluster="+ g.getClusterCode());

            // Force links and redirects to open in the WebView instead of in a browser
            wv.setWebViewClient(new WebViewClient());

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        } catch (Exception e) {
            Connection.MessageBox(VHWReports.this, e.getMessage());
            return;
        }
    }
}






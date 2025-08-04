package org.icddrb.ibd;


import static android.os.Build.VERSION.SDK_INT;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.*;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Objects;

import Common.Connection;
import Common.DataClass;
import Common.DataClassProperty;
import Common.Global;
import Common.UploadData;
import Common.UploadDataJSON;
import Utility.MySharedPreferences;

public class LoginActivity extends Activity{
    Connection C;
    Global g;
    boolean networkAvailable=false;
    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private ProgressDialog dialog;
    int count = 0;
    TextView lblStaffType;
    String   SystemUpdateDT="";
    private  String Password="";
    private String Cluster;
    MySharedPreferences sp;

    //------------------------------------24/10/20 SMS----------------------------------------------------------------------------------------
    public static final String SECURITY_TAG = "Security Permission";
    private static final int REQUEST_Code = 0;
    private static String[] PERMISSIONS_LIST = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.SEND_SMS,
            Manifest.permission.RECEIVE_SMS
    };

    private void checkPermission() {
        Log.e(SECURITY_TAG, "Checking Permission.");
        if (
                (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) &
                        (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) &
                        (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) &
                        (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        ) {
            Log.e(SECURITY_TAG, "Calling Requesting Permission!!!");
            requestPermission();
        } else {
            Log.e(SECURITY_TAG, "Your permission has already been granted.");

            Activity_Load();
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            Log.e(SECURITY_TAG, "Requesting Permission to User.");
            ActivityCompat.requestPermissions(this, PERMISSIONS_LIST, REQUEST_Code);
        } else {
            Log.e(SECURITY_TAG, "Requesting Permission Directly.");
            ActivityCompat.requestPermissions(this, PERMISSIONS_LIST, REQUEST_Code);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (grantResults.length == PERMISSIONS_LIST.length && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //********* Granted ********
//            Activity_Load();
        } else {
            //********* Not Granted ********
            ActivityCompat.requestPermissions(this, PERMISSIONS_LIST, REQUEST_Code);
        }
    }
    //----------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try
        {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.login);
            C = new Connection(this);
            g = Global.getInstance();
            sp = new MySharedPreferences();
            sp.save(this,"cluster","");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if(!Environment.isExternalStorageManager())
                {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    startActivityForResult(intent, 2296);
                }
            }
            checkPermission();


        }
        catch(Exception ex)
        {
            Connection.MessageBox(LoginActivity.this, ex.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //************** Please do not write any code before that
        if (requestCode == 2296) {
            if (SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    // perform action when allow permission success
                    checkPermission();
                } else {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    startActivityForResult(intent, 2296);
                }
            }
        }
    }

    private void Activity_Load()
    {

        final Spinner uid      = (Spinner)findViewById(R.id.userId);
        final Spinner weekNo   = (Spinner)findViewById(R.id.weekNo);
        final EditText pass    = (EditText)findViewById(R.id.pass);
        TextView lblSystemDate = (TextView)findViewById(R.id.lblSystemDate);

        //Need to update date every time whenever shared updated system
        //Format: DDMMYYYY
        //*********************************************************************
        //SystemUpdateDT = "22072025";
        SystemUpdateDT = "03082025";
        lblSystemDate.setText("Version:1.0, Built on: " + SystemUpdateDT + "(" + Global.Organization + ")");

        //Check for Internet connectivity
        if (Connection.haveNetworkConnection(LoginActivity.this)) {
            networkAvailable=true;
        } else {
            networkAvailable=false;
        }

        //Rebuild Database
        String TotalTab = C.ReturnSingleValue("SELECT count(*) FROM sqlite_master WHERE type = 'table' AND name != 'android_metadata' AND name != 'sqlite_sequence'");
        if(Integer.valueOf(TotalTab) == 0)
        {
            if(networkAvailable)
            {
                //Call Setting Form
                finish();
                Intent f1 = new Intent(getApplicationContext(),SettingForm.class);
                startActivity(f1);
                return;
            }
            else
            {
                Connection.MessageBox(LoginActivity.this,"Internet connection is not available for building initial database.");
                return;
            }
        }



        Cluster = C.ReturnSingleValue("select Cluster from Cluster");
        g.setClusterCode(Cluster);
        sp.save(this,"cluster",Cluster);

        try {
            C.CreateTable("process_tab", "Create table process_tab(process_id int)");
            String resp = "";

            if (!C.Existence("Select * from process_tab where process_id=6")) {

                C.CreateTable("data_GAge", "Create table data_GAge(ChildId varchar (18),Vill varchar (3),Bari varchar (4),HH varchar (2),SNo varchar (2),PNo varchar (10),GAge varchar (2),StartTime varchar (5),EndTime varchar (5),DeviceID varchar (10),EntryUser varchar (10),Lat varchar (20),Lon varchar (20),EnDt datetime,Upload int,modifyDate datetime,Constraint pk_data_GAge Primary Key(Vill,Bari,HH,SNo))");
                C.Save("Insert into process_tab(process_id)values(6)");
            }

        }catch (Exception ex){

        }

        if (networkAvailable)
        {
            Intent syncService = new Intent(this, Sync_Service.class);
            startService(syncService);
        }

        String SQLStr;
        String Res = "";
        String TableName="";
        String VariableList;
        String UniqueField;


        //need to update weekly visit data
        if(networkAvailable & !C.Existence("Select Week from weeklyvstdt where date('now') between date(stdate) and date(endate)"))
        {
            //WeeklyVstDt
            TableName = "WeeklyVstDt";
            SQLStr = "Select top 5 Week, (cast(YEAR(StDate) as varchar(4))+'-'+right('0'+ cast(MONTH(StDate) as varchar(2)),2)+'-'+right('0'+cast(DAY(StDate) as varchar(2)),2))StDate," +
                    "(cast(YEAR(EnDate) as varchar(4))+'-'+right('0'+ cast(MONTH(EnDate) as varchar(2)),2)+'-'+right('0'+cast(DAY(EnDate) as varchar(2)),2))EnDate" +
                    " from WeeklyVstDt where week <=(select week from WeeklyVstDt where GETDATE() between cast(stdate as datetime) and cast(endate as datetime)) order by CAST(week as numeric(8)) desc";
            VariableList = "Week, StDate, EnDate";
            Res = C.DownloadJSON(SQLStr, "WeeklyVstDt", "Week, StDate, EnDate", "Week");
        }

        uid.setAdapter(C.getArrayAdapter("Select l.UserId||'-'||v.VHWNAME from Login l,VHWs v where l.userid=cast(v.vhw as varchar(10))"));
        weekNo.setAdapter(C.getArrayAdapter("Select week from WeeklyVstDt order by week desc limit 300"));
        String Week  = C.ReturnSingleValue("Select Week from weeklyvstdt where date('now') between date(stdate) and date(endate)");
        weekNo.setSelection(Global.SpinnerItemPosition(weekNo,Week.length(),Week));

        //***********************************************
        //netwoekAvailable=false;
        //***********************************************

        Button btnClose=(Button)findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
                System.exit(0);
            }
        });

        //Login -----------------------------------------------------------------------
        Button loginButton = (Button) findViewById(R.id.btnLogin);
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try
                {
                    String[] WeekDate  = Connection.split(C.ReturnSingleValue("Select (stdate||','||endate) DT from weeklyvstdt where week='"+ weekNo.getSelectedItem().toString()  +"'"),',');
                    g.setWeekStartDate(WeekDate[0].toString());
                    g.setWeekEndDate(WeekDate[1].toString());

                    String[] U = Connection.split(uid.getSelectedItem().toString(),'-');
                    g.setUserId(U[0]);

                    //Download Updated System
                    //...................................................................................
                    Bundle IDbundle = new Bundle();
                    if(networkAvailable==true)
                    {
                        if(C.Existence("Select * from vhws where BariChar is null or length(barichar)=0"))
                        {
                            String SQLStr = "Select Cluster,VHW,BariChar from VHWs where VHW = '"+ g.getUserId() +"'";
                            String VariableList = "Cluster,VHW,BariChar";
                            String Res = C.DownloadJSON(SQLStr, "vhws", VariableList, "Cluster,VHW");
                        }

                        //Retrieve data from server for checking local device
                        String server_val = C.ReturnResult("ReturnSingleValue","sp_ServerCheck '"+ g.getClusterCode() +"'");
                        if(server_val!=null) {
                            String[] ServerVal = Connection.split(server_val, ',');
                            String ServerDate = ServerVal[0].toString();
                            String UpdateDT = ServerVal[1].toString();

                            //Check for New Version
                            if (!UpdateDT.equals(SystemUpdateDT)) {
                                systemDownload d = new systemDownload();
                                d.setContext(getApplicationContext());
                                d.execute(Global.UpdatedSystem);
                            } else {
                                //check for system date
                                if (ServerDate.equals(Global.TodaysDateforCheck()) == false) {
                                    Connection.MessageBox(LoginActivity.this, "আপনার ট্যাব এর তারিখ সঠিক নয় [" + Global.DateNowDMY() + "]।");
                                    startActivity(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS));
                                    return;
                                }

                                finish();
                                IDbundle.putString("weekno", weekNo.getSelectedItem().toString());
                                Intent f1 = new Intent(getApplicationContext(), HouseholdIndex.class);
                                f1.putExtras(IDbundle);
                                startActivity(f1);
                            }
                        }else{
                            finish();
                            IDbundle.putString("weekno", weekNo.getSelectedItem().toString());
                            Intent f1 = new Intent(getApplicationContext(), HouseholdIndex.class);
                            f1.putExtras(IDbundle);
                            startActivity(f1);
                        }
                    }
                    else
                    {
                        finish();
                        IDbundle.putString("weekno", weekNo.getSelectedItem().toString());
                        Intent f1 = new Intent(getApplicationContext(),HouseholdIndex.class);
                        f1.putExtras(IDbundle);
                        startActivity(f1);
                    }
                }
                catch(Exception ex)
                {
                    Connection.MessageBox(LoginActivity.this, ex.getMessage());
                    return;
                }
            }
        });
    }


    //Install application
    private void InstallApplication()
    {
        File apkfile = new File(Environment.getExternalStorageDirectory() + File.separator + Global.NewVersionName + ".apk");
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                Uri apkuri = FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()),
                        Objects.requireNonNull(getApplicationContext()).getPackageName() + ".provider", apkfile);

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // without this flag android returned a intent error!
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                intent.setDataAndType(apkuri, "application/vnd.android.package-archive");

                startActivity(intent);
            } else {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // without this flag android returned a intent error!
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                intent.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");

                startActivity(intent);
            }
        }catch (Exception ex){
            Connection.MessageBox(this,ex.getMessage());
            return;
        }
    }


    //Downloading updated system from the central server
    class systemDownload extends AsyncTask<String,String,Void>{
        private Context context;

        public void setContext(Context contextf){
            context = contextf;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(LoginActivity.this);
            dialog.setMessage("Downloading Updated System...");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
            dialog.show();
        }


        protected void onProgressUpdate(String... progress) {
            dialog.setProgress(Integer.parseInt(progress[0]));
            //publishProgress(progress);
        }

        //@Override
        protected void onPostExecute(String unused) {
            dialog.dismiss();
        }


        @Override
        protected Void doInBackground(String... arg0) {
            try {
                URL url = new URL(arg0[0]);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.connect();
                int lenghtOfFile = c.getContentLength();

                File file=Environment.getExternalStorageDirectory();

                file.mkdirs();
                File outputFile = new File(file.getAbsolutePath()+ File.separator + Global.NewVersionName +".apk");

                if(outputFile.exists()){
                    outputFile.delete();
                }
                else
                {
                    outputFile.createNewFile();
                }

                FileOutputStream fos = new FileOutputStream(outputFile);

                InputStream is = c.getInputStream();


                byte[] buffer = new byte[1024];
                int len1 = 0;
                long total = 0;
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);
                    count++;
                }
                fos.close();
                is.close();


                InstallApplication();

                dialog.dismiss();

            } catch (IOException e) {
                //Log.e("UpdateAPP", "Update error! " + e.getMessage());
            }
            return null;
        }
    }


}


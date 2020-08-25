package org.icddrb.ibd;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
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
    boolean netwoekAvailable=false;
    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private ProgressDialog dialog;
    int count = 0;
    TextView lblStaffType;
    String   SystemUpdateDT="";
    private  String Password="";
    private String Cluster;
    MySharedPreferences sp;

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

            final Spinner uid      = (Spinner)findViewById(R.id.userId);
            final Spinner weekNo   = (Spinner)findViewById(R.id.weekNo);
            final EditText pass    = (EditText)findViewById(R.id.pass);
            TextView lblSystemDate = (TextView)findViewById(R.id.lblSystemDate);

            //Need to update date every time whenever shared updated system
            //Format: DDMMYYYY
            //*********************************************************************
            SystemUpdateDT = "17082020";
            lblSystemDate.setText("Version:1.0, Built on: " + SystemUpdateDT + "(" + Global.Organization + ")");

            //Check for Internet connectivity
            if (Connection.haveNetworkConnection(LoginActivity.this)) {
                netwoekAvailable=true;
            } else {
                netwoekAvailable=false;
            }

            //Update data for Training Purpose
            //No data sync will work for VHW: 999
            //--------------------------------------------------------------------------------------
            //C.Save("Update Login set UserID='999',UserName='999',Pass='Test User'");
            //C.Save("Update vhws set cluster='999',vhw='999',vhwname='Test VHW'");

            //31 May 2016
            //C.Save("Update Child set Referral='' where LENGTH(Referral)>0 and cast(SUBSTR(Referral,13,3) as int)<=330");
            //C.Save("Update Child set Referral_add='' where LENGTH(Referral_add)>0 and cast(SUBSTR(Referral_add,13,3) as int)<=330");
            //C.Save("Update Child set Referral_foll='' where LENGTH(Referral_Foll)>0 and cast(SUBSTR(Referral_foll,13,3) as int)<=330");

            //Rebuild Database
            String TotalTab = C.ReturnSingleValue("SELECT count(*) FROM sqlite_master WHERE type = 'table' AND name != 'android_metadata' AND name != 'sqlite_sequence'");
            if(Integer.valueOf(TotalTab) == 0)
            {
                if(netwoekAvailable)
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
                    //finish();
                    //System.exit(0);
                    return;
                }
            }



            Cluster = C.ReturnSingleValue("select Cluster from Cluster");
            g.setClusterCode(Cluster);
            sp.save(this,"cluster",Cluster);

            if (netwoekAvailable)
            {
                Intent syncService = new Intent(this, Sync_Service.class);
                startService(syncService);
            }

            /*String TableName = "WeeklyVstDt";
            String SQLStr = "Select top 20 Week, (cast(YEAR(StDate) as varchar(4))+'-'+right('0'+ cast(MONTH(StDate) as varchar(2)),2)+'-'+right('0'+cast(DAY(StDate) as varchar(2)),2))StDate," +
                    "(cast(YEAR(EnDate) as varchar(4))+'-'+right('0'+ cast(MONTH(EnDate) as varchar(2)),2)+'-'+right('0'+cast(DAY(EnDate) as varchar(2)),2))EnDate" +
                    " from WeeklyVstDt order by CAST(week as numeric(8)) desc";
            String VariableList = "Week, StDate, EnDate";
            String Res = C.DownloadJSON(SQLStr, "WeeklyVstDt", "Week, StDate, EnDate","Week");
            */
            //C.Save("delete from WeeklyVstDt");

            String SQLStr;
            String Res = "";
            String TableName="";
            String VariableList;
            String UniqueField;

            //C.Save("Update child set extype='',exdate='' where childid='32602887188'");

            //download from 300 week
            if(netwoekAvailable & !C.Existence("Select Week from weeklyvstdt where Week=250")) {
                TableName = "WeeklyVstDt";
                SQLStr = "Select Week, (cast(YEAR(StDate) as varchar(4))+'-'+right('0'+ cast(MONTH(StDate) as varchar(2)),2)+'-'+right('0'+cast(DAY(StDate) as varchar(2)),2))StDate," +
                        "(cast(YEAR(EnDate) as varchar(4))+'-'+right('0'+ cast(MONTH(EnDate) as varchar(2)),2)+'-'+right('0'+cast(DAY(EnDate) as varchar(2)),2))EnDate" +
                        " from WeeklyVstDt where week >= 250";
                VariableList = "Week, StDate, EnDate";
                Res = C.DownloadJSON(SQLStr, "WeeklyVstDt", "Week, StDate, EnDate", "Week");
            }

            //need to update weeklu visit data
            if(netwoekAvailable & !C.Existence("Select Week from weeklyvstdt where date('now') between date(stdate) and date(endate)"))
            {
                //WeeklyVstDt
                TableName = "WeeklyVstDt";
                SQLStr = "Select top 5 Week, (cast(YEAR(StDate) as varchar(4))+'-'+right('0'+ cast(MONTH(StDate) as varchar(2)),2)+'-'+right('0'+cast(DAY(StDate) as varchar(2)),2))StDate," +
                        "(cast(YEAR(EnDate) as varchar(4))+'-'+right('0'+ cast(MONTH(EnDate) as varchar(2)),2)+'-'+right('0'+cast(DAY(EnDate) as varchar(2)),2))EnDate" +
                        " from WeeklyVstDt where week <=(select week from WeeklyVstDt where GETDATE() between cast(stdate as datetime) and cast(endate as datetime)) order by CAST(week as numeric(8)) desc";
                VariableList = "Week, StDate, EnDate";
                Res = C.DownloadJSON(SQLStr, "WeeklyVstDt", "Week, StDate, EnDate", "Week");

                //CodeList
                /*TableName = "Bari";
                SQLStr = "Select Vill, Bari, BariName, BariLoc, Cluster, Block from Bari Where Upload='3' and Cluster='"+ g.getClusterCode() +"'";
                VariableList = "Vill, Bari, BariName, BariLoc, Cluster, Block";
                Res = C.DownloadJSON_UpdateServer(SQLStr, TableName, VariableList, "Vill,Bari");
                */
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

                        /*
                        //Data Upload to Server (JSON Format)
                        //--------------------------------------------------------------------------
                        String TableName;
                        String VariableList;
                        String UniqueField;

                        TableName    = "vhws";
                        VariableList = "Cluster,vhw,vhwname";
                        UniqueField  = "vhw";

                        C.UploadJSON(TableName,VariableList,UniqueField);

                        //--------------------------------------------------------------------------
                        String Resp = C.DownloadJSON("Select Cluster,vhw,vhwname from VHWs",TableName,VariableList,UniqueField);
                        if(Resp.length()>0)
                        {
                            Connection.MessageBox(LoginActivity.this,Resp);
                            return;
                        }
                        */

                        /*
                        DataClass dt = new DataClass();
                        dt.settablename("vhw_temp");
                        dt.setcolumnlist("vhw,vhwname");
                        List<DataClassProperty> data = C.GetDataListJSON("vhw,vhwname", "vhws","vhw");
                        dt.setdata(data);

                        Gson gson = new Gson();
                        String json = gson.toJson(dt);
                        String response="";
                        UploadDataJSON u = new UploadDataJSON();
                        try{
                            response=u.execute(json).get();
                            if(response.equalsIgnoreCase("done"))
                            {
                                //Save("Update " + TableName + " Set Upload='1' where " + WhereClause);
                                //totalRec+=1;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        */

                        String[] U = Connection.split(uid.getSelectedItem().toString(),'-');
                        g.setUserId(U[0]);


                        //netwoekAvailable=false;

                        //Download Updated System
                        //...................................................................................
                        Bundle IDbundle = new Bundle();
                        if(netwoekAvailable==true)
                        {
                            if(C.Existence("Select * from vhws where BariChar is null or length(barichar)=0"))
                            {
                                String SQLStr = "Select Cluster,VHW,BariChar from VHWs where VHW = '"+ g.getUserId() +"'";
                                String VariableList = "Cluster,VHW,BariChar";
                                String Res = C.DownloadJSON(SQLStr, "vhws", VariableList, "Cluster,VHW");
                            }

                            //Retrieve data from server for checking local device
                            String[] ServerVal  = Connection.split(C.ReturnResult("ReturnSingleValue","sp_ServerCheck '"+ g.getClusterCode() +"'"),',');
                            String ServerDate            = ServerVal[0].toString();
                            String UpdateDT              = ServerVal[1].toString();

                            //Check for New Version
                            if (!UpdateDT.equals(SystemUpdateDT)) {
                                systemDownload d = new systemDownload();
                                d.setContext(getApplicationContext());
                                d.execute(Global.UpdatedSystem);
                            }
                            else
                            {
                                //check for system date
                                if(ServerDate.equals(Global.TodaysDateforCheck())==false)
                                {
                                    Connection.MessageBox(LoginActivity.this, "আপনার ট্যাব এর তারিখ সঠিক নয় ["+ Global.DateNowDMY() +"]।");
                                    startActivity(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS));
                                    return;
                                }

                                finish();
                                IDbundle.putString("weekno", weekNo.getSelectedItem().toString());
                                Intent f1 = new Intent(getApplicationContext(),HouseholdIndex.class);
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
        catch(Exception ex)
        {
            Connection.MessageBox(LoginActivity.this, ex.getMessage());
        }
    }

    //Install application
    private void InstallApplication()
    {
        File apkfile = new File(Environment.getExternalStorageDirectory() + File.separator + Global.NewVersionName +".apk");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // without this flag android returned a intent error!
        intent.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");

        startActivity(intent);
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


package org.icddrb.ibd;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Handler;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.os.SystemClock;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import Common.Connection;
import Common.Global;
import Common.GlobalTextConverter;

public class HouseholdIndex extends Activity {
    Connection C;
    Global g;
    boolean netwoekAvailable=false;
    ArrayList<HashMap<String, String>> mylistBari;
    ArrayList<HashMap<String, String>> mylistChild;
    ArrayList<HashMap<String, String>> evmylist = new ArrayList<HashMap<String, String>>();
    SimpleAdapter mScheduleBari;
    SimpleAdapter mScheduleChild;
    SimpleAdapter eList;
    String VariableID;
    static final int DATE_DIALOG = 1;
    private int mDay;
    private int mMonth;
    private int mYear;

    Bundle IDbundle;

    private static String CurrentVillage;
    private static String CurrentVCode;

    Location currentLocation;
    double currentLatitude, currentLongitude;

    Location currentLocationNet;
    double currentLatitudeNet, currentLongitudeNet;

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mnuexit, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        AlertDialog.Builder adb = new AlertDialog.Builder(HouseholdIndex.this);

        switch (item.getItemId()) {
            case R.id.menuMemberSearch:
                Intent f = new Intent(getApplicationContext(),MemSearch.class);
                startActivity(f);
                return true;

            case R.id.menuReport:
                Intent f1 = new Intent(getApplicationContext(),WebReports.class);
                startActivity(f1);
                return true;

            case R.id.menuVHWReport:
                Intent f2 = new Intent(getApplicationContext(),VHWReports.class);
                startActivity(f2);
                return true;

            case R.id.menuClusterBlock:
                if(g.getUserId().equals("999")) return true;

                //Check for Internet connectivity
                //*******************************************************************************
                if (!Connection.haveNetworkConnection(this)) {
                    Connection.MessageBox(HouseholdIndex.this, "ডাটা Sync করার জন্য Internet থাকা আবশ্যক।");
                    return true;
                }

                adb.setTitle("Data Sync");
                adb.setMessage("আপনি কি বাড়ী, ক্লাস্টার এবং ব্লক এর তথ্য Sync করতে চান[Yes/No]?");
                adb.setNegativeButton("No", null);
                adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        final ProgressDialog progDailog = ProgressDialog.show(
                                HouseholdIndex.this, "", "অপেক্ষা করুন ...", true);

                        new Thread() {
                            public void run() {
                                String ResponseString="Status:";
                                String response;
                                try {
                                    //Start
                                    //----------------------------------------------------------------------------------
                                    String SQLStr;
                                    String Res = "";
                                    String TableName;
                                    String VariableList;
                                    String UniqueField;


                                    //Status on Server
                                    //3-Update Block
                                    //4-Update Cluster and Block
                                    //----------------------------------------------------------------------------------
                                    //Upload update to server
                                    TableName     = "Bari";
                                    VariableList  = "Vill, Bari, BariName, BariLoc, Cluster, Block, Lat, Lon, EnDt, UserId, Upload";
                                    UniqueField   = "Vill, Bari";
                                    C.UploadJSON(TableName,VariableList,UniqueField);

                                    //Download Update from Server
                                    //3-Update Bari Information
                                    TableName = "Bari";
                                    SQLStr = "Select Vill,Bari,Cluster,Block,BariName,BariLoc from Bari where Cluster='"+ Cluster +"' and Upload='3'";
                                    VariableList = "Vill,Bari,Cluster,Block,BariName,BariLoc";
                                    Res = C.DownloadJSON_UpdateServer(SQLStr, TableName, VariableList, "Vill,Bari");

                                    //Remove Bari from Cluster (Local Database)
                                    //--------------------------------------------------------------------------------------
                                    TableName = "Bari";
                                    VariableList = "Vill, Bari, Cluster, Block";
                                    SQLStr = "Select Vill, Bari, Cluster, Block from BariRemove where Upload='1' and Cluster='"+ Cluster +"'";
                                    Res = C.DownloadJSON_BlockUpdate_UpdateServer(SQLStr, TableName, VariableList, "Vill, Bari");

                                    //4-Update Cluster and Block
                                    String ServerVal  = C.ReturnResult("ReturnSingleValue","Select Count(*)Total from Bari where Cluster='"+ Cluster +"' and Upload='4'");
                                    if(Integer.valueOf(ServerVal)>0) {
                                        //DSS Bari
                                        //--------------------------------------------------------------------------------------
                                        TableName = "DSSBari";
                                        VariableList = "Vill, Bari, BariName, BariLoc, Cluster, Block, Lat, Lon, EnDt, UserId";
                                        SQLStr  = "Select d.Vill, d.Bari, d.BariName, d.BariLoc, d.Cluster, d.Block, d.Lat, d.Lon, d.EnDt, d.UserId";
                                        SQLStr += " from DSSBari d,Bari b where d.Vill+d.Bari=b.vill+b.bari and b.Cluster='"+ Cluster +"' and b.Upload='4'";
                                        Res = C.DownloadJSON(SQLStr, TableName, VariableList, "Vill, Bari");

                                        //Visits
                                        //--------------------------------------------------------------------------------------
                                        TableName = "Visits";
                                        VariableList = "ChildId, PID, CID, Week, VDate, VStat, SickStatus, ExDate, RSVStatus, Lat, Lon, EnDt, UserId, Upload, UploadDT";
                                        SQLStr  = " select ChildId, PID, CID, Week, VDate, VStat, SickStatus, ExDate, RSVStatus, Lat, Lon, EnDt, UserId, Upload, UploadDT from";
                                        SQLStr += " (Select ChildId, PID, CID, Week, (cast(YEAR(VDate) as varchar(4))+'-'+right('0'+ cast(MONTH(VDate) as varchar(2)),2)+'-'+right('0'+cast(DAY(VDate) as varchar(2)),2)) VDate,";
                                        SQLStr += " VStat, SickStatus, (cast(YEAR(ExDate) as varchar(4))+'-'+right('0'+ cast(MONTH(ExDate) as varchar(2)),2)+'-'+right('0'+cast(DAY(ExDate) as varchar(2)),2)) ExDate,";
                                        SQLStr += " v.Lat, v.Lon, v.EnDt, v.UserId, v.Upload, v.UploadDT,rank() over (partition by childid order by week desc)total";
                                        SQLStr += " from Visits v, Bari b where left(v.ChildId,7)=b.Vill+b.Bari and b.Cluster='"+ Cluster +"' and b.Upload='4')a";
                                        SQLStr += " where total  between 1 and 5";
                                        Res = C.DownloadJSON(SQLStr, TableName, VariableList, "ChildId,Week");

                                        //Child
                                        //--------------------------------------------------------------------------------------
                                        SQLStr = "Select ChildId, c.Vill, c.bari, HH, SNo, PID, CID, Name, Sex, (cast(YEAR(BDate) as varchar(4))+'-'+right('0'+ cast(MONTH(BDate) as varchar(2)),2)+'-'+right('0'+cast(DAY(BDate) as varchar(2)),2))BDate,";
                                        SQLStr += "AgeM, MoNo, MoPNO, MoName, FaNo, FaPNO, FaName, EnType, (cast(YEAR(EnDate) as varchar(4))+'-'+right('0'+ cast(MONTH(EnDate) as varchar(2)),2)+'-'+right('0'+cast(DAY(EnDate) as varchar(2)),2))EnDate,";
                                        SQLStr += "ExType, (cast(YEAR(ExDate) as varchar(4))+'-'+right('0'+ cast(MONTH(ExDate) as varchar(2)),2)+'-'+right('0'+cast(DAY(ExDate) as varchar(2)),2))ExDate,";
                                        SQLStr += "(cast(YEAR(VStDate) as varchar(4))+'-'+right('0'+ cast(MONTH(VStDate) as varchar(2)),2)+'-'+right('0'+cast(DAY(VStDate) as varchar(2)),2))VStDate,VHW, VHWCluster, VHWBlock, Referral, c.EnDt, c.UserId, c.Upload, c.UploadDt";
                                        SQLStr += " from Child c,Bari b where c.Vill=b.Vill and c.Bari=b.Bari and b.Cluster='"+ Cluster +"' and b.upload='4'";

                                        TableName = "Child";
                                        VariableList = "ChildId, Vill, bari, HH, SNo, PID, CID, Name, Sex, BDate, AgeM, MoNo, MoPNO, MoName, FaNo, FaPNO, FaName, EnType, EnDate, ExType, ExDate, VStDate, VHW, VHWCluster, VHWBlock, Referral, EnDt, UserId, Upload, UploadDt";
                                        Res = C.DownloadJSON(SQLStr, TableName, VariableList, "ChildId");

                                        //MWRA
                                        //--------------------------------------------------------------------------------------
                                        SQLStr = "Select mwraId, c.Vill, c.bari, HH, SNo, PID, CID, Name, Sex, (cast(YEAR(BDate) as varchar(4))+'-'+right('0'+ cast(MONTH(BDate) as varchar(2)),2)+'-'+right('0'+cast(DAY(BDate) as varchar(2)),2))BDate,";
                                        SQLStr += "AgeM, MoNo, MoPNO, MoName, FaNo, FaPNO, FaName, EnType, (cast(YEAR(EnDate) as varchar(4))+'-'+right('0'+ cast(MONTH(EnDate) as varchar(2)),2)+'-'+right('0'+cast(DAY(EnDate) as varchar(2)),2))EnDate,";
                                        SQLStr += "ExType, (cast(YEAR(ExDate) as varchar(4))+'-'+right('0'+ cast(MONTH(ExDate) as varchar(2)),2)+'-'+right('0'+cast(DAY(ExDate) as varchar(2)),2))ExDate,c.PStat,c.LMPDt,c.EnDt";
                                        SQLStr += " from MWRA c,Bari b where c.Vill=b.Vill and c.Bari=b.Bari and b.Cluster='"+ Cluster +"' and b.Upload='4'";

                                        TableName = "MWRA";
                                        VariableList = "MwraId, Vill, bari, HH, SNo, PID, CID, Name, Sex, BDate, AgeM, MoNo, MoPNO, MoName, FaNo, FaPNO, FaName, EnType, EnDate, ExType, ExDate,PStat,LMPDt, EnDt";
                                        Res = C.DownloadJSON(SQLStr, TableName, VariableList, "MwraId");

                                        //AssNewBorn
                                        //--------------------------------------------------------------------------------------
                                        SQLStr = "select a.ChildId, a.CID, a.PID, Temp, Week, VType, Visit, (cast(YEAR(VDate) as varchar(4))+'-'+right('0'+ cast(MONTH(VDate) as varchar(2)),2)+'-'+right('0'+cast(DAY(VDate) as varchar(2)),2))VDate, Oth1, Oth2, Oth3, HNoCry, HNoBrea, HConv, HUncon, HDBrea, HJaund, HHFever, HLFever, HSkin, HFedp, HPus, HVomit, HWeak, HLeth, Asses, RR1, RR2, NoCry, Gasp, SBrea, BirthAs, Conv, RBrea, CInd, HFever, Hypo, UCon, Pus, UmbR, Weak, Leth, NoFed, Vsd, ConvH, Fonta, Vomit, H1Fever, LFever, NJaun, Pvsd, Jaund, SJaun, EyeP, Gono, Sick, Ref, RSlip, Comp, Reason, TPlace, TPlaceC, TAbsIn, TAbsDur, Hos, StartTime, EndTime, a.EnDt, a.UserId, a.Upload";
                                        SQLStr += " from AssNewBorn a";
                                        SQLStr += " inner join Child c on a.ChildId=c.ChildId";
                                        SQLStr += " inner join Bari b on c.Vill=b.Vill and c.bari=b.Bari";
                                        SQLStr += " where b.Cluster='"+ Cluster +"' and b.upload='4'";

                                        TableName = "AssNewBorn";
                                        VariableList = "ChildId, CID, PID, Temp, Week, VType, Visit, VDate, Oth1, Oth2, Oth3, HNoCry, HNoBrea, HConv, HUncon, HDBrea, HJaund, HHFever, HLFever, HSkin, HFedp, HPus, HVomit, HWeak, HLeth, Asses, RR1, RR2, NoCry, Gasp, SBrea, BirthAs, Conv, RBrea, CInd, HFever, Hypo, UCon, Pus, UmbR, Weak, Leth, NoFed, Vsd, ConvH, Fonta, Vomit, H1Fever, LFever, NJaun, Pvsd, Jaund, SJaun, EyeP, Gono, Sick, Ref, RSlip, Comp, Reason, TPlace, TPlaceC, TAbsIn, TAbsDur, Hos, StartTime, EndTime, EnDt, UserId, Upload";
                                        Res = C.DownloadJSON(SQLStr, TableName, VariableList, "ChildId, Week, Visit");

                                        //AssPneu
                                        //--------------------------------------------------------------------------------------
                                        SQLStr = "select a.ChildId, a.PID, a.CID, Week, (cast(YEAR(VDate) as varchar(4))+'-'+right('0'+ cast(MONTH(VDate) as varchar(2)),2)+'-'+right('0'+cast(DAY(VDate) as varchar(2)),2))VDate, VType, Visit, temp, Cough, ";
                                        SQLStr += " (cast(YEAR(CoughDt) as varchar(4))+'-'+right('0'+ cast(MONTH(CoughDt) as varchar(2)),2)+'-'+right('0'+cast(DAY(CoughDt) as varchar(2)),2))CoughDt, DBrea, ";
                                        SQLStr += " (cast(YEAR(DBreaDt) as varchar(4))+'-'+right('0'+ cast(MONTH(DBreaDt) as varchar(2)),2)+'-'+right('0'+cast(DAY(DBreaDt) as varchar(2)),2))DBreaDt, Fever, ";
                                        SQLStr += " (cast(YEAR(FeverDt) as varchar(4))+'-'+right('0'+ cast(MONTH(FeverDt) as varchar(2)),2)+'-'+right('0'+cast(DAY(FeverDt) as varchar(2)),2))FeverDt, OthCom1, OthCom2, OthCom3, Asses, RR1, RR2, Conv, FBrea, CInd, Leth, UCon, Drink, Vomit, None, LFever, MFever, HFever, Neck, Fonta, Conv2, Leth2, Ucon2, Drink2, Vomit2, CSPne, CPPne, CNPne, CLFever, CMFever, CHFever, CMenin, TSPne, TPPne, TNPne, TLFever, TMFever, THFever, TMenin, Ref, RSlip, Comp, Reason, TPlace, TPlaceC, TAbsIn, TAbsDur, Hos, a.EnDt, a.UserId, a.Upload,a.RRDk,a.tempDk";
                                        SQLStr += " from AssPneu a";
                                        SQLStr += " inner join Child c on a.ChildId=c.ChildId";
                                        SQLStr += " inner join Bari b on c.Vill=b.Vill and c.bari=b.Bari";
                                        SQLStr += " where b.Cluster='"+ Cluster +"' and b.upload='4'";

                                        TableName = "AssPneu";
                                        VariableList = "ChildId, PID, CID, Week, VDate, VType, Visit, temp, Cough, CoughDt, DBrea, DBreaDt, Fever, FeverDt, OthCom1, OthCom2, OthCom3, Asses, RR1, RR2, Conv, FBrea, CInd, Leth, UCon, Drink, Vomit, None, LFever, MFever, HFever, Neck, Fonta, Conv2, Leth2, Ucon2, Drink2, Vomit2, CSPne, CPPne, CNPne, CLFever, CMFever, CHFever, CMenin, TSPne, TPPne, TNPne, TLFever, TMFever, THFever, TMenin, Ref, RSlip, Comp, Reason, TPlace, TPlaceC, TAbsIn, TAbsDur, Hos, EnDt, UserId, Upload,RRDk,tempDk";
                                        Res = C.DownloadJSON(SQLStr, TableName, VariableList, "ChildId, Week, Visit");

                                        //NonComp
                                        //--------------------------------------------------------------------------------------
                                        SQLStr = " select a.ChildId, a.CID, a.PID, Week, VType, Visit, (cast(YEAR(VDate) as varchar(4))+'-'+right('0'+ cast(MONTH(VDate) as varchar(2)),2)+'-'+right('0'+cast(DAY(VDate) as varchar(2)),2)) VDate, RefResult, Q1a, Q1b, Q1c, Q1d, CausOth, VisitOthYN, Provider1, Provider2, Provider3, Provider4, ProviderOth1, Prescrip, RefA, RefB, RefC, RefD, RefE, RefF, RefG, RefH, RefI, RefX, RefOth, ServiceA, ServiceB, ServiceC, ServiceD, ServiceE, ServiceF, ServiceG, ServiceH, ServiceX, ServiceOth, StartTime, EndTime, a.UserId, a.EnDt, a.Upload";
                                        SQLStr += " from NonComp a";
                                        SQLStr += " inner join Child c on a.ChildId=c.ChildId";
                                        SQLStr += " inner join Bari b on c.Vill=b.Vill and c.bari=b.Bari";
                                        SQLStr += " where b.Cluster='"+ Cluster +"' and b.upload='4'";

                                        TableName = "NonComp";
                                        VariableList = "ChildId, CID, PID, Week, VType, Visit, VDate, RefResult, Q1a, Q1b, Q1c, Q1d, CausOth, VisitOthYN, Provider1, Provider2, Provider3, Provider4, ProviderOth1, Prescrip, RefA, RefB, RefC, RefD, RefE, RefF, RefG, RefH, RefI, RefX, RefOth, ServiceA, ServiceB, ServiceC, ServiceD, ServiceE, ServiceF, ServiceG, ServiceH, ServiceX, ServiceOth, StartTime, EndTime, UserId, EnDt, Upload";
                                        Res = C.DownloadJSON(SQLStr,TableName,VariableList,"ChildId, Week, Visit");
                                    }

                                    //Bari - finally change the status of Upload='4'
                                    //--------------------------------------------------------------------------------------
                                    TableName = "Bari";
                                    VariableList = "Vill, Bari, BariName, BariLoc, Cluster, Block, Lat, Lon, EnDt, UserId, Upload, UploadDT";
                                    SQLStr = "Select Vill, Bari, BariName, BariLoc, Cluster, Block, Lat, Lon, EnDt, UserId, Upload, UploadDT" +
                                            " from Bari where Cluster = '"+ Cluster +"' and Upload='4'";
                                    Res = C.DownloadJSON_UpdateServer(SQLStr,TableName,VariableList,"Vill, Bari");


                                    Connection.MessageBox(HouseholdIndex.this, "তথ্য ডাটাবেজ সার্ভারে সম্পূর্ণ ভাবে আপলোড হয়েছে। ");
                                    //End-----------------------------------------------------------------

                                } catch (Exception e) {

                                }
                                progDailog.dismiss();

                            }
                        }.start();

                    }
                });
                adb.show();

                return true;
            case R.id.menuSync:
                if(g.getUserId().equals("999")) return true;

                //Check for Internet connectivity
                //*******************************************************************************
                if (!Connection.haveNetworkConnection(this)) {
                    Connection.MessageBox(HouseholdIndex.this, "ডাটা Sync করার জন্য Internet থাকা আবশ্যক।");
                    return true;
                }

                adb.setTitle("Data Sync");
                adb.setMessage("আপনি কি সকল নতুন তথ্য Sync করতে চান[Yes/No]?");
                adb.setNegativeButton("No", null);
                adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        final ProgressDialog progDailog = ProgressDialog.show(
                                HouseholdIndex.this, "", "অপেক্ষা করুন ...", true);

                        new Thread() {
                            public void run() {
                                String ResponseString="Status:";
                                String response;
                                try {
                                    //Start --------------------------------------------------------------
                                    String SQLStr;
                                    String Res = "";
                                    String TableName;
                                    String VariableList;
                                    String UniqueField;

                                    TableName     = "MDSSVill";
                                    VariableList  = "Vill, Vname, UCode, UName, Cluster, Status, OldUnion";
                                    UniqueField   = "Vill";
                                    C.Sync_Download_Vill(TableName,VariableList,UniqueField,g.getClusterCode());

                                    //CID Update(CID_Update_Log)
                                    TableName     = "CID_Update_Log";
                                    VariableList  = "ChildId, NewCID, OldCID, ChangeType, UserId, UpdateDT, Status, Upload";
                                    UniqueField   = "ChildId, NewCID, OldCID";
                                    C.UploadJSON(TableName,VariableList,UniqueField);

                                    //Download update from Server
                                    //3-Update Bari Information
                                    /*TableName = "Bari";
                                    SQLStr = "Select Vill,Bari,Cluster,Block,BariName,BariLoc from Bari where Cluster='"+ Cluster +"' and Upload='3'";
                                    VariableList = "Vill,Bari,Cluster,Block,BariName,BariLoc";
                                    Res = C.DownloadJSON_UpdateServer(SQLStr, TableName, VariableList, "Vill,Bari");*/
                                    C.Sync_Download_Bari(Cluster);

                                    //Upload
                                    //----------------------------------------------------------------------------------
                                    C.ExecuteCommandOnServer("Insert into UploadMonitor(Cluster)Values('"+ g.getClusterCode() +"')");

                                    //Bari(New/Old-Block Update)
                                    TableName     = "Bari";
                                    VariableList  = "Vill, Bari, BariName, BariLoc, Cluster, Block, Lat, Lon, EnDt, UserId, Upload";
                                    UniqueField   = "Vill, Bari";

                                    C.UploadJSON(TableName,VariableList,UniqueField);

                                    //Child
                                    TableName     = "Child";
                                    VariableList  = "ChildId, Vill, bari, HH, SNo, PID, CID, Name, Sex, BDate, AgeM, MoNo, MoPNO, MoName, FaNo, FaPNO, FaName, EnType, EnDate, ExType, ExDate, VStDate, VHW, VHWCluster, VHWBlock, Referral,Referral_Add,Referral_Foll,Absent_Sick,ContactNo, EnDt, UserId, Upload";
                                    UniqueField   = "ChildId";

                                    C.UploadJSON(TableName,VariableList,UniqueField);

                                    //Visits
                                    TableName     = "Visits";
                                    VariableList  = "ChildId, PID, CID, Week, VDate, VStat, SickStatus, ExDate, RSVStatus, Lat, Lon, EnDt, UserId, Upload";
                                    UniqueField   = "ChildId,Week";

                                    C.UploadJSON(TableName,VariableList,UniqueField);

                                    //AssNewBorn
                                    TableName     = "AssNewBorn";
                                    VariableList  = "ChildId, CID, PID, Temp, Week, VType, Visit, VDate, Oth1, Oth2, Oth3, HNoCry, HNoBrea, HConv, HUncon, HDBrea, HJaund, HHFever, HLFever, HSkin, HFedp, HPus, HVomit, HWeak, ";
                                    VariableList += "HLeth, Asses, RR1, RR2, NoCry, Gasp, SBrea, BirthAs, Conv, RBrea, CInd, HFever, Hypo, UCon, Pus, UmbR, Weak, Leth, NoFed, Vsd, ConvH, Fonta, Vomit, H1Fever, LFever, NJaun, Pvsd, Jaund, SJaun, EyeP, Gono, Sick, Ref, RSlip, Comp, Reason, TPlace, TPlaceC, TAbsIn, TAbsDur, Hos, StartTime, EndTime, EnDt, UserId, Upload";
                                    UniqueField   = "ChildId, Week, VType, Visit";

                                    C.UploadJSON(TableName,VariableList,UniqueField);

                                    //AssPneu
                                    TableName     = "AssPneu";
                                    VariableList  = "ChildId, PID, CID, Week, VDate, VType, Visit, temp, Cough, CoughDt, DBrea, DBreaDt, Fever, FeverDt, OthCom1, OthCom2, OthCom3, Asses, RR1, RR2, Conv, FBrea, CInd, Leth, UCon, Drink, Vomit, None, LFever, MFever, HFever, Neck, Fonta, Conv2, Leth2, Ucon2, Drink2, Vomit2, CSPne, CPPne, CNPne, CLFever, CMFever, CHFever, ";
                                    VariableList += "CMenin, TSPne, TPPne, TNPne, TLFever, TMFever, THFever, TMenin, Ref, RSlip, Comp, Reason, TPlace, TPlaceC, TAbsIn, TAbsDur, Hos, EnDt, UserId, Upload,RRDk,tempDk";
                                    UniqueField   = "ChildId, Week, VType, Visit";

                                    C.UploadJSON(TableName,VariableList,UniqueField);

                                    //NonComp
                                    TableName     = "NonComp";
                                    VariableList  = "ChildId, CID, PID, Week, VType, Visit, VDate, RefResult, Q1a, Q1b, Q1c, Q1d, CausOth, VisitOthYN, Provider1, Provider2, Provider3, Provider4, ProviderOth1, Prescrip, RefA, RefB, RefC, RefD, RefE, RefF, RefG, RefH, RefI, RefX, RefOth, ";
                                    VariableList += "ServiceA, ServiceB, ServiceC, ServiceD, ServiceE, ServiceF, ServiceG, ServiceH, ServiceX, ServiceOth, StartTime, EndTime, UserId, EnDt, Upload";
                                    UniqueField   = "ChildId, Week, VType, Visit";

                                    C.UploadJSON(TableName,VariableList,UniqueField);

                                    //CID Update(CID_Update_Log)
                                    TableName     = "CID_Update_Log";
                                    VariableList  = "ChildId, NewCID, OldCID, ChangeType, UserId, UpdateDT, Status, Upload";
                                    UniqueField   = "ChildId, NewCID, OldCID";

                                    C.UploadJSON(TableName,VariableList,UniqueField);


                                    TableName     = "RSV";
                                    VariableList  = "ChildID, CID, PID, Week, VDate, VType, Visit, SlNo, Temp, Cough, dtpCoughDt, DBrea, dtpDBreaDt, DeepCold, DeepColdDt, SoreThroat, SoreThroatDt, RSVsuitable, RSVlisted, RSVlistedDt, Reason, StartTime, EndTime, DeviceID, EntryUser, Lat, Lon, EnDt, Upload, modifyDate";
                                    UniqueField   = "ChildID, Week, VType, Visit";

                                    C.UploadJSON(TableName,VariableList,UniqueField);

                                    //Delete
                                    //-------------------------------------------------------------------
                                    //Child remove based on server data, Table: ChildRemove
                                    SQLStr = "select ChildId,c.Vill,c.Bari from ChildRemove c,Bari b where c.vill+c.bari=b.Vill+b.bari and b.Cluster='"+ Cluster +"' and c.Upload='1'";
                                    VariableList = "ChildId,Vill,Bari";
                                    Res = C.DownloadJSON_Delete_UpdateServer(SQLStr, "Child", "ChildRemove", VariableList, "ChildId,Vill,Bari");

                                    //AssPneu remove based on server data, Table: AssPneu_Audit
                                    SQLStr = "Select a.ChildId, Week, VType, Visit from AssPneu_Audit a inner join Child c on a.childid=c.childid inner join Bari b on c.vill+c.bari=b.vill+b.bari where b.cluster='"+ Cluster +"' and a.Upload='1'";
                                    VariableList = "ChildId, Week, VType, Visit";
                                    Res = C.DownloadJSON_Delete_UpdateServer(SQLStr, "AssPneu", "AssPneu_Audit", VariableList, "ChildId, Week, VType, Visit");

                                    //AssNewBorn remove based on server data, Table: AssNewBorn_Audit
                                    SQLStr = "Select a.ChildId, Week, VType, Visit from AssNewBorn_Audit a inner join Child c on a.childid=c.childid inner join Bari b on c.vill+c.bari=b.vill+b.bari where b.cluster='"+ Cluster +"' and a.Upload='1'";
                                    VariableList = "ChildId, Week, VType, Visit";
                                    Res = C.DownloadJSON_Delete_UpdateServer(SQLStr, "AssNewBorn", "AssNewBorn_Audit", VariableList, "ChildId, Week, VType, Visit");

                                    //Visits remove based on server data, Table: Visits_Audit
                                    SQLStr = "Select a.ChildId, a.Week, a.VDate from Visits_Audit a inner join Child c on a.childid=c.childid inner join Bari b on c.vill+c.bari=b.vill+b.bari where b.cluster='"+ Cluster +"' and a.Upload='1'";
                                    VariableList = "ChildId, Week, VDate";
                                    Res = C.DownloadJSON_Delete_UpdateServer(SQLStr, "Visits", "Visits_Audit", VariableList, "ChildId, Week, VDate");


                                    //Data Update on local device
                                    //-------------------------------------------------------------------

                                    //Child
                                    /*TableName = "Child";
                                    VariableList = "ChildId, Vill, bari, HH, SNo, PID, CID, Name, Sex, BDate, AgeM, MoNo, MoPNO, MoName, FaNo, FaPNO, FaName, EnType, EnDate, ExType, ExDate, VStDate, VHW, VHWCluster, VHWBlock, Referral, Referral_Add, Referral_Foll, ContactNo";
                                    SQLStr  = "select ChildId, C.Vill, C.bari, HH, SNo, PID, CID, Name, Sex, (cast(YEAR(BDate) as varchar(4))+'-'+right('0'+ cast(MONTH(BDate) as varchar(2)),2)+'-'+right('0'+cast(DAY(BDate) as varchar(2)),2))BDate, AgeM, MoNo, MoPNO, MoName, FaNo, FaPNO, FaName, EnType, ";
                                    SQLStr += " (cast(YEAR(EnDate) as varchar(4))+'-'+right('0'+ cast(MONTH(EnDate) as varchar(2)),2)+'-'+right('0'+cast(DAY(EnDate) as varchar(2)),2))EnDate, ExType, (case when len(ExType)=0 then null else (cast(YEAR(ExDate) as varchar(4))+'-'+right('0'+ cast(MONTH(ExDate) as varchar(2)),2)+'-'+right('0'+cast(DAY(ExDate) as varchar(2)),2))end)ExDate, ";
                                    SQLStr += " (cast(YEAR(VStDate) as varchar(4))+'-'+right('0'+ cast(MONTH(VStDate) as varchar(2)),2)+'-'+right('0'+cast(DAY(VStDate) as varchar(2)),2))VStDate, VHW, VHWCluster, VHWBlock, Referral, Referral_Add, Referral_Foll, ContactNo";
                                    SQLStr += " from Child c,Bari b where c.Vill+c.bari=b.Vill+b.Bari and b.Cluster='"+ Cluster +"' and c.Upload='3'";
                                    Res = C.DownloadJSON_UpdateServer(SQLStr,TableName,VariableList,"ChildId");
                                    */
                                    C.Sync_Download_Child(Cluster);

                                    //Visit
                                    TableName = "Visits";
                                    VariableList = "ChildId, PID, CID, Week, VDate, VStat, SickStatus, ExDate";
                                    SQLStr  = " select top 1000 v.ChildId, v.PID, v.CID, Week, (cast(YEAR(VDate) as varchar(4))+'-'+right('0'+ cast(MONTH(VDate) as varchar(2)),2)+'-'+right('0'+cast(DAY(VDate) as varchar(2)),2))VDate, VStat, SickStatus, v.ExDate";
                                    SQLStr += " from Visits v, Child c, Bari b where v.ChildId=c.ChildId and c.Vill+c.bari=b.Vill+b.bari";
                                    SQLStr += " and b.Cluster='"+ Cluster +"' and v.Upload='3' order by Week asc";
                                    Res = C.DownloadJSON_UpdateServer(SQLStr,TableName,VariableList,"ChildId, Week");

                                    //Assessment (0-28 days)
                                    TableName = "AssNewBorn";
                                    VariableList = "ChildId, CID, PID, Temp, Week, VType, Visit, VDate, Oth1, Oth2, Oth3, HNoCry, HNoBrea, HConv, HUncon, HDBrea, HJaund, HHFever, HLFever, HSkin, HFedp, HPus, HVomit, HWeak, HLeth, Asses, RR1, RR2, NoCry, Gasp, SBrea, BirthAs, Conv, RBrea, CInd, HFever, Hypo, UCon, Pus, UmbR, Weak, Leth, NoFed, Vsd, ConvH, Fonta, Vomit, H1Fever, LFever, NJaun, Pvsd, Jaund, SJaun, EyeP, Gono, Sick, Ref, RSlip, Comp, Reason, TPlace, TPlaceC, TAbsIn, TAbsDur, Hos, StartTime, EndTime, EnDt, UserId, Upload";

                                    /*VariableList = "ChildId, CID, PID, Temp, Week, VType, Visit, VDate, Oth1, Oth2, Oth3, HNoCry, HNoBrea, HConv, HUncon, HDBrea, HJaund, HHFever, HLFever, HSkin, HFedp, HPus, HVomit, HWeak, HLeth, Asses, RR1, RR2, NoCry, Gasp, SBrea, BirthAs, Conv, RBrea, CInd, HFever, Hypo, UCon, Pus, UmbR, Weak, Leth, NoFed, Vsd, ConvH, Fonta, Vomit, H1Fever, LFever, NJaun, Pvsd, Jaund, SJaun, EyeP, Gono, Sick, Ref, RSlip, Comp, Reason, TPlace, TPlaceC, TAbsIn, TAbsDur, Hos";
                                    SQLStr  = " Select a.ChildId, a.CID, a.PID, Temp, Week, VType, Visit, VDate, Oth1, Oth2, Oth3, HNoCry, HNoBrea, HConv, HUncon, HDBrea, HJaund, HHFever,";
                                    SQLStr += " HLFever, HSkin, HFedp, HPus, HVomit, HWeak, HLeth, Asses, RR1, RR2, NoCry, Gasp, SBrea, BirthAs, Conv, RBrea, CInd, HFever, Hypo, UCon, Pus,";
                                    SQLStr += " UmbR, Weak, Leth, NoFed, Vsd, ConvH, Fonta, Vomit, H1Fever, LFever, NJaun, Pvsd, Jaund, SJaun, EyeP, Gono, Sick, Ref, RSlip, Comp, Reason,";
                                    SQLStr += " TPlace, TPlaceC, TAbsIn, TAbsDur, Hos from AssNewBorn a,Child c,Bari b where a.ChildId=c.ChildId and c.Vill+c.bari=b.Vill+b.bari";
                                    SQLStr += " and b.Cluster='"+ Cluster +"' and a.Upload='3'";*/

                                    SQLStr = "select a.ChildId, a.CID, a.PID, Temp, Week, VType, Visit, (cast(YEAR(VDate) as varchar(4))+'-'+right('0'+ cast(MONTH(VDate) as varchar(2)),2)+'-'+right('0'+cast(DAY(VDate) as varchar(2)),2))VDate, Oth1, Oth2, Oth3, HNoCry, HNoBrea, HConv, HUncon, HDBrea, HJaund, HHFever, HLFever, HSkin, HFedp, HPus, HVomit, HWeak, HLeth, Asses, RR1, RR2, NoCry, Gasp, SBrea, BirthAs, Conv, RBrea, CInd, HFever, Hypo, UCon, Pus, UmbR, Weak, Leth, NoFed, Vsd, ConvH, Fonta, Vomit, H1Fever, LFever, NJaun, Pvsd, Jaund, SJaun, EyeP, Gono, Sick, Ref, RSlip, Comp, Reason, TPlace, TPlaceC, TAbsIn, TAbsDur, Hos, StartTime, EndTime, a.EnDt, a.UserId, '1' Upload";
                                    SQLStr += " from AssNewBorn a";
                                    SQLStr += " inner join Child c on a.ChildId=c.ChildId";
                                    SQLStr += " inner join Bari b on c.Vill=b.Vill and c.bari=b.Bari";
                                    SQLStr += " where b.Cluster='"+ Cluster +"' and a.upload='3'";

                                    Res = C.DownloadJSON_UpdateServer(SQLStr,TableName,VariableList,"ChildId, Week, VType, Visit");

                                    //Assessment (29-59 months)
                                    TableName = "AssPneu";
                                    VariableList = "ChildId, PID, CID, Week, VDate, VType, Visit, temp, Cough, CoughDt, DBrea, DBreaDt, Fever, FeverDt, OthCom1, OthCom2, OthCom3, Asses, RR1, RR2, Conv, FBrea, CInd, Leth, UCon, Drink, Vomit, None, LFever, MFever, HFever, Neck, Fonta, Conv2, Leth2, Ucon2, Drink2, Vomit2, CSPne, CPPne, CNPne, CLFever, CMFever, CHFever, CMenin, TSPne, TPPne, TNPne, TLFever, TMFever, THFever, TMenin, Ref, RSlip, Comp, Reason, TPlace, TPlaceC, TAbsIn, TAbsDur, Hos, EnDt, UserId, Upload,RRDk,tempDk";

                                    /*VariableList = "ChildId, PID, CID, Week, VDate, VType, Visit, temp, Cough, CoughDt, DBrea, DBreaDt, Fever, FeverDt, OthCom1, OthCom2, OthCom3, Asses, RR1, RR2, Conv, FBrea, CInd, Leth, UCon, Drink, Vomit, None, LFever, MFever, HFever, Neck, Fonta, Conv2, Leth2, Ucon2, Drink2, Vomit2, CSPne, CPPne, CNPne, CLFever, CMFever, CHFever, CMenin, TSPne, TPPne, TNPne, TLFever, TMFever, THFever, TMenin, Ref, RSlip, Comp, Reason, TPlace, TPlaceC, TAbsIn, TAbsDur, Hos";
                                    SQLStr  = " Select a.ChildId, a.PID, a.CID, Week, VDate, VType, Visit, temp, Cough, CoughDt, DBrea, DBreaDt, Fever, FeverDt, OthCom1, OthCom2, OthCom3,";
                                    SQLStr += " Asses, RR1, RR2, Conv, FBrea, CInd, Leth, UCon, Drink, Vomit, None, LFever, MFever, HFever, Neck, Fonta, Conv2, Leth2, Ucon2, Drink2, Vomit2,";
                                    SQLStr += " CSPne, CPPne, CNPne, CLFever, CMFever, CHFever, CMenin, TSPne, TPPne, TNPne, TLFever, TMFever, THFever, TMenin, Ref, RSlip, Comp, Reason,";
                                    SQLStr += " TPlace, TPlaceC, TAbsIn, TAbsDur, Hos from AssPneu a,Child c,Bari b where a.ChildId=c.ChildId and c.Vill+c.bari=b.Vill+b.bari";
                                    SQLStr += " and b.Cluster='"+ Cluster +"' and a.Upload='3'";*/

                                    SQLStr = "select top 1000 a.ChildId, a.PID, a.CID, Week, (cast(YEAR(VDate) as varchar(4))+'-'+right('0'+ cast(MONTH(VDate) as varchar(2)),2)+'-'+right('0'+cast(DAY(VDate) as varchar(2)),2))VDate, VType, Visit, temp, Cough, ";
                                    SQLStr += " (cast(YEAR(CoughDt) as varchar(4))+'-'+right('0'+ cast(MONTH(CoughDt) as varchar(2)),2)+'-'+right('0'+cast(DAY(CoughDt) as varchar(2)),2))CoughDt, DBrea, ";
                                    SQLStr += " (cast(YEAR(DBreaDt) as varchar(4))+'-'+right('0'+ cast(MONTH(DBreaDt) as varchar(2)),2)+'-'+right('0'+cast(DAY(DBreaDt) as varchar(2)),2))DBreaDt, Fever, ";
                                    SQLStr += " (cast(YEAR(FeverDt) as varchar(4))+'-'+right('0'+ cast(MONTH(FeverDt) as varchar(2)),2)+'-'+right('0'+cast(DAY(FeverDt) as varchar(2)),2))FeverDt, OthCom1, OthCom2, OthCom3, Asses, RR1, RR2, Conv, FBrea, CInd, Leth, UCon, Drink, Vomit, None, LFever, MFever, HFever, Neck, Fonta, Conv2, Leth2, Ucon2, Drink2, Vomit2, CSPne, CPPne, CNPne, CLFever, CMFever, CHFever, CMenin, TSPne, TPPne, TNPne, TLFever, TMFever, THFever, TMenin, Ref, RSlip, Comp, Reason, TPlace, TPlaceC, TAbsIn, TAbsDur, Hos, a.EnDt, a.UserId, '1' Upload,a.RRDk,a.tempDk";
                                    SQLStr += " from AssPneu a";
                                    SQLStr += " inner join Child c on a.ChildId=c.ChildId";
                                    SQLStr += " inner join Bari b on c.Vill=b.Vill and c.bari=b.Bari";
                                    SQLStr += " where b.Cluster='"+ Cluster +"' and a.upload='3'";

                                    Res = C.DownloadJSON_UpdateServer(SQLStr,TableName,VariableList,"ChildId, Week, VType, Visit");

                                    //Non-Complience
                                    TableName = "NonComp";
                                    VariableList = "ChildId, CID, PID, Week, VType, Visit, VDate, RefResult, Q1a, Q1b, Q1c, Q1d, CausOth, VisitOthYN, Provider1, Provider2, Provider3, Provider4, ProviderOth1, Prescrip, RefA, RefB, RefC, RefD, RefE, RefF, RefG, RefH, RefI, RefX, RefOth, ServiceA, ServiceB, ServiceC, ServiceD, ServiceE, ServiceF, ServiceG, ServiceH, ServiceX, ServiceOth, StartTime, EndTime, UserId, EnDt, Upload";

                                    /*VariableList = "ChildId, CID, PID, Week, VType, Visit, VDate, RefResult, Q1a, Q1b, Q1c, Q1d, CausOth, VisitOthYN, Provider1, Provider2, Provider3, Provider4, ProviderOth1, Prescrip, RefA, RefB, RefC, RefD, RefE, RefF, RefG, RefH, RefI, RefX, RefOth, ServiceA, ServiceB, ServiceC, ServiceD, ServiceE, ServiceF, ServiceG, ServiceH, ServiceX, ServiceOth";
                                    SQLStr  = " Select a.ChildId, a.CID, a.PID, Week, VType, Visit, VDate, RefResult, Q1a, Q1b, Q1c, Q1d, CausOth, VisitOthYN, Provider1, Provider2, Provider3,";
                                    SQLStr += " Provider4, ProviderOth1, Prescrip, RefA, RefB, RefC, RefD, RefE, RefF, RefG, RefH, RefI, RefX, RefOth, ServiceA, ServiceB, ServiceC, ServiceD,";
                                    SQLStr += " ServiceE, ServiceF, ServiceG, ServiceH, ServiceX, ServiceOth";
                                    SQLStr += " from NonComp a,Child c,Bari b where a.ChildId=c.ChildId and c.Vill+c.bari=b.Vill+b.bari";
                                    SQLStr += " and b.Cluster='"+ Cluster +"' and a.Upload='3'";*/


                                    SQLStr = " select a.ChildId, a.CID, a.PID, Week, VType, Visit, (cast(YEAR(VDate) as varchar(4))+'-'+right('0'+ cast(MONTH(VDate) as varchar(2)),2)+'-'+right('0'+cast(DAY(VDate) as varchar(2)),2)) VDate, RefResult, Q1a, Q1b, Q1c, Q1d, CausOth, VisitOthYN, Provider1, Provider2, Provider3, Provider4, ProviderOth1, Prescrip, RefA, RefB, RefC, RefD, RefE, RefF, RefG, RefH, RefI, RefX, RefOth, ServiceA, ServiceB, ServiceC, ServiceD, ServiceE, ServiceF, ServiceG, ServiceH, ServiceX, ServiceOth, StartTime, EndTime, a.UserId, a.EnDt, '1' Upload";
                                    SQLStr += " from NonComp a";
                                    SQLStr += " inner join Child c on a.ChildId=c.ChildId";
                                    SQLStr += " inner join Bari b on c.Vill=b.Vill and c.bari=b.Bari";
                                    SQLStr += " where b.Cluster='"+ Cluster +"' and a.upload='3'";
                                    Res = C.DownloadJSON_UpdateServer(SQLStr,TableName,VariableList,"ChildId, Week, VType, Visit");

                                    //Upload Database to Server : 09 Nov 2016
                                    C.DatabaseUploadZip(Cluster);

                                    Connection.MessageBox(HouseholdIndex.this, "তথ্য ডাটাবেজ সার্ভারে সম্পূর্ণ ভাবে আপলোড হয়েছে। ");
                                    //End-----------------------------------------------------------------

                                } catch (Exception e) {

                                }
                                progDailog.dismiss();

                            }
                        }.start();

                    }
                });
                adb.show();




                return true;

            case R.id.menuMasterData:
                //Check for Internet connectivity
                //*******************************************************************************
                if (!Connection.haveNetworkConnection(this)) {
                    Connection.MessageBox(HouseholdIndex.this, "ডাটা Sync করার জন্য Internet থাকা আবশ্যক।");
                    return true;
                }

                adb.setTitle("Data Sync");
                adb.setMessage("আপনি কি Data Sync করতে চান[Yes/No]?");
                adb.setNegativeButton("No", null);
                adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        final ProgressDialog progDailog = ProgressDialog.show(
                                HouseholdIndex.this, "", "অপেক্ষা করুন ...", true);

                        new Thread() {
                            public void run() {
                                String ResponseString="Status:";
                                String response;
                                try {
                                    //Start --------------------------------------------------------------
                                    String SQLStr;
                                    String Res = "";
                                    String TableName;
                                    String VariableList;
                                    String UniqueField;

                                    //Download
                                    //----------------------------------------------------------------------------------

                                    //CodeList
                                    /*
                                    TableName = "CodeList";
                                    SQLStr = "select FName, VarName, VarCode, VarDes from CodeList";
                                    VariableList = "FName, VarName, VarCode, VarDes";
                                    Res = C.DownloadJSON(SQLStr, TableName, VariableList, "FName, VarName, VarCode");
                                    */

                                    //MigChild
                                    C.Save("Delete from MigChild");
                                    TableName = "MigChild";
                                    SQLStr = "select ChildId, Vill, bari, HH, SNo, PID, CID, Name, Sex, BDate, AgeM, MoNo, MoPNO, MoName, FaNo, FaPNO, FaName, EnType, " +
                                            "EnDate, ExType, ExDate from MigChild";
                                    VariableList = "ChildId, Vill, bari, HH, SNo, PID, CID, Name, Sex, BDate, AgeM, MoNo, MoPNO, MoName, FaNo, FaPNO, FaName, EnType, EnDate, ExType, ExDate";
                                    Res = C.DownloadJSON_InsertOnly(SQLStr, TableName, VariableList, "ChildId");

                                    //End-----------------------------------------------------------------


                                } catch (Exception e) {

                                }
                                progDailog.dismiss();

                            }
                        }.start();
                    }
                });
                adb.show();
                return true;

            case R.id.menuExit:
                adb.setTitle("Exit");
                adb.setMessage("আপনি কি সিস্টেম থেকে বের হতে চান[Yes/No]?");
                adb.setNegativeButton("No", null);
                adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        System.exit(0);
                    }
                });
                adb.show();
                return true;

            //Household not visited due to some reason
            case R.id.menuNotVisited:
                ShowVisitMissingForm(Cluster,lblBlock.getText().toString(),lblWeek.getText().toString());
                return true;

            case R.id.menuOutSideArea:
                //Check for Internet connectivity
                //*******************************************************************************
                /*
                if (!Connection.haveNetworkConnection(this)) {
                    Connection.MessageBox(HouseholdIndex.this, "অন্য এলাকার শিশু Assess করার জন্য Internet থাকা আবশ্যক।");
                    return true;
                }

                AssessmentForm_Outside(lblBlock.getText().toString());
                */
                Bundle IDbundle = new Bundle();
                IDbundle.putString("weekno", WeekNo);

                Intent f3 = new Intent(getApplicationContext(),HouseholdIndex_Outside.class);
                f3.putExtras(IDbundle);
                startActivity(f3);
                return true;

            //EDD List Sync from DSS Database
            case R.id.menuSyncEDDList:
                //Check for Internet connectivity
                //*******************************************************************************
                if (!Connection.haveNetworkConnection(this)) {
                    Connection.MessageBox(HouseholdIndex.this, "EDD List Sync করার জন্য Internet থাকা আবশ্যক।");
                    return true;
                }

                adb.setTitle("EDD List");
                adb.setMessage("আপনি কি EDD লিস্টSync করতে চান[Yes/No]?");
                adb.setNegativeButton("No", null);
                adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        final ProgressDialog progDailog = ProgressDialog.show(
                                HouseholdIndex.this, "", "অপেক্ষা করুন ...", true);

                        new Thread() {
                            public void run() {
                                String ResponseString="Status:";
                                String response;
                                try {
                                    //Start --------------------------------------------------------------
                                    String SQLStr;
                                    String Res = "";
                                    String TableName;
                                    String VariableList;
                                    String UniqueField;

                                    SQLStr = "Select mwraId, c.Vill, c.bari, HH, SNo, PID, CID, Name, Sex, (cast(YEAR(BDate) as varchar(4))+'-'+right('0'+ cast(MONTH(BDate) as varchar(2)),2)+'-'+right('0'+cast(DAY(BDate) as varchar(2)),2))BDate,";
                                    SQLStr += "AgeM, MoNo, MoPNO, MoName, FaNo, FaPNO, FaName, EnType, (cast(YEAR(EnDate) as varchar(4))+'-'+right('0'+ cast(MONTH(EnDate) as varchar(2)),2)+'-'+right('0'+cast(DAY(EnDate) as varchar(2)),2))EnDate,";
                                    SQLStr += "ExType, (cast(YEAR(ExDate) as varchar(4))+'-'+right('0'+ cast(MONTH(ExDate) as varchar(2)),2)+'-'+right('0'+cast(DAY(ExDate) as varchar(2)),2))ExDate,c.PStat,c.LMPDt,c.EnDt";
                                    SQLStr += " from MWRA c,Bari b where c.Vill=b.Vill and c.Bari=b.Bari and b.Cluster='"+ Cluster +"'";

                                    TableName = "MWRA";
                                    VariableList = "MwraId, Vill, bari, HH, SNo, PID, CID, Name, Sex, BDate, AgeM, MoNo, MoPNO, MoName, FaNo, FaPNO, FaName, EnType, EnDate, ExType, ExDate,PStat,LMPDt, EnDt";
                                    Res = C.DownloadJSON(SQLStr, TableName, VariableList, "MwraId");

                                    //End-----------------------------------------------------------------

                                } catch (Exception e) {

                                }
                                progDailog.dismiss();

                            }
                        }.start();
                    }
                });
                adb.show();

                return true;

            case R.id.menuSyncDSSBari:
                //Check for Internet connectivity
                //*******************************************************************************
                if (!Connection.haveNetworkConnection(this)) {
                    Connection.MessageBox(HouseholdIndex.this, "ডাটা Sync করার জন্য Internet থাকা আবশ্যক।");
                    return true;
                }

                adb.setTitle("Bari List");
                adb.setMessage("আপনি কি বাড়ী লিস্টSync করতে চান[Yes/No]?");
                adb.setNegativeButton("No", null);
                adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        final ProgressDialog progDailog = ProgressDialog.show(
                                HouseholdIndex.this, "", "অপেক্ষা করুন ...", true);

                        new Thread() {
                            public void run() {
                                String ResponseString="Status:";
                                String response;
                                try {
                                    //Start --------------------------------------------------------------
                                    String SQLStr;
                                    String Res = "";
                                    String TableName;
                                    String VariableList;

                                    TableName = "DSSBari";
                                    VariableList = "Vill, Bari, BariName, BariLoc, Cluster, Block, Lat, Lon, EnDt, UserId";
                                    SQLStr  = "Select d.Vill, d.Bari, d.BariName, d.BariLoc, d.Cluster, d.Block, d.Lat, d.Lon, d.EnDt, d.UserId";
                                    SQLStr += " from DSSBari d,Bari b where d.Vill+d.Bari=b.vill+b.bari and b.Cluster='"+ Cluster +"'";
                                    Res = C.DownloadJSON(SQLStr, TableName, VariableList, "Vill, Bari");
                                    //End-----------------------------------------------------------------

                                } catch (Exception e) {

                                }
                                progDailog.dismiss();

                            }
                        }.start();
                    }
                });
                adb.show();

                return true;
        }
        return false;
    }


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

    ListView list;
    Button cmdBlock1;
    Button cmdBlock2;
    Button cmdBlock3;
    Button cmdBlock4;
    Button cmdBlock5;
    String Cluster;
    String Block;
    TextView lblBlock;
    TextView lblWeek;
    TextView lblPageHeading;
    Button cmdBackToBariList;
    Spinner VillageList;
    Spinner BariList;
    String WeekNo;

    Button cmdRefresh;
    Button btnInMigration;
    Spinner spnFilterOption;
    EditText dtpVDate;
    CheckBox chkMWRA;

    //visit data form
    ImageButton btnVVDate;
    EditText dtpVVDate;
    TextView Age;
    TextView txtDOB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.householdindex);
        try {

            C = new Connection(this);
            g = Global.getInstance();
            setTitle("[ Cluster: " + g.getClusterCode() + " ]");

            turnGPSOn();
            FindLocation();

            //C.Save("update Visits set cid=replace(cid,'-','') where Week=332 and LENgth(cid)<>11");

            chkMWRA = (CheckBox)findViewById(R.id.chkMWRA);
            cmdBlock1 = (Button) findViewById(R.id.cmdBlock1);
            cmdBlock2 = (Button) findViewById(R.id.cmdBlock2);
            cmdBlock3 = (Button) findViewById(R.id.cmdBlock3);
            cmdBlock4 = (Button) findViewById(R.id.cmdBlock4);
            cmdBlock5 = (Button) findViewById(R.id.cmdBlock5);
            lblBlock = (TextView) findViewById(R.id.lblBlock);
            lblWeek = (TextView) findViewById(R.id.lblWeek);
            lblPageHeading = (TextView) findViewById(R.id.lblPageHeading);
            cmdBackToBariList = (Button) findViewById(R.id.cmdBackToBariList);

            Cluster = g.getClusterCode();
            Block   = "1"; //default block 1

            //WeekNo  = "100";
            Bundle B = new Bundle();
            B	     = getIntent().getExtras();
            WeekNo = B.getString("weekno");

            //WeekNo  = C.ReturnSingleValue("select Week from weeklyvstdt where date('now') between date(stdate) and date(endate)");
            if(WeekNo.length()==0)
            {
                Connection.MessageBox(HouseholdIndex.this,"Weekly visit schedule information is not available in database.");
                return;
            }
            lblWeek.setText(WeekNo);
            lblPageHeading.setText("  Bari List");


            VillageList = (Spinner)findViewById(R.id.VillageList);
            //VillageList.setAdapter(C.getArrayAdapter("select distinct ifnull(b.vill,'')||', '||ifnull(v.vname,'') from bari b,mdssvill v where b.vill=v.vill and b.cluster='" + Cluster + "' and b.block='" + Block + "'"));

            list = (ListView) findViewById(R.id.listHHIndex);

            BariList = (Spinner) findViewById(R.id.BariList);
            //BariList.setAdapter(C.getArrayAdapter("Select ' All Bari' union select trim(ifnull(bari,''))||', '||trim(ifnull(bariname,'')) from bari where cluster='" + Cluster + "' and block='" + Block + "'"));


            //BlockWiseVillageBari(Block);

            cmdBlock1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    BlockWiseVillageBari("1");
                }
            });
            cmdBlock2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    BlockWiseVillageBari("2");
                }
            });
            cmdBlock3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    BlockWiseVillageBari("3");
                }
            });
            cmdBlock4.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    BlockWiseVillageBari("4");
                }
            });
            cmdBlock5.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    BlockWiseVillageBari("5");
                }
            });


            Button cmdNewBari = (Button) findViewById(R.id.cmdNewBari);
            cmdNewBari.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    String CurrentBariNo = "";
                    String VillCode = Global.Left(VillageList.getSelectedItem().toString(), 3);


                    String BariChar   = C.ReturnSingleValue("select ifnull(BariChar,'') from Vhws where active='1' and VHW='" + g.getUserId() + "' limit 1");
                    String SQL = "select substr('000'||cast(ifnull(max(substr(Bari,2,4)),0)+1 as text),length('000'||cast(ifnull(max(substr(Bari,2,4)),0)+1 as text))-2,3)BariSl  from Bari";
                    SQL += " where vill='"+ VillCode +"' and substr(bari,1,1)='"+ BariChar +"' order by substr(Bari,2,3) desc limit 1";
                    String LastBariNo = C.ReturnSingleValue(SQL);
                    CurrentBariNo = BariChar + LastBariNo;

                    ShowBariForm(VillCode, VillageList.getSelectedItem().toString(), CurrentBariNo, "s");
                }
            });

            Button cmdBariUpdate = (Button) findViewById(R.id.cmdBariUpdate);
            cmdBariUpdate.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    //if (BariList.getSelectedItemPosition() == 0) return;
                    if (BariList.getSelectedItem().toString().trim().equalsIgnoreCase("all bari")) {
                        Connection.MessageBox(HouseholdIndex.this,"বাড়ীর তালিকা থেকে সঠিক বাড়ী সিলেক্ট করুন.");
                        return;
                    }

                    String VillCode = Global.Left(VillageList.getSelectedItem().toString(),3);
                    String CurrentBariNo = Global.Left(BariList.getSelectedItem().toString(), 4);
                    ShowBariForm(VillCode, VillageList.getSelectedItem().toString(), CurrentBariNo, "u");
                }
            });

            btnInMigration = (Button) findViewById(R.id.btnInMigration);
            btnInMigration.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    String CurrentBariNo = "";
                    if (BariList.getSelectedItemPosition() == 0) {
                        Connection.MessageBox(HouseholdIndex.this,"বাড়ীর তালিকা থেকে সঠিক বাড়ী সিলেক্ট করুন.");
                        return;
                    }
                    else {
                        String[] B = BariList.getSelectedItem().toString().split(",");
                        CurrentBariNo = B[0];
                    }

                    Bundle IDbundle = new Bundle();
                    IDbundle.putString("childid", "");
                    IDbundle.putString("mothercid", "");
                    IDbundle.putString("motherpid", "");
                    IDbundle.putString("mother", "");
                    IDbundle.putString("father", "");

                    IDbundle.putString("vill", Global.Left(VillageList.getSelectedItem().toString(), 3));
                    IDbundle.putString("bari", CurrentBariNo);
                    IDbundle.putString("hh", "");
                    IDbundle.putString("status", "new");
                    IDbundle.putString("Cluster", Cluster);
                    IDbundle.putString("Block", Block);
//                    IDbundle.putString("Union", UCode );
                    Intent f1 = new Intent(getApplicationContext(),ChildRegistration.class);
                    f1.putExtras(IDbundle);
                    startActivity(f1);
                }
            });


            chkMWRA.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    String CurrentBariNo = "";
                    if (BariList.getSelectedItemPosition() == 0)
                        CurrentBariNo = "%";
                    else {
                        String[] B = BariList.getSelectedItem().toString().split(",");
                        CurrentBariNo = B[0];
                    }


                    if (BariList.getSelectedItem().toString().trim().equalsIgnoreCase("all bari")) {
                        BariWiseChildList(Global.Left(VillageList.getSelectedItem().toString(), 3),"%",WeekNo);
                    } else {
                        BariWiseChildList(Global.Left(VillageList.getSelectedItem().toString(), 3),CurrentBariNo,WeekNo);
                    }
                }
            });

            cmdRefresh = (Button) findViewById(R.id.cmdRefresh);
            cmdRefresh.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    String CurrentBariNo = "";
                    if (BariList.getSelectedItemPosition() == 0)
                        CurrentBariNo = "%";
                    else {
                        String[] B = BariList.getSelectedItem().toString().split(",");
                        CurrentBariNo = B[0];
                    }


                    if (BariList.getSelectedItem().toString().trim().equalsIgnoreCase("all bari")) {
                        //g.setBariCode("");
                        /*if(cmdBackToBariList.isShown())
                            BariWiseChildList(Global.Left(VillageList.getSelectedItem().toString(), 3),"%",WeekNo);
                        else
                            VillageWiseBariList(false, Cluster, Block, Global.Left(VillageList.getSelectedItem().toString(), 3), "%");
                        */

                        BariWiseChildList(Global.Left(VillageList.getSelectedItem().toString(), 3),"%",WeekNo);
                    } else {
                        /*if(cmdBackToBariList.isShown())
                            BariWiseChildList(Global.Left(VillageList.getSelectedItem().toString(), 3),CurrentBariNo,WeekNo);
                        else
                            VillageWiseBariList(false, Cluster, Block, Global.Left(VillageList.getSelectedItem().toString(), 3), CurrentBariNo);
                        */

                        BariWiseChildList(Global.Left(VillageList.getSelectedItem().toString(), 3),CurrentBariNo,WeekNo);
                    }
                }
            });

            BariList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    String CurrentBariNo = "";
                    if (BariList.getSelectedItemPosition() == 0)
                        CurrentBariNo = "%";
                    else {
                        String[] B = BariList.getSelectedItem().toString().split(",");
                        CurrentBariNo = B[0];
                    }

                    if (BariList.getSelectedItem().toString().trim().equalsIgnoreCase("all bari")) {
                        //g.setBariCode("");
                        /*if(cmdBackToBariList.isShown())
                            BariWiseChildList(Global.Left(VillageList.getSelectedItem().toString(), 3),"%",WeekNo);
                        else
                            VillageWiseBariList(false, Cluster, Block, Global.Left(VillageList.getSelectedItem().toString(), 3), "%");
                        */

                        BariWiseChildList(Global.Left(VillageList.getSelectedItem().toString(), 3), "%", WeekNo);
                    } else {
                        /*if(cmdBackToBariList.isShown())
                            BariWiseChildList(Global.Left(VillageList.getSelectedItem().toString(), 3),CurrentBariNo,WeekNo);
                        else
                            VillageWiseBariList(false, Cluster, Block, Global.Left(VillageList.getSelectedItem().toString(), 3), CurrentBariNo);
                        */

                        BariWiseChildList(Global.Left(VillageList.getSelectedItem().toString(), 3), CurrentBariNo, WeekNo);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {

                }

            });

            spnFilterOption = (Spinner)findViewById(R.id.spnFilterOption);
            spnFilterOption.setAdapter(C.getArrayAdapter("Select '1-বর্তমানে সক্রিয়' union Select '2-সকল শিশু' union Select '3-ভিজিট সম্পন্ন হয়েছে' union Select '4-ভিজিট সম্পন্ন হয়নি' union Select '5-বয়স উত্তীর্ণ' union Select '6-নবজাতক' union Select '7-মৃত্যু বরণ' union Select '8-অসুস্থ্য শিশু' union Select '9-স্থানান্তর'"));
            spnFilterOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    String CurrentBariNo = "";
                    if (BariList.getSelectedItemPosition() == 0)
                        CurrentBariNo = "%";
                    else {
                        String[] B = BariList.getSelectedItem().toString().split(",");
                        CurrentBariNo = B[0];
                    }

                    BariWiseChildList(Global.Left(VillageList.getSelectedItem().toString(), 3),CurrentBariNo,WeekNo);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {

                }
            });


            cmdBackToBariList.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    VillageList.setEnabled(true);
                    BariList.setEnabled(true);
                    cmdBlock1.setEnabled(true);
                    cmdBlock2.setEnabled(true);
                    cmdBlock3.setEnabled(true);
                    cmdBlock4.setEnabled(true);
                    cmdBlock5.setEnabled(true);

                    cmdRefresh.setEnabled(false);
                    btnInMigration.setEnabled(false);

                    String CurrentBariNo = "";
                    if(BariList.getCount() > 0) {
                        if (BariList.getSelectedItemPosition() == 1)
                            CurrentBariNo = "%";
                        else if (BariList.getSelectedItemPosition() > 1)
                            CurrentBariNo = Global.Left(BariList.getSelectedItem().toString(), 4);

                        VillageWiseBariList(false, Cluster, Block, Global.Left(VillageList.getSelectedItem().toString(), 3), CurrentBariNo);
                        //lblPageHeading.setText("  Bari List");
                        cmdBackToBariList.setVisibility(View.GONE);
                    }
                }
            });


            VillageList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    //VillageWiseBariList(false,  Cluster, Block, Global.Left(VillageList.getSelectedItem().toString(), 3),"");
                    BariList.setAdapter(C.getArrayAdapter("Select ' All Bari' union select trim(ifnull(bari,''))||', '||trim(ifnull(bariname,'')) from bari where cluster='" + Cluster + "' and block='" + Block + "' and Vill='"+ Global.Left(VillageList.getSelectedItem().toString(), 3) +"'"));

                    String CurrentBariNo = "";
                    if (BariList.getSelectedItemPosition() == 0)
                        CurrentBariNo = "%";
                    else {
                        String[] B = BariList.getSelectedItem().toString().split(",");
                        CurrentBariNo = B[0];
                    }

                    if (BariList.getSelectedItem().toString().trim().equalsIgnoreCase("all bari")) {
                        //g.setBariCode("");
                        /*if(cmdBackToBariList.isShown())
                            BariWiseChildList(Global.Left(VillageList.getSelectedItem().toString(), 3),"%",WeekNo);
                        else
                            VillageWiseBariList(false, Cluster, Block, Global.Left(VillageList.getSelectedItem().toString(), 3), "%");
                        */

                        BariWiseChildList(Global.Left(VillageList.getSelectedItem().toString(), 3), "%", WeekNo);
                    } else {
                        /*if(cmdBackToBariList.isShown())
                            BariWiseChildList(Global.Left(VillageList.getSelectedItem().toString(), 3),CurrentBariNo,WeekNo);
                        else
                            VillageWiseBariList(false, Cluster, Block, Global.Left(VillageList.getSelectedItem().toString(), 3), CurrentBariNo);
                        */

                        BariWiseChildList(Global.Left(VillageList.getSelectedItem().toString(), 3), CurrentBariNo, WeekNo);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {

                }

            });

            String CurrentBariNo = "";
            if(BariList.getCount() > 0) {
                if (BariList.getSelectedItemPosition() == 0)
                    CurrentBariNo = "%";
                else
                    CurrentBariNo = Global.Left(BariList.getSelectedItem().toString(), 4);


                //VillageWiseBariList(false,  Cluster, Block, Global.Left(VillageList.getSelectedItem().toString(), 3), CurrentBariNo);
                //cmdBackToBariList.setVisibility(View.GONE);

                //09 12 2015
                //BariWiseChildList(Global.Left(VillageList.getSelectedItem().toString(), 3), CurrentBariNo,WeekNo);
                cmdBackToBariList.setVisibility(View.GONE);
            }



            BlockWiseVillageBari(Block);


        } catch (Exception ex) {
            Connection.MessageBox(HouseholdIndex.this, ex.getMessage());
            return;
        }

    }

    private void BlockWiseVillageBari(String BlockCode)
    {
        Block = BlockCode;
        lblBlock.setText(BlockCode);

        VillageList.setAdapter(C.getArrayAdapter("select distinct ifnull(b.vill,'')||', '||ifnull(v.vname,'') from bari b,mdssvill v where b.vill=v.vill and b.cluster='" + Cluster + "' and b.block='" + BlockCode + "'"));
        BariList.setAdapter(C.getArrayAdapter("Select ' All Bari' union select trim(ifnull(bari,''))||', '||trim(ifnull(bariname,'')) from bari where cluster='" + Cluster + "' and block='" + BlockCode + "' and Vill='"+ Global.Left(VillageList.getSelectedItem().toString(), 3) +"'"));


        if (BariList.getSelectedItem().toString().trim().equalsIgnoreCase("all bari")) {
            g.setBariCode("");
            BariWiseChildList(Global.Left(VillageList.getSelectedItem().toString(), 3), "%", WeekNo);

        } else {
            g.setBariCode(BariList.getSelectedItem().toString());
            BariWiseChildList(Global.Left(VillageList.getSelectedItem().toString(), 3), Global.Left(BariList.getSelectedItem().toString(), 4), WeekNo);
        }


        if(BlockCode.equals("1")) {
            cmdBlock1.setBackgroundColor(Color.GREEN);
            cmdBlock1.setTextColor(Color.BLACK);
            cmdBlock2.setBackgroundColor(Color.DKGRAY);
            cmdBlock2.setTextColor(Color.WHITE);
            cmdBlock3.setBackgroundColor(Color.DKGRAY);
            cmdBlock3.setTextColor(Color.WHITE);
            cmdBlock4.setBackgroundColor(Color.DKGRAY);
            cmdBlock4.setTextColor(Color.WHITE);
            cmdBlock5.setBackgroundColor(Color.DKGRAY);
            cmdBlock5.setTextColor(Color.WHITE);
        }
        else if(BlockCode.equals("2")) {
            cmdBlock1.setBackgroundColor(Color.DKGRAY);
            cmdBlock1.setTextColor(Color.WHITE);
            cmdBlock2.setBackgroundColor(Color.GREEN);
            cmdBlock2.setTextColor(Color.BLACK);
            cmdBlock3.setBackgroundColor(Color.DKGRAY);
            cmdBlock3.setTextColor(Color.WHITE);
            cmdBlock4.setBackgroundColor(Color.DKGRAY);
            cmdBlock4.setTextColor(Color.WHITE);
            cmdBlock5.setBackgroundColor(Color.DKGRAY);
            cmdBlock5.setTextColor(Color.WHITE);
        }
        else if(BlockCode.equals("3")) {
            cmdBlock1.setBackgroundColor(Color.DKGRAY);
            cmdBlock1.setTextColor(Color.WHITE);
            cmdBlock2.setBackgroundColor(Color.DKGRAY);
            cmdBlock2.setTextColor(Color.WHITE);
            cmdBlock3.setBackgroundColor(Color.GREEN);
            cmdBlock3.setTextColor(Color.BLACK);
            cmdBlock4.setBackgroundColor(Color.DKGRAY);
            cmdBlock4.setTextColor(Color.WHITE);
            cmdBlock5.setBackgroundColor(Color.DKGRAY);
            cmdBlock5.setTextColor(Color.WHITE);
        }
        else if(BlockCode.equals("4"))
        {
            cmdBlock1.setBackgroundColor(Color.DKGRAY);
            cmdBlock1.setTextColor(Color.WHITE);
            cmdBlock2.setBackgroundColor(Color.DKGRAY);
            cmdBlock2.setTextColor(Color.WHITE);
            cmdBlock3.setBackgroundColor(Color.DKGRAY);
            cmdBlock3.setTextColor(Color.WHITE);
            cmdBlock4.setBackgroundColor(Color.GREEN);
            cmdBlock4.setTextColor(Color.BLACK);
            cmdBlock5.setBackgroundColor(Color.DKGRAY);
            cmdBlock5.setTextColor(Color.WHITE);
            }
        else if(BlockCode.equals("5")) {
            cmdBlock1.setBackgroundColor(Color.DKGRAY);
            cmdBlock1.setTextColor(Color.WHITE);
            cmdBlock2.setBackgroundColor(Color.DKGRAY);
            cmdBlock2.setTextColor(Color.WHITE);
            cmdBlock3.setBackgroundColor(Color.DKGRAY);
            cmdBlock3.setTextColor(Color.WHITE);
            cmdBlock4.setBackgroundColor(Color.DKGRAY);
            cmdBlock4.setTextColor(Color.WHITE);
            cmdBlock5.setBackgroundColor(Color.GREEN);
            cmdBlock5.setTextColor(Color.BLACK);
        }
    }


    ListView listChild;
    public void BariWiseChildList(String Village, String BariCode, String WeekNo) {
        mScheduleBari = null;
        mScheduleChild = null;

        listChild = (ListView) findViewById(R.id.listHHIndex);
        listChild.setAdapter(null);
        mylistChild = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map;

        try {
            String BCode = "";
            String SQL   = "";

            String WeekEndDate = g.getWeekEndDate();

            //Active Child
            if(spnFilterOption.getSelectedItemPosition()==0)
            {
                if (BariCode.length() > 1) {
                    SQL = " select ifnull(db.cluster,'') dssc,ifnull(db.block,'') dssb, 'c' childmwra, c.BDate as bdate,ifnull(c.referral,'') as referral,ifnull(c.referral_add,'') as referral_add,ifnull(c.referral_foll,'') as referral_foll, ifnull(c.absent_sick,'') as absent_sick, b.Cluster cluster,b.Block block,c.ChildId childid,c.Vill vill,c.bari bari,c.HH hh,c.SNo sno,";
                    SQL += " c.PID pid,c.CID cid,Name name,Sex sex,cast(julianday(case when date('now')<date('"+ WeekEndDate +"') then date('now') else date('"+ WeekEndDate +"') end)-julianday(c.BDate) as int)aged, Cast((cast(julianday(case when date('now')<date('"+ WeekEndDate +"') then date('now') else date('"+ WeekEndDate +"') end)-julianday(c.BDate) as int)/30.44) as int) agem,ifnull(FaName,'') father,ifnull(MoName,'') mother,(case when v.ChildId is null then '2' else '1' end)visit,";
                    SQL += " (case when c.extype='4' then '' when length(c.extype)>0 and date(c.exdate)>=date(case when date(v.vdate)<'"+ WeekEndDate +"' then date(v.vdate) else '"+ WeekEndDate +"' end) then '' else ifnull(c.extype,'') end) extype,'' pstat,'' lmpdt";
                    SQL += " ,ifnull(c.contactno,'') contactno,ifnull(v.vstat,'')vstat,ifnull(v.sickstatus,'')sickstatus from Child c inner join Bari b on c.Vill=b.Vill and c.bari=b.Bari";
                    SQL += " left outer join DSSBari db on c.Vill=db.Vill and c.bari=db.Bari";
                    SQL += " left outer join (select * from visits where week='" + WeekNo + "' group by childid,week order by childid,min(vstat)) v on c.childid=v.childid and v.week='" + WeekNo + "'";
                    SQL += " where b.vill='"+ Village +"' ";
                    SQL += " and ((case when c.extype='4' then '' else c.extype end) is null or length(case when c.extype='4' or date(case when date(v.vdate)<'"+ WeekEndDate +"' then date(v.vdate) else '"+ WeekEndDate +"' end)<=date(c.exdate) then '' else ifnull(c.extype,'') end)=0)";
                    SQL += " and  b.Cluster='" + Cluster + "' and b.Block='" + Block + "' and b.bari like('" + BariCode + "')";
                    SQL += " and cast(julianday(case when date('now')<date('"+ WeekEndDate +"') then date('now') else date('"+ WeekEndDate +"') end)-julianday(c.BDate) as int) <= 1825";

                } else {
                    SQL = " select ifnull(db.cluster,'') dssc,ifnull(db.block,'') dssb, 'c' childmwra, c.BDate as bdate,ifnull(c.referral,'') as referral,ifnull(c.referral_add,'') as referral_add,ifnull(c.referral_foll,'') as referral_foll, ifnull(c.absent_sick,'') as absent_sick, b.Cluster cluster,b.Block block,c.ChildId childid,c.Vill vill,c.bari bari,c.HH hh,c.SNo sno,";
                    SQL += " c.PID pid,c.CID cid,Name name,Sex sex,cast(julianday(case when date('now')<date('"+ WeekEndDate +"') then date('now') else date('"+ WeekEndDate +"') end)-julianday(c.BDate) as int)aged, Cast((cast(julianday(case when date('now')<date('"+ WeekEndDate +"') then date('now') else date('"+ WeekEndDate +"') end)-julianday(c.BDate) as int)/30.44) as int) agem,ifnull(FaName,'') father,ifnull(MoName,'') mother,(case when v.ChildId is null then '2' else '1' end)visit,";
                    SQL += " (case when c.extype='4' then '' when length(c.extype)>0 and date(c.exdate)>=date(case when date(v.vdate)<'"+ WeekEndDate +"' then date(v.vdate) else '"+ WeekEndDate +"' end) then '' else ifnull(c.extype,'') end) extype,'' pstat,'' lmpdt";
                    SQL += " ,ifnull(c.contactno,'') contactno,ifnull(v.vstat,'')vstat,ifnull(v.sickstatus,'')sickstatus from Child c inner join Bari b on c.Vill=b.Vill and c.bari=b.Bari";
                    SQL += " left outer join DSSBari db on c.Vill=db.Vill and c.bari=db.Bari";
                    SQL += " left outer join (select * from visits where week='" + WeekNo + "' group by childid,week order by childid,min(vstat)) v on c.childid=v.childid and v.week='" + WeekNo + "'";
                    SQL += " where b.vill='"+ Village +"' ";
                    SQL += " and ((case when c.extype='4' then '' else c.extype end) is null or length(case when c.extype='4' or date(case when date(v.vdate)<'"+ WeekEndDate +"' then date(v.vdate) else '"+ WeekEndDate +"' end) < date(c.exdate) then '' else ifnull(c.extype,'') end)=0)";
                    SQL += " and  b.Cluster='" + Cluster + "' and b.Block='" + Block + "'";
                    SQL += " and cast(julianday(case when date('now')<date('"+ WeekEndDate +"') then date('now') else date('"+ WeekEndDate +"') end)-julianday(c.BDate) as int) <= 1825";
                }
            }
            //All Child
            else if(spnFilterOption.getSelectedItemPosition()==1) {
                if (BariCode.length() > 1) {
                    SQL = " select ifnull(db.cluster,'') dssc,ifnull(db.block,'') dssb, 'c' childmwra, c.BDate as bdate,ifnull(c.referral,'') as referral,ifnull(c.referral_add,'') as referral_add,ifnull(c.referral_foll,'') as referral_foll, ifnull(c.absent_sick,'') as absent_sick, b.Cluster cluster,b.Block block,c.ChildId childid,c.Vill vill,c.bari bari,c.HH hh,c.SNo sno,";
                    SQL += " c.PID pid,c.CID cid,Name name,Sex sex,cast(julianday(case when date('now')<date('"+ WeekEndDate +"') then date('now') else date('"+ WeekEndDate +"') end)-julianday(c.BDate) as int)aged, Cast((cast(julianday(case when date('now')<date('"+ WeekEndDate +"') then date('now') else date('"+ WeekEndDate +"') end)-julianday(c.BDate) as int)/30.44) as int) agem,ifnull(FaName,'') father,ifnull(MoName,'') mother,(case when v.ChildId is null then '2' else '1' end)visit,ifnull(c.extype,'') extype,'' pstat,'' lmpdt";
                    SQL += " ,ifnull(c.contactno,'') contactno,ifnull(v.vstat,'')vstat,ifnull(v.sickstatus,'')sickstatus from Child c inner join Bari b on c.Vill=b.Vill and c.bari=b.Bari";
                    SQL += " left outer join DSSBari db on c.Vill=db.Vill and c.bari=db.Bari";
                    //SQL += " left outer join Visits v on c.childid=v.childid and v.week='" + WeekNo + "'";
                    SQL += " left outer join (select * from visits where week='" + WeekNo + "' group by childid,week order by childid,min(vstat)) v on c.childid=v.childid and v.week='" + WeekNo + "'";
                    SQL += " where b.vill='"+ Village +"' and b.Cluster='" + Cluster + "' and b.Block='" + Block + "' and b.bari like('" + BariCode + "')";
                } else {
                    SQL = " select ifnull(db.cluster,'') dssc,ifnull(db.block,'') dssb, 'c' childmwra, c.BDate as bdate,ifnull(c.referral,'') as referral,ifnull(c.referral_add,'') as referral_add,ifnull(c.referral_foll,'') as referral_foll, ifnull(c.absent_sick,'') as absent_sick, b.Cluster cluster,b.Block block,c.ChildId childid,c.Vill vill,c.bari bari,c.HH hh,c.SNo sno,";
                    SQL += " c.PID pid,c.CID cid,Name name,Sex sex,Cast(((julianday(date('now'))-julianday(c.BDate))) as int)aged, Cast(((julianday(date('now'))-julianday(c.BDate))/30.44) as int) agem,ifnull(FaName,'') father,ifnull(MoName,'') mother,(case when v.ChildId is null then '2' else '1' end)visit,ifnull(c.extype,'') extype,'' pstat,'' lmpdt";
                    SQL += " ,ifnull(c.contactno,'') contactno,ifnull(v.vstat,'')vstat,ifnull(v.sickstatus,'')sickstatus from Child c inner join Bari b on c.Vill=b.Vill and c.bari=b.Bari";
                    SQL += " left outer join DSSBari db on c.Vill=db.Vill and c.bari=db.Bari";
                    //SQL += " left outer join Visits v on c.childid=v.childid and v.week='" + WeekNo + "'";
                    SQL += " left outer join (select * from visits where week='" + WeekNo + "' group by childid,week order by childid,min(vstat)) v on c.childid=v.childid and v.week='" + WeekNo + "'";
                    SQL += " where b.vill='"+ Village +"' and b.Cluster='" + Cluster + "' and b.Block='" + Block + "'";
                }
            }
            //Visit Completed
            else if(spnFilterOption.getSelectedItemPosition()==2) {
                if (BariCode.length() > 1) {
                    SQL = " select ifnull(db.cluster,'') dssc,ifnull(db.block,'') dssb, 'c' childmwra, c.BDate as bdate,ifnull(c.referral,'') as referral,ifnull(c.referral_add,'') as referral_add,ifnull(c.referral_foll,'') as referral_foll, ifnull(c.absent_sick,'') as absent_sick, b.Cluster cluster,b.Block block,c.ChildId childid,c.Vill vill,c.bari bari,c.HH hh,c.SNo sno,";
                    SQL += " c.PID pid,c.CID cid,Name name,Sex sex,cast(julianday(case when date('now')<date('"+ WeekEndDate +"') then date('now') else date('"+ WeekEndDate +"') end)-julianday(c.BDate) as int)aged, Cast((cast(julianday(case when date('now')<date('"+ WeekEndDate +"') then date('now') else date('"+ WeekEndDate +"') end)-julianday(c.BDate) as int)/30.44) as int) agem,ifnull(FaName,'') father,ifnull(MoName,'') mother,(case when v.ChildId is null then '2' else '1' end)visit,ifnull(c.extype,'') extype,'' pstat,'' lmpdt";
                    SQL += " ,ifnull(c.contactno,'') contactno,ifnull(v.vstat,'')vstat,ifnull(v.sickstatus,'')sickstatus from Child c inner join Bari b on c.Vill=b.Vill and c.bari=b.Bari";
                    SQL += " left outer join DSSBari db on c.Vill=db.Vill and c.bari=db.Bari";
                    //SQL += " left outer join Visits v on c.childid=v.childid and v.week='" + WeekNo + "'";
                    SQL += " left outer join (select * from visits where week='" + WeekNo + "' group by childid,week order by childid,min(vstat)) v on c.childid=v.childid and v.week='" + WeekNo + "'";
                    SQL += " where b.vill='"+ Village +"' ";
                    SQL += " and ((case when c.extype='4' then '' else c.extype end) is null or length((case when c.extype='4' then '' else ifnull(c.extype,'') end))=0)";
                    SQL += " and  b.Cluster='" + Cluster + "' and b.Block='" + Block + "' and b.bari like('" + BariCode + "')";
                    SQL += " and cast(julianday(case when date('now')<date('"+ WeekEndDate +"') then date('now') else date('"+ WeekEndDate +"') end)-julianday(c.BDate) as int) <= 1825";
                    SQL += " and (case when v.ChildId is null then '2' else '1' end)='1' ";

                } else {
                    SQL = " select ifnull(db.cluster,'') dssc,ifnull(db.block,'') dssb, 'c' childmwra, c.BDate as bdate,ifnull(c.referral,'') as referral,ifnull(c.referral_add,'') as referral_add,ifnull(c.referral_foll,'') as referral_foll, ifnull(c.absent_sick,'') as absent_sick, b.Cluster cluster,b.Block block,c.ChildId childid,c.Vill vill,c.bari bari,c.HH hh,c.SNo sno,";
                    SQL += " c.PID pid,c.CID cid,Name name,Sex sex,cast(julianday(case when date('now')<date('"+ WeekEndDate +"') then date('now') else date('"+ WeekEndDate +"') end)-julianday(c.BDate) as int)aged, Cast((cast(julianday(case when date('now')<date('"+ WeekEndDate +"') then date('now') else date('"+ WeekEndDate +"') end)-julianday(c.BDate) as int)/30.44) as int) agem,ifnull(FaName,'') father,ifnull(MoName,'') mother,(case when v.ChildId is null then '2' else '1' end)visit,ifnull(c.extype,'') extype,'' pstat,'' lmpdt";
                    SQL += " ,ifnull(c.contactno,'') contactno,ifnull(v.vstat,'')vstat,ifnull(v.sickstatus,'')sickstatus from Child c inner join Bari b on c.Vill=b.Vill and c.bari=b.Bari";
                    SQL += " left outer join DSSBari db on c.Vill=db.Vill and c.bari=db.Bari";
                    //SQL += " left outer join Visits v on c.childid=v.childid and v.week='" + WeekNo + "'";
                    SQL += " left outer join (select * from visits where week='" + WeekNo + "' group by childid,week order by childid,min(vstat)) v on c.childid=v.childid and v.week='" + WeekNo + "'";
                    SQL += " where b.vill='"+ Village +"' ";
                    SQL += " and ((case when c.extype='4' then '' else c.extype end) is null or length((case when c.extype='4' then '' else ifnull(c.extype,'') end))=0)";
                    SQL += " and  b.Cluster='" + Cluster + "' and b.Block='" + Block + "'";
                    SQL += " and cast(julianday(case when date('now')<date('"+ WeekEndDate +"') then date('now') else date('"+ WeekEndDate +"') end)-julianday(c.BDate) as int) <= 1825";
                    SQL += " and (case when v.ChildId is null then '2' else '1' end)='1' ";
                }
            }
            //Not Yet Visited
            else if(spnFilterOption.getSelectedItemPosition()==3) {
                if (BariCode.length() > 1) {
                    SQL = " select ifnull(db.cluster,'') dssc,ifnull(db.block,'') dssb, 'c' childmwra, c.BDate as bdate,ifnull(c.referral,'') as referral,ifnull(c.referral_add,'') as referral_add,ifnull(c.referral_foll,'') as referral_foll, ifnull(c.absent_sick,'') as absent_sick, b.Cluster cluster,b.Block block,c.ChildId childid,c.Vill vill,c.bari bari,c.HH hh,c.SNo sno,";
                    SQL += " c.PID pid,c.CID cid,Name name,Sex sex,cast(julianday(case when date('now')<date('"+ WeekEndDate +"') then date('now') else date('"+ WeekEndDate +"') end)-julianday(c.BDate) as int)aged, Cast((cast(julianday(case when date('now')<date('"+ WeekEndDate +"') then date('now') else date('"+ WeekEndDate +"') end)-julianday(c.BDate) as int)/30.44) as int) agem,ifnull(FaName,'') father,ifnull(MoName,'') mother,(case when v.ChildId is null then '2' else '1' end)visit,ifnull(c.extype,'') extype,'' pstat,'' lmpdt";
                    SQL += " ,ifnull(c.contactno,'') contactno,ifnull(v.vstat,'')vstat,ifnull(v.sickstatus,'')sickstatus from Child c inner join Bari b on c.Vill=b.Vill and c.bari=b.Bari";
                    SQL += " left outer join DSSBari db on c.Vill=db.Vill and c.bari=db.Bari";
                    //SQL += " left outer join Visits v on c.childid=v.childid and v.week='" + WeekNo + "'";
                    SQL += " left outer join (select * from visits where week='" + WeekNo + "' group by childid,week order by childid,min(vstat)) v on c.childid=v.childid and v.week='" + WeekNo + "'";

                    SQL += " where b.vill='"+ Village +"' ";
                    SQL += " and ((case when c.extype='4' then '' else c.extype end) is null or length((case when c.extype='4' then '' else ifnull(c.extype,'') end))=0)";
                    SQL += " and  b.Cluster='" + Cluster + "' and b.Block='" + Block + "' and b.bari like('" + BariCode + "')";
                    SQL += " and cast(julianday(case when date('now')<date('"+ WeekEndDate +"') then date('now') else date('"+ WeekEndDate +"') end)-julianday(c.BDate) as int) <= 1825";
                    SQL += " and (case when v.ChildId is null then '2' else '1' end)='2' ";

                } else {
                    SQL = " select ifnull(db.cluster,'') dssc,ifnull(db.block,'') dssb, 'c' childmwra, c.BDate as bdate,ifnull(c.referral,'') as referral,ifnull(c.referral_add,'') as referral_add,ifnull(c.referral_foll,'') as referral_foll, ifnull(c.absent_sick,'') as absent_sick, b.Cluster cluster,b.Block block,c.ChildId childid,c.Vill vill,c.bari bari,c.HH hh,c.SNo sno,";
                    SQL += " c.PID pid,c.CID cid,Name name,Sex sex,cast(julianday(case when date('now')<date('"+ WeekEndDate +"') then date('now') else date('"+ WeekEndDate +"') end)-julianday(c.BDate) as int)aged, Cast((cast(julianday(case when date('now')<date('"+ WeekEndDate +"') then date('now') else date('"+ WeekEndDate +"') end)-julianday(c.BDate) as int)/30.44) as int) agem,ifnull(FaName,'') father,ifnull(MoName,'') mother,(case when v.ChildId is null then '2' else '1' end)visit,ifnull(c.extype,'') extype,'' pstat,'' lmpdt";
                    SQL += " ,ifnull(c.contactno,'') contactno,ifnull(v.vstat,'')vstat,ifnull(v.sickstatus,'')sickstatus from Child c inner join Bari b on c.Vill=b.Vill and c.bari=b.Bari";
                    SQL += " left outer join DSSBari db on c.Vill=db.Vill and c.bari=db.Bari";
                    //SQL += " left outer join Visits v on c.childid=v.childid and v.week='" + WeekNo + "'";
                    SQL += " left outer join (select * from visits where week='" + WeekNo + "' group by childid,week order by childid,min(vstat)) v on c.childid=v.childid and v.week='" + WeekNo + "'";

                    SQL += " where b.vill='"+ Village +"' ";
                    SQL += " and ((case when c.extype='4' then '' else c.extype end) is null or length((case when c.extype='4' then '' else ifnull(c.extype,'') end))=0)";
                    SQL += " and  b.Cluster='" + Cluster + "' and b.Block='" + Block + "'";
                    SQL += " and cast(julianday(case when date('now')<date('"+ WeekEndDate +"') then date('now') else date('"+ WeekEndDate +"') end)-julianday(c.BDate) as int) <= 1825";
                    SQL += " and (case when v.ChildId is null then '2' else '1' end)='2' ";

                }
            }
            //Over Age
            else if(spnFilterOption.getSelectedItemPosition()==4)
            {
                if (BariCode.length() > 1) {
                    SQL = " select ifnull(db.cluster,'') dssc,ifnull(db.block,'') dssb, 'c' childmwra, c.BDate as bdate,ifnull(c.referral,'') as referral,ifnull(c.referral_add,'') as referral_add,ifnull(c.referral_foll,'') as referral_foll, ifnull(c.absent_sick,'') as absent_sick, b.Cluster cluster,b.Block block,c.ChildId childid,c.Vill vill,c.bari bari,c.HH hh,c.SNo sno,";
                    SQL += " c.PID pid,c.CID cid,Name name,Sex sex,cast(julianday(case when date('now')<date('"+ WeekEndDate +"') then date('now') else date('"+ WeekEndDate +"') end)-julianday(c.BDate) as int)aged, Cast((cast(julianday(case when date('now')<date('"+ WeekEndDate +"') then date('now') else date('"+ WeekEndDate +"') end)-julianday(c.BDate) as int)/30.44) as int) agem,ifnull(FaName,'') father,ifnull(MoName,'') mother,(case when v.ChildId is null then '2' else '1' end)visit,ifnull(c.extype,'') extype,'' pstat,'' lmpdt";
                    SQL += " ,ifnull(c.contactno,'') contactno,ifnull(v.vstat,'')vstat,ifnull(v.sickstatus,'')sickstatus from Child c inner join Bari b on c.Vill=b.Vill and c.bari=b.Bari";
                    SQL += " left outer join DSSBari db on c.Vill=db.Vill and c.bari=db.Bari";
                    SQL += " left outer join (select * from visits where week='" + WeekNo + "' group by childid,week order by childid,min(vstat)) v on c.childid=v.childid and v.week='" + WeekNo + "'";

                    SQL += " where b.vill='"+ Village +"' ";
                    SQL += " and ((case when c.extype='4' then '' else c.extype end) is null or length((case when c.extype='4' then '' else ifnull(c.extype,'') end))=0)";
                    SQL += " and  b.Cluster='" + Cluster + "' and b.Block='" + Block + "' and b.bari like('" + BariCode + "')";
                    SQL += " and cast(julianday(case when date('now')<date('"+ WeekEndDate +"') then date('now') else date('"+ WeekEndDate +"') end)-julianday(c.BDate) as int) > 1825";
                } else {
                    SQL = " select ifnull(db.cluster,'') dssc,ifnull(db.block,'') dssb, 'c' childmwra, c.BDate as bdate,ifnull(c.referral,'') as referral,ifnull(c.referral_add,'') as referral_add,ifnull(c.referral_foll,'') as referral_foll,ifnull(c.absent_sick,'') as absent_sick,  b.Cluster cluster,b.Block block,c.ChildId childid,c.Vill vill,c.bari bari,c.HH hh,c.SNo sno,";
                    SQL += " c.PID pid,c.CID cid,Name name,Sex sex,cast(julianday(case when date('now')<date('"+ WeekEndDate +"') then date('now') else date('"+ WeekEndDate +"') end)-julianday(c.BDate) as int)aged, Cast((cast(julianday(case when date('now')<date('"+ WeekEndDate +"') then date('now') else date('"+ WeekEndDate +"') end)-julianday(c.BDate) as int)/30.44) as int) agem,ifnull(FaName,'') father,ifnull(MoName,'') mother,(case when v.ChildId is null then '2' else '1' end)visit,ifnull(c.extype,'') extype,'' pstat,'' lmpdt";
                    SQL += " ,ifnull(c.contactno,'') contactno,ifnull(v.vstat,'')vstat,ifnull(v.sickstatus,'')sickstatus from Child c inner join Bari b on c.Vill=b.Vill and c.bari=b.Bari";
                    SQL += " left outer join DSSBari db on c.Vill=db.Vill and c.bari=db.Bari";
                    SQL += " left outer join (select * from visits where week='" + WeekNo + "' group by childid,week order by childid,min(vstat)) v on c.childid=v.childid and v.week='" + WeekNo + "'";

                    SQL += " where b.vill='"+ Village +"' ";
                    SQL += " and ((case when c.extype='4' then '' else c.extype end) is null or length((case when c.extype='4' then '' else ifnull(c.extype,'') end))=0)";
                    SQL += " and  b.Cluster='" + Cluster + "' and b.Block='" + Block + "'";
                    SQL += " and cast(julianday(case when date('now')<date('"+ WeekEndDate +"') then date('now') else date('"+ WeekEndDate +"') end)-julianday(c.BDate) as int) > 1825";
                }
            }
            //Neonate
            else if(spnFilterOption.getSelectedItemPosition()==5)
            {
                if (BariCode.length() > 1) {
                    SQL = " select ifnull(db.cluster,'') dssc,ifnull(db.block,'') dssb, 'c' childmwra, c.BDate as bdate,ifnull(c.referral,'') as referral,ifnull(c.referral_add,'') as referral_add,ifnull(c.referral_foll,'') as referral_foll,ifnull(c.absent_sick,'') as absent_sick,  b.Cluster cluster,b.Block block,c.ChildId childid,c.Vill vill,c.bari bari,c.HH hh,c.SNo sno,";
                    SQL += " c.PID pid,c.CID cid,Name name,Sex sex,cast(julianday(case when date('now')<date('"+ WeekEndDate +"') then date('now') else date('"+ WeekEndDate +"') end)-julianday(c.BDate) as int)aged, Cast((cast(julianday(case when date('now')<date('"+ WeekEndDate +"') then date('now') else date('"+ WeekEndDate +"') end)-julianday(c.BDate) as int)/30.44) as int) agem,ifnull(FaName,'') father,ifnull(MoName,'') mother,(case when v.ChildId is null then '2' else '1' end)visit,ifnull(c.extype,'') extype,'' pstat,'' lmpdt";
                    SQL += " ,ifnull(c.contactno,'') contactno,ifnull(v.vstat,'')vstat,ifnull(v.sickstatus,'')sickstatus from Child c inner join Bari b on c.Vill=b.Vill and c.bari=b.Bari";
                    SQL += " left outer join DSSBari db on c.Vill=db.Vill and c.bari=db.Bari";
                    SQL += " left outer join (select * from visits where week='" + WeekNo + "' group by childid,week order by childid,min(vstat)) v on c.childid=v.childid and v.week='" + WeekNo + "'";

                    SQL += " where b.vill='"+ Village +"' ";
                    SQL += " and ((case when c.extype='4' then '' else c.extype end) is null or length((case when c.extype='4' then '' else ifnull(c.extype,'') end))=0)";
                    SQL += " and  b.Cluster='" + Cluster + "' and b.Block='" + Block + "' and b.bari like('" + BariCode + "')";
                    SQL += " and cast(julianday(case when date('now')<date('"+ WeekEndDate +"') then date('now') else date('"+ WeekEndDate +"') end)-julianday(c.BDate) as int) <= 28";

                } else {
                    SQL = " select ifnull(db.cluster,'') dssc,ifnull(db.block,'') dssb, 'c' childmwra, c.BDate as bdate,ifnull(c.referral,'') as referral,ifnull(c.referral_add,'') as referral_add,ifnull(c.referral_foll,'') as referral_foll,ifnull(c.absent_sick,'') as absent_sick,  b.Cluster cluster,b.Block block,c.ChildId childid,c.Vill vill,c.bari bari,c.HH hh,c.SNo sno,";
                    SQL += " c.PID pid,c.CID cid,Name name,Sex sex,cast(julianday(case when date('now')<date('"+ WeekEndDate +"') then date('now') else date('"+ WeekEndDate +"') end)-julianday(c.BDate) as int)aged, Cast((cast(julianday(case when date('now')<date('"+ WeekEndDate +"') then date('now') else date('"+ WeekEndDate +"') end)-julianday(c.BDate) as int)/30.44) as int) agem,ifnull(FaName,'') father,ifnull(MoName,'') mother,(case when v.ChildId is null then '2' else '1' end)visit,ifnull(c.extype,'') extype,'' pstat,'' lmpdt";
                    SQL += " ,ifnull(c.contactno,'') contactno,ifnull(v.vstat,'')vstat,ifnull(v.sickstatus,'')sickstatus from Child c inner join Bari b on c.Vill=b.Vill and c.bari=b.Bari";
                    SQL += " left outer join DSSBari db on c.Vill=db.Vill and c.bari=db.Bari";
                    SQL += " left outer join (select * from visits where week='" + WeekNo + "' group by childid,week order by childid,min(vstat)) v on c.childid=v.childid and v.week='" + WeekNo + "'";

                    SQL += " where b.vill='"+ Village +"' ";
                    SQL += " and ((case when c.extype='4' then '' else c.extype end) is null or length((case when c.extype='4' then '' else ifnull(c.extype,'') end))=0)";
                    SQL += " and  b.Cluster='" + Cluster + "' and b.Block='" + Block + "'";
                    SQL += " and cast(julianday(case when date('now')<date('"+ WeekEndDate +"') then date('now') else date('"+ WeekEndDate +"') end)-julianday(c.BDate) as int) <= 28";
                }
            }
            //death
            else if(spnFilterOption.getSelectedItemPosition()==6)
            {
                if (BariCode.length() > 1) {
                    SQL = " select ifnull(db.cluster,'') dssc,ifnull(db.block,'') dssb, 'c' childmwra, c.BDate as bdate,ifnull(c.referral,'') as referral,ifnull(c.referral_add,'') as referral_add,ifnull(c.referral_foll,'') as referral_foll,ifnull(c.absent_sick,'') as absent_sick,  b.Cluster cluster,b.Block block,c.ChildId childid,c.Vill vill,c.bari bari,c.HH hh,c.SNo sno,";
                    SQL += " c.PID pid,c.CID cid,Name name,Sex sex,cast(julianday(case when date('now')<date('"+ WeekEndDate +"') then date('now') else date('"+ WeekEndDate +"') end)-julianday(c.BDate) as int)aged, Cast((cast(julianday(case when date('now')<date('"+ WeekEndDate +"') then date('now') else date('"+ WeekEndDate +"') end)-julianday(c.BDate) as int)/30.44) as int) agem,ifnull(FaName,'') father,ifnull(MoName,'') mother,(case when v.ChildId is null then '2' else '1' end)visit,ifnull(c.extype,'') extype,'' pstat,'' lmpdt";
                    SQL += " ,ifnull(c.contactno,'') contactno,ifnull(v.vstat,'')vstat,ifnull(v.sickstatus,'')sickstatus from Child c inner join Bari b on c.Vill=b.Vill and c.bari=b.Bari";
                    SQL += " left outer join DSSBari db on c.Vill=db.Vill and c.bari=db.Bari";
                    //SQL += " left outer join Visits v on c.childid=v.childid and v.week='" + WeekNo + "'";
                    SQL += " left outer join (select * from visits where week='" + WeekNo + "' group by childid,week order by childid,min(vstat)) v on c.childid=v.childid and v.week='" + WeekNo + "'";

                    SQL += " where b.vill='"+ Village +"' and b.Cluster='" + Cluster + "' and b.Block='" + Block + "' and b.bari like('" + BariCode + "')";
                    SQL += " and v.vstat='6'";
                } else {
                    SQL = " select ifnull(db.cluster,'') dssc,ifnull(db.block,'') dssb, 'c' childmwra, c.BDate as bdate,ifnull(c.referral,'') as referral,ifnull(c.referral_add,'') as referral_add,ifnull(c.referral_foll,'') as referral_foll,ifnull(c.absent_sick,'') as absent_sick,  b.Cluster cluster,b.Block block,c.ChildId childid,c.Vill vill,c.bari bari,c.HH hh,c.SNo sno,";
                    SQL += " c.PID pid,c.CID cid,Name name,Sex sex,cast(julianday(case when date('now')<date('"+ WeekEndDate +"') then date('now') else date('"+ WeekEndDate +"') end)-julianday(c.BDate) as int)aged, Cast((cast(julianday(case when date('now')<date('"+ WeekEndDate +"') then date('now') else date('"+ WeekEndDate +"') end)-julianday(c.BDate) as int)/30.44) as int) agem,ifnull(FaName,'') father,ifnull(MoName,'') mother,(case when v.ChildId is null then '2' else '1' end)visit,ifnull(c.extype,'') extype,'' pstat,'' lmpdt";
                    SQL += " ,ifnull(c.contactno,'') contactno,ifnull(v.vstat,'')vstat,ifnull(v.sickstatus,'')sickstatus from Child c inner join Bari b on c.Vill=b.Vill and c.bari=b.Bari";
                    SQL += " left outer join DSSBari db on c.Vill=db.Vill and c.bari=db.Bari";
                    //SQL += " left outer join Visits v on c.childid=v.childid and v.week='" + WeekNo + "'";
                    SQL += " left outer join (select * from visits where week='" + WeekNo + "' group by childid,week order by childid,min(vstat)) v on c.childid=v.childid and v.week='" + WeekNo + "'";

                    SQL += " where b.vill='"+ Village +"' and b.Cluster='" + Cluster + "' and b.Block='" + Block + "'";
                    SQL += " and v.vstat='6'";
                }
            }
            //sick
            else if(spnFilterOption.getSelectedItemPosition()==7)
            {
                if (BariCode.length() > 1) {
                    SQL = " select ifnull(db.cluster,'') dssc,ifnull(db.block,'') dssb, 'c' childmwra, c.BDate as bdate,ifnull(c.referral,'') as referral,ifnull(c.referral_add,'') as referral_add,ifnull(c.referral_foll,'') as referral_foll,ifnull(c.absent_sick,'') as absent_sick,  b.Cluster cluster,b.Block block,c.ChildId childid,c.Vill vill,c.bari bari,c.HH hh,c.SNo sno,";
                    SQL += " c.PID pid,c.CID cid,Name name,Sex sex,cast(julianday(case when date('now')<date('"+ WeekEndDate +"') then date('now') else date('"+ WeekEndDate +"') end)-julianday(c.BDate) as int)aged, Cast((cast(julianday(case when date('now')<date('"+ WeekEndDate +"') then date('now') else date('"+ WeekEndDate +"') end)-julianday(c.BDate) as int)/30.44) as int) agem,ifnull(FaName,'') father,ifnull(MoName,'') mother,(case when v.ChildId is null then '2' else '1' end)visit,ifnull(c.extype,'') extype,'' pstat,'' lmpdt";
                    SQL += " ,ifnull(c.contactno,'') contactno,ifnull(v.vstat,'')vstat,ifnull(v.sickstatus,'')sickstatus from Child c inner join Bari b on c.Vill=b.Vill and c.bari=b.Bari";
                    SQL += " left outer join DSSBari db on c.Vill=db.Vill and c.bari=db.Bari";
                    //SQL += " left outer join Visits v on c.childid=v.childid and v.week='" + WeekNo + "'";
                    SQL += " left outer join (select * from visits where week='" + WeekNo + "' group by childid,week order by childid,min(vstat)) v on c.childid=v.childid and v.week='" + WeekNo + "'";

                    SQL += " where b.vill='"+ Village +"' and (c.extype is null or length(c.extype)=0) and  b.Cluster='" + Cluster + "' and b.Block='" + Block + "' and b.bari like('" + BariCode + "')";
                    SQL += " and ifnull(v.sickstatus,'')='2'";
                } else {
                    SQL = " select ifnull(db.cluster,'') dssc,ifnull(db.block,'') dssb, 'c' childmwra, c.BDate as bdate,ifnull(c.referral,'') as referral,ifnull(c.referral_add,'') as referral_add,ifnull(c.referral_foll,'') as referral_foll,ifnull(c.absent_sick,'') as absent_sick,  b.Cluster cluster,b.Block block,c.ChildId childid,c.Vill vill,c.bari bari,c.HH hh,c.SNo sno,";
                    SQL += " c.PID pid,c.CID cid,Name name,Sex sex,cast(julianday(case when date('now')<date('"+ WeekEndDate +"') then date('now') else date('"+ WeekEndDate +"') end)-julianday(c.BDate) as int)aged, Cast((cast(julianday(case when date('now')<date('"+ WeekEndDate +"') then date('now') else date('"+ WeekEndDate +"') end)-julianday(c.BDate) as int)/30.44) as int) agem,ifnull(FaName,'') father,ifnull(MoName,'') mother,(case when v.ChildId is null then '2' else '1' end)visit,ifnull(c.extype,'') extype,'' pstat,'' lmpdt";
                    SQL += " ,ifnull(c.contactno,'') contactno,ifnull(v.vstat,'')vstat,ifnull(v.sickstatus,'')sickstatus from Child c inner join Bari b on c.Vill=b.Vill and c.bari=b.Bari";
                    SQL += " left outer join DSSBari db on c.Vill=db.Vill and c.bari=db.Bari";
                    //SQL += " left outer join Visits v on c.childid=v.childid and v.week='" + WeekNo + "'";
                    SQL += " left outer join (select * from visits where week='" + WeekNo + "' group by childid,week order by childid,min(vstat)) v on c.childid=v.childid and v.week='" + WeekNo + "'";

                    SQL += " where b.vill='"+ Village +"' and (c.extype is null or length(c.extype)=0) and b.Cluster='" + Cluster + "' and b.Block='" + Block + "'";
                    SQL += " and ifnull(v.sickstatus,'')='2'";
                }
            }
            //migration
            else if(spnFilterOption.getSelectedItemPosition()==8)
            {
                if (BariCode.length() > 1) {
                    SQL = " select ifnull(db.cluster,'') dssc,ifnull(db.block,'') dssb, 'c' childmwra, c.BDate as bdate,ifnull(c.referral,'') as referral,ifnull(c.referral_add,'') as referral_add,ifnull(c.referral_foll,'') as referral_foll,ifnull(c.absent_sick,'') as absent_sick,  b.Cluster cluster,b.Block block,c.ChildId childid,c.Vill vill,c.bari bari,c.HH hh,c.SNo sno,";
                    SQL += " c.PID pid,c.CID cid,Name name,Sex sex,cast(julianday(case when date('now')<date('"+ WeekEndDate +"') then date('now') else date('"+ WeekEndDate +"') end)-julianday(c.BDate) as int)aged, Cast((cast(julianday(case when date('now')<date('"+ WeekEndDate +"') then date('now') else date('"+ WeekEndDate +"') end)-julianday(c.BDate) as int)/30.44) as int) agem,ifnull(FaName,'') father,ifnull(MoName,'') mother,(case when v.ChildId is null then '2' else '1' end)visit,ifnull(c.extype,'') extype,'' pstat,'' lmpdt";
                    SQL += " ,ifnull(c.contactno,'') contactno,ifnull(v.vstat,'')vstat,ifnull(v.sickstatus,'')sickstatus from Child c inner join Bari b on c.Vill=b.Vill and c.bari=b.Bari";
                    SQL += " left outer join DSSBari db on c.Vill=db.Vill and c.bari=db.Bari";
                    //SQL += " left outer join Visits v on c.childid=v.childid and v.week='" + WeekNo + "'";
                    SQL += " left outer join (select * from visits where week='" + WeekNo + "' group by childid,week order by childid,min(vstat)) v on c.childid=v.childid and v.week='" + WeekNo + "'";

                    SQL += " where b.vill='"+ Village +"' and b.Cluster='" + Cluster + "' and b.Block='" + Block + "' and b.bari like('" + BariCode + "')";
                    SQL += " and v.vstat='5'";
                } else {
                    SQL = " select ifnull(db.cluster,'') dssc,ifnull(db.block,'') dssb, 'c' childmwra, c.BDate as bdate,ifnull(c.referral,'') as referral,ifnull(c.referral_add,'') as referral_add,ifnull(c.referral_foll,'') as referral_foll,ifnull(c.absent_sick,'') as absent_sick,  b.Cluster cluster,b.Block block,c.ChildId childid,c.Vill vill,c.bari bari,c.HH hh,c.SNo sno,";
                    SQL += " c.PID pid,c.CID cid,Name name,Sex sex,cast(julianday(case when date('now')<date('"+ WeekEndDate +"') then date('now') else date('"+ WeekEndDate +"') end)-julianday(c.BDate) as int)aged, Cast((cast(julianday(case when date('now')<date('"+ WeekEndDate +"') then date('now') else date('"+ WeekEndDate +"') end)-julianday(c.BDate) as int)/30.44) as int) agem,ifnull(FaName,'') father,ifnull(MoName,'') mother,(case when v.ChildId is null then '2' else '1' end)visit,ifnull(c.extype,'') extype,'' pstat,'' lmpdt";
                    SQL += " ,ifnull(c.contactno,'') contactno,ifnull(v.vstat,'')vstat,ifnull(v.sickstatus,'')sickstatus from Child c inner join Bari b on c.Vill=b.Vill and c.bari=b.Bari";
                    SQL += " left outer join DSSBari db on c.Vill=db.Vill and c.bari=db.Bari";
                    //SQL += " left outer join Visits v on c.childid=v.childid and v.week='" + WeekNo + "'";
                    SQL += " left outer join (select * from visits where week='" + WeekNo + "' group by childid,week order by childid,min(vstat)) v on c.childid=v.childid and v.week='" + WeekNo + "'";

                    SQL += " where b.vill='"+ Village +"' and b.Cluster='" + Cluster + "' and b.Block='" + Block + "'";
                    SQL += " and v.vstat='5'";
                }
            }

            if(chkMWRA.isChecked()) {
                SQL += " Union Select ifnull(db.cluster,'') dssc,ifnull(db.block,'') dssb, 'm' childmwra, c.BDate as bdate,'' as referral,'' as referral_add,'' as referral_foll,'' as absent_sick,  b.Cluster cluster,b.Block block,c.mwraid childid,c.Vill vill,c.bari bari,c.HH hh,c.SNo sno,";
                SQL += " c.PID pid,c.CID cid,Name name,Sex sex,Cast(((julianday(date('now'))-julianday(c.BDate))/365.25) as int)aged, Cast(((julianday(date('now'))-julianday(c.BDate))/30.44) as int) agem,ifnull(FaName,'') father,ifnull(MoName,'') mother,'2' visit,ifnull(c.extype,'') extype,pstat as pstat,lmpdt as lmpdt";
                SQL += " ,'' contactno,'' vstat,'' sickstatus from MWRA c inner join Bari b on c.Vill=b.Vill and c.bari=b.Bari";
                SQL += " left outer join DSSBari db on c.Vill=db.Vill and c.bari=db.Bari";
                SQL += " where b.vill='" + Village + "' and (c.extype is null or length(c.extype)=0) and  B.Cluster='" + Cluster + "' and b.Block='" + Block + "' and b.bari like('" + BariCode + "') order by c.cid asc";
            }

            Cursor cur = C.ReadData(SQL);
            cur.moveToFirst();

            int i=0;
            while (!cur.isAfterLast()) {
                map = new HashMap<String, String>();
                map.put("sl", String.valueOf(i));
                map.put("childid", cur.getString(cur.getColumnIndex("childid")));
                map.put("vill", cur.getString(cur.getColumnIndex("vill")));
                map.put("bari", cur.getString(cur.getColumnIndex("bari")));
                map.put("hh", cur.getString(cur.getColumnIndex("hh")));
                map.put("sno", cur.getString(cur.getColumnIndex("sno")));
                map.put("pid", cur.getString(cur.getColumnIndex("pid")));
                map.put("cid", cur.getString(cur.getColumnIndex("cid")));
                map.put("name", cur.getString(cur.getColumnIndex("name")));
                map.put("sex", cur.getString(cur.getColumnIndex("sex")));
                map.put("aged", cur.getString(cur.getColumnIndex("aged")));
                map.put("agem", cur.getString(cur.getColumnIndex("agem")));
                map.put("father", cur.getString(cur.getColumnIndex("father")).trim());
                map.put("mother", cur.getString(cur.getColumnIndex("mother")).trim());
                map.put("visit", cur.getString(cur.getColumnIndex("visit")).trim());
                map.put("extype", cur.getString(cur.getColumnIndex("extype")).trim());

                map.put("referral", cur.getString(cur.getColumnIndex("referral")).trim());
                map.put("referral_add", cur.getString(cur.getColumnIndex("referral_add")).trim());
                map.put("referral_foll", cur.getString(cur.getColumnIndex("referral_foll")).trim());
                map.put("absent_sick", cur.getString(cur.getColumnIndex("absent_sick")).trim());


                map.put("bdate", cur.getString(cur.getColumnIndex("bdate")).trim());
                map.put("childmwra", cur.getString(cur.getColumnIndex("childmwra")).trim());

                map.put("pstat", cur.getString(cur.getColumnIndex("pstat")).trim());
                map.put("lmpdt", cur.getString(cur.getColumnIndex("lmpdt")).trim());

                map.put("dssc", cur.getString(cur.getColumnIndex("dssc")).trim());
                map.put("dssb", cur.getString(cur.getColumnIndex("dssb")).trim());
                map.put("contactno", cur.getString(cur.getColumnIndex("contactno")).trim());
                map.put("vstat", cur.getString(cur.getColumnIndex("vstat")).trim());
                map.put("sickstatus", cur.getString(cur.getColumnIndex("sickstatus")).trim());

                if(cur.getString(cur.getColumnIndex("childmwra")).trim().equals("c"))
                    i+=1;

                mylistChild.add(map);

                cur.moveToNext();
            }
            cur.close();
            mScheduleChild = new SimpleAdapter(this, mylistChild, R.layout.child_list_row,
                    new String[]{"bari"},
                    new int[]{R.id.Bari});

            lblPageHeading.setText("  শিশুর তালিকা ( "+String.valueOf(i)+" )");
            listChild.setAdapter(new ChildListAdapter(this));

        } catch (Exception e) {
            AlertDialog.Builder adb = new AlertDialog.Builder(HouseholdIndex.this);
            adb.setTitle("Message");
            adb.setMessage(e.getMessage());
            adb.setPositiveButton("Ok", null);
            adb.show();
        }

    }


    String FM;
    public class ChildListAdapter extends BaseAdapter {
        private Context context;

        public ChildListAdapter(Context c) {
            context = c;
        }

        public int getCount() {
            return mScheduleChild.getCount();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }


        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.child_list_row, null);
            }

            TextView lblChildName = (TextView) convertView.findViewById(R.id.lblChildName);
            TextView ChildName = (TextView) convertView.findViewById(R.id.ChildName);
            TextView CID = (TextView) convertView.findViewById(R.id.CID);
            TextView PID = (TextView) convertView.findViewById(R.id.PID);
            TextView FatherMother = (TextView) convertView.findViewById(R.id.FatherMother);
            TextView Age = (TextView) convertView.findViewById(R.id.Age);
            TextView Sex = (TextView) convertView.findViewById(R.id.Sex);
            TextView BariLocation = (TextView) convertView.findViewById(R.id.BariLocation);

            TextView lblDSSCluster = (TextView) convertView.findViewById(R.id.lblDSSCluster);
            TextView lblDSSBlock   = (TextView) convertView.findViewById(R.id.lblDSSBlock);
            TextView lblContactNo   = (TextView) convertView.findViewById(R.id.lblContactNo);
            LinearLayout secContactNo   = (LinearLayout) convertView.findViewById(R.id.secContactNo);
            TextView lblFM_Husband = (TextView)convertView.findViewById(R.id.lblFM_Husband);

            TextView lblVisitStatus = (TextView)convertView.findViewById(R.id.lblVisitStatus);
            LinearLayout secVisitStatus = (LinearLayout)convertView.findViewById(R.id.secVisitStatus);

            final HashMap<String, String> o = (HashMap<String, String>) mScheduleChild.getItem(position);

            if(o.get("vstat").toString().length()>0)
                secVisitStatus.setVisibility(View.VISIBLE);
            else
                secVisitStatus.setVisibility(View.GONE);

            String VS = o.get("vstat").toString();
            String SS = o.get("sickstatus").toString();
            String SStatus = "";
            if(SS.equals("1")) SStatus = "সুস্থ আছে";
            else if(SS.equals("2")) SStatus = "অসুস্থ";
            else if(SS.equals("3")) SStatus = "জানিনা";

            if(VS.equals("1"))
                lblVisitStatus.setText(": শিশু উপস্থিত ("+ SStatus +")");
            else if(VS.equals("2"))
                lblVisitStatus.setText(": শিশু অনুপস্থিত ("+ SStatus +")");
            else if(VS.equals("3"))
                lblVisitStatus.setText(": চিকিৎসার জন্য অনুপস্থিত ("+ SStatus +")");
            else if(VS.equals("4"))
                lblVisitStatus.setText(": বয়স উত্তীর্ণ");
            else if(VS.equals("5"))
                lblVisitStatus.setText(": স্থানান্তর");
            else if(VS.equals("6"))
                lblVisitStatus.setText(": মৃত্যুবরন");
            else if(VS.equals("7"))
                lblVisitStatus.setText(": সার্ভিলেন্সে থাকতে অসম্মতি");
            else if(VS.equals("8"))
                lblVisitStatus.setText(": VHW ছুটিতে আছে");
            else if(VS.equals("9"))
                lblVisitStatus.setText(": তথ্য পাওয়া জায়নি");
            else if(VS.equals("10"))
                lblVisitStatus.setText(": অপ্রত্যাশিত কারণ");
            else if(VS.equals("11"))
                lblVisitStatus.setText(": ঝড় / বৃষ্টির কারণ");
            else if(VS.equals("12"))
                lblVisitStatus.setText(": বন্যার কারণ");
            else if(VS.equals("0"))
                lblVisitStatus.setText(": পুনঃ আগমন");
            else if(VS.equals("13"))
                lblVisitStatus.setText(": সরকারী ছুটির দিন");
            else if(VS.equals("15"))
                lblVisitStatus.setText(": Training/Meeting");
            else if(VS.equals("14"))
                lblVisitStatus.setText(": অন্যান্য কারণ");
//            else if(VS.equals("16"))
//                lblVisitStatus.setText(": ফোন কল রিসিভ: সাক্ষাতকার দিতে সম্মত হয়েছে");
            else if(VS.equals("17"))
                lblVisitStatus.setText(": ফোন কল রিসিভ: সাক্ষাতকার দিতে রাজি নন");
            else if(VS.equals("18"))
                lblVisitStatus.setText(": ফোন কল রিসিভ করেনি");
            else if(VS.equals("19"))
                lblVisitStatus.setText(": ফোন সুইচ অফ");
            else if(VS.equals("20"))
                lblVisitStatus.setText(": ভুল নম্বর");
            else if(VS.equals("21"))
                lblVisitStatus.setText(": ফোন কল রিসিভ: সাক্ষাতকার দিতে সম্মত হয়েছেহয়েছে। শিশু উপস্থিত(সুস্থ আছে)");
            else if(VS.equals("22"))
                lblVisitStatus.setText(": ফোন কল রিসিভ: সাক্ষাতকার দিতে সম্মত হয়েছেহয়েছে। শিশু উপস্থিত(অসুস্থ আছে)");
            else if(VS.equals("23"))
                lblVisitStatus.setText(": ফোন কল রিসিভ: সাক্ষাতকার দিতে সম্মত হয়েছেহয়েছে। শিশু অনুপস্থিত(সুস্থ আছে)");
            else if(VS.equals("24"))
                lblVisitStatus.setText(": ফোন কল রিসিভ: সাক্ষাতকার দিতে সম্মত হয়েছেহয়েছে। শিশু অনুপস্থিত(অসুস্থ আছে)");
            else if(VS.equals("25"))
                lblVisitStatus.setText(": ফোন কল রিসিভ: সাক্ষাতকার দিতে সম্মত হয়েছেহয়েছে। শিশু চিকিৎসার জন্য অনুপস্থিত(অসুস্থ আছে)");
            else if(VS.equals("26"))
                lblVisitStatus.setText(": ফোন কল রিসিভ: সাক্ষাতকার দিতে সম্মত হয়েছেহয়েছে। (স্থানান্তর)");
            else if(VS.equals("27"))
                lblVisitStatus.setText(": ফোন কল রিসিভ: সাক্ষাতকার দিতে সম্মত হয়েছেহয়েছে। (মৃত্যুবরণ)");

            //CID.setText(": " + o.get("cid"));
            CID.setText(": "+ o.get("vill")+"-"+o.get("bari")+"-"+o.get("hh")+"-"+o.get("sno"));
            PID.setText(": "+ o.get("pid"));

            FM = o.get("father");
            if(o.get("mother").toString().length()>0)
                FM += "/"+ o.get("mother");

            FatherMother.setText(": "+ FM);

            if(Integer.valueOf(o.get("aged")) <= 28)
                Age.setText(": "+ o.get("aged")+ " দিন");
            else
                Age.setText(": "+ o.get("agem")+ " মাস");

            double m = Integer.valueOf(o.get("aged"))/30.44;
            double d = Integer.valueOf(o.get("aged"))%30.44;

            final String AgeDayMonth = String.valueOf((int)m)+" মাস "+ String.valueOf((int)d)+"  দিন";

            Sex.setText(": "+ Global.DateConvertDMY(o.get("bdate").toString()));

            lblDSSCluster.setText(": " + o.get("dssc"));
            lblDSSBlock.setText(": "+ o.get("dssb"));
            lblContactNo.setText(": "+ o.get("contactno"));
            if(o.get("contactno").length()>0)
                secContactNo.setVisibility(View.VISIBLE);
            else
                secContactNo.setVisibility(View.GONE);


            Button btnRegis = (Button) convertView.findViewById(R.id.btnRegis);
            btnRegis.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {

                    Bundle IDbundle = new Bundle();
                    IDbundle.putString("childid", "");

                    IDbundle.putString("mothercid", o.get("vill")+o.get("bari")+o.get("hh")+o.get("sno"));
                    IDbundle.putString("motherpid", o.get("pid"));
                    IDbundle.putString("mother", o.get("name"));
                    IDbundle.putString("father", o.get("father"));

                    IDbundle.putString("vill", Global.Left(VillageList.getSelectedItem().toString(), 3));
                    IDbundle.putString("bari", o.get("bari"));
                    IDbundle.putString("hh", o.get("hh"));
                    IDbundle.putString("status", "new1");

                    Intent f1 = new Intent(getApplicationContext(), ChildRegistration.class);
                    f1.putExtras(IDbundle);
                    startActivity(f1);
                }
            });


            Button btnUpdate   = (Button)convertView.findViewById(R.id.btnUpdate);
            btnUpdate.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    Bundle IDbundle = new Bundle();
                    IDbundle.putString("childid", o.get("childid"));
                    IDbundle.putString("name", o.get("name"));
                    IDbundle.putString("cid", o.get("cid"));
                    IDbundle.putString("pid", o.get("pid"));
                    IDbundle.putString("vill", o.get("vill"));
                    IDbundle.putString("bari", o.get("bari"));

                    IDbundle.putString("mothercid", "");
                    IDbundle.putString("motherpid", "");
                    IDbundle.putString("mother", "");
                    IDbundle.putString("father", "");
                    IDbundle.putString("hh", "");
                    IDbundle.putString("status", "update");

                    Intent f1 = new Intent(getApplicationContext(),ChildRegistration.class);
                    f1.putExtras(IDbundle);
                    startActivity(f1);
                }
            });

            Button btnFollowUp = (Button)convertView.findViewById(R.id.btnFollowUp);
            btnFollowUp.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    FM = o.get("father");
                    if(o.get("mother").toString().length()>0)
                        FM += "/"+ o.get("mother");

                    Bundle IDbundle = new Bundle();
                    IDbundle.putString("childid", o.get("childid"));
                    IDbundle.putString("name", o.get("name"));
                    IDbundle.putString("fm", FM);
                    IDbundle.putString("agedm", AgeDayMonth);
                    IDbundle.putString("cid", o.get("vill")+o.get("bari")+o.get("hh")+o.get("sno"));
                    IDbundle.putString("pid", o.get("pid"));
                    IDbundle.putString("aged", o.get("aged"));
                    IDbundle.putString("agem", o.get("agem"));
                    IDbundle.putString("bdate", o.get("bdate"));
                    IDbundle.putString("weekno", WeekNo);
                    IDbundle.putString("visittype", "1");
                    IDbundle.putString("visitstatus", o.get("vstat").toString());
                    IDbundle.putString("village", VillageList.getSelectedItem().toString().split("-")[0]);
                    IDbundle.putString("contactno", o.get("contactno"));

                    if(Integer.valueOf(o.get("aged"))>=1826)
                    {
                        Connection.MessageBox(HouseholdIndex.this,"শিশুর বয়স ৫ বৎসরের বেশী.");
                        return;
                    }

                    Intent f1 = new Intent(getApplicationContext(),FollowUpVisit.class);
                    f1.putExtras(IDbundle);
                    startActivity(f1);
                }
            });

            Button btnAdditionalFollowUp = (Button)convertView.findViewById(R.id.btnAdditionalFollowUp);
            btnAdditionalFollowUp.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    FM = o.get("father");
                    if(o.get("mother").toString().length()>0)
                        FM += "/"+ o.get("mother");

                    VisitForm_Additional(FM,o.get("childid"),o.get("name"),AgeDayMonth,o.get("vill")+o.get("bari")+o.get("hh")+o.get("sno"),o.get("pid"),o.get("aged"),o.get("agem"),o.get("bdate"),WeekNo,"3","y");
                }
            });

            Button btnFollowUpVisit = (Button)convertView.findViewById(R.id.btnFollowUpVisit);
            btnFollowUpVisit.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    FM = o.get("father");
                    if(o.get("mother").toString().length()>0)
                        FM += "/"+ o.get("mother");

                    VisitForm_Additional(FM,o.get("childid"),o.get("name"),AgeDayMonth,o.get("vill")+o.get("bari")+o.get("hh")+o.get("sno"),o.get("pid"),o.get("aged"),o.get("agem"),o.get("bdate"),WeekNo,"2","y");
                }
            });

            LinearLayout secChildNameRow = (LinearLayout)convertView.findViewById(R.id.secChildNameRow);
            //TextView lblChildName = (TextView)convertView.findViewById(R.id.lblChildName);

            if(o.get("visit").equals("1")) {
                //ChildName.setTextColor(Color.GREEN);
                if(o.get("vstat").equals("1") | o.get("vstat").equals("2") | o.get("vstat").equals("3")) {
                    secChildNameRow.setBackgroundColor(Color.GREEN);
                    lblChildName.setTextColor(Color.BLACK);
                    ChildName.setTextColor(Color.BLACK);
                }
                else
                {
                    secChildNameRow.setBackgroundColor(Color.YELLOW);
                    lblChildName.setTextColor(Color.BLACK);
                    ChildName.setTextColor(Color.BLACK);
                }

                //enable only if weekly visit has completed
                btnAdditionalFollowUp.setEnabled(true);
                btnFollowUpVisit.setEnabled(true);

            }
            else {
                lblChildName.setTextColor(Color.parseColor("#006699"));
                ChildName.setTextColor(Color.parseColor("#006699"));
                secChildNameRow.setBackgroundColor(Color.parseColor("#FFFFFF"));

                //enable only if weekly visit has completed
                btnAdditionalFollowUp.setEnabled(false);
                btnFollowUpVisit.setEnabled(false);
            }

            TextView childStatus = (TextView)convertView.findViewById(R.id.childStatus);
            childStatus.setText("");
            childStatus.setTextColor(Color.RED);

            if(o.get("childmwra").equals("c")) {
                //if (o.get("extype").equals("4") || Integer.valueOf(o.get("aged")) > 1825) {
                if (o.get("extype").equals("5")) {
                    lblChildName.setTextColor(Color.BLUE);
                    ChildName.setTextColor(Color.BLUE);
                    btnAdditionalFollowUp.setEnabled(false);
                    btnFollowUpVisit.setEnabled(false);
                    btnFollowUp.setEnabled(true);
                    childStatus.setText("স্থানান্তর");
                } else if (o.get("extype").equals("7")) {
                    lblChildName.setTextColor(Color.BLUE);
                    ChildName.setTextColor(Color.BLUE);
                    btnAdditionalFollowUp.setEnabled(false);
                    btnFollowUpVisit.setEnabled(false);
                    btnFollowUp.setEnabled(true);
                    childStatus.setText("অসম্মতি");
                } else if (o.get("extype").equals("6")) {
                    lblChildName.setTextColor(Color.RED);
                    ChildName.setTextColor(Color.RED);
                    btnAdditionalFollowUp.setEnabled(false);
                    btnFollowUpVisit.setEnabled(false);
                    btnFollowUp.setEnabled(false);
                    childStatus.setText("মৃত্যুবরন");
                }else if (Integer.valueOf(o.get("aged")) > 1825) {
                    lblChildName.setTextColor(Color.BLUE);
                    ChildName.setTextColor(Color.BLUE);
                    btnAdditionalFollowUp.setEnabled(false);
                    btnFollowUpVisit.setEnabled(false);
                    btnFollowUp.setEnabled(false);
                    childStatus.setText("বয়স উত্তীর্ণ");
                }
                else
                {
                    btnFollowUp.setEnabled(true);
                }
            }
            else
            {
                lblChildName.setTextColor(Color.BLACK);
                ChildName.setTextColor(Color.BLACK);
            }


            Button btnReferral = (Button)convertView.findViewById(R.id.btnReferral);
            Button btnAdditionalFollowUp_R = (Button)convertView.findViewById(R.id.btnAdditionalFollowUp_R);
            Button btnFollowUpVisit_R = (Button)convertView.findViewById(R.id.btnFollowUpVisit_R);
            Button btnAbsent_Sick_R = (Button)convertView.findViewById(R.id.btnAbsent_Sick_R);

            LinearLayout secReferralButton = (LinearLayout)convertView.findViewById(R.id.secReferralButton);
            if(o.get("referral").length()==0 & o.get("referral_add").length()==0 & o.get("referral_foll").length()==0 & o.get("absent_sick").length()==0) {
                secReferralButton.setVisibility(View.GONE);
            }
            else {
                secReferralButton.setVisibility(View.VISIBLE);
                btnReferral.setVisibility(View.GONE);
                btnAdditionalFollowUp_R.setVisibility(View.GONE);
                btnFollowUpVisit_R.setVisibility(View.GONE);
                btnAbsent_Sick_R.setVisibility(View.GONE);

                if(o.get("referral").length()>0) {
                    btnReferral.setVisibility(View.VISIBLE);
                }
                if(o.get("referral_add").length()>0) {
                    btnAdditionalFollowUp_R.setVisibility(View.VISIBLE);
                }
                if(o.get("referral_foll").length()>0) {
                    btnFollowUpVisit_R.setVisibility(View.VISIBLE);
                }
                if(o.get("absent_sick").length()>0) {
                    btnAbsent_Sick_R.setVisibility(View.VISIBLE);
                }
            }



            btnReferral.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    //ChildID+","+txtWeek.getText().toString()+","+VisitType+","+txtVisit.getSelectedItem().toString()+","+Global.DateConvertYMD(dtpVDate.getText().toString());
                    String[] ref = o.get("referral").toString().split(",");
                    Bundle IDbundle = new Bundle();
                    IDbundle.putString("childid", o.get("childid"));
                    IDbundle.putString("name", o.get("name"));
                    IDbundle.putString("fm", FM);
                    IDbundle.putString("agedm", AgeDayMonth);
                    IDbundle.putString("cid", o.get("cid"));
                    IDbundle.putString("pid", o.get("pid"));
                    IDbundle.putString("aged", o.get("aged"));
                    IDbundle.putString("agem", o.get("agem"));
                    IDbundle.putString("bdate", o.get("bdate"));
                    IDbundle.putString("weekno", ref[1]);
                    IDbundle.putString("visittype", ref[2]);
                    IDbundle.putString("visitno", ref[3]);
                    IDbundle.putString("child_outside_area", "n");

                    Intent f1 = new Intent(getApplicationContext(),NonComp.class);
                    f1.putExtras(IDbundle);
                    startActivity(f1);
                }
            });

            btnAdditionalFollowUp_R.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    //ChildID+","+txtWeek.getText().toString()+","+VisitType+","+txtVisit.getSelectedItem().toString()+","+Global.DateConvertYMD(dtpVDate.getText().toString());
                    String[] ref = o.get("referral_add").toString().split(",");
                    Bundle IDbundle = new Bundle();
                    IDbundle.putString("childid", o.get("childid"));
                    IDbundle.putString("name", o.get("name"));
                    IDbundle.putString("fm", FM);
                    IDbundle.putString("agedm", AgeDayMonth);
                    IDbundle.putString("cid", o.get("cid"));
                    IDbundle.putString("pid", o.get("pid"));
                    IDbundle.putString("aged", o.get("aged"));
                    IDbundle.putString("agem", o.get("agem"));
                    IDbundle.putString("bdate", o.get("bdate"));
                    IDbundle.putString("weekno", ref[1]);
                    IDbundle.putString("visittype", ref[2]);
                    IDbundle.putString("visitno", ref[3]);
                    IDbundle.putString("child_outside_area", "n");

                    Intent f1 = new Intent(getApplicationContext(),NonComp.class);
                    f1.putExtras(IDbundle);
                    startActivity(f1);
                }
            });


            btnFollowUpVisit_R.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    //ChildID+","+txtWeek.getText().toString()+","+VisitType+","+txtVisit.getSelectedItem().toString()+","+Global.DateConvertYMD(dtpVDate.getText().toString());
                    String[] ref = o.get("referral_foll").toString().split(",");
                    Bundle IDbundle = new Bundle();
                    IDbundle.putString("childid", o.get("childid"));
                    IDbundle.putString("name", o.get("name"));
                    IDbundle.putString("fm", FM);
                    IDbundle.putString("agedm", AgeDayMonth);
                    IDbundle.putString("cid", o.get("cid"));
                    IDbundle.putString("pid", o.get("pid"));
                    IDbundle.putString("aged", o.get("aged"));
                    IDbundle.putString("agem", o.get("agem"));
                    IDbundle.putString("bdate", o.get("bdate"));
                    IDbundle.putString("weekno", ref[1]);
                    IDbundle.putString("visittype", ref[2]);
                    IDbundle.putString("visitno", ref[3]);
                    IDbundle.putString("child_outside_area", "n");

                    Intent f1 = new Intent(getApplicationContext(),NonComp.class);
                    f1.putExtras(IDbundle);
                    startActivity(f1);
                }
            });

            //24 May 2016
            btnAbsent_Sick_R.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    String[] ref = o.get("absent_sick").toString().split(",");
                    Bundle IDbundle = new Bundle();
                    IDbundle.putString("childid", o.get("childid"));
                    IDbundle.putString("name", o.get("name"));
                    IDbundle.putString("fm", FM);
                    IDbundle.putString("agedm", AgeDayMonth);
                    IDbundle.putString("cid", o.get("cid"));
                    IDbundle.putString("pid", o.get("pid"));
                    IDbundle.putString("aged", o.get("aged"));
                    IDbundle.putString("agem", o.get("agem"));
                    IDbundle.putString("bdate", o.get("bdate"));
                    IDbundle.putString("weekno", ref[1]);
                    IDbundle.putString("visittype", ref[2]);
                    IDbundle.putString("visitno", ref[3]);
                    IDbundle.putString("childpresent", "y");
                    IDbundle.putString("child_outside_area", "n");
                    //IDbundle.putString("visitdate", dtpVVDate.getText().toString());

                    Intent f1;
                    //Call Assessment based on age of child
                    if(Integer.valueOf(o.get("aged"))<=28)
                    {
                        f1 = new Intent(getApplicationContext(),AssNewBorn.class);
                        f1.putExtras(IDbundle);
                        startActivity(f1);
                    }
                    else if(Integer.valueOf(o.get("aged"))>28 & Integer.valueOf(o.get("aged"))<=1825)
                    {
                        f1 = new Intent(getApplicationContext(),AssPneu.class);
                        f1.putExtras(IDbundle);
                        startActivity(f1);
                    }
                }
            });

            LinearLayout secCommandButton = (LinearLayout)convertView.findViewById(R.id.secCommandButton);
            LinearLayout secCommandButtonMWRA = (LinearLayout)convertView.findViewById(R.id.secCommandButtonMWRA);
            LinearLayout secPregStatus = (LinearLayout)convertView.findViewById(R.id.secPregStatus);
            LinearLayout secChildDob   = (LinearLayout)convertView.findViewById(R.id.secChildDob);
            TextView PregStatus        = (TextView)convertView.findViewById(R.id.PregStatus);
            TextView DOB_MWRA          = (TextView)convertView.findViewById(R.id.DOB_MWRA);

            if(o.get("childmwra").equals("c")) {
                lblChildName.setText("শিশুর নাম");
                ChildName.setText(": " + o.get("name") + " (" + (o.get("sex").toString().equals("1") ? "ছেলে" : "মেয়ে") + ")");
                secCommandButton.setVisibility(View.VISIBLE);
                secCommandButtonMWRA.setVisibility(View.GONE);
                secPregStatus.setVisibility(View.GONE);
                secChildDob.setVisibility(View.VISIBLE);
                lblFM_Husband.setText("পিতা/মাতা");
            }
            else {
                lblChildName.setText("MWRA");
                lblFM_Husband.setText("স্বামী");
                ChildName.setText(": " + o.get("name")+" ("+ String.valueOf(o.get("aged")) +" বছর)");
                DOB_MWRA.setText(": "+ Global.DateConvertDMY(o.get("bdate").toString()));
                secCommandButton.setVisibility(View.GONE);
                secCommandButtonMWRA.setVisibility(View.VISIBLE);
                secPregStatus.setVisibility(View.VISIBLE);
                if(o.get("pstat").equals("41")) {

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar cal = Calendar.getInstance();
                    try {
                        cal.setTime(sdf.parse(o.get("lmpdt")));
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    cal.add(Calendar.DATE, 280); //minus number would decrement the days
                    String D = Global.DateConvertDMY(sdf.format(cal.getTime()));

                    PregStatus.setText("LMP:" + Global.DateConvertDMY(o.get("lmpdt")) + ", EDD:" + D +"");
                    PregStatus.setTextColor(Color.GREEN);
                }
                else{
                    PregStatus.setText("");
                }
                secChildDob.setVisibility(View.GONE);
            }

            return convertView;
        }
    }


    ListView listBari;
    public void VillageWiseBariList(Boolean heading, String Cluster, String Block,  String Village, String BariCode) {
        mScheduleBari = null;
        mScheduleChild = null;

        listBari = (ListView) findViewById(R.id.listHHIndex);
        listBari.setAdapter(null);
        mylistBari = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map;

        try {
            String BCode = ""; //BariCode.length()==0?"%":BariCode;
            String SQL = "";
            //BariCode = g.getBariCode().length()==0?BariCode:g.getBariCode();

            if (BariCode.length() > 1) {
                SQL = "Select vill,bari,bariname,bariloc from bari where Cluster='"+ Cluster +"' and Block='"+ Block +"' and Vill='"+ Village +"' and bari like('"+ BariCode +"') order by bari";
            } else {
                SQL = "Select vill,bari,bariname,bariloc from bari where Cluster='"+ Cluster +"' and Block='"+ Block +"' and Vill='"+ Village +"'  order by bari";
            }
            Cursor cur = C.ReadData(SQL);

            cur.moveToFirst();
            if (heading == true) {
                //View header = getLayoutInflater().inflate(R.layout.householdindexheading, null);
                //list.addHeaderView(header);
            }

            int i=0;

            while (!cur.isAfterLast()) {
                map = new HashMap<String, String>();
                map.put("vill", cur.getString(0));
                map.put("bari", cur.getString(1));
                map.put("bariname", cur.getString(2));
                map.put("barilocation", cur.getString(3));
                map.put("sl", String.valueOf(i));
                i+=1;
                mylistBari.add(map);

                cur.moveToNext();
            }
            cur.close();
            mScheduleBari = new SimpleAdapter(this, mylistBari, R.layout.householdindexrow,
                    new String[]{"bari"},
                    new int[]{R.id.Bari});

            lblPageHeading.setText("  বাড়ীর তালিকা ( "+String.valueOf(i)+" )");
            listBari.setAdapter(new HHListAdapter(this));

        } catch (Exception e) {
            AlertDialog.Builder adb = new AlertDialog.Builder(HouseholdIndex.this);
            adb.setTitle("Message");
            adb.setMessage(e.getMessage());
            adb.setPositiveButton("Ok", null);
            adb.show();
        }

    }



    public class HHListAdapter extends BaseAdapter {
        private Context context;

        public HHListAdapter(Context c) {
            context = c;
        }

        public int getCount() {
            return mScheduleBari.getCount();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }


        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.householdindexrow, null);
            }


            TextView Bari = (TextView) convertView.findViewById(R.id.Bari);
            TextView BariN = (TextView) convertView.findViewById(R.id.BariN);
            TextView BariLocation = (TextView) convertView.findViewById(R.id.BariLocation);

            final HashMap<String, String> o = (HashMap<String, String>) mScheduleBari.getItem(position);

            Bari.setText(": "+ o.get("bari"));
            BariN.setText(o.get("bariname"));
            BariLocation.setText(": "+ o.get("barilocation"));

            LinearLayout bariListRow = (LinearLayout)convertView.findViewById(R.id.bariListRow);
            /*if( Integer.valueOf(o.get("sl"))%2==0)
                bariListRow.setBackgroundColor(Color.WHITE);
            else
                bariListRow.setBackgroundColor(Color.parseColor("#F3F3F3"));
            */

            bariListRow.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VillageList.setEnabled(false);
                    BariList.setEnabled(true);
                    cmdBlock1.setEnabled(false);
                    cmdBlock2.setEnabled(false);
                    cmdBlock3.setEnabled(false);
                    cmdBlock4.setEnabled(false);
                    cmdBlock5.setEnabled(false);

                    lblPageHeading.setText("Children List");
                    BariList.setSelection(SpinnerItem_Position(BariList, o.get("bari")));
                    //BariWiseChildList(o.get("vill"), o.get("bari"),WeekNo);
                    cmdBackToBariList.setVisibility(View.VISIBLE);
                    cmdRefresh.setEnabled(true);
                    btnInMigration.setEnabled(true);

                    String CurrentBariNo = "";
                    if (BariList.getSelectedItemPosition() == 0)
                        CurrentBariNo = "%";
                    else {
                        String[] B = BariList.getSelectedItem().toString().split(",");
                        CurrentBariNo = B[0];
                    }


                    if (BariList.getSelectedItem().toString().trim().equalsIgnoreCase("all bari")) {
                        //g.setBariCode("");
                        if(cmdBackToBariList.isShown())
                            BariWiseChildList(Global.Left(VillageList.getSelectedItem().toString(), 3),"%",WeekNo);
                        else
                            VillageWiseBariList(false, Cluster, Block, Global.Left(VillageList.getSelectedItem().toString(), 3), "%");
                    } else {
                        //g.setBariCode(BariList.getSelectedItem().toString());
                        if(cmdBackToBariList.isShown())
                            BariWiseChildList(Global.Left(VillageList.getSelectedItem().toString(), 3),CurrentBariNo,WeekNo);
                        else
                            VillageWiseBariList(false, Cluster, Block, Global.Left(VillageList.getSelectedItem().toString(), 3), CurrentBariNo);
                    }
                }
            });

            return convertView;
        }
    }

    public int SpinnerItem_Position(Spinner spn, String Value)
    {
        int pos = 0;
        if(Value.length()!=0)
        {
            for(int i=0;i<spn.getCount();i++)
            {
                String[] b = spn.getItemAtPosition(i).toString().split(",");
                if(spn.getItemAtPosition(i).toString().length()!=0)
                {
                    if(b[0].equalsIgnoreCase(Value))
                    {
                        pos = i;
                        i   = spn.getCount();
                    }
                }
            }
        }
        return pos;
    }

    private void ShowBariForm(final String Vill, final String VillName, final String BariNo, final String Status) {
        //Status: u-update, s-save (new bari)

        final Dialog dialog = new Dialog(HouseholdIndex.this);
        dialog.setTitle("Bari Form");
        //dialog .requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bari);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        final Spinner BariList = (Spinner) findViewById(R.id.BariList);
        final TextView txtCluster = (TextView) dialog.findViewById(R.id.txtCluster);
        final Spinner txtBlock = (Spinner) dialog.findViewById(R.id.txtBlock);
        final TextView txtVill = (TextView) dialog.findViewById(R.id.txtVill);
        final EditText txtBari = (EditText) dialog.findViewById(R.id.txtBari);
        final EditText txtBName = (EditText) dialog.findViewById(R.id.txtBName);
        final EditText txtBLoc = (EditText) dialog.findViewById(R.id.txtBLoc);

        txtCluster.setText(Cluster);
        txtBlock.setAdapter(C.getArrayAdapter("Select '1' union Select '2' union Select '3' union Select '4' union Select '5'"));
        txtBlock.setSelection(Global.SpinnerItemPosition(txtBlock, 1, Block));
        if (Status.equalsIgnoreCase("s")) txtBlock.setEnabled(false);
        txtVill.setText(VillName);
        txtBari.setText(BariNo);
        //txtBari.setEnabled(false);

        if (Status.equalsIgnoreCase("u")) {
            String[] BN = BariList.getSelectedItem().toString().split(",");
            String BL = C.ReturnSingleValue("Select ifnull(BariLoc,'')BariLoc from Bari where Vill||Bari='" + (Vill + BariNo) + "'");
            txtBName.setText(BN[1].trim());
            txtBLoc.setText(BL.trim());
        }
        txtBName.requestFocus();

        Button cmdBariSave = (Button) dialog.findViewById(R.id.cmdBariSave);
        cmdBariSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (txtBName.getText().length() == 0) {
                    Connection.MessageBox(HouseholdIndex.this, "বাড়ীর নাম খালি রাখা যাবে না।");
                    return;
                }
                else if (txtBari.getText().length() < 4) {
                    Connection.MessageBox(HouseholdIndex.this, "বাড়ী নম্বর ৪ সংখ্যার কম হবেনা");
                    return;
                }
                //else if(Global.Right(txtBari.getText().toString(), 3).matches("[a-zA-z]{1}"))
                //{
                //    Connection.MessageBox(HouseholdIndex.this, "বাড়ীর নম্বর সঠিক নয়");
                //    return;
                //}
                else {
                    try {
                        //------------------------------------------------------------------
                        int latdegrees = (int) currentLatitudeNet;
                        currentLatitudeNet -= latdegrees;
                        currentLatitudeNet *= 60.;
                        if (currentLatitudeNet < 0)
                            currentLatitudeNet *= -1;
                        int latminutes = (int) currentLatitudeNet;
                        currentLatitudeNet -= latminutes;
                        currentLatitudeNet *= 60.;
                        double latseconds = currentLatitudeNet;
                        //------------------------------------------------------------------
                        int londegrees = (int) currentLongitudeNet;
                        currentLongitudeNet -= londegrees;
                        currentLongitudeNet *= 60.;
                        if (currentLongitudeNet < 0)
                            currentLongitudeNet *= -1;
                        int lonminutes = (int) currentLongitudeNet;
                        currentLongitudeNet -= lonminutes;
                        currentLongitudeNet *= 60.;
                        double lonseconds = currentLongitudeNet;
                        //------------------------------------------------------------------

                        String SQL = "";
                        int selBari = BariList.getSelectedItemPosition();

                        if (Status.equalsIgnoreCase("s")) {
                            if(C.Existence("Select Vill from Bari where Vill='"+ Vill +"' and Bari='" + txtBari.getText() + "'"))
                            {
                                Connection.MessageBox(HouseholdIndex.this,"বাড়ী নম্বর "+ txtBari.getText().toString() +" ডাটাবেজে আছে।");
                                return;
                            }

                            SQL = "Insert into Bari (Vill, Bari, BariName, BariLoc, Cluster, Block, Lat, Lon, EnDt, UserId,Upload)Values(";
                            SQL += "'" + Vill + "','" + txtBari.getText() + "','"+ txtBName.getText() +"','"+ txtBLoc.getText() +"','"+ txtCluster.getText() +"','" + txtBlock.getSelectedItem().toString() + "','','','"+ Global.DateTimeNowYMDHMS() +"','"+ g.getUserId() +"','2')";
                            C.Save(SQL);
                        } else if (Status.equalsIgnoreCase("u")) {

                            if(!txtBari.getText().toString().toUpperCase().equals(BariNo.toUpperCase()) & C.Existence("Select Vill from Bari where Vill='"+ Vill +"' and Bari='" + txtBari.getText() + "'"))
                            {
                                Connection.MessageBox(HouseholdIndex.this,"বাড়ী নম্বর "+ txtBari.getText().toString() +" ডাটাবেজে আছে।");
                                return;
                            }
                            else if (txtBari.getText().length() < 4) {
                                Connection.MessageBox(HouseholdIndex.this, "বাড়ী নম্বর ৪ সংখ্যার কম হবেনা");
                                return;
                            }
                            //Update Bari File
                            SQL = "Update Bari Set upload='2',";
                            SQL += " Bari='" + txtBari.getText() + "',BariName='" + txtBName.getText() + "',BariLoc='" + txtBLoc.getText() + "',Block='"+ txtBlock.getSelectedItem().toString() +"',Upload='2'";
                            SQL += " Where Vill='" + Vill + "' and Bari='" + BariNo + "'";
                            C.Save(SQL);

                            //Update Child File
                            C.Save("Update Child set Upload='2',Bari='" + txtBari.getText() + "' Where Vill='" + Vill + "' and Bari='" + BariNo + "'");
                        }
                        dialog.dismiss();
                        final Spinner BariList = (Spinner) findViewById(R.id.BariList);
                        BariList.setAdapter(C.getArrayAdapter("Select ' All Bari' union select trim(ifnull(bari,''))||', '||trim(ifnull(bariname,'')) from bari where cluster='" + Cluster + "' and block='" + Block + "'"));

                        try {
                            if(BariList.getCount() > selBari)
                                BariList.setSelection(selBari);
                            //BlockList(false, Global.Left(BariList.getSelectedItem().toString(),4));
                        }
                        catch(Exception ee){
                        }

                    } catch (Exception ex) {
                        Connection.MessageBox(HouseholdIndex.this, ex.getMessage());
                        return;
                    }
                }
            }
        });

        Button cmdBariClose = (Button) dialog.findViewById(R.id.cmdBariClose);
        cmdBariClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                dialog.cancel();
            }
        });

        dialog.show();
    }


    private void AssessmentForm_Outside(final String Block) {
        final Dialog dialog = new Dialog(HouseholdIndex.this);
        dialog.setTitle(" অন্য এলাকার শিশু");
        //dialog .requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.only_assessment_form);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.TOP;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        final EditText txtCID = (EditText) dialog.findViewById(R.id.txtCID);
        final EditText txtSNo = (EditText) dialog.findViewById(R.id.txtSNo);
        final EditText txtPID = (EditText) dialog.findViewById(R.id.txtPID);

        Button cmdAssSave = (Button) dialog.findViewById(R.id.cmdAssSave);
        cmdAssSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (!Connection.haveNetworkConnection(HouseholdIndex.this)) {
                    Connection.MessageBox(HouseholdIndex.this, "আইডি সার্চ করার জন্য ইন্টারনেট এর সংযোগ নেই।");
                    return;
                }
            try {
                String SQL = "select top 1 Name+','+Sex+','+(cast(YEAR(BDate) as varchar(4))+'-'+right('0'+ cast(MONTH(BDate) as varchar(2)),2)+'-'+right('0'+cast(DAY(BDate) as varchar(2)),2))+','+cast(DATEDIFF(D,BDate,getdate())as varchar(5))+','+MoName+','+FaName+','+cast(DATEDIFF(d,BDate,getdate()) as varchar(10))+','+PID from Child where Vill+Bari+HH+SNo='" + txtCID.getText().toString() + txtSNo.getText().toString() + "'";
                final String[] childinfo = C.ReturnResult("ReturnSingleValue", SQL).split(",");

                if (childinfo.length > 1) {

                } else {
                    Connection.MessageBox(HouseholdIndex.this, "শিশুটির আইডি নম্বর সঠিক নয়।");
                    return;
                }

                if(C.Existence("Select childid from Child where Vill+Bari+HH+SNo='" + txtCID.getText().toString() + txtSNo.getText().toString() + "'"))
                {
                    Connection.MessageBox(HouseholdIndex.this, "শিশুটি আপনার নিজের এলাকার শিশু।");
                    return;
                }

                Bundle IDbundle = new Bundle();
                Integer aged = Integer.valueOf(childinfo[6].toString());
                double m = Integer.valueOf(aged) / 30.44;
                double d = Integer.valueOf(aged) % 30.44;

                final String AgeDayMonth = String.valueOf((int) m) + " মাস " + String.valueOf((int) d) + "  দিন";

                IDbundle.putString("childid", txtCID.getText().toString() + txtSNo.getText().toString());
                IDbundle.putString("name", childinfo[0].toString());
                IDbundle.putString("fm", childinfo[4].toString() + "," + childinfo[5].toString());
                IDbundle.putString("agedm", AgeDayMonth);
                IDbundle.putString("cid", txtCID.getText().toString());
                //IDbundle.putString("pid", txtPID.getText().toString());
                //IDbundle.putString("pid", childinfo[7].toString());
                IDbundle.putString("pid", "");
                IDbundle.putString("aged", aged.toString());
                IDbundle.putString("agem", "");
                IDbundle.putString("bdate", childinfo[2]);
                IDbundle.putString("weekno", WeekNo);
                IDbundle.putString("visittype", "3");
                IDbundle.putString("visitno", "");
                IDbundle.putString("childpresent", "y");
                IDbundle.putString("child_outside_area", "y");

                //Call Assessment based on age of child
                if (Integer.valueOf(aged) <= 28) {
                    dialog.dismiss();
                    Intent f1 = new Intent(getApplicationContext(), AssNewBorn.class);
                    f1.putExtras(IDbundle);
                    startActivity(f1);
                } else if (Integer.valueOf(aged) > 28 & Integer.valueOf(aged) <= 1825) {
                    dialog.dismiss();
                    Intent f1 = new Intent(getApplicationContext(), AssPneu.class);
                    f1.putExtras(IDbundle);
                    startActivity(f1);
                } else if (Integer.valueOf(aged) >= 1826) {
                    Connection.MessageBox(HouseholdIndex.this, "শিশুর বয়স ৫ বৎসরের বেশী.");
                    return;
                }
            }
            catch(Exception ex)
            {
                Connection.MessageBox(HouseholdIndex.this,ex.getMessage());
                return;
            }
            }
        });

        Button cmdAssClose = (Button) dialog.findViewById(R.id.cmdAssClose);
        cmdAssClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                dialog.cancel();
            }
        });

        dialog.show();
    }


    private void ShowVisitMissingForm(final String Cluster, final String Block, final String WeekNo) {
        final Dialog dialog = new Dialog(HouseholdIndex.this);
        dialog.setTitle("Form: ভিজিট দিতে পারেনি");
        //dialog .requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.visit_not_possible);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        final TextView txtCluster = (TextView) dialog.findViewById(R.id.txtCluster);
        final TextView txtBlock = (TextView) dialog.findViewById(R.id.txtBlock);
        final TextView txtWeek = (TextView) dialog.findViewById(R.id.txtWeek);
        dtpVDate = (EditText) dialog.findViewById(R.id.dtpVDate);
        dtpVDate.setText(Global.DateNowDMY());
        final Spinner spnVstat = (Spinner) dialog.findViewById(R.id.spnVstat);

        txtCluster.setText(Cluster);
        txtBlock.setText(Block);
        txtWeek.setText(WeekNo);

        List<String> listVstat = new ArrayList<String>();

        listVstat.add("");
        listVstat.add("8-VHW ছুটিতে আছে");
        listVstat.add("13-সরকারী ছুটির দিন");
        listVstat.add("10-অপ্রত্যাশিত কারণ");
        listVstat.add("11-ঝড় / বৃষ্টির কারণ");
        listVstat.add("12-বন্যার কারণ");
        listVstat.add("15-Training/Meeting");
        listVstat.add("14-অন্যান্য কারণ");

        ArrayAdapter<String> adptrVstat= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listVstat);
        spnVstat.setAdapter(adptrVstat);

        ImageButton btnVDate = (ImageButton) dialog.findViewById(R.id.btnVDate);
        btnVDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariableID = "btnVDate";
                showDialog(DATE_DIALOG);
            }
        });

        Button cmdSave = (Button) dialog.findViewById(R.id.cmdSave);
        cmdSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                String DV = "";


                DV = Global.DateValidate(dtpVDate.getText().toString());
                if (DV.length() != 0) {
                    Connection.MessageBox(HouseholdIndex.this, DV);
                    dtpVDate.requestFocus();
                    return;
                } else if (spnVstat.getSelectedItemPosition() == 0) {
                    Connection.MessageBox(HouseholdIndex.this, "Required field:পরিদর্শনের অবস্থা.");
                    spnVstat.requestFocus();
                    return;
                }
                AlertDialog.Builder adb = new AlertDialog.Builder(HouseholdIndex.this);
                adb.setTitle("Close");
                adb.setMessage("আপনি কি ব্লক নম্বর " + Block + " এর সকল ভিজিটের ফলাফল " + spnVstat.getSelectedItem().toString() + " দেখাতে চান[Yes/No]?");
                adb.setNegativeButton("No", null);
                adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String SQL = "";
                        String ChildID = "";
                        String CID = "";
                        String PID = "";
                        String[] VS = spnVstat.getSelectedItem().toString().split("-");
                        String VisitStatus = VS[0];

                        Cursor cur = C.ReadData("Select ChildId as childid,cid as cid,pid as pid from Child c,Bari b Where c.Vill=b.Vill and c.Bari=b.Bari and ((case when c.extype='4' then '' else c.extype end) is null or length((case when c.extype='4' then '' else c.extype end))=0) and Cast(((julianday(date('now'))-julianday(c.BDate))) as int) <= 1825 and b.Cluster='" + Cluster + "' and b.Block='" + Block + "'");
                        cur.moveToFirst();
                        while (!cur.isAfterLast()) {
                            ChildID = cur.getString(cur.getColumnIndex("childid"));
                            CID  = cur.getString(cur.getColumnIndex("cid"));
                            PID  = cur.getString(cur.getColumnIndex("pid"));
                            if (!C.Existence("Select ChildId from Visits  Where ChildId='" + ChildID + "' and Week='" + WeekNo + "'")) {
                                SQL = "Insert into Visits(ChildId,CID,PID,Week,VDate,VStat,UserId,EnDt,Upload)Values('" + ChildID + "','"+ CID +"','"+ PID +"','" + WeekNo + "','" + Global.DateConvertYMD(dtpVDate.getText().toString()) + "','" + VisitStatus + "','" + g.getUserId() + "','" + Global.DateTimeNowYMDHMS() + "','2')";
                                C.Save(SQL);
                            }

                            cur.moveToNext();
                        }
                        cur.close();

                        Connection.MessageBox(HouseholdIndex.this, "Saved Successfully");


                        String CurrentBariNo = "";
                        if (BariList.getSelectedItemPosition() == 0)
                            CurrentBariNo = "%";
                        else {
                            String[] B = BariList.getSelectedItem().toString().split(",");
                            CurrentBariNo = B[0];
                        }

                        if (BariList.getSelectedItem().toString().trim().equalsIgnoreCase("all bari")) {
                            BariWiseChildList(Global.Left(VillageList.getSelectedItem().toString(), 3),"%",WeekNo);
                        } else {
                            BariWiseChildList(Global.Left(VillageList.getSelectedItem().toString(), 3),CurrentBariNo,WeekNo);
                        }
                        dialog.dismiss();
                    }
                });
                adb.show();
            }
        });

        Button cmdClose = (Button) dialog.findViewById(R.id.cmdClose);
        cmdClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                dialog.cancel();
            }
        });

        dialog.show();
    }




    protected Dialog onCreateDialog(int id) {
        final Calendar c = Calendar.getInstance();
        switch (id) {
            case DATE_DIALOG:
                return new DatePickerDialog(this, mDateSetListener,g.mYear,g.mMonth-1,g.mDay);
        }
        return null;
    }
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear+1;
            mDay = dayOfMonth;
            if(VariableID.equals("btnVDate")) {
                dtpVDate.setText(new StringBuilder()
                        .append(Global.Right("00" + mDay, 2)).append("/")
                        .append(Global.Right("00" + mMonth, 2)).append("/")
                        .append(mYear));
            }
            else if(VariableID.equals("btnVVDate")) {
                dtpVVDate.setText(new StringBuilder()
                        .append(Global.Right("00" + mDay, 2)).append("/")
                        .append(Global.Right("00" + mMonth, 2)).append("/")
                        .append(mYear));

                String AgeDay = String.valueOf(Global.DateDifferenceDays(dtpVVDate.getText().toString(),txtDOB.getText().toString()));
                double m = Integer.valueOf(AgeDay)/30.44;
                double d = Integer.valueOf(AgeDay)%30.44;

                String AgeDayMonth = String.valueOf((int)m)+" মাস "+ String.valueOf((int)d)+"  দিন";
                Age.setText(AgeDayMonth);
            }

        }
    };

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
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    void updateLocation(Location location) {
        currentLocation = location;
        currentLatitude = currentLocation.getLatitude();
        currentLongitude = currentLocation.getLongitude();
    }

    Chronometer mChronometer;
    MediaPlayer player1;
    long currentNeededTime1=60000;
    Vibrator vib;
    long elapsedTime;
    TextView txtCurrentTime;
    TextView startMsg;
    String Start;
    private void StopWatch()
    {
        final Dialog dialog = new Dialog(HouseholdIndex.this);
        try
        {
            dialog.setTitle("Clock");
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.stopwatch);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);


            //txtCurrentTime = (TextView)dialog.findViewById(R.id.txtCurrentTime);
            //startMsg = (TextView)dialog.findViewById(R.id.startMsg);
            //startMsg.setVisibility(View.VISIBLE);
            Start = "2";

            mChronometer = (Chronometer)dialog.findViewById(R.id.chronometer);
            //player1 = MediaPlayer.create(mainmenu.this, R.raw.apple_ring);
            vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

            mChronometer.start();
            Button cmdStart = (Button)dialog.findViewById(R.id.cmdStart);
            Button cmdStop  = (Button)dialog.findViewById(R.id.cmdStop);
            cmdStart.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    mChronometer.stop();
                    mChronometer.setBase(SystemClock.elapsedRealtime());
                    mChronometer.start();
                }});
            cmdStop.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    mChronometer.stop();
                }});

            //----------------
            dialog.show();

            final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);

            mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                @Override
                public void onChronometerTick(Chronometer chronometer) {
                    elapsedTime = SystemClock.elapsedRealtime()- chronometer.getBase();
                    //txtCurrentTime.setText(String.valueOf(elapsedTime));
                    if (elapsedTime <= currentNeededTime1  & Start=="1")
                    {
                        //tg.startTone(ToneGenerator.TONE_PROP_BEEP2);
                    }
                    if (elapsedTime >= 3000 & elapsedTime <=3100 & Start=="2")
                    {
                        mChronometer.stop();
                        mChronometer.setBase(SystemClock.elapsedRealtime());
                        chronometer.setVisibility(View.VISIBLE);
                        //startMsg.setVisibility(View.GONE);
                        mChronometer.start();
                        vib.vibrate(1000);
                        Start = "1";
                    }
                    else if(elapsedTime == 30000 & Start=="1")
                    {
                        tg.stopTone();
                        vib.vibrate(1000);
                    }
                    else if (elapsedTime > currentNeededTime1) {
                        tg.stopTone();
                        //tg.startTone(ToneGenerator.TONE_CDMA_KEYPAD_VOLUME_KEY_LITE);
                        vib.vibrate(1000);
                        mChronometer.stop();
                        Start = "2";
                        dialog.dismiss();
                    }
                    else
                    {
                        //if (elapsedTime <= currentNeededTime1  & Start=="1")
                            //tg.startTone(ToneGenerator.TONE_PROP_BEEP);
                    }
                }
            });

        }
        catch(Exception ex)
        {
            Connection.MessageBox(HouseholdIndex.this, ex.getMessage());
            return;
        }

    }



//Database backup to external SD card
//--------------------------------------------------------------------------------------------------
    ArrayList<String> mFileLocation = new ArrayList<String>();
    ArrayList<String> mFileName = new ArrayList<String>();
    protected static final int COPY_SUCCESS = 1;
    protected static final int COPY_FAILED = 2;
    private ProgressDialog progressDialog;
    private SharedPreferences app_preferences;

    public static final String EXT_SDCARDLOC_ForTab = "/mnt/extSdCard/";
    public static final String EXT_SDCARDLOC = "/mnt/sdcard/external_sd/";
    private long mil;
    private String FName;
    Context con;

    private String readTxt(InputStream inputStream) {

        System.out.println(inputStream);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        int i;
        try {
            i = inputStream.read();
            while (i != -1) {
                byteArrayOutputStream.write(i);
                i = inputStream.read();
            }
            inputStream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return byteArrayOutputStream.toString();
    }

    private void LoadFileList() {
        app_preferences = PreferenceManager.getDefaultSharedPreferences(this);
        AssetManager am = this.getAssets();
        try {
            InputStream is = am.open("fileshuman.json");
            //InputStream is = am.open("files_hbis.json");
            //InputStream is = am.open("files_Zoonotic.json");
//			InputStream is = am.open("files.json");
            //InputStream is = am.open("filesmidline.json");
            //InputStream is = am.open("filesScvb.json");

            String a = readTxt(is);
            JSONObject rootObject = new JSONObject(a);
            JSONArray rootArrayOfLetters = rootObject.getJSONArray("files");
            for (int lCount = 0; lCount < rootArrayOfLetters.length(); lCount++) {
                JSONArray mFileArray = rootArrayOfLetters.getJSONArray(lCount);

                JSONObject mFileNameObeject = mFileArray.getJSONObject(0);
                mFileName.add(mFileNameObeject.getString("name"));

                JSONObject mFileLocObeject = mFileArray.getJSONObject(1);
                mFileLocation.add(mFileLocObeject.getString("location"));

                //Log.e("fileNameObeject.getString",
                //        "" + mFileNameObeject.getString("name"));
                //Log.e("fileNameObeject.getString",
                //        "" + mFileLocObeject.getString("location"));
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static String getSdCardPath() {
        //	return Environment.getExternalStoragePublicDirectory(Environment.getRootDirectory()).getAbsolutePath();
        return Environment.getExternalStorageDirectory().getAbsolutePath();//EXT_SDCARDLOC_ForTab + "/";
    }
    private void takeBackup() {
        // TODO Auto-generated method stub
        mil = System.currentTimeMillis();
        //FName = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(mil), TimeUnit.MILLISECONDS.toMinutes(mil) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(mil)), TimeUnit.MILLISECONDS.toSeconds(mil) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(mil)));

        progressDialog = ProgressDialog.show(HouseholdIndex.this, "Copying...", "Please wait while finish transfering the files to external sdcard",true);
        //final ProgressDialog progDailog = ProgressDialog.show(HouseholdIndex.this, "", "অপেক্ষা করুন ...", true);
        progressDialog.setCancelable(true);

        new Thread()
        {

            public void run()
            {
                SharedPreferences.Editor editor = app_preferences
                        .edit();
                editor.putString("last_backup", mil+"");
                editor.commit(); // Very important

                for(int i=0;i<mFileLocation.size();i++){
                    try {
                        copyFileToLocation(mFileName.get(i), mFileLocation.get(i));
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        Message msg = new Message();
                        msg.what = COPY_FAILED;
                        //handler.sendMessage(msg);
                    }
                }
                Message msg = new Message();
                msg.what = COPY_SUCCESS;
                //handler.sendMessage(msg);
            }
        }.start();

    }

    public void copyFileToLocation(String fileName, String srcloc)
            throws IOException {

        final Calendar c = Calendar.getInstance();
        int dateYear = c.get(Calendar.YEAR);
        int dateMonth = c.get(Calendar.MONTH) + 1;
        int dateDay = c.get(Calendar.DAY_OF_MONTH);

        InputStream databaseInput = null;

        String inFileName = srcloc + fileName;

        String dDirName = dateDay + "-" + dateMonth + "-" + dateYear;

        String outDir = "";
        String outDatedDir = "";
        String outDatedMilDir = "";
        String outFileName = "";


		/*outDir = getSdCardPath() + "icddrbDB/";

		 outDatedDir = getSdCardPath() + "icddrbDB/" + dDirName;

		outDatedMilDir = getSdCardPath() + "icddrbDB/" + dDirName + "/"
				+ mil;

		outFileName = getSdCardPath() + "icddrbDB/" + dDirName + "/" + mil
				+ "/" + fileName;*/


        if (IsTablet() == true) {
            outDir = EXT_SDCARDLOC_ForTab + "icddrbDB/";

            outDatedDir = EXT_SDCARDLOC_ForTab + "icddrbDB/" + dDirName;

            outDatedMilDir = EXT_SDCARDLOC_ForTab + "icddrbDB/" + dDirName + "/"
                    + mil;

            outFileName = EXT_SDCARDLOC_ForTab + "icddrbDB/" + dDirName + "/" + mil
                    + "/" + fileName;
        } else {
            //get to mobile
            outDir = EXT_SDCARDLOC + "icddrbDB/";

            outDatedDir = EXT_SDCARDLOC + "icddrbDB/" + dDirName;

            outDatedMilDir = EXT_SDCARDLOC + "icddrbDB/" + dDirName + "/"
                    + mil;

            outFileName = EXT_SDCARDLOC + "icddrbDB/" + dDirName + "/" + mil
                    + "/" + fileName;

            File dir = new File(outDir);
            File dDir = new File(outDatedDir);
            if (dir.exists()) {
                if (dDir.exists()) {

                } else {
                    if (dDir.mkdirs()) {
                        Log.e("making", outDatedDir + "dir");
                    }
                }
            } else {
                if (dir.mkdirs()) {
                    Log.e("making", "dir");
                    if (dDir.mkdirs()) {
                        Log.e("making", outDatedDir + "dir");
                    }
                }
            }
            File dmDir = new File(outDatedMilDir);
            if (dmDir.exists()) {
                File file = new File(outFileName);
                if (file.exists()) {
                    if (file.delete()) {
                        Log.e("deleting", "file");
                    }
                }
            } else {
                if (dmDir.mkdirs()) {
                    Log.e("making", outDatedMilDir + "dir");
                }
            }

            OutputStream databaseOutput = new FileOutputStream(outFileName);

            byte[] buffer = new byte[1024];
            int length;
            File f = new File(inFileName);
            if (f.exists()) {
                databaseInput = new FileInputStream(f);
                while ((length = databaseInput.read(buffer)) > 0) {
                    databaseOutput.write(buffer, 0, length);
                    databaseOutput.flush();
                    Log.e("writing to ", outDatedMilDir + "");
                }
                databaseInput.close();

                databaseOutput.flush();
                databaseOutput.close();
            }

        }
    }


    private boolean IsTablet()
    {
        try {
            // Compute screen size
            DisplayMetrics dm = con.getResources().getDisplayMetrics();

            float screenWidth  = dm.widthPixels / dm.xdpi;

            float screenHeight = dm.heightPixels / dm.ydpi;

            double size = Math.sqrt(Math.pow(screenWidth, 2) +

                    Math.pow(screenHeight, 2));

            return size >= 6;

        }
        catch(Throwable t) {

            //Log.e("Failed to compute screen size", "Error");

            return false;

        }
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









    private void VisitForm_Additional(final String FM,final String childid, final String name,final  String AgeDayMonth,final  String cid, final String pid,final  String aged,final  String agem,final  String bdate,final String WeekNo,final  String VType,final  String Present)
    {
        final Dialog dialog = new Dialog(HouseholdIndex.this);
        dialog.setTitle("Form: ভিজিট");
        //dialog .requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.visitdate_form);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        if(VType.equals("3"))
            dialog.setTitle("অতিরিক্ত পরিদর্শন");
        else if(VType.equals("2"))
            dialog.setTitle("ফলোআপ");

        Age    = (TextView)dialog.findViewById(R.id.Age);
        txtDOB = (TextView)dialog.findViewById(R.id.txtDOB);
        Age.setText(AgeDayMonth);
        txtDOB.setText(Global.DateConvertDMY(bdate));
        dtpVVDate = (EditText) dialog.findViewById(R.id.dtpVVDate);
        dtpVVDate.setText(Global.DateNowDMY());

        ImageButton btnVVDate = (ImageButton) dialog.findViewById(R.id.btnVVDate);
        btnVVDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariableID = "btnVVDate";
                showDialog(DATE_DIALOG);
            }
        });

        Button cmdSave = (Button) dialog.findViewById(R.id.cmdSave);
        cmdSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                String DV = Global.DateValidate(dtpVVDate.getText().toString());
                if(DV.length()!=0)
                {
                    Connection.MessageBox(HouseholdIndex.this, DV);
                    dtpVVDate.requestFocus();
                    return;
                }

                else if(Global.DateDifferenceDays(dtpVVDate.getText().toString(), txtDOB.getText().toString())<0)
                {
                    Connection.MessageBox(HouseholdIndex.this, "ভিজিটের তারিখ অবশ্যই জন্ম তারিখের সমান অথবা বড় হতে হবে।");
                    dtpVVDate.requestFocus();
                    return;
                }

                else if(Global.DateDifferenceDays(dtpVVDate.getText().toString(), Global.DateConvertDMY(g.getWeekStartDate()))<0)
                {
                    Connection.MessageBox(HouseholdIndex.this, WeekNo + " সপ্তাহের ভিজিট অবশ্যই " + Global.DateConvertDMY(g.getWeekStartDate()) + " এবং " + Global.DateConvertDMY(g.getWeekEndDate()) +" এর মধ্যে হতে হবে।");
                    dtpVVDate.requestFocus();
                    return;
                }

                else if(Global.DateDifferenceDays(Global.DateConvertDMY(g.getWeekEndDate()),dtpVVDate.getText().toString())<0)
                {
                    Connection.MessageBox(HouseholdIndex.this, WeekNo + " সপ্তাহের ভিজিট অবশ্যই " + Global.DateConvertDMY(g.getWeekStartDate()) + " এবং " + Global.DateConvertDMY(g.getWeekEndDate()) +" এর মধ্যে হতে হবে।");
                    dtpVVDate.requestFocus();
                    return;
                }

                String AgeDay = String.valueOf(Global.DateDifferenceDays(dtpVVDate.getText().toString(),txtDOB.getText().toString()));
                double m = Integer.valueOf(AgeDay)/30.44;
                double d = Integer.valueOf(AgeDay)%30.44;

                final String AgeDayMonth = String.valueOf((int)m)+" মাস "+ String.valueOf((int)d)+"  দিন";

                Bundle IDbundle = new Bundle();
                IDbundle.putString("childid", childid);
                IDbundle.putString("name", name);
                IDbundle.putString("fm", FM);
                IDbundle.putString("agedm", AgeDayMonth);
                IDbundle.putString("cid", cid);
                IDbundle.putString("pid", pid);
                IDbundle.putString("aged", AgeDay);
                IDbundle.putString("agem", agem);
                IDbundle.putString("bdate", bdate);
                IDbundle.putString("weekno", WeekNo);
                IDbundle.putString("visittype", VType);
                IDbundle.putString("visitno", "");
                IDbundle.putString("childpresent", "y");
                IDbundle.putString("visitdate", dtpVVDate.getText().toString());
                IDbundle.putString("child_outside_area", "n");
                IDbundle.putString("village", VillageList.getSelectedItem().toString().split("-")[0]);


                //Call Assessment based on age of child
                if(Integer.valueOf(AgeDay)<=28)
                {
                    dialog.dismiss();
                    Intent f1 = new Intent(getApplicationContext(),AssNewBorn.class);
                    f1.putExtras(IDbundle);
                    startActivity(f1);
                }
                else if(Integer.valueOf(AgeDay)>28 & Integer.valueOf(AgeDay)<=1825)
                {
                    dialog.dismiss();
                    Intent f1 = new Intent(getApplicationContext(),AssPneu.class);
                    f1.putExtras(IDbundle);
                    startActivity(f1);
                }
                else if(Integer.valueOf(AgeDay)>=1826)
                {
                    Connection.MessageBox(HouseholdIndex.this,"শিশুর বয়স ৫ বৎসরের বেশী.");
                    return;
                }

            }
        });


        Button cmdClose = (Button) dialog.findViewById(R.id.cmdClose);
        cmdClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                dialog.cancel();
            }
        });


    dialog.show();

    }

}

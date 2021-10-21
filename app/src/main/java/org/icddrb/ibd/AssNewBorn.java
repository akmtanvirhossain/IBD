package org.icddrb.ibd;

/**
 * Created by TanvirHossain on 24/11/2015.
 */
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import android.app.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.ToneGenerator;
import android.os.SystemClock;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.KeyEvent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ArrayAdapter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

import Common.Connection;
import Common.Global;


public class AssNewBorn extends Activity {
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
    //Top menu
    //--------------------------------------------------------------------------------------------------
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mnuclose_timer, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog.Builder adb = new AlertDialog.Builder(AssNewBorn.this);
        switch (item.getItemId()) {
            case R.id.menuClose:
                adb.setTitle("Close");
                adb.setMessage("আপনি কি এই ফর্ম থেকে বের হতে চান[Yes/No]?");
                adb.setNegativeButton("No", null);
                adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        finish();
                    }});
                adb.show();

                return true;
            case R.id.menuTimer:
                StopWatch(60);
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
    SimpleAdapter dataAdapter;
    ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
    private String TableName;

    LinearLayout secChildId;
    TextView VlblChildId;
    TextView txtChildId;
    /*
        LinearLayout secCID;
        TextView VlblCID;
        EditText txtCID;
        LinearLayout secPID;
        TextView VlblPID;
        EditText txtPID;
    */
    LinearLayout secName;
    TextView VlblName;
    TextView txtName;

    LinearLayout secWeek;
    TextView VlblWeek;
    Spinner txtWeek;
    //LinearLayout secVType;
    //TextView VlblVType;
    //RadioGroup rdogrpVType;

    //RadioButton rdoVType1;
    //RadioButton rdoVType2;
    //RadioButton rdoVType3;
    //LinearLayout secVisit;
    TextView VlblVisit;
    Spinner txtVisit;
    LinearLayout secVDate;
    TextView VlblVDate;
    EditText dtpVDate;
    ImageButton btnVDate;
    LinearLayout secOth1;
    TextView VlblOth1;
    Spinner spnOth1;
    LinearLayout secOth2;
    TextView VlblOth2;
    Spinner spnOth2;
    LinearLayout secOth3;
    TextView VlblOth3;
    Spinner spnOth3;
    LinearLayout secHNoCry;
    TextView VlblHNoCry;
    RadioGroup rdogrpHNoCry;

    RadioButton rdoHNoCry1;
    RadioButton rdoHNoCry2;
    LinearLayout secHNoBrea;
    TextView VlblHNoBrea;
    RadioGroup rdogrpHNoBrea;

    RadioButton rdoHNoBrea1;
    RadioButton rdoHNoBrea2;
    LinearLayout secHConv;
    TextView VlblHConv;
    RadioGroup rdogrpHConv;

    RadioButton rdoHConv1;
    RadioButton rdoHConv2;
    LinearLayout secHUncon;
    TextView VlblHUncon;
    RadioGroup rdogrpHUncon;

    RadioButton rdoHUncon1;
    RadioButton rdoHUncon2;
    LinearLayout secHDBrea;
    TextView VlblHDBrea;
    RadioGroup rdogrpHDBrea;

    RadioButton rdoHDBrea1;
    RadioButton rdoHDBrea2;
    LinearLayout secHJaund;
    TextView VlblHJaund;
    RadioGroup rdogrpHJaund;

    RadioButton rdoHJaund1;
    RadioButton rdoHJaund2;
    LinearLayout secHHFever;
    TextView VlblHHFever;
    RadioGroup rdogrpHHFever;

    RadioButton rdoHHFever1;
    RadioButton rdoHHFever2;
    LinearLayout secHLFever;
    TextView VlblHLFever;
    RadioGroup rdogrpHLFever;

    RadioButton rdoHLFever1;
    RadioButton rdoHLFever2;
    LinearLayout secHSkin;
    TextView VlblHSkin;
    RadioGroup rdogrpHSkin;

    RadioButton rdoHSkin1;
    RadioButton rdoHSkin2;
    LinearLayout secHFedp;
    TextView VlblHFedp;
    RadioGroup rdogrpHFedp;

    RadioButton rdoHFedp1;
    RadioButton rdoHFedp2;
    LinearLayout secHPus;
    TextView VlblHPus;
    RadioGroup rdogrpHPus;

    RadioButton rdoHPus1;
    RadioButton rdoHPus2;
    LinearLayout secHVomit;
    TextView VlblHVomit;
    RadioGroup rdogrpHVomit;

    RadioButton rdoHVomit1;
    RadioButton rdoHVomit2;
    LinearLayout secHWeak;
    TextView VlblHWeak;
    RadioGroup rdogrpHWeak;

    RadioButton rdoHWeak1;
    RadioButton rdoHWeak2;
    LinearLayout secHLeth;
    TextView VlblHLeth;
    RadioGroup rdogrpHLeth;

    RadioButton rdoHLeth1;
    RadioButton rdoHLeth2;
    LinearLayout secAsses;
    TextView VlblAsses;
    RadioGroup rdogrpAsses;

    RadioButton rdoAsses1;
    RadioButton rdoAsses2;
    LinearLayout secAskMo;
    LinearLayout secRR1;
    TextView VlblRR1;
    EditText txtRR1;

    CheckBox chkRR;

    LinearLayout secRR2;
    TextView VlblRR2;
    EditText txtRR2;
    CheckBox chkTemp;
    LinearLayout secTemp;
    TextView VlblTemp;
    EditText txtTemp;

    LinearLayout secTemp1;
    RadioGroup rdogrpTemp1;
    RadioButton rdoTemp1;
    RadioButton rdoTemp2;
    RadioButton rdoTemp3;

    LinearLayout secNoCry;
    TextView VlblNoCry;
    RadioGroup rdogrpNoCry;

    RadioButton rdoNoCry1;
    RadioButton rdoNoCry2;
    LinearLayout secGasp;
    TextView VlblGasp;
    RadioGroup rdogrpGasp;

    RadioButton rdoGasp1;
    RadioButton rdoGasp2;
    LinearLayout secSBrea;
    TextView VlblSBrea;
    RadioGroup rdogrpSBrea;

    RadioButton rdoSBrea1;
    RadioButton rdoSBrea2;
    LinearLayout secBirthAs;
    TextView VlblBirthAs;
    RadioGroup rdogrpBirthAs;

    RadioButton rdoBirthAs1;
    RadioButton rdoBirthAs2;
    LinearLayout secConv;
    TextView VlblConv;
    RadioGroup rdogrpConv;

    RadioButton rdoConv1;
    RadioButton rdoConv2;
    LinearLayout secRBrea;
    TextView VlblRBrea;
    RadioGroup rdogrpRBrea;

    RadioButton rdoRBrea1;
    RadioButton rdoRBrea2;
    LinearLayout secCInd;
    TextView VlblCInd;
    RadioGroup rdogrpCInd;

    RadioButton rdoCInd1;
    RadioButton rdoCInd2;
    LinearLayout secHFever;
    TextView VlblHFever;
    RadioGroup rdogrpHFever;

    RadioButton rdoHFever1;
    RadioButton rdoHFever2;
    LinearLayout secHypo;
    TextView VlblHypo;
    RadioGroup rdogrpHypo;

    RadioButton rdoHypo1;
    RadioButton rdoHypo2;
    LinearLayout secUCon;
    TextView VlblUCon;
    RadioGroup rdogrpUCon;

    RadioButton rdoUCon1;
    RadioButton rdoUCon2;
    LinearLayout secPus;
    TextView VlblPus;
    RadioGroup rdogrpPus;

    RadioButton rdoPus1;
    RadioButton rdoPus2;
    LinearLayout secUmbR;
    TextView VlblUmbR;
    RadioGroup rdogrpUmbR;

    RadioButton rdoUmbR1;
    RadioButton rdoUmbR2;
    LinearLayout secWeak;
    TextView VlblWeak;
    RadioGroup rdogrpWeak;

    RadioButton rdoWeak1;
    RadioButton rdoWeak2;
    LinearLayout secLeth;
    TextView VlblLeth;
    RadioGroup rdogrpLeth;

    RadioButton rdoLeth1;
    RadioButton rdoLeth2;
    LinearLayout secNoFed;
    TextView VlblNoFed;
    RadioGroup rdogrpNoFed;

    RadioButton rdoNoFed1;
    RadioButton rdoNoFed2;
    LinearLayout secVsd;
    TextView VlblVsd;
    RadioGroup rdogrpVsd;

    RadioButton rdoVsd1;
    RadioButton rdoVsd2;
    LinearLayout secConvH;
    TextView VlblConvH;
    RadioGroup rdogrpConvH;

    RadioButton rdoConvH1;
    RadioButton rdoConvH2;
    LinearLayout secFonta;
    TextView VlblFonta;
    RadioGroup rdogrpFonta;

    RadioButton rdoFonta1;
    RadioButton rdoFonta2;
    LinearLayout secVomit;
    TextView VlblVomit;
    RadioGroup rdogrpVomit;

    RadioButton rdoVomit1;
    RadioButton rdoVomit2;
    LinearLayout secH1Fever;
    TextView VlblH1Fever;
    RadioGroup rdogrpH1Fever;

    RadioButton rdoH1Fever1;
    RadioButton rdoH1Fever2;
    LinearLayout secLFever;
    TextView VlblLFever;
    RadioGroup rdogrpLFever;

    RadioButton rdoLFever1;
    RadioButton rdoLFever2;
    LinearLayout secNJaun;
    TextView VlblNJaun;
    RadioGroup rdogrpNJaun;

    RadioButton rdoNJaun1;
    RadioButton rdoNJaun2;
    LinearLayout secPvsd;
    TextView VlblPvsd;
    RadioGroup rdogrpPvsd;

    RadioButton rdoPvsd1;
    RadioButton rdoPvsd2;
    LinearLayout secJaund;
    TextView VlblJaund;
    RadioGroup rdogrpJaund;

    RadioButton rdoJaund1;
    RadioButton rdoJaund2;
    LinearLayout secSJaun;
    TextView VlblSJaun;
    RadioGroup rdogrpSJaun;

    RadioButton rdoSJaun1;
    RadioButton rdoSJaun2;
    LinearLayout secEyeP;
    TextView VlblEyeP;
    RadioGroup rdogrpEyeP;

    RadioButton rdoEyeP1;
    RadioButton rdoEyeP2;
    LinearLayout secGono;
    TextView VlblGono;
    RadioGroup rdogrpGono;

    RadioButton rdoGono1;
    RadioButton rdoGono2;
    LinearLayout secSick;
    TextView VlblSick;
    RadioGroup rdogrpSick;

    RadioButton rdoSick1;
    RadioButton rdoSick2;
    LinearLayout secRef;
    TextView VlblRef;
    RadioGroup rdogrpRef;

    RadioButton rdoRef1;
    RadioButton rdoRef2;
    RadioButton rdoRef3;
    LinearLayout secRSlip;
    TextView VlblRSlip;
    EditText txtRSlip;

    LinearLayout secPhone;
    TextView VlblPhone;
    EditText txtPhone;

    LinearLayout secComp;
    TextView VlblComp;
    RadioGroup rdogrpComp;

    RadioButton rdoComp1;
    RadioButton rdoComp2;
    LinearLayout secReason;
    TextView VlblReason;
    RadioGroup rdogrpReason;
    //Spinner spnReason;
    RadioButton rdoReason1;
    RadioButton rdoReason2;
    RadioButton rdoReason3;
    LinearLayout secTPlace;
    TextView VlblTPlace;
    Spinner spnTPlace;
    LinearLayout secTPlaceC;
    TextView VlblTPlaceC;
    EditText txtTPlaceC;
    LinearLayout secTAbsDur;
    //LinearLayout secTAbsIn;
    TextView VlblTAbsDur;
    //TextView VlblTAbsDur;
    EditText txtTAbsDur;
    RadioGroup rdogrpTAbsIn;
    RadioButton rdoTAbsIn1;
    RadioButton rdoTAbsIn2;

    LinearLayout secHos;
    TextView VlblHos;
    RadioGroup rdogrpHos;
    RadioButton rdoHos1;
    RadioButton rdoHos2;
    RadioButton rdoHos3;

    LinearLayout secSick_lbl1;
    LinearLayout secEnDt;
    TextView VlblEnDt;
    EditText dtpEnDt;
    ImageButton btnEnDt;
    LinearLayout secUserId;
    TextView VlblUserId;
    EditText txtUserId;
    LinearLayout secUpload;
    TextView VlblUpload;
    EditText txtUpload;
    LinearLayout secUploadDT;
    TextView VlblUploadDT;
    EditText dtpUploadDT;
    ImageButton btnUploadDT;

    String StartTime;
    //String a;
    String ChildID;
//    28/07/2020
    String CID;
    String PID;
    String NAME;
    String BDATE;
    String VILLAGE;
    String CONTACT_NO;
    String CONTACT_Kum;
//
    String WeekNo;
    String VisitType;
    String VisitNo;
    String AgeM;
    String AgeD;
    String DOB;
    String ChildPresent;
    String VisitStatus;
    String Child_Outside_Area;

    TextView lblVisit;
    TextView Age;
    TextView Dob;
    String AgeDM;
    String FM;
    TextView txtFMName;
    TextView txtPID;

    String txttemp;
    String n;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            setContentView(R.layout.assnewborn);
            C = new Connection(this);
            g = Global.getInstance();
            StartTime = g.CurrentTime24();

            TableName = "AssNewBorn";

            Bundle B    = new Bundle();
            B	        = getIntent().getExtras();
            ChildID     = B.getString("childid");
//            28/07/2020
            CID     = B.getString("cid");
            PID     = B.getString("pid");
            NAME     = B.getString("name");
            FM     = B.getString("fm");
            BDATE     = B.getString("bdate");
            VILLAGE = B.getString("village");
            CONTACT_NO = B.getString("contactno");
//
            AgeD        = B.getString("aged");
            AgeM        = B.getString("agem");
            WeekNo      = B.getString("weekno");
            VisitType   = B.getString("visittype");
            VisitNo     = B.getString("visitno");
            ChildPresent= B.getString("childpresent");
            DOB         = B.getString("bdate");
            VisitStatus = B.getString("visitstatus");
            Child_Outside_Area = B.getString("child_outside_area");

            txtPID = (TextView)findViewById(R.id.txtPID);
            txtPID.setText(B.getString("pid"));

            txtVisit=(Spinner) findViewById(R.id.txtVisit);
            Age = (TextView)findViewById(R.id.Age);
            Age.setText(": "+ B.getString("agedm"));
            if(Integer.valueOf(AgeD) <= 28)
            {
                Age.setText(": "+ AgeD.toString()+" দিন");
            }
            else
            {
                Age.setText(": "+ AgeM.toString()+" মাস");
            }

            Dob = (TextView)findViewById(R.id.Dob);
            Dob.setText(": "+ Global.DateConvertDMY(DOB));

            //For new Assessment
            if(VisitNo.length()==0) {
                VisitNo = C.ReturnSingleValue("select (ifnull(max(cast(Visit as int)),0)+1) VNo from AssNewBorn where childid='" + ChildID + "' and Week='" + WeekNo + "'");
                //VisitNo = VN.equals("0") ? "0" : String.valueOf(Integer.valueOf(VN) + 1);
                String SQL = "";
                for(int i=1;i<=Integer.valueOf(VisitNo);i++)
                {
                    if(SQL.length()==0)
                        SQL += "Select '"+ String.valueOf(i) +"' ";
                    else
                        SQL += "Union Select '"+ String.valueOf(i) +"' ";
                }
                //txtVisit.setAdapter(C.getArrayAdapter("Select '1' union Select '2' union Select '3' union Select '4' union Select '5' union Select '6'"));
                txtVisit.setAdapter(C.getArrayAdapter(SQL));

                txtVisit.setSelection(Global.SpinnerItemPosition(txtVisit, 1, VisitNo));
                txtVisit.setEnabled(true);
            }
            else if(VisitNo.equals("0"))
            {
                txtVisit.setAdapter(C.getArrayAdapter("Select '0'"));
                txtVisit.setSelection(0);
            }
            else
            {
                txtVisit.setAdapter(C.getArrayAdapter("Select '1' union Select '2' union Select '3' union Select '4' union Select '5' union Select '6'"));
            }



            secSick_lbl1 = (LinearLayout)findViewById(R.id.secSick_lbl1);
            secChildId=(LinearLayout)findViewById(R.id.secChildId);
            VlblChildId=(TextView) findViewById(R.id.VlblChildId);
            txtChildId=(TextView) findViewById(R.id.txtChildId);
            txtChildId.setText(ChildID);
/*            secCID=(LinearLayout)findViewById(R.id.secCID);
            VlblCID=(TextView) findViewById(R.id.VlblCID);
            txtCID=(EditText) findViewById(R.id.txtCID);
            txtCID.setText("31300010115");
            secPID=(LinearLayout)findViewById(R.id.secPID);
            VlblPID=(TextView) findViewById(R.id.VlblPID);
            txtPID=(EditText) findViewById(R.id.txtPID);*/
            secName=(LinearLayout)findViewById(R.id.secName);
            VlblName=(TextView) findViewById(R.id.VlblName);
            txtName=(TextView) findViewById(R.id.txtName);
            txtName.setText(B.getString("name"));
            txtFMName = (TextView)findViewById(R.id.txtFMName);
            txtFMName.setText(": "+ B.getString("fm"));

            secWeek=(LinearLayout)findViewById(R.id.secWeek);
            VlblWeek=(TextView) findViewById(R.id.VlblWeek);

            txtWeek=(Spinner) findViewById(R.id.txtWeek);
            txtWeek.setAdapter(C.getArrayAdapter("select week from WeeklyVstDt order by CAST(week as int) desc limit 300"));
            txtWeek.setSelection(Global.SpinnerItemPosition(txtWeek, 3, WeekNo));
            lblVisit=(TextView) findViewById(R.id.lblVisit);

            if(VisitType.equals("1")) {
                lblVisit.setText("সাপ্তাহিক পরিদর্শন");
                txtWeek.setEnabled(false);
            }
            else if(VisitType.equals("2")) {
                lblVisit.setText("ফলোআপ পরিদর্শন");
                txtWeek.setEnabled(true);
            }
            else if(VisitType.equals("3")) {
                lblVisit.setText("অতিরিক্ত পরিদর্শন");
                txtWeek.setEnabled(true);
            }
            VlblVisit = (TextView)findViewById(R.id.VlblVisit);

            secVDate=(LinearLayout)findViewById(R.id.secVDate);
            VlblVDate=(TextView) findViewById(R.id.VlblVDate);
            dtpVDate=(EditText) findViewById(R.id.dtpVDate);
            dtpVDate.setText(B.getString("visitdate"));
            //dtpVDate.setText(Global.DateNowDMY());

            if(VisitType.equals("1"))
            {
                VlblVisit.setVisibility(View.GONE);
                txtVisit.setVisibility(View.GONE);
                txtVisit.setSelection(0);
            }
            else
            {
                VlblVisit.setVisibility(View.VISIBLE);
                txtVisit.setVisibility(View.VISIBLE);
                secVDate.setVisibility(View.VISIBLE);
            }


            secOth1=(LinearLayout)findViewById(R.id.secOth1);
            VlblOth1=(TextView) findViewById(R.id.VlblOth1);
            spnOth1=(Spinner) findViewById(R.id.spnOth1);

        /* List<String> listOth1 = new ArrayList<String>();

         listOth1.add("");
         listOth1.add("");
         ArrayAdapter<String> adptrOth1= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listOth1);
         spnOth1.setAdapter(adptrOth1);*/

            secOth2=(LinearLayout)findViewById(R.id.secOth2);
            VlblOth2=(TextView) findViewById(R.id.VlblOth2);
            spnOth2=(Spinner) findViewById(R.id.spnOth2);
            List<String> listOth2 = new ArrayList<String>();

            listOth2.add("");
            ArrayAdapter<String> adptrOth2= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listOth2);
            spnOth2.setAdapter(adptrOth2);

            secOth3=(LinearLayout)findViewById(R.id.secOth3);
            VlblOth3=(TextView) findViewById(R.id.VlblOth3);
            spnOth3=(Spinner) findViewById(R.id.spnOth3);
            List<String> listOth3 = new ArrayList<String>();

            listOth3.add("");
            ArrayAdapter<String> adptrOth3= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listOth3);
            spnOth3.setAdapter(adptrOth3);

            secHNoCry=(LinearLayout)findViewById(R.id.secHNoCry);
            VlblHNoCry = (TextView) findViewById(R.id.VlblHNoCry);
            rdogrpHNoCry = (RadioGroup) findViewById(R.id.rdogrpHNoCry);

            rdoHNoCry1 = (RadioButton) findViewById(R.id.rdoHNoCry1);
            rdoHNoCry2 = (RadioButton) findViewById(R.id.rdoHNoCry2);
            secHNoBrea=(LinearLayout)findViewById(R.id.secHNoBrea);
            VlblHNoBrea = (TextView) findViewById(R.id.VlblHNoBrea);
            rdogrpHNoBrea = (RadioGroup) findViewById(R.id.rdogrpHNoBrea);

            rdoHNoBrea1 = (RadioButton) findViewById(R.id.rdoHNoBrea1);
            rdoHNoBrea2 = (RadioButton) findViewById(R.id.rdoHNoBrea2);
            secHConv=(LinearLayout)findViewById(R.id.secHConv);
            VlblHConv = (TextView) findViewById(R.id.VlblHConv);
            rdogrpHConv = (RadioGroup) findViewById(R.id.rdogrpHConv);

            rdoHConv1 = (RadioButton) findViewById(R.id.rdoHConv1);
            rdoHConv2 = (RadioButton) findViewById(R.id.rdoHConv2);
            secHUncon=(LinearLayout)findViewById(R.id.secHUncon);
            VlblHUncon = (TextView) findViewById(R.id.VlblHUncon);
            rdogrpHUncon = (RadioGroup) findViewById(R.id.rdogrpHUncon);

            rdoHUncon1 = (RadioButton) findViewById(R.id.rdoHUncon1);
            rdoHUncon2 = (RadioButton) findViewById(R.id.rdoHUncon2);
            secHDBrea=(LinearLayout)findViewById(R.id.secHDBrea);
            VlblHDBrea = (TextView) findViewById(R.id.VlblHDBrea);
            rdogrpHDBrea = (RadioGroup) findViewById(R.id.rdogrpHDBrea);

            rdoHDBrea1 = (RadioButton) findViewById(R.id.rdoHDBrea1);
            rdoHDBrea2 = (RadioButton) findViewById(R.id.rdoHDBrea2);
            secHJaund=(LinearLayout)findViewById(R.id.secHJaund);
            VlblHJaund = (TextView) findViewById(R.id.VlblHJaund);
            rdogrpHJaund = (RadioGroup) findViewById(R.id.rdogrpHJaund);

            rdoHJaund1 = (RadioButton) findViewById(R.id.rdoHJaund1);
            rdoHJaund2 = (RadioButton) findViewById(R.id.rdoHJaund2);
            secHHFever=(LinearLayout)findViewById(R.id.secHHFever);
            VlblHHFever = (TextView) findViewById(R.id.VlblHHFever);
            rdogrpHHFever = (RadioGroup) findViewById(R.id.rdogrpHHFever);

            rdoHHFever1 = (RadioButton) findViewById(R.id.rdoHHFever1);
            rdoHHFever2 = (RadioButton) findViewById(R.id.rdoHHFever2);
            secHLFever=(LinearLayout)findViewById(R.id.secHLFever);
            VlblHLFever = (TextView) findViewById(R.id.VlblHLFever);
            rdogrpHLFever = (RadioGroup) findViewById(R.id.rdogrpHLFever);

            rdoHLFever1 = (RadioButton) findViewById(R.id.rdoHLFever1);
            rdoHLFever2 = (RadioButton) findViewById(R.id.rdoHLFever2);
            secHSkin=(LinearLayout)findViewById(R.id.secHSkin);
            VlblHSkin = (TextView) findViewById(R.id.VlblHSkin);
            rdogrpHSkin = (RadioGroup) findViewById(R.id.rdogrpHSkin);

            rdoHSkin1 = (RadioButton) findViewById(R.id.rdoHSkin1);
            rdoHSkin2 = (RadioButton) findViewById(R.id.rdoHSkin2);
            secHFedp=(LinearLayout)findViewById(R.id.secHFedp);
            VlblHFedp = (TextView) findViewById(R.id.VlblHFedp);
            rdogrpHFedp = (RadioGroup) findViewById(R.id.rdogrpHFedp);

            rdoHFedp1 = (RadioButton) findViewById(R.id.rdoHFedp1);
            rdoHFedp2 = (RadioButton) findViewById(R.id.rdoHFedp2);
            secHPus=(LinearLayout)findViewById(R.id.secHPus);
            VlblHPus = (TextView) findViewById(R.id.VlblHPus);
            rdogrpHPus = (RadioGroup) findViewById(R.id.rdogrpHPus);

            rdoHPus1 = (RadioButton) findViewById(R.id.rdoHPus1);
            rdoHPus2 = (RadioButton) findViewById(R.id.rdoHPus2);
            secHVomit=(LinearLayout)findViewById(R.id.secHVomit);
            VlblHVomit = (TextView) findViewById(R.id.VlblHVomit);
            rdogrpHVomit = (RadioGroup) findViewById(R.id.rdogrpHVomit);

            rdoHVomit1 = (RadioButton) findViewById(R.id.rdoHVomit1);
            rdoHVomit2 = (RadioButton) findViewById(R.id.rdoHVomit2);
            secHWeak=(LinearLayout)findViewById(R.id.secHWeak);
            VlblHWeak = (TextView) findViewById(R.id.VlblHWeak);
            rdogrpHWeak = (RadioGroup) findViewById(R.id.rdogrpHWeak);

            rdoHWeak1 = (RadioButton) findViewById(R.id.rdoHWeak1);
            rdoHWeak2 = (RadioButton) findViewById(R.id.rdoHWeak2);
            secHLeth=(LinearLayout)findViewById(R.id.secHLeth);
            VlblHLeth = (TextView) findViewById(R.id.VlblHLeth);
            rdogrpHLeth = (RadioGroup) findViewById(R.id.rdogrpHLeth);

            rdoHLeth1 = (RadioButton) findViewById(R.id.rdoHLeth1);
            rdoHLeth2 = (RadioButton) findViewById(R.id.rdoHLeth2);
            secAsses=(LinearLayout)findViewById(R.id.secAsses);
            VlblAsses = (TextView) findViewById(R.id.VlblAsses);
            rdogrpAsses = (RadioGroup) findViewById(R.id.rdogrpAsses);

            rdoAsses1 = (RadioButton) findViewById(R.id.rdoAsses1);
            rdoAsses2 = (RadioButton) findViewById(R.id.rdoAsses2);
            rdogrpAsses.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(RadioGroup radioGroup,int radioButtonID) {
                    RadioButton rb = (RadioButton)findViewById(rdogrpAsses.getCheckedRadioButtonId());
                    if (rb == null) return;
                    String rbData = Global.Left(rb.getText().toString(),1);
                    if(rbData.equalsIgnoreCase("2"))
                    {
                        secSick_lbl1.setVisibility(View.GONE);
                        secAskMo.setVisibility(View.GONE);
                        secRR1.setVisibility(View.GONE);
                        txtRR1.setText("");
                        secRR2.setVisibility(View.GONE);
                        txtRR2.setText("");
//                        27/07/2020
                        secTemp1.setVisibility(View.GONE);
                        rdogrpTemp1.clearCheck();
//                       -------

                        secTemp.setVisibility(View.GONE);
                        txtTemp.setText("");
                        secNoCry.setVisibility(View.GONE);
                        rdogrpNoCry.clearCheck();
                        secGasp.setVisibility(View.GONE);
                        rdogrpGasp.clearCheck();
                        secSBrea.setVisibility(View.GONE);
                        rdogrpSBrea.clearCheck();
                        secBirthAs.setVisibility(View.GONE);
                        rdogrpBirthAs.clearCheck();
                        secConv.setVisibility(View.GONE);
                        rdogrpConv.clearCheck();
                        secRBrea.setVisibility(View.GONE);
                        rdogrpRBrea.clearCheck();
                        secCInd.setVisibility(View.GONE);
                        rdogrpCInd.clearCheck();
                        secHFever.setVisibility(View.GONE);
                        rdogrpHFever.clearCheck();
                        secHypo.setVisibility(View.GONE);
                        rdogrpHypo.clearCheck();
                        secUCon.setVisibility(View.GONE);
                        rdogrpUCon.clearCheck();
                        secPus.setVisibility(View.GONE);
                        rdogrpPus.clearCheck();
                        secUmbR.setVisibility(View.GONE);
                        rdogrpUmbR.clearCheck();
                        secWeak.setVisibility(View.GONE);
                        rdogrpWeak.clearCheck();
                        secLeth.setVisibility(View.GONE);
                        rdogrpLeth.clearCheck();
                        secNoFed.setVisibility(View.GONE);
                        rdogrpNoFed.clearCheck();
                        secVsd.setVisibility(View.GONE);
                        rdogrpVsd.clearCheck();
                        secConvH.setVisibility(View.GONE);
                        rdogrpConvH.clearCheck();
                        secFonta.setVisibility(View.GONE);
                        rdogrpFonta.clearCheck();
                        secVomit.setVisibility(View.GONE);
                        rdogrpVomit.clearCheck();
                        secH1Fever.setVisibility(View.GONE);
                        rdogrpH1Fever.clearCheck();
                        secLFever.setVisibility(View.GONE);
                        rdogrpLFever.clearCheck();
                        secNJaun.setVisibility(View.GONE);
                        rdogrpNJaun.clearCheck();
                        secPvsd.setVisibility(View.GONE);
                        rdogrpPvsd.clearCheck();
                        secJaund.setVisibility(View.GONE);
                        rdogrpJaund.clearCheck();
                        secSJaun.setVisibility(View.GONE);
                        rdogrpSJaun.clearCheck();
                        secEyeP.setVisibility(View.GONE);
                        rdogrpEyeP.clearCheck();
                        secGono.setVisibility(View.GONE);
                        rdogrpGono.clearCheck();
                        secSick.setVisibility(View.GONE);
                        rdogrpSick.clearCheck();
                        secRef.setVisibility(View.GONE);
                        rdogrpRef.clearCheck();
                        secRSlip.setVisibility(View.GONE);
                        txtRSlip.setText("");
                        secPhone.setVisibility(View.GONE);
                        //txtPhone.setText("");
                        secComp.setVisibility(View.GONE);
                        rdogrpComp.clearCheck();

                        secReason.setVisibility(View.VISIBLE);
                        rdogrpReason.clearCheck();
                        secTPlace.setVisibility(View.VISIBLE);
                        secTPlaceC.setVisibility(View.GONE);
                        txtTPlaceC.setText("");
                        secTAbsDur.setVisibility(View.VISIBLE);
                        secHos.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        secSick_lbl1.setVisibility(View.VISIBLE);
                        secAskMo.setVisibility(View.VISIBLE);
                        secRR1.setVisibility(View.VISIBLE);
                        secRR2.setVisibility(View.VISIBLE);

//                        27/07/2020
                        secTemp1.setVisibility(View.VISIBLE);

//            -------

                        secTemp.setVisibility(View.VISIBLE);
                        secNoCry.setVisibility(View.VISIBLE);
                        secGasp.setVisibility(View.VISIBLE);
                        secSBrea.setVisibility(View.VISIBLE);
                        secBirthAs.setVisibility(View.VISIBLE);
                        secConv.setVisibility(View.VISIBLE);
                        secRBrea.setVisibility(View.VISIBLE);
                        secCInd.setVisibility(View.VISIBLE);
                        secHFever.setVisibility(View.VISIBLE);
                        secHypo.setVisibility(View.VISIBLE);
                        secUCon.setVisibility(View.VISIBLE);
                        secPus.setVisibility(View.VISIBLE);
                        secUmbR.setVisibility(View.VISIBLE);
                        secWeak.setVisibility(View.VISIBLE);
                        secLeth.setVisibility(View.VISIBLE);
                        secNoFed.setVisibility(View.VISIBLE);
                        secVsd.setVisibility(View.VISIBLE);
                        secConvH.setVisibility(View.VISIBLE);
                        secFonta.setVisibility(View.VISIBLE);
                        secVomit.setVisibility(View.VISIBLE);
                        secH1Fever.setVisibility(View.VISIBLE);
                        secLFever.setVisibility(View.VISIBLE);
                        secNJaun.setVisibility(View.VISIBLE);
                        secPvsd.setVisibility(View.VISIBLE);
                        secJaund.setVisibility(View.VISIBLE);
                        secSJaun.setVisibility(View.VISIBLE);
                        secEyeP.setVisibility(View.VISIBLE);
                        secGono.setVisibility(View.VISIBLE);
                        secSick.setVisibility(View.VISIBLE);
                        secRef.setVisibility(View.VISIBLE);
                        //secRSlip.setVisibility(View.VISIBLE);
                        secComp.setVisibility(View.GONE);
                        //secReason.setVisibility(View.VISIBLE);

                        secReason.setVisibility(View.GONE);
                        rdogrpReason.clearCheck();
                        secTPlace.setVisibility(View.GONE);
                        spnTPlace.setSelection(0);
                        secTPlaceC.setVisibility(View.GONE);
                        txtTPlaceC.setText("");
                        //spnTPlaceC.setSelection(0);
                        //secTAbsIn.setVisibility(View.GONE);
                        //rdogrpTAbsIn.clearCheck();
                        secTAbsDur.setVisibility(View.GONE);
                        txtTAbsDur.setText("");
                        rdogrpTAbsIn.clearCheck();
                        secHos.setVisibility(View.GONE);
                        rdogrpHos.clearCheck();
                    }
                }
                public void onNothingSelected(AdapterView<?> adapterView) {
                    return;
                }
            });



            secAskMo=(LinearLayout)findViewById(R.id.secAskMo);
            secRR1=(LinearLayout)findViewById(R.id.secRR1);
            VlblRR1=(TextView) findViewById(R.id.VlblRR1);
            txtRR1=(EditText) findViewById(R.id.txtRR1);
            txtRR1.addTextChangedListener(new TextWatcher() {
                                              public void afterTextChanged(Editable s) {
                                              }
                                              public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                              }

                                              public void onTextChanged(CharSequence s, int start, int before, int count) {



                                                  String var = txtRR1.getText().toString();
                                                  Integer rr1 = Integer.parseInt(var.length() == 0 ? "0" : var);
                                                  if(rr1<60)
                                                  {
                                                      secRR2.setVisibility(View.GONE);
                                                      txtRR2.setText("");
                                                  }
                                                  else
                                                  {
                                                      secRR2.setVisibility(View.VISIBLE);
                                                  }


                                                  return;
                                              }

                                          }

            );

            chkRR=(CheckBox)findViewById(R.id.chkRR);
            chkRR.setOnCheckedChangeListener(new OnCheckedChangeListener()
            {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    if ( isChecked )
                    {
                        txtRR1.setEnabled(false);
                        txtRR1.setText("");
                        secRR2.setVisibility(View.GONE);
                        txtRR2.setText("");
                    }
                    else
                    {
                        txtRR1.setEnabled(true);
                        secRR2.setVisibility(View.VISIBLE);
                    }

                }
            });



            secRR2=(LinearLayout)findViewById(R.id.secRR2);
            VlblRR2=(TextView) findViewById(R.id.VlblRR2);
            txtRR2=(EditText) findViewById(R.id.txtRR2);
            secTemp=(LinearLayout)findViewById(R.id.secTemp);
            VlblTemp=(TextView) findViewById(R.id.VlblTemp);
            chkTemp=(CheckBox)findViewById(R.id.chkTemp);
            chkTemp.setOnCheckedChangeListener(new OnCheckedChangeListener()
            {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    if ( isChecked )
                    {
                        txtTemp.setEnabled(false);
                        txtTemp.setText("");
                    }
                    else
                    {
                        txtTemp.setEnabled(true);
                    }

                }
            });
            txtTemp=(EditText) findViewById(R.id.txtTemp);
//            27/07/2020
            secTemp1=(LinearLayout) findViewById(R.id.secTemp1) ;
            rdogrpTemp1=(RadioGroup)findViewById(R.id.rdogrpTemp1) ;
            rdoTemp1=(RadioButton)findViewById(R.id.rdoTemp1) ;
            rdoTemp2=(RadioButton)findViewById(R.id.rdoTemp2) ;
            rdoTemp3=(RadioButton)findViewById(R.id.rdoTemp3) ;

            rdogrpTemp1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup,int radioButtonID) {
                    if(rdoTemp1.isChecked()){
                        txtTemp.setEnabled(true);
                        txtTemp.requestFocus();
                    }
                    else if(rdoTemp2.isChecked()){
                        txtTemp.setEnabled(false);
                        txtTemp.setText("");
                    }
                    else if(rdoTemp3.isChecked()){
                        txtTemp.setEnabled(false);
                        txtTemp.setText("");
                    }
                    else{
                        txtTemp.setEnabled(true);
                    }
                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                    return;
                }
            });
//


            secNoCry=(LinearLayout)findViewById(R.id.secNoCry);
            VlblNoCry = (TextView) findViewById(R.id.VlblNoCry);
            rdogrpNoCry = (RadioGroup) findViewById(R.id.rdogrpNoCry);

            rdoNoCry1 = (RadioButton) findViewById(R.id.rdoNoCry1);
            rdoNoCry2 = (RadioButton) findViewById(R.id.rdoNoCry2);
            secGasp=(LinearLayout)findViewById(R.id.secGasp);
            VlblGasp = (TextView) findViewById(R.id.VlblGasp);
            rdogrpGasp = (RadioGroup) findViewById(R.id.rdogrpGasp);

            rdoGasp1 = (RadioButton) findViewById(R.id.rdoGasp1);
            rdoGasp2 = (RadioButton) findViewById(R.id.rdoGasp2);
            secSBrea=(LinearLayout)findViewById(R.id.secSBrea);
            VlblSBrea = (TextView) findViewById(R.id.VlblSBrea);
            rdogrpSBrea = (RadioGroup) findViewById(R.id.rdogrpSBrea);

            rdoSBrea1 = (RadioButton) findViewById(R.id.rdoSBrea1);
            rdoSBrea2 = (RadioButton) findViewById(R.id.rdoSBrea2);
            secBirthAs=(LinearLayout)findViewById(R.id.secBirthAs);
            VlblBirthAs = (TextView) findViewById(R.id.VlblBirthAs);
            rdogrpBirthAs = (RadioGroup) findViewById(R.id.rdogrpBirthAs);

            rdoBirthAs1 = (RadioButton) findViewById(R.id.rdoBirthAs1);
            rdoBirthAs2 = (RadioButton) findViewById(R.id.rdoBirthAs2);
            secConv=(LinearLayout)findViewById(R.id.secConv);
            VlblConv = (TextView) findViewById(R.id.VlblConv);
            rdogrpConv = (RadioGroup) findViewById(R.id.rdogrpConv);

            rdoConv1 = (RadioButton) findViewById(R.id.rdoConv1);
            rdoConv2 = (RadioButton) findViewById(R.id.rdoConv2);
            secRBrea=(LinearLayout)findViewById(R.id.secRBrea);
            VlblRBrea = (TextView) findViewById(R.id.VlblRBrea);
            rdogrpRBrea = (RadioGroup) findViewById(R.id.rdogrpRBrea);

            rdoRBrea1 = (RadioButton) findViewById(R.id.rdoRBrea1);
            rdoRBrea2 = (RadioButton) findViewById(R.id.rdoRBrea2);
            secCInd=(LinearLayout)findViewById(R.id.secCInd);
            VlblCInd = (TextView) findViewById(R.id.VlblCInd);
            rdogrpCInd = (RadioGroup) findViewById(R.id.rdogrpCInd);

            rdoCInd1 = (RadioButton) findViewById(R.id.rdoCInd1);
            rdoCInd2 = (RadioButton) findViewById(R.id.rdoCInd2);
            secHFever=(LinearLayout)findViewById(R.id.secHFever);
            VlblHFever = (TextView) findViewById(R.id.VlblHFever);
            rdogrpHFever = (RadioGroup) findViewById(R.id.rdogrpHFever);

            rdoHFever1 = (RadioButton) findViewById(R.id.rdoHFever1);
            rdoHFever2 = (RadioButton) findViewById(R.id.rdoHFever2);
            secHypo=(LinearLayout)findViewById(R.id.secHypo);
            VlblHypo = (TextView) findViewById(R.id.VlblHypo);
            rdogrpHypo = (RadioGroup) findViewById(R.id.rdogrpHypo);

            rdoHypo1 = (RadioButton) findViewById(R.id.rdoHypo1);
            rdoHypo2 = (RadioButton) findViewById(R.id.rdoHypo2);
            secUCon=(LinearLayout)findViewById(R.id.secUCon);
            VlblUCon = (TextView) findViewById(R.id.VlblUCon);
            rdogrpUCon = (RadioGroup) findViewById(R.id.rdogrpUCon);

            rdoUCon1 = (RadioButton) findViewById(R.id.rdoUCon1);
            rdoUCon2 = (RadioButton) findViewById(R.id.rdoUCon2);
            secPus=(LinearLayout)findViewById(R.id.secPus);
            VlblPus = (TextView) findViewById(R.id.VlblPus);
            rdogrpPus = (RadioGroup) findViewById(R.id.rdogrpPus);

            rdoPus1 = (RadioButton) findViewById(R.id.rdoPus1);
            rdoPus2 = (RadioButton) findViewById(R.id.rdoPus2);
            secUmbR=(LinearLayout)findViewById(R.id.secUmbR);
            VlblUmbR = (TextView) findViewById(R.id.VlblUmbR);
            rdogrpUmbR = (RadioGroup) findViewById(R.id.rdogrpUmbR);

            rdoUmbR1 = (RadioButton) findViewById(R.id.rdoUmbR1);
            rdoUmbR2 = (RadioButton) findViewById(R.id.rdoUmbR2);
            secWeak=(LinearLayout)findViewById(R.id.secWeak);
            VlblWeak = (TextView) findViewById(R.id.VlblWeak);
            rdogrpWeak = (RadioGroup) findViewById(R.id.rdogrpWeak);

            rdoWeak1 = (RadioButton) findViewById(R.id.rdoWeak1);
            rdoWeak2 = (RadioButton) findViewById(R.id.rdoWeak2);
            secLeth=(LinearLayout)findViewById(R.id.secLeth);
            VlblLeth = (TextView) findViewById(R.id.VlblLeth);
            rdogrpLeth = (RadioGroup) findViewById(R.id.rdogrpLeth);

            rdoLeth1 = (RadioButton) findViewById(R.id.rdoLeth1);
            rdoLeth2 = (RadioButton) findViewById(R.id.rdoLeth2);
            secNoFed=(LinearLayout)findViewById(R.id.secNoFed);
            VlblNoFed = (TextView) findViewById(R.id.VlblNoFed);
            rdogrpNoFed = (RadioGroup) findViewById(R.id.rdogrpNoFed);

            rdoNoFed1 = (RadioButton) findViewById(R.id.rdoNoFed1);
            rdoNoFed2 = (RadioButton) findViewById(R.id.rdoNoFed2);
            secVsd=(LinearLayout)findViewById(R.id.secVsd);
            VlblVsd = (TextView) findViewById(R.id.VlblVsd);
            rdogrpVsd = (RadioGroup) findViewById(R.id.rdogrpVsd);

            rdoVsd1 = (RadioButton) findViewById(R.id.rdoVsd1);
            rdoVsd2 = (RadioButton) findViewById(R.id.rdoVsd2);
            secConvH=(LinearLayout)findViewById(R.id.secConvH);
            VlblConvH = (TextView) findViewById(R.id.VlblConvH);
            rdogrpConvH = (RadioGroup) findViewById(R.id.rdogrpConvH);

            rdoConvH1 = (RadioButton) findViewById(R.id.rdoConvH1);
            rdoConvH2 = (RadioButton) findViewById(R.id.rdoConvH2);
            secFonta=(LinearLayout)findViewById(R.id.secFonta);
            VlblFonta = (TextView) findViewById(R.id.VlblFonta);
            rdogrpFonta = (RadioGroup) findViewById(R.id.rdogrpFonta);

            rdoFonta1 = (RadioButton) findViewById(R.id.rdoFonta1);
            rdoFonta2 = (RadioButton) findViewById(R.id.rdoFonta2);
            secVomit=(LinearLayout)findViewById(R.id.secVomit);
            VlblVomit = (TextView) findViewById(R.id.VlblVomit);
            rdogrpVomit = (RadioGroup) findViewById(R.id.rdogrpVomit);

            rdoVomit1 = (RadioButton) findViewById(R.id.rdoVomit1);
            rdoVomit2 = (RadioButton) findViewById(R.id.rdoVomit2);
            secH1Fever=(LinearLayout)findViewById(R.id.secH1Fever);
            VlblH1Fever = (TextView) findViewById(R.id.VlblH1Fever);
            rdogrpH1Fever = (RadioGroup) findViewById(R.id.rdogrpH1Fever);

            rdoH1Fever1 = (RadioButton) findViewById(R.id.rdoH1Fever1);
            rdoH1Fever2 = (RadioButton) findViewById(R.id.rdoH1Fever2);
            secLFever=(LinearLayout)findViewById(R.id.secLFever);
            VlblLFever = (TextView) findViewById(R.id.VlblLFever);
            rdogrpLFever = (RadioGroup) findViewById(R.id.rdogrpLFever);

            rdoLFever1 = (RadioButton) findViewById(R.id.rdoLFever1);
            rdoLFever2 = (RadioButton) findViewById(R.id.rdoLFever2);
            secNJaun=(LinearLayout)findViewById(R.id.secNJaun);
            VlblNJaun = (TextView) findViewById(R.id.VlblNJaun);
            rdogrpNJaun = (RadioGroup) findViewById(R.id.rdogrpNJaun);

            rdoNJaun1 = (RadioButton) findViewById(R.id.rdoNJaun1);
            rdoNJaun2 = (RadioButton) findViewById(R.id.rdoNJaun2);
            secPvsd=(LinearLayout)findViewById(R.id.secPvsd);
            VlblPvsd = (TextView) findViewById(R.id.VlblPvsd);
            rdogrpPvsd = (RadioGroup) findViewById(R.id.rdogrpPvsd);

            rdoPvsd1 = (RadioButton) findViewById(R.id.rdoPvsd1);
            rdoPvsd2 = (RadioButton) findViewById(R.id.rdoPvsd2);
            secJaund=(LinearLayout)findViewById(R.id.secJaund);
            VlblJaund = (TextView) findViewById(R.id.VlblJaund);
            rdogrpJaund = (RadioGroup) findViewById(R.id.rdogrpJaund);

            rdoJaund1 = (RadioButton) findViewById(R.id.rdoJaund1);
            rdoJaund2 = (RadioButton) findViewById(R.id.rdoJaund2);
            secSJaun=(LinearLayout)findViewById(R.id.secSJaun);
            VlblSJaun = (TextView) findViewById(R.id.VlblSJaun);
            rdogrpSJaun = (RadioGroup) findViewById(R.id.rdogrpSJaun);

            rdoSJaun1 = (RadioButton) findViewById(R.id.rdoSJaun1);
            rdoSJaun2 = (RadioButton) findViewById(R.id.rdoSJaun2);
            secEyeP=(LinearLayout)findViewById(R.id.secEyeP);
            VlblEyeP = (TextView) findViewById(R.id.VlblEyeP);
            rdogrpEyeP = (RadioGroup) findViewById(R.id.rdogrpEyeP);

            rdoEyeP1 = (RadioButton) findViewById(R.id.rdoEyeP1);
            rdoEyeP2 = (RadioButton) findViewById(R.id.rdoEyeP2);
            secGono=(LinearLayout)findViewById(R.id.secGono);
            VlblGono = (TextView) findViewById(R.id.VlblGono);
            rdogrpGono = (RadioGroup) findViewById(R.id.rdogrpGono);

            rdoGono1 = (RadioButton) findViewById(R.id.rdoGono1);
            rdoGono2 = (RadioButton) findViewById(R.id.rdoGono2);
            secSick=(LinearLayout)findViewById(R.id.secSick);
            VlblSick = (TextView) findViewById(R.id.VlblSick);
            rdogrpSick = (RadioGroup) findViewById(R.id.rdogrpSick);

            rdoSick1 = (RadioButton) findViewById(R.id.rdoSick1);
            rdoSick2 = (RadioButton) findViewById(R.id.rdoSick2);
            secRef=(LinearLayout)findViewById(R.id.secRef);
            VlblRef = (TextView) findViewById(R.id.VlblRef);
            rdogrpRef = (RadioGroup) findViewById(R.id.rdogrpRef);

            rdoRef1 = (RadioButton) findViewById(R.id.rdoRef1);
            rdoRef2 = (RadioButton) findViewById(R.id.rdoRef2);
            rdoRef3 = (RadioButton) findViewById(R.id.rdoRef3);
            rdogrpRef.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(RadioGroup radioGroup,int radioButtonID) {
                    RadioButton rb = (RadioButton)findViewById(rdogrpRef.getCheckedRadioButtonId());
                    if (rb == null) return;
                    String rbData = Global.Left(rb.getText().toString(),1);
                    if(rbData.equalsIgnoreCase("3"))
                    {
                        secRSlip.setVisibility(View.GONE);
                        txtRSlip.setText("");
                        secPhone.setVisibility(View.GONE);
                        //txtPhone.setText("");
                        secComp.setVisibility(View.GONE);
                        rdogrpComp.clearCheck();
                        secReason.setVisibility(View.GONE);
                        //spnReason.setSelection(0);
                        rdogrpReason.clearCheck();
                        secTPlace.setVisibility(View.GONE);
                        spnTPlace.setSelection(0);
                        secTPlaceC.setVisibility(View.GONE);
                        //spnTPlaceC.setSelection(0);
                        txtTPlaceC.setText("");
                        secTAbsDur.setVisibility(View.GONE);
                        txtTAbsDur.setText("");
                        rdogrpTAbsIn.clearCheck();
                        secHos.setVisibility(View.GONE);
                        rdogrpHos.clearCheck();
                    }
                    else if(rbData.equalsIgnoreCase("1") | rbData.equalsIgnoreCase("2"))
                    {
                        secRSlip.setVisibility(View.VISIBLE);
                        secPhone.setVisibility(View.VISIBLE);
                        secComp.setVisibility(View.GONE);
                        if(rdoAsses2.isChecked()) {
                            secReason.setVisibility(View.VISIBLE);
                            secTPlace.setVisibility(View.VISIBLE);
                            secTPlaceC.setVisibility(View.VISIBLE);
                            //secTAbsIn.setVisibility(View.VISIBLE);
                            secTAbsDur.setVisibility(View.VISIBLE);
                            secHos.setVisibility(View.VISIBLE);
                        }else if(rdoAsses1.isChecked())
                        {
                            secReason.setVisibility(View.GONE);
                            //spnReason.setSelection(0);
                            rdogrpReason.clearCheck();
                            secTPlace.setVisibility(View.GONE);
                            spnTPlace.setSelection(0);
                            secTPlaceC.setVisibility(View.GONE);
                            //spnTPlaceC.setSelection(0);
                            txtTPlaceC.setText("");
                            secTAbsDur.setVisibility(View.GONE);
                            rdogrpTAbsIn.clearCheck();
                            txtTAbsDur.setText("");
                            secHos.setVisibility(View.GONE);
                            rdogrpHos.clearCheck();
                        }
                    }
                }
                public void onNothingSelected(AdapterView<?> adapterView) {
                    return;
                }
            });
            secRSlip=(LinearLayout)findViewById(R.id.secRSlip);
            VlblRSlip=(TextView) findViewById(R.id.VlblRSlip);
            txtRSlip=(EditText) findViewById(R.id.txtRSlip);
            secPhone=(LinearLayout)findViewById(R.id.secPhone);
            VlblPhone=(TextView) findViewById(R.id.VlblPhone);
            txtPhone=(EditText) findViewById(R.id.txtPhone);
            secComp=(LinearLayout)findViewById(R.id.secComp);
            VlblComp = (TextView) findViewById(R.id.VlblComp);
            rdogrpComp = (RadioGroup) findViewById(R.id.rdogrpComp);

            rdoComp1 = (RadioButton) findViewById(R.id.rdoComp1);
            rdoComp2 = (RadioButton) findViewById(R.id.rdoComp2);
            rdogrpComp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(RadioGroup radioGroup,int radioButtonID) {
                    RadioButton rb = (RadioButton)findViewById(rdogrpComp.getCheckedRadioButtonId());
                    if (rb == null) return;
                    String rbData = Global.Left(rb.getText().toString(),1);
                    if(rbData.equalsIgnoreCase("1"))
                    {
                        secReason.setVisibility(View.GONE);
                        //spnReason.setSelection(0);
                        rdogrpReason.clearCheck();
                        secTPlace.setVisibility(View.GONE);
                        spnTPlace.setSelection(0);
                        secTPlaceC.setVisibility(View.GONE);
                        //spnTPlaceC.setSelection(0);
                        txtTPlaceC.setText("");
                        secTAbsDur.setVisibility(View.GONE);
                        txtTAbsDur.setText("");
                        rdogrpTAbsIn.clearCheck();
                        secHos.setVisibility(View.GONE);
                        rdogrpHos.clearCheck();
                    }
                    else if(rbData.equalsIgnoreCase("2"))
                    {
                        secReason.setVisibility(View.GONE);
                        //spnReason.setSelection(0);
                        rdogrpReason.clearCheck();
                        secTPlace.setVisibility(View.GONE);
                        spnTPlace.setSelection(0);
                        secTPlaceC.setVisibility(View.GONE);
                        //spnTPlaceC.setSelection(0);
                        txtTPlaceC.setText("");
                        secTAbsDur.setVisibility(View.GONE);
                        txtTAbsDur.setText("");
                        rdogrpTAbsIn.clearCheck();
                        secHos.setVisibility(View.GONE);
                        rdogrpHos.clearCheck();
                    }
                    else
                    {
                        secReason.setVisibility(View.VISIBLE);
                        secTPlace.setVisibility(View.VISIBLE);
                        secTPlaceC.setVisibility(View.VISIBLE);
                        secTAbsDur.setVisibility(View.VISIBLE);
                        secHos.setVisibility(View.VISIBLE);
                    }
/*                 else
                 {
                    secReason.setVisibility(View.VISIBLE);
                    secTPlace.setVisibility(View.VISIBLE);
                    secTPlaceC.setVisibility(View.VISIBLE);
                    secTAbsIn.setVisibility(View.VISIBLE);
                    secTAbsDur.setVisibility(View.VISIBLE);
                    secHos.setVisibility(View.VISIBLE);
                 }
 */             }
                public void onNothingSelected(AdapterView<?> adapterView) {
                    return;
                }
            });
            secReason=(LinearLayout)findViewById(R.id.secReason);
            VlblReason=(TextView) findViewById(R.id.VlblReason);
            rdogrpReason = (RadioGroup) findViewById(R.id.rdogrpReason);

            rdoReason1 = (RadioButton) findViewById(R.id.rdoReason1);
            rdoReason2 = (RadioButton) findViewById(R.id.rdoReason2);
            rdoReason3 = (RadioButton) findViewById(R.id.rdoReason3);
            rdogrpReason.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int radioButtonID) {
                    RadioButton rb = (RadioButton) findViewById(rdogrpReason.getCheckedRadioButtonId());
                    if (rb == null) return;
                    String rbData = Global.Left(rb.getText().toString(), 1);
                    if (rbData.equalsIgnoreCase("2")) {
                        secTPlace.setVisibility(View.VISIBLE);
                        //secTPlaceC.setVisibility(View.VISIBLE);
                        secTAbsDur.setVisibility(View.VISIBLE);
                        secHos.setVisibility(View.VISIBLE);

                    } else {
                        secTPlace.setVisibility(View.GONE);
                        spnTPlace.setSelection(0);
                        secTPlaceC.setVisibility(View.GONE);
                        txtTPlaceC.setText("");
                        secTAbsDur.setVisibility(View.GONE);
                        txtTAbsDur.setText("");
                        secHos.setVisibility(View.GONE);
                        rdogrpHos.clearCheck();
                    }
                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                    return;
                }
            });

            secTPlace=(LinearLayout)findViewById(R.id.secTPlace);
            VlblTPlace=(TextView) findViewById(R.id.VlblTPlace);
            spnTPlace=(Spinner) findViewById(R.id.spnTPlace);
            List<String> listTPlace = new ArrayList<String>();

            listTPlace.add("");
            listTPlace.add("1-কুমুদিনি হাসপাতাল");
            listTPlace.add("2-অন্যান্য হাসপাতাল, ক্লিনিক");
            listTPlace.add("3-পাশকরা ডাক্তার");
            listTPlace.add("4-গ্রাম ডাক্তার");
            listTPlace.add("5-অন্যান্য");
            ArrayAdapter<String> adptrTPlace= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listTPlace);
            spnTPlace.setAdapter(adptrTPlace);

            spnTPlace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView,
                                           View selectedItemView, int position, long id) {
                    if (spnTPlace.getSelectedItem().toString().length() == 0)
                        return;
                    String spnData = Global.Left(spnTPlace.getSelectedItem().toString(), 1);
                    if (spnData.equalsIgnoreCase("5")) {
                        secTPlaceC.setVisibility(View.VISIBLE);

                    } else {
                        secTPlaceC.setVisibility(View.GONE);
                        txtTPlaceC.setText("");
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }
            });
            secTPlaceC=(LinearLayout)findViewById(R.id.secTPlaceC);
            VlblTPlaceC=(TextView) findViewById(R.id.VlblTPlaceC);
            txtTPlaceC=(EditText) findViewById(R.id.txtTPlaceC);
/*            spnTPlaceC=(Spinner) findViewById(R.id.spnTPlaceC);
            List<String> listTPlaceC = new ArrayList<String>();

            listTPlaceC.add("");
            listTPlaceC.add("1-ফার্মেসি");
            ArrayAdapter<String> adptrTPlaceC= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listTPlaceC);

            spnTPlaceC.setAdapter(adptrTPlaceC);*/

            secTAbsDur=(LinearLayout)findViewById(R.id.secTAbsDur);
            VlblTAbsDur = (TextView) findViewById(R.id.VlblTAbsDur);
            txtTAbsDur=(EditText) findViewById(R.id.txtTAbsDur);
            rdogrpTAbsIn = (RadioGroup) findViewById(R.id.rdogrpTAbsIn);
            rdoTAbsIn1 = (RadioButton) findViewById(R.id.rdoTAbsIn1);
            rdoTAbsIn2 = (RadioButton) findViewById(R.id.rdoTAbsIn2);

            secHos=(LinearLayout)findViewById(R.id.secHos);
            VlblHos = (TextView) findViewById(R.id.VlblHos);
            rdogrpHos = (RadioGroup) findViewById(R.id.rdogrpHos);
            rdoHos1 = (RadioButton) findViewById(R.id.rdoHos1);
            rdoHos2 = (RadioButton) findViewById(R.id.rdoHos2);
            rdoHos3 = (RadioButton) findViewById(R.id.rdoHos3);

/*            spnReason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    if (spnReason.getSelectedItem().toString().length() == 0) return;
                    String spnData = Global.Left(spnReason.getSelectedItem().toString(), 1);
                    if (spnData.equalsIgnoreCase("2")) {
                        secTPlace.setVisibility(View.VISIBLE);
                        secTAbsIn.setVisibility(View.VISIBLE);
                        secTAbsDur.setVisibility(View.VISIBLE);
                        secHos.setVisibility(View.VISIBLE);
                    } else {
                        secTPlace.setVisibility(View.GONE);
                        spnTPlace.setSelection(0);
                        secTPlaceC.setVisibility(View.GONE);
                        spnTPlaceC.setSelection(0);
                        secTAbsIn.setVisibility(View.GONE);
                        rdogrpTAbsIn.clearCheck();
                        secTAbsDur.setVisibility(View.GONE);
                        txtTAbsDur.setText("");
                        secHos.setVisibility(View.GONE);
                        rdogrpHos.clearCheck();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }
            });*/



         /*secEnDt=(LinearLayout)findViewById(R.id.secEnDt);
         VlblEnDt=(TextView) findViewById(R.id.VlblEnDt);
         dtpEnDt=(EditText) findViewById(R.id.dtpEnDt);
         secUserId=(LinearLayout)findViewById(R.id.secUserId);
         VlblUserId=(TextView) findViewById(R.id.VlblUserId);
         txtUserId=(EditText) findViewById(R.id.txtUserId);
         secUpload=(LinearLayout)findViewById(R.id.secUpload);
         VlblUpload=(TextView) findViewById(R.id.VlblUpload);
         txtUpload=(EditText) findViewById(R.id.txtUpload);
         secUploadDT=(LinearLayout)findViewById(R.id.secUploadDT);
         VlblUploadDT=(TextView) findViewById(R.id.VlblUploadDT);
         dtpUploadDT=(EditText) findViewById(R.id.dtpUploadDT);
*/


            btnVDate = (ImageButton) findViewById(R.id.btnVDate);
            btnVDate.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VariableID = "btnVDate";
                    showDialog(DATE_DIALOG);
                }
            });


            // form load
            secSick_lbl1.setVisibility(View.GONE);
            secAskMo.setVisibility(View.GONE);
            secRR1.setVisibility(View.GONE);
            txtRR1.setText("");
            secRR2.setVisibility(View.GONE);
            txtRR2.setText("");

//            27/07/2020
            secTemp1.setVisibility(View.GONE);
            rdogrpTemp1.clearCheck();
//            -------

            secTemp.setVisibility(View.GONE);
            txtTemp.setText("");
            secNoCry.setVisibility(View.GONE);
            rdogrpNoCry.clearCheck();
            secGasp.setVisibility(View.GONE);
            rdogrpGasp.clearCheck();
            secSBrea.setVisibility(View.GONE);
            rdogrpSBrea.clearCheck();
            secBirthAs.setVisibility(View.GONE);
            rdogrpBirthAs.clearCheck();
            secConv.setVisibility(View.GONE);
            rdogrpConv.clearCheck();
            secRBrea.setVisibility(View.GONE);
            rdogrpRBrea.clearCheck();
            secCInd.setVisibility(View.GONE);
            rdogrpCInd.clearCheck();
            secHFever.setVisibility(View.GONE);
            rdogrpHFever.clearCheck();
            secHypo.setVisibility(View.GONE);
            rdogrpHypo.clearCheck();
            secUCon.setVisibility(View.GONE);
            rdogrpUCon.clearCheck();
            secPus.setVisibility(View.GONE);
            rdogrpPus.clearCheck();
            secUmbR.setVisibility(View.GONE);
            rdogrpUmbR.clearCheck();
            secWeak.setVisibility(View.GONE);
            rdogrpWeak.clearCheck();
            secLeth.setVisibility(View.GONE);
            rdogrpLeth.clearCheck();
            secNoFed.setVisibility(View.GONE);
            rdogrpNoFed.clearCheck();
            secVsd.setVisibility(View.GONE);
            rdogrpVsd.clearCheck();
            secConvH.setVisibility(View.GONE);
            rdogrpConvH.clearCheck();
            secFonta.setVisibility(View.GONE);
            rdogrpFonta.clearCheck();
            secVomit.setVisibility(View.GONE);
            rdogrpVomit.clearCheck();
            secH1Fever.setVisibility(View.GONE);
            rdogrpH1Fever.clearCheck();
            secLFever.setVisibility(View.GONE);
            rdogrpLFever.clearCheck();
            secNJaun.setVisibility(View.GONE);
            rdogrpNJaun.clearCheck();
            secPvsd.setVisibility(View.GONE);
            rdogrpPvsd.clearCheck();
            secJaund.setVisibility(View.GONE);
            rdogrpJaund.clearCheck();
            secSJaun.setVisibility(View.GONE);
            rdogrpSJaun.clearCheck();
            secEyeP.setVisibility(View.GONE);
            rdogrpEyeP.clearCheck();
            secGono.setVisibility(View.GONE);
            rdogrpGono.clearCheck();
            secSick.setVisibility(View.GONE);
            rdogrpSick.clearCheck();
            secRef.setVisibility(View.GONE);
            rdogrpRef.clearCheck();
            secRSlip.setVisibility(View.GONE);
            txtRSlip.setText("");
            secPhone.setVisibility(View.GONE);
            //txtPhone.setText("");
            secComp.setVisibility(View.GONE);
            rdogrpComp.clearCheck();
            //--------
            secReason.setVisibility(View.GONE);
            rdogrpReason.clearCheck();
            secTPlace.setVisibility(View.GONE);
            spnTPlace.setSelection(0);
            secTPlaceC.setVisibility(View.GONE);
            txtTPlaceC.setText("");
            //secTAbsIn.setVisibility(View.GONE);
            //rdogrpTAbsIn.clearCheck();
            secTAbsDur.setVisibility(View.GONE);
            txtTAbsDur.setText("");
            secHos.setVisibility(View.GONE);
            rdogrpHos.clearCheck();

            Oth1();
            Oth2();
            Oth3();

            txtVisit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    ClearForm();
                    DataSearch(ChildID, WeekNo, VisitType, txtVisit.getSelectedItem().toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }
            });


            DataSearch(ChildID, WeekNo,VisitType,VisitNo);
            DataSearchPhone(ChildID);
            Button cmdSave = (Button) findViewById(R.id.cmdSave);
            cmdSave.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    DataSave();
                }});

            if(ChildPresent.equals("y"))
            {
                //rdogrpAsses.clearCheck();
                rdoAsses1.setEnabled(true);
                rdoAsses2.setEnabled(true);
            }
            else
            {
                rdoAsses2.setChecked(true);
                rdoAsses1.setEnabled(false);
                rdoAsses2.setEnabled(false);
            }

        }
        catch(Exception  e)
        {
            Connection.MessageBox(AssNewBorn.this, e.getMessage());
            return;
        }
    }

    //Code list
    String FName="AssNewBorn";
    private void Oth1() {
        Spinner Oth1 = (Spinner) findViewById(R.id.spnOth1);
        Oth1.setAdapter(C.getArrayAdapter("Select '' VarCodeDes from CodeList union  Select varCode|| '-' ||varDes  From CodeList where FName='"+ FName +"' and VarName='Oth1'"));
    }
    private void Oth2() {
        Spinner Oth2 = (Spinner) findViewById(R.id.spnOth2);
        Oth2.setAdapter(C.getArrayAdapter("Select '' VarCodeDes from CodeList union  Select varCode|| '-' ||varDes  From CodeList where FName='"+ FName +"' and VarName='Oth2'"));
    }
    private void Oth3() {
        Spinner Oth3 = (Spinner) findViewById(R.id.spnOth3);
        Oth3.setAdapter(C.getArrayAdapter("Select '' VarCodeDes from CodeList union  Select varCode|| '-' ||varDes  From CodeList where FName='"+ FName +"' and VarName='Oth3'"));
    }


    private void ClearForm()
    {
        txtTemp.setText("");
        dtpVDate.setText("");
        spnOth1.setSelection(0);
        spnOth2.setSelection(0);
        spnOth3.setSelection(0);

        rdogrpHNoCry.clearCheck();
        rdogrpHNoBrea.clearCheck();
        rdogrpHConv.clearCheck();
        rdogrpHUncon.clearCheck();
        rdogrpHDBrea.clearCheck();
        rdogrpHJaund.clearCheck();
        rdogrpHHFever.clearCheck();
        rdogrpHLFever.clearCheck();
        rdogrpHSkin.clearCheck();
        rdogrpHFedp.clearCheck();
        rdogrpHPus.clearCheck();
        rdogrpHVomit.clearCheck();
        rdogrpHWeak.clearCheck();
        rdogrpHLeth.clearCheck();
        rdogrpAsses.clearCheck();
        txtRR1.setText("");
        txtRR2.setText("");
        rdogrpNoCry.clearCheck();
        rdogrpGasp.clearCheck();
        rdogrpSBrea.clearCheck();
        rdogrpBirthAs.clearCheck();
        rdogrpConv.clearCheck();
        rdogrpRBrea.clearCheck();
        rdogrpCInd.clearCheck();
        rdogrpHFever.clearCheck();
        rdogrpHypo.clearCheck();
        rdogrpUCon.clearCheck();
        rdogrpPus.clearCheck();
        rdogrpUmbR.clearCheck();
        rdogrpWeak.clearCheck();
        rdogrpLeth.clearCheck();
        rdogrpNoFed.clearCheck();
        rdogrpVsd.clearCheck();
        rdogrpConvH.clearCheck();
        rdogrpFonta.clearCheck();
        rdogrpVomit.clearCheck();
        rdogrpH1Fever.clearCheck();
        rdogrpLFever.clearCheck();
        rdogrpNJaun.clearCheck();
        rdogrpPvsd.clearCheck();
        rdogrpJaund.clearCheck();
        rdogrpSJaun.clearCheck();
        rdogrpEyeP.clearCheck();
        rdogrpGono.clearCheck();
        rdogrpSick.clearCheck();
        rdogrpRef.clearCheck();
        txtRSlip.setText("");
        //txtPhone.setText("");
        rdogrpComp.clearCheck();
        rdogrpReason.clearCheck();
        //spnReason.setSelection(0);
        spnTPlace.setSelection(0);
        //spnTPlaceC.setSelection(0);
        txtTPlaceC.setText("");
        rdogrpTAbsIn.clearCheck();
        txtTAbsDur.setText("");
        rdogrpHos.clearCheck();
    }


    private void DataSave()
    {
        try
        {
            String DV="";

            if(txtChildId.getText().toString().length()==0)
            {
                Connection.MessageBox(AssNewBorn.this, "Required field:ChildID.");
                txtChildId.requestFocus();
                return;
            }

            /*else if(txtWeek.getText().toString().length()==0 & secWeek.isShown())
            {
                Connection.MessageBox(AssNewBorn.this, "Required field:সপ্তাহ.");
                txtWeek.requestFocus();
                return;
            }*/

            DV = Global.DateValidate(dtpVDate.getText().toString());
            if(DV.length()!=0 & secVDate.isShown())
            {
                Connection.MessageBox(AssNewBorn.this, DV);
                dtpVDate.requestFocus();
                return;
            }

            else if(spnOth1.getSelectedItemPosition()==0  & secOth1.isShown())
            {
                Connection.MessageBox(AssNewBorn.this, "Required field:নবজাতকের সমস্যা ১.");
                spnOth1.requestFocus();
                return;
            }

         /*else if(spnOth2.getSelectedItemPosition()==0  & secOth2.isShown())
           {
             Connection.MessageBox(AssNewBorn.this, "Required field:নবজাতকের সমস্যা ২.");
             spnOth2.requestFocus();
             return;
           }
         else if(spnOth3.getSelectedItemPosition()==0  & secOth3.isShown())
           {
             Connection.MessageBox(AssNewBorn.this, "Required field:নবজাতকের সমস্যা ৩.");
             spnOth3.requestFocus();
             return;
           }*/

            else if(!rdoHNoCry1.isChecked() & !rdoHNoCry2.isChecked() & secHNoCry.isShown())
            {
                Connection.MessageBox(AssNewBorn.this, "অপশন সিলেক্ট করা হয় নাই - (জন্মের পর না কাঁদা)");
                rdoHNoCry1.requestFocus();
                return;
            }

            else if(!rdoHNoBrea1.isChecked() & !rdoHNoBrea2.isChecked() & secHNoBrea.isShown())
            {
                Connection.MessageBox(AssNewBorn.this, "অপশন সিলেক্ট করা হয় নাই - (জন্মের পর পর শ্বাস না নেওয়া)");
                rdoHNoBrea1.requestFocus();
                return;
            }

            else if(!rdoHConv1.isChecked() & !rdoHConv2.isChecked() & secHConv.isShown())
            {
                Connection.MessageBox(AssNewBorn.this, "অপশন সিলেক্ট করা হয় নাই - (খিচুনী)");
                rdoHConv1.requestFocus();
                return;
            }

            else if(!rdoHUncon1.isChecked() & !rdoHUncon2.isChecked() & secHUncon.isShown())
            {
                Connection.MessageBox(AssNewBorn.this, "অপশন সিলেক্ট করা হয় নাই - (অজ্ঞান)");
                rdoHUncon1.requestFocus();
                return;
            }

            else if(!rdoHDBrea1.isChecked() & !rdoHDBrea2.isChecked() & secHDBrea.isShown())
            {
                Connection.MessageBox(AssNewBorn.this, "অপশন সিলেক্ট করা হয় নাই - (শ্বাস কষ্ট)");
                rdoHDBrea1.requestFocus();
                return;
            }

            else if(!rdoHJaund1.isChecked() & !rdoHJaund2.isChecked() & secHJaund.isShown())
            {
                Connection.MessageBox(AssNewBorn.this, "অপশন সিলেক্ট করা হয় নাই - (শরীর হলুদ হয়ে যাওয়া)");
                rdoHJaund1.requestFocus();
                return;
            }

            else if(!rdoHHFever1.isChecked() & !rdoHHFever2.isChecked() & secHHFever.isShown())
            {
                Connection.MessageBox(AssNewBorn.this, "অপশন সিলেক্ট করা হয় নাই - (শরীরের তাপমাত্রা বেড়ে যাওয়া)");
                rdoHHFever1.requestFocus();
                return;
            }

            else if(!rdoHLFever1.isChecked() & !rdoHLFever2.isChecked() & secHLFever.isShown())
            {
                Connection.MessageBox(AssNewBorn.this, "অপশন সিলেক্ট করা হয় নাই - (শরীরের তাপমাত্রা কমে যাওয়া)");
                rdoHLFever1.requestFocus();
                return;
            }

            else if(!rdoHSkin1.isChecked() & !rdoHSkin2.isChecked() & secHSkin.isShown())
            {
                Connection.MessageBox(AssNewBorn.this, "অপশন সিলেক্ট করা হয় নাই - (চামড়ায় সংক্রমন)");
                rdoHSkin1.requestFocus();
                return;
            }

            else if(!rdoHFedp1.isChecked() & !rdoHFedp2.isChecked() & secHFedp.isShown())
            {
                Connection.MessageBox(AssNewBorn.this, "অপশন সিলেক্ট করা হয় নাই - (খাওয়ার সমস্যা)");
                rdoHFedp1.requestFocus();
                return;
            }

            else if(!rdoHPus1.isChecked() & !rdoHPus2.isChecked() & secHPus.isShown())
            {
                Connection.MessageBox(AssNewBorn.this, "অপশন সিলেক্ট করা হয় নাই - (নাভিতে পুজ বা লাল বা র্দুগন্ধ)");
                rdoHPus1.requestFocus();
                return;
            }

            else if(!rdoHVomit1.isChecked() & !rdoHVomit2.isChecked() & secHVomit.isShown())
            {
                Connection.MessageBox(AssNewBorn.this, "অপশন সিলেক্ট করা হয় নাই - (দীর্ঘ মেয়াদী বমি)");
                rdoHVomit1.requestFocus();
                return;
            }

            else if(!rdoHWeak1.isChecked() & !rdoHWeak2.isChecked() & secHWeak.isShown())
            {
                Connection.MessageBox(AssNewBorn.this, "অপশন সিলেক্ট করা হয় নাই - (দুর্বল)");
                rdoHWeak1.requestFocus();
                return;
            }

            else if(!rdoHLeth1.isChecked() & !rdoHLeth2.isChecked() & secHLeth.isShown())
            {
                Connection.MessageBox(AssNewBorn.this, "অপশন সিলেক্ট করা হয় নাই - (নেতিয়ে পড়া)");
                rdoHLeth1.requestFocus();
                return;
            }

            else if(!rdoAsses1.isChecked() & !rdoAsses2.isChecked() & secAsses.isShown())
            {
                Connection.MessageBox(AssNewBorn.this, "অপশন সিলেক্ট করা হয় নাই - (নবজাতককে কি পরীক্ষা করা হয়েছে?)");
                rdoAsses1.requestFocus();
                return;
            }
            if (!chkRR.isChecked()) {
//            else if(txtRR1.getText().toString().length()==0 & secRR1.isShown())
                if (txtRR1.getText().toString().length() == 0 & secRR1.isShown()) {
                    Connection.MessageBox(AssNewBorn.this, "এক মিনিটে কতবার শ্বাস প্রশ্বাস  গুনুন - খালি থাকতে পারবেনা");
                    txtRR1.requestFocus();
                    return;
                }
            }
            else if(txtRR2.getText().toString().length()==0 & secRR2.isShown())
            {
                Connection.MessageBox(AssNewBorn.this, "শ্বাস প্রশ্বাস >=৬০ বা তার বেশী শুনুন  - খালি থাকতে পারবেনা");
                txtRR2.requestFocus();
                return;
            }
//            27/27/2020
            else if(!rdoTemp1.isChecked() & !rdoTemp2.isChecked() & !rdoTemp3.isChecked() & secTemp1.isShown())
            {
                Connection.MessageBox(AssNewBorn.this, "তাপমাত্রা নিতে পেরেছে কিনা অপশন সিলেক্ট করা হয় নাই");
                rdoTemp1.requestFocus();
                return;
            }
//
//            if (!chkTemp.isChecked()) {
////                if (txtTemp.getText().toString().length() == 0 & secTemp.isShown()) {
////                    Connection.MessageBox(AssNewBorn.this, "তাপমাত্রা - খালি থাকতে পারবেনা");
////                    txtTemp.requestFocus();
////                    return;
////                }
////                else
////                {
////                    if (secTemp.isShown() & (Double.valueOf(txtTemp.getText().toString().length() == 0 ? "0.0" : txtTemp.getText().toString()) < 92 || Double.valueOf(txtTemp.getText().toString().length() == 0 ? "999" : txtTemp.getText().toString()) > 108.0)) {
////                        Connection.MessageBox(AssNewBorn.this, "Current Temperature should be between 92 - 108");
////                        txtTemp.requestFocus();
////                        return;
////                    }
////                }
////            }
//            27/07/2020
            if (rdoTemp1.isChecked()) {
                if (txtTemp.getText().toString().length() == 0 & secTemp.isShown()) {
                    Connection.MessageBox(AssNewBorn.this, "তাপমাত্রা - খালি থাকতে পারবেনা");
                    txtTemp.requestFocus();
                    return;
                }
                else
                {
                    if (secTemp.isShown() & (Double.valueOf(txtTemp.getText().toString().length() == 0 ? "0.0" : txtTemp.getText().toString()) < 92 || Double.valueOf(txtTemp.getText().toString().length() == 0 ? "999" : txtTemp.getText().toString()) > 108.0)) {
                        Connection.MessageBox(AssNewBorn.this, "Current Temperature should be between 92 - 108");
                        txtTemp.requestFocus();
                        return;
                    }
                }
            }
//
            String var = txtRR1.getText().toString();
            Integer rr4 = Integer.parseInt(var.length() == 0 ? "0" : var);
            if(!rdoNoCry1.isChecked() & !rdoNoCry2.isChecked() & secNoCry.isShown())
            {
                Connection.MessageBox(AssNewBorn.this, "অপশন সিলেক্ট করা হয় নাই - (শিশু কাঁদছে না/শ্বাস-প্রশ্বাস নিচ্ছে না)");
                rdoNoCry1.requestFocus();
                return;
            }

            else if(!rdoGasp1.isChecked() & !rdoGasp2.isChecked() & secGasp.isShown())
            {
                Connection.MessageBox(AssNewBorn.this, "অপশন সিলেক্ট করা হয় নাই - (গ্যাসপিং)");
                rdoGasp1.requestFocus();
                return;
            }
            else if(!rdoSBrea1.isChecked() & !rdoSBrea2.isChecked() & secSBrea.isShown())
            {
                Connection.MessageBox(AssNewBorn.this, "অপশন সিলেক্ট করা হয় নাই - (ধীর শ্বাস)");
                rdoSBrea1.requestFocus();
                return;
            }
//            update_Shahidul 11-Dec-2018
            else if(rdoSBrea1.isChecked() & (rr4>=30))
            {
                Connection.MessageBox(AssNewBorn.this, "শ্বাসের হার ৩০ বা তার চেয়ে বেশী ,সুতরাং ধীর শ্বাস হ্যাঁ হবে না");
                rdoSBrea1.requestFocus();
                return;
            }
//            --------------------------------------------------------------------------
            else if(!rdoBirthAs1.isChecked() & !rdoBirthAs2.isChecked() & secBirthAs.isShown())
            {
                Connection.MessageBox(AssNewBorn.this, "অপশন সিলেক্ট করা হয় নাই - (জন্মকালীন শ্বাস কষ্ট)");
                rdoBirthAs1.requestFocus();
                return;
            }
            else if(!rdoConv1.isChecked() & !rdoConv2.isChecked() & secConv.isShown())
            {
                Connection.MessageBox(AssNewBorn.this, "অপশন সিলেক্ট করা হয় নাই - (খিঁচুনী হতে দেখা)");
                rdoConv1.requestFocus();
                return;
            }

            else if(!rdoRBrea1.isChecked() & !rdoRBrea2.isChecked() & secRBrea.isShown())
            {
                Connection.MessageBox(AssNewBorn.this, "অপশন সিলেক্ট করা হয় নাই - দ্রুত শ্বাস ৬০ (মিনিটে  বার বা তার চেয়ে বেশী)");
                rdoRBrea1.requestFocus();
                return;
            }

            else if(!rdoCInd1.isChecked() & !rdoCInd2.isChecked() & secCInd.isShown())
            {
                Connection.MessageBox(AssNewBorn.this, "অপশন সিলেক্ট করা হয় নাই - (বুকের নীচের অংশ মারাত্বক ভাবে ডেবে যাওয়া)");
                rdoCInd1.requestFocus();
                return;
            }

            else if(!rdoHFever1.isChecked() & !rdoHFever2.isChecked() & secHFever.isShown())
            {
                Connection.MessageBox(AssNewBorn.this, "অপশন সিলেক্ট করা হয় নাই - (তাপমাত্রা ১০১ºF -এর বেশী)");
                rdoHFever1.requestFocus();
                return;
            }

            else if(!rdoHypo1.isChecked() & !rdoHypo2.isChecked() & secHypo.isShown())
            {
                Connection.MessageBox(AssNewBorn.this, "অপশন সিলেক্ট করা হয় নাই - (তাপমাত্রা 95.5ºF -এর কম)");
                rdoHypo1.requestFocus();
                return;
            }

            else if(!rdoUCon1.isChecked() & !rdoUCon2.isChecked() & secUCon.isShown())
            {
                Connection.MessageBox(AssNewBorn.this, "অপশন সিলেক্ট করা হয় নাই - (অজ্ঞান)");
                rdoUCon1.requestFocus();
                return;
            }

            else if(!rdoPus1.isChecked() & !rdoPus2.isChecked() & secPus.isShown())
            {
                Connection.MessageBox(AssNewBorn.this, "অপশন সিলেক্ট করা হয় নাই - (অনেক অথবা মারাত্বক পুঁজ সহ দানা বা ফোস্কা)");
                rdoPus1.requestFocus();
                return;
            }

            else if(!rdoUmbR1.isChecked() & !rdoUmbR2.isChecked() & secUmbR.isShown())
            {
                Connection.MessageBox(AssNewBorn.this, "অপশন সিলেক্ট করা হয় নাই - (নাভি লাল )");
                rdoUmbR1.requestFocus();
                return;
            }

            else if(!rdoWeak1.isChecked() & !rdoWeak2.isChecked() & secWeak.isShown())
            {
                Connection.MessageBox(AssNewBorn.this, "অপশন সিলেক্ট করা হয় নাই - (দুর্বল)");
                rdoWeak1.requestFocus();
                return;
            }

            else if(!rdoLeth1.isChecked() & !rdoLeth2.isChecked() & secLeth.isShown())
            {
                Connection.MessageBox(AssNewBorn.this, "অপশন সিলেক্ট করা হয় নাই - (নেতিয়ে পড়া)");
                rdoLeth1.requestFocus();
                return;
            }

            else if(!rdoNoFed1.isChecked() & !rdoNoFed2.isChecked() & secNoFed.isShown())
            {
                Connection.MessageBox(AssNewBorn.this, "অপশন সিলেক্ট করা হয় নাই - (বাচ্চা খেতে পারে না)");
                rdoNoFed1.requestFocus();
                return;
            }

            else if(!rdoVsd1.isChecked() & !rdoVsd2.isChecked() & secVsd.isShown())
            {
                Connection.MessageBox(AssNewBorn.this, "অপশন সিলেক্ট করা হয় নাই - (খুব মারাত্বক রোগ)");
                rdoVsd1.requestFocus();
                return;
            }

            else if(!rdoConvH1.isChecked() & !rdoConvH2.isChecked() & secConvH.isShown())
            {
                Connection.MessageBox(AssNewBorn.this, "অপশন সিলেক্ট করা হয় নাই - (খিঁচুনীর ইতিহাস)");
                rdoConvH1.requestFocus();
                return;
            }

            else if(!rdoFonta1.isChecked() & !rdoFonta2.isChecked() & secFonta.isShown())
            {
                Connection.MessageBox(AssNewBorn.this, "অপশন সিলেক্ট করা হয় নাই - (মাথার তালু ফোলা বা উঁচ)ু");
                rdoFonta1.requestFocus();
                return;
            }

            else if(!rdoVomit1.isChecked() & !rdoVomit2.isChecked() & secVomit.isShown())
            {
                Connection.MessageBox(AssNewBorn.this, "অপশন সিলেক্ট করা হয় নাই - (সব কিছু বমি করে ফেলে দেয়)");
                rdoVomit1.requestFocus();
                return;
            }

            else if(!rdoH1Fever1.isChecked() & !rdoH1Fever2.isChecked() & secH1Fever.isShown())
            {
                Connection.MessageBox(AssNewBorn.this, "অপশন সিলেক্ট করা হয় নাই - (তাপমাত্রা ১00ºF - ১০১ºF এর মধ্যে)");
                rdoH1Fever1.requestFocus();
                return;
            }

            else if(!rdoLFever1.isChecked() & !rdoLFever2.isChecked() & secLFever.isShown())
            {
                Connection.MessageBox(AssNewBorn.this, "অপশন সিলেক্ট করা হয় নাই - (তাপমাত্রা ৯৫.৫ºF - ৯৭.৫ºF এর মধ্যে)");
                rdoLFever1.requestFocus();
                return;
            }

            else if(!rdoNJaun1.isChecked() & !rdoNJaun2.isChecked() & secNJaun.isShown())
            {
                Connection.MessageBox(AssNewBorn.this, "অপশন সিলেক্ট করা হয় নাই - (হাতের তালু বা পায়ের পাতায় জন্মের ১ দিন পর থেকে জন্ডিস)");
                rdoNJaun1.requestFocus();
                return;
            }

            else if(!rdoPvsd1.isChecked() & !rdoPvsd2.isChecked() & secPvsd.isShown())
            {
                Connection.MessageBox(AssNewBorn.this, "অপশন সিলেক্ট করা হয় নাই - (সম্ভবত খুব মারাত্বক রোগ)");
                rdoPvsd1.requestFocus();
                return;
            }

            else if(!rdoJaund1.isChecked() & !rdoJaund2.isChecked() & secJaund.isShown())
            {
                Connection.MessageBox(AssNewBorn.this, "অপশন সিলেক্ট করা হয় নাই - ( জন্মের ২৪ ঘন্টার মধ্যে শরীরের যে কোন অংশ হলুদ হয়ে যাওয়া (জন্ডিস))");
                rdoJaund1.requestFocus();
                return;
            }

            else if(!rdoSJaun1.isChecked() & !rdoSJaun2.isChecked() & secSJaun.isShown())
            {
                Connection.MessageBox(AssNewBorn.this, "অপশন সিলেক্ট করা হয় নাই - (মারাত্বক জন্ডিস)");
                rdoSJaun1.requestFocus();
                return;
            }

            else if(!rdoEyeP1.isChecked() & !rdoEyeP2.isChecked() & secEyeP.isShown())
            {
                Connection.MessageBox(AssNewBorn.this, "অপশন সিলেক্ট করা হয় নাই - (চোখ থেকে পুঁজ বের  হওয়া)");
                rdoEyeP1.requestFocus();
                return;
            }

            else if(!rdoGono1.isChecked() & !rdoGono2.isChecked() & secGono.isShown())
            {
                Connection.MessageBox(AssNewBorn.this, "অপশন সিলেক্ট করা হয় নাই - (চোখে সম্ভাব্য গনকোক্কাল ইনফেকশন)");
                rdoGono1.requestFocus();
                return;
            }

            else if(!rdoSick1.isChecked() & !rdoSick2.isChecked() & secSick.isShown())
            {
                Connection.MessageBox(AssNewBorn.this, "অপশন সিলেক্ট করা হয় নাই - (শিশুর বর্তমান আবস্তা )");
                rdoSick1.requestFocus();
                return;
            }

            else if(!rdoRef1.isChecked() & !rdoRef2.isChecked() & !rdoRef3.isChecked() & secRef.isShown())
            {
                Connection.MessageBox(AssNewBorn.this, "অপশন সিলেক্ট করা হয় নাই - (রেফার করা হয়েছে?)");
                rdoRef1.requestFocus();
                return;
            }
            if (rdoRef1.isChecked() || rdoRef2.isChecked()) {
                if (txtRSlip.getText().toString().length() == 0 & secRSlip.isShown()) {
                    Connection.MessageBox(AssNewBorn.this, "রেফারাল স্লিপ নং. খালি থাকতে পারবেনা");
                    txtRSlip.requestFocus();
                    return;
                }
            }

            if (txtRSlip.getText().toString().length() < 6  & secRSlip.isShown()) {
                Connection.MessageBox(AssNewBorn.this,"রেফারাল স্লিপ  ৬ সঙ্খারকম হবেনা");
                txtRSlip.requestFocus();
                return;
            }
            if (txtPhone.getText().toString().length() != 0 & secPhone.isShown()) {
                if (txtPhone.getText().toString().length() < 11 & secPhone.isShown()) {
                    Connection.MessageBox(AssNewBorn.this, "ফোন নম্বর ১১  সঙ্খারকম হবেনা");
                    txtPhone.requestFocus();
                    return;
                }
            }

            //            20/07/2020
//            if (!chkRR.isChecked())
//            {
//                if (txtRR1.getText().toString().length() == 0 & secRR1.isShown()) {
//                    Connection.MessageBox(AssNewBorn.this, "এক মিনিটে কতবার শ্বাস প্রশ্বাস  গুনুনঃ - খালি থাকতে পারবেনা");
//                    txtRR1.requestFocus();
//                    return;
//                }
//            }
//            --------------------------------
/*            String RFNo;
            RFNo=C.ReturnSingleValue("Select RSlip  from AssPneu WHERE   RSlip = '"+ txtRSlip.getText() +"'");
            if(RFNo.trim().equalsIgnoreCase(txtRSlip.getText().toString().trim()))
            {
                Connection.MessageBox(AssNewBorn.this,	"এই রেফারাল স্লিপ নং পূর্বে ব্যবহার করা হয়েছে");
                txtRSlip.requestFocus();
                return;
            }*/
            /*else if(!rdoComp1.isChecked() & !rdoComp2.isChecked() & secComp.isShown())
            {
                Connection.MessageBox(AssNewBorn.this, "Select anyone options from Comp.");
                rdoComp1.requestFocus();
                return;
            }*/
            if(!rdoReason1.isChecked() & !rdoReason2.isChecked() & secReason.isShown())
            {
                Connection.MessageBox(AssNewBorn.this, "অপশন সিলেক্ট করা হয় নাই - (শিশুটিকে পরীক্ষা না করার কারন)");
                rdoReason1.requestFocus();
                return;
            }
/*            else if(spnReason.getSelectedItemPosition()==0  & secReason.isShown())
            {
                Connection.MessageBox(AssNewBorn.this, "Required field:শিশুটিকে পরীক্ষা না করার কারন.");
                spnReason.requestFocus();
                return;
            }*/
            else if(spnTPlace.getSelectedItemPosition()==0  & secTPlace.isShown())
            {
                Connection.MessageBox(AssNewBorn.this, "ড্রপডাউন মেনু সিলেক্ট করা হয় নাই - (কোথায় চিকিৎসার জন্য গিয়েছে?)");
                spnTPlace.requestFocus();
                return;
            }
/*            else if(spnTPlaceC.getSelectedItemPosition()==0  & secTPlaceC.isShown())
            {
                Connection.MessageBox(AssNewBorn.this, "Required field:অন্যান্য.");
                spnTPlaceC.requestFocus();
                return;
            }*/
            else if(txtTPlaceC.getText().toString().length()==0 & secTPlaceC.isShown())
            {
                Connection.MessageBox(AssNewBorn.this, "Required field:অন্যান্য");
                txtTPlaceC.requestFocus();
                return;
            }
            else if(txtTAbsDur.getText().toString().length()==0 & secTAbsDur.isShown())
            {
                Connection.MessageBox(AssNewBorn.this, "চিকিৎসার জন্য কতদিন হয় গিয়েছে? - খালি থাকতে পারবেনা");
                txtTAbsDur.requestFocus();
                return;
            }
            else if(!rdoTAbsIn1.isChecked() & !rdoTAbsIn2.isChecked() & secTAbsDur.isShown())
            {
                Connection.MessageBox(AssNewBorn.this, "অপশন সিলেক্ট করা হয় নাই - (চিকিৎসার জন্য কতদিন হয় গিয়েছে?)");
                rdoTAbsIn1.requestFocus();
                return;
            }
            else if(!rdoHos1.isChecked() & !rdoHos2.isChecked() & !rdoHos3.isChecked() & secHos.isShown())
            {
                Connection.MessageBox(AssNewBorn.this, "অপশন সিলেক্ট করা হয় নাই - (হাসপাতালে ভর্তি ছিল কি?)");
                rdoHos1.requestFocus();
                return;
            }

            String var2 = txtRR2.getText().toString();
            Integer rr2 = Integer.parseInt(var2.length() == 0 ? "0" : var2);
            if (rr2 >= 60 & rdoRBrea2.isChecked())
            {
                Connection.MessageBox(AssNewBorn.this, "শিশুর শ্বাসের হার ৬০ বা তার বেশী, তাহলে দ্রুত শ্বাস না হবেনা।");
                return;
            }
            else if((rdoNoCry1.isChecked() || rdoGasp1.isChecked() || rdoSBrea1.isChecked()) && (rdoBirthAs2.isChecked()))
            {
                Connection.MessageBox(AssNewBorn.this, "কান্না ও শ্বাস প্রশাসসের লক্ষণ হ্যাঁ কিন্ত জন্মকালীন শ্বাস কষ্ট না হতে পারবেনা");
                return;
            }
            else if((rdoNoCry2.isChecked() & rdoGasp2.isChecked() & rdoSBrea2.isChecked()) && (rdoBirthAs1.isChecked()))
            {
                Connection.MessageBox(AssNewBorn.this, "কান্না ও শ্বাস প্রশাসসের লক্ষণ না কিন্ত জন্মকালীন শ্বাস কষ্ট হ্যাঁ হতে পারবেনা");
                return;
            }

            else if((rdoConv1.isChecked() || rdoRBrea1.isChecked() || rdoCInd1.isChecked() || rdoHFever1.isChecked()  || rdoHypo1.isChecked()  || rdoUCon1.isChecked()  || rdoPus1.isChecked() || rdoUmbR1.isChecked() || rdoWeak1.isChecked() || rdoLeth1.isChecked() || rdoNoFed1.isChecked()) && (rdoVsd2.isChecked()))
            {
                Connection.MessageBox(AssNewBorn.this, "খুব মারাত্বকর রোগের লক্ষণগুলো হ্যাঁ কিন্ত খুব মারাত্বকরর রোগ না হতে পারবেনা");
                return;
            }
            else if((rdoConv2.isChecked() & rdoRBrea2.isChecked() & rdoCInd2.isChecked() & rdoHFever2.isChecked()  & rdoHypo2.isChecked()  & rdoUCon2.isChecked()  & rdoPus2.isChecked() & rdoUmbR2.isChecked() & rdoWeak2.isChecked() & rdoLeth2.isChecked() & rdoNoFed2.isChecked()) && (rdoVsd1.isChecked()))
            {
                Connection.MessageBox(AssNewBorn.this, "খুব মারাত্বকর রোগের লক্ষণগুলো না  কিন্ত খুব মারাত্বকরর রোগ হ্যাঁ  হতে পারবেনা");
                return;
            }
            else if((rdoConvH1.isChecked() || rdoFonta1.isChecked() || rdoVomit1.isChecked() || rdoH1Fever1.isChecked() || rdoLFever1.isChecked() || rdoNJaun1.isChecked()) && (rdoPvsd2.isChecked()))
            {
                Connection.MessageBox(AssNewBorn.this, "সম্ভবত খুব মারাত্বকর রোগের লক্ষণগুলো হ্যাঁ কিন্ত সম্ভবত খুব মারাত্বকরর রোগ না হতে পারবেনা");
                return;
            }
            else if((rdoConvH2.isChecked() & rdoFonta2.isChecked() & rdoVomit2.isChecked() & rdoH1Fever2.isChecked() & rdoLFever2.isChecked() & rdoNJaun2.isChecked()) && (rdoPvsd1.isChecked()))
            {
                Connection.MessageBox(AssNewBorn.this, "সম্ভবত খুব মারাত্বকর রোগের লক্ষণগুলো না  কিন্ত সম্ভবত খুব মারাত্বকরর রোগ হ্যাঁ  হতে পারবেনা");
                return;
            }
            else if(rdoJaund1.isChecked() & rdoSJaun2.isChecked())
            {
                Connection.MessageBox(AssNewBorn.this, "জন্মের ২৪ ঘন্টার মধ্যে শরীর হালুদ হ্যাঁ কিন্ত  মারাত্বক জন্ডিস না হতে পারবেনা");
                return;
            }
            else if(rdoJaund2.isChecked() & rdoSJaun1.isChecked())
            {
                Connection.MessageBox(AssNewBorn.this, "জন্মের ২৪ ঘন্টার মধ্যে শরীর হালুদ না  কিন্ত  মারাত্বক জন্ডিস হ্যাঁ হতে পারবেনা");
                return;
            }
            else if(rdoEyeP1.isChecked() & rdoGono2.isChecked())
            {
                Connection.MessageBox(AssNewBorn.this, "চোখ থেকে পুঁজ বের হয় হ্যাঁ কিন্ত  চোখে সম্ভাব্য গনকোক্কাল ইনফেকশন  না হতে পারবেনা");
                return;
            }
            else if(rdoEyeP2.isChecked() & rdoGono1.isChecked())
            {
                Connection.MessageBox(AssNewBorn.this, "চোখ থেকে পুঁজ বের হয় না কিন্ত  চোখে সম্ভাব্য গনকোক্কাল ইনফেকশন  হ্যাঁ হতে পারবেনা");
                return;
            }
            else if((rdoBirthAs1.isChecked() || rdoVsd1.isChecked() || rdoPvsd1.isChecked() || rdoSJaun1.isChecked() || rdoGono1.isChecked()) && (rdoRef3.isChecked()))
            {
                Connection.MessageBox(AssNewBorn.this, "রেফার এর লক্ষণগুলো হ্যাঁ  কিনত রেফার করা হয়নি  হতে পারবে না");
                return;
            }
            else if((rdoBirthAs2.isChecked() & rdoVsd2.isChecked() & rdoPvsd2.isChecked() & rdoSJaun2.isChecked() & rdoGono2.isChecked()) && (rdoRef1.isChecked() || rdoRef2.isChecked()))
            {
                Connection.MessageBox(AssNewBorn.this, "রেফার এর লক্ষণগুলো না  কিনত রেফার করা হয়েছে হ্যাঁ  হতে পারবে না");
                return;
            }
            else if((rdoBirthAs2.isChecked() & rdoVsd2.isChecked() & rdoPvsd2.isChecked() & rdoSJaun2.isChecked() & rdoGono2.isChecked()) && (rdoSick2.isChecked()))
            {
                Connection.MessageBox(AssNewBorn.this, "কোন সমস্যা নাই কিন্ত শিশুটি অসুস্থ  হতে পারবে না");
                return;
            }
            else if((rdoBirthAs1.isChecked() || rdoVsd1.isChecked() || rdoPvsd1.isChecked() || rdoSJaun1.isChecked() || rdoGono1.isChecked()) && (rdoSick1.isChecked()))
            {
                Connection.MessageBox(AssNewBorn.this, "শিশুর সমস্যা আছে  কিন্ত শিশুটি সুস্থ্য আছে  হতে পারবে না");
                return;
            }
            //String tm = txtTemp.getText().toString();
            //Float Temp = Float.parseFloat(tm);
            String a=txtTemp.getText().toString();
            Float Temp =  Float.parseFloat(a.length()==0?"0":a);
            if(txtTemp.getText().toString().length()==0)
            {

            }
            else
            {
                if (Temp > 101.0)
                {
                    if (!rdoHFever1.isChecked())
                    {
                        Connection.MessageBox(AssNewBorn.this, "১০১ ফাঃ এর বেশী  তাহলে তাপমাত্রা ১০১ ফাঃ এর বেশী না হবেনা");
                        return;
                    }
                   /* else if (rdoHFever1.isChecked())
                    {
                        Connection.MessageBox(AssNewBorn.this, "১০১ ফাঃ এর কম  তাহলে তাপমাত্রা ১০১ ফাঃ এর বেশী হ্যাঁ হবেনা");
                        return;
                    }*/
                }
                if (Temp < 101)
                {
                    if (rdoHFever1.isChecked())
                    {
                        Connection.MessageBox(AssNewBorn.this, "১০১ ফাঃ এর কম  তাহলে তাপমাত্রা ১০১ ফাঃ এর বেশী হ্যাঁ হবেনা");
                        return;
                    }
                }
                if (Temp >= 100 & Temp <= 101)
                {
                    if (!rdoH1Fever1.isChecked())
                    {
                        Connection.MessageBox(AssNewBorn.this, "১০০-১০১ ফাঃ এর বেশী  তাহলে তাপমাত্রা ১০০-১০১ ফাঃ এর মধ্যে না হবেনা");
                        return;
                    }
                }
                if (Temp < 100 || Temp > 101)
                {
                    if (rdoH1Fever1.isChecked())
                    {
                        Connection.MessageBox(AssNewBorn.this, "১০০-১০১ ফাঃ এর বেশী মধ্যে নয় তাহলে  তাপমাত্রা ১০০-১০১ ফাঃ এর মধ্যে হ্যাঁ হবেনা");
                        return;
                    }
                }
                if (Temp >= 95.5 & Temp <= 97.5)
                {
                    if (!rdoLFever1.isChecked())
                    {
                        Connection.MessageBox(AssNewBorn.this, "৯৫.৫-৯৭.৫ ফাঃ এর মধ্যে তাহলে তাপমাত্রা ৯৫.৫-৯৭.৫ ফাঃ এর মধ্যে না হবেনা");
                        return;
                    }
                }
                if (Temp < 95.5 || Temp > 97.5)
                {
                    if (rdoLFever1.isChecked())
                    {
                        Connection.MessageBox(AssNewBorn.this, "৯৫.৫-৯৭.৫ ফাঃ এর মধ্যে নয় তাহলে  তাপমাত্রা ৯৫.৫-৯৭.৫ ফাঃ এর মধ্যে হ্যাঁ হবেনা");
                        return;
                    }
                }
                if (Temp < 95.5)
                {
                    if (!rdoHypo1.isChecked())
                    {
                        Connection.MessageBox(AssNewBorn.this, "৯৫.৫ ফাঃ এর কম  তাহলে  তাপমাত্রা ৯৫.৫ ফাঃ এর কম না  হবেনা");
                        return;
                    }
                }
                if (Temp >= 95.5)
                {
                    if (rdoHypo1.isChecked())
                    {
                        Connection.MessageBox(AssNewBorn.this, "৯৫.৫ ফাঃ এর বেশী  তাহলে  তাপমাত্রা ৯৫.৫ ফাঃ এর কম হ্যাঁ হবেনা");
                        return;
                    }
                }
            }



/*            if (txtRSlip.getText().toString().length() != 0 & secRSlip.isShown()) {
                String RFNo;
                RFNo = C.ReturnSingleValue("Select RSlip  from AssNewBorn WHERE   RSlip = '" + txtRSlip.getText() + "'");
                //Connection.MessageBox(AssPneu.this,	RFNo);
                if (RFNo.trim().equalsIgnoreCase(txtRSlip.getText().toString().trim())) {
                    Connection.MessageBox(AssNewBorn.this, "এই রেফারাল স্লিপ নং পূর্বে ব্যবহার করা হয়েছে");
                    txtRSlip.requestFocus();
                    return;
                }
            }*/
            //Select ChildId from " + TableName + "  Where ChildId='"+ ChildID +"' and Week='"+ txtWeek.getSelectedItem().toString() +"' and VType='"+ VisitType +"' and Visit='"+ txtVisit.getSelectedItem().toString() +"'"))
            if (txtRSlip.getText().toString().length() != 0 & secRSlip.isShown()) {
                String RFNo;
                //RFNo = C.ReturnSingleValue("Select RSlip  from AssNewBorn WHERE  ChildId='" + ChildID + "' and Week='" + txtWeek.getSelectedItem().toString() + "' and  and VType=' "+ VisitType + "' and Visit='" + txtVisit.getSelectedItem().toString() + "' and RSlip = '" + txtRSlip.getText() + "'");
                //RFNo = C.ReturnSingleValue("Select RSlip  from AssNewBorn WHERE   RSlip = '" + txtRSlip.getText().toString() + "' and ChildId <> '"+ ChildID +"'");
                RFNo = C.ReturnSingleValue("Select rslip from AssNewBorn where rslip='"+ txtRSlip.getText().toString() +"' and childid||week||vtype||visit not in('"+ (ChildID+txtWeek.getSelectedItem().toString()+VisitType+txtVisit.getSelectedItem().toString()) +"') limit 1");
                if (RFNo.trim().equalsIgnoreCase(txtRSlip.getText().toString().trim())) {
                    Connection.MessageBox(AssNewBorn.this, "এই রেফারাল স্লিপ নং পূর্বে নবজাতকে-এ ব্যবহার করা হয়েছে");
                    txtRSlip.requestFocus();
                    return;
                }
                String R2;
                //R2 = C.ReturnSingleValue("Select RSlip  from AssPneu WHERE   RSlip = '" + txtRSlip.getText().toString() + "' and ChildId <> '"+ ChildID +"'");
                R2 = C.ReturnSingleValue("Select rslip from AssPneu where rslip='"+ txtRSlip.getText().toString() +"' and childid||week||vtype||visit not in('"+ (ChildID+txtWeek.getSelectedItem().toString()+VisitType+txtVisit.getSelectedItem().toString()) +"') limit 1");
                if (R2.trim().equalsIgnoreCase(txtRSlip.getText().toString().trim())) {
                    Connection.MessageBox(AssNewBorn.this, "এই রেফারাল স্লিপ নং পূর্বে (০-৫৯ মাস)-এ  ব্যবহার করা হয়েছে");
                    txtRSlip.requestFocus();
                    return;
                }
            }


            String SQL = "";

            if(!C.Existence("Select ChildId from " + TableName + "  Where ChildId='"+ ChildID +"' and Week='"+ txtWeek.getSelectedItem().toString() +"' and VType='"+ VisitType +"' and Visit='"+ txtVisit.getSelectedItem().toString() +"'"))
            {
                SQL = "Insert into " + TableName + "(ChildId,CID,PID,Week,VType,Visit,StartTime,EnDt,UserId, Upload)Values('"+ ChildID +"','','','"+ txtWeek.getSelectedItem().toString() +"','"+ VisitType +"','"+ txtVisit.getSelectedItem().toString() +"','"+ g.CurrentTime24() +"','"+ Global.DateTimeNowYMDHMS() +"','"+ g.getUserId() +"','2')";
                C.Save(SQL);
            }

            SQL = "Update " + TableName + " Set Upload='2',";
            SQL+="Temp = '"+ txtTemp.getText().toString() +"',";
//            SQL +="tempDk = '"+ (chkTemp.isChecked()?"1":"2") +"',";
            //21 Jul 2020
            if(rdoTemp1.isChecked())
                SQL +="tempDk = '2',";
            else if(rdoTemp2.isChecked())
                SQL +="tempDk = '1',";
            else if(rdoTemp3.isChecked())
                SQL +="tempDk = '3',";
            else
                SQL +="tempDk = '',";


            //SQL+="Week = '"+ txtWeek.getText().toString() +"',";
            //SQL+="VType = '"+ VisitType +"',";
            //SQL+="Visit = '"+ txtVisit.getText().toString() +"',";
            SQL+="VDate = '"+ Global.DateConvertYMD(dtpVDate.getText().toString()) +"',";
            SQL+="Oth1 = '"+ (spnOth1.getSelectedItemPosition()==0?"":Global.Left(spnOth1.getSelectedItem().toString(),2)) +"',";
            SQL+="Oth2 = '"+ (spnOth2.getSelectedItemPosition()==0?"":Global.Left(spnOth2.getSelectedItem().toString(),2)) +"',";
            SQL+="Oth3 = '"+ (spnOth3.getSelectedItemPosition()==0?"":Global.Left(spnOth3.getSelectedItem().toString(),2)) +"',";
            RadioButton rbHNoCry = (RadioButton)findViewById(rdogrpHNoCry.getCheckedRadioButtonId());
            SQL+="HNoCry = '"+ (rbHNoCry==null?"":(Global.Left(rbHNoCry.getText().toString(),1))) +"',";
            RadioButton rbHNoBrea = (RadioButton)findViewById(rdogrpHNoBrea.getCheckedRadioButtonId());
            SQL+="HNoBrea = '"+ (rbHNoBrea==null?"":(Global.Left(rbHNoBrea.getText().toString(),1))) +"',";
            RadioButton rbHConv = (RadioButton)findViewById(rdogrpHConv.getCheckedRadioButtonId());
            SQL+="HConv = '"+ (rbHConv==null?"":(Global.Left(rbHConv.getText().toString(),1))) +"',";
            RadioButton rbHUncon = (RadioButton)findViewById(rdogrpHUncon.getCheckedRadioButtonId());
            SQL+="HUncon = '"+ (rbHUncon==null?"":(Global.Left(rbHUncon.getText().toString(),1))) +"',";
            RadioButton rbHDBrea = (RadioButton)findViewById(rdogrpHDBrea.getCheckedRadioButtonId());
            SQL+="HDBrea = '"+ (rbHDBrea==null?"":(Global.Left(rbHDBrea.getText().toString(),1))) +"',";
            RadioButton rbHJaund = (RadioButton)findViewById(rdogrpHJaund.getCheckedRadioButtonId());
            SQL+="HJaund = '"+ (rbHJaund==null?"":(Global.Left(rbHJaund.getText().toString(),1))) +"',";
            RadioButton rbHHFever = (RadioButton)findViewById(rdogrpHHFever.getCheckedRadioButtonId());
            SQL+="HHFever = '"+ (rbHHFever==null?"":(Global.Left(rbHHFever.getText().toString(),1))) +"',";
            RadioButton rbHLFever = (RadioButton)findViewById(rdogrpHLFever.getCheckedRadioButtonId());
            SQL+="HLFever = '"+ (rbHLFever==null?"":(Global.Left(rbHLFever.getText().toString(),1))) +"',";
            RadioButton rbHSkin = (RadioButton)findViewById(rdogrpHSkin.getCheckedRadioButtonId());
            SQL+="HSkin = '"+ (rbHSkin==null?"":(Global.Left(rbHSkin.getText().toString(),1))) +"',";
            RadioButton rbHFedp = (RadioButton)findViewById(rdogrpHFedp.getCheckedRadioButtonId());
            SQL+="HFedp = '"+ (rbHFedp==null?"":(Global.Left(rbHFedp.getText().toString(),1))) +"',";
            RadioButton rbHPus = (RadioButton)findViewById(rdogrpHPus.getCheckedRadioButtonId());
            SQL+="HPus = '"+ (rbHPus==null?"":(Global.Left(rbHPus.getText().toString(),1))) +"',";
            RadioButton rbHVomit = (RadioButton)findViewById(rdogrpHVomit.getCheckedRadioButtonId());
            SQL+="HVomit = '"+ (rbHVomit==null?"":(Global.Left(rbHVomit.getText().toString(),1))) +"',";
            RadioButton rbHWeak = (RadioButton)findViewById(rdogrpHWeak.getCheckedRadioButtonId());
            SQL+="HWeak = '"+ (rbHWeak==null?"":(Global.Left(rbHWeak.getText().toString(),1))) +"',";
            RadioButton rbHLeth = (RadioButton)findViewById(rdogrpHLeth.getCheckedRadioButtonId());
            SQL+="HLeth = '"+ (rbHLeth==null?"":(Global.Left(rbHLeth.getText().toString(),1))) +"',";
            RadioButton rbAsses = (RadioButton)findViewById(rdogrpAsses.getCheckedRadioButtonId());
            SQL+="Asses = '"+ (rbAsses==null?"":(Global.Left(rbAsses.getText().toString(),1))) +"',";
            SQL+="RR1 = '"+ txtRR1.getText().toString() +"',";
            SQL +="RRDk = '"+ (chkRR.isChecked()?"1":"2") +"',";
            SQL+="RR2 = '"+ txtRR2.getText().toString() +"',";
            RadioButton rbNoCry = (RadioButton)findViewById(rdogrpNoCry.getCheckedRadioButtonId());
            SQL+="NoCry = '"+ (rbNoCry==null?"":(Global.Left(rbNoCry.getText().toString(),1))) +"',";
            RadioButton rbGasp = (RadioButton)findViewById(rdogrpGasp.getCheckedRadioButtonId());
            SQL+="Gasp = '"+ (rbGasp==null?"":(Global.Left(rbGasp.getText().toString(),1))) +"',";
            RadioButton rbSBrea = (RadioButton)findViewById(rdogrpSBrea.getCheckedRadioButtonId());
            SQL+="SBrea = '"+ (rbSBrea==null?"":(Global.Left(rbSBrea.getText().toString(),1))) +"',";
            RadioButton rbBirthAs = (RadioButton)findViewById(rdogrpBirthAs.getCheckedRadioButtonId());
            SQL+="BirthAs = '"+ (rbBirthAs==null?"":(Global.Left(rbBirthAs.getText().toString(),1))) +"',";
            RadioButton rbConv = (RadioButton)findViewById(rdogrpConv.getCheckedRadioButtonId());
            SQL+="Conv = '"+ (rbConv==null?"":(Global.Left(rbConv.getText().toString(),1))) +"',";
            RadioButton rbRBrea = (RadioButton)findViewById(rdogrpRBrea.getCheckedRadioButtonId());
            SQL+="RBrea = '"+ (rbRBrea==null?"":(Global.Left(rbRBrea.getText().toString(),1))) +"',";
            RadioButton rbCInd = (RadioButton)findViewById(rdogrpCInd.getCheckedRadioButtonId());
            SQL+="CInd = '"+ (rbCInd==null?"":(Global.Left(rbCInd.getText().toString(),1))) +"',";
            RadioButton rbHFever = (RadioButton)findViewById(rdogrpHFever.getCheckedRadioButtonId());
            SQL+="HFever = '"+ (rbHFever==null?"":(Global.Left(rbHFever.getText().toString(),1))) +"',";
            RadioButton rbHypo = (RadioButton)findViewById(rdogrpHypo.getCheckedRadioButtonId());
            SQL+="Hypo = '"+ (rbHypo==null?"":(Global.Left(rbHypo.getText().toString(),1))) +"',";
            RadioButton rbUCon = (RadioButton)findViewById(rdogrpUCon.getCheckedRadioButtonId());
            SQL+="UCon = '"+ (rbUCon==null?"":(Global.Left(rbUCon.getText().toString(),1))) +"',";
            RadioButton rbPus = (RadioButton)findViewById(rdogrpPus.getCheckedRadioButtonId());
            SQL+="Pus = '"+ (rbPus==null?"":(Global.Left(rbPus.getText().toString(),1))) +"',";
            RadioButton rbUmbR = (RadioButton)findViewById(rdogrpUmbR.getCheckedRadioButtonId());
            SQL+="UmbR = '"+ (rbUmbR==null?"":(Global.Left(rbUmbR.getText().toString(),1))) +"',";
            RadioButton rbWeak = (RadioButton)findViewById(rdogrpWeak.getCheckedRadioButtonId());
            SQL+="Weak = '"+ (rbWeak==null?"":(Global.Left(rbWeak.getText().toString(),1))) +"',";
            RadioButton rbLeth = (RadioButton)findViewById(rdogrpLeth.getCheckedRadioButtonId());
            SQL+="Leth = '"+ (rbLeth==null?"":(Global.Left(rbLeth.getText().toString(),1))) +"',";
            RadioButton rbNoFed = (RadioButton)findViewById(rdogrpNoFed.getCheckedRadioButtonId());
            SQL+="NoFed = '"+ (rbNoFed==null?"":(Global.Left(rbNoFed.getText().toString(),1))) +"',";
            RadioButton rbVsd = (RadioButton)findViewById(rdogrpVsd.getCheckedRadioButtonId());
            SQL+="Vsd = '"+ (rbVsd==null?"":(Global.Left(rbVsd.getText().toString(),1))) +"',";
            RadioButton rbConvH = (RadioButton)findViewById(rdogrpConvH.getCheckedRadioButtonId());
            SQL+="ConvH = '"+ (rbConvH==null?"":(Global.Left(rbConvH.getText().toString(),1))) +"',";
            RadioButton rbFonta = (RadioButton)findViewById(rdogrpFonta.getCheckedRadioButtonId());
            SQL+="Fonta = '"+ (rbFonta==null?"":(Global.Left(rbFonta.getText().toString(),1))) +"',";
            RadioButton rbVomit = (RadioButton)findViewById(rdogrpVomit.getCheckedRadioButtonId());
            SQL+="Vomit = '"+ (rbVomit==null?"":(Global.Left(rbVomit.getText().toString(),1))) +"',";
            RadioButton rbH1Fever = (RadioButton)findViewById(rdogrpH1Fever.getCheckedRadioButtonId());
            SQL+="H1Fever = '"+ (rbH1Fever==null?"":(Global.Left(rbH1Fever.getText().toString(),1))) +"',";
            RadioButton rbLFever = (RadioButton)findViewById(rdogrpLFever.getCheckedRadioButtonId());
            SQL+="LFever = '"+ (rbLFever==null?"":(Global.Left(rbLFever.getText().toString(),1))) +"',";
            RadioButton rbNJaun = (RadioButton)findViewById(rdogrpNJaun.getCheckedRadioButtonId());
            SQL+="NJaun = '"+ (rbNJaun==null?"":(Global.Left(rbNJaun.getText().toString(),1))) +"',";
            RadioButton rbPvsd = (RadioButton)findViewById(rdogrpPvsd.getCheckedRadioButtonId());
            SQL+="Pvsd = '"+ (rbPvsd==null?"":(Global.Left(rbPvsd.getText().toString(),1))) +"',";
            RadioButton rbJaund = (RadioButton)findViewById(rdogrpJaund.getCheckedRadioButtonId());
            SQL+="Jaund = '"+ (rbJaund==null?"":(Global.Left(rbJaund.getText().toString(),1))) +"',";
            RadioButton rbSJaun = (RadioButton)findViewById(rdogrpSJaun.getCheckedRadioButtonId());
            SQL+="SJaun = '"+ (rbSJaun==null?"":(Global.Left(rbSJaun.getText().toString(),1))) +"',";
            RadioButton rbEyeP = (RadioButton)findViewById(rdogrpEyeP.getCheckedRadioButtonId());
            SQL+="EyeP = '"+ (rbEyeP==null?"":(Global.Left(rbEyeP.getText().toString(),1))) +"',";
            RadioButton rbGono = (RadioButton)findViewById(rdogrpGono.getCheckedRadioButtonId());
            SQL+="Gono = '"+ (rbGono==null?"":(Global.Left(rbGono.getText().toString(),1))) +"',";
            RadioButton rbSick = (RadioButton)findViewById(rdogrpSick.getCheckedRadioButtonId());
            SQL+="Sick = '"+ (rbSick==null?"":(Global.Left(rbSick.getText().toString(),1))) +"',";
            RadioButton rbRef = (RadioButton)findViewById(rdogrpRef.getCheckedRadioButtonId());
            SQL+="Ref = '"+ (rbRef==null?"":(Global.Left(rbRef.getText().toString(),1))) +"',";
            SQL+="RSlip = '"+ txtRSlip.getText().toString() +"',";
            //RadioButton rbComp = (RadioButton)findViewById(rdogrpComp.getCheckedRadioButtonId());
            //SQL+="Comp = '"+ (rbComp==null?"":(Global.Left(rbComp.getText().toString(),1))) +"',";
            SQL+="Comp = '',";
            RadioButton rbReason = (RadioButton)findViewById(rdogrpReason.getCheckedRadioButtonId());
            SQL+="Reason = '"+ (rbReason==null?"":(Global.Left(rbReason.getText().toString(),1))) +"',";
            //SQL+="Reason = '"+ (spnReason.getSelectedItemPosition()==0?"":Global.Left(spnReason.getSelectedItem().toString(),1)) +"',";
            SQL+="TPlace = '"+ (spnTPlace.getSelectedItemPosition()==0?"":Global.Left(spnTPlace.getSelectedItem().toString(),1)) +"',";
            //SQL+="TPlaceC = '"+ (spnTPlaceC.getSelectedItemPosition()==0?"":Global.Left(spnTPlaceC.getSelectedItem().toString(),2)) +"',";
            SQL+="TPlaceC = '"+ txtTPlaceC.getText().toString() +"',";
            RadioButton rbTAbsIn = (RadioButton)findViewById(rdogrpTAbsIn.getCheckedRadioButtonId());
            SQL+="TAbsIn = '"+ (rbTAbsIn==null?"":(Global.Left(rbTAbsIn.getText().toString(),2))) +"',";
            SQL+="TAbsDur = '"+ txtTAbsDur.getText().toString() +"',";
            RadioButton rbHos = (RadioButton)findViewById(rdogrpHos.getCheckedRadioButtonId());
            SQL+="Hos = '"+ (rbHos==null?"":(Global.Left(rbHos.getText().toString(),1))) +"'";
             /*SQL+="EnDt = '"+ Global.DateConvertYMD(dtpEnDt.getText().toString()) +"',";
             SQL+="UserId = '"+ txtUserId.getText().toString() +"',";
             SQL+="Upload = '"+ txtUpload.getText().toString() +"',";
             SQL+="UploadDT = '"+ Global.DateConvertYMD(dtpUploadDT.getText().toString()) +"'";*/
            SQL+="Where ChildId='"+ ChildID +"' and Week='"+ txtWeek.getSelectedItem().toString() +"' and VType='"+ VisitType +"' and Visit='"+ txtVisit.getSelectedItem().toString() +"'";
            C.Save(SQL);
            if (txtPhone.getText().toString().length() != 0 & secPhone.isShown()) {
                C.Save("Update Child set ContactNo='" + txtPhone.getText().toString() + "',upload='2' Where ChildId='" + ChildID + "'");
            }
            //Referral Information
            if(Child_Outside_Area.equals("n")) {
                if (rdoRef1.isChecked() | rdoRef2.isChecked()) {
                    String RefInfo = ChildID + "," + txtWeek.getSelectedItem().toString() + "," + VisitType + "," + txtVisit.getSelectedItem().toString() + "," + Global.DateConvertYMD(dtpVDate.getText().toString());

                    if (VisitType.equals("1"))
                        C.Save("Update Child set Upload='2',Referral='" + RefInfo + "' where ChildId='" + ChildID + "'");
                    else if (VisitType.equals("2"))
                        C.Save("Update Child set Upload='2',Referral_Foll='" + RefInfo + "' where ChildId='" + ChildID + "'");
                    else if (VisitType.equals("3"))
                        C.Save("Update Child set Upload='2',Referral_Add='" + RefInfo + "' where ChildId='" + ChildID + "'");
                }

                if (rdoHos3.isChecked()) {
                    String RefInfo1 = ChildID + "," + txtWeek.getSelectedItem().toString() + "," + VisitType + "," + txtVisit.getSelectedItem().toString() + "," + Global.DateConvertYMD(dtpVDate.getText().toString());
                    C.Save("Update Child set Upload='2',Absent_Sick='" + RefInfo1 + "' where ChildId='" + ChildID + "'");
                } else {
                    C.Save("Update Child set Upload='2',Absent_Sick='' where ChildId='" + ChildID + "'");
                }

            }
            //Outside Child Information: 25 May 2016
            else if(Child_Outside_Area.equals("y"))
            {
                String SQLStr = "";
                String VariableList = "";
                String Res = "";

                //Save Child Information
                //Table: Child_Outside
                //--------------------------------------------------------------------------------------
                SQLStr = "Select ChildId, c.Vill, c.bari, HH, SNo, PID, CID, Name, Sex, (cast(YEAR(BDate) as varchar(4))+'-'+right('0'+ cast(MONTH(BDate) as varchar(2)),2)+'-'+right('0'+cast(DAY(BDate) as varchar(2)),2))BDate,";
                SQLStr += "AgeM, MoNo, MoPNO, MoName, FaNo, FaPNO, FaName, EnType, (cast(YEAR(EnDate) as varchar(4))+'-'+right('0'+ cast(MONTH(EnDate) as varchar(2)),2)+'-'+right('0'+cast(DAY(EnDate) as varchar(2)),2))EnDate,";
                SQLStr += "ExType, (cast(YEAR(ExDate) as varchar(4))+'-'+right('0'+ cast(MONTH(ExDate) as varchar(2)),2)+'-'+right('0'+cast(DAY(ExDate) as varchar(2)),2))ExDate,";
                SQLStr += "(cast(YEAR(VStDate) as varchar(4))+'-'+right('0'+ cast(MONTH(VStDate) as varchar(2)),2)+'-'+right('0'+cast(DAY(VStDate) as varchar(2)),2))VStDate,VHW, VHWCluster, VHWBlock, Referral, c.EnDt, c.UserId, c.Upload, c.UploadDt";
                SQLStr += " from Child c Where Vill+Bari+HH+SNo='" + ChildID + "'";

                TableName = "Child";
                VariableList = "ChildId, Vill, bari, HH, SNo, PID, CID, Name, Sex, BDate, AgeM, MoNo, MoPNO, MoName, FaNo, FaPNO, FaName, EnType, EnDate, ExType, ExDate, VStDate, VHW, VHWCluster, VHWBlock, Referral, EnDt, UserId, Upload, UploadDt";
                Res = C.DownloadJSON_OutsideChild(SQLStr,TableName,VariableList,"ChildId");

                if (rdoRef1.isChecked() | rdoRef2.isChecked()) {
                    String RefInfo = ChildID + "," + txtWeek.getSelectedItem().toString() + "," + VisitType + "," + txtVisit.getSelectedItem().toString() + "," + Global.DateConvertYMD(dtpVDate.getText().toString());

                    if (VisitType.equals("1"))
                        C.Save("Update Child set Upload='2',Referral='" + RefInfo + "' where ChildId='" + ChildID + "'");
                    else if (VisitType.equals("2"))
                        C.Save("Update Child set Upload='2',Referral_Foll='" + RefInfo + "' where ChildId='" + ChildID + "'");
                    else if (VisitType.equals("3"))
                        C.Save("Update Child set Upload='2',Referral_Add='" + RefInfo + "' where ChildId='" + ChildID + "'");

                }

                if (rdoHos3.isChecked()) {
                    String RefInfo1 = ChildID + "," + txtWeek.getSelectedItem().toString() + "," + VisitType + "," + txtVisit.getSelectedItem().toString() + "," + Global.DateConvertYMD(dtpVDate.getText().toString());
                    C.Save("Update Child_Outside set Upload='2',Absent_Sick='" + RefInfo1 + "' where ChildId='" + ChildID + "'");
                } else {
                    C.Save("Update Child_Outside set Upload='2',Absent_Sick='' where ChildId='" + ChildID + "'");
                }
            }


            if(rdoRef1.isChecked()) {

//                AlertDialog.Builder alert=new AlertDialog.Builder(this);
//                alert.setTitle("Confirm");
//                alert.setMessage("Do you want to send the message?");
//                alert.setNegativeButton("No", null);
//                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int asd) {
//
//                        String VHWCl;
//                        VHWCl=C.ReturnSingleValue("select b.Cluster from Child c left outer join Bari b on b.Vill=SUBSTR(c.CID,1,3) and b.Bari=SUBSTR(c.CID,4,4) where CID='" + CID + "'");
//
//                        CONTACT_NO = txtPhone.getText().toString();
////                        String[] mob={CONTACT_NO};
//                        //String[] mob={"01813364948"};
//                        String[] mob = {CONTACT_NO,"01995207371"};
////                        String[] mob = {CONTACT_NO,"01739957707"};
//                        String SMS = "" +
//                                "CID: " + CID + "" +
//                                "\nPNO: " + PID + "" +
//                                "\nনাম" + NAME + "" +
//                                "\nপিত/মাতা: " + FM + "" +
//                                "\nজন্ম তারিখ: " + Global.DateConvertDMY(BDATE) + "" +
//                                "\nগ্রাম: " + VILLAGE.split(",")[1] + "" +
//                                "\nSlip: " + txtRSlip.getText().toString() + "" +
//                                "\nRefer DT: " + dtpVDate.getText().toString()+ "" +
//                                "\nVHW Cluster:" + VHWCl + "" +
//                                "\nতত্ত্বাবধানে: সি এইচ আর এফ";
//                        for (int i = 0; i < mob.length; i++) sendSMS(mob[i], SMS);
//
//
//                        Intent returnIntent = new Intent();
//                        setResult(Activity.RESULT_OK, returnIntent);
//                    }
//                });
//
//                AlertDialog alertDialog=alert.create();
//                alertDialog.show();

//            finish();
            }


//            //******************RSV


                Bundle IDbundle = new Bundle();
                Intent f1;
                IDbundle.putString("childid", ChildID);
                IDbundle.putString("pid", txtPID.getText().toString());
                IDbundle.putString("weekno", WeekNo);
                IDbundle.putString("fm", txtFMName.getText().toString());
                IDbundle.putString("aged", AgeD);
                IDbundle.putString("agem", AgeM);
                IDbundle.putString("agedm", AgeD+" দিন");
                IDbundle.putString("bdate", DOB);
                IDbundle.putString("name", txtName.getText().toString());
                IDbundle.putString("visittype", VisitType);
                IDbundle.putString("visitno", "0");
                IDbundle.putString("visitdate", dtpVDate.getText().toString());

                IDbundle.putString("temp", txttemp);
                IDbundle.putString("Cough", "");
                IDbundle.putString("CoughDt", "");
                IDbundle.putString("DBrea", "");
                IDbundle.putString("DBreaDt", "");
                IDbundle.putString("source", "nb");


                f1 = new Intent(getApplicationContext(), RSV.class);
                f1.putExtras(IDbundle);
                startActivityForResult(f1, 1);
//                //******************RSV


            Connection.MessageBox(AssNewBorn.this, "Saved Successfully");
        }
        catch(Exception  e)
        {
            Connection.MessageBox(AssNewBorn.this, e.getMessage());
            return;
        }
    }

//    28/07/2020
    private void sendSMS(String phoneNumber, String message)
    {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
//        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
        try {
            if(message.length() > 80) {
                ArrayList<String> messageList = SmsManager.getDefault().divideMessage(message);
                sms.sendMultipartTextMessage(phoneNumber, null, messageList, null, null);
            } else {
                sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
            }
        } catch (Exception e) {
            Log.e("SmsProvider", "" + e);
        }
    }
//
    private void DataSearchPhone(String ChildID)
    {
        try
        {
            Cursor cur = C.ReadData("Select * from Child Where ChildId='"+ ChildID +"'");
            cur.moveToFirst();
            while(!cur.isAfterLast())
            {

                txtPhone.setText(cur.getString(cur.getColumnIndex("ContactNo")));


                cur.moveToNext();
            }
            cur.close();
        }
        catch(Exception  e)
        {
            Connection.MessageBox(AssNewBorn.this, e.getMessage());
            return;
        }
    }


    private void DataSearch(String ChildId, String Week, String VType, String Visit)
    {
        try
        {

            RadioButton rb;
            //Cursor cur = C.ReadData("Select * from "+ TableName +"  Where ChildId='"+ ChildID +"' and Week='"+ Week +"' and VType='"+ VType +"' and Visit='"+ Visit +"'");
            Cursor cur = C.ReadData("Select * from "+ TableName +"  Where ChildId='"+ ChildID +"' and Week='"+ Week +"' and Visit='"+ Visit +"'");
            cur.moveToFirst();
            while(!cur.isAfterLast())
            {
                if(VisitNo.equals("0"))
                {
                    txtVisit.setEnabled(false);
                }
                else
                {
                    txtVisit.setEnabled(true);
                }

                txtTemp.setText(cur.getString(cur.getColumnIndex("Temp")));
                String a= cur.getString(cur.getColumnIndex("Temp"));

//                if(cur.getString(cur.getColumnIndex("tempDk")).equals("1"))
//                {
//                    chkTemp.setChecked(true);
//                }
//                else if(cur.getString(cur.getColumnIndex("tempDk")).equals("2"))
//                {
//                    chkTemp.setChecked(false);
//                }
//                27/07/2020
                if(cur.getString(cur.getColumnIndex("tempDk")).equals("2"))
                    rdoTemp1.setChecked(true);
                else if(cur.getString(cur.getColumnIndex("tempDk")).equals("1"))
                    rdoTemp2.setChecked(true);
                else if(cur.getString(cur.getColumnIndex("tempDk")).equals("3"))
                    rdoTemp3.setChecked(true);
                else
                    rdogrpTemp1.clearCheck();
//

//                if(a.equals(""))
//                {
//                    chkTemp.setChecked(true);
//                }
//
//                else
//                {
//                    chkTemp.setChecked(false);
//                }
                txtWeek.setSelection(Global.SpinnerItemPosition(txtWeek,3,cur.getString(cur.getColumnIndex("Week"))));
                VisitType = cur.getString(cur.getColumnIndex("VType"));
                if(VisitType.equals("1")) {
                    lblVisit.setText("সাপ্তাহিক পরিদর্শন");
                    txtWeek.setEnabled(false);
                }
                else if(VisitType.equals("2")) {
                    lblVisit.setText("ফলোআপ পরিদর্শন");
                    txtWeek.setEnabled(true);
                }
                else if(VisitType.equals("3")) {
                    lblVisit.setText("অতিরিক্ত পরিদর্শন");
                    txtWeek.setEnabled(false);
                }

                //txtWeek.setText(cur.getString(cur.getColumnIndex("Week")));
                txtVisit.setSelection(Global.SpinnerItemPosition(txtVisit, 1, cur.getString(cur.getColumnIndex("Visit"))));
                dtpVDate.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("VDate"))));
                spnOth1.setSelection(Global.SpinnerItemPosition(spnOth1, 2 ,cur.getString(cur.getColumnIndex("Oth1"))));
                spnOth2.setSelection(Global.SpinnerItemPosition(spnOth2, 2 ,cur.getString(cur.getColumnIndex("Oth2"))));
                spnOth3.setSelection(Global.SpinnerItemPosition(spnOth3, 2 ,cur.getString(cur.getColumnIndex("Oth3"))));
                for (int i = 0; i < rdogrpHNoCry.getChildCount(); i++)
                {
                    rb = (RadioButton)rdogrpHNoCry.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("HNoCry"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpHNoBrea.getChildCount(); i++)
                {
                    rb = (RadioButton)rdogrpHNoBrea.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("HNoBrea"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpHConv.getChildCount(); i++)
                {
                    rb = (RadioButton)rdogrpHConv.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("HConv"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpHUncon.getChildCount(); i++)
                {
                    rb = (RadioButton)rdogrpHUncon.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("HUncon"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpHDBrea.getChildCount(); i++)
                {
                    rb = (RadioButton)rdogrpHDBrea.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("HDBrea"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpHJaund.getChildCount(); i++)
                {
                    rb = (RadioButton)rdogrpHJaund.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("HJaund"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpHHFever.getChildCount(); i++)
                {
                    rb = (RadioButton)rdogrpHHFever.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("HHFever"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpHLFever.getChildCount(); i++)
                {
                    rb = (RadioButton)rdogrpHLFever.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("HLFever"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpHSkin.getChildCount(); i++)
                {
                    rb = (RadioButton)rdogrpHSkin.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("HSkin"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpHFedp.getChildCount(); i++)
                {
                    rb = (RadioButton)rdogrpHFedp.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("HFedp"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpHPus.getChildCount(); i++)
                {
                    rb = (RadioButton)rdogrpHPus.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("HPus"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpHVomit.getChildCount(); i++)
                {
                    rb = (RadioButton)rdogrpHVomit.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("HVomit"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpHWeak.getChildCount(); i++)
                {
                    rb = (RadioButton)rdogrpHWeak.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("HWeak"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpHLeth.getChildCount(); i++)
                {
                    rb = (RadioButton)rdogrpHLeth.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("HLeth"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpAsses.getChildCount(); i++)
                {
                    rb = (RadioButton)rdogrpAsses.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Asses"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }

                if(cur.getString(cur.getColumnIndex("RRDk")).equals("1"))
                {
                    chkRR.setChecked(true);
                }
                else if(cur.getString(cur.getColumnIndex("RRDk")).equals("2"))
                {
                    chkRR.setChecked(false);
                }

                txtRR1.setText(cur.getString(cur.getColumnIndex("RR1")));
                txtRR2.setText(cur.getString(cur.getColumnIndex("RR2")));
                for (int i = 0; i < rdogrpNoCry.getChildCount(); i++)
                {
                    rb = (RadioButton)rdogrpNoCry.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("NoCry"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpGasp.getChildCount(); i++)
                {
                    rb = (RadioButton)rdogrpGasp.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Gasp"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpSBrea.getChildCount(); i++)
                {
                    rb = (RadioButton)rdogrpSBrea.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("SBrea"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpBirthAs.getChildCount(); i++)
                {
                    rb = (RadioButton)rdogrpBirthAs.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("BirthAs"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpConv.getChildCount(); i++)
                {
                    rb = (RadioButton)rdogrpConv.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Conv"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpRBrea.getChildCount(); i++)
                {
                    rb = (RadioButton)rdogrpRBrea.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("RBrea"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpCInd.getChildCount(); i++)
                {
                    rb = (RadioButton)rdogrpCInd.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("CInd"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpHFever.getChildCount(); i++)
                {
                    rb = (RadioButton)rdogrpHFever.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("HFever"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpHypo.getChildCount(); i++)
                {
                    rb = (RadioButton)rdogrpHypo.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Hypo"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpUCon.getChildCount(); i++)
                {
                    rb = (RadioButton)rdogrpUCon.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("UCon"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpPus.getChildCount(); i++)
                {
                    rb = (RadioButton)rdogrpPus.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Pus"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpUmbR.getChildCount(); i++)
                {
                    rb = (RadioButton)rdogrpUmbR.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("UmbR"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpWeak.getChildCount(); i++)
                {
                    rb = (RadioButton)rdogrpWeak.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Weak"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpLeth.getChildCount(); i++)
                {
                    rb = (RadioButton)rdogrpLeth.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Leth"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpNoFed.getChildCount(); i++)
                {
                    rb = (RadioButton)rdogrpNoFed.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("NoFed"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpVsd.getChildCount(); i++)
                {
                    rb = (RadioButton)rdogrpVsd.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Vsd"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpConvH.getChildCount(); i++)
                {
                    rb = (RadioButton)rdogrpConvH.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("ConvH"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpFonta.getChildCount(); i++)
                {
                    rb = (RadioButton)rdogrpFonta.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Fonta"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpVomit.getChildCount(); i++)
                {
                    rb = (RadioButton)rdogrpVomit.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Vomit"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpH1Fever.getChildCount(); i++)
                {
                    rb = (RadioButton)rdogrpH1Fever.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("H1Fever"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpLFever.getChildCount(); i++)
                {
                    rb = (RadioButton)rdogrpLFever.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("LFever"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpNJaun.getChildCount(); i++)
                {
                    rb = (RadioButton)rdogrpNJaun.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("NJaun"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpPvsd.getChildCount(); i++)
                {
                    rb = (RadioButton)rdogrpPvsd.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Pvsd"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpJaund.getChildCount(); i++)
                {
                    rb = (RadioButton)rdogrpJaund.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Jaund"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpSJaun.getChildCount(); i++)
                {
                    rb = (RadioButton)rdogrpSJaun.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("SJaun"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpEyeP.getChildCount(); i++)
                {
                    rb = (RadioButton)rdogrpEyeP.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("EyeP"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpGono.getChildCount(); i++)
                {
                    rb = (RadioButton)rdogrpGono.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Gono"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpSick.getChildCount(); i++)
                {
                    rb = (RadioButton)rdogrpSick.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Sick"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpRef.getChildCount(); i++)
                {
                    rb = (RadioButton)rdogrpRef.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Ref"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                txtRSlip.setText(cur.getString(cur.getColumnIndex("RSlip")));
                /*for (int i = 0; i < rdogrpComp.getChildCount(); i++)
                {
                    rb = (RadioButton)rdogrpComp.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Comp"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }*/
                for (int i = 0; i < rdogrpReason.getChildCount(); i++)
                {
                    rb = (RadioButton)rdogrpReason.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Reason"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                //spnReason.setSelection(Global.SpinnerItemPosition(spnReason, 1 ,cur.getString(cur.getColumnIndex("Reason"))));
                spnTPlace.setSelection(Global.SpinnerItemPosition(spnTPlace, 1 ,cur.getString(cur.getColumnIndex("TPlace"))));
                txtTPlaceC.setText(cur.getString(cur.getColumnIndex("TPlaceC")));
                //spnTPlaceC.setSelection(Global.SpinnerItemPosition(spnTPlaceC, 2 ,cur.getString(cur.getColumnIndex("TPlaceC"))));
                for (int i = 0; i < rdogrpTAbsIn.getChildCount(); i++)
                {
                    rb = (RadioButton)rdogrpTAbsIn.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 2).equalsIgnoreCase(cur.getString(cur.getColumnIndex("TAbsIn"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                txtTAbsDur.setText(cur.getString(cur.getColumnIndex("TAbsDur")));
                for (int i = 0; i < rdogrpHos.getChildCount(); i++)
                {
                    rb = (RadioButton)rdogrpHos.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Hos"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                // dtpEnDt.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("EnDt"))));
            /* txtUserId.setText(cur.getString(cur.getColumnIndex("UserId")));
             txtUpload.setText(cur.getString(cur.getColumnIndex("Upload")));
             dtpUploadDT.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("UploadDT"))));
*/
                cur.moveToNext();
            }
            cur.close();
        }
        catch(Exception  e)
        {
            Connection.MessageBox(AssNewBorn.this, e.getMessage());
            return;
        }
    }

    private void DataSearchName(String CID)
    {
        try
        {
            Cursor cur = C.ReadData("Select * from Child Where CID='31300010115'");
            cur.moveToFirst();
            while(!cur.isAfterLast())
            {
                //txtPID.setText(cur.getString(cur.getColumnIndex("PID")));
                txtName.setText(cur.getString(cur.getColumnIndex("Name")));

                cur.moveToNext();
            }
            cur.close();
        }
        catch(Exception  e)
        {
            Connection.MessageBox(AssNewBorn.this, e.getMessage());
            return;
        }
    }



    protected Dialog onCreateDialog(int id) {
        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        switch (id) {
            case DATE_DIALOG:
                return new DatePickerDialog(this, mDateSetListener,g.mYear,g.mMonth-1,g.mDay);
      /* case TIME_DIALOG:
           return new TimePickerDialog(this, timePickerListener, hour, minute,false);*/
        }
        return null;
    }
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year; mMonth = monthOfYear+1; mDay = dayOfMonth;
            EditText dtpDate;


            dtpDate = (EditText)findViewById(R.id.dtpVDate);
            if (VariableID.equals("btnVDate"))
            {
                dtpDate = (EditText)findViewById(R.id.dtpVDate);
            }
            else if (VariableID.equals("btnEnDt"))
            {
                // dtpDate = (EditText)findViewById(R.id.dtpEnDt);
            }
             /*else if (VariableID.equals("btnUploadDT"))
              {
                  //dtpDate = (EditText)findViewById(R.id.dtpUploadDT);
              }*/
            dtpDate.setText(new StringBuilder()
                    .append(Global.Right("00"+mDay,2)).append("/")
                    .append(Global.Right("00"+mMonth,2)).append("/")
                    .append(mYear));
        }
    };

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
            hour = selectedHour; minute = selectedMinute;
            EditText tpTime;


            //tpTime.setText(new StringBuilder().append(Global.Right("00"+hour,2)).append(":").append(Global.Right("00"+minute,2)));

        }
    };




    Chronometer mChronometer;
    MediaPlayer player1;
    long currentNeededTime1=60000;
    Vibrator vib;
    long elapsedTime;
    TextView txtCurrentTime;
    TextView startMsg;
    String Start;
    Integer alertTimeStart = 29999;
    Integer alertTimeEnd   = 30100;

    private void StopWatch(Integer time)
    {
        final Dialog dialog = new Dialog(AssNewBorn.this);
        try
        {
            dialog.setTitle("Clock");
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.stopwatch);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(true);

            final TextView title = (TextView)dialog.findViewById(R.id.title);
            //title.setText("Timer ("+ String.valueOf(time) +" Seconds)");

            //txtCurrentTime = (TextView)dialog.findViewById(R.id.txtCurrentTime);
            //startMsg = (TextView)dialog.findViewById(R.id.startMsg);
            //startMsg.setVisibility(View.VISIBLE);
            Start = "2";
            final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
            mChronometer = (Chronometer)dialog.findViewById(R.id.chronometer);
            //player1 = MediaPlayer.create(mainmenu.this, R.raw.apple_ring);
            vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

            //mChronometer.start();
            final Button cmdStart = (Button)dialog.findViewById(R.id.cmdStart);
            final Button cmdStop  = (Button)dialog.findViewById(R.id.cmdStop);
            final RadioGroup rdom = (RadioGroup)dialog.findViewById(R.id.rdogrpM);
            final RadioButton rdo1m = (RadioButton)dialog.findViewById(R.id.rdo1m);
            //final RadioButton rdo2m = (RadioButton)dialog.findViewById(R.id.rdo2m);
            final RadioButton rdo3m = (RadioButton)dialog.findViewById(R.id.rdo3m);

            cmdStop.setText("Close");
            //cmdStart.setBackgroundColor(Color.GREEN);
            cmdStart.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if(cmdStart.getText().toString().trim().equals("Start")) {
                        mChronometer.stop();
                        mChronometer.setBase(SystemClock.elapsedRealtime());

                        if(rdo1m.isChecked()) {
                            alertTimeStart = 29999;
                            alertTimeEnd = 30100;
                        }
                        else
                        {
                            alertTimeStart = 29999*3;
                            alertTimeEnd = 30100*3;
                        }
                        mChronometer.start();
                        tg.startTone(ToneGenerator.TONE_CDMA_ABBR_ALERT);
                        vib.vibrate(1000);
                        cmdStart.setText("Stop");
                    }
                    else if(cmdStart.getText().toString().trim().equals("Stop")) {

                        mChronometer.stop();
                        cmdStart.setText("Start");
                    }
                }});
            cmdStop.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //mChronometer.stop();
                    dialog.dismiss();
                }
            });

            rdom.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int radioButtonID) {
                    if (rdo1m.isChecked()) {
                        currentNeededTime1 = 60000;
                        //} else if (rdo2m.isChecked()) {
                        //    currentNeededTime1 = 120000;
                    } else if (rdo3m.isChecked()) {
                        currentNeededTime1 = 180000;
                    }

                }});


            //----------------
            dialog.show();



            mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                @Override
                public void onChronometerTick(Chronometer chronometer) {
                    elapsedTime = SystemClock.elapsedRealtime() - chronometer.getBase();
                    //txtCurrentTime.setText(String.valueOf(elapsedTime));
                    if (elapsedTime <= currentNeededTime1 & Start == "1") {
                        //tg.startTone(ToneGenerator.TONE_PROP_BEEP2);
                    }
                    if (elapsedTime >= 999 & elapsedTime <= 1100 & Start == "2") {
                        mChronometer.stop();
                        mChronometer.setBase(SystemClock.elapsedRealtime());
                        chronometer.setVisibility(View.VISIBLE);
                        mChronometer.start();
                        //tg.startTone(ToneGenerator.TONE_CDMA_ABBR_ALERT);
                        vib.vibrate(1000);
                        Start = "1";

                    }
                    else if (elapsedTime >= alertTimeStart & elapsedTime <= alertTimeEnd & Start == "1") {
                        tg.startTone(ToneGenerator.TONE_CDMA_ABBR_ALERT);
                        if(rdo1m.isChecked()) {
                            alertTimeStart += 29999;
                            alertTimeEnd += 30100;
                        }
                        else
                        {
                            alertTimeStart += 29999*3;
                            alertTimeEnd += 30100*3;
                        }
                    }
                    else if (elapsedTime > currentNeededTime1) {

                        tg.startTone(ToneGenerator.TONE_CDMA_ABBR_ALERT);
                        tg.startTone(ToneGenerator.TONE_CDMA_ABBR_ALERT);
                        tg.stopTone();
                        vib.vibrate(1000);
                        mChronometer.stop();
                        Start = "2";
                        alertTimeStart = 29999;
                        alertTimeEnd   = 30100;

                        dialog.dismiss();
                    } else {
                        if (elapsedTime <= currentNeededTime1 & Start == "1") {
                            //tg.startTone(ToneGenerator.TONE_PROP_BEEP);
                        }
                    }
                }
            });

        }

        catch(
                Exception ex
                )

        {
            Connection.MessageBox(AssNewBorn.this, ex.getMessage());
            return;
        }

    }

}

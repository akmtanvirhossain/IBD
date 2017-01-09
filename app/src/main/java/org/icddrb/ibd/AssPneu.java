package org.icddrb.ibd;

/**
 * Created by TanvirHossain on 03/12/2015.
 */
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

//import ccah.icddrb.ClinicalInformation;
import android.app.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.ToneGenerator;
import android.os.SystemClock;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;

import Common.Connection;
import Common.Global;

public class AssPneu extends Activity {
    boolean netwoekAvailable = false;
    Location currentLocation;
    double currentLatitude, currentLongitude;
    Location currentLocationNet;
    double currentLatitudeNet, currentLongitudeNet;

    // Disabled Back/Home key
    // --------------------------------------------------------------------------------------------------
    @Override
    public boolean onKeyDown(int iKeyCode, KeyEvent event) {
        if (iKeyCode == KeyEvent.KEYCODE_BACK
                || iKeyCode == KeyEvent.KEYCODE_HOME) {
            return false;
        } else {
            return true;
        }
    }

    // Top menu
    // --------------------------------------------------------------------------------------------------
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mnuclose_timer, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog.Builder adb = new AlertDialog.Builder(AssPneu.this);
        switch (item.getItemId()) {
            case R.id.menuClose:
                adb.setTitle("Close");
                adb.setMessage("আপনি কি এই ফর্ম থেকে বের হতে চান[Yes/No]?");
                adb.setNegativeButton("No", null);
                adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //g.setIdNo("");
                        //g.setmemSlNo("");
                        //g.setmemRID("");

                        finish();
                    }
                });
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

    TextView VlblVT;
    LinearLayout secChildId;
    TextView VlblChildId;
    TextView txtChildId;
    TextView txtName;
    LinearLayout secCID;
    TextView VlblCID;
    //EditText txtCID;
    //LinearLayout secPID;
    TextView VlblPID;
    //EditText txtPID;
    LinearLayout secWeek;
    TextView VlblWeek;
    Spinner txtWeek;
    LinearLayout secVDate;
    TextView VlblVDate;
    EditText dtpVDate;
    ImageButton btnVDate;
    LinearLayout secVType;
    TextView VlblVType;
    /*RadioGroup rdogrpVType;

    RadioButton rdoVType1;
    RadioButton rdoVType2;
    RadioButton rdoVType3;
    */
    //LinearLayout secVisit;
    TextView VlblVisit;
    Spinner txtVisit;
    CheckBox chkTemp;
    LinearLayout sectemp;
    TextView Vlbltemp;
    EditText txttemp;


    LinearLayout secFeverChk_lbl;
    CheckBox chkFever;
    TextView VlblfeverChk_lbl;

    LinearLayout secCough;
    TextView VlblCough;
    RadioGroup rdogrpCough;

    RadioButton rdoCough1;
    RadioButton rdoCough2;
    LinearLayout secCoughDt;
    TextView VlblCoughDt;
    EditText dtpCoughDt;
    ImageButton btnCoughDt;
    LinearLayout secDBrea;
    TextView VlblDBrea;
    RadioGroup rdogrpDBrea;

    RadioButton rdoDBrea1;
    RadioButton rdoDBrea2;
    LinearLayout secDBreaDt;
    TextView VlblDBreaDt;
    EditText dtpDBreaDt;
    ImageButton btnDBreaDt;
    LinearLayout secFever;
    TextView VlblFever;
    RadioGroup rdogrpFever;

    RadioButton rdoFever1;
    RadioButton rdoFever2;
    LinearLayout secFeverDt;
    TextView VlblFeverDt;
    EditText dtpFeverDt;
    ImageButton btnFeverDt;
    LinearLayout secOthCom1;
    TextView VlblOthCom1;
    Spinner spnOthCom1;
    LinearLayout secOthCom2;
    TextView VlblOthCom2;
    Spinner spnOthCom2;
    LinearLayout secOthCom3;
    TextView VlblOthCom3;
    Spinner spnOthCom3;
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
    LinearLayout secCof_lbl;
    LinearLayout secConv;
    TextView VlblConv;
    RadioGroup rdogrpConv;

    RadioButton rdoConv1;
    RadioButton rdoConv2;
    LinearLayout secFBrea;
    TextView VlblFBrea;
    RadioGroup rdogrpFBrea;

    RadioButton rdoFBrea1;
    RadioButton rdoFBrea2;
    LinearLayout secCInd;
    TextView VlblCInd;
    RadioGroup rdogrpCInd;

    RadioButton rdoCInd1;
    RadioButton rdoCInd2;
    LinearLayout secLeth;
    TextView VlblLeth;
    RadioGroup rdogrpLeth;

    RadioButton rdoLeth1;
    RadioButton rdoLeth2;
    LinearLayout secUCon;
    TextView VlblUCon;
    RadioGroup rdogrpUCon;

    RadioButton rdoUCon1;
    RadioButton rdoUCon2;
    LinearLayout secDrink;
    TextView VlblDrink;
    RadioGroup rdogrpDrink;

    RadioButton rdoDrink1;
    RadioButton rdoDrink2;
    LinearLayout secVomit;
    TextView VlblVomit;
    RadioGroup rdogrpVomit;

    RadioButton rdoVomit1;
    RadioButton rdoVomit2;
    LinearLayout secNone;
    TextView VlblNone;
    RadioGroup rdogrpNone;

    LinearLayout secCofCls_lbl;

    RadioButton rdoNone1;
    RadioButton rdoNone2;
    LinearLayout secCSPne;
    TextView VlblCSPne;
    RadioGroup rdogrpCSPne;

    RadioButton rdoCSPne1;
    RadioButton rdoCSPne2;
    LinearLayout secCPPne;
    TextView VlblCPPne;
    RadioGroup rdogrpCPPne;

    RadioButton rdoCPPne1;
    RadioButton rdoCPPne2;
    LinearLayout secCNPne;
    TextView VlblCNPne;
    RadioGroup rdogrpCNPne;

    LinearLayout secCofMan_lbl;

    RadioButton rdoCNPne1;
    RadioButton rdoCNPne2;
    LinearLayout secTSPne;
    TextView VlblTSPne;
    RadioGroup rdogrpTSPne;

    RadioButton rdoTSPne1;
    RadioButton rdoTSPne2;
    LinearLayout secTPPne;
    TextView VlblTPPne;
    RadioGroup rdogrpTPPne;

    RadioButton rdoTPPne1;
    RadioButton rdoTPPne2;
    LinearLayout secTNPne;
    TextView VlblTNPne;
    RadioGroup rdogrpTNPne;

    LinearLayout secFever_lbl;

    RadioButton rdoTNPne1;
    RadioButton rdoTNPne2;
    LinearLayout secLFever;
    TextView VlblLFever;
    RadioGroup rdogrpLFever;

    RadioButton rdoLFever1;
    RadioButton rdoLFever2;
    LinearLayout secCLFever;
    TextView VlblCLFever;
    RadioGroup rdogrpCLFever;

    RadioButton rdoCLFever1;
    RadioButton rdoCLFever2;
    LinearLayout secTLFever;
    TextView VlblTLFever;
    RadioGroup rdogrpTLFever;

    RadioButton rdoTLFever1;
    RadioButton rdoTLFever2;
    LinearLayout secMFever;
    TextView VlblMFever;
    RadioGroup rdogrpMFever;

    RadioButton rdoMFever1;
    RadioButton rdoMFever2;
    LinearLayout secCMFever;
    TextView VlblCMFever;
    RadioGroup rdogrpCMFever;

    RadioButton rdoCMFever1;
    RadioButton rdoCMFever2;
    LinearLayout secTMFever;
    TextView VlblTMFever;
    RadioGroup rdogrpTMFever;

    RadioButton rdoTMFever1;
    RadioButton rdoTMFever2;
    LinearLayout secHFever;
    TextView VlblHFever;
    RadioGroup rdogrpHFever;

    RadioButton rdoHFever1;
    RadioButton rdoHFever2;
    LinearLayout secCHFever;
    TextView VlblCHFever;
    RadioGroup rdogrpCHFever;

    RadioButton rdoCHFever1;
    RadioButton rdoCHFever2;
    LinearLayout secTHFever;
    TextView VlblTHFever;
    RadioGroup rdogrpTHFever;

    LinearLayout secFSick_lbl;

    RadioButton rdoTHFever1;
    RadioButton rdoTHFever2;
    LinearLayout secNeck;
    TextView VlblNeck;
    RadioGroup rdogrpNeck;

    RadioButton rdoNeck1;
    RadioButton rdoNeck2;
    LinearLayout secFonta;
    TextView VlblFonta;
    RadioGroup rdogrpFonta;

    RadioButton rdoFonta1;
    RadioButton rdoFonta2;
    LinearLayout secConv2;
    TextView VlblConv2;
    RadioGroup rdogrpConv2;

    RadioButton rdoConv21;
    RadioButton rdoConv22;
    LinearLayout secLeth2;
    TextView VlblLeth2;
    RadioGroup rdogrpLeth2;

    RadioButton rdoLeth21;
    RadioButton rdoLeth22;
    LinearLayout secUcon2;
    TextView VlblUcon2;
    RadioGroup rdogrpUcon2;

    RadioButton rdoUcon21;
    RadioButton rdoUcon22;
    LinearLayout secDrink2;
    TextView VlblDrink2;
    RadioGroup rdogrpDrink2;

    RadioButton rdoDrink21;
    RadioButton rdoDrink22;
    LinearLayout secVomit2;
    TextView VlblVomit2;
    RadioGroup rdogrpVomit2;

    RadioButton rdoVomit21;
    RadioButton rdoVomit22;
    LinearLayout secCMenin;
    TextView VlblCMenin;
    RadioGroup rdogrpCMenin;

    RadioButton rdoCMenin1;
    RadioButton rdoCMenin2;
    LinearLayout secTMenin;
    TextView VlblTMenin;
    RadioGroup rdogrpTMenin;

    RadioButton rdoTMenin1;
    RadioButton rdoTMenin2;
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

    RadioButton rdoReason1;
    RadioButton rdoReason2;
    RadioButton rdoReason3;
    LinearLayout secTPlace;
    TextView VlblTPlace;
    Spinner spnTPlace;
    LinearLayout secTPlaceC;
    TextView VlblTPlaceC;
    EditText txtTPlaceC;
    //Spinner spnTPlaceC;
    //LinearLayout secTAbsIn;
    TextView VlblTAbsIn;
    RadioGroup rdogrpTAbsIn;

    RadioButton rdoTAbsIn1;
    RadioButton rdoTAbsIn2;
    LinearLayout secTAbsDur;
    TextView VlblTAbsDur;
    EditText txtTAbsDur;
    LinearLayout secHos;
    TextView VlblHos;
    RadioGroup rdogrpHos;

    RadioButton rdoHos1;
    RadioButton rdoHos2;
    RadioButton rdoHos3;
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
    String a;
    String VisitSt;
    String St;


    String ChildID;
    String WeekNo;
    String VisitType;
    String VisitNo;
    String AgeM;
    String AgeD;
    String ChildPresent;
    String VisitStatus;
    String Child_Outside_Area;

    TextView lblVisit;
    TextView Age;
    TextView Dob;
    String DOB;
    TextView txtFMName;
    TextView txtPID;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.asspneu);
            C = new Connection(this);
            g = Global.getInstance();
            StartTime = g.CurrentTime24();

            TableName = "AssPneu";

            Bundle B    = new Bundle();
            B	        = getIntent().getExtras();
            ChildID     = B.getString("childid");
            AgeD        = B.getString("aged");
            AgeM        = B.getString("agem");
            WeekNo      = B.getString("weekno");
            VisitType   = B.getString("visittype");
            VisitNo     = B.getString("visitno");
            ChildPresent= B.getString("childpresent");
            DOB         = B.getString("bdate");
            VisitStatus = B.getString("visitstatus");
            Child_Outside_Area = B.getString("child_outside_area");

            VisitStatus = VisitStatus==null?"":VisitStatus;

            txtPID = (TextView)findViewById(R.id.txtPID);
            txtPID.setText(B.getString("pid"));
            //VlblVT= (TextView) findViewById(R.id.VlblVT);
            secCof_lbl = (LinearLayout) findViewById(R.id.secCof_lbl);
            secCofCls_lbl = (LinearLayout) findViewById(R.id.secCofCls_lbl);
            secCofMan_lbl = (LinearLayout) findViewById(R.id.secCofMan_lbl);
            secFever_lbl = (LinearLayout) findViewById(R.id.secFever_lbl);
            secFSick_lbl = (LinearLayout) findViewById(R.id.secFSick_lbl);
            secChildId = (LinearLayout) findViewById(R.id.secChildId);
            VlblChildId = (TextView) findViewById(R.id.VlblChildId);
            txtChildId = (TextView) findViewById(R.id.txtChildId);
            txtChildId.setText(ChildID);
            txtName = (TextView) findViewById(R.id.txtName);
            txtName.setText(B.getString("name"));
            txtFMName = (TextView)findViewById(R.id.txtFMName);
            txtFMName.setText(": "+ B.getString("fm"));
            secCID = (LinearLayout) findViewById(R.id.secCID);
            VlblCID = (TextView) findViewById(R.id.VlblCID);
            //txtCID = (EditText) findViewById(R.id.txtCID);
            //txtCID.setText(ChildID);
            //secPID = (LinearLayout) findViewById(R.id.secPID);
            VlblPID = (TextView) findViewById(R.id.VlblPID);
            //txtPID = (EditText) findViewById(R.id.txtPID);
            secWeek = (LinearLayout) findViewById(R.id.secWeek);
            VlblWeek = (TextView) findViewById(R.id.VlblWeek);

            txtWeek = (Spinner) findViewById(R.id.txtWeek);
            txtWeek.setAdapter(C.getArrayAdapter("select week from WeeklyVstDt order by CAST(week as int) desc limit 100"));
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

            txtVisit=(Spinner) findViewById(R.id.txtVisit);
            txtVisit.setEnabled(true);

            //For new Assessment
            if(VisitNo.length()==0) {
                VisitNo = C.ReturnSingleValue("select (ifnull(max(cast(Visit as int)),0)+1) VNo from AssPneu where childid='" + ChildID + "' and Week='" + WeekNo + "'");
                //VisitNo = VN.equals("0") ? "0" : String.valueOf(Integer.valueOf(VN) + 1);
                String SQL = "";
                for(int i=0;i<=Integer.valueOf(VisitNo);i++)
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
                txtVisit.setEnabled(false);
            }
            else
            {
                txtVisit.setAdapter(C.getArrayAdapter("Select '1' union Select '2' union Select '3' union Select '4' union Select '5' union Select '6'"));
                txtVisit.setSelection(Global.SpinnerItemPosition(txtVisit,1,VisitNo));
                txtVisit.setEnabled(false);
            }


            Age = (TextView)findViewById(R.id.Age);
            Age.setText(": "+ B.getString("agedm"));

            /*if(Integer.valueOf(AgeD) <= 28)
            {
                Age.setText(": "+ AgeD.toString()+" দিন");
            }
            else
            {
                Age.setText(": "+ AgeM.toString()+" মাস");
            }*/

            Dob = (TextView)findViewById(R.id.Dob);
            Dob.setText(": "+ Global.DateConvertDMY(DOB));

            VlblVisit = (TextView) findViewById(R.id.VlblVisit);
            secVDate = (LinearLayout) findViewById(R.id.secVDate);
            if(VisitType.equals("1"))
            {
                VlblVisit.setVisibility(View.GONE);
                txtVisit.setVisibility(View.GONE);
                txtVisit.setSelection(0);
            }
            //follow-up visit
            else if(VisitType.equals("2"))
            {
                VlblVisit.setVisibility(View.VISIBLE);
                txtVisit.setVisibility(View.VISIBLE);
                secVDate.setVisibility(View.VISIBLE);
            }
            //additional visit
            else if(VisitType.equals("3"))
            {
                VlblVisit.setVisibility(View.VISIBLE);
                txtVisit.setVisibility(View.VISIBLE);
                secVDate.setVisibility(View.VISIBLE);
                txtVisit.setSelection(1);
                txtVisit.setEnabled(false);
            }
            else
            {
                VlblVisit.setVisibility(View.VISIBLE);
                txtVisit.setVisibility(View.VISIBLE);
                secVDate.setVisibility(View.VISIBLE);
            }


            VlblVDate = (TextView) findViewById(R.id.VlblVDate);
            dtpVDate = (EditText) findViewById(R.id.dtpVDate);

            if(B.getString("visitdate")==null)
            {
                dtpVDate.setText(Global.DateNowDMY());
            }
            else {
                if (B.getString("visitdate").toString().length() == 0)
                    dtpVDate.setText(Global.DateNowDMY());
                else
                    dtpVDate.setText(B.getString("visitdate"));
            }
            //dtpVDate.setText(Global.DateNowDMY());

            //secVType = (LinearLayout) findViewById(R.id.secVType);
            //VlblVType = (TextView) findViewById(R.id.VlblVType);
            /*rdogrpVType = (RadioGroup) findViewById(R.id.rdogrpVType);
            //secVisit = (LinearLayout) findViewById(R.id.secVisit);
            rdoVType1 = (RadioButton) findViewById(R.id.rdoVType1);
            rdoVType2 = (RadioButton) findViewById(R.id.rdoVType2);
            rdoVType3 = (RadioButton) findViewById(R.id.rdoVType3);
            */

            sectemp = (LinearLayout) findViewById(R.id.sectemp);
            Vlbltemp = (TextView) findViewById(R.id.Vlbltemp);
            chkTemp=(CheckBox)findViewById(R.id.chkTemp);
            chkTemp.setOnCheckedChangeListener(new OnCheckedChangeListener()
            {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    if ( isChecked )
                    {
                        txttemp.setEnabled(false);
                        txttemp.setText("");

                        secFeverDt.setVisibility(View.GONE);
                        dtpFeverDt.setText("");
                        secFever_lbl.setVisibility(View.GONE);

                        secFeverChk_lbl.setVisibility(View.GONE);

                        secLFever.setVisibility(View.GONE);
                        rdogrpLFever.clearCheck();
                        secCLFever.setVisibility(View.GONE);
                        rdogrpCLFever.clearCheck();
                        secTLFever.setVisibility(View.GONE);
                        rdogrpTLFever.clearCheck();

                        secMFever.setVisibility(View.GONE);
                        rdogrpMFever.clearCheck();
                        secCMFever.setVisibility(View.GONE);
                        rdogrpCMFever.clearCheck();
                        secTMFever.setVisibility(View.GONE);
                        rdogrpTMFever.clearCheck();

                        secHFever.setVisibility(View.GONE);
                        rdogrpHFever.clearCheck();
                        secCHFever.setVisibility(View.GONE);
                        rdogrpCHFever.clearCheck();
                        secTHFever.setVisibility(View.GONE);
                        rdogrpTHFever.clearCheck();

                        secFSick_lbl.setVisibility(View.GONE);
                        secNeck.setVisibility(View.GONE);
                        rdogrpNeck.clearCheck();
                        secFonta.setVisibility(View.GONE);
                        rdogrpFonta.clearCheck();
                        secConv2.setVisibility(View.GONE);
                        rdogrpConv2.clearCheck();
                        secLeth2.setVisibility(View.GONE);
                        rdogrpLeth2.clearCheck();
                        secUcon2.setVisibility(View.GONE);
                        rdogrpUcon2.clearCheck();
                        secDrink2.setVisibility(View.GONE);
                        rdogrpDrink2.clearCheck();
                        secVomit2.setVisibility(View.GONE);
                        rdogrpVomit2.clearCheck();
                        secCMenin.setVisibility(View.GONE);
                        rdogrpCMenin.clearCheck();
                        secTMenin.setVisibility(View.GONE);
                        rdogrpTMenin.clearCheck();
                    }
                    else
                    {
                        txttemp.setEnabled(true);
                    }

                }
            });

            txttemp = (EditText) findViewById(R.id.txttemp);
            txttemp.addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable et) {
                    if (et.length() > 0) {
                        if (txttemp.getText().toString().length() > 0) {
                            String s = txttemp.getText().toString();

/*                            Float Temp1=Float.parseFloat(s);
                            if(Temp1<94 || Temp1>108)
                            {
                                C.MessageBox(AssPneu.this, "Temperature range should be 94-108");
                                //txttemp.setText(null);
                                //txttemp.requestFocus();
                                return;
                            }*/
                            double Temp =Double.parseDouble(s) ;

							/*
							 * if(txttemp.getText().toString().trim().
							 * equalsIgnoreCase("999.9")) {
							 *
							 * }
							 */
                            //String CDOB1;
                            //CDOB1=C.ReturnSingleValue("Select BDate  from Child WHERE   CID = '"+ ChildID +"'");

                            int dy=Global.DateDifferenceDays(dtpVDate.getText().toString(),Global.DateConvertDMY(DOB));
                            //int dy=Global.DateDifferenceDays(dtpVDate.getText().toString(),Global.DateConvertDMY(CDOB1));

                            if(Temp>108)
                            {
                                C.MessageBox(AssPneu.this, "Temperature range should be less then 108");
                                //txttemp.setText(null);
                                //txttemp.requestFocus();
                                return;
                            }
                            if (Temp >= 99.5) {
                                secFeverDt.setVisibility(View.VISIBLE);
                                dtpFeverDt.setText("");

                                secFever_lbl.setVisibility(View.VISIBLE);
                                secLFever.setVisibility(View.VISIBLE);
                                rdogrpLFever.clearCheck();
                                secCLFever.setVisibility(View.VISIBLE);
                                rdogrpCLFever.clearCheck();
                                secTLFever.setVisibility(View.VISIBLE);
                                rdogrpTLFever.clearCheck();

                                secMFever.setVisibility(View.VISIBLE);
                                rdogrpMFever.clearCheck();
                                secCMFever.setVisibility(View.VISIBLE);
                                rdogrpCMFever.clearCheck();
                                secTMFever.setVisibility(View.VISIBLE);
                                rdogrpTMFever.clearCheck();

                                secHFever.setVisibility(View.VISIBLE);
                                rdogrpHFever.clearCheck();
                                secCHFever.setVisibility(View.VISIBLE);
                                rdogrpCHFever.clearCheck();
                                secTHFever.setVisibility(View.VISIBLE);
                                rdogrpTHFever.clearCheck();

                                secFSick_lbl.setVisibility(View.VISIBLE);
                                secNeck.setVisibility(View.VISIBLE);
                                rdogrpNeck.clearCheck();
                                secFonta.setVisibility(View.VISIBLE);
                                rdogrpFonta.clearCheck();
                                secConv2.setVisibility(View.VISIBLE);
                                rdogrpConv2.clearCheck();
                                secLeth2.setVisibility(View.VISIBLE);
                                rdogrpLeth2.clearCheck();
                                secUcon2.setVisibility(View.VISIBLE);
                                rdogrpUcon2.clearCheck();
                                secDrink2.setVisibility(View.VISIBLE);
                                rdogrpDrink2.clearCheck();
                                secVomit2.setVisibility(View.VISIBLE);
                                rdogrpVomit2.clearCheck();
                                secCMenin.setVisibility(View.VISIBLE);
                                rdogrpCMenin.clearCheck();
                                secTMenin.setVisibility(View.VISIBLE);
                                rdogrpTMenin.clearCheck();
                            }

                            else if	(Temp < 99.5) {
                                secFeverDt.setVisibility(View.GONE);
                                dtpFeverDt.setText("");
                                secFever_lbl.setVisibility(View.GONE);
                                secLFever.setVisibility(View.GONE);
                                rdogrpLFever.clearCheck();
                                secCLFever.setVisibility(View.GONE);
                                rdogrpCLFever.clearCheck();
                                secTLFever.setVisibility(View.GONE);
                                rdogrpTLFever.clearCheck();

                                secMFever.setVisibility(View.GONE);
                                rdogrpMFever.clearCheck();
                                secCMFever.setVisibility(View.GONE);
                                rdogrpCMFever.clearCheck();
                                secTMFever.setVisibility(View.GONE);
                                rdogrpTMFever.clearCheck();

                                secHFever.setVisibility(View.GONE);
                                rdogrpHFever.clearCheck();
                                secCHFever.setVisibility(View.GONE);
                                rdogrpCHFever.clearCheck();
                                secTHFever.setVisibility(View.GONE);
                                rdogrpTHFever.clearCheck();

                                secFSick_lbl.setVisibility(View.GONE);
                                secNeck.setVisibility(View.GONE);
                                rdogrpNeck.clearCheck();
                                secFonta.setVisibility(View.GONE);
                                rdogrpFonta.clearCheck();
                                secConv2.setVisibility(View.GONE);
                                rdogrpConv2.clearCheck();
                                secLeth2.setVisibility(View.GONE);
                                rdogrpLeth2.clearCheck();
                                secUcon2.setVisibility(View.GONE);
                                rdogrpUcon2.clearCheck();
                                secDrink2.setVisibility(View.GONE);
                                rdogrpDrink2.clearCheck();
                                secVomit2.setVisibility(View.GONE);
                                rdogrpVomit2.clearCheck();
                                secCMenin.setVisibility(View.GONE);
                                rdogrpCMenin.clearCheck();
                                secTMenin.setVisibility(View.GONE);
                                rdogrpTMenin.clearCheck();
                            }

                            if	(Temp >= 99.5) {
                                secFeverChk_lbl.setVisibility(View.GONE);
                            }
                            else if (!rdoCough1.isChecked() & !rdoDBrea1.isChecked() & rdoFever1.isChecked() & Temp < 99.5) {
                                secFeverChk_lbl.setVisibility(View.VISIBLE);
                            }

                            if (Temp >= 99.5 && Temp <= 100.3) {
                                secLFever.setVisibility(View.VISIBLE);
                                rdoLFever1.setChecked(true);
                                rdoLFever1.setEnabled(false);
                                rdoLFever2.setEnabled(false);
                                secCLFever.setVisibility(View.VISIBLE);
                                rdoCLFever1.setChecked(true);
                                rdoCLFever2.setEnabled(false);
                                secTLFever.setVisibility(View.VISIBLE);
                                rdoTLFever1.setChecked(true);
                                rdoTLFever2.setEnabled(false);

                                secMFever.setVisibility(View.VISIBLE);
                                rdoMFever2.setChecked(true);
                                rdoMFever1.setEnabled(false);
                                secCMFever.setVisibility(View.VISIBLE);
                                rdoCMFever2.setChecked(true);
                                rdoCMFever1.setEnabled(false);
                                secTMFever.setVisibility(View.VISIBLE);
                                rdoTMFever2.setChecked(true);
                                rdoTMFever1.setEnabled(false);

                                secHFever.setVisibility(View.VISIBLE);
                                rdoHFever2.setChecked(true);
                                rdoHFever1.setEnabled(false);
                                secCHFever.setVisibility(View.VISIBLE);
                                rdoCHFever2.setChecked(true);
                                rdoCHFever1.setEnabled(false);
                                secTHFever.setVisibility(View.VISIBLE);
                                rdoTHFever2.setChecked(true);
                                rdoTHFever1.setEnabled(false);
                            }
                            if (dy>=29 & dy<=59)
                            {
                                if (Temp >= 100.4 && Temp <= 100.9) {
                                    secLFever.setVisibility(View.VISIBLE);
                                    rdoLFever2.setChecked(true);
                                    rdoLFever1.setEnabled(false);
                                    secCLFever.setVisibility(View.VISIBLE);
                                    rdoCLFever2.setChecked(true);
                                    rdoCLFever1.setEnabled(false);
                                    rdoCLFever2.setEnabled(false);
                                    secTLFever.setVisibility(View.VISIBLE);
                                    rdoTLFever2.setChecked(true);
                                    rdoTLFever1.setEnabled(false);
                                    rdoTLFever2.setEnabled(false);

                                    secMFever.setVisibility(View.VISIBLE);
                                    rdoMFever1.setChecked(true);
                                    rdoMFever2.setEnabled(false);
                                    secCMFever.setVisibility(View.VISIBLE);
                                    rdoCMFever1.setChecked(true);
                                    rdoCMFever2.setEnabled(false);
                                    secTMFever.setVisibility(View.VISIBLE);
                                    rdoTMFever1.setChecked(true);
                                    rdoTMFever2.setEnabled(false);

                                    secHFever.setVisibility(View.VISIBLE);
                                    rdoHFever2.setChecked(true);
                                    rdoHFever1.setEnabled(false);
                                    secCHFever.setVisibility(View.VISIBLE);
                                    rdoCHFever2.setChecked(true);
                                    rdoCHFever1.setEnabled(false);
                                    secTHFever.setVisibility(View.VISIBLE);
                                    rdoTHFever2.setChecked(true);
                                    rdoTHFever1.setEnabled(false);
                                }
                                if (Temp >= 101) {
                                    secLFever.setVisibility(View.VISIBLE);
                                    rdoLFever2.setChecked(true);
                                    rdoLFever1.setEnabled(false);
                                    secCLFever.setVisibility(View.VISIBLE);
                                    rdoCLFever2.setChecked(true);
                                    rdoCLFever1.setEnabled(false);
                                    secTLFever.setVisibility(View.VISIBLE);
                                    rdoTLFever2.setChecked(true);
                                    rdoTLFever1.setEnabled(false);

                                    secMFever.setVisibility(View.VISIBLE);
                                    rdoMFever2.setChecked(true);
                                    rdoMFever1.setEnabled(false);
                                    rdoMFever2.setEnabled(false);
                                    secCMFever.setVisibility(View.VISIBLE);
                                    rdoCMFever2.setChecked(true);
                                    rdoCMFever1.setEnabled(false);
                                    rdoCMFever2.setEnabled(false);
                                    secTMFever.setVisibility(View.VISIBLE);
                                    rdoTMFever2.setChecked(true);
                                    rdoTMFever1.setEnabled(false);
                                    rdoTMFever2.setEnabled(false);

                                    secHFever.setVisibility(View.VISIBLE);
                                    rdoHFever1.setChecked(true);
                                    rdoHFever1.setEnabled(false);
                                    rdoHFever2.setEnabled(false);
                                    secCHFever.setVisibility(View.VISIBLE);
                                    rdoCHFever1.setChecked(true);
                                    rdoCHFever1.setEnabled(false);
                                    rdoCHFever2.setEnabled(false);
                                    secTHFever.setVisibility(View.VISIBLE);
                                    rdoTHFever1.setChecked(true);
                                    rdoTHFever1.setEnabled(false);
                                    rdoTHFever2.setEnabled(false);
                                }
                            }
                            if(dy>=60)
                            {
                                if (Temp >= 100.4 && Temp <= 101.9) {
                                    secLFever.setVisibility(View.VISIBLE);
                                    rdoLFever2.setChecked(true);
                                    rdoLFever1.setEnabled(false);
                                    rdoLFever2.setEnabled(false);
                                    secCLFever.setVisibility(View.VISIBLE);
                                    rdoCLFever2.setChecked(true);
                                    rdoCLFever1.setEnabled(false);
                                    rdoCLFever2.setEnabled(false);
                                    secTLFever.setVisibility(View.VISIBLE);
                                    rdoTLFever2.setChecked(true);
                                    rdoTLFever1.setEnabled(false);
                                    rdoTLFever2.setEnabled(false);

                                    secMFever.setVisibility(View.VISIBLE);
                                    rdoMFever1.setChecked(true);
                                    rdoMFever1.setEnabled(false);
                                    rdoMFever2.setEnabled(false);
                                    secCMFever.setVisibility(View.VISIBLE);
                                    rdoCMFever1.setChecked(true);
                                    rdoCMFever1.setEnabled(false);
                                    rdoCMFever2.setEnabled(false);
                                    secTMFever.setVisibility(View.VISIBLE);
                                    rdoTMFever1.setChecked(true);
                                    rdoTMFever1.setEnabled(false);
                                    rdoTMFever2.setEnabled(false);

                                    secHFever.setVisibility(View.VISIBLE);
                                    rdoHFever2.setChecked(true);
                                    rdoHFever1.setEnabled(false);
                                    rdoHFever2.setEnabled(false);
                                    secCHFever.setVisibility(View.VISIBLE);
                                    rdoCHFever2.setChecked(true);
                                    rdoCHFever1.setEnabled(false);
                                    rdoCHFever2.setEnabled(false);
                                    secTHFever.setVisibility(View.VISIBLE);
                                    rdoTHFever2.setChecked(true);
                                    rdoTHFever1.setEnabled(false);
                                    rdoTHFever2.setEnabled(false);
                                }
                                if (Temp >= 102) {
                                    secLFever.setVisibility(View.VISIBLE);
                                    rdoLFever2.setChecked(true);
                                    rdoLFever1.setEnabled(false);
                                    rdoLFever2.setEnabled(false);
                                    secCLFever.setVisibility(View.VISIBLE);
                                    rdoCLFever2.setChecked(true);
                                    rdoCLFever1.setEnabled(false);
                                    rdoCLFever2.setEnabled(false);
                                    secTLFever.setVisibility(View.VISIBLE);
                                    rdoTLFever2.setChecked(true);
                                    rdoTLFever1.setEnabled(false);
                                    rdoTLFever2.setEnabled(false);

                                    secMFever.setVisibility(View.VISIBLE);
                                    rdoMFever2.setChecked(true);
                                    rdoMFever1.setEnabled(false);
                                    rdoMFever2.setEnabled(false);
                                    secCMFever.setVisibility(View.VISIBLE);
                                    rdoCMFever2.setChecked(true);
                                    rdoCMFever1.setEnabled(false);
                                    rdoCMFever2.setEnabled(false);
                                    secTMFever.setVisibility(View.VISIBLE);
                                    rdoTMFever2.setChecked(true);
                                    rdoTMFever1.setEnabled(false);
                                    rdoTMFever2.setEnabled(false);

                                    secHFever.setVisibility(View.VISIBLE);
                                    rdoHFever1.setChecked(true);
                                    rdoHFever1.setEnabled(false);
                                    rdoHFever2.setEnabled(false);
                                    secCHFever.setVisibility(View.VISIBLE);
                                    rdoCHFever1.setChecked(true);
                                    rdoCHFever1.setEnabled(false);
                                    rdoCHFever2.setEnabled(false);
                                    secTHFever.setVisibility(View.VISIBLE);
                                    rdoTHFever1.setChecked(true);
                                    rdoTHFever1.setEnabled(false);
                                    rdoTHFever2.setEnabled(false);
                                }
                            }
                        }
                    }

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {
                }
            });


            secFeverChk_lbl = (LinearLayout) findViewById(R.id.secFeverChk_lbl);
            chkFever=(CheckBox)findViewById(R.id.chkFever);
            VlblfeverChk_lbl = (TextView) findViewById(R.id.VlblfeverChk_lbl);




            secCough = (LinearLayout) findViewById(R.id.secCough);
            VlblCough = (TextView) findViewById(R.id.VlblCough);
            rdogrpCough = (RadioGroup) findViewById(R.id.rdogrpCough);

            rdoCough1 = (RadioButton) findViewById(R.id.rdoCough1);
            rdoCough2 = (RadioButton) findViewById(R.id.rdoCough2);
            rdogrpCough.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup,
                                             int radioButtonID) {
                    RadioButton rb = (RadioButton) findViewById(rdogrpCough.getCheckedRadioButtonId());
                    if (rb == null)
                        return;
                    String rbData = Global.Left(rb.getText().toString(), 1);
                    if (rbData.equalsIgnoreCase("1") & VisitStatus.equals("1")) {
                        //secFeverChk_lbl.setVisibility(View.GONE);
                        secCoughDt.setVisibility(View.VISIBLE);
                        secFeverChk_lbl.setVisibility(View.GONE);
                    } else {
                        //secFeverChk_lbl.setVisibility(View.VISIBLE);
                        dtpCoughDt.setText("");
                        secCoughDt.setVisibility(View.GONE);
                    }

                    //27 04 2016
                    if (rdoCough2.isChecked() & rdoDBrea2.isChecked() & rdoFever2.isChecked())
                    {
                        Connection.MessageBox(AssPneu.this,"কাশি, শ্বাসকষ্ট এবং জ্বর, সকল সমস্যা না হতে পারবে না।");
                        return;
                    }
                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                    return;
                }
            });
            secCoughDt = (LinearLayout) findViewById(R.id.secCoughDt);
            VlblCoughDt = (TextView) findViewById(R.id.VlblCoughDt);
            dtpCoughDt = (EditText) findViewById(R.id.dtpCoughDt);
            secDBrea = (LinearLayout) findViewById(R.id.secDBrea);
            VlblDBrea = (TextView) findViewById(R.id.VlblDBrea);
            rdogrpDBrea = (RadioGroup) findViewById(R.id.rdogrpDBrea);

            rdoDBrea1 = (RadioButton) findViewById(R.id.rdoDBrea1);
            rdoDBrea2 = (RadioButton) findViewById(R.id.rdoDBrea2);
            rdogrpDBrea.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup,int radioButtonID) {
                    RadioButton rb = (RadioButton) findViewById(rdogrpDBrea.getCheckedRadioButtonId());
                    if (rb == null)
                        return;
                    String rbData = Global.Left(
                            rb.getText().toString(), 1);
                    if (rbData.equalsIgnoreCase("1") & VisitStatus.equals("1")) {
                        secDBreaDt.setVisibility(View.VISIBLE);
                        secFeverChk_lbl.setVisibility(View.GONE);
                    } else {
                        dtpDBreaDt.setText("");
                        secDBreaDt.setVisibility(View.GONE);
                    }

                    //27 04 2016
                    if (rdoCough2.isChecked() & rdoDBrea2.isChecked() & rdoFever2.isChecked())
                    {
                        Connection.MessageBox(AssPneu.this, "কাশি, শ্বাসকষ্ট এবং জ্বর, সকল সমস্যা না হতে পারবে না।");
                        return;
                    }

                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                    return;
                }
            });
            secDBreaDt = (LinearLayout) findViewById(R.id.secDBreaDt);
            VlblDBreaDt = (TextView) findViewById(R.id.VlblDBreaDt);
            dtpDBreaDt = (EditText) findViewById(R.id.dtpDBreaDt);
            secFever = (LinearLayout) findViewById(R.id.secFever);
            VlblFever = (TextView) findViewById(R.id.VlblFever);
            rdogrpFever = (RadioGroup) findViewById(R.id.rdogrpFever);

            rdoFever1 = (RadioButton) findViewById(R.id.rdoFever1);
            rdoFever2 = (RadioButton) findViewById(R.id.rdoFever2);
            rdogrpFever.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup,
                                             int radioButtonID) {
                    //27 04 2016
                    if (rdoCough2.isChecked() & rdoDBrea2.isChecked() & rdoFever2.isChecked())
                    {
                        Connection.MessageBox(AssPneu.this, "কাশি, শ্বাসকষ্ট এবং জ্বর, সকল সমস্যা না হতে পারবে না।");
                        return;
                    }
                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                    return;
                }
            });
/*
            rdogrpFever.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup,
                                             int radioButtonID) {
                    RadioButton rb = (RadioButton) findViewById(rdogrpFever.getCheckedRadioButtonId());
                    if (rb == null)
                        return;
                    String rbData = Global.Left(
                            rb.getText().toString(), 1);
                    if (rbData.equalsIgnoreCase("1")) {
                        secFeverDt.setVisibility(View.VISIBLE);

                        secFever_lbl.setVisibility(View.VISIBLE);
                        secLFever.setVisibility(View.VISIBLE);
                        secCLFever.setVisibility(View.VISIBLE);
                        secTLFever.setVisibility(View.VISIBLE);
                        secMFever.setVisibility(View.VISIBLE);
                        secCMFever.setVisibility(View.VISIBLE);
                        secTMFever.setVisibility(View.VISIBLE);
                        secHFever.setVisibility(View.VISIBLE);
                        secCHFever.setVisibility(View.VISIBLE);
                        secTHFever.setVisibility(View.VISIBLE);
                        secFSick_lbl.setVisibility(View.VISIBLE);
                        secNeck.setVisibility(View.VISIBLE);
                        secFonta.setVisibility(View.VISIBLE);
                        secConv2.setVisibility(View.VISIBLE);
                        secLeth2.setVisibility(View.VISIBLE);
                        secUcon2.setVisibility(View.VISIBLE);
                        secDrink2.setVisibility(View.VISIBLE);
                        secVomit2.setVisibility(View.VISIBLE);
                        secCMenin.setVisibility(View.VISIBLE);
                        secTMenin.setVisibility(View.VISIBLE);
                    } else {
                        //dtpFeverDt.setText("");
                        //secFeverDt.setVisibility(View.GONE);

                        secLFever.setVisibility(View.GONE);
                        rdogrpLFever.clearCheck();
                        secCLFever.setVisibility(View.GONE);
                        rdogrpCLFever.clearCheck();
                        secTLFever.setVisibility(View.GONE);
                        rdogrpTLFever.clearCheck();
                        secMFever.setVisibility(View.GONE);
                        rdogrpMFever.clearCheck();
                        secCMFever.setVisibility(View.GONE);
                        rdogrpCMFever.clearCheck();
                        secTMFever.setVisibility(View.GONE);
                        rdogrpTMFever.clearCheck();
                        secHFever.setVisibility(View.GONE);
                        rdogrpHFever.clearCheck();
                        secCHFever.setVisibility(View.GONE);
                        rdogrpCHFever.clearCheck();
                        secTHFever.setVisibility(View.GONE);
                        rdogrpTHFever.clearCheck();
                        secFSick_lbl.setVisibility(View.GONE);
                        secNeck.setVisibility(View.GONE);
                        rdogrpNeck.clearCheck();
                        secFonta.setVisibility(View.GONE);
                        rdogrpFonta.clearCheck();
                        secConv2.setVisibility(View.GONE);
                        rdogrpConv2.clearCheck();
                        secLeth2.setVisibility(View.GONE);
                        rdogrpLeth2.clearCheck();
                        secUcon2.setVisibility(View.GONE);
                        rdogrpUcon2.clearCheck();
                        secDrink2.setVisibility(View.GONE);
                        rdogrpDrink2.clearCheck();
                        secVomit2.setVisibility(View.GONE);
                        rdogrpVomit2.clearCheck();
                        secCMenin.setVisibility(View.GONE);
                        rdogrpCMenin.clearCheck();
                        secTMenin.setVisibility(View.GONE);
                        rdogrpTMenin.clearCheck();
                    }
                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                    return;
                }
            });
*/
            secFeverDt = (LinearLayout) findViewById(R.id.secFeverDt);
            VlblFeverDt = (TextView) findViewById(R.id.VlblFeverDt);
            dtpFeverDt = (EditText) findViewById(R.id.dtpFeverDt);
            secOthCom1 = (LinearLayout) findViewById(R.id.secOthCom1);
            VlblOthCom1 = (TextView) findViewById(R.id.VlblOthCom1);
            spnOthCom1 = (Spinner) findViewById(R.id.spnOthCom1);
            List<String> listOthCom1 = new ArrayList<String>();

            listOthCom1.add("");
            listOthCom1.add("");
            ArrayAdapter<String> adptrOthCom1 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, listOthCom1);
            spnOthCom1.setAdapter(adptrOthCom1);

            secOthCom2 = (LinearLayout) findViewById(R.id.secOthCom2);
            VlblOthCom2 = (TextView) findViewById(R.id.VlblOthCom2);
            spnOthCom2 = (Spinner) findViewById(R.id.spnOthCom2);
            List<String> listOthCom2 = new ArrayList<String>();

            listOthCom2.add("");
            listOthCom2.add("");
            ArrayAdapter<String> adptrOthCom2 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, listOthCom2);
            spnOthCom2.setAdapter(adptrOthCom2);

            secOthCom3 = (LinearLayout) findViewById(R.id.secOthCom3);
            VlblOthCom3 = (TextView) findViewById(R.id.VlblOthCom3);
            spnOthCom3 = (Spinner) findViewById(R.id.spnOthCom3);
            List<String> listOthCom3 = new ArrayList<String>();

            //listOthCom3.add("");
            listOthCom3.add("");
            ArrayAdapter<String> adptrOthCom3 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, listOthCom3);
            spnOthCom3.setAdapter(adptrOthCom3);

            secAsses = (LinearLayout) findViewById(R.id.secAsses);
            VlblAsses = (TextView) findViewById(R.id.VlblAsses);
            rdogrpAsses = (RadioGroup) findViewById(R.id.rdogrpAsses);

            rdoAsses1 = (RadioButton) findViewById(R.id.rdoAsses1);
            rdoAsses2 = (RadioButton) findViewById(R.id.rdoAsses2);
            rdogrpAsses.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup,
                                             int radioButtonID) {
                    RadioButton rb = (RadioButton) findViewById(rdogrpAsses.getCheckedRadioButtonId());
                    if (rb == null)
                        return;
                    String rbData = Global.Left(
                            rb.getText().toString(), 1);
                    if (rbData.equalsIgnoreCase("2")) {
                        secAskMo.setVisibility(View.GONE);
                        secRR1.setVisibility(View.GONE);
                        txtRR1.setText("");

                        secRR2.setVisibility(View.GONE);
                        txtRR2.setText("");

                        sectemp.setVisibility(View.GONE);
                        txttemp.setText("");
                        secFeverChk_lbl.setVisibility(View.GONE);
                        secCoughDt.setVisibility(View.GONE);
                        dtpCoughDt.setText("");
                        secDBreaDt.setVisibility(View.GONE);
                        dtpDBreaDt.setText("");
                        secFeverDt.setVisibility(View.GONE);
                        dtpFeverDt.setText("");
                        secCof_lbl.setVisibility(View.GONE);
                        secConv.setVisibility(View.GONE);
                        rdogrpConv.clearCheck();
                        secFBrea.setVisibility(View.GONE);
                        rdogrpFBrea.clearCheck();
                        secCInd.setVisibility(View.GONE);
                        rdogrpCInd.clearCheck();
                        secLeth.setVisibility(View.GONE);
                        rdogrpLeth.clearCheck();
                        secUCon.setVisibility(View.GONE);
                        rdogrpUCon.clearCheck();
                        secDrink.setVisibility(View.GONE);
                        rdogrpDrink.clearCheck();
                        secVomit.setVisibility(View.GONE);
                        rdogrpVomit.clearCheck();
                        secNone.setVisibility(View.GONE);
                        secCofCls_lbl.setVisibility(View.GONE);
                        rdogrpNone.clearCheck();
                        secCSPne.setVisibility(View.GONE);
                        rdogrpCSPne.clearCheck();
                        secCPPne.setVisibility(View.GONE);
                        rdogrpCPPne.clearCheck();
                        secCNPne.setVisibility(View.GONE);
                        rdogrpCNPne.clearCheck();
                        secCofMan_lbl.setVisibility(View.GONE);
                        secTSPne.setVisibility(View.GONE);
                        rdogrpTSPne.clearCheck();
                        secTPPne.setVisibility(View.GONE);
                        rdogrpTPPne.clearCheck();
                        secTNPne.setVisibility(View.GONE);
                        rdogrpTNPne.clearCheck();
                        secFever_lbl.setVisibility(View.GONE);
                        secLFever.setVisibility(View.GONE);
                        rdogrpLFever.clearCheck();
                        secCLFever.setVisibility(View.GONE);
                        rdogrpCLFever.clearCheck();
                        secTLFever.setVisibility(View.GONE);
                        rdogrpTLFever.clearCheck();
                        secMFever.setVisibility(View.GONE);
                        rdogrpMFever.clearCheck();
                        secCMFever.setVisibility(View.GONE);
                        rdogrpCMFever.clearCheck();
                        secTMFever.setVisibility(View.GONE);
                        rdogrpTMFever.clearCheck();
                        secHFever.setVisibility(View.GONE);
                        rdogrpHFever.clearCheck();
                        secCHFever.setVisibility(View.GONE);
                        rdogrpCHFever.clearCheck();
                        secTHFever.setVisibility(View.GONE);
                        rdogrpTHFever.clearCheck();
                        secFSick_lbl.setVisibility(View.GONE);
                        secNeck.setVisibility(View.GONE);
                        rdogrpNeck.clearCheck();
                        secFonta.setVisibility(View.GONE);
                        rdogrpFonta.clearCheck();
                        secConv2.setVisibility(View.GONE);
                        rdogrpConv2.clearCheck();
                        secLeth2.setVisibility(View.GONE);
                        rdogrpLeth2.clearCheck();
                        secUcon2.setVisibility(View.GONE);
                        rdogrpUcon2.clearCheck();
                        secDrink2.setVisibility(View.GONE);
                        rdogrpDrink2.clearCheck();
                        secVomit2.setVisibility(View.GONE);
                        rdogrpVomit2.clearCheck();
                        secCMenin.setVisibility(View.GONE);
                        rdogrpCMenin.clearCheck();
                        secTMenin.setVisibility(View.GONE);
                        rdogrpTMenin.clearCheck();
                        secReason.setVisibility(View.VISIBLE);
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

                    } else {

                        /*
                        secCoughDt.setVisibility(View.GONE);
                        dtpCoughDt.setText("");
                        secDBreaDt.setVisibility(View.GONE);
                        dtpDBreaDt.setText("");
                        secFeverDt.setVisibility(View.GONE);
                        dtpFeverDt.setText("");
                        */
                        secAskMo.setVisibility(View.VISIBLE);
                        secRR1.setVisibility(View.VISIBLE);
                        String CDOB2;
                        CDOB2=C.ReturnSingleValue("Select BDate  from Child WHERE   ChildId = '"+ ChildID +"'");

                        int dy1=Global.DateDifferenceDays(dtpVDate.getText().toString(),Global.DateConvertDMY(CDOB2));
                        if (dy1<59) {
                            secRR2.setVisibility(View.VISIBLE);
                        }
                        sectemp.setVisibility(View.VISIBLE);
                        //   txttemp.setText("");
                        if(rdoCough1.isChecked())
                        {
                            secCoughDt.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            secCoughDt.setVisibility(View.GONE);
                            dtpCoughDt.setText("");
                        }
                        if(rdoDBrea1.isChecked()) {
                            secDBreaDt.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            secDBreaDt.setVisibility(View.GONE);
                            dtpDBreaDt.setText("");
                        }
                      /*  if(rdoFever1.isChecked()) {
                            secFeverDt.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            secFeverDt.setVisibility(View.GONE);
                            dtpFeverDt.setText("");
                        }*/

                        secCof_lbl.setVisibility(View.VISIBLE);
                        secConv.setVisibility(View.VISIBLE);
                        secFBrea.setVisibility(View.VISIBLE);
                        secCInd.setVisibility(View.VISIBLE);
                        secLeth.setVisibility(View.VISIBLE);
                        secUCon.setVisibility(View.VISIBLE);
                        secDrink.setVisibility(View.VISIBLE);
                        secVomit.setVisibility(View.VISIBLE);
                        secNone.setVisibility(View.VISIBLE);
                        secCofCls_lbl.setVisibility(View.VISIBLE);
                        secCSPne.setVisibility(View.VISIBLE);
                        secCPPne.setVisibility(View.VISIBLE);
                        secCNPne.setVisibility(View.VISIBLE);
                        secCofMan_lbl.setVisibility(View.VISIBLE);
                        secTSPne.setVisibility(View.VISIBLE);
                        secTPPne.setVisibility(View.VISIBLE);
                        secTNPne.setVisibility(View.VISIBLE);

                       /* if(rdoFever1.isChecked()){
                            secFever_lbl.setVisibility(View.VISIBLE);
                            secLFever.setVisibility(View.VISIBLE);
                            secCLFever.setVisibility(View.VISIBLE);
                            secTLFever.setVisibility(View.VISIBLE);
                            secMFever.setVisibility(View.VISIBLE);
                            secCMFever.setVisibility(View.VISIBLE);
                            secTMFever.setVisibility(View.VISIBLE);
                            secHFever.setVisibility(View.VISIBLE);
                            secCHFever.setVisibility(View.VISIBLE);
                            secTHFever.setVisibility(View.VISIBLE);
                           secFSick_lbl.setVisibility(View.VISIBLE);
                            secNeck.setVisibility(View.VISIBLE);
                            secFonta.setVisibility(View.VISIBLE);
                            secConv2.setVisibility(View.VISIBLE);
                            secLeth2.setVisibility(View.VISIBLE);
                            secUcon2.setVisibility(View.VISIBLE);
                            secDrink2.setVisibility(View.VISIBLE);
                            secVomit2.setVisibility(View.VISIBLE);
                            secCMenin.setVisibility(View.VISIBLE);
                            secTMenin.setVisibility(View.VISIBLE);
                        }
                        if(rdoFever2.isChecked()){
                            secFever_lbl.setVisibility(View.GONE);
                            secLFever.setVisibility(View.GONE);
                            secCLFever.setVisibility(View.GONE);
                            secTLFever.setVisibility(View.GONE);
                            secMFever.setVisibility(View.GONE);
                            secCMFever.setVisibility(View.GONE);
                            secTMFever.setVisibility(View.GONE);
                            secHFever.setVisibility(View.GONE);
                            secCHFever.setVisibility(View.GONE);
                            secTHFever.setVisibility(View.GONE);
                            secFSick_lbl.setVisibility(View.GONE);
                            secNeck.setVisibility(View.GONE);
                            secFonta.setVisibility(View.GONE);
                            secConv2.setVisibility(View.GONE);
                            secLeth2.setVisibility(View.GONE);
                            secUcon2.setVisibility(View.GONE);
                            secDrink2.setVisibility(View.GONE);
                            secVomit2.setVisibility(View.GONE);
                            secCMenin.setVisibility(View.GONE);
                            secTMenin.setVisibility(View.GONE);
                        }*/
                        secRef.setVisibility(View.VISIBLE);
                        //secRSlip.setVisibility(View.VISIBLE);
                        //secPhone.setVisibility(View.VISIBLE);
                        //secComp.setVisibility(View.VISIBLE);
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
            secAskMo = (LinearLayout) findViewById(R.id.secAskMo);
            secRR1 = (LinearLayout) findViewById(R.id.secRR1);
            VlblRR1 = (TextView) findViewById(R.id.VlblRR1);
            txtRR1 = (EditText) findViewById(R.id.txtRR1);

            txtRR1.addTextChangedListener(new TextWatcher() {
                                              public void afterTextChanged(Editable s) {
                                              }

                                              public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                              }

                                              public void onTextChanged(CharSequence s, int start, int before, int count) {

                                                  String CDOB;
                                                  CDOB=C.ReturnSingleValue("Select BDate  from Child WHERE   ChildId = '"+ ChildID +"'");

                                                  int d=Global.DateDifferenceDays(dtpVDate.getText().toString(),Global.DateConvertDMY(CDOB));

                                                  String var = txtRR1.getText().toString();
                                                  Integer rr1 = Integer.parseInt(var.length() == 0 ? "0" : var);

                                                  if(txtRR1.length()>0)
                                                  {
                                                      if(rr1>105)
                                                      {
                                                          C.MessageBox(AssPneu.this, "শ্বাস ১০৫ এর বেশী হবে না,please check ");
                                                          //txttemp.setText(null);
                                                          //txttemp.requestFocus();
                                                          return;
                                                      }
                                                  }

                                                  if(txtRR1.length()>0)
                                                  {
                                                      if((d/30)<2& rr1>59)
                                                      {
                                                          secRR2.setVisibility(View.VISIBLE);

                                                      }
                                                      else
                                                      {
                                                          secRR2.setVisibility(View.GONE);
                                                          txtRR2.setText("");
                                                      }


                                                      return;
                                                  }
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

            secRR2 = (LinearLayout) findViewById(R.id.secRR2);
            VlblRR2 = (TextView) findViewById(R.id.VlblRR2);
            txtRR2 = (EditText) findViewById(R.id.txtRR2);
            secConv = (LinearLayout) findViewById(R.id.secConv);
            VlblConv = (TextView) findViewById(R.id.VlblConv);
            rdogrpConv = (RadioGroup) findViewById(R.id.rdogrpConv);

            rdoConv1 = (RadioButton) findViewById(R.id.rdoConv1);
            rdoConv2 = (RadioButton) findViewById(R.id.rdoConv2);
            secFBrea = (LinearLayout) findViewById(R.id.secFBrea);
            VlblFBrea = (TextView) findViewById(R.id.VlblFBrea);
            rdogrpFBrea = (RadioGroup) findViewById(R.id.rdogrpFBrea);

            rdoFBrea1 = (RadioButton) findViewById(R.id.rdoFBrea1);
            rdoFBrea2 = (RadioButton) findViewById(R.id.rdoFBrea2);
            secCInd = (LinearLayout) findViewById(R.id.secCInd);
            VlblCInd = (TextView) findViewById(R.id.VlblCInd);
            rdogrpCInd = (RadioGroup) findViewById(R.id.rdogrpCInd);

            rdoCInd1 = (RadioButton) findViewById(R.id.rdoCInd1);
            rdoCInd2 = (RadioButton) findViewById(R.id.rdoCInd2);
            secLeth = (LinearLayout) findViewById(R.id.secLeth);
            VlblLeth = (TextView) findViewById(R.id.VlblLeth);
            rdogrpLeth = (RadioGroup) findViewById(R.id.rdogrpLeth);

            rdoLeth1 = (RadioButton) findViewById(R.id.rdoLeth1);
            rdoLeth2 = (RadioButton) findViewById(R.id.rdoLeth2);
            secUCon = (LinearLayout) findViewById(R.id.secUCon);
            VlblUCon = (TextView) findViewById(R.id.VlblUCon);
            rdogrpUCon = (RadioGroup) findViewById(R.id.rdogrpUCon);

            rdoUCon1 = (RadioButton) findViewById(R.id.rdoUCon1);
            rdoUCon2 = (RadioButton) findViewById(R.id.rdoUCon2);
            secDrink = (LinearLayout) findViewById(R.id.secDrink);
            VlblDrink = (TextView) findViewById(R.id.VlblDrink);
            rdogrpDrink = (RadioGroup) findViewById(R.id.rdogrpDrink);

            rdoDrink1 = (RadioButton) findViewById(R.id.rdoDrink1);
            rdoDrink2 = (RadioButton) findViewById(R.id.rdoDrink2);
            secVomit = (LinearLayout) findViewById(R.id.secVomit);
            VlblVomit = (TextView) findViewById(R.id.VlblVomit);
            rdogrpVomit = (RadioGroup) findViewById(R.id.rdogrpVomit);

            rdoVomit1 = (RadioButton) findViewById(R.id.rdoVomit1);
            rdoVomit2 = (RadioButton) findViewById(R.id.rdoVomit2);
            secNone = (LinearLayout) findViewById(R.id.secNone);
            VlblNone = (TextView) findViewById(R.id.VlblNone);
            rdogrpNone = (RadioGroup) findViewById(R.id.rdogrpNone);

            rdoNone1 = (RadioButton) findViewById(R.id.rdoNone1);
            rdoNone2 = (RadioButton) findViewById(R.id.rdoNone2);
            secCSPne = (LinearLayout) findViewById(R.id.secCSPne);
            VlblCSPne = (TextView) findViewById(R.id.VlblCSPne);
            rdogrpCSPne = (RadioGroup) findViewById(R.id.rdogrpCSPne);

            rdoCSPne1 = (RadioButton) findViewById(R.id.rdoCSPne1);
/*            rdoCSPne1.setOnCheckedChangeListener(new OnCheckedChangeListener()
            {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    if (rdoCSPne1.isChecked() & rdoCPPne1.isChecked() )
                    {
                        C.MessageBox(AssPneu.this, "More than two select in CLS");
                        //txttemp.setText(null);
                        //txttemp.requestFocus();
                        return;
                    }
                    else if (rdoCSPne1.isChecked() & rdoCNPne1.isChecked())
                    {
                        C.MessageBox(AssPneu.this, "More than two select in CLS");
                        //txttemp.setText(null);
                        //txttemp.requestFocus();
                        return;
                    }

                }
            });*/


            rdoCSPne2 = (RadioButton) findViewById(R.id.rdoCSPne2);
            secCPPne = (LinearLayout) findViewById(R.id.secCPPne);
            VlblCPPne = (TextView) findViewById(R.id.VlblCPPne);
            rdogrpCPPne = (RadioGroup) findViewById(R.id.rdogrpCPPne);

            rdoCPPne1 = (RadioButton) findViewById(R.id.rdoCPPne1);

/*            rdoCPPne1.setOnCheckedChangeListener(new OnCheckedChangeListener()
            {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    if (rdoCPPne1.isChecked() & rdoCSPne1.isChecked() )
                    {
                        C.MessageBox(AssPneu.this, "More than two select in CLS");
                        //txttemp.setText(null);
                        //txttemp.requestFocus();
                        return;
                    }
                    else if (rdoCPPne1.isChecked() & rdoCNPne1.isChecked())
                    {
                        C.MessageBox(AssPneu.this, "More than two select in CLS");
                        //txttemp.setText(null);
                        //txttemp.requestFocus();
                        return;
                    }

                }
            });*/

            rdoCPPne2 = (RadioButton) findViewById(R.id.rdoCPPne2);
            secCNPne = (LinearLayout) findViewById(R.id.secCNPne);
            VlblCNPne = (TextView) findViewById(R.id.VlblCNPne);
            rdogrpCNPne = (RadioGroup) findViewById(R.id.rdogrpCNPne);

            rdoCNPne1 = (RadioButton) findViewById(R.id.rdoCNPne1);
/*            rdoCNPne1.setOnCheckedChangeListener(new OnCheckedChangeListener()
            {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    if (rdoCNPne1.isChecked() & rdoCPPne1.isChecked())
                    {
                        C.MessageBox(AssPneu.this, "More than two select in CLS");
                        //txttemp.setText(null);
                        //txttemp.requestFocus();
                        return;
                    }
                    else if (rdoCNPne1.isChecked() & rdoCSPne1.isChecked())
                    {
                        C.MessageBox(AssPneu.this, "More than two select in CLS");
                        //txttemp.setText(null);
                        //txttemp.requestFocus();
                        return;
                    }

                }
            });*/
            rdoCNPne2 = (RadioButton) findViewById(R.id.rdoCNPne2);
            secTSPne = (LinearLayout) findViewById(R.id.secTSPne);
            VlblTSPne = (TextView) findViewById(R.id.VlblTSPne);
            rdogrpTSPne = (RadioGroup) findViewById(R.id.rdogrpTSPne);

            rdoTSPne1 = (RadioButton) findViewById(R.id.rdoTSPne1);
            rdoTSPne2 = (RadioButton) findViewById(R.id.rdoTSPne2);
            secTPPne = (LinearLayout) findViewById(R.id.secTPPne);
            VlblTPPne = (TextView) findViewById(R.id.VlblTPPne);
            rdogrpTPPne = (RadioGroup) findViewById(R.id.rdogrpTPPne);

            rdoTPPne1 = (RadioButton) findViewById(R.id.rdoTPPne1);
            rdoTPPne2 = (RadioButton) findViewById(R.id.rdoTPPne2);
            secTNPne = (LinearLayout) findViewById(R.id.secTNPne);
            VlblTNPne = (TextView) findViewById(R.id.VlblTNPne);
            rdogrpTNPne = (RadioGroup) findViewById(R.id.rdogrpTNPne);

            rdoTNPne1 = (RadioButton) findViewById(R.id.rdoTNPne1);
            rdoTNPne2 = (RadioButton) findViewById(R.id.rdoTNPne2);
            secLFever = (LinearLayout) findViewById(R.id.secLFever);
            VlblLFever = (TextView) findViewById(R.id.VlblLFever);
            rdogrpLFever = (RadioGroup) findViewById(R.id.rdogrpLFever);

            rdoLFever1 = (RadioButton) findViewById(R.id.rdoLFever1);
            rdoLFever2 = (RadioButton) findViewById(R.id.rdoLFever2);
            secCLFever = (LinearLayout) findViewById(R.id.secCLFever);
            VlblCLFever = (TextView) findViewById(R.id.VlblCLFever);
            rdogrpCLFever = (RadioGroup) findViewById(R.id.rdogrpCLFever);

            rdoCLFever1 = (RadioButton) findViewById(R.id.rdoCLFever1);
            rdoCLFever2 = (RadioButton) findViewById(R.id.rdoCLFever2);
            secTLFever = (LinearLayout) findViewById(R.id.secTLFever);
            VlblTLFever = (TextView) findViewById(R.id.VlblTLFever);
            rdogrpTLFever = (RadioGroup) findViewById(R.id.rdogrpTLFever);

            rdoTLFever1 = (RadioButton) findViewById(R.id.rdoTLFever1);
            rdoTLFever2 = (RadioButton) findViewById(R.id.rdoTLFever2);
            secMFever = (LinearLayout) findViewById(R.id.secMFever);
            VlblMFever = (TextView) findViewById(R.id.VlblMFever);
            rdogrpMFever = (RadioGroup) findViewById(R.id.rdogrpMFever);

            rdoMFever1 = (RadioButton) findViewById(R.id.rdoMFever1);
            rdoMFever2 = (RadioButton) findViewById(R.id.rdoMFever2);
            secCMFever = (LinearLayout) findViewById(R.id.secCMFever);
            VlblCMFever = (TextView) findViewById(R.id.VlblCMFever);
            rdogrpCMFever = (RadioGroup) findViewById(R.id.rdogrpCMFever);

            rdoCMFever1 = (RadioButton) findViewById(R.id.rdoCMFever1);
            rdoCMFever2 = (RadioButton) findViewById(R.id.rdoCMFever2);
            secTMFever = (LinearLayout) findViewById(R.id.secTMFever);
            VlblTMFever = (TextView) findViewById(R.id.VlblTMFever);
            rdogrpTMFever = (RadioGroup) findViewById(R.id.rdogrpTMFever);

            rdoTMFever1 = (RadioButton) findViewById(R.id.rdoTMFever1);
            rdoTMFever2 = (RadioButton) findViewById(R.id.rdoTMFever2);
            secHFever = (LinearLayout) findViewById(R.id.secHFever);
            VlblHFever = (TextView) findViewById(R.id.VlblHFever);
            rdogrpHFever = (RadioGroup) findViewById(R.id.rdogrpHFever);

            rdoHFever1 = (RadioButton) findViewById(R.id.rdoHFever1);
            rdoHFever2 = (RadioButton) findViewById(R.id.rdoHFever2);
            secCHFever = (LinearLayout) findViewById(R.id.secCHFever);
            VlblCHFever = (TextView) findViewById(R.id.VlblCHFever);
            rdogrpCHFever = (RadioGroup) findViewById(R.id.rdogrpCHFever);

            rdoCHFever1 = (RadioButton) findViewById(R.id.rdoCHFever1);
            rdoCHFever2 = (RadioButton) findViewById(R.id.rdoCHFever2);
            secTHFever = (LinearLayout) findViewById(R.id.secTHFever);
            VlblTHFever = (TextView) findViewById(R.id.VlblTHFever);
            rdogrpTHFever = (RadioGroup) findViewById(R.id.rdogrpTHFever);

            rdoTHFever1 = (RadioButton) findViewById(R.id.rdoTHFever1);
            rdoTHFever2 = (RadioButton) findViewById(R.id.rdoTHFever2);
            secNeck = (LinearLayout) findViewById(R.id.secNeck);
            VlblNeck = (TextView) findViewById(R.id.VlblNeck);
            rdogrpNeck = (RadioGroup) findViewById(R.id.rdogrpNeck);

            rdoNeck1 = (RadioButton) findViewById(R.id.rdoNeck1);
            rdoNeck2 = (RadioButton) findViewById(R.id.rdoNeck2);
            secFonta = (LinearLayout) findViewById(R.id.secFonta);
            VlblFonta = (TextView) findViewById(R.id.VlblFonta);
            rdogrpFonta = (RadioGroup) findViewById(R.id.rdogrpFonta);

            rdoFonta1 = (RadioButton) findViewById(R.id.rdoFonta1);
            rdoFonta2 = (RadioButton) findViewById(R.id.rdoFonta2);
            secConv2 = (LinearLayout) findViewById(R.id.secConv2);
            VlblConv2 = (TextView) findViewById(R.id.VlblConv2);
            rdogrpConv2 = (RadioGroup) findViewById(R.id.rdogrpConv2);

            rdoConv21 = (RadioButton) findViewById(R.id.rdoConv21);
            rdoConv22 = (RadioButton) findViewById(R.id.rdoConv22);
            secLeth2 = (LinearLayout) findViewById(R.id.secLeth2);
            VlblLeth2 = (TextView) findViewById(R.id.VlblLeth2);
            rdogrpLeth2 = (RadioGroup) findViewById(R.id.rdogrpLeth2);

            rdoLeth21 = (RadioButton) findViewById(R.id.rdoLeth21);
            rdoLeth22 = (RadioButton) findViewById(R.id.rdoLeth22);
            secUcon2 = (LinearLayout) findViewById(R.id.secUcon2);
            VlblUcon2 = (TextView) findViewById(R.id.VlblUcon2);
            rdogrpUcon2 = (RadioGroup) findViewById(R.id.rdogrpUcon2);

            rdoUcon21 = (RadioButton) findViewById(R.id.rdoUcon21);
            rdoUcon22 = (RadioButton) findViewById(R.id.rdoUcon22);
            secDrink2 = (LinearLayout) findViewById(R.id.secDrink2);
            VlblDrink2 = (TextView) findViewById(R.id.VlblDrink2);
            rdogrpDrink2 = (RadioGroup) findViewById(R.id.rdogrpDrink2);

            rdoDrink21 = (RadioButton) findViewById(R.id.rdoDrink21);
            rdoDrink22 = (RadioButton) findViewById(R.id.rdoDrink22);
            secVomit2 = (LinearLayout) findViewById(R.id.secVomit2);
            VlblVomit2 = (TextView) findViewById(R.id.VlblVomit2);
            rdogrpVomit2 = (RadioGroup) findViewById(R.id.rdogrpVomit2);

            rdoVomit21 = (RadioButton) findViewById(R.id.rdoVomit21);
            rdoVomit22 = (RadioButton) findViewById(R.id.rdoVomit22);
            secCMenin = (LinearLayout) findViewById(R.id.secCMenin);
            VlblCMenin = (TextView) findViewById(R.id.VlblCMenin);
            rdogrpCMenin = (RadioGroup) findViewById(R.id.rdogrpCMenin);

            rdoCMenin1 = (RadioButton) findViewById(R.id.rdoCMenin1);
            rdoCMenin2 = (RadioButton) findViewById(R.id.rdoCMenin2);
            secTMenin = (LinearLayout) findViewById(R.id.secTMenin);
            VlblTMenin = (TextView) findViewById(R.id.VlblTMenin);
            rdogrpTMenin = (RadioGroup) findViewById(R.id.rdogrpTMenin);

            rdoTMenin1 = (RadioButton) findViewById(R.id.rdoTMenin1);
            rdoTMenin2 = (RadioButton) findViewById(R.id.rdoTMenin2);
            secRef = (LinearLayout) findViewById(R.id.secRef);
            VlblRef = (TextView) findViewById(R.id.VlblRef);
            rdogrpRef = (RadioGroup) findViewById(R.id.rdogrpRef);

            rdoRef1 = (RadioButton) findViewById(R.id.rdoRef1);
            rdoRef2 = (RadioButton) findViewById(R.id.rdoRef2);

/*            VisitSt=C.ReturnSingleValue("Select VStat  from Visits WHERE  ChildId='" + ChildID +  "' and Week=' " + txtWeek.getSelectedItem().toString() + "'");
            String St=VisitSt;
            if ((St==2) {
            }*/

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
                        if(rdoAsses1.isChecked()) {
                            secComp.setVisibility(View.GONE);
                            rdogrpComp.clearCheck();
                            secReason.setVisibility(View.GONE);
                            rdogrpReason.clearCheck();
                            secTPlace.setVisibility(View.GONE);
                            spnTPlace.setSelection(0);
                            secTPlaceC.setVisibility(View.GONE);
                            txtTPlaceC.setText("");
                            secTAbsDur.setVisibility(View.GONE);
                            txtTAbsDur.setText("");
                            secHos.setVisibility(View.GONE);
                            rdogrpHos.clearCheck();
                        }
                    } else {
                        secRSlip.setVisibility(View.VISIBLE);
                        secPhone.setVisibility(View.VISIBLE);
                        if(rdoAsses2.isChecked() && (rdoRef1.isChecked() || rdoRef2.isChecked())) {
                            secTPlace.setVisibility(View.VISIBLE);
                            secTPlaceC.setVisibility(View.VISIBLE);

                            secTAbsDur.setVisibility(View.VISIBLE);
                            secHos.setVisibility(View.VISIBLE);
                        }
                    }
                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                    return;
                }
            });
            secRSlip = (LinearLayout) findViewById(R.id.secRSlip);
            VlblRSlip = (TextView) findViewById(R.id.VlblRSlip);
            txtRSlip = (EditText) findViewById(R.id.txtRSlip);
            secPhone = (LinearLayout) findViewById(R.id.secPhone);
            VlblPhone = (TextView) findViewById(R.id.VlblPhone);
            txtPhone = (EditText) findViewById(R.id.txtPhone);
            secComp = (LinearLayout) findViewById(R.id.secComp);
            VlblComp = (TextView) findViewById(R.id.VlblComp);
            rdogrpComp = (RadioGroup) findViewById(R.id.rdogrpComp);

            rdoComp1 = (RadioButton) findViewById(R.id.rdoComp1);
            rdoComp2 = (RadioButton) findViewById(R.id.rdoComp2);
            secReason = (LinearLayout) findViewById(R.id.secReason);
            VlblReason = (TextView) findViewById(R.id.VlblReason);
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
            secTPlace = (LinearLayout) findViewById(R.id.secTPlace);
            VlblTPlace = (TextView) findViewById(R.id.VlblTPlace);
            spnTPlace = (Spinner) findViewById(R.id.spnTPlace);
            List<String> listTPlace = new ArrayList<String>();

            listTPlace.add("");
            listTPlace.add("1-কুমুদিনি হাসপাতাল");
            listTPlace.add("2-অন্যান্য হাসপাতাল/ ক্লিনিক");
            listTPlace.add("3-পাশকরা ডাক্তার");
            listTPlace.add("4-গ্রাম ডাক্তার");
            listTPlace.add("5-অন্যান্য");
            ArrayAdapter<String> adptrTPlace = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, listTPlace);
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
            secTPlaceC = (LinearLayout) findViewById(R.id.secTPlaceC);
            VlblTPlaceC = (TextView) findViewById(R.id.VlblTPlaceC);
            txtTPlaceC = (EditText) findViewById(R.id.txtTPlaceC);

            //secTAbsIn = (LinearLayout) findViewById(R.id.secTAbsIn);
            secTAbsDur = (LinearLayout) findViewById(R.id.secTAbsDur);
            VlblTAbsDur = (TextView) findViewById(R.id.VlblTAbsDur);
            txtTAbsDur = (EditText) findViewById(R.id.txtTAbsDur);
            rdogrpTAbsIn = (RadioGroup) findViewById(R.id.rdogrpTAbsIn);
            rdoTAbsIn1 = (RadioButton) findViewById(R.id.rdoTAbsIn1);
            rdoTAbsIn2 = (RadioButton) findViewById(R.id.rdoTAbsIn2);


            secHos = (LinearLayout) findViewById(R.id.secHos);
            VlblHos = (TextView) findViewById(R.id.VlblHos);
            rdogrpHos = (RadioGroup) findViewById(R.id.rdogrpHos);

            rdoHos1 = (RadioButton) findViewById(R.id.rdoHos1);
            rdoHos2 = (RadioButton) findViewById(R.id.rdoHos2);
            rdoHos3 = (RadioButton) findViewById(R.id.rdoHos3);
/*			secEnDt = (LinearLayout) findViewById(R.id.secEnDt);
			VlblEnDt = (TextView) findViewById(R.id.VlblEnDt);
			dtpEnDt = (EditText) findViewById(R.id.dtpEnDt);
			secUserId = (LinearLayout) findViewById(R.id.secUserId);
			VlblUserId = (TextView) findViewById(R.id.VlblUserId);
			txtUserId = (EditText) findViewById(R.id.txtUserId);
			secUpload = (LinearLayout) findViewById(R.id.secUpload);
			VlblUpload = (TextView) findViewById(R.id.VlblUpload);
			txtUpload = (EditText) findViewById(R.id.txtUpload);
			secUploadDT = (LinearLayout) findViewById(R.id.secUploadDT);
			VlblUploadDT = (TextView) findViewById(R.id.VlblUploadDT);
			dtpUploadDT = (EditText) findViewById(R.id.dtpUploadDT);
*/
            btnVDate = (ImageButton) findViewById(R.id.btnVDate);
            btnVDate.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VariableID = "btnVDate";
                    showDialog(DATE_DIALOG);
                }
            });

            btnCoughDt = (ImageButton) findViewById(R.id.btnCoughDt);
            btnCoughDt.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VariableID = "btnCoughDt";
                    showDialog(DATE_DIALOG);
                }
            });

            btnDBreaDt = (ImageButton) findViewById(R.id.btnDBreaDt);
            btnDBreaDt.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VariableID = "btnDBreaDt";
                    showDialog(DATE_DIALOG);
                }
            });

            btnFeverDt = (ImageButton) findViewById(R.id.btnFeverDt);
            btnFeverDt.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VariableID = "btnFeverDt";
                    showDialog(DATE_DIALOG);
                }
            });

/*			btnEnDt = (ImageButton) findViewById(R.id.btnEnDt);
			btnEnDt.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					VariableID = "btnEnDt";
					showDialog(DATE_DIALOG);
				}
			});

			btnUploadDT = (ImageButton) findViewById(R.id.btnUploadDT);
			btnUploadDT.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					VariableID = "btnUploadDT";
					showDialog(DATE_DIALOG);
				}
			});*/

            // form load
            secAskMo.setVisibility(View.GONE);
            secRR1.setVisibility(View.GONE);
            txtRR1.setText("");
            secRR2.setVisibility(View.GONE);
            txtRR2.setText("");
            sectemp.setVisibility(View.GONE);
            txttemp.setText("");
            secFeverChk_lbl.setVisibility(View.GONE);
            secCoughDt.setVisibility(View.GONE);
            dtpCoughDt.setText("");
            secDBreaDt.setVisibility(View.GONE);
            dtpDBreaDt.setText("");
            secFeverDt.setVisibility(View.GONE);
            dtpFeverDt.setText("");
            secCof_lbl.setVisibility(View.GONE);
            secConv.setVisibility(View.GONE);
            rdogrpConv.clearCheck();
            secFBrea.setVisibility(View.GONE);
            rdogrpFBrea.clearCheck();
            secCInd.setVisibility(View.GONE);
            rdogrpCInd.clearCheck();
            secLeth.setVisibility(View.GONE);
            rdogrpLeth.clearCheck();
            secUCon.setVisibility(View.GONE);
            rdogrpUCon.clearCheck();
            secDrink.setVisibility(View.GONE);
            rdogrpDrink.clearCheck();
            secVomit.setVisibility(View.GONE);
            rdogrpVomit.clearCheck();
            secNone.setVisibility(View.GONE);
            secCofCls_lbl.setVisibility(View.GONE);
            rdogrpNone.clearCheck();
            secCSPne.setVisibility(View.GONE);
            rdogrpCSPne.clearCheck();
            secCPPne.setVisibility(View.GONE);
            rdogrpCPPne.clearCheck();
            secCNPne.setVisibility(View.GONE);
            rdogrpCNPne.clearCheck();
            secCofMan_lbl.setVisibility(View.GONE);
            secTSPne.setVisibility(View.GONE);
            rdogrpTSPne.clearCheck();
            secTPPne.setVisibility(View.GONE);
            rdogrpTPPne.clearCheck();
            secTNPne.setVisibility(View.GONE);
            rdogrpTNPne.clearCheck();
            secFever_lbl.setVisibility(View.GONE);
            secLFever.setVisibility(View.GONE);
            rdogrpLFever.clearCheck();
            secCLFever.setVisibility(View.GONE);
            rdogrpCLFever.clearCheck();
            secTLFever.setVisibility(View.GONE);
            rdogrpTLFever.clearCheck();
            secMFever.setVisibility(View.GONE);
            rdogrpMFever.clearCheck();
            secCMFever.setVisibility(View.GONE);
            rdogrpCMFever.clearCheck();
            secTMFever.setVisibility(View.GONE);
            rdogrpTMFever.clearCheck();
            secHFever.setVisibility(View.GONE);
            rdogrpHFever.clearCheck();
            secCHFever.setVisibility(View.GONE);
            rdogrpCHFever.clearCheck();
            secTHFever.setVisibility(View.GONE);
            rdogrpTHFever.clearCheck();
            secFSick_lbl.setVisibility(View.GONE);
            secNeck.setVisibility(View.GONE);
            rdogrpNeck.clearCheck();
            secFonta.setVisibility(View.GONE);
            rdogrpFonta.clearCheck();
            secConv2.setVisibility(View.GONE);
            rdogrpConv2.clearCheck();
            secLeth2.setVisibility(View.GONE);
            rdogrpLeth2.clearCheck();
            secUcon2.setVisibility(View.GONE);
            rdogrpUcon2.clearCheck();
            secDrink2.setVisibility(View.GONE);
            rdogrpDrink2.clearCheck();
            secVomit2.setVisibility(View.GONE);
            rdogrpVomit2.clearCheck();
            secCMenin.setVisibility(View.GONE);
            rdogrpCMenin.clearCheck();
            secTMenin.setVisibility(View.GONE);
            rdogrpTMenin.clearCheck();
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



            OthCom1();
            OthCom2();
            OthCom3();
            String tm;
            txtVisit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    //ClearForm();
                    DataSearch(ChildID, WeekNo, VisitType, txtVisit.getSelectedItem().toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }
            });

            if(VisitStatus.equals("1") | VisitStatus.equals(""))
            {
                rdoAsses1.setEnabled(true);
                rdoAsses2.setEnabled(true);

                rdoReason1.setEnabled(true);
                rdoReason2.setEnabled(true);
                rdoReason3.setEnabled(true);
            }
            else if(VisitStatus.equals("2"))
            {
                rdoAsses1.setEnabled(false);
                rdoAsses2.setEnabled(false);
                rdoAsses2.setChecked(true);

                rdoReason3.setChecked(true);
                //rdoReason1.setEnabled(false);
                //rdoReason2.setEnabled(false);
                //rdoReason3.setEnabled(false);
            }
            else if(VisitStatus.equals("3"))
            {
                rdoAsses1.setEnabled(false);
                rdoAsses2.setEnabled(false);
                rdoAsses2.setChecked(true);

                rdoReason2.setChecked(true);
                //rdoReason1.setEnabled(false);
                //rdoReason2.setEnabled(false);
                //rdoReason3.setEnabled(false);
            }


            DataSearch(ChildID,txtWeek.getSelectedItem().toString(),VisitType,txtVisit.getSelectedItem().toString());
            DataSearchPhone(ChildID);
            Button cmdSave = (Button) findViewById(R.id.cmdSave);
            cmdSave.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    DataSave();
                }
            });
        } catch (Exception e) {
            Connection.MessageBox(AssPneu.this, e.getMessage());
            return;
        }
    }

    String FName = "AssPneu";

    private void OthCom1() {
        Spinner OthCom1 = (Spinner) findViewById(R.id.spnOthCom1);
        OthCom1.setAdapter(C
                .getArrayAdapter("Select '' VarCodeDes from CodeList union  Select varCode|| '-' ||varDes  From CodeList where FName='"
                        + FName + "' and VarName='OthCom1'"));
    }

    private void OthCom2() {
        Spinner OthCom2 = (Spinner) findViewById(R.id.spnOthCom2);
        OthCom2.setAdapter(C
                .getArrayAdapter("Select '' VarCodeDes from CodeList union  Select varCode|| '-' ||varDes  From CodeList where FName='"
                        + FName + "' and VarName='OthCom2'"));
    }

    private void OthCom3() {
        Spinner OthCom3 = (Spinner) findViewById(R.id.spnOthCom3);
        OthCom3.setAdapter(C
                .getArrayAdapter("Select '' VarCodeDes from CodeList union  Select varCode|| '-' ||varDes  From CodeList where FName='"
                        + FName + "' and VarName='OthCom3'"));
    }

    private void DataSave() {
        try {

            String DV = "";

            if (txtChildId.getText().toString().length() == 0) {
                Connection.MessageBox(AssPneu.this, "Required field:Child ID.");
                txtChildId.requestFocus();
                return;
            }

            DV = Global.DateValidate(dtpVDate.getText().toString());
            if (DV.length() != 0 & secVDate.isShown()) {
                Connection.MessageBox(AssPneu.this, DV);
                dtpVDate.requestFocus();
                return;
            }

            if (!chkTemp.isChecked())
            {
                if (txttemp.getText().toString().length() == 0 & sectemp.isShown()) {
                    Connection.MessageBox(AssPneu.this, "তাপমাত্রা - খালি থাকতে পারবেনা");
                    txttemp.requestFocus();
                    return;
                }
                else
                {
                    if (sectemp.isShown() & (Double.valueOf(txttemp.getText().toString().length() == 0 ? "0.0" : txttemp.getText().toString()) < 92 || Double.valueOf(txttemp.getText().toString().length() == 0 ? "999" : txttemp.getText().toString()) > 108.0)) {
                        Connection.MessageBox(AssPneu.this, "Current Temperature should be between 92 - 108");
                        txttemp.requestFocus();
                        return;
                    }
                }

            }

            if (!rdoCough1.isChecked() & !rdoCough2.isChecked() & secCough.isShown()) {
                Connection.MessageBox(AssPneu.this,"অপশন সিলেক্ট করা হয় নাই - (কাশি )");
                rdoCough1.requestFocus();
                return;
            }
            DV = Global.DateValidate(dtpCoughDt.getText().toString());
            if (DV.length() != 0 & secCoughDt.isShown()) {
                Connection.MessageBox(AssPneu.this,DV);
                dtpCoughDt.requestFocus();
                return;
            }

            else if (!rdoDBrea1.isChecked() & !rdoDBrea2.isChecked() & secDBrea.isShown()) {
                Connection.MessageBox(AssPneu.this,"অপশন সিলেক্ট করা হয় নাই - (শ্বাস কষ্ট)");
                rdoDBrea1.requestFocus();
                return;
            }
            DV = Global.DateValidate(dtpDBreaDt.getText().toString());
            if (DV.length() != 0 & secDBreaDt.isShown()) {
                Connection.MessageBox(AssPneu.this, DV);
                dtpDBreaDt.requestFocus();
                return;
            }

            else if (!rdoFever1.isChecked() & !rdoFever2.isChecked() & secFever.isShown()) {
                Connection.MessageBox(AssPneu.this,"অপশন সিলেক্ট করা হয় নাই - (জ্বর)");
                rdoFever1.requestFocus();
                return;
            }

            //27 04 2016
            if(rdoCough2.isChecked() & rdoDBrea2.isChecked() & rdoFever2.isChecked())
            {
                Connection.MessageBox(AssPneu.this, "কাশি,শ্বাসকষ্ট এবং জ্বর, সকল সমস্যা না হতে পারবে না।");
                return;
            }


            DV = Global.DateValidate(dtpFeverDt.getText().toString());
            if (DV.length() != 0 & secFeverDt.isShown()) {
                Connection.MessageBox(AssPneu.this, "জ্বর শুরুর তারিখ খালি থাকতে পারবেনা");
                dtpFeverDt.requestFocus();
                return;
            }

            else if (!rdoAsses1.isChecked() & !rdoAsses2.isChecked() & secAsses.isShown()) {
                Connection.MessageBox(AssPneu.this,  "শিশুকে পরীক্ষা করেছেন কিনা খালি থাকতে পারবে না");
                rdoAsses1.requestFocus();
                return;
            }



            if (!chkRR.isChecked())
            {
                if (txtRR1.getText().toString().length() == 0 & secRR1.isShown()) {
                    Connection.MessageBox(AssPneu.this, "এক মিনিটে কতবার শ্বাস প্রশ্বাস  গুনুনঃ - খালি থাকতে পারবেনা");
                    txtRR1.requestFocus();
                    return;
                }
            }
            if (txtRR2.getText().toString().length() == 0  & secRR2.isShown()) {
                Connection.MessageBox(AssPneu.this,"যদি বয়স <২ মাস এবং শ্বাস প্রশ্বাস >৫৯ হলে ২ বার গুনন - খালি থাকতে পারবেনা");
                txtRR2.requestFocus();
                return;
            }
            if (!rdoAsses2.isChecked())
            {
                if(txttemp.getText().toString().length()>0) {
                    String t1 = txttemp.getText().toString();
                    double Temp9 = Double.parseDouble(t1);
                    if (!rdoCough1.isChecked() & !rdoDBrea1.isChecked() & rdoFever1.isChecked()) {
                        if (!chkFever.isChecked() & (Temp9 < 99.5) & chkFever.isShown()) {
                            Connection.MessageBox(AssPneu.this, "চেকবক্স সিলেক্ট করা হয় নাই - (মা বলেছে জ্বর আছে কিন্ত মেপে জ্বর পাওয়া যায়নি)");
                            chkFever.requestFocus();
                            return;
                        }
                    }
                }
            }

            if (!rdoConv1.isChecked() & !rdoConv2.isChecked() & secConv.isShown()) {
                Connection.MessageBox(AssPneu.this, "অপশন সিলেক্ট করা হয় নাই - (খিঁচুনী)");
                rdoConv1.requestFocus();
                return;
            }

            else if (!rdoFBrea1.isChecked() & !rdoFBrea2.isChecked() & secFBrea.isShown()) {
                Connection.MessageBox(AssPneu.this, "অপশন সিলেক্ট করা হয় নাই - (দ্রুত শ্বাস)");
                rdoFBrea1.requestFocus();
                return;
            }

            else if (!rdoCInd1.isChecked() & !rdoCInd2.isChecked() & secCInd.isShown()) {
                Connection.MessageBox(AssPneu.this,"অপশন সিলেক্ট করা হয় নাই - (বুকের নীচের অংশ ডেবে যাওয়া)");
                rdoCInd1.requestFocus();
                return;
            }

            else if (!rdoLeth1.isChecked() & !rdoLeth2.isChecked()  & secLeth.isShown()) {
                Connection.MessageBox(AssPneu.this,"অপশন সিলেক্ট করা হয় নাই - (শিশুটি নেতিয়ে পড়েছে)");
                rdoLeth1.requestFocus();
                return;
            }

            else if (!rdoUCon1.isChecked() & !rdoUCon2.isChecked() & secUCon.isShown()) {
                Connection.MessageBox(AssPneu.this,"অপশন সিলেক্ট করা হয় নাই - (অজ্ঞান)");
                rdoUCon1.requestFocus();
                return;
            }

            else if (!rdoDrink1.isChecked() & !rdoDrink2.isChecked() & secDrink.isShown()) {
                Connection.MessageBox(AssPneu.this,"অপশন সিলেক্ট করা হয় নাই - (বুকের দুধ বা পানি পান না করতে পারলে)");
                rdoDrink1.requestFocus();
                return;
            }

            else if (!rdoVomit1.isChecked() & !rdoVomit2.isChecked() & secVomit.isShown()) {
                Connection.MessageBox(AssPneu.this,"অপশন সিলেক্ট করা হয় নাই - (সবকিছু বমি করে দিলে)");
                rdoVomit1.requestFocus();
                return;
            }

            else if (!rdoNone1.isChecked() & !rdoNone2.isChecked() & secNone.isShown()) {
                Connection.MessageBox(AssPneu.this,"অপশন সিলেক্ট করা হয় নাই - (উপরের কোন লক্ষণ নেই)");
                rdoNone1.requestFocus();
                return;
            }

            else if (!rdoCSPne1.isChecked() & !rdoCSPne2.isChecked() & secCSPne.isShown()) {
                Connection.MessageBox(AssPneu.this, "অপশন সিলেক্ট করা হয় নাই - (সম্ভাব্য মারাত্বক নিউমোনিয়া)");
                rdoCSPne1.requestFocus();
                return;
            }
            else if (rdoCSPne1.isChecked() & rdoCPPne1.isChecked() )
            {
                Connection.MessageBox(AssPneu.this, "একের অধিক  শ্রেনী বিভাগ হতে পারবেনা");
                rdoCSPne1.requestFocus();
                return;
            }
            else if (rdoCSPne1.isChecked() & rdoCNPne1.isChecked() )
            {
                Connection.MessageBox(AssPneu.this, "একের অধিক  শ্রেনী বিভাগ হতে পারবেনা");
                rdoCSPne1.requestFocus();
                return;
            }
            else if (!rdoCPPne1.isChecked() & !rdoCPPne2.isChecked() & secCPPne.isShown()) {
                Connection.MessageBox(AssPneu.this, "অপশন সিলেক্ট করা হয় নাই - (সম্ভাব্য নিউমোনিয়া)");
                rdoCPPne1.requestFocus();
                return;
            }

            else if (!rdoCNPne1.isChecked() & !rdoCNPne2.isChecked() & secCNPne.isShown()) {
                Connection.MessageBox(AssPneu.this, "অপশন সিলেক্ট করা হয় নাই - (নিউমোনিয়া নয়, সর্দি বা কাশি)");
                rdoCNPne1.requestFocus();
                return;
            }
            else if (rdoCNPne1.isChecked() & rdoCPPne1.isChecked() )
            {
                Connection.MessageBox(AssPneu.this, "একের অধিক  শ্রেনী বিভাগ হতে পারবেনা");
                rdoCNPne1.requestFocus();
                return;
            }
            else if (!rdoTSPne1.isChecked() & !rdoTSPne2.isChecked() & secTSPne.isShown()) {
                Connection.MessageBox(AssPneu.this, "অপশন সিলেক্ট করা হয় নাই - (জরুরী ভিত্তিতে কুমুদিনী হাসপাতালে প্রেরণ)");
                rdoTSPne1.requestFocus();
                return;
            }
            else if (rdoTSPne1.isChecked() & rdoTPPne1.isChecked() )
            {
                Connection.MessageBox(AssPneu.this, "একের অধিক ব্যবস্থাপনা হতে পারবেনা");
                rdoTSPne1.requestFocus();
                return;
            }
            else if (rdoTSPne1.isChecked() & rdoTNPne1.isChecked() )
            {
                Connection.MessageBox(AssPneu.this, "একের অধিক ব্যবস্থাপনা হতে পারবেনা");
                rdoTSPne1.requestFocus();
                return;
            }
            else if (!rdoTPPne1.isChecked() & !rdoTPPne2.isChecked() & secTPPne.isShown()) {
                Connection.MessageBox(AssPneu.this, "অপশন সিলেক্ট করা হয় নাই - (নিকটতম সাস্থকেন্দ্রে প্রেরণ)");
                rdoTPPne1.requestFocus();
                return;
            }

            else if (!rdoTNPne1.isChecked() & !rdoTNPne2.isChecked() & secTNPne.isShown()) {
                Connection.MessageBox(AssPneu.this, "অপশন সিলেক্ট করা হয় নাই - (বাড়ীতে চিকিৎসা)");
                rdoTNPne1.requestFocus();
                return;
            }
            else if (rdoTNPne1.isChecked() & rdoTPPne1.isChecked() )
            {
                Connection.MessageBox(AssPneu.this, "একের অধিক ব্যবস্থাপনা হতে পারবেনা");
                rdoTPPne1.requestFocus();
                return;
            }
            else if (!rdoLFever1.isChecked() & !rdoLFever2.isChecked() & secLFever.isShown()) {
                Connection.MessageBox(AssPneu.this,"অপশন সিলেক্ট করা হয় নাই - (তাপমাত্রা ৯৯.৫ ফাঃ - ১০০.৩ ফাঃ)");
                rdoLFever1.requestFocus();
                return;
            }

            else if (!rdoCLFever1.isChecked() & !rdoCLFever2.isChecked() & secCLFever.isShown()) {
                Connection.MessageBox(AssPneu.this,"অপশন সিলেক্ট করা হয় নাই - (অল্প জ্বর)");
                rdoCLFever1.requestFocus();
                return;
            }

            else if (!rdoTLFever1.isChecked() & !rdoTLFever2.isChecked() & secTLFever.isShown()) {
                Connection.MessageBox(AssPneu.this, "অপশন সিলেক্ট করা হয় নাই - (বাড়ীতে চিকিৎসা)");
                rdoTLFever1.requestFocus();
                return;
            }

            else if (!rdoMFever1.isChecked() & !rdoMFever2.isChecked() & secMFever.isShown()) {
                Connection.MessageBox(AssPneu.this,"অপশন সিলেক্ট করা হয় নাই - (তাপমাত্রা ১০০.৪  ফাঃ -  ১০১.৯  ফাঃ)");
                rdoMFever1.requestFocus();
                return;
            }

            else if (!rdoCMFever1.isChecked() & !rdoCMFever2.isChecked() & secCMFever.isShown()) {
                Connection.MessageBox(AssPneu.this,"অপশন সিলেক্ট করা হয় নাই - (মাঝারি জ্বর)");
                rdoCMFever1.requestFocus();
                return;
            }

            else if (!rdoTMFever1.isChecked() & !rdoTMFever2.isChecked() & secTMFever.isShown()) {
                Connection.MessageBox(AssPneu.this,"অপশন সিলেক্ট করা হয় নাই - (জরুরী ভিত্তিতে কুমুদিনী হাসপাতালে প্রেরণ)");
                rdoTMFever1.requestFocus();
                return;
            }

            else if (!rdoHFever1.isChecked() & !rdoHFever2.isChecked() & secHFever.isShown()) {
                Connection.MessageBox(AssPneu.this,"অপশন সিলেক্ট করা হয় নাই - (তাপমাত্রা ১০২  ফাঃ অথবা বেশী)");
                rdoHFever1.requestFocus();
                return;
            }

            else if (!rdoCHFever1.isChecked() & !rdoCHFever2.isChecked() & secCHFever.isShown()) {
                Connection.MessageBox(AssPneu.this,"অপশন সিলেক্ট করা হয় নাই - (বেশী জ্বর)");
                rdoCHFever1.requestFocus();
                return;
            }

            else if (!rdoTHFever1.isChecked() & !rdoTHFever2.isChecked() & secTHFever.isShown()) {
                Connection.MessageBox(AssPneu.this,"অপশন সিলেক্ট করা হয় নাই - (জরুরী ভিত্তিতে কুমুদিনী হাসপাতালে প্রেরণ)");
                rdoTHFever1.requestFocus();
                return;
            }

            else if (!rdoNeck1.isChecked() & !rdoNeck2.isChecked() & secNeck.isShown()) {
                Connection.MessageBox(AssPneu.this, "অপশন সিলেক্ট করা হয় নাই - (ঘাড় শক্ত)");
                rdoNeck1.requestFocus();
                return;
            }

            else if (!rdoFonta1.isChecked() & !rdoFonta2.isChecked() & secFonta.isShown()) {
                Connection.MessageBox(AssPneu.this, "অপশন সিলেক্ট করা হয় নাই - (মাথার তালু ফুলে যাওয়া)");
                rdoFonta1.requestFocus();
                return;
            }

            else if (!rdoConv21.isChecked() & !rdoConv22.isChecked() & secConv2.isShown()) {
                Connection.MessageBox(AssPneu.this,"অপশন সিলেক্ট করা হয় নাই - (খিঁচুনী)");
                rdoConv21.requestFocus();
                return;
            }

            else if (!rdoLeth21.isChecked() & !rdoLeth22.isChecked() & secLeth2.isShown()) {
                Connection.MessageBox(AssPneu.this,"অপশন সিলেক্ট করা হয় নাই - (শিশুটি নেতিয়ে পড়েছে)");
                rdoLeth21.requestFocus();
                return;
            }

            else if (!rdoUcon21.isChecked() & !rdoUcon22.isChecked() & secUcon2.isShown()) {
                Connection.MessageBox(AssPneu.this,"অপশন সিলেক্ট করা হয় নাই - (অজ্ঞান)");
                rdoUcon21.requestFocus();
                return;
            }

            else if (!rdoDrink21.isChecked() & !rdoDrink22.isChecked() & secDrink2.isShown()) {
                Connection.MessageBox(AssPneu.this,"অপশন সিলেক্ট করা হয় নাই - (বুকের দুধ বা পানি পান না করতে পারলে)");
                rdoDrink21.requestFocus();
                return;
            }

            else if (!rdoVomit21.isChecked() & !rdoVomit22.isChecked() & secVomit2.isShown()) {
                Connection.MessageBox(AssPneu.this,"অপশন সিলেক্ট করা হয় নাই - (সবকিছু বমি করে দেয়)");
                rdoVomit21.requestFocus();
                return;
            }

            else if (!rdoCMenin1.isChecked() & !rdoCMenin2.isChecked() & secCMenin.isShown()) {
                Connection.MessageBox(AssPneu.this,"অপশন সিলেক্ট করা হয় নাই - (সম্ভাব্য মেনিনজাইটিস )");
                rdoCMenin1.requestFocus();
                return;
            }

            else if (!rdoTMenin1.isChecked() & !rdoTMenin2.isChecked() & secTMenin.isShown()) {
                Connection.MessageBox(AssPneu.this,"অপশন সিলেক্ট করা হয় নাই - (জরুরী ভিত্তিতে কুমুদিনী হাসপাতালে প্রেরণ)");
                rdoTMenin1.requestFocus();
                return;
            }

            else if (!rdoRef1.isChecked() & !rdoRef2.isChecked() & !rdoRef3.isChecked() & secRef.isShown()) {
                Connection.MessageBox(AssPneu.this, "অপশন সিলেক্ট করা হয় নাই - (রেফার)");
                rdoRef1.requestFocus();
                return;
            }
            if (rdoRef1.isChecked() || rdoRef2.isChecked()) {
                if (txtRSlip.getText().toString().length() == 0 & secRSlip.isShown()) {
                    Connection.MessageBox(AssPneu.this, "রেফারাল স্লিপ নং. - খালি থাকতে পারবেনা");
                    txtRSlip.requestFocus();
                    return;
                }
            }
            if (txtRSlip.getText().toString().length() < 6  & secRSlip.isShown()) {
                Connection.MessageBox(AssPneu.this,"রেফারাল স্লিপ  ৬ সঙ্খারকম হবেনা");
                txtRSlip.requestFocus();
                return;
            }

            if (txtPhone.getText().toString().length() != 0 & secPhone.isShown()) {
                if (txtPhone.getText().toString().length() < 11 & secPhone.isShown()) {
                    Connection.MessageBox(AssPneu.this, "ফোন নম্বর ১১  সঙ্খারকম হবেনা");
                    txtPhone.requestFocus();
                    return;
                }
            }

            if (!rdoComp1.isChecked() & !rdoComp2.isChecked() & secComp.isShown()) {
                Connection.MessageBox(AssPneu.this,"অপশন সিলেক্ট করা হয় নাই - (রেফারালের ফলাফল)");
                rdoComp1.requestFocus();
                return;
            }

            else if (!rdoReason1.isChecked() & !rdoReason2.isChecked() & !rdoReason3.isChecked() & secReason.isShown()) {
                Connection.MessageBox(AssPneu.this,"অপশন সিলেক্ট করা হয় নাই - (শিশুটিকে পরীক্ষা না করার কারন)");
                rdoReason1.requestFocus();
                return;
            } else if (spnTPlace.getSelectedItemPosition() == 0 & secTPlace.isShown()) {
                Connection.MessageBox(AssPneu.this,"ড্রপডাউন মেনু সিলেক্ট করা হয় নাই - (কোথায় চিকিৎসার জন্য গিয়েছে?)");
                spnTPlace.requestFocus();
                return;
            }

            else if (!rdoTAbsIn1.isChecked() & !rdoTAbsIn2.isChecked() & secTAbsDur.isShown()) {
                Connection.MessageBox(AssPneu.this,	"অপশন সিলেক্ট করা হয় নাই - (চিকিৎসার জন্য কতদিন/ ঘণ্টা  হয় গিয়েছিল)");
                rdoTAbsIn1.requestFocus();
                return;
            }
            else if (txtTAbsDur.getText().toString().length() == 0 & secTAbsDur.isShown()) {
                Connection.MessageBox(AssPneu.this,	"চিকিৎসার জন্য কতদিন/ ঘণ্টা  হয় গিয়েছিল - খালি থাকতে পারবেনা");
                txtTAbsDur.requestFocus();
                return;
            }

            else if (!rdoHos1.isChecked() & !rdoHos2.isChecked() & !rdoHos3.isChecked() & secHos.isShown()) {
                Connection.MessageBox(AssPneu.this, "অপশন সিলেক্ট করা হয় নাই - (হাসপাতালে ভর্তি ছিল কি?)");
                rdoHos1.requestFocus();
                return;
            }

            String CDOB;
            CDOB=C.ReturnSingleValue("Select BDate  from Child WHERE   ChildID = '"+ ChildID +"'");

            int d = Global.DateDifferenceDays(dtpVDate.getText().toString(),Global.DateConvertDMY(CDOB));
            int m = (int)(d/30.44);


            //------------Less 2month------------------------------------------
            if (d>=29 & d<=59)
            {
                Integer var1 = Integer.parseInt(txtRR1.getText().toString().length()==0?"0":txtRR1.getText().toString());
                Integer var2 = Integer.parseInt(txtRR2.getText().toString().length()==0?"0":txtRR2.getText().toString());
                Integer rr2 = var2 == 0 ? var1 : var2;

                //String var1 = txtRR1.getText().toString().length()==0?"0":txtRR1.getText().toString();
                //String var2 = txtRR2.getText().toString().length()==0?"0":txtRR2.getText().toString();

                //Integer rr2 = Integer.parseInt(var2.length() == 0 ? var1 : var2);
                if (rr2 >= 60 & rdoFBrea2.isChecked())
                {
                    Connection.MessageBox(AssPneu.this, "শিশুর বয়স ২ মাসের কম এবং শ্বাসের হার ৬০ বা তার বেশী, তাহলে দ্রুত শ্বাস না হবেনা।");
                    return;
                }
                else if ((rr2 < 60) & (rdoFBrea1.isChecked()))
                {
                    Connection.MessageBox(AssPneu.this, "শিশুর বয়স ২ মাসের কম এবং শ্বাসের হার ৬০ এর কম, দ্রুত শ্বাস  হ্যাঁ  হাবে না");
                    return;
                }
                else if (rr2 >= 60 & rdoCSPne2.isChecked())
                {
                    Connection.MessageBox(AssPneu.this, "শিশুর বয়স ২ মাসের কম এবং শ্বাসের হার ৬০ বা তার বেশী, তাহলে সম্ভাব্য মারাত্বক নিউমোনিয়া না হতে পারবেনা");
                    return;
                }

                String a=txttemp.getText().toString();
                Float Temp =  Float.parseFloat(a.length()==0?"0":a);

                if(!rdoAsses2.isChecked())
                {
                    if ((Temp < 96.0) & (!rdoCSPne1.isChecked()) && (chkTemp.isChecked() == false)) {
                        Connection.MessageBox(AssPneu.this, "শিশুর বয়স ২ মাসের কম এবং তাপমাত্রা ৯৬.০ এর কম, সম্ভাব্য মারাত্বক নিউমোনিয়া না হতে পারবেনা");
                        return;
                    }
                }

                if ((Temp > 101.0) & (!rdoCSPne1.isChecked()) && (chkTemp.isChecked()==false))
                {
                    Connection.MessageBox(AssPneu.this, "শিশুর বয়স ২ মাসের কম এবং তাপমাত্রা ১০১ এর বেশী , সম্ভাব্য মারাত্বক নিউমোনিয়া না হতে পারবেনা");
                    return;
                }

            }
            else if (d>60 & d<=365)
            {
                Integer var1 = Integer.parseInt(txtRR1.getText().toString().length()==0?"0":txtRR1.getText().toString());
                Integer var2 = Integer.parseInt(txtRR2.getText().toString().length()==0?"0":txtRR2.getText().toString());
                Integer rr2 = var2 == 0 ? var1 : var2;

                if (rr2 >= 50 & rdoFBrea2.isChecked())
                {
                    Connection.MessageBox(AssPneu.this, "শিশুর বয়স ২-১২ মাস হলে এবং শ্বাসের হার ৫০ বা তার বেশী হলে দ্রুত শ্বাস না হবে না।");
                    return;
                }
                else if ((rr2 < 50) & (rdoFBrea1.isChecked()))
                {
                    Connection.MessageBox(AssPneu.this, "শিশুর বয়স ২-১২ মাস হলে এবং শ্বাসের হার ৫০ এর কম হলে দ্রুত শ্বাস  হ্যাঁ  হবে না");
                    return;
                }
            }
            else if (d>365)
            {
                Integer var1 = Integer.parseInt(txtRR1.getText().toString().length()==0?"0":txtRR1.getText().toString());
                Integer var2 = Integer.parseInt(txtRR2.getText().toString().length()==0?"0":txtRR2.getText().toString());
                //Integer rr2 = Integer.parseInt(var2.length() == 0 ? "0" : var2);
                Integer rr2 = var2 == 0 ? var1 : var2;


                if (rr2 >= 40 & rdoFBrea2.isChecked())
                {
                    Connection.MessageBox(AssPneu.this, "শিশুর বয়স ১২ মাসের বেশী  এবং শ্বাসের হার ৪০ বা তার বেশী হলে দ্রুত শ্বাস না হবে না।");
                    return;
                }
                else if ((rr2 < 40) & (rdoFBrea1.isChecked()))
                {
                    Connection.MessageBox(AssPneu.this, "শিশুর বয়স ১২ মাসের বেশী  এবং শ্বাসের হার ৪০ এর কম হলে দ্রুত শ্বাস  হ্যাঁ  হবে না");
                    return;
                }
            }




            //------------Greater 2month------------------------------------------
            if (m>=2)
            {

                if ((rdoConv2.isChecked() & rdoFBrea1.isChecked() & rdoCInd2.isChecked() & rdoLeth2.isChecked() & rdoUCon2.isChecked() & rdoDrink2.isChecked() & rdoVomit2.isChecked() & rdoNone2.isChecked()) && (!rdoCPPne1.isChecked()))
                {
                    Connection.MessageBox(AssPneu.this, "দ্রুত শ্বাস  হ্যাঁ  এবং  সম্ভাব্য নিউমোনিয়া  না  হতে পারবে না");
                    return;
                }
                else if ((rdoConv1.isChecked() & rdoCInd1.isChecked() & rdoLeth1.isChecked() & rdoUCon1.isChecked() & rdoDrink1.isChecked() & rdoVomit1.isChecked() & rdoNone2.isChecked()) && (!rdoCSPne1.isChecked()))
                {
                    Connection.MessageBox(AssPneu.this, "1কাশি এবং শ্বাস কষ্টের লক্ষণ হ্যাঁ কিন্ত সম্ভাব্য মারাত্বক নিউমোনিয়া না হতে পারবেনা");
                    return;
                }
                else if ((rdoConv2.isChecked() & rdoFBrea1.isChecked() & rdoCInd2.isChecked() & rdoLeth2.isChecked() & rdoUCon2.isChecked() & rdoDrink2.isChecked() & rdoVomit2.isChecked() & rdoNone2.isChecked()) && (!rdoCPPne1.isChecked()))
                {
                    Connection.MessageBox(AssPneu.this, "দ্রুত শ্বাস  হ্যাঁ  এবং  সম্ভাব্য নিউমোনিয়া  না  হতে পারবে না");
                    return;
                }

                else if((rdoConv2.isChecked() & rdoFBrea2.isChecked() & rdoCInd2.isChecked() & rdoLeth2.isChecked() & rdoUCon2.isChecked() & rdoDrink2.isChecked() & rdoVomit2.isChecked() & rdoNone2.isChecked()) && (rdoCSPne1.isChecked() || rdoCPPne1.isChecked() || rdoCNPne1.isChecked()))
                {
                    Connection.MessageBox(AssPneu.this, "কাশি এবং শ্বাস কষ্টের সবগুলো লক্ষণ না সুতরাং শ্রেনী বিভাগ হ্যাঁ  হতে পারবে না");
                    return;
                }

            }
            //------------Greater 2-12month------------------------------------------
            if (m>=2 & m<12)
            {
                Integer var11 = Integer.parseInt(txtRR1.getText().toString().length()==0?"0":txtRR1.getText().toString());
                //String var11 = txtRR1.getText().toString().length()==0?"0":txtRR1.getText().toString();
                Integer rr = var11;

                if (rr >=50 & rdoFBrea2.isChecked())
                {
                    Connection.MessageBox(AssPneu.this, "শিশুর বয়স ২ মাস থেকে ১২ এবং শ্বাসের হার ৫০ বা তার বেশী, তাহলে দ্রুত শ্বাস না হবেনা।");
                    return;
                }
            }
            //------------Greater 2-12month------------------------------------------
            if (m>=12)
            {
                Integer var11 = Integer.parseInt(txtRR1.getText().toString().length()==0?"0":txtRR1.getText().toString());
                //String var11 = txtRR1.getText().toString().length()==0?"0":txtRR1.getText().toString();
                Integer rr = var11; //Integer.parseInt(var11.length() == 0 ? "0" : var11);

                if (rr >= 40 & rdoFBrea2.isChecked())
                {
                    Connection.MessageBox(AssPneu.this, "শিশুর বয়স ১২ মাস থেকে ৫ বছরের মধ্যে এবং শ্বাসের হার ৪০ বা তার বেশী, তাহলে দ্রুত শ্বাস না হবেনা।");
                    return;
                }
            }
            //================================================================
            if(!rdoAsses1.isChecked() & !rdoAsses2.isChecked())
            {
                Connection.MessageBox(AssPneu.this, "শিশুকে পরীক্ষা করেছেন হ্যাঁ/না সিলেক্ট করা হয় নাই");
                return;
            }
            else if((rdoConv1.isChecked() || rdoFBrea1.isChecked() || rdoCInd1.isChecked() || rdoLeth1.isChecked() || rdoUCon1.isChecked() || rdoDrink1.isChecked() || rdoVomit1.isChecked()) && (rdoNone1.isChecked()))
            {
                Connection.MessageBox(AssPneu.this, "কাশি এবং শ্বাস কষ্টের  উপর্সগ  হ্যাঁ  এবং কোন লক্ষণ নেই  হ্যাঁ  হতে পারবে না.");
                return;
            }
            else if((rdoConv2.isChecked() & rdoFBrea2.isChecked() & rdoCInd2.isChecked() & rdoLeth2.isChecked() & rdoUCon2.isChecked() & rdoDrink2.isChecked() & rdoVomit2.isChecked() & rdoNone1.isChecked()) & (rdoCNPne2.isChecked()))
            {
                Connection.MessageBox(AssPneu.this, "কোন লক্ষণ নেই  হ্যাঁ এবং  নিউমোনিয়া নয়  না হতে পারবে না");
                return;
            }
            else if((rdoConv2.isChecked() & rdoFBrea2.isChecked() & rdoCInd2.isChecked() & rdoLeth2.isChecked() & rdoUCon2.isChecked() & rdoDrink2.isChecked() & rdoVomit2.isChecked() & rdoNone2.isChecked()) && (rdoCough1.isChecked()))
            {
                Connection.MessageBox(AssPneu.this, "মা বলেছে কাশি  তাহলে কাশি এবং শ্বাস কষ্টের সব  উপর্সগ  না  হতে পারবে না.");
                return;
            }



            else if((rdoCSPne2.isChecked() & rdoCPPne2.isChecked() & rdoCNPne2.isChecked()) && (!rdoTSPne2.isChecked() || !rdoTPPne2.isChecked() || !rdoTNPne2.isChecked()))
            {
                Connection.MessageBox(AssPneu.this, "শ্রেনী বিভাগ না হোলে  ব্যবস্থাপনা হ্যাঁ হতে পারবেনা");
                return;
            }
            else if((rdoConv1.isChecked()==true || rdoCInd1.isChecked()==true || rdoLeth1.isChecked()==true || rdoUCon1.isChecked()==true || rdoDrink1.isChecked()==true || rdoVomit1.isChecked()==true) && (rdoCSPne2.isChecked()))
            {
                Connection.MessageBox(AssPneu.this, "কাশি এবং শ্বাস কষ্টের  লক্ষণগুলো  হ্যাঁ  এবং সম্ভাব্য মারাত্বক নিউমোনিয়া  না  হতে পারবে না");
                return;
            }
            //------------Menin
            else if((rdoNeck2.isChecked() & rdoFonta2.isChecked() & rdoConv22.isChecked() & rdoLeth22.isChecked() & rdoUcon22.isChecked() & rdoDrink22.isChecked() & rdoVomit22.isChecked()) && (rdoCMenin1.isChecked()))
            {
                Connection.MessageBox(AssPneu.this, "জ্বরের সবগুলো  উপর্সগ না এবং সম্ভাব্য মেনিনজাইটিস  হ্যাঁ সঠিক নয়");
                return;
            }
            else if((rdoNeck1.isChecked() || rdoFonta1.isChecked() || rdoConv21.isChecked() || rdoLeth21.isChecked() || rdoUcon21.isChecked() || rdoDrink21.isChecked() || rdoVomit21.isChecked()) && (rdoCMenin2.isChecked()))
            {
                Connection.MessageBox(AssPneu.this, "জ্বরের উপর্সগ হ্যাঁ   এবং সম্ভাব্য মেনিনজাইটিস  না সঠিক নয়");
                return;
            }
            else if(rdoCMenin1.isChecked() & rdoTMenin2.isChecked())
            {
                Connection.MessageBox(AssPneu.this, "সম্ভাব্য মেনিনজাইটিস  হ্যাঁ  এবং জরুরী ভিত্তিতে কুমুদিনী হাসপাতালে প্রেরণ  না  সঠিক নয়");
                return;
            }
            else if(rdoCMenin2.isChecked() & rdoTMenin1.isChecked())
            {
                Connection.MessageBox(AssPneu.this, "সম্ভাব্য মেনিনজাইটিস  না  এবং জরুরী ভিত্তিতে কুমুদিনী হাসপাতালে প্রেরণ  হ্যাঁ  সঠিক নয়");
                return;
            }
            //------------Menin end
            else if((rdoCSPne1.isChecked() || rdoCMFever1.isChecked() || rdoCHFever1.isChecked() || rdoCMenin1.isChecked()) && (rdoRef3.isChecked()))
            {
                Connection.MessageBox(AssPneu.this, "রেফার এর লক্ষণগুলো হ্যাঁ কিন্তু     রেফার করা হয়নি   হতে পারবে না");
                return;
            }
            else if((!rdoCSPne1.isChecked() & !rdoCMFever1.isChecked() & !rdoCHFever1.isChecked() & !rdoCMenin1.isChecked()) && (rdoRef1.isChecked() || rdoRef2.isChecked()))
            {
                Connection.MessageBox(AssPneu.this, "রেফার এর লক্ষণগুলো না  কিন্তু রেফার করা  হয়েছে   হতে পারবে না");
                return;
            }
            String a=txttemp.getText().toString();
            Float Temp =  Float.parseFloat(a.length()==0?"0":a);
            if ((Temp >= 99.5) && (chkTemp.isChecked()==false)) {
                if (rdoConv1.isChecked() & !rdoConv21.isChecked()) {
                    Connection.MessageBox(AssPneu.this, "কাশি এবং শ্বাস কষ্টের লক্ষণের মধ্যে খিঁচুনী থাকলে জ্বরের উপর্সগ খিঁচুনী হ্যাঁ হবে");
                    return;
                }
                //25 04 2016
                else if (rdoLeth1.isChecked() & !rdoLeth21.isChecked()) {
                    Connection.MessageBox(AssPneu.this, "কাশি এবং শ্বাস কষ্টের লক্ষণের মধ্যে শিশুটি নেতিয়ে পড়েছে থাকলে জ্বরের উপর্সগ শিশুটি নেতিয়ে পড়েছে হ্যাঁ হবে");
                    return;
                }
                else if (rdoUCon1.isChecked() & !rdoUcon21.isChecked()) {
                    Connection.MessageBox(AssPneu.this, "কাশি এবং শ্বাস কষ্টের লক্ষণের মধ্যে অজ্ঞান থাকলে জ্বরের উপর্সগ অজ্ঞান হ্যাঁ হবে");
                    return;
                }
                else if (rdoDrink1.isChecked() & !rdoDrink21.isChecked()) {
                    Connection.MessageBox(AssPneu.this, "কাশি এবং শ্বাস কষ্টের লক্ষণের মধ্যে 'বুকের দুধ বা পানি পান না করতে পারলে' থাকলে জ্বরের উপর্সগ 'বুকের দুধ বা পানি পান না করতে পারলে' হ্যাঁ হবে");
                    return;
                }
                else if (rdoVomit1.isChecked() & !rdoVomit21.isChecked()) {
                    Connection.MessageBox(AssPneu.this, "কাশি এবং শ্বাস কষ্টের লক্ষণের মধ্যে 'সবকিছু বমি করে দেয়' থাকলে জ্বরের উপর্সগ 'সবকিছু বমি করে দেয়' হ্যাঁ হবে");
                    return;
                }

            }
            if (!rdoConv2.isChecked() || !rdoFBrea2.isChecked() || !rdoCInd2.isChecked() || !rdoLeth2.isChecked() || !rdoUCon2.isChecked() || !rdoDrink2.isChecked() || !rdoVomit2.isChecked() || !rdoNone2.isChecked()) {
                if(rdoCSPne1.isChecked() & !rdoTSPne1.isChecked())
                {
                    Connection.MessageBox(AssPneu.this, "সম্ভাব্য মারাত্বক নিউমোনিয়া হ্যাঁ হলে জরুরী ভিত্তিতে কুমুদিনী হাসপাতালে প্রেরণ না হতে পারবেনা");
                    return;
                }
                else if (rdoCPPne1.isChecked() & !rdoTPPne1.isChecked()) {
                    Connection.MessageBox(AssPneu.this, "সম্ভাব্য নিউমোনিয়া হ্যাঁ হলে নিকটতম স্বাস্থ্যকেন্দ্রে প্রেরণ না হতে পারবেনা");
                    return;
                }
                else if (rdoCNPne1.isChecked() & !rdoTNPne1.isChecked()) {
                    Connection.MessageBox(AssPneu.this, "নিউমোনিয়া নয়  না এবং বাড়ীতে চিকিৎসা না হতে পারবেনা");
                    return;
                }
            }




            if (txtRSlip.getText().toString().length() != 0 & secRSlip.isShown()) {
                String RFNo;
                RFNo = C.ReturnSingleValue("Select RSlip  from AssNewBorn WHERE   RSlip = '" + txtRSlip.getText() + "' and childid<>'"+ ChildID +"'");
                if (RFNo.trim().equalsIgnoreCase(txtRSlip.getText().toString().trim())) {
                    Connection.MessageBox(AssPneu.this, "এই রেফারাল স্লিপ নং পূর্বে নবজাতকে-এ ব্যবহার করা হয়েছে");
                    txtRSlip.requestFocus();
                    return;
                }
                String R2;
                R2 = C.ReturnSingleValue("Select RSlip  from AssPneu WHERE   RSlip = '" + txtRSlip.getText() + "' and childid<>'"+ txtChildId.getText().toString() +"'");
                if (R2.trim().equalsIgnoreCase(txtRSlip.getText().toString().trim())) {
                    Connection.MessageBox(AssPneu.this, "এই রেফারাল স্লিপ নং পূর্বে (০-৫৯ মাস)-এ  ব্যবহার করা হয়েছে");
                    txtRSlip.requestFocus();
                    return;
                }
            }


            String SQL = "";
            if (!C.Existence("Select ChildId from "
                    + TableName	+ "  Where ChildId='" + txtChildId.getText().toString()	+ "' and Week='" + txtWeek.getSelectedItem().toString() + "' and VType='" + VisitType + "' and Visit='" + txtVisit.getSelectedItem().toString() + "'"))
            {
                SQL = "Insert into "
                        + TableName
                        + "(ChildId,CID,PID,Week,VType,Visit,UserId,EnDt,Upload)Values('"
                        + txtChildId.getText() + "','','','" + txtWeek.getSelectedItem().toString() + "','"
                        + VisitType + "','"
                        + txtVisit.getSelectedItem().toString() + "','" + g.getUserId() + "','"
                        + Global.DateTimeNowYMDHMS() + "','2')";
                C.Save(SQL);
            }

            SQL = "Update " + TableName + " Set Upload='2',";
            //SQL += "ChildId = '" + txtChildId.getText().toString() + "',";
            //SQL += "CID = '" + txtCID.getText().toString() + "',";
            //SQL += "PID = '" + txtPID.getText().toString() + "',";
            //SQL += "Week = '" + txtWeek.getText().toString() + "',";
            SQL += "VDate = '"+ Global.DateConvertYMD(dtpVDate.getText().toString())+ "',";
            //RadioButton rbVType = (RadioButton) findViewById(rdogrpVType.getCheckedRadioButtonId());
            //SQL += "VType = '" + (rbVType == null ? "" : (Global.Left(rbVType.getText().toString(), 1))) + "',";
            SQL += "Visit = '" + txtVisit.getSelectedItem().toString() + "',";
            SQL += "temp = '" + txttemp.getText().toString() + "',";
            RadioButton rbCough = (RadioButton) findViewById(rdogrpCough.getCheckedRadioButtonId());
            SQL += "Cough = '" + (rbCough == null ? "" : (Global.Left(rbCough.getText().toString(), 1))) + "',";
            SQL += "CoughDt = '" + Global.DateConvertYMD(dtpCoughDt.getText().toString()) + "',";
            RadioButton rbDBrea = (RadioButton) findViewById(rdogrpDBrea.getCheckedRadioButtonId());
            SQL += "DBrea = '"	+ (rbDBrea == null ? "" : (Global.Left(rbDBrea.getText().toString(), 1))) + "',";
            SQL += "DBreaDt = '" + Global.DateConvertYMD(dtpDBreaDt.getText().toString()) + "',";
            RadioButton rbFever = (RadioButton) findViewById(rdogrpFever.getCheckedRadioButtonId());
            SQL += "Fever = '" + (rbFever == null ? "" : (Global.Left(rbFever.getText().toString(), 1))) + "',";
            SQL += "FeverDt = '" + Global.DateConvertYMD(dtpFeverDt.getText().toString()) + "',";
            SQL += "OthCom1 = '" + (spnOthCom1.getSelectedItemPosition() == 0 ? "" : Global.Left(spnOthCom1.getSelectedItem().toString(), 2)) + "',";
            SQL += "OthCom2 = '" + (spnOthCom2.getSelectedItemPosition() == 0 ? "" : Global.Left(spnOthCom2.getSelectedItem().toString(), 2)) + "',";
            SQL += "OthCom3 = '" + (spnOthCom3.getSelectedItemPosition() == 0 ? "" : Global.Left(spnOthCom3.getSelectedItem().toString(), 2)) + "',";
            RadioButton rbAsses = (RadioButton) findViewById(rdogrpAsses.getCheckedRadioButtonId());
            SQL += "Asses = '"	+ (rbAsses == null ? "" : (Global.Left(rbAsses.getText().toString(), 1))) + "',";
            SQL += "RR1 = '" + txtRR1.getText().toString() + "',";
            SQL += "RR2 = '" + txtRR2.getText().toString() + "',";
            RadioButton rbConv = (RadioButton) findViewById(rdogrpConv.getCheckedRadioButtonId());
            SQL += "Conv = '" + (rbConv == null ? "" : (Global.Left(rbConv.getText().toString(), 1))) + "',";
            RadioButton rbFBrea = (RadioButton) findViewById(rdogrpFBrea.getCheckedRadioButtonId());
            SQL += "FBrea = '" + (rbFBrea == null ? "" : (Global.Left(rbFBrea.getText().toString(), 1))) + "',";
            RadioButton rbCInd = (RadioButton) findViewById(rdogrpCInd.getCheckedRadioButtonId());
            SQL += "CInd = '"+ (rbCInd == null ? "" : (Global.Left(rbCInd.getText().toString(), 1))) + "',";
            RadioButton rbLeth = (RadioButton) findViewById(rdogrpLeth.getCheckedRadioButtonId());
            SQL += "Leth = '" + (rbLeth == null ? "" : (Global.Left(rbLeth.getText().toString(), 1))) + "',";
            RadioButton rbUCon = (RadioButton) findViewById(rdogrpUCon.getCheckedRadioButtonId());
            SQL += "UCon = '" + (rbUCon == null ? "" : (Global.Left(rbUCon.getText().toString(), 1))) + "',";
            RadioButton rbDrink = (RadioButton) findViewById(rdogrpDrink.getCheckedRadioButtonId());
            SQL += "Drink = '"	+ (rbDrink == null ? "" : (Global.Left(rbDrink.getText().toString(), 1))) + "',";
            RadioButton rbVomit = (RadioButton) findViewById(rdogrpVomit.getCheckedRadioButtonId());
            SQL += "Vomit = '" + (rbVomit == null ? "" : (Global.Left(rbVomit.getText().toString(), 1))) + "',";
            RadioButton rbNone = (RadioButton) findViewById(rdogrpNone.getCheckedRadioButtonId());
            SQL += "None = '" + (rbNone == null ? "" : (Global.Left(rbNone.getText().toString(), 1))) + "',";
            RadioButton rbCSPne = (RadioButton) findViewById(rdogrpCSPne.getCheckedRadioButtonId());
            SQL += "CSPne = '" + (rbCSPne == null ? "" : (Global.Left(rbCSPne.getText().toString(), 1))) + "',";
            RadioButton rbCPPne = (RadioButton) findViewById(rdogrpCPPne.getCheckedRadioButtonId());
            SQL += "CPPne = '" + (rbCPPne == null ? "" : (Global.Left(rbCPPne.getText().toString(), 1))) + "',";
            RadioButton rbCNPne = (RadioButton) findViewById(rdogrpCNPne.getCheckedRadioButtonId());
            SQL += "CNPne = '"	+ (rbCNPne == null ? "" : (Global.Left(rbCNPne.getText().toString(), 1))) + "',";
            RadioButton rbTSPne = (RadioButton) findViewById(rdogrpTSPne.getCheckedRadioButtonId());
            SQL += "TSPne = '"	+ (rbTSPne == null ? "" : (Global.Left(rbTSPne.getText().toString(), 1))) + "',";
            RadioButton rbTPPne = (RadioButton) findViewById(rdogrpTPPne.getCheckedRadioButtonId());
            SQL += "TPPne = '" + (rbTPPne == null ? "" : (Global.Left(rbTPPne.getText().toString(), 1))) + "',";
            RadioButton rbTNPne = (RadioButton) findViewById(rdogrpTNPne.getCheckedRadioButtonId());
            SQL += "TNPne = '" + (rbTNPne == null ? "" : (Global.Left(rbTNPne.getText()	.toString(), 1))) + "',";
            RadioButton rbLFever = (RadioButton) findViewById(rdogrpLFever.getCheckedRadioButtonId());
            SQL += "LFever = '"	+ (rbLFever == null ? "" : (Global.Left(rbLFever.getText().toString(), 1))) + "',";
            RadioButton rbCLFever = (RadioButton) findViewById(rdogrpCLFever.getCheckedRadioButtonId());
            SQL += "CLFever = '" + (rbCLFever == null ? "" : (Global.Left(rbCLFever.getText().toString(), 1))) + "',";
            RadioButton rbTLFever = (RadioButton) findViewById(rdogrpTLFever.getCheckedRadioButtonId());
            SQL += "TLFever = '" + (rbTLFever == null ? "" : (Global.Left(rbTLFever.getText().toString(), 1))) + "',";
            RadioButton rbMFever = (RadioButton) findViewById(rdogrpMFever.getCheckedRadioButtonId());
            SQL += "MFever = '"	+ (rbMFever == null ? "" : (Global.Left(rbMFever.getText().toString(), 1))) + "',";
            RadioButton rbCMFever = (RadioButton) findViewById(rdogrpCMFever.getCheckedRadioButtonId());
            SQL += "CMFever = '" + (rbCMFever == null ? "" : (Global.Left(rbCMFever.getText().toString(), 1))) + "',";
            RadioButton rbTMFever = (RadioButton) findViewById(rdogrpTMFever.getCheckedRadioButtonId());
            SQL += "TMFever = '" + (rbTMFever == null ? "" : (Global.Left(rbTMFever.getText().toString(), 1))) + "',";
            RadioButton rbHFever = (RadioButton) findViewById(rdogrpHFever.getCheckedRadioButtonId());
            SQL += "HFever = '" + (rbHFever == null ? "" : (Global.Left(rbHFever.getText().toString(), 1))) + "',";
            RadioButton rbCHFever = (RadioButton) findViewById(rdogrpCHFever.getCheckedRadioButtonId());
            SQL += "CHFever = '" + (rbCHFever == null ? "" : (Global.Left(rbCHFever.getText().toString(), 1))) + "',";
            RadioButton rbTHFever = (RadioButton) findViewById(rdogrpTHFever.getCheckedRadioButtonId());
            SQL += "THFever = '" + (rbTHFever == null ? "" : (Global.Left(rbTHFever.getText().toString(), 1))) + "',";
            RadioButton rbNeck = (RadioButton) findViewById(rdogrpNeck.getCheckedRadioButtonId());
            SQL += "Neck = '" + (rbNeck == null ? "" : (Global.Left(rbNeck.getText().toString(), 1))) + "',";
            RadioButton rbFonta = (RadioButton) findViewById(rdogrpFonta.getCheckedRadioButtonId());
            SQL += "Fonta = '" + (rbFonta == null ? "" : (Global.Left(rbFonta.getText().toString(), 1))) + "',";
            RadioButton rbConv2 = (RadioButton) findViewById(rdogrpConv2.getCheckedRadioButtonId());
            SQL += "Conv2 = '"	+ (rbConv2 == null ? "" : (Global.Left(rbConv2.getText().toString(), 1))) + "',";
            RadioButton rbLeth2 = (RadioButton) findViewById(rdogrpLeth2.getCheckedRadioButtonId());
            SQL += "Leth2 = '"	+ (rbLeth2 == null ? "" : (Global.Left(rbLeth2.getText().toString(), 1))) + "',";
            RadioButton rbUcon2 = (RadioButton) findViewById(rdogrpUcon2.getCheckedRadioButtonId());
            SQL += "Ucon2 = '"	+ (rbUcon2 == null ? "" : (Global.Left(rbUcon2.getText().toString(), 1))) + "',";
            RadioButton rbDrink2 = (RadioButton) findViewById(rdogrpDrink2.getCheckedRadioButtonId());
            SQL += "Drink2 = '"	+ (rbDrink2 == null ? "" : (Global.Left(rbDrink2.getText().toString(), 1))) + "',";
            RadioButton rbVomit2 = (RadioButton) findViewById(rdogrpVomit2.getCheckedRadioButtonId());
            SQL += "Vomit2 = '"	+ (rbVomit2 == null ? "" : (Global.Left(rbVomit2.getText().toString(), 1))) + "',";
            RadioButton rbCMenin = (RadioButton) findViewById(rdogrpCMenin.getCheckedRadioButtonId());
            SQL += "CMenin = '"	+ (rbCMenin == null ? "" : (Global.Left(rbCMenin.getText().toString(), 1))) + "',";
            RadioButton rbTMenin = (RadioButton) findViewById(rdogrpTMenin.getCheckedRadioButtonId());
            SQL += "TMenin = '"	+ (rbTMenin == null ? "" : (Global.Left(rbTMenin.getText().toString(), 1))) + "',";
            RadioButton rbRef = (RadioButton) findViewById(rdogrpRef.getCheckedRadioButtonId());
            SQL += "Ref = '" + (rbRef == null ? "" : (Global.Left(rbRef.getText().toString(), 1))) + "',";
            SQL += "RSlip = '" + txtRSlip.getText().toString() + "',";
            RadioButton rbComp = (RadioButton) findViewById(rdogrpComp.getCheckedRadioButtonId());
            SQL += "Comp = '" + (rbComp == null ? "" : (Global.Left(rbComp.getText().toString(), 1))) + "',";
            RadioButton rbReason = (RadioButton) findViewById(rdogrpReason.getCheckedRadioButtonId());
            SQL += "Reason = '"	+ (rbReason == null ? "" : (Global.Left(rbReason.getText().toString(), 1))) + "',";
            SQL += "TPlace = '"	+ (spnTPlace.getSelectedItemPosition() == 0 ? "" : Global.Left(spnTPlace.getSelectedItem().toString(), 1)) + "',";
            //SQL += "TPlaceC = '" + Global.Left(spnTPlaceC.getSelectedItem().toString(), 2) + "',";
            //SQL+="TPlaceC = '"+ (spnTPlaceC.getSelectedItemPosition()==0?"":Global.Left(spnTPlaceC.getSelectedItem().toString(),2)) +"',";
            SQL += "TPlaceC = '" + txtTPlaceC.getText().toString() + "',";
            RadioButton rbTAbsIn = (RadioButton) findViewById(rdogrpTAbsIn.getCheckedRadioButtonId());
            SQL += "TAbsIn = '" + (rbTAbsIn == null ? "" : (Global.Left(rbTAbsIn.getText().toString(), 1))) + "',";
            SQL += "TAbsDur = '" + txtTAbsDur.getText().toString() + "',";
            RadioButton rbHos = (RadioButton) findViewById(rdogrpHos.getCheckedRadioButtonId());
            SQL += "Hos = '" + (rbHos == null ? "" : (Global.Left(rbHos.getText().toString(), 1))) + "'";
			/*SQL += "EnDt = '" + Global.DateConvertYMD(dtpEnDt.getText().toString())	+ "',";
			SQL += "UserId = '" + txtUserId.getText().toString() + "',";
			SQL += "Upload = '" + txtUpload.getText().toString() + "',";
			SQL += "UploadDT = '" + Global.DateConvertYMD(dtpUploadDT.getText().toString())	+ "'";
		*/
            SQL += "  Where ChildId='" + txtChildId.getText().toString() + "' and Week='" + txtWeek.getSelectedItem().toString() + "' and VType='" + VisitType	+ "' and Visit='" + txtVisit.getSelectedItem().toString() + "'";
            C.Save(SQL);
            if (txtPhone.getText().toString().length() != 0 & secPhone.isShown()) {
                C.Save("Update Child set ContactNo='" + txtPhone.getText().toString() + "',upload='2' Where ChildId='" + ChildID + "'");
            }

            //absent for sickness and hospitalization status=don't know
            //24 May 2016
            if(Child_Outside_Area.equals("n")) {
                //Referral Information
                if(rdoRef1.isChecked() | rdoRef2.isChecked())
                {
                    String RefInfo = ChildID+","+txtWeek.getSelectedItem().toString()+","+VisitType+","+txtVisit.getSelectedItem().toString()+","+Global.DateConvertYMD(dtpVDate.getText().toString());

                    if(VisitType.equals("1"))
                        C.Save("Update Child set Upload='2',Referral='"+ RefInfo +"' where ChildId='"+ ChildID +"'");
                    else if(VisitType.equals("2"))
                        C.Save("Update Child set Upload='2',Referral_Foll='"+ RefInfo +"' where ChildId='"+ ChildID +"'");
                    else if(VisitType.equals("3"))
                        C.Save("Update Child set Upload='2',Referral_Add='"+ RefInfo +"' where ChildId='"+ ChildID +"'");
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

                //Referral Information
                if(rdoRef1.isChecked() | rdoRef2.isChecked())
                {
                    String RefInfo = ChildID+","+txtWeek.getSelectedItem().toString()+","+VisitType+","+txtVisit.getSelectedItem().toString()+","+Global.DateConvertYMD(dtpVDate.getText().toString());

                    if(VisitType.equals("1"))
                        C.Save("Update Child_Outside set Upload='2',Referral='"+ RefInfo +"' where ChildId='"+ ChildID +"'");
                    else if(VisitType.equals("2"))
                        C.Save("Update Child_Outside set Upload='2',Referral_Foll='"+ RefInfo +"' where ChildId='"+ ChildID +"'");
                    else if(VisitType.equals("3"))
                        C.Save("Update Child_Outside set Upload='2',Referral_Add='"+ RefInfo +"' where ChildId='"+ ChildID +"'");
                }

                if (rdoHos3.isChecked()) {
                    String RefInfo1 = ChildID + "," + txtWeek.getSelectedItem().toString() + "," + VisitType + "," + txtVisit.getSelectedItem().toString() + "," + Global.DateConvertYMD(dtpVDate.getText().toString());
                    C.Save("Update Child_Outside set Upload='2',Absent_Sick='" + RefInfo1 + "' where ChildId='" + ChildID + "'");
                } else {
                    C.Save("Update Child_Outside set Upload='2',Absent_Sick='' where ChildId='" + ChildID + "'");
                }

                //Save Assessment for Newborn
                //Save Assessment for Under 5 Child
            }

            Connection.MessageBox(AssPneu.this, "Saved Successfully");
        } catch (Exception e) {
            Connection.MessageBox(AssPneu.this, e.getMessage());
            return;
        }
    }

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
            Connection.MessageBox(AssPneu.this, e.getMessage());
            return;
        }
    }


    private void DataSearch(String ChildId, String Week, String VType, String Visit) {
        try {

            RadioButton rb;
            Cursor cur = C.ReadData("Select ChildId, PID, CID, Week, VDate, VType, Visit, temp as temp, Cough, CoughDt, DBrea, DBreaDt, Fever, FeverDt, OthCom1, OthCom2, OthCom3, Asses, RR1, RR2, Conv, FBrea, CInd, Leth, UCon, Drink, Vomit, None, LFever, MFever, HFever, Neck, Fonta, Conv2, Leth2, Ucon2, Drink2, Vomit2, CSPne, CPPne, CNPne, CLFever, CMFever, CHFever, CMenin, TSPne, TPPne, TNPne, TLFever, TMFever, THFever, TMenin, Ref, RSlip, Comp, Reason, TPlace, TPlaceC, TAbsIn, TAbsDur, Hos from " + TableName + "  Where ChildId='" + ChildId + "' and Week='" + Week + "' and VType='" + VType + "' and Visit='" + Visit + "'");
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                txtChildId.setText(cur.getString(cur.getColumnIndex("ChildId")));
                //txtCID.setText(cur.getString(cur.getColumnIndex("CID")));
                //txtPID.setText(cur.getString(cur.getColumnIndex("PID")));
                //txtWeek.setText(cur.getString(cur.getColumnIndex("Week")));
                txtWeek.setSelection(Global.SpinnerItemPosition(txtWeek, 3, cur.getString(cur.getColumnIndex("Week"))));
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
/*
                for (int i = 0; i < rdogrpVType.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpVType.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1)
                            .equalsIgnoreCase(
                                    cur.getString(cur.getColumnIndex("VType"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
*/
                //txtVisit.setText(cur.getString(cur.getColumnIndex("Visit")));
                txttemp.setText(cur.getString(cur.getColumnIndex("temp")));

                String a= cur.getString(cur.getColumnIndex("temp"));

                if(a.equals(""))
                {
                    chkTemp.setChecked(true);
                }

                else
                {
                    chkTemp.setChecked(false);
                }

                for (int i = 0; i < rdogrpCough.getChildCount(); i++)
                {
                    rb = (RadioButton) rdogrpCough.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Cough"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                dtpCoughDt.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("CoughDt"))));
                for (int i = 0; i < rdogrpDBrea.getChildCount(); i++)
                {
                    rb = (RadioButton) rdogrpDBrea.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("DBrea"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                dtpDBreaDt.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("DBreaDt"))));
                for (int i = 0; i < rdogrpFever.getChildCount(); i++)
                {
                    rb = (RadioButton) rdogrpFever.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Fever"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                dtpFeverDt.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("FeverDt"))));
                spnOthCom1.setSelection(Global.SpinnerItemPosition(spnOthCom1,2, cur.getString(cur.getColumnIndex("OthCom1"))));
                spnOthCom2.setSelection(Global.SpinnerItemPosition(spnOthCom2,2, cur.getString(cur.getColumnIndex("OthCom2"))));
                spnOthCom3.setSelection(Global.SpinnerItemPosition(spnOthCom3,2, cur.getString(cur.getColumnIndex("OthCom3"))));
                for (int i = 0; i < rdogrpAsses.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpAsses.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1)
                            .equalsIgnoreCase(
                                    cur.getString(cur.getColumnIndex("Asses"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                txtRR1.setText(cur.getString(cur.getColumnIndex("RR1")));

                String r= cur.getString(cur.getColumnIndex("RR1"));

                if(r.equals(""))
                {
                    chkRR.setChecked(true);
                }

                else
                {
                    chkRR.setChecked(false);
                }
                txtRR2.setText(cur.getString(cur.getColumnIndex("RR2")));
                for (int i = 0; i < rdogrpConv.getChildCount(); i++)
                {
                    rb = (RadioButton) rdogrpConv.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Conv"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpFBrea.getChildCount(); i++)
                {
                    rb = (RadioButton) rdogrpFBrea.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("FBrea"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpCInd.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpCInd.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("CInd"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpLeth.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpLeth.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Leth"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpUCon.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpUCon.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("UCon"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpDrink.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpDrink.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Drink"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpVomit.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpVomit.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Vomit"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpNone.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpNone.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("None"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpCSPne.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpCSPne.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("CSPne"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpCPPne.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpCPPne.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("CPPne"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpCNPne.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpCNPne.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("CNPne"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpTSPne.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpTSPne.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("TSPne"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpTPPne.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpTPPne.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("TPPne"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpTNPne.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpTNPne.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("TNPne"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpLFever.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpLFever.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("LFever"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpCLFever.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpCLFever.getChildAt(i);
                    if (Global
                            .Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("CLFever"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpTLFever.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpTLFever.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("TLFever"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpMFever.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpMFever.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("MFever"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpCMFever.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpCMFever.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("CMFever"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpTMFever.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpTMFever.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("TMFever"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpHFever.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpHFever.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("HFever"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpCHFever.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpCHFever.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("CHFever"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpTHFever.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpTHFever.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("THFever"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpNeck.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpNeck.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Neck"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpFonta.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpFonta.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Fonta"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpConv2.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpConv2.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Conv2"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpLeth2.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpLeth2.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Leth2"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpUcon2.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpUcon2.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Ucon2"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpDrink2.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpDrink2.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Drink2"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpVomit2.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpVomit2.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Vomit2"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpCMenin.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpCMenin.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("CMenin"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpTMenin.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpTMenin.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("TMenin"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpRef.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpRef.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Ref"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                txtRSlip.setText(cur.getString(cur.getColumnIndex("RSlip")));

                for (int i = 0; i < rdogrpComp.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpComp.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Comp"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                for (int i = 0; i < rdogrpReason.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpReason.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Reason"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                spnTPlace.setSelection(Global.SpinnerItemPosition(spnTPlace, 1,cur.getString(cur.getColumnIndex("TPlace"))));
                /*spnTPlaceC.setSelection(Global.SpinnerItemPosition(spnTPlaceC,
                        2, cur.getString(cur.getColumnIndex("TPlaceC"))));*/
                txtTPlaceC.setText(cur.getString(cur.getColumnIndex("TPlaceC")));
                for (int i = 0; i < rdogrpTAbsIn.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpTAbsIn.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("TAbsIn"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                txtTAbsDur.setText(cur.getString(cur.getColumnIndex("TAbsDur")));
                for (int i = 0; i < rdogrpHos.getChildCount(); i++) {
                    rb = (RadioButton) rdogrpHos.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Hos"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            Connection.MessageBox(AssPneu.this, e.getMessage());
            return;
        }
    }

    private void DataSearchName(String CID)
    {
        try
        {


            Cursor cur = C.ReadData("Select * from Child Where CID='"+ CID +"'");
            cur.moveToFirst();
            while(!cur.isAfterLast())
            {

                //txtPID.setText(cur.getString(cur.getColumnIndex("PID")));


                cur.moveToNext();
            }
            cur.close();
        }
        catch(Exception  e)
        {
            Connection.MessageBox(AssPneu.this, e.getMessage());
            return;
        }
    }


    protected Dialog onCreateDialog(int id) {
        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        switch (id) {
            case DATE_DIALOG:
                return new DatePickerDialog(this, mDateSetListener, g.mYear,
                        g.mMonth - 1, g.mDay);
            case TIME_DIALOG:
                return new TimePickerDialog(this, timePickerListener, hour, minute,
                        false);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear + 1;
            mDay = dayOfMonth;
            EditText dtpDate;

            dtpDate = (EditText) findViewById(R.id.dtpVDate);
            if (VariableID.equals("btnVDate")) {
                dtpDate = (EditText) findViewById(R.id.dtpVDate);
            } else if (VariableID.equals("btnCoughDt")) {
                dtpDate = (EditText) findViewById(R.id.dtpCoughDt);
            } else if (VariableID.equals("btnDBreaDt")) {
                dtpDate = (EditText) findViewById(R.id.dtpDBreaDt);
            } else if (VariableID.equals("btnFeverDt")) {
                dtpDate = (EditText) findViewById(R.id.dtpFeverDt);
            } /*else if (VariableID.equals("btnEnDt")) {
				dtpDate = (EditText) findViewById(R.id.dtpEnDt);
			} else if (VariableID.equals("btnUploadDT")) {
				dtpDate = (EditText) findViewById(R.id.dtpUploadDT);
			}*/
            dtpDate.setText(new StringBuilder()
                    .append(Global.Right("00" + mDay, 2)).append("/")
                    .append(Global.Right("00" + mMonth, 2)).append("/")
                    .append(mYear));
        }
    };
    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int selectedHour,
                              int selectedMinute) {
            hour = selectedHour;
            minute = selectedMinute;
            EditText tpTime;

            // tpTime.setText(new
            // StringBuilder().append(Global.Right("00"+hour,2)).append(":").append(Global.Right("00"+minute,2)));

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
        final Dialog dialog = new Dialog(AssPneu.this);
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
            Connection.MessageBox(AssPneu.this, ex.getMessage());
            return;
        }

    }

}

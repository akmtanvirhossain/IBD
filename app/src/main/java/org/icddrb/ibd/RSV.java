
package org.icddrb.ibd;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.app.*;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.view.KeyEvent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.MotionEvent;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
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
import android.widget.CompoundButton;
import android.graphics.Color;
import android.view.WindowManager;

import Utility.*;
import Common.*;

import android.widget.AutoCompleteTextView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

public class RSV extends Activity {
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

    boolean networkAvailable = false;
    Location currentLocation;
    double currentLatitude, currentLongitude;
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
    TextView lblHeading;
    LinearLayout seclbl1;
    View linelbl1;
    LinearLayout seclbl2;
    View linelbl2;
    LinearLayout secChildID;
    View lineChildID;
    TextView VlblChildID;
    EditText txtChildID;


    LinearLayout secCID;
    View lineCID;
    TextView VlblCID;
    EditText txtCID;
    LinearLayout secPID;
    View linePID;
    TextView VlblPID;
    EditText txtPID;
    LinearLayout secWeek;
    View lineWeek;
    TextView VlblWeek;
    EditText txtWeek;
    LinearLayout secVDate;
    View lineVDate;
    TextView VlblVDate;
    EditText dtpVDate;
    LinearLayout secVType;
    View lineVType;
    TextView VlblVType;
    RadioGroup rdogrpVType;
    RadioButton rdoVType1;
    RadioButton rdoVType2;
    RadioButton rdoVType3;
    LinearLayout secVisit;
    View lineVisit;
    TextView VlblVisit;
    EditText txtVisit;
    LinearLayout secSlNo;
    View lineSlNo;
    TextView VlblSlNo;
    EditText txtSlNo;
    LinearLayout secTemp;
    View lineTemp;
    TextView VlblTemp;
    EditText txtTemp;
    LinearLayout seclbl3;
    View linelbl3;
    //    30/07/2020
    LinearLayout secCough;
    View lineCough;
    TextView VlblCough;
    RadioGroup rdogrpCough;
    RadioButton rdoCough1;
    RadioButton rdoCough2;
    //
    LinearLayout secdtpCoughDt;
    View linedtpCoughDt;
    TextView VlbldtpCoughDt;
    EditText dtpdtpCoughDt;
    //    30/07/2020
    LinearLayout secDBrea;
    View lineDBrea;
    TextView VlblDBrea;
    RadioGroup rdogrpDBrea;
    RadioButton rdoDBrea1;
    RadioButton rdoDBrea2;
    //
    LinearLayout secdtpDBreaDt;
    View linedtpDBreaDt;
    TextView VlbldtpDBreaDt;
    EditText dtpdtpDBreaDt;
    //    30/20/2020
    LinearLayout secDeepCold;
    View lineDeepCold;
    TextView VlblDeepCold;
    RadioGroup rdogrpDeepCold;
    RadioButton rdoDeepCold1;
    RadioButton rdoDeepCold2;
    //
    LinearLayout secDeepColdDt;
    View lineDeepColdDt;
    TextView VlblDeepColdDt;
    EditText dtpDeepColdDt;
    LinearLayout secSoreThroatDt;
    //    30/07/2020
    LinearLayout secSoreThroat;
    View lineSoreThroat;
    TextView VlblSoreThroat;
    RadioGroup rdogrpSoreThroat;
    RadioButton rdoSoreThroat1;
    RadioButton rdoSoreThroat2;
    //
    View lineSoreThroatDt;
    TextView VlblSoreThroatDt;
    EditText dtpSoreThroatDt;
    LinearLayout secRSVsuitable;
    View lineRSVsuitable;
    TextView VlblRSVsuitable;
    RadioGroup rdogrpRSVsuitable;
    RadioButton rdoRSVsuitable1;
    RadioButton rdoRSVsuitable2;
    LinearLayout secSuitSam;
    View lineSuitSam;
    TextView VlblSuitSam;
    RadioGroup rdogrpSuitSam;
    RadioButton rdoSuitSam1;
    RadioButton rdoSuitSam2;
    LinearLayout secSuitSamRe;
    View lineSuitSamRe;
    TextView VlblSuitSamRe;
    RadioGroup rdogrpSuitSamRe;
    RadioButton rdoSuitSamRe1;
    RadioButton rdoSuitSamRe2;
    RadioButton rdoSuitSamRe3;
    RadioButton rdoSuitSamRe4;
    LinearLayout secSuitSamReO;
    View lineSuitSamReO;
    TextView VlblSuitSamReO;
    EditText txtSuitSamReO;
    LinearLayout secRSVlisted;
    View lineRSVlisted;
    TextView VlblRSVlisted;
    RadioGroup rdogrpRSVlisted;
    RadioButton rdoRSVlisted1;
    RadioButton rdoRSVlisted2;
    LinearLayout secRSVlistedDt;
    View lineRSVlistedDt;
    TextView VlblRSVlistedDt;
    EditText dtpRSVlistedDt;
    LinearLayout secReason;
    View lineReason;
    TextView VlblReason;
    EditText txtReason;
    LinearLayout secSampleAgree;
    View lineSampleAgree;
    TextView VlblSampleAgree;
    RadioGroup rdogrpSampleAgree;
    RadioButton rdoSampleAgree1;
    RadioButton rdoSampleAgree2;
    LinearLayout secNotAgree;
    View lineNotAgree;
    TextView VlblNotAgree;
    RadioGroup rdogrpNotAgree;
    RadioButton rdoNotAgree1;
    RadioButton rdoNotAgree2;
    RadioButton rdoNotAgree3;
    RadioButton rdoNotAgree4;
    RadioButton rdoNotAgree5;
    RadioButton rdoNotAgree6;
    RadioButton rdoNotAgree7;
    LinearLayout secOthersR;
    View lineOthersR;
    TextView VlblOthersR;
    AutoCompleteTextView txtOthersR;

    TextView txtFMName;
    TextView txtName;
    TextView Age;
    TextView Dob;

    LinearLayout secFever;
    RadioGroup rdogrpFever;
    RadioButton rdoFever1;
    RadioButton rdoFever2;
    RadioButton rdoFever3;
    RadioButton rdoFever4;
    LinearLayout secFeverDt;
    EditText dtpFeverDt;

//    String ChildID;

    static int MODULEID = 0;
    static int LANGUAGEID = 0;
    static String TableName;

    static String STARTTIME = "";
    static String DEVICEID = "";
    static String ENTRYUSER = "";
    MySharedPreferences sp;

    Bundle IDbundle;
    static String CHILDID = "";
    static String WEEK = "";
    static String VTYPE = "";
    static String VISITDate = "";

    static String BDATE = "";
    static String VISIT = "", FMName = "", CHName = "", CHID = "", PID = "", AgeDM = "", AgeD = "", AgeM = "", TEMP = "";
    static String Cough = "";
    static String CoughDate = "";
    static String DBrea = "";
    static String DBreaDate = "";
    static String SOURCE = "";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.rsv);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            C = new Connection(this);
            g = Global.getInstance();

            STARTTIME = g.CurrentTime24();
            DEVICEID = g.getUserId();
            ENTRYUSER = g.getUserId();

            IDbundle = getIntent().getExtras();
            CHILDID = IDbundle.getString("childid");
            CHID = IDbundle.getString("cid");
            PID = IDbundle.getString("pid");
            WEEK = IDbundle.getString("weekno");
            FMName = IDbundle.getString("fm");
            AgeD = IDbundle.getString("aged");
            AgeM = IDbundle.getString("agem");
            AgeDM = IDbundle.getString("agedm");
            BDATE = IDbundle.getString("bdate");
            CHName = IDbundle.getString("name");
            VTYPE = IDbundle.getString("visittype");
            VISIT = IDbundle.getString("visitno");
            VISITDate = IDbundle.getString("visitdate");

            TEMP = IDbundle.getString("temp");
            Cough = IDbundle.getString("Cough");
            CoughDate = IDbundle.getString("CoughDt");
            DBrea = IDbundle.getString("DBrea");
            DBreaDate = IDbundle.getString("DBreaDt");
            SOURCE = IDbundle.getString("source");


            TableName = "RSV";

//         MODULEID = 0;
//         LANGUAGEID = Integer.parseInt(sp.getValue(this, "languageid"));
            lblHeading = (TextView) findViewById(R.id.lblHeading);

            ImageButton cmdBack = (ImageButton) findViewById(R.id.cmdBack);
            cmdBack.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(RSV.this);
                    adb.setTitle("Close");
                    adb.setMessage("Do you want to close this form[Yes/No]?");
                    adb.setNegativeButton("No", null);
                    adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    adb.show();
                }
            });


            Initialization();
//        Connection.LocalizeLanguage(RSV.this, MODULEID, LANGUAGEID);


            dtpVDate.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        VariableID = "btnVDate";
                        showDialog(DATE_DIALOG);
                        return true;
                    }
                    return false;
                }
            });
            dtpdtpCoughDt.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        VariableID = "btndtpCoughDt";
                        showDialog(DATE_DIALOG);
                        return true;
                    }
                    return false;
                }
            });
            dtpdtpDBreaDt.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        VariableID = "btndtpDBreaDt";
                        showDialog(DATE_DIALOG);
                        return true;
                    }
                    return false;
                }
            });
            dtpDeepColdDt.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        VariableID = "btnDeepColdDt";
                        showDialog(DATE_DIALOG);
                        return true;
                    }
                    return false;
                }
            });
            dtpSoreThroatDt.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        VariableID = "btnSoreThroatDt";
                        showDialog(DATE_DIALOG);
                        return true;
                    }
                    return false;
                }
            });
            dtpRSVlistedDt.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        VariableID = "btnRSVlistedDt";
                        showDialog(DATE_DIALOG);
                        return true;
                    }
                    return false;
                }
            });
            dtpFeverDt.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        VariableID = "dtpFeverDt";
                        showDialog(DATE_DIALOG);
                        return true;
                    }
                    return false;
                }
            });

            //Hide all skip variables

            if (Cough.equals("1")) {
                rdoCough1.setChecked(true);
                rdoCough2.setEnabled(false);
                dtpdtpCoughDt.setText(IDbundle.getString("CoughDt"));
                dtpdtpCoughDt.setEnabled(false);
            } else if (Cough.equals("2")) {
                rdoCough2.setChecked(true);
                rdoCough1.setEnabled(false);
            }

            if (DBrea.equals("1")) {
                rdoDBrea1.setChecked(true);
                rdoDBrea2.setEnabled(false);
                dtpdtpDBreaDt.setText(IDbundle.getString("DBreaDt"));
                dtpdtpDBreaDt.setEnabled(false);
            } else if (DBrea.equals("2")) {
                rdoDBrea2.setChecked(true);
                rdoDBrea1.setEnabled(false);
            }

            secFeverDt.setVisibility(View.GONE);

            secSuitSam.setVisibility(View.GONE);
            lineSuitSam.setVisibility(View.GONE);
            secSuitSamRe.setVisibility(View.GONE);
            lineSuitSamRe.setVisibility(View.GONE);
            secSuitSamReO.setVisibility(View.GONE);
            lineSuitSamReO.setVisibility(View.GONE);
            secSampleAgree.setVisibility(View.GONE);
            lineSampleAgree.setVisibility(View.GONE);
            secNotAgree.setVisibility(View.GONE);
            lineNotAgree.setVisibility(View.GONE);
            secOthersR.setVisibility(View.GONE);
            lineOthersR.setVisibility(View.GONE);

                DataSearch(CHILDID, WEEK, VTYPE, VISIT);


            Button cmdSave = (Button) findViewById(R.id.cmdSave);
            cmdSave.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    DataSave();
                }
            });
        } catch (Exception e) {
            Connection.MessageBox(RSV.this, e.getMessage());
            return;
        }
    }

    private void Initialization() {
        try {
            secFever=(LinearLayout) findViewById(R.id.secFever) ;
            rdogrpFever=(RadioGroup) findViewById(R.id.rdogrpFever) ;
            rdoFever1=(RadioButton)findViewById(R.id.rdoFever1) ;
            rdoFever2=(RadioButton)findViewById(R.id.rdoFever2) ;
            rdoFever3=(RadioButton)findViewById(R.id.rdoFever3) ;
            rdoFever4=(RadioButton)findViewById(R.id.rdoFever4) ;
            secFeverDt=(LinearLayout)findViewById(R.id.secFeverDt) ;
            dtpFeverDt=(EditText)findViewById(R.id.dtpFeverDt) ;

//         String CDOB;
//         CDOB=C.ReturnSingleValue("Select BDate  from Child WHERE   ChildID = '"+ ChildID +"'");
//
//         int d = Global.DateDifferenceDays(dtpVDate.getText().toString(),Global.DateConvertDMY(CDOB));
//         int m = (int)(d/30.44);
//         ------------------------------------------------

            txtName = (TextView) findViewById(R.id.txtName);
            txtName.setText(CHName);
            txtName.setEnabled(false);
            txtFMName = (TextView) findViewById(R.id.txtFMName);
            txtFMName.setText(FMName);
            Age = (TextView) findViewById(R.id.Age);
//            Age.setText(": " + (AgeDM));
            Age.setText(AgeDM);
            Dob = (TextView) findViewById(R.id.Dob);
            Dob.setText(": " + Global.DateConvertDMY(BDATE));


//         ------------------------------------------------
            seclbl1 = (LinearLayout) findViewById(R.id.seclbl1);
            linelbl1 = (View) findViewById(R.id.linelbl1);
            seclbl2 = (LinearLayout) findViewById(R.id.seclbl2);
            linelbl2 = (View) findViewById(R.id.linelbl2);
            secChildID = (LinearLayout) findViewById(R.id.secChildID);
            lineChildID = (View) findViewById(R.id.lineChildID);
            VlblChildID = (TextView) findViewById(R.id.VlblChildID);
            txtChildID = (EditText) findViewById(R.id.txtChildID);
            txtChildID.setText(CHILDID);
            txtChildID.setEnabled(false);


            secCID = (LinearLayout) findViewById(R.id.secCID);
            lineCID = (View) findViewById(R.id.lineCID);
            VlblCID = (TextView) findViewById(R.id.VlblCID);
            txtCID = (EditText) findViewById(R.id.txtCID);
            txtCID.setText(CHID);
            txtCID.setEnabled(false);

            secPID = (LinearLayout) findViewById(R.id.secPID);
//         linePID=(View)findViewById(R.id.linePID);
            VlblPID = (TextView) findViewById(R.id.VlblPID);
            txtPID = (EditText) findViewById(R.id.txtPID);
            txtPID.setText(PID);
            txtPID.setEnabled(false);
            secWeek = (LinearLayout) findViewById(R.id.secWeek);
            lineWeek = (View) findViewById(R.id.lineWeek);
            VlblWeek = (TextView) findViewById(R.id.VlblWeek);
            txtWeek = (EditText) findViewById(R.id.txtWeek);
            txtWeek.setText(WEEK);
            txtWeek.setEnabled(false);
            secVDate = (LinearLayout) findViewById(R.id.secVDate);
            lineVDate = (View) findViewById(R.id.lineVDate);
            VlblVDate = (TextView) findViewById(R.id.VlblVDate);
            dtpVDate = (EditText) findViewById(R.id.dtpVDate);
            dtpVDate.setEnabled(false);

            if (IDbundle.getString("visitdate") == null) {
                dtpVDate.setText(Global.DateNowDMY());
            } else {
                if (IDbundle.getString("visitdate").toString().length() == 0)
                    dtpVDate.setText(Global.DateNowDMY());
                else
                    dtpVDate.setText(IDbundle.getString("visitdate"));
            }

            secVType = (LinearLayout) findViewById(R.id.secVType);
            lineVType = (View) findViewById(R.id.lineVType);
            VlblVType = (TextView) findViewById(R.id.VlblVType);
            rdogrpVType = (RadioGroup) findViewById(R.id.rdogrpVType);
            rdoVType1 = (RadioButton) findViewById(R.id.rdoVType1);
            rdoVType2 = (RadioButton) findViewById(R.id.rdoVType2);
            rdoVType3 = (RadioButton) findViewById(R.id.rdoVType3);


            if (VTYPE.equals("1")) {
                rdoVType1.setChecked(true);
                rdoVType2.setEnabled(false);
                rdoVType3.setEnabled(false);
            } else if (VTYPE.equals("2")) {
                rdoVType2.setChecked(true);
                rdoVType1.setEnabled(false);
                rdoVType3.setEnabled(false);
            } else if (VTYPE.equals("3")) {
                rdoVType3.setChecked(true);
                rdoVType1.setEnabled(false);
                rdoVType2.setEnabled(false);
            }


            secVisit = (LinearLayout) findViewById(R.id.secVisit);
            lineVisit = (View) findViewById(R.id.lineVisit);
            VlblVisit = (TextView) findViewById(R.id.VlblVisit);
            txtVisit = (EditText) findViewById(R.id.txtVisit);
            txtVisit.setText(VISIT);
            txtVisit.setEnabled(false);
            secSlNo = (LinearLayout) findViewById(R.id.secSlNo);
            lineSlNo = (View) findViewById(R.id.lineSlNo);
            VlblSlNo = (TextView) findViewById(R.id.VlblSlNo);
            txtSlNo = (EditText) findViewById(R.id.txtSlNo);

         txtSlNo.setText(SlNumber(CHILDID,WEEK,VTYPE,VISIT));
         txtSlNo.setEnabled(false);

            secTemp = (LinearLayout) findViewById(R.id.secTemp);
            lineTemp = (View) findViewById(R.id.lineTemp);
            VlblTemp = (TextView) findViewById(R.id.VlblTemp);
            txtTemp = (EditText) findViewById(R.id.txtTemp);
            txtTemp.setText(TEMP);
            txtTemp.setEnabled(false);
            seclbl3 = (LinearLayout) findViewById(R.id.seclbl3);
            linelbl3 = (View) findViewById(R.id.linelbl3);

            secCough = (LinearLayout) findViewById(R.id.secCough);
            lineCough = (View) findViewById(R.id.lineCough);
            VlblCough = (TextView) findViewById(R.id.VlblCough);
            rdogrpCough = (RadioGroup) findViewById(R.id.rdogrpCough);
            rdoCough1 = (RadioButton) findViewById(R.id.rdoCough1);
            rdoCough2 = (RadioButton) findViewById(R.id.rdoCough2);



            rdogrpCough.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int radioButtonID) {
                    String rbData = "";
                    RadioButton rb;
                    String[] d_rdogrpCough = new String[]{"1", "2"};
                    for (int i = 0; i < rdogrpCough.getChildCount(); i++) {
                        rb = (RadioButton) rdogrpCough.getChildAt(i);
                        if (rb.isChecked()) rbData = d_rdogrpCough[i];
                    }

                    if (rbData.equalsIgnoreCase("2")) {
                        secdtpCoughDt.setVisibility(View.GONE);
                        linedtpCoughDt.setVisibility(View.GONE);
                        dtpdtpCoughDt.setText("");
                    } else {
                        secdtpCoughDt.setVisibility(View.VISIBLE);
                        linedtpCoughDt.setVisibility(View.VISIBLE);

                    }
                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                    return;
                }
            });

            secdtpCoughDt = (LinearLayout) findViewById(R.id.secdtpCoughDt);
            linedtpCoughDt = (View) findViewById(R.id.linedtpCoughDt);
            VlbldtpCoughDt = (TextView) findViewById(R.id.VlbldtpCoughDt);
            dtpdtpCoughDt = (EditText) findViewById(R.id.dtpdtpCoughDt);
//            dtpdtpCoughDt.setEnabled(false);
//            if (IDbundle.getString("CoughDt") == null) {
//                dtpdtpCoughDt.setText(Global.DateNowDMY());
//            } else {
//                dtpdtpCoughDt.setText(IDbundle.getString("CoughDt"));
//                dtpdtpCoughDt.setEnabled(false);


//                if (IDbundle.getString("CoughDt").toString().length() == 0)
//                    dtpdtpCoughDt.setText(Global.DateNowDMY());
//                else
//                    dtpdtpCoughDt.setText(IDbundle.getString("CoughDt"));
//            }

            secDBrea = (LinearLayout) findViewById(R.id.secDBrea);
            lineDBrea = (View) findViewById(R.id.lineDBrea);
            VlblDBrea = (TextView) findViewById(R.id.VlblDBrea);
            rdogrpDBrea = (RadioGroup) findViewById(R.id.rdogrpDBrea);
            rdoDBrea1 = (RadioButton) findViewById(R.id.rdoDBrea1);
            rdoDBrea2 = (RadioButton) findViewById(R.id.rdoDBrea2);

            rdogrpDBrea.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int radioButtonID) {
                    String rbData = "";
                    RadioButton rb;
                    String[] d_rdogrpDBrea = new String[]{"1", "2"};
                    for (int i = 0; i < rdogrpDBrea.getChildCount(); i++) {
                        rb = (RadioButton) rdogrpDBrea.getChildAt(i);
                        if (rb.isChecked()) rbData = d_rdogrpDBrea[i];
                    }

                    if (rbData.equalsIgnoreCase("2")) {
                        secdtpDBreaDt.setVisibility(View.GONE);
                        linedtpDBreaDt.setVisibility(View.GONE);
                        dtpdtpDBreaDt.setText("");

                    } else {
                        secdtpDBreaDt.setVisibility(View.VISIBLE);
                        linedtpDBreaDt.setVisibility(View.VISIBLE);
                    }
                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                    return;
                }
            });

            secdtpDBreaDt = (LinearLayout) findViewById(R.id.secdtpDBreaDt);
            linedtpDBreaDt = (View) findViewById(R.id.linedtpDBreaDt);
            VlbldtpDBreaDt = (TextView) findViewById(R.id.VlbldtpDBreaDt);
            dtpdtpDBreaDt = (EditText) findViewById(R.id.dtpdtpDBreaDt);
//            dtpdtpDBreaDt.setEnabled(false);

//            if (IDbundle.getString("DBreaDt") == null) {
//                dtpdtpDBreaDt.setText(Global.DateNowDMY());
//            } else {
//                dtpdtpDBreaDt.setText(IDbundle.getString("DBreaDt"));
//                dtpdtpDBreaDt.setEnabled(false);
////                if (IDbundle.getString("DBreaDt").toString().length() == 0)
////                    dtpdtpDBreaDt.setText(Global.DateNowDMY());
////                else
////                    dtpdtpDBreaDt.setText(IDbundle.getString("DBreaDt"));
//            }

            secDeepCold = (LinearLayout) findViewById(R.id.secDeepCold);
            lineDeepCold = (View) findViewById(R.id.lineDeepCold);
            VlblDeepCold = (TextView) findViewById(R.id.VlblDeepCold);
            rdogrpDeepCold = (RadioGroup) findViewById(R.id.rdogrpDeepCold);
            rdoDeepCold1 = (RadioButton) findViewById(R.id.rdoDeepCold1);
            rdoDeepCold2 = (RadioButton) findViewById(R.id.rdoDeepCold2);
            rdogrpDeepCold.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int radioButtonID) {
                    String rbData = "";
                    RadioButton rb;
                    String[] d_rdogrpDeepCold = new String[]{"1", "2"};
                    for (int i = 0; i < rdogrpDeepCold.getChildCount(); i++) {
                        rb = (RadioButton) rdogrpDeepCold.getChildAt(i);
                        if (rb.isChecked()) rbData = d_rdogrpDeepCold[i];
                    }

                    if (rbData.equalsIgnoreCase("2")) {
                        secDeepColdDt.setVisibility(View.GONE);
                        lineDeepColdDt.setVisibility(View.GONE);
                        dtpDeepColdDt.setText("");
                    } else {
                        secDeepColdDt.setVisibility(View.VISIBLE);
                        lineDeepColdDt.setVisibility(View.VISIBLE);
                    }
                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                    return;
                }
            });

            secDeepColdDt = (LinearLayout) findViewById(R.id.secDeepColdDt);
            lineDeepColdDt = (View) findViewById(R.id.lineDeepColdDt);
            VlblDeepColdDt = (TextView) findViewById(R.id.VlblDeepColdDt);
            dtpDeepColdDt = (EditText) findViewById(R.id.dtpDeepColdDt);

            secSoreThroat = (LinearLayout) findViewById(R.id.secSoreThroat);
            lineSoreThroat = (View) findViewById(R.id.lineSoreThroat);
            VlblSoreThroat = (TextView) findViewById(R.id.VlblSoreThroat);
            rdogrpSoreThroat = (RadioGroup) findViewById(R.id.rdogrpSoreThroat);
            rdoSoreThroat1 = (RadioButton) findViewById(R.id.rdoSoreThroat1);
            rdoSoreThroat2 = (RadioButton) findViewById(R.id.rdoSoreThroat2);
            rdogrpSoreThroat.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int radioButtonID) {
                    String rbData = "";
                    RadioButton rb;
                    String[] d_rdogrpSoreThroat = new String[]{"1", "2"};
                    for (int i = 0; i < rdogrpSoreThroat.getChildCount(); i++) {
                        rb = (RadioButton) rdogrpSoreThroat.getChildAt(i);
                        if (rb.isChecked()) rbData = d_rdogrpSoreThroat[i];
                    }

                    if (rbData.equalsIgnoreCase("2")) {
                        secSoreThroatDt.setVisibility(View.GONE);
                        lineSoreThroatDt.setVisibility(View.GONE);
                        dtpSoreThroatDt.setText("");
                    } else {
                        secSoreThroatDt.setVisibility(View.VISIBLE);
                        lineSoreThroatDt.setVisibility(View.VISIBLE);
                    }
                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                    return;
                }
            });

            secSoreThroatDt = (LinearLayout) findViewById(R.id.secSoreThroatDt);
            lineSoreThroatDt = (View) findViewById(R.id.lineSoreThroatDt);
            VlblSoreThroatDt = (TextView) findViewById(R.id.VlblSoreThroatDt);
            dtpSoreThroatDt = (EditText) findViewById(R.id.dtpSoreThroatDt);
            secRSVsuitable = (LinearLayout) findViewById(R.id.secRSVsuitable);
            lineRSVsuitable = (View) findViewById(R.id.lineRSVsuitable);
            VlblRSVsuitable = (TextView) findViewById(R.id.VlblRSVsuitable);
            rdogrpRSVsuitable = (RadioGroup) findViewById(R.id.rdogrpRSVsuitable);
            rdoRSVsuitable1 = (RadioButton) findViewById(R.id.rdoRSVsuitable1);
            rdoRSVsuitable2 = (RadioButton) findViewById(R.id.rdoRSVsuitable2);
            rdogrpRSVsuitable.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(RadioGroup radioGroup,int radioButtonID) {
                    String rbData = "";
                    RadioButton rb;
                    String[] d_rdogrpRSVsuitable = new String[] {"1","2"};
                    for (int i = 0; i < rdogrpRSVsuitable.getChildCount(); i++)
                    {
                        rb = (RadioButton)rdogrpRSVsuitable.getChildAt(i);
                        if (rb.isChecked()) rbData = d_rdogrpRSVsuitable[i];
                    }

                    if(rbData.equalsIgnoreCase("2"))
                    {
//                        secRSVlisted.setVisibility(View.GONE);
//                        lineRSVlisted.setVisibility(View.GONE);
//                        rdogrpRSVlisted.clearCheck();
//                        secRSVlistedDt.setVisibility(View.GONE);
//                        lineRSVlistedDt.setVisibility(View.GONE);
//                        dtpRSVlistedDt.setText("");
//                        secReason.setVisibility(View.GONE);
//                        lineReason.setVisibility(View.GONE);
//                        txtReason.setText("");
                        secSuitSam.setVisibility(View.GONE);
                        lineSuitSam.setVisibility(View.GONE);
                        rdogrpSuitSam.clearCheck();
                        secSuitSamRe.setVisibility(View.GONE);
                        lineSuitSamRe.setVisibility(View.GONE);
                        rdogrpSuitSamRe.clearCheck();
                        secSuitSamReO.setVisibility(View.GONE);
                        lineSuitSamReO.setVisibility(View.GONE);
                        txtSuitSamReO.setText("");
                        secSampleAgree.setVisibility(View.GONE);
                        lineSampleAgree.setVisibility(View.GONE);
                        rdogrpSampleAgree.clearCheck();
                        secNotAgree.setVisibility(View.GONE);
                        lineNotAgree.setVisibility(View.GONE);
                        rdogrpNotAgree.clearCheck();
                        secOthersR.setVisibility(View.GONE);
                        lineOthersR.setVisibility(View.GONE);
                        txtOthersR.setText("");
                    }
                    else
                    {
//                        secRSVlisted.setVisibility(View.VISIBLE);
//                        lineRSVlisted.setVisibility(View.VISIBLE);
//                        secReason.setVisibility(View.VISIBLE);
//                        lineReason.setVisibility(View.VISIBLE);
                        secSuitSam.setVisibility(View.VISIBLE);
                        lineSuitSam.setVisibility(View.VISIBLE);
//                        secSuitSamRe.setVisibility(View.VISIBLE);
//                        lineSuitSamRe.setVisibility(View.VISIBLE);
//                        secSuitSamReO.setVisibility(View.VISIBLE);
//                        lineSuitSamReO.setVisibility(View.VISIBLE);
                        secSampleAgree.setVisibility(View.VISIBLE);
                        lineSampleAgree.setVisibility(View.VISIBLE);
//                        secNotAgree.setVisibility(View.VISIBLE);
//                        lineNotAgree.setVisibility(View.VISIBLE);
                    }
                }
                public void onNothingSelected(AdapterView<?> adapterView) {
                    return;
                }
            });
            secRSVlisted = (LinearLayout) findViewById(R.id.secRSVlisted);
            lineRSVlisted = (View) findViewById(R.id.lineRSVlisted);
            VlblRSVlisted = (TextView) findViewById(R.id.VlblRSVlisted);
            rdogrpRSVlisted = (RadioGroup) findViewById(R.id.rdogrpRSVlisted);
            rdoRSVlisted1 = (RadioButton) findViewById(R.id.rdoRSVlisted1);
            rdoRSVlisted2 = (RadioButton) findViewById(R.id.rdoRSVlisted2);
            rdogrpRSVlisted.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(RadioGroup radioGroup,int radioButtonID) {
                    String rbData = "";
                    RadioButton rb;
                    String[] d_rdogrpRSVlisted = new String[] {"1","2"};
                    for (int i = 0; i < rdogrpRSVlisted.getChildCount(); i++)
                    {
                        rb = (RadioButton)rdogrpRSVlisted.getChildAt(i);
                        if (rb.isChecked()) rbData = d_rdogrpRSVlisted[i];
                    }

                    if(rbData.equalsIgnoreCase("2"))
                    {
                        secRSVlistedDt.setVisibility(View.GONE);
                        lineRSVlistedDt.setVisibility(View.GONE);
                        dtpRSVlistedDt.setText("");
                        secReason.setVisibility(View.VISIBLE);
                        lineReason.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        secRSVlistedDt.setVisibility(View.VISIBLE);
                        lineRSVlistedDt.setVisibility(View.VISIBLE);
                        secReason.setVisibility(View.GONE);
                        lineReason.setVisibility(View.GONE);
                        txtReason.setText("");
                    }
                }
                public void onNothingSelected(AdapterView<?> adapterView) {
                    return;
                }
            });
            secRSVlistedDt = (LinearLayout) findViewById(R.id.secRSVlistedDt);
            lineRSVlistedDt = (View) findViewById(R.id.lineRSVlistedDt);
            VlblRSVlistedDt = (TextView) findViewById(R.id.VlblRSVlistedDt);
            dtpRSVlistedDt = (EditText) findViewById(R.id.dtpRSVlistedDt);
            secReason = (LinearLayout) findViewById(R.id.secReason);
            lineReason = (View) findViewById(R.id.lineReason);
            VlblReason = (TextView) findViewById(R.id.VlblReason);
            txtReason = (EditText) findViewById(R.id.txtReason);


            rdogrpFever.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int radioButtonID) {
                    String rbData = "";
                    RadioButton rb;
                    String[] d_rdogrpFever = new String[]{"1","2","3","4"};
                    for (int i = 0; i < rdogrpFever.getChildCount(); i++) {
                        rb = (RadioButton) rdogrpFever.getChildAt(i);
                        if (rb.isChecked()) rbData = d_rdogrpFever[i];
                    }

                    if (rbData.equalsIgnoreCase("1")|rbData.equalsIgnoreCase("2")|rbData.equalsIgnoreCase("3")) {
                        secFeverDt.setVisibility(View.VISIBLE);
                    } else {
                        secFeverDt.setVisibility(View.GONE);
                    }
                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                    return;
                }
            });
            secSuitSam=(LinearLayout)findViewById(R.id.secSuitSam);
            lineSuitSam=(View)findViewById(R.id.lineSuitSam);
            VlblSuitSam = (TextView) findViewById(R.id.VlblSuitSam);
            rdogrpSuitSam = (RadioGroup) findViewById(R.id.rdogrpSuitSam);
            rdoSuitSam1 = (RadioButton) findViewById(R.id.rdoSuitSam1);
            rdoSuitSam2 = (RadioButton) findViewById(R.id.rdoSuitSam2);
            rdogrpSuitSam.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(RadioGroup radioGroup,int radioButtonID) {
                    String rbData = "";
                    RadioButton rb;
                    String[] d_rdogrpSuitSam = new String[] {"1","2"};
                    for (int i = 0; i < rdogrpSuitSam.getChildCount(); i++)
                    {
                        rb = (RadioButton)rdogrpSuitSam.getChildAt(i);
                        if (rb.isChecked()) rbData = d_rdogrpSuitSam[i];
                    }

                    if(rbData.equalsIgnoreCase("1"))
                    {
                        secSuitSamRe.setVisibility(View.GONE);
                        lineSuitSamRe.setVisibility(View.GONE);
                        rdogrpSuitSamRe.clearCheck();
                        secSuitSamReO.setVisibility(View.GONE);
                        lineSuitSamReO.setVisibility(View.GONE);
                        txtSuitSamReO.setText("");
                        secSampleAgree.setVisibility(View.VISIBLE);
                        lineSampleAgree.setVisibility(View.VISIBLE);
                        rdogrpSampleAgree.clearCheck();
                        secNotAgree.setVisibility(View.GONE);
                        lineNotAgree.setVisibility(View.GONE);
                        rdogrpNotAgree.clearCheck();
                        secOthersR.setVisibility(View.GONE);
                        lineOthersR.setVisibility(View.GONE);
                        txtOthersR.setText("");

                        //******************* Sample Date Check
//                        String count=C.ReturnSingleValue("SELECT cast((ifnull(cast((julianday(date('now'))-julianday(date(VDate))) as int),0)) as int)Diff  FROM RSVSample where Status='1'");
//                        if(Integer.parseInt(count)>0 && Integer.parseInt(count)<15){
//                            Connection.MessageBox(RSV.this,"Last Sample collect diff=2");
//                        }
                        //******************* Sample Date Check Shahidul
////                        String Day14;
////                        Day14=C.ReturnSingleValue("SELECT cast((ifnull(cast((julianday(date(e)max(VDat))-julianday(date('"+Global.DateConvertYMD(dtpVDate.getText().toString())+"'))) as int),0)) as int)Diff  FROM RSVSample WHERE   ChildId = '"+ txtChildID.getText().toString() +"' and Status='1'");
//                        String Day14=C.ReturnSingleValue("SELECT date(max(VDate)) FROM RSVSample WHERE   ChildId = '"+ txtChildID.getText().toString() +"' and Status='1'");
//                        if (Day14 != null) {
//                            int dy1 = Global.DateDifferenceDays(dtpVDate.getText().toString(), Global.DateConvertDMY(Day14));
//                            if (dy1 >= 0 & dy1 <= 14) {
//                                Connection.MessageBox(RSV.this, "শেষ নমুনা সংগ্রহের তারিখ (" + Day14 + ") হইতে আজকের তারিখের (পার্থক্য : " + dy1 + " দিন)। (সুতরাং ১৪ দিনের কম সময়ের মধ্যে আবার স্যাম্পল দেওয়ার জন্য উপযুক্ত হতে পারে না)");
//                            }
//                        }

//                        String Day60;
                        String Day60=C.ReturnSingleValue("SELECT date(max(VDate)) FROM RSVSample WHERE   ChildId = '"+ txtChildID.getText().toString() +"' and Status='1'");
                        if (Day60 != null) {
                            int dy1 = Global.DateDifferenceDays(dtpVDate.getText().toString(), Global.DateConvertDMY(Day60));
                            if (dy1 >= 0 & dy1 <= 60) {
                                Connection.MessageBox(RSV.this, "শেষ নমুনা সংগ্রহের তারিখ (" + Day60 + ") হইতে আজকের তারিখের (পার্থক্য : " + dy1 + " দিন)। (সুতরাং ৬০ দিনের কম সময়ের মধ্যে আবার স্যাম্পল দেওয়ার জন্য উপযুক্ত হতে পারে না)");
                            }
                        }

                        //******************* Sample Date Check
                    }
                    else
                    {
                        secSuitSamRe.setVisibility(View.VISIBLE);
                        lineSuitSamRe.setVisibility(View.VISIBLE);
                        secSampleAgree.setVisibility(View.GONE);
                        lineSampleAgree.setVisibility(View.GONE);
                        rdogrpSampleAgree.clearCheck();
                        secNotAgree.setVisibility(View.GONE);
                        lineNotAgree.setVisibility(View.GONE);
                        rdogrpNotAgree.clearCheck();
                        secOthersR.setVisibility(View.GONE);
                        lineOthersR.setVisibility(View.GONE);
                        txtOthersR.setText("");
                    }
                }
                public void onNothingSelected(AdapterView<?> adapterView) {
                    return;
                }
            });
            secSuitSamRe=(LinearLayout)findViewById(R.id.secSuitSamRe);
            lineSuitSamRe=(View)findViewById(R.id.lineSuitSamRe);
            VlblSuitSamRe = (TextView) findViewById(R.id.VlblSuitSamRe);
            rdogrpSuitSamRe = (RadioGroup) findViewById(R.id.rdogrpSuitSamRe);
            rdoSuitSamRe1 = (RadioButton) findViewById(R.id.rdoSuitSamRe1);
            rdoSuitSamRe2 = (RadioButton) findViewById(R.id.rdoSuitSamRe2);
            rdoSuitSamRe3 = (RadioButton) findViewById(R.id.rdoSuitSamRe3);
            rdoSuitSamRe4 = (RadioButton) findViewById(R.id.rdoSuitSamRe4);
            rdogrpSuitSamRe.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(RadioGroup radioGroup,int radioButtonID) {
                    String rbData = "";
                    RadioButton rb;
                    String[] d_rdogrpSuitSamRe = new String[] {"1","2","3","7"};
                    for (int i = 0; i < rdogrpSuitSamRe.getChildCount(); i++)
                    {
                        rb = (RadioButton)rdogrpSuitSamRe.getChildAt(i);
                        if (rb.isChecked()) rbData = d_rdogrpSuitSamRe[i];
                    }

                    if(rbData.equalsIgnoreCase("1"))
                    {
                        secSuitSamReO.setVisibility(View.GONE);
                        lineSuitSamReO.setVisibility(View.GONE);
                        txtSuitSamReO.setText("");
                        secSampleAgree.setVisibility(View.GONE);
                        lineSampleAgree.setVisibility(View.GONE);
                        rdogrpSampleAgree.clearCheck();
                        secNotAgree.setVisibility(View.GONE);
                        lineNotAgree.setVisibility(View.GONE);
                        rdogrpNotAgree.clearCheck();
                        secOthersR.setVisibility(View.GONE);
                        lineOthersR.setVisibility(View.GONE);
                        txtOthersR.setText("");
                    }
                    else if(rbData.equalsIgnoreCase("2"))
                    {
                        secSuitSamReO.setVisibility(View.GONE);
                        lineSuitSamReO.setVisibility(View.GONE);
                        txtSuitSamReO.setText("");
                        secSampleAgree.setVisibility(View.GONE);
                        lineSampleAgree.setVisibility(View.GONE);
                        rdogrpSampleAgree.clearCheck();
                        secNotAgree.setVisibility(View.GONE);
                        lineNotAgree.setVisibility(View.GONE);
                        rdogrpNotAgree.clearCheck();
                        secOthersR.setVisibility(View.GONE);
                        lineOthersR.setVisibility(View.GONE);
                        txtOthersR.setText("");
                    }
                    else if(rbData.equalsIgnoreCase("3"))
                    {
                        secSuitSamReO.setVisibility(View.GONE);
                        lineSuitSamReO.setVisibility(View.GONE);
                        txtSuitSamReO.setText("");
                        secSampleAgree.setVisibility(View.GONE);
                        lineSampleAgree.setVisibility(View.GONE);
                        rdogrpSampleAgree.clearCheck();
                        secNotAgree.setVisibility(View.GONE);
                        lineNotAgree.setVisibility(View.GONE);
                        rdogrpNotAgree.clearCheck();
                        secOthersR.setVisibility(View.GONE);
                        lineOthersR.setVisibility(View.GONE);
                        txtOthersR.setText("");
                    }
                    else
                    {
                        secSuitSamReO.setVisibility(View.VISIBLE);
                        lineSuitSamReO.setVisibility(View.VISIBLE);
                        secSampleAgree.setVisibility(View.GONE);
                        lineSampleAgree.setVisibility(View.GONE);
                        secNotAgree.setVisibility(View.GONE);
                        lineNotAgree.setVisibility(View.GONE);
                    }
                }
                public void onNothingSelected(AdapterView<?> adapterView) {
                    return;
                }
            });
            secSuitSamReO=(LinearLayout)findViewById(R.id.secSuitSamReO);
            lineSuitSamReO=(View)findViewById(R.id.lineSuitSamReO);
            VlblSuitSamReO=(TextView) findViewById(R.id.VlblSuitSamReO);
            txtSuitSamReO=(EditText) findViewById(R.id.txtSuitSamReO);
            secSampleAgree=(LinearLayout)findViewById(R.id.secSampleAgree);
            lineSampleAgree=(View)findViewById(R.id.lineSampleAgree);
            VlblSampleAgree = (TextView) findViewById(R.id.VlblSampleAgree);
            rdogrpSampleAgree = (RadioGroup) findViewById(R.id.rdogrpSampleAgree);
            rdoSampleAgree1 = (RadioButton) findViewById(R.id.rdoSampleAgree1);
            rdoSampleAgree2 = (RadioButton) findViewById(R.id.rdoSampleAgree2);
            rdogrpSampleAgree.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(RadioGroup radioGroup,int radioButtonID) {
                    String rbData = "";
                    RadioButton rb;
                    String[] d_rdogrpSampleAgree = new String[] {"1","2"};
                    for (int i = 0; i < rdogrpSampleAgree.getChildCount(); i++)
                    {
                        rb = (RadioButton)rdogrpSampleAgree.getChildAt(i);
                        if (rb.isChecked()) rbData = d_rdogrpSampleAgree[i];
                    }

                    if(rbData.equalsIgnoreCase("1"))
                    {
                        secNotAgree.setVisibility(View.GONE);
                        lineNotAgree.setVisibility(View.GONE);
                        rdogrpNotAgree.clearCheck();
                        secOthersR.setVisibility(View.GONE);
                        lineOthersR.setVisibility(View.GONE);
                        txtOthersR.setText("");
                    }
                    else
                    {
                        secNotAgree.setVisibility(View.VISIBLE);
                        lineNotAgree.setVisibility(View.VISIBLE);
                    }
                }
                public void onNothingSelected(AdapterView<?> adapterView) {
                    return;
                }
            });
            secNotAgree=(LinearLayout)findViewById(R.id.secNotAgree);
            lineNotAgree=(View)findViewById(R.id.lineNotAgree);
            VlblNotAgree = (TextView) findViewById(R.id.VlblNotAgree);
            rdogrpNotAgree = (RadioGroup) findViewById(R.id.rdogrpNotAgree);
            rdoNotAgree1 = (RadioButton) findViewById(R.id.rdoNotAgree1);
            rdoNotAgree2 = (RadioButton) findViewById(R.id.rdoNotAgree2);
            rdoNotAgree3 = (RadioButton) findViewById(R.id.rdoNotAgree3);
            rdoNotAgree4 = (RadioButton) findViewById(R.id.rdoNotAgree4);
            rdoNotAgree5 = (RadioButton) findViewById(R.id.rdoNotAgree5);
            rdoNotAgree6 = (RadioButton) findViewById(R.id.rdoNotAgree6);
            rdoNotAgree7 = (RadioButton) findViewById(R.id.rdoNotAgree7);
            rdogrpNotAgree.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(RadioGroup radioGroup,int radioButtonID) {
                    String rbData = "";
                    RadioButton rb;
                    String[] d_rdogrpNotAgree = new String[] {"1","2","3","4","5","6","7"};
                    for (int i = 0; i < rdogrpNotAgree.getChildCount(); i++)
                    {
                        rb = (RadioButton)rdogrpNotAgree.getChildAt(i);
                        if (rb.isChecked()) rbData = d_rdogrpNotAgree[i];
                    }

                    if(rbData.equalsIgnoreCase("1"))
                    {
                        secOthersR.setVisibility(View.GONE);
                        lineOthersR.setVisibility(View.GONE);
                        txtOthersR.setText("");
                    }
                    else if(rbData.equalsIgnoreCase("2"))
                    {
                        secOthersR.setVisibility(View.GONE);
                        lineOthersR.setVisibility(View.GONE);
                        txtOthersR.setText("");
                    }
                    else if(rbData.equalsIgnoreCase("3"))
                    {
                        secOthersR.setVisibility(View.GONE);
                        lineOthersR.setVisibility(View.GONE);
                        txtOthersR.setText("");
                    }
                    else if(rbData.equalsIgnoreCase("4"))
                    {
                        secOthersR.setVisibility(View.GONE);
                        lineOthersR.setVisibility(View.GONE);
                        txtOthersR.setText("");
                    }
                    else if(rbData.equalsIgnoreCase("5"))
                    {
                        secOthersR.setVisibility(View.GONE);
                        lineOthersR.setVisibility(View.GONE);
                        txtOthersR.setText("");
                    }
                    else if(rbData.equalsIgnoreCase("6"))
                    {
                        secOthersR.setVisibility(View.GONE);
                        lineOthersR.setVisibility(View.GONE);
                        txtOthersR.setText("");
                    }
                    else
                    {
                        secOthersR.setVisibility(View.VISIBLE);
                        lineOthersR.setVisibility(View.VISIBLE);
                    }
                }
                public void onNothingSelected(AdapterView<?> adapterView) {
                    return;
                }
            });
            secOthersR=(LinearLayout)findViewById(R.id.secOthersR);
            lineOthersR=(View)findViewById(R.id.lineOthersR);
            VlblOthersR=(TextView) findViewById(R.id.VlblOthersR);
            txtOthersR=(AutoCompleteTextView) findViewById(R.id.txtOthersR);
            txtOthersR.setAdapter(C.getArrayAdapter("Select distinct OthersR from "+ TableName +" order by OthersR"));

            txtOthersR.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View arg1, int pos,long id) {

                }
            });
            txtOthersR.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    final int DRAWABLE_RIGHT = 2;

                    if(event.getAction() == MotionEvent.ACTION_UP) {
                        if(event.getRawX() >= (txtOthersR.getRight() - txtOthersR.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            ((EditText)v).setText("");
                            return true;
                        }
                    }
                    return false;
                }
            });
        } catch (Exception e) {
            Connection.MessageBox(RSV.this, e.getMessage());
            return;
        }
    }

    private void DataSave() {
        try {
            String ValidationMSG = ValidationCheck();
            if (ValidationMSG.length() > 0) {
                Connection.MessageBox(RSV.this, ValidationMSG);
                return;
            }

            String SQL = "";
            RadioButton rb;

            RSV_DataModel objSave = new RSV_DataModel();
            objSave.setChildID(txtChildID.getText().toString());
            objSave.setCID(txtCID.getText().toString());
            objSave.setPID(txtPID.getText().toString());
            objSave.setWeek(txtWeek.getText().toString());
            objSave.setVDate(dtpVDate.getText().toString().length() > 0 ? Global.DateConvertYMD(dtpVDate.getText().toString()) : dtpVDate.getText().toString());
            String[] d_rdogrpVType = new String[]{"1", "2", "3"};
            objSave.setVType("");
            for (int i = 0; i < rdogrpVType.getChildCount(); i++) {
                rb = (RadioButton) rdogrpVType.getChildAt(i);
                if (rb.isChecked()) objSave.setVType(d_rdogrpVType[i]);
            }

            objSave.setVisit(txtVisit.getText().toString());
            objSave.setSlNo(txtSlNo.getText().toString());
            objSave.setTemp(txtTemp.getText().toString());

            String[] d_rdogrpCough = new String[]{"1", "2"};
            objSave.setCough("");
            for (int i = 0; i < rdogrpCough.getChildCount(); i++) {
                rb = (RadioButton) rdogrpCough.getChildAt(i);
                if (rb.isChecked()) objSave.setCough(d_rdogrpCough[i]);
            }

            objSave.setdtpCoughDt(dtpdtpCoughDt.getText().toString().length() > 0 ? Global.DateConvertYMD(dtpdtpCoughDt.getText().toString()) : dtpdtpCoughDt.getText().toString());

            String[] d_rdogrpDBrea = new String[]{"1", "2"};
            objSave.setDBrea("");
            for (int i = 0; i < rdogrpDBrea.getChildCount(); i++) {
                rb = (RadioButton) rdogrpDBrea.getChildAt(i);
                if (rb.isChecked()) objSave.setDBrea(d_rdogrpDBrea[i]);
            }

            objSave.setdtpDBreaDt(dtpdtpDBreaDt.getText().toString().length() > 0 ? Global.DateConvertYMD(dtpdtpDBreaDt.getText().toString()) : dtpdtpDBreaDt.getText().toString());

            String[] d_rdogrpDeepCold = new String[]{"1", "2"};
            objSave.setDeepCold("");
            for (int i = 0; i < rdogrpDeepCold.getChildCount(); i++) {
                rb = (RadioButton) rdogrpDeepCold.getChildAt(i);
                if (rb.isChecked()) objSave.setDeepCold(d_rdogrpDeepCold[i]);
            }

            objSave.setDeepColdDt(dtpDeepColdDt.getText().toString().length() > 0 ? Global.DateConvertYMD(dtpDeepColdDt.getText().toString()) : dtpDeepColdDt.getText().toString());
            String[] d_rdogrpSoreThroat = new String[]{"1", "2"};
            objSave.setSoreThroat("");
            for (int i = 0; i < rdogrpSoreThroat.getChildCount(); i++) {
                rb = (RadioButton) rdogrpSoreThroat.getChildAt(i);
                if (rb.isChecked()) objSave.setSoreThroat(d_rdogrpSoreThroat[i]);
            }

            objSave.setSoreThroatDt(dtpSoreThroatDt.getText().toString().length() > 0 ? Global.DateConvertYMD(dtpSoreThroatDt.getText().toString()) : dtpSoreThroatDt.getText().toString());

            String[] d_rdogrpFever = new String[]{"1", "2", "3","4"};
            objSave.setFever("");
            for (int i = 0; i < rdogrpFever.getChildCount(); i++) {
                rb = (RadioButton) rdogrpFever.getChildAt(i);
                if (rb.isChecked()) objSave.setFever(d_rdogrpFever[i]);
            }
            objSave.setFeverDt(dtpFeverDt.getText().toString().length() > 0 ? Global.DateConvertYMD(dtpFeverDt.getText().toString()) : dtpFeverDt.getText().toString());


            String[] d_rdogrpRSVsuitable = new String[]{"1", "2"};
            objSave.setRSVsuitable("");
            for (int i = 0; i < rdogrpRSVsuitable.getChildCount(); i++) {
                rb = (RadioButton) rdogrpRSVsuitable.getChildAt(i);
                if (rb.isChecked()) objSave.setRSVsuitable(d_rdogrpRSVsuitable[i]);
            }

            String[] d_rdogrpRSVlisted = new String[]{"1", "2"};
            objSave.setRSVlisted("");
            for (int i = 0; i < rdogrpRSVlisted.getChildCount(); i++) {
                rb = (RadioButton) rdogrpRSVlisted.getChildAt(i);
                if (rb.isChecked()) objSave.setRSVlisted(d_rdogrpRSVlisted[i]);
            }

            objSave.setRSVlistedDt(dtpRSVlistedDt.getText().toString().length() > 0 ? Global.DateConvertYMD(dtpRSVlistedDt.getText().toString()) : dtpRSVlistedDt.getText().toString());
            objSave.setReason(txtReason.getText().toString());

            String[] d_rdogrpSuitSam = new String[] {"1","2"};
            objSave.setSuitSam("");
            for (int i = 0; i < rdogrpSuitSam.getChildCount(); i++)
            {
                rb = (RadioButton)rdogrpSuitSam.getChildAt(i);
                if (rb.isChecked()) objSave.setSuitSam(d_rdogrpSuitSam[i]);
            }

            String[] d_rdogrpSuitSamRe = new String[] {"1","2","3","7"};
            objSave.setSuitSamRe("");
            for (int i = 0; i < rdogrpSuitSamRe.getChildCount(); i++)
            {
                rb = (RadioButton)rdogrpSuitSamRe.getChildAt(i);
                if (rb.isChecked()) objSave.setSuitSamRe(d_rdogrpSuitSamRe[i]);
            }

            objSave.setSuitSamReO(txtSuitSamReO.getText().toString());

            String[] d_rdogrpSampleAgree = new String[] {"1","2"};
            objSave.setSampleAgree("");
            for (int i = 0; i < rdogrpSampleAgree.getChildCount(); i++)
            {
                rb = (RadioButton)rdogrpSampleAgree.getChildAt(i);
                if (rb.isChecked()) objSave.setSampleAgree(d_rdogrpSampleAgree[i]);
            }
            String[] d_rdogrpNotAgree = new String[] {"1","2","3","4","5","6","7"};
            objSave.setNotAgree("");
            for (int i = 0; i < rdogrpNotAgree.getChildCount(); i++)
            {
                rb = (RadioButton)rdogrpNotAgree.getChildAt(i);
                if (rb.isChecked()) objSave.setNotAgree(d_rdogrpNotAgree[i]);
            }
            objSave.setOthersR(txtOthersR.getText().toString());

            objSave.setStartTime(STARTTIME);
            objSave.setEndTime(g.CurrentTime24());
            objSave.setDeviceID(DEVICEID);
            objSave.setEntryUser(ENTRYUSER); //from data entry user list
            objSave.setLat(MySharedPreferences.getValue(this, "lat"));
            objSave.setLon(MySharedPreferences.getValue(this, "lon"));

            String status = objSave.SaveUpdateData(this);
            if (status.length() == 0) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("res", "");
                setResult(Activity.RESULT_OK, returnIntent);

                Connection.MessageBox(RSV.this, "Saved Successfully");
            } else {
                Connection.MessageBox(RSV.this, status);
                return;
            }
        } catch (Exception e) {
            Connection.MessageBox(RSV.this, e.getMessage());
            return;
        }
    }

    private String ValidationCheck() {
        String ValidationMsg = "";
        String DV = "";
        try {
            ResetSectionColor();
            if (txtChildID.getText().toString().length() == 0 & secChildID.isShown()) {
                ValidationMsg += "\nRequired field: Child ID.";
                secChildID.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_Section_Highlight));
            }


//         if(secChildID.isShown() & (Integer.valueOf(txtChildID.getText().toString().length()==0 ? "1" : txtChildID.getText().toString()) < 1 || Integer.valueOf(txtChildID.getText().toString().length()==0 ? "9999999999999999" : txtChildID.getText().toString()) > 9999999))
//           {
//             ValidationMsg += "\nValue should be between 1 and 99999999999(Child ID).";
//             secChildID.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color_Section_Highlight));
//           }
//         if(txtCID.getText().toString().length()==0 & secCID.isShown())
//           {
//             ValidationMsg += "\nRequired field: শিশুর চলতি ID নং.";
//             secCID.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color_Section_Highlight));
//           }
//         if(secCID.isShown() & (Integer.valueOf(txtCID.getText().toString().length()==0 ? "1" : txtCID.getText().toString()) < 1 || Integer.valueOf(txtCID.getText().toString().length()==0 ? "99999999999" : txtCID.getText().toString()) > 999999999))
//           {
//             ValidationMsg += "\nValue should be between 1 and 99999999999(শিশুর চলতি ID নং).";
//             secCID.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color_Section_Highlight));
//           }
//         if(txtPID.getText().toString().length()==0 & secPID.isShown())
//           {
//             ValidationMsg += "\nRequired field: শিশুর স্থায়ী ID নং.";
//             secPID.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color_Section_Highlight));
//           }
//         if(secPID.isShown() & (Integer.valueOf(txtPID.getText().toString().length()==0 ? "1" : txtPID.getText().toString()) < 1 || Integer.valueOf(txtPID.getText().toString().length()==0 ? "99999999" : txtPID.getText().toString()) > 99999999))
//           {
//             ValidationMsg += "\nValue should be between 1 and 99999999(শিশুর স্থায়ী ID নং).";
//             secPID.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color_Section_Highlight));
//           }
            if (txtWeek.getText().toString().length() == 0 & secWeek.isShown()) {
                ValidationMsg += "\nRequired field: সপ্তাহ.";
                secWeek.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_Section_Highlight));
            }
            if (secWeek.isShown() & (Integer.valueOf(txtWeek.getText().toString().length() == 0 ? "1" : txtWeek.getText().toString()) < 1 || Integer.valueOf(txtWeek.getText().toString().length() == 0 ? "999" : txtWeek.getText().toString()) > 999)) {
                ValidationMsg += "\nValue should be between 1 and 999(সপ্তাহ).";
                secWeek.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_Section_Highlight));
            }
            DV = Global.DateValidate(dtpVDate.getText().toString());
            if (DV.length() != 0 & secVDate.isShown()) {
                ValidationMsg += "\nRequired field/Not a valid date format: ভিজিটের তারিখ.";
                secVDate.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_Section_Highlight));
            }
            if (!rdoVType1.isChecked() & !rdoVType2.isChecked() & !rdoVType3.isChecked() & secVType.isShown()) {
                ValidationMsg += "\nRequired field: ফলোআপ পরিদর্শন/ অতিরিক্ত.";
                secVType.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_Section_Highlight));
            }
            if (txtVisit.getText().toString().length() == 0 & secVisit.isShown()) {
                ValidationMsg += "\nRequired field: পরিদর্শন সংখ্যা.";
                secVisit.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_Section_Highlight));
            }
            if (secVisit.isShown() & (Integer.valueOf(txtVisit.getText().toString().length() == 0 ? "1" : txtVisit.getText().toString()) < 0 || Integer.valueOf(txtVisit.getText().toString().length() == 0 ? "9" : txtVisit.getText().toString()) > 9)) {
                ValidationMsg += "\nValue should be between 1 and 9(পরিদর্শন সংখ্যা).";
                secVisit.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_Section_Highlight));
            }
            if (txtSlNo.getText().toString().length() == 0 & secSlNo.isShown()) {
                ValidationMsg += "\nRequired field: আর এস ভি পরিদর্শন #.";
                secSlNo.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_Section_Highlight));
            }
            if (secSlNo.isShown() & (Integer.valueOf(txtSlNo.getText().toString().length() == 0 ? "1" : txtSlNo.getText().toString()) < 1 || Integer.valueOf(txtSlNo.getText().toString().length() == 0 ? "2" : txtSlNo.getText().toString()) > 2)) {
                ValidationMsg += "\nValue should be between 1 and 2(আর এস ভি পরিদর্শন #).";
                secSlNo.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_Section_Highlight));
            }
//            if (txtTemp.getText().toString().length() == 0 & secTemp.isShown()) {
//                ValidationMsg += "\nRequired field: তাপমাত্রা.";
//                secTemp.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_Section_Highlight));
//            }
            if (secTemp.isShown() & (Double.valueOf(txtTemp.getText().toString().length() == 0 ? "070" : txtTemp.getText().toString()) < 070 || Double.valueOf(txtTemp.getText().toString().length() == 0 ? "106" : txtTemp.getText().toString()) > 106)) {
                ValidationMsg += "\nValue should be between 070 and 106(তাপমাত্রা).";
                secTemp.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_Section_Highlight));
            }
            DV = Global.DateValidate(dtpdtpCoughDt.getText().toString());
            if (DV.length() != 0 & secdtpCoughDt.isShown()) {
                ValidationMsg += "\nRequired field/Not a valid date format: কাশি শুরুর তারিখ.";
                secdtpCoughDt.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_Section_Highlight));
            }
            DV = Global.DateValidate(dtpdtpDBreaDt.getText().toString());
            if (DV.length() != 0 & secdtpDBreaDt.isShown()) {
                ValidationMsg += "\nRequired field/Not a valid date format: শ্বাসকষ্ট শুরুর তারিখ.";
                secdtpDBreaDt.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_Section_Highlight));
            }
            DV = Global.DateValidate(dtpDeepColdDt.getText().toString());

            if (!rdoDeepCold1.isChecked() & !rdoDeepCold2.isChecked() & secDeepCold.isShown()) {
                ValidationMsg += "\nRequired field: ঘন সর্দি.";
                secDeepCold.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_Section_Highlight));
            }

            if (DV.length() != 0 & secDeepColdDt.isShown()) {
                ValidationMsg += "\nRequired field/Not a valid date format: ঘন সর্দি শুরুর তারিখ.";
                secDeepColdDt.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_Section_Highlight));
            }

            if (!rdoSoreThroat1.isChecked() & !rdoSoreThroat2.isChecked() & secSoreThroat.isShown()) {
                ValidationMsg += "\nRequired field: গলা ব্যাথা.";
                secSoreThroat.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_Section_Highlight));
            }

            DV = Global.DateValidate(dtpSoreThroatDt.getText().toString());
            if (DV.length() != 0 & secSoreThroatDt.isShown()) {
                ValidationMsg += "\nRequired field/Not a valid date format: গলা ব্যাথা শুরুর তারিখ.";
                secSoreThroatDt.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_Section_Highlight));
            }

            if(!rdoFever1.isChecked() & !rdoFever2.isChecked() & !rdoFever3.isChecked() & !rdoFever4.isChecked() & secFever.isShown()){
                ValidationMsg += "\nRequired field: জ্বর (অল্প/মাঝারি/বেশি).";
                secFever.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_Section_Highlight));
            }
            DV = Global.DateValidate(dtpFeverDt.getText().toString());
            if (DV.length() != 0 & secFeverDt.isShown()) {
                ValidationMsg += "\nRequired field/Not a valid date format: জ্বর শুরুর তারিখ.";
                secFeverDt.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_Section_Highlight));
            }

            if (!rdoRSVsuitable1.isChecked() & !rdoRSVsuitable2.isChecked() & secRSVsuitable.isShown()) {
                ValidationMsg += "\nRequired field: আর এস ভি গবেষণার জন্য উপযুক্ত.";
                secRSVsuitable.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_Section_Highlight));
            }
//            //******************* Sample Date Check
//            //******************* Sample Date Check (0-14 day) Shahidul
//            String Day14d_day;
////                        Day14=C.ReturnSingleValue("SELECT cast((ifnull(cast((julianday(date(e)max(VDat))-julianday(date('"+Global.DateConvertYMD(dtpVDate.getText().toString())+"'))) as int),0)) as int)Diff  FROM RSVSample WHERE   ChildId = '"+ txtChildID.getText().toString() +"' and Status='1'");
//            Day14d_day=C.ReturnSingleValue("SELECT date(max(VDate)) FROM RSVSample WHERE   ChildId = '"+ txtChildID.getText().toString() +"' and Status='1'");
//            if (Day14d_day != null) {
//                int dy14 = Global.DateDifferenceDays(dtpVDate.getText().toString(), Global.DateConvertDMY(Day14d_day));
//                if (dy14 >= 0 & dy14 <= 14 & !rdoSuitSam2.isChecked()) {
////                            Connection.MessageBox(RSV.this,"শেষ নমুনা সংগ্রহের তারিখ হইতে আজকের তারিখের পার্থক্য ১৪ দিনের কম ("+Global.DateConvertYMD(dtpVDate.getText().toString())+" "+Day14+" "+dy1+" দিন)");
////                Connection.MessageBox(RSV.this,"শেষ নমুনা সংগ্রহের তারিখ ("+Day14d_day+") হইতে আজকের তারিখের (পার্থক্য : "+dy14+" দিন)। (সুতরাং ১৪ দিনের কম সময়ের মধ্যে আবার স্যাম্পল দেওয়ার জন্য উপযুক্ত হাঁ হতে পারবেনা)");
//                    ValidationMsg += "শেষ নমুনা সংগ্রহের তারিখ (" + Day14d_day + ") হইতে আজকের তারিখের (পার্থক্য : " + dy14 + " দিন)। (সুতরাং ১৪ দিনের কম সময়ের মধ্যে আবার স্যাম্পল দেওয়ার জন্য উপযুক্ত হাঁ হতে পারবেনা)";
//                    secRSVlisted.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_Section_Highlight));
//                }
//            }
            //******************* Sample Date Check (0-60 day) Shahidul
            String Day60d_day;
            Day60d_day=C.ReturnSingleValue("SELECT date(max(VDate)) FROM RSVSample WHERE   ChildId = '"+ txtChildID.getText().toString() +"' and Status='1'");
            if (Day60d_day != null) {
                int dy60 = Global.DateDifferenceDays(dtpVDate.getText().toString(), Global.DateConvertDMY(Day60d_day));
                if (dy60 >= 0 & dy60 <= 60 & !rdoSuitSam2.isChecked()) {
                    ValidationMsg += "শেষ নমুনা সংগ্রহের তারিখ (" + Day60d_day + ") হইতে আজকের তারিখের (পার্থক্য : " + dy60 + " দিন)। (সুতরাং ৬০ দিনের কম সময়ের মধ্যে আবার স্যাম্পল দেওয়ার জন্য উপযুক্ত হাঁ হতে পারবেনা)";
                    secRSVlisted.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_Section_Highlight));
                }
            }


            //******************* Sample Date Check
//            //******************* Sample Date Check (>14) days Shahidul
//            String Day14d_day1;
//            Day14d_day1=C.ReturnSingleValue("SELECT date(max(VDate)) FROM RSVSample WHERE   ChildId = '"+ txtChildID.getText().toString() +"' and Status='1'");
//            if (Day14d_day1 != null) {
//                int dy14_1 = Global.DateDifferenceDays(dtpVDate.getText().toString(), Global.DateConvertDMY(Day14d_day1));
//                if (dy14_1 > 14 & rdoSuitSam2.isChecked() & rdoSuitSamRe1.isChecked()) {
//                    ValidationMsg += "শেষ নমুনা সংগ্রহের তারিখ (" + Day14d_day1 + ") হইতে আজকের তারিখের (পার্থক্য : " + dy14_1 + " দিন)। (সুতরাং ১৪ দিনের কম সময়ের মধ্যে অপশন সিলেক্ট কোরা যাবে না)";
//                    secRSVlisted.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_Section_Highlight));
//                }
//            }
//          ******************* Sample Date Check (>60) days (scRNA) Shahidul
            String Day60d_day1;
            Day60d_day1=C.ReturnSingleValue("SELECT date(max(VDate)) FROM RSVSample WHERE   ChildId = '"+ txtChildID.getText().toString() +"' and Status='1'");
            if (Day60d_day1 != null) {
                int dy60_1 = Global.DateDifferenceDays(dtpVDate.getText().toString(), Global.DateConvertDMY(Day60d_day1));
                if (dy60_1 > 60 & rdoSuitSam2.isChecked() & rdoSuitSamRe1.isChecked()) {
                    ValidationMsg += "শেষ নমুনা সংগ্রহের তারিখ (" + Day60d_day1 + ") হইতে আজকের তারিখের (পার্থক্য : " + dy60_1 + " দিন)। (সুতরাং ৬০ দিনের কম সময়ের মধ্যে অপশন সিলেক্ট কোরা যাবে না)";
                    secRSVlisted.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_Section_Highlight));
                }
            }

            //******************* Sample Date Check
//            //******************* Sample Date Check (0-14 day but <> ১৪ দিনের কম অপশন সিলেক্ট)  days Shahidul
//            String Day14d_day2;
//            Day14d_day2=C.ReturnSingleValue("SELECT date(max(VDate)) FROM RSVSample WHERE   ChildId = '"+ txtChildID.getText().toString() +"' and Status='1'");
//            if (Day14d_day2 != null) {
//                int dy14_2 = Global.DateDifferenceDays(dtpVDate.getText().toString(), Global.DateConvertDMY(Day14d_day2));
//                if (dy14_2 >= 0 & dy14_2 <= 14 & rdoSuitSam2.isChecked() & (rdoSuitSamRe2.isChecked() || rdoSuitSamRe3.isChecked() || rdoSuitSamRe3.isChecked())) {
//                    ValidationMsg += "শেষ নমুনা সংগ্রহের তারিখ (" + Day14d_day2 + ") হইতে আজকের তারিখের (পার্থক্য : " + dy14_2 + " দিন)। (সুতরাং ১৪ দিনের কম সময়ের মধ্যে অপশন সিলেক্ট করতে হবে)";
//                    secRSVlisted.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_Section_Highlight));
//                }
//            }

//          ******************* Sample Date Check (0-60 day but <> ৬০ দিনের কম অপশন সিলেক্ট)  days Shahidul
            String Day60d_day2;
            Day60d_day2=C.ReturnSingleValue("SELECT date(max(VDate)) FROM RSVSample WHERE   ChildId = '"+ txtChildID.getText().toString() +"' and Status='1'");
            if (Day60d_day2 != null) {
                int dy60_2 = Global.DateDifferenceDays(dtpVDate.getText().toString(), Global.DateConvertDMY(Day60d_day2));
                if (dy60_2 >= 0 & dy60_2 <= 60 & rdoSuitSam2.isChecked() & (rdoSuitSamRe2.isChecked() || rdoSuitSamRe3.isChecked() || rdoSuitSamRe3.isChecked())) {
                    ValidationMsg += "শেষ নমুনা সংগ্রহের তারিখ (" + Day60d_day2 + ") হইতে আজকের তারিখের (পার্থক্য : " + dy60_2 + " দিন)। (সুতরাং ৬০ দিনের কম সময়ের মধ্যে অপশন সিলেক্ট করতে হবে)";
                    secRSVlisted.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_Section_Highlight));
                }
            }

            //******************* Sample Date Check


            if (!rdoRSVlisted1.isChecked() & !rdoRSVlisted2.isChecked() & secRSVlisted.isShown()) {
                ValidationMsg += "\nRequired field: আর এস ভি গবেষণার জন্য তালিকাভুক্ত.";
                secRSVlisted.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_Section_Highlight));
            }
            if (!rdoCough1.isChecked() & !rdoDBrea1.isChecked() & !rdoDeepCold1.isChecked() & !rdoSoreThroat1.isChecked() & (rdoFever1.isChecked() | rdoFever4.isChecked()) & secRSVlisted.isShown()) {
                ValidationMsg += "কাশি, শ্বাসকষ্ট, ঘন সর্দি, গলা ব্যাথা যদি না হয় এবং অল্প জ্বর/জ্বর না থাকে তাহলে আর এস ভি গবেষণার জন্য তালিকাভুক্ত হা হবেনা।";
                secRSVlisted.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_Section_Highlight));
            }
            DV = Global.DateValidate(dtpRSVlistedDt.getText().toString());
            if (DV.length() != 0 & secRSVlistedDt.isShown()) {
                ValidationMsg += "\nRequired field/Not a valid date format: তারিখ.";
                secRSVlistedDt.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_Section_Highlight));
            }

            String Rdt = Global.DateConvertYMD(dtpRSVlistedDt.getText().toString());
            String Vdt = Global.DateConvertYMD(dtpVDate.getText().toString());
            int Rdt_difference = Global.DateDifferenceDays(Global.DateConvertDMY(Rdt), Global.DateConvertDMY(Vdt));

            if (secRSVlistedDt.isShown() && Rdt_difference < 0) {
                ValidationMsg += "আর এস ভি গবেষণার জন্য তালিকাভুক্ত তারিখ" + Rdt + " অবশ্যই ভিজিটের তারিখ " + Vdt + "  এর সমান অথবা বেশী হতে হবে ।.";
                secRSVlistedDt.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_Section_Highlight));
            }
//            if (txtReason.getText().toString().length() == 0 & secReason.isShown()) {
//                ValidationMsg += "\nRequired field: তালিকাভুক্ত না হলে কারন .........( অনুপস্থিত, মৃত্যু, সম্মতি প্রত্যাহার,  অন্যান্য).";
//                secReason.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_Section_Highlight));
//            }
            if(!rdoSuitSam1.isChecked() & !rdoSuitSam2.isChecked() & secSuitSam.isShown())
            {
                ValidationMsg += "\nRequired field: স্যাম্পল দেওয়ার জন্য উপযুক্ত.";
                secSuitSam.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color_Section_Highlight));
            }
            if(!rdoSuitSamRe1.isChecked() & !rdoSuitSamRe2.isChecked() & !rdoSuitSamRe3.isChecked() & !rdoSuitSamRe4.isChecked() & secSuitSamRe.isShown())
            {
                ValidationMsg += "\nRequired field: উপযুক্ত না হওয়ার কারণ.";
                secSuitSamRe.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color_Section_Highlight));
            }
            if(txtSuitSamReO.getText().toString().length()==0 & secSuitSamReO.isShown())
            {
                ValidationMsg += "\nRequired field: অন্যান্য (উল্লেখ করুন).";
                secSuitSamReO.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color_Section_Highlight));
            }
            if(!rdoSampleAgree1.isChecked() & !rdoSampleAgree2.isChecked() & secSampleAgree.isShown())
            {
                ValidationMsg += "\nRequired field: স্যাম্পল দিতে রাজি :.";
                secSampleAgree.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color_Section_Highlight));
            }
            if(!rdoNotAgree1.isChecked() & !rdoNotAgree2.isChecked() & !rdoNotAgree3.isChecked() & !rdoNotAgree4.isChecked() & !rdoNotAgree6.isChecked() & !rdoNotAgree7.isChecked() & !rdoNotAgree5.isChecked() & secNotAgree.isShown())
            {
                ValidationMsg += "\nRequired field: রাজী না হওয়ার কারণ.";
                secNotAgree.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color_Section_Highlight));
            }
            if(txtOthersR.getText().toString().length()==0 & secOthersR.isShown())
            {
                ValidationMsg += "\nRequired field: অন্যান্য (উল্লেখ করুন).";
                secOthersR.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color_Section_Highlight));
            }
        } catch (Exception e) {
            ValidationMsg += "\n" + e.getMessage();
        }

        return ValidationMsg;
    }

    private void ResetSectionColor() {
        try {
            secChildID.setBackgroundColor(Color.WHITE);
            secChildID.setBackgroundColor(Color.WHITE);
            secCID.setBackgroundColor(Color.WHITE);
            secCID.setBackgroundColor(Color.WHITE);
            secPID.setBackgroundColor(Color.WHITE);
            secPID.setBackgroundColor(Color.WHITE);
            secWeek.setBackgroundColor(Color.WHITE);
            secWeek.setBackgroundColor(Color.WHITE);
            secVDate.setBackgroundColor(Color.WHITE);
            secVType.setBackgroundColor(Color.WHITE);
            secVisit.setBackgroundColor(Color.WHITE);
            secVisit.setBackgroundColor(Color.WHITE);
            secSlNo.setBackgroundColor(Color.WHITE);
            secSlNo.setBackgroundColor(Color.WHITE);
            secTemp.setBackgroundColor(Color.WHITE);
            secTemp.setBackgroundColor(Color.WHITE);
            secdtpCoughDt.setBackgroundColor(Color.WHITE);
            secdtpDBreaDt.setBackgroundColor(Color.WHITE);
            secDeepColdDt.setBackgroundColor(Color.WHITE);
            secSoreThroatDt.setBackgroundColor(Color.WHITE);
            secRSVsuitable.setBackgroundColor(Color.WHITE);
            secRSVlisted.setBackgroundColor(Color.WHITE);
            secRSVlistedDt.setBackgroundColor(Color.WHITE);
            secReason.setBackgroundColor(Color.WHITE);
            secFever.setBackgroundColor(Color.WHITE);
            secFeverDt.setBackgroundColor(Color.WHITE);
            secSuitSam.setBackgroundColor(Color.WHITE);
            secSuitSamRe.setBackgroundColor(Color.WHITE);
            secSuitSamReO.setBackgroundColor(Color.WHITE);
            secNotAgree.setBackgroundColor(Color.WHITE);
            secOthersR.setBackgroundColor(Color.WHITE);
        } catch (Exception e) {
        }
    }

    private void DataSearch(String ChildID, String Week, String VType, String Visit) {
        try {
            RadioButton rb;
            RSV_DataModel d = new RSV_DataModel();
            String SQL = "Select * from " + TableName + "  Where ChildID='" + ChildID + "' and Week='" + Week + "' and VType='" + VType + "' and Visit='" + Visit + "'";
            List<RSV_DataModel> data = d.SelectAll(this, SQL);
            for (RSV_DataModel item : data) {
                txtChildID.setText(String.valueOf(item.getChildID()));
                txtCID.setText(String.valueOf(item.getCID()));
                txtPID.setText(String.valueOf(item.getPID()));
                txtWeek.setText(String.valueOf(item.getWeek()));
                dtpVDate.setText(item.getVDate().toString().length() == 0 ? "" : Global.DateConvertDMY(item.getVDate()));
                String[] d_rdogrpVType = new String[]{"1", "2", "3"};
                for (int i = 0; i < d_rdogrpVType.length; i++) {
                    if (String.valueOf(item.getVType()).equals(String.valueOf(d_rdogrpVType[i]))) {
                        rb = (RadioButton) rdogrpVType.getChildAt(i);
                        rb.setChecked(true);
                    }
                }
                txtVisit.setText(String.valueOf(item.getVisit()));
                txtSlNo.setText(String.valueOf(item.getSlNo()));
                txtTemp.setText(String.valueOf(item.getTemp()));

                String[] d_rdogrpCough = new String[]{"1", "2"};
                for (int i = 0; i < d_rdogrpCough.length; i++) {
                    if (String.valueOf(item.getCough()).equals(String.valueOf(d_rdogrpCough[i]))) {
                        rb = (RadioButton) rdogrpCough.getChildAt(i);
                        rb.setChecked(true);
                    }
                }

                dtpdtpCoughDt.setText(item.getdtpCoughDt().toString().length() == 0 ? "" : Global.DateConvertDMY(item.getdtpCoughDt()));
                //dtpdtpCoughDt.setText(item.getdtpCoughDt().toString().length() == 0 ? "" : Global.DateConvertMDY(item.getdtpCoughDt()));

                String[] d_rdogrpDBrea = new String[]{"1", "2"};
                for (int i = 0; i < d_rdogrpDBrea.length; i++) {
                    if (String.valueOf(item.getDBrea()).equals(String.valueOf(d_rdogrpDBrea[i]))) {
                        rb = (RadioButton) rdogrpDBrea.getChildAt(i);
                        rb.setChecked(true);
                    }
                }

                dtpdtpDBreaDt.setText(item.getdtpDBreaDt().toString().length() == 0 ? "" : Global.DateConvertDMY(item.getdtpDBreaDt()));

                String[] d_rdogrpDeepCold = new String[]{"1", "2"};
                for (int i = 0; i < d_rdogrpDeepCold.length; i++) {
                    if (String.valueOf(item.getDeepCold()).equals(String.valueOf(d_rdogrpDeepCold[i]))) {
                        rb = (RadioButton) rdogrpDeepCold.getChildAt(i);
                        rb.setChecked(true);
                    }
                }

                dtpDeepColdDt.setText(item.getDeepColdDt().toString().length() == 0 ? "" : Global.DateConvertDMY(item.getDeepColdDt()));

                String[] d_rdogrpSoreThroat = new String[]{"1", "2"};
                for (int i = 0; i < d_rdogrpSoreThroat.length; i++) {
                    if (String.valueOf(item.getSoreThroat()).equals(String.valueOf(d_rdogrpSoreThroat[i]))) {
                        rb = (RadioButton) rdogrpSoreThroat.getChildAt(i);
                        rb.setChecked(true);
                    }
                }

                dtpSoreThroatDt.setText(item.getSoreThroatDt().toString().length() == 0 ? "" : Global.DateConvertDMY(item.getSoreThroatDt()));
                String[] d_rdogrpRSVsuitable = new String[]{"1", "2"};
                for (int i = 0; i < d_rdogrpRSVsuitable.length; i++) {
                    if (String.valueOf(item.getRSVsuitable()).equals(String.valueOf(d_rdogrpRSVsuitable[i]))) {
                        rb = (RadioButton) rdogrpRSVsuitable.getChildAt(i);
                        rb.setChecked(true);
                    }
                }


                String[] d_rdogrpFever = new String[]{"1", "2", "3","4"};
                for (int i = 0; i < d_rdogrpFever.length; i++) {
                    if (String.valueOf(item.getFever()).equals(String.valueOf(d_rdogrpFever[i]))) {
                        rb = (RadioButton) rdogrpFever.getChildAt(i);
                        rb.setChecked(true);
                    }
                }
                //dtpFeverDt.setText(item.getFeverDt().toString().length() == 0 ? "" : Global.DateConvertMDY(item.getFeverDt()));
                dtpFeverDt.setText(item.getFeverDt().toString().length() == 0 ? "" : Global.DateConvertDMY(item.getFeverDt()));

                String[] d_rdogrpSuitSam = new String[] {"1","2"};
                for (int i = 0; i < d_rdogrpSuitSam.length; i++)
                {
                    if (String.valueOf(item.getSuitSam()).equals(String.valueOf(d_rdogrpSuitSam[i])))
                    {
                        rb = (RadioButton)rdogrpSuitSam.getChildAt(i);
                        rb.setChecked(true);
                    }
                }
                String[] d_rdogrpSuitSamRe = new String[] {"1","2","3","7"};
                for (int i = 0; i < d_rdogrpSuitSamRe.length; i++)
                {
                    if (String.valueOf(item.getSuitSamRe()).equals(String.valueOf(d_rdogrpSuitSamRe[i])))
                    {
                        rb = (RadioButton)rdogrpSuitSamRe.getChildAt(i);
                        rb.setChecked(true);
                    }
                }
                txtSuitSamReO.setText(item.getSuitSamReO());

                String[] d_rdogrpRSVlisted = new String[]{"1", "2"};
                for (int i = 0; i < d_rdogrpRSVlisted.length; i++) {
                    if (String.valueOf(item.getRSVlisted()).equals(String.valueOf(d_rdogrpRSVlisted[i]))) {
                        rb = (RadioButton) rdogrpRSVlisted.getChildAt(i);
                        rb.setChecked(true);
                    }
                }
                dtpRSVlistedDt.setText(item.getRSVlistedDt().toString().length() == 0 ? "" : Global.DateConvertDMY(item.getRSVlistedDt()));
                txtReason.setText(item.getReason());
                String[] d_rdogrpSampleAgree = new String[] {"1","2"};
                for (int i = 0; i < d_rdogrpSampleAgree.length; i++)
                {
                    if (String.valueOf(item.getSampleAgree()).equals(String.valueOf(d_rdogrpSampleAgree[i])))
                    {
                        rb = (RadioButton)rdogrpSampleAgree.getChildAt(i);
                        rb.setChecked(true);
                    }
                }
                String[] d_rdogrpNotAgree = new String[] {"1","2","3","4","5","6","7"};
                for (int i = 0; i < d_rdogrpNotAgree.length; i++)
                {
                    if (String.valueOf(item.getNotAgree()).equals(String.valueOf(d_rdogrpNotAgree[i])))
                    {
                        rb = (RadioButton)rdogrpNotAgree.getChildAt(i);
                        rb.setChecked(true);
                    }
                }
                txtOthersR.setText(item.getOthersR());
            }
        } catch (Exception e) {
            Connection.MessageBox(RSV.this, e.getMessage());
            return;
        }
    }


    protected Dialog onCreateDialog(int id) {
        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        switch (id) {
            case DATE_DIALOG:
                return new DatePickerDialog(this, mDateSetListener, g.mYear, g.mMonth - 1, g.mDay);
            case TIME_DIALOG:
                return new TimePickerDialog(this, timePickerListener, hour, minute, false);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear + 1;
            mDay = dayOfMonth;
            EditText dtpDate;


            dtpDate = (EditText) findViewById(R.id.dtpVDate);
            if (VariableID.equals("btnVDate")) {
                dtpDate = (EditText) findViewById(R.id.dtpVDate);
            } else if (VariableID.equals("btndtpCoughDt")) {
                dtpDate = (EditText) findViewById(R.id.dtpdtpCoughDt);
            } else if (VariableID.equals("btndtpDBreaDt")) {
                dtpDate = (EditText) findViewById(R.id.dtpdtpDBreaDt);
            } else if (VariableID.equals("btnDeepColdDt")) {
                dtpDate = (EditText) findViewById(R.id.dtpDeepColdDt);
            } else if (VariableID.equals("btnSoreThroatDt")) {
                dtpDate = (EditText) findViewById(R.id.dtpSoreThroatDt);
            } else if (VariableID.equals("btnRSVlistedDt")) {
                dtpDate = (EditText) findViewById(R.id.dtpRSVlistedDt);
            }
            else if (VariableID.equals("dtpFeverDt")) {
                dtpDate = (EditText) findViewById(R.id.dtpFeverDt);
            }

            dtpDate.setText(new StringBuilder()
                    .append(Global.Right("00" + mDay, 2)).append("/")
                    .append(Global.Right("00" + mMonth, 2)).append("/")
                    .append(mYear));
        }
    };

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
            hour = selectedHour;
            minute = selectedMinute;
            EditText tpTime;

        }
    };

     private String SlNumber(String ChildID, String Week, String VType, String Visit)
     {
         String M = C.ReturnSingleValue("Select cast(ifnull(max(SlNo),0)+1 as varchar(1))SlNumber from RSV where ChildID='"+ ChildID +"' and Week='"+ Week +"' and VType='"+ VType +"' and Visit='"+ Visit +"'");
         return M;
     }

    // turning off the GPS if its in on state. to avoid the battery drain.
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
}
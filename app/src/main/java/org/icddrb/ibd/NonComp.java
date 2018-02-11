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
import android.view.KeyEvent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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

import org.w3c.dom.Text;

import Common.*;

public class NonComp extends Activity {
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
        inflater.inflate(R.menu.mnuclose, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog.Builder adb = new AlertDialog.Builder(NonComp.this);
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

    LinearLayout secAll;
    LinearLayout secChildId;
    TextView VlblChildId;
    TextView txtChildId;
    LinearLayout secCID;

    TextView txtName;
    TextView txtWeek;
    TextView txtVisit;

    LinearLayout secVDate;
    TextView VlblVDate;
    EditText dtpVDate;
    ImageButton btnVDate;
    LinearLayout secRefResult;
    TextView VlblRefResult;
    RadioGroup rdogrpRefResult;

    RadioButton rdoRefResult1;
    RadioButton rdoRefResult2;

    LinearLayout seclbl1;

    LinearLayout secVisitOthYN;
    TextView VlblVisitOthYN;
    RadioGroup rdogrpVisitOthYN;

    RadioButton rdoVisitOthYN1;
    RadioButton rdoVisitOthYN2;
    LinearLayout seclblPro;
    LinearLayout secProvider1;
    TextView VlblProvider1;
    Spinner spnProvider1;
    LinearLayout secProvider2;
    TextView VlblProvider2;
    Spinner spnProvider2;
    LinearLayout secProvider3;
    TextView VlblProvider3;
    Spinner spnProvider3;
    LinearLayout secProvider4;
    TextView VlblProvider4;
    Spinner spnProvider4;
    LinearLayout secProviderOth1;
    TextView VlblProviderOth1;
    EditText txtProviderOth1;
    LinearLayout secPrescrip;
    TextView VlblPrescrip;
    RadioGroup rdogrpPrescrip;

    RadioButton rdoPrescrip1;
    RadioButton rdoPrescrip2;
    RadioButton rdoPrescrip3;
    RadioButton rdoPrescrip4;
    LinearLayout seclbl22;
    LinearLayout secRefA;
    TextView VlblRefA;
    CheckBox chkRefA;
    LinearLayout secRefB;
    TextView VlblRefB;
    CheckBox chkRefB;
    LinearLayout secRefC;
    TextView VlblRefC;
    CheckBox chkRefC;
    LinearLayout secRefD;
    TextView VlblRefD;
    CheckBox chkRefD;
    LinearLayout secRefE;
    TextView VlblRefE;
    CheckBox chkRefE;
    LinearLayout secRefF;
    TextView VlblRefF;
    CheckBox chkRefF;
    LinearLayout secRefG;
    TextView VlblRefG;
    CheckBox chkRefG;
    LinearLayout secRefH;
    TextView VlblRefH;
    CheckBox chkRefH;
    LinearLayout secRefI;
    TextView VlblRefI;
    CheckBox chkRefI;
    LinearLayout secRefX;
    TextView VlblRefX;
    CheckBox chkRefX;
    LinearLayout secRefOth;
    TextView VlblRefOth;
    EditText txtRefOth;
    LinearLayout seclbl23;
    LinearLayout secServiceA;
    TextView VlblServiceA;
    CheckBox chkServiceA;
    LinearLayout secServiceB;
    TextView VlblServiceB;
    CheckBox chkServiceB;
    LinearLayout secServiceC;
    TextView VlblServiceC;
    CheckBox chkServiceC;
    LinearLayout secServiceD;
    TextView VlblServiceD;
    CheckBox chkServiceD;
    LinearLayout secServiceE;
    TextView VlblServiceE;
    CheckBox chkServiceE;
    LinearLayout secServiceF;
    TextView VlblServiceF;
    CheckBox chkServiceF;
    LinearLayout secServiceG;
    TextView VlblServiceG;
    CheckBox chkServiceG;
    LinearLayout secServiceH;
    TextView VlblServiceH;
    CheckBox chkServiceH;
    LinearLayout secServiceX;
    TextView VlblServiceX;
    CheckBox chkServiceX;
    LinearLayout secServiceOth;
    TextView VlblServiceOth;
    EditText txtServiceOth;

    Spinner spnCause1;
    Spinner spnCause2;
    Spinner spnCause3;
    Spinner spnCause4;

    LinearLayout secTreatment;

    String StartTime;
    String ChildID;
    String WeekNo;
    String VisitType;
    String VisitNo;
    String CID;
    String PID;
    String Child_Outside_Area;

    TextView lblVisit;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            setContentView(R.layout.noncomp);
            C = new Connection(this);
            g = Global.getInstance();
            StartTime = g.CurrentTime24();

            TableName = "NonComp";

            Bundle B    = new Bundle();
            B	        = getIntent().getExtras();
            ChildID     = B.getString("childid");
            WeekNo      = B.getString("weekno");
            VisitType   = B.getString("visittype");
            VisitNo     = B.getString("visitno");
            CID         = B.getString("cid");
            PID         = B.getString("pid");
            Child_Outside_Area = B.getString("child_outside_area");

            lblVisit = (TextView)findViewById(R.id.lblVisit);

            if(VisitType.equals("1")) {
                lblVisit.setText("সাপ্তাহিক পরিদর্শন");
            }
            else if(VisitType.equals("2")) {
                lblVisit.setText("ফলোআপ পরিদর্শন");
            }
            else if(VisitType.equals("3")) {
                lblVisit.setText("অতিরিক্ত পরিদর্শন");
            }


            spnCause1 = (Spinner)findViewById(R.id.spnCause1);
            spnCause2 = (Spinner)findViewById(R.id.spnCause2);
            spnCause3 = (Spinner)findViewById(R.id.spnCause3);
            spnCause4 = (Spinner)findViewById(R.id.spnCause4);


            /*
            List<String> listCause = new ArrayList<String>();

            listCause.add("");
            listCause.add("01-মনে হয়েছে বাচ্চা সুস্থ/প্রয়োজন মনে করিনি 	");
            listCause.add("02-বাসা কুমুদিনী হাসপাতাল থেকে অনেক দূরে /যাতায়াতের  অসুবিধা	");
            listCause.add("03-সাথে নেয়ার মত কেউ ছিলনা	");
            listCause.add("04-টাকা ছিলনা/অনেক খরচ	");
            listCause.add("05-অনেকক্ষন অপেক্ষা করতে হয়	");
            listCause.add("06-আরেকটি ছোট বাচ্চা থাকার কারনে	");
            listCause.add("07-অনুমতি পাইনি	");
            listCause.add("08-কুমুদিনী হাসপাতালের চিকিৎসা ভাল না	");
            listCause.add("09-কুমুদিনী হাসপাতালের পূর্বের প্রেসক্রিপশন ছিল	");
            listCause.add("10-প্রাকৃতিক দুর্যোগ	");
            listCause.add("11-ঔষধ ফ্রি পাওয়া যায় না	");
            listCause.add("12-ঘরে ঔষধ ছিল (আগের প্রেসক্রিমশন দেখে ঔষধ খাওয়ানো হয়েছে	");
            listCause.add("13-হেমিওপ্যাথিক ঔষধ খাওয়ানো হয়েছে	");
            listCause.add("77-অন্যান্য	");

            ArrayAdapter<String> adptrCause= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listCause);
            spnCause1.setAdapter(adptrCause);
            */

            spnCause1.setAdapter(C.getArrayAdapter("Select '  ' union select varcode||'-'||vardes from CodeList where upper(fname)='NONCOMP' and upper(varname)='Q1A'"));
            spnCause2.setAdapter(C.getArrayAdapter("Select '  ' union select varcode||'-'||vardes from CodeList where upper(fname)='NONCOMP' and upper(varname)='Q1A'"));
            spnCause3.setAdapter(C.getArrayAdapter("Select '  ' union select varcode||'-'||vardes from CodeList where upper(fname)='NONCOMP' and upper(varname)='Q1A'"));
            spnCause4.setAdapter(C.getArrayAdapter("Select '  ' union select varcode||'-'||vardes from CodeList where upper(fname)='NONCOMP' and upper(varname)='Q1A'"));

            secAll = (LinearLayout)findViewById(R.id.secAll);
            secTreatment = (LinearLayout)findViewById(R.id.secTreatment);
            secChildId=(LinearLayout)findViewById(R.id.secChildId);
            VlblChildId=(TextView) findViewById(R.id.VlblChildId);
            txtChildId=(TextView) findViewById(R.id.txtChildId);
            txtChildId.setText(ChildID);
            secCID=(LinearLayout)findViewById(R.id.secCID);

            txtName = (TextView)findViewById(R.id.txtName);
            txtName.setText(B.getString("name"));
            txtWeek = (TextView)findViewById(R.id.txtWeek);
            txtWeek.setText(WeekNo);
            txtVisit = (TextView)findViewById(R.id.txtVisit);
            txtVisit.setText(VisitNo);

            secVDate=(LinearLayout)findViewById(R.id.secVDate);
            VlblVDate=(TextView) findViewById(R.id.VlblVDate);
            dtpVDate=(EditText) findViewById(R.id.dtpVDate);
            dtpVDate.setText(Global.DateNowDMY());

            secRefResult=(LinearLayout)findViewById(R.id.secRefResult);
            VlblRefResult = (TextView) findViewById(R.id.VlblRefResult);
            rdogrpRefResult = (RadioGroup) findViewById(R.id.rdogrpRefResult);

            rdoRefResult1 = (RadioButton) findViewById(R.id.rdoRefResult1);
            rdoRefResult2 = (RadioButton) findViewById(R.id.rdoRefResult2);
            rdogrpRefResult.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int radioButtonID) {

                    if (rdoRefResult1.isChecked()) {
                        secAll.setVisibility(View.GONE);

                        seclbl1.setVisibility(View.GONE);
                        seclblPro.setVisibility(View.GONE);
                        seclbl22.setVisibility(View.GONE);
                        seclbl23.setVisibility(View.GONE);

                        secVisitOthYN.setVisibility(View.GONE);
                        rdogrpVisitOthYN.clearCheck();
                        secProvider1.setVisibility(View.GONE);
                        spnProvider1.setSelection(0);
                        secProvider2.setVisibility(View.GONE);
                        spnProvider2.setSelection(0);
                        secProvider3.setVisibility(View.GONE);
                        spnProvider3.setSelection(0);
                        secProvider4.setVisibility(View.GONE);
                        spnProvider4.setSelection(0);
                        secProviderOth1.setVisibility(View.GONE);
                        txtProviderOth1.setText("");
                        secPrescrip.setVisibility(View.GONE);
                        rdogrpPrescrip.clearCheck();
                        secRefA.setVisibility(View.GONE);
                        chkRefA.setChecked(false);
                        secRefB.setVisibility(View.GONE);
                        chkRefB.setChecked(false);
                        secRefC.setVisibility(View.GONE);
                        chkRefC.setChecked(false);
                        secRefD.setVisibility(View.GONE);
                        chkRefD.setChecked(false);
                        secRefE.setVisibility(View.GONE);
                        chkRefE.setChecked(false);
                        secRefF.setVisibility(View.GONE);
                        chkRefF.setChecked(false);
                        secRefG.setVisibility(View.GONE);
                        chkRefG.setChecked(false);
                        secRefH.setVisibility(View.GONE);
                        chkRefH.setChecked(false);
                        secRefI.setVisibility(View.GONE);
                        chkRefI.setChecked(false);
                        secRefX.setVisibility(View.GONE);
                        chkRefX.setChecked(false);
                        secRefOth.setVisibility(View.GONE);
                        txtRefOth.setText("");
                        secServiceA.setVisibility(View.GONE);
                        chkServiceA.setChecked(false);
                        secServiceB.setVisibility(View.GONE);
                        chkServiceB.setChecked(false);
                        secServiceC.setVisibility(View.GONE);
                        chkServiceC.setChecked(false);
                        secServiceD.setVisibility(View.GONE);
                        chkServiceD.setChecked(false);
                        secServiceE.setVisibility(View.GONE);
                        chkServiceE.setChecked(false);
                        secServiceF.setVisibility(View.GONE);
                        chkServiceF.setChecked(false);
                        secServiceG.setVisibility(View.GONE);
                        chkServiceG.setChecked(false);
                        secServiceH.setVisibility(View.GONE);
                        chkServiceH.setChecked(false);
                        secServiceX.setVisibility(View.GONE);
                        chkServiceX.setChecked(false);
                        secServiceOth.setVisibility(View.GONE);
                        txtServiceOth.setText("");

                    } else {
                        secAll.setVisibility(View.VISIBLE);
                        seclbl1.setVisibility(View.VISIBLE);
                        seclblPro.setVisibility(View.VISIBLE);
                        seclbl22.setVisibility(View.VISIBLE);
                        seclbl23.setVisibility(View.VISIBLE);

                        //secCausOth.setVisibility(View.VISIBLE);
                        secVisitOthYN.setVisibility(View.VISIBLE);
                        secProvider1.setVisibility(View.VISIBLE);
                        secProvider2.setVisibility(View.VISIBLE);
                        secProvider3.setVisibility(View.VISIBLE);
                        secProvider4.setVisibility(View.VISIBLE);
                        //secProviderOth1.setVisibility(View.VISIBLE);
                        secPrescrip.setVisibility(View.VISIBLE);
                        secRefA.setVisibility(View.VISIBLE);
                        secRefB.setVisibility(View.VISIBLE);
                        secRefC.setVisibility(View.VISIBLE);
                        secRefD.setVisibility(View.VISIBLE);
                        secRefE.setVisibility(View.VISIBLE);
                        secRefF.setVisibility(View.VISIBLE);
                        secRefG.setVisibility(View.VISIBLE);
                        secRefH.setVisibility(View.VISIBLE);
                        secRefI.setVisibility(View.VISIBLE);
                        secRefX.setVisibility(View.VISIBLE);
                        //secRefOth.setVisibility(View.VISIBLE);
                        secServiceA.setVisibility(View.VISIBLE);
                        secServiceB.setVisibility(View.VISIBLE);
                        secServiceC.setVisibility(View.VISIBLE);
                        secServiceD.setVisibility(View.VISIBLE);
                        secServiceE.setVisibility(View.VISIBLE);
                        secServiceF.setVisibility(View.VISIBLE);
                        secServiceG.setVisibility(View.VISIBLE);
                        secServiceH.setVisibility(View.VISIBLE);
                        secServiceX.setVisibility(View.VISIBLE);
                        //secServiceOth.setVisibility(View.VISIBLE);
                        //secServiceOth.setVisibility(View.VISIBLE);
                    }
                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                    return;
                }
            });
            seclbl1=(LinearLayout)findViewById(R.id.seclbl1);

            secVisitOthYN=(LinearLayout)findViewById(R.id.secVisitOthYN);
            VlblVisitOthYN = (TextView) findViewById(R.id.VlblVisitOthYN);
            rdogrpVisitOthYN = (RadioGroup) findViewById(R.id.rdogrpVisitOthYN);

            rdoVisitOthYN1 = (RadioButton) findViewById(R.id.rdoVisitOthYN1);
            rdoVisitOthYN2 = (RadioButton) findViewById(R.id.rdoVisitOthYN2);
            rdogrpVisitOthYN.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(RadioGroup radioGroup,int radioButtonID) {
                    RadioButton rb = (RadioButton)findViewById(rdogrpVisitOthYN.getCheckedRadioButtonId());
                    if (rb == null) return;
                    String rbData = Global.Left(rb.getText().toString(),1);
                    if(rbData.equalsIgnoreCase("2"))
                    {
                        secTreatment.setVisibility(View.GONE);
                        secProvider1.setVisibility(View.GONE);
                        spnProvider1.setSelection(0);
                        secProvider2.setVisibility(View.GONE);
                        spnProvider2.setSelection(0);
                        secProvider3.setVisibility(View.GONE);
                        spnProvider3.setSelection(0);
                        secProvider4.setVisibility(View.GONE);
                        spnProvider4.setSelection(0);
                        secProviderOth1.setVisibility(View.GONE);
                        txtProviderOth1.setText("");
                        secPrescrip.setVisibility(View.GONE);
                        rdogrpPrescrip.clearCheck();
                        secRefA.setVisibility(View.GONE);
                        chkRefA.setChecked(false);
                        secRefB.setVisibility(View.GONE);
                        chkRefB.setChecked(false);
                        secRefC.setVisibility(View.GONE);
                        chkRefC.setChecked(false);
                        secRefD.setVisibility(View.GONE);
                        chkRefD.setChecked(false);
                        secRefE.setVisibility(View.GONE);
                        chkRefE.setChecked(false);
                        secRefF.setVisibility(View.GONE);
                        chkRefF.setChecked(false);
                        secRefG.setVisibility(View.GONE);
                        chkRefG.setChecked(false);
                        secRefH.setVisibility(View.GONE);
                        chkRefH.setChecked(false);
                        secRefI.setVisibility(View.GONE);
                        chkRefI.setChecked(false);
                        secRefX.setVisibility(View.GONE);
                        chkRefX.setChecked(false);
                        secRefOth.setVisibility(View.GONE);
                        txtRefOth.setText("");
                        secServiceA.setVisibility(View.GONE);
                        chkServiceA.setChecked(false);
                        secServiceB.setVisibility(View.GONE);
                        chkServiceB.setChecked(false);
                        secServiceC.setVisibility(View.GONE);
                        chkServiceC.setChecked(false);
                        secServiceD.setVisibility(View.GONE);
                        chkServiceD.setChecked(false);
                        secServiceE.setVisibility(View.GONE);
                        chkServiceE.setChecked(false);
                        secServiceF.setVisibility(View.GONE);
                        chkServiceF.setChecked(false);
                        secServiceG.setVisibility(View.GONE);
                        chkServiceG.setChecked(false);
                        secServiceH.setVisibility(View.GONE);
                        chkServiceH.setChecked(false);
                        secServiceX.setVisibility(View.GONE);
                        chkServiceX.setChecked(false);
                    }
                    else
                    {
                        secTreatment.setVisibility(View.VISIBLE);
                        secProvider1.setVisibility(View.VISIBLE);
                        secProvider2.setVisibility(View.VISIBLE);
                        secProvider3.setVisibility(View.VISIBLE);
                        secProvider4.setVisibility(View.VISIBLE);
                        secProviderOth1.setVisibility(View.VISIBLE);
                        secPrescrip.setVisibility(View.VISIBLE);
                        secRefA.setVisibility(View.VISIBLE);
                        secRefB.setVisibility(View.VISIBLE);
                        secRefC.setVisibility(View.VISIBLE);
                        secRefD.setVisibility(View.VISIBLE);
                        secRefE.setVisibility(View.VISIBLE);
                        secRefF.setVisibility(View.VISIBLE);
                        secRefG.setVisibility(View.VISIBLE);
                        secRefH.setVisibility(View.VISIBLE);
                        secRefI.setVisibility(View.VISIBLE);
                        secRefX.setVisibility(View.VISIBLE);
                        //secRefOth.setVisibility(View.VISIBLE);
                        secServiceA.setVisibility(View.VISIBLE);
                        secServiceB.setVisibility(View.VISIBLE);
                        secServiceC.setVisibility(View.VISIBLE);
                        secServiceD.setVisibility(View.VISIBLE);
                        secServiceE.setVisibility(View.VISIBLE);
                        secServiceF.setVisibility(View.VISIBLE);
                        secServiceG.setVisibility(View.VISIBLE);
                        secServiceH.setVisibility(View.VISIBLE);
                        secServiceX.setVisibility(View.VISIBLE);
                        //secServiceOth.setVisibility(View.VISIBLE);
                    }
                }
                public void onNothingSelected(AdapterView<?> adapterView) {
                    return;
                }
            });
            seclblPro=(LinearLayout)findViewById(R.id.seclblPro);
            secProvider1=(LinearLayout)findViewById(R.id.secProvider1);
            VlblProvider1=(TextView) findViewById(R.id.VlblProvider1);
            spnProvider1=(Spinner) findViewById(R.id.spnProvider1);
            List<String> listProvider = new ArrayList<String>();

            listProvider.add("");
            listProvider.add("11-উপজেলা স্বাস্থ্য কমপ্লেক্স ");
            listProvider.add("12-এফডাব্লিউসি/উপ-স্বাস্থ্যকেন্দ্র ");
            listProvider.add("13-কমিউনিটি ক্লিনিক  14-প্রাইভেট ক্লিনিক ");
            listProvider.add("15-এমবিবিএস ডাক্তার (চেম্বার) ");
            listProvider.add("16-স্যাকমো (SACMO)");
            listProvider.add("17-গ্রাম্য ডাক্তার");
            listProvider.add("18-ঔষধরে দোকান");
            listProvider.add("19-হোমিওপ্যাথ");
            listProvider.add("20-কবিরাজ");
            listProvider.add("21-চ্যারিটেবল হেলথ্ সেন্টার/এনজিও ক্লিনিক");
            listProvider.add("77-অন্যান্য");
            ArrayAdapter<String> adptrProvider= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listProvider);

            secProvider2=(LinearLayout)findViewById(R.id.secProvider2);
            VlblProvider2=(TextView) findViewById(R.id.VlblProvider2);
            spnProvider2=(Spinner) findViewById(R.id.spnProvider2);

            secProvider3=(LinearLayout)findViewById(R.id.secProvider3);
            VlblProvider3=(TextView) findViewById(R.id.VlblProvider3);
            spnProvider3=(Spinner) findViewById(R.id.spnProvider3);

            secProvider4=(LinearLayout)findViewById(R.id.secProvider4);
            VlblProvider4=(TextView) findViewById(R.id.VlblProvider4);
            spnProvider4=(Spinner) findViewById(R.id.spnProvider4);

            spnProvider1.setAdapter(adptrProvider);
            spnProvider2.setAdapter(adptrProvider);
            spnProvider3.setAdapter(adptrProvider);
            spnProvider4.setAdapter(adptrProvider);

            secProviderOth1=(LinearLayout)findViewById(R.id.secProviderOth1);
            VlblProviderOth1=(TextView) findViewById(R.id.VlblProviderOth1);
            txtProviderOth1=(EditText) findViewById(R.id.txtProviderOth1);
            secPrescrip=(LinearLayout)findViewById(R.id.secPrescrip);
            VlblPrescrip = (TextView) findViewById(R.id.VlblPrescrip);
            rdogrpPrescrip = (RadioGroup) findViewById(R.id.rdogrpPrescrip);

            rdoPrescrip1 = (RadioButton) findViewById(R.id.rdoPrescrip1);
            rdoPrescrip2 = (RadioButton) findViewById(R.id.rdoPrescrip2);
            rdoPrescrip3 = (RadioButton) findViewById(R.id.rdoPrescrip3);
            rdoPrescrip4 = (RadioButton) findViewById(R.id.rdoPrescrip4);
            seclbl22=(LinearLayout)findViewById(R.id.seclbl22);
            secRefA=(LinearLayout)findViewById(R.id.secRefA);
            VlblRefA=(TextView) findViewById(R.id.VlblRefA);
            chkRefA=(CheckBox) findViewById(R.id.chkRefA);
            secRefB=(LinearLayout)findViewById(R.id.secRefB);
            VlblRefB=(TextView) findViewById(R.id.VlblRefB);
            chkRefB=(CheckBox) findViewById(R.id.chkRefB);
            secRefC=(LinearLayout)findViewById(R.id.secRefC);
            VlblRefC=(TextView) findViewById(R.id.VlblRefC);
            chkRefC=(CheckBox) findViewById(R.id.chkRefC);
            secRefD=(LinearLayout)findViewById(R.id.secRefD);
            VlblRefD=(TextView) findViewById(R.id.VlblRefD);
            chkRefD=(CheckBox) findViewById(R.id.chkRefD);
            secRefE=(LinearLayout)findViewById(R.id.secRefE);
            VlblRefE=(TextView) findViewById(R.id.VlblRefE);
            chkRefE=(CheckBox) findViewById(R.id.chkRefE);
            secRefF=(LinearLayout)findViewById(R.id.secRefF);
            VlblRefF=(TextView) findViewById(R.id.VlblRefF);
            chkRefF=(CheckBox) findViewById(R.id.chkRefF);
            secRefG=(LinearLayout)findViewById(R.id.secRefG);
            VlblRefG=(TextView) findViewById(R.id.VlblRefG);
            chkRefG=(CheckBox) findViewById(R.id.chkRefG);
            secRefH=(LinearLayout)findViewById(R.id.secRefH);
            VlblRefH=(TextView) findViewById(R.id.VlblRefH);
            chkRefH=(CheckBox) findViewById(R.id.chkRefH);
            secRefI=(LinearLayout)findViewById(R.id.secRefI);
            VlblRefI=(TextView) findViewById(R.id.VlblRefI);
            chkRefI=(CheckBox) findViewById(R.id.chkRefI);
            secRefX=(LinearLayout)findViewById(R.id.secRefX);
            VlblRefX=(TextView) findViewById(R.id.VlblRefX);
            chkRefX=(CheckBox) findViewById(R.id.chkRefX);
            chkRefX.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    if(!((CheckBox) v).isChecked())
                    {
                        secRefOth.setVisibility(View.GONE);
                        txtRefOth.setText("");
                        secServiceA.setVisibility(View.GONE);
                        chkServiceA.setChecked(false);
                    }
                }
            });
            secRefOth=(LinearLayout)findViewById(R.id.secRefOth);
            VlblRefOth=(TextView) findViewById(R.id.VlblRefOth);
            txtRefOth=(EditText) findViewById(R.id.txtRefOth);
            seclbl23=(LinearLayout)findViewById(R.id.seclbl23);
            secServiceA=(LinearLayout)findViewById(R.id.secServiceA);
            VlblServiceA=(TextView) findViewById(R.id.VlblServiceA);
            chkServiceA=(CheckBox) findViewById(R.id.chkServiceA);
            secServiceB=(LinearLayout)findViewById(R.id.secServiceB);
            VlblServiceB=(TextView) findViewById(R.id.VlblServiceB);
            chkServiceB=(CheckBox) findViewById(R.id.chkServiceB);
            secServiceC=(LinearLayout)findViewById(R.id.secServiceC);
            VlblServiceC=(TextView) findViewById(R.id.VlblServiceC);
            chkServiceC=(CheckBox) findViewById(R.id.chkServiceC);
            secServiceD=(LinearLayout)findViewById(R.id.secServiceD);
            VlblServiceD=(TextView) findViewById(R.id.VlblServiceD);
            chkServiceD=(CheckBox) findViewById(R.id.chkServiceD);
            secServiceE=(LinearLayout)findViewById(R.id.secServiceE);
            VlblServiceE=(TextView) findViewById(R.id.VlblServiceE);
            chkServiceE=(CheckBox) findViewById(R.id.chkServiceE);
            secServiceF=(LinearLayout)findViewById(R.id.secServiceF);
            VlblServiceF=(TextView) findViewById(R.id.VlblServiceF);
            chkServiceF=(CheckBox) findViewById(R.id.chkServiceF);
            secServiceG=(LinearLayout)findViewById(R.id.secServiceG);
            VlblServiceG=(TextView) findViewById(R.id.VlblServiceG);
            chkServiceG=(CheckBox) findViewById(R.id.chkServiceG);
            secServiceH=(LinearLayout)findViewById(R.id.secServiceH);
            VlblServiceH=(TextView) findViewById(R.id.VlblServiceH);
            chkServiceH=(CheckBox) findViewById(R.id.chkServiceH);
            secServiceX=(LinearLayout)findViewById(R.id.secServiceX);
            VlblServiceX=(TextView) findViewById(R.id.VlblServiceX);
            chkServiceX=(CheckBox) findViewById(R.id.chkServiceX);
            chkServiceX.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    if(!((CheckBox) v).isChecked())
                    {
                        secServiceOth.setVisibility(View.GONE);
                        txtServiceOth.setText("");
                    }
                }
            });
            secServiceOth=(LinearLayout)findViewById(R.id.secServiceOth);
            VlblServiceOth=(TextView) findViewById(R.id.VlblServiceOth);
            txtServiceOth=(EditText) findViewById(R.id.txtServiceOth);



            btnVDate = (ImageButton) findViewById(R.id.btnVDate);
            btnVDate.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) { VariableID = "btnVDate"; showDialog(DATE_DIALOG); }});




            Button cmdSave = (Button) findViewById(R.id.cmdSave);
            cmdSave.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    DataSave();
                }});


            secAll.setVisibility(View.GONE);

            seclbl1.setVisibility(View.GONE);
            seclblPro.setVisibility(View.GONE);
            seclbl22.setVisibility(View.GONE);
            seclbl23.setVisibility(View.GONE);

            secVisitOthYN.setVisibility(View.GONE);
            rdogrpVisitOthYN.clearCheck();
            secProvider1.setVisibility(View.GONE);
            spnProvider1.setSelection(0);
            secProvider2.setVisibility(View.GONE);
            spnProvider2.setSelection(0);
            secProvider3.setVisibility(View.GONE);
            spnProvider3.setSelection(0);
            secProvider4.setVisibility(View.GONE);
            spnProvider4.setSelection(0);
            secProviderOth1.setVisibility(View.GONE);
            txtProviderOth1.setText("");
            secPrescrip.setVisibility(View.GONE);
            rdogrpPrescrip.clearCheck();
            secRefA.setVisibility(View.GONE);
            chkRefA.setChecked(false);
            secRefB.setVisibility(View.GONE);
            chkRefB.setChecked(false);
            secRefC.setVisibility(View.GONE);
            chkRefC.setChecked(false);
            secRefD.setVisibility(View.GONE);
            chkRefD.setChecked(false);
            secRefE.setVisibility(View.GONE);
            chkRefE.setChecked(false);
            secRefF.setVisibility(View.GONE);
            chkRefF.setChecked(false);
            secRefG.setVisibility(View.GONE);
            chkRefG.setChecked(false);
            secRefH.setVisibility(View.GONE);
            chkRefH.setChecked(false);
            secRefI.setVisibility(View.GONE);
            chkRefI.setChecked(false);
            secRefX.setVisibility(View.GONE);
            chkRefX.setChecked(false);
            secRefOth.setVisibility(View.GONE);
            txtRefOth.setText("");
            secServiceA.setVisibility(View.GONE);
            chkServiceA.setChecked(false);
            secServiceB.setVisibility(View.GONE);
            chkServiceB.setChecked(false);
            secServiceC.setVisibility(View.GONE);
            chkServiceC.setChecked(false);
            secServiceD.setVisibility(View.GONE);
            chkServiceD.setChecked(false);
            secServiceE.setVisibility(View.GONE);
            chkServiceE.setChecked(false);
            secServiceF.setVisibility(View.GONE);
            chkServiceF.setChecked(false);
            secServiceG.setVisibility(View.GONE);
            chkServiceG.setChecked(false);
            secServiceH.setVisibility(View.GONE);
            chkServiceH.setChecked(false);
            secServiceX.setVisibility(View.GONE);
            chkServiceX.setChecked(false);
            secServiceOth.setVisibility(View.GONE);
            txtServiceOth.setText("");

            DataSearch(ChildID,WeekNo,VisitType,VisitNo);
        }
        catch(Exception  e)
        {
            Connection.MessageBox(NonComp.this, e.getMessage());
            return;
        }
    }

    private void DataSave()
    {
        try
        {
            String DV="";

            if(txtChildId.getText().toString().length()==0)
            {
                Connection.MessageBox(NonComp.this, "Required field:Child ID.");
                txtChildId.requestFocus();
                return;
            }

            DV = Global.DateValidate(dtpVDate.getText().toString());
            if(DV.length()!=0 & secVDate.isShown())
            {
                Connection.MessageBox(NonComp.this, DV);
                dtpVDate.requestFocus();
                return;
            }

            else if(!rdoRefResult1.isChecked() & !rdoRefResult2.isChecked() & secRefResult.isShown())
            {
                Connection.MessageBox(NonComp.this, "Select anyone options from RefResult.");
                //rdoRefResult1.requestFocus();
                return;
            }
            /*else if(spnCausOth.getSelectedItemPosition()==0  & secCausOth.isShown())
            {
                Connection.MessageBox(NonComp.this, "Required field:অন্যান্য.");
                spnCausOth.requestFocus();
                return;
            }*/

            else if(!rdoVisitOthYN1.isChecked() & !rdoVisitOthYN2.isChecked() & secVisitOthYN.isShown())
            {
                Connection.MessageBox(NonComp.this, "Select anyone options from VisitOthYN.");
                rdoVisitOthYN1.requestFocus();
                return;
            }
            else if(spnProvider1.getSelectedItemPosition()==0  & secProvider1.isShown())
            {
                Connection.MessageBox(NonComp.this, "Required field:১ম ভিজিট.");
                spnProvider1.requestFocus();
                return;
            }
            /*else if(spnProvider2.getSelectedItemPosition()==0  & secProvider2.isShown())
            {
                Connection.MessageBox(NonComp.this, "Required field:২য় ভিজিট.");
                spnProvider2.requestFocus();
                return;
            }
            else if(spnProvider3.getSelectedItemPosition()==0  & secProvider3.isShown())
            {
                Connection.MessageBox(NonComp.this, "Required field:৩য় ভিজিট.");
                spnProvider3.requestFocus();
                return;
            }
            else if(spnProvider4.getSelectedItemPosition()==0  & secProvider4.isShown())
            {
                Connection.MessageBox(NonComp.this, "Required field:৪র্থ ভিজিট.");
                spnProvider4.requestFocus();
                return;
            }*/
            /*else if(txtProviderOth1.getText().toString().length()==0 & secProviderOth1.isShown())
            {
                Connection.MessageBox(NonComp.this, "Required field:অন্যান্য.");
                txtProviderOth1.requestFocus();
                return;
            }

            else if(!rdoPrescrip1.isChecked() & !rdoPrescrip2.isChecked() & !rdoPrescrip3.isChecked() & secPrescrip.isShown())
            {
                Connection.MessageBox(NonComp.this, "Select anyone options from Prescrip.");
                rdoPrescrip1.requestFocus();
                return;
            }
            else if(txtRefOth.getText().toString().length()==0 & secRefOth.isShown())
            {
                Connection.MessageBox(NonComp.this, "Required field:অন্যান্য (উল্লেখ করুন).");
                txtRefOth.requestFocus();
                return;
            }
            else if(txtServiceOth.getText().toString().length()==0 & secServiceOth.isShown())
            {
                Connection.MessageBox(NonComp.this, "Required field:অন্যান্য (উল্লেখ করুন).");
                txtServiceOth.requestFocus();
                return;
            }
            */


            String SQL = "";

            if(!C.Existence("Select ChildId,CID,Week,StartTime,EndTime,UserId,EnDt,Upload from " + TableName + "  Where ChildId='"+ txtChildId.getText().toString() +"' and CID='"+ CID +"' and Week='"+ WeekNo +"' and VType='"+ VisitType +"' and Visit='"+ VisitNo +"'"))
            {
                SQL = "Insert into " + TableName + "(ChildId,CID,Week,VType,Visit,StartTime,EndTime,UserId,EnDt,Upload)Values('"+ txtChildId.getText() +"','"+ CID +"','"+ WeekNo +"','"+ VisitType +"','"+ VisitNo +"','"+ StartTime +"','"+ g.CurrentTime24() +"','"+ g.getUserId() +"','"+ Global.DateTimeNowYMDHMS() +"','2')";
                C.Save(SQL);
            }

            SQL = "Update " + TableName + " Set upload='2',";
            //SQL+="ChildId = '"+ txtChildId.getText().toString() +"',";
            SQL+="CID = '"+ CID +"',";
            SQL+="PID = '"+ PID +"',";
            SQL+="Week = '"+ WeekNo +"',";
            SQL+="VType = '"+ VisitType +"',";
            SQL+="Visit = '"+ VisitNo +"',";
            SQL+="VDate = '"+ Global.DateConvertYMD(dtpVDate.getText().toString()) +"',";

            if(rdoRefResult1.isChecked())
                SQL+="RefResult = '1',";
            else if(rdoRefResult2.isChecked())
                SQL+="RefResult = '2',";

            String C1 = spnCause1.getSelectedItemPosition()==0?"":Global.Left(spnCause1.getSelectedItem().toString(),2);
            String C2 = spnCause2.getSelectedItemPosition()==0?"":Global.Left(spnCause2.getSelectedItem().toString(),2);
            String C3 = spnCause3.getSelectedItemPosition()==0?"":Global.Left(spnCause3.getSelectedItem().toString(),2);
            String C4 = spnCause4.getSelectedItemPosition()==0?"":Global.Left(spnCause4.getSelectedItem().toString(),2);

            SQL+="Q1a = '"+ C1 +"',";
            SQL+="Q1b = '"+ C2 +"',";
            SQL+="Q1c = '"+ C3 +"',";
            SQL+="Q1d = '"+ C4 +"',";

            RadioButton rbVisitOthYN = (RadioButton)findViewById(rdogrpVisitOthYN.getCheckedRadioButtonId());
            SQL+="VisitOthYN = '"+ (rbVisitOthYN==null?"":(Global.Left(rbVisitOthYN.getText().toString(),1))) +"',";
            SQL+="Provider1 = '"+ (spnProvider1.getSelectedItemPosition()==0?"":Global.Left(spnProvider1.getSelectedItem().toString(),2)) +"',";
            SQL+="Provider2 = '"+ (spnProvider2.getSelectedItemPosition()==0?"":Global.Left(spnProvider2.getSelectedItem().toString(),2)) +"',";
            SQL+="Provider3 = '"+ (spnProvider3.getSelectedItemPosition()==0?"":Global.Left(spnProvider3.getSelectedItem().toString(),2)) +"',";
            SQL+="Provider4 = '"+ (spnProvider4.getSelectedItemPosition()==0?"":Global.Left(spnProvider4.getSelectedItem().toString(),2)) +"',";
            SQL+="ProviderOth1 = '"+ txtProviderOth1.getText().toString() +"',";
            RadioButton rbPrescrip = (RadioButton)findViewById(rdogrpPrescrip.getCheckedRadioButtonId());
            SQL+="Prescrip = '"+ (rbPrescrip==null?"":(Global.Left(rbPrescrip.getText().toString(),1))) +"',";
            SQL+="RefA = '"+ (chkRefA.isChecked()?"1":"2") +"',";
            SQL+="RefB = '"+ (chkRefB.isChecked()?"1":"2") +"',";
            SQL+="RefC = '"+ (chkRefC.isChecked()?"1":"2") +"',";
            SQL+="RefD = '"+ (chkRefD.isChecked()?"1":"2") +"',";
            SQL+="RefE = '"+ (chkRefE.isChecked()?"1":"2") +"',";
            SQL+="RefF = '"+ (chkRefF.isChecked()?"1":"2") +"',";
            SQL+="RefG = '"+ (chkRefG.isChecked()?"1":"2") +"',";
            SQL+="RefH = '"+ (chkRefH.isChecked()?"1":"2") +"',";
            SQL+="RefI = '"+ (chkRefI.isChecked()?"1":"2") +"',";
            SQL+="RefX = '"+ (chkRefX.isChecked()?"1":"2") +"',";
            SQL+="RefOth = '"+ txtRefOth.getText().toString() +"',";
            SQL+="ServiceA = '"+ (chkServiceA.isChecked()?"1":"2") +"',";
            SQL+="ServiceB = '"+ (chkServiceB.isChecked()?"1":"2") +"',";
            SQL+="ServiceC = '"+ (chkServiceC.isChecked()?"1":"2") +"',";
            SQL+="ServiceD = '"+ (chkServiceD.isChecked()?"1":"2") +"',";
            SQL+="ServiceE = '"+ (chkServiceE.isChecked()?"1":"2") +"',";
            SQL+="ServiceF = '"+ (chkServiceF.isChecked()?"1":"2") +"',";
            SQL+="ServiceG = '"+ (chkServiceG.isChecked()?"1":"2") +"',";
            SQL+="ServiceH = '"+ (chkServiceH.isChecked()?"1":"2") +"',";
            SQL+="ServiceX = '"+ (chkServiceX.isChecked()?"1":"2") +"',";
            SQL+="ServiceOth = '"+ txtServiceOth.getText().toString() +"'";
            SQL+="  Where ChildId='"+ txtChildId.getText().toString() +"' and Week='"+ WeekNo +"' and VType='"+ VisitType +"' and Visit='"+ VisitNo +"'";
            C.Save(SQL);

            //Referral Complience Information
            if(Child_Outside_Area.equals("n")) {
                if (VisitType.equals("1"))
                    C.Save("Update Child set Upload='2',Referral='' where ChildId='" + ChildID + "'");
                else if (VisitType.equals("2"))
                    C.Save("Update Child set Upload='2',Referral_Foll='' where ChildId='" + ChildID + "'");
                else if (VisitType.equals("3"))
                    C.Save("Update Child set Upload='2',Referral_Add='' where ChildId='" + ChildID + "'");
            }
            //29 may 2016
            else if(Child_Outside_Area.equals("y")) {
                if (VisitType.equals("1"))
                    C.Save("Update Child_Outside set Upload='2',Referral='' where ChildId='" + ChildID + "'");
                else if (VisitType.equals("2"))
                    C.Save("Update Child_Outside set Upload='2',Referral_Foll='' where ChildId='" + ChildID + "'");
                else if (VisitType.equals("3"))
                    C.Save("Update Child_Outside set Upload='2',Referral_Add='' where ChildId='" + ChildID + "'");
            }

            Connection.MessageBox(NonComp.this, "Saved Successfully");
        }
        catch(Exception  e)
        {
            Connection.MessageBox(NonComp.this, e.getMessage());
            return;
        }
    }
    private void DataSearch(String ChildId, String WeekNo, String VisitType, String VisitNo)
    {
        try
        {

            RadioButton rb;
            Cursor cur = C.ReadData("Select * from "+ TableName +"  Where ChildId='"+ ChildId +"' and Week='"+ WeekNo +"' and VType='"+ VisitType +"' and Visit='"+ VisitNo +"'");
            cur.moveToFirst();
            while(!cur.isAfterLast())
            {
                txtChildId.setText(cur.getString(cur.getColumnIndex("ChildId")));
                txtWeek.setText(cur.getString(cur.getColumnIndex("Week")));
                txtVisit.setText(cur.getString(cur.getColumnIndex("Visit")));
                dtpVDate.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("VDate"))));

                if(cur.getString(cur.getColumnIndex("RefResult")).equals("1"))
                    rdoRefResult1.setChecked(true);
                else if(cur.getString(cur.getColumnIndex("RefResult")).equals("2"))
                    rdoRefResult2.setChecked(true);

                spnCause1.setSelection(Global.SpinnerItemPosition(spnCause1, 2 ,cur.getString(cur.getColumnIndex("Q1a"))));
                spnCause2.setSelection(Global.SpinnerItemPosition(spnCause2, 2 ,cur.getString(cur.getColumnIndex("Q1b"))));
                spnCause3.setSelection(Global.SpinnerItemPosition(spnCause3, 2 ,cur.getString(cur.getColumnIndex("Q1c"))));
                spnCause4.setSelection(Global.SpinnerItemPosition(spnCause4, 2 ,cur.getString(cur.getColumnIndex("Q1d"))));

                for (int i = 0; i < rdogrpVisitOthYN.getChildCount(); i++)
                {
                    rb = (RadioButton)rdogrpVisitOthYN.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("VisitOthYN"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                spnProvider1.setSelection(Global.SpinnerItemPosition(spnProvider1, 2 ,cur.getString(cur.getColumnIndex("Provider1"))));
                spnProvider2.setSelection(Global.SpinnerItemPosition(spnProvider2, 2 ,cur.getString(cur.getColumnIndex("Provider2"))));
                spnProvider3.setSelection(Global.SpinnerItemPosition(spnProvider3, 2 ,cur.getString(cur.getColumnIndex("Provider3"))));
                spnProvider4.setSelection(Global.SpinnerItemPosition(spnProvider4, 2 ,cur.getString(cur.getColumnIndex("Provider4"))));
                txtProviderOth1.setText(cur.getString(cur.getColumnIndex("ProviderOth1")));
                for (int i = 0; i < rdogrpPrescrip.getChildCount(); i++)
                {
                    rb = (RadioButton)rdogrpPrescrip.getChildAt(i);
                    if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Prescrip"))))
                        rb.setChecked(true);
                    else
                        rb.setChecked(false);
                }
                if(cur.getString(cur.getColumnIndex("RefA")).equals("1"))
                {
                    chkRefA.setChecked(true);
                }
                else if(cur.getString(cur.getColumnIndex("RefA")).equals("2"))
                {
                    chkRefA.setChecked(false);
                }
                if(cur.getString(cur.getColumnIndex("RefB")).equals("1"))
                {
                    chkRefB.setChecked(true);
                }
                else if(cur.getString(cur.getColumnIndex("RefB")).equals("2"))
                {
                    chkRefB.setChecked(false);
                }
                if(cur.getString(cur.getColumnIndex("RefC")).equals("1"))
                {
                    chkRefC.setChecked(true);
                }
                else if(cur.getString(cur.getColumnIndex("RefC")).equals("2"))
                {
                    chkRefC.setChecked(false);
                }
                if(cur.getString(cur.getColumnIndex("RefD")).equals("1"))
                {
                    chkRefD.setChecked(true);
                }
                else if(cur.getString(cur.getColumnIndex("RefD")).equals("2"))
                {
                    chkRefD.setChecked(false);
                }
                if(cur.getString(cur.getColumnIndex("RefE")).equals("1"))
                {
                    chkRefE.setChecked(true);
                }
                else if(cur.getString(cur.getColumnIndex("RefE")).equals("2"))
                {
                    chkRefE.setChecked(false);
                }
                if(cur.getString(cur.getColumnIndex("RefF")).equals("1"))
                {
                    chkRefF.setChecked(true);
                }
                else if(cur.getString(cur.getColumnIndex("RefF")).equals("2"))
                {
                    chkRefF.setChecked(false);
                }
                if(cur.getString(cur.getColumnIndex("RefG")).equals("1"))
                {
                    chkRefG.setChecked(true);
                }
                else if(cur.getString(cur.getColumnIndex("RefG")).equals("2"))
                {
                    chkRefG.setChecked(false);
                }
                if(cur.getString(cur.getColumnIndex("RefH")).equals("1"))
                {
                    chkRefH.setChecked(true);
                }
                else if(cur.getString(cur.getColumnIndex("RefH")).equals("2"))
                {
                    chkRefH.setChecked(false);
                }
                if(cur.getString(cur.getColumnIndex("RefI")).equals("1"))
                {
                    chkRefI.setChecked(true);
                }
                else if(cur.getString(cur.getColumnIndex("RefI")).equals("2"))
                {
                    chkRefI.setChecked(false);
                }
                if(cur.getString(cur.getColumnIndex("RefX")).equals("1"))
                {
                    chkRefX.setChecked(true);
                }
                else if(cur.getString(cur.getColumnIndex("RefX")).equals("2"))
                {
                    chkRefX.setChecked(false);
                }
                txtRefOth.setText(cur.getString(cur.getColumnIndex("RefOth")));
                if(cur.getString(cur.getColumnIndex("ServiceA")).equals("1"))
                {
                    chkServiceA.setChecked(true);
                }
                else if(cur.getString(cur.getColumnIndex("ServiceA")).equals("2"))
                {
                    chkServiceA.setChecked(false);
                }
                if(cur.getString(cur.getColumnIndex("ServiceB")).equals("1"))
                {
                    chkServiceB.setChecked(true);
                }
                else if(cur.getString(cur.getColumnIndex("ServiceB")).equals("2"))
                {
                    chkServiceB.setChecked(false);
                }
                if(cur.getString(cur.getColumnIndex("ServiceC")).equals("1"))
                {
                    chkServiceC.setChecked(true);
                }
                else if(cur.getString(cur.getColumnIndex("ServiceC")).equals("2"))
                {
                    chkServiceC.setChecked(false);
                }
                if(cur.getString(cur.getColumnIndex("ServiceD")).equals("1"))
                {
                    chkServiceD.setChecked(true);
                }
                else if(cur.getString(cur.getColumnIndex("ServiceD")).equals("2"))
                {
                    chkServiceD.setChecked(false);
                }
                if(cur.getString(cur.getColumnIndex("ServiceE")).equals("1"))
                {
                    chkServiceE.setChecked(true);
                }
                else if(cur.getString(cur.getColumnIndex("ServiceE")).equals("2"))
                {
                    chkServiceE.setChecked(false);
                }
                if(cur.getString(cur.getColumnIndex("ServiceF")).equals("1"))
                {
                    chkServiceF.setChecked(true);
                }
                else if(cur.getString(cur.getColumnIndex("ServiceF")).equals("2"))
                {
                    chkServiceF.setChecked(false);
                }
                if(cur.getString(cur.getColumnIndex("ServiceG")).equals("1"))
                {
                    chkServiceG.setChecked(true);
                }
                else if(cur.getString(cur.getColumnIndex("ServiceG")).equals("2"))
                {
                    chkServiceG.setChecked(false);
                }
                if(cur.getString(cur.getColumnIndex("ServiceH")).equals("1"))
                {
                    chkServiceH.setChecked(true);
                }
                else if(cur.getString(cur.getColumnIndex("ServiceH")).equals("2"))
                {
                    chkServiceH.setChecked(false);
                }
                if(cur.getString(cur.getColumnIndex("ServiceX")).equals("1"))
                {
                    chkServiceX.setChecked(true);
                }
                else if(cur.getString(cur.getColumnIndex("ServiceX")).equals("2"))
                {
                    chkServiceX.setChecked(false);
                }
                txtServiceOth.setText(cur.getString(cur.getColumnIndex("ServiceOth")));

                cur.moveToNext();
            }
            cur.close();
        }
        catch(Exception  e)
        {
            Connection.MessageBox(NonComp.this, e.getMessage());
            return;
        }
    }




    protected Dialog onCreateDialog(int id) {
        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        switch (id) {
            case DATE_DIALOG:
                return new DatePickerDialog(this, mDateSetListener,g.mYear,g.mMonth-1,g.mDay);
            case TIME_DIALOG:
                return new TimePickerDialog(this, timePickerListener, hour, minute,false);
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
}
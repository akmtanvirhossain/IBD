
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
    public boolean onKeyDown(int iKeyCode, KeyEvent event)
    {
        if(iKeyCode == KeyEvent.KEYCODE_BACK || iKeyCode == KeyEvent.KEYCODE_HOME) 
             { return false; }
        else { return true;  }
    }

    boolean networkAvailable=false;
    Location currentLocation; 
    double currentLatitude,currentLongitude; 
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
    LinearLayout secdtpCoughDt;
    View linedtpCoughDt;
    TextView VlbldtpCoughDt;
    EditText dtpdtpCoughDt;
    LinearLayout secdtpDBreaDt;
    View linedtpDBreaDt;
    TextView VlbldtpDBreaDt;
    EditText dtpdtpDBreaDt;
    LinearLayout secDeepColdDt;
    View lineDeepColdDt;
    TextView VlblDeepColdDt;
    EditText dtpDeepColdDt;
    LinearLayout secSoreThroatDt;
    View lineSoreThroatDt;
    TextView VlblSoreThroatDt;
    EditText dtpSoreThroatDt;
    LinearLayout secRSVsuitable;
    View lineRSVsuitable;
    TextView VlblRSVsuitable;
    RadioGroup rdogrpRSVsuitable;
    RadioButton rdoRSVsuitable1;
    RadioButton rdoRSVsuitable2;
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

    TextView txtFMName;
    TextView txtName;
    TextView Age;
    TextView Dob;

    static int MODULEID   = 0;
    static int LANGUAGEID = 0;
    static String TableName;

    static String STARTTIME = "";
    static String DEVICEID  = "";
    static String ENTRYUSER = "";
    MySharedPreferences sp;

    Bundle IDbundle;
    static String CHILDID = "";
    static String WEEK = "";
    static String VTYPE = "";
    static String VISITDate = "";
    static String BDATE = "";
    static String VISIT = "",FMName="",CHName="",PID="",AgeDM="",AgeD="",AgeM="",TEMP="";

 public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
   try
     {
         setContentView(R.layout.rsv);
         getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

         C = new Connection(this);
         g = Global.getInstance();

         STARTTIME = g.CurrentTime24();
         DEVICEID  = g.getUserId();
         ENTRYUSER = g.getUserId();

         IDbundle = getIntent().getExtras();
         CHILDID=IDbundle.getString("childid");
         PID=IDbundle.getString("pid");
         WEEK=IDbundle.getString("weekno");
         FMName=IDbundle.getString("fm");
         AgeD=IDbundle.getString("aged");
         AgeM=IDbundle.getString("agem");
         AgeDM=IDbundle.getString("agedm");
         BDATE=IDbundle.getString("bdate");
         CHName=IDbundle.getString("name");
         VTYPE=IDbundle.getString("visittype");
         VISIT=IDbundle.getString("visitno");
         VISITDate=IDbundle.getString("visitdate");
         TEMP=IDbundle.getString("temp");

         TableName = "RSV";

//         MODULEID = 0;
//         LANGUAGEID = Integer.parseInt(sp.getValue(this, "languageid"));
         lblHeading = (TextView)findViewById(R.id.lblHeading);

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
                     }});
                 adb.show();
             }});


        Initialization();
//        Connection.LocalizeLanguage(RSV.this, MODULEID, LANGUAGEID);


         dtpVDate.setOnTouchListener(new View.OnTouchListener() {
             @Override
             public boolean onTouch(View v, MotionEvent event) {
                 if(event.getAction() == MotionEvent.ACTION_UP) {
                      VariableID = "btnVDate"; showDialog(DATE_DIALOG);
                      return true;
                 }
                 return false;
             }
         });
         dtpdtpCoughDt.setOnTouchListener(new View.OnTouchListener() {
             @Override
             public boolean onTouch(View v, MotionEvent event) {
                 if(event.getAction() == MotionEvent.ACTION_UP) {
                      VariableID = "btndtpCoughDt"; showDialog(DATE_DIALOG);
                      return true;
                 }
                 return false;
             }
         });
         dtpdtpDBreaDt.setOnTouchListener(new View.OnTouchListener() {
             @Override
             public boolean onTouch(View v, MotionEvent event) {
                 if(event.getAction() == MotionEvent.ACTION_UP) {
                      VariableID = "btndtpDBreaDt"; showDialog(DATE_DIALOG);
                      return true;
                 }
                 return false;
             }
         });
         dtpDeepColdDt.setOnTouchListener(new View.OnTouchListener() {
             @Override
             public boolean onTouch(View v, MotionEvent event) {
                 if(event.getAction() == MotionEvent.ACTION_UP) {
                      VariableID = "btnDeepColdDt"; showDialog(DATE_DIALOG);
                      return true;
                 }
                 return false;
             }
         });
         dtpSoreThroatDt.setOnTouchListener(new View.OnTouchListener() {
             @Override
             public boolean onTouch(View v, MotionEvent event) {
                 if(event.getAction() == MotionEvent.ACTION_UP) {
                      VariableID = "btnSoreThroatDt"; showDialog(DATE_DIALOG);
                      return true;
                 }
                 return false;
             }
         });
         dtpRSVlistedDt.setOnTouchListener(new View.OnTouchListener() {
             @Override
             public boolean onTouch(View v, MotionEvent event) {
                 if(event.getAction() == MotionEvent.ACTION_UP) {
                      VariableID = "btnRSVlistedDt"; showDialog(DATE_DIALOG);
                      return true;
                 }
                 return false;
             }
         });



         //Hide all skip variables


        DataSearch(CHILDID,WEEK,VTYPE,VISIT);


        Button cmdSave = (Button) findViewById(R.id.cmdSave);
        cmdSave.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) { 
            DataSave();
        }});
     }
     catch(Exception  e)
     {
         Connection.MessageBox(RSV.this, e.getMessage());
         return;
     }
 }

 private void Initialization()
 {
   try
     {
//         ------------------------------------------------

         txtName=(TextView) findViewById(R.id.txtName);
         txtName.setText(CHName);
         txtName.setEnabled(false);
         txtFMName=(TextView) findViewById(R.id.txtFMName);
         txtFMName.setText(FMName);
         Age = (TextView)findViewById(R.id.Age);
         Age.setText(": "+ (AgeDM));
         Dob = (TextView)findViewById(R.id.Dob);
         Dob.setText(": "+ Global.DateConvertDMY(BDATE));

//         ------------------------------------------------
         seclbl1=(LinearLayout)findViewById(R.id.seclbl1);
         linelbl1=(View)findViewById(R.id.linelbl1);
         seclbl2=(LinearLayout)findViewById(R.id.seclbl2);
         linelbl2=(View)findViewById(R.id.linelbl2);
         secChildID=(LinearLayout)findViewById(R.id.secChildID);
         lineChildID=(View)findViewById(R.id.lineChildID);
         VlblChildID=(TextView) findViewById(R.id.VlblChildID);
         txtChildID=(EditText) findViewById(R.id.txtChildID);
         txtChildID.setText(CHILDID);
         txtChildID.setEnabled(false);


         secCID=(LinearLayout)findViewById(R.id.secCID);
         lineCID=(View)findViewById(R.id.lineCID);
         VlblCID=(TextView) findViewById(R.id.VlblCID);
         txtCID=(EditText) findViewById(R.id.txtCID);
         secPID=(LinearLayout)findViewById(R.id.secPID);
//         linePID=(View)findViewById(R.id.linePID);
         VlblPID=(TextView) findViewById(R.id.VlblPID);
         txtPID=(EditText) findViewById(R.id.txtPID);
         txtPID.setText(PID);
         secWeek=(LinearLayout)findViewById(R.id.secWeek);
         lineWeek=(View)findViewById(R.id.lineWeek);
         VlblWeek=(TextView) findViewById(R.id.VlblWeek);
         txtWeek=(EditText) findViewById(R.id.txtWeek);
         txtWeek.setText(WEEK);
         txtWeek.setEnabled(false);
         secVDate=(LinearLayout)findViewById(R.id.secVDate);
         lineVDate=(View)findViewById(R.id.lineVDate);
         VlblVDate=(TextView) findViewById(R.id.VlblVDate);
         dtpVDate=(EditText) findViewById(R.id.dtpVDate);

         if(IDbundle.getString("visitdate")==null)
         {
             dtpVDate.setText(Global.DateNowDMY());
         }
         else {
             if (IDbundle.getString("visitdate").toString().length() == 0)
                 dtpVDate.setText(Global.DateNowDMY());
             else
                 dtpVDate.setText(IDbundle.getString("visitdate"));
         }

         secVType=(LinearLayout)findViewById(R.id.secVType);
         lineVType=(View)findViewById(R.id.lineVType);
         VlblVType = (TextView) findViewById(R.id.VlblVType);
         rdogrpVType = (RadioGroup) findViewById(R.id.rdogrpVType);
         rdoVType1 = (RadioButton) findViewById(R.id.rdoVType1);
         rdoVType2 = (RadioButton) findViewById(R.id.rdoVType2);
         rdoVType3 = (RadioButton) findViewById(R.id.rdoVType3);


        if (VTYPE.equals("1")) {
            rdoVType1.setChecked(true);
        }
        else if (VTYPE.equals("2"))
        {
            rdoVType2.setChecked(true);
        }
        else if (VTYPE.equals("3"))
        {
            rdoVType3.setChecked(true);
        }


         secVisit=(LinearLayout)findViewById(R.id.secVisit);
         lineVisit=(View)findViewById(R.id.lineVisit);
         VlblVisit=(TextView) findViewById(R.id.VlblVisit);
         txtVisit=(EditText) findViewById(R.id.txtVisit);
         txtVisit.setText(VISIT);
         txtVisit.setEnabled(false);
         secSlNo=(LinearLayout)findViewById(R.id.secSlNo);
         lineSlNo=(View)findViewById(R.id.lineSlNo);
         VlblSlNo=(TextView) findViewById(R.id.VlblSlNo);
         txtSlNo=(EditText) findViewById(R.id.txtSlNo);

//         txtSlNo.setText(SlNumber(CHILDID,WEEK,VTYPE,VISIT));

         secTemp=(LinearLayout)findViewById(R.id.secTemp);
         lineTemp=(View)findViewById(R.id.lineTemp);
         VlblTemp=(TextView) findViewById(R.id.VlblTemp);
         txtTemp=(EditText) findViewById(R.id.txtTemp);
         txtTemp.setText(TEMP);
         txtTemp.setEnabled(false);
         seclbl3=(LinearLayout)findViewById(R.id.seclbl3);
         linelbl3=(View)findViewById(R.id.linelbl3);
         secdtpCoughDt=(LinearLayout)findViewById(R.id.secdtpCoughDt);
         linedtpCoughDt=(View)findViewById(R.id.linedtpCoughDt);
         VlbldtpCoughDt=(TextView) findViewById(R.id.VlbldtpCoughDt);
         dtpdtpCoughDt=(EditText) findViewById(R.id.dtpdtpCoughDt);
         secdtpDBreaDt=(LinearLayout)findViewById(R.id.secdtpDBreaDt);
         linedtpDBreaDt=(View)findViewById(R.id.linedtpDBreaDt);
         VlbldtpDBreaDt=(TextView) findViewById(R.id.VlbldtpDBreaDt);
         dtpdtpDBreaDt=(EditText) findViewById(R.id.dtpdtpDBreaDt);
         secDeepColdDt=(LinearLayout)findViewById(R.id.secDeepColdDt);
         lineDeepColdDt=(View)findViewById(R.id.lineDeepColdDt);
         VlblDeepColdDt=(TextView) findViewById(R.id.VlblDeepColdDt);
         dtpDeepColdDt=(EditText) findViewById(R.id.dtpDeepColdDt);
         secSoreThroatDt=(LinearLayout)findViewById(R.id.secSoreThroatDt);
         lineSoreThroatDt=(View)findViewById(R.id.lineSoreThroatDt);
         VlblSoreThroatDt=(TextView) findViewById(R.id.VlblSoreThroatDt);
         dtpSoreThroatDt=(EditText) findViewById(R.id.dtpSoreThroatDt);
         secRSVsuitable=(LinearLayout)findViewById(R.id.secRSVsuitable);
         lineRSVsuitable=(View)findViewById(R.id.lineRSVsuitable);
         VlblRSVsuitable = (TextView) findViewById(R.id.VlblRSVsuitable);
         rdogrpRSVsuitable = (RadioGroup) findViewById(R.id.rdogrpRSVsuitable);
         rdoRSVsuitable1 = (RadioButton) findViewById(R.id.rdoRSVsuitable1);
         rdoRSVsuitable2 = (RadioButton) findViewById(R.id.rdoRSVsuitable2);
         secRSVlisted=(LinearLayout)findViewById(R.id.secRSVlisted);
         lineRSVlisted=(View)findViewById(R.id.lineRSVlisted);
         VlblRSVlisted = (TextView) findViewById(R.id.VlblRSVlisted);
         rdogrpRSVlisted = (RadioGroup) findViewById(R.id.rdogrpRSVlisted);
         rdoRSVlisted1 = (RadioButton) findViewById(R.id.rdoRSVlisted1);
         rdoRSVlisted2 = (RadioButton) findViewById(R.id.rdoRSVlisted2);
         secRSVlistedDt=(LinearLayout)findViewById(R.id.secRSVlistedDt);
         lineRSVlistedDt=(View)findViewById(R.id.lineRSVlistedDt);
         VlblRSVlistedDt=(TextView) findViewById(R.id.VlblRSVlistedDt);
         dtpRSVlistedDt=(EditText) findViewById(R.id.dtpRSVlistedDt);
         secReason=(LinearLayout)findViewById(R.id.secReason);
         lineReason=(View)findViewById(R.id.lineReason);
         VlblReason=(TextView) findViewById(R.id.VlblReason);
         txtReason=(EditText) findViewById(R.id.txtReason);
     }
     catch(Exception  e)
     {
         Connection.MessageBox(RSV.this, e.getMessage());
         return;
     }
 }

 private void DataSave()
 {
   try
     {
         String ValidationMSG = ValidationCheck();
         if(ValidationMSG.length()>0)
         {
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
         String[] d_rdogrpVType = new String[] {"1","2","3"};
         objSave.setVType("");
         for (int i = 0; i < rdogrpVType.getChildCount(); i++)
         {
             rb = (RadioButton)rdogrpVType.getChildAt(i);
             if (rb.isChecked()) objSave.setVType(d_rdogrpVType[i]);
         }

         objSave.setVisit(txtVisit.getText().toString());
         objSave.setSlNo(txtSlNo.getText().toString());
         objSave.setTemp(txtTemp.getText().toString());
         objSave.setdtpCoughDt(dtpdtpCoughDt.getText().toString().length() > 0 ? Global.DateConvertYMD(dtpdtpCoughDt.getText().toString()) : dtpdtpCoughDt.getText().toString());
         objSave.setdtpDBreaDt(dtpdtpDBreaDt.getText().toString().length() > 0 ? Global.DateConvertYMD(dtpdtpDBreaDt.getText().toString()) : dtpdtpDBreaDt.getText().toString());
         objSave.setDeepColdDt(dtpDeepColdDt.getText().toString().length() > 0 ? Global.DateConvertYMD(dtpDeepColdDt.getText().toString()) : dtpDeepColdDt.getText().toString());
         objSave.setSoreThroatDt(dtpSoreThroatDt.getText().toString().length() > 0 ? Global.DateConvertYMD(dtpSoreThroatDt.getText().toString()) : dtpSoreThroatDt.getText().toString());
         String[] d_rdogrpRSVsuitable = new String[] {"1","2"};
         objSave.setRSVsuitable("");
         for (int i = 0; i < rdogrpRSVsuitable.getChildCount(); i++)
         {
             rb = (RadioButton)rdogrpRSVsuitable.getChildAt(i);
             if (rb.isChecked()) objSave.setRSVsuitable(d_rdogrpRSVsuitable[i]);
         }

         String[] d_rdogrpRSVlisted = new String[] {"1","2"};
         objSave.setRSVlisted("");
         for (int i = 0; i < rdogrpRSVlisted.getChildCount(); i++)
         {
             rb = (RadioButton)rdogrpRSVlisted.getChildAt(i);
             if (rb.isChecked()) objSave.setRSVlisted(d_rdogrpRSVlisted[i]);
         }

         objSave.setRSVlistedDt(dtpRSVlistedDt.getText().toString().length() > 0 ? Global.DateConvertYMD(dtpRSVlistedDt.getText().toString()) : dtpRSVlistedDt.getText().toString());
         objSave.setReason(txtReason.getText().toString());
         objSave.setStartTime(STARTTIME);
         objSave.setEndTime(g.CurrentTime24());
         objSave.setDeviceID(DEVICEID);
         objSave.setEntryUser(ENTRYUSER); //from data entry user list
         objSave.setLat(MySharedPreferences.getValue(this, "lat"));
         objSave.setLon(MySharedPreferences.getValue(this, "lon"));

         String status = objSave.SaveUpdateData(this);
         if(status.length()==0) {
             Intent returnIntent = new Intent();
             returnIntent.putExtra("res", "");
             setResult(Activity.RESULT_OK, returnIntent);

             Connection.MessageBox(RSV.this, "Saved Successfully");
         }
         else{
             Connection.MessageBox(RSV.this, status);
             return;
         }
     }
     catch(Exception  e)
     {
         Connection.MessageBox(RSV.this, e.getMessage());
         return;
     }
 }

 private String ValidationCheck()
 {
   String ValidationMsg = "";
   String DV = "";
   try
     {
         ResetSectionColor();
         if(txtChildID.getText().toString().length()==0 & secChildID.isShown())
           {
             ValidationMsg += "\nRequired field: Child ID.";
             secChildID.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color_Section_Highlight));
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
         if(txtWeek.getText().toString().length()==0 & secWeek.isShown())
           {
             ValidationMsg += "\nRequired field: সপ্তাহ.";
             secWeek.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color_Section_Highlight));
           }
         if(secWeek.isShown() & (Integer.valueOf(txtWeek.getText().toString().length()==0 ? "1" : txtWeek.getText().toString()) < 1 || Integer.valueOf(txtWeek.getText().toString().length()==0 ? "999" : txtWeek.getText().toString()) > 999))
           {
             ValidationMsg += "\nValue should be between 1 and 999(সপ্তাহ).";
             secWeek.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color_Section_Highlight));
           }
         DV = Global.DateValidate(dtpVDate.getText().toString());
         if(DV.length()!=0 & secVDate.isShown())
           {
             ValidationMsg += "\nRequired field/Not a valid date format: ভিজিটের তারিখ.";
             secVDate.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color_Section_Highlight));
           }
         if(!rdoVType1.isChecked() & !rdoVType2.isChecked() & !rdoVType3.isChecked() & secVType.isShown())
           {
             ValidationMsg += "\nRequired field: ফলোআপ পরিদর্শন/ অতিরিক্ত.";
             secVType.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color_Section_Highlight));
           }
         if(txtVisit.getText().toString().length()==0 & secVisit.isShown())
           {
             ValidationMsg += "\nRequired field: পরিদর্শন সংখ্যা.";
             secVisit.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color_Section_Highlight));
           }
         if(secVisit.isShown() & (Integer.valueOf(txtVisit.getText().toString().length()==0 ? "1" : txtVisit.getText().toString()) < 0 || Integer.valueOf(txtVisit.getText().toString().length()==0 ? "9" : txtVisit.getText().toString()) > 9))
           {
             ValidationMsg += "\nValue should be between 1 and 9(পরিদর্শন সংখ্যা).";
             secVisit.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color_Section_Highlight));
           }
         if(txtSlNo.getText().toString().length()==0 & secSlNo.isShown())
           {
             ValidationMsg += "\nRequired field: আর এস ভি পরিদর্শন #.";
             secSlNo.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color_Section_Highlight));
           }
         if(secSlNo.isShown() & (Integer.valueOf(txtSlNo.getText().toString().length()==0 ? "1" : txtSlNo.getText().toString()) < 1 || Integer.valueOf(txtSlNo.getText().toString().length()==0 ? "2" : txtSlNo.getText().toString()) > 2))
           {
             ValidationMsg += "\nValue should be between 1 and 2(আর এস ভি পরিদর্শন #).";
             secSlNo.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color_Section_Highlight));
           }
         if(txtTemp.getText().toString().length()==0 & secTemp.isShown())
           {
             ValidationMsg += "\nRequired field: তাপমাত্রা.";
             secTemp.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color_Section_Highlight));
           }
         if(secTemp.isShown() & (Double.valueOf(txtTemp.getText().toString().length()==0 ? "070" : txtTemp.getText().toString()) < 070 || Double.valueOf(txtTemp.getText().toString().length()==0 ? "106" : txtTemp.getText().toString()) > 106))
           {
             ValidationMsg += "\nValue should be between 070 and 106(তাপমাত্রা).";
             secTemp.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color_Section_Highlight));
           }
         DV = Global.DateValidate(dtpdtpCoughDt.getText().toString());
         if(DV.length()!=0 & secdtpCoughDt.isShown())
           {
             ValidationMsg += "\nRequired field/Not a valid date format: কাশি শুরুর তারিখ.";
             secdtpCoughDt.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color_Section_Highlight));
           }
         DV = Global.DateValidate(dtpdtpDBreaDt.getText().toString());
         if(DV.length()!=0 & secdtpDBreaDt.isShown())
           {
             ValidationMsg += "\nRequired field/Not a valid date format: শ্বাসকষ্ট শুরুর তারিখ.";
             secdtpDBreaDt.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color_Section_Highlight));
           }
         DV = Global.DateValidate(dtpDeepColdDt.getText().toString());
         if(DV.length()!=0 & secDeepColdDt.isShown())
           {
             ValidationMsg += "\nRequired field/Not a valid date format: ঘন সর্দি শুরুর তারিখ.";
             secDeepColdDt.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color_Section_Highlight));
           }
         DV = Global.DateValidate(dtpSoreThroatDt.getText().toString());
         if(DV.length()!=0 & secSoreThroatDt.isShown())
           {
             ValidationMsg += "\nRequired field/Not a valid date format: গলা ব্যাথা শুরুর তারিখ.";
             secSoreThroatDt.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color_Section_Highlight));
           }
         if(!rdoRSVsuitable1.isChecked() & !rdoRSVsuitable2.isChecked() & secRSVsuitable.isShown())
           {
             ValidationMsg += "\nRequired field: আর এস ভি গবেষণার জন্য উপযুক্ত.";
             secRSVsuitable.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color_Section_Highlight));
           }
         if(!rdoRSVlisted1.isChecked() & !rdoRSVlisted2.isChecked() & secRSVlisted.isShown())
           {
             ValidationMsg += "\nRequired field: আর এস ভি গবেষণার জন্য তালিকাভুক্ত.";
             secRSVlisted.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color_Section_Highlight));
           }
         DV = Global.DateValidate(dtpRSVlistedDt.getText().toString());
         if(DV.length()!=0 & secRSVlistedDt.isShown())
           {
             ValidationMsg += "\nRequired field/Not a valid date format: তারিখ.";
             secRSVlistedDt.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color_Section_Highlight));
           }
         if(txtReason.getText().toString().length()==0 & secReason.isShown())
           {
             ValidationMsg += "\nRequired field: তালিকাভুক্ত না হলে কারন .........( অনুপস্থিত, মৃত্যু, সম্মতি প্রত্যাহার,  অন্যান্য).";
             secReason.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color_Section_Highlight));
           }
     }
     catch(Exception  e)
     {
         ValidationMsg += "\n"+ e.getMessage();
     }

     return ValidationMsg;
 }

 private void ResetSectionColor()
 {
   try
     {
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
     }
     catch(Exception  e)
     {
     }
 }

 private void DataSearch(String ChildID, String Week, String VType, String Visit)
     {
       try
        {     
           RadioButton rb;
           RSV_DataModel d = new RSV_DataModel();
           String SQL = "Select * from "+ TableName +"  Where ChildID='"+ ChildID +"' and Week='"+ Week +"' and VType='"+ VType +"' and Visit='"+ Visit +"'";
           List<RSV_DataModel> data = d.SelectAll(this, SQL);
           for(RSV_DataModel item : data){
             txtChildID.setText(String.valueOf(item.getChildID()));
             txtCID.setText(String.valueOf(item.getCID()));
             txtPID.setText(String.valueOf(item.getPID()));
             txtWeek.setText(String.valueOf(item.getWeek()));
             dtpVDate.setText(item.getVDate().toString().length()==0 ? "" : Global.DateConvertDMY(item.getVDate()));
             String[] d_rdogrpVType = new String[] {"1","2","3"};
             for (int i = 0; i < d_rdogrpVType.length; i++)
             {
                 if (String.valueOf(item.getVType()).equals(String.valueOf(d_rdogrpVType[i])))
                 {
                     rb = (RadioButton)rdogrpVType.getChildAt(i);
                     rb.setChecked(true);
                 }
             }
             txtVisit.setText(String.valueOf(item.getVisit()));
             txtSlNo.setText(String.valueOf(item.getSlNo()));
             txtTemp.setText(String.valueOf(item.getTemp()));
             dtpdtpCoughDt.setText(item.getdtpCoughDt().toString().length()==0 ? "" : Global.DateConvertDMY(item.getdtpCoughDt()));
             dtpdtpDBreaDt.setText(item.getdtpDBreaDt().toString().length()==0 ? "" : Global.DateConvertDMY(item.getdtpDBreaDt()));
             dtpDeepColdDt.setText(item.getDeepColdDt().toString().length()==0 ? "" : Global.DateConvertDMY(item.getDeepColdDt()));
             dtpSoreThroatDt.setText(item.getSoreThroatDt().toString().length()==0 ? "" : Global.DateConvertDMY(item.getSoreThroatDt()));
             String[] d_rdogrpRSVsuitable = new String[] {"1","2"};
             for (int i = 0; i < d_rdogrpRSVsuitable.length; i++)
             {
                 if (String.valueOf(item.getRSVsuitable()).equals(String.valueOf(d_rdogrpRSVsuitable[i])))
                 {
                     rb = (RadioButton)rdogrpRSVsuitable.getChildAt(i);
                     rb.setChecked(true);
                 }
             }
             String[] d_rdogrpRSVlisted = new String[] {"1","2"};
             for (int i = 0; i < d_rdogrpRSVlisted.length; i++)
             {
                 if (String.valueOf(item.getRSVlisted()).equals(String.valueOf(d_rdogrpRSVlisted[i])))
                 {
                     rb = (RadioButton)rdogrpRSVlisted.getChildAt(i);
                     rb.setChecked(true);
                 }
             }
             dtpRSVlistedDt.setText(item.getRSVlistedDt().toString().length()==0 ? "" : Global.DateConvertDMY(item.getRSVlistedDt()));
             txtReason.setText(item.getReason());
           }
        }
        catch(Exception  e)
        {
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
      else if (VariableID.equals("btndtpCoughDt"))
      {
          dtpDate = (EditText)findViewById(R.id.dtpdtpCoughDt);
      }
      else if (VariableID.equals("btndtpDBreaDt"))
      {
          dtpDate = (EditText)findViewById(R.id.dtpdtpDBreaDt);
      }
      else if (VariableID.equals("btnDeepColdDt"))
      {
          dtpDate = (EditText)findViewById(R.id.dtpDeepColdDt);
      }
      else if (VariableID.equals("btnSoreThroatDt"))
      {
          dtpDate = (EditText)findViewById(R.id.dtpSoreThroatDt);
      }
      else if (VariableID.equals("btnRSVlistedDt"))
      {
          dtpDate = (EditText)findViewById(R.id.dtpRSVlistedDt);
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

    }
  };

//     private String SlNumber(String ChildID, String Week, String VType, String Visit)
//     {
//         String M = C.ReturnSingleValue("Select cast(ifnull(max(SlNo,0))+1 as varchar(1))SlNumber from RSV ChildID='"+ ChildID +"' and Week='"+ Week +"' and VType='"+ VType +"' and Visit='"+ Visit +"'");
//         return M;
//     }
 
 // turning off the GPS if its in on state. to avoid the battery drain.
 @Override
 protected void onDestroy() {
     // TODO Auto-generated method stub
     super.onDestroy();
 }
}
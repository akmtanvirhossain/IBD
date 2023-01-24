
 package org.icddrb.ibd;


 import android.app.Activity;
 import android.app.AlertDialog;
 import android.app.DatePickerDialog;
 import android.app.Dialog;
 import android.app.TimePickerDialog;
 import android.content.DialogInterface;
 import android.graphics.Color;
 import android.location.Location;
 import android.os.Bundle;
 import android.support.v4.content.ContextCompat;
 import android.support.v7.app.AppCompatActivity;
 import android.view.KeyEvent;
 import android.view.Menu;
 import android.view.MenuInflater;
 import android.view.MenuItem;
 import android.view.View;
 import android.view.WindowManager;
 import android.widget.Button;
 import android.widget.DatePicker;
 import android.widget.EditText;
 import android.widget.ImageButton;
 import android.widget.LinearLayout;
 import android.widget.RadioButton;
 import android.widget.SimpleAdapter;
 import android.widget.TextView;
 import android.widget.TimePicker;

 import java.util.ArrayList;
 import java.util.Calendar;
 import java.util.HashMap;
 import java.util.List;

 import Common.Connection;
 import Common.Global;
 import Utility.MySharedPreferences;

 public class data_GAge extends Activity {
    //Disabled Back/Home key
    //--------------------------------------------------------------------------------------------------
    @Override 
    public boolean onKeyDown(int iKeyCode, KeyEvent event)
    {
        if(iKeyCode == KeyEvent.KEYCODE_BACK || iKeyCode == KeyEvent.KEYCODE_HOME) 
             { return false; }
        else { return true;  }
    }

     public boolean onCreateOptionsMenu(Menu menu) {
         MenuInflater inflater = getMenuInflater();
         inflater.inflate(R.menu.mnuclose, menu);
         return true;
     }

     public boolean onOptionsItemSelected(MenuItem item) {
         AlertDialog.Builder adb = new AlertDialog.Builder(data_GAge.this);
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

                 return true;

         }
         return false;
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

    LinearLayout secVill;
    View lineVill;
    TextView VlblVill;
    EditText txtVill;
    LinearLayout secBari;
    View lineBari;
    TextView VlblBari;
    EditText txtBari;
    LinearLayout secHH;
    View lineHH;
    TextView VlblHH;
    EditText txtHH;
    LinearLayout secSNo;
    View lineSNo;
    TextView VlblSNo;
    EditText txtSNo;
    LinearLayout secPNo;
    View linePNo;
    TextView VlblPNo;
    EditText txtPNo;
    LinearLayout secGAge;
    View lineGAge;
    TextView VlblGAge;
    EditText txtGAge;

    static int MODULEID   = 0;
    static int LANGUAGEID = 0;
    static String TableName;

    static String STARTTIME = "";
    static String DEVICEID  = "";
    static String ENTRYUSER = "";
    MySharedPreferences sp;

    Bundle IDbundle;
    static String VILL = "";
    static String BARI = "";
    static String HH = "";
    static String SNO = "";
    static String PNO = "";
    static String CHILDID = "";
    static String GAGE = "";

 public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
   try
     {
         setContentView(R.layout.data_gage);
         getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

         C = new Connection(this);
         g = Global.getInstance();

         STARTTIME = g.CurrentTime24();
         DEVICEID  = g.getClusterCode();// MySharedPreferences.getValue(this, "deviceid");
         ENTRYUSER = g.getUserId();// MySharedPreferences.getValue(this, "userid");

         IDbundle = getIntent().getExtras();
         VILL = IDbundle.getString("vill");
         BARI = IDbundle.getString("bari");
         HH = IDbundle.getString("hh");
         SNO = IDbundle.getString("sno");
         PNO = IDbundle.getString("pno");
         CHILDID = IDbundle.getString("childid");
         GAGE = IDbundle.getString("gage");

         TableName = "data_GAge";


        Initialization();
//        DataSearch(VILL,BARI,HH,SNO);
        DataSearch(CHILDID);


        Button cmdSave = (Button) findViewById(R.id.cmdSave);
        cmdSave.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) { 
            DataSave();
        }});
     }
     catch(Exception  e)
     {
         Connection.MessageBox(data_GAge.this, e.getMessage());
         return;
     }
 }

 private void Initialization()
 {
   try
     {
         secVill=(LinearLayout)findViewById(R.id.secVill);
         lineVill=(View)findViewById(R.id.lineVill);
         VlblVill=(TextView) findViewById(R.id.VlblVill);
         txtVill=(EditText) findViewById(R.id.txtVill);
         txtVill.setText(VILL);
         txtVill.setEnabled(false);
         secBari=(LinearLayout)findViewById(R.id.secBari);
         lineBari=(View)findViewById(R.id.lineBari);
         VlblBari=(TextView) findViewById(R.id.VlblBari);
         txtBari=(EditText) findViewById(R.id.txtBari);
         txtBari.setText(BARI);
         txtBari.setEnabled(false);
         secHH=(LinearLayout)findViewById(R.id.secHH);
         lineHH=(View)findViewById(R.id.lineHH);
         VlblHH=(TextView) findViewById(R.id.VlblHH);
         txtHH=(EditText) findViewById(R.id.txtHH);
         txtHH.setText(HH);
         txtHH.setEnabled(false);
         secSNo=(LinearLayout)findViewById(R.id.secSNo);
         lineSNo=(View)findViewById(R.id.lineSNo);
         VlblSNo=(TextView) findViewById(R.id.VlblSNo);
         txtSNo=(EditText) findViewById(R.id.txtSNo);
         txtSNo.setText(SNO);
         txtSNo.setEnabled(false);
         secPNo=(LinearLayout)findViewById(R.id.secPNo);
         linePNo=(View)findViewById(R.id.linePNo);
         VlblPNo=(TextView) findViewById(R.id.VlblPNo);
         txtPNo=(EditText) findViewById(R.id.txtPNo);
         txtPNo.setText(CHILDID);
//         if(CHILDID.length()>0)
         txtPNo.setEnabled(false);
//         else
//         txtPNo.setEnabled(true);
         secGAge=(LinearLayout)findViewById(R.id.secGAge);
         lineGAge=(View)findViewById(R.id.lineGAge);
         VlblGAge=(TextView) findViewById(R.id.VlblGAge);
         txtGAge=(EditText) findViewById(R.id.txtGAge);
         txtGAge.setText(GAGE);
     }
     catch(Exception  e)
     {
         Connection.MessageBox(data_GAge.this, e.getMessage());
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
         	Connection.MessageBox(data_GAge.this, ValidationMSG);
         	return;
         }
 
         String SQL = "";
         RadioButton rb;

         data_GAge_DataModel objSave = new data_GAge_DataModel();
         objSave.setVill(txtVill.getText().toString());
         objSave.setBari(txtBari.getText().toString());
         objSave.setHH(txtHH.getText().toString());
         objSave.setSNo(txtSNo.getText().toString());
         objSave.setChildId(txtPNo.getText().toString());
         objSave.setGAge(txtGAge.getText().toString());
         objSave.setStartTime(STARTTIME);
         objSave.setEndTime(g.CurrentTime24());
         objSave.setDeviceID(DEVICEID);
         objSave.setEntryUser(ENTRYUSER); //from data entry user list
         objSave.setLat(MySharedPreferences.getValue(this, "lat"));
         objSave.setLon(MySharedPreferences.getValue(this, "lon"));

         String status = objSave.SaveUpdateData(this);
         if(status.length()==0) {
             finish();
             Connection.MessageBox(data_GAge.this, "Saved Successfully");
         }
         else{
             Connection.MessageBox(data_GAge.this, status);
             return;
         }
     }
     catch(Exception  e)
     {
         Connection.MessageBox(data_GAge.this, e.getMessage());
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
         if(txtVill.getText().toString().length()==0 & secVill.isShown())
           {
             ValidationMsg += "\nRequired field: Village.";
             secVill.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color_Section_Highlight));
           }
         if(txtBari.getText().toString().length()==0 & secBari.isShown())
           {
             ValidationMsg += "\nRequired field: Bari.";
             secBari.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color_Section_Highlight));
           }
         if(txtHH.getText().toString().length()==0 & secHH.isShown())
           {
             ValidationMsg += "\nRequired field: Household.";
             secHH.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color_Section_Highlight));
           }
         if(txtSNo.getText().toString().length()==0 & secSNo.isShown())
           {
             ValidationMsg += "\nRequired field: Member Serial.";
             secSNo.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color_Section_Highlight));
           }
         /*if(txtPNo.getText().toString().length()==0 & secPNo.isShown())
           {
             ValidationMsg += "\nRequired field: PNO.";
             secPNo.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color_Section_Highlight));
           }*/
         if(txtGAge.getText().toString().length()==0 & secGAge.isShown())
           {
             ValidationMsg += "\nRequired field: Gestational Age (Weeks).";
             secGAge.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color_Section_Highlight));
           }
         if(secGAge.isShown() & (Integer.valueOf(txtGAge.getText().toString().length()==0 ? "10" : txtGAge.getText().toString()) < 10 || Integer.valueOf(txtGAge.getText().toString().length()==0 ? "50" : txtGAge.getText().toString()) > 50))
           {
             ValidationMsg += "\nValue should be between 10 and 50(Gestational Age (Weeks)).";
             secGAge.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color_Section_Highlight));
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
             secVill.setBackgroundColor(Color.WHITE);
             secBari.setBackgroundColor(Color.WHITE);
             secHH.setBackgroundColor(Color.WHITE);
             secSNo.setBackgroundColor(Color.WHITE);
             secPNo.setBackgroundColor(Color.WHITE);
             secGAge.setBackgroundColor(Color.WHITE);
             secGAge.setBackgroundColor(Color.WHITE);
     }
     catch(Exception  e)
     {
     }
 }

// private void DataSearch(String Vill, String Bari, String HH, String SNo)
 private void DataSearch(String ChildId)
     {
       try
        {     
           RadioButton rb;
           data_GAge_DataModel d = new data_GAge_DataModel();
           String SQL = "Select * from "+ TableName +"  Where ChildId='"+ ChildId +"'";
//           String SQL = "Select * from "+ TableName +"  Where Vill='"+ Vill +"' and Bari='"+ Bari +"' and HH='"+ HH +"' and SNo='"+ SNo +"'";
           List<data_GAge_DataModel> data = d.SelectAll(this, SQL);
           for(data_GAge_DataModel item : data){
             txtVill.setText(item.getVill());
             txtBari.setText(item.getBari());
             txtHH.setText(item.getHH());
             txtSNo.setText(item.getSNo());
             txtPNo.setText(item.getChildId());
             txtGAge.setText(String.valueOf(item.getGAge()));
           }
        }
        catch(Exception  e)
        {
            Connection.MessageBox(data_GAge.this, e.getMessage());
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


      }
  };

 private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
    public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
       hour = selectedHour; minute = selectedMinute;
       EditText tpTime;

    }
  };


 
 // turning off the GPS if its in on state. to avoid the battery drain.
 @Override
 protected void onDestroy() {
     // TODO Auto-generated method stub
     super.onDestroy();
 }
}
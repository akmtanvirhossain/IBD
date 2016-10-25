package org.icddrb.ibd;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

//import ccah.icddrb.Global;
import android.app.*;
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

import Common.Connection;
import Common.Global;


public class VisitPneu extends Activity {
    boolean netwoekAvailable=false;
    Location currentLocation; 
    double currentLatitude,currentLongitude; 
    Location currentLocationNet; 
    double currentLatitudeNet,currentLongitudeNet; 
    //Disabled Back/Home key
    //--------------------------------------------------------------------------------------------------
  
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.menuclose, menu);
        return true;
    }
    

public boolean onOptionsItemSelected(MenuItem item) {
		AlertDialog.Builder adb = new AlertDialog.Builder(VisitPneu.this);
    	/*switch (item.getItemId()) {
    		case R.id.mnuClose:    			
        		adb.setTitle("Close");
		          adb.setMessage("Do you want to close this form[Yes/No]?");
		          adb.setNegativeButton("No", null);
		          adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
		              public void onClick(DialogInterface dialog, int which) {
		      	    	g.setIdNo("");
		    	    	g.setmemSlNo("");
		    	    	g.setmemRID("");    	    	
		         	  	
		         	  	finish();    			
		              }});
		          adb.show();    			    			
    			 			   			
    			return true;

    	}    */
    	return false;
    }

    //Disabled Back/Home key
    //--------------------------------------------------------------------------------------------------   
//Disabled Back/Home key
//--------------------------------------------------------------------------------------------------   
@Override 
public boolean onKeyDown(int iKeyCode, KeyEvent event)
{
    if(iKeyCode == KeyEvent.KEYCODE_BACK || iKeyCode == KeyEvent.KEYCODE_HOME) 
         { return false; }
    else { return true;  }
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

         LinearLayout secCID;
         TextView VlblCID;
         EditText txtCID;
         LinearLayout secPID;
         TextView VlblPID;
         EditText txtPID;
         LinearLayout secName;
         TextView VlblName;
         EditText txtName;
        /* LinearLayout secSex;
         TextView VlblSex;
         RadioGroup rdogrpSex;
         
         RadioButton rdoSex1;*/
         LinearLayout secVDate;
         TextView VlblVDate;
         EditText dtpVDate;
         ImageButton btnVDate;
         LinearLayout secWeek;
         TextView VlblWeek;
         EditText txtWeek;
         LinearLayout secVstat;
         TextView VlblVstat;
         Spinner spnVstat;
         LinearLayout secExDate;
         TextView VlblExDate;
         EditText dtpExDate;
         ImageButton btnExDate;
         LinearLayout secSickStatus;
         TextView VlblSickStatus;
         //EditText txtSickStatus;
         RadioGroup rdogrpStatus;
         RadioButton rdoStatus1;
         RadioButton rdoStatus2;
         

 String StartTime;
 String a;
 String b;
 public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
   try
     {
         setContentView(R.layout.visitpneu);
         C = new Connection(this);
         g = Global.getInstance();
         StartTime = g.CurrentTime24();

         TableName = "VisitPneu";


         secCID=(LinearLayout)findViewById(R.id.secCID);
         VlblCID=(TextView) findViewById(R.id.VlblCID);
         txtCID=(EditText) findViewById(R.id.txtCID);
         //txtCID.setText("31300010115");
      
         secPID=(LinearLayout)findViewById(R.id.secPID);
         VlblPID=(TextView) findViewById(R.id.VlblPID);
         txtPID=(EditText) findViewById(R.id.txtPID);
         secName=(LinearLayout)findViewById(R.id.secName);
         VlblName=(TextView) findViewById(R.id.VlblName);
         txtName=(EditText) findViewById(R.id.txtName);
        /* secSex=(LinearLayout)findViewById(R.id.secSex);
         VlblSex = (TextView) findViewById(R.id.VlblSex);
         rdogrpSex = (RadioGroup) findViewById(R.id.rdogrpSex);
         
         rdoSex1 = (RadioButton) findViewById(R.id.rdoSex1);*/
         
         secVDate=(LinearLayout)findViewById(R.id.secVDate);
         VlblVDate=(TextView) findViewById(R.id.VlblVDate);
         dtpVDate=(EditText) findViewById(R.id.dtpVDate);
         //ldtpVDate.setText()
         secWeek=(LinearLayout)findViewById(R.id.secWeek);
         VlblWeek=(TextView) findViewById(R.id.VlblWeek);
         
         
         
         
         a=C.ReturnSingleValue("Select MAX(Week)+1  from VisitPneu WHERE     CID = '31300010115'");
         
         txtWeek=(EditText) findViewById(R.id.txtWeek);
         txtWeek.setText(a);
         secVstat=(LinearLayout)findViewById(R.id.secVstat);
         
         
         VlblVstat=(TextView) findViewById(R.id.VlblVstat);
         spnVstat=(Spinner) findViewById(R.id.spnVstat);
         List<String> listVstat = new ArrayList<String>();
         
         listVstat.add("");
         listVstat.add("1-শিশু উপস্থিত ছিল ");
         listVstat.add("2-শিশু উপস্থিত অনুপস্থিত ছিল");
         listVstat.add("3-চিকিৎসার জন্য অনুপস্থিত");
         listVstat.add("4-বয়স উত্তীর্ণ হলে");
         listVstat.add("5-স্থানান্তর হলে");
         listVstat.add("6-মৃত্যুবরন করলে");
         listVstat.add("7-সার্ভিলেন্সে থাকতে অসম্মতি জানালে");
         listVstat.add("8-VHW ছুটি থাকেলে");
         listVstat.add("9-তথ্য");
         ArrayAdapter<String> adptrVstat= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listVstat);
         spnVstat.setAdapter(adptrVstat);
         spnVstat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
	    	    @Override
	    	    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
	    	    	
	    	    	if(spnVstat.getSelectedItemPosition()==4|spnVstat.getSelectedItemPosition()==5|spnVstat.getSelectedItemPosition()==6|spnVstat.getSelectedItemPosition()==7)
	    	    	{
	    	    		secExDate.setVisibility(View.VISIBLE);
	    	    	}
	    	    	else
	    	    	{
	    	    		dtpExDate.setText("");
	    	    		secExDate.setVisibility(View.GONE);
	    	    	}

	    	    }

	    	    @Override
	    	    public void onNothingSelected(AdapterView<?> parentView) {
	    	    	
	    	    }

	    	});	   
         secExDate=(LinearLayout)findViewById(R.id.secExDate);
         VlblExDate=(TextView) findViewById(R.id.VlblExDate);
         dtpExDate=(EditText) findViewById(R.id.dtpExDate);
         secSickStatus=(LinearLayout)findViewById(R.id.secSickStatus);
         VlblSickStatus=(TextView) findViewById(R.id.VlblSickStatus);
         //txtSickStatus=(EditText) findViewById(R.id.txtSickStatus);
         rdogrpStatus= (RadioGroup) findViewById(R.id.rdogrpStatus);
         rdoStatus1= (RadioButton) findViewById(R.id.rdoStatus1);
         rdoStatus2= (RadioButton) findViewById(R.id.rdoStatus2);
        



         btnVDate = (ImageButton) findViewById(R.id.btnVDate);
         btnVDate.setOnClickListener(new View.OnClickListener() {
         public void onClick(View v) { VariableID = "btnVDate"; showDialog(DATE_DIALOG); }});

         btnExDate = (ImageButton) findViewById(R.id.btnExDate);
         btnExDate.setOnClickListener(new View.OnClickListener() {
         public void onClick(View v) { VariableID = "btnExDate"; showDialog(DATE_DIALOG); }});



         //DataSearchName("31300010115");
        Button cmdSave = (Button) findViewById(R.id.cmdSave);
        cmdSave.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) { 
            DataSave();
        }});
     }
     catch(Exception  e)
     {
         Connection.MessageBox(VisitPneu.this, e.getMessage());
         return;
     }
 }

 private void DataSave()
 {
   try
     {
	     b=C.ReturnSingleValue("Select MAX(VDate)  from VisitPneu WHERE     CID = '31300010115'");
         String DV="";
         String DV1="";
         
         int c=Global.DateDifferenceDays(dtpVDate.getText().toString(),Global.DateConvertDMY(b));
         if(c>5 & c<9)
         {
        		 
         }
         
         else
         {
        	 Connection.MessageBox(VisitPneu.this, "Difference between current visit date and previous visit date must be between 5 to 9 days.");
        	 dtpVDate.requestFocus(); 
             return;
         }
         
         DV = Global.DateValidate(dtpVDate.getText().toString());
       
         if(txtCID.getText().toString().length()==0 & secCID.isShown())
           {
             Connection.MessageBox(VisitPneu.this, "Required field:শিশুর চলতি ID নং.");
             txtCID.requestFocus(); 
             return;	
           }
          if(txtPID.getText().toString().length()==0 & secPID.isShown())
           {
             Connection.MessageBox(VisitPneu.this, "Required field:শিশুর স্থায়ী ID নং.");
             txtPID.requestFocus(); 
             return;	
           }
          if(txtName.getText().toString().length()==0 & secName.isShown())
           {
             Connection.MessageBox(VisitPneu.this, "Required field:শিশুর নাম.");
             txtName.requestFocus(); 
             return;	
           }
         
   /*      else if(!rdoSex1.isChecked() & secSex.isShown())
           {
              Connection.MessageBox(VisitPneu.this, "Select anyone options from Sex.");
              rdoSex1.requestFocus();
              return;
           }
        
          */
         if(DV.length()!=0 & secVDate.isShown())
         {
           Connection.MessageBox(VisitPneu.this, DV);
           dtpVDate.requestFocus(); 
           return;	
         }
         
         
       /*  If (dtdy < 5 Or dtdy > 9) Then
         MsgBox "Difference between current visit date and previous visit date must be between 5 to 9 days. Previous visit date=" & LastFDate, vbExclamation, "Date difference"
         Week1.LastTextBox = True
         Week1.SetFocus
         Week1.LastTextBox = True
     End If*/
         

        if(txtWeek.getText().toString().length()==0 & secWeek.isShown())
           {
             Connection.MessageBox(VisitPneu.this, "Required field:সপ্তাহ.");
             txtWeek.requestFocus(); 
             return;	
           }
        /* else if(txtVstat.getText().toString().length()==0 & secVstat.isShown())
           {
             Connection.MessageBox(VisitPneu.this, "Required field:পরিদর্শনের অবস্থা.");
             txtVstat.requestFocus(); 
             return;	
           }*/
         DV = Global.DateValidate(dtpExDate.getText().toString());
         if(DV.length()!=0 & secExDate.isShown())
           {
             Connection.MessageBox(VisitPneu.this, DV);
             dtpExDate.requestFocus(); 
             return;	
           }
         /*else if(txtSickStatus.getText().toString().length()==0 & secSickStatus.isShown())
           {
             Connection.MessageBox(VisitPneu.this, "Required field:Sick Status.");
             txtSickStatus.requestFocus(); 
             return;	
           }
 */
         String SQL = "";
         
         if(!C.Existence("Select CID,Week,StartTime,EndTime,UserId,EnDt,Upload from " + TableName + "  Where CID='"+ txtCID.getText().toString() +"' and Week='"+ txtWeek.getText().toString() +"'"))
         {
             //SQL = "Insert into " + TableName + "(CID,Week,StartTime,EndTime,UserId,EnDt,Upload)Values('"+ txtCID.getText() +"','"+ txtWeek.getText() +"','"+ StartTime +"','"+ g.CurrentTime24() +"','"+ g.getUserId() +"','"+ Global.DateNow()+"','2')";
             C.Save(SQL);
         }

         SQL = "Update " + TableName + " Set ";
         //SQL+="CID = '"+ txtCID.getText().toString() +"',";
         //SQL+="PID = '"+ txtPID.getText().toString() +"',";
         //SQL+="Name = '"+ txtName.getText().toString() +"',";
/*         RadioButton rbSex = (RadioButton)findViewById(rdogrpSex.getCheckedRadioButtonId());
         SQL+="Sex = '"+ (rbSex==null?"":(Global.Left(rbSex.getText().toString(),1))) +"',";*/
         SQL+="VDate = '"+ Global.DateConvertYMD(dtpVDate.getText().toString()) +"',";
         SQL+="Week = '"+ txtWeek.getText().toString() +"',";
        // SQL+="Vstat = '"+ txtVstat.getText().toString() +"',";
         SQL+="Vstat = '"+ (spnVstat.getSelectedItemPosition()==0?"":Global.Left(spnVstat.getSelectedItem().toString(),1)) +"',";
         
       //  SQL+="ExDate = '"+ Global.DateConvertYMD(dtpExDate.getText().toString()) +"',";
         RadioButton rbStatus = (RadioButton)findViewById(rdogrpStatus.getCheckedRadioButtonId());
         SQL+="SickStatus = '"+ (rbStatus==null?"":(Global.Left(rbStatus.getText().toString(),1))) +"'";
       //  SQL+="SickStatus = '"+ txtSickStatus.getText().toString() +"'";
         SQL+="  Where CID='"+ txtCID.getText().toString() +"' and Week='"+ txtWeek.getText().toString() +"'";
        
         C.Save(SQL);


         if(spnVstat.getSelectedItemPosition()==4|spnVstat.getSelectedItemPosition()==5|spnVstat.getSelectedItemPosition()==6|spnVstat.getSelectedItemPosition()==7)
           {
                SQL = "Update Child Set ";
                SQL+="ExType = '"+ (spnVstat.getSelectedItemPosition()==0?"":Global.Left(spnVstat.getSelectedItem().toString(),1)) +"',";
                SQL+="ExDate = '"+ Global.DateConvertYMD(dtpExDate.getText().toString()) +"'";
                SQL+="  Where CID='"+ txtCID.getText().toString()+"'";
                C.Save(SQL);
            }

        Connection.MessageBox(VisitPneu.this, "Saved Successfully");
     }
     catch(Exception  e)
     {
         Connection.MessageBox(VisitPneu.this, e.getMessage());
         return;
     }
 }
 private void DataSearch(String CID, String Week)
     {
       try
        {
           RadioButton rb;
           Cursor cur = C.ReadData("Select * from "+ TableName +"  Where CID='"+ CID +"' and Week='"+ Week +"'");
           cur.moveToFirst();
           while(!cur.isAfterLast())
           {
             txtCID.setText(cur.getString(cur.getColumnIndex("CID")));
             txtPID.setText(cur.getString(cur.getColumnIndex("PID")));
            // txtName.setText(cur.getString(cur.getColumnIndex("Name")));
/*             for (int i = 0; i < rdogrpSex.getChildCount(); i++)
             {
                rb = (RadioButton)rdogrpSex.getChildAt(i);
                if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("Sex"))))
                   rb.setChecked(true);
                else
                   rb.setChecked(false);
             }*/
             
             
             dtpVDate.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("VDate"))));
             txtWeek.setText(cur.getString(cur.getColumnIndex("Week")));
            // txtVstat.setText(cur.getString(cur.getColumnIndex("Vstat")));
             spnVstat.setSelection(Global.SpinnerItemPosition(spnVstat, 1 ,cur.getString(cur.getColumnIndex("Vstat"))));
             //
             dtpExDate.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("ExDate"))));
           //  txtSickStatus.setText(cur.getString(cur.getColumnIndex("SickStatus")));
             for (int i = 0; i < rdogrpStatus.getChildCount(); i++)
             {
                rb = (RadioButton)rdogrpStatus.getChildAt(i);
                if (Global.Left(rb.getText().toString(), 1).equalsIgnoreCase(cur.getString(cur.getColumnIndex("SickStatus"))))
                   rb.setChecked(true);
                else
                   rb.setChecked(false);
             }

             cur.moveToNext();
           }
           cur.close();
        }
        catch(Exception  e)
        {
            Connection.MessageBox(VisitPneu.this, e.getMessage());
            return;
        }
     }
       private void DataSearchName(String CID)
       {
         try
          {
       
            
             Cursor cur = C.ReadData("Select * from ChildPneu Where CID='"+ CID +"'");
             cur.moveToFirst();
             while(!cur.isAfterLast())
             {
              
               txtPID.setText(cur.getString(cur.getColumnIndex("PID")));
               txtName.setText(cur.getString(cur.getColumnIndex("Name")));


               cur.moveToNext();
             }
             cur.close();
          }
          catch(Exception  e)
          {
              Connection.MessageBox(VisitPneu.this, e.getMessage());
              return;
          }
     }

       private void DataSearchWeeks(String CID)
       {
         try
          {
       
            
             Cursor cur = C.ReadData("Select Week from VisitPnu Where CID='"+ CID +"'");
             cur.moveToFirst();
             while(!cur.isAfterLast())
             {
              
            	 
            	 
            	 txtWeek.setText(cur.getString(cur.getColumnIndex("Week")));


               cur.moveToNext();
             }
             cur.close();
          }
          catch(Exception  e)
          {
              Connection.MessageBox(VisitPneu.this, e.getMessage());
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
             else if (VariableID.equals("btnExDate"))
              {
                  dtpDate = (EditText)findViewById(R.id.dtpExDate);
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


        //  tpTime.setText(new StringBuilder().append(Global.Right("00"+hour,2)).append(":").append(Global.Right("00"+minute,2)));

    }
  };
}

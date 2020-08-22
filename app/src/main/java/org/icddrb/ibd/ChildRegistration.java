package org.icddrb.ibd;

/* Created by TanvirHossain on 22/11/2015.*/

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
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
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
import android.view.WindowManager;
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
import android.widget.Toast;

import Common.*;

public class ChildRegistration extends Activity {
    boolean netwoekAvailable=false;
    Location currentLocation;
    double currentLatitude,currentLongitude;
    Location currentLocationNet;
    double currentLatitudeNet,currentLongitudeNet;
    ArrayList<HashMap<String, String>> evmylist = new ArrayList<HashMap<String, String>>();
    SimpleAdapter eList;


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
        AlertDialog.Builder adb = new AlertDialog.Builder(ChildRegistration.this);
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

    EditText lblBari;
    LinearLayout secChildId;
    TextView VlblChildId;
    //EditText txtChildId;

    EditText txtHH;
    LinearLayout secSNo;
    TextView VlblSNo;
    EditText txtSNo;
    LinearLayout secCID;
    TextView VlblCID;
    EditText txtCID;
    LinearLayout secPID;
    TextView VlblPID;
    EditText txtPID;
    LinearLayout secName;
    TextView VlblName;
    EditText txtName;
    LinearLayout secSex;
    TextView VlblSex;
    RadioGroup rdogrpSex;

    RadioButton rdoSex1;
    RadioButton rdoSex2;
    LinearLayout secBdate;
    TextView VlblBdate;
    EditText dtpBdate;
    ImageButton btnBdate;
    //LinearLayout secAgeM;
    TextView VlblAgeM;
    TextView txtAgeM;
    LinearLayout secMoName;
    TextView VlblMoName;
    EditText txtMoName;
    LinearLayout secFaName;
    TextView VlblFaName;
    EditText txtFaName;
    LinearLayout secMoPNO;
    TextView VlblMoPNO;
    EditText txtMoPNO;
    LinearLayout secEnType;
    TextView VlblEnType;
    Spinner spnEnType;
    LinearLayout secVStDate;
    TextView VlblVStDate;
    EditText dtpVStDate;
    ImageButton btnVStDate;
    Button cmdMig;
    String StartTime;

    String ChildId;
    String Vill;
    String Bari;
    String HH;
    String Status;
    String Old_CID;
    EditText txtPhone;
    String NewOld = "";
    String Migration = "2";
    String CONTACT_NO;
    String Clst;
    String Blc;
    String Card;
    String UNc;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            setContentView(R.layout.childregistration);
            C = new Connection(this);
            g = Global.getInstance();
            StartTime = g.CurrentTime24();

            TableName = "Child";
            Old_CID   = "";

            Bundle B = new Bundle();
            B	     = getIntent().getExtras();

            ChildId  = B.getString("childid");
            Vill     = B.getString("vill");
            Bari     = B.getString("bari");
            Status   = B.getString("status");
            Clst   = B.getString("Cluster");
            Blc   = B.getString("Block");
            NewOld   = "n"; //n-new, o-old

            txtPhone = (EditText)findViewById(R.id.txtPhone);
            lblBari  = (EditText)findViewById(R.id.lblBari);
            lblBari.setText(Bari);
            lblBari.setEnabled(false);
            secChildId=(LinearLayout)findViewById(R.id.secChildId);
            VlblChildId=(TextView) findViewById(R.id.VlblChildId);
            //txtChildId=(EditText) findViewById(R.id.txtChildId);

            txtHH=(EditText) findViewById(R.id.txtHH);


            VlblSNo=(TextView) findViewById(R.id.VlblSNo);
            txtSNo=(EditText) findViewById(R.id.txtSNo);
            secCID=(LinearLayout)findViewById(R.id.secCID);
            VlblCID=(TextView) findViewById(R.id.VlblCID);
            txtCID=(EditText) findViewById(R.id.txtCID);
            txtCID.setEnabled(false);
            secPID=(LinearLayout)findViewById(R.id.secPID);
            VlblPID=(TextView) findViewById(R.id.VlblPID);
            txtPID=(EditText) findViewById(R.id.txtPID);

            String HH = C.ReturnSingleValue("select cast(ifnull(max(cast(HH as int)),69)+1 as text) from Child where vill='"+ Vill +"' and bari='"+ Bari +"' and cast(HH as int)>69");
            txtHH.setText(HH);

            //String SN = C.ReturnSingleValue("select cast(ifnull(max(cast(SNo as int)),87)+1 as text) from Child where vill='"+ Vill +"' and bari='"+ Bari +"' and HH='"+ txtHH.getText().toString() +"' and cast(SNo as int)>87");
            String SN = C.ReturnSingleValue("select cast(ifnull(max(cast(substr(ChildId,10,2) as int)),87)+1 as text) from Child where substr(ChildId,1,9) = '"+ Vill+Bari+txtHH.getText().toString() +"' and cast(substr(ChildId,10,2) as int)>87");

            txtSNo.setText(SN);
            txtCID.setText(Vill + Bari + txtHH.getText().toString() + txtSNo.getText().toString());

            lblBari.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(lblBari.getText().toString().length()==4)
                    {
                        txtCID.setText(Vill + lblBari.getText().toString() + txtHH.getText().toString());
                        txtSNo.setText(C.ReturnSingleValue("select cast(ifnull(max(cast(substr(ChildId,10,2) as int)),87)+1 as text) from Child where substr(ChildId,1,9) = '" + Vill + lblBari.getText().toString() + txtHH.getText().toString() + "' and cast(substr(ChildId,10,2) as int)>87"));
                        txtCID.setText(Vill + lblBari.getText().toString() + txtHH.getText().toString() + txtSNo.getText().toString());
                    }
                    if (txtHH.getText().toString().length() == 2) {
                        txtCID.setText(Vill + lblBari.getText().toString() + txtHH.getText().toString());
                        txtSNo.setText(C.ReturnSingleValue("select cast(ifnull(max(cast(substr(ChildId,10,2) as int)),87)+1 as text) from Child where substr(ChildId,1,9) = '" + Vill + lblBari.getText().toString() + txtHH.getText().toString() + "' and cast(substr(ChildId,10,2) as int)>87"));
                        txtCID.setText(Vill + lblBari.getText().toString() + txtHH.getText().toString() + txtSNo.getText().toString());
                    }

                    if (txtSNo.getText().toString().length() == 2) {
                        txtCID.setText(Vill + lblBari.getText().toString() + txtHH.getText().toString() + txtSNo.getText().toString());
                    }

                }
            });

            txtHH.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (txtHH.getText().toString().length() == 2) {
                        txtCID.setText(Vill + lblBari.getText().toString() + txtHH.getText().toString());
                        txtSNo.setText(C.ReturnSingleValue("select cast(ifnull(max(cast(substr(ChildId,10,2) as int)),87)+1 as text) from Child where substr(ChildId,1,9) = '"+ Vill+lblBari.getText().toString()+txtHH.getText().toString() +"' and cast(substr(ChildId,10,2) as int)>87"));
                        txtCID.setText(Vill + lblBari.getText().toString() + txtHH.getText().toString() + txtSNo.getText().toString());
                    }

                    if (txtSNo.getText().toString().length() == 2) {
                        txtCID.setText(Vill + lblBari.getText().toString() + txtHH.getText().toString() + txtSNo.getText().toString());
                    }

                }
            });

            txtSNo.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (txtHH.getText().toString().length() == 2) {
                        txtCID.setText(Vill + lblBari.getText().toString() + txtHH.getText().toString());
                    }
                    if (txtSNo.getText().toString().length() == 2) {
                        txtCID.setText(Vill + lblBari.getText().toString() + txtHH.getText().toString() + txtSNo.getText().toString());
                    }

                    if (txtSNo.getText().toString().length() > 0) {
                        if (Integer.valueOf(txtSNo.getText().toString()) >= 88 & Migration.equals("2")) {
                            //txtPID.setText("");
                            txtPID.setEnabled(false);
                        } else {
                            txtPID.setEnabled(true);
                        }
                    }
                }
            });

            secName=(LinearLayout)findViewById(R.id.secName);
            VlblName=(TextView) findViewById(R.id.VlblName);
            txtName=(EditText) findViewById(R.id.txtName);
            secSex=(LinearLayout)findViewById(R.id.secSex);
            VlblSex = (TextView) findViewById(R.id.VlblSex);
            rdogrpSex = (RadioGroup) findViewById(R.id.rdogrpSex);

            rdoSex1 = (RadioButton) findViewById(R.id.rdoSex1);
            rdoSex2 = (RadioButton) findViewById(R.id.rdoSex2);
            secBdate=(LinearLayout)findViewById(R.id.secBdate);
            VlblBdate=(TextView) findViewById(R.id.VlblBdate);
            dtpBdate=(EditText) findViewById(R.id.dtpBdate);
            //secAgeM=(LinearLayout)findViewById(R.id.secAgeM);
            VlblAgeM=(TextView) findViewById(R.id.VlblAgeM);
            txtAgeM=(TextView) findViewById(R.id.txtAgeM);
            secMoName=(LinearLayout)findViewById(R.id.secMoName);
            VlblMoName=(TextView) findViewById(R.id.VlblMoName);
            txtMoName=(EditText) findViewById(R.id.txtMoName);
            secFaName=(LinearLayout)findViewById(R.id.secFaName);
            VlblFaName=(TextView) findViewById(R.id.VlblFaName);
            txtFaName=(EditText) findViewById(R.id.txtFaName);
            secMoPNO=(LinearLayout)findViewById(R.id.secMoPNO);
            VlblMoPNO=(TextView) findViewById(R.id.VlblMoPNO);
            txtMoPNO=(EditText) findViewById(R.id.txtMoPNO);
            secEnType=(LinearLayout)findViewById(R.id.secEnType);
            VlblEnType=(TextView) findViewById(R.id.VlblEnType);
            spnEnType=(Spinner) findViewById(R.id.spnEnType);
            List<String> listEnType = new ArrayList<String>();

            listEnType.add("");
            listEnType.add("1-সার্ভিলেন্সের শুরুতেই অর্ন্তভুক্তি");
            listEnType.add("2-জন্মগত");
            listEnType.add("3-মাইগ্রেশন- ইন");
            listEnType.add("4-DSS কর্তৃক নতুন সংযোজন");
            ArrayAdapter<String> adptrEnType= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listEnType);
            spnEnType.setAdapter(adptrEnType);

            secVStDate=(LinearLayout)findViewById(R.id.secVStDate);
            VlblVStDate=(TextView) findViewById(R.id.VlblVStDate);
            dtpVStDate=(EditText) findViewById(R.id.dtpVStDate);
            dtpVStDate.setText(Global.DateNowDMY());

            cmdMig = (Button)findViewById(R.id.cmdMig);
            cmdMig.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    MigrationForm();
                }
            });

            btnBdate = (ImageButton) findViewById(R.id.btnBdate);
            btnBdate.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VariableID = "btnBdate";
                    showDialog(DATE_DIALOG);
                }
            });

            btnVStDate = (ImageButton) findViewById(R.id.btnVStDate);
            btnVStDate.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VariableID = "btnVStDate";
                    showDialog(DATE_DIALOG);
                }
            });

            if(Status.equals("new"))
            {
                cmdMig.setVisibility(View.VISIBLE);
            }
            else if(Status.equals("update"))
            {
                cmdMig.setVisibility(View.GONE);
            }
            else if(Status.equals("new1"))
            {
                cmdMig.setVisibility(View.GONE);
            }

            //IDbundle.putString("mothercid", o.get("vill") + o.get("bari") + o.get("hh") + o.get("sno"));
            if(B.getString("hh").toString().length()>0) {
                txtHH.setText(B.getString("hh"));
                txtHH.setEnabled(false);
                //txtCID.setText(B.getString("mothercid"));
                txtCID.setText(Vill + Bari + txtHH.getText().toString() + txtSNo.getText().toString());
                txtMoPNO.setText(B.getString("motherpid"));
                txtMoPNO.setEnabled(false);
                txtMoName.setText(B.getString("mother"));
                txtMoName.setEnabled(false);
                txtFaName.setText(B.getString("father"));
                spnEnType.setSelection(2);
                spnEnType.setEnabled(true);
            }
            else
            {
                spnEnType.setEnabled(true);
            }

            DataSearch(ChildId);

            Button cmdSave = (Button) findViewById(R.id.cmdSave);
            cmdSave.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    DataSave();
                }});
        }
        catch(Exception  e)
        {
            Connection.MessageBox(ChildRegistration.this, e.getMessage());
            return;
        }
    }

    private void DataSave()
    {
        try {

            String DV = "";
            if(NewOld.equals("o")) {
                if (!C.Existence("Select Bari from Bari where Vill='" + Vill + "' and Bari='" + lblBari.getText().toString() + "'")) {
                    Connection.MessageBox(ChildRegistration.this, "বাড়ী নম্বর সঠিক নয়।");
                    lblBari.requestFocus();
                    return;
                }
            }
            if ((txtPID.getText().toString().equals(txtMoPNO.getText().toString()) & (!txtPID.getText().toString().equals("") & !txtMoPNO.getText().toString().equals("")))) {
                Connection.MessageBox(ChildRegistration.this, "মায়ের সিরিয়াল নাম্বার এবং সদস্যের সিরিয়াল নাম্বার একই রকম হবে না।");
                return;

            }

            if (txtHH.getText().toString().length() == 0) {
                Connection.MessageBox(ChildRegistration.this, "Required field:Household No.");
                txtSNo.requestFocus();
                return;
            } else if (txtSNo.getText().toString().length() == 0) {
                Connection.MessageBox(ChildRegistration.this, "Required field:SNo.");
                txtSNo.requestFocus();
                return;
            } else if (txtCID.getText().toString().length() == 0) {
                Connection.MessageBox(ChildRegistration.this, "Required field:শিশুর চলতি ID নং.");
                txtCID.requestFocus();
                return;
            } else if (txtCID.getText().toString().length() != 11) {
                Connection.MessageBox(ChildRegistration.this, "শিশুর চলতি ID নং. অবশ্যই ১১ সংখ্যার হতে হবে।");
                txtCID.requestFocus();
                return;
            } else if (Integer.valueOf(Global.Right(txtCID.getText().toString(), 2)) < 88 & txtPID.getText().toString().length() == 0) {
                Connection.MessageBox(ChildRegistration.this, "Required field:শিশুর স্থায়ী ID নং.");
                txtPID.requestFocus();
                return;
            } else if (txtPID.getText().toString().length() > 0 & txtPID.getText().toString().length() != 8) {
                Connection.MessageBox(ChildRegistration.this, "শিশুর স্থায়ী ID নং. অবশ্যই ৮ সংখ্যার হতে হবে।");
                txtPID.requestFocus();
                return;
            } else if (txtName.getText().toString().length() == 0 & secName.isShown()) {
                Connection.MessageBox(ChildRegistration.this, "Required field:নাম.");
                txtName.requestFocus();
                return;
            } else if (!rdoSex1.isChecked() & !rdoSex2.isChecked() & secSex.isShown()) {
                Connection.MessageBox(ChildRegistration.this, "Select anyone options from Sex.");
                rdoSex1.requestFocus();
                return;
            }
            DV = Global.DateValidate(dtpBdate.getText().toString());
            if (DV.length() != 0 & secBdate.isShown()) {
                Connection.MessageBox(ChildRegistration.this, DV);
                dtpBdate.requestFocus();
                return;
            } else if (txtMoName.getText().toString().length() == 0 & secMoName.isShown()) {
                Connection.MessageBox(ChildRegistration.this, "Required field:মায়ের নাম .");
                txtMoName.requestFocus();
                return;

            } else if (txtFaName.getText().toString().length() == 0 & secFaName.isShown()) {
                Connection.MessageBox(ChildRegistration.this, "Required field:পিতার নাম.");
                txtFaName.requestFocus();
                return;
            } else if (txtMoPNO.getText().toString().length() > 0 & txtMoPNO.getText().toString().length() != 8) {
                Connection.MessageBox(ChildRegistration.this, "মায়ের স্থায়ী ID নং. অবশ্যই ৮ সংখ্যার হতে হবে");
                txtMoPNO.requestFocus();
                return;
            }
            if (txtPID.getText().toString().length() > 0){
                if (!C.Existence("select vill from mdssvill where vill='" + Global.Left(txtPID.getText().toString(), 3) + "'")) {
                    Connection.MessageBox(ChildRegistration.this, "শিশুর স্থায়ী ID নং. সঠিক নয়");
                    txtPID.requestFocus();
                    return;
                }
            }

            if (txtMoPNO.getText().toString().length() > 0){
                if (!C.Existence("select vill from mdssvill where vill='" + Global.Left(txtMoPNO.getText().toString(), 3) + "'")) {
                    Connection.MessageBox(ChildRegistration.this, "মায়ের স্থায়ী ID নং. সঠিক নয়");
                    txtMoPNO.requestFocus();
                    return;
                }
            }

            if(spnEnType.getSelectedItemPosition()==0  & secEnType.isShown())
            {
                Connection.MessageBox(ChildRegistration.this, "Required field:নিবন্ধনের ধরণ.");
                spnEnType.requestFocus();
                return;
            }
            DV = Global.DateValidate(dtpVStDate.getText().toString());
            if(DV.length()!=0 & secVStDate.isShown())
            {
                Connection.MessageBox(ChildRegistration.this, DV);
                dtpVStDate.requestFocus();
                return;
            }

            Integer age = Global.DateDifferenceDays(dtpVStDate.getText().toString(),dtpBdate.getText().toString());

            if(age < 0)
            {
                Connection.MessageBox(ChildRegistration.this, "জন্ম তারিখ অবশ্যই প্রথম পরিদর্শনের তারিখের কম হতে হবে।");
                dtpBdate.requestFocus();
                return;
            }



            String SQL = "";
            boolean childexists = C.Existence("Select ChildId from " + TableName + "  Where ChildId='" + Vill + lblBari.getText().toString() + txtHH.getText().toString()+txtSNo.getText().toString() +"'");

            if(age >= 1825 & !childexists)
            {
                Connection.MessageBox(ChildRegistration.this, "শিশুর বয়স অবশ্যই ৫ বছরের নীচে হতে হবে।");
                dtpBdate.requestFocus();
                return;
            }

            if(ChildId.length()==0 & childexists)
            {
                Connection.MessageBox(ChildRegistration.this,"Duplicate child CID.");
                return;
            }

            if(ChildId.length()==0)
                ChildId = Vill+lblBari.getText().toString()+txtHH.getText().toString()+txtSNo.getText().toString();


            if(!C.Existence("Select ChildId from " + TableName + "  Where ChildId='"+ ChildId +"'"))
            {
                SQL = "Insert into " + TableName + "(ChildId,CID,PID,UserId,EnDt,Upload)Values('"+ ChildId +"','','','"+ g.getUserId() +"','"+ Global.DateTimeNowYMDHMS() +"','2')";
                C.Save(SQL);
            }
            else
            {

            }

            SQL = "Update " + TableName + " Set Upload='2',";
            SQL+="ContactNo='"+ txtPhone.getText().toString() +"',";
            SQL+="Vill = '"+ Vill +"',";
            SQL+="Bari = '"+ lblBari.getText().toString() +"',";
            SQL+="HH = '"+ txtHH.getText() +"',";
            SQL+="SNo = '"+ txtSNo.getText().toString() +"',";
            SQL+="CID = '"+ txtCID.getText().toString() +"',";
            SQL+="PID = '"+ txtPID.getText().toString() +"',";
            SQL+="Name = '"+ txtName.getText().toString().toUpperCase() +"',";
            RadioButton rbSex = (RadioButton)findViewById(rdogrpSex.getCheckedRadioButtonId());
            if(rdoSex1.isChecked())
                SQL+="Sex = '1',";
            else if(rdoSex2.isChecked())
                SQL+="Sex = '2',";
            else
                SQL+="Sex = '',";

            SQL+="Bdate = '"+ Global.DateConvertYMD(dtpBdate.getText().toString()) +"',";
            SQL+="AgeM = '"+ txtAgeM.getText().toString() +"',";
            SQL+="MoName = '"+ txtMoName.getText().toString().toUpperCase() +"',";
            SQL+="FaName = '"+ txtFaName.getText().toString().toUpperCase() +"',";
            SQL+="MoPNO = '"+ txtMoPNO.getText().toString() +"',";
            SQL+="EnType = '"+ (spnEnType.getSelectedItemPosition()==0?"":Global.Left(spnEnType.getSelectedItem().toString(),1)) +"',";
            SQL+="EnDate = '"+ Global.DateConvertYMD(dtpVStDate.getText().toString()) +"',";

            //SQL+="ExType = '',";
            //SQL+="ExDate = '',";
            //20 Sep 2016
            if(Migration.equals("1"))
            {
                SQL+="ExType = '',";
                SQL+="ExDate = '',";
            }

            SQL+="VStDate = '"+ Global.DateConvertYMD(dtpVStDate.getText().toString()) +"'";
            SQL+="  Where ChildId='"+ ChildId +"'";
            C.Save(SQL);

            //Update log for changeing CID
            if(Old_CID.length() > 0 & !Old_CID.toUpperCase().equals(txtCID.getText().toString().toUpperCase()))
            {
                SQL  = "Insert into CID_Update_Log(ChildId, NewCID, OldCID, ChangeType, UserId, UpdateDT, Status, Upload)Values(";
                SQL += "'"+ ChildId +"',";
                SQL += "'"+ txtCID.getText().toString() +"',";
                SQL += "'"+ Old_CID +"',";
                SQL += "'u',";
                SQL += "'"+ g.getUserId() +"',";
                SQL += "'"+ Global.DateTimeNowYMDHMS() +"',";
                SQL += "'1',";
                SQL += "'2')";

                C.Save(SQL);
            }

            //remove from migration list if child migrated from others location
            if(Migration.equals("1"))
            {
                C.Save("Delete from MigChild where ChildId='"+ ChildId +"'");
            }



//            21/08/2020  SMS

//            CHRF
//                    <5 Child card
//            CID:32702240388
//            Name: aaaaaaaaaaaa
//            Sex:Female
//            DOB:27/09/19
//            Mo name: aaaaaaaaaaaa
//            Mo PNO:22824635
//            Fa name: aaaaaaaaaaaa
//            Cl:01
//            Bl:01
//            Union:17
//   -----------------------------------------------------
//            String CDOB;
//            CDOB=C.ReturnSingleValue("Select BDate  from Child WHERE   ChildID = '"+ ChildID +"'");

            UNc=C.ReturnSingleValue("select v.UName from Bari b left outer join MDSSVill v on b.vill=v.vill where b.Cluster='"+ Clst +"'");


                Card="Under 5 Child card";


//------------------------------------------------------

            AlertDialog.Builder alert=new AlertDialog.Builder(this);
            alert.setTitle("Confirm");
            alert.setMessage("Do you want to send the message?");
            alert.setNegativeButton("No", null);
            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int asd) {
                    CONTACT_NO = txtPhone.getText().toString();
                    String sex=rdoSex1.isChecked()?"Male":rdoSex2.isChecked()?"Female":"";

                        String[] mob={CONTACT_NO,"01813364948"};
                    //String[] mob={"01813364948"};
//                    String[] mob = {CONTACT_NO, "01739957707"};
                    String SMS = "" + "," +
                            "CHRF:" + Card +
                            "CID:" + txtCID.getText().toString() + "," +
                            "Name:" + txtName.getText().toString() + "," +
                            "Sex:" + sex + "," +
                            "DOB:" + dtpBdate.getText().toString()+
                            "Mother:" + txtMoName.getText().toString() + "," +
                            "PNO:" + txtMoPNO.getText().toString() + "," +
                            "Father:" + txtFaName.getText().toString() + ","+
                            "Cluster:" + Clst + "," +
                            "Block:" + Blc + "." +
                            "Union:" + UNc + ",";

                    for (int i = 0; i < mob.length; i++) sendSMS(mob[i], SMS);


                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, returnIntent);
//                    finish();
                }
            });

            AlertDialog alertDialog=alert.create();
            alertDialog.show();

//            finish();

//    21/08/2020

            Connection.MessageBox(ChildRegistration.this, "Saved Successfully");
//            finish();

        }
        catch(Exception  e)
        {
            Connection.MessageBox(ChildRegistration.this, e.getMessage());
            return;
        }
    }

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
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
    }

    private void DataSearch(String ChildId)
    {
        try
        {

            RadioButton rb;
            Cursor cur = C.ReadData("Select ChildId, Vill, bari, HH, SNo, PID, CID, Name, Sex, BDate, AgeM, MoNo, MoPNO, MoName, FaNo, FaPNO, FaName, EnType, EnDate, ExType, ExDate, VStDate, ContactNo from  "+ TableName +"  Where ChildId='"+ ChildId +"'");
            cur.moveToFirst();
            while(!cur.isAfterLast())
            {
                lblBari.setEnabled(true);
                NewOld = "o";
                txtHH.setText(cur.getString(cur.getColumnIndex("HH")));
                txtSNo.setText(cur.getString(cur.getColumnIndex("SNo")));
                txtCID.setText(cur.getString(cur.getColumnIndex("CID")));
                Old_CID = cur.getString(cur.getColumnIndex("CID"));

                txtPID.setText(cur.getString(cur.getColumnIndex("PID")));
                txtName.setText(cur.getString(cur.getColumnIndex("Name")));
                if(cur.getString(cur.getColumnIndex("Sex")).equals("1"))
                    rdoSex1.setChecked(true);
                else if(cur.getString(cur.getColumnIndex("Sex")).equals("2"))
                    rdoSex2.setChecked(true);

                dtpBdate.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("BDate"))));
                txtAgeM.setText(cur.getString(cur.getColumnIndex("AgeM")));
                txtMoName.setText(cur.getString(cur.getColumnIndex("MoName")));
                txtFaName.setText(cur.getString(cur.getColumnIndex("FaName")));
                txtMoPNO.setText(cur.getString(cur.getColumnIndex("MoPNO")));
                spnEnType.setSelection(Global.SpinnerItemPosition(spnEnType, 1 ,cur.getString(cur.getColumnIndex("EnType"))));
                dtpVStDate.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("VStDate"))));
                txtPhone.setText(cur.getString(cur.getColumnIndex("ContactNo")));

                Integer age = Global.DateDifferenceDays(Global.DateNowDMY(),dtpBdate.getText().toString());
                if(age <= 28)
                {
                    txtAgeM.setText(age.toString());
                    VlblAgeM.setText("দিন");
                }
                else
                {
                    txtAgeM.setText(String.valueOf(age / 30));
                    VlblAgeM.setText("মাস");
                }

                cur.moveToNext();
            }
            cur.close();
        }
        catch(Exception  e)
        {
            Connection.MessageBox(ChildRegistration.this, e.getMessage());
            return;
        }
    }




    protected Dialog onCreateDialog(int id) {
        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        String DT = dtpBdate.getText().toString();
        int mYear;
        int mMonth;
        int mDay;

        if(DT.length()==10) {
            mYear = Integer.valueOf(Global.Right(DT, 4));
            mMonth = Integer.valueOf(DT.substring(3, 5));
            mDay = Integer.valueOf(Global.Left(DT, 2));
        }
        else
        {
            mYear = g.mYear;
            mMonth = g.mMonth;
            mDay = g.mDay;
        }

        switch (id) {
            case DATE_DIALOG:
                return new DatePickerDialog(this, mDateSetListener,mYear,mMonth-1,mDay);
            case TIME_DIALOG:
                return new TimePickerDialog(this, timePickerListener, hour, minute,false);
        }
        return null;
    }
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year; mMonth = monthOfYear+1; mDay = dayOfMonth;
            EditText dtpDate;


            dtpDate = (EditText)findViewById(R.id.dtpBdate);
            if (VariableID.equals("btnBdate"))
            {
                dtpDate = (EditText)findViewById(R.id.dtpBdate);
                dtpDate.setText(new StringBuilder()
                        .append(Global.Right("00"+mDay,2)).append("/")
                        .append(Global.Right("00"+mMonth,2)).append("/")
                        .append(mYear));

                Integer age = Global.DateDifferenceDays(Global.DateNowDMY(),dtpDate.getText().toString());
                if(age <= 28)
                {
                    txtAgeM.setText(age.toString());
                    VlblAgeM.setText("দিন");
                }
                else
                {
                    txtAgeM.setText(String.valueOf(age / 30.44));
                    VlblAgeM.setText("মাস");
                }

            }
            else if (VariableID.equals("btnVStDate"))
            {
                dtpDate = (EditText)findViewById(R.id.dtpVStDate);
                dtpDate.setText(new StringBuilder()
                        .append(Global.Right("00"+mDay,2)).append("/")
                        .append(Global.Right("00"+mMonth,2)).append("/")
                        .append(mYear));
            }
/*
            dtpDate.setText(new StringBuilder()
                    .append(Global.Right("00"+mDay,2)).append("/")
                    .append(Global.Right("00"+mMonth,2)).append("/")
                    .append(mYear));
*/


        }
    };
    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
            hour = selectedHour; minute = selectedMinute;
            EditText tpTime;


            //tpTime.setText(new StringBuilder().append(Global.Right("00"+hour,2)).append(":").append(Global.Right("00"+minute,2)));

        }
    };





    private void MigrationForm()
    {
        try
        {
            final Dialog dialog = new Dialog(ChildRegistration.this);

            dialog .requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.migration);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(true);
            Window window = dialog.getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();

            wlp.gravity = Gravity.TOP;
            wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(wlp);


            TextView lblMigTitle = (TextView)dialog.findViewById(R.id.lblMigTitle);
            final Spinner VillageList = (Spinner)dialog.findViewById(R.id.VillageList);

            VillageList.setAdapter(C.getArrayAdapter("select vill||' - '||vname from mdssvill where vill in(Select distinct vill from MigChild) order by vname asc"));
            lblMigTitle.setText(" মাইগ্রেশন- ইন ");

            final EditText txtMember = (EditText)dialog.findViewById(R.id.txtMember);

            final ListView evlist = (ListView)dialog.findViewById(R.id.lstMigration);
            //View header = getLayoutInflater().inflate(R.layout.migrationheading, null);
            //evlist.addHeaderView(header);

            VillageList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    String V = Global.Left(VillageList.getSelectedItem().toString(),3);
                    MigrationData(V, txtMember.getText().toString(),dialog,evlist,"no");
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }
            });

            txtMember.addTextChangedListener(new TextWatcher(){
                public void afterTextChanged(Editable s) {}
                public void beforeTextChanged(CharSequence s, int start, int count, int after){}
                public void onTextChanged(CharSequence s, int start, int before, int count){
                    String V = Global.Left(VillageList.getSelectedItem().toString(),3);
                    //need to consider Same cluster/different cluster
                    MigrationData(V,txtMember.getText().toString(),dialog,evlist,"yes");
                }});

            Button cmdMigListClose = (Button)dialog.findViewById(R.id.cmdMigListClose);
            cmdMigListClose.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    dialog.dismiss();
                }
            });
            Button cmdMigListOk = (Button)dialog.findViewById(R.id.cmdMigListOk);
            cmdMigListOk.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    if(g.getChildId().trim().length()==0)
                    {
                        Connection.MessageBox(ChildRegistration.this, " সদস্য সিলেক্ট করা হয়নি, প্রথমে তালিকা থেকে সদস্য সিলেক্ট করুন।");
                        return;
                    }

                    ChildId = g.getChildId();
                    txtPID.setText(g.getPNo());
                    txtName.setText(g.getName());
                    if(g.getSex().equals("1"))
                        rdoSex1.setChecked(true);
                    else
                        rdoSex2.setChecked(true);

                    dtpBdate.setText(g.getDOB());
                    Integer age = Global.DateDifferenceDays(Global.DateNowDMY(),dtpBdate.getText().toString());
                    if(age <= 28)
                    {
                        txtAgeM.setText(age.toString());
                        VlblAgeM.setText("দিন");
                    }
                    else
                    {
                        txtAgeM.setText(String.valueOf(age / 30.44));
                        VlblAgeM.setText("মাস");
                    }

                    txtMoName.setText(g.getMother());
                    txtFaName.setText(g.getFather());
                    txtMoPNO.setText(g.getMotherPNo());
                    spnEnType.setSelection(3);
                    spnEnType.setEnabled(false);
                    txtPhone.setText(g.getContactNo());
                    Migration = "1";

                    //EditText txtPNo = (EditText)d.findViewById(R.id.txtQPNo);
                    //EditText EvDate = (EditText)d.findViewById(R.id.EvDate);
                    //txtPNo.setText(g.getPNo());
                    //EvDate.setText(g.getEvDate());
                    dialog.dismiss();
                }
            });

            dialog.show();
        }
        catch(Exception  e)
        {
            Connection.MessageBox(ChildRegistration.this, e.getMessage());
            return;
        }
    }

    private void MigrationData(String Village, String Name,final Dialog d,final ListView evlist, String Search)
    {
        String SQL = "";
        SQL = "Select childid as childid, vill as vill, bari as bari, hh as hh, Sno as Sno,PID Pno,Name as Name,Sex as sex, bdate as bdate,moname as mother,mopno as mopno,faname as father,ExDate as ExDate,ifnull(ContactNo,'') as contactno from MigChild where Vill='"+ Village +"' and (Name like('"+ Name +"%') or PID like('"+ Name + "%'))order by name asc";

        Cursor cur1 = C.ReadData(SQL);

        cur1.moveToFirst();
        evmylist.clear();
        eList = null;
        evlist.setAdapter(null);

        while(!cur1.isAfterLast())
        {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("childid",   cur1.getString(cur1.getColumnIndex("childid")));
            map.put("vill",   cur1.getString(cur1.getColumnIndex("vill")));
            map.put("bari",   cur1.getString(cur1.getColumnIndex("bari")));

            map.put("hh",   cur1.getString(cur1.getColumnIndex("hh")));
            map.put("sno",  cur1.getString(cur1.getColumnIndex("Sno")));
            map.put("pno",  cur1.getString(cur1.getColumnIndex("Pno")));
            map.put("name", cur1.getString(cur1.getColumnIndex("Name")));
            map.put("sex", cur1.getString(cur1.getColumnIndex("sex")));
            map.put("bdate", cur1.getString(cur1.getColumnIndex("bdate")));
            map.put("mother", cur1.getString(cur1.getColumnIndex("mother")));
            map.put("mopno", cur1.getString(cur1.getColumnIndex("mopno")));
            map.put("father", cur1.getString(cur1.getColumnIndex("father")));
            map.put("contactno", cur1.getString(cur1.getColumnIndex("contactno")));
            map.put("exdate", Global.DateConvertDMY(cur1.getString(cur1.getColumnIndex("ExDate"))));

            evmylist.add(map);

            eList = new SimpleAdapter(ChildRegistration.this, evmylist, R.layout.migrationrow,
                    new String[] {"childid"},
                    new int[] {R.id.lblCID});
            //evlist.setAdapter(eList);
            evlist.setAdapter(new MigrationListAdapter(this,d));

            cur1.moveToNext();
        }


        cur1.close();

        /*evlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                String[] a = listView.getItemAtPosition(position).toString().split(",");
                String E  = a[7].substring(8, a[7].length());
                String P  = a[10].substring(5, a[10].length());
                String N  = a[3].substring(6, a[3].length());
                String CN = a[6].substring(11, a[6].length());

                TextView lblName = (TextView)d.findViewById(R.id.lblName);
                lblName.setText("Name: " + N + " [Outdate: " + E + "]");

                //g.setPNo(P);
                g.setEvDate(E);
                g.setChildId(a[5].substring(9, a[5].length()));
                g.setPNo(P);
                g.setName(a[3].substring(6, a[3].length()));
                g.setSex(a[0].substring(5, a[0].length()));
                g.setDOB(a[7].substring(8, a[7].length()));
                g.setMother(a[2].substring(8, a[2].length()));
                g.setMotherPNo(a[8].substring(7, a[8].length()));
                g.setFather(a[11].substring(8, a[11].length() - 1));
                g.setContactNo(CN);

                //g.setPrevHousehold(H);
                //Object o =  listView.getItemAtPosition(position);
            }
        });*/
    }


    public class MigrationListAdapter extends BaseAdapter
    {
        private Context context;
        private Dialog dg;
        public MigrationListAdapter(Context c,Dialog d){
            context = c;
            dg=d;
        }

        public int getCount() {
            return eList.getCount();
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
                convertView = inflater.inflate(R.layout.migrationrow, null);
            }

            final HashMap<String, String> o = (HashMap<String, String>) eList.getItem(position);

            TextView lblName   = (TextView)convertView.findViewById(R.id.lblName);
            TextView lblDOB   = (TextView)convertView.findViewById(R.id.lblDOB);
            TextView lblFatherMother  = (TextView)convertView.findViewById(R.id.lblFatherMother);
            TextView lblCID= (TextView)convertView.findViewById(R.id.lblCID);
            TextView lblPID= (TextView)convertView.findViewById(R.id.lblPID);

            lblName.setText(": "+ o.get("name"));
            lblDOB.setText(": "+ Global.DateConvertDMY(o.get("bdate")));
            String FM = o.get("father");
            if(o.get("mother").toString().length()>0)
                FM += "/"+ o.get("mother");

            lblFatherMother.setText(": "+ FM);

            //lblFatherMother.setText(": "+ o.get("father"));
            lblCID.setText(": "+ o.get("vill") + o.get("bari")+o.get("hh")+o.get("sno"));
            //lblCID.setText(": "+ o.get("childid"));
            lblPID.setText(": "+ o.get("pno"));

            LinearLayout mig_row = (LinearLayout)convertView.findViewById(R.id.mig_row);
            mig_row.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    TextView lblTextName = (TextView)dg.findViewById(R.id.lblTextName);
                    //lblTextName.setText("Name: " + o.get("name") + " [Outdate: " + o.get("exdate") + "]");
                    String FaMo = o.get("father");
                    if(o.get("mother").toString().length()>0)
                        FaMo += "/"+ o.get("mother");

                    lblTextName.setText("নাম: " + o.get("name") + ",পিতা/মাতা: "+ FaMo);

                    g.setEvDate(o.get("exdate"));
                    g.setChildId(o.get("childid"));
                    g.setPNo(o.get("pno"));
                    g.setName(o.get("name"));
                    g.setSex(o.get("sex"));
                    g.setDOB(Global.DateConvertDMY(o.get("bdate")));
                    g.setMother(o.get("mother"));
                    g.setMotherPNo(o.get("mopno"));
                    g.setFather(o.get("father"));
                    g.setContactNo(o.get("contactno"));
                }
            });

            return convertView;
        }

    }

}
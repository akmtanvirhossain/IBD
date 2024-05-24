package org.icddrb.ibd;

/**
 * Created by TanvirHossain on 20/11/2015.
 */


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

public class FollowUpVisit extends Activity {
    boolean netwoekAvailable=false;
    Location currentLocation;
    double currentLatitude,currentLongitude;

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
        AlertDialog.Builder adb = new AlertDialog.Builder(FollowUpVisit.this);
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
            /*case R.id.prev:
                return  true;
            case R.id.next:
                return true;*/
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

    LinearLayout secCID;
    TextView VlblCID;
    TextView txtCID;
    LinearLayout secPID;
    TextView VlblPID;
    TextView txtPID;
    LinearLayout secWeek;
    TextView VlblWeek;
    TextView txtWeek;
    LinearLayout secVDate;
    TextView VlblVDate;
    EditText dtpVDate;
    ImageButton btnVDate;
    LinearLayout secVstat;
    TextView VlblVstat;
    Spinner spnVstat;
    LinearLayout secSickStatus;
    TextView VlblSickStatus;
    RadioGroup rdogrpSickStatus;

    RadioButton rdoSickStatus1;
    RadioButton rdoSickStatus2;
    RadioButton rdoSickStatus3;

    LinearLayout secExDate;
    TextView VlblExDate;
    EditText dtpExDate;
    ImageButton btnExDate;

    LinearLayout secRSVStatus;
    View lineRSVStatus;
    TextView VlblRSVStatus;
    RadioGroup rdogrpRSVStatus;
    RadioButton rdoRSVStatus1;
    RadioButton rdoRSVStatus2;

    String StartTime;
    String CID;
    String PID;
    String ChildID;
    String WeekNo;
    String VisitType;
    String VisitNo;
    String AgeM;
    String AgeD;
    String DOB;
    String FM;
    String VILLAGE;
    String CONTACT_NO;

    TextView lblCName;
    TextView FatherMother;
    TextView Age;
    TextView txtDOB;
    String AgeDM;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            setContentView(R.layout.followup_visit);
            C = new Connection(this);
            g = Global.getInstance();
            StartTime = g.CurrentTime24();

            TableName = "Visits";

            turnGPSOn();

            //GPS Location
            FindLocation();


            Bundle B = new Bundle();
            B	     = getIntent().getExtras();

            ChildID  = B.getString("childid");
            AgeD     = B.getString("aged");
            AgeM     = B.getString("agem");
            DOB      = B.getString("bdate");
            WeekNo   = B.getString("weekno");
            VisitType = B.getString("visittype");
            CID       = B.getString("cid");
            PID       = B.getString("pid");
            VILLAGE       = B.getString("village");
            CONTACT_NO       = B.getString("contactno");

            FatherMother = (TextView)findViewById(R.id.FatherMother);
            FatherMother.setText(": "+ B.getString("fm"));
            FM = B.getString("fm");

            Age = (TextView)findViewById(R.id.Age);
            Age.setText(": "+B.getString("agedm"));
            AgeDM = B.getString("agedm");

            txtDOB = (TextView)findViewById(R.id.txtDOB);
            txtDOB.setText(Global.DateConvertDMY(DOB));

            lblCName = (TextView)findViewById(R.id.lblCName);
            lblCName.setText(": "+ B.getString("name"));

            secCID=(LinearLayout)findViewById(R.id.secCID);
            VlblCID=(TextView) findViewById(R.id.VlblCID);
            txtCID=(TextView) findViewById(R.id.txtCID);
            txtCID.setText(": "+B.getString("cid"));

            secPID=(LinearLayout)findViewById(R.id.secPID);
            VlblPID=(TextView) findViewById(R.id.VlblPID);
            txtPID=(TextView) findViewById(R.id.txtPID);
            txtPID.setText(": "+B.getString("pid"));

            secWeek=(LinearLayout)findViewById(R.id.secWeek);
            VlblWeek=(TextView) findViewById(R.id.VlblWeek);
            txtWeek=(TextView) findViewById(R.id.txtWeek);
            txtWeek.setText(": "+WeekNo);

            secVDate=(LinearLayout)findViewById(R.id.secVDate);
            VlblVDate=(TextView) findViewById(R.id.VlblVDate);
            dtpVDate=(EditText) findViewById(R.id.dtpVDate);
            dtpVDate.setText(Global.DateNowDMY());

            secVstat=(LinearLayout)findViewById(R.id.secVstat);
            VlblVstat=(TextView) findViewById(R.id.VlblVstat);
            spnVstat=(Spinner) findViewById(R.id.spnVstat);
            List<String> listVstat = new ArrayList<String>();

            listVstat.add("");
            listVstat.add("1-শিশু উপস্থিত");
            listVstat.add("2-শিশু অনুপস্থিত");
            listVstat.add("3-চিকিৎসার জন্য অনুপস্থিত  ");
            //listVstat.add("4-বয়স উত্তীর্ণ");
            listVstat.add("5-স্থানান্তর");
            listVstat.add("6-মৃত্যুবরন");
            listVstat.add("7-সার্ভিলেন্সে থাকতে অসম্মতি");
            listVstat.add("8-VHW ছুটিতে আছে");
            listVstat.add("9-তথ্য পাওয়া জায়নি");
            listVstat.add("0-পুনঃ আগমন");
            listVstat.add("10-অপ্রত্যাশিত কারণ");
            listVstat.add("11-ঝড় / বৃষ্টির কারণ");
            listVstat.add("12-বন্যার কারণ");
            listVstat.add("13-সরকারী ছুটির দিন");
            listVstat.add("15-Training/Meeting");
            listVstat.add("14-অন্যান্য কারণ");
//            listVstat.add("16-ফোন কল রিসিভ: সাক্ষাতকার দিতে সম্মত হয়েছে");
//          //block code (17-27) dated:19-Oct-2021
//          //17-27 code use for lockdown
            listVstat.add("17-ফোন কল রিসিভ: সাক্ষাতকার দিতে রাজি নন");
            listVstat.add("18-ফোন কল রিসিভ করেনি");
            listVstat.add("19-ফোন সুইচ অফ");
            listVstat.add("20-ভুল নম্বর");
            listVstat.add("21-ফোন কল রিসিভ: সাক্ষাতকার দিতে সম্মত হয়েছেহয়েছে। শিশু উপস্থিত(সুস্থ আছে)");
            listVstat.add("22-ফোন কল রিসিভ: সাক্ষাতকার দিতে সম্মত হয়েছেহয়েছে। শিশু উপস্থিত(অসুস্থ আছে)");
            listVstat.add("23-ফোন কল রিসিভ: সাক্ষাতকার দিতে সম্মত হয়েছেহয়েছে। শিশু অনুপস্থিত(সুস্থ আছে)");
            listVstat.add("24-ফোন কল রিসিভ: সাক্ষাতকার দিতে সম্মত হয়েছেহয়েছে। শিশু অনুপস্থিত(অসুস্থ আছে)");
            listVstat.add("25-ফোন কল রিসিভ: সাক্ষাতকার দিতে সম্মত হয়েছেহয়েছে। শিশু চিকিৎসার জন্য অনুপস্থিত(অসুস্থ আছে)");
            listVstat.add("26-ফোন কল রিসিভ: সাক্ষাতকার দিতে সম্মত হয়েছেহয়েছে। (স্থানান্তর)");
            listVstat.add("27-ফোন কল রিসিভ: সাক্ষাতকার দিতে সম্মত হয়েছেহয়েছে। (মৃত্যুবরণ)");

            ArrayAdapter<String> adptrVstat= new ArrayAdapter<String>(this, R.layout.multiline_spinner_dropdown_item, listVstat);
            spnVstat.setAdapter(adptrVstat);


            spnVstat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    if (spnVstat.getSelectedItem().toString().length() == 0) return;
                    //String spnData = Global.Left(spnVstat.getSelectedItem().toString(), 1);
                    String[] VS = spnVstat.getSelectedItem().toString().split("-");
                    String spnData = VS[0];


                    //if (spnData.equalsIgnoreCase("2") | spnData.equalsIgnoreCase("3") | spnData.equalsIgnoreCase("4") | spnData.equalsIgnoreCase("7") | spnData.equalsIgnoreCase("8") | spnData.equalsIgnoreCase("9")) {
                    if (spnData.equalsIgnoreCase("4") | spnData.equalsIgnoreCase("7") | spnData.equalsIgnoreCase("8") | spnData.equalsIgnoreCase("9")) {
                        secSickStatus.setVisibility(View.GONE);
                        secExDate.setVisibility(View.GONE);
                        dtpExDate.setText("");
                        secRSVStatus.setVisibility(View.GONE);

                    } else if (spnData.equalsIgnoreCase("5")) {
                        secSickStatus.setVisibility(View.GONE);
                        secExDate.setVisibility(View.VISIBLE);
                        VlblExDate.setText("স্থানান্তরের তারিখ");
                        dtpExDate.setText(Global.DateNowDMY());
                        secRSVStatus.setVisibility(View.GONE);
//                    24/07/2020
                    } else if (spnData.equalsIgnoreCase("26")) {
                        secSickStatus.setVisibility(View.GONE);
                        secExDate.setVisibility(View.VISIBLE);
                        VlblExDate.setText("স্থানান্তরের তারিখ");
                        dtpExDate.setText(Global.DateNowDMY());
                        secRSVStatus.setVisibility(View.GONE);

                    } else if (spnData.equalsIgnoreCase("6")) {
                        secSickStatus.setVisibility(View.GONE);
                        secExDate.setVisibility(View.VISIBLE);
                        VlblExDate.setText("মৃত্যুর তারিখ");
                        secRSVStatus.setVisibility(View.GONE);
//                    24/07/2020
                    } else if (spnData.equalsIgnoreCase("27")) {
                        secSickStatus.setVisibility(View.GONE);
                        secExDate.setVisibility(View.VISIBLE);
                        VlblExDate.setText("মৃত্যুর তারিখ");
                        secRSVStatus.setVisibility(View.GONE);

                    } else if (spnData.equalsIgnoreCase("0")) {
                        secSickStatus.setVisibility(View.VISIBLE);
                        secExDate.setVisibility(View.GONE);
                        dtpExDate.setText("");

                    } else if (spnData.equalsIgnoreCase("1") | spnData.equalsIgnoreCase("2") | spnData.equalsIgnoreCase("3") | spnData.equalsIgnoreCase("21")| spnData.equalsIgnoreCase("22")| spnData.equalsIgnoreCase("23")| spnData.equalsIgnoreCase("24")| spnData.equalsIgnoreCase("25")) {
                        secSickStatus.setVisibility(View.VISIBLE);
                        secExDate.setVisibility(View.GONE);


                    } else {
                        secSickStatus.setVisibility(View.GONE);
                        secExDate.setVisibility(View.GONE);
                        dtpExDate.setText("");
                        secRSVStatus.setVisibility(View.GONE);
                        rdogrpRSVStatus.clearCheck();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }
            });
            secSickStatus=(LinearLayout)findViewById(R.id.secSickStatus);
            VlblSickStatus = (TextView) findViewById(R.id.VlblSickStatus);
            rdogrpSickStatus = (RadioGroup) findViewById(R.id.rdogrpSickStatus);

            rdoSickStatus1 = (RadioButton) findViewById(R.id.rdoSickStatus1);
            rdoSickStatus2 = (RadioButton) findViewById(R.id.rdoSickStatus2);
            rdoSickStatus3 = (RadioButton) findViewById(R.id.rdoSickStatus3);


                rdogrpSickStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int radioButtonID) {
                        String CDOB1;
                        CDOB1 = C.ReturnSingleValue("Select BDate  from Child WHERE   ChildID = '" + ChildID + "'");

                        int daa = Global.DateDifferenceDays(dtpVDate.getText().toString(), Global.DateConvertDMY(CDOB1));
                        int moo = (int) (daa / 30.44);


                        String rbData = "";
                        RadioButton rb;
                        String[] d_rdogrpRSVsuitable = new String[]{"1", "2", "3"};
                        for (int i = 0; i < rdogrpSickStatus.getChildCount(); i++) {
                            rb = (RadioButton) rdogrpSickStatus.getChildAt(i);
                            if (rb.isChecked()) rbData = d_rdogrpRSVsuitable[i];
                        }

                        if (rbData.equalsIgnoreCase("2") || rbData.equalsIgnoreCase("3")) {
                            secRSVStatus.setVisibility(View.GONE);
                            lineRSVStatus.setVisibility(View.GONE);
                            rdogrpRSVStatus.clearCheck();

                        }
                        String[] VS1 = spnVstat.getSelectedItem().toString().split("-");
                        String spnData = VS1[0];

                        if (rbData.equalsIgnoreCase("1")) {
//Cluster check for RSV***********************
                            String RSVCluster = C.ReturnSingleValue("Select Cluster from Cluster");
                            if (RSVCluster.equals("001") || RSVCluster.equals("002") || RSVCluster.equals("003") || RSVCluster.equals("004") || RSVCluster.equals("005") || RSVCluster.equals("006") || RSVCluster.equals("007") || RSVCluster.equals("008") || RSVCluster.equals("009") || RSVCluster.equals("010") || RSVCluster.equals("011") || RSVCluster.equals("012") || RSVCluster.equals("013") || RSVCluster.equals("014") || RSVCluster.equals("015") || (RSVCluster.equals("016") & VILLAGE.substring(0, 3).equals("307")) || RSVCluster.equals("017") || RSVCluster.equals("018") || RSVCluster.equals("019")) {


                                if ((spnData.equalsIgnoreCase("1") | spnData.equalsIgnoreCase("21")) & (rbData.equalsIgnoreCase("1"))) {
                                    if (daa <= 731) {
//                            if (moo <= 24) {
//                                        Stop RSV : 15-06-2023  ****************************Open again 22-10-23
//                                        Stop again : 30-11-2023  ****************************
//                                        secRSVStatus.setVisibility(View.VISIBLE);
//                                        lineRSVStatus.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                        }
//

                    }
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        return;
                    }
                });


            secExDate=(LinearLayout)findViewById(R.id.secExDate);
            VlblExDate=(TextView) findViewById(R.id.VlblExDate);
            dtpExDate=(EditText) findViewById(R.id.dtpExDate);


            btnVDate = (ImageButton) findViewById(R.id.btnVDate);
            btnVDate.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VariableID = "btnVDate";
                    showDialog(DATE_DIALOG);
                }
            });

            btnExDate = (ImageButton) findViewById(R.id.btnExDate);
            btnExDate.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VariableID = "btnExDate";
                    showDialog(DATE_DIALOG);
                }
            });

//            ---
                secRSVStatus = (LinearLayout) findViewById(R.id.secRSVStatus);
                lineRSVStatus = (View) findViewById(R.id.lineRSVStatus);
                VlblRSVStatus = (TextView) findViewById(R.id.VlblRSVStatus);
                rdogrpRSVStatus = (RadioGroup) findViewById(R.id.rdogrpRSVStatus);
                rdoRSVStatus1 = (RadioButton) findViewById(R.id.rdoRSVStatus1);
                rdoRSVStatus2 = (RadioButton) findViewById(R.id.rdoRSVStatus2);

//
            secSickStatus.setVisibility(View.GONE);
            secExDate.setVisibility(View.GONE);
            String AgeDay = String.valueOf(Global.DateDifferenceDays(dtpVDate.getText().toString(),txtDOB.getText().toString()));
            AgeD = AgeDay;
            if(Integer.valueOf(AgeD)<=28)
                rdoSickStatus2.setText("অসুস্থ");
            else
                rdoSickStatus2.setText("অসুস্থ (কাশি, শ্বাস কষ্ট, জ্বর )");

            DataSearch(ChildID, WeekNo);
            Button cmdSave = (Button) findViewById(R.id.cmdSave);
            cmdSave.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    DataSave();
                }});
        }
        catch(Exception  e)
        {
            Connection.MessageBox(FollowUpVisit.this, e.getMessage());
            return;
        }
    }

    private void DataSave()
    {
        try
        {
            String DV="";
            DV = Global.DateValidate(dtpVDate.getText().toString());
            if(DV.length()!=0 & secVDate.isShown())
            {
                Connection.MessageBox(FollowUpVisit.this, DV);
                dtpVDate.requestFocus();
                return;
            }
            else if(spnVstat.getSelectedItemPosition()==0  & secVstat.isShown())
            {
                Connection.MessageBox(FollowUpVisit.this, "পরিদর্শনের অবস্থা কি সিলেক্ট করুন।");
                spnVstat.requestFocus();
                return;
            }

            else if(!rdoSickStatus1.isChecked() & !rdoSickStatus2.isChecked() & !rdoSickStatus3.isChecked() & secSickStatus.isShown())
            {
                Connection.MessageBox(FollowUpVisit.this, "শারীরিক অবস্থা কি সিলেক্ট করুন।");
                rdoSickStatus1.requestFocus();
                return;
            }
//            27/07/2020
            String[] VS2 = spnVstat.getSelectedItem().toString().split("-");
            String spnData = VS2[0];
            if((spnData.equalsIgnoreCase("21") | spnData.equalsIgnoreCase("23"))  & secVstat.isShown() & !rdoSickStatus1.isChecked())
            {
                Connection.MessageBox(FollowUpVisit.this, "পরিদর্শনের অবস্থা- শিশু উপস্থিত(সুস্থ আছে)/অনুপস্থিত(সুস্থ আছে) কিন্তু শারীরিক অবস্থা- অসুস্থ সিলেক্ট করা হয়েছে");
                spnVstat.requestFocus();
                return;
            }
            else if((spnData.equalsIgnoreCase("22") | spnData.equalsIgnoreCase("24") | spnData.equalsIgnoreCase("25"))  & secVstat.isShown() & !rdoSickStatus2.isChecked())
            {
                Connection.MessageBox(FollowUpVisit.this, "পরিদর্শনের অবস্থা- শিশু উপস্থিত(অসুস্থ আছে)/অনুপস্থিত(অসুস্থ আছে)/চিকিৎসার জন্য অনুপস্থিত(অসুস্থ আছে) কিন্তু শারীরিক অবস্থা- সুস্থ সিলেক্ট করা হয়েছে");
                spnVstat.requestFocus();
                return;
            }
//
            DV = Global.DateValidate(dtpExDate.getText().toString());
            if(DV.length()!=0 & secExDate.isShown())
            {
                Connection.MessageBox(FollowUpVisit.this, DV);
                dtpExDate.requestFocus();
                return;
            }
            else if(!rdoRSVStatus1.isChecked() & !rdoRSVStatus2.isChecked() &  secRSVStatus.isShown())
            {
                Connection.MessageBox(FollowUpVisit.this, "আর এস ভি সিলেক্ট করুন।");
                secRSVStatus.requestFocus();
                return;
            }

            if(Global.DateDifferenceDays(dtpVDate.getText().toString(), txtDOB.getText().toString())<0)
            {
                Connection.MessageBox(FollowUpVisit.this, "ভিজিটের তারিখ অবশ্যই জন্ম তারিখের সমান অথবা বড় হতে হবে।");
                dtpVDate.requestFocus();
                return;
            }

            if(Global.DateDifferenceDays(dtpVDate.getText().toString(), Global.DateConvertDMY(g.getWeekStartDate()))<0)
            {
                Connection.MessageBox(FollowUpVisit.this, WeekNo + " সপ্তাহের ভিজিট অবশ্যই " + Global.DateConvertDMY(g.getWeekStartDate()) + " এবং " + Global.DateConvertDMY(g.getWeekEndDate()) +" এর মধ্যে হতে হবে।");
                        dtpVDate.requestFocus();
                return;
            }
            if(Global.DateDifferenceDays(Global.DateConvertDMY(g.getWeekEndDate()),dtpVDate.getText().toString())<0)
            {
                Connection.MessageBox(FollowUpVisit.this, WeekNo + " সপ্তাহের ভিজিট অবশ্যই " + Global.DateConvertDMY(g.getWeekStartDate()) + " এবং " + Global.DateConvertDMY(g.getWeekEndDate()) +" এর মধ্যে হতে হবে।");
                dtpVDate.requestFocus();
                return;
            }

            String SQL = "";
            String[] VS = spnVstat.getSelectedItem().toString().split("-");
            String VisitStatus = VS[0];

            if(rdoSickStatus3.isChecked() & !VisitStatus.equals("2"))
            {
                Connection.MessageBox(FollowUpVisit.this, "শিশু অনুপস্থিত থাকলেই শুধুমাএ অসুস্থ্যতার অবস্থা জানি না হতে পারে.");
                return;
            }
            else if(!rdoSickStatus2.isChecked() & VisitStatus.equals("3"))
            {
                Connection.MessageBox(FollowUpVisit.this, "শিশু চিকিৎসার জন্য অনুপস্থিত থাকলে অসুস্থ্যতার অবস্থা - অসুস্থ (কাশি, শ্বাস কষ্ট, জ্বর ) হতে হবে ।");
                return;
            }


            if(!C.Existence("Select ChildId from " + TableName + "  Where ChildId='"+ ChildID +"' and Week='"+ WeekNo +"'"))
            {
                SQL = "Insert into " + TableName + "(ChildId,CID,PID,Week,UserId,EnDt,Upload,Lat,Lon)Values('"+ ChildID +"','','','"+ WeekNo +"','"+ g.getUserId() +"','"+ Global.DateTimeNowYMDHMS() + "','2','"+ Double.toString(currentLatitude) +"','"+ Double.toString(currentLongitude) +"')";
                C.Save(SQL);
            }

            SQL = "Update " + TableName + " Set Upload='2',";
            SQL += "CID = '" + CID +"',";
            SQL += "PID = '" + PID + "',";
            SQL += "Week = '" + WeekNo +"',";
            SQL+="VDate = '"+ Global.DateConvertYMD(dtpVDate.getText().toString()) +"',";
            SQL+="Vstat = '"+ VS[0] +"',";
            //SQL+="Vstat = '"+ (spnVstat.getSelectedItemPosition() == 0 ? "" : Global.Left(spnVstat.getSelectedItem().toString(),1)) +"',";

            if(rdoSickStatus1.isChecked())
                SQL+="SickStatus = '1',";
            else if(rdoSickStatus2.isChecked())
                SQL+="SickStatus = '2',";
            else if(rdoSickStatus3.isChecked())
                SQL+="SickStatus = '3',";

            //RadioButton rbSickStatus = (RadioButton)findViewById(rdogrpSickStatus.getCheckedRadioButtonId());
            //SQL+="SickStatus = '" + (rbSickStatus == null ? "":(Global.Left(rbSickStatus.getText().toString(),1))) +"',";
            SQL+="ExDate = '"+ Global.DateConvertYMD(dtpExDate.getText().toString()) +"'";

            if(rdoRSVStatus1.isChecked())
                SQL+=",RSVStatus = '1'";
            else if(rdoRSVStatus2.isChecked())
                SQL+=",RSVStatus = '2'";

            SQL+="  Where ChildId='"+ ChildID +"' and Week='"+ WeekNo +"'";
            C.Save(SQL);

            String EX = VS[0].toString(); // Global.Left(spnVstat.getSelectedItem().toString(),1);
            if(EX.equals("4")|EX.equals("5")|EX.equals("6")|EX.equals("7")|EX.equals("26")|EX.equals("27"))
            {
                C.Save("Update Child set upload='2',ExType='"+ EX +"',ExDate='"+ Global.DateConvertYMD(dtpExDate.getText().toString()) +"' Where ChildId='"+ ChildID +"'");
            }
            //continue
            else if(EX.equals("0"))
            {
                C.Save("Update Child set upload='2',ExType='',ExDate='' Where ChildId='"+ ChildID +"'");
            }
            else
            {
                C.Save("Update Child set upload='2',ExType='',ExDate='' Where ChildId='"+ ChildID +"'");
            }

//            24/08/2020
            String CDOB;
            CDOB=C.ReturnSingleValue("Select BDate  from Child WHERE   ChildID = '"+ ChildID +"'");

            int da = Global.DateDifferenceDays(dtpVDate.getText().toString(),Global.DateConvertDMY(CDOB));
            int mo = (int)(da/30.44);

//            Stop RSV : 15-06-2023  ****************************
//            Stop again : 30-11-2023  ****************************
//            if(rdoRSVStatus1.isChecked() &  secRSVStatus.isShown()) {
//
////                String RSVCluster = C.ReturnSingleValue("Select Cluster from Cluster");
////                if (RSVCluster.equals("001") || RSVCluster.equals("002") || RSVCluster.equals("003") || RSVCluster.equals("004") || RSVCluster.equals("005") || RSVCluster.equals("006") || RSVCluster.equals("007") || RSVCluster.equals("008") || RSVCluster.equals("009") || RSVCluster.equals("011") || RSVCluster.equals("012") || RSVCluster.equals("013") || RSVCluster.equals("014") || RSVCluster.equals("015") || (RSVCluster.equals("016") & VILLAGE.substring(0,3).equals("307"))  || RSVCluster.equals("017") || RSVCluster.equals("018") || RSVCluster.equals("019")) {
//
//                    if (da <= 731) {
////                if (mo <= 24) {
//
//                        Bundle IDbundle = new Bundle();
//                        Intent f1;
//                        IDbundle.putString("childid", ChildID);
//                        IDbundle.putString("pid", PID);
////                    IDbundle.putString("pid", CID);
//                        IDbundle.putString("weekno", WeekNo);
//                        IDbundle.putString("fm", FatherMother.getText().toString());
//                        IDbundle.putString("aged", AgeD);
//                        IDbundle.putString("agem", AgeM);
//                        IDbundle.putString("agedm", AgeDM);
//                        IDbundle.putString("bdate", DOB);
//                        IDbundle.putString("name", lblCName.getText().toString());
//                        IDbundle.putString("visittype", VisitType);
//                        IDbundle.putString("visitno", "0");
//                        IDbundle.putString("visitdate", dtpVDate.getText().toString());
//                        IDbundle.putString("temp", "");
//                        IDbundle.putString("Cough", "");
//                        IDbundle.putString("CoughDt", "");
//                        IDbundle.putString("DBrea", "");
//                        IDbundle.putString("DBreaDt", "");
//                        IDbundle.putString("source", "");
//
//                        f1 = new Intent(getApplicationContext(), RSV.class);
//                        f1.putExtras(IDbundle);
//                        startActivityForResult(f1, 1);
//                    }
//                }
            //******************RSV

//            }
//


            Connection.MessageBox(FollowUpVisit.this, "Saved Successfully");

            finish();

            if(EX.equals("0")|EX.equals("1")|EX.equals("2")|EX.equals("3")|EX.equals("21")|EX.equals("22")|EX.equals("23")|EX.equals("24")|EX.equals("25")) {
                String AgeDay = String.valueOf(Global.DateDifferenceDays(dtpVDate.getText().toString(),txtDOB.getText().toString()));
                AgeD = AgeDay;
                double m = Integer.valueOf(AgeDay)/30.44;
                double d = Integer.valueOf(AgeDay)%30.44;

                //Call Assessment based on age of child
                if (rdoSickStatus2.isChecked() & Integer.valueOf(AgeD) <= 28) {
                    AgeDM = String.valueOf(AgeD)+"  দিন";

                    Bundle IDbundle = new Bundle();
                    IDbundle.putString("childid", ChildID);
                    IDbundle.putString("pid", PID);
                    IDbundle.putString("cid", CID);
                    IDbundle.putString("weekno", WeekNo);
                    IDbundle.putString("fm", FM);
                    IDbundle.putString("aged", AgeD);
                    IDbundle.putString("agem", AgeM);
                    IDbundle.putString("agedm", AgeDM);
                    IDbundle.putString("bdate", DOB);
                    IDbundle.putString("name", lblCName.getText().toString());
                    IDbundle.putString("visittype", VisitType);
                    IDbundle.putString("visitno", "0");
                    IDbundle.putString("visitdate", dtpVDate.getText().toString());
                    if(EX.equals("2")|EX.equals("3"))
                        IDbundle.putString("childpresent", "n");
                    else
                        IDbundle.putString("childpresent", "y");

                    IDbundle.putString("visitstatus", EX);
                    IDbundle.putString("child_outside_area", "n");
                    IDbundle.putString("village", VILLAGE);
                    IDbundle.putString("contactno", CONTACT_NO);

                    Intent f1 = new Intent(getApplicationContext(), AssNewBorn.class);
                    f1.putExtras(IDbundle);
                    startActivity(f1);
                } else if (rdoSickStatus2.isChecked() & Integer.valueOf(AgeD) > 28) {
                    AgeDM = String.valueOf((int)m)+" মাস "+ String.valueOf((int)d)+"  দিন";

                    Bundle IDbundle = new Bundle();
                    IDbundle.putString("childid", ChildID);
                    IDbundle.putString("pid", PID);
                    IDbundle.putString("cid", CID);
                    IDbundle.putString("weekno", WeekNo);
                    IDbundle.putString("fm", FM);
                    IDbundle.putString("aged", AgeD);
                    IDbundle.putString("agem", AgeM);
                    IDbundle.putString("agedm", AgeDM);
                    IDbundle.putString("bdate", DOB);
                    IDbundle.putString("name", lblCName.getText().toString());
                    IDbundle.putString("visittype", VisitType);
                    IDbundle.putString("visitno", "0");
                    IDbundle.putString("visitdate", dtpVDate.getText().toString());
                    if(EX.equals("2")|EX.equals("3"))
                        IDbundle.putString("childpresent", "n");
                    else
                        IDbundle.putString("childpresent", "y");

                    IDbundle.putString("visitstatus", EX);
                    IDbundle.putString("child_outside_area", "n");
                    IDbundle.putString("village", VILLAGE);
                    IDbundle.putString("contactno", CONTACT_NO);

                    Intent f1 = new Intent(getApplicationContext(), AssPneu.class);
                    f1.putExtras(IDbundle);
                    startActivity(f1);
                }
            }

        }
        catch(Exception  e)
        {
            Connection.MessageBox(FollowUpVisit.this, e.getMessage());
            return;
        }
    }
    private void DataSearch(String ChildId, String Week)
    {
        try
        {

            RadioButton rb;
            Cursor cur = C.ReadData("Select CID as cid,PID as pid,Week as week,VDate as vdate,VStat as vstat,ifnull(SickStatus,'') as sickstatus,ifnull(ExDate,'') as exdate,ifnull(RSVStatus,'') as rsvstatus " +
                    "from "+ TableName +"  Where ChildId='"+ ChildId +"' and Week='"+ Week +"' group by childid,week order by min(vstat)");
            cur.moveToFirst();
            while(!cur.isAfterLast())
            {
                //txtChildId.setText(cur.getString(cur.getColumnIndex("ChildId")));
                //txtCID.setText(cur.getString(cur.getColumnIndex("cid")));
                //txtPID.setText(cur.getString(cur.getColumnIndex("pid")));

                txtWeek.setText(cur.getString(cur.getColumnIndex("week")));
                dtpVDate.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("vdate"))));
                spnVstat.setSelection(Global.SpinnerItemPosition(spnVstat, cur.getString(cur.getColumnIndex("vstat")).length() ,cur.getString(cur.getColumnIndex("vstat"))));

                if(cur.getString(cur.getColumnIndex("sickstatus")).equals("1"))
                    rdoSickStatus1.setChecked(true);
                else if(cur.getString(cur.getColumnIndex("sickstatus")).equals("2"))
                    rdoSickStatus2.setChecked(true);
                else if(cur.getString(cur.getColumnIndex("sickstatus")).equals("3"))
                    rdoSickStatus3.setChecked(true);

                dtpExDate.setText(Global.DateConvertDMY(cur.getString(cur.getColumnIndex("exdate"))));
                if(cur.getString(cur.getColumnIndex("rsvstatus")).equals("1"))
                    rdoRSVStatus1.setChecked(true);
                else if(cur.getString(cur.getColumnIndex("rsvstatus")).equals("2"))
                    rdoRSVStatus2.setChecked(true);

                String AgeDay = String.valueOf(Global.DateDifferenceDays(dtpVDate.getText().toString(),txtDOB.getText().toString()));
                AgeD = AgeDay;
                double m = Integer.valueOf(AgeDay)/30.44;
                double d = Integer.valueOf(AgeDay)%30.44;
                String AgeDayMonth = String.valueOf((int)m)+" মাস "+ String.valueOf((int)d)+"  দিন";
                Age.setText(": "+ AgeDayMonth);

                cur.moveToNext();
            }
            cur.close();
        }
        catch(Exception  e)
        {
            Connection.MessageBox(FollowUpVisit.this, e.getMessage());
            return;
        }
    }




    protected Dialog onCreateDialog(int id) {
        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        String DT = dtpVDate.getText().toString();
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



            if (VariableID.equals("btnVDate"))
            {
                dtpDate = (EditText)findViewById(R.id.dtpVDate);
                dtpDate = (EditText)findViewById(R.id.dtpVDate);
                dtpDate.setText(new StringBuilder()
                        .append(Global.Right("00"+mDay,2)).append("/")
                        .append(Global.Right("00"+mMonth,2)).append("/")
                        .append(mYear));

                String AgeDay = String.valueOf(Global.DateDifferenceDays(dtpVDate.getText().toString(),txtDOB.getText().toString()));
                AgeD = AgeDay;
                double m = Integer.valueOf(AgeDay)/30.44;
                double d = Integer.valueOf(AgeDay)%30.44;

                String AgeDayMonth = String.valueOf((int)m)+" মাস "+ String.valueOf((int)d)+"  দিন";
                Age.setText(": "+ AgeDayMonth);

            }
            else if (VariableID.equals("btnExDate"))
            {
                dtpDate = (EditText)findViewById(R.id.dtpExDate);
                dtpDate.setText(new StringBuilder()
                        .append(Global.Right("00"+mDay,2)).append("/")
                        .append(Global.Right("00"+mMonth,2)).append("/")
                        .append(mYear));
            }
        }
    };
    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
            hour = selectedHour; minute = selectedMinute;
            EditText tpTime;


            //tpTime.setText(new StringBuilder().append(Global.Right("00"+hour,2)).append(":").append(Global.Right("00"+minute,2)));

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
}
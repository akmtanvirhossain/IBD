package org.icddrb.ibd;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Common.Connection;
import Common.Global;

public class DSSMember extends Activity {
    boolean netwoekAvailable = false;
    //DSS
    ArrayList<HashMap<String, String>> mylistDSS;
    SimpleAdapter mScheduleDSS;


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

    //Top menu
    //--------------------------------------------------------------------------------------------------
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.nextprev, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog.Builder adb = new AlertDialog.Builder(DSSMember.this);
        switch (item.getItemId()) {
            case R.id.homemenu:
                adb.setTitle("Close");
                adb.setMessage("আপনি কি এই ফর্ম থেকে বের হতে চান[Yes/No]?");
                adb.setNegativeButton("No", null);
                adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                adb.show();
                return true;
            /*case R.id.prev:
                return true;
            case R.id.next:
                return true;*/
        }
        return false;
    }

    Connection C;
    Global g;
    SimpleAdapter dataAdapter;
    ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();

    Spinner VillageList;
    Spinner BariList;

    String TableName;
    String VariableList;
    String UniqueIDField;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.dss_member);
            C = new Connection(this);
            g = Global.getInstance();

            Bundle B = new Bundle();
            B = getIntent().getExtras();

            VillageList = (Spinner)findViewById(R.id.VillageList);
            BariList    = (Spinner)findViewById(R.id.BariList);

            VillageList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {

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


                    /*if (BariList.getSelectedItem().toString().trim().equalsIgnoreCase("all bari")) {
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
                    }*/
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {

                }

            });


            DSSMemberList("101","0001","316");

        } catch (Exception e) {
            Connection.MessageBox(DSSMember.this, e.getMessage());
            return;
        }
    }




    //DSS Member List
    //**********************************************************************************************
    ListView listDSSMember;
    public void DSSMemberList(String Village, String BariCode, String WeekNo) {
        mScheduleDSS= null;

        listDSSMember = (ListView) findViewById(R.id.listDSSMember);
        listDSSMember.setAdapter(null);
        mylistDSS = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map;

        try {
            String SQL = "";
            String Cluster = "059";
            String Block   = "5";

            if (BariCode.length() > 1) {
                SQL  = " select c.referral as referral, b.Cluster cluster,b.Block block,c.ChildId childid,c.Vill vill,c.bari bari,c.HH hh,c.SNo sno,";
                SQL += " c.PID pid,c.CID cid,Name name,Sex sex,Cast(((julianday(date('now'))-julianday(c.BDate))) as int)aged, Cast(((julianday(date('now'))-julianday(c.BDate))/30.4) as int) agem,ifnull(FaName,'') father,ifnull(MoName,'') mother,(case when v.ChildId is null then '2' else '1' end)visit,ifnull(c.extype,'') extype";
                SQL += " from Child c inner join Bari b on c.Vill=b.Vill and c.bari=b.Bari";
                SQL += " left outer join Visits v on c.childid=v.childid and v.week='"+ WeekNo +"'";
                SQL += " where b.Cluster='"+ Cluster +"' and b.Block='"+ Block +"' order by c.cid asc";
            } else {
                SQL  = " select c.referral as referral, b.Cluster cluster,b.Block block,c.ChildId childid,c.Vill vill,c.bari bari,c.HH hh,c.SNo sno,";
                SQL += " c.PID pid,c.CID cid,Name name,Sex sex,Cast(((julianday(date('now'))-julianday(c.BDate))) as int)aged, Cast(((julianday(date('now'))-julianday(c.BDate))/30.4) as int) agem,ifnull(FaName,'') father,ifnull(MoName,'') mother,(case when v.ChildId is null then '2' else '1' end)visit,ifnull(c.extype,'') extype";
                SQL += " from Child c inner join Bari b on c.Vill=b.Vill and c.bari=b.Bari";
                SQL += " left outer join Visits v on c.childid=v.childid and v.week='316'";
                SQL += " where b.Cluster='"+ Cluster +"' and b.Block='"+ Block +"' order by c.cid asc";
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
                /*map.put("sex", cur.getString(cur.getColumnIndex("sex")));
                map.put("aged", cur.getString(cur.getColumnIndex("aged")));
                map.put("agem", cur.getString(cur.getColumnIndex("agem")));
                map.put("father", cur.getString(cur.getColumnIndex("father")).trim());
                map.put("mother", cur.getString(cur.getColumnIndex("mother")).trim());
                map.put("visit", cur.getString(cur.getColumnIndex("visit")).trim());
                map.put("extype", cur.getString(cur.getColumnIndex("extype")).trim());
                map.put("referral", cur.getString(cur.getColumnIndex("referral")).trim());
                */

                i+=1;
                mylistDSS.add(map);

                cur.moveToNext();
            }
            cur.close();
            mScheduleDSS = new SimpleAdapter(this, mylistDSS, R.layout.member_list_row,
                    new String[]{"bari"},
                    new int[]{R.id.Bari});

            //lblPageHeading.setText("  শিশুর তালিকা ( "+String.valueOf(i)+" )");
            listDSSMember.setAdapter(new DSSListAdapter(this));

        } catch (Exception e) {
            AlertDialog.Builder adb = new AlertDialog.Builder(DSSMember.this);
            adb.setTitle("Message");
            adb.setMessage(e.getMessage());
            adb.setPositiveButton("Ok", null);
            adb.show();
        }

    }


    String FM = "";
    public class DSSListAdapter extends BaseAdapter {
        private Context context;

        public DSSListAdapter(Context c) {
            context = c;
        }

        public int getCount() {
            return mScheduleDSS.getCount();
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
                convertView = inflater.inflate(R.layout.member_list_row, null);
            }


            TextView ChildName = (TextView) convertView.findViewById(R.id.ChildName);
            TextView CID = (TextView) convertView.findViewById(R.id.CID);
            TextView PID = (TextView) convertView.findViewById(R.id.PID);
            TextView FatherMother = (TextView) convertView.findViewById(R.id.FatherMother);
            TextView Age = (TextView) convertView.findViewById(R.id.Age);
            TextView Sex = (TextView) convertView.findViewById(R.id.Sex);
            TextView BariLocation = (TextView) convertView.findViewById(R.id.BariLocation);

            final HashMap<String, String> o = (HashMap<String, String>) mScheduleDSS.getItem(position);

            ChildName.setText(": "+ o.get("name"));



            LinearLayout bariListRow = (LinearLayout)convertView.findViewById(R.id.bariListRow);
            if( Integer.valueOf(o.get("sl"))%2==0)
                bariListRow.setBackgroundColor(Color.WHITE);
            else
                bariListRow.setBackgroundColor(Color.parseColor("#F3F3F3"));

            return convertView;
        }
    }

}
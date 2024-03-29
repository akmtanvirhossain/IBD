package org.icddrb.ibd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import Common.Connection;
import Common.Global;

public class RSV_DataModel_20Apr2020 {

       private String _ChildID = "";
       public String getChildID(){
             return _ChildID;
        }
       public void setChildID(String newValue){
             _ChildID = newValue;
        }
       private String _CID = "";
       public String getCID(){
             return _CID;
        }
       public void setCID(String newValue){
             _CID = newValue;
        }
       private String _PID = "";
       public String getPID(){
             return _PID;
        }
       public void setPID(String newValue){
             _PID = newValue;
        }
       private String _Week = "";
       public String getWeek(){
             return _Week;
        }
       public void setWeek(String newValue){
             _Week = newValue;
        }
       private String _VDate = "";
       public String getVDate(){
             return _VDate;
        }
       public void setVDate(String newValue){
             _VDate = newValue;
        }
       private String _VType = "";
       public String getVType(){
             return _VType;
        }
       public void setVType(String newValue){
             _VType = newValue;
        }
       private String _Visit = "";
       public String getVisit(){
             return _Visit;
        }
       public void setVisit(String newValue){
             _Visit = newValue;
        }
       private String _SlNo = "";
       public String getSlNo(){
             return _SlNo;
        }
       public void setSlNo(String newValue){
             _SlNo = newValue;
        }
       private String _Temp = "";
       public String getTemp(){
             return _Temp;
        }
       public void setTemp(String newValue){
             _Temp = newValue;
        }

       private String _Cough = "";
       public String getCough(){ return _Cough; }
       public void setCough(String newValue){ _Cough = newValue; }

       private String _dtpCoughDt = "";
       public String getdtpCoughDt(){
             return _dtpCoughDt;
        }
       public void setdtpCoughDt(String newValue){
             _dtpCoughDt = newValue;
        }

       private String _DBrea = "";
       public String getDBrea(){ return _DBrea; }
       public void setDBrea(String newValue){ _DBrea = newValue; }

       private String _dtpDBreaDt = "";
       public String getdtpDBreaDt(){
             return _dtpDBreaDt;
        }
       public void setdtpDBreaDt(String newValue){
             _dtpDBreaDt = newValue;
        }

       private String _DeepCold = "";
       public String getDeepCold(){ return _DeepCold; }
       public void setDeepCold(String newValue){ _DeepCold = newValue; }

       private String _DeepColdDt = "";
       public String getDeepColdDt(){
             return _DeepColdDt;
        }
       public void setDeepColdDt(String newValue){
             _DeepColdDt = newValue;
        }

       private String _SoreThroat = "";
       public String getSoreThroat(){ return _SoreThroat; }
       public void setSoreThroat(String newValue){ _SoreThroat = newValue; }

       private String _SoreThroatDt = "";
       public String getSoreThroatDt(){
             return _SoreThroatDt;
        }
       public void setSoreThroatDt(String newValue){
             _SoreThroatDt = newValue;
        }

    private String _Fever = "";
    public String getFever(){
        return _Fever;
    }
    public void setFever(String newValue){
        _Fever = newValue;
    }

    private String _FeverDt = "";
    public String getFeverDt(){
        return _FeverDt;
    }
    public void setFeverDt(String newValue){
        _FeverDt = newValue;
    }


    private String _RSVsuitable = "";
       public String getRSVsuitable(){
             return _RSVsuitable;
        }
       public void setRSVsuitable(String newValue){
             _RSVsuitable = newValue;
        }
       private String _RSVlisted = "";
       public String getRSVlisted(){
             return _RSVlisted;
        }
       public void setRSVlisted(String newValue){
             _RSVlisted = newValue;
        }
       private String _RSVlistedDt = "";
       public String getRSVlistedDt(){
             return _RSVlistedDt;
        }
       public void setRSVlistedDt(String newValue){
             _RSVlistedDt = newValue;
        }
       private String _Reason = "";
       public String getReason(){
             return _Reason;
        }
       public void setReason(String newValue){
             _Reason = newValue;
        }
       private String _SampleAgree = "";
       public String getSampleAgree(){ return _SampleAgree; }
       public void setSampleAgree(String newValue){ _SampleAgree = newValue; }
       private String _NotAgree = "";
       public String getNotAgree(){ return _NotAgree; }
       public void setNotAgree(String newValue){ _NotAgree = newValue; }
       private String _OthersR = "";
       public String getOthersR(){ return _OthersR; }
       public void setOthersR(String newValue){ _OthersR = newValue; }
       private String _StartTime = "";
       public void setStartTime(String newValue){
             _StartTime = newValue;
        }
       private String _EndTime = "";
       public void setEndTime(String newValue){
             _EndTime = newValue;
        }
       private String _DeviceID = "";
       public void setDeviceID(String newValue){
             _DeviceID = newValue;
        }
       private String _EntryUser = "";
       public void setEntryUser(String newValue){
             _EntryUser = newValue;
        }
       private String _Lat = "";
       public void setLat(String newValue){
             _Lat = newValue;
        }
       private String _Lon = "";
       public void setLon(String newValue){
             _Lon = newValue;
        }
       private String _EnDt = Global.DateTimeNowYMDHMS();
       private int _Upload = 2;
       private String _modifyDate = Global.DateTimeNowYMDHMS();

       String TableName = "RSV";

       public String SaveUpdateData(Context context)
       {
           String response = "";
           C = new Connection(context);
           String SQL = "";
           try
           {
                if(C.Existence("Select * from "+ TableName +"  Where ChildID='"+ _ChildID +"' and Week='"+ _Week +"' and VType='"+ _VType +"' and Visit='"+ _Visit +"' "))
                   response = UpdateData(context);
                else
                   response = SaveData(context);
           }
           catch(Exception  e)
           {
                response = e.getMessage();
           }
          return response;
       }
       Connection C;

       private String SaveData(Context context)
       {
           String response = "";
           C = new Connection(context);
           try
             {
                ContentValues contentValues = new ContentValues();
                contentValues.put("ChildID", _ChildID);
                contentValues.put("CID", _CID);
                contentValues.put("PID", _PID);
                contentValues.put("Week", _Week);
                contentValues.put("VDate", _VDate);
                contentValues.put("VType", _VType);
                contentValues.put("Visit", _Visit);
                contentValues.put("SlNo", _SlNo);
                contentValues.put("Temp", _Temp);
                contentValues.put("Cough", _Cough);
                contentValues.put("dtpCoughDt", _dtpCoughDt);
                contentValues.put("DBrea", _DBrea);
                contentValues.put("dtpDBreaDt", _dtpDBreaDt);
                contentValues.put("DeepCold", _DeepCold);
                contentValues.put("DeepColdDt", _DeepColdDt);
                contentValues.put("SoreThroat", _SoreThroat);
                contentValues.put("SoreThroatDt", _SoreThroatDt);

                contentValues.put("Fever", _Fever);
                contentValues.put("FeverDt", _FeverDt);

                contentValues.put("RSVsuitable", _RSVsuitable);
                contentValues.put("RSVlisted", _RSVlisted);
                contentValues.put("RSVlistedDt", _RSVlistedDt);
                contentValues.put("Reason", _Reason);
                contentValues.put("SampleAgree", _SampleAgree);
                contentValues.put("NotAgree", _NotAgree);
                contentValues.put("OthersR", _OthersR);
                contentValues.put("StartTime", _StartTime);
                contentValues.put("EndTime", _EndTime);
                contentValues.put("DeviceID", _DeviceID);
                contentValues.put("EntryUser", _EntryUser);
                contentValues.put("Lat", _Lat);
                contentValues.put("Lon", _Lon);
                contentValues.put("EnDt", _EnDt);
                contentValues.put("Upload", _Upload);
                contentValues.put("modifyDate", _modifyDate);
                C.InsertData(TableName, contentValues);
             }
             catch(Exception  e)
             {
                response = e.getMessage();
             }
          return response;
       }

       private String UpdateData(Context context)
       {
           String response = "";
           C = new Connection(context);
           try
             {
                ContentValues contentValues = new ContentValues();
                contentValues.put("ChildID", _ChildID);
                contentValues.put("CID", _CID);
                contentValues.put("PID", _PID);
                contentValues.put("Week", _Week);
                contentValues.put("VDate", _VDate);
                contentValues.put("VType", _VType);
                contentValues.put("Visit", _Visit);
                contentValues.put("SlNo", _SlNo);
                contentValues.put("Temp", _Temp);
                contentValues.put("Cough", _Cough);
                contentValues.put("dtpCoughDt", _dtpCoughDt);
                contentValues.put("DBrea", _DBrea);
                contentValues.put("dtpDBreaDt", _dtpDBreaDt);
                contentValues.put("DeepCold", _DeepCold);
                contentValues.put("DeepColdDt", _DeepColdDt);
                contentValues.put("SoreThroat", _SoreThroat);
                contentValues.put("SoreThroatDt", _SoreThroatDt);

                contentValues.put("Fever", _Fever);
                contentValues.put("FeverDt", _FeverDt);

                contentValues.put("RSVsuitable", _RSVsuitable);
                contentValues.put("RSVlisted", _RSVlisted);
                contentValues.put("RSVlistedDt", _RSVlistedDt);
                contentValues.put("Reason", _Reason);
                contentValues.put("SampleAgree", _SampleAgree);
                contentValues.put("NotAgree", _NotAgree);
                contentValues.put("OthersR", _OthersR);
                contentValues.put("Upload", _Upload);
                contentValues.put("modifyDate", _modifyDate);
                C.UpdateData(TableName, "ChildID,Week,VType,Visit", (""+ _ChildID +", "+ _Week +", "+ _VType +", "+ _Visit +""), contentValues);
             }
             catch(Exception  e)
             {
                response = e.getMessage();
             }
          return response;
       }

         int Count = 0;          private int _Count = 0;          public int getCount(){ return _Count; }
       public List<RSV_DataModel_20Apr2020> SelectAll(Context context, String SQL)
       {
           Connection C = new Connection(context);
           List<RSV_DataModel_20Apr2020> data = new ArrayList<RSV_DataModel_20Apr2020>();
           RSV_DataModel_20Apr2020 d = new RSV_DataModel_20Apr2020();
           Cursor cur = C.ReadData(SQL);

           cur.moveToFirst();
           while(!cur.isAfterLast())
           {
               Count += 1;
               d = new RSV_DataModel_20Apr2020();
               d._Count = Count;
               d._ChildID = cur.getString(cur.getColumnIndex("ChildID"));
               d._CID = cur.getString(cur.getColumnIndex("CID"));
               d._PID = cur.getString(cur.getColumnIndex("PID"));
               d._Week = cur.getString(cur.getColumnIndex("Week"));
               d._VDate = cur.getString(cur.getColumnIndex("VDate"));
               d._VType = cur.getString(cur.getColumnIndex("VType"));
               d._Visit = cur.getString(cur.getColumnIndex("Visit"));
               d._SlNo = cur.getString(cur.getColumnIndex("SlNo"));
               d._Temp = cur.getString(cur.getColumnIndex("Temp"));
               d._Cough = cur.getString(cur.getColumnIndex("Cough"));
               d._dtpCoughDt = cur.getString(cur.getColumnIndex("dtpCoughDt"));
               d._DBrea = cur.getString(cur.getColumnIndex("DBrea"));
               d._dtpDBreaDt = cur.getString(cur.getColumnIndex("dtpDBreaDt"));
               d._DeepCold = cur.getString(cur.getColumnIndex("DeepCold"));
               d._DeepColdDt = cur.getString(cur.getColumnIndex("DeepColdDt"));
               d._SoreThroat = cur.getString(cur.getColumnIndex("SoreThroat"));
               d._SoreThroatDt = cur.getString(cur.getColumnIndex("SoreThroatDt"));

               d._Fever = cur.getString(cur.getColumnIndex("Fever"));
               d._FeverDt = cur.getString(cur.getColumnIndex("FeverDt"));

               d._RSVsuitable = cur.getString(cur.getColumnIndex("RSVsuitable"));
               d._RSVlisted = cur.getString(cur.getColumnIndex("RSVlisted"));
               d._RSVlistedDt = cur.getString(cur.getColumnIndex("RSVlistedDt"));
               d._Reason = cur.getString(cur.getColumnIndex("Reason"));
               d._SampleAgree = cur.getString(cur.getColumnIndex("SampleAgree"));
               d._NotAgree = cur.getString(cur.getColumnIndex("NotAgree"));
               d._OthersR = cur.getString(cur.getColumnIndex("OthersR"));
               data.add(d);

               cur.moveToNext();
           }
           cur.close();
         return data;
       }
}
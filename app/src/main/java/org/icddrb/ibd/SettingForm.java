package org.icddrb.ibd;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import Common.Connection;
import Common.Global;

public class SettingForm extends Activity {
    Connection C;
    //private ProgressDialog dialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.devicesetting);
            C = new Connection(this);

            final Spinner spnCluster = (Spinner) findViewById(R.id.spnCluster);
            final Spinner spnVHW = (Spinner) findViewById(R.id.spnVHW);
            SpinnerItem(spnCluster, "select Cluster from Cluster where DeviceSetting='2'");

            spnCluster.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    SpinnerItem(spnVHW, "select VHW+'-'+VHWNAME from VHWS where Active='1' and Cluster='" + spnCluster.getSelectedItem().toString() + "'");
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }
            });


            Button cmdSave = (Button) findViewById(R.id.cmdSave);
            cmdSave.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    try {

                        String Setting = C.ReturnResult("Existence", "Select Cluster from CLuster where Cluster='" + spnCluster.getSelectedItem().toString() + "' and DeviceSetting='1'");
                        if (Setting.equals("2")) {
                            Connection.MessageBox(SettingForm.this, "This is not a valid information for device setting or information not available for this VHW.");
                            return;
                        }


                        String ResponseString = "Status:";

                        final ProgressDialog progDailog = ProgressDialog.show(
                                SettingForm.this, "", "অপেক্ষা করুন. . .", true);

                        new Thread() {
                            public void run() {
                                try {
                                    String[] V = spnVHW.getSelectedItem().toString().split("-");
                                    C.RebuildDatabase(spnCluster.getSelectedItem().toString(),V[0],V[1]);

                                } catch (Exception e) {

                                }
                                progDailog.dismiss();

                                //Call Login Form
                                finish();
                                Intent f1 = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(f1);


                            }
                        }.start();

                    } catch (Exception ex) {
                        Connection.MessageBox(SettingForm.this, ex.getMessage());
                        return;
                    }
                }
            });
        } catch (Exception ex) {
            Connection.MessageBox(SettingForm.this, ex.getMessage());
            return;
        }
    }

    private void SpinnerItem(Spinner SpinnerName, String SQL) {
        List<String> listItem = new ArrayList<String>();
        listItem = C.DownloadJSONList(SQL);
        ArrayAdapter<String> adptrList = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listItem);
        SpinnerName.setAdapter(adptrList);
    }

}
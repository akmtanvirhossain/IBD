package org.icddrb.ibd;

        import android.app.Activity;
        import android.app.AlertDialog;
        import android.app.DatePickerDialog;
        import android.app.Dialog;
        import android.app.ProgressDialog;
        import android.app.TimePickerDialog;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.database.Cursor;
        import android.location.Location;
        import android.os.Bundle;
        import android.view.KeyEvent;
        import android.view.LayoutInflater;
        import android.view.Menu;
        import android.view.MenuInflater;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.ViewGroup;
        import android.view.WindowManager;
        import android.webkit.WebSettings;
        import android.webkit.WebView;
        import android.webkit.WebViewClient;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.BaseAdapter;
        import android.widget.Button;
        import android.widget.CheckBox;
        import android.widget.DatePicker;
        import android.widget.EditText;
        import android.widget.GridView;
        import android.widget.ImageButton;
        import android.widget.LinearLayout;
        import android.widget.RadioButton;
        import android.widget.RadioGroup;
        import android.widget.SimpleAdapter;
        import android.widget.Spinner;
        import android.widget.TextView;
        import android.widget.TimePicker;

        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.HashMap;
        import java.util.List;

        import Common.Connection;
        import Common.Global;

/**
 * Created by user on 18/03/2015.
 */

public class WebReports extends Activity {
    boolean netwoekAvailable = false;
    Location currentLocation;
    double currentLatitude, currentLongitude;
    Location currentLocationNet;
    double currentLatitudeNet, currentLongitudeNet;


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
        inflater.inflate(R.menu.mnuclose, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog.Builder adb = new AlertDialog.Builder(WebReports.this);
        switch (item.getItemId()) {
            case R.id.menuClose:
                adb.setMessage("আপনি কি এই ফর্ম থেকে বের হতে চান?");
                adb.setNegativeButton("না", null);
                adb.setPositiveButton("হ্যাঁ", new AlertDialog.OnClickListener() {
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

    private boolean IsUserHA(String provtype)
    {
        if(provtype.equalsIgnoreCase("02"))
            return true;
        else
            return false;


    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.webreports);
            final WebView wv = (WebView)findViewById(R.id.webview);

            // Enable Javascript
            WebSettings webSettings = wv.getSettings();
            webSettings.setJavaScriptEnabled(true);

            //wv.loadUrl("http://beta.html5test.com/");
//            wv.loadUrl("http://mchd.icddrb.org/ibdweb");
//            wv.loadUrl("http://182.160.109.210/ibdweb");
            wv.loadUrl("http://103.174.189.131/ibdweb");

            // Force links and redirects to open in the WebView instead of in a browser
            wv.setWebViewClient(new WebViewClient());

            /*
            final ProgressDialog progDailog = ProgressDialog.show(WebReports.this, "", "অপেক্ষা করুন ...", true);
            new Thread() {
                public void run() {
                    wv.setWebViewClient(new WebViewClient() {
                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            view.loadUrl(url);
                            return true;
                        }
                    });

                    progDailog.dismiss();

                }
            }.start();

            wv.loadUrl("http://203.190.254.42:9080/RHISv2");
            */

            /*((Button) findViewById(R.id.cmdelco)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent f1 = new Intent(getApplicationContext(), Elcoview.class);
                        startActivity(f1);
                    }
                });



            ((Button) findViewById(R.id.cmdPreg)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent f1 = new Intent(getApplicationContext(), PregRegView.class);
                    startActivity(f1);
                }
            });

            ((Button)findViewById(R.id.cmdWInjectable)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent f1 = new Intent(getApplicationContext(), InjectableView.class);
                    startActivity(f1);
                }
            });

            ((Button) findViewById(R.id.cmddeath)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent f1 = new Intent(getApplicationContext(), Death.class);
                    startActivity(f1);
                }
            });
            ((Button) findViewById(R.id.cmdstock)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent f1 = new Intent(getApplicationContext(), ELCOCurrentStock.class);
                    startActivity(f1);
                }
            });
            ((Button) findViewById(R.id.cmdStockAdjustment)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    final ProgressDialog progDailog = ProgressDialog.show(
                            WebReports.this, "", "à¦…à¦ªà§‡à¦•à§à¦·à¦¾ à¦•à¦°à§à¦¨ ...", true);
                    new Thread() {
                        public void run() {

                            Intent f1 = new Intent(getApplicationContext(), StockAdjustment.class);
                            startActivity(f1);

                            progDailog.dismiss();

                        }
                    }.start();


                }
            });

                ((Button)findViewById(R.id.cmdu5Child)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        final ProgressDialog progDailog = ProgressDialog.show(
                                WebReports.this, "", "à¦…à¦ªà§‡à¦•à§à¦·à¦¾ à¦•à¦°à§à¦¨ ...", true);
                        new Thread() {
                            public void run() {
                                g.setCallFrom("1");
                                Intent f1 = new Intent(getApplicationContext(), Under5ChildView.class);
                                startActivity(f1);

                                progDailog.dismiss();

                            }
                        }.start();

                    }
                });

                ((Button)findViewById(R.id.cmdadolescent)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        final ProgressDialog progDailog = ProgressDialog.show(
                                WebReports.this, "", "à¦…à¦ªà§‡à¦•à§à¦·à¦¾ à¦•à¦°à§à¦¨ ...", true);
                        new Thread() {
                            public void run() {
                                g.setCallFrom("1");
                                Intent f1 = new Intent(getApplicationContext(), AdolescentView.class);
                                startActivity(f1);

                                progDailog.dismiss();

                            }
                        }.start();

                    }
                });

                ((Button) findViewById(R.id.cmdepi)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        final ProgressDialog progDailog = ProgressDialog.show(
                                WebReports.this, "", "à¦…à¦ªà§‡à¦•à§à¦·à¦¾ à¦•à¦°à§à¦¨ ...", true);
                        new Thread() {
                            public void run() {
                                g.setepiCallForm("2");
                                g.setepiSubBlockId("");
                                Intent f1 = new Intent(getApplicationContext(), EpiList.class);
                                startActivity(f1);

                                progDailog.dismiss();

                            }
                        }.start();

                    }
                });


                ((Button) findViewById(R.id.cmdepiwomen)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        final ProgressDialog progDailog = ProgressDialog.show(
                                WebReports.this, "", "à¦…à¦ªà§‡à¦•à§à¦·à¦¾ à¦•à¦°à§à¦¨ ...", true);
                        new Thread() {
                            public void run() {
                                Intent f1 = new Intent(getApplicationContext(), EpiListWoman.class);
                                startActivity(f1);

                                progDailog.dismiss();

                            }
                        }.start();

                    }
                });
*/



          /*  ((Button)findViewById(R.id.cmdepi)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent f1 = new Intent(getApplicationContext(), EpiList.class);
                    startActivity(f1);
                }
            });*/
  /*          ((Button)findViewById(R.id.cmdnrc)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                  Intent f1 = new Intent(getApplicationContext(), nrc.class);
                  //  Intent f1 = new Intent(getApplicationContext(), Twolistview.class);

                    startActivity(f1);
                }
            });
            ((Button)findViewById(R.id.cmdnrecqu)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent f1 = new Intent(getApplicationContext(), Requisition.class);
                    //  Intent f1 = new Intent(getApplicationContext(), Twolistview.class);

                    startActivity(f1);
                }
            });


            ((Button)findViewById(R.id.cmdAccepted)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent f1 = new Intent(getApplicationContext(), ReqAccepted.class);
                    //  Intent f1 = new Intent(getApplicationContext(), Twolistview.class);

                    startActivity(f1);
                }
            });

            if (IsUserHA(g.getProvType())) {
                ((Button) findViewById(R.id.cmdelco)).setVisibility(View.GONE);
                ((Button) findViewById(R.id.cmdPreg)).setVisibility(View.GONE);
                ((Button) findViewById(R.id.cmdstock)).setVisibility(View.GONE);
                ((Button) findViewById(R.id.cmdStockAdjustment)).setVisibility(View.VISIBLE);
                ((Button)findViewById(R.id.cmdu5Child)).setVisibility(View.GONE);
                ((Button)findViewById(R.id.cmdadolescent)).setVisibility(View.GONE);
            }

            else {
                ((Button) findViewById(R.id.cmdelco)).setVisibility(View.VISIBLE);
                ((Button) findViewById(R.id.cmdPreg)).setVisibility(View.VISIBLE);
                ((Button) findViewById(R.id.cmdWInjectable)).setVisibility(View.VISIBLE);
                ((Button) findViewById(R.id.cmdstock)).setVisibility(View.VISIBLE);
                ((Button) findViewById(R.id.cmdStockAdjustment)).setVisibility(View.VISIBLE);
                ((Button)findViewById(R.id.cmdu5Child)).setVisibility(View.VISIBLE);
                ((Button)findViewById(R.id.cmdadolescent)).setVisibility(View.VISIBLE);
            }*/
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            //PNC Section End
        } catch (Exception e) {
            Connection.MessageBox(WebReports.this, e.getMessage());
            return;
        }
    }
}






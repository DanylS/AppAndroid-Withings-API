package com.semmache.danyl.withingshy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.semmache.danyl.withingshy.model.UserReport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;


public class WithingsActivity extends AppCompatActivity {
    final String URL_BASE = "https://wbsapi.withings.net/measure";
    final String URL_ACTION = "?action=getmeas";
    final String URL_USERID = "&userid=10706169";
    final String URL_OAUTH_CONSUMER_KEY = "&oauth_consumer_key=6d97254d677d31bc7781930df84b37212c4e90016117dd54cd9edec6ec";
    final String URL_OAUTH_NONCE = "&oauth_nonce=70d4c03622482ddc1d42ad40d95ac4ca";
    final String URL_OAUTH_SIGNATURE = "&oauth_signature=JsvwKDgNdT1CsTocHWZ6prDZ%2F98%3D";
    final String URL_OAUTH_SIGNATURE_METHOD = "&oauth_signature_method=HMAC-SHA1";
    final String URL_OAUTH_TIMESTAMP = "&oauth_timestamp=1472349100";
    final String URL_OAUTH_TOKEN = "&oauth_token=9be95634668b178f5397ccc56bb3d52e808492f5f0cbe08895b64674f9e6c";
    final String URL_OAUTH_VERSION = "&oauth_version=1.0";

    private ArrayList<UserReport> userReportsList = new ArrayList<>();
    http://localhost/healyourself/index.php?userid=10706169&oauth_token=6c528856e8895cd633d54518b4884c2c0663431e5111909bb36116bf6988&oauth_verifier=QLxK92r07UF
    https://wbsapi.withings.net/measure?action=getmeas?action=getmeas&userid=10706169&oauth_consumer_key=6d97254d677d31bc7781930df84b37212c4e90016117dd54cd9edec6ec&oauth_nonce=70d4c03622482ddc1d42ad40d95ac4ca&oauth_signature=JsvwKDgNdT1CsTocHWZ6prDZ%2F98%3D&oauth_signature_method=HMAC-SHA1&oauth_timestamp=1472349100&oauth_token=9be95634668b178f5397ccc56bb3d52e808492f5f0cbe08895b64674f9e6c
    private TextView lastUpdate;
    private TextView dia;
    private TextView diaType;
    private TextView sys;
    private TextView sysType;
    private TextView bpm;
    private TextView bpmType;
    private TextView timeZone;
    private TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withings);

        lastUpdate = (TextView)findViewById(R.id.lastUpdate);
        dia = (TextView)findViewById(R.id.dia);
        diaType = (TextView)findViewById(R.id.diaType);
        sys = (TextView)findViewById(R.id.sys);
        sysType = (TextView)findViewById(R.id.sysType);
        bpm = (TextView)findViewById(R.id.bpm);
        bpmType = (TextView)findViewById(R.id.bpmType);
        timeZone = (TextView)findViewById(R.id.timeZone);
        result = (TextView)findViewById(R.id.result);


        final String url =  URL_BASE + URL_ACTION + URL_USERID + URL_OAUTH_CONSUMER_KEY
                + URL_OAUTH_NONCE + URL_OAUTH_SIGNATURE + URL_OAUTH_SIGNATURE_METHOD
                + URL_OAUTH_TIMESTAMP + URL_OAUTH_TOKEN + URL_OAUTH_VERSION;

        Log.e("request", url);
        final JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            int dia;
            int diaType;
            int sys;
            int sysType;
            int bpm;
            int bpmType;
            String result;


            @Override
            public void onResponse(JSONObject response) {
                try {
                    // Parsing body object
                    JSONObject body = response.getJSONObject("body");
                    // Get updatetime attribute
                    int updatetime = body.getInt("updatetime");
                    // Convert unix updatetime to current date
                    long unixSeconds = updatetime;
                    Date date = new Date(unixSeconds*1000L); // *1000 is to convert seconds to milliseconds
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"); // the format of your date
                    sdf.setTimeZone(TimeZone.getTimeZone("GMT+2")); // give a timezone reference for formating (see comment at the bottom
                    String lastUpdate = sdf.format(date);

                    // init
                    Log.v("Date", lastUpdate);
                    // Parsing measuregrps object
                    JSONArray measuregrps = body.getJSONArray("measuregrps");
                    for (int x = 0; x < 3; x++) {
                        JSONObject obj = measuregrps.getJSONObject(x);
                        //JSONArray measuresArr = obj.getJSONArray("grpid");
                        JSONArray measuresArr = obj.getJSONArray("measures");
                        if (x == 0) {
                            JSONObject Odia = measuresArr.getJSONObject(0);
                            dia = Odia.getInt("value");
                            diaType = Odia.getInt("type");
                            Log.v("JSON", "DIA :" + dia + " mmHg");
                            Log.v("JSON", "Type :" + diaType);

                            JSONObject Osys = measuresArr.getJSONObject(1);
                            sys = Osys.getInt("value");
                            sysType = Osys.getInt("type");
                            Log.v("JSON", "SYS :" + sys + " mmHg");
                            Log.v("JSON", "Type :" + sysType);

                            JSONObject Obpm = measuresArr.getJSONObject(2);
                            bpm = Obpm.getInt("value");
                            bpmType = Obpm.getInt("type");
                            Log.v("JSON", "BPM :" + bpm + " BPM");
                            Log.v("JSON", "Type :" + bpmType);
                        }

                        String timeZone = body.getString("timezone");
                        result = "";

                        UserReport report = new UserReport(lastUpdate, dia, diaType, sys, sysType, bpm, bpmType, timeZone, result);
                        userReportsList.add(report);
                    }
                    // Get timezone attribute

                    // Parsing measuregrps object

                } catch (JSONException e) {


                    Log.v("JSON", "EXC:" + e.getLocalizedMessage());
                }

                updateUI();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("FUN", "Err: " + error.getLocalizedMessage());
            }
        });

        Volley.newRequestQueue(this).add(jsonRequest);
    }

    public void updateUI() {

        if (userReportsList.size() > 2) {
            UserReport report = userReportsList.get(2);

            lastUpdate.setText(report.getLastUpdate());
            dia.setText(Integer.toString(report.getDia()) + " mmHg");
            diaType.setText(Integer.toString(report.getDiaType()));
            sys.setText(Integer.toString(report.getSys()) + " mmHg");
            sysType.setText(Integer.toString(report.getSysType()));
            bpm.setText(Integer.toString(report.getBpm()) + " BPM");
            bpmType.setText(Integer.toString(report.getBpmType()));
            timeZone.setText(report.getTimeZone());

            if (report.getSys() < 120 && report.getDia() < 80)
            {
                result.setText("Optimal");
                Log.v("PRINT", "Result :" + result);
            }
            else if (((report.getSys() >= 120) && (report.getSys() <= 129) && (report.getDia() >= 80 && report.getDia() <= 84))
                    || ((report.getSys() >= 120) && (report.getSys() <= 129) || (report.getDia() >= 80 && report.getDia() <= 84)))
            {
                result.setText("Normal");
                Log.v("PRINT", "Result :" + result);
            }
            else if (((report.getSys() >= 130) && (report.getSys() <= 139) && (report.getDia() >= 85 && report.getDia() <= 89))
                    || ((report.getSys() >= 130) && (report.getSys() <= 139) || (report.getDia() >= 85 && report.getDia() <= 89)))
            {
                result.setText("High Normal");
                Log.v("PRINT", "Result :" + result);
            }
            else if (((report.getSys() >= 140) && (report.getSys() <= 149) && (report.getDia() >= 90 && report.getDia() <= 99))
                    || ((report.getSys() >= 140) && (report.getSys() <= 149) || (report.getDia() >= 90 && report.getDia() <= 99)))
            {
                result.setText("Grade 1 hypertension");
                Log.v("PRINT", "Result :" + result);
            }
            else if (((report.getSys() >= 160) && (report.getSys() <= 179) && (report.getDia() >= 100 && report.getDia() <= 109))
                    || ((report.getSys() >= 160) && (report.getSys() <= 179) || (report.getDia() >= 100 && report.getDia() <= 109)))
            {
                result.setText("Grade 2 hypertension");
                Log.v("PRINT", "Result :" + result);
            }
            else if ((report.getSys() >= 180) && (report.getDia() >= 110)
                    || ((report.getSys() >= 180) || (report.getDia() >= 110)))
            {
                result.setText("Grade 3 hypertension");
                Log.v("PRINT", "Result :" + result);
            }
            else if ((report.getSys() >= 140) && (report.getDia() < 90))
            {
                result.setText("Isolated systolic hypertension");
                Log.v("PRINT", "Result :" + result);
            }
        }
    }
}
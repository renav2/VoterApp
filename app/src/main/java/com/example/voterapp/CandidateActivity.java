package com.example.voterapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.voterapp.adapter.CandidateAdapter;
import com.example.voterapp.model.Candidate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CandidateActivity extends AppCompatActivity {
    String election_id = "", election_type = "", election_date = "";
    ProgressDialog dialog;
    public static List<Candidate> candidates = new ArrayList<>();
    RecyclerView.Adapter adapter;
    RecyclerView rv_candidates;
    TextView txtId, txtType, txtTime;
    private CountDownTimer countDownTimer; // built in android class
    // CountDownTimer
    private long totalTimeCountInMilliseconds; // total count down time in
    // milliseconds
    private long timeBlinkInMilliseconds; // start time of start blinking
    private boolean blink; // controls the blinking .. on and off
    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate);
        prefManager = new PrefManager(this);
        rv_candidates = (RecyclerView) findViewById(R.id.rv_candidates);
        Intent intent = getIntent();
        if (intent != null) {
            election_date = intent.getStringExtra("election_date");
            election_id = intent.getStringExtra("election_id");
            prefManager.setUserID(election_id);
            election_type = intent.getStringExtra("election_type");
            loadCandidates();
        }
        txtId = (TextView) findViewById(R.id.txtElectionId);
        txtTime = (TextView) findViewById(R.id.txtTimeRemaining);
        txtType = (TextView) findViewById(R.id.txtElectionType);
        txtType.setText(election_type);
        txtId.setText(election_id);
        setTimer();
        startTimer();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void loadCandidates() {
        if (election_id.equalsIgnoreCase("")) {
            Toast.makeText(this, "There is no selected election", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setIndeterminate(false);
        dialog.setMessage("Please wait...");
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerUtility.url_get_Candidate_detials(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.has(ServerUtility.TAG_SUCCESS)) {
                                JSONArray jsonArray = jsonObject.getJSONArray("CandidateInfo");
                                candidates = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String name = object.getString("candidate_name");
                                    String photo = ServerUtility.Server_URL + "Profile/" + object.getString("profile");
                                    String dob = object.getString("birth_date");
                                    String mobile = object.getString("contact_no");
                                    String gender = object.getString("gender");
                                    String party = object.getString("party_name");
                                    String id = object.getString("id");
                                    Candidate candidate = new Candidate(name, mobile, dob, gender, party, photo, id);
                                    candidates.add(candidate);
                                }
                                adapter = new CandidateAdapter(candidates, CandidateActivity.this);
                                rv_candidates.setAdapter(adapter);
                                rv_candidates.setLayoutManager(new LinearLayoutManager(CandidateActivity.this));
                                adapter.notifyDataSetChanged();

                            } else {
                                Toast.makeText(CandidateActivity.this, "There is no Candidate added", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("e_id", election_id);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void setTimer() {
        try {
            String myDate = election_date + " 19:00:00";
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            Date date = sdf.parse(myDate);
//            totalTimeCountInMilliseconds = date.getTime();


            String dateStop = "";
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar cal = Calendar.getInstance();
            dateStop = format.format(cal.getTime());
            Date d1 = null;
            Date d2 = null;


            d1 = format.parse(myDate);
            d2 = format.parse(dateStop);

            //in milliseconds
            totalTimeCountInMilliseconds = d1.getTime() - d2.getTime();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(totalTimeCountInMilliseconds, 500) {
            // 500 means, onTick function will be called at every 500
            // milliseconds

            @Override
            public void onTick(long leftTimeInMilliseconds) {
                long seconds = leftTimeInMilliseconds / 1000;

                if (leftTimeInMilliseconds < timeBlinkInMilliseconds) {
//                    textViewShowTime.setTextAppearance(getApplicationContext(),
//                            R.style.blinkText);
                    // change the style of the textview .. giving a red
                    // alert style

                    if (blink) {
                        txtTime.setVisibility(View.VISIBLE);
                        // if blink is true, textview will be visible
                    } else {
                        txtTime.setVisibility(View.INVISIBLE);
                    }

                    blink = !blink; // toggle the value of blink
                }

                txtTime.setText(String.format("%02d:%02d:%02d", seconds / 3600,
                        (seconds % 3600) / 60, (seconds % 60)));
                // format the textview to show the easily readable format

            }

            @Override
            public void onFinish() {
                // this function will be called when the timecount is finished
                txtTime.setText("Time up!");

            }

        }.start();

    }
}

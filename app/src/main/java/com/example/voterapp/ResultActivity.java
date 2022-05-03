package com.example.voterapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import de.hdodenhof.circleimageview.CircleImageView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ResultActivity extends AppCompatActivity {
    String election_id = "";
    ProgressDialog dialog;
    TextView txtCandidateName, txtElectionType, txtVoteCount, txtPartyName;
    CircleImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        txtCandidateName = (TextView) findViewById(R.id.txtCandidateName);
        txtPartyName = (TextView) findViewById(R.id.txtPartyName);
        txtVoteCount = (TextView) findViewById(R.id.txtVoteCount);
        txtElectionType = (TextView) findViewById(R.id.txtElectionType);
        imageView = (CircleImageView) findViewById(R.id.iv_circular_user_image);
        Intent intent = getIntent();
        if (intent != null) {
            election_id = intent.getStringExtra("election_id");
            getResultDetails();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
    private void getResultDetails() {
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerUtility.url_get_result(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.has(ServerUtility.TAG_SUCCESS)) {


                                String name = jsonObject.getString("candidate_name");
                                String elctionType = jsonObject.getString("election_type");
                                String voteCount = jsonObject.getString("vote_count");
                                String party = jsonObject.getString("party_name");
                                String photo = ServerUtility.Server_URL + "Profile/" + jsonObject.getString("profile");
                                Picasso.with(ResultActivity.this)
                                        .load(photo)
                                        .into(imageView);

                                txtCandidateName.setText(name);
                                txtVoteCount.setText("( " + voteCount + " )");
                                txtElectionType.setText(elctionType);
                                txtPartyName.setText(party);
                            } else {
                                Toast.makeText(ResultActivity.this, "There is an Error..", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ResultActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", election_id);

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

}

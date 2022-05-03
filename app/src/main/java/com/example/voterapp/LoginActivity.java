package com.example.voterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    Button btnSignup, btnLogin;
    EditText txtUsername, txtPassword,txtServerIP;
    ProgressDialog dialog;
    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        prefManager = new PrefManager(this);
        txtServerIP=findViewById(R.id.txtServerIP);
        btnSignup = (Button) findViewById(R.id.btnSignup);
        btnLogin = (Button) findViewById(R.id.btn_signUp);
        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtServerIP.getText().toString().equals(""))
                {
                    Toast.makeText(LoginActivity.this, "Please enter Server IP", Toast.LENGTH_SHORT).show();
                    return;
                }
                ServerUtility.Server_URL="http://"+txtServerIP.getText().toString()+"/EVoting_System/";
                startActivity(new Intent(LoginActivity.this, ConfirmAuthorityActivity.class));
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtServerIP.getText().toString().equals(""))
                {
                    Toast.makeText(LoginActivity.this, "Please enter Server IP", Toast.LENGTH_SHORT).show();
                    return;
                }
                ServerUtility.Server_URL="http://"+txtServerIP.getText().toString()+"/EVoting_System/";
                loginCheck();
            }
        });
    }

    private void loginCheck() {
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setIndeterminate(false);
        dialog.setMessage("Please wait...");
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerUtility.url_login_check(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.has(ServerUtility.TAG_SUCCESS)) {
                                prefManager.setUserEmail(txtUsername.getText().toString());
                                prefManager.setUserAddress(jsonObject.getString("village"));
                                prefManager.setZipCode(jsonObject.getString("city"));
                                prefManager.setUserName(jsonObject.getString("name"));
                                prefManager.setLongitude(jsonObject.getString("voter_id"));
                                prefManager.setLatitude(jsonObject.getString("dob"));
                                prefManager.setUserPassword(txtPassword.getText().toString());
                                prefManager.setGender(jsonObject.getString("gender"));
                                prefManager.setUserMobile(jsonObject.getString("mobile"));
                                Toast.makeText(LoginActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, UserHomeActivity.class));
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "Error..", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
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
                params.put("username", txtUsername.getText().toString());
                params.put("password", txtPassword.getText().toString());

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

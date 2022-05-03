package com.example.voterapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Activity_SignUp extends AppCompatActivity {
    EditText etUsername, etPassword, etRePassword, etMobile, etEmail, etVillage, etCity, etVoterID, etAdhar;
    Button btnSignUp, btnLogin, btnDOB;
    CheckBox checkBox;
    private int mYear, mMonth, mDay, mHour, mMinute;
    ProgressDialog dialog;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__sign_up);
        etAdhar = findViewById(R.id.etAdhar);
        etVoterID = (EditText) findViewById(R.id.etVoterID);
        radioGroup = (RadioGroup) findViewById(R.id.genderGroup);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etRePassword = (EditText) findViewById(R.id.etRePassword);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etMobile = (EditText) findViewById(R.id.etMobile);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnSignUp = (Button) findViewById(R.id.btn_signUp);
        checkBox = (CheckBox) findViewById(R.id.chkBox1);
        etVillage = (EditText) findViewById(R.id.etVillage);
        etCity = (EditText) findViewById(R.id.etCity);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()) {
//                    registerVoter();
                    sendOTP();
                }
            }
        });
        btnDOB = (Button) findViewById(R.id.btnDOB);
        btnDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR) - 18;
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);


                    DatePickerDialog datePickerDialog = new DatePickerDialog(Activity_SignUp.this, new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            btnDOB.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                        }
                    }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private boolean validation() {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        String repass = etRePassword.getText().toString();
        String email = etEmail.getText().toString();
        String mobile = etMobile.getText().toString();
        String village = etVillage.getText().toString();
        String city = etCity.getText().toString();
        String adhar = etAdhar.getText().toString();
        if (username.equals("")) {
            Toast.makeText(this, "Please enter username", Toast.LENGTH_SHORT).show();
            return false;
        }

        //validate email address

        //validate password
        if (password.equals("")) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.length() < 5) {
            Toast.makeText(this, "Password length minimum 5", Toast.LENGTH_SHORT).show();
            return false;

        }
        if (!password.equals(repass)) {
            Toast.makeText(this, "Password does not matched", Toast.LENGTH_SHORT).show();
            return false;
        }

        //validate mobile number
        if (mobile.length() != 10) {
            Toast.makeText(this, "Please enter 10 digit mobile number", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (adhar.length() != 12) {
            Toast.makeText(this, "Please enter 12 digit adhar number", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (village.equals("")) {
            Toast.makeText(this, "Please enter village name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (city.equals("")) {
            Toast.makeText(this, "Please enter city name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etVoterID.getText().toString().equals("")) {
            Toast.makeText(this, "Please enter voter id", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (btnDOB.getText().toString().equals("DOB")) {
            Toast.makeText(this, "Please select DOB", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }

    private void verifyOTPAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_opt, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button btnVerify = (Button) dialogView.findViewById(R.id.btnValidate);
        final EditText etOTP = (EditText) dialogView.findViewById(R.id.etOTP);

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                verifyOTP(etOTP.getText().toString());
            }
        });
    }

    private void sendOTP() {
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setIndeterminate(false);
        dialog.setMessage("Please wait...");
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerUtility.url_send_otp(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.has(ServerUtility.TAG_SUCCESS)) {
                                Toast.makeText(Activity_SignUp.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                verifyOTPAlert();
                            } else {
                                Toast.makeText(Activity_SignUp.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Activity_SignUp.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("ERROR_MESSAGE", error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("adhar_number", etAdhar.getText().toString());
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

    private void verifyOTP(final String otp) {
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setIndeterminate(false);
        dialog.setMessage("Please wait...");
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerUtility.url_verify_otp(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.has(ServerUtility.TAG_SUCCESS)) {
                                Toast.makeText(Activity_SignUp.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                registerVoter();
                            } else {
                                Toast.makeText(Activity_SignUp.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                verifyOTPAlert();
                            }
                        } catch (JSONException e) {

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Activity_SignUp.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("ERROR_MESSAGE", error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("adhar_number", etAdhar.getText().toString());
                params.put("otp", otp);
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

    private void registerVoter() {
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setIndeterminate(false);
        dialog.setMessage("Please wait...");
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerUtility.url_register_voter(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.has(ServerUtility.TAG_SUCCESS)) {
                                Toast.makeText(Activity_SignUp.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Activity_SignUp.this, "Error..", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Activity_SignUp.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("ERROR_MESSAGE", error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", etUsername.getText().toString());
                params.put("email", etEmail.getText().toString());
                params.put("password", etPassword.getText().toString());
                params.put("dob", btnDOB.getText().toString());
                params.put("village", etVillage.getText().toString());
                params.put("mobile", etMobile.getText().toString());
                params.put("city", etCity.getText().toString());
                params.put("voter_id", etVoterID.getText().toString());
                params.put("adhar_number", etAdhar.getText().toString());
                int id = radioGroup.getCheckedRadioButtonId();
                String gender = "";
                if (id == R.id.Male) {
                    gender = "Male";
                } else {
                    gender = "Female";
                }
                params.put("gender", gender);
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

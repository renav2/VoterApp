package com.example.voterapp.ui.home;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.voterapp.R;
import com.example.voterapp.ServerUtility;
import com.example.voterapp.model.Candidate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {
    ProgressDialog dialog;
    private HomeViewModel homeViewModel;
    TextView txtElection, txtCandidates, txtVoters;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        txtElection = (TextView) root.findViewById(R.id.txtElection);
        txtCandidates = (TextView) root.findViewById(R.id.txtCandidates);
        txtVoters = (TextView) root.findViewById(R.id.txtVoters);
        loadDashboard();
        return root;
    }

    private void loadDashboard() {

        dialog = new ProgressDialog(HomeFragment.this.getContext());
        dialog.setIndeterminate(false);
        dialog.setMessage("Please wait...");
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerUtility.url_get_dashboard_details(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.has(ServerUtility.TAG_SUCCESS)) {
                                txtCandidates.setText(jsonObject.getString("candidates"));
                                txtElection.setText(jsonObject.getString("elections"));
                                txtVoters.setText(jsonObject.getString("voters"));
                            } else {
                                Toast.makeText(HomeFragment.this.getContext(), "Error", Toast.LENGTH_SHORT).show();
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
//                params.put("e_id", election_id);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(HomeFragment.this.getContext());
        requestQueue.add(stringRequest);
    }
}
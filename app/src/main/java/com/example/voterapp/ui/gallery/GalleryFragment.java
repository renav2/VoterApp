package com.example.voterapp.ui.gallery;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
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
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.voterapp.LoginActivity;
import com.example.voterapp.PrefManager;
import com.example.voterapp.R;
import com.example.voterapp.ServerUtility;
import com.example.voterapp.UserHomeActivity;
import com.example.voterapp.adapter.ElectionAdapter;
import com.example.voterapp.model.Election;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GalleryFragment extends Fragment {

    RecyclerView rv_election;
    ProgressDialog dialog;
    PrefManager prefManager;
    public static List<Election> electionList = new ArrayList<>();
    private RecyclerView.Adapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        prefManager = new PrefManager(this.getContext());
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        rv_election = (RecyclerView) root.findViewById(R.id.recyclerview);
        loadElectionDetails();
        return root;
    }

    private void loadElectionDetails() {
        dialog = new ProgressDialog(this.getContext());
        dialog.setCancelable(false);
        dialog.setIndeterminate(false);
        dialog.setMessage("Please wait...");
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerUtility.url_get_election_details(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.has(ServerUtility.TAG_SUCCESS)) {
                                JSONArray jsonArray = jsonObject.getJSONArray("ElectionInfo");
                                electionList = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String election_id = object.getString("election_id");
                                    String election_type = object.getString("election_type");
                                    String village = object.getString("village");
                                    String city = object.getString("city");
                                    String state = object.getString("state");
                                    String country = object.getString("country");
                                    String election_date = object.getString("election_date");
                                    String status = object.getString("status");
                                    Election election = new Election(election_id, election_type, village, city, state, country, election_date, status);
                                    electionList.add(election);
                                }
                                adapter = new ElectionAdapter(electionList, GalleryFragment.this.getContext());
                                rv_election.setAdapter(adapter);
                                rv_election.setLayoutManager(new LinearLayoutManager(GalleryFragment.this.getContext()));
                                adapter.notifyDataSetChanged();

                            } else {
                                Toast.makeText(GalleryFragment.this.getContext(), "There is no any election", Toast.LENGTH_SHORT).show();
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
                params.put("village", prefManager.getUserAddress());
                params.put("city", prefManager.getZipCode());
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this.getContext());
        requestQueue.add(stringRequest);
    }


}
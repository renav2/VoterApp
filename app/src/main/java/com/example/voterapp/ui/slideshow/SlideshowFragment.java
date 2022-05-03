package com.example.voterapp.ui.slideshow;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.voterapp.PrefManager;
import com.example.voterapp.R;

public class SlideshowFragment extends Fragment {
    EditText etUsername, etPassword, etMobile, etEmail, etVillage, etCity, etVoterID;
    Button btnDOB;
    private int mYear, mMonth, mDay, mHour, mMinute;
    ProgressDialog dialog;
    RadioButton rdMale, rdFemale;
    PrefManager prefManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        prefManager = new PrefManager(this.getContext());
        etVoterID = (EditText) root.findViewById(R.id.etVoterID);
        rdFemale = (RadioButton) root.findViewById(R.id.Female);

        rdMale = (RadioButton) root.findViewById(R.id.Male);
        etUsername = (EditText) root.findViewById(R.id.etUsername);
        etPassword = (EditText) root.findViewById(R.id.etPassword);
        etEmail = (EditText) root.findViewById(R.id.etEmail);
        etMobile = (EditText) root.findViewById(R.id.etMobile);
        etVillage = (EditText) root.findViewById(R.id.etVillage);
        etCity = (EditText) root.findViewById(R.id.etCity);
        btnDOB = (Button) root.findViewById(R.id.btnDOB);
loadDetails();
        return root;
    }

    private void loadDetails() {
        etVillage.setText(prefManager.getUserAddress());
        etCity.setText(prefManager.getZipCode());
        etUsername.setText(prefManager.getUserName());
        etPassword.setText(prefManager.getUserPassword());
        etEmail.setText(prefManager.getUserEmail());
        etMobile.setText(prefManager.getUserMobile());
        btnDOB.setText(prefManager.getLatitude());
        etVoterID.setText(prefManager.getLongitude());
        if (prefManager.getGender().equalsIgnoreCase("male")) {
            rdMale.setChecked(true);
        } else {
            rdFemale.setChecked(true);
        }
        etVillage.setEnabled(false);
        etCity.setEnabled(false);
        etUsername.setEnabled(false);
        etPassword.setEnabled(false);
        etEmail.setEnabled(false);
        etMobile.setEnabled(false);
        btnDOB.setEnabled(false);
        etVoterID.setEnabled(false);

    }
}
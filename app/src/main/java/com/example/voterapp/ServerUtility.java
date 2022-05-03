package com.example.voterapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by SDinesh on 15-02-2018.
 */

public class ServerUtility {
    public static String Server_URL = "http://192.168.0.113:8080/EVoting_System/";
    public static byte[] captureImage = null;
    public static String TAG_SUCCESS = "success";


    public static String url_login_check() {
        return Server_URL + "VoterLogin";
    }

    public static String url_register_voter() {
        return Server_URL + "VoterDetails";
    }

    public static String url_get_voter_details() {
        return Server_URL + "GetVoterDetails";
    }

    public static String url_get_election_details() {
        return Server_URL + "ElectionDetails";
    }

    public static String url_get_Candidate_detials() {
        return Server_URL + "CandidateDetails";
    }
    public static String url_get_dashboard_details() {
        return Server_URL + "GetDashboard";
    }
    public static String url_get_All_Candidate_detials() {
        return Server_URL + "AllCandidateDetails";
    }
    public static String url_get_result() {
        return Server_URL + "ElectionResult";
    }
    public static String url_provide_vote() {
        return Server_URL + "ProvideVote";
    }
    public static String url_verify_otp() {
        return Server_URL + "VerifyOTP";
    }
    public static String url_send_otp() {
        return Server_URL + "SendOTP";
    }
    public static List<String> subject_names = new ArrayList<String>();

    public static String getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, "");

    }

    public static void setDefaults(String str_key, String value, Context context) {
        SharedPreferences shre = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = shre.edit();
        edit.putString(str_key, value);
        edit.apply();
    }


}

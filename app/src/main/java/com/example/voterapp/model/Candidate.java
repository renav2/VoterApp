package com.example.voterapp.model;

public class Candidate {
    private String name;
    private String mobile;
    private String dob;
    private String gender;
    private  String party;
    private String photo;
    private String candidate_id;

    public Candidate(String name, String mobile, String dob, String gender, String party, String photo, String candidate_id) {
        this.name = name;
        this.mobile = mobile;
        this.dob = dob;
        this.gender = gender;
        this.party = party;
        this.photo = photo;
        this.candidate_id = candidate_id;
    }

    public String getCandidate_id() {
        return candidate_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getDob() {
        return dob;
    }

    public String getGender() {
        return gender;
    }

    public String getParty() {
        return party;
    }

    public String getPhoto() {
        return photo;
    }
}

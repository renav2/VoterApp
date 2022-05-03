package com.example.voterapp.model;

public class Election {
    private String election_id;
    private String election_type;
    private String village;
    private String city;
    private String state;
    private String country;
    private String election_date;
    private String election_status;

    public Election(String election_id, String election_type, String village, String city, String state, String country, String election_date, String election_status) {
        this.election_id = election_id;
        this.election_type = election_type;
        this.village = village;
        this.city = city;
        this.state = state;
        this.country = country;
        this.election_date = election_date;
        this.election_status = election_status;
    }

    public String getElection_id() {
        return election_id;
    }

    public String getElection_type() {
        return election_type;
    }

    public String getVillage() {
        return village;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public String getElection_date() {
        return election_date;
    }

    public String getElection_status() {
        return election_status;
    }

    public void setElection_id(String election_id) {
        this.election_id = election_id;
    }

    public void setElection_type(String election_type) {
        this.election_type = election_type;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setElection_date(String election_date) {
        this.election_date = election_date;
    }

    public void setElection_status(String election_status) {
        this.election_status = election_status;
    }
}

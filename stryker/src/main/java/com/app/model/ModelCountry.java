package com.app.model;

import java.util.List;

public class ModelCountry {

    public ModelCountry(String countryId, String countryName) {
        super();
        this.countryId = countryId;
        this.countryName = countryName;

    }

    private String countryId, countryName;
    private List<ModelState> states;

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public List<ModelState> getStates() {
        return states;
    }

    public void setStates(List<ModelState> states) {
        this.states = states;
    }

}

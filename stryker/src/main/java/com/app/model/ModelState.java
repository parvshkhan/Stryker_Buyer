package com.app.model;

import java.util.ArrayList;

public class ModelState {

    String stateId, stateName;
    ArrayList<ModelCityList> cityList;

    public ModelState(String stateId, String stateName) {
        super();
        this.stateId = stateId;
        this.stateName = stateName;
    }


    public ModelState(String stateId, String stateName,
                      ArrayList<ModelCityList> cityList) {
        super();
        this.stateId = stateId;
        this.stateName = stateName;
        this.cityList = cityList;
    }


    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public ArrayList<ModelCityList> getCityList() {
        return cityList;
    }

    public void setCityList(ArrayList<ModelCityList> cityList) {
        this.cityList = cityList;
    }


}

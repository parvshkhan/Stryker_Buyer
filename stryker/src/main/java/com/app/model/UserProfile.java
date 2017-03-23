package com.app.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserProfile {
    public static ArrayList<ModelCountry> countries = new ArrayList<ModelCountry>();
    public static ArrayList<ModelAddress> addresses = new ArrayList<ModelAddress>();


    public static void PopulateCountryStateCity(String responseString) {
        countries.clear();
        JSONObject jo;
        try {
            ModelCountry mod;
            jo = new JSONObject(responseString);
            JSONArray jaCountries = jo.getJSONArray("countries");
            for (int i = 0; i < jaCountries.length(); i++) {
                JSONObject joCountry = jaCountries.getJSONObject(i);
                String countryID = joCountry.getString("Id");
                String countryName = joCountry.getString("Name");
                mod = new ModelCountry(countryID, countryName);
                JSONArray jaState = joCountry.getJSONArray("States");
                mod.setStates(UserProfile.PopulateState(jaState));
                countries.add(mod);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    public static void PopulateAddress(String responseString) {
        addresses.clear();
        JSONObject jo;
        try {
            ModelAddress mod;
            jo = new JSONObject(responseString);
            JSONArray jaAddress = jo.getJSONArray("useraddresses");
            for (int i = 0; i < jaAddress.length(); i++) {
                JSONObject joAddress = jaAddress.getJSONObject(i);
                addresses.add(new ModelAddress(joAddress.getString("ID"), joAddress.getString("Address1"),
                        joAddress.getString("Address2"), joAddress.getString("Country"), joAddress.getString("State"), joAddress.getString("City"),
                        joAddress.getString("ZipCode")));
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    private static ArrayList<ModelState> PopulateState(JSONArray jaStates) {
        ArrayList<ModelState> stateList = new ArrayList<ModelState>();
        ModelState mod;
        for (int i = 0; i < jaStates.length(); i++) {
            try {
                JSONObject joState = jaStates.getJSONObject(i);
                String stateID = joState.getString("Id");
                String stateName = joState.getString("Name");
                mod = new ModelState(stateID, stateName);
                JSONArray jaCities = joState.getJSONArray("Cities");
                mod.setCityList(UserProfile.PopulateCity(jaCities));
                stateList.add(mod);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        return stateList;
    }

    private static ArrayList<ModelCityList> PopulateCity(JSONArray jaCities) {
        ArrayList<ModelCityList> cityList = new ArrayList<ModelCityList>();
        ModelCityList mod;
        for (int i = 0; i < jaCities.length(); i++) {
            JSONObject joCity;
            try {
                joCity = jaCities.getJSONObject(i);
                String stateID = joCity.getString("Id");
                String stateName = joCity.getString("Name");
                mod = new ModelCityList(stateID, stateName);
                cityList.add(mod);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        return cityList;
    }

    public static String[] Countries() {
        String[] cotr = new String[countries.size()];
        for (int i = 0; i < countries.size(); i++) {
            cotr[i] = countries.get(i).getCountryName();
        }

        return cotr;

    }

    public static String CountryID(String CountryName) {
        for (int i = 0; i < countries.size(); i++) {
            if (countries.get(i).getCountryName().equals(CountryName))
                return countries.get(i).getCountryId();
        }

        return "";

    }

    public static String CountryName(String CountryID) {
        for (int i = 0; i < countries.size(); i++) {
            if (countries.get(i).getCountryId().equals(CountryID))
                return countries.get(i).getCountryName();
        }

        return "";

    }

    public static String[] States(String CountryName) {
        String[] cotr = null;
        for (int i = 0; i < countries.size(); i++) {
            if (countries.get(i).getCountryName().equals(CountryName)) {
                cotr = new String[countries.get(i).getStates().size()];
                for (int j = 0; j < countries.get(i).getStates().size(); j++) {
                    cotr[j] = countries.get(i).getStates().get(j).getStateName();
                }
                continue;
            }
        }

        return cotr;

    }

    public static String StateID(String StateName) {
        for (int i = 0; i < countries.size(); i++) {
            for (int j = 0; j < countries.get(i).getStates().size(); j++) {
                if (countries.get(i).getStates().get(j).getStateName().equals(StateName))
                    return countries.get(i).getStates().get(j).getStateId();
            }
        }

        return "";

    }

    public static String StateName(String StateID) {
        for (int i = 0; i < countries.size(); i++) {
            for (int j = 0; j < countries.get(i).getStates().size(); j++) {
                if (countries.get(i).getStates().get(j).getStateId().equals(StateID))
                    return countries.get(i).getStates().get(j).getStateName();
            }
        }

        return "";

    }


    public static String[] Cities(String CountryName, String StateName) {
        String[] cotr = null;
        for (int i = 0; i < countries.size(); i++) {
            if (countries.get(i).getCountryName().equals(CountryName)) {
                ModelCountry ctry = countries.get(i);
                for (int j = 0; j < ctry.getStates().size(); j++) {
                    if (ctry.getStates().get(j).getStateName().equals(StateName)) {
                        cotr = new String[ctry.getStates().get(j).getCityList().size()];
                        for (int k = 0; k < ctry.getStates().get(j).getCityList().size(); k++) {
                            cotr[k] = ctry.getStates().get(j).getCityList().get(k).getCityName();
                        }
                        continue;
                    }
                }
            }
        }

        return cotr;

    }

    public static String CityID(String CityName) {
        for (int i = 0; i < countries.size(); i++) {
            for (int j = 0; j < countries.get(i).getStates().size(); j++) {
                for (int k = 0; k < countries.get(i).getStates().get(j).getCityList().size(); k++) {
                    if (countries.get(i).getStates().get(j).getCityList().get(k).getCityName().equals(CityName))
                        return countries.get(i).getStates().get(j).getCityList().get(k).getCityId();
                }
            }
        }

        return "";

    }

    public static String CityName(String CityID) {
        for (int i = 0; i < countries.size(); i++) {
            for (int j = 0; j < countries.get(i).getStates().size(); j++) {
                for (int k = 0; k < countries.get(i).getStates().get(j).getCityList().size(); k++) {
                    if (countries.get(i).getStates().get(j).getCityList().get(k).getCityId().equals(CityID))
                        return countries.get(i).getStates().get(j).getCityList().get(k).getCityName();
                }
            }
        }

        return "";

    }
}

package com.alessandro.sia.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.alessandro.sia.model.Appointment;
import com.alessandro.sia.model.Rating;
import com.alessandro.sia.model.Token;
import com.alessandro.sia.model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LocalStore {

    public static final String SP_NAME = "ClientDetails";
    SharedPreferences clientLocalDB;

    public LocalStore(Context context) {
        clientLocalDB = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void storeToken(String accessToken, String refreshToken ) throws JSONException {
        SharedPreferences.Editor editor = clientLocalDB.edit();
        editor.putString("access_token", accessToken);
        editor.putString("refresh_token", refreshToken);
        editor.commit();
    }
    public Token getStoredToken(){
        String access_token = clientLocalDB.getString("access_token", null);
        String refresh_token = clientLocalDB.getString("refresh_token", null);
        Token token = new Token();
        token.setAccess_token(access_token);
        token.setRefresh_token(refresh_token);
        return token;
    }
    public void storeUser(User user){
        SharedPreferences.Editor editor = clientLocalDB.edit();
        editor.putInt("id", user.getId());
        editor.putString("email", user.getEmail());
        editor.putString("password", user.getPassword());
        editor.putString("name", user.getName());
        editor.putString("surname", user.getSurname());
        editor.putString("competence", user.getCompetence());
        editor.putString("city", user.getCity());
        editor.putString("address", user.getAddress());
        editor.putString("phone", user.getPhone());
        editor.commit();
    }
    public User getStoredUser(){
        User user = new User();
        user.setId(clientLocalDB.getInt("id", 0));
        user.setEmail(clientLocalDB.getString("email", null));
        user.setPassword(clientLocalDB.getString("password", null));
        user.setName(clientLocalDB.getString("name", null));
        user.setSurname(clientLocalDB.getString("surname", null));
        user.setCompetence(clientLocalDB.getString("competence", null));
        user.setCity(clientLocalDB.getString("city", null));
        user.setAddress(clientLocalDB.getString("address", null));
        user.setPhone(clientLocalDB.getString("phone", null));
        return user;
    }

    public void storeLoggedUser(String loggedUserJson){
        SharedPreferences.Editor editor = clientLocalDB.edit();
        editor.putString("loggedUser", loggedUserJson);
        editor.commit();
    }

    public ArrayList<Rating> getRating() {
        String json = clientLocalDB.getString("loggedUser", null);
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("ratingsForUserReceiver");
            ArrayList<Rating> yourArray = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<Rating>>(){}.getType());
            return yourArray;
        } catch (JSONException e) {
            e.printStackTrace();
        }
       return null;

    }

    public User getStoredLoggedUser() {
        String json = clientLocalDB.getString("loggedUser", null);
        User yourArray = new Gson().fromJson(json, new TypeToken<User>(){}.getType());
        return yourArray;
    }
    public void storeAppointments(String appointmentsJson){
        SharedPreferences.Editor editor = clientLocalDB.edit();
        editor.putString("appointments", appointmentsJson);
        editor.commit();
    }

    public ArrayList<Appointment> getStoredAppointments() {
        String json = clientLocalDB.getString("appointments", null);
        ArrayList<Appointment> yourArray = new Gson().fromJson(json, new TypeToken<List<Appointment>>(){}.getType());
        return yourArray;
    }
}

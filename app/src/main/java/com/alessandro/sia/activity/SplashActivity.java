package com.alessandro.sia.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.alessandro.sia.R;
import com.alessandro.sia.model.User;
import com.alessandro.sia.util.LocalStore;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SplashActivity extends Activity {

    private LocalStore localStore;
    private static int SPLASH_TIME_OUT = 500;
    private static String URL_GETAPPOINTMENT = "http://192.168.1.102:8080/SiaRestServerJson/appointment/getAll/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        localStore = new LocalStore(this);

        User user = new User();
        user.setId(1);
        user.setEmail("alessandro.distefano@live");
        user.setPassword("123456");
        user.setName("Alessandro");
        user.setSurname("Di Stefano");
        user.setCompetence("Android");
        user.setCity("Roma");
        user.setAddress("via Toscana 16");
        user.setPhone("3927844137");

        localStore.storeUser(user);

        GET_APPOINTMENTS_TASK(getApplicationContext(), URL_GETAPPOINTMENT+localStore.getStoredUser().getId());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }, SPLASH_TIME_OUT);

    }

    public String GET_APPOINTMENTS_TASK(Context context, String url) {

        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        LocalStore localStore = new LocalStore(getApplicationContext());
                        localStore.storeAppointments(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );

        Volley.newRequestQueue(context).add(getRequest);
        return null;
    }

}

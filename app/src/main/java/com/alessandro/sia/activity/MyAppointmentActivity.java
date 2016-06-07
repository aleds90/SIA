package com.alessandro.sia.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.alessandro.sia.R;
import com.alessandro.sia.adapter.AppointmentAdapter;
import com.alessandro.sia.adapter.SlotAdapter;
import com.alessandro.sia.adapter.UserRatingAdapter;
import com.alessandro.sia.model.Appointment;
import com.alessandro.sia.model.Slot;
import com.alessandro.sia.model.User;
import com.alessandro.sia.util.LocalStore;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class MyAppointmentActivity extends AppCompatActivity {

    ImageView back;
    ListView lv_myAppointments;
    LocalStore localStore;
    private static String URL_GETAPPOINTMENT = "http://192.168.1.102:8080/SiaRestServerJson/appointment/getAll/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_appointment);
        localStore = new LocalStore(this);
        findViewById();
    }

    private void findViewById() {
        back = (ImageView)findViewById(R.id.tb_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        lv_myAppointments= (ListView)findViewById(R.id.myappointment_lv_appointments);
        //GET_APPOINTMENTS_TASK(getApplicationContext(), URL_GETAPPOINTMENT + localStore.getStoredUser().getId());

        appointment(getApplicationContext(), new LocalStore(this).getStoredToken().getAccess_token(), lv_myAppointments);

    }

    public void appointment(final Context context, String access_token, final ListView listView){
        final String url = "http://192.168.1.102:8080/appointment/api/appointment/"+new LocalStore(context).getStoredUser().getId()+"/?access_token="+access_token;;
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        final ArrayList<Appointment> yourArray = new Gson().fromJson(response, new TypeToken<List<Appointment>>(){}.getType());
                        listView.setAdapter(new AppointmentAdapter(yourArray, context));
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(context, AppointmentDetailsActivity.class);
                                intent.putExtra("Appointment", yourArray.get(position));
                                startActivity(intent);
                            }
                        });
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        ) ;
        Volley.newRequestQueue(context).add(postRequest);
    }

    public String GET_APPOINTMENTS_TASK(Context context, String url) {

        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        final ArrayList<Appointment> yourArray = new Gson().fromJson(response, new TypeToken<List<Appointment>>(){}.getType());
                        lv_myAppointments.setAdapter(new AppointmentAdapter(yourArray, getApplicationContext()));
                        lv_myAppointments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent detail = new Intent(getApplicationContext(), AppointmentDetailsActivity.class);
                                detail.putExtra("appointment", yourArray.get(position));
                                startActivity(detail);
                            }
                        });
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

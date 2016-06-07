package com.alessandro.sia.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;

import com.alessandro.sia.R;
import com.alessandro.sia.adapter.AppointmentAdapter;
import com.alessandro.sia.adapter.ReservationSlotAdapter;
import com.alessandro.sia.adapter.SlotAdapter;
import com.alessandro.sia.model.Appointment;
import com.alessandro.sia.model.Slot;
import com.alessandro.sia.util.LocalStore;
import com.alessandro.sia.util.NestedListView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class MyReservationSlotActivity extends AppCompatActivity {

    private static String URL_GETSLOTRESERVATION = "http://192.168.1.102:8080/SiaRestServerJson/slot/getReservationSlot/";
    NestedListView lv_slots;
    LocalStore localStore;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reservation_slot);
        findViewById();

    }

    private void findViewById() {
        localStore = new LocalStore(this);

        lv_slots = (NestedListView) findViewById(R.id.reservationSlot_lv_slots);
        slot(getApplicationContext(), new LocalStore(this).getStoredToken().getAccess_token(), lv_slots);

        //GET_SLOT_RESERVATION(getApplicationContext(), URL_GETSLOTRESERVATION + localStore.getStoredUser().getId());
        back = (ImageView)findViewById(R.id.tb_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    public String GET_SLOT_RESERVATION(Context context, String url) {

        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ArrayList<Slot> yourArray = new Gson().fromJson(response, new TypeToken<List<Slot>>(){}.getType());
                        lv_slots.setAdapter(new SlotAdapter(yourArray, getApplicationContext()));
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

    public void slot(final Context context, String access_token, final ListView listView){
        final String url = "http://192.168.1.102:8080/appointment/api/slot/byUser/"+new LocalStore(context).getStoredUser().getId()+"/?access_token="+access_token;;
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        final ArrayList<Slot> yourArray = new Gson().fromJson(response, new TypeToken<List<Slot>>(){}.getType());
                        listView.setAdapter(new SlotAdapter(yourArray, context));
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


}

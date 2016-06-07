package com.alessandro.sia.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alessandro.sia.R;
import com.alessandro.sia.model.Appointment;
import com.alessandro.sia.model.User;
import com.alessandro.sia.util.LocalStore;
import com.alessandro.sia.util.NestedListView;
import com.alessandro.sia.adapter.SlotAdapter;
import com.alessandro.sia.model.Slot;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AppointmentDetailsActivity extends AppCompatActivity {

    private static String URL_GETSLOT = "http://192.168.1.102:8080/SiaRestServerJson/slot/getAll/";
    NestedListView lv_slots;
    ImageView back;
    TextView name, competence, address;
    LocalStore localStore;
    Appointment appointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_details);
        findViewById();


        //GET_APPOINTMENTS_TASK(getApplicationContext(), URL_GETSLOT + appointment.getId());
        slot(getApplicationContext(), new LocalStore(this).getStoredToken().getAccess_token(), lv_slots);
    }




    private void findViewById() {
        
        appointment = (Appointment) getIntent().getSerializableExtra("Appointment");
        localStore = new LocalStore(this);
        back = (ImageView)findViewById(R.id.tb_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        lv_slots = (NestedListView)findViewById(R.id.appointmentdeatil_lv_slots);



        name        = (TextView)findViewById(R.id.detailAppointment_tv_name);
        name.setText(localStore.getStoredUser().getName() + " " + localStore.getStoredUser().getSurname());
        competence  = (TextView)findViewById(R.id.detailAppointment_tv_competence);
        competence.setText("Lezioni di " + appointment.getName());
        address     = (TextView)findViewById(R.id.detailAppointment_tv_description);
        address.setText(localStore.getStoredUser().getAddress() + ", " + localStore.getStoredUser().getCity());
    }



    public String GET_APPOINTMENTS_TASK(Context context, String url) {

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
        final String url = "http://192.168.1.102:8080/appointment/api/slot/byAppointment/"+appointment.getId()+"/?access_token="+access_token;;
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        final ArrayList<Slot> yourArray = new Gson().fromJson(response, new TypeToken<List<Slot>>(){}.getType());
                        listView.setAdapter(new SlotAdapter(yourArray, context));
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                                new SweetAlertDialog(AppointmentDetailsActivity.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Sei sicuro?")
                                        .setContentText("Aspetta la conferma del tuo maestro e la lezione sara' prenotata!")
                                        .setConfirmText("Si, prenota!")
                                        .setCancelText("Annulla")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {

                                                reservation(new LocalStore(getApplicationContext()).getStoredToken().getAccess_token(), new LocalStore(getApplicationContext()).getStoredLoggedUser(), yourArray.get(position).getId());
                                                sDialog.dismissWithAnimation();
                                            }
                                        })
                                        .show();
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

    public void reservation(String access_token, User user, int slot){



        JSONObject jsonobject = null;
        jsonobject = new JSONObject();
        JSONObject jsonobjectUser= new JSONObject();
        try {
            jsonobjectUser.put("id", user.getId());
            jsonobject.put("status", "Non Confermato");
            jsonobject.put("user", jsonobjectUser);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JSONObject sendJson = jsonobject;


        System.out.println(jsonobject.toString());
        final String token = access_token;
        String URL_INSERTAPPOINTMENT = "http://192.168.1.102:8080/appointment/api/reservation/insert/"+slot;

        StringRequest postRequest = new StringRequest(Request.Method.POST, URL_INSERTAPPOINTMENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                // System.out.println("auth:"+ new LocalStore(getApplicationContext()).getStoredToken().getAccess_token().toString());
                //map.put("Authorization", "Bearer " + new LocalStore(getApplicationContext()).getStoredToken().getAccess_token().toString());

                map.put("Authorization", "Bearer "+token);

                return map;
            }
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("appointment", sendJson.toString());
                return params;
            }
        };

        Volley.newRequestQueue(this).add(postRequest);
    }

}

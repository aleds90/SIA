package com.alessandro.sia.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.alessandro.sia.R;
import com.alessandro.sia.model.User;
import com.alessandro.sia.util.LocalStore;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class NewAppointmentActivity extends AppCompatActivity {

    ImageView back;
    DatePickerDialog dateDialog;
    SimpleDateFormat dateFormat;
    TextView newAppointment_tv_data, newAppointment_tv_hourEnd, newAppointment_tv_hourStart, newAppointment_tv_name, newAppointment_tv_description, newAppointment_tv_competence;
    Toolbar newAppointment_btn_create;
    LocalStore localStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_appointment);
        setDateDialog();
        findViewById();
    }

    private void findViewById() {
        back = (ImageView) findViewById(R.id.tb_back_creaappuntamento);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        localStore = new LocalStore(this);

        Calendar now = Calendar.getInstance();
        Calendar later = Calendar.getInstance();
        later.add(Calendar.HOUR_OF_DAY, 1);
        System.out.println(now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE));

        newAppointment_tv_hourStart = (TextView) findViewById(R.id.newAppointment_tv_hourStart);
        newAppointment_tv_hourStart.setText(now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE));
        newAppointment_tv_hourStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimeDialogStart();
            }
        });
        newAppointment_tv_hourEnd = (TextView) findViewById(R.id.newAppointment_tv_hourEnd);
        newAppointment_tv_hourEnd.setText(later.get(Calendar.HOUR_OF_DAY) + ":" + later.get(Calendar.MINUTE));
        newAppointment_tv_hourEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimeDialogEnd();
            }
        });

        newAppointment_btn_create = (Toolbar) findViewById(R.id.newAppointment_btn_create);
        newAppointment_btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TaskCreateAppointment(getApplicationContext(), localStore.getStoredUser(), new LocalStore(getApplicationContext()).getStoredToken().getAccess_token());

                test(new LocalStore(getApplicationContext()).getStoredToken().getAccess_token(), new LocalStore(getApplicationContext()).getStoredUser());

            }
        });

        newAppointment_tv_data = (TextView) findViewById(R.id.newAppointment_tv_data);
        newAppointment_tv_data.setText(dateFormat.format(Calendar.getInstance().getTime()));

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.layout_giornoappuntamento);
        relativeLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dateDialog.show();

                //dateDialog.show();
            }
        });

        RelativeLayout relativeLayout1 = (RelativeLayout)findViewById(R.id.layout_appointment3);
        relativeLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(NewAppointmentActivity.this);
                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.dialog_choose_data, null);
                dialogBuilder.setView(dialogView);

                dialogBuilder.show();
                
            }
        });

    }

    private void setTimeDialogStart() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, R.style.DialogTheme,new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                newAppointment_tv_hourStart.setText(selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.show();

    }

    private void setTimeDialogEnd() {

        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                newAppointment_tv_hourEnd.setText(selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.show();

    }

    private void setDateDialog(){
        Calendar newCalendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ITALY);
        dateDialog = new DatePickerDialog(this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                newAppointment_tv_data.setText(dateFormat.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    //task server php
    public void TaskNewAppointment(){
        String URL_INSERTAPPOINTMENT = "http://192.168.1.102:8080/SiaRestServerJson/appointment/insert";

        StringRequest postRequest = new StringRequest(Request.Method.POST, URL_INSERTAPPOINTMENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Intent main = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(main);
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
                map.put("Accept", "application/json");
                return map;
            }
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("name", newAppointment_tv_name.getText().toString());
                params.put("description", newAppointment_tv_description.getText().toString());
                params.put("dateStart", newAppointment_tv_data.getText().toString() +" "+ newAppointment_tv_hourStart.getText().toString() + ":00");
                params.put("dateEnd", newAppointment_tv_data.getText().toString()+" " + newAppointment_tv_hourEnd.getText().toString() + ":00");
                params.put("user", String.valueOf(localStore.getStoredUser().getId()));
                return params;
            }
        };

        Volley.newRequestQueue(this).add(postRequest);
    }
    //task server php
    public void TaskCreateSlot(){

        JSONObject jsonobject = null;
        try {
            JSONObject jsonobject_conf = new JSONObject();
            jsonobject_conf.put("uniqueReservation", false);

            JSONObject jsonobject_one = new JSONObject();
            jsonobject_one.put("type", "reservable");
            jsonobject_one.put("configuration", jsonobject_conf);

            JSONObject jsonobject_conf2 = new JSONObject();
            jsonobject_conf2.put("slot_duration", 30);
            jsonobject_conf2.put("can_adapt_appointment_end_time", false);
            JSONObject jsonobject_TWO = new JSONObject();
            jsonobject_TWO.put("type", "fixed_duration");
            jsonobject_TWO.put("configuration", jsonobject_conf2);

            jsonobject = new JSONObject();
            jsonobject.put("strategy", jsonobject_TWO);
            jsonobject.put("slotFactory", jsonobject_one);



        }catch (JSONException e) {
            e.printStackTrace();
        }
        String url = "http://apiappointment1.altervista.org/web/api/appointments/3/slot-generator.json";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST,url, jsonobject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Accept", "application/json");
                headers.put("Authorization", "Bearer " + new LocalStore(getApplicationContext()).getStoredToken().getAccess_token().toString());
                return headers;
            }
        };
    }

    public void TaskCreateAppointment(Context context, User user, final String access_token ){

        JSONObject jsonobject = null;
        try {
            jsonobject = new JSONObject();
            JSONObject jsonobjectUser= new JSONObject();
            jsonobjectUser.put("id", user.getId());
            jsonobject.put("name", user.getCompetence());
            jsonobject.put("dateStart", newAppointment_tv_data.getText().toString() +" "+ newAppointment_tv_hourStart.getText().toString() + ":00");
            jsonobject.put("dateEnd",newAppointment_tv_data.getText().toString()+" " + newAppointment_tv_hourEnd.getText().toString() + ":00");
            jsonobject.put("description", user.getAddress()+", "+ user.getCity());
            jsonobject.put("user", jsonobjectUser);

            System.out.println(jsonobject.toString());

        }catch (JSONException e) {
            e.printStackTrace();
        }
        String url = "http://192.168.1.102:8080/appointment/api/appointment/insert";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST,url, jsonobject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                // System.out.println("auth:"+ new LocalStore(getApplicationContext()).getStoredToken().getAccess_token().toString());
                //map.put("Authorization", "Bearer " + new LocalStore(getApplicationContext()).getStoredToken().getAccess_token().toString());
                map.put("Accept", "application/json");
                map.put("Authorization", "Bearer " + access_token);
                map.put("Content-type", "application/json");
                return map;
            }
        };
        Volley.newRequestQueue(context).add(jsonObjReq);
    }

    public void test(String access_token, User user){


        JSONObject jsonobject = null;
        jsonobject = new JSONObject();
        JSONObject jsonobjectUser= new JSONObject();
        try {
            jsonobjectUser.put("id", user.getId());
            jsonobject.put("name", user.getCompetence());
            jsonobject.put("dateStart", newAppointment_tv_data.getText().toString() +" "+ newAppointment_tv_hourStart.getText().toString() + ":00");
            jsonobject.put("dateEnd",newAppointment_tv_data.getText().toString()+" " + newAppointment_tv_hourEnd.getText().toString() + ":00");
            jsonobject.put("description", user.getAddress()+", "+ user.getCity());
            jsonobject.put("user", jsonobjectUser);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JSONObject sendJson = jsonobject;


            System.out.println(jsonobject.toString());
        final String token = access_token;
        String URL_INSERTAPPOINTMENT = "http://192.168.1.102:8080/appointment/api/appointment/insertTest";

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

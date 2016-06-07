package com.alessandro.sia.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.alessandro.sia.R;
import com.alessandro.sia.model.Appointment;
import com.alessandro.sia.model.User;
import com.alessandro.sia.util.LocalStore;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity  {

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);

        Button mEmailLoginButton = (Button)findViewById(R.id.email_login_button);
        mEmailLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                login(getApplicationContext(), mEmailView.getText().toString(), mPasswordView.getText().toString());

//                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                startActivity(intent);
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
               // TaskRegistration();
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginActivity.this);
                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View dialogView = inflater.inflate(R.layout.dialog_registration, null);
                final EditText et_email =(EditText)dialogView.findViewById(R.id.et_registration_visitor_email);
                final EditText et_password =(EditText)dialogView.findViewById(R.id.et_registration_visitor_password);
                final EditText et_name =(EditText)dialogView.findViewById(R.id.et_registration_visitor_name);
                final EditText et_surname =(EditText)dialogView.findViewById(R.id.et_registration_visitor_surname);
                final EditText et_competence =(EditText)dialogView.findViewById(R.id.et_registration_visitor_competence);
                final EditText et_city =(EditText)dialogView.findViewById(R.id.et_registration_visitor_city);
                final EditText et_address =(EditText)dialogView.findViewById(R.id.et_registration_visitor_address);
                final CheckBox checkBox = (CheckBox)dialogView.findViewById(R.id.cb_registration_visitor_terms);
                final User user = new User();
                dialogBuilder.setPositiveButton("Conferma", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        user.setName(et_name.getText().toString());
                        user.setSurname(et_surname.getText().toString());
                        user.setEmail(et_email.getText().toString());
                        user.setPassword(et_password.getText().toString());
                        user.setCompetence(et_competence.getText().toString());
                        user.setCity(et_city.getText().toString());
                        user.setAddress(et_address.getText().toString());

                        if (checkBox.isChecked()){
                            registration(getApplicationContext(), user);
                            new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Registrazione effettuata!")
                                    .setContentText("Conferma la registrazione nella tua email per poter accedere!")
                                    .show();
                        }

                    }
                });
                dialogBuilder.setNegativeButton("annulla", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                });

                dialogBuilder.setView(dialogView);
                dialogBuilder.show();

            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

    }

    public void TaskRegistration(User user){
        String url = "http://192.168.1.102:8080/appointment/registration/user";
        final User u = user;
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            System.out.println(response);
                            JSONObject jsonResponse = new JSONObject(response);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                return headers;
            }

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("name", u.getName());
                params.put("surname", u.getSurname());
                params.put("email", u.getEmail());
                params.put("password", u.getPassword());
                params.put("competence", u.getCompetence());
                params.put("address", u.getAddress());
                params.put("city", u.getCity());
                params.put("description", "");
                params.put("username", u.getEmail());
                return params;
            }
        };

        Volley.newRequestQueue(this).add(postRequest);
    }



    public void TaskLogin(){
        String url = "http://apiappointment1.altervista.org/web/oauth/v2/token";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonResponse = new JSONObject(response);

                            if (response!=null){
                                LocalStore localStore = new LocalStore(getApplicationContext());
                                localStore.storeToken(jsonResponse.getString("access_token"),jsonResponse.getString("refresh_token"));
                                Intent main = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(main);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("client_id", "2_67obwcnqpxooskgookw4sk0w8408k0o4s8kckkkgggswcks8sk");
                params.put("client_secret", "2m1m2d3b2ncw48ggscsw08k8gwkwo8c0swg8wwsw84w8ggk800");
                params.put("grant_type", "password");
                params.put("username", mEmailView.getText().toString());
                params.put("password", mPasswordView.getText().toString());
                return params;
            }
        };


        Volley.newRequestQueue(this).add(postRequest);
    }

    public void login(Context context, String username, String password){
        JSONObject jsonobject = null;
            jsonobject = new JSONObject();


        final String url = "http://192.168.1.102:8080/appointment/oauth/token?grant_type=password&client_id=restapp&client_secret=restapp&username="+username+"&password="+password;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST,url, jsonobject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                            System.out.println(response);
                        try {
                            getLoggedUser(getApplicationContext(), response.getString("access_token"));
                            new LocalStore(getApplicationContext()).storeToken(response.getString("access_token"), response.getString("refresh_token"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                return headers;
            }
        };
        Volley.newRequestQueue(context).add(jsonObjReq);
    }

    public void registration(Context context, User user){

        JSONObject jsonobject = null;
        try {
            jsonobject = new JSONObject();
            jsonobject.put("name", user.getName());
            jsonobject.put("surname", user.getSurname());
            jsonobject.put("email",user.getEmail());
            jsonobject.put("password", user.getPassword());
            jsonobject.put("competence", user.getCompetence());
            jsonobject.put("address", user.getAddress());
            jsonobject.put("city", user.getCity());
            jsonobject.put("description", "");
            jsonobject.put("username", user.getEmail());
            System.out.println(jsonobject.toString());

        }catch (JSONException e) {
            e.printStackTrace();
        }
        String url = "http://192.168.1.102:8080/appointment/registration/user";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST,url, jsonobject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


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
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                return headers;
            }
        };
        Volley.newRequestQueue(context).add(jsonObjReq);
    }

    public void getLoggedUser(Context context, String access_token){
        JSONObject jsonobject = null;
        jsonobject = new JSONObject();


        final String url = "http://192.168.1.102:8080/appointment/api/user/getLoggedUser/?access_token="+access_token;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET,url, jsonobject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("user loggato:" + response);
                        new LocalStore(getApplicationContext()).storeLoggedUser(response.toString());
                        Intent main = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(main);
//                        try {
//                           System.out.println(response);
//                            //new LocalStore(getApplicationContext()).storeUser());
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }

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
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                return headers;
            }
        };
        Volley.newRequestQueue(context).add(jsonObjReq);
    }

    public void getAllAnnouncements(Context context, String access_token){
        JSONObject jsonobject = null;
        jsonobject = new JSONObject();


        final String url = "http://192.168.1.102:8080/appointment/api/user/getAllProfessionist/?access_token="+access_token;
        System.out.println(url);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET,url, jsonobject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("annunci: "+response);

//                        try {
//                           System.out.println(response);
//                            //new LocalStore(getApplicationContext()).storeUser());
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());
                System.out.println(error.getStackTrace().toString());
                System.out.println(error.getMessage());
            }
        }) {

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                return headers;
            }
        };
        Volley.newRequestQueue(context).add(jsonObjReq);
    }
}


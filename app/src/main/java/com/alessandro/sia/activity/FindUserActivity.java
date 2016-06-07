package com.alessandro.sia.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alessandro.sia.R;
import com.alessandro.sia.adapter.UserAdapter;
import com.alessandro.sia.adapter.UserRatingAdapter;
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

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class FindUserActivity extends AppCompatActivity {

    private String  SHOWCASE_ID ="1";
    TextView status_star;
    ListView lv_users;
    ImageView img_back, img_star;
    EditText ed_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_user);

        img_back = (ImageView)findViewById(R.id.findUser_tb_back);
        findViewById();
    }

    private void findViewById() {
        final LocalStore localStore = new LocalStore(this);

        lv_users = (ListView)findViewById(R.id.findUser_lv_users);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ed_search = (EditText)findViewById(R.id.findUser_et_search);
        ed_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (ed_search.getText().toString().equals(null) || ed_search.getText().toString().equals("")) {
                    lv_users.setAdapter(null);
                }else{
                    search(getApplicationContext(), localStore.getStoredToken().getAccess_token(), lv_users, "/"+ed_search.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        status_star = (TextView)findViewById(R.id.tv_status_star);

        img_star = (ImageView)findViewById(R.id.star);
        img_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status_star.getText().toString().equals("1")) {
                    img_star.setColorFilter(getResources().getColor(R.color.star));
                    status_star.setText("2");
                } else {
                    img_star.setColorFilter(getResources().getColor(R.color.white));
                    status_star.setText("1");
                }
            }
        });

        new MaterialShowcaseView.Builder(this)
                .setTarget(img_star)
                .setDismissText("GOT IT")
                .setContentText("This is some amazing feature you should know about")
                .singleUse(SHOWCASE_ID) // provide a unique ID used to ensure it is only shown once
                .show();
    }

    // Task ricerca
    public void search(final Context context, String access_token, final ListView listView, final String param){
        final String url = "http://192.168.1.102:8080/appointment/api/user"+param+"/?access_token="+access_token;;
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        final ArrayList<User> yourArray = new Gson().fromJson(response, new TypeToken<List<User>>(){}.getType());
                        listView.setAdapter(new UserAdapter(yourArray,context));
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent user = new Intent(FindUserActivity.this, UserActivity.class);
                                user.putExtra("User", yourArray.get(position));
                                startActivity(user);
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


}

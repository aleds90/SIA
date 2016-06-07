package com.alessandro.sia.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alessandro.sia.R;
import com.alessandro.sia.adapter.AppointmentAdapter;
import com.alessandro.sia.model.Appointment;
import com.alessandro.sia.model.User;
import com.alessandro.sia.util.LocalStore;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemLongClickListener;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity implements OnMenuItemClickListener, OnMenuItemLongClickListener {
    ImageView ic_add, ic_back;
    TextView title, description;
    private ContextMenuDialogFragment mMenuDialogFragment;
    private FragmentManager fragmentManager;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        findViewById();
    }

    private void findViewById() {
        title = (TextView)findViewById(R.id.user_activity_title);
        description = (TextView)findViewById(R.id.user_activity_description);
        user = (User) getIntent().getSerializableExtra("User");
        title.setText(user.getName()+" "+user.getSurname());
        description.setText(user.getDescription());

        fragmentManager = getSupportFragmentManager();
        ic_add = (ImageView)findViewById(R.id.ic_add);
        ic_back = (ImageView)findViewById(R.id.tb_back);
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        initMenuFragment();
        ic_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMenuDialogFragment.show(fragmentManager, ContextMenuDialogFragment.TAG);
            }
        });
    }

    private void initMenuFragment() {
        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize((int) getResources().getDimension(R.dimen.tool_bar_height));
        menuParams.setMenuObjects(getMenuObjects());
        menuParams.setClosableOutside(false);
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
        mMenuDialogFragment.setItemClickListener(this);
        mMenuDialogFragment.setItemLongClickListener(this);
    }


    private List<MenuObject> getMenuObjects() {
        // You can use any [resource, bitmap, drawable, color] as image:
        // item.setResource(...)
        // item.setBitmap(...)
        // item.setDrawable(...)
        // item.setColor(...)
        // You can set image ScaleType:
        // item.setScaleType(ScaleType.FIT_XY)
        // You can use any [resource, drawable, color] as background:
        // item.setBgResource(...)
        // item.setBgDrawable(...)
        // item.setBgColor(...)
        // You can use any [color] as text color:
        // item.setTextColor(...)
        // You can set any [color] as divider color:
        // item.setDividerColor(...)

        List<MenuObject> menuObjects = new ArrayList<>();

        MenuObject close = new MenuObject();
        Drawable mDrawable = getResources().getDrawable(R.drawable.ic_close_white_24dp);
        mDrawable.setColorFilter(getResources().getColor(R.color.background2), android.graphics.PorterDuff.Mode.MULTIPLY);
        close.setDrawable(mDrawable);

        MenuObject send = new MenuObject("Invia Messaggio");
        Drawable mDrawable1 = getResources().getDrawable(R.drawable.ic_message_white_24dp);
        mDrawable1.setColorFilter(getResources().getColor(R.color.background2), android.graphics.PorterDuff.Mode.MULTIPLY);
        send.setDrawable(mDrawable1);

        MenuObject like = new MenuObject("Aggiungi Preferiti");

        Drawable mDrawable2 = getResources().getDrawable(R.drawable.ic_person_add_white_24dp);
        mDrawable2.setColorFilter(getResources().getColor(R.color.background2), android.graphics.PorterDuff.Mode.MULTIPLY);
        like.setDrawable(mDrawable2);

        MenuObject appointment = new MenuObject("Vedi Appuntamenti");
        Drawable mDrawable3 = getResources().getDrawable(R.drawable.ic_assignment_white_24dp);
        mDrawable3.setColorFilter(getResources().getColor(R.color.background2), android.graphics.PorterDuff.Mode.MULTIPLY);
        appointment.setDrawable(mDrawable3);

        MenuObject rating = new MenuObject("Esprimi un parere");
        Drawable mDrawable4 = getResources().getDrawable(R.drawable.ic_star_white_24dp);
        mDrawable4.setColorFilter(getResources().getColor(R.color.background2), android.graphics.PorterDuff.Mode.MULTIPLY);
        rating.setDrawable(mDrawable4);

        menuObjects.add(close);
        menuObjects.add(send);
        menuObjects.add(like);
        menuObjects.add(rating);
        menuObjects.add(appointment);
        return menuObjects;
    }

    @Override
    public void onMenuItemClick(View clickedView, int position) {
        Toast.makeText(this, "Clicked on position: " + position, Toast.LENGTH_SHORT).show();
        if (position == 4){
            Intent intent = new Intent(UserActivity.this, UserAppointmentActivity.class);
            intent.putExtra("User", user);
            startActivity(intent);
        }
    }

    @Override
    public void onMenuItemLongClick(View clickedView, int position) {
        Toast.makeText(this, "Long clicked on position: " + position, Toast.LENGTH_SHORT).show();

    }


}

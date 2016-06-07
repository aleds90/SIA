package com.alessandro.sia.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Vibrator;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alessandro.sia.R;
import com.alessandro.sia.adapter.UserRatingAdapter;
import com.alessandro.sia.model.Appointment;
import com.alessandro.sia.model.Rating;
import com.alessandro.sia.util.LocalStore;
import com.alessandro.sia.model.User;
import com.alessandro.sia.util.ShakeListener;
import com.alessandro.sia.util.ViewTargets;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.amlcurran.showcaseview.ShowcaseDrawer;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private ShakeListener mShaker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation);

        //checkRefreshToken(getApplicationContext(), new LocalStore(this).getStoredToken().getRefresh_token());


        User user = new LocalStore(this).getStoredLoggedUser();
        if (user != null) {
            System.out.println(user.getName());
            System.out.println(user.getRatingsForUserReceiver().size());
        }


//        final Vibrator vibe = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
//
//        mShaker = new ShakeListener(this);
//        mShaker.setOnShakeListener(new ShakeListener.OnShakeListener() {
//            public void onShake() {
//                vibe.vibrate(100);
//                new AlertDialog.Builder(MainActivity.this)
//                        .setPositiveButton(android.R.string.ok, null)
//                        .setMessage("Shooken!")
//                        .show();
//            }
//        });

        // Toolbar settings
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    private void checkRefreshToken(final Context context, String refresh_token){
        JSONObject jsonobject = null;
        jsonobject = new JSONObject();


        final String url = "http://192.168.1.102:8080/appointment/oauth/token?grant_type=refresh_token&client_id=restapp&client_secret=restapp&refresh_token="+refresh_token;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET,url, jsonobject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);
                        try {
                            new LocalStore(getApplicationContext()).storeToken(response.getString("access_token"), response.getString("refresh_token"));
                            System.out.println("token aggiornati");
                            System.out.println(new LocalStore(context).getStoredToken().getRefresh_token());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Intent login = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(login);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.setting_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(getApplicationContext(), MyAppointmentActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_login) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
    }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment  {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        LocalStore localStore =null;



        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public void announcement(final Context context, String access_token, final ListView listView){
            final String url = "http://192.168.1.102:8080/appointment/api/user/getAllProfessionist/?access_token="+access_token;;
            StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            final ArrayList<User> yourArray = new Gson().fromJson(response, new TypeToken<List<User>>(){}.getType());
                            listView.setAdapter(new UserRatingAdapter(yourArray,context));
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent user = new Intent(getContext(), UserActivity.class);
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

        @Override
        public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_main_viewpager2, container, false);

            ListView lv_userRating = (ListView)rootView.findViewById(R.id.viewpager2_userlist);
            announcement(getContext(), new LocalStore(getContext()).getStoredToken().getAccess_token(), lv_userRating);

            //Fragment Profile
            if (getArguments().getInt(ARG_SECTION_NUMBER)==3) {
                localStore = new LocalStore(getContext());
                View view = inflater.inflate(R.layout.activity_main_viewpager3, container, false);

                TextView name = (TextView)view.findViewById(R.id.viewpager3_name);
                name.setText(localStore.getStoredLoggedUser().getName() +" "+ localStore.getStoredLoggedUser().getSurname());

                TextView competence = (TextView)view.findViewById(R.id.viewpager3_competence);
                TextView city = (TextView) view.findViewById(R.id.viewpager3_city);
                if (!localStore.getStoredLoggedUser().getCompetence().isEmpty()) {
                    competence.setText("Insegnante di " + localStore.getStoredLoggedUser().getCompetence());
                    city.setText("Presso "+localStore.getStoredLoggedUser().getAddress()+", "+ localStore.getStoredLoggedUser().getCity());

                }
                TextView description = (TextView)view.findViewById(R.id.viewpager3_description);
                description.setText(localStore.getStoredLoggedUser().getDescription());

                RatingBar ratingBar=(RatingBar)view.findViewById(R.id.viewpager_ratingBar);
                TextView ratingNumber = (TextView)view.findViewById(R.id.viewpager_ratingNumber);

                float media = 0;
                int somma = 0;
                new LocalStore(getContext()).getRating();
                System.out.println(new LocalStore(getContext()).getStoredToken().getRefresh_token());
                ArrayList<Rating> rating = new LocalStore(getContext()).getRating();
                if (rating!=null) {
                    for (int i = 0; i < rating.size(); i++) {
                        somma += rating.get(i).getRate();
                    }
                    if (somma!=0)
                    media = somma/rating.size();
                    else{}
                }
                if (media!=0)
                ratingBar.setRating(media);

                if (rating!=null)
                ratingNumber.setText("Votato da "+rating.size()+" persone");
                else ratingNumber.setText("Votato da 0 persone");

                System.out.println(media);
                System.out.println(somma);


                return view;
            }

            if (getArguments().getInt(ARG_SECTION_NUMBER)==1) {
                View view = inflater.inflate(R.layout.activity_main_viewpager1test, container, false);
                RelativeLayout createReservation = (RelativeLayout)view.findViewById(R.id.main_layout_createReservation);
                createReservation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent createReservation = new Intent(getContext(), FindUserActivity.class);
                        startActivity(createReservation);
                    }
                });

                RelativeLayout myReservation = (RelativeLayout)view.findViewById(R.id.main_layout_myReservation);
                myReservation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent myReservation = new Intent(getContext(), MyReservationSlotActivity.class);
                        startActivity(myReservation);
                    }
                });
                RelativeLayout createAppointment = (RelativeLayout)view.findViewById(R.id.main_layout_createAppointment);
                createAppointment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent createAppointment = new Intent(getContext(), NewAppointmentActivity.class);
                        startActivity(createAppointment);
                    }
                });
                RelativeLayout myAppointment = (RelativeLayout)view.findViewById(R.id.main_layout_myAppointment);
                myAppointment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent myAppointment = new Intent(getContext(), MyAppointmentActivity.class);
                        startActivity(myAppointment);
                    }
                });

                ShowcaseConfig config = new ShowcaseConfig();
                config.setDelay(300); // half second between each showcase view

                MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(getActivity(), "");

                sequence.setConfig(config);
                sequence.addSequenceItem(createReservation,
                        "Ricerca utenti e prenota appuntamenti", "AVANTI");

                sequence.addSequenceItem(createAppointment,
                        "Crea nuovi appuntamenti", "AVANTI");

                sequence.addSequenceItem(myReservation,
                        "Controlla le tue prenotazioni", "AVANTI");

                sequence.addSequenceItem(myAppointment,
                        "Gestisci i tuoi appuntamenti", "AVANTI");
                sequence.start();


                return view;
            }else {
                return rootView;
            }

        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Appuntamenti";
                case 1:
                    return "Annunci";
                case 2:
                    return "Profilo";
            }
            return null;
        }
    }

    private static class CustomShowcaseView implements ShowcaseDrawer {

        private final float width;
        private final float height;
        private final Paint eraserPaint;
        private final Paint basicPaint;
        private final int eraseColour;
        private final RectF renderRect;

        public CustomShowcaseView(Resources resources) {
            width = resources.getDimension(R.dimen.custom_showcase_width);
            height = resources.getDimension(R.dimen.custom_showcase_height);
            PorterDuffXfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY);
            eraserPaint = new Paint();
            eraserPaint.setColor(0xFFFFFF);
            eraserPaint.setAlpha(0);
            eraserPaint.setXfermode(xfermode);
            eraserPaint.setAntiAlias(true);
            eraseColour = resources.getColor(R.color.custom_showcase_bg);
            basicPaint = new Paint();
            renderRect = new RectF();
        }

        @Override
        public void setShowcaseColour(int color) {
            eraserPaint.setColor(color);
        }

        @Override
        public void drawShowcase(Bitmap buffer, float x, float y, float scaleMultiplier) {
            Canvas bufferCanvas = new Canvas(buffer);
            renderRect.left = x - width / 2f;
            renderRect.right = x + width / 2f;
            renderRect.top = y - height / 2f;
            renderRect.bottom = y + height / 2f;
            bufferCanvas.drawRect(renderRect, eraserPaint);
        }

        @Override
        public int getShowcaseWidth() {
            return (int) width;
        }

        @Override
        public int getShowcaseHeight() {
            return (int) height;
        }

        @Override
        public float getBlockedRadius() {
            return width;
        }

        @Override
        public void setBackgroundColour(int backgroundColor) {
            // No-op, remove this from the API?
        }

        @Override
        public void erase(Bitmap bitmapBuffer) {
            bitmapBuffer.eraseColor(eraseColour);
        }

        @Override
        public void drawToCanvas(Canvas canvas, Bitmap bitmapBuffer) {
            canvas.drawBitmap(bitmapBuffer, 0, 0, basicPaint);
        }

    }

    public ArrayList<User> fakeUsers(){

        User u1 = new User();
        User u2 = new User();
        User u3 = new User();
        User u4 = new User();
        User u5 = new User();
        User u6 = new User();
        User u7 = new User();
        User u8 = new User();

        u1.setName("Alessandro");
        u1.setSurname("Di Stefano");
        u1.setCompetence("Golf");
        u1.setCity("RM");

        u2.setName("Martina");
        u2.setSurname("Stefanino");
        u2.setCompetence("Tennis");
        u2.setCity("TO");

        u3.setName("Luca");;
        u3.setSurname("Balsamo");
        u3.setCompetence("Tennis");
        u3.setCity("RM");

        u4.setName("Romina");
        u4.setSurname("Balsamo");
        u4.setCompetence("Golf");
        u4.setCity("RM");

        u5.setName("alessandro");
        u5.setSurname("di stefano");
        u5.setCompetence("golf");
        u5.setCity("roma");

        u6.setName("martina");
        u6.setSurname("stefanino");
        u6.setCompetence("tennis");
        u6.setCity("torino");

        u7.setName("luca");;
        u7.setSurname("balsamo");
        u7.setCompetence("tennis");
        u7.setCity("roma");

        u8.setName("romina");
        u8.setSurname("balsamo");
        u8.setCompetence("golf");
        u8.setCity("roma");

        ArrayList<User> userArrayList = new ArrayList<>();
        userArrayList.add(u1);
        userArrayList.add(u2);
        userArrayList.add(u3);
        userArrayList.add(u4);
        userArrayList.add(u5);
        userArrayList.add(u6);
        userArrayList.add(u7);
        userArrayList.add(u8);

        return userArrayList;
    }




}

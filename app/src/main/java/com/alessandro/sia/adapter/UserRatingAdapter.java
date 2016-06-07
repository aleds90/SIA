package com.alessandro.sia.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;
import java.util.ArrayList;
import com.alessandro.sia.R;
import com.alessandro.sia.model.Rating;
import com.alessandro.sia.model.User;
import com.alessandro.sia.util.LocalStore;

public class UserRatingAdapter extends ArrayAdapter<User>{

    private ArrayList<User> userArrayList;
    private Context context;


    public UserRatingAdapter(ArrayList<User> userArrayList, Context context){
        super(context, R.layout.adapter_user_rating, userArrayList);
        this.userArrayList = userArrayList;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.adapter_user_rating, parent, false);
        User user = userArrayList.get(position);

        TextView nome = (TextView)view.findViewById(R.id.adp_nome);
        TextView cognome = (TextView)view.findViewById(R.id.adp_cognome);
        TextView role = (TextView)view.findViewById(R.id.adp_role);
        TextView city = (TextView)view.findViewById(R.id.adp_city);

        nome.setText(user.getName());
        cognome.setText(user.getSurname());
        role.setText(user.getCompetence());
        city.setText("("+user.getCity()+")");

        RatingBar ratingBar = (RatingBar)view.findViewById(R.id.user_rating_ratingBar);

        float media = 0;
        int somma = 0;
        ArrayList<Rating> ratings = new ArrayList<>();
        ratings.addAll(user.getRatingsForUserReceiver());
        if (ratings!=null) {
            for (int i = 0; i < ratings.size(); i++) {
                somma += ratings.get(i).getRate();
            }
            if (somma!=0)
                media = somma/ratings.size();
            else{}
        }
        if (media!=0)
            ratingBar.setRating(media);
        System.out.println(media);
        System.out.println(somma);


        return view;
    }
}

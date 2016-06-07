package com.alessandro.sia.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import com.alessandro.sia.R;
import com.alessandro.sia.model.User;

public class UserAdapter extends ArrayAdapter<User>{

    private ArrayList<User> userArrayList;
    private Context context;


    public UserAdapter(ArrayList<User> userArrayList, Context context){
        super(context, R.layout.adapter_user, userArrayList);
        this.userArrayList = userArrayList;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.adapter_user, parent, false);
        User user = userArrayList.get(position);

        TextView nome = (TextView)view.findViewById(R.id.adp_nome);
        TextView cognome = (TextView)view.findViewById(R.id.adp_cognome);
        TextView role = (TextView)view.findViewById(R.id.adp_role);
        TextView city = (TextView)view.findViewById(R.id.adp_city);

        nome.setText(user.getName());
        cognome.setText(user.getSurname());
        role.setText(user.getCompetence());
        city.setText("("+user.getCity()+")");

        return view;
    }
}

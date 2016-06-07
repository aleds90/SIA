package com.alessandro.sia.adapter;


import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.alessandro.sia.R;
import com.alessandro.sia.model.Appointment;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AppointmentAdapter extends ArrayAdapter<Appointment>{

    private ArrayList<Appointment> appointmentArrayList;
    private Context context;

    public AppointmentAdapter(ArrayList<Appointment> appointmentArrayList, Context context){
        super(context, R.layout.adapter_appointment, appointmentArrayList);
        this.appointmentArrayList = appointmentArrayList;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.adapter_appointment, parent, false);
        Appointment appointment = appointmentArrayList.get(position);
        Calendar cal = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ITALY);
        try {
            cal.setTime(sdf.parse(appointment.getDateStart()));// all done
        } catch (ParseException e) {
            e.printStackTrace();
        }

        TextView data = (TextView)view.findViewById(R.id.data);
        data.setText(cal.get(Calendar.DAY_OF_MONTH)+"-"+cal.get(Calendar.MONTH)+"-"+cal.get(Calendar.YEAR));
        TextView competence=(TextView)view.findViewById(R.id.adapter_appointment_competence);
        competence.setText(appointment.getName());
        return view;
    }
}

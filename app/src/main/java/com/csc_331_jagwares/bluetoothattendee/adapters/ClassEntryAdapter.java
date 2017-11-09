package com.csc_331_jagwares.bluetoothattendee.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.csc_331_jagwares.bluetoothattendee.R;
import com.csc_331_jagwares.bluetoothattendee.persistence.model.Class;

import java.util.ArrayList;


public class ClassEntryAdapter extends ArrayAdapter {

    // Holds the list of classes from the database to be added to the ListView.
    private ArrayList<Class> classEntries;


    public ClassEntryAdapter(Context context, ArrayList<Class> classEntries) {
        super(context, 0, classEntries);
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.class_entry, null);

        // Set object values to the ListView layout elements.
        Class classEntry = (Class) getItem(position);
        TextView tvClassName = view.findViewById(R.id.tvClassName);

        if (classEntry != null) {
            tvClassName.setText(classEntry.getClassName()/* + "-" + classEntry.getSection() + " " + classEntry.getTitle()*/);
        }

        return view;
    }

}

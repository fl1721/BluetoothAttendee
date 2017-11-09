package com.csc_331_jagwares.bluetoothattendee.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.csc_331_jagwares.bluetoothattendee.R;
import com.csc_331_jagwares.bluetoothattendee.activities.ClassActivity;
import com.csc_331_jagwares.bluetoothattendee.activities.MainActivity;
import com.csc_331_jagwares.bluetoothattendee.adapters.ClassEntryAdapter;
import com.csc_331_jagwares.bluetoothattendee.persistence.AttendeeDatasource;
import com.csc_331_jagwares.bluetoothattendee.persistence.model.Class;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClassesFragment extends Fragment {

    private AttendeeDatasource datasource;

    private ArrayList<Class> classes;


    public ClassesFragment() {
        // Required empty public constructor.
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Set the main activity title.
        getActivity().setTitle(R.string.nav_classes);
        // Inflate the layout for this fragment.
        View view = inflater.inflate(R.layout.fragment_classes, container, false);

        // Get Datasource object from the MainActivity.
        datasource = ((MainActivity) getActivity()).getDatasource();

        // Get classes from the main activity.
        classes = datasource.getAllClasses();

        // Add the classes from the ArrayList to the ListView.
        if (classes != null) {
            populateListView(view, classes);
        }

        // Listen for a ListView entry selection.
        registerClickCallback(view);

        return view;
    }

    private void populateListView(View view, ArrayList<Class> classes) {
        // Create the adapter to convert the array to views
        ClassEntryAdapter adapter = new ClassEntryAdapter(getContext(), classes);

        // Attach the adapter to a ListView
        ListView lvClassList = view.findViewById(R.id.lvClassList);
        lvClassList.setAdapter(adapter);
    }

    private void registerClickCallback(View view) {
        ListView list = view.findViewById(R.id.lvClassList);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String className = classes.get(position).getClassName();
                Log.d("WHOA 1", className);
                // Start a new ClassActivity based on the class selected.
                Intent intent = new Intent(getActivity(), ClassActivity.class);
                intent.putExtra("className", className);
                startActivity(intent);
            }
        });
    }

}

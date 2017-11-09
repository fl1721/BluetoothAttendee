package com.csc_331_jagwares.bluetoothattendee.fragments;


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
import com.csc_331_jagwares.bluetoothattendee.adapters.StudentEntryAdapter;
import com.csc_331_jagwares.bluetoothattendee.persistence.AttendeeDatasource;
import com.csc_331_jagwares.bluetoothattendee.persistence.model.Class;
import com.csc_331_jagwares.bluetoothattendee.persistence.model.Student;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TakeAttendanceFragment extends Fragment {

    private AttendeeDatasource datasource;

    private View view;

    private String className;
    private Class classEntry;
    private ArrayList<Student> students;

    StudentEntryAdapter adapter;


    public TakeAttendanceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Set the main activity title.
        getActivity().setTitle("Take Attendance");
        // Inflate the layout for this fragment.
        View view = inflater.inflate(R.layout.fragment_take_attendance, container, false);

        // Get Datasource object from the ClassActivity.
        datasource = ((ClassActivity) getActivity()).getDatasource();

        // Get classEntry from ClassFragment.
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            className = bundle.getString("className");
        }
        // Setup Class object.
        classEntry = datasource.getClassByName(className);

        // Get ArrayList of students from the class.
        students = classEntry.getStudents();

        // Add the students from the ArrayList to the ListView.
        if (students != null) {
            populateListView(view, students);
        }

        // Listen for a ListView entry selection.
        registerClickCallback(view);

        return view;
    }

    private void populateListView(View view, ArrayList<Student> students) {
        // Create the adapter to convert the array to views
        adapter = new StudentEntryAdapter(getContext(), students);

        // Attach the adapter to a ListView
        ListView lvStudentList = view.findViewById(R.id.lvStudentList);
        lvStudentList.setAdapter(adapter);
    }

    private void registerClickCallback(View view) {
        ListView list = view.findViewById(R.id.lvStudentList);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Student studentEntry = students.get(position);
            }
        });
    }

}

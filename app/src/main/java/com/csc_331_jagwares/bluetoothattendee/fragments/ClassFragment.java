package com.csc_331_jagwares.bluetoothattendee.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.csc_331_jagwares.bluetoothattendee.R;
import com.csc_331_jagwares.bluetoothattendee.activities.ClassActivity;
import com.csc_331_jagwares.bluetoothattendee.persistence.model.Class;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClassFragment extends Fragment implements View.OnClickListener {

    private Class classEntry;


    public ClassFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment.
        View view = inflater.inflate(R.layout.fragment_class, container, false);

        // Get class from the class activity.
        classEntry = ((ClassActivity) getActivity()).getClassEntry();

        // Set the main activity title.
        getActivity().setTitle(classEntry.getClassName()  /*classEntry.getNumber() + "-" + classEntry.getSection()*/);

        // Populate class details table with class attributes.
        TextView tvClassName = view.findViewById(R.id.tvClassName);
        /*
        TextView tvClassTitle = view.findViewById(R.id.tvClassTitle);
        TextView tvClassCRN = view.findViewById(R.id.tvClassCRN);
        TextView tvClassDays = view.findViewById(R.id.tvClassDays);
        TextView tvClassTime = view.findViewById(R.id.tvClassTime);
        TextView tvClassLocation = view.findViewById(R.id.tvClassLocation);
        TextView tvClassSemester = view.findViewById(R.id.tvClassSemester);
        TextView tvClassNumStudents = view.findViewById(R.id.tvClassNumStudents);
        */
        if (classEntry != null) {
            tvClassName.setText(classEntry.getClassName()/*classEntry.getNumber() + "-" + classEntry.getSection()*/);
         /*   tvClassTitle.setText (classEntry.getTitle());
            tvClassCRN.setText (getString(R.string.frag_crn) + classEntry.getCrn());
            tvClassDays.setText(getString(R.string.frag_days) + classEntry.getDays());
            tvClassLocation.setText(getString(R.string.frag_loc) + classEntry.getLocation());
            tvClassSemester.setText(classEntry.getSemester());
            tvClassTime.setText(classEntry.getTime());
            tvClassNumStudents.setText("Number of Students: "+ classEntry.getStudents().size());
        */
        }

        // Setup action buttons.
        Button rosterBtn = view.findViewById(R.id.rosterBtn);
        Button attendanceBtn = view.findViewById(R.id.attendanceBtn);
        Button registerBtn = view.findViewById(R.id.registerBtn);

        rosterBtn.setOnClickListener(this);
        attendanceBtn.setOnClickListener(this);
        registerBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        // Handle button clicks.
        String className = classEntry.getClassName();
        Fragment fragment = null;

        switch (view.getId()) {
            case R.id.rosterBtn:
                fragment = new ClassRosterFragment();
                break;
            case R.id.attendanceBtn:
                fragment = new TakeAttendanceFragment();
                break;
            case R.id.registerBtn:
                fragment = new RegisterDevicesFragment();
                break;
        }

        // Switch to the selected fragment.
        Bundle bundle = new Bundle();
        bundle.putString("className", className);

        if (fragment != null) {
            fragment.setArguments(bundle);
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainLayout, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }
    }

}

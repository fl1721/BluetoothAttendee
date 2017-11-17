package com.csc_331_jagwares.bluetoothattendee.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.csc_331_jagwares.bluetoothattendee.R;
import com.csc_331_jagwares.bluetoothattendee.persistence.Student;

import java.util.ArrayList;

/**
 * Created by fzlor on 11/13/2017.
 */

public class AttendanceAdapter extends ArrayAdapter {
    private ArrayList<Student> studentEntries;

    public AttendanceAdapter(Context context, ArrayList<Student> studentEntries) {
        super(context, 0, studentEntries);
        this.studentEntries = studentEntries;
}
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.student_attendance, null);

        // Set object values to the ListView layout elements.
        Student studentEntry = (Student) getItem(position);
        TextView tvStudentName = view.findViewById(R.id.tvStudentName);
        TextView tvStudentId = view.findViewById(R.id.tvStudentId);
        CheckBox cbAttendance = view.findViewById(R.id.cbAttend);

        if (studentEntry != null) {
            tvStudentName.setText(studentEntry.getFirstName() + " " + studentEntry.getLastName());
            tvStudentId.setText(studentEntry.getJagNumber());
            if (studentEntry.getMacAddress() != null) {
                view.setBackgroundResource(R.color.colorRegistered);
                cbAttendance.setChecked(true);
            }
            else{
                cbAttendance.setChecked(false);
            }
        }

        return view;
    }

    public ArrayList<Student> getData() {
        return studentEntries;
    }

}
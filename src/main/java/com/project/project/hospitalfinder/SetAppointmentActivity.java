package com.project.project.hospitalfinder;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.project.project.R;
import com.project.project.Utils;
import com.project.project.acommon.LoginActivity;
import com.project.project.hospitalfinder.data.Appointments;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SetAppointmentActivity extends AppCompatActivity {


    private final String TAG = this.getClass().getSimpleName();

    @BindView(R.id.details)
    TextView tvDetails;

    @BindView(R.id.button1)
    Button btnSetDate;

    @BindView(R.id.button2)
    Button btnSetTime;

    @BindView(R.id.button3)
    Button btnSubmit;

    @BindView(R.id.spinner)
    Spinner spinner;

    String selectedDate = "";
    String selectedTime = "", selectedSpeciality;

    String[] specialities = {"Cardiologist", "Orthopeadician", "General Physician", "Biologist", "Gynaecologist"};
    private Unbinder unbinderknife;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_hospitalfinder_setappointment);

        unbinderknife = ButterKnife.bind(this);

        String details= getIntent().getStringExtra("details");

        tvDetails.setText(details);

        Utils.showToast(getApplicationContext(), details);

        ArrayAdapter<String> aa = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, specialities);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedSpeciality = specialities[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnSetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePickerDialog();
            }
        });

        btnSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTimePickerDialog(false);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseObject parseObject = new ParseObject(Appointments.class.getSimpleName());
                parseObject.put(Appointments.hospitalName, details);
                parseObject.put(Appointments.date, selectedDate);
                parseObject.put(Appointments.time, selectedTime);
                parseObject.put(Appointments.username, LoginActivity.username);
                parseObject.put(Appointments.specialist, selectedSpeciality);


                parseObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Utils.showToast(getApplicationContext(), "Appointment booked successfully");
                            finish();
                        } else {
                            e.printStackTrace();
                            Utils.showToast(getApplicationContext(), "User add error: " + e.getMessage());
                        }
                    }
                });
            }
        });
    }

    int year, month, day;

    private void openDatePickerDialog(){
        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(this, datePickerListener,
                year, month, day).show();
    }

    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            Log.i(TAG, "Date: " + day + "-" + month + "-" + year);
            btnSetDate.setText(new StringBuilder().append(day).append("-").append(month + 1)
                    .append("-").append(year)
                    .append(" "));

            selectedDate = day + "/" + (month+1) + "/" + year;


        }
    };


    private void openTimePickerDialog(boolean is24r) {
        Calendar calendar = Calendar.getInstance();

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                onTimeSetListener, calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), is24r);
        timePickerDialog.setTitle("Set Alarm Time");
        timePickerDialog.show();
    }

    TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            btnSetTime.setText(hourOfDay + ":" + minute);
            selectedTime = hourOfDay + ":" + minute;
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinderknife.unbind();

    }

}

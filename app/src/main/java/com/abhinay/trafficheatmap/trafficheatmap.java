package com.abhinay.trafficheatmap;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class trafficheatmap extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    private int day, month, year, hour, minute;
    private int myday, myMonth, myYear, myHour, myMinute;
    private Button settime,viewvolume,viewspeed;
    private EditText date,time,locid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trafficheatmap);

        settime = findViewById(R.id.settimeanddate);
        viewspeed = findViewById(R.id.viewspeed);
        viewvolume = findViewById(R.id.viewvolume);
        date = findViewById(R.id.typedate);
        time = findViewById(R.id.typetime);
        locid = findViewById(R.id.idlocid);

        date.setText("1-10-2021");
        time.setText("13-0");
        settime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(trafficheatmap.this, trafficheatmap.this,year, month,day);
                datePickerDialog.show();
            }
        });
        viewvolume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String a=date.getText().toString();
                String b=time.getText().toString();
                String c=locid.getText().toString();
                if(a.isEmpty()||b.isEmpty()||c.isEmpty())
                {
                    Toast.makeText(trafficheatmap.this, "Enter every field of date and time !", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent = new Intent(getApplicationContext(), viewvolumetrictraffic.class);
                    intent.putExtra("date",a);
                    intent.putExtra("time",b);
                    intent.putExtra("locid",c);
                    startActivity(intent);
                }
            }
        });
        viewspeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String a=date.getText().toString();
                String b=time.getText().toString();
                String c=locid.getText().toString();
                if(a.isEmpty()||b.isEmpty()||c.isEmpty())
                {
                    Toast.makeText(trafficheatmap.this, "Enter every field of date and time !", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent = new Intent(getApplicationContext(), viewspeedtraffic.class);
                    intent.putExtra("date",a);
                    intent.putExtra("time",b);
                    intent.putExtra("locid",c);
                    startActivity(intent);
                }
            }
        });
    }
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        myYear = year;
        myday = dayOfMonth;
        myMonth = month;
        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR);
        minute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(trafficheatmap.this, trafficheatmap.this, hour, minute, DateFormat.is24HourFormat(this));
        timePickerDialog.show();
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        myHour = hourOfDay;
        myMinute = minute;
        date.setText(myday + "-" + myMonth + "-" + myYear);
        time.setText(myHour+"-"+myMinute);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
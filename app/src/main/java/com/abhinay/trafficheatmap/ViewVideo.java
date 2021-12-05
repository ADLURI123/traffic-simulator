package com.abhinay.trafficheatmap;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class ViewVideo extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private EditText locid,locname,date,time;
    private Button searchloc,settime,searchvid;
    private ProgressDialog progressDialog;
    private Uri videouri;
    private String a,b,c;
    private VideoView vidview;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private int day, month, year, hour, minute;
    private int myday, myMonth, myYear, myHour, myMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_video);
        getSupportActionBar().setTitle("View Traffic");

        locid = findViewById(R.id.locid);
        locname = findViewById(R.id.locname);
        date = findViewById(R.id.txtdate);
        time = findViewById(R.id.txttime);
        searchloc = findViewById(R.id.searchbtn);
        settime = findViewById(R.id.setdateandtime);
        searchvid = findViewById(R.id.searchvideo);
        vidview = findViewById(R.id.vview);
        date.setText("1-10-2021");
        time.setText("13-0");
        locid.setText("1");

        firebaseDatabase = FirebaseDatabase.getInstance();

        settime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(ViewVideo.this, ViewVideo.this,year, month,day);
                datePickerDialog.show();
            }
        });
        searchvid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                try
                {
                    a=locid.getText().toString();
                    b=date.getText().toString();
                    c=time.getText().toString();
                    if(a.isEmpty()||b.isEmpty()||c.isEmpty())
                    {
                        return ;
                    }
                    String str = "https://firebasestorage.googleapis.com/v0/b/trafficheatmap-329015.appspot.com/o/Recordings%2F"+a+"%2F"+b+"%2F"+c+".mp4?alt=media&token=cf969b9f-2940-4882-9fb6-2150d6421c52";
                    videouri = Uri.parse(str);

                    vidview.setVideoURI(videouri);
                    MediaController mediaController = new MediaController(ViewVideo.this);
                    mediaController.setAnchorView(vidview);
                    mediaController.setMediaPlayer(vidview);
                    vidview.setMediaController(mediaController);
                    vidview.requestFocus();
                    vidview.start();
                    databaseReference = firebaseDatabase.getReference(a+"-"+b+"-"+c+"/left count");
                    getdata();
                }
                catch(Exception e)
                {
                    return ;
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
        TimePickerDialog timePickerDialog = new TimePickerDialog(ViewVideo.this, ViewVideo.this, hour, minute, DateFormat.is24HourFormat(this));
        timePickerDialog.show();
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        myHour = hourOfDay;
        myMinute = minute;
        date.setText(myday + "-" + myMonth + "-" + myYear);
        time.setText(myHour+"-"+myMinute);
    }

    private void getdata() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = Integer.toString(snapshot.getValue(Integer.class));
                locname.setText(value);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewVideo.this, error +"Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
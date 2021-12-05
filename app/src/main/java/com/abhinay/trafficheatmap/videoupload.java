package com.abhinay.trafficheatmap;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.util.Calendar;
import java.util.HashMap;

public class videoupload extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private Button selectv,uploadv,settime;
    private ProgressDialog progressDialog;
    private Uri videouri;
    private EditText edtdate,edttime,locid;
    private String a,b,c;

    private int day, month, year, hour, minute;
    private int myday, myMonth, myYear, myHour, myMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoupload);
        getSupportActionBar().setTitle("Upload Traffic Video");

        selectv = findViewById(R.id.SelectVideo);
        uploadv = findViewById(R.id.AddVideo);
        settime = findViewById(R.id.settime);
        edtdate = findViewById(R.id.Date);
        edttime = findViewById(R.id.Time);
        locid = findViewById(R.id.LocationID);

        selectv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                progressDialog = new ProgressDialog(videoupload.this);
                choosevideo();
            }
        });
        uploadv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                a=locid.getText().toString();
                b=edtdate.getText().toString();
                c=edttime.getText().toString();
                if(a.isEmpty()||b.isEmpty()||c.isEmpty())
                {
                    Toast.makeText(videoupload.this, "Enter every field of location id , date and time !", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    progressDialog.setTitle("Uploading...");
                    progressDialog.show();
                    uploadvideo();
                }
            }
        });
        settime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(videoupload.this, videoupload.this,year, month,day);
                datePickerDialog.show();
            }
        });
    }


    private void choosevideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 5);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 5 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            videouri = data.getData();
            VideoView videoView =(VideoView)findViewById(R.id.videoView);
            MediaController mediaController= new MediaController(this);
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);
            videoView.setVideoURI(videouri);
            videoView.requestFocus();
            videoView.start();
        }
    }

    private String getfiletype(Uri videouri) {
        ContentResolver r = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(r.getType(videouri));
    }

    private void uploadvideo() {
        if (videouri != null) {
            try
            {
                a=locid.getText().toString();
                b=edtdate.getText().toString();
                c=edttime.getText().toString();
                if(a.isEmpty()||b.isEmpty()||c.isEmpty())
                {
                    Toast.makeText(videoupload.this, "Enter every field of location id , date and time !", Toast.LENGTH_SHORT).show();
                    return ;
                }
                final StorageReference reference = FirebaseStorage.getInstance().getReference("Recordings/"+a+"/" + b+"/" +c+ "." + getfiletype(videouri));
                reference.putFile(videouri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful()) ;
                        String downloadUri = uriTask.getResult().toString();
                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Video");
                        HashMap<String, String> map = new HashMap<>();
                        map.put("videolink", downloadUri);
                        reference1.child("" + System.currentTimeMillis()).setValue(map);
                        progressDialog.dismiss();
                        Toast.makeText(videoupload.this, "Video Uploaded!!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        progressDialog.dismiss();
                        Toast.makeText(videoupload.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>()
                {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot)
                    {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        progressDialog.setMessage("Uploaded " + (int) progress + "%");
                    }
                });
            }
            catch (Exception e)
            {
            }
            return ;
        }
    }
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        myYear = year;
        myday = dayOfMonth;
        myMonth = month;
        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR);
        minute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(videoupload.this, videoupload.this, hour, minute, DateFormat.is24HourFormat(this));
        timePickerDialog.show();
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        myHour = hourOfDay;
        myMinute = minute;
        edtdate.setText(myday + "-" + myMonth + "-" + myYear);
        edttime.setText(myHour+"-"+myMinute);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}

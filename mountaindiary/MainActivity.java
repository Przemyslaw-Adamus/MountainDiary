package com.example.mountaindiary;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {
    private Button buttonTapToScreen;
    private GlobalData gd;
    private static final String[] PERMISIONS = {
            Manifest.permission.BODY_SENSORS,
            Manifest.permission.VIBRATE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.SEND_SMS,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        veryfiPermisions();
        gd = new GlobalData(this);
        gd.create(this);
        buttonTapToScreen = findViewById(R.id.buttonTapToScrees);
        buttonTapToScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MountainDiaryActivity.class);
                startActivity(intent);
                finish();
            }
        });
        buttonTapToScreen.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(MainActivity.this, EasterEgg.class);
                startActivity(intent);
                return false;
            }
        });
    }

    private void veryfiPermisions() {
        int permisionsBodySensor = ActivityCompat.checkSelfPermission(this, Manifest.permission.BODY_SENSORS);
        int permisionsVibrate = ActivityCompat.checkSelfPermission(this,Manifest.permission.VIBRATE);
        int permisionsSendSMS = ActivityCompat.checkSelfPermission(this,Manifest.permission.SEND_SMS);
        int permisionsCamera = ActivityCompat.checkSelfPermission(this,Manifest.permission.CAMERA);
        int permisionsLocation = ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION);
        int permisionsCall = ActivityCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE);
        int permisionsWrite = ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE);


        if(permisionsBodySensor != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,PERMISIONS,1);
        }
        if(permisionsVibrate != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,PERMISIONS,1);
        }
        if(permisionsSendSMS != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,PERMISIONS,1);
        }
        if(permisionsCamera != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,PERMISIONS,1);
        }
        if(permisionsLocation != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,PERMISIONS,1);
        }
        if(permisionsCall != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,PERMISIONS,1);
        }
        if(permisionsWrite != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,PERMISIONS,1);
        }
    }
}

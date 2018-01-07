package com.rootonelabs.vicky.youractivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.DetectedActivity;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    BroadcastReceiver broadcastReceiver;

    private TextView txtActvity, txtConfidence;
    private ImageView imgActivity;
    private Button btnStartTracking, btnStopTracking;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtActvity = findViewById(R.id.txt_activity);
        txtConfidence = findViewById(R.id.txt_confidence);
        imgActivity = findViewById(R.id.img_activity);
        btnStartTracking = findViewById(R.id.bt_start_tracking);
        btnStopTracking = findViewById(R.id.btn_stop_tracking);

        btnStartTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTracking();
            }
        });

        btnStopTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopTracking();
            }
        });

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(Constants.BROAADCAST_DETECTED_ACTIVITY)){
                    int type = intent.getIntExtra("type", -1);
                    int confidence = intent.getIntExtra("confidence", 0);
                    handleUserActivity(type, confidence);
                }
            }
        };
        startTracking();
    }

    private void handleUserActivity(int type, int confidence) {
        String label = "STILL";
        int icon = R.drawable.ic_still;

        switch (type) {
            case DetectedActivity.IN_VEHICLE: {
                label = "IN VEHICLE";
                icon = R.drawable.ic_driving;
                break;
            }
            case DetectedActivity.ON_BICYCLE: {
                label = "ON BICYCLE";
                icon = R.drawable.ic_on_bicycle;
                break;
            }
            case DetectedActivity.ON_FOOT: {
                label = "ON FOOT";
                icon = R.drawable.ic_walking;
                break;
            }
            case DetectedActivity.RUNNING: {
                label = "RUNNING";
                icon = R.drawable.ic_running;
                break;
            }
            case DetectedActivity.STILL: {
                label = "STILL";
                break;
            }
            case DetectedActivity.TILTING: {
                label = "TILTING";
                icon = R.drawable.ic_tilting;
                break;
            }
            case DetectedActivity.WALKING: {
                label = "WALKING";
                icon = R.drawable.ic_walking;
                break;
            }
            case DetectedActivity.UNKNOWN: {
                label = "UNKNOWN";
                break;
            }
        }

        Log.e(TAG, "User Activity: " + ", Confidence: " + confidence);

        if (confidence > Constants.CONFIDENCE) {
            txtActvity.setText(label);
            txtConfidence.setText("Confidence: " + confidence);
            imgActivity.setImageResource(icon);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(Constants.BROAADCAST_DETECTED_ACTIVITY));
    }

    @Override
    protected void onPause(){
        super.onPause();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    private void startTracking(){
        Intent intent1 = new Intent(MainActivity.this, BackgroundDetectedActivitiesService.class);
        startService(intent1);
    }

    private void stopTracking(){
        Intent intent = new Intent(MainActivity.this, BackgroundDetectedActivitiesService.class);
        stopService(intent);
    }

}

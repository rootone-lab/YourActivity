package com.rootonelabs.vicky.youractivity;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

/**
 * Created by Vicky on 12/23/2017.
 */

public class BackgroundDetectedActivitiesService extends Service {
    private static final String TAG = BackgroundDetectedActivitiesService.class.getSimpleName();

    private Intent mIntentService;
    private PendingIntent mPendingIntent;
    private ActivityRecognitionClient mActivityRecognitionClient;

    IBinder mBinder = (IBinder) new BackgroundDetectedActivitiesService();

    public class LocalBinder extends Binder {
        public BackgroundDetectedActivitiesService getServerInstance(){
            return  BackgroundDetectedActivitiesService.this;
        }
    }
    public BackgroundDetectedActivitiesService(){

    }

    @Override
    public void onCreate() {
        super.onCreate();
        mActivityRecognitionClient = new ActivityRecognitionClient(this);
        mIntentService = new Intent(this, DetectedActivitiesIntentService.class);
        mPendingIntent = PendingIntent.getService(this, 1, mIntentService, PendingIntent.FLAG_UPDATE_CURRENT);
        requestActivityUpdatesButtonHandler();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    private void requestActivityUpdatesButtonHandler() {
        Task<Void> task = mActivityRecognitionClient.requestActivityUpdates(Constants.DETECTION_INTERVAL_IN_MILLISECONDS, mPendingIntent);

        task.addOnSuccessListener(new OnSuccessListener<Void>(){
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(),"Successfully requested activity updated",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void removeActivityUpdatesButtonHandler() {
        Task<Void> task = mActivityRecognitionClient.removeActivityUpdates(mPendingIntent);
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Successfully Removed The Activity", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        removeActivityUpdatesButtonHandler();
    }

}

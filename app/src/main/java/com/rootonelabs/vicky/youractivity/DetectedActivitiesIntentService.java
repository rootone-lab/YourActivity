package com.rootonelabs.vicky.youractivity;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by Vicky on 12/23/2017.
 */

public class DetectedActivitiesIntentService extends IntentService{


    public DetectedActivitiesIntentService() {
        super(TAG);
    }
    @SuppressWarnings("unchecked")
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);

        ArrayList<DetectedActivity> detectedActivies = (ArrayList)result.getProbableActivities();

        for (DetectedActivity activity: detectedActivies){
            Log.e(TAG, "Detected Activity: " + activity.getType() + ", " + activity.getConfidence());
            broadcastActivity(activity);
        }
    }

    private void broadcastActivity(DetectedActivity activity) {
        Intent intent = new Intent(Constants.BROAADCAST_DETECTED_ACTIVITY);
        intent.putExtra("type", activity.getType());
        intent.putExtra("confidence", activity.getConfidence());
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}

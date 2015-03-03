package com.ups.weatheralarm.weatheractivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.ups.weatheralarm.HomeActivity;

import za.co.neilson.alarm.R;

public class NoiseActivity extends Activity {
    /* constants */
    private static final int POLL_INTERVAL = 300;

    /**
     * running state *
     */
    private boolean mRunning = false;

    /**
     * config state *
     */
    private int mThreshold;

    private PowerManager.WakeLock mWakeLock;

    private Handler mHandler = new Handler();

    /* References to view elements */
    private TextView mStatusView;
    private SoundLevelView mDisplay;

    /* sound data source */
    private SoundMeter mSensor;

    final Handler handler = new Handler();

    /**
     * *************** Define runnable thread again and again, detect noise ********
     */

    private Runnable mSleepTask = new Runnable() {
        public void run() {
            //Log.i("Noise", "runnable mSleepTask");

            start();
        }
    };

    // Create runnable thread to Monitor Voice
    private Runnable mPollTask = new Runnable() {
        public void run() {

            double amp = mSensor.getAmplitude();
            //Log.i("Noise", "runnable mPollTask");
            updateDisplay("Monitoring Voice...", amp);

            if ((amp > mThreshold)) {
                callForHelp();
                //Log.i("Noise", "==== onCreate ===");

            }

            // Runnable(mPollTask) will again execute after POLL_INTERVAL
            mHandler.postDelayed(mPollTask, POLL_INTERVAL);

        }
    };


    //Called when the activity is first created.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Defined SoundLevelView in main.xml file
        setContentView(R.layout.activity_noise);
        mStatusView = (TextView) findViewById(R.id.status);

        // Used to record voice
        mSensor = new SoundMeter();
        mDisplay = (SoundLevelView) findViewById(R.id.volume);

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "NoiseActivity");
    }


    @Override
    public void onResume() {
        super.onResume();
        //Log.i("Noise", "==== onResume ===");

        initializeApplicationConstants();
        mDisplay.setLevel(0, mThreshold);

        if (!mRunning) {
            mRunning = true;
            start();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        // Log.i("Noise", "==== onStop ===");

        //Stop noise monitoring
        stop();

    }

    private void start() {
        //Log.i("Noise", "==== start ===");

        mSensor.start();
        if (!mWakeLock.isHeld()) {
            mWakeLock.acquire();
        }

        //Noise monitoring start
        // Runnable(mPollTask) will execute after POLL_INTERVAL
        mHandler.postDelayed(mPollTask, POLL_INTERVAL);
    }

    private void stop() {
        Log.i("Noise", "==== Stop Noise Monitoring===");
        if (mWakeLock.isHeld()) {
            mWakeLock.release();
        }
        mHandler.removeCallbacks(mSleepTask);
        mHandler.removeCallbacks(mPollTask);
        mSensor.stop();
        mDisplay.setLevel(0, 0);
        updateDisplay("stopped...", 0.0);
        mRunning = false;

    }


    private void initializeApplicationConstants() {
        // Set Noise Threshold
        mThreshold = 10;  // orignal: 8

    }

    private void updateDisplay(String status, double signalEMA) {
        mStatusView.setText(status);
        mDisplay.setLevel((int) signalEMA, mThreshold);
    }


    private void callForHelp() {

        //stop();
        // Show alert when noise thersold crossed
        Toast.makeText(getApplicationContext(), "Noise Test Passed, Congratulations!",
                Toast.LENGTH_LONG).show();
        setContentView(R.layout.blowcloudy); // change view to

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                Intent intent = new Intent();
                intent.setClass(NoiseActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
                //setContentView(R.layout.activity_home);
            }
        }, 3000);
    }


//    // MIC validation process
//    private void validateMicAvailability() throws MicUnaccessibleException {
//        AudioRecord recorder =
//                new AudioRecord(MediaRecorder.AudioSource.MIC, 44100,
//                        AudioFormat.CHANNEL_IN_MONO,
//                        AudioFormat.ENCODING_DEFAULT, 44100);
//        try{
//            if(recorder.getRecordingState() != AudioRecord.RECORDSTATE_STOPPED ){
//                throw new MicUnaccessibleException("Mic didn't successfully initialized");
//            }
//
//            recorder.startRecording();
//            if(recorder.getRecordingState() != AudioRecord.RECORDSTATE_RECORDING){
//                recorder.stop();
//                throw new MicUnaccessibleException("Mic is in use and can't be accessed");
//            }
//            recorder.stop();
//        } finally{
//            recorder.release();
//            recorder = null;
//        }
//    }

};

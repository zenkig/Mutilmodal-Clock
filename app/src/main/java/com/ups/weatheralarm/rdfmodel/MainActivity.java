package com.ups.weatheralarm.rdfmodel;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.ups.weatheralarm.weatheractivity.NoiseActivity;
import com.ups.weatheralarm.weatheractivity.ScrathActivity;
import com.ups.weatheralarm.weatheractivity.ShakeActivity;
import com.ups.weatheralarm.weatheractivity.SpeechActivity;

import java.io.IOException;
import java.util.ArrayList;

import za.co.neilson.alarm.R;

public class MainActivity extends Activity {
    private Vibrator vibrator;


    protected RDFModel rdf;
    protected Spinner userprofile_spinner, difficulty_spinner, weather_spinner;
    protected Boolean[] inputModality = {false, false, false, false, false};//Speak, Click, Blow, Wipe, Shake
    protected Boolean[] outputModality = { false, false, false, false};// Visual, Sound, Vibrate, Flash
    Modality m = new Modality();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = (Button) findViewById(R.id.button1);
        Button action = (Button) findViewById(R.id.action);

        userprofile_spinner = (Spinner) findViewById(R.id.UserProfile);
        difficulty_spinner = (Spinner) findViewById(R.id.Difficulty);
        weather_spinner = (Spinner) findViewById(R.id.Weather);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.userprofile, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.difficulty, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.weather, android.R.layout.simple_spinner_item);

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        userprofile_spinner.setAdapter(adapter1);
        difficulty_spinner.setAdapter(adapter2);
        weather_spinner.setAdapter(adapter3);

        try {
            this.rdf = new RDFModel(this);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }


        action.setOnClickListener(new OnClickListener(){

            public void onClick(View v) {
                // TODO Auto-generated method stub

                String attr1 = userprofile_spinner.getSelectedItem().toString();
                String attr2 = difficulty_spinner.getSelectedItem().toString();
                String attr3 = weather_spinner.getSelectedItem().toString();

                Log.i("rescue: mode", attr1 + " " + attr2 + " " + attr3);

                UserProfile userProfile = new UserProfile("http://imi.org/" + attr1, attr1);
                DifficultyRDF difficulty = new DifficultyRDF("http://imi.org/" + attr2, attr2);
                Weather weather = new Weather("http://imi.org/" + attr3, attr3);

                rdf.getModality(userProfile, difficulty, weather, m);

                ArrayList<String> inList = m.getInputModality();
                ArrayList<String> outList = m.getOutputModality();

                //Log.i("rescue", "final in:" + in + "; out: " + out);

                Toast.makeText(getApplicationContext(), "InputModality:" + inList + " OutputModality:" + outList, Toast.LENGTH_SHORT).show();

                for(int num=0; num<inList.size(); num++)
                {
                    if (inList.contains("Speak")) {
                        inputModality[0] = true;//Speak, Click, Blow, Wipe, Shake
                    }
                    if (inList.contains("Click")) {
                        inputModality[1] = true;//Speak, Click, Blow, Wipe, Shake
                    }
                    if (inList.contains("Blow")) {
                        inputModality[2] = true;//Speak, Click, Blow, Wipe, Shake
                    }
                    if (inList.contains("Wipe")) {
                        inputModality[3] = true;////Speak, Click, Blow, Wipe, Shake
                    }
                    if (inList.contains("Shake")) {
                        inputModality[4] = true;//Speak, Click, Blow, Wipe, Shake
                    }
                }
                for(int num=0; num<outList.size(); num++) {
                    if (outList.contains("Visual")) {
                        outputModality[0] = true;//Visual, Sound, Vibrate, Flash
                    }
                    if (outList.contains("Sound")) {
                        outputModality[1] = true;//Visual, Sound, Vibrate, Flash
                    }
                    if (outList.contains("Vibrate")) {
                        outputModality[2] = true;//Visual, Sound, Vibrate, Flash
                    }
                    if (outList.contains("Flash")) {
                        outputModality[3] = true;//Visual, Sound, Vibrate, Flash
                    }
                }
                System.out.println(inputModality[0]);
                System.out.println(inputModality[1]);
                System.out.println(inputModality[2]);
                System.out.println(inputModality[3]);
                System.out.println(inputModality[4]);

                System.out.println("OUTPUT  " + outputModality[0]);
                System.out.println("OUTPUT  " + outputModality[1]);
                System.out.println("OUTPUT  " + outputModality[2]);
                System.out.println("OUTPUT  " + outputModality[3]);

                if(inputModality[0]){
                    showOutput();
                    System.out.println("0");
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, SpeechActivity.class);
                    startActivity(intent);
                    finish();
                    //setContentView(R.layout.activity_speech);

                }
                else if(inputModality[1]){
                    showOutput();
                    System.out.println("1");
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, ShakeActivity.class);
                    startActivity(intent);
                    finish();

//                    setContentView(R.layout.activity_shake);
                }
                else if(inputModality[2]){
                    showOutput();
                    System.out.println("2");
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, NoiseActivity.class);
                    startActivity(intent);
                    finish();

//                    setContentView(R.layout.activity_noise);
                }
                else if(inputModality[3]){
                    showOutput();
                    System.out.println("3");
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, ScrathActivity.class);
                    startActivity(intent);
                    finish();
//                    setContentView(R.layout.scrath_activity);
                }
                else if(inputModality[4]){
                    showOutput();
                    System.out.println("4");
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, ShakeActivity.class);
                    startActivity(intent);
                    finish();
//                    setContentView(R.layout.activity_shake);
                }
            }
        });

        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                String attr1 = userprofile_spinner.getSelectedItem().toString();
                String attr2 = difficulty_spinner.getSelectedItem().toString();
                String attr3 = weather_spinner.getSelectedItem().toString();

                Log.i("rescue: mode", attr1 + " " + attr2 + " " + attr3);

                UserProfile userProfile = new UserProfile("http://imi.org/" + attr1, attr1);
                DifficultyRDF difficulty = new DifficultyRDF("http://imi.org/" + attr2, attr2);
                Weather weather = new Weather("http://imi.org/" + attr3, attr3);

                rdf.getModality(userProfile, difficulty, weather, m);

                ArrayList<String> inList = m.getInputModality();
                ArrayList<String> outList = m.getOutputModality();

                //Log.i("rescue", "final in:" + in + "; out: " + out);

                Toast.makeText(getApplicationContext(), "InputModality:" + inList + " OutputModality:" + outList, Toast.LENGTH_SHORT).show();

                for(int num=0; num<inList.size(); num++)
                {
                    if (inList.contains("Speak")) {
                        inputModality[0] = true;//Speak, Click, Blow, Wipe, Shake
                    } else if (inList.contains("Click")) {
                        inputModality[1] = true;//Speak, Click, Blow, Wipe, Shake
                    } else if (inList.contains("Blow")) {
                        inputModality[2] = true;//Speak, Click, Blow, Wipe, Shake
                    } else if (inList.contains("Wipe")) {
                        inputModality[3] = true;////Speak, Click, Blow, Wipe, Shake
                    } else if (inList.contains("Shake")) {
                        inputModality[4] = true;//Speak, Click, Blow, Wipe, Shake
                    }
                }
                for(int num=0; num<outList.size(); num++) {
                    if (outList.contains("Visual")) {
                        outputModality[0] = true;//Visual, Sound, Vibrate, Flash
                    }
                    if (outList.contains("Sound")) {
                        outputModality[1] = true;//Visual, Sound, Vibrate, Flash
                    }
                    if (outList.contains("Vibrate")) {
                        outputModality[2] = true;//Visual, Sound, Vibrate, Flash
                    }
                    if (outList.contains("Flash")) {
                        outputModality[3] = true;//Visual, Sound, Vibrate, Flash
                    }
                }
                System.out.println(inputModality[0]);
                System.out.println(inputModality[1]);
                System.out.println(inputModality[2]);
                System.out.println(inputModality[3]);
                System.out.println(inputModality[4]);

                System.out.println("OUTPUT  " + outputModality[0]);
                System.out.println("OUTPUT  " + outputModality[1]);
                System.out.println("OUTPUT  " + outputModality[2]);
                System.out.println("OUTPUT  " + outputModality[3]);

                //Log.i("rescue", "Speak: " + modalities[0] + "; Click: " +modalities[1] + "; Blow: " +modalities[2] + "; Wipe: " +modalities[3] + "; Shake: " +modalities[4] + "; Visual: " +modalities[5] + "; Sound: " +modalities[6] + "; Vibrate: " +modalities[7]+ "; Flash: " +modalities[8]);
            }
        });


    }
    public void showOutput(){
        //Visual, Sound, Vibrate, Flash
        System.out.println("OUTPUT STARTS" + outputModality[0]);
        System.out.println("OUTPUT STARTS" + outputModality[1]);
        System.out.println("OUTPUT STARTS" + outputModality[2]);
        System.out.println("OUTPUT STARTS" + outputModality[3]);
        if(outputModality[0]){

        }
        if(outputModality[1]){
            System.out.println("OUTPUT 1");
            // TODO Sound
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        }
        if(outputModality[2]){
            System.out.println("OUTPUT 2");
            // TODO Vibrate
            vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
            long [] pattern = {100,1000,100,1000};   // 停止 开启 停止 开启
            vibrator.vibrate(pattern, -1);           //重复两次上面的pattern 如果只想震动一次，index设为-1
        }
        if(outputModality[3]){
//            Camera cam = Camera.open();
//            Camera.Parameters p = cam.getParameters();
//            p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
//            cam.setParameters(p);
//            cam.startPreview();
        }
    }
}

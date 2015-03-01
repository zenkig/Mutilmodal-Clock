package za.co.neilson.alarm.rdfmodel;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;

import za.co.neilson.alarm.R;

public class MainActivity extends Activity {
    protected RDFModel rdf;
    protected Spinner userprofile_spinner, difficulty_spinner, weather_spinner;
    protected Boolean[] modalities = {false, false, false, false, false, false, false};//Voice, Gesture, Shake, Visual, Sound, Vibrate, Flash
    Modality m = new Modality();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = (Button) findViewById(R.id.button1);
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

        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                String attr1 = userprofile_spinner.getSelectedItem().toString();
                String attr2 = difficulty_spinner.getSelectedItem().toString();
                String attr3 = weather_spinner.getSelectedItem().toString();

                //Log.i("rescue: mode", attr1 + " " + attr2 + " " + attr3);

                UserProfile userProfile = new UserProfile("http://imi.org/" + attr1, attr1);
                Difficulty difficulty = new Difficulty("http://imi.org/" + attr2, attr2);
                Weather weather = new Weather("http://imi.org/" + attr3, attr3);

                rdf.getModality(userProfile, difficulty, weather, m);

                String in = m.getInputModality();
                String out = m.getOutputModality();

                //Log.i("rescue", "final in:" + in + "; out: " + out);

                Toast.makeText(getApplicationContext(), "InputModality:" + in + " OutputModality:" + out, Toast.LENGTH_SHORT).show();

                /*
                if (in.equals("VoiceModality")) {
                    modalities[0] = true;//Voice, Gesture, Shake, Visual, Sound, Vibrate, Flash
                } else if (in.equals("GestureModality")) {
                    modalities[1] = true;//Voice, Gesture, Shake, Visual, Sound, Vibrate, Flash
                } else if (in.equals("ShakeModality")) {
                    modalities[2] = true;//Voice, Gesture, Shake, Visual, Sound, Vibrate, Flash
                }*/

                if (in.equals("Voice")) {
                    modalities[0] = true;//Voice, Gesture, Shake, Visual, Sound, Vibrate, Flash
                } else if (in.equals("Gesture")) {
                    modalities[1] = true;//Voice, Gesture, Shake, Visual, Sound, Vibrate, Flash
                } else if (in.equals("Shake")) {
                    modalities[2] = true;//Voice, Gesture, Shake, Visual, Sound, Vibrate, Flash
                }

                /*
                if (out.equals("VisualModality")) {
                    modalities[3] = true;//Voice, Gesture, Shake, Visual, Sound, Vibrate, Flash
                } else if (out.equals("SoundModality")) {
                    modalities[4] = true;//Voice, Gesture, Shake, Visual, Sound, Vibrate, Flash
                } else if (out.equals("VibrateModality")) {
                    modalities[5] = true;//Voice, Gesture, Shake, Visual, Sound, Vibrate, Flash
                } else if (out.equals("FlashModality")) {
                    modalities[6] = true;//Voice, Gesture, Shake, Visual, Sound, Vibrate, Flash
                }*/

                if (out.equals("Visual")) {
                    modalities[3] = true;//Voice, Gesture, Shake, Visual, Sound, Vibrate, Flash
                } else if (out.equals("Sound")) {
                    modalities[4] = true;//Voice, Gesture, Shake, Visual, Sound, Vibrate, Flash
                } else if (out.equals("Vibrate")) {
                    modalities[5] = true;//Voice, Gesture, Shake, Visual, Sound, Vibrate, Flash
                } else if (out.equals("Flash")) {
                    modalities[6] = true;//Voice, Gesture, Shake, Visual, Sound, Vibrate, Flash
                }



                //Log.i("rescue", "a:" + modalities[0] + ";" +modalities[1] + ";" +modalities[2] + ";" +modalities[3] + ";" +modalities[4] + ";" +modalities[5] + ";" +modalities[6]);

            }


        });


    }
}

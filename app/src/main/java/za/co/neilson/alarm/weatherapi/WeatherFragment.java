package za.co.neilson.alarm.weatherapi;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;

import za.co.neilson.alarm.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WeatherFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WeatherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class WeatherFragment extends android.support.v4.app.Fragment {
    private TextView cityText;
    private TextView condDescr;
    private TextView temp;
    private TextView press;
    private TextView windSpeed;
    private TextView windDeg;
    private TextView hum;
    private ImageView imgView;
    private View myFragmentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);
        myFragmentView = inflater.inflate(R.layout.fragment_weatherinfo, container, false);

        String city = "Paris, FR";  // city settings, use paris by default
        cityText = (TextView)  myFragmentView.findViewById(R.id.cityText);
        condDescr = (TextView) myFragmentView.findViewById(R.id.condDescr);
        temp = (TextView)  myFragmentView.findViewById(R.id.temp);
        hum = (TextView) myFragmentView.findViewById(R.id.hum);
        press = (TextView) myFragmentView.findViewById(R.id.press);
        windSpeed = (TextView)  myFragmentView.findViewById(R.id.windSpeed);
        windDeg = (TextView)  myFragmentView.findViewById(R.id.windDeg);
        imgView = (ImageView)  myFragmentView.findViewById(R.id.condIcon);

        JSONWeatherTask task = new JSONWeatherTask();
        task.execute(new String[]{city});

        return myFragmentView;
    }

    public interface OnFragmentInteractionListener {
    }

    private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {

        @Override
        protected Weather doInBackground(String... params) {
            Weather weather = new Weather();
            String data = ( (new WeatherHttpClient()).getWeatherData(params[0]));

            try {
                weather = JSONWeatherParser.getWeather(data);

                // Let's retrieve the icon
                weather.iconData = ( (new WeatherHttpClient()).getImage(weather.currentCondition.getIcon()));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return weather;
        }

        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);

            if (weather.iconData != null && weather.iconData.length > 0) {
                Bitmap img = BitmapFactory.decodeByteArray(weather.iconData, 0, weather.iconData.length);
                imgView.setImageBitmap(img);
            }

            // get weatherID to classify into one of 4 modalities
            String weatherID = String.valueOf(weather.currentCondition.getWeatherId());
            char initialNum = weatherID.charAt(0); // the code for openweather API, weather Number

            cityText.setText(weather.location.getCity() + "," + weather.location.getCountry());
            condDescr.setText(weather.currentCondition.getCondition() +
                              "(" + weather.currentCondition.getDescr() + ")");
            temp.setText("" + Math.round((weather.temperature.getTemp() - 273.15)) + "℃");
            hum.setText("" + weather.currentCondition.getHumidity() + "%");
            press.setText("" + weather.currentCondition.getPressure() + " hPa");
            windSpeed.setText("" + weather.wind.getSpeed() + " mps");
            windDeg.setText("" + weather.wind.getDeg() + "km/h");

        }

    }

}

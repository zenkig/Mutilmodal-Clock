package za.co.neilson.alarm.weatherapi;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;

import za.co.neilson.alarm.R;

public class WeatherInfoActivity extends FragmentActivity {
	
	private TextView cityText;
	private TextView condDescr;
	private TextView temp;
	private TextView press;
	private TextView windSpeed;
	private TextView windDeg;
	private TextView hum;
	private ImageView imgView;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_weatherinfo);
        String city = "Paris, FR";  // city settings, use paris by default

        cityText = (TextView)  findViewById(R.id.cityText);
		condDescr = (TextView) findViewById(R.id.condDescr);
		temp = (TextView)  findViewById(R.id.temp);
		hum = (TextView) findViewById(R.id.hum);
		press = (TextView) findViewById(R.id.press);
		windSpeed = (TextView)  findViewById(R.id.windSpeed);
		windDeg = (TextView)  findViewById(R.id.windDeg);
		imgView = (ImageView)  findViewById(R.id.condIcon);

		JSONWeatherTask task = new JSONWeatherTask();
		task.execute(new String[]{city});
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}
	
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
            char initialNum = weatherID.charAt(0); // the code for openweather API

			cityText.setText(weather.location.getCity() + "," + weather.location.getCountry());
			condDescr.setText(weather.currentCondition.getCondition() + "(" + weather.currentCondition.getDescr() + ")");
			temp.setText("" + Math.round((weather.temperature.getTemp() - 273.15)) + "℃");
			hum.setText("" + weather.currentCondition.getHumidity() + "%");
			press.setText("" + weather.currentCondition.getPressure() + " hPa");
			windSpeed.setText("" + weather.wind.getSpeed() + " mps");
			windDeg.setText("" + weather.wind.getDeg() + "km/h");
				
		}

	
  }

}

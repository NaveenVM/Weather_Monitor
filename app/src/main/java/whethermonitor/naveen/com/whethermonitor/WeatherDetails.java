package whethermonitor.naveen.com.whethermonitor;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

public class WeatherDetails extends AppCompatActivity{

    TextView pressValue,humidValue,windValue,pressText,humidText,windText;
 //   ArrayList<BeanWeather> chennaiList = new ArrayList<BeanWeather>();
    ArrayList<BeanWeather> mumbaiList = new ArrayList<BeanWeather>();
    ArrayList<BeanWeather> bangaloreList = new ArrayList<BeanWeather>();
    ArrayList<BeanWeather> newdelhiList = new ArrayList<BeanWeather>();
 //   BeanWeather beanWeather = new BeanWeather();
    String pressure,humidity,wind;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WeatherDetails.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.header);
        getSupportActionBar().setElevation(0);
        View v = getSupportActionBar().getCustomView();
        TextView titleTxtView = (TextView) v.findViewById(R.id.titleTxt);


        setContentView(R.layout.activity_weather_details);

        Typeface typeface= Typeface.createFromAsset(getApplicationContext().getAssets(),"comfortaa_bold.ttf");
        Typeface typefaceBroshk= Typeface.createFromAsset(getApplicationContext().getAssets(),"broshk.ttf");
        pressValue = (TextView)findViewById(R.id.press_Value);
        pressValue.setTypeface(typeface);
        humidValue = (TextView)findViewById(R.id.humid_value);
        humidValue.setTypeface(typeface);
        windValue = (TextView)findViewById(R.id.wind_value);
        windValue.setTypeface(typeface);
        pressText = (TextView)findViewById(R.id.text_title_1);
        pressText.setTypeface(typefaceBroshk);
        humidText = (TextView)findViewById(R.id.text_title_2);
        humidText.setTypeface(typefaceBroshk);
        windText = (TextView)findViewById(R.id.text_title_3);
        windText.setTypeface(typefaceBroshk);

        ArrayList<BeanWeather> cityList = (ArrayList<BeanWeather>) getIntent().getSerializableExtra("mylist");
        String cityName = getIntent().getExtras().getString("cityName");
        Log.d("TestTag","cityList "+cityList);
        titleTxtView.setText(cityName);
        getWeatherDetails(cityList);
    }
    public void getWeatherDetails(ArrayList<BeanWeather> cityList){
        Log.d("TestTag","size "+cityList.size());
        for(int i=0; i<cityList.size();i++){
         //   Log.d("TestTag","size "+chennaiList.size());
            BeanWeather beanWeather = cityList.get(i);
            pressure = beanWeather.getPress();
            humidity = beanWeather.getHumid();
            wind = beanWeather.getWind();

            pressValue.setText(pressure+" hPa");
            humidValue.setText(humidity+" %");
            windValue.setText(wind+" m/s");

            Log.d("TestTag","pressure 222 "+pressure);

        }

    }
}

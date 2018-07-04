package whethermonitor.naveen.com.whethermonitor;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    Timer timer;
    TimerTask doAsynchronousTask;
    TextView tempChennai,tempMumbai,tempBangalore,tempNewDelhi;
    RelativeLayout relativeChennai,relativeMumdai,relativeBangalore,relativeNewDelhi;
    String weatherKey = "&APPID=5ceb8f0a44dc7554154ce93db3bc28e8";
    String urls,cheURL;
    String temperature,pressure,humidity,wind;
    ArrayList<BeanWeather> chennaiList = new ArrayList<BeanWeather>();
    ArrayList<BeanWeather> mumbaiList = new ArrayList<BeanWeather>();
    ArrayList<BeanWeather> bangaloreList = new ArrayList<BeanWeather>();
    ArrayList<BeanWeather> newdelhiList = new ArrayList<BeanWeather>();
    double tempdouble;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.main_header);
        getSupportActionBar().setElevation(0);
        View v = getSupportActionBar().getCustomView();
        TextView titleTxtView = (TextView) v.findViewById(R.id.titleTxt);

        Typeface typeface= Typeface.createFromAsset(getApplicationContext().getAssets(),"comfortaa_bold.ttf");

        tempChennai = (TextView)findViewById(R.id.temperatureValue_1);
        tempChennai.setTypeface(typeface);
        tempMumbai = (TextView)findViewById(R.id.temperatureValue_2);
        tempMumbai.setTypeface(typeface);
        tempBangalore = (TextView)findViewById(R.id.temperatureValue_3);
        tempBangalore.setTypeface(typeface);
        tempNewDelhi = (TextView)findViewById(R.id.temperatureValue_4);
        tempNewDelhi.setTypeface(typeface);

        relativeChennai = (RelativeLayout)findViewById(R.id.relative_chennai);
        relativeMumdai = (RelativeLayout)findViewById(R.id.relative_mumbai);
        relativeBangalore = (RelativeLayout)findViewById(R.id.relative_bangalore);
        relativeNewDelhi = (RelativeLayout)findViewById(R.id.relative_newdelhi);

        titleTxtView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.key_address_layouy);

                final EditText key_Text = (EditText) dialog.findViewById(R.id.editText);
                Button btn = (Button) dialog.findViewById(R.id.button);

                dialog.show();
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String weather_key = key_Text.getText().toString().trim();
                        if(weather_key.equals("")||(weather_key.length()==0)){
                            Toast.makeText(MainActivity.this,"Please enter IP address",Toast.LENGTH_SHORT).show();
                        }else{
                            weatherKey = "&APPID="+weather_key;
                            callAsynchronousTask();
                            dialog.dismiss();
                        }
                    }
                });
                return false;
            }
        });
        ///5ceb8f0a44dc7554154ce93db3bc28e8

        urls=  AppController.stringIpAddress(MainActivity.this);
   //     cheURL = urls+chennai_url;
        Log.d("TestTag","111");
     callAsynchronousTask();

        relativeChennai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,WeatherDetails.class);
                intent.putExtra("mylist", chennaiList);
                intent.putExtra("cityName","Chennai Report");
                startActivity(intent);
            }
        });
        relativeMumdai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,WeatherDetails.class);
                intent.putExtra("mylist", mumbaiList);
                intent.putExtra("cityName","Mumbai Report");
                startActivity(intent);
            }
        });
        relativeBangalore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,WeatherDetails.class);
                intent.putExtra("mylist", bangaloreList);
                intent.putExtra("cityName","Bangalore Report");
                startActivity(intent);
            }
        });
        relativeNewDelhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,WeatherDetails.class);
                intent.putExtra("mylist", newdelhiList);
                intent.putExtra("cityName","New Delhi Report");
                startActivity(intent);
            }
        });
    }

    public void callAsynchronousTask() {
        Log.d("TestTag","00111");
        final Handler handler = new Handler();
        Log.d("TestTag","00222");
        doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                Log.d("TestTag","00333");
                handler.post(new Runnable() {

                    public void run() {
                        getChennaiWeatherDetails();
                        getMumbaiWeatherDetails();
                        getBangaloreWeatherDetails();
                        getNewDelhiWeatherDetails();
                    }
                });
            }
        };
        timer = new Timer();
        timer.schedule(doAsynchronousTask, 0, 30000); //execute in every 30 sec
    }

    private void getChennaiWeatherDetails() {
        Log.d("TestTag","222");
        cheURL = urls+"Chennai"+weatherKey;
        Log.d("TestTag","cheURL "+cheURL);
        JsonObjectRequest req = new JsonObjectRequest( Request.Method.GET,
                cheURL,
                null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            BeanWeather beanWeather = new BeanWeather();

                            JSONObject object = new JSONObject(String.valueOf(response));
                            Log.d("TestTag","object "+object);
                            JSONObject objMain = object.getJSONObject("main");
                            Log.d("TestTag","getMain "+objMain);
                            temperature = objMain.get("temp").toString();
                            pressure = objMain.get("pressure").toString();
                            humidity = objMain.get("humidity").toString();
                            JSONObject objWind = object.getJSONObject("wind");
                            wind = objWind.get("speed").toString();

                            tempdouble = Double.parseDouble(temperature);
                            double roundOff = (double) Math.round((tempdouble-273.15) * 100) / 100;
                            temperature = String.valueOf(roundOff);

                            tempChennai.setText(temperature+" °C");
                            Log.d("TestTag","temperature "+temperature);
                            beanWeather.setTemp(temperature);
                            beanWeather.setPress(pressure);
                            beanWeather.setHumid(humidity);
                            beanWeather.setWind(wind);

                            chennaiList.add(beanWeather);
                        }catch (Exception e){

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try{
                            if(error instanceof TimeoutError || error instanceof NoConnectionError) {
                                Toast.makeText(getApplicationContext(),"Timeout Please try again",
                                        Toast.LENGTH_LONG).show();
                            } else if (error instanceof AuthFailureError) {
                                Toast.makeText(getApplicationContext(),"AuthFailure Please try again",
                                        Toast.LENGTH_LONG).show();
                            } else if (error instanceof ServerError) {
                                Toast.makeText(getApplicationContext(),"Server Problem Please try again",
                                        Toast.LENGTH_LONG).show();
                            } else if (error instanceof NetworkError) {
                                Toast.makeText(getApplicationContext(),"Please check your Network",
                                        Toast.LENGTH_LONG).show();
                            } else if (error instanceof ParseError) {
                                Toast.makeText(getApplicationContext()," Parse Problem Please try again",
                                        Toast.LENGTH_LONG).show();
                            }}catch (Exception e){}
                        // hidepDialog();
                    }
                });
        AppController.getInstance().addToRequestQueue(req);
    }
    private void getMumbaiWeatherDetails() {
        Log.d("TestTag","222");
        cheURL = urls+"Mumbai"+weatherKey;
        JsonObjectRequest req = new JsonObjectRequest( Request.Method.GET,
                cheURL,
                null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            BeanWeather beanWeather = new BeanWeather();
                            JSONObject object = new JSONObject(String.valueOf(response));
                            Log.d("TestTag","object "+object);
                            JSONObject objMain = object.getJSONObject("main");
                            Log.d("TestTag","getMain "+objMain);
                            temperature = objMain.get("temp").toString();
                            pressure = objMain.get("pressure").toString();
                            humidity = objMain.get("humidity").toString();

                            JSONObject objWind = object.getJSONObject("wind");
                            wind = objWind.get("speed").toString();

                            tempdouble = Double.parseDouble(temperature);
                            double roundOff = (double) Math.round((tempdouble-273.15) * 100) / 100;
                            temperature = String.valueOf(roundOff);

                            tempMumbai.setText(temperature+" °C");
                            Log.d("TestTag","temperature "+temperature+" "+pressure+" "+humidity+" "+wind);
                            beanWeather.setTemp(temperature);
                            beanWeather.setPress(pressure);
                            beanWeather.setHumid(humidity);
                            beanWeather.setWind(wind);

                            mumbaiList.add(beanWeather);
                        }catch (Exception e){

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try{
                            if(error instanceof TimeoutError || error instanceof NoConnectionError) {
                                Toast.makeText(getApplicationContext(),"Timeout Please try again",
                                        Toast.LENGTH_LONG).show();
                            } else if (error instanceof AuthFailureError) {
                                Toast.makeText(getApplicationContext(),"AuthFailure Please try again",
                                        Toast.LENGTH_LONG).show();
                            } else if (error instanceof ServerError) {
                                Toast.makeText(getApplicationContext(),"Server Problem Please try again",
                                        Toast.LENGTH_LONG).show();
                            } else if (error instanceof NetworkError) {
                                Toast.makeText(getApplicationContext(),"Please check your Network",
                                        Toast.LENGTH_LONG).show();
                            } else if (error instanceof ParseError) {
                                Toast.makeText(getApplicationContext()," Parse Problem Please try again",
                                        Toast.LENGTH_LONG).show();
                            }}catch (Exception e){}
                        // hidepDialog();
                    }
                });
        AppController.getInstance().addToRequestQueue(req);
    }
    private void getBangaloreWeatherDetails() {
        Log.d("TestTag","222");
        cheURL = urls+"Bangalore"+weatherKey;
        JsonObjectRequest req = new JsonObjectRequest( Request.Method.GET,
                cheURL,
                null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            BeanWeather beanWeather = new BeanWeather();
                            JSONObject object = new JSONObject(String.valueOf(response));
                            Log.d("TestTag","object "+object);
                            JSONObject objMain = object.getJSONObject("main");
                            Log.d("TestTag","getMain "+objMain);
                            temperature = objMain.get("temp").toString();
                            pressure = objMain.get("pressure").toString();
                            humidity = objMain.get("humidity").toString();

                            JSONObject objWind = object.getJSONObject("wind");
                            wind = objWind.get("speed").toString();

                            tempdouble = Double.parseDouble(temperature);
                            double roundOff = (double) Math.round((tempdouble-273.15) * 100) / 100;
                            temperature = String.valueOf(roundOff);

                            tempBangalore.setText(temperature+" °C");
                            Log.d("TestTag","temperature "+temperature);
                            beanWeather.setTemp(temperature);
                            beanWeather.setPress(pressure);
                            beanWeather.setHumid(humidity);
                            beanWeather.setWind(wind);

                            bangaloreList.add(beanWeather);

                        }catch (Exception e){

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try{
                            if(error instanceof TimeoutError || error instanceof NoConnectionError) {
                                Toast.makeText(getApplicationContext(),"Timeout Please try again",
                                        Toast.LENGTH_LONG).show();
                            } else if (error instanceof AuthFailureError) {
                                Toast.makeText(getApplicationContext(),"AuthFailure Please try again",
                                        Toast.LENGTH_LONG).show();
                            } else if (error instanceof ServerError) {
                                Toast.makeText(getApplicationContext(),"Server Problem Please try again",
                                        Toast.LENGTH_LONG).show();
                            } else if (error instanceof NetworkError) {
                                Toast.makeText(getApplicationContext(),"Please check your Network",
                                        Toast.LENGTH_LONG).show();
                            } else if (error instanceof ParseError) {
                                Toast.makeText(getApplicationContext()," Parse Problem Please try again",
                                        Toast.LENGTH_LONG).show();
                            }}catch (Exception e){}
                        // hidepDialog();
                    }
                });
        AppController.getInstance().addToRequestQueue(req);
    }
    private void getNewDelhiWeatherDetails() {
        Log.d("TestTag","222");
        cheURL = urls+"New Delhi"+weatherKey;
        JsonObjectRequest req = new JsonObjectRequest( Request.Method.GET,
                cheURL,
                null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            BeanWeather beanWeather = new BeanWeather();
                            JSONObject object = new JSONObject(String.valueOf(response));
                            Log.d("TestTag","object "+object);
                            JSONObject objMain = object.getJSONObject("main");
                            Log.d("TestTag","getMain "+objMain);
                            temperature = objMain.get("temp").toString();
                            pressure = objMain.get("pressure").toString();
                            humidity = objMain.get("humidity").toString();

                            JSONObject objWind = object.getJSONObject("wind");
                            wind = objWind.get("speed").toString();

                            tempdouble = Double.parseDouble(temperature);
                            double roundOff = (double) Math.round((tempdouble-273.15) * 100) / 100;
                            temperature = String.valueOf(roundOff);

                            tempNewDelhi.setText(temperature+" °C");
                            Log.d("TestTag","temperature "+temperature);
                            beanWeather.setTemp(temperature);
                            beanWeather.setPress(pressure);
                            beanWeather.setHumid(humidity);
                            beanWeather.setWind(wind);

                            newdelhiList.add(beanWeather);
                        }catch (Exception e){

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try{
                            if(error instanceof TimeoutError || error instanceof NoConnectionError) {
                                Toast.makeText(getApplicationContext(),"Timeout Please try again",
                                        Toast.LENGTH_LONG).show();
                            } else if (error instanceof AuthFailureError) {
                                Toast.makeText(getApplicationContext(),"AuthFailure Please try again",
                                        Toast.LENGTH_LONG).show();
                            } else if (error instanceof ServerError) {
                                Toast.makeText(getApplicationContext(),"Server Problem Please try again",
                                        Toast.LENGTH_LONG).show();
                            } else if (error instanceof NetworkError) {
                                Toast.makeText(getApplicationContext(),"Please check your Network",
                                        Toast.LENGTH_LONG).show();
                            } else if (error instanceof ParseError) {
                                Toast.makeText(getApplicationContext()," Parse Problem Please try again",
                                        Toast.LENGTH_LONG).show();
                            }}catch (Exception e){}
                        // hidepDialog();
                    }
                });
        AppController.getInstance().addToRequestQueue(req);
    }
}

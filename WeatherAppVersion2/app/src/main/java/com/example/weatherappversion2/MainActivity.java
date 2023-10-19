//Joshua Stephen
//Capstone Project
//Real Time Weather App System
//MainActivity.java

package com.example.weatherappversion2;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//Volley Library
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

//JSON methods
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;


public class MainActivity extends AppCompatActivity {

    //Initializes the variables retrieved from activity_main.xml
    EditText lat;
    EditText lon;
    TextView Data;


    //Creates the link that the api retrieves data from
    private final String apilink = "https://api.open-meteo.com/v1/forecast?latitude";
    private final String apilink2  = "hourly=temperature_2m&hourly=windspeed_10m&hourly=precipitation&hourly=weathercode&hourly=apparent_temperature";

    DecimalFormat df = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lat = (EditText) findViewById(R.id.lat);
        lon = (EditText) findViewById(R.id.lon);
        Data = findViewById(R.id.Data);

    }

    // The method that retrieves weather data
    public void getWeatherInfo(View view) {
        String templink = "";
        float latitude = Float.parseFloat(lat.getText().toString());
        float longitude = Float.parseFloat(lon.getText().toString());

        if(lat == null || lon == null){
            Data.setText(R.string.error);
        } else {

            templink = apilink + "=" + latitude + "&longitude=" + longitude + "&" + apilink2;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, templink, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String data = "";
                try {

                    JSONObject jsonresponse = new JSONObject(response);

                    JSONObject jsonObjectMain = jsonresponse.getJSONObject("hourly");
                    JSONArray jsonArray = jsonresponse.getJSONArray("weathercode");

                    //Temperature is obtained in Kelvin, so it's converted to Fahrenheit
                    double temp = (jsonObjectMain.getDouble("temperature_2m") - 273.15) * (9/5) + 32;

                    double feelsLike = (jsonObjectMain.getDouble("apparent_temperature") - 273.15) * (9/5) + 32;
                    int rain = jsonObjectMain.getInt("precipitation");
                    double wind = jsonObjectMain.getDouble("windspeed_10m");

                    Data.setTextColor(Color.rgb(68,134,199));
                    data += "The weather is:"
                            + "\n Temp: " + df.format(temp) + " °F"
                            + "\n Feels Like: " + df.format(feelsLike) + " °F"
                            + "\n Precipitation: " + rain + "%"
                            + "\n Wind Speed: " + wind + "m/s (meters per second)";

                    Data.setText(data);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        });
        //Delivers response
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
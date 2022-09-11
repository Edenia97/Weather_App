package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
  EditText cityInput;
  TextView weatherData;

  private final String url = "https://api.openweathermap.org/data/2.5/weather";
  private final String apiKey = "fabcf7f98b15e8cbd282ef6228eb5348";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        cityInput = findViewById(R.id.textInputEditText);
        weatherData = findViewById(R.id.fetchedData);


    }

    /**
     * @param view
     * On button "Get" clicked fetch weather data for the relevant city from  Openweathermaps API
     */


    public void fetchWeather (View view) {
        String city = cityInput.getText().toString();
        if (city.equals("")) {
            Toast.makeText(getApplicationContext(), "You must enter a city name", Toast.LENGTH_LONG).show();

        } else {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url + "?q=" + city + "&appid=" + apiKey, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray weatherArray = jsonObject.getJSONArray("weather");
                        JSONObject weatherObject = weatherArray.getJSONObject(0);
                        String main = weatherObject.getString("main");
                        String description = weatherObject.getString("description");
                        JSONObject mainObject = jsonObject.getJSONObject("main");
                        double temp = mainObject.getDouble("temp") - 273.15;
                        double feels_like = mainObject.getDouble("feels_like") - 273.15;
                        int humidity = mainObject.getInt("humidity");
                        String country = jsonObject.getJSONObject("sys").getString("country");
                        String city = jsonObject.getString("name");

                        String forecast = "  " + city + ", " + country
                                + "\n  Weather Forecast: " + main
                                + "\n  Description: " + description
                                + "\n  Temperature: " + Math.round(temp) + " °C"
                                + "\n  Feels like: " + Math.round(feels_like) + " °C"
                                + "\n  Humidity: " + humidity + "%";

                        weatherData.setText(forecast);


                    } catch (JSONException exception) {
                        exception.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                 
                   weatherData.setText(error.toString());
                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }
}
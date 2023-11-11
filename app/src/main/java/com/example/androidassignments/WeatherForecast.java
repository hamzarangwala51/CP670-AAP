package com.example.androidassignments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class WeatherForecast extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView minTempTextView,currentTempTextView,maxTempTextView;
    private ImageView weatherImageView;
    private Spinner dropdownList;
    private ExecutorService executorService;
    private Handler handler;
    private  String minTemperature;
    private  String maxTemperature;
    private  String currentTemperature;
    private  String weatherIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        // Find the ProgressBar, TextViews, and ImageView by their IDs
        progressBar = findViewById(R.id.progressBar);
        minTempTextView = findViewById(R.id.textView_min_temp);
        maxTempTextView = findViewById(R.id.textView_max_temp);
        currentTempTextView = findViewById(R.id.textView_current_temp);
        weatherImageView = findViewById(R.id.imageView_current_weather);
        dropdownList = findViewById(R.id.drop_down_list_city);
        progressBar.setVisibility(View.VISIBLE);
        String[] cities = { "Toronto", "Ottawa", "Edmonton", "Victoria", "Winnipeg","Regina"};
        ArrayAdapter aa = new ArrayAdapter( this, android.R.layout.simple_spinner_item,cities);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdownList.setAdapter(aa);


        // Create and execute the ForecastQuery AsyncTask

        dropdownList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ForecastQuery f = new ForecastQuery();
                f.execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private class ForecastQuery extends AsyncTask<String, Integer, String> {

        private String minTemperature;
        private String maxTemperature;
        private String currentTemperature;
        private String weatherIcon;

        @Override
        protected  String doInBackground(String ...args) {
            try {
                // Corrected API URL with the placeholder API key
                String city = dropdownList.getSelectedItem().toString().toLowerCase();
                String apiUrl ="https://api.openweathermap.org/data/2.5/weather?q="+city+",ca&APPID=79cecf493cb6e52d25bb7b7050ff723c&mode=xml&units=metric";
                URL url = new URL(apiUrl);

                // Open a connection to the URL
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Read the response from the URL
                    InputStream inputStream = connection.getInputStream();

                    // Create an XMLPullParser to parse the XML data
                    XmlPullParser parser = Xml.newPullParser();
                    parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,false);
                    parser.setInput(inputStream, null);



                    while (parser.next() != XmlPullParser.END_DOCUMENT) {
                        parser.next();
                        if (parser.getEventType() != XmlPullParser.START_TAG) {
                            continue;
                        }
                            String tagName = parser.getName();
                            if ("temperature".equals(tagName)) {
                                minTemperature = parser.getAttributeValue(null, "min");
                                publishProgress(25);
                                Log.i("HAMZA",minTemperature);
                                maxTemperature = parser.getAttributeValue(null, "max");
                                publishProgress(50);
                                currentTemperature = parser.getAttributeValue(null, "value");
                                publishProgress(75);
                                // Update progress
                            } else if ("weather".equals(tagName)) {
                                weatherIcon = parser.getAttributeValue(null, "icon");
                            }
                    }
                    inputStream.close();
                    connection.disconnect();
                }else {
                    Log.e("WeatherApp", "Error: Non-OK response received from the weather API.");
                    return null;
                }


                if (weatherIcon != null && !weatherIcon.isEmpty()) {
                    String iconFileName = weatherIcon + ".png";
                    if (fileExistance(iconFileName)) {
                        Log.i("WeatherApp", iconFileName + " exists locally, no need to download.");
                        return iconFileName;
                    } else {
                        Log.i("WeatherApp", iconFileName + " does not exist, downloading now.");
                        String iconUrl = "https://openweathermap.org/img/w/" + weatherIcon + ".png";
                        Bitmap iconBitmap = downloadImage(iconUrl);
                        if (iconBitmap != null) {
                            saveImageLocally(iconFileName, iconBitmap);
                        }
                         // Download completed
                        publishProgress(100);
                        return iconFileName;
                    }
                } else {
                    Log.e("WeatherApp", "Weather icon is null or empty.");
                }


            } catch (Exception e) {
                Log.e("WeatherApp", "Error fetching weather data: " + e.getMessage());
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE); // Set ProgressBar visibility
                progressBar.setProgress(values[0]); // Update ProgressBar progress
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Update GUI components with temperature values
            minTempTextView.setText("Min Temp: " + minTemperature + "°C");
            maxTempTextView.setText("Max Temp: " + maxTemperature + "°C");
            currentTempTextView.setText("Current Temp: " + currentTemperature + "°C");

            // Set the ImageView with the downloaded Bitmap
            if (weatherImageView != null && result != null) {
                Bitmap iconBitmap = readImageFromStorage(result);
                if (iconBitmap != null) {
                    weatherImageView.setImageBitmap(iconBitmap);
                }
            }

            // Set the visibility of the ProgressBar to INVISIBLE
            if (progressBar != null) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        }


        // Add the downloadImage, saveImageLocally, and fileExistance methods as provided in your original code.
        // ...

        public boolean fileExistance(String fname) {
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }

        private Bitmap readImageFromStorage(String fileName) {
            try (FileInputStream fis = openFileInput(fileName)) {
                return BitmapFactory.decodeStream(fis);
            } catch (IOException e) {
                Log.e("WeatherApp", "Error reading image from storage: " + e.getMessage());
                return null;
            }
        }
        private void saveImageLocally(String fileName, Bitmap image) {
            try {
                FileOutputStream outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
                image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                outputStream.flush();
                outputStream.close();
                Log.i("WeatherApp", "Image " + fileName + ".png saved to local storage.");
            } catch (IOException e) {
                Log.e("WeatherApp", "Error saving image to local storage: " + e.getMessage());
            }
        }
        private Bitmap downloadImage(String imageUrl) {
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();

                // Get the input stream
                InputStream input = connection.getInputStream();

                // Decode the input stream into a Bitmap
                Bitmap bitmap = BitmapFactory.decodeStream(input);

                // Close the input stream and disconnect the connection
                input.close();
                connection.disconnect();

                return bitmap;
            } catch (IOException e) {
                Log.e("WeatherApp", "Error downloading image: " + e.getMessage());
                return null;
            }
        }

    }



}



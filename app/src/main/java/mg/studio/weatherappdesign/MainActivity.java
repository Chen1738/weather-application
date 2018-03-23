package mg.studio.weatherappdesign;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaCodec;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {

    //View v = this.getCurrentFocus();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(CheckNet.getNetState(this)==CheckNet.NET_NONE)
        {
            Log.d("weather","connect failed");
            Toast.makeText(MainActivity.this,"connect failed",Toast.LENGTH_SHORT).show();
        }else
        {
            Log.d("weather","connect success");
            Toast.makeText(MainActivity.this,"connect success",Toast.LENGTH_SHORT).show();
        }
        Date date = new Date(new Date().getTime());
        SimpleDateFormat dformat = new SimpleDateFormat("yyyy-MM-dd");
        String time = dformat.format(date);
        int weekday = date.getDay();
        String weekd = "null";



        Drawable mondraw = getResources().getDrawable(R.drawable .rainy_small );
        Drawable tuedraw = getResources().getDrawable(R.drawable .partly_sunny_small );
        Drawable thurdraw = getResources().getDrawable(R.drawable .windy_small );
        Drawable fridraw = getResources().getDrawable(R.drawable .sunny_small );
        Drawable weekbgd = getResources().getDrawable(R.drawable .blueback );

        switch(weekday){
            case 0:weekd = "Sunday";break;

            case 1:weekd = "Monday";
                ((ImageView)findViewById(R.id.img_weather_condition)).setImageDrawable(mondraw);break;
            case 2:weekd = "Tuesday";
                ((ImageView)findViewById(R.id.img_weather_condition)).setImageDrawable(tuedraw);break;
            case 3:weekd = "Wednsday";break;
            case 4:weekd = "Thursday";
                ((ImageView)findViewById(R.id.img_weather_condition)).setImageDrawable(thurdraw);break;
            case 5:weekd = "Friday";
                ((ImageView)findViewById(R.id.img_weather_condition)).setImageDrawable(fridraw);break;
            case 6:weekd = "Saturday";break;

        }

        ((TextView) findViewById(R.id.tv_date)).setText(time);
        ((TextView) findViewById(R.id.week)).setText(weekd);
        ((TextView) findViewById(R.id.temperature_of_the_day)).setText("20");
    }



    public void btnClick(View view) {
        try {
            new DownloadUpdate().execute();
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "network unconnected", Toast.LENGTH_SHORT).show();
        }
    }

    public void refresh(View v){
        //refresh remperature,the date and the date of the week
        if(CheckNet.getNetState(this)==CheckNet.NET_NONE)
        {   Log.d("weather","connect failed");
            Toast.makeText(MainActivity.this,"network unconnected",Toast.LENGTH_SHORT).show();}
        else
        {

        Date date = new Date(new Date().getTime());
        SimpleDateFormat dformat = new SimpleDateFormat("yyyy-MM-dd");
        String time = dformat.format(date);
        int weekday = date.getDay();
        String weekd = "null";



        Drawable mondraw = getResources().getDrawable(R.drawable .rainy_small );
        Drawable tuedraw = getResources().getDrawable(R.drawable .partly_sunny_small );
        Drawable thurdraw = getResources().getDrawable(R.drawable .windy_small );
        Drawable fridraw = getResources().getDrawable(R.drawable .sunny_small );
        Drawable weekbgd = getResources().getDrawable(R.drawable .blueback );

        switch(weekday){
            case 0:weekd = "Sunday";break;

            case 1:weekd = "Monday";
                ((ImageView)findViewById(R.id.img_weather_condition)).setImageDrawable(mondraw);break;
            case 2:weekd = "Tuesday";
                ((ImageView)findViewById(R.id.img_weather_condition)).setImageDrawable(tuedraw);break;
            case 3:weekd = "Wednsday";break;
            case 4:weekd = "Thursday";
                ((ImageView)findViewById(R.id.img_weather_condition)).setImageDrawable(thurdraw);break;
            case 5:weekd = "Friday";
                ((ImageView)findViewById(R.id.img_weather_condition)).setImageDrawable(fridraw);break;
            case 6:weekd = "Saturday";break;

        }

        ((TextView) findViewById(R.id.tv_date)).setText(time);
        ((TextView) findViewById(R.id.week)).setText(weekd);
        ((TextView) findViewById(R.id.tv_location)).setText("chongqing");
        ((TextView) findViewById(R.id.temperature_of_the_day)).setText("18");
        Toast.makeText(MainActivity.this, "update successfully", Toast.LENGTH_SHORT).show();
        }
    }



    private class DownloadUpdate extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String stringUrl = "https://api.seniverse.com/v3/weather/now.json?key=qkt9kcebscstyuee&location=chongqing&language=en&unit=c";
            HttpURLConnection urlConnection = null;
            BufferedReader reader;

            try {
                URL url = new URL(stringUrl);

                // Create the request to get the information from the server, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Mainly needed for debugging
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                //The temperature
                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            }  catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "update successfully", Toast.LENGTH_SHORT).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String temp) {
            //Update the temperature displayed
            int indexStart = temp.indexOf("temperature\":\"");
            int endIndex = temp.indexOf("last_update");
            String temperatures = temp.substring(indexStart+14,endIndex-4);
            //String[] trans = temp.split("temperature\":\"");
            //String temperatures = trans[1];
            ((TextView) findViewById(R.id.temperature_of_the_day)).setText(temperatures);
        }
    }
}

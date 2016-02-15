package com.example.android.sunshine.app;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment {

    public ForecastFragment() {
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.forecastfragment, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            FetchWeatherTask weatherTask=new FetchWeatherTask();

            weatherTask.execute("122001");
            return  true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        String[] forecastArray = {
                "Today - Sunny - 88/22",
                "Tomorrow - Cloudy - 81/52",
                "Weds - Foggy - 82/32",
                "Thurs - Bunny - 28/92",
                "Fri - Tunny - 18/02",
                "Sat - Tunny - 18/02",
                "Sun - Tunny - 18/02"
        };
        List<String> weekForecast = new ArrayList<String>(Arrays.asList(forecastArray));
        ArrayAdapter<String> mForecastAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_forecast, R.id.list_item_forecast_textview, weekForecast);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(mForecastAdapter);
       // super.onCreateOptionsMenu(R.menu.forecastfragment, inflater);
        //inflater.inflate(R.menu.mnulayout, menu);
        setHasOptionsMenu(true);

        return rootView;
    }

    public class FetchWeatherTask extends AsyncTask<String,Void,Void>
    {
        private final String LOG_TAG=FetchWeatherTask.class.getSimpleName();

        protected Void doInBackground(String... params) {
            try {
                if(params.length==0){
                    return  null;
                }
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are available at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&cnt=7&units=metric&appid=78c701443ea092d00e7e0180a6bf1401");
                String format="json";
                String units="metric";
                String apikey="78c701443ea092d00e7e0180a6bf1401";
                int numDays=7;
                final String BaseUrl="http://api.openweathermap.org/data/2.5/forecast/daily?";
                final String Query_Param="q";
                final String API_Param="appid";
                final String Units_Param="units";
                final String Format_Param="mode";
                final String Days_Param="cnt";

                // Create the request to OpenWeatherMap, and open the connection
                Uri builtUri=Uri.parse(BaseUrl).buildUpon().appendQueryParameter(Query_Param, params[0])
                        .appendQueryParameter(Format_Param, format).appendQueryParameter(Units_Param, units)
                        .appendQueryParameter(Days_Param, Integer.toString(numDays)).appendQueryParameter(API_Param, apikey).build();
                Log.v("BuiltURL",builtUri.toString());
          //      Toast.makeText(getActivity(), builtUri.toString(), Toast.LENGTH_LONG).show();
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }

                Log.v("JSONDATAWEATHER",buffer.toString());
                return null;
                //buffer.toString();
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                return null;
            }
        }
    }
}
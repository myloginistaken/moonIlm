package anton.ilm.screenFragments;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import anton.ilm.objects.Place;
import anton.ilm.parsers.IntegerToString;
import anton.ilm.asyncTask.DownloadXMLTask;
import anton.ilm.main.Main;
import anton.ilm.interfaces.OnTaskCompleted;
import anton.ilm.R;
import anton.ilm.objects.Forecast;

/**
 * Created by anton on 26.05.15.
 */
public class MainScreenFragment extends Fragment {

    private FrameLayout frameLayout;
    private boolean displayDesc = false;
    private Switch mySwitch;
    private TextView date, temp, phenomenon, tempWords, desc, wind, more;
    private static final int TOMORROW = Main.getTomorrow();
    private IntegerToString its;
    private boolean isPortrait = true;

    private void getData(){
        if ((Main.getsPref().equals(getResources().getString(R.string.any))) && (Main.isWifi() || Main.isMobile())) {
            DownloadXMLTask task = new DownloadXMLTask(new OnTaskCompleted() {
                @Override
                public void onTaskCompleted(List result) {
                    fillPage(result);
                }
            });
            task.execute(Main.getUrl());
        } else if ((Main.getsPref().equals(getResources().getString(R.string.wifi))) && (Main.isWifi())) {
            DownloadXMLTask task = new DownloadXMLTask(new OnTaskCompleted() {
                @Override
                public void onTaskCompleted(List result) {
                    fillPage(result);
                }
            });
            task.execute(Main.getUrl());
        }
        //else is working from Main itself. No need to repeat it here

    }

    public void fillPage(List<Forecast> forecasts){
        date.setText(forecasts.get(TOMORROW).getDate());
        List<Place> places = forecasts.get(TOMORROW).getPlaces();
        if(Main.getPlaceName() != null) {
            for (int i = 0; i < places.size(); i++) {
                if (places.get(i).getName().equals(Main.getPlaceName())) {
                    fillPagePlaceSelected(forecasts, i);
                }
            }
        } else {
            fillPageDefault(forecasts);
        }
    }
    //fill page with default data
    public void fillPageDefault(List<Forecast> forecasts){
        temp.setText(forecasts.get(TOMORROW).getTempInterval()+getResources().getString(R.string.temp_unit));

        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "artill_clean_icons.ttf");
        phenomenon.setTypeface(font);
        if(Main.isDay()) {
            phenomenon.setText(Main.weather_icons.get(forecasts.get(TOMORROW).getPhenomenon()).toString());
        } else {
            String weather_symbol = Main.weather_icons.get(forecasts.get(TOMORROW).getPhenomenon()).toString();
            if(weather_symbol.equals("1")){
                weather_symbol = "6";
            } else if(weather_symbol.equals("2")){
                weather_symbol = "a";
            } else {
                weather_symbol = weather_symbol.toLowerCase();

            }
            phenomenon.setText(weather_symbol);
        }

        tempWords.setText(its.parseValue(forecasts.get(TOMORROW).getTempInterval())
                +getResources().getString(R.string.temp_unit_words));

        if(isPortrait) {
            desc.setVisibility(View.GONE);
        }
        desc.setText(forecasts.get(TOMORROW).getDesc());
        wind.setText(getResources().getString(R.string.wind)+forecasts.get(TOMORROW).getWind()
                +getResources().getString(R.string.wind_unit));
    }

    //fill page accordingly to a place if the city was selected
    public void fillPagePlaceSelected(List<Forecast> forecasts, int position){
        temp.setText(forecasts.get(TOMORROW).getPlaces().get(position).getTempInterval()
                +getResources().getString(R.string.temp_unit));

        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "artill_clean_icons.ttf");
        phenomenon.setTypeface(font);
        // using artill_clean_icons.ttf allows to change between day and night weather icons by going to lower case
        if(Main.isDay()) {
            phenomenon.setText(Main.weather_icons.get(forecasts.get(TOMORROW).getPlaces().get(position).getPhenomenon()).toString());
        } else {
            // those are two exceptions I used, where lower case doesn't help
            String weather_symbol = Main.weather_icons.get(forecasts.get(TOMORROW).getPlaces().get(position).getPhenomenon()).toString();
            if(weather_symbol.equals("1")){
                weather_symbol = "6";
            } else if(weather_symbol.equals("2")){
                weather_symbol = "a";
            } else {
                weather_symbol = weather_symbol.toLowerCase();

            }
            phenomenon.setText(weather_symbol);
        }

        tempWords.setText(its.parseValue(forecasts.get(TOMORROW).getPlaces().get(position).getTempInterval().toString())
                +getResources().getString(R.string.temp_unit_words));

        desc.setVisibility(View.GONE);
        if(isPortrait) {
            more.setVisibility(View.GONE);
        }
        wind.setText(forecasts.get(TOMORROW).getPlaces().get(position).getName());
    }

    private void initViews(ViewGroup rootView){
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyy");
        String currentDate = sdf.format(new Date());

        //switch between day and night forecasts
        mySwitch = (Switch) rootView.findViewById(R.id.mySwitch);
        //set the switch
        mySwitch.setChecked(Main.isDay());
        //attach a listener to check for changes in state
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    Main.setDay(true);
                }else{
                    Main.setDay(false);
                }
                getData();
                ForecastsScreenFragment.getData();

            }
        });

        its = new IntegerToString(rootView.getContext());
        more = null;

        date = (TextView) rootView.findViewById(R.id.date);
        date.setText(currentDate);

        temp = (TextView) rootView.findViewById(R.id.temp);
        phenomenon = (TextView) rootView.findViewById(R.id.ic_bigWeather);
        tempWords = (TextView) rootView.findViewById(R.id.temp_words);
        desc = (TextView) rootView.findViewById(R.id.desc);
        wind = (TextView) rootView.findViewById(R.id.wind);


        if((more = (TextView) rootView.findViewById(R.id.more)) != null) {
            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView tv = (TextView) getActivity().findViewById(R.id.desc);
                    if (!displayDesc) {
                        tv.setVisibility(View.VISIBLE);
                    } else {
                        tv.setVisibility(View.GONE);
                    }
                    displayDesc = !displayDesc;
                }
            });
        }

        desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv = (TextView) getActivity().findViewById(R.id.more);
                if (displayDesc) {
                    tv.setVisibility(View.VISIBLE);
                    v.findViewById(R.id.desc).setVisibility(View.GONE);
                }
                displayDesc = !displayDesc;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        frameLayout = new FrameLayout(getActivity());
        final ViewGroup rootView;

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            isPortrait = true;
            rootView = (ViewGroup) inflater.inflate(R.layout.main_portrait, container, false);
        } else {
            isPortrait = false;
            rootView = (ViewGroup) inflater.inflate(R.layout.main_landscape, container, false);
        }

        initViews(rootView);
        getData();
        frameLayout.addView(rootView);

        return frameLayout;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);

        frameLayout. removeAllViews();
        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup rootView = null;

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            isPortrait = false;
            rootView = (ViewGroup) inflater.inflate(R.layout.main_landscape, null);
            initViews(rootView);
            getData();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            rootView = (ViewGroup) inflater.inflate(R.layout.main_portrait, null);
            isPortrait = true;
            initViews(rootView);
            getData();
        }

        frameLayout.addView(rootView);
    }
}

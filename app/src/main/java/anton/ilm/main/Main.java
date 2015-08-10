package anton.ilm.main;

import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import java.util.HashMap;

import anton.ilm.R;
import anton.ilm.dialogs.WarningDialog;
import anton.ilm.screenFragments.ForecastsScreenFragment;
import anton.ilm.screenFragments.MainScreenFragment;
import anton.ilm.screenFragments.PlacesScreenFragment;
import anton.ilm.screenFragments.SettingsScreenFragment;

/**
 * Created by anton on 26.05.15.
 */

public class Main extends ActionBarActivity {

    private ActionBar actionBar;
    private static final String WIFI = "WiFi";
    private static final String ANY = "Any";
    private static final String URL = "http://www.ilmateenistus.ee/ilma_andmed/xml/forecast.php";
    private static String sPref = "Any";
    private static boolean wifi = false;
    private static boolean mobile = false;
    private static final int NUM_PAGES = 4;
    private static final int TOMORROW = 0;
    private static String placeName = null;
    private static boolean isDay = true;
    public static final HashMap weather_icons = new HashMap<String, Character>();
    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;
    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    // Uses AsyncTask to download the XML forecasts
    public void checkConnection() {

        ConnectivityManager connManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        // Whether there is a Wi-Fi connection.
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        // Whether there is a mobile connection.
        NetworkInfo mMobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((sPref.equals(ANY)) && (mWifi.isConnected() || mMobile.isConnected())) {
            if(mWifi.isConnected()) {
                wifi = true;
            } else {
                wifi = false;
            }
            mobile = true;
        } else if ((sPref.equals(WIFI)) && (mWifi.isConnected())) {
            wifi = true;
        } else if ((sPref.equals(WIFI)) && (mMobile.isConnected())) {
            showDialog(getResources().getString(R.string.no_3g_connection));
        } else {
            showDialog(getResources().getString(R.string.no_connection));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_slide);
        //allow downloading data when connection exists
        //sPref = ANY;
        //allow downloading data only if wifi connection exists
        //sPref = WIFI;
        if(SettingsScreenFragment.getCheckBoxStatus()){
            sPref = WIFI;
        } else {
            sPref = ANY;
        }
        checkConnection();

        actionBar = getSupportActionBar();
        // Specify that tabs should be displayed in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setStackedBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.dark_purple)));
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        alignIconsWithPhenomenons();

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        // When swiping between pages, select the corresponding tab.
                        getSupportActionBar().setSelectedNavigationItem(position);
                    }
                });

        // Create a tab listener that is called when the user changes tabs.
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {

            public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {
                mPager.setCurrentItem(tab.getPosition());
            }

            public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {
                //mPager.setCurrentItem(tab.getPosition());
            }

            public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {
            }
        };

        // Add 4 tabs, specifying the tab's text and TabListener
        actionBar.addTab(actionBar.newTab().setText(getResources().getString(R.string.main_tab)).setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText(getResources().getString(R.string.forecasts_tab)).setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText(getResources().getString(R.string.places_tab)).setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText(getResources().getString(R.string.settings_tab)).setTabListener(tabListener));
    }

    private void showDialog(String msg) {

        // DialogFragment.show() will take care of adding the fragment in a transaction
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = WarningDialog.newInstance(msg);
        newFragment.show(ft, "dialog");
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    public static boolean isWifi() {
        return wifi;
    }

    public static boolean isMobile() {
        return mobile;
    }

    public static String getUrl() {
        return URL;
    }

    public static String getsPref() {
        return sPref;
    }

    public static int getTomorrow(){
        return TOMORROW;
    }

    private static void alignIconsWithPhenomenons(){

        weather_icons.put("Clear", '1');
        weather_icons.put("Few clouds", '2');
        weather_icons.put("Variable clouds", 'A');
        weather_icons.put("Cloudy with clear spells", 'A');
        weather_icons.put("Cloudy", '3');
        weather_icons.put("Light snow shower", 'H');
        weather_icons.put("Moderate snow shower", 'H');
        weather_icons.put("Heavy snow shower", 'H');
        weather_icons.put("Light shower", 'F');
        weather_icons.put("Moderate shower", 'L');
        weather_icons.put("Heavy shower", 'V');
        weather_icons.put("Light rain", 'G');
        weather_icons.put("Moderate rain", 'M');
        weather_icons.put("Heavy rain", 'W');
        weather_icons.put("Risk of glaze", '…');
        weather_icons.put("Light sleet", 'T');
        weather_icons.put("Moderate sleet", 'U');
        weather_icons.put("Light snowfall", 'I');
        weather_icons.put("Moderate snowfall", 'I');
        weather_icons.put("Heavy snowfall", 'I');
        weather_icons.put("Snowstorm", 'I');
        weather_icons.put("Drifting snow", 'I');
        weather_icons.put("Hail", 'O');
        weather_icons.put("Mist", 'C');
        weather_icons.put("Fog", 'C');
        weather_icons.put("Thunder", 'Y');
        weather_icons.put("Thunderstorm", 'S');
    }

    public static boolean isDay() {
        return isDay;
    }

    public static void setDay(boolean day){
        isDay = day;
    }

    public static String getPlaceName() {
        return placeName;
    }

    public static void setPlaceName(String name) {
        placeName = name;
    }

    /**
     * A simple pager adapter that represents 4 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            if(position == 0) {
                return new MainScreenFragment();
            } else if(position == 1){
                return new ForecastsScreenFragment();
            } else if(position == 2){
                return new PlacesScreenFragment();
            } else {
                return new SettingsScreenFragment();
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}

package anton.ilm.screenFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

import anton.ilm.asyncTask.DownloadXMLTask;
import anton.ilm.main.Main;
import anton.ilm.interfaces.OnTaskCompleted;
import anton.ilm.adapters.PlacesListViewAdapter;
import anton.ilm.R;
import anton.ilm.objects.Forecast;
import anton.ilm.objects.Place;

/**
 * Created by anton on 26.05.15.
 */

public class PlacesScreenFragment extends Fragment {

    private List<Place> places;
    private List<Forecast> forecasts;
    private ListView listView;
    private Context context;
    private static final int TOMORROW = Main.getTomorrow();

    private void getData(){
        if ((Main.getsPref().equals(getResources().getString(R.string.any))) && (Main.isWifi() || Main.isMobile())) {
            DownloadXMLTask task = new DownloadXMLTask(new OnTaskCompleted() {
                @Override
                public void onTaskCompleted(List result) {
                    forecasts = result;
                    places = forecasts.get(TOMORROW).getPlaces();
                    listView.setAdapter(new PlacesListViewAdapter(context.getApplicationContext(), places));
                }
            });
            task.execute(Main.getUrl());
        } else if ((Main.getsPref().equals(getResources().getString(R.string.wifi))) && (Main.isWifi())) {
            DownloadXMLTask task = new DownloadXMLTask(new OnTaskCompleted() {
                @Override
                public void onTaskCompleted(List result) {
                    forecasts = result;
                    places = forecasts.get(TOMORROW).getPlaces();
                    listView.setAdapter(new PlacesListViewAdapter(context.getApplicationContext(), places));
                }
            });
            task.execute(Main.getUrl());
        }
        //else is working from Main itself. No need to repeat it here

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.list_items, container, false);

        places = new ArrayList<>();

        listView = (ListView) rootView.findViewById(R.id.places);
        context = rootView.getContext();

        getData();

        return rootView;
    }
}

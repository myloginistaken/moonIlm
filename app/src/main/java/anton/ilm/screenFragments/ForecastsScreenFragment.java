package anton.ilm.screenFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import java.util.List;
import anton.ilm.asyncTask.DownloadXMLTask;
import anton.ilm.adapters.ForecastsListViewAdapter;
import anton.ilm.main.Main;
import anton.ilm.interfaces.OnTaskCompleted;
import anton.ilm.R;
import anton.ilm.objects.Forecast;

/**
 * Created by anton on 29.05.15.
 */
public class ForecastsScreenFragment extends Fragment {

    private static List<Forecast> forecasts;
    private static ListView listView;
    private static Context context;

    public static void getData(){
        if ((Main.getsPref().equals(context.getResources().getString(R.string.any))) && (Main.isWifi() || Main.isMobile())) {
            DownloadXMLTask task = new DownloadXMLTask(new OnTaskCompleted() {
                @Override
                public void onTaskCompleted(List result) {
                    forecasts = result;
                    listView.setAdapter(new ForecastsListViewAdapter(context.getApplicationContext(), forecasts));
                }
            });
            task.execute(Main.getUrl());
        } else if ((Main.getsPref().equals(context.getResources().getString(R.string.wifi))) && (Main.isWifi())) {
            DownloadXMLTask task = new DownloadXMLTask(new OnTaskCompleted() {
                @Override
                public void onTaskCompleted(List result) {
                    forecasts = result;
                    listView.setAdapter(new ForecastsListViewAdapter(context.getApplicationContext(), forecasts));
                }
            });
            task.execute(Main.getUrl());
        }
        //else is working from Main itself. No need to repeat it here

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.list_items, container, false);

        listView = (ListView) rootView.findViewById(R.id.places);
        context = rootView.getContext();

        getData();

        return rootView;
    }
}

package anton.ilm.screenFragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import anton.ilm.R;
import anton.ilm.adapters.SettingsListViewAdapter;

/**
 * Created by anton on 6.06.15.
 */
public class SettingsScreenFragment extends Fragment {

    private static String[] places;
    private static int[] group = {1,0,0,0,0,0,0}; //array that contains radio buttons statuses
    private static ListView listView;
    private static Context context;
    private static boolean wifiOnly = false; //value to save wifi/any connection checkbox status

    private static void fillPage(){
        places = context.getResources().getStringArray(R.array.place_names);
        listView.setAdapter(new SettingsListViewAdapter(context.getApplicationContext(), places, group));
    }

    //clear all the radio buttons, except the pressed one
    public static void clearTheRest(int position){
        for(int i=0;i<group.length;i++){
            group[i]=0;
        }
        group[position]=1;

        fillPage();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.settings, container, false);

        listView = (ListView) rootView.findViewById(R.id.set_places);
        context = rootView.getContext();

        //checkbox for wifi/any connection setting
        final CheckBox chk = (CheckBox) rootView.findViewById(R.id.chk);
        if(chk != null){
            chk.setChecked(wifiOnly);
            chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        wifiOnly = true;
                    } else {
                        wifiOnly = false;
                    }

                }
            });
        }
        fillPage();

        return rootView;
    }

    public static boolean getCheckBoxStatus(){
        return wifiOnly;
    }
}

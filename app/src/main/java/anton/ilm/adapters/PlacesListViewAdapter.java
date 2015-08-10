package anton.ilm.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

import anton.ilm.main.Main;
import anton.ilm.R;
import anton.ilm.objects.Place;

/**
 * Created by anton on 28.05.15.
 */
public class PlacesListViewAdapter extends BaseAdapter {

    private Context context;
    private List items;

    public PlacesListViewAdapter(Context context, List data) {
        super();
        this.context = context;
        this.items = data;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int pos) {
        return items.get(pos);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.single_place, null);
        }
        Place place = new Place();
        Object o = items.get(position);
        if(o instanceof Place){
            place = (Place) items.get(position);
        } else {
            Log.e("PlacesListViewAdapter", "Unrecognized object");
        }

        if (place != null) {
            TextView name = (TextView) v.findViewById(R.id.place_name);
            TextView phenomenon = (TextView) v.findViewById(R.id.ic_weather);
            TextView temp = (TextView) v.findViewById(R.id.place_temp);
            if (name != null) {
                name.setText(place.getName());
            }
            if (phenomenon != null) {
                Typeface font = Typeface.createFromAsset(context.getAssets(), "artill_clean_icons.ttf");
                phenomenon.setTypeface(font);
                phenomenon.setTextSize(60);
                phenomenon.setText(Main.weather_icons.get(place.getPhenomenon()).toString());
            }
            if (temp != null) {
                temp.setText(place.getTempInterval().toString()+context.getResources().getString(R.string.temp_unit));
            }
        }
        return v;
    }
}

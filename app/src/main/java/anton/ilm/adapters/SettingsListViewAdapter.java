package anton.ilm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;
import anton.ilm.main.Main;
import anton.ilm.R;
import anton.ilm.screenFragments.SettingsScreenFragment;

/**
 * Created by anton on 28.05.15.
 */
public class SettingsListViewAdapter extends BaseAdapter {

    private Context context;
    private String[] items;
    private int[] group;

    public SettingsListViewAdapter(Context context, String[] data, int[] group) {
        super();
        this.context = context;
        this.items = data;
        this.group = group;
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public String getItem(int pos) {
        return items[pos];
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.select_place, null);
        }
        final String place = items[position];
        if (place != null) {
            TextView name = (TextView) v.findViewById(R.id.place_name);
            final RadioButton choose = (RadioButton) v.findViewById(R.id.choose);
            if (name != null) {
                name.setText(place);
            }
            if(choose != null){
                //if group array contains status 0 for the radio button, we should uncheck it
                if(group[position]==0){
                    choose.setChecked(false);
                } else {
                    choose.setChecked(true);
                }
                choose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        choose.setChecked(true);
                        if(choose.isChecked()){
                            Main.setPlaceName(place);
                        } else {
                            Main.setPlaceName(null);
                        }
                        SettingsScreenFragment.clearTheRest(position);
                    }
                });
            }
        }
        return v;
    }
}

package anton.ilm.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import anton.ilm.main.Main;
import anton.ilm.R;
import anton.ilm.objects.Forecast;

/**
 * Created by anton on 29.05.15.
 */
public class ForecastsListViewAdapter extends BaseAdapter {

    private Context context;
    private List items;

    public ForecastsListViewAdapter(Context context, List data) {
        super();
        this.context = context;
        this.items = data;
    }

    @Override
    public int getCount() {
        // Auto-generated method stub
        return items.size();
    }

    @Override
    public Object getItem(int pos) {
        // Auto-generated method stub
        return items.get(pos);
    }

    @Override
    public long getItemId(int id) {
        // Auto-generated method stub
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.single_forecast, null);
        }
        Forecast forecast = new Forecast();
        Object o = items.get(position);
        if(o instanceof Forecast){
            forecast = (Forecast) items.get(position);
        } else {
            Log.e("ForecastsListViewAdapter", "Unrecognized object");
        }

        if(forecast != null){
            TextView date = (TextView) v.findViewById(R.id.forc_date);
            TextView weekday = (TextView) v.findViewById(R.id.forc_weekday);
            TextView phenomenon = (TextView) v.findViewById(R.id.ic_forc);
            TextView temp = (TextView) v.findViewById(R.id.forc_temp);
            TextView desc = (TextView) v.findViewById(R.id.forc_desc);

            if (date != null) {
                date.setText(forecast.getDate());
            }
            if (weekday != null) {
                SimpleDateFormat  dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                Calendar cal = Calendar.getInstance();
                Date convertedDate = new Date();
                try {
                    convertedDate = dateFormat.parse(forecast.getDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                cal.setTime(convertedDate);
                weekday.setText(dayOfWeek(cal.get(Calendar.DAY_OF_WEEK)));
            }
            if (phenomenon != null) {
                Typeface font = Typeface.createFromAsset(context.getAssets(), "artill_clean_icons.ttf");
                phenomenon.setTypeface(font);
                phenomenon.setTextSize(60);

                if(Main.isDay()) {
                    phenomenon.setText(Main.weather_icons.get(forecast.getPhenomenon()).toString());
                } else {
                    String weather_symbol = Main.weather_icons.get(forecast.getPhenomenon()).toString();
                    if(weather_symbol.equals("1")){
                        weather_symbol = "6";
                    } else if(weather_symbol.equals("2")){
                        weather_symbol = "a";
                    } else {
                        weather_symbol = weather_symbol.toLowerCase();

                    }
                    phenomenon.setText(weather_symbol);
                }
            }
            if (temp != null) {
                temp.setText(forecast.getTempInterval()+context.getResources().getString(R.string.temp_unit));
            }
            if (desc != null) {
                desc.setText(forecast.getDesc());
                desc.setVisibility(View.GONE);
            }
        }
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.findViewById(R.id.forc_desc).getVisibility() == View.GONE) {
                    v.findViewById(R.id.forc_desc).setVisibility(View.VISIBLE);
                } else {
                    v.findViewById(R.id.forc_desc).setVisibility(View.GONE);
                }
            }
        });
        return v;
    }

    private String dayOfWeek(int dayNumber){
        return context.getResources().getStringArray(R.array.weekday)[dayNumber-1];
    }

}

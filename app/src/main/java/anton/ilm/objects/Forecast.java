package anton.ilm.objects;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by anton on 22.05.15.
 */
public class Forecast {
    private final Date date;
    private final String tempInterval;
    private final String phenomenon;
    private final String desc;
    private final List places;
    private final String wind;

    public Forecast(){
        this.date = new Date();
        this.tempInterval = null;
        this.phenomenon = null;
        this.desc = null;
        this.places = null;
        this.wind = null;
    }

    public Forecast(Date date, String phenomenon, String tempInterval, String desc, List places, String wind){
        this.date = date;
        this.tempInterval = tempInterval;
        this.phenomenon = phenomenon;
        this.desc = desc;
        this.places = places;
        this.wind = wind;
    }

    public Forecast(Date date, String phenomenon, String tempInterval, String desc, List places){
        this.date = date;
        this.tempInterval = tempInterval;
        this.phenomenon = phenomenon;
        this.desc = desc;
        this.places = places;
        this.wind = null;
    }

    public String getDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String monthToShow = "";
        String dayToShow = "";
        int day = cal.get(Calendar.DATE);
        if(day<10){
            dayToShow = "0"+day;
        } else {
            dayToShow = ""+day;
        }
        int month = cal.get(Calendar.MONTH)+1; //because month begins with element 0
        if(month<10){
            monthToShow = "0"+month;
        } else {
            monthToShow = ""+month;
        }
        int year = cal.get(Calendar.YEAR);
        return dayToShow+"."+monthToShow+"."+year;
    }

    public String getTempInterval() {
        return tempInterval;
    }

    public String getPhenomenon() {
        return phenomenon;
    }

    public String getDesc() {
        return desc;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public String getWind() {
        return wind;
    }
}

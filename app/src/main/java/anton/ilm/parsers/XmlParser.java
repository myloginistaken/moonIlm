package anton.ilm.parsers;

import android.util.Log;
import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import anton.ilm.main.Main;
import anton.ilm.objects.DayOrNightElements;
import anton.ilm.objects.Forecast;
import anton.ilm.objects.IntPair;
import anton.ilm.objects.Place;

/**
 * Created by anton on 22.05.15.
 */
public class XmlParser {
    private static final String ns = null;

    public List parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readForecasts(parser);
        } finally {
            in.close();
        }
    }

    private List readForecasts(XmlPullParser parser) throws XmlPullParserException, IOException {
        List forecasts = new ArrayList();

        parser.require(XmlPullParser.START_TAG, ns, "forecasts");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("forecast")) {
                forecasts.add(readForecast(parser));
            } else {
                skip(parser);
            }
        }
        return forecasts;
    }

    private Forecast readForecast(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "forecast");
        //read date
        String target = parser.getAttributeValue(null, "date");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = df.parse(target);
        } catch (ParseException pe){
            pe.printStackTrace();
        }
        //read other elements
        DayOrNightElements day = new DayOrNightElements();
        DayOrNightElements night = new DayOrNightElements();
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if(name.equals("night")){
                night = readDayOrNight(parser, "night");
            } else if(name.equals("day")){
                day = readDayOrNight(parser, "day");
            } else {
                skip(parser);
            }

        }

        // Look through all the places temperatures and put night temperature as minimal and day temperature as maximum
        // then create temperature intervals to show. The xml is weird - that's why.
        List<Place> dayPlaces = day.getPlaces(); //get the day data about the places
        List<Place> nightPlaces = night.getPlaces(); //get the night data about the places
        List tempPlaces = new ArrayList(); //list to put combined temperature data (night and day are still different by the phenomenon)
        //all the lists have the same size
        for(int i=0; i<dayPlaces.size(); i++){
            Place place = dayPlaces.get(i);
            tempPlaces.add(new Place(place.getName(), place.getPhenomenon(), new IntPair(place.getTemp(), nightPlaces.get(i).getTemp())));
        }
        day.setPlaces(tempPlaces);
        night.setPlaces(tempPlaces);

        //return forecast for day
        if(Main.isDay()){
            if (!day.getWinds().isEmpty()) {
                return new Forecast(date, day.getPhenomenon(), day.getTemps(), day.getDesc(), day.getPlaces(), day.getMinMaxWind());
            } else {
                return new Forecast(date, day.getPhenomenon(), day.getTemps(), day.getDesc(), day.getPlaces());
            }
        //return forecast for night
        } else {
            if (!night.getWinds().isEmpty()) {
                return new Forecast(date, night.getPhenomenon(), night.getTemps(), night.getDesc(), night.getPlaces(), night.getMinMaxWind());
            } else {
                return new Forecast(date, night.getPhenomenon(), night.getTemps(), night.getDesc(), night.getPlaces());
            }
        }
    }

    private DayOrNightElements readDayOrNight(XmlPullParser parser, String dayOrNight) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, dayOrNight);
        int tempMax = 0;
        int tempMin = 0;
        String phenomenon = null;
        String desc = null;
        List places = new ArrayList();
        List winds = new ArrayList();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
             if (name.equals("tempmin")) {
                tempMin = readInt(parser, "tempmin");
            } else if (name.equals("tempmax")) {
                tempMax = readInt(parser, "tempmax");
            } else if (name.equals("phenomenon")) {
                phenomenon = readPhenomenon(parser);
            }else if(name.equals("text")){
                desc = readText(parser);
            } else if(name.equals("place")){
                places.add(readPlace(parser));
             }else if(name.equals("wind")){
                winds.add(readWind(parser));
            }else {
                skip(parser);
            }
        }
        return new DayOrNightElements(tempMax, tempMin, phenomenon, desc, places, winds);
    }

    private String readPhenomenon(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "phenomenon");
        String phenomenon = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "phenomenon");
        return phenomenon;
    }

    private Place readPlace(XmlPullParser parser) throws IOException, XmlPullParserException {
        String placeName = null;
        String phenomenon = null;
        int tempMin = 0;
        int tempMax = 0;
        int temp;

        parser.require(XmlPullParser.START_TAG, ns, "place");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("name")) {
                placeName = readText(parser);
            } else if(name.equals("phenomenon")){
                phenomenon = readText(parser);
            } else if(name.equals("tempmin")){
                tempMin = readInt(parser, "tempmin");
            } else if(name.equals("tempmax")){
                tempMax = readInt(parser, "tempmax");
            } else {
                skip(parser);
            }
        }
        if(tempMin==0){
            temp = tempMax;
        } else {
            temp = tempMin;
        }
        return new Place(placeName, phenomenon, temp);
    }

    private IntPair readWind(XmlPullParser parser) throws IOException, XmlPullParserException {
        int maxSpeed = 0;
        int minSpeed = 0;

        parser.require(XmlPullParser.START_TAG, ns, "wind");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("speedmin")) {
                minSpeed = readInt(parser, "speedmin");
            } else if(name.equals("speedmax")){
                maxSpeed = readInt(parser, "speedmax");
            }else {
                skip(parser);
            }
        }
        return new IntPair(maxSpeed, minSpeed);
    }

    private int readInt(XmlPullParser parser, String tagName) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, tagName);
        int value = Integer.parseInt(readText(parser));
        parser.require(XmlPullParser.END_TAG, ns, tagName);
        return value;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}

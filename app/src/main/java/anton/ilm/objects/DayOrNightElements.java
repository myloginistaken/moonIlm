package anton.ilm.objects;

import java.util.List;

import anton.ilm.objects.IntPair;

/**
 * Created by anton on 24.05.15.
 */
public class DayOrNightElements {
    private final IntPair temps;
    private final String phenomenon;
    private final String desc;
    private final List winds;
    private List places;

    public DayOrNightElements(){
        this.temps = null;
        this.phenomenon = null;
        this.desc = null;
        this.places = null;
        this.winds = null;
    }

    public DayOrNightElements(int tempMax, int tempMin, String phenomenon, String desc, List places, List winds){
        this.temps = new IntPair(tempMax, tempMin);
        this.phenomenon = phenomenon;
        this.desc = desc;
        this.places = places;
        this.winds = winds;
    }

     private int findMax(List<IntPair> winds){
        int max = winds.get(0).max;
        for(int i=0; i<winds.size(); i++){
            if(max<winds.get(i).max){
                max = winds.get(i).max;
            }
        }
        return max;
    }

    private int findMin(List<IntPair> winds){
        int min = winds.get(0).min;
        for(int i=0; i<winds.size(); i++){
            if(min>winds.get(i).min){
                min = winds.get(i).min;
            }
        }
        return min;
    }

    public String getTemps() {
        return temps.toString();
    }

    public String getPhenomenon() {
        return phenomenon;
    }

    public String getDesc() {
        return desc;
    }

    public List getWinds() {
        return winds;
    }

    public List getPlaces() {
        return places;
    }

    public void setPlaces(List places) {
        this.places = places;
    }

    public String getMinMaxWind(){
        return findMin(winds) + " — " + findMax(winds);
    }
}

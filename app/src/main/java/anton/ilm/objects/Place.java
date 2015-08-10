package anton.ilm.objects;

/**
 * Created by anton on 24.05.15.
 */
public class Place {
    private final String name;
    private final String phenomenon;
    private final int temp;
    private final IntPair tempInterval;

    public Place(){
        this.name = null;
        this.phenomenon = null;
        this.temp = 0;
        this.tempInterval = null;
    }

    public Place(String name, String phenomenon, int temp){
        this.name = name;
        this.phenomenon = phenomenon;
        this.temp = temp;
        this.tempInterval = null;
    }

    public Place(String name, String phenomenon, IntPair tempInterval){
        this.name = name;
        this.phenomenon = phenomenon;
        this.temp = 0;
        this.tempInterval = tempInterval;
    }

    public String getName() {
        return name;
    }

    public String getPhenomenon() {
        return phenomenon;
    }

    public int getTemp() {
        return temp;
    }

    public IntPair getTempInterval() {
        return tempInterval;
    }
}

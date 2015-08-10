package anton.ilm.objects;


/**
 * Created by anton on 24.05.15.
 */
public class IntPair {
    public final int max;
    public final int min;

    public IntPair(int max, int min){
        this.max = max;
        this.min = min;
    }

    public String toString(){
        return min+" — "+max;
    }
}

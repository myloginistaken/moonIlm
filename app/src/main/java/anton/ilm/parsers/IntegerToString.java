package anton.ilm.parsers;

import android.content.Context;

import anton.ilm.R;

/**
 * Created by anton on 3.06.15.
 */
public class IntegerToString {

    private static Context context;

    public IntegerToString(Context context){
        this.context = context;
    }

    private static String parseDigit(int digit){
        String result = "";
        if(digit < 10 && digit >=0){
            result = context.getResources().getStringArray(R.array.digits)[digit];
            }
        return result;
    }
    //parse 2 digit number
    private static String parseNumber(String number){
        String result = "";
        int i = 0;
        if(1 == number.length()){
            return parseDigit(Integer.parseInt(number));
        } else if(2 == number.length()){
            if(number.charAt(i) == '1' && number.charAt(i+1) == '0'){
                return context.getResources().getStringArray(R.array.tens)[0];
            } else if(number.charAt(i) == '1'){
                result += parseDigit(Character.getNumericValue(number.charAt(i+1)))+context.getResources().getString(R.string.teist);
            } else if(number.charAt(i) == '2' && number.charAt(i+1) == '0'){
                return context.getResources().getStringArray(R.array.tens)[1];
            } else if(number.charAt(i) == '2'){
                result += context.getResources().getStringArray(R.array.tens)[1]+" "
                        +parseDigit(Character.getNumericValue(number.charAt(i+1)));
            } else if(number.charAt(i) == '3' && number.charAt(i+1) == '0'){
                return context.getResources().getStringArray(R.array.tens)[2];
            } else if(number.charAt(i) == '3'){
                result += context.getResources().getStringArray(R.array.tens)[2]+" "
                        +parseDigit(Character.getNumericValue(number.charAt(i+1)));
            } else if(number.charAt(i) == '4' && number.charAt(i+1) == '0'){
                return context.getResources().getStringArray(R.array.tens)[3];
            } else if(number.charAt(i) == '4'){
                result += context.getResources().getStringArray(R.array.tens)[3]+" "
                        +parseDigit(Character.getNumericValue(number.charAt(i+1)));
            }
        }
        return result;
    }

    public static String parseValue(String value) {
        value = value.replace(" ", "");
        String result = "";
        String number = "";
        for (int i = 0; i < value.length(); i++) {
            if (value.charAt(i) == '-') {
                result += context.getResources().getString(R.string.minus);
            }
            if(value.charAt(i) == '—'){
                result += parseNumber(number);
                result += context.getResources().getString(R.string.kuni);
                number = "";
            }
            if(Character.isDigit(value.charAt(i))){
                number += value.charAt(i);
            }
            if(i+1==value.length()){
                result += parseNumber(number);
            }
        }
        return result;
    }
}

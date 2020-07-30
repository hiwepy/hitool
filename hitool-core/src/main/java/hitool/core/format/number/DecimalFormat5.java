package hitool.core.format.number;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
public class DecimalFormat5 {   
    public static void main(String args[]) {   
        // 本地格式   
        NumberFormat nf1 = NumberFormat.getInstance();   
        Object obj1 = null;   
        // 基于格式的解析   
        try {   
            obj1 = nf1.parse("1234,56");   
        }   
        catch (ParseException e1) {   
            System.err.println(e1);   
        }   
        System.out.println(obj1);   
        // 德国格式   
        NumberFormat nf2 =   
            NumberFormat.getInstance(Locale.GERMAN);   
        Object obj2 = null;   
        // 基于格式的解析   
        try {   
            obj2 = nf2.parse("1234,56");   
        }   
        catch (ParseException e2) {   
            System.err.println(e2);   
        }   
        System.out.println(obj2);   
    }   
}   
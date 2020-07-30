package hitool.core.format.number;

import java.text.DecimalFormat;
import java.util.Locale;
public class DecimalFormat2 {   
    public static void main(String args[]) {   
        // 得到本地的缺省格式   
        DecimalFormat df1 = new DecimalFormat("####.000");   
        System.out.println(df1.format(1234.56));   
        // 得到德国的格式   
        Locale.setDefault(Locale.GERMAN);   
        DecimalFormat df2 = new DecimalFormat("####.000");   
        System.out.println(df2.format(1234.56));   
    }   
}   
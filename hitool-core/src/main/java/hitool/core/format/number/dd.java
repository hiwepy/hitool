package hitool.core.format.number;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
public class dd {    
    public static void main(String args[]) {    
        DecimalFormat df = null;    
        // 得到一个NumberFormat 对象并    
        // 强制转换为一个 DecimalFormat 对象    
        try {    
            df = (DecimalFormat)    
                NumberFormat.getInstance(Locale.GERMAN);    
        }    
        catch (ClassCastException e) {    
            System.err.println(e);    
        }    
        // 设置格式模式    
        df.applyPattern("####.00000");    
        // format a number    
        System.out.println(df.format(1234.56));    
    }    
}   
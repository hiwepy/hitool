package hitool.core.format.number;

import java.text.NumberFormat;
public class DecimalFormat4 {    
    public static void main(String args[]) {    
        NumberFormat nf = NumberFormat.getPercentInstance();    
        System.out.println(nf.format(0.47));    
    }    
}  
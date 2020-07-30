package hitool.core.format.number;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
public class DecimalFormat1 {    
	
    public static void main(String args[]) {  
    	
    	NumberFormat nf = (NumberFormat)DecimalFormat.getInstance();  
    	if (nf instanceof NumberFormat) {  
    	                 ((DecimalFormat) nf).applyPattern("#.##");//最多两位小数  
    	             }  
    	//nf.format(double类型数字); 
    	
        // 得到本地的缺省格式    
        NumberFormat nf1 = NumberFormat.getInstance();    
        System.out.println(nf1.format(1234.56));    
        // 得到德国的格式    
        NumberFormat nf2 =    
            NumberFormat.getInstance(Locale.GERMAN);    
        System.out.println(nf2.format(1234.56));    
    }    
} 
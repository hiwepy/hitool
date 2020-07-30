package hitool.core.format.number;


import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Locale;

import hitool.core.format.Format;
/** 
 * 使用舍入模式的格式化操作
 */
public class IntegerFormat implements Format {
	
	private String fmt;
	private Locale locale;
	public IntegerFormat(String fmt){
		this.fmt = fmt;
	}
	
	public IntegerFormat(Locale locale){
		this.locale = locale;
	}
	
	public IntegerFormat(String fmt,Locale locale){
		this.fmt = fmt;
		this.locale = locale;
	}
	
    public static void main(String args[]){

        double pi = 3.1415927;//pi   
        // 取一位整数   
        System.out.println(new DecimalFormat("0").format(pi));// 3   
        // 取一位整数和两位小数   
        System.out.println(new DecimalFormat("0.00").format(pi));// 3.14   
        // 取两位整数和三位小数，整数不足部分以0填补。   
        System.out.println(new DecimalFormat("00.000").format(pi));// 03.142   
        // 取所有整数部分   
        System.out.println(new DecimalFormat("#").format(pi));// 3   
        // 以百分比方式计数，并取两位小数   
        System.out.println(new DecimalFormat("#.##%").format(pi));// 314.16%   
  
        long c = 299792458;   
        // 显示为科学计数法，并取五位小数   
        System.out.println(new DecimalFormat("#.#####E0").format(c));// 2.99792E8   
        // 显示为两位整数的科学计数法，并取四位小数   
        System.out.println(new DecimalFormat("00.####E0").format(c));// 29.9792E7   
        // 每三位以逗号进行分隔。   
        System.out.println(new DecimalFormat(",###").format(c));// 299,792,458   
        // 将格式嵌入文本   
        System.out.println(new DecimalFormat("光速大小为每秒,###米。").format(c));  
        
    	//String fmtString = "###.######"; //16bit
        //IntegerFormat format = new IntegerFormat(Locale.CANADA);
        
        //System.out.println(format.format("12312303322"));
        //System.out.println(format.doubleOutPut(12.3959, 2));
        //System.out.println(format.roundFormat(12.335, 2));
        

            // 取一位整数  
            System.out.println(new DecimalFormat("0").format(pi));// 3  
            // 取一位整数和两位小数  
            System.out.println(new DecimalFormat("0.00").format(pi));// 3.14  
            // 取两位整数和三位小数，整数不足部分以0填补。  
            System.out.println(new DecimalFormat("00.000").format(pi));// 03.142  
            // 取所有整数部分  
            System.out.println(new DecimalFormat("#").format(pi));// 3  
            // 以百分比方式计数，并取两位小数  
            System.out.println(new DecimalFormat("#.##%").format(pi));// 314.16%  
      
            // 显示为科学计数法，并取五位小数  
            System.out.println(new DecimalFormat("#.#####E0").format(c));// 2.99792E8  
            // 显示为两位整数的科学计数法，并取四位小数  
            System.out.println(new DecimalFormat("00.####E0").format(c));// 29.9792E7  
            // 每三位以逗号进行分隔。  
            System.out.println(new DecimalFormat(",###").format(c));// 299,792,458  
            // 将格式嵌入文本  
            System.out.println(new DecimalFormat("光速大小为每秒,###米。").format(c));
            
            DecimalFormat df = new DecimalFormat();
            double data = 1234.56789;
            System.out.println("格式化之前的数字: " + data);
            String style = "0.0";//定义要显示的数字的格式
            df.applyPattern(style);// 将格式应用于格式化器
            System.out.println("采用style: " + style + "格式化之后: " + df.format(data));
            style = "00000.000 kg";//在格式后添加诸如单位等字符
            df.applyPattern(style);
            System.out.println("采用style: " + style + "格式化之后: " + df.format(data));
            // 模式中的"#"表示如果该位存在字符，则显示字符，如果不存在，则不显示。
            style = "##000.000 kg";
            df.applyPattern(style);
            System.out.println("采用style: " + style + "格式化之后: " + df.format(data));
            // 模式中的"-"表示输出为负数，要放在最前面
            style = "-000.000";
            df.applyPattern(style);
            System.out.println("采用style: " + style + "格式化之后: " + df.format(data));
            // 模式中的","在数字中添加逗号，方便读数字
            style = "-0,000.0#";
            df.applyPattern(style);
            System.out.println("采用style: " + style + "格式化之后: " + df.format(data));
            // 模式中的"E"表示输出为指数，"E"之前的字符串是底数的格式，
            // "E"之后的是字符串是指数的格式
            style = "0.00E000";
            df.applyPattern(style);
            System.out.println("采用style: " + style + "格式化之后: " + df.format(data));
            // 模式中的"%"表示乘以100并显示为百分数，要放在最后。
            style = "0.00%";
            df.applyPattern(style);
            System.out.println("采用style: " + style + "格式化之后: " + df.format(data));
            // 模式中的"\u2030"表示乘以1000并显示为千分数，要放在最后。
            style = "0.00\u2030";
            //在构造函数中设置数字格式
            DecimalFormat df1 = new DecimalFormat(style);
            //df.applyPattern(style);
            System.out.println("采用style: " + style + "格式化之后: " + df1.format(data));
	            
    } 
    
    public String doubleOutPut(double v,Integer num){
        if( v == Double.valueOf(v).intValue()){
            return Double.valueOf(v).intValue() + "";
        }else{
            BigDecimal b = new BigDecimal(Double.toString(v));
            return b.setScale(num,BigDecimal.ROUND_HALF_UP).toString();
        }
    }
    
    public String roundFormat(double v,int num){
        String fmtString = "0000000000000000"; //16bit
        fmtString = num>0 ? "0." + fmtString.substring(0,num):"0";
        DecimalFormat dFormat = new DecimalFormat(fmtString);
        return dFormat.format(v);
    }

	@Override
	public <T> String format(String message, T bean) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String format(String message, String... replaceParas) {
		// TODO Auto-generated method stub
		return null;
	}
    
	@Override
	public String format(String message, String regex, String[] replaceParas) {
		return regex;
		
		 // TODO Auto-generated method stub return null;
		
	}
    
}
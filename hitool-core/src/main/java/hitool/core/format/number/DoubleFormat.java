package hitool.core.format.number;


import java.math.BigDecimal;
import java.text.DecimalFormat;
/**
 *使用舍入模式的格式化操作
 **/
public class DoubleFormat {
    public static void main(String args[]){
        DoubleFormat format = new DoubleFormat();
        System.out.println(format.doubleOutPut(12.345, 2));
        System.out.println(format.roundNumber(12.335, 2));
    }
    public String doubleOutPut(double v,Integer num){
        if( v == Double.valueOf(v).intValue()){
            return Double.valueOf(v).intValue() + "";
        }else{
            BigDecimal b = new BigDecimal(Double.toString(v));
            return b.setScale(num,BigDecimal.ROUND_HALF_UP).toString();
        }
    }
    public String roundNumber(double v,int num){
        String fmtString = "0000000000000000"; //16bit
        fmtString = num>0 ? "0." + fmtString.substring(0,num):"0";
        DecimalFormat dFormat = new DecimalFormat(fmtString);
        return dFormat.format(v);
    }
}
package hitool.core.format.number;


import java.text.NumberFormat;

public class DefaultNumberFormat
{
	public static void defaultNumberFormat()
	{

		int i = 123456;

		double x = 882323.23523;

		double p = 0.528;

		double c = 52.83;
		
		System.out.println("--------------------------------------");

		//实例化NumberFormat需要使用NumberFormat的getInstance()方法
		NumberFormat nf = NumberFormat.getInstance();
		//默认格式化int类型，结果已分节
		//结果：Integer 123456 is displayed as 123,456
		System.out.println("Integer " + i + " is displayed as " + nf.format(i));
		//默认格式化double类型，结果保留三位小数，已分节
		//结果：Double 882323.23523 is displayed as 882,323.235
		System.out.println("Double " + x + " is displayed as " + nf.format(x));
		
		System.out.println("--------------------------------------");

		//实例化为整型数据格式化
		NumberFormat nfInt = NumberFormat.getIntegerInstance();
		//默认，结果：Integer 123456 is displayed as 123,456
		System.out.println("Integer " + i + " is displayed as "
				+ nfInt.format(i));
		
		System.out.println("--------------------------------------");

		//实例化为数据格式化
		NumberFormat nfNumber = NumberFormat.getNumberInstance();
		//结果：Double 882323.23523 is displayed as 882,323.235
		System.out.println("Double " + x + " is displayed as "
				+ nfNumber.format(x));
		
		System.out.println("--------------------------------------");

		//百分数格式化
		NumberFormat nfPercent = NumberFormat.getPercentInstance();
		//结果已四舍五入
		//结果：Percent 0.528 is displayed as 53%
		System.out.println("Percent " + p + " is displayed as "
				+ nfPercent.format(p));

		System.out.println("--------------------------------------");
		
		//货币型格式化
		NumberFormat nfCurrency = NumberFormat.getCurrencyInstance();
		//结果：Currency 52.83 is displayed as ￥52.83
		System.out.println("Currency " + c + " is displayed as "
				+ nfCurrency.format(c));
	}
	
	public static void main(String[] args)
	{
		defaultNumberFormat();
		System.out.println();
	}

}


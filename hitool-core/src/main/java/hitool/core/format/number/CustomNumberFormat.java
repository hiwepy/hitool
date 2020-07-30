package hitool.core.format.number;


import java.text.DecimalFormat;

public class CustomNumberFormat
{

	public static void customNumberFormat()
	{

		double x = 100000.0 / 3;

		// default output is 33333.333333333336
		System.out.println("default output is " + x);
		// 33,333.33
		System.out.println(new DecimalFormat("###,###.##").format(x));
		// 33333.33
		System.out.println(new DecimalFormat("####.##").format(x));
		// 33333.33
		System.out.println(new DecimalFormat("####.00").format(x));
		// 33333.33
		System.out.println(new DecimalFormat("####.0#").format(x));
		// 0033333.33
		System.out.println(new DecimalFormat("0000000.##").format(x));
		// $33,333.33
		System.out.println(new DecimalFormat("$###,###.##").format(x));
		// 3.333E4
		System.out.println(new DecimalFormat("0.###E0").format(x));
		// 3333333.33%
		System.out.println(new DecimalFormat("00.##%").format(x));

		System.out.println("--------------------------------------");

		double y = 23232323.0012;
		//default output is 2.32323230012E7
		System.out.println("default output is " + y);
		//23,232,323
		System.out.println(new DecimalFormat("###,###.##").format(y));
		//23232323
		System.out.println(new DecimalFormat("####.##").format(y));
		//23232323.00
		System.out.println(new DecimalFormat("####.00").format(y));
		//23232323.0
		System.out.println(new DecimalFormat("####.0#").format(y));
		//0023232323
		System.out.println(new DecimalFormat("0000000000.##").format(y));
		//$23,232,323
		System.out.println(new DecimalFormat("$###,###.##").format(y));
		//2.323E7
		System.out.println(new DecimalFormat("0.###E0").format(y));
		//2323232300.12%
		System.out.println(new DecimalFormat("00.##%").format(y));
	}

	public static void main(String[] args)
	{
		customNumberFormat();
		System.out.println();
	}

}


package hitool.core.format;

import java.util.Date;

import hitool.core.format.annotations.Formater;

public class TestSS {

	@Formater("您好我是{0}！")
	private String filed_1;
	
	@Formater("yyyy-MM-dd")
	private Date filed_2;

	public TestSS(String filed_1, Date filed_2) {
		super();
		this.filed_1 = filed_1;
		this.filed_2 = filed_2;
	}

	public String getFiled_1() {

		return filed_1;
	}

	public void setFiled_1(String filed_1) {

		this.filed_1 = filed_1;
	}

	public Date getFiled_2() {

		return filed_2;
	}

	public void setFiled_2(Date filed_2) {
		this.filed_2 = filed_2;
	}

}

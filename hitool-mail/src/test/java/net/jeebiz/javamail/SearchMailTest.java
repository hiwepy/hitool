package net.jeebiz.javamail;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.mail.search.AndTerm;
import javax.mail.search.BodyTerm;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.FromStringTerm;
import javax.mail.search.IntegerComparisonTerm;
import javax.mail.search.NotTerm;
import javax.mail.search.OrTerm;
import javax.mail.search.SearchTerm;
import javax.mail.search.SentDateTerm;
import javax.mail.search.SizeTerm;
import javax.mail.search.SubjectTerm;

/**
 * 搜索邮件
 */
public class SearchMailTest {

	public static void main(String[] args) throws Exception {
		Properties props = new Properties();
		props.setProperty("mail.pop3.auth", "true");
		Session session = Session.getInstance(props);
		URLName url = new URLName("pop3", "pop3.163.com", 110, null,
				"xyang81@163.com", "yX546900873");
		Store store = session.getStore(url);
		store.connect();
		// 得到收件箱
		Folder folder = store.getFolder("INBOX");
		// 以读写模式打开收件箱
		folder.open(Folder.READ_WRITE);

		Message[] messages = search(folder);

		System.out.println("收件箱中共有:" + folder.getMessageCount() + "封邮件，搜索到"
				+ messages.length + "封符合条件的邮件!");

		// 解析邮件搜索到的邮件
		POP3ReceiveMailTest.parseMessage(messages);

		// 根据用户输入的条件搜索所有邮件,并提示用户是否删除
		// searchDemo(folder);

		folder.close(true);
		store.close();
	}

	public static Message[] search(Folder folder) throws Exception {
		// 搜索主题包含美食的邮件
		String subject = "java培训";
		SearchTerm subjectTerm = new SubjectTerm(subject);

		// 搜索发件人包含支付宝的邮件
		SearchTerm fromTerm = new FromStringTerm("支付宝");

		// 搜索邮件内容包含"招聘"的邮件
		SearchTerm bodyTerm = new BodyTerm("招聘");

		// 搜索发件人不包含“智联招聘”的邮件
		SearchTerm notTerm = new NotTerm(new FromStringTerm("智联招聘"));

		// 搜索发件人为“智联招聘”，而且内容包含“Java工程师“的邮件
		SearchTerm andTerm = new AndTerm(new FromStringTerm("智联招聘"), new BodyTerm("java工程师"));

		// 搜索发件人为”智联招聘“或主题包含”最新职位信息“的邮件
		SearchTerm orTerm = new OrTerm(new FromStringTerm("智联招聘"), new SubjectTerm("最新职位信息"));

		// 搜索周一到今天收到的的所有邮件
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_WEEK, calendar.get(Calendar.DAY_OF_WEEK - (Calendar.DAY_OF_WEEK - 1)) - 1);
		Date mondayDate = calendar.getTime();
		SearchTerm comparisonTermGe = new SentDateTerm(ComparisonTerm.GE, mondayDate);
		SearchTerm comparisonTermLe = new SentDateTerm(ComparisonTerm.LE, new Date());
		SearchTerm comparisonAndTerm = new AndTerm(comparisonTermGe, comparisonTermLe);

		// 搜索大于或等100KB的所有邮件
		int mailSize = 1024 * 100;
		SearchTerm intComparisonTerm = new SizeTerm(IntegerComparisonTerm.GE, mailSize);

		return folder.search(intComparisonTerm);
	}

	/**
	 * 根据用户输入的收件人地址（包括email地址和姓名）和主题作为搜索条件，并提示用户是否删除搜索到的邮件
	 * 
	 * @param from
	 *            收件人
	 * @param subject
	 *            主题
	 */
	public static void searchDemo(Folder folder) throws Exception {
		String notifyMsg = "收件箱中一共有" + folder.getMessageCount()
				+ "封邮件。请选择操作：\n";
		notifyMsg += "1、输入收件人\n" + "2、输入主题\n" + "3、开始搜索\n" + "4、退出";
		System.out.println(notifyMsg);
		String from = null;
		String subject = null;
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));
		String oper = reader.readLine().trim();
		while (!(from != null && subject != null)) {
			if ("4".equals(oper)) {
				System.exit(0);
			} else {
				if ("1".equals(oper)) {
					System.out.print("请输入收件人：");
					from = reader.readLine();
				} else if ("2".equals(oper)) {
					System.out.print("请输入主题：");
					subject = reader.readLine();
				} else if ("3".equals(oper)) {
					if (from == null || subject == null) {
						System.out.println("未输入搜索条件，无法进行搜索！");
					} else {
						break;
					}
				}
			}
			System.out.print("请选择操作：");
			oper = reader.readLine().trim();
		}

		System.out.println("\n系统正在根据搜索条件查询所有邮件，请稍候......\n");

		// 根据输入的条件，创建SearchTerm实例对象
		SearchTerm orTerm = new OrTerm(new FromStringTerm(from),
				new SubjectTerm(subject));

		// 根据搜索条件得到搜索到的邮件列表
		Message[] messages = folder.search(orTerm);

		System.out.println("共搜索到" + messages.length
				+ "封满足搜索条件的邮件！\n\n请选择操作：1、查看邮件\t 2、删除所有邮件");

		String deleteQuestion = "是否要删除搜索到的邮件?(yes/no)";

		String searchResultOper = reader.readLine();

		if ("1".equals(searchResultOper)) {
			for (Message message : messages) {
				MimeMessage msg = (MimeMessage) message;
				String sub = POP3ReceiveMailTest.getSubject(msg);
				System.out.println("开始查看第" + msg.getMessageNumber() + "封邮件...");
				System.out.println("主题: " + sub);
				System.out.println("发件人: " + POP3ReceiveMailTest.getFrom(msg));
				System.out.println("收件人：" + POP3ReceiveMailTest.getReceiveAddress(msg, null));
				System.out.println("发送时间：" + POP3ReceiveMailTest.getSentDate(msg, null));
				System.out.println(deleteQuestion);
				String answer = reader.readLine();
				if ("yes".equals(answer)) {
					msg.setFlag(Flag.DELETED, true);
					System.out.println("邮件[" + sub + "]删除成功!");
				} else if ("no".equals(answer)) {
					System.out.println("第" + msg.getMessageNumber()
							+ "封邮件查看完成！");
				} else if ("stop".equals(answer)) {
					System.exit(0);
				}
				System.out.println();
			}
		} else {
			System.out.println(deleteQuestion);
			String answer = reader.readLine();
			if ("yes".equals(answer)) {
				for (Message message : messages) {
					String sub = MimeUtility.decodeText(message.getSubject());
					message.setFlag(Flag.DELETED, true);
					System.out.println("邮件[" + sub + "]删除成功!");
				}
			}
		}
	}

}
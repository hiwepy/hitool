/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.lang3.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * 
 * 1、获取本机mac 2、获取远程主机mac:
 * 主机A向主机B发送“UDP－NetBIOS－NS”询问包，即向主机B的137端口，发Query包来询问主机B的NetBIOS Names信息。
 * 其次，主机B接收到“UDP－NetBIOS－NS”询问包， 假设主机B正确安装了NetBIOS服务........... 而且137端口开放，
 * 则主机B会向主机A发送一个“UDP－NetBIOS－NS”应答包，即发Answer包给主机A。 并利用UDP(NetBIOS Name
 * Service)来快速获取远程主机MAC地址的方法
 */
public class MacAddressUtils {

	protected static Logger LOG = LoggerFactory.getLogger(MacAddressUtils.class);
	protected static String UNKNOWN_MAC_ADDRESS = "Unknown Mac Address";
	private static int remotePort = 137;
	private static byte[] buffer = new byte[1024];
	private static DatagramSocket ds = null;

	static {
		try {
			ds = new DatagramSocket();
		} catch (SocketException e) {

		}
	}

	// 发送数据包
	protected static final DatagramPacket send(String remoteAddr, byte[] bytes) throws IOException {
		DatagramPacket dp = new DatagramPacket(bytes, bytes.length, InetAddress.getByName(remoteAddr), remotePort);
		ds.send(dp);
		return dp;
	}

	// 接收数据包
	protected static final DatagramPacket receive() {
		DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
		try {
			ds.setSoTimeout(3000);
			ds.receive(dp);
		} catch (SocketTimeoutException ex) {
			LOG.error("接收数据超时...,不能获取客户端MAC地址", ex);
		} catch (SocketException e1) {
			LOG.error("发生Sorcket异常...", e1);
		} catch (IOException e2) {
			LOG.error("发生IO异常...", e2);
		}
		return dp;
	}

	// 询问包结构:
	// Transaction ID 两字节（16位） 0x00 0x00
	// Flags 两字节（16位） 0x00 0x10
	// Questions 两字节（16位） 0x00 0x01
	// AnswerRRs 两字节（16位） 0x00 0x00
	// AuthorityRRs 两字节（16位） 0x00 0x00
	// AdditionalRRs 两字节（16位） 0x00 0x00
	// Name:array [1..34] 0x20 0x43 0x4B 0x41(30个) 0x00 ;
	// Type:NBSTAT 两字节 0x00 0x21
	// Class:INET 两字节（16位）0x00 0x01
	protected static final byte[] getQueryCmd() throws Exception {
		byte[] t_ns = new byte[50];
		t_ns[0] = 0x00;
		t_ns[1] = 0x00;
		t_ns[2] = 0x00;
		t_ns[3] = 0x10;
		t_ns[4] = 0x00;
		t_ns[5] = 0x01;
		t_ns[6] = 0x00;
		t_ns[7] = 0x00;
		t_ns[8] = 0x00;
		t_ns[9] = 0x00;
		t_ns[10] = 0x00;
		t_ns[11] = 0x00;
		t_ns[12] = 0x20;
		t_ns[13] = 0x43;
		t_ns[14] = 0x4B;

		for (int i = 15; i < 45; i++) {
			t_ns[i] = 0x41;
		}

		t_ns[45] = 0x00;
		t_ns[46] = 0x00;
		t_ns[47] = 0x21;
		t_ns[48] = 0x00;
		t_ns[49] = 0x01;
		return t_ns;
	}

	// 表1 “UDP－NetBIOS－NS”应答包的结构及主要字段一览表
	// 序号 字段名 长度
	// 1 Transaction ID 两字节（16位）
	// 2 Flags 两字节（16位）
	// 3 Questions 两字节（16位）
	// 4 AnswerRRs 两字节（16位）
	// 5 AuthorityRRs 两字节（16位）
	// 6 AdditionalRRs 两字节（16位）
	// 7 Name<Workstation/Redirector> 34字节（272位）
	// 8 Type:NBSTAT 两字节（16位）
	// 9 Class:INET 两字节（16位）
	// 10 Time To Live 四字节（32位）
	// 11 Length 两字节（16位）
	// 12 Number of name 一个字节（8位）
	// NetBIOS Name Info 18×Number Of Name字节
	// Unit ID 6字节（48位

	protected static final String getMacAddr(byte[] brevdata) throws Exception {
		// 获取计算机名
		// System.out.println(new String(brevdata, 57, 18));
		// System.out.println(new String(brevdata, 75, 18));
		// System.out.println(new String(brevdata, 93, 18));
		int i = brevdata[56] * 18 + 56;
		String sAddr = "";
		StringBuffer sb = new StringBuffer(17);
		// 先从第56字节位置，读出Number Of Names（NetBIOS名字的个数，其中每个NetBIOS Names
		// Info部分占18个字节）
		// 然后可计算出“Unit ID”字段的位置＝56＋Number Of
		// Names×18，最后从该位置起连续读取6个字节，就是目的主机的MAC地址。
		for (int j = 1; j < 7; j++) {
			sAddr = Integer.toHexString(0xFF & brevdata[i + j]);
			if (sAddr.length() < 2) {
				sb.append(0);
			}
			sb.append(sAddr.toUpperCase());
			if (j < 6) {
				sb.append('-');
			}
		}
		return sb.toString();
	}

	public static final void close() {
		try {
			ds.close();
		} catch (Exception ex) {
			LOG.error(ex.getMessage());
		}
	}

	/*
	 * 获取远程主机的mac地址
	 * 
	 * @param remoteIPAddr
	 * @return
	 * @throws Exception
	 */
	public static final String getRemoteMacAddr(String remoteIPAddr) {
		try {
			byte[] bqcmd = getQueryCmd();
			send(remoteIPAddr, bqcmd);
			DatagramPacket dp = receive();
			String smac = "";
			smac = getMacAddr(dp.getData());
			close();
			return smac;
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
		return UNKNOWN_MAC_ADDRESS;
	}

	// -------------------------------------------------------

	/*
	 * 获取当前操作系统名称. return 操作系统名称 例如:windows,Linux,Unix等
	 * @return
	 */
	public static String getOSName() {
		return System.getProperty("os.name").toLowerCase();
	}

	/*
	 * 获取widnowXp网卡的mac地址
	 * 
	 * @param execStr
	 * @return
	 */
	public static String getWindowXPMacAddress(String execStr) {
		String mac = null;
		BufferedReader reader = null;
		Process process = null;
		try {
			// windows下的命令，显示信息中包含有mac地址信息
			process = Runtime.getRuntime().exec(execStr);
			reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			int index = -1;
			while ((line = reader.readLine()) != null) {
				// 排除有虚拟网卡的情况
				if (line.indexOf("本地连接") != -1) {
					continue;
				}
				// 寻找标示字符串[physical address]
				index = line.toLowerCase().indexOf("physical address");
				if (index != -1) {
					index = line.indexOf(":");
					if (index != -1) {
						// 取出mac地址并去除2边空格
						mac = line.substring(index + 1).trim();
					}
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
				process.destroy();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			reader = null;
			process = null;
		}
		return mac;
	}

	/*
	 * 获取widnow7网卡的mac地址
	 * 
	 * @return ：
	 */
	public static String getWindow7MacAddress() {
		// 获得网络接口对象（即网卡），并得到mac地址，mac地址存在于一个byte数组中。
		byte[] mac = null;
		try {
			mac = NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getHardwareAddress();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		// 下面代码是把mac地址拼装成String
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < mac.length; i++) {
			if (i != 0) {
				sb.append("-");
			}
			// mac[i] & 0xFF 是为了把byte转化为正整数
			String s = Integer.toHexString(mac[i] & 0xFF);
			sb.append(s.length() == 1 ? 0 + s : s);
		}
		// 把字符串所有小写字母改为大写成为正规的mac地址并返回
		return sb.toString().toUpperCase();
	}

	public static String getHostMacAddress(String host) {

		// 获得网络接口对象（即网卡），并得到mac地址，mac地址存在于一个byte数组中。
		byte[] mac = null;
		try {
			mac = NetworkInterface.getByInetAddress(InetAddress.getByName(host)).getHardwareAddress();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		if (mac == null || mac.length == 0) {
			return null;
		}
		// 下面代码是把mac地址拼装成String
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < mac.length; i++) {
			if (i != 0) {
				sb.append("-");
			}
			// mac[i] & 0xFF 是为了把byte转化为正整数
			String s = Integer.toHexString(mac[i] & 0xFF);
			sb.append(s.length() == 1 ? 0 + s : s);
		}
		// 把字符串所有小写字母改为大写成为正规的mac地址并返回
		return sb.toString().toUpperCase();
	}

	public static List<String> getAllMacAddresses() {
		List<String> addresses = new ArrayList<String>();
		StringBuffer sb = new StringBuffer();
		try {
			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
			while (networkInterfaces.hasMoreElements()) {
				NetworkInterface netInterface = networkInterfaces.nextElement();
				byte[] mac = netInterface.getHardwareAddress();
				if (mac != null && mac.length != 0) {
					sb.delete(0, sb.length());
					for (int i = 0; i < mac.length; i++) {
						if (i != 0) {
							sb.append("-");
						}
						// mac[i] & 0xFF 是为了把byte转化为正整数
						String s = Integer.toHexString(mac[i] & 0xFF);
						sb.append(s.length() == 1 ? 0 + s : s);
					}
					addresses.add(sb.toString().toUpperCase());
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return addresses;
	}

	/*
	 * 获取Linux网卡的mac地址
	 * 
	 * @return
	 */
	public static String getLinuxMacAddress() {
		String mac = null;
		BufferedReader reader = null;
		Process process = null;
		try {
			// linux下的命令，一般取eth0作为本地主网卡 显示信息中包含有mac地址信息
			process = Runtime.getRuntime().exec("ifconfig eth0");
			reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			int index = -1;
			while ((line = reader.readLine()) != null) {
				index = line.toLowerCase().indexOf("硬件地址");
				if (index != -1) {
					// 取出mac地址并去除2边空格
					mac = line.substring(index + 4).trim();
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
				process.destroy();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			reader = null;
			process = null;
		}
		return mac;
	}

	/*
	 * 获取Unix网卡的mac地址
	 * 
	 * @return
	 */
	public static String getUnixMacAddress() {
		String mac = null;
		BufferedReader reader = null;
		Process process = null;
		try {
			// Unix下的命令，一般取eth0作为本地主网卡 显示信息中包含有mac地址信息
			process = Runtime.getRuntime().exec("ifconfig eth0");
			reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			int index = -1;
			while ((line = reader.readLine()) != null) {
				// 寻找标示字符串[hwaddr]
				index = line.toLowerCase().indexOf("hwaddr");
				if (index != -1) {
					// 取出mac地址并去除2边空格
					mac = line.substring(index + "hwaddr".length() + 1).trim();
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
				process.destroy();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			reader = null;
			process = null;
		}

		return mac;
	}

	/*
	 * 获取MAC地址
	 * 
	 * @return
	 */
	public static String getMacAddress() {
		String os = getOSName();
		String mac = null;
		if (os.startsWith("windows")) {
			String execStr = getSystemRoot() + "/system32/ipconfig /all";
			if (os.equals("windows xp")) {// xp
				mac = getWindowXPMacAddress(execStr);
			} else if (os.equals("windows 2003")) {// 2003
				mac = getWindowXPMacAddress(execStr);
			} else {// win7
				mac = getWindow7MacAddress();
			}
		} else if (os.startsWith("linux")) {
			mac = getLinuxMacAddress();
		} else {
			mac = getUnixMacAddress();
		}
		return mac;
	}

	/*
	 * jdk1.4获取系统命令路径 ：SystemRoot=C:\WINDOWS @return @return String 返回类型 @throws
	 */
	public static String getSystemRoot() {
		String cmd = null;
		String os = null;
		String result = null;
		String envName = "windir";
		os = System.getProperty("os.name").toLowerCase();
		if (os.startsWith("windows")) {
			cmd = "cmd /c SET";
		} else {
			cmd = "env";
		}
		BufferedReader reader = null;
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(cmd);
			reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				line = line.toLowerCase();// 重要(同一操作系统不同电脑有些返回的是小写,有些是大写.因此需要统一转换成小写)
				if (line.indexOf(envName) > -1) {
					result = line.substring(line.indexOf(envName) + envName.length() + 1);
					return result;
				}
			}
		} catch (Exception e) {
			LOG.error("获取系统命令路径 error: " + cmd + ":", e);
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
				process.destroy();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			reader = null;
			process = null;
		}
		return null;
	}

}

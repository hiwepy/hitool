/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.lang3.network;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * 
 * IP地址分为普通地址和特殊地址。在前面的文章中所使用的大多数都是普通的IP地址，
 * 在本文中将介绍如何利用InetAddress类提供的十个方法来确定一个IP地址是否是一个特殊的IP地址。
 *
 * 一、isAnyLocalAddress方法
 *
 * 当IP地址是通配符地址时返回true，否则返回false.这个通配符地址对于拥有多个网络接口（如两块网卡）的计算机非常拥有。
 * 使用通配符地址可以允许在服务器主机接受来自任何网络接口的客户端连接。IPv4的通配符地址是0.0.0.0.IPv6的通配符地址是0：0：0：0：0：0：0：0，也可以简写成：：。
 *
 * 二、isLoopbackAddress方法
 *
 * 当IP地址是loopback地址时返回true，否则返回false.loopback地址就是代表本机的IP地址。
 * IPv4的loopback地址的范围是127.0.0.0 ~ 127.255.255.255，也就是说，只要第一个字节是127，就是lookback地址。如127.1.2.3、127.0.200.200都是loopback地址。
 * IPv6的loopback地址是0：0：0：0：0：0：0：1，也可以简写成：：1. 
 * 
 * 我们可以使用ping命令来测试lookback地址。如下面的命令行所示：
 *
 * ping 127.200.200.200
 *
 * 运行结果：
 *
 * Reply from 127.0.0.1: bytes=32 time<1ms TTL=128 (注：win7下是127.200.200.200)
 * Reply from 127.0.0.1: bytes=32 time<1ms TTL=128 Reply from 127.0.0.1:
 * bytes=32 time<1ms TTL=128 Reply from 127.0.0.1: bytes=32 time<1ms TTL=128
 * 
 * Ping statistics for 127.200.200.200: Packets: Sent = 4, Received = 4, Lost =
 * 0 (0% loss), Approximate round trip times in milli-seconds: Minimum = 0ms,
 * Maximum = 0ms, Average = 0ms
 *
 * 虽然127.255.255.255也是loopback地址，但127.255.255.255在Windows下是无法ping通的。
 * 这是因为127.255.255.255是广播地址，在Windows下对发给广播地址的请求不做任何响应，而在其他操作系统上根据设置的不同，可能会得到不同的结果。
 *
 * 三、isLinkLocalAddress方法
 *
 * 当IP地址是本地连接地址（LinkLocalAddress）时返回true，否则返回false.
 * IPv4的本地连接地址的范围是169.254.0.0 ~ 169.254.255.255.
 * IPv6的本地连接地址的前12位是FE8，其他的位可以是任意取值，如FE88：：、FE80：：ABCD：：都是本地连接地址。
 *
 * 四、isSiteLocalAddress方法
 *
 * 当IP地址是地区本地地址（SiteLocalAddress）时返回true，否则返回false.
 * IPv4的地址本地地址分为三段：10.0.0.0 ~ 10.255.255.255、172.16.0.0 ~ 172.31.255.255、192.168.0.0 ~ 192.168.255.255.
 * IPv6的地区本地地址的前12位是FEC，其他的位可以是任意取值，如FED0：：、FEF1：：都是地区本地地址。
 *
 * 五、isMulticastAddress方法
 *
 * 当IP地址是广播地址（MulticastAddress）时返回true，否则返回false.
 * 通过广播地址可以向网络中的所有计算机发送信息， 而不是只向一台特定的计算机发送信息。
 * IPv4的广播地址的范围是224.0.0.0 ~ 239.255.255.255.
 * IPv6的广播地址第一个字节是FF，其他的字节可以是任意值。关于广播地址的详细内容将在以后的章节中讨论。
 *
 * 六、isMCGlobal方法
 *
 * 当IP地址是全球范围的广播地址时返回true，否则返回false.全球范围的广播地址可以向Internet中的所有的计算机发送信息。
 * IPv4的广播地址除了224.0.0.0和第一个字节是239的IP地址都是全球范围的广播地址。
 * IPv6的全球范围的广播地址中第一个字节是FF，第二个字节的范围是0E ~ FE，其他的字节可以是任意值，如FFBE：：、FF0E：：都是全球范围的广播地址。
 *
 * 七、isMCLinkLocal方法
 *
 * 当IP地址是子网广播地址时返回true，否则返回false.使用子网的广播地址只能向子网内的计算机发送信息。
 * IPv4的子网广播地址的范围是224.0.0.0 ~ 224.0.0.255.
 * IPv6的子网广播地址的第一个字节是FF，第二个字节的范围是02 ~ F2，其他的字节可以是任意值，如FFB2：：、FF02：ABCD：：都是子网广播地址。
 *
 * 八、isMCNodeLocal方法
 *
 * 当IP地址是本地接口广播地址时返回true，否则返回false.本地接口广播地址不能将广播信息发送到产生广播信息的网络接口，
 * 即使是同一台计算机的另一个网络接口也不行。所有的IPv4广播地址都不是本地接口广播地址。
 * IPv6的本地接口广播地址的第一个字节是FF，第二个节字的范围是01 ~ F1，其他的字节可以是任意值，如FFB1：：、FF01：A123：：都是本地接口广播地址。
 *
 * 九、isMCOrgLocal方法
 *
 * 当IP地址是组织范围的广播地址时返回ture，否则返回false.使用组织范围广播地址可以向公司或企业内部的所有的计算机发送广播信息。
 * IPv4的组织范围广播地址的第一个字节是239，第二个字节不小于192，第三个字节不大于195，如239.193.100.200、239.192.195.
 * 0都是组织范围广播地址。IPv6的组织范围广播地址的第一个字节是FF，第二个字节的范围是08 ~
 * F8，其他的字节可以是任意值，如FF08：：、FF48：：都是组织范围的广播地址。
 *
 * 十、isMCSiteLocal方法
 *
 * 当IP地址是站点范围的广播地址时返回true，否则返回false.使用站点范围的广播地址，可以向站点范围内的计算机发送广播信息。
 * IPv4的站点范围广播地址的范围是239.255.0.0 ~ 239.255.255.255，
 * 如239.255.1.1、239.255.0.0都是站点范围的广播地址。IPv6的站点范围广播地址的第一个字节是FF，
 * 第二个字节的范围是05 ~ F5，其他的字节可以是任意值，如FF05：：、FF45：：都是站点范围的广播地址。
 *
 */
public abstract class InetAddressUtils {

	protected static Logger LOG = LoggerFactory.getLogger(InetAddressUtils.class);
	protected static String LOCAL_IP = "127.0.0.1";
	protected static String LOCAL_IP_255 = "255.255.255.0";
	protected static String LOCALHOST = "localhost";
	protected static String UNKNOWN_HOST_ADDRESS = "Unknown Host Address";

	public static String getIPString(byte[] ipBytes) {
		StringBuffer sb = new StringBuffer();
		sb.append(ipBytes[0] & 0xFF);
		sb.append('.');
		sb.append(ipBytes[1] & 0xFF);
		sb.append('.');
		sb.append(ipBytes[2] & 0xFF);
		sb.append('.');
		sb.append(ipBytes[3] & 0xFF);
		return sb.toString();
	}

	/*
	 * 从ip的字符串形式得到字节数组形式
	 */
	public static byte[] getIpV4Bytes(String ipOrMask) {
		// ipv6的地址，不解析，返回127.0.0.1
		if (ipOrMask.contains(":")) {
			ipOrMask = LOCAL_IP;
		}
		byte[] ret = new byte[4];
		StringTokenizer st = new StringTokenizer(ipOrMask, ".");
		try {
			ret[0] = (byte) (Integer.parseInt(st.nextToken()) & 0xFF);
			ret[1] = (byte) (Integer.parseInt(st.nextToken()) & 0xFF);
			ret[2] = (byte) (Integer.parseInt(st.nextToken()) & 0xFF);
			ret[3] = (byte) (Integer.parseInt(st.nextToken()) & 0xFF);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return ret;
	}
	
	public static int getIpV4Value(String ipOrMask) {
		byte[] addr = getIpV4Bytes(ipOrMask);
		int address1 = addr[3] & 0xFF;
		address1 |= ((addr[2] << 8) & 0xFF00);
		address1 |= ((addr[1] << 16) & 0xFF0000);
		address1 |= ((addr[0] << 24) & 0xFF000000);
		return address1;
	}
	
	/*
	 * 获取主机名
	 */
	public static String getCanonicalHostName() {
		String name = null;
		try {
			Enumeration<NetworkInterface> infs = NetworkInterface.getNetworkInterfaces();
			while (infs.hasMoreElements() && (name == null)) {
				NetworkInterface net = infs.nextElement();
				if (net.isLoopback()) {
					continue;
				}
				Enumeration<InetAddress> addr = net.getInetAddresses();
				while (addr.hasMoreElements()) {
					InetAddress inet = addr.nextElement();
					name = inet.getCanonicalHostName();
					break;
				}
			}
			return name;
		} catch (SocketException e) {
			LOG.error("Error when getting canonical host name: <{}>.", e.getMessage());
			return LOCALHOST;
		}
	}
	
	/*
	 * 获取主机别名
	 */
	public static String getHostName() {
		if (System.getenv("COMPUTERNAME") != null) {
			return System.getenv("COMPUTERNAME");
		}
		// getHostName For Liunx 
		String name = null;
		try {
			Enumeration<NetworkInterface> infs = NetworkInterface.getNetworkInterfaces();
			while (infs.hasMoreElements() && (name == null)) {
				NetworkInterface net = infs.nextElement();
				if (net.isLoopback()) {
					continue;
				}
				Enumeration<InetAddress> addr = net.getInetAddresses();
				while (addr.hasMoreElements()) {
					InetAddress inet = addr.nextElement();
					name = inet.getHostName();
					break;
				}
			}
			return name;
		} catch (SocketException e) {
			LOG.error("Error when getting host name: <{}>.", e.getMessage());
			try {
				return InetAddress.getLocalHost().getHostName();
			} catch (UnknownHostException uhe) {
				String host = uhe.getMessage(); // host = "hostname: hostname"
				if (host != null) {
					int colon = host.indexOf(':');
					if (colon > 0) {
						return host.substring(0, colon);
					}
				}
				return LOCALHOST;
			}
		}
	}

	/*
	 * 获取主机网络地址
	 */
	public static String getHostAddress() {
		try {
			// 本地IP，如果没有配置外网IP则返回它
			String localip = null;
			// 外网IP
			String netip = null;
			// 是否找到外网IP
			boolean finded = false;
			Enumeration<NetworkInterface> infs = NetworkInterface.getNetworkInterfaces();
			while (infs.hasMoreElements() && !finded) {
				NetworkInterface net = infs.nextElement();
				Enumeration<InetAddress> addr = net.getInetAddresses();
				while (addr.hasMoreElements()) {

					InetAddress inet = addr.nextElement();

					LOG.debug(inet.getHostName() + ";" + inet.getHostAddress() + ";inet.isSiteLocalAddress()="
							+ inet.isSiteLocalAddress() + ";inet.isLoopbackAddress()=" + inet.isLoopbackAddress());
					// 外网IP
					if (!inet.isSiteLocalAddress() && !inet.isLoopbackAddress()
							&& inet.getHostAddress().indexOf(":") == -1) {
						netip = inet.getHostAddress();
						finded = true;
						break;
					}
					// 内网IP
					else if (inet.isSiteLocalAddress() && !inet.isLoopbackAddress()
							&& inet.getHostAddress().indexOf(":") == -1) {
						localip = inet.getHostAddress();
					}
					
				}
			}
			if (netip != null && !"".equals(netip)) {  
	            return netip;  
	        } else {  
	            return localip;  
	        }
		} catch (SocketException e) {
			LOG.error("Error when getting host address: <{}>.", e.getMessage());
			return UNKNOWN_HOST_ADDRESS;
		}
	}
	
	/*
	 * 获取网卡信息 Map<网卡名称,网卡信息>
	 */
	public static Map<String, InetAddress> getNetworkAdapter() {
		Map<String, InetAddress> map = new HashMap<String, InetAddress>();
		try {
			Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
			while (enumeration.hasMoreElements()) {
				NetworkInterface networkInterface = enumeration.nextElement();

				if (!networkInterface.isLoopback() && !networkInterface.isVirtual() && networkInterface.isUp()) {
					LOG.debug(networkInterface.getDisplayName());
					Enumeration<InetAddress> addressEnumeration = networkInterface.getInetAddresses();
					while (addressEnumeration.hasMoreElements()) {
						LOG.debug("\t" + addressEnumeration.nextElement());
						map.put(networkInterface.getDisplayName(), addressEnumeration.nextElement());
					}
				}
			}
		} catch (SocketException e) {
			LOG.error("Error when getting host ip address: <{}>.", e.getMessage());
		}
		return map;
	}

	/*
	 * 获取网卡信息List<网卡信息>
	 */
	public static List<InetAddress> getAddressList() {
		List<InetAddress> addressList = null;
		try {
			addressList = new ArrayList<InetAddress>();
			Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
			while (enumeration.hasMoreElements()) {
				NetworkInterface networkInterface = enumeration.nextElement();
				if (!networkInterface.isLoopback() && !networkInterface.isVirtual() && networkInterface.isUp()) {
					LOG.debug(networkInterface.getDisplayName());
					Enumeration<InetAddress> addressEnumeration = networkInterface.getInetAddresses();
					while (addressEnumeration.hasMoreElements()) {
						LOG.debug("\t" + addressEnumeration.nextElement());
						addressList.add(addressEnumeration.nextElement());
					}
				}
			}

		} catch (SocketException e) {
			LOG.error("Error when getting host ip address: <{}>.", e.getMessage());
		}
		return null;
	}

	/*
	 * Split a string in the form of
	 * "host1:port1,host2:port2 host3:port3,host4:port4" into a Map of
	 * InetSocketAddress instances suitable for instantiating a
	 * MemcachedClient,map's key is the main memcached node,and value is the
	 * standby node for main node. Note that colon-delimited IPv6 is also
	 * supported. For example: ::1:11211
	 * 
	 * @param s
	 * @return
	 */
	public static Map<InetSocketAddress, InetSocketAddress> getAddressMap(String s) {
		if (s == null) {
			throw new NullPointerException("Null host list");
		}
		if (s.trim().equals("")) {
			throw new IllegalArgumentException("No hosts in list:  ``" + s + "''");
		}
		s = s.trim();
		Map<InetSocketAddress, InetSocketAddress> result = new LinkedHashMap<InetSocketAddress, InetSocketAddress>();
		for (String hosts : s.split(" ")) {
			String[] nodes = hosts.split(",");

			if (nodes.length < 1) {
				throw new IllegalArgumentException("Invalid server ``" + hosts + "'' in list:  " + s);
			}
			String mainHost = nodes[0].trim();
			InetSocketAddress mainAddress = getInetSocketAddress(s, mainHost);
			if (nodes.length >= 2) {
				InetSocketAddress standByAddress = getInetSocketAddress(s, nodes[1].trim());
				result.put(mainAddress, standByAddress);
			} else {
				result.put(mainAddress, null);
			}

		}
		assert !result.isEmpty() : "No addrs found";
		return result;
	}

	private static InetSocketAddress getInetSocketAddress(String s, String mainHost) {
		int finalColon = mainHost.lastIndexOf(':');
		if (finalColon < 1) {
			throw new IllegalArgumentException("Invalid server ``" + mainHost + "'' in list:  " + s);
		}
		String hostPart = mainHost.substring(0, finalColon).trim();
		String portNum = mainHost.substring(finalColon + 1).trim();

		InetSocketAddress mainAddress = new InetSocketAddress(hostPart, Integer.parseInt(portNum));
		return mainAddress;
	}

	/*
	 * Split a string in the form of "host:port host2:port" into a List of
	 * InetSocketAddress instances suitable for instantiating a MemcachedClient.
	 * 
	 * Note that colon-delimited IPv6 is also supported. For example: ::1:11211
	 */
	public static List<InetSocketAddress> getAddresses(String s) {
		if (s == null) {
			throw new NullPointerException("Null host list");
		}
		if (s.trim().equals("")) {
			throw new IllegalArgumentException("No hosts in list:  ``" + s + "''");
		}
		s = s.trim();
		ArrayList<InetSocketAddress> addrs = new ArrayList<InetSocketAddress>();

		for (String hoststuff : s.split(" ")) {
			int finalColon = hoststuff.lastIndexOf(':');
			if (finalColon < 1) {
				throw new IllegalArgumentException("Invalid server ``" + hoststuff + "'' in list:  " + s);
			}
			String hostPart = hoststuff.substring(0, finalColon).trim();
			String portNum = hoststuff.substring(finalColon + 1).trim();

			addrs.add(new InetSocketAddress(hostPart, Integer.parseInt(portNum)));
		}
		assert !addrs.isEmpty() : "No addrs found";
		return addrs;
	}

	public static InetSocketAddress getOneAddress(String server) {
		if (server == null) {
			throw new NullPointerException("Null host");
		}
		if (server.trim().equals("")) {
			throw new IllegalArgumentException("No hosts in:  ``" + server + "''");
		}
		server = server.trim();
		int finalColon = server.lastIndexOf(':');
		if (finalColon < 1) {
			throw new IllegalArgumentException("Invalid server ``" + server + "''");
		}
		String hostPart = server.substring(0, finalColon).trim();
		String portNum = server.substring(finalColon + 1).trim();
		return new InetSocketAddress(hostPart, Integer.parseInt(portNum));
	}

	public static Map<InetSocketAddress, InetSocketAddress> getAddressMap(List<InetSocketAddress> addressList) {
		Map<InetSocketAddress, InetSocketAddress> addressMap = new LinkedHashMap<InetSocketAddress, InetSocketAddress>();
		if (addressList != null) {
			for (InetSocketAddress addr : addressList) {
				addressMap.put(addr, null);
			}
		}
		return addressMap;
	}

	public static boolean internalIp(String ip) {
		byte[] addr = textToNumericFormatV4(ip);
		return internalIp(addr) || LOCAL_IP.equals(ip);
	}
	
	private static boolean internalIp(byte[] addr) {
		if (Objects.isNull(addr) || addr.length < 2) {
			return true;
		}
		final byte b0 = addr[0];
		final byte b1 = addr[1];
		// 10.x.x.x/8
		final byte SECTION_1 = 0x0A;
		// 172.16.x.x/12
		final byte SECTION_2 = (byte) 0xAC;
		final byte SECTION_3 = (byte) 0x10;
		final byte SECTION_4 = (byte) 0x1F;
		// 192.168.x.x/16
		final byte SECTION_5 = (byte) 0xC0;
		final byte SECTION_6 = (byte) 0xA8;
		switch (b0) {
		case SECTION_1:
			return true;
		case SECTION_2:
			if (b1 >= SECTION_3 && b1 <= SECTION_4) {
				return true;
			}
		case SECTION_5:
			switch (b1) {
			case SECTION_6:
				return true;
			}
		default:
			return false;
		}
	}

	/**
	 * 将IPv4地址转换成字节
	 * 
	 * @param text IPv4地址
	 * @return byte 字节
	 */
	public static byte[] textToNumericFormatV4(String text) {
		if (text.length() == 0) {
			return null;
		}

		byte[] bytes = new byte[4];
		String[] elements = text.split("\\.", -1);
		try {
			long l;
			int i;
			switch (elements.length) {
			case 1:
				l = Long.parseLong(elements[0]);
				if ((l < 0L) || (l > 4294967295L)) {
					return null;
				}
				bytes[0] = (byte) (int) (l >> 24 & 0xFF);
				bytes[1] = (byte) (int) ((l & 0xFFFFFF) >> 16 & 0xFF);
				bytes[2] = (byte) (int) ((l & 0xFFFF) >> 8 & 0xFF);
				bytes[3] = (byte) (int) (l & 0xFF);
				break;
			case 2:
				l = Integer.parseInt(elements[0]);
				if ((l < 0L) || (l > 255L)) {
					return null;
				}
				bytes[0] = (byte) (int) (l & 0xFF);
				l = Integer.parseInt(elements[1]);
				if ((l < 0L) || (l > 16777215L)) {
					return null;
				}
				bytes[1] = (byte) (int) (l >> 16 & 0xFF);
				bytes[2] = (byte) (int) ((l & 0xFFFF) >> 8 & 0xFF);
				bytes[3] = (byte) (int) (l & 0xFF);
				break;
			case 3:
				for (i = 0; i < 2; ++i) {
					l = Integer.parseInt(elements[i]);
					if ((l < 0L) || (l > 255L)) {
						return null;
					}
					bytes[i] = (byte) (int) (l & 0xFF);
				}
				l = Integer.parseInt(elements[2]);
				if ((l < 0L) || (l > 65535L)) {
					return null;
				}
				bytes[2] = (byte) (int) (l >> 8 & 0xFF);
				bytes[3] = (byte) (int) (l & 0xFF);
				break;
			case 4:
				for (i = 0; i < 4; ++i) {
					l = Integer.parseInt(elements[i]);
					if ((l < 0L) || (l > 255L)) {
						return null;
					}
					bytes[i] = (byte) (int) (l & 0xFF);
				}
				break;
			default:
				return null;
			}
		} catch (NumberFormatException e) {
			return null;
		}
		return bytes;
	}
	
	public static boolean isSameSegment(String remoteIp) {
		String localIp = getLocalHostAddress();
		int mask = InetAddressUtils.getIpV4Value(LOCAL_IP_255);
		boolean flag = (mask & InetAddressUtils.getIpV4Value(localIp)) == (mask & InetAddressUtils.getIpV4Value(remoteIp));
		return flag;
	}
	
	public static String getLocalHostAddress() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
		}
		return LOCAL_IP;
	}
	
	public static String getLocalHostName() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
		}
		return UNKNOWN_HOST_ADDRESS;
	}
}

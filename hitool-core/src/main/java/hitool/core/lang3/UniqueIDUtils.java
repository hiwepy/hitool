/** 
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.lang3;

import java.io.IOException;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 产生唯一的ID值
 */
public class UniqueIDUtils {

	protected static Logger LOG = LoggerFactory.getLogger(UniqueIDUtils.class);
	private static char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	private static String hostAddr;
	private static Random random = new SecureRandom();
	private static MessageDigest mHasher;

	private static class UniqTimer {

		private long lastTime = System.currentTimeMillis();

		public synchronized long getCurrentTime() {
			long currTime = System.currentTimeMillis();

			lastTime = Math.max(lastTime + 1, currTime);

			return lastTime;
		}
	}

	private static UniqTimer timer = new UniqTimer();

	static {
		try {
			InetAddress addr = InetAddress.getLocalHost();

			hostAddr = addr.getHostAddress();
		} catch (IOException e) {
			LOG.error("[UniqID] Get HostAddr Error", e);
			hostAddr = String.valueOf(System.currentTimeMillis());
		}

		if (StringUtils.isBlank(hostAddr) || "127.0.0.1".equals(hostAddr)) {
			hostAddr = String.valueOf(System.currentTimeMillis());
		}

		if (LOG.isDebugEnabled()) {
			LOG.debug("[UniqID]hostAddr is:" + hostAddr);
		}

		try {
			mHasher = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException nex) {
			mHasher = null;
			LOG.error("[UniqID]new MD5 Hasher error", nex);
		}
	}

	public static String getUniqID() {
		StringBuffer sb = new StringBuffer();
		long t = timer.getCurrentTime();

		sb.append(t);

		sb.append("-");

		sb.append(random.nextInt(8999) + 1000);

		sb.append("-");
		sb.append(hostAddr);

		sb.append("-");
		sb.append(Thread.currentThread().hashCode());

		if (LOG.isDebugEnabled()) {
			LOG.debug("[UniqID.getUniqID]" + sb.toString());
		}

		return sb.toString();
	}

	public static String getUniqIDHash() {
		return hash(getUniqID());
	}

	public static synchronized String hash(String str) {
		byte[] bt = mHasher.digest(str.getBytes());
		int l = bt.length;

		char[] out = new char[l << 1];

		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = digits[(0xF0 & bt[i]) >>> 4];
			out[j++] = digits[0x0F & bt[i]];
		}

		if (LOG.isDebugEnabled()) {
			LOG.debug("[UniqID.hash]" + (new String(out)));
		}

		return new String(out);
	}

	public static void main(String[] args) {
		System.out.println(UniqueIDUtils.getUniqID());
		System.out.println(UniqueIDUtils.getUniqIDHash());
	}

}

/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.lang3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * 序列化工具类，负责byte[]和Object之间的相互转换.
 */
public class SerializationUtils extends org.apache.commons.lang3.SerializationUtils {

	protected static Logger LOG = LoggerFactory.getLogger(SerializationUtils.class);

	/*
	 * 对实体Bean进行序列化操作.
	 * @param source 待转换的实体
	 * @return 转换之后的字节数组
	 */
	public static byte[] serialize(Object source) {
		ByteArrayOutputStream byteOut = null;
		ObjectOutputStream ObjOut = null;
		try {
			byteOut = new ByteArrayOutputStream();
			ObjOut = new ObjectOutputStream(byteOut);
			ObjOut.writeObject(source);
			ObjOut.flush();
		} catch (IOException e) {
			LOG.error(source.getClass().getName() + " serialized error !",e);
		} finally {
			try {
				if (null != ObjOut) {
					ObjOut.close();
				}
			} catch (IOException e) {
				ObjOut = null;
			}
		}
		return byteOut.toByteArray();
	}

	/*
	 * 将字节数组反序列化为实体Bean.
	 * @param source 需要进行反序列化的字节数组
	 * @return 反序列化后的实体Bean
	 */
	public static Object deserialize(byte[] source) {
		ObjectInputStream ObjIn = null;
		Object retVal = null;
		try {
			ByteArrayInputStream byteIn = new ByteArrayInputStream(source);
			ObjIn = new ObjectInputStream(byteIn);
			retVal = ObjIn.readObject();
		} catch (Exception e) {
			LOG.error("deserialized error  !", e);
		} finally {
			try {
				if (null != ObjIn) {
					ObjIn.close();
				}
			} catch (IOException e) {
				ObjIn = null;
			}
		}
		return retVal;
	}
	
}

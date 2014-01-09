/*****************************************************************<br>
 * <B>FILE :</B> BeanUtil.java <br>
 * <B>CREATE DATE :</B> Jul 8, 2010 <br>
 * <B>DESCRIPTION :</B> <br>
 *
 * <B>CHANGE HISTORY LOG</B><br>
 *---------------------------------------------------------------<br>
 * NO.  |  DATE |   NAME   |   REASON   |  DESCRIPTION           <br>
 *---------------------------------------------------------------<br>
 *          
 *****************************************************************<br>
 */
package com.thayer.idsservice.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * <B>Function :</B> <br>
 * <B>General Usage :</B> <br>
 * <B>Special Usage :</B> <br>
 * 
 * @author : Jean.Jiang<br>
 * @since : Jul 8, 2010<br>
 * @version : v1.0
 */
public class BeanUtil {

	private BeanUtil() {
	}

	/**
	 * 反序列化为对象
	 * 
	 * @param persistentValue
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Object unpersistentToObject(byte[] persistentValue) throws IOException, ClassNotFoundException {
		ObjectInputStream objStream = new ObjectInputStream(new ByteArrayInputStream(persistentValue));
		return objStream.readObject();
	}

	/**
	 * 序列化对象
	 * 
	 * @param persistObject
	 * @return
	 * @throws Exception
	 */
	public static byte[] persistentObject(Object persistObject) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream objStream = new ObjectOutputStream(out);
		objStream.writeObject(persistObject);
		return out.toByteArray();
	}
}

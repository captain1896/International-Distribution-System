package com.thayer.idsservice.service.interf;

import java.io.Serializable;

/**
 * <B>Function :</B> 检测服务状态接口<br>
 * <B>General Usage :</B> <br>
 * <B>Special Usage :</B> <br>
 * 
 * @author : Jean.Jiang<br>
 * @since : 2010-10-8<br>
 * @version : v1.0
 */
public interface ITimeoutRecoverService extends Serializable {
	/**
	 * 是否恢复
	 * 
	 * @return
	 */
	public boolean isRecover();
}

package com.thayer.idsservice.task.interf;

/**
 * <B>Function :</B>IDS接口 更新rate,avail 任务接口 <br>
 * <B>General Usage :</B> <br>
 * <B>Special Usage :</B> <br>
 * 
 * @author : Jean.Jiang<br>
 * @since : 2010-10-8<br>
 * @version : v1.0
 */
public interface IRateAvailUploadTask {
	/**
	 * 接受并处理更新消息
	 * 
	 * @param message
	 */
	void onMessage(Object message);
}

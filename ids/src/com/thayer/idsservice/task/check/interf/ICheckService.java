/*****************************************************************<br>
 * <B>FILE :</B> ICheckService.java <br>
 * <B>CREATE DATE :</B> 2010-11-26 <br>
 * <B>DESCRIPTION :</B> <br>
 *
 * <B>CHANGE HISTORY LOG</B><br>
 *---------------------------------------------------------------<br>
 * NO.  |  DATE |   NAME   |   REASON   |  DESCRIPTION           <br>
 *---------------------------------------------------------------<br>
 *          
 *****************************************************************<br>
 */
package com.thayer.idsservice.task.check.interf;

import java.util.List;

import com.thayer.idsservice.dao.EMsgLog;
import com.thayer.idsservice.exception.BizException;
import com.thayer.idsservice.task.update.bean.ResultBean;

/**
 * <B>Function :</B> <br>
 * <B>General Usage :</B> <br>
 * <B>Special Usage :</B> <br>
 * 
 * @author : Jean.Jiang<br>
 * @since : 2010-11-26<br>
 * @version : v1.0
 */
public interface ICheckService {
	void doCheckRs(List<EMsgLog> eMsgLogs, ResultBean resultBean) throws BizException;
}

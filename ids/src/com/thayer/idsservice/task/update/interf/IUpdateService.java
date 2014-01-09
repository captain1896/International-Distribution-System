package com.thayer.idsservice.task.update.interf;

import com.thayer.idsservice.exception.BizException;
import com.thayer.idsservice.exception.MappingException;
import com.thayer.idsservice.exception.TimeOut4ThirdException;
import com.thayer.idsservice.task.update.bean.FogMQBean;
import com.thayer.idsservice.task.update.bean.ResultBean;

public interface IUpdateService {
	/**
	 * 更新入口
	 * 
	 * @throws BizException
	 */
	public void update(FogMQBean fogMQBean, ResultBean resultBean) throws BizException, TimeOut4ThirdException,MappingException;

}

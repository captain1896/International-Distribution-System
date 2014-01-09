package com.thayer.idsservice.task.receive.interf;

import java.io.Serializable;

public interface IReceiveResvService extends Serializable {
	public String downloadResv(String xml);
}

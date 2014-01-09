package com.thayer.idsservice.context;

public class ThreadLocalServiceContextHolderStrategy implements IContextHolderStrategy {

	private static ThreadLocal<ServiceContext> contextHolder = new ThreadLocal<ServiceContext>();

	public void clearContext() {
		contextHolder.remove();
	}

	public ServiceContext getContext() {

		return contextHolder.get();
	}

	public void setContext(ServiceContext securityContext) {
		contextHolder.set(securityContext);
	}
}

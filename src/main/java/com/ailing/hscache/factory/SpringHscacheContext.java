package com.ailing.hscache.factory;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringHscacheContext implements ApplicationContextAware{
	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		applicationContext = context;
	}

	/**
	 * @return the applicationContext
	 */
	public static ApplicationContext getContext() {
		return applicationContext;
	}
	
	public static Object getBean(String beanName) {
		if(applicationContext == null) {
			return null;
		}
		
		return applicationContext.getBean(beanName);
	}
	
	public static <T> T getBean(Class<T> clz) {
		if(applicationContext == null) {
			return null;
		}
		
		return applicationContext.getBean(clz);
	}
}

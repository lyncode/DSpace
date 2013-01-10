/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.springui.utils;

import java.lang.reflect.Proxy;

import org.dspace.kernel.ServiceManager;
import org.dspace.utils.DSpace;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * This proxy imports all defenitions of DSpace Services to the webapp context.
 * 
 * @author João Melo <jmelo@lyncode.com>
 */
public class DSpaceBeanProxy implements ApplicationContextAware {
	private boolean initialized = false; 
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		if (!initialized) {
			DSpace dspace = new DSpace();
			
			AutowireCapableBeanFactory factory = applicationContext.getAutowireCapableBeanFactory();
			DefaultListableBeanFactory registry = (DefaultListableBeanFactory) factory;
			
			ServiceManager sm = dspace.getServiceManager();
			
			for (String name : sm.getServicesNames()) {
				Object obj = sm.getServiceByName(name, Object.class);
				Class<?> realClass = null;
				if (obj.getClass().getName().contains("Proxy")) {
					if (obj.getClass().getInterfaces().length > 0)
						realClass = obj.getClass().getInterfaces()[0];
					else {
						if (obj.getClass().getSuperclass() != null && !obj.getClass().getSuperclass().getName().equals(Proxy.class.getName()))
							realClass = obj.getClass().getSuperclass();
					}
				} else
					realClass = obj.getClass();
				
				if (realClass != null) {
					BeanDefinition bean = BeanDefinitionBuilder.genericBeanDefinition(realClass).getBeanDefinition();
					if (!registry.containsBean(name)) {
						System.out.println("Serviço ("+name+") = "+realClass.getName());
						registry.registerBeanDefinition(name, bean);
						registry.registerSingleton(name, obj);
					}
				}
			}
		}
		initialized = true;
	}
}

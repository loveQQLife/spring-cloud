package com.kreken.conf;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

/**
 * 获取上下文
 */
public class ApplicationContextConfig {

    private static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}

package com.wolf.cache.listener;


import com.wolf.cache.kafka.KafkaConsumer;
import com.wolf.cache.spring.SpringContext;
import com.wolf.cache.zk.ZookeeperSession;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 系统初始化监听器
 * @author wolf
 */
public class InitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();
        ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        SpringContext.setApplicationContext(context);

        new Thread(new KafkaConsumer("cache-message")).start();

        ZookeeperSession.init();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
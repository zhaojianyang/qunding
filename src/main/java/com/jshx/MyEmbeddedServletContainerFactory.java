package com.jshx;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.embedded.EmbeddedServletContainer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.stereotype.Component;

/**
 * Created by Leo35 on 2017/11/19.
 */
@Component()
public class MyEmbeddedServletContainerFactory extends TomcatEmbeddedServletContainerFactory {

    private static final Logger logger = LoggerFactory.getLogger(MyEmbeddedServletContainerFactory.class);

    public EmbeddedServletContainer getEmbeddedServletContainer(ServletContextInitializer... initializers)
    {
        logger.debug(" Tomcat Initialization done ");
        return super.getEmbeddedServletContainer(initializers);
    }

    protected void customizeConnector(Connector connector)
    {
        super.customizeConnector(connector);
        Http11NioProtocol protocol = (Http11NioProtocol)connector.getProtocolHandler();

        logger.debug("default MaxConnections>>"+ protocol.getMaxConnections());
        logger.debug("default MaxThreads>>"+ protocol.getMaxThreads());
        logger.debug("default AcceptorThreadCount>>"+ protocol.getAcceptorThreadCount());
        logger.debug("default MinSpareThreads>>"+ protocol.getMinSpareThreads());
        logger.debug("default ConnectionTimeout>>"+ protocol.getConnectionTimeout());


        //设置最大连接数
        protocol.setMaxConnections(-1);

        //设置最大线程数
        protocol.setMaxThreads(1000);

        //设置接受连接的线程的数量 --跟CPU 的core数目一样
        protocol.setAcceptorThreadCount(16);

        protocol.setAcceptCount(-1);

        //始终保持运行最小线程数 --启动时准备好所有的线程
        protocol.setMinSpareThreads(100);

        //连接超时时间
        protocol.setConnectionTimeout(20000);

        protocol.setKeepAliveTimeout(-1);

        protocol.setMaxKeepAliveRequests(-1);

        logger.debug(" Tomcat Attribute setting done ");
    }
}

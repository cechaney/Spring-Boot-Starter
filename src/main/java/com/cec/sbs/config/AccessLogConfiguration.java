package com.cec.sbs.config;

import org.apache.catalina.valves.AccessLogValve;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class AccessLogConfiguration extends WebMvcConfigurerAdapter implements EmbeddedServletContainerCustomizer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccessLogConfiguration.class.getName());

    @Value("${server.tomcat.accesslog.directory}")
    private String logDirectory;

    @Value("${server.tomcat.accesslog.prefix}")
    private String logFilePrefix;

    @Value("${server.port}")
    private int port;

    @Value("${server.tomcat.accesslog.pattern}")
    private String logPattern;

    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {

        if (container instanceof TomcatEmbeddedServletContainerFactory) {

            TomcatEmbeddedServletContainerFactory factory = (TomcatEmbeddedServletContainerFactory) container;

            AccessLogValve accessLogValve = new AccessLogValve();

            accessLogValve.setEnabled(true);
            accessLogValve.setRenameOnRotate(true);
            accessLogValve.setDirectory(logDirectory);
            accessLogValve.setPrefix(logFilePrefix);
            accessLogValve.setSuffix(".log");
            accessLogValve.setPattern(logPattern);

            factory.addContextValves(accessLogValve);
            factory.setPort(port);

        } else {
            LOGGER.warn("The container you have configured does not support customization");
        }
    }

}

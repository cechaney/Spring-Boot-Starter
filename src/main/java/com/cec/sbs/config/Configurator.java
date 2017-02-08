package com.cec.sbs.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.cec.sbs.health.ConfigHealthIndicator;

@Service
public class Configurator implements Runnable{

	private static final Logger LOGGER = LoggerFactory.getLogger(Configurator.class);
    private static final String DEFAULT_CONFIG_PATH = "/etc/config.properties";
    private static final String LOGLEVEL_PREFIX = "LOGLEVEL.";

    private final ScheduledExecutorService pollService;
    private final AtomicReference<Properties> config;
    private final String configPath;
    private final int pollInterval;

    private ConfigHealthIndicator healthIndicator;

    @Autowired
    public Configurator(
            @Value("${configPath:}") String configPath,
            @Value("${pollinterval:1000}") int pollInterval,
            ConfigHealthIndicator healthIndicator) throws IOException {

    	pollService = Executors.newSingleThreadScheduledExecutor();
    	config = new AtomicReference<>();

        if(StringUtils.isEmpty(configPath)){
            this.configPath = DEFAULT_CONFIG_PATH;
        } else {
            this.configPath = configPath;
        }

        this.pollInterval = pollInterval;
        this.healthIndicator = healthIndicator;

        this.healthIndicator.setConfigPath(this.configPath);
        this.healthIndicator.setParent(this);

        LOGGER.info("Application config: {}", configPath);

        loadPropertiesFile();

    }

    @Override
    public void run(){
        loadPropertiesFile();
    }

    public void loadPropertiesFile(){

    	/*
    	This block will show up as uncovered by Jacoco.
    	Jacoco has a bug that will not allow it to properly
    	calculate coverage for Java 7 "try with resources" blocks
    	*/
        try(FileInputStream fis = new FileInputStream(this.configPath)){

            Properties newConfig = new Properties();

            newConfig.load(fis);

            if (!newConfig.equals(this.getGlobalConfig())) {

            	LOGGER.debug("Config change detected");

            	this.setGlobalConfig(newConfig);

            	healthIndicator.loadConfig();

            	updateLoggingConfig(newConfig);

            	this.healthIndicator.setLastConfigUpdate(Calendar.getInstance().getTime());

            	LOGGER.debug("Config updated!");

            }
        } catch(IOException ioe){

        	LOGGER.info("Config load failed");

        	this.setGlobalConfig(new Properties());

        	healthIndicator.unloadConfig();
        }


    }

    @PostConstruct
    public void start() throws IOException {
    	pollService.scheduleAtFixedRate(this, 0, this.pollInterval, TimeUnit.MILLISECONDS);
    }

    @PreDestroy
    public void shutdown() throws IOException {
    	pollService.shutdown();
    }

    public void setGlobalConfig(Properties props){
        config.set(props);
    }

    public Properties getGlobalConfig(){

    	Properties configCopy = SerializationUtils.clone(config.get());

        return configCopy;

    }

    @Bean(name = "request.config")
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public Properties getRequestConfig() {

        //Provide a copy of the configuration that might be updated out of this thread
        Properties props = new Properties();

        props.putAll(config.get());

        LOGGER.debug("Returning copy of atomicProps: {}", props);

        return props;
    }

    private void updateLoggingConfig(Properties props){

    	for(Object key: props.keySet()){

    		String keyAsString = key.toString();
    		String valueAsString = props.get(key).toString();

    		if(keyAsString.startsWith(LOGLEVEL_PREFIX)){

    			String logLevelKey =  keyAsString.replace(LOGLEVEL_PREFIX,"");

    			ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(logLevelKey);

    			logger.setLevel(ch.qos.logback.classic.Level.toLevel(valueAsString));

    		}
    	}

    }

}

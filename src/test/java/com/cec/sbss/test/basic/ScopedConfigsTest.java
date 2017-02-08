package com.cec.sbss.test.basic;

import java.util.Properties;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.cec.sbss.config.Configurator;
import com.cec.sbss.health.ConfigHealthIndicator;

public class ScopedConfigsTest {

    private Configurator cf;

    @Before
    public void setup() throws Exception {

        String configPath = System.getProperty("app.props.location");

        cf = new Configurator(configPath, 250, new ConfigHealthIndicator());

    }

    @After
    public void shutdown() throws Exception {

    }

    @Test
    public void testRequestConfig(){

        Properties config = cf.getRequestConfig();

        Assert.assertNotNull(config);

    }

    @Test
    public void testGlobalConfig(){

        Properties config = cf.getGlobalConfig();

        Assert.assertNotNull(config);

    }

}

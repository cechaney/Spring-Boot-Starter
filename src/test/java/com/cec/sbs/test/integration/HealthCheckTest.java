package com.cec.sbs.test.integration;

import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.CORBA.portable.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;

import com.cec.sbs.App;
import com.cec.sbs.health.ConfigHealthIndicator;
import com.cec.sbs.health.HealthCheckEndpoint;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=App.class, webEnvironment=WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class HealthCheckTest {

    @Autowired
    private ConfigHealthIndicator configHealthIndicator;

    @Autowired
    private HealthCheckEndpoint healthCheckEndpoint;

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp(){
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testHealthCheckEndpointGood() throws ApplicationException{

    	configHealthIndicator.loadConfig();

    	MockHttpServletResponse response = new MockHttpServletResponse();

    	healthCheckEndpoint.healthCheck(response);

        Assert.assertTrue(response.getStatus() == HttpStatus.SC_OK);

    }

    @Test
    public void testHealthCheckEndpointBadConfig() throws ApplicationException{

    	configHealthIndicator.unloadConfig();

    	MockHttpServletResponse response = new MockHttpServletResponse();

    	healthCheckEndpoint.healthCheck(response);

        Assert.assertTrue(response.getStatus() == HttpStatus.SC_INTERNAL_SERVER_ERROR);

    }

    @Test
    public void testHealthCheckEndpointEmptyConfig() throws ApplicationException{

    	String configPath = configHealthIndicator.getConfigPath();

    	if(configPath != null){
    		configHealthIndicator.setConfigPath("");
    	}

    	configHealthIndicator.unloadConfig();

    	MockHttpServletResponse response = new MockHttpServletResponse();

    	healthCheckEndpoint.healthCheck(response);

        Assert.assertTrue(response.getStatus() == HttpStatus.SC_INTERNAL_SERVER_ERROR);

    }

}

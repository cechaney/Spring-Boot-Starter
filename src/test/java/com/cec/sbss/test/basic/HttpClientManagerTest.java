package com.cec.sbss.test.basic;

import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.cec.sbss.client.support.HttpClientManager;

public class HttpClientManagerTest {


    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testInit(){

        final int INT_CONSTANT = 100;

        HttpClientManager hcm = new HttpClientManager();

        hcm.setMaxConnectionsPerRoute(INT_CONSTANT);
        hcm.setMaxPoolConnections(INT_CONSTANT);
        hcm.setSocketConnectTimeout(INT_CONSTANT);
        hcm.setSocketReadTimeout(INT_CONSTANT);

        hcm.init();

        Assert.assertNotNull(hcm);
        Assert.assertNotNull(hcm.getMaxConnectionsPerRoute());
        Assert.assertNotNull(hcm.getMaxPoolConnections());
        Assert.assertNotNull(hcm.getRestTemplate());
        Assert.assertNotNull(hcm.getSocketReadTimeout());

    }

    @Test
    public void testAccessorsGettersAndSetters(){

        final int INT_CONSTANT = 100;
        final RestTemplate RT_CONSTANT = new RestTemplate();

        HttpClientManager hcm = new HttpClientManager();

        hcm.setMaxConnectionsPerRoute(INT_CONSTANT);
        Assert.assertEquals(INT_CONSTANT, hcm.getMaxConnectionsPerRoute());

        hcm.setMaxPoolConnections(INT_CONSTANT);
        Assert.assertEquals(INT_CONSTANT, hcm.getMaxConnectionsPerRoute());

        hcm.setRestTemplate(RT_CONSTANT);
        Assert.assertEquals(RT_CONSTANT, hcm.getRestTemplate());

        hcm.setSocketConnectTimeout(INT_CONSTANT);
        Assert.assertEquals(INT_CONSTANT, hcm.getSocketConnectTimeout());

        hcm.setSocketReadTimeout(INT_CONSTANT);
        Assert.assertEquals(INT_CONSTANT, hcm.getSocketReadTimeout());

    }

    public void testExternalCall(){

        HttpClientManager hcm = new HttpClientManager();

        hcm.init();

        RestTemplate rt = hcm.getRestTemplate();

        Assert.assertNotNull(rt);

        ResponseEntity<String> google = rt.getForEntity("http://www.google.com", String.class);

        Assert.assertEquals(google.getStatusCode(), HttpStatus.SC_OK);

        Assert.assertNotNull(google);

    }

}

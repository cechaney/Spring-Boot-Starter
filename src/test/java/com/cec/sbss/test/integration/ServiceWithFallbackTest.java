package com.cec.sbss.test.integration;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.cec.sbss.App;
import com.cec.sbss.client.ClientWithFallback;
import com.cec.sbss.service.ServiceWithFallback;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=App.class, webEnvironment=WebEnvironment.RANDOM_PORT)
public class ServiceWithFallbackTest {

    private static ServiceWithFallback swfb = new ServiceWithFallback();

    @BeforeClass
    public static void setUpClass() {
        swfb.setFallbackClient(new ClientWithFallback());
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
    public void testNoFallback(){

        ResponseEntity<String> re = swfb.noFallback();

        Assert.assertNotNull(re);
        Assert.assertEquals(re.getStatusCode(), HttpStatus.OK);

    }

    @Test
    public void testExceptionFallback(){

        ResponseEntity<String> re = null;

        re = swfb.exceptionFallback(false);
        Assert.assertNull(re);

        re = swfb.exceptionFallback(true);
        Assert.assertNotNull(re);

    }


    @Test
    public void testTimeoutFallback(){

        ResponseEntity<String> re = swfb.timeoutFallback();

        Assert.assertNotNull(re);
        Assert.assertEquals(re.getStatusCode(), HttpStatus.OK);

        Thread.currentThread().interrupt();
        re = swfb.timeoutFallback();
        Assert.assertNull(re);


    }

    @Test
    public void testGetFallbackClient(){

        ServiceWithFallback swfb2 = new ServiceWithFallback();

        swfb2.setFallbackClient(new ClientWithFallback());

        Assert.assertNotNull(swfb2.getFallbackClient());


    }

}

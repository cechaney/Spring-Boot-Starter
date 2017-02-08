package com.cec.sbs.test.integration;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.cec.sbs.App;
import com.cec.sbs.client.ClientWithFallback;
import com.cec.sbs.client.ClientWithFallback.ClientException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=App.class, webEnvironment=WebEnvironment.RANDOM_PORT)
public class ClientWithFallbackTest {

    @Autowired
    private ClientWithFallback cwfb;

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
    public void testNoFallback(){

        ResponseEntity<String> re = cwfb.noFallback();

        Assert.assertNotNull(re);
        Assert.assertEquals(re.getStatusCode(), HttpStatus.OK);

    }

    @Test
    public void testExceptionFallback(){

        ClientWithFallback cwfb = new ClientWithFallback();

        ResponseEntity<String> re = null;

        try {
            re = cwfb.exceptionFallback(false);
        } catch (ClientException e) {

        }

        Assert.assertNull(re);

    }


    @Test
    public void testTimeoutFallback(){

        ClientWithFallback cwfb = new ClientWithFallback();

        ResponseEntity<String> re = cwfb.timeoutFallback();

        Assert.assertNotNull(re);
        Assert.assertEquals(re.getStatusCode(), HttpStatus.OK);

        Thread.currentThread().interrupt();
        re = cwfb.timeoutFallback();
        Assert.assertNull(re);


    }

    @Test
    public void testFallback(){

        ClientWithFallback cwfb = new ClientWithFallback();

        ResponseEntity<String> re = cwfb.fallback();

        Assert.assertNotNull(re);
        Assert.assertEquals(re.getStatusCode(), HttpStatus.OK);

    }

}

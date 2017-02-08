package com.cec.sbs.test.integration;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.cec.sbs.App;
import com.cec.sbs.filter.StaticAssetFilter;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=App.class, webEnvironment=WebEnvironment.RANDOM_PORT)
public class StaticAssetFilterTest {

    @Value("${local.server.port}")
    private int port;

    @Value("${server.context-path}")
    private String context;

    private RestTemplate rt = new RestTemplate();


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
    public void testStaticContent() throws Exception{

        HttpHeaders headers = rt.headForHeaders("http://localhost:" + port + context + "/img/code.png");

        Assert.assertNotNull(headers);
        Assert.assertEquals("image/png", headers.getContentType().toString());

    }

    @Test
    public void testFilterLifecycle(){

        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse res = new MockHttpServletResponse();
        MockFilterChain fc = new MockFilterChain();
        MockFilterConfig config = new MockFilterConfig();

        StaticAssetFilter filter = new StaticAssetFilter();

        try{

            filter.init(config);

            filter.doFilter(req, res, fc);

            Assert.assertNotNull(res);

            filter.destroy();

        } catch(IOException | ServletException ex){
            Assert.fail();
        }

    }

}

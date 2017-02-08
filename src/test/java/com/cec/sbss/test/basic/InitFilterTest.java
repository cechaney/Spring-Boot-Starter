package com.cec.sbss.test.basic;

import com.cec.sbss.filter.InitFilter;
import com.cec.sbss.filter.SecurityFilter;
import org.junit.*;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;

public class InitFilterTest {

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
    public void testFilterLifecycle(){

        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse res = new MockHttpServletResponse();
        MockFilterChain fc = new MockFilterChain();
        MockFilterConfig config = new MockFilterConfig();

        InitFilter filter = new InitFilter();

        try{

            config.addInitParameter(SecurityFilter.REDIRECT_VIEW_CONFIG_PARAM, "error.html");

            filter.init(config);

            filter.doFilter(req, res, fc);

            Assert.assertNotNull(res);

            filter.destroy();

        } catch(IOException | ServletException ex){
            Assert.fail();
        }

    }

}

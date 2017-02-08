package com.cec.sbss.test.integration;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import com.cec.sbss.App;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=App.class, webEnvironment=WebEnvironment.RANDOM_PORT)
public class EchoServiceTest {

    @Value("${local.server.port}")
    private int port;

    @Value("${server.context-path}")
    private String context;

    @Autowired
    private WebApplicationContext wac;

    private RestTemplate rt = new RestTemplate();

    private MockMvc mockMvc;

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @After
    public void tearDown() {

    }

    @Test
    public void testShowHelloViewEndpoint() throws Exception{

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/echo");

        request.accept(MediaType.parseMediaType("application/json;charset=UTF-8"));

        request.param("value", "echo");

        MvcResult result = mockMvc.perform(request)
                .andExpect(
                        MockMvcResultMatchers.status().isOk()
                ).andReturn();

        Assert.assertNotNull(result.getResponse().getContentAsString());
    }

    @Test
    public void testBadParametersShowHelloViewEndpoint() throws Exception{

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/echo");

        request.accept(MediaType.parseMediaType("application/json;charset=UTF-8"));

        MvcResult result = mockMvc.perform(request)
                .andExpect(
                        MockMvcResultMatchers.status().isBadRequest()
                ).andReturn();

        Assert.assertTrue(StringUtils.isEmpty(result.getResponse().getContentAsString()));
    }

    @Test
    public void testShowHomeViewEndpoint(){

        String result = rt.getForObject("http://localhost:" + port + context + "/echo?value=echoechoecho", String.class);

        Assert.assertNotNull(result);
        Assert.assertTrue(result.length() > 0);

    }
}

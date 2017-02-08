package com.cec.sbss.test.integration;

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
public class HomeEndpointTest {

    @Value("${local.server.port}")
    private int port;

    @Value("${server.context-path}")
    private String context;

    private RestTemplate rt = new RestTemplate();


    @Autowired
    private WebApplicationContext wac;

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
    public void testShowHomeViewEndpoint() throws Exception {

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/home");

        request.accept(MediaType.parseMediaType("text/html;charset=UTF-8"));

        MvcResult result = mockMvc.perform(request)
                .andExpect(
                        MockMvcResultMatchers.status().isOk()
                ).andReturn();

        Assert.assertNotNull(result.getResponse().getContentAsString());
    }

    @Test
    public void testIntegrationShowHomeViewEndpoint(){

        String result = rt.getForObject("http://localhost:" + port + context + "/home", String.class);

        Assert.assertNotNull(result);
        Assert.assertTrue(result.length() > 0);

    }

}

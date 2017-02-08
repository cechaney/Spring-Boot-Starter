package com.cec.sbss.test.integration;

import java.io.File;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.catalina.Valve;
import org.apache.catalina.valves.AccessLogValve;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.Compression;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.JspServlet;
import org.springframework.boot.context.embedded.MimeMappings;
import org.springframework.boot.context.embedded.Ssl;
import org.springframework.boot.context.embedded.SslStoreProvider;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.test.context.junit4.SpringRunner;

import com.cec.sbss.App;
import com.cec.sbss.config.AccessLogConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=App.class, webEnvironment=WebEnvironment.RANDOM_PORT)
public class AccessLogConfigurationTest {

    @Value("${server.tomcat.accesslog.directory}")
    private String logDirectory;

    @Value("${server.tomcat.accesslog.prefix}")
    private String logFilePrefix;

    @Value("${server.port}")
    private int port;

    @Value("${server.tomcat.access-log-pattern:%A %h %l %u %t '%r' %s %b %D}")
    private String logPattern;

    @Autowired
    private AccessLogConfiguration config;

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
    public void testAccessLogConfiguration(){

        Assert.assertNotNull(config);

        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();

        config.customize(factory);

		List<Valve> valves = new ArrayList<>(factory.getContextValves());

        for(Valve valve : valves){

            if(valve instanceof AccessLogValve){

                AccessLogValve av = (AccessLogValve) valve;

                Assert.assertTrue(av.getEnabled());
                Assert.assertTrue(av.isRenameOnRotate());
                Assert.assertTrue(logDirectory.equals(av.getDirectory()));
                Assert.assertTrue(logFilePrefix.equals(av.getPrefix()));
                Assert.assertTrue(".log".equals(av.getSuffix()));
                Assert.assertTrue(logPattern.equals(av.getPattern()));
                Assert.assertTrue(port == factory.getPort());

                break;
            }
        }

    }

    @Test
    public void testBadAccessLogConfiguration(){

        TestingEmbeddedServletContainerFactory factory = new TestingEmbeddedServletContainerFactory();

        config.customize(factory);

        Assert.assertTrue(factory.getPort() == 0);

    }

    public class TestingEmbeddedServletContainerFactory implements ConfigurableEmbeddedServletContainer{

        private int port = 0;

        public int getPort(){
            return this.port;
        }

		@Override
		public void addErrorPages(ErrorPage... arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void addInitializers(ServletContextInitializer... arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void setAddress(InetAddress arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void setCompression(Compression arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void setContextPath(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void setDisplayName(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void setDocumentRoot(File arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void setErrorPages(Set<? extends ErrorPage> arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void setInitializers(List<? extends ServletContextInitializer> arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void setJspServlet(JspServlet arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void setLocaleCharsetMappings(Map<Locale, Charset> arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void setMimeMappings(MimeMappings arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void setPersistSession(boolean arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void setPort(int arg0) {
			this.port = arg0;
		}

		@Override
		public void setRegisterDefaultServlet(boolean arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void setServerHeader(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void setSessionStoreDir(File arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void setSessionTimeout(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void setSessionTimeout(int arg0, TimeUnit arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void setSsl(Ssl arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void setSslStoreProvider(SslStoreProvider arg0) {
			// TODO Auto-generated method stub

		}


    }

}

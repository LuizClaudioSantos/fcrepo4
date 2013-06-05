
package org.fcrepo.integration.generator;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.fcrepo.utils.FedoraJcrTypes;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public abstract class AbstractResourceIT {

    protected Logger logger;

    @Before
    public void setLogger() {
        logger = LoggerFactory.getLogger(this.getClass());
    }

    protected static final int SERVER_PORT = Integer.parseInt(System
            .getProperty("test.port", "8080"));

    protected static final String HOSTNAME = "localhost";

    protected static final String serverAddress = "http://" + HOSTNAME + ":" +
            SERVER_PORT + "/";

    protected static final String serverOAIAddress = "http://" + HOSTNAME +
            ":" + SERVER_PORT + "/oai/";

    protected final PoolingClientConnectionManager connectionManager =
            new PoolingClientConnectionManager();

    protected static HttpClient client;

    public AbstractResourceIT() {
        connectionManager.setMaxTotal(Integer.MAX_VALUE);
        connectionManager.setDefaultMaxPerRoute(5);
        connectionManager.closeIdleConnections(3, TimeUnit.SECONDS);
        client = new DefaultHttpClient(connectionManager);
    }

    protected static HttpPost postObjMethod(final String pid) {
        return new HttpPost(serverAddress + "objects/" + pid);
    }

    protected static HttpPost postDSMethod(final String pid, final String ds,
            final String content) throws UnsupportedEncodingException {
        final HttpPost post =
                new HttpPost(serverAddress + "objects/" + pid +
                        "/" + ds + "?mixin=" + FedoraJcrTypes.FEDORA_DATASTREAM);
        post.setEntity(new StringEntity(content));
        return post;
    }

    protected static HttpPut putDSMethod(final String pid, final String ds) {
        return new HttpPut(serverAddress + "objects/" + pid +
                "/" + ds + "?mixin=" + FedoraJcrTypes.FEDORA_DATASTREAM);
    }

    protected int getStatus(final HttpUriRequest method)
            throws ClientProtocolException, IOException {
        logger.debug("Executing: " + method.getMethod() + " to " +
                method.getURI());
        return client.execute(method).getStatusLine().getStatusCode();
    }

}

package com.globo.pepe.api.services;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.globo.pepe.api.mocks.AmqpMockConfiguration;
import com.globo.pepe.common.services.AmqpService;
import com.globo.pepe.common.services.JsonLoggerService;
import com.rabbitmq.http.client.domain.QueueInfo;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Set;
import org.apache.commons.io.IOUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockserver.integration.ClientAndServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {
    AmqpMockConfiguration.class,
    AmqpService.class,
    ObjectMapper.class,
    JsonLoggerService.class
}, loader = AnnotationConfigContextLoader.class)
@TestPropertySource(properties = {
    "amqp.management.url=http://127.0.0.1:15672/api",
    "amqp.management.login=guest",
    "amqp.management.password=guest"
})
public class AmqpServiceTests {

    private static ClientAndServer mockServer;

    @Autowired
    private AmqpService amqpService;

    @BeforeClass
    public static void setupClass() throws IOException {
        mockServer = ClientAndServer.startClientAndServer(15672);

        InputStream resourceAuthOk = AmqpServiceTests.class.getResourceAsStream("/queues.json");
        String bodyAuthOk = IOUtils.toString(resourceAuthOk, Charset.defaultCharset());
        mockServer.when(request().withMethod("GET").withPath("/api/queues/"))
            .respond(response().withBody(bodyAuthOk).withHeader("Content-Type", APPLICATION_JSON_VALUE).withStatusCode(200));
    }

    @AfterClass
    public static void cleanup() {
        if (mockServer.isRunning()) {
            mockServer.stop();
        }
    }

    @Test
    public void notNullTest() {
        Assert.assertNotNull(amqpService);
    }

    @Test
    public void getQueuesTest() {
        Set<QueueInfo> queues;
        Assert.assertFalse((queues = amqpService.queuesFromRabbit("pepe.trigger.")) == null || queues.isEmpty());
    }

}
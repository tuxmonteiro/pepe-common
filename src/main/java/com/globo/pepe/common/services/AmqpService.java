/*
 * Copyright (c) 2019 - Globo.com - ATeam
 * All rights reserved.
 *
 * This source is subject to the Apache License, Version 2.0.
 * Please see the LICENSE file for more information.
 *
 * Authors: See AUTHORS file
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.globo.pepe.common.services;

import com.rabbitmq.http.client.Client;
import com.rabbitmq.http.client.domain.QueueInfo;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AmqpService {

    @Value("${amqp.management.url}") String managementUrl;
    @Value("${amqp.management.login}") String managementLogin;
    @Value("${amqp.management.password}") String managementPassword;

    private final RabbitTemplate template;
    private final RabbitAdmin admin;
    private final ConnectionFactory connectionFactory;

    private final Map<String, SimpleMessageListenerContainer> messageListenerContainerMap = new HashMap<>();
    private final Map<String, List<MessageListener>> messageListeners = new HashMap<>();
    private final JsonLoggerService jsonLoggerService;

    private Client client = null;

    public AmqpService(
        ConnectionFactory connectionFactory,
        RabbitTemplate template,
        RabbitAdmin admin,
        JsonLoggerService jsonLoggerService) throws MalformedURLException, URISyntaxException {

        this.connectionFactory = connectionFactory;
        this.template = template;
        this.admin = admin;
        this.jsonLoggerService = jsonLoggerService;
//        this.managementClient = new Client("http://127.0.0.1:15672", "guest", "guest");
    }

    public boolean hasQueue(String queue) {
        return messageListeners.containsKey(queue);
    }

    public Set<String> queuesRegistered() {
        return messageListeners.keySet();
    }

    public ConnectionFactory connectionFactory() {
        return connectionFactory;
    }

    public void convertAndSend(String queue, String message, long ttl) {
        template.convertAndSend(queue, message, m -> {
            m.getMessageProperties().setExpiration(String.valueOf(ttl));
            return m;
        });
    }

    public boolean exist(String queueName) {
        try {
            final String queueNameProd = (String) admin.getQueueProperties(queueName).get(RabbitAdmin.QUEUE_NAME);
            return queueNameProd != null;
        } catch (Exception ignore) {
            // ignored
        }
        return false;
    }

    public boolean newQueue(String queueName) throws RuntimeException {
        messageListeners.computeIfAbsent(queueName, q -> new ArrayList<>());
        if (exist(queueName)) {
            return false;
        }
        final Queue queue = new Queue(queueName);
        jsonLoggerService.newLogger(getClass()).put("message", "Queue " + queueName + " created.").sendInfo();
        return admin.declareQueue(queue) != null;
    }

    private MessageListener messageListener(String queueName) {
        return message -> {
            if (messageListeners.get(queueName).isEmpty()) {
                jsonLoggerService.newLogger(getClass())
                    .put("message", "Discarding queue message: " + message + ". Queue " + queueName + " dont have listeners registered.")
                    .sendWarn();
            }
            for (MessageListener messageListener: messageListeners.get(queueName)) {
                messageListener.onMessage(message);
            }
        };
    }

    public void prepareListenersMap(String queueName) {
        final SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addQueueNames(queueName);
        container.setAutoStartup(true);
        container.setAmqpAdmin(admin);
        container.initialize();
        container.start();
        messageListenerContainerMap.put(queueName, container);
    }

    public void stopListener(String queueName) {
        final SimpleMessageListenerContainer container;
        if ((container = messageListenerContainerMap.get(queueName)) != null) {
            container.shutdown();
            messageListeners.remove(queueName);
            messageListenerContainerMap.remove(queueName);
        } else {
            throw new IllegalStateException("queue not exist");
        }
    }

    public void registerListener(String queueName, MessageListener newMessageListener) {
        if (messageListeners.get(queueName).isEmpty()) {
            final SimpleMessageListenerContainer container = messageListenerContainerMap.get(queueName);
            container.setMessageListener(messageListener(queueName));
        }
        final List<MessageListener> listOfMessageListener = messageListeners.get(queueName);
        listOfMessageListener.add(newMessageListener);
        messageListeners.put(queueName, listOfMessageListener);
    }

    public Set<QueueInfo> queuesFromRabbit(String prefix) {
        try {
            if (this.client == null) {
                this.client = new Client(managementUrl, managementLogin, managementPassword);
            }
            final List<QueueInfo> queues = client.getQueues();
            return queues.stream().filter(q -> q.getName().startsWith(prefix)).collect(Collectors.toSet());
        } catch (Exception e) {
            jsonLoggerService.newLogger(getClass()).message(e.getMessage()).sendError(e);
        }
        return Collections.emptySet();
    }

}

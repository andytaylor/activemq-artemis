/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.activemq.artemis.tests.e2e.examples;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.qpid.jms.JmsConnectionFactory;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.utility.DockerImageName;

import static org.junit.Assert.assertEquals;

public class WIP_BrokerCluster {

   public static final String TEST_MESSAGE = "Hello world ";

   @ClassRule
   public static Network NETWORK = Network.newNetwork();

   @Rule
   public GenericContainer<?> artemis0 = new GenericContainer<>(DockerImageName.parse("artemis-centos"))
      .withExposedPorts(61616, 8161)
      .withFileSystemBind("../../brokerCluster0.xml", "/tmp/artemis-instance/etc/broker.xml")
      .withCommand("run --broker /tmp/artemis-instance/etc/broker.xml")
      .withNetwork(NETWORK)
      .withNetworkMode("host")
      .withNetworkAliases("artemis0");


   @Rule
   public GenericContainer<?> artemis1 = new GenericContainer<>(DockerImageName.parse("artemis-centos"))
      .withExposedPorts(61616, 8161)
      .withFileSystemBind("../../brokerCluster1.xml", "/tmp/artemis-instance/etc/broker.xml")
      .withCommand("run --broker /tmp/artemis-instance/etc/broker.xml")
      .withNetwork(NETWORK)
      .withNetworkMode("host")
      .withNetworkAliases("artemis1");

   @Test
   public void test1() throws JMSException {
      String cluster0Address = artemis0.getHost();
      int cluster0Port = artemis0.getMappedPort(61616);
      int cluster0Console = artemis0.getMappedPort(8161);

      String cluster1Address = artemis1.getHost();
      int cluster1Port = artemis1.getMappedPort(61616);
      int cluster1Console = artemis1.getMappedPort(8161);

      ConnectionFactory connectionFactory = new JmsConnectionFactory("amqp://" + cluster0Address + ":" + cluster0Port);

      // Step 1. Create an amqp qpid 1.0 connection
      try (Connection connection = connectionFactory.createConnection("artemis", "artemis")) {

         // Step 2. Create a session
         Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

         // Step 3. Create a sender
         Queue queue = session.createQueue("exampleQueue");
         MessageProducer sender = session.createProducer(queue);

         // Step 4. send a few simple message
         sender.send(session.createTextMessage(TEST_MESSAGE));

         connection.start();

         // Step 5. create a moving receiver, this means the message will be removed from the queue
         MessageConsumer consumer = session.createConsumer(queue);

         // Step 7. receive the simple message
         TextMessage m = (TextMessage) consumer.receive(5000);
         System.out.println("message = " + m.getText());

         assertEquals("Message contents sent is the same that was consumed", TEST_MESSAGE, m.getText());

      }
   }

}

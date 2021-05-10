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

import org.apache.activemq.artemis.test.clientendpoint.TestClient;
import org.apache.qpid.jms.JmsConnectionFactory;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

public class TestProducer extends TestClient {
   public static final String TEST_MESSAGE = "Hello world ";

   public void runTest(String[] args) throws Exception {

      ConnectionFactory connectionFactory = new JmsConnectionFactory("amqp://" + getHost() + ":" + getPort());

      // Step 1. Create an amqp qpid 1.0 connection
      try (Connection connection = connectionFactory.createConnection("artemis", "artemis")) {

         // Step 2. Create a session
         Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

         // Step 3. Create a sender
         Queue queue = session.createQueue("exampleQueue");
         MessageProducer sender = session.createProducer(queue);

         // Step 4. send a few simple message
         sender.send(session.createTextMessage(TEST_MESSAGE));
      }
   }
}

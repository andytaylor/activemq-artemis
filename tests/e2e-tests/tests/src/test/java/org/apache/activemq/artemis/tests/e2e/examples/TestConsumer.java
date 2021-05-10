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
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;


public class TestConsumer extends TestClient {

   public static final String TEST_MESSAGE = "Hello world ";

   public void runTest(String[] args) throws Exception {

      ConnectionFactory connectionFactory = new JmsConnectionFactory("amqp://" + getHost() + ":" + getPort());

      try (Connection connection = connectionFactory.createConnection("artemis", "artemis")) {

         Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

         Queue queue = session.createQueue("exampleQueue");

         connection.start();

         MessageConsumer consumer = session.createConsumer(queue);

         TextMessage m = (TextMessage) consumer.receive(5000);

         System.out.println("message = " + m.getText());
      }
   }
}

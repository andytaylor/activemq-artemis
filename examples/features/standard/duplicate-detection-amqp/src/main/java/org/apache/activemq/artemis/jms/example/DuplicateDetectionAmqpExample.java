/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.activemq.artemis.jms.example;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.TransactionRolledBackException;
import javax.naming.InitialContext;

import org.apache.activemq.artemis.api.core.Message;
import org.apache.activemq.artemis.util.ServerUtil;
import org.apache.qpid.jms.JmsConnectionFactory;

/**
 * A simple example that demonstrates failover of the JMS connection from one node to another
 * when the live server crashes using a JMS <em>transacted</em> session.
 */
public class DuplicateDetectionAmqpExample {

   // You need to guarantee uniqueIDs when using duplicate detection
   // It needs to be unique even after a restart
   // as these IDs are stored on the journal for control
   // We recommend some sort of UUID, but for this example the Current Time as string would be enough
   static String uniqueID = Long.toString(System.currentTimeMillis());

   private static Process server0;

   public static void main(final String[] args) throws Exception {
      Connection connection = null;

      try {
         server0 = ServerUtil.startServer(args[0], DuplicateDetectionAmqpExample.class.getSimpleName() + "0", 0, 5000);

         String queueName = "exampleQueue";
         String url = "failover:(amqp://localhost:61616)?failover.maxReconnectAttempts=-1&failover.amqpOpenServerListAction=IGNORE&jms.prefetchPolicy.all=5&jms.forceSyncSend=true";
         ConnectionFactory connectionFactory = new JmsConnectionFactory(url);

         // We create a JMS Connection
         connection = connectionFactory.createConnection();

         // We create a *non-transacted* JMS Session
         Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
         Queue queue = session.createQueue(queueName);

         connection.start();
         MessageProducer producer = session.createProducer(queue);
         MessageConsumer consumer = session.createConsumer(queue);

         // Send 2 message with the same unique id
         sendMessages(session, producer, 2);

         // Consume onw message
         TextMessage message0 = (TextMessage) consumer.receive(3000);
         if (message0 == null) {
            throw new IllegalStateException("Example failed - message wasn't received");
         }
         TextMessage message1 = (TextMessage) consumer.receive(3000);
         if (message1 != null) {
            throw new IllegalStateException("Example failed - duplicated message wasn't received");
         }

         System.out.println("Other message on the server? " + consumer.receive(5000));
      } finally {
         // Step 13. Be sure to close our resources!

         if (connection != null) {
            connection.close();
         }

         ServerUtil.killServer(server0);
      }
   }

   private static void sendMessages(final Session session,
                                    final MessageProducer producer,
                                    final int numMessages) throws Exception {

      for (int i = 0; i < numMessages; i++) {
         TextMessage message = session.createTextMessage("This is text message " + i);

         message.setStringProperty(Message.HDR_DUPLICATE_DETECTION_ID.toString(), uniqueID);

         producer.send(message);

         System.out.println("Sent message: " + message.getText());
      }
      
   }
}

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

import org.apache.activemq.artemis.api.core.Message;
import org.apache.activemq.artemis.util.ServerUtil;
import org.apache.qpid.jms.JmsConnectionFactory;

/**
 * A simple example that demonstrates failover of the JMS connection from one node to another
 * when the live server crashes using a JMS <em>transacted</em> session.
 */
public class DuplicateDetectionAmqpExample {

   static String uniqueID = Long.toString(System.currentTimeMillis());

   private static Process server0;

   public static void main(final String[] args) throws Exception {
      Connection connection = null;

      try {
         server0 = ServerUtil.startServer(args[0], DuplicateDetectionAmqpExample.class.getSimpleName() + "0", 0, 5000);

         String queueName = "exampleQueue";
         String url = "failover:(amqp://localhost:61616)?failover.maxReconnectAttempts=-1&failover.amqpOpenServerListAction=IGNORE&jms.prefetchPolicy.all=5&jms.forceSyncSend=true";
         ConnectionFactory connectionFactory = new JmsConnectionFactory("amq","secret",url);

         // We create a JMS Connection
         connection = connectionFactory.createConnection();

         // We create a *non-transacted* JMS Session
         Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
         Queue queue = session.createQueue(queueName);

         // We create a *transacted* JMS Session
         Session sessionTransacted = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
         Queue queueTransacted = sessionTransacted.createQueue(queueName);

         connection.start();

         /***************
          *  Non-transacted duplicated message send
          ***************/
         MessageProducer producer = session.createProducer(queue);
         MessageConsumer consumer = session.createConsumer(queue);
         MessageProducer producerTransacted = sessionTransacted.createProducer(queueTransacted);

         // Send 2 message with the same unique id
         sendMessage(session, producer, uniqueID);
         sendMessage(session, producer, uniqueID);
         // Expect one message only
         TextMessage message0 = (TextMessage) consumer.receive(5000);
         if (message0 == null) {
            throw new IllegalStateException("Example failed - message wasn't received");
         }
         TextMessage message1 = (TextMessage) consumer.receive(5000);
         if (message1 != null) {
            throw new IllegalStateException("Example failed - duplicated message was received");
         }

         /***************
          *  Transacted duplicated message send
          ***************/

         // Send 2 message with the same unique id
         sendMessage(sessionTransacted, producerTransacted,uniqueID+"-transacted");
         sessionTransacted.commit();
         sendMessage(sessionTransacted, producerTransacted,uniqueID+"-transacted");
         sessionTransacted.commit(); //Exception happens here: javax.jms.TransactionRolledBackException: Duplicate message detected

         // Expect one message only
         message0 = (TextMessage) consumer.receive(5000);
         if (message0 == null) {
            throw new IllegalStateException("Example failed - transacted message wasn't received");
         }
         message1 = (TextMessage) consumer.receive(5000);
         if (message1 != null) {
            throw new IllegalStateException("Example failed - transacted duplicated message was received");
         }

         System.out.println("Other message on the server? " + consumer.receive(5000));
      } finally {

         if (connection != null) {
            connection.close();
         }

         ServerUtil.killServer(server0);
      }
   }

   private static void sendMessage(final Session session,
                                   final MessageProducer producer,
                                   final String duplId) throws Exception {

         TextMessage message = session.createTextMessage("Hello " + duplId);
         message.setStringProperty(Message.HDR_DUPLICATE_DETECTION_ID.toString(), duplId);

         System.out.println("Sending message: " + message.getText());
         producer.send(message);

   }
}

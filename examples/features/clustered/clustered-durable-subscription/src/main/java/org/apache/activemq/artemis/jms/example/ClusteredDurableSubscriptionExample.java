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
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

/**
 * A simple example that shows a JMS Durable Subscription across two nodes of a cluster.
 *
 * The same durable subscription can exist on more than one node of the cluster, and messages
 * sent to the topic will be load-balanced in a round-robin fashion between the two nodes
 */
public class ClusteredDurableSubscriptionExample {

   public static void main(final String[] args) throws Exception {
      Connection connection0 = null;

      Connection connection1 = null;

      try {
         // Step 1. Instantiate the connection factory on server 0
         ConnectionFactory cf0 = new ActiveMQConnectionFactory("tcp://localhost:61616");

         // Step 2. nstantiate the connection factory on server 1
         ConnectionFactory cf1 = new ActiveMQConnectionFactory("tcp://localhost:61617");

         // Step 3. We create a JMS Connection connection0 which is a connection to server 0
         // and set the client-id
         connection0 = cf0.createConnection();

         final String clientID = "my-client-id";

         connection0.setClientID(clientID);

         // Step 5. We create a JMS Session on server 0
         final Session session0 = connection0.createSession(false, Session.AUTO_ACKNOWLEDGE);

         // Step 7. We start the connections to ensure delivery occurs on them
         connection0.start();

         // Step 8. We create JMS durable subscriptions with the same name and client-id on both nodes
         // of the cluster

         final String subscriptionName = "my-subscription";

         // Step 9. lookup the topic
         Topic topic = session0.createTopic("exampleTopic");

         MessageConsumer subscriber0 = session0.createDurableSubscriber(topic, subscriptionName);

         Thread.sleep(1000);

         // Step 10. We create a JMS MessageProducer object on server 0
         MessageProducer producer = session0.createProducer(topic);

         for (int i = 0; i < 10; i++) {
            TextMessage message = session0.createTextMessage("This is text message " + i);

            producer.send(message);

            System.out.println("Sent message: " + message.getText());
         }

         // Step 12. We now consume those messages on *both* server 0 and server 1.
         // Note that the messages have been load-balanced between the two nodes, with some
         // messages on node 0 and others on node 1.
         // The "logical" subscription is distributed across the cluster and contains exactly one copy of all the
         // messages

         for (int i = 0; i < 5; i ++) {
            TextMessage message0 = (TextMessage) subscriber0.receive(5000);

            System.out.println("Got message: " + message0.getText() + " from node 0");
         }
         subscriber0.close();

         Connection connection = cf1.createConnection();

         connection.setClientID(clientID);

         Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

         connection.start();

         subscriber0 = session.createDurableSubscriber(topic, subscriptionName);

         Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
               for (int i = 0; i < 10; i++) {
                  try {
                     TextMessage message = session0.createTextMessage("This is text message " + i);

                     producer.send(message);

                     System.out.println("Sent message: " + message.getText());
                  } catch (JMSException e) {
                     e.printStackTrace();
                  }
               }
            }
         });
         t.start();
         for (int i = 0; i < 15; i ++) {
            TextMessage message0 = (TextMessage) subscriber0.receive(5000);

            System.out.println("Got message: " + message0.getText() + " from node 1");
         }
         System.out.println("check the console to see that the subscritipn queue has been deleted then press enter");
         System.in.read();

      } finally {
         // Step 15. Be sure to close our JMS resources!
         if (connection0 != null) {
            connection0.close();
         }

         if (connection1 != null) {
            connection1.close();
         }

         Thread.sleep(1000);
      }
   }
}

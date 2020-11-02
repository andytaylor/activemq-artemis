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

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.jms.Message;
import javax.jms.QueueBrowser;
import javax.jms.Session;

import org.apache.activemq.artemis.util.ServerUtil;
import org.messaginghub.pooled.jms.JmsPoolConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.JmsListenerEndpointRegistry;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@EnableJms
@EnableScheduling
@SpringBootApplication
public class TransactionFailoverSpringBoot implements CommandLineRunner {

   private static final Logger log = LoggerFactory.getLogger(TransactionFailoverSpringBoot.class);

   private static Process server0;
   private static Process server1;
   public static void main(String[] args) {
      SpringApplication.run(TransactionFailoverSpringBoot.class, args);
   }

   @Autowired
   private JmsTemplate jmsTemplate;

   @Autowired
   JmsListenerEndpointRegistry jmsListenerEndpointRegistry;

   @Autowired
   private ConfigurableApplicationContext applicationContext;

   @Value("${receive.queue}")
   String receiveQueue;

   @Value("${send.count}")
   Integer sendCount;

   @Value("${receive.forward.queue}")
   String forwardQueue;

   private AtomicInteger receiveCounter = new AtomicInteger();
   private AtomicInteger sendCounter = new AtomicInteger();
   private AtomicInteger receiveForwardedCounter = new AtomicInteger();
   private int receiveCounterLast = 0;

   @Override
   public void run(String... args) throws Exception {

      try {
         server0 = ServerUtil.startServer(args[0], TransactionFailoverSpringBoot.class.getSimpleName() + "0", 0, 5000);
         server1 = ServerUtil.startServer(args[1], TransactionFailoverSpringBoot.class.getSimpleName() + "1", 1, 5000);

         //Send messages -
         for (int i=0; i< sendCount; i++) {
            sendMessage(receiveQueue);
         }

         log.info("Total sent: {}",sendCounter.get());

         Thread.sleep(2000);
         log.info("Start listeners");
         jmsListenerEndpointRegistry.start();

         //Wait until we received 10% of messages
         while (receiveCounter.get() < sendCount/10) {
            Thread.sleep(100);
         }

         //Broker failover
         ServerUtil.killServer(server0);

         //Wait until we received all of messages, and no more was incoming in the last second
         while (receiveCounter.get() < sendCount || receiveCounter.get() > receiveCounterLast) {
            Thread.sleep(1000);
         }

         log.info("Wait a bit more before shutdown...");
         Thread.sleep(15000);

         log.info("Counting...");
         
         int targetCount = jmsTemplate.browse(forwardQueue, (Session session, QueueBrowser browser) ->{
            Enumeration enumeration = browser.getEnumeration();
            int counter = 0;
            while (enumeration.hasMoreElements()) {
               Message msg = (Message) enumeration.nextElement();
               counter += 1;
            }
            return counter;
         });

         int DLQCount = jmsTemplate.browse("DLQ", (Session session, QueueBrowser browser) ->{
            Enumeration enumeration = browser.getEnumeration();
            int counter = 0;
            while (enumeration.hasMoreElements()) {
               Message msg = (Message) enumeration.nextElement();
               log.info("DLQ message: {} - {}",msg.getStringProperty("_AMQ_DUPL_ID"), msg.getStringProperty("SEND_COUNTER"));
               counter += 1;
            }
            return counter;
         });
         
         log.info("Sent messages: {}",sendCounter.get());
         log.info("Message count on target queue: {}",targetCount);
         log.warn("Duplicates on DLQ: {}", DLQCount);
         //Number of messages on DLQ should be 0 for a seamless failover

      } finally {
         //Shut down listeners and scheduled tasks
         log.info("Stop applicationContext");
         applicationContext.close();

         log.info("Shut down servers");
         ServerUtil.killServer(server0);
         ServerUtil.killServer(server1);
         log.info("Done");
      }
   }

   public void sendMessage(String sendQuque) {
      String uuid = UUID.randomUUID().toString();
      log.debug("Sending: {}", uuid);

      this.jmsTemplate.convertAndSend(sendQuque, "message: "+uuid, m -> {
         m.setStringProperty("_AMQ_DUPL_ID", uuid);
         m.setStringProperty("SEND_COUNTER", ""+sendCounter.incrementAndGet());
         return m;
      });

   }

   Map<String,String> amqDuplIds = new ConcurrentHashMap<>();

   @JmsListener(destination = "${receive.queue}", concurrency="${receive.concurrentConsumers}")
   public void receiveMessage(String text, @Header("SEND_COUNTER") String counter, @Header("_AMQ_DUPL_ID") String amqDuplId) {
      //Receive is transactional
      log.debug("Received: {} - {}", amqDuplId, counter);
      receiveCounter.incrementAndGet();
      if (amqDuplIds.put(amqDuplId,counter) != null) {
         log.warn("Received again: {} - {}", amqDuplId, counter);
      }

      //Send also participates in the transaction
      this.jmsTemplate.convertAndSend(forwardQueue, text, m -> {
         m.setStringProperty("SEND_COUNTER", counter);
         m.setStringProperty("_AMQ_DUPL_ID", amqDuplId);
         return m;
      });
      receiveForwardedCounter.incrementAndGet();
      log.debug("Forwarded: {} - {}", amqDuplId, counter);

      log.debug("Done: {} - {}", amqDuplId, counter);
      //Commit
   }

   @Scheduled(fixedRate = 1000)
   public void reportCurrentTime() {
      int current = receiveCounter.get();
      int diff = current - receiveCounterLast;
      receiveCounterLast = current;
      log.info("sent: {}, received: {} ({}/s), forwarded: {}", sendCounter.get(), current, diff, receiveForwardedCounter.get());
   }

}

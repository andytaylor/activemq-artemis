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
package org.apache.activemq.artemis.tests.rules;

import org.apache.activemq.artemis.test.brokerendpoint.Broker;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.Iterator;
import java.util.ServiceLoader;

public class BrokerRule implements TestRule {

   private final Broker broker;

   private boolean primary = true;
   private String brokerName;

   public BrokerRule(String brokerName) {
      super();
      this.brokerName = brokerName;
      String brokerContainerName = System.getProperty("artemis.broker.test.container");
      Iterator<Broker> it = ServiceLoader.load(Broker.class, BrokerRule.class.getClassLoader()).iterator();
      Broker firstBroker = null;
      Broker theBroker = null;
      while (it.hasNext()) {
         Broker broker = it.next();
         if (firstBroker == null) {
            firstBroker = broker;
         }
         if (brokerContainerName != null && brokerContainerName.equalsIgnoreCase(broker.getName())) {
            theBroker = broker;
            break;
         }
      }
      this.broker = theBroker != null ? theBroker : firstBroker;
   }


   @Override
   public Statement apply(Statement statement, Description description) {
      return broker.apply(statement, description);
   }

   public Broker getBroker() {
      return broker;
   }

   public BrokerRule isPrimary(boolean primary) {
      this.primary = primary;
      return this;
   }

   public BrokerRule init() {
      broker.init(brokerName, primary);
      return this;
   }
}

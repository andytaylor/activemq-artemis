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

import org.apache.activemq.artemis.test.clientendpoint.Client;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.Iterator;
import java.util.ServiceLoader;

public class ClientRule  implements TestRule {

   private final Client client;
   private final String clientName;
   private final BrokerRule brokerRule;
   private final Class testClient;
   private String[] args;

   public ClientRule(String clientName, BrokerRule brokerRule, Class testClient, String[] args) {
      super();
      this.clientName = clientName;
      this.brokerRule = brokerRule;
      this.testClient = testClient;
      this.args = args;
      String clientContainerName = System.getProperty("artemis.client.test.container");
      Iterator<Client> it = ServiceLoader.load(Client.class, ClientRule.class.getClassLoader()).iterator();
      Client firstClient = null;
      Client theClient = null;
      while (it.hasNext()) {
         Client client = it.next();
         if (firstClient == null) {
            firstClient = client;
         }
         if (clientContainerName != null && clientContainerName.equalsIgnoreCase(client.getName())) {
            theClient = client;
            break;
         }
      }
      this.client = theClient != null ? theClient : firstClient;
   }

   @Override
   public Statement apply(Statement statement, Description description) {
      return new Statement() {
         @Override
         public void evaluate() throws Throwable {
            client.run(clientName, brokerRule.getBroker().getHost(), brokerRule.getBroker().getPort(), testClient, args);
            statement.evaluate();
         }
      };
   }

   public void waitComplete() {
      client.waitForComplete();
   }

   public boolean didFail() {
      return client.didFail();
   }

   public String getfailureMessage() {
      return null;
   }
}

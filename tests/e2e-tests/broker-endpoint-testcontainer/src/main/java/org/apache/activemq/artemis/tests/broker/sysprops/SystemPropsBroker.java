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
package org.apache.activemq.artemis.tests.broker.sysprops;

import org.apache.activemq.artemis.test.brokerendpoint.Broker;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class SystemPropsBroker implements Broker {
   private String host = "localhost";
   private int port = 61616;

   public SystemPropsBroker() {
      super();
   }

   @Override
   public int getPort() {
      return port;
   }

   @Override
   public String getHost() {
      return host;
   }

   @Override
   public Statement apply(Statement statement, Description description) {
      return new Statement() {
         @Override
         public void evaluate() throws Throwable {
            statement.evaluate();
         }
      };
   }

   @Override
   public void init(String brokerName, boolean primary) {
      host = System.getProperty("SystemPropsBroker." + brokerName + ".host", "localhost");
      String sPort = System.getProperty("SystemPropsBroker." + brokerName + ".host", "61616");
      port = Integer.parseInt(sPort);
   }

   @Override
   public String getName() {
      return "artemis.broker.test.sysprops";
   }
}

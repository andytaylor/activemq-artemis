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
package org.apache.activemq.artemis.tests.broker.testcontainer;

import org.apache.activemq.artemis.test.brokerendpoint.Broker;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.OutputFrame;
import org.testcontainers.utility.DockerImageName;

import java.util.function.Consumer;

public class TestContainerBroker implements Broker {
   private static String NAME = "testcontainer";

   public GenericContainer<?> artemis;

   @Override
   public String getName() {
      return NAME;
   }

   @Override
   public int getPort() {
      return artemis.getMappedPort(61616);
   }

   @Override
   public String getHost() {
      return artemis.getHost();
   }

   @Override
   public Statement apply(Statement statement, Description description) {
      return artemis.apply(statement, description);
   }

   @Override
   public void init(String brokerName, boolean primary) {
      artemis = new GenericContainer<>(DockerImageName.parse("artemis-centos")).withExposedPorts(61616, 8161).withLogConsumer(new Consumer<OutputFrame>() {
         @Override
         public void accept(OutputFrame outputFrame) {
            System.out.println(outputFrame.getUtf8String());
         }
      });
   }
}

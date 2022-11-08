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
package org.apache.activemq.artemis.maven;

import java.util.HashMap;
import java.util.Map;

class BrokerProperties {
   Map<String, BrokerPropertyDescription> properties = new HashMap<>();

   Map<String, BrokerProperties> nestedConfigProperties = new HashMap<>();

   private final String name;

   BrokerProperties(String name) {
      this.name = name;
   }

   public String getName() {
      return name;
   }

   public void addProperty(BrokerPropertyDescription brokerPropertyDescription) {
      properties.put(brokerPropertyDescription.getPropertyName(), brokerPropertyDescription);
   }

   public BrokerProperties addConfigProperties(String name) {
      BrokerProperties brokerProperties = new BrokerProperties(name);
      nestedConfigProperties.put(brokerProperties.name, brokerProperties);
      return brokerProperties;
   }
}

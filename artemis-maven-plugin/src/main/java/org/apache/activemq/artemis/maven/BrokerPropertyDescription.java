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

import org.apache.activemq.artemis.api.config.BrokerProperty;

public class BrokerPropertyDescription {

   private final BrokerProperty brokerProperty;
   private final String propertyName;
   private final String propertyType;
   private final String xmlName;
   private final String defaultConfig;
   private final String docs;
   private final BrokerPropertyDescription parentProperty;

   public BrokerPropertyDescription(BrokerProperty brokerProperty, String propertyName, String propertyType, String xmlName, String xmlType, String defaultConfig, String docs, BrokerPropertyDescription parentProperty) {
      this.brokerProperty = brokerProperty;
      this.propertyName = propertyName;
      this.propertyType = propertyType;
      this.xmlName = xmlName;
      this.parentProperty = parentProperty;
      this.defaultConfig = defaultConfig;
      this.docs = docs;
   }

   public String getPrefix(String suffix, String instancePrefix, String instanceSuffix, String separator) {
      String prefix = getPropertyName() + separator;

      //if we have an instance name in the property use it
      if (getBrokerProperty().mappedName().length() > 0) {
         prefix += instancePrefix + getBrokerProperty().mappedName().toLowerCase() + instanceSuffix;
      }
      //if we have a suffix use it
      if (suffix != null && suffix.length() > 0) {
         prefix = prefix + suffix;
      }
      return getParentProperty() == null ? prefix : getParentProperty().getPrefix(prefix, instancePrefix, instanceSuffix, separator);
   }

   public String getPrefix(String instancePrefix, String instanceSuffix, String separator) {
      if (getParentProperty() != null) {
         return getParentProperty().getPrefix("", instancePrefix, instanceSuffix, separator);
      }
      return "";
   }


   public BrokerProperty getBrokerProperty() {
      return brokerProperty;
   }

   public String getPropertyName() {
      return propertyName;
   }

   public String getPropertyType() {
      return propertyType;
   }

   public String getXmlName() {
      return xmlName;
   }

   public BrokerPropertyDescription getParentProperty() {
      return parentProperty;
   }

   public String getDefaultConfig() {
      return defaultConfig;
   }

   public String getDocs() {
      return docs;
   }

}

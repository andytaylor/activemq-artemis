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

import java.util.Collection;

public abstract class BrokerPropertyFormatter {

   public static final String PROPERTIES_PROPERTY = ".properties.{PROPERTY}";
   public static final String PROPERTIES = "Properties";
   public static final String PROPERTY = "property";
   public static final String A_SET_OF_KEY_VALUE_PAIRS_SPECIFIC_TO_EACH_NAMED_PROPERTY_SEE_ABOVE_DESCRIPTION = "A set of Key value pairs specific to each named property, see above description";

   enum FormatType {
      MARKDOWN,
      LIST;
   }

   public static BrokerPropertyFormatter getFormatter(String format) {
      FormatType type = FormatType.valueOf(format);
      if (type.equals(FormatType.MARKDOWN)) {
         return new MarkdownFormatter();
      } else if (type.equals(FormatType.LIST)) {
         return new ListMarkdownFormatter();
      }
      throw new RuntimeException(type + " not found");
   }

   public String getOutput(BrokerProperties properties) {
      StringBuffer buffer = new StringBuffer();
      addTitle(buffer);
      printProperties(buffer, properties.properties.values());

      for (BrokerProperties nestedConfigProperty : properties.nestedConfigProperties.values()) {
         addSubTitle(buffer, nestedConfigProperty.getName());
         printNestedProperties(buffer, nestedConfigProperty);
      }
      return buffer.toString();
   }

   private void printProperties(StringBuffer buffer, Collection<BrokerPropertyDescription> properties) {
      for (BrokerPropertyDescription property : properties) {
         printProperty(buffer, property.getPropertyName(), property.getPropertyType(), property.getDefaultConfig(), property.getXmlName(), property.getDocs());

         if (property.getBrokerProperty().type() == BrokerProperty.PROPERTIES) {
            printProperty(buffer, property.getPrefix(getInstancePrefix(), getInstanceSuffix(), getSeparator()) + PROPERTIES_PROPERTY, PROPERTIES, "", PROPERTY, A_SET_OF_KEY_VALUE_PAIRS_SPECIFIC_TO_EACH_NAMED_PROPERTY_SEE_ABOVE_DESCRIPTION);
         }
      }

   }

   private void printNestedProperties(StringBuffer buffer, BrokerProperties properties) {
      for (BrokerPropertyDescription property : properties.properties.values()) {
         printProperty(buffer, property.getPrefix(getInstancePrefix(), getInstanceSuffix(), getSeparator()) + property.getPropertyName(), property.getPropertyType(), property.getDefaultConfig(), property.getXmlName(), property.getDocs());

         if (property.getBrokerProperty().type() == BrokerProperty.PROPERTIES) {
            printProperty(buffer, property.getPrefix(getInstancePrefix(), getInstanceSuffix(), getSeparator()) + PROPERTIES_PROPERTY, PROPERTIES, "", PROPERTY, A_SET_OF_KEY_VALUE_PAIRS_SPECIFIC_TO_EACH_NAMED_PROPERTY_SEE_ABOVE_DESCRIPTION);
         }

         if (property.getBrokerProperty().type() == BrokerProperty.OBJECT || property.getBrokerProperty().type() == BrokerProperty.MAP) {
            BrokerProperties brokerProperties = properties.nestedConfigProperties.get(property.getPropertyType());
            printNestedProperties(buffer, brokerProperties);
         }
      }
   }

   protected abstract String getSeparator();

   protected abstract String getInstancePrefix();

   protected abstract String getInstanceSuffix();


   abstract void printProperty(StringBuffer buffer, String propertyName, String propertyType, String defaultConfig, String xmlName, String docs);

   abstract void addTitle(StringBuffer buffer);

   abstract void addSubTitle(StringBuffer buffer, String title);

   static class MarkdownFormatter extends BrokerPropertyFormatter {

      public static final String ENDLINE = "\n";

      @Override
      void addTitle(StringBuffer buffer) {
         buffer.append("Name | Description | Default | XML Name\n").append("---|---|---|---").append("\n");
      }

      @Override
      void addSubTitle(StringBuffer buffer, String title) {
         buffer.append("**").append(title).append("** | ").append(" | ").append("\n");
      }

      @Override
      protected String getSeparator() {
         return " .";
      }

      @Override
      protected String getInstancePrefix() {
         return " ***";
      }

      @Override
      protected String getInstanceSuffix() {
         return "*** .";
      }

      @Override
      void printProperty(StringBuffer buffer, String propertyName, String propertyType, String defaultConfig, String xmlName, String docs) {
         buffer.append(propertyName).append(" | ").append(getDocs(docs)).append(" | ").append(defaultConfig.length() > 0 ? defaultConfig : "n/a").append(" | ").append(xmlName).append(ENDLINE);
      }

      private String getDocs(String docs) {
         StringBuffer buffer = new StringBuffer();
         String[] split = docs.split("\\\\n");
         for (String s : split) {
            buffer.append(s.trim());
         }
         return buffer.toString().replace(ENDLINE, "");
      }
   }

   static class ListMarkdownFormatter extends BrokerPropertyFormatter {

      private static final String NAME = "**Name**: ";

      public static final String TYPE = "Type: ";
      public static final String DEFAULT = "Default: ";
      public static final String XML_NAME = "XML name: ";
      public static final String DESCRIPTION = "Description: ";
      public static final String ENDLINE = " +";

      public static final String LINE_BREAK = "\n";
      public static final String BROKER_PROPERTIES = "= Broker Properties";
      public static final String HEADER2 = "## ";

      @Override
      protected String getSeparator() {
         return ".";
      }

      @Override
      protected String getInstancePrefix() {
         return "<__";
      }

      @Override
      protected String getInstanceSuffix() {
         return "__>.";
      }

      @Override
      void printProperty(StringBuffer buffer, String propertyName, String propertyType, String defaultConfig, String xmlName, String docs) {
         buffer.append("`").append(propertyName).append("`").append(ENDLINE).append(LINE_BREAK).append(LINE_BREAK);
         buffer.append(TYPE).append(propertyType).append(ENDLINE).append(LINE_BREAK);
         buffer.append(DEFAULT).append(defaultConfig).append(ENDLINE).append(LINE_BREAK);
         buffer.append(XML_NAME).append(xmlName).append(ENDLINE).append(LINE_BREAK);
         buffer.append(DESCRIPTION).append(getDocs(docs)).append(LINE_BREAK).append(LINE_BREAK);
      }

      @Override
      void addTitle(StringBuffer buffer) {
         buffer.append(BROKER_PROPERTIES).append(LINE_BREAK).append(LINE_BREAK);
      }

      @Override
      void addSubTitle(StringBuffer buffer, String title) {
         buffer.append(HEADER2).append(title).append(LINE_BREAK);
      }

      private String getDocs(String docs) {
         StringBuffer buffer = new StringBuffer();
         buffer.append(docs.replace("\n", "").replace("\\n", "").replaceAll("  +", " "));
         return buffer.toString().replace(ENDLINE, "");
      }
   }
}

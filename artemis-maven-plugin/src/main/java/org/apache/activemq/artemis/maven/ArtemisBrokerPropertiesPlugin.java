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
import org.apache.activemq.artemis.core.config.Configuration;
import org.apache.activemq.artemis.utils.XMLUtil;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Mojo(name = "broker-properties", defaultPhase = LifecyclePhase.PACKAGE)
public class ArtemisBrokerPropertiesPlugin extends AbstractMojo {

   public static final String NAME = "name";
   public static final String CONFIGURATION_TYPE = "configurationType";
   public static final String XSD_ALL = "xsd:all";
   public static final String XSD_ATTRIBUTE = "xsd:attribute";
   public static final String XSD_SEQUENCE = "xsd:sequence";
   public static final String XSD_CHOICE = "xsd:choice";
   public static final String XSD_COMPLEX_TYPE = "xsd:complexType";
   public static final String DEFAULT = "default";
   public static final String XSD_ANNOTATION = "xsd:annotation";
   public static final String XSD_DOCUMENTATION = "xsd:documentation";
   public static final String ROOT = "ROOT";
   public static final String EMPTY = "";
   @Parameter
   String outputFile;

   @Parameter
   String schema;

   @Parameter(defaultValue = "MARKDOWN")
   String format;

   @Override
   public void execute() throws MojoExecutionException, MojoFailureException {
      try {
         BrokerProperties brokerProperties = new BrokerProperties(ROOT);
         URL resource = XMLUtil.findResource(schema);
         Element root = XMLUtil.urlToElement(resource);
         Node core = getCoreNode(root);
         List<Method> methods = getBrokerPropertyMethods(Configuration.class);// Configuration.class.getDeclaredMethods();

         for (Method declaredMethod : methods) {
            BrokerProperty brokerProperty = declaredMethod.getAnnotation(BrokerProperty.class);
            if (brokerProperty.type() == BrokerProperty.SIMPLE) {
               String propertyName = getPropertyName(brokerProperty, declaredMethod);
               String propertyType = getPropertyType(brokerProperty, declaredMethod);
               String xmlName = getXMLName(propertyName, brokerProperty.xmlName());
               String configDefault = getDefault(xmlName, EMPTY, root, core);
               String docs = getDocumentation(xmlName, EMPTY, root, core, null, brokerProperty.extraDocs());
               brokerProperties.addProperty(new BrokerPropertyDescription(brokerProperty, propertyName, propertyType, xmlName, EMPTY, configDefault, docs, null));
            }
         }

         for (Method method : methods) {
            BrokerProperty brokerProperty = method.getAnnotation(BrokerProperty.class);
            if (brokerProperty.type() == BrokerProperty.OBJECT || brokerProperty.type() == BrokerProperty.MAP) {
               String propertyName = getPropertyName(brokerProperty, method);
               String propertyType = getPropertyType(brokerProperty, method);
               String xmlName = getXMLName(propertyName, brokerProperty.xmlName());
               String xmlType = getXMLType(brokerProperty, propertyName);
               Node node = getNode(root, xmlType);
               String configDefault = getDefault(xmlName, xmlType, root, core);
               String docs = getDocumentation(xmlName, xmlType, root, core, null, brokerProperty.extraDocs());
               BrokerPropertyDescription brokerPropertyDescription = new BrokerPropertyDescription(brokerProperty, propertyName, propertyType, xmlName, xmlType, configDefault, docs, null);
               BrokerProperties objectProperties = brokerProperties.addConfigProperties(propertyName);
               printObject(getBrokerPropertyMethods(method.getParameters()[brokerProperty.param()].getType()), root, (Element) node, null, objectProperties, brokerPropertyDescription);
            }
         }
         BrokerPropertyFormatter formatter = BrokerPropertyFormatter.getFormatter(format);
         File file = new File(outputFile);
         //file.getParentFile().mkdirs();
         BufferedWriter writer = new BufferedWriter(new FileWriter(file));
         String markDown = formatter.getOutput(brokerProperties);
         writer.append(markDown);
         writer.close();
      } catch (Exception e) {
         e.printStackTrace();
         throw new MojoExecutionException(e.getMessage());
      }
   }

   public List<Method> getBrokerPropertyMethods(Class aClass) {
      List<Method> methodsList = new ArrayList<>();
      getBrokerPropertyMethods(aClass, methodsList);
      return methodsList;
   }

   private void getBrokerPropertyMethods(Class aClass, List<Method> methodsList) {
      Method[] methods = aClass.getMethods();
      for (Method method : methods) {
         BrokerProperty brokerProperty = method.getAnnotation(BrokerProperty.class);
         if ((method.getAnnotation(Deprecated.class) == null && brokerProperty != null) && !methodsList.contains(method)) {
            methodsList.add(method);
         }
      }
      var superclass = aClass.getSuperclass();
      if (superclass != null) {
         getBrokerPropertyMethods(superclass, methodsList);
      }

      var interfaces = aClass.getInterfaces();
      for (Class anInterface : interfaces) {
         getBrokerPropertyMethods(anInterface, methodsList);
      }
   }

   private void printObject(List<Method> methods, Element root, Element configElement, Node subConfigElement, BrokerProperties objectProperties, BrokerPropertyDescription parentProperty) {
      for (Method method : methods) {
         BrokerProperty brokerProperty = method.getAnnotation(BrokerProperty.class);
         String propertyName = getPropertyName(brokerProperty, method);
         String propertyType = getPropertyType(brokerProperty, method);
         String xmlName = getXMLName(propertyName, brokerProperty.xmlName());
         String xmlType = getXMLType(brokerProperty, propertyName);
         String configDefault = getDefault(xmlName, xmlType, root, configElement);

         String docs = getDocumentation(xmlName, xmlType, root, configElement, subConfigElement, brokerProperty.extraDocs());
         BrokerPropertyDescription brokerPropertyDescription = new BrokerPropertyDescription(brokerProperty, propertyName, propertyType, xmlName, xmlType, configDefault, docs, parentProperty);
         objectProperties.addProperty(brokerPropertyDescription);
         if (brokerProperty.type() == BrokerProperty.OBJECT || brokerProperty.type() == BrokerProperty.MAP) {
            BrokerProperties nextObjectProperties = objectProperties.addConfigProperties(propertyType);
            Node nextConfigElement = getNode(root, xmlType);
            Node nextSubConfigElement = null;
            if (brokerProperty.xmlSubType().length() > 0) {
               nextSubConfigElement = getNode(root, brokerProperty.xmlSubType());
            }
            printObject(getBrokerPropertyMethods(method.getParameters()[brokerProperty.param()].getType()), root, (Element) nextConfigElement, nextSubConfigElement, nextObjectProperties, brokerPropertyDescription);
         }
      }
   }

   private String getPropertyName(BrokerProperty brokerProperty, Method declaredMethod) {
      String propertyName = brokerProperty.propertyName();
      if (propertyName.length() == 0) {
         //get the property name from the declared method
         propertyName = declaredMethod.getName().substring(3);
         //and then remove 1st capital
         propertyName = Character.toLowerCase(propertyName.charAt(0)) + (propertyName.length() > 1 ? propertyName.substring(1) : EMPTY);
         if (brokerProperty.type() == BrokerProperty.MAP) {
            //  and pluralise if its a Map/Collection
            propertyName += "s";
         }
      }
      return propertyName;
   }

   private String getPropertyType(BrokerProperty brokerProperty, Method declaredMethod) {
      Class<?> type = declaredMethod.getParameters()[brokerProperty.param()].getType();
      //we just use the class name unless its an ENUM where we want to deliver the ENUM types as well
      String propertyType = type.getSimpleName();
      if (brokerProperty.type() == BrokerProperty.ENUM) {
         propertyType = propertyType + "( ";
         Object[] enumConstants = type.getEnumConstants();
         for (Object enumConstant : enumConstants) {
            propertyType = propertyType + enumConstant + " ";
         }
         propertyType = propertyType + ")";
      }
      return propertyType;
   }

   private String getXMLType(BrokerProperty brokerProperty, String propertyName) {
      return brokerProperty.xmlType() != null ? brokerProperty.xmlType() : propertyName + "Type";
   }

   private String getTypeFromElement(Node node) {
      if (node != null && node.getAttributes() != null && node.getAttributes().getNamedItem("type") != null) {
         return node.getAttributes().getNamedItem("type").getNodeValue();
      }
      return EMPTY;
   }

   private String getDocumentation(String xmlname, String xmlType, Node root, Node configurationType, Node subConfigElement, String extraDocs) {
      String documentation = EMPTY;
      if (xmlType != null && xmlType.length() > 0) {
         Node node = getNodeByName((Element) root, xmlType);
         documentation += getDocumentationFromNode(node, xmlType);
      }
      if (documentation.length() == 0) {
         Node node = getNodeByName((Element) configurationType, xmlname);
         documentation += getDocumentationFromNode(node, xmlname);
      }

      if (documentation.length() == 0 && subConfigElement != null) {
         Node node = getNodeByName((Element) subConfigElement, xmlname);
         String name = getName(node);
         documentation += getDocumentationFromNode(node, xmlname);
      }

      if (extraDocs != null && extraDocs.length() > 0) {
         documentation = documentation + "\n" + extraDocs;
      }
      return documentation;
   }

   private String getDocumentationFromNode(Node node, String xmlName) {
      if (node == null) return EMPTY;
      Node annotation = getAnnotationNode(node, xmlName);
      if (annotation != null) {
         NodeList childNodes1 = annotation.getChildNodes();
         for (int j = 0; j < childNodes1.getLength(); j++) {
            Node item1 = childNodes1.item(j);
            if (item1.getNodeName().equals(XSD_DOCUMENTATION)) {
               String text = item1.getTextContent();
               text = text.trim();
               return text;
            }
         }
      }
      return EMPTY;
   }

   private Node getAnnotationNode(Node node, String xmlName) {
      if (node == null) return null;
      NodeList childNodes = node.getChildNodes();
      for (int i = 0; i < childNodes.getLength(); i++) {
         Node item = childNodes.item(i);
         String name = getName(item);
         if (name.length() > 0 && !name.equals(xmlName)) {
            return null;
         }
         if (item.getNodeName().equals(XSD_ANNOTATION)) {
            return item;
         }
      }

      for (int i = 0; i < childNodes.getLength(); i++) {
         Node annotation = getAnnotationNode(childNodes.item(i), xmlName);
         if (node != null) {
            return node;
         }
      }
      return null;
   }

   public String getName(Node node) {
      if (node != null && node.hasAttributes() && node.getAttributes().getNamedItem(NAME) != null) {
         return node.getAttributes().getNamedItem(NAME).getNodeValue();
      }
      return EMPTY;
   }

   private String getDefault(String xmlname, String xmlType, Node root, Node configurationType) {
      Node node;
      if (xmlType != null && xmlType.length() > 0) {
         node = getNode((Element) root, xmlType);
      } else {
         node = getNodeByName((Element) configurationType, xmlname);
      }
      if (node == null) return EMPTY;
      NamedNodeMap attributes = node.getAttributes();
      if (attributes != null && attributes.getNamedItem(DEFAULT) != null) {
         return attributes.getNamedItem(DEFAULT).getNodeValue();
      }
      return EMPTY;
   }

   private static String getDefault(Node node) {
      if (node == null) return EMPTY;
      NamedNodeMap attributes = node.getAttributes();
      if (attributes != null && attributes.getNamedItem(DEFAULT) != null) {
         return attributes.getNamedItem(DEFAULT).getNodeValue();
      }
      return EMPTY;
   }

   private Node getNode(Element element, String xmlName) {
      if (element == null) return null;
      NodeList childNodes = element.getChildNodes();
      for (int i = 0; i < childNodes.getLength(); i++) {
         Node item = childNodes.item(i);

         Node node;
         if (item.getNodeName().equalsIgnoreCase(XSD_SEQUENCE) || item.getNodeName().equalsIgnoreCase(XSD_CHOICE) || item.getNodeName().equalsIgnoreCase(XSD_COMPLEX_TYPE)) {
            node = getNode((Element) item, xmlName);
            if (node != null) {
               return node;
            }
         } else if (item.getNodeName().equalsIgnoreCase(XSD_ATTRIBUTE) && item.hasAttributes() && item.getAttributes().getNamedItem(NAME) != null) {
            String nodeValue = item.getAttributes().getNamedItem(NAME).getNodeValue();
            if (xmlName.equals(nodeValue)) {
               return item;
            }
         }

         NamedNodeMap attributes = item.getAttributes();
         if (attributes != null && attributes.getNamedItem(NAME) != null && attributes.getNamedItem(NAME).getNodeValue().equalsIgnoreCase(xmlName)) {
            return item;
         }

      }
      return null;
   }


   private Node getNodeByName(Element element, String xmlName) {
      if (element == null) return null;
      NodeList childNodes = element.getChildNodes();
      //search the top level elements for the node that matches name=xmlName
      for (int i = 0; i < childNodes.getLength(); i++) {
         Node item = childNodes.item(i);
         NamedNodeMap attributes = item.getAttributes();
         if (attributes != null && attributes.getNamedItem(NAME) != null && attributes.getNamedItem(NAME).getNodeValue().equalsIgnoreCase(xmlName)) {
            return item;
         }
      }
      //now try the next level down
      for (int i = 0; i < childNodes.getLength(); i++) {
         Node item = childNodes.item(i);
         if (item instanceof Element) {
            Node node = getNodeByName((Element) item, xmlName);
            if (node != null) {
               return node;
            }
         }
      }
      return null;
   }


   private Node getCoreNode(Element element) {
      NodeList childNodes = element.getChildNodes();
      for (int i = 0; i < childNodes.getLength(); i++) {
         Node item = childNodes.item(i);
         if (item.getAttributes() != null && item.getAttributes().getNamedItem(NAME) != null && item.getAttributes().getNamedItem(NAME).getNodeValue().equals(CONFIGURATION_TYPE)) {
            NodeList childNodes1 = item.getChildNodes();
            for (int j = 0; j < childNodes1.getLength(); j++) {
               Node item1 = childNodes1.item(j);
               if (item1.getNodeName().equals(XSD_ALL)) {
                  return item1;
               }
            }
         }
      }
      return null;
   }

   private String getXMLName(String name, String xmlname) {
      if (xmlname == null || xmlname.length() == 0) {
         return name.replaceAll("([A-Z])", "-$1").toLowerCase();
      }
      return xmlname;
   }


   public static void main(String[] args) throws Exception {
      ArtemisBrokerPropertiesPlugin propertiesPlugin = new ArtemisBrokerPropertiesPlugin();
      propertiesPlugin.outputFile = "configuration.md";
      propertiesPlugin.schema = "schema/artemis-configuration.xsd";
      propertiesPlugin.format = "MARKDOWN";
      propertiesPlugin.execute();

      propertiesPlugin = new ArtemisBrokerPropertiesPlugin();
      propertiesPlugin.outputFile = "configuration.adoc";
      propertiesPlugin.schema = "schema/artemis-configuration.xsd";
      propertiesPlugin.format = "LIST";
      propertiesPlugin.execute();
   }

}

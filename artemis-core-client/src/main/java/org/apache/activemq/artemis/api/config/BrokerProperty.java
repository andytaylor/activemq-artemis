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
package org.apache.activemq.artemis.api.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface BrokerProperty {

   /**
    * Used to describe a primitive property or String, this is the default
    */
   int SIMPLE = 0;

   /**
    * Used to describe a single object
    */
   int OBJECT = 1;

   /**
    * Describes a property that is an ENUM.
    */
   int ENUM = 2;

   /**
    * A Collection or Map of objects. Note each entry must have a String key (the name) or a getName/setName method.
    */
   int MAP = 3;

   /**
    * This is just for NamedPropertyConfiguration
    */
   int PROPERTIES = 6;

   /**
    * The type of property
    *
    * @return the property type
    */
   int type() default SIMPLE;

   /**
    * The property name used to set the property. Usually this is not set as the property name is extracted from the
    * setter or add method. For instance setMyProperty would translate to myProperty. For non conformant names this is useful.
    *
    * @return
    */
   String propertyName() default "";

   /**
    * This refers to the xml name of the property in the artemis-configuration.xsd. It is used to extract the default and
    * documentation of simple types. This is only neeeded if the camel case setter name doesnt match the xml element name,
    * i.e. my-property = myProperty.
    *
    * @return The xml name
    */
   String xmlName() default "";

   /**
    * This refers to the xml name of the property type in the artemis-configuration.xsd. It is used to extract the default and
    * documentation of more complex objects.
    *
    * @return the name of the xml type.
    */
   String xmlType() default "";

   /**
    * Sometime xml elements are described in more than 1 element, for example xsd:extensions. This allows a seconf objecy type to ne used
    *
    * @return
    */
   String xmlSubType() default "";

   /**
    * The name used to represent the name of an object in a collecion or map. So NAME would be used in the form myProperty.{NAME}.myNestedProperty
    *
    * @return the mapped name
    */
   String mappedName() default "";

   /**
    * Usually the 1st parameter for a setter or an add method is used to describe a property. However in some cases
    * this can change, for instance addMyProperty(String key, Object o)
    *
    * @return The parameter location
    */
   int param() default 0;

   /**
    * Sometimes it is handy to add extra docs that may be specific to the way broker properties work with a specific documentation
    *
    * @return
    */
   String extraDocs() default "";

}

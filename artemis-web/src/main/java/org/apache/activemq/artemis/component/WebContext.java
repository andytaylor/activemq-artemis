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
package org.apache.activemq.artemis.component;

import org.eclipse.jetty.server.Handler;

import java.nio.file.Path;
import java.util.List;

public abstract class WebContext {
   public static WebContext createContext(boolean jakartaRequired, String url, Path warDirectory, String warFile, Path temporaryWarDir, String virtualHost) {
      if (jakartaRequired) {
         return new JakartaWebContext(url, warDirectory, warFile, temporaryWarDir, virtualHost);
      }
      return new JavaxWebContext(url, warDirectory, warFile, temporaryWarDir, virtualHost);
   }

   public abstract void addHandler(Handler.Sequence handlers);

   public abstract void setStrict();

   public abstract void addEventListener();

   public abstract void checkConsole(String uri, List<String> consoleUrls, List<String> jolokiaUrls);
}

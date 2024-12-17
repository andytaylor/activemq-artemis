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

import javax.servlet.DispatcherType;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

import org.apache.activemq.artemis.logs.AuditLogger;
import org.eclipse.jetty.ee8.security.DefaultAuthenticatorFactory;
import org.eclipse.jetty.ee8.servlet.FilterHolder;
import org.eclipse.jetty.ee8.webapp.WebAppContext;
import org.eclipse.jetty.server.Handler;

import java.io.File;
import java.nio.file.Path;
import java.util.EnumSet;
import java.util.List;

public class JavaxWebContext extends WebContext {


   // this should match the value of <display-name> in the console war's WEB-INF/web.xml
   public static final String WEB_CONSOLE_DISPLAY_NAME = System.getProperty("org.apache.activemq.artemis.webConsoleDisplayName", "hawtio");

   private final WebAppContext webapp;

   public JavaxWebContext(String url, Path warDirectory, String warFile, Path temporaryWarDir, String virtualHost) {
      webapp = new WebAppContext();
      if (url.startsWith("/")) {
         webapp.setContextPath(url);
      } else {
         webapp.setContextPath("/" + url);
      }
      //add the filters needed for audit logging
      webapp.addFilter(new FilterHolder(JolokiaFilter.class), "/*", EnumSet.of(DispatcherType.INCLUDE, DispatcherType.REQUEST));
      webapp.addFilter(new FilterHolder(AuthenticationFilter.class), "/auth/login/*", EnumSet.of(DispatcherType.REQUEST));

      webapp.setWar(warDirectory.resolve(warFile).toString());

      String baseTempDir = temporaryWarDir.toFile().getAbsolutePath();
      webapp.setAttribute("org.eclipse.jetty.webapp.basetempdir", baseTempDir);
      webapp.setTempDirectory(new File(baseTempDir + File.separator + warFile));

      // Set the default authenticator factory to avoid NPE due to the following commit:
      // https://github.com/eclipse/jetty.project/commit/7e91d34177a880ecbe70009e8f200d02e3a0c5dd
      webapp.getSecurityHandler().setAuthenticatorFactory(new DefaultAuthenticatorFactory());

      webapp.setVirtualHosts(new String[]{virtualHost});
   }

   @Override
   public void addHandler(Handler.Sequence handlers) {
      handlers.addHandler(webapp);
   }

   @Override
   public void setStrict() {
      webapp.getSessionHandler().getSessionCookieConfig().setComment("__SAME_SITE_STRICT__");
   }

   public void addEventListener() {
      webapp.addEventListener(new ServletContextListener() {
         @Override
         public void contextInitialized(ServletContextEvent sce) {
            sce.getServletContext().addListener(new ServletRequestListener() {
               @Override
               public void requestDestroyed(ServletRequestEvent sre) {
                  ServletRequestListener.super.requestDestroyed(sre);
                  AuditLogger.currentCaller.remove();
                  AuditLogger.remoteAddress.remove();
               }
            });
         }
      });
   }

   @Override
   public void checkConsole(String uri, List<String> consoleUrls, List<String> jolokiaUrls) {
      if (WEB_CONSOLE_DISPLAY_NAME.equals(webapp.getDisplayName())) {
         consoleUrls.add(uri + webapp.getContextPath());
         jolokiaUrls.add(uri + webapp.getContextPath() + "/jolokia");
      }
   }

   //for tests
   public WebAppContext getWebContext() {
      return webapp;
   }
}

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
package org.apache.activemq.artemis.tests.client.invm;

import org.apache.activemq.artemis.test.clientendpoint.Client;
import org.apache.activemq.artemis.test.clientendpoint.TestClient;

public class InvmClient implements Client {

   Thread runningThread;

   @Override
   public String getName() {
      return "artemis.client.test.invm";
   }

   @Override
   public void run(String clientName, String host, int port, Class clientClass, String[] args) throws Exception {
      TestClient testClient = (TestClient) clientClass.newInstance();
      testClient.setHost(host);
      testClient.setPort(port);
      runningThread = new Thread(new Runnable() {
         @Override
         public void run() {
            try {
               testClient.runTest(args);
            } catch (Exception e) {
               e.printStackTrace();
            }
         }
      });
      synchronized (runningThread) {
         runningThread.start();
      }
   }

   @Override
   public void waitForComplete() {
      synchronized (runningThread) {
         try {
            if (runningThread.isAlive()) {
               runningThread.wait();
            }
         } catch (InterruptedException e) {
            e.printStackTrace();
         }
      }
   }

   @Override
   public boolean didFail() {
      return false;
   }
}

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

package org.apache.activemq.artemis.tests.e2e.examples;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.utility.DockerImageName;

import static org.junit.Assert.assertEquals;

public class SeleniumUsage {

   /*
    * Create a network to be used between the selenium and broker containers to let selenium access broker console
    */
   @ClassRule
   public static Network NETWORK = Network.newNetwork();

   @Rule
   public GenericContainer<?> artemis = new GenericContainer<>(DockerImageName.parse("artemis-centos"))
      .withExposedPorts(61616, 8161)
      .withNetwork(NETWORK)
      .withNetworkAliases("artemis");

   @Rule
   public BrowserWebDriverContainer<?> chrome = new BrowserWebDriverContainer<>()
      .withCapabilities(new ChromeOptions())
      .withNetwork(NETWORK);

   @Test
   public void test1() throws InterruptedException {

      RemoteWebDriver driver = chrome.getWebDriver();
      driver.manage().window().maximize();
      driver.get("http://artemis:8161/console");
      Thread.sleep(5000);
      WebElement username = driver.findElement(By.id("username"));
      WebElement password = driver.findElement(By.id("password"));
      username.sendKeys("artemis");
      password.sendKeys("artemis");
      WebElement login = driver.findElement(By.cssSelector("button[type=\"submit\"]"));
      login.click();
      Thread.sleep(5000);
      String actualURL = driver.getCurrentUrl();
      String expectedURL = "http://artemis:8161/console/artemis/artemisStatus?nid=root-org.apache.activemq.artemis-0.0.0.0";

      assertEquals(expectedURL, actualURL);
   }
}

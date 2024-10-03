/*-
 * #%L
 * Add-on Utils - Vaadin 23 Spring-Boot Test
 * %%
 * Copyright (C) 2024 Flowing Code
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.example.application;

import com.vaadin.flow.component.html.testbench.DivElement;
import com.vaadin.testbench.TestBenchTestCase;
import java.time.Duration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AllowedPackageChecker23IT extends TestBenchTestCase {

  @Before
  public void setup() throws Exception {
    setDriver(new ChromeDriver());
  }

  @After
  public void tearDown() throws Exception {
    getDriver().quit();
  }

  @Test
  public void testBad() {
    getDriver().get("http://127.0.0.1:8023/bad");
    WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(1));
    wait.until(ExpectedConditions.alertIsPresent());
    getDriver().switchTo().alert().dismiss();
    $(DivElement.class).first();
    LogEntries logEntries = getDriver().manage().logs().get("browser");
    for (LogEntry entry : logEntries) {
      System.out.println(entry);
    }
  }

  @Test
  public void testGood() {
    getDriver().get("http://127.0.0.1:8023/good");
    getCommandExecutor().waitForVaadin();
    $(DivElement.class).first();
  }

}


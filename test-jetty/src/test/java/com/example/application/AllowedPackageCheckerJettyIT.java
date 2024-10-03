/*-
 * #%L
 * Add-on Utils - Jetty Test
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
import com.vaadin.testbench.BrowserTest;
import com.vaadin.testbench.BrowserTestBase;

public class AllowedPackageCheckerJettyIT extends BrowserTestBase {

  @BrowserTest
  public void testGood() {
    // just check that the lack of Spring does not break it
    getDriver().get("http://127.0.0.1:8080/good");
    getCommandExecutor().waitForVaadin();
    $(DivElement.class).first();
  }

}


/*-
 * #%L
 * Add-on Utils
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
package com.flowingcode.vaadin.addons.utils.checker;

import com.vaadin.flow.component.Component;

/**
 * Utilities for checking if a component's package is
 * <a href="https://vaadin.com/docs/latest/flow/integrations/spring/configuration">allowed</a>.
 */
public class AllowedPackageChecker {

  private static AllowedPackages impl;

  private static final String SCRIPT =
      "Vaadin.Flow.fcWarnPackageNotAllowed = Vaadin.Flow.fcWarnPackageNotAllowed || function(element, package) {"
          + "var tag = element.tagName.toLowerCase();"
          + "if (!Vaadin.Flow.fcWarnPackageNotAllowed[tag]) {"
          + "  Vaadin.Flow.fcWarnPackageNotAllowed[tag]=true;"
          + "  var message = 'Please check that package '+package+' is allowed.';"
          + "  if (!element.shadowRoot) message = '<'+tag+'> is not a web component.\\n'+message;"
          + "  console.error(message+'\\nSee https://vaadin.com/docs/latest/flow/integrations/spring/configuration'); "
          + "  alert(message);"
          + "}}; Vaadin.Flow.fcWarnPackageNotAllowed(this, $0);";

  private AllowedPackageChecker() {}

  static void setImpl(AllowedPackages impl) {
    AllowedPackageChecker.impl = impl;
  }

  /**
   * Checks that the component's package is allowed and displays an error message if the package
   * name is not in the list.
   * <p>
   * The check is performed only in development mode for Spring Boot applications that have an
   * allowed packages list. No check is done in production mode.
   *
   * @param component the component instance to be checked.
   * @param type the type of the component to be checked.
   */
  public static <T extends Component> void check(T component, Class<? super T> type) {
    String componentPackageName = type.getPackage().getName();
    check(component, type, componentPackageName);
  }

  /**
   * Checks that the component's package is allowed and displays an error message if the package
   * name is not in the list.
   * <p>
   * The check is performed only in development mode for Spring Boot applications that have an
   * allowed packages list. No check is done in production mode.
   * <p>
   * The error message will reference {@code packageName}.
   *
   * @param component the component instance to be checked.
   * @param type the type of the component to be checked.
   * @param packageName the package to report in the error message.
   * @throws NullPointerException if any argument is {@code null}.
   * @throws IllegalArgumentException if the component's package does not match or is not a
   *         subpackage of {@code packageName}.
   */
  public static <T extends Component> void check(T component, Class<? super T> type, String packageName) {
    String componentPackageName = type.getPackage().getName();
    if (!componentPackageName.equals(packageName) && !componentPackageName.startsWith(packageName + ".")) {
      throw new IllegalArgumentException();
    }
    check(component, componentPackageName, packageName);
  }

  private static void check(Component component, String componentPackageName,
      String messagePackageName) {
    if (impl != null) {
      if (!impl.isPackageAllowed(componentPackageName)) {
        component.getElement().executeJs(SCRIPT, messagePackageName);
      }
    }
  }

}

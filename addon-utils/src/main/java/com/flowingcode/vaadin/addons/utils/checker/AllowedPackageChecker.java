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
   * <p>
   * This method is equivalent to {@link #check(Component, String, int) check(component,type,0)}.
   *
   * @param component the component instance to be checked.
   * @param type the type of the component to be checked.
   */
  public static <T extends Component> void check(T component, Class<? super T> type) {
    check(component, type, 0);
  }

  /**
   * Checks that the component's package is allowed and displays an error message if the package
   * name is not in the list.
   * <p>
   * The check is performed only in development mode for Spring Boot applications that have an
   * allowed packages list. No check is done in production mode.
   * <p>
   * When displaying the error message, {@code -removePackages} subpackages are removed from the
   * package of {@code type}. For instance, if {@code type} is
   * {@code com.flowingcode.vaadin.addons.utils} and {@code removePackages} is {@code -1}, the error
   * will reference {@code com.flowingcode.vaadin.addons}.
   *
   * @param component the component instance to be checked.
   * @param type the type of the component to be checked.
   * @param removePackages a negative number specifying how many subpackage levels to discard from
   *        the type package when displaying the error message.
   * @throws IllegalArgumentException if {@code removePackages} is positive, or if the package name
   *         does not contain enough subpackages to remove the specified number of levels.
   */
  public static <T extends Component> void check(T component, Class<? super T> type, int removePackages) {
    if (removePackages > 0) {
      throw new IllegalArgumentException();
    }
    String packageName = type.getPackage().getName();
    check(component, packageName, removePackages);
  }

  private static void check(Component component, String packageName, int removePackages) {
    if (impl != null) {
      // call removePackages before if in order to validate attribute
      String messagePackageName = removePackages(packageName, removePackages);
      if (!impl.isPackageAllowed(packageName)) {
        component.getElement().executeJs(SCRIPT, messagePackageName);
      }
    }
  }

  private static String removePackages(String packageName, int removePackages) {
    while (removePackages != 0) {
      int pos = packageName.lastIndexOf('.');
      if (pos < 0) {
        throw new IllegalArgumentException();
      }
      packageName = packageName.substring(0, pos);
      removePackages++;
    }
    return packageName;
  }

}

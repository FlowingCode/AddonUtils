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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.context.ApplicationContext;

class AllowedPackagesImpl implements AllowedPackages {

  private final List<String> allowedPackages;

  AllowedPackagesImpl(ApplicationContext appContext) {
    // "reuse" code from VaadinServletContextInitializer

    String onlyScanProperty = appContext.getEnvironment().getProperty("vaadin.allowed-packages");
    if (onlyScanProperty == null) {
      onlyScanProperty = appContext.getEnvironment().getProperty("vaadin.whitelisted-packages");
    }
    if (onlyScanProperty == null) {
      allowedPackages = Collections.emptyList();
    } else {
      allowedPackages = Arrays.stream(onlyScanProperty.split(","))
          .map(onlyPackage -> onlyPackage.replace('/', '.').trim()).collect(Collectors.toList());
    }
  }

  @Override
  public boolean isPackageAllowed(String packageName) {
    if (allowedPackages.isEmpty() || allowedPackages.contains(packageName)) {
      return true;
    }
    int pos = packageName.lastIndexOf('.');
    return pos > 0 && isPackageAllowed(packageName.substring(0, pos));
  }

}

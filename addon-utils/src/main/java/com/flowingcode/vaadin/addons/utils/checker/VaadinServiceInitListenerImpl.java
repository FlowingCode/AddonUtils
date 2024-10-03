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

import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.server.VaadinServletContext;
import java.lang.reflect.Constructor;

@SuppressWarnings("serial")
public class VaadinServiceInitListenerImpl implements VaadinServiceInitListener {

  @SuppressWarnings("unchecked")
  private static Constructor<? extends AllowedPackages> reflectConstructor() throws Exception {
    Class<?> applicationContextClass =
        Class.forName("org.springframework.context.ApplicationContext");
    Class<?> allowedPackagesSpringImplClass =
        Class.forName("com.flowingcode.vaadin.addons.utils.checker.AllowedPackagesImpl");
    return (Constructor<? extends AllowedPackages>) allowedPackagesSpringImplClass
        .getDeclaredConstructor(applicationContextClass);
  }

  private static AllowedPackages newInstance(Object context) throws Exception {
    Constructor<? extends AllowedPackages> constructor = reflectConstructor();
    return constructor.newInstance(context);
  }

  private static Object getServletContext(VaadinServletContext vaadinServletContext)
      throws Exception {
    return VaadinServletContext.class.getMethod("getContext").invoke(vaadinServletContext);
  }

  private static AllowedPackages newAllowedPackageChecker(VaadinService service) {
    try {
      Object applicationContext;
      Object servletContext = getServletContext((VaadinServletContext) service.getContext());
      applicationContext = getWebApplicationContext(servletContext);
      if (applicationContext != null) {
        return newInstance(applicationContext);
      }
    } catch (Exception e) {
      // do nothing
    }
    return null;
  }

  private static Object getWebApplicationContext(Object servletContext) throws Exception {
    Class<?> servletContextClass;
    try {
      servletContextClass = Class.forName("javax.servlet.ServletContext");
    } catch (ClassNotFoundException e) {
      servletContextClass = Class.forName("jakarta.servlet.ServletContext");
    }
    return Class.forName("org.springframework.web.context.support.WebApplicationContextUtils")
        .getMethod("getWebApplicationContext", servletContextClass).invoke(null, servletContext);
  }


  @Override
  public void serviceInit(ServiceInitEvent event) {
    VaadinService service = event.getSource();
    if (!service.getDeploymentConfiguration().isProductionMode()) {
      AllowedPackageChecker.setImpl(newAllowedPackageChecker(service));
    }
  }

}

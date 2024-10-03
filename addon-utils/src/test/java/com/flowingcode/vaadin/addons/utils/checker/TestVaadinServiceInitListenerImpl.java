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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.function.DeploymentConfiguration;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServletContext;
import jakarta.servlet.ServletContext;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;
import org.springframework.web.context.WebApplicationContext;

public class TestVaadinServiceInitListenerImpl {

  private static VaadinService mockVaadinService(boolean productionMode) {
    var service = Mockito.mock(VaadinService.class);
    var config = Mockito.mock(DeploymentConfiguration.class);
    Mockito.when(service.getDeploymentConfiguration()).thenReturn(config);
    Mockito.when(config.isProductionMode()).thenReturn(productionMode);

    var vctx = Mockito.mock(VaadinServletContext.class);
    var sctx = Mockito.mock(ServletContext.class);
    var actx = Mockito.mock(WebApplicationContext.class);
    Mockito.when(service.getContext()).thenReturn(vctx);
    Mockito.when(vctx.getContext()).thenReturn(sctx);
    Mockito.when(sctx.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE))
        .thenReturn(actx);

    var env = Mockito.mock(Environment.class);
    Mockito.when(actx.getEnvironment()).thenReturn(env);
    Mockito.when(env.getProperty("vaadin.allowed-packages")).thenReturn("foo");

    return service;
  }

  private static Component mockComponent() {
    var component = Mockito.mock(Component.class);
    var element = Mockito.mock(Element.class);
    Mockito.when(component.getElement()).thenReturn(element);
    return component;
  }

  @Test
  public void testProductionMode() {
    AllowedPackageChecker.setImpl(null);
    ServiceInitEvent ev = new ServiceInitEvent(mockVaadinService(true));
    new VaadinServiceInitListenerImpl().serviceInit(ev);

    // no checks are done in production mode
    var component = mockComponent();
    AllowedPackageChecker.check(component, Component.class);
    verifyNoInteractions(component.getElement());
  }

  @Test
  public void testDevelopmentMode() {
    AllowedPackageChecker.setImpl(null);
    ServiceInitEvent ev = new ServiceInitEvent(mockVaadinService(false));
    new VaadinServiceInitListenerImpl().serviceInit(ev);

    // a check is done in development mode, and it fails
    var component = mockComponent();
    AllowedPackageChecker.check(component, Component.class);
    verify(component.getElement(), times(1)).executeJs(any(), any());
  }

}

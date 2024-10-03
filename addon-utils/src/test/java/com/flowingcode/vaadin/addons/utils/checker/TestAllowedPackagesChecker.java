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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.dom.Element;
import org.junit.Test;
import org.mockito.Mockito;

public class TestAllowedPackagesChecker {

  private static Component mockComponent() {
    var component = Mockito.mock(Component.class);
    var element = Mockito.mock(Element.class);
    Mockito.when(component.getElement()).thenReturn(element);
    return component;
  }

  @Test
  public void testCheck() {
    var impl = Mockito.mock(AllowedPackages.class);
    var component = mockComponent();
    AllowedPackageChecker.setImpl(impl);

    AllowedPackageChecker.check(component, Component.class);
    verify(impl, times(1)).isPackageAllowed("com.vaadin.flow.component");
    verify(component.getElement(), times(1)).executeJs(any(),
        eq("com.vaadin.flow.component"));
  }

  @Test
  public void testCheckMinus1() {
    var impl = Mockito.mock(AllowedPackages.class);
    var component = mockComponent();
    AllowedPackageChecker.setImpl(impl);

    AllowedPackageChecker.check(component, Component.class, -1);
    verify(impl, times(1)).isPackageAllowed("com.vaadin.flow.component");
    verify(component.getElement(), times(1)).executeJs(any(),
        eq("com.vaadin.flow"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCheckMinus4() {
    var component = mockComponent();
    AllowedPackageChecker.check(component, Component.class, -4);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCheckPlus1() {
    var component = mockComponent();
    AllowedPackageChecker.check(component, Component.class, +1);
  }

}

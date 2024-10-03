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

import static org.junit.Assert.assertFalse;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

public class TestAllowedPackages {

  @Test
  public void testAllowedPackages() {
    var env = Mockito.mock(Environment.class);
    var ctx = Mockito.mock(ApplicationContext.class);
    Mockito.when(ctx.getEnvironment()).thenReturn(env);
    Mockito.when(env.getProperty("vaadin.allowed-packages")).thenReturn("foo, bar");

    var impl = new AllowedPackagesImpl(ctx);
    assertFalse("foo is not allowed", !impl.isPackageAllowed("foo"));
    assertFalse("bar is not allowed", !impl.isPackageAllowed("bar"));
    assertFalse("foo.x is not allowed", !impl.isPackageAllowed("foo.x"));
    assertFalse("baz is allowed", impl.isPackageAllowed("baz"));
  }

  @Test
  public void testWhitelistedPackages() {
    var env = Mockito.mock(Environment.class);
    var ctx = Mockito.mock(ApplicationContext.class);
    Mockito.when(ctx.getEnvironment()).thenReturn(env);
    Mockito.when(env.getProperty("vaadin.whitelisted-packages")).thenReturn("foo, bar");

    var impl = new AllowedPackagesImpl(ctx);
    assertFalse("foo is not allowed", !impl.isPackageAllowed("foo"));
    assertFalse("bar is not allowed", !impl.isPackageAllowed("bar"));
    assertFalse("foo.x is not allowed", !impl.isPackageAllowed("foo.x"));
    assertFalse("baz is allowed", impl.isPackageAllowed("baz"));
  }

  @Test
  public void testNoPackages() {
    var env = Mockito.mock(Environment.class);
    var ctx = Mockito.mock(ApplicationContext.class);
    Mockito.when(ctx.getEnvironment()).thenReturn(env);

    var impl = new AllowedPackagesImpl(ctx);
    assertFalse("foo is not allowed", !impl.isPackageAllowed("foo"));
  }

}

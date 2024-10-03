/*-
 * #%L
 * Add-on Utils - Vaadin 14 Spring-Boot Test
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

import com.flowingcode.vaadin.addons.utils.checker.AllowedPackageChecker;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.html.Div;

@SuppressWarnings("serial")
@Tag("my-component")
public class GoodComponent extends Div {

  {
    AllowedPackageChecker.check(this, GoodComponent.class);
  }

}

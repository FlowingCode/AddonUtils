[![Build Status](https://jenkins.flowingcode.com/job/utils-addon/badge/icon)](https://jenkins.flowingcode.com/job/utils-addon)
[![Maven Central](https://img.shields.io/maven-central/v/com.flowingcode.vaadin.addons/addon-utils)](https://mvnrepository.com/artifact/com.flowingcode.vaadin.addons/addon-utils)
[![Javadoc](https://img.shields.io/badge/javadoc-00b4f0)](https://javadoc.flowingcode.com/artifact/com.flowingcode.vaadin.addons/addon-utils)

# Add-on Utils

Utilities for Vaadin add-ons.

## Features

* Check that the add-on package is [allowed](https://vaadin.com/docs/latest/integrations/spring/configuration/#configure-the-scanning-of-packages). In development mode, show a browser alert if it isn't allowed.
* Compatible with Vaadin 14-24

### Maven install

Add the following dependencies in your pom.xml file:

```xml
<dependency>
   <groupId>com.flowingcode.vaadin.addons</groupId>
   <artifactId>addon-utils</artifactId>
   <version>X.Y.Z</version>
</dependency>
```
<!-- the above dependency should be updated with latest released version information -->

Release versions are available from Maven Central repository. For SNAPSHOT versions see [here](https://maven.flowingcode.com/snapshots/).

## Release notes

See [here](https://github.com/FlowingCode/AddonUtils/releases)

## Issue tracking

The issues for this library are tracked on its github.com page. All bug reports and feature requests are appreciated. 

## Contributions

Contributions are welcome, but there are no guarantees that they are accepted as such. 

As first step, please refer to our [Development Conventions](https://github.com/FlowingCode/DevelopmentConventions) page to find information about Conventional Commits & Code Style requirements.

Then, follow these steps for creating a contribution:

- Fork this project.
- Create an issue to this project about the contribution (bug or feature) if there is no such issue about it already. Try to keep the scope minimal.
- Develop and test the fix or functionality carefully. Only include minimum amount of code needed to fix the issue.
- For commit message, use [Conventional Commits](https://github.com/FlowingCode/DevelopmentConventions/blob/main/conventional-commits.md) to describe your change.
- Send a pull request for the original project.
- Comment on the original issue that you have implemented a fix for it.

## License & Author

This library is distributed under Apache License 2.0. For license terms, see LICENSE.txt.

AddonUtils is written by Flowing Code S.A.

# Developer Guide

## Getting started

Check that `com.flowingcode.vaadin.addons.example` is included in the allowed-packages list. If missing, show an alert in the browser.

```
package com.flowingcode.vaadin.addons.example;

public class MyComponent extends Div {

  public MyComponent() {
    AllowedPackageChecker.check(this, MyComponent.class);
  }

}
```

Check that `com.flowingcode.vaadin.addons.example` is included in the allowed-packages list. If missing, show an alert in the browser (the alert will reference `com.flowingcode.vaadin.addons`, i.e. the package name with one subpackage removed).
```
package com.flowingcode.vaadin.addons.example;

public class MyComponent extends Div {

  public MyComponent() {
    AllowedPackageChecker.check(this, MyComponent.class, -1);
  }

}
```

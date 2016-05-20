## Overview

This library allows reporting issues to Rollbar in anything that can use Java.

It's still under development, and many of the design decisions may still
be altered. If you have an opinion voice it in the issues!

The library is split into small modules to enable re-use as much as
possible. If you want to change a single piece of how it works it should
be relatively straightforward.

* `rollbar-utilties` contains code shared by the other modules.
* `rollbar-testing` contains shared test code.
* `rollbar-sender` implements sending occurrences to Rollbar. No external
dependencies make this lightweight, but a good candidate for an
upgrade.
* `rollbar-payload` implements a Payload object that can be serialized to
JSON. It does so with no external dependencies.
* `rollbar` brings together all the pieces from above to make it easy to
install and start recording errors.

## Installing

If you want to work on the code, you can build it yourself with `./gradlew build`.

If you want to use the code in your project, add it as a dependency. Released
artifacts are hosted on JCenter.

### Maven

Add [JCenter](https://bintray.com/bintray/jcenter) as a repository to your Maven project by following that link and clicking "Set me up" in the top right.

Add the dependency to your pom file:

```xml
<dependencies>
<dependency>
  <groupId>com.truevault.rollbar</groupId>
   <artifactId>rollbar</artifactId>
   <version>1.0.0</version>
</dependency>
</dependencies>
```

### Gradle

```groovy

repositories {
  jcenter()
}

...

dependencies {
  compile('com.truevault.rollbar:rollbar:1.0.0')
}
```

## Usage

For actual usage, the easiest way to get started is with the `rollbar`
package. See the [documentation there](https://github.com/rollbar/rollbar-java/tree/master/rollbar).

## Contributing

This library was written by someone who knows C# much better than Java. Feel free to issue stylistic PRs, or offer
suggestions on how we can improve this library.

1. [Fork it](https://github.com/rollbar/rollbar-java)
2. Create your feature branch (```git checkout -b my-new-feature```).
3. Commit your changes (```git commit -am 'Added some feature'```)
4. Push to the branch (```git push origin my-new-feature```)
5. Create new Pull Request

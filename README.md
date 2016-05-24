## Overview

This library allows reporting issues to Rollbar in anything that can use Java. This is a heavily modified fork of the original [Java library from Rollbar](https://github.com/rollbar/rollbar-java). The major changes in this fork are using non-blocking I/O for its HTTP requests so that your app doesn't have to wait, Jackson for its JSON serialization, and a tidier API (though aesthetic things like that are arguable of course).

## Setup

If you want to use the code in your project, add it as a dependency. Released
artifacts are hosted on JCenter.

### Gradle

```groovy
repositories {
  jcenter()
}

dependencies {
  compile('com.truevault.rollbar:rollbar:1.0.0')
  compile('com.truevault.rollbar:rollbar-http-ahc:1.0.0')
}
```

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
<dependency>
  <groupId>com.truevault.rollbar</groupId>
   <artifactId>rollbar-http-ahc</artifactId>
   <version>1.0.0</version>
</dependency>
</dependencies>
```

## Setup

Basic initialization will typically look like this:

```java
RollbarReporter rollbar = new DefaultRollbarReporter.Builder(new AsyncHttpItemClient(),
            "prod", "super secret access token") .build();

```

This uses the default http layer (which uses [Async Http Client](https://github.com/AsyncHttpClient/async-http-client), provided by the `rollbar-http-ahc` artifact you depended on above). If you want to use a different HTTP client, you can implement `HttpItemClient` instead and use your custom one.

`DefaultRollbarReporter.Builder` will let you customize a few other things; see the javadoc for more.

- Set an `ItemFilter` to suppress certain reports at runtime.
- Set an `ItemTransformer` to alter reports right before they're sent (say, to remove personally identifying info)
- Customize how Throwables are mapped to Rollbar `Level`s
- Customize the default data added to each new report

If you need further customization, you can implement your own `RollbarReporter` (perhaps wrapping the `DefaultRollbarReporter`).

## Usage

If you're writing a simple app that uses a single thread (like a command line tool), it may be enough to simply
set an `UncaughtExceptionHandler` for the main thread:

```java
Thread.currentThread().setUncaughtExceptionHandler((t, e) -> rollbar.log(e));
```

## Web frameworks

If you're running a Web application or using a framework that handles errors in a special way you'll need to get an
instance of a `RollbarReporter` using whatever technique is appropriate for your framework. For Spring, for instance, a properly
configured Rollbar bean along with the following class will do the trick:

```java
@ControllerAdvice
class RollbarExceptionHandler {
    private final RollbarReporter rollbar;

    public RollbarExceptionHandler(RollbarReporter rollbar) {
        this.rollbar = rollbar;
    }

    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null)
            throw e;

        rollbar.log(e);

        throw e;
    }
}
```

You might configure your bean with something like this:

```java
package com.yourcompany.product;

import org.springframework.beans.factory.FactoryBean;
import com.truevault.rollbar.RollbarReporter;

public class RollbarBean implements FactoryBean<RollbarReporter> {
	private RollbarReporter rollbar = new DefaultRollbarReporter.Builder(new AsyncHttpItemClient(),
                                                    "prod", "super secret access token")
                                              .build();

	@Override
	public Rollbar getObject() throws Exception {
		return rollbar;
	}

	@Override
	public Class<? extends RollbarReporter> getObjectType() {
		return RollbarReporter.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
```

You can, of course, always choose to configure this with XML.

## Custom usage

Rollbar also provides a set of classes for building and sending error reports to Rollbar.

The simplest way to use this is with a `try/catch` like so:

```java
public class MyClass {
    private final RollbarReporter rollbar = ...;

    /*...*/
    public void doSomething() {
        try {
            this.monitoredMethod();
        } catch (Throwable t) {
            // this returns a CompletableFuture, so add .get() if you want to wait until the request either succeeds
            // or fails, or whenComplete() or equivalent to log if something goes wrong with the request.
            rollbar.log(t);

            // You can obviously choose to do something *other* than re-throw the exception
            throw t;
        }
    }
}
```

Uncaught errors look different depending on the framework you're using. Play, for instance, uses an `HttpErrorHandler`.
For general use you'll want to do something like this:

```java
public class Program {
    /*...*/
    public void main(String[] argv) {
        RollbarReporter rollbar = ...;
        Thread.currentThread().setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                rollbar.log(t);
                throw t;
            }
        });
    }
}
```

Note that the above snippet monitors a **single** thread. If your framework starts a thread per request, or uses
different threads for different parts of your UI you'll need to take that into consideration.

## Tips for Optimal Usage

 * The `Extensible` class represents the various portions of the payload that can have arbitrary additional data sent.

   This class contains two additional methods: `get(String key)` and `put(String key, Object value)`. `get` simply
   retrieves the value at that key. It can be used for built-in and custom keys. `put` checks the key against the
   built-in properties and will throw an `IllegalArgumentException` if it is one of the known values. This allows the
   built-in property setters to validate the properties when they are set.


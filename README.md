junit-js
========

JUnit-JS is a JUnit test runner for Javascript tests. It uses Java Nashorn to run javascript in the JVM so you can use it to test javascript functions and access Java methods at the same time. So you can use all of the normal JUnit test methods.

The following sections show an example of how to use.

#Example

The following is the content of a Javascript file. Each file is equivalent to a JUnit test class. Each function whose name starts with test (eg testFred, testMe) will be called as a test case.

```javascript

function testDummy01() {
	print("testDummy01");
	var x = "5";
	assertEquals("x should be 5", 5, x);
}

function testDummy02() {
	print("testDummy02");
	fail("not implemented");
}
```

Note that the normal JUnit assertEquals is called from testDummy01. Be careful with this, because JavaScript coerces types it can be a tricky to get it to call the intended assertEquals method. Nashorn allows specific method to be called. Check out the TestUtils.js file for examples.

While tests can be defined in JavaScript, a JUnit test suite needs to be defined in Java. The following shows how to include various javascript tests in a test suite. The test files are loaded from the same class loader as the test suite class. This means that as with normal java resources, the javascript files are loaded relative to the class or absolutely if their path starts with '/'.

```java
import org.junit.runner.RunWith;
import uk.co.benjiweber.junitjs.JSRunner;
import uk.co.benjiweber.junitjs.Tests;

@RunWith(JSRunner.class)
@Tests({
	"/uk/co/benjiweber/junitjs/examples/DummyJSTest.js",
	"SimpleJSTest.js",
	"/uk/co/benjiweber/junitjs/other/FredsJSTest.js",
	"../other/FredsJSTest2.js",
	"sub/SimpleJSTest.js"
})
public class ExampleTestSuite	 {}
```

# Usage

## Maven

To use with maven use the following dependencies (note that you also need to include JUnit).

```xml
  	<dependency>
  		<groupId>com.belteshazzar</groupId>
  		<artifactId>junit-js</artifactId>
  		<version>1.0.0</version>
  	</dependency>
  	<dependency>
  		<groupId>junit</groupId>
  		<artifactId>junit</artifactId>
  		<version>4.11</version>
  	</dependency>
```

## Non-Maven Download

For non-Maven use cases, you download jars from [Central Maven repository](http://repo1.maven.org/maven2/com/belteshazzar/junit-js/1.0.0/junit-js-1.0.0.jar).

# Eclipse Plugin

If you run JUnit tests from eclipse you'll notice that it expects all test classes to be java classes. It was probably a fair assumption but when using JUnit-JS we break this assumption. To make life easier when using eclipse we've created a feature patch for the JUnit pluggin. This plugin updates the JUnit view so that double clicking on test classes opens the appropriate javascript file and double clicking on a test case opens the appropriate javascript file and selects the function called. 

```
http://junit-js.belteshazzar.com/site.xml
```

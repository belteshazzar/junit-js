junit-js
========

JUnit Runner for Javascript Tests.

#Example

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


```
http://junit-js.belteshazzar.com/
```

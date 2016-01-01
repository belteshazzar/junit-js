package uk.co.benjiweber.junitjs.examples;

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
public class ExampleTestSuite	 {

}

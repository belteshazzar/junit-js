package uk.co.benjiweber.junitjs.examples;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class ExampleJavaTest {

	@Test
	public void testReturnFred() {
		assertEquals("umm",3,5);
	}

	@Test
	public void testReturnSteve() {
		fail("Not yet implemented");
	}

}

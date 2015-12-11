package uk.co.benjiweber.junitjs;

import org.junit.runner.Description;

public class TestCase {

	public final TestClass testClass;
	public final String functionName;
	private final int lineNumber;
	public final Runnable testCase;

	public TestCase(TestClass testClass, String functionName, int lineNumber, Runnable testCase) {
		this.testClass = testClass;
		this.functionName = functionName;
		this.lineNumber = lineNumber;
		this.testCase = testCase;
	}

	public Description createTestDescription() {
		return Description.createTestDescription(testClass.resolvedName + ":" + lineNumber, functionName);
	}
}


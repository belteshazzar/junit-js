package uk.co.benjiweber.junitjs;

import static java.util.Arrays.asList;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.Filterable;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.manipulation.Sortable;
import org.junit.runner.manipulation.Sorter;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

public class JSRunner extends Runner implements Filterable, Sortable  {
	
	private List<TestClass> tests;
	private final Class<?> cls;

	public JSRunner(Class<?> cls) throws URISyntaxException {
		this.cls = cls;
		List<String> testNames = asList(cls.getAnnotation(Tests.class).value());
		this.tests = findJSTests(cls,testNames);
	}
	
	private List<String> discoverValidClassPaths() throws URISyntaxException {
		ClassLoader cl = ClassLoader.getSystemClassLoader();
		URL[] urls = ((URLClassLoader)cl).getURLs();
		List<String> paths = new ArrayList<String>();
		for (URL url : urls) {
			if (new File(url.toURI().getPath()).isDirectory()) {
				paths.add(url.toString());
			}
		}
		return paths;
	}
	
	@Override
	public Description getDescription() {
		Description suite = Description.createSuiteDescription(cls);
		for (TestClass testClass : tests) {
			suite.addChild(testClass.createTestDescription());
		}
		return suite;
	}

	@Override
	public void run(RunNotifier notifier) {
		for (TestClass testClass : tests) {
			List<TestCase> tests = testClass.testCases;
			for (TestCase test : tests) {
				
				Description desc = test.createTestDescription();
				notifier.fireTestStarted(desc);
				try {
					test.testCase.run();
					notifier.fireTestFinished(desc);
				} catch (Exception | Error e) {
					notifier.fireTestFailure(new Failure(desc, bestException(e)));
				}
			}
		}
	}
	
	private List<TestClass> findJSTests(Class<?> cls, List<String> testNames) {
		
		try {
			List<String> validClassPaths = discoverValidClassPaths();

			List<TestClass> testClasses = new ArrayList<TestClass>();
			for (String name : testNames) {
				testClasses.add(new TestClass(cls,name,validClassPaths));
			}
			return testClasses;
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void sort(Sorter sorter) {
		//
	}

	public void filter(Filter filter) throws NoTestsRemainException {
		//
	}

	private Throwable bestException(Throwable e) {
			return e.getCause() != null ? e.getCause() : e;
	}

}


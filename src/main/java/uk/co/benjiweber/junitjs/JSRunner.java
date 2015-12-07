package uk.co.benjiweber.junitjs;

import static java.util.Arrays.asList;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.io.IOUtils;
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

	public JSRunner(Class<?> cls) {
		this.cls = cls;
		
		
		List<String> testNames = asList(cls.getAnnotation(Tests.class).value());
		this.tests = findJSTests(cls,testNames);
	}
	
	@Override
	public Description getDescription() {
		Description suite = Description.createSuiteDescription(cls);
		for (TestClass testClass : tests) {
			List<TestCase> tests = testClass.testCases;
			Description desc = Description.createTestDescription(testClass.junitName(), testClass.junitName());
			suite.addChild(desc);
			for (TestCase test : tests) {
				Description methodDesc = Description.createTestDescription(testClass.junitName(), test.name);
				desc.addChild(methodDesc);
			}
		}
		return suite;
	}

	@Override
	public void run(RunNotifier notifier) {
		for (TestClass testClass : tests) {
			List<TestCase> tests = testClass.testCases;
			for (TestCase test : tests) {
				Description desc = Description.createTestDescription(testClass.junitName(), test.name);
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
			ScriptEngineManager factory = new ScriptEngineManager();
			ScriptEngine engine = factory.getEngineByName("nashorn");
			loadTestUtilities(engine);
			engine.put("loadResource",new ClassPathLoader(engine));
			List<TestClass> testClasses = new ArrayList<TestClass>();
			for (String name : testNames) {
				testClasses.add(new TestClass(name, load(cls, engine, name)));
			}
			return testClasses;
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void loadTestUtilities(ScriptEngine engine) throws ScriptException,IOException {
		engine.eval(IOUtils.toString(JSRunner.class.getResource("TestUtils.js")));
	}

	@FunctionalInterface
	public interface Loader {
				
		public void load(String filename) throws ScriptException;
	}
	
	public class ClassPathLoader implements Loader {
		private final ScriptEngine engine;
		public ClassPathLoader(ScriptEngine engine) {
			this.engine = engine;
		}
		@Override
		public void load(String filename) throws ScriptException {
			System.err.println("loading: " + filename);
			System.err.println("using: " + cls);
			URL r = cls.getResource(filename);
			System.err.println("url: " + r);
			String file = r.getFile();
			System.err.println(file);
			engine.eval("load(\"" + file.toString() + "\");");
		}
		
	}
	
	@SuppressWarnings("unchecked")
	private List<TestCase> load(Class<?> cls, ScriptEngine engine, String name) throws ScriptException, IOException{
		System.err.println("load: " + name);
		InputStream s = cls.getResourceAsStream(name);
		System.err.println("input stream: " + s);
		String src = IOUtils.toString(s);
		System.err.println("src: " + src);
		return (List<TestCase>) engine.eval(src);
        }

	public void sort(Sorter sorter) {
		//
	}

	public void filter(Filter filter) throws NoTestsRemainException {
		//
	}

	private Throwable bestException(Throwable e) {
		if (nashornException(e))
			return e.getCause() != null ? e.getCause() : e;
		if (rhinoException(e)) {
			return extractActualExceptionFromRhino(e);
		}
		return e;
	}

	private boolean rhinoException(Throwable e) {
		return e.getClass().getSimpleName().contains("JavaScript");
	}

	private boolean nashornException(Throwable e) {
		return e.getClass().getSimpleName().contains("ECMA");
	}

	private Throwable extractActualExceptionFromRhino(Throwable e) {
		try {
			Field f = e.getClass().getDeclaredField("value");
			f.setAccessible(true);
			Object javascriptWrapper = f.get(e);
			Field javaThrowable = javascriptWrapper.getClass().getDeclaredField("javaObject");
			javaThrowable.setAccessible(true);
			Throwable t = (Throwable) javaThrowable.get(javascriptWrapper);
			return t;
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e1) {
			throw new RuntimeException(e);
		}
	}
	
}

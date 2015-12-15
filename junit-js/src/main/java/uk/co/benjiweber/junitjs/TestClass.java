package uk.co.benjiweber.junitjs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.io.IOUtils;
import org.junit.runner.Description;

public class TestClass {
	private Class<?> suiteClass;
	List<TestCase> testCases;
	private String name;
	private String url;
	private String filename;
	String resolvedName;

    public TestClass(Class<?> suiteClass, String name, List<String> validClassPaths) throws NoSuchMethodException, ScriptException, IOException, URISyntaxException {
    	this.suiteClass = suiteClass;
		this.name = name;
		this.testCases = load(suiteClass);
		this.filename = url.substring(url.lastIndexOf('/')+1);
		for (String path : validClassPaths) {
			if (url.length()>path.length()) {
				String urlStart = url.substring(0, path.length());
				if (urlStart.equals(path)) {
					resolvedName = url.substring(urlStart.length()-1);
					break;
				}
			}
		}
		if (resolvedName==null) {
			System.err.println("unable to find test file in classpath: " + name);
		}
	}
      
    private int findLineNumber(String[] lines, String functionName) {
    	// attempt to find "function functionName" or "functionName = function"
    	Pattern pattern = Pattern.compile(".*((function\\s+" + functionName + ")|(" + functionName + "\\s*=\\s*function)).*",Pattern.DOTALL);
    	for (int lineNumber = 0 ; lineNumber<lines.length ; lineNumber++) {
    		if (pattern.matcher(lines[lineNumber]).matches()) {
    			return lineNumber+1;
    		}
    	}
    	// can't find
    	return 0;
    }

	private List<TestCase> load(Class<?> suiteClass) throws ScriptException, IOException, NoSuchMethodException, URISyntaxException{
		ScriptEngineManager factory = new ScriptEngineManager();
		ScriptEngine engine = factory.getEngineByName("nashorn");
		engine.put("loadResource",new ClassPathLoader(engine));
		loadTestUtilities(engine);
		URL resourceUrl = suiteClass.getResource(name);
		if (resourceUrl==null) throw new FileNotFoundException(name);
		this.url = resourceUrl.toString();
		InputStream s = suiteClass.getResourceAsStream(name);
		if (s==null) throw new FileNotFoundException(name);
		String src = IOUtils.toString(s);
		String[] lines = src.split("\n");
		engine.eval(src);
		List<TestCase> testCases = new ArrayList<TestCase>();
		final Invocable invocable = (Invocable)engine;
		Bindings bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
		for (final String key : bindings.keySet()) {
			if (key.matches("^test.*")) {
				testCases.add(new TestCase(TestClass.this,key,findLineNumber(lines,key),new Runnable() {
					@Override
					public void run() {
						try {
							invocable.invokeFunction(key);
						} catch (NoSuchMethodException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ScriptException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}));
			}
		}
		return testCases;
    }

	public Description createTestDescription() {
		Description desc = Description.createTestDescription(filename,resolvedName);
		for (TestCase test : testCases) {
			desc.addChild(test.createTestDescription());
		}
		return desc;
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
			URL r = suiteClass.getResource(filename);
			String file = r.getFile();
			engine.eval("load(\"" + file.toString() + "\");");
		}
		
	}

}


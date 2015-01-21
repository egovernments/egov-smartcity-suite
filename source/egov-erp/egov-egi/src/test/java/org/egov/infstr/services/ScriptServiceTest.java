/**
 * 
 */
package org.egov.infstr.services;

import org.egov.commons.EgiObjectFactory;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.ValidationException;
import org.egov.infstr.models.Script;
import org.egov.models.AbstractPersistenceServiceTest;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test cases for ScriptService
 */
@Ignore
public class ScriptServiceTest extends
		AbstractPersistenceServiceTest<Script, Long> {
	private ScriptService scriptService;
	private EgiObjectFactory factory;
	private static final String TEST_SCRIPT_SIMPLE_JRUBY = "test.scriptservice.simple.jruby";
	private static final String TEST_SCRIPT_SIMPLE_JRUBY_SYNTAXERROR = "test.scriptservice.simple.jruby.syntaxerror";
	private static final String TEST_SCRIPT_SIMPLE_JRUBY_NULLCONTEXT = "test.scriptservice.simple.jruby.nullcontext";
	private static final String TEST_SCRIPT_SIMPLE_JRUBY_VALIDATIONERROR = "test.scriptservice.simple.jruby.validationerror";
	private static final String TEST_SCRIPT_SIMPLE_JRUBY_VALIDATIONERROR_MESSAGE = "test.scriptservice.simple.jruby.validationerror.message";
	private static final String TEST_SCRIPT_SIMPLE_JRUBY_VALIDATIONERROR_MAP = "test.scriptservice.simple.jruby.validationerror.map";
	private static final String TEST_SCRIPT_SIMPLE_JYTHON = "test.scriptservice.simple.jython";
	private static final String TEST_SCRIPT_SIMPLE_JYTHON_SYNTAXERROR = "test.scriptservice.simple.jython.syntaxerror";
	private static final String TEST_SCRIPT_FUNCLIB = "test.scriptservice.funclib";
	private static final String TEST_SCRIPT_CALLFUNC_VALID = "test.scriptservice.callfunc.valid";
	private static final String TEST_SCRIPT_CALLFUNC_INVALID = "test.scriptservice.callfunc.invalid";
	private static final String TEST_SCRIPT_CALLFUNC_EXCEPTION = "test.scriptservice.callfunc.exception";
	private static final String TEST_SCRIPT_INVALID_ENGINE = "test.scriptservice.invalid.engine";

	@Before
	public void setUp() {
		this.scriptService = new ScriptService(1, 2, 3, 5);
		this.scriptService.setSessionFactory(sessionFactory);
		this.factory = new EgiObjectFactory(this.session);
	}

	@Test
	public void testExecuteScriptJRuby() {
		this.factory.createScript(TEST_SCRIPT_SIMPLE_JRUBY, "jruby", "$label");

		final Object result = this.scriptService.executeScript(
				TEST_SCRIPT_SIMPLE_JRUBY,
				ScriptService.createContext("label", "this is the label"));
		assertEquals("this is the label", result);
	}

	@Test
	public void testExecuteScriptJython() {
		this.factory.createScript(TEST_SCRIPT_SIMPLE_JYTHON, "jython",
				"result = \"Hello World\"");

		Object result = this.scriptService.executeScript(
				TEST_SCRIPT_SIMPLE_JYTHON, ScriptService.createContext());
		assertEquals("Hello World", result);

		// Second execution covers cached script execution
		result = this.scriptService.executeScript(TEST_SCRIPT_SIMPLE_JYTHON,
				ScriptService.createContext());
		assertEquals("Hello World", result);

		// Clear script cache
		this.scriptService.clearScriptCache();
		assertTrue(true);
	}

	@Test(expected = EGOVRuntimeException.class)
	public void testExecuteScriptCompilationError() {
		this.factory.createScript(TEST_SCRIPT_SIMPLE_JYTHON_SYNTAXERROR,
				"jython", "result = \"Hello World");

		this.scriptService.executeScript(TEST_SCRIPT_SIMPLE_JYTHON_SYNTAXERROR,
				ScriptService.createContext());
		fail();
	}

	@Test
	public void testExecuteScriptFunction() {
		this.factory.createScript(TEST_SCRIPT_FUNCLIB, "jython",
				"def getMessage(prefix):\n\treturn(prefix + \"Governation\")");
		this.factory
				.createScript(
						TEST_SCRIPT_CALLFUNC_VALID,
						"jython",
						"pfx = \"Hello \"\nscriptService.loadFunctionsFromScript(\""
								+ TEST_SCRIPT_FUNCLIB
								+ "\")\nresult = scriptService.executeFunction(scriptEngine, \"getMessage\", [pfx])");

		final Object result = this.scriptService.executeScript(
				TEST_SCRIPT_CALLFUNC_VALID, ScriptService.createContext());
		assertEquals("Hello Governation", result);
	}

	@Test(expected = EGOVRuntimeException.class)
	public void testExecuteScriptFunctionInvalid() {
		this.factory.createScript(TEST_SCRIPT_FUNCLIB, "jython",
				"def getMessage(prefix):\n\treturn(prefix + \"Governation\")");
		this.factory
				.createScript(
						TEST_SCRIPT_CALLFUNC_INVALID,
						"jython",
						"pfx = \"Hello \"\nscriptService.loadFunctionsFromScript(\""
								+ TEST_SCRIPT_FUNCLIB
								+ "\")\nresult = scriptService.executeFunction(scriptEngine, \"getMessage1\", [pfx])");

		this.scriptService.executeScript(TEST_SCRIPT_CALLFUNC_INVALID,
				ScriptService.createContext());
		fail();
	}

	@Test(expected = EGOVRuntimeException.class)
	public void testExecuteScriptFunctionException() {
		this.factory.createScript(TEST_SCRIPT_SIMPLE_JRUBY, "jruby", "$label");
		this.factory
				.createScript(
						TEST_SCRIPT_CALLFUNC_EXCEPTION,
						"jython",
						"pfx = \"Hello \"\nscriptService.loadFunctionsFromScript(\""
								+ TEST_SCRIPT_SIMPLE_JRUBY
								+ "\")\nresult = scriptService.executeFunction(scriptEngine, \"getMessage1\", [pfx])");

		this.scriptService.executeScript(TEST_SCRIPT_CALLFUNC_EXCEPTION,
				ScriptService.createContext());
		fail();
	}

	@Test(expected = EGOVRuntimeException.class)
	public void testExecuteNullScript() {
		this.scriptService.executeScript("test.abc.xyz",
				ScriptService.createContext());
		fail();
	}

	@Test(expected = EGOVRuntimeException.class)
	public void testExecuteScriptInvalidEngine() {
		this.factory.createScript(TEST_SCRIPT_INVALID_ENGINE, "invalid_engine",
				"result = \"Hello World\"");

		this.scriptService.executeScript(TEST_SCRIPT_INVALID_ENGINE,
				ScriptService.createContext());
		fail();
	}

	@Test(expected = EGOVRuntimeException.class)
	public void testExecuteScriptNullContext() {
		this.factory.createScript(TEST_SCRIPT_SIMPLE_JRUBY_NULLCONTEXT,
				"jython", "result = \"Hello World\"");

		this.scriptService.executeScript(TEST_SCRIPT_SIMPLE_JRUBY_NULLCONTEXT,
				null);
		fail();
	}

	@Test(expected = EGOVRuntimeException.class)
	public void testInvalidContext() {
		ScriptService.createContext("label", "this is the label", "keyonly");
		fail();
	}

	@Test(expected = ValidationException.class)
	public void testValidationErrors() {
		this.factory
				.createScript(
						TEST_SCRIPT_SIMPLE_JRUBY_VALIDATIONERROR,
						"jruby",
						"require 'java'\n import org.egov.infstr.ValidationError\n$validationErrors=[ValidationError.new('a','b')]");
		this.scriptService.executeScript(
				TEST_SCRIPT_SIMPLE_JRUBY_VALIDATIONERROR,
				ScriptService.createContext());
		fail();
	}

	@Test
	public void testValidationErrorsWithErrorMessages() {
		this.factory.createScript(
				TEST_SCRIPT_SIMPLE_JRUBY_VALIDATIONERROR_MESSAGE, "jruby",
				"$validationErrors='invalid.value'");
		try {
			this.scriptService.executeScript(
					TEST_SCRIPT_SIMPLE_JRUBY_VALIDATIONERROR_MESSAGE,
					ScriptService.createContext());
			fail("Script executed succesfully where we expected a ValidationException!");
		} catch (final ValidationException e) {
			assertEquals("invalid.value", e.getErrors().get(0).getKey());
			assertEquals("invalid.value", e.getErrors().get(0).getMessage());
		}
	}

	@Test
	public void testValidationErrorsWithErrorMaps() {
		this.factory.createScript(TEST_SCRIPT_SIMPLE_JRUBY_VALIDATIONERROR_MAP,
				"jruby", "$validationErrors={:invalid_value=>'zzz'}");
		try {
			this.scriptService.executeScript(
					TEST_SCRIPT_SIMPLE_JRUBY_VALIDATIONERROR_MAP,
					ScriptService.createContext());
			fail();
		} catch (final ValidationException e) {
			assertEquals("invalid_value", e.getErrors().get(0).getKey());
			assertEquals("zzz", e.getErrors().get(0).getMessage());
		}
	}

	@Test(expected = EGOVRuntimeException.class)
	public void testInvalidJRubyScript() {
		this.factory.createScript(TEST_SCRIPT_SIMPLE_JRUBY_SYNTAXERROR,
				"jruby", "incorrect");
		this.scriptService.executeScript(TEST_SCRIPT_SIMPLE_JRUBY_SYNTAXERROR,
				ScriptService.createContext());
		fail();
	}
}

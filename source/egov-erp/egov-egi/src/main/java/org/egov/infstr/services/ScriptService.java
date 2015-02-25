/*
 * @(#)ScriptService.java 3.0, 17 Jun, 2013 3:08:06 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.services;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.cache.LRUCache;
import org.egov.infstr.models.Script;
import org.egov.infstr.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service for executing scripts. Caches the script engine and frequently used
 * scripts to improve performance. Also provides methods that can be used from
 * within scripts to invoke other scripts or functions from already loaded
 * scripts. <br>
 * <br>
 * Code snippet: <br>
 * 
 * <pre>
 * <code>scriptService.executeScript(&quot;script name&quot;, ScriptService.createContext(&quot;var1&quot;,
 * 		value1, &quot;var2&quot;, value2));
 * </code>
 * </pre>
 */
public class ScriptService extends PersistenceService<Script, Long> {
	private static LRUCache<String, ScriptEngine> scriptEngineCache;
	private static LRUCache<String, Script> scriptCache;
	private static final Logger logger = LoggerFactory.getLogger(ScriptService.class);

	/**
	 * Creates instance of script service with given sizes of engine cache and script cache
	 * @param engineCacheMinSize Minimum (initial) size of script engine cache
	 * @param engineCacheMaxSize Maximum size of script engine cache
	 * @param scriptCacheMinSize Minimum (initial) Size of script cache
	 * @param scriptCacheMaxSize Maximum size of script cache
	 */
	public ScriptService(final int engineCacheMinSize, final int engineCacheMaxSize, final int scriptCacheMinSize, final int scriptCacheMaxSize) {
		if (scriptEngineCache == null) {
			scriptEngineCache = new LRUCache<String, ScriptEngine>(engineCacheMinSize, engineCacheMaxSize);
		}

		if (scriptCache == null) {
			scriptCache = new LRUCache<String, Script>(scriptCacheMinSize, scriptCacheMaxSize);
		}
	}

	/**
	 * Takes an even number of arguments, and creates a <code>ScriptContext</code> object, with ith argument as the key and (i+1)th argument as the value. This is why this method expects an even number of arguments.
	 * @param args Arguments from which the context will be created
	 * @return The script context in which a script can be executed. This can be passed as second argument to the method {@link ScriptService#executeScript(String, ScriptContext)}
	 */
	public static ScriptContext createContext(final Object... args) {
		final SimpleScriptContext context = new SimpleScriptContext();
		if (args.length % 2 != 0) {
			throw new EGOVRuntimeException("Number of arguments must be even");
		}
		for (int i = 0; i < args.length; i += 2) {
			context.setAttribute((String) args[i], args[i + 1], ScriptContext.ENGINE_SCOPE);
		}
		return context;
	}

	/**
	 * @param scriptType Script type for which the script engine is to be fetched
	 * @return Script engine for given script type
	 */
	private ScriptEngine getScriptEngine(final String scriptType) {
		// Check if engine for given script type is already cached
		ScriptEngine engine = scriptEngineCache.get(scriptType);
		if (engine == null) {
			// Engine not cached. Create and cache it.
			final ScriptEngineManager m = new ScriptEngineManager();
			engine = m.getEngineByName(scriptType);
			if (engine == null) {
				// Engine could not be created. throw exception
				throw new EGOVRuntimeException("Could not get script engine for [" + scriptType + "]");
			}
			scriptEngineCache.put(scriptType, engine);
		}

		return engine;
	}

	/**
	 * Loads all functions from given script.
	 * @param scriptName Script which is to be loaded
	 */
	public void loadFunctionsFromScript(final String scriptName) {
		final Script script = getScript(scriptName, DateUtils.today());
		final ScriptEngine engine = getScriptEngine(script.getType());
		executeScript(script, engine, engine.getContext());
	}

	/**
	 * Sets standard context attributes that can be used inside the script. These are: <br>
	 * <code>scriptService</code> - Instance of script service - can be used to invoke another script, load functions from a script, or execute an already loaded function.<br>
	 * <code>scriptEngine</code> - The script engine - to be passed as first argument to the {@link ScriptService#executeFunction(ScriptEngine, String, Object...)}<br>
	 * <code>scriptContext</code> - The script context - to be passed as second argument to the {@link ScriptService#executeScript(String, ScriptContext)} API<br>
	 * @param engine The script engine
	 * @param context The script context
	 */
	private void setupContextAttributes(final ScriptEngine engine, final ScriptContext context) {
		context.setAttribute("scriptService", this, ScriptContext.ENGINE_SCOPE);
		context.setAttribute("scriptEngine", engine, ScriptContext.ENGINE_SCOPE);
		context.setAttribute("scriptContext", context, ScriptContext.ENGINE_SCOPE);
	}

	/**
	 * Executes the given script using given script engine and context
	 * @param script Script to be executed
	 * @param engine Engine to be used for executing the script
	 * @param context Context in which the script is to be executed
	 * @return The result from the script, or the value of the script variable "result"
	 * @throws ValidationException if the script returns a list of ValidationErrors
	 * @throws EGOVRuntimException if script execution throws an exception
	 */
	private Object executeScript(final Script script, final ScriptEngine engine, final ScriptContext context) {
		if (context == null) {
			// Context must be passed. We don't want to use the default engine
			// context as it will keep on growing over a period of time
			final String errMsg = "ScriptContext not passed to executeScript method!";
			logger.error(errMsg);
			throw new EGOVRuntimeException(errMsg);
		}

		Object evalResult = null;

		// Set up standard context attributes
		setupContextAttributes(engine, context);

		try {
			final CompiledScript compiledScript = script.getCompiledScript();
			if (engine instanceof Compilable && compiledScript != null) {
				// Script engine supports compiled scripts.
				// Execute the compiled script using given context.
				evalResult = compiledScript.eval(context);
			} else {
				// Script engine doesn't support compiled scripts.
				// Set the context on engine and execute the script.
				evalResult = engine.eval(script.getScript(), context);
			}

			handleErrorsIfAny(context.getAttribute("validationErrors"));

			// No errors. Get and return the result.
			final Object result = context.getAttribute("result");
			return evalResult == null ? result : evalResult;
		} catch (final ScriptException e) {
			logger.error("script error for " + script.getType() + ":" + script.getName() + ":" + script.getScript(), e);
			throw new EGOVRuntimeException("script.error", e);
		}
	}

	/**
	 * Executes given function using given script engine
	 * @param engine Script engine to be used for executing the function
	 * @param functionName Name of function to be executed
	 * @return Return value from the function
	 */
	public Object executeFunctionNoArgs(final ScriptEngine engine, final String functionName) {
		return executeFunction(engine, functionName);
	}

	/**
	 * Executes given function using given script engine
	 * @param engine Script engine to be used for executing the function
	 * @param functionName Name of function to be executed
	 * @param args Arguments to be passed to the function
	 * @return Return value from the function
	 */
	public Object executeFunction(final ScriptEngine engine, final String functionName, final Object... args) {
		Object evalResult = null;
		if (engine instanceof Invocable) {
			try {
				evalResult = ((Invocable) engine).invokeFunction(functionName, args);
				if (evalResult == null) {
					evalResult = engine.get("result");
				}
			} catch (final Exception e) {
				final String errMsg = "Exception while invoking function [" + functionName + "]";
				logger.error(errMsg, e);
				throw new EGOVRuntimeException(errMsg, e);
			}
		} else {
			final String errMsg = "Script engine [" + engine + "] does not support method execution!";
			logger.error(errMsg);
			throw new EGOVRuntimeException(errMsg);
		}
		return evalResult;
	}

	/**
	 * Compiles given script if it is not already compiled, and if the corresponding script engine supports compilation
	 * @param script Script to be compiled
	 * @return The compiled script object. Returns null if the script engine doesn't support compilation.
	 */
	private CompiledScript compileScriptIfRequired(final Script script) {
		CompiledScript compiledScript = script.getCompiledScript();

		if (compiledScript != null) {
			return compiledScript;
		}

		final ScriptEngine engine = getScriptEngine(script.getType());
		if (engine instanceof Compilable) {
			try {
				// Script engine supports compilation
				compiledScript = ((Compilable) engine).compile(script.getScript());
				script.setCompiledScript(compiledScript);

				// TODO: Add compiledScript column to SCRIPT table and persist
				// the modified object. Problem: JythonCompiledScript is not
				// serializable!
				// scriptService.persist(script);
			} catch (final ScriptException e) {
				final String errMsg = "Could not compile script " + script.getType() + ":" + script.getName() + ":" + script.getScript();
				logger.error(errMsg, e);
				throw new EGOVRuntimeException(errMsg, e);
			}
		}

		return compiledScript;
	}

	/**
	 * @param scriptName Script name
	 * @param asOnDate The date against which validity of the script is to be checked. If null, validity as of current date will be checked.
	 * @return Script object for given name
	 * @throws EGOVRuntimeException if the script is not configured in the system
	 */
	private Script getScript(final String scriptName, Date asOnDate) {
		if (asOnDate == null) {
			asOnDate = DateUtils.today();
		}

		Script script = scriptCache.get(scriptName);
		if (script != null && script.getPeriod().getEndDate().before(asOnDate) == false) {
			// Script found in cache and is still valid
			return script;
		}

		// Script not available in cache. Try to fetch from database.
		script = findByNamedQuery(Script.QRY_SCRIPT_BY_NAME_DATE, scriptName, asOnDate);
		if (script == null) {
			throw new EGOVRuntimeException("Script [" + scriptName + "] not found!");
		}

		// Compile the script if required and possible
		compileScriptIfRequired(script);

		// Add the script to cache
		scriptCache.put(scriptName, script);

		return script;
	}

	/**
	 * Executes the given script with context as the ScriptContext. The values in the context are available as variables in the script. The names of these variables will be the attributes in the context.
	 * @param script Script to be executed
	 * @param context The script context - attributes in this context can be used as variables inside the script
	 * @return The result from the script, or the value of the script variable "result"
	 * @throws ValidationException if the script returns a list of ValidationErrors
	 */
	public Object executeScript(final Script script, final ScriptContext context) {
		final ScriptEngine engine = getScriptEngine(script.getType());
		return executeScript(script, engine, context);
	}

	/**
	 * Executes the given script with context as the ScriptContext. The values in the context are available as variables in the script. The names of these variables will be the attributes in the context
	 * @param scriptName Name of script to be executed
	 * @param context
	 * @return The result from the script, or the value of the script variable "result"
	 * @throws ValidationException , if the script returns a list of ValidationErrors
	 */
	public Object executeScript(final String scriptName, final ScriptContext context) {
		return executeScript(getScript(scriptName, DateUtils.today()), context);
	}

	/**
	 * Executes the given script with context as the ScriptContext as on given date. This means that the script that was valid as on given date will be executed (not the currently valid one). The values in the context are available as variables in the script. The names of these variables will be the
	 * attributes in the context.
	 * @param scriptName Name of script to be executed
	 * @param context
	 * @param asOnDate The date as on which the script is to be executed
	 * @return The result from the script, or the value of the script variable "result"
	 * @throws ValidationException , if the script returns a list of ValidationErrors
	 */
	public Object executeScript(final String scriptName, final ScriptContext context, final Date asOnDate) {
		return executeScript(getScript(scriptName, asOnDate), context);
	}

	/**
	 * Checks if the script has returned any validation errors. If yes, throws a new <code>ValidationException</code> containing the errors.
	 * @param errors The value of "validationErrors" attributes from script context
	 */
	private void handleErrorsIfAny(final Object errors) {
		if (errors != null) {
			List<ValidationError> validationErrors = null;
			if (errors instanceof List) {
				validationErrors = (List<ValidationError>) errors;
			} else if (errors instanceof Map) {
				validationErrors = toErrors((Map) errors);
			} else {
				validationErrors = Arrays.asList(new ValidationError(errors.toString(), errors.toString()));
			}
			throw new ValidationException(validationErrors);
		}
	}

	/**
	 * Converts a map of validation errors to a list of ValidationError objects assuming that key = error key and value = error message
	 * @param errors Map of validation errors
	 * @return List of ValidationError objects
	 */
	private List<ValidationError> toErrors(final Map errors) {
		List<ValidationError> validationErrors;
		validationErrors = new LinkedList<ValidationError>();
		final Set<Entry> errorEntries = errors.entrySet();
		for (final Entry entry : errorEntries) {
			validationErrors.add(new ValidationError(entry.getKey().toString(), entry.getValue().toString()));
		}
		return validationErrors;
	}

	/**
	 * Clears the script cache
	 */
	public void clearScriptCache() {
		scriptCache.clear();
	}
}

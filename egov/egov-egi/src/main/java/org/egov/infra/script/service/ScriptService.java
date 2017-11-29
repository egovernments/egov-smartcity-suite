/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */

package org.egov.infra.script.service;

import org.egov.infra.cache.impl.LRUCache;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.script.entity.Script;
import org.egov.infra.script.repository.ScriptRepository;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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

@Transactional(readOnly=true)
@Service
public class ScriptService  {
    private static LRUCache<String, Script> scriptCache;
    private static final Logger LOG = LoggerFactory.getLogger(ScriptService.class);
    
    @Autowired
    private ScriptRepository scriptRepository;
    
    @Autowired
    private ScriptEngineProvider scriptEngineProvider;
    
    public Script getByName(String name) {
        return scriptRepository.findByName(name);
    }

    public Script findByNameAndPeriod( String name, Date period) {
        return scriptRepository.findByNameAndPeriod(name, period);
    }
    
    public ScriptService() {
        if (scriptCache == null)
            scriptCache = new LRUCache<String, Script>(10, 50);
    }

    /**
     * Takes an even number of arguments, and creates a
     * <code>ScriptContext</code> object, with ith argument as the key and
     * (i+1)th argument as the value. This is why this method expects an even
     * number of arguments.
     * 
     * @param args
     *            Arguments from which the context will be created
     * @return The script context in which a script can be executed. This can be
     *         passed as second argument to the method
     *         {@link ScriptService#executeScript(String, ScriptContext)}
     */
    public static ScriptContext createContext(final Object... args) {
        final SimpleScriptContext context = new SimpleScriptContext();
        if (args.length % 2 != 0)
            throw new ApplicationRuntimeException("Number of arguments must be even");
        for (int i = 0; i < args.length; i += 2)
            context.setAttribute((String) args[i], args[i + 1], ScriptContext.ENGINE_SCOPE);
        return context;
    }

    /**
     * Loads all functions from given script.
     * 
     * @param scriptName
     *            Script which is to be loaded
     */
    public void loadFunctionsFromScript(final String scriptName) {
        final Script script = getScript(scriptName, DateUtils.today());
        final ScriptEngine engine = scriptEngineProvider.getScriptEngine(script.getType());
        executeScript(script, engine, engine.getContext());
    }

    /**
     * Sets standard context attributes that can be used inside the script.
     * These are: <br>
     * <code>scriptService</code> - Instance of script service - can be used to
     * invoke another script, load functions from a script, or execute an
     * already loaded function.<br>
     * <code>scriptEngine</code> - The script engine - to be passed as first
     * argument to the
     * {@link ScriptService#executeFunction(ScriptEngine, String, Object...)}
     * <br>
     * <code>scriptContext</code> - The script context - to be passed as second
     * argument to the
     * {@link ScriptService#executeScript(String, ScriptContext)} API<br>
     * 
     * @param engine
     *            The script engine
     * @param context
     *            The script context
     */
    private void setupContextAttributes(final ScriptEngine engine, final ScriptContext context) {
        context.setAttribute("scriptService", this, ScriptContext.ENGINE_SCOPE);
        context.setAttribute("scriptEngine", engine, ScriptContext.ENGINE_SCOPE);
        context.setAttribute("scriptContext", context, ScriptContext.ENGINE_SCOPE);
    }

    /**
     * Executes the given script using given script engine and context
     * 
     * @param script
     *            Script to be executed
     * @param engine
     *            Engine to be used for executing the script
     * @param context
     *            Context in which the script is to be executed
     * @return The result from the script, or the value of the script variable
     *         "result"
     * @throws ValidationException
     *             if the script returns a list of ValidationErrors
     * @throws EGOVRuntimException
     *             if script execution throws an exception
     */
    private Object executeScript(final Script script, final ScriptEngine engine, final ScriptContext context) {
        if (context == null) {
            // Context must be passed. We don't want to use the default engine
            // context as it will keep on growing over a period of time
            final String errMsg = "ScriptContext not passed to executeScript method!";
            LOG.error(errMsg);
            throw new ApplicationRuntimeException(errMsg);
        }

        Object evalResult = null;

        // Set up standard context attributes
        setupContextAttributes(engine, context);

        try {
            final CompiledScript compiledScript = script.getCompiledScript();
            if (engine instanceof Compilable && compiledScript != null)
                // Script engine supports compiled scripts.
                // Execute the compiled script using given context.
                evalResult = compiledScript.eval(context);
            else
                // Script engine doesn't support compiled scripts.
                // Set the context on engine and execute the script.
                evalResult = engine.eval(script.getScript(), context);

            handleErrorsIfAny(context.getAttribute("validationErrors"));

            // No errors. Get and return the result.
            final Object result = context.getAttribute("result");
            return evalResult == null ? result : evalResult;
        } catch (final ScriptException e) {
            LOG.error("script error for " + script.getType() + ":" + script.getName() + ":" + script.getScript(), e);
            throw new ApplicationRuntimeException("script.error", e);
        } catch (final  ValidationException e) {
            if(e.getErrors()!=null && !e.getErrors().isEmpty())
            LOG.error(e.getErrors().get(0).getMessage());
            throw e;
        }
        catch (final  Exception e) {
            LOG.error("Exception  for " + script.getType() + ":" + script.getName() + ":" + script.getScript(), e);
            throw new ApplicationRuntimeException("script.error", e);
        }
    }

    /**
     * Executes given function using given script engine
     * 
     * @param engine
     *            Script engine to be used for executing the function
     * @param functionName
     *            Name of function to be executed
     * @return Return value from the function
     */
    public Object executeFunctionNoArgs(final ScriptEngine engine, final String functionName) {
        return executeFunction(engine, functionName);
    }

    /**
     * Executes given function using given script engine
     * 
     * @param engine
     *            Script engine to be used for executing the function
     * @param functionName
     *            Name of function to be executed
     * @param args
     *            Arguments to be passed to the function
     * @return Return value from the function
     */
    public Object executeFunction(final ScriptEngine engine, final String functionName, final Object... args) {
        Object evalResult = null;
        if (engine instanceof Invocable)
            try {
                evalResult = ((Invocable) engine).invokeFunction(functionName, args);
                if (evalResult == null)
                    evalResult = engine.get("result");
            } catch (final Exception e) {
                final String errMsg = "Exception while invoking function [" + functionName + "]";
                LOG.error(errMsg, e);
                throw new ApplicationRuntimeException(errMsg, e);
            }
        else {
            final String errMsg = "Script engine [" + engine + "] does not support method execution!";
            LOG.error(errMsg);
            throw new ApplicationRuntimeException(errMsg);
        }
        return evalResult;
    }

    /**
     * Compiles given script if it is not already compiled, and if the
     * corresponding script engine supports compilation
     * 
     * @param script
     *            Script to be compiled
     * @return The compiled script object. Returns null if the script engine
     *         doesn't support compilation.
     */
    private CompiledScript compileScriptIfRequired(final Script script) {
        CompiledScript compiledScript = script.getCompiledScript();

        if (compiledScript != null)
            return compiledScript;

        final ScriptEngine engine = scriptEngineProvider.getScriptEngine(script.getType());
        if (engine instanceof Compilable)
            try {
                // Script engine supports compilation
                compiledScript = ((Compilable) engine).compile(script.getScript());
                script.setCompiledScript(compiledScript);

                // TODO: Add compiledScript column to SCRIPT table and persist
                // the modified object. Problem: JythonCompiledScript is not
                // serializable!
                // scriptService.persist(script);
            } catch (final ScriptException e) {
                final String errMsg = "Could not compile script " + script.getType() + ":" + script.getName() + ":"
                        + script.getScript();
                LOG.error(errMsg, e);
                throw new ApplicationRuntimeException(errMsg, e);
            }

        return compiledScript;
    }

    /**
     * @param scriptName
     *            Script name
     * @param asOnDate
     *            The date against which validity of the script is to be
     *            checked. If null, validity as of current date will be checked.
     * @return Script object for given name
     * @throws ApplicationRuntimeException
     *             if the script is not configured in the system
     */
    private Script getScript(final String scriptName, Date asOnDate) {
    	  Date currentDate = new Date();
    	if (asOnDate != null)
    		currentDate =  asOnDate;

        Script script = scriptCache.get(scriptName);
        if (script != null && new DateTime(script.getPeriod().getEndDate()).isAfter(new DateTime(asOnDate.getTime())))
            // Script found in cache and is still valid
            return script;

        // Script not available in cache. Try to fetch from database.
        
        script = scriptRepository.findByNameAndPeriod(scriptName, currentDate);
        if (script == null)
            throw new ApplicationRuntimeException("Script [" + scriptName + "] not found!");

        // Compile the script if required and possible
        compileScriptIfRequired(script);

        // Add the script to cache
        scriptCache.put(scriptName, script);

        return script;
    }

    /**
     * Executes the given script with context as the ScriptContext. The values
     * in the context are available as variables in the script. The names of
     * these variables will be the attributes in the context.
     * 
     * @param script
     *            Script to be executed
     * @param context
     *            The script context - attributes in this context can be used as
     *            variables inside the script
     * @return The result from the script, or the value of the script variable
     *         "result"
     * @throws ValidationException
     *             if the script returns a list of ValidationErrors
     */
    public Object executeScript(final Script script, final ScriptContext context) {
        final ScriptEngine engine = scriptEngineProvider.getScriptEngine(script.getType());
        return executeScript(script, engine, context);
    }

    /**
     * Executes the given script with context as the ScriptContext. The values
     * in the context are available as variables in the script. The names of
     * these variables will be the attributes in the context
     * 
     * @param scriptName
     *            Name of script to be executed
     * @param context
     * @return The result from the script, or the value of the script variable
     *         "result"
     * @throws ValidationException
     *             , if the script returns a list of ValidationErrors
     */
    public Object executeScript(final String scriptName, final ScriptContext context) {
        return executeScript(getScript(scriptName, DateUtils.today()), context);
    }

    /**
     * Executes the given script with context as the ScriptContext as on given
     * date. This means that the script that was valid as on given date will be
     * executed (not the currently valid one). The values in the context are
     * available as variables in the script. The names of these variables will
     * be the attributes in the context.
     * 
     * @param scriptName
     *            Name of script to be executed
     * @param context
     * @param asOnDate
     *            The date as on which the script is to be executed
     * @return The result from the script, or the value of the script variable
     *         "result"
     * @throws ValidationException
     *             , if the script returns a list of ValidationErrors
     */
    public Object executeScript(final String scriptName, final ScriptContext context, final Date asOnDate) {
        return executeScript(getScript(scriptName, asOnDate), context);
    }

    /**
     * Checks if the script has returned any validation errors. If yes, throws a
     * new <code>ValidationException</code> containing the errors.
     * 
     * @param errors
     *            The value of "validationErrors" attributes from script context
     */
    private void handleErrorsIfAny(final Object errors) {
        if (errors != null) {
            List<ValidationError> validationErrors = null;
            if (errors instanceof List)
                validationErrors = (List<ValidationError>) errors;
            else if (errors instanceof Map)
                validationErrors = toErrors((Map) errors);
            else
                validationErrors = Arrays.asList(new ValidationError(errors.toString(), errors.toString()));
            throw new ValidationException(validationErrors);
        }
    }

    /**
     * Converts a map of validation errors to a list of ValidationError objects
     * assuming that key = error key and value = error message
     * 
     * @param errors
     *            Map of validation errors
     * @return List of ValidationError objects
     */
    private List<ValidationError> toErrors(final Map errors) {
        List<ValidationError> validationErrors;
        validationErrors = new LinkedList<ValidationError>();
        final Set<Entry> errorEntries = errors.entrySet();
        for (final Entry entry : errorEntries)
            validationErrors.add(new ValidationError(entry.getKey().toString(), entry.getValue().toString()));
        return validationErrors;
    }

    /**
     * Clears the script cache
     */
    public void clearScriptCache() {
        scriptCache.clear();
    }
}

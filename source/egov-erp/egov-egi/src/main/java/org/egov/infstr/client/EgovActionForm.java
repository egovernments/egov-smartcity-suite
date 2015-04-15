/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.infstr.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionForm;

/*
 * An abstract base class for ActionForms that adds support for automatic formating and unformatting of string values and for the transfer of the resulting values between itself and the given bean. the <code>populate()</code> methord provides an entry point to this functionality,while the
 * <code>keysToSkip()</code> methord allows subclasses to specify fields that should not be populated. <p> Additional methords are provided to allow subclasses to override formatting defaults. the <code>setDefaultString()</code> methord allow callers to specify for the default string used to
 * represent a <code>null</code> for a given property.similarly,<code>setFormatterType()</code> methord allows callers to specify a formatters type other than the default for a given property
 */
public abstract class EgovActionForm extends ActionForm {
	
	private static final long serialVersionUID = 1L;

	// the convertion from string to Object
	public final static int TO_OBJECT = 0;

	// the convertion from Object to string
	public final static int TO_STRING = 1;

	/*
	 * the strings to display when nulls are encountered ,keys correspond to the fields in the form the precence of a given key indicates that the value provided in the map should be used insted of the normal default strings
	 */
	private final Map defaultStringMap = new HashMap();

	/*
	 * the format to use insted of the default format for a given type
	 */
	private final Map formatMap = new HashMap();

	/*
	 * Tansfers values to and from a given bean,depending on the value of <code>mode</code> if the mode is TO_STRINGpopulates the instance by by intrispecting the specified bean, converting any typed value to formatted strings,and using reflection to invoke its own string based setter methords . If
	 * the mode is TO_OBJECT performs the inverse operation ,unformatting and converting properties of the myform instance and populating the resulting values in the given bean <p> if null values are encountered in the bean ,myform will be populated with the default string associated with the given
	 * type.the default given type.the default null values can be overriden by callin <code>setdefaultString(String key,String value)</code> and providing an alternative string
	 */

	public void populate(final Object bean, final int mode) {

		final String errorMsg = "Unable to format values from bean: ";
		// TO_OBJECT: Source = formbean; destination: object
		// TO_STRING: Source = object; destination: form bean
		final Object source = (mode == TO_STRING ? bean : this);
		final Object target = (mode == TO_STRING ? this : bean);
		final Map valueMap = mapRepresentation(source);
		final Iterator keyIter = valueMap.keySet().iterator();
		while (keyIter.hasNext()) {
			final String currKey = (String) keyIter.next();
			final Object currValue = valueMap.get(currKey);
			Object value = null;
			
				try {
					final Class type = PropertyUtils.getPropertyType(bean, currKey);
					if (type != null) {
						final boolean isNestedObject = isNestedObject(type);
						final boolean isListOfNestedObjects = isListOfNestedObjects(currValue, type);
						// logger.info("isNestedObject== " + isNestedObject + "isListOfNestedObjects==  " + isListOfNestedObjects);
						if (isNestedObject) {
							value = populateNestedObject(currValue, mode);
						}
						if (isListOfNestedObjects) {
							value = populateList((List) currValue, mode);
						}

						if (!(isNestedObject || isListOfNestedObjects)) {
							switch (mode) {
							case TO_OBJECT:
								value =  currValue;
								break;
							case TO_STRING:
								if (currValue == null) {
									value = this.defaultStringMap.get(currKey);
								} else {
									value = currValue;
								}
								break;
							default:
								throw new RuntimeException("Unknown mode: " + mode);
							}
						}
						// logger.info("target ::"+ target + " currKey::"+currKey + " value::"+ value);
						PropertyUtils.setSimpleProperty(target, currKey, value);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} 
			
		}
	}

	/*
	 * Sets the default value to display for the given key when the property value in the associated bean is <code>null</code>
	 * @param key the name of the property,@param value the value to display
	 */
	public void setDefaultString(final String key, final String value) {
		this.defaultStringMap.put(key, value);
	}

	/*
	 * Sets the default formatter class to use for the given key
	 * @param key the name of the property,@param value the value to display
	 */

	public void setFormatterType(final String key, final Class type) {
		this.formatMap.put(key, type);
	}

	/*
	 * Returns a map containing the values from the provided javabean, keyed by field name. Entries that match any of the string returned by <code>keysToSkip()</code> will be removed
	 * @param bean the java bean from which to create the map
	 * @return a map containing values from the provided bean
	 */

	protected Map mapRepresentation(final Object bean) {
		final String errorMsg = "Unable to format values from bean:";
		Map valueMap = null;
		// propertyUtils.describe() uses Introspection to generate a map of values from its argument
		// keyd by its field name
		try {
			valueMap = PropertyUtils.describe(bean);
		} catch (final Exception iae) {
			iae.printStackTrace();
		}
		// remove keys for values that shouldn't be populated
		// subclasses can override keysToSkip() to customize
		final Iterator keyIter = keysToSkip().iterator();
		while (keyIter.hasNext()) {
			final String key = (String) keyIter.next();
			valueMap.remove(key);

		}
		return valueMap;
	}

	/*
	 * retrns an array of keys ,representing the values that should not be populated for the current form instance Subclasses that override this methord to provide additional keys to be skipped should be sure to call the <code>super</code>
	 */

	protected ArrayList keysToSkip() {
		final ArrayList keysToSkip = new ArrayList();
		keysToSkip.add("class");
		keysToSkip.add("servletWrapper");
		keysToSkip.add("multipartRequestHandler");
		keysToSkip.add("resultValueMap");
		return keysToSkip;
	}

	/*
	 * returns an array of keys which should not be formatted for the current form instance. The assumption is that all the classes created by eGov will not need any formatting. Subclasses that override this methord to provide additional keys to be skipped should be sure to call the
	 * <code>super</code>
	 */

	protected boolean skipFormatting(final Class cls) {
		if (cls.getName().startsWith("org.egov")) {
			return true;
		}
		return false;
	}

	
	/**
	 * Returns a class corresponding to the provided class. Used by the infrastructure to determine the binding between ActionForms and their associated JavaBean classes. This method must be overridden by subclasses that contain nested objects to return the appropriate types.
	 * @param type The given class
	 * @return The Class corresponding to the provided type
	 */
	public Class mappedType(final Class type) {
		return null;
	}

	/**
	 * Returns <code>true</code> if the provided object is a List containing nested objects; false otherwise.
	 * @param object The potential List
	 * @param type The class of the provided object
	 * @return boolean
	 */
	public static boolean isListOfNestedObjects(final Object object, final Class type) {
		if (type == null) {
			return false;
		}
		if (List.class.isAssignableFrom(type)) {
			final Iterator listIter = ((List) object).iterator();
			if (listIter.hasNext()) {
				final Object obj = listIter.next();
				if (isNestedObject(obj.getClass())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Returns <code>true</code> if an object of the provided type should be considered a nested object, false otherwise.
	 * @param type The type to be tested
	 * @return boolean
	 */
	public static boolean isNestedObject(final Class type) {
		if (type == null) {
			return false;
		}
		if (AbstractDO.class.isAssignableFrom(type) || EgovActionForm.class.isAssignableFrom(type)) {
			return true;
		}
		return false;
	}

	/**
	 * If the provided List is non-null, returns a List of objects populated with the values of the objects in the provided List.
	 * @param source The original list of objects
	 * @param mode The direction in which binding is taking place
	 * @return A List of objects
	 */
	protected List populateList(final List source, final int mode) throws InstantiationException, IllegalAccessException {
		if (source == null) {
			return null;
		}
		final ArrayList target = new ArrayList();
		final Iterator sourceIter = source.iterator();
		while (sourceIter.hasNext()) {
			final Object currObj = sourceIter.next();
			if (currObj instanceof AbstractDO || currObj instanceof EgovActionForm) {
				final Object newObj = populateNestedObject(currObj, mode);
				target.add(newObj);
			}
		}
		return target;
	}

	/**
	 * If the provided object is non-null, returns a new object populated with values from the provided object. The new object will be of the type returned by a call to the <code>mappedType()</code> method, which subclasses should override to return an appropriate type.
	 * @param source The nested object
	 * @param mode The direction in which binding is taking place
	 * @return An object populated with values from the nested object.
	 */
	protected Object populateNestedObject(final Object source, final int mode) throws InstantiationException, IllegalAccessException {
		if (source == null) {
			return null;
		}
		final Class sourceClass = source.getClass();
		final Class targetClass = mappedType(sourceClass);
		final Object target = targetClass.newInstance();
		if (target instanceof EgovActionForm) {
			((EgovActionForm) target).populate(source, mode);
		} else {
			((EgovActionForm) source).populate(target, mode);
		}
		return target;
	}
}// end of class


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
package com.exilant.exility.common;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Static utility Class to set/get field of/from an object we will not worry about any exception. We only promised that we will
 * TRY :-)
 */

public class ObjectGetSetter {

    private static final Logger logger = Logger.getLogger(ObjectGetSetter.class);

    private ObjectGetSetter() {
    }

    /****************************************************
     * Gets the value of a field of an object as an object. Caller can cast this to appropraite class or inspect its type before
     * using it.
     *
     * @param object : object whose field value is to be returned
     * @param fieldName : Name of the field
     * @return String representing the String value of the field
     */
    public static Object get(final Object object, final String fieldName) {

        try {
            final Field field = ObjectGetSetter.getField(object, fieldName);
            field.setAccessible(true);
            return field.get(object).toString();
        } catch (final Exception e) {
            // logger.error("Problem in get()"+object+"for Fiels"+fieldName);
        } // let us not worry about any exception. We only promised that we will TRY :-)
        return null;
    }

    /*********************************************
     * Returns a HashMap of name/value pairs for all fields declared by the class or super classes Note: fields with 'private'
     * modifiers are not picked up.
     *
     * @param object : object from which the fields are to be extracted
     * @param primitivesonly : Data types, int, Integer, float, Float, ..... and String are considered to be primitive If this
     * field is set to true, fields of only these type are picked up. Else all fields are picked up.
     * @return HashMap containing name/value pairs of all field values
     */
    public static HashMap getAll(final Object object, final boolean primitivesonly) {

        final HashMap values = new HashMap();
        Field[] fields;
        Field field;
        /*
         * Get fields declared in this as well as super classes, but ones that are not private
         */
        Class cls = object.getClass();
        while (!cls.equals(Object.class)) { // go up the super class till you reach Object.
            fields = cls.getDeclaredFields();
            for (final Field field2 : fields) {
                field = field2;
                if (field.getModifiers() == 2)
                    continue; // let us keep private fields private !!

                // int, char ets return isPrimitive() = true. Classes like Integer, String are defined in java.lang. These are
                // also primitives
                if (primitivesonly && !field.getType().isPrimitive() && field.getType().getName().indexOf("java.lang.") != 0)
                    continue;

                field.setAccessible(true); // ensures that the field is accessible
                try {
                    values.put(field.getName(), field.get(object));
                } catch (final Exception e) {
                    // logger.error("Error while putting values to HashMap");
                }
            }
            cls = cls.getSuperclass(); // loop back for the declared fields in the superclass
        }
        return values;
    }

    /**********************
     * Get a field from the class or the super classes of the object
     * @param object
     * @param fieldName name of teh field to return
     * @return Field object if found
     * @throws NoSuchFieldException
     */
    public static Field getField(final Object object, final String fieldName) throws NoSuchFieldException {
        Field field = null;
        Class cls = object.getClass();
        while (field == null && !cls.equals(Object.class))
            try {
                field = cls.getDeclaredField(fieldName);
            } catch (final NoSuchFieldException e) {
                // logger.error("Error while getting declared field"+object+"    "+fieldName);
                cls = cls.getSuperclass();
            }
        if (field == null)
            throw new NoSuchFieldException("NoSuchFieldException for field " + fieldName + " for class"
                    + object.getClass().getName());
        return field;

    }

    /**********************************
     * Sets the field to the specified value in the suppled object Either the field name shoudl exist, or a generic HashMap named
     * 'atributes' should exist Else, teh suppled value is not set.
     * @param object
     * @param fieldName
     * @param fieldValue
     */
    public static void set(final Object object, final String fieldName, final String fieldValue) {
        final Class[] clsarr = { String.class };
        final String[] strarr = { fieldValue };
        Field field;
        try {
            field = ObjectGetSetter.getField(object, fieldName);
            field.setAccessible(true);
            final Class cls = field.getType();
            if (cls.isPrimitive() || cls.equals(String.class))
                if (cls.equals(int.class))
                    field.setInt(object, Integer.parseInt(fieldValue));
                else if (cls.equals(long.class))
                    field.setLong(object, Long.parseLong(fieldValue));
                else if (cls.equals(short.class))
                    field.setShort(object, Short.parseShort(fieldValue));
                else if (cls.equals(byte.class))
                    field.setByte(object, Byte.parseByte(fieldValue));
                else if (cls.equals(float.class))
                    field.setFloat(object, Float.parseFloat(fieldValue));
                else if (cls.equals(double.class))
                    field.setDouble(object, Double.parseDouble(fieldValue));
                else if (cls.equals(boolean.class)) {
                    if (fieldValue.equalsIgnoreCase("true")
                            || fieldValue.equalsIgnoreCase("yes")
                            || fieldValue.equals("1"))
                        field.setBoolean(object, true);
                    else
                        field.setBoolean(object, false);

                } else if (cls.equals(char.class))
                    field.setChar(object, fieldValue.charAt(0));
                else if (cls.equals(String.class))
                    field.set(object, fieldValue);
                else { // let us try to create that object with a constructur that accepts String
                    final Object o = cls.getConstructor(clsarr).newInstance(strarr);
                    field.set(object, o);
                }
            return;
        } catch (final Exception e) {
            /*
             * Let us try for a generic field named attributes with a put(object, object) method
             */

            // logger.error(e.getMessage());
            try { // to invoke put (fieldName, fieldValue) for
                final Class[] objectClass = { Object.class, Object.class };
                field = ObjectGetSetter.getField(object, "attributes");
                field.setAccessible(true);
                Object atts = field.get(object);
                final Class fldclass = field.getType();
                // instantiate and assign the instance if required
                if (null == atts) {
                    atts = fldclass.newInstance();
                    field.setAccessible(true);
                    field.set(object, atts);
                }
                final Method met = fldclass.getMethod("put", objectClass);
                final Object[] objarr = { fieldName, fieldValue };
                met.invoke(atts, objarr);
            } catch (final Exception e1) {
                // logger.error(e.getMessage());

            }
        }
    }

    /**********************
     * Sets values from a HashMap containing bname/value pairs to a corresponding fields of the supplied object. Any mismatch is
     * ignored, and no exception is raised
     * @param object
     * @param values
     */
    public static void setAll(final Object object, final HashMap values) {
        Object o;
        Map.Entry entry;
        final Iterator iter = values.entrySet().iterator();
        while (iter.hasNext()) {
            entry = (Map.Entry) iter.next();
            o = entry.getKey();
            if (!o.getClass().equals(String.class))
                continue; // key has to be a string
            ObjectGetSetter.set(object, (String) o, entry.getValue().toString());
        }
    }

    /**********************
     * Sets values from an Attribute containing bname/value pairs to a corresponding fields of the supplied object. Any mismatch
     * is ignored, and no exception is raised
     *
     * @param object
     * @param values
     */
    public static void setAll(final Object object, final Attributes values) {
        for (int i = 0; i < values.getLength(); i++)
            ObjectGetSetter.set(object, values.getQName(i), values.getValue(i));
    }
    /*
     * public static String unescapeXMLChars(String value){ return
     * value.replaceAll("&gt;",">").replaceAll("&lt;","<").replaceAll("&quot;","\"").replaceAll("&amp;", "&"); }
     */
}

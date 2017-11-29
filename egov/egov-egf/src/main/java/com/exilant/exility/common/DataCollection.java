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

import java.util.HashMap;
import java.util.Iterator;

//import java.lang.reflect.Array;

/**
 * @author raghu.bhandi Class designed to be a common carrier of generic data across objects across layers This is also inteded to
 * be carried across to the client tier Recommendation is that we use a naming convention entity_attribute as the name, so that
 * they are unique acros all entities within an enterprise.
 *
 * Allows storage of primitive data type, including String, and single/two dimensional array of these. It internally has a
 * MessageList object, but provides its own methods rather than exposing the MessageList Object
 *
 * USAGE:
 *
 */

public class DataCollection {
    /*
     * all primitive data stored as name/value pair in a hashmap
     */
    private static final Logger LOGGER = Logger.getLogger(DataCollection.class);
    public HashMap values;

    // for all single dimension arraya
    protected HashMap valueLists;

    // two dimension arrays
    protected HashMap grids;

    // three dimensional arrays :-) NO NO, No.. three dimensional arrays are not allaowed at Exilant
    // list of messages (error message, warnings etc..
    protected MessageList messageList;

    /**
     *
     */
    public DataCollection() {
        super();
        values = new HashMap();
        valueLists = new HashMap();
        grids = new HashMap();
        messageList = new MessageList();
    }

    /**********
     *
     * @param name
     * @param value Should be a prmitive data type or String. Other types are not added
     * @return value is returned is added, else null is returned
     */
    public void addValue(final String name, final String value) {
        addValueHelper(name, value);
    }

    public void addValue(final String name, final int value) {
        addValueHelper(name, Integer.valueOf(value));
    }

    public void addValue(final String name, final long value) {
        addValueHelper(name, Long.valueOf(value));
    }

    public void addValue(final String name, final float value) {
        addValueHelper(name, new Float(value));
    }

    public void addValue(final String name, final double value) {
        addValueHelper(name, new Double(value));
    }

    public void addValue(final String name, final boolean value) {
        addValueHelper(name, Boolean.valueOf(value));
    }

    private void addValueHelper(final String name, final Object value) {
        if (null == values)
            values = new HashMap();
        values.put(name, value);
    }

    public String getValue(final String name) {
        final Object obj = values.get(name);
        if (null == obj)
            return "";
        return obj.toString();
    }

    public float getFloat(final String name) {
        final Object obj = values.get(name);
        if (obj instanceof Number)
            return ((Number) obj).floatValue();
        try {
            return Float.parseFloat(obj.toString());
        } catch (final Exception e1) {
            LOGGER.error("Inside getFloat" + e1.getMessage());
            return 0;
        }
    }

    public double getDouble(final String name) {
        final Object obj = values.get(name);
        if (obj instanceof Number)
            return ((Number) obj).doubleValue();
        try {
            return Double.parseDouble(obj.toString());
        } catch (final Exception e1) {
            LOGGER.error("Inside getDouble" + e1.getMessage());
            return 0;
        }
    }

    public int getInt(final String name) {
        final Object obj = values.get(name);
        if (obj instanceof Number)
            return ((Number) obj).intValue();
        try {
            return Integer.parseInt(obj.toString());
        } catch (final Exception e1) {
            LOGGER.error("Inside getInt" + e1.getMessage());
            return 0;
        }
    }

    public long getLong(final String name) {
        final Object obj = values.get(name);
        if (obj instanceof Number)
            return ((Number) obj).longValue();
        try {
            return Long.parseLong(obj.toString());
        } catch (final Exception e1) {
            LOGGER.error("Inside getLong" + e1.getMessage());
            return 0;
        }
    }

    public boolean getBoolean(final String name) {
        final Object obj = values.get(name);
        if (obj instanceof Boolean)
            return ((Boolean) obj).booleanValue();
        if (obj.toString().equalsIgnoreCase("true")
                || obj.toString().equalsIgnoreCase("yes")
                || obj.toString().equals("1"))
            return true;
        return false;
    }

    public void addValueList(final String name, final String[] anArray) {
        valueLists.put(name, anArray);
    }

    public String[] getValueList(final String name) {
        try {
            return (String[]) valueLists.get(name);
        } catch (final Exception e) {
            LOGGER.error("Inside getValueList" + e.getMessage());
        }
        final String[] arr = new String[0];
        return arr;
    }

    public void addGrid(final String name, final String[][] grid) {
        grids.put(name, grid);
    }

    public String[][] getGrid(final String name) {
        final Object obj = grids.get(name);
        try {
            return (String[][]) obj;
        } catch (final Exception e) {
            LOGGER.error("Typecasting error in getGrid" + e.getMessage());
        }
        final String[][] arr = new String[0][0];
        return arr;
    }

    public boolean hasName(final String name) {
        return values.containsKey(name);
    }

    public boolean hasList(final String name) {
        return valueLists.containsKey(name);
    }

    public boolean hasgrid(final String name) {
        return grids.containsKey(name);
    }

    /************
     *
     */
    public int addMessage(final String code) {
        return messageList.add(code);
    }

    public int addMessage(final String code, final String p1) {
        return messageList.add(code, p1);
    }

    public int addMessage(final String code, final String p1, final String p2) {
        return messageList.add(code, p1, p2);
    }

    public int addMessage(final String code, final String p1, final String p2, final String p3) {
        return messageList.add(code, p1, p2, p3);
    }

    public int addMessage(final String code, final String p1, final String p2, final String p3, final String p4) {
        return messageList.add(code, p1, p2, p3, p4);
    }

    public int addMessage(final String code, final String[] parms) {
        return messageList.add(code, parms);
    }

    public int getSevirity() {
        return messageList.getSevirity();
    }

    public String getMessageText() {
        return messageList.toString();
    }

    public MessageList getMessageList() {
        return messageList;
    }

    public Iterator getFieldNames() {
        return values.keySet().iterator();
    }

    public Iterator getListNames() {
        return valueLists.keySet().iterator();

    }

    public Iterator getGridNames() {
        return grids.keySet().iterator();
    }

    @Override
    public String toString() {
        return XMLGenerator.getInstance().toXML(this, "DataCollection", "");
    }
    /*
     * public static void main(String[] args) { DataCollection dc = new DataCollection(); dc.addValue("abcd" , "ABCD");
     * dc.addValue("abcd" , "MNOP"); dc.addValue("anInt" , 12); dc.addValue("aFloat" , 12.12345); String[] a =
     * {"1","2","3","4","5","6","7"}; String[] b = {"a","b","c"}; String[][] c = {{"a","b","c"},{"w","x","y","z"}}; String[][] q =
     * {a,{"10","11","12","23","24"}}; dc.addValueList("an int Array",a ); dc.addValueList("a String Array",b );
     * dc.addGrid("a grid",c ); dc.addGrid("a String grid",c ); dc.addGrid("an int grid",q ); dc.addMessage("a" ,"a", "b", "c",
     * "d"); dc.addMessage("b" , b); dc.addMessage("c"); dc.addMessage("d"); dc.addMessage("e"); dc.addMessage("f");
     * if(LOGGER.isDebugEnabled()) LOGGER.debug("values has " + dc.values.size() + " entries"); if(LOGGER.isDebugEnabled())
     * LOGGER.debug("ValueList has " + dc.valueLists.size() + " entries"); if(LOGGER.isDebugEnabled()) LOGGER.debug("Grid has " +
     * dc.grids.size() + " entries"); if(LOGGER.isDebugEnabled()) LOGGER.debug("value of abcd is " +dc.getValue("abcd"));
     * if(LOGGER.isDebugEnabled()) LOGGER.debug("anInt = " +dc.getInt("anInt")); if(LOGGER.isDebugEnabled())
     * LOGGER.debug("aFloat = " +dc.getFloat("aFloat")); if(LOGGER.isDebugEnabled()) LOGGER.debug("JUNK = " +dc.getValue("JUNK"));
     * if(LOGGER.isDebugEnabled()) LOGGER.debug("an invalid value = " +dc.getValue("an invalid value"));
     * if(LOGGER.isDebugEnabled()) LOGGER.debug("value of an int Array " +dc.getValueList("an int Array"));
     * if(LOGGER.isDebugEnabled()) LOGGER.debug("value of a String Array " +dc.getValueList("a String Array"));
     * if(LOGGER.isDebugEnabled()) LOGGER.debug("value of abcd as avluelist " +dc.getValueList("abcd"));
     * if(LOGGER.isDebugEnabled()) LOGGER.debug("value of a grid is " +dc.getGrid("a grid")); if(LOGGER.isDebugEnabled())
     * LOGGER.debug("value of unkown grid is " +dc.getGrid("unkknown grid")); if(LOGGER.isDebugEnabled())
     * LOGGER.debug("Message sevirity is" + dc.getSevirity()); if(LOGGER.isDebugEnabled()) LOGGER.debug("Message List is "
     * +dc.getMessageText()); if(LOGGER.isDebugEnabled()) LOGGER.debug("And taking the message lis I got : ");
     * if(LOGGER.isDebugEnabled()) LOGGER.debug(dc.getMessageList()); if(LOGGER.isDebugEnabled())
     * LOGGER.debug(" By the way, let me try XML Generator"); if(LOGGER.isDebugEnabled())
     * LOGGER.debug(XMLGenerator.getInstance().toXML(dc, "MyDataCollection", "")); }
     */
}

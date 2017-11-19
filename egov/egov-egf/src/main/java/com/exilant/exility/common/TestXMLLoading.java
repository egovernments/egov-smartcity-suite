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

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author raghu.bhandi
 *
 * Example to demonstrate the use of DefaultDefinition
 */
public class TestXMLLoading /* extends DefaultObject */{
    private static final Logger LOGGER = Logger.getLogger(TestXMLLoading.class);
    // fields inherited from DefaultObject
    // protected String type;
    // protected ArrayList children;
    // protected HashMap attributes;

    // String type;
    String id;
    String type;
    protected float grossMargin;
    protected String img;
    protected boolean booleanValue;
    // a child of same type.
    // in my first version, I was initializing this field with new TestXMLLoading()
    // guess what, that resulted in an infinite loop !!
    // XMLLoader is smart enough to initialize it when required.
    // just declare and leave it to XMLLoader
    protected TestXMLLoading parameter; // = new TestXMLLoading();
    public TestXMLLoading node; // type declararion for nodes
    public ArrayList nodes;
    protected TestXMLLoading child;
    protected HashMap childs;
    protected TestXMLLoading[] childInArrays;
    protected HashMap attributes;

    public TestXMLLoading() {
        super();
    }

    public static void main(final String[] args) {
        final TestXMLLoading de = new TestXMLLoading();
        final XMLLoader xl = new XMLLoader();
        xl.load("resource/testXMLLoading.xml", de);
        // XMLGenerator xg = XMLGenerator.getInstance();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Dumping using XML Generator");

    }
}

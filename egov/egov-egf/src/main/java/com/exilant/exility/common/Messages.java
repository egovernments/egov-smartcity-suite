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
import org.egov.infstr.utils.EGovConfig;

import java.net.URL;
import java.util.HashMap;

/**
 * @author raghu.bhandi
 *
 * Static Class that returns a Message object for a given code, and optional paramaters At this time, it provides only english
 * text. but can be extended to return eror message in different langauegs
 */
public class Messages {

    private static final Logger LOGGER = Logger.getLogger(Messages.class);
    public static final int UNDEFINED = 0; // Code not defined for this
    // installation
    public static final int IGNORE = 1;
    public static final int INFO = 2;
    public static final int WARNING = 3;
    public static final int ERROR = 4;
    /*
     * Singleton pattern is used instad of making the class static. instance carries one instance that the static methods use.
     * There are no public instance methods, not even the constructor.
     */

    private static Messages instance; // stores the singleton instance of this
    // class

    // array contiaining text for sevirity. This is only in English. To be made
    // 'multi-lingual' as and when required
    private final String[] sevirityText = { "UNDEFINED", "", "Information : ",
            "WARNING : ", "ERROR : ", "UNDEFINED" };

    protected Message message;
    public HashMap messages; // contais all the msesages defined for this
    // application. It is stored once.

    /*
     * Not to be instantiated by public
     */

    private Messages() {
        // load the messages from xml file
        messages = new HashMap();
        final XMLLoader xl = new XMLLoader();
        final URL url = EGovConfig.class.getClassLoader().getResource(
                "config/resource/messages.xml");
        // if(LOGGER.isDebugEnabled()) LOGGER.debug("url in Messages=================="+url);
        xl.load(url.toString(), this);
    }

    /*
     * getMessage is defined with different number of parameters for convinience All of them in turn call getMessage(String code,
     * String[] parameters
     */
    public static Message getMessage(final String code) {
        final String[] p = {};
        return getMessage(code, p);
    }

    public static Message getMessage(final String code, final String p1) {
        final String[] p = { p1 };
        return getMessage(code, p);
    }

    public static Message getMessage(final String code, final String p1, final String p2) {
        final String[] p = { p1, p2 };
        return getMessage(code, p);
    }

    public static Message getMessage(final String code, final String p1, final String p2,
            final String p3) {
        final String[] p = { p1, p2, p3 };
        return getMessage(code, p);
    }

    public static Message getMessage(final String code, final String p1, final String p2,
            final String p3, final String p4) {
        final String[] p = { p1, p2, p3, p4 };
        return getMessage(code, p);
    }

    public static Message getMessage(final String code, final String[] parameters) {
        if (instance == null)
            instance = new Messages();
        return instance.get(code, parameters);

    }

    /*
     * Helper method for all variations of getMessgae()
     */
    private Message get(final String code, final String[] params) {
        final Message messageToReturn = new Message();
        messageToReturn.id = code;

        // get the message for the code from the hashmap
        final Object o = messages.get(code);

        try {
            final Message m = (Message) o; // null pointer, or non-message will throw
            // exception
            messageToReturn.sevirity = m.sevirity;
            final StringBuffer sbf = new StringBuffer(sevirityText[m.sevirity]);
            sbf.append(m.text);

            if (params != null) { // replace @1, @2 etc in the message with the
                // paramater supplied
                final int len = params.length;
                int j = 0;
                for (int i = 0; i < len; i++) {
                    j = sbf.indexOf("@" + (i + 1));
                    if (j > 0)
                        sbf.replace(j, j + 2, params[i]);
                }
            }
            messageToReturn.text = sbf.toString();

        } catch (final Exception e) {
            LOGGER.error("Error in getting Message" + e.getMessage());
            messageToReturn.sevirity = Messages.UNDEFINED;
            messageToReturn.text = code + " is not a valid message code"; // what
            // about
            // language??
            // To
            // have
            // this
            // by
            // langauge
            // as
            // well..
        }

        return messageToReturn;
    }

    public static void main(final String[] args) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug(Messages.getMessage("exilNoServiceName", "p11111111",
                    "p2", "p3"));
    }

}

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

package org.egov.infra.validation.regex;

public class Constants {
    public static final String IP_ADDRESS = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
    public static final String PHONE_NUM = "^(\\+[1-9][0-9]*(\\([0-9]*\\)|-[0-9]*-))?[0]?[1-9][0-9\\- ]*$";
    public static final String MOBILE_NUM = "^((\\+)?(\\d{2}[-]))?(\\d{10}){1}?$";
    public static final String STRONG_PASSWORD = "(?=^.{8,32}$)(?=.*\\d)(?!.*[&<>#%\\'\\\"\\\\\\/])(?!.*\\s)(?=.*[A-Z])(?=.*[a-z]).*$";
    public static final String MEDIUM_PASSWORD = "(?=^.{8,32}$)(?=.*\\d)(?!.*\\s)(?=.*[A-Z])(?=.*[a-z]).*$";
    public static final String LOW_PASSWORD = "(?=^.{4,32}$)(?!.*\\s)(?=.*\\d)(?=.*[A-Z])(?=.*[a-z]).*$";
    public static final String NONE_PASSWORD = "(?=^.{6,32}$)(?!.*\\s).*$";
    public static final String EMAIL = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    public static final String MIXEDCHAR = "^[a-z|A-Z|]+[a-z|A-Z|&/ :,-.]*";
    public static final String ALPHANUMERIC = "[0-9a-zA-Z]+";
    public static final String PANNUMBER = "[a-zA-Z]{5}[0-9]{4}[a-zA-Z]{1}";
    public static final String NUMERIC = "[0-9]+";
    public static final String ALPHABETS = "[A-Za-z]+";
    public static final String ALPHANUMERIC_WITHSPACE = "[0-9a-zA-Z ]+";
    public static final String ALPHANUMERIC_WITHSLASHES = "[0-9a-zA-Z/]+";
    public static final String NUMERIC_WITHMIXEDCHAR = "[0-9-,]+";
    public static final String DATEFORMAT = "(0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](19|20)[0-9]{2}";
    /**
     * Matches any unsigned floating point number/NUMERIC. Also matches empty
     * strings.
     */
    public static final String UNSIGNED_NUMERIC = "^\\d*\\.?\\d*$";
    public static final String UNSIGNED_NUMBER = "^\\d*$";
    /**
     * Matches any floating point number/NUMERIC, including optional sign
     * character (-). Also matches empty strings.
     */
    public static final String SIGNED_NUMERIC = "^(\\-)?\\d*(\\.\\d+)?$";
    public static final String SIGNED_NUMBER = "^(\\-)?\\d*$";
    public static final String ALPHABETS_WITHMIXEDCHAR = "[A-Z-_ ]+";
    public static final String ALLTYPESOFALPHABETS_WITHMIXEDCHAR = "[A-Za-z-_ ]+";
    public static final String ALPHANUMERICWITHSPECIALCHAR = "[0-9a-zA-Z-& :,/.()@]+";
    
}

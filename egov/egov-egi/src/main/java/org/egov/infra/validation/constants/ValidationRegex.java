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

package org.egov.infra.validation.constants;

public class ValidationRegex {
    public static final String IP_ADDRESS = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
    public static final String PHONE_NUMBER = "[0-9 -]+";
    public static final String MOBILE_NUMBER = "^(\\d{10}){1}?$";
    public static final String STRONG_PASSWORD = "(?=^.{8,32}$)(?=.*\\d)(?!.*[&<>#%\\'\\\"\\\\\\/])(?!.*\\s)(?=.*[A-Z])(?=.*[a-z]).*$";
    public static final String MEDIUM_PASSWORD = "(?=^.{8,32}$)(?=.*\\d)(?!.*\\s)(?=.*[A-Z])(?=.*[a-z]).*$";
    public static final String LOW_PASSWORD = "(?=^.{4,32}$)(?!.*\\s)(?=.*\\d)(?=.*[A-Z])(?=.*[a-z]).*$";
    public static final String NONE_PASSWORD = "(?=^.{6,32}$)(?!.*\\s).*$";
    public static final String EMAIL = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    public static final String ALPHANUMERIC = "[0-9a-zA-Z]+$";
    public static final String PAN_NUMBER = "[a-zA-Z]{5}[0-9]{4}[a-zA-Z]{1}";
    public static final String NUMERIC = "[0-9]+$";
    public static final String DATEFORMAT = "(0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](19|20)[0-9]{2}";
    public static final String FILE_NAME = "^[\\w\\[\\]()\\-\\s]{1,245}.([a-zA-Z]){1,9}$";
    public static final String UNSIGNED_NUMERIC = "^\\d*\\.?\\d*$";
    public static final String MASTER_DATA_CODE = "^[a-zA-Z0-9]+(([_-][a-zA-Z0-9])?[a-zA-Z0-9]*)*$";
    public static final String NAME_WITH_SPECIAL_CHARS = "^[a-zA-Z]+(([ ()/-_][a-zA-Z])?[a-zA-Z]*)*$";
    public static final String NAME_WITH_EXTRA_SPECIAL_CHARS = "^[a-zA-Z]+(([ ()/-_&,.][a-zA-Z])?[a-zA-Z]*)*$";
    public static final String ALPHABETS = "[A-Za-z]+$";
    public static final String ALPHABETS_WITH_SPACE = "^[a-zA-Z]+(([\\s][a-zA-Z])?[a-zA-Z]*)*$";
    public static final String ALPHANUMERIC_WITH_SPACE = "^[a-zA-Z0-9]+(([\\s][a-zA-Z0-9])?[a-zA-Z0-9]*)*$";
    public static final String ALPHANUMERIC_WITH_SLASH = "^[a-zA-Z0-9]+(([/][a-zA-Z0-9])?[a-zA-Z0-9]*)*$";
    public static final String ALPHABETS_UNDERSCORE_HYPHEN_SPACE = "^[a-zA-Z]+(([ _-][a-zA-Z])?[a-zA-Z]*)*$";
    public static final String ALPHANUMERIC_UNDERSCORE_HYPHEN_SPACE = "^[a-zA-Z0-9]+(([ _-][a-zA-Z0-9])?[a-zA-Z0-9]*)*$";
    public static final String ALPHANUMERIC_WITH_SPECIAL_CHARS = "^([a-zA-Z0-9]+([ _\\-&:,/.()@#])?[a-zA-Z0-9])+$";
    public static final String SALUTATION = "^(Mrs?|Miss)$";
    public static final String PERSON_NAME = "^[a-zA-Z]+(([.\\s][a-zA-Z])?[a-zA-Z]*)*$";
    public static final String USERNAME = "^(?:[\\w+( \\w+)*]{2,64}|[[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*]{2,100}@(?:[a-zA-Z0-9-]{1,22}+\\.)+[a-zA-Z]{2,6})$";
    public static final String ADDRESS = "^(\\w*\\s*[\\#\\-\\,\\/\\.\\(\\)\\&\\:\\']*)+$";
}

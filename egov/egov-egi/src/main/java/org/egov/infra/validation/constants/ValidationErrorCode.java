/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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

public final class ValidationErrorCode {
    public static final String INVALID_ALPHABETS_WITH_SPACE = "{invalid.pattern.alphabets.with.space}";
    public static final String INVALID_MASTER_DATA_CODE = "{invalid.pattern.master.data.code}";
    public static final String INVALID_NAME_WITH_SPECIAL_CHARS = "{invalid.pattern.name.with.special.chars}";
    public static final String INVALID_ALPHABETS_UNDERSCORE_HYPHEN_SPACE = "{invalid.pattern.alphabets.with.underscore.hyphen.space}";
    public static final String INVALID_ALPHANUMERIC_UNDERSCORE_HYPHEN_SPACE = "{invalid.pattern.alphanumeric.with.underscore.hyphen.space}";
    public static final String INVALID_ALPHANUMERIC_WITH_SPECIAL_CHARS = "{invalid.pattern.alphanumeric.with.special.chars}";
    public static final String INVALID_NAME_WITH_EXTRA_SPECIAL_CHARS="{invalid.pattern.name.with.extra.special.chars}";
    public static final String INVALID_PHONE_NUMBER = "{invalid.phone.number}";
    public static final String INVALID_MOBILE_NUMBER = "{invalid.mobile.number}";
    public static final String INVALID_ALPHANUMERIC_WITH_SPACE = "{invalid.pattern.alphanumeric.with.space}";
    public static final String INVALID_PAN_NUMBER = "{invalid.pan.number}";
    public static final String INVALID_SALUTATION = "{invalid.salutation}";
    public static final String INVALID_PERSON_NAME = "{invalid.person.name}";
    public static final String INVALID_USERNAME = "{invalid.username}";
    public static final String INVALID_ALPHABETS = "{invalid.pattern.alphabet}";
    public static final String INVALID_FILE_NAME = "{invalid.file.name}";
    public static final String INVALID_ADDRESS = "{invalid.address}";
    public static final String INVALID_NUMERIC = "{invalid.numeric}";

    private ValidationErrorCode() {
        //only static fields
    }
}

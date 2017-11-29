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

package org.egov.infra.validation;

import org.egov.infra.exception.ApplicationRuntimeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.egov.infra.validation.regex.Constants.LOW_PASSWORD;
import static org.egov.infra.validation.regex.Constants.MEDIUM_PASSWORD;
import static org.egov.infra.validation.regex.Constants.NONE_PASSWORD;
import static org.egov.infra.validation.regex.Constants.STRONG_PASSWORD;

@Service("validatorUtils")
public class ValidatorUtils {

    private Pattern passwordPattern;

    public ValidatorUtils(@Value("${user.pwd.strength}") String passwordStrength) {
        if ("high".equals(passwordStrength))
            this.passwordPattern = Pattern.compile(STRONG_PASSWORD);
        else if ("medium".equals(passwordStrength))
            this.passwordPattern = Pattern.compile(MEDIUM_PASSWORD);
        else if ("low".equals(passwordStrength))
            this.passwordPattern = Pattern.compile(LOW_PASSWORD);
        else
            this.passwordPattern = Pattern.compile(NONE_PASSWORD);
    }

    public static void assertNotNull(Object value, String message) {
        if (value == null)
            throw new ApplicationRuntimeException(message);
    }

    public static void assertNotNull(String value, String message) {
        if (isBlank(value))
            throw new ApplicationRuntimeException(message);
    }

    public boolean isValidPassword(String pwd) {
        return isNotBlank(pwd) && passwordPattern.matcher(pwd).find();
    }
}

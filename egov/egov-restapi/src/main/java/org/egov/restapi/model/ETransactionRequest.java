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

package org.egov.restapi.model;

import org.egov.infra.persistence.validator.annotation.Numeric;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.restapi.validator.annotation.NoFutureDate;
import org.egov.restapi.validator.annotation.OverlappedDateRange;
import org.egov.restapi.validator.annotation.ThisCityCode;

import javax.validation.constraints.Pattern;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@OverlappedDateRange(fromDate = "fromDate", toDate = "toDate", dateFormat = ETransactionRequest.DATE_FORMAT, message = "fromDate must be less than or equal to toDate")
public class ETransactionRequest {

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");

    private static final String DATE_PATTERN = "([0-3][0-9])-((0[0-9])|(1[0-2]))-(\\d\\d\\d\\d)";
    protected static final String DATE_FORMAT = "dd-MM-yyyy";

    @NoFutureDate(dateFormat = DATE_FORMAT)
    @Pattern(regexp = DATE_PATTERN, message = "fromDate must be of dd-MM-yyyy pattern")
    @Required
    private String fromDate;

    @NoFutureDate(dateFormat = DATE_FORMAT)
    @Pattern(regexp = DATE_PATTERN, message = "fromDate must be of dd-MM-yyyy pattern")
    @Required
    private String toDate;

    @ThisCityCode
    @Numeric
    @Required
    private String ulbCode;

    public ETransactionRequest() {
    }

    public ETransactionRequest(String fromDate, String toDate, String ulbCode) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.ulbCode = ulbCode;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getUlbCode() {
        return ulbCode;
    }

    public void setUlbCode(String ulbCode) {
        this.ulbCode = ulbCode;
    }

    public Date getParsedFromDate() {
        try {
            return SIMPLE_DATE_FORMAT.parse(fromDate);
        } catch (ParseException e) {
            throw new ValidationException(new ValidationError("INVALID_DATE", "Invalid fromDate"));
        }
    }

    public Date getParsedToDate() {
        try {
            return SIMPLE_DATE_FORMAT.parse(toDate);
        } catch (ParseException e) {
            throw new ValidationException(new ValidationError("INVALID_DATE", "Invalid toDate"));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        ETransactionRequest that = (ETransactionRequest) o;


        if (fromDate != null && !fromDate.equals(that.fromDate))
            return false;
        if (toDate != null && !toDate.equals(that.toDate))
            return false;
        return (ulbCode == that.ulbCode && ulbCode == null) || ulbCode.equals(that.ulbCode);

    }

    @Override
    public String toString() {
        return "ETransactionRequest{" +
                "fromDate='" + fromDate + '\'' +
                ", toDate='" + toDate + '\'' +
                ", ulbCode='" + ulbCode + '\'' +
                '}';
    }
}

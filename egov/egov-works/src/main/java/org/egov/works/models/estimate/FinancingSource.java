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
package org.egov.works.models.estimate;

import org.egov.commons.Fundsource;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infstr.models.BaseModel;

import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FinancingSource extends BaseModel {

    private static final long serialVersionUID = 8308765350444092816L;

    public FinancingSource() {
    }

    @Min(value = 0, message = "financingsource.percentage.not.negative")
    private double percentage;

    private Fundsource fundSource;
    private FinancialDetail financialDetail;

    private Date lastModifiedDate;

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(final double percentage) {
        this.percentage = percentage;
    }

    public Fundsource getFundSource() {
        return fundSource;
    }

    public void setFundSource(final Fundsource fundSource) {
        this.fundSource = fundSource;
    }

    public FinancialDetail getFinancialDetail() {
        return financialDetail;
    }

    public void setFinancialDetail(final FinancialDetail financialDetail) {
        this.financialDetail = financialDetail;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(final Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public List<ValidationError> validate() {
        final List<ValidationError> validationErrors = new ArrayList<ValidationError>();

        if (fundSource == null || fundSource.getCode() == null)
            validationErrors.add(new ValidationError("invalidpercentage", "financingsource.fundsource.null"));

        if (percentage <= 0.0 || percentage > 100)
            validationErrors.add(new ValidationError("invalidpercentage", "financingsource.invalid.percentage"));
        return validationErrors;
    }
}

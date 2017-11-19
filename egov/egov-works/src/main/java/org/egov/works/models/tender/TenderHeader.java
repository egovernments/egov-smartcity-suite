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
package org.egov.works.models.tender;

import org.egov.infra.persistence.validator.annotation.DateFormat;
import org.egov.infra.persistence.validator.annotation.OptionalPattern;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infstr.models.BaseModel;
import org.egov.works.utils.DateConversionUtil;
import org.egov.works.utils.WorksConstants;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class TenderHeader extends BaseModel {

    private static final long serialVersionUID = 6208133912780798895L;

    @Required(message = "tenderHeader.tenderNo.null")
    @Length(max = 50, message = "tenderHeader.tenderNo.length")
    @OptionalPattern(regex = WorksConstants.alphaNumericwithspecialchar, message = "tenderHeader.tenderNo.alphaNumeric")
    private String tenderNo;

    @Required(message = "tenderHeader.tenderDate.null")
    @DateFormat(message = "invalid.fieldvalue.tenderDate")
    private Date tenderDate;

    @Valid
    private List<TenderEstimate> tenderEstimates = new LinkedList<TenderEstimate>();

    public String getTenderNo() {
        return tenderNo;
    }

    public void setTenderNo(final String tenderNo) {
        this.tenderNo = tenderNo;
    }

    public Date getTenderDate() {
        return tenderDate;
    }

    public void setTenderDate(final Date tenderDate) {
        this.tenderDate = tenderDate;
    }

    public List<TenderEstimate> getTenderEstimates() {
        return tenderEstimates;
    }

    public void setTenderEstimates(final List<TenderEstimate> tenderEstimates) {
        this.tenderEstimates = tenderEstimates;
    }

    public void addTenderEstimate(final TenderEstimate tenderEstimate) {
        tenderEstimates.add(tenderEstimate);
    }

    @Override
    public List<ValidationError> validate() {
        final List<ValidationError> validationErrors = new ArrayList<ValidationError>();
        for (final TenderEstimate tenderEstimate : tenderEstimates)
            if (tenderDate != null)
                if (tenderEstimate.getAbstractEstimate() != null
                        && DateConversionUtil.isBeforeByDate(tenderDate, tenderEstimate.getAbstractEstimate()
                                .getEstimateDate()))
                    validationErrors.add(new ValidationError("tenderDate",
                            "tenderEstimate.tenderDate.cannot_greaterthan_estimateDate"));
                else if (tenderEstimate.getWorksPackage() != null
                        && DateConversionUtil.isBeforeByDate(tenderDate, tenderEstimate.getWorksPackage()
                                .getWpDate()))
                    validationErrors.add(new ValidationError("tenderDate",
                            "tenderEstimate.tenderDate.cannot_greaterthan_wpDate"));
        return validationErrors;
    }
}

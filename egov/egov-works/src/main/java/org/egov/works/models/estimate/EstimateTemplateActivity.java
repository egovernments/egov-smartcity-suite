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
package org.egov.works.models.estimate;

import org.egov.common.entity.UOM;
import org.egov.infra.persistence.entity.component.Money;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infstr.models.BaseModel;
import org.egov.works.abstractestimate.entity.NonSor;
import org.egov.works.models.masters.ScheduleOfRate;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

public class EstimateTemplateActivity extends BaseModel {

    private static final long serialVersionUID = 7697746931463590763L;

    private EstimateTemplate estimateTemplate;

    private ScheduleOfRate schedule;

    @Valid
    private NonSor nonSor;

    private UOM uom;

    private Money rate = new Money(0.0);

    public EstimateTemplate getEstimateTemplate() {
        return estimateTemplate;
    }

    public void setEstimateTemplate(final EstimateTemplate estimateTemplate) {
        this.estimateTemplate = estimateTemplate;
    }

    public ScheduleOfRate getSchedule() {
        return schedule;
    }

    public void setSchedule(final ScheduleOfRate schedule) {
        this.schedule = schedule;
    }

    public NonSor getNonSor() {
        return nonSor;
    }

    public void setNonSor(final NonSor nonSor) {
        this.nonSor = nonSor;
    }

    public UOM getUom() {
        return uom;
    }

    public void setUom(final UOM uom) {
        this.uom = uom;
    }

    public Money getRate() {
        return rate;
    }

    public void setRate(final Money rate) {
        this.rate = rate;
    }

    @Override
    public List<ValidationError> validate() {
        final List<ValidationError> validationErrors = new ArrayList<ValidationError>();
        /*
         * if (rate.getValue() <= 0.0) { validationErrors.add(new ValidationError("estimateTemplateActivity.rate.not.null",
         * "estimateTemplateActivity.rate.not.null")); }
         */
        if (nonSor != null
                && (nonSor.getUom() == null || nonSor.getUom().getId() == null || nonSor.getUom().getId() == 0))
            validationErrors.add(new ValidationError("estimateTemplateActivity.nonsor.invalid",
                    "estimateTemplateActivity.nonsor.invalid"));
        return validationErrors;
    }

}

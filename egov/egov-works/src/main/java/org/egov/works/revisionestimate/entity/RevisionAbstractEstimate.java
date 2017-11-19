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
package org.egov.works.revisionestimate.entity;

import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.abstractestimate.entity.Activity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "EGW_REVISION_ESTIMATE")
public class RevisionAbstractEstimate extends AbstractEstimate {

    
    private static final long serialVersionUID = 4389815027807161766L;

    @Transient
    private List<String> revisionEstActions = new ArrayList<String>();

    @Transient
    private String additionalRule;

    @Transient
    private BigDecimal amountRule;

    @Override
    public String getStateDetails() {
        return "Revision Estimate : " + getEstimateNumber();
    }

    public List<String> getRevisionEstActions() {
        return revisionEstActions;
    }

    public void setRevisionEstActions(final List<String> revisionEstActions) {
        this.revisionEstActions = revisionEstActions;
    }

    public void deleteNonSORActivities() {
        if (getActivities() != null && getActivities().size() > 0)
            for (final Activity estActivity : getActivities())
                if (estActivity.getNonSor() != null)
                    estActivity.setNonSor(null);
    }

    public String getAdditionalRule() {
        return additionalRule;
    }

    public BigDecimal getAmountRule() {
        return amountRule;
    }

    public void setAdditionalRule(final String additionalRule) {
        this.additionalRule = additionalRule;
    }

    public void setAmountRule(final BigDecimal amountRule) {
        this.amountRule = amountRule;
    }

}
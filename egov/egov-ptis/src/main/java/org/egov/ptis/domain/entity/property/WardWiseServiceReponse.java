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

package org.egov.ptis.domain.entity.property;

import java.util.List;
import java.util.Map;

public class WardWiseServiceReponse {
    private String revenueWard;
    private Long applicationsApproved;
    private Long applicationsRejected;
    private Long applicationsPending;
    private Long taxBeforeAffctd;
    private Long taxAfterAffctd;

    public String getRevenueWard() {
        return revenueWard;
    }

    public void setRevenueWard(String revenueWard) {
        this.revenueWard = revenueWard;
    }

    public Long getApplicationsApproved() {
        return applicationsApproved;
    }

    public void setApplicationApproved(Map<String, List<Long>> countMapApproved) {
        List<Long> valuesList = countMapApproved.get("APPROVED");
        this.applicationsApproved = valuesList == null ? Long.valueOf(0) : valuesList.get(0);
        this.taxBeforeAffctd = valuesList == null ? Long.valueOf(0) : valuesList.get(1);
        this.taxAfterAffctd = valuesList == null ? Long.valueOf(0) : valuesList.get(2);
    }

    public Long getApplicationsRejected() {
        return applicationsRejected;
    }

    public void setApplicationsRejected(Map<String, List<Long>> countMapRejected) {
        List<Long> valuesList = countMapRejected.get("REJECTED");
        this.applicationsRejected = valuesList == null ? Long.valueOf(0) : valuesList.get(0);
    }

    public Long getApplicationsPending() {
        return applicationsPending;
    }

    public void setApplicationsPending(Map<String, List<Long>> countMapPending) {
        List<Long> valuesList = countMapPending.get("INPROGRESS");
        this.applicationsPending = valuesList == null ? Long.valueOf(0) : valuesList.get(0);
    }

    public void setCountFieldValues(Map<String, List<Long>> countMap) {
        setApplicationApproved(countMap);
        setApplicationsRejected(countMap);
        setApplicationsPending(countMap);
    }

    public Long getTaxBeforeAffctd() {
        return taxBeforeAffctd;
    }

    public void setTaxBeforeAffctd(Long taxBeforeAffctd) {
        this.taxBeforeAffctd = taxBeforeAffctd;
    }

    public Long getTaxAfterAffctd() {
        return taxAfterAffctd;
    }

    public void setTaxAfterAffctd(Long taxAfterAffctd) {
        this.taxAfterAffctd = taxAfterAffctd;
    }

    
}

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

package org.egov.stms.transactions.repository.spec;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.Arrays;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.egov.stms.entity.contracts.ApplicationSearchRequest;
import org.egov.stms.entity.view.SewerageApplicationView;
import org.springframework.data.jpa.domain.Specification;

public final class ApplicationSearchSpec {
    private ApplicationSearchSpec() {
    }

    public static Specification<SewerageApplicationView> searchApplicationSpecification(
            final ApplicationSearchRequest applicationSearchRequest) {
        return (root, query, builder) -> {
            final Predicate predicates = builder.conjunction();

            if (isNotBlank(applicationSearchRequest.getApplicationNumber()))
                predicates.getExpressions()
                        .add(builder.equal(root.get("applicationNo"), applicationSearchRequest.getApplicationNumber()));
            if (isNotBlank(applicationSearchRequest.getShscNumber()))
                predicates.getExpressions().add(builder.equal(root.get("shscNo"), applicationSearchRequest.getShscNumber()));
            if (isNotBlank(applicationSearchRequest.getApplicantName()))
                predicates.getExpressions()
                        .add(builder.equal(builder.lower(root.get("propertyOwner")),
                                applicationSearchRequest.getApplicantName().toLowerCase()));
            if (isNotBlank(applicationSearchRequest.getRevenueWard()))
                predicates.getExpressions()
                        .add(builder.equal(root.get("revenueWard"), applicationSearchRequest.getRevenueWard()));
            if (isNotBlank(applicationSearchRequest.getMobileNo()))
                predicates.getExpressions().add(builder.equal(root.get("mobileNo"), applicationSearchRequest.getMobileNo()));
            if (applicationSearchRequest.getFromDate() != null)
                predicates.getExpressions().add(
                        builder.greaterThanOrEqualTo(root.get("applicationDate"), applicationSearchRequest.getFromDate()));
            if (applicationSearchRequest.getToDate() != null)
                predicates.getExpressions()
                        .add(builder.lessThanOrEqualTo(root.get("applicationDate"), applicationSearchRequest.getToDate()));
            if (isNotBlank(applicationSearchRequest.getPropertyId()))
                predicates.getExpressions().add(builder.equal(root.get("propertyId"), applicationSearchRequest.getPropertyId()));

            if (isNotBlank(applicationSearchRequest.getApplicationType()))
                predicates.getExpressions()
                        .add(builder.equal(root.get("applicationType"), applicationSearchRequest.getApplicationType()));

            if (isNotBlank(applicationSearchRequest.getConnectionStatus())) {
                predicates.getExpressions()
                        .add(builder.equal(root.get("connectionStatus"), applicationSearchRequest.getConnectionStatus()));
            } else {
                List<String> status = Arrays.asList("ACTIVE", "INPROGRESS");
                predicates.getExpressions().add(root.get("connectionStatus").in(status));
            }
            return predicates;
        };
    }
}
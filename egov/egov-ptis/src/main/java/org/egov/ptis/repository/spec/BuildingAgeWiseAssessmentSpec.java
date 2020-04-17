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
package org.egov.ptis.repository.spec;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.criteria.Predicate;

import org.egov.ptis.domain.entity.property.view.PropertyMVInfo;
import org.egov.ptis.report.bean.BuidingAgeWiseReportResult;
import org.springframework.data.jpa.domain.Specification;

public class BuildingAgeWiseAssessmentSpec {

    private static final String FLOORDETAILS = "floorDetails";
    private static final String CONSTRUCTION_DATE = "constructionDate";

    public static Specification<PropertyMVInfo> pagedAgeWiseRecordSpecification(
            final BuidingAgeWiseReportResult buidingAgeWiseReportResult) {
        return (root, query, builder) -> {
            final Predicate predicate = builder.conjunction();
            if (buidingAgeWiseReportResult.getFromAge() != null) {
                Calendar cal = Calendar.getInstance();
                if (buidingAgeWiseReportResult.getFromAge().equals("0"))
                    cal.add(Calendar.YEAR, Integer.parseInt(buidingAgeWiseReportResult.getFromAge()));
                else
                    cal.add(Calendar.YEAR, Integer.parseInt("-" + buidingAgeWiseReportResult.getFromAge()));
                Date fromDate = getDate(cal);
                predicate.getExpressions()
                        .add(builder.lessThanOrEqualTo(root.join(FLOORDETAILS).get(CONSTRUCTION_DATE), fromDate));
            }
            if (buidingAgeWiseReportResult.getToAge() != null) {
                Calendar calendar = Calendar.getInstance();
                if (buidingAgeWiseReportResult.getToAge().equals("0")) {
                    calendar.add(Calendar.YEAR, Integer.parseInt("-1"));
                    Date toDate = getDate(calendar);
                    predicate.getExpressions()
                            .add(builder.greaterThan(root.join(FLOORDETAILS).get(CONSTRUCTION_DATE), toDate));
                } else{
                    calendar.add(Calendar.YEAR, Integer.parseInt("-" + buidingAgeWiseReportResult.getToAge()));
                    calendar.set(Calendar.HOUR_OF_DAY, 00);
                    calendar.set(Calendar.MINUTE, 00);
                    calendar.set(Calendar.SECOND, 00);
                    calendar.set(Calendar.MILLISECOND, 000);
                Date toDate = calendar.getTime();
                predicate.getExpressions()
                        .add(builder.greaterThanOrEqualTo(root.join(FLOORDETAILS).get(CONSTRUCTION_DATE), toDate));
                }
            }
            if (buidingAgeWiseReportResult.getPropertyTypeMaster() != null
                    && !buidingAgeWiseReportResult.getPropertyTypeMaster().equals("-1"))
                predicate.getExpressions()
                        .add(builder.equal(root.get("propType"), buidingAgeWiseReportResult.getPropertyTypeMaster()));
            query.distinct(true);
            return predicate;
        };
    }

    public static Date getDate(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

}
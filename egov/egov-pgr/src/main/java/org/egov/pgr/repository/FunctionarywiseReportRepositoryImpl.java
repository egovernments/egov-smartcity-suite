/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 *           Further, all user interfaces, including but not limited to citizen facing interfaces,
 *           Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *           derived works should carry eGovernments Foundation logo on the top right corner.
 *
 * 	       For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 * 	       For any further queries on attribution, including queries on brand guidelines,
 *           please contact contact@egovernments.org
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
 */

package org.egov.pgr.repository;

import org.egov.infra.utils.DateUtils;
import org.egov.infra.utils.StringUtils;
import org.egov.infstr.services.Page;
import org.egov.pgr.entity.dto.FunctionarywiseReportRequest;
import org.egov.pgr.entity.view.FunctionarywiseReport;
import org.springframework.data.domain.Sort.Direction;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class FunctionarywiseReportRepositoryImpl implements FunctionarywiseReportRepositoryCustom {

    private static final String CREATED_DATE = "createdDate";
    private static final String EMPLOYEE_ID = "employeeId";
    private static final String EMPLOYEE_NAME = "employeeName";
    private static final String STATUS = "status";
    private static final String REGISTERED = "registered";
    private static final String INPROCESS = "inprocess";
    private static final String COMPLETED = "completed";
    private static final String REOPENED = "reopened";
    private static final String REJECTED = "rejected";
    private static final String WITHINSLA = "withinSLA";
    private static final String BEYONDSLA = "beyondSLA";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<FunctionarywiseReport> findByFunctionarywiseRequest(FunctionarywiseReportRequest functionarywiseReportRequest) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<FunctionarywiseReport> criteriaQuery = criteriaBuilder.createQuery(FunctionarywiseReport.class);
        Root<FunctionarywiseReport> root = criteriaQuery.from(FunctionarywiseReport.class);
        CriteriaQuery<FunctionarywiseReport> recordCountQuery = criteriaBuilder.createQuery(FunctionarywiseReport.class);
        Root<FunctionarywiseReport> countRoot = recordCountQuery.from(FunctionarywiseReport.class);

        recordCountQuery.multiselect(criteriaBuilder.count(countRoot))
                .where(criteria(functionarywiseReportRequest, criteriaBuilder, countRoot)
                        .toArray(new Predicate[]{}))
                .groupBy(countRoot.get(EMPLOYEE_ID), countRoot.get(EMPLOYEE_NAME));

        CriteriaQuery<FunctionarywiseReport> reportQuery = generateReportByRequest(criteriaQuery,
                functionarywiseReportRequest, criteriaBuilder, root);

        if (functionarywiseReportRequest.orderBy().equals(EMPLOYEE_NAME))
            reportQuery.orderBy(functionarywiseReportRequest.orderDir().equals(Direction.ASC)
                    ? criteriaBuilder.asc(root.get(functionarywiseReportRequest.orderBy()))
                    : criteriaBuilder.desc(root.get(functionarywiseReportRequest.orderBy())));
        else
            reportQuery.orderBy(functionarywiseReportRequest.orderDir().equals(Direction.ASC)
                    ? criteriaBuilder.asc(criteriaBuilder.sum(root.get(functionarywiseReportRequest.orderBy())))
                    : criteriaBuilder.desc(criteriaBuilder.sum(root.get(functionarywiseReportRequest.orderBy()))));

        return new Page<>(entityManager.createQuery(reportQuery), functionarywiseReportRequest.pageNumber() + 1,
                functionarywiseReportRequest.pageSize(), entityManager.createQuery(recordCountQuery).getResultList().size());
    }

    @Override
    public Page<FunctionarywiseReport> findComplaintsByEmployeeId(FunctionarywiseReportRequest functionarywiseReportRequest) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<FunctionarywiseReport> criteriaQuery = criteriaBuilder.createQuery(FunctionarywiseReport.class);
        Root<FunctionarywiseReport> root = criteriaQuery.from(FunctionarywiseReport.class);
        CriteriaQuery<FunctionarywiseReport> countCriteriaQuery = criteriaBuilder.createQuery(FunctionarywiseReport.class);
        Root<FunctionarywiseReport> countRoot = countCriteriaQuery.from(FunctionarywiseReport.class);

        countCriteriaQuery.multiselect(criteriaBuilder.count(countRoot.get("crn")))
                .where(criteria(functionarywiseReportRequest, criteriaBuilder, countRoot)
                        .toArray(new Predicate[]{}))
                .groupBy(countRoot.get("crn"));


        TypedQuery<FunctionarywiseReport> query = entityManager.createQuery(generateReportByEmployeeId(criteriaQuery,
                functionarywiseReportRequest, criteriaBuilder, root)
                .orderBy(functionarywiseReportRequest.orderDir().equals(Direction.ASC)
                        ? criteriaBuilder.asc(root.get(functionarywiseReportRequest.orderBy()))
                        : criteriaBuilder.desc(root.get(functionarywiseReportRequest.orderBy()))));

        TypedQuery<FunctionarywiseReport> countquery = entityManager.createQuery(countCriteriaQuery);

        return new Page<>(query, functionarywiseReportRequest.pageNumber() + 1,
                functionarywiseReportRequest.pageSize(), countquery.getResultList().size());
    }

    @Override
    public Object[] findGrandTotalByRequest(FunctionarywiseReportRequest functionarywiseReportRequest) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<FunctionarywiseReport> root = criteriaQuery.from(FunctionarywiseReport.class);

        criteriaQuery
                .multiselect(criteriaBuilder.sum(root.get(REGISTERED)), criteriaBuilder.sum(root.get(INPROCESS)),
                        criteriaBuilder.sum(root.get(COMPLETED)), criteriaBuilder.sum(root.get(REOPENED)),
                        criteriaBuilder.sum(root.get(REJECTED)), criteriaBuilder.sum(root.get(WITHINSLA)),
                        criteriaBuilder.sum(root.get(BEYONDSLA)))
                .where(criteria(functionarywiseReportRequest, criteriaBuilder, root).toArray(new Predicate[]{}));
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    @Override
    public List<FunctionarywiseReport> findFunctionarywiseReportByEmployeeId(FunctionarywiseReportRequest functionarywiseReportRequest) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<FunctionarywiseReport> criteriaQuery = criteriaBuilder.createQuery(FunctionarywiseReport.class);
        Root<FunctionarywiseReport> root = criteriaQuery.from(FunctionarywiseReport.class);

        return entityManager.createQuery(generateReportByEmployeeId(criteriaQuery,
                functionarywiseReportRequest, criteriaBuilder, root)).getResultList();
    }

    @Override
    public List<FunctionarywiseReport> findFunctionarywiseReportByRequest(FunctionarywiseReportRequest functionarywiseReportRequest) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<FunctionarywiseReport> criteriaQuery = criteriaBuilder.createQuery(FunctionarywiseReport.class);
        Root<FunctionarywiseReport> root = criteriaQuery.from(FunctionarywiseReport.class);

        return entityManager.createQuery(generateReportByRequest(criteriaQuery,
                functionarywiseReportRequest, criteriaBuilder, root)).getResultList();
    }

    private CriteriaQuery<FunctionarywiseReport> generateReportByEmployeeId(CriteriaQuery<FunctionarywiseReport> criteriaQuery,
                                                                            FunctionarywiseReportRequest functionarywiseReportRequest,
                                                                            CriteriaBuilder criteriaBuilder,
                                                                            Root<FunctionarywiseReport> root) {

        criteriaQuery.multiselect(root.get("complainantId"), root.get("crn"), root.get(CREATED_DATE),
                root.get("complainantName"), root.get("complaintDetail"), root.get(STATUS),
                root.get("boundaryName"), root.get("feedback"), root.get("isSLA"))
                .where(criteria(functionarywiseReportRequest, criteriaBuilder, root).toArray(new Predicate[]{}));
        return criteriaQuery;
    }

    private CriteriaQuery<FunctionarywiseReport> generateReportByRequest(CriteriaQuery<FunctionarywiseReport> criteriaQuery,
                                                                         FunctionarywiseReportRequest functionarywiseReportRequest,
                                                                         CriteriaBuilder criteriaBuilder,
                                                                         Root<FunctionarywiseReport> root) {
        criteriaQuery
                .multiselect(root.get(EMPLOYEE_ID), root.get(EMPLOYEE_NAME),
                        criteriaBuilder.sum(root.get(REGISTERED)), criteriaBuilder.sum(root.get(INPROCESS)),
                        criteriaBuilder.sum(root.get(COMPLETED)), criteriaBuilder.sum(root.get(REOPENED)),
                        criteriaBuilder.sum(root.get(REJECTED)), criteriaBuilder.sum(root.get(WITHINSLA)),
                        criteriaBuilder.sum(root.get(BEYONDSLA)))
                .where(criteria(functionarywiseReportRequest, criteriaBuilder, root).toArray(new Predicate[]{}))
                .groupBy(root.get(EMPLOYEE_ID), root.get(EMPLOYEE_NAME));

        return criteriaQuery;
    }

    private List<Predicate> criteria(FunctionarywiseReportRequest functionarywiseReportRequest,
                                     CriteriaBuilder criteriaBuilder,
                                     Root<FunctionarywiseReport> root) {

        final List<Predicate> predicates = new ArrayList<>();
        if (functionarywiseReportRequest.getFromDate() != null && functionarywiseReportRequest.getToDate() != null)
            predicates.add(criteriaBuilder.between(root.get(CREATED_DATE),
                    DateUtils.startOfDay(functionarywiseReportRequest.getFromDate()),
                    DateUtils.endOfDay(functionarywiseReportRequest.getToDate())));
        if ("lastsevendays".equals(functionarywiseReportRequest.getComplaintDateType()))
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(CREATED_DATE),
                    DateUtils.endOfToday().minusDays(8).toDate()));
        if ("lastthirtydays".equals(functionarywiseReportRequest.getComplaintDateType()))
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(CREATED_DATE),
                    DateUtils.endOfToday().minusDays(31).toDate()));
        if ("lastninetydays".equals(functionarywiseReportRequest.getComplaintDateType()))
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(CREATED_DATE),
                    DateUtils.endOfToday().minusDays(91).toDate()));
        if (functionarywiseReportRequest.getToDate() != null)
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(CREATED_DATE),
                    DateUtils.endOfDay(functionarywiseReportRequest.getToDate())));
        if (functionarywiseReportRequest.getFromDate() != null)
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(CREATED_DATE),
                    DateUtils.startOfDay(functionarywiseReportRequest.getFromDate())));
        if (StringUtils.isNotBlank(functionarywiseReportRequest.getUsrid()))
            predicates.add(criteriaBuilder.equal(root.get(EMPLOYEE_ID), functionarywiseReportRequest.getUsrid()));
        if (StringUtils.isNotBlank(functionarywiseReportRequest.getStatus())) {
            predicates.addAll(predicatesForComplaints(functionarywiseReportRequest, criteriaBuilder, root));
        }
        return predicates;
    }

    private List<Predicate> predicatesForComplaints(FunctionarywiseReportRequest functionarywiseReportRequest,
                                                    CriteriaBuilder criteriaBuilder,
                                                    Root<FunctionarywiseReport> root) {
        final List<Predicate> predicates = new ArrayList<>();
        if (REGISTERED.equalsIgnoreCase(functionarywiseReportRequest.getStatus()))
            predicates.add(criteriaBuilder.equal(root.get(STATUS), "REGISTERED"));
        if (INPROCESS.equalsIgnoreCase(functionarywiseReportRequest.getStatus()))
            predicates.add(root.get(STATUS).in("FORWARDED", "PROCESSING", "NOTCOMPLETED"));
        if (REJECTED.equalsIgnoreCase(functionarywiseReportRequest.getStatus()))
            predicates.add(criteriaBuilder.equal(root.get(STATUS), "REJECTED"));
        if (COMPLETED.equalsIgnoreCase(functionarywiseReportRequest.getStatus()))
            predicates.add(root.get(STATUS).in("WITHDRAWN", "COMPLETED", "CLOSED"));
        if (REOPENED.equalsIgnoreCase(functionarywiseReportRequest.getStatus()))
            predicates.add(criteriaBuilder.equal(root.get(STATUS), "REOPENED"));
        if ("Within SLA".equalsIgnoreCase(functionarywiseReportRequest.getStatus()))
            predicates.add(criteriaBuilder.greaterThan(root.get(WITHINSLA), 0));
        if ("Beyond SLA".equalsIgnoreCase(functionarywiseReportRequest.getStatus()))
            predicates.add(criteriaBuilder.greaterThan(root.get(BEYONDSLA), 0));
        return predicates;
    }
}

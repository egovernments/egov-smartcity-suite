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
import org.egov.pgr.entity.dto.DrillDownReportRequest;
import org.egov.pgr.entity.view.DrillDownReports;
import org.springframework.data.domain.Sort.Direction;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class DrillDownwiseReportRepositoryImpl implements DrillDownwiseReportRepositoryCustom {

    private static final String CREATED_DATE = "createdDate";
    private static final String STATUS = "status";
    private static final String REGISTERED = "registered";
    private static final String INPROCESS = "inprocess";
    private static final String COMPLETED = "completed";
    private static final String REOPENED = "reopened";
    private static final String REJECTED = "rejected";
    private static final String WITHINSLA = "withinSLA";
    private static final String BEYONDSLA = "beyondSLA";
    private static final String PARENTBOUNDARY = "parentBoundary";
    private static final String LOCALITY = "locality";
    private static final String DEPARTMENT = "department";
    private static final String COMPLAINTTYPENAME = "complaintTypeName";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<DrillDownReports> findDrillDownRecords(DrillDownReportRequest reportRequest) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<DrillDownReports> criteriaQuery = criteriaBuilder.createQuery(DrillDownReports.class);
        Root<DrillDownReports> root = criteriaQuery.from(DrillDownReports.class);
        CriteriaQuery<DrillDownReports> recordCountQuery = criteriaBuilder.createQuery(DrillDownReports.class);
        Root<DrillDownReports> countRoot = recordCountQuery.from(DrillDownReports.class);

        Path<String> selection = null;
        if (selectByBoundary(reportRequest, root) != null)
            selection = selectByBoundary(reportRequest, root);
        else
            selection = selectbyDepartment(reportRequest, root);

        recordCountQuery.multiselect(criteriaBuilder.count(selection))
                .where(criteria(reportRequest, criteriaBuilder, countRoot)
                        .toArray(new Predicate[]{})).groupBy(selection);

        CriteriaQuery<DrillDownReports> reportQuery = generateCriteriaToGetRecordsByRequest(reportRequest, criteriaQuery,
                criteriaBuilder, root);
        if (reportRequest.orderBy().equals(PARENTBOUNDARY) || reportRequest.orderBy().equals(LOCALITY) ||
                reportRequest.orderBy().equals(DEPARTMENT) || reportRequest.orderBy().equals(COMPLAINTTYPENAME) ||
                reportRequest.orderBy().equals("employeePosition")) {
            reportQuery.orderBy(reportRequest.orderDir().equals(Direction.ASC)
                    ? criteriaBuilder.asc(root.get(reportRequest.orderBy()))
                    : criteriaBuilder.desc(root.get(reportRequest.orderBy())));
        } else
            reportQuery.orderBy(reportRequest.orderDir().equals(Direction.ASC)
                    ? criteriaBuilder.asc(criteriaBuilder.sum(root.get(reportRequest.orderBy())))
                    : criteriaBuilder.desc(criteriaBuilder.sum(root.get(reportRequest.orderBy()))));

        return new Page<>(entityManager.createQuery(reportQuery), reportRequest.pageNumber() + 1,
                reportRequest.pageSize(), entityManager.createQuery(recordCountQuery).getResultList().size());
    }

    @Override
    public Page<DrillDownReports> findDrillDownRecordsByComplaintTypeId(DrillDownReportRequest reportRequest) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<DrillDownReports> criteriaQuery = criteriaBuilder.createQuery(DrillDownReports.class);
        Root<DrillDownReports> root = criteriaQuery.from(DrillDownReports.class);
        CriteriaQuery<DrillDownReports> countCriteriaQuery = criteriaBuilder.createQuery(DrillDownReports.class);
        Root<DrillDownReports> countRoot = countCriteriaQuery.from(DrillDownReports.class);

        countCriteriaQuery.multiselect(criteriaBuilder.count(countRoot.get("crn")))
                .where(criteria(reportRequest, criteriaBuilder, countRoot)
                        .toArray(new Predicate[]{}))
                .groupBy(countRoot.get("crn"));


        TypedQuery<DrillDownReports> query = entityManager.createQuery(generateCriteriaToGetComplaints(reportRequest, criteriaQuery,
                criteriaBuilder, root)
                .orderBy(reportRequest.orderDir().equals(Direction.ASC)
                        ? criteriaBuilder.asc(root.get(reportRequest.orderBy()))
                        : criteriaBuilder.desc(root.get(reportRequest.orderBy()))));

        TypedQuery<DrillDownReports> countquery = entityManager.createQuery(countCriteriaQuery);

        return new Page<>(query, reportRequest.pageNumber() + 1,
                reportRequest.pageSize(), countquery.getResultList().size());
    }

    @Override
    public Object[] findDrillDownGrandTotal(DrillDownReportRequest reportRequest) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<DrillDownReports> root = criteriaQuery.from(DrillDownReports.class);

        criteriaQuery
                .multiselect(criteriaBuilder.sum(root.get(REGISTERED)), criteriaBuilder.sum(root.get(INPROCESS)),
                        criteriaBuilder.sum(root.get(COMPLETED)), criteriaBuilder.sum(root.get(REJECTED)),
                        criteriaBuilder.sum(root.get(REOPENED)), criteriaBuilder.sum(root.get(WITHINSLA)),
                        criteriaBuilder.sum(root.get(BEYONDSLA)))
                .where(criteria(reportRequest, criteriaBuilder, root).toArray(new Predicate[]{}));
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    @Override
    public List<DrillDownReports> findDrillDownRecordsByRequest(DrillDownReportRequest reportRequest) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<DrillDownReports> criteriaQuery = criteriaBuilder.createQuery(DrillDownReports.class);
        Root<DrillDownReports> root = criteriaQuery.from(DrillDownReports.class);

        return entityManager.createQuery(generateCriteriaToGetComplaints(reportRequest, criteriaQuery, criteriaBuilder, root))
                .getResultList();
    }

    @Override
    public List<DrillDownReports> findDrillDownRecordList(DrillDownReportRequest reportRequest) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<DrillDownReports> criteriaQuery = criteriaBuilder.createQuery(DrillDownReports.class);
        Root<DrillDownReports> root = criteriaQuery.from(DrillDownReports.class);

        return entityManager.createQuery(generateCriteriaToGetRecordsByRequest(reportRequest, criteriaQuery, criteriaBuilder, root))
                .getResultList();
    }

    private CriteriaQuery<DrillDownReports> generateCriteriaToGetComplaints(DrillDownReportRequest reportRequest,
                                                                            CriteriaQuery<DrillDownReports> criteriaQuery,
                                                                            CriteriaBuilder criteriaBuilder,
                                                                            Root<DrillDownReports> root) {
        criteriaQuery.multiselect(root.get("complainantId"), root.get("crn"), root.get(CREATED_DATE),
                root.get("complainantName"), root.get("complaintDetail"), root.get(STATUS),
                root.get("boundaryName"), root.get("feedback"), root.get("isSLA"))
                .where(criteria(reportRequest, criteriaBuilder, root).toArray(new Predicate[]{}));
        return criteriaQuery;
    }

    private CriteriaQuery<DrillDownReports> generateCriteriaToGetRecordsByRequest(DrillDownReportRequest reportRequest,
                                                                                  CriteriaQuery<DrillDownReports> criteriaQuery,
                                                                                  CriteriaBuilder criteriaBuilder,
                                                                                  Root<DrillDownReports> root) {
        Path<String> selection = null;
        if ("ByBoundary".equals(reportRequest.getGroupBy()))
            selection = selectByBoundary(reportRequest, root);
        else
            selection = selectbyDepartment(reportRequest, root);
        criteriaQuery.multiselect(selection,
                criteriaBuilder.sum(root.get(REGISTERED)), criteriaBuilder.sum(root.get(INPROCESS)),
                criteriaBuilder.sum(root.get(COMPLETED)), criteriaBuilder.sum(root.get(REJECTED)),
                criteriaBuilder.sum(root.get(REOPENED)), criteriaBuilder.sum(root.get(WITHINSLA)),
                criteriaBuilder.sum(root.get(BEYONDSLA)))
                .where(criteria(reportRequest, criteriaBuilder, root).toArray(new Predicate[]{}))
                .groupBy(selection);

        return criteriaQuery;
    }

    private List<Predicate> criteria(DrillDownReportRequest reportRequest, CriteriaBuilder criteriaBuilder,
                                     Root<DrillDownReports> root) {

        final List<Predicate> predicates = new ArrayList<>();
        if (reportRequest.getFromDate() != null && reportRequest.getToDate() != null)
            predicates.add(criteriaBuilder.between(root.get(CREATED_DATE),
                    DateUtils.startOfDay(reportRequest.getFromDate()),
                    DateUtils.endOfDay(reportRequest.getToDate())));
        if ("lastsevendays".equals(reportRequest.getComplaintDateType()))
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(CREATED_DATE),
                    DateUtils.endOfToday().minusDays(8).toDate()));
        if ("lastthirtydays".equals(reportRequest.getComplaintDateType()))
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(CREATED_DATE),
                    DateUtils.endOfToday().minusDays(31).toDate()));
        if ("lastninetydays".equals(reportRequest.getComplaintDateType()))
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(CREATED_DATE),
                    DateUtils.endOfToday().minusDays(91).toDate()));
        if (reportRequest.getToDate() != null && reportRequest.getFromDate() == null)
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(CREATED_DATE),
                    DateUtils.endOfDay(reportRequest.getToDate())));
        if (reportRequest.getFromDate() != null && reportRequest.getToDate() == null)
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(CREATED_DATE),
                    DateUtils.startOfDay(reportRequest.getFromDate())));
        if (StringUtils.isNotBlank(reportRequest.getBoundary()))
            predicates.add(criteriaBuilder.equal(root.get(PARENTBOUNDARY), reportRequest.getBoundary()));
        if (StringUtils.isNotBlank(reportRequest.getDeptid()))
            predicates.add(criteriaBuilder.equal(root.get(DEPARTMENT), reportRequest.getDeptid()));
        if (StringUtils.isNotBlank(reportRequest.getLocality()))
            predicates.add(criteriaBuilder.equal(root.get(LOCALITY), reportRequest.getLocality()));
        if (StringUtils.isNotBlank(reportRequest.getComplainttypeid()))
            predicates.add(criteriaBuilder.equal(root.get(COMPLAINTTYPENAME), reportRequest.getComplainttypeid()));
        if (StringUtils.isNotBlank(reportRequest.getSelecteduserid())) {
            String userName = reportRequest.getSelecteduserid().split("~")[0];
            predicates.add(criteriaBuilder.equal(root.get("employeeName"), userName));
        }
        return predicates;
    }

    private Path<String> selectByBoundary(DrillDownReportRequest reportRequest, Root<DrillDownReports> root) {

        Path<String> selection = null;
        if (StringUtils.isNotBlank(reportRequest.getBoundary())) {
            if (StringUtils.isNotBlank(reportRequest.getLocality())) {
                selection = selectbyDepartment(reportRequest, root);
            } else
                selection = root.get(LOCALITY);
        } else if ("ByBoundary".equals(reportRequest.getGroupBy()))
            selection = root.get(PARENTBOUNDARY);

        return selection;
    }

    private Path<String> selectbyDepartment(DrillDownReportRequest reportRequest, Root<DrillDownReports> root) {
        Path<String> selection;
        if (StringUtils.isNotBlank(reportRequest.getDeptid())) {
            if (StringUtils.isNotBlank(reportRequest.getComplainttypeid()))
                selection = root.get("employeePosition");
            else
                selection = root.get(COMPLAINTTYPENAME);
        } else
            selection = root.get(DEPARTMENT);
        return selection;
    }

}

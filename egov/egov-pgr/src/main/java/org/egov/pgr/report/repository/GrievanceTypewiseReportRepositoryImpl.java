/*
 * eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) <2017>  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 * 	Further, all user interfaces, including but not limited to citizen facing interfaces,
 *         Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *         derived works should carry eGovernments Foundation logo on the top right corner.
 *
 * 	For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 * 	For any further queries on attribution, including queries on brand guidelines,
 *         please contact contact@egovernments.org
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.pgr.report.repository;

import org.egov.infra.utils.DateUtils;
import org.egov.infra.utils.StringUtils;
import org.egov.infra.persistence.utils.Page;
import org.egov.pgr.report.entity.contract.DrilldownReportRequest;
import org.egov.pgr.report.entity.view.DrilldownReportView;
import org.springframework.data.domain.Sort.Direction;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class GrievanceTypewiseReportRepositoryImpl implements GrievanceTypewiseReportRepositoryCustom {

    private static final String CREATED_DATE = "createdDate";
    private static final String STATUS = "status";
    private static final String REGISTERED = "registered";
    private static final String INPROCESS = "inprocess";
    private static final String COMPLETED = "completed";
    private static final String REOPENED = "reopened";
    private static final String REJECTED = "rejected";
    private static final String WITHINSLA = "withinSLA";
    private static final String BEYONDSLA = "beyondSLA";
    private static final String COMPLAINTTYPEID = "complaintTypeId";
    private static final String COMPLAINTTYPENAME = "complaintTypeName";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<DrilldownReportView> findGrievanceTypewiseRecord(DrilldownReportRequest reportRequest) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<DrilldownReportView> criteriaQuery = criteriaBuilder.createQuery(DrilldownReportView.class);
        Root<DrilldownReportView> root = criteriaQuery.from(DrilldownReportView.class);
        CriteriaQuery<DrilldownReportView> recordCountQuery = criteriaBuilder.createQuery(DrilldownReportView.class);
        Root<DrilldownReportView> countRoot = recordCountQuery.from(DrilldownReportView.class);

        recordCountQuery.multiselect(criteriaBuilder.count(countRoot))
                .where(criteria(reportRequest, criteriaBuilder, countRoot)
                        .toArray(new Predicate[]{}))
                .groupBy(countRoot.get(COMPLAINTTYPEID), countRoot.get(COMPLAINTTYPENAME));

        CriteriaQuery<DrilldownReportView> reportQuery = criteriaToGetRecords(reportRequest, criteriaQuery, criteriaBuilder, root);

        if (reportRequest.orderBy().equals(COMPLAINTTYPENAME))
            reportQuery.orderBy(reportRequest.orderDir().equals(Direction.ASC)
                    ? criteriaBuilder.asc(root.get(reportRequest.orderBy()))
                    : criteriaBuilder.desc(root.get(reportRequest.orderBy())));
        else
            reportQuery.orderBy(reportRequest.orderDir().equals(Direction.ASC)
                    ? criteriaBuilder.asc(criteriaBuilder.sum(root.get(reportRequest.orderBy())))
                    : criteriaBuilder.desc(criteriaBuilder.sum(root.get(reportRequest.orderBy()))));

        return new Page<>(entityManager.createQuery(reportQuery), reportRequest.pageNumber() + 1,
                reportRequest.pageSize(), entityManager.createQuery(recordCountQuery).getResultList().size());
    }

    @Override
    public Page<DrilldownReportView> findGrievanceTypewiseRecordsByComplaintId(DrilldownReportRequest reportRequest) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<DrilldownReportView> criteriaQuery = criteriaBuilder.createQuery(DrilldownReportView.class);
        Root<DrilldownReportView> root = criteriaQuery.from(DrilldownReportView.class);
        CriteriaQuery<DrilldownReportView> countCriteriaQuery = criteriaBuilder.createQuery(DrilldownReportView.class);
        Root<DrilldownReportView> countRoot = countCriteriaQuery.from(DrilldownReportView.class);

        countCriteriaQuery.multiselect(criteriaBuilder.count(countRoot.get("crn")))
                .where(criteria(reportRequest, criteriaBuilder, countRoot)
                        .toArray(new Predicate[]{}))
                .groupBy(countRoot.get("crn"));

        CriteriaQuery<DrilldownReportView> reportQuery = getCriteriaForComplaints(reportRequest, criteriaQuery, criteriaBuilder, root);

        reportQuery.orderBy(reportRequest.orderDir().equals(Direction.ASC)
                ? criteriaBuilder.asc(root.get(reportRequest.orderBy()))
                : criteriaBuilder.desc(root.get(reportRequest.orderBy())));

        return new Page<>(entityManager.createQuery(reportQuery), reportRequest.pageNumber() + 1,
                reportRequest.pageSize(), entityManager.createQuery(countCriteriaQuery).getResultList().size());
    }

    @Override
    public Object[] findGrandTotal(DrilldownReportRequest reportRequest) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<DrilldownReportView> root = criteriaQuery.from(DrilldownReportView.class);

        criteriaQuery
                .multiselect(criteriaBuilder.sum(root.get(REGISTERED)), criteriaBuilder.sum(root.get(INPROCESS)),
                        criteriaBuilder.sum(root.get(COMPLETED)), criteriaBuilder.sum(root.get(REJECTED)),
                        criteriaBuilder.sum(root.get(REOPENED)), criteriaBuilder.sum(root.get(WITHINSLA)),
                        criteriaBuilder.sum(root.get(BEYONDSLA)))
                .where(criteria(reportRequest, criteriaBuilder, root).toArray(new Predicate[]{}));
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    @Override
    public List<DrilldownReportView> findGrievanceTypewiseRecordlistByComplaintId(DrilldownReportRequest reportRequest) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<DrilldownReportView> criteriaQuery = criteriaBuilder.createQuery(DrilldownReportView.class);
        Root<DrilldownReportView> root = criteriaQuery.from(DrilldownReportView.class);

        return entityManager.createQuery(getCriteriaForComplaints(reportRequest, criteriaQuery, criteriaBuilder, root))
                .getResultList();
    }

    @Override
    public List<DrilldownReportView> findGrievanceTypewiseRecordList(DrilldownReportRequest reportRequest) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<DrilldownReportView> criteriaQuery = criteriaBuilder.createQuery(DrilldownReportView.class);
        Root<DrilldownReportView> root = criteriaQuery.from(DrilldownReportView.class);

        return entityManager.createQuery(criteriaToGetRecords(reportRequest, criteriaQuery, criteriaBuilder, root))
                .getResultList();
    }

    private CriteriaQuery<DrilldownReportView> getCriteriaForComplaints(DrilldownReportRequest reportRequest,
                                                                        CriteriaQuery<DrilldownReportView> criteriaQuery,
                                                                        CriteriaBuilder criteriaBuilder,
                                                                        Root<DrilldownReportView> root) {
        criteriaQuery.multiselect(root.get("complainantId"), root.get("crn"), root.get(CREATED_DATE),
                root.get("complainantName"), root.get("complaintDetail"), root.get(STATUS),
                root.get("boundaryName"), root.get("feedback"), root.get("isSLA"))
                .where(criteria(reportRequest, criteriaBuilder, root).toArray(new Predicate[]{}));
        return criteriaQuery;
    }

    private CriteriaQuery<DrilldownReportView> criteriaToGetRecords(DrilldownReportRequest reportRequest,
                                                                    CriteriaQuery<DrilldownReportView> criteriaQuery,
                                                                    CriteriaBuilder criteriaBuilder,
                                                                    Root<DrilldownReportView> root) {
        criteriaQuery
                .multiselect(root.get(COMPLAINTTYPENAME), root.get(COMPLAINTTYPEID),
                        criteriaBuilder.sum(root.get(REGISTERED)), criteriaBuilder.sum(root.get(INPROCESS)),
                        criteriaBuilder.sum(root.get(COMPLETED)), criteriaBuilder.sum(root.get(REJECTED)),
                        criteriaBuilder.sum(root.get(REOPENED)), criteriaBuilder.sum(root.get(WITHINSLA)),
                        criteriaBuilder.sum(root.get(BEYONDSLA)))
                .where(criteria(reportRequest, criteriaBuilder, root).toArray(new Predicate[]{}))
                .groupBy(root.get(COMPLAINTTYPEID), root.get(COMPLAINTTYPENAME));
        return criteriaQuery;
    }

    private List<Predicate> criteria(DrilldownReportRequest reportRequest, CriteriaBuilder criteriaBuilder,
                                     Root<DrilldownReportView> root) {

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
        if (StringUtils.isNotBlank(reportRequest.getComplaintType()))
            predicates.add(criteriaBuilder.equal(root.get(COMPLAINTTYPEID), reportRequest.getComplaintType()));
        if (StringUtils.isNotBlank(reportRequest.getComplaintTypeWithStatus()) &&
                StringUtils.isNotBlank(reportRequest.getStatus()))
            predicates.add(criteriaBuilder.equal(root.get(COMPLAINTTYPEID), reportRequest.getComplaintTypeWithStatus()));
        if (StringUtils.isNotBlank(reportRequest.getStatus())) {
            predicates.addAll(predicatesForComplaints(reportRequest, criteriaBuilder, root));
        }
        return predicates;
    }

    private List<Predicate> predicatesForComplaints(DrilldownReportRequest reportRequest, CriteriaBuilder criteriaBuilder,
                                                    Root<DrilldownReportView> root) {
        final List<Predicate> predicates = new ArrayList<>();
        if (REGISTERED.equalsIgnoreCase(reportRequest.getStatus()))
            predicates.add(criteriaBuilder.equal(root.get(STATUS), "REGISTERED"));
        if (INPROCESS.equalsIgnoreCase(reportRequest.getStatus()))
            predicates.add(root.get(STATUS).in("FORWARDED", "PROCESSING", "NOTCOMPLETED"));
        if (REJECTED.equalsIgnoreCase(reportRequest.getStatus()))
            predicates.add(criteriaBuilder.equal(root.get(STATUS), "REJECTED"));
        if (COMPLETED.equalsIgnoreCase(reportRequest.getStatus()))
            predicates.add(root.get(STATUS).in("WITHDRAWN", "COMPLETED", "CLOSED"));
        if (REOPENED.equalsIgnoreCase(reportRequest.getStatus()))
            predicates.add(criteriaBuilder.equal(root.get(STATUS), "REOPENED"));
        if ("Within SLA".equalsIgnoreCase(reportRequest.getStatus()))
            predicates.add(criteriaBuilder.greaterThan(root.get(WITHINSLA), 0));
        if ("Beyond SLA".equalsIgnoreCase(reportRequest.getStatus()))
            predicates.add(criteriaBuilder.greaterThan(root.get(BEYONDSLA), 0));
        return predicates;
    }
}

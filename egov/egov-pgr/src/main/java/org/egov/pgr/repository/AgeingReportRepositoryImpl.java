/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.egov.infra.utils.DateUtils;
import org.egov.infstr.services.Page;
import org.egov.pgr.entity.dto.AgeingReportForm;
import org.egov.pgr.entity.dto.AgeingReportRequest;
import org.springframework.data.domain.Sort.Direction;

public class AgeingReportRepositoryImpl implements AgeingReportRepositoryCustom {

    private static final String COMPLETED = "Completed";
    private static final String STATUS = "status";
    private static final String CREATED_DATE = "createddate";
    private static final String GREATER30 = "greater30";
    private static final String BETWEEN10_TO_30 = "btw10to30";
    private static final String BETWEEN5_TO_10 = "btw5to10";
    private static final String BETWEEN2_TO_5 = "btw2to5";
    private static final String LESSTHAN2 = "lsthn2";
    private static final String BOUNDARY = "boundary";
    private static final String DEPARTMENT = "department";
    private static final String BYDEPARTMENT = "ByDepartment";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<AgeingReportForm> findByAgeingReportResult(final AgeingReportRequest ageingReportRequest) {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<AgeingReportForm> criteriaQuery = criteriaBuilder.createQuery(AgeingReportForm.class);
        final CriteriaQuery<AgeingReportForm> countCriteriaQuery = criteriaBuilder.createQuery(AgeingReportForm.class);
        final Root<AgeingReportForm> root = criteriaQuery.from(AgeingReportForm.class);
        final Root<AgeingReportForm> countRoot = countCriteriaQuery.from(AgeingReportForm.class);
        CriteriaQuery<AgeingReportForm> searchQuery;
        if (BYDEPARTMENT.equals(ageingReportRequest.getMode()))
            searchQuery = findByDepartment(criteriaQuery, criteriaBuilder, root, ageingReportRequest);
        else
            searchQuery = findByBoundary(criteriaQuery, criteriaBuilder, root, ageingReportRequest);

        final TypedQuery<AgeingReportForm> query = entityManager.createQuery(searchQuery);

        countCriteriaQuery.multiselect(criteriaBuilder.count(countRoot))
                .where(predicatecondition(ageingReportRequest, criteriaBuilder, countRoot).toArray(new Predicate[] {}));

        if (BYDEPARTMENT.equals(ageingReportRequest.getMode()))
            countCriteriaQuery.groupBy(countRoot.get(DEPARTMENT));
        else
            countCriteriaQuery.groupBy(countRoot.get(BOUNDARY));

        final TypedQuery<AgeingReportForm> countquery = entityManager.createQuery(countCriteriaQuery);
        final int recordTotal = countquery.getResultList().size();

        return new Page<>(query, ageingReportRequest.pageNumber() + 1, ageingReportRequest.pageSize(), recordTotal);
    }

    @Override
    public List<AgeingReportForm> getAgeingReportRecords(final AgeingReportRequest ageingReportRequest) {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<AgeingReportForm> listCriteriaQuery = criteriaBuilder.createQuery(AgeingReportForm.class);
        final Root<AgeingReportForm> rootList = listCriteriaQuery.from(AgeingReportForm.class);
        CriteriaQuery<AgeingReportForm> searchQuery;
        if (BYDEPARTMENT.equals(ageingReportRequest.getMode()))
            searchQuery = findByDepartment(listCriteriaQuery, criteriaBuilder, rootList, ageingReportRequest);
        else
            searchQuery = findByBoundary(listCriteriaQuery, criteriaBuilder, rootList, ageingReportRequest);

        return entityManager.createQuery(searchQuery).getResultList();
    }

    @Override
    public Object[] getGrandTotal(final AgeingReportRequest ageingReportRequest) {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        final Root<AgeingReportForm> root = criteriaQuery.from(AgeingReportForm.class);
        criteriaQuery
                .multiselect(
                        criteriaBuilder.sum(root.get(GREATER30)),
                        criteriaBuilder.sum(root.get(BETWEEN10_TO_30)),
                        criteriaBuilder.sum(root.get(BETWEEN5_TO_10)),
                        criteriaBuilder.sum(root.get(BETWEEN2_TO_5)),
                        criteriaBuilder.sum(root.get(LESSTHAN2)),
                        criteriaBuilder
                                .sum(criteriaBuilder.sum(root.get(GREATER30), criteriaBuilder.sum(root.get(BETWEEN10_TO_30),
                                        criteriaBuilder.sum(root.get(BETWEEN5_TO_10),
                                                criteriaBuilder.sum(root.get(BETWEEN2_TO_5), root.get(LESSTHAN2)))))))
                .where(predicatecondition(ageingReportRequest, criteriaBuilder, root).toArray(new Predicate[] {}));
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    private CriteriaQuery<AgeingReportForm> findByDepartment(final CriteriaQuery<AgeingReportForm> criteriaQuery,
            final CriteriaBuilder criteriaBuilder,
            final Root<AgeingReportForm> root, final AgeingReportRequest ageingReportRequest) {
        criteriaQuery
                .multiselect(root.get(DEPARTMENT).alias("name"),
                        criteriaBuilder.sum(root.get(GREATER30)),
                        criteriaBuilder.sum(root.get(BETWEEN10_TO_30)),
                        criteriaBuilder.sum(root.get(BETWEEN5_TO_10)),
                        criteriaBuilder.sum(root.get(BETWEEN2_TO_5)),
                        criteriaBuilder.sum(root.get(LESSTHAN2)))
                .where(predicatecondition(ageingReportRequest, criteriaBuilder, root).toArray(new Predicate[] {}))
                .groupBy(root.get(DEPARTMENT));
        return queryWithOrderBy(criteriaQuery, criteriaBuilder, root, ageingReportRequest);
    }

    private CriteriaQuery<AgeingReportForm> findByBoundary(final CriteriaQuery<AgeingReportForm> criteriaQuery,
            final CriteriaBuilder criteriaBuilder,
            final Root<AgeingReportForm> root, final AgeingReportRequest ageingReportRequest) {
        criteriaQuery
                .multiselect(root.get(BOUNDARY).alias("name"),
                        criteriaBuilder.sum(root.get(GREATER30)),
                        criteriaBuilder.sum(root.get(BETWEEN10_TO_30)),
                        criteriaBuilder.sum(root.get(BETWEEN5_TO_10)),
                        criteriaBuilder.sum(root.get(BETWEEN2_TO_5)),
                        criteriaBuilder.sum(root.get(LESSTHAN2)))
                .where(predicatecondition(ageingReportRequest, criteriaBuilder, root).toArray(new Predicate[] {}))
                .groupBy(root.get(BOUNDARY));

        return queryWithOrderBy(criteriaQuery, criteriaBuilder, root, ageingReportRequest);
    }

    private List<Predicate> predicatecondition(final AgeingReportRequest ageingReportRequest,
            final CriteriaBuilder criteriaBuilder,
            final Root<AgeingReportForm> root) {
        final List<Predicate> predicates = new ArrayList<>();
        if (ageingReportRequest.getStatus().equals(COMPLETED))
            predicates.add(criteriaBuilder.or(criteriaBuilder.like(root.get(STATUS), "COMPLETED"),
                    criteriaBuilder.like(root.get(STATUS), "WITHDRAWN"),
                    criteriaBuilder.like(root.get(STATUS), "CLOSED")));
        if ("Pending".equals(ageingReportRequest.getStatus()))
            predicates.add(criteriaBuilder.or(criteriaBuilder.like(root.get(STATUS), "REGISTERED"),
                    criteriaBuilder.like(root.get(STATUS), "FORWARDED"),
                    criteriaBuilder.like(root.get(STATUS), "PROCESSING"),
                    criteriaBuilder.like(root.get(STATUS), "REOPENED")));
        if ("Rejected".equals(ageingReportRequest.getStatus()))
            predicates.add(criteriaBuilder.or(criteriaBuilder.like(root.get(STATUS), "REJECTED")));
        if (ageingReportRequest.getFromDate() != null && ageingReportRequest.getToDate() != null)
            predicates.add(criteriaBuilder.between(root.get(CREATED_DATE),
                    DateUtils.startOfDay(ageingReportRequest.getFromDate()),
                    DateUtils.endOfDay(ageingReportRequest.getToDate())));
        if ("lastsevendays".equals(ageingReportRequest.getComplaintDateType()))
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(CREATED_DATE),
                    DateUtils.endOfToday().minusDays(7).toDate()));
        if ("lastthirtydays".equals(ageingReportRequest.getComplaintDateType()))
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(CREATED_DATE),
                    DateUtils.endOfToday().minusDays(30).toDate()));
        if ("lastninetydays".equals(ageingReportRequest.getComplaintDateType()))
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(CREATED_DATE),
                    DateUtils.endOfToday().minusDays(90).toDate()));
        if (ageingReportRequest.getToDate() != null)
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(CREATED_DATE),
                    DateUtils.endOfDay(ageingReportRequest.getToDate())));
        return predicates;
    }

    private CriteriaQuery<AgeingReportForm> queryWithOrderBy(final CriteriaQuery<AgeingReportForm> criteriaQuery,
            final CriteriaBuilder criteriaBuilder, final Root<AgeingReportForm> root,
            final AgeingReportRequest ageingReportRequest) {
        if (ageingReportRequest.orderBy().equals(BOUNDARY) || ageingReportRequest.orderBy().equals(DEPARTMENT))
            criteriaQuery.orderBy(ageingReportRequest.orderDir().equals(Direction.ASC)
                    ? criteriaBuilder.asc(root.get(ageingReportRequest.orderBy()))
                    : criteriaBuilder.desc(root.get(ageingReportRequest.orderBy())));
        else
            criteriaQuery.orderBy(ageingReportRequest.orderDir().equals(Direction.ASC)
                    ? criteriaBuilder.asc(criteriaBuilder.sum(root.get(ageingReportRequest.orderBy())))
                    : criteriaBuilder.desc(criteriaBuilder.sum(root.get(ageingReportRequest.orderBy()))));
        return criteriaQuery;
    }

}

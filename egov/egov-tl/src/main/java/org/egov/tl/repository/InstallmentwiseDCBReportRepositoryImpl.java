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

package org.egov.tl.repository;

import org.egov.infra.persistence.utils.Page;
import org.egov.tl.entity.contracts.InstallmentWiseDCBRequest;
import org.egov.tl.entity.view.LicenseInstallmentwiseDCBReportView;
import org.springframework.data.domain.Sort.Direction;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InstallmentwiseDCBReportRepositoryImpl implements InstallmentwiseDCBReportRepositoryCustom {

    public static final String ACTIVE = "active";
    public static final String LICENSEID = "licenseId";
    private static final String INSTALLMENT = "installment";
    private static final String CURRENTDEMAND = "currentDemand";
    private static final String LICENSENUMBER = "licenseNumber";
    private static final String CURRENTCOLLECTION = "currentCollection";
    private static final String CURRENTBALANCE = "currentBalance";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<LicenseInstallmentwiseDCBReportView> findByInstallmentWiseDCB(InstallmentWiseDCBRequest installmentWiseDCBRequest,
                                                                              Date financialYearStartDate) {

        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<LicenseInstallmentwiseDCBReportView> countCriteriaQuery = criteriaBuilder
                .createQuery(LicenseInstallmentwiseDCBReportView.class);
        final Root<LicenseInstallmentwiseDCBReportView> countRoot = countCriteriaQuery.from(LicenseInstallmentwiseDCBReportView.class);

        final TypedQuery<LicenseInstallmentwiseDCBReportView> query = entityManager
                .createQuery(generateReport(criteriaBuilder, installmentWiseDCBRequest, financialYearStartDate, true));

        countCriteriaQuery.multiselect(criteriaBuilder.count(countRoot))
                .where(predicatecondition(criteriaBuilder, countRoot, installmentWiseDCBRequest, financialYearStartDate)
                        .toArray(new Predicate[]{}))
                .groupBy(countRoot.get(LICENSEID));

        final TypedQuery<LicenseInstallmentwiseDCBReportView> countquery = entityManager.createQuery(countCriteriaQuery);
        final int recordTotal = countquery.getResultList().size();

        return new Page<>(query, installmentWiseDCBRequest.pageNumber() + 1, installmentWiseDCBRequest.pageSize(), recordTotal);
    }

    @Override
    public List<LicenseInstallmentwiseDCBReportView> findInstallmentWiseReport(final InstallmentWiseDCBRequest installmentWiseDCBRequest,
                                                                               final Date financialYearStartDate) {

        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        return entityManager.createQuery(generateReport(criteriaBuilder, installmentWiseDCBRequest, financialYearStartDate, false))
                .getResultList();
    }

    @Override
    public Object[] findReportTotal(final InstallmentWiseDCBRequest installmentWiseDCBRequest,
                                    final Date financialYearStartDate) {

        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        final Root<LicenseInstallmentwiseDCBReportView> root = criteriaQuery.from(LicenseInstallmentwiseDCBReportView.class);

        criteriaQuery.multiselect(
                criteriaBuilder.coalesce(criteriaBuilder.sum(criteriaBuilder.<Number>selectCase()
                                .when(criteriaBuilder.lessThan(root.get(INSTALLMENT), financialYearStartDate),
                                        root.get(CURRENTDEMAND))
                                .otherwise(0.0)),
                        0).alias("arrearDemand"),
                criteriaBuilder.coalesce(criteriaBuilder.sum(criteriaBuilder.<Number>selectCase()
                                .when(criteriaBuilder.equal(root.get(INSTALLMENT), financialYearStartDate),
                                        root.get(CURRENTDEMAND))
                                .otherwise(0.0)),
                        0).alias(CURRENTDEMAND),

                criteriaBuilder.sum(criteriaBuilder.coalesce(criteriaBuilder.sum(criteriaBuilder.<Number>selectCase()
                                .when(criteriaBuilder.lessThan(root.get(INSTALLMENT), financialYearStartDate),
                                        root.get(CURRENTDEMAND))
                                .otherwise(0.0)),
                        0),
                        criteriaBuilder.coalesce(criteriaBuilder.sum(criteriaBuilder.<Number>selectCase()
                                        .when(criteriaBuilder.equal(root.get(INSTALLMENT), financialYearStartDate),
                                                root.get(CURRENTDEMAND))
                                        .otherwise(0.0)),
                                0))
                        .alias("totalDemand"),

                criteriaBuilder.coalesce(criteriaBuilder.sum(criteriaBuilder.<Number>selectCase()
                                .when(criteriaBuilder.lessThan(root.get(INSTALLMENT), financialYearStartDate),
                                        root.get(CURRENTCOLLECTION))
                                .otherwise(0.0)),
                        0).alias("arrearCollection"),

                criteriaBuilder.coalesce(criteriaBuilder.sum(criteriaBuilder.<Number>selectCase()
                                .when(criteriaBuilder.equal(root.get(INSTALLMENT), financialYearStartDate),
                                        root.get(CURRENTCOLLECTION))
                                .otherwise(0.0)),
                        0).alias(CURRENTCOLLECTION),

                criteriaBuilder.sum(criteriaBuilder.coalesce(criteriaBuilder.sum(criteriaBuilder.<Number>selectCase()
                                .when(criteriaBuilder.lessThan(root.get(INSTALLMENT), financialYearStartDate),
                                        root.get(CURRENTCOLLECTION))
                                .otherwise(0.0)),
                        0),
                        criteriaBuilder.coalesce(criteriaBuilder.sum(criteriaBuilder.<Number>selectCase()
                                        .when(criteriaBuilder.equal(root.get(INSTALLMENT), financialYearStartDate),
                                                root.get(CURRENTCOLLECTION))
                                        .otherwise(0.0)),
                                0)),

                criteriaBuilder.coalesce(criteriaBuilder.sum(criteriaBuilder.<Number>selectCase()
                                .when(criteriaBuilder.lessThan(root.get(INSTALLMENT), financialYearStartDate),
                                        root.get(CURRENTBALANCE))
                                .otherwise(0.0)),
                        0).alias("arrearBalance"),

                criteriaBuilder.coalesce(criteriaBuilder.sum(criteriaBuilder.<Number>selectCase()
                                .when(criteriaBuilder.equal(root.get(INSTALLMENT), financialYearStartDate),
                                        root.get(CURRENTBALANCE))
                                .otherwise(0.0)),
                        0).alias(CURRENTBALANCE),

                criteriaBuilder.sum(criteriaBuilder.coalesce(criteriaBuilder.sum(criteriaBuilder.<Number>selectCase()
                                .when(criteriaBuilder.lessThan(root.get(INSTALLMENT), financialYearStartDate),
                                        root.get(CURRENTBALANCE))
                                .otherwise(0.0)),
                        0),
                        criteriaBuilder.coalesce(criteriaBuilder.sum(criteriaBuilder.<Number>selectCase()
                                        .when(criteriaBuilder.equal(root.get(INSTALLMENT), financialYearStartDate),
                                                root.get(CURRENTBALANCE))
                                        .otherwise(0.0)),
                                0)))
                .where(predicatecondition(criteriaBuilder, root, installmentWiseDCBRequest, financialYearStartDate)
                        .toArray(new Predicate[]{}));
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    public CriteriaQuery<LicenseInstallmentwiseDCBReportView> generateReport(CriteriaBuilder criteriaBuilder,
                                                                             InstallmentWiseDCBRequest installmentWiseDCBRequest,
                                                                             Date financialYearStartDate,
                                                                             Boolean fuzzyLogic) {
        final CriteriaQuery<LicenseInstallmentwiseDCBReportView> criteriaQuery = criteriaBuilder.createQuery(LicenseInstallmentwiseDCBReportView.class);
        final Root<LicenseInstallmentwiseDCBReportView> root = criteriaQuery.from(LicenseInstallmentwiseDCBReportView.class);

        criteriaQuery.multiselect(root.get(LICENSEID), root.get(LICENSENUMBER), root.get(ACTIVE),
                criteriaBuilder.coalesce(criteriaBuilder.sum(criteriaBuilder.<Number>selectCase()
                                .when(criteriaBuilder.equal(root.get(INSTALLMENT), financialYearStartDate),
                                        root.get(CURRENTDEMAND))
                                .otherwise(0.0)),
                        0).alias(CURRENTDEMAND),
                criteriaBuilder.coalesce(criteriaBuilder.sum(criteriaBuilder.<Number>selectCase()
                                .when(criteriaBuilder.equal(root.get(INSTALLMENT), financialYearStartDate),
                                        root.get(CURRENTCOLLECTION))
                                .otherwise(0.0)),
                        0).alias(CURRENTCOLLECTION),
                criteriaBuilder.coalesce(criteriaBuilder.sum(criteriaBuilder.<Number>selectCase()
                                .when(criteriaBuilder.equal(root.get(INSTALLMENT), financialYearStartDate),
                                        root.get(CURRENTBALANCE))
                                .otherwise(0.0)),
                        0).alias(CURRENTBALANCE),
                criteriaBuilder.coalesce(criteriaBuilder.sum(criteriaBuilder.<Number>selectCase()
                                .when(criteriaBuilder.lessThan(root.get(INSTALLMENT), financialYearStartDate),
                                        root.get(CURRENTDEMAND))
                                .otherwise(0.0)),
                        0).alias("arrearDemand"),
                criteriaBuilder.coalesce(criteriaBuilder.sum(criteriaBuilder.<Number>selectCase()
                                .when(criteriaBuilder.lessThan(root.get(INSTALLMENT), financialYearStartDate),
                                        root.get(CURRENTCOLLECTION))
                                .otherwise(0.0)),
                        0).alias("arrearCollection"),
                criteriaBuilder.coalesce(criteriaBuilder.sum(criteriaBuilder.<Number>selectCase()
                                .when(criteriaBuilder.lessThan(root.get(INSTALLMENT), financialYearStartDate),
                                        root.get(CURRENTBALANCE))
                                .otherwise(0.0)),
                        0).alias("arrearBalance"))
                .where(predicatecondition(criteriaBuilder, root, installmentWiseDCBRequest, financialYearStartDate)
                        .toArray(new Predicate[]{}))
                .groupBy(root.get(LICENSEID), root.get(LICENSENUMBER), root.get(ACTIVE));
        if (fuzzyLogic) {
            if (installmentWiseDCBRequest.orderBy().equals(LICENSENUMBER) || installmentWiseDCBRequest.orderBy().equals(ACTIVE))
                criteriaQuery.orderBy(installmentWiseDCBRequest.orderDir().equals(Direction.ASC)
                        ? criteriaBuilder.asc(root.get(installmentWiseDCBRequest.orderBy()))
                        : criteriaBuilder.desc(root.get(installmentWiseDCBRequest.orderBy())));
            else
                criteriaQuery.orderBy(installmentWiseDCBRequest.orderDir().equals(Direction.ASC)
                        ? criteriaBuilder.asc(criteriaBuilder.sum(root.get(installmentWiseDCBRequest.orderBy())))
                        : criteriaBuilder.desc(criteriaBuilder.sum(root.get(installmentWiseDCBRequest.orderBy()))));
        }
        return criteriaQuery;
    }

    private List<Predicate> predicatecondition(CriteriaBuilder criteriaBuilder,
                                               Root<LicenseInstallmentwiseDCBReportView> root,
                                               InstallmentWiseDCBRequest installmentWiseDCBRequest,
                                               Date financialYearStartDate) {
        final List<Predicate> predicates = new ArrayList<>();
        if (installmentWiseDCBRequest.getLicenseNumber() != null)
            predicates.add(criteriaBuilder.equal(root.get(LICENSENUMBER), installmentWiseDCBRequest.getLicenseNumber()));
        if (installmentWiseDCBRequest.getActiveLicense() > 0)
            predicates.add(criteriaBuilder.equal(root.get(ACTIVE), installmentWiseDCBRequest.getActiveLicense() == 1));
        if (installmentWiseDCBRequest.getWardId() != null && !installmentWiseDCBRequest.getWardId().isEmpty())
            predicates.add(root.get("wardId").in(installmentWiseDCBRequest.getWardId()));
        predicates.add(criteriaBuilder.or(criteriaBuilder.equal(root.get(INSTALLMENT), financialYearStartDate),
                criteriaBuilder.lessThan(root.get(INSTALLMENT), financialYearStartDate)));
        return predicates;
    }
}

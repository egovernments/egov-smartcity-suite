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
package org.egov.works.reports.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.egov.infra.admin.master.entity.Department;
import org.egov.works.lineestimate.entity.enums.LineEstimateStatus;
import org.egov.works.lineestimate.repository.LineEstimateDetailsRepository;
import org.egov.works.reports.entity.EstimateAbstractReport;
import org.egov.works.reports.entity.WorkProgressRegister;
import org.egov.works.reports.entity.WorkProgressRegisterSearchRequest;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WorkProgressRegisterService {

    @Autowired
    private LineEstimateDetailsRepository lineEstimateDetailsRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public List<String> findWorkIdentificationNumbersToSearchLineEstimatesForLoa(final String code) {
        final List<String> workIdNumbers = lineEstimateDetailsRepository
                .findWorkIdentificationNumbersToSearchWorkProgressRegister("%" + code + "%",
                        LineEstimateStatus.ADMINISTRATIVE_SANCTIONED.toString(),
                        LineEstimateStatus.TECHNICAL_SANCTIONED.toString());
        return workIdNumbers;
    }

    @SuppressWarnings({ "deprecation", "unchecked" })
    @Transactional
    public List<WorkProgressRegister> searchWorkProgressRegister(
            final WorkProgressRegisterSearchRequest workProgressRegisterSearchRequest) {
        if (workProgressRegisterSearchRequest != null) {
            final Criteria criteria = entityManager.unwrap(Session.class).createCriteria(WorkProgressRegister.class);
            if (workProgressRegisterSearchRequest.getDepartment() != null)
                criteria.add(Restrictions.eq("department.id",
                        workProgressRegisterSearchRequest.getDepartment()));
            if (workProgressRegisterSearchRequest.getWorkIdentificationNumber() != null)
                criteria.add(Restrictions.eq("winCode",
                        workProgressRegisterSearchRequest.getWorkIdentificationNumber()).ignoreCase());
            if (workProgressRegisterSearchRequest.getContractor() != null) {
                criteria.createAlias("contractor", "contractor");
                criteria.add(Restrictions.or(Restrictions.ilike("contractor.code",
                        workProgressRegisterSearchRequest.getContractor(), MatchMode.ANYWHERE),
                        Restrictions.ilike("contractor.name",
                                workProgressRegisterSearchRequest.getContractor(), MatchMode.ANYWHERE)));
            }
            if (workProgressRegisterSearchRequest.getAdminSanctionFromDate() != null)
                criteria.add(Restrictions.ge("adminSanctionDate",
                        workProgressRegisterSearchRequest.getAdminSanctionFromDate()));
            if (workProgressRegisterSearchRequest.getAdminSanctionToDate() != null)
                criteria.add(Restrictions.le("adminSanctionDate",
                        workProgressRegisterSearchRequest.getAdminSanctionToDate()));

            if (workProgressRegisterSearchRequest.isSpillOverFlag())
                criteria.add(Restrictions.eq("spillOverFlag", workProgressRegisterSearchRequest.isSpillOverFlag()));

            criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            return criteria.list();
        } else
            return new ArrayList<>();
    }

    public Date getReportSchedulerRunDate() {
        List<WorkProgressRegister> obj = entityManager.createQuery("from WorkProgressRegister ", WorkProgressRegister.class)
                .setMaxResults(1).getResultList();
        Date runDate = null;
        if (obj != null)
            runDate = obj.get(0).getCreatedDate();
        return runDate;
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public List<EstimateAbstractReport> searchEstimateAbstractReportByDepartmentWise(
            final EstimateAbstractReport estimateAbstractReport) {

        Query query = entityManager
                .createNativeQuery(getQueryForDepartmentWiseReport(estimateAbstractReport));
        query = setParameterForDepartmentWiseReport(estimateAbstractReport, query);
		List<Object[]> results = query.getResultList();
		if (results.isEmpty()) {
			return Collections.emptyList();
		} else
			return prepareEstimateAbstractReportDetails(results, true);

    }

    private Query setParameterForDepartmentWiseReport(EstimateAbstractReport estimateAbstractReport,
            Query query) {
        if (estimateAbstractReport != null) {

            if (estimateAbstractReport.isSpillOverFlag())
                query.setParameter("spilloverflag", true);
            if (estimateAbstractReport.getDepartment() != null)
                query.setParameter("department", estimateAbstractReport.getDepartment());

            if (estimateAbstractReport.getAdminSanctionFromDate() != null)
                query.setParameter("fromDate", estimateAbstractReport.getAdminSanctionFromDate(), TemporalType.DATE);

            if (estimateAbstractReport.getAdminSanctionToDate() != null)
                query.setParameter("toDate", estimateAbstractReport.getAdminSanctionToDate(), TemporalType.DATE);

            if (estimateAbstractReport.getScheme() != null)
                query.setParameter("scheme", estimateAbstractReport.getScheme());

            if (estimateAbstractReport.getSubScheme() != null)
                query.setParameter("subScheme", estimateAbstractReport.getSubScheme());

            if (estimateAbstractReport.getWorkCategory() != null)
                query.setParameter("workcategory", estimateAbstractReport.getWorkCategory());

            if (estimateAbstractReport.getBeneficiary() != null)
                query.setParameter("beneficiary", estimateAbstractReport.getBeneficiary());

            if (estimateAbstractReport.getNatureOfWork() != null)
                query.setParameter("natureofwork", estimateAbstractReport.getNatureOfWork());

        }
        return query;
    }

    private Query setParameterForTypeOfWorkWiseReport(EstimateAbstractReport estimateAbstractReport, Query query) {

        if (estimateAbstractReport != null) {
            if (estimateAbstractReport.isSpillOverFlag())
                query.setParameter("spilloverflag", true);

            if (estimateAbstractReport.getTypeOfWork() != null)
                query.setParameter("typeofwork", estimateAbstractReport.getTypeOfWork());

            if (estimateAbstractReport.getSubTypeOfWork() != null)
                query.setParameter("subtypeofwork", estimateAbstractReport.getSubTypeOfWork());

            if (estimateAbstractReport.getDepartments() != null
                    && !estimateAbstractReport.getDepartments().toString().equalsIgnoreCase("[null]")) {
                List<Long> departmentIds = new ArrayList<>();
                for (Department dept : estimateAbstractReport.getDepartments())
                    departmentIds.add(dept.getId());
                query.setParameter("departmentIds", departmentIds);

            }
            if (estimateAbstractReport.getAdminSanctionFromDate() != null)
                query.setParameter("fromDate", estimateAbstractReport.getAdminSanctionFromDate(), TemporalType.DATE);

            if (estimateAbstractReport.getAdminSanctionToDate() != null)
                query.setParameter("toDate", estimateAbstractReport.getAdminSanctionToDate(), TemporalType.DATE);
            if (estimateAbstractReport.getScheme() != null)
                query.setParameter("scheme", estimateAbstractReport.getScheme());

            if (estimateAbstractReport.getSubScheme() != null)
                query.setParameter("subScheme", estimateAbstractReport.getSubScheme());

            if (estimateAbstractReport.getWorkCategory() != null)
                query.setParameter("workcategory", estimateAbstractReport.getWorkCategory());

            if (estimateAbstractReport.getBeneficiary() != null)
                query.setParameter("beneficiary", estimateAbstractReport.getBeneficiary());

            if (estimateAbstractReport.getNatureOfWork() != null)
                query.setParameter("natureofwork", estimateAbstractReport.getNatureOfWork());

        }
        return query;
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public List<EstimateAbstractReport> searchEstimateAbstractReportByTypeOfWorkWise(
            final EstimateAbstractReport estimateAbstractReport) {
        Query query = null;
        if (estimateAbstractReport.getDepartments() != null
                && !estimateAbstractReport.getDepartments().toString().equalsIgnoreCase("[null]")) {
            query = entityManager.createNativeQuery(getQueryForTypeOfWorkWiseReport(estimateAbstractReport));
            query = setParameterForTypeOfWorkWiseReport(estimateAbstractReport, query);
        } else {
            query = entityManager.createNativeQuery(getQueryForTypeOfWorkWiseReport(estimateAbstractReport));
            query = setParameterForTypeOfWorkWiseReport(estimateAbstractReport, query);

        }

		List<Object[]> results = query.getResultList();
		if (results.isEmpty()) {
			return Collections.emptyList();
		} else
			return prepareEstimateAbstractReportDetails(results, false);
	}

    private String getQueryForDepartmentWiseReport(EstimateAbstractReport estimateAbstractReport) {
        StringBuilder workInProgessCondition = new StringBuilder();
        StringBuilder filterConditions = new StringBuilder();

        if (estimateAbstractReport != null) {

            if (estimateAbstractReport.getDepartment() != null)
                filterConditions.append(" AND details.department =:department ");

            if (estimateAbstractReport.getAdminSanctionFromDate() != null)
                filterConditions.append(" AND details.adminsanctiondate >=:fromDate ");

            if (estimateAbstractReport.getAdminSanctionToDate() != null)
                filterConditions.append(" AND details.adminsanctiondate <=:toDate ");

            if (estimateAbstractReport.getScheme() != null)
                filterConditions.append(" AND details.scheme =:scheme ");

            if (estimateAbstractReport.getSubScheme() != null)
                filterConditions.append(" AND details.subScheme =:subScheme ");

            if (estimateAbstractReport.getWorkCategory() != null)
                filterConditions.append(" AND details.workcategory =:workcategory ");
            if (estimateAbstractReport.getBeneficiary() != null)
                filterConditions.append(" AND details.beneficiary =:beneficiary ");

            if (estimateAbstractReport.getNatureOfWork() != null)
                filterConditions.append(" AND details.natureofwork =:natureofwork ");
            if (estimateAbstractReport.isSpillOverFlag()) {
                filterConditions.append(" AND details.spilloverflag =:spilloverflag ");

                workInProgessCondition.append(" SELECT details.departmentName AS departmentName, ");
                workInProgessCondition.append(" 0                             AS lineEstimates, ");
                workInProgessCondition.append(" 0                             AS lineEstimateDetails, ");
                workInProgessCondition.append(" 0                             AS adminSanctionedAmountInCrores, ");
                workInProgessCondition.append(" 0                             AS adminSanctionedEstimates, ");
                workInProgessCondition.append(" 0                             AS technicalSanctionedEstimates, ");
                workInProgessCondition.append(" 0                             AS loaCreated, ");
                workInProgessCondition.append(" 0                             AS agreementValueInCrores, ");
                workInProgessCondition.append(" COUNT(DISTINCT details.ledid) AS workInProgress, ");
                workInProgessCondition.append(" 0                             AS workCompleted, ");
                workInProgessCondition.append(" 0                             AS billsCreated, ");
                workInProgessCondition.append(" 0                             AS billValueInCrores ");
                workInProgessCondition.append(" FROM egw_mv_work_progress_register details ");
                workInProgessCondition.append(" WHERE ");
                workInProgessCondition.append(" ( details.workordercreated  = true or details.wostatuscode = 'APPROVED') ");
                workInProgessCondition.append(" AND details.workcompleted  = false ");
                workInProgessCondition.append(filterConditions.toString());
                workInProgessCondition.append(" GROUP BY details.departmentName ");

            } else {
                workInProgessCondition.append(" SELECT details.departmentName AS departmentName, ");
                workInProgessCondition.append(" 0                             AS lineEstimates, ");
                workInProgessCondition.append(" 0                             AS lineEstimateDetails, ");
                workInProgessCondition.append(" 0                             AS adminSanctionedAmountInCrores, ");
                workInProgessCondition.append(" 0                             AS adminSanctionedEstimates, ");
                workInProgessCondition.append(" 0                             AS technicalSanctionedEstimates, ");
                workInProgessCondition.append(" 0                             AS loaCreated, ");
                workInProgessCondition.append(" 0                             AS agreementValueInCrores, ");
                workInProgessCondition.append(" COUNT(DISTINCT details.ledid) AS workInProgress, ");
                workInProgessCondition.append(" 0                             AS workCompleted, ");
                workInProgessCondition.append(" 0                             AS billsCreated, ");
                workInProgessCondition.append(" 0                             AS billValueInCrores ");
                workInProgessCondition.append(" FROM egw_mv_work_progress_register details ");
                workInProgessCondition.append(" WHERE ");
                workInProgessCondition.append(" ( details.workordercreated  = true or details.wostatuscode = 'APPROVED') ");
                workInProgessCondition.append(" AND details.workcompleted  = false ");
                workInProgessCondition.append(" AND details.spilloverflag  = true ");
                workInProgessCondition.append(filterConditions.toString());
                workInProgessCondition.append(" GROUP BY details.departmentName ");
                workInProgessCondition.append(" UNION ");
                workInProgessCondition.append(" SELECT details.departmentName AS departmentName, ");
                workInProgessCondition.append(" 0                             AS lineEstimates, ");
                workInProgessCondition.append(" 0                             AS lineEstimateDetails, ");
                workInProgessCondition.append(" 0                             AS adminSanctionedAmountInCrores, ");
                workInProgessCondition.append(" 0                             AS adminSanctionedEstimates, ");
                workInProgessCondition.append(" 0                             AS technicalSanctionedEstimates, ");
                workInProgessCondition.append(" 0                             AS loaCreated, ");
                workInProgessCondition.append(" 0                             AS agreementValueInCrores, ");
                workInProgessCondition.append(" COUNT(DISTINCT details.ledid) AS workInProgress, ");
                workInProgessCondition.append(" 0                             AS workCompleted, ");
                workInProgessCondition.append(" 0                             AS billsCreated, ");
                workInProgessCondition.append(" 0                             AS billValueInCrores ");
                workInProgessCondition.append(" FROM egw_mv_work_progress_register details ");
                workInProgessCondition.append(" WHERE ");
                workInProgessCondition.append(" details.wostatuscode = 'APPROVED' ");
                workInProgessCondition.append(" AND details.workcompleted  = false ");
                workInProgessCondition.append(" AND details.spilloverflag  = false ");
                workInProgessCondition.append(filterConditions.toString());
                workInProgessCondition.append(" GROUP BY details.departmentName ");

            }
        }
        StringBuilder query = new StringBuilder();
        query.append("SELECT departmentName AS departmentName, ");
        query.append(" SUM(lineEstimates)                 AS lineEstimates ,  ");
        query.append(" SUM(lineEstimateDetails)           AS lineEstimateDetails ,  ");
        query.append(" SUM(adminSanctionedAmountInCrores) AS adminSanctionedAmountInCrores,  ");
        query.append(" SUM(adminSanctionedEstimates)      AS adminSanctionedEstimates,  ");
        query.append(" SUM(technicalSanctionedEstimates)  AS technicalSanctionedEstimates,  ");
        query.append(" SUM(loaCreated)                    AS loaCreated,  ");
        query.append(" SUM(agreementValueInCrores)        AS agreementValueInCrores,  ");
        query.append(" SUM(workInProgress)                AS workInProgress,  ");
        query.append(" SUM(workCompleted)                 AS workCompleted ,  ");
        query.append(" SUM(billsCreated)                  AS billsCreated,  ");
        query.append(" SUM(billValueInCrores)             AS billValueInCrores  ");
        query.append(" FROM  ");
        query.append(" (  ");
        query.append(" SELECT details.departmentName       AS departmentName,  ");
        query.append(" COUNT(DISTINCT details.leid)         AS lineEstimates,  ");
        query.append(" COUNT(details.ledid)                 AS lineEstimateDetails,  ");
        query.append(" SUM(details.estimateamount)/10000000 AS adminSanctionedAmountInCrores,  ");
        query.append(" COUNT(details.lineestimatestatus)              AS adminSanctionedEstimates,  ");
        query.append(" 0                                    AS technicalSanctionedEstimates,  ");
        query.append(" 0                                    AS loaCreated,  ");
        query.append(" 0                                    AS agreementValueInCrores, ");
        query.append(" 0                                    AS workInProgress, ");
        query.append(" 0                                    AS workCompleted , ");
        query.append(" 0                                    AS billsCreated, ");
        query.append(" 0                                    AS billValueInCrores ");
        query.append(" FROM egw_mv_work_progress_register details, ");
        query.append("   egw_status status ");
        query.append(" WHERE details.lineestimatestatus = status.code ");
        query.append(" AND status.code       IN ('TECHNICAL_SANCTIONED','ADMINISTRATIVE_SANCTIONED') ");
        query.append(filterConditions.toString());
        query.append(" GROUP BY details.departmentName ");
        query.append(" UNION ");
        query.append(" SELECT details.departmentName AS departmentName, ");
        query.append(" 0                           AS lineEstimates, ");
        query.append(" 0                           AS lineEstimateDetails, ");
        query.append(" 0                           AS adminSanctionedAmountInCrores, ");
        query.append(" 0                           AS adminSanctionedEstimates, ");
        query.append(" COUNT(details.lineestimatestatus)     AS technicalSanctionedEstimates, ");
        query.append(" 0                           AS loaCreated, ");
        query.append(" 0                           AS agreementValueInCrores, ");
        query.append(" 0                           AS workInProgress, ");
        query.append(" 0                           AS workCompleted , ");
        query.append(" 0                           AS billsCreated, ");
        query.append(" 0                           AS billValueInCrores ");
        query.append(" FROM egw_mv_work_progress_register details, ");
        query.append(" egw_status status ");
        query.append(" WHERE details.lineestimatestatus = status.code ");
        query.append(" AND status.code       IN ('TECHNICAL_SANCTIONED') ");
        query.append(filterConditions.toString());
        query.append(" GROUP BY details.departmentName ");
        query.append(" UNION ");
        query.append(" SELECT details.departmentName         AS departmentName, ");
        query.append(" 0                                     AS lineEstimates, ");
        query.append(" 0                                     AS lineEstimateDetails, ");
        query.append(" 0                                     AS adminSanctionedAmountInCrores, ");
        query.append(" 0                                     AS adminSanctionedEstimates, ");
        query.append(" 0                                     AS technicalSanctionedEstimates, ");
        query.append(" COUNT(details.ledid)                  AS loaCreated, ");
        query.append(" SUM(details.agreementamount)/10000000 AS agreementValueInCrores, ");
        query.append(" 0                                     AS workInProgress, ");
        query.append(" 0                                     AS workCompleted, ");
        query.append(" 0                                     AS billsCreated, ");
        query.append(" 0                                     AS billValueInCrores ");
        query.append(" FROM egw_mv_work_progress_register details ");
        query.append(" WHERE details.agreementnumber IS NOT NULL ");
        query.append(" AND details.wostatuscode       = 'APPROVED' ");
        query.append(filterConditions.toString());
        query.append(" GROUP BY details.departmentName ");
        query.append(" UNION ");
        query.append(workInProgessCondition.toString());
        query.append(" UNION ");
        query.append(" SELECT details.departmentName AS departmentName, ");
        query.append(" 0                             AS lineEstimates, ");
        query.append(" 0                             AS lineEstimateDetails, ");
        query.append(" 0                             AS adminSanctionedAmountInCrores, ");
        query.append(" 0                             AS adminSanctionedEstimates, ");
        query.append(" 0                             AS technicalSanctionedEstimates, ");
        query.append(" 0                             AS loaCreated, ");
        query.append(" 0                             AS agreementValueInCrores, ");
        query.append(" 0                             AS workInProgress, ");
        query.append(" COUNT(DISTINCT details.ledid) AS workCompleted, ");
        query.append(" 0                             AS billsCreated, ");
        query.append(" 0                             AS billValueInCrores ");
        query.append(" FROM egw_mv_work_progress_register details ");
        query.append(" WHERE details.workcompleted = true ");
        query.append(filterConditions.toString());
        query.append(" GROUP BY details.departmentName ");
        query.append(" UNION ");
        query.append(" SELECT details.departmentName       AS departmentName, ");
        query.append(" 0                                   AS lineEstimates, ");
        query.append(" 0                                   AS lineEstimateDetails, ");
        query.append(" 0                                   AS adminSanctionedAmountInCrores, ");
        query.append(" 0                                   AS adminSanctionedEstimates, ");
        query.append(" 0                                   AS technicalSanctionedEstimates, ");
        query.append(" 0                                   AS loaCreated, ");
        query.append(" 0                                   AS agreementValueInCrores, ");
        query.append(" 0                                   AS workInProgress, ");
        query.append(" 0                                   AS workCompleted , ");
        query.append(" COUNT(DISTINCT billdetail.billid)   AS billsCreated, ");
        query.append(" SUM(billdetail.billamount)/10000000 AS billValueInCrores ");
        query.append(" FROM egw_mv_work_progress_register details , ");
        query.append(" egw_mv_billdetail billdetail ");
        query.append(" WHERE billdetail.ledid = details.ledid ");
        query.append(filterConditions.toString());
        query.append(" GROUP BY details.departmentName ");
        query.append(" ) final ");
        query.append(" GROUP BY departmentname  ");
        return query.toString();
    }

    private String getQueryForTypeOfWorkWiseReport(EstimateAbstractReport estimateAbstractReport) {
        StringBuilder workInProgessCondition = new StringBuilder();
        StringBuilder filterConditions = new StringBuilder();
        StringBuilder selectQuery = new StringBuilder();
        StringBuilder groupByQuery = new StringBuilder();
        StringBuilder mainSelectQuery = new StringBuilder();
        StringBuilder mainGroupByQuery = new StringBuilder();
        if (estimateAbstractReport.getDepartments() != null
                && !estimateAbstractReport.getDepartments().toString().equalsIgnoreCase("[null]")) {
            filterConditions.append(" AND details.department in ( :departmentIds ) ");

            selectQuery.append(" SELECT details.typeOfWorkName       AS typeOfWorkName,  ");
            selectQuery.append(" details.subTypeOfWorkName         AS subTypeOfWorkName,  ");
            selectQuery.append(" details.departmentName         AS departmentName,  ");

            mainSelectQuery.append(" SELECT typeOfWorkName       AS typeOfWorkName,  ");
            mainSelectQuery.append(" subTypeOfWorkName         AS subTypeOfWorkName,  ");
            mainSelectQuery.append(" departmentName         AS departmentName,  ");

            groupByQuery.append(" GROUP BY details.typeOfWorkName,details.subTypeOfWorkName,details.departmentName ");
            mainGroupByQuery.append(" GROUP BY typeofworkname,subtypeofworkname,departmentname ");
        } else {
            selectQuery.append(" SELECT details.typeOfWorkName       AS typeOfWorkName,  ");
            selectQuery.append(" details.subTypeOfWorkName         AS subTypeOfWorkName,  ");

            mainSelectQuery.append(" SELECT typeOfWorkName       AS typeOfWorkName,  ");
            mainSelectQuery.append(" subTypeOfWorkName         AS subTypeOfWorkName,  ");

            groupByQuery.append(" GROUP BY details.typeOfWorkName,details.subTypeOfWorkName ");

            mainGroupByQuery.append(" GROUP BY typeofworkname,subtypeofworkname ");
        }

        if (estimateAbstractReport != null) {

            if (estimateAbstractReport.getTypeOfWork() != null)
                filterConditions.append(" AND details.typeofwork =:typeofwork ");

            if (estimateAbstractReport.getSubTypeOfWork() != null)
                filterConditions.append(" AND details.subtypeofwork =:subtypeofwork ");

            if (estimateAbstractReport.getAdminSanctionFromDate() != null)
                filterConditions.append(" AND details.adminsanctiondate >=:fromDate ");

            if (estimateAbstractReport.getAdminSanctionToDate() != null)
                filterConditions.append(" AND details.adminsanctiondate <=:toDate ");

            if (estimateAbstractReport.getScheme() != null)
                filterConditions.append(" AND details.scheme =:scheme ");

            if (estimateAbstractReport.getSubScheme() != null)
                filterConditions.append(" AND details.subScheme =:subScheme ");

            if (estimateAbstractReport.getWorkCategory() != null)
                filterConditions.append(" AND details.workcategory =:workcategory ");

            if (estimateAbstractReport.getBeneficiary() != null)
                filterConditions.append(" AND details.beneficiary =:beneficiary ");

            if (estimateAbstractReport.getNatureOfWork() != null)
                filterConditions.append(" AND details.natureofwork =:natureofwork ");
            if (estimateAbstractReport.isSpillOverFlag()) {
                filterConditions.append(" AND details.spilloverflag =:spilloverflag ");

                workInProgessCondition.append(selectQuery.toString());
                workInProgessCondition.append(" 0                             AS lineEstimates, ");
                workInProgessCondition.append(" 0                             AS lineEstimateDetails, ");
                workInProgessCondition.append(" 0                             AS adminSanctionedAmountInCrores, ");
                workInProgessCondition.append(" 0                             AS adminSanctionedEstimates, ");
                workInProgessCondition.append(" 0                             AS technicalSanctionedEstimates, ");
                workInProgessCondition.append(" 0                             AS loaCreated, ");
                workInProgessCondition.append(" 0                             AS agreementValueInCrores, ");
                workInProgessCondition.append(" COUNT(DISTINCT details.ledid) AS workInProgress, ");
                workInProgessCondition.append(" 0                             AS workCompleted, ");
                workInProgessCondition.append(" 0                             AS billsCreated, ");
                workInProgessCondition.append(" 0                             AS billValueInCrores ");
                workInProgessCondition.append(" FROM egw_mv_work_progress_register details ");
                workInProgessCondition.append(" WHERE ");
                workInProgessCondition.append(" ( details.workordercreated  = true or details.wostatuscode = 'APPROVED') ");
                workInProgessCondition.append(" AND details.workcompleted  = false ");
                workInProgessCondition.append(filterConditions.toString());
                workInProgessCondition.append(groupByQuery.toString());

            } else {
                workInProgessCondition.append(selectQuery.toString());
                workInProgessCondition.append(" 0                             AS lineEstimates, ");
                workInProgessCondition.append(" 0                             AS lineEstimateDetails, ");
                workInProgessCondition.append(" 0                             AS adminSanctionedAmountInCrores, ");
                workInProgessCondition.append(" 0                             AS adminSanctionedEstimates, ");
                workInProgessCondition.append(" 0                             AS technicalSanctionedEstimates, ");
                workInProgessCondition.append(" 0                             AS loaCreated, ");
                workInProgessCondition.append(" 0                             AS agreementValueInCrores, ");
                workInProgessCondition.append(" COUNT(DISTINCT details.ledid) AS workInProgress, ");
                workInProgessCondition.append(" 0                             AS workCompleted, ");
                workInProgessCondition.append(" 0                             AS billsCreated, ");
                workInProgessCondition.append(" 0                             AS billValueInCrores ");
                workInProgessCondition.append(" FROM egw_mv_work_progress_register details ");
                workInProgessCondition.append(" WHERE ");
                workInProgessCondition.append(" ( details.workordercreated  = true or details.wostatuscode = 'APPROVED') ");
                workInProgessCondition.append(" AND details.workcompleted  = false ");
                workInProgessCondition.append(" AND details.spilloverflag  = true ");
                workInProgessCondition.append(filterConditions.toString());
                workInProgessCondition.append(groupByQuery.toString());
                workInProgessCondition.append(" UNION ");
                workInProgessCondition.append(selectQuery.toString());
                workInProgessCondition.append(" 0                             AS lineEstimates, ");
                workInProgessCondition.append(" 0                             AS lineEstimateDetails, ");
                workInProgessCondition.append(" 0                             AS adminSanctionedAmountInCrores, ");
                workInProgessCondition.append(" 0                             AS adminSanctionedEstimates, ");
                workInProgessCondition.append(" 0                             AS technicalSanctionedEstimates, ");
                workInProgessCondition.append(" 0                             AS loaCreated, ");
                workInProgessCondition.append(" 0                             AS agreementValueInCrores, ");
                workInProgessCondition.append(" COUNT(DISTINCT details.ledid) AS workInProgress, ");
                workInProgessCondition.append(" 0                             AS workCompleted, ");
                workInProgessCondition.append(" 0                             AS billsCreated, ");
                workInProgessCondition.append(" 0                             AS billValueInCrores ");
                workInProgessCondition.append(" FROM egw_mv_work_progress_register details ");
                workInProgessCondition.append(" WHERE ");
                workInProgessCondition.append(" details.wostatuscode = 'APPROVED' ");
                workInProgessCondition.append(" AND details.workcompleted  = false ");
                workInProgessCondition.append(" AND details.spilloverflag  = false ");
                workInProgessCondition.append(filterConditions.toString());
                workInProgessCondition.append(groupByQuery.toString());

            }
        }
        StringBuilder query = new StringBuilder();
        query.append(mainSelectQuery.toString());
        query.append(" SUM(lineEstimates)                 AS lineEstimates ,  ");
        query.append(" SUM(lineEstimateDetails)           AS lineEstimateDetails ,  ");
        query.append(" SUM(adminSanctionedAmountInCrores) AS adminSanctionedAmountInCrores,  ");
        query.append(" SUM(adminSanctionedEstimates)      AS adminSanctionedEstimates,  ");
        query.append(" SUM(technicalSanctionedEstimates)  AS technicalSanctionedEstimates,  ");
        query.append(" SUM(loaCreated)                    AS loaCreated,  ");
        query.append(" SUM(agreementValueInCrores)        AS agreementValueInCrores,  ");
        query.append(" SUM(workInProgress)                AS workInProgress,  ");
        query.append(" SUM(workCompleted)                 AS workCompleted ,  ");
        query.append(" SUM(billsCreated)                  AS billsCreated,  ");
        query.append(" SUM(billValueInCrores)             AS billValueInCrores  ");
        query.append(" FROM  ");
        query.append(" (  ");
        query.append(selectQuery.toString());
        query.append(" COUNT(DISTINCT details.leid)         AS lineEstimates,  ");
        query.append(" COUNT(details.ledid)                 AS lineEstimateDetails,  ");
        query.append(" SUM(details.estimateamount)/10000000 AS adminSanctionedAmountInCrores,  ");
        query.append(" COUNT(details.lineestimatestatus)              AS adminSanctionedEstimates,  ");
        query.append(" 0                                    AS technicalSanctionedEstimates,  ");
        query.append(" 0                                    AS loaCreated,  ");
        query.append(" 0                                    AS agreementValueInCrores, ");
        query.append(" 0                                    AS workInProgress, ");
        query.append(" 0                                    AS workCompleted , ");
        query.append(" 0                                    AS billsCreated, ");
        query.append(" 0                                    AS billValueInCrores ");
        query.append(" FROM egw_mv_work_progress_register details, ");
        query.append("   egw_status status ");
        query.append(" WHERE details.lineestimatestatus = status.code ");
        query.append(" AND status.code       IN ('TECHNICAL_SANCTIONED','ADMINISTRATIVE_SANCTIONED') ");
        query.append(filterConditions.toString());
        query.append(groupByQuery.toString());
        query.append(" UNION ");
        query.append(selectQuery.toString());
        query.append(" 0                           AS lineEstimates, ");
        query.append(" 0                           AS lineEstimateDetails, ");
        query.append(" 0                           AS adminSanctionedAmountInCrores, ");
        query.append(" 0                           AS adminSanctionedEstimates, ");
        query.append(" COUNT(details.lineestimatestatus)     AS technicalSanctionedEstimates, ");
        query.append(" 0                           AS loaCreated, ");
        query.append(" 0                           AS agreementValueInCrores, ");
        query.append(" 0                           AS workInProgress, ");
        query.append(" 0                           AS workCompleted , ");
        query.append(" 0                           AS billsCreated, ");
        query.append(" 0                           AS billValueInCrores ");
        query.append(" FROM egw_mv_work_progress_register details, ");
        query.append(" egw_status status ");
        query.append(" WHERE details.lineestimatestatus = status.code ");
        query.append(" AND status.code       IN ('TECHNICAL_SANCTIONED') ");
        query.append(filterConditions.toString());
        query.append(groupByQuery.toString());
        query.append(" UNION ");
        query.append(selectQuery.toString());
        query.append(" 0                                     AS lineEstimates, ");
        query.append(" 0                                     AS lineEstimateDetails, ");
        query.append(" 0                                     AS adminSanctionedAmountInCrores, ");
        query.append(" 0                                     AS adminSanctionedEstimates, ");
        query.append(" 0                                     AS technicalSanctionedEstimates, ");
        query.append(" COUNT(details.ledid)                  AS loaCreated, ");
        query.append(" SUM(details.agreementamount)/10000000 AS agreementValueInCrores, ");
        query.append(" 0                                     AS workInProgress, ");
        query.append(" 0                                     AS workCompleted, ");
        query.append(" 0                                     AS billsCreated, ");
        query.append(" 0                                     AS billValueInCrores ");
        query.append(" FROM egw_mv_work_progress_register details ");
        query.append(" WHERE details.agreementnumber IS NOT NULL ");
        query.append(" AND details.wostatuscode       = 'APPROVED' ");
        query.append(filterConditions.toString());
        query.append(groupByQuery.toString());
        query.append(" UNION ");
        query.append(workInProgessCondition.toString());
        query.append(" UNION ");
        query.append(selectQuery.toString());
        query.append(" 0                             AS lineEstimates, ");
        query.append(" 0                             AS lineEstimateDetails, ");
        query.append(" 0                             AS adminSanctionedAmountInCrores, ");
        query.append(" 0                             AS adminSanctionedEstimates, ");
        query.append(" 0                             AS technicalSanctionedEstimates, ");
        query.append(" 0                             AS loaCreated, ");
        query.append(" 0                             AS agreementValueInCrores, ");
        query.append(" 0                             AS workInProgress, ");
        query.append(" COUNT(DISTINCT details.ledid) AS workCompleted, ");
        query.append(" 0                             AS billsCreated, ");
        query.append(" 0                             AS billValueInCrores ");
        query.append(" FROM egw_mv_work_progress_register details ");
        query.append(" WHERE details.workcompleted = true ");
        query.append(filterConditions.toString());
        query.append(groupByQuery.toString());
        query.append(" UNION ");
        query.append(selectQuery.toString());
        query.append(" 0                                   AS lineEstimates, ");
        query.append(" 0                                   AS lineEstimateDetails, ");
        query.append(" 0                                   AS adminSanctionedAmountInCrores, ");
        query.append(" 0                                   AS adminSanctionedEstimates, ");
        query.append(" 0                                   AS technicalSanctionedEstimates, ");
        query.append(" 0                                   AS loaCreated, ");
        query.append(" 0                                   AS agreementValueInCrores, ");
        query.append(" 0                                   AS workInProgress, ");
        query.append(" 0                                   AS workCompleted , ");
        query.append(" COUNT(DISTINCT billdetail.billid)   AS billsCreated, ");
        query.append(" SUM(billdetail.billamount)/10000000 AS billValueInCrores ");
        query.append(" FROM egw_mv_work_progress_register details , ");
        query.append(" egw_mv_billdetail billdetail ");
        query.append(" WHERE billdetail.ledid = details.ledid ");
        query.append(filterConditions.toString());
        query.append(groupByQuery.toString());
        query.append(" ) final ");
        query.append(mainGroupByQuery.toString());
        return query.toString();
    }
    
	private List<EstimateAbstractReport> prepareEstimateAbstractReportDetails(List<Object[]> results,
			final boolean isByDepartment) {
		List<EstimateAbstractReport> estimateAbstractReportList;
		for (Object[] obj : results) {
			for (int i = 0; i < obj.length; i++) {
				if (obj[i] == null) {
					obj[i] = "NA";
				}
			}
		}
		if (isByDepartment) {
			estimateAbstractReportList = results.stream().map(result -> new EstimateAbstractReport("NA", "NA",
					result[0].toString(), new BigDecimal(result[1].toString()).longValue(),
					new BigDecimal(result[2].toString()).longValue(), result[3].toString(),
					new BigDecimal(result[4].toString()).longValue(), new BigDecimal(result[5].toString()).longValue(),
					new BigDecimal(result[6].toString()).longValue(), result[7].toString(),
					new BigDecimal(result[8].toString()).longValue(), new BigDecimal(result[9].toString()).longValue(),
					new BigDecimal(result[10].toString()).longValue(), result[11].toString()))
					.collect(Collectors.toList());
		} else
			estimateAbstractReportList = results.stream().map(result -> new EstimateAbstractReport(result[0].toString(),
					result[1].toString(), result[2].toString(), new BigDecimal(result[3].toString()).longValue(),
					new BigDecimal(result[4].toString()).longValue(), result[5].toString(),
					new BigDecimal(result[6].toString()).longValue(), new BigDecimal(result[7].toString()).longValue(),
					new BigDecimal(result[8].toString()).longValue(), result[9].toString(),
					new BigDecimal(result[10].toString()).longValue(),
					new BigDecimal(result[11].toString()).longValue(),
					new BigDecimal(result[12].toString()).longValue(), result[13].toString()))
					.collect(Collectors.toList());

		return estimateAbstractReportList;
	}

}

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
package org.egov.works.reports.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.egov.commons.CFinancialYear;
import org.egov.commons.service.CFinancialYearService;
import org.egov.infra.admin.master.entity.Department;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.abstractestimate.repository.AbstractEstimateRepository;
import org.egov.works.config.properties.WorksApplicationProperties;
import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.egov.works.lineestimate.entity.enums.LineEstimateStatus;
import org.egov.works.lineestimate.repository.LineEstimateDetailsRepository;
import org.egov.works.reports.entity.ContractorWiseAbstractReport;
import org.egov.works.reports.entity.ContractorWiseAbstractSearchResult;
import org.egov.works.reports.entity.EstimateAbstractReport;
import org.egov.works.reports.entity.WorkProgressRegister;
import org.egov.works.reports.entity.WorkProgressRegisterSearchRequest;
import org.egov.works.reports.entity.enums.WorkStatus;
import org.egov.works.reports.repository.WorkProgressRegisterRepository;
import org.egov.works.utils.WorksConstants;
import org.egov.works.workorder.entity.WorkOrder.OfflineStatuses;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WorkProgressRegisterService {

    public static final String JSONDATAPREFIX = "{ \"data\":";
    public static final String CONTENTDISPOSITION = "content-disposition";
    private static final String CONTRACTOR = "contractor";
    private static final String WORKSTATUS = "workstatus";
    private static final String LINEESTIMATE_STATUS = "'TECHNICAL_SANCTIONED','ADMINISTRATIVE_SANCTIONED'";
    // Multiple status can be added like
    // 'TECHNICAL_SANCTIONED','ADMINISTRATIVE_SANCTIONED'
    private static final String ABSTRACTESTIMATE_STATUS = "'APPROVED'";
    private static final String DEPARTMENTNAME = "departmentName";
    private static final String LINEESTIMATES = "lineEstimates";
    private static final String ABSTRACTESTIMATES = "abstractEstimates";
    private static final String ADMINSANCTIONEDESTIMATES = "adminSanctionedEstimates";
    private static final String LEADMINSANCTIONEDAMOUNTINCRORES = "leAdminSanctionedAmountInCrores";
    private static final String AEADMINSANCTIONEDAMOUNTINCRORES = "aeAdminSanctionedAmountInCrores";
    private static final String WORKVALUEOFADMINSANCTIONEDAEINCRORES = "workValueOfAdminSanctionedAEInCrores";
    private static final String TECHNICALSANCTIONEDESTIMATES = "technicalSanctionedEstimates";
    private static final String LOACREATED = "loaCreated";
    private static final String LOANOTCREATED = "loaNotCreated";
    private static final String WORKNOTCOMMENCED = "workNotCommenced";
    private static final String AGREEMENTVALUEINCRORES = "agreementValueInCrores";
    private static final String WORKINPROGRESS = "workInProgress";
    private static final String WORKCOMPLETED = "workCompleted";
    private static final String BILLSCREATED = "billsCreated";
    private static final String BILLVALUEINCRORES = "billValueInCrores";
    private static final String TYPEOFWORKNAME = "typeOfWorkName";
    private static final String SUBTYPEOFWORKNAME = "subTypeOfWorkName";

    @Autowired
    private LineEstimateDetailsRepository lineEstimateDetailsRepository;

    @Autowired
    private WorkProgressRegisterRepository workProgressRegisterRepository;

    @Autowired
    private CFinancialYearService cFinancialYearService;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private AbstractEstimateRepository abstractEstimateRepository;

    @Autowired
    private WorksApplicationProperties worksApplicationProperties;

    public List<String> findWorkIdentificationNumbersToSearchLineEstimatesForLoa(final String code) {
        return lineEstimateDetailsRepository.findWorkIdentificationNumbersToSearchWorkProgressRegister("%" + code + "%",
                LineEstimateStatus.ADMINISTRATIVE_SANCTIONED.toString(),
                LineEstimateStatus.TECHNICAL_SANCTIONED.toString());
    }

    @Transactional
    public List<WorkProgressRegister> searchWorkProgressRegister(
            final WorkProgressRegisterSearchRequest workProgressRegisterSearchRequest) {
        if (workProgressRegisterSearchRequest != null) {
            final Criteria criteria = entityManager.unwrap(Session.class).createCriteria(WorkProgressRegister.class);
            if (workProgressRegisterSearchRequest.getDepartment() != null)
                criteria.add(Restrictions.eq("department.id", workProgressRegisterSearchRequest.getDepartment()));
            if (workProgressRegisterSearchRequest.getWorkIdentificationNumber() != null)
                criteria.add(Restrictions.eq("winCode", workProgressRegisterSearchRequest.getWorkIdentificationNumber())
                        .ignoreCase());
            if (workProgressRegisterSearchRequest.getContractor() != null) {
                criteria.createAlias(CONTRACTOR, CONTRACTOR);
                criteria.add(Restrictions.or(
                        Restrictions.ilike("contractor.code", workProgressRegisterSearchRequest.getContractor(),
                                MatchMode.ANYWHERE),
                        Restrictions.ilike("contractor.name", workProgressRegisterSearchRequest.getContractor(),
                                MatchMode.ANYWHERE)));
            }
            if (workProgressRegisterSearchRequest.getAdminSanctionFromDate() != null)
                criteria.add(Restrictions.ge("adminSanctionDate",
                        workProgressRegisterSearchRequest.getAdminSanctionFromDate()));
            if (workProgressRegisterSearchRequest.getAdminSanctionToDate() != null)
                criteria.add(Restrictions.le("adminSanctionDate",
                        workProgressRegisterSearchRequest.getAdminSanctionToDate()));
            if (workProgressRegisterSearchRequest.getWorkStatus() != null)
                criteria.add(Restrictions.eq(WORKSTATUS, workProgressRegisterSearchRequest.getWorkStatus()));
            criteria.add(Restrictions.eq("spillOverFlag", workProgressRegisterSearchRequest.isSpillOverFlag()));

            criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            return criteria.list();
        } else
            return new ArrayList<WorkProgressRegister>();
    }

    public Date getReportSchedulerRunDate() {
        Query query = null;
        query = entityManager.unwrap(Session.class).createQuery("from WorkProgressRegister ");
        final List<WorkProgressRegister> obj = query.setMaxResults(1).list();
        Date runDate = null;
        if (obj != null)
            runDate = obj.get(0).getCreatedDate();
        return runDate;
    }

    @Transactional
    public List<EstimateAbstractReport> searchEstimateAbstractReportByDepartmentWise(
            final EstimateAbstractReport estimateAbstractReport) {

        Query query;
        query = entityManager.unwrap(Session.class)
                .createSQLQuery(getQueryForDepartmentWiseReport(estimateAbstractReport))
                .addScalar(DEPARTMENTNAME, StringType.INSTANCE).addScalar(LINEESTIMATES, StringType.INSTANCE)
                .addScalar(ADMINSANCTIONEDESTIMATES, StringType.INSTANCE)
                .addScalar(LEADMINSANCTIONEDAMOUNTINCRORES, StringType.INSTANCE)
                .addScalar(AEADMINSANCTIONEDAMOUNTINCRORES, StringType.INSTANCE)
                .addScalar(WORKVALUEOFADMINSANCTIONEDAEINCRORES, StringType.INSTANCE)
                .addScalar(TECHNICALSANCTIONEDESTIMATES, StringType.INSTANCE).addScalar(LOACREATED, StringType.INSTANCE)
                .addScalar(LOANOTCREATED, StringType.INSTANCE).addScalar(WORKNOTCOMMENCED, StringType.INSTANCE)
                .addScalar(AGREEMENTVALUEINCRORES, StringType.INSTANCE).addScalar(WORKINPROGRESS, StringType.INSTANCE)
                .addScalar(WORKCOMPLETED, StringType.INSTANCE).addScalar(BILLSCREATED, StringType.INSTANCE)
                .addScalar(BILLVALUEINCRORES, StringType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(EstimateAbstractReport.class));
        query = setParameterForDepartmentWiseReport(estimateAbstractReport, query);
        return query.list();

    }

    private Query setParameterForDepartmentWiseReport(final EstimateAbstractReport estimateAbstractReport, final Query query) {
        if (estimateAbstractReport != null) {

            if (estimateAbstractReport.isSpillOverFlag())
                query.setBoolean("spilloverflag", true);
            if (estimateAbstractReport.getDepartment() != null)
                query.setLong("department", estimateAbstractReport.getDepartment());

            setAdminSanctionDateQueryParameters(estimateAbstractReport, query);

            setSchemeQueryParameters(estimateAbstractReport, query);

            if (estimateAbstractReport.getWorkCategory() != null)
                query.setString("workcategory", estimateAbstractReport.getWorkCategory());

            if (estimateAbstractReport.getBeneficiary() != null)
                query.setString("beneficiary", estimateAbstractReport.getBeneficiary());

            if (estimateAbstractReport.getNatureOfWork() != null)
                query.setLong("natureofwork", estimateAbstractReport.getNatureOfWork());

            setWorkStatusQueryParameters(estimateAbstractReport, query);

        }
        return query;
    }

    private void setWorkStatusQueryParameters(final EstimateAbstractReport estimateAbstractReport, final Query query) {
        if (estimateAbstractReport.getWorkStatus() != null
                && !StringUtils.EMPTY.equalsIgnoreCase(estimateAbstractReport.getWorkStatus()))
            query.setString(WORKSTATUS, estimateAbstractReport.getWorkStatus().replace("_", " "));
    }

    private void setSchemeQueryParameters(final EstimateAbstractReport estimateAbstractReport, final Query query) {
        if (estimateAbstractReport.getScheme() != null)
            query.setLong("scheme", estimateAbstractReport.getScheme());

        if (estimateAbstractReport.getSubScheme() != null)
            query.setLong("subScheme", estimateAbstractReport.getSubScheme());
    }

    private void setAdminSanctionDateQueryParameters(final EstimateAbstractReport estimateAbstractReport,
            final Query query) {
        if (estimateAbstractReport.getAdminSanctionFromDate() != null)
            query.setDate("fromDate", estimateAbstractReport.getAdminSanctionFromDate());

        if (estimateAbstractReport.getAdminSanctionToDate() != null)
            query.setDate("toDate", estimateAbstractReport.getAdminSanctionToDate());
    }

    private Query setParameterForTypeOfWorkWiseReport(final EstimateAbstractReport estimateAbstractReport,
            final Query query) {

        if (estimateAbstractReport != null) {
            if (estimateAbstractReport.isSpillOverFlag())
                query.setBoolean("spilloverflag", true);

            setTypeOfWorkQueryParameters(estimateAbstractReport, query);

            if (checkEstimateAbstractReportDepartments(estimateAbstractReport)) {
                final List<Long> departmentIds = new ArrayList<Long>();
                for (final Department dept : estimateAbstractReport.getDepartments())
                    departmentIds.add(dept.getId());
                query.setParameterList("departmentIds", departmentIds);

            }
            setAdminSanctionDateQueryParameters(estimateAbstractReport, query);
            setSchemeQueryParameters(estimateAbstractReport, query);

            if (estimateAbstractReport.getWorkCategory() != null)
                query.setString("workcategory", estimateAbstractReport.getWorkCategory());

            if (estimateAbstractReport.getBeneficiary() != null)
                query.setString("beneficiary", estimateAbstractReport.getBeneficiary());

            if (estimateAbstractReport.getNatureOfWork() != null)
                query.setLong("natureofwork", estimateAbstractReport.getNatureOfWork());

            setWorkStatusQueryParameters(estimateAbstractReport, query);
        }
        return query;
    }

    private void setTypeOfWorkQueryParameters(final EstimateAbstractReport estimateAbstractReport, final Query query) {
        if (estimateAbstractReport.getTypeOfWork() != null)
            query.setLong("typeofwork", estimateAbstractReport.getTypeOfWork());

        if (estimateAbstractReport.getSubTypeOfWork() != null)
            query.setLong("subtypeofwork", estimateAbstractReport.getSubTypeOfWork());
    }

    @Transactional
    public List<EstimateAbstractReport> searchEstimateAbstractReportByTypeOfWorkWise(
            final EstimateAbstractReport estimateAbstractReport) {

        Query query;
        if (checkEstimateAbstractReportDepartments(estimateAbstractReport)) {
            query = entityManager.unwrap(Session.class)
                    .createSQLQuery(getQueryForTypeOfWorkWiseReport(estimateAbstractReport))
                    .addScalar(TYPEOFWORKNAME, StringType.INSTANCE).addScalar(SUBTYPEOFWORKNAME, StringType.INSTANCE)
                    .addScalar(DEPARTMENTNAME, StringType.INSTANCE).addScalar(LINEESTIMATES, StringType.INSTANCE)
                    .addScalar(ADMINSANCTIONEDESTIMATES, StringType.INSTANCE)
                    .addScalar(LEADMINSANCTIONEDAMOUNTINCRORES, StringType.INSTANCE)
                    .addScalar(AEADMINSANCTIONEDAMOUNTINCRORES, StringType.INSTANCE)
                    .addScalar(WORKVALUEOFADMINSANCTIONEDAEINCRORES, StringType.INSTANCE)
                    .addScalar(TECHNICALSANCTIONEDESTIMATES, StringType.INSTANCE)
                    .addScalar(LOACREATED, StringType.INSTANCE).addScalar(AGREEMENTVALUEINCRORES, StringType.INSTANCE)
                    .addScalar(LOANOTCREATED, StringType.INSTANCE).addScalar(WORKNOTCOMMENCED, StringType.INSTANCE)
                    .addScalar(WORKINPROGRESS, StringType.INSTANCE).addScalar(WORKCOMPLETED, StringType.INSTANCE)
                    .addScalar(BILLSCREATED, StringType.INSTANCE).addScalar(BILLVALUEINCRORES, StringType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(EstimateAbstractReport.class));
            query = setParameterForTypeOfWorkWiseReport(estimateAbstractReport, query);
        } else {
            query = entityManager.unwrap(Session.class)
                    .createSQLQuery(getQueryForTypeOfWorkWiseReport(estimateAbstractReport))
                    .addScalar(TYPEOFWORKNAME, StringType.INSTANCE).addScalar(SUBTYPEOFWORKNAME, StringType.INSTANCE)
                    .addScalar(LINEESTIMATES, StringType.INSTANCE)
                    .addScalar(ADMINSANCTIONEDESTIMATES, StringType.INSTANCE)
                    .addScalar(LEADMINSANCTIONEDAMOUNTINCRORES, StringType.INSTANCE)
                    .addScalar(AEADMINSANCTIONEDAMOUNTINCRORES, StringType.INSTANCE)
                    .addScalar(WORKVALUEOFADMINSANCTIONEDAEINCRORES, StringType.INSTANCE)
                    .addScalar(TECHNICALSANCTIONEDESTIMATES, StringType.INSTANCE)
                    .addScalar(LOACREATED, StringType.INSTANCE).addScalar(AGREEMENTVALUEINCRORES, StringType.INSTANCE)
                    .addScalar(LOANOTCREATED, StringType.INSTANCE).addScalar(WORKNOTCOMMENCED, StringType.INSTANCE)
                    .addScalar(WORKINPROGRESS, StringType.INSTANCE).addScalar(WORKCOMPLETED, StringType.INSTANCE)
                    .addScalar(BILLSCREATED, StringType.INSTANCE).addScalar(BILLVALUEINCRORES, StringType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(EstimateAbstractReport.class));
            query = setParameterForTypeOfWorkWiseReport(estimateAbstractReport, query);

        }
        return query.list();

    }

    private boolean checkEstimateAbstractReportDepartments(final EstimateAbstractReport estimateAbstractReport) {
        return estimateAbstractReport.getDepartments() != null
                && !"[null]".equalsIgnoreCase(estimateAbstractReport.getDepartments().toString())
                && !"[]".equalsIgnoreCase(estimateAbstractReport.getDepartments().toString());
    }

    private String getQueryForDepartmentWiseReport(final EstimateAbstractReport estimateAbstractReport) {
        final boolean lineEstimateRequired = worksApplicationProperties.lineEstimateRequired();
        final StringBuilder filterConditions = new StringBuilder();

        setFilterConditions(estimateAbstractReport, filterConditions);
        final StringBuilder query = new StringBuilder();
        query.append("SELECT departmentName AS departmentName, ");
        queryAppendForEstimateReportLENotRequiredCase(lineEstimateRequired, query);
        query.append(" SUM(adminSanctionedEstimates)        AS adminSanctionedEstimates,  ");
        query.append(" SUM(aeAdminSanctionedAmountInCrores) AS aeAdminSanctionedAmountInCrores,  ");
        query.append(" SUM(workValueOfAdminSanctionedAEInCrores) AS workValueOfAdminSanctionedAEInCrores, ");
        query.append(" SUM(technicalSanctionedEstimates)  AS technicalSanctionedEstimates,  ");
        query.append(" SUM(loaCreated)                    AS loaCreated,  ");
        query.append(" SUM(agreementValueInCrores)        AS agreementValueInCrores,  ");
        query.append(" SUM(loaNotCreated)                 AS loaNotCreated, ");
        query.append(" SUM(workNotCommenced)              AS workNotCommenced, ");
        query.append(" SUM(workInProgress)                AS workInProgress,  ");
        query.append(" SUM(workCompleted)                 AS workCompleted ,  ");
        query.append(" SUM(billsCreated)                  AS billsCreated,  ");
        query.append(" SUM(billValueInCrores)             AS billValueInCrores  ");
        query.append(" FROM  ");
        query.append(" (  ");
        query.append(" SELECT details.departmentName        AS departmentName,  ");
        if (lineEstimateRequired) {
            query.append(" COUNT(DISTINCT details.leid)                 AS lineEstimates,  ");
            query.append(" COUNT(details.ledid)                         AS lineEstimateDetails,  ");
            query.append(" SUM(details.adminsanctionamount)/10000000    AS leAdminSanctionedAmountInCrores,  ");
            query.append(" SUM(details.estimatevalue)/10000000          AS aeAdminSanctionedAmountInCrores,  ");
            query.append(" SUM(details.workvalue)/10000000              AS workValueOfAdminSanctionedAEInCrores, ");
            query.append(" COUNT(details.adminsanctionby)       AS adminSanctionedEstimates,  ");
            query.append(" COUNT(details.technicalsanctionby)   AS technicalSanctionedEstimates,  ");
        } else {
            query.append(" COUNT(DISTINCT details.aeid)                 AS abstractEstimates ,  ");
            query.append(" SUM(details.adminsanctionamount)/10000000    AS aeAdminSanctionedAmountInCrores,  ");
            query.append(" SUM(details.workvalue)/10000000              AS workValueOfAdminSanctionedAEInCrores, ");
            query.append(" COUNT(details.adminsanctionby)       AS adminSanctionedEstimates,  ");
            query.append(" COUNT(details.technicalsanctionby)   AS technicalSanctionedEstimates,  ");
        }
        query.append(" 0                                    AS loaCreated,  ");
        query.append(" 0                                    AS agreementValueInCrores, ");
        query.append(" 0                                    AS loaNotCreated, ");
        query.append(" 0                                    AS workNotCommenced, ");
        query.append(" 0                                    AS workInProgress, ");
        query.append(" 0                                    AS workCompleted , ");
        query.append(" 0                                    AS billsCreated, ");
        query.append(" 0                                    AS billValueInCrores ");
        query.append(" FROM egw_mv_work_progress_register details, ");
        query.append("   egw_status status ");
        queryAppendForStatusCheck(lineEstimateRequired, query);
        query.append(filterConditions.toString());
        query.append(" GROUP BY details.departmentName ");
        query.append(" UNION ");
        query.append(" SELECT details.departmentName        AS departmentName, ");
        queryAppendForLENotRequiredCase(lineEstimateRequired, query);
        query.append(" 0                                    AS adminSanctionedEstimates, ");
        query.append(" 0                                    AS aeAdminSanctionedAmountInCrores,  ");
        query.append(" 0                                    AS workValueOfAdminSanctionedAEInCrores, ");
        query.append(" 0                                    AS technicalSanctionedEstimates, ");
        query.append(" 0                                    AS loaCreated, ");
        query.append(" 0                                    AS agreementValueInCrores, ");
        query.append(" 0                                    AS loaNotCreated, ");
        query.append(" 0                                    AS workNotCommenced, ");
        query.append(" 0                                    AS workInProgress, ");
        query.append(" 0                                    AS workCompleted , ");
        query.append(" 0                                    AS billsCreated, ");
        query.append(" 0                                    AS billValueInCrores ");
        query.append(" FROM egw_mv_work_progress_register details, ");
        query.append(" egw_status status ");
        query.append(" WHERE details.estimatestatuscode = status.code ");
        query.append(" AND status.code       IN ('ADMIN_SANCTIONED') ");
        query.append(filterConditions.toString());
        query.append(" GROUP BY details.departmentName ");
        query.append(" UNION ");
        query.append(" SELECT details.departmentName AS departmentName, ");
        queryAppendForLENotRequiredCase(lineEstimateRequired, query);
        query.append(" 0                           AS adminSanctionedEstimates, ");
        query.append(" 0                           AS aeAdminSanctionedAmountInCrores,  ");
        query.append(" 0                           AS workValueOfAdminSanctionedAEInCrores, ");
        query.append(" 0                           AS technicalSanctionedEstimates, ");
        query.append(" 0                           AS loaCreated, ");
        query.append(" 0                           AS agreementValueInCrores, ");
        query.append(" 0                           AS loaNotCreated, ");
        query.append(" 0                           AS workNotCommenced, ");
        query.append(" 0                           AS workInProgress, ");
        query.append(" 0                           AS workCompleted , ");
        query.append(" 0                           AS billsCreated, ");
        query.append(" 0                           AS billValueInCrores ");
        query.append(" FROM egw_mv_work_progress_register details, ");
        query.append(" egw_status status ");
        query.append(" WHERE details.estimatestatuscode = status.code ");
        if (lineEstimateRequired)
            query.append(" AND status.code       IN ('ADMIN_SANCTIONED','TECH_SANCTIONED') ");
        else
            query.append(" AND status.code       IN (" + ABSTRACTESTIMATE_STATUS + ") ");
        query.append(filterConditions.toString());
        query.append(" GROUP BY details.departmentName ");
        query.append(" UNION ");
        query.append(" SELECT details.departmentName         AS departmentName, ");
        queryAppendForLENotRequiredCase(lineEstimateRequired, query);
        query.append(" 0                                     AS adminSanctionedEstimates, ");
        query.append(" 0                                     AS aeAdminSanctionedAmountInCrores,  ");
        query.append(" 0                                     AS workValueOfAdminSanctionedAEInCrores, ");
        query.append(" 0                                     AS technicalSanctionedEstimates, ");
        queryAppendForLoaCreated(lineEstimateRequired, query);
        query.append(" SUM(details.agreementamount)/10000000 AS agreementValueInCrores, ");
        query.append(" 0                                     AS loaNotCreated, ");
        query.append(" 0                                     AS workNotCommenced, ");
        query.append(" 0                                     AS workInProgress, ");
        query.append(" 0                                     AS workCompleted, ");
        query.append(" 0                                     AS billsCreated, ");
        query.append(" 0                                     AS billValueInCrores ");
        query.append(" FROM egw_mv_work_progress_register details ");
        query.append(" WHERE details.agreementnumber IS NOT NULL ");
        query.append(" AND details.wostatuscode       = " + ABSTRACTESTIMATE_STATUS + " ");
        query.append(filterConditions.toString());
        query.append(" GROUP BY details.departmentName ");
        query.append(" UNION ");
        query.append(" SELECT details.departmentName         AS departmentName, ");
        queryAppendForLENotRequiredCase(lineEstimateRequired, query);
        query.append(" 0                                     AS adminSanctionedEstimates, ");
        query.append(" 0                                     AS aeAdminSanctionedAmountInCrores,  ");
        query.append(" 0                                     AS workValueOfAdminSanctionedAEInCrores, ");
        query.append(" 0                                     AS technicalSanctionedEstimates, ");
        query.append(" 0                                     AS loaCreated, ");
        query.append(" 0                                     AS agreementValueInCrores, ");
        if (lineEstimateRequired)
            query.append(" COUNT(details.ledid)                  AS loaNotCreated, ");
        else
            query.append(" COUNT(details.aeid)                  AS loaNotCreated, ");
        query.append(" 0                                     AS workNotCommenced, ");
        query.append(" 0                                     AS workInProgress, ");
        query.append(" 0                                     AS workCompleted, ");
        query.append(" 0                                     AS billsCreated, ");
        query.append(" 0                                     AS billValueInCrores ");
        query.append(" FROM egw_mv_work_progress_register details ");
        query.append(" WHERE details.workstatus       = '" + WorkStatus.LOA_Not_Created.toString() + "' ");
        query.append(filterConditions.toString());
        query.append(" GROUP BY details.departmentName ");
        query.append(" UNION ");
        query.append(" SELECT details.departmentName         AS departmentName, ");
        queryAppendForLENotRequiredCase(lineEstimateRequired, query);
        query.append(" 0                                     AS adminSanctionedEstimates, ");
        query.append(" 0                                     AS aeAdminSanctionedAmountInCrores,  ");
        query.append(" 0                                     AS workValueOfAdminSanctionedAEInCrores, ");
        query.append(" 0                                     AS technicalSanctionedEstimates, ");
        query.append(" 0                                     AS loaCreated, ");
        query.append(" 0                                     AS agreementValueInCrores, ");
        query.append(" 0                                     AS loaNotCreated, ");
        queryAppendForWorkNotCommenced(lineEstimateRequired, query);
        query.append(" 0                                     AS workInProgress, ");
        query.append(" 0                                     AS workCompleted, ");
        query.append(" 0                                     AS billsCreated, ");
        query.append(" 0                                     AS billValueInCrores ");
        query.append(" FROM egw_mv_work_progress_register details ");
        query.append(" WHERE details.workstatus       = '" + WorkStatus.Not_Commenced.toString() + "' ");
        query.append(filterConditions.toString());
        query.append(" GROUP BY details.departmentName ");
        query.append(" UNION ");
        query.append(" SELECT details.departmentName AS departmentName, ");
        queryAppendForLENotRequiredCase(lineEstimateRequired, query);
        query.append(" 0                             AS adminSanctionedEstimates, ");
        query.append(" 0                             AS aeAdminSanctionedAmountInCrores,  ");
        query.append(" 0                             AS workValueOfAdminSanctionedAEInCrores, ");
        query.append(" 0                             AS technicalSanctionedEstimates, ");
        query.append(" 0                             AS loaCreated, ");
        query.append(" 0                             AS agreementValueInCrores, ");
        query.append(" 0                             AS loaNotCreated, ");
        query.append(" 0                             AS workNotCommenced, ");
        if (lineEstimateRequired)
            query.append(" COUNT(DISTINCT details.ledid) AS workInProgress, ");
        else
            query.append(" COUNT(DISTINCT details.aeid)  AS workInProgress, ");
        query.append(" 0                             AS workCompleted, ");
        query.append(" 0                             AS billsCreated, ");
        query.append(" 0                             AS billValueInCrores ");
        query.append(" FROM egw_mv_work_progress_register details ");
        query.append(" WHERE details.workstatus       = '" + WorkStatus.In_Progress.toString() + "' ");
        query.append(filterConditions.toString());
        query.append(" GROUP BY details.departmentName ");
        query.append(" UNION ");
        query.append(" SELECT details.departmentName AS departmentName, ");
        queryAppendForLENotRequiredCase(lineEstimateRequired, query);
        query.append(" 0                             AS adminSanctionedEstimates, ");
        query.append(" 0                             AS aeAdminSanctionedAmountInCrores,  ");
        query.append(" 0                             AS workValueOfAdminSanctionedAEInCrores, ");
        query.append(" 0                             AS technicalSanctionedEstimates, ");
        query.append(" 0                             AS loaCreated, ");
        query.append(" 0                             AS agreementValueInCrores, ");
        query.append(" 0                             AS loaNotCreated, ");
        query.append(" 0                             AS workNotCommenced, ");
        query.append(" 0                             AS workInProgress, ");
        queryAppendForWorkCompleted(lineEstimateRequired, query);
        query.append(" 0                             AS billsCreated, ");
        query.append(" 0                             AS billValueInCrores ");
        query.append(" FROM egw_mv_work_progress_register details ");
        query.append(" WHERE details.workstatus       = '" + WorkStatus.Completed.toString() + "' ");
        query.append(filterConditions.toString());
        query.append(" GROUP BY details.departmentName ");
        query.append(" UNION ");
        query.append(" SELECT details.departmentName       AS departmentName, ");
        queryAppendForLENotRequiredCase(lineEstimateRequired, query);
        query.append(" 0                                   AS adminSanctionedEstimates, ");
        query.append(" 0                                   AS aeAdminSanctionedAmountInCrores,  ");
        query.append(" 0                                   AS workValueOfAdminSanctionedAEInCrores, ");
        query.append(" 0                                   AS technicalSanctionedEstimates, ");
        query.append(" 0                                   AS loaCreated, ");
        query.append(" 0                                   AS agreementValueInCrores, ");
        query.append(" 0                                   AS loaNotCreated, ");
        query.append(" 0                                   AS workNotCommenced, ");
        query.append(" 0                                   AS workInProgress, ");
        query.append(" 0                                   AS workCompleted , ");
        query.append(" COUNT(DISTINCT billdetail.billid)   AS billsCreated, ");
        query.append(" SUM(billdetail.billamount)/10000000 AS billValueInCrores ");
        query.append(" FROM egw_mv_work_progress_register details , ");
        query.append(" egw_mv_billdetail billdetail ");
        queryAppendForBillDetailLENotRequiredCase(lineEstimateRequired, query);
        query.append(filterConditions.toString());
        query.append(" GROUP BY details.departmentName ");
        query.append(" ) final ");
        query.append(" GROUP BY departmentname  ");
        return query.toString();
    }

    private void queryAppendForWorkNotCommenced(final boolean lineEstimateRequired, final StringBuilder query) {
        if (lineEstimateRequired)
            query.append(" COUNT(details.ledid)                  AS workNotCommenced, ");
        else
            query.append(" COUNT(details.aeid)                  AS workNotCommenced, ");
    }

    private void queryAppendForLoaCreated(final boolean lineEstimateRequired, final StringBuilder query) {
        if (lineEstimateRequired)
            query.append(" COUNT(details.ledid)                  AS loaCreated, ");
        else
            query.append(" COUNT(details.aeid)                  AS loaCreated, ");
    }

    private void queryAppendForBillDetailLENotRequiredCase(final boolean lineEstimateRequired,
            final StringBuilder query) {
        if (lineEstimateRequired)
            query.append(" WHERE billdetail.ledid = details.ledid ");
        else
            query.append(" WHERE billdetail.aeid = details.aeid ");
    }

    private void queryAppendForLENotRequiredCase(final boolean lineEstimateRequired, final StringBuilder query) {
        if (lineEstimateRequired) {
            query.append(" 0                                    AS lineEstimates, ");
            query.append(" 0                                    AS lineEstimateDetails, ");
            query.append(" 0                                    AS leAdminSanctionedAmountInCrores, ");
        } else
            query.append(" 0                                AS abstractEstimates ,  ");
    }

    private String getQueryForTypeOfWorkWiseReport(final EstimateAbstractReport estimateAbstractReport) {
        final boolean lineEstimateRequired = worksApplicationProperties.lineEstimateRequired();
        final StringBuilder filterConditions = new StringBuilder();
        final StringBuilder selectQuery = new StringBuilder();
        final StringBuilder groupByQuery = new StringBuilder();
        final StringBuilder mainSelectQuery = new StringBuilder();
        final StringBuilder mainGroupByQuery = new StringBuilder();
        if (checkEstimateAbstractReportDepartments(estimateAbstractReport)) {
            filterConditions.append(" AND details.department in ( :departmentIds ) ");

            selectQuery.append(" SELECT details.typeOfWorkName       AS typeOfWorkName,  ");
            selectQuery.append(" details.subTypeOfWorkName         AS subTypeOfWorkName,  ");
            selectQuery.append(" details.departmentName         AS departmentName,  ");

            mainSelectQuery.append(" SELECT typeOfWorkName       AS typeOfWorkName,  ");
            mainSelectQuery.append(" subTypeOfWorkName         AS subTypeOfWorkName,  ");
            mainSelectQuery.append(" departmentName         AS departmentName,  ");

            groupByQuery.append(" GROUP BY details.typeOfWorkName,details.subTypeOfWorkName,details.departmentName");
            mainGroupByQuery.append(" GROUP BY typeofworkname,subtypeofworkname,departmentname ");
        } else {
            selectQuery.append(" SELECT details.typeOfWorkName       AS typeOfWorkName,  ");
            selectQuery.append(" details.subTypeOfWorkName         AS subTypeOfWorkName,  ");

            mainSelectQuery.append(" SELECT typeOfWorkName       AS typeOfWorkName,  ");
            mainSelectQuery.append(" subTypeOfWorkName         AS subTypeOfWorkName,  ");

            groupByQuery.append(" GROUP BY details.typeOfWorkName,details.subTypeOfWorkName ");

            mainGroupByQuery.append(" GROUP BY typeofworkname,subtypeofworkname ");
        }

        setFilterConditionsForTypeOfWorkWise(estimateAbstractReport, filterConditions);
        final StringBuilder query = new StringBuilder();
        query.append(mainSelectQuery.toString());
        queryAppendForEstimateReportLENotRequiredCase(lineEstimateRequired, query);
        query.append(" SUM(adminSanctionedEstimates)        AS adminSanctionedEstimates,  ");
        query.append(" SUM(aeAdminSanctionedAmountInCrores) AS aeAdminSanctionedAmountInCrores,  ");
        query.append(" SUM(workValueOfAdminSanctionedAEInCrores) AS workValueOfAdminSanctionedAEInCrores, ");
        query.append(" SUM(technicalSanctionedEstimates)  AS technicalSanctionedEstimates,  ");
        query.append(" SUM(loaCreated)                    AS loaCreated,  ");
        query.append(" SUM(agreementValueInCrores)        AS agreementValueInCrores,  ");
        query.append(" SUM(loaNotCreated)                 AS loaNotCreated, ");
        query.append(" SUM(workNotCommenced)              AS workNotCommenced, ");
        query.append(" SUM(workInProgress)                AS workInProgress,  ");
        query.append(" SUM(workCompleted)                 AS workCompleted ,  ");
        query.append(" SUM(billsCreated)                  AS billsCreated,  ");
        query.append(" SUM(billValueInCrores)             AS billValueInCrores  ");
        query.append(" FROM  ");
        query.append(" (  ");
        query.append(selectQuery.toString());
        if (lineEstimateRequired) {
            query.append(" COUNT(DISTINCT details.leid)                 AS lineEstimates,  ");
            query.append(" COUNT(details.ledid)                         AS lineEstimateDetails,  ");
            query.append(" SUM(details.adminsanctionamount)/10000000    AS leAdminSanctionedAmountInCrores,  ");
            query.append(" SUM(details.estimatevalue)/10000000          AS aeAdminSanctionedAmountInCrores,  ");
            query.append(" COUNT(details.adminsanctionby)               AS adminSanctionedEstimates,  ");
            query.append(" COUNT(details.technicalsanctionby)           AS technicalSanctionedEstimates,  ");
            query.append(" SUM(details.workvalue)/10000000              AS workValueOfAdminSanctionedAEInCrores, ");
        } else {
            query.append(" COUNT(DISTINCT details.aeid)                 AS abstractEstimates,  ");
            query.append(" SUM(details.adminsanctionamount)/10000000    AS aeAdminSanctionedAmountInCrores,  ");
            query.append(" COUNT(details.adminsanctionby)               AS adminSanctionedEstimates,  ");
            query.append(" COUNT(details.technicalsanctionby)           AS technicalSanctionedEstimates,  ");
            query.append(" SUM(details.workvalue)/10000000              AS workValueOfAdminSanctionedAEInCrores, ");
        }
        query.append(" 0                                    AS loaCreated,  ");
        query.append(" 0                                    AS agreementValueInCrores, ");
        query.append(" 0                                    AS loaNotCreated, ");
        query.append(" 0                                    AS workNotCommenced, ");
        query.append(" 0                                    AS workInProgress, ");
        query.append(" 0                                    AS workCompleted , ");
        query.append(" 0                                    AS billsCreated, ");
        query.append(" 0                                    AS billValueInCrores ");
        query.append(" FROM egw_mv_work_progress_register details, ");
        query.append("   egw_status status ");
        queryAppendForStatusCheck(lineEstimateRequired, query);
        query.append(filterConditions.toString());
        query.append(groupByQuery.toString());
        query.append(" UNION ");
        query.append(selectQuery.toString());
        queryAppendForLENotRequiredCase(lineEstimateRequired, query);
        query.append(" COUNT(details.adminsanctionby)       AS adminSanctionedEstimates, ");
        query.append(" SUM(details.adminsanctionamount)/10000000  AS aeAdminSanctionedAmountInCrores,  ");
        query.append(" SUM(details.workvalue)/10000000      AS workValueOfAdminSanctionedAEInCrores, ");
        query.append(" 0                                    AS technicalSanctionedEstimates, ");
        query.append(" 0                                    AS loaCreated, ");
        query.append(" 0                                    AS agreementValueInCrores, ");
        query.append(" 0                                    AS loaNotCreated, ");
        query.append(" 0                                    AS workNotCommenced, ");
        query.append(" 0                                    AS workInProgress, ");
        query.append(" 0                                    AS workCompleted , ");
        query.append(" 0                                    AS billsCreated, ");
        query.append(" 0                                    AS billValueInCrores ");
        query.append(" FROM egw_mv_work_progress_register details, ");
        query.append(" egw_status status ");
        query.append(" WHERE details.estimatestatuscode = status.code ");
        query.append(" AND status.code       IN ('ADMIN_SANCTIONED') ");
        query.append(filterConditions.toString());
        query.append(groupByQuery.toString());
        query.append(" UNION ");
        query.append(selectQuery.toString());
        queryAppendForLENotRequiredCase(lineEstimateRequired, query);
        query.append(" 0                           AS adminSanctionedEstimates,  ");
        query.append(" 0                           AS aeAdminSanctionedAmountInCrores,  ");
        query.append(" 0                           AS workValueOfAdminSanctionedAEInCrores, ");
        query.append(" COUNT(details.estimatestatuscode)     AS technicalSanctionedEstimates, ");
        query.append(" 0                           AS loaCreated, ");
        query.append(" 0                           AS agreementValueInCrores, ");
        query.append(" 0                           AS loaNotCreated, ");
        query.append(" 0                           AS workNotCommenced, ");
        query.append(" 0                           AS workInProgress, ");
        query.append(" 0                           AS workCompleted , ");
        query.append(" 0                           AS billsCreated, ");
        query.append(" 0                           AS billValueInCrores ");
        query.append(" FROM egw_mv_work_progress_register details, ");
        query.append(" egw_status status ");
        query.append(" WHERE details.estimatestatuscode = status.code ");
        query.append(" AND status.code       IN ('ADMIN_SANCTIONED','TECH_SANCTIONED') ");
        query.append(filterConditions.toString());
        query.append(groupByQuery.toString());
        query.append(" UNION ");
        query.append(selectQuery.toString());
        queryAppendForLENotRequiredCase(lineEstimateRequired, query);
        query.append(" 0                                     AS adminSanctionedEstimates,  ");
        query.append(" 0                                     AS aeAdminSanctionedAmountInCrores,  ");
        query.append(" 0                                     AS workValueOfAdminSanctionedAEInCrores, ");
        query.append(" 0                                     AS technicalSanctionedEstimates, ");
        queryAppendForLoaCreated(lineEstimateRequired, query);
        query.append(" SUM(details.agreementamount)/10000000 AS agreementValueInCrores, ");
        query.append(" 0                                     AS loaNotCreated, ");
        query.append(" 0                                     AS workNotCommenced, ");
        query.append(" 0                                     AS workInProgress, ");
        query.append(" 0                                     AS workCompleted, ");
        query.append(" 0                                     AS billsCreated, ");
        query.append(" 0                                     AS billValueInCrores ");
        query.append(" FROM egw_mv_work_progress_register details ");
        query.append(" WHERE details.agreementnumber IS NOT NULL ");
        query.append(" AND details.wostatuscode       = " + ABSTRACTESTIMATE_STATUS + " ");
        query.append(filterConditions.toString());
        query.append(groupByQuery.toString());
        query.append(" UNION ");
        query.append(selectQuery.toString());
        queryAppendForLENotRequiredCase(lineEstimateRequired, query);
        query.append(" 0                                     AS adminSanctionedEstimates,  ");
        query.append(" 0                                     AS aeAdminSanctionedAmountInCrores,  ");
        query.append(" 0                                     AS workValueOfAdminSanctionedAEInCrores, ");
        query.append(" 0                                     AS technicalSanctionedEstimates, ");
        query.append(" 0                                     AS loaCreated, ");
        query.append(" 0                                     AS agreementValueInCrores, ");
        if (lineEstimateRequired)
            query.append(" COUNT(details.ledid)              AS loaNotCreated, ");
        else
            query.append(" COUNT(details.aeid)               AS loaNotCreated, ");
        query.append(" 0                                     AS workNotCommenced, ");
        query.append(" 0                                     AS workInProgress, ");
        query.append(" 0                                     AS workCompleted, ");
        query.append(" 0                                     AS billsCreated, ");
        query.append(" 0                                     AS billValueInCrores ");
        query.append(" FROM egw_mv_work_progress_register details ");
        query.append(" WHERE details.workstatus       = '" + WorkStatus.LOA_Not_Created.toString() + "' ");
        query.append(filterConditions.toString());
        query.append(groupByQuery.toString());
        query.append(" UNION ");
        query.append(selectQuery.toString());
        queryAppendForLENotRequiredCase(lineEstimateRequired, query);
        query.append(" 0                                     AS adminSanctionedEstimates,  ");
        query.append(" 0                                     AS aeAdminSanctionedAmountInCrores,  ");
        query.append(" 0                                     AS workValueOfAdminSanctionedAEInCrores, ");
        query.append(" 0                                     AS technicalSanctionedEstimates, ");
        query.append(" 0                                     AS loaCreated, ");
        query.append(" 0                                     AS agreementValueInCrores, ");
        query.append(" 0                                     AS loaNotCreated, ");
        queryAppendForWorkNotCommenced(lineEstimateRequired, query);
        query.append(" 0                                     AS workInProgress, ");
        query.append(" 0                                     AS workCompleted, ");
        query.append(" 0                                     AS billsCreated, ");
        query.append(" 0                                     AS billValueInCrores ");
        query.append(" FROM egw_mv_work_progress_register details ");
        query.append(" WHERE details.workstatus       = '" + WorkStatus.Not_Commenced.toString() + "' ");
        query.append(filterConditions.toString());
        query.append(groupByQuery.toString());
        query.append(" UNION ");
        query.append(selectQuery.toString());
        queryAppendForLENotRequiredCase(lineEstimateRequired, query);
        query.append(" 0                                     AS adminSanctionedEstimates,  ");
        query.append(" 0                                     AS aeAdminSanctionedAmountInCrores,  ");
        query.append(" 0                                     AS workValueOfAdminSanctionedAEInCrores, ");
        query.append(" 0                                     AS technicalSanctionedEstimates, ");
        query.append(" 0                                     AS loaCreated, ");
        query.append(" 0                                     AS agreementValueInCrores, ");
        query.append(" 0                                     AS loaNotCreated, ");
        query.append(" 0                                     AS workNotCommenced, ");
        if (lineEstimateRequired)
            query.append(" COUNT(details.ledid)              AS workInProgress, ");
        else
            query.append(" COUNT(details.aeid)               AS workInProgress, ");
        query.append(" 0                                     AS workCompleted, ");
        query.append(" 0                                     AS billsCreated, ");
        query.append(" 0                                     AS billValueInCrores ");
        query.append(" FROM egw_mv_work_progress_register details ");
        query.append(" WHERE details.workstatus       = '" + WorkStatus.In_Progress.toString() + "' ");
        query.append(filterConditions.toString());
        query.append(groupByQuery.toString());
        query.append(" UNION ");
        query.append(selectQuery.toString());
        queryAppendForLENotRequiredCase(lineEstimateRequired, query);
        query.append(" 0                             AS adminSanctionedEstimates,  ");
        query.append(" 0                             AS aeAdminSanctionedAmountInCrores,  ");
        query.append(" 0                             AS workValueOfAdminSanctionedAEInCrores, ");
        query.append(" 0                             AS technicalSanctionedEstimates, ");
        query.append(" 0                             AS loaCreated, ");
        query.append(" 0                             AS agreementValueInCrores, ");
        query.append(" 0                             AS loaNotCreated, ");
        query.append(" 0                             AS workNotCommenced, ");
        query.append(" 0                             AS workInProgress, ");
        queryAppendForWorkCompleted(lineEstimateRequired, query);
        query.append(" 0                             AS billsCreated, ");
        query.append(" 0                             AS billValueInCrores ");
        query.append(" FROM egw_mv_work_progress_register details ");
        query.append(" WHERE details.workstatus = '" + WorkStatus.Completed.toString() + "' ");
        query.append(filterConditions.toString());
        query.append(groupByQuery.toString());
        query.append(" UNION ");
        query.append(selectQuery.toString());
        queryAppendForLENotRequiredCase(lineEstimateRequired, query);
        query.append(" 0                                   AS adminSanctionedEstimates,  ");
        query.append(" 0                                   AS aeAdminSanctionedAmountInCrores,  ");
        query.append(" 0                                   AS workValueOfAdminSanctionedAEInCrores, ");
        query.append(" 0                                   AS technicalSanctionedEstimates, ");
        query.append(" 0                                   AS loaCreated, ");
        query.append(" 0                                   AS agreementValueInCrores, ");
        query.append(" 0                                   AS loaNotCreated, ");
        query.append(" 0                                   AS workNotCommenced, ");
        query.append(" 0                                   AS workInProgress, ");
        query.append(" 0                                   AS workCompleted , ");
        query.append(" COUNT(DISTINCT billdetail.billid)   AS billsCreated, ");
        query.append(" SUM(billdetail.billamount)/10000000 AS billValueInCrores ");
        query.append(" FROM egw_mv_work_progress_register details , ");
        query.append(" egw_mv_billdetail billdetail ");
        queryAppendForBillDetailLENotRequiredCase(lineEstimateRequired, query);
        query.append(filterConditions.toString());
        query.append(groupByQuery.toString());
        query.append(" ) final ");
        query.append(mainGroupByQuery.toString());
        return query.toString();
    }

    private void queryAppendForStatusCheck(final boolean lineEstimateRequired, final StringBuilder query) {
        if (lineEstimateRequired) {
            query.append(" WHERE details.lineestimatestatus = status.code ");
            query.append(" AND status.code       IN (" + LINEESTIMATE_STATUS + ")");
        } else {
            query.append(" WHERE details.estimatestatuscode = status.code ");
            query.append(" AND status.code       IN (" + ABSTRACTESTIMATE_STATUS
                    + ") AND status.moduletype = 'AbstractEstimate'");
        }
    }

    private void queryAppendForWorkCompleted(final boolean lineEstimateRequired, final StringBuilder query) {
        if (lineEstimateRequired)
            query.append(" COUNT(DISTINCT details.ledid) AS workCompleted, ");
        else
            query.append(" COUNT(DISTINCT details.aeid) AS workCompleted, ");
    }

    private void queryAppendForEstimateReportLENotRequiredCase(final boolean lineEstimateRequired,
            final StringBuilder query) {
        if (lineEstimateRequired) {
            query.append(" SUM(lineEstimates)                 AS lineEstimates ,  ");
            query.append(" SUM(lineEstimateDetails)           AS lineEstimateDetails ,  ");
            query.append(" SUM(leAdminSanctionedAmountInCrores) AS leAdminSanctionedAmountInCrores,  ");
        } else
            query.append(" SUM(abstractEstimates)           AS abstractEstimates ,  ");
    }

    private void setFilterConditionsForTypeOfWorkWise(final EstimateAbstractReport estimateAbstractReport,
            final StringBuilder filterConditions) {
        if (estimateAbstractReport != null) {

            if (estimateAbstractReport.getTypeOfWork() != null)
                filterConditions.append(" AND details.typeofwork =:typeofwork ");

            if (estimateAbstractReport.getSubTypeOfWork() != null)
                filterConditions.append(" AND details.subtypeofwork =:subtypeofwork ");

            setFilterConditionsForAdminSanctionDate(estimateAbstractReport, filterConditions);

            setFilterConditionsForSchemes(estimateAbstractReport, filterConditions);

            if (estimateAbstractReport.getWorkCategory() != null)
                filterConditions.append(" AND details.workcategory =:workcategory ");

            if (estimateAbstractReport.getBeneficiary() != null)
                filterConditions.append(" AND details.beneficiary =:beneficiary ");

            if (estimateAbstractReport.getNatureOfWork() != null)
                filterConditions.append(" AND details.natureofwork =:natureofwork ");

            if (estimateAbstractReport.isSpillOverFlag())
                filterConditions.append(" AND details.spilloverflag =:spilloverflag ");

            setFilterConditionForWorkStatus(estimateAbstractReport, filterConditions);
        }
    }

    public WorkProgressRegister getWorkProgressRegisterByLineEstimateDetailsId(final LineEstimateDetails led) {
        return workProgressRegisterRepository.findByLineEstimateDetails(led);
    }

    public WorkProgressRegister getWorkProgressRegisterByAbstractEstimate(final AbstractEstimate abstractEstimate) {
        return workProgressRegisterRepository.findByAbstractEstimate(abstractEstimate);
    }

    public List<ContractorWiseAbstractSearchResult> searchContractorWiseAbstractReport(
            final ContractorWiseAbstractReport contractorWiseAbstractReport) {
        Query query;
        query = entityManager.unwrap(Session.class)
                .createSQLQuery(getContractorListWithAllstatus(contractorWiseAbstractReport))
                .addScalar("contractorName", StringType.INSTANCE).addScalar("contractorCode", StringType.INSTANCE)
                .addScalar("electionWard", StringType.INSTANCE).addScalar("approvedEstimates", IntegerType.INSTANCE)
                .addScalar("approvedAmount", BigDecimalType.INSTANCE)
                .addScalar("siteNotHandedOverEstimates", IntegerType.INSTANCE)
                .addScalar("siteNotHandedOverAmount", BigDecimalType.INSTANCE)
                .addScalar("notWorkCommencedEstimates", IntegerType.INSTANCE)
                .addScalar("notWorkCommencedAmount", BigDecimalType.INSTANCE)
                .addScalar("workCommencedEstimates", IntegerType.INSTANCE)
                .addScalar("workCommencedAmount", BigDecimalType.INSTANCE)
                .addScalar("lagecyWorkCommencedEstimates", IntegerType.INSTANCE)
                .addScalar("lagecyWorkCommencedAmount", BigDecimalType.INSTANCE)
                .addScalar("workCompletedEstimates", IntegerType.INSTANCE)
                .addScalar("workCompletedAmount", BigDecimalType.INSTANCE)
                .addScalar("balanceWorkEstimates", IntegerType.INSTANCE)
                .addScalar("balanceWorkAmount", BigDecimalType.INSTANCE)
                .addScalar("liableAmount", BigDecimalType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(ContractorWiseAbstractSearchResult.class));
        query = setQueryParametersForContractorWiseReport(contractorWiseAbstractReport, query);
        return query.list();
    }

    private String getContractorListWithAllstatus(final ContractorWiseAbstractReport contractorWiseAbstractReport) {
        final StringBuilder filterConditions = new StringBuilder();
        if (contractorWiseAbstractReport != null) {
            if (contractorWiseAbstractReport.getFinancialYearId() != null) {
                filterConditions.append(" AND details.adminSanctionDate >= :fromDate");
                filterConditions.append(" AND details.adminSanctionDate <= :toDate ");
            }
            if (contractorWiseAbstractReport.getNatureOfWork() != null)
                filterConditions.append(" AND details.natureOfWork =:natureOfWork");
            if (StringUtils.isNotBlank(contractorWiseAbstractReport.getWorkStatus()))
                filterConditions.append(" AND details.workstatus =:workstatus ");
            if (StringUtils.isNotBlank(contractorWiseAbstractReport.getContractor()))
                filterConditions
                        .append(" AND (details.contractorname like (:contractor) OR details.contractorcode like (:contractor))");
            if (contractorWiseAbstractReport.getElectionWardId() != null)
                filterConditions.append(" AND details.ward =:ward ");
        }
        return getLOACreatedQuery(filterConditions.toString(), contractorWiseAbstractReport);
    }

    private Query setQueryParametersForContractorWiseReport(final ContractorWiseAbstractReport contractorWiseAbstractReport,
            final Query query) {
        if (contractorWiseAbstractReport != null) {
            if (contractorWiseAbstractReport.getFinancialYearId() != null) {
                final CFinancialYear finYear = cFinancialYearService.findOne(contractorWiseAbstractReport.getFinancialYearId());
                query.setParameter("fromDate", finYear.getStartingDate());
                query.setParameter("toDate", finYear.getEndingDate());
            }
            if (contractorWiseAbstractReport.getElectionWardId() != null)
                query.setParameter("ward", contractorWiseAbstractReport.getElectionWardId());
            if (contractorWiseAbstractReport.getNatureOfWork() != null)
                query.setParameter("natureOfWork", contractorWiseAbstractReport.getNatureOfWork());
            if (StringUtils.isNotBlank(contractorWiseAbstractReport.getWorkStatus()))
                query.setParameter(WORKSTATUS, contractorWiseAbstractReport.getWorkStatus());
            if (StringUtils.isNotBlank(contractorWiseAbstractReport.getContractor()))
                query.setParameter(CONTRACTOR, contractorWiseAbstractReport.getContractor());
        }

        return query;
    }

    private String getLOACreatedQuery(final String commonFilterConditions,
            final ContractorWiseAbstractReport contractorWiseAbstractReport) {
        final StringBuilder query = new StringBuilder();
        final StringBuilder groupByFilter = new StringBuilder();
        groupByFilter.append(
                " GROUP BY boundarytype.name, boundary.name,details.boundaryNum ,details.contractorName ,details.contractorCode");
        if (StringUtils.isBlank(contractorWiseAbstractReport.getContractor())) {
            query.append(" SELECT array_agg(electionWard) AS electionWard,contractorName,contractorCode,");
            query.append(" SUM(approvedEstimates)                 AS approvedEstimates ,  ");
            query.append(" SUM(approvedAmount)                    AS approvedAmount ,  ");
            query.append(" SUM(siteNotHandedOverEstimates)        AS siteNotHandedOverEstimates,  ");
            query.append(" SUM(siteNotHandedOverAmount)           AS siteNotHandedOverAmount,  ");
            query.append(" SUM(notWorkCommencedEstimates)         AS notWorkCommencedEstimates,  ");
            query.append(" SUM(notWorkCommencedAmount)            AS notWorkCommencedAmount, ");
            query.append(" SUM(workCommencedEstimates)            AS workCommencedEstimates,  ");
            query.append(" SUM(workCommencedAmount)               AS workCommencedAmount,  ");
            query.append(" SUM(workCompletedEstimates)            AS workCompletedEstimates,  ");
            query.append(" SUM(workCompletedAmount)               AS workCompletedAmount, ");
            query.append(" SUM(lagecyWorkCommencedEstimates)      AS lagecyWorkCommencedEstimates,  ");
            query.append(" SUM(lagecyWorkCommencedAmount)         AS lagecyWorkCommencedAmount, ");
            query.append(" SUM(balanceWorkEstimates)              AS balanceWorkEstimates, ");
            query.append(" SUM(balanceWorkAmount)                 AS balanceWorkAmount,  ");
            query.append(" SUM(liableAmount)                      AS liableAmount   ");
            query.append(" FROM  ");
            query.append(" (  ");
        }
        final StringBuilder selectQuery = new StringBuilder();
        selectQuery.append(
                "SELECT (CASE WHEN upper(boundarytype.name) = 'CITY' THEN boundary.name ELSE CAST(details.boundaryNum AS CHAR) END ) AS electionWard,details.contractorName as contractorName,details.contractorCode as contractorCode,");
        query.append(" SELECT electionWard,contractorName,contractorCode,");
        query.append(" SUM(approvedEstimates)                 AS approvedEstimates ,  ");
        query.append(" SUM(approvedAmount)                    AS approvedAmount ,  ");
        query.append(" SUM(siteNotHandedOverEstimates)        AS siteNotHandedOverEstimates,  ");
        query.append(" SUM(siteNotHandedOverAmount)           AS siteNotHandedOverAmount,  ");
        query.append(" SUM(notWorkCommencedEstimates)         AS notWorkCommencedEstimates,  ");
        query.append(" SUM(notWorkCommencedAmount)            AS notWorkCommencedAmount, ");
        query.append(" SUM(workCommencedEstimates)            AS workCommencedEstimates,  ");
        query.append(" SUM(workCommencedAmount)               AS workCommencedAmount,  ");
        query.append(" SUM(workCompletedEstimates)            AS workCompletedEstimates,  ");
        query.append(" SUM(workCompletedAmount)               AS workCompletedAmount, ");
        query.append(" SUM(lagecyWorkCommencedEstimates)      AS lagecyWorkCommencedEstimates,  ");
        query.append(" SUM(lagecyWorkCommencedAmount)         AS lagecyWorkCommencedAmount, ");
        query.append(" SUM(balanceWorkEstimates)              AS balanceWorkEstimates, ");
        query.append(" SUM(balanceWorkAmount)                 AS balanceWorkAmount,  ");
        query.append(" SUM(liableAmount)                      AS liableAmount   ");
        query.append(" FROM  ");
        query.append(" (  ");
        query.append(selectQuery.toString());
        query.append("COUNT(DISTINCT details.agreementnumber)       AS approvedEstimates,");
        query.append("SUM(details.agreementamount)                  AS approvedAmount,");
        query.append("0                                             AS siteNotHandedOverEstimates,");
        query.append("0                                             AS siteNotHandedOverAmount,");
        query.append("0                                             AS notWorkCommencedEstimates,");
        query.append("0                                             AS notWorkCommencedAmount,");
        query.append("0                                             AS workCommencedEstimates,");
        query.append("0                                             AS workCommencedAmount,");
        query.append("0                                             AS lagecyWorkCommencedEstimates,");
        query.append("0                                             AS lagecyWorkCommencedAmount,");
        query.append("0                                             AS workCompletedEstimates,");
        query.append("0                                             AS workCompletedAmount,");
        query.append("0                                             AS balanceWorkEstimates,");
        query.append("0                                             AS balanceWorkAmount, ");
        query.append("0                                             AS liableAmount ");
        query.append(
                " FROM egw_mv_work_progress_register details left join egw_contractor_detail cd on cd.contractor_id = details.contractor left join eg_boundary boundary on details.ward = boundary.id left join eg_boundary_type boundarytype on boundary.boundaryType = boundarytype.id ");
        query.append(" WHERE  details.woStatusCode = '" + WorksConstants.APPROVED + "' ");
        query.append(commonFilterConditions);
        query.append(groupByFilter.toString());
        query.append(" UNION ");
        query.append(selectQuery.toString());
        query.append("0                                                 AS approvedEstimates,");
        query.append("0                                                 AS approvedAmount,");
        query.append("COUNT(DISTINCT details.agreementnumber)           AS siteNotHandedOverEstimates,");
        query.append("SUM(details.agreementamount)                      AS siteNotHandedOverAmount,");
        query.append("0                                                 AS notWorkCommencedEstimates,");
        query.append("0                                                 AS notWorkCommencedAmount,");
        query.append("0                                                 AS workCommencedEstimates,");
        query.append("0                                                 AS workCommencedAmount,");
        query.append("0                                                 AS lagecyWorkCommencedEstimates,");
        query.append("0                                                 AS lagecyWorkCommencedAmount,");
        query.append("0                                                 AS workCompletedEstimates,");
        query.append("0                                                 AS workCompletedAmount,");
        query.append("0                                                 AS balanceWorkEstimates,");
        query.append("0                                                 AS balanceWorkAmount, ");
        query.append("0                                                 AS liableAmount ");
        query.append(
                " FROM egw_mv_work_progress_register details left join egw_contractor_detail cd on cd.contractor_id = details.contractor left join eg_boundary boundary on details.ward = boundary.id left join eg_boundary_type boundarytype on boundary.boundaryType = boundarytype.id ");
        query.append(" WHERE details.woStatusCode = '" + WorksConstants.APPROVED + "' ");
        query.append(" AND details.woOfflineStatusCode not in ('" + OfflineStatuses.SITE_HANDED_OVER.toString().toUpperCase()
                + "','" + OfflineStatuses.WORK_COMMENCED.toString().toUpperCase() + "') ");
        query.append(commonFilterConditions);
        query.append(groupByFilter.toString());
        query.append(" UNION ");
        query.append(selectQuery.toString());
        query.append("0                                                 AS approvedEstimates,");
        query.append("0                                                 AS approvedAmount,");
        query.append("0                                                 AS siteNotHandedOverEstimates,");
        query.append("0                                                 AS siteNotHandedOverAmount,");
        query.append("COUNT(DISTINCT details.agreementnumber)           AS notWorkCommencedEstimates,");
        query.append("SUM(details.agreementamount)                      AS notWorkCommencedAmount,");
        query.append("0                                                 AS workCommencedEstimates,");
        query.append("0                                                 AS workCommencedAmount,");
        query.append("0                                                 AS lagecyWorkCommencedEstimates,");
        query.append("0                                                 AS lagecyWorkCommencedAmount,");
        query.append("0                                                 AS workCompletedEstimates,");
        query.append("0                                                 AS workCompletedAmount,");
        query.append("0                                                 AS balanceWorkEstimates,");
        query.append("0                                                 AS balanceWorkAmount, ");
        query.append("0                                                 AS liableAmount ");
        query.append(
                " FROM egw_mv_work_progress_register details left join egw_contractor_detail cd on cd.contractor_id = details.contractor left join eg_boundary boundary on details.ward = boundary.id left join eg_boundary_type boundarytype on boundary.boundaryType = boundarytype.id ");
        query.append(" WHERE details.woStatusCode = '" + WorksConstants.APPROVED + "' ");
        query.append(" AND details.woOfflineStatusCode = '" + OfflineStatuses.SITE_HANDED_OVER.toString().toUpperCase() + "' ");
        query.append(commonFilterConditions);
        query.append(groupByFilter.toString());
        query.append(" UNION ");
        query.append(selectQuery.toString());
        query.append("0                                                 AS approvedEstimates,");
        query.append("0                                                 AS approvedAmount,");
        query.append("0                                                 AS siteNotHandedOverEstimates,");
        query.append("0                                                 AS siteNotHandedOverAmount,");
        query.append("0                                                 AS notWorkCommencedEstimates,");
        query.append("0                                                 AS notWorkCommencedAmount,");
        query.append("COUNT(DISTINCT details.agreementnumber)           AS workCommencedEstimates,");
        query.append("SUM(details.agreementamount)                      AS workCommencedAmount,");
        query.append("0                                                 AS lagecyWorkCommencedEstimates,");
        query.append("0                                                 AS lagecyWorkCommencedAmount,");
        query.append("0                                                 AS workCompletedEstimates,");
        query.append("0                                                 AS workCompletedAmount,");
        query.append("0                                                 AS balanceWorkEstimates,");
        query.append("0                                                 AS balanceWorkAmount, ");
        query.append("0                                                 AS liableAmount ");
        query.append(
                " FROM egw_mv_work_progress_register details left join egw_contractor_detail cd on cd.contractor_id = details.contractor left join eg_boundary boundary on details.ward = boundary.id left join eg_boundary_type boundarytype on boundary.boundaryType = boundarytype.id ");
        query.append(" WHERE details.woStatusCode = '" + WorksConstants.APPROVED + "' ");
        query.append(" AND details.woOfflineStatusCode ='" + OfflineStatuses.WORK_COMMENCED.toString().toUpperCase() + "' ");
        query.append(" AND (details.billtype is NULL OR details.billtype != '" + WorksConstants.FINAL_BILL + "' )");
        query.append(" AND details.boqexists = 't' ");
        query.append(commonFilterConditions);
        query.append(groupByFilter.toString());
        query.append(" UNION ");
        query.append(selectQuery.toString());
        query.append("0                                                 AS approvedEstimates,");
        query.append("0                                                 AS approvedAmount,");
        query.append("0                                                 AS siteNotHandedOverEstimates,");
        query.append("0                                                 AS siteNotHandedOverAmount,");
        query.append("0                                                 AS notWorkCommencedEstimates,");
        query.append("0                                                 AS notWorkCommencedAmount,");
        query.append("0                                                 AS workCommencedEstimates,");
        query.append("0                                                 AS workCommencedAmount,");
        query.append("COUNT(DISTINCT details.agreementnumber)           AS lagecyWorkCommencedEstimates,");
        query.append("SUM(details.agreementamount)                      AS lagecyWorkCommencedAmount,");
        query.append("0                                                 AS workCompletedEstimates,");
        query.append("0                                                 AS workCompletedAmount,");
        query.append("0                                                 AS balanceWorkEstimates,");
        query.append("0                                                 AS balanceWorkAmount, ");
        query.append("0                                                 AS liableAmount ");
        query.append(
                " FROM egw_mv_work_progress_register details left join egw_contractor_detail cd on cd.contractor_id = details.contractor left join eg_boundary boundary on details.ward = boundary.id left join eg_boundary_type boundarytype on boundary.boundaryType = boundarytype.id ");
        query.append(" WHERE details.woStatusCode = '" + WorksConstants.APPROVED + "' ");
        query.append(" AND details.billtype != '" + WorksConstants.FINAL_BILL + "' AND details.workstatus = '"
                + WorkStatus.In_Progress.toString() + "' ");
        query.append(" AND details.boqexists = 'f' ");
        query.append(commonFilterConditions);
        query.append(groupByFilter.toString());
        query.append(" UNION ");
        query.append(selectQuery.toString());
        query.append("0                                                 AS approvedEstimates,");
        query.append("0                                                 AS approvedAmount,");
        query.append("0                                                 AS siteNotHandedOverEstimates,");
        query.append("0                                                 AS siteNotHandedOverAmount,");
        query.append("0                                                 AS notWorkCommencedEstimates,");
        query.append("0                                                 AS notWorkCommencedAmount,");
        query.append("0                                                 AS workCommencedEstimates,");
        query.append("0                                                 AS workCommencedAmount,");
        query.append("0                                                 AS lagecyWorkCommencedEstimates,");
        query.append("0                                                 AS lagecyWorkCommencedAmount,");
        query.append("COUNT(DISTINCT details.agreementnumber)           AS workCompletedEstimates,");
        query.append("SUM(details.agreementamount)                      AS workCompletedAmount,");
        query.append("0                                                 AS balanceWorkEstimates,");
        query.append("0                                                 AS balanceWorkAmount, ");
        query.append("0                                                 AS liableAmount ");
        query.append(
                " FROM egw_mv_work_progress_register details left join egw_contractor_detail cd on cd.contractor_id = details.contractor left join eg_boundary boundary on details.ward = boundary.id left join eg_boundary_type boundarytype on boundary.boundaryType = boundarytype.id ");
        query.append(" WHERE details.woStatusCode = '" + WorksConstants.APPROVED + "' ");
        query.append(" AND details.billtype = '" + WorksConstants.FINAL_BILL + "' ");
        query.append(commonFilterConditions);
        query.append(groupByFilter.toString());
        query.append(" UNION ");
        query.append(selectQuery.toString());
        query.append("0                                                 AS approvedEstimates,");
        query.append("0                                                 AS approvedAmount,");
        query.append("0                                                 AS siteNotHandedOverEstimates,");
        query.append("0                                                 AS siteNotHandedOverAmount,");
        query.append("0                                                 AS notWorkCommencedEstimates,");
        query.append("0                                                 AS notWorkCommencedAmount,");
        query.append("0                                                 AS workCommencedEstimates,");
        query.append("0                                                 AS workCommencedAmount,");
        query.append("0                                                 AS lagecyWorkCommencedEstimates,");
        query.append("0                                                 AS lagecyWorkCommencedAmount,");
        query.append("0                                                 AS workCompletedEstimates,");
        query.append("0                                                 AS workCompletedAmount,");
        query.append("0                                                 AS balanceWorkEstimates,");
        query.append("0                                                 AS balanceWorkAmount, ");
        query.append("sum(details.totalbillamount)                      AS liableAmount ");
        query.append(
                " FROM egw_mv_work_progress_register details left join egw_contractor_detail cd on cd.contractor_id = details.contractor left join eg_boundary boundary on details.ward = boundary.id left join eg_boundary_type boundarytype on boundary.boundaryType = boundarytype.id ");
        query.append(" WHERE details.woStatusCode = '" + WorksConstants.APPROVED + "' ");
        query.append(" AND details.woOfflineStatusCode ='" + OfflineStatuses.WORK_COMMENCED.toString().toUpperCase() + "' ");
        query.append(" AND details.billtype != '" + WorksConstants.FINAL_BILL + "' ");
        query.append(commonFilterConditions);
        query.append(groupByFilter.toString());
        query.append(" ) final ");
        query.append(" GROUP BY electionWard,contractorName,contractorCode ");
        if (StringUtils.isBlank(contractorWiseAbstractReport.getContractor())) {
            query.append(" ) test ");
            query.append(" GROUP BY contractorName,contractorCode ");
        }
        return query.toString();
    }

    public List<String> findWorkIdentificationNumbersToSearchAbstractEstimatesForLoa(final String code) {
        return abstractEstimateRepository.findWorkIdentificationNumbersToSearchWorkProgressRegister("%" + code + "%",
                WorksConstants.APPROVED);
    }

    public List<EstimateAbstractReport> searchEstimateAbstractReportByDepartmentWiseForAE(
            final EstimateAbstractReport estimateAbstractReport) {
        Query query;
        query = entityManager.unwrap(Session.class)
                .createSQLQuery(getQueryForDepartmentWiseReport(estimateAbstractReport))
                .addScalar(DEPARTMENTNAME, StringType.INSTANCE).addScalar(ABSTRACTESTIMATES, StringType.INSTANCE)
                .addScalar(ADMINSANCTIONEDESTIMATES, StringType.INSTANCE)
                .addScalar(AEADMINSANCTIONEDAMOUNTINCRORES, StringType.INSTANCE)
                .addScalar(WORKVALUEOFADMINSANCTIONEDAEINCRORES, StringType.INSTANCE)
                .addScalar(TECHNICALSANCTIONEDESTIMATES, StringType.INSTANCE).addScalar(LOACREATED, StringType.INSTANCE)
                .addScalar(LOANOTCREATED, StringType.INSTANCE).addScalar(WORKNOTCOMMENCED, StringType.INSTANCE)
                .addScalar(AGREEMENTVALUEINCRORES, StringType.INSTANCE).addScalar(WORKINPROGRESS, StringType.INSTANCE)
                .addScalar(WORKCOMPLETED, StringType.INSTANCE).addScalar(BILLSCREATED, StringType.INSTANCE)
                .addScalar(BILLVALUEINCRORES, StringType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(EstimateAbstractReport.class));
        query = setParameterForDepartmentWiseReport(estimateAbstractReport, query);
        return query.list();
    }

    private void setFilterConditions(final EstimateAbstractReport estimateAbstractReport,
            final StringBuilder filterConditions) {
        if (estimateAbstractReport != null) {

            if (estimateAbstractReport.getDepartment() != null)
                filterConditions.append(" AND details.department =:department ");

            setFilterConditionsForAdminSanctionDate(estimateAbstractReport, filterConditions);

            setFilterConditionsForSchemes(estimateAbstractReport, filterConditions);

            if (estimateAbstractReport.getWorkCategory() != null)
                filterConditions.append(" AND details.workcategory =:workcategory ");
            if (estimateAbstractReport.getBeneficiary() != null)
                filterConditions.append(" AND details.beneficiary =:beneficiary ");

            if (estimateAbstractReport.getNatureOfWork() != null)
                filterConditions.append(" AND details.natureofwork =:natureofwork ");

            if (estimateAbstractReport.isSpillOverFlag())
                filterConditions.append(" AND details.spilloverflag =:spilloverflag ");

            setFilterConditionForWorkStatus(estimateAbstractReport, filterConditions);

        }
    }

    private void setFilterConditionForWorkStatus(final EstimateAbstractReport estimateAbstractReport,
            final StringBuilder filterConditions) {
        if (estimateAbstractReport.getWorkStatus() != null
                && !StringUtils.EMPTY.equalsIgnoreCase(estimateAbstractReport.getWorkStatus()))
            filterConditions.append(" AND details.workstatus =:workstatus ");
    }

    private void setFilterConditionsForSchemes(final EstimateAbstractReport estimateAbstractReport,
            final StringBuilder filterConditions) {
        if (estimateAbstractReport.getScheme() != null)
            filterConditions.append(" AND details.scheme =:scheme ");

        if (estimateAbstractReport.getSubScheme() != null)
            filterConditions.append(" AND details.subScheme =:subScheme ");
    }

    private void setFilterConditionsForAdminSanctionDate(final EstimateAbstractReport estimateAbstractReport,
            final StringBuilder filterConditions) {
        if (estimateAbstractReport.getAdminSanctionFromDate() != null)
            filterConditions.append(" AND details.adminsanctiondate >=:fromDate ");

        if (estimateAbstractReport.getAdminSanctionToDate() != null)
            filterConditions.append(" AND details.adminsanctiondate <=:toDate ");
    }

    @Transactional
    public List<EstimateAbstractReport> searchEstimateAbstractReportByTypeOfWorkWiseForAE(
            final EstimateAbstractReport estimateAbstractReport) {
        Query query;
        if (checkEstimateAbstractReportDepartments(estimateAbstractReport)) {
            query = entityManager.unwrap(Session.class)
                    .createSQLQuery(getQueryForTypeOfWorkWiseReport(estimateAbstractReport))
                    .addScalar(TYPEOFWORKNAME, StringType.INSTANCE).addScalar(SUBTYPEOFWORKNAME, StringType.INSTANCE)
                    .addScalar(DEPARTMENTNAME, StringType.INSTANCE).addScalar(ABSTRACTESTIMATES, StringType.INSTANCE)
                    .addScalar(ADMINSANCTIONEDESTIMATES, StringType.INSTANCE)
                    .addScalar(AEADMINSANCTIONEDAMOUNTINCRORES, StringType.INSTANCE)
                    .addScalar(WORKVALUEOFADMINSANCTIONEDAEINCRORES, StringType.INSTANCE)
                    .addScalar(TECHNICALSANCTIONEDESTIMATES, StringType.INSTANCE)
                    .addScalar(LOACREATED, StringType.INSTANCE).addScalar(AGREEMENTVALUEINCRORES, StringType.INSTANCE)
                    .addScalar(LOANOTCREATED, StringType.INSTANCE).addScalar(WORKNOTCOMMENCED, StringType.INSTANCE)
                    .addScalar(WORKINPROGRESS, StringType.INSTANCE).addScalar(WORKCOMPLETED, StringType.INSTANCE)
                    .addScalar(BILLSCREATED, StringType.INSTANCE).addScalar(BILLVALUEINCRORES, StringType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(EstimateAbstractReport.class));
            query = setParameterForTypeOfWorkWiseReport(estimateAbstractReport, query);
        } else {
            query = entityManager.unwrap(Session.class)
                    .createSQLQuery(getQueryForTypeOfWorkWiseReport(estimateAbstractReport))
                    .addScalar(TYPEOFWORKNAME, StringType.INSTANCE).addScalar(SUBTYPEOFWORKNAME, StringType.INSTANCE)
                    .addScalar(ABSTRACTESTIMATES, StringType.INSTANCE)
                    .addScalar(ADMINSANCTIONEDESTIMATES, StringType.INSTANCE)
                    .addScalar(AEADMINSANCTIONEDAMOUNTINCRORES, StringType.INSTANCE)
                    .addScalar(WORKVALUEOFADMINSANCTIONEDAEINCRORES, StringType.INSTANCE)
                    .addScalar(TECHNICALSANCTIONEDESTIMATES, StringType.INSTANCE)
                    .addScalar(LOACREATED, StringType.INSTANCE).addScalar(AGREEMENTVALUEINCRORES, StringType.INSTANCE)
                    .addScalar(LOANOTCREATED, StringType.INSTANCE).addScalar(WORKNOTCOMMENCED, StringType.INSTANCE)
                    .addScalar(WORKINPROGRESS, StringType.INSTANCE).addScalar(WORKCOMPLETED, StringType.INSTANCE)
                    .addScalar(BILLSCREATED, StringType.INSTANCE).addScalar(BILLVALUEINCRORES, StringType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(EstimateAbstractReport.class));
            query = setParameterForTypeOfWorkWiseReport(estimateAbstractReport, query);

        }
        return query.list();

    }

}
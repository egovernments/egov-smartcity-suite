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

package org.egov.works.services;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.egf.commons.EgovCommon;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.config.persistence.datasource.routing.annotation.ReadOnly;
import org.egov.infra.exception.ApplicationException;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.commons.Position;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeServiceOld;
import org.egov.works.models.tender.OfflineStatus;
import org.egov.works.models.tender.TenderResponse;
import org.egov.works.models.tender.TenderResponseContractors;
import org.egov.works.models.tender.WorksPackage;
import org.egov.works.models.workorder.WorkOrderEstimate;
import org.egov.works.utils.WorksConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * 
 * @author subhash
 *
 */
@SuppressWarnings("deprecation")
@Service
public class WorksReadOnlyService {

    private static final String STATUS_OBJECTID = "getStatusDateByObjectId_Type_Desc";

    @Autowired
    private EmployeeServiceOld employeeService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private AppConfigValueService appConfigValuesService;

    @Autowired
    @Qualifier("persistenceService")
    protected transient PersistenceService<Object, ?> persistenceService;

    @Autowired
    private FinancialYearHibernateDAO finHibernateDao;

    @Autowired
    private EgovCommon egovCommon;

    @Autowired
    private ContractorBillService contractorBillService;

    @ReadOnly
    public PersonalInformation getEmployeeforPosition(final Position position) {
        return employeeService.getEmployeeforPosition(position);
    }

    @SuppressWarnings("unchecked")
    @ReadOnly
    public TenderResponse getTenderResponse(final WorkOrderEstimate woe) {
        final List<TenderResponse> results = persistenceService.getSession()
                .createQuery("from TenderResponse tr where tr.negotiationNumber =:negotiationNumber " +
                        "and tr.egwStatus.code = :status")
                .setParameter("negotiationNumber", woe.getWorkOrder().getNegotiationNumber())
                .setParameter("status", TenderResponse.TenderResponseStatus.APPROVED.toString())
                .list();
        return results.isEmpty() ? null : results.get(0);
    }

    @ReadOnly
    public Assignment getPrimaryAssignmentForPosition(Long positionId) {
        return assignmentService.getPrimaryAssignmentForPositon(positionId);
    }

    @SuppressWarnings("unchecked")
    @ReadOnly
    public WorksPackage getWorksPackageByEstimateNo(String estimateNumber) {
        final List<WorksPackage> results = persistenceService.getSession()
                .createQuery(
                        "from WorksPackage wp where wp.id in (select wpd.worksPackage.id "
                                + "from WorksPackageDetails wpd where wpd.estimate.estimateNumber = :estimateNumber )"
                                + " and wp.egwStatus.code<>'CANCELLED'")
                .setParameter("estimateNumber", estimateNumber)
                .list();
        return results.isEmpty() ? null : results.get(0);
    }

    @ReadOnly
    public Map<String, Integer> getExceptionSOR() {
        final List<AppConfigValues> appConfigList = getAppConfigValue(WorksConstants.WORKS_MODULE_NAME, "EXCEPTIONALSOR");
        final Map<String, Integer> resultMap = new HashMap<String, Integer>();
        for (final AppConfigValues configValue : appConfigList) {
            final String value[] = configValue.getValue().split(",");
            resultMap.put(value[0], Integer.valueOf(value[1]));
        }
        return resultMap;
    }

    @ReadOnly
    public List<AppConfigValues> getAppConfigValue(final String moduleName, final String key) {
        return appConfigValuesService.getConfigValuesByModuleAndKey(moduleName, key);
    }

    @ReadOnly
    public double getTotalWorkOrderQuantity(final String negotiationNumber) {
        Object[] params = new Object[] { negotiationNumber, WorksConstants.CANCELLED_STATUS };
        Double totalWorkOrderQty = (Double) persistenceService.findByNamedQuery("getTotalQuantityForWO", params);
        params = new Object[] { negotiationNumber, WorksConstants.NEW };
        final Double totalWorkOrderQtyForNew = (Double) persistenceService.findByNamedQuery(
                "getTotalQuantityForNewWO", params);

        if (totalWorkOrderQty != null && totalWorkOrderQtyForNew != null)
            totalWorkOrderQty = totalWorkOrderQty + totalWorkOrderQtyForNew;
        if (totalWorkOrderQty == null && totalWorkOrderQtyForNew != null)
            totalWorkOrderQty = totalWorkOrderQtyForNew;
        if (totalWorkOrderQty == null)
            return 0.0d;
        else
            return totalWorkOrderQty.doubleValue();
    }

    @ReadOnly
    public OfflineStatus getOfflineStatusByObjectType(final TenderResponseContractors tenderResponseCntractrs) {
        return (OfflineStatus) persistenceService.findByNamedQuery(
                "getmaxStatusByObjectId_Type", tenderResponseCntractrs.getId(),
                tenderResponseCntractrs.getId(), TenderResponseContractors.class.getSimpleName(),
                TenderResponseContractors.class.getSimpleName());
    }

    @ReadOnly
    public String getWorksConfigValue(final String key) {
        final List<AppConfigValues> configList = getAppConfigValue(WorksConstants.WORKS_MODULE_NAME, key);
        if (!configList.isEmpty())
            return configList.get(0).getValue();
        return null;
    }

    @ReadOnly
    public String getFinancialYearRange(final Long finYearId) {
        return finHibernateDao.getFinancialYearById(
                finYearId.longValue())
                .getFinYearRange();
    }

    @SuppressWarnings("unchecked")
    @ReadOnly
    public List<Object> getTenderResponseByEstimateId(final Long estimateId) {
        return persistenceService.getSession()
                .createQuery(
                        "select wpkg,tr from TenderResponse tr,WorksPackage wpkg left outer join wpkg.worksPackageDetails wpkgd "
                                + "where tr.tenderEstimate.worksPackage.id=wpkg.id and wpkgd.estimate.id=:estimateId")
                .setParameter("estimateId", estimateId)
                .list();
    }

    @ReadOnly
    public BigDecimal getPaymentAmountByBillRegisterId(Long id) throws ApplicationException {
        return egovCommon.getPaymentAmount(id);
    }

    @ReadOnly
    public BigDecimal getNetPayableAmountForGlCodeId(Long id) throws ApplicationException {
        return contractorBillService.getNetPayableAmountForGlCodeId(id);
    }

    @ReadOnly
    public String getBillType() {
        return (String) contractorBillService.getBillType().get(1);
    }

    @ReadOnly
    public OfflineStatus getStatusDateByObjectIdTypeDesc(Long id, String objectType, String status) {
        return (OfflineStatus) persistenceService.findByNamedQuery(
                STATUS_OBJECTID, id, objectType, status);
    }

    @ReadOnly
    public Double getAssignedQuantity(final String query, Object... params) {
        return (Double) persistenceService.findByNamedQuery(query, params);
    }
}

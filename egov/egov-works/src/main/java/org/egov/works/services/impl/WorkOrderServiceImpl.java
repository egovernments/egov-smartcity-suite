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
package org.egov.works.services.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.commons.CFinancialYear;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.utils.DateUtils;
import org.egov.infstr.services.PersistenceService;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.models.masters.Contractor;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.models.tender.EstimateLineItemsForWP;
import org.egov.works.models.tender.TenderResponse;
import org.egov.works.models.tender.TenderResponseActivity;
import org.egov.works.models.tender.WorksPackage;
import org.egov.works.models.workorder.AbstractEstimateForWp;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.models.workorder.WorkOrderActivity;
import org.egov.works.models.workorder.WorkOrderEstimate;
import org.egov.works.models.workorder.WorkOrderNumberGenerator;
import org.egov.works.revisionestimate.entity.enums.RevisionType;
import org.egov.works.services.MeasurementBookService;
import org.egov.works.services.WorkOrderService;
import org.egov.works.services.WorksPackageService;
import org.egov.works.services.WorksService;
import org.egov.works.utils.WorksConstants;

@SuppressWarnings("deprecation")
public class WorkOrderServiceImpl extends BaseServiceImpl<WorkOrder, Long> implements WorkOrderService {
    private static final Logger logger = Logger.getLogger(WorkOrderServiceImpl.class);

    private PersistenceService<Contractor, Long> contractorService;
    private WorksService worksService;
    private MeasurementBookService measurementBookService;
    private WorkOrderNumberGenerator workOrderNumberGenerators;

    // Search parameters
    public static final String CONTRACTOR_ID = "CONTRACTOR_ID";
    public static final String CREATE_DATE = "CREATE_DATE";
    public static final String FROM_DATE = "FROM_DATE";
    public static final String TO_DATE = "TO_DATE";
    public static final String STATUS = "STATUS";
    public static final String TENDER_NO = "TENDER_NO";
    public static final String WORKORDER_NO = "WORKORDER_NO";
    public static final String MB_CREATION = "MB_CREATION";
    public static final String WORKORDER_ID = "WORKORDER_ID";
    public static final String WORKORDER_ESTIMATE_ID = "WORKORDER_ESTIMATE_ID";
    public static final String PROJECT_CODE = "PROJECT_CODE";
    public static final String ACTIVITY_DESC = "ACTIVITY_DESC";
    public static final String ACTIVITY_CODE = "ACTIVITY_CODE";
    public static final String ACTION_FLAG = "ACTION_FLAG";
    public static final String ESTIMATE_NO = "ESTIMATE_NO";
    public static final String WP_NO = "WP_NO";
    public static final String TENDER_FILE_NO = "TENDER_FILE_NO";
    public static final String REVISION_TYPE = "REVISION_TYPE";
    public static final String SOURCEPAGE = "sourcepage";
    public static final String CANCELWO = "cancelWO";

    private WorksPackageService workspackageService;

    @PersistenceContext
    private EntityManager entityManager;

    public WorkOrderServiceImpl(final PersistenceService<WorkOrder, Long> persistenceService) {
        super(persistenceService);
    }

    @Override
    public Double getSecurityDepositConfValue() {
        final String securityDepConfValue = worksService.getWorksConfigValue("SECURITY_DEPOSIT_MULTIPLIER");
        if (StringUtils.isNotBlank(securityDepConfValue))
            return Double.valueOf(securityDepConfValue);
        return 0.0;
    }

    @Override
    public Double getLabourWelfareFundConfValue() {
        final String labourWelfareConfValue = worksService.getWorksConfigValue("LWF_MULTIPLIER");
        if (StringUtils.isNotBlank(labourWelfareConfValue))
            return Double.valueOf(labourWelfareConfValue);
        return 0.0;
    }

    /**
     * This method will set workorder number to the work order object
     *
     * @param entity
     * @param workOrder
     */
    @Override
    public WorkOrder setWorkOrderNumber(final AbstractEstimate abstractEstimate, final WorkOrder workOrder,
            final WorksPackage worksPackage) {
        final CFinancialYear financialYear = getCurrentFinancialYear(workOrder.getWorkOrderDate());
        if (workOrder.getWorkOrderNumber() == null)
            workOrder.setWorkOrderNumber(workOrderNumberGenerators.getWorkOrderNumberGenerator(abstractEstimate,
                    financialYear, worksPackage, workOrder, persistenceService));
        return workOrder;
    }

    /**
     * This method will return all the contractors which are having active work orders.
     *
     * @return List of ContractorDetail
     */
    @Override
    public List<Contractor> getContractorsWithWO() {
        logger.info("-------------------------Inside getContractorsWithWO---------------------");
        List<Contractor> contractorList = null;

        contractorList = contractorService.findAllByNamedQuery("getContractorsWithWO");

        return contractorList;
    }

    /**
     * This method will search list of WO's for the given criteria and eligible for MB. CriteriaMap will
     * have:CONTRACTOR_ID,CREATE_DATE,TENDER_NO,WORKORDER_NO Filter: 1)isApprovalLimitReachedForWO 2)isMBCreatedAndPendingForWO
     * 3)isFinalBillApprovedForWO
     *
     * @param criteriaMap
     * @return
     */
    @Override
    public List<WorkOrder> searchWOForMB(final Map<String, Object> criteriaMap) {
        logger.info("---------------------------Inside searchWOForMB----------------------------");
        final List<WorkOrder> filteredList = new ArrayList<>();
        criteriaMap.put(ACTION_FLAG, "searchWOForMB");
        // Filter list for approval limit
        for (final WorkOrder workorder : searchWO(criteriaMap))
            if (!isApprovalLimitReachedForWO(workorder.getId()))
                filteredList.add(workorder);

        return filteredList;

    }

    /**
     * This method will search list of WO's for the given criteria and eligible for MB. CriteriaMap will
     * have:CONTRACTOR_ID,CREATE_DATE,TENDER_NO,WORKORDER_NO,PROJECT_CODE Filter: 1)An existing bill with status in "New" or
     * "approval pending" or "Rejected will NOT be retrieved 2)Work orders for which the final bill is generated will NOT be
     * retrieved for selection in the search result set. 2)Work orders with existing bill with status " Approved" with no existing
     * bill can be retrieved for selection
     *
     * @param criteriaMap
     * @return
     */
    @Override
    public List<WorkOrder> searchWOForBilling(final Map<String, Object> criteriaMap) {
        logger.debug("-------------------------Inside searchWOForBilling-----------------------");
        final List<WorkOrder> filteredList = new ArrayList<>();
        criteriaMap.put(ACTION_FLAG, "searchWOForBilling");
        // Filter list for approval limit
        for (final WorkOrder workorder : searchWO(criteriaMap))
            if (!isWOValidforBill(workorder.getId()))
                filteredList.add(workorder);
        return filteredList;

    }

    /**
     * This method will search list of WO's for the given criteria and eligible to be view. CriteriaMap may
     * have:CONTRACTOR_ID,FROM_DATE,TO_DATE,WORKORDER_NO,STATUS
     *
     * @param criteriaMap
     * @return List<WorkOrder>
     */
    @Override
    public List<String> searchWOToPaginatedView(final Map<String, Object> criteriaMap, final List<Object> paramList) {
        logger.info("-------------------------Inside searchWOToView-----------------------");

        return searchWOQuery(criteriaMap, paramList);
    }

    /**
     * Copy of searchWO using for Pagination This method will search list of WO's for the given criteria. CriteriaMap will
     * have:CONTRACTOR_ID,CREATE_DATE,TENDER_NO,WORKORDER_NO,PROJECT_CODE Date of creation ~ WorkOrder.workOrderDate Contractor ~
     * WorkOrder.contractor Tender number ~ WorkOrder.abstractEstimate ~ TenderEstimate.tendernumber Work order number ~
     * WorkOrder.workOrderNumber Project code ~ WorkOrder.AbstractEstimate.projectCode. approved quantity ~
     * WorkOrder.WorkOrderActivity.approvedQuantity a pre-defined % ~ APP_CONFIG line items in a work order ~
     * WorkOrder.WorkOrderActivity final bill is approved MB is in a "approval pending" ~ MBHeader.currentStat bill with status
     * "New" or "approval pending" or "Rejected" final bill is generated existing bill with status "Approved"
     *
     * @param criteriaMap
     * @return
     */

    public List<String> searchWOQuery(final Map<String, Object> criteriaMap, final List<Object> paramList) {
        logger.info("-------------------------Inside searchWO---------------------------------");
        final List<String> qryList = new ArrayList<>();
        final StringBuffer commonQueryFilter = new StringBuffer();
        int index = 1;
        // this String is the return Countqruery
        final StringBuffer CountQry = new StringBuffer(" select count(distinct wo)")
                .append(" from WorkOrder wo left join wo.workOrderEstimates workOrderEstimate")
                .append(" where wo.id is not null and wo.parent is null and wo.egwStatus.code<>'NEW' ");

        final StringBuffer dynQuery = new StringBuffer("select distinct wo")
                .append(" from WorkOrder wo left join wo.workOrderEstimates workOrderEstimate")
                .append(" where wo.id is not null and wo.parent is null and wo.egwStatus.code<>'NEW' ");
        final String setStat = worksService.getWorksConfigValue("WorkOrder.setstatus");
        if (criteriaMap.get(STATUS) != null)
            if (criteriaMap.get(STATUS).equals("APPROVED") || criteriaMap.get(STATUS).equals("CANCELLED")) {
                if (criteriaMap.get(SOURCEPAGE) != null && CANCELWO.equals(criteriaMap.get(SOURCEPAGE))) {
                    commonQueryFilter.append(" and wo.egwStatus.code = ? ").append(index++);
                    paramList.add(criteriaMap.get(STATUS));
                } else if (criteriaMap.get(STATUS).equals("APPROVED")) {
                    commonQueryFilter.append(" and wo.egwStatus.code = ?").append(index++)
                            .append(" and wo.id not in (select objectId from OfflineStatus where objectType = ?").append(index++)
                            .append(")");
                    paramList.add(criteriaMap.get(STATUS));
                    paramList.add("WorkOrder");
                } else if (criteriaMap.get(STATUS).equals("CANCELLED")) {
                    commonQueryFilter.append(" and wo.egwStatus.code = ?").append(index++);
                    paramList.add(criteriaMap.get(STATUS));
                }
            } else if (!criteriaMap.get(STATUS).equals("-1")
                    && Arrays.asList(setStat.split(",")).contains(criteriaMap.get(STATUS))) {
                commonQueryFilter.append(" and wo.egwStatus.code = 'APPROVED' and wo.id in(select stat.objectId")
                        .append(" from OfflineStatus stat where stat.egwStatus.code = ?").append(index++)
                        .append(" and stat.id = (select max(stat1.id) from OfflineStatus stat1")
                        .append(" where wo.id=stat1.objectId and stat1.objectType = ?").append(index++)
                        .append(") and stat.objectType = ?").append(index++)
                        .append(")");
                paramList.add(criteriaMap.get(STATUS));
                paramList.add("WorkOrder");
                paramList.add("WorkOrder");
            } else if (!criteriaMap.get(STATUS).equals("-1")
                    && !Arrays.asList(setStat.split(",")).contains(criteriaMap.get(STATUS))) {
                commonQueryFilter.append(" and wo.egwStatus.code = ?").append(index++);
                paramList.add(criteriaMap.get(STATUS));
            }
        if (criteriaMap.get(CREATE_DATE) != null) {
            commonQueryFilter.append(" and wo.workOrderDate = ?").append(index++);
            paramList.add(criteriaMap.get(CREATE_DATE));
        }

        if (criteriaMap.get(FROM_DATE) != null && criteriaMap.get(TO_DATE) == null) {
            commonQueryFilter.append(" and wo.workOrderDate >= ?").append(index++);
            paramList.add(criteriaMap.get(FROM_DATE));

        } else if (criteriaMap.get(TO_DATE) != null && criteriaMap.get(FROM_DATE) == null) {
            commonQueryFilter.append(" and wo.workOrderDate <= ?").append(index++);
            paramList.add(criteriaMap.get(TO_DATE));
        } else if (criteriaMap.get(FROM_DATE) != null && criteriaMap.get(TO_DATE) != null) {
            commonQueryFilter.append(" and wo.workOrderDate between ?").append(index++)
                    .append(" and ?").append(index++);
            paramList.add(criteriaMap.get(FROM_DATE));
            paramList.add(criteriaMap.get(TO_DATE));
        }
        if (criteriaMap.get(WORKORDER_NO) != null) {
            commonQueryFilter.append(" and UPPER(wo.workOrderNumber) like ?").append(index++);
            paramList.add("%" + criteriaMap.get(WORKORDER_NO).toString().trim().toUpperCase() + "%");
        }
        if (criteriaMap.get(WP_NO) != null) {
            commonQueryFilter.append(" and UPPER(wo.packageNumber) like ?").append(index++);
            paramList.add("%" + criteriaMap.get(WP_NO).toString().trim().toUpperCase() + "%");
        }
        if (criteriaMap.get(TENDER_FILE_NO) != null) {
            commonQueryFilter.append(" and wo.negotiationNumber in (select tr1.negotiationNumber from TenderResponse tr1")
                    .append(" where UPPER(tr1.tenderEstimate.worksPackage.tenderFileNumber) like ?").append(index++)
                    .append(" )");
            paramList.add("%" + criteriaMap.get(TENDER_FILE_NO).toString().trim().toUpperCase() + "%");
        }
        if (criteriaMap.get(CONTRACTOR_ID) != null) {
            commonQueryFilter.append(" and wo.contractor.id = ?").append(index++);
            paramList.add(criteriaMap.get(CONTRACTOR_ID));
        }
        if (criteriaMap.get("DEPT_ID") != null) {
            commonQueryFilter
                    .append(" and wo.id in (select we.workOrder.id from WorkOrderEstimate we where we.workOrder.id=wo.id and ")
                    .append(" we.estimate.executingDepartment.id = ?").append(index++)
                    .append(") ");
            paramList.add(criteriaMap.get("DEPT_ID"));
        }

        if (criteriaMap.get(ESTIMATE_NO) != null) {
            commonQueryFilter
                    .append(" and wo.id in (select we.workOrder.id from WorkOrderEstimate we where we.workOrder.id=wo.id and ")
                    .append(" UPPER(we.estimate.estimateNumber) like ?").append(index++)
                    .append(") ");
            paramList.add("%" + criteriaMap.get(ESTIMATE_NO).toString().trim().toUpperCase() + "%");
        }
        if (criteriaMap.get(TENDER_NO) != null && !"".equalsIgnoreCase((String) criteriaMap.get(TENDER_NO))) {
            logger.debug("-------TENDER_NO-----------" + criteriaMap.get(TENDER_NO));
            commonQueryFilter.append(" and UPPER(wo.tenderNumber like) ?").append(index++)
                    .append(" ) ");
            paramList.add("%" + criteriaMap.get(TENDER_NO).toString().trim().toUpperCase() + "%");
        }
        if (criteriaMap.get(PROJECT_CODE) != null) {
            commonQueryFilter
                    .append(" and wo.id in (select we.workOrder.id from WorkOrderEstimate we where we.workOrder.id=wo.id and ")
                    .append(" UPPER(we.estimate.executingDepartment.projectCode.code) like ?").append(index++)
                    .append(" ) ");
            paramList.add("%" + criteriaMap.get(PROJECT_CODE).toString().trim().toUpperCase() + "%");
        }

        if (criteriaMap.get(ACTION_FLAG) != null
                && criteriaMap.get(ACTION_FLAG).toString().equalsIgnoreCase("searchWOForMB")) {
            commonQueryFilter.append(" and workOrderEstimate.estimate.projectCode.egwStatus.code!='CLOSED'")
                    .append(" and workOrderEstimate.id not in (select distinct mbh.workOrderEstimate.id ")
                    .append(" from MBHeader mbh where mbh.egwStatus.code = ?").append(index++)
                    .append(" or mbh.egwStatus.code = ?").append(index++)
                    .append(" or mbh.egwStatus.code = ?").append(index++)
                    .append(" or mbh.egwStatus.code = ?").append(index++)
                    .append(" or mbh.egwStatus.code = ?").append(index++)
                    .append(" ) and workOrderEstimate.id not in (select distinct mbh.workOrderEstimate.id ")
                    .append(" from MBHeader mbh where mbh.egwStatus.code = ?").append(index++)
                    .append(" and mbh.egBillregister.billstatus = ?").append(index++)
                    .append(" and mbh.egBillregister.billtype = ?").append(index++)
                    .append(")");
            paramList.add(MBHeader.MeasurementBookStatus.CREATED.toString());
            paramList.add(MBHeader.MeasurementBookStatus.CHECKED.toString());
            paramList.add(MBHeader.MeasurementBookStatus.RESUBMITTED.toString());
            paramList.add(MBHeader.MeasurementBookStatus.REJECTED.toString());
            paramList.add(MBHeader.MeasurementBookStatus.NEW.toString());
            paramList.add(MBHeader.MeasurementBookStatus.APPROVED.toString());
            paramList.add(MBHeader.MeasurementBookStatus.APPROVED.toString());
            paramList.add(getFinalBillTypeConfigValue());
        }

        if (criteriaMap.get(ACTION_FLAG) != null
                && criteriaMap.get(ACTION_FLAG).toString().equalsIgnoreCase("searchWOForBilling")) {
            commonQueryFilter.append(" and workOrderEstimate.estimate.projectCode.egwStatus.code!='CLOSED'")
                    .append(" and workOrderEstimate.id not in (select distinct mbh.workOrderEstimate.id from MBHeader mbh")
                    .append(" where mbh.egwStatus.code = ?").append(index++)
                    .append(" and (mbh.egBillregister.billstatus <> ?").append(index++)
                    .append(" and mbh.egBillregister.billtype = ?").append(index++)
                    .append("))");

            paramList.add(MBHeader.MeasurementBookStatus.APPROVED.toString());
            paramList.add(MBHeader.MeasurementBookStatus.CANCELLED.toString());
            paramList.add(getFinalBillTypeConfigValue());
        }

        final String orderQry = " Order by wo.workOrderDate ";
        logger.info("Query is ::" + dynQuery);

        qryList.add(CountQry.append(commonQueryFilter).append(orderQry).toString());
        qryList.add(dynQuery.append(commonQueryFilter).append(orderQry).toString());

        return qryList;
    }

    /**
     * This method will search list of WO's for the given criteria and eligible to be view. CriteriaMap may
     * have:CONTRACTOR_ID,FROM_DATE,TO_DATE,WORKORDER_NO,STATUS
     *
     * @param criteriaMap
     * @return List<WorkOrder>
     */
    @Override
    public List<WorkOrder> searchWOToView(final Map<String, Object> criteriaMap) {
        logger.debug("-------------------------Inside searchWOToView-----------------------");

        return searchWO(criteriaMap);
    }

    /**
     * This method will search list of WO's for the given criteria. CriteriaMap will
     * have:CONTRACTOR_ID,CREATE_DATE,TENDER_NO,WORKORDER_NO,PROJECT_CODE Date of creation ~ WorkOrder.workOrderDate Contractor ~
     * WorkOrder.contractor Tender number ~ WorkOrder.abstractEstimate ~ TenderEstimate.tendernumber Work order number ~
     * WorkOrder.workOrderNumber Project code ~ WorkOrder.AbstractEstimate.projectCode. approved quantity ~
     * WorkOrder.WorkOrderActivity.approvedQuantity a pre-defined % ~ APP_CONFIG line items in a work order ~
     * WorkOrder.WorkOrderActivity final bill is approved MB is in a "approval pending" ~ MBHeader.currentStat bill with status
     * "New" or "approval pending" or "Rejected" final bill is generated existing bill with status "Approved"
     *
     * @param criteriaMap
     * @return
     */
    public List<WorkOrder> searchWO(final Map<String, Object> criteriaMap) {
        logger.info("-------------------------Inside searchWO---------------------------------");
        final StringBuffer dynQuery = new StringBuffer("select distinct wo")
                .append(" from WorkOrder wo left join wo.workOrderEstimates workOrderEstimate")
                .append(" where wo.id is not null and wo.parent is null ");
        final Map<String, Object> paramsMap = new HashMap<>();
        final String setStat = worksService.getWorksConfigValue("WorkOrder.setstatus");
        if (criteriaMap.get(STATUS) != null)
            if (criteriaMap.get(STATUS).equals("APPROVED") || criteriaMap.get(STATUS).equals("CANCELLED")) {
                if (criteriaMap.get(SOURCEPAGE) != null && CANCELWO.equals(criteriaMap.get(SOURCEPAGE))) {
                    dynQuery.append(" and wo.egwStatus.code = :woStatus ");
                    paramsMap.put("woStatus", criteriaMap.get(STATUS));
                } else {
                    dynQuery.append(" and wo.egwStatus.code = :woStatus and ")
                            .append(" wo.id not in (select objectId from OfflineStatus where objectType = :objectType)");
                    paramsMap.put("woStatus", criteriaMap.get(STATUS));
                    paramsMap.put("objectType", "WorkOrder");
                }
            } else if (!criteriaMap.get(STATUS).equals("-1")
                    && Arrays.asList(setStat.split(",")).contains(criteriaMap.get(STATUS))) {
                dynQuery.append(
                        " and wo.id in (select stat.objectId from OfflineStatus stat where stat.egwStatus.code = :osStatus")
                        .append(" and stat.id = (select max(stat1.id) from OfflineStatus stat1 where wo.id = stat1.objectId")
                        .append(" and stat1.objectType = :objectType) and stat.objectType = :objectType)");
                paramsMap.put("osStatus", criteriaMap.get(STATUS));
                paramsMap.put("objectType", "WorkOrder");
            } else if (!criteriaMap.get(STATUS).equals("-1")
                    && !Arrays.asList(setStat.split(",")).contains(criteriaMap.get(STATUS))) {
                dynQuery.append(" and wo.egwStatus.code = :woStatus");
                paramsMap.put("woStatus", criteriaMap.get(STATUS));
            }
        if (criteriaMap.get(CREATE_DATE) != null) {
            dynQuery.append(" and wo.workOrderDate = :workOrderDate ");
            paramsMap.put("workOrderDate", criteriaMap.get(CREATE_DATE));
        }

        if (criteriaMap.get(FROM_DATE) != null && criteriaMap.get(TO_DATE) == null) {
            dynQuery.append(" and wo.workOrderDate >= :workOrderFromDate ");
            paramsMap.put("workOrderFromDate", criteriaMap.get(FROM_DATE));

        } else if (criteriaMap.get(TO_DATE) != null && criteriaMap.get(FROM_DATE) == null) {
            dynQuery.append(" and wo.workOrderDate <= :workOrderToDate ");
            paramsMap.put("workOrderToDate", criteriaMap.get(TO_DATE));
        } else if (criteriaMap.get(FROM_DATE) != null && criteriaMap.get(TO_DATE) != null) {
            dynQuery.append(" and wo.workOrderDate between :workOrderFromDate and :workOrderToDate ");
            paramsMap.put("workOrderFromDate", criteriaMap.get(FROM_DATE));
            paramsMap.put("workOrderToDate", criteriaMap.get(TO_DATE));
        }
        if (criteriaMap.get(WORKORDER_NO) != null) {
            dynQuery.append(" and UPPER(wo.workOrderNumber) like :workOrderNumber ");
            paramsMap.put("workOrderNumber", "%" + criteriaMap.get(WORKORDER_NO).toString().trim().toUpperCase() + "%");
        }
        if (criteriaMap.get(WP_NO) != null) {
            dynQuery.append(" and UPPER(wo.packageNumber) like :packageNumber ");
            paramsMap.put("packageNumber", "%" + criteriaMap.get(WP_NO).toString().trim().toUpperCase() + "%");
        }
        if (criteriaMap.get(TENDER_FILE_NO) != null) {
            dynQuery.append(" and wo.negotiationNumber in (select tr1.negotiationNumber from TenderResponse tr1 where ")
                    .append(" UPPER(tr1.tenderEstimate.worksPackage.tenderFileNumber) like :tenderFileNumber )");
            paramsMap.put("tenderFileNumber", "%" + criteriaMap.get(TENDER_FILE_NO).toString().trim().toUpperCase() + "%");
        }
        if (criteriaMap.get(CONTRACTOR_ID) != null) {
            dynQuery.append(" and wo.contractor.id = :contractorId ");
            paramsMap.put("contractorId", criteriaMap.get(CONTRACTOR_ID));
        }
        if (criteriaMap.get("DEPT_ID") != null) {
            dynQuery.append(" and wo.id in (select we.workOrder.id from WorkOrderEstimate we where we.workOrder.id=wo.id and ")
                    .append(" we.estimate.executingDepartment.id = :edId) ");
            paramsMap.put("edId", criteriaMap.get("DEPT_ID"));
        }

        if (criteriaMap.get(ESTIMATE_NO) != null) {
            dynQuery.append(" and wo.id in (select we.workOrder.id from WorkOrderEstimate we where we.workOrder.id=wo.id and ")
                    .append(" UPPER(we.estimate.estimateNumber) like :estimateNumber ) ");
            paramsMap.put("estimateNumber", "%" + criteriaMap.get(ESTIMATE_NO).toString().trim().toUpperCase() + "%");
        }
        if (criteriaMap.get(TENDER_NO) != null && !"".equalsIgnoreCase((String) criteriaMap.get(TENDER_NO))) {
            logger.debug("-------TENDER_NO-----------" + criteriaMap.get(TENDER_NO));
            dynQuery.append(" and UPPER(wo.tenderNumber like) :tenderNumber ) ");
            paramsMap.put("tenderNumber", "%" + criteriaMap.get(TENDER_NO).toString().trim().toUpperCase() + "%");
        }
        if (criteriaMap.get(PROJECT_CODE) != null) {
            dynQuery.append(" and wo.id in (select we.workOrder.id from WorkOrderEstimate we where we.workOrder.id=wo.id and ")
                    .append(" UPPER(we.estimate.executingDepartment.projectCode.code) like :projectCode ) ");
            paramsMap.put("projectCode", "%" + criteriaMap.get(PROJECT_CODE).toString().trim().toUpperCase() + "%");
        }

        if (criteriaMap.get(ACTION_FLAG) != null
                && criteriaMap.get(ACTION_FLAG).toString().equalsIgnoreCase("searchWOForMB")) {
            dynQuery.append(" and workOrderEstimate.workOrder.egwStatus.code!='CANCELLED'")
                    .append(" and workOrderEstimate.estimate.projectCode.egwStatus.code!='CLOSED' and workOrderEstimate.id")
                    .append(" not in (select distinct mbh.workOrderEstimate.id ")
                    .append(" from MBHeader mbh where mbh.egwStatus.code = :mbhCreatedStatus or mbh.egwStatus.code = :mbhCheckedStatus")
                    .append(" or mbh.egwStatus.code = :mbhResubmittedStatus or mbh.egwStatus.code = :mbhRejectedStatus")
                    .append(" or mbh.egwStatus.code = :mbhNewStatus ) and workOrderEstimate.id not in (select distinct mbh.workOrderEstimate.id ")
                    .append(" from MBHeader mbh where mbh.egwStatus.code = :mbhApprovedStatus and  mbh.egBillregister.billstatus = :billStatus")
                    .append(" and mbh.egBillregister.billtype = :billtype)");
            paramsMap.put("mbhCreatedStatus", MBHeader.MeasurementBookStatus.CREATED.toString());
            paramsMap.put("mbhCheckedStatus", MBHeader.MeasurementBookStatus.CHECKED.toString());
            paramsMap.put("mbhResubmittedStatus", MBHeader.MeasurementBookStatus.RESUBMITTED.toString());
            paramsMap.put("mbhRejectedStatus", MBHeader.MeasurementBookStatus.REJECTED.toString());
            paramsMap.put("mbhNewStatus", MBHeader.MeasurementBookStatus.NEW.toString());
            paramsMap.put("mbhApprovedStatus", MBHeader.MeasurementBookStatus.APPROVED.toString());
            paramsMap.put("billStatus", MBHeader.MeasurementBookStatus.APPROVED.toString());
            paramsMap.put("billtype", getFinalBillTypeConfigValue());
        }

        if (criteriaMap.get(ACTION_FLAG) != null
                && criteriaMap.get(ACTION_FLAG).toString().equalsIgnoreCase("searchWOForBilling")) {
            dynQuery.append(" and workOrderEstimate.workOrder.egwStatus.code!='CANCELLED'")
                    .append(" and workOrderEstimate.estimate.projectCode.egwStatus.code!='CLOSED' and workOrderEstimate.id not in ")
                    .append(" (select distinct mbh.workOrderEstimate.id from MBHeader mbh where mbh.egwStatus.code = :mbhApprovedStatus ")
                    .append(" and (mbh.egBillregister.billstatus <> :cancelledBillStatus and mbh.egBillregister.billtype = :billtype))");

            paramsMap.put("mbhApprovedStatus", MBHeader.MeasurementBookStatus.APPROVED.toString());
            paramsMap.put("cancelledBillStatus", MBHeader.MeasurementBookStatus.CANCELLED.toString());
            paramsMap.put("billtype", getFinalBillTypeConfigValue());
        }

        // @Todo check action_flag

        logger.debug("Query is ::" + dynQuery);

        final TypedQuery<WorkOrder> typedQuery = entityManager.createQuery(dynQuery.toString(), WorkOrder.class);
        paramsMap.entrySet().forEach(entry -> typedQuery.setParameter(entry.getKey(), entry.getValue()));

        return typedQuery.getResultList();

    }

    /**
     * This method will check whether approval limit is already used for all line item for the WO. Sum of
     * WorkOrder.WorkOrderActivity.approvedQuantity <= ??Quantity??
     *
     * @param woId
     * @return
     */
    public Boolean isApprovalLimitReachedForWO(final Long woId) {
        Boolean result = false;
        final WorkOrder wo = findById(woId, false);
        if (measurementBookService.getWorkOrderEstimatesForMB(wo.getWorkOrderEstimates()).isEmpty())
            result = true;
        return result;
    }

    public Boolean isWOValidforBill(final Long woId) {
        Boolean result = false;
        final WorkOrder wo = findById(woId, false);
        if (measurementBookService.getWOEstForBillExludingLegacyMB(wo.getWorkOrderEstimates()).isEmpty())
            result = true;
        return result;
    }

    /**
     * This method will search and return list of woactivity based on searched criteria. Search criteria:
     * WORKORDER_NO,ACTIVITY_DESC,LINEITEM_CODE Story #567 Search Line item to record measurement
     *
     * @param criteriaMap
     * @return
     */
    @Override
    public List<WorkOrderActivity> searchWOActivities(final Map<String, Object> criteriaMap) {
        logger.info("-------------------------Inside searchWOActivities-----------------------");

        final StringBuffer dynQuery = new StringBuffer("select distinct woa")
                .append(" from WorkOrderActivity woa left join woa.activity.schedule schedule")
                .append(" left join woa.activity.nonSor nonSor where woa.id != null and woa.workOrderEstimate.estimate.parent is null ")
                .append(" and woa.workOrderEstimate.workOrder.egwStatus.code != 'CANCELLED' ");

        final Map<String, Object> paramsMap = new HashMap<>();

        if (criteriaMap.get(WORKORDER_NO) != null) {
            dynQuery.append(" and woa.workOrderEstimate.workOrder.workOrderNumber = :workOrderNumber ");
            paramsMap.put("workOrderNumber", criteriaMap.get(WORKORDER_NO));
        }
        if (criteriaMap.get(WORKORDER_ESTIMATE_ID) != null) {
            dynQuery.append(" and woa.workOrderEstimate.estimate.id = :woeId ");
            paramsMap.put("woeId", criteriaMap.get(WORKORDER_ESTIMATE_ID));
        }
        if (criteriaMap.get(ACTIVITY_DESC) != null) {
            dynQuery.append(
                    " and ((UPPER(schedule.description) like :scheduleDesc) or (UPPER(nonSor.description) like :nonSorDesc ))");
            paramsMap.put("scheduleDesc", "%" + ((String) criteriaMap.get(ACTIVITY_DESC)).toUpperCase() + "%");
            paramsMap.put("nonSorDesc", "%" + ((String) criteriaMap.get(ACTIVITY_DESC)).toUpperCase() + "%");
        }
        if (criteriaMap.get(ACTIVITY_CODE) != null) {
            dynQuery.append(" and UPPER(schedule.code) like :scheduleCode ");
            paramsMap.put("scheduleCode", "%" + ((String) criteriaMap.get(ACTIVITY_CODE)).toUpperCase() + "%");
        }
        // @Todo state not in approved and cancelled
        /*
         * dynQuery.append("and woa.id not in (select distinct mbd.workOrderActivity.id from MBDetails mbd where " +
         * "mbd.mbHeader.state.previous.value not in (?,?) and mbd.workOrderActivity.id = woa.id)" ;
         * paramsMap.put(MBHeader.MeasurementBookStatus.APPROVED.toString());
         * paramsMap.put(MBHeader.MeasurementBookStatus.CANCELLED.toString());
         */

        final Double extraPercentage = worksService.getConfigval();
        double factor = 1;
        if (extraPercentage.doubleValue() > 0)
            factor = 1 + extraPercentage / 100;
        // @Todo ignore quantity of cancelled mb
        if (!"Required".equals(worksService.getWorksConfigValue("ORDER_NUMBER_REQUIRED"))) {
            dynQuery.append(" and ((woa.approvedQuantity*:factor > (select sum(mbd.quantity) as sumq from MBDetails mbd ")
                    .append(" where mbd.mbHeader.egwStatus.code != :status group by mbd.workOrderActivity ")
                    .append(" having mbd.workOrderActivity.id = woa.id)) or (select sum(mbd.quantity) as sumq from MBDetails mbd ")
                    .append(" where mbd.mbHeader.egwStatus.code != :status group by mbd.workOrderActivity ")
                    .append(" having mbd.workOrderActivity.id = woa.id) is null)");
            paramsMap.put("factor", factor);
            paramsMap.put("status", MBHeader.MeasurementBookStatus.CANCELLED.toString());
        }

        final TypedQuery<WorkOrderActivity> typedQuery = entityManager.createQuery(dynQuery.toString(), WorkOrderActivity.class);
        paramsMap.entrySet().forEach(entry -> typedQuery.setParameter(entry.getKey(), entry.getValue()));

        return typedQuery.getResultList();
    }

    /**
     * For the purpose of change quantity in revision estimate Will get work order activity list for the original work order and
     * subsequent revision work orders It will not get the activities for which MB is present in workflow
     *
     * @param criteriaMap
     * @return
     */
    @Override
    public List<WorkOrderActivity> searchWOActivitiesForChangeQuantity(final Map<String, Object> criteriaMap) {
        logger.info("-------------------------Inside searchWOActivities-----------------------");

        final StringBuffer dynQuery = new StringBuffer("select distinct woa")
                .append(" from WorkOrderActivity woa left join woa.activity.schedule schedule")
                .append(" left join woa.activity.nonSor nonSor where woa.id is not null ");
        final Map<String, Object> paramMap = new HashMap<>();

        if (criteriaMap.get(ACTIVITY_DESC) != null) {
            dynQuery.append(
                    " and ((UPPER(schedule.description) like :scheduleDesc) or (UPPER(nonSor.description) like :nonSorDesc ))");
            paramMap.put("scheduleDesc", "%" + ((String) criteriaMap.get(ACTIVITY_DESC)).toUpperCase() + "%");
            paramMap.put("nonSorDesc", "%" + ((String) criteriaMap.get(ACTIVITY_DESC)).toUpperCase() + "%");
        }
        if (criteriaMap.get(ACTIVITY_CODE) != null) {
            dynQuery.append(" and UPPER(schedule.code) like :scheduleCode");
            paramMap.put("scheduleCode", "%" + ((String) criteriaMap.get(ACTIVITY_CODE)).toUpperCase() + "%");
        }
        if (criteriaMap.get(WORKORDER_ESTIMATE_ID) != null) {
            if (criteriaMap.get(WORKORDER_ID) != null) {
                dynQuery.append(" and (woa.workOrderEstimate.estimate.id = :estimateId")
                        .append(" and woa.workOrderEstimate.workOrder.egwStatus.code = :woStatus")
                        .append(" and woa.workOrderEstimate.workOrder.id = :woId) ");
                paramMap.put("estimateId", criteriaMap.get(WORKORDER_ESTIMATE_ID));
                paramMap.put("woStatus", WorksConstants.APPROVED);
                paramMap.put("woId", criteriaMap.get(WORKORDER_ID));
            } else {
                dynQuery.append(" and (woa.workOrderEstimate.estimate.id = :estimateId")
                        .append(" and woa.workOrderEstimate.workOrder.egwStatus.code = :woStatus) ");
                paramMap.put("estimateId", criteriaMap.get(WORKORDER_ESTIMATE_ID));
                paramMap.put("woStatus", WorksConstants.APPROVED);
            }
            dynQuery.append(" or ((woa.workOrderEstimate.estimate.egwStatus is not null")
                    .append(" and woa.workOrderEstimate.estimate.egwStatus.code = :estimateStatus)")
                    .append(" and (woa.workOrderEstimate.estimate.parent is not null")
                    .append(" and woa.workOrderEstimate.estimate.parent.id = :estParentId ))");
            paramMap.put("estimateStatus", AbstractEstimate.EstimateStatus.APPROVED.toString());
            paramMap.put("estParentId", criteriaMap.get(WORKORDER_ESTIMATE_ID));
        }
        if (criteriaMap.get(WORKORDER_ID) != null) {
            dynQuery.append(" and (woa.workOrderEstimate.workOrder.id = :woId)")
                    .append(" or ((woa.workOrderEstimate.workOrder.egwStatus is not null")
                    .append(" and woa.workOrderEstimate.workOrder.egwStatus.code = :woStatus)")
                    .append(" and (woa.workOrderEstimate.workOrder.parent is not null")
                    .append(" and woa.workOrderEstimate.workOrder.parent.id = :woParentId ))");
            paramMap.put("woId", criteriaMap.get(WORKORDER_ID));
            paramMap.put("woStatus", WorksConstants.APPROVED);
            paramMap.put("woParentId", criteriaMap.get(WORKORDER_ID));
        }

        dynQuery.append("and woa.id not in (select distinct mbd.workOrderActivity.id from MBDetails mbd where ")
                .append(" mbd.mbHeader.egwStatus.code not in (:mbhApprovedStatus, :mbhCancelledStatus) and mbd.workOrderActivity.id = woa.id)");
        paramMap.put("mbhApprovedStatus", MBHeader.MeasurementBookStatus.APPROVED.toString());
        paramMap.put("mbhCancelledStatus", MBHeader.MeasurementBookStatus.CANCELLED.toString());

        dynQuery.append(" order by woa.activity.id asc");

        final TypedQuery<WorkOrderActivity> typedQuery = entityManager.createQuery(dynQuery.toString(), WorkOrderActivity.class);
        paramMap.entrySet().forEach(entry -> typedQuery.setParameter(entry.getKey(), entry.getValue()));

        return typedQuery.getResultList();
    }

    /**
     * This method will search and return list of woactivity from only revision estimates based on searched criteria. Search
     * criteria: WORKORDER_NO,ACTIVITY_DESC,LINEITEM_CODE
     *
     * @param criteriaMap
     * @return
     */
    @Override
    public List<WorkOrderActivity> searchWOActivitiesFromRevEstimates(final Map<String, Object> criteriaMap) {
        logger.info("-------------------------Inside searchWOActivities-----------------------");
        final StringBuffer dynQuery = new StringBuffer("select distinct woa")
                .append(" from WorkOrderActivity woa left join woa.activity.schedule schedule")
                .append(" left join woa.activity.nonSor nonSor where woa.id != null ");
        final Map<String, Object> paramsMap = new HashMap<>();

        if (criteriaMap.get(WORKORDER_NO) != null) {
            dynQuery.append(" and woa.workOrderEstimate.workOrder.parent.workOrderNumber = :workOrderNumber ");
            paramsMap.put("workOrderNumber", criteriaMap.get(WORKORDER_NO));
        }
        if (criteriaMap.get(WORKORDER_ESTIMATE_ID) != null) {
            dynQuery.append(" and woa.workOrderEstimate.estimate.parent.id = :estParentId ");
            paramsMap.put("estParentId", criteriaMap.get(WORKORDER_ESTIMATE_ID));
        }
        if (criteriaMap.get(ACTIVITY_DESC) != null) {
            dynQuery.append(" and ((UPPER(schedule.description) like :scheduleDesc)")
                    .append(" or (UPPER(nonSor.description)  like :nonSorDesc ))");
            paramsMap.put("scheduleDesc", "%" + ((String) criteriaMap.get(ACTIVITY_DESC)).toUpperCase() + "%");
            paramsMap.put("nonSorDesc", "%" + ((String) criteriaMap.get(ACTIVITY_DESC)).toUpperCase() + "%");
        }
        if (criteriaMap.get(ACTIVITY_CODE) != null) {
            dynQuery.append(" and UPPER(schedule.code) like :scheduleCode ");
            paramsMap.put("scheduleCode", "%" + ((String) criteriaMap.get(ACTIVITY_CODE)).toUpperCase() + "%");
        }
        // Check Approved REs
        dynQuery.append(" and  woa.workOrderEstimate.estimate.egwStatus.code = :estimateStatus   ");
        paramsMap.put("estimateStatus", WorksConstants.APPROVED);
        if (criteriaMap.get(REVISION_TYPE) != null) {
            dynQuery.append(" and woa.activity.revisionType = :revisionType  ");
            paramsMap.put("revisionType", criteriaMap.get(REVISION_TYPE));

        }

        // @Todo state not in approved and cancelled
        /*
         * dynQuery.append("and woa.id not in (select distinct mbd.workOrderActivity.id from MBDetails mbd where " +
         * "mbd.mbHeader.state.previous.value not in (?,?) and mbd.workOrderActivity.id = woa.id)" ;
         * paramsMap.put(MBHeader.MeasurementBookStatus.APPROVED.toString());
         * paramsMap.put(MBHeader.MeasurementBookStatus.CANCELLED.toString());
         */

        final Double extraPercentage = worksService.getConfigval();
        double factor = 1;
        if (extraPercentage.doubleValue() > 0)
            factor = 1 + extraPercentage / 100;
        // @Todo ignore quantity of cancelled mb
        if (!"Required".equals(worksService.getWorksConfigValue("ORDER_NUMBER_REQUIRED"))) {
            dynQuery.append("and ((woa.approvedQuantity*:factor > (select sum(mbd.quantity) as sumq from MBDetails mbd ")
                    .append(" where mbd.mbHeader.egwStatus.code != :mbHeaderStatus group by mbd.workOrderActivity ")
                    .append(" having mbd.workOrderActivity.id = woa.id)) or (select sum(mbd.quantity) as sumq from MBDetails mbd ")
                    .append(" where mbd.mbHeader.egwStatus.code != :mbHeaderStatus group by mbd.workOrderActivity ")
                    .append(" having mbd.workOrderActivity.id = woa.id) is null)");
            paramsMap.put("factor", factor);
            paramsMap.put("mbHeaderStatus", MBHeader.MeasurementBookStatus.CANCELLED.toString());
        }
        // /woActivityListOriEst = searchWOActivities(criteriaMap);
        // Remove the SOR items that were present in the original Estimate
        /*
         * if(woActivityListOriEst !=null && !woActivityListOriEst.isEmpty() && woActivityList!=null && !woActivityList.isEmpty())
         * { for(WorkOrderActivity woaOri :woActivityListOriEst) { for(WorkOrderActivity woaRev : woActivityList) {
         * if(woaOri.getActivity().getId()==woaRev.getActivity().getId()) { woActivityList.remove(woaRev); } } } }
         */
        final TypedQuery<WorkOrderActivity> typedQuery = entityManager.createQuery(dynQuery.toString(), WorkOrderActivity.class);
        paramsMap.entrySet().forEach(entry -> typedQuery.setParameter(entry.getKey(), entry.getValue()));

        return typedQuery.getResultList();
    }

    /**
     * This method will return toPageNo for a line item from the last mb entry.
     *
     * @param workOrderActivity
     * @return
     */
    @Override
    public MBHeader findLastMBPageNoForLineItem(final WorkOrderActivity workOrderActivity, final Long mbHeaderId) {
        logger.info("-------------------------Inside findLastMBPageNoForLineItem--------------");

        final StringBuffer query = new StringBuffer("select distinct mbh from MBHeader mbh join mbh.mbDetails as mbDetail ");
        final Map<String, Object> paramsMap = new HashMap<>();
        // if(workOrderActivity.getActivity().getSchedule() != null)
        // query = query +
        // "where mbDetail.workOrderActivity.activity.schedule.id = "+workOrderActivity.getActivity().getSchedule().getId();
        // if(workOrderActivity.getActivity().getNonSor() != null)
        // query = query +
        // "where mbDetail.workOrderActivity.activity.nonSor.id = "+workOrderActivity.getActivity().getNonSor().getId();

        query.append(" where mbDetail.workOrderActivity.id = :woaId and mbh.id != :mbhId and mbh.egwStatus.code = :mbhStatus ")
                .append(" and mbh.modifiedDate < (select modifiedDate from MBHeader where id = :mbhId )")
                .append(" order by mbh.modifiedDate desc");
        paramsMap.put("woaId", workOrderActivity.getId());
        paramsMap.put("mbhId", mbHeaderId);
        paramsMap.put("mbhStatus", WorksConstants.APPROVED);

        final TypedQuery<MBHeader> typedQuery = entityManager.createQuery(query.toString(), MBHeader.class);
        paramsMap.entrySet().forEach(entry -> typedQuery.setParameter(entry.getKey(), entry.getValue()));
        final List<MBHeader> mbHeaderList = typedQuery.getResultList();
        MBHeader result = null;
        if (mbHeaderList != null && !mbHeaderList.isEmpty())
            result = mbHeaderList.get(0);
        return result;
    }

    /**
     * This method will check whether MB is created and pending for approval for the given WO. final bill is not approved for
     * workorder??
     *
     * @param woId
     * @return
     */
    // public Boolean isMBCreatedAndPendingForWO(Long woId){
    // Boolean result = false;
    //
    // return result;
    // }

    /**
     * This method will check whether final bill is already approved for wo or not. No MB is in a "approval pending" for the work
     * order.
     *
     * @param woId
     * @return
     */
    // public Boolean isFinalBillApprovedForWO(Long woId){
    // Boolean result = false;
    //
    // return result;
    //
    // }

    public void setContractorService(final PersistenceService<Contractor, Long> contractorService) {
        this.contractorService = contractorService;
    }

    public void setWorksService(final WorksService worksService) {
        this.worksService = worksService;
    }

    /**
     * The method return true if the work number has to be re-generated
     *
     * @param entity an instance of <code>AbstractEstimate</code> containing the estimate date
     * @param financialYear an instance of <code>CFinancialYear</code> representing the financial year for the estimate date.
     * @return a boolean value indicating if the estimate number change is required.
     */
    /*
     * private boolean workOrderNumberChangeRequired(AbstractEstimate entity, CFinancialYear financialYear,WorkOrder workOrder){
     * String[] workOrderNum = workOrder.getWorkOrderNumber().split("/"); if(entity!=null &&
     * workOrderNum[0].equals(entity.getExecutingDepartment().getDeptCode()) &&
     * workOrderNum[2].equals(financialYear.getFinYearRange())) { return false; } return true; }
     */

    // end work order generation logic

    /**
     * @param workOrderNumberGenerators
     */
    public void setWorkOrderNumberGenerators(final WorkOrderNumberGenerator workOrderNumberGenerators) {
        this.workOrderNumberGenerators = workOrderNumberGenerators;
    }

    /*
     * @return Contractor List
     */
    @Override
    public List<Contractor> getAllContractorForWorkOrder() {
        logger.info("-------------------------Inside getAllContractorForWorkOrder---------------------");
        // Assuming that status is inserted using db script
        final String status = worksService.getWorksConfigValue("CONTRACTOR_STATUS");
        List<Contractor> contractorList = null;
        contractorList = contractorService.findAllByNamedQuery("GET_All_CONTRACTORS", status);
        return contractorList;
    }

    /**
     * Check whether any MB entry is pending for approval for the given WorkOrder
     *
     * @param woId
     * @return
     */
    @Override
    public Boolean isMBInApprovalPendingForWO(final String woNumber) {
        Boolean result = false;
        final Map<String, Object> criteriaMap = new HashMap<>();
        criteriaMap.put(WORKORDER_NO, woNumber);
        criteriaMap.put(ACTION_FLAG, "searchWOForMB");
        if (searchWO(criteriaMap).isEmpty())
            result = true;

        return result;
    }

    /**
     * This method will return ActivitiesForWorkorder.
     *
     * @param tenderResponse
     * @return Collection<EstimateLineItemsForWP>
     */

    @Override
    public Collection<EstimateLineItemsForWP> getActivitiesForWorkorder(final TenderResponse tenderResponse) {

        final Map<Long, EstimateLineItemsForWP> resultMap = new HashMap<>();
        final Map<String, Integer> exceptionaSorMap = getSpecialUoms();
        final List<String> tenderTypeList = worksService.getTendertypeList();
        String percTenderType = "";
        if (tenderTypeList != null && !tenderTypeList.isEmpty())
            percTenderType = tenderTypeList.get(0);
        for (final TenderResponseActivity tenAct : tenderResponse.getTenderResponseActivities()) {
            final EstimateLineItemsForWP estlineItem = new EstimateLineItemsForWP();
            if (tenAct.getActivity().getSchedule() != null)
                if (resultMap.containsKey(tenAct.getActivity().getSchedule().getId())) {
                    final EstimateLineItemsForWP preEstlineItem = resultMap.get(tenAct.getActivity().getSchedule()
                            .getId());
                    preEstlineItem.setQuantity(tenAct.getNegotiatedQuantity() + preEstlineItem.getQuantity());
                    if (DateUtils.compareDates(tenAct.getActivity().getAbstractEstimate().getEstimateDate(),
                            preEstlineItem.getEstimateDate())) {
                        if (tenderResponse.getTenderEstimate().getTenderType().equals(percTenderType))
                            preEstlineItem.setRate(tenAct.getActivity().getSORCurrentRate().getValue());
                        else
                            preEstlineItem.setRate(tenAct.getNegotiatedRate());
                        double result = 1;
                        if (tenAct.getActivity().getSchedule() != null
                                && exceptionaSorMap.containsKey(tenAct.getActivity().getUom().getUom())) {
                            result = exceptionaSorMap.get(tenAct.getActivity().getUom().getUom());
                            preEstlineItem.setAmt(preEstlineItem.getQuantity() * preEstlineItem.getRate() / result);
                        } else
                            preEstlineItem.setAmt(preEstlineItem.getQuantity() * preEstlineItem.getRate());
                        preEstlineItem.setActivity(tenAct.getActivity());
                    }
                    resultMap.put(tenAct.getActivity().getSchedule().getId(), preEstlineItem);
                } else {

                    addEstLineItem(tenAct, estlineItem);
                    resultMap.put(tenAct.getActivity().getSchedule().getId(), estlineItem);
                }
            if (tenAct.getActivity().getNonSor() != null) {
                addEstLineItem(tenAct, estlineItem);
                resultMap.put(tenAct.getActivity().getNonSor().getId(), estlineItem);
            }
        }
        return getEstLineItemsWithSrlNo(resultMap.values());
    }

    /**
     * This method will return Activities For WorksPackage.
     *
     * @param tenderResponse
     * @return Collection<EstimateLineItemsForWP>
     */

    @Override
    public Collection<EstimateLineItemsForWP> getActivitiesForWorksPackage(final TenderResponse tenderResponse) {

        final Map<Long, EstimateLineItemsForWP> resultMap = new HashMap<>();
        final Map<String, Integer> exceptionaSorMap = getSpecialUoms();
        final List<String> tenderTypeList = worksService.getTendertypeList();
        if (tenderTypeList != null && !tenderTypeList.isEmpty())
            tenderTypeList.get(0);
        for (final TenderResponseActivity tenAct : tenderResponse.getTenderResponseActivities()) {
            final EstimateLineItemsForWP estlineItem = new EstimateLineItemsForWP();
            if (tenAct.getActivity().getSchedule() != null)
                if (resultMap.containsKey(tenAct.getActivity().getSchedule().getId())) {
                    final EstimateLineItemsForWP preEstlineItem = resultMap.get(tenAct.getActivity().getSchedule()
                            .getId());
                    preEstlineItem.setQuantity(tenAct.getActivity().getQuantity() + preEstlineItem.getQuantity());
                    if (DateUtils.compareDates(tenAct.getActivity().getAbstractEstimate().getEstimateDate(),
                            preEstlineItem.getEstimateDate())) {
                        preEstlineItem.setRate(tenAct.getActivity().getSORCurrentRate().getValue());
                        double result = 1;
                        if (tenAct.getActivity().getSchedule() != null
                                && exceptionaSorMap.containsKey(tenAct.getActivity().getUom().getUom())) {
                            result = exceptionaSorMap.get(tenAct.getActivity().getUom().getUom());
                            preEstlineItem.setAmt(preEstlineItem.getQuantity() * preEstlineItem.getRate() / result);
                        } else
                            preEstlineItem.setAmt(preEstlineItem.getQuantity() * preEstlineItem.getRate());
                        preEstlineItem.setActivity(tenAct.getActivity());
                    }
                    resultMap.put(tenAct.getActivity().getSchedule().getId(), preEstlineItem);
                } else {

                    addEstLineItemWP(tenAct, estlineItem);
                    resultMap.put(tenAct.getActivity().getSchedule().getId(), estlineItem);
                }
            if (tenAct.getActivity().getNonSor() != null) {
                addEstLineItemWP(tenAct, estlineItem);
                resultMap.put(tenAct.getActivity().getNonSor().getId(), estlineItem);
            }
        }
        return getEstLineItemsWithSrlNo(resultMap.values());
    }

    private Map<String, Integer> getSpecialUoms() {
        return worksService.getExceptionSOR();
    }

    private Collection<EstimateLineItemsForWP> getEstLineItemsWithSrlNo(final Collection<EstimateLineItemsForWP> actList) {
        int i = 1;
        final Collection<EstimateLineItemsForWP> latestEstLineItemList = new ArrayList<>();
        for (final EstimateLineItemsForWP act : actList) {
            act.setSrlNo(i);
            latestEstLineItemList.add(act);
            i++;
        }
        return latestEstLineItemList;
    }

    private void addEstLineItem(final TenderResponseActivity act, final EstimateLineItemsForWP estlineItem) {
        if (act.getActivity().getSchedule() == null) {
            estlineItem.setCode("");
            estlineItem.setSummary("");
            estlineItem.setDescription(act.getActivity().getNonSor().getDescription());
            estlineItem.setRate(act.getNegotiatedRate());
            estlineItem.setAmt(act.getNegotiatedQuantity() * act.getNegotiatedRate());
        } else {
            estlineItem.setCode(act.getActivity().getSchedule().getCode());
            estlineItem.setDescription(act.getActivity().getSchedule().getDescription());
            estlineItem.setRate(act.getNegotiatedRate());
            estlineItem.setSummary(act.getActivity().getSchedule().getSummary());
            final Map<String, Integer> exceptionaSorMap = getSpecialUoms();
            double result = 1;
            if (exceptionaSorMap.containsKey(act.getActivity().getUom().getUom()))
                result = exceptionaSorMap.get(act.getActivity().getUom().getUom());
            estlineItem.setAmt(act.getNegotiatedQuantity() * act.getNegotiatedRate() / result);
        }
        estlineItem.setActivity(act.getActivity());
        estlineItem.setQuantity(act.getNegotiatedQuantity());
        estlineItem.setUom(act.getActivity().getUom().getUom());
        estlineItem.setConversionFactor(act.getActivity().getConversionFactor());
    }

    private void addEstLineItemWP(final TenderResponseActivity act, final EstimateLineItemsForWP estlineItem) {
        if (act.getActivity().getSchedule() == null) {
            estlineItem.setCode("");
            estlineItem.setSummary("");
            estlineItem.setDescription(act.getActivity().getNonSor().getDescription());
            estlineItem.setRate(act.getActivity().getRate());
            estlineItem.setAmt(act.getActivity().getQuantity() * estlineItem.getRate());
        } else {
            estlineItem.setCode(act.getActivity().getSchedule().getCode());
            estlineItem.setDescription(act.getActivity().getSchedule().getDescription());
            estlineItem.setRate(act.getActivity().getSORCurrentRate().getValue());
            estlineItem.setSummary(act.getActivity().getSchedule().getSummary());
            final Map<String, Integer> exceptionaSorMap = getSpecialUoms();
            double result = 1;
            if (exceptionaSorMap.containsKey(act.getActivity().getUom().getUom()))
                result = exceptionaSorMap.get(act.getActivity().getUom().getUom());
            estlineItem.setAmt(act.getActivity().getQuantity() * estlineItem.getRate() / result);
        }
        estlineItem.setActivity(act.getActivity());
        estlineItem.setQuantity(act.getActivity().getQuantity());
        estlineItem.setUom(act.getActivity().getUom().getUom());
        estlineItem.setConversionFactor(act.getActivity().getConversionFactor());
    }

    /**
     * This method will return ActivitiesForWorkorder.
     *
     * @param WorkOrder
     * @return Collection<EstimateLineItemsForWP>
     */

    @Override
    public Collection<EstimateLineItemsForWP> getActivitiesForWorkorder(final WorkOrder workOrder) {

        final Map<Long, EstimateLineItemsForWP> resultMap = new HashMap<>();
        final Map<String, Integer> exceptionaSorMap = getSpecialUoms();
        for (final WorkOrderEstimate workOrderEstimate : workOrder.getWorkOrderEstimates())
            for (final WorkOrderActivity woAct : workOrderEstimate.getWorkOrderActivities()) {
                final EstimateLineItemsForWP estlineItem = new EstimateLineItemsForWP();
                if (woAct.getActivity().getSchedule() != null)
                    if (resultMap.containsKey(woAct.getActivity().getSchedule().getId())) {
                        final EstimateLineItemsForWP preEstlineItem = resultMap.get(woAct.getActivity().getSchedule()
                                .getId());
                        preEstlineItem.setQuantity(woAct.getApprovedQuantity() + preEstlineItem.getQuantity());
                        if (DateUtils.compareDates(woAct.getActivity().getAbstractEstimate().getEstimateDate(),
                                preEstlineItem.getEstimateDate())) {
                            preEstlineItem.setRate(woAct.getActivity().getSORCurrentRate().getValue());
                            double result = 1;
                            if (woAct.getActivity().getSchedule() != null
                                    && exceptionaSorMap.containsKey(woAct.getActivity().getUom().getUom())) {
                                result = exceptionaSorMap.get(woAct.getActivity().getUom().getUom());
                                preEstlineItem.setAmt(preEstlineItem.getQuantity() * preEstlineItem.getRate() / result);
                            } else
                                preEstlineItem.setAmt(preEstlineItem.getQuantity() * preEstlineItem.getRate());
                            preEstlineItem.setActivity(woAct.getActivity());
                        }
                        resultMap.put(woAct.getActivity().getSchedule().getId(), preEstlineItem);
                    } else {

                        addEstLineItem(woAct, estlineItem);
                        resultMap.put(woAct.getActivity().getSchedule().getId(), estlineItem);
                    }
                if (woAct.getActivity().getNonSor() != null) {
                    addEstLineItem(woAct, estlineItem);
                    resultMap.put(woAct.getActivity().getNonSor().getId(), estlineItem);
                }
            }
        return getEstLineItemsWithSrlNo(resultMap.values());
    }

    private void addEstLineItem(final WorkOrderActivity act, final EstimateLineItemsForWP estlineItem) {
        if (act.getActivity().getSchedule() == null) {
            estlineItem.setCode("");
            estlineItem.setSummary("");
            estlineItem.setDescription(act.getActivity().getNonSor().getDescription());
            estlineItem.setRate(act.getApprovedRate());
            estlineItem.setAmt(act.getApprovedQuantity() * act.getApprovedRate());
        } else {
            estlineItem.setCode(act.getActivity().getSchedule().getCode());
            estlineItem.setDescription(act.getActivity().getSchedule().getDescription());
            estlineItem.setRate(act.getApprovedRate());
            estlineItem.setSummary(act.getActivity().getSchedule().getSummary());
            final Map<String, Integer> exceptionaSorMap = getSpecialUoms();
            double result = 1;
            if (exceptionaSorMap.containsKey(act.getActivity().getUom().getUom()))
                result = exceptionaSorMap.get(act.getActivity().getUom().getUom());
            estlineItem.setAmt(act.getApprovedQuantity() * act.getApprovedRate() / result);
        }
        estlineItem.setActivity(act.getActivity());
        estlineItem.setQuantity(act.getApprovedQuantity());
        estlineItem.setUom(act.getActivity().getUom().getUom());
        estlineItem.setConversionFactor(act.getActivity().getConversionFactor());
    }

    /**
     * returns headermap for pdf
     *
     * @param workOrder
     * @return
     */

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Map createHeaderParams(final WorkOrder workOrder, final String type) {
        final Map<String, Object> reportParams = new HashMap<>();
        if (workOrder != null)
            if ("estimate".equalsIgnoreCase(type)) {
                for (final WorkOrderEstimate workOrderEstimate : workOrder.getWorkOrderEstimates())
                    if (workOrderEstimate != null && workOrderEstimate.getEstimate() != null) {
                        reportParams
                                .put("deptName", workOrderEstimate.getEstimate().getExecutingDepartment().getName());
                        final Boundary b = getTopLevelBoundary(workOrderEstimate.getEstimate().getWard());
                        reportParams.put("cityName", b == null ? "" : b.getName());
                        // TODO - Department address variable has been removed
                        // from Department object. Need to find alternative way
                        // to find department address and pass the same here.
                        // reportParams.put("deptAddress",workOrderEstimate.getEstimate().getExecutingDepartment().getDeptAddress());
                        reportParams.put("deptAddress", "");
                        reportParams.put("aeWorkNameForEstimate", workOrderEstimate.getEstimate().getName());
                        reportParams.put("negotiatedAmtForEstimate", workOrder.getWorkOrderAmount());
                        reportParams.put("estimateNo", workOrderEstimate.getEstimate().getEstimateNumber());
                        reportParams.put("estimateDate", workOrderEstimate.getEstimate().getEstimateDate());

                        if (workOrderEstimate.getEstimate().getProjectCode() != null)
                            reportParams.put("projectCode", workOrderEstimate.getEstimate().getProjectCode().getCode());
                    }
            } else {
                // reportParams.put("grandTotal", getGrandTotal(aeForWp));
                final List<WorkOrderEstimate> aeList = getAbstractEstimateListForWp(workOrder);
                final String projectCode = getProjectCodeListForAe(aeList);
                reportParams.put("projectCodeList", projectCode);
                final WorksPackage wp = workspackageService.findByNamedQuery("GET_WORKSPACKAGE_PACKAGENUMBER",
                        workOrder.getPackageNumber());
                if (wp != null)
                    reportParams.put("workPackageDate", wp.getCreatedDate());
                if (wp != null)
                    reportParams.put("tenderFileNumber", wp.getTenderFileNumber());

                reportParams.put("workPackageNo", workOrder.getPackageNumber());
                if (aeList != null && !aeList.isEmpty()) {
                    reportParams.put("deptName", aeList.get(0).getEstimate().getExecutingDepartment().getName());
                    final Boundary b = getTopLevelBoundary(aeList.get(0).getEstimate().getWard());
                    reportParams.put("cityName", b == null ? "" : b.getName());
                    // TODO - Department address variable has been removed from
                    // Department object. Need to find alternative way to find
                    // department address and pass the same here.
                    // reportParams.put("deptAddress",aeList.get(0).getEstimate().getExecutingDepartment().getDeptAddress());
                    reportParams.put("deptAddress", "");
                }
            }
        if (workOrder != null && workOrder.getContractor() != null) {
            String contractorAddress = workOrder.getContractor().getName() + "  ,  "
                    + workOrder.getContractor().getCode();
            if (workOrder.getContractor().getPaymentAddress() != null)
                contractorAddress = contractorAddress + "  ,  " + workOrder.getContractor().getPaymentAddress();
            reportParams.put("contractorAddress", contractorAddress);
        }

        reportParams.put("WorkOrderObj", workOrder);

        return reportParams;
    }

    public double getGrandTotal(final List<AbstractEstimateForWp> aeForWp) {
        double grandTotal = 0.00;
        for (final AbstractEstimateForWp aeforWp : aeForWp)
            grandTotal = grandTotal + aeforWp.getNegotiatedAmtForWp();
        return grandTotal;
    }

    protected Boundary getTopLevelBoundary(final Boundary boundary) {
        Boundary b = boundary;
        while (b != null && b.getParent() != null)
            b = b.getParent();
        return b;
    }

    protected String getProjectCodeListForAe(final List<WorkOrderEstimate> aeList) {
        String projectCodes = "";
        int i = 0;
        for (final WorkOrderEstimate ae : aeList) {
            if (ae.getEstimate().getProjectCode() != null && ae.getEstimate().getProjectCode().getCode() != null)
                projectCodes = projectCodes + ae.getEstimate().getProjectCode().getCode();

            if (i < aeList.size() - 1)
                projectCodes = projectCodes.concat(", ");
            i++;
        }
        return projectCodes;
    }

    /**
     * returns AbstractEstimateForWp list
     *
     * @param workOrder
     * @return
     */
    protected List<AbstractEstimateForWp> getAeForWp(final List<WorkOrderEstimate> aeList) {
        final List<AbstractEstimateForWp> aeForWpList = new ArrayList<>();
        int srlNo = 0;
        for (final WorkOrderEstimate ae : aeList) {
            final AbstractEstimateForWp aeForWp = new AbstractEstimateForWp();
            aeForWp.setSrlNo(++srlNo);
            aeForWp.setAeWorkNameForWp(ae.getEstimate().getName());
            aeForWp.setNegotiatedAmtForWp(getWorkOrderEstimateAmount(ae));
            aeForWpList.add(aeForWp);
        }
        return aeForWpList;
    }

    public double getWorkOrderEstimateAmount(final WorkOrderEstimate ae) {
        double totalAmt = 0.0;
        for (final WorkOrderActivity wact : ae.getWorkOrderActivities())
            totalAmt += wact.getApprovedAmount();
        return totalAmt;
    }

    @Override
    public List<AbstractEstimateForWp> getAeForWp(final WorkOrder workOrder) {
        final List<WorkOrderEstimate> aeList = getAbstractEstimateListForWp(workOrder);
        return getAeForWp(aeList);
    }

    /**
     * returns WorkOrderEstimate list
     *
     * @param workOrder
     * @return
     */
    public List<WorkOrderEstimate> getAbstractEstimateListForWp(final WorkOrder workOrder) {
        final List<WorkOrderEstimate> aeList = new ArrayList<>();
        final List<WorkOrderEstimate> workOrderEstimateList = workOrder.getWorkOrderEstimates();
        for (final WorkOrderEstimate workOrderEstimate : workOrderEstimateList)
            aeList.add(workOrderEstimate);
        return aeList;
    }

    /**
     * Populate all the cumulative fields related to WOA line item
     *
     * @param workOrderEstimate
     * @return
     */
    @Override
    public WorkOrderEstimate calculateCumulativeDetailsForRE(final WorkOrderEstimate workOrderEstimate) {
        final List<WorkOrderActivity> woaList = workOrderEstimate.getWorkOrderActivities();
        double lPrevCumlvQuant = 0;
        for (final WorkOrderActivity detail : woaList) {
            if (detail.getActivity().getParent() == null) { // IN CASE OF
                // NON_TENDERED &
                // LUMPSUM
                lPrevCumlvQuant = measurementBookService.prevCumulativeQuantityIncludingCQ(detail.getId(), null, detail
                        .getActivity().getId(), workOrderEstimate.getWorkOrder());
                detail.setTotalEstQuantity(measurementBookService.totalEstimatedQuantityForPreviousREs(detail.getId(),
                        workOrderEstimate.getEstimate().getId(), detail.getActivity().getId(),
                        workOrderEstimate.getWorkOrder()));
            } else { // IN CASE OF CHANGE_QUANTITY
                List<WorkOrderActivity> woActivities = null;
                WorkOrderActivity woa = null;
                // CHANGE_QUANTITY DONE FOR NON_TENDERED & LUMPSUM ACTIVITIES
                if (detail.getActivity().getParent().getRevisionType() != null
                        && (detail.getActivity().getParent().getRevisionType().equals(RevisionType.NON_TENDERED_ITEM) || detail
                                .getActivity().getParent().getRevisionType().equals(RevisionType.LUMP_SUM_ITEM))) {
                    woActivities = entityManager
                            .createQuery(
                                    "from WorkOrderActivity where activity.id = :activityId and workOrderEstimate.estimate.id = :estimateId",
                                    WorkOrderActivity.class)
                            .setParameter("activityId", detail.getActivity().getParent().getId())
                            .setParameter("estimateId", detail.getActivity().getParent().getAbstractEstimate().getId())
                            .getResultList();
                    woa = woActivities.isEmpty() ? null : woActivities.get(0);
                } else {
                    // CHANGE_QUANTITY DONE FOR ORIGINAL ESTIMATE ACTIVITIES
                    woActivities = entityManager.createQuery(
                            "from WorkOrderActivity where activity.id = :activityId and workOrderEstimate.estimate.id = :estimateId",
                            WorkOrderActivity.class)
                            .setParameter("activityId", detail.getActivity().getParent().getId())
                            .setParameter("estimateId", workOrderEstimate.getEstimate().getParent().getId())
                            .getResultList();
                    woa = woActivities.isEmpty() ? null : woActivities.get(0);
                }
                detail.setParent(woa);
                lPrevCumlvQuant = measurementBookService.prevCumulativeQuantityIncludingCQ(detail.getId(), null, detail
                        .getActivity().getParent().getId(), workOrderEstimate.getWorkOrder().getParent());
                detail.setTotalEstQuantity(measurementBookService.totalEstimatedQuantityForPreviousREs(detail.getId(),
                        workOrderEstimate.getEstimate().getId(), detail.getActivity().getParent().getId(),
                        workOrderEstimate.getWorkOrder().getParent()));
            }

            if (detail.getTotalEstQuantity() == 0 && detail.getParent() != null
                    && detail.getParent().getActivity().getQuantity() != 0)
                detail.setTotalEstQuantity(detail.getApprovedQuantity());

            detail.setPrevCumlvQuantity(lPrevCumlvQuant);
        }
        return workOrderEstimate;
    }

    public void setWorkspackageService(final WorksPackageService workspackageService) {
        this.workspackageService = workspackageService;
    }

    public void setMeasurementBookService(final MeasurementBookService measurementBookService) {
        this.measurementBookService = measurementBookService;
    }

    public String getFinalBillTypeConfigValue() {
        return worksService.getWorksConfigValue("FinalBillType");
    }

    @Override
    public Date getWorkCommencedDateByWOId(final Long id) {
        final List<Date> results = entityManager.createQuery(new StringBuffer(" select stat.statusDate")
                .append(" from OfflineStatus stat")
                .append(" where stat.objectId = :objectId and stat.objectType = :objectType and stat.egwStatus.code = :status ")
                .toString(),
                Date.class)
                .setParameter("objectId", id)
                .setParameter("objectType", "WorkOrder")
                .setParameter("status", WorksConstants.WO_STATUS_WOCOMMENCED)
                .getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public String getWorksPackageName(final String wpNumber) {
        final List<String> results = entityManager.createQuery(new StringBuffer(" select wp.name")
                .append(" from WorksPackage wp")
                .append(" where wp.wpNumber = :wpNumber and wp.egwStatus.code = :status ").toString(), String.class)
                .setParameter("wpNumber", wpNumber)
                .setParameter("status", WorksConstants.APPROVED)
                .getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public Object getTenderNegotiationInfo(final String negotiationNo) {
        final List<Object> results = entityManager.createQuery(
                new StringBuffer("select tr.percNegotiatedAmountRate,tr.tenderEstimate.tenderType")
                        .append(" from TenderResponse tr")
                        .append(" where tr.egwStatus.code = 'APPROVED' and tr.negotiationNumber = :negotiationNumber ")
                        .toString(),
                Object.class)
                .setParameter("negotiationNumber", negotiationNo)
                .getResultList();
        return results.isEmpty() ? null : results.get(0);

    }

    @Override
    public WorkOrderEstimate getWorkOrderEstimateForWOIdAndEstimateId(final Long workOrderId, final Long estimateId) {
        final List<WorkOrderEstimate> workOrderEstimates = entityManager
                .createQuery(new StringBuffer("from WorkOrderEstimate woe")
                        .append(" where woe.workOrder.id = :woId and woe.estimate.id = :estimateId and woe.estimate.egwStatus.code = :estimateStatus")
                        .append(" and woe.workOrder.egwStatus.code = :woStatus and woe.workOrder.parent is null ").toString(),
                        WorkOrderEstimate.class)
                .setParameter("woId", workOrderId)
                .setParameter("estimateId", estimateId)
                .setParameter("estimateStatus", AbstractEstimate.EstimateStatus.ADMIN_SANCTIONED.toString())
                .setParameter("woStatus", WorksConstants.APPROVED)
                .getResultList();
        return workOrderEstimates.isEmpty() ? null : workOrderEstimates.get(0);
    }

    @Override
    public List<Object> getWorkOrderDetails(final Long estimateId) {
        final List<Object> woDetails = entityManager.createQuery(
                new StringBuffer("select woe.workOrder.id ,woe.workOrder.workOrderNumber")
                        .append(" from WorkOrderEstimate woe ")
                        .append(" where woe.estimate.id = :estimateId and woe.workOrder.egwStatus.code not in :woStatuses ")
                        .toString(),
                Object.class)
                .setParameter("estimateId", estimateId)
                .setParameter("woStatuses", Arrays.asList(WorksConstants.NEW, WorksConstants.CANCELLED_STATUS))
                .getResultList();
        return woDetails;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<WorkOrderActivity> getActionWorkOrderActivitiesList(
            final List<WorkOrderActivity> actionWorkOrderActivities) {
        return CollectionUtils.select(actionWorkOrderActivities,
                workOrderActivity -> (WorkOrderActivity) workOrderActivity != null);
    }
}

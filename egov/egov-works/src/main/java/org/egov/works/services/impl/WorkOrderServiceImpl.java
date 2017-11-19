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
        final List<WorkOrder> filteredList = new ArrayList<WorkOrder>();
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
        final List<WorkOrder> filteredList = new ArrayList<WorkOrder>();
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
        final List<String> qryList = new ArrayList<String>();
        StringBuffer commonQueryFilter = new StringBuffer();
        // this String is the return Countqruery
        final String CountQry = " select count(distinct wo) from WorkOrder wo left join wo.workOrderEstimates workOrderEstimate"
                + " where wo.id is not null and wo.parent is null and wo.egwStatus.code<>'NEW' ";

        final String dynQuery = "select distinct wo from WorkOrder wo left join wo.workOrderEstimates workOrderEstimate"
                + " where wo.id is not null and wo.parent is null and wo.egwStatus.code<>'NEW' ";
        final String setStat = worksService.getWorksConfigValue("WorkOrder.setstatus");
        if (criteriaMap.get(STATUS) != null)
            if (criteriaMap.get(STATUS).equals("APPROVED") || criteriaMap.get(STATUS).equals("CANCELLED")) {
                if (criteriaMap.get(SOURCEPAGE) != null && CANCELWO.equals(criteriaMap.get(SOURCEPAGE))) {
                    commonQueryFilter = commonQueryFilter.append(" and wo.egwStatus.code = ? ");
                    paramList.add(criteriaMap.get(STATUS));
                } else if (criteriaMap.get(STATUS).equals("APPROVED")) {
                    commonQueryFilter = commonQueryFilter.append(" and wo.egwStatus.code = ? and "
                            + " wo.id not in (select objectId from OfflineStatus where objectType=?)");
                    paramList.add(criteriaMap.get(STATUS));
                    paramList.add("WorkOrder");
                } else if (criteriaMap.get(STATUS).equals("CANCELLED")) {
                    commonQueryFilter = commonQueryFilter.append(" and wo.egwStatus.code = ? ");
                    paramList.add(criteriaMap.get(STATUS));
                }
            } else if (!criteriaMap.get(STATUS).equals("-1")
                    && Arrays.asList(setStat.split(",")).contains(criteriaMap.get(STATUS))) {
                commonQueryFilter = commonQueryFilter
                        .append(" and wo.egwStatus.code = 'APPROVED' and wo.id in(select stat.objectId from "
                                + "OfflineStatus stat where stat.egwStatus.code=? and stat.id = (select"
                                + " max(stat1.id) from OfflineStatus stat1 where wo.id=stat1.objectId and stat1.objectType=?) and stat.objectType=?)");
                paramList.add(criteriaMap.get(STATUS));
                paramList.add("WorkOrder");
                paramList.add("WorkOrder");
            } else if (!criteriaMap.get(STATUS).equals("-1")
                    && !Arrays.asList(setStat.split(",")).contains(criteriaMap.get(STATUS))) {
                commonQueryFilter = commonQueryFilter.append(" and wo.egwStatus.code = ?");
                paramList.add(criteriaMap.get(STATUS));
            }
        if (criteriaMap.get(CREATE_DATE) != null) {
            commonQueryFilter = commonQueryFilter.append(" and wo.workOrderDate = ? ");
            paramList.add(criteriaMap.get(CREATE_DATE));
        }

        if (criteriaMap.get(FROM_DATE) != null && criteriaMap.get(TO_DATE) == null) {
            commonQueryFilter = commonQueryFilter.append(" and wo.workOrderDate >= ? ");
            paramList.add(criteriaMap.get(FROM_DATE));

        } else if (criteriaMap.get(TO_DATE) != null && criteriaMap.get(FROM_DATE) == null) {
            commonQueryFilter = commonQueryFilter.append(" and wo.workOrderDate <= ? ");
            paramList.add(criteriaMap.get(TO_DATE));
        } else if (criteriaMap.get(FROM_DATE) != null && criteriaMap.get(TO_DATE) != null) {
            commonQueryFilter = commonQueryFilter.append(" and wo.workOrderDate between ? and ? ");
            paramList.add(criteriaMap.get(FROM_DATE));
            paramList.add(criteriaMap.get(TO_DATE));
        }
        if (criteriaMap.get(WORKORDER_NO) != null) {
            commonQueryFilter = commonQueryFilter.append(" and UPPER(wo.workOrderNumber) like ? ");
            paramList.add("%" + criteriaMap.get(WORKORDER_NO).toString().trim().toUpperCase() + "%");
        }
        if (criteriaMap.get(WP_NO) != null) {
            commonQueryFilter = commonQueryFilter.append(" and UPPER(wo.packageNumber) like ? ");
            paramList.add("%" + criteriaMap.get(WP_NO).toString().trim().toUpperCase() + "%");
        }
        if (criteriaMap.get(TENDER_FILE_NO) != null) {
            commonQueryFilter = commonQueryFilter
                    .append(" and wo.negotiationNumber in (select tr1.negotiationNumber from TenderResponse tr1 where "
                            + "UPPER(tr1.tenderEstimate.worksPackage.tenderFileNumber) like ? )");
            paramList.add("%" + criteriaMap.get(TENDER_FILE_NO).toString().trim().toUpperCase() + "%");
        }
        if (criteriaMap.get(CONTRACTOR_ID) != null) {
            commonQueryFilter = commonQueryFilter.append(" and wo.contractor.id = ? ");
            paramList.add(criteriaMap.get(CONTRACTOR_ID));
        }
        if (criteriaMap.get("DEPT_ID") != null) {
            commonQueryFilter = commonQueryFilter
                    .append(" and wo.id in (select we.workOrder.id from WorkOrderEstimate we where we.workOrder.id=wo.id and "
                            + " we.estimate.executingDepartment.id = ?) ");
            paramList.add(criteriaMap.get("DEPT_ID"));
        }

        if (criteriaMap.get(ESTIMATE_NO) != null) {
            commonQueryFilter = commonQueryFilter
                    .append(" and wo.id in (select we.workOrder.id from WorkOrderEstimate we where we.workOrder.id=wo.id and "
                            + " UPPER(we.estimate.estimateNumber) like ? ) ");
            paramList.add("%" + criteriaMap.get(ESTIMATE_NO).toString().trim().toUpperCase() + "%");
        }
        if (criteriaMap.get(TENDER_NO) != null && !"".equalsIgnoreCase((String) criteriaMap.get(TENDER_NO))) {
            logger.debug("-------TENDER_NO-----------" + criteriaMap.get(TENDER_NO));
            commonQueryFilter = commonQueryFilter.append(" and UPPER(wo.tenderNumber like) ? ) ");
            paramList.add("%" + criteriaMap.get(TENDER_NO).toString().trim().toUpperCase() + "%");
        }
        if (criteriaMap.get(PROJECT_CODE) != null) {
            commonQueryFilter = commonQueryFilter
                    .append(" and wo.id in (select we.workOrder.id from WorkOrderEstimate we where we.workOrder.id=wo.id and "
                            + " UPPER(we.estimate.executingDepartment.projectCode.code) like ? ) ");
            paramList.add("%" + criteriaMap.get(PROJECT_CODE).toString().trim().toUpperCase() + "%");
        }

        if (criteriaMap.get(ACTION_FLAG) != null
                && criteriaMap.get(ACTION_FLAG).toString().equalsIgnoreCase("searchWOForMB")) {
            commonQueryFilter = commonQueryFilter
                    .append(" and workOrderEstimate.estimate.projectCode.egwStatus.code!='CLOSED' and workOrderEstimate.id not in (select distinct mbh.workOrderEstimate.id "
                            + "from MBHeader mbh where mbh.egwStatus.code=? "
                            + " or mbh.egwStatus.code=?"
                            + " or mbh.egwStatus.code=?"
                            + " or mbh.egwStatus.code=? or mbh.egwStatus.code=? )"
                            + "and workOrderEstimate.id not in (select distinct mbh.workOrderEstimate.id "
                            + "from MBHeader mbh where "
                            + " mbh.egwStatus.code = ? and  mbh.egBillregister.billstatus = ? and "
                            + " mbh.egBillregister.billtype=?)");
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
            commonQueryFilter = commonQueryFilter
                    .append(" and workOrderEstimate.estimate.projectCode.egwStatus.code!='CLOSED' and workOrderEstimate.id not in "
                            + "(select distinct mbh.workOrderEstimate.id from MBHeader mbh where mbh.egwStatus.code = ? "
                            + " and (mbh.egBillregister.billstatus <> ? and mbh.egBillregister.billtype = ?))");

            paramList.add(MBHeader.MeasurementBookStatus.APPROVED.toString());
            paramList.add(MBHeader.MeasurementBookStatus.CANCELLED.toString());
            paramList.add(getFinalBillTypeConfigValue());
        }

        final String orderQry = " Order by wo.workOrderDate ";
        logger.info("Query is ::" + dynQuery);

        qryList.add(CountQry + commonQueryFilter + orderQry);
        qryList.add(dynQuery + commonQueryFilter + orderQry);

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
        List<WorkOrder> wolList = null;

        String dynQuery = "select distinct wo from WorkOrder wo left join wo.workOrderEstimates workOrderEstimate"
                + " where wo.id is not null and wo.parent is null ";
        Object[] params;
        final List<Object> paramList = new ArrayList<Object>();
        final String setStat = worksService.getWorksConfigValue("WorkOrder.setstatus");
        if (criteriaMap.get(STATUS) != null)
            if (criteriaMap.get(STATUS).equals("APPROVED") || criteriaMap.get(STATUS).equals("CANCELLED")) {
                if (criteriaMap.get(SOURCEPAGE) != null && CANCELWO.equals(criteriaMap.get(SOURCEPAGE))) {
                    dynQuery = dynQuery + " and wo.egwStatus.code = ? ";
                    paramList.add(criteriaMap.get(STATUS));
                } else {
                    dynQuery = dynQuery + " and wo.egwStatus.code = ? and "
                            + " wo.id not in (select objectId from OfflineStatus where objectType=?)";
                    paramList.add(criteriaMap.get(STATUS));
                    paramList.add("WorkOrder");
                }
            } else if (!criteriaMap.get(STATUS).equals("-1")
                    && Arrays.asList(setStat.split(",")).contains(criteriaMap.get(STATUS))) {
                dynQuery = dynQuery
                        + " and wo.id in(select stat.objectId from "
                        + "OfflineStatus stat where stat.egwStatus.code=? and stat.id = (select"
                        + " max(stat1.id) from OfflineStatus stat1 where wo.id=stat1.objectId and stat1.objectType=?) and stat.objectType=?)";
                paramList.add(criteriaMap.get(STATUS));
                paramList.add("WorkOrder");
                paramList.add("WorkOrder");
            } else if (!criteriaMap.get(STATUS).equals("-1")
                    && !Arrays.asList(setStat.split(",")).contains(criteriaMap.get(STATUS))) {
                dynQuery = dynQuery + " and wo.egwStatus.code = ?";
                paramList.add(criteriaMap.get(STATUS));
            }
        if (criteriaMap.get(CREATE_DATE) != null) {
            dynQuery = dynQuery + " and wo.workOrderDate = ? ";
            paramList.add(criteriaMap.get(CREATE_DATE));
        }

        if (criteriaMap.get(FROM_DATE) != null && criteriaMap.get(TO_DATE) == null) {
            dynQuery = dynQuery + " and wo.workOrderDate >= ? ";
            paramList.add(criteriaMap.get(FROM_DATE));

        } else if (criteriaMap.get(TO_DATE) != null && criteriaMap.get(FROM_DATE) == null) {
            dynQuery = dynQuery + " and wo.workOrderDate <= ? ";
            paramList.add(criteriaMap.get(TO_DATE));
        } else if (criteriaMap.get(FROM_DATE) != null && criteriaMap.get(TO_DATE) != null) {
            dynQuery = dynQuery + " and wo.workOrderDate between ? and ? ";
            paramList.add(criteriaMap.get(FROM_DATE));
            paramList.add(criteriaMap.get(TO_DATE));
        }
        if (criteriaMap.get(WORKORDER_NO) != null) {
            dynQuery = dynQuery + " and UPPER(wo.workOrderNumber) like ? ";
            paramList.add("%" + criteriaMap.get(WORKORDER_NO).toString().trim().toUpperCase() + "%");
        }
        if (criteriaMap.get(WP_NO) != null) {
            dynQuery = dynQuery + " and UPPER(wo.packageNumber) like ? ";
            paramList.add("%" + criteriaMap.get(WP_NO).toString().trim().toUpperCase() + "%");
        }
        if (criteriaMap.get(TENDER_FILE_NO) != null) {
            dynQuery = dynQuery
                    + " and wo.negotiationNumber in (select tr1.negotiationNumber from TenderResponse tr1 where "
                    + "UPPER(tr1.tenderEstimate.worksPackage.tenderFileNumber) like ? )";
            paramList.add("%" + criteriaMap.get(TENDER_FILE_NO).toString().trim().toUpperCase() + "%");
        }
        if (criteriaMap.get(CONTRACTOR_ID) != null) {
            dynQuery = dynQuery + " and wo.contractor.id = ? ";
            paramList.add(criteriaMap.get(CONTRACTOR_ID));
        }
        if (criteriaMap.get("DEPT_ID") != null) {
            dynQuery = dynQuery
                    + " and wo.id in (select we.workOrder.id from WorkOrderEstimate we where we.workOrder.id=wo.id and "
                    + " we.estimate.executingDepartment.id = ?) ";
            paramList.add(criteriaMap.get("DEPT_ID"));
        }

        if (criteriaMap.get(ESTIMATE_NO) != null) {
            dynQuery = dynQuery
                    + " and wo.id in (select we.workOrder.id from WorkOrderEstimate we where we.workOrder.id=wo.id and "
                    + " UPPER(we.estimate.estimateNumber) like ? ) ";
            paramList.add("%" + criteriaMap.get(ESTIMATE_NO).toString().trim().toUpperCase() + "%");
        }
        if (criteriaMap.get(TENDER_NO) != null && !"".equalsIgnoreCase((String) criteriaMap.get(TENDER_NO))) {
            logger.debug("-------TENDER_NO-----------" + criteriaMap.get(TENDER_NO));
            dynQuery = dynQuery + " and UPPER(wo.tenderNumber like) ? ) ";
            paramList.add("%" + criteriaMap.get(TENDER_NO).toString().trim().toUpperCase() + "%");
        }
        if (criteriaMap.get(PROJECT_CODE) != null) {
            dynQuery = dynQuery
                    + " and wo.id in (select we.workOrder.id from WorkOrderEstimate we where we.workOrder.id=wo.id and "
                    + " UPPER(we.estimate.executingDepartment.projectCode.code) like ? ) ";
            paramList.add("%" + criteriaMap.get(PROJECT_CODE).toString().trim().toUpperCase() + "%");
        }

        if (criteriaMap.get(ACTION_FLAG) != null
                && criteriaMap.get(ACTION_FLAG).toString().equalsIgnoreCase("searchWOForMB")) {
            dynQuery = dynQuery
                    + " and workOrderEstimate.workOrder.egwStatus.code!='CANCELLED' and workOrderEstimate.estimate.projectCode.egwStatus.code!='CLOSED' and workOrderEstimate.id not in (select distinct mbh.workOrderEstimate.id "
                    + "from MBHeader mbh where mbh.egwStatus.code=? " + " or mbh.egwStatus.code=?"
                    + " or mbh.egwStatus.code=?" + " or mbh.egwStatus.code=? or mbh.egwStatus.code=? )"
                    + "and workOrderEstimate.id not in (select distinct mbh.workOrderEstimate.id "
                    + "from MBHeader mbh where "
                    + " mbh.egwStatus.code = ? and  mbh.egBillregister.billstatus = ? and "
                    + " mbh.egBillregister.billtype=?)";
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
            dynQuery = dynQuery
                    + " and workOrderEstimate.workOrder.egwStatus.code!='CANCELLED' and workOrderEstimate.estimate.projectCode.egwStatus.code!='CLOSED' and workOrderEstimate.id not in "
                    + "(select distinct mbh.workOrderEstimate.id from MBHeader mbh where mbh.egwStatus.code = ? "
                    + " and (mbh.egBillregister.billstatus <> ? and mbh.egBillregister.billtype = ?))";

            paramList.add(MBHeader.MeasurementBookStatus.APPROVED.toString());
            paramList.add(MBHeader.MeasurementBookStatus.CANCELLED.toString());
            paramList.add(getFinalBillTypeConfigValue());
        }

        // @Todo check action_flag

        logger.debug("Query is ::" + dynQuery);
        if (paramList.isEmpty())
            wolList = findAllBy(dynQuery);
        else {
            params = new Object[paramList.size()];
            params = paramList.toArray(params);
            wolList = findAllBy(dynQuery, params);
        }

        return wolList;

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
        List<WorkOrderActivity> woActivityList;

        String dynQuery = "select distinct woa from WorkOrderActivity woa left join woa.activity.schedule schedule"
                + " left join woa.activity.nonSor nonSor where woa.id != null and woa.workOrderEstimate.estimate.parent is null "
                + " and woa.workOrderEstimate.workOrder.egwStatus.code != 'CANCELLED' ";
        Object[] params;
        final List<Object> paramList = new ArrayList<Object>();

        if (criteriaMap.get(WORKORDER_NO) != null) {
            dynQuery = dynQuery + " and woa.workOrderEstimate.workOrder.workOrderNumber = ? ";
            paramList.add(criteriaMap.get(WORKORDER_NO));
        }
        if (criteriaMap.get(WORKORDER_ESTIMATE_ID) != null) {
            dynQuery = dynQuery + " and woa.workOrderEstimate.estimate.id = ? ";
            paramList.add(criteriaMap.get(WORKORDER_ESTIMATE_ID));
        }
        if (criteriaMap.get(ACTIVITY_DESC) != null) {
            dynQuery = dynQuery + " and (" + "(UPPER(schedule.description) like ?) or ("
                    + " UPPER(nonSor.description)  like ? ))";
            paramList.add("%" + ((String) criteriaMap.get(ACTIVITY_DESC)).toUpperCase() + "%");
            paramList.add("%" + ((String) criteriaMap.get(ACTIVITY_DESC)).toUpperCase() + "%");
        }
        if (criteriaMap.get(ACTIVITY_CODE) != null) {
            dynQuery = dynQuery + " and " + "UPPER(schedule.code) like ? ";
            paramList.add("%" + ((String) criteriaMap.get(ACTIVITY_CODE)).toUpperCase() + "%");
        }
        // @Todo state not in approved and cancelled
        /*
         * dynQuery = dynQuery + "and woa.id not in (select distinct mbd.workOrderActivity.id from MBDetails mbd where " +
         * "mbd.mbHeader.state.previous.value not in (?,?) and mbd.workOrderActivity.id = woa.id)" ;
         * paramList.add(MBHeader.MeasurementBookStatus.APPROVED.toString());
         * paramList.add(MBHeader.MeasurementBookStatus.CANCELLED.toString());
         */

        final Double extraPercentage = worksService.getConfigval();
        double factor = 1;
        if (extraPercentage.doubleValue() > 0)
            factor = 1 + extraPercentage / 100;
        // @Todo ignore quantity of cancelled mb
        if (!"Required".equals(worksService.getWorksConfigValue("ORDER_NUMBER_REQUIRED"))) {
            dynQuery = dynQuery
                    + "and ((woa.approvedQuantity*? > (select sum(mbd.quantity) as sumq from MBDetails mbd "
                    + " where mbd.mbHeader.egwStatus.code != ? group by mbd.workOrderActivity "
                    + "having mbd.workOrderActivity.id = woa.id)) or (select sum(mbd.quantity) as sumq from MBDetails mbd "
                    + " where mbd.mbHeader.egwStatus.code != ? group by mbd.workOrderActivity "
                    + "having mbd.workOrderActivity.id = woa.id) is null)";
            paramList.add(factor);
            paramList.add(MBHeader.MeasurementBookStatus.CANCELLED.toString());
            paramList.add(MBHeader.MeasurementBookStatus.CANCELLED.toString());
        }
        params = new Object[paramList.size()];
        params = paramList.toArray(params);
        woActivityList = genericService.findAllBy(dynQuery, params);

        return woActivityList;
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
        List<WorkOrderActivity> woActivityList;

        String dynQuery = "select distinct woa from WorkOrderActivity woa left join woa.activity.schedule schedule"
                + " left join woa.activity.nonSor nonSor where woa.id is not null ";
        Object[] params;
        final List<Object> paramList = new ArrayList<Object>();

        if (criteriaMap.get(ACTIVITY_DESC) != null)
            dynQuery = dynQuery + " and (" + "(UPPER(schedule.description) like '%"
                    + ((String) criteriaMap.get(ACTIVITY_DESC)).toUpperCase() + "%') or ("
                    + " UPPER(nonSor.description)  like '%" + ((String) criteriaMap.get(ACTIVITY_DESC)).toUpperCase()
                    + "%' ))";
        if (criteriaMap.get(ACTIVITY_CODE) != null)
            dynQuery = dynQuery + " and " + "UPPER(schedule.code) like '%"
                    + ((String) criteriaMap.get(ACTIVITY_CODE)).toUpperCase() + "%'";
        if (criteriaMap.get(WORKORDER_ESTIMATE_ID) != null) {
            if (criteriaMap.get(WORKORDER_ID) != null) {
                dynQuery = dynQuery
                        + " and (woa.workOrderEstimate.estimate.id = ? and woa.workOrderEstimate.workOrder.egwStatus.code=? and woa.workOrderEstimate.workOrder.id = ?) ";
                paramList.add(criteriaMap.get(WORKORDER_ESTIMATE_ID));
                paramList.add(WorksConstants.APPROVED);
                paramList.add(criteriaMap.get(WORKORDER_ID));
            } else {
                dynQuery = dynQuery
                        + " and (woa.workOrderEstimate.estimate.id = ? and woa.workOrderEstimate.workOrder.egwStatus.code=?) ";
                paramList.add(criteriaMap.get(WORKORDER_ESTIMATE_ID));
                paramList.add(WorksConstants.APPROVED);
            }
            dynQuery = dynQuery
                    + " or ((woa.workOrderEstimate.estimate.egwStatus is not null and woa.workOrderEstimate.estimate.egwStatus.code=?)"
                    + " and (woa.workOrderEstimate.estimate.parent is not null and woa.workOrderEstimate.estimate.parent.id = ? ))";
            paramList.add(AbstractEstimate.EstimateStatus.APPROVED.toString());
            paramList.add(criteriaMap.get(WORKORDER_ESTIMATE_ID));
        }
        if (criteriaMap.get(WORKORDER_ID) != null) {
            dynQuery = dynQuery
                    + " and (woa.workOrderEstimate.workOrder.id = ?) or ((woa.workOrderEstimate.workOrder.egwStatus is not null and woa.workOrderEstimate.workOrder.egwStatus.code=?)"
                    + " and (woa.workOrderEstimate.workOrder.parent is not null and woa.workOrderEstimate.workOrder.parent.id = ? ))";
            paramList.add(criteriaMap.get(WORKORDER_ID));
            paramList.add(WorksConstants.APPROVED);
            paramList.add(criteriaMap.get(WORKORDER_ID));
        }

        dynQuery = dynQuery + "and woa.id not in (select distinct mbd.workOrderActivity.id from MBDetails mbd where "
                + "mbd.mbHeader.egwStatus.code not in (?,?) and mbd.workOrderActivity.id = woa.id)";
        paramList.add(MBHeader.MeasurementBookStatus.APPROVED.toString());
        paramList.add(MBHeader.MeasurementBookStatus.CANCELLED.toString());

        params = new Object[paramList.size()];
        params = paramList.toArray(params);
        dynQuery = dynQuery + " order by woa.activity.id asc";
        woActivityList = genericService.findAllBy(dynQuery, params);

        return woActivityList;
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
        List<WorkOrderActivity> woActivityList;
        String dynQuery = "select distinct woa from WorkOrderActivity woa left join woa.activity.schedule schedule"
                + " left join woa.activity.nonSor nonSor where woa.id != null ";
        Object[] params;
        final List<Object> paramList = new ArrayList<Object>();

        if (criteriaMap.get(WORKORDER_NO) != null) {
            dynQuery = dynQuery + " and woa.workOrderEstimate.workOrder.parent.workOrderNumber = ? ";
            paramList.add(criteriaMap.get(WORKORDER_NO));
        }
        if (criteriaMap.get(WORKORDER_ESTIMATE_ID) != null) {
            dynQuery = dynQuery + " and woa.workOrderEstimate.estimate.parent.id = ? ";
            paramList.add(criteriaMap.get(WORKORDER_ESTIMATE_ID));
        }
        if (criteriaMap.get(ACTIVITY_DESC) != null) {
            dynQuery = dynQuery + " and (" + "(UPPER(schedule.description) like ?) or ("
                    + " UPPER(nonSor.description)  like ? ))";
            paramList.add("%" + ((String) criteriaMap.get(ACTIVITY_DESC)).toUpperCase() + "%");
            paramList.add("%" + ((String) criteriaMap.get(ACTIVITY_DESC)).toUpperCase() + "%");
        }
        if (criteriaMap.get(ACTIVITY_CODE) != null) {
            dynQuery = dynQuery + " and " + "UPPER(schedule.code) like ? ";
            paramList.add("%" + ((String) criteriaMap.get(ACTIVITY_CODE)).toUpperCase() + "%");
        }
        // Check Approved REs
        dynQuery = dynQuery + " and  woa.workOrderEstimate.estimate.egwStatus.code=?   ";
        paramList.add(WorksConstants.APPROVED);
        if (criteriaMap.get(REVISION_TYPE) != null) {
            dynQuery = dynQuery + " and woa.activity.revisionType=?  ";
            paramList.add(criteriaMap.get(REVISION_TYPE));

        }

        // @Todo state not in approved and cancelled
        /*
         * dynQuery = dynQuery + "and woa.id not in (select distinct mbd.workOrderActivity.id from MBDetails mbd where " +
         * "mbd.mbHeader.state.previous.value not in (?,?) and mbd.workOrderActivity.id = woa.id)" ;
         * paramList.add(MBHeader.MeasurementBookStatus.APPROVED.toString());
         * paramList.add(MBHeader.MeasurementBookStatus.CANCELLED.toString());
         */

        final Double extraPercentage = worksService.getConfigval();
        double factor = 1;
        if (extraPercentage.doubleValue() > 0)
            factor = 1 + extraPercentage / 100;
        // @Todo ignore quantity of cancelled mb
        if (!"Required".equals(worksService.getWorksConfigValue("ORDER_NUMBER_REQUIRED"))) {
            dynQuery = dynQuery
                    + "and ((woa.approvedQuantity*? > (select sum(mbd.quantity) as sumq from MBDetails mbd "
                    + " where mbd.mbHeader.egwStatus.code != ? group by mbd.workOrderActivity "
                    + "having mbd.workOrderActivity.id = woa.id)) or (select sum(mbd.quantity) as sumq from MBDetails mbd "
                    + " where mbd.mbHeader.egwStatus.code != ? group by mbd.workOrderActivity "
                    + "having mbd.workOrderActivity.id = woa.id) is null)";
            paramList.add(factor);
            paramList.add(MBHeader.MeasurementBookStatus.CANCELLED.toString());
            paramList.add(MBHeader.MeasurementBookStatus.CANCELLED.toString());
        }
        params = new Object[paramList.size()];
        params = paramList.toArray(params);
        woActivityList = genericService.findAllBy(dynQuery, params);
        // /woActivityListOriEst = searchWOActivities(criteriaMap);
        // Remove the SOR items that were present in the original Estimate
        /*
         * if(woActivityListOriEst !=null && !woActivityListOriEst.isEmpty() && woActivityList!=null && !woActivityList.isEmpty())
         * { for(WorkOrderActivity woaOri :woActivityListOriEst) { for(WorkOrderActivity woaRev : woActivityList) {
         * if(woaOri.getActivity().getId()==woaRev.getActivity().getId()) { woActivityList.remove(woaRev); } } } }
         */
        return woActivityList;
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

        String query = "select distinct mbh from MBHeader mbh join mbh.mbDetails as mbDetail ";
        Object[] params;
        final List<Object> paramList = new ArrayList<Object>();
        // if(workOrderActivity.getActivity().getSchedule() != null)
        // query = query +
        // "where mbDetail.workOrderActivity.activity.schedule.id = "+workOrderActivity.getActivity().getSchedule().getId();
        // if(workOrderActivity.getActivity().getNonSor() != null)
        // query = query +
        // "where mbDetail.workOrderActivity.activity.nonSor.id = "+workOrderActivity.getActivity().getNonSor().getId();

        query = query + " where mbDetail.workOrderActivity.id = ? " + " and mbh.id != ? and mbh.egwStatus.code=? "
                + " and mbh.modifiedDate < (select modifiedDate from MBHeader where id = ? )"
                + " order by mbh.modifiedDate desc";
        paramList.add(workOrderActivity.getId());
        paramList.add(mbHeaderId);
        paramList.add(WorksConstants.APPROVED);
        paramList.add(mbHeaderId);

        params = new Object[paramList.size()];
        params = paramList.toArray(params);
        final List<MBHeader> mbHeaderList = genericService.findAllBy(query, params);
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
        final Map<String, Object> criteriaMap = new HashMap<String, Object>();
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

        final Map<Long, EstimateLineItemsForWP> resultMap = new HashMap<Long, EstimateLineItemsForWP>();
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

        final Map<Long, EstimateLineItemsForWP> resultMap = new HashMap<Long, EstimateLineItemsForWP>();
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
        final Collection<EstimateLineItemsForWP> latestEstLineItemList = new ArrayList<EstimateLineItemsForWP>();
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

        final Map<Long, EstimateLineItemsForWP> resultMap = new HashMap<Long, EstimateLineItemsForWP>();
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

    @Override
    public Map createHeaderParams(final WorkOrder workOrder, final String type) {
        final Map<String, Object> reportParams = new HashMap<String, Object>();
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
        final List<AbstractEstimateForWp> aeForWpList = new ArrayList<AbstractEstimateForWp>();
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
        final List<WorkOrderEstimate> aeList = new ArrayList<WorkOrderEstimate>();
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
                WorkOrderActivity woa = null;
                // CHANGE_QUANTITY DONE FOR NON_TENDERED & LUMPSUM ACTIVITIES
                if (detail.getActivity().getParent().getRevisionType() != null
                        && (detail.getActivity().getParent().getRevisionType().equals(RevisionType.NON_TENDERED_ITEM) || detail
                                .getActivity().getParent().getRevisionType().equals(RevisionType.LUMP_SUM_ITEM)))
                    woa = (WorkOrderActivity) genericService.find(
                            "from WorkOrderActivity where activity.id=? and workOrderEstimate.estimate.id=?", detail
                                    .getActivity().getParent().getId(),
                            detail.getActivity().getParent()
                                    .getAbstractEstimate().getId());
                else
                    // CHANGE_QUANTITY DONE FOR ORIGINAL ESTIMATE ACTIVITIES
                    woa = (WorkOrderActivity) genericService.find(
                            "from WorkOrderActivity where activity.id=? and workOrderEstimate.estimate.id=?", detail
                                    .getActivity().getParent().getId(),
                            workOrderEstimate.getEstimate().getParent()
                                    .getId());

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
        final Object wOCommencedDate = persistenceService
                .find(" select stat.statusDate from OfflineStatus stat where stat.objectId = ? and stat.objectType = ? and stat.egwStatus.code = ? ",
                        id, "WorkOrder", WorksConstants.WO_STATUS_WOCOMMENCED);
        return (Date) wOCommencedDate;
    }

    @Override
    public String getWorksPackageName(final String wpNumber) {
        final Object nameOfWO = persistenceService.find(
                " select wp.name from WorksPackage wp where wp.wpNumber = ? and wp.egwStatus.code = ? ", wpNumber,
                WorksConstants.APPROVED);
        return nameOfWO.toString();
    }

    @Override
    public Object getTenderNegotiationInfo(final String negotiationNo) {
        final Object obj = persistenceService
                .find("select tr.percNegotiatedAmountRate,tr.tenderEstimate.tenderType from TenderResponse tr where tr.egwStatus.code = 'APPROVED' and tr.negotiationNumber = ? ",
                        negotiationNo);
        return obj;

    }

    @Override
    public WorkOrderEstimate getWorkOrderEstimateForWOIdAndEstimateId(final Long workOrderId, final Long estimateId) {
        final WorkOrderEstimate workOrderEstimate = (WorkOrderEstimate) genericService
                .find("from WorkOrderEstimate woe where woe.workOrder.id = ? "
                        + "and woe.estimate.id = ? and woe.estimate.egwStatus.code = ? and woe.workOrder.egwStatus.code = ? "
                        + "and woe.workOrder.parent is null ", workOrderId, estimateId,
                        AbstractEstimate.EstimateStatus.ADMIN_SANCTIONED.toString(), WorksConstants.APPROVED);
        return workOrderEstimate;
    }

    @Override
    public List<Object> getWorkOrderDetails(final Long estimateId) {
        final List<Object> woDetails = genericService.findAllBy(
                "select woe.workOrder.id ,woe.workOrder.workOrderNumber from WorkOrderEstimate woe "
                        + " where woe.estimate.id = ? and woe.workOrder.egwStatus.code not in (?,?) ",
                estimateId,
                WorksConstants.NEW, WorksConstants.CANCELLED_STATUS);
        return woDetails;
    }

    @Override
    public Collection<WorkOrderActivity> getActionWorkOrderActivitiesList(
            final List<WorkOrderActivity> actionWorkOrderActivities) {
        return CollectionUtils.select(actionWorkOrderActivities,
                workOrderActivity -> (WorkOrderActivity) workOrderActivity != null);
    }
}

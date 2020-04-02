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
package org.egov.works.web.actions.contractoradvance;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.contractorbill.entity.ContractorBillRegister;
import org.egov.works.models.contractoradvance.ContractorAdvanceRequisition;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.services.contractoradvance.ContractorAdvanceService;
import org.egov.works.utils.WorksConstants;

public class AjaxContractorAdvanceAction extends BaseFormAction {
    /**
     *
     */
    private static final long serialVersionUID = 1017466477668341059L;

    private static final Logger LOGGER = Logger.getLogger(AjaxContractorAdvanceAction.class);

    private static final String ESTIMATE_NUMBER_SEARCH_RESULTS = "estimateNoSearchResults";
    private static final String WORKORDER_NUMBER_SEARCH_RESULTS = "workOrderNoSearchResults";
    private static final String WP_NUMBER_SEARCH_RESULTS = "wpNoSearchResults";
    private static final String TN_NUMBER_SEARCH_RESULTS = "tenderNegotiationNoSearchResults";
    private static final String DRAWINGOFFICER_SEARCH_RESULTS = "drawingOfficers";

    private String query;
    private List<String> estimateNumberSearchList = new LinkedList<>();
    private List<String> workOrderNumberSearchList = new LinkedList<>();
    private List<String> wpNumberSearchList = new LinkedList<>();
    private List<String> tenderNegotiationNumberSearchList = new LinkedList<>();
    private List<HashMap> drawingOfficerList = new LinkedList<>();

    private ContractorAdvanceService contractorAdvanceService;
    private Date advanceRequisitionDate;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public String execute() {
        return SUCCESS;
    }

    @Override
    public Object getModel() {
        return null;
    }

    /*
     * Autocomplete for estimates where WO is approved and part bills are not created
     */
    public String searchEstimateNumber() {
        final StringBuffer strquery = new StringBuffer();
        if (!StringUtils.isEmpty(query)) {
            strquery.append("select distinct(woe.estimate.estimateNumber)")
                    .append(" from WorkOrderEstimate woe")
                    .append(" where woe.estimate.parent is null ")
                    .append(" and NOT EXISTS (select 1 from MBHeader mbh where mbh.workOrderEstimate.id = woe.id and mbh.egwStatus.code = :mbStatus")
                    .append(" and (mbh.egBillregister is not null and mbh.egBillregister.billstatus <> :billStatus)) ")
                    .append(" and woe.workOrder.egwStatus.code = :workOrderStatus and UPPER(woe.estimate.estimateNumber) like '%'||:estimateNumber||'%'")
                    .append(" and woe.estimate.egwStatus.code = :estimateStatus")
                    .append(" order by woe.estimate.estimateNumber");

            estimateNumberSearchList = entityManager.createQuery(strquery.toString(), String.class)
                    .setParameter("mbStatus", MBHeader.MeasurementBookStatus.APPROVED.toString())
                    .setParameter("billStatus", ContractorBillRegister.BillStatus.CANCELLED.toString())
                    .setParameter("workOrderStatus", WorksConstants.APPROVED.toString())
                    .setParameter("estimateNumber", query.toUpperCase())
                    .setParameter("estimateStatus", AbstractEstimate.EstimateStatus.ADMIN_SANCTIONED.toString())
                    .getResultList();

        }
        return ESTIMATE_NUMBER_SEARCH_RESULTS;
    }

    /*
     * Autocomplete for WPs where WO is approved
     */
    public String searchWPNumber() {
        final StringBuffer strquery = new StringBuffer();
        final ArrayList<Object> params = new ArrayList<>();
        if (!StringUtils.isEmpty(query)) {
            strquery.append("select distinct(woe.workOrder.packageNumber)")
                    .append(" from WorkOrderEstimate woe")
                    .append(" where woe.workOrder.parent is null ")
                    .append(" and woe.workOrder.egwStatus.code = :workOrderStatus ")
                    .append(" and UPPER(woe.workOrder.packageNumber) like '%'||:packageNumber||'%'")
                    .append(" order by woe.workOrder.packageNumber");
            params.add(WorksConstants.APPROVED.toString());
            params.add(query.toUpperCase());

            wpNumberSearchList = entityManager.createQuery(strquery.toString(), String.class)
                    .setParameter("workOrderStatus", WorksConstants.APPROVED.toString())
                    .setParameter("packageNumber", query.toUpperCase())
                    .getResultList();

        }
        return WP_NUMBER_SEARCH_RESULTS;
    }

    /*
     * Autocomplete for TNs where WO is approved
     */
    public String searchTNNumber() {
        final StringBuffer strquery = new StringBuffer();
        if (!StringUtils.isEmpty(query)) {
            strquery.append("select distinct(woe.workOrder.negotiationNumber)")
                    .append(" from WorkOrderEstimate woe")
                    .append(" where woe.workOrder.parent is null ")
                    .append(" and woe.workOrder.egwStatus.code = :workOrderStatus")
                    .append(" and UPPER(woe.workOrder.negotiationNumber) like '%'||:negotiationNumber||'%'")
                    .append(" order by woe.workOrder.negotiationNumber");

            tenderNegotiationNumberSearchList = entityManager.createQuery(strquery.toString(), String.class)
                    .setParameter("workOrderStatus", WorksConstants.APPROVED.toString())
                    .setParameter("negotiationNumber", query.toUpperCase())
                    .getResultList();

        }
        return TN_NUMBER_SEARCH_RESULTS;
    }

    /*
     * Autocomplete for Approved WOs
     */
    public String searchWorkOrderNumber() {
        final StringBuffer strquery = new StringBuffer();
        if (!StringUtils.isEmpty(query)) {
            strquery.append("select distinct(woe.workOrder.workOrderNumber)")
                    .append(" from WorkOrderEstimate woe")
                    .append(" where woe.workOrder.parent is null ")
                    .append(" and woe.workOrder.egwStatus.code = :workOrderStatus")
                    .append(" and UPPER(woe.workOrder.workOrderNumber) like '%'||:workOrderNumber||'%'")
                    .append(" order by woe.workOrder.workOrderNumber");

            workOrderNumberSearchList = entityManager.createQuery(strquery.toString(), String.class)
                    .setParameter("workOrderStatus", WorksConstants.APPROVED.toString())
                    .setParameter("workOrderNumber", query.toUpperCase())
                    .getResultList();

        }
        return WORKORDER_NUMBER_SEARCH_RESULTS;
    }

    public String searchDrawingOfficer() {
        try {
            drawingOfficerList = contractorAdvanceService.getDrawingOfficerListForARF(query, advanceRequisitionDate);
        } catch (final Exception e) {
            LOGGER.error("Error in method searchDrawingOfficer:::" + e.getMessage());
        }
        return DRAWINGOFFICER_SEARCH_RESULTS;
    }

    /*
     * Autocomplete for distinct estimates from ARF
     */
    public String searchEstimateNumberFromARF() {
        final StringBuffer strquery = new StringBuffer();
        if (!StringUtils.isEmpty(query)) {
            strquery.append("select distinct(arf.workOrderEstimate.estimate.estimateNumber)")
                    .append(" from ContractorAdvanceRequisition arf")
                    .append(" where arf.status.code <> :carStatus and ")
                    .append(" UPPER(arf.workOrderEstimate.estimate.estimateNumber) like '%'||:estimateNumber||'%'")
                    .append("  order by arf.workOrderEstimate.estimate.estimateNumber");

            estimateNumberSearchList = entityManager.createQuery(strquery.toString(), String.class)
                    .setParameter("carStatus", ContractorAdvanceRequisition.ContractorAdvanceRequisitionStatus.NEW.toString())
                    .setParameter("estimateNumber", query.toUpperCase())
                    .getResultList();

        }
        return ESTIMATE_NUMBER_SEARCH_RESULTS;
    }

    /*
     * Autocomplete for Approved WOs
     */
    public String searchWorkOrderNumberFromARF() {
        final StringBuffer strquery = new StringBuffer();
        if (!StringUtils.isEmpty(query)) {
            strquery.append("select distinct(arf.workOrderEstimate.workOrder.workOrderNumber)")
                    .append(" from ContractorAdvanceRequisition arf")
                    .append(" where arf.status.code <> :arfStatus")
                    .append(" and UPPER(arf.workOrderEstimate.workOrder.workOrderNumber) like '%'||:workOrderNumber||'%'")
                    .append(" order by arf.workOrderEstimate.workOrder.workOrderNumber");

            workOrderNumberSearchList = entityManager.createQuery(strquery.toString(), String.class)
                    .setParameter("arfStatus", ContractorAdvanceRequisition.ContractorAdvanceRequisitionStatus.NEW.toString())
                    .setParameter("workOrderNumber", query.toUpperCase())
                    .getResultList();

        }
        return WORKORDER_NUMBER_SEARCH_RESULTS;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(final String query) {
        this.query = query;
    }

    public List<String> getEstimateNumberSearchList() {
        return estimateNumberSearchList;
    }

    public List<String> getWorkOrderNumberSearchList() {
        return workOrderNumberSearchList;
    }

    public List<String> getWpNumberSearchList() {
        return wpNumberSearchList;
    }

    public List<String> getTenderNegotiationNumberSearchList() {
        return tenderNegotiationNumberSearchList;
    }

    public List<HashMap> getDrawingOfficerList() {
        return drawingOfficerList;
    }

    public void setDrawingOfficerList(final List<HashMap> drawingOfficerList) {
        this.drawingOfficerList = drawingOfficerList;
    }

    public ContractorAdvanceService getContractorAdvanceService() {
        return contractorAdvanceService;
    }

    public void setContractorAdvanceService(
            final ContractorAdvanceService contractorAdvanceService) {
        this.contractorAdvanceService = contractorAdvanceService;
    }

    public Date getAdvanceRequisitionDate() {
        return advanceRequisitionDate;
    }

    public void setAdvanceRequisitionDate(final Date advanceRequisitionDate) {
        this.advanceRequisitionDate = advanceRequisitionDate;
    }

}
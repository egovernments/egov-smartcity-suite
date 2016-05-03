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
package org.egov.works.web.actions.milestone;

import org.apache.commons.lang.StringUtils;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.estimate.ProjectCode;
import org.egov.works.models.masters.MilestoneTemplate;
import org.egov.works.models.milestone.Milestone;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.utils.WorksConstants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class AjaxMilestoneAction extends BaseFormAction {

    private static final long serialVersionUID = -6739009668177776477L;
    private MilestoneTemplate milestoneTemplate = new MilestoneTemplate();
    private static final String MILESTONECHECK = "milestoneCheck";
    private static final String SEARCH_RESULTS = "searchResults";
    private static final String ACTIVITIES = "activities";
    private int status;
    private String code;
    private long workTypeId;
    private long subTypeId;
    private String query;
    private Long workOrderEstimateId;
    private boolean milestoneexistsOrNot;
    private boolean woWorkCommenced;
    private static final String OBJECT_TYPE = "WorkOrder";
    private List<Milestone> workOrdEstList = new LinkedList<Milestone>();
    private String workOrderEstimates;
    private List<ProjectCode> projectCodeList = new LinkedList<ProjectCode>();
    private List<WorkOrder> workOrderList = new LinkedList<WorkOrder>();
    private List<AbstractEstimate> estimateList = new LinkedList<AbstractEstimate>();

    @Override
    public Object getModel() {
        // TODO Auto-generated method stub
        return milestoneTemplate;
    }

    public String searchAjax() {
        return SEARCH_RESULTS;
    }

    public Collection<MilestoneTemplate> getMilestoneTemplateList() {
        String strquery = "";
        final ArrayList<Object> params = new ArrayList<Object>();
        if (workTypeId > 0) {
            strquery = "from MilestoneTemplate mt where upper(mt.code) like '%'||?||'%'" + " and mt.workType.id=?";
            params.add(query.toUpperCase());
            params.add(workTypeId);
        }
        if (subTypeId > 0) {
            strquery += " and mt.subType.id=?";
            params.add(subTypeId);
        }
        return getPersistenceService().findAllBy(strquery, params.toArray());
    }

    public String findByCode() {
        milestoneTemplate = (MilestoneTemplate) getPersistenceService().find(
                "from MilestoneTemplate where upper(code)=?", code.toUpperCase());

        return ACTIVITIES;
    }

    public String checkMilestone() {
        getMilestoneCheck();
        return MILESTONECHECK;
    }

    public void getMilestoneCheck() {
        milestoneexistsOrNot = false;
        woWorkCommenced = false;
        Long milestoneId = null;
        if (workOrderEstimateId != null)
            if (getPersistenceService().find("from Milestone where workOrderEstimate.id=? and egwStatus.code<>?",
                    workOrderEstimateId, "CANCELLED") != null)
                milestoneId = (Long) getPersistenceService().find(
                        "select m.id from Milestone m where m.workOrderEstimate.id=? and egwStatus.code<>?",
                        workOrderEstimateId, "CANCELLED");
        if (milestoneId != null) {
            milestoneexistsOrNot = true;
            if (getPersistenceService()
                    .find("from WorkOrderEstimate as woe where woe.workOrder.egwStatus.code=?  and woe.id=? and woe.workOrder.id in ( select stat.objectId from "
                            + " OfflineStatus stat where stat.egwStatus.code= ? and stat.id = (select max(stat1.id) from OfflineStatus stat1 where stat1.objectType='"
                            + OBJECT_TYPE + "' and woe.workOrder.id=stat1.objectId))", "APPROVED", workOrderEstimateId,
                            "Work commenced") != null)
                woWorkCommenced = true;
        } else {
            milestoneexistsOrNot = false;
            woWorkCommenced = false;
        }
    }

    public String searchProjectCodeForMileStone() {
        if (!StringUtils.isEmpty(query)) {
            String strquery = "";
            final ArrayList<Object> params = new ArrayList<Object>();
            strquery = "select distinct(ms.workOrderEstimate.estimate.projectCode) from Milestone ms where upper(ms.workOrderEstimate.estimate.projectCode.code) like '%'||?||'%'"
                    + " and ms.workOrderEstimate.estimate.egwStatus.code=? and ms.egwStatus.code=?";
            params.add(query.toUpperCase());
            params.add(WorksConstants.ADMIN_SANCTIONED_STATUS);
            params.add(WorksConstants.APPROVED);
            projectCodeList = getPersistenceService().findAllBy(strquery, params.toArray());
        }
        return "projectCodeSearchResults";
    }

    public String searchEstimateForMileStone() {
        if (!StringUtils.isEmpty(query)) {
            String strquery = "";
            final ArrayList<Object> params = new ArrayList<Object>();
            strquery = "select ms.workOrderEstimate.estimate from Milestone ms where upper(ms.workOrderEstimate.estimate.estimateNumber) like '%'||?||'%'"
                    + " and ms.workOrderEstimate.estimate.egwStatus.code=? and ms.egwStatus.code=? ";
            params.add(query.toUpperCase());
            params.add(WorksConstants.ADMIN_SANCTIONED_STATUS);
            params.add(WorksConstants.APPROVED);
            estimateList = getPersistenceService().findAllBy(strquery, params.toArray());
        }
        return "estimateSearchResults";
    }

    public String searchWorkOrdNumForMileStone() {
        if (!StringUtils.isEmpty(query)) {
            String strquery = "";
            final ArrayList<Object> params = new ArrayList<Object>();
            strquery = "select distinct(ms.workOrderEstimate.workOrder) from Milestone ms where upper(ms.workOrderEstimate.workOrder.workOrderNumber) like '%'||?||'%'"
                    + " and ms.workOrderEstimate.workOrder.egwStatus.code=? and ms.egwStatus.code=?";
            params.add(query.toUpperCase());
            params.add(WorksConstants.APPROVED);
            params.add(WorksConstants.APPROVED);
            workOrderList = getPersistenceService().findAllBy(strquery, params.toArray());
        }
        return "workOrderNoSearchResults";
    }

    public MilestoneTemplate getMilestoneTemplate() {
        return milestoneTemplate;
    }

    public void setMilestoneTemplate(final MilestoneTemplate milestoneTemplate) {
        this.milestoneTemplate = milestoneTemplate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(final int status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public long getWorkTypeId() {
        return workTypeId;
    }

    public void setWorkTypeId(final long workTypeId) {
        this.workTypeId = workTypeId;
    }

    public long getSubTypeId() {
        return subTypeId;
    }

    public void setSubTypeId(final long subTypeId) {
        this.subTypeId = subTypeId;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(final String query) {
        this.query = query;
    }

    public Long getWorkOrderEstimateId() {
        return workOrderEstimateId;
    }

    public void setWorkOrderEstimateId(final Long workOrderEstimateId) {
        this.workOrderEstimateId = workOrderEstimateId;
    }

    public boolean isMilestoneexistsOrNot() {
        return milestoneexistsOrNot;
    }

    public void setMilestoneexistsOrNot(final boolean milestoneexistsOrNot) {
        this.milestoneexistsOrNot = milestoneexistsOrNot;
    }

    public boolean isWoWorkCommenced() {
        return woWorkCommenced;
    }

    public void setWoWorkCommenced(final boolean woWorkCommenced) {
        this.woWorkCommenced = woWorkCommenced;
    }

    public List<Milestone> getWorkOrdEstList() {
        return workOrdEstList;
    }

    public void setWorkOrdEstList(final List<Milestone> workOrdEstList) {
        this.workOrdEstList = workOrdEstList;
    }

    public String getWorkOrderEstimates() {
        return workOrderEstimates;
    }

    public void setWorkOrderEstimates(final String workOrderEstimates) {
        this.workOrderEstimates = workOrderEstimates;
    }

    public List<ProjectCode> getProjectCodeList() {
        return projectCodeList;
    }

    public void setProjectCodeList(final List<ProjectCode> projectCodeList) {
        this.projectCodeList = projectCodeList;
    }

    public List<WorkOrder> getWorkOrderList() {
        return workOrderList;
    }

    public void setWorkOrderList(final List<WorkOrder> workOrderList) {
        this.workOrderList = workOrderList;
    }

    public List<AbstractEstimate> getEstimateList() {
        return estimateList;
    }

    public void setEstimateList(final List<AbstractEstimate> estimateList) {
        this.estimateList = estimateList;
    }

}

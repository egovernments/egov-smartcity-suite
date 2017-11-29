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
package org.egov.works.web.actions.tender;

import org.apache.commons.lang.StringUtils;
import org.egov.infra.web.struts.actions.SearchFormAction;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.models.tender.TenderEstimate;
import org.egov.works.models.tender.TenderResponseActivity;
import org.egov.works.utils.WorksConstants;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SearchTenderResponseActivitiesAction extends SearchFormAction {

    private static final long serialVersionUID = 8616631394931994625L;
    private Long tenderRespContrId;
    private Long tenderRespId;
    private String activityType;
    private String sorCode;
    private String activityDesc;
    private Long estimateId;
    private String estimateName;
    private Long activityId;
    private String negotiationNumber;
    private double assignedQty;
    private String recordId;
    private String selectedactivities;

    @Override
    public Object getModel() {
        return null;
    }

    @Override
    public String execute() {
        return INDEX;
    }

    public SearchTenderResponseActivitiesAction() {
    }

    @Override
    public void prepare() {
        super.prepare();
        final List<AbstractEstimate> estimateList = new ArrayList<AbstractEstimate>();
        TenderEstimate tenderEstimate = null;
        if (tenderRespId != null)
            tenderEstimate = (TenderEstimate) getPersistenceService().find(
                    "select tr.tenderEstimate from TenderResponse tr where tr.id=?", tenderRespId);
        if (tenderEstimate != null && tenderEstimate.getAbstractEstimate() == null)
            estimateList.addAll(tenderEstimate.getWorksPackage().getAllEstimates());
        else if (tenderEstimate != null && tenderEstimate.getWorksPackage() == null)
            estimateList.add(tenderEstimate.getAbstractEstimate());
        // List<TenderResponseActivity>
        // tenderResponseList=getPersistenceService().findAllBy("select tra.activity from TenderResponseActivity tra where
        // tra.tenderResponse.id=?",tenderRespId);
        addDropdownData("estimateList", estimateList);
    }

    @Override
    public SearchQuery prepareQuery(final String sortField, final String sortOrder) {
        final StringBuilder sb = new StringBuilder(300);
        final List<Object> paramList = new ArrayList<Object>();
        int counter = 0;
        sb.append("from TenderResponseActivity as tra left join tra.activity.schedule schedule"
                + " left join tra.activity.nonSor nonSor where tra.tenderResponse.id= ?");
        paramList.add(tenderRespId);
        counter++;
        if (StringUtils.isNotBlank(activityType) && activityType.equalsIgnoreCase("SOR"))
            sb.append(" and schedule is not null");
        if (StringUtils.isNotBlank(activityType) && activityType.equalsIgnoreCase("Non SOR"))
            sb.append(" and nonSor is not null");
        if (StringUtils.isNotBlank(sorCode)) {
            sb.append(" and UPPER(schedule.code) like ?");
            paramList.add("%" + sorCode.toUpperCase() + "%");
            counter++;
        }
        if (StringUtils.isNotBlank(activityDesc)) {
            sb.append(" and ((UPPER(schedule.description) like ?) or " + "(UPPER(nonSor.description) like ? ))");
            paramList.add("%" + activityDesc.toUpperCase() + "%");
            counter++;
            paramList.add("%" + activityDesc.toUpperCase() + "%");
            counter++;
        }
        if (StringUtils.isNotBlank(estimateName)) {
            sb.append(" and UPPER(tra.activity.abstractEstimate.name) like ?");
            paramList.add("%" + estimateName.toUpperCase() + "%");
            counter++;
        }

        if (estimateId != null && estimateId != -1) {
            sb.append(" and tra.activity.abstractEstimate.id= ?");
            paramList.add(estimateId);
            counter++;
        }

        if (StringUtils.isNotBlank(selectedactivities)) {
            sb.append(" and tra.activity.id not in(?").append(counter).append(")");
            final String[] activitiesId = selectedactivities.split(",");
            final List<Long> activitiesIdList = new ArrayList<Long>();
            for (final String element : activitiesId)
                activitiesIdList.add(Long.valueOf(element));
            paramList.add(activitiesIdList);
        }

        sb.append(" order by tra.activity.abstractEstimate.id");

        final String query = sb.toString();
        final String countQuery = "select count(*) " + query;
        return new SearchQueryHQL(query, countQuery, paramList);
    }

    @Override
    public String search() {
        setPageSize(100);
        final String retVal = super.search();
        populateAssignedQunatity();
        if (searchResult.getFullListSize() == 0)
            addFieldError("result not found", "No results found for search parameters");
        return retVal;
    }

    private void populateAssignedQunatity() {
        final List<TenderResponseActivity> tenderResponseActivityList = new LinkedList<TenderResponseActivity>();

        final Iterator iter = searchResult.getList().iterator();
        while (iter.hasNext()) {
            final Object[] row = (Object[]) iter.next();
            final TenderResponseActivity tenderResponseActivity = (TenderResponseActivity) row[0];
            final double assignedQty = getAssignedQuantity(tenderResponseActivity.getActivity().getId(),
                    tenderResponseActivity.getTenderResponse().getNegotiationNumber());
            if (assignedQty < tenderResponseActivity.getNegotiatedQuantity()) {
                tenderResponseActivity.setAssignedQty(assignedQty);
                tenderResponseActivityList.add(tenderResponseActivity);
            }
        }
        searchResult.getList().clear();
        searchResult.getList().addAll(tenderResponseActivityList);
    }

    private double getAssignedQuantity(final Long activityId, final String negotiationNumber) {
        Object[] params = new Object[] { negotiationNumber, WorksConstants.CANCELLED_STATUS, activityId };
        Double assignedQty = (Double) getPersistenceService()
                .findByNamedQuery("getAssignedQuantityForActivity", params);
        params = new Object[] { negotiationNumber, WorksConstants.NEW, activityId };
        final Double assignedQtyForNew = (Double) getPersistenceService().findByNamedQuery(
                "getAssignedQuantityForActivityForNewWO", params);

        if (assignedQty != null && assignedQtyForNew != null)
            assignedQty = assignedQty + assignedQtyForNew;
        if (assignedQty == null && assignedQtyForNew != null)
            assignedQty = assignedQtyForNew;
        if (assignedQty == null)
            return 0.0d;
        else
            return assignedQty.doubleValue();
    }

    public String getAssignedQuantity() {
        assignedQty = getAssignedQuantity(activityId, negotiationNumber);
        return "assignedQty";
    }

    public Long getTenderRespContrId() {
        return tenderRespContrId;
    }

    public void setTenderRespContrId(final Long tenderRespContrId) {
        this.tenderRespContrId = tenderRespContrId;
    }

    public Long getTenderRespId() {
        return tenderRespId;
    }

    public void setTenderRespId(final Long tenderRespId) {
        this.tenderRespId = tenderRespId;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(final String activityType) {
        this.activityType = activityType;
    }

    public String getSorCode() {
        return sorCode;
    }

    public void setSorCode(final String sorCode) {
        this.sorCode = sorCode;
    }

    public String getActivityDesc() {
        return activityDesc;
    }

    public void setActivityDesc(final String activityDesc) {
        this.activityDesc = activityDesc;
    }

    public Long getEstimateId() {
        return estimateId;
    }

    public void setEstimateId(final Long estimateId) {
        this.estimateId = estimateId;
    }

    public String getEstimateName() {
        return estimateName;
    }

    public void setEstimateName(final String estimateName) {
        this.estimateName = estimateName;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(final Long activityId) {
        this.activityId = activityId;
    }

    public String getNegotiationNumber() {
        return negotiationNumber;
    }

    public void setNegotiationNumber(final String negotiationNumber) {
        this.negotiationNumber = negotiationNumber;
    }

    public double getAssignedQty() {
        return assignedQty;
    }

    public void setAssignedQty(final double assignedQty) {
        this.assignedQty = assignedQty;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(final String recordId) {
        this.recordId = recordId;
    }

    public String getSelectedactivities() {
        return selectedactivities;
    }

    public void setSelectedactivities(final String selectedactivities) {
        this.selectedactivities = selectedactivities;
    }
}

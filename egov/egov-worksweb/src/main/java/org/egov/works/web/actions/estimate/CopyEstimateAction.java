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
package org.egov.works.web.actions.estimate;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.workflow.service.WorkflowService;
import org.egov.pims.service.EmployeeServiceOld;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.abstractestimate.entity.Activity;
import org.egov.works.abstractestimate.entity.AssetsForEstimate;
import org.egov.works.abstractestimate.entity.MultiYearEstimate;
import org.egov.works.abstractestimate.entity.NonSor;
import org.egov.works.abstractestimate.entity.OverheadValue;
import org.egov.works.services.AbstractEstimateService;
import org.egov.works.services.WorksService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ParentPackage("egov")
@Result(name = CopyEstimateAction.SUCCESS, location = "copyEstimate-success.jsp")
public class CopyEstimateAction extends BaseFormAction {

    private static final long serialVersionUID = 4934369735700310753L;
    private Long estimateId;
    private String copyCancelledEstNum;
    private AbstractEstimate copyEstimate = new AbstractEstimate();
    private AbstractEstimateService abstractEstimateService;
    @Autowired
    private EmployeeServiceOld employeeService;
    private Date financialYearStartDate;
    private String messageKey;
    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;
    @Autowired
    private FinancialYearHibernateDAO finHibernateDo;
    private WorksService worksService;

    @Override
    public void prepare() {
        super.prepare();
    }

    public String copyEstimate() {
        final AbstractEstimate abstractEstimate = abstractEstimateService.findById(estimateId, false);
        employeeService
                .getEmpForUserId(worksService.getCurrentLoggedInUserId());

        copyEstimate.setWard(abstractEstimate.getWard());
        copyEstimate.setName(abstractEstimate.getName());
        copyEstimate.setEstimateDate(abstractEstimate.getEstimateDate());
        copyEstimate.setDescription(abstractEstimate.getDescription());
        copyEstimate.setLocation(abstractEstimate.getLocation());
        copyEstimate.setNatureOfWork(abstractEstimate.getNatureOfWork());
        copyEstimate.setCategory(abstractEstimate.getCategory());
        copyEstimate.setParentCategory(abstractEstimate.getParentCategory());
        copyEstimate.setUserDepartment(abstractEstimate.getUserDepartment());
        copyEstimate.setExecutingDepartment(abstractEstimate.getExecutingDepartment());
        copyEstimate.setFundSource(abstractEstimate.getFundSource());
        copyEstimate.setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode("AbstractEstimate", "NEW"));

        copyEstimate.setOverheadValues(cloneOverheadValue(abstractEstimate.getOverheadValues()));
        copyEstimate.setActivities(cloneActivity(abstractEstimate.getActivities()));
        copyEstimate.setAssetValues(closeAssetForEstimate(abstractEstimate.getAssetValues()));
        copyEstimate.setMultiYearEstimates(cloneMultiYearEstimate(abstractEstimate.getMultiYearEstimates()));

        copyEstimate.setWorkValue(abstractEstimate.getWorkValue());
        copyEstimate.setCopiedEstimate(true);

        // loggedInEmp.getAssignment(getFinancialYearStartDate()).getPosition();

        // TODO - workflowService.start hase been removed. Need to find
        // alternative.
        // copyEstimate = (AbstractEstimate) workflowService.start(copyEstimate,
        // owner, "Copy Estimate created");
        copyEstimate = abstractEstimateService.persist(copyEstimate);

        // set estimate number
        if (abstractEstimate.getEstimateNumber().endsWith("/C") && copyCancelledEstNum.equals("yes")) {
            final String estNum = abstractEstimate.getEstimateNumber().substring(0,
                    abstractEstimate.getEstimateNumber().length() - 2);
            copyEstimate.setEstimateNumber(estNum);
        } else
            // EstimateNumberGenerator is invoked
            abstractEstimateService.setEstimateNumber(copyEstimate);

        messageKey = "The estimate was copied successfully from estimate " + abstractEstimate.getEstimateNumber()
                + " to estimate " + copyEstimate.getEstimateNumber();
        return SUCCESS;
    }

    private List<OverheadValue> cloneOverheadValue(final List<OverheadValue> overHeadValueList) {
        final List<OverheadValue> newOverHeadList = new ArrayList<OverheadValue>();
        for (final OverheadValue overhead : overHeadValueList) {
            final OverheadValue newOverhead = new OverheadValue();

            newOverhead.setAmount(overhead.getAmount());
            newOverhead.setOverhead(overhead.getOverhead());
            newOverhead.setAbstractEstimate(copyEstimate);

            newOverHeadList.add(newOverhead);
        }
        return newOverHeadList;
    }

    private List<MultiYearEstimate> cloneMultiYearEstimate(final List<MultiYearEstimate> multiYearEstList) {
        final List<MultiYearEstimate> newMultiYearEstList = new ArrayList<MultiYearEstimate>();
        for (final MultiYearEstimate multiYearEst : multiYearEstList) {
            final MultiYearEstimate newMultiYearEst = new MultiYearEstimate();

            newMultiYearEst.setAbstractEstimate(copyEstimate);
            newMultiYearEst.setFinancialYear(multiYearEst.getFinancialYear());
            newMultiYearEst.setPercentage(multiYearEst.getPercentage());

            newMultiYearEstList.add(newMultiYearEst);
        }
        return newMultiYearEstList;
    }

    private List<Activity> cloneActivity(final List<Activity> activityList) {
        final List<Activity> newActivityList = new ArrayList<Activity>();
        for (final Activity activity : activityList) {
            final Activity newActivity = new Activity();

            newActivity.setUom(activity.getUom());
            newActivity.setSchedule(activity.getSchedule());
            newActivity.setServiceTaxPerc(activity.getServiceTaxPerc());
            newActivity.setQuantity(activity.getQuantity());
            newActivity.setRate(activity.getRate());
            newActivity.setAbstractEstimate(copyEstimate);
            if (activity.getNonSor() != null) {
                final NonSor nonSORObj = new NonSor();
                nonSORObj.setDescription(activity.getNonSor().getDescription());
                nonSORObj.setUom(activity.getNonSor().getUom());
                newActivity.setNonSor(nonSORObj);
            } else
                newActivity.setNonSor(null);
            newActivityList.add(newActivity);
        }
        return newActivityList;
    }

    private List<AssetsForEstimate> closeAssetForEstimate(final List<AssetsForEstimate> assetEstList) {
        final List<AssetsForEstimate> newAssetEstList = new ArrayList<AssetsForEstimate>();
        for (final AssetsForEstimate asset : assetEstList) {
            final AssetsForEstimate newAssetEst = new AssetsForEstimate();

            newAssetEst.setAbstractEstimate(copyEstimate);
            newAssetEst.setAsset(asset.getAsset());

            newAssetEstList.add(newAssetEst);
        }
        return newAssetEstList;
    }

    @Override
    public Object getModel() {

        return null;
    }

    public Date getFinancialYearStartDate() {
        financialYearStartDate = finHibernateDo.getFinancialYearByFinYearRange(
                worksService.getWorksConfigValue("FINANCIAL_YEAR_RANGE")).getStartingDate();
        return financialYearStartDate;
    }

    public void setFinancialYearStartDate(final Date financialYearStartDate) {
        this.financialYearStartDate = financialYearStartDate;
    }

    public Long getEstimateId() {
        return estimateId;
    }

    public void setEstimateId(final Long estimateId) {
        this.estimateId = estimateId;
    }

    public AbstractEstimateService getAbstractEstimateService() {
        return abstractEstimateService;
    }

    public void setAbstractEstimateService(final AbstractEstimateService abstractEstimateService) {
        this.abstractEstimateService = abstractEstimateService;
    }

    public EmployeeServiceOld getEmployeeService() {
        return employeeService;
    }

    public void setEmployeeService(final EmployeeServiceOld employeeService) {
        this.employeeService = employeeService;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(final String messageKey) {
        this.messageKey = messageKey;
    }

    public WorksService getWorksService() {
        return worksService;
    }

    public void setWorksService(final WorksService worksService) {
        this.worksService = worksService;
    }

    public String getCopyCancelledEstNum() {
        return copyCancelledEstNum;
    }

    public void setCopyCancelledEstNum(final String copyCancelledEstNum) {
        this.copyCancelledEstNum = copyCancelledEstNum;
    }

    public AbstractEstimate getCopyEstimate() {
        return copyEstimate;
    }

    public void setCopyEstimate(final AbstractEstimate copyEstimate) {
        this.copyEstimate = copyEstimate;
    }

    public void setEstimateWorkflowService(final WorkflowService<AbstractEstimate> workflow) {
    }
}

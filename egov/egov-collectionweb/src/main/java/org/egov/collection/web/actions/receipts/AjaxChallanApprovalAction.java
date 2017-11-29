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
package org.egov.collection.web.actions.receipts;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.collection.service.ReceiptHeaderService;
import org.egov.collection.utils.CollectionsUtil;
import org.egov.commons.exception.NoSuchObjectException;
import org.egov.eis.entity.EmployeeView;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.pims.commons.Designation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ParentPackage("egov")
@Results({
        @Result(name = "userList", location = "ajaxChallanApproval-userList.jsp"),
        @Result(name = "designationList", location = "ajaxChallanApproval-designationList.jsp")
})
public class AjaxChallanApprovalAction extends BaseFormAction {
    private static final long serialVersionUID = 1L;
    private static final String USERLIST = "userList";
    private static final String DESIGNATIONLIST = "designationList";
    private Integer designationId;
    private Integer approverDeptId;
    private Long receiptheaderId;
    private List<EmployeeView> postionUserList = new ArrayList<EmployeeView>(0);
    private List<Designation> designationMasterList = new ArrayList<Designation>(0);
    private CollectionsUtil collectionsUtil;
    private ReceiptHeaderService receiptHeaderService;

    @Override
    public Object getModel() {

        return null;
    }

    @Action(value = "/receipts/ajaxChallanApproval-positionUserList")
    public String positionUserList() {
        if (designationId != null && approverDeptId != null)
            try
            {
                postionUserList = collectionsUtil.getPositionBySearchParameters(null, designationId, approverDeptId, null, null,
                        new Date(), 0);
            } catch (final NoSuchObjectException e) {
                throw new ApplicationRuntimeException("Designation Postion not found", e);
            }
        return USERLIST;

    }

    @Action(value = "/receipts/ajaxChallanApproval-approverDesignationList")
    public String approverDesignationList() {
        if (approverDeptId != null)
            designationMasterList = collectionsUtil.getDesignationsAllowedForChallanApproval(approverDeptId);

        return DESIGNATIONLIST;
    }

    /**
     * @param designationId the designationId to set
     */
    public void setDesignationId(final Integer designationId) {
        this.designationId = designationId;
    }

    /**
     * @return the postionUserList
     */
    public List<EmployeeView> getPostionUserList() {
        return postionUserList;
    }

    /**
     * @param collectionsUtil the collectionsUtil to set
     */
    public void setCollectionsUtil(final CollectionsUtil collectionsUtil) {
        this.collectionsUtil = collectionsUtil;
    }

    /**
     * @return the approverDeptId
     */
    public Integer getApproverDeptId() {
        return approverDeptId;
    }

    /**
     * @param approverDeptId the approverDeptId to set
     */
    public void setApproverDeptId(final Integer approverDeptId) {
        this.approverDeptId = approverDeptId;
    }

    /**
     * @return the designationMasterList
     */
    public List<Designation> getDesignationMasterList() {
        return designationMasterList;
    }

    /**
     * @param receiptHeaderService the receiptHeaderService to set
     */
    public void setReceiptHeaderService(final ReceiptHeaderService receiptHeaderService) {
        this.receiptHeaderService = receiptHeaderService;
    }

    /**
     * @return the receiptheaderId
     */
    public Long getReceiptheaderId() {
        return receiptheaderId;
    }

    /**
     * @param receiptheaderId the receiptheaderId to set
     */
    public void setReceiptheaderId(final Long receiptheaderId) {
        this.receiptheaderId = receiptheaderId;
    }

}

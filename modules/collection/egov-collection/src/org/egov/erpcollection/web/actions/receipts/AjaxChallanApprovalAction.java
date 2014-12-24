package org.egov.erpcollection.web.actions.receipts;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.erpcollection.services.ReceiptHeaderService;
import org.egov.erpcollection.util.CollectionsUtil;
import org.egov.exceptions.NoSuchObjectException;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.model.EmployeeView;
import org.egov.web.actions.BaseFormAction;

public class AjaxChallanApprovalAction extends BaseFormAction{
	private static final long serialVersionUID = 1L;
	private static final String USERLIST = "userList";
	private static final String DESIGNATIONLIST = "designationList";
	private Integer designationId;
	private Integer approverDeptId;
	private Long receiptheaderId;
	private List<EmployeeView> postionUserList = new ArrayList<EmployeeView>();
	private List<DesignationMaster> designationMasterList = new ArrayList<DesignationMaster>();
	private CollectionsUtil collectionsUtil;
	private ReceiptHeaderService receiptHeaderService;
	
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String positionUserList() {
		if(designationId!=null && approverDeptId!=null)
		{	try
			{
				postionUserList=collectionsUtil.getPositionBySearchParameters(null,designationId,approverDeptId,null,null,new Date(),0);
			}	
			catch(NoSuchObjectException e){
				throw new EGOVRuntimeException("Designation Postion not found",e);
			}	
		}
		return USERLIST;
		 
	}
	
	public String approverDesignationList(){
		if(approverDeptId!=null)
		{
			
			designationMasterList=collectionsUtil.getDesignationsAllowedForChallanApproval(
					approverDeptId,receiptHeaderService.findById(receiptheaderId, false));
		}	
		
		return DESIGNATIONLIST;
	}
	
	
	/**
	 * @param designationId the designationId to set
	 */
	public void setDesignationId(Integer designationId) {
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
	public void setCollectionsUtil(CollectionsUtil collectionsUtil) {
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
	public void setApproverDeptId(Integer approverDeptId) {
		this.approverDeptId = approverDeptId;
	}


	/**
	 * @return the designationMasterList
	 */
	public List<DesignationMaster> getDesignationMasterList() {
		return designationMasterList;
	}


	/**
	 * @param receiptHeaderService the receiptHeaderService to set
	 */
	public void setReceiptHeaderService(ReceiptHeaderService receiptHeaderService) {
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
	public void setReceiptheaderId(Long receiptheaderId) {
		this.receiptheaderId = receiptheaderId;
	}


	
}

/**
 * 
 */
package org.egov.works.web.actions.serviceOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.commons.Functionary;
import org.egov.egf.bills.model.Cbill;
import org.egov.lib.rjbac.user.UserImpl;
import org.egov.pims.model.EmployeeView;
import org.egov.services.voucher.VoucherService;
import org.egov.web.actions.BaseFormAction;

/**
 * @author manoranjan
 *
 */
public class CommonAction extends BaseFormAction{
	
	
	private static final long serialVersionUID = 1L;
	private static final Logger	LOGGER	= Logger.getLogger(CommonAction.class);
	private Long billRegisterId;
	private VoucherService voucherService;
	private Integer departmentId;
	private  List<Map<String, Object>> designationList;
	private String functionaryName;
	private  List<UserImpl> userList;
	private Integer  designationId;
	
	public String ajaxLoadDesg(){
		LOGGER.debug("CommonAction | ajaxLoadDesg | Start ");

		Map<String, Object>  map=null;
		if(getBillRegisterId()!=null)
		{
			Cbill cbill = (Cbill) persistenceService.find(" from EgBillregister where id=?", getBillRegisterId());
			map = voucherService.getDesgBYPassingWfItem("cbill.nextUser", cbill, departmentId);
		}
		else
			map = voucherService.getDesgBYPassingWfItem("cbill.nextUser", null, departmentId);

		designationList = (List<Map<String, Object>>)map.get("designationList");
		LOGGER.debug("CommonAction | ajaxLoadDesg | End ");
		return "desg";
	}
	
	public String ajaxLoadUser()throws Exception
	{
		userList = new ArrayList<UserImpl>();
		LOGGER.debug("CommonAction | ajaxLoadUserByDesg | Start");
		LOGGER.debug("Functionar received : = "+ functionaryName);
		String functionaryId = null;
		if(! "ANYFUNCTIONARY".equalsIgnoreCase(functionaryName)){
			Functionary functionary = (Functionary) persistenceService.find("from Functionary where name='"+functionaryName+"'");
			functionaryId =  functionary !=null?functionary.getId().toString():null;
		}
		if(departmentId!=-1 && designationId!=-1 && null !=functionaryName  && functionaryName.trim().length()!=0){
			List<EmployeeView>   empInfoList = voucherService.getUserByDeptAndDesgName(departmentId.toString(),
					designationId.toString(),functionaryId);
			 for (EmployeeView employeeView : empInfoList) {
					userList.add(employeeView.getUserMaster());
				}
		}
		return "users";
	}

	
	public Long getBillRegisterId() {
		return billRegisterId;
	}
	public void setBillRegisterId(Long billRegisterId) {
		this.billRegisterId = billRegisterId;
	}
	@Override
	public Object getModel() {
		return null;
	}
	public List<Map<String, Object>> getDesignationList() {
		return designationList;
	}
	public void setDesignationList(List<Map<String, Object>> designationList) {
		this.designationList = designationList;
	}

	public String getFunctionaryName() {
		return functionaryName;
	}

	public void setFunctionaryName(String functionaryName) {
		this.functionaryName = functionaryName;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public List<UserImpl> getUserList() {
		return userList;
	}

	public Integer getDesignationId() {
		return designationId;
	}

	public void setVoucherService(VoucherService voucherService) {
		this.voucherService = voucherService;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public void setUserList(List<UserImpl> userList) {
		this.userList = userList;
	}

	public void setDesignationId(Integer designationId) {
		this.designationId = designationId;
	}


}

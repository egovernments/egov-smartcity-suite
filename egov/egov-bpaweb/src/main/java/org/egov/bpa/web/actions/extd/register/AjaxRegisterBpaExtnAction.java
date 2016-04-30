/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.bpa.web.actions.extd.register;

import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infstr.services.EISServeable;
import org.egov.infstr.services.PersistenceService;

import java.util.List;

public class AjaxRegisterBpaExtnAction extends BaseFormAction {
	
	private static final String WF_APPROVERS = "approvers"; 
	private List<Object> approverList;
	private Integer designationId;
	private Integer approverDepartmentId;
	private EISServeable eisService;
	private PersistenceService persistenceService; 
	private List<String> roleList;

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/*
	 * To get Position By Passing DepartmentId and DesignationId and Roles...
	 * (Commented as part of BUG 24191 - To Forward the Application Directly to AE/AEE changes)
	 */
	/* 
	public String getUsersByDesgAndDept() {
		if (this.designationId != null && this.designationId != -1) {
			UtilsExtnService utilsExtnService = new UtilsExtnService(); 
			final HashMap<String, Object> paramMap = new HashMap<String, Object>(); 
			if (this.approverDepartmentId != null && this.approverDepartmentId != -1) { 
				paramMap.put("departmentId", this.approverDepartmentId.toString());
			}
			/*
			 * roleList: passing parameter roleList As String From Jsp and spliting and again Put into as ArrayList....
			 */
	/*
			if(!roleList.isEmpty() && roleList.size()>0)
			{
				if(this.roleList.get(0)!=null && this.roleList.get(0)!="")
					paramMap.put("roleList",Arrays.asList(this.roleList.get(0).split(",")));
			}
			paramMap.put("designationId", this.designationId.toString());
			this.approverList = new ArrayList<Object>(); 
			List<Integer> posId=new ArrayList<Integer>();
			final List<? extends EmployeeView> empList = (List<? extends EmployeeView>) this.eisService.getEmployeeInfoListByParameterAsObject(paramMap);
			if(empList!=null && !empList.isEmpty())
			for (final EmployeeView emp : empList) {
				posId.add(emp.getPosition().getId());
			}
			if(!posId.isEmpty()){
				List<Object[]> result=utilsExtnService.getLowLoadedAeorAeeForBpa(this.designationId,posId); 
				if(result!=null && !result.isEmpty()){
					Object[] obj=result.get(0);
						if(obj.length!=0){
							this.approverList.add((EmployeeView) persistenceService.find("from EmployeeView where position.id=?",Integer.valueOf(obj[0].toString())));
						}
				}
			}
		}
		return WF_APPROVERS;
	}
	*/

	public List<Object> getApproverList() {
		return approverList;
	}

	public void setApproverList(List<Object> approverList) {
		this.approverList = approverList;
	}

	public Integer getDesignationId() {
		return designationId;
	}

	public void setDesignationId(Integer designationId) {
		this.designationId = designationId;
	}

	public Integer getApproverDepartmentId() {
		return approverDepartmentId;
	}

	public void setApproverDepartmentId(Integer approverDepartmentId) {
		this.approverDepartmentId = approverDepartmentId;
	}

	public EISServeable getEisService() {
		return eisService;
	}

	public void setEisService(EISServeable eisService) {
		this.eisService = eisService;
	}

	public List<String> getRoleList() {
		return roleList;
	} 

	public void setRoleList(List<String> roleList) {
		this.roleList = roleList;
	}

	public PersistenceService getPersistenceService() {
		return persistenceService;
	}

	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}
	
}

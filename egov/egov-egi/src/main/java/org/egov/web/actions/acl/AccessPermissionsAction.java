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
package org.egov.web.actions.acl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.infstr.security.spring.acl.AclConstants;
import org.egov.infstr.security.spring.acl.models.AclObjClass;
import org.egov.infstr.security.spring.acl.models.AclObjectIdentity;
import org.egov.infstr.security.spring.acl.models.AclSid;
import org.egov.infstr.security.spring.acl.models.AclSidType;
import org.egov.infstr.services.EISServeable;
import org.egov.infstr.workflow.NotificationGroup;
import org.egov.web.actions.BaseFormAction;
import org.springframework.security.acls.domain.BasePermission;

@ParentPackage("egov")
public class AccessPermissionsAction extends BaseFormAction {

	private static final long serialVersionUID = 1L;
	private String query;
	private static final String USER_SEARCH_RESULTS = "user";
	private static final String ROLE_SEARCH_RESULTS = "role";
	private static final String EMP_SEARCH_RESULTS = "employee";
	private List<User> userList = new ArrayList<User>();
	private List<Integer> userIdList = new ArrayList<Integer>();

	private List<Role> roleList = new ArrayList<Role>();
	private List<Integer> roleIdList = new ArrayList<Integer>();

	private List<Object> empList = new ArrayList<Object>();
	private List<Integer> empIdList = new ArrayList<Integer>();

	private List<Long> groupIdList = new ArrayList<Long>();
	private List<NotificationGroup> groupList = new ArrayList<NotificationGroup>();

	private boolean userChk;
	private boolean roleChk;
	private boolean empChk;
	private boolean groupChk;
	private boolean workflowChk;

	private String mode;

	private Long objectId;
	private String objectClass;

	private EISServeable eisService;
	private AclObjectIdentity aclObjIdentity;

	@Override
	public Object getModel() {
		return null;
	}

	@Override
	public void prepare() {
		addDropdownData("groupList", getActiveGroupList());
	}

	@Override
	@SkipValidation
	public String execute() {
		this.aclObjIdentity = getAclObjectIdentity();
		if (this.aclObjIdentity != null) {
			getAcl();
		}
		return SUCCESS;
	}

	@SkipValidation
	public String ajaxUserNames() {
		return USER_SEARCH_RESULTS;
	}

	public List<User> getAllUsers() {

		if (StringUtils.isNotBlank(this.query)) {
			this.userList.addAll(this.persistenceService.findAllBy("from User where upper(userName) like '%' || ? || '%' and active=1 ", this.query.toUpperCase()));

		}
		return this.userList;
	}

	@SkipValidation
	public String ajaxRoleNames() {
		return ROLE_SEARCH_RESULTS;
	}

	@SkipValidation
	public List<Role> getAllRoles() {

		if (StringUtils.isNotBlank(this.query)) {
			this.roleList.addAll(this.persistenceService.findAllBy(" from Role where upper(name) like '%' || ? || '%' ", this.query.toUpperCase()));

		}
		return this.roleList;
	}

	@SkipValidation
	public String ajaxEmpNames() {
		return EMP_SEARCH_RESULTS;
	}

	@SkipValidation
	public List<Object> getAllEmps() {
		final List<Object> employeeList = this.persistenceService.findAllByNamedQuery(AclConstants.GETEMPLOYEES, this.query.toUpperCase());
		for (final Object emp : employeeList) {
			this.empList.add(emp);
		}
		return this.empList;
	}

	private List<NotificationGroup> getActiveGroupList() {
		return this.persistenceService.findAllBy("from NotificationGroup where active='Y' ");
	}

	public String saveOrUpdate() {
		this.aclObjIdentity = getAclObjectIdentity();
		if (this.aclObjIdentity == null) {
			this.aclObjIdentity = new AclObjectIdentity();
			AclObjClass aclclass = (AclObjClass) this.persistenceService.findByNamedQuery(AclConstants.GET_ACL_OBJCLASS, getAclObjectIdentity());
			if (aclclass == null) {
				aclclass = new AclObjClass();
				aclclass.setClassName(getObjectClass());
			}
			this.aclObjIdentity.setAclObjClass(aclclass);
			this.aclObjIdentity.setDomainObjectId(getObjectId());
			createSidList();
		} else {
			this.aclObjIdentity.getAclSidList().clear();
			createSidList();
		}
		getAcl();
		this.addActionMessage((this.aclObjIdentity == null ? "Successfully Created" : "Successfully Updated"));
		return SUCCESS;
	}

	private void getAcl() {
		final Set<AclSid> sidSet = this.aclObjIdentity.getAclSidList();
		for (final AclSid aclSid : sidSet) {
			if (aclSid.getSidType().getType().equalsIgnoreCase(AclConstants.SIDUSER)) {
				setUserChk(true);
				getUserIdList().add(Integer.valueOf(aclSid.getOwnerSid().toString()));
			} else if (aclSid.getSidType().getType().equalsIgnoreCase(AclConstants.SIDROLE)) {
				setRoleChk(true);
				getRoleIdList().add(Integer.valueOf(aclSid.getOwnerSid().toString()));
			} else if (aclSid.getSidType().getType().equalsIgnoreCase(AclConstants.SIDEMP)) {
				setEmpChk(true);
				getEmpIdList().add(Integer.valueOf(aclSid.getOwnerSid().toString()));
			} else if (aclSid.getSidType().getType().equalsIgnoreCase(AclConstants.SIDGROUP)) {
				setGroupChk(true);
				getGroupIdList().add(Long.valueOf(aclSid.getOwnerSid().toString()));
			} else if (aclSid.getSidType().getType().equalsIgnoreCase(AclConstants.SIDWORKFLOW)) {
				setWorkflowChk(true);
			}

		}
		if (isUserChk()) {
			getUserList().addAll(this.persistenceService.findAllByNamedQuery(AclConstants.GET_USERS_BYIDS, getUserIdList()));
		}
		if (isRoleChk()) {
			getRoleList().addAll(this.persistenceService.findAllByNamedQuery(AclConstants.GET_ROLES_BYIDS, getRoleIdList()));
		}
		if (isEmpChk()) {
			getEmpList().addAll(this.persistenceService.findAllByNamedQuery(AclConstants.GET_EMPS_BYIDS, getEmpIdList()));
		}
		if (isGroupChk()) {
			getGroupList().addAll(this.persistenceService.findAllByNamedQuery(AclConstants.GET_GROUPS_BYIDS, getGroupIdList()));
		}
	}

	private AclObjectIdentity getAclObjectIdentity() {
		final AclObjectIdentity aclObjIdentity = (AclObjectIdentity) this.persistenceService.findByNamedQuery(AclConstants.GET_ACL_OBJID, getObjectId(), getObjectClass());
		return aclObjIdentity;
	}

	private void createSidList() {
		if (isUserChk()) {
			createUserSidList();
		}
		if (isRoleChk()) {
			createRoleSidList();
		}
		if (isEmpChk()) {
			createEmpSidList();
		}
		if (isGroupChk()) {
			createGroupSidList();
		}
		if (this.workflowChk) {
			createWorkflowSid();
		}

		// aclObjIdentity.setAclSidList(this.sidList);

		this.persistenceService.setType(AclObjectIdentity.class);
		this.persistenceService.persist(this.aclObjIdentity);
	}

	private void createUserSidList() {

		for (final Integer userId : getUserIdList()) {
			final AclSid sid = new AclSid();
			sid.setOwnerSid(Long.valueOf(userId.toString()));
			sid.setPermission(BasePermission.READ.getMask());// read
			sid.setSidType((AclSidType) this.persistenceService.findByNamedQuery(AclConstants.GET_ACL_SIDTYPE, AclConstants.SIDUSER));
			this.aclObjIdentity.addAclSid(sid);
		}
	}

	private void createRoleSidList() {

		for (final Integer userId : getRoleIdList()) {
			final AclSid sid = new AclSid();
			sid.setOwnerSid(Long.valueOf(userId.toString()));
			sid.setPermission(BasePermission.READ.getMask());// read
			sid.setSidType((AclSidType) this.persistenceService.findByNamedQuery(AclConstants.GET_ACL_SIDTYPE, AclConstants.SIDROLE));
			this.aclObjIdentity.addAclSid(sid);
		}
	}

	private void createEmpSidList() {
		for (final Integer empId : getEmpIdList()) {
			final AclSid sid = new AclSid();
			sid.setOwnerSid(Long.valueOf(empId.toString()));
			sid.setPermission(BasePermission.READ.getMask());// read
			sid.setSidType((AclSidType) this.persistenceService.findByNamedQuery(AclConstants.GET_ACL_SIDTYPE, AclConstants.SIDEMP));
			this.aclObjIdentity.addAclSid(sid);
		}
	}

	private void createGroupSidList() {
		for (final Long groupId : getGroupIdList()) {
			if (groupId != null) {
				final AclSid sid = new AclSid();
				sid.setOwnerSid(Long.valueOf(groupId.toString()));
				sid.setPermission(BasePermission.READ.getMask());// read
				sid.setSidType((AclSidType) this.persistenceService.findByNamedQuery(AclConstants.GET_ACL_SIDTYPE, AclConstants.SIDGROUP));
				this.aclObjIdentity.addAclSid(sid);
			}
		}
	}

	private void createWorkflowSid() {
		final AclSid sid = new AclSid();
		sid.setPermission(BasePermission.READ.getMask());// read
		sid.setSidType((AclSidType) this.persistenceService.findByNamedQuery(AclConstants.GET_ACL_SIDTYPE, AclConstants.SIDWORKFLOW));
		sid.setAclObjectIdentity(this.aclObjIdentity);
		this.aclObjIdentity.addAclSid(sid);
	}

	public String getQuery() {
		return this.query;
	}

	public void setQuery(final String query) {
		this.query = query;
	}

	public void setUserList(final List<User> userList) {
		this.userList = userList;
	}

	public List<User> getUserList() {
		return this.userList;
	}

	public List<Integer> getUserIdList() {
		return this.userIdList;
	}

	public void setUserIdList(final List<Integer> userIdList) {
		this.userIdList = userIdList;
	}

	public void setRoleList(final List<Role> roleList) {
		this.roleList = roleList;
	}

	public List<Role> getRoleList() {
		return this.roleList;
	}

	public List<Integer> getRoleIdList() {
		return this.roleIdList;
	}

	public void setRoleIdList(final List<Integer> roleIdList) {
		this.roleIdList = roleIdList;
	}

	public void setEmpList(final List<Object> empList) {
		this.empList = empList;
	}

	public List<Object> getEmpList() {
		return this.empList;
	}

	public List<Integer> getEmpIdList() {
		return this.empIdList;
	}

	public void setEmpIdList(final List<Integer> empIdList) {
		this.empIdList = empIdList;
	}

	public List<Long> getGroupIdList() {
		return this.groupIdList;
	}

	public void setGroupIdList(final List<Long> groupIdList) {
		this.groupIdList = groupIdList;
	}

	public void setGroupList(final List<NotificationGroup> groupList) {
		this.groupList = groupList;
	}

	public List<NotificationGroup> getGroupList() {
		return this.groupList;
	}

	public boolean isUserChk() {
		return this.userChk;
	}

	public void setUserChk(final boolean userChk) {
		this.userChk = userChk;
	}

	public boolean isRoleChk() {
		return this.roleChk;
	}

	public void setRoleChk(final boolean roleChk) {
		this.roleChk = roleChk;
	}

	public boolean isEmpChk() {
		return this.empChk;
	}

	public void setEmpChk(final boolean empChk) {
		this.empChk = empChk;
	}

	public boolean isGroupChk() {
		return this.groupChk;
	}

	public void setGroupChk(final boolean groupChk) {
		this.groupChk = groupChk;
	}

	public boolean isWorkflowChk() {
		return this.workflowChk;
	}

	public void setWorkflowChk(final boolean workflowChk) {
		this.workflowChk = workflowChk;
	}

	public Long getObjectId() {
		return this.objectId;
	}

	public void setObjectId(final Long objectId) {
		this.objectId = objectId;
	}

	public String getObjectClass() {
		return this.objectClass;
	}

	public void setObjectClass(final String objectClass) {
		this.objectClass = objectClass;
	}

	public EISServeable getEisService() {
		return this.eisService;
	}

	public void setEisService(final EISServeable eisService) {
		this.eisService = eisService;
	}

	public String getMode() {
		return this.mode;
	}

	public void setMode(final String mode) {
		this.mode = mode;
	}

	@Override
	public void validate() {
		super.validate();
		/*
		 * if(!(isUserChk() || isRoleChk() || isEmpChk() || isGroupChk() || isWorkflowChk())) { this.addFieldError("userChk", this.getText("check.required")); } else
		 */if (isUserChk() && getUserIdList().isEmpty()) {
			this.addFieldError("userIdList", this.getText("userIdList.required"));
		} else if (isRoleChk() && getRoleIdList().isEmpty()) {
			this.addFieldError("roleIdList", this.getText("roleIdList.required"));
		} else if (isEmpChk() && getEmpIdList().isEmpty()) {
			this.addFieldError("empIdList", this.getText("empIdList.required"));
		} else if (isGroupChk() && getGroupIdList().isEmpty()) {
			this.addFieldError("groupIdList", this.getText("groupIdList.required"));
		}
	}

}

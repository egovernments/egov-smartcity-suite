package org.egov.lib.rjbac.role.ejb.server;

import java.util.List;

import org.egov.exceptions.DuplicateElementException;
import org.egov.lib.rjbac.role.Role;
import org.egov.lib.rjbac.role.dao.RoleDAO;
import org.egov.lib.rjbac.role.ejb.api.RoleService;

public class RoleServiceImpl implements RoleService {
	
	
	public void createRole(Role role) throws DuplicateElementException {
		new RoleDAO().createRole(role);
	}
	
	public void updateRole(Role role) {
		new RoleDAO().updateRole(role);
	}
	
	public Role getRoleByRoleName(String roleName) {
		return new RoleDAO().getRoleByRoleName(roleName);
	}
	
	public void removeRole(Role role) {
		new RoleDAO().removeRole(role);
	}
	
	public List<Role> getAllRoles() {
		return new RoleDAO().getAllRoles();
	}
	
	public List<Role> getAllTopLevelRoles() {
		return new RoleDAO().getAllTopLevelRoles();
	}
	
	public Role getRole(Integer roleID) {
		return new RoleDAO().getRoleByID(roleID);
	}
}

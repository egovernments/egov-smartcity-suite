package org.egov.api.model;

import java.util.List;
import java.util.Set;

import org.egov.infra.admin.master.entity.User;
import org.egov.pims.commons.Designation;

public class ForwardDetails {
	
	List<Designation> designations;
	Set<User> users;
	public List<Designation> getDesignations() {
		return designations;
	}
	public void setDesignations(List<Designation> designations) {
		this.designations = designations;
	}
	public Set<User> getUsers() {
		return users;
	}
	public void setUsers(Set<User> users) {
		this.users = users;
	}

}

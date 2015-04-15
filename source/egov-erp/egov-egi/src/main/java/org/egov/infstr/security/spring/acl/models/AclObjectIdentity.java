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
package org.egov.infstr.security.spring.acl.models;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.validation.Valid;

import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infstr.models.BaseModel;

public class AclObjectIdentity extends BaseModel {
	private static final long serialVersionUID = 5770066884148808731L;

	private AclObjClass aclObjClass;
	private Long domainObjectId;// reference to domain object
	private Set<AclSid> aclSidList = new LinkedHashSet<AclSid>();

	@Required(message = "aclObjClass is required")
	public AclObjClass getAclObjClass() {
		return aclObjClass;
	}

	public void setAclObjClass(AclObjClass aclObjClass) {
		this.aclObjClass = aclObjClass;
	}

	@Required(message = "domainObjectId is required")
	public Long getDomainObjectId() {
		return domainObjectId;
	}

	public void setDomainObjectId(Long domainObjectId) {
		this.domainObjectId = domainObjectId;
	}

	@Valid
	public Set<AclSid> getAclSidList() {
		return aclSidList;
	}

	public void setAclSidList(Set<AclSid> aclSidList) {
		this.aclSidList = aclSidList;
	}

	public void addAclSid(AclSid sid) {
		this.getAclSidList().add(sid);
		sid.setAclObjectIdentity(this);
	}

	public Set<AclSidType> getDistinctSidTypes() {
		Set<AclSidType> sidTypeSet = new LinkedHashSet<AclSidType>();
		for (AclSid sid : getAclSidList()) {
			sidTypeSet.add(sid.getSidType());
		}
		return sidTypeSet;
	}

}

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
package org.egov.infstr.security.spring.acl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.security.spring.acl.models.AclObjectIdentity;
import org.egov.infstr.security.spring.acl.models.AclSid;
import org.egov.infstr.security.spring.acl.models.AclSidType;
import org.egov.infstr.services.EISServeable;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.workflow.NotificationGroup;
import org.egov.lib.rrbac.model.Action;
import org.egov.lib.rrbac.services.RbacService;
import org.egov.pims.commons.Position;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

//FIXME This may not work
public abstract class GenericAclVoter implements AccessDecisionVoter {
	private static final Logger logger = LoggerFactory.getLogger(GenericAclVoter.class);
	protected PersistenceService persistenceService;
	protected String processConfigAttribute;
	protected Permission[] requirePermission;
	protected AbstractAuditable domainObject;
	protected AclObjectIdentity aclObjIdentity;
	protected boolean permissionGranted = false;
	private EISServeable eisService;
	private Collection<? extends GrantedAuthority> authorities;
	private RbacService rbacManager;

	abstract protected AbstractAuditable getDomainObjectInstance(Object object);

	protected Boolean isPermitted() {
		Boolean isGranted = false;
		// Obtain the permission list applicable to the domain object
		final List<AclSid> sidList = this.persistenceService.findAllByNamedQuery(AclConstants.GETMASKFORACLENTRY, this.domainObject.getId());

		// If principal has no permissions for domain object, deny
		if (sidList == null || sidList.isEmpty()) {
			if (logger.isDebugEnabled()) {
				logger.debug("Voting to deny access - no Masks returned for this principal");
			}

			isGranted = false;
		} else {
			final Iterator<AclSid> sidItr = sidList.iterator();
			// Principal has some permissions for domain object, check them

			while (sidItr.hasNext()) {
				final AclSid sid = sidItr.next();
				for (final Permission element : this.requirePermission) {

					if (sid.getPermission() == (element).getMask()) {
						if (logger.isDebugEnabled()) {
							logger.debug("Voting to grant access");
						}

						isGranted = true;
					} else {
						isGranted = false;
					}
				}
			}
		}
		return isGranted;
	}

	public PersistenceService getPersistenceService() {
		return this.persistenceService;
	}

	public void setPersistenceService(final PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

	public String getProcessConfigAttribute() {
		return this.processConfigAttribute;
	}

	@Override
	public boolean supports(final ConfigAttribute attribute) {
		if ((attribute.getAttribute() != null) && (getProcessConfigAttribute() == null || attribute.getAttribute().equals(getProcessConfigAttribute()))) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean supports(final Class clazz) {
		// TODO Auto-generated method stub
		return true;
	}

	protected void setAclIdentity() {
		// FIXME: should return distinct sidtypes associated with this object
		this.aclObjIdentity = (AclObjectIdentity) this.persistenceService.findByNamedQuery(AclConstants.GET_ACL_OBJID, this.domainObject.getId(), this.domainObject.getClass().getCanonicalName());//

	}

	private Boolean isUserHasAccess(final Authentication authentication) {

		// FIXME - query the ACLSid where type= userType and id = logged in user id
		// if list is null/empty then access is not granted
		final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		final AclSid aclSid = (AclSid) this.persistenceService.findByNamedQuery(AclConstants.GET_ACL_SID_FORUSER, AclConstants.SIDUSER, userDetails.getUsername(), this.aclObjIdentity.getDomainObjectId(), getPermissionList());
		if (aclSid != null) {
			this.permissionGranted = true;

		}
		return this.permissionGranted;

	}

	private boolean isRoleHasAcces(final Authentication authentication) {
		this.authorities = authentication.getAuthorities();
		final List<AclSid> roleSidList = this.persistenceService.findAllByNamedQuery(AclConstants.GET_ACL_SID_FORROLE, AclConstants.SIDROLE, this.aclObjIdentity.getDomainObjectId(), getAuthorities(), getPermissionList());
		// FIXME: check the ACL table if any of user's roles has permissions
		if (roleSidList != null && !roleSidList.isEmpty()) {
			this.permissionGranted = true;

		}
		return this.permissionGranted;
	}

	private Boolean isEmpHasAccess() {

		final List<AclSid> empSidList = this.persistenceService.findAllByNamedQuery(AclConstants.GET_ACL_SID_FOREMP, AclConstants.SIDEMP, Integer.valueOf(EGOVThreadLocals.getUserId()), this.aclObjIdentity.getDomainObjectId(), getPermissionList());

		if (empSidList != null && !empSidList.isEmpty()) {
			this.permissionGranted = true;

		}
		return this.permissionGranted;

	}

	public Boolean isGroupHasAccess() {

		// FIXME : join with the notification group table's position to check if any of user's positions belong in the list
		final List<AclSid> groupSidList = getGroupSidList();

		if (groupSidList != null && !groupSidList.isEmpty()) {
			this.permissionGranted = true;

		}
		return this.permissionGranted;
	}

	// getting the notification groupids for logged in user's position
	private List<AclSid> getGroupSidList() {
		List<AclSid> groupSidList = null;
		final List<Long> notificationIdList = this.persistenceService.getSession().createCriteria(NotificationGroup.class, "group").createAlias("group.members", "positions").add(Restrictions.in("positions.id", getPosIdList()))
				.setProjection(Projections.distinct(Projections.property("group.id"))).list();
		final Criteria sidCriteria = this.persistenceService.getSession().createCriteria(AclSid.class, "sid");
		if (notificationIdList != null && !notificationIdList.isEmpty()) {
			groupSidList = sidCriteria.add(Restrictions.in("sid.ownerSid", notificationIdList)).createAlias("sid.sidType", "sidType").add(Restrictions.ilike("sidType.type", AclConstants.SIDGROUP)).createAlias("sid.aclObjectIdentity", "aclObjectIdentity")
					.add(Restrictions.eq("aclObjectIdentity.domainObjectId", this.aclObjIdentity.getDomainObjectId())).add(Restrictions.in("sid.permission", getPermissionList())).list();
		}
		return groupSidList;
	}

	private List<Long> getPosIdList() {
		final List<Position> positions = getEisService().getPositionsForUser(Long.valueOf(EGOVThreadLocals.getUserId()), new Date());
		final List<Long> posIdList = new ArrayList<Long>();
		for (final Position position : positions) {
			posIdList.add(position.getId());
		}
		return posIdList;
	}

	public boolean isWFHasAccess() {
		if (this.domainObject instanceof StateAware) {
			final List<Position> posList = this.eisService.getPositionsForUser(Long.valueOf(EGOVThreadLocals.getUserId()), new Date());
			State thisState = ((StateAware) this.domainObject).getState();

			for (final Position currentUserPosition : posList) {
				if (thisState.getOwnerPosition().getId() == currentUserPosition.getId()) {
					this.permissionGranted = true;
					return this.permissionGranted;
				}

				for (StateHistory history : ((StateAware) this.domainObject).getStateHistory()) {
					if (history.getOwnerPosition().getId() == currentUserPosition.getId()) {
						this.permissionGranted = true;
						return this.permissionGranted;
					}
				}
			}

			return this.permissionGranted;
		}
		// This is not a workflow object
		return this.permissionGranted;
	}

	protected Boolean chkAccess(final String sidType, final Authentication authentication) {
		Boolean permGranted = false;
		if (sidType.equals(AclConstants.SIDUSER)) {
			permGranted = isUserHasAccess(authentication);
		} else if (sidType.equals(AclConstants.SIDROLE)) {
			permGranted = isRoleHasAcces(authentication);
		} else if (sidType.equals(AclConstants.SIDEMP)) {
			permGranted = isEmpHasAccess();
		} else if (sidType.equals(AclConstants.SIDGROUP)) {
			permGranted = isGroupHasAccess();
		} else if (sidType.equals(AclConstants.SIDWORKFLOW)) {
			permGranted = isWFHasAccess();
		}
		return permGranted;
	}

	public EISServeable getEisService() {
		return this.eisService;
	}

	public void setEisService(final EISServeable eisService) {
		this.eisService = eisService;
	}

	@Override
	public int vote(final Authentication authentication, final Object object, Collection arg) {

		// Need to make an access decision on this invocation
				// Attempt to locate the domain object instance to process
				final AbstractAuditable domainObject = getDomainObjectInstance(object);

				// set sidtype and sid to member variable
				setAclIdentity();

				if (this.aclObjIdentity == null) {
					return AccessDecisionVoter.ACCESS_GRANTED;
				} else {
					if (isPermissionGranted(authentication)) {
						return AccessDecisionVoter.ACCESS_GRANTED;
					} else {
						return AccessDecisionVoter.ACCESS_DENIED;
					}

		}

	}

	public boolean isPermissionGranted(final Authentication authentication) {

		for (final AclSidType sidType : this.aclObjIdentity.getDistinctSidTypes()) {
			if (!this.permissionGranted) {
				this.permissionGranted = chkAccess(sidType.getType(), authentication);
			} else {
				break;// any one sid type has access allow to access
			}

		}
		// if user has PERMISSIONACTION roles grant access even if check fails
		if (!this.permissionGranted) {
			this.authorities = authentication.getAuthorities();
			final Action action = this.rbacManager.getActionByName(AclConstants.PERMISSIONACTION);
			if (action == null) {
				throw new EGOVRuntimeException((AclConstants.PERMISSIONACTION + " is not in database"));
			} else {
				/*final List<Object> listUserroles = this.persistenceService.findAllByNamedQuery(AclConstants.GETUSERROLESBYUSERROLE, Integer.valueOf(EGOVThreadLocals.getUserId()), action.getRoles());

				if (listUserroles != null && !listUserroles.isEmpty()) {
					this.permissionGranted = true;
				}*/
			}

		}

		return this.permissionGranted;
	}

	private List<Permission> getBasePermissionList() {
		return (Arrays.asList(this.requirePermission));
	}

	private List<Integer> getPermissionList() {
		final List<Integer> permissionList = new ArrayList<Integer>();
		for (final Permission permission : getBasePermissionList()) {
			permissionList.add(permission.getMask());
		}

		return permissionList;
	}

	private List<String> getAuthorities() {
		final List<String> roleNameList = new ArrayList<String>();
		for (final GrantedAuthority authority : this.authorities) {
			roleNameList.add(authority.getAuthority());
		}
		return roleNameList;
	}

	public RbacService getRbacManager() {
		return this.rbacManager;
	}

	public void setRbacManager(final RbacService rbacManager) {
		this.rbacManager = rbacManager;
	}

}

/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 * 	1) All versions of this program, verbatim or modified must carry this
 * 	   Legal Notice.
 *
 * 	2) Any misrepresentation of the origin of the material is prohibited. It
 * 	   is required that all modified versions of this material be marked in
 * 	   reasonable ways as different from the original version.
 *
 * 	3) This license does not grant any rights to any user of the program
 * 	   with regards to rights under trademark law for use of the trade names
 * 	   or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.ptis.actions.view;

import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_ALTER_ASSESSENT;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_BIFURCATE_ASSESSENT;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_NEW_ASSESSENT;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_REVISION_PETITION;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_STATUS_MARK_DEACTIVE;
import static org.egov.ptis.constants.PropertyTaxConstants.SESSIONLOGINID;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.persistence.entity.Address;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infstr.services.PersistenceService;
import org.egov.ptis.actions.common.CommonServices;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.entity.objection.RevisionPetition;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyDocs;
import org.egov.ptis.domain.entity.property.PropertyOwnerInfo;
import org.egov.ptis.domain.entity.property.PropertyStatusValues;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.springframework.beans.factory.annotation.Autowired;

@ParentPackage("egov")
@Results({ @Result(name = "view", location = "viewProperty-view.jsp") })
public class ViewPropertyAction extends BaseFormAction {
    /**
     *
     */
    private static final long serialVersionUID = 4609817011534083012L;
    private final Logger LOGGER = Logger.getLogger(getClass());
    private String propertyId;
    private BasicProperty basicProperty;
    private Property property;
    private String ownerAddress;
    private String currTax;
    private String currTaxDue;
    private String totalArrDue;
    private Map<String, Object> viewMap;
    private PropertyTaxUtil propertyTaxUtil;
    private String markedForDeactive = "N";
    private String roleName;
    private Set<PropertyDocs> propDocsSet;
    private String docNumber;
    private boolean isDemandActive;
    private String demandEffectiveYear;
    private Integer noOfDaysForInactiveDemand;
    private String parentProps;
    private String applicationNo;
    private String applicationType;
    
    @Autowired
    private BasicPropertyDAO basicPropertyDAO;
    @Autowired
    private PtDemandDao ptDemandDAO;
    @Autowired
    private UserService UserService;
    @Autowired
    private PersistenceService<Property, Long> propertyImplService;
    @Autowired
    private PersistenceService<RevisionPetition, Long> revisionPetitionPersistenceService;

    @Override
    public Object getModel() {
        return property;
    }

    @Action(value = "/view/viewProperty-viewForm")
    public String viewForm() {
        LOGGER.debug("Entered into viewForm method");
        try {
            LOGGER.debug("viewForm : propertyId in View Property : " + propertyId);
            viewMap = new HashMap<String, Object>();
			if (propertyId != null && !propertyId.isEmpty()) {
				setBasicProperty(basicPropertyDAO.getBasicPropertyByPropertyID(propertyId));
			} else if (applicationNo != null && !applicationNo.isEmpty()) {
				getBasicPropForAppNo(applicationNo, applicationType);
			}
            LOGGER.debug("viewForm : BasicProperty : " + basicProperty);
            Set<PropertyStatusValues> propStatusValSet = new HashSet<PropertyStatusValues>();
            property = getBasicProperty().getProperty();
            LOGGER.debug("viewForm : Property : " + property);
            checkIsDemandActive(property);
            if (getBasicProperty().getPropertyOwnerInfo() != null
                    && !getBasicProperty().getPropertyOwnerInfo().isEmpty()) {
                for (final PropertyOwnerInfo propOwner : getBasicProperty().getPropertyOwnerInfo()) {
                    final List<Address> addrSet = propOwner.getOwner().getAddress();
                    for (final Address address : addrSet) {
                        ownerAddress = address.toString();
                        break;
                    }
                }
                viewMap.put("ownerAddress", ownerAddress == null ? PropertyTaxConstants.NOT_AVAILABLE : ownerAddress);
                final PropertyTypeMaster propertyTypeMaster = basicProperty.getProperty().getPropertyDetail()
                        .getPropertyTypeMaster();
                viewMap.put("ownershipType", propertyTypeMaster.getType());
            }
			if (!isDemandActive) {
				final Map<String, BigDecimal> demandCollMap = ptDemandDAO.getDemandCollMap(property);
				viewMap.put("currTax", demandCollMap.get(CURR_DMD_STR).toString());
				viewMap.put("currTaxDue", demandCollMap.get(CURR_DMD_STR).subtract(demandCollMap.get(CURR_COLL_STR))
						.toString());
				viewMap.put("totalArrDue", demandCollMap.get(ARR_DMD_STR).subtract(demandCollMap.get(ARR_COLL_STR))
						.toString());
			} else {
				viewMap.put("currTax", BigDecimal.ZERO);
				viewMap.put("currTaxDue", BigDecimal.ZERO);
				viewMap.put("totalArrDue", BigDecimal.ZERO);
			}
            getBasicProperty().getObjections();
            propStatusValSet = getBasicProperty().getPropertyStatusValuesSet();
            for (final PropertyStatusValues propStatusVal : propStatusValSet) {
                LOGGER.debug("viewForm : Property Status Values : " + propStatusVal);
                if (propStatusVal.getPropertyStatus().getStatusCode().equals(PROPERTY_STATUS_MARK_DEACTIVE))
                    markedForDeactive = "Y";
                LOGGER.debug("Marked for Deactivation ? : " + markedForDeactive);
            }
            final Long userId = (Long) session().get(SESSIONLOGINID);
            if (userId != null) {
                setRoleName(getRolesForUserId(userId));
            }
            if (!getBasicProperty().getPropertyDocsSet().isEmpty() && getBasicProperty().getPropertyDocsSet() != null)
                for (final PropertyDocs propDocs : getBasicProperty().getPropertyDocsSet())
                    setDocNumber(propDocs.getSupportDoc().getFileStoreId());

            LOGGER.debug("viewForm : Owner Address : " + viewMap.get(ownerAddress) + ", " + "Current Tax : "
                    + viewMap.get(currTax) + ", " + "Current Tax Due : " + viewMap.get(currTaxDue) + ", "
                    + "Total Arrears Tax Due : " + viewMap.get(totalArrDue));
            LOGGER.debug("Exit from method viewForm");
            return "view";
        } catch (final Exception e) {
            LOGGER.error("Exception in View Property: ", e);
            throw new EGOVRuntimeException("Exception : " + e);
        }
    }

    private void checkIsDemandActive(final Property property) {
        LOGGER.debug("Entered into checkIsDemandActive");
        if (property.getStatus().equals(PropertyTaxConstants.STATUS_DEMAND_INACTIVE))
            isDemandActive = false;
        else
            isDemandActive = true;
        LOGGER.debug("checkIsDemandActive - Is demand active? : " + isDemandActive);
        LOGGER.debug("Exiting from checkIsDemandActive");
    }

    private String getRolesForUserId(final Long userId) {
        LOGGER.debug("Entered into method getRolesForUserId");
        LOGGER.debug("User id : " + userId);
        String roleName;
        final List<String> roleNameList = new ArrayList<String>();
        final User user = UserService.getUserById(userId);
        for (final Role role : user.getRoles()) {
            roleName = role.getName() != null ? role.getName() : "";
            roleNameList.add(roleName);
        }
        LOGGER.debug("Exit from method getRolesForUserId with return value : " + roleNameList.toString().toUpperCase());
        return roleNameList.toString().toUpperCase();
    }

	private void getBasicPropForAppNo(String appNo, String appType) {
		if (appType != null && !appType.isEmpty()) {
			if ((appType.equalsIgnoreCase(APPLICATION_TYPE_NEW_ASSESSENT)
					|| appType.equalsIgnoreCase(APPLICATION_TYPE_ALTER_ASSESSENT) || appType
						.equalsIgnoreCase(APPLICATION_TYPE_BIFURCATE_ASSESSENT))) {
				Property property = propertyImplService.find("from PropertyImpl where applicationNo=?", appNo);
				setBasicProperty(property.getBasicProperty());
			} else if (appType.equalsIgnoreCase(APPLICATION_TYPE_REVISION_PETITION)) {
				RevisionPetition rp = revisionPetitionPersistenceService.find(
						"from RevisionPetition where objectionNumber=?", appNo);
				setBasicProperty(rp.getBasicProperty());
			}
		}
	}
    public String getFloorNoStr(final Integer floorNo) {
        return CommonServices.getFloorStr(floorNo);
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(final String propertyId) {
        this.propertyId = propertyId;
    }

    public BasicProperty getBasicProperty() {
        return basicProperty;
    }

    public void setBasicProperty(final BasicProperty basicProperty) {
        this.basicProperty = basicProperty;
    }

    public String getOwnerAddress() {
        return ownerAddress;
    }

    public void setOwnerAddress(final String ownerAddress) {
        this.ownerAddress = ownerAddress;
    }

    public String getCurrTax() {
        return currTax;
    }

    public void setCurrTax(final String currTax) {
        this.currTax = currTax;
    }

    public String getCurrTaxDue() {
        return currTaxDue;
    }

    public void setCurrTaxDue(final String currTaxDue) {
        this.currTaxDue = currTaxDue;
    }

    public String getTotalArrDue() {
        return totalArrDue;
    }

    public void setTotalArrDue(final String totalArrDue) {
        this.totalArrDue = totalArrDue;
    }

    public PropertyTaxUtil getPropertyTaxUtil() {
        return propertyTaxUtil;
    }

    public void setPropertyTaxUtil(final PropertyTaxUtil propertyTaxUtil) {
        this.propertyTaxUtil = propertyTaxUtil;
    }

    public Map<String, Object> getViewMap() {
        return viewMap;
    }

    public String getMarkedForDeactive() {
        return markedForDeactive;
    }

    public void setMarkedForDeactive(final String markedForDeactive) {
        this.markedForDeactive = markedForDeactive;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(final String roleName) {
        this.roleName = roleName;
    }

    public Set<PropertyDocs> getPropDocsSet() {
        return propDocsSet;
    }

    public void setPropDocsSet(final Set<PropertyDocs> propDocsSet) {
        this.propDocsSet = propDocsSet;
    }

    public String getDocNumber() {
        return docNumber;
    }

    public void setDocNumber(final String docNumber) {
        this.docNumber = docNumber;
    }

    public boolean getIsDemandActive() {
        return isDemandActive;
    }

    public void setIsDemandActive(final boolean isDemandActive) {
        this.isDemandActive = isDemandActive;
    }

    public String getDemandEffectiveYear() {
        return demandEffectiveYear;
    }

    public void setDemandEffectiveYear(final String demandEffectiveYear) {
        this.demandEffectiveYear = demandEffectiveYear;
    }

    public Integer getNoOfDaysForInactiveDemand() {
        return noOfDaysForInactiveDemand;
    }

    public void setNoOfDaysForInactiveDemand(final Integer noOfDaysForInactiveDemand) {
        this.noOfDaysForInactiveDemand = noOfDaysForInactiveDemand;
    }

    public String getParentProps() {
        return parentProps;
    }

    public void setParentProps(final String parentProps) {
        this.parentProps = parentProps;
    }

    public Property getProperty() {
        return property;
    }
    
    public void setBasicPropertyDAO(final BasicPropertyDAO basicPropertyDAO) {
        this.basicPropertyDAO = basicPropertyDAO;
    }

    public void setPtDemandDAO(final PtDemandDao ptDemandDAO) {
        this.ptDemandDAO = ptDemandDAO;
    }

    public void setUserService(final UserService userService) {
        UserService = userService;
    }

	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}

	public String getApplicationType() {
		return applicationType;
	}

	public void setApplicationType(String applicationType) {
		this.applicationType = applicationType;
	}
    
}

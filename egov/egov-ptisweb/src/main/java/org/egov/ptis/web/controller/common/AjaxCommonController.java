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
package org.egov.ptis.web.controller.common;

import org.egov.infra.admin.master.entity.User;
import org.egov.infra.persistence.entity.Address;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.entity.property.AmalgamatedPropInfo;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.PropertyOwnerInfo;
import org.egov.ptis.domain.entity.property.PropertyUsage;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.master.service.PropertyUsageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.egov.ptis.constants.PropertyTaxConstants.CATEGORY_MIXED;
import static org.egov.ptis.constants.PropertyTaxConstants.CATEGORY_NON_RESIDENTIAL;
import static org.egov.ptis.constants.PropertyTaxConstants.CATEGORY_RESIDENTIAL;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND_STR;

@Controller
@RequestMapping(value = "/common")
public class AjaxCommonController {

    @Autowired
    private BasicPropertyDAO basicPropertyDAO;

    @Autowired
    private PropertyService propService;

    @Autowired
    private PropertyUsageService propertyUsageService;

    /**
     * Provides the details of the property for amalgamation
     * @param assessmentNo
     * @return
     */
    @RequestMapping(value = "/amalgamation/getamalgamatedpropdetails", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public AmalgamatedPropInfo getAmalgamatedPropertyDetails(@RequestParam("assessmentNo") final String assessmentNo,@RequestParam("retainerAssessmentNo") final String retainerAssessmentNo) {
        final AmalgamatedPropInfo amalgamatedProp = new AmalgamatedPropInfo();
        amalgamatedProp.setValidationMsg("");
        final BasicProperty basicProp = basicPropertyDAO.getBasicPropertyByPropertyID(assessmentNo);
        String amalgPropType = basicProp.getProperty().getPropertyDetail().getPropertyTypeMaster().getType();
        amalgamatedProp.setAssessmentNo(assessmentNo);
        final BasicProperty retainerBasicProp = basicPropertyDAO.getBasicPropertyByPropertyID(retainerAssessmentNo);
        String retainerPropType = retainerBasicProp.getProperty().getPropertyDetail().getPropertyTypeMaster().getType();
        if (basicProp == null)
            amalgamatedProp.setValidationMsg("Assessment does not exist!");
        else {
            if (basicProp.isUnderWorkflow())
                amalgamatedProp.setValidationMsg("Assessment: " + basicProp.getUpicNo() + " is under workflow!");
            else if ((retainerPropType.equals(OWNERSHIP_TYPE_VAC_LAND_STR) && !amalgPropType.equals(OWNERSHIP_TYPE_VAC_LAND_STR)) || (!retainerPropType.equals(OWNERSHIP_TYPE_VAC_LAND_STR) && amalgPropType.equals(OWNERSHIP_TYPE_VAC_LAND_STR)))
                amalgamatedProp.setValidationMsg("Vacant Land cannot be amalgamated with Non Vacant Land");
            else {
                final BigDecimal totalDue = propService.getTotalPropertyTaxDue(basicProp);
                final boolean hasDues = totalDue.compareTo(BigDecimal.ZERO) > 0 ? true : false;
                if (hasDues)
                    amalgamatedProp.setValidationMsg("This property has dues!");
                else {
                    getChildPropertyDetails(assessmentNo, amalgamatedProp, basicProp, totalDue);
                }
            }
        }
        return amalgamatedProp;
    }

    private void getChildPropertyDetails(final String assessmentNo, final AmalgamatedPropInfo amalgamatedProp,
            final BasicProperty basicProp, final BigDecimal totalDue) {
        List<Map<String, Object>> childPropOwners = new ArrayList<>();
        for (final PropertyOwnerInfo propOwner : basicProp.getPropertyOwnerInfo()) {
            final List<Address> addrSet = propOwner.getOwner().getAddress().isEmpty()
                    ? Arrays.asList(basicProp.getAddress()) : propOwner.getOwner().getAddress();
            getChildPropertyOwners(assessmentNo, childPropOwners, propOwner);
            for (final Address address : addrSet) {
                amalgamatedProp.setOwnerName(propOwner.getOwner().getName());
                amalgamatedProp.setMobileNo(propOwner.getOwner().getMobileNumber());
                amalgamatedProp.setPropertyAddress(address.toString());
                amalgamatedProp.setPaymentDone(totalDue.compareTo(BigDecimal.ZERO) == 0 ? true : false);
                break;
            }
        }
        amalgamatedProp.setOwners(childPropOwners);
    }

    private void getChildPropertyOwners(final String assessmentNo, List<Map<String, Object>> childPropOwners,
            final PropertyOwnerInfo propOwner) {
        User user = propOwner.getOwner();
        Map<String, Object> owner = new HashMap<>();
        if (user != null) {
            owner.put("ownerUser", user.getUsername());
            owner.put("upicNo", assessmentNo);
            owner.put("aadhaarNumber", user.getAadhaarNumber());
            owner.put("mobileNumber", user.getMobileNumber());
            owner.put("ownerId", user.getId());
            owner.put("ownerName", user.getName());
            owner.put("gender", user.getGender().toString());
            owner.put("emailId", user.getEmailId());
            owner.put("guardian", user.getGuardian());
            owner.put("guardianRelation", user.getGuardianRelation());
        }
        childPropOwners.add(owner);
    }

    /**
     * Populates the usages based on the property type category
     * @param propTypeCategory
     * @return
     */
    @RequestMapping(value = "/getusagebypropertytype", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<PropertyUsage> getUsageByPropertyType(@RequestParam("propTypeCategory") final String propTypeCategory) {
        List<PropertyUsage> propUsageList = new ArrayList<>();
        if (propTypeCategory.equals(CATEGORY_MIXED))
            propUsageList = propertyUsageService.getAllActivePropertyUsages();
        else if (propTypeCategory.equals(CATEGORY_RESIDENTIAL))
            propUsageList = propertyUsageService.getResidentialPropertyUsages();
        else if (propTypeCategory.equals(CATEGORY_NON_RESIDENTIAL))
            propUsageList = propertyUsageService.getNonResidentialPropertyUsages();
        return propUsageList;
    }

}

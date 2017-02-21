/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.ptis.web.controller.common;

import static org.egov.ptis.constants.PropertyTaxConstants.CATEGORY_MIXED;
import static org.egov.ptis.constants.PropertyTaxConstants.CATEGORY_NON_RESIDENTIAL;
import static org.egov.ptis.constants.PropertyTaxConstants.CATEGORY_RESIDENTIAL;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
    public AmalgamatedPropInfo getAmalgamatedPropertyDetails(@RequestParam("assessmentNo") final String assessmentNo) {
        final AmalgamatedPropInfo amalgamatedProp = new AmalgamatedPropInfo();
        amalgamatedProp.setValidationMsg("");
        final BasicProperty basicProp = basicPropertyDAO.getBasicPropertyByPropertyID(assessmentNo);
        if (basicProp.isUnderWorkflow())
            amalgamatedProp.setValidationMsg("Assessment: " + basicProp.getUpicNo() + " is under workflow!");
        if (!basicProp.isActive())
            amalgamatedProp.setValidationMsg("Assessment: " + basicProp.getUpicNo() + " is deactivated!");
        final BigDecimal totalDue = propService.getTotalPropertyTaxDue(basicProp);
        final boolean hasDues = totalDue.compareTo(BigDecimal.ZERO) > 0 ? true : false;
        if (hasDues)
            amalgamatedProp.setValidationMsg("This property has dues!");
        else
            for (final PropertyOwnerInfo propOwner : basicProp.getPropertyOwnerInfo()) {
                final List<Address> addrSet = propOwner.getOwner().getAddress();
                for (final Address address : addrSet) {
                    amalgamatedProp.setAssessmentNo(assessmentNo);
                    amalgamatedProp.setOwnerName(propOwner.getOwner().getName());
                    amalgamatedProp.setMobileNo(propOwner.getOwner().getMobileNumber());
                    amalgamatedProp.setPropertyAddress(address.toString());
                    amalgamatedProp.setPaymentDone(totalDue.compareTo(BigDecimal.ZERO) == 0 ? true : false);
                    break;
                }
            }

        return amalgamatedProp;
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

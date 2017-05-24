/*
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
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
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
 */

package org.egov.ptis.web.controller.transactions.editowner;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.egov.infra.persistence.entity.Address;
import org.egov.infra.persistence.entity.enums.Gender;
import org.egov.ptis.bean.PropertyOwner;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.OwnerAudit;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyOwnerInfo;
import org.egov.ptis.domain.service.property.OwnerAuditService;
import org.egov.ptis.domain.service.property.PropertyPersistenceService;
import org.egov.ptis.domain.service.property.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/editowner/{assessmentNo}")
public class EditOwnerDetailsController {

    protected static final String OWNERDETAILS_FROM = "ownerdetails-form";
    protected static final String OWNERDETAILS_SUCCESS = "ownerdetails-success";
    private static final String ERROR_MSG = "errorMsg";

    @Autowired
    private BasicPropertyDAO basicPropertyDAO;

    @Autowired
    private PropertyPersistenceService basicPropertyService;
    
    @Autowired
    private OwnerAuditService ownerAuditService;

    @Autowired
    private PropertyService propertyService;

    @ModelAttribute
    public PropertyOwner getPropertyOwner(@PathVariable final String assessmentNo) {
        PropertyOwner propertyOwner = new PropertyOwner();
        BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(assessmentNo);
        PropertyImpl property;
        if (null != basicProperty) {
            property = (PropertyImpl) basicProperty.getProperty();
            propertyOwner.setProperty(property);
            propertyOwner.setPropertyOwnerInfo(basicProperty.getPropertyOwnerInfo());
        }
        return propertyOwner;
    }
    

    @RequestMapping(method = RequestMethod.GET)
    public String newForm(@ModelAttribute final PropertyOwner propertyOwner, final Model model,
            @PathVariable String assessmentNo) {
        BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(assessmentNo);
        List<OwnerAudit> ownerAuditList;
        model.addAttribute("guardianRelationMap", PropertyTaxConstants.GUARDIAN_RELATION);
        model.addAttribute("gender", Gender.values());
        for (PropertyOwnerInfo ownerInfo : basicProperty.getPropertyOwnerInfo()) {
            for (Address address : ownerInfo.getOwner().getAddress()) {
                model.addAttribute("doorNumber", address.getHouseNoBldgApt());
                model.addAttribute("existingDoorNumber", address.getHouseNoBldgApt());
                model.addAttribute("pinCode", address.getPinCode());
            }
        }
        ownerAuditList = ownerAuditService.setOwnerAuditDetails(basicProperty);
        propertyOwner.setOwnerAudit(ownerAuditList);
        model.addAttribute("propertyOwner", propertyOwner);
        model.addAttribute(ERROR_MSG, "");
        return OWNERDETAILS_FROM;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String updateOwnerDetails(@ModelAttribute final PropertyOwner propertyOwner,
            final RedirectAttributes redirectAttrs, final BindingResult errors, final Model model,
            final HttpServletRequest request, @RequestParam String doorNumber) {
        String errMsg ;
        model.addAttribute("doorNumber", doorNumber);
        model.addAttribute("guardianRelationMap", PropertyTaxConstants.GUARDIAN_RELATION);
        for (PropertyOwnerInfo ownerInfo : propertyOwner.getPropertyOwnerInfo())
            for (Address address : ownerInfo.getOwner().getAddress())
                model.addAttribute("existingDoorNumber", address.getHouseNoBldgApt());
        if ((!StringUtils.isBlank(doorNumber))
                && (propertyService.isDuplicateDoorNumber(doorNumber, propertyOwner.getProperty().getBasicProperty())))
            errMsg = "error.accept";
        else
            errMsg = basicPropertyService.updateOwners(propertyOwner.getProperty(),
                    propertyOwner.getProperty().getBasicProperty(), doorNumber, errors);
        if (!errMsg.isEmpty()) {
            model.addAttribute(ERROR_MSG, errMsg);
            return OWNERDETAILS_FROM;
        } else {
            ownerAuditService.saveOwnerDetails(propertyOwner.getOwnerAudit());
            return OWNERDETAILS_SUCCESS;
        }
    }

}

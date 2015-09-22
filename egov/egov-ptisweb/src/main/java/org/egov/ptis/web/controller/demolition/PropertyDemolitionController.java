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
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.ptis.web.controller.demolition;

import static org.egov.ptis.constants.PropertyTaxConstants.ARR_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_DMD_STR;

import java.math.BigDecimal;
import java.util.Map;

import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/property/demolition/{assessmentNo}")
public class PropertyDemolitionController {

    @Autowired
    private BasicPropertyDAO basicPropertyDAO;

    @Autowired
    private PtDemandDao ptDemandDAO;

    @ModelAttribute
    public Property complaintTypeModel(@PathVariable String assessmentNo) {
        BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(assessmentNo);
        return basicProperty.getActiveProperty();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String newForm(final Model model, @PathVariable String assessmentNo) {
        BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(assessmentNo);
        if (null != basicProperty) {
            Property property = basicProperty.getActiveProperty();
            model.addAttribute("property", basicProperty.getActiveProperty());
            if (!property.getIsExemptedFromTax()) {
                final Map<String, BigDecimal> demandCollMap = ptDemandDAO.getDemandCollMap(property);
                model.addAttribute("currTax", demandCollMap.get(CURR_DMD_STR));
                model.addAttribute("currTaxDue",
                        demandCollMap.get(CURR_DMD_STR).subtract(demandCollMap.get(CURR_COLL_STR)));
                model.addAttribute("totalArrDue",
                        demandCollMap.get(ARR_DMD_STR).subtract(demandCollMap.get(ARR_COLL_STR)));
            } else {
                model.addAttribute("currTax", BigDecimal.ZERO);
                model.addAttribute("currTaxDue", BigDecimal.ZERO);
                model.addAttribute("totalArrDue", BigDecimal.ZERO);
            }
        }
        return "demolition-form";
    }

}

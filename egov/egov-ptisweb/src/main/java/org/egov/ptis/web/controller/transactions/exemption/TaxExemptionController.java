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
package org.egov.ptis.web.controller.transactions.exemption;

import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_TAX_EXEMTION;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.TARGET_TAX_DUES;
import static org.egov.ptis.constants.PropertyTaxConstants.TARGET_WORKFLOW_ERROR;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.TaxExeptionReason;
import org.egov.ptis.domain.service.property.PropertyService;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author subhash
 *
 */
@Controller
@RequestMapping(value = "/exemption")
public class TaxExemptionController {

    private static final String TAX_EXEMPTION_FORM = "taxExemption-form";
    @Autowired
    private BasicPropertyDAO basicPropertyDAO;
    @Autowired
    private PtDemandDao ptDemandDAO;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private PropertyService propertyService;

    @ModelAttribute
    public Property propertyModel(@PathVariable final String assessmentNo) {
        final BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(assessmentNo);
        return null != basicProperty ? basicProperty.getActiveProperty() : null;
    }

    @SuppressWarnings("unchecked")
    @ModelAttribute("taxExemptionReasons")
    public List<TaxExeptionReason> getTaxExemptionReasons() {
        return getSession().createQuery("from TaxExeptionReason order by name").list();
    }

    @RequestMapping(value = "/form/{assessmentNo}", method = RequestMethod.GET)
    public String exemptionForm(final HttpServletRequest request, final Model model, @PathVariable("assessmentNo") final String assessmentNo) {
        final BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(assessmentNo);
        if (null != basicProperty && basicProperty.isUnderWorkflow()) {
            model.addAttribute("wfPendingMsg", "Could not do " + APPLICATION_TYPE_TAX_EXEMTION
                    + " now, property is undergoing some work flow.");
            return TARGET_WORKFLOW_ERROR;
        } else if (null != basicProperty) {
            final Map<String, BigDecimal> propertyTaxDetails = propertyService.getCurrentPropertyTaxDetails(basicProperty
                    .getActiveProperty());
            final BigDecimal currentPropertyTax = propertyTaxDetails.get(CURR_DMD_STR);
            final BigDecimal currentPropertyTaxDue = propertyTaxDetails.get(CURR_DMD_STR).subtract(
                    propertyTaxDetails.get(CURR_COLL_STR));
            final BigDecimal arrearPropertyTaxDue = propertyTaxDetails.get(ARR_DMD_STR).subtract(
                    propertyTaxDetails.get(ARR_COLL_STR));
            final BigDecimal currentWaterTaxDue = propertyService.getWaterTaxDues(basicProperty.getUpicNo(), request);
            model.addAttribute("currentPropertyTax", currentPropertyTax);
            model.addAttribute("currentPropertyTaxDue", currentPropertyTaxDue);
            model.addAttribute("arrearPropertyTaxDue", arrearPropertyTaxDue);
            model.addAttribute("currentWaterTaxDue", currentWaterTaxDue);
            if (currentWaterTaxDue.add(currentPropertyTaxDue).add(arrearPropertyTaxDue).longValue() > 0) {
                model.addAttribute("taxDuesErrorMsg", "Above tax dues must be payed before initiating " + APPLICATION_TYPE_TAX_EXEMTION);
                return TARGET_TAX_DUES;
            }
        }
        final BasicProperty parentBasicProperty = basicPropertyDAO.getParentBasicPropertyByBasicPropertyId(basicProperty.getId());
        if (null != parentBasicProperty)
            model.addAttribute("parentAssessment", parentBasicProperty.getUpicNo());
        final Ptdemand ptDemand = ptDemandDAO.getNonHistoryCurrDmdForProperty(basicProperty.getActiveProperty());
        if (null != ptDemand && null != ptDemand.getDmdCalculations() && null != ptDemand.getDmdCalculations().getAlv())
            model.addAttribute("arv", ptDemand.getDmdCalculations().getAlv());
        else
            model.addAttribute("arv", BigDecimal.ZERO);
        return TAX_EXEMPTION_FORM;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(final HttpServletRequest request, final HttpServletResponse response, final Model model) {
        return null;
    }

    public Session getSession() {
        return entityManager.unwrap(Session.class);
    }
    
}

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
package org.egov.ptis.web.controller.mobile;

import static org.egov.ptis.constants.PropertyTaxConstants.CATEGORY_TYPE_PROPERTY_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.CATEGORY_TYPE_VACANTLAND_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.CHECK_PROPERTY_CATEGORY;
import static org.egov.ptis.constants.PropertyTaxConstants.MOBILE_PAYMENT_INCORRECT_BILL_DATA;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_VALIDATION;
import static org.egov.ptis.constants.PropertyTaxConstants.THIRD_PARTY_DEMAND_AMOUNT_GREATER_MSG;
import static org.egov.ptis.constants.PropertyTaxConstants.THIRD_PARTY_ERR_MSG_ASSESSMENT_NO_NOT_FOUND;
import static org.egov.ptis.constants.PropertyTaxConstants.THIRD_PARTY_ERR_MSG_EXEMPTED_PROPERTY;
import static org.egov.ptis.constants.PropertyTaxConstants.THIRD_PARTY_ERR_MSG_PROPERTY_TAX_ASSESSMENT_NOT_FOUND;
import static org.egov.ptis.constants.PropertyTaxConstants.THIRD_PARTY_ERR_MSG_VACANTLAND_ASSESSMENT_NOT_FOUND;
import static org.egov.ptis.constants.PropertyTaxConstants.THIRD_PARTY_ERR_MSG_WRONG_CATEGORY;

import java.math.BigDecimal;
import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;

import org.egov.demand.model.EgDemandDetails;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.service.mobile.MobilePaymentService;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = { "/public/mobile", "/mobile" })
public class MobilePaymentController {

    private static final String PAYTAX_FORM = "mobilePayment-form";
    private static final String ERROR_MSG = "errorMsg";
    private static final String ERROR_MSG_LIST="errorMsgList";

    @Autowired
    private BasicPropertyDAO basicPropertyDAO;

    @Autowired
    private PtDemandDao ptDemandDAO;

    @Autowired
    private MobilePaymentService mobilePaymentService;
    
    @Autowired
    PropertyTaxCommonUtils propertyTaxCommonUtils;

    /**
     * API to process payments from Mobile App
     *
     * @param model
     * @param assessmentNo
     * @param ulbCode
     * @param amountToBePaid
     * @param mobileNumber
     * @param emailId
     * @param request
     * @return
     * @throws ParseException
     */
    @PostMapping(value = "/paytax/{assessmentNo},{ulbCode},{amountToBePaid},{mobileNumber},{emailId},{category}")
    public String newform(final Model model, @PathVariable final String assessmentNo,
            @PathVariable final String ulbCode, @PathVariable final BigDecimal amountToBePaid,
            @PathVariable final String mobileNumber, @PathVariable final String emailId,
            @PathVariable final String category, final HttpServletRequest request) {
        String redirectUrl = "";
        if (!basicPropertyDAO.isAssessmentNoExist(assessmentNo) || !ApplicationThreadLocals.getCityCode().equals(ulbCode)) {
            model.addAttribute(ERROR_MSG, THIRD_PARTY_ERR_MSG_ASSESSMENT_NO_NOT_FOUND);
            return PROPERTY_VALIDATION;
        }
        final BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(assessmentNo);
        if (basicProperty != null) {
            final Property property = basicProperty.getProperty();
            if (property != null) {
                if (property.getIsExemptedFromTax()) {
                    model.addAttribute(ERROR_MSG, THIRD_PARTY_ERR_MSG_EXEMPTED_PROPERTY);
                    return PROPERTY_VALIDATION;
                }
                final Ptdemand currentPtdemand = ptDemandDAO.getNonHistoryCurrDmdForProperty(property);
                BigDecimal totalTaxDue = BigDecimal.ZERO;
                final String propType = property.getPropertyDetail().getPropertyTypeMaster().getCode();
                String propCategory = checkPropertyCategory(propType);
                if (!category.equalsIgnoreCase(propCategory)) {
                    model.addAttribute(ERROR_MSG, CHECK_PROPERTY_CATEGORY);
                    return PROPERTY_VALIDATION;
                }
                if (currentPtdemand != null)
                    for (final EgDemandDetails demandDetails : currentPtdemand.getEgDemandDetails())
                        if (propertyTaxCommonUtils.getTotalDemandVariationAmount(demandDetails).subtract(demandDetails.getAmtCollected())
                                .compareTo(BigDecimal.ZERO) > 0)
                            totalTaxDue = totalTaxDue
                                    .add(propertyTaxCommonUtils.getTotalDemandVariationAmount(demandDetails).subtract(demandDetails.getAmtCollected()));
                if (amountToBePaid.compareTo(totalTaxDue) > 0) {
                    model.addAttribute(ERROR_MSG, THIRD_PARTY_DEMAND_AMOUNT_GREATER_MSG);
                    return PROPERTY_VALIDATION;
                }
                if (!propType.equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND)
                        && category.equalsIgnoreCase(CATEGORY_TYPE_VACANTLAND_TAX)) {
                    model.addAttribute(ERROR_MSG, THIRD_PARTY_ERR_MSG_VACANTLAND_ASSESSMENT_NOT_FOUND);
                    return PROPERTY_VALIDATION;
                } else if (propType.equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND)
                        && category.equalsIgnoreCase(CATEGORY_TYPE_PROPERTY_TAX)) {
                    model.addAttribute(ERROR_MSG, THIRD_PARTY_ERR_MSG_PROPERTY_TAX_ASSESSMENT_NOT_FOUND);
                    return PROPERTY_VALIDATION;
                }
                if (!category.equalsIgnoreCase(CATEGORY_TYPE_PROPERTY_TAX)
                        && !category.equalsIgnoreCase(CATEGORY_TYPE_VACANTLAND_TAX)) {
                    model.addAttribute(ERROR_MSG, THIRD_PARTY_ERR_MSG_WRONG_CATEGORY);
                    return PROPERTY_VALIDATION;
                }
            }
        }
        try {
            redirectUrl = mobilePaymentService.mobileBillPayment(assessmentNo, amountToBePaid, category);
            if (!redirectUrl.isEmpty())
                model.addAttribute("redirectUrl", redirectUrl);
            else {
                model.addAttribute(ERROR_MSG, MOBILE_PAYMENT_INCORRECT_BILL_DATA);
                return PROPERTY_VALIDATION;
            }
        } catch (final ValidationException e) {
            model.addAttribute(ERROR_MSG_LIST, e.getErrors());
            return PROPERTY_VALIDATION;
        } catch (final Exception e) {

            return PROPERTY_VALIDATION;
        }
        return PAYTAX_FORM;
    }

    private String checkPropertyCategory(String propType) {
        // TODO - fix me
        if (propType == OWNERSHIP_TYPE_VAC_LAND)
            return CATEGORY_TYPE_VACANTLAND_TAX;
        else
            return CATEGORY_TYPE_PROPERTY_TAX;
    }
}
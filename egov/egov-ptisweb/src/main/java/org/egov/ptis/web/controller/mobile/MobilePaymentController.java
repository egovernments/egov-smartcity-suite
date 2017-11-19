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

import org.apache.commons.lang3.StringUtils;
import org.egov.collection.integration.models.BillInfo.COLLECTIONTYPE;
import org.egov.collection.integration.models.BillInfoImpl;
import org.egov.collection.integration.pgi.PaymentRequest;
import org.egov.demand.dao.EgBillDao;
import org.egov.demand.model.EgBill;
import org.egov.demand.model.EgDemandDetails;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.ptis.client.bill.PTBillServiceImpl;
import org.egov.ptis.client.integration.utils.CollectionHelper;
import org.egov.ptis.client.integration.utils.SpringBeanUtil;
import org.egov.ptis.client.util.PropertyTaxNumberGenerator;
import org.egov.ptis.domain.bill.PropertyTaxBillable;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;

import static org.egov.ptis.constants.PropertyTaxConstants.BILLTYPE_AUTO;
import static org.egov.ptis.constants.PropertyTaxConstants.CATEGORY_TYPE_PROPERTY_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.CATEGORY_TYPE_VACANTLAND_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.MOBILE_PAYMENT_INCORRECT_BILL_DATA;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_VALIDATION;
import static org.egov.ptis.constants.PropertyTaxConstants.THIRD_PARTY_DEMAND_AMOUNT_GREATER_MSG;
import static org.egov.ptis.constants.PropertyTaxConstants.THIRD_PARTY_ERR_MSG_ASSESSMENT_NO_NOT_FOUND;
import static org.egov.ptis.constants.PropertyTaxConstants.THIRD_PARTY_ERR_MSG_EXEMPTED_PROPERTY;
import static org.egov.ptis.constants.PropertyTaxConstants.THIRD_PARTY_ERR_MSG_PROPERTY_TAX_ASSESSMENT_NOT_FOUND;
import static org.egov.ptis.constants.PropertyTaxConstants.THIRD_PARTY_ERR_MSG_VACANTLAND_ASSESSMENT_NOT_FOUND;
import static org.egov.ptis.constants.PropertyTaxConstants.THIRD_PARTY_ERR_MSG_WRONG_CATEGORY;

@Controller
@RequestMapping(value = {"/public/mobile", "/mobile"})
public class MobilePaymentController {

    private static final String PAYTAX_FORM = "mobilePayment-form";

    @Autowired
    private BasicPropertyDAO basicPropertyDAO;

    @Autowired
    @Qualifier("propertyTaxBillable")
    private PropertyTaxBillable propertyTaxBillable;

    @Autowired
    private PropertyTaxNumberGenerator propertyTaxNumberGenerator;

    @Autowired
    private EgBillDao egBillDAO;

    @Autowired
    private PTBillServiceImpl ptBillServiceImpl;

    @Autowired
    private PtDemandDao ptDemandDAO;

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
    @RequestMapping(value = "/paytax/{assessmentNo},{ulbCode},{amountToBePaid},{mobileNumber},{emailId},{category}", method = RequestMethod.GET)
    public String newform(final Model model, @PathVariable final String assessmentNo,
                          @PathVariable final String ulbCode, @PathVariable final BigDecimal amountToBePaid,
                          @PathVariable final String mobileNumber, @PathVariable final String emailId,
                          @PathVariable final String category, final HttpServletRequest request) throws ParseException {
        String redirectUrl = "";
        if (!basicPropertyDAO.isAssessmentNoExist(assessmentNo)) {
            model.addAttribute("errorMsg", THIRD_PARTY_ERR_MSG_ASSESSMENT_NO_NOT_FOUND);
            return PROPERTY_VALIDATION;
        }
        final BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(assessmentNo);
        if (basicProperty != null) {
            final Property property = basicProperty.getProperty();
            if (property != null) {
                if (property.getIsExemptedFromTax()) {
                    model.addAttribute("errorMsg", THIRD_PARTY_ERR_MSG_EXEMPTED_PROPERTY);
                    return PROPERTY_VALIDATION;
                }
                final Ptdemand currentPtdemand = ptDemandDAO.getNonHistoryCurrDmdForProperty(property);
                BigDecimal totalTaxDue = BigDecimal.ZERO;
                final String propType = property.getPropertyDetail().getPropertyTypeMaster().getCode();
                if (currentPtdemand != null)
                    for (final EgDemandDetails demandDetails : currentPtdemand.getEgDemandDetails())
                        if (demandDetails.getAmount().subtract(demandDetails.getAmtCollected())
                                .compareTo(BigDecimal.ZERO) > 0)
                            totalTaxDue = totalTaxDue
                                    .add(demandDetails.getAmount().subtract(demandDetails.getAmtCollected()));
                if (amountToBePaid.compareTo(totalTaxDue) > 0) {
                    model.addAttribute("errorMsg", THIRD_PARTY_DEMAND_AMOUNT_GREATER_MSG);
                    return PROPERTY_VALIDATION;
                }
                if (!propType.equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND)
                        && category.equalsIgnoreCase(CATEGORY_TYPE_VACANTLAND_TAX)) {
                    model.addAttribute("errorMsg", THIRD_PARTY_ERR_MSG_VACANTLAND_ASSESSMENT_NOT_FOUND);
                    return PROPERTY_VALIDATION;
                } else if (propType.equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND)
                        && category.equalsIgnoreCase(CATEGORY_TYPE_PROPERTY_TAX)) {
                    model.addAttribute("errorMsg", THIRD_PARTY_ERR_MSG_PROPERTY_TAX_ASSESSMENT_NOT_FOUND);
                    return PROPERTY_VALIDATION;
                }
                if (!category.equalsIgnoreCase(CATEGORY_TYPE_PROPERTY_TAX)
                        && !category.equalsIgnoreCase(CATEGORY_TYPE_VACANTLAND_TAX)) {
                    model.addAttribute("errorMsg", THIRD_PARTY_ERR_MSG_WRONG_CATEGORY);
                    return PROPERTY_VALIDATION;
                }
            }
        }
        try {
            final BillInfoImpl billInfo = getBillInfo(assessmentNo, amountToBePaid, category);
            if (billInfo != null) {
                final PaymentRequest paymentRequest = SpringBeanUtil.getCollectionIntegrationService()
                        .processMobilePayments(billInfo);
                if (paymentRequest != null) {
                    for (final Object obj : paymentRequest.getRequestParameters().values())
                        redirectUrl = obj.toString();
                    model.addAttribute("redirectUrl", redirectUrl);
                }
            } else {
                model.addAttribute("errorMsg", MOBILE_PAYMENT_INCORRECT_BILL_DATA);
                return PROPERTY_VALIDATION;
            }
        } catch (final ValidationException e) {

            return PROPERTY_VALIDATION;
        } catch (final Exception e) {

            return PROPERTY_VALIDATION;
        }

        return PAYTAX_FORM;
    }

    /**
     * API to return BillInfoImpl, used in tax payment through Mobile App
     *
     * @param mobilePropertyTaxDetails
     * @return
     */
    public BillInfoImpl getBillInfo(final String assessmentNo, final BigDecimal amountToBePaid, final String category) {
        BillInfoImpl billInfoImpl = null;
        final BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(assessmentNo);
        propertyTaxBillable.setBasicProperty(basicProperty);
        propertyTaxBillable.setReferenceNumber(propertyTaxNumberGenerator
                .generateBillNumber(basicProperty.getPropertyID().getWard().getBoundaryNum().toString()));
        propertyTaxBillable.setBillType(egBillDAO.getBillTypeByCode(BILLTYPE_AUTO));
        propertyTaxBillable.setLevyPenalty(Boolean.TRUE);
        if (StringUtils.isNotBlank(category) && category.equalsIgnoreCase(CATEGORY_TYPE_VACANTLAND_TAX))
            propertyTaxBillable.setVacantLandTaxPayment(true);

        final EgBill egBill = ptBillServiceImpl.generateBill(propertyTaxBillable);
        final CollectionHelper collectionHelper = new CollectionHelper(egBill);

        billInfoImpl = collectionHelper.prepareBillInfo(amountToBePaid, COLLECTIONTYPE.O, null);
        return billInfoImpl;
    }

}

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
package org.egov.wtms.web.controller.mobile;

import static org.egov.ptis.constants.PropertyTaxConstants.BILLTYPE_MANUAL;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_VALIDATION;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.egov.collection.integration.models.BillInfo.COLLECTIONTYPE;
import org.egov.collection.integration.models.BillInfoImpl;
import org.egov.collection.integration.pgi.PaymentRequest;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.demand.dao.EgBillDao;
import org.egov.demand.interfaces.Billable;
import org.egov.demand.model.EgBill;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.utils.autonumber.AutonumberServiceBeanResolver;
import org.egov.ptis.client.integration.utils.SpringBeanUtil;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.enums.BasicPropertyStatus;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.service.ConnectionDemandService;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.application.service.collection.WaterConnectionBillable;
import org.egov.wtms.application.service.collection.WaterTaxExternalService;
import org.egov.wtms.autonumber.BillReferenceNumberGenerator;
import org.egov.wtms.masters.entity.enums.ConnectionStatus;
import org.egov.wtms.utils.PropertyExtnUtils;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = { "/public/mobile", "/mobile" })
public class WaterTaxMobilePaymentController {

    private static final String PAYTAX_FORM = "mobilePayment-form";

    @Autowired
    @Qualifier("waterConnectionBillable")
    private WaterConnectionBillable waterConnectionBillable;

    @Autowired
    private PropertyExtnUtils propertyExtnUtils;

    @Autowired
    private WaterTaxExternalService waterTaxExternalService;

    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;

    @Autowired
    private ConnectionDemandService connectionDemandService;

    @Autowired
    private EgBillDao egBillDAO;

    @Autowired
    private AutonumberServiceBeanResolver beanResolver;

    @Autowired
    private FinancialYearDAO financialYearDAO;

    /**
     * API to process payments from Mobile App
     *
     * @param model
     * @param consumerNo
     * @param ulbCode
     * @param amountToBePaid
     * @param request
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "/payWatertax/{consumerNo},{ulbCode},{amountToBePaid},{mobileNumber},{emailId}", method = RequestMethod.GET)
    public String collectTax(final Model model, @PathVariable final String consumerNo,
            @PathVariable final String ulbCode, @PathVariable final BigDecimal amountToBePaid,
            @PathVariable final String mobileNumber, @PathVariable final String emailId,
            final HttpServletRequest request) throws ParseException {
        String redirectUrl = "";
        final BillInfoImpl billInfo = getBillInfo(consumerNo, amountToBePaid);
        if (billInfo != null) {
            final PaymentRequest paymentRequest = SpringBeanUtil.getCollectionIntegrationService()
                    .processMobilePayments(billInfo);
            if (paymentRequest != null) {
                for (final Object obj : paymentRequest.getRequestParameters().values())
                    redirectUrl = obj.toString();
                model.addAttribute("redirectUrl", redirectUrl);
            }
        } else {
            model.addAttribute("errorMsg", "Bill data is incorrect");
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
    public BillInfoImpl getBillInfo(final String consumerNo, final BigDecimal amountToBePaid) {
        BillInfoImpl billInfoImpl = null;
        WaterConnectionDetails waterConnectionDetails = null;
        String currentInstallmentYear = null;
        final SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
        final BillReferenceNumberGenerator billRefeNumber = beanResolver
                .getAutoNumberServiceFor(BillReferenceNumberGenerator.class);
        if (consumerNo != null)
            waterConnectionDetails = waterConnectionDetailsService
                    .findByApplicationNumberOrConsumerCodeAndStatus(consumerNo, ConnectionStatus.ACTIVE);
        final AssessmentDetails assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(
                waterConnectionDetails.getConnection().getPropertyIdentifier(),
                PropertyExternalService.FLAG_FULL_DETAILS, BasicPropertyStatus.ALL);
        waterConnectionBillable.setWaterConnectionDetails(waterConnectionDetails);
        waterConnectionBillable.setAssessmentDetails(assessmentDetails);
        waterConnectionBillable.setUserId(2L);
        ApplicationThreadLocals.setUserId(2L);
        currentInstallmentYear = formatYear.format(connectionDemandService
                .getCurrentInstallment(WaterTaxConstants.WATER_RATES_NONMETERED_PTMODULE, null, new Date())
                .getInstallmentYear());
        waterConnectionBillable.setReferenceNumber(billRefeNumber.generateBillNumber(currentInstallmentYear));
        waterConnectionBillable.setBillType(connectionDemandService.getBillTypeByCode(BILLTYPE_MANUAL));
        final EgBill egBill = generateBill(waterConnectionBillable, financialYearDAO);
        billInfoImpl = prepareBillForCollection(amountToBePaid, egBill, null);
        return billInfoImpl;
    }

    public final EgBill generateBill(final Billable billObj, final FinancialYearDAO financialYearDAO) {
        final EgBill bill = waterTaxExternalService.generateBillForConnection(billObj, financialYearDAO);
        egBillDAO.create(bill);
        return bill;
    };

    @Transactional
    public BillInfoImpl prepareBillForCollection(final BigDecimal amountToBePaid, final EgBill bill,
            final String source) {

        if (!waterTaxExternalService.isCollectionPermitted(bill))
            throw new ApplicationRuntimeException(
                    "Collection is not allowed - current balance is zero and advance coll exists.");

        final BillInfoImpl billInfo = waterTaxExternalService.prepareBillInfo(amountToBePaid, COLLECTIONTYPE.O, bill,
                source);
        return billInfo;
    }
}

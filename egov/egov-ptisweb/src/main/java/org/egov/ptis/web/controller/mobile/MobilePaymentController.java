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
package org.egov.ptis.web.controller.mobile;

import static org.egov.ptis.constants.PropertyTaxConstants.BILLTYPE_AUTO;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_VALIDATION;

import java.math.BigDecimal;
import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;

import org.egov.collection.integration.models.BillInfo.COLLECTIONTYPE;
import org.egov.collection.integration.models.BillInfoImpl;
import org.egov.collection.integration.pgi.PaymentRequest;
import org.egov.demand.dao.EgBillDao;
import org.egov.demand.model.EgBill;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.ptis.client.bill.PTBillServiceImpl;
import org.egov.ptis.client.integration.utils.CollectionHelper;
import org.egov.ptis.client.integration.utils.SpringBeanUtil;
import org.egov.ptis.client.util.PropertyTaxNumberGenerator;
import org.egov.ptis.domain.bill.PropertyTaxBillable;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = {"/public/mobile","/mobile"})
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
	
	/**
	 * API to process payments from Mobile App 
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
    public String newform(final Model model, @PathVariable final String assessmentNo, @PathVariable String ulbCode, @PathVariable BigDecimal amountToBePaid, 
    		@PathVariable String mobileNumber, @PathVariable String emailId, @PathVariable String category, final HttpServletRequest request)
            throws ParseException {
		String redirectUrl = "";
		BillInfoImpl billInfo = getBillInfo(assessmentNo, amountToBePaid);
		if(billInfo != null){
			PaymentRequest paymentRequest = SpringBeanUtil.getCollectionIntegrationService().processMobilePayments(billInfo);
	        if(paymentRequest != null){
	        	for(Object obj : paymentRequest.getRequestParameters().values()){
		        	redirectUrl = obj.toString();
		        }
		        model.addAttribute("redirectUrl", redirectUrl);
	        }
		}else{
			model.addAttribute("errorMsg", "Bill data is incorrect");
            return PROPERTY_VALIDATION;
		}
        return PAYTAX_FORM;
    }
	
	/**
     * API to return BillInfoImpl, used in tax payment through Mobile App
     * @param mobilePropertyTaxDetails
     * @return
     */
    public BillInfoImpl getBillInfo(final String assessmentNo, final BigDecimal amountToBePaid) {
    	BillInfoImpl billInfoImpl = null;
        final BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(assessmentNo);
        propertyTaxBillable.setBasicProperty(basicProperty);
        propertyTaxBillable.setUserId(2L);
        ApplicationThreadLocals.setUserId(2L);
        propertyTaxBillable.setReferenceNumber(propertyTaxNumberGenerator.generateBillNumber(basicProperty
                .getPropertyID().getWard().getBoundaryNum().toString()));
        propertyTaxBillable.setBillType(egBillDAO.getBillTypeByCode(BILLTYPE_AUTO));
        propertyTaxBillable.setLevyPenalty(Boolean.TRUE);

        final EgBill egBill = ptBillServiceImpl.generateBill(propertyTaxBillable);
        final CollectionHelper collectionHelper = new CollectionHelper(egBill);
        
        billInfoImpl = collectionHelper.prepareBillInfo(amountToBePaid, COLLECTIONTYPE.O, null);
        return billInfoImpl;
    }

}

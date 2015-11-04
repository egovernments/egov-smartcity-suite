/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

        1) All versions of this program, verbatim or modified must carry this
           Legal Notice.

        2) Any misrepresentation of the origin of the material is prohibited. It
           is required that all modified versions of this material be marked in
           reasonable ways as different from the original version.

        3) This license does not grant any rights to any user of the program
           with regards to rights under trademark law for use of the trade names
           or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.restapi.web.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.egov.collection.integration.models.RestAggregatePaymentInfo;
import org.egov.collection.integration.models.RestReceiptInfo;
import org.egov.collection.integration.services.CollectionIntegrationService;
import org.egov.collection.service.ServiceCategoryService;
import org.egov.commons.Bank;
import org.egov.commons.dao.BankHibernateDAO;
import org.egov.infra.web.support.json.adapter.HibernateProxyTypeAdapter;
import org.egov.infstr.models.ServiceCategory;
import org.egov.ptis.domain.model.ErrorDetails;
import org.egov.restapi.model.PaymentInfoSearchRequest;
import org.egov.restapi.util.JsonConvertor;
import org.egov.restapi.util.ValidationUtil;
import org.egov.search.domain.Document;
import org.egov.services.masters.BankService;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

@RestController
public class RestPaymentReportConroller {

	@Autowired
	private  CollectionIntegrationService collectionService;

	@Autowired
	private ServiceCategoryService serviceCategoryService;

	@Autowired
	private  BankHibernateDAO bankHibernateDAO;

	@Autowired
	ApplicationContext applicationContext;



	@RequestMapping(value = "/reconciliation/paymentaggregate", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public String searchAggregatePaymentsByDate(@RequestBody final PaymentInfoSearchRequest paymentInfoSearchRequest)
			throws JsonGenerationException, JsonMappingException, IOException {

		List<RestAggregatePaymentInfo> listAggregatePaymentInfo = collectionService.getAggregateReceiptTotal(
				paymentInfoSearchRequest.getFromdate(),
				paymentInfoSearchRequest.getTodate());
		return getJSONResponse(listAggregatePaymentInfo);
	}

	@RequestMapping(value = "/reconciliation/paymentdetails", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public String searchPaymentDetailsByServiceAndDate(@RequestBody final PaymentInfoSearchRequest paymentInfoSearchRequest,BindingResult errors)
			throws JsonGenerationException, JsonMappingException, IOException {


		validatePaymentDetails(paymentInfoSearchRequest,errors);
		List<RestReceiptInfo> receiptInfoList = collectionService.getReceiptDetailsByDateAndService(
				paymentInfoSearchRequest.getFromdate(),
				paymentInfoSearchRequest.getTodate(), paymentInfoSearchRequest.getServicecode());
		return getJSONResponse(receiptInfoList);
	}

	private void validatePaymentDetails(PaymentInfoSearchRequest  payment, BindingResult errors) {
		if(payment.getFromdate()==null )
		{

		}

	}

	@RequestMapping(value="/banks",method=RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	public String bankNames() {
		List<Bank> banks=null;
		try {
			banks = bankHibernateDAO.findAll(); 

		} catch (Exception e) {
			ErrorDetails er=new ErrorDetails();
			er.setErrorCode(e.getMessage());
			er.setErrorMessage(e.getMessage());
			return	JsonConvertor.convert(er);
		}
		return	JsonConvertor.convert(banks);

	}
	@RequestMapping(value="/services",method=RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	public String services() throws JsonGenerationException, JsonMappingException, IOException {

		Map<String, String> serviceCategory=null;
		List<ServiceCategory> services;
		try {
			services = serviceCategoryService.getAllActiveServiceCategories();
			if(services!=null || services.size()>=0)
			{
				serviceCategory=new LinkedHashMap<String, String>();
				for(ServiceCategory scs:services)
				{
					serviceCategory.put(scs.getCode(), scs.getName());
				}
			}
		} catch (Exception e) {
			ErrorDetails er=new ErrorDetails();
			er.setErrorCode(e.getMessage());
			er.setErrorMessage(e.getMessage());
			return	JsonConvertor.convert(er);

		}
		return	JsonConvertor.convert(serviceCategory);

	}

	/**
	 * This method is used to prepare jSON response.
	 *
	 * @param obj - a POJO object
	 * @return jsonResponse - JSON response string
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	private String getJSONResponse(final Object obj) throws JsonGenerationException, JsonMappingException, IOException {
		final Gson jsonCreator = new GsonBuilder().registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY)
				.disableHtmlEscaping().create();
		return jsonCreator.toJson(obj, new TypeToken<Collection<Document>>() {
		}.getType());
	}
}

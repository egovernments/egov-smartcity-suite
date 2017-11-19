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
package org.egov.works.web.controller.contractorbill;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.dao.ChartOfAccountsHibernateDAO;
import org.egov.works.contractorbill.entity.ContractorBillRegister;
import org.egov.works.contractorbill.entity.SearchRequestContractorBill;
import org.egov.works.contractorbill.service.ContractorBillRegisterService;
import org.egov.works.web.adaptor.SearchContractorBillJsonAdaptor;
import org.egov.works.web.adaptor.SearchContractorBillsToCancelJsonAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping(value = "/contractorbill")
public class AjaxContractorBillController {

    @Autowired
    private ContractorBillRegisterService contractorBillRegisterService;

    @Autowired
    private SearchContractorBillJsonAdaptor searchContractorBillJsonAdaptor;

    @Autowired
    private ChartOfAccountsHibernateDAO chartOfAccountsHibernateDAO;

    @Autowired
    private SearchContractorBillsToCancelJsonAdaptor searchContractorBillsToCancelJsonAdaptor;

    @RequestMapping(value = "/ajaxsearch-contractorbill", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String showSearchContractorBill(final Model model,
            @ModelAttribute final SearchRequestContractorBill searchRequestContractorBill) {
        final List<ContractorBillRegister> searchContractorBillList = contractorBillRegisterService
                .searchContractorBill(searchRequestContractorBill);
        final String result = new StringBuilder("{ \"data\":").append(toSearchContractorBillJson(searchContractorBillList))
                .append("}").toString();
        return result;
    }

    public Object toSearchContractorBillJson(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(ContractorBillRegister.class, searchContractorBillJsonAdaptor).create();
        final String json = gson.toJson(object);
        return json;
    }

    @RequestMapping(value = "/ajaxworkidentificationnumber-contractorbill", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<String> findworkIdNumbersForLoa(@RequestParam final String code) {
        return contractorBillRegisterService.findWorkIdentificationNumbersToSearchContractorBill(code);
    }

    @RequestMapping(value = "/ajaxsearchcontractors-contractorbill", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<String> findLoaContractor(@RequestParam final String contractorname) {
        return contractorBillRegisterService.getApprovedContractorsForCreateContractorBill(contractorname);
    }

    @RequestMapping(value = "/ajaxdeduction-coa", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<CChartOfAccounts> findDetailedAccountCodesByGlcodeLike(@RequestParam final String searchQuery) {
        return chartOfAccountsHibernateDAO.findDetailedAccountCodesByGlcodeOrNameLike(searchQuery);
    }

    @RequestMapping(value = "/cancel/ajax-search", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String searchContractorBillsToCancel(final Model model,
            @ModelAttribute final SearchRequestContractorBill searchRequestContractorBill) {
        final List<ContractorBillRegister> searchContractorBillList = contractorBillRegisterService
                .searchContractorBillsToCancel(searchRequestContractorBill);
        final String result = new StringBuilder("{ \"data\":")
                .append(toSearchContractorBillsToCancelJson(searchContractorBillList))
                .append("}").toString();
        return result;
    }

    public Object toSearchContractorBillsToCancelJson(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(ContractorBillRegister.class, searchContractorBillsToCancelJsonAdaptor)
                .create();
        final String json = gson.toJson(object);
        return json;
    }

    @RequestMapping(value = "/ajaxworkidentificationnumbers-contractorbilltocancel", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<String> findworkIdNumbersToCancelContractorBill(@RequestParam final String code) {
        return contractorBillRegisterService.findWorkIdentificationNumbersToSearchContractorBillToCancel(code);
    }

    @RequestMapping(value = "/ajaxbillnumbers-contractorbilltocancel", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<String> findBillNumbersToCancelContractorBill(@RequestParam final String billNumber) {
        return contractorBillRegisterService.findBillNumbersToSearchContractorBillToCancel(billNumber);
    }
    
    @RequestMapping(value = "/gettotalcreditanddebitamount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object findRundedAndWithHeldAmountForWorkOrderByAccountCode(@RequestParam final Long workOrderEstimateId,@RequestParam final BigDecimal glCodeId,@RequestParam final Long contractorBillId) {
       return contractorBillRegisterService.getTotalDebitAndCreditAmountByAccountCode(workOrderEstimateId,glCodeId,contractorBillId);
    }
}

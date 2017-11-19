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

package org.egov.egf.web.controller.contract;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.egov.billsaccounting.services.CreateVoucher;
import org.egov.billsaccounting.services.VoucherConstant;
import org.egov.commons.CFiscalPeriod;
import org.egov.commons.CFunction;
import org.egov.commons.CGeneralLedger;
import org.egov.commons.CGeneralLedgerDetail;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.dao.FiscalPeriodHibernateDAO;
import org.egov.commons.service.FunctionService;
import org.egov.commons.service.FunctionaryService;
import org.egov.commons.service.FundService;
import org.egov.commons.service.FundsourceService;
import org.egov.egf.contract.model.AccountDetailContract;
import org.egov.egf.contract.model.AccountDetailKeyContract;
import org.egov.egf.contract.model.AccountDetailTypeContract;
import org.egov.egf.contract.model.ErrorDetail;
import org.egov.egf.contract.model.FinancialYearContract;
import org.egov.egf.contract.model.FiscalPeriodContract;
import org.egov.egf.contract.model.FunctionContract;
import org.egov.egf.contract.model.FunctionaryContract;
import org.egov.egf.contract.model.FundsourceContract;
import org.egov.egf.contract.model.SchemeContract;
import org.egov.egf.contract.model.SubledgerDetailContract;
import org.egov.egf.contract.model.VoucherContract;
import org.egov.egf.contract.model.VoucherContractResponse;
import org.egov.egf.contract.model.VoucherRequest;
import org.egov.egf.contract.model.VoucherResponse;
import org.egov.egf.voucher.service.ContractVoucherService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.services.masters.SchemeService;
import org.egov.services.masters.SubSchemeService;
import org.egov.services.voucher.GeneralLedgerDetailService;
import org.egov.services.voucher.GeneralLedgerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/vouchers")
public class ContractVoucherController {

    @Autowired
    private CreateVoucher createVoucher;

    @Autowired
    private FundService fundService;

    @Autowired
    private FunctionService functionService;

    @Autowired
    private ContractVoucherService contractVoucherService;

    @Autowired
    private FiscalPeriodHibernateDAO fiscalPeriodHibernateDAO;

    @Autowired
    private GeneralLedgerService generalLedgerService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private GeneralLedgerDetailService generalLedgerDetailService;

    @Autowired
    private SchemeService schemeService;

    @Autowired
    private SubSchemeService subSchemeService;

    @Autowired
    private FundsourceService fundsourceService;

    @Autowired
    private FunctionaryService functionaryService;

    @RequestMapping(value = "/_create", method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public @ResponseBody String createVoucher(@RequestBody final VoucherContract voucherRequest,
            @RequestParam final String tenantId, final HttpServletResponse response) throws Exception {

        List<ErrorDetail> errorList = new ArrayList<>(0);
        final ErrorDetail re = new ErrorDetail();
        ApplicationThreadLocals.setUserId(2L);

        CVoucherHeader cVoucherHeader = new CVoucherHeader();

        errorList = contractVoucherService.validateVoucherReuest(voucherRequest);

        if (!errorList.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return getJSONResponse(errorList);
        }

        final HashMap<String, Object> headerDetails = new HashMap<String, Object>();
        final List<HashMap<String, Object>> accountCodeDetails = new ArrayList<HashMap<String, Object>>();
        final List<HashMap<String, Object>> subledgerDetails = new ArrayList<HashMap<String, Object>>();

        for (final VoucherRequest request : voucherRequest.getVouchers()) {

            prepairHeaderDetail(headerDetails, request);

            prepairSchemeAndSubScheme(headerDetails, request);

            prepairFunctionaryFundsource(headerDetails, request);

            final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            try {
                headerDetails.put(VoucherConstant.VOUCHERDATE, formatter.parse(request.getVoucherDate()));
            } catch (final ParseException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }

            if (!request.getLedgers().isEmpty())
                for (final AccountDetailContract accountDetailContract : request.getLedgers()) {
                    prepairAccountDetail(accountCodeDetails, accountDetailContract);

                    if (!accountDetailContract.getSubledgerDetails().isEmpty())
                        for (final SubledgerDetailContract subledgerDetailContract : accountDetailContract
                                .getSubledgerDetails())
                            prepairSubledgerDetails(subledgerDetails, accountDetailContract, subledgerDetailContract);
                }

            try {
                cVoucherHeader = createVoucher.createVoucher(headerDetails, accountCodeDetails, subledgerDetails);
            } catch (final ApplicationRuntimeException e) {
                re.setErrorCode(e.getMessage());
                re.setErrorMessage(e.getMessage());
                errorList.add(re);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return getJSONResponse(errorList);

            } catch (final ValidationException e) {
                re.setErrorCode(e.getErrors().get(0).getKey());
                re.setErrorMessage(e.getErrors().get(0).getMessage());
                errorList.add(re);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return getJSONResponse(errorList);

            } catch (final Exception e) {
                re.setErrorCode(e.getMessage());
                re.setErrorMessage(e.getMessage());
                errorList.add(re);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return getJSONResponse(errorList);

            }

        }

        final VoucherContractResponse voucherContractResponse = new VoucherContractResponse();
        final List<VoucherResponse> vouchers = new ArrayList<>();
        final VoucherResponse voucherResponse = prepairVoucherResponse(cVoucherHeader);
        final List<AccountDetailContract> accountDetailContracts = new ArrayList<>();
        final List<SubledgerDetailContract> subledgerDetailContracts = new ArrayList<>();
        List<CGeneralLedgerDetail> cGeneralLedgerDetails = new ArrayList<>();
        for (final CGeneralLedger cGeneralLedger : generalLedgerService
                .findCGeneralLedgerByVoucherHeaderId(cVoucherHeader.getId())) {

            final AccountDetailContract accountDetailContract = prepairAccountDetailResponse(cGeneralLedger);

            accountDetailContracts.add(accountDetailContract);

            cGeneralLedgerDetails = generalLedgerDetailService
                    .findCGeneralLedgerDetailByLedgerId(cGeneralLedger.getId());
            if (!cGeneralLedgerDetails.isEmpty()) {
                for (final CGeneralLedgerDetail cGeneralLedgerDetail : cGeneralLedgerDetails) {
                    final SubledgerDetailContract subledgerDetailContract1 = prepairSubledgerDetailResponse(
                            cGeneralLedgerDetail);
                    subledgerDetailContracts.add(subledgerDetailContract1);
                }

                accountDetailContracts.get(0).setSubledgerDetails(subledgerDetailContracts);

            }

        }

        voucherResponse.setLedgers(accountDetailContracts);

        vouchers.add(voucherResponse);
        voucherContractResponse.setVouchers(vouchers);
        response.setStatus(HttpServletResponse.SC_CREATED);
        return getJSONResponse(voucherContractResponse);
    }

    private AccountDetailContract prepairAccountDetailResponse(final CGeneralLedger cGeneralLedger) {
        final AccountDetailContract accountDetailContract = new AccountDetailContract();
        accountDetailContract.setId(cGeneralLedger.getId());
        accountDetailContract.setCreditAmount(cGeneralLedger.getCreditAmount());
        accountDetailContract.setDebitAmount(cGeneralLedger.getDebitAmount());
        accountDetailContract.setGlcode(cGeneralLedger.getGlcode());
        if (cGeneralLedger.getFunctionId() != null) {
            final FunctionContract functionContract = new FunctionContract();
            final CFunction cFunction = functionService.findOne(Long.valueOf(cGeneralLedger.getFunctionId()));
            functionContract.setActive(cFunction.getIsActive());
            functionContract.setCode(cFunction.getCode());
            functionContract.setId(cFunction.getId());
            if (cFunction.getParentId() != null) {
                functionContract.setParentId(cFunction.getParentId().getId());
                functionContract.setIsParent(Boolean.TRUE);
            } else
                functionContract.setIsParent(Boolean.FALSE);
            functionContract.setLevel(Long.valueOf(cFunction.getLlevel()));
            functionContract.setName(cFunction.getName());
            accountDetailContract.setFunction(functionContract);
        }
        return accountDetailContract;
    }

    private SubledgerDetailContract prepairSubledgerDetailResponse(final CGeneralLedgerDetail cGeneralLedgerDetail) {
        final SubledgerDetailContract subledgerDetailContract1 = new SubledgerDetailContract();
        subledgerDetailContract1.setId(cGeneralLedgerDetail.getId());
        subledgerDetailContract1.setAmount(cGeneralLedgerDetail.getAmount().doubleValue());
        final AccountDetailKeyContract accountDetailKeyContract = new AccountDetailKeyContract();

        final AccountDetailTypeContract accountDetailTypeContract = new AccountDetailTypeContract();
        if (cGeneralLedgerDetail.getDetailTypeId() != null)
            accountDetailTypeContract.setActive(cGeneralLedgerDetail.getDetailTypeId().getIsactive());
        accountDetailTypeContract.setDescription(cGeneralLedgerDetail.getDetailTypeId().getDescription());
        accountDetailTypeContract.setFullyQualifiedName(cGeneralLedgerDetail.getDetailTypeId().getFullQualifiedName());
        accountDetailTypeContract.setId(Long.valueOf(cGeneralLedgerDetail.getDetailTypeId().getId()));
        accountDetailTypeContract.setName(cGeneralLedgerDetail.getDetailTypeId().getName());
        accountDetailTypeContract.setTableName(cGeneralLedgerDetail.getDetailTypeId().getTablename());

        subledgerDetailContract1.setAccountDetailType(accountDetailTypeContract);

        accountDetailKeyContract.setAccountDetailType(accountDetailTypeContract);
        accountDetailKeyContract.setId(Long.valueOf(cGeneralLedgerDetail.getDetailKeyId()));

        subledgerDetailContract1.setAccountDetailKey(accountDetailKeyContract);
        return subledgerDetailContract1;
    }

    private void prepairHeaderDetail(final HashMap<String, Object> headerDetails, final VoucherRequest request) {
        headerDetails.put(VoucherConstant.VOUCHERNAME, request.getName());
        headerDetails.put(VoucherConstant.VOUCHERTYPE, request.getType());
        headerDetails.put(VoucherConstant.DESCRIPTION, request.getDescription());
        headerDetails.put(VoucherConstant.SOURCEPATH, request.getSource());
        if (request.getDepartment() != null)
            headerDetails.put(VoucherConstant.DEPARTMENTCODE,
                    departmentService.getDepartmentById(request.getDepartment()).getCode());
        headerDetails.put(VoucherConstant.MODULEID, request.getModuleId());
        headerDetails.put(VoucherConstant.VOUCHERNUMBER, request.getVoucherNumber());
        if (request.getFund() != null && request.getFund().getId() != null)
            headerDetails.put(VoucherConstant.FUNDCODE,
                    fundService.findOne(request.getFund().getId().intValue()).getCode());
        else if (request.getFund() != null && !request.getFund().getCode().isEmpty())
            headerDetails.put(VoucherConstant.FUNDCODE, request.getFund().getCode());
        else if (request.getFund() != null && !request.getFund().getName().isEmpty())
            headerDetails.put(VoucherConstant.FUNDCODE, fundService.findByName(request.getFund().getName()).getCode());
    }

    private void prepairAccountDetail(final List<HashMap<String, Object>> accountCodeDetails,
            final AccountDetailContract accountDetailContract) {
        HashMap<String, Object> accountDetails1;
        accountDetails1 = new HashMap<String, Object>();
        accountDetails1.put(VoucherConstant.GLCODE, accountDetailContract.getGlcode());
        accountDetails1.put(VoucherConstant.DEBITAMOUNT, accountDetailContract.getDebitAmount());
        accountDetails1.put(VoucherConstant.CREDITAMOUNT, accountDetailContract.getCreditAmount());

        if (accountDetailContract.getFunction() != null && accountDetailContract.getFunction().getId() != null)
            accountDetails1.put(VoucherConstant.FUNCTIONCODE,
                    functionService.findOne(accountDetailContract.getFunction().getId()).getCode());
        else if (accountDetailContract.getFunction() != null && accountDetailContract.getFunction().getCode() != null)
            accountDetails1.put(VoucherConstant.FUNCTIONCODE, accountDetailContract.getFunction().getCode());
        else if (accountDetailContract.getFunction() != null && accountDetailContract.getFunction().getName() != null)
            accountDetails1.put(VoucherConstant.FUNCTIONCODE,
                    functionService.findByName(accountDetailContract.getFunction().getName()).getCode());
        accountCodeDetails.add(accountDetails1);
    }

    private void prepairSubledgerDetails(final List<HashMap<String, Object>> subledgerDetails,
            final AccountDetailContract accountDetailContract, final SubledgerDetailContract subledgerDetailContract) {
        HashMap<String, Object> subledgerDetails1;
        subledgerDetails1 = new HashMap<String, Object>();
        subledgerDetails1.put(VoucherConstant.CREDITAMOUNT, subledgerDetailContract.getAmount());
        subledgerDetails1.put(VoucherConstant.GLCODE, accountDetailContract.getGlcode());
        subledgerDetails1.put(VoucherConstant.DETAILTYPEID, subledgerDetailContract.getAccountDetailType().getId());
        subledgerDetails1.put(VoucherConstant.DETAILKEYID, subledgerDetailContract.getAccountDetailKey().getId());
        subledgerDetails.add(subledgerDetails1);
    }

    private VoucherResponse prepairVoucherResponse(final CVoucherHeader cVoucherHeader) {
        final VoucherResponse voucherResponse = new VoucherResponse();
        voucherResponse.setFund(cVoucherHeader.getFundId());

        if (cVoucherHeader.getVouchermis().getFunctionary() != null) {
            final FunctionaryContract functionaryContract = new FunctionaryContract();
            functionaryContract.setCode(cVoucherHeader.getVouchermis().getFunctionary().getCode().toString());
            voucherResponse.setFunctionary(functionaryContract);
        }
        if (cVoucherHeader.getVouchermis().getFundsource() != null) {
            final FundsourceContract fundsourceContract = new FundsourceContract();
            fundsourceContract.setCode(cVoucherHeader.getVouchermis().getFundsource().getCode());
            voucherResponse.setFundsource(fundsourceContract);
        }
        if (cVoucherHeader.getVouchermis().getSchemeid() != null) {
            final SchemeContract schemeContract = new SchemeContract();
            schemeContract.setCode(cVoucherHeader.getVouchermis().getSchemeid().getCode());
            voucherResponse.setScheme(schemeContract);
        }
        if (cVoucherHeader.getVouchermis().getSubschemeid() != null) {
            final SchemeContract subSchemeContract = new SchemeContract();
            subSchemeContract.setCode(cVoucherHeader.getVouchermis().getSubschemeid().getCode());
            voucherResponse.setSubScheme(subSchemeContract);
        }

        voucherResponse.setSource(cVoucherHeader.getVouchermis().getSourcePath());
        voucherResponse.setCgvn(cVoucherHeader.getCgvn());
        voucherResponse.setVoucherNumber(cVoucherHeader.getVoucherNumber());
        voucherResponse.setDescription(cVoucherHeader.getDescription());
        voucherResponse.setVoucherDate(
                org.egov.infra.utils.DateUtils.getFormattedDate(cVoucherHeader.getVoucherDate(), "dd-MM-yyyy"));
        voucherResponse.setName(cVoucherHeader.getName());
        voucherResponse.setType(cVoucherHeader.getType());
        voucherResponse.setOriginalVhId(cVoucherHeader.getOriginalvcId());
        voucherResponse.setRefVhId(cVoucherHeader.getRefvhId());
        voucherResponse.setStatus("Approved");
        voucherResponse.setId(cVoucherHeader.getId());

        final CFiscalPeriod fiscalPeriod = fiscalPeriodHibernateDAO
                .getFiscalPeriodByDate(cVoucherHeader.getVoucherDate());
        final FiscalPeriodContract fiscalPeriodContract = new FiscalPeriodContract();
        final FinancialYearContract financialYearContract = new FinancialYearContract();
        financialYearContract.setActive(fiscalPeriod.getcFinancialYear().getIsActive());
        financialYearContract.setEndingDate(fiscalPeriod.getcFinancialYear().getStartingDate());
        financialYearContract.setFinYearRange(fiscalPeriod.getcFinancialYear().getFinYearRange());
        financialYearContract.setId(fiscalPeriod.getcFinancialYear().getId());
        financialYearContract.setIsActiveForPosting(fiscalPeriod.getcFinancialYear().getIsActiveForPosting());
        financialYearContract.setIsClosed(fiscalPeriod.getcFinancialYear().getIsClosed());
        financialYearContract.setTransferClosingBalance(fiscalPeriod.getcFinancialYear().getTransferClosingBalance());
        fiscalPeriodContract.setActive(fiscalPeriod.getIsActive());
        fiscalPeriodContract.setStartingDate(fiscalPeriod.getStartingDate());
        fiscalPeriodContract.setEndingDate(fiscalPeriod.getEndingDate());
        fiscalPeriodContract.setId(fiscalPeriod.getId());
        fiscalPeriodContract.setIsActiveForPosting(fiscalPeriod.getIsActiveForPosting());
        fiscalPeriodContract.setName(fiscalPeriod.getName());
        fiscalPeriodContract.setFinancialYear(financialYearContract);
        voucherResponse.setFiscalPeriod(fiscalPeriodContract);
        return voucherResponse;
    }

    private String getJSONResponse(final Object obj) throws IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("dd-MM-yyyy"));
        final String jsonResponse = objectMapper.writeValueAsString(obj);
        return jsonResponse;
    }

    private void prepairSchemeAndSubScheme(final HashMap<String, Object> headerDetails, final VoucherRequest request) {
        if (request.getScheme() != null && request.getScheme().getId() != null)
            headerDetails.put(VoucherConstant.SCHEMECODE,
                    schemeService.findById(request.getScheme().getId().intValue(), false) != null
                            ? schemeService.findById(request.getScheme().getId().intValue(), false).getCode() : null);
        else if (request.getScheme() != null && !request.getScheme().getCode().isEmpty())
            headerDetails.put(VoucherConstant.SCHEMECODE, request.getScheme().getCode());
        if (request.getScheme() != null && request.getScheme().getCode() != null && request.getSubScheme() != null
                && request.getSubScheme().getId() != null)
            headerDetails.put(VoucherConstant.SUBSCHEMECODE,
                    subSchemeService.findById(request.getSubScheme().getId().intValue(), false) != null
                            ? subSchemeService.findById(request.getSubScheme().getId().intValue(), false).getCode()
                            : null);
        else if (request.getScheme() != null && request.getScheme().getCode() != null && request.getSubScheme() != null
                && !request.getSubScheme().getCode().isEmpty())
            headerDetails.put(VoucherConstant.SUBSCHEMECODE, request.getSubScheme().getCode());
    }

    private void prepairFunctionaryFundsource(final HashMap<String, Object> headerDetails,
            final VoucherRequest request) {
        if (request.getFundsource() != null && request.getFundsource().getId() != null)
            headerDetails.put(VoucherConstant.FUNDSOURCECODE,
                    fundsourceService.findOne(request.getFundsource().getId()) != null
                            ? fundsourceService.findOne(request.getFundsource().getId()).getCode() : null);
        else if (request.getFundsource() != null && !request.getFundsource().getCode().isEmpty())
            headerDetails.put(VoucherConstant.FUNDSOURCECODE, request.getFundsource().getCode());
        if (request.getFunctionary() != null && request.getFunctionary().getId() != null)
            headerDetails.put(VoucherConstant.FUNCTIONARYCODE,
                    functionaryService.findOne(request.getFunctionary().getId()) != null
                            ? functionaryService.findOne(request.getFunctionary().getId()).getCode() : null);
        else if (request.getFunctionary() != null && !request.getFunctionary().getCode().isEmpty())
            headerDetails.put(VoucherConstant.FUNCTIONARYCODE, request.getFunctionary().getCode());
    }

}
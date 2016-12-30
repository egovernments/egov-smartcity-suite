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
package org.egov.egf.web.controller.common;

import static org.egov.infra.web.support.json.adapter.HibernateProxyTypeAdapter.FACTORY;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.Bankbranch;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFunction;
import org.egov.commons.Fundsource;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.commons.service.AccountdetailtypeService;
import org.egov.commons.service.ChartOfAccountsService;
import org.egov.commons.service.EntityTypeService;
import org.egov.commons.service.FunctionService;
import org.egov.commons.service.FundsourceService;
import org.egov.commons.utils.EntityType;
import org.egov.egf.billsubtype.service.EgBillSubTypeService;
import org.egov.egf.commons.bankbranch.service.CreateBankBranchService;
import org.egov.egf.web.adaptor.ChartOfAccountsAdaptor;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.model.bills.EgBillSubType;
import org.egov.services.masters.SchemeService;
import org.egov.services.masters.SubSchemeService;
import org.egov.utils.FinancialConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonSerializer;

/**
 * @author venki
 *
 */

@Controller
@RequestMapping(value = "/common")
public class AjaxCommonController {
    @Autowired
    @Qualifier("schemeService")
    private SchemeService schemeService;

    @Autowired
    @Qualifier("subSchemeService")
    private SubSchemeService subSchemeService;

    @Autowired
    private CreateBankBranchService createBankBranchService;

    @Autowired
    private FundsourceService fundsourceService;

    @Autowired
    private FunctionService functionService;

    @Autowired
    private AccountdetailtypeService accountdetailtypeService;

    @Autowired
    @Qualifier("chartOfAccountsService")
    private ChartOfAccountsService chartOfAccountsService;

    @Autowired
    private EgBillSubTypeService egBillSubTypeService;

    @Autowired
    private AppConfigValueService appConfigValueService;

    @RequestMapping(value = "/getschemesbyfundid", method = RequestMethod.GET)
    @ResponseBody
    public List<Scheme> getAllSchemesByFundId(@RequestParam("fundId") final String fundId)
            throws ApplicationException {
        return schemeService.getByFundId(Integer.parseInt(fundId));
    }

    @RequestMapping(value = "/getsubschemesbyschemeid", method = RequestMethod.GET)
    @ResponseBody
    public List<SubScheme> getAllSubSchemesBySchemeId(@RequestParam("schemeId") final String schemeId)
            throws ApplicationException {
        return subSchemeService.getBySchemeId(Integer.parseInt(schemeId));
    }

    @RequestMapping(value = "/getfundsourcesbysubschemeid", method = RequestMethod.GET)
    @ResponseBody
    public List<Fundsource> getAllFundSourcesBySubSchemeId(
            @RequestParam("subSchemeId") final String subSchemeId) throws ApplicationException {
        return fundsourceService.getBySubSchemeId(Integer.parseInt(subSchemeId));
    }

    @RequestMapping(value = "/ajaxfunctionnames", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<String> findFunctionNames(@RequestParam final String name) {
        final List<String> functionNames = new ArrayList<>();
        final List<CFunction> functions = functionService.findByNameLikeOrCodeLike(name);
        for (final CFunction function : functions)
            if (!function.getIsNotLeaf())
                functionNames.add(function.getCode() + " - " + function.getName() + " ~ " + function.getId());

        return functionNames;
    }

    @RequestMapping(value = "/getentitesbyaccountdetailtype", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<String> findEntitesByAccountDetailType(@RequestParam final String name,
            @RequestParam final String accountDetailType) {
        final List<String> entityNames = new ArrayList<>();
        List<EntityType> entitiesList = new ArrayList<>();
        final Accountdetailtype detailType = accountdetailtypeService.findOne(Integer.parseInt(accountDetailType));
        try {
            final String table = detailType.getFullQualifiedName();
            final Class<?> service = Class.forName(table);
            String simpleName = service.getSimpleName();
            simpleName = simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1) + "Service";

            final WebApplicationContext wac = WebApplicationContextUtils
                    .getWebApplicationContext(ServletActionContext.getServletContext());
            final EntityTypeService entityService = (EntityTypeService) wac.getBean(simpleName);
            entitiesList = (List<EntityType>) entityService.filterActiveEntities(name, 20, detailType.getId());
        } catch (final Exception e) {
            e.printStackTrace();
            entitiesList = new ArrayList<>();
        }
        for (final EntityType entity : entitiesList)
            entityNames.add(entity.getCode() + " - " + entity.getName() + "~" + entity.getEntityId());

        return entityNames;
    }

    @RequestMapping(value = "/getaccountcodesforaccountdetailtype", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findAccountCodesForAccountDetailType(@RequestParam final String glcode,
            @RequestParam final String accountDetailType) {
        final List<CChartOfAccounts> chartOfAccounts = chartOfAccountsService
                .getSubledgerAccountCodesForAccountDetailTypeAndNonSubledgers(Integer.parseInt(accountDetailType),
                        glcode);
        for (final CChartOfAccounts coa : chartOfAccounts)
            if (coa.getChartOfAccountDetails().isEmpty())
                coa.setIsSubLedger(false);
            else
                coa.setIsSubLedger(true);
        return toJSON(chartOfAccounts, CChartOfAccounts.class, ChartOfAccountsAdaptor.class);
    }

    @RequestMapping(value = "/getnetpayablecodesbyaccountdetailtype", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getNetPayableCodesByAccountDetailType(
            @RequestParam("accountDetailType") final String accountDetailType) throws ApplicationException {
        final List<CChartOfAccounts> chartOfAccounts = chartOfAccountsService
                .getNetPayableCodesByAccountDetailType(Integer.parseInt(accountDetailType));
        for (final CChartOfAccounts coa : chartOfAccounts)
            if (coa.getChartOfAccountDetails().isEmpty())
                coa.setIsSubLedger(false);
            else
                coa.setIsSubLedger(true);
        return toJSON(chartOfAccounts, CChartOfAccounts.class, ChartOfAccountsAdaptor.class);
    }

    @RequestMapping(value = "/getchecklistbybillsubtype", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<AppConfigValues> getCheckListByBillSubType(
            @RequestParam("billSubType") final String billSubType) {
        final EgBillSubType egBillSubType = egBillSubTypeService.getById(Long.parseLong(billSubType));

        List<AppConfigValues> checkList;
        checkList = appConfigValueService.getConfigValuesByModuleAndKey(FinancialConstants.MODULE_NAME_APPCONFIG,
                egBillSubType.getName());
        if (checkList == null || checkList.isEmpty())
            checkList = appConfigValueService.getConfigValuesByModuleAndKey(FinancialConstants.MODULE_NAME_APPCONFIG,
                    FinancialConstants.CBILL_DEFAULTCHECKLISTNAME);

        return checkList;
    }

    @RequestMapping(value = "/getallaccountcodes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findAllAccountCodes(@RequestParam final String glcode) {
        final List<CChartOfAccounts> chartOfAccounts = chartOfAccountsService.getAllAccountCodes(glcode);
        for (final CChartOfAccounts coa : chartOfAccounts)
            if (coa.getChartOfAccountDetails().isEmpty())
                coa.setIsSubLedger(false);
            else
                coa.setIsSubLedger(true);
        return toJSON(chartOfAccounts, CChartOfAccounts.class, ChartOfAccountsAdaptor.class);
    }

    @RequestMapping(value = "/getaccountdetailtypesbyglcodeid", method = RequestMethod.GET)
    @ResponseBody
    public List<Accountdetailtype> getAccountDetailTypesByGlcodeId(@RequestParam("glcodeId") final String glcodeId)
            throws ApplicationException {
        return accountdetailtypeService.findByGlcodeId(Long.parseLong(glcodeId));
    }

    @RequestMapping(value = "/getbankbranchesbybankid", method = RequestMethod.GET)
    @ResponseBody
    public List<Bankbranch> getBankbranchesByBankId(@RequestParam("bankId") final String bankId)
            throws ApplicationException {
        if (!"0".equals(bankId))
            return createBankBranchService.getByBankId(Integer.parseInt(bankId));
        else
            return createBankBranchService.getByIsActiveTrueOrderByBranchname();
    }

    public static <T> String toJSON(final Collection<T> objects, final Class<? extends T> objectClazz,
            final Class<? extends JsonSerializer<T>> adptorClazz) {
        try {
            return new GsonBuilder().registerTypeAdapterFactory(FACTORY)
                    .registerTypeAdapter(objectClazz, adptorClazz.newInstance()).create().toJson(objects);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ApplicationRuntimeException("Could not convert object list to json string", e);
        }
    }

}

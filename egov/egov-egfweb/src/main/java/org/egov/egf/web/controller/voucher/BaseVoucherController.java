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
package org.egov.egf.web.controller.voucher;

import com.exilant.eGov.src.transactions.VoucherTypeForULB;
import org.egov.commons.CGeneralLedger;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.service.ChartOfAccountsService;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.utils.DateUtils;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.utils.FinancialConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * @author venki
 *
 */

@Controller
public abstract class BaseVoucherController extends GenericWorkFlowController {

    protected List<String> headerFields = new ArrayList<String>();
    protected List<String> mandatoryFields = new ArrayList<String>();

    @Autowired
    protected AppConfigValueService appConfigValuesService;

    @Autowired
    @Qualifier("chartOfAccountsService")
    private ChartOfAccountsService chartOfAccountsService;

    @SuppressWarnings("deprecation")
    @Autowired
    private EgovMasterDataCaching masterDataCache;

    @Autowired
    private VoucherTypeForULB voucherTypeForULB;

    @Autowired
    public BaseVoucherController(final AppConfigValueService appConfigValuesService) {
        this.appConfigValuesService = appConfigValuesService;
        getHeaderMandateFields();
    }

    protected void getHeaderMandateFields() {
        final List<AppConfigValues> appConfigList = appConfigValuesService
                .getConfigValuesByModuleAndKey(FinancialConstants.MODULE_NAME_APPCONFIG,
                        FinancialConstants.KEY_DEFAULTTXNMISATTRRIBUTES);

        for (final AppConfigValues appConfigVal : appConfigList) {
            final String value = appConfigVal.getValue();
            final String header = value.substring(0, value.indexOf("|"));
            headerFields.add(header);
            final String mandate = value.substring(value.indexOf("|") + 1);
            if ("M".equals(mandate))
                mandatoryFields.add(header);
        }
        mandatoryFields.add("voucherdate");
    }

    @SuppressWarnings("deprecation")
    protected void setDropDownValues(final Model model) {

        if (headerFields.contains("department"))
            model.addAttribute("departments", masterDataCache.get("egi-department"));
        if (headerFields.contains("functionary"))
            model.addAttribute("functionarys", masterDataCache.get("egi-functionary"));
        if (headerFields.contains("function"))
            model.addAttribute("functions", masterDataCache.get("egi-function"));
        if (headerFields.contains("fund"))
            model.addAttribute("funds", masterDataCache.get("egi-fund"));
        if (headerFields.contains("fundsource"))
            model.addAttribute("fundsources", masterDataCache.get("egi-fundSource"));
        if (headerFields.contains("field"))
            model.addAttribute("fields", masterDataCache.get("egi-ward"));
        if (headerFields.contains("scheme"))
            model.addAttribute("schemes", Collections.emptyList());
        if (headerFields.contains("subscheme"))
            model.addAttribute("subschemes", Collections.emptyList());

        model.addAttribute("headerFields", headerFields);
        model.addAttribute("mandatoryFields", mandatoryFields);
    }

    protected void prepareValidActionListByCutOffDate(final Model model) {
        final List<AppConfigValues> cutOffDateconfigValue = appConfigValuesService
                .getConfigValuesByModuleAndKey(FinancialConstants.MODULE_NAME_APPCONFIG,
                        FinancialConstants.KEY_DATAENTRYCUTOFFDATE);

        if (!cutOffDateconfigValue.isEmpty()) {
            final DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            model.addAttribute("validActionList",
                    Arrays.asList(FinancialConstants.BUTTONFORWARD, FinancialConstants.CREATEANDAPPROVE));
            try {
                model.addAttribute("cutOffDate",
                        DateUtils.getDefaultFormattedDate(df.parse(cutOffDateconfigValue.get(0).getValue())));
            } catch (final ParseException e) {
                e.printStackTrace();
            }
        }
    }

    protected Boolean isVoucherNumberGenerationAuto(final CVoucherHeader voucherHeader) {
        String vNumGenMode;
        if (voucherHeader.getType() != null
                && FinancialConstants.STANDARD_VOUCHER_TYPE_JOURNAL.equalsIgnoreCase(voucherHeader.getType()))
            vNumGenMode = voucherTypeForULB.readVoucherTypes(FinancialConstants.VOUCHER_TYPE_JOURNAL);
        else
            vNumGenMode = voucherTypeForULB.readVoucherTypes(voucherHeader.getType());
        if (!FinancialConstants.AUTO.equalsIgnoreCase(vNumGenMode)) {
            mandatoryFields.add("vouchernumber");
            return true;
        } else
            return false;
    }

    @SuppressWarnings("unchecked")
    protected void populateAccountDetails(final CVoucherHeader voucherHeader) {

        if (voucherHeader.getGeneralLedger() != null)
            voucherHeader.getGeneralLedger().clear();
        else
            voucherHeader.setGeneralLedger(new HashSet<>());
        voucherHeader.getGeneralLedger().addAll(voucherHeader.getAccountDetails());
        Integer voucherLineId = 1;
        for (final CGeneralLedger details : voucherHeader.getGeneralLedger()) {
            details.setVoucherlineId(voucherLineId++);
            details.setEffectiveDate(new Date());
            if (voucherHeader.getVouchermis().getFunction() != null)
                details.setFunctionId(voucherHeader.getVouchermis().getFunction().getId().intValue());
            details.setVoucherHeaderId(voucherHeader);
            details.setGlcodeId(chartOfAccountsService.findById(details.getGlcodeId().getId(), false));
            if (details.getDebitAmount() != null)
                details.setCreditAmount((double) 0);
            else
                details.setDebitAmount((double) 0);
        }
    }

    protected void populateVoucherName(final CVoucherHeader voucherHeader) {

        switch (voucherHeader.getVoucherSubType()) {
        case FinancialConstants.JOURNALVOUCHER_NAME_GENERAL:
            voucherHeader.setVoucherNumType(FinancialConstants.VOUCHER_TYPE_JOURNAL);
            voucherHeader.setName(FinancialConstants.JOURNALVOUCHER_NAME_GENERAL);
            break;

        case FinancialConstants.STANDARD_EXPENDITURETYPE_WORKS:
            voucherHeader.setVoucherNumType(FinancialConstants.VOUCHER_TYPE_WORKS);
            voucherHeader.setName(FinancialConstants.JOURNALVOUCHER_NAME_CONTRACTORJOURNAL);
            break;

        case FinancialConstants.STANDARD_EXPENDITURETYPE_PURCHASE:
            voucherHeader.setVoucherNumType(FinancialConstants.VOUCHER_TYPE_PURCHASE);
            voucherHeader.setName(FinancialConstants.JOURNALVOUCHER_NAME_SUPPLIERJOURNAL);
            break;

        case FinancialConstants.STANDARD_EXPENDITURETYPE_SALARY:
            voucherHeader.setVoucherNumType(FinancialConstants.VOUCHER_TYPE_SALARY);
            voucherHeader.setName(FinancialConstants.JOURNALVOUCHER_NAME_SALARYJOURNAL);
            break;

        case FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT:
            voucherHeader.setVoucherNumType(FinancialConstants.VOUCHER_TYPE_CONTINGENT);
            voucherHeader.setName(FinancialConstants.JOURNALVOUCHER_NAME_EXPENSEJOURNAL);
            break;

        case FinancialConstants.STANDARD_SUBTYPE_FIXED_ASSET:
            voucherHeader.setVoucherNumType(FinancialConstants.VOUCHER_TYPE_FIXEDASSET);
            voucherHeader.setName(FinancialConstants.JOURNALVOUCHER_NAME_SUPPLIERJOURNAL);
            break;

        case FinancialConstants.STANDARD_EXPENDITURETYPE_PENSION:
            voucherHeader.setVoucherNumType(FinancialConstants.VOUCHER_TYPE_PENSION);
            voucherHeader.setName(FinancialConstants.JOURNALVOUCHER_NAME_PENSIONJOURNAL);
            break;
        default:
            break;
        }

    }

}
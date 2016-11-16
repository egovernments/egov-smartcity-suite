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
package org.egov.egf.web.controller.voucher;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.egov.commons.CVoucherHeader;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.utils.DateUtils;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.utils.FinancialConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.exilant.eGov.src.transactions.VoucherTypeForULB;

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
            vNumGenMode = voucherTypeForULB.readVoucherTypes(FinancialConstants.JOURNAL);
        else
            vNumGenMode = voucherTypeForULB.readVoucherTypes(voucherHeader.getType());
        if (!FinancialConstants.AUTO.equalsIgnoreCase(vNumGenMode)) {
            mandatoryFields.add("vouchernumber");
            return true;
        } else
            return false;
    }

}
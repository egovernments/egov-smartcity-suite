/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2016>  eGovernments Foundation
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

package org.egov.adtax.web.controller.demand;

import static org.egov.infra.utils.JsonUtils.toJSON;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.egov.adtax.entity.AdvertisementDemandGenerationLog;
import org.egov.adtax.entity.AdvertisementDemandGenerationLogDetail;
import org.egov.adtax.search.contract.AdvertisementDemandStatus;
import org.egov.adtax.service.AdTaxDemandGenerationLogService;
import org.egov.adtax.utils.constants.AdvertisementTaxConstants;
import org.egov.adtax.web.adaptor.AdvertisementDemandStatusAdapter;
import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentDao;
import org.egov.infra.admin.master.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/advertisement")
public class ViewAdTaxDemandGenerationStatusController {

    private static final String DATA = "{\"data\":";
    private static final String DEMAND_STATUS_FORM = "demand-status-form";

    @Autowired
    private InstallmentDao installmentDao;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private AdTaxDemandGenerationLogService adTaxDemandGenerationLogService;

    @ModelAttribute("financialYears")
    public List<Installment> financialyear() {

        return installmentDao.getInsatllmentByModule(moduleService.getModuleByName(AdvertisementTaxConstants.MODULE_NAME));
    }

    @RequestMapping(value = "/demand-status", method = GET)
    public String viewDemand(@ModelAttribute final AdvertisementDemandStatus advertisementDemandStatus) {
        return DEMAND_STATUS_FORM;
    }

    @RequestMapping(value = "/demand-status", method = POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String getDemandGenerationStatus(@ModelAttribute final AdvertisementDemandStatus advertisementDemandStatus,
            @RequestParam final String financialYear, final HttpServletRequest request) {
        final List<AdvertisementDemandStatus> resultList = new ArrayList<>();
        if (financialYear != null) {
            final List<AdvertisementDemandGenerationLog> generationLogList = adTaxDemandGenerationLogService
                    .getDemandGenerationLogByInstallmentYear(financialYear);
            if (generationLogList != null && !generationLogList.isEmpty()) {
                final AdvertisementDemandStatus demandStatus = adTaxDemandGenerationLogService
                        .getDemandStatusResult(generationLogList);
                demandStatus.setFinancialYear(financialYear);
                resultList.add(demandStatus);
            }
        }
        return new StringBuilder(DATA)
                .append(toJSON(resultList, AdvertisementDemandStatus.class, AdvertisementDemandStatusAdapter.class))
                .append("}")
                .toString();
    }

    @RequestMapping(value = "/demand-status-records-view/{financialYear}", method = GET)
    public String viewDemandStatusOfRecords(@ModelAttribute final AdvertisementDemandStatus advertisementDemandStatus,
            @PathVariable final String financialYear, final Model model) {
        model.addAttribute("financialYear", financialYear);
        return "adtax-demand-status-view";
    }

    @RequestMapping(value = "/demand-status-records-view/", method = POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String viewDemandStatus(@RequestParam("financialyear") final String financialyear, final Model model) {
        List<AdvertisementDemandStatus> resultList;
        final List<AdvertisementDemandStatus> outputList = new ArrayList<>();
        final List<Long> detailList = new ArrayList<>();
        if (financialyear != null) {
            final String[] inputArray = financialyear.split("~");
            boolean val = false;
            if (inputArray[0].contains("0"))// 0 mean success records
                val = true;
            final List<AdvertisementDemandGenerationLog> demandGenLogList = adTaxDemandGenerationLogService
                    .getDemandGenerationLogByInstallmentYear(inputArray[1]);
            for (final AdvertisementDemandGenerationLog generationLog : demandGenLogList) {
                final List<AdvertisementDemandGenerationLogDetail> logDetailList = generationLog.getDetails();
                if (logDetailList != null && !logDetailList.isEmpty()) {
                    resultList = adTaxDemandGenerationLogService.getLogDetailResultList(logDetailList, generationLog, detailList,
                            val);
                    outputList.addAll(resultList);
                }
            }

        }
        return new StringBuilder(DATA)
                .append(toJSON(outputList, AdvertisementDemandStatus.class, AdvertisementDemandStatusAdapter.class))
                .append("}")
                .toString();
    }

}

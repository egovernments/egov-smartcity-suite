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
package org.egov.stms.web.controller.transactions;

import static org.egov.infra.utils.JsonUtils.toJSON;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentDao;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.stms.entity.SewerageDemandGenerationLog;
import org.egov.stms.entity.SewerageDemandGenerationLogDetail;
import org.egov.stms.entity.SewerageTaxBatchDemandGenerate;
import org.egov.stms.transactions.entity.SewerageDemandStatusDetails;
import org.egov.stms.transactions.entity.SewerageDemandStatusResponse;
import org.egov.stms.transactions.service.SewerageBatchDemandGenService;
import org.egov.stms.transactions.service.SewerageDemandGenerationLogService;
import org.egov.stms.web.adapter.SewerageDemandResponseStatusAdapter;
import org.egov.stms.web.adapter.SewerageDemandStatusDetailsAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/transactions")
public class SewerageDemandGenerationController {

    private static final String SEWERAGE_TAX_MANAGEMENT = "Sewerage Tax Management";
    private static final String DEMAND_STATUS_FORM = "sewerage-demand-status";
    private static final String DATA = "{\"data\":";

    @Autowired
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private InstallmentDao installmentDao;

    @Autowired
    private SewerageBatchDemandGenService sewerageBatchDemandGenService;

    @Autowired
    private SewerageDemandGenerationLogService sewerageDemandGenerationLogService;

    @ModelAttribute("financialYear")
    public List<Installment> financialyear(final Installment installment) throws ParseException {

        final List<Installment> installmentList = installmentDao.fetchPreviousInstallmentsInDescendingOrderByModuleAndDate(
                moduleService.getModuleByName(SEWERAGE_TAX_MANAGEMENT), new Date(), 1);

        if (installmentList!=null && !installmentList.isEmpty())
            return installmentDao.fetchNextInstallmentsByModuleAndDate(
                    moduleService.getModuleByName(SEWERAGE_TAX_MANAGEMENT),
                    installmentList.get(0).getFromDate());
        return installmentList;
    }

    @RequestMapping(value = "/generatedemand", method = GET)
    public String search(final Model model, @ModelAttribute final SewerageDemandStatusDetails sewerageDemandStatusDetails) {

        return "seweragedemand-generate";
    }

    @RequestMapping(value = "/generatedemand", method = POST)
    public String generateDemand(final Model model, @ModelAttribute final SewerageDemandStatusDetails sewerageDemandStatusDetails,
            final RedirectAttributes redirectAttributes, final BindingResult resultBinder) {

        Installment installment;

        if (sewerageDemandStatusDetails.getFinancialYear() != null)
            installment = installmentDao.getInsatllmentByModuleAndDescription(
                    moduleService.getModuleByName(SEWERAGE_TAX_MANAGEMENT), sewerageDemandStatusDetails.getFinancialYear());
        else {
            return "seweragedemand-generate";
        }

        if (installment != null) {
            final SewerageTaxBatchDemandGenerate sewerageBatchDemandGenerate = new SewerageTaxBatchDemandGenerate();
            sewerageBatchDemandGenerate.setActive(true);
            sewerageBatchDemandGenerate.setInstallment(installment);
            sewerageBatchDemandGenerate.setJobName("Generate Demand For " + installment.getDescription());
            sewerageBatchDemandGenService.createSewerageTaxBatchDemandGenerate(sewerageBatchDemandGenerate);
        }

        redirectAttributes.addFlashAttribute("message", "msg.demand.Scheduled");// CHANGE MESSAGE AS SCHEDULED

        model.addAttribute("sewerageDemandStatusDetails", sewerageDemandStatusDetails);
        return "generate-success";
    }

    @RequestMapping(value = "/seweragedemand-status", method = GET)
    public String viewDemand(@ModelAttribute final SewerageDemandStatusDetails sewerageDemandStatusDetails) {
        return DEMAND_STATUS_FORM;
    }

    @RequestMapping(value = "/seweragedemand-status", method = POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String getDemandGenerationStatus(@ModelAttribute final SewerageDemandStatusDetails sewerageDemandStatusDetails,
            @RequestParam final String financialYear, final HttpServletRequest request) {
        final List<SewerageDemandStatusDetails> resultList = new ArrayList<>();

        if (financialYear != null) {
            final List<SewerageDemandGenerationLog> generationLogList = sewerageDemandGenerationLogService
                    .getDemandGenerationLogListByInstallmentYear(financialYear);

            if (generationLogList != null && !generationLogList.isEmpty()) {
                final SewerageDemandStatusDetails demandStatus = sewerageDemandGenerationLogService
                        .getDemandStatusResult(generationLogList);

                demandStatus.setFinancialYear(financialYear);
                resultList.add(demandStatus);
            }
        }
        return new StringBuilder(DATA)
                .append(toJSON(resultList, SewerageDemandStatusDetails.class, SewerageDemandStatusDetailsAdapter.class))
                .append("}")
                .toString();
    }

    @RequestMapping(value = "/seweragedemand-status-records-view/{financialYear}", method = GET)
    public String viewDemandStatusOfRecords(@ModelAttribute final SewerageDemandStatusDetails sewerageDemandStatusDetails,
            @PathVariable final String financialYear, final Model model) {
        model.addAttribute("financialYear", financialYear);
        return "seweragestatus-view"; 
    }

    @RequestMapping(value = "/seweragedemand-status-records-view/", method = POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String viewDemandStatus(@RequestParam("financialyear") final String financialyear, final Model model) {
        List<SewerageDemandStatusDetails> resultList;
        final List<SewerageDemandStatusDetails> outputList = new ArrayList<>();
        final List<Long> detailList = new ArrayList<>();
        if (financialyear != null) {
            final String[] inputArray = financialyear.split("~");
            boolean val = false;
            if (inputArray[0].contains("0"))// 0 mean success records
                val = true;
            final List<SewerageDemandGenerationLog> demandGenLogList = sewerageDemandGenerationLogService
                    .getDemandGenerationLogListByInstallmentYear(inputArray[1]);

            for (final SewerageDemandGenerationLog generationLog : demandGenLogList) {
                final List<SewerageDemandGenerationLogDetail> logDetailList = generationLog.getDetails();
                if (logDetailList != null && !logDetailList.isEmpty()) {
                    resultList = sewerageDemandGenerationLogService.getLogDetailResultList(logDetailList, generationLog,
                            detailList,
                            val);
                    outputList.addAll(resultList);
                }
            }

        }
        return new StringBuilder(DATA)
                .append(toJSON(outputList, SewerageDemandStatusDetails.class, SewerageDemandStatusDetailsAdapter.class))
                .append("}")
                .toString();
    }

    @RequestMapping(value = "/seweragedemand-batch", method = POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String getDemandGeneration(@ModelAttribute final SewerageDemandStatusResponse sewerageDemandStatusDetails,
            final HttpServletRequest request, @RequestParam final String financialYear, final Model model) {
        final List<SewerageDemandStatusResponse> batchresultList = new ArrayList<>();
        final List<SewerageTaxBatchDemandGenerate> batchList = sewerageBatchDemandGenService.findActiveBatchDemands();
        if (batchList != null)
            for (final SewerageTaxBatchDemandGenerate batch : batchList) {

                final SewerageDemandStatusResponse batchobj = new SewerageDemandStatusResponse();
                batchobj.setJobname(batch.getJobName());
                batchobj.setCreatedDate(batch.getCreatedDate());
                batchobj.setFinancialYear(financialYear);
                batchobj.setStatus("Demand Generation is scheduled and waiting for completion");
                batchresultList.add(batchobj);
            }
        return new StringBuilder(DATA)
                .append(toJSON(batchresultList, SewerageDemandStatusResponse.class, SewerageDemandResponseStatusAdapter.class))
                .append("}")
                .toString();
    }

}

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
package org.egov.adtax.web.controller.hoarding;

import org.egov.adtax.entity.AdvertisementBatchDemandGenerate;
import org.egov.adtax.search.contract.HoardingSearch;
import org.egov.adtax.service.AdvertisementBatchDemandGenService;
import org.egov.adtax.service.AdvertisementRateService;
import org.egov.adtax.utils.constants.AdvertisementTaxConstants;
import org.egov.adtax.web.controller.GenericController;
import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentDao;
import org.egov.infra.admin.master.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/hoarding")
public class GenerateDemandHoardingController extends GenericController {

    @Autowired
    private AdvertisementRateService advertisementRateService;

    @Autowired
    private InstallmentDao installmentDao;
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private AdvertisementBatchDemandGenService advertisementBatchDemandGenService;

    public @ModelAttribute("financialYears") List<Installment> financialyear() {

        return installmentDao.getInsatllmentByModule(moduleService.getModuleByName(AdvertisementTaxConstants.MODULE_NAME));
    }

    @RequestMapping(value = "/generate-search", method = GET)
    public String search(@ModelAttribute final HoardingSearch hoardingSearch) {
        return "hoarding-generate";
    }

    @RequestMapping(value = "generate-search", method = POST)
    public String searchHoarding(@ModelAttribute final HoardingSearch hoardingSearch,
            final RedirectAttributes redirectAttributes, final BindingResult resultBinder) {

        Installment installment = null;

        if (hoardingSearch.getFinancialYear() != null) {
            installment = installmentDao.fetchInstallmentByModuleAndInstallmentNumber(moduleService.getModuleByName(AdvertisementTaxConstants.MODULE_NAME), Integer.valueOf(hoardingSearch.getFinancialYear()));//   cFinancialYearRepository.getOne(Long.valueOf(hoardingSearch.getFinancialYear()));
        } else {
            resultBinder.rejectValue("financialYear", "*");
            return "hoarding-generate";
        }

        if (installment != null) {
            AdvertisementBatchDemandGenerate advBatchDmdGenerate = new AdvertisementBatchDemandGenerate();
            advBatchDmdGenerate.setActive(true);
            advBatchDmdGenerate.setInstallment(installment);
            advBatchDmdGenerate.setJobName("Generate Demand For " + installment.getFinYearRange() + new Date());
            advertisementBatchDemandGenService.createAdvertisementBatchDemandGenerate(advBatchDmdGenerate);
        }

        redirectAttributes.addFlashAttribute("message", "msg.demand.Scheduled");

        return "generateDemand-success";
    }

}

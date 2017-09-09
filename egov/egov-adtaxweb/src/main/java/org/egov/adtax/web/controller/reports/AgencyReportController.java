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

package org.egov.adtax.web.controller.reports;

import com.google.gson.GsonBuilder;
import org.apache.commons.io.IOUtils;
import org.egov.adtax.entity.AdvertisementPermitDetail;
import org.egov.adtax.entity.HoardingCategory;
import org.egov.adtax.search.contract.HoardingDcbReport;
import org.egov.adtax.service.AdvertisementDemandService;
import org.egov.adtax.service.AdvertisementPermitDetailService;
import org.egov.adtax.service.HoardingCategoryService;
import org.egov.adtax.utils.constants.AdvertisementTaxConstants;
import org.egov.adtax.web.controller.GenericController;
import org.egov.infra.config.core.LocalizationSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping(value = "/reports")
public class AgencyReportController extends GenericController {

    @Autowired
    private AdvertisementDemandService advertisementDemandService;
    @Autowired
    private HoardingCategoryService hoardingCategoryService;

    @Autowired
    private AdvertisementPermitDetailService advertisementPermitDetailService;

    @ModelAttribute("advertisementPermitDetail")
    public AdvertisementPermitDetail advertisementPermitDetail() {
        return new AdvertisementPermitDetail();
    }

    @ModelAttribute("hoardingCategories")
    public List<HoardingCategory> hoardingCategories() {
        return hoardingCategoryService.getAllActiveHoardingCategory();
    }

    @RequestMapping(value = "/dcbreport-search", method = GET)
    public String searchAgencyWiseHoardingForm(@ModelAttribute final AdvertisementPermitDetail advertisementPermitDetail) {
        return "report-agencywise";
    }

    @RequestMapping(value = "/getAgencyWiseDcb", method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void agencyWiseViewHoarding(@ModelAttribute final AdvertisementPermitDetail advertisementPermitDetail, @RequestParam String agency,
                                       final HttpServletResponse response) throws IOException {
        IOUtils.write("{ \"data\":" + new GsonBuilder().setDateFormat(LocalizationSettings.datePattern()).create()
                .toJson(advertisementPermitDetailService.getAgencyWiseAdvertisementSearchResult(advertisementPermitDetail))
                + "}", response.getWriter());
    }

    @RequestMapping(value = "/report-view", method = GET)
    public String agencywiseReport(@RequestParam("id") final String id, @RequestParam("category") final String category,
                                   @RequestParam("subcategory") final String subcategory, @RequestParam("zone") final String zone,
                                   @RequestParam("ward") final String ward, @RequestParam("ownerDetail") final String ownerDetail, final Model model) {
        Long categoryid = (category != null) ? Long.parseLong(category) : null;
        Long subcategoryid = (subcategory != null) ? Long.parseLong(subcategory) : null;
        Long zoneid = (zone != null) ? Long.parseLong(zone) : null;
        Long wardid = (ward != null) ? Long.parseLong(ward) : null;
        final List<AdvertisementPermitDetail> advertisementPermitDetail = advertisementPermitDetailService
                .getAdvertisementPermitDetailBySearchParam(Long.parseLong(id), categoryid, subcategoryid, zoneid, wardid, ownerDetail);
        if (advertisementPermitDetail != null && !advertisementPermitDetail.isEmpty())
            model.addAttribute("agency", advertisementPermitDetail.get(0).getAgency().getName());
        else
            model.addAttribute("agency", "");
        model.addAttribute("dcbResult", getAgencyWiseDCBResult(advertisementPermitDetail));
        return "report-agencywise-view";
    }

    private List<HoardingDcbReport> getAgencyWiseDCBResult(final List<AdvertisementPermitDetail> advertisementPermitDetail) {
        List<HoardingDcbReport> hoardingDcbReportResults = new ArrayList<>();

        for (AdvertisementPermitDetail advpermitdetail : advertisementPermitDetail) {
            if (advpermitdetail.getAgency() != null && advpermitdetail.getAdvertisement() != null
                    && advpermitdetail.getAdvertisement().getDemandId() != null) {
                HoardingDcbReport hoardingReport = new HoardingDcbReport();

                final Map<String, BigDecimal> demandWiseFeeDetail = advertisementDemandService
                        .checkPendingAmountByDemand(advpermitdetail);

                hoardingReport.setDemandAmount(demandWiseFeeDetail.get(AdvertisementTaxConstants.TOTAL_DEMAND));
                hoardingReport.setCollectedAmount(demandWiseFeeDetail.get(AdvertisementTaxConstants.TOTALCOLLECTION));
                hoardingReport.setPendingAmount(demandWiseFeeDetail.get(AdvertisementTaxConstants.PENDINGDEMANDAMOUNT));
                hoardingReport.setPenaltyAmount(demandWiseFeeDetail.get(AdvertisementTaxConstants.PENALTYAMOUNT));
                hoardingReport.setAdditionalTaxAmount(demandWiseFeeDetail.get(AdvertisementTaxConstants.ADDITIONALTAXAMOUNT));
                hoardingReport.setApplicationNumber(advpermitdetail.getApplicationNumber());
                hoardingReport.setPermissionNumber(advpermitdetail.getPermissionNumber());
                hoardingReport.setAgencyName(advpermitdetail.getAgency().getName());
                hoardingReport.setCategory(advpermitdetail.getAdvertisement().getCategory().getName());
                hoardingReport.setSubcategory(advpermitdetail.getAdvertisement().getSubCategory().getDescription());
                hoardingReport.setOwnerDetail(advpermitdetail.getOwnerDetail());
                hoardingDcbReportResults.add(hoardingReport);
            }
        }
        return hoardingDcbReportResults;

    }
}

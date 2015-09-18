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
package org.egov.wtms.web.controller.application;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.egov.commons.Installment;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.repository.WaterConnectionDetailsRepository;
import org.egov.wtms.application.service.ConnectionDemandService;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.masters.entity.enums.ConnectionStatus;
import org.egov.wtms.utils.WaterTaxUtils;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/application")
public class EditDemandForDataEntryController {

    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;
    private final WaterConnectionDetailsRepository waterConnectionDetailsRepository;
    private final ConnectionDemandService connectionDemandService;
    @Autowired
    private WaterTaxUtils waterTaxUtils;

    @Autowired
    public EditDemandForDataEntryController(final WaterConnectionDetailsRepository waterConnectionDetailsRepository,
            final ConnectionDemandService connectionDemandService) {
        this.waterConnectionDetailsRepository = waterConnectionDetailsRepository;
        this.connectionDemandService = connectionDemandService;
    }

    @ModelAttribute
    public WaterConnectionDetails getWaterConnectionDetails(@PathVariable final String consumerCode) {
        final WaterConnectionDetails waterConnectionDetails = waterConnectionDetailsService
                .findByConsumerCodeAndConnectionStatus(consumerCode, ConnectionStatus.ACTIVE);
        if (waterConnectionDetails.getDemand() == null){
            waterConnectionDetails.setDemand(new EgDemand());
        }
        List<Installment> allInstallments = new ArrayList<Installment>();
        final DateFormat dateFormat = new SimpleDateFormat(PropertyTaxConstants.DATE_FORMAT_DDMMYYY);
        try {
            allInstallments = waterTaxUtils.getInstallmentListByStartDate(dateFormat.parse("01/04/1963"));
        } catch (final ParseException e) {
            throw new ApplicationRuntimeException("Error while getting all installments from start date", e);
        }
        for (final Map.Entry<String, String> entry : WaterTaxConstants.NON_METERED_DMDRSN_CODE_MAP.entrySet())
            for (final Installment installObj : allInstallments) {
                final EgDemandReason demandReasonObj = connectionDemandService.getDemandReasonByCodeAndInstallment(
                        entry.getKey(), installObj);
                if (demandReasonObj != null)
                    waterConnectionDetails.getDemand().addEgDemandDetails(new EgDemandDetails());
            }
        return waterConnectionDetails;
    }

    @RequestMapping(value = "/editDemand/{consumerCode}", method = GET)
    public String newForm(final Model model, @PathVariable final String consumerCode,
            @ModelAttribute final WaterConnectionDetails waterConnectionDetails, final HttpServletRequest request) {
        final List<EgDemandDetails> demandDetailList = new ArrayList<EgDemandDetails>();
        return loadViewData(model, request, waterConnectionDetails, demandDetailList);

    }

    private String loadViewData(final Model model, final HttpServletRequest request,
            final WaterConnectionDetails waterConnectionDetails, final List<EgDemandDetails> demandDetailList) {

        List<Installment> allInstallments = new ArrayList<Installment>();
        final DateFormat dateFormat = new SimpleDateFormat(PropertyTaxConstants.DATE_FORMAT_DDMMYYY);
        try {
            allInstallments = waterTaxUtils.getInstallmentListByStartDate(dateFormat.parse("01/04/1963"));
        } catch (final ParseException e) {
            throw new ApplicationRuntimeException("Error while getting all installments from start date", e);
        }
        model.addAttribute("allInstallments", allInstallments);
        for (final Map.Entry<String, String> entry : WaterTaxConstants.NON_METERED_DMDRSN_CODE_MAP.entrySet())
            for (final Installment installObj : allInstallments) {
                final EgDemandReason demandReasonObj = connectionDemandService.getDemandReasonByCodeAndInstallment(
                        entry.getKey(), installObj);
                if (demandReasonObj != null) {
                    final EgDemandDetails dmdDtl = createDemandDetailsForDataEntry(null, entry.getKey(), installObj,
                            null, demandReasonObj, waterConnectionDetails.getDemand());
                    demandDetailList.add(dmdDtl);
                }
            }
        model.addAttribute("demandDetailList", demandDetailList);
        final Set<EgDemandDetails> set = new HashSet<EgDemandDetails>(demandDetailList);
        if (waterConnectionDetails.getDemand() == null)
            waterConnectionDetails.setDemand(new EgDemand());
        waterConnectionDetails.getDemand().setEgDemandDetails(set);
        model.addAttribute("waterConnectionDetails", waterConnectionDetails);
        return "editDemandDateEntry-newForm";
    }
/**
 * 
 * @param amount
 * @param demandReason
 * @param installment
 * @param collectedAmount
 * @param demandReasonObj
 * @param demand
 * @return building EgDemandDetails for ALl installment and demandReason combination
 */
    private EgDemandDetails createDemandDetailsForDataEntry(final Double amount, final String demandReason,
            final Installment installment, final BigDecimal collectedAmount, final EgDemandReason demandReasonObj,
            final EgDemand demand) {

        final EgDemandDetails demandDetail = new EgDemandDetails();
        demandDetail.setAmount(BigDecimal.ZERO);
        demandDetail.setAmtCollected(BigDecimal.ZERO);
        demandDetail.setAmtRebate(BigDecimal.ZERO);
        demandDetail.setEgDemandReason(demandReasonObj);
        demandDetail.getEgDemandReason().setEgInstallmentMaster(installment);
        demandDetail.setAmtCollected(BigDecimal.ZERO);
        demandDetail.setCreateDate(new Date());
        demandDetail.setModifiedDate(new Date());

        return demandDetail;
    }

    @RequestMapping(value = "/editDemand/{consumerCode}", method = RequestMethod.POST)
    public String updateMeterEntry(@ModelAttribute WaterConnectionDetails waterConnectionDetails,
            final BindingResult errors, final RedirectAttributes redirectAttrs, final Model model,
            final HttpServletRequest request) {
        if (waterConnectionDetails.getDemand() == null)
            waterConnectionDetails.setDemand(new EgDemand());
        waterConnectionDetails = buildDemandDetails(waterConnectionDetails, waterConnectionDetails.getDemand()
                .getEgDemandDetails());

        waterConnectionDetails = connectionDemandService.updateDemandForNonMeteredConnectionDataEntry(
                waterConnectionDetails, new ArrayList<EgDemandDetails>(waterConnectionDetails.getDemand()
                        .getEgDemandDetails()));
        final WaterConnectionDetails savedWaterConnectionDetails = waterConnectionDetailsRepository
                .save(waterConnectionDetails);
        model.addAttribute("waterConnectionDetails", savedWaterConnectionDetails);
        return "editDemand-dataEntryAck";
    }

    private WaterConnectionDetails buildDemandDetails(final WaterConnectionDetails waterConnectionDetails,
            final Set<EgDemandDetails> unitDetail) {
        final Set<EgDemandDetails> unitSet = new HashSet<EgDemandDetails>();
        for (final EgDemandDetails unitdetail : unitDetail)
            if (unitdetail.getAmount() != null && !"".equals(unitdetail.getAmount())) {

                final Installment istall = waterConnectionDetailsRepository.getInstallmentByDescription(
                        WaterTaxConstants.PROPERTY_MODULE_NAME, unitdetail.getEgDemandReason().getEgInstallmentMaster()
                                .getDescription());
                unitdetail.getEgDemandReason().setEgInstallmentMaster(istall);
                unitdetail.setCreateDate(new Date());
                unitdetail.setEgDemandReason(unitdetail.getEgDemandReason());
                unitdetail.setModifiedDate(new Date());
                unitSet.add(unitdetail);
            }
        waterConnectionDetails.getDemand().getEgDemandDetails().addAll(unitSet);

        return waterConnectionDetails;

    }

}

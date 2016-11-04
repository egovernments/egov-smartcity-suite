package org.egov.works.web.adaptor;

import java.lang.reflect.Type;
import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.egov.infra.utils.DateUtils;
import org.egov.works.reports.entity.ContractorWiseAbstractSearchResult;
import org.egov.works.reports.service.WorkProgressRegisterService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

@Component
public class ContractorWiseAbstractJsonAdaptor implements JsonSerializer<ContractorWiseAbstractSearchResult> {

    @Autowired
    private WorkProgressRegisterService workProgressRegisterService;

    @Override
    public JsonElement serialize(final ContractorWiseAbstractSearchResult searchResult, final Type typeOfSrc,
            final JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();
        if (searchResult != null) {
            if (searchResult.getElectionWard().contains("{"))
                jsonObject.addProperty("ward",
                        searchResult.getElectionWard().replace("{", " ").replace("}", " ").replaceAll("\"", ""));
            else
                jsonObject.addProperty("ward", searchResult.getElectionWard());
            jsonObject.addProperty("contractorName",
                    searchResult.getContractorName() + " " + "-" + " " + searchResult.getContractorCode());
            jsonObject.addProperty("approvedEstimates", searchResult.getApprovedEstimates());
            if (searchResult.getApprovedAmount() != null)
                jsonObject.addProperty("approvedAmount",
                        searchResult.getApprovedAmount().divide(new BigDecimal(10000000)).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                .toString());
            else
                jsonObject.addProperty("approvedAmount", "");

            jsonObject.addProperty("siteNotHandedOverEstimates", searchResult.getSiteNotHandedOverEstimates());
            if (searchResult.getSiteNotHandedOverAmount() != null)
                jsonObject.addProperty("siteNotHandedOverAmount",
                        searchResult.getSiteNotHandedOverAmount().divide(new BigDecimal(10000000))
                                .setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
            else
                jsonObject.addProperty("siteNotHandedOverAmount", "");

            jsonObject.addProperty("notWorkCommencedEstimates", searchResult.getNotWorkCommencedEstimates());
            if (searchResult.getNotWorkCommencedAmount() != null)
                jsonObject.addProperty("notWorkCommencedAmount",
                        searchResult.getNotWorkCommencedAmount().divide(new BigDecimal(10000000))
                                .setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
            else
                jsonObject.addProperty("notWorkCommencedAmount", "");

            if (searchResult.getWorkCommencedEstimates() != null
                    && searchResult.getLagecyWorkCommencedEstimates() != null)
                jsonObject.addProperty("workCommencedEstimates",
                        searchResult.getWorkCommencedEstimates() + searchResult.getLagecyWorkCommencedEstimates());
            else if (searchResult.getWorkCommencedEstimates() != null)
                jsonObject.addProperty("workCommencedEstimates", searchResult.getWorkCommencedEstimates());
            else if (searchResult.getLagecyWorkCommencedEstimates() != null)
                jsonObject.addProperty("workCommencedEstimates", searchResult.getLagecyWorkCommencedEstimates());
            else
                jsonObject.addProperty("workCommencedEstimates", 0);

            if (searchResult.getWorkCommencedAmount() != null && searchResult.getLagecyWorkCommencedAmount() != null)
                jsonObject.addProperty("workCommencedAmount",
                        searchResult.getWorkCommencedAmount().add(searchResult.getLagecyWorkCommencedAmount())
                                .divide(new BigDecimal(10000000))
                                .setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
            else if (searchResult.getWorkCommencedAmount() != null)
                jsonObject.addProperty("workCommencedAmount",
                        searchResult.getWorkCommencedAmount().setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                .divide(new BigDecimal(10000000)));
            else if (searchResult.getLagecyWorkCommencedAmount() != null)
                jsonObject.addProperty("workCommencedAmount",
                        searchResult.getLagecyWorkCommencedAmount().setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                .divide(new BigDecimal(10000000)));
            else
                jsonObject.addProperty("workCommencedAmount",
                        new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());

            jsonObject.addProperty("workCompletedEstimates",
                    searchResult.getWorkCompletedEstimates() != null ? searchResult.getWorkCompletedEstimates() : 0);
            if (searchResult.getWorkCompletedAmount() != null)
                jsonObject.addProperty("workCompletedAmount",
                        searchResult.getWorkCompletedAmount().divide(new BigDecimal(10000000))
                                .setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
            else
                jsonObject.addProperty("workCompletedAmount",
                        new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());

            if (searchResult.getApprovedEstimates() != null && searchResult.getWorkCompletedEstimates() != null)
                jsonObject.addProperty("balanceWorkEstimates", searchResult.getApprovedEstimates().intValue()
                        - searchResult.getWorkCompletedEstimates().intValue());
            else if (searchResult.getApprovedEstimates() != null)
                jsonObject.addProperty("balanceWorkEstimates", searchResult.getApprovedEstimates());

            if (searchResult.getApprovedAmount() != null && searchResult.getWorkCompletedAmount() != null) {
                final BigDecimal balanceWorkAmount = searchResult.getApprovedAmount()
                        .subtract(searchResult.getWorkCompletedAmount());
                jsonObject.addProperty("balanceWorkAmount",
                        balanceWorkAmount.divide(new BigDecimal(10000000)).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
            } else if (searchResult.getApprovedAmount() != null)
                jsonObject.addProperty("balanceWorkAmount",
                        searchResult.getApprovedAmount().divide(new BigDecimal(10000000)).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                .toString());
            else
                jsonObject.addProperty("balanceWorkAmount", "");

            if (searchResult.getWorkCompletedAmount() == null)
                searchResult.setWorkCompletedAmount(new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_EVEN));

            if (searchResult.getApprovedAmount() != null && searchResult.getLiableAmount() != null
                    && searchResult.getWorkCompletedAmount() != null)
                jsonObject
                        .addProperty("liableAmount",
                                searchResult.getApprovedAmount()
                                        .subtract(searchResult.getWorkCompletedAmount()
                                                .add(searchResult.getLiableAmount()))
                                        .divide(new BigDecimal(10000000)).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());

            else
                jsonObject.addProperty("liableAmount", new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());

            jsonObject
                    .addProperty("createdDate",
                            workProgressRegisterService.getReportSchedulerRunDate() != null
                                    ? DateUtils.getFormattedDateWithTimeStamp(
                                            new DateTime(workProgressRegisterService.getReportSchedulerRunDate()))
                                    : StringUtils.EMPTY);
        }
        return jsonObject;
    }

}

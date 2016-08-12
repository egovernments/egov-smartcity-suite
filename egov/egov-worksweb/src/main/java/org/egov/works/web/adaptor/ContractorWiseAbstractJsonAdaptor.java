package org.egov.works.web.adaptor;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import org.egov.works.reports.entity.ContractorWiseAbstractSearchResult;
import org.egov.works.reports.service.WorkProgressRegisterService;
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
        final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        if (searchResult != null) {
            jsonObject.addProperty("ward", searchResult.getElectionWard());
            jsonObject.addProperty("contractorName", searchResult.getContractorName());
            jsonObject.addProperty("contractorCode", searchResult.getContractorCode());
            jsonObject.addProperty("contractorClass", searchResult.getContractorClass());
            jsonObject.addProperty("approvedEstimates", searchResult.getApprovedEstimates());
            if (searchResult.getApprovedAmount() != null)
                jsonObject.addProperty("approvedAmount",
                        searchResult.getApprovedAmount().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
            else
                jsonObject.addProperty("approvedAmount", "");

            
            jsonObject.addProperty("siteNotHandedOverEstimates", searchResult.getSiteNotHandedOverEstimates());
            if (searchResult.getSiteNotHandedOverAmount() != null)
                jsonObject.addProperty("siteNotHandedOverAmount",
                        searchResult.getSiteNotHandedOverAmount().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
            else
                jsonObject.addProperty("siteNotHandedOverAmount", "");
            
            jsonObject.addProperty("notWorkCommencedEstimates", searchResult.getNotWorkCommencedEstimates());
            if (searchResult.getNotWorkCommencedAmount() != null)
                jsonObject.addProperty("notWorkCommencedAmount",
                        searchResult.getNotWorkCommencedAmount().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
            else
                jsonObject.addProperty("notWorkCommencedAmount", "");
            
            jsonObject.addProperty("workCommencedEstimates", searchResult.getWorkCommencedEstimates());
            if (searchResult.getWorkCommencedAmount() != null)
                jsonObject.addProperty("workCommencedAmount",
                        searchResult.getWorkCommencedAmount().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
            else
                jsonObject.addProperty("workCommencedAmount", "");
            
            jsonObject.addProperty("workCompletedEstimates", searchResult.getWorkCompletedEstimates());
            if (searchResult.getWorkCompletedAmount() != null)
                jsonObject.addProperty("workCompletedAmount",
                        searchResult.getWorkCompletedAmount().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
            else
                jsonObject.addProperty("workCompletedAmount", "");
            
            if(searchResult.getApprovedEstimates() != null && searchResult.getBalanceWorkEstimates() != null) {
            jsonObject.addProperty("balanceWorkEstimates",
                    searchResult.getApprovedEstimates().intValue() - searchResult.getBalanceWorkEstimates().intValue());
            } else if(searchResult.getApprovedEstimates() != null) 
                jsonObject.addProperty("balanceWorkEstimates",
                        searchResult.getApprovedEstimates());
            
            if (searchResult.getApprovedAmount() != null && searchResult.getWorkCompletedAmount() != null) {
                final BigDecimal balanceWorkAmount = searchResult.getApprovedAmount()
                        .subtract(searchResult.getWorkCompletedAmount());
                jsonObject.addProperty("balanceWorkAmount",
                        balanceWorkAmount.setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
            } else if (searchResult.getApprovedAmount() != null)
                jsonObject.addProperty("balanceWorkAmount",
                        searchResult.getApprovedAmount().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
            else
                jsonObject.addProperty("balanceWorkAmount", "");
            
            
            if (searchResult.getLiableAmount() != null)
                jsonObject.addProperty("liableAmount",
                        searchResult.getLiableAmount().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
            else
                jsonObject.addProperty("liableAmount", "");
            jsonObject.addProperty("createdDate", formatter.format(workProgressRegisterService.getReportSchedulerRunDate()));
        }
        return jsonObject;
    }

}

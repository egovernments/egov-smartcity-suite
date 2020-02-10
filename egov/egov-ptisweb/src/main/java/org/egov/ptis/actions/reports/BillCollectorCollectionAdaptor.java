package org.egov.ptis.actions.reports;

import static org.egov.ptis.constants.PropertyTaxConstants.DATE_FORMAT_DDMMYYY;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.egov.infra.utils.DateUtils;
import org.egov.infra.web.support.json.adapter.DataTableJsonAdapter;
import org.egov.infra.web.support.ui.DataTable;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.domain.entity.property.CollectionSummary;
import org.egov.ptis.domain.entity.property.CollectionSummaryDetails;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

public class BillCollectorCollectionAdaptor implements DataTableJsonAdapter<CollectionSummary> {

    public BillCollectorCollectionAdaptor() {
    }

    @Autowired
    public BillCollectorCollectionAdaptor(final PropertyTaxUtil propertyTaxUtil) {
    }

    @Override
    public JsonElement serialize(DataTable<CollectionSummary> billCollectorCollectionResponse, Type typeOfSrc,
            JsonSerializationContext context) {
        final List<CollectionSummary> bcCollectionResult = billCollectorCollectionResponse.getData();
        final JsonArray bcCollectionDetails = new JsonArray();
        bcCollectionResult.forEach(bcCollectionResultObj -> {
            final JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("collectionDate",
                    DateUtils.getFormattedDate(bcCollectionResultObj.getReceiptDate(), DATE_FORMAT_DDMMYYY));
            jsonObject.addProperty("name", bcCollectionResultObj.getUser().getName());
            jsonObject.addProperty("ward", bcCollectionResultObj.getWardId().getName());
            getTaxDetails(bcCollectionResultObj, jsonObject);
            bcCollectionDetails.add(jsonObject);
        });
        return enhance(bcCollectionDetails, billCollectorCollectionResponse);
    }

    private void getTaxDetails(final CollectionSummary collectionSummary, JsonObject jsonObject) {

        BigDecimal totalArrear;
        BigDecimal totalCurrent;
        BigDecimal totalPenalty;
        BigDecimal total;

        final Set<CollectionSummaryDetails> collectionList = new LinkedHashSet<>(collectionSummary.getCollectionDetails());
        new LinkedHashMap<>();
        for (final CollectionSummaryDetails collectionData : collectionList) {

            jsonObject.addProperty("arrearPenaltyTax",
                    collectionData.getArrearPenaltyColl() != null ? collectionData.getArrearPenaltyColl() : BigDecimal.ZERO);
            jsonObject.addProperty("arrearPropertyTax",
                    collectionData.getArrearTaxColl() != null ? collectionData.getArrearTaxColl() : BigDecimal.ZERO);
            jsonObject.addProperty("arrearLibTax",
                    collectionData.getArrearLibCessColl() != null ? collectionData.getArrearLibCessColl() : BigDecimal.ZERO);
            totalArrear = (collectionData.getArrearTaxColl() != null ? collectionData.getArrearTaxColl() : BigDecimal.ZERO)
                    .add(collectionData.getArrearLibCessColl() != null ? collectionData.getArrearLibCessColl() : BigDecimal.ZERO);
            jsonObject.addProperty("totalArrearTax", totalArrear);
            jsonObject.addProperty("currentPropertyTax",
                    collectionData.getCurrentTaxColl() != null ? collectionData.getCurrentTaxColl() : BigDecimal.ZERO);
            jsonObject.addProperty("currentLibTax",
                    collectionData.getLibCessColl() != null ? collectionData.getLibCessColl() : BigDecimal.ZERO);
            totalCurrent = (collectionData.getCurrentTaxColl() != null ? collectionData.getCurrentTaxColl() : BigDecimal.ZERO)
                    .add(collectionData.getLibCessColl() != null ? collectionData.getLibCessColl() : BigDecimal.ZERO);
            jsonObject.addProperty("currentTotal", totalCurrent);
            jsonObject.addProperty("currentPenaltyTax",
                    collectionData.getPenaltyColl() != null ? collectionData.getPenaltyColl() : BigDecimal.ZERO);
            totalPenalty = (collectionData.getArrearPenaltyColl() != null ? collectionData.getArrearPenaltyColl()
                    : BigDecimal.ZERO)
                            .add(collectionData.getPenaltyColl() != null ? collectionData.getPenaltyColl() : BigDecimal.ZERO);
            jsonObject.addProperty("totalPenaltyTax", totalPenalty);
            total = totalArrear.add(totalCurrent).add(totalPenalty);
            jsonObject.addProperty("total", total);
        }
    }

}

package org.egov.ptis.actions.reports;

import static org.egov.ptis.constants.PropertyTaxConstants.DATE_FORMAT_DDMMYYY;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.egov.infra.utils.DateUtils;
import org.egov.infra.web.support.json.adapter.DataTableJsonAdapter;
import org.egov.infra.web.support.ui.DataTable;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.CollectionSummary;
import org.egov.ptis.domain.entity.property.CollectionSummaryDetails;
import org.egov.ptis.domain.entity.property.view.CollectionIndexInfo;
import org.egov.ptis.repository.reports.PropertyMVInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

public class BillCollectorCollectionBookAdaptor implements DataTableJsonAdapter<CollectionIndexInfo> {

    PropertyMVInfoRepository propertyMVInfoRepository;

    @Override
    public JsonElement serialize(DataTable<CollectionIndexInfo> billCollectorCollectionBookResponse, Type typeOfSrc,
            JsonSerializationContext context) {
        final List<CollectionIndexInfo> collectionIndexInfo = billCollectorCollectionBookResponse.getData();
        final JsonArray bcCollectionBookDetails = new JsonArray();
        collectionIndexInfo.forEach(collectionIndexInfoObj -> {
            final JsonObject jsonObject = new JsonObject();
            BigDecimal arrearTotal = BigDecimal.ZERO;
            BigDecimal currentTotal = BigDecimal.ZERO;
            jsonObject.addProperty("collectionDate",
                    DateUtils.getFormattedDate(collectionIndexInfoObj.getReceiptDate(), DATE_FORMAT_DDMMYYY));
            jsonObject.addProperty("name", collectionIndexInfoObj.getCreatedBy().getName());
            jsonObject.addProperty("ward", collectionIndexInfoObj.getWardId());
            jsonObject.addProperty("assessmentNo", collectionIndexInfoObj.getConsumerCode());
            jsonObject.addProperty("fromInstallment", collectionIndexInfoObj.getFromInstallment());
            jsonObject.addProperty("toInstallment", collectionIndexInfoObj.getToInstallment());
            jsonObject.addProperty("arrearPropertyTax", collectionIndexInfoObj.getArrearAmount() == null ? BigDecimal.ZERO
                    : collectionIndexInfoObj.getArrearAmount());
            jsonObject.addProperty("currentPropertyTax", collectionIndexInfoObj.getCurrentAmount() == null ? BigDecimal.ZERO
                    : collectionIndexInfoObj.getCurrentAmount());
            jsonObject.addProperty("arrearEduTax", collectionIndexInfoObj.getArrearEduTax() == null ? BigDecimal.ZERO
                    : collectionIndexInfoObj.getArrearEduTax());
            jsonObject.addProperty("currentEduTax", collectionIndexInfoObj.getCurrentEduTax() == null ? BigDecimal.ZERO
                    : collectionIndexInfoObj.getCurrentEduTax());
            jsonObject.addProperty("arrearLibCess", collectionIndexInfoObj.getArrearLibCess() == null ? BigDecimal.ZERO
                    : collectionIndexInfoObj.getArrearLibCess());
            jsonObject.addProperty("currentLibCess", collectionIndexInfoObj.getCurrentLibCes() == null ? BigDecimal.ZERO
                    : collectionIndexInfoObj.getCurrentLibCes());
            arrearTotal = (collectionIndexInfoObj.getArrearAmount() == null ? BigDecimal.ZERO
                    : collectionIndexInfoObj.getArrearAmount())
                            .add(collectionIndexInfoObj.getArrearEduTax() == null ? BigDecimal.ZERO
                                    : collectionIndexInfoObj.getArrearEduTax())
                            .add(collectionIndexInfoObj.getArrearLibCess() == null ? BigDecimal.ZERO
                                    : collectionIndexInfoObj.getArrearLibCess());
            jsonObject.addProperty("arrearTotal", arrearTotal);
            currentTotal = (collectionIndexInfoObj.getCurrentAmount() == null ? BigDecimal.ZERO
                    : collectionIndexInfoObj.getCurrentAmount())
                            .add(collectionIndexInfoObj.getCurrentEduTax() == null ? BigDecimal.ZERO
                                    : collectionIndexInfoObj.getCurrentEduTax())
                                            .add(collectionIndexInfoObj.getCurrentLibCes() == null ? BigDecimal.ZERO
                                                    : collectionIndexInfoObj.getCurrentLibCes());
            jsonObject.addProperty("currentTotal", currentTotal);
            bcCollectionBookDetails.add(jsonObject);
        });
        return enhance(bcCollectionBookDetails, billCollectorCollectionBookResponse);
    }

}

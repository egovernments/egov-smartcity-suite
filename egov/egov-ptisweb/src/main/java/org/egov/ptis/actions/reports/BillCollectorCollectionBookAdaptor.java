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
package org.egov.ptis.actions.reports;

import static org.egov.ptis.constants.PropertyTaxConstants.DATE_FORMAT_DDMMYYY;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.List;

import org.egov.infra.utils.DateUtils;
import org.egov.infra.web.support.json.adapter.DataTableJsonAdapter;
import org.egov.infra.web.support.ui.DataTable;
import org.egov.ptis.domain.entity.property.view.CollectionIndexInfo;
import org.egov.ptis.domain.service.report.BillCollectorBookReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

@Component
public class BillCollectorCollectionBookAdaptor implements DataTableJsonAdapter<CollectionIndexInfo> {

    private static BillCollectorBookReportService billCollectorBookReportService;

    public BillCollectorCollectionBookAdaptor() {
    }

    @Autowired
    public BillCollectorCollectionBookAdaptor(final BillCollectorBookReportService billCollectorBookReportService) {
        BillCollectorCollectionBookAdaptor.billCollectorBookReportService = billCollectorBookReportService;
    }

    @Override
    public JsonElement serialize(DataTable<CollectionIndexInfo> billCollectorCollectionBookResponse, Type typeOfSrc,
            JsonSerializationContext context) {
        final List<CollectionIndexInfo> collectionIndexInfo = billCollectorCollectionBookResponse.getData();
        final JsonArray bcCollectionBookDetails = new JsonArray();
        collectionIndexInfo.forEach(collectionIndexInfoObj -> {
            final JsonObject jsonObject = new JsonObject();
            BigDecimal arrearTotal = BigDecimal.ZERO;
            BigDecimal currentTotal = BigDecimal.ZERO;
            BigDecimal arrearAmount = BigDecimal.ZERO;
            BigDecimal currentAmount = BigDecimal.ZERO;
            jsonObject.addProperty("collectionDate",
                    DateUtils.getFormattedDate(collectionIndexInfoObj.getReceiptDate(), DATE_FORMAT_DDMMYYY));
            jsonObject.addProperty("name", collectionIndexInfoObj.getCreatedBy().getName());
            jsonObject.addProperty("ward", collectionIndexInfoObj.getWardId());
            jsonObject.addProperty("assessmentNo", collectionIndexInfoObj.getConsumerCode());
            jsonObject.addProperty("fromInstallment", collectionIndexInfoObj.getFromInstallment());
            if (billCollectorBookReportService.getbalanceAmount(collectionIndexInfoObj.getId()))
                jsonObject.addProperty("toInstallment", collectionIndexInfoObj.getToInstallment() + "[P]");
            else
                jsonObject.addProperty("toInstallment", collectionIndexInfoObj.getToInstallment());
            if (collectionIndexInfoObj.getArrearEduTax() != null)
                arrearAmount = (collectionIndexInfoObj.getArrearAmount() == null ? BigDecimal.ZERO
                        : collectionIndexInfoObj.getArrearAmount())
                                .subtract(collectionIndexInfoObj.getArrearEduTax() == null ? BigDecimal.ZERO
                                        : collectionIndexInfoObj.getArrearEduTax());
            jsonObject.addProperty("arrearPropertyTax", arrearAmount);
            currentAmount = (collectionIndexInfoObj.getCurrentAmount() == null ? BigDecimal.ZERO
                    : collectionIndexInfoObj.getCurrentAmount())
                            .subtract(collectionIndexInfoObj.getCurrentEduTax() == null ? BigDecimal.ZERO
                                    : collectionIndexInfoObj.getCurrentEduTax());
            jsonObject.addProperty("currentPropertyTax", currentAmount);
            jsonObject.addProperty("arrearEduTax", collectionIndexInfoObj.getArrearEduTax() == null ? BigDecimal.ZERO
                    : collectionIndexInfoObj.getArrearEduTax());
            jsonObject.addProperty("currentEduTax", collectionIndexInfoObj.getCurrentEduTax() == null ? BigDecimal.ZERO
                    : collectionIndexInfoObj.getCurrentEduTax());
            jsonObject.addProperty("arrearLibCess", collectionIndexInfoObj.getArrearLibCess() == null ? BigDecimal.ZERO
                    : collectionIndexInfoObj.getArrearLibCess());
            jsonObject.addProperty("currentLibCess", collectionIndexInfoObj.getCurrentLibCes() == null ? BigDecimal.ZERO
                    : collectionIndexInfoObj.getCurrentLibCes());
            arrearTotal = arrearAmount.add(collectionIndexInfoObj.getArrearEduTax() == null ? BigDecimal.ZERO
                    : collectionIndexInfoObj.getArrearEduTax())
                    .add(collectionIndexInfoObj.getArrearLibCess() == null ? BigDecimal.ZERO
                            : collectionIndexInfoObj.getArrearLibCess());
            jsonObject.addProperty("arrearTotal", arrearTotal);
            currentTotal = currentAmount.add(collectionIndexInfoObj.getCurrentEduTax() == null ? BigDecimal.ZERO
                    : collectionIndexInfoObj.getCurrentEduTax())
                    .add(collectionIndexInfoObj.getCurrentLibCes() == null ? BigDecimal.ZERO
                            : collectionIndexInfoObj.getCurrentLibCes());
            jsonObject.addProperty("currentTotal", currentTotal);
            bcCollectionBookDetails.add(jsonObject);
        });
        return enhance(bcCollectionBookDetails, billCollectorCollectionBookResponse);
    }

}

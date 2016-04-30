/* eGov suite of products aim to improve the internal efficiency,transparency,
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
package org.egov.tl.web.adaptor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.egov.tl.entity.FeeMatrixDetail;
import org.egov.tl.entity.FeeType;
import org.egov.tl.entity.LicenseSubCategoryDetails;

import java.lang.reflect.Type;

public class FeeMatrixAdaptor implements JsonSerializer<FeeMatrixDetail> {
    @Override
    public JsonElement serialize(final FeeMatrixDetail feeMatrixDetail, final Type type, final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        if (feeMatrixDetail != null) {
            jsonObject.addProperty("id", feeMatrixDetail.getId());
            jsonObject.addProperty("licenseCategory", feeMatrixDetail.getFeeMatrix().getLicenseCategory() == null ? ""
                    : feeMatrixDetail.getFeeMatrix().getLicenseCategory().getName());
            jsonObject.addProperty("subCategory", feeMatrixDetail.getFeeMatrix().getSubCategory() == null ? ""
                    : feeMatrixDetail.getFeeMatrix().getSubCategory().getName());
            jsonObject.addProperty("uom", feeMatrixDetail.getFeeMatrix().getUnitOfMeasurement() == null ? ""
                    : feeMatrixDetail.getFeeMatrix().getUnitOfMeasurement().getName());
            final FeeType feeType = feeMatrixDetail.getFeeMatrix().getFeeType();
            for (final LicenseSubCategoryDetails licenseSubCategoryDetails : feeMatrixDetail.getFeeMatrix().getSubCategory()
                    .getLicenseSubCategoryDetails())
                if (feeType.getCode().equals(licenseSubCategoryDetails.getFeeType().getCode())) {
                    jsonObject.addProperty("rateType", licenseSubCategoryDetails.getRateType() == null ? ""
                            : licenseSubCategoryDetails.getRateType().toString());
                    break;
                }
            jsonObject.addProperty("financialYear", feeMatrixDetail.getFeeMatrix().getFinancialYear() == null ? ""
                    : feeMatrixDetail.getFeeMatrix().getFinancialYear().getFinYearRange());
            jsonObject.addProperty("uomfrom",
                    feeMatrixDetail.getUomFrom() == null ? "" : feeMatrixDetail.getUomFrom().toString());
            jsonObject.addProperty("uomto", feeMatrixDetail.getUomTo() == null ? "" : feeMatrixDetail.getUomTo().toString());
            jsonObject.addProperty("rate", feeMatrixDetail.getAmount());

        }
        return jsonObject;
    }
}
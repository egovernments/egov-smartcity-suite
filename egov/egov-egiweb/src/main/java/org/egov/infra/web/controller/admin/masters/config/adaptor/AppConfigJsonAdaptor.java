
/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.infra.web.controller.admin.masters.config.adaptor;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.egov.infra.admin.master.entity.AppConfig;
import org.egov.infra.admin.master.entity.AppConfigValues;

import java.lang.reflect.Type;

import static org.egov.infra.utils.DateUtils.getDefaultFormattedDate;

public class AppConfigJsonAdaptor implements JsonSerializer<AppConfig> {

    @Override
    public JsonElement serialize(final AppConfig appConfig, final Type type, final JsonSerializationContext jsc) {
        JsonObject appConfigJSON = new JsonObject();
        appConfigJSON.addProperty("keyName", appConfig.getKeyName());
        appConfigJSON.addProperty("description", appConfig.getDescription());
        appConfigJSON.addProperty("module", appConfig.getModule().getName());
        appConfigJSON.addProperty("id", appConfig.getId());
        JsonArray  configValues = new JsonArray();
        for(AppConfigValues confValue : appConfig.getConfValues()) {
        	JsonObject configValueJSON = new JsonObject();
            configValueJSON.addProperty("Effective Date", getDefaultFormattedDate(confValue.getEffectiveFrom()));
            configValueJSON.addProperty("Value", confValue.getValue());
            configValues.add(configValueJSON);
        }
        appConfigJSON.add("values", configValues);
        return appConfigJSON;
    }

}

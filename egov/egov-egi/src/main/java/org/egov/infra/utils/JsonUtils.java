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

package org.egov.infra.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSerializer;
import org.egov.infra.exception.ApplicationRuntimeException;

import java.util.Collection;

import static org.egov.infra.web.support.json.adapter.HibernateProxyTypeAdapter.FACTORY;

public final class JsonUtils {

    private JsonUtils() {
        //only static methods
    }

    public static <T> String toJSON(Collection<T> objects, Class<? extends T> objectClazz, JsonSerializer<T> adapter) {
        return new GsonBuilder().
                registerTypeAdapterFactory(FACTORY).
                registerTypeAdapter(objectClazz, adapter).create().toJson(objects);
    }

    public static <T> String toJSON(Collection<T> objects, Class<? extends T> objectClazz, Class<? extends JsonSerializer<T>> adapterClazz) {
        try {
            return toJSON(objects, objectClazz, adapterClazz.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ApplicationRuntimeException("Could not convert object list to json string", e);
        }
    }

    public static <T> String toJSON(T object, JsonSerializer<T> adapter) {
        return new GsonBuilder().
                registerTypeAdapterFactory(FACTORY).
                registerTypeAdapter(object.getClass(), adapter).create().toJson(object);
    }

    public static <T> String toJSON(T object, Class<? extends JsonSerializer<T>> adapterClazz) {
        try {
            return toJSON(object, adapterClazz.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ApplicationRuntimeException("Could not convert object to json string", e);
        }
    }

    public static <T> T fromJSON(String json, Class<T> clazz) {
        return new Gson().fromJson(json, clazz);
    }

    public static String toJSON(Object object) {
        return new Gson().toJson(object);
    }
}

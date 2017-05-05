/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2017>  eGovernments Foundation
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
package org.egov.services.wrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;

import com.netflix.zuul.http.HttpServletRequestWrapper;
import com.netflix.zuul.http.ServletInputStreamWrapper;

public class CustomRequestWrapper extends HttpServletRequestWrapper {

    private String payload;
    
    private HttpServletRequest request;

    private Map<String, String[]> parameterMap;


    public CustomRequestWrapper(final HttpServletRequest request) {
        super(request);
        convertInputStreamToString(request);
        this.request = request;
    }

    private void convertInputStreamToString(final HttpServletRequest request) {
        try {
            payload = IOUtils.toString(request.getInputStream());
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(final String payload) {
        this.payload = payload;
    }

    @Override
    public int getContentLength() {
        return payload.length();
    }

    @Override
    public long getContentLengthLong() {
        return payload.length();
    }

    @Override
    public ServletInputStream getInputStream() {
        return new ServletInputStreamWrapper(payload.getBytes());
    }
    
    public void addParameter(String name, String value) {
        if (parameterMap == null) {
                parameterMap = new HashMap<String, String[]>();
                parameterMap.putAll(request.getParameterMap());
        }
        String[] values = parameterMap.get(name);
        if (values == null) {
                values = new String[0];
        }
        List<String> list = new ArrayList<String>(values.length + 1);
        //list.addAll(Arrays.asList(values));
        list.add(value);
        parameterMap.put(name, list.toArray(new String[0]));
    }
    
    @Override
    public String getParameter(String name) {
            if (parameterMap == null) {
                    return request.getParameter(name);
            }

            String[] strings = parameterMap.get(name);
            if (strings != null) {
                    return strings[0];
            }
            return null;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
            if (parameterMap == null) {
                    return request.getParameterMap();
            }

            return Collections.unmodifiableMap(parameterMap);
    }

    @Override
    public Enumeration<String> getParameterNames() {
            if (parameterMap == null) {
                    return request.getParameterNames();
            }

            return Collections.enumeration(parameterMap.keySet());
    }

    @Override
    public String[] getParameterValues(String name) {
            if (parameterMap == null) {
                    return request.getParameterValues(name);
            }
            return parameterMap.get(name);
    }

}

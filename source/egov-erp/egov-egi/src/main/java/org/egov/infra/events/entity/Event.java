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
package org.egov.infra.events.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class Event implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private String module;
    private String eventCode;
    private Map<String, String> params;
    private Date dateRaised;
    private String responseType;
    private String responseTemplate;

    public Event(final String module, final String eventCode, final Map<String, String> params) {
        super();
        this.module = module;
        this.eventCode = eventCode;
        this.params = params;
    }

    public void addParam(final String key, final String value) {
        getParams().put(key, value);
    }

    public void addParams(final Map<String, String> params) {

        getParams().putAll(params);
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public Date getDateRaised() {
        return dateRaised;
    }

    public void setDateRaised(final Date dateRaised) {
        this.dateRaised = dateRaised;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(final Map<String, String> params) {
        this.params = params;
    }

    public String getModule() {
        return module;
    }

    public void setModule(final String module) {
        this.module = module;
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(final String eventCode) {
        this.eventCode = eventCode;
    }

    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(final String responseType) {
        this.responseType = responseType;
    }

    public String getResponseTemplate() {
        return responseTemplate;
    }

    public void setResponseTemplate(final String responseTemplate) {
        this.responseTemplate = responseTemplate;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Event [id=").append(id).append(", module=").append(module).append(", eventCode=")
        .append(eventCode).append(", params=").append(params).append(", dateRaised=").append(dateRaised)
        .append(", responseType=").append(responseType).append(", responseTemplate=").append(responseTemplate);
        return builder.toString();
    }

}
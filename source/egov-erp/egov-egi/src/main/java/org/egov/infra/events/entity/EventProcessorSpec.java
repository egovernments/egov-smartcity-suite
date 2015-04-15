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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.egov.infra.persistence.entity.AbstractPersistable;

@Entity
@Table(name = "eg_event_processor_spec")
@NamedQuery(name = "event_specByModuleAndCode", query = "select EP from EventProcessorSpec EP where EP.module=:module and EP.eventCode=:eventCode")
public class EventProcessorSpec extends AbstractPersistable<Long> {

    private static final long serialVersionUID = 5661966690272607421L;

    @Column(name = "module")
    private String module;

    @Column(name = "event_code")
    private String eventCode;

    @Column(name = "response_template")
    private String responseTemplate;

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

    public String getResponseTemplate() {
        return responseTemplate;
    }

    public void setResponseTemplate(final String responseTemplate) {
        this.responseTemplate = responseTemplate;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("EventProcessorSpec [id=").append(getId()).append(", module=").append(module)
        .append(", eventCode=").append(eventCode).append(", responseTemplate=").append(responseTemplate)
        .append("]");
        return builder.toString();
    }

}

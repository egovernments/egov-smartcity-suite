/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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

package org.egov.infra.workflow.entity.contract;

import com.google.gson.GsonBuilder;
import org.joda.time.LocalDate;

import java.util.Date;

public class StateInfoBuilder {
    private String refNo;
    private String refDate;
    private String type;
    private String citizenName;
    private String citizenPhoneNo;
    private String citizenAddress;
    private String status;
    private String resolutionDate;
    private String locationWard;
    private String senderName;
    private String senderPhoneNo;
    private String itemDetails;

    public StateInfoBuilder refNo(String refNo) {
        this.refNo = refNo;
        return this;
    }

    public StateInfoBuilder refDate(Date refDate) {
        this.refDate = new LocalDate(refDate).toString("dd-MM-yyyy");
        return this;
    }

    public StateInfoBuilder type(String type) {
        this.type = type;
        return this;
    }

    public StateInfoBuilder citizenName(String citizenName) {
        this.citizenName = citizenName;
        return this;
    }

    public StateInfoBuilder citizenPhoneNo(String citizenPhoneNo) {
        this.citizenPhoneNo = citizenPhoneNo;
        return this;
    }

    public StateInfoBuilder citizenAddress(String citizenAddress) {
        this.citizenAddress = citizenAddress;
        return this;
    }

    public StateInfoBuilder status(String status) {
        this.status = status;
        return this;
    }

    public StateInfoBuilder resolutionDate(Date resolutionDate) {
        this.resolutionDate = new LocalDate(resolutionDate).toString("dd-MM-yyyy");
        return this;
    }

    public StateInfoBuilder locationWard(String locationWard) {
        this.locationWard = locationWard;
        return this;
    }

    public StateInfoBuilder senderName(String senderName) {
        this.senderName = senderName;
        return this;
    }

    public StateInfoBuilder senderPhoneNo(String senderPhoneNo) {
        this.senderPhoneNo = senderPhoneNo;
        return this;
    }

    public StateInfoBuilder itemDetails(String itemDetails) {
        this.itemDetails = itemDetails;
        return this;
    }

    public String toJson() {
        return new GsonBuilder().create().toJson(this);
    }
}

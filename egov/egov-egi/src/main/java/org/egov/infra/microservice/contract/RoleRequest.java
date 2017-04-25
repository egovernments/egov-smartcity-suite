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
package org.egov.infra.microservice.contract;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class RoleRequest {

    private Long id;

    private String name;

    private String code;

    private String description;

    private Long createdBy;

    @JsonFormat(pattern = "MM/dd/yyyy")
    private Date createdDate;

    private Long lastModifiedBy;
    @JsonFormat(pattern = "MM/dd/yyyy")

    private Date lastModifiedDate;

    public RoleRequest() {
    }

    public RoleRequest(final org.egov.infra.admin.master.entity.Role roleEntity) {
        id = roleEntity.getId();
        name = roleEntity.getName();
        code = roleEntity.getName();
        description = roleEntity.getDescription();
        createdBy = roleEntity.getCreatedBy() == null ? 0L : roleEntity.getCreatedBy().getId();
        createdDate = roleEntity.getCreatedDate();
        lastModifiedBy = roleEntity.getLastModifiedBy() == null ? 0L : roleEntity.getLastModifiedBy().getId();
        lastModifiedDate = roleEntity.getLastModifiedDate();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public Long getLastModifiedBy() {
        return lastModifiedBy;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

}
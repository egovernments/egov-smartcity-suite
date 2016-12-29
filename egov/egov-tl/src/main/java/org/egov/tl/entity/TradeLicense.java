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

package org.egov.tl.entity;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.Entity;
import javax.persistence.Table;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.egov.infra.utils.DateUtils.toDefaultDateFormat;

@Entity
@Table(name = "egtl_trade_license")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class TradeLicense extends License {

    @Override
    public String getStateDetails() {
        StringBuilder details = new StringBuilder();
        if (isNotBlank(getLicenseNumber()))
            details.append("Trade License Number ").append(getLicenseNumber()).append(" and ");
        details.append("App No. ").append(applicationNumber).append(" dated ").append(toDefaultDateFormat(applicationDate));
        if (isNotBlank(this.getState().getComments()))
            details.append("<br/> Remarks : ").append(this.getState().getComments());
        return details.toString();
    }

    @Override
    public String myLinkId() {
        if ("Closure License".equals(this.getState().getNatureOfTask()))
            return "/tl/viewtradelicense/viewTradeLicense-closure.action?model.id=" + this.id;
        else
            return "/tl/newtradelicense/newTradeLicense-showForApproval.action?model.id=" + this.id;
    }
}

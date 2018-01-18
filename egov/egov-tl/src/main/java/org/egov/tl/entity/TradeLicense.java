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

package org.egov.tl.entity;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.egov.infra.utils.DateUtils.toDefaultDateFormat;
import static org.egov.tl.utils.Constants.CLOSURE_NATUREOFTASK;

@Entity
@Table(name = "egtl_trade_license")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class TradeLicense extends License {

    private static final long serialVersionUID = 986289058758315223L;
    private static final String CLOSURE_APPROVAL_URL = "/tl/viewtradelicense/viewTradeLicense-closure.action?model.id=%d";
    private static final String NEW_RENEW_APPROVAL_URL = "/tl/newtradelicense/newTradeLicense-showForApproval.action?model.id=%d";

    @Transient
    private List<Integer> financialyear = new ArrayList<>();
    @Transient
    private List<Integer> legacyInstallmentwiseFees = new ArrayList<>();
    @Transient
    private List<Boolean> legacyFeePayStatus = new ArrayList<>();
    @Transient
    private transient MultipartFile[] files;

    @Override
    public String getStateDetails() {
        StringBuilder details = new StringBuilder();
        if (isNotBlank(getLicenseNumber()))
            details.append("License No. ").append(getLicenseNumber()).append(" and ");
        details.append("Application No. ").append(applicationNumber).append(" dated ")
                .append(toDefaultDateFormat(applicationDate));
        if (isNotBlank(getState().getComments()))
            details.append("<br/>Remarks : ").append(getState().getComments());
        return details.toString();
    }

    @Override
    public String myLinkId() {
        if (CLOSURE_NATUREOFTASK.equals(getState().getNatureOfTask()))
            return format(CLOSURE_APPROVAL_URL, id);
        else
            return format(NEW_RENEW_APPROVAL_URL, id);
    }

    public List<Integer> getFinancialyear() {
        return financialyear;
    }

    public void setFinancialyear(List<Integer> financialyear) {
        this.financialyear = financialyear;
    }

    public List<Integer> getLegacyInstallmentwiseFees() {
        return legacyInstallmentwiseFees;
    }

    public void setLegacyInstallmentwiseFees(List<Integer> legacyInstallmentwiseFees) {
        this.legacyInstallmentwiseFees = legacyInstallmentwiseFees;
    }

    public List<Boolean> getLegacyFeePayStatus() {
        return legacyFeePayStatus;
    }

    public void setLegacyFeePayStatus(List<Boolean> legacyFeePayStatus) {
        this.legacyFeePayStatus = legacyFeePayStatus;
    }

    public MultipartFile[] getFiles() {
        return files;
    }

    public void setFiles(MultipartFile[] files) {
        this.files = files;
    }

}

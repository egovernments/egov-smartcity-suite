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

package org.egov.works.elasticsearch.model;

import java.util.List;

/**
 * @author venki
 */

public class WorksIndexsRequest {

    private String typeofwork;

    private String distname;

    private String ulbname;

    private String ulbcode;

    private List<String> ulbcodes;

    private String reportType;

    private Integer lineestimatedetailid;

    private List<Integer> lineestimatedetailids;

    public String getTypeofwork() {
        return typeofwork;
    }

    public void setTypeofwork(final String typeofwork) {
        this.typeofwork = typeofwork;
    }

    public String getDistname() {
        return distname;
    }

    public void setDistname(final String distname) {
        this.distname = distname;
    }

    public String getUlbcode() {
        return ulbcode;
    }

    public void setUlbcode(final String ulbcode) {
        this.ulbcode = ulbcode;
    }

    public String getUlbname() {
        return ulbname;
    }

    public void setUlbname(final String ulbname) {
        this.ulbname = ulbname;
    }

    public List<String> getUlbcodes() {
        return ulbcodes;
    }

    public void setUlbcodes(final List<String> ulbcodes) {
        this.ulbcodes = ulbcodes;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(final String reportType) {
        this.reportType = reportType;
    }

    public Integer getLineestimatedetailid() {
        return lineestimatedetailid;
    }

    public void setLineestimatedetailid(final Integer lineestimatedetailid) {
        this.lineestimatedetailid = lineestimatedetailid;
    }

    public List<Integer> getLineestimatedetailids() {
        return lineestimatedetailids;
    }

    public void setLineestimatedetailids(final List<Integer> lineestimatedetailids) {
        this.lineestimatedetailids = lineestimatedetailids;
    }

}

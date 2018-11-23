/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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

package org.egov.infra.web.contract.response;

public class HomePageResponse {

    private String menu;
    private String userName;
    private String appVersion;
    private String appBuildNo;
    private String appCoreBuildNo;
    private String issueReportingURL;
    private boolean requiredPasswordReset;
    private boolean warnPasswordExpiry;
    private int daysToPasswordExpiry;

    public String getMenu() {
        return menu;
    }

    public void setMenu(final String menu) {
        this.menu = menu;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(final String userName) {
        this.userName = userName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(final String appVersion) {
        this.appVersion = appVersion;
    }

    public String getAppBuildNo() {
        return appBuildNo;
    }

    public void setAppBuildNo(final String appBuildNo) {
        this.appBuildNo = appBuildNo;
    }

    public String getAppCoreBuildNo() {
        return appCoreBuildNo;
    }

    public void setAppCoreBuildNo(final String appCoreBuildNo) {
        this.appCoreBuildNo = appCoreBuildNo;
    }

    public String getIssueReportingURL() {
        return issueReportingURL;
    }

    public void setIssueReportingURL(final String issueReportingURL) {
        this.issueReportingURL = issueReportingURL;
    }

    public boolean isRequiredPasswordReset() {
        return requiredPasswordReset;
    }

    public void setRequiredPasswordReset(final boolean requiredPasswordReset) {
        this.requiredPasswordReset = requiredPasswordReset;
    }

    public boolean isWarnPasswordExpiry() {
        return warnPasswordExpiry;
    }

    public void setWarnPasswordExpiry(final boolean warnPasswordExpiry) {
        this.warnPasswordExpiry = warnPasswordExpiry;
    }

    public int getDaysToPasswordExpiry() {
        return daysToPasswordExpiry;
    }

    public void setDaysToPasswordExpiry(final int daysToPasswordExpiry) {
        this.daysToPasswordExpiry = daysToPasswordExpiry;
    }
}

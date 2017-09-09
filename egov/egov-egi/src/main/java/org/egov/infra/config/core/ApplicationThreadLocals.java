/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.infra.config.core;

public class ApplicationThreadLocals {

    private static ThreadLocal<String> domainName = new ThreadLocal<>();
    private static ThreadLocal<Long> userId = new ThreadLocal<>();
    private static ThreadLocal<String> tenantID = new ThreadLocal<>();
    private static ThreadLocal<String> cityCode = new ThreadLocal<>();
    private static ThreadLocal<String> cityName = new ThreadLocal<>();
    private static ThreadLocal<String> municipalityName = new ThreadLocal<>();
    private static ThreadLocal<String> domainURL = new ThreadLocal<>();

    private ApplicationThreadLocals() {
        //Not to be initialized
    }

    public static String getCityName() {
        return cityName.get();
    }

    public static void setCityName(String citiName) {
        cityName.set(citiName);
    }

    public static String getCityCode() {
        return cityCode.get();
    }

    public static void setCityCode(String citiCode) {
        cityCode.set(citiCode);
    }

    public static String getTenantID() {
        return tenantID.get();
    }

    public static void setTenantID(String tenantJNDI) {
        tenantID.set(tenantJNDI);
    }

    public static String getDomainName() {
        return domainName.get();
    }

    public static void setDomainName(String domName) {
        domainName.set(domName);
    }

    public static Long getUserId() {
        return userId.get();
    }

    public static void setUserId(Long userid) {
        userId.set(userid);
    }

    public static String getMunicipalityName() {
        return municipalityName.get();
    }

    public static void setMunicipalityName(String cityMunicipalityName) {
        municipalityName.set(cityMunicipalityName);
    }

    public static String getDomainURL() {
        return domainURL.get();
    }

    public static void setDomainURL(String domURL) {
        domainURL.set(domURL);
    }

    public static void clearValues() {
        domainName.remove();
        userId.remove();
        tenantID.remove();
        cityCode.remove();
        cityName.remove();
        municipalityName.remove();
        domainURL.remove();

    }
}

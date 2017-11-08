/*
 * eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) <2017>  eGovernments Foundation
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
 * 	Further, all user interfaces, including but not limited to citizen facing interfaces,
 *         Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *         derived works should carry eGovernments Foundation logo on the top right corner.
 *
 * 	For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 * 	For any further queries on attribution, including queries on brand guidelines,
 *         please contact contact@egovernments.org
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

package org.egov.infra.utils;

public final class ApplicationConstant {

    public static final String CITY_CODE_KEY = "cityCode";
    public static final String CITY_NAME_KEY = "cityname";
    public static final String CITY_URL_KEY = "cityurl";
    public static final String CITY_LOGO_KEY = "citylogo";
    public static final String CITY_LOCAL_NAME_KEY = "citynamelocal";
    public static final String CITY_CAPTCHA_PRIV_KEY = "siteSecret";
    public static final String CITY_CAPTCHA_PUB_KEY = "siteKey";
    public static final String CITY_LAT_KEY = "citylat";
    public static final String CITY_LNG_KEY = "citylng";
    public static final String CITY_CORP_GRADE_KEY = "cityGrade";
    public static final String CITY_DIST_NAME_KEY = "districtName";
    public static final String CITY_DIST_CODE_KEY = "districtCode";
    public static final String CITY_CORP_NAME_KEY = "citymunicipalityname";
    public static final String CITY_CORP_ADDRESS_KEY = "corpAddress";
    public static final String CITY_CORP_CALLCENTER_NO_KEY = "corpCallCenterNo";
    public static final String CITY_CORP_CONTACT_NO_KEY = "corpContactNo";
    public static final String CITY_CORP_EMAIL_KEY = "corpContactEmail";
    public static final String CITY_CORP_TWITTER_KEY = "corpTwitterLink";
    public static final String CITY_CORP_FB_KEY = "corpFBLink";
    public static final String CITY_CORP_GOOGLE_MAP_KEY = "corpGisLink";
    public static final String CITY_REGION_NAME_KEY = "cityRegion";
    public static final String CITY_GOOGLE_API_KEY = "googleApiKey";

    public static final String CITY_LOGO_URL = "/downloadfile/logo?fileStoreId=%s&moduleName=%s";
    public static final String CITY_LOGO_PATH_KEY = "logopath";
    public static final String CITY_LOGIN_URL = "%s/egi/login/secure";
    public static final String CITY_LOGO_STATIC_URL = "%s/egi%s";

    public static final String CDN_ATTRIB_NAME = "cdn";
    public static final String APP_RELEASE_ATTRIB_NAME = "app_release_no";

    public static final Character Y = 'Y';
    public static final Character N = 'N';

    public static final String ES_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String DEFAULT_TIMEZONE = "IST";
    public static final String ES_DATE_FORMAT_WITHOUT_TS = "yyyy-MM-dd";

    public static final String DEFAULT_CHARACTER_ENCODING = "UTF-8";

    public static final String USERID_KEY = "userid";
    public static final String USERNAME_KEY = "username";
    public static final String CITIZEN_ROLE_NAME = "CITIZEN";
    public static final String ANONYMOUS_USERNAME = "anonymous";
    public static final String TENANTID_KEY = "tenant_id";

    public static final String NA = "N/A";
    public static final String YES = "Yes";
    public static final String NO = "No";
    public static final String UNDERSCORE = "_";
    public static final String HYPHEN = "-";
    public static final String WHITESPACE = " ";
    public static final String SLASH = "/";
    public static final String COLON = ":";

    private ApplicationConstant() {
    }
}

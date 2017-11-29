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
package org.egov.assets.util;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class AssetConstants {

    public static final String alphaNumericwithspecialchar = "[0-9a-zA-Z-&/: ]+";
    public static final int PAGE_SIZE = 30;
    public static final String MODULE_NAME = "Asset Management";
    public static final String MODULE_ID = "12";
    public static final String JOURNALVOUCHER = "Journal Voucher";
    public static final Locale LOCALE = new Locale("en", "IN");
    public static final SimpleDateFormat DDMMYYYYFORMATH = new SimpleDateFormat("dd-MMM-yyyy", LOCALE);
    public static final SimpleDateFormat DDMMYYYYFORMATS = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    public static final SimpleDateFormat YYYYMMDDFORMATH = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    public static final String VOUCHERNAME = "Voucher Name";
    public static final String VOUCHERTYPE = "Voucher Type";
    public static final String CREATEASSET = "createAsset";
    public static final String VIEWASSET = "viewAsset";
    public static final String MODIFYASSET = "modifyAsset";
    
    public static final String ASSETACCCODEPURPOSEID = "ASSET_ACCOUNT_CODE_PURPOSEID";  
    public static final String REVRESACCPURPOSEID = "REVALUATION_RESERVE_ACCOUNT_PURPOSEID";
    public static final String DEPEXPACCPURPOSEID = "DEPRECIATION_EXPENSE_ACCOUNT_PURPOSEID";
    public static final String ACCDEPPURPOSEID = "ACCUMULATED_DEPRECIATION_PURPOSEID";
    
    public static final String ASSET_CATEGORY_CODE_CREATION_MODE = "Asset_category_code_creation_mode";
    

}

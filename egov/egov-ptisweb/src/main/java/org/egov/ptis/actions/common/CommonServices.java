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
package org.egov.ptis.actions.common;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.ptis.constants.PropertyTaxConstants;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.egov.ptis.constants.PropertyTaxConstants.AMENITY_TYPE_FULL;
import static org.egov.ptis.constants.PropertyTaxConstants.AMENITY_TYPE_NIL;
import static org.egov.ptis.constants.PropertyTaxConstants.AMENITY_TYPE_PARTIAL;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE_TYPE_BILL;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE_TYPE_DEMAND_BILL;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE_TYPE_MUTATION_CERTIFICATE;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE_TYPE_SPECIAL_NOTICE;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE_TYPE_ESD;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE_TYPE_OC;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE_TYPE_VRPROCEEDINGS;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE_TYPE_GRPPROCEEDINGS;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE_TYPE_RPPROCEEDINGS;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE_TYPE_EXEMPTION;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE_TYPE_EXEMPTIONPROCEEDINGS;


public class CommonServices {

	public static Map<String, Integer> getWaterMeterRateMstr() {
		Map<String, Integer> waterMeterMap = new HashMap<String, Integer>();
		waterMeterMap.put("WATER_METER", 0);
		waterMeterMap.put("GWR_IMPOSED", 1);
		waterMeterMap.put("GWR_NOT_IMPOSED", 2);
		waterMeterMap.put("WATER_LINE_WITHOUT_METER", 3);
		waterMeterMap.put("WATER_LINE_WITH_METER20", 3);
		return waterMeterMap;
	}

	public static Map<String, String> getWaterMeterMstr() {
		return PropertyTaxConstants.waterRates;
	}

	public static Integer getWaterMeterRate(String mstrCode) {
		Map<String, Integer> waterMeterMstr = getWaterMeterRateMstr();
		return waterMeterMstr.get(mstrCode);
	}

	public static String getWaterMeterDtls(String mstrCode) {
		Map<String, String> waterMeterMstr = getWaterMeterMstr();
		return (waterMeterMstr.get(mstrCode) == null) ? "N/A" : waterMeterMstr.get(mstrCode);
	}

	public static Map<String, String> getNoticeTypeMstr() {
		Map<String, String> noticeTypeMap = new HashMap<String, String>();
		noticeTypeMap.put(NOTICE_TYPE_BILL, NOTICE_TYPE_DEMAND_BILL);
		noticeTypeMap.put(NOTICE_TYPE_SPECIAL_NOTICE, NOTICE_TYPE_SPECIAL_NOTICE);
		noticeTypeMap.put(NOTICE_TYPE_MUTATION_CERTIFICATE, NOTICE_TYPE_MUTATION_CERTIFICATE);
		noticeTypeMap.put(NOTICE_TYPE_ESD, NOTICE_TYPE_ESD);
	        noticeTypeMap.put(NOTICE_TYPE_OC, NOTICE_TYPE_OC);
                noticeTypeMap.put(NOTICE_TYPE_GRPPROCEEDINGS, NOTICE_TYPE_GRPPROCEEDINGS);
                noticeTypeMap.put(NOTICE_TYPE_RPPROCEEDINGS, NOTICE_TYPE_RPPROCEEDINGS);
                noticeTypeMap.put(NOTICE_TYPE_VRPROCEEDINGS, NOTICE_TYPE_VRPROCEEDINGS);
                noticeTypeMap.put(NOTICE_TYPE_EXEMPTION, NOTICE_TYPE_EXEMPTIONPROCEEDINGS);
		return noticeTypeMap;
	}

	public static Map<String, String> getAmenities() {
		Map<String, String> amenitiesMap = new HashMap<String, String>();
		amenitiesMap.put(AMENITY_TYPE_FULL, AMENITY_TYPE_FULL);
		amenitiesMap.put(AMENITY_TYPE_PARTIAL, AMENITY_TYPE_PARTIAL);
		amenitiesMap.put(AMENITY_TYPE_NIL, AMENITY_TYPE_NIL);

		return amenitiesMap;
	}

	public static String getAmenitiesDtls(String mstrCode) {
		Map<String, String> amenitiesrMstr = getAmenities();
		return amenitiesrMstr.get(mstrCode);
	}

	public static Map<Long, String> getFormattedBndryMap(List<Boundary> zoneList) {
		Map<Long, String> zoneMap = new TreeMap<Long, String>();
		for (Boundary boundary : zoneList) {
			zoneMap.put(boundary.getId(),
			boundary.getLocalName());
		}
		return zoneMap;
	}

	public static List<String> getTaxExemptedList() {
		return Arrays.asList("Agiaries", "Andhalaya", "Beggars Home", "Budh Vihar", "Burial ground", "Charitable",
				"Church", "Dharmshala", "Durgahs", "Government Tenant", "Gurudwara", "Jain Temple", "Mosque",
				"Musafirkhana", "Orphanages Asylum", "Place of cremation/burning ghat", "Prayer Halls", "Remand Home",
				"School and Hostels for the physically challenged", "Synagogues", "Temple");
	}

	@SuppressWarnings("serial")
    public static final LinkedHashMap<String, String> outstandingAmountRanges = new LinkedHashMap<String, String>() {
        {
            put("5000 25000", "5000-25000");
            put("25001 50000", "25001-50000");
            put("50001 100000", "50001-100000");
            put("100001", "100001 & Above");
        }
    };
}

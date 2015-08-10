/*******************************************************************************
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
 * 	1) All versions of this program, verbatim or modified must carry this 
 * 	   Legal Notice.
 * 
 * 	2) Any misrepresentation of the origin of the material is prohibited. It 
 * 	   is required that all modified versions of this material be marked in 
 * 	   reasonable ways as different from the original version.
 * 
 * 	3) This license does not grant any rights to any user of the program 
 * 	   with regards to rights under trademark law for use of the trade names 
 * 	   or trademarks of eGovernments Foundation.
 * 
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.ptis.actions.common;

import static org.egov.ptis.constants.PropertyTaxConstants.AMENITY_TYPE_FULL;
import static org.egov.ptis.constants.PropertyTaxConstants.AMENITY_TYPE_NIL;
import static org.egov.ptis.constants.PropertyTaxConstants.AMENITY_TYPE_PARTIAL;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE_TYPE_BILL;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE_TYPE_SPECIAL_NOTICE;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.property.PropertyTypeMasterDAO;
import org.egov.ptis.domain.dao.property.PropertyUsageDAO;
import org.springframework.beans.factory.annotation.Autowired;

public class CommonServices {

	@Autowired
	private static PropertyUsageDAO propertyUsageDAO;
	@Autowired
	private static PropertyTypeMasterDAO propertyTypeMasterDAO;

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

	public static TreeMap<Integer, String> floorMap() {
		TreeMap<Integer, String> floorMap = new TreeMap<Integer, String>();
		floorMap.put(-2, "Basement-2");
		floorMap.put(-1, "Basement-1");
		floorMap.put(0, "Ground Floor");
		floorMap.put(1, "1st floor");
		floorMap.put(2, "2nd Floor");
		floorMap.put(3, "3rd Floor");
		floorMap.put(4, "4th Floor");
		floorMap.put(5, "5th Floor");
		floorMap.put(6, "6th Floor");
		floorMap.put(7, "7th Floor");
		floorMap.put(8, "8th Floor");
		floorMap.put(9, "9th Floor");
		floorMap.put(10, "10th Floor");
		floorMap.put(11, "11th Floor");
		floorMap.put(12, "12th Floor");
		floorMap.put(13, "13th Floor");
		floorMap.put(14, "14th Floor");
		floorMap.put(15, "15th Floor");
		floorMap.put(16, "16th Floor");
		floorMap.put(17, "17th Floor");
		floorMap.put(18, "18th Floor");
		floorMap.put(19, "19th Floor");
		floorMap.put(20, "20th Floor");
		floorMap.put(21, "21st Floor");
		floorMap.put(22, "22nd Floor");
		floorMap.put(23, "23rd Floor");
		floorMap.put(24, "24th Floor");
		floorMap.put(25, "25th Floor");
		floorMap.put(26, "26th Floor");
		floorMap.put(27, "27th Floor");
		floorMap.put(28, "28th Floor");
		floorMap.put(29, "29th Floor");
		floorMap.put(30, "30th Floor");
		return floorMap;
	}

	public static String getFloorStr(Integer flrNo) {
		Map<Integer, String> floorMap = floorMap();
		String flrNoStr = "";
		if (flrNo != null && (flrNo >= -2 && flrNo <= 10)) {
			flrNoStr = floorMap.get(flrNo);
		}
		return flrNoStr;
	}

	public static Map<String, String> getNoticeTypeMstr() {
		Map<String, String> noticeTypeMap = new HashMap<String, String>();
		noticeTypeMap.put(NOTICE_TYPE_BILL, NOTICE_TYPE_BILL);
		noticeTypeMap.put(NOTICE_TYPE_SPECIAL_NOTICE, NOTICE_TYPE_SPECIAL_NOTICE);
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
			/*
			 * StringUtils.leftPad(boundary.getBoundaryNum().toString(), 2, "0")
			 * + '-' +
			 */boundary.getLocalName());
		}
		return zoneMap;
	}

	public static List<String> getTaxExemptedList() {
		return Arrays.asList("Agiaries", "Andhalaya", "Beggars Home", "Budh Vihar", "Burial ground", "Charitable",
				"Church", "Dharmshala", "Durgahs", "Government Tenant", "Gurudwara", "Jain Temple", "Mosque",
				"Musafirkhana", "Orphanages Asylum", "Place of cremation/burning ghat", "Prayer Halls", "Remand Home",
				"School and Hostels for the physically challenged", "Synagogues", "Temple");
	}

	public static final LinkedHashMap<String, String> outstandingAmountRanges = new LinkedHashMap<String, String>() {
		{
			put("5000 25000", "5000-25000");
			put("25001 50000", "25001-50000");
			put("50001 100000", "50001-100000");
			put("100001", "100001 & Above");
		}
	};
}

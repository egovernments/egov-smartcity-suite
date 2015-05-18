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

import static org.egov.ptis.constants.PropertyTaxConstants.NON_RESIDENTIAL_PROPERTY_TYPE_CATEGORY;
import static org.egov.ptis.constants.PropertyTaxConstants.OPEN_PLOT_PROPERTY_TYPE_CATEGORY;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPTYPE_CENTRAL_GOVT;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPTYPE_NON_RESD;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPTYPE_OPEN_PLOT;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPTYPE_RESD;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPTYPE_STATE_GOVT;
import static org.egov.ptis.constants.PropertyTaxConstants.RESIDENTIAL_PROPERTY_TYPE_CATEGORY;
import static org.egov.ptis.constants.PropertyTaxConstants.UNITTYPE_OPEN_PLOT;
import static org.egov.ptis.constants.PropertyTaxConstants.UNITTYPE_RESD;
import static org.egov.ptis.constants.PropertyTaxConstants.USAGES_FOR_NON_RESD;
import static org.egov.ptis.constants.PropertyTaxConstants.USAGES_FOR_OPENPLOT;
import static org.egov.ptis.constants.PropertyTaxConstants.USAGES_FOR_RESD;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.property.PropertyTypeMasterDAO;
import org.egov.ptis.domain.dao.property.PropertyUsageDAO;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.egov.ptis.domain.entity.property.PropertyUsage;
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
		floorMap.put(-5, "Lower Ground Floor");
		floorMap.put(-4, "Upper Ground Floor");
		floorMap.put(0, "Ground Floor");
		floorMap.put(1, "First Floor");
		floorMap.put(2, "Second Floor");
		floorMap.put(3, "Third Floor");
		floorMap.put(4, "Fourth Floor");
		floorMap.put(5, "Fifth Floor");
		floorMap.put(6, "Sixth Floor");
		floorMap.put(7, "Seventh Floor");
		floorMap.put(8, "Eighth Floor");
		floorMap.put(9, "Ninenth Floor");
		floorMap.put(10, "Tenth Floor");
		floorMap.put(11, "Eleventh Floor");
		floorMap.put(12, "Twelfth Floor");
		floorMap.put(13, "Thirteenth Floor");
		floorMap.put(14, "Fourteenth Floor");
		floorMap.put(15, "Fifteenth Floor");
		floorMap.put(16, "Sixteenth Floor");
		floorMap.put(17, "Seventeenth Floor");
		floorMap.put(18, "Eighteenth Floor");
		floorMap.put(19, "Nineteenth Floor");
		floorMap.put(20, "Twentieth Floor");
		floorMap.put(21, "Twenty First Floor");
		floorMap.put(22, "Twenty Second Floor");
		floorMap.put(23, "Twenty Third Floor");
		floorMap.put(24, "Twenty Fourth Floor");
		floorMap.put(25, "Twenty Fifth Floor");
		floorMap.put(26, "Twenty Sixth Floor");
		floorMap.put(27, "Twenty Seventh Floor");
		floorMap.put(28, "Twenty Eighth Floor");
		floorMap.put(29, "Twenty Ninth Floor");
		floorMap.put(30, "Thirtieth Floor");
		floorMap.put(31, "Thirty First Floor");
		floorMap.put(32, "Thirty Second Floor");
		floorMap.put(33, "Thirty Third Floor");
		floorMap.put(34, "Thirty Fourth Floor");
		floorMap.put(35, "Thirty Fifth Floor");
		floorMap.put(36, "Thirty Sixth Floor");
		floorMap.put(37, "Thirty Seventh Floor");
		floorMap.put(38, "Thirty Eighth Floor");
		floorMap.put(39, "Thirty Ninth Floor");
		floorMap.put(40, "Fortieth Floor");
		floorMap.put(41, "Forty First Floor");
		floorMap.put(42, "Forty Second Floor");
		floorMap.put(43, "Forty Third Floor");
		floorMap.put(44, "Forty Fourth Floor");
		floorMap.put(45, "Forty Fifth Floor");
		floorMap.put(46, "Forty Sixth Floor");
		floorMap.put(47, "Forty Seventh Floor");
		floorMap.put(48, "Forty Eighth Floor");
		floorMap.put(49, "Forty Nineth Floor");
		floorMap.put(50, "Fiftieth Floor");
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
		noticeTypeMap.put("Prativrutta", "Prativrutta");
		noticeTypeMap.put("Notice 127", "Notice 127");
		noticeTypeMap.put("Notice 134", "Notice 134");
		noticeTypeMap.put("Notice 155", "Notice 155");
		noticeTypeMap.put("Warrant-application", "Warrant-application");
		noticeTypeMap.put("Notice 156", "Notice 156");
		noticeTypeMap.put("Notice 159", "Notice 159");
		noticeTypeMap.put("Bill", "Bill");
		noticeTypeMap.put("MutationCertificate", "MutationCertificate");
		return noticeTypeMap;
	}

	public static Map<String, String> getAmenities() {
		Map<String, String> amenitiesMap = new HashMap<String, String>();
		amenitiesMap.put(PropertyTaxConstants.AMENITY_TYPE_FULL,
				PropertyTaxConstants.AMENITY_TYPE_FULL);
		amenitiesMap.put(PropertyTaxConstants.AMENITY_TYPE_PARTIAL,
				PropertyTaxConstants.AMENITY_TYPE_PARTIAL);
		amenitiesMap.put(PropertyTaxConstants.AMENITY_TYPE_NIL,
				PropertyTaxConstants.AMENITY_TYPE_NIL);

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
					StringUtils.leftPad(boundary.getBoundaryNum().toString(), 2, "0") + '-'
							+ boundary.getLocalName());
		}
		return zoneMap;
	}

	public static List<PropertyUsage> usagesForPropType(Integer propTypeId) {
		List<PropertyUsage> propUsageList = new ArrayList<PropertyUsage>();
		List<PropertyUsage> usagesList = propertyUsageDAO.findAll();
		PropertyTypeMaster propType = null;

		if (!propTypeId.toString().equals("-1")) {
			propType = propertyTypeMasterDAO.findById(propTypeId,
					false);
		} else {
			return Collections.EMPTY_LIST;
		}

		if (propType != null && !(propType.getCode().toString().equals("-1"))) {
			if (propType.getCode().equalsIgnoreCase(PROPTYPE_OPEN_PLOT)) {
				for (PropertyUsage pu : usagesList) {
					if (USAGES_FOR_OPENPLOT.contains(pu.getUsageName())) {
						propUsageList.add(pu);
					}
				}
			} else if (propType.getCode().equalsIgnoreCase(PROPTYPE_STATE_GOVT)
					|| propType.getCode().equalsIgnoreCase(PROPTYPE_CENTRAL_GOVT)
					|| propType.getCode().equalsIgnoreCase(PROPTYPE_RESD)) {

				for (PropertyUsage pu : usagesList) {
					if (USAGES_FOR_RESD.contains(pu.getUsageName())) {
						propUsageList.add(pu);
					}
				}
			} else if (propType.getCode().equalsIgnoreCase(PROPTYPE_NON_RESD)) {
				for (PropertyUsage pu : usagesList) {
					if (USAGES_FOR_NON_RESD.contains(pu.getUsageName())) {
						propUsageList.add(pu);
					}
				}
			}
		}

		return propUsageList;
	}

	public static List<String> getTaxExemptedList() {
		return Arrays.asList("Agiaries", "Andhalaya", "Beggars Home", "Budh Vihar",
				"Burial ground", "Charitable", "Church", "Dharmshala", "Durgahs",
				"Government Tenant", "Gurudwara", "Jain Temple", "Mosque", "Musafirkhana",
				"Orphanages Asylum", "Place of cremation/burning ghat", "Prayer Halls",
				"Remand Home", "School and Hostels for the physically challenged", "Synagogues",
				"Temple");
	}

	public static String getUnitTypeCategory(String unitTypeCode, String categoryCode) {
		String categoryValue = null;
		if (unitTypeCode.equals(UNITTYPE_OPEN_PLOT)) {
			categoryValue = OPEN_PLOT_PROPERTY_TYPE_CATEGORY.get(categoryCode);
		} else if (unitTypeCode.equals(UNITTYPE_RESD)) {
			categoryValue = RESIDENTIAL_PROPERTY_TYPE_CATEGORY.get(categoryCode);
		} else {
			categoryValue = NON_RESIDENTIAL_PROPERTY_TYPE_CATEGORY.get(categoryCode);
		}
		return categoryValue;
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

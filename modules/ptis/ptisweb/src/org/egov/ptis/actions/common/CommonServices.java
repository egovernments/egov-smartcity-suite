package org.egov.ptis.actions.common;

import static org.egov.ptis.nmc.constants.NMCPTISConstants.NON_RESIDENTIAL_PROPERTY_TYPE_CATEGORY;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.OPEN_PLOT_PROPERTY_TYPE_CATEGORY;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPTYPE_CENTRAL_GOVT;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPTYPE_NON_RESD;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPTYPE_OPEN_PLOT;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPTYPE_RESD;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPTYPE_STATE_GOVT;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.RESIDENTIAL_PROPERTY_TYPE_CATEGORY;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.UNITTYPE_OPEN_PLOT;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.UNITTYPE_RESD;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.USAGES_FOR_NON_RESD;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.USAGES_FOR_OPENPLOT;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.USAGES_FOR_RESD;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.egov.lib.admbndry.Boundary;
import org.egov.ptis.domain.dao.property.PropertyDAOFactory;
import org.egov.ptis.domain.dao.property.PropertyTypeMasterDAO;
import org.egov.ptis.domain.dao.property.PropertyUsageDAO;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.egov.ptis.domain.entity.property.PropertyUsage;
import org.egov.ptis.nmc.constants.NMCPTISConstants;

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
		return NMCPTISConstants.waterRates;
	}

	public static Integer getWaterMeterRate(String mstrCode) {
		Map<String, Integer> waterMeterMstr = getWaterMeterRateMstr();
		return waterMeterMstr.get(mstrCode);
	}

	public static String getWaterMeterDtls(String mstrCode) {
		Map<String, String> waterMeterMstr = getWaterMeterMstr();
		return (waterMeterMstr.get(mstrCode)==null) ? "N/A" : waterMeterMstr.get(mstrCode);
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
		amenitiesMap.put(NMCPTISConstants.AMENITY_TYPE_FULL, NMCPTISConstants.AMENITY_TYPE_FULL);
		amenitiesMap.put(NMCPTISConstants.AMENITY_TYPE_PARTIAL, NMCPTISConstants.AMENITY_TYPE_PARTIAL);
		amenitiesMap.put(NMCPTISConstants.AMENITY_TYPE_NIL, NMCPTISConstants.AMENITY_TYPE_NIL);

		return amenitiesMap;
	}

	public static String getAmenitiesDtls(String mstrCode) {
		Map<String, String> amenitiesrMstr = getAmenities();
		return amenitiesrMstr.get(mstrCode);
	}

	public static Map<Integer, String> getFormattedBndryMap(List<Boundary> zoneList) {
		Map<Integer, String> zoneMap = new TreeMap<Integer, String>();
		for (Boundary boundary : zoneList) {
			zoneMap.put(boundary.getId(), StringUtils.leftPad(boundary.getBoundaryNum().toString(), 2, "0") + '-'
					+ boundary.getName());
		}
		return zoneMap;
	}

	public static List<PropertyUsage> usagesForPropType(Integer propTypeId) {
		PropertyUsageDAO propertyUsageDAO = PropertyDAOFactory.getDAOFactory().getPropertyUsageDAO();
		PropertyTypeMasterDAO propertyTypeMasterDAO = PropertyDAOFactory.getDAOFactory().getPropertyTypeMasterDAO();
		List<PropertyUsage> propUsageList = new ArrayList<PropertyUsage>();
		List<PropertyUsage> usagesList = propertyUsageDAO.findAll();
		PropertyTypeMaster propType = null;

		if (!propTypeId.toString().equals("-1")) {
			propType = (PropertyTypeMaster) propertyTypeMasterDAO.findById(propTypeId.longValue(), true);
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
		List<String> taxExemptedList = new ArrayList<String>();
		taxExemptedList.add("Temple");
		taxExemptedList.add("Mosque");
		taxExemptedList.add("Church");
		taxExemptedList.add("Gurudwara");
		taxExemptedList.add("Synagogues");
		taxExemptedList.add("Durgahs");
		taxExemptedList.add("Agiaries");
		taxExemptedList.add("Jain Temple");
		taxExemptedList.add("Budh Vihar");
		taxExemptedList.add("Prayer Halls");
		taxExemptedList.add("Dharmshala");
		taxExemptedList.add("Musafirkhana");
		taxExemptedList.add("Orphanages Asylum");
		taxExemptedList.add("Beggars Home");
		taxExemptedList.add("Andhalaya");
		taxExemptedList.add("Remand Home");
		taxExemptedList.add("School and Hostels for the physically challenged");
		taxExemptedList.add("Place of cremation/burning ghat");
		taxExemptedList.add("Burial ground");
		taxExemptedList.add("Charitable");
		Collections.sort(taxExemptedList);

		return taxExemptedList;
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
	
	public static final Map<String, String> outstandingAmountRanges = new LinkedHashMap<String, String>() {
		{
			put("5000 25000", "5000-25000");
			put("25001 50000", "25001-50000");
			put("50001 100000", "50001-100000"); 
			put("100001 99999999", "100001 & Above");
		}
	};
}

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
package org.egov.ptis.bean;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.egov.commons.Installment;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.infstr.utils.StringUtils;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.client.model.MiscellaneousTax;
import org.egov.ptis.client.model.TaxCalculationInfo;
import org.egov.ptis.client.model.UnitTaxCalculationInfo;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.domain.dao.property.PropertyDAOFactory;
import org.egov.ptis.domain.entity.demand.FloorwiseDemandCalculations;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.property.FloorIF;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyMutation;
import org.egov.ptis.domain.entity.property.UnitCalculationDetail;
import org.egov.ptis.utils.PTISCacheManager;
import org.egov.ptis.utils.PTISCacheManagerInteface;

public class PropertyNoticeInfo {
	private final PropertyImpl property;
	private TaxCalculationInfo taxCalInfo;
	private String noticeNo;
	// Total Tax payable for Current Installment
	// private BigDecimal totalTax;

	PropertyTaxUtil propertyTaxUtil = new PropertyTaxUtil();
	private static final Logger LOGGER = Logger.getLogger(PropertyNoticeInfo.class);
	private static final String PROPERTY_TYPE = "PROPERTY-TYPE";
	private static final String PROPERTY_TYPE_CATEGORY = "PROPERTY-TYPE-CATEGORY";
	private static final String PROPERTY_AMENITY = "PROPERTY-AMENITY";

	private final Set<PropertyFloorDetailsInfo> propertyFloorDetails = new TreeSet<PropertyFloorDetailsInfo>();
	private DateFormat dateFormatter = new SimpleDateFormat(PropertyTaxConstants.DATE_FORMAT_DDMMYYY);
	private int isCentralGovtProp = 0;

	private PTISCacheManagerInteface ptisCacheMgr = new PTISCacheManager();
	Map<Date, Map<Installment, TaxCalculationInfo>> installmentAndHistoryTaxCalcsByDate = new TreeMap<Date, Map<Installment, TaxCalculationInfo>>();
	Map<Date, Map<String, String>> propertyInfoByCreatedDate = new TreeMap<Date, Map<String, String>>();
	private Ptdemand currentDemand = null;

	public Set<PropertyFloorDetailsInfo> getPropertyFloorDetails() {
		return propertyFloorDetails;
	}

	public PropertyNoticeInfo(PropertyImpl property, String noticeNo, Boolean instwiseNoticeReport) {
		this.property = property;
		this.noticeNo = noticeNo;
		this.currentDemand = PropertyDAOFactory.getDAOFactory().getPtDemandDao()
				.getNonHistoryCurrDmdForProperty(property);
		addFloorDetails();
	}

	private void addFloorDetails() {

		String query = "from UnitCalculationDetail where property = ? order by installmentFromDate, unitNumber";

		@SuppressWarnings("unchecked")
		List<UnitCalculationDetail> unitCalculationDetails = HibernateUtil
				.getCurrentSession().createQuery(query).setEntity(0, property).list();

		for (UnitCalculationDetail unitCalcDetail : unitCalculationDetails) {
			propertyFloorDetails.add(new PropertyFloorDetailsInfo(unitCalcDetail, null, null));
		}
		
		//LOGGER.info("propertyFloorDetails == "+propertyFloorDetails.toString());
	}

	/**
	 * The method returns the date when the property was approved in case of a
	 * transfer.
	 *
	 * @return
	 */
	public Date getApprovalDate() {
		// Given property would have been transfered and created as a new
		// property.
		// so date of approval of transfer will be the date this property was
		// created.
		if (!property.getBasicProperty().getPropMutationSet().isEmpty())
			return property.getCreatedDate().toDate();

		return null;
	}

	/**
	 * The method returns the name of the most recent owner of the property in
	 * case the property has been transfered.
	 *
	 * @return a <code>String</code> representing the name of the current owner
	 */
	public String getNewOwnerName() {
		if (!property.getBasicProperty().getPropMutationSet().isEmpty())
			return ptisCacheMgr.buildOwnerFullName(property.getPropertyOwnerSet());

		return "";
	}

	/**
	 * The method returns the total number of 'Tenants' in the property.
	 *
	 * @return an int value representing the number of tenants in the property
	 */
	public int getNoOfTenants() {
		int tenants = 0;

		for (FloorIF floor : property.getPropertyDetail().getFloorDetails()) {
			if (PropertyTaxConstants.TENANT.equalsIgnoreCase(floor.getPropertyOccupation().getOccupancyCode())) {
				tenants++;
			}
		}

		return tenants;
	}

	/**
	 * The method returns the date the property was transfered last. If the
	 * property has not been transfered, transfer date is null.
	 *
	 * @return a <code>Date</code> instance indicating the date when the
	 *         property was transfered
	 */
	public Date getTransferDate() {
		Set<PropertyMutation> propMutSet = property.getBasicProperty().getPropMutationSet();

		if (propMutSet.isEmpty()) {
			return null;
		}

		SortedSet<Date> mutationDates = new TreeSet<Date>();
		for (PropertyMutation propertyMutation : propMutSet) {
			mutationDates.add(propertyMutation.getMutationDate());
		}

		return mutationDates.last();
	}

	public BigDecimal getTotalTax() {
		return this.currentDemand.getBaseDemand();
	}

	public BigDecimal getTotalALV() {
		BigDecimal totalALV = BigDecimal.ZERO;

		for (FloorwiseDemandCalculations floorDemand : currentDemand.getDmdCalculations().getFlrwiseDmdCalculations()) {
			totalALV = totalALV.add(floorDemand.getAlv());
		}

		return totalALV;
	}

	public int getNoOfUnits() {
		Set<Integer> noOfUnits = new TreeSet<Integer>();

		for (FloorIF floor : property.getPropertyDetail().getFloorDetails()) {
			noOfUnits.add(Integer.valueOf(floor.getExtraField1()));
		}

		return noOfUnits.size();
	}

	public String getPropertyNo() {
		return property.getBasicProperty().getUpicNo();
	}

	public String getHouseNo() {
		return property.getBasicProperty().getAddress().getHouseNoBldgApt();
	}

	private String getWardNumber() {
		return getWardBoundary().getBoundaryNum().toString();
	}

	private Boundary getWardBoundary() {
		return property.getBasicProperty().getPropertyID().getWard();
	}

	public String getWardNo() {

		return StringUtils.leftPad(getWardNumber(), 3, '0');
	}

	public String getZoneNo() {

		return StringUtils.leftPad(property.getBasicProperty().getBoundary().getParent().getBoundaryNum().toString(),
				2, '0');
	}

	public String getWardName() {
		return this.getWardNumber() + "-" + getWardBoundary().getName();
	}

	public String getZoneName() {
		return property.getBasicProperty().getPropertyID().getZone().getName();
	}

	public String getStreet() {
		return getOwnerAddress();
	}

	public String getOwnerName() {
		return ptisCacheMgr.buildOwnerFullName(property.getBasicProperty());
	}

	public String getOwnerAddress() {
		return ptisCacheMgr.buildAddressByImplemetation(property.getBasicProperty().getAddress()).concat(", Nagpur");
	}

	public String getCompleteAddress() {
		StringBuilder completeAddr = new StringBuilder();
		completeAddr.append(getOwnerName());
		completeAddr.append("\n");
		completeAddr.append(getOwnerAddress());
		return completeAddr.toString();
	}

	/**
	 * TO DO : Should be changed after Notice-BasicProperty relationship is
	 * confirmed
	 *
	 * @return
	 *
	 */
	public String getNoticeNo() {
		return this.noticeNo;
	}

	public String getNoticeDate() {

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		return sdf.format(new Date());

	}

	public String getPropertyType() {
		return PropertyTaxConstants.PROPERTYTYPE_CODE_TO_STR.get(property.getPropertyDetail().getPropertyTypeMaster()
				.getCode());
	}

	public int getIsCentralGovtProp() {
		return isCentralGovtProp;
	}

	public void setIsCentralGovtProp(int isCentralGovtProp) {
		this.isCentralGovtProp = isCentralGovtProp;
	}

	private void getTotalTaxForMiscTax(Map<Integer, Map<String, BigDecimal>> miscTaxAndTotalTaxForUnit,
			List<UnitTaxCalculationInfo> unitTaxes) {
		LOGGER.info("Entered into getTotalTaxForMiscTax");
		LOGGER.info("getTotalTaxForMiscTax - miscTaxAndTotalTaxForUnit: " + miscTaxAndTotalTaxForUnit);

		if (miscTaxAndTotalTaxForUnit.isEmpty()) {

			for (UnitTaxCalculationInfo unit : unitTaxes) {
				Map<String, BigDecimal> miscTaxAndTotalTax = new HashMap<String, BigDecimal>();
				for (MiscellaneousTax miscTax : unit.getMiscellaneousTaxes()) {
					miscTaxAndTotalTax.put(miscTax.getTaxName(), miscTax.getTotalCalculatedTax());
				}
				miscTaxAndTotalTaxForUnit.put(unit.getUnitNumber(), miscTaxAndTotalTax);
			}

		} else {

			for (UnitTaxCalculationInfo unit : unitTaxes) {
				Map<String, BigDecimal> miscTaxAndTotalTax = miscTaxAndTotalTaxForUnit.get(unit.getUnitNumber()) == null ? new HashMap<String, BigDecimal>()
						: miscTaxAndTotalTaxForUnit.get(unit.getUnitNumber());
				for (MiscellaneousTax miscTax : unit.getMiscellaneousTaxes()) {
					miscTaxAndTotalTax.put(miscTax.getTaxName(), miscTax.getTotalCalculatedTax());
				}
				miscTaxAndTotalTaxForUnit.put(unit.getUnitNumber(), miscTaxAndTotalTax);
			}

		}

		LOGGER.info("getTotalTaxForMiscTax - miscTaxAndTotalTaxForUnit: " + miscTaxAndTotalTaxForUnit);
		LOGGER.info("Exiting from getTotalTaxForMiscTax");
	}

	private Map<Date, Map<Integer, List<UnitTaxCalculationInfo>>> orderByDate(
			Map<Integer, List<UnitTaxCalculationInfo>> unitsByUnitNo) throws ParseException {
		LOGGER.debug("Entered into orderByDate");

		Map<Date, Map<Integer, List<UnitTaxCalculationInfo>>> unitTaxesByDateUnitNo = new TreeMap<Date, Map<Integer, List<UnitTaxCalculationInfo>>>();

		for (Map.Entry<Integer, List<UnitTaxCalculationInfo>> mapEntry : unitsByUnitNo.entrySet()) {

			for (UnitTaxCalculationInfo unit : mapEntry.getValue()) {
				Date date = dateFormatter.parse(unit.getInstDate());

				if (unitTaxesByDateUnitNo.get(date) == null) {
					Map<Integer, List<UnitTaxCalculationInfo>> unByUnitNo = new TreeMap<Integer, List<UnitTaxCalculationInfo>>();
					List<UnitTaxCalculationInfo> units = new ArrayList<UnitTaxCalculationInfo>();
					units.add(unit);
					unByUnitNo.put(mapEntry.getKey(), units);
					unitTaxesByDateUnitNo.put(date, unByUnitNo);
				} else {
					if (unitTaxesByDateUnitNo.get(date).get(mapEntry.getKey()) == null) {
						List<UnitTaxCalculationInfo> units = new ArrayList<UnitTaxCalculationInfo>();
						units.add(unit);
						unitTaxesByDateUnitNo.get(date).put(mapEntry.getKey(), units);
					} else {
						unitTaxesByDateUnitNo.get(date).get(mapEntry.getKey()).add(unit);
					}
				}
			}
		}
		LOGGER.debug("Entered into orderByDate");

		return unitTaxesByDateUnitNo;
	}

	/**
	 * Gives the Property Assessment Date
	 *
	 * @return assessment date (property created date)
	 */
	public String getAssessmentDate() {
		return dateFormatter.format(this.property.getBasicProperty().getPropCreateDate());
	}
}

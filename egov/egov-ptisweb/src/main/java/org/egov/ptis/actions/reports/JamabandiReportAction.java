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
package org.egov.ptis.actions.reports;

import static java.lang.Boolean.TRUE;
import static java.math.BigDecimal.ZERO;
import static java.util.Calendar.YEAR;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPTYPE_CENTRAL_GOVT;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPTYPE_NON_RESD;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPTYPE_OPEN_PLOT;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPTYPE_RESD;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPTYPE_STATE_GOVT;
import static org.egov.ptis.constants.PropertyTaxConstants.REPORT_TEMPLATENAME_JAMABANDI;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.WARD_BNDRY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.ZONE_BNDRY_TYPE;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.Installment;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.reporting.engine.ReportRequest.ReportDataSourceType;
import org.egov.infra.web.struts.actions.ReportFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.ptis.bean.ReportInfo;
import org.egov.ptis.bean.UnitWiseInfo;
import org.egov.ptis.client.util.CurrFlrDmdCalcMvComparator;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.domain.entity.property.CurrFloorDmdCalcMaterializeView;
import org.egov.ptis.domain.entity.property.InstDmdCollMaterializeView;
import org.egov.ptis.domain.entity.property.PropertyMaterlizeView;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;

@ParentPackage("egov")
public class JamabandiReportAction extends ReportFormAction {
	private final Logger LOGGER = Logger.getLogger(getClass());
	private Long zoneId;
	private Long wardId;
	private String partNo;
	public static final String RESIDENTIAL = "R";
	public static final String NONRESD = "NR";
	public static final String OPENPLOT = "OP";
	public static final String SGOVT = "SGOVT";
	public static final String CGOVT = "CGOVT";
	@Autowired
	private BoundaryService boundaryDAO;
	@Autowired
        private PropertyTaxUtil propertyTaxUtil;

	@SuppressWarnings("unchecked")
	public void prepare() {
		LOGGER.debug("Entered into prepare method");
		super.prepare();
		List<Boundary> zoneList = getPersistenceService().findAllBy(
				"from BoundaryImpl BI where BI.boundaryType.name=? and BI.boundaryType.heirarchyType.name=? "
						+ "and BI.isHistory='N' order by BI.name", ZONE_BNDRY_TYPE, REVENUE_HIERARCHY_TYPE);

		addDropdownData("Zone", zoneList);
		LOGGER.debug("Zone id : " + zoneId + ", " + "Ward id : " + wardId);
		prepareWardDropDownData(zoneId != null, wardId != null);
		if (wardId == null || wardId.equals(-1)) {
			addDropdownData("partNumbers", Collections.EMPTY_LIST);
		}
		LOGGER.debug("Exit from prepare method");
	}

	@SuppressWarnings("unchecked")
	private void prepareWardDropDownData(boolean zoneExists, boolean wardExists) {
		LOGGER.debug("Entered into prepareWardDropDownData method");
		LOGGER.debug("Zone Exists ? : " + zoneExists + ", " + "Ward Exists ? : " + wardExists);
		if (zoneExists && wardExists) {
			List<Boundary> wardNewList = new ArrayList<Boundary>();
			wardNewList = getPersistenceService()
					.findAllBy(
							"from BoundaryImpl BI where BI.boundaryType.name=? and BI.parent.id = ? and BI.isHistory='N' order by BI.name ",
							WARD_BNDRY_TYPE, getZoneId());
			addDropdownData("wardList", wardNewList);
		} else {
			addDropdownData("wardList", Collections.EMPTY_LIST);
		}
		LOGGER.debug("Exit from prepareWardDropDownData method");
	}

	@SkipValidation
	public String index() {
		return INDEX;
	}

	public void validate() {
		if (getZoneId() == null || getZoneId() == -1) {
			addActionError(getText("mandatory.zone"));
		}
		if (getWardId() == null || getWardId() == -1) {
			addActionError(getText("mandatory.ward"));
		}
	}

	@SuppressWarnings("unchecked")
	@ValidationErrorPage(value = INDEX)
	public String generateJamabandi() {
		ReportInfo reportInfo = new ReportInfo();
		List<UnitWiseInfo> unitWiseInfoList = new ArrayList<UnitWiseInfo>();
		Installment currentInstallment = propertyTaxUtil.getCurrentInstallment();
		if ((zoneId != null && zoneId != -1) && (wardId != null && wardId != -1)) {
			String strZoneNum = "";//FIXME PHOENIX (boundaryDAO.getBoundary(zoneId).getBoundaryNum()).toString();
			String strWardNum = "";//FIXME PHOENIX(boundaryDAO.getBoundary(wardId).getBoundaryNum()).toString();
			StringBuffer query = new StringBuffer(300);

			query.append("from PropertyMaterlizeView pmv left join fetch pmv.currFloorDmdCalc left join fetch pmv.propTypeMstrID where pmv.zone.id=? and pmv.ward.id=? ");
			if (partNo != null && !partNo.equals("-1")) {
				query.append(" and pmv.partNo=? ");
			}
			query.append(" order by pmv.houseNo ");
			Query qry = getPersistenceService().getSession().createQuery(query.toString());
			qry.setParameter(0, zoneId).setParameter(1, wardId);
			if (partNo != null && !partNo.equals("-1")) {
				qry.setParameter(2, partNo);
			}
			List<PropertyMaterlizeView> propMatViewList = qry.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
					.list();
			LOGGER.debug("PropertyMaterlizeView List Size" + propMatViewList.size());

			for (PropertyMaterlizeView propMatView : propMatViewList) {
				if (propMatView.getCurrFloorDmdCalc() == null || propMatView.getCurrFloorDmdCalc().isEmpty()
						|| propMatView.getCurrFloorDmdCalc().size() == 0) {
					UnitWiseInfo unitWiseInfo = prepareNonUnitWiseInfo(propMatView, currentInstallment);
					unitWiseInfo.setIsLastUnit(TRUE);
					unitWiseInfoList.add(unitWiseInfo);
				} else {
					// If there is only one unit
					if (propMatView.getCurrFloorDmdCalc().size() == 1) {
						CurrFloorDmdCalcMaterializeView currFlrDmdMatView = propMatView.getCurrFloorDmdCalc()
								.iterator().next();
						UnitWiseInfo unitWiseInfo = prepareUnitWiseInfo(currFlrDmdMatView);
						unitWiseInfo.setIsLastUnit(TRUE);
						unitWiseInfoList.add(unitWiseInfo);
					} else {
						// if there are more than one unit
						List<CurrFloorDmdCalcMaterializeView> flrDmdCalcList = new ArrayList<CurrFloorDmdCalcMaterializeView>(
								propMatView.getCurrFloorDmdCalc());
						Collections.sort(flrDmdCalcList, new CurrFlrDmdCalcMvComparator());
						List unitList = new ArrayList();
						UnitWiseInfo unitWiseInfoTotal = null;
						int unitCounter = 0;
						/**
						 * unitList list has the list of unit number for that
						 * particular index number unitWiseInfoTotal has the
						 * grouped info of unitwise if the unit numbers are same
						 * for that particular index num
						 */
						for (CurrFloorDmdCalcMaterializeView currFlrDmdMatView : flrDmdCalcList) {
							UnitWiseInfo unitWiseInfo = prepareUnitWiseInfo(currFlrDmdMatView);
							// initially the block is executed
							if (unitList.size() == 0) {
								unitList.add(unitWiseInfo.getUnitNo());
								unitWiseInfoTotal = unitWiseInfo;
								unitCounter++;
							} else if (unitList.contains(unitWiseInfo.getUnitNo())) {

								if (unitWiseInfoTotal == null) {
									/**
									 * if the unit is already added to the list
									 * and if still similar units exists
									 */
									unitWiseInfoTotal = getExistingUnitFromList(unitWiseInfoList, unitWiseInfo);
								} else {
									/**
									 * if the next unit exists in the list, then
									 * the unit info is added to the existing
									 * total
									 */
									unitWiseInfoTotal = addUnitWiseInfo(unitWiseInfoTotal, unitWiseInfo);
								}

								unitCounter++;
								/**
								 * if the counter and size are same, ie. if no
								 * other units exits, then the unitWiseInfoTotal
								 * is added to list
								 */
								if (unitCounter == flrDmdCalcList.size()) {
									unitWiseInfoList.add(unitWiseInfoTotal);
								}
							} else if (!unitList.contains(unitWiseInfo.getUnitNo())) {
								/**
								 * if the next unit number does not exist in the
								 * list, then the unit number is added to
								 * unitList and the total is added to list.
								 */
								unitList.add(unitWiseInfo.getUnitNo());
								if (unitWiseInfoTotal != null) {
									unitWiseInfoList.add(unitWiseInfoTotal);
									unitWiseInfoTotal = null;
								}
								/**
								 * This use case occurs when there are no common
								 * unit numbers, so there wont be
								 * unitWiseInfoTotal and the actual unitWiseInfo
								 * has to be added to the list
								 */
								if (unitWiseInfoTotal == null) {
									unitWiseInfoList.add(unitWiseInfo);
								}
							}
						}
					}
				}
			}

			Calendar installmentFromDate = Calendar.getInstance();
			installmentFromDate.setTime(currentInstallment.getFromDate());

			Calendar installmentToDate = Calendar.getInstance();
			installmentToDate.setTime(currentInstallment.getToDate());

			reportInfo.setCurrInstallment(installmentFromDate.get(YEAR) + "-" + installmentToDate.get(YEAR));

			reportInfo.setZoneNo(strZoneNum);
			reportInfo.setWardNo(strWardNum);
			reportInfo.setPartNo(partNo);
			reportInfo.setUnitWiseInfoList(unitWiseInfoList);

			setDataSourceType(ReportDataSourceType.JAVABEAN);
			setReportData(reportInfo);
			super.report();
		}
		return "generate";
	}

	private UnitWiseInfo getExistingUnitFromList(List<UnitWiseInfo> unitWiseInfoList, UnitWiseInfo unitWiseInfo) {
		for (UnitWiseInfo unitwise : unitWiseInfoList) {
			if (unitwise.getIndexNumber().equals(unitWiseInfo.getIndexNumber())
					&& unitwise.getUnitNo().equals(unitWiseInfo.getUnitNo())) {
				unitWiseInfoList.remove(unitwise);
				return unitwise;
			}
		}
		return unitWiseInfo;
	}

	private UnitWiseInfo addUnitWiseInfo(UnitWiseInfo unitWiseInfoTotal, UnitWiseInfo unitWiseInfo2) {
		if (unitWiseInfo2.getPropType() != null
				&& (unitWiseInfoTotal.getPropType() != null && !unitWiseInfoTotal.getPropType().contains(
						unitWiseInfo2.getPropType()))) {
			unitWiseInfoTotal.setPropType(unitWiseInfoTotal.getPropType() + "-" + unitWiseInfo2.getPropType());
		} else if (unitWiseInfoTotal.getPropType() == null && unitWiseInfo2.getPropType() != null) {
			unitWiseInfoTotal.setPropType(unitWiseInfo2.getPropType());
		}
		unitWiseInfoTotal.setAlv(unitWiseInfoTotal.getAlv().add(unitWiseInfo2.getAlv()));
		unitWiseInfoTotal.setConservancyTax(unitWiseInfoTotal.getConservancyTax()
				.add(unitWiseInfo2.getConservancyTax()));
		unitWiseInfoTotal.setWaterTax(unitWiseInfoTotal.getWaterTax().add(unitWiseInfo2.getWaterTax()));
		unitWiseInfoTotal.setGeneralTax(unitWiseInfoTotal.getGeneralTax().add(unitWiseInfo2.getGeneralTax()));
		unitWiseInfoTotal.setFireServiceTax(unitWiseInfoTotal.getFireServiceTax()
				.add(unitWiseInfo2.getFireServiceTax()));
		unitWiseInfoTotal.setLightTax(unitWiseInfoTotal.getLightTax().add(unitWiseInfo2.getLightTax()));
		unitWiseInfoTotal.setTotalTax(unitWiseInfoTotal.getTotalTax().add(unitWiseInfo2.getTotalTax()));
		unitWiseInfoTotal.setBigBuildingTax(unitWiseInfoTotal.getBigBuildingTax()
				.add(unitWiseInfo2.getBigBuildingTax()));
		unitWiseInfoTotal.setEduCess(unitWiseInfoTotal.getEduCess().add(unitWiseInfo2.getEduCess()));
		unitWiseInfoTotal.setEgsCess(unitWiseInfoTotal.getEgsCess().add(unitWiseInfo2.getEgsCess()));

		return unitWiseInfoTotal;
	}

	private UnitWiseInfo initializeUnitWiseInfo() {
		UnitWiseInfo propertyWiseTotals = new UnitWiseInfo();
		propertyWiseTotals.setAlv(ZERO);
		propertyWiseTotals.setBigBuildingTax(ZERO);
		propertyWiseTotals.setConservancyTax(ZERO);
		propertyWiseTotals.setEduCess(ZERO);
		propertyWiseTotals.setEgsCess(ZERO);
		propertyWiseTotals.setFireServiceTax(ZERO);
		propertyWiseTotals.setGeneralTax(ZERO);
		propertyWiseTotals.setLightTax(ZERO);
		propertyWiseTotals.setTotalTax(ZERO);
		propertyWiseTotals.setWaterTax(ZERO);
		return propertyWiseTotals;
	}

	private UnitWiseInfo preparePropertyWiseTotals(UnitWiseInfo propertyWiseTotals, UnitWiseInfo unitWiseInfo) {
		propertyWiseTotals.setAlv(propertyWiseTotals.getAlv().add(
				unitWiseInfo.getAlv() != null ? unitWiseInfo.getAlv() : ZERO));
		propertyWiseTotals.setConservancyTax(propertyWiseTotals.getConservancyTax().add(
				unitWiseInfo.getConservancyTax() != null ? unitWiseInfo.getConservancyTax() : ZERO));
		propertyWiseTotals.setWaterTax(propertyWiseTotals.getWaterTax().add(
				unitWiseInfo.getWaterTax() != null ? unitWiseInfo.getWaterTax() : ZERO));
		propertyWiseTotals.setGeneralTax(propertyWiseTotals.getGeneralTax().add(
				unitWiseInfo.getGeneralTax() != null ? unitWiseInfo.getGeneralTax() : ZERO));
		propertyWiseTotals.setFireServiceTax(propertyWiseTotals.getFireServiceTax().add(
				unitWiseInfo.getFireServiceTax() != null ? unitWiseInfo.getFireServiceTax() : ZERO));
		propertyWiseTotals.setLightTax(propertyWiseTotals.getLightTax().add(
				unitWiseInfo.getLightTax() != null ? unitWiseInfo.getLightTax() : ZERO));
		propertyWiseTotals.setTotalTax(propertyWiseTotals.getTotalTax().add(
				unitWiseInfo.getTotalTax() != null ? unitWiseInfo.getTotalTax() : ZERO));
		propertyWiseTotals.setBigBuildingTax(propertyWiseTotals.getBigBuildingTax().add(
				unitWiseInfo.getBigBuildingTax() != null ? unitWiseInfo.getBigBuildingTax() : ZERO));
		propertyWiseTotals.setEduCess(propertyWiseTotals.getEduCess().add(
				unitWiseInfo.getEduCess() != null ? unitWiseInfo.getEduCess() : ZERO));
		propertyWiseTotals.setEgsCess(propertyWiseTotals.getEgsCess().add(
				unitWiseInfo.getEgsCess() != null ? unitWiseInfo.getEgsCess() : ZERO));
		return propertyWiseTotals;
	}

	private UnitWiseInfo prepareUnitWiseInfo(CurrFloorDmdCalcMaterializeView currFlrDmdMatView) {
		UnitWiseInfo unitwiseInfo = preparePropInfo(currFlrDmdMatView.getPropMatView());

		BigDecimal totalTax = currFlrDmdMatView.getSewerageTax().add(currFlrDmdMatView.getWaterTax())
				.add(currFlrDmdMatView.getGeneralTax()).add(currFlrDmdMatView.getFireTax())
				.add(currFlrDmdMatView.getLightTax());

		if (currFlrDmdMatView.getUnitTypeConst() != null && !currFlrDmdMatView.getUnitTypeConst().equals("N/A")
				&& unitwiseInfo.getPropType() == null) {
			unitwiseInfo.setPropType(currFlrDmdMatView.getUnitTypeConst());
		}
		unitwiseInfo.setUnitNo(currFlrDmdMatView.getUnitNo());
		unitwiseInfo.setConservancyTax(currFlrDmdMatView.getSewerageTax());

		if (currFlrDmdMatView.getWaterScheme() != null && !currFlrDmdMatView.getWaterScheme().equals(0)
				&& unitwiseInfo.getWaterScheme() == null) {
			unitwiseInfo.setWaterScheme(currFlrDmdMatView.getWaterScheme());
		}

		unitwiseInfo.setWaterTax(currFlrDmdMatView.getWaterTax());
		unitwiseInfo.setGeneralTax(currFlrDmdMatView.getGeneralTax());
		unitwiseInfo.setFireServiceTax(currFlrDmdMatView.getFireTax());
		unitwiseInfo.setLightTax(currFlrDmdMatView.getLightTax());
		/*
		 * Total of cons tax, water tax, general tax, fire tax and light tax
		 */
		unitwiseInfo.setTotalTax(totalTax);
		unitwiseInfo.setBigBuildingTax(currFlrDmdMatView.getBigBldgTax());

		// summing up the EduCessResd and EduCessNonResd
		unitwiseInfo.setEduCess(currFlrDmdMatView.getEduCessResdTax().add(currFlrDmdMatView.getEduCessNonResdTax()));

		unitwiseInfo.setEgsCess(currFlrDmdMatView.getEgsTax());

		if (unitwiseInfo.getAlv() == null || currFlrDmdMatView.getAlv() != null) {
			unitwiseInfo.setAlv(currFlrDmdMatView.getAlv());
		}

		return unitwiseInfo;
	}

	private UnitWiseInfo preparePropInfo(PropertyMaterlizeView propMatView) {
		UnitWiseInfo unitwiseInfo = new UnitWiseInfo();

		unitwiseInfo.setIndexNumber(propMatView.getPropertyId());
		unitwiseInfo.setHouseNo(propMatView.getHouseNo());
		unitwiseInfo.setOwnerName(propMatView.getOwnerName());
		if (propMatView.getPropTypeMstrID() != null) {
			PropertyTypeMaster propTypeMstr = propMatView.getPropTypeMstrID();

			/*
			 * Mixed Property Type is not considered here bcoz it exists in
			 * floor unit type (CurrFloorDmdCalcMaterializeView)
			 */
			if (PROPTYPE_NON_RESD.equals(propTypeMstr.getCode())) {
				unitwiseInfo.setPropType(NONRESD);
			} else if (PROPTYPE_OPEN_PLOT.equals(propTypeMstr.getCode())) {
				unitwiseInfo.setPropType(OPENPLOT);
			} else if (PROPTYPE_RESD.equals(propTypeMstr.getCode())
					|| PROPTYPE_CENTRAL_GOVT.equals(propTypeMstr.getCode())
					|| PROPTYPE_STATE_GOVT.equals(propTypeMstr.getCode())) {
				unitwiseInfo.setPropType(RESIDENTIAL);
			}
		}
		// Todo : needs to be removed as waterscheme is removed from propertyMaterializedview
		/*if (propMatView.getWaterScheme() != null && !propMatView.getWaterScheme().equals(0)) {
			unitwiseInfo.setWaterScheme(propMatView.getWaterScheme());
		}*/
		if (propMatView.getAlv() != null && !propMatView.getAlv().equals(BigDecimal.ZERO)) {
			unitwiseInfo.setAlv(propMatView.getAlv());
		}

		return unitwiseInfo;
	}

	private UnitWiseInfo prepareNonUnitWiseInfo(PropertyMaterlizeView propMatView, Installment currInstallment) {
		UnitWiseInfo unitwiseInfo = preparePropInfo(propMatView);
		InstDmdCollMaterializeView currInstDmdCollMatView = null;
		for (InstDmdCollMaterializeView instDmdColl : propMatView.getInstDmdColl()) {
			if (instDmdColl.getInstallment().equals(currInstallment)) {
				currInstDmdCollMatView = instDmdColl;
				break;
			}
		}
		/*
		 * Total of cons tax, water tax, general tax, fire tax and light tax
		 */
		BigDecimal totalTax = currInstDmdCollMatView.getSewerageTax().add(currInstDmdCollMatView.getWaterTax())
				.add(currInstDmdCollMatView.getGeneralTax()).add(currInstDmdCollMatView.getFireTax())
				.add(currInstDmdCollMatView.getLightTax());

		unitwiseInfo.setConservancyTax(currInstDmdCollMatView.getSewerageTax());
		unitwiseInfo.setWaterTax(currInstDmdCollMatView.getWaterTax());
		unitwiseInfo.setGeneralTax(currInstDmdCollMatView.getGeneralTax());
		unitwiseInfo.setFireServiceTax(currInstDmdCollMatView.getFireTax());
		unitwiseInfo.setLightTax(currInstDmdCollMatView.getLightTax());
		unitwiseInfo.setTotalTax(totalTax);
		unitwiseInfo.setBigBuildingTax(currInstDmdCollMatView.getBigBldgTax());

		unitwiseInfo.setEduCess(currInstDmdCollMatView.getEduCessResdTax().add(
				currInstDmdCollMatView.getEduCessNonResdTax()));
		unitwiseInfo.setEgsCess(currInstDmdCollMatView.getEgsTax());

		return unitwiseInfo;
	}

	public Long getZoneId() {
		return zoneId;
	}

	public void setZoneId(Long zoneId) {
		this.zoneId = zoneId;
	}

	public Long getWardId() {
		return wardId;
	}

	public void setWardId(Long wardId) {
		this.wardId = wardId;
	}

	@Override
	public String criteria() {
		return null;
	}

	@Override
	protected String getReportTemplateName() {
		return REPORT_TEMPLATENAME_JAMABANDI;
	}

	public String getPartNo() {
		return partNo;
	}

	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}

}

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
package org.egov.ptis.bean;

import org.apache.log4j.Logger;
import org.egov.commons.Installment;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.reporting.util.ReportUtil;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.property.UnitCalculationDetail;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.egov.ptis.client.util.PropertyTaxUtil.isNotNull;
import static org.egov.ptis.constants.PropertyTaxConstants.STYLE_TAG_BEGIN;
import static org.egov.ptis.constants.PropertyTaxConstants.STYLE_TAG_END;

/**
 * The property floor details object
 */
public class PropertyFloorDetailsInfo implements Comparable<PropertyFloorDetailsInfo> {
	
	private static final Logger LOGGER = Logger.getLogger(PropertyFloorDetailsInfo.class);
	private static final String HYPEN = "-";
	
	private UnitCalculationDetail unitCalcDetail;

	private BigDecimal fireServiceTax = BigDecimal.ZERO;
	private BigDecimal lightTax = BigDecimal.ZERO;
	private BigDecimal sewerageTax = BigDecimal.ZERO;
	private BigDecimal genTax = BigDecimal.ZERO;
	private BigDecimal waterTax = BigDecimal.ZERO;
	private BigDecimal eduCessResd = BigDecimal.ZERO;
	private BigDecimal eduCessNonResd = BigDecimal.ZERO;
	private BigDecimal bigResBldgTax = BigDecimal.ZERO;
	private BigDecimal empGrnteTax = BigDecimal.ZERO;
	
	private Boolean hasFireTaxChanged = Boolean.FALSE;
	private Boolean hasLightTaxChanged = Boolean.FALSE;
	private Boolean hasSewerageTaxChanged = Boolean.FALSE;
	private Boolean hasGenTaxChanged = Boolean.FALSE;
	private Boolean hasWaterTaxChanged = Boolean.TRUE;
	private Boolean hasResdEduCessChanged = Boolean.FALSE;
	private Boolean hasNonResdEduCessChanged = Boolean.FALSE;
	private Boolean hasEgCessChanged = Boolean.FALSE;
	private Boolean hasBigResdTaxtChanged = Boolean.FALSE;	
	
	private Boolean hasALVChanged = Boolean.FALSE;
	
	private String totalTax = "";
	private String totalServChrg = "";
	private String NOTAVAIL = "N/A";
	
	private DateFormat dateFormat = new SimpleDateFormat(PropertyTaxConstants.DATE_FORMAT_DDMMYYY);
	

	@Autowired
        private PropertyTaxUtil propertyTaxUtil;

	public PropertyFloorDetailsInfo(UnitCalculationDetail unitCalcDetail, String propType, Installment installment) {
		this.unitCalcDetail = unitCalcDetail;
		this.fireServiceTax = unitCalcDetail.getFireTax();
		this.lightTax = unitCalcDetail.getLightTax();
		this.sewerageTax = unitCalcDetail.getSewerageTax();
		this.genTax = unitCalcDetail.getGeneralTax();
		this.waterTax = unitCalcDetail.getWaterTax();
		this.eduCessResd = unitCalcDetail.getEduCessResd();
		this.eduCessNonResd = unitCalcDetail.getEduCessNonResd();
		this.bigResBldgTax = unitCalcDetail.getBigBuildingTax();
		this.empGrnteTax = unitCalcDetail.getEmpGrntCess();
		
		BigDecimal total = this.genTax.add(this.fireServiceTax).add(this.lightTax).add(this.waterTax)
				.add(sewerageTax);
		totalTax = total.toString();
		
		findTaxChange();
	}
	
	private void findTaxChange() {
		LOGGER.debug("Entered into findTaxChange unitCalcDetail=" + unitCalcDetail);
		
		if (isNotNull(unitCalcDetail.getFromDate())) {
			hasSewerageTaxChanged =  unitCalcDetail.getFromDate().equals(unitCalcDetail.getSewerageTaxFromDate()) ? true : false;
			hasGenTaxChanged = unitCalcDetail.getFromDate().equals(unitCalcDetail.getGeneralTaxFromDate()) ? true : false;
			hasWaterTaxChanged = unitCalcDetail.getFromDate().equals(unitCalcDetail.getWaterTaxFromDate()) ? true : false;
			hasFireTaxChanged = unitCalcDetail.getFromDate().equals(unitCalcDetail.getFireTaxFromDate())? true : false;
			hasLightTaxChanged = unitCalcDetail.getFromDate().equals(unitCalcDetail.getLightTaxFromDate())? true : false;

			if (eduCessResd.compareTo(BigDecimal.ZERO) > 0 ) {
				hasResdEduCessChanged = unitCalcDetail.getFromDate().equals(unitCalcDetail.getEduCessResdFromDate())? true : false;
			} else if (eduCessNonResd.compareTo(BigDecimal.ZERO) > 0 ) {
				hasNonResdEduCessChanged = unitCalcDetail.getFromDate().equals(unitCalcDetail.getEduCessNonResdFromDate())? true : false;
			}

			if (empGrnteTax.compareTo(BigDecimal.ZERO) > 0 ) {
				hasEgCessChanged = unitCalcDetail.getFromDate().equals(unitCalcDetail.getEmpGrntCessFromDate())? true : false;
			}
		} else {
			hasSewerageTaxChanged = hasGenTaxChanged = hasWaterTaxChanged = hasFireTaxChanged = hasLightTaxChanged = hasResdEduCessChanged = hasNonResdEduCessChanged = hasEgCessChanged = false;
			hasALVChanged = true;
		}
		
		LOGGER.debug("Exiting from findTaxChange");
	}
	
	
	/*public PropertyFloorDetailsInfo(UnitTaxCalculationInfo unit, String propType, Installment installment, Map<String, Date> taxAndMinEffDate) {
		this.unitCalcDetail = unit;
		//this.installment = installment;
		BigDecimal totalActualTaxAmt = BigDecimal.ZERO;
		BigDecimal totalTaxAmt = BigDecimal.ZERO;		
		DateFormat dateformatter = new SimpleDateFormat(PropertyTaxConstants.DATE_FORMAT_DDMMYYY);		
		Date unitTaxDate = null;
		
		try {
			unitTaxDate = dateformatter.parse(unit.getInstDate());			
		} catch (ParseException pe) {
			throw new ApplicationRuntimeException("Error while parsing unit effective date", pe);
		}
		
		for (MiscellaneousTax miscTax : unitDetail.getMiscellaneousTaxes()) {
			String reasonCode = miscTax.getTaxName();
			BigDecimal actualTaxAmt = BigDecimal.ZERO;
			BigDecimal taxAmt = BigDecimal.ZERO;

			if (propType != null && PROPTYPE_CENTGOVT_STR.equals(propType)) {
				actualTaxAmt = miscTax.getTotalActualTax(); // Tax after applying service charges of amenities
				taxAmt = miscTax.getTotalCalculatedTax(); // Tax before applying service charges of amenities
				actualTaxAmt = miscTax.getTaxDetails().get(0).getActualTaxValue();
				actualTaxAmt.setScale(0, ROUND_HALF_UP);
				taxAmt = miscTax.getTaxDetails().get(0).getCalculatedTaxValue();
				//taxAmt.setScale(0, ROUND_HALF_UP);
			} else {
				taxAmt = miscTax.getTotalCalculatedTax();
				//taxAmt = miscTax.getTaxDetails().get(0).getCalculatedTaxValue();
				//taxAmt.setScale(0, ROUND_HALF_UP);
			}

			if (DEMANDRSN_CODE_FIRE_SERVICE_TAX.equals(reasonCode)) {
				
				if (!taxAndMinEffDate.get(DEMANDRSN_CODE_FIRE_SERVICE_TAX).after(unitTaxDate)) {
					this.hasFireTaxChanged = (miscTax.getHasChanged() == null || miscTax.getHasChanged() == false) ? false
							: true;

					if (taxAmt.compareTo(ZERO) > 0) {
						totalTaxAmt = totalTaxAmt.add(taxAmt);
					}
					if (propType != null && PROPTYPE_CENTGOVT_STR.equals(propType)) {
						totalActualTaxAmt = totalActualTaxAmt.add(actualTaxAmt);
						this.fireServiceTax = actualTaxAmt;
					} else {
						this.fireServiceTax = taxAmt;
					}
				}
			} else if (DEMANDRSN_CODE_LIGHTINGTAX.equals(reasonCode)) {
				
				if (!taxAndMinEffDate.get(DEMANDRSN_CODE_LIGHTINGTAX).after(unitTaxDate)) {
					this.hasLightTaxChanged = (miscTax.getHasChanged() == null || miscTax.getHasChanged() == false) ? false
							: true;
					if (taxAmt.compareTo(ZERO) > 0) {
						totalTaxAmt = totalTaxAmt.add(taxAmt);
					}
					if (propType != null && PROPTYPE_CENTGOVT_STR.equals(propType)) {
						totalActualTaxAmt = totalActualTaxAmt.add(actualTaxAmt);
						this.lightTax = actualTaxAmt;
					} else {
						this.lightTax = taxAmt;
					}
				}
			} else 	if (DEMANDRSN_CODE_SEWERAGE_TAX.equals(reasonCode)) {
				this.hasSewerageTaxChanged = (miscTax.getHasChanged() == null || miscTax.getHasChanged() == false) ? false : true;
				if (taxAmt.compareTo(ZERO) > 0) {
					totalTaxAmt = totalTaxAmt.add(taxAmt);
				}
				if (propType != null && PROPTYPE_CENTGOVT_STR.equals(propType)) {
					totalActualTaxAmt = totalActualTaxAmt.add(actualTaxAmt);
					this.sewerageTax = actualTaxAmt;
				} else {
					this.sewerageTax = taxAmt;
				}
			} else if (DEMANDRSN_CODE_GENERAL_TAX.equals(reasonCode)) {
				this.hasGenTaxChanged = (miscTax.getHasChanged() == null ||	miscTax.getHasChanged() == false) ? false : true;
				if (taxAmt.compareTo(ZERO) > 0) {
					totalTaxAmt = totalTaxAmt.add(taxAmt);
				}
				if (propType != null && PROPTYPE_CENTGOVT_STR.equals(propType)) {
					totalActualTaxAmt = totalActualTaxAmt.add(actualTaxAmt);
					this.genTax = actualTaxAmt;
				} else {
					this.genTax = taxAmt;
				}
			} else if (DEMANDRSN_CODE_GENERAL_WATER_TAX.equals(reasonCode)) {
				this.hasWaterTaxChanged = (miscTax.getHasChanged() == null || miscTax.getHasChanged() == false) ? false : true;
				if (taxAmt.compareTo(ZERO) > 0) {
					totalTaxAmt = totalTaxAmt.add(taxAmt);
				}
				if (propType != null && PROPTYPE_CENTGOVT_STR.equals(propType)) {
					totalActualTaxAmt = totalActualTaxAmt.add(actualTaxAmt);
					this.waterTax = actualTaxAmt;
				} else {
					this.waterTax = taxAmt;
				}
			} else if (DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD.equals(reasonCode)) {
				this.hasResdEduCessChanged = (miscTax.getHasChanged() == null || miscTax.getHasChanged() == false) ? false : true;
				this.eduCessResd = this.eduCessResd.add(taxAmt);
			}  else if (DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD.equals(reasonCode)) {
				this.hasNonResdEduCessChanged = (miscTax.getHasChanged() == null || miscTax.getHasChanged() == false) ? false : true;
				this.eduCessNonResd = this.eduCessNonResd.add(taxAmt);
			}  else if (DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX.equals(reasonCode)) {
				if (!taxAndMinEffDate.get(DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX).after(unitTaxDate)) {
					this.hasBigResdTaxtChanged = (miscTax.getHasChanged() == null || miscTax.getHasChanged() == false) ? false
							: true;
					this.bigResBldgTax = taxAmt;
				}
			}  else if (DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX.equals(reasonCode)) {
				this.hasEgCessChanged = (miscTax.getHasChanged() == null || miscTax.getHasChanged() == false) ? false : true;
				this.empGrnteTax = taxAmt;
			}
		}

		totalActualTaxAmt = totalActualTaxAmt.setScale(2, BigDecimal.ROUND_HALF_UP);
		totalTaxAmt = totalTaxAmt.setScale(0, BigDecimal.ROUND_HALF_UP);
		totalTax = totalTaxAmt.toString();
		if (propType != null && PROPTYPE_CENTGOVT_STR.equals(propType)) {
			totalServChrg = totalActualTaxAmt.toString();
		} else {
			totalServChrg = "N/A";
		}
	}*/

	public Integer getUnitNum() {
		return unitCalcDetail.getUnitNumber();
	}

	public String getOccupantName() {
		return "N/A"; //unitCalcDetail.getUnitOccupier();
	}

	public String getOccupant() {
		return unitCalcDetail.getUnitOccupation();
	}
	
	public String getLightTax() {
		StringBuilder sb = new StringBuilder(100);

		if (this.lightTax.compareTo(BigDecimal.ZERO) == 0)
			return NOTAVAIL;
		else if (this.lightTax.compareTo(new BigDecimal("-1")) == 0){
			return HYPEN;
		} else if (hasLightTaxChanged) {
			sb.append(STYLE_TAG_BEGIN).append(ReportUtil.formatNumber(this.lightTax, 2, false)).append(STYLE_TAG_END);
		} else {
			sb.append(ReportUtil.formatNumber(this.lightTax, 2, false));
		}

		return sb.toString();
	}

	public String getFireServiceTax() {
		StringBuilder sb = new StringBuilder(100);

		if (this.fireServiceTax.compareTo(BigDecimal.ZERO) == 0)
			return NOTAVAIL;
		else if (this.fireServiceTax.compareTo(new BigDecimal("-1")) == 0){
			return HYPEN;
		} else if (hasFireTaxChanged) {
			sb.append(STYLE_TAG_BEGIN).append(ReportUtil.formatNumber(this.fireServiceTax, 2, false))
					.append(STYLE_TAG_END);
		} else {
			sb.append(ReportUtil.formatNumber(this.fireServiceTax, 2, false));
		}

		return sb.toString();
	}

	public String getSewerageTax() {
		StringBuilder sb = new StringBuilder(100);

		if (this.sewerageTax.compareTo(BigDecimal.ZERO) == 0)
			return NOTAVAIL;
		else if (this.sewerageTax.compareTo(new BigDecimal("-1")) == 0){
			return HYPEN;
		} else if (hasSewerageTaxChanged) {
			sb.append(STYLE_TAG_BEGIN).append(ReportUtil.formatNumber(this.sewerageTax, 2, false))
					.append(STYLE_TAG_END);
		} else {
			sb.append(ReportUtil.formatNumber(this.sewerageTax, 2, false));
		}

		return sb.toString();
	}

	public String getAlv() {
		StringBuilder sb = new StringBuilder();
		String ALV = ReportUtil.formatNumber(this.unitCalcDetail.getAlv(), 2, false);
		
		if (hasALVChanged) {
			sb.append(STYLE_TAG_BEGIN).append(ALV).append(STYLE_TAG_END);
		} else {
			sb.append(ALV);
		}

		if (unitCalcDetail.getResidentialALV().compareTo(BigDecimal.ZERO) > 0
				&& unitCalcDetail.getNonResidentialALV().compareTo(BigDecimal.ZERO) > 0) {

			sb.append("\nR-").append(this.unitCalcDetail.getResidentialALV())
			  .append("\nNR-").append(this.unitCalcDetail.getNonResidentialALV());
		}
		return sb.toString();
	}

	public String getGenTax() {
		StringBuilder sb = new StringBuilder(100);

		if (this.genTax.compareTo(BigDecimal.ZERO) == 0)
			return NOTAVAIL;
		else if (this.genTax.compareTo(new BigDecimal("-1")) == 0){
			return HYPEN;
		} else if (hasGenTaxChanged) {
			sb.append(STYLE_TAG_BEGIN).append(ReportUtil.formatNumber(this.genTax, 2, false)).append(STYLE_TAG_END);
		} else {
			sb.append(ReportUtil.formatNumber(this.genTax, 2, false));
		}

		return sb.toString();
	}

	public String getWaterTax() {
		StringBuilder sb = new StringBuilder(100);

		if (this.waterTax.compareTo(BigDecimal.ZERO) == 0)
			return NOTAVAIL;
		else if (this.waterTax.compareTo(new BigDecimal("-1")) == 0){
			return HYPEN;
		} else if (hasWaterTaxChanged) {
			sb.append(STYLE_TAG_BEGIN).append(ReportUtil.formatNumber(this.waterTax, 2, false)).append(STYLE_TAG_END);
		} else {
			sb.append(ReportUtil.formatNumber(this.waterTax, 2, false));
		}

		return sb.toString();
	}

	public String getEduCess() {
		StringBuilder sb = new StringBuilder(100);
		StringBuilder eduCessChanged = new StringBuilder(100);
		StringBuilder nrEduCessChanged = new StringBuilder(100);
		
		if (hasResdEduCessChanged) {
			eduCessChanged.append(STYLE_TAG_BEGIN).append(ReportUtil.formatNumber(this.eduCessResd, 2, false))
					.append(STYLE_TAG_END);
		} 
		
		if (hasNonResdEduCessChanged) {
			nrEduCessChanged.append(STYLE_TAG_BEGIN).append(ReportUtil.formatNumber(this.eduCessNonResd, 2, false))
					.append(STYLE_TAG_END);
		} 
		
		if (this.eduCessResd.compareTo(BigDecimal.ZERO) == 0 && this.eduCessNonResd.compareTo(BigDecimal.ZERO) == 0)
			return NOTAVAIL;
		else if (this.eduCessResd.compareTo(new BigDecimal("-1")) == 0 || this.eduCessNonResd.compareTo(new BigDecimal("-1")) == 0){
			return HYPEN;
		} else if (unitCalcDetail.getResidentialALV().compareTo(BigDecimal.ZERO) > 0
				&& unitCalcDetail.getNonResidentialALV().compareTo(BigDecimal.ZERO) > 0) {

			sb.append("\n");

			if (hasResdEduCessChanged) {
				sb.append(eduCessChanged);
			} else {
				sb.append(ReportUtil.formatNumber(this.eduCessResd, 2, false));
			}

			sb.append("\n");

			if (hasNonResdEduCessChanged) {
				sb.append(nrEduCessChanged);
			} else {
				sb.append(ReportUtil.formatNumber(this.eduCessNonResd, 2, false));
			}

		} else if (eduCessNonResd.compareTo(BigDecimal.ZERO) > 0) {

			if (hasNonResdEduCessChanged) {
				sb.append(eduCessChanged);
			} else {
				sb.append(ReportUtil.formatNumber(this.eduCessNonResd, 2, false));
			}

		} else if (eduCessResd.compareTo(BigDecimal.ZERO) > 0) {

			if (hasResdEduCessChanged) {
				sb.append(nrEduCessChanged);
			} else {
				sb.append(ReportUtil.formatNumber(this.eduCessResd, 2, false));
			}
		}

		return sb.toString();
	}

	public String getBigResBldgTax() {
		StringBuilder sb = new StringBuilder(100);

		if (this.bigResBldgTax.compareTo(BigDecimal.ZERO) == 0)
			return NOTAVAIL;
		else if (this.bigResBldgTax.compareTo(new BigDecimal("-1")) == 0){
			return HYPEN;
		} else if (hasBigResdTaxtChanged) {
			sb.append(STYLE_TAG_BEGIN).append(ReportUtil.formatNumber(this.bigResBldgTax, 2, false))
					.append(STYLE_TAG_END);
		} else {
			sb.append(ReportUtil.formatNumber(this.bigResBldgTax, 2, false));
		}

		return sb.toString();
	}

	public String getEmpGrnteTax() {
		StringBuilder sb = new StringBuilder(100);
		StringBuilder stringWithBoldStyle = new StringBuilder(100);
		stringWithBoldStyle.append(STYLE_TAG_BEGIN).append(ReportUtil.formatNumber(this.empGrnteTax, 2, false))
				.append(STYLE_TAG_END);

		if (this.empGrnteTax.compareTo(BigDecimal.ZERO) == 0)
			return NOTAVAIL;
		else if (this.empGrnteTax.compareTo(new BigDecimal("-1")) == 0){
			return HYPEN;
		} else if (unitCalcDetail.getResidentialALV().compareTo(BigDecimal.ZERO) > 0
				&& unitCalcDetail.getNonResidentialALV().compareTo(BigDecimal.ZERO) > 0) {
			sb.append("\n").append(NOTAVAIL).append("\n");
			if (hasEgCessChanged) {
				sb.append(stringWithBoldStyle);
			} else {
				sb.append(ReportUtil.formatNumber(this.empGrnteTax, 2, false));
			}
		} else {
			if (hasEgCessChanged) {
				sb.append(stringWithBoldStyle);
			} else {
				sb.append(ReportUtil.formatNumber(this.empGrnteTax, 2, false));
			}
		}

		return sb.toString();
	}

	public String getTotalTax() {
		return totalTax;
	}

	public String getTotalServChrg() {
		return unitCalcDetail.getServiceCharge().compareTo(BigDecimal.ZERO) == 0 ? NOTAVAIL : unitCalcDetail
				.getServiceCharge().toString();
	}

	public String getInstDate() {
		if (PropertyTaxUtil.isNotNull(unitCalcDetail.getFromDate())) {
			return dateFormat.format(unitCalcDetail.getFromDate());
		}
		
		Installment installment = propertyTaxUtil.getPTInstallmentForDate(unitCalcDetail.getInstallmentFromDate());
		
		String dateString = dateFormat.format(unitCalcDetail.getInstallmentFromDate());
		
		if (propertyTaxUtil.between(unitCalcDetail.getOccupancyDate(), installment.getFromDate(),
				installment.getToDate())) {
			if (unitCalcDetail.getOccupancyDate().after(unitCalcDetail.getGuidValEffectiveDate())) {
				dateString = dateFormat.format(unitCalcDetail.getOccupancyDate());
			} else {
				dateString = dateFormat.format(unitCalcDetail.getGuidValEffectiveDate());
			}
		} else if (propertyTaxUtil.between(unitCalcDetail.getGuidValEffectiveDate(), installment.getFromDate(),
				installment.getToDate())) {
			dateString = dateFormat.format(unitCalcDetail.getGuidValEffectiveDate());
		} 
		
		return dateString;
	}

	public String getUnitOccupation() {
		return unitCalcDetail.getUnitOccupation();
	}

	public BigDecimal getFireTaxValue() {
		return fireServiceTax;
	}
	
	public BigDecimal getLightTaxValue() {
		return lightTax;
	}
	
	public BigDecimal getSewerageTaxValue() {
		return sewerageTax;
	}
	
	public BigDecimal getGeneralTaxValue() {
		return genTax;
	}
	
	public BigDecimal getWaterTaxValue() {
		return waterTax;
	}
	
	public BigDecimal getEduCessResdValue() {
		return eduCessResd;
	}
	
	public BigDecimal getEduCessNonResdValue() {
		return eduCessNonResd;
	}
	
	public BigDecimal getBigResBldgTaxValue() {
		return bigResBldgTax;
	}
	
	public BigDecimal getEmpGrnteTaxValue() {
		return empGrnteTax;
	}
	/*
     * FIXME PHOENIX use HashCodeBuilder from apache commons 
     * override equals method as well with EqualsBuilder
     * 
     * @Override
	public boolean equals(Object object) {
		
		if (object == null) {
			return false;
		}
		
 		if (!(object instanceof PropertyFloorDetailsInfo)) {
			return false;
		}
		
		PropertyFloorDetailsInfo floorDetailsInfo = (PropertyFloorDetailsInfo) object;
		
		if (this.getUnitNum().equals(floorDetailsInfo.getUnitNum()) 
				&& this.unitCalcDetail.getAlv().compareTo(
						floorDetailsInfo.unitCalcDetail.getAlv()) == 0
				&& this.getInstDate().equals(floorDetailsInfo.getInstDate())
				&& this.getUnitOccupation().equals(floorDetailsInfo.getUnitOccupation())
				&& this.bigResBldgTax.compareTo(floorDetailsInfo.getBigResBldgTaxValue()) == 0
				&& this.eduCessNonResd.compareTo(floorDetailsInfo.getEduCessNonResdValue()) == 0
				&& this.eduCessResd.compareTo(floorDetailsInfo.getEduCessResdValue()) == 0
				&& this.empGrnteTax.compareTo(floorDetailsInfo.getEmpGrnteTaxValue()) == 0
				&& this.fireServiceTax.compareTo(floorDetailsInfo.getFireTaxValue()) == 0
				&& this.genTax.compareTo(floorDetailsInfo.getGeneralTaxValue()) == 0
				&& this.lightTax.compareTo(floorDetailsInfo.getLightTaxValue()) == 0
				&& this.sewerageTax.compareTo(floorDetailsInfo.getSewerageTaxValue()) == 0
				&& this.waterTax.compareTo(floorDetailsInfo.getWaterTaxValue()) == 0) {
			return true;
		}
		
		return false;
		
	}
	
	@Override
    public int hashCode() {

        int seedValue = HashCodeUtil.SEED;

        seedValue = HashCodeUtil.hash(seedValue, this.getUnitNum());
        seedValue = HashCodeUtil.hash(seedValue, this.unitCalcDetail.getAlv());
        seedValue = HashCodeUtil.hash(seedValue, this.getInstDate());
        seedValue = HashCodeUtil.hash(seedValue, this.getUnitOccupation());
        seedValue = HashCodeUtil.hash(seedValue, this.getBigResBldgTaxValue());
        seedValue = HashCodeUtil.hash(seedValue, this.getEduCessNonResdValue());
        seedValue = HashCodeUtil.hash(seedValue, this.getEduCessResdValue());
        seedValue = HashCodeUtil.hash(seedValue, this.getEmpGrnteTaxValue());
        seedValue = HashCodeUtil.hash(seedValue, this.getFireTaxValue());
        seedValue = HashCodeUtil.hash(seedValue, this.getGeneralTaxValue());
        seedValue = HashCodeUtil.hash(seedValue, this.getLightTaxValue());
        seedValue = HashCodeUtil.hash(seedValue, this.getSewerageTaxValue());
        seedValue = HashCodeUtil.hash(seedValue, this.getWaterTaxValue());

        return seedValue;

    }
*/
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("PropertyFloorDetailsInfo[")
			.append("UnitNo=").append(getUnitNum())
			.append(", unitOccupation=").append(this.getUnitOccupation())
			.append(", bigResBldgTax=").append(this.bigResBldgTax)
			.append(", eduCessonResd=").append(this.eduCessNonResd)
			.append(", eduCessResd=").append(this.eduCessResd)
			.append(", empGrnteTax=").append(this.empGrnteTax)
			.append(", fireServiceTax=").append(this.fireServiceTax)
			.append(", genTax=").append(this.genTax)
			.append(", lightTax=").append(this.lightTax)
			.append(", sewerageTax=").append(this.sewerageTax)
			.append(", waterTax=").append(this.waterTax)
			.append(", instDate=").append(getInstDate())
			.append("]");
		return str.toString();
	}

	@Override
	public int compareTo(PropertyFloorDetailsInfo o) {
		
		int result = 0;
		
		Date thisDate = null;
		Date otherDate = null;

		try {
			thisDate = dateFormat.parse(this.getInstDate());
			otherDate = dateFormat.parse(o.getInstDate());
		} catch (ParseException e) {
			LOGGER.error("Error while parsing property floor details inst date", e);
			throw new ApplicationRuntimeException("Error while parsing property floor details inst date", e);
		}
		
		result = thisDate.compareTo(otherDate); 
		
		if (result == 0) {
			result = this.getUnitNum().compareTo(o.getUnitNum());
		}
		
		return result;
	}
}

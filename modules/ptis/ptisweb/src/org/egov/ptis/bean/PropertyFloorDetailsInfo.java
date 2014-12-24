package org.egov.ptis.bean;

import static java.math.BigDecimal.ZERO;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_FIRE_SERVICE_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_GENERAL_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_GENERAL_WATER_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_LIGHTINGTAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_SEWERAGE_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPTYPE_CENTGOVT_STR;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.STYLE_TAG_BEGIN;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.STYLE_TAG_END;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.Installment;
import org.egov.infstr.reporting.util.ReportUtil;
import org.egov.ptis.nmc.constants.NMCPTISConstants;
import org.egov.ptis.nmc.model.MiscellaneousTax;
import org.egov.ptis.nmc.model.UnitTaxCalculationInfo;
import org.egov.ptis.nmc.util.PropertyTaxUtil;

/**
 * The property floor details object
 */
public class PropertyFloorDetailsInfo {
	private UnitTaxCalculationInfo unitDetail;

	private BigDecimal fireServiceTax = BigDecimal.ZERO;
	private BigDecimal lightTax = BigDecimal.ZERO;
	private BigDecimal sewerageTax = BigDecimal.ZERO;
	private BigDecimal genTax = BigDecimal.ZERO;
	private BigDecimal waterTax = BigDecimal.ZERO;
	private BigDecimal eduCessResd = BigDecimal.ZERO;
	private BigDecimal eduCessNonResd = BigDecimal.ZERO;
	private BigDecimal bigResBldgTax = BigDecimal.ZERO;
	private BigDecimal empGrnteTax = BigDecimal.ZERO;
	private String totalTax = "";
	private String totalServChrg = "";
	private String NOTAVAIL = "N/A";
	private static final String HYPEN = "-";

	private Boolean hasFireTaxChanged = Boolean.FALSE;
	private Boolean hasLightTaxChanged = Boolean.FALSE;
	private Boolean hasSewerageTaxChanged = Boolean.FALSE;
	private Boolean hasGenTaxChanged = Boolean.FALSE;
	private Boolean hasWaterTaxChanged = Boolean.TRUE;
	private Boolean hasResdEduCessChanged = Boolean.FALSE;
	private Boolean hasNonResdEduCessChanged = Boolean.FALSE;
	private Boolean hasEgCessChanged = Boolean.FALSE;
	private Boolean hasBigResdTaxtChanged = Boolean.FALSE;	

	private PropertyTaxUtil propertyTaxUtil = new PropertyTaxUtil();
	//private Installment installment;

	public PropertyFloorDetailsInfo(UnitTaxCalculationInfo unit, String propType, Installment installment, Map<String, Date> taxAndMinEffDate) {
		this.unitDetail = unit;
		//this.installment = installment;
		BigDecimal totalActualTaxAmt = BigDecimal.ZERO;
		BigDecimal totalTaxAmt = BigDecimal.ZERO;		
		DateFormat dateformatter = new SimpleDateFormat(NMCPTISConstants.DATE_FORMAT_DDMMYYY);		
		Date unitTaxDate = null;
		
		try {
			unitTaxDate = dateformatter.parse(unit.getInstDate());			
		} catch (ParseException pe) {
			throw new EGOVRuntimeException("Error while parsing unit effective date", pe);
		}
		
		for (MiscellaneousTax miscTax : unitDetail.getMiscellaneousTaxes()) {
			String reasonCode = miscTax.getTaxName();
			BigDecimal actualTaxAmt = BigDecimal.ZERO;
			BigDecimal taxAmt = BigDecimal.ZERO;

			if (propType != null && PROPTYPE_CENTGOVT_STR.equals(propType)) {
				actualTaxAmt = miscTax.getTotalActualTax(); // Tax after applying service charges of amenities
				taxAmt = miscTax.getTotalCalculatedTax(); // Tax before applying service charges of amenities
				/*actualTaxAmt = miscTax.getTaxDetails().get(0).getActualTaxValue();
				actualTaxAmt.setScale(0, ROUND_HALF_UP);
				taxAmt = miscTax.getTaxDetails().get(0).getCalculatedTaxValue();*/
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
	}

	public Integer getUnitNum() {
		return unitDetail.getUnitNumber();
	}

	public String getOccupantName() {
		return unitDetail.getUnitOccupier();
	}

	public String getOccupant() {
		return unitDetail.getUnitOccupation();
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
		String ALV = ReportUtil.formatNumber(this.unitDetail.getAnnualRentAfterDeduction(), 2, false);

		if (unitDetail.getHasALVChanged()) {
			sb.append(STYLE_TAG_BEGIN).append(ALV).append(STYLE_TAG_END);
		} else {
			sb.append(ALV);
		}

		if (unitDetail.getResidentialALV().compareTo(BigDecimal.ZERO) > 0
				&& unitDetail.getNonResidentialALV().compareTo(BigDecimal.ZERO) > 0) {

			sb.append("\nR-").append(this.unitDetail.getResidentialALV()).append("\nNR-")
					.append(this.unitDetail.getNonResidentialALV());
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

		if (this.eduCessResd.compareTo(BigDecimal.ZERO) == 0 && this.eduCessNonResd.compareTo(BigDecimal.ZERO) == 0)
			return NOTAVAIL;
		else if (this.eduCessResd.compareTo(new BigDecimal("-1")) == 0 || this.eduCessNonResd.compareTo(new BigDecimal("-1")) == 0){
			return HYPEN;
		} else if (unitDetail.getResidentialALV().compareTo(BigDecimal.ZERO) > 0
				&& unitDetail.getNonResidentialALV().compareTo(BigDecimal.ZERO) > 0) {

			sb.append("\n");

			if (hasResdEduCessChanged) {
				sb.append(STYLE_TAG_BEGIN).append(ReportUtil.formatNumber(this.eduCessResd, 2, false))
						.append(STYLE_TAG_END);
			} else {
				sb.append(ReportUtil.formatNumber(this.eduCessResd, 2, false));
			}

			sb.append("\n");

			if (hasNonResdEduCessChanged) {
				sb.append(STYLE_TAG_BEGIN).append(ReportUtil.formatNumber(this.eduCessNonResd, 2, false))
						.append(STYLE_TAG_END);
			} else {
				sb.append(ReportUtil.formatNumber(this.eduCessNonResd, 2, false));
			}

		} else if (eduCessNonResd.compareTo(BigDecimal.ZERO) == 0) {

			if (hasResdEduCessChanged) {
				sb.append(STYLE_TAG_BEGIN).append(ReportUtil.formatNumber(this.eduCessResd, 2, false))
						.append(STYLE_TAG_END);
			} else {
				sb.append(ReportUtil.formatNumber(this.eduCessResd, 2, false));
			}

		} else if (eduCessResd.compareTo(BigDecimal.ZERO) == 0) {

			if (hasNonResdEduCessChanged) {
				sb.append(STYLE_TAG_BEGIN).append(ReportUtil.formatNumber(this.eduCessNonResd, 2, false))
						.append(STYLE_TAG_END);
			} else {
				sb.append(ReportUtil.formatNumber(this.eduCessNonResd, 2, false));
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
		} else if (unitDetail.getResidentialALV().compareTo(BigDecimal.ZERO) > 0
				&& unitDetail.getNonResidentialALV().compareTo(BigDecimal.ZERO) > 0) {
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

	public String getEffectiveAssessment() {
		return propertyTaxUtil.getEffectiveAssessmentPeriod(unitDetail.getOccpancyDate());
	}

	public String getTotalTax() {
		return totalTax;
	}

	public String getTotalServChrg() {
		return totalServChrg;
	}

	public String getInstDate() {
		return unitDetail.getInstDate();
	}

	public String getUnitOccupation() {
		return unitDetail.getUnitOccupation();
	}

	/*public String getInstallment() {
		return "";
	}*/
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("PropertyFloorDetailsInfo[").append("UnitNo=").append(getUnitNum()).append(", instDate").append(getInstDate())
				.append("hasFireTaxChanged=").append(hasFireTaxChanged).append(", ")
				.append("hasLightTaxChanged=").append(hasLightTaxChanged).append(", ").append("hasSewerageTaxChanged=")
				.append(hasSewerageTaxChanged).append(", ").append("hasGenTaxChanged=").append(hasGenTaxChanged)
				.append(", ").append("hasWaterTaxChanged=").append(hasWaterTaxChanged).append(", ")
				.append("hasResdEduCessChanged=").append(hasResdEduCessChanged).append(", ")
				.append("hasNonResdEduCessChanged=").append(hasNonResdEduCessChanged).append(", ")
				.append("hasEgCessChanged=").append(hasEgCessChanged).append(", ").append("hasBigResdTaxtChanged=")
				.append(hasBigResdTaxtChanged).append("]");
		return str.toString();
	}
}

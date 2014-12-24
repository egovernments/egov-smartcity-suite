package org.egov.ptis.actions.reports;

import static java.util.Calendar.YEAR;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPTYPE_CENTRAL_GOVT;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPTYPE_NON_RESD;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPTYPE_OPEN_PLOT;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPTYPE_RESD;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPTYPE_STATE_GOVT;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.REPORT_TEMPLATENAME_JAMABANDI;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.REVENUE_HIERARCHY_TYPE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.WARD_BNDRY_TYPE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.ZONE_BNDRY_TYPE;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.Installment;
import org.egov.infstr.reporting.engine.ReportRequest.ReportDataSourceType;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.BoundaryDAO;
import org.egov.ptis.bean.ReportInfo;
import org.egov.ptis.bean.UnitWiseInfo;
import org.egov.ptis.domain.entity.property.CurrFloorDmdCalcMaterializeView;
import org.egov.ptis.domain.entity.property.PropertyMaterlizeView;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.egov.ptis.nmc.util.PropertyTaxUtil;
import org.egov.web.actions.ReportFormAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.hibernate.Criteria;

@ParentPackage("egov")
public class JamabandiReportAction extends ReportFormAction {
	private final Logger LOGGER = Logger.getLogger(getClass());
	private Integer zoneId;
	private Integer wardId;
	public static final String RESIDENTIAL = "R";
	public static final String NONRESD = "NR";
	public static final String OPENPLOT = "OP";
	public static final String SGOVT = "SGOVT";
	public static final String CGOVT = "CGOVT";
	
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
	@ValidationErrorPage(value=INDEX)
	public String generateJamabandi() {
		BoundaryDAO boundaryDAO = new BoundaryDAO();
		ReportInfo reportInfo = new ReportInfo();
		List<UnitWiseInfo> unitWiseInfoList = new ArrayList<UnitWiseInfo>();
		
		if ((zoneId != null && zoneId != -1) && (wardId != null && wardId != -1)) {
			String strZoneNum = (boundaryDAO.getBoundary(zoneId).getBoundaryNum()).toString();
			String strWardNum = (boundaryDAO.getBoundary(wardId).getBoundaryNum()).toString();
			List<PropertyMaterlizeView> propMatViewList = getPersistenceService()
					.getSession()
					.createQuery(
							"from PropertyMaterlizeView pmv left join fetch pmv.currFloorDmdCalc left join fetch pmv.propTypeMstrID where pmv.zoneID=? and pmv.wardID=? order by pmv.propertyId")
					.setParameter(0, zoneId).setParameter(1, wardId)
					.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
			LOGGER.debug("PropertyMaterlizeView List Size"+propMatViewList.size());
			
			for (PropertyMaterlizeView propMatView : propMatViewList) {
				if (propMatView.getCurrFloorDmdCalc() == null || propMatView.getCurrFloorDmdCalc().isEmpty() 
						|| propMatView.getCurrFloorDmdCalc().size() == 0) {
					UnitWiseInfo unitWiseInfo = preparePropInfo(propMatView);
					unitWiseInfoList.add(unitWiseInfo);
				}
				for (CurrFloorDmdCalcMaterializeView currFlrDmdMatView : propMatView.getCurrFloorDmdCalc()) {
					UnitWiseInfo unitWiseInfo = prepareUnitWiseInfo(currFlrDmdMatView);
					unitWiseInfoList.add(unitWiseInfo);
				}
			}
			Installment currentInstallment = PropertyTaxUtil.getCurrentInstallment();
			
			Calendar installmentFromDate = Calendar.getInstance();
			installmentFromDate.setTime(currentInstallment.getFromDate());
			
			Calendar installmentToDate = Calendar.getInstance();
			installmentToDate.setTime(currentInstallment.getToDate());
			
			reportInfo.setCurrInstallment(installmentFromDate.get(YEAR) + "-" + installmentToDate.get(YEAR));
			
			reportInfo.setZoneNo(strZoneNum);
			reportInfo.setWardNo(strWardNum);
			reportInfo.setUnitWiseInfoList(unitWiseInfoList);
			
			setDataSourceType(ReportDataSourceType.JAVABEAN);
			setReportData(reportInfo);
			super.report();
		}
		return "generate";
	}
	
	public Integer getZoneId() {
		return zoneId;
	}

	public void setZoneId(Integer zoneId) {
		this.zoneId = zoneId;
	}

	public Integer getWardId() {
		return wardId;
	}

	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}
	
	private UnitWiseInfo prepareUnitWiseInfo(CurrFloorDmdCalcMaterializeView currFlrDmdMatView) {
		UnitWiseInfo unitwiseInfo = preparePropInfo(currFlrDmdMatView.getPropMatView());
		
		BigDecimal totalTax = currFlrDmdMatView.getSewerageTax().add(currFlrDmdMatView.getWaterTax())
				.add(currFlrDmdMatView.getGeneralTax()).add(currFlrDmdMatView.getFireTax()).add(currFlrDmdMatView.getLightTax());
		
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
		unitwiseInfo.setTotalTax(totalTax); //Total of cons tax, water tax, general tax, fire tax and light tax  
		unitwiseInfo.setBigBuildingTax(currFlrDmdMatView.getBigBldgTax());
		if (currFlrDmdMatView.getEduCessResdTax().equals(BigDecimal.ZERO)) {
			unitwiseInfo.setEduCess(currFlrDmdMatView.getEduCessResdTax());
		} else {
			unitwiseInfo.setEduCess(currFlrDmdMatView.getEduCessNonResdTax());
		}
		unitwiseInfo.setEgsCess(currFlrDmdMatView.getEgsTax());
		
		if (unitwiseInfo.getAlv() == null || currFlrDmdMatView.getAlv() != null ) {
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
			
			//Mixed Property Type is not considered here bcoz it exists in floor unit type (CurrFloorDmdCalcMaterializeView)
			if (PROPTYPE_NON_RESD.equals(propTypeMstr.getCode())) {
				unitwiseInfo.setPropType(NONRESD);
			}
			else if (PROPTYPE_OPEN_PLOT.equals(propTypeMstr.getCode())) {
				unitwiseInfo.setPropType(OPENPLOT);
			}
			else if (PROPTYPE_RESD.equals(propTypeMstr.getCode()) || PROPTYPE_CENTRAL_GOVT.equals(propTypeMstr.getCode()) 
					|| PROPTYPE_STATE_GOVT.equals(propTypeMstr.getCode())) {
				unitwiseInfo.setPropType(RESIDENTIAL);
			}
		}
		
		if (propMatView.getWaterScheme() != null && !propMatView.getWaterScheme().equals(0)) { 
			unitwiseInfo.setWaterScheme(propMatView.getWaterScheme());
		}
		if (propMatView.getAlv() != null && !propMatView.getAlv().equals(BigDecimal.ZERO)) {
			unitwiseInfo.setAlv(propMatView.getAlv());
		}
		
		return unitwiseInfo;
	}
	
	@Override
	public String criteria() {
		return null;
	}

	@Override
	protected String getReportTemplateName() {
		return REPORT_TEMPLATENAME_JAMABANDI;
	}

}
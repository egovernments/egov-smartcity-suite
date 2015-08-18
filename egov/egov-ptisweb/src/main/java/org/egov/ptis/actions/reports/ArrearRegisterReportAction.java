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
import static org.egov.ptis.constants.PropertyTaxConstants.ADMIN_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.LOCALITY;
import static org.egov.ptis.constants.PropertyTaxConstants.LOCATION_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.REPORT_TEMPLATENAME_ARREARREGISTER;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.reporting.engine.ReportRequest.ReportDataSourceType;
import org.egov.infra.web.struts.actions.ReportFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.ptis.bean.PropertyWiseArrearInfo;
import org.egov.ptis.bean.ReportInfo;
import org.egov.ptis.domain.entity.property.InstDmdCollMaterializeView;
import org.egov.ptis.domain.entity.property.PropertyMaterlizeView;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.validator.annotations.Validations;

@SuppressWarnings("serial")
@ParentPackage("egov")
@Validations
@Results({ @Result(name = ArrearRegisterReportAction.INDEX, location = "arrearRegisterReport-index.jsp"),
    @Result(name = ArrearRegisterReportAction.GENERATE, location = "arrearRegisterReport-generate.jsp")})
public class ArrearRegisterReportAction extends ReportFormAction {
	private final Logger LOGGER = Logger.getLogger(getClass());
	private Long zoneId;
	private Long wardId;
	private Long areaId;
	private Long localityId;
	public static final String GENERATE = "generate";
	 @Autowired
        private BoundaryService boundaryService;
	 @Autowired
	private FinancialYearDAO financialYearDAO;
	private List<PropertyWiseArrearInfo> propertyWiseInfoList;
	 
	@SuppressWarnings("unchecked")
	public void prepare() {
		LOGGER.debug("Entered into prepare method");
		super.prepare();
		final List<Boundary> localityList = boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(
		                LOCALITY, LOCATION_HIERARCHY_TYPE);
		addDropdownData("localityList", localityList);
		
		List<Boundary> zoneList = boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName("Zone",ADMIN_HIERARCHY_TYPE);
		addDropdownData("Zone", zoneList);
		LOGGER.debug("Zone id : " + zoneId + ", " + "Ward id : " + wardId);
		prepareWardDropDownData(zoneId != null, wardId != null);
		if (wardId == null || wardId.equals(-1)) {
			addDropdownData("blockList", Collections.EMPTY_LIST);
		}
		prepareBlockDropDownData(wardId != null, areaId != null);
		LOGGER.debug("Exit from prepare method");
	}

	@SuppressWarnings("unchecked")
	private void prepareWardDropDownData(boolean zoneExists, boolean wardExists) {
		LOGGER.debug("Entered into prepareWardDropDownData method");
		LOGGER.debug("Zone Exists ? : " + zoneExists + ", " + "Ward Exists ? : " + wardExists);
		if (zoneExists && wardExists) {
			List<Boundary> wardList = new ArrayList<Boundary>();
			wardList = boundaryService.getActiveChildBoundariesByBoundaryId(getZoneId());
			addDropdownData("wardList", wardList);
		} else {
			addDropdownData("wardList", Collections.EMPTY_LIST);
		}
		LOGGER.debug("Exit from prepareWardDropDownData method");
	}
	
	@SuppressWarnings("unchecked")
        private void prepareBlockDropDownData(boolean wardExists, boolean blockExists) {
                LOGGER.debug("Entered into prepareBlockDropDownData method");
                LOGGER.debug("Ward Exists ? : " + wardExists + ", " + "Block Exists ? : " + blockExists);
                if (wardExists && blockExists) {
                        List<Boundary> blockList = new ArrayList<Boundary>();
                        blockList = boundaryService.getActiveChildBoundariesByBoundaryId(getWardId());
                        addDropdownData("blockList", blockList);
                } else {
                        addDropdownData("blockList", Collections.EMPTY_LIST);
                }
                LOGGER.debug("Exit from prepareWardDropDownData method");
        }

	@SkipValidation
	@Action(value = "/reports/arrearRegisterReport-index")
	public String index() {
		return INDEX;
	}

	@SuppressWarnings("unchecked")
	@ValidationErrorPage(value = INDEX)
	@Action(value = "/reports/arrearRegisterReport-generateArrearReport")
	public String generateArrearReport() {
		ReportInfo reportInfo = new ReportInfo();
		propertyWiseInfoList = new ArrayList<PropertyWiseArrearInfo>();
		// Get current financial year
		CFinancialYear finYear=financialYearDAO.getFinYearByDate(new Date());
			String strZoneNum = null, strWardNum = null, strBlockNum = null,strLocalityNum = null;
			StringBuffer query = new StringBuffer(300);
			
			// Query that retrieves all the properties that has arrears.
			query.append("select distinct pmv from PropertyMaterlizeView pmv,InstDmdCollMaterializeView idc where "
			        + "pmv.basicPropertyID = idc.propMatView.basicPropertyID and idc.installment.fromDate not between  ('"+finYear.getStartingDate()+"') and ('"+finYear.getEndingDate()+"') ");
			
    			if ((localityId == null || localityId==-1) && (zoneId != null && zoneId!=-1)) {
    			        strZoneNum = boundaryService.getBoundaryById(zoneId).getName();
                                query.append(" and pmv.zone.id=? ");
    			} else if(localityId != null && localityId!=-1){
    			    strLocalityNum = boundaryService.getBoundaryById(localityId).getName();
                            query.append(" and pmv.locality.id=? ");
                            if (zoneId != null && zoneId!=-1) {
                                strZoneNum = boundaryService.getBoundaryById(zoneId).getName();
                                query.append(" and pmv.zone.id=? ");
                            }
                        }
                        if(wardId != null && wardId!=-1){
                            strWardNum = boundaryService.getBoundaryById(wardId).getName();
                            query.append("  and pmv.ward.id=? ");
                        }
                        if(areaId != null && areaId!=-1){
                            strBlockNum = boundaryService.getBoundaryById(areaId).getName();
                            query.append("  and pmv.block.id=? ");
                        }
                            
                        query.append(" order by pmv.basicPropertyID ");
			Query qry = getPersistenceService().getSession().createQuery(query.toString()); 
			if ((localityId == null || localityId==-1) && (zoneId != null && zoneId!=-1)) {
			    if (zoneId != null && zoneId!=-1) {
                                qry.setParameter(0, zoneId);
                            }
			    if(wardId != null && wardId!=-1){
	                        qry.setParameter(1, wardId);
	                    }
	                    if(areaId != null && areaId!=-1){
	                        qry.setParameter(2, areaId);
	                    }
			} else if(localityId != null && localityId!=-1){
			    qry.setParameter(0, localityId); 
			    if (zoneId != null && zoneId!=-1) {
                                qry.setParameter(1, zoneId);
			    }
			    if(wardId != null && wardId!=-1){
                                qry.setParameter(2, wardId);
                            }
                            if(areaId != null && areaId!=-1){
                                qry.setParameter(3, areaId);
                            }
			}
                        
			
			List<PropertyMaterlizeView> propertyViewList = qry.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
					.list();
			LOGGER.debug("PropertyMaterlizeView List Size" + propertyViewList.size());

			for (PropertyMaterlizeView propMatView : propertyViewList) {
					// If there is only one Arrear Installment 
					if (propMatView.getInstDmdColl().size() == 1) {
					    InstDmdCollMaterializeView currIDCMatView = propMatView.getInstDmdColl()
								.iterator().next();
						PropertyWiseArrearInfo propertyWiseInfo = preparePropertyWiseInfo(currIDCMatView);
						if(propertyWiseInfo!=null){
						    propertyWiseInfo.setIsLastUnit(TRUE);
						    propertyWiseInfoList.add(propertyWiseInfo);
						}
					} else {
						// if there are more than one arrear Installments
						List<InstDmdCollMaterializeView> idcList = new ArrayList<InstDmdCollMaterializeView>(
								propMatView.getInstDmdColl());
						List unitList = new ArrayList();
						PropertyWiseArrearInfo propertyWiseInfoTotal = null;
						/**
						 * unitList list has the list of unit number for that
						 * particular Assessment/Index number unitWiseInfoTotal has the
						 * grouped info of unitwise if the unit numbers are same
						 * for that particular index num
						 */
						for (InstDmdCollMaterializeView instlDmdColMatView : idcList) {
							PropertyWiseArrearInfo propertyWiseInfo = preparePropertyWiseInfo(instlDmdColMatView);
							if(propertyWiseInfo!=null){
							// initially the block is executed
							if (unitList.size() == 0) {
								unitList.add(propertyWiseInfo.getArrearInstallmentYear());
								propertyWiseInfoTotal = propertyWiseInfo;
							}  
							// executed for second installment in same financial year
							else if (unitList.contains(propertyWiseInfo.getArrearInstallmentYear())) {
									propertyWiseInfoTotal = addPropertyWiseInfo(propertyWiseInfoTotal, propertyWiseInfo);
							}
							// executed for different financial year - ie first installment 
							else if (!unitList.contains(propertyWiseInfo.getArrearInstallmentYear())) {
							    
							            propertyWiseInfoList.add(propertyWiseInfoTotal);
							            unitList.add(propertyWiseInfo.getArrearInstallmentYear());
							            propertyWiseInfoTotal=propertyWiseInfo;
							            propertyWiseInfoTotal.setIndexNumber("");
                                                                    propertyWiseInfoTotal.setOwnerName("");
                                                                    propertyWiseInfoTotal.setHouseNo("");
							}
							} // end of if - null condition
							else{
							    propertyWiseInfoList.add(propertyWiseInfoTotal);
							}
						}
					}
			}
			reportInfo.setZoneNo(strZoneNum);
			reportInfo.setWardNo(strWardNum);
			reportInfo.setBlockNo(strBlockNum);
			reportInfo.setLocalityNo(strLocalityNum);
			reportInfo.setPropertyWiseArrearInfoList(propertyWiseInfoList);
			setDataSourceType(ReportDataSourceType.JAVABEAN);
			setReportData(reportInfo);
			super.report();
		return "generate";
	}

	/**
	 * @param propertyWiseInfoTotal
	 * @param propertyInfo
	 * @return
	 */
	private PropertyWiseArrearInfo addPropertyWiseInfo(PropertyWiseArrearInfo propertyWiseInfoTotal, PropertyWiseArrearInfo propertyInfo) {
		propertyWiseInfoTotal.setArrearLibraryCess(propertyWiseInfoTotal.getArrearLibraryCess().add(propertyInfo.getArrearLibraryCess()));
		propertyWiseInfoTotal.setArrearPropertyTax(propertyWiseInfoTotal.getArrearPropertyTax().add(propertyInfo.getArrearPropertyTax()));
		propertyWiseInfoTotal.setArrearPenalty(propertyWiseInfoTotal.getArrearPenalty().add(propertyInfo.getArrearPenalty()));
		propertyWiseInfoTotal.setTotalArrearTax(propertyWiseInfoTotal.getTotalArrearTax().add(propertyInfo.getTotalArrearTax()));
		return propertyWiseInfoTotal;
	}


	/**
	 * @param currInstDmdColMatView
	 * @param currInstallment
	 * @return
	 */
	private PropertyWiseArrearInfo preparePropertyWiseInfo(InstDmdCollMaterializeView currInstDmdColMatView) {
	          PropertyWiseArrearInfo propertyWiseInfo=null;
	        propertyWiseInfo = preparePropInfo(currInstDmdColMatView.getPropMatView());
		BigDecimal totalTax = currInstDmdColMatView.getLibCessTax().add(currInstDmdColMatView.getGeneralTax())
				.add(currInstDmdColMatView.getPenaltyFinesTax());

		CFinancialYear finYear=financialYearDAO.getFinYearByDate(currInstDmdColMatView.getInstallment().getFromDate());
		propertyWiseInfo.setArrearInstallmentYear(finYear.getFinYearRange());
		propertyWiseInfo.setArrearLibraryCess(currInstDmdColMatView.getLibCessTax());
		propertyWiseInfo.setArrearPropertyTax(currInstDmdColMatView.getGeneralTax());
		propertyWiseInfo.setArrearPenalty(currInstDmdColMatView.getPenaltyFinesTax());
		/*
		 * Total of Arrear Librarycess tax,general tax and penalty tax
		 */
		propertyWiseInfo.setTotalArrearTax(totalTax);
		return propertyWiseInfo;
	}

	/**
	 * @param propMatView
	 * @return
	 */
	private PropertyWiseArrearInfo preparePropInfo(PropertyMaterlizeView propMatView) {
		PropertyWiseArrearInfo propertyWiseInfo = new PropertyWiseArrearInfo();
		propertyWiseInfo.setBasicPropId(propMatView.getBasicPropertyID().longValue());
		propertyWiseInfo.setIndexNumber(propMatView.getPropertyId());
		propertyWiseInfo.setOwnerName(propMatView.getOwnerName());
		propertyWiseInfo.setHouseNo(propMatView.getHouseNo());
		return propertyWiseInfo;
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
		return REPORT_TEMPLATENAME_ARREARREGISTER; 
	}

    public Long getLocalityId() {
        return localityId;
    }

    public void setLocalityId(Long localityId) {
        this.localityId = localityId;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public List<PropertyWiseArrearInfo> getPropertyWiseInfoList() {
        return propertyWiseInfoList;
    }

    public void setPropertyWiseInfoList(List<PropertyWiseArrearInfo> propertyWiseInfoList) {
        this.propertyWiseInfoList = propertyWiseInfoList;
    }

}

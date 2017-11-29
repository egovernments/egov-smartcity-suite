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
package org.egov.wtms.reports.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author subhash
 *
 */
public class ArrearReportInfo {

    private String currInstallment;
    private String zoneNo;
    private String wardNo;
    private String blockNo;
    private String localityNo;
    private String partNo;
    private List<ArrearRegisterReport> propertyWiseArrearInfoList=new ArrayList<ArrearRegisterReport>();
    private String dateString;
    private Map<String, BigDecimal> demandCollMap;
    private Integer totalNoProps;
    private Integer totalGenBills;
    private BigDecimal arrDmd;
    private BigDecimal currDmd;
    private BigDecimal totalDmd;
    private String boundaryname;

    private Long basicPropId;
    private String indexNumber;
    private String ownerName;
    private String houseNo;
    private BigDecimal arrColl;
    private BigDecimal currColl;
    private BigDecimal totalColl;
    private String municipal;
    private String district;

    public String getBoundaryname() {
		return boundaryname;
	}

	public void setBoundaryname(String boundaryname) {
		this.boundaryname = boundaryname;
	}

	public Long getBasicPropId() {
		return basicPropId;
	}

	public void setBasicPropId(Long basicPropId) {
		this.basicPropId = basicPropId;
	}

	public String getIndexNumber() {
		return indexNumber;
	}

	public void setIndexNumber(String indexNumber) {
		this.indexNumber = indexNumber;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}

	public String getCurrInstallment() {
        return currInstallment;
    }

    public void setCurrInstallment(final String currInstallment) {
        this.currInstallment = currInstallment;
    }

    public String getZoneNo() {
        return zoneNo;
    }

    public void setZoneNo(final String zoneNo) {
        this.zoneNo = zoneNo;
    }

    public String getWardNo() {
        return wardNo;
    }

    public void setWardNo(final String wardNo) {
        this.wardNo = wardNo;
    }

    public String getPartNo() {
        return partNo;
    }

    public void setPartNo(final String partNo) {
        this.partNo = partNo;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(final String dateString) {
        this.dateString = dateString;
    }

    public Map<String, BigDecimal> getDemandCollMap() {
        return demandCollMap;
    }

    public void setDemandCollMap(final Map<String, BigDecimal> demandCollMap) {
        this.demandCollMap = demandCollMap;
    }

    public Integer getTotalNoProps() {
        return totalNoProps;
    }

    public void setTotalNoProps(final Integer totalNoProps) {
        this.totalNoProps = totalNoProps;
    }

    public BigDecimal getArrSewerageTax() {
        return demandCollMap.get("arrSewerageTax");
    }

    public BigDecimal getArrWaterTax() {
        return demandCollMap.get("arrWaterTax");
    }

    public BigDecimal getArrGenTax() {
        return demandCollMap.get("arrGenTax");
    }

    public BigDecimal getArrFireTax() {
        return demandCollMap.get("arrFireTax");
    }

    public BigDecimal getArrLightTax() {
        return demandCollMap.get("arrLightTax");
    }

    public BigDecimal getArrEduTax() {
        return demandCollMap.get("arrEduTax");
    }

    public BigDecimal getArrEgsTax() {
        return demandCollMap.get("arrEgsTax");
    }

    public BigDecimal getArrBigBldgTax() {
        return demandCollMap.get("arrBigBldgTax");
    }

    public BigDecimal getArrSewerageTaxColl() {
        return demandCollMap.get("arrSewerageTaxColl");
    }

    public BigDecimal getArrWaterTaxColl() {
        return demandCollMap.get("arrWaterTaxColl");
    }

    public BigDecimal getArrGenTaxColl() {
        return demandCollMap.get("arrGenTaxColl");
    }

    public BigDecimal getArrFireTaxColl() {
        return demandCollMap.get("arrFireTaxColl");
    }

    public BigDecimal getArrLightTaxColl() {
        return demandCollMap.get("arrLightTaxColl");
    }

    public BigDecimal getArrEduTaxColl() {
        return demandCollMap.get("arrEduTaxColl");
    }

    public BigDecimal getArrEgsTaxColl() {
        return demandCollMap.get("arrEgsTaxColl");
    }

    public BigDecimal getArrBigBldgTaxColl() {
        return demandCollMap.get("arrBigBldgTaxColl");
    }

    public BigDecimal getCurrSewerageTax() {
        return demandCollMap.get("currSewerageTax");
    }

    public BigDecimal getCurrWaterTax() {
        return demandCollMap.get("currWaterTax");
    }

    public BigDecimal getCurrGenTax() {
        return demandCollMap.get("currGenTax");
    }

    public BigDecimal getCurrFireTax() {
        return demandCollMap.get("currFireTax");
    }

    public BigDecimal getCurrLightTax() {
        return demandCollMap.get("currLightTax");
    }

    public BigDecimal getCurrEduTax() {
        return demandCollMap.get("currEduTax");
    }

    public BigDecimal getCurrEgsTax() {
        return demandCollMap.get("currEgsTax");
    }

    public BigDecimal getCurrBigBldgTax() {
        return demandCollMap.get("currBigBldgTax");
    }

    public BigDecimal getCurrSewerageTaxColl() {
        return demandCollMap.get("currSewerageTaxColl");
    }

    public BigDecimal getCurrWaterTaxColl() {
        return demandCollMap.get("currWaterTaxColl");
    }

    public BigDecimal getCurrGenTaxColl() {
        return demandCollMap.get("currGenTaxColl");
    }

    public BigDecimal getCurrFireTaxColl() {
        return demandCollMap.get("currFireTaxColl");
    }

    public BigDecimal getCurrLightTaxColl() {
        return demandCollMap.get("currLightTaxColl");
    }

    public BigDecimal getCurrEduTaxColl() {
        return demandCollMap.get("currEduTaxColl");
    }

    public BigDecimal getCurrEgsTaxColl() {
        return demandCollMap.get("currEgsTaxColl");
    }

    public BigDecimal getCurrBigBldgTaxColl() {
        return demandCollMap.get("currBigBldgTaxColl");
    }

    public BigDecimal getArrChqBuncPenalty() {
        return demandCollMap.get("arrChqBuncPenalty");
    }

    public BigDecimal getArrPenaltyFine() {
        return demandCollMap.get("arrPenaltyFine");
    }

    public BigDecimal getArrChqBuncPnltyColl() {
        return demandCollMap.get("arrChqBuncPnltyColl");
    }

    public BigDecimal getArrPenaltyFineColl() {
        return demandCollMap.get("arrPenaltyFineColl");
    }

    public BigDecimal getCurrChqBuncPenalty() {
        return demandCollMap.get("currChqBuncPenalty");
    }

    public BigDecimal getCurrPenaltyFine() {
        return demandCollMap.get("currPenaltyFine");
    }

    public BigDecimal getCurrChqBncPntyColl() {
        return demandCollMap.get("currChqBncPntyColl");
    }

    public BigDecimal getCurrPnltyFineColl() {
        return demandCollMap.get("currPnltyFineColl");
    }

    public BigDecimal getArrDmdTotal() {
        return demandCollMap.get("arrDmdTotal");
    }

    public BigDecimal getCurrDmdTotal() {
        return demandCollMap.get("currDmdTotal");
    }

    public BigDecimal getArrCollTotal() {
        return demandCollMap.get("arrCollTotal");
    }

    public BigDecimal getCurrCollTotal() {
        return demandCollMap.get("currCollTotal");
    }

    public BigDecimal getArrDmdGrandTotal() {
        return demandCollMap.get("arrDmdGrandTotal");
    }

    public BigDecimal getCurrDmdGrandTotal() {
        return demandCollMap.get("currDmdGrandTotal");
    }

    public BigDecimal getArrCollGrandTotal() {
        return demandCollMap.get("arrCollGrandTotal");
    }

    public BigDecimal getCurrCollGrandTotal() {
        return demandCollMap.get("currCollGrandTotal");
    }

    public BigDecimal getArrAdvt() {
        return BigDecimal.ZERO.setScale(2);
    }

    public BigDecimal getCurrAdvt() {
        return BigDecimal.ZERO.setScale(2);
    }

    public BigDecimal getArrAdvtColl() {
        return BigDecimal.ZERO.setScale(2);
    }

    public BigDecimal getCurrAdvtColl() {
        return BigDecimal.ZERO.setScale(2);
    }

    public BigDecimal getArrMutation() {
        return BigDecimal.ZERO.setScale(2);
    }

    public BigDecimal getCurrMutation() {
        return BigDecimal.ZERO.setScale(2);
    }

    public BigDecimal getArrMutColl() {
        return BigDecimal.ZERO.setScale(2);
    }

    public BigDecimal getCurrMutColl() {
        return BigDecimal.ZERO.setScale(2);
    }

    public Integer getTotalGenBills() {
        return totalGenBills;
    }

    public void setTotalGenBills(final Integer totalGenBills) {
        this.totalGenBills = totalGenBills;
    }

    public BigDecimal getArrDmd() {
        return arrDmd;
    }

    public void setArrDmd(final BigDecimal arrDmd) {
        this.arrDmd = arrDmd;
    }

    public BigDecimal getCurrDmd() {
        return currDmd;
    }

    public void setCurrDmd(final BigDecimal currDmd) {
        this.currDmd = currDmd;
    }

    public BigDecimal getTotalDmd() {
        return totalDmd;
    }

    public void setTotalDmd(final BigDecimal totalDmd) {
        this.totalDmd = totalDmd;
    }

    public BigDecimal getArrColl() {
        return arrColl;
    }

    public void setArrColl(final BigDecimal arrColl) {
        this.arrColl = arrColl;
    }

    public BigDecimal getCurrColl() {
        return currColl;
    }

    public void setCurrColl(final BigDecimal currColl) {
        this.currColl = currColl;
    }

    public BigDecimal getTotalColl() {
        return totalColl;
    }

    public void setTotalColl(final BigDecimal totalColl) {
        this.totalColl = totalColl;
    }

    public String getBlockNo() {
        return blockNo;
    }

    public void setBlockNo(final String blockNo) {
        this.blockNo = blockNo;
    }

    public String getLocalityNo() {
        return localityNo;
    }

    public void setLocalityNo(final String localityNo) {
        this.localityNo = localityNo;
    }

    public List<ArrearRegisterReport> getPropertyWiseArrearInfoList() {
        return propertyWiseArrearInfoList;
    }

    public void setPropertyWiseArrearInfoList(final List<ArrearRegisterReport> propertyWiseArrearInfoList) {
        this.propertyWiseArrearInfoList = propertyWiseArrearInfoList;
    }

    public void addPropertyWiseArrearInfoList(final ArrearRegisterReport arrearRegisterReport) {
    	getPropertyWiseArrearInfoList().add(arrearRegisterReport);
    }

    public String getMunicipal() {
        return municipal;
    }

    public void setMunicipal(final String municipal) {
        this.municipal = municipal;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(final String district) {
        this.district = district;
    }
}

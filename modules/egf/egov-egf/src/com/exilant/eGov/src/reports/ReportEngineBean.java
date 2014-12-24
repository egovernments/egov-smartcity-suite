package com.exilant.eGov.src.reports;

import java.util.List;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;

public class ReportEngineBean {
	 private static final Logger LOGGER = Logger.getLogger(ReportEngineBean.class);
	 private String    fundId ;
	 private String    fundsourceId;
	 private String    departmentId;
	 private String    functionaryId;
	 private String    fromVoucherNumber;
	 private String    toVoucherNumber;
	 private String    finacialYearId;
	 private String    fiscalPeriodId;
	 private String    fromDate;
	 private String    toDate;
	 private String    divisionId;
	 private String    schemeId;
	 private String    subSchemeId;
	 private String    functionId;
	 private List<String> 	excludeStatuses;
	 private List<String> includeStatuses;
	 private int filtersCount=0;
	 public String getFundId() {
		return fundId;
		
	}
	public void setFundId(final String fundId) {
		this.fundId = fundId;
		filtersCount+=1;
	}
	public String getFundsourceId() {
		return fundsourceId;
		
	}
	public void setFundsourceId(final String fundsourceId) {
		this.fundsourceId = fundsourceId;
		filtersCount+=1;
	}
	public String getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(final String departmentId) {
		this.departmentId = departmentId;
		filtersCount+=1;
	}
	public String getFunctionaryId() {
		return functionaryId;
	}
	public void setFunctionaryId(final String functionaryId) {
		this.functionaryId = functionaryId;
		filtersCount+=1;
	}
	public String getFromVoucherNumber() {
		return fromVoucherNumber;
	}
	public void setFromVoucherNumber(final String fromVoucherNumber) {
		this.fromVoucherNumber = fromVoucherNumber;
		filtersCount+=1;
	}
	public String getToVoucherNumber() {
		return toVoucherNumber;
		
	}
	public void setToVoucherNumber(final String toVoucherNumber) {
		this.toVoucherNumber = toVoucherNumber;
		filtersCount+=1;
	}
	public String getFinacialYearId() {
		return finacialYearId;
	}
	public void setFinacialYearId(final String finacialYearId) {
		this.finacialYearId = finacialYearId;
		filtersCount+=1;
	}
	public String getFiscalPeriodId() {
		return fiscalPeriodId;
	}
	public void setFiscalPeriodId(final String fiscalPeriodId) {
		this.fiscalPeriodId = fiscalPeriodId;
		filtersCount+=1;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(final String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate( final String toDate) {
		this.toDate = toDate;
		filtersCount+=1;
	}
	public String getDivisionId() {
		return divisionId;
	}
	public void setDivisionId( final String divisionId) {
		this.divisionId = divisionId;
		filtersCount+=1;
	}
	public String getSchemeId() {
		return schemeId;
	}
	public void setSchemeId( final String schemeId) {
		this.schemeId = schemeId;
		filtersCount+=1;
	}
	public String getSubSchemeId() {
		return subSchemeId;
	}
	public void setSubSchemeId( final String subSchemeId) {
		this.subSchemeId = subSchemeId;
		filtersCount+=1;
	}
	public String getFunctionId() {
		return functionId;
	}
	public void setFunctionId(final String functionId) {
		this.functionId = functionId;
		filtersCount+=1;
	}
	public List<String> getExcludeStatuses() {
		return excludeStatuses;
	}
	public void setExcludeStatuses(final List<String> excludeStatuses) {
		this.excludeStatuses = excludeStatuses;
		
	}
	public List<String> getIncludeStatuses() {
		return includeStatuses;
	}
	public void setIncludeStatuses(final List<String> includeStatuses) {
		this.includeStatuses = includeStatuses;
	}
	public int getFiltersCount() {
		return filtersCount;
	}
	public String getCommaSeperatedValues(final List<String> list) throws EGOVRuntimeException
	{
		StringBuffer commaSeperatedValues=new StringBuffer("");
		if(!list.isEmpty())
		{
			if(list.size()==1)
			{
				commaSeperatedValues.append(list.get(0).toString());
			}
			else
			{
				
			String comma="";
				for(int i=0;i<list.size();i++)
				{
					commaSeperatedValues.append(comma+list.get(i).toString());
					comma=",";
				}
			}
		}
		else
		{
			throw new EGOVRuntimeException("List contains 0 items cannot create comma seperate values");
		}
		LOGGER.info("*************Comma seprated values");
		LOGGER.info(commaSeperatedValues);
		return commaSeperatedValues.toString();
	}
	
}

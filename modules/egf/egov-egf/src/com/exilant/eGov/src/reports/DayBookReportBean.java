package com.exilant.eGov.src.reports;

import java.util.*;


public class DayBookReportBean 
{
	private String startDate;
	private String endDate;
	private String totalCount;
	private String isConfirmedCount;
    private String fundId;
	
	/**
	 * 
	 *
	 */
	public DayBookReportBean() {
		
		this.startDate = "";
		this.endDate = "";
		this.totalCount="";
		this.isConfirmedCount="";
        this.fundId="0";
	}
	/**
	 * @return Returns the endDate.
	 */
	public String getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate The endDate to set.
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return Returns the startDate.
	 */
	public String getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate The startDate to set.
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return Returns the totalCount.
	 */
	public String getTotalCount() {
		return totalCount;
	}
	/**
	 * @param totalCount The totalCount to set.
	 */
	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}
	/**
	 * @return Returns the isConfirmedCount.
	 */
	public String getIsConfirmedCount() {
		return isConfirmedCount;
	}
	/**
	 * @param isConfirmedCount The isConfirmedCount to set.
	 */
	public void setIsConfirmedCount(String isConfirmedCount) {
		this.isConfirmedCount = isConfirmedCount;
	}
    /**
     * @return Returns the fundId.
     */
    public String getFundId() {
        return fundId;
    }
    /**
     * @param fundId The fundId to set.
     */
    public void setFundId(String fundId) {
        this.fundId = fundId;
    }
}


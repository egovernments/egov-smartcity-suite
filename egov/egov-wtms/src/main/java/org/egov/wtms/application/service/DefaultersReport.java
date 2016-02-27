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
package org.egov.wtms.application.service;

import java.math.BigDecimal;

public class DefaultersReport {
	
	private Integer slNo;
	private String hscNo;
	private String ownerName;
	private String wardName;
	private String houseNo;
	private String locality;
	private String mobileNumber;
	private double arrearsDue;
	
	private double currentDue;
	private double totalDue;
	private String arrearsFrmInstallment;
	private String arrearsToInstallment;
	private BigDecimal aggrArrearPenalyDue;
        private BigDecimal aggrCurrPenalyDue;
        private BigDecimal fromAmount;
    	
		private BigDecimal toAmount;
		private String duePeriodFrom;
	
	public Integer getSlNo() {
		return slNo;
	}
	public void setSlNo(Integer slNo) {
		this.slNo = slNo;
	}
	public String getHscNo() {
		return hscNo;
	}
	public void setHscNo(String hscNo) {
		this.hscNo = hscNo;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public String getWardName() {
		return wardName;
	}
	public void setWardName(String wardName) {
		this.wardName = wardName;
	}
	public String getHouseNo() {
		return houseNo;
	}
	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}
	public String getLocality() {
		return locality;
	}
	public void setLocality(String locality) {
		this.locality = locality;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public double getArrearsDue() {
		return arrearsDue;
	}
	public void setArrearsDue(double arrearsDue) {
		this.arrearsDue = arrearsDue;
	}
	public double getCurrentDue() {
		return currentDue;
	}
	public void setCurrentDue(double currentDue) {
		this.currentDue = currentDue;
	}
	public double getTotalDue() {
		return totalDue;
	}
	public void setTotalDue(double totalDue) {
		this.totalDue = totalDue;
	}
	
        public String getArrearsFrmInstallment() {
            return arrearsFrmInstallment;
        }
        public void setArrearsFrmInstallment(String arrearsFrmInstallment) {
            this.arrearsFrmInstallment = arrearsFrmInstallment; 
        }
        public String getArrearsToInstallment() {
            return arrearsToInstallment;
        }
        public void setArrearsToInstallment(String arrearsToInstallment) {
            this.arrearsToInstallment = arrearsToInstallment;
        }
        public BigDecimal getAggrArrearPenalyDue() {
            return aggrArrearPenalyDue;
        }
        public void setAggrArrearPenalyDue(BigDecimal aggrArrearPenalyDue) {
            this.aggrArrearPenalyDue = aggrArrearPenalyDue;
        }
        public BigDecimal getAggrCurrPenalyDue() {
            return aggrCurrPenalyDue;
        }
        public void setAggrCurrPenalyDue(BigDecimal aggrCurrPenalyDue) {
            this.aggrCurrPenalyDue = aggrCurrPenalyDue;
        }
        public BigDecimal getFromAmount() {
			return fromAmount;
		}
		public void setFromAmount(BigDecimal fromAmount) {
			this.fromAmount = fromAmount;
		}
		public BigDecimal getToAmount() {
			return toAmount;
		}
		public void setToAmount(BigDecimal toAmount) {
			this.toAmount = toAmount;
		}
		public String getDuePeriodFrom() {
			return duePeriodFrom;
		}
		public void setDuePeriodFrom(String duePeriodFrom) {
			this.duePeriodFrom = duePeriodFrom;
		}
		
		
	
		
		
	
}

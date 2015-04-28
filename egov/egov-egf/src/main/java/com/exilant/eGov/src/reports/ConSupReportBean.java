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
package com.exilant.eGov.src.reports;


public class ConSupReportBean
{

    private String name;
    private String workOrderNo;
    private String workname;
    private String orderValue;
    private String maxAdv;
    private String advPaid;
    private String advAdj;
    private String amtPaid;
    private String billAmount;
    private String orderDate;
    private String relCode;
    private String worksDetailId;
    private String relationTypeId;
    private String code;
    private int conSupType;
    private String conSupName;
    private String totalCount;
    private String fromDate;
    private String toDate;
    private String dedAmount;
    private String relId;
    private String fundId;

    public ConSupReportBean()
    {
        name = "";
        workOrderNo = "";
        workname = "";
        orderValue = "";
        maxAdv = "";
        advPaid = "";
        advAdj = "";
        amtPaid = "";
        billAmount = "";
        orderDate = "";
        relCode = "";
        worksDetailId = "";
        relationTypeId = "";
        code = "";
        conSupType = 0;
        conSupName = "";
        totalCount = "";
        fromDate="";
        toDate="";
        dedAmount="";
        relId="";
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getWorkOrderNo()
    {
        return workOrderNo;
    }

    public void setWorkOrderNo(String workOrderNo)
    {
        this.workOrderNo = workOrderNo;
    }

    public String getWorkname()
    {
        return workname;
    }

    public void setWorkname(String workname)
    {
        this.workname = workname;
    }

    public String getOrderValue()
    {
        return orderValue;
    }

    public void setOrderValue(String orderValue)
    {
        this.orderValue = orderValue;
    }

    public String getMaxAdv()
    {
        return maxAdv;
    }

    public void setMaxAdv(String maxAdv)
    {
        this.maxAdv = maxAdv;
    }

    public String getAdvPaid()
    {
        return advPaid;
    }

    public void setAdvPaid(String advPaid)
    {
        this.advPaid = advPaid;
    }

    public String getAdvAdj()
    {
        return advAdj;
    }

    public void setAdvAdj(String advAdj)
    {
        this.advAdj = advAdj;
    }

    public String getAmtPaid()
    {
        return amtPaid;
    }

    public void setAmtPaid(String amtPaid)
    {
        this.amtPaid = amtPaid;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public int getConSupType()
    {
        return conSupType;
    }

    public void setConSupType(int conSupType)
    {
        this.conSupType = conSupType;
    }

    public String getConSupName()
    {
        return conSupName;
    }

    public void setConSupName1(String conSupName1)
    {
        conSupName = conSupName1;
    }

    public String getTotalCount()
    {
        return totalCount;
    }

    public void setTotalCount(String totalCount1)
    {
        totalCount = totalCount1;
    }

    public void setBillAmount(String billAmount)
    {
        this.billAmount = billAmount;
    }

    public String getBillAmount()
    {
        return billAmount;
    }

    public void setOrderDate(String orderDate)
    {
        this.orderDate = orderDate;
    }

    public String getOrderDate()
    {
        return orderDate;
    }

    public String getRelCode()
    {
        return relCode;
    }

    public void setRelCode(String relCode)
    {
        this.relCode = relCode;
    }

    public String getWorksDetailId()
    {
        return worksDetailId;
    }

    public void setWorksDetailId(String worksDetailId)
    {
        this.worksDetailId = worksDetailId;
    }

    public String getRelationTypeId()
    {
        return relationTypeId;
    }

    public void setRelationTypeId(String relationTypeId)
    {
        this.relationTypeId = relationTypeId;
    }

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getDedAmount() {
		return dedAmount;
	}

	public void setDedAmount(String dedAmount) {
		this.dedAmount = dedAmount;
	}

	public String getRelId() {
		return relId;
	}

	public void setRelId(String relId) {
		this.relId = relId;
	}

	public String getFundId() {
		return fundId;
	}

	public void setFundId(String fundId) {
		this.fundId = fundId;
	}
	
}

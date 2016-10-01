/*
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
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
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
 */

package org.egov.ptis.elasticsearch.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "collection", type = "collection_bifurcation")
public class CollectionIndex {

	@Id
	private String citycode;
	
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String receiptnumber;
	
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String billingservice;
	
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String paymentmode;
	
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String channel;
	
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String paymentgateway;
	
	@Field(type = FieldType.Long)
	private Long billnumber;
	
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String cityname;
	
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String districtname;
	
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String regionname;
	
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String status;
	
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String citygrade;
	
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String receiptcreator;
	
	@Field(type = FieldType.Date)
	private Date receiptdate;
	
	@Field(type = FieldType.Double)
	private Double arrearamount;
	
	@Field(type = FieldType.Double)
	private Double penaltyamount;
	
	@Field(type = FieldType.Double)
	private Double currentamount;
	
	@Field(type = FieldType.Double)
	private Double totalamount;
	
	@Field(type = FieldType.Double)
	private Double advanceamount;
	
	@Field(type = FieldType.Double)
	private Double latepaymentcharges;
	
	@Field(type = FieldType.Double)
	private Double arrearcess;
	
	@Field(type = FieldType.Double)
	private Double currentcess;
	
	@Field(type = FieldType.String)
	private String installmentfrom;
	
	@Field(type = FieldType.String)
	private String installmentto;
	
	@Field(type = FieldType.String)
	private String consumername;
	
	@Field(type = FieldType.Double)
	private Double reductionamount;

	public String getReceiptnumber() {
		return receiptnumber;
	}

	public void setReceiptnumber(String receiptnumber) {
		this.receiptnumber = receiptnumber;
	}

	public String getBillingservice() {
		return billingservice;
	}

	public void setBillingservice(String billingservice) {
		this.billingservice = billingservice;
	}

	public String getPaymentmode() {
		return paymentmode;
	}

	public void setPaymentmode(String paymentmode) {
		this.paymentmode = paymentmode;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getPaymentgateway() {
		return paymentgateway;
	}

	public void setPaymentgateway(String paymentgateway) {
		this.paymentgateway = paymentgateway;
	}

	public Long getBillnumber() {
		return billnumber;
	}

	public void setBillnumber(Long billnumber) {
		this.billnumber = billnumber;
	}

	public String getCityname() {
		return cityname;
	}

	public void setCityname(String cityname) {
		this.cityname = cityname;
	}

	public String getDistrictname() {
		return districtname;
	}

	public void setDistrictname(String districtname) {
		this.districtname = districtname;
	}

	public String getRegionname() {
		return regionname;
	}

	public void setRegionname(String regionname) {
		this.regionname = regionname;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCitygrade() {
		return citygrade;
	}

	public void setCitygrade(String citygrade) {
		this.citygrade = citygrade;
	}

	public String getCitycode() {
		return citycode;
	}

	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}

	public String getReceiptcreator() {
		return receiptcreator;
	}

	public void setReceiptcreator(String receiptcreator) {
		this.receiptcreator = receiptcreator;
	}

	public Date getReceiptdate() {
		return receiptdate;
	}

	public void setReceiptdate(Date receiptdate) {
		this.receiptdate = receiptdate;
	}

	public Double getArrearamount() {
		return arrearamount;
	}

	public void setArrearamount(Double arrearamount) {
		this.arrearamount = arrearamount;
	}

	public Double getPenaltyamount() {
		return penaltyamount;
	}

	public void setPenaltyamount(Double penaltyamount) {
		this.penaltyamount = penaltyamount;
	}

	public Double getCurrentamount() {
		return currentamount;
	}

	public void setCurrentamount(Double currentamount) {
		this.currentamount = currentamount;
	}

	public Double getTotalamount() {
		return totalamount;
	}

	public void setTotalamount(Double totalamount) {
		this.totalamount = totalamount;
	}

	public Double getAdvanceamount() {
		return advanceamount;
	}

	public void setAdvanceamount(Double advanceamount) {
		this.advanceamount = advanceamount;
	}

	public Double getLatepaymentcharges() {
		return latepaymentcharges;
	}

	public void setLatepaymentcharges(Double latepaymentcharges) {
		this.latepaymentcharges = latepaymentcharges;
	}

	public Double getArrearcess() {
		return arrearcess;
	}

	public void setArrearcess(Double arrearcess) {
		this.arrearcess = arrearcess;
	}

	public Double getCurrentcess() {
		return currentcess;
	}

	public void setCurrentcess(Double currentcess) {
		this.currentcess = currentcess;
	}

	public String getInstallmentfrom() {
		return installmentfrom;
	}

	public void setInstallmentfrom(String installmentfrom) {
		this.installmentfrom = installmentfrom;
	}

	public String getInstallmentto() {
		return installmentto;
	}

	public void setInstallmentto(String installmentto) {
		this.installmentto = installmentto;
	}

	public String getConsumername() {
		return consumername;
	}

	public void setConsumername(String consumername) {
		this.consumername = consumername;
	}

	public Double getReductionamount() {
		return reductionamount;
	}

	public void setReductionamount(Double reductionamount) {
		this.reductionamount = reductionamount;
	}
}

/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It
	   is required that all modified versions of this material be marked in
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program
	   with regards to rights under trademark law for use of the trade names
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.works.models.tender;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.egov.infstr.models.BaseModel;
import org.egov.works.models.estimate.Activity;

public class TenderResponseActivity extends BaseModel{
	

	private TenderResponse tenderResponse;
		
	private Activity activity;
	
	//@Required(message="tenderResponseActivity.negotiatedRate.not.null")
	//@GreaterThan(value=0,message="tenderResponseActivity.negotiatedRate.non.negative")
	private double negotiatedRate;
	
	//@Required(message="tenderResponseActivity.negotiatedQuantity.not.null")  
	//@GreaterThan(value=0,message="tenderResponseActivity.negotiatedQuantity.non.negative")
	private double negotiatedQuantity;

	private String schCode;
	private double assignedQty;
	
	private double estimatedQty;
	
	@Valid
	private List<TenderResponseQuotes> tenderResponseQuotes = new LinkedList<TenderResponseQuotes>();

	
	public TenderResponse getTenderResponse() {
		return tenderResponse;
	}

	public void setTenderResponse(TenderResponse tenderResponse) {
		this.tenderResponse = tenderResponse;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public double getNegotiatedRate() {
		return negotiatedRate;
	}

	public void setNegotiatedRate(double negotiatedRate) {
		this.negotiatedRate = negotiatedRate;
	}

	public double getNegotiatedQuantity() {
		return negotiatedQuantity;
	}

	public void setNegotiatedQuantity(double negotiatedQuantity) {
		this.negotiatedQuantity = negotiatedQuantity;
	}

	public String getSchCode() {
		return schCode;
	}

	public void setSchCode(String schCode) {
		this.schCode = schCode;
	}
	
	public List<TenderResponseQuotes> getTenderResponseQuotes() {
		return tenderResponseQuotes;
	}
	
	public Collection<TenderResponseQuotes> getTenderResponseQuotesList(){
		return CollectionUtils.select(tenderResponseQuotes, new Predicate(){
			public boolean evaluate(Object tenderReponseQuote) {
				return ((TenderResponseQuotes)tenderReponseQuote)!=null;
			}});
		
	}

	public void setTenderResponseQuotes(
			List<TenderResponseQuotes> tenderResponseQuotes) {
		this.tenderResponseQuotes = tenderResponseQuotes;
	}
	
	public void addTenderResponseQuotes(TenderResponseQuotes tenderResponseQuotes) {
		this.tenderResponseQuotes.add(tenderResponseQuotes);
	}

	public double getAssignedQty() {
		return assignedQty;
	}

	public void setAssignedQty(double assignedQty) {
		this.assignedQty = assignedQty;
	}
	

	public double getEstimatedQty() {
			return estimatedQty;
	}

	public void setEstimatedQty(double estimatedQty) {
			this.estimatedQty = estimatedQty;
	}
	
}

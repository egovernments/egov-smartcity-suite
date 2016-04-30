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
package org.egov.bpa.models.extd;

import org.egov.bpa.models.extd.masters.CheckListDetailsExtn;
import org.egov.infstr.models.BaseModel;

import java.util.Comparator;

public class RejectionChecklistExtn extends BaseModel implements Comparator<RejectionChecklistExtn>, Comparable<RejectionChecklistExtn>{

	/**
	 * Serial version uid
	 */
	private static final long serialVersionUID = 1L;
	private RejectionExtn rejection;
	private CheckListDetailsExtn checkListDetails;
	private Boolean isChecked;
	private String remarks;

	public RejectionExtn getRejection() {
		return rejection;
	}

	public void setRejection(RejectionExtn rejection) {
		this.rejection = rejection;
	}

	public Boolean getIsChecked() {
		return isChecked;
	}

	public void setIsChecked(Boolean isChecked) {
		this.isChecked = isChecked;
	}

	public CheckListDetailsExtn getCheckListDetails() {
		return checkListDetails;
	}

	public void setCheckListDetails(CheckListDetailsExtn checkListDetails) {
		this.checkListDetails = checkListDetails;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;  
	}

	@Override
	public int compareTo(RejectionChecklistExtn o) {
		if(this.getCheckListDetails()!=null && this.getCheckListDetails().getDescription()!=null && o.getCheckListDetails()!=null && o.getCheckListDetails().getDescription()!=null)
		 return (this.getCheckListDetails().getDescription().compareTo(o.getCheckListDetails().getDescription()));
		else return 0;
	}

	@Override
	public int compare(RejectionChecklistExtn o1, RejectionChecklistExtn o2) {
		 return Integer.valueOf(o1.getCheckListDetails().getDescription()).compareTo(Integer.valueOf(o2.getCheckListDetails().getDescription())); 
	}

}
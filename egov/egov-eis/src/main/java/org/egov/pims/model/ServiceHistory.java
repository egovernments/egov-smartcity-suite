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
package org.egov.pims.model;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

public class ServiceHistory implements Serializable,Comparable
{
   private Integer idService;
   private Date commentDate;
   private String comments;
   private String reason;
   private String orderNo;
   private Long docNo;
   private String payScale;
   private PersonalInformation employeeId;
   
   public ServiceHistory()
   {
	   
   }
   
   public ServiceHistory(PersonalInformation employeeId,Date commentDate,String comments,String reason,String serviceOrderNo,String serviceDocNo, String payScale)
   {
	   this.commentDate=commentDate;
	   this.comments=comments;
	   this.reason=reason;
	   this.orderNo=serviceOrderNo;
	   this.docNo=serviceDocNo.equals("")? this.docNo : Long.valueOf(serviceDocNo);
	   this.payScale=payScale;
   }
   
   public ServiceHistory(Integer idService)
   {
	   this.idService=idService;
   }
   
  
   
public Integer getIdService() {
	return idService;
}
public void setIdService(Integer idService) {
	this.idService = idService;
}
public Date getCommentDate() {
	return commentDate;
}
public void setCommentDate(Date commentDate) {
	this.commentDate = commentDate;
}
public String getComments() {
	return comments;
}
public void setComments(String comments) {
	this.comments = comments;
}
public String getReason() {
	return reason;
}
public void setReason(String reason) {
	this.reason = reason;
}

public PersonalInformation getEmployeeId() {
	return employeeId;
}

public void setEmployeeId(PersonalInformation employeeId) {
	this.employeeId = employeeId;
}

public static Comparator commentDateComparator = new Comparator() {
	public int compare(Object commentDate, Object anotherCommentDate) {
		Date commentDate1 = ((ServiceHistory)commentDate).getCommentDate();
		Date commentDate2 = ((ServiceHistory)anotherCommentDate).getCommentDate();
		return commentDate1.compareTo(commentDate2);
	}
};

public int compareTo(Object anotherCommentDate) throws ClassCastException {
    if (!(anotherCommentDate instanceof ServiceHistory))
      throw new ClassCastException("A ServiceHistory object expected.");
    Integer serviceId = ((ServiceHistory) anotherCommentDate).getIdService(); 
    return this.idService.compareTo(serviceId);  
  }

public String getOrderNo() {
	return orderNo;
}

public Long getDocNo() {
	return docNo;
}

public void setOrderNo(String orderNo) {
	this.orderNo = orderNo;
}

public void setDocNo(Long docNo) {
	this.docNo = docNo;
}

public String getPayScale() {
	return payScale;
}

public void setPayScale(String payScale) {
	this.payScale = payScale;
}


   
}

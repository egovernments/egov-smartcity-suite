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
package org.egov.infstr.docmgmt.documents;

import java.util.Date;

import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infstr.annotation.Search;
import org.egov.infstr.docmgmt.DocumentObject;

@Node(extend = DocumentObject.class)
public class Notice extends DocumentObject {
	
	private static final long serialVersionUID = 1L;
	private static final String TO_STRING_SEPARATOR = "|";
	
	@Field(jcrMandatory = true)
	private String noticeType;
	@Field(jcrMandatory = true)
	private Date noticeDate;
	@Field
	private String associatedObjectId;
	@Field
	private String addressedTo;
	@Field
	private String address;
	@Field
	private String comments;
	
	public Notice() {
		super();
	}
	
	public Notice(String documentNumber, String moduleName, String noticeType, Date noticeDate) {
		super();
		this.documentNumber = documentNumber;
		this.moduleName = moduleName;
		this.noticeType = noticeType;
		this.noticeDate = noticeDate;
		this.domainName = escapeSpecialChars(EgovThreadLocals.getDomainName());
	}
	
	@Override
	public String toString() {
	    return new StringBuilder()
        .append(moduleName)
        .append(TO_STRING_SEPARATOR)
        .append(noticeType)
        .append(TO_STRING_SEPARATOR)
        .append(documentNumber)
	    .toString();
	}
	
	@Search(searchOp = Search.Operator.startsWith)
	public String getNoticeType() {
		return noticeType;
	}
	
	public void setNoticeType(String noticeType) {
		this.noticeType = noticeType;
	}
	
	@Search(searchOp = Search.Operator.between)
	public Date getNoticeDate() {
		return noticeDate;
	}
	
	public void setNoticeDate(Date noticeDate) {
		this.noticeDate = noticeDate;
	}
	
	public String getAssociatedObjectId() {
		return associatedObjectId;
	}
	
	public void setAssociatedObjectId(String associatedObjectId) {
		this.associatedObjectId = associatedObjectId;
	}
	
	@Search(searchOp = Search.Operator.contains)
	public String getAddressedTo() {
		return addressedTo;
	}
	
	public void setAddressedTo(String addressedTo) {
		this.addressedTo = addressedTo;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getComments() {
		return comments;
	}
	
	public void setComments(String comments) {
		this.comments = comments;
	}
	
}

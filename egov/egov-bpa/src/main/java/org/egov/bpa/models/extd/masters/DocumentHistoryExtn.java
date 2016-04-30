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
package org.egov.bpa.models.extd.masters;

import org.egov.bpa.models.extd.RegistrationExtn;
import org.egov.infra.admin.master.entity.User;
import org.egov.infstr.models.BaseModel;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


public class DocumentHistoryExtn extends BaseModel{
	
	/**
	 * Serial version uid
	 */
	private RegistrationExtn registrationId;
	private String documentNum;
	private Date documentDate;
	private User createdUser;
	private Date modifiedDate;
	private BigDecimal docEnclosedextentInsqmt;
	private BigDecimal layoutdextentInsqmt;
	private Boolean wheatherdocumentEnclosed;
	private Boolean plotDevelopedBy;
	private Boolean wheatherpartOfLayout;
	private String  wheatherplotDevelopedBy;
	private Boolean wheatherFmsOrSketchCopyOfReg;
	
	private Set<DocumentHistoryExtnDetails> DocumentHistoryDetailSet = new HashSet<DocumentHistoryExtnDetails>(0);
	
	

	public User getCreatedUser() {
		return createdUser;
	}
	public void setCreatedUser(User createdUser) {
		this.createdUser = createdUser;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public String getDocumentNum() {
		return documentNum;
	}
	public void setDocumentNum(String documentNum) {
		this.documentNum = documentNum;
	}
	public BigDecimal getLayoutdextentInsqmt() {
		return layoutdextentInsqmt;
	}
	public void setLayoutdextentInsqmt(BigDecimal layoutdextentInsqmt) {
		this.layoutdextentInsqmt = layoutdextentInsqmt;
	}
	public Date getDocumentDate() {
		return documentDate;
	}
	public void setDocumentDate(Date documentDate) {
		this.documentDate = documentDate;
	}
	public BigDecimal getDocEnclosedextentInsqmt() {
		return docEnclosedextentInsqmt;
	}
	public void setDocEnclosedextentInsqmt(BigDecimal docEnclosedextentInsqmt) {
		this.docEnclosedextentInsqmt = docEnclosedextentInsqmt;
	}
	
	public Boolean getWheatherdocumentEnclosed() {
		return wheatherdocumentEnclosed;
	}
	public void setWheatherdocumentEnclosed(Boolean wheatherdocumentEnclosed) {
		this.wheatherdocumentEnclosed = wheatherdocumentEnclosed;
	}
	
	public RegistrationExtn getRegistrationId() {
		return registrationId;
	}
	public void setRegistrationId(RegistrationExtn registrationId) {
		this.registrationId = registrationId;
	}
	public Boolean getWheatherpartOfLayout() {
		return wheatherpartOfLayout;
	}
	public void setWheatherpartOfLayout(Boolean wheatherpartOfLayout) {
		this.wheatherpartOfLayout = wheatherpartOfLayout;
	}
	public String getWheatherplotDevelopedBy() {
		return wheatherplotDevelopedBy;
	}
	public void setWheatherplotDevelopedBy(String wheatherplotDevelopedBy) {
		this.wheatherplotDevelopedBy = wheatherplotDevelopedBy;
	}
	public Boolean getWheatherFmsOrSketchCopyOfReg() {
		return wheatherFmsOrSketchCopyOfReg;
	}
	public void setWheatherFmsOrSketchCopyOfReg(Boolean wheatherFmsOrSketchCopyOfReg) {
		this.wheatherFmsOrSketchCopyOfReg = wheatherFmsOrSketchCopyOfReg;
	}
	public Set<DocumentHistoryExtnDetails> getDocumentHistoryDetailSet() {
		return DocumentHistoryDetailSet;
	}
	public void setDocumentHistoryDetailSet(
			Set<DocumentHistoryExtnDetails> documentHistoryDetailSet) {
		DocumentHistoryDetailSet = documentHistoryDetailSet;
	}
	
	public void addDocumentHistoryDetailSet(DocumentHistoryExtnDetails documenthistorydetail) {
		this.getDocumentHistoryDetailSet().add(documenthistorydetail);
	}
	
	public void removeDocumentHistoryDetailSet(DocumentHistoryExtnDetails documenthistorydetail) {
		this.getDocumentHistoryDetailSet().remove(documenthistorydetail);
	}
	public Boolean getPlotDevelopedBy() {
		return plotDevelopedBy;
	}
	public void setPlotDevelopedBy(Boolean plotDevelopedBy) {
		this.plotDevelopedBy = plotDevelopedBy;
	}
	
	

}

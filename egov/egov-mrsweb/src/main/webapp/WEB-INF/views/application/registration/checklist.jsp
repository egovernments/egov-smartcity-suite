<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  ~
  --%>

<!-- #-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation 
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or 
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this 
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It 
# 	   is required that all modified versions of this material be marked in 
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program 
# 	   with regards to rights under trademark law for use of the trade names 
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#------------------------------------------------------------------------------- -->


<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

	<div class="panel-heading">
		<div class="panel-title">
				<spring:message code="lbl.common.docs"/>
		</div>
	</div>
	
	<div class="form-group">
		<label class="col-sm-offset-6 view-content">
			<spring:message code="lbl.submitted.by.couple"/>
		</label>
	</div>
	
	<c:forEach var="doc" items="${generalDocuments}" varStatus="status">	
	<div class="form-group">	
		<div class="col-sm-1"></div>
		<form:hidden id="documents[${status.index}].id" path="documents[${status.index}].id" value="${doc.id}" /> 
		<div class="col-sm-4 add-margin text-right">
			<c:out value="${doc.name}"></c:out><c:if test="${doc.mandatory}"><span class="mandatory"></span></c:if>
		</div> 
		<div class="col-sm-2 add-margin text-center">
		<c:choose>
		<c:when test="${doc.mandatory}">
			<input type="file" id="file${status.index}id" name="documents[${status.index}].file" class="file-ellipsis upload-file" required="required">
		</c:when>
		<c:otherwise>
			<input type="file" id="file${status.index}id" name="documents[${status.index}].file" class="file-ellipsis upload-file">
		</c:otherwise>
		</c:choose>	
			<form:errors path="documents[${status.index}].file" cssClass="add-margin error-msg" />
		</div>
		<div class="col-sm-2">
			<c:set value="false" var="isDocFound"></c:set>
			<c:forEach items="${marriageRegistration.registrationDocuments}" var="regdoc" varStatus="loopStatus">
				<c:if test="${regdoc.document.id == doc.id}">
					<c:set value="true" var="isDocFound"></c:set>
					 <input type="hidden" id="registrationfile${status.index}" value="${regdoc.fileStoreMapper.fileName}|${regdoc.fileStoreMapper.contentType}|${regdoc.base64EncodedFile}"> 
					<a id="regdoc${status.index}"> ${regdoc.fileStoreMapper.fileName}</a>
				</c:if>
			</c:forEach>
		</div>
	</div>
	</c:forEach>
	
	<%-- <div class="form-group">
		<label class="col-sm-6 control-label">
			<spring:message code="lbl.memo.of.marriage"/><span class="mandatory"></span>
		</label>
		<div class="col-sm-1 text-center">
			<form:checkbox path="memorandumOfMarriage" required="required"/>
			<form:errors path="memorandumOfMarriage" cssClass="add-margin error-msg"/>
		</div>
	</div>
	
	<div class="form-group">
		<label class="col-sm-6 control-label">
			<spring:message code="lbl.court.fee.stamp"/><span class="mandatory"></span>
		</label>
		<div class="col-sm-1 text-center">
			<form:checkbox path="courtFeeStamp" required="required"/>
			<form:errors path="courtFeeStamp" cssClass="add-margin error-msg"/>
		</div>
	</div>
	
	<div class="form-group">
		<label class="col-sm-6 control-label">
			<spring:message code="lbl.court.affidavit"/>
		</label>
		<div class="col-sm-1 text-center">
			<form:checkbox path="affidavit"/>
			<form:errors path="affidavit" cssClass="add-margin error-msg"/>
		</div>
	</div>
	
	<div class="form-group">
		<label class="col-sm-6 control-label">
			<spring:message code="lbl.marriage.card"/>
		</label>
		<div class="col-sm-1 text-center">
			<form:checkbox path="marriageCard" />
			<form:errors path="marriageCard" cssClass="add-margin error-msg"/>
		</div>
	</div> --%> 
	
	<div class="panel-heading">
		<div class="panel-title">
				<spring:message code="lbl.indi.docs"/>
		</div>
	</div>
	
	<div class="form-group">
		<div class="col-sm-offset-5 col-sm-1 view-content">
			<spring:message code="lbl.husband"/>
		</div>
		<div class="col-sm-5 text-center view-content">
			<spring:message code="lbl.wife"/>
		</div>
	</div>
	
	<c:forEach var="doc" items="${individualDocuments}" varStatus="status">	
	<c:if test="${doc.documentProofType=='COMMON'}">
	<div class="form-group">	
		<div class="col-sm-1"></div>
		<form:hidden id="husband.documents[${status.index}].id" path="husband.documents[${status.index}].id" value="${doc.id}" /> 
		<form:hidden id="wife.documents[${status.index}].id" path="wife.documents[${status.index}].id" value="${doc.id}" /> 
		<div class="col-sm-4 add-margin text-right">
			<c:out value="${doc.name}"></c:out>
		</div>
		<div class="col-sm-2 add-margin text-center">
			<c:choose>
			<c:when test="${doc.name=='Passport'}">
				<input type="file" id="indvcommonhusband${doc.name}file${status.index}id" name="husband.documents[${status.index}].file" class="file-ellipsis upload-file">
			</c:when>
			<c:otherwise>
				<input type="file" id="indvcommonhusbandfile${status.index}id" name="husband.documents[${status.index}].file" class="file-ellipsis upload-file">
			</c:otherwise>
			</c:choose>
			<form:errors path="husband.documents[${status.index}].file" cssClass="add-margin error-msg" />
			&nbsp;&nbsp;
			<c:set value="false" var="isDocFound"></c:set>
			<c:forEach items="${marriageRegistration.husband.applicantDocuments}" var="appdoc" varStatus="loopStatus">
				<c:if test="${appdoc.document.id == doc.id}">
					<c:set value="true" var="isDocFound"></c:set>
					<input type="hidden" id="husbandfile${status.index}" value="${appdoc.fileStoreMapper.fileName}|${appdoc.fileStoreMapper.contentType}|${appdoc.base64EncodedFile}">
					<a id="husbanddoc${status.index}"> ${appdoc.fileStoreMapper.fileName}</a>
				</c:if>
			</c:forEach>
			<%-- <c:if test="${!isDocFound}">
				NA
			</c:if> --%>
		</div>
		<div class="col-sm-1"></div>
		<div class="col-sm-2 add-margin text-center">
			<c:choose>
			<c:when test="${doc.name=='Passport'}">
				<input type="file" id="indvcommonwife${doc.name}file${status.index}id" name="wife.documents[${status.index}].file" class="file-ellipsis upload-file">
			</c:when>
			<c:otherwise>
				<input type="file" id="indvcommonwifefile${status.index}id" name="wife.documents[${status.index}].file" class="file-ellipsis upload-file">
			</c:otherwise>
			</c:choose>
			<form:errors path="wife.documents[${status.index}].file" cssClass="add-margin error-msg" />
			
			<c:set value="false" var="isDocFound"></c:set>
			<c:forEach items="${marriageRegistration.wife.applicantDocuments}" var="appdoc" varStatus="loopStatus">
				<c:if test="${appdoc.document.id == doc.id}">
					<c:set value="true" var="isDocFound"></c:set>
					<input type="hidden" id="wifefile${status.index}" value="${appdoc.fileStoreMapper.fileName}|${appdoc.fileStoreMapper.contentType}|${appdoc.base64EncodedFile}">
					<a id="wifedoc${status.index}"> ${appdoc.fileStoreMapper.fileName}</a>
				</c:if>
			</c:forEach>
			<%-- <c:if test="${!isDocFound}">
				NA
			</c:if> --%>
		</div>
	</div>
	</c:if>
</c:forEach> 
	
<%-- 	<div class="form-group">
		<label class="col-sm-6 control-label">
			<spring:message code="lbl.photograph"/>
		</label>
		<div class="col-sm-1 text-center">
			<form:hidden path="husband.proofsAttached.id"/>
			<form:hidden path="wife.proofsAttached.id"/>
			<form:checkbox path="husband.proofsAttached.photograph" value="false" />
			<form:errors path="husband.proofsAttached.photograph" cssClass="add-margin error-msg"/>
		</div>
		<div class="col-sm-1 text-center">
			<form:checkbox path="wife.proofsAttached.photograph" value="false" />
			<form:errors path="wife.proofsAttached.photograph" cssClass="add-margin error-msg"/>
		</div>
	</div> 
	
	<div class="form-group">
		<label class="col-sm-6 control-label">
			<spring:message code="lbl.aadhar"/></span>
		</label>
		<div class="col-sm-1 text-center">
			<form:checkbox path="husband.proofsAttached.aadhar" value="false" />
			<form:errors path="husband.proofsAttached.aadhar" cssClass="add-margin error-msg"/>
		</div>
		<div class="col-sm-1 text-center">
			<form:checkbox path="wife.proofsAttached.aadhar" value="false" />
			<form:errors path="wife.proofsAttached.aadhar" cssClass="add-margin error-msg"/>
		</div>
	</div> --%>
	
	<div class="form-group">
		<label class="col-sm-5 control-label text-left view-content">
			<spring:message code="lbl.proof.of.age"/><span class="mandatory"></span><br>
			<spring:message code="lbl.proof.note"/>
		</label>
	</div>
	<div><br></div>
	<c:forEach var="doc" items="${individualDocuments}" varStatus="status">	
	<c:if test="${doc.documentProofType=='AGE_PROOF'}">
	<div class="form-group">	
		<div class="col-sm-1"></div>
		<form:hidden id="husband.documents[${status.index}].id" path="husband.documents[${status.index}].id" value="${doc.id}" /> 
		<form:hidden id="wife.documents[${status.index}].id" path="wife.documents[${status.index}].id" value="${doc.id}" /> 
		<div class="col-sm-4 add-margin text-right">
			<c:out value="${doc.name}"></c:out>
		</div>
		<div class="col-sm-2 add-margin text-center">
			<input type="file" id="ageproofhusbandfile${status.index}id" name="husband.documents[${status.index}].file" class="file-ellipsis upload-file">
			<form:errors path="husband.documents[${status.index}].file" cssClass="add-margin error-msg" />
			&nbsp;&nbsp;
			<c:set value="false" var="isDocFound"></c:set>
			<c:forEach items="${marriageRegistration.husband.applicantDocuments}" var="appdoc" varStatus="loopStatus">
				<c:if test="${appdoc.document.id == doc.id}">
					<c:set value="true" var="isDocFound"></c:set>
					<input type="hidden" id="husbandfile${status.index}" value="${appdoc.fileStoreMapper.fileName}|${appdoc.fileStoreMapper.contentType}|${appdoc.base64EncodedFile}">
					<a id="husbanddoc${status.index}"> ${appdoc.fileStoreMapper.fileName}</a>
				</c:if>
			</c:forEach>
			<%-- <c:if test="${!isDocFound}">
				NA
			</c:if> --%>
		</div>
		<div class="col-sm-1"></div>
		<div class="col-sm-2 add-margin text-center">
			<input type="file" id="ageproofwifefile${status.index}id" name="wife.documents[${status.index}].file" class="file-ellipsis upload-file">
			<form:errors path="wife.documents[${status.index}].file" cssClass="add-margin error-msg" />
			
			<c:set value="false" var="isDocFound"></c:set>
			<c:forEach items="${marriageRegistration.wife.applicantDocuments}" var="appdoc" varStatus="loopStatus">
				<c:if test="${appdoc.document.id == doc.id}">
					<c:set value="true" var="isDocFound"></c:set>
					<input type="hidden" id="wifefile${status.index}" value="${appdoc.fileStoreMapper.fileName}|${appdoc.fileStoreMapper.contentType}|${appdoc.base64EncodedFile}">
					<a id="wifedoc${status.index}"> ${appdoc.fileStoreMapper.fileName}</a>
				</c:if>
			</c:forEach>
			<%-- <c:if test="${!isDocFound}">
				NA
			</c:if> --%>
		</div>
	</div>
	</c:if>
</c:forEach> 
	
	<%-- <div class="form-group">
		<label class="col-sm-6 control-label text-right">
			<spring:message code="lbl.school.leaving.certificate"/>
		</label>
		<div class="col-sm-1 text-center">
			<form:checkbox path="husband.proofsAttached.schoolLeavingCertificate" value="${husband.proofsAttached.schoolLeavingCertificate}"  id="ageProofHLC"/>
			<form:errors path="husband.proofsAttached.schoolLeavingCertificate" cssClass="add-margin error-msg"/>
		</div>
		<div class="col-sm-1 text-center">			
			<form:checkbox path="wife.proofsAttached.schoolLeavingCertificate" value="${wife.proofsAttached.schoolLeavingCertificate}" id="ageProofWLC"/>
			<form:errors path="wife.proofsAttached.schoolLeavingCertificate" cssClass="add-margin error-msg"/>
		</div>
	</div>
	
	<div class="form-group">
		<label class="col-sm-6 control-label text-right">
			<spring:message code="lbl.birth.certificate"/>
		</label>
		<div class="col-sm-1 text-center">
			<form:checkbox path="husband.proofsAttached.birthCertificate" value="${husband.proofsAttached.birthCertificate}" id="ageProofHBC" />
			<form:errors path="husband.proofsAttached.birthCertificate" cssClass="add-margin error-msg"/>
		</div>
		<div class="col-sm-1 text-center">
			<form:checkbox path="wife.proofsAttached.birthCertificate" value="${wife.proofsAttached.birthCertificate}" id="ageProofWBC"/>			
			<form:errors path="wife.proofsAttached.birthCertificate" cssClass="add-margin error-msg"/>
		</div>
	</div>
	
	<div class="form-group">
		<label class="col-sm-6 control-label">
			<spring:message code="lbl.divorce.certificate"/>
		</label>
		<div class="col-sm-1 text-center">
			<form:checkbox path="husband.proofsAttached.divorceCertificate" value="${husband.proofsAttached.divorceCertificate}" id="ageProofHDC"/>
			<form:errors path="husband.proofsAttached.divorceCertificate" cssClass="add-margin error-msg"/>
		</div>
		<div class="col-sm-1 text-center">
			<form:checkbox path="wife.proofsAttached.divorceCertificate" value="${wife.proofsAttached.divorceCertificate}" id="ageProofWDC"/>
			<form:errors path="wife.proofsAttached.divorceCertificate" cssClass="add-margin error-msg"/>
		</div>
	</div>
	
	<div class="form-group">
		<label class="col-sm-6 control-label">
			<spring:message code="lbl.death.certificate"/>
		</label>
		<div class="col-sm-1 text-center">
			<form:checkbox path="husband.proofsAttached.deaceasedDeathCertificate" value="${husband.proofsAttached.deaceasedDeathCertificate}" id="ageProofHDDC"/>
			<form:errors path="husband.proofsAttached.deaceasedDeathCertificate" cssClass="add-margin error-msg"/>
		</div>
		<div class="col-sm-1 text-center">
			<form:checkbox path="wife.proofsAttached.deaceasedDeathCertificate" value="${wife.proofsAttached.deaceasedDeathCertificate}" id="ageProofWDDC"/>
			<form:errors path="wife.proofsAttached.deaceasedDeathCertificate" cssClass="add-margin error-msg"/>
		</div>
	</div>
	
	<div class="form-group">
		<label class="col-sm-6 control-label text-right">
			<spring:message code="lbl.passport"/>
		</label>
		<div class="col-sm-1 text-center">
			<form:checkbox path="husband.proofsAttached.passport" value="${husband.proofsAttached.passport}" id="ageProofHPassport"/>
			<form:errors path="husband.proofsAttached.passport" cssClass="add-margin error-msg"/>
		</div>
		<div class="col-sm-1 text-center">
			<form:checkbox path="wife.proofsAttached.passport" value="${wife.proofsAttached.passport}" id="ageProofWPassport"/>
			<form:errors path="wife.proofsAttached.passport" cssClass="add-margin error-msg"/>
		</div>
	</div>
	
	<div class="form-group">
		<label class="col-sm-6 control-label">
			<spring:message code="lbl.notary.affidavit"/>
		</label>
		<div class="col-sm-1 text-center">
			<form:checkbox path="husband.proofsAttached.notaryAffidavit" value="${husband.proofsAttached.notaryAffidavit}" id="ageProofHNA"/>
			<form:errors path="husband.proofsAttached.notaryAffidavit" cssClass="add-margin error-msg"/>
		</div>
		<div class="col-sm-1 text-center">
			<form:checkbox path="wife.proofsAttached.notaryAffidavit" value="${wife.proofsAttached.notaryAffidavit}" id="ageProofWNA"/>
			<form:errors path="wife.proofsAttached.notaryAffidavit" cssClass="add-margin error-msg"/>
		</div>
	</div>
	 --%>
	<div class="form-group">
		<label class="col-sm-5 control-label text-left view-content">
			<spring:message code="lbl.proof.of.residence"/><span class="mandatory"></span><br>
			<spring:message code="lbl.proof.note"/>
		</label>
		<div class="col-sm-6">
		</div>
	</div>
	<div><br></div>
	<c:forEach var="doc" items="${individualDocuments}" varStatus="status">	
	<c:if test="${doc.documentProofType=='ADDRESS_PROOF'}">
	<div class="form-group">	
		<div class="col-sm-1"></div>
		<form:hidden id="husband.documents[${status.index}].id" path="husband.documents[${status.index}].id" value="${doc.id}" /> 
		<form:hidden id="wife.documents[${status.index}].id" path="wife.documents[${status.index}].id" value="${doc.id}" /> 
		<div class="col-sm-4 add-margin text-right">
			<c:out value="${doc.name}"></c:out>
		</div>
		<div class="col-sm-2 add-margin text-center">
			<input type="file" id="addressproofhusbandfile${status.index}id" name="husband.documents[${status.index}].file" class="file-ellipsis upload-file">
			<form:errors path="husband.documents[${status.index}].file" cssClass="add-margin error-msg" />
			&nbsp;&nbsp;
			<c:set value="false" var="isDocFound"></c:set>
			<c:forEach items="${marriageRegistration.husband.applicantDocuments}" var="appdoc" varStatus="loopStatus">
				<c:if test="${appdoc.document.id == doc.id}">
					<c:set value="true" var="isDocFound"></c:set>
					<input type="hidden" id="husbandfile${status.index}" value="${appdoc.fileStoreMapper.fileName}|${appdoc.fileStoreMapper.contentType}|${appdoc.base64EncodedFile}">
					<a id="husbanddoc${status.index}"> ${appdoc.fileStoreMapper.fileName}</a>
				</c:if>
			</c:forEach>
			<%-- <c:if test="${!isDocFound}">
				NA
			</c:if> --%>
		</div>
		<div class="col-sm-1"></div>
		<div class="col-sm-2 add-margin text-center">
			<input type="file" id="addressproofwifefile${status.index}id" name="wife.documents[${status.index}].file" class="file-ellipsis upload-file">
			<form:errors path="wife.documents[${status.index}].file" cssClass="add-margin error-msg" />
			
			<c:set value="false" var="isDocFound"></c:set>
			<c:forEach items="${marriageRegistration.wife.applicantDocuments}" var="appdoc" varStatus="loopStatus">
				<c:if test="${appdoc.document.id == doc.id}">
					<c:set value="true" var="isDocFound"></c:set>
					<input type="hidden" id="wifefile${status.index}" value="${appdoc.fileStoreMapper.fileName}|${appdoc.fileStoreMapper.contentType}|${appdoc.base64EncodedFile}">
					<a id="wifedoc${status.index}"> ${appdoc.fileStoreMapper.fileName}</a>
				</c:if>
			</c:forEach>
			<%-- <c:if test="${!isDocFound}">
				NA
			</c:if> --%>
		</div>
	</div>
	</c:if>
</c:forEach> 
	
	
	<%-- <div class="form-group">
		<label class="col-sm-6 control-label text-right">
			<spring:message code="lbl.passport"/>
		</label>
		<div class="col-sm-1 text-center">
			<form:checkbox path="husband.proofsAttached.passport" value="${husband.proofsAttached.passport}" id="resProofHPassport"/>
			<form:errors path="husband.proofsAttached.passport" cssClass="add-margin error-msg"/>
		</div>
		<div class="col-sm-1 text-center">
			<form:checkbox path="wife.proofsAttached.passport" value="${wife.proofsAttached.passport}" id="resProofWPassport"/>
			<form:errors path="wife.proofsAttached.passport" cssClass="add-margin error-msg"/>
		</div>
	</div>
	
	<div class="form-group">
		<label class="col-sm-6 control-label text-right">
			<spring:message code="lbl.ration.card"/>
		</label>
		<div class="col-sm-1 text-center">
			<form:checkbox path="husband.proofsAttached.rationCard" value="${husband.proofsAttached.rationCard}" id="resProofHRC"/>
			<form:errors path="husband.proofsAttached.rationCard" cssClass="add-margin error-msg"/>
		</div>
		<div class="col-sm-1 text-center">
			<form:checkbox path="wife.proofsAttached.rationCard" value="${wife.proofsAttached.rationCard}" id="resProofWRC"/>
			<form:errors path="wife.proofsAttached.rationCard" cssClass="add-margin error-msg"/>
		</div>
	</div>
	
	<div class="form-group">
		<label class="col-sm-6 control-label text-right">
			<spring:message code="lbl.mseb.bill"/>
		</label>
		<div class="col-sm-1 text-center">
			<form:checkbox path="husband.proofsAttached.msebBill" value="${husband.proofsAttached.msebBill}" id="resProofHMsebBill"/>
			<form:errors path="husband.proofsAttached.msebBill" cssClass="add-margin error-msg"/>
		</div>
		<div class="col-sm-1 text-center">
			<form:checkbox path="wife.proofsAttached.msebBill" value="${wife.proofsAttached.msebBill}" id="resProofWMsebBill"/>
			<form:errors path="wife.proofsAttached.msebBill" cssClass="add-margin error-msg"/>
		</div>
	</div>
	
	<div class="form-group">
		<label class="col-sm-6 control-label text-right">
			<spring:message code="lbl.telephone.bill"/>
		</label>
		<div class="col-sm-1 text-center">
			<form:checkbox path="husband.proofsAttached.telephoneBill" value="${husband.proofsAttached.telephoneBill}" id="resProofHTeleBill"/>
			<form:errors path="husband.proofsAttached.telephoneBill" cssClass="add-margin error-msg"/>
		</div>
		<div class="col-sm-1 text-center">
			<form:checkbox path="wife.proofsAttached.telephoneBill" value="${wife.proofsAttached.telephoneBill}" id="resProofWTeleBill"/>
			<form:errors path="wife.proofsAttached.telephoneBill" cssClass="add-margin error-msg"/>
		</div>
	</div> --%>
	
	<div class="form-group">
		<label class="col-sm-6 add-margin text-right">
			<spring:message code="lbl.same.place"/>
		</label>
		<div class="col-sm-1 text-center">
			<form:checkbox path="coupleFromSamePlace" value="${coupleFromSamePlace}" />
			<form:errors path="coupleFromSamePlace" cssClass="add-margin error-msg"/>
		</div>
	</div>
	
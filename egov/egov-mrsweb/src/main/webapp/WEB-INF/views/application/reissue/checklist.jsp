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
<div class="row">
	<div class="col-sm-2"></div>
	<div class="form-group">
		<label class="col-sm-4 control-label">
			<spring:message code="lbl.photograph"/><span class="mandatory"></span>
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
	<div class="col-sm-4"></div>
</div>
<div class="row">
	<div class="col-sm-2"></div>
	<div class="form-group">
		<label class="col-sm-4 control-label text-left view-content">
			<spring:message code="lbl.proof.of.age"/><span class="mandatory"></span><br>
			<spring:message code="lbl.proof.note"/>
		</label>
		<div class="col-sm-4">
		</div>
	</div>
	<div class="col-sm-2"></div>
</div>
<div class="row">
	<div class="col-sm-2"></div>
	<div class="form-group">
		<label class="col-sm-4 control-label text-right">
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
	<div class="col-sm-4"></div>
</div>
<div class="row">
	<div class="col-sm-2"></div>
	<div class="form-group">
		<label class="col-sm-4 control-label text-right">
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
	<div class="col-sm-4"></div>
</div>
<div class="row">
	<div class="col-sm-2"></div>
	<div class="form-group">
		<label class="col-sm-4 control-label">
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
	<div class="col-sm-4"></div>
</div>
<div class="row">
	<div class="col-sm-2"></div>
	<div class="form-group">
		<label class="col-sm-4 control-label">
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
	<div class="col-sm-4"></div>
</div>
<div class="row">
	<div class="col-sm-2"></div>
	<div class="form-group">
		<label class="col-sm-4 control-label text-left view-content">
			<spring:message code="lbl.proof.of.residence"/><span class="mandatory"></span><br>
			<spring:message code="lbl.proof.note"/>
		</label>
		<div class="col-sm-4">
		</div>
	</div>
	<div class="col-sm-2"></div>
</div>
<div class="row">
	<div class="col-sm-2"></div>
	<div class="form-group">
		<label class="col-sm-4 control-label text-right">
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
	<div class="col-sm-4"></div>
</div>
<div class="row">
	<div class="col-sm-2"></div>
	<div class="form-group">
		<label class="col-sm-4 control-label text-right">
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
	<div class="col-sm-4"></div>
</div>
<div class="row">
	<div class="col-sm-2"></div>
	<div class="form-group">
		<label class="col-sm-4 control-label text-right">
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
	<div class="col-sm-4"></div>
</div>
<div class="row">
	<div class="col-sm-2"></div>
	<div class="form-group">
		<label class="col-sm-4 control-label text-right">
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
	</div>
	<div class="col-sm-4"></div>
</div>
<div class="row">
	<div class="col-sm-2"></div>
	<div class="form-group">
		<label class="col-sm-4 add-margin">
			<spring:message code="lbl.same.place"/>
		</label>
		<div class="col-sm-1 text-center">
			<form:checkbox path="coupleFromSamePlace" value="${coupleFromSamePlace}" />
			<form:errors path="coupleFromSamePlace" cssClass="add-margin error-msg"/>
		</div>
	</div>
	<div class="col-sm-4"></div>
</div>

<jsp:include page="../registration/documentdetails.jsp"></jsp:include>
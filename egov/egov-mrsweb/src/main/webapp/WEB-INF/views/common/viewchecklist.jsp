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


<div class="row">
	<div class="col-sm-2"></div>
	<div class="form-group">
		<label class="col-sm-4 add-margin view-content">
			<spring:message code="lbl.common.docs"/>
		</label>
		<div class="col-sm-1 text-center view-content">
			<spring:message code="lbl.submitted.by.couple"/>
		</div>
	</div>
	<div class="col-sm-2"></div>
</div>
<div class="row">
	<div class="col-sm-2"></div>
	<div class="form-group">
		<label class="col-sm-4 add-margin">
			<spring:message code="lbl.memo.of.marriage"/><span class="mandatory"></span>
		</label>
		<div class="col-sm-1 text-center">
			<form:checkbox path="registration.memorandumOfMarriage" cssClass="disabled" disabled="true" />
		</div>
	</div>
	<div class="col-sm-4"></div>
</div>
<div class="row">
	<div class="col-sm-2"></div>
	<div class="form-group">
		<label class="col-sm-4 add-margin">
			<spring:message code="lbl.court.fee.stamp"/><span class="mandatory"></span>
		</label>
		<div class="col-sm-1 text-center">
			<form:checkbox path="registration.courtFeeStamp" cssClass="disabled" disabled="true"/>
		</div>
	</div>
	<div class="col-sm-4"></div>
</div>
<div class="row">
	<div class="col-sm-2"></div>
	<div class="form-group">
		<label class="col-sm-4 add-margin">
			<spring:message code="lbl.court.affidavit"/>
		</label>
		<div class="col-sm-1 text-center">
			<form:checkbox path="registration.affidavit" cssClass="disbled" disabled="true"/>
		</div>
		<div class="col-sm-1"></div>
	</div>
	<div class="col-sm-4"></div>
</div>
<div class="row">
	<div class="col-sm-2"></div>
	<div class="form-group">
		<label class="col-sm-4 add-margin">
			<spring:message code="lbl.marriage.card"/>
		</label>
		<div class="col-sm-1 text-center">
			<form:checkbox path="registration.marriageCard" cssClass="disbled" disabled="true"/>
		</div>
		<div class="col-sm-1"></div>
	</div>
	<div class="col-sm-4"></div>
</div>
<div class="row">
	<div class="col-sm-2"></div>
	<div class="form-group">
		<label class="col-sm-4 text-left view-content">
			<spring:message code="lbl.indi.docs"/>
		</label>
		<div class="col-sm-1 text-center view-content">
			<spring:message code="lbl.husband"/>
		</div>
		<div class="col-sm-1 text-center view-content">
			<spring:message code="lbl.wife"/>
		</div>
	</div>
	<div class="col-sm-1"></div>
</div>
<div class="row">
	<div class="col-sm-2"></div>
	<div class="form-group">
		<label class="col-sm-4 add-margin">
			<spring:message code="lbl.photograph"/><span class="mandatory"></span>
		</label>
		<div class="col-sm-1 text-center">
			<form:checkbox path="registration.husband.proofsAttached.photograph" value="${registration.husband.proofsAttached.photograph}" cssClass="disbled" disabled="true"/>
		</div>
		<div class="col-sm-1 text-center">
			<form:checkbox path="registration.wife.proofsAttached.photograph" value="${registration.wife.proofsAttached.photograph}" cssClass="disbled" disabled="true"/>
		</div>
	</div>
	<div class="col-sm-4"></div>
</div>
<div class="row">
	<div class="col-sm-2"></div>
	<div class="form-group">
		<label class="col-sm-4 add-margin text-left view-content">
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
		<label class="col-sm-4 add-margin text-right">
			<spring:message code="lbl.school.leaving.certificate"/>
		</label>
		<div class="col-sm-1 text-center">
			<form:checkbox path="registration.husband.proofsAttached.schoolLeavingCertificate" value="${registration.husband.proofsAttached.schoolLeavingCertificate}" cssClass="disbled" disabled="true" />
		</div>
		<div class="col-sm-1 text-center">			
			<form:checkbox path="registration.wife.proofsAttached.schoolLeavingCertificate" value="${registration.wife.proofsAttached.schoolLeavingCertificate}" cssClass="disbled" disabled="true"/>
		</div>
	</div>
	<div class="col-sm-4"></div>
</div>
<div class="row">
	<div class="col-sm-2"></div>
	<div class="form-group">
		<label class="col-sm-4 add-margin text-right">
			<spring:message code="lbl.birth.certificate"/>
		</label>
		<div class="col-sm-1 text-center">
			<form:checkbox path="registration.husband.proofsAttached.birthCertificate" value="${registration.husband.proofsAttached.birthCertificate}" cssClass="disbled" disabled="true"/>
		</div>
		<div class="col-sm-1 text-center">
			<form:checkbox path="registration.wife.proofsAttached.birthCertificate" value="${registration.wife.proofsAttached.birthCertificate}" cssClass="disbled" disabled="true"/>			
		</div>
	</div>
	<div class="col-sm-4"></div>
</div>
<div class="row">
	<div class="col-sm-2"></div>
	<div class="form-group">
		<label class="col-sm-4 add-margin">
			<spring:message code="lbl.divorce.certificate"/>
		</label>
		<div class="col-sm-1 text-center">
			<form:checkbox path="registration.husband.proofsAttached.divorceCertificate" value="${registration.husband.proofsAttached.divorceCertificate}" cssClass="disbled" disabled="true" />
		</div>
		<div class="col-sm-1 text-center">
			<form:checkbox path="registration.wife.proofsAttached.divorceCertificate" value="${registration.wife.proofsAttached.divorceCertificate}" cssClass="disbled" disabled="true"/>
		</div>
	</div>
	<div class="col-sm-4"></div>
</div>
<div class="row">
	<div class="col-sm-2"></div>
	<div class="form-group">
		<label class="col-sm-4 add-margin">
			<spring:message code="lbl.death.certificate"/>
		</label>
		<div class="col-sm-1 text-center">
			<form:checkbox path="registration.husband.proofsAttached.deaceasedDeathCertificate" value="${registration.husband.proofsAttached.deaceasedDeathCertificate}" cssClass="disbled" disabled="true" />
		</div>
		<div class="col-sm-1 text-center">
			<form:checkbox path="registration.wife.proofsAttached.deaceasedDeathCertificate" value="${registration.wife.proofsAttached.deaceasedDeathCertificate}" cssClass="disbled" disabled="true" />
		</div>
	</div>
	<div class="col-sm-4"></div>
</div>
<div class="row">
	<div class="col-sm-2"></div>
	<div class="form-group">
		<label class="col-sm-4 text-left view-content">
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
		<label class="col-sm-4 add-margin text-right">
			<spring:message code="lbl.school.leaving.certificate"/>
		</label>
		<div class="col-sm-1 text-center">
			<form:checkbox path="registration.husband.proofsAttached.passport" value="${registration.husband.proofsAttached.passport}" cssClass="disbled" disabled="true" />
		</div>
		<div class="col-sm-1 text-center">
			<form:checkbox path="registration.wife.proofsAttached.passport" value="${registration.wife.proofsAttached.passport}" cssClass="disbled" disabled="true" />
		</div>
	</div>
	<div class="col-sm-4"></div>
</div>
<div class="row">
	<div class="col-sm-2"></div>
	<div class="form-group">
		<label class="col-sm-4 add-margin text-right">
			<spring:message code="lbl.birth.certificate"/>
		</label>
		<div class="col-sm-1 text-center">
			<form:checkbox path="registration.husband.proofsAttached.rationCard" value="${registration.husband.proofsAttached.rationCard}" cssClass="disbled" disabled="true" />
		</div>
		<div class="col-sm-1 text-center">
			<form:checkbox path="registration.wife.proofsAttached.rationCard" value="${registration.wife.proofsAttached.rationCard}" cssClass="disbled" disabled="true" />
		</div>
	</div>
	<div class="col-sm-4"></div>
</div>
<div class="row">
	<div class="col-sm-2"></div>
	<div class="form-group">
		<label class="col-sm-4 add-margin text-right">
			<spring:message code="lbl.school.leaving.certificate"/>
		</label>
		<div class="col-sm-1 text-center">
			<form:checkbox path="registration.husband.proofsAttached.msebBill" value="${registration.husband.proofsAttached.msebBill}" cssClass="disbled" disabled="true" />
		</div>
		<div class="col-sm-1 text-center">
			<form:checkbox path="registration.wife.proofsAttached.msebBill" value="${registration.wife.proofsAttached.msebBill}" cssClass="disbled" disabled="true" />
		</div>
	</div>
	<div class="col-sm-4"></div>
</div>
<div class="row">
	<div class="col-sm-2"></div>
	<div class="form-group">
		<label class="col-sm-4 add-margin text-right">
			<spring:message code="lbl.birth.certificate"/>
		</label>
		<div class="col-sm-1 text-center">
			<form:checkbox path="registration.husband.proofsAttached.telephoneBill" value="${registration.husband.proofsAttached.telephoneBill}" cssClass="disbled" disabled="true" />
		</div>
		<div class="col-sm-1 text-center">
			<form:checkbox path="registration.wife.proofsAttached.telephoneBill" value="${registration.wife.proofsAttached.telephoneBill}" cssClass="disbled" disabled="true" />
		</div>
	</div>
	<div class="col-sm-4"></div>
</div>
<div class="row">
	<div class="col-sm-2"></div>
	<div class="form-group">
		<label class="col-sm-4 add-margin">
			<spring:message code="lbl.same.place"/><span class="mandatory"></span>
		</label>
		<div class="col-sm-1 text-center">
			<form:checkbox path="registration.coupleFromSamePlace" value="${registration.coupleFromSamePlace}" cssClass="disbled" disabled="true" />
		</div>
	</div>
	<div class="col-sm-4"></div>
</div>

<%-- <jsp:include page="viewdocumentdetails.jsp"></jsp:include> --%>
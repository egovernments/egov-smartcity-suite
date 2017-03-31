<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
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
  --%>
<%@ page contentType="text/html" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<div class="row">
	<div class="col-md-12">
		<div class="panel panel-primary" data-collapsed="0">
			<div class="panel-heading">
				<c:if test="${mode == 'view' }">
					<div class="panel-title" style="text-align:center;" ><spring:message code="lbl.viewcontractor" /></div>
				</c:if>
			</div>
			<input type="hidden" value="${mode}" id="mode" />
			<input type="hidden" value="${contractorMasterHiddenFields }" id="hide" />
			<div class="panel-body">
				<div class="row add-border">
					<div class="col-xs-3 add-margin" id = "codeLable">
						<spring:message code="lbl.code" />
					</div>
					<div class="col-xs-3 add-margin view-content" id = "code-value">
						<c:out value="${contractor.code}" />
					</div>
					<div class="col-xs-3 add-margin" id = "nameLabel">
						<spring:message code="lbl.name" />
					</div>
					<div class="col-xs-3 add-margin view-content" id = 'name-value'>
						<c:out value="${contractor.name}" />
					</div>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin" id = "correspondenceAddressLabel">
						<spring:message code="lbl.correspondenceaddress" />
					</div>
					<div class="col-xs-3 add-margin view-content" id = "correspondenceAddress-value">
						<c:out value="${contractor.correspondenceAddress}" />
					</div>
					<div class="col-xs-3 add-margin" id = "paymentAddressLabel">
						<spring:message code="lbl.paymentaddress" />
					</div>
					<div class="col-xs-3 add-margin view-content" id = "paymentAddress-value">
						<c:out value="${contractor.paymentAddress}" />
					</div>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin" id = "contactPersonLabel">
						<spring:message code="lbl.contactperson" />
					</div>
					<div class="col-xs-3 add-margin view-content" id = "contactPerson-value">
						<c:out value="${contractor.contactPerson}" />
					</div>
					<div class="col-xs-3 add-margin" id = "emailLabel">
						<spring:message code="lbl.email" />
					</div>
					<div class="col-xs-3 add-margin view-content" id = "email-value">
						<c:out value="${contractor.email}" />
					</div>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin" id = "narrationLabel">
						<spring:message code="lbl.narration" />
					</div>
					<div class="col-xs-3 add-margin view-content" id = "narration-value">
						<c:out value="${contractor.narration}" />
					</div>
					<div class="col-xs-3 add-margin" id = "mobileNumberLabel">
						<spring:message code="lbl.mobilenumber" />
					</div>
					<div class="col-xs-3 add-margin view-content" id = "mobileNumber-value">
						<c:out value="${contractor.mobileNumber}" />
					</div>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin" id = "panNumberLabel">
						<spring:message code="lbl.pannumber" />
					</div>
					<div class="col-xs-3 add-margin view-content" id = "panNumber-value">
						<c:out value="${contractor.panNumber}" />
					</div>
					<div class="col-xs-3 add-margin" id = "tinnumberLabel">
						<spring:message code="lbl.tinnumber" />
					</div>
					<div class="col-xs-3 add-margin view-content" id = "tinnumber-value">
						<c:out value="${contractor.tinNumber}" />
					</div>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin" id = "bankLabel">
						<spring:message code="lbl.bank" />
					</div>
					<div class="col-xs-3 add-margin view-content" id = "bank-value">
						<c:out value="${contractor.bank.name}" />
					</div>
					<div class="col-xs-3 add-margin" id = "ifscCodeLabel">
						<spring:message code="lbl.ifsccode" />
					</div>
					<div class="col-xs-3 add-margin view-content" id = "ifscCode-value">
						<c:out value="${contractor.ifscCode}" />
					</div>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin" id = "bankAccountLabel">
						<spring:message code="lbl.bankaccount" />
					</div>
					<div class="col-xs-3 add-margin view-content" id = "bankAccount-value">
						<c:out value="${contractor.bankAccount}" />
					</div>
					<div class="col-xs-3 add-margin" id = "pwdApprovalCodeLabel">
						<spring:message code="lbl.pwdapprovalcode" />
					</div>
					<div class="col-xs-3 add-margin view-content" id = "pwdApprovalCode-value">
						<c:out value="${contractor.pwdApprovalCode}" />
					</div>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin" id = "exemptionFormLabel">
						<spring:message code="lbl.exemptionform" />
					</div>
					<div class="col-xs-3 add-margin view-content" id = "exemptionForm-value">
						<c:out value="${contractor.exemptionForm}" />
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="panel panel-primary" data-collapsed="0">
	<div class="panel-heading">
		<div class="panel-title">
			<spring:message code="lbl.contractordetails" />
		</div>
	</div>
	<div class="panel-body">
		<table class="table table-bordered">
			<thead>
				<tr>
					<th id = "department"><spring:message code="lbl.department" /></th>
					<th id = "registrationNumber"><spring:message code="lbl.registrationno" /></th>
					<th id = "category"><spring:message code="lbl.category" /></th>
					<th id = "contractorClass"><spring:message code="lbl.contractorclass" /></th>
					<th id = "status"><spring:message code="lbl.status" /></th>
					<th id = "fromDate"><spring:message code="lbl.fromdate" /></th>
					<th id = "toDate"><spring:message code="lbl.todate" /></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach	var="contractorDetails" items = "${contractor.contractorDetails }">
					<tr>
						<td><c:out value="${contractorDetails.department.name}"></c:out></td>
						<td><c:out value="${contractorDetails.registrationNumber}"></c:out></td>
						<td><c:out value="${contractorDetails.category}"></c:out></td>
						<td><c:out value="${contractorDetails.grade.grade}"></c:out></td>
						<td><c:out value="${contractorDetails.status.code}"></c:out></td>
						<td><fmt:formatDate value="${contractorDetails.validity.startDate}" pattern="dd/MM/yyyy" /></td>
						<td><fmt:formatDate value="${contractorDetails.validity.endDate}" pattern="dd/MM/yyyy" /></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>
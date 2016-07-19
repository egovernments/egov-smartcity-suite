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

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<form:form method="post" action="create"
	class="form-horizontal form-groups-bordered" modelAttribute="judgment"
	id="judgmentForm">
	<input type="hidden" name="mode" value="${mode}" />
	<div class="row">
		<div class="col-md-12">
			<div class="panel panel-primary" data-collapsed="0">
			<div class="panel-body custom-form ">
				<c:if test="${not empty message}">
					<div class=role="alert">${message}</div>
				</c:if>
				<div class="panel-heading">
					<div class="panel-title">
						<spring:message code="title.legalCase.view" />
					</div>
				</div>
				<div class="panel-body">
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.courttype" />
						</div>
						<div class="col-xs-3 add-margin view-content">
							<c:out value="${legalCase.courtMaster.courtType.courtType}" />
						</div>

						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.petitiontype" />
						</div>
						<div class="col-xs-3 add-margin view-content">
							<c:out value="${legalCase.petitionTypeMaster.code}" />
						</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.court" />
						</div>
						<div class="col-xs-3 add-margin view-content">
							<c:out value="${legalCase.courtMaster.name}" />
						</div>
						<div class="col-xs-3 add-margin">Case Type</div>
						<div class="col-xs-3 add-margin view-content">
							<c:out value="${legalCase.caseTypeMaster.code}" />
						</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.caseNumber" />
						</div>
						<div class="col-xs-3 add-margin view-content">
							<c:out value="${legalCase.caseNumber}" />
						</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.lcnumber" />
						</div>
						<div class="col-xs-3 add-margin view-content">
							<c:out value="${legalCase.lcNumber}" />
						</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.casedate" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							<fmt:formatDate pattern="dd/MM/yyyy"
								value="${legalCase.caseDate}" var="casedate" />
							<c:out value="${casedate}" />
						</div>
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.previouscaseNumber" />
						</div>
						<div class="col-xs-3 add-margin view-content">
							<c:out value="${legalCase.appealNum}" />
						</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.title" />
						</div>
						<div class="col-xs-3 add-margin view-content">
							<c:out value="${legalCase.caseTitle}" />
						</div>
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.prayer" />
						</div>
						<div class="col-xs-3 add-margin view-content">
							<c:out value="${legalCase.prayer}" />
						</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.case.receivingdate" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							<fmt:formatDate pattern="dd/MM/yyyy"
								value="${legalCase.caseReceivingDate}" var="casercdate" />
							<c:out value="${casercdate}" />
						</div>
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.caDue.date" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							<fmt:formatDate pattern="dd/MM/yyyy"
								value="${legalCase.caDueDate}" var="caduedate" />
							<c:out value="${caduedate}" />
						</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.fieldbycarp" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${legalCase.isfiledbycorporation}</div>
					</div>


				</div>

			</div>
		</div>
	</div>
	<div class="panel-heading">
		<div class="panel-title">
			<spring:message code="lbl.bipartisanDetails.details" />
		</div>
	</div>
	<table class="table table-striped table-bordered" id="petitionDetails">
		<thead>
			<tr>
				<th class="text-center"><spring:message code="lbl.IsGovtDept" /></th>
				<th class="text-center"><spring:message code="lbl.name" /><span
					class="mandatory"></span></th>
				<th class="text-center"><spring:message code="lbl.discription" /></th>
				<th class="text-center"><spring:message
						code="lbl.contactnumber" /></th>
				<th class="text-center"><spring:message code="lbl.Govt_Dept" /></th>
				<th class="text-center"><spring:message
						code="lbl.add/delete_pet" /></th>
			</tr>
		</thead>
		<tbody>
			<tr class="">
				<td class="text-center"><input type="checkbox" id="activeid"
					name="bipartisanDetails[0].isRespondentGovernment"
					id="bipartisanDetails[0].isRespondentGovernment"
					readonly="readonly" onblur="onChangeofPetitioncheck()" /></td>
				<td class="text-right"><input type="text"
					class="form-control table-input text-right"
					data-pattern="alphanumerichyphenbackslash"
					name="bipartisanDetails[0].name" id="bipartisanDetails[0].name"
					readonly="readonly" maxlength="50" required="required"></td>
				<td class="text-right"><input type="text"
					class="form-control table-input"
					name="bipartisanDetails[0].address" readonly="readonly"
					id="bipartisanDetails[0].address" maxlength="256"></td>
				<td class="text-right"><input type="text"
					class="form-control table-input text-right patternvalidation"
					data-pattern="number" name="bipartisanDetails[0].contactNumber"
					readonly="readonly" id="bipartisanDetails[0].contactNumber"
					maxlength="10"></td>
				<td class="text-right"><form:select path=""
						data-first-option="false"
						name="bipartisanDetails[0].governmentDepartment"
						readonly="readonly" id="bipartisanDetails[0].governmentDepartment"
						cssClass="form-control" onfocus="callAlertForDepartment();"
						cssErrorClass="form-control error">
						<form:option value="">
							<spring:message code="lbl.select" />
						</form:option>
						<form:options items="${govtDeptList}" itemValue="id"
							itemLabel="code" />
					</form:select></td>
				<td class="text-center" readonly="readonly"><a
					href="javascript:void(0);" class="btn-sm btn-default"
					onclick="addPetRow();"><i class="fa fa-plus"></i></a> <a
					href="javascript:void(0);" class="btn-sm btn-default"
					id="pet_delete_row"><i class="fa fa-trash"></i></a></td>
			</tr>
		</tbody>
	</table>

	<div class="panel-heading">
		<div class="panel-title">
			<spring:message code="lbl.bipartisanDetails.respondant" />
		</div>
	</div>
	<table class="table table-striped table-bordered" id="respodantDetails">
		<thead>
			<tr>
				<th class="text-center"><spring:message code="lbl.IsGovtDept" /></th>
				<th class="text-center"><spring:message code="lbl.name" /><span
					class="mandatory"></span></th>
				<th class="text-center"><spring:message code="lbl.discription" /></th>
				<th class="text-center"><spring:message
						code="lbl.contactnumber" /></th>
				<th class="text-center"><spring:message code="lbl.Govt_Dept" /></th>
				<th class="text-center"><spring:message
						code="lbl.add/delete_pet" /></th>
			</tr>
		</thead>
		<tbody>
			<tr class="">
				<td class="text-center"><input type="checkbox" id="activeid"
					name="bipartisanDetailsBeanList[0].isRespondentGovernment"
					readonly="readonly"
					id="bipartisanDetailsBeanList[0].isRespondentGovernment"
					onblur="onChangeofRespodantcheck()" /></td>
				<td class="text-right"><input type="text"
					class="form-control table-input text-right"
					data-pattern="alphanumerichyphenbackslash"
					name="bipartisanDetailsBeanList[0].name" readonly="readonly"
					id="bipartisanDetailsBeanList[0].name" maxlength="50"
					required="required"></td>
				<td class="text-right"><input type="text"
					class="form-control table-input"
					name="bipartisanDetailsBeanList[0].address" readonly="readonly"
					id="bipartisanDetailsBeanList[0].address" maxlength="256"></td>
				<td class="text-right"><input type="text"
					class="form-control table-input text-right patternvalidation"
					data-pattern="number"
					name="bipartisanDetailsBeanList[0].contactNumber"
					readonly="readonly" id="bipartisanDetailsBeanList[0].contactNumber"
					maxlength="10"></td>
				<td class="text-right"><form:select path=""
						data-first-option="false"
						name="bipartisanDetailsBeanList[0].governmentDepartment"
						readonly="readonly"
						id="bipartisanDetailsBeanList[0].governmentDepartment"
						cssClass="form-control" onfocus="callAlertForDepartment();"
						cssErrorClass="form-control error">
						<form:option value="">
							<spring:message code="lbl.select" />
						</form:option>
						<form:options items="${govtDeptList}" itemValue="id"
							itemLabel="code" />
					</form:select></td>

				<td class="text-center" readonly="readonly"><a
					href="javascript:void(0);" class="btn-sm btn-default"
					onclick="addResRow();"><i class="fa fa-plus"></i></a> <a
					href="javascript:void(0);" class="btn-sm btn-default"
					id="res_delete_row"><i class="fa fa-trash"></i></a></td>
			</tr>
		</tbody>
	</table>

</form:form>
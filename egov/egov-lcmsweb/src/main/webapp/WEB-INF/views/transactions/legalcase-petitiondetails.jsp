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

<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="panel-heading">
	<div class="panel-title">
		<spring:message code="lbl.bipartisanDetails.details" />
	</div>
</div>
<table class="table table-striped table-bordered" id="petitionDetails">
	<thead>
		<tr>
			<th class="text-center"><spring:message code="lbl.slno" /> <%-- <th class="text-center"><spring:message code="lbl.IsGovtDept" /></th>  --%>
			<th class="text-center"><spring:message code="lbl.name" /><span
				class="mandatory"></span></th>
			<th class="text-center"><spring:message code="lbl.discription" /></th>
			<th class="text-center"><spring:message code="lbl.contactnumber" /></th>
			<%--  <th class="text-center"><spring:message code="lbl.Govt_Dept" /></th>  --%>
			<th class="text-center"><spring:message
					code="lbl.add/delete_pet" /></th>
		</tr>
	</thead>
	<tbody>
		<c:choose>
			<c:when test="${not empty bipartisanPetitionerDetailsList}">
				<c:forEach var="bipartisanPetitionerDetailsList"
					items="${bipartisanPetitionerDetailsList}" varStatus="status">
					<tr class="">
						<td><span class="petitionDetails spansno">1</span></td>
						<%-- 	<td>
								<input type="text" id="table_name${status.index}" class="form-control"
								readonly="readonly" style="text-align: center"
								value="${status.index+1}" /> 
								</td> --%>
						<%-- <td class="text-center"><input type="checkbox" id="bipartisanPetitionerDetailsList[${status.index}].isRespondentGovernment"
				name="bipartisanPetitionerDetailsList[${status.index}].isRespondentGovernment"
			value="${bipartisanPetitionerDetailsList.isRespondentGovernment}"
				onblur="onChangeofPetitioncheck(this)" /></td>
			 --%>
						<td class="text-right"><input type="text"
							class="form-control table-input text-left"
							id="bipartisanPetitionerDetailsList[${status.index}].name"
							name="bipartisanPetitionerDetailsList[${status.index}].name"
							value="${bipartisanPetitionerDetailsList.name}" maxlength="50"
							required="required" /></td>

						<td class="text-right"><input type="text"
							class="form-control table-input"
							name="bipartisanPetitionerDetailsList[${status.index}].address"
							id="bipartisanPetitionerDetailsList[${status.index}].address"
							value="${bipartisanPetitionerDetailsList.address}"
							maxlength="256" /></td>
						<td class="text-right"><input type="text"
							class="form-control table-input text-left" data-pattern="number"
							name="bipartisanPetitionerDetailsList[${status.index}].contactNumber"
							id="bipartisanPetitionerDetailsList[${status.index}].contactNumber"
							value="${bipartisanPetitionerDetailsList.contactNumber}"
							maxlength="10" /></td>
						<%-- <td class="text-right"><form:select path=""
					data-first-option="false"
					name="bipartisanPetitionerDetailsList[${status.index}].governmentDepartment"
					id="bipartisanPetitionerDetailsList[${status.index}].governmentDepartment"
					value="${bipartisanPetitionerDetailsList.governmentDepartment.name}"
					cssClass="form-control"
					cssErrorClass="form-control error">
					<form:options items="${govtDeptList}" itemValue="id"
						itemLabel="code" />
				</form:select></td> --%>
						<input type="hidden"
							name="bipartisanPetitionerDetailsList[${status.index}].id"
							id="bipartisanPetitionerDetailsList[${status.index}].id"
							value="${bipartisanPetitionerDetailsList.id}" />
						<td class="text-center"><a href="javascript:void(0);"
							class="btn-sm btn-default" onclick="addPetRow();"><span
								style="cursor: pointer;"><i class="fa fa-plus"></i></span></a> <a
							href="javascript:void(0);" class="btn-sm btn-default"
							id="pet_delete_row"><span style="cursor: pointer;"> <i
									class="fa fa-trash"></i></span></a></td>
					</tr>
				</c:forEach>
			</c:when>
			<c:otherwise>

				<tr class="">
					<td><span class="petitionDetails spansno">1</span> <form:hidden
							path="bipartisanPetitionerDetailsList[0].id"
							name="bipartisanPetitionerDetailsList[0].id"
							value="${bipartisanPetitionerDetailsList[0].id}"
							class="form-control table-input hidden-input" /></td>
					<!-- <td class="text-center"><input type="checkbox" id="activeid"
				name="bipartisanPetitionerDetailsList[0].isRespondentGovernment"
				id="bipartisanPetitionerDetailsList[0].isRespondentGovernment"
				onblur="onChangeofPetitioncheck(this)" /></td> -->
					<td class="text-right"><input type="text"
						class="form-control table-input text-left"
						data-pattern="alphanumerichyphenbackslash"
						name="bipartisanPetitionerDetailsList[0].name"
						id="bipartisanPetitionerDetailsList[0].name" maxlength="50"
						required="required"></td>
					<td class="text-right"><input type="text"
						class="form-control table-input"
						name="bipartisanPetitionerDetailsList[0].address"
						id="bipartisanPetitionerDetailsList[0].address" maxlength="256"></td>
					<td class="text-right"><input type="text"
						class="form-control table-input text-left patternvalidation"
						name="bipartisanPetitionerDetailsList[0].contactNumber"
						id="bipartisanPetitionerDetailsList[0].contactNumber"
						onkeyup="decimalvalue(this);" maxlength="10"></td>
					<%-- <td class="text-right"><form:select path=""
					data-first-option="false"
					name="bipartisanPetitionerDetailsList[0].governmentDepartment"
					id="bipartisanPetitionerDetailsList[0].governmentDepartment"
					cssClass="form-control" 
					cssErrorClass="form-control error">
					<form:option value="">
						<spring:message code="lbl.select" />
					</form:option>
					<form:options items="${govtDeptList}" itemValue="id"
						itemLabel="code" />
				</form:select></td> --%>
					<td class="text-center"><a href="javascript:void(0);"
						class="btn-sm btn-default" onclick="addPetRow();"><span
							style="cursor: pointer;"><i class="fa fa-plus"></i></span></a> <a
						href="javascript:void(0);" class="btn-sm btn-default"
						id="pet_delete_row"><span style="cursor: pointer;"
							id="addRowId"><i class="fa fa-trash"></i></span></a></td>
				</tr>
			</c:otherwise>
		</c:choose>

	</tbody>
</table>


<div class="panel-heading">
	<div class="panel-title">
		<spring:message code="lbl.bipartisanDetails.respondant" />
	</div>
</div>
<table class="table table-striped table-bordered" id="respondantDetails">
	<thead>
		<tr>
			<th class="text-center"><spring:message code="lbl.slno" /> <%-- <th class="text-center"><spring:message code="lbl.IsGovtDept" /></th> --%>
			<th class="text-center"><spring:message code="lbl.name" /><span
				class="mandatory"></span></th>
			<th class="text-center"><spring:message code="lbl.discription" /></th>
			<th class="text-center"><spring:message code="lbl.contactnumber" /></th>
			<%-- <th class="text-center"><spring:message code="lbl.Govt_Dept" /></th> --%>
			<th class="text-center"><spring:message
					code="lbl.add/delete_Res" /></th>
		</tr>
	</thead>
	<tbody>
		<c:choose>
			<c:when test="${not empty bipartisanRespondentDetailsList}">
				<c:forEach var="bipartisanRespondentDetailsList"
					items="${bipartisanRespondentDetailsList}" varStatus="status">
					<tr>
						<td><span class="respondantDetails spansno">1</span></td>
						<%-- 	<td>
								<input type="text" id="table_name${status.index}" class="form-control"
								readonly="readonly" style="text-align: center"
								value="${status.index+1}" /> 
								</td> --%>
						<%-- <td class="text-center"><input type="checkbox" id="bipartisanRespondentDetailsList[${status.index}].isRespondentGovernment"
				name="bipartisanRespondentDetailsList[${status.index}].isRespondentGovernment"
			value="${bipartisanRespondentDetailsList.isRespondentGovernment}"
				onblur="onChangeofPetitioncheck(this)" /></td> --%>

						<td><input type="text"
							class="form-control table-input text-left"
							id="bipartisanRespondentDetailsList[${status.index}].name"
							name="bipartisanRespondentDetailsList[${status.index}].name"
							value="${bipartisanRespondentDetailsList.name}" /></td>


						<td><input type="text-right"
							class="form-control table-input text-right"
							id="bipartisanRespondentDetailsList[${status.index}].address"
							name="bipartisanRespondentDetailsList[${status.index}].address"
							value="${bipartisanRespondentDetailsList.address}" /></td>
						<td class="text-right"><input type="text"
							id="bipartisanRespondentDetailsList[${status.index}].contactNumber"
							name="bipartisanRespondentDetailsList[${status.index}].contactNumber"
							class="form-control table-input text-left patternvalidation"
							value="${bipartisanRespondentDetailsList.contactNumber}" /></td>
						<%-- 
				<td>
					<form:select path="" data-first-option="false"
						name="bipartisanRespondentDetailsList[${status.index}].governmentDepartment"
						id="bipartisanRespondentDetailsList[${status.index}].governmentDepartment"
						cssClass="form-control" 
						value="${bipartisanRespondentDetailsList.governmentDepartment.name}"
						cssErrorClass="form-control error">
						<form:options items="${govtDeptList}" itemValue="id"
							itemLabel="code" />
					</form:select>
				</td> --%>
						<input type="hidden" id="activeid"
							name="bipartisanRespondentDetailsList[${status.index}].id"
							id="bipartisanRespondentDetailsList[${status.index}].id"
							value="${bipartisanRespondentDetailsList.id}" />
						<input type="hidden"
							id="bipartisanRespondentDetailsList[${status.index}].isRepondent"
							name="bipartisanRespondentDetailsList[${status.index}].isRepondent"
							class="form-control table-input text-right"
							style="text-align: center" value="${true}" />
						<td class="text-center"><a href="javascript:void(0);"
							class="btn-sm btn-default" onclick="addResRow();"><i
								class="fa fa-plus"></i></a> <a href="javascript:void(0);"
							class="btn-sm btn-default" id="res_delete_row"><i
								class="fa fa-trash"></i></a></td>
					</tr>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<tr class="">
					<td><span class="respondantDetails spansno">1</span> <form:hidden
							path="bipartisanRespondentDetailsList[0].id"
							name="bipartisanRespondentDetailsList[0].id"
							value="${bipartisanRespondentDetailsList[0].id}"
							class="form-control table-input hidden-input" /></td>
					<!-- <td class="text-center"><input type="checkbox" id="activeid"
				name="bipartisanRespondentDetailsList[0].isRespondentGovernment"
				id="bipartisanRespondentDetailsList[0].isRespondentGovernment"
				onblur="onChangeofPetitioncheck(this)" /></td> -->
					<td class="text-right"><input type="text"
						class="form-control table-input text-left"
						data-pattern="alphanumerichyphenbackslash"
						name="bipartisanRespondentDetailsList[0].name"
						id="bipartisanRespondentDetailsList[0].name" maxlength="50"
						required="required"></td>
					<td class="text-right"><input type="text"
						class="form-control table-input"
						name="bipartisanRespondentDetailsList[0].address"
						id="bipartisanRespondentDetailsList[0].address" maxlength="256"></td>
					<td class="text-right"><input type="text"
						class="form-control table-input text-left patternvalidation"
						name="bipartisanRespondentDetailsList[0].contactNumber"
						id="bipartisanRespondentDetailsList[0].contactNumber"
						onkeyup="decimalvalue(this);" maxlength="10"></td>
					<%-- 	<td class="text-right"><form:select path=""
					data-first-option="false"
					name="bipartisanRespondentDetailsList[0].governmentDepartment"
					id="bipartisanRespondentDetailsList[0].governmentDepartment"
					cssClass="form-control"
					cssErrorClass="form-control error">
					<form:option value="">
						<spring:message code="lbl.select" />
					</form:option>
					<form:options items="${govtDeptList}" itemValue="id"
						itemLabel="code" />
				</form:select></td> --%>
					<input type="hidden"
						id="bipartisanRespondentDetailsList[0].isRepondent"
						name="bipartisanRespondentDetailsList[0].isRepondent"
						class="form-control table-input text-right"
						style="text-align: center" value="${true}" />

					<td class="text-center"><a href="javascript:void(0);"
						class="btn-sm btn-default" onclick="addResRow();"><span
							style="cursor: pointer;"><i class="fa fa-plus"></i></span></a> <a
						href="javascript:void(0);" class="btn-sm btn-default"
						id="res_delete_row"><span style="cursor: pointer;"><i
								class="fa fa-trash"></i></span></a></td>
				</tr>
			</c:otherwise>
		</c:choose>
	</tbody>
</table>
<div class="form-group">
	<label class="col-sm-3 control-label text-right" id="persons"><spring:message
			code="lbl.representedby" />:</label>
	<div class="col-sm-3 add-margin" id="personsdiv">
		<form:input class="form-control patternvalidation"
			data-pattern="string" maxlength="50" id="representedby"
			path="representedby" />
		<form:errors path="representedby" cssClass="add-margin error-msg" />
	</div>

	<label class="col-sm-2 control-label text-right" id="persons">
		Standing Council Name:</label>
	<div class="col-sm-3 add-margin" id="personsdiv">
		<form:input class="form-control patternvalidation"
			data-pattern="string" maxlength="50" id="oppPartyAdvocate"
			path="oppPartyAdvocate" />
		<form:errors path="oppPartyAdvocate" cssClass="add-margin error-msg" />
	</div>

</div>
<div class="form-group">
	<label class="col-sm-3 control-label text-right" id="persons">Remarks:</label>
	<div class="col-sm-3 add-margin">
		<form:textarea class="form-control" path="remarks" id="remarks"
			name="remarks" maxlength="256" />
		<form:errors path="remarks" cssClass="add-margin error-msg" />
	</div>
</div>
<div id="legalCaseUploadDocuments"></div>
<input type="hidden" id="mode"  name="mode" value="${mode}" />
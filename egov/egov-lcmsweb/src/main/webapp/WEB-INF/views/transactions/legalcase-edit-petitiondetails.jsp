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

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
<div class="panel-heading">
	<div class="panel-title">
		<spring:message code="lbl.bipartisanDetails.details" />
	</div>
</div>
<table class="table table-striped table-bordered" id="petitionDetails">
	<thead>
		<tr>
			<!-- <th class="text-center">SI NO</th> -->
			<%-- <th class="text-center"><spring:message code="lbl.IsGovtDept" /></th> --%>
			<th class="text-center"><spring:message code="lbl.name" /><span
				class="mandatory"></span></th>
			<th class="text-center"><spring:message code="lbl.discription" /></th>
			<th class="text-center"><spring:message code="lbl.contactnumber" /></th>
			<%-- 	<th class="text-center"><spring:message code="lbl.Govt_Dept" /></th> --%>
			<th class="text-center">Delete Petitioner <%-- <spring:message
					code="lbl.add/delete_pet" /> --%>
			</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="bipartisanPetitionerDetailsList"
			items="${legalCase.getPetitioners()}" varStatus="status">
			<tr class="">
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
					value="${bipartisanPetitionerDetailsList.address}" maxlength="256" /></td>
				<td class="text-right"><input type="text"
					class="form-control table-input text-left patternvalidation"
					name="bipartisanPetitionerDetailsList[${status.index}].contactNumber"
					id="bipartisanPetitionerDetailsList[${status.index}].contactNumber"
					data-pattern="number"
					value="${bipartisanPetitionerDetailsList.contactNumber}"
					maxlength="10" onkeyup="decimalvalue(this);" /></td>
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
			<!-- 	<th class="text-center">SI NO</th> -->
			<%-- <th class="text-center"><spring:message code="lbl.IsGovtDept" /></th> --%>
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

		<c:forEach var="bipartisanRespondentDetailsList"
			items="${legalCase.getRespondents()}" varStatus="status">
			<tr>
				<%-- 	<td>
								<input type="text" id="table_name${status.index}" class="form-control"
								readonly="readonly" style="text-align: center"
								value="${status.index+1}" /> 
								</td> --%>
				<%-- <td class="text-center"><input type="checkbox" id="bipartisanRespondentDetailsList[${status.index}].isRespondentGovernment"
				name="bipartisanRespondentDetailsList[${status.index}].isRespondentGovernment"
			value="${bipartisanRespondentDetailsList.isRespondentGovernment}"
				onblur="onChangeofPetitioncheck(this)" /></td> --%>

				<td class="text-right"><input type="text"
					class="form-control table-input text-left"
					id="bipartisanRespondentDetailsList[${status.index}].name"
					name="bipartisanRespondentDetailsList[${status.index}].name"
					value="${bipartisanRespondentDetailsList.name}" /></td>


				<td class="text-right"><input type="text"
					class="form-control table-input text-left"
					id="bipartisanRespondentDetailsList[${status.index}].address"
					name="bipartisanRespondentDetailsList[${status.index}].address"
					value="${bipartisanRespondentDetailsList.address}" /></td>

				<td class="text-right"><input type="text"
					id="bipartisanRespondentDetailsList[${status.index}].contactNumber"
					name="bipartisanRespondentDetailsList[${status.index}].contactNumber"
					class="form-control table-input text-left patternvalidation"
					onkeyup="decimalvalue(this);"
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
		<spring:message code="lbl.standingcounselname" />:
	</label>
	<div class="col-sm-3 add-margin" id="personsdiv">
		<form:input class="form-control patternvalidation"
			data-pattern="string" maxlength="50" id="oppPartyAdvocate"
			path="oppPartyAdvocate" />
		<form:errors path="oppPartyAdvocate" cssClass="add-margin error-msg" />
	</div>

</div>
<div class="form-group">
	<label class="col-sm-3 control-label text-right" id="persons"><spring:message
			code="lbl.remarks" />:</label>
	<div class="col-sm-3 add-margin">
		<form:textarea class="form-control" path="remarks" id="remarks"
			name="remarks" maxlength="256" />
		<form:errors path="remarks" cssClass="add-margin error-msg" />
	</div>
</div>

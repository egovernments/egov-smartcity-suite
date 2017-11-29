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

<div class="row">
	<div class="col-md-12">

		<div class="form-group">
			<label class="col-sm-3 control-label text-right">Standing
				Counsel:<span class="mandatory"></span>:
			</label>
			<div class="col-sm-3 add-margin">
				<form:input id="standingCouncilName" type="text"
					class="form-control " autocomplete="off" path="advocateMaster.name"
					name="advocateMaster.name" value="${advocateMaster.name}"
					placeholder="" required="required" />
				<input type="hidden" id="advocateId" value="" />
				<c:forEach items="${departments}" var="advocate">
					<a onclick="setAdvocateId(<c:out value="${advocate.id}"/>)"
						href="javascript:void(0)"
						class="btn btn-secondary btn-xs tag-element freq-ct"><c:out
							value="${advocate.name }" /> </a>
				</c:forEach>
			</div>
			<label class="col-sm-2 control-label text-right">Assigned
				Date:<span class="mandatory"></span>:
			</label>
			<div class="col-sm-3 add-margin">
				<form:input path="assignedToDate" class="form-control datepicker"
					title="Please enter a valid date" pattern="\d{1,2}/\d{1,2}/\d{4}"
					data-date-end-date="-1d" id="assignedDate"
					data-inputmask="'mask': 'd/m/y'" required="required" />
				<form:errors path="assignedToDate" cssClass="add-margin error-msg" />
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label text-right">Date on
				which Vakalaat filed:</label>
			<div class="col-sm-3 add-margin">
				<form:input path="vakalatDate" class="form-control datepicker"
					title="Please enter a valid date" pattern="\d{1,2}/\d{1,2}/\d{4}"
					data-date-end-date="-1d" id="vakalatdate"
					data-inputmask="'mask': 'd/m/y'" />
				<form:errors path="vakalatDate" cssClass="add-margin error-msg" />
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label text-right" id="persons">Is
				Senior Standing Counsel Required:</label>
			<div class="col-sm-3 add-margin">
				<form:checkbox id="isSeniorAdvocate" name="isSeniorAdvocate"
					value="${isSeniorAdvocate}" path="isSeniorAdvocate"
					onclick="enableSeniorDetails();" />
				<form:errors path="isSeniorAdvocate" />
			</div>
		</div>
		<div id="seniordov1" class="form-group">
			<label class="col-sm-3 control-label text-right">Senior
				Standing Counsel<span class="mandatory"></span>:
			</label>
			<div class="col-sm-3 add-margin">
				<form:input id="seniorAdvocateName" type="text"
					class="form-control " autocomplete="off" path="seniorAdvocate.name"
					name="seniorAdvocate.name" required="required"
					value="${seniorAdvocate.name}" placeholder="" />
				<input type="hidden" id="senioradvocateId" value="" />
				<c:forEach items="${departments}" var="advocate">

				</c:forEach>
			</div>
			<div class="form-group" id="seniordov3">
				<label class="col-sm-2 control-label text-right">Assigned
					On:</label>
				<div class="col-sm-3 add-margin">
					<form:input path="assignedToDateForSenior"
						class="form-control datepicker" title="Please enter a valid date"
						pattern="\d{1,2}/\d{1,2}/\d{4}" data-date-end-date="-1d"
						id="assignedToDateForSenior" data-inputmask="'mask': 'd/m/y'" />
					<form:errors path="assignedToDateForSenior"
						cssClass="add-margin error-msg" />
				</div>
			</div>
		</div>
		<div id="seniordov2" class="form-group">
			<label class="col-sm-3 control-label text-right">Order Date<span
				class="mandatory"></span>:
			</label>
			<div class="col-sm-3 add-margin">
				<form:input path="orderDate" class="form-control datepicker"
					title="Please enter a valid date" pattern="\d{1,2}/\d{1,2}/\d{4}"
					data-date-end-date="-1d" id="orderDate" required="required"
					data-inputmask="'mask': 'd/m/y'" />
				<form:errors path="orderDate" cssClass="add-margin error-msg" />
			</div>
			<label class="col-sm-2 control-label text-right">Order Number<span
				class="mandatory"></span>:
			</label>
			<div class="col-sm-3 add-margin">
				<form:input class="form-control patternvalidation"
					data-pattern="number" maxlength="50" id="orderNumber"
					required="required" path="orderNumber" />
				<form:errors path="orderNumber" cssClass="add-margin error-msg" />
			</div>
		</div>



	</div>
</div>





<script>
function enableSeniorDetails()
{
	  var elm = document.getElementById('isSeniorAdvocate');
if(elm.checked)
{
		    $("#seniordov1").show(); 
		    $("#seniordov2").show(); 
		    $("#seniordov3").show(); 
   
}
else
{
	$("#seniorAdvocateName").val('');
	$("#assignedtodateForsenior").val('');
	$("#orderDate").val('');
	$("#orderNumber").val('');
	 $("#seniordov1").hide(); 
	    $("#seniordov2").hide(); 
	    $("#seniordov3").hide(); 
	    
}
  
}
</script>

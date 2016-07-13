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
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<div class="row">
	<div class="col-md-12">
		<form:form method="post" action="" modelAttribute="legalcase"
			id="standCouncilForm" class="form-horizontal form-groups-bordered"
			enctype="multipart/form-data">
		<!-- 	<div class="panel-heading">
					<div class="panel-title">
						Assign Standing Counsel
					</div>
				</div> -->

<div class="form-group">
	<label class="col-sm-3 control-label text-right">Standing
		Council:<span class="mandatory"></span>:
	</label>
	<div class="col-sm-3 add-margin">
		<form:input id="standingCouncilName" type="text" class="form-control "
			autocomplete="off"
			path="eglcLegalcaseAdvocates[0].advocateMaster.name"
			name="eglcLegalcaseAdvocates[0].advocateMaster.name"
			value="${eglcLegalcaseAdvocates[0].advocateMaster.name}"
			placeholder="" required="required" />
		<input type="hidden" id="advocateId" value="" />
		<c:forEach items="${departments}" var="advocate">
			<a onclick="setAdvocateId(<c:out value="${advocate.id}"/>)"
				href="javascript:void(0)"
				class="btn btn-secondary btn-xs tag-element freq-ct"><c:out
					value="${advocate.name }" /> </a>
		</c:forEach>
	</div>
	<label class="col-sm-2 control-label text-right">Assigned Date:<span
		class="mandatory"></span>:
	</label>
	<div class="col-sm-3 add-margin">
		<form:input path="eglcLegalcaseAdvocates[0].assignedtodate"
			class="form-control datepicker" title="Please enter a valid date"
			pattern="\d{1,2}/\d{1,2}/\d{4}" data-date-end-date="-1d"
			id="assignedDate" data-inputmask="'mask': 'd/m/y'"  required="required"/>
		<form:errors path="eglcLegalcaseAdvocates[0].assignedtodate"
			cssClass="add-margin error-msg" />
	</div>
</div>
<div class="form-group">
	<label class="col-sm-3 control-label text-right">Date on which
		Vakalaat filed:</label>
	<div class="col-sm-3 add-margin">
		<form:input path="eglcLegalcaseAdvocates[0].vakalatdate"
			class="form-control datepicker" title="Please enter a valid date"
			pattern="\d{1,2}/\d{1,2}/\d{4}" data-date-end-date="-1d"
			id="vakalatdate" data-inputmask="'mask': 'd/m/y'" />
		<form:errors path="eglcLegalcaseAdvocates[0].vakalatdate"
			cssClass="add-margin error-msg" />
	</div>
	<%-- <label class="col-sm-2 control-label text-right">CA Filing
		Date<span class="mandatory"></span>:</label>
	<div class="col-sm-3 add-margin">
		<form:input path="eglcPwrs[0].caFilingdate"
			class="form-control datepicker" title="Please enter a valid date"
			pattern="\d{1,2}/\d{1,2}/\d{4}" data-date-end-date="-1d"
			id="cafillingDate" data-inputmask="'mask': 'd/m/y'" required="required"/>
		<form:errors path="eglcPwrs[0].caFilingdate"
			cssClass="add-margin error-msg" />
	</div> --%>
</div>
<div class="form-group">
	<label class="col-sm-3 control-label text-right" id="persons">Is
		Senior Standing Counsel Required:</label>
	<div class="col-sm-3 add-margin">
		<form:checkbox id="isSeniorAdvocate"
			path="isSenioradvrequired" onclick="enableSeniorDetails();" />
		<form:errors path="isSenioradvrequired" />
	</div>
</div>
<div id="seniordov1" class="form-group">
	<label class="col-sm-3 control-label text-right">Senior
		Standing Council<span class="mandatory"></span>:
	</label>
	<div class="col-sm-3 add-margin">
		<form:input id="seniorAdvocateName" type="text" class="form-control "
			autocomplete="off"
			path="eglcLegalcaseAdvocates[0].eglcSeniorAdvocateMaster.name"
			name="eglcLegalcaseAdvocates[0].eglcSeniorAdvocateMaster.name"
			value="${eglcLegalcaseAdvocates[0].eglcSeniorAdvocateMaster.name}"
			placeholder="" />
		<input type="hidden" id="senioradvocateId" value="" />
		<c:forEach items="${departments}" var="advocate">
			
		</c:forEach>
	</div>
	<div class="form-group" id="seniordov3">
	<label class="col-sm-2 control-label text-right">Assigned On:</label>
	<div class="col-sm-3 add-margin">
		<form:input path="eglcLegalcaseAdvocates[0].assignedtodateForsenior"
			class="form-control datepicker" title="Please enter a valid date"
			pattern="\d{1,2}/\d{1,2}/\d{4}" data-date-end-date="-1d"
			id="assignedtodateForsenior" data-inputmask="'mask': 'd/m/y'" />
		<form:errors path="eglcLegalcaseAdvocates[0].assignedtodateForsenior"
			cssClass="add-margin error-msg" />
	</div>
</div>
</div>
<div id="seniordov2" class="form-group">
	<label class="col-sm-3 control-label text-right">Order Date<span
		class="mandatory"></span>:
	</label>
	<div class="col-sm-3 add-margin">
		<form:input path="eglcLegalcaseAdvocates[0].orderdate"
			class="form-control datepicker" title="Please enter a valid date"
			pattern="\d{1,2}/\d{1,2}/\d{4}" data-date-end-date="-1d"
			id="orderDate" data-inputmask="'mask': 'd/m/y'" />
		<form:errors path="eglcLegalcaseAdvocates[0].orderdate"
			cssClass="add-margin error-msg" />
	</div>
	<label class="col-sm-2 control-label text-right">Order Number<span
		class="mandatory"></span>:
	</label>
	<div class="col-sm-3 add-margin">
		<form:input class="form-control patternvalidation"
			data-pattern="string" maxlength="50" id="orderNumber"
			path="eglcLegalcaseAdvocates[0].ordernumber" />
		<form:errors path="eglcLegalcaseAdvocates[0].ordernumber"
			cssClass="add-margin error-msg" />
	</div>
</div>


<%-- 
<div class="form-group" >
<label class="col-sm-3 control-label text-right"><font size="2"><spring:message code="lbl.mesg.document"/></font>	</label>
	<div class="col-sm-3 add-margin">
	
				<input type="file" id="file" name="legalcaseDocuments[0].files" 
				class="file-ellipsis upload-file">
			
		<form:errors path="legalcaseDocuments[0].files" cssClass="add-margin error-msg" />
		<div class="add-margin error-msg text-left" ><font size="2">
								<spring:message code="lbl.mesg.document"/>	
								</font></div></div>
	</div>  --%>
	<div class="buttonbottom" align="center">
				<div class="form-group text-center">
					<button type="submit" class="btn btn-primary" value="Save"
						id="buttonid">
						<spring:message code="lbl.save.button" />
					</button>
					<a onclick="self.close()" class="btn btn-default"
						href="javascript:void(0)"><spring:message
							code="lbl.close.button" /></a>
				</div>
			</div>
		</form:form>
	</div>
</div>
<link rel="stylesheet"
	href="<c:url value='/resources/global/css/bootstrap/typeahead.css' context='/egi'/>">
<link rel="stylesheet"
	href="<c:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>" />
<script
	src="<c:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"></script>
<script
	src="<c:url value='/resources/global/js/bootstrap/typeahead.bundle.js' context='/egi'/>"></script>
<script
	src="<c:url value='/resources/global/js/egov/inbox.js' context='/egi'/>"></script>
	<script
	src="<c:url value='/resources/js/app/legalcase-ajax.js?rnd=${app_release_no}'/>"></script>
<script
	src="<c:url value='/resources/js/app/legalcasenew.js?rnd=${app_release_no}'/>"></script>

	
<script>
function enableSeniorDetails()
{
	if($('#isSeniorAdvocate').val() == 'true')
{
		    $("#seniordov1").show(); 
		    $("#seniordov2").show(); 
		    $("#seniordov3").show(); 
   
}
else
{
	 $("#seniordov1").hide(); 
	    $("#seniordov2").hide(); 
	    $("#seniordov3").hide(); 
	    dom.get('seniorAdvocateName').value="";
		  dom.get('assignedtodateForsenior').value="";
	      dom.get('orderDate').value="";
	      dom.get("orderNumber").value="";
}
  
}
</script>

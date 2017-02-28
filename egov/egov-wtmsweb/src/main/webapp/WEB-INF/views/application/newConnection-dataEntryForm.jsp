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
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<div class="row">
	<div class="col-md-12"> 
		<div class="text-right error-msg" style="font-size:14px;"><spring:message code="lbl.application.date"/> : <fmt:formatDate pattern="dd/MM/yyyy" value="${waterConnectionDetails.applicationDate}" /></div>
		<form:form role="form" action="/wtms/application/newConnection-dataEntryForm" 
			modelAttribute="waterConnectionDetails" id="newWaterConnectionform"
			cssClass="form-horizontal form-groups-bordered"
			enctype="multipart/form-data">
			<form:hidden path="applicationType" id="applicationType" value="${waterConnectionDetails.applicationType.id}"/>
			<form:hidden path="id" id="id" value="${waterConnectionDetails.id}"/>
			<form:hidden path="legacy" id="legacy" value="true"/>
			<form:hidden path="connectionStatus" id="connectionStatus" value="${waterConnectionDetails.connectionStatus}"/> 
			
			<input type="hidden" name="allowIfPTDueExists" id="allowIfPTDueExists" value="true"> 
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">
						<spring:message code="lbl.applicant.details" />
					</div>
				</div>
				<div class="panel-body custom-form ">
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message code="lbl.appType" /></label>
						<div class="col-sm-6 add-margin dynamic-span capitalize">
							<form:radiobuttons path="applicationType" items="${radioButtonMap}"  element="span"  onchange="resetPropertyDetailsafterCheckBox();"/> 
						</div>
					</div>
					<jsp:include page="applicantdetails.jsp"></jsp:include>
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.consumerno" /><span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin">
							<form:input path="connection.oldConsumerNumber" id="consumerCodeData"
								class="form-control text-left patternvalidation" data-pattern="number" maxlength="15" required="required" />
								<form:errors path="connection.oldConsumerNumber" cssClass="add-margin error-msg" />	
						</div>
						<label class="col-sm-2 control-label text-right"><spring:message
								code="lbl.connectiondate" /><span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin">
							<form:input  path="executionDate"  
								class="form-control datepicker" title="Please enter a valid date" pattern="\d{1,2}/\d{1,2}/\d{4}" data-date-end-date="-1d"
								id="executionDate" data-inputmask="'mask': 'd/m/y'" required="required" />
								<form:errors path="executionDate" cssClass="add-margin error-msg" />
						</div>
					</div>
					<jsp:include page="connectiondetails.jsp"></jsp:include>	
					<jsp:include page="dataEntryDetails.jsp"></jsp:include>
					
				</div>
			</div>	
			<div class="buttonbottom" align="center">
				<table>
					<tr>
						<td><form:button type="submit" id="Create"
								class="btn btn-primary" value="Create" onclick="validate();"><spring:message code="lbl.Submit.button"/></form:button>
							<input type="button" name="button2" id="button2" value="Close"
							class="btn btn-primary" onclick="window.close();" /></td>
					</tr>
				</table>
			</div>
					
		<script>

		 if($('#connectionType').val()=='METERED')
		 {
		   $('#metereddetails').show();
		   $('.showfields').show();
		 }
		
			function validate(){
				
				if($('#connectionType').val() !='METERED')
					{
						$('#monthlyFee').attr('required', 'required');
						$('#existmeterCost').removeAttr('required');
						$('#existmeterName').removeAttr('required');
						$('#existmeterNo').removeAttr('required');
						$('#previousReading').removeAttr('required');
						$('#currentcurrentReading').removeAttr('required');
						$('#existreadingDate').removeAttr('required');
					}
				else {
					$('#monthlyFee').removeAttr('required');
					$('#existmeterCost').attr('required', 'required');
					$('#existmeterName').attr('required', 'required');
					$('#existmeterNo').attr('required', 'required');
					$('#previousReading').attr('required', 'required');
					$('#currentcurrentReading').attr('required', 'required');
					$('#existreadingDate').attr('required', 'required');
					}
				var radioValue = $("input[name='applicationType']:checked").val();
			    var ar=document.getElementsByName('applicationType');
	            ar[0].value=radioValue;
				return true;
		    }

		//default ajax callback function
		function callBackAjax()
		{
			//bootbox.alert('callback function called!');
			
		}
		function resetPropertyDetailsafterCheckBox() {
			$('#propertyIdentifier').val('');
			$('#applicantname').val('');
			$('#mobileNumber').val('');
			$('#email').val('');
			$('#aadhaar').val('');
			$('#nooffloors').val('');
			$('#propertyaddress').val('');
			$('#locality').val('');
			$('#zonewardblock').val('');
			$('#propertytax').val('0.00');
		}
		function getEmptyValues()
		{
			if($('#connectionType').val() !='METERED')
			 {
			   $('#existmeterCost').val('');
				$('#existmeterName').val('');
				$('#existmeterNo').val('');
				$('#previousReading').val('');
				$('#currentcurrentReading').val('');
				$('#existreadingDate').val('');
			 }
			}
	
		</script>
		</form:form>
	</div>
</div>

<script src="<cdn:url value='/resources/js/app/newconnection.js?rnd=${app_release_no}'/>"></script>
<script src="<cdn:url value='/resources/js/app/newconnection-dataentry.js?rnd=${app_release_no}'/>"></script>
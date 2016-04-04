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
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<html>
	<head>
		<title><s:text name="page.title.cancellicense" /></title>
  
		
		<script type="text/javascript">
	function validateDataBeforeSubmit() {
		var canclReasonObj = document.getElementById("reasonForCancellation");
		var canclReasonIndex = canclReasonObj.selectedIndex;
		var canclReasonValue = canclReasonObj.options[canclReasonIndex].value;
		if (canclReasonValue == " " || canclReasonValue == "-1"
				|| canclReasonValue == "0") {
			showMessage('cancelLicense_error', '<s:text name="tradelicense.LicenseCancelInfo.reasonForCancellation.none" />');
			return false;
		} else if (document.getElementById("refernceno").value == '') {
			showMessage('cancelLicense_error', '<s:text name="tradelicense.LicenseCancelInfo.refernceNo.none" />');
			return false;
		} else if (document.getElementById("cancelInforemarks").value == '') {
			showMessage('cancelLicense_error', '<s:text name="tradelicense.LicenseCancelInfo.cancelInforemarks.none" />');
			return false;
		} else {
	    	clearMessage('cancelLicense_error');
	    	document.cancelLicenseForm.action='${pageContext.request.contextPath}/cancellation/cancelLicense-confirmCancellation.action';
	    	document.cancelLicenseForm.submit();
		}
	} 
 
</script>
	</head>
	<body>
	<div id="cancelLicense_error" class="error-msg" style="display:none;"></div> 
	<div class="row">
		<div class="col-md-12">
			<div class="panel-body">
			<s:if test="%{hasErrors()}">
				<div align="left">
					<s:actionerror />
					<s:fielderror/>
				</div>			
			</s:if>
			<s:if test="%{hasActionMessages()}">
			<div class="messagestyle">
				<s:actionmessage theme="simple" />
			</div>
			</s:if>

			<s:form action="cancelLicense" theme="css_xhtml" name="cancelLicenseForm" id="cancelLicenseForm" cssClass="form-horizontal form-groups-bordered">
				<s:token />
				<s:push value="model">
					<div class="panel panel-primary" data-collapsed="0">
						<div class="panel-heading">
							<div class="panel-title text-left">
									<s:text name='page.title.cancellicense' />
							</div>
						</div>
						<div class="panel-body custom-form">
							<s:hidden name="licenseId" />
						
							<div class="form-group">
								<label for="field-1" class="col-sm-3 control-label text-right"><s:text
										name="license.LicenseCancelInfo.reasonForCancellation" /><span class="mandatory"></span></label>
								  <div class="col-sm-3 add-margin">
								       <s:select name="reasonForCancellation" id="reasonForCancellation" list="reasonMap" disabled="%{sDisabled}"
											headerKey="-1" headerValue="%{getText('license.default.select')}" class="form-control" />
								   </div>
								
								<label for="field-1" class="col-sm-2 control-label text-right"><s:text
										name="license.license.refernceno" /><span class="mandatory"></span></label>
								<div class="col-sm-3 add-margin">
									<s:textfield id="refernceno" name="refernceno" value="%{refNo}" disabled="%{sDisabled}" size="20" maxLength="64" tabindex="2" class="form-control"/>
								</div>
							</div>
							
							<div class="form-group">
								<label for="field-1" class="col-sm-3 control-label text-right"><s:text
										name="license.license.referencedate" /></label>
								  <div class="col-sm-3 add-margin">
								       <s:date name="refDate" id="commDateId2" format="dd/MM/yyyy" />
										<s:textfield disabled="%{sDisabled}" name="commdateApp" id="commdateApp" class="form-control datepicker" data-date-end-date="0d" maxlength="10" size="10" value="%{commDateId2}" />
								   </div>
								
								<label for="field-1" class="col-sm-2 control-label text-right"><s:text
										name="license.license.Remarks" /><span class="mandatory"></span></label>
								<div class="col-sm-3 add-margin">
									<s:textarea tabindex="4" name="cancelInforemarks" rows="3" cols="33" id="cancelInforemarks" disabled="%{sDisabled}" value="%{cancelInforemarks}" maxlength="1024" class="form-control"/>
								</div>
							</div>   
						</div>
					</div>
				</s:push>
				
				<div class="row">
					<div class="text-center">
						<button type="button" id="btnsubmit" class="btn btn-primary" onclick="return validateDataBeforeSubmit();">
						Submit</button>
						<%-- <s:submit name="button32" type="submit" cssClass="buttonsubmit" id="button32" method="confirmCancellation" value="Submit" /> --%>
						<button type="button" id="btnclose" class="btn btn-default" onclick="window.close();">
						Close</button>
					</div>
				</div>
				
			</s:form></div></div></div>
			<script>
			jQuery(".datepicker").datepicker({
				format: "dd/mm/yyyy",
				autoclose: true 
			}); 
			</script>
	</body>
</html>

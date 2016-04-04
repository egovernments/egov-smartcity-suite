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
<%@ include file="/includes/taglibs.jsp"%>
<html>
<head>
<title><s:text name="page.title.transfertrade" /></title>
<sx:head />
<script>
		function validateDataBeforeSubmit() {
			if (document.getElementById("applicantName").value == '' || document.getElementById("applicantName").value == null){
				showMessage('transferLicense_error', '<s:text name="licensetransfer.applicantname.null" />');
				document.getElementById("applicantName").focus();
				return false;
			}  else if (document.getElementById("licenseeAddress").value == '' || document.getElementById("licenseeAddress").value == null){
				showMessage('transferLicense_error', '<s:text name="licensetransfer.licenseeaddress.null" />');
				document.getElementById("licenseeAddress").focus();
				return false;
			} else if (document.getElementById("commdateApp").value == '') {
				showMessage('transferLicense_error', '<s:text name="licensetransfer.date.none" />');
				return false;
			} else {
		    	clearMessage('transferLicense_error');
		    	document.transferTradeLicense.action='${pageContext.request.contextPath}/transfer/transferTradeLicense-create.action';
		    	document.transferTradeLicense.submit();
			}
		} 
		
		function onSubmit() {
			return validateDataBeforeSubmit(this);
		}
</script>
</head>

	<body>
	<div id="transferLicense_error" class="error-msg" style="display:none;"></div> 
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

			<s:form action="transferTradeLicense" theme="css_xhtml" name="transferTradeLicense" id="transferTradeLicense" cssClass="form-horizontal form-groups-bordered">
				<s:token />
				<s:push value="model">
					<s:hidden name="id"/>
					<s:hidden name="licenseId" />
					<div class="panel panel-primary" data-collapsed="0">
						<div class="panel-heading">
							<div class="panel-title text-left">
									<s:text name='page.title.transfertrade' />
							</div>
						</div>
						<div class="panel-body custom-form">
							<div class="form-group">
								<label for="field-1" class="col-sm-3 control-label text-right"><s:text
										name="licenseTransfer.licensenumber" /></label>
								  <div class="col-sm-3 add-margin"><s:property value="licenseNumber" />			       
								   </div>
								
								<label for="field-1" class="col-sm-2 control-label text-right"><s:text
										name="licenseTransfer.establishmentname" /></label>
								<div class="col-sm-3 add-margin">
									<s:property value="nameOfEstablishment" />	
								</div>
							</div>
							
							<div class="form-group">
								<label for="field-1" class="col-sm-3 control-label text-right"><s:text
										name="licenseTransfer.tradename" /></label>
								  <div class="col-sm-3 add-margin"><s:property value="tradeName.name" />			       
								   </div>
								
								<label for="field-1" class="col-sm-2 control-label text-right"><s:text
										name="licenseTransfer.licenseeName" /></label>
								<div class="col-sm-3 add-margin">
									<s:property value="licensee.applicantName" />	
								</div>
							</div>
							
							<div class="form-group">
								<label for="field-1" class="col-sm-3 control-label text-right"><s:text
										name="licenseTransfer.dateofapplication" /><span class="mandatory"></span></label>
								  <div class="col-sm-3 add-margin">
								       <s:date name="applicationDate" id="commDateId2" format="dd/MM/yyyy" />
										<s:textfield disabled="%{sDisabled}" name="commdateApp" id="commdateApp" class="form-control datepicker" data-date-end-date="0d" maxlength="10" size="10" value="%{commDateId2}" />
								   </div>
								
								<label for="field-1" class="col-sm-2 control-label text-right"><s:text
										name="licenseTransfer.applicantname" /><span class="mandatory"></span></label>
								<div class="col-sm-3 add-margin">
									<s:textfield name="licensee.applicantName" id="applicantName" maxlength="32" cssClass="form-control patternvalidation" data-pattern="alphabetwithspace"/>
								</div>
							</div>   
							
							<div class="form-group">
								<label for="field-1" class="col-sm-3 control-label text-right"><s:text
										name="licenseTransfer.isaddressdifferent" /><span class="mandatory"></span></label>
								<div class="col-sm-3 add-margin">
									<s:checkbox id="active"	name="active" value="%{active}"/>
								</div>
								
								<label for="field-1" class="col-sm-2 control-label text-right"><s:text
										name="licenseTransfer.address" /><span class="mandatory"></span></label>
								<div class="col-sm-3 add-margin">
									<s:textarea name="licensee.address"  id="licenseeAddress" maxlength="250" cssClass="form-control"/>
								</div>
							</div>   
						</div>
					</div>
				</s:push>
				
				<div class="row">
					<div class="text-center">
						<button type="button" id="btnsubmit" class="btn btn-primary" onclick="return validateDataBeforeSubmit();">
						Submit</button>
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
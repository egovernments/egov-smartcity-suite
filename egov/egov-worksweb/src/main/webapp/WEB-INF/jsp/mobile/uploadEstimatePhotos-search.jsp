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

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ include file="/includes/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript" src="/egi/script/jsCommonMethods.js"></script>
<script type="text/javascript" src="/egi/javascript/dateValidation.js"></script>
<script type="text/javascript" src="/egi/commonjs/calender.js"></script>
<style type="text/css">
      html { height: 100% }
      body { height: 100%; margin: 0; padding: 0 }
</style>

<style>

.ui-grid-a {
	margin-top: 1em;
	padding-top: .8em;
	margin-top: 1.4em;
	border-top: 1px solid rgba(0, 0, 0, .1);
}

@media all and (max-width: 480px) {
	#footers {
		display: none;
	}
	.nav-footer .ui-btn .ui-btn-inner {
		padding-top: 40px !important;
	}
	.nav-footer .ui-btn .ui-icon {
		width: 30px !important;
		height: 30px !important;
		margin-left: -15px !important;
		box-shadow: none !important;
		-moz-box-shadow: none !important;
		-webkit-box-shadow: none !important;
		-webkit-border-radius: 0 !important;
		border-radius: 0 !important;
	}
}
</style>

<title>Search Estimate</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<link rel="stylesheet" href="../resources/css/jquerymobile/jquery.mobile-1.3.1.min.css" />
<link rel="stylesheet" href="../resources/css/jquery/jquery.toastmessage.css" />
<script src="../resources/js/jquery-1.7.2.min.js"></script>
<script src="../resources/js/jquerymobile/jquery.mobile-1.3.1.min.js"></script>
<script type="text/javascript" src="../resources/js/jquery/jquery.toastmessage.js"></script>

<link rel="stylesheet" href="../resources/css/jquerymobile/jqm-datebox.min.css" />
<script src="../resources/js/jquerymobile/jqm-datebox.core.min.js"></script>
<script src="../resources/js/jquerymobile/jqm-datebox.mode.datebox.min.js"></script>

<script type="text/javascript">
$(document).ready(function() {
	$.mobile.defaultPageTransition = 'none';
	$.mobile.defaultDialogTransition = 'none';
	$.mobile.useFastClick = false;
});

function init(){}

function validateForm(){
		if (!window.localStorage){
      		 return;
  			 }
	return true;
}
	   
function resetAll()
{
	document.getElementById("estimateNumber").value="";
	document.getElementById("fromDate").value="";
	document.getElementById("toDate").value="";
	//Resetting and refreshing the drop downs
	var myselect = $("select#execDeptId");
	myselect[0].selectedIndex =0;
	myselect.selectmenu("refresh");

	var myselect1 = $("select#type");
	myselect1[0].selectedIndex =0;
	myselect1.selectmenu("refresh");
}
function setDates()
{
	var fromDate = '<s:property value="%{fromDate}" />';
	var toDate = '<s:property value="%{toDate}" />';
	if(fromDate!='')
		document.getElementById("fromDate").value=fromDate;
	if(toDate!='')
		document.getElementById("toDate").value=toDate;
}
function validateSearch()
{	
	document.getElementById("errors").style.display='none';
	var todate = document.getElementById("toDate").value;
	var fromdate = document.getElementById("fromDate").value;
	var estimateNumber = document.getElementById("estimateNumber").value;
	var natureOfWork = document.getElementById("natureOfWork").value;
	var execDept = document.getElementById("execDeptId").value;
	if(estimateNumber == '' && natureOfWork == -1 && todate=='' && fromdate=='' && execDept == -1)
	{
		document.getElementById("errors").style.display='block';
	    document.getElementById("errors").innerHTML='<s:text name="mobile.provide.one.criteria" />';
		return false;
	}	
	if(fromdate !='' && todate!='' && compareDate(fromdate,todate) == -1){
		document.getElementById("errors").style.display='block';
	    document.getElementById("errors").innerHTML='<s:text name="mobile.provide.formDate.lessThan.toDate" />';
		return false;
	}
}
</script>
</head>
<body onload="init();setDates();">

	<s:form action="uploadEstimatePhotos!searchList.action" method="post"
		enctype="multipart/form-data" theme="simple" name="tablet" id="tablet"
		data-ajax="false" onsubmit="return validateSearch();" >
		<s:token />
		<s:push value="model">	
			<s:if test="%{hasErrors()}">
				<div class="errorstyle">
					<s:actionerror />
					<s:fielderror />
				</div>
			</s:if>
			<s:if test="%{hasActionMessages()}">
				<div class="errorstyle">
					<s:actionmessage />
				</div>
			</s:if>
			<!-- Needed -->
			<div data-role="page" id="searchEstimate" class="pageclass">
				<s:if test="%{hasErrors()}">
					<div class="required" style="color: red;font-size: 12px">
						<s:actionerror />
						<s:fielderror />
					</div>
				</s:if>
				<s:if test="%{hasActionMessages()}">
					<div class="errorstyle">
						<s:actionmessage />
					</div>
				</s:if>
				<div data-mini="true" data-theme="b" data-role="header" data-position="fixed">
					<h5>Search Estimate</h5>
				</div>
				<div id="errors" style="display: none;font-size: 10px;font-weight: bold;color: #F00;text-align: left;margin-bottom: 10px;padding: 10px;background-color: #FFFFEE;font-family: Arial, Helvetica, sans-serif;border: 1px solid #EEEE00;"></div>
				<div data-mini="true" data-role="content" id="contentMain" style="padding: 5px">
					<div data-role="fieldcontain" id="estNoDiv">
						<label data-mini="true" id="whatLabel" for="estimateNumber">Estimate Number:</label>
						<s:textfield data-mini="true" id="estimateNumber" name="estimateNumber" />
					</div>
					<div data-mini="true" data-role="fieldcontain" id="natureOfWorkDiv">
						<fieldset data-mini="true" data-role="controlgroup">
							<legend >Nature Of Work:</legend>
							<s:select data-mini="true" id="natureOfWork"
								name="natureOfWork.id"  headerKey="-1" headerValue="%{getText('estimate.default.select')}"
								list="dropdownData.typeList" listKey="id" listValue="name" value="%{natureOfWork.id}" >
							</s:select>
						</fieldset>
					</div>
					<div data-role="fieldcontain" id="estFromDtDiv">
						<label data-mini="true" id="fromDateLbl" for="fromDate">From Date:</label>
						<input data-mini="true" id="fromDate" name="fromDate" type="text" data-role="datebox" data-theme="b"  maxlength="10" onblur="validateDateFormat(this)"  onkeyup="DateFormat(this,this.value,event,false,'3')" data-options='{"mode":"datebox","overrideDateFieldOrder":["d","m","y"], "useNewStyle":true, "themeButton":"b","themeInput":"b","themeHeader":"b","useHeader":"false","overrideDateFormat": "%d/%m/%Y"}' />
					</div>
					<div data-role="fieldcontain" id="estToDtDiv">
						<label data-mini="true" id="toDateLbl" for="toDate">To Date:</label>
						<input data-mini="true" id="toDate" name="toDate" type="text" data-role="datebox" data-theme="b" maxlength="10" onblur="validateDateFormat(this)"  onkeyup="DateFormat(this,this.value,event,false,'3')" data-options='{"mode":"datebox", "overrideDateFieldOrder":["d","m","y"], "useNewStyle":true,"themeButton":"b","themeInput":"b","themeHeader":"b","useHeader":"false","overrideDateFormat": "%d/%m/%Y"}' />
					</div>
					<div data-mini="true" data-role="fieldcontain" id="execDeptDiv">
						<fieldset data-mini="true" data-role="controlgroup">
							<legend >Executing Dept:</legend>
							<s:select data-mini="true" id="execDeptId"
								name="execDeptId"  headerKey="-1" headerValue="%{getText('estimate.default.select')}"
								list="dropdownData.execDeptList" listKey="id" listValue="name" value="%{execDeptId}" >
							</s:select>
						</fieldset>
					</div>
					<fieldset class="ui-grid-a">
						<div class="ui-block-a">
							<button type="submit" data-mini="true" id="submit" data-theme="b">Submit</button>
						</div>
						<div class="ui-block-b">
							<button type="button" data-mini="true" id="cancel" onclick="resetAll()" data-theme="d">Cancel</button>
						</div>
					</fieldset>
				</div>
			</div>
			<!-- Needed -->
		</s:push>
	</s:form>
</body>
</html>

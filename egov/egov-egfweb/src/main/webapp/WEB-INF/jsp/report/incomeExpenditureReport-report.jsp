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


<%@ taglib prefix="s" uri="/WEB-INF/tags/struts-tags.tld"%>
<link href="/EGF/resources/css/budget.css?rnd=${app_release_no}" rel="stylesheet"
	type="text/css" />
<style type="text/css">
@media print {
	#non-printable {
		display: none;
	}
}
</style>

<script>
var callback = {
		success: function(o){
			document.getElementById('result').innerHTML=o.responseText;
			undoLoadingMask();
			},
			failure: function(o) {
				undoLoadingMask();
		    }
		}
function disableAsOnDate(){
	if(document.getElementById('period').value != "Date"){
		document.getElementById('asOndate').disabled = true;
		document.getElementById('financialYear').disabled = false;
	}else{
		document.getElementById('financialYear').disabled = true;
		document.getElementById('asOndate').disabled = false;
	}
}

function validateMandatoryFields(){

	if(document.getElementById('period').value=="Select")
	{
		bootbox.alert('Please select Period');
		return false;
	}
		
	if(document.getElementById('period').value!="Date"){
		if(document.getElementById('financialYear').value==0){
			bootbox.alert('Please select a Financial year');
			return false;
		}
	}
	if(document.getElementById('period').value=="Date" && document.getElementById('asOndate').value==""){
		bootbox.alert('Please enter As On Date');
		return false;
	}
	return true;
}
function getData(){
	if(validateMandatoryFields()){
		doLoadingMask();
		var url = '/EGF/report/incomeExpenditureReport-ajaxPrintIncomeExpenditureReport.action?showDropDown=false&model.period='+document.getElementById('period').value+'&model.currency='+document.getElementById('currency').value+'&model.financialYear.id='+document.getElementById('financialYear').value+'&model.department.id='+document.getElementById('department').value+'&model.function.id='+document.getElementById('function').value+'&model.field.id='+document.getElementById('field').value+'&model.functionary.id='+document.getElementById('functionary').value+'&model.asOndate='+document.getElementById('asOndate').value+'&model.fund.id='+document.getElementById('fund').value;
		YAHOO.util.Connect.asyncRequest('POST', url, callback, null);
		return true;
    }
    
	return false;
}
function showAllMinorSchedules(){
	if(validateMandatoryFields()){
		window.open('/EGF/report/incomeExpenditureReport-generateScheduleReport.action?showDropDown=false&model.period='+document.getElementById('period').value+'&model.currency='+document.getElementById('currency').value+'&model.financialYear.id='+document.getElementById('financialYear').value+'&model.department.id='+document.getElementById('department').value+'&model.function.id='+document.getElementById('function').value+'&model.field.id='+document.getElementById('field').value+'&model.functionary.id='+document.getElementById('functionary').value+'&model.asOndate='+document.getElementById('asOndate').value+'&model.fund.id='+document.getElementById('fund').value,'','resizable=yes,height=650,width=900,scrollbars=yes,left=30,top=30,status=no');
	return true;
    }
	return false;
}
function showAllSchedules(){
	if(validateMandatoryFields()){
		window.open('/EGF/report/incomeExpenditureReport-generateDetailCodeReport.action?showDropDown=false&model.period='+document.getElementById('period').value+'&model.currency='+document.getElementById('currency').value+'&model.financialYear.id='+document.getElementById('financialYear').value+'&model.department.id='+document.getElementById('department').value+'&model.function.id='+document.getElementById('function').value+'&model.field.id='+document.getElementById('field').value+'&model.functionary.id='+document.getElementById('functionary').value+'&model.asOndate='+document.getElementById('asOndate').value+'&model.fund.id='+document.getElementById('fund').value,'','resizable=yes,height=650,width=900,scrollbars=yes,left=30,top=30,status=no');
	return true;
    }
	return false;
}

function showSchedule(majorCode, scheduleNo){
	if(validateMandatoryFields()){
		window.open('/EGF/report/incomeExpenditureReport-generateIncomeExpenditureSubReport.action?showDropDown=false&model.period='+document.getElementById('period').value+'&model.currency='+document.getElementById('currency').value+'&model.financialYear.id='+document.getElementById('financialYear').value+'&model.department.id='+document.getElementById('department').value+'&model.asOndate='+document.getElementById('asOndate').value+'&majorCode='+majorCode+'&scheduleNo='+scheduleNo+'&model.function.id='+document.getElementById('function').value+'&model.field.id='+document.getElementById('field').value+'&model.functionary.id='+document.getElementById('functionary').value+'&endDate='+document.getElementById('asOndate').value+'&asOnDateRange='+document.getElementById('asOndate').value+'&model.fund.id='+document.getElementById('fund').value,'','height=650,width=900,scrollbars=yes,left=30,top=30,status=no');
	return true;
    }
	return false;
}
</script>
<style>
th.bluebgheadtd {
	padding: 0px;
	margin: 0px;
}

.extracontent {
	font-weight: bold;
	font-size: xx-small;
	color: #CC0000;
}
</style>
<div id="non-printable">
	<s:form name="incomeExpenditureReport" action="incomeExpenditureReport"
		theme="simple">
		<div class="formmainbox">
			<div class="formheading"></div>
			<div class="subheadnew">Income Expenditure Report</div>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="10%" class="bluebox">&nbsp;</td>
					<td width="15%" class="bluebox"><s:text name="report.period" />:<span
						class="mandatory1">*</span></td>
					<td width="22%" class="bluebox"><s:select name="period"
							id="period"
							list="#{'Select':'---Choose---','Date':'Date','Yearly':'Yearly','Half Yearly':'Half Yearly'}"
							onclick="disableAsOnDate()" value="%{model.period}" /></td>
					<td class="bluebox" width="12%"><s:text
							name="report.financialYear" />:<span class="mandatory1">*</span></td>
					<td width="41%" class="bluebox"><s:select name="financialYear"
							id="financialYear" list="dropdownData.financialYearList"
							listKey="id" listValue="finYearRange" headerKey="0"
							headerValue="----Select----" value="%{model.financialYear.id}" />
					</td>
				</tr>
				<tr>
					<td class="greybox">&nbsp;</td>
					<td class="greybox"><s:text name="report.asOnDate" />:</td>
					<td class="greybox"><s:textfield name="asOndate" id="asOndate"
							cssStyle="width:100px" /><a
						href="javascript:show_calendar('incomeExpenditureReport.asOndate');"
						style="text-decoration: none">&nbsp;<img
							src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a>(dd/mm/yyyy)
					</td>
					<td class="greybox"><s:text name="report.rupees" />:<span
						class="mandatory1">*</span></td>
					<td class="greybox"><s:select name="currency" id="currency"
							list="#{'Rupees':'Rupees','Thousands':'Thousands','Lakhs':'Lakhs'}"
							value="%{model.currency}" /></td>
				</tr>
				<tr>
					<td class="bluebox">&nbsp;</td>
					<td class="bluebox"><s:text name="report.department" />:</td>
					<td class="bluebox"><s:select name="department"
							id="department" list="dropdownData.departmentList" listKey="id"
							listValue="name" headerKey="0" headerValue="----Select----"
							value="model.department.id" /></td>
					<td class="bluebox"><s:text name="report.fund" />:</td>
					<td class="bluebox"><s:select name="fund" id="fund"
							list="dropdownData.fundDropDownList" listKey="id"
							listValue="name" headerKey="0" headerValue="----Select----"
							value="model.fund.id" /></td>
				</tr>
				<tr>
					<td class="greybox">&nbsp;</td>
					<td class="greybox"><s:text name="report.function" />:</td>
					<td class="greybox"><s:select name="function" id="function"
							list="dropdownData.functionList" listKey="id" listValue="name"
							headerKey="0" headerValue="----Select----"
							value="model.function.id" /></td>
					<td class="greybox"><s:text name="report.functionary" />:</td>
					<td class="greybox"><s:select name="functionary"
							id="functionary" list="dropdownData.functionaryList" listKey="id"
							listValue="name" headerKey="0" headerValue="----Select----"
							value="model.functionary.id" /></td>
				</tr>
				<tr>
					<td class="bluebox">&nbsp;</td>
					<td class="bluebox"><s:text name="report.field" />:</td>
					<td class="bluebox"><s:select name="field" id="field"
							list="dropdownData.fieldList" listKey="id" listValue="name"
							headerKey="0" headerValue="----Select----" value="model.field.id" />
					</td>
				</tr>
				<tr>
					<td></td>
				</tr>
			</table>
			<div align="left" class="mandatory1">
				*
				<s:text name="report.mandatory.fields" />
			</div>
			<div class="buttonbottom" style="padding-bottom: 10px;">
				<input type="button" value="Submit" class="buttonsubmit"
					onclick="return getData()" /> <input name="button" type="button"
					class="buttonsubmit" id="button3" value="Print"
					onclick="window.print()" />&nbsp;&nbsp; <input type="button"
					value="View All Minor Schedules" class="buttonsubmit"
					onclick="return showAllMinorSchedules()" /> &nbsp;&nbsp; <input
					type="button" value="View All Schedules" class="buttonsubmit"
					onclick="return showAllSchedules()" /> &nbsp;&nbsp;
			</div>
			<div align="left" class="extracontent">
				To print the report, please ensure the following settings:<br /> 1.
				Paper size: A4<br /> 2. Paper Orientation: Landscape <br />
			</div>
		</div>
	</s:form>
</div>
<script>
disableAsOnDate();
</script>
<div id="result"></div>

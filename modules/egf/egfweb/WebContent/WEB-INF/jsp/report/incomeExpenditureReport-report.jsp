<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %> 
<link href="common/css/budget.css" rel="stylesheet" type="text/css" />
<link href="common/css/commonegov.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" language="javascript" src="common/js/test.js"></script>
 <style type="text/css">

    @media print
    {
        #non-printable { display: none; }
    }
    </style>

<script>
var callback = {
		success: function(o){
			document.getElementById('result').innerHTML=o.responseText;
			},
			failure: function(o) {
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
	if(document.getElementById('period').value!="Date"){
		if(document.getElementById('financialYear').value==0){
			alert('Please select a Financial year');
			return false;
		}
	}
	if(document.getElementById('period').value=="Date" && document.getElementById('asOndate').value==""){
		alert('Please enter As On Date');
		return false;
	}
	return true;
}
function getData(){
	if(validateMandatoryFields()){
		var url = '/EGF/report/incomeExpenditureReport!ajaxPrintIncomeExpenditureReport.action?showDropDown=false&model.period='+document.getElementById('period').value+'&model.currency='+document.getElementById('currency').value+'&model.financialYear.id='+document.getElementById('financialYear').value+'&model.department.id='+document.getElementById('department').value+'&model.function.id='+document.getElementById('function').value+'&model.field.id='+document.getElementById('field').value+'&model.functionary.id='+document.getElementById('functionary').value+'&model.asOndate='+document.getElementById('asOndate').value;
		YAHOO.util.Connect.asyncRequest('POST', url, callback, null);
		return true;
    }
	return false;
}
function showAllSchedules(){
	if(validateMandatoryFields()){
		window.open('/EGF/report/incomeExpenditureReport!generateScheduleReport.action?showDropDown=false&model.period='+document.getElementById('period').value+'&model.currency='+document.getElementById('currency').value+'&model.financialYear.id='+document.getElementById('financialYear').value+'&model.department.id='+document.getElementById('department').value+'&model.function.id='+document.getElementById('function').value+'&model.field.id='+document.getElementById('field').value+'&model.functionary.id='+document.getElementById('functionary').value+'&model.asOndate='+document.getElementById('asOndate').value,'','resizable=yes,height=650,width=900,scrollbars=yes,left=30,top=30,status=no');
	return true;
    }
	return false;
}

function showSchedule(majorCode){
	if(validateMandatoryFields()){
		window.open('/EGF/report/incomeExpenditureReport!generateIncomeExpenditureSubReport.action?showDropDown=false&model.period='+document.getElementById('period').value+'&model.currency='+document.getElementById('currency').value+'&model.financialYear.id='+document.getElementById('financialYear').value+'&model.department.id='+document.getElementById('department').value+'&model.asOndate='+document.getElementById('asOndate').value+'&majorCode='+majorCode,'','height=650,width=900,scrollbars=yes,left=30,top=30,status=no');
	return true;
    }
	return false;
}
</script>
<style>
th.bluebgheadtd{
	padding:0px;
	margin:0px;
}
.extracontent{
	font-weight:bold;
	font-size:xx-small;
	color:#CC0000;
}
</style>
<div id="non-printable">
<s:form name="incomeExpenditureReport" action="incomeExpenditureReport" theme="simple">
<div class="formmainbox">
	<div class="formheading"></div>
	<div class="subheadnew">Income Expenditure Report</div>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
	  <tr>
	    <td width="10%" class="bluebox">&nbsp;</td>
	    <td width="15%" class="bluebox"><s:text name="report.period"/>:<span class="mandatory">*</span></td>
	    <td width="22%" class="bluebox">
		<s:select name="period" id="period" list="#{'Select':'---Choose---','Date':'Date','Yearly':'Yearly','Half Yearly':'Half Yearly'}" onclick="disableAsOnDate()" value="%{model.period}"/>
		</td>
	    <td class="bluebox" width="12%"><s:text name="report.financialYear"/>:<span class="mandatory">*</span></td>
	    <td width="41%" class="bluebox">
		<s:select name="financialYear" id="financialYear" list="dropdownData.financialYearList" listKey="id" listValue="finYearRange" headerKey="0" headerValue="----Select----"  value="%{model.financialYear.id}" />
	    </td>
	  </tr>
	  <tr>
	    <td class="greybox">&nbsp;</td>
	    <td class="greybox"><s:text name="report.asOnDate"/>:</td>
	    <td class="greybox">
		<s:textfield name="asOndate" id="asOndate" cssStyle="width:100px"/><a href="javascript:show_calendar('incomeExpenditureReport.asOndate');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>(dd/mm/yyyy)
		</td>
		<td class="greybox"><s:text name="report.rupees"/>:<span class="mandatory">*</span></td>
	    <td class="greybox">
		<s:select name="currency" id="currency" list="#{'Rupees':'Rupees','Thousands':'Thousands','Lakhs':'Lakhs'}" value="%{model.currency}"/>
		</td>	
	  </tr>
	  <tr>
	    <td class="bluebox">&nbsp;</td>
	    <td class="bluebox"><s:text name="report.department"/>:</td>
	    <td class="bluebox">
		<s:select name="department" id="department" list="dropdownData.departmentList" listKey="id" listValue="deptName" headerKey="0" headerValue="----Select----"  value="model.department.id" />      
		 </td>
	    <td class="bluebox"><s:text name="report.functionary"/>:</td>
	    <td class="bluebox">
		<s:select name="functionary" id="functionary" list="dropdownData.functionaryList" listKey="id" listValue="name" headerKey="0" headerValue="----Select----"  value="model.functionary.id" />
		</td>
	  </tr>
	  <tr>
	    <td class="greybox">&nbsp;</td>
	    <td class="greybox"><s:text name="report.function"/>:</td>
	    <td class="greybox">
			<s:select name="function" id="function" list="dropdownData.functionList" listKey="id" listValue="name" headerKey="0" headerValue="----Select----"  value="model.function.id" />
		</td>
	    <td class="greybox"><s:text name="report.field"/>:</td>
	    <td class="greybox">
		<s:select name="field" id="field" list="dropdownData.fieldList" listKey="id" listValue="name" headerKey="0" headerValue="----Select----"  value="model.field.id" />
		</td>
	  </tr>
	  <tr>
	  <td>  </td>
	  </tr>
	</table>
	<div align="left" class="mandatory">* <s:text name="report.mandatory.fields"/></div>
	<div class="buttonbottom" style="padding-bottom:10px;"> 
		<input type="button" value="Submit" class="buttonsubmit" onclick="return getData()"/>
		<input name="button" type="button" class="buttonsubmit" id="button3" value="Print" onclick="window.print()"/>&nbsp;&nbsp;
	 	<input type="button" value="View All Schedules" class="buttonsubmit" onclick="return showAllSchedules()"/>	&nbsp;&nbsp;
	</div>
	<div align="left" class="extracontent">
	To print the report, please ensure the following settings:<br />
	     1. Paper size: A4<br />
	     2. Paper Orientation: Landscape <br />
	</div>
</div>
</s:form>
</div>
<script>
disableAsOnDate();
</script>
<div id="result">
</div>

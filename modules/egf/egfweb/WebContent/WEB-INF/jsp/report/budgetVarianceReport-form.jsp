<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<head>
	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/contra.js"></script>
	<script type="text/javascript" src="/EGF/commonjs/ajaxCommonFunctions.js"></script>
</head>
<script>
var callback = {
		success: function(o){
			document.getElementById('results').innerHTML=o.responseText;
			},
			failure: function(o) {
		    }
		}
function getData(){
	var asOnDate =  document.getElementById('asOnDate').value;
	var accountType =  document.getElementById('accountType').value;
	var budgetGroup =  document.getElementById('budgetGroup').value;
	isValid = validateData();
	if(isValid == false)
		return false;
	var url = '/EGF/report/budgetVarianceReport!ajaxLoadData.action?skipPrepare=true&asOnDate='+asOnDate+'&accountType='+accountType+'&budgetDetail.budgetGroup.id='+budgetGroup+getMiscData();
	YAHOO.util.Connect.asyncRequest('POST', url, callback, null);
}

function getMiscData(){
	var fund,department,functionary,field,scheme,subscheme,data="";
	fund = document.getElementById('fund');
	department = document.getElementById('executingDepartment');
	functionId = document.getElementById('function');
	functionary = document.getElementById('functionary');
	functionary = document.getElementById('subScheme');
	functionary = document.getElementById('scheme');
	functionary = document.getElementById('boundary');
	if(fund != undefined)
		data = data+"&budgetDetail.fund.id="+fund.value;
	if(department != undefined)
		data = data+"&budgetDetail.executingDepartment.id="+department.value;
	if(functionary != undefined)
		data = data+"&budgetDetail.functionary.id="+functionary.value;
	if(functionId != undefined)
		data = data+"&budgetDetail.function.id="+functionId.value;
	if(field != undefined)
		data = data+"&budgetDetail.boundary.id="+field.value;
	if(scheme != undefined)
		data = data+"&budgetDetail.scheme.id="+scheme.value;
	if(subscheme != undefined)
		data = data+"&budgetDetail.subScheme.id="+subscheme.value;
	return data;
}

function exportXls(){
	var asOnDate =  document.getElementById('asOnDate').value;
	var departmentid =  document.getElementById('executingDepartment').value;
	var accountType =  document.getElementById('accountType').value;
	var budgetGroup =  document.getElementById('budgetGroup').value;
	var functionId =  document.getElementById('function').value;
	window.open('/EGF/report/budgetVarianceReport!exportXls.action?skipPrepare=true&asOnDate='+asOnDate+'&accountType='+accountType+'&budgetDetail.budgetGroup.id='+budgetGroup+getMiscData(),'','resizable=yes,height=650,width=900,scrollbars=yes,left=30,top=30,status=no');
}

function exportPdf(){
	var asOnDate =  document.getElementById('asOnDate').value;
	var departmentid =  document.getElementById('executingDepartment').value;
	var accountType =  document.getElementById('accountType').value;
	var budgetGroup =  document.getElementById('budgetGroup').value;
	var functionId =  document.getElementById('function').value;
	window.open('/EGF/report/budgetVarianceReport!exportPdf.action?skipPrepare=true&asOnDate='+asOnDate+'&accountType='+accountType+'&budgetDetail.budgetGroup.id='+budgetGroup+getMiscData(),'','resizable=yes,height=650,width=900,scrollbars=yes,left=30,top=30,status=no');
}

function validateData(){
	var asOnDate =  Date.parse(document.getElementById('asOnDate').value);
	if(asOnDate == ''){
		alert("Please enter a valid date")
		return false;
	}
	<s:if test="%{isFieldMandatory('executingDepartment')}">
		if(!checkMandatoryField("executingDepartment"))
			return false;
	</s:if>
	<s:if test="%{isFieldMandatory('function')}">
		if(!checkMandatoryField("function"))
			return false;
	</s:if>
	<s:if test="%{isFieldMandatory('fund')}">
		if(!checkMandatoryField("fund"))
			return false;
	</s:if>
	<s:if test="%{isFieldMandatory('functionary')}">
		if(!checkMandatoryField("functionary"))
			return false;
	</s:if>
	<s:if test="%{isFieldMandatory('scheme')}">
		if(!checkMandatoryField("scheme"))
			return false;
	</s:if>
	<s:if test="%{isFieldMandatory('subScheme')}">
		if(!checkMandatoryField("subScheme"))
			return false;
	</s:if>
	<s:if test="%{isFieldMandatory('boundary')}">
		if(!checkMandatoryField("boundary"))
			return false;
	</s:if>
	return true;	
}

function checkMandatoryField(fieldName){
	var field = document.getElementById(fieldName);
	if(field.value == -1){
		alert("Please select a "+fieldName)
		return false;
	}
	return true;
}
</script>
<body>
<div class="formmainbox">
<div class="formheading"></div>
<div class="subheadnew">Budget Variance Report</div>


<s:form action="budgetVarianceReport" theme="simple" name="budgetVarianceReport">
<table width="100%" cellpadding="0" cellspacing="0" border="0">
	<tr>
		<s:if test="%{shouldShowHeaderField('executingDepartment')}">
		    <td class="bluebox" width="10%">Department:<s:if test="%{isFieldMandatory('executingDepartment')}"><span class="mandatory">*</span></s:if></td>
		    <td class="bluebox">
		    	<s:select name="executingDepartment" id="executingDepartment" list="dropdownData.departmentList" listKey="id" listValue="deptName" headerKey="-1" headerValue="----Choose----" />
		    </td>
	    </s:if>
	    <s:if test="%{shouldShowHeaderField('function')}">
			<td class="bluebox" width="10%">Function:<s:if test="%{isFieldMandatory('function')}"><span class="mandatory">*</span></s:if></td>
			<td class="bluebox">
				<s:select  name="function" id="function" list="dropdownData.functionList" listKey="id" listValue="name" headerKey="-1" headerValue="----Choose----"/>
			</td>
		</s:if>
	</tr>
	<tr>
		<s:if test="%{shouldShowHeaderField('fund')}">
		    <td class="greybox" width="10%">Fund:<s:if test="%{isFieldMandatory('fund')}"><span class="mandatory">*</span></s:if></td>
		    <td class="greybox">
		    	<s:select name="fund" id="fund" list="dropdownData.fundList" listKey="id" listValue="name" headerKey="-1" headerValue="----Choose----" />
		    </td>
	    </s:if>
	    <s:if test="%{shouldShowHeaderField('functionary')}">
			<td class="greybox" width="10%">Functionary:<s:if test="%{isFieldMandatory('functionary')}"><span class="mandatory">*</span></s:if></td>
			<td class="greybox">
				<s:select  name="functionary" id="functionary" list="dropdownData.functionaryList" listKey="id" listValue="name" headerKey="-1" headerValue="----Choose----"/>
			</td>
		</s:if>
		<s:else>
		    <td class="greybox">&nbsp;</td>
		    <td class="greybox">&nbsp;</td>
		</s:else>
	</tr>
	<tr>
		<s:if test="%{shouldShowHeaderField('scheme')}">
				<td width="10%" class="bluebox">&nbsp;</td>
				<td class="bluebox"><s:text name="scheme"/>:<s:if test="%{isFieldMandatory('scheme')}"><span class="mandatory">*</span></s:if></td>
			    <td class="bluebox"><s:select list="dropdownData.schemeList"  listKey="id" listValue="name" headerKey="0" headerValue="--- Select ---" name="scheme" onchange="updateGrid('scheme.id',document.getElementById('budgetDetail_scheme').selectedIndex);populateSubSchemes(this);" value="scheme.id" id="budgetDetail_scheme"></s:select></td>
		</s:if>
		<s:if test="%{shouldShowHeaderField('subScheme')}">
				<egov:ajaxdropdown id="subScheme" fields="['Text','Value']" dropdownId="budgetDetail_subScheme" url="budget/budgetDetail!ajaxLoadSubSchemes.action" afterSuccess="onHeaderSubSchemePopulation"/>
				<td class="bluebox"><s:text name="subScheme"/>:<s:if test="%{isFieldMandatory('subScheme')}"><span class="mandatory">*</span></s:if></td>
			    <td class="bluebox"><s:select list="dropdownData.subschemeList"  listKey="id" listValue="name" headerKey="0" headerValue="--- Select ---" name="subScheme" onchange="updateGrid('subScheme.id',document.getElementById('budgetDetail_subScheme').selectedIndex)" value="subScheme.id" id="budgetDetail_subScheme"></s:select></td>
		</s:if>
	</tr>
	<tr>
		<s:if test="%{shouldShowHeaderField('boundary')}">
				<td class="greybox"><s:text name="field"/>:<s:if test="%{isFieldMandatory('boundary')}"><span class="mandatory">*</span></s:if></td>
			    <td class="greybox"><s:select list="dropdownData.fieldList"  listKey="id" listValue="name" headerKey="0" headerValue="--- Select ---" name="boundary" onchange="updateGrid('boundary.id',document.getElementById('budgetDetail_boundary').selectedIndex)" value="boundary.id" id="budgetDetail_boundary"></s:select></td>
		</s:if>
	</tr>
	<tr>
	    <td class="bluebox" width="10%">Account Type:</td>
	    <td class="bluebox">
	    	<s:select name="accountType" id="accountType" list="dropdownData.accountTypeList" headerKey="-1" headerValue="----Choose----" />
	    </td>
		<td class="bluebox" width="10%">Budget Head:</td>
		<td class="bluebox">
			<s:select  name="budgetGroup" id="budgetGroup" list="dropdownData.budgetGroupList" listKey="id" listValue="name" headerKey="-1" headerValue="----Choose----"/>
		</td>
	</tr>
	<tr>
		<td class="greybox" width="10%">As On Date:<span class="mandatory">*</span></td>
		<td class="greybox">
			<s:textfield name="asOnDate" id="asOnDate" cssStyle="width:100px" value='%{getFormattedDate(asOnDate)}' onkeyup="DateFormat(this,this.value,event,false,'3')"/><a href="javascript:show_calendar('budgetVarianceReport.asOnDate');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>(dd/mm/yyyy)<br/>
		</td>
		<td class="greybox">&nbsp;</td>
		<td class="greybox">&nbsp;</td>
	</tr>
</table>
<br/><br/>
<div class="buttonbottom">
  <input type="button" value="Search" class="buttonsubmit" onclick="return getData()"/>
  &nbsp;
	<s:reset name="button" type="submit" cssClass="button" id="button" value="Cancel"/>
	<s:submit value="Close" onclick="javascript: self.close()" cssClass="button"/>
</div>
</div>
</s:form>

<div id="results">
</div>
</body>
</html>

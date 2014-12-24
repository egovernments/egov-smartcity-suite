<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<head>
	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/contra.js"></script>
	<script type="text/javascript" src="/EGF/commonjs/ajaxCommonFunctions.js"></script>
</head>
<script>

function validateFields() {
	<s:if test="%{isFieldMandatory('executingDepartment')}">
		if(document.getElementById('department').value == '0') {
			alert("Please select a Department");
			return false;
		}
	</s:if>
	if(document.getElementById('budgetHeadId').value == '0') {
		alert("Please select a Budget Head");
		return false;
	}
	<s:if test="%{isFieldMandatory('function')}">
	if(document.getElementById('function').value == '0') {
		alert("Please select a Function");
		return false;
	}
	</s:if>
	<s:if test="%{isFieldMandatory('fund')}">
	if(document.getElementById('fund').value == '0') {
		alert("Please select a Fund")
		return false;
	}
	</s:if>
	if(document.getElementById('asOnDate').value == '' ) {
		alert("Please select the As On Date");
		return false;
	}

	var asOnDate =  Date.parse(document.getElementById('asOnDate').value);
	if( isNaN(asOnDate) ) {
		alert("Please enter valid As On Date");
		return false;
	}

	return true;	
}

function generateReport(){
	var asOnDate =  document.getElementById('asOnDate').value;
	var department = document.getElementById('department').value;
	var functionId = document.getElementById('function').value;
	var budgetHeadId = document.getElementById('budgetHeadId').value;
	var fundId = document.getElementById('fund').value;
	
	isValid = validateFields();
	if(isValid == false)
		return false;
		
	var url = '../report/budgetAppropriationRegisterReport!search.action?asOnDate='+asOnDate+'&department.id='+department+'&function.id='+functionId+'&budgetGroup.id='+budgetHeadId+'&fund.id='+fundId;
	window.open(url, 'Search','resizable=no,scrollbars=yes,left=300,top=40, width=1200, height=700');
}
</script>
<body>
<div class="formmainbox">
<div class="formheading"></div>
<div class="subheadnew">Budget Appropriation Register Report</div>
<br/>

<s:form action="budgetAppropriationRegisterReport" theme="simple" name="budgetAppropriationRegister">
<table width="100%" cellpadding="0" cellspacing="0" border="0">
	<tr>
	    <td class="bluebox">&nbsp;</td>
		<td class="bluebox"><s:text name="report.department"/><s:if test="%{isFieldMandatory('executingDepartment')}"><span class="mandatory">*</span></s:if></td>
	    <td class="bluebox"><s:select list="dropdownData.executingDepartmentList"  listKey="id" listValue="deptName" name="department.id" headerKey="0" headerValue="--- Select ---" value="department.id" id="department"></s:select></td>
		<td class="bluebox"><s:text name="report.budged.head"/><span class="mandatory">*</span></td>
		<td class="bluebox"><s:select list="dropdownData.budgetGroupList"  listKey="id" listValue="name" name="budgetGroup.id" headerKey="0" headerValue="--- Select ---" value="budgetGroup.id" id="budgetHeadId"></s:select></td>
		<td class="bluebox">&nbsp;</td>
	</tr>
	<tr>
	    <td class="greybox">&nbsp;</td>
		<td class="greybox"><s:text name="report.function.center"/><s:if test="%{isFieldMandatory('function')}"><span class="mandatory">*</span></s:if></td>
	    <td class="greybox"><s:select list="dropdownData.functionList"  listKey="id" listValue="name" name="function.id" headerKey="0" headerValue="--- Select ---"  value="function.id" id="function"></s:select></td>
		<td class="greybox">As on Date:<span class="mandatory">*</span></td>
		<td class="greybox">
			<s:textfield name="asOnDate" id="asOnDate" cssStyle="width:100px" onkeyup="DateFormat(this,this.value,event,false,'3')"/><a href="javascript:show_calendar('budgetAppropriationRegister.asOnDate');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>(dd/mm/yyyy)
		</td>
	</tr>

	<tr>
	  <td class="bluebox">&nbsp;</td>
		<td  class="bluebox">
			<s:text name="report.fund"/><s:if test="%{isFieldMandatory('fund')}"><span class="mandatory">*</span></s:if>
		</td>
	    <td  class="bluebox"><s:select list="dropdownData.fundList"  listKey="id" listValue="name" name="fund" headerKey="0" headerValue="--- Select ---"  value="fund.id" id="fund"></s:select></td>
	</tr>	
</table>
<br/><br/>
<div class="subheadsmallnew"></div>
<div align="left" class="mandatory">* Mandatory Fields</div>

<div class="buttonbottom">
  <input type="button" value="Submit" class="buttonsubmit" onclick="return generateReport()"/>
  &nbsp;
	<s:reset name="button" type="submit" cssClass="button" id="button" value="Cancel"/>
	<s:submit value="Close" onclick="javascript: self.close()" cssClass="button"/>
</div>
	<input type="hidden" name="accountNumber.id" id="accountNumber.id"/>
</div>
</s:form>

<div id="results">
</div>
</body>
</html>

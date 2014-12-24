<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ page language="java"%>
<html>  
<head>  
    <title><s:text name="bill.salarybill.register"/></title>
    <link href="common/css/budget.css" rel="stylesheet" type="text/css" />
	<link href="common/css/commonegov.css" rel="stylesheet" type="text/css" />
	<script>
var callback = {
		success: function(o){
			document.getElementById('results').innerHTML=o.responseText;
			},
			failure: function(o) {
		    }
		};
function searchBills(){
	var fromDate =  document.getElementById('fromDate').value;
	var toDate = document.getElementById('toDate').value;
	var dept = document.getElementById('department').value;
	var month = document.getElementById('month').value;
	isValid = validateDates();
	if(isValid == false)
		return false;
	var url = '../bill/salaryBillRegisterView!ajaxSearch.action?fromDate='+fromDate+'&toDate='+toDate+'&department.id='+dept+'&month='+month;
	YAHOO.util.Connect.asyncRequest('POST', url, callback, null);
}

function validateDates(){
	var fromDate =  Date.parse(document.getElementById('fromDate').value);
	var toDate = Date.parse(document.getElementById('toDate').value);
	if(isNaN(toDate) || isNaN(fromDate)){
		alert("Please enter valid dates")
		return false;
	}
	if (toDate < fromDate){
		alert("From date should be greater than To date")
		return false;
	}
	if(fromDate == '' || toDate == ''){
		alert("Please select the dates")
		return false;
	}
	return true;	
}
</script>
</head> 
	<body>  
		<s:form action="salaryBillRegisterView" theme="simple" name="salaryBillView">  
			<span class="mandatory">
				<s:actionerror/>  
				<s:fielderror />
				<s:actionmessage />
			</span>
			<div class="formmainbox"><div class="subheadnew"><s:text name="bill.salarybill.register.view"/></div>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
				  <tr>
				  <td width="9%" class="bluebox">&nbsp;</td>
				    <td width="18%" class="bluebox"><s:text name="billDate"/></>:<span class="mandatory">*</span></td>
				    <td width="23%" class="bluebox">
				    	<input type="text" name="fromDate"  id="fromDate" onkeyup="DateFormat(this,this.value,event,false,'3')"/>
							<a href="javascript:show_calendar('salaryBillView.fromDate');" style="text-decoration:none">&nbsp;<img tabIndex="-1" src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></A>(dd/mm/yyyy)</td>
				    </td>
				    <td width="18%" class="bluebox"><s:text name="billDate"/></>:<span class="mandatory">*</span></td>
				    <td width="23%" class="bluebox">
				    	<input type="text" name="toDate"  id="toDate" onkeyup="DateFormat(this,this.value,event,false,'3')"/>
							<a href="javascript:show_calendar('salaryBillView.toDate');" style="text-decoration:none">&nbsp;<img tabIndex="-1" src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></A>(dd/mm/yyyy)</td>
				    </td>
				  </tr>
				  <tr>
				    <td class="greybox">&nbsp;</td>
				    <td class="greybox">For the Month of:</td>
				    <td class="greybox"><select name="month" id="month">
				      <option value="-1" selected="selected">------Choose-----</option>
				      <option value="1"> January</option> 
				      <option value="2">February</option> 
				      <option value="3">March</option> 
				      <option value="4">April</option> 
				      <option value="5">May</option> 
				      <option value="6">June</option> 
				      <option value="7">July</option> 
				      <option value="8">August</option> 
				      <option value="9">September</option> 
				      <option value="10">October</option> 
				      <option value="11">November</option> 
				      <option value="12">December</option>
				      </select></td>
					<td class="greybox"><s:text name="department"/>:</td>
					<td class="greybox"><s:select name="department.id" id="department" list="dropdownData.departmentList" listKey="id" listValue="deptName" headerKey="-1" headerValue="----Choose----" /></td>
				  </tr>
				  </tr>
				</table>
					
				<div class="buttonbottom">
					<input type="button" class="buttonsubmit" id="search" name="search" value="Search" onClick="return searchBills();"/>
					<s:reset name="button" type="submit" cssClass="button" id="button" value="Cancel"/>
					<s:submit value="Close" onclick="javascript: self.close()" cssClass="button"/>
				</div>
			</div>
			<div id="results">
			</div>
		</s:form>  
	</body>  
</html>

<%@ include file="/includes/taglibs.jsp" %>
<%@page import="java.util.*" %>
<%@ page import="org.egov.payroll.utils.PayrollConstants,org.egov.infstr.commons.dao.*" %>

 <jsp:useBean  id="advanceForm" scope="request"
       type="org.egov.payroll.client.advance.AdvanceForm" />

<html>
<head>

	<title>Salary Advance creation</title>

	<style type="text/css">
		#codescontainer {position:absolute;left:11em;width:9%}
		#codescontainer .yui-ac-content {position:absolute;width:80%;border:1px solid #404040;background:#fff;overflow:hidden;z-index:9050;}
		#codescontainer .yui-ac-shadow {position:absolute;margin:.3em;width:40%;background:#a0a0a0;z-index:9049;}
		#codescontainer ul {padding:5px 0;width:80%;}
		#codescontainer li {padding:0 5px;cursor:default;white-space:nowrap;}
		#codescontainer li.yui-ac-highlight {background:#ff0;}
		#codescontainer li.yui-ac-prehighlight {background:#FFFFCC;}
</style>

<%
	String advanceWfType = GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getAppConfigValueByDate("Payroll",PayrollConstants.ADVANCE_WF_TYPE,new java.util.Date()).getValue();
%>

<SCRIPT type="text/javascript" src="<c:url value='/javascript/payrollUtil.js' />"></SCRIPT>
<script language="JavaScript"  type="text/JavaScript">
		var empCodeArray;
		var selectedEmpCode;
		var acctCodeArray;
		var selectedAcctCode;
		var yuiflag = new Array();
		var yuiflag1 = new Array();
/* if will invoke addRowToTable() method based on key pressed */
		
		var interest_type = "<%= PayrollConstants.SAL_ADV_TYPE_INTEREST %>";
		var nonInterest_type = "<%= PayrollConstants.SAL_ADV_TYPE_NONINTEREST %>";
		var simpleInterest = "<%= PayrollConstants.SAL_ADV_INTEREST_TYPE_SIMPLE %>";	
		var reducingInterest = "<%= PayrollConstants.SAL_ADV_INTEREST_TYPE_REDUCING %>";		
		var chequePayment = "<%= PayrollConstants.SAL_ADV_PAYMENT_TYPE_CHEQUE %>";
		var cashPayment = "<%= PayrollConstants.SAL_ADV_PAYMENT_TYPE_CASH %>";
		var bankPayment = "<%= PayrollConstants.SAL_ADV_PAYMENT_TYPE_DIRECT_BANK %>";
		
function deleteRow(table,obj)
{
if(table=='paytable')
{
var tbl = document.getElementById(table);
var rowNumber=getRow(obj).rowIndex;
if(${fn:length(salaryPaySlipForm.payHead)}<(eval(rowNumber)-1))
   tbl.deleteRow(rowNumber)
else
{
     alert("You cannot delete this row");
     return false;
}
}
if(table=='salAdvances')
{
	var tbl = document.getElementById(table);
	var rowNumber=getRow(obj).rowIndex;
	if(${fn:length(salaryPaySlipForm.salaryAdvances)}<(eval(rowNumber)-6))
	   tbl.deleteRow(rowNumber)
	else
	{
	     alert("You cannot delete this row");
	     return false;
	}
}
if(table=='deductions')
{
	var tbl = document.getElementById(table);
	var rowNumber=getRow(obj).rowIndex;
	if(${fn:length(salaryPaySlipForm.salaryAdvances)}<(eval(rowNumber)-1))
	   tbl.deleteRow(rowNumber)
	else
	{
	     alert("You cannot delete this row");
	     return false;
	}
}
}
function addRowToTable(tbl,obj)
{
  tableObj=document.getElementById(tbl);
  var rowObj1=getRow(obj);
  var tbody=tableObj.tBodies[0];
  var lastRow = tableObj.rows.length;
   if(tbl=='paytable' && lastRow<getControlInBranch(tableObj.rows[rowObj1.rowIndex],'payHead').options.length)
  {
  	   var rowObj = tableObj.rows[lastRow-1].cloneNode(true);
       tbody.appendChild(rowObj);
  	   var remlen1=document.salaryPaySlipForm.payHeadAmount.length;
 	   document.salaryPaySlipForm.pct[remlen1-1].value="";
 	   document.salaryPaySlipForm.payHeadAmount[remlen1-1].value="";
 	   document.salaryPaySlipForm.calculationType[remlen1-1].value="";
 	   document.salaryPaySlipForm.pctBasis[remlen1-1].value="";
	   //document.salaryPaySlipForm.yearToDate[remlen1-1].value="";
  }
  else
  {
	  if(tbl=='paytable')
	  {
	    alert("No pay Heads Available to insert");
	    return false;
	  }
  }
  if(tbl=='salAdvances' && ((lastRow-eval(5))<getControlInBranch(tableObj.rows[rowObj1.rowIndex],'salaryAdvances').length))
  {
	 var rowObj = tableObj.rows[lastRow-1].cloneNode(true);
	  tbody.appendChild(rowObj);
  	  var remlen=document.salaryPaySlipForm.salaryAdvances.length;
	  document.salaryPaySlipForm.salaryAdvancesAmount[remlen-1].value="";
  }
  else
  {
  	  if(tbl=='salAdvances')
  	  {
  	    alert("No salary Advances Available to insert");
  	    return false;
  	  }
  }
  if(tbl=='deductions' && (lastRow<acctCodeArray.length))
    {
  	  var rowObj = tableObj.rows[lastRow-1].cloneNode(true);
  	  tbody.appendChild(rowObj);
  	  var remlen=document.salaryPaySlipForm.otherDeductAccountCode.length;
  	  document.salaryPaySlipForm.otherDeductAccountCode[remlen-1].value="";
  	  document.salaryPaySlipForm.otherDeductionsAmount[remlen-1].value="";
  }
  else
  {
  	 if(tbl=='deductions')
  	 {
  	   alert("No otherDeduction AccountCode Available to insert");
  	   return false;
     }
  }
}


 function onBodyLoad()
 {  
   loadEmpCodes(); 
   
   //To disable the maintain schedule flag.
   //document.getElementById("maintainScheduleFlag").disabled =true;
 }

 function initiateRequest() {
	if (window.XMLHttpRequest) {
		return new XMLHttpRequest();
	} else if (window.ActiveXObject) {
		isIE = true;
		return new ActiveXObject("Microsoft.XMLHTTP");
	}
   }
/*
* This function returns absolue left and top position of the object
*/
	function findPos(obj)
	{
		var curleft = curtop = 0;
		if (obj.offsetParent)
		{
			curleft = obj.offsetLeft;
			curtop = obj.offsetTop;
			while (obj = obj.offsetParent)
			{	//alert(obj.nodeName);
				curleft =curleft + obj.offsetLeft;
				curtop =curtop + obj.offsetTop; //alert(curtop);
			}
		}
		return [curleft,curtop];
	}
function getRow(obj)
 {
 	if(!obj)return null;
 	tag = obj.nodeName.toUpperCase();
 	while(tag != 'BODY'){
 		if (tag == 'TR') return obj;
 		obj=obj.parentNode;
 		tag = obj.nodeName.toUpperCase();
 	}
 	return null;
 }

 function autocompleteEmpCode(obj,event)
 {
 	// set position of dropdown
 	var src = obj;
 	var target = document.getElementById('codescontainer');
 	var posSrc=findPos(src);
 	target.style.left=posSrc[0];
 	target.style.top=posSrc[1]+25;
 	if(obj.name=='employeeCode') target.style.left=posSrc[0]+0;

 	target.style.width=500;

 	var currRow=getRow(obj);
 	var coaCodeObj = obj;
 	if(yuiflag1[currRow.rowIndex] == undefined)
 	{
 	//40 --> Down arrow, 38 --> Up arrow
 	if(event.keyCode != 40 )
 	{
 		if(event.keyCode != 38 )
 		{

 				var oAutoComp1 = new YAHOO.widget.AutoComplete(coaCodeObj,'codescontainer', selectedEmpCode);
 				oAutoComp1.queryDelay = 0;
 				oAutoComp1.useShadow = true;
 				oAutoComp1.maxResultsDisplayed = 15;
   				oAutoComp1.useIFrame = true;
 		}
 	}
 	yuiflag1[currRow.rowIndex]=1;
  }
}

function getControlInBranch(obj,controlName)
{
	if (!obj || !(obj.getAttribute)) return null;
	if (obj.getAttribute('name') == controlName) return obj;

	var children = obj.childNodes;
	var child;
	if (children && children.length > 0){
		for(var i=0; i<children.length; i++){
			child=this.getControlInBranch(children[i],controlName);
			if(child) return child;
		}
	}
	return null;
}
function fillNeibrAfterSplit(obj,neibrObjName)
{
	var currRow=getRow(obj);	
	yuiflag[currRow.rowIndex] = undefined;
	neibrObj=getControlInBranch(currRow,neibrObjName);
	var temp = obj.value;
	temp = temp.split("`-`");
	document.advanceForm.checkEmpCode.value = temp[1];
	obj.value=temp[0];
	if(temp[1] == null)
		getEmployeeByEnteringCode(temp[0],neibrObj);
	if(temp[2]==null || temp[2]=="") { neibrObj.value=""; return; }
	if(temp[1]==null && (neibrObj.value!='' || neibrObj.value!=null) ) {  return ;}
	else {
		document.getElementById("employeeName").value=temp[1];
		neibrObj.value=temp[2];			
	}
	
 }

 function getEmployeeByEnteringCode(code,empId){
		var url = "<%=request.getContextPath()%>"+"/commons/process.jsp?type=getEmployeeByCode&code="+code;
      	var req = initiateRequest();
      	req.onreadystatechange = function(){
	      if (req.readyState == 4){
	            if (req.status == 200){
                   	var glcodes=req.responseText
                   	var a = glcodes.split("^");
                   	var codes = a[0];								
					var emp = codes.split("`-`");
					if(codes == "false"){
						//alert("Enter correct glcode");
						//document.payheadForm.glcode.focus();
					}
					else{
						empId.value = emp[2];
						document.advanceForm.checkEmpCode.value = emp[1];  	
				  		document.getElementById("employeeName").value = emp[1];
					}
	           }
	       }
        };
	   req.open("GET", url, true);
	   req.send(null);
  }



 function loadEmpCodes()
 { 	
	 var type='getAllEmployeeCodes';
		var url = "<%=request.getContextPath()%>"+"/commons/process.jsp?type=" +type+ " ";
		var req2 = initiateRequest();
		 req2.onreadystatechange = function()
		 {
				  if (req2.readyState == 4)
				  {
					  if (req2.status == 200)
					  {
						var codes2=req2.responseText;
						var a = codes2.split("^");
						var codes = a[0];
						empCodeArray=codes.split("+");
						selectedEmpCode = new YAHOO.widget.DS_JSArray(empCodeArray);
					  }
				  }
		};
		req2.open("GET", url, true);
	req2.send(null);

 }
   
   function removeZero(obj){
		if(obj.value==0)
		{	
			obj.value="";
			obj.focus();	
		}
		return true;
	}
	
	function checkdecimalval(obj,amount){		
	    var objt = obj;
	    var amt = amount;
	    if(amt != null && amt != "")
	    {
	      if(amt < 0 )
	        {
	            alert("Please enter positive value for the amount");
	            obj.value="";
	            objt.focus();
	            return false;
	
	        }
	        if(isNaN(amt))
	        {
	            alert("Please enter a numeric value for the amount");
	            obj.value="";
	            objt.focus();
	            return false;	
	        }	           
	    }
	}
	
	function checkForPct(obj){
		var objt = obj;
	    var amt = obj.value;
	    if(amt != null && amt != "")
	    {
	      if(amt < 0 || amt >100)
	        {
	            alert("Please enter value (0-100) for the amount");
	            obj.value="";
	            objt.focus();
	            return false;
	
	        }
	        if(isNaN(amt))
	        {
	            alert("Please enter a numeric value for the amount");
	            obj.value="";
	            objt.focus();
	            return false;	
	        }	           
	    }
	}
	
	function addZero(obj){
		if(obj.value=="")
		{
			obj.value=0;
		}
		collectionSum();
		calOnchangeAmount(obj);
		return;
	}
	   
  function populateCalculatedAmount(){
	if(document.advanceForm.pendingPrevAmt.value=='')
			document.advanceForm.pendingPrevAmt.value='0';  
  	if(document.advanceForm.advanceType.value== interest_type){
  		populateMonthlyPayment();
	/*  	var advAmount = 0;
	  	var pctAmount = 0;
	  	var interestAmount = 0;
	  	var totalAmount = 0;
	  	advAmount = eval(document.advanceForm.advAmount.value);	
	  	pctAmount = eval(document.advanceForm.interestPct.value);
	  	if(document.advanceForm.advAmount.value != "" && document.advanceForm.interestPct.value != ""){
		  	if(document.advanceForm.interestType.value == simpleInterest){
			  	interestAmount = advAmount*pctAmount/100;  	
			  	totalAmount= totalAmount+ advAmount + interestAmount;  
			  	document.advanceForm.interestAmount.value= Math.round(interestAmount);
			  	document.advanceForm.total.value = Math.round(totalAmount);
		  	}  	
	  	}	*/
  	}
  	if(document.advanceForm.advanceType.value== nonInterest_type){
  		if(document.advanceForm.advAmount.value != ""){
	  		var advAmt = eval(document.advanceForm.advAmount.value);
	  		var pendingPrvAmt = eval(document.advanceForm.pendingPrevAmt.value);
	  		document.advanceForm.total.value = Math.round(advAmt+pendingPrvAmt);
	  		if(document.advanceForm.numberOfInstallments.value != ""){
		  		var noOfInstallment = eval(document.advanceForm.numberOfInstallments.value);
	  			var monthlyPayment = eval(document.advanceForm.total.value)/noOfInstallment;
	  			document.advanceForm.monthlyPayment.value = Math.round(monthlyPayment); 
	  		}	  		
	  	}	
  	}
  }
  
  function populateMonthlyPayment(){
	if(document.advanceForm.pendingPrevAmt.value=='')
			document.advanceForm.pendingPrevAmt.value='0';  
  	if(document.advanceForm.numberOfInstallments.value != "" && document.advanceForm.advAmount.value != "" && 
  																document.advanceForm.interestPct.value != "" &&
  																document.advanceForm.interestType.value != ""){
  		var advAmount = 0;
	  	var pctAmount = 0;
	  	var interestAmount = 0;
	  	var totalAmount = 0;
	  	advAmount = eval(document.advanceForm.advAmount.value);	
	  	pctAmount = eval(document.advanceForm.interestPct.value);
	  	var pendingPrvAmt = eval(document.advanceForm.pendingPrevAmt.value);
	  	var noOfInstallment = eval(document.advanceForm.numberOfInstallments.value);
	  	if(document.advanceForm.advAmount.value != "" && document.advanceForm.interestPct.value != ""){
		  	if(document.advanceForm.interestType.value == simpleInterest){
			  	interestAmount = (advAmount+pendingPrvAmt)*pctAmount*noOfInstallment/(100*12);  	
			  	totalAmount= totalAmount+ advAmount + interestAmount + pendingPrvAmt;  
			  	document.advanceForm.interestAmount.value= Math.round(interestAmount);
			  	document.advanceForm.total.value = Math.round(totalAmount);
		  	}  	
	  	}
	  	var monthlyPayment = eval(document.advanceForm.total.value)/noOfInstallment;
	  	document.advanceForm.monthlyPayment.value = Math.round(monthlyPayment); 

		
  	}
  }
  
  function populateTotalAndMonthlyPayment(){
	if(document.advanceForm.pendingPrevAmt.value=='')
			document.advanceForm.pendingPrevAmt.value='0';    
  	if(document.advanceForm.advAmount.value != "" && document.advanceForm.interestAmount.value != ""){	
	  	var interestAmt = eval(document.advanceForm.interestAmount.value);
	  	var noOfInstallment = eval(document.advanceForm.numberOfInstallments.value);
	  	var advAmt = eval(document.advanceForm.advAmount.value);
	  	var totalAmt = interestAmt + advAmt;
		document.advanceForm.total.value = Math.round(totalAmt);
		var monthlyPayment = totalAmt/noOfInstallment;
		if(document.advanceForm.numberOfInstallments.value != "")
			document.advanceForm.monthlyPayment.value = Math.round(monthlyPayment);	
	}		
  }  
   
   function refreshFild(){
   		document.advanceForm.salarycode.value = "";
   		document.advanceForm.advanceType.value = "";
	  	document.advanceForm.advAmount.value = "";
	 	document.advanceForm.interestPct.value = "";
	 	document.advanceForm.interestType.value = "";
	 	document.advanceForm.numberOfInstallments.value = "";
	 	document.advanceForm.interestAmount.value = "";
	 	document.advanceForm.total.value = "";
	 	document.advanceForm.monthlyPayment.value = "";
  }
  
  function checkBankAccountForEmp(){
  		var empId = document.advanceForm.employeeCodeId.value;
		var action = "getBankAccountForEmp";		
		var url = "<%=request.getContextPath()%>"+"/commons/uniqueSaladvance.jsp?action=" +action+ "&empId="+empId ;
		var isUnique;
		var req = initiateRequest();
      	req.onreadystatechange = function(){
	      if (req.readyState == 4){
	            if (req.status == 200){
                   	var payheads = req.responseText
                   	var a = payheads.split("^");
                   	var codes = a[0];                   	
                   	if(codes=="true"){
                   		isUnique = "true";
                   	}
                   	else if(codes=="false"){
         				isUnique = "false";
                   	}
	       		}
	       	}
        };
		req.open("GET", url, false);
		req.send(null);				
		return isUnique;
  }
  
   function uniqueCheckSanctionNo(number){
		var action = "getAdvanceBySanctionNo";		
		var url = "<%=request.getContextPath()%>"+"/commons/uniqueSaladvance.jsp?action=" +action+ "&number="+number ;
		var isUnique;
		var req = initiateRequest();
      	req.onreadystatechange = function(){
	      if (req.readyState == 4){
	            if (req.status == 200){
                   	var payheads = req.responseText
                   	var a = payheads.split("^");
                   	var codes = a[0];                   	
                   	if(codes=="true"){
                   		isUnique = "true";
                   	}
                   	else if(codes=="false"){
         				isUnique = "false";
                   	}
	       		}
	       	}
        };
		req.open("GET", url, false);
		req.send(null);				
		return isUnique;
  }

  function checkonSubmit(){  	
	
	/**if("Deduction-BankLoan" == document.advanceForm.advanceCategory.value){
		if(document.advanceForm.sanctionNo.value == ""){
			alert('Enter sanction number');	
			document.advanceForm.sanctionNo.focus();
			return false;
		}
		if(document.advanceForm.sanctionDate.value == ""){
			alert('Enter sanction date');	
			document.advanceForm.sanctionDate.focus();
			return false;
		}
		if(uniqueCheckSanctionNo(document.advanceForm.sanctionNo.value)=="false"){
	  		alert('<bean:message key="alertSanctionNoEexists"/>');
	  		document.advanceForm.sanctionNo.focus();
	  		return false;
	  	}
	}**/
	if(document.advanceForm.checkEmpCode.value == "undefined"){
		alert('<bean:message key="alertCorrectEmployeeCode"/>');	
		document.advanceForm.employeeCode.focus();
		return false;
	}
  	if(document.advanceForm.employeeCode.value==""){
  		alert('<bean:message key="alertEmployeeCode"/>');
  		document.advanceForm.employeeCode.focus();
  		return false;
  	}  	
  	if(document.advanceForm.salarycode.value==""){
  		alert('<bean:message key="alertSalaryCode"/>');
  		document.advanceForm.salarycode.focus();
  		return false;
  	}	
  	if(document.advanceForm.advanceType.value==""){
  		alert('<bean:message key="alertAdvanceType"/>');
  		document.advanceForm.advanceType.focus();
  		return false;
  	}	
  	if(document.advanceForm.advAmount.value==""){
  		alert('<bean:message key="alertAdvAmount"/>');
  		document.advanceForm.advAmount.focus();
  		return false;
  	}	
  	if(document.advanceForm.advanceType.value== interest_type && document.advanceForm.interestPct.value==""){
  		alert('<bean:message key="alertInterestPct"/>');
  		document.advanceForm.interestPct.focus();
  		return false;
  	}	
  	if(document.advanceForm.advanceType.value== nonInterest_type && document.advanceForm.interestPct.value!=""){ 
		alert('<bean:message key="alertNonInterestedAdvance"/>');
		document.advanceForm.interestPct.value = "";
		document.advanceForm.interestPct.focus();
		return false;
  	}	
  	if(document.advanceForm.advanceType.value== interest_type && document.advanceForm.interestType.value==""){
  		alert('<bean:message key="alertInterestType"/>');
  		document.advanceForm.interestType.focus();
  		return false;
  	}	
  	if(document.advanceForm.advanceType.value== nonInterest_type && document.advanceForm.interestType.value!=""){ 
		alert('<bean:message key="alertNonInterestedAdvance"/>');
		document.advanceForm.interestType.value = "";
		document.advanceForm.interestType.focus();
		return false;
  	}	
  	if(document.advanceForm.advanceType.value== interest_type && document.advanceForm.interestAmount.value==""){
  		alert('<bean:message key="alertInterestAmount"/>');
  		document.advanceForm.interestAmount.focus();
  		return false;
  	}	
  	if(document.advanceForm.advanceType.value== nonInterest_type && document.advanceForm.interestAmount.value!=""){ 
		alert('<bean:message key="alertNonInterestedAdvance"/>');
		document.advanceForm.interestAmount.value = "";
		document.advanceForm.interestAmount.focus();
		return false;
  	}		
	 
	if(document.advanceForm.advanceType.value== interest_type && document.advanceForm.maintainScheduleFlag.checked == false)
	{	
		alert('<bean:message key="alertMandateForInterestBearing"/>');
		return false;
	}
  	
  	if(document.advanceForm.numberOfInstallments.value==""){
  		alert('<bean:message key="alertNoOfInstallments"/>');
  		document.advanceForm.numberOfInstallments.focus();
  		return false;
  	}	
  	 
  	if(document.advanceForm.monthlyPayment.value==""){
  		alert('<bean:message key="alertMonthlyPayment"/>');
  		document.advanceForm.monthlyPayment.focus();
  		return false;
  	}  	
  	if(document.advanceForm.paymentMethod.disabled==false && document.advanceForm.paymentMethod.value==""){
  		alert('<bean:message key="alertPaymentMethod"/>');
  		document.advanceForm.paymentMethod.focus();
  		return false;
  	}	  
  	if(checkBankAccountForEmp() == "true" && document.advanceForm.paymentMethod.value==bankPayment){
  		alert('<bean:message key="alertBankA/CForEmployeeNotExist"/>');
  		document.advanceForm.paymentMethod.focus();
  		return false;
  	}  
	
	if(document.getElementById("maintainScheduleFlag").checked && !checkTotalOfPrincipalAndInterest())
	{
		return false;
	}

	<%		
	 if("Manual".equals(advanceWfType)){%>
	 	if(validateForMandatory() == "false"){
	 		return false;
	 	}
	 <%}%>
	
	enablePropertyOnSubmit();
	enableScheduleRelatedFields();
	
	
  }

  function enablePropertyOnSubmit(){
	document.advanceForm.advanceType.disabled=false;
  }	
  
  function disableRow(){
  	
  	document.advanceForm.maintainScheduleFlag.checked = false;
  	if(document.advanceForm.advanceType.value== nonInterest_type){
  		document.advanceForm.interestType.value ="";
		document.advanceForm.interestPct.value ="";
  		document.advanceForm.interestAmount.value ="";
  		document.advanceForm.interestType.disabled =true;
		document.advanceForm.interestPct.disabled =true;
  		document.advanceForm.interestAmount.disabled =true;
  		document.advanceForm.maintainScheduleFlag.disabled =false;
  	}
  	if(document.advanceForm.advanceType.value== interest_type){
  		document.advanceForm.interestType.disabled =false;
		document.advanceForm.interestPct.disabled =false;
  		document.advanceForm.interestAmount.disabled =false;
  		document.advanceForm.maintainScheduleFlag.disabled =false;
  	}
  }

  function callBankBranch(){	
  	document.getElementById("bankBranchValue").style.display = "none";
  	document.getElementById("bankBranchText").style.display = "none";
  	document.getElementById("bankAccountValue").style.display = "none";
	//document.getElementById("sanctionRowId").style.display = "none";
  	document.advanceForm.paymentMethod.disabled = false;
	document.advanceForm.maintainScheduleFlag.checked = false;
	document.getElementById("maintainSchdFlagLblMandatory").innerHTML = '';
	deleteRowWhileUnselectofSchFlag();
	document.getElementById("advanceScheduleContainer").style.display='none';
  	<c:forEach var="payheadObj" items="${salarycodes}">
		if(document.advanceForm.salarycode.value == "${payheadObj.head}"){
			document.advanceForm.advanceCategory.value =  "${payheadObj.categoryMaster.name}";
			
			<c:if test = "${payheadObj.interestAccount != null}">
				document.advanceForm.advanceType.value = interest_type;
				document.advanceForm.maintainScheduleFlag.disabled =false;
				document.getElementById("maintainSchdFlagLblMandatory").innerHTML = '<font color="red">*</font>';
			</c:if>
			<c:if test = "${payheadObj.interestAccount == null}">
				document.advanceForm.advanceType.value = nonInterest_type;
				document.advanceForm.maintainScheduleFlag.disabled =true;
				
			</c:if>
			if("${payheadObj.categoryMaster.name}" == "Deduction-BankLoan"){
				document.getElementById("bankBranchValue").style.display = "";
				document.getElementById("bankBranchText").style.display = "";
				//document.getElementById("sanctionRowId").style.display = "block";
				document.advanceForm.paymentMethod.disabled = true;
				var count = 1;
				document.advanceForm.bank.value = "${payheadObj.tdsId.bank.name}";
			}
		}	
  	</c:forEach>
	disableRow();
	document.advanceForm.advanceType.disabled=true;
	
	if(document.advanceForm.salarycode.value=="")
	{
		document.advanceForm.advanceType.value="";
		document.advanceForm.advanceType.disabled=false;
		document.advanceForm.maintainScheduleFlag.disabled = true;
	}

  }
  
  function callBankAccount(){  	
  	document.getElementById("bankAccountValue").style.display = "none";
  	for(var resLen=document.advanceForm.bankAccountId.length;resLen>1;resLen--){  		
		document.advanceForm.bankAccountId.options[resLen-1]=null;
	}
	var count = 1;
  	<c:forEach var="bankAccountObj" items="${bankAccounts}">
  		if(document.advanceForm.bankBranchId.value == "${bankAccountObj.bankbranch.id}"){
  			document.getElementById("bankAccountValue").style.display = "block";
			document.advanceForm.bankAccountId.options[count] = new 
			Option("${bankAccountObj.accountnumber}","${bankAccountObj.id}");	
			count=count+1;
  		}	
  	</c:forEach>  	
  } 
  		
  function setMaintainSchedule()
  {
		var installmentNo = document.advanceForm.numberOfInstallments.value;
	 		
		if(document.advanceForm.numberOfInstallments.value != "" 
		&& document.advanceForm.numberOfInstallments.value != 0 
		&& document.advanceForm.advAmount.value != ""  
		&& document.advanceForm.advAmount.value != 0 )
	 	{
			if(installmentNo>0){
				if(document.getElementById("maintainScheduleFlag").checked)
				{
					disableScheduleRelatedFields();
					document.getElementById("maintainSchedule").value="Y";
					
					document.getElementById("advanceScheduleContainer").style.display='inline';
					if(document.advanceForm.advanceType.value== interest_type)
					{
						for(var i=0;i<installmentNo*2;i++)
						{
							addRowWhileSelectofSchFlag(i);
						}
					}
					if(document.advanceForm.advanceType.value== nonInterest_type)
					{
						for(var i=0;i<installmentNo;i++)
						{
							addRowWhileSelectofSchFlag(i);
						}
					}
					document.getElementById("advanceScheduleContainer").style.display='inline';
				}
				else
				{
					if(confirm('<bean:message key="alertAdvanceSchRowsDelete"/>')){
						deleteRowWhileUnselectofSchFlag();
						document.getElementById("advanceScheduleContainer").style.display='none';
						document.getElementById("maintainSchedule").value="N";
						enableScheduleRelatedFields();
					}
					else
					{
						document.getElementById("maintainScheduleFlag").checked=true;
						document.getElementById("maintainSchedule").value="Y";
					}
				}
			}
	  	}
	  	else
	  	{
			document.getElementById("maintainScheduleFlag").checked=false;
			if(document.advanceForm.numberOfInstallments.value=="" || document.advanceForm.numberOfInstallments.value==0)
			{
	 			alert('<bean:message key="alertNoOfInstallments"/>');
	 			document.advanceForm.numberOfInstallments.focus();
	 			return false;
	 		}
	 		else if(document.advanceForm.advAmount.value == ""  || document.advanceForm.advAmount.value == 0)
			{
				alert('<bean:message key="alertAdvAmount"/>');
	 			document.advanceForm.advAmount.focus();
	 			return false;
			}
			else if(document.advanceForm.interestPct.value == ""  || document.advanceForm.interestPct.value == 0)
			{
				alert('<bean:message key="alertInterestPct"/>');
	 			document.advanceForm.interestPct.focus();
	 			return false;
			}
			else if(document.advanceForm.interestType.value == "" )
			{
				alert('<bean:message key="alertInterestType"/>');
	 			document.advanceForm.interestType.focus();
	 			return false;
			}
	  	}
	}
  	
	function addRowWhileSelectofSchFlag(whichRow)
	{
		var tbl=document.getElementById("advanceSchedule");
	 	var rows=tbl.rows;
	 	var tbody=tbl.tBodies[0];
	 	var rowObj = rows[1].cloneNode(true);
	 	var rows = 0;
		if(whichRow>0)
		{
			tbody.appendChild(rowObj);
	 		rows = parseInt(tbl.rows.length)-2;
		}
		
		var advAmount = 0;
		var prevPendAmount = 0;
	  	var pctAmount = 0;
	  	var interestAmount = 0;
	  	var totalAmount = 0;
	  	
	  	advAmount = eval(document.advanceForm.advAmount.value);	
	  	prevPendAmount = eval(document.advanceForm.pendingPrevAmt.value);
	  	pctAmount = eval(document.advanceForm.interestPct.value);
	  	
	  	var noOfInstallment = eval(document.advanceForm.numberOfInstallments.value);
		
		document.getElementsByName('installmentNo')[rows].value=rows+1;
		
		if(whichRow<noOfInstallment)
		{
			//Calculation for principal installment amount
			var principalInstAmount = (advAmount+prevPendAmount)/noOfInstallment;
			document.getElementsByName('principalInstAmount')[rows].value=roundNumber(principalInstAmount,2);
		  
			document.getElementsByName('interestInstAmount')[rows].value=0;
		}
		else
		{
			document.getElementsByName('principalInstAmount')[rows].value=0;
			
			//Calculation for interest installement amount.
			var interestInstAmount = (advAmount+prevPendAmount)*pctAmount/(100*12);  
			document.getElementsByName('interestInstAmount')[rows].value=roundNumber(interestInstAmount,2);
		}
	}	
	
		
	function roundNumber(num, dec) {
		var result = Math.round(num*Math.pow(10,dec))/Math.pow(10,dec);
		return result;
	}

 	function deleteRowWhileUnselectofSchFlag()
	{
		 var tbl = document.getElementById('advanceSchedule');
		 var lastRow = (tbl.rows.length-1);
		 for(var i=lastRow;i>1;i--)
			 tbl.deleteRow(i);
		 return true;
	}

	
	function addRow()
	{
		 var tbl=document.getElementById("advanceSchedule");
  		 var rows=tbl.rows;
  		 var tbody=tbl.tBodies[0];
  		 var rowObj = rows[1].cloneNode(true);
  		 tbody.appendChild(rowObj);
  			
		 var rows = parseInt(tbl.rows.length)-2;
		 document.getElementsByName('installmentNo')[rows].value=rows+1;
		 document.getElementsByName('principalInstAmount')[rows].value=0;
		 document.getElementsByName('interestInstAmount')[rows].value=0;
	 }
	 
	 function deleteRow()
	 {
		  var tbl = document.getElementById('advanceSchedule');
		  var lastRow = (tbl.rows.length)-1;
 		  if(lastRow ==1)
		  {
			 alert("This row can not be deleted");
			 return false;
		  }
		  else
		  {
			 tbl.deleteRow(lastRow);
			 return true;
		  }
	 }
	 
	function enableScheduleRelatedFields()
	{
		document.advanceForm.salarycode.disabled=false;
		document.advanceForm.numberOfInstallments.disabled=false;
		document.advanceForm.advAmount.disabled=false;
		document.advanceForm.interestPct.disabled=false;
		document.advanceForm.interestType.disabled=false;
		document.advanceForm.total.disabled=false;
		document.advanceForm.interestAmount.disabled=false;
		document.advanceForm.monthlyPayment.disabled=false;
		document.advanceForm.pendingPrevAmt.disabled=false;
	}

	function disableScheduleRelatedFields()
	{
		document.advanceForm.salarycode.disabled=true;
		document.advanceForm.numberOfInstallments.disabled=true;
		document.advanceForm.advAmount.disabled=true;
		document.advanceForm.interestPct.disabled=true;
		document.advanceForm.interestType.disabled=true;
		document.advanceForm.total.disabled=true;
		document.advanceForm.interestAmount.disabled=true;
		document.advanceForm.monthlyPayment.disabled=true;
		document.advanceForm.pendingPrevAmt.disabled=true;
	}

	function checkTotalOfPrincipalAndInterest()
	{
		if(document.advanceForm.pendingPrevAmt.value=='')
			document.advanceForm.pendingPrevAmt.value='0';
		var totPrincipalAndInterestInst =0;
		var totPrincipal =0;
		var tbl=document.getElementById("advanceSchedule");
		var rows = parseInt(tbl.rows.length)-1;

		for(var i=0;i<rows;i++)
		{
			var curPrincipalAmt =0;
			var curInterestAmt =0;
			
			if(document.getElementsByName('principalInstAmount')[i].value=="")
				document.getElementsByName('principalInstAmount')[i].value =0;
				
			if(document.getElementsByName('interestInstAmount')[i].value=="")
				document.getElementsByName('interestInstAmount')[i].value =0;
			
			curPrincipalAmt =eval(document.getElementsByName('principalInstAmount')[i].value);
			curInterestAmt =eval(document.getElementsByName('interestInstAmount')[i].value);
				
			totPrincipal = 	eval(totPrincipal + curPrincipalAmt );
			totPrincipalAndInterestInst =eval(totPrincipalAndInterestInst + curPrincipalAmt +curInterestAmt );
		}
		var totAmt =eval(document.advanceForm.total.value);
		var advAmt =eval(document.advanceForm.advAmount.value);
		var prevPendAmount = eval(document.advanceForm.pendingPrevAmt.value);
		if(Math.round(totPrincipal)!=(advAmt+prevPendAmount))
		{
			alert("Total of all principal schedule amounts("+Math.round(totPrincipal)+") should tally with the total amount("+(advAmt+prevPendAmount)+")");
			return false;
		}
		
		if(Math.round(totPrincipalAndInterestInst)!=totAmt)
		{
			alert("Total of all principal and interest schedule amounts("+Math.round(totPrincipalAndInterestInst)+") should tally with the total amount("+totAmt+")");
			return false;
		}
		return true;
		

	}
	
	var empCodeSelectionHandler = function(sType, arguments)
    { 
        var oData = arguments[2];
	 	var empDetails = oData[0];
	 	var empCode = empDetails.split(EMPCODE_SEP)[0];
	 	var empName = empDetails.split(EMPCODE_SEP)[1];
	 	dom.get("employeeCodeId").value = oData[1];	 	
	 	dom.get("employeeCode").value = empCode;
	 	dom.get("checkEmpCode").value = empCode;
	 	dom.get("employeeName").value = empName;	 	
	 	employeeName=empName;
	 	empcode=empCode;
 	}
    var empCodeSelectionEnforceHandler = function(sType, arguments) {
      		warn('improperEmpCodeSelection');
  	}

   	function paramsFunction()
   	{ return "type=AllEmployeeCodes";
   	}
   	
   	function getPrevPendingAmt(){ 
   	   	var salcode =  document.getElementsByName("salarycode")[0].value;
    	if(document.getElementById("employeeCodeId").value!='' && salcode!=''){
    		getPrevPendingAmtValue();
    	}
    	else{
	    	document.getElementById("pendingPrevAmt").value='';
    	}
    	
    }
   function getPrevPendingAmtValue(){
   		var myCodeSuccessHandler = function(req,res) {
   			document.getElementById("pendingPrevAmt").value=Math.round(eval(res.results[0].Amount));
   		};
            
	    var myCodeFailureHandler = function() {
	    	document.getElementById("pendingPrevAmt").value='';
	        alert("Unable to get Previous Pending Amount");
	    };
	makeJSONCall(["Amount"],'${pageContext.request.contextPath}/advance/ajaxAdvance!getPendingPreviousAmt.action',{employeeCodeId:document.getElementById("employeeCodeId").value,salaryCode:document.getElementsByName("salarycode")[0].value},myCodeSuccessHandler,myCodeFailureHandler) ;
	        
   }
	
</script>
</head>

<body onLoad="onBodyLoad()" >
<html:form method="POST" action="/salaryadvance/afterSalaryadvance" >



	<input type="hidden" name="checkEmpCode" id="checkEmpCode"/>
	<input type="hidden" name = "advanceCategory" />
	<table  width="95%" cellpadding ="0" cellspacing ="0" border = "0" id="employee">
		<tr>
		<td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
                  <div class="headplacer">Generate Advance </div></td>
	</tr>
		
	
	    <tr>
	 	 <html:hidden property="employeeCodeId" styleId="employeeCodeId" />
	 	 <c:choose>
		     <c:when test="${(empty ess) and (empty param.ess)}">
	  		<td class="whiteboxwk"><span class="mandatory">*</span><bean:message key="EmployeeCode"/></td>
	  		<td  class="whitebox2wk" valign="top">
  		<div class="yui-skin-sam" >
	    	<div id="empSearch_autocomplete">
	    		<div>
	    	    <html:text styleClass="selectwk" property="employeeCode" styleId ="employeeCode" onblur="getPrevPendingAmt()" /> 	    
	    		</div>
	   	    <span id="empCodeSearchResults"></span>
	    	</div>
		</div>		    	
		   	    <egovtags:autocomplete name="employeeCode"  field="employeeCode" 
		   	    	url="${pageContext.request.contextPath}/common/employeeSearch!getActiveEmpListByEmpCodeLike.action" queryQuestionMark="true" paramsFunction="paramsFunction" results="empCodeSearchResults" 
		   	    	handler="empCodeSelectionHandler" forceSelectionHandler="empCodeSelectionEnforceHandler"/>
		   	    <span class='warning' id="improperempCodeSelectionWarning"></span>
  		</td>
  		</c:when>
  		<c:otherwise>
  			<td class="whiteboxwk"><bean:message key="EmployeeCode"/></td>
  			<td  class="whitebox2wk" valign="top"><html:text styleClass="selectwk" property="employeeCode" styleId="employeeCode" readonly="true"/></td>
  		</c:otherwise>
  		</c:choose>
  		
		  	<td class="whiteboxwk"><bean:message key="EmployeeName"/></td>
		  	<td class="whitebox2wk"><html:text styleClass="selectwk" property="employeeName" styleId="employeeName" readonly="true"/></td>
	   
	   </tr>
	  	<tr>
	  		<td><div id="codescontainer"></div></td>
	    </tr>  
	   
		<tr>	 	 
	  		<td class="greyboxwk"><span class="mandatory">*</span><bean:message key="Advance"/></td>
	  		<td class="greybox2wk">
	  		<html:select styleClass="selectwk" property="salarycode" onchange="callBankBranch();getPrevPendingAmt();">
	  		<html:option value=""><bean:message key="select"/> </html:option>
			   <c:forEach var="salcodeObj" items="${salarycodes}" >
	   	 		<html:option value="${salcodeObj.head}">${salcodeObj.head}</html:option>
			   </c:forEach>
			</html:select>
	  		</td>
	  		<td class="greyboxwk"><bean:message key="AdvanceType"/></td>
	  		<td class="greybox2wk">
	  		<html:select styleClass="selectwk" property="advanceType" onchange="disableRow();">
	  		<html:option value=""><bean:message key="select"/></html:option>
	   	 		<html:option value="interest">Interest Bearing</html:option>
	   	 		<html:option value="nonInterest">Non-Interest Bearing</html:option>	   	 		
			</html:select>
	  		</td>
	    </tr>
		<tr>
			<td class="whiteboxwk"><bean:message key="PrevPendingAmt"/></td>
			<td class="whitebox2wk">
				<input type="text"  class="selectwk" id="pendingPrevAmt" name="pendingPrevAmt"
				onchange="return checkdecimalval(this,this.value)"  
			 	onblur="trim(this,this.value);populateCalculatedAmount();"  />
			</td>
			<td class="whiteboxwk"><bean:message key="Interest%"/> </td>
			<td class="whitebox2wk">
				<input type="text"  class="selectwk" name="interestPct" 
				onblur="checkForPct(this);populateCalculatedAmount();trim(this,this.value);"/>
			</td>
		</tr>	
		<tr>
			<td class="greyboxwk"><span class="mandatory">*</span><bean:message key="AdvanceAmount"/></td>
			<td class="greybox2wk">
			<input type="text"  class="selectwk" name="advAmount"  
			 onchange="return checkdecimalval(this,this.value)" 
			 onblur="trim(this,this.value);populateCalculatedAmount();" />
			</td>
			<td class="greyboxwk"><bean:message key="InterestType"/> </td>
			<td class="greybox2wk">
				<html:select property="interestType" styleClass="selectwk" onchange="populateCalculatedAmount();">
				<html:option value="">-----------Select-----------</html:option>
				<html:option value="simple">Simple</html:option>
				<!-- <html:option value="reducingBalance">reducing Balance</html:option>	-->
				</html:select>
			</td>
		</tr>	
		<tr>
			<td class="whiteboxwk"></td>
			<td class="whitebox2wk"></td>
			<td class="whiteboxwk"><span class="mandatory">*</span><bean:message key="NumberOfInstallment"/> </td>
			<td class="whitebox2wk">
				<input type="text"  class="selectwk" name="numberOfInstallments" maxlength="3"
				onchange="return checkdecimalval(this,this.value)"
				onblur="trim(this,this.value);populateCalculatedAmount();" />
			</td>
		</tr>	
		<tr>
			<td class="greyboxwk"><bean:message key="InterestAmount"/> </td>
			<td class="greybox2wk">
				<input type="text"  class="selectwk" name="interestAmount" 
					onchange="return checkdecimalval(this,this.value)" 
					onblur="trim(this,this.value);populateTotalAndMonthlyPayment();" />
			</td>
			<td class="greyboxwk" id="bankBranchText" style="display: none;"><bean:message key="Bank"/></td>
			<td class="greybox2wk" id="bankBranchValue" style="display: none;">
				<input type="text" name="bank" id="BANK" readonly="readonly">			
			</td>	
		</tr>			
		<tr>
			<td class="whiteboxwk"><span class="mandatory">*</span><bean:message key="TotalAmount"/></td>
			<td class="whitebox2wk"><input type="text"  class="fieldcell" name="total" readonly="readonly" /></td>
			<td class="whiteboxwk" id="bankAccountText" ></td>
			<td class="whitebox2wk" id="bankAccountValue" >
				
			</td>			
		</tr>	
			
		<tr>
			<td class="greyboxwk"><span class="mandatory">*</span><bean:message key="MonthlyPayment"/></td>
			<td class="greybox2wk">
				<input type="text"  class="selectwk" name="monthlyPayment" onblur="trim(this,this.value);"
				onchange="return checkdecimalval(this,this.value)" />
			</td>	
			<td class="greyboxwk"><span class="mandatory">*</span><bean:message key="PaymentMethod"/></td>
			<td class="greybox2wk">
			<html:select property="paymentMethod" styleClass="selectwk">
				<html:option value="">-----------Select-----------</html:option>
				<html:option value="cheque">Cheque</html:option>
				<html:option value="cash">Cash</html:option>
				<html:option value="dbt">Direct Bank Transfer</html:option>
			</html:select>
			</td>			
		</tr>	
		<!--tr id="sanctionRowId" style="display: none;" >
			<td class="labelcell"><bean:message key="SanctionNo"/><font color="red">*</font> </td>
			<td class="labelcell"><input type="text"  class="fieldcell" name="sanctionNo" /></td>			
			<td class="labelcell"><bean:message key="Sanction/RejectionDate"/> <font color="red">*</font></td>
			<td class="labelcell">
				<input type="text" class="fieldcell" name="sanctionDate" value="${sanctionDate}" onblur="validateDateFormat(this);checkFdateTdate(this.value,'20/12/2007')"/>
				<a href="javascript:show_calendar('advanceForm.sanctionDate');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img id="img1" src="../img/show-calendar.gif" width="15" height="15" border="0"></a>
			</td>						
		</tr-->
		<tr>
		    <td class="whiteboxwk"><bean:message key="MaintainSchedule"/></td>
			<td class="whitebox2wk"><html:hidden property="maintainSchedule" styleId="maintainSchedule" value="N" />
			<span id="maintainSchdFlagLblMandatory"></span><input type="checkbox" id="maintainScheduleFlag" onclick="setMaintainSchedule();"/></td>
		</tr>	
  </table>	
  
  <div class="tbl-header2" style="height:254px;display: none;overflow-y: auto;" id="advanceScheduleContainer">
	<center>
	<table  width="98%" border="0" cellpadding="0" cellspacing="0" id="dtlHeading">
		<tr>
			<td colspan="4" class="headingwk" >
			
			<div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
                  <div class="headplacer">Advance Installment Schedule</div>
				  </td>
        </tr>
	</table>
	</center>
	<table width="98%" border="0"  cellpadding="0" cellspacing="0" id="advanceSchedule" name="advanceSchedule">
       <tr class="shadowwk">
			<td width="20%" class="tablesubheadwk"><bean:message key="AdvSchInstallmentNo"/></td>
			<td width="20%" class="tablesubheadwk"><bean:message key="AdvSchPrincipalAmt"/></td>
			<td width="20%" class="tablesubheadwk"><bean:message key="AdvSchInterestAmt"/></td>
       </tr>
  				
  	   <tr id="row">
			<td class="whitebox3wk" ><input  name="installmentNo" class="selectwk" id="installmentNo" value="1"  readonly="true"></td>
			<td class="whitebox3wk"><input name="principalInstAmount" class="selectwk" id="principalInstAmount" onblur="checkNumber(this)" ></td>
			<td class="whitebox3wk"><input  name="interestInstAmount" class="selectwk" id="interestInstAmount"  onblur="checkNumber(this)" ></td>
		</tr>
	</table>
	<table id="tbl_buttons" align="left">
        <tr>
			<td ><a href="#"><img src="<%=request.getContextPath()%>/common/image/add.png" alt="Add" width="16" height="16" border="0" id="addDetail" name="addDetail" onclick="addRow()" /></a></td>
			<td >
			<a href="#"><img src="<%=request.getContextPath()%>/common/image/cancel.png" alt="Delete" width="16" height="16" border="0" id="deleteDetail" name="deleteDetail" onclick="deleteRow()" /></a></td>
       </tr>
	</table>
  </div>

	<%		
		if("Manual".equals(advanceWfType)){
	%>
			<%@ include file='../payslip/manualWfApproverSelection.jsp'%>	
	<%
		}
	%>
			<table  width="98%">
				<tr>
                <td><div align="right" class="mandatory">* Mandatory Fields</div></td>
              </tr>
			</table>	
		
<div class="buttonholderwk">
	<html:submit property="action" value="Save"styleClass="buttonfinal"  onclick="return checkonSubmit();"/>
	<c:if test="${(empty ess) && (empty param.ess)}">
	<html:submit property="action" value="Save and New" onclick="return checkonSubmit();" styleClass="buttonfinal"/>
	</c:if>
	<input type="button" value="Cancel" onclick="history.go(0)" class="buttonfinal"/>
	<input type="button" name="button" id="button" value="CLOSE"  class="buttonfinal" onclick="window.close()"/>
</div>

</html:form>
</body>

</html>
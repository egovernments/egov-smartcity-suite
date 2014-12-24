<%@ include file="/includes/taglibs.jsp" %>
<%@ page import="org.egov.payroll.utils.PayrollConstants,java.util.*" %>
<html>
<head>
	<title>Salary advance sanction</title>    
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
	
%>
<script language="JavaScript"  type="text/JavaScript">
	
	var interest_type = "<%= PayrollConstants.SAL_ADV_TYPE_INTEREST %>";
	var nonInterest_type = "<%= PayrollConstants.SAL_ADV_TYPE_NONINTEREST %>";
	var simpleInterest = "<%= PayrollConstants.SAL_ADV_INTEREST_TYPE_SIMPLE %>";	
	var reducingInterest = "<%= PayrollConstants.SAL_ADV_INTEREST_TYPE_REDUCING %>";
	var chequePayment = "<%= PayrollConstants.SAL_ADV_PAYMENT_TYPE_CHEQUE %>";
	var cashPayment = "<%= PayrollConstants.SAL_ADV_PAYMENT_TYPE_CASH %>";
	var bankPayment = "<%= PayrollConstants.SAL_ADV_PAYMENT_TYPE_DIRECT_BANK %>";
	
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
	
  function checkonSubmit(obj){     
  	
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
  	if(document.advanceForm.paymentMethod.value==""){
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
	
	if(eval(document.advanceForm.advAmount.value) > eval(document.advanceForm.requestAmnt.value)){
  		alert('<bean:message key="SanctionAmountNotGreaterRequestAmount"/>');
  		document.advanceForm.advAmount.focus();
  		return false;
  	}
  	if(document.advanceForm.sanctionDate.value==""){
  		alert('<bean:message key="alertSanctionDate"/>');
  		document.advanceForm.sanctionDate.focus();
  		return false;  		
  	}
  	if(document.advanceForm.sanctionDate.value != ""){	
	//	alert("${sanctionDate}");
  		if(compareDate(document.advanceForm.sanctionDate.value,"${sanctionDate}") == 1){
  			alert('<bean:message key="alertSanctionDateNnotLesserCurrentDate"/>');
  			document.advanceForm.sanctionDate.focus();
	  		return false;
  		}  		
  	}
	
	if("reject" != obj){ 
		if(document.advanceForm.sanctionNo.value==""){
	  		alert('<bean:message key="alertSanctionNo"/>');
	  		document.advanceForm.sanctionNo.focus();
	  		return false;
	  	}
	  	if(uniqueCheckSanctionNo(document.advanceForm.sanctionNo.value)=="false"){
	  		alert('<bean:message key="alertSanctionNoEexists"/>');
	  		document.advanceForm.sanctionNo.focus();
	  		return false;
	  	}
	} 		
  	if(obj != "reject"){
  		alert('<bean:message key="alertAdvanceSanctionedSuccessfully"/>');
  	}
  	if(obj == "reject"){
  		alert('<bean:message key="alertAdvanceRejectedSuccessfully"/>');
  	}
	
	enableScheduleRelatedFields();     
	enablePropertyOnSubmit();
  	     	
  }

   function enablePropertyOnSubmit(){
	document.advanceForm.advanceType.disabled=false;
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
	  		document.advanceForm.total.value = Math.round(advAmt);
	  		if(document.advanceForm.numberOfInstallments.value != ""){
		  		var noOfInstallment = eval(document.advanceForm.numberOfInstallments.value);
	  			var monthlyPayment = eval(document.advanceForm.total.value)/noOfInstallment
	  			document.advanceForm.monthlyPayment.value = Math.round(monthlyPayment); 
	  		}	  		
	  	}	
  	}
  }
  
  function populateMonthlyPayment(){
  	if(document.advanceForm.numberOfInstallments.value != "" && document.advanceForm.advAmount.value != "" && 
  																document.advanceForm.interestPct.value != "" &&
  																document.advanceForm.interestType.value != ""){
  		var advAmount = 0;
	  	var pctAmount = 0;
	  	var interestAmount = 0;
	  	var totalAmount = 0;
	  	advAmount = eval(document.advanceForm.advAmount.value);	
	  	pctAmount = eval(document.advanceForm.interestPct.value);
	  	var noOfInstallment = eval(document.advanceForm.numberOfInstallments.value);
	  	if(document.advanceForm.advAmount.value != "" && document.advanceForm.interestPct.value != ""){
		  	if(document.advanceForm.interestType.value == simpleInterest){
			  	interestAmount = advAmount*pctAmount*noOfInstallment/(100*12);  	
			  	totalAmount= totalAmount+ advAmount + interestAmount;  
			  	document.advanceForm.interestAmount.value= Math.round(interestAmount);
			  	document.advanceForm.total.value = Math.round(totalAmount);
		  	}  	
	  	}
	  	var monthlyPayment = eval(document.advanceForm.total.value)/noOfInstallment;
	  	document.advanceForm.monthlyPayment.value = Math.round(monthlyPayment); 
  	}
  }
    
  function initiateRequest() {
	if (window.XMLHttpRequest) {
		return new XMLHttpRequest();
	} else if (window.ActiveXObject) {
		isIE = true;
		return new ActiveXObject("Microsoft.XMLHTTP");
	}
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
  
  function populateAdvances(){    
     	for(var resLen=1;resLen<document.advanceForm.salarycode.length;resLen++)
		{
	      document.advanceForm.salarycode.options[resLen]=null;
     	}
		var empId = document.getElementById("employeeCodeId").value;		
		var count = 1;	
	  	<c:forEach var="salAdvancesObj" items="${salaryadvances}">	 
      	if(empId == "${salAdvancesObj.employee.idPersonalInformation}"){			
    		document.advanceForm.salarycode.options[count]=new Option("${salAdvancesObj.salaryCodes.head}","${salAdvancesObj.id}");
			count=count+1;			
      	} 	 	
     	</c:forEach>
    }
  
  function fillAllFild(obj){
//    alert(obj.value);
    <c:forEach var="salAdvancesObj" items="${salaryadvances}">	 
    	if(obj.value == "${salAdvancesObj.id}"){
		 	document.advanceForm.advAmount.value = "${salAdvancesObj.advanceAmt}";
		 	document.advanceForm.interestPct.value = "${salAdvancesObj.interestPct}";
		 	document.advanceForm.interestType.value = "${salAdvancesObj.interestType}";
		 	document.advanceForm.numberOfInstallments.value = "${salAdvancesObj.numOfInst}";
		 	document.advanceForm.interestAmount.value = "${salAdvancesObj.interestAmt}";
		 	document.advanceForm.total.value = "${salAdvancesObj.pendingAmt}";
		 	document.advanceForm.monthlyPayment.value = "${salAdvancesObj.instAmt}";
		} 	
	</c:forEach>	 	
  }   
  
  function populateTotalAndMonthlyPayment(){
  	var interestAmt = eval(document.advanceForm.interestAmount.value);
  	var noOfInstallment = eval(document.advanceForm.numberOfInstallments.value);
  	var advAmt = eval(document.advanceForm.advAmount.value);
  	var totalAmt = interestAmt + advAmt;
	document.advanceForm.total.value = Math.round(totalAmt);
	var monthlyPayment = totalAmt/noOfInstallment;
	if(document.advanceForm.numberOfInstallments.value != "")
		document.advanceForm.monthlyPayment.value = Math.round(monthlyPayment);	
  }
  
  function disableRow(){
  	if(document.advanceForm.advanceType.value== nonInterest_type){  		
  		document.advanceForm.interestType.value ="";
		document.advanceForm.interestPct.value ="";
  		document.advanceForm.interestAmount.value ="";
  		document.advanceForm.interestType.disabled =true;
		document.advanceForm.interestPct.disabled =true;
  		document.advanceForm.interestAmount.disabled =true;
		document.advanceForm.maintainSchedule.value=="N";
    	document.advanceForm.maintainScheduleFlag.disabled =true;

  	}
  	if(document.advanceForm.advanceType.value==interest_type){  		
  		document.advanceForm.interestType.disabled =false;
		document.advanceForm.interestPct.disabled =false;
  		document.advanceForm.interestAmount.disabled =false;
  	}
  }
  
  function populateFild(){
	if(document.getElementById("maintainSchedule").value=="Y")
	{
		//disableScheduleRelatedFields();
		document.getElementById("maintainSchdFlagLblMandatory").innerHTML = '<font color="red">*</font>';
		document.getElementById("maintainScheduleFlag").checked=true;
		document.getElementById("advanceScheduleContainer").style.display='inline';
    }

  	disableRow();
	document.advanceForm.advanceType.disabled=true;
  }
 		
 function setMaintainSchedule()
   {
		var installmentNo = document.advanceForm.numberOfInstallments.value;
	 		
		if(document.advanceForm.numberOfInstallments.value != "" && document.advanceForm.numberOfInstallments.value != 0 && 
			document.advanceForm.advAmount.value != ""  && document.advanceForm.advAmount.value != 0 && 
	 		document.advanceForm.interestPct.value != "" && document.advanceForm.interestPct.value != 0 &&
	 																document.advanceForm.interestType.value != ""){
			if(installmentNo>0){
				if(document.getElementById("maintainScheduleFlag").checked)
				{
					disableScheduleRelatedFields();
					document.getElementById("maintainSchedule").value="Y";
					
					document.getElementById("advanceScheduleContainer").style.display='inline';
					for(var i=0;i<installmentNo*2;i++)
					{
						addRowWhileSelectofSchFlag(i);
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
	  	var pctAmount = 0;
	  	var interestAmount = 0;
	  	var totalAmount = 0;
	  	
	  	advAmount = eval(document.advanceForm.advAmount.value);	
	  	pctAmount = eval(document.advanceForm.interestPct.value);
	  	
	  	var noOfInstallment = eval(document.advanceForm.numberOfInstallments.value);
		
		document.getElementsByName('installmentNo')[rows].value=rows+1;
		
		if(whichRow<noOfInstallment)
		{
			//Calculation for principal installment amount
			var principalInstAmount = advAmount/noOfInstallment;
			document.getElementsByName('principalInstAmount')[rows].value=roundNumber(principalInstAmount,2);
		  
			document.getElementsByName('interestInstAmount')[rows].value=0;
		}
		else
		{
			document.getElementsByName('principalInstAmount')[rows].value=0;
			
			//Calculation for interest installement amount.
			var interestInstAmount = advAmount*pctAmount/(100*12);  
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
		 document.getElementsByName('recover')[rows].value='';
		 document.getElementsByName('payslip')[rows].value='';

		 if(document.getElementsByName('principalInstAmount')[rows].readOnly)
		 	document.getElementsByName('principalInstAmount')[rows].readOnly=false;
		 if(document.getElementsByName('interestInstAmount')[rows].readOnly)
		 	document.getElementsByName('interestInstAmount')[rows].readOnly=false;
	 }
	 
	 function deleteRow()
	 {
		  var tbl = document.getElementById('advanceSchedule');
		  var lastRow = (tbl.rows.length)-1;
 		  
 		  if(document.getElementsByName('recover')[lastRow-1].value=='Y'  && document.getElementsByName('payslipId')[lastRow-1].value!='')
 		  {
	 		 alert("This row cannot be deleted, since payslip already generated");
	 		 return false;
		  }
		  else if(lastRow ==1)
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
	}

	function checkTotalOfPrincipalAndInterest()
	{
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
		
		if(Math.round(totPrincipal)!=advAmt)
		{
			alert("Total of all principal schedule amounts("+Math.round(totPrincipal)+") should tally with the advance amount("+advAmt+")");
			return false;
		}
		
		if(Math.round(totPrincipalAndInterestInst)!=totAmt)
		{
			alert("Total of all principal and interest schedule amounts("+Math.round(totPrincipalAndInterestInst)+") should tally with the total amount("+totAmt+")");
			return false;
		}
		return true;
		

	}	
</script>
</head>

<c:set var="payslipMonAndYearFieldValue" value ="" scope="page" />

<body onload="populateFild();">
<html:form  action="/salaryadvance/afterSanction">

	<html:hidden name="advanceForm" property="salaryadvanceId" value="${salaryadvance.id}"/>
	<input type="hidden" name="employeeCodeId" id="employeeCodeId" value="${salaryadvance.employee.idPersonalInformation}" />
	

	<table  width="95%" cellpadding ="0" cellspacing ="0" border = "0" id="employee">
		
	    <tr>
		<td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
                  <div class="headplacer">Sanction Advance</div></td>
	</tr>
		
	
	    <tr>
	  		<td class="whiteboxwk"><bean:message key="EmployeeCode"/></td>
	  		<td class="whitebox2wk">  			  		
  			<input type="text" name="employeeCode" class="selectwk" id="employeeCode" value="${salaryadvance.employee.employeeCode}" readonly="readonly"/>
  			</td>
		  	<td class="whiteboxwk"><bean:message key="EmployeeName"/></td>
		  	<td class="whitebox2wk"><input type="text" class="selectwk" name="employeeName" value="${salaryadvance.employee.employeeName}" id ="employeeName" readonly="readonly" /></td>
	    </tr>
	  	<tr>
	  		<td><div id="codescontainer"></div></td>
	    </tr>  
	    
	</table> 	
	<table width="95%" cellpadding ="0" cellspacing ="0" border = "0" id="advance">
		<tr>	 	 
	  		<td class="greyboxwk"><bean:message key="Advance"/></td>
	  		<td class="greybox2wk">
	  		<input type="text" name="salarycode" class="selectwk" value="${salaryadvance.salaryCodes.head}" readonly="readonly" />
	  		</td>
	  		<td class="greyboxwk"><bean:message key="AdvanceType"/>  </td>
	  		<td class="greybox2wk">
	  		<html:select styleClass="selectwk" property="advanceType" value="${salaryadvance.advanceType}" onchange="disableRow();" >
	  		<html:option value="">-----------Select-----------</html:option>
	   	 		<html:option value="interest">Interest Bearing</html:option>
	   	 		<html:option value="nonInterest">Non-Interest Bearing</html:option>	   	 		
			</html:select>
	  		</td>
	    </tr>
		<tr>
			<td class="whiteboxwk"><span class="mandatory">*</span><bean:message key="RequestAmount"/> </td>
			<td class="whitebox2wk">
			<input type="text"  class="selectwk" name="requestAmnt" value="${salaryadvance.advanceAmt}" 
			onchange="return checkdecimalval(this,this.value)" 
			onblur="trim(this,this.value);populateCalculatedAmount();populateMonthlyPayment();" readonly="readonly"/>
			</td>
			<td class="whiteboxwk"><bean:message key="Interest%"/> </td>
			<td class="whitebox2wk">
				<input type="text"  class="selectwk" name="interestPct" value="${salaryadvance.interestPct}" 
				onblur="trim(this,this.value); checkForPct(this); populateCalculatedAmount();populateMonthlyPayment();" />
			</td>
		</tr>	
		<tr>
			<td class="greyboxwk"><span class="mandatory">*</span><bean:message key="SanctionAmount"/></td>
			<td class="greybox2wk">
			<input type="text"  class="selectwk" name="advAmount"  style="selectwk"  value="${salaryadvance.advanceAmt}" 
			onchange="return checkdecimalval(this,this.value)" 
			onblur="trim(this,this.value);populateCalculatedAmount();populateMonthlyPayment();"/>
			</td>
			<td class="greyboxwk"><bean:message key="InterestType"/>  </td>
			<td class="greybox2wk">
				<html:select property="interestType" styleClass="selectwk" value="${salaryadvance.interestType}" onchange="populateCalculatedAmount();populateMonthlyPayment();">
				<html:option value="">-----------Select-----------</html:option>
				<html:option value="simple">Simple</html:option>
				<html:option value="reducingBalance">Reducing Balance</html:option>
				</html:select>
			</td>
		</tr>	
		<tr>
			<td class="whiteboxwk"></td>
			<td class="whitebox2wk"></td>
			<td class="whiteboxwk"><span class="mandatory">*</span><bean:message key="NumberOfInstallment"/> </td>
			<td class="whitebox2wk">
				<input type="text"  class="selectwk" name="numberOfInstallments" value="${salaryadvance.numOfInst}" maxlength="3" 
				onchange="return checkdecimalval(this,this.value)" 
				onblur="trim(this,this.value);populateMonthlyPayment();" />
			</td>
		</tr>	
		<tr>
			<td class="greyboxwk"><bean:message key="InterestAmount"/> </td>
			<td class="greybox2wk" colspan="6">
				<input type="text"  class="selectwk" name="interestAmount" value="${salaryadvance.interestAmt}"
				 onblur="populateTotalAndMonthlyPayment();" />
			</td>			
		</tr>			
		<tr>
			<td class="whiteboxwk"><span class="mandatory">*</span><bean:message key="TotalAmount"/></td>
			<td class="whitebox2wk">
			<input type="text"  class="selectwk" name="total" value="${salaryadvance.interestAmt + salaryadvance.advanceAmt}" readonly="readonly"/>
			</td>			
		</tr>	
			
		<tr>
			<td class="greyboxwk"><span class="mandatory">*</span><bean:message key="MonthlyPayment"/> </td>
			<td class="greybox2wk">
			<input type="text"  class="selectwk" name="monthlyPayment" value="${salaryadvance.instAmt}"/>
			</td>	
			<td class="greyboxwk"><span class="mandatory">*</span><bean:message key="PaymentMethod"/> </td>
			<td class="greybox2wk">
			<html:select property="paymentMethod" styleClass="selectwk" value="${salaryadvance.paymentType}" >
				<html:option value="">-----------Select-----------</html:option>
				<html:option value="cheque">Cheque</html:option>
				<html:option value="cash">Cash</html:option>
				<html:option value="dbt">Direct Bank Transfer</html:option>
			</html:select>
			</td>		
		</tr>
		
		<tr>
		<td class="whiteboxwk"><bean:message key="MaintainSchedule"/>
			<td class="whitebox2wk"><html:hidden property="maintainSchedule" styleId="maintainSchedule" value="${salaryadvance.maintainSchedule}" />
			<span id="maintainSchdFlagLblMandatory"></span><input type="checkbox" id="maintainScheduleFlag" onclick="setMaintainSchedule();"/></td>
		</tr>
	</table>	
	
	<div class="tbl-header2" style="height:254px;display:none;overflow-y: auto;" id="advanceScheduleContainer">
    
		<c:if test="${salaryadvance.maintainSchedule!=null && salaryadvance.maintainSchedule=='Y'}" >
	
			<center>
			<table width="98%" border=0 cellpadding="0" cellspacing="0" id="dtlHeading">
				<tr class="tableheader">
					<td colspan="4" class="headingwk" >
			
					<div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
               	   <div class="headplacer">Advance Installment Schedule</div>
				  </td>
		        </tr>
		        
		        
        
			</table>
			</center>
			<table  width="98%" border="0" cellpadding="0" cellspacing="0" id="advanceSchedule" name="advanceSchedule">
		       <tr class="shadowwk">
					<td width="20%" class="tablesubheadwk"><bean:message key="AdvSchInstallmentNo"/></td>
					<td width="20%" class="tablesubheadwk"><bean:message key="AdvSchPrincipalAmt"/></td>
					<td width="20%" class="tablesubheadwk"><bean:message key="AdvSchInterestAmt"/></td>
					<td width="20%" class="tablesubheadwk"><bean:message key="AdvSchRecover"/></td>
					<td width="20%" class="tablesubheadwk"><bean:message key="AdvSchPayslip"/></td>
		       </tr>
		  				
				<c:forEach var="advanceSchedule" items="${salaryadvance.advanceSchedules}">
		 			<tr id="row">
						
						<td class="whitebox3wk" ><input class="selectwk" size="5"  name="installmentNo" id="installmentNo" value="${advanceSchedule.installmentNo}"  style="width:100px"  readonly="true"></td>
						<c:if test="${advanceSchedule.recover!=null && advanceSchedule.recover=='Y'}">
							<script language="JavaScript"  type="text/JavaScript">
								if(document.getElementById("maintainScheduleFlag").disabled==false)
									document.getElementById("maintainScheduleFlag").disabled=true;
							</script>
							<td class="whitebox3wk"><input class="selectwk"  name="principalInstAmount" id="principalInstAmount" value="${advanceSchedule.principalAmt}" style="width:100px" readonly="true" ></td>
							<td class="whitebox3wk"><input class="selectwk"  name="interestInstAmount" id="interestInstAmount" value="${advanceSchedule.interestAmt}" style="width:100px" readonly="true" ></td>
						</c:if>
						<c:if test="${advanceSchedule.recover==null }">
							<td class="whitebox3wk"><input class="selectwk"  name="principalInstAmount" id="principalInstAmount" value="${advanceSchedule.principalAmt}" style="width:100px" onblur="checkNumber(this)" ></td>
							<td class="whitebox3wk"><input class="selectwk"  name="interestInstAmount" id="interestInstAmount" value="${advanceSchedule.interestAmt}" style="width:100px" onblur="checkNumber(this)" ></td>
						</c:if>
						<td class="whitebox3wk"><input class="selectwk"  name="recover" id="recover" value="${advanceSchedule.recover}" style="width:100px" readonly="true" ></td>
						
							<c:if test="${advanceSchedule.recover!=null && advanceSchedule.recover=='Y'}">
									
									<c:set var="payslipMonAndYearFieldValue" scope="page" >
										${advanceSchedule.egPayDeductions.empPayroll.id} (
									</c:set>
									<c:if test="${advanceSchedule.egPayDeductions.empPayroll.month==1}"><c:set var="payslipMonAndYearFieldValue" value="${payslipMonAndYearFieldValue} Jan"/></c:if>
									<c:if test="${advanceSchedule.egPayDeductions.empPayroll.month==2}"><c:set var="payslipMonAndYearFieldValue" value="${payslipMonAndYearFieldValue} Feb" /></c:if>
									<c:if test="${advanceSchedule.egPayDeductions.empPayroll.month==3}"><c:set var="payslipMonAndYearFieldValue" value="${payslipMonAndYearFieldValue} Mar"/></c:if>
									<c:if test="${advanceSchedule.egPayDeductions.empPayroll.month==4}"><c:set var="payslipMonAndYearFieldValue" value="${payslipMonAndYearFieldValue} Apr"/></c:if>
									<c:if test="${advanceSchedule.egPayDeductions.empPayroll.month==5}"><c:set var="payslipMonAndYearFieldValue" value="${payslipMonAndYearFieldValue} May"/></c:if>
									<c:if test="${advanceSchedule.egPayDeductions.empPayroll.month==6}"><c:set var="payslipMonAndYearFieldValue" value="${payslipMonAndYearFieldValue} Jun"/></c:if>
									<c:if test="${advanceSchedule.egPayDeductions.empPayroll.month==7}"><c:set var="payslipMonAndYearFieldValue" value="${payslipMonAndYearFieldValue} Jul"/></c:if>
									<c:if test="${advanceSchedule.egPayDeductions.empPayroll.month==8}"><c:set var="payslipMonAndYearFieldValue" value="${payslipMonAndYearFieldValue} Aug"/></c:if>
									<c:if test="${advanceSchedule.egPayDeductions.empPayroll.month==9}"><c:set var="payslipMonAndYearFieldValue" value="${payslipMonAndYearFieldValue} Sep"/></c:if>
									<c:if test="${advanceSchedule.egPayDeductions.empPayroll.month==10}"><c:set var="payslipMonAndYearFieldValue" value="${payslipMonAndYearFieldValue} Oct"/></c:if>
									<c:if test="${advanceSchedule.egPayDeductions.empPayroll.month==11}"><c:set var="payslipMonAndYearFieldValue" value="${payslipMonAndYearFieldValue} Nov"/></c:if>
									<c:if test="${advanceSchedule.egPayDeductions.empPayroll.month==12}"><c:set var="payslipMonAndYearFieldValue" value="${payslipMonAndYearFieldValue} Dec"/></c:if>
									<c:set var="payslipMonAndYearFieldValue" value="${payslipMonAndYearFieldValue} "/>
									<td class="whitebox3wk">
										<input type="text" class="selectwk"  name="payslip" id="payslip" value="${payslipMonAndYearFieldValue} <fmt:formatDate value="${advanceSchedule.egPayDeductions.empPayroll.fromDate}" pattern="yyyy" /> )" style="width:100px" readonly="true">
									</td>
									<td class="whitebox3wk">
										<input type="hidden" class="selectwk"  name="payslipId" id="payslipId" value="${advanceSchedule.egPayDeductions.empPayroll.id}" style="width:100px" >
									</td>
								</c:if>

								<c:if test="${advanceSchedule.recover==null}">
									<td class="whitebox3wk">
										<input type="text" class="selectwk"  name="payslip" id="payslip" value="" style="width:100px" readonly="true">
									</td>
									<td class="whitebox3wk">
										<input type="hidden" class="selectwk"  name="payslipId" id="payslipId" value="" style="width:100px" >
									</td>
								</c:if>
								
					</tr>
				</c:forEach>
			</table>
			<table id="tbl_buttons" align="left">
		        <tr>
					<td ><a href="#"><img src="<%=request.getContextPath()%>/common/image/add.png" alt="Add" width="16" height="16" border="0" id="addDetail" name="addDetail" onclick="addRow()" /></a></td>
			<td ><a href="#"><img src="<%=request.getContextPath()%>/common/image/cancel.png" alt="Delete" width="16" height="16" border="0" id="deleteDetail" name="deleteDetail" onclick="deleteRow()" /></a></td>
			
			</tr>
			</table>
		</c:if>
	</div>
	<table  width="95%"  cellpadding ="0" cellspacing ="0" border = "0" id="advanceSanctionTbl">

		<tr >
	    	
	    	<td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
                  <div class="headplacer">Sanction Details</div></td>    	
	    </tr>		    	
		<tr >
			<td class="whiteboxwk"><bean:message key="SanctionNo"/></td>
			<td class="whitebox2wk"><input type="text"  class="selectwk" name="sanctionNo" /></td>			
			<td class="whiteboxwk"><span class="mandatory">*</span><bean:message key="Sanction/RejectionDate"/> </td>
			<td class="whitebox2wk">
				<input type="text" class="selectwk" name="sanctionDate" value="${sanctionDate}" onblur="validateDateFormat(this);checkFdateTdate(this.value,'20/12/2007')"/>
				<a href="javascript:show_calendar('advanceForm.sanctionDate');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img id="img1" src="../img/show-calendar.gif" width=15 height=15 border=0></a>
			</td>						
		</tr>	
		<tr >
			
			<td class="greyboxwk"><bean:message key="Sanction/RejectBy"/></td>
			<td class="greybox2wk" colspan="6">
				<input type="text"  class="selectwk" name="sanctionBy" value="${sanctionBy}" readonly="readonly" />
			</td>					
		</tr>			
  </table>	

<br>

<table align='center' id="table2">	
 		<tr>
	    	<td class="whiteboxwk" >
	    		<html:submit property="sanctionRejectAction" value="sanction" onclick="return checkonSubmit('sanction');" styleClass="buttonfinal"/>		    					   
			</td>    
	    	<td class="whiteboxwk" >
				<html:submit property="sanctionRejectAction" value="sanction and next" onclick="return checkonSubmit('sancAndNext');" styleClass="buttonfinal"/>
			</td>
			<td class="whiteboxwk" >
			<html:submit property="sanctionRejectAction" value="reject" onclick="return checkonSubmit('reject');" styleClass="buttonfinal"/>
			</td>
	    </tr>	
</table>	
</html:form>

</body>
</html>

<%@ include file="/includes/taglibs.jsp" %>


<%@ page import="java.util.*,
				 java.text.SimpleDateFormat,
				 org.egov.payroll.model.*,
				 org.egov.payroll.dao.*,
				 org.egov.payroll.services.payslip.*,
				 org.egov.payroll.services.payhead.*,
				 org.egov.payroll.services.advance.*,
				 org.egov.payroll.utils.PayrollManagersUtill,
				 org.egov.infstr.utils.*,
				 org.egov.pims.utils.*,
			  	 java.math.*,
			  	 java.text.SimpleDateFormat,
				 org.egov.infstr.commons.*,
				 org.egov.payroll.utils.PayrollConstants" %>


<html>

<head>


	<title>Revise payslip</title>

	<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/commonyui/examples/autocomplete/css/examples.css">


	<style type="text/css">
		#codescontainer {position:absolute;left:11em;width:9%}
		#codescontainer .yui-ac-content {position:absolute;width:80%;border:1px solid #404040;background:#fff;overflow:hidden;z-index:9050;}
		#codescontainer .yui-ac-shadow {position:absolute;margin:.3em;width:40%;background:#a0a0a0;z-index:9049;}
		#codescontainer ul {padding:5px 0;width:80%;}
		#codescontainer li {padding:0 5px;cursor:default;white-space:nowrap;}
		#codescontainer li.yui-ac-highlight {background:#ff0;}
		#codescontainer li.yui-ac-prehighlight {background:#FFFFCC;}
</style>
<% session.removeAttribute("payHeader");%>


<% 
	EmpPayroll paySlip = (EmpPayroll) session.getAttribute("paySlip");

	List deductionsAdvList = (List)session.getAttribute("deductionsAdvList");
	System.out.println("deductionsAdvList.size()vvvvvvvvvvvvvvvvvvv"+deductionsAdvList.size());
	List deductionsotherList = (List)session.getAttribute("deductionsotherList");
	List deductionsTaxList = (List)session.getAttribute("deductionsTaxList");
	Set delEarnings = new HashSet();
	Set delAdvDeds = new HashSet();
	Set delOtherDeds = new HashSet();
	session.setAttribute("delEarnings", delEarnings);
	session.setAttribute("delAdvDeds", delAdvDeds);
	session.setAttribute("delOtherDeds", delOtherDeds);
		java.util.Date date = new java.util.Date();
SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
%>
<script language="JavaScript"  type="text/JavaScript">
		var empCodeArray;
		var selectedEmpCode;
		var acctCodeArray;
		var selectedAcctCode;
		var yuiflag = new Array();
		var yuiflag1 = new Array();
		var dedtaxes=new Array();
		var dedothers=new Array();
 function onBodyLoad(){
 
 netPayCollection();
	// alert(document.getElementById("payTable").rows.length);
	 var payheadLen = document.getElementById("paytable").rows.length;
	 if(payheadLen>2){
	 	if(document.salaryPaySlipForm.payType.value == 1)
		 	document.salaryPaySlipForm.payHeadAmount[0].readOnly=true;
	 }	
	 else
 	 	if(document.salaryPaySlipForm.payType.value == 1)
		 	document.salaryPaySlipForm.payHeadAmount.readOnly=true;
	 <c:if test = "${fn:length(deductionsAdvList)==0}">
		 populateAdvances();
	 </c:if>
   	 loadAccountCodes();
	<%
		EmpPayroll payslip = (EmpPayroll)session.getAttribute("paySlip");
		PayRollService payrollService = PayrollManagersUtill.getPayRollService();
    	PayheadService payheadService = PayrollManagersUtill.getPayheadService();
		AdvanceService advanceService = PayrollManagersUtill.getAdvanceService();
		ArrayList payScaleHeaderList=(ArrayList)payrollService.getAllPayScaleHeaders();
		System.out.println(">>>>>>>>>>>>>>>>>>> pay  " + payScaleHeaderList.size());
		ArrayList salaryCodes = (ArrayList)payrollService.getOrderedSalaryCodes();
//		ArrayList salaryCodes=(ArrayList)EgovMasterDataCaching.getInstance().get("pay-salaryCodes");
		System.out.println(">>>>>>>>>>>>>>>>>>> salaryCodes  " + salaryCodes.size());
		//ArrayList salAdvances=(ArrayList)EgovMasterDataCaching.getInstance().get("pay-salAdvances");
		List salAdvances = advanceService.getAllEligibleAdvancesForEmp(payslip.getEmployee().getIdPersonalInformation());
		//session.setAttribute("salAdvances",salAdvances);
		System.out.println(">>>>>>>>>>>>>>>>>>> salAdvances  " + salAdvances.size());



	%>
	document.salaryPaySlipForm.payType.disabled="true";

 }
/* if will invoke addRowToTable() method based on key pressed */
function whichButtonEarnings(tbl,obj,objr)
{
		
   if(checkPayHead() && objr=="earnings")
   addRowToTable(tbl,obj);

}
function populateDeleteSet(setName, delId)
{

	var http = initiateRequest();   
		var url = "<%=request.getContextPath()%>"+"/commons/updateDelSets.jsp?type="+setName+"&id="+delId;		      
		http.open("GET", url, true);
		http.onreadystatechange = function()
		{
			if (http.readyState == 4) 
			{
				if (http.status == 200) 
				{
				       var statusString =http.responseText.split("^");
	
				       
				 } 
			} 
		};
		http.send(null);   
}

 function deleteRow(table,obj)
	{
	if(table=='paytable')
	{
	var tbl = document.getElementById(table);
	var rowNumber=getRow(obj).rowIndex;
	var rowObj = getRow(obj);
	if(${fn:length(salaryPaySlipForm.payHead)}<(eval(rowNumber)-1)){
		var tbl = document.getElementById(table);
		//alert(getControlInBranch(tbl.rows[rowObj.rowIndex],'payHead').value);
		document.salaryPaySlipForm.delPayhead.value = getControlInBranch(tbl.rows[rowObj.rowIndex],'payHead').value;
	    var earningId = getControlInBranch(tbl.rows[rowObj.rowIndex],'earningId').value;
	    populateDeleteSet("delEarnings" , earningId);
	    tbl.deleteRow(rowNumber)
	}  
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
		var rowObj = getRow(obj);
		if(${fn:length(salaryPaySlipForm.salaryAdvances)}<(eval(rowNumber)-1)){		   
		   //alert(getControlInBranch(tbl.rows[rowObj.rowIndex],'salaryAdvances').value);
		   var advDedId = getControlInBranch(tbl.rows[rowObj.rowIndex],'advDedId').value;
		   //alert(advDedId);
		   populateDeleteSet("delAdvDeds",advDedId);
		   tbl.deleteRow(rowNumber)
		}   
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
		var rowObj = getRow(obj);
		if(${fn:length(salaryPaySlipForm.salaryAdvances)}<(eval(rowNumber)-1)){
			var otherDedId = getControlInBranch(tbl.rows[rowObj.rowIndex],'otherDedId').value;
			populateDeleteSet("delOtherDeds", otherDedId);
		    tbl.deleteRow(rowNumber)
		}   
		else
		{
		     alert("You cannot delete this row");
		     return false;
		}
	}
	if(table=='deductionOther')
	{
	
	    var tbl = document.getElementById(table);
		var rowNumber=getRow(obj).rowIndex;
		
		var rowObj = getRow(obj);
		if(rowNumber>1){
			var  dedOtherId = getControlInBranch(tbl.rows[rowObj.rowIndex],'dedOthrTxId').value;
			 
			if(dedOtherId !="")
			populateDeleteSet("delDedsOther", dedOtherId);
		    tbl.deleteRow(rowNumber)
		}   
		else
		{
		 var  dedOtherId = getControlInBranch(tbl.rows[rowObj.rowIndex],'dedOthrTxId').value;
			if(dedOtherId !="")
		     populateDeleteSet("delDedsOther", dedOtherId);
			  getControlInBranch(tbl.rows[rowObj.rowIndex],'dedOthrName').value="";
			  getControlInBranch(tbl.rows[rowObj.rowIndex],'dedOthrAmount').value="";
			  getControlInBranch(tbl.rows[rowObj.rowIndex],'dedRefNo').value="";
			  getControlInBranch(tbl.rows[rowObj.rowIndex],'yearToDateOther').value="";
		    
		}
	
	}
		if(table=='deductionTax')
	{
	
	var tbl = document.getElementById(table);
		var rowNumber=getRow(obj).rowIndex;
		var rowObj = getRow(obj);
		
		if(rowNumber>4){
		var  dedOtherId = getControlInBranch(tbl.rows[rowObj.rowIndex],'otherDedTxDedId').value;
			if(dedOtherId !="")
			populateDeleteSet("delDedsTax", dedOtherId);
		    tbl.deleteRow(rowNumber)
		}   
		else
		{
		     var  dedOtherId = getControlInBranch(tbl.rows[rowObj.rowIndex],'otherDedTxDedId').value;
			  if(dedOtherId !="")
		      populateDeleteSet("delDedsTax", dedOtherId);
		      getControlInBranch(tbl.rows[rowObj.rowIndex],'taxTypeName').value="";
			  getControlInBranch(tbl.rows[rowObj.rowIndex],'taxTypeAmount').value="";
			  getControlInBranch(tbl.rows[rowObj.rowIndex],'referenceno').value="";
			  getControlInBranch(tbl.rows[rowObj.rowIndex],'yearToDateTax').value="";
		      
		}
	
	}
	collectionSum();
	netPayCollection();
 }
function whichButtonTaxDeductions(tbl,obj,objr)
{
     if( objr=="taxDeductions")
	 {
       addRowToTable(tbl,obj);
	   }
    if(objr=="taxDeductionsOth")
	   {

	  addrowToDedOther(obj);
	   
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
  	   document.salaryPaySlipForm.earningId[remlen1-1].value="0";
 	   document.salaryPaySlipForm.pct[remlen1-1].value="";
 	   document.salaryPaySlipForm.payHeadAmount[remlen1-1].value="0";
 	   document.salaryPaySlipForm.calculationType[remlen1-1].value="";
 	   document.salaryPaySlipForm.payHead[remlen1-1].disabled = false;
 	   //alert(document.salaryPaySlipForm.payHead[remlen1-1].disabled);
 	   document.salaryPaySlipForm.pctBasis[remlen1-1].value="0";
	   document.salaryPaySlipForm.yearToDateHead[remlen1-1].value="0";
	   
  }
  else
  {
	  if(tbl=='paytable')
	  {
	    alert("No pay Heads Available to insert");
	    return false;
	  }
  }
  if(tbl=='salAdvances' && lastRow<getControlInBranch(tableObj.rows[rowObj1.rowIndex],'salaryAdvances').length)
  {
	 var rowObj = tableObj.rows[lastRow-1].cloneNode(true);
	  tbody.appendChild(rowObj);
  	  var remlen=document.salaryPaySlipForm.salaryAdvances.length;
	  document.salaryPaySlipForm.salaryAdvancesAmount[remlen-1].value="";
	  //alert("inside");	  
	  //alert(document.salaryPaySlipForm.salaryAdvances[remlen-1].disabled);
	  document.salaryPaySlipForm.salaryAdvances[remlen-1].disabled = false;
	  document.salaryPaySlipForm.advDedId[remlen-1].value="0";	
	  document.salaryPaySlipForm.yearToDateAdv[remlen-1].value="";
	  document.salaryPaySlipForm.advanceSchedule[remlen-1].value="";
  }
  else
  {
  	  if(tbl=='salAdvances')
  	  {
  	    alert("No salary Advances Available to insert");
  	    return false;
  	  }
  }
  if(tbl=='deductions' )//&& (lastRow<acctCodeArray.length)
    {
  	  var rowObj = tableObj.rows[lastRow-1].cloneNode(true);
  	  tbody.appendChild(rowObj);
  	  var remlen=document.salaryPaySlipForm.otherDeductAccountCode.length;
  	  document.salaryPaySlipForm.otherDeductAccountCode[remlen-1].value="";
  	  document.salaryPaySlipForm.otherDeductionsAmount[remlen-1].value="";
  	  document.salaryPaySlipForm.otherDeductAccountDescription[remlen-1].value="";
  	  document.salaryPaySlipForm.otherDedId[remlen-1].value="0";
	  document.salaryPaySlipForm.otherDeductAccountCode[remlen-1].disabled=false;
  }
  else
  {
  	 if(tbl=='deductions')
  	 {
  	   alert("No otherDeduction AccountCode Available to insert");
  	   return false;
     }
  }
  if(tbl=='deductionTax')
		     {
		   var rowObj = tableObj.rows[lastRow-1].cloneNode(true);
		   tbody.appendChild(rowObj);
		   var remlen=document.salaryPaySlipForm.yearToDateTax.length;
		  
		   document.salaryPaySlipForm.taxTypeName[remlen-1].value="";
		   document.salaryPaySlipForm.taxTypeAmount[remlen-1].value="";
		   document.salaryPaySlipForm.referenceno[remlen-1].value="";
		   document.salaryPaySlipForm.yearToDateTax[remlen-1].value="";
		    document.salaryPaySlipForm.otherDedTxDedId[remlen-1].value="0";
		   }
  
  
}
function addrowToDedOther(obj)
{

	tableObj=document.getElementById("deductionOther");
	var rowObj1=getRow(obj);
	var tbody=tableObj.tBodies[0];
	var lastRow = tableObj.rows.length;
    var rownum=rowObj1.rowIndex+1; 
	var rowObj = tableObj.rows[lastRow-1].cloneNode(true);
	tbody.appendChild(rowObj);
	getControlInBranch(tableObj.rows[rownum],'dedOthrName').value="";
	getControlInBranch(tableObj.rows[rownum],'dedOthrAmount').value="";
	getControlInBranch(tableObj.rows[rownum],'dedRefNo').value="";
	getControlInBranch(tableObj.rows[rownum],'yearToDateOther').value="";
	getControlInBranch(tableObj.rows[rownum],'dedOthrTxId').value="0";
	
  }
 
  

function whichButtonEarningsDeductions(tbl,obj,objr)
{
     if(checkSalAdv('key') && objr=="advDeductions")
       addRowToTable(tbl,obj);
}


function checkOtherDed(arg)
{
	var tbl = document.getElementById('deductions');
	var rCount=tbl.rows.length-1;
	if(tbl.rows.length == 2)
  {
	  if(arg!="submit")
	  {
	   if(document.salaryPaySlipForm.otherDeductAccountCode.value=="")
	   {
		  alert("Please select the otherDeduction AccountCode!!!");
		  document.salaryPaySlipForm.otherDeductAccountCode.value="";
		  document.salaryPaySlipForm.otherDeductAccountCode.focus();
		  return false;
	   }
       if(document.salaryPaySlipForm.otherDeductionsAmount.value=="" && document.salaryPaySlipForm.otherDeductionsAmount.value!=0)
	  {
		  alert("Please Enter the otherDeductions Amount!!!");
		  document.salaryPaySlipForm.otherDeductionsAmount.value="";
		  document.salaryPaySlipForm.otherDeductionsAmount.focus();
		  return false;
	  }
    }
    else
    {
		 if(document.salaryPaySlipForm.otherDeductAccountCode.value=="" && document.salaryPaySlipForm.otherDeductionsAmount.value!="" && document.salaryPaySlipForm.otherDeductionsAmount.value!=0)
		 {
		 	alert("Please select the otherDeduction AccountCode!!!");
			document.salaryPaySlipForm.otherDeductAccountCode.value="";
			document.salaryPaySlipForm.otherDeductAccountCode.focus();
		    return false;
		 }
		 if(document.salaryPaySlipForm.otherDeductAccountCode.value=="" && document.salaryPaySlipForm.otherDeductAccountDescription.value!="")
		 {
			alert("Please select the otherDeduction AccountCode!!!");
			document.salaryPaySlipForm.otherDeductAccountCode.value="";
			document.salaryPaySlipForm.otherDeductAccountDescription.value="";
			document.salaryPaySlipForm.otherDeductAccountCode.focus();
			return false;
		 }


	}
  }
  if(tbl.rows.length>2)
  {
	  if(arg!="submit")
	  {

	  if(document.salaryPaySlipForm.otherDeductAccountCode[rCount-1].value=="")
	  {
		alert("Please select the otherDeduction AccountCode!!!");
		document.salaryPaySlipForm.otherDeductAccountCode[rCount-1].value="";
		document.salaryPaySlipForm.otherDeductAccountCode[rCount-1].focus();
		return false;
	 }
	 if(document.salaryPaySlipForm.otherDeductionsAmount[rCount-1].value=="" &&  document.salaryPaySlipForm.otherDeductionsAmount[rCount-1].value!=0)
	 {
		alert("Please Enter the otherDeductions Amount!!!");
		document.salaryPaySlipForm.otherDeductionsAmount[rCount-1].value="";
		document.salaryPaySlipForm.otherDeductionsAmount[rCount-1].focus();
		return false;
	 }
    }
    else
	{
		 if(document.salaryPaySlipForm.otherDeductAccountCode[rCount-1].value=="" && (document.salaryPaySlipForm.otherDeductionsAmount[rCount-1].value!="" && document.salaryPaySlipForm.otherDeductionsAmount[rCount-1].value!=0))
		 {
			alert("Please select the otherDeduction AccountCode!!!");
			document.salaryPaySlipForm.otherDeductAccountCode.value="";
			document.salaryPaySlipForm.otherDeductAccountCode.focus();
			return false;
		 }
		  if(document.salaryPaySlipForm.otherDeductAccountCode[rCount-1].value=="" && document.salaryPaySlipForm.otherDeductAccountDescription[rCount-1].value!="")
		 {
			alert("Please select the otherDeduction AccountCode!!!");
			document.salaryPaySlipForm.otherDeductAccountCode[rCount-1].value="";
			document.salaryPaySlipForm.otherDeductAccountDescription[rCount-1].value="";
			document.salaryPaySlipForm.otherDeductAccountCode[rCount-1].focus();
			return false;
		 }


	}
 }
 return true;
}
function whichButtonEarnings2(tbl,obj,objr)
{
    
    if(checkOtherDed('key') && objr=="otherDed")
      addRowToTable(tbl,obj);
}
function checkSalAdv(arg)
{
	//alert("innn");
	var tbl = document.getElementById('salAdvances');
	var rCount=tbl.rows.length-1;
	 if(tbl.rows.length == 2)
	 {
		  if(arg!="submit")
		  {
		   if(document.salaryPaySlipForm.salaryAdvances =="0")
		   {
			  alert("Please select the salary Advance!!!");
			  document.salaryPaySlipForm.salaryAdvances.focus();
			  return false;
		   }
		   if(document.salaryPaySlipForm.salaryAdvancesAmount.value=="" || document.salaryPaySlipForm.salaryAdvancesAmount.value==0)
		   {
			  alert("Please Enter the salary Advances Amount!!!");
			  document.salaryPaySlipForm.salaryAdvancesAmount.value="";
			  document.salaryPaySlipForm.salaryAdvancesAmount.focus();
			  return false;
		   }
	      }
	      else
	      {

			 if(document.salaryPaySlipForm.salaryAdvances.options[document.salaryPaySlipForm.salaryAdvances.selectedIndex].value =="0" &&
			  document.salaryPaySlipForm.salaryAdvancesAmount.value!="" && document.salaryPaySlipForm.salaryAdvancesAmount.value!=0)
			  {
				  alert("Please select the salary Advance!!!");
				  document.salaryPaySlipForm.salaryAdvances.focus();
			       return false;
			 }

		  if(document.salaryPaySlipForm.salaryAdvances.options[document.salaryPaySlipForm.salaryAdvances.selectedIndex].value !="0"
	   && (document.salaryPaySlipForm.salaryAdvancesAmount.value!="0"
	   || document.salaryPaySlipForm.salaryAdvancesAmount.value!=""))
		{
			 
			 var advId = document.salaryPaySlipForm.salaryAdvances.options[document.salaryPaySlipForm.salaryAdvances.selectedIndex].value;
			 var dedAmount = 0;
		     var pendingAmt;
			<c:forEach var="dedObj" items="${deductionsAdvList}">		
				if(advId == "${dedObj.salAdvances.id}"){
					dedAmount = "${dedObj.amount}" ;			
				}		
			</c:forEach>	
			 var advAmount = document.salaryPaySlipForm.salaryAdvancesAmount.value;
			 var checkAmount = eval(advAmount) - eval(dedAmount);
			 
			if(checkingPendingAmount('EGPAY_SALADVANCES','PENDING_AMT',
					checkAmount,
					'ID_EMPLOYEE',document.getElementById("employeeCodeId").value,'ID',
			document.salaryPaySlipForm.salaryAdvances.options[document.salaryPaySlipForm.salaryAdvances.selectedIndex].value)=="false")
				{
					var head;
					<c:forEach var="salAdvancesObj" items="<%=salAdvances%>">
					  if(document.salaryPaySlipForm.salaryAdvances.options[document.salaryPaySlipForm.salaryAdvances.selectedIndex].value==
					  "${salAdvancesObj.id}")
					  {
						  head="${salAdvancesObj.salaryCodes.head}";
						  pendingAmt = "${salAdvancesObj.pendingAmt}"
					  }
					</c:forEach>
						var alertAmt = eval(pendingAmt) + eval(dedAmount);						
						alert("The Pending Amount for "+head+" is only Rs."+alertAmt);
						document.salaryPaySlipForm.salaryAdvancesAmount.focus();
						return false;
				}
		 }
		}
	  }
	  if(tbl.rows.length>2)
	  {
	   if(arg!="submit")
	   {
		  if(document.salaryPaySlipForm.salaryAdvances[rCount-1].value =="0" || document.salaryPaySlipForm.salaryAdvances[rCount-1].value=="")
		{
			alert("Please select the salary Advances!!!");
			document.salaryPaySlipForm.salaryAdvances[rCount-1].focus();
			return false;
		}
		if(document.salaryPaySlipForm.salaryAdvancesAmount[rCount-1].value=="" || document.salaryPaySlipForm.salaryAdvancesAmount[rCount-1].value==0)
		{
			alert("Please Enter the salary Advances Amount!!!");
			document.salaryPaySlipForm.salaryAdvancesAmount[rCount-1].value="";
			document.salaryPaySlipForm.salaryAdvancesAmount[rCount-1].focus();
			return false;
		}
	  }
		else
		{
			if(document.salaryPaySlipForm.salaryAdvances[rCount-1].options[document.salaryPaySlipForm.salaryAdvances[rCount-1].selectedIndex].value =="0" &&
			  (document.salaryPaySlipForm.salaryAdvancesAmount[rCount-1].value!="" && document.salaryPaySlipForm.salaryAdvancesAmount[rCount-1].value!=0))
			  {
				  alert("Please select the salary Advance!!!");
				  document.salaryPaySlipForm.salaryAdvances[rCount-1].focus();
				 return false;
			 }


		 if(document.salaryPaySlipForm.salaryAdvances[rCount-1].options[document.salaryPaySlipForm.salaryAdvances[rCount-1].selectedIndex].value !="0"
	   && (document.salaryPaySlipForm.salaryAdvancesAmount[rCount-1].value!="0"
	   || document.salaryPaySlipForm.salaryAdvancesAmount[rCount-1].value!=""))
		{
		/*	if(checkingPendingAmount('EGPAY_SALADVANCES','PENDING_AMT',
					document.salaryPaySlipForm.salaryAdvancesAmount[rCount-1].value,
					'ID_EMPLOYEE',document.getElementById("employeeCodeId").value,'ID',
			document.salaryPaySlipForm.salaryAdvances[rCount-1].options[document.salaryPaySlipForm.salaryAdvances[rCount-1].selectedIndex].value)=="false")
				{
						var head;
					<c:forEach var="salAdvancesObj" items="<%=salAdvances%>">
					  if(document.salaryPaySlipForm.salaryAdvances.options[document.salaryPaySlipForm.salaryAdvances[rCount-1].selectedIndex].value==
					  "${salAdvancesObj.id}")
					  {
						  head="${salAdvancesObj.salaryCodes.head}";
					  }
					</c:forEach>
						alert("2The Pending Amount for "+head
						+" is only Rs."+document.salaryPaySlipForm.salaryAdvPendingAmount[rCount-1].value);
						document.salaryPaySlipForm.salaryAdvancesAmount[rCount-1].focus();
						return false;
				}		*/
		}
   }
   }
   return true;
}
function checkPayHead()
{
	var tbl = document.getElementById('paytable');
	var rCount=tbl.rows.length-1;
	if(tbl.rows.length == 2)
  	{
		if(document.salaryPaySlipForm.payHead.options[document.salaryPaySlipForm.payHead.selectedIndex].value =="0")
		{
			alert("Please select the payHead!!!");
			document.salaryPaySlipForm.payHead.focus();
			return false;
		}
		if(document.salaryPaySlipForm.payHeadAmount.value=="")
		{
			alert("Please Enter the Amount for the Selected PayHead!!!");
			document.salaryPaySlipForm.payHeadAmount.value="";
			document.salaryPaySlipForm.payHeadAmount.focus();
			return false;
		}
		if(document.salaryPaySlipForm.pct.value=="" && document.salaryPaySlipForm.calculationType.value=="ComputedValue")
		{
			alert("Please Enter the Percentage for the Selected PayHead!!!");
			document.salaryPaySlipForm.pct.value="";
			document.salaryPaySlipForm.pct.focus();
			return false;
		}
	}
	else
	{
		if(tbl.rows.length>2)
		{
			if(document.salaryPaySlipForm.payHead[rCount-1].options[document.salaryPaySlipForm.payHead[rCount-1].selectedIndex].value =="0")
			{
				alert("Please select payHead!!!");
				document.salaryPaySlipForm.payHead[rCount-1].focus();
				return false;
			 }
			 if(document.salaryPaySlipForm.payHeadAmount[rCount-1].value=="")
			{
				alert("Please Enter the Amount for the Selected PayHead!!!");
				document.salaryPaySlipForm.payHeadAmount[rCount-1].value="";
				document.salaryPaySlipForm.payHeadAmount[rCount-1].focus();
				return false;
			}
			if(document.salaryPaySlipForm.pct[rCount-1].value=="" && document.salaryPaySlipForm.calculationType[rCount-1].value=="ComputedValue")
			{
				alert("Please Enter the Percentage for the Selected PayHead!!!");
				document.salaryPaySlipForm.pct[rCount-1].value="";
				document.salaryPaySlipForm.pct[rCount-1].focus();
				return false;
			}
		}
	 }
	 return true;
 }
 function checkDuplicate(obj)
  {
 	 var tbl = document.getElementById('paytable').rows.length;
 	var rowObj=getRow(obj);
     var pctBasisId;
     var table= document.getElementById("paytable");
     getControlInBranch(table.rows[rowObj.rowIndex],'pct').value="";
	 getControlInBranch(table.rows[rowObj.rowIndex],'payHeadAmount').value="";
	 getControlInBranch(table.rows[rowObj.rowIndex],'yearToDateHead').value="";
	<c:forEach var="payHeadObj" items="<%=salaryCodes%>" >
	  if(getControlInBranch(table.rows[rowObj.rowIndex],'payHead').value=="${payHeadObj.id}"){
		<c:if test="${payHeadObj.calType=='ComputedValue'}">
			getControlInBranch(table.rows[rowObj.rowIndex],'calculationType').value = "${payHeadObj.calType}";
			getControlInBranch(table.rows[rowObj.rowIndex],'payHeadAmount').readOnly=true;
			pctBasisId = "${payHeadObj.salaryCodes.id}"
			var result=funPctBasis(pctBasisId);
			if(result!=false){
				getControlInBranch(table.rows[rowObj.rowIndex],'pctBasis').value =result;
			}
			getControlInBranch(table.rows[rowObj.rowIndex],'pct').readOnly=false;
		</c:if>
		<c:if test="${payHeadObj.calType=='MonthlyFlatRate'}">
			getControlInBranch(table.rows[rowObj.rowIndex],'calculationType').value = "${payHeadObj.calType}";
			getControlInBranch(table.rows[rowObj.rowIndex],'pctBasis').value = "";
			getControlInBranch(table.rows[rowObj.rowIndex],'pct').readOnly=true;
			getControlInBranch(table.rows[rowObj.rowIndex],'payHeadAmount').readOnly=false;
		</c:if>
		}
	</c:forEach>
}
 function calAmount(obj)
 {
 	 calEarningsAmt();
	 checkdecimalval(obj,obj.value);
	 var sum1;
	 var pctBasisText;
	 var rowObj=getRow(obj);
	 var table= document.getElementById("paytable");
	 if(getControlInBranch(table.rows[rowObj.rowIndex],'pct').readOnly==false)
	 {
		 if(getControlInBranch(table.rows[rowObj.rowIndex],'pct').value!="" && getControlInBranch(table.rows[rowObj.rowIndex],'pct').value>100)
		 {

			 alert("Please enter the percentage between 0-100!!!");
			 getControlInBranch(table.rows[rowObj.rowIndex],'pct').focus();
			 return false;
		 }
		 if(getControlInBranch(table.rows[rowObj.rowIndex],'pct').value!="" && getControlInBranch(table.rows[rowObj.rowIndex],'pct').value>0)
		 {
			  sum1 = getControlInBranch(table.rows[rowObj.rowIndex],'pct').value/100;
			  pctBasisText = getControlInBranch(table.rows[rowObj.rowIndex],'pctBasis').value
		  }
		  else
		  {
			   sum1=0;
			   pctBasisText = getControlInBranch(table.rows[rowObj.rowIndex],'pctBasis').value
	   	  }
		  var remlen1=document.salaryPaySlipForm.pctBasis.length;
	 for(var i=0;i<remlen1-1;i++)
	 {
		if(document.salaryPaySlipForm.payHead[i].options[document.salaryPaySlipForm.payHead[i].selectedIndex].text==pctBasisText)
		 {
			 sum1= sum1*document.salaryPaySlipForm.payHeadAmount[i].value;
			 getControlInBranch(table.rows[rowObj.rowIndex],'payHeadAmount').value =Math.round(sum1);

	  	 }
	 }

	 collectionSum();
	 netPayCollection();
	}

 }
 function funPctBasis(pctBasisId)
 {
 	var result="";
 	<c:forEach var="payHeadObj" items="<%=salaryCodes%>" >
 	if(pctBasisId=="${payHeadObj.id}")
 	{
		result = "${payHeadObj.head}";
	}
  </c:forEach>
  if(result=="")
  {
	  return false
  }
  return result;
}

function checkPctBasis(obj)
{
	 var rowObj=getRow(obj);
	 var hit=0;
	 var table= document.getElementById("paytable");
	 var tbl = document.getElementById('paytable').rows.length;
	 var pctBasisText = getControlInBranch(table.rows[rowObj.rowIndex],'pctBasis').value;
	if(tbl>2)
	{
		for(var i=0;i<tbl;i++)
		{
			for(var j=i+1;j<tbl-1;j++)
			{

				if(i!=j)
				{
					if(document.salaryPaySlipForm.payHead[i].options[document.salaryPaySlipForm.payHead[i].selectedIndex].text==document.salaryPaySlipForm.payHead[j].options[document.salaryPaySlipForm.payHead[j].selectedIndex].text)
					{
						alert("1Duplicate Selection of Pay Head!!!");
						document.salaryPaySlipForm.payHead[j].focus();
						return false;
					 }
				}
			}
		 }
 	 }
}
function checksalAdvances(obj)
{
	if(obj.value!="0")
	{
		var tbl = document.getElementById('salAdvances').rows.length;
		var rowObj=getRow(obj);
		var rowNo = rowObj.rowIndex;
		var currRow = rowNo-1;
		if(tbl>2)
		{
			for(var i=0;i<tbl;i++)
			{
				for(var j=i+1;j<tbl-1;j++)
				{
					if(i!=j)
					{
						if(document.salaryPaySlipForm.salaryAdvances[i].options[document.salaryPaySlipForm.salaryAdvances[i].selectedIndex].value==document.salaryPaySlipForm.salaryAdvances[j].options[document.salaryPaySlipForm.salaryAdvances[j].selectedIndex].value)
						{
							alert("<bean:message key='alertDupSelSalAdv'/>");
							obj.selectedIndex=0;
							//document.salaryPaySlipForm.yearToDateAdv[currRow].value = eval(document.salaryPaySlipForm.yearToDateAdv[currRow].value) - eval(document.salaryPaySlipForm.salaryAdvancesAmount[currRow].value);
							document.salaryPaySlipForm.salaryAdvancesAmount[currRow].value="";
							return false;
						 }
					}
				}
			 }
	 	 }
	 }
}
function checkOtherDedCode(obj)
{
	var tbl = document.getElementById('deductions').rows.length;
	var rowObj=getRow(obj);
	if(tbl>2)
	{
		for(var i=0;i<tbl;i++)
		{
			for(var j=i+1;j<tbl-1;j++)
			{
				if(i!=j)
				{
					if(document.salaryPaySlipForm.otherDeductAccountCode[i].value==document.salaryPaySlipForm.otherDeductAccountCode[j].value)
					{
						alert("Duplicate Selection of other Deductions AccountCode!!!");
						document.salaryPaySlipForm.otherDeductAccountCode[j].focus();
						return false;
					 }
				}
			}
		 }
 	 }
 }
function populateAdvances()
{
   
	var empId = document.getElementById("employeeCodeId").value;
	var count=1;
	//alert(empId);
	for(var resLen=1;resLen<document.salaryPaySlipForm.salaryAdvances.length;resLen++)
	{
	     // document.salaryPaySlipForm.salaryAdvances.options[resLen]=null;
     }
    
	if(empId !="")
	{
		<c:forEach var="payHeadObj" items="<%=salaryCodes%>">
		  <c:forEach var="salAdvancesObj" items="<%=salAdvances%>">
		  <c:if test = "${payHeadObj.id==salAdvancesObj.salaryCodes.id}">
		  
		     if(empId == "${salAdvancesObj.employee.idPersonalInformation}")
			  {
					<c:if test = "${salAdvancesObj.status.description=='Disbursed'}">
					<c:if test = "${salAdvancesObj.pendingAmt!='0'}">
		    		document.salaryPaySlipForm.salaryAdvances.options[count] = new Option("${payHeadObj.head}","${salAdvancesObj.id}",false,false);
					count=count+1;
					</c:if>
					</c:if>
				}

		 	</c:if>
		  </c:forEach>
		</c:forEach>
	}
}
function checkingPendingAmount(tablename,fieldname,fieldvalue,fieldname1,fieldvalue1,fieldname2,fieldvalue2)
  {
 	var type = "checkingAmount";
 	var link = "<%=request.getContextPath()%>"+"/commons/checkingPendingAmount.jsp?type=" + type+"&tablename=" + tablename+"&fieldname=" + fieldname+ "&fieldvalue=" + fieldvalue+"&fieldname1=" + fieldname1+ "&fieldvalue1=" + fieldvalue1+"&fieldname2=" + fieldname2+ "&fieldvalue2=" + fieldvalue2+ " ";
 	var request = initiateRequest();
 	var isUnique;
 	request.onreadystatechange = function()
 	{

 	if (request.readyState == 4)
 	{
 	if (request.status == 200)
 	{
 	var response=request.responseText;
 	var result = response.split("/");
 	//alert("result "+result[0]);

 		if(result[0]=="true")
 		{

 			isUnique="true";
 		}
 		else if(result[0]=="false")
 		{
 			isUnique="false";
 		}

 	    }
 	  }
 	};
 	request.open("GET", link, false);
 	request.send(null);

 	return isUnique;
}

function populateInstAmount(obj)
{
	
	 var rowObj=getRow(obj);
     var table= document.getElementById("salAdvances");
	 var advScheduleExistInPayslip;
	 var advSchedulerId = getControlInBranch(table.rows[rowObj.rowIndex],'advanceSchedule').value;
	 //alert(getControlInBranch(table.rows[rowObj.rowIndex],'salaryAdvances').value);
	 <c:forEach var="salAdvancesObj" items="<%=salAdvances%>">
	 if(getControlInBranch(table.rows[rowObj.rowIndex],'salaryAdvances').value=="${salAdvancesObj.id}"){
	  
	   <c:if test ="${salAdvancesObj.maintainSchedule=='Y'}">
	      ("hwrere"+getControlInBranch(table.rows[rowObj.rowIndex],'salaryAdvPendingAmount'));
	      getControlInBranch(table.rows[rowObj.rowIndex],'salaryAdvancesAmount').readOnly='true';
	      getControlInBranch(table.rows[rowObj.rowIndex],'yearToDateAdv').readOnly='true';
	      </c:if>
		if(${fn:length(salAdvancesObj.advanceSchedules)} != 0){
			<c:forEach var="advSchedule" items="${salAdvancesObj.advanceSchedules}">				
				<c:forEach var="dedObj" items="${paySlip.deductionses}">					
					<c:if test = "${dedObj.advanceScheduler.id == advSchedule.id}">
							//alert("sched----"+"${advSchedule.id}");
							getControlInBranch(table.rows[rowObj.rowIndex],'salaryAdvPendingAmount').value = "${salAdvancesObj.pendingAmt}";
							getControlInBranch(table.rows[rowObj.rowIndex],'salaryAdvancesAmount').value = "${advSchedule.principalAmt + advSchedule.interestAmt}";
							getControlInBranch(table.rows[rowObj.rowIndex],'advanceSchedule').value = "${advSchedule.id}";
							advScheduleExistInPayslip = "true";
					</c:if>
				</c:forEach>
			</c:forEach>
			
			if(advScheduleExistInPayslip != "true"){
				//alert("not req");
				<c:set var="endLoop" value="true"/>
				<c:forEach var="advSchedule" items="${salAdvancesObj.advanceSchedules}">
					<c:if test = "${endLoop}">
						<c:if test = "${advSchedule.recover == null}">
							getControlInBranch(table.rows[rowObj.rowIndex],'salaryAdvPendingAmount').value = "${salAdvancesObj.pendingAmt}";
							getControlInBranch(table.rows[rowObj.rowIndex],'salaryAdvancesAmount').value = "${advSchedule.principalAmt + advSchedule.interestAmt}";
							getControlInBranch(table.rows[rowObj.rowIndex],'advanceSchedule').value = "${advSchedule.id}";							
							<c:set var="endLoop" value="false"/>
						</c:if>
					</c:if>
				</c:forEach>
			}
			
		}else if(${fn:length(salAdvancesObj.advanceSchedules)} == 0){
			getControlInBranch(table.rows[rowObj.rowIndex],'salaryAdvPendingAmount').value = "${salAdvancesObj.pendingAmt}";
			getControlInBranch(table.rows[rowObj.rowIndex],'salaryAdvancesAmount').value = "${salAdvancesObj.instAmt}";
			getControlInBranch(table.rows[rowObj.rowIndex],'advanceSchedule').value = "";
		}
	}
	</c:forEach>
	if(getControlInBranch(table.rows[rowObj.rowIndex],'salaryAdvances').value=="" || getControlInBranch(table.rows[rowObj.rowIndex],'salaryAdvances').value=="0"){
		getControlInBranch(table.rows[rowObj.rowIndex],'yearToDateAdv').value = "";//eval(getControlInBranch(table.rows[rowObj.rowIndex],'yearToDateAdv').value) - eval(document.salaryPaySlipForm.salaryAdvancesAmount[i].value);
		getControlInBranch(table.rows[rowObj.rowIndex],'salaryAdvancesAmount').value = "";
		getControlInBranch(table.rows[rowObj.rowIndex],'salaryAdvancesAmount').focus();
		return false;
	}
}

function checkAmount(obj)
{	
	 var rowObj=getRow(obj);
     var table= document.getElementById("salAdvances");
     var advId = getControlInBranch(table.rows[rowObj.rowIndex],'salaryAdvances').value;    
     var dedAmount = 0;
     var pendingAmt;
	<c:forEach var="dedObj" items="${deductionsAdvList}">		
		if(advId == "${dedObj.salAdvances.id}"){
			dedAmount = "${dedObj.amount}" ;			
		}		
	</c:forEach>	
	
	 var advAmount = getControlInBranch(table.rows[rowObj.rowIndex],'salaryAdvancesAmount').value;
	 var checkAmount = eval(advAmount) - eval(dedAmount);	
	 if(getControlInBranch(table.rows[rowObj.rowIndex],'salaryAdvances').value!="" && getControlInBranch(table.rows[rowObj.rowIndex],'salaryAdvances').value!="0"){
		if(checkingPendingAmount('EGPAY_SALADVANCES','PENDING_AMT',
		   checkAmount,
		   'ID_EMPLOYEE',document.getElementById("employeeCodeId").value,
		   'ID',getControlInBranch(table.rows[rowObj.rowIndex],'salaryAdvances').value) == "false")
		{
			var head;
			<c:forEach var="salAdvancesObj" items="<%=salAdvances%>">
			  if(getControlInBranch(table.rows[rowObj.rowIndex],'salaryAdvances').value==
			  "${salAdvancesObj.id}")
			  {
				  head="${salAdvancesObj.salaryCodes.head}";
				  pendingAmt = "${salAdvancesObj.pendingAmt}" ;
			  }
			</c:forEach>
			var alertAmt = eval(pendingAmt) + eval(dedAmount);			
			alert("The Pending Amount for "+head+" is only Rs."
			+ alertAmt);
			getControlInBranch(table.rows[rowObj.rowIndex],'salaryAdvancesAmount').focus();
			return false;
		}
	}
}

function removeZero(obj)
{
	if(obj.value==0)
	{

		obj.value="";
		obj.focus();

	}
		return true;
}

function addZero(obj)
{
	if(obj.value=="")
	{
		obj.value=0;
	}
	collectionSum();
	calOnchangeAmount(obj);
	return;
}
function checkdecimalval(obj,amount)
{
    var objt = obj;
    var amt = amount;
    if(amt != null && amt != "")
    {
      if(amt < 0 )
        {
            alert("Please enter positive value for the amount");
            objt.focus();
            return false;

        }
        if(isNaN(amt))
        {
            alert("Please enter a numeric value for the amount");
            objt.focus();
            return false;

        }
           objt.value= Math.round(amt);
           collectionSum();
           calOnchangeAmount(objt);

    }
}
function calOnchangeAmount(objt)
{
	var sum1;
	var rowObj=getRow(objt);
	var payText;
	var table= document.getElementById("paytable");
	 if(getControlInBranch(table.rows[rowObj.rowIndex],'payHeadAmount').readOnly==false)
	 {
		if(getControlInBranch(table.rows[rowObj.rowIndex],'payHeadAmount').value!="" && getControlInBranch(table.rows[rowObj.rowIndex],'payHeadAmount').value>0)
		 {
			  var payHeadValue= getControlInBranch(table.rows[rowObj.rowIndex],'payHead').value;
			  <c:forEach var="payHeadObj" items="<%=salaryCodes%>" >
			  if(payHeadValue=="${payHeadObj.id}")
			  {
					payText="${payHeadObj.head}"
					sum1 = getControlInBranch(table.rows[rowObj.rowIndex],'payHeadAmount').value


			  }
			  </c:forEach>


		  }
		  else
		  {

			  var payHeadValue= getControlInBranch(table.rows[rowObj.rowIndex],'payHead').value;
			  <c:forEach var="payHeadObj" items="<%=salaryCodes%>" >
			  if(payHeadValue=="${payHeadObj.id}")
			  {
					payText="${payHeadObj.head}"

			  }
			  </c:forEach>
			  sum1=0;
		  }
	 var remlen1=document.salaryPaySlipForm.pctBasis.length;
	 for(var i=0;i<remlen1;i++)
	 {
		var sum=0;
		if(document.salaryPaySlipForm.pctBasis[i].value==payText && sum1!=0)
		 {
			  sum= sum1*(document.salaryPaySlipForm.pct[i].value/100);
			  document.salaryPaySlipForm.payHeadAmount[i].value =Math.round(sum);

		 }
		else
		 {
			 if(document.salaryPaySlipForm.pctBasis[i].value==payText && sum1==0)
			 {
				 document.salaryPaySlipForm.payHeadAmount[i].value =sum1;
			 }
		 }
	}
	collectionSum();
	netPayCollection();
   }
 }
function checkdecimalval1(obj,amount)
{
    var objt = obj;
    var amt = amount;
    if(amt != null && amt != "")
    {
      if(amt < 0 )
        {
            alert("Please enter positive value for the amount");
            objt.focus();
            return false;

        }
        if(isNaN(amt))
        {
            alert("Please enter a numeric value for the amount");
            objt.focus();
            return false;

        }
           objt.value= Math.round(amt);
           netPayCollection();

    }
}

function collectionSum()
{
	 var sum=0;
	if(document.salaryPaySlipForm.payHeadAmount.length>0)
  {
	 for(var i=0;i<document.salaryPaySlipForm.payHeadAmount.length;i++)
	 {
		if(document.salaryPaySlipForm.payHeadAmount[i].value!="")
		{
			sum = sum+ eval(document.salaryPaySlipForm.payHeadAmount[i].value);
		}
		else
		{
			document.salaryPaySlipForm.payHeadAmount[i].value=0;
		}
	 }
   }
   else
   {
	   if(document.salaryPaySlipForm.payHeadAmount.value!="")
	   {
		   sum = sum+ eval(document.salaryPaySlipForm.payHeadAmount.value);
	   }
	   else
	   {
		   document.salaryPaySlipForm.payHeadAmount.value=0;
	   }
   }
   document.salaryPaySlipForm.grossPay.value=Math.round(sum);

	return true;
}
function netPayCollection()
{

	var net=0;
	if(document.salaryPaySlipForm.taxTypeAmount.length>0)
	{
		for(var i=0;i<document.salaryPaySlipForm.taxTypeAmount.length;i++)
		{
			if(document.salaryPaySlipForm.taxTypeAmount[i].value!="")
			{
				net = net+eval(document.salaryPaySlipForm.taxTypeAmount[i].value);
			}
			else
			{
				document.salaryPaySlipForm.taxTypeAmount[i].value=0;
			}
		}

	}
	else
	{
		if(document.salaryPaySlipForm.taxTypeAmount.value !="")
		{
			net = net+ eval(document.salaryPaySlipForm.taxTypeAmount.value);
		}
		else
		{
			document.salaryPaySlipForm.taxTypeAmount.value=0;
	    }
	}
	
	
	if(document.salaryPaySlipForm.dedOthrAmount.length>0)
	{
		for(var i=0;i<document.salaryPaySlipForm.dedOthrAmount.length;i++)
		{
			if(document.salaryPaySlipForm.dedOthrAmount[i].value!="")
			{
				net = net+eval(document.salaryPaySlipForm.dedOthrAmount[i].value);
			}
			else
			{
				document.salaryPaySlipForm.dedOthrAmount[i].value=0;
			}
		}

	}
	else
	{
		if(document.salaryPaySlipForm.dedOthrAmount.value !="")
		{
			net = net+ eval(document.salaryPaySlipForm.dedOthrAmount.value);
		}
		else
		{
			document.salaryPaySlipForm.dedOthrAmount.value=0;
	    }
	}
	
	  var table= document.getElementById("salAdvances");
	      var len = table.rows.length;
	      
	
	if(len>2)
	{
	
		 for(var i=0;i<len-1;i++)
		 {
			if(document.salaryPaySlipForm.salaryAdvancesAmount[i].value!="")
			{
				net = net+ eval(document.salaryPaySlipForm.salaryAdvancesAmount[i].value);
			}
			else
			{
				document.salaryPaySlipForm.salaryAdvancesAmount[i].value=0;
			}
		}
	}
	else
	{
	
		if(document.salaryPaySlipForm.salaryAdvancesAmount.value!="")
		{
			net = net+ eval(document.salaryPaySlipForm.salaryAdvancesAmount.value);
		}
		else
		{
			document.salaryPaySlipForm.salaryAdvancesAmount.value=0;
	    }
	}
	if(document.salaryPaySlipForm.otherDeductionsAmount.length>0)
	{
		 for(var i=0;i<document.salaryPaySlipForm.otherDeductionsAmount.length;i++)
		 {
			if(document.salaryPaySlipForm.otherDeductionsAmount[i].value!="")
			{
				net = net+ eval(document.salaryPaySlipForm.otherDeductionsAmount[i].value);
			}
			else
			{
				document.salaryPaySlipForm.otherDeductionsAmount[i].value=0;
			}
		}
	}
	else
	{
		if(document.salaryPaySlipForm.otherDeductionsAmount.value!="")
		{
			net = net+ eval(document.salaryPaySlipForm.otherDeductionsAmount.value);
		}
		else
		{
			document.salaryPaySlipForm.otherDeductionsAmount.value=0;
		}
	}
	document.salaryPaySlipForm.totalDeductions.value=net;
	if(document.salaryPaySlipForm.grossPay.value!="" && document.salaryPaySlipForm.totalDeductions.value!="")
	{
	    var sum=eval(document.salaryPaySlipForm.grossPay.value)-net;
	    document.salaryPaySlipForm.netPay.value = Math.round(sum);
    }
    else
    {
		document.salaryPaySlipForm.netPay.value=net;
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


function autocompleteDeduc(obj,event)
 {
 	// set position of dropdown
 	var src = obj;
 	var target = document.getElementById('codescontainer');
 	var posSrc=findPos(src);
 	target.style.left=posSrc[0];
 	target.style.top=posSrc[1]+25;
 	if(obj.name=='otherDeductAccountCode') target.style.left=posSrc[0]+0;

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

 				var oAutoComp1 = new YAHOO.widget.AutoComplete(coaCodeObj,'codescontainer', selectedAcctCode);
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

 function fillNeibrAfterSplit1(obj,neibrObjName)
 {
	var currRow=getRow(obj);
	yuiflag[currRow.rowIndex] = undefined;
	neibrObj1=getControlInBranch(currRow,neibrObjName);
	var temp = obj.value;temp = temp.split("`-`");
	var xyz = getControlInBranch(currRow,'otherDeductAccountCode');
	xyz.value=temp[0];
	//alert(temp[0]+" "+temp[1]+" "+temp[2]);
	if(obj.value==null || obj.value=="") { neibrObj1.value=""; return; }
	if(temp[1]==null && (neibrObj1.value!='' || neibrObj1.value!=null) ) {  return ;}
	else {
			neibrObj1.value=temp[2];
			getControlInBranch(currRow,'otherDeductAccountDescription').value=temp[1];
			getControlInBranch(currRow,'otherDeductionsAmount').value="";
			getControlInBranch(currRow,'otherDeductionsAmount').focus();
		 }

   }
 function loadAccountCodes()
 {
 		var type='getAllGlcodesFromAccount';
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
						acctCodeArray=codes.split("+");
						selectedAcctCode = new YAHOO.widget.DS_JSArray(acctCodeArray);
					  }
				  }
		};
		req2.open("GET", url, true);
	req2.send(null);

 }
  function validation(arg)
 {
	//alert(document.salaryPaySlipForm.advanceSchedule[0].value);
	//alert(document.salaryPaySlipForm.advanceSchedule[1].value);
	//return false;
	netPayCollection(); 
    if(arg=="save")
	{
	
	if(document.salaryPaySlipForm.modifyRemarks.value=="")	{
		alert("Enter remarks for modification");
		document.salaryPaySlipForm.modifyRemarks.focus();
		return false;
	}
	
    if(document.salaryPaySlipForm.payHeadAmount.length>0)
	 {
	 	//alert("inside");
	 	 for(var i=0;i<document.salaryPaySlipForm.payHeadAmount.length;i++)
	 	 {
			//alert(document.salaryPaySlipForm.payHead[i].value);	 	 
	 		if(document.salaryPaySlipForm.payHead[i].options[document.salaryPaySlipForm.payHead[i].selectedIndex].value =="0")
			{
				alert("Please Select the PayHead!!!");
				document.salaryPaySlipForm.payHead[i].focus();
				return false;
			}
			//alert(document.salaryPaySlipForm.pct[i].value);
	 		if(document.salaryPaySlipForm.pct[i].value=="" && document.salaryPaySlipForm.calculationType[i].value=="ComputedValue")
			{
				alert("Please Enter the Percentage for the Selected PayHead!!!");
				document.salaryPaySlipForm.pct[i].focus();
				return false;
			}
	 	  }
	  }
     else
	    {
	 	   if(document.salaryPaySlipForm.payHead.options[document.salaryPaySlipForm.payHead.selectedIndex].value =="0")
			{
				alert("Please Select the PayHead!!!");
				document.salaryPaySlipForm.payHead.focus();
				return false;
			}
	 	   if(document.salaryPaySlipForm.pct.value=="" && document.salaryPaySlipForm.calculationType.value=="ComputedValue")
			{
				alert("Please Enter the Percentage for the Selected PayHead!!!");
				document.salaryPaySlipForm.pct.focus();
				return false;
			}
	   }
	   var remlen1=document.salaryPaySlipForm.pctBasis.length;
	   var payBasisText="";
	if(remlen1>0)
	{
	   	//alert("greate then zero");
	   	for(var j=0;j<remlen1;j++)
	   	{
	   	 	//alert("drill 11");
	   	    if(document.salaryPaySlipForm.calculationType[j].value=="ComputedValue" && document.salaryPaySlipForm.pctBasis[j].value!="")
	   	    {
				//payBasisText =payBasisText+","+document.salaryPaySlipForm.pctBasis[j].value;
				if(trimAll(payBasisText)!="")
				{
					payBasisText =payBasisText+","+trimAll(document.salaryPaySlipForm.pctBasis[j].value);
				}
				else
				{
					payBasisText =trimAll(document.salaryPaySlipForm.pctBasis[j].value);
				}
				//alert("payBasisText  "+payBasisText);

	   	  	}
		}
		   var d1 = payBasisText;
			d1 = d1.split(',');
			var hit=0;
			 for(var i=0;i<d1.length;i++)
	        {
				for(var j=0;j<document.salaryPaySlipForm.payHead.length;j++)
					{
						if(d1[i]==document.salaryPaySlipForm.payHead[j].options[document.salaryPaySlipForm.payHead[j].selectedIndex].text
						&& d1[i]!="" )
						{
							hit=1;
						}
					}

			}
			if(hit==0 && payBasisText!="" )
			{
				alert("Please the select PctBasis as above");
				return false;
			}
	  }
	  else
	  {
		  if(document.salaryPaySlipForm.pctBasis.value!="" && document.salaryPaySlipForm.calculationType.value=="ComputedValue")
		  {
			    alert("Please the select flat value type first");
			    document.salaryPaySlipForm.payHead.focus();
				return false;
		  }
	  }
	   
	   var temp = checkSalAdv('submit');
	   //alert(temp); 	
	   if(temp=="false")
	   		return false;		
	   checkOtherDed('submit');
	   if(document.salaryPaySlipForm.grossPay.value =="0")
		{
			alert("Please Enter the Amount greater than Zero!!!");
			return false;
		}
       		
		var len =document.getElementById("deductionTax").rows.length;
		if(len==5)
		{
		
		   if(document.salaryPaySlipForm.taxTypeName.value !="")
			  {
			 
	           if(document.salaryPaySlipForm.taxTypeAmount.value ==0)
				   {
				     alert("please fill the amountttt ");
			         return false;
					}
				
				
			}
		
		
		} 
		else if(len>5)
		{
		
		for(var i=0;i<len-4;i++)
	
		{
			if(document.salaryPaySlipForm.taxTypeName[i].value !="")
			{
			 
	           if(document.salaryPaySlipForm.taxTypeAmount[i].value ==0)
				   {
				     alert("please fill the amount ");
			         return false;
					}
				
				
			}
		}
			}
				var lenOthr =document.getElementById("deductionOther").rows.length;
				
				
		if(lenOthr==2)
		{

		   if(document.salaryPaySlipForm.dedOthrName.value !="")
			  {
			 
	           if(document.salaryPaySlipForm.dedOthrAmount.value ==0)
				   {
				     alert("please fill the amountttt ");
			         return false;
					}
				
			}
		
		
		} 
		else if(lenOthr>2)
		{
		
		for(var i=0;i<lenOthr-1;i++)
	
		{
			if(document.salaryPaySlipForm.dedOthrName[i].value !="")
			{
			 
	           if(document.salaryPaySlipForm.dedOthrAmount[i].value ==0)
				   {
				     alert("please fill the amount ");
			         return false;
					}
				
				
			}
		}
			}
		
	//alert(document.salaryPaySlipForm.payType.value);
	/*
	if(document.salaryPaySlipForm.payType.options[document.salaryPaySlipForm.payType.selectedIndex].text=="<%=PayrollConstants.EMP_PAYSLIP_PAYTYPE_LEAVE_ENCASHMENT%>" && document.salaryPaySlipForm.payType.options[document.salaryPaySlipForm.payType.selectedIndex].text=="<%=PayrollConstants.EMP_PAYSLIP_PAYTYPE_FINAL_SETTLEMENT%>"){
	   if(document.salaryPaySlipForm.grossPay.value!=0 && document.salaryPaySlipForm.grossPay.value!="")
	   	{
			<c:forEach var="pay" items="<%=payScaleHeaderList%>" >
				if(document.getElementById("payScaleName").value=="${pay.name}")
				{
					if(eval(document.salaryPaySlipForm.payHeadAmount[0].value)<"${pay.amountFrom}" ||
					eval(document.salaryPaySlipForm.payHeadAmount[0].value)>"${pay.amountTo}")
					{
						alert("Basic amount should be with in the \n  range of ${pay.amountFrom} - ${pay.amountTo} pay scale amount!!!");
						return false;
					}

				}
			</c:forEach>
		}
	}	*/


	 document.salaryPaySlipForm.action ="${pageContext.request.contextPath}/payslip/modifyGenPaySlips.do?type=modify";

	}
	else if(arg=="saveNew")
	{
		document.salaryPaySlipForm.action ="${pageContext.request.contextPath}/payslip/BeforeviewGenPaySlips.do";
	 }
	 
	//enable all combo boxes before submit
	enableAllDropDowns();
	document.salaryPaySlipForm.payType.disabled=false;
        document.salaryPaySlipForm.submit();	
	///window.opener.location.href="${pageContext.request.contextPath}/inbox/Inbox.jsp";
	
  }
  
  function enableAllDropDowns() {
  	//alert(document.getElementById("paytable").rows.length);
  	var payTablelen = document.getElementById("paytable").rows.length;
  	if(payTablelen>2)
	 {
	 	 for(var i=0;i<document.salaryPaySlipForm.payHead.length;i++)
	 	 {
	 	 	document.salaryPaySlipForm.payHead[i].disabled=false;
	 	 }
	 }
	 else
	 	document.salaryPaySlipForm.payHead.disabled=false;
	 var table = document.getElementById("salAdvances")	
	 if(table.rows.length > 2){
		 if(document.salaryPaySlipForm.salaryAdvances.length>1)
		 {
		 	 for(var i=0;i<document.salaryPaySlipForm.salaryAdvances.length;i++)
		 	 {
		 	 	document.salaryPaySlipForm.salaryAdvances[i].disabled=false;
		 	 }
		 }
	  }
	  else{
	  	 document.salaryPaySlipForm.salaryAdvances.disabled=false;
	  }
	  var tbl = document.getElementById("deductions");
	  //alert(tbl.rows.length);
	  if(tbl.rows.length > 2){
	  	 if(document.salaryPaySlipForm.otherDeductAccountCode.length>1)
		 {
		 	 for(var i=0;i<document.salaryPaySlipForm.otherDeductAccountCode.length;i++)
		 	 {
		 	 	document.salaryPaySlipForm.otherDeductAccountCode[i].disabled=false;
		 	 }
		 }
	  }
	  else{
	  	document.salaryPaySlipForm.otherDeductAccountCode.disabled=false;
	  }
	  	 
  }
  
  function populateYTD(finYearId,month,empCode,obj){
  	var type = "getYtdForPayslip";
  	//alert(finYearId+","+month+","+empCode+","+obj.value);
 	var link = "<%=request.getContextPath()%>"+"/commons/getYTD.jsp?type=" + type+"&finYearId=" + finYearId+"&month=" + month+ "&empCode=" + empCode+"&payhead=" + obj.value ;
 	var request = initiateRequest();
 	var isUnique;
 	request.onreadystatechange = function(){
	 	if (request.readyState == 4){
		 	if (request.status == 200){
			 	var response=request.responseText;
		 		var a = response.split("^");	 			
		 		//alert(a[0]);
		 		var rowObj = getRow(obj);
	   		    var table = document.getElementById("paytable");			    
				getControlInBranch(table.rows[rowObj.rowIndex],'yearToDateHead').value = a[0];
 	    	}
 	  	}
 	};
 	request.open("GET", link, false);
 	request.send(null);
 	return isUnique;
}
  	
  function populateEarningYTD(obj)	{
	var rowObj = getRow(obj);
  	var table = document.getElementById("paytable");  
  	var currentEarning = getControlInBranch(table.rows[rowObj.rowIndex],'payHeadAmount').value ;
	var selectPayhead = getControlInBranch(table.rows[rowObj.rowIndex],'payHead').value;  	  
	 <c:forEach var="earObj" items="<%=paySlip.getEarningses()%>" >
			  if(selectPayhead=="${earObj.salaryCodes.id}") {
			  	if(currentEarning == "")
			  		currentEarning = 0;		  	
				getControlInBranch(table.rows[rowObj.rowIndex],'yearToDateHead').value = eval(currentEarning)+ eval("${earObj.prevYtdAmount}");
			  }
	  </c:forEach> 
  }
  
  function populateAdvanceYTD(obj){
  	var rowObj = getRow(obj);
  	var table = document.getElementById("salAdvances");
  	var currAdv = getControlInBranch(table.rows[rowObj.rowIndex],'salaryAdvancesAmount').value ;
  	var selectAdv = getControlInBranch(table.rows[rowObj.rowIndex],'salaryAdvances').value;  	
  	<c:forEach var="advObj" items="<%=paySlip.getDeductionses()%>" >
  		if("${advObj.salAdvances}" != null ){
			if(selectAdv=="${advObj.salAdvances.id}") {	  		
				getControlInBranch(table.rows[rowObj.rowIndex],'yearToDateAdv').value = eval(currAdv)+ eval("${advObj.prevYtdAmount}");
			}
		}	  
	  </c:forEach> 
  }
  	
  function populateTaxYTD(obj,prevDedYtd){
 	
  	var rowObj = getRow(obj);
  	var table = document.getElementById("deductionTax");
  	
  	var selectDed = getControlInBranch(table.rows[rowObj.rowIndex],'taxTypeName').value ;
 
  	var currDed = getControlInBranch(table.rows[rowObj.rowIndex],'taxTypeAmount').value ;  	  

   
  	<c:forEach var="taxDedObj" items="<%=deductionsTaxList%>" >
                 
				 
                 if(prevDedYtd==0)
				 {
			
				 getControlInBranch(table.rows[rowObj.rowIndex],'yearToDateTax').value = eval(currDed)+ eval(0);
				 }
				 else
				 {
		
				getControlInBranch(table.rows[rowObj.rowIndex],'yearToDateTax').value = eval(currDed)+ eval("${taxDedObj.prevYtdAmount}");
				}
			
		
 		
	  </c:forEach>   
     
    <c:if test ="${fn:length(deductionsTaxList) ==0}">
	   getControlInBranch(table.rows[rowObj.rowIndex],'yearToDateTax').value = eval(currDed)+ eval(0);
     </c:if>
  }
  
  function populateOtherYTD(obj,prevDedYtd)
  
  {
 
  	var rowObj = getRow(obj);
  	var table = document.getElementById("deductionOther");
  	
  	var selectDed = getControlInBranch(table.rows[rowObj.rowIndex],'dedOthrName').value ;
  
  	var currDed = getControlInBranch(table.rows[rowObj.rowIndex],'dedOthrAmount').value ; 

   
<c:forEach var="taxDedObj" items="${deductionOtherList}" >
      
  		if("${taxDedObj.salaryCodes}" != null  ){  			
			
                 if(prevDedYtd==0)
				 {
				 
				getControlInBranch(table.rows[rowObj.rowIndex],'yearToDateOther').value = eval(currDed)+ eval(0);
				 }
				 else
				 {
				
				getControlInBranch(table.rows[rowObj.rowIndex],'yearToDateOther').value = eval(currDed)+ eval("${taxDedObj.prevYtdAmount}");
				}
			
		}	
 		
	  </c:forEach>  	  
	   <c:if test ="${fn:length(deductionOtherList) ==0}">
	   getControlInBranch(table.rows[rowObj.rowIndex],'yearToDateOther').value = eval(currDed)+ eval(0);
     </c:if>
  }
	
  function	populateOtherDedYTD(obj){
  	var rowObj = getRow(obj);
  	var table = document.getElementById("deductions");
  	var selectDed = getControlInBranch(table.rows[rowObj.rowIndex],'otherDeductAccountCode').value ;
  	var currDed = getControlInBranch(table.rows[rowObj.rowIndex],'otherDeductionsAmount').value ;  	
	
  	<c:forEach var="otherDedObj" items="<%=paySlip.getDeductionses()%>" >
  		if("${otherDedObj.chartofaccounts}" != null ){
			if(selectDed == "${otherDedObj.chartofaccounts.glcode}") {  		
				getControlInBranch(table.rows[rowObj.rowIndex],'yearToDateDed').value = eval(currDed)+ eval("${otherDedObj.prevYtdAmount}");
			}
		}	  
	  </c:forEach>   	
  }	
  
  function getPendingAmt(tablename,fieldname,fieldvalue,fieldname1,fieldvalue1,fieldname2,fieldvalue2)
  {
 	var type = "checkingAmount";
 	var link = "<%=request.getContextPath()%>"+"/commons/checkingPendingAmount.jsp?type=" + type+"&tablename=" + tablename+"&fieldname=" + fieldname+ "&fieldvalue=" + fieldvalue+"&fieldname1=" + fieldname1+ "&fieldvalue1=" + fieldvalue1+"&fieldname2=" + fieldname2+ "&fieldvalue2=" + fieldvalue2+ " ";
 	var request = initiateRequest();
 	var isUnique;
 	request.onreadystatechange = function(){
	 	if (request.readyState == 4){
	 	if (request.status == 200){
	 	var response=request.responseText;
	 	var result = response.split("/");
	 	//alert("result "+result[0]);
	
	 		if(result[0]=="true")
	 		{
	
	 			isUnique="true";
	 		}
	 		else if(result[0]=="false")
	 		{
	 			isUnique="false";
	 		}
	
	 	    }
	 	  }
	 	};
	 	request.open("GET", link, false);
	 	request.send(null);
	
	 	return isUnique;
}

	function checkExisting(obj){
		var earningId = document.getElementById("earningId").value;
		//alert(earningId);
		//alert(obj.value);
	}
	
	
	 /**
	    * this will recompute the earnings amount based on basic salary
	    * it will return the earnings amount
	    **/
	    function getEarningAmt(salcodeid)
	    {  
	   	var type = "reComputeEarningAmt";
	   	var basicSal=document.salaryPaySlipForm.basicPay.value;
	   /*	if(document.salaryPaySlipForm.payHeadAmount[0].value==null)
	   	  basicSal=document.salaryPaySlipForm.payHeadAmount.value;
	   	else
	   	  basicSal=document.salaryPaySlipForm.payHeadAmount[0].value; */
	   	var month=document.salaryPaySlipForm.month.value;
	   	var finyr=document.salaryPaySlipForm.year.value;
	   	var totaldays=document.getElementById("workingDays").value;
 		var paiddays=document.getElementById("numDays").value;
	      	var link = "<%=request.getContextPath()%>"+"/commons/checkingPayslip.jsp?basicSal=" + basicSal+"&type="+type+"&month="+month+"&finyr="+finyr+"&salcodeid="+salcodeid+"&totaldays="+totaldays+"&paiddays="+paiddays;
	      	var request = initiateRequest();    
	      	var response;
	      	request.onreadystatechange = function()
	      	{
	     
	  	   if (request.readyState == 4)
	  	   {
	  		if (request.status == 200)
	  		{
	  		    response=request.responseText;
	  		    response=response.split("/")[0];  	
	  		}
	      	   }
	      	};
	      	request.open("GET", link, false);
	      	request.send(null);
	    	return response;
  	}
	
	function calEarningsAmt()
	  {
	        
	          var tableObj=document.getElementById('paytable');
	          var rows=tableObj.rows.length;
	          for(var i=1;i<rows;i++)
	  	  {
	  	    var salcodeid=getControlInBranch(tableObj.rows[i],'payHead').value;
	  	    var caltype=getControlInBranch(tableObj.rows[i],'calculationType').value;
	  	    var pct=getControlInBranch(tableObj.rows[i],'pct').value;
	  	    var pctbasis=getControlInBranch(tableObj.rows[i],'pctBasis').value;
	  	    var amount=0
	  	    if(caltype=="ComputedValue")
	  	    {
	  	        for(var j=1;j<rows;j++)
	  	        { 
		
	  	           if(getControlInBranch(tableObj.rows[j],'payHead').options[getControlInBranch(tableObj.rows[j],'payHead').selectedIndex].text==pctbasis)
	  	           {  	          
	  	              amount=getControlInBranch(tableObj.rows[j],'payHeadAmount').value;
	  	           }
	  	        }
	  	        amount=Math.round((eval(amount)*eval(pct))/100);
	  	    }else if(caltype=="SlabBased")
	  	    {
	  	      amount=getEarningAmt(salcodeid);
	  	    }
	  	    if(amount>0)
	  	    {
	  	      getControlInBranch(tableObj.rows[i],'payHeadAmount').value=amount;
	  	    }
	  	  }
	  	  collectionSum();
	  	  
  	}
	
	function paintdedamts() {
	//alert(document.salaryPaySlipForm.prevTaxYtd.value);
	    calDeductionAmts();	  
	    calEarningsAmt();
	    var i=0;	   
	    
	    if(dedtaxes.length==1)
	    {
	    var amnt = eval(document.salaryPaySlipForm.prevTaxYtd.value);
	    document.salaryPaySlipForm.yearToDateTax.value = amnt + eval(dedtaxes[0]);
	    document.salaryPaySlipForm.taxTypeAmount[i++].value=dedtaxes[0];
	    }else{
	    for(var j=0;j<dedtaxes.length;j++){
	         var amnt = eval(document.salaryPaySlipForm.prevTaxYtd[j].value);
	      //alert(amnt);	
         	 document.salaryPaySlipForm.yearToDateTax[j].value = amnt + eval(dedtaxes[j]);
	         document.salaryPaySlipForm.taxTypeAmount[i++].value=dedtaxes[j];
	    }
	    }
	    for(var j=0;j<dedothers.length;j++){
          var amnt = eval(document.salaryPaySlipForm.prevOtherYtd[j].value);	
          //alert(amnt);
          document.salaryPaySlipForm.yearToDateOther[j].value = amnt + eval(dedothers[j]);
	      document.salaryPaySlipForm.taxTypeAmount[i++].value=dedothers[j];
	    }     
	    netPayCollection();
    }
  
  	function calDeductionAmts(){  
	 	var type = "reComputeDeductionAmts";
	 	//var basicSal=document.salaryPaySlipForm.payHeadAmount[0].value;
	 	var grossamt=document.salaryPaySlipForm.grossPay.value;
	 	var basicSal=document.salaryPaySlipForm.basicPay.value;
	 	var month=document.salaryPaySlipForm.month.value;
 		var finyr=document.salaryPaySlipForm.year.value;
 		var totaldays=document.getElementById("workingDays").value;
 		var paiddays=document.getElementById("numDays").value;
	    	var link = "<%=request.getContextPath()%>"+"/commons/checkingPayslip.jsp?basicSal=" + basicSal+"&type="+type+"&month="+month+"&finyr="+finyr+"&grossamt="+grossamt+"&totaldays="+totaldays+"&paiddays="+paiddays;
	    	var request = initiateRequest();
	    	var isUnique;
	    	request.onreadystatechange = function()
	    	{
	   
		   if (request.readyState == 4)
		   {
			if (request.status == 200)
			{
			    var response=request.responseText;
			    response=response.split("/")[0];
			    var result = response.split("@");
			    dedtaxes=result[0].split("~");
			    
			    dedothers=result[1].split("~");
			}
	    	   }
	    	};
	    	request.open("GET", link, false);
	    	request.send(null);
	  	return isUnique;
	}

	function closeAndRefreshParent(){			
			window.opener.location.href = "payslipInbox.jsp";	
			window.close();	
	}
function checkForUniqueCombo(obj,arg)
{

if(obj.value!="")
{
var dedId;
var curReferNo= obj.value;
var table;
if(arg=="tax")
   {
 table = document.getElementById("deductionTax");
 var rowObj = getRow(obj);
var len = rowObj.rowIndex;

var currDed = getControlInBranch(table.rows[len],'taxTypeName').value;

	if(len  >=  4)
	 {
			for(var i=len-1;i>=4;i--)
			        {

					var taxname = getControlInBranch(table.rows[i],'taxTypeName').value;
					var referNo = getControlInBranch(table.rows[i],'referenceno').value;
						if(currDed == taxname)
								{
					             if(curReferNo == referNo)
					              {
					              alert("The reference number can't be repeated");
								  obj.value="";
								  getControlInBranch(table.rows[len],'referenceno').focus();
					              }
					
					}
			       }
		}
 }
 else if(arg=="ded")
 {
 table = document.getElementById("deductionOther");

 var rowObj = getRow(obj);
var len = rowObj.rowIndex;

var currDed = getControlInBranch(table.rows[len],'dedOthrName').value;

if(len >=  2)
{


			for(var i=len-1; i>=1; i--)
			        {
                    
					var taxname = getControlInBranch(table.rows[i],'dedOthrName').value;
					var referNo = getControlInBranch(table.rows[i],'dedRefNo').value;
						if(currDed == taxname)
								{
					             if(curReferNo == referNo)
					              {
					              alert("The reference number can't be repeated");
								  obj.value="";
								  getControlInBranch(table.rows[len],'referenceno').focus();
					              }
					
					}
			       }
		
 }
	}
}
 }
 function checkAlphaNumeric(obj){
		if(obj.value!=""){
		var num=obj.value;
		var objRegExp  = /^[a-zA-Z - 0-9 .]+$/;
		if(!objRegExp.test(num)){
		alert('Please enter alphanumeric characters only ');
		obj.value="";
		obj.focus();
		}
		}
		}

</script>
</head>


<body onLoad="onBodyLoad();" >


<!-- Header Section Begins -->

<!-- Header Section Ends -->
<!-- Tab Navigation Ends -->

   <html:form method="POST" action="/payslip/generatePaySlips" >      
       
        	<html:hidden name="salaryPaySlipForm" property="delPayhead"/>
        	<html:hidden property="basicPay" value="${paySlip.basicPay}" />
        	<html:hidden property="payslipId"/>
        <!--	<html:hidden property="payType" value="${paySlip.payType}" /> -->

		<table width="95%" border="0" cellspacing="0" cellpadding="0">
		<tr>
		<td></td>
		</tr>
		<tr>
  <table  width="95%" cellpadding ="0" cellspacing ="0" border = "0" id="employee">
  <tr>
		<td colspan="4" class="headingwk"><div class="arrowiconwk">
		<img src="${pageContext.request.contextPath}/common/image/arrow.gif" /></div>
		<div class="headplacer">Modify Payslip</div></td>
		</tr>
	
  <tr>
  <input type="hidden" name="employeeCodeId" id="employeeCodeId" value="${paySlip.employee.idPersonalInformation}">
  	<td class="whiteboxwk">Employee Code</td>
  	<td class="whitebox2wk">
  	<input type="text"   name="employeeCode" id ="employeeCode" value="<c:out value="${paySlip.employee.employeeCode}" escapeXml="false"/>" readonly="readonly"></td>
  	<td class="whiteboxwk">Employee Name</td>
  	<td class="whitebox2wk">
  	<input type="text"   name="employeeName" id ="employeeName" value="<c:out value="${paySlip.employee.employeeFirstName}" escapeXml="false"/>&nbsp;<c:out value="${paySlip.employee.employeeMiddleName}" escapeXml="false"/>&nbsp;<c:out value="${paySlip.employee.employeeLastName}" escapeXml="false"/>" readonly="true"></td>
  </tr>
  <tr>
  <td><div id="codescontainer"></div></td>
  </tr>
  <tr>
    <td class="greyboxwk">Designation</td>
    <td class="greybox2wk">
    <input type="text" name="designation" id ="designation" value="<c:out value="${designation}" escapeXml="false"/>" readonly="readonly"></td>
    <td class="greyboxwk">Year Of Joining</td>

    <fmt:parseDate value="${paySlip.employee.dateOfFirstAppointment}" pattern="yyyy-MM-dd"  var ="asd"/>
 <td class="labelcell">
 <input type="text"  class="fieldcell" name="yearOfJoining" id ="yearOfJoining" value="<fmt:formatDate value="${asd}" pattern="dd/MM/yyyy" dateStyle="full"/>" readonly="readonly"></td>
  </tr>
  <tr>
    <td class="whiteboxwk">Department</td>
     <td class="whitebox2wk">
     <input type="text"  name="department" id ="department" value="${department}" readonly="readonly"/> 
     </td>
     <td class="whiteboxwk">PayScale Name</td>
     <%
	// PayScaleHeaderDAO payScaleHeadersDAO = PayrollDAOFactory.getDAOFactory().getPayScaleHeaderDAO();
	// PayScaleHeader pay12 = (PayScaleHeader) payScaleHeadersDAO.getPayScaleHeaderByEmployeeId(paySlip.getEmployee().getIdPersonalInformation());
	// System.out.println("The pay anme >>>>>>  " + pay12.getName());%>
     <td class="whitebox2wk">
     <input type="text"  name="payScaleName" id ="payScaleName" value="${payStructure.payHeader.name}" readonly="readonly"></td>
    </tr>
  <tr>
   <html:hidden property="effectiveFrom" />
   	<html:hidden property="effectiveTo"/>
    <input type="hidden"  name="year" id ="year" value="${paySlip.financialyear.id}" >
    <td class="greyboxwk">Month/Year</td>
    <td class="greybox2wk">
   
    <input type="text" name="month" id ="month" value="<%=(String)EisManagersUtill.getMonthsStrVsDaysMap().get(new Integer(paySlip.getMonth().intValue()))%>" readonly="readonly">/<input type="text" style="width:80px" class="fieldcell" name="yearRange" id ="yearRange" value="<c:out value="${paySlip.financialyear.finYearRange}" escapeXml="false"/>" readonly="readonly"></td>
    <td class="greyboxwk">
     Pay Type
    </td>
    <td class="greybox2wk">
    <html:select  property="payType"  styleId="payType" styleClass="selectwk">
          <html:options collection="paytypelist" property="id" labelProperty="paytype" />
      </html:select>
    </td>
    </tr>
    <tr>
     <td class="whiteboxwk">Working Days</td>
        <td class="whitebox2wk"><input type="text" name="workingDays" id ="workingDays" value="<c:out value="${paySlip.workingDays}" escapeXml="false"/>" readonly="readonly"></td>
        <td class="whiteboxwk">No of Paid Days</td>        
 	<td class="whitebox2wk">
 	<input type="text"  class="fieldcell" name="numDays" id ="numDays" value="<c:out value="${paySlip.numdays}" escapeXml="false"/>" readonly="readonly"></td>
    </tr>
   
   <br>
   <tr>
		<td colspan="7" class="headingwk"><div class="arrowiconwk">
		<img src="${pageContext.request.contextPath}/common/image/arrow.gif" /></div>
		<div class="headplacer">Earnings</div></td>
		</tr>
 
  <table  width="95%"  cellpadding ="0" cellspacing ="0" border = "0" id="paytable">
  <tr>
    	<td class="whitebox2wk">Pay Head</td>
      <td class="whitebox2wk" >Calculation&nbsp;Type</td>
      <td class="whitebox2wk" >%</td>
      <td class="whitebox2wk" >%Basis</td>
      <td class="whitebox2wk" >Amount</td>
       <td class="whitebox2wk" >Year&nbsp;To&nbsp;Date</td>
      <!--<td class="labelcell">Taxable</p></td>-->
   </tr>
<%BigDecimal grossTotalpay = new BigDecimal(0);
for(int i=0;i<salaryCodes.size();i++)
{
	SalaryCodes sal1 = (SalaryCodes)salaryCodes.get(i);
	for(Iterator iter = paySlip.getEarningses().iterator(); iter.hasNext();)
	{
		Earnings salear = (Earnings) iter.next();
		if(sal1.getId().equals(salear.getSalaryCodes().getId())){
			grossTotalpay = grossTotalpay.add(salear.getAmount());
	%>
  		<tr id="earnings" >
     	 <td class="greybox2wk" >
     	   <input type="hidden" name="earningId" id="earningId" value="<%= salear.getId()%>" />
							<html:select styleClass="selectwk" property="payHead"  
							onchange="checkDuplicate(this);populateEarningYTD(this);" 
							onblur="checkPctBasis(this);trim(this,this.value)" disabled="true">
							<html:option value="0">Choose</html:option>
							<%for(int j=0;j<salaryCodes.size();j++)
							{
							SalaryCodes sal2 = (SalaryCodes)salaryCodes.get(j);
							if(sal2.getCategoryMaster().getCatType().equals("E")){%>
							<option value="<%=sal2.getId()%>" <%=((sal1.getId()==sal2.getId())? "selected":"")%>><%=sal2.getHead()%></option>
							<%}
							}%>
							</html:select>
     	   </td>
     	 <td class="greybox2wk">
		 <html:text styleClass="selectwk" property="calculationType"  value="<%=salear.getSalaryCodes().getCalType()%>" 
		 readonly="true"/>
  		 </td>
  		 <%
		 if(salear.getSalaryCodes().getCalType().equals("ComputedValue"))
	  		 {%>
	     	 <td class="greybox2wk">
			 <input type="text"  name="pct" id="pct" value="<%if(salear.getPct()!=null){%><%=salear.getPct().toString()%><%}else{%>""<%}%>" 
			 onblur="calAmount(this);trim(this,this.value);populateEarningYTD(this);" >
			 </td>
	     	  <%}
		  else
	  		 {%>
	  		 <td class="greybox2wk">
			 <input type="text"  name="pct" id="pct" value="<%if(salear.getPct()!=null){%>
			 <%=salear.getPct().toString()%><%}else{%>""<%}%>"  
			 onblur="calAmount(this);trim(this,this.value);populateEarningYTD(this);" readonly="readonly" /></td>
	  		 <%}%>
     	
		  <td class="greybox2wk" >
     		<input type="text"  name="pctBasis" id="pctBasis"
     		 value="<%if(salear.getSalaryCodes().getSalaryCodes()!=null){%><%=salear.getSalaryCodes().getSalaryCodes().getHead()%>
     		 <%}else{%>""<%}%>"
			 readonly="readonly" >
  		 </td>
  		  <% 
		  if(salear.getSalaryCodes().getCalType().equals("ComputedValue"))
  		 {
		 %>
	  		 <td class="greybox2wk" >
	  		 <input  type="text" name="payHeadAmount"   value="<%=salear.getAmount().toString()%>" 
			 onfocus="return removeZero(this)" onchange="return checkdecimalval(this,this.value)"  onblur="addZero(this);trim(this,this.value);
	  		 populateEarningYTD(this);" readonly="true"/>
	  		 </td>
  		 <%}
  		 else
  		 {%>
				<td class="greybox2wk" >
				<input  type="text" name="payHeadAmount"   value="<%=salear.getAmount().toString()%>" onfocus="return removeZero(this)" 
				onchange="return checkdecimalval(this,this.value)"  onblur="addZero(this);trim(this,this.value);
				populateEarningYTD(this);" />
				</td>
  		 <%}
		 %>
  		 <%
  		 BigDecimal ytdEarning = salear.getPrevYtdAmount();
  		 System.out.println("Earning Ytd"+ytdEarning);
  		 ytdEarning = ytdEarning.add(salear.getAmount());
  		 
  		
  		%>
  		 <td class="greybox2wk" >
  		 <html:text styleClass="selectwk" property="yearToDateHead" value="<%=ytdEarning.toString()%>" 
  		  readonly="true"/></td>
  		<!--  <td class="greybox2wk" width="20%" ><p>
		      	 <input  onclick="whichButtonEarnings('paytable',this,'earnings');" style="WIDTH: 15px; HEIGHT: 22px" 
		      	 type="button" value="+" name="addF"> 
		      	 <input  onclick="deleteRow('paytable',this);" style="WIDTH: 15px; HEIGHT: 22px" 
		      	 type="button" value="-" name="dDelF" ></p>
     	         </td>-->
     	          <td class="greyboxwk">
             <a href="#"><img src="<%=request.getContextPath()%>/common/image/add.png" alt="Add" width="16" height="16"
                					   border="0" onclick="whichButtonEarnings('paytable',this,'earnings');" /></a>
			 <a href="#"><img src="<%=request.getContextPath()%>/common/image/cancel.png" alt="Delete" width="16" height="16"
			                           border="0" onclick="deleteRow('paytable',this);" /></a>
                					 </td>
     	 <!--<td class="labelcell">&nbsp;<input  type="checkbox" name="isTaxable" id="isTaxable" style="width:50px"></td>-->
   </tr>
   <%}
   }
   }%>
   </table>
   <br>
     <table  width="95%"  cellpadding ="0" cellspacing ="0" border = "0" id="deductionTax">
		       <tr>
		       <td class="whiteboxwk">GROSS PAY</td>
		       <td class="whitebox2wk" >
		       <html:text styleClass="selectwk" property="grossPay" value="<%=grossTotalpay.toString()%>" 
		       onblur="return netPayCollection()" readonly="true"/>
		       </td>
		       
			   
				<td class="whiteboxwk" >
				<html:button property="recompute" value="Recompute Deductions" onclick ="paintdedamts()" styleClass="buttonfinal"/>
				</td>
				</tr>
				<br>
	    <tr>
		<td colspan="7" class="headingwk"><div class="arrowiconwk">
		<img src="${pageContext.request.contextPath}/common/image/arrow.gif" /></div>
		<div class="headplacer">Deductions</div></td>
		</tr>  
       
				<tr>
				<td class="greybox2wk" width="25%" >Taxes</td>
				<td class="greybox2wk" width="25%" >Amount</td>
				<td class="greybox2wk" width="25%" >ReferenceNo</td>
				<td class="greybox2wk" width="25%" >Year&nbsp;To&nbsp;Date</td>
				<td class="greybox2wk" width="25%"><b>Add/Del</b></td>
				</tr>
						<c:choose>
						<c:when test ="${flagForDeductionTax =='valTaxDed'}">

						<c:forEach var="dedu" items="${deductionsTaxList}">	 
						
						<tr>
						
						<input type="hidden" name="otherDedTxDedId" id="otherDedTxDedId" value="${dedu.id}"/>
                        <input type="hidden" name="prevTaxYtd" value="${dedu.prevYtdAmount}" />
                        
								<td class="whitebox3wk" >
								<html:select property="taxTypeName" styleClass="selectwk" value="${dedu.salaryCodes.id}" > 
									<html:option value="">-----------Choose-----------</html:option>					
									<c:forEach var="salCode" items="${ salcodesTax}" > 
									<html:option value="${salCode.id}">${salCode.head}</html:option>
									</c:forEach>
								</html:select>
								</td>
								
								<td class="whitebox3wk">
									<input class="selectwk" type="text" name="taxTypeAmount" 
									 value="${dedu.amount}"id="taxTypeAmount"
										onfocus="return removeZero(this)" onchange="return checkdecimalval1(this,this.value)"  
										onblur="netPayCollection();trim(this,this.value);
										populateTaxYTD(this,${dedu. prevYtdAmount});">
								</td>
				  
								<td class="whitebox3wk" >
									<input class="selectwk" type="text"  name="referenceno" id="referenceno" value="${dedu.referenceno}" 
									onblur="checkForUniqueCombo(this,'tax');checkAlphaNumeric(this);"/>
								</td> 
				 
								<td class="whitebox3wk">
									<html:text styleClass="selectwk" property="yearToDateTax" 
									value="${dedu.prevYtdAmount+dedu.amount}"   readonly="true"/>
								</td>
				 
								 <td class="whitebox3wk">
             <a href="#"><img src="<%=request.getContextPath()%>/common/image/add.png" alt="Add" width="16" height="16"
                					   border="0" onclick="whichButtonTaxDeductions('deductionTax',this,'taxDeductions');" /></a>
			 <a href="#"><img src="<%=request.getContextPath()%>/common/image/cancel.png" alt="Delete" width="16" height="16"
			                           border="0" onclick="deleteRow('deductionTax',this);" /></a>
                					 </td>

					</tr>
					
					</c:forEach>
					</c:when>
					<c:otherwise>
					
					<tr>
						
							<input type="hidden" name="otherDedTxDedId" id="otherDedTxDedId" value="0"/>
				 <input type="hidden" name="prevTaxYtd" value="0" />
								<td class="whitebox3wk" >
								<html:select property="taxTypeName" styleClass="selectwk" > 
								<html:option value="">-----------Choose-----------</html:option>					
								<c:forEach var="salCode" items="${ salcodesTax}" > 
								<html:option value="${salCode.id}">${salCode.head}</html:option>
								</c:forEach>
								</html:select>
								</td>
								<td class="whitebox3wk">
								<input class="selectwk" type="text" name="taxTypeAmount"  id="taxTypeAmount"  value=""
								onfocus="return removeZero(this)" onchange="return checkdecimalval1(this,this.value)"  
								onblur="netPayCollection();trim(this,this.value);
								populateTaxYTD(this,0);">
								</td>
								<td class="whitebox3wk" >
									<input type="text" class="selectwk" name="referenceno" id="referenceno" value="${dedu.referenceno}" 
									onblur="checkForUniqueCombo(this,'ded');checkAlphaNumeric(this);"/>
									</td>
								<td class="whitebox3wk">
								<html:text styleClass="selectwk" property="yearToDateTax" readonly="true"/>
								</td>
				                <td class="whitebox3wk">
             						<a href="#"><img src="<%=request.getContextPath()%>/common/image/add.png" alt="Add" width="16" height="16"
                					   border="0" onclick="whichButtonTaxDeductions('deductionTax',this,'taxDeductions');" /></a>
			 						<a href="#"><img src="<%=request.getContextPath()%>/common/image/cancel.png" alt="Delete" width="16" height="16"
			                           border="0" onclick="deleteRow('deductionTax',this);" /></a>
                					 </td>
									
				                
								<!--  <td class="greybox2wk"  ><p>
								<input  onclick="whichButtonTaxDeductions('deductionTax',this,'taxDeductions');" 
								 type="button" value="+" name="addF"> 
								<input  onclick="deleteRow('',this);"  type="button" value="-" name="dDelF" ></p>
								</td>-->

						</tr>
						
						</c:otherwise>
						</c:choose>	
						</table>
         <table  width="95%"  cellpadding ="0" cellspacing ="0" border = "0" id="deductionOther" >
                
		        
                <tr>
				<td class="greybox2wk" width="25%">Deduction-Other</td>
				<td class="greybox2wk" width="25%" >Amount</td>
				<td class="greybox2wk"  width="25%">ReferenceNo</td>
				<td class="greybox2wk" width="25%" >Year&nbsp;To&nbsp;Date</td>
				<td class="greybox2wk"width="25%">Add/Del</td>
				</tr>
				
						<!--  <tr>
						<td class="whiteboxwk"  >Deduction-Other</td>
						<td class="whiteboxwk"  >Amount</td>
						<td class="whiteboxwk"  >ReferenceNo</td>
						<td class="whiteboxwk"  >Year&nbsp;To&nbsp;Date</td>
						</tr> -->      
						    <c:choose>
						    <c:when test ="${flagForDeductionOthr =='valTaxOthr'}">

							<c:forEach var="dedu" items="${deductionOtherList}">	
							<tr>
						
							<input type="hidden" name="dedOthrTxId" id="dedOthrTxId" value="${dedu.id}"/>

									<td class="whitebox3wk" >
									<html:select property="dedOthrName" styleClass="selectwk"   value="${dedu.salaryCodes.id}" > 
									<html:option value="">-----------Choose-----------</html:option>					
									<c:forEach var="salCode" items="${salcodesDedOther}" > 
									<html:option value="${salCode.id}">${salCode.head}</html:option>
									</c:forEach>
									</html:select>
									</td>
									
									<td class="whitebox3wk">
									<input class="selectwk" type="text" name="dedOthrAmount" value="${dedu.amount}"id="dedOthrAmount"
									onfocus="return removeZero(this)" onchange="return checkdecimalval1(this,this.value)"  
									onblur="netPayCollection();trim(this,this.value);
									populateOtherYTD(this,${dedu. prevYtdAmount});">
									</td>
									
									<td class="whitebox3wk" >
									<input type="text" class="selectwk" name="dedRefNo" id="dedRefNo" value="${dedu.referenceno}" 
									onblur="checkForUniqueCombo(this,'ded');checkAlphaNumeric(this);"/>
									</td> 
									 
									<td class="whitebox3wk">
									<html:text styleClass="selectwk" property="yearToDateOther" value="${dedu.prevYtdAmount+dedu.amount}" 
									  readonly="true"/>
									</td>
									 <td class="whitebox3wk">
             							<a href="#"><img src="<%=request.getContextPath()%>/common/image/add.png" alt="Add" width="16" height="16"
                					   border="0" onclick="whichButtonTaxDeductions('deductionOther',this,'taxDeductionsOth');" /></a>
			 							<a href="#"><img src="<%=request.getContextPath()%>/common/image/cancel.png" alt="Delete" width="16" height="16"
			                           border="0" onclick="deleteRow('deductionOther',this);" /></a>
                					 </td>
									<!--  <td class="greybox2wk"  ><p>
									<input  onclick="whichButtonTaxDeductions('deductionOther',this,'taxDeductionsOth');" style="WIDTH: 20px; HEIGHT: 22px" type="button" value="+" name="addF"> 
									<input  onclick="deleteRow('deductionOther',this);" style="WIDTH: 20px; HEIGHT: 22px" type="button" value="-" name="dDelF" ></p>
									</td>-->

								</tr>
								</c:forEach>
								</c:when>
								<c:otherwise>
								
								
							<tr>
						
						        <input type="hidden" name="dedOthrTxId" id="dedOthrTxId" value="0"/>
								<td class="whitebox3wk" >
								<html:select property="dedOthrName" styleClass="selectwk" > 
								<html:option value="">-----------Choose-----------</html:option>					
								<c:forEach var="salCode" items="${ salcodesDedOther}" > 
								
								<html:option value="${salCode.id}">${salCode.head}</html:option>
								</c:forEach>
								</html:select>
								</td>

								<td class="whitebox3wk">
								<input class="selectwk" type="text" name="dedOthrAmount"  id="dedOthrAmount"  value=""
								onfocus="return removeZero(this)" onchange="return checkdecimalval1(this,this.value)"  
								onblur="netPayCollection();trim(this,this.value);
								populateTaxYTD(this,0);">
								</td>
				  
								<td class="whitebox3wk" >
									<input type="text" class="selectwk" name="dedRefNo" id="dedRefNo" value="${dedu.referenceno}" 
									onblur="checkForUniqueCombo(this,'ded');checkAlphaNumeric(this);"/>
									</td>
				 
								<td class="whitebox3wk">
								<html:text styleClass="selectwk" property="yearToDateOther" readonly="true"/>
								</td>
				                <td class="whitebox3wk">
             <a href="#"><img src="<%=request.getContextPath()%>/common/image/add.png" alt="Add" width="16" height="16"
                					   border="0" onclick="whichButtonTaxDeductions('deductionOther',this,'taxDeductionsOth');" /></a>
			 <a href="#"><img src="<%=request.getContextPath()%>/common/image/cancel.png" alt="Delete" width="16" height="16"
			                           border="0" onclick="deleteRow('deductionOther',this);" /></a>
                					 </td>
									
				                
								<!--  <td class="greybox2wk"  ><p>
								<input  onclick="whichButtonTaxDeductions('deductionTax',this,'taxDeductions');" 
								 type="button" value="+" name="addF"> 
								<input  onclick="deleteRow('',this);"  type="button" value="-" name="dDelF" ></p>
								</td>-->

						</tr>
							
							</c:otherwise>
							</c:choose>
							</table>
<%

BigDecimal dedTotalpay = new BigDecimal(0);
BigDecimal netTotalpay = new BigDecimal(0);
System.out.println("the size othe tax list ::: ???????????????????????????? " + deductionsTaxList.size());
%>
   
   
   
    <table  width="95%"  cellpadding ="0" cellspacing ="0" border = "0" id="salAdvances">
   
     <tr>
	<td colspan="9" class="headingwk"><div class="arrowiconwk">
		<img src="${pageContext.request.contextPath}/common/image/arrow.gif" /></div>
		<div class="headplacer">ADVANCE&nbsp;ADJUSTMENTS</div></td>
		</tr>
<%
 System.out.println("the deductionsAdvList.size()  >>>>>>>>   "  +deductionsAdvList.size());
 Advance salAdv=null;
if(deductionsAdvList.size()!=0)
{
	for(Iterator iter4 = deductionsAdvList.iterator(); iter4.hasNext();)
   {
	   BigDecimal pendingAmount = new BigDecimal(0);
       Deductions salDed1 = (Deductions) iter4.next();
       System.out.println("the salAdvances.size()  >>>>>>>>   "  +salAdvances.size());%>
	   <tr id="advDeductions">
			

   			
   				<td class="whitebox2wk" width="35%">
			 <input type="hidden" name="advDedId" id="advDedId" value="<%= salDed1.getId()%>" />
       		<html:select styleClass="selectwk" property="salaryAdvances"  
	       		onchange="populateInstAmount(this);populateAdvanceYTD(this);" onblur="checksalAdvances(this)" >
       			<html:option value="0">Choose</html:option>
 		<%
 		for(int i=0;i<salAdvances.size();i++)
		{
		  salAdv = (Advance)salAdvances.get(i);
		   %>
		   <%
		   System.out.println("the value paySlip.getEmployee().getIdPersonalInformation() >>>>>>>>   "  +paySlip.getEmployee().getIdPersonalInformation());
		   System.out.println("the value salAdv.getEmployee().getIdPersonalInformation() >>>>>>>>   "  +salAdv.getEmployee().getIdPersonalInformation());
		   System.out.println("the value salDed1.getSalAdvances().getId() >>>>>>>>   "  +salDed1.getSalAdvances().getId());
		   System.out.println("the value  salAdv.getId() >>>>>>>>   "  +salAdv.getId());
		   
		   if(salDed1.getSalAdvances().getId().equals(salAdv.getId())){
			   pendingAmount = salAdv.getPendingAmt().add(salDed1.getAmount());
			   System.out.println("the value  salAdv.getPendingAmt() >>>>>>>>   "  +salAdv.getPendingAmt());
			   System.out.println("the value  salDed1.getAmount() >>>>>>>>   "  +salDed1.getAmount());
			   System.out.println("the value  pendingAmount >>>>>>>>   "  +pendingAmount);
			   System.out.println("the value  >>>>>>>>   "  +salAdv.getPendingAmt());
			   System.out.println("the value  >>>>>>>>   "  +salAdv.getMaintainSchedule() );
			   dedTotalpay = dedTotalpay.add(salDed1.getAmount());
		   }	   
		   
			 if(paySlip.getEmployee().getIdPersonalInformation().equals(salAdv.getEmployee().getIdPersonalInformation()) )
			 {
				  
		  %>
       		<option  value="<%=salAdv.getId() %>" <%=((salAdv.getId().equals(salDed1.getSalAdvances().getId()))?
       		 "selected":"")%>><%= salAdv.getSalaryCodes().getHead()+" ~ "+ salAdv.getAdvanceAmt()+" ~ "+salAdv.getId() %></option>
			<%}
			}%>
       		</html:select>
       		</td>
          	
        	<% if("Y".equals(salAdv.getMaintainSchedule()))
          	{	%>
					 <td class="whitebox2wk" width="25%">
				<% if(salDed1.getAdvanceScheduler() != null){	%>
					 <input type="hidden" name="advanceSchedule" id="advanceSchedule" 
					 value="<%= salDed1.getAdvanceScheduler().getId().toString() %>">
				<% } else{	%>
					<input type="hidden" name="advanceSchedule" id="advanceSchedule" value="">
				<%	} %>
				 <input type="hidden" name="salaryAdvPendingAmount" id="salaryAdvPendingAmount" value="<%=pendingAmount.toString()%>">
				 <input class="whitebox3wk" type="text" name="salaryAdvancesAmount" style="width:100px" 
				 value="<%=salDed1.getAmount().toString()%>" onfocus="return removeZero(this)" 
				 onchange="return checkdecimalval1(this,this.value)" 
				 onblur="checkAmount(this);netPayCollection();trim(this,this.value);populateAdvanceYTD(this);" readOnly="true" />
				 <br><br></td>
				  <%
        	 BigDecimal ytdSalAdv = salDed1.getPrevYtdAmount();
			 ytdSalAdv = ytdSalAdv.add(salDed1.getAmount());			 
        	
        	 %>
        	 
			
			<td class="whitebox2wk" width="25%">
			<html:text styleClass="selectwk" property="yearToDateAdv" value="<%=ytdSalAdv.toString()%>" readonly="true"/>
			<br><br></td>
			<td class="whitebox2wk" width="25%">
			    
			</td>
			
				<% } else{	%>
					 <td class="whitebox3wk" width="25%">
				<% if(salDed1.getAdvanceScheduler() != null){	%>
					 <input type="hidden" name="advanceSchedule" id="advanceSchedule" 
					 value="<%= salDed1.getAdvanceScheduler().getId().toString() %>">
				<% } else{	%>
					<input type="hidden" name="advanceSchedule" id="advanceSchedule" value="">
				<%	} %>
				 <input type="hidden" name="salaryAdvPendingAmount" id="salaryAdvPendingAmount" value="<%=pendingAmount.toString()%>">
				 <input class="selectwk" type="text" name="salaryAdvancesAmount" style="width:100px" 
				 value="<%=salDed1.getAmount().toString()%>" onfocus="return removeZero(this)" 
				 onchange="return checkdecimalval1(this,this.value)" 
				 onblur="checkAmount(this);netPayCollection();trim(this,this.value);populateAdvanceYTD(this);" />
				 </td>
				 
				 <%
        	 BigDecimal ytdSalAdv = salDed1.getPrevYtdAmount();
			 ytdSalAdv = ytdSalAdv.add(salDed1.getAmount());			 
        	
        	 %>
        	 
			
			<td class="whitebox2wk" width="25%">
			<html:text styleClass="selectwk" property="yearToDateAdv" value="<%=ytdSalAdv.toString()%>" />
			<br><br></td>
				<%	} %>
				<td class="whitebox2wk" width="25%">
			
			</td>
			 <td class="whitebox2wk" width="25%">
			    
			</td>
			<td class="whitebox2wk" width="25%">
             <a href="#"><img src="<%=request.getContextPath()%>/common/image/add.png" alt="Add" width="16" height="16"
                					   border="0" onclick="whichButtonEarningsDeductions('salAdvances',this,'advDeductions');" /></a>
			 <a href="#"><img src="<%=request.getContextPath()%>/common/image/cancel.png" alt="Delete" width="16" height="16"
			                           border="0" onclick="deleteRow('salAdvances',this);" /></a>
                					 </td>
			
			
			
    		</tr>
    	<%}
	  }
 // }
//}
else
   {
   %>
   <tr id="advDeductions">
   
            
   			<input type="hidden" name="advDedId" id="advDedId" value="0"/>
   			<td class="whitebox3wk" width="24%">
			<html:select styleClass="selectwk" property="salaryAdvances" 
			onchange="populateInstAmount(this);populateAdvanceYTD(this);" onblur="checksalAdvances(this);">
					<html:option value="0">-----------Choose-----------</html:option>
					<%
						for(int i=0;i<salAdvances.size();i++)
						{
							Advance adv = (Advance)salAdvances.get(i);
							if(adv.getPendingAmt().compareTo(BigDecimal.ZERO)==1)
							{	
					%>
							<html:option value="<%=adv.getId().toString()%>"><%=adv.getSalaryCodes().getHead() %></html:option>
					<%
							}
   						}
					%>		
			</html:select>
			</td>
			
				
			  <input type="hidden" name="salaryAdvPendingAmount" id="salaryAdvPendingAmount" >
			  <input type="hidden" name="advanceSchedule" id="advanceSchedule" value="">
			 <td class="whitebox3wk" width="25%">
			 <html:text styleClass="selectwk"  property="salaryAdvancesAmount"    onfocus="return removeZero(this)" 
			 onchange="return checkdecimalval1(this,this.value)"  onblur="return netPayCollection();trim(this,this.value)" />
			 </td>
			 
			
			 <td class="whitebox3wk" width="25%">
		 <html:text styleClass="selectwk" property="yearToDateAdv" value=""  /></td>
			 <!--  <td class="whiteboxwk"  ><p>
				 <input  onclick="whichButtonEarningsDeductions('salAdvances',this,'advDeductions');" style="WIDTH: 20px; HEIGHT: 22px" type="button" value="+" name="addF"> 
				 <input  onclick="deleteRow('salAdvances',this);" style="WIDTH: 20px; HEIGHT: 22px" type="button" value="-" name="dDelF" ></p>
     	 		 </td>-->
     	 		
     	 		<td class="whitebox2wk" width="25%">
			    
			</td>
     	 		 <td class="whitebox3wk">
             <a href="#"><img src="<%=request.getContextPath()%>/common/image/add.png" alt="Add" width="16" height="16"
                					   border="0" onclick="whichButtonEarningsDeductions('salAdvances',this,'advDeductions');" /></a>
			 <a href="#"><img src="<%=request.getContextPath()%>/common/image/cancel.png" alt="Delete" width="16" height="16"
			                           border="0" onclick="deleteRow('salAdvances',this);" /></a>
                					 </td>
                					 
     	 		 
   </tr>
   <%}%>
 </table>
 <br>
   
   
    <table  width="95%"  cellpadding ="0" cellspacing ="0" border = "0" id="deductions">
    <tr>
		<td colspan="7" class="headingwk"><div class="arrowiconwk">
		<img src="${pageContext.request.contextPath}/common/image/arrow.gif" /></div>
		<div class="headplacer">OTHER&nbsp;DEDUCTIONS</div></td>
		</tr>
   
     <%System.out.println("if(deductionsotherList.size()!=0)   "  + deductionsotherList.size());
     if(deductionsotherList.size()!=0)
     {
		 for(Iterator iter5 = deductionsotherList.iterator(); iter5.hasNext();)
	  {
       		Deductions salDed2 = (Deductions) iter5.next();
			dedTotalpay = dedTotalpay.add(salDed2.getAmount());%>
			  <tr id="otherDed" >
			  <input type="hidden" name="otherDedId" id="otherDedId" value="<%= salDed2.getId() %>"/>
			 <input type="hidden" name="accountCodeId" id="accountCodeId" value="<%=salDed2.getChartofaccounts().getId().toString()%>" />
			 <td class="whitebox3wk">
			 <input type="text"  class="selectwk" name="otherDeductAccountCode" id ="otherDeductAccountCode" 
			 value="<%=salDed2.getChartofaccounts().getGlcode().toString() %>" autocomplete="off"   onkeyup="autocompleteDeduc(this,event);" 
			 onblur="fillNeibrAfterSplit1(this,'accountCodeId');checkOtherDedCode(this);"
			 onchange="populateOtherDedYTD(this);"  />
			</td>
			 <td class="whitebox2wk">
			 <input type="text" 
			  class="selectwk" name="otherDeductAccountDescription" id ="otherDeductAccountDescription"
			   value="<%=salDed2.getChartofaccounts().getName() %>" >
			</td>
			 <td class="whitebox2wk">
			 <input class="selectwk" type="text" name="otherDeductionsAmount" value="<%=salDed2.getAmount().toString() %>" 
			 onfocus="return removeZero(this)" onchange="return checkdecimalval1(this,this.value)"  
			 onblur="netPayCollection();trim(this,this.value);
			 populateOtherDedYTD(this);"/>
			 </td>
 			<%
 			BigDecimal ytdOtherDed = salDed2.getPrevYtdAmount();
			ytdOtherDed = ytdOtherDed.add(salDed2.getAmount());
 			
 			
 			%>
			<td class="whitebox2wk">
                  <a href="#"><img src="<%=request.getContextPath()%>/common/image/add.png" alt="Add" width="16" height="16"
                					   border="0" onclick="whichButtonEarnings2('deductions',this,'otherDed');" /></a>
			<a href="#"><img src="<%=request.getContextPath()%>/common/image/cancel.png" alt="Delete" width="16" height="16"
			 border="0" onclick="deleteRow('deductions',this);" /></a>
                 </td>
		  </tr>
		<%}
	   }
  	     else
		 {%>
				 <tr id="otherDed" >
				 <input type="hidden" name="accountCodeId" id="accountCodeId" >
				 <input type="hidden" name="otherDedId" id="otherDedId" value="0" />
				 <td class="whitebox2wk">
				 <input type="text"  class="selectwk" name="otherDeductAccountCode" 
				 id ="otherDeductAccountCode"  autocomplete="off"   onkeyup="autocompleteDeduc(this,event);" 
				 	onblur="fillNeibrAfterSplit1(this,'accountCodeId');checkOtherDedCode(this);
				 	trim(this,this.value)"  >
				</td>
				 <td class="whitebox2wk">
				 <input type="text"  class="selectwk" name="otherDeductAccountDescription" id ="otherDeductAccountDescription"  readonly="true">
				</td>
				 <td class="whitebox2wk">
				 <html:text styleClass="selectwk"   property="otherDeductionsAmount"  
				 onfocus="return removeZero(this)" onchange="return checkdecimalval1(this,this.value)" 
				  onblur="return netPayCollection();trim(this,this.value)"/></td>
				 <td class="whitebox2wk"><html:text styleClass="selectwk" property="yearToDateDed" value="" 
				  readonly="true"/></td>
				  
				  <td class="whitebox2wk">
                  <a href="#"><img src="<%=request.getContextPath()%>/common/image/add.png" alt="Add" width="16" height="16"
                					   border="0" onclick="whichButtonEarnings2('deductions',this,'otherDed');" /></a>
			<a href="#"><img src="<%=request.getContextPath()%>/common/image/cancel.png" alt="Delete" width="16" height="16"
			 border="0" onclick="deleteRow('deductions',this);" /></a>
                 </td>
				  
				 <!--  <td class="whiteboxwk"  ><p>
				 	 <input  onclick="" style="WIDTH: 20px; HEIGHT: 22px" type="button" value="+" name="addF"> 
				 	 <input  onclick="" style="WIDTH: 20px; HEIGHT: 22px" type="button" value="-" name="dDelF" ></p>
 				 </td>-->
				</tr> 
			<%
   		}
      netTotalpay=grossTotalpay.subtract(dedTotalpay); %>
  </table>
  <br>
  
   <table  width="95%" cellpadding ="0" cellspacing ="0" border = "0" >
   <tr>
   		<td class="whiteboxwk"  >TOTAL&nbsp;DEDUCTIONS</td>
        <td class="whitebox2wk"  >
        <input class="selectwk" type="text" name="totalDeductions"  id="totalDeductions" value="<%=dedTotalpay.toString()%>" readonly="true">
        </td>
   </tr>
   <tr>
       <td class="greyboxwk">NET PAY</td>
        <td class="greybox2wk" >
        &nbsp;<html:text styleClass="selectwk" property="netPay"  value="<%=netTotalpay.toString()%>" readonly="true"/></td>
  </tr>
  
  <tr>
    	<td  class="whiteboxwk" >
    	<span class="mandatory">*</span><b>Remarks</b></td>
    	<td  class="whitebox2wk" colspan="2">
		<html:textarea rows="5" cols="20" property="modifyRemarks" />
		</td>
		</td>
        <td class="whitebox2wk"><div class="buttonholderwk">
		<input type="button" class="buttonfinal" name="save" value="Save & View" onclick="return validation('save');"/>
		</div></td>
		
	    <td class="whitebox2wk"><div class="buttonholderwk">
		<input type="button" class="buttonfinal" name="cancel" value="Cancel" onclick="history.go(0)"/>
		</div></td>
        
        </tr>
  </table>
  
       
       
        </tr>
		</table>
	
</html:form>

<% session.removeAttribute("paySlip");
session.removeAttribute("deductionsTaxList");
session.removeAttribute("deductionsAdvList");
session.removeAttribute("deductionsotherList");

 %>

</body>
</html>

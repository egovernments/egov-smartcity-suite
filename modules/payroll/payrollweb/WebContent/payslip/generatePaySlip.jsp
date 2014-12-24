<%@ include file="/includes/taglibs.jsp" %>
<%@ page language="java" import="java.util.*,
org.egov.infstr.utils.*,org.egov.payroll.model.*,
org.egov.payroll.utils.PayrollManagersUtill,
 org.egov.payroll.services.payslip.PayRollService,
 org.egov.payroll.client.payslip.*,
 java.text.SimpleDateFormat,
 org.egov.infstr.commons.dao.*,
 org.egov.payroll.utils.PayrollConstants" %>

<html>

<head>

	<title>Generate Payslip</title>
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
<% session.removeAttribute("payHeader"); 
String payslipWfType = GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getAppConfigValueByDate("Payroll","PayslipWorkflow",new java.util.Date()).getValue();
%>
<script language="JavaScript"  type="text/JavaScript">
		var empCodeArray;
		var selectedEmpCode;
		var acctCodeArray;
		var selectedAcctCode;
		var yuiflag = new Array();
		var yuiflag1 = new Array();
		var empId=0;
		var totalwrkdays=0;
		var paiddays=0;
		var basicsal=0;
		var noofdaysmonth=0;
		var noofabsents=0;
		var dedtaxes=new Array();
		var dedothers=new Array();
		
		
function onBodyLoad()
 {  
 <%
 String msg=(String)request.getAttribute("alertMessage");
 if(msg!=null)
 {
 %>
 alert("<%=msg%>");
 window.close();
 }
 <!-- fallowing lines of code to close the script,body tags ,BZ in case of runtime exception no need of running total JSP -->
 </script>
 <body onLoad="onBodyLoad();"/>
 <!-- above lines of code to close the script,body tags ,BZ in case of runtime exception no need of running total JSP -->
 <%
 }else{
 %>
 
   loadAccountCodes();
   collectionSum();
   netPayCollection();
   document.salaryPaySlipForm.month.disabled="true";
   document.salaryPaySlipForm.year.disabled="true";
   document.getElementById("employeeName").value="<%=(String)(request.getParameter("employeeName")!=null?request.getParameter("employeeName"):request.getAttribute("employeeName"))%>";
   document.getElementById("designation").value="<%=(String)(request.getParameter("designation")!=null?request.getParameter("designation"):request.getAttribute("designation"))%>";
   document.getElementById("department").value="<%=(String)(request.getParameter("department")!=null?request.getParameter("department"):request.getAttribute("department"))%>";
   document.getElementById("yearOfJoining").value="<%=(String)(request.getParameter("yearOfJoining")!=null?request.getParameter("yearOfJoining"):request.getAttribute("yearOfJoining"))%>";
   document.getElementById("payScaleName").value="<%=(String)(request.getParameter("payScaleName")!=null?request.getParameter("payScaleName"):request.getAttribute("payScaleName"))%>";
   document.getElementById("employeeCode").value="<%=(String)(request.getParameter("empcode")!=null?request.getParameter("empcode"):request.getAttribute("empcode"))%>";
	<%
		ArrayList payScaleHeaderList=(ArrayList)session.getAttribute("payScaleHeaderList");		
		ArrayList salaryCodes=(ArrayList)session.getAttribute("salaryCodes");		
		ArrayList salAdvances=(ArrayList)session.getAttribute("salAdvances");		
		List deductionsotherList = (List)session.getAttribute("otherDedList");
	%>
	noofdaysmonth="<%=request.getAttribute("noofdaysmonth") %>";
	noofabsents="<%=request.getAttribute("noofabsents") %>";
	document.getElementById("noofdaysmonth").value=noofdaysmonth;
	document.salaryPaySlipForm.payType.disabled="true";
	if(document.salaryPaySlipForm.payType==null || document.salaryPaySlipForm.payType.value == null ||document.salaryPaySlipForm.payType.value=="" || document.getElementById('payType').options[document.getElementById('payType').selectedIndex].text=="<%=PayrollConstants.EMP_PAYSLIP_PAYTYPE_NORMAL%>")
	{
		if(document.salaryPaySlipForm.payHeadAmount[0]==null)
		{
			document.salaryPaySlipForm.payHeadAmount.readOnly="true";
		}
		else
		{
			document.salaryPaySlipForm.payHeadAmount[0].readOnly="true";
		}
	}
}

/* if will invoke addRowToTable() method based on key pressed */
function whichButtonEarnings(e,tbl,obj,objr)
{
	var F2 = 113;
	var del = 46;
	var code;

	if ( !e )
	 var e = window.event;

	if ( e.keyCode ) code = e.keyCode ;
	else if ( e.which ) code = e.which ;

	//if ( (code == F2) )
	//{
	   if(checkPayHead() && objr=="earnings")
	    addRowToTable(tbl,obj);

   	//}
   	//else if ( (code == del) )
	//{
	///  deleteRow(tbl,obj);
	//}
	//else return;
}

function deleteRow(table,obj)
{
	if(table=='paytable')
	{
		var tbl = document.getElementById(table);
		var tableLength=tbl.rows.length-1;
		var rowIndex=getRow(obj).rowIndex;	
		
		if(tableLength>1)
		{
		   tbl.deleteRow(rowIndex)
		}
		else if(tableLength==1)
		{
		     alert("<bean:message key='alertDeleteRow'/>");
		     return false;
		}
	}
	if(table=='salAdvances')
	{
		var tbl = document.getElementById(table);
		var tableLength=tbl.rows.length-2;
		var rowIndex=getRow(obj).rowIndex;	
		
		if(tableLength>1)
		{
		   tbl.deleteRow(rowIndex)
		}
		else if(tableLength==1)
		{
		     alert("<bean:message key='alertDeleteRow'/>");
		     return false;
		}
	}
	if(table=='deductions')
	{
		var tbl = document.getElementById(table);
		var tableLength=tbl.rows.length-2;
		var rowIndex=getRow(obj).rowIndex;	
		
		if(tableLength>1)
		{
		   tbl.deleteRow(rowIndex)
		}
		else if(tableLength==1)
		{
		     alert("<bean:message key='alertDeleteRow'/>");
		     return false;
		}
	}
	if(table=="deductionTax")
	{
		var tbl = document.getElementById(table);
		var tableLength=tbl.rows.length-1;
		var rowIndex=getRow(obj).rowIndex;	
		
		if(tableLength>1)
		{
		   tbl.deleteRow(rowIndex)
		}
		else if(tableLength==1)
		{
		     alert("<bean:message key='alertDeleteRow'/>");
		     return false;
		}
	}
	if(table=="OtherdeductionTax")
	{
	var tbl = document.getElementById(table);
		var tableLength=tbl.rows.length-1;
		var rowIndex=getRow(obj).rowIndex;	
		
		if(tableLength>1)
		{
		   tbl.deleteRow(rowIndex)
		}
		else if(tableLength==1)
		{
		     alert("<bean:message key='alertDeleteRow'/>");
		     return false;
		}
	}
	collectionSum();
	netPayCollection();
}

function addRowToTable(tbl,obj)
{
  tableObj=document.getElementById(tbl);
  var rowObj1=getRow(obj);
  var tbody=tableObj.tBodies[0];
  var lastRow = tableObj.rows.length;
   if(tbl=='paytable' && lastRow < getControlInBranch(tableObj.rows[rowObj1.rowIndex],'payHead').options.length)
  {
  	   var rowObj = tableObj.rows[lastRow-1].cloneNode(true);
       	   tbody.appendChild(rowObj);
  	   var remlen1=document.salaryPaySlipForm.payHeadAmount.length;
 	   document.salaryPaySlipForm.pct[remlen1-1].value="";
 	   document.salaryPaySlipForm.payHeadAmount[remlen1-1].value="";
 	   document.salaryPaySlipForm.calculationType[remlen1-1].value="";
 	   document.salaryPaySlipForm.pctBasis[remlen1-1].value="";
  }
  else
  {
	  if(tbl=='paytable')
	  {
	    alert("<bean:message key='alertNoPayHeadAvailable'/>");
	    return false;
	  }
  }
  if(tbl=='salAdvances' )
  {
	 var rowObj = tableObj.rows[lastRow-1].cloneNode(true);
	  tbody.appendChild(rowObj);
  	  var remlen=document.salaryPaySlipForm.salaryAdvances.length;
	  document.salaryPaySlipForm.salaryAdvancesAmount[remlen-1].value="";
	  document.salaryPaySlipForm.advanceSchedule[remlen-1].value="";
  }
  else
  {
  	  if(tbl=='salAdvances')
  	  {
  	    alert("<bean:message key='alertNoSalAdvAvailable'/>");
  	    return false;
  	  }
  }
  if(tbl=='deductions')
    {
  	  var rowObj = tableObj.rows[lastRow-1].cloneNode(true);
  	  tbody.appendChild(rowObj);
  	 var remlen=document.salaryPaySlipForm.otherDeductAccountCode.length;
  	  document.salaryPaySlipForm.otherDeductAccountCode[remlen-1].value="";
  	  document.salaryPaySlipForm.otherDeductAccountDescription[remlen-1].value="";
  	  document.salaryPaySlipForm.otherDeductionsAmount[remlen-1].value="";
  }
  if(tbl=="deductionTax")
  {
   
   var rowObj = tableObj.rows[lastRow-1].cloneNode(true);
   tbody.appendChild(rowObj);
   var remlen=document.salaryPaySlipForm.taxTypeName.length;
   document.salaryPaySlipForm.taxTypeName[remlen-1].value="0";
   document.salaryPaySlipForm.taxTypeAmount[remlen-1].value="";
   document.salaryPaySlipForm.referenceno[remlen-1].value="";
  } 
  if(tbl=="OtherdeductionTax")
  {

   var rowObj = tableObj.rows[lastRow-1].cloneNode(true);
   tbody.appendChild(rowObj);
  
   var remlen=document.salaryPaySlipForm.dedOthrName.length;
   document.salaryPaySlipForm.dedOthrName[remlen-1].value="0";
   document.salaryPaySlipForm.dedOthrAmount[remlen-1].value="";
   document.salaryPaySlipForm.dedRefNo[remlen-1].value="";
   document.salaryPaySlipForm.dedOthrTxId[remlen-1].value="0";
  }
  
}

function whichButtonEarningsDeductions(e,tbl,obj,objr)
{
	var F2 = 113;
	var del = 46;
	var code;

	if ( !e )
	 var e = window.event;

	if ( e.keyCode ) code = e.keyCode ;
	else if ( e.which ) code = e.which ;

	   if(checkSalAdv('key') && objr=="advDeductions")
	    addRowToTable(tbl,obj);

}

function checkOtherDed(arg)
{
	var tbl = document.getElementById('deductions');
	var rCount=tbl.rows.length-2;
	if(tbl.rows.length == 3)
  {
	  if(arg!="submit")
	  {
	   if(document.salaryPaySlipForm.otherDeductAccountCode.value=="")
	   {
		  alert("<bean:message key='alertSelectOtherDeduction'/>");
		  document.salaryPaySlipForm.otherDeductAccountCode.value="";
		  document.salaryPaySlipForm.otherDeductAccountCode.focus();
		  return false;
	   }
	   if(document.salaryPaySlipForm.otherDeductionsAmount.value=="" && document.salaryPaySlipForm.otherDeductionsAmount.value!=0)
	  {
		  alert("<bean:message key='alertSelectOtherDedAmt'/>");
		  document.salaryPaySlipForm.otherDeductionsAmount.value="";
		  document.salaryPaySlipForm.otherDeductionsAmount.focus();
		  return false;
	  }
    }
    else
    {
		 if(document.salaryPaySlipForm.otherDeductAccountCode.value=="" && document.salaryPaySlipForm.otherDeductionsAmount.value!="" && document.salaryPaySlipForm.otherDeductionsAmount.value!=0)
		 {
		 	alert("<bean:message key='alertSelectOtherDeduction'/>");
			document.salaryPaySlipForm.otherDeductAccountCode.value="";
			document.salaryPaySlipForm.otherDeductAccountCode.focus();
		    return false;
		 }
		 if(document.salaryPaySlipForm.otherDeductAccountCode.value=="" && document.salaryPaySlipForm.otherDeductAccountDescription.value!="")
		 {
			alert("<bean:message key='alertSelectOtherDeduction'/>");
			document.salaryPaySlipForm.otherDeductAccountCode.value="";
			document.salaryPaySlipForm.otherDeductAccountDescription.value="";
			document.salaryPaySlipForm.otherDeductAccountCode.focus();
			return false;
		 }


	}
  }
  if(tbl.rows.length>3)
  {
	  if(arg!="submit")
	  {

	  if(document.salaryPaySlipForm.otherDeductAccountCode[rCount-1].value=="")
	  {
		alert("<bean:message key='alertSelectOtherDeduction'/>");
		document.salaryPaySlipForm.otherDeductAccountCode[rCount-1].value="";
		document.salaryPaySlipForm.otherDeductAccountCode[rCount-1].focus();
		return false;
	 }
	 if(document.salaryPaySlipForm.otherDeductionsAmount[rCount-1].value=="" &&  document.salaryPaySlipForm.otherDeductionsAmount[rCount-1].value!=0)
	 {
		alert("<bean:message key='alertSelectOtherDedAmt'/>");
		document.salaryPaySlipForm.otherDeductionsAmount[rCount-1].value="";
		document.salaryPaySlipForm.otherDeductionsAmount[rCount-1].focus();
		return false;
	 }
    }
    else
	{
		 if(document.salaryPaySlipForm.otherDeductAccountCode[rCount-1].value=="" && (document.salaryPaySlipForm.otherDeductionsAmount[rCount-1].value!="" && document.salaryPaySlipForm.otherDeductionsAmount[rCount-1].value!=0))
		 {
			alert("<bean:message key='alertSelectOtherDeduction'/>");
			document.salaryPaySlipForm.otherDeductAccountCode.value="";
			document.salaryPaySlipForm.otherDeductAccountCode.focus();
			return false;
		 }
		  if(document.salaryPaySlipForm.otherDeductAccountCode[rCount-1].value=="" && document.salaryPaySlipForm.otherDeductAccountDescription[rCount-1].value!="")
		 {
			alert("<bean:message key='alertSelectOtherDeduction'/>");
			document.salaryPaySlipForm.otherDeductAccountCode[rCount-1].value="";
			document.salaryPaySlipForm.otherDeductAccountDescription[rCount-1].value="";
			document.salaryPaySlipForm.otherDeductAccountCode[rCount-1].focus();
			return false;
		 }


	}
 }
 return true;
}
function whichButtonEarnings2(e,tbl,obj,objr)
{


	var F2 = 113;
	var del =46;
	var code;

	if ( !e )
	 var e = window.event;

	if ( e.keyCode ) code = e.keyCode ;
	else if ( e.which ) code = e.which ;

	
	   if(checkOtherDed('key') && objr=="otherDed")
	    addRowToTable(tbl,obj);

}
function whichButtonTaxDeductions(e,tbl,obj,objr,str)
{
	var code;

	if ( !e )
	 var e = window.event;

	if ( e.keyCode ) code = e.keyCode ;
	else if ( e.which ) code = e.which ;
       
       if(objr=="taxDeductions" )
	   {
	   addRowToTable(tbl,obj);
	   }

	  
}
function whichButtonOtherTaxDeductions(e,tbl,obj,objr)
{
	var code;

	if ( !e )
	 var e = window.event;

	if ( e.keyCode ) code = e.keyCode ;
	else if ( e.which ) code = e.which ;
       
       if(objr=="othertaxDeductions" )
	   {
	   addRowToTable(tbl,obj);
	   }

	  
}
function checkSalAdv(arg)
{
	//alert("innn");
	var tbl = document.getElementById('salAdvances');
	var rCount=tbl.rows.length-2;
	 if(tbl.rows.length == 3)
	 {
		  if(arg!="submit")
		  {
		   if(document.salaryPaySlipForm.salaryAdvances.options[document.salaryPaySlipForm.salaryAdvances.selectedIndex].value =="0")
		   {
			  alert("<bean:message key='alertSalAdv'/>");
			  document.salaryPaySlipForm.salaryAdvances.focus();
			  return false;
		   }
		   if(document.salaryPaySlipForm.salaryAdvancesAmount.value=="" || document.salaryPaySlipForm.salaryAdvancesAmount.value==0)
		   {
			  alert("<bean:message key='alertSalAdvAmt'/>");
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
				  alert("<bean:message key='alertSalAdv'/>");
				  document.salaryPaySlipForm.salaryAdvances.focus();
			       return false;
			 }

		//alert("in submit");
		  if(document.salaryPaySlipForm.salaryAdvances.options[document.salaryPaySlipForm.salaryAdvances.selectedIndex].value !="0"
	   && (document.salaryPaySlipForm.salaryAdvancesAmount.value!="0"
	   || document.salaryPaySlipForm.salaryAdvancesAmount.value!=""))
		{
			if(checkingPendingAmount('EGPAY_SALADVANCES','PENDING_AMT',
					document.salaryPaySlipForm.salaryAdvancesAmount.value,
					'ID_EMPLOYEE',document.getElementById("employeeCodeId").value,'ID',
			document.salaryPaySlipForm.salaryAdvances.options[document.salaryPaySlipForm.salaryAdvances.selectedIndex].value)=="false")
				{
						var head;
					<c:forEach var="salAdvancesObj" items="<%=salAdvances%>">
					  if(document.salaryPaySlipForm.salaryAdvances.options[document.salaryPaySlipForm.salaryAdvances.selectedIndex].value==
					  "${salAdvancesObj.id}")
					  {
						  head="${salAdvancesObj.salaryCodes.head}";
					  }
					</c:forEach>
						alert("<bean:message key='alertPendingAmtFor'/> "+head
						+" <bean:message key='alertIsOnlyRs'/>"+document.salaryPaySlipForm.salaryAdvPendingAmount.value);
						document.salaryPaySlipForm.salaryAdvancesAmount.focus();
						return false;
				}
		}
		}
	  }
	  if(tbl.rows.length>3)
	  {
	   if(arg!="submit")
	   {
		  if(document.salaryPaySlipForm.salaryAdvances[rCount-1].value =="0" || document.salaryPaySlipForm.salaryAdvances[rCount-1].value=="")
		{
			alert("<bean:message key='alertSalAdv'/>");
			document.salaryPaySlipForm.salaryAdvances[rCount-1].focus();
			return false;
		}
		if(document.salaryPaySlipForm.salaryAdvancesAmount[rCount-1].value=="" || document.salaryPaySlipForm.salaryAdvancesAmount[rCount-1].value==0)
		{
			alert("<bean:message key='alertSalAdvAmt'/>");
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
				  alert("<bean:message key='alertSalAdv'/>");
				  document.salaryPaySlipForm.salaryAdvances[rCount-1].focus();
				 return false;
			 }


		 if(document.salaryPaySlipForm.salaryAdvances[rCount-1].options[document.salaryPaySlipForm.salaryAdvances[rCount-1].selectedIndex].value !="0"
	   && (document.salaryPaySlipForm.salaryAdvancesAmount[rCount-1].value!="0"
	   || document.salaryPaySlipForm.salaryAdvancesAmount[rCount-1].value!=""))
		{
			if(checkingPendingAmount('EGPAY_SALADVANCES','PENDING_AMT',
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
						alert("<bean:message key='alertPendingAmtFor'/> "+head
						+"<bean:message key='alertIsOnlyRs'/>"+document.salaryPaySlipForm.salaryAdvPendingAmount[rCount-1].value);
						document.salaryPaySlipForm.salaryAdvancesAmount[rCount-1].focus();
						return false;
				}
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
			alert("<bean:message key='alertSelectpayHead'/>");
			document.salaryPaySlipForm.payHead.focus();
			return false;
		}

		if(document.salaryPaySlipForm.payHeadAmount.value=="")
		{
			alert("<bean:message key='alertSelectpayHeadAmt'/>");
			document.salaryPaySlipForm.payHeadAmount.value="";
			document.salaryPaySlipForm.payHeadAmount.focus();
			return false;
		}
		if(document.salaryPaySlipForm.pct.value=="" && document.salaryPaySlipForm.calculationType.value=="ComputedValue")
		{
			alert("<bean:message key='alertEnterPercForPayHead'/>");
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
				alert("<bean:message key='alertSelectpayHeadAmt'/>");
				document.salaryPaySlipForm.payHead[rCount-1].focus();
				return false;
			 }
			 if(document.salaryPaySlipForm.payHeadAmount[rCount-1].value=="")
			{
				alert("<bean:message key='alertSelectpayHeadAmt'/>");
				document.salaryPaySlipForm.payHeadAmount[rCount-1].value="";
				document.salaryPaySlipForm.payHeadAmount[rCount-1].focus();
				return false;
			}
			if(document.salaryPaySlipForm.pct[rCount-1].value=="" && document.salaryPaySlipForm.calculationType[rCount-1].value=="ComputedValue")
			{
				alert("<bean:message key='alertEnterPercForPayHead'/>");
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
	 //getControlInBranch(table.rows[rowObj.rowIndex],'yearToDate').value="";
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
	collectionSum();
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

			 alert("<bean:message key='alertenterPercRange'/>");
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
	 var caught=0;
	 var table= document.getElementById("paytable");
	 var tbl = document.getElementById('paytable').rows.length;
	 var pctBasisText = getControlInBranch(table.rows[rowObj.rowIndex],'pctBasis').value;
	 if(tbl>2)
	{
		for(var i=0;i < tbl;i++)
		{
			for(var j=i+1;j < tbl-1;j++)
			{

				if(i!=j)
				{
					if(document.salaryPaySlipForm.payHead[i].options[document.salaryPaySlipForm.payHead[i].selectedIndex].text==document.salaryPaySlipForm.payHead[j].options[document.salaryPaySlipForm.payHead[j].selectedIndex].text)
					{
						alert("<bean:message key='alertDupSelPayHead'/>");
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
	var tbl = document.getElementById('salAdvances').rows.length-2;
	var rowObj=getRow(obj);

	if(tbl>1)
	{
		for(var i=0;i <  tbl;i++)
		{
			for(var j=i+1;j < tbl-1;j++)
			{
				if(i!=j)
				{

					if(document.salaryPaySlipForm.salaryAdvances[i].options[document.salaryPaySlipForm.salaryAdvances[i].selectedIndex].value==document.salaryPaySlipForm.salaryAdvances[j].options[document.salaryPaySlipForm.salaryAdvances[j].selectedIndex].value)
					{
						alert("<bean:message key='alertDupSelSalAdv'/>");
						document.salaryPaySlipForm.salaryAdvances[j].selectedIndex=0;
						document.salaryPaySlipForm.salaryAdvancesAmount[j].value="";
						return false;
					 }
				}
			}
		 }
 	 }
}
function checkOtherDedCode(obj)
{
	var tbl = document.getElementById('deductions').rows.length-2;
	var rowObj=getRow(obj);
	var rowIndex=rowObj.rowIndex-2;
	if(tbl>1)
	{
		for(var i=0;i < tbl;i++)
		{
			for(var j=i+1;j < tbl;j++)
			{
				if(i!=j)
				{
					if(document.salaryPaySlipForm.accountCodeId[i].value==document.salaryPaySlipForm.accountCodeId[j].value)
					{
						alert("<bean:message key='alertDupSelOtherDedAcc'/>");
						document.salaryPaySlipForm.otherDeductAccountCode[j].focus();
						return false;
					 }
				}
			}
		 }
 	 }
 }
 function getEmpAttAndIncData(empid)
   {
  	var type = "gettingempattendence";
  	var link = "<%=request.getContextPath()%>"+"/commons/checkingPayslip.jsp?empId=" + empid+"&type="+type+" ";
  	var request = initiateRequest();
  	var isUnique;
  	request.onreadystatechange = function()
  	{
 
  	if (request.readyState == 4)
  	{
  	if (request.status == 200)
  	{
  	var response=request.responseText;
  	var result = response.split("`");
  	//alert("result ="+result[0]);
  	var temp=result[0].split("~");
 	totalwrkdays=temp[0];
 	paiddays=temp[1];  	
 
  	    }
  	  }
  	};
  	request.open("GET", link, false);
  	request.send(null);
 
  	return isUnique;
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
	 <c:forEach var="salAdvancesObj" items="<%=salAdvances%>">
	 if(getControlInBranch(table.rows[rowObj.rowIndex],'salaryAdvances').value=="${salAdvancesObj.id}"){
	     // alert('${salAdvancesObj.maintainSchedule}');
	      <c:if test ="${salAdvancesObj.maintainSchedule=='Y'}">
	      //alert("hwrere"+getControlInBranch(table.rows[rowObj.rowIndex],'salaryAdvPendingAmount'));
	      getControlInBranch(table.rows[rowObj.rowIndex],'salaryAdvancesAmount').readOnly='true';
	      </c:if>
		//alert(${fn:length(salAdvancesObj.advanceSchedules)});
		if(${fn:length(salAdvancesObj.advanceSchedules)} != 0){
			<c:set var="endLoop" value="true"/>
			<c:forEach var="advSchedule" items="${salAdvancesObj.advanceSchedules}">
				<c:if test = "${endLoop}">
					<c:if test = "${advSchedule.recover == null}">
						getControlInBranch(table.rows[rowObj.rowIndex],'salaryAdvPendingAmount').value = "${salAdvancesObj.pendingAmt}";
						getControlInBranch(table.rows[rowObj.rowIndex],'salaryAdvancesAmount').value = "${advSchedule.principalAmt + advSchedule.interestAmt}";
						getControlInBranch(table.rows[rowObj.rowIndex],'advanceSchedule').value = "${advSchedule.id}";
						//alert("${advSchedule.id}");
						<c:set var="endLoop" value="false"/>
					</c:if>
				</c:if>
			</c:forEach>
		}else if(${fn:length(salAdvancesObj.advanceSchedules)} == 0){
			getControlInBranch(table.rows[rowObj.rowIndex],'salaryAdvPendingAmount').value = "${salAdvancesObj.pendingAmt}";
			getControlInBranch(table.rows[rowObj.rowIndex],'salaryAdvancesAmount').value = "${salAdvancesObj.instAmt}";
			getControlInBranch(table.rows[rowObj.rowIndex],'advanceSchedule').value = "";
		}	
	}
	</c:forEach>
	if(getControlInBranch(table.rows[rowObj.rowIndex],'salaryAdvances').value=="" || 
	getControlInBranch(table.rows[rowObj.rowIndex],'salaryAdvances').value=="0"){
		getControlInBranch(table.rows[rowObj.rowIndex],'salaryAdvancesAmount').value = "";
		getControlInBranch(table.rows[rowObj.rowIndex],'salaryAdvancesAmount').focus();
		return false;
	}
	netPayCollection();

}
function checkAmount(obj)
{
	//alert("inside");
	var rowObj=getRow(obj);
     var table= document.getElementById("salAdvances");
	if(getControlInBranch(table.rows[rowObj.rowIndex],'salaryAdvances').value!="" && getControlInBranch(table.rows[rowObj.rowIndex],'salaryAdvances').value!="0"
	&& getControlInBranch(table.rows[rowObj.rowIndex],'salaryAdvancesAmount').value>0 && getControlInBranch(table.rows[rowObj.rowIndex],'salaryAdvancesAmount').value!=""){

		if(checkingPendingAmount('EGPAY_SALADVANCES','PENDING_AMT',
		getControlInBranch(table.rows[rowObj.rowIndex],'salaryAdvancesAmount').value,
		'ID_EMPLOYEE',document.getElementById("employeeCodeId").value,
		'ID',getControlInBranch(table.rows[rowObj.rowIndex],'salaryAdvances').value)=="false")
		{
			var head;
			<c:forEach var="salAdvancesObj" items="<%=salAdvances%>">
			  if(getControlInBranch(table.rows[rowObj.rowIndex],'salaryAdvances').value==
			  "${salAdvancesObj.id}")
			  {
				  head="${salAdvancesObj.salaryCodes.head}";
			  }
			</c:forEach>
			alert("<bean:message key='alertPendingAmtFor'/> "+head+"<bean:message key='alertIsOnlyRs'/>"
			+getControlInBranch(table.rows[rowObj.rowIndex],'salaryAdvPendingAmount').value);
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
            alert("<bean:message key='alertEnterPositiveVal'/>");
            objt.focus();
            return false;

        }
        if(isNaN(amt))
        {
            alert("<bean:message key='alertEnterNumericVal'/>");
            objt.focus();
            return false;

        }
           objt.value= Math.round(amt);
           collectionSum();
           calOnchangeAmount(objt);


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
					payText="${payHeadObj.head}";
					sum1 = getControlInBranch(table.rows[rowObj.rowIndex],'payHeadAmount').value;

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
	 for(var i=0;i < remlen1;i++)
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
            alert("<bean:message key='alertEnterPositiveVal'/>");
            objt.focus();
            return false;

        }
        if(isNaN(amt))
        {
            alert("<bean:message key='alertEnterNumericVal'/>");
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
		if(document.salaryPaySlipForm.taxTypeAmount.value!="")
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
		if(document.salaryPaySlipForm.dedOthrAmount.value!="")
		{
			net = net+ eval(document.salaryPaySlipForm.dedOthrAmount.value);
		}
		else
		{
			document.salaryPaySlipForm.dedOthrAmount.value=0;
	    }
	}
	
	if(document.salaryPaySlipForm.salaryAdvancesAmount.length>0)
	{
		 for(var i=0;i<document.salaryPaySlipForm.salaryAdvancesAmount.length;i++)
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

 function autocompleteEmpCode(obj)
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
 	if(yuiflag[currRow.rowIndex] == undefined)
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
 	yuiflag[currRow.rowIndex]=1;
  }
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
	yuiflag1[currRow.rowIndex] = undefined;
	neibrObj1=getControlInBranch(currRow,neibrObjName);
	var temp = obj.value;temp = temp.split("`-`");
	var xyz = getControlInBranch(currRow,'otherDeductAccountCode');
	window.setTimeout("100",500);
	xyz.value=temp[0];
	if(obj.value==null || obj.value=="") { neibrObj1.value=""; return; }
	if(temp[1]==null && (neibrObj1.value!='' || neibrObj1.value!=null) ) {  return ;}
	else {
			neibrObj1.value=temp[2];
			getControlInBranch(currRow,'otherDeductAccountDescription').value=temp[1];
		 }

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

 function validation(arg,obj)
 {
	//alert(document.salaryPaySlipForm.advanceSchedule[0].value);
	//alert(document.salaryPaySlipForm.advanceSchedule[1].value);
	
	obj.disabled = true; 
    if(document.salaryPaySlipForm.employeeCodeId.value=="")
	 {
		 alert("<bean:message key='alertSelEmpCode'/>");
		 document.getElementById("employeeCode").focus();
		 obj.disabled = false;
		 return false;
	 }
	 /*else if(!uniqueCheckingBoolean('<%=request.getContextPath()%>/commonyui/egov/uniqueCheckAjax.jsp', 'EGPAY_EMPPAYROLL', 'ID_EMPLOYEE', 'employeeCodeId', 'no', 'no'))
	 {
	 	alert("For selected employee payslip already exists");
		document.getElementById("employeeCode").value="";
		document.getElementById("employeeName").value="";
		document.getElementById("designation").value="";
		document.getElementById("department").value="";
		document.getElementById("yearOfJoining").value="";
		document.getElementById("payScaleName").value="";
 		document.getElementById("employeeCode").focus()
	 	return false;
	 }*/	 
	 if(document.salaryPaySlipForm.payHeadAmount.length>0)
	   {
	 	 for(var i=0;i<document.salaryPaySlipForm.payHeadAmount.length;i++)
	 	 {
	 		if(document.salaryPaySlipForm.payHead[i].options[document.salaryPaySlipForm.payHead[i].selectedIndex].value =="0")
			{
				alert("<bean:message key='alertSelPayHead'/>");
				document.salaryPaySlipForm.payHead[i].focus();
				obj.disabled = false;
				return false;
			}
	 		if(document.salaryPaySlipForm.pct[i].value=="" && document.salaryPaySlipForm.calculationType[i].value=="ComputedValue")
			{
				alert("<bean:message key='alertEnterPercForPayHead'/>");
				document.salaryPaySlipForm.pct[i].focus();
				obj.disabled = false;
				return false;
			}
	 	  }
	    }
	    else
	    {
	 	   if(document.salaryPaySlipForm.payHead.options[document.salaryPaySlipForm.payHead.selectedIndex].value =="0")
			{
				alert("<bean:message key='alertSelPayHead'/>");
				document.salaryPaySlipForm.payHead.focus();
				obj.disabled = false;
				return false;
			}
	 	   if(document.salaryPaySlipForm.pct.value=="" && document.salaryPaySlipForm.calculationType.value=="ComputedValue")
			{
				alert("<bean:message key='alertEnterPercForPayHead'/>");
				document.salaryPaySlipForm.pct.focus();
				obj.disabled = false;
				return false;
			}

	   }
	   if(!checkSalAdv('submit'))
	   {
	     return false;
	   }
	   if(!checkOtherDed('submit'))
	   {
	     return false;
	   }
	  	var remlen1=document.salaryPaySlipForm.pctBasis.length;
			var payBasisText="";
			if(remlen1>0)
			{
				for(var j=0;j < remlen1;j++)
				{
					if(document.salaryPaySlipForm.calculationType[j].value=="ComputedValue" && document.salaryPaySlipForm.pctBasis[j].value!="")
					{

						payBasisText =payBasisText+","+document.salaryPaySlipForm.pctBasis[j].value;

					}
				}
			   var d1 = payBasisText;
				d1 = d1.split(',');
				var hit=0;
				 for(var i=0;i < d1.length;i++)
				{
					for(var j=0;j < document.salaryPaySlipForm.payHead.length;j++)
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
					alert("<bean:message key='alertSelPerBasis'/>");
					obj.disabled = false;
					return false;
				}
		  }
		  else
		  {
			  if(document.salaryPaySlipForm.pctBasis.value!="" && document.salaryPaySlipForm.calculationType.value=="ComputedValue")
			  {
					alert("<bean:message key='alertSelFlatVal'/>");
					document.salaryPaySlipForm.payHead.focus();
					obj.disabled = false;
					return false;
			  }
		}
		if(document.salaryPaySlipForm.grossPay.value=="0")
		{
			alert("<bean:message key='alertEnterAmtGrZero'/>");
			obj.disabled = false;
			return false;
		}
		/**
		 * here we will reassign the basic
		 */
		if(document.salaryPaySlipForm.grossPay.value!=0 && document.salaryPaySlipForm.grossPay.value!="")
		{
			if(document.salaryPaySlipForm.payHeadAmount[0]==null)
			{
				document.salaryPaySlipForm.basicPay.value=document.salaryPaySlipForm.payHeadAmount.value;
			}else
			{
				document.salaryPaySlipForm.basicPay.value=document.salaryPaySlipForm.payHeadAmount[0].value;
			}
		}
		if(eval(document.salaryPaySlipForm.netPay.value) < 0)		
		{
		   alert("<bean:message key='alertNetPayNotNegative'/>");
		   obj.disabled = false;
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
					 obj.disabled = false;
			         return false;
					}
				
				
			}
		
		
		} 
		else if(len>5)
		{
		
		for(var i=0;i < len-4;i++)
	
		{
			if(document.salaryPaySlipForm.taxTypeName[i].value !="")
			{
			 
	           if(document.salaryPaySlipForm.taxTypeAmount[i].value ==0)
				   {
				     alert("please fill the amount ");
					 obj.disabled = false;
			         return false;
					}
			
				
			}
		}
			}
				var lenOthr =document.getElementById("OtherdeductionTax").rows.length;
				
				
		if(lenOthr==2)
		{
           
		   if(document.salaryPaySlipForm.dedOthrName.value !="")
			  {
			 
	           if(document.salaryPaySlipForm.dedOthrAmount.value ==0)
				   {
				     alert("please fill the amountttt ");
					 obj.disabled = false;
			         return false;
					}
				
			
				
			}
		
		
		} 
		else if(lenOthr>2)
		{
		
		for(var i=0;i < lenOthr-1;i++)
	
		{
			if(document.salaryPaySlipForm.dedOthrName[i].value !="")
			{
			 
	           if(document.salaryPaySlipForm.dedOthrAmount[i].value ==0)
				   {
				     alert("please fill the amount ");
					 obj.disabled = false;
			         return false;
					}
				
				
			}
		}
			}

		<%		
		 if("Manual".equals(payslipWfType)){%>
			if(validateForMandatory() == "false"){
				obj.disabled = false;
				return false;
			}
		 <%}%>	
	 	if(arg=="save")
	   	{		
		 document.salaryPaySlipForm.frwdType.value = "";	
	   	 document.salaryPaySlipForm.action ="${pageContext.request.contextPath}/payslip/generatePaySlips.do";
	   	}
	   	else if(arg=="saveNew")
	   	{	
	   		document.salaryPaySlipForm.action ="${pageContext.request.contextPath}/payslip/generatePaySlipsAgain.do";
	   	}
	   	//alert("here"+document.salaryPaySlipForm.payType.disabled);
	   	document.salaryPaySlipForm.payType.disabled=false;
	   	document.salaryPaySlipForm.month.disabled=false;
   		document.salaryPaySlipForm.year.disabled=false;
	   	//alert(document.salaryPaySlipForm.payType.disabled);
	   	
  		 document.salaryPaySlipForm.submit();

  }
  /**
  * this will recompute the deduction amount based on basic salary  
  **/
  function calDeductionAmts()
  {  
 	var type = "reComputeDeductionAmts";
 	var basicSal=0;
 	
 	var basicSal=document.salaryPaySlipForm.basicPay.value;
 	var grossamt=document.salaryPaySlipForm.grossPay.value;
 	var month=document.salaryPaySlipForm.month.value;
 	var finyr=document.salaryPaySlipForm.year.value; 
 	var totaldays=document.getElementById("noofdaysmonth").value;
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
   	var totaldays=document.getElementById("noofdaysmonth").value;
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

	if(len > 4)
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
 table = document.getElementById("OtherdeductionTax");

 var rowObj = getRow(obj);
var len = rowObj.rowIndex;
var currDed = getControlInBranch(table.rows[len],'dedOthrName').value;

	if(len > 1)
	 {
			for(var i=len-1;i>=1;i--)
			        {

					var taxname = getControlInBranch(table.rows[i],'dedOthrName').value;
					var referNo = getControlInBranch(table.rows[i],'dedRefNo').value;
						if(currDed == taxname)
								{
					             if(curReferNo == referNo)
					              {
					              alert("The reference number can't be repeated");
								  obj.value="";
								  getControlInBranch(table.rows[len],'dedRefNo').focus();
					              }
					
					}
			       }
		}
 }
	}

 }
  
  function calEarningsAmt()
  {
        
          var tableObj=document.getElementById('paytable');
          var rows=tableObj.rows.length;
          for(var i=1;i < rows;i++)
  	  {
  	    var salcodeid=getControlInBranch(tableObj.rows[i],'payHead').value;
  	    var caltype=getControlInBranch(tableObj.rows[i],'calculationType').value;
  	    var pct=getControlInBranch(tableObj.rows[i],'pct').value;
  	    var pctbasis=getControlInBranch(tableObj.rows[i],'pctBasis').value;
  	    var amount=0
  	    if(caltype=="ComputedValue")
  	    {
  	        for(var j=1;j < rows;j++)
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
  	     // alert("slab="+amount);
  	    }
  	    if(amount>0)
  	    {
  	      getControlInBranch(tableObj.rows[i],'payHeadAmount').value=amount;
  	    }
  	  }
  	  collectionSum();
  	  
  }
  
  /**
  * it will paint the recomputd values to screen
  *
  **/
  function paintdedamts()
  {
	  calDeductionAmts();
	  calEarningsAmt();
	  var i=0;
	  for(var j=0;j < dedtaxes.length;j++)
	  {
	    document.salaryPaySlipForm.taxTypeAmount[i++].value=dedtaxes[j];
	  }
	  for(var j=0;j < dedothers.length;j++)
	  {
	    document.salaryPaySlipForm.taxTypeAmount[i++].value=dedothers[j];
	  }  
	  netPayCollection();
  }
function addrowTest(table,obj)
{

  tableObj=document.getElementById(table);
  var rowObj1=getRow(obj);
  var tbody=tableObj.tBodies[0];
  var lastRow = tableObj.rows.length;
  var rowObj = tableObj.rows[lastRow-1].cloneNode(true);
  tbody.appendChild(rowObj);
  //alert("testjsekjfxk");
  for(var i=1;i <= (tableObj.rows[1].cells.length-2);i++)
  {
     tableObj.rows[lastRow].cells[i].childNodes[0].value="";
  }	
	
}

function deleteRowTest(table,obj)
{
	var tbl = document.getElementById(table);
	var rowNumber=getRow(obj).rowIndex;
	//alert("rowNumber="+rowNumber);
	if(rowNumber>1)
	   tbl.deleteRow(rowNumber)
	else
	{			 
		 alert("<bean:message key='alertDeleteRow'/>"); 
		 return false; 
	}
}




</script>
</head>

 

<body onLoad="onBodyLoad();" >
   <!-- Body Begins -->
  

 <html:form method="POST" action="/payslip/generatePaySlips" >

 <table align='center' id="table2" width="95%" >
 <tr>
 <td>
 <!-- Tab Navigation Begins -->

 <!-- Tab Navigation Ends -->

 <!-- Body Begins -->

 <div align="center">

  <center>
  

  <table  width="100%" cellpadding ="0" cellspacing ="0" border = "0" id="employee">
	<tr>
    		

			 <td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
                  <div class="headplacer"><bean:message key="genPayslipTitle"/></div></td>

    	</tr>


  <tr>
 <!--  <html:hidden property="payType" styleId="payType"/> -->
  <html:hidden property="employeeCodeId" styleId="employeeCodeId"/>
  <html:hidden property="effectiveFrom" />
  <html:hidden property="frwdType" />
  <html:hidden property="effectiveTo"/>
  <html:hidden property="leaveApplication"/>

   	<!-- <html:hidden property="payType" /> -->
  	<td class="whiteboxwk"><bean:message key="empCode"/><font color="red">*</font></td>
  	<td class="whitebox2wk"><input type="text"  class="selectwk" name="employeeCode" id ="employeeCode" readonly ></td>
  	<td class="whiteboxwk"><bean:message key="empName"/></td>
  	<td class="whitebox2wk"><input type="text"  class="selectwk" name="employeeName" id ="employeeName" readOnly="true" ></td>
  </tr>
  <tr>
  <td><div id="codescontainer"></div></td>
  </tr>
  <tr>
    <td class="greyboxwk"><bean:message key="designation"/></td>
    <td class="greybox2wk"><input type="text" class="selectwk" name="designation" id ="designation" readOnly></td>
    <td class="greyboxwk"><bean:message key="yearOfJoining"/></td>
    <td class="greybox2wk"><input type="text"  class="selectwk" name="yearOfJoining" id ="yearOfJoining" readOnly></td>
  </tr>
  <tr>
    <td class="whiteboxwk"><bean:message key="department"/></td>
   <td class="whitebox2wk"><input type="text"  class="selectwk" name="department" id ="department" readOnly  ></td>
   <td class="whiteboxwk"><bean:message key="payscale"/></td>
   <td class="whitebox2wk"><input type="text"  class="selectwk" name="payScaleName" id ="payScaleName" value="${payStructure.payHeader.name}" readOnly ></td>
  </tr>
  <tr>
    <td class="greyboxwk"><bean:message key="monthYr"/></td>
    <td class="greybox2wk">
    <html:select property="month" styleClass="selectwk">
    	<html:option value="0">Choose</html:option>
    	<html:option value="1">JAN</html:option>
	<html:option value="2">FEB</html:option>
	<html:option value="3">MAR</html:option>
	<html:option value="4">APR</html:option>
	<html:option value="5">MAY</html:option>
	<html:option value="6">JUN</html:option>
	<html:option value="7">JUL</html:option>
	<html:option value="8">AUG</html:option>
	<html:option value="9">SEP</html:option>
	<html:option value="10">OCT</html:option>
	<html:option value="11">NOV</html:option>
	<html:option value="12">DEC</html:option>
    	</html:select>
    	<html:select property="year" style="width:70px">
    	<html:option value="0">Choose</html:option>
    	<c:forEach var="financialYearObj" items="${financialYear}">
    	<c:if test = "${financialYearObj.isActive=='1'}">
    	<html:option value="${financialYearObj.id}">${financialYearObj.finYearRange}</html:option>
    	</c:if>
    	</c:forEach>
     	</html:select>
     	<html:hidden property="basicPay" styleId="basicPay"/>
    </td>
    <td class="greyboxwk"><bean:message key="paytype"/>
      </td>
      <td class="greybox2wk"><html:select styleClass="selectwk" property="payType"  styleId="payType">
      <html:options collection="paytypelist" property="id" labelProperty="paytype" />
      </html:select>
    </td>
  </tr>
  <tr>
	  <td class="whiteboxwk"><bean:message key="totalNoOfDays"/>:
	  </td>
	  
	  <html:hidden property="workingDays"/>
	  <td class="whitebox2wk"><input type="text" readOnly="true" id="noofdaysmonth" Class="selectwk"/>  
	  </td>
	<!--  <td class="labelcell">
		<bean:message key="noOfAbsents"/>:
	  </td>
	  <td class="fieldcell">
		<input type="text" readOnly="true" id="noofabsents"/>
	  </td>	-->
	  <td class="whiteboxwk">
		Paid days:
	  </td>
	  <td class="whitebox2wk">
		  <html:text property="numDays" readonly="true" styleId="numDays" styleClass="selectwk"/>
	  </td>
  </tr>
  <tr>
  	

	<td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
                  <div class="headplacer"><bean:message key="earnings"/></div></td>

  </tr>
  <table width="100%" cellpadding ="0" cellspacing ="0" border = "0" id="paytable">
  <tr>
      <td class="tablesubheadwk"><bean:message key="payHead"/></td>
      <td class="tablesubheadwk"><bean:message key="calType"/></td>
      <td class="tablesubheadwk">%</td>
      <td class="tablesubheadwk"><bean:message key="basis"/></td>
      <td class="tablesubheadwk"><bean:message key="amount"/> </td>
      <td class="tablesubheadwk">Add/Del</td>
      <!-- <td class="labelcell"><P align="center"><bean:message key="taxable"/> </p></td> -->
   </tr>
  
  <c:forEach var="payhead1" items="${SalaryPaySlipForm.payHead}" varStatus="order">     
  <tr id="earnings" >
     	 <td class="whitebox3wk">
     	 	<html:select styleClass="selectwk" property="payHead" onchange="checkDuplicate(this)" onblur="checkPctBasis(this);trim(this,this.value)">
     	    	<html:option value="0">Choose</html:option>
     	    	
				<c:forEach var="payHeadObj" items="<%=salaryCodes%>" >
				<c:if test = "${payHeadObj.categoryMaster.catType=='E'}">
				
				<c:choose>
				<c:when test="${payHeadObj.id==payhead1}">
				<option value="${payHeadObj.id}" selected=true>${payHeadObj.head}</option>
				</c:when>
				<c:otherwise>
     	 			   <option value="${payHeadObj.id}">${payHeadObj.head}</option>
     	 			</c:otherwise>
     	 			</c:choose>
				</c:if>
  			</c:forEach>
     	    </html:select>
     	   </td>
     	 <td class="whitebox3wk"><html:text styleClass="selectwk" property="calculationType"  style="width:140px" readonly="true" value="${SalaryPaySlipForm.calculationType[order.index]}"/>
  		 </td>
     	 <td class="labelcell">
     	 
     	 <c:choose>
     	 <c:when test="${SalaryPaySlipForm.pct[order.index]!= ''}" >     	 
  		<html:text  property="pct"  styleClass="selectwk" onblur="calAmount(this);trim(this,this.value)" readonly="false" value="${SalaryPaySlipForm.pct[order.index]}"/>
  	 </c:when>
  	 <c:otherwise>  	 
  	        <html:text  property="pct"  styleClass="selectwk" onblur="calAmount(this);trim(this,this.value)" readonly="true" value="${SalaryPaySlipForm.pct[order.index]}"/>
  	 </c:otherwise>
  	 </c:choose>
     	 </td>
     	 <td class="whitebox3wk"><html:text  property="pctBasis" styleClass="selectwk" readonly="true" value="${SalaryPaySlipForm.pctBasis[order.index]}"/>
  		 </td>
     	 <td class="whitebox3wk">
     	 <c:choose>
     	 <c:when test="${SalaryPaySlipForm.calculationType[order.index] == 'ComputedValue'}" >
     	 <html:text  property="payHeadAmount" styleClass="selectwk" style="text-align:right" onfocus="return removeZero(this)" onchange="return checkdecimalval(this,this.value)" readonly="true" onblur="return addZero(this);trim(this,this.value)" value="${SalaryPaySlipForm.payHeadAmount[order.index]}"/>
     	 </c:when>
  	 <c:otherwise>
  	 <html:text  property="payHeadAmount"styleClass="selectwk" style="text-align:right" onfocus="return removeZero(this)" onchange="return checkdecimalval(this,this.value)"  onblur="return addZero(this);trim(this,this.value)" value="${SalaryPaySlipForm.payHeadAmount[order.index]}"/>
  	 </c:otherwise>
  	 </c:choose>
     	 </td>
     	 <td class="whitebox3wk"  ><p>
     	
     	 <div align="center"><a href="#"><img src="../common/image/add.png" alt="Add" width="16" height="16" border="0" 
	 		onclick="whichButtonEarnings(event,'paytable',this,'earnings');" /></a> 
	 		<a href="#"><img src="../common/image/cancel.png" alt="Del" width="16" height="16" border="0" 
	 		onclick="deleteRow('paytable',this);"/></a></div></td>
     	  <!-- <td class="labelcell">&nbsp;&nbsp;&nbsp;&nbsp;<input  type="checkbox" name="isTaxable" id="isTaxable" style="width:50px"></td> -->
   </tr>
   </c:forEach>
   
   </table>
   <table id="grossPay" width="100%"><tr>
    <td  class="whitebox3wk"  width="65%"></td>
    <td class="whitebox3wk" >
	<bean:message key="grossPay"/></td>
	<td class="whitebox3wk">
	<html:text styleClass="selectwk" style="text-align:right" property="grossPay" onblur="return netPayCollection()" readonly="true"/></td>
    
    </tr>
     <tr>
      	<td colspan="5" align="center">
      	<html:button property="recompute" value="Recompute Deductions" onclick ="paintdedamts()" styleClass="buttonfinal"/>
      	</td>
  </tr>
  <tr>
    	

		<td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
                  <div class="headplacer"><bean:message key="deductions"/></div></td>
  </tr></table>
   
  <table  width="100%" cellpadding ="0" cellspacing ="0" border = "0" id="deductionTax">
    
   
  
  <tr>
     <td class="tablesubheadwk"  ><b><bean:message key="taxes"/></b></td>
     <td class="tablesubheadwk" ><b><bean:message key="amount"/></b></td>
	 <td class="tablesubheadwk"><b>Reference No</b></td>
	  <td class="tablesubheadwk"><b>Add/Del</b></td>
  </tr>
   
		 
		  <c:choose>
		  <c:when test="${flagForDedTax ==''}">
		  
				   <tr id="taxDeductions" >
				   <td class="whitebox3wk" >
				    <html:select property="taxTypeName" styleClass="selectwk"  > 
                                <html:option value="">-----------Choose-----------</html:option>					
								<c:forEach var="dedo" items="${dedListWithAmount}" > 
								<html:option value="${dedo.salaryCodes.id}">${dedo.salaryCodes.head}</html:option>
								</c:forEach>
					</html:select>
					</td>
					<td class="whitebox3wk">
						  <input class="selectwk" style="text-align:right" type="text" name="taxTypeAmount" id="taxTypeAmount" 
						  onfocus="return removeZero(this)" onchange="return checkdecimalval1(this,this.value)"  
						  onblur="return netPayCollection();trim(this,this.value)" value="" >
						  </td>
					    <td class="whitebox3wk" >
				        <input type="text" class="selectwk" name="referenceno" id="referenceno" onblur="checkForUniqueCombo(this,'tax');checkAlphaNumeric(this);" />
						</td>
						<td>
						<div align="center"><a href="#"><img src="../common/image/add.png" alt="Add" width="16" height="16" border="0" 
	 		onclick="whichButtonTaxDeductions(event,'deductionTax',this,'taxDeductions');" /></a> 
	 		<a href="#"><img src="../common/image/cancel.png" alt="Del" width="16" height="16" border="0" 
	 		onclick="deleteRow('deductionTax',this);"/></a></div>

					</td>

			       </tr>
		
		</c:when>
		<c:otherwise>
	    <c:forEach var="dedu" items="${dedListWithAmount}" > 
		
	    <c:if test ="${dedu.amount !=0 }">
							<tr id="taxDeductions" >
							<td class="whitebox3wk" >
							<html:select property="taxTypeName" styleClass="selectwk" value="${dedu.salaryCodes.id}" >   
                             <html:option value="">-----------Choose-----------</html:option>							
							<c:forEach var="dedo" items="${dedListWithAmount}" > 
								<html:option value="${dedo.salaryCodes.id}">${dedo.salaryCodes.head}</html:option>
							</c:forEach>
							</html:select>
							</td>
							
							<td class="whitebox3wk">
							<input class="selectwk" type="text" name="taxTypeAmount" id="taxTypeAmount" 
							onfocus="return removeZero(this)" onchange="return checkdecimalval1(this,this.value)"  
							onblur="return netPayCollection();trim(this,this.value)" value="${dedu.amount}" >
							</td>
							
							<td class="whitebox3wk" >
							<input type="text" class="selectwk" name="referenceno" id="referenceno" onblur="checkForUniqueCombo(this,'tax');checkAlphaNumeric(this);"/>
							</td> 
						


					<td>
						<div align="center"><a href="#"><img src="../common/image/add.png" alt="Add" width="16" height="16" border="0" 
	 		onclick="whichButtonTaxDeductions(event,'deductionTax',this,'taxDeductions');" /></a> 
	 		<a href="#"><img src="../common/image/cancel.png" alt="Del" width="16" height="16" border="0" 
	 		onclick="deleteRow('deductionTax',this);"/></a></div>

					</td>


							</tr>
		</c:if>
		</c:forEach>
		</tr>
		</c:otherwise>
		</c:choose>
  </table>
  
  <table  width="100%" cellpadding ="0" cellspacing ="0" border = "0" id="OtherdeductionTax">
  <tr>
       <td class="tablesubheadwk" width="32%"><b>Deduction-other</b></td>
       <td class="tablesubheadwk" width="31%"><b><bean:message key="amount"/></b></td>
	    <td class="tablesubheadwk" width="26%"><b>Reference No</b></td>
		<td class="tablesubheadwk" ><b>Add/Del</b></td>
  </tr>
  	 
     <c:choose>
	 
     <c:when test="${flagForDedOthr ==''}">
    
	  
	 
					    <tr id="othertaxDeductions" >
					    <td class="whitebox3wk" >
	                    <html:select property="dedOthrName" styleClass="selectwk" >
                         <html:option value="">-----------Choose-----------</html:option>							
						<c:forEach var="dedo" items="${dedOtherListWithAmount}" > 
						<html:option value="${dedo.salaryCodes.id}">${dedo.salaryCodes.head}</html:option>
						</c:forEach>
		                </html:select>
		                </td>
						<c:forEach var="dedu" items="${dedOtherListWithAmount}" > 
						<input type="hidden" name="dedOthrTxId" id="dedOthrTxId" value="0"/>
						</c:forEach>
						<td class="whitebox3wk">
						<input class="selectwk" style="text-align:right" type="text" name="dedOthrAmount" id="dedOthrAmount" 
						onfocus="return removeZero(this)" onchange="return checkdecimalval1(this,this.value)"  
						onblur="return netPayCollection();trim(this,this.value)" value="" >
						</td>
							
						<td class="whitebox3wk" >
						<input class="selectwk" type="text"  name="dedRefNo" id="dedRefNo" onblur="checkForUniqueCombo(this,'ded');checkAlphaNumeric(this);" />
						</td>
						

					

						<td>
						<div align="center"><a href="#"><img src="../common/image/add.png" alt="Add" width="16" height="16" border="0" 
	 		onclick="whichButtonOtherTaxDeductions(event,'OtherdeductionTax',this,'othertaxDeductions');"/></a> 
	 		<a href="#"><img src="../common/image/cancel.png" alt="Del" width="16" height="16" border="0" 
	 		onclick="deleteRow('OtherdeductionTax',this);"/></a></div>

					</td>


		                </tr>

	
	</c:when>
	<c:otherwise>
	 <c:forEach var="dedu" items="${dedOtherListWithAmount}" > 
	 <c:if test ="${dedu.amount !=0 }">
	 
	                     <tr id="othertaxDeductions" > 
					    <td class="whitebox3wk" >
	                    <html:select property="dedOthrName" styleClass="selectwk"   value="${dedu.salaryCodes.id}" >   
                          <html:option value="">-----------Choose-----------</html:option>							
						<c:forEach var="dedo" items="${dedOtherListWithAmount}" > 
						<html:option value="${dedo.salaryCodes.id}">${dedo.salaryCodes.head}</html:option>
						</c:forEach>
		                </html:select>
		                </td>
						<input type="hidden" name="dedOthrTxId" id="dedOthrTxId" value="0"/>
						<td class="whitebox3wk">
						<input class="selectwk" style="text-align:right" type="text" name="dedOthrAmount" id="dedOthrAmount" 
						onfocus="return removeZero(this)" onchange="return checkdecimalval1(this,this.value)"  
						onblur="return netPayCollection();trim(this,this.value)" value ="${dedu.amount}">
						</td>
						
							
						<td class="whitebox3wk" >
						<input type="text" class="selectwk" name="dedRefNo" id="dedRefNo"  onblur="checkForUniqueCombo(this,'ded');checkAlphaNumeric(this);" />
						</td>
						
						


						<td>
						<div align="center"><a href="#"><img src="../common/image/add.png" alt="Add" width="16" height="16" border="0" 
	 		onclick="whichButtonOtherTaxDeductions(event,'OtherdeductionTax',this,'othertaxDeductions');"/></a> 
	 		<a href="#"><img src="../common/image/cancel.png" alt="Del" width="16" height="16" border="0" 
	 		onclick="deleteRow('OtherdeductionTax',this);"/></a></div>

					</td>


		                </tr>
						
   </c:if >
	</c:forEach>
    </c:otherwise>
	</c:choose>
    </table>
    <table  width="100%"  cellpadding ="0" cellspacing ="0" border = "0" id="salAdvances">
       <tr>
          <td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
                  <div class="headplacer"><bean:message key="advAdjustments"/></div></td>

          
       </tr>
       <tr>
         <td class="tablesubheadwk" ><b>Deduction-Advance</b></td>
       <td class="tablesubheadwk" colspan="2"><b><bean:message key="amount"/></b></td>
       </tr>
       <tr id="advDeductions" >
       
       <td class="whitebox2wk">
           		<html:select styleClass="selectwk" property="salaryAdvances"  style="width:150px" 
           		onchange="populateInstAmount(this)" onblur="checksalAdvances(this)">
           		<html:option value="0">Choose</html:option>
           		<c:forEach var="adv" items="${salAdvances}">
					<c:if test = "${adv.pendingAmt!='0'}">
						<c:if test = "${adv.salaryCodes.categoryMaster.name=='Deduction-BankLoan'}">					
						    <c:if test = "${adv.status.code=='Disbursed'}">							 
								<html:option value="${adv.id}" >${adv.salaryCodes.head} ~ ${adv.advanceAmt} ~ ${adv.id}</html:option>
							</c:if>
						 </c:if>
						 <c:if test = "${adv.salaryCodes.categoryMaster.name=='Deduction-Advance'}">
							<c:if test = "${adv.isLegacyAdvance == 'Y'}">							 
								<html:option value="${adv.id}" >${adv.salaryCodes.head} ~ ${adv.advanceAmt} ~ ${adv.id}</html:option>
							</c:if>
							<c:if test = "${adv.isLegacyAdvance=='N' && adv.salaryARF.egAdvanceReqMises.voucherheader!=null && adv.salaryARF.egAdvanceReqMises.voucherheader.status==0}">							 
								<html:option value="${adv.id}" >${adv.salaryCodes.head} ~ ${adv.advanceAmt} ~ ${adv.id}</html:option>
							</c:if>
						 </c:if>	
					</c:if>
           		</c:forEach>
           		</html:select>
              	 </td>
				 <input type="hidden" name="advanceSchedule" id="advanceSchedule" value="">
              	 <input type="hidden" name="salaryAdvPendingAmount" id="salaryAdvPendingAmount" >
              	 
            	 <td class="whitebox2wk">
					<html:text styleClass="selectwk"  style="text-align:right" property="salaryAdvancesAmount"    onfocus="return removeZero(this)" 
					onchange="return checkdecimalval1(this,this.value)"  
					onblur="return checkAmount(this);netPayCollection();trim(this,this.value)"/>
				</td>
            	

		 <td>
						<div align="center"><a href="#"><img src="../common/image/add.png" alt="Add" width="16" height="16" border="0" 
	 		onclick="whichButtonEarningsDeductions(event,'salAdvances',this,'advDeductions');"/></a> 
	 		<a href="#"><img src="../common/image/cancel.png" alt="Del" width="16" height="16" border="0" 
	 		onclick="deleteRow('salAdvances',this);"/></a></div>

					</td>


        </tr>
    </table>
    <table  width="100%" cellpadding ="0" cellspacing ="0" border = "0" id="deductions">
     <tr>
        
		<td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
                  <div class="headplacer"><bean:message key="otherDeductions"/></div></td>
     </tr>
     <tr>
     <td class="tablesubheadwk" style="text-align:right">Account Code</td>
     <td class="tablesubheadwk" style="text-align:right">Name</td>
      <td class="tablesubheadwk" style="text-align:right" ><b><bean:message key="amount"/></b></td>
      <td class="tablesubheadwk" style="text-align:right">&nbsp;</td>
     </tr>
     <%
          if(deductionsotherList!=null && deductionsotherList.size()!=0)
          {
          System.out.println("if(deductionsotherList.size()!=0)   "  + deductionsotherList.size());
     		 for(Iterator iter5 = deductionsotherList.iterator(); iter5.hasNext();)
     	  {
            		Deductions salDed2 = (Deductions) iter5.next();
     		%>
     			  <tr id="otherDed" >
     			  <input type="hidden" name="otherDedId" id="otherDedId" value="<%= salDed2.getId() %>"/>
     			 <input type="hidden" name="accountCodeId" id="accountCodeId" value="<%=salDed2.getChartofaccounts().getId().toString()%>" />
     			 <td class="whiteboxwk">
     			 <input type="text"  class="selectwk" name="otherDeductAccountCode" id ="otherDeductAccountCode" value="<%=salDed2.getChartofaccounts().getGlcode().toString() %>" autocomplete="off"   onkeyup="autocompleteDeduc(this,event);" 
     			 onblur="fillNeibrAfterSplit1(this,'accountCodeId');checkOtherDedCode(this);"/>
     			</td>
     			 <td class="whiteboxwk"><input type="text"  class="fieldcell" name="otherDeductAccountDescription" id ="otherDeductAccountDescription" value="<%=salDed2.getChartofaccounts().getName() %>" readOnly>
     			</td>
     			 <td class="whiteboxwk">
     			 <input class="selectwk" type="text"  style="text-align:right" name="otherDeductionsAmount"  value="<%=salDed2.getAmount().toString() %>" onfocus="return removeZero(this)" onchange="return checkdecimalval1(this,this.value)"  onblur="netPayCollection();trim(this,this.value);
     			 populateOtherDedYTD(this);"/>
     			 </td>
      			
					 <td>
						<div align="center"><a href="#"><img src="../common/image/add.png" alt="Add" width="16" height="16" border="0" 
	 		onclick="whichButtonEarnings2(event,'deductions',this,'otherDed');"/></a> 
	 		<a href="#"><img src="../common/image/cancel.png" alt="Del" width="16" height="16" border="0" 
	 		onclick="deleteRow('deductions',this);"/></a></div>

					</td>

     		  </tr>
     		<%}
     	   }
       	     else
     		 {%>
     <tr id="otherDed" >
          <input type="hidden" name="accountCodeId" id="accountCodeId">
          <td class="whiteboxwk"><input type="text"  class="selectwk" name="otherDeductAccountCode" id ="otherDeductAccountCode" value="" autocomplete="off"   onkeyup="autocompleteDeduc(this,event);" onblur="fillNeibrAfterSplit1(this,'accountCodeId');checkOtherDedCode(this);trim(this,this.value)">
     	</td>
     	 <td class="whiteboxwk"><input type="text"  class="selectwk" name="otherDeductAccountDescription" id ="otherDeductAccountDescription" readOnly>
     	</td>
             <td class="whiteboxwk"><html:text styleClass="selectwk"  style="text-align:right"property="otherDeductionsAmount"  onfocus="return removeZero(this)" onchange="return checkdecimalval1(this,this.value)"  onblur="return netPayCollection();trim(this,this.value)"/></td>
     	
			<td>
						<div align="center"><a href="#"><img src="../common/image/add.png" alt="Add" width="16" height="16" border="0" 
	 		onclick="whichButtonEarnings2(event,'deductions',this,'otherDed');"/></a> 
	 		<a href="#"><img src="../common/image/cancel.png" alt="Del" width="16" height="16" border="0" 
	 		onclick="deleteRow('deductions',this);"/></a></div>

					</td>


      </tr>
<%
}
%>
     
  </table>
   <table width="100%" cellpadding ="0" cellspacing ="0" border = "0" >
   <tr>
   		<td class="whiteboxwk" width="66%"></td>
   		<td class="whiteboxwk" ><bean:message key="totalDeductions"/></td>
        <td class="whitebox2wk"><input class="selectwk" style="text-align:right" type="text" name="totalDeductions"  id="totalDeductions" readOnly></td>
   </tr>
   <tr>
   		<td class="whiteboxwk" width="66%"></td>
       <td class="whiteboxwk" ><bean:message key="netPay"/></td>
        <td class="whitebox2wk"><html:text styleClass="selectwk" style="text-align:right" property="netPay"  readonly="true"/></td>
  </tr>
  </table> 
  
		<%		
		if("Manual".equals(payslipWfType)){%>
			<%@ include file='manualWfApproverSelection.jsp'%>	
		<%}%>


</table>
</center>
</div>
</td>
</tr>
</table>
<table align="center">
<tr>
<td><input type="button" class="buttonfinal" name="save" value="Save & View" onclick="return validation('save',this);"/></td>
<td><input type="button" class="buttonfinal" name="save & new" value="Save & New" onclick="return validation('saveNew',this);"/></td>
<td><input type="button" class="buttonfinal" name="cancel" value="Cancel" onclick="history.go(0)"/></td>
</tr>
<% session.removeAttribute("financialYear");
session.removeAttribute("payHeader");

%>
</table>
</html:form>
<%}%>
</body>
</html>
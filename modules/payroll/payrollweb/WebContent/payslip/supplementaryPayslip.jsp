<%@ include file="/includes/taglibs.jsp" %>
<%@page	import="java.util.*,org.egov.infstr.utils.*,org.egov.payroll.services.payslip.*,org.egov.payroll.utils.PayrollManagersUtill,java.sql.Connection"%>
<%@page	import="org.egov.infstr.utils.HibernateUtil,java.text.SimpleDateFormat,org.egov.commons.service.CommonsService,org.egov.payroll.utils.PayrollConstants,java.util.Set,org.egov.payroll.utils.*"%>
<html>

<head>


<title>Supplementary payslip</title>

<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/commonyui/examples/autocomplete/css/examples.css">

<style type="text/css">
		#codescontainer {position:absolute;left:11em;width:9%}
		#codescontainer .yui-ac-content {position:absolute;width:80%;border:1px solid #404040;background:#fff;overflow:hidden;z-index:9050;}
		#codescontainer .yui-ac-shadow {position:absolute;margin:.3em;width:40%;background:#a0a0a0;z-index:9049;}
		#codescontainer ul {padding:5px 0;width:80%;}
		#codescontainer li {padding:0 5px;cursor:default;white-space:nowrap;}
		#codescontainer li.yui-ac-highlight {background:#ff0;}
		#codescontainer li.yui-ac-prehighlight {background:#FFFFCC;}
</style>

<script language="JavaScript" type="text/JavaScript">
		var empCodeArray;
		var selectedEmpCode;
		var yuiflag = new Array();
		var employeeName;	
		var designation;
		var department;
		var yearOfJoining;
		var empcode;
		var month;
		var finYr;
	//payScaleName=temp[6];

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
  	if(yuiflag[currRow.rowIndex] == undefined)
  	{  	
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
	document.salaryPaySlipForm.checkEmpCode.value = temp[1];
 	obj.value=temp[0]; 	
	if(temp[1] == null){
		if(temp[0] != "")
			getEmployeeByEnteringCode(temp[0],neibrObj);	
	}
 	if(temp[2]==null || temp[2]=="") { neibrObj.value=""; return; }
 	if(temp[1]==null && (neibrObj.value!='' || neibrObj.value!=null) ) {  return ;}
 	else {
			document.getElementById("employeeName").value = temp[1];
			
 			neibrObj.value=temp[2];
 		 }
  }

   function getEmployeeByEnteringCode(code,empId){
		var url = "<%=request.getContextPath()%>"+"/commons/process.jsp?type=getActiveEmployeeByCode&code="+code;
      	var req = initiateRequest();
      	req.onreadystatechange = function(){
	      if (req.readyState == 4){
	            if (req.status == 200){
                   	var glcodes=req.responseText
                   	var a = glcodes.split("^");
                   	var codes = a[0];												
					var emp = codes.split("`-`");
					if(codes == "false"){						
					}
					else{
						empId.value = emp[2];
						document.salaryPaySlipForm.checkEmpCode.value = emp[1];  							
						if(emp[1]!=null)
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
 	 var type='getEmployedEmpCodes';
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

 //tocheck the payslip exists in db ,if so give alert msg
 function checkUnique(tablename,fieldname,fieldvalue,fieldname1,fieldvalue1,fieldname2,fieldvalue2)
  {
 	var type = "checkUniqueness";
 	var link = "<%=request.getContextPath()%>"+"/commons/checkingPayslip.jsp?type=" + type+"&tablename=" + tablename+"&fieldname=" + fieldname+ "&fieldvalue=" + fieldvalue+"&fieldname1=" + fieldname1+ "&fieldvalue1=" + fieldvalue1+"&fieldname2=" + fieldname2+ "&fieldvalue2=" + fieldvalue2+ " ";
 	var request = initiateRequest();
 	var isUnique;
 	request.onreadystatechange = function(){
	 	if (request.readyState == 4){
		 	if (request.status == 200)
		 	{
			 	var response=request.responseText;
			 	var result = response.split("/");
		 		if(result[0]=="true"){	 			
		 			isUnique="true";
		 		}
		 		else if(result[0]=="false"){	
		 			isUnique="false";
		 		}
		 	 }
 	    }
 	};
 	request.open("GET", link, false);
 	request.send(null);
 	return isUnique;
}

function onBodyLoad()
{
	loadEmpCodes();
	<%
		String currDate="";
		Connection conn=null;		
	 	//conn = HibernateUtil.getCurrentSession().connection();
	 	PayrollExternalInterface payrollExternalInterface=new PayrollExternalImpl();
 		currDate=  payrollExternalInterface.getCurrentDate() ;
		String currFinYr=(String)request.getAttribute("currFinYr");
	%>
	var currentdate = "<%=currDate%>"; 
	finYr = "<%=currFinYr %>" ;	
	if(currentdate.split("/")[1].charAt(0)=='0')
	  month=currentdate.split("/")[1].charAt(1);
	else
 	  month=currentdate.split("/")[1];
 	  
 	var paytype=document.getElementById("payType");
	var i=1;       				
	paytype.options.length=1;       				
	<c:forEach var="paytype" items="${paytypelist}">        				  
	 
	  if("${paytype.paytype}" != "<%=PayrollConstants.EMP_PAYSLIP_PAYTYPE_NORMAL%>" && "${paytype.paytype}" != "<%=PayrollConstants.EMP_PAYSLIP_PAYTYPE_EXCEPTION%>" )
	  {
	    paytype.options[i++] = new Option('${paytype.paytype}','${paytype.id}');
	  }
	  <c:if test="${salaryPaySlipForm.payType==paytype.id}">       				 
	   paytype.selectedIndex=i-1; 				    
	  </c:if>
	</c:forEach>

}

 function checkPaidLeave(){
 	var type = "checkPaidLeave"; 	
 	var empId = document.getElementById("employeeCodeId").value;
 	var month = document.salaryPaySlipForm.month.value;
 	var year = document.salaryPaySlipForm.year.value;
 	var link = "<%=request.getContextPath()%>"+"/commons/checkingPayslip.jsp?type="+ type +"&empId="+empId +"&month="+month +"&year="+year;
 	var request = initiateRequest();
 	var noOfLeaves;
 	request.onreadystatechange = function(){
	 	if (request.readyState == 4){
		 	if (request.status == 200)
		 	{
			 	var response=request.responseText;
			 	var result = response.split("/");
		 	//	alert(result[0]);
		 		noOfLeaves = result[0];		 		
		 	 }
 	    }
 	};
 	request.open("GET", link, false);
 	request.send(null);
 	return noOfLeaves;
 }
 
 function populatePaidDays(obj){  
 	if(document.salaryPaySlipForm.payType.options[document.salaryPaySlipForm.payType.selectedIndex].text=="<%=PayrollConstants.EMP_PAYSLIP_PAYTYPE_FINAL_SETTLEMENT%>"){	 
 		alert("Attention! Please make sure that the Final Settlement Report has been signed off before proceeding");	
 		document.salaryPaySlipForm.numDays.value="";
 	}	
 	if(document.getElementById("employeeCodeId").value!="" && document.salaryPaySlipForm.month.value!="0" && document.salaryPaySlipForm.year.value!="0" && document.salaryPaySlipForm.payType.options[document.salaryPaySlipForm.payType.selectedIndex].text=="<%=PayrollConstants.EMP_PAYSLIP_PAYTYPE_LEAVE_ENCASHMENT%>"){
	 	var type = "checkPaidLeave"; 	 	
	 	var empId = document.getElementById("employeeCodeId").value;
	 	var month = document.salaryPaySlipForm.month.value;
	 	var year = document.salaryPaySlipForm.year.value;
		 	var link = "<%=request.getContextPath()%>"+"/commons/checkingPayslip.jsp?type="+ type +"&empId="+empId +"&month="+month +"&year="+year;
		 	var request = initiateRequest();
		 	var noOfLeaves;
		 	request.onreadystatechange = function(){
			 	if (request.readyState == 4){
				 	if (request.status == 200)
				 	{
					 	var response=request.responseText;
					 	var result = response.split("/");
				 	//	alert(result[0]);
				 		noOfLeaves = result[0];		
				 		document.salaryPaySlipForm.numDays.value=noOfLeaves;
				 	 }
		 	    }
		 	};
		 	request.open("GET", link, false);
		 	request.send(null);
		 //	return noOfLeaves;	
	}
 }

 function checkEmpAssignment(){
	//	alert("dasdas");
		var type = "checkEmpAssignment";
		var month = document.getElementById("month").value ;
		var year = document.getElementById("year").value;
		var empid = document.getElementById("employeeCodeId").value;
		var link = "<%=request.getContextPath()%>"+"/commons/checkingPayslip.jsp?type=" + type+"&month=" + month+"&year=" + year+ "&empid=" + empid+ " ";
		var request = initiateRequest();
		var isUnique;
		request.onreadystatechange = function(){
			if (request.readyState == 4){
				if (request.status == 200){
					var response=request.responseText;
					var result = response.split("/");
				//	alert(result[0]);
					if(result[0]=="true"){
						isUnique="true";
					}
					else if(result[0]=="false"){
						isUnique="false";
					}
				}
			}
		};
		request.open("GET", link, false);
		request.send(null);	
		return isUnique;		
	}

	function checkEmpPayscale(){
//		alert("checkPayscale");
		var type = "checkEmpPayscale";
		var month = document.getElementById("month").value ;
		var year = document.getElementById("year").value;
		var empid = document.getElementById("employeeCodeId").value;
		var link = "<%=request.getContextPath()%>"+"/commons/checkingPayslip.jsp?type=" + type+"&month=" + month+"&year=" + year+ "&empid=" + empid+ " ";
		var request = initiateRequest();
		var isUnique;
		request.onreadystatechange = function(){
			if (request.readyState == 4){
				if (request.status == 200){
					var response=request.responseText;
					var result = response.split("/");
				//	alert(result[0]);
					if(result[0]=="true"){
						isUnique="true";
					}
					else if(result[0]=="false"){
						isUnique="false";
					}
				}
			}
		};
		request.open("GET", link, false);
		request.send(null);	
		return isUnique;
	}

 function validation(arg){   	
  document.getElementById("numDays").disabled = false;	if(document.salaryPaySlipForm.payType.options[document.salaryPaySlipForm.payType.selectedIndex].text=="<%=PayrollConstants.EMP_PAYSLIP_PAYTYPE_LEAVE_ENCASHMENT%>"){
		if(document.salaryPaySlipForm.leaveApplication.value == ""){
			alert("Select leave application");
			document.salaryPaySlipForm.leaveApplication.focus();
			return false;
		}		
  }
  if(document.salaryPaySlipForm.checkEmpCode.value == "undefined"){
		alert("Enter correct employee code");	
		document.salaryPaySlipForm.employeeCode.focus();
		return false;
  }	
  if(checkPaySlipDate()=="false"){
	alert("Afeter Emp First Appointment Date only we can create payslip");
	//document.getElementById("employeeCode").value="";
	return false;
  }
  if(checkEmpAssignment()=="false"){
		alert("Employee not assigned for this period");
		//document.getElementById("employeeCode").value="";
		return false;		
  }
  if(checkEmpPayscale()=="false"){
		alert("Payscale not assigned for this Employee for this period ");
		//document.getElementById("employeeCode").value="";
		return false;		
  }
 /*if(document.salaryPaySlipForm.numDays.value != ""){
		 	var leave = checkPaidLeave();		 	
			if(eval(document.salaryPaySlipForm.numDays.value) > eval(leave) && document.salaryPaySlipForm.payType.options[document.salaryPaySlipForm.payType.selectedIndex].text=="<%=PayrollConstants.EMP_PAYSLIP_PAYTYPE_LEAVE_ENCASHMENT%>"){
				alert("Paid days can't be greater than "+leave);
				document.salaryPaySlipForm.numDays.focus();
				return false;
			}	
	  }*/
	 if(document.getElementById("employeeCodeId").value=="")
	 {
		 alert("Please select the Employee Code from the drop down");
		 document.getElementById("employeeCode").focus();
		 return false;
	 }
	// alert(document.salaryPaySlipForm.payType.options[document.salaryPaySlipForm.payType.selectedIndex].text);
	/* if(document.salaryPaySlipForm.payType.options[document.salaryPaySlipForm.payType.selectedIndex].text!="<%=PayrollConstants.EMP_PAYSLIP_PAYTYPE_ARREAR_PAYMENT%>")
	 if(checkUnique('EGPAY_EMPPAYROLL','ID_EMPLOYEE',document.getElementById("employeeCodeId").value,'MONTH',document.salaryPaySlipForm.month.options[document.salaryPaySlipForm.month.selectedIndex].value,'FINANCIALYEARID',document.salaryPaySlipForm.year.options[document.salaryPaySlipForm.year.selectedIndex].value)=="true")
	 {
	 	alert("For this employee payslip of this year and month already exists");
		return false;
	 }	*/
	 if(document.salaryPaySlipForm.month.options[document.salaryPaySlipForm.month.selectedIndex].value =="0")
	   {
		   alert("Please Select the Month!!!");
		   document.salaryPaySlipForm.month.focus();
		   return false;
	   }
	   if(document.salaryPaySlipForm.year.options[document.salaryPaySlipForm.year.selectedIndex].value =="0")
	   {
		   alert("Please Select the Year!!!");
		   document.salaryPaySlipForm.year.focus();
		   return false;
	   }
	   if(document.salaryPaySlipForm.numDays.value==""){
			alert("Please enter paid days");
			document.salaryPaySlipForm.numDays.focus();
			return false;
	   }
	   if(document.salaryPaySlipForm.workingDays.value==""){
			alert("Please enter working days");
			document.salaryPaySlipForm.workingDays.focus();
			return false;
	   }
	   if(document.salaryPaySlipForm.payType.value==""){
			alert("Please select pay type");
			document.salaryPaySlipForm.payType.focus();
			return false;
	   }
	   /* Paid days can be greater than working days in all cases
	   if(document.salaryPaySlipForm.payType.options[document.salaryPaySlipForm.payType.selectedIndex].text != "<%=PayrollConstants.EMP_PAYSLIP_PAYTYPE_LEAVE_ENCASHMENT%>"){	
		   if(eval(document.salaryPaySlipForm.numDays.value) > eval(document.salaryPaySlipForm.workingDays.value)){
				alert("Paid days can't be greater than working days");
				document.salaryPaySlipForm.workingDays.focus();
				return false;
		   }
	   }*/
	   if(document.salaryPaySlipForm.supplComment.value==""){
	   		alert("Enter the Reason");
	   		document.salaryPaySlipForm.supplComment.focus();
	   		return false;
	   }
	   
		var fId = document.salaryPaySlipForm.year.options[document.salaryPaySlipForm.year.selectedIndex].value;
		//alert(fId);
	   <c:forEach var="financialYearObj" items="${financialYears}">
				if(fId == ${financialYearObj.id}){
					//alert('${financialYearObj.finYearRange}');
					var a = '${financialYearObj.finYearRange}';
					//alert(a.split('-')[0]);
					var b = '${currentFinancialYear.finYearRange}';
					//alert(b.split('-')[0]);
				   if(eval(a.split('-')[0]) > eval(b.split('-')[0]))
				   {
					   alert("Can't Create or Alter the payslips For future dates");
					   document.salaryPaySlipForm.year.focus();
					   return false;	
				   }else if (document.salaryPaySlipForm.year.options[document.salaryPaySlipForm.year.selectedIndex].value == eval(finYr)) {

						var currmonth1,ipmonth2;
						if(eval(month)<4)
						{
						  currmonth1=eval(month)+12;
						}else
						{
						  currmonth1=eval(month);
						}
						//alert(currmonth1);
						if(eval(document.salaryPaySlipForm.month.options[document.salaryPaySlipForm.month.selectedIndex].value)<4)
						{
						  ipmonth2=eval(document.salaryPaySlipForm.month.options[document.salaryPaySlipForm.month.selectedIndex].value)+eval(12);
						}else
						{
						  ipmonth2=eval(document.salaryPaySlipForm.month.options[document.salaryPaySlipForm.month.selectedIndex].value);
						}
						//alert(ipmonth2);
						if(currmonth1 < eval(ipmonth2))
						{
						   alert("Can't Create or Alter the payslips For feture dates");
						   document.salaryPaySlipForm.month.focus();
						   return false;
						}
					}
					/*alert(compareDate("${financialYearObj.startingDate}","${currentFinancialYear.startingDate}"));
					if(compareDate('${financialYearObj.startingDate}','${currentFinancialYear.startingDate}') == 1){
						alert("dasdsadasd");
					}*/
				}
	   </c:forEach>

	  
	   document.salaryPaySlipForm.action ="${pageContext.request.contextPath}/payslip/generateSuppPaySlip.do?submitType=leaveEncashmentPayslip&mode=create&frwdType=supplementary";
	   document.salaryPaySlipForm.submit();
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

function checkPaySlipDate()
{

	var type = "checkPaySlipDateTOAPMDT";
	var month = document.getElementById("month").value ;
	var year = document.getElementById("year").value;
	var empid = document.getElementById("employeeCodeId").value;
	var link = "<%=request.getContextPath()%>"+"/commons/checkingPayslip.jsp?type=" + type+"&month=" + month+"&year=" + year+ "&empid=" + empid+ " ";
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

function populateDaysInMonth(){
	document.salaryPaySlipForm.workingDays.value = 30;
}

function populatePayDetails(obj){		
	document.getElementById("numDays").disabled = false;	
	document.getElementById("leaveEncashmentRowId").style.display = "none";
	document.getElementById("leaveEncashmentRow").style.display = "none";	
	if(document.salaryPaySlipForm.payType.options[document.salaryPaySlipForm.payType.selectedIndex].text=="<%=PayrollConstants.EMP_PAYSLIP_PAYTYPE_FINAL_SETTLEMENT%>"){	 
			alert("Attention! Please make sure that the Final Settlement Report has been signed off before proceeding");	
			document.salaryPaySlipForm.numDays.value="";
			document.getElementById("leaveEncashmentRowId").style.display = "none";
			document.getElementById("leaveEncashmentRow").style.display = "none";
	}	if(document.salaryPaySlipForm.payType.options[document.salaryPaySlipForm.payType.selectedIndex].text=="<%=PayrollConstants.EMP_PAYSLIP_PAYTYPE_ARREAR_PAYMENT%>"){	 
			//alert("Check report");	
			document.salaryPaySlipForm.numDays.value="";
			document.getElementById("leaveEncashmentRowId").style.display = "none";
			document.getElementById("leaveEncashmentRow").style.display = "none";
	}	if(document.salaryPaySlipForm.payType.options[document.salaryPaySlipForm.payType.selectedIndex].text=="<%=PayrollConstants.EMP_PAYSLIP_PAYTYPE_LEAVE_ENCASHMENT%>"){
		document.getElementById("leaveEncashmentRowId").style.display = "";
		document.getElementById("leaveEncashmentRow").style.display = "";
		for(var resLen=1;resLen<document.salaryPaySlipForm.leaveApplication.length;resLen+1){
				document.salaryPaySlipForm.leaveApplication.options[resLen]=null;
		}
		var empCode = document.salaryPaySlipForm.employeeCode.value;
		var leaveApplication = document.getElementById("leaveApplication");
		var i = 1;
		<c:forEach var="leaveApplObj" items="${leaveApplList}">			
			if(empCode == ${leaveApplObj.employeeId.employeeCode}){				
				 leaveApplication.options[i++] = new Option('${leaveApplObj.applicationNumber}','${leaveApplObj.id}');
			}			
		</c:forEach>
	}
}


function populateLeavePaidDays(obj){	
	var applicationId = obj.value;
	<c:forEach var="leaveApplObj" items="${leaveApplList}">			
		if(applicationId == ${leaveApplObj.id}){
			document.salaryPaySlipForm.numDays.value = "${leaveApplObj.workingDays}";
			//alert("dasd"+document.getElementById("numDays").value);
			document.getElementById("numDays").disabled = true;	
		}
	</c:forEach>
}
 var empCodeSelectionHandler = function(sType, arguments)
    { 
        var oData = arguments[2];
	 	var empDetails = oData[0];
	 	var empCode = empDetails.split(EMPCODE_SEP)[0];
	 	var empName = empDetails.split(EMPCODE_SEP)[1];
	 	document.getElementById("employeeCodeId").value = oData[1];	 	
	 	document.getElementById("employeeCode").value = empCode;
	 	document.getElementById("checkEmpCode").value = empCode;	 	
		document.getElementById("employeeName").value	=empName;
	 	empcode=empCode;
 	}
    var empCodeSelectionEnforceHandler = function(sType, arguments) {
      		warn('improperEmpCodeSelection');
  	}

   	function paramsFunction()
   	{ return "type=AllEmployeeCodes";
   	}
</script>
</head>

<body onLoad="onBodyLoad();">
<html:form method="POST" action="/payslip/viewGenPaySlips">

	<input type="hidden" name="checkEmpCode" id="checkEmpCode"/>

	<table width="95%" cellpadding="0" cellspacing="0"
		border="0" id="employee">
		<tr>
			<td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
                  <div class="headplacer">Suplementary Payslip</div></td>
		</tr>
		

  <tr>
	<input type="hidden" name="employeeCodeId" id="employeeCodeId">
  	<td class="whiteboxwk"><span class="mandatory">*</span>Employee Code</td>
  	<td class="whitebox2wk" valign="top">			
		<div class="yui-skin-sam">
    	<div id="empSearch_autocomplete">
    	<div>
    	    <input type="text"  class="selectwk" name="employeeCode" id="employeeCode" />		    	    
    	   			    	   
    	</div>
   	    <span id="empCodeSearchResults"></span>
    	</div>
    	</div>
    	
   	    <egovtags:autocomplete name="employeeCode"  field="employeeCode" 
   	    	url="${pageContext.request.contextPath}/common/employeeSearch!getEmpListByEmpCodeLike.action" queryQuestionMark="true" paramsFunction="paramsFunction" results="empCodeSearchResults" 
   	    	handler="empCodeSelectionHandler" forceSelectionHandler="empCodeSelectionEnforceHandler"/>
   	    <span class='warning' id="improperempCodeSelectionWarning"></span>		
			  
	</td> 	
  	<td class="whiteboxwk">Employee name</td>
  	<td class="whitebox2wk">
	  	<input type="text"  class="selectwk" name="employeeName" id="employeeName" readonly="readonly"/>
  	</td>  	
  </tr>	  
  <tr>	
  	<td class="greyboxwk"><span class="mandatory">*</span>Month</td>
	    <td class="greybox2wk">
		<select name="month" id="month" styleClass="selectwk" onblur="populateDaysInMonth();">
	    	<option value="0">Choose</option>
	    	<option value="1">JAN</option>
	    	<option value="2">FEB</option>
	    	<option value="3">MAR</option>
	    	<option value="4">APR</option>
	    	<option value="5">MAY</option>
	    	<option value="6">JUN</option>
	    	<option value="7">JUL</option>
	    	<option value="8">AUG</option>
	    	<option value="9">SEP</option>
	    	<option value="10">OCT</option>
	    	<option value="11">NOV</option>
	    	<option value="12">DEC</option>
	    	</select>
	    </td>
	    <td class="greyboxwk"><span class="mandatory">*</span>Year</td>
	    <td class="greybox2wk">	
	    	<select name="year" id="year" styleClass="selectwk" onblur="populateDaysInMonth();" >
				<option value="0">Choose</option>
				<c:forEach var="financialYearObj" items="${financialYears}">
				<c:if test = "${financialYearObj.isActive=='1'}">
				<option value="${financialYearObj.id}">${financialYearObj.finYearRange}</option>
	    		</c:if>
	    	</c:forEach>
	     	</select>
	    </td>
  </tr>
  <tr>
  	<td class="whiteboxwk"><span class="mandatory">*</span>Type</td>
  	<td class="whitebox2wk">
  		<html:select property="payType" styleId="payType" styleClass="selectwk" onblur="populatePayDetails(this);">
			<html:option value="">---------Choose---------</html:option>		
	    	<html:options collection="paytypelist" property="id" labelProperty="paytype" />
	    </html:select>	
  	</td>
	<td class="whiteboxwk" id="leaveEncashmentRowId" style="display:none">Leave Application<font color="red">*</font></td>
  	<td class="whitebox2wk" id="leaveEncashmentRow" style="display:none">
  		<html:select property="leaveApplication" styleId="leaveApplication" styleClass="selectwk" onblur="populateLeavePaidDays(this);">
			<html:option value="">---------Choose--------</html:option>
	    <!--	<html:options collection="leaveApplList" property="applicationNumber" labelProperty="applicationNumber" />	-->
	    </html:select>	
  	</td>
  </tr>
  <tr>
  	<td class="greyboxwk"><span class="mandatory">*</span>Paid Days</td>
  	<td class="greybox2wk">
	  	<input type="text" class="selectwk" name="numDays" id="numDays" onchange="return checkdecimalval(this,this.value)"
	  		onblur="trim(this,this.value);"/>
  	</td>  	
  	<td class="greyboxwk"><span class="mandatory">*</span>Total Days</td>
  	<td class="greybox2wk">
	  	<input type="text" class="selectwk" name="workingDays" id="workingDays" onchange="return checkdecimalval(this,this.value)"
	  		onblur="trim(this,this.value);" readOnly="true"/>
  	</td>  	
  </tr>
  <tr>
	<td class="whiteboxwk"><span class="mandatory">*</span>Reason</td>
  	<td class="whitebox2wk" colspan="6">
		<input type="text" name="supplComment" id="supplComment" class="selectwk" />
  	</td>
   </tr>
  
  </table>
  <table  width="95%" cellpadding ="0" cellspacing ="0" border = "0" >
	  <tr>
	  <td align="center">
	  	<input type="button" class="buttonfinal" name="create" value="Create" onclick="return validation('create');"/>
	  </td>	  
	  </tr>
  </table>

   <tr>
     <td colspan="4" class="shadowwk"></td>
   </tr>
   <tr>
     <td colspan="4"><div align="right" class="mandatory">* Mandatory Fields</div></td>
   </tr>



</html:form>

<%session.removeAttribute("payHeader");%>

</body>
</html>

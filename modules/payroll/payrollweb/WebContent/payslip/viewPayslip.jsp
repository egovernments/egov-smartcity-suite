<%@ include file="/includes/taglibs.jsp" %>
<%@page
	import="java.util.*,java.sql.Connection,
                org.egov.infstr.utils.*,
                org.egov.payroll.services.payslip.*,
		org.egov.payroll.utils.*,
		org.egov.infstr.commons.dao.*,
		org.egov.infstr.utils.HibernateUtil,
		org.egov.pims.service.*,
		org.egov.commons.service.CommonsService"%>

<%@page import="java.util.*"%>


<html>

<head>


<title><bean:message key="viewpayslip.title"/></title>
<script language="JavaScript" src="/javascript/dateValidation.js"
	type="text/JavaScript"></script>

<link type="text/css" rel="stylesheet"
	href="/commonyui/examples/autocomplete/css/examples.css">


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

var codeSelectionHandler = function(sType, arguments) {
    var oData = arguments[2];
    var billDetails = oData[0];
    var billNumberId = oData[1];
    document.getElementById('billNumber').value = billDetails;
    document.getElementById('billNumberId').value = billNumberId;
}
var codeSelectionEnforceHandler = function(sType, arguments) {
    warn('impropercodeSelection');
}

var month="",dept="0",fin_Year="",flag="no";

		var empCodeArray;
		var selectedEmpCode;
		var yuiflag = new Array();
		var employeeName;	
		var designation;
		var department;
		var yearOfJoining;
		var empcode;
		var month=0;
		var finYr;
	//payScaleName=temp[6];

<% 	
		String currDate="";
		Connection conn=null;
		String currFinYr="";
 	try{
 		
 		//conn = HibernateUtil.getCurrentSession().connection();
 		PayrollExternalInterface payrollExternalInterface=new PayrollExternalImpl();
 		currDate=  payrollExternalInterface.getCurrentDate() ;
 		System.out.println(currDate.split("/")[1]);
 		currFinYr=(String)session.getAttribute("currFinYr");
 	}
 	catch(Exception e){
 		e.printStackTrace();	
 	}
 
	%>

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
 
  var empCodeSelectionHandler = function(sType, arguments)
     { 
         var oData = arguments[2];
 	 	var empDetails = oData[0];
 	 	var empCode = empDetails.split(EMPCODE_SEP)[0];
 	 	var empName = empDetails.split(EMPCODE_SEP)[1];
 	 	dom.get("employeeCodeId").value = oData[1];	 	
 	 	document.getElementById("employeeCode").value = empCode;
 	 	dom.get("checkEmpCode").value = empCode;	 	
 	 	employeeName=empName;
 	 	empcode=empCode;
  	}
     var empCodeSelectionEnforceHandler = function(sType, arguments) {
       		warn('improperEmpCodeSelection');
  	} 

  function autocompleteEmpCode(obj,myEvent)
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
  	var Key = window.event ? window.event.keyCode : myEvent.keyCode;  	
  	//alert(Key);
  	//40 --> Down arrow, 38 --> Up arrow
  	if(Key!= 40 )
  	{
  	
  		if(Key != 38 )
  		{
  		
  		yuiflag[currRow.rowIndex];
  		//alert("selectedEmpCode"+selectedEmpCode);
  				var oAutoComp1 = new YAHOO.widget.AutoComplete(coaCodeObj,'codescontainer', selectedEmpCode);
  				//alert("oAutoComp1"+oAutoComp1);
  				oAutoComp1.queryDelay = 0;
  				oAutoComp1.prehighlightClassName = "yui-ac-prehighlight";
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
 	obj.value=temp[0];
 	employeeName=temp[1];	
//	designation=temp[3];
	//department=temp[4];
	//yearOfJoining=temp[5];
	//payScaleName=temp[6];
	empcode=temp[0];
	document.salaryPaySlipForm.checkEmpCode.value = temp[1];
	if(temp[1] == null && temp[0]!=null && temp[0]!="")
		getEmployeeByEnteringCode(temp[0],neibrObj);	
 	if(temp[2]==null || temp[2]=="") { neibrObj.value=""; return; }
 	if(temp[1]==null && (neibrObj.value!='' || neibrObj.value!=null) ) {  return ;}
 	else {

 			neibrObj.value=temp[2]; 			
 			//document.getElementById("monthAndYear").value="${asd}";
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
						employeeName=emp[1];	
						empcode=emp[0];
					}
	           }
	       }
        };
	   req.open("GET", url, true);
	   req.send(null);
  }

  function loadEmpCodes()
  {
 	 var type='getAllActiveEmployeeCodes';
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
	 if(fieldvalue!="")
	  {
 	var type = "checkUniqueness";
 	var link = "<%=request.getContextPath()%>"+"/commons/checkingPayslip.jsp?type=" + type+"&tablename=" + tablename+"&fieldname=" + fieldname+ "&fieldvalue=" + fieldvalue+"&fieldname1=" + fieldname1+ "&fieldvalue1=" + fieldvalue1+"&fieldname2=" + fieldname2+ "&fieldvalue2=" + fieldvalue2+ " ";
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
 			alert('<bean:message key="emppayslip.alert"/>')
			document.getElementById("employeeCode").value="";
			document.getElementById("employeeCode").focus();
 			isUnique="false";
 		}

 	    }
 	  }
 	};
 	request.open("GET", link, false);
 	request.send(null);

 	return isUnique;
	  }
}
//this API will check whether the employee appointment date should be greater then the payslip end date
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
	//alert("result "+result[0]);

		if(result[0]=="true")
		{

			isUnique="true";
		}
		else if(result[0]=="false")
		{
		//	alert("For selected employee code, month and year payslip is not found");
		//	document.getElementById("employeeCode").value="";
		//	document.getElementById("employeeCode").focus();
			isUnique="false";
		}

	    }
	  }
	};
	request.open("GET", link, false);
	request.send(null);	
 	return isUnique;
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

function onBodyLoad()
{
 	<%
		ArrayList financialYearList=(ArrayList)EgovMasterDataCaching.getInstance().get("egi-activeFinYr");
	%>
	var currentdate = "<%=currDate%>"; 
	finYr = "<%=currFinYr %>" ;	
 	if(currentdate.split("/")[1].charAt(0)=='0')
 	  month=currentdate.split("/")[1].charAt(1);
 	else
 	  month=currentdate.split("/")[1];
	loadEmpCodes();
	<%
		ArrayList payScaleHeaderList=(ArrayList)EgovMasterDataCaching.getInstance().get("pay-payScaleHeader");
		System.out.println(">>>>>>>>>>>>>>>>>>> pay  " + payScaleHeaderList.size());

	%>
	
	if('${mode}'!="central")
	{
	  document.getElementById("create").style.display = "none";
	  document.getElementById("generatePayslipId").style.display = "none";

	}
	else
	{
	document.getElementById("payview").style.display = "none";
    document.getElementById("viewPayslipId").style.display = "none";
	}
}

	function validation(arg)
 	{
		<c:if test="${(empty ess)  &&  (empty param.ess)}">
		if(!checkDeptMandatory(document.salaryPaySlipForm.department)){
			return false;
		}
		</c:if>
		
		if(document.salaryPaySlipForm.checkEmpCode.value=="" && document.salaryPaySlipForm.department.value=="0")
		{
			alert('<bean:message key="employee.alert"/>');
			document.salaryPaySlipForm.employeeCode.focus();
			return false;
    		}
  		
	  	if(document.salaryPaySlipForm.checkEmpCode.value!="" && document.salaryPaySlipForm.checkEmpCode.value != "undefined")
		{
		  	if(document.salaryPaySlipForm.department.value!="0")
			{
				alert('<bean:message key="employeedept.alert"/>');
				document.salaryPaySlipForm.employeeCode.value="";
				document.salaryPaySlipForm.department.value="0";
				document.salaryPaySlipForm.checkEmpCode.value = "";
				return false;
			}
		}
	 
	 	if(arg=="create" || arg=="view" || arg=="modify")
	 	{
		 
			if(document.salaryPaySlipForm.month.options[document.salaryPaySlipForm.month.selectedIndex].value =="0")
		 	{
				alert('<bean:message key="month.alert"/>');
			   	document.salaryPaySlipForm.month.focus();
			   	return false;
		 	}
		 
		 	if(document.salaryPaySlipForm.year.options[document.salaryPaySlipForm.year.selectedIndex].value =="0")
		 	{
			   alert('<bean:message key="year.alert"/>');
			   document.salaryPaySlipForm.year.focus();
			   return false;
		 	}
		 	else 
		 	{
			   	if(document.salaryPaySlipForm.year.options[document.salaryPaySlipForm.year.selectedIndex].value > eval(finYr))
			   	{
					alert('<bean:message key="payslipfuturedates.alert"/>');
				   	document.salaryPaySlipForm.year.focus();
				   	return false;	
			   	}
			   	else if (document.salaryPaySlipForm.year.options[document.salaryPaySlipForm.year.selectedIndex].value == eval(finYr)) 
			   	{
			      	var currmonth1,ipmonth2;
					if(eval(month)<4)
					{
					  	currmonth1=eval(month)+12;
					}
					else
					{
					  	currmonth1=eval(month);
					}

					if(eval(document.salaryPaySlipForm.month.options[document.salaryPaySlipForm.month.selectedIndex].value)<4)
					{
					  	ipmonth2=eval(document.salaryPaySlipForm.month.options[document.salaryPaySlipForm.month.selectedIndex].value)+eval(12);
					}
					else
					{
					  	ipmonth2=eval(document.salaryPaySlipForm.month.options[document.salaryPaySlipForm.month.selectedIndex].value);
					}
					//alert(ipmonth2);
					if(currmonth1 < eval(ipmonth2))
					{
					   	alert('<bean:message key="payslipfuturedates.alert"/>');
					   	document.salaryPaySlipForm.month.focus();
					   	return false;
					}
			   	}
	 	 	} 
		 
			if(arg!="create" && checkUnique('EGPAY_EMPPAYROLL','ID_EMPLOYEE',document.getElementById("employeeCodeId").value,'MONTH',document.salaryPaySlipForm.month.options[document.salaryPaySlipForm.month.selectedIndex].value,'FINANCIALYEARID',document.salaryPaySlipForm.year.options[document.salaryPaySlipForm.year.selectedIndex].value)=="false")
			{
				return false;
			} 
		 
		 	if(arg=="create")
		 	{
		 
			 	if(document.getElementById("employeeCodeId").value!="" && isValidPaySlipExists('EGPAY_EMPPAYROLL','ID_EMPLOYEE',document.getElementById("employeeCodeId").value)=="true")
			 	{
			    	alert('<bean:message key="emppayslipexists.alert"/>');
			 	    document.getElementById("employeeCode").value="";
				    return false;
			 	}
				/*  if(!uniqueCheckingBoolean('<%=request.getContextPath()%>/commonyui/egov/uniqueCheckAjax.jsp', 'EGPAY_EMPPAYROLL', 'ID_EMPLOYEE', 'employeeCodeId', 'no', 'no'))
			  	{
			    	alert('<bean:message key="emppayslipexists.alert"/>');
			    	document.getElementById("employeeCode").value="";
			    	return false;
		
			  	}  */
			 	// alert(checkPaySlipDate());
			  	
			  	if(checkPaySlipDate()=="false")
			  	{
			    	alert('<bean:message key="emppayslipexists.alert"/>');
			        document.getElementById("employeeCode").value="";
			        return false;
			  	}
			  	if(checkEmpAssignment()=="false"){
					alert('<bean:message key="empassperiod.alert"/>');
			        document.getElementById("employeeCode").value="";
			        return false;		
			  	}
			  	if(checkEmpPayscale()=="false"){
					alert('<bean:message key="emppayscaleperiod.alert"/>');
			        document.getElementById("employeeCode").value="";
			        return false;		
			  	}
	   		}
	  
	   		if(arg=="create")
		   	{
			   	document.salaryPaySlipForm.action ="${pageContext.request.contextPath}/payslip/BeforeManualGenPaySlips.do?mode=create&frwdType=newPayslip";
		   	}
		   	else if(arg=="view")
		   	{
			<c:choose>
			  <c:when test="${ess  == 1 or (not empty param.ess)}">
				document.salaryPaySlipForm.action="${pageContext.request.contextPath}/payslip/viewGenPaySlips.do?ess=1&mode=view";
			  </c:when>
			  <c:otherwise>
				document.salaryPaySlipForm.action="${pageContext.request.contextPath}/payslip/viewGenPaySlips.do?mode=view";
			  </c:otherwise>
			</c:choose>
		   	
		   	}
		   	else if(arg=="modify")
		   	{
				document.salaryPaySlipForm.action ="${pageContext.request.contextPath}/payslip/modifyPaySlips.do";
		   	}
			
		   	document.salaryPaySlipForm.submit();
	   
	  	}
 	}
 
 
 function isValidPaySlipExists(tablename,fieldname,fieldvalue)
 {
 	var type = "validPaySlipExists";
  	var link = "<%=request.getContextPath()%>"+"/commons/checkingPayslip.jsp?type=" + type+"&tablename=" + tablename+"&fieldname=" + fieldname+ "&fieldvalue=" + fieldvalue+ " ";
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

 function validationForReport(reportExtn)
 {
	<c:if test="${(empty ess)  &&  (empty param.ess)}">
	if(!checkDeptMandatory(document.salaryPaySlipForm.department)){
		return false;
	}
	</c:if>
	setAction();
	if(document.salaryPaySlipForm.checkEmpCode.value == "" && document.salaryPaySlipForm.department.value=="0")
	{
		alert('<bean:message key="employee.alert"/>');
		document.salaryPaySlipForm.employeeCode.focus();
		return false;
	}
  		
  	if(document.salaryPaySlipForm.checkEmpCode.value!="" && document.salaryPaySlipForm.checkEmpCode.value != "undefined")
	{
	  
		  if(document.salaryPaySlipForm.department.value!="0")
		  {
			alert('<bean:message key="employeedept.alert"/>');
			document.salaryPaySlipForm.employeeCode.value="";
			document.salaryPaySlipForm.department.value="0";
			document.salaryPaySlipForm.checkEmpCode.value = "";
			return false;
		  }
	 }
  	
  	if(document.salaryPaySlipForm.month.options[document.salaryPaySlipForm.month.selectedIndex].value =="0")
    {
	   alert('<bean:message key="month.alert"/>');
	   document.salaryPaySlipForm.month.focus();
	   return false;
    }
    if(document.salaryPaySlipForm.year.options[document.salaryPaySlipForm.year.selectedIndex].value =="0")
    {
	   alert('<bean:message key="year.alert"/>');
	   document.salaryPaySlipForm.year.focus();
	   return false;
    }
    if(document.salaryPaySlipForm.year.value !="0" && document.salaryPaySlipForm.month.value!="0")
    {
		month = document.salaryPaySlipForm.month.value;
		fin_Year = document.salaryPaySlipForm.year.value;
    }
    if(document.salaryPaySlipForm.department.value !="0")
    {
		dept= document.salaryPaySlipForm.department.value;
	}
	if(document.salaryPaySlipForm.forceSelection.checked)
	{
		flag = "yes"
	}
	if(!document.salaryPaySlipForm.forceSelection.checked)
	{
		flag = "no"
	}
	
	<c:choose>
	  <c:when test="${ess  == 1 or (not empty param.ess)}">
		document.salaryPaySlipForm.action="${pageContext.request.contextPath}/generatePDF.do?ess=1&fin_Year="+fin_Year+"&dept="+dept+"&month="+month+"&flag="+flag+"&reportExtn="+reportExtn;
	  </c:when>
	  <c:otherwise>
		document.salaryPaySlipForm.action="${pageContext.request.contextPath}/generatePDF.do?fin_Year="+fin_Year+"&dept="+dept+"&month="+month+"&flag="+flag+"&reportExtn="+reportExtn;
	  </c:otherwise>
	</c:choose>
	document.salaryPaySlipForm.submit();
 }

function showPayslipInfo(payslipId){   
	   	window.location = "${pageContext.request.contextPath}/payslip/viewPaySlip.do?payslipId="+payslipId 
   }
   
  

   	function paramsFunction()
   	{ return "type=AllEmployeeCodes";
   	}
   	
   	function setAction()
   	{
   	<c:choose>
	  <c:when test="${ess  == 1 or (not empty param.ess)}">
	  document.salaryPaySlipForm.action="/payslip/viewGenPaySlips.do?ess=1";
	  </c:when>
	  <c:otherwise>
	  document.salaryPaySlipForm.action="/payslip/viewGenPaySlips.do";
	  </c:otherwise>
	</c:choose>
   	}
</script>
</head>


<body onLoad="onBodyLoad();">



<html:form method="POST" action="/payslip/viewGenPaySlips" >
	
		<input type="hidden" name="checkEmpCode" id="checkEmpCode" value="${employeeCode !=null ? employeeCode : '' }"/>

	<table  width="95%" cellpadding="0" cellspacing="0" border="0" id="employee">
		<tr>

		<td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
                  <div class="headplacer"><bean:message key="viewpayslip.header"/> </div></td>

		<td id="generatePayslipId" colspan="6" height=30 bgcolor=#bbbbbb align=middle
				class="tablesubcaption">
			<p align="center"><b>Generate Payslip &nbsp;&nbsp;&nbsp;</b>
			</td>
		</tr>
		
  <html:hidden property="employeeCodeId" styleId="employeeCodeId"/>
<c:choose>
  <c:when test="${ess  == 1 or (not empty param.ess)}">
    <tr>
      	<td class="whiteboxwk" ><bean:message key="empcode"/>:</td>
      	<td  class="whitebox2wk" valign="top">${employeeCode} </td>
      	<td class="whiteboxwk"><bean:message key="empname"/>:</td>
      	<td class="whitebox2wk">${employeeName}</td>
      	<input type="hidden" name="department" id="department" value="0" />
      	<input type="hidden" name="employeeCode" id="employeeCode" value="${employeeCode}"/>
      	<input type="hidden" name="ess" id="ess" value="1" />
   </tr>
  </c:when>
  <c:otherwise>
  <tr>
  	<td class="whiteboxwk" width="15%"><bean:message key="empcode"/>:</td>
  	<td  class="whitebox2wk" valign="top" width="15%">
  	
 <!-- 	<input type="text"  class="selectwk" name="employeeCode" id ="employeeCode" autocomplete="off"   
  	onkeyPress="autocompleteEmpCode(this,event);" onblur="fillNeibrAfterSplit(this,'employeeCodeId');trim(this,this.value)"> -->
  	
  		<div class="yui-skin-sam">
	    	<div id="empSearch_autocomplete">
	    		<div>
	    	    <input type="text"  class="selectwk" name="employeeCode" id ="employeeCode" /> 	    
	    		</div>
	   	    <span id="empCodeSearchResults"></span>
	    	</div>
		</div>		    	
		   	    <egovtags:autocomplete name="employeeCode"  field="employeeCode" 
		   	    	url="${pageContext.request.contextPath}/common/employeeSearch!getEmpListByEmpCodeLike.action" queryQuestionMark="true" paramsFunction="paramsFunction" results="empCodeSearchResults" 
		   	    	handler="empCodeSelectionHandler" forceSelectionHandler="empCodeSelectionEnforceHandler"/>
		   	    <span class='warning' id="improperempCodeSelectionWarning"></span>
  	</td>

	 <td class="whiteboxwk"><egovtags:filterByDeptMandatory/><bean:message key="department"/>:</td>
     <td class="whitebox2wk" >
		<select  name="department" id="department" class="selectwk">
		<option value='0'>--Choose--</option>
		<egovtags:filterByDeptSelect/>
		</select>
	</td>

  </tr>
  </c:otherwise>
  </c:choose>

  <tr>
 

	<td class="greyboxwk"><span class="mandatory">*</span><bean:message key="monthyear"/>:</td> 
	    <td class="greybox2wk" ><html:select property="month" styleId="month">
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
	    	<html:select property="year" styleId="year" >
	    	<option value="0">Choose</option>
	    	<c:forEach var="financialYearObj" items="${financialYear}">
	    	<c:if test = "${financialYearObj.isActive=='1'}">
	    	<html:option value="${financialYearObj.id}">${financialYearObj.finYearRange}</html:option>
	    	</c:if>
	    	</c:forEach>
	     	</html:select>
    </td>
     <td class="greyboxwk" width="15%"><bean:message key="billNo"/></td>
		<td  class="greybox2wk" width="15%" valign="top">  	
 			<div class="yui-skin-sam">
	    		<div id="billNumberSearch_autocomplete" >
	    	    	<input type="text"  class="selectwk" name="billNumber" id ="billNumber" /> 	    
	   	   			<span id="codeSearchResults"></span> 
    			</div>
			</div>
		<egovtags:autocomplete name="billNumber"  field="billNumber" 
	   	    	url="${pageContext.request.contextPath}/billNumber/billNumberMaster!getBillNumberList.action" queryQuestionMark="true"  results="codeSearchResults" 
	   	    	handler="codeSelectionHandler" forceSelectionHandler="codeSelectionEnforceHandler"/>
	   		   <span class='warning' id="impropercodeSelectionWarning"></span>
		</td>
		<input type="hidden" name="billNumberId" id="billNumberId"/>   

  </tr>

  <tr>
  <td class="whiteboxwk">
   <td class="whitebox2wk">
  <Input type="checkbox" value="no" id="forceSelection" name="forceSelection"><b> <bean:message key="forcepdf/rtf"/> </b>
  </td>
  </td>
  </tr>

  <tr>
                <td colspan="4" class="shadowwk"></td>
              </tr>
              <tr>
                <td colspan="4"><div align="right" class="mandatory"><bean:message key="mandatory"/></div></td>
              </tr>


  <tr>
  <td><div id="codescontainer"></div></td>
  </tr>
  </table>
  <br>
  <div class="buttonwk" align="center">
	  	<input type="button" class="buttonfinal" name="create" id="create" value="Generate Payslip" onclick="return validation('create');"/>
	 
	  	<input type="button" class="buttonfinal" name="view" id="payview" value="View Payslip" onclick="return validation('view');"/>
	 <input type="button" class="buttonfinal" name="create" value="Generate PDF" onclick="return validationForReport('pdf');" />
	 <input type="button" class="buttonfinal" name="create" value="Generate RTF" onclick="return validationForReport('rtf');" />
	  
 </div>
</center>
</div>
</html:form>
</div>
&nbsp
<%session.removeAttribute("payHeader");%>

 <table  width="100%"  align="center" cellpadding="0" cellspacing="0" border="0" id="paytable">  
  <c:if test="${param.billNumber!='' && param.billNumber!=null }">
	<tr>
		<td align="left" bgcolor=#bbbbbb height=20>
				<bean:message key="billNo"/>  :    <c:out value="${param.billNumber}"/>
		</td>
	</tr>	
	<tr>
		<td class="whiteboxwk" colspan="7"/>
	</tr>	
  </c:if>	
   	<c:if test="${payslips != null}">
	<c:if test="${fn:length(payslips) == 0}">
		<tr>
			<td  >
				<font color="red"><bean:message key="nopayslip"/></font>
			</td>
		</tr>
	</c:if>
	</c:if>

	

 	<display:table   id="data" style="width:100%"
                    name="${payslips}"
                     pagesize="10"  class="its" requestURI="/payslip/viewGenPaySlips.do">
	    <display:setProperty name="basic.msg.empty_list" value="" />
            <display:column property="employee.employeeCode" title="Employee Code"    />
            <display:column property="employee.employeeName" title="Employee Name" />
            <display:column property="empAssignment.desigId.designationName" title="Designation"  />
            <display:column property="grossPay" title="Gross Pay" />
            <display:column property="netPay" title="Net Pay"   />
            <display:column property="payType.paytype" title="PayType"  />
	    	 <c:if test="${data.state.value!='END' }">
	    		<display:column property="state.value" title="Workflow Status"  />
	   		 </c:if>
	    	<c:if test="${data.state.value=='END' }">
	    		<display:column title="Workflow Status">Fully Approved</display:column>
	   		</c:if>
	    
	    <display:column property="userName" title="Owner"  />
	    <display:column  title="View"><a href="#" onclick="showPayslipInfo('${data.id}');">View </a> </display:column>
        </display:table>

	 </table>
	
</body>
</html>
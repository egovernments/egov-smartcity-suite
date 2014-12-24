<%@ include file="/includes/taglibs.jsp" %>
<%@ page language="java" import="java.util.*,
                java.sql.*,
				java.text.SimpleDateFormat,
				org.egov.pims.service.*,
				org.egov.infstr.commons.dao.*,
                org.egov.infstr.utils.*,org.egov.payroll.utils.PayrollConstants
                " %>
<html>

<head>
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">

	<title> Pay Slips Report</title>

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
ArrayList finYrList=(ArrayList)EgovMasterDataCaching.getInstance().get("egi-activeFinYr");
ArrayList functionaryList=(ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-Functionary");
String mode = (String)request.getParameter("mode");
java.util.Date date = new java.util.Date();
SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

String currDate="";
Connection conn=null;
String overlapErrMsg =PayrollConstants.PAYSLIP_PERIOD_OVERLAP_MSG;
%>
<c:set var="financialYear" value="<%=finYrList%>" scope="page" />
<c:set var="functionaryList" value="<%=functionaryList%>" scope="page" />
<script language="JavaScript"  type="text/JavaScript">
var empCodeArray;
		var selectedEmpCode;
		var yuiflag = new Array();
		var employeeName;	
function fillNeibrAfterSplit(obj,neibrObjName)
 {
 	var currRow=getRow(obj);
 	yuiflag[currRow.rowIndex] = undefined;
 	neibrObj=getControlInBranch(currRow,neibrObjName);
 	var temp = obj.value;temp = temp.split("`-`");
 	obj.value=temp[0];
 	employeeName=temp[1];	
	designation=temp[3];
	department=temp[4];
	yearOfJoining=temp[5];
	payScaleName=temp[6];
	empcode=temp[0];
 	if(temp[2]==null || temp[2]=="") { neibrObj.value=""; return; }
 	if(temp[1]==null && (neibrObj.value!='' || neibrObj.value!=null) ) {  return ;}
 	else {

 			neibrObj.value=temp[2];
 			//document.getElementById("monthAndYear").value="${asd}";
 		 }

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
 function onBodyLoad()
  {
  	<%
  	String msg=(String)request.getAttribute("alertMessage");
  	if( msg!= null )
  	{
  	   %>
  	   alert("<%=msg %>");
  	   <%
  	}
  	%>
 	 //loadEmpCodes();
 	 
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
function validateEmpData(obj)
{
   if(checkEmpPayscale(obj)=="true")
   {
      if(checkEmpAssignment(obj)=="true")
      {
    	 	 if(checkPositionBillnumber(obj)=="true")
    	     {
				if(!checkErrorMsg(obj))
     	   		 {
	     	   		 genpayslip(obj);
     	   		 }
				 else
	    	     {
	    	   	 	 alert("Payslip Period is overlapping, cannot generate Payslip"); 
	    	     }
    	     }
    	     else
    	     {
    	   	 	 alert("There is no bill number mapped to Position :"); 
    	     }
      }
      else
      {
         alert("Employee Doesn't Have Assignment for current Period ");
      }
   }else
   {
      alert("Employee Doesn't Have PayStructure for current Period ");
   }
}

function checkErrorMsg(obj){
	var currRow=getRow(obj);
	var remarks=getControlInBranch(document.getElementById("results").rows[currRow.rowIndex],"remarks").value;		
	if(remarks=="<%=overlapErrMsg%>")
	{
		return true;
	}
	else
	{
		return false;
	}	
}
function checkEmpAssignment(obj){
		var currRow=getRow(obj);
		var type = "checkEmpAssignment";
		var month = document.getElementById("month").value;
		var year = document.getElementById("finYr").value;
		var empid=getControlInBranch(document.getElementById("results").rows[currRow.rowIndex],"empids").value;
		var link = "<%=request.getContextPath()%>"+"/commons/checkingPayslip.jsp?type=" + type+"&month=" + month+"&year=" + year+ "&empid=" + empid+ " ";
		var request = initiateRequest();
		request.open("GET", link, false);
		request.send(null);	
		var isUnique;
				if (request.status == 200){
					var response=request.responseText;
					var result = response.split("/");
					if(result[0]=="true"){
						isUnique="true";
					}
					else if(result[0]=="false"){
						isUnique="false";
					}
				}
		return isUnique;		
	}

	function checkEmpPayscale(obj){
		var currRow=getRow(obj);
		var type = "checkEmpPayscale";		
		var month = document.getElementById("month").value;
		var year = document.getElementById("finYr").value;
		var empid=getControlInBranch(document.getElementById("results").rows[currRow.rowIndex],"empids").value;
		var link = "<%=request.getContextPath()%>"+"/commons/checkingPayslip.jsp?type=" + type+"&month=" + month+"&year=" + year+ "&empid=" + empid+ " ";
		var request = initiateRequest();
		var isUnique;
		request.open("GET", link, false);
		request.send(null);	
		if (request.status == 200){
			var response=request.responseText;
			var result = response.split("/");
			if(result[0]=="true"){
				isUnique="true";
			}
			else if(result[0]=="false"){
				isUnique="false";
			}
		}
		request.open("GET", link, false);
		return isUnique;
	}
	function checkPositionBillnumber(obj){
		var currRow=getRow(obj);
		var type = "checkPositionBillnumber";		
		var month = document.getElementById("month").value;
		var year = document.getElementById("finYr").value;
		var empid=getControlInBranch(document.getElementById("results").rows[currRow.rowIndex],"empids").value;
		var link = "<%=request.getContextPath()%>"+"/commons/checkingPayslip.jsp?type=" + type+"&month=" + month+"&year=" + year+ "&empid=" + empid+ " ";
		var request = initiateRequest();
		var billnumberExists;
		request.open("GET", link, false);
		request.send(null);	
		if (request.status == 200){
			var response=request.responseText;
			var result = response.split("/");
			if(result[0]=="0"){
				billnumberExists="false";
			}
			else if(result[0]!="0"){
				billnumberExists="true";
			}
		}
		request.open("GET", link, false);
		return billnumberExists;
	}
function genpayslip(obj)
{
var currRow=getRow(obj);
var empid=getControlInBranch(document.getElementById("results").rows[currRow.rowIndex],"empids").value;

var fromdate=getControlInBranch(document.getElementById("results").rows[currRow.rowIndex],"fromdates").value;
var todate=getControlInBranch(document.getElementById("results").rows[currRow.rowIndex],"todates").value;
var paytype=getControlInBranch(document.getElementById("results").rows[currRow.rowIndex],"paytypes").value;
var month=todate.split("/")[1];
if(getControlInBranch(document.getElementById("results").rows[currRow.rowIndex],"remarks").value!="Pay Slip already exists")
{
	//if(paytype=="<%=PayrollConstants.EMP_PAYSLIP_PAYTYPE_NORMAL%>")
	//{
	  document.forms[0].action ="${pageContext.request.contextPath}/payslip/BeforeManualGenPaySlips.do?mode=reGenerate&frwdType=resolve"+
	      "&empid="+empid+"&month="+month+
		"&fromdate="+fromdate+"&todate="+todate+"&payType="+paytype;
	/*}
	/else
	//{
	//document.forms[0].action ="${pageContext.request.contextPath}/payslip/generateSuppPaySlip.do?submitType=supplementary&mode=reGenerate&frwdType=resolve"+
	      "&empid="+empid+"&month="+month+
		"&fromdate="+fromdate+"&todate="+todate+"&payType="+paytype;

	}*/

	    document.forms[0].submit();
}
/*window.open("${pageContext.request.contextPath}/payslip/BeforeManualGenPaySlips.do?mode=create"+
   "&empid="+empid+"&month="+month+
    "&fromdate="+fromdate+"&todate="+todate+" ","","height=650,width=900,scrollbars=yes,left=0,top=0,status=yes");*/
}
/**
* this function validates the form fields
**/
function validation()
{
  	var month=document.getElementById("month").value;
  	var finYr=document.getElementById("finYr").value;
  	var checkEmpId=checkEmpCode(this);
  	  
  	if(month=="-1")
  	{
    	alert("Please Select the Month");
    	return false;
  	}
  	
  	if(finYr=="-1")
  	{
    	alert("Please Select the Year ");
    	return false;
  	}
  
  	if(!checkDeptMandatory(document.getElementById("deptid"))){
		return false;
	}
	if(checkEmpId==false)
	{
	  return false;
	}
  
  	return true;
}
function setCheckBoxValue(obj)
{	
	if(obj.checked)
	{
		obj.value='Y';
	}
	else
	{
		obj.value='N';
	}
	//alert(obj.value)
}

function checkEmpCode(obj)
{
    if(document.getElementById("employeeCode").value!="")
    {
		var empId = document.getElementById("empid").value;
   
	   	if(empId=='')
	   	{ 
	       	alert("Please select the proper employee code(AutoComplete)");
	       	document.getElementById("employeeCode").value='';
	       	document.getElementById("employeeCode").focus();
	    	return false;
	   	} 
	}
	   
	return true;
}

	var empCodeSelectionHandler = function(sType, arguments) { 
        var oData = arguments[2];
	 	//alert(oData);
	 	var empDetails = oData[0];
	 	var empCode = empDetails.split(EMPCODE_SEP)[0];
	 	var empName = empDetails.split(EMPCODE_SEP)[1];
	 	dom.get("empid").value = oData[1];
	 	
	 	dom.get("employeeCode").value = empCode;	 	
	 	/*var currRow=getRow(obj);	
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
		}*/
		
		
       	
    }
    
    var empCodeSelectionEnforceHandler = function(sType, arguments) {
      		warn('improperEmpCodeSelection');
  	}

   	function paramsFunction()
   	{ return "type=AllEmployeeCodes";
   	}
   	function populateBillNo(){
      var deptId=document.getElementById("deptid").value;
      //alert("deptId"+ deptId);
    	  populatebillNumberId({departmentId:deptId});
   	   	}
</script>

</head>
<body onLoad="onBodyLoad();">
<html:form action ="/reports/payslipreports"  >
<%	if(mode != null){	%>
	<html:hidden property="mode" value="<%=mode%>"/>
<%	}	%>
	
	<%
	
	%>
 	
  
		
		
		<table width="95%" border="0" cellspacing="0" cellpadding="0">
		<tr>
		<td></td>
		</tr>
		<tr>

			<table width="95%" border="0" cellpadding="0" cellspacing="0" id="paytable" >   	
			
			<tr>
			<td colspan="4" class="headingwk"><div class="arrowiconwk">
			<img src="${pageContext.request.contextPath}/common/image/arrow.gif" /></div>
			<div class="headplacer">Batch Payslip Failure Details</div></td>
			</tr>

			<tr>

			</tr>
			<tr>
			<td width="15%" class="whiteboxwk" >
			
			<span class="mandatory">*</span>Month:</td>
			
			<td width="26%" class="whitebox2wk">
			
			<html:select property="month" styleId="month" styleClass="selectwk">
			<html:option value="-1">Choose</html:option>
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
			</td>	


			<td width="14%" class="whiteboxwk">
			
			<span class="mandatory">*</span>Year:</td>
			<td width="45%" class="whitebox2wk">
			<html:select property="finYr"  styleId="finYr" styleClass="selectwk">
			<html:option value="-1">Choose</html:option>
			<c:forEach var="financialYearObj" items="${financialYear}">
			<c:if test = "${financialYearObj.isActive=='1'}">
			<html:option value="${financialYearObj.id}">${financialYearObj.finYearRange}</html:option>
			</c:if>
			</c:forEach>
			</html:select>
			</td>
			</tr>
			<tr>
			<td class="greyboxwk"><egovtags:filterByDeptMandatory/> Department List:</td>
			<td class="greybox2wk">
				<html:select property="deptid" styleId="deptid" styleClass="selectwk" onblur="populateBillNo(this);">
					<html:option value="-1">---choose---</html:option>
					<egovtags:filterByDeptSelect/>
				</html:select>
			</td>


			<td class="greyboxwk">Functionary List:</td>
			<td class="greybox2wk">
			<html:select property="functionaryId" styleId="functionaryId" styleClass="selectwk" >
			<html:option value="-1">---choose---</html:option>
			<c:forEach var="functionary" items="${functionaryList}">
			<html:option value="${functionary.id}" >${functionary.name}</html:option>
			</c:forEach>

			</html:select>
			</td>



			</tr>
			<tr>
			<td class="whiteboxwk"> Employee Code:</td>
			<html:hidden property="empid" styleId="empid"/>	     	   	
			<td class="whitebox2wk" valign="top">
			
			
			
			
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
			<td class="whiteboxwk">All Failure Payslips:</td>
			<td class="whitebox2wk"><html:checkbox property="errorPay" onclick="setCheckBoxValue(this);" />
			</td>
			
			</tr>
			<tr><td class="greyboxwk">Bill No:</td>
			<td  class="greybox2wx">
			<egovtags:ajaxdropdown id="billNumberId" fields="['Text','Value']" dropdownId="billNumberId" url="billNumber/billNumberMaster!getBillNumberListByDepartment.action"/>
		    <html:select property="billNumberId" styleId="billNumberId" styleClass="selectwk" >	
		    	<html:option value="">-------------choose---------------</html:option>
				<c:forEach var="billList" items="${billNoList}">
					<html:option value="${billList.id}" >${billList.billNumber}</html:option>
				</c:forEach>
			</html:select>
			
</tr>
		
			
			<tr>
			<td colspan="4" class="shadowwk"></td>
			</tr>
			
			<tr>
			<td colspan="4"><div align="right" class="mandatory">* Mandatory Fields</div></td>
			</tr>

			<tr>
			<td colspan="4"><div class="buttonholderwk">
			<html:submit value="Search Pay Slips" property="b1" styleClass="buttonfinal" onclick="return validation();"/>
			</div></td>
			</tr>
			
			<tr>
			<td><div id="codescontainer"></div></td>
			</tr>

			<tr>
			<td colspan="4">
			<c:if test="${payslipSearchForm.empids!=null}">

			<div class="tbl-container" id="tbl-container">
			<%int i=1;%>
			<display:table name="${payslipSearchForm.empids}" export="false" cellspacing="0" cellpadding="0" id="results" class="its"  style="width:810px" pagesize="10" requestURI="${pageContext.request.contextPath}/reports/payslipreports.do">  
			<c:set var="j" value="<%=new Integer(i-1)%>"/>

			<div STYLE="display:table-header-group">

			<display:column title="Sr No::"  style="border:0;" ><%=i++%> <html:hidden property="paytypes" value="${payslipSearchForm.paytypes[j]}"/></display:column>
			<display:column title="Employee Name::" style="border:0;" >${payslipSearchForm.empnames[j]}<html:hidden property="empids" value="${payslipSearchForm.empids[j]}"/></display:column>
			<display:column title="Employee Code::" style="border:0;" >${payslipSearchForm.empcodes[j]}<html:hidden property="empcodes" value="${payslipSearchForm.empcodes[j]}"/></display:column>
			<display:column title="Department Name::" style="border:0;" >${payslipSearchForm.deptnames[j]}<html:hidden property="deptids" value="${payslipSearchForm.deptids[j]}"/></display:column>
			<display:column title="From Date::" style="border:0;" >${payslipSearchForm.fromdates[j]}<html:hidden property="fromdates" value="${payslipSearchForm.fromdates[j]}"/></display:column>
			<display:column title="To Date::" style="border:0;" >${payslipSearchForm.todates[j]}<html:hidden property="todates" value="${payslipSearchForm.todates[j]}"/></display:column>
			<display:column title="Remarks::" style="border:0;" >${payslipSearchForm.remarks[j]}<html:hidden property="remarks" value="${payslipSearchForm.remarks[j]}"/></display:column>			
			<display:column title="Action::"style="border:0;" ><a href="#" onclick="validateEmpData(this)">Resolve</a></display:column>
			
			<display:setProperty name="basic.show.header" value="true" />

			</div>
		 
    </display:table>
    </div>
	</c:if>
	</td>
	</tr>
   
	</table>
	</tr>
	
	</table>
	
	</html:form>
	</body>
	</html>

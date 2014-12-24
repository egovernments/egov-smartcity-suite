<%@ include file="/includes/taglibs.jsp" %>
<%@ page language="java" import="java.util.*,
                java.sql.*,
				java.util.*,
				java.text.SimpleDateFormat,
				org.egov.pims.service.*,
				org.egov.infstr.commons.dao.*,
                org.egov.infstr.utils.*,org.egov.payroll.utils.PayrollConstants
                " %>
<html>

<head>
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">

	<title>eGov EIS Payroll</title>

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
System.out.println("mode-----------"+mode);

	Connection conn=null;
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
 	 loadEmpCodes();
 	 
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
         genpayslip(obj);
      }else
      {
    	  alert('<bean:message key="alert.emp.assignment"/>');
      }
   }else
   {
	   alert('<bean:message key="alert.emp.paystructure"/>');
   }
}
function checkEmpAssignment(obj){
	//	alert("dasdas");
		var currRow=getRow(obj);
		var type = "checkEmpAssignment";
		var month = document.getElementById("month").value;
		var year = document.getElementById("finYr").value;
		var empid=getControlInBranch(document.getElementById("results").rows[currRow.rowIndex],"empids").value;
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

	function checkEmpPayscale(obj){
//		alert("checkPayscale");
		var currRow=getRow(obj);
		var type = "checkEmpPayscale";		
		var month = document.getElementById("month").value;
		var year = document.getElementById("finYr").value;
		var empid=getControlInBranch(document.getElementById("results").rows[currRow.rowIndex],"empids").value;
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

  var fromDate=document.payslipSearchForm.fromdate.value;
  var toDate=document.payslipSearchForm.todate.value;
  if(fromDate =="")
  {
	  alert('<bean:message key="alert.from.date"/>');
    return false;
  }
  if(toDate == "")
  {
	  alert('<bean:message key="alert.to.date"/>');
    return false;
  }
  
  if(!checkDeptMandatory(document.payslipSearchForm.deptid)){
	 return false;
  }
  return true;
}
</script>   

</head>
<body onLoad="onBodyLoad();">
	<html:form action ="/reports/payslipExceptionReports" onsubmit="return validation();" >
		

							<%	if(mode != null){	%>
								<html:hidden property="mode" value="<%=mode%>"/>
							<%	}	%>
							<center>	
								 
							  	<table width="95%" cellpadding="0" cellspacing="0" border="0" id="paytable" >   	
								  	<tr>
									   <td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
								           <div class="headplacer">Exception payslip report  </div>
								       </td>
								    </tr>
								   	<tr>
								   		<td class="labelcellforbg" align="right" colspan="4">&nbsp</td>
								   	</tr>
								   	
								   	<tr>
									   	<td width="15%" class="whiteboxwk">
											<b>From Date&nbsp;(dd/mm/yyyy)&nbsp;<font color="red">*</font></b> 
										</td>
										<td width="26%" class="whitebox2wk">
											<input type="text" name="fromdate"  id="fromDate" class="datefieldinput" class="selectwk"  size="12" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="validateDateFormat(this);" value="${payslipSearchForm.fromdate}" />
									
											<a href="javascript:show_calendar('payslipSearchForm.fromDate');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="<c:url value='../common/image/calendar.png' />" border="0"></a>
									
										</td>
										<td width="15%" class="whiteboxwk" ><b>To Date&nbsp;(dd/mm/yyyy)&nbsp;<font color="red">*</font></b></b></td>
										<td width="26%" class="whitebox2wk">
											<input type="text" name="todate"  id="todate" class="datefieldinput" class="selectwk" size="12" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="validateDateFormat(this);" value="${payslipSearchForm.todate}" />
											<a href="javascript:show_calendar('payslipSearchForm.todate');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="<c:url value='../common/image/calendar.png' />" border="0"></a>
										</td>	
								   	</tr>
								   	<tr>
									   	<td class="greyboxwk" ><egovtags:filterByDeptMandatory/><b>Department List</b></td>
									   	<td class="greybox2wk">
											<html:select property="deptid" styleId="deptid" styleClass="selectwk" >
												<html:option value="">---choose---</html:option>
												<egovtags:filterByDeptSelect/>
											</html:select>
									   	</td>
									   								   	
									   	<td class="greyboxwk" ><b>Functionary List</b></td>
										<td class="greybox2wk" >
											<html:select property="functionaryId" styleId="functionaryId" styleClass="selectwk" >
												<html:option value="">---choose---</html:option>
												<c:forEach var="functionary" items="${functionaryList}">
													<html:option value="${functionary.id}" >${functionary.name}</html:option>
												</c:forEach>
											</html:select>
									   	</td>
									</tr>
								
									<tr>
										<td colspan="4"><div align="right" class="mandatory">* Mandatory Fields</div></td>
							        </tr>
									
									<tr>
									   	<td colspan="4" align="center">
									   		<html:submit  property="srchExceptionPayslips" styleClass="buttonfinal"><bean:message key="srchExceptionPayslips"/></html:submit>
									   	</td>
									</tr>
									   
								    <tr>
								        <td colspan="4" align="center">
								        <c:if test="${payslipSearchForm.empids!=null}">      
											
											<%int i=1;%>
											<display:table name="${payslipSearchForm.empids}" export="false" cellspacing="0" cellpadding="0" id="results" class="its"  pagesize="10" requestURI="${pageContext.request.contextPath}/reports/payslipExceptionReports.do">  
											<c:set var="j" value="<%=new Integer(i-1)%>"/>
												 
													  <display:column title="Serial No"  style="border:0;" ><%=i++%> 
																<html:hidden property="paytypes" value="${payslipSearchForm.paytypes[j]}"/>
													  </display:column>
													  <display:column title="Employee Name" style="border:0;" >${payslipSearchForm.empnames[j]}
																<html:hidden property="empids" value="${payslipSearchForm.empids[j]}"/>
													  </display:column>	
													  <display:column title="Employee Code" style="border:0;" >${payslipSearchForm.empcodes[j]}
																<html:hidden property="empcodes" value="${payslipSearchForm.empcodes[j]}"/>  
													  </display:column>
													  <display:column title="Department Name" style="border:0;" >${payslipSearchForm.deptnames[j]}
																<html:hidden property="deptids" value="${payslipSearchForm.deptids[j]}"/></display:column>
													  <display:column title="From Date" style="border:0;" >${payslipSearchForm.fromdates[j]}
																<html:hidden property="fromdates" value="${payslipSearchForm.fromdates[j]}"/></display:column>
													  <display:column title="To Date" style="border:0;" >${payslipSearchForm.todates[j]}
																<html:hidden property="todates" value="${payslipSearchForm.todates[j]}"/></display:column>
													  <display:column title="Remarks" style="border:0;" >${payslipSearchForm.remarks[j]}
																<html:hidden property="remarks" value="${payslipSearchForm.remarks[j]}"/></display:column>         
													  <display:setProperty name="basic.show.header" value="true" />	
													  
												
											</display:table >
								        </c:if>
								        </td>
								      </tr>
								 </table>
  								</html:form>
		</body>
</html>


<%@page import="org.egov.payroll.utils.PayrollExternalInterface"%><%@ include file="/includes/taglibs.jsp" %>


<%@ page language="java" import="java.util.*,
                java.sql.*,
                org.egov.commons.service.CommonsService,
                org.egov.payroll.utils.PayrollManagersUtill, 
                org.egov.infstr.utils.HibernateUtil,
				java.text.SimpleDateFormat,
				org.egov.infstr.commons.dao.*,
				org.egov.pims.service.*,
                org.egov.infstr.utils.EgovMasterDataCaching,org.egov.payroll.utils.PayrollExternalInterface,org.egov.payroll.utils.PayrollExternalImpl" %>
<html>

<head>


	<title> <bean:message key="payslip.title"/> </title>
	 
	 <LINK REL="stylesheet" HREF="<%=request.getContextPath()%>/ccMenu.css" TYPE="text/css">
	
	<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/commonyui/examples/autocomplete/css/examples.css">

<%

ArrayList functionaryList=(ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-Functionary");
ArrayList functionList = (ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-function");
String currDate="";
	Connection conn=null;
	java.util.Date date = new java.util.Date();
SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

String payslipWfType = GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getAppConfigValueByDate("Payroll","PayslipWorkflow",new java.util.Date()).getValue();
%>
<c:set var="functionaryList" value="<%=functionaryList%>" scope="page" />
<c:set var="functionList" value="<%=functionList%>" scope="page" />


<script language="JavaScript"  type="text/JavaScript">
function validation()
{
	
   <% 	
	try{
		//conn = HibernateUtil.getCurrentSession().connection();
		PayrollExternalInterface payrollExternalInterface=new PayrollExternalImpl();
		currDate=  payrollExternalInterface.getCurrentDate() ;
	}
	catch(Exception e){
		e.printStackTrace();	
	}

   %>
	
	if(document.getElementById("fromDate").value == "")
	{
	  alert('<bean:message key="fromdatealert"/>');
	 // obj.value = "Block Pay";
	  return false;
	}
	if(document.getElementById("toDate").value == "")
	{
		alert('<bean:message key="todatealert"/>'); 
	 // obj.value = "Block Pay";
	  return false;
	}
	if( isDiffDatesAMonth()==false)
	 {
	  return false;
	 }
	var currentdate = "<%=currDate%>"; 
	if(compareDate(document.getElementById("fromDate").value,currentdate)== -1)
	{
		alert('<bean:message key="datealert1"/> '+currentdate);		
		document.getElementById('fromDate').value="";
		document.getElementById('fromDate').focus();
		return false;
	}
	if(compareDate(document.getElementById("toDate").value,currentdate)== -1)
	{
		alert('<bean:message key="datealert2"/> '+currentdate);		
		document.getElementById('toDate').value="";
		document.getElementById('toDate').focus();
		return false;
	}	
	var todate=document.getElementById("toDate").value;
	 if(compareDate(document.getElementById("toDate").value,document.getElementById("fromDate").value)== 1)
	 {
		    alert('<bean:message key="comparedatealert"/>'); 
		    document.getElementById('toDate').value="";
		    document.getElementById('toDate').focus();
		    return false;
	 }	 	 
	 if(document.getElementById("deptId").value =="-1")
	 {
		  alert('<bean:message key="deptalert"/>'); 
		  document.getElementById('deptId').focus();
		  return false;
	 }	 
	 if(document.getElementById("billNumberIds").selectedIndex == "-1" || document.getElementById("billNumberIds").selectedIndex == "0")
	 {
		  alert('<bean:message key="billnumalert"/>'); 
		  document.getElementById('billNumberIds').focus();
		  return false;
	 }
	 <%		
	 if("Manual".equals(payslipWfType)){%>
	 	if(validateForMandatory() == "false"){
	 		return false;
	 	}
	 <%}%>	
	 
	 /*if(document.getElementById("functionaryId").value !="-1")
	 {
		  if(document.getElementById("deptId").value =="-1")
		  {
			  alert("Please choose the department");
			  document.getElementById('deptId').focus();
			  return false;
		  }
	  }*/
	var retResult="";
	var type = "checkingPayDates";
	var fromDate = document.getElementById("fromDate").value;
	var deptid = document.getElementById("deptId").value;
	var functionaryId = document.getElementById("functionaryId").value;
	var functionId = document.getElementById("functionId").value;
	var billNumberIds = document.getElementById("billNumberIds");	
	var billNumberStr="";		
	for (var i=0; i<billNumberIds.length; i++)
	{ 		       		
		if(document.getElementById("billNumberIds")[i].selected == true)
			billNumberStr=billNumberStr+document.getElementById("billNumberIds")[i].value+",";
	}	
	var month =(document.getElementById("toDate").value).split('/')[1];
	if(deptid==-1 || deptid=="-1")
	{
		deptid=null;
	}
	if(functionaryId==-1 || functionaryId=="-1")
	{
		functionaryId=null;
	}
	var url = "<%=request.getContextPath()%>"+"/commons/checkingPayslip.jsp?type=" + type+"&fromdate="+fromDate+"&deptid="+deptid +"&functionaryId="+functionaryId +"&month="+month+"&functionId="+functionId+"&billNumberStr="+billNumberStr;
	var request = initiateRequest();
			
	request.onreadystatechange = function(){		
		if (request.readyState == 4)
		{			
			if (request.status == 200)
			{
				var response=request.responseText;
				var result = response.split("/");				
				var retResult=result[0];
				if(retResult!="false")
				{
				  	if (retResult == "1") 
				  	{
					  alert('<bean:message key="payslipjobalert1"/>');
					  return false;
					}
					else if(retResult == "5")
					{
						alert('<bean:message key="payslipjobalert2"/>');
						return false;
					}
					
					/*if (retResult == "2") 
					{
					  alert("Cannot schedule this job as payslips have already been generated.");
					  return false;
					}	*/
				}
				document.getElementById('subButton').disabled=true;
				document.salaryPaySlipForm.action = "${pageContext.request.contextPath}/payslip/generateBatchPaySlips.do";
				document.salaryPaySlipForm.submit();
			}
		}
	};	
	request.open("GET", url, true);
	request.send(null);	

}

function referenceToFunction(){
	alert("sad");
}


function enable()
{

<%
  	String msg=(String)request.getSession().getAttribute("alertMessage");
  	if( msg!= null )
  	{
  	   %>
  	   alert("<%=msg %>");
  	   
  	   <%
  	   request.getSession().removeAttribute("alertMessage");
  	}
  	%>
	document.getElementById('deptId').value ="-1";
	document.getElementById('functionaryId').value ="-1";
	document.getElementById('msg').style.display="none";
	var succcount="<%=request.getAttribute("succcount")%>";

}
function showmsg()
{
	document.getElementById("msg").style.display="block";
	//alert(document.getElementById('msg').innerHTML);
	var todate=document.getElementById("toDate").value;
	var month=todate.split('/')[1];
	var msg1="Pay Slips will be generating for the month :"+month;	
	document.getElementById("message").innerHTML=msg1;

}
function validation_PayslipException()
{
var fromdate=document.getElementById("fromDate").value;
var todate=document.getElementById("toDate").value;
var deptId=document.getElementById("deptId").value;
var functionaryId=document.getElementById("functionaryId").value;
if(deptId=="-1")
{
deptId="";
}
if(functionaryId=="-1") 
{
functionaryId="";
}

	if(document.getElementById("fromDate").value == "")
	{
	  alert('<bean:message key="fromdatealert"/>');
	  return false;
	}
	if(document.getElementById("toDate").value == "")
	{
		alert('<bean:message key="todatealert"/>');
	  return false;
	}
	window.open('${pageContext.request.contextPath}/reports/payslipExceptionReports.do?fromdate='+fromdate+'&todate='+todate+'&deptid='+deptId+'&functionaryId='+functionaryId,'ExceptionPayslipReport','height=650,width=900');

}


function isDiffDatesAMonth()
{
	var minutes = 1000*60;
	var hours = minutes*60;
	var days = hours*24;

	var fromdateStr=document.getElementById("fromDate").value;
	var todateStr=document.getElementById("toDate").value;
	
	var frm_date = getDateFromFormat(fromdateStr, "d/M/y");
	var to_date = getDateFromFormat(todateStr, "d/M/y");

	var diff_date = Math.round((to_date - frm_date)/days);
	//alert("Diff date is11: " + diff_date );
		
	if(diff_date > 31){
	
		alert('<bean:message key="payslipgenprd"/>');		
		return false;
	}
	return true;
}

function populateBillNo(){
    var deptId=document.getElementById("deptId").value;
    //alert("deptId"+ deptId);
  	  populatebillNumberIds({departmentId:deptId});
 	   	}
</script>   

</head>
<body onload="enable();">
<html:form action ="/payslip/generateBatchPaySlips">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
		<td></td>
		</tr>
		<tr>
		
		<table width="100%"  cellpadding="0" cellspacing="0" border="0" id="paytable" >  
		<tr>
		<td colspan="4" class="headingwk"><div class="arrowiconwk">
		<img src="${pageContext.request.contextPath}/common/image/arrow.gif" /></div>
		<div class="headplacer"><bean:message key="payslipheader"/></div></td>
		</tr>		
		<tr>
		<td width="15%" class="whiteboxwk">
		<span class="mandatory">*</span><bean:message key="fromdate"/>:</td>
		<td width="26%" class="whitebox2wk">
		<input name="effectiveFrom"  id="fromDate"type="text"  class="selectwk" size="10"  maxlength="10"
		onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="validateDateFormat(this);" />
	    <a href="javascript:show_calendar('salaryPaySlipForm.effectiveFrom');"
		onmouseover="window.status='Date Picker';return true;" 
		onmouseout="window.status='';return true;"><img src="<c:url value='../common/image/calendar.png' />" border=0></a>
		</td>
		<td width="14%" class="whiteboxwk">
		<span class="mandatory">*</span><bean:message key="todate"/>:
		</td>
		<td width="45%" class="whitebox2wk">
		<input name="effectiveTo" id="toDate" type="text"  class="selectwk" size="10"  maxlength="10" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="validateDateFormat(this);"  />
		<a href="javascript:show_calendar('salaryPaySlipForm.effectiveTo');"
		onmouseover="window.status='Date Picker';return true;" 
		onmouseout="window.status='';return true;"><img src="<c:url value='../common/image/calendar.png' />" border=0></a>
		</td>
		</tr>

		<tr>
		<td class="greyboxwk"><span class="mandatory">*</span> <bean:message key="deptlist"/>:</td>
		<td class="greybox2wk">
		<html:select property="deptId" styleId="deptId" styleClass="selectwk" onblur="populateBillNo(this);">
			<html:option value="-1"><bean:message key="choose"/></html:option>
			<egovtags:filterByDeptSelect/>
		</html:select>
		</td>
		<td class="greyboxwk"> <bean:message key="funarylist"/>:</td>
		<td class="greybox2wk">
		<html:select property="functionaryId" styleId="functionaryId" styleClass="selectwk" >
		<html:option value="-1"><bean:message key="choose"/></html:option>
		<c:forEach var="functionary" items="${functionaryList}">
		<html:option value="${functionary.id}" >${functionary.name}</html:option>
		</c:forEach>

		</html:select>
		</td>
		</tr>
		<tr>
			<td class="whiteboxwk"><bean:message key="funlist"/>:</td>
			<td class="whitebox2wk">
			<html:select style="width:200px" property="functionId" styleId="functionId" styleClass="selectwk" >
				<html:option value="-1"><bean:message key="choose"/></html:option>
			  <c:forEach var="function" items="${functionList}">
				<html:option value="${function.id}" >${function.name}</html:option>
			  </c:forEach>	
			</html:select>
		</td>
		<td class="whiteboxwk"><span class="mandatory">*</span> Bill No:</td>
			<td  class="whitebox2wk">
			<egovtags:ajaxdropdown id="billNumberIds" fields="['Text','Value']" dropdownId="billNumberIds" url="billNumber/billNumberMaster!getBillNumberListByDepartment.action"/>
		    <html:select property="billNumberIds" styleId="billNumberIds" styleClass="selectwk"  multiple="true">	
		    	<html:option value="-1">-------------choose---------------</html:option>				
			</html:select>
			
	</tr>
	<tr><td class="greyboxwk"><bean:message key="comments"/>:
			<td class="greybox2wk"  align="left" colspan="4"> 
				<textarea name="remarks" cols="50" id="remarks" style="width:325px"></textarea>
			</td>			
		</tr>
		<tr id="msg">
		<td align="center" colspan="4">
		<div id="message" width="800">
		<bean:message key="payslipgeneratemsg"/> :
		</div>
		</td>
		</tr>
		<tr>
		<td colspan="4" class="shadowwk"></td>
		</tr>
		<tr>
		<td colspan="4"><div align="right" class="mandatory"><bean:message key="mandatory"/></div></td>
		</tr>
		<tr>		
		<td colspan="4">
		
		</td>
		</tr>
		</tr>
		</table>
		</tr>
		</table>
		
		<%		
		if("Manual".equals(payslipWfType)){%>
			<%@ include file='manualWfApproverSelection.jsp'%>	
		<%}%>
			 
		
		
		
		
		
		
		
		<div class="buttonholderwk">
			<input type="button" name="subButton" id="subButton" value="Generate Pay Slips" property="b1" onclick="return validation();" class ="buttonfinal"/>			
			<html:button  property="payslipExceptions" onclick="validation_PayslipException()" styleClass ="buttonfinal">
				<bean:message key="payslipExceptions"/>
			</html:button>			
			<html:button property="close"  styleId="button"  styleClass="buttonfinal"  onclick="window.close()"  ><bean:message key="close"/>
			</html:button>
		</div>		
		
		</html:form>
		</body>
		</html>

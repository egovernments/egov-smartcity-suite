<%@ include file="/includes/taglibs.jsp" %>
<%@ page import=
"java.util.*,
 org.egov.infstr.utils.*,
  org.egov.commons.CFinancialYear,
 org.egov.pims.empLeave.model.*,
 org.egov.pims.empLeave.service.*,
 org.egov.pims.model.*,
  org.egov.pims.utils.*,
 java.text.SimpleDateFormat"
%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <c:if test="${mode=='modify' && mode!='fromInbox'}">
    <title>Modify CompOff</title>
	</c:if>

<c:if test="${mode=='fromInbox'}">
  <title>Approve CompOff</title>
</c:if>

<c:if test="${mode=='cancel'}">
<title>Cancel CompOff</title>
</c:if>

<c:if test="${mode=='cancelBeforeApply'}">
<title>Apply CompOff</title>
</c:if>

<c:if test="${mode=='view'}">
<title>View CompOff</title>
</c:if>

    

    <SCRIPT type="text/javascript" src="<%=request.getContextPath()%>/javascript/Employeevalidation.js" type="text/javascript"></SCRIPT>
    <SCRIPT type="text/javascript" src="<%=request.getContextPath()%>/javascript/dateValidation.js" type="text/javascript"></SCRIPT>
	<SCRIPT type="text/javascript" src="<%=request.getContextPath()%>/commonjs/calender.js" type="text/javascript"></SCRIPT>
    <script language="text/JavaScript" src="<%=request.getContextPath()%>/Admin-Homepage_files/dhtml.js" type="text/JavaScript"></script>

</head>


<script>

var dbCompOffDate="";
function ButtonPressNew(arg)
{
	document.leaveForm.compOffDate.disabled=false;
	if(arg == "submit")
	{
		
			if( checkMandatoryfields())
			{
			document.leaveForm.action = "${pageContext.request.contextPath}/leave/BeforeCompOffAction.do?submitType=createCompOff";
			document.leaveForm.submit();
			}
		
		
	}
	else if(arg=="cancelholidayworkedon")
	{
		document.leaveForm.action = "${pageContext.request.contextPath}/leave/BeforeCompOffAction.do?submitType=cancelHolidayWorkedON";
		document.leaveForm.submit();  
	}
	else if(arg == "beforeModify")
	{
			document.leaveForm.action = "${pageContext.request.contextPath}/leave/BeforeCompOffAction.do?submitType=beforeCompOffApprove&mode=modify&idCompOff="+document.leaveForm.compOffId.value;
			document.leaveForm.submit();
	}
	else if(arg == "afterModify")
	{
	
		if(dbCompOffDate==document.leaveForm.compOffDate.value || checkMandatoryfields())
		{
			
				document.leaveForm.action = "${pageContext.request.contextPath}/leave/AfterCompOffAction.do?submitType=modifyCompOff";
				document.leaveForm.submit();
		}
	}
	else if(arg == "approve")
	{
		
			document.leaveForm.action = "${pageContext.request.contextPath}/leave/AfterCompOffAction.do?submitType=approveCompOff";
			document.leaveForm.submit();
	}
	else if(arg == "reject")
	{
			document.leaveForm.action = "${pageContext.request.contextPath}/leave/AfterCompOffAction.do?submitType=rejectCompOff";
			document.leaveForm.submit();
	}
	else if(arg == "cancel")
	{
			document.leaveForm.action = "${pageContext.request.contextPath}/leave/AfterCompOffAction.do?submitType=cancelCompOffAfterApproval";
			document.leaveForm.submit();
	}
	else if(arg == "close")
	{
		window.close();
	}

}



function checkForHoliday(compoffdateStr,empid)
{
var url = "${pageContext.request.contextPath}/leave/BeforeCompOffAction.do?submitType=checkCompOffDateAjax&compOffRequestDate="+compoffdateStr+"&empId="+empid+"&workedOnDate="+document.leaveForm.workedOnHolidayDate.value;
var req = initiateRequest();

req.open("GET", url, false);

req.send(null);
var leaveApplied="";
	if (req.status == 200)
	{  
			leaveApplied=req.responseText;
			
			//alert('leaveApplied'+leaveApplied);
			
			if(trimAll(leaveApplied)!="")
			{
			alert(leaveApplied);
			document.leaveForm.compOffDate.value="";
			//document.leaveForm.compOffDate.focus();
			return false;
			}
			else
			{	
			return true;
			}
	}
	else
	{
	alert("unable to process request");
	return false;
	}
}
function checkCutOffDate(strValue,empid)
{
	if(strValue != "")
	{
		//alert("Inside checkCutOffDate");
		var entereddate = strValue;

		var enteredyear = entereddate.substr(6,9);
		var enteredmonth = entereddate.substr(3,2);
		var entereddate = entereddate.substr(0,2);
		var entereddate1 = new Date(enteredyear,enteredmonth-1,entereddate);
		var workeddate = document.leaveForm.workedOnHolidayDate.value;

		var workedyear = workeddate.substr(6,9);
		var workedmonth = workeddate.substr(3,2);
		var workeddate = workeddate.substr(0,2);
		var workeddate1 =new Date(workedyear,workedmonth-1,workeddate);

		var date = eval(workeddate);
		var month = eval(workedmonth);//+eval(3);
		var year = eval(workedyear);

		/*if(eval(month) > 12)
		{
			month=eval(month)-eval(12);
			year=eval(workedyear)+1;
		}*/

		var dateNew = new Date(year,month-1,date);
		if(entereddate1 <= workeddate1)
		{
			alert('<bean:message key="alertCompOffDtGtWorkDt"/>');
			document.leaveForm.compOffDate.value="";
			return false;
		}
		
		else
		{
		return checkForHoliday(strValue,document.leaveForm.empId.value);
		}
	}
	return true;
}




function checkMandatoryfields()
{
	if(document.leaveForm.compOffDate.value=="")
	{
		alert("Please Enter Compoff Date");
		document.leaveForm.compOffDate.focus();
		return false;
	}
	 <c:if test="${compoffWf=='Manual' && mode=='cancelBeforeApply'}">	
	else if(validateForMandatory() == "false")
	{
	return false;
	}
	</c:if>	
	else {
	return checkCutOffDate(document.leaveForm.compOffDate.value,document.leaveForm.empId.value);
	}	
	return true;
}
function onloadAlert()
{
	dbCompOffDate=document.leaveForm.compOffDate.value;
	
	
	<c:if test="${alertMsg != null}">
	 
		alert("${alertMsg}");
		refreshInbox();
		window.close();
	</c:if>
	
	
	<c:if test="${ mode=='modify' || mode=='cancelBeforeApply' }">
	document.leaveForm.compOffDate.disabled=false;
	document.getElementById('imgCal').style.display="";
	</c:if>
	<c:if test="${mode == 'view' || mode=='fromInbox' || mode=='cancel' }">
	document.leaveForm.compOffDate.disabled=true;
	document.getElementById('imgCal').style.display="none";
	</c:if>
	
	
}
function refreshInbox() 
  { 
 
  if(opener.top.document.getElementById('inboxframe')!=null) 
  { 
   if(opener.top.document.getElementById('inboxframe').contentWindow.name!=null && opener.top.document.getElementById('inboxframe').contentWindow.name=="inboxframe") 
    {  
     opener.top.document.getElementById('inboxframe').contentWindow.egovInbox.refresh(); 
    } 
   } 
 } 
</script>


<div align="center">
<table align='center'>


<tr>
<td style="buffer:0pt; padding:0pt; margin:0pt">

</table>
</div>

<Center>



			<!-- Tab Navigation Begins -->
				<table align='center'>
				<tr>
				<td align="center">
				<!-- Tab Navigation Begins -->

<center>
</center>
<!-- Tab Navigation Ends -->
				</td>
				</tr>
				</table>
		<!-- Tab Navigation Ends -->




<!-- Header Section Begins -->

<!-- Header Section Ends -->

<table  id="table2" cellpadding ="0" cellspacing ="0" border = "0" width="100%">
<tr>
<td>
<!-- Tab Navigation Begins -->

<!-- Tab Navigation Ends -->

<!-- Body Begins -->


		<div class="formmainbox">
			<div class="insidecontent">
		  <div class="rbroundbox2">
			<div class="rbtop2"><div></div></div>
			  <div class="rbcontent2">
<!-- Body Begins -->

<div align="center">
<center>
<body onload="onloadAlert();">
<html:form  action="/leave/BeforeCompOffAction.do?submitType=createCompOff"  >

<table   cellpadding ="0" cellspacing ="0" border = "0" width="95%">
<tbody>
<tr>
 
  <td colspan="10" class="headingwk">
  <div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
  <c:if test="${mode=='modify' && mode!='fromInbox'}">
  <p id="title">Modify CompOff&nbsp;&nbsp;&nbsp;</p>
    <title>Modify CompOff</title>
	</c:if>

<c:if test="${mode=='fromInbox'}">
  <p id="title"><bean:message key="CompOffApproval"/>&nbsp;&nbsp;&nbsp;</p>
</c:if>

<c:if test="${mode=='cancel'}">
<p id="title">View/Cancel CompOff&nbsp;&nbsp;&nbsp;</p>
</c:if>

<c:if test="${mode=='cancelBeforeApply'}">
<p id="title">Apply CompOff&nbsp;&nbsp;&nbsp;</p>
</c:if>

<c:if test="${mode=='view'}">
<p id="title">View CompOff&nbsp;&nbsp;&nbsp;</p>
</c:if>


</td>
  </tr>

<tr>
	  		<td class="whiteboxwk" width="14%">
	  		<bean:message key="EmployeeName"/>: </td>

	  		<td  class="whitebox2wk" width="26%"><html:text property="employeeName" readonly="true" /></td>
	  		<td class="whiteboxwk" width="29%">
				 <bean:message key="EmployeeCode"/>:	 </td>

	  		<td  class="whitebox2wk" width="45%"><html:text property="empCode" readonly="true" /></td>
	  		
</tr>
</table>

 <table  cellpadding ="0" cellspacing ="0" border = "0" width="95%" id="TPTable" name="TPTable" >
    <tbody>

<tr>
		<td class="greyboxwk" width="14%">	<bean:message key="WorkedOn"/>:</td>
		
		<td class="greybox2wk" >	
		<html:hidden property="compOffId"/>	
		<html:hidden property="empId"/>
		<html:text property="workedOnHolidayDate" styleId="workedDate" readonly="true" styleClass="selectwk grey"></html:text>				
		
	  	</td>
	  	
    	  <td class="greyboxwk" width="20%">
				  		<span class="mandatory">*</span><bean:message key="CompOffDate"/>: </td>

	  		<td class="greybox2wk" width="26%"><html:text   styleClass="selectwk" styleId="compOffDate"  property="compOffDate"  onblur = "validateDateFormat(this);" size="10" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')" />
			<a href="javascript:show_calendar('leaveForm.compOffDate');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;">
			<img id="imgCal" src="<c:url value='../common/image/calendar.png' />" border=0 onclick="document.leaveForm.compOffDate.focus();"></a>
			</td>
    
	  <td class="greybox2wk"> </td>
</td>

</tr>
<tr>

            <td colspan="6" ><div align="right" class="mandatory">* Mandatory Fields</div></td>
          </tr>

 </tbody>

 

</table>
<table>

		  </table>
		  <c:if test="${compoffWf=='Manual' && mode=='cancelBeforeApply'}">
		  <%@ include file="ManualWfApproverSelection.jsp" %>
		  </c:if>

<table  cellpadding ="0" cellspacing ="0" border = "0" >
<c:if test="${mode=='fromInbox'}">
<script>
document.leaveForm.compOffDate.disabled=true;

</script>
<tr >
	<td align="center">
	<html:button styleClass="buttonfinal" value="Modify" property="b1" onclick="ButtonPressNew('beforeModify');" /></td>
	
	<td><html:button styleClass="buttonfinal" value="Approve" property="b2" onclick="ButtonPressNew('approve')" /></td>
	<td><html:button styleClass="buttonfinal" value="Reject" property="b3" onclick="ButtonPressNew('reject')" /></td>
</tr>
</c:if>
<c:if test="${mode=='modify' && mode!='fromInbox'}">
<script>
document.leaveForm.compOffDate.disabled=false;
</script>
<td align="center">
	<html:button styleId="submitButton" styleClass="buttonfinal" value="Submit" property="b4"  onclick="ButtonPressNew('afterModify');"/></td>
	<td><html:button styleClass="buttonfinal" value="Close" property="b5" onclick="ButtonPressNew('close')" /></td>
</c:if>

<c:if test="${mode=='cancel'}">
<script>
document.leaveForm.workedOnHolidayDate.disabled=true;
document.leaveForm.compOffDate.disabled=false;
document.getElementById('imgCal').style.display="none";
</script>
<tr >
	<td align="center">
	<html:button styleId="submitButton" styleClass="buttonfinal" value="CancelCompoff" property="b7"  onclick="ButtonPressNew('cancel');"/></td>
	<td><html:button styleClass="buttonfinal" value="Close" property="b8" onclick="ButtonPressNew('close')" /></td>
</tr>
</c:if>
<c:if test="${mode=='view'}">
<script>
document.leaveForm.workedOnHolidayDate.disabled=true;
document.leaveForm.compOffDate.disabled=true;
</script>
<tr >
	<td align="center">
	<html:button styleClass="buttonfinal" value="CLOSE" property="b7" onclick="ButtonPressNew('close')" /></td>
</tr>
</c:if>
<c:if test="${mode=='cancelBeforeApply'}">
<script>
document.leaveForm.workedOnHolidayDate.disabled=true;
document.leaveForm.compOffDate.disabled=false;
</script>
<tr >
	<td align="center">
	<html:button styleId="submitButton" styleClass="buttonfinal" value="SUBMIT" property="b6"  onclick="ButtonPressNew('submit');"/></td>
	<td><input type="button" name="button" id="button" id="holidayWorkedOn" value="CANCEL-HOLIDAYWORKEDON"  class="buttonfinal" onclick="ButtonPressNew('cancelholidayworkedon');"/></td>
	<td><html:button styleClass="buttonfinal" value="CLOSE" property="b7" onclick="ButtonPressNew('close')" /></td>
</tr>
</c:if>

</table>
 </html:form>
 </body>
</table>
</center>
</div>
</td>
</tr>
<!-- Body Section Ends -->
</table>

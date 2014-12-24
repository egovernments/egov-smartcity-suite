
<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://displaytag.sf.net" prefix="display" %>



<%@ page import="java.util.*,
		 
		 org.egov.pims.service.*,
		 org.egov.infstr.utils.*"


%>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Search Employee</title>

	<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/ccMenu.css">
    <SCRIPT type="text/javascript" src="<%=request.getContextPath()%>/javascript/Employeevalidation.js" type="text/javascript"></SCRIPT>
	<SCRIPT type="text/javascript" src="<%=request.getContextPath()%>/javascript/dateValidation.js"  type="text/javascript"></SCRIPT>

</head>

<%
ArrayList employeeList=(ArrayList)request.getAttribute("employeeList");
if(employeeList!=null)
System.out.println(employeeList.size());
String master="Employee";
%>

<script>

function checkDate()
{
	if(document.pIMSForm.dateInput.value == null ||document.pIMSForm.dateInput.value =="")
	{
		alert("please fill the date");
		document.pIMSForm.dateInput.focus();
		return false;
	}
	return true;
	 

}





</script>

<!-- Header Section Begins -->

<!-- Header Section Ends -->

<table cellpadding ="0" cellspacing ="0" border = "0" width="100%"  id="table2">
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
<html:form  action="/pims/AfterAssignmentAction.do?submitType=executeAssignment" onsubmit="return checkDate();" >
<table  cellpadding ="0" cellspacing ="0" border = "0" width="100%"  >
<tbody>
<tr>
  <td colspan="8" class="headingwk">
  <div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>

<div class="headplacer">Search Employee Assignment</div></td>
  </tr>



 <td  class="whiteboxwk"><span class="mandatory">*</span>Enter Date(dd/mm/yyyy)</td>
 <td class="whitebox2wk" >
<input type="text"id="dateInput" name="dateInput" onBlur = "validateDateFormat(this);" size="15" align = "left" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')"  >
<a href="javascript:show_calendar('pIMSForm.dateInput');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="<c:url value='../common/image/calendar.png' />" border=0></a>


</td>


  </tr>
</tbody>
</table>


<br>
<table id = "submit" cellpadding ="0" cellspacing ="0" border = "0" width="100%">
<tr align = "center">


  <td><input type="submit" property="b4" value="Search"  class="buttonfinal" onclick="return checkDate();" /></td>

<tr>
<tr><td>&nbsp;</td></tr>
</table>

<c:if test="${employeeList != null}">

<div class="tbl-container" id="tbl-container">
<display:table name="${employeeList}" id="PeronalInformation" cellspacing="0" style="width:100%;" requestURI="/pims/AfterAssignmentAction.do"
export="false" defaultsort="2" pagesize = "15" sort="list" class="its" >
<display:setProperty name="paging.banner.placement" value="bottom"/>
	<display:column title="Employee Code" style="width:70px;" >
	${PeronalInformation.employeeCode}
	</display:column>
	<display:column title="Employee Name" style="width:100px;" >
	${PeronalInformation.employeeName}
	</display:column>

<display:column title="Assign" style="width:50;" >

<a  href="${pageContext.request.contextPath}/pims/BeforePIMSMasterAction.do?submitType=setIdForDetailsModify&master=Employee&Id=${PeronalInformation.idPersonalInformation}">Modify</a>

	</display:column>
 </div>
</display:table>
</c:if>


</div>

</table>
</center>

</html:form>

</body>
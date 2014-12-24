
<%@ include file="/includes/taglibs.jsp" %>
<%@ page import="java.util.*,
		 org.egov.pims.model.*,
		 org.egov.pims.empLeave.model.*,
		 org.egov.pims.utils.*"
%> 

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Search Employee</title>
		<link href="../common/css/eispayroll.css" rel="stylesheet" type="text/css" />
		<link href="../common/css/commonegov.css" rel="stylesheet" type="text/css" />

       <SCRIPT type="text/javascript" src="${pageContext.request.contextPath}/javascript/dateValidation.js" ></SCRIPT>
    <SCRIPT type="text/javascript" src="<%=request.getContextPath()%>/javascript/commoneis.js" type="text/javascript"></SCRIPT>




<script>



function execute()
{
	var target="<%=(request.getAttribute("alertMsg"))%>";

	 if(target!=null && target != "null")
	 {
		alert("<%=request.getAttribute("alertMsg")%>");
		<%	request.setAttribute("alertMsg",null);	%>
	 }
	 

}

 function checkAlphaNumeric(obj){
  		if(obj.value!=""){
  		var num=obj.value;
  		var objRegExp  = /^[a-zA-Z0-9]+$/;
  		if(!objRegExp.test(num)){
  		alert('Please Enter the proper code');
  		obj.value="";
  		obj.focus();
  		}
  		}
		}
function submitButtonPress(arg)
{
		if(!checkDeptMandatory(document.leaveForm.department.options.selectedIndex)){
				return false;
		}
	if(arg=='view')
	{
		if(trimAll(document.leaveForm.workedonFromDate.value)=="")
		{
			alert("Please Enter FromDate");
			return false;
		}
	document.leaveForm.action="${pageContext.request.contextPath}/leave/BeforeCompOffAction.do?submitType=getAllPresentsOnHolidays&mode=view";
	document.leaveForm.submit();
	}
	if(arg=='cancel')
	{
	document.leaveForm.action="${pageContext.request.contextPath}/leave/BeforeCompOffAction.do?submitType=getAllPresentsOnHolidays&mode=cancel";
	document.leaveForm.submit();
	}
	else if(arg=='create')
	{
	document.leaveForm.action="${pageContext.request.contextPath}/leave/BeforeCompOffAction.do?submitType=getAllPresentsOnHolidays&mode=create";
	document.leaveForm.submit();
	}
}
</script>
</head>
<body onLoad = "execute();"  >
<html:form   action="leave/BeforeCompOffAction.do?submitType=getAllPresentsOnHolidays&mode=create"    ><!-- /leave/BeforeCompOffAction.do?submitType=getEmpInfoForCompOff -->


		<div class="formmainbox">
			<div class="insidecontent">
		  <div class="rbroundbox2">
			<div class="rbtop2"><div></div></div>
			  <div class="rbcontent2">

<!-- Header Section Begins -->

<!-- Header Section Ends -->

<table width="95%" cellpadding ="0" cellspacing ="0" border = "0" id="table2">
<tr>
<td>
<!-- Tab Navigation Begins -->

<!-- Tab Navigation Ends -->

<!-- Body Begins -->


<!-- Body Begins -->

<div >
<center>

<table width="100%" cellpadding ="0" cellspacing ="0" border = "0"  >
<tbody>
<tr><td>&nbsp;</td></tr>
<tr>
  <td colspan="8" class="headingwk"><div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
  <div class="headplacer">Search Criteria</div>
 
<p><div class="headplacer" ></div></p></td>
  </tr>
  <tr><td>&nbsp;</td></tr>


<tr>
    <td  class="whiteboxwk">Employee Code</td>
    <td   class="whitebox2wk" width="10%" >
<input type="text" id="code" name="code"onBlur="checkAlphaNumericForCode(this);" class="selectwk" >
</td>
<td  class="whiteboxwk">Functionary:</td>
      <td  class="whitebox2wk" >
      
  <select  id="functionary" name="functionary" class="selectwk">
  <option value="0">Choose</option>
  <c:forEach var="functionaryItem" items="${functionaryList}">
   <option value="${functionaryItem.id}">${functionaryItem.name}</option>
  </c:forEach>
  </select>
  
  </td>
  </tr>
 
<tr>
             <td class="greyboxwk"><egovtags:filterByDeptMandatory/>Department:</td>
             
             <td class="greybox2wk">
			<select name="department" class="selectwk">
			<option value="0">Choose</option>
			<egovtags:filterByDeptSelect/>
			</select>
			</td>
			
             <td class="greyboxwk">Designation:</td>
             
             <td class="greybox2wk">
				<select name="designation" class="selectwk"><option value="0">Choose</option>
				<c:forEach var="designationItem" items="${designationList}">
				<option value="${designationItem.designationId}">${designationItem.designationName}</option>
				</c:forEach>
				</select>
			</td>
 </tr>
 
 <c:if test="${mode != null && mode=='view'}">
 
 <tr id="workedOnFrom">
    <td  class="whiteboxwk"><span class="mandatory">*</span>FromDate</td>
    <td   class="whitebox2wk" width="10%" >
		<html:text styleId="workedonFromDate" property="workedonFromDate"  styleClass="selectwk" onblur = "validateDateFormat(this);" maxlength="10" size="10" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')"/>
			<a href="javascript:show_calendar('leaveForm.workedonFromDate');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;">
			<img id="imgCal" src="<c:url value='../common/image/calendar.png' />" border=0 onclick="document.leaveForm.workedonFromDate.focus();">
			</a>
	</td>
	<td  class="whiteboxwk">ToDate:</td>
      <td  class="whitebox2wk" >      
  		<html:text styleId="workedonToDate" property="workedonToDate" styleClass="selectwk" onblur = "validateDateFormat(this);" maxlength="10" size="10" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')"/>
  			<a href="javascript:show_calendar('workedonToDate');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;">
			<img id="imgCal" src="<c:url value='../common/image/calendar.png' />" border=0 onclick="document.leaveForm.workedonToDate.focus();">
			</a>
  	</td>
  </tr>
  
 </c:if>
	
<tr>
	<td colspan="11" class="note">
	<div class="note">You can choose either code, designation, department or the functionary as the search criteria .</div>
	</td>
</tr>

<tr>

  	 <td colspan="6" ><div align="right" class="mandatory">* Mandatory Fields</div></td>
 </tr>
	
	<tr>
		<td colspan="4">
		<div class="buttonholderwk">		
		<c:if test="${mode != null && mode=='cancel'}">
		<html:button styleClass="buttonfinal" value="SUBMIT" property="b2"  onclick="submitButtonPress('cancel');"/>
		</c:if>
		<c:if test="${mode != null && mode=='view'}">
		<html:button styleClass="buttonfinal" value="SUBMIT" property="b2"  onclick="submitButtonPress('view');"/>
		</c:if>
		<c:if test="${(mode != null && mode=='create') || mode == null }">
		<html:button styleClass="buttonfinal" value="SUBMIT" property="b1"  onclick="submitButtonPress('create');"/>
		</c:if>
		<input type="button" name="button" id="button" value="CLOSE"  class="buttonfinal" onclick="window.close();"/></div>
		</td>
	</tr>
</tbody>
</table>
<br>



 
<c:if test="${compOffList != null}">
  <table align="center" width="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
  <td class="headingwk" colspan="5">
  <div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
  <div align="left" style="margin-top:4px;">Date Worked (Holidays)</div>

  </td>
</tr>
  
            
            



<tr id="compoff">
<td>

<c:if test="${mode != null && mode=='create'}">
<display:table name="${compOffList}" id="eid" cellspacing="0" style="width: 100%;" requestURI="${pageContext.request.contextPath}/pims/AfterSearchAction.do"
  export="false" defaultsort="2" pagesize = "15" sort="list"  class="its" >

<display:setProperty name="compOffId" value="id"/>
<display:column style="tablesubheadwk:10%"   title="DateWorkedOn"  ><fmt:formatDate value="${eid.attObj.attDate}" pattern="dd/MM/yyyy" />
</display:column>
<display:column title="Action">

	<a  href="${pageContext.request.contextPath}/leave/BeforeCompOffAction.do?submitType=getEmpInfoForCompOff&mode=create&compOffId=${eid.id}" >	
	CreateCompOff
	</a>
</display:column>
</display:table>
</c:if>


<c:if test="${mode != null && mode=='cancel' }">
<c:choose>

<c:when test="${compOffList!=null && not empty compOffList}">
<display:table name="${compOffList}" id="eid" cellspacing="0" style="width: 100%;" requestURI="${pageContext.request.contextPath}/pims/AfterSearchAction.do"
  export="false" defaultsort="2" pagesize = "15" sort="list"  class="its" >

<display:setProperty name="compOffId" value="id"/>
<display:column style="tablesubheadwk:10%"   title="CompOffDate"  >
<c:if test="${eid.compOffDate==null}">
No compoffs available
</c:if>
<fmt:formatDate value="${eid.compOffDate}" pattern="dd/MM/yyyy" />

</display:column>
<display:column title="Action">
<c:if test="${eid.compOffDate!=null}">

	<a  href="${pageContext.request.contextPath}/leave/BeforeCompOffAction.do?submitType=getEmpInfoForCompOff&mode=cancel&compOffId=${eid.id}" >	
	Cancel CompOff
	</a>
	
</c:if>
</display:column>


</display:table>
</c:when>
<c:otherwise>
<center><font color="red">No CompOff Available</font></center>
</c:otherwise>
</c:choose>
</c:if>



<c:if test="${mode != null && mode=='view' }">
<display:table name="${compOffList}" id="eid" cellspacing="0" style="width: 100%;" requestURI="${pageContext.request.contextPath}/pims/AfterSearchAction.do"
  export="false" defaultsort="2" pagesize = "15" sort="list"  class="its" >

<display:setProperty name="compOffId" value="id"/>
<display:column style="tablesubheadwk:10%"   title="CompOffDate"  >
<c:if test="${eid.compOffDate==null}">
No compoffs available
</c:if>
<fmt:formatDate value="${eid.compOffDate}" pattern="dd/MM/yyyy" />

</display:column>
<display:column style="tablesubheadwk:10%"   title="Status"  >${eid.status.name}</display:column>
<display:column title="Action">
<c:if test="${eid.compOffDate!=null}">

	<a  href="${pageContext.request.contextPath}/leave/BeforeCompOffAction.do?submitType=getEmpInfoForCompOff&mode=view&compOffId=${eid.id}" >	
	ViewCompOff
	</a>
</c:if>

	
</display:column>
</display:table>
</c:if>


</td></tr>

</table>
</c:if> 

</table>

	


<div align="center">
  		<span></span>
</div>


</div>
			<div class="rbbot2"><div></div></div>
		</div>
</div></div>





</html:form>

</body>

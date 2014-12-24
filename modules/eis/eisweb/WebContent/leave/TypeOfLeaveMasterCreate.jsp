<%@ include file="/includes/taglibs.jsp" %>
<%@ page import="java.util.*,
		 org.egov.infstr.utils.*,
		 org.apache.log4j.Logger,
		 java.text.SimpleDateFormat,
		 org.egov.pims.empLeave.dao.*,
		 org.egov.pims.empLeave.model.*,
		 org.egov.pims.empLeave.service.*,
		 org.egov.pims.empLeave.client.*"

%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">

	<%if(((String)(session.getAttribute("viewMode"))).trim().equals("create")){%>
	<title>Create Leave Type </title>
	<%}else if(((String)(session.getAttribute("viewMode"))).trim().equals("delete")){%>
	<title>Modify Leave Type </title>
	<%}if(((String)(session.getAttribute("viewMode"))).trim().equals("view")){%>
	<title>View Leave Type </title>
	<%}%>

			<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/ccMenu.css">





	<SCRIPT type="text/javascript" src="<%=request.getContextPath() +"/commonjs/ajaxCommonFunctions.js"%>"></Script>


<SCRIPT language="Javascript">
var unsignedInt = /^\d*$/;
var unsignedDecimal=/^\d*\.?\d*$/;
function ButtonPress(arg)
{
	if(arg == "savenew")
	{
		var submitType="";
		<%
			String mode1=((String)(session.getAttribute("viewMode"))).trim();
			if(mode1.equalsIgnoreCase("modify"))
			{
            %>

			if(document.leaveTypeForm.name.value == "" )
			{
				alert('<bean:message key="alertFillModifyNme"/>');
				document.leaveTypeForm.name.focus();
				return false;
			}


			submitType="modifyDetails";
		 <%
		 }
		 	 else if(mode1.equalsIgnoreCase("create"))
		 {
		 %>
		 	if(document.leaveTypeForm.name.value == "" )
			{
				alert('<bean:message key="alertFillLeaveTypeNme"/>');
				document.leaveTypeForm.name.focus();
				return false;
			}


		 	submitType="saveDetails";
		 <%
		 }
		 	else
		 {
		 %>
		 	submitType="deleteDetails";
		 <%
		 }
		 %>
		 	document.leaveTypeForm.action = "${pageContext.request.contextPath}/leave/AfterLeaveTypeAction.do?submitType="+submitType;
			document.leaveTypeForm.submit();
	}
	if(arg == "close")
	{
		window.close();
	}

}
function goto(arg)
{

	if(arg == "save")
	{

		document.leaveTypeForm.action = "${pageContext.request.contextPath}/leave/BeforeLeaveTypeAction.do?submitType=beforeCreate";
		document.leaveTypeForm.submit();
	}
	else if(arg == "modify")
	{
		document.leaveTypeForm.action = "${pageContext.request.contextPath}/leave/BeforeLeaveTypeAction.do?submitType=beforeModify";
		document.leaveTypeForm.submit();
	}
	else if(arg == "delete")
	{
		document.leaveTypeForm.action = "${pageContext.request.contextPath}/leave/BeforeLeaveTypeAction.do?submitType=beforeDelete";
		document.leaveTypeForm.submit();
	}
	else if(arg == "view")
	{
		document.leaveTypeForm.action = "${pageContext.request.contextPath}/leave/BeforeLeaveTypeAction.do?submitType=beforeView";
		document.leaveTypeForm.submit();
	}
	else if(arg == "index")
	{
			document.leaveTypeForm.action = "${pageContext.request.contextPath}/staff/index.jsp";
			document.leaveTypeForm.submit();
	}

}

function onClickCancel()
{
	window.location="${pageContext.request.contextPath}/leave/TypeOfLeaveMasterCreate.jsp";
}
function checkMode()
{
	var target="<%=(request.getAttribute("alertMessage"))%>";
	if(target!="null")
	{
					alert("<%=request.getAttribute("alertMessage")%>");
					document.leaveTypeForm.name.value="";

	}
	var viewMode="<%=(session.getAttribute("viewMode"))%>";
	if(viewMode!="null")
	{
		<%
		if( ((String)(session.getAttribute("viewMode"))).trim().equalsIgnoreCase("view") ||  ((String)(session.getAttribute("viewMode"))).trim().equalsIgnoreCase("delete"))
		{
			String id = (String)session.getAttribute("Id");
			System.out.println("id"+id);
			EmpLeaveServiceImpl empLeaveServiceImpl=new EmpLeaveServiceImpl();


		TypeOfLeaveMaster typeOfLeaveMaster = empLeaveServiceImpl.getTypeOfLeaveMasterById(new Integer(id));


		%>
			<%
				if(typeOfLeaveMaster.getName()!=null&&!typeOfLeaveMaster.getName().equals(""))
			%>
				document.leaveTypeForm.name.value ="<%=typeOfLeaveMaster.getName()%>";
				document.leaveTypeForm.typeId.value ="<%=typeOfLeaveMaster.getId()%>";
			<%
			if(typeOfLeaveMaster.getPayElegible().equals(new Character('1')))
			{
			%>
			document.leaveTypeForm.payElegible[0].checked = true;

			<%
			}
			%>

			<%
			if(typeOfLeaveMaster.getAccumulate().equals(new Character('1')))
			{
			%>
			document.leaveTypeForm.accumulate[0].checked = true;

			<%
			}
			%>

			<%
			if(typeOfLeaveMaster.getIsHalfDay().equals(new Character('1')))
			{
			%>
			document.leaveTypeForm.isHalfDay[0].checked = true;

			<%
			}
			%>
			
			<%
			if(typeOfLeaveMaster.getIsEncashable().equals(new Character('1')))
			{
			%>
			document.leaveTypeForm.isEncashable[0].checked = true;

			<%
			}
			
		
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		}
		%>

	}

}

</SCRIPT>
</head>
<body onload="checkMode()">


		<div class="formmainbox">
			<div class="insidecontent">
		  <div class="rbroundbox2">
			<div class="rbtop2"><div></div></div>
			  <div class="rbcontent2">
<!-- Header Section Begins -->

<!-- Header Section Ends -->


<table width="100%" cellpadding ="0" cellspacing ="0" border = "0" >
<table id="table2" width="95%" cellpadding ="0" cellspacing ="0" border = "0">
<tr>
<td>


<br>
<table width="100%" cellpadding ="0" cellspacing ="0" border = "0">
<tr><td>


<html:form  action="/leave/AfterLeaveTypeAction" >
<input type = hidden name="typeId" id="typeId"  />
<table  width="100%" cellpadding ="0" cellspacing ="0" border = "0"   >

<tr>
  <td colspan="8" class="headingwk"><div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
  

	<%
		String mode = "";
		if(((String)(session.getAttribute("viewMode"))).trim().equals("create")){
			mode="Create Leave Type";%>
		
		<%}else if(((String)(session.getAttribute("viewMode"))).trim().equals("delete")){
			mode="Modify Leave Type"; %>
		
		<%}if(((String)(session.getAttribute("viewMode"))).trim().equals("view")){
			mode="View Leave Type";%>
		
		<%}%>
		<p><div class="headplacer"><%=mode%></div></p></td>
		</tr>
  

  
  </td>
  </tr>
    <tr><td class="whiteboxwk"  ><span class="mandatory">*</span><bean:message key="LeaveTypeNme"/></td>
       <td class="whitebox2wk"     >
	   <input   type="text" name = "name" id="name" onblur="uniqueChecking('<%=request.getContextPath() %>/commonyui/egov/uniqueCheckAjax.jsp',  'EGEIS_TYPE_OF_LEAVE_MSTR', 'TYPE_OF_LEAVE_VALUE', 'name', 'no', 'yes');"></td>
       <td  class="whiteboxwk" ><bean:message key="IsHalfDay"/></td>
          <td   class="whitebox2wk"> <bean:message key="Yes"/>
          <input type="radio" value="1" name="isHalfDay" id="isHalfDay"  > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:message key="No"/><input type="radio" value="0" checked name="isHalfDay" id="isHalfDay">
</td>
   </tr>
   <tr>
   <td  class="greyboxwk" ><bean:message key="PayElegible"/></td>
   <td   class="greybox2wk" > <bean:message key="Yes"/>
   <input type="radio" value="1" checked name="payElegible" id="payElegible"  > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:message key="No"/><input type="radio" value="0" name="payElegible" id="payElegible">
</td>
<td  class="greyboxwk" ><bean:message key="Accumulative"/></td>
   <td  class="greybox2wk" > <bean:message key="Yes"/>
   <input type="radio" value="1" name="accumulate" id="accumulate"  > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:message key="No"/><input type="radio" value="0" checked name="accumulate" id="accumulate">
</td>
   </tr>
  <tr><td  class=whiteboxwk >Encashable</td>
   <td   class="whitebox2wk" colspan="3"> <bean:message key="Yes" />
   <input type="radio" value="1"  name="isEncashable" id="isEncashable"  > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:message key="No"/><input type="radio" value="0" checked name="isEncashable" id="encashable">
</td>


</tr> 
   </table>
  


  
  </div>
  </td></tr>
  
  <tr>
            <td><div align="right" class="mandatory">* Mandatory Fields</div></td>
          </tr>
  </table>
  
</td>
</tr>
</table>
</div>
	  <div class="rbbot2"><div></div></div>
</div>
  </div>
  </table>
  </div>
<div class="buttonholderwk">&nbsp;
<table align="center">
<tr>

	 

    		<%
  		   	if(!((String)(session.getAttribute("viewMode"))).trim().equals("view"))
  		   	{
  		   	%>
  		  		<td><html:button styleClass="buttonfinal" value="Save" property="b4" onclick="ButtonPress('savenew')" />

  		  		<%
  		  	}
    	%>
    	
     	
     	
    
 	 <input type="button" name="button" id="button" value="CLOSE"  class="buttonfinal" onclick="window.close();"/>
 	</td>
 	 </tr>
 	 </table>
</div>
</html:form>
 </body>
</html>
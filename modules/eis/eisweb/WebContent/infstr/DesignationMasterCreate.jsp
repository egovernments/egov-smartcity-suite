<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-nested" prefix="nested"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ page import="java.util.*,
		 org.egov.infstr.utils.*,
		 org.egov.asset.utils.*,
		 org.apache.log4j.Logger,
		 org.egov.infstr.commons.*,
		 java.text.SimpleDateFormat,
		 
		 org.egov.infstr.commons.dao.*,
		 org.egov.infstr.commons.client.*"
		  	
		  				  		
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
	<title>Asset Category</title>
	
	<LINK rel="stylesheet" type="text/css" href="/css/egov.css">
	<LINK rel="stylesheet" type="text/css" href="../css/ccMenu.css">
	<SCRIPT type="text/javascript" src="../javascript/calender.js" ></SCRIPT>
	<SCRIPT type="text/javascript" src="../commonjs/ajaxCommonFunctions.js" type="text/javascript"></SCRIPT>
	<SCRIPT type="text/javascript" src="../javascript/Employeevalidation.js" type="text/javascript"></SCRIPT>
	<SCRIPT type="text/javascript" src="../script/jsCommonMethods.js" type="text/javascript"></SCRIPT>
	<SCRIPT type="text/javascript" src="../javascript/dateValidation.js" type="text/javascript"></SCRIPT>

	
<SCRIPT language="Javascript">
var unsignedInt = /^\d*$/;
var unsignedDecimal=/^\d*\.?\d*$/;

function goto(arg)
{

	if(arg == "save")
	{

		document.forms("desigForm").action = "/infstr/BeforeGenericMasterAction.do?submitType=beforeCreate";
		document.forms("desigForm").submit();
	}
	else if(arg == "modify")
	{
		document.forms("desigForm").action = "/infstr/BeforeDesignationMasterAction.do?submitType=beforeModify";
		document.forms("desigForm").submit();
	}
	else if(arg == "delete")
	{
		document.forms("desigForm").action = "/infstr/BeforeDesignationMasterAction.do?submitType=beforDelete";
		document.forms("desigForm").submit();
	}
	else if(arg == "view")
	{
		document.forms("desigForm").action = "/pims/BeforeGenericMasterAction.do?submitType=beforeView";
		document.forms("desigForm").submit();
	}
	else if(arg == "index")
	{
			document.forms("desigForm").action = "/staff/index.jsp";
			document.forms("desigForm").submit();
	}

}
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
			if(document.desigForm.designationName.value == "" )
			{
				alert("Pls Fill in the Designation Name");
				document.desigForm.designationName.focus();
				return false;
			}
			if(document.desigForm.designationDescription.value == "" )
			{
				alert("Pls Fill in the Designation Description");
				document.desigForm.designationDescription.focus();
				return false;
			}
			if(document.desigForm.sanctionedPosts.value == "" )
			{
				alert("Pls Fill in the sanctioned Posts ");
				document.desigForm.sanctionedPosts.focus();
				return false;
			}
			if(document.desigForm.outsourcedPosts.value == "" )
			{
				alert("Pls Fill in the outsourced Posts");
				document.desigForm.outsourcedPosts.focus();
				return false;
			}
			if(document.desigForm.basicFrom.value == "" )
			{
				alert("Pls Fill in the basic From");
				document.desigForm.basicFrom.focus();
				return false;
			}
			if(document.desigForm.basicTo.value == "" )
			{
				alert("Pls Fill in the basic To ");
				document.desigForm.basicTo.focus();
				return false;
			}
			if(document.desigForm.departmentId.value == "" )
			{
				alert("Pls Fill in the Department ");
				document.desigForm.departmentId.focus();
				return false;
			}
			
			submitType="modifyDetails";
		 <%
		 }
		 	 else if(mode1.equalsIgnoreCase("create"))
		 {
		 %>
		 	if(document.desigForm.designationName.value == "" )
						{
							alert("Pls Fill in the Designation Name");
							document.desigForm.designationName.focus();
							return false;
						}
						if(document.desigForm.designationDescription.value == "" )
						{
							alert("Pls Fill in the Designation Description");
							document.desigForm.designationDescription.focus();
							return false;
						}
						if(document.desigForm.sanctionedPosts.value == "" )
						{
							alert("Pls Fill in the sanctioned Posts ");
							document.desigForm.sanctionedPosts.focus();
							return false;
						}
						if(document.desigForm.outsourcedPosts.value == "" )
						{
							alert("Pls Fill in the outsourced Posts");
							document.desigForm.outsourcedPosts.focus();
							return false;
						}
						if(document.desigForm.basicFrom.value == "" )
						{
							alert("Pls Fill in the basic From");
							document.desigForm.basicFrom.focus();
							return false;
						}
						if(document.desigForm.basicTo.value == "" )
						{
							alert("Pls Fill in the basic To ");
							document.desigForm.basicTo.focus();
							return false;
						}
						if(document.desigForm.departmentId.value == "" )
						{
							alert("Pls Fill in the Department ");
							document.desigForm.departmentId.focus();
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
		 	document.forms("desigForm").action = "../infstr/AfterDesignationMasterAction.do?submitType="+submitType;		
			document.forms("desigForm").submit();
	}
	if(arg == "close")
	{
		window.close();
	}
		
}
function onClickCancel()
{
	window.location="../pims/DesignationMasterCreate.jsp";	
}
function checkMode()
{
	var target="<%=(request.getAttribute("alertMessage"))%>";
	if(target!="null")
	{
					alert("<%=request.getAttribute("alertMessage")%>");
					document.desigForm.designationName.value="";
					document.desigForm.designationDescription.value="";
					document.desigForm.sanctionedPosts.value="";
					document.desigForm.outsourcedPosts.value="";
					document.desigForm.basicFrom.value="";
					document.desigForm.basicTo.value="";
					document.desigForm.annIncrement.value="";
					document.desigForm.sanctionedPosts.value="";
					document.desigForm.reportsTo.selectedIndex=0;
					document.desigForm.departmentId.selectedIndex=0;
					
	}
	var viewMode="<%=(session.getAttribute("viewMode"))%>";
	if(viewMode!="null")
	{
		<%
		if( ((String)(session.getAttribute("viewMode"))).trim().equalsIgnoreCase("modify") ||  ((String)(session.getAttribute("viewMode"))).trim().equalsIgnoreCase("delete"))
		{
			String id = (String)session.getAttribute("Id");
			System.out.println("id"+id);
			DesignationMasterDAO designationMasterDAO = new DesignationMasterDAO();
			DesignationMaster designationMaster = designationMasterDAO.getDesignationMaster(new Integer(id).intValue());
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			DesignationMaster designationMasterRt = designationMaster.getReportsTo();
			Integer deptment = designationMaster.getDeptId();
		%>
			<%
				if(designationMaster.getDesignationName()!=null&&!designationMaster.getDesignationName().equals(""))
			%>
				document.desigForm.designationName.value ="<%=designationMaster.getDesignationName()%>";
			<%
				if(designationMaster.getDesignationDescription()!=null&&!designationMaster.getDesignationDescription().equals("")&&!designationMaster.getDesignationDescription().equals("null"))
				if(!designationMaster.getDesignationDescription().equals("null"))
				
			%>
				document.desigForm.designationDescription.value ="<%=designationMaster.getDesignationDescription()%>";
				
			<%
				if(designationMaster.getSanctionedPosts()!=null&&!designationMaster.getSanctionedPosts().equals(""))
			%>
				document.desigForm.sanctionedPosts.value ="<%=designationMaster.getSanctionedPosts()%>";
			<%
				if(designationMaster.getOutsourcedPosts()!=null&&!designationMaster.getOutsourcedPosts().equals(""))
				
			%>
				document.desigForm.outsourcedPosts.value ="<%=designationMaster.getOutsourcedPosts()%>";
			
				
			<%
				if(designationMaster.getBasicFrom()!=null&&!designationMaster.getBasicFrom().equals(""))
			%>
				document.desigForm.basicFrom.value ="<%=designationMaster.getBasicFrom()%>";
			<%
				if(designationMaster.getBasicTo()!=null&&!designationMaster.getBasicTo().equals(""))
			%>
				document.desigForm.basicTo.value ="<%=designationMaster.getBasicTo()%>";
		
			var repTo="<%= (designationMasterRt != null) ? designationMasterRt.getDesignationId().intValue() : 0 %>";
			for(i=0;i<document.desigForm.reportsTo.options.length;i++)
			{
				if(repTo == document.desigForm.reportsTo.options[i].value)
				{
					document.desigForm.reportsTo.selectedIndex=i;
				}
			}
			
			var dept="<%= (deptment != null) ? deptment.intValue() : 0 %>";
						for(i=0;i<document.desigForm.departmentId.options.length;i++)
						{
							if(dept == document.desigForm.departmentId.options[i].value)
							{
								document.desigForm.departmentId.selectedIndex=i;
							}
			}
			
			document.desigForm.designationId.value ="<%=designationMaster.getDesignationId()%>";
		<%
		}
		%>
		
	}
	
}

</SCRIPT>
</head><!-- Header Section Begins -->
<%@ include file="/staff/egovHeader.jsp" %>
<!-- Header Section Ends -->

<body onload="checkMode()" >
<table align=center>
<table align='center' id="table2">
<tr>
<td>

<div id="main"><div id="m2"><div id="m3">

<br>
<table align=center>
<tr><td>
<div id="main"><div id="m2"><div id="m3">
<html:form  action="/infstr/AfterDesignationMasterAction" >
<input type=hidden name="designationId" id="designationId" />
<table align='center' class="tableStyle"cellpadding ="0" cellspacing ="0" border = "1"  name ="pisTable" id ="pisTable" width="785" style="border: 1px solid #D7E5F2"> 
   <tr>
           <td colspan="8"  class="tableheader" align="center">Designation<span></td>
 </tr>
    <tr><td class="labelcellforsingletd"  width="30%" align="right">Designation Name<SPAN class="leadon">*</SPAN>&nbsp;</td> 
    
       
       <%
       if( ((String)(session.getAttribute("viewMode"))).trim().equalsIgnoreCase("create") )
       {
       %>
       
       <td class="fieldcell"   width="70%"  align="center"><html:text property="designationName" onblur="uniqueChecking('/commonyui/egov/uniqueCheckAjax.jsp',  'EG_DESIGNATION', 'DESIGNATION_NAME', 'designationName', 'no', 'no')" /></td> 
       <%
       }
       else
       {
       %>
       <td class="fieldcell"   width="70%"  align="center"><html:text property="designationName"  /></td> 
       
       <%
       }
       %>
       <td class="labelcellforsingletd" align="right">Designation Description<SPAN class="leadon">*</SPAN>&nbsp;</td> 
         <td class="fieldcell" align="center"><html:text property="designationDescription"/></td> 
   </tr>
   
   
   
   <tr><td class="labelcellforsingletd" align="right">Sanctioned Posts<SPAN class="leadon">*</SPAN></td> 
            <td class="fieldcell" align="center"><html:text property="sanctionedPosts" value = ""/></td> 
            <td class="labelcellforsingletd" align="right">Outsourced Posts<SPAN class="leadon">*</SPAN>&nbsp;</td> 
               <td class="fieldcell" align="center"><html:text property="outsourcedPosts"/></td> 
   </tr>
   
   <tr><td class="labelcellforsingletd" align="right">basic From<SPAN class="leadon">*</SPAN></td> 
                  <td class="fieldcell" align="center"><html:text property="basicFrom"/></td> 
                  <td class="labelcellforsingletd" align="right">basic To<SPAN class="leadon">*</SPAN>&nbsp;</td> 
                  <td class="fieldcell" align="center"><html:text property="basicTo"/></td>
   </tr>
   
   <tr>
        	<td class="labelcellforsingletd" align="right">Reports To <SPAN class="leadon">*</SPAN>&nbsp;&nbsp;</td> 
   	<td class="smallfieldcell">
   	<html:select  property="reportsTo" >
   		<html:option value='0' >----choose----</html:option>
   		<%
   			DesignationMasterDAO designationMasterDAO = new DesignationMasterDAO();
			Map desMap = designationMasterDAO.getAllDesignationMaster();
   			for (Iterator it = desMap.entrySet().iterator(); it.hasNext(); ) 
   			{
   				Map.Entry entry = (Map.Entry) it.next();
   		%>
   				<html:option value='<%= entry.getKey().toString() %>'><%= entry.getValue() %></html:option>
   
   		<%
   			}
   			
   		%>
   	</html:select>
   	</td> 
   	<td class="labelcellforsingletd" align="right">Department &nbsp;&nbsp;<SPAN class="leadon">*</SPAN></td> 
   	<td class="smallfieldcell">
   	   	<html:select  property="departmentId" >
	   		<html:option   value='0'>----choose----</html:option>
	   		<%
	   			Map deptmap =(Map)session.getAttribute("deptmap");
	   			for (Iterator it = deptmap.entrySet().iterator(); it.hasNext(); ) 
	   			{
	   				Map.Entry entry = (Map.Entry) it.next();
	   		%>
	   				<html:option value='<%= entry.getKey().toString() %>'><%= entry.getValue() %></html:option>
	   
	   		<%
	   			}
	   			
	   		%>
	   	</html:select>
   	</td> 
   	
   	
        </tr>

   </table> 
  
  
 <table align=center name="buttonTable" id="buttonTable">
    	<tr>
   		
   		
 		  		<td><html:button styleClass="button" value="Save" property="b4" onclick="ButtonPress('savenew')" /></td>
 		  		
 		<td><html:button styleClass="button" value="Modify" property="b4" onclick="goto('modify')" /></td>
   		
   		
   		<td><html:button styleClass="button" value="Index" property="b4" onclick="goto('index')" /></td>
    	<tr>
   </table>
  </html:form>
  </div></div></div>
  </td></tr>
  </table>
 </body>
</html>
<!-- #-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation 
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or 
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this 
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It 
# 	   is required that all modified versions of this material be marked in 
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program 
# 	   with regards to rights under trademark law for use of the trade names 
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#------------------------------------------------------------------------------- -->
			<%@ include file="/includes/taglibs.jsp" %>
			<%@ page import="java.util.*,
			 org.egov.infstr.utils.*,
			 org.apache.log4j.Logger,
			 org.egov.pims.*,
			 org.egov.pims.dao.*,
			 org.egov.pims.model.*,
			 java.text.SimpleDateFormat,
			 org.egov.pims.client.*"


			%>

			<%!
			public Map getColumnMap()
			{
			Map colMap = new HashMap();

	        colMap.put("HowAcquiredMaster", "HOW_ACQUIRED_VALUE");
	        colMap.put("TestNameMaster", "NAME_OF_TEST_VALUE");
	        colMap.put("ReligionMaster", "EGEIS_RELIGION_MSTR");
	        colMap.put("PayFixedInMaster", "PAY_FIXED_IN_VALUE");
	        colMap.put("LanguagesQulifiedMaster", "QULIFIED_NAME");
	        colMap.put("LanguagesKnownMaster", "LANGUAGES_KNOWN_VALUE");
	        colMap.put("CommunityMaster", "COMMUNITY_NAME");
	        colMap.put("CategoryMaster", "CATEGORY_NAME");
	        colMap.put("BloodGroupMaster", "VALUE");
	        colMap.put("GradeMaster", "GRADE_VALUE");
	        colMap.put("EmployeeStatusMaster", "EGEIS_STATUS_MASTER");
	        colMap.put("TypeOfRecruimentMaster", "RECRUITMENT_TYPE_NAME");
			colMap.put("RecruimentMaster", "MODE_OF_RECRUIMENT_NAME");
				return colMap;
			}

			%>
			<html>
			<head>
			<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
			<title>Employee Master Screen</title>


			<LINK rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/ccMenu.css">
			<SCRIPT type="text/javascript" src="${pageContext.request.contextPath}/javascript/dateValidation.js" type="text/javascript"></SCRIPT>
			<SCRIPT language="Javascript">
			var unsignedInt = /^\d*$/;
			var unsignedDecimal=/^\d*\.?\d*$/;

			<%
			String className ="";
			if(request.getParameter("className")!=null)
			className = request.getParameter("className").trim();
			else
			className = ((String)session.getAttribute("className")).trim();
			%>

			function ButtonPress(arg)
			{
			if(arg == "savenew")
			{
			var submitType="";
			if(document.genericForm.name.value == "" )
			{
				alert('<bean:message key="alertFillNme"/>');
				document.genericForm.name.focus();
				return false;
			}
			<%
			if(!(className.equals("EmployeeStatusMaster")|| className.equals("ReligionMaster")||className.equals("LanguagesKnownMaster")||className.equals("BloodGroupMaster")))
			{
			%>
				if(document.genericForm.fromDate.value == "" )
			{
				alert('<bean:message key="alertEnterFromDate"/>');
				document.genericForm.fromDate.focus();
				return false;
			}
			if(document.genericForm.toDate.value == "" )
			{
				alert('<bean:message key="alertFillToDate"/>');
				document.genericForm.toDate.focus();
				return false;
			}
			<%
			}
			%>

			<%
				String mode1=((String)(session.getAttribute("viewMode"))).trim();
				if(mode1.equalsIgnoreCase("modify"))
			{
			%>


				submitType="modifyDetails";
			 <%
			 }
				 else if(mode1.equalsIgnoreCase("create"))
			 {
			 %>


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
			document.genericForm.action = "${pageContext.request.contextPath}/pims/AfterGenericMasterAction.do?submitType="+submitType+"&className="+'<%=className%>';
			document.genericForm.submit();
			}
			if(arg == "close")
			{
			window.close();
			}

			}
			function onClickCancel()
			{
			window.location="${pageContext.request.contextPath}/pims/genericMasterCreate.jsp";
			}

			//Function to validate Start date > end date
			function CompareDate(obj)
			{
				if(document.genericForm.toDate.value !="")
				{
					if(compareDate(document.genericForm.toDate.value,document.genericForm.fromDate.value) == 1||compareDate(document.genericForm.toDate.value,document.genericForm.fromDate.value)==0)
						{

								alert('End Date should be greater than Start Date');
								document.genericForm.fromDate.value="";
								document.genericForm.toDate.value="";
								document.genericForm.fromDate.focus();
								return false;
						}
				}
			}

			function goindex(arg)
			{

			if(arg == "Index")
			{

			document.genericForm.action = "${pageContext.request.contextPath}/staff/index.jsp";
			document.genericForm.submit();
			}


			}
			function goto(arg)
			{

			if(arg == "save")
			{

			document.genericForm.action = "${pageContext.request.contextPath}/pims/BeforeGenericMasterAction.do?submitType=beforeCreate&className=<%=className%>";
			document.genericForm.submit();
			}
			else if(arg == "modify")
			{

			document.genericForm.action = "${pageContext.request.contextPath}/pims/BeforeGenericMasterAction.do?submitType=beforeModify&className=<%=className%>";
			document.genericForm.submit();
			}
			else if(arg == "delete")
			{
			document.genericForm.action = "${pageContext.request.contextPath}/pims/BeforeGenericMasterAction.do?submitType=beforeDelete&className=<%=className%>";
			document.genericForm.submit();
			}
			else if(arg == "view")
			{
			document.genericForm.action = "${pageContext.request.contextPath}/pims/BeforeGenericMasterAction.do?submitType=beforeView&className=<%=className%>";
			document.genericForm.submit();
			}
			else if(arg == "index")
			{
				document.genericForm.action = "${pageContext.request.contextPath}/staff/index.jsp";
			document.genericForm.submit();
				}

			}

			function checkMode()
			{
			var target="<%=(request.getAttribute("alertMessage"))%>";
			if(target!="null")
			{
			alert("<%=request.getAttribute("alertMessage")%>");
			document.genericForm.name.value="";
			document.genericForm.fromDate.value="";
			document.genericForm.toDate.value="";

			}

			var viewMode="<%=(session.getAttribute("viewMode"))%>";
			if(viewMode!="null")
			{
			<%
			System.out.println("1");
			if(session.getAttribute("Id")!=null)
			{
			if( ((String)(session.getAttribute("viewMode"))).trim().equalsIgnoreCase("modify") ||  ((String)(session.getAttribute("viewMode"))).trim().equalsIgnoreCase("delete") ||  ((String)(session.getAttribute("viewMode"))).trim().equalsIgnoreCase("view"))
			{
			System.out.println("1");
			String sessionid = (String)session.getAttribute("Id");
			System.out.println("2");

			System.out.println("3");
			GenericMasterDAO genericMasterDAO = new GenericMasterDAO();
			System.out.println("4");
			GenericMaster gm = (GenericMaster)genericMasterDAO.getGenericMaster(new Integer(sessionid).intValue(),className);
			System.out.println("5");
		   	%>
			document.genericForm.name.value ="<%=gm.getName()%>";
			<%
			System.out.println("6"+gm.getFromDate());
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			if(gm.getFromDate()!=null&&!gm.getFromDate().equals(""))
			if(!gm.getFromDate().equals("null"))
			{
			System.out.println("7");

			{
			%>
				document.genericForm.fromDate.value ="<%=sdf.format(gm.getFromDate())%>";
			<%
			}
			}
			if(gm.getToDate()!=null&&!gm.getToDate().equals("")&&!gm.getToDate().equals("null"))
			if(!gm.getFromDate().equals("null"))
			{

			{
			%>
				document.genericForm.toDate.value ="<%=sdf.format(gm.getToDate())%>";
			<%
			}
			}
			%>
				document.genericForm.id.value ="<%=gm.getId()%>";
			<%
			}
			}
			%>


			}

			}
			function checkClassName()
			{

			<%
				if(className.equals("EmployeeStatusMaster")||className.equals("ReligionMaster")||className.equals("LanguagesKnownMaster")||className.equals("BloodGroupMaster"))
				{
			%>
				document.getElementById("fromDateRow").style.display="none";
				document.getElementById("toDateRow").style.display="none";
			<%
				}
			%>
			}
			</SCRIPT>

			</head>

			<body onload="checkMode();checkClassName()" >
		<table align="center" id="table3" width="100%" cellpadding ="0" cellspacing ="0" border = "0">
		<tr><td>
			
		<div class="formmainbox">
			<div class="insidecontent">
		  <div class="rbroundbox2">
			<div class="rbtop2"><div></div></div>
			  <div class="rbcontent2">
			<!-- Header Section Begins -->

			<!-- Header Section Ends -->


	
			

			<html:form  action="/pims/AfterGenericMasterAction" >
			<table align='center' id="table2"  WIDTH=95%  cellpadding ="0" cellspacing ="0" border = "0">
			<tr>
				<td>
					<input type=hidden name="id" id="id" />
					<table width="100%" border="0" cellspacing="0" cellpadding="0">



						<%
						Map genericMap  = (Map)session.getAttribute("genericMap");
						Map genericTable  = (Map)genericMap.get("genericTable");
						System.out.println("genericTable"+genericTable);
						Map genericName  = (Map)genericMap.get("genericName");
						String tableName = ((String)genericTable.get(className)).trim();
						System.out.println("tableName"+tableName);
						String nameOfMaster = (String)genericName.get(className);
						String columnname = ((String)getColumnMap().get(className)).trim();

			 			%>
						<input type = hidden name="className" id="className" value="<%=className%>" />
						<tr><td>&nbsp;</td></tr>
						<tr>

		    				<td colspan="8" class="headingwk"><div class="arrowiconwk">
									<img src="<%=request.getContextPath()%>/common/image/arrow.gif" /></div>
							<div class="headplacer"><%=nameOfMaster%></div></td>
						</tr>
						<tr><td>&nbsp;</td></tr>
						<tr>
						<%if("Employee Status".equals(nameOfMaster)) {%>
						<td class="whiteboxwk"  ><span class="mandatory">*</span>Employee Type <bean:message key="EmpName"/></td>
						<% }else{%>
						<td class="whiteboxwk"  ><span class="mandatory">*</span><%=nameOfMaster%>  <bean:message key="EmpName"/></td>
						<% }%>
						<td class="whitebox2wk"  ><input type="text"  class="selectwk" id="name" name="name" onblur="uniqueChecking('/commonyui/egov/uniqueCheckAjax.jsp',  '<%=tableName%>', '<%=columnname%>', 'name', 'no', 'no')"  /></td>
						 </tr>
						<tr id = "fromDateRow"><td class="greyboxwk" align="right"><span class="mandatory">*</span><bean:message key="StartDate"/></td>
						<td class="greybox2wk" align="center"><input type="text"  class="selectwk" id="fromDate" name="fromDate" onBlur = "validateDateFormat(this);CompareDate(this);" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')"/></td>
						</tr>
						<tr id = "toDateRow"><td class="whiteboxwk" align="right"><span class="mandatory">*</span><bean:message key="EndDate"/></td>
						<td class="whitebox2wk" align="center"><input type="text"  class="selectwk" id="toDate" name="toDate" onBlur = "validateDateFormat(this);CompareDate(this);" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')"/></td>
						</tr>
						<tr><td colspan=2>&nbsp;</td></tr>
					</table></td></tr>
					<tr>
            		<td><div align="right" class="mandatory">* Mandatory Fields</div></td>
          			</tr>
					

		   		</table>
			</html:form>
			</div>
			<div class="rbbot2"><div></div></div>
		</div>
		</td></tr>

		<tr><td>
					<table align=center name="buttonTable" id="buttonTable" border="0">
						<tr>
							<%
							if(!((String)(session.getAttribute("viewMode"))).trim().equals("view"))
							{
							%>
							<td><html:button styleClass="buttonfinal" value="Save" property="b4" onclick="ButtonPress('savenew')" /></td>
							<%
							}
							%>
							<td><html:button styleClass="buttonfinal" value="Modify" property="b4" onclick="goto('modify')" /></td>
							<td><html:button styleClass="buttonfinal" value="View" property="b4" onclick="goto('view')" /></td>
							<td><html:button styleClass="buttonfinal" value="Delete" property="b4" onclick="goto('delete')" /></td>
						</tr>
		   			</table></td></tr>
		   	</table>
			</body>
			<!-- footer section starts -->
			
		  </div>
	
			</html>

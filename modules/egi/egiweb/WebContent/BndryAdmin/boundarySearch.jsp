<%@page import="java.util.ArrayList, org.egov.infstr.utils.EgovMasterDataCaching"%>
<%@ include file="/includes/taglibs.jsp" %>
<%
ArrayList hierarchyTypeList=(ArrayList)EgovMasterDataCaching.getInstance().get("egi-hierarchyType");
%>
<html>
	<head>
		<title>Search Boundary </title>
		<script type="text/javascript">	
		
		function goToModify() {		
			if(document.getElementById('heirarchyType').options[document.getElementById('heirarchyType').selectedIndex].value =="0")   {
			   alert("Please select the heirarchyType!!!");
			   document.getElementById('heirarchyType').focus();
			   return false;
		   	}
		   
		   	if(document.getElementById('name').options[document.getElementById('name').selectedIndex].value=="0" || document.getElementById('name').options[document.getElementById('name').selectedIndex].value=="") {
			   alert("Please select the Boundary Type!!!");
			   document.getElementById('name').focus();
			   return false;
			}
	
			document.forms[0].action = "<c:url value='/SearchBoundry.do?'/>";
			document.forms[0].submit();		
		}
		
		function goToDelete() {
		
			if(document.getElementById('heirarchyType').options[document.getElementById('heirarchyType').selectedIndex].value =="0") {
			   alert("Please select the heirarchyType!!!");
			   document.getElementById('heirarchyType').focus();
			   return false;
			}
			
			if(document.getElementById('name').options[document.getElementById('name').selectedIndex].value=="0" || document.getElementById('name').options[document.getElementById('name').selectedIndex].value=="") {		   
				alert("Please select the Boundary Type!!!");
			   document.getElementById('name').focus();
			   return false;
			}
			
			if(document.getElementById('bndryId').options[document.getElementById('bndryId').selectedIndex].value =="0" || document.getElementById('bndryId').options[document.getElementById('bndryId').selectedIndex].value=="")	{
			   alert("Please select the Boundary Value!!!");
			   document.getElementById('bndryId').focus();
			   return false;
	   		}
	
			document.forms[0].action = "<c:url value='/SetupBoundry.do?bool=DELETE'/>";
			document.forms[0].submit();		
		}
		
		function populateBoundaryType(obj) {
			loadSelectData('../commonyui/egov/loadComboAjax.jsp', 'EG_BOUNDARY_TYPE', 'ID_BNDRY_TYPE', 'NAME', 'ID_HEIRARCHY_TYPE=#1 order by ID_BNDRY_TYPE', obj.id, 'name');
		}
		</script>
	</head>
	<body>
		<html:form action="/Boundry" >
			<table align="center" width="400">
				<tr>
					<td class="tableheader" align="center" width="400" height="23"> Search Boundary</td>
				</tr>
				<tr>
					<td colspan=4>&nbsp;</td>
				</tr>
			</table>

			<table align="center"  width="400">
				<tr>
					<td  class="labelcell" width="40%" height="23" >Hierarchy Type<font class="ErrorText">*</font></td>
					<td class="labelcell"  align="left" width="40%" height="23" >
					<html:select property="heirarchyType"  styleId="heirarchyType" onchange="return populateBoundaryType(this)">
					<html:option value="0">--Choose--</html:option>
					<c:forEach var="hierarchyTypeObj" items="<%=hierarchyTypeList%>">
					    <html:option value="${hierarchyTypeObj.id}">${hierarchyTypeObj.name}</html:option>
			    		</c:forEach>
					</html:select>
					</td>
		
				</tr>
				<tr>
					<td  class="labelcell" width="40%" height="23" >Boundary Type<font class="ErrorText">*</font></td>
					<td  class="labelcell" align="left" width="40%" height="23" >
						<select  name="name" id="name" >
						<option value="0">--Choose--</option>
						</select>
					</td>
				</tr>
				<tr height="23px">
					<td colspan=4>&nbsp;</td>
				</tr>
				<tr>		
					<td height="23" class="button2"  align="center" colspan="2" align="right">
						<input type=button  value="Search" onclick="goToModify()"/>
					</td>
				</tr>
			</table>
		</html:form>
	</body>		
</html>

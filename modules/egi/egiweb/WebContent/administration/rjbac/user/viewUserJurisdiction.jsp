<%@ page import="java.util.*,
				java.text.*,
				org.egov.lib.rjbac.jurisdiction.*,org.egov.lib.rjbac.user.*,
				org.egov.lib.rjbac.jurisdiction.dao.*"%>
<%@ include file="/includes/taglibs.jsp" %>
<html>
<head>
<title>User Jurisdiction</title>
</head>
<%Set jurObjSet = new HashSet();
SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
if(request.getAttribute("jurObj")!=null)
{
	jurObjSet = (Set)request.getAttribute("jurObj");
	System.out.println("The size of the exsiting jurisdiction object is >>>>> " + jurObjSet.size());

}
String userIdStr="";
User user = null;
if(request.getAttribute("user")!=null)
{
	user = (User)request.getAttribute("user");
	userIdStr = (String)user.getId().toString();
	System.out.println("the user id in jsp--------------->  " +user.getId());
}%>
<body>


 <html:form method="post" action="/CreateJurisdiction.do?submitType=saveJurisdiction">

		<table  style="width: 810px;" cellpadding ="0" cellspacing ="0" border = "1"  >
		<tbody>
		<tr>
		  <td colspan="8" height=20 bgcolor=#dddddd align=middle  class="tableheader">
		<p>User Jurisdiction</p></td>
		  </tr>



			<tr>
				<td   colspan="4" align="middle" width="50%" height="56" >
				<p align="center" class="labelcell"><b>User Name</b></td>
				<td   colspan="4" align="middle" width="47%" height="56"><p  class="labelcell">
				<%if(user!=null)
				{%>
				<b><%=user.getUserName()%></b>
				<%}
				else
				{%>
				<b>&nbsp;</b>
				<%}%>
				</td>
				</tr>
				</table>
				<table  border="1" height="25"  class="tableStyle" >
				<tr >
						<td class="tablesubcaption" vAlign="bottom"  colspan="5" align="left" width="17%" height="29">
						<b>Boundary Type Values</b>

						</td>
					</tr>

				<tr >
						<td vAlign="bottom" align="left" width="17%" height="29">
												<p align="left" class="labelcell"><b>BoundaryTypeValue</b></td>
						<td vAlign="bottom" align="left" width="17%" height="29">
												<p align="left" class="labelcellforsingletd">
												<b>From Date(dd/mm/yyyy)</b></td>
						<td vAlign="bottom" align="left" width="16%" height="29">
												<p align="center" class="labelcell">
												<b>To Date(dd/mm/yyyy)</b></td>
					</tr>
				<%
				if(jurObjSet!=null && !jurObjSet.isEmpty()){
					for (Iterator iter = jurObjSet.iterator(); iter.hasNext();) {
						JurisdictionValues jur = (JurisdictionValues)iter.next();%>
				<html:hidden property="bndryTypeID" value="<%=jur.getBoundary().getBoundaryType().getId().toString()%>"/>
				<html:hidden property="bndryID" value="<%=jur.getId().toString()%>"/>

				<tr >
						<td class="labelcell" vAlign="bottom" align="left" width="17%" height="29">&nbsp;&nbsp;<%=jur.getBoundary().getName().toString()%></td>
						<td class="labelcell" vAlign="bottom" align="right" width="17%" height="29">&nbsp;&nbsp;<%=formatter.format(jur.getFromDate())%></td>
						<%if(jur.getToDate()!=null){%>
						<td class="labelcell" vAlign="bottom" align="center" width="16%" height="29">&nbsp;&nbsp;<%=formatter.format(jur.getToDate())%></td>
						<%}
						else
						{%>
							<td class="labelcell" vAlign="bottom" align="center" width="16%" height="29">&nbsp;</td>
						<%}%>
					</tr>
				<%}}%>
				</table>
				<table class="tableStyle">
					<tr >
						<td vAlign="bottom" align="left" colSpan="6" height="23">
						<p align="center">
						<input type="button" class="button2" value="Back" onclick="window.location = '<c:url value='/administration/rjbac/user/searchUser.jsp'/>';">
						</td>
					</tr>
					</table>


</html:form>



</body>
</html>

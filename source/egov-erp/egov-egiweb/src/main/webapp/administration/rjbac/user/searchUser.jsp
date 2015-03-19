<%@page import="java.util.*,org.egov.lib.admbndry.*,org.egov.lib.rjbac.dept.*,org.egov.lib.rjbac.role.*"%>
<%@page import="org.egov.infra.admin.master.entity.User"%>
<%@page import="org.egov.lib.rjbac.user.UserImpl"%>
<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib uri="/deptRole" prefix="deptRole"%>


<% 
	UserImpl user = null;
	user =(UserImpl)session.getAttribute("userDetail");
	ArrayList list= new ArrayList();
	if(user!=null) {
		list.add(user);
	}
	session.removeAttribute("userDetail"); 
%>
		

<html>

	<head>
		<title>Search User</title>
		<script>
			var codeObj = null;
			function onBodyLoad() {
				codeObj = loadYUIAjaxData('/egi/commonyui/egov/yuiDataAjax.jsp?applXmlName=egi_sqlconfig.xml&xmlTagName=egi-userName');
			}
			
			function autocompletecode(obj,eve) {
				doAutoComplete(1,obj,'codescontainer',codeObj,15,eve);
			}
			
			function fillusernames() {
				markYuiflagUndefined(1);
			}
			
			function validation() {
			   var result=uniqueCheckingBoolean('/egi/commonyui/egov/uniqueCheckAjax.jsp' , 'EG_USER', 'USER_NAME', 'userName', 'no', 'no');
			 	if(result) {
			   		alert("User Name does not match");
			   		document.getElementById('userName').value="";
			   		document.getElementById('userName').focus();
			   		return false;
				}
			    if(document.getElementById('userName').value == "") {
			   		alert("User Name is required");
			   		return false;
			   } else {
			   		return true;
			   }
			}
	
	</script>
		
		<style type="text/css">
			#codescontainer .yui-ac-content {position:absolute;min-width:250px;left:inherit;border:1px solid #404040;background:#fff;overflow:hidden;z-index:9050;clear:both}
			#codescontainer .yui-ac-shadow {position:absolute;margin:.3em;width:100%;background:#a0a0a0;z-index:9049;}
			#codescontainer ul {padding:5px 0;width:100%;}
			#codescontainer li {padding:0 5px;cursor:default;white-space:nowrap;}
			#codescontainer li.yui-ac-highlight {background:#ff0;}
			#codescontainer li.yui-ac-prehighlight {background:#FFFFCC;}
			table.its thead th {position:relative;display:table-cell;z-index: 0;}
		</style>		
	</head>
	<body bgcolor="#FFFFFF" onload="onBodyLoad()">
		<html:form action="SearchUser?" method="POST" onsubmit="return validation()">
			<table align="center" class="tableStyle" width="100%">
				<tr>
					<td colspan='4'>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td class="tableheader" valign="middle" align="center" colspan="2" height="26">
						<bean:message key="SearchUserlabel" />
						<bean:message key="SearchUserlabel.ll" />
					</td>
				</tr>
			</table>
			<br/>
			<table align="center" class="tableStyle" width="50%">
				<tr>
					<td class="labelcell" width="25%" height="23">
						<bean:message key="userName" />
						<bean:message key="userName.ll" />
						<font class="ErrorText"></font>
					</td>
					<td class="labelcell" align="center">
						<input type="text" id="userName" name="userName" autocomplete="off" onkeyup="autocompletecode(this,event);" onblur="fillusernames();" />
						<div id="codescontainer"></div>
					</td>					
				</tr>
			</table>
			<br/>
			<table align="center" width="100%">
				<tr>
					<td class="button2" align="center">
						<html:submit value="Search"/>
					</td>
					
				</tr>
			</table>
			<br/>
		<%		
		if (user!=null) {
		%>
		<display:table style="color:black;border:1px solid black;thead:block" name="<%=list%>" class="its" export="false" id="its" cellspacing="0" cellpadding="0" >
			<display:column property="id" title="ID" />
			<display:column property="title" title="TITLE" />
			<display:column property="salutation" title="SALUTATION" />
			<display:column property="firstName" title="FIRST NAME" />
			<display:column property="middleName" title="MIDDLE NAME" />
			<display:column property="lastName" title="LAST NAME" />
			<display:column property="updateTime" title="UPDATE TIME" />
			<display:column property="userName" url="/SetupUpdateUser.do" paramId="userName" title="USER NAME" />
			<display:column property="fromDate" title="FROM DATE" />
			<display:column property="toDate" title="TO DATE" />
		</display:table>
		<html:hidden property="userDetail" value="<%= Integer.toString(user.getId().intValue()) %>" />
		<c:set var="userid">
			<%=user.getId()%>
		</c:set>
		<br/>
		<input type="button" style="font-size:12px" name="viewJur" value="View Jurisdiction" onclick="window.location = '<c:url value='/CreateJurisdiction.do?submitType=viewJurisdiction&userid=${userid}'/>';" />
		<input type="button" style="font-size:12px"  name="viewJur" value="Edit Jurisdiction" onclick="window.location = '<c:url value='/BeforeJurisdiction.do?userid=${userid}'/>';" />
		<div class="ErrorText">
			Click on a User Name to edit user info
		</div>
		<%
		}				
		%>				
		</html:form>
	</body>
</html>
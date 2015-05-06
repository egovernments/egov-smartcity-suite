<!-- /*
#   eGov suite of products aim to improve the internal efficiency,transparency, 
#      accountability and the service delivery of the government  organizations.
#   
#       Copyright (C) <2015>  eGovernments Foundation
#   
#       The updated version of eGov suite of products as by eGovernments Foundation 
#       is available at http://www.egovernments.org
#   
#       This program is free software: you can redistribute it and/or modify
#       it under the terms of the GNU General Public License as published by
#       the Free Software Foundation, either version 3 of the License, or
#       any later version.
#   
#       This program is distributed in the hope that it will be useful,
#       but WITHOUT ANY WARRANTY; without even the implied warranty of
#       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#       GNU General Public License for more details.
#   
#       You should have received a copy of the GNU General Public License
#       along with this program. If not, see http://www.gnu.org/licenses/ or 
#       http://www.gnu.org/licenses/gpl.html .
#   
#       In addition to the terms of the GPL license to be adhered to in using this
#       program, the following additional terms are to be complied with:
#   
#   	1) All versions of this program, verbatim or modified must carry this 
#   	   Legal Notice.
#   
#   	2) Any misrepresentation of the origin of the material is prohibited. It 
#   	   is required that all modified versions of this material be marked in 
#   	   reasonable ways as different from the original version.
#   
#   	3) This license does not grant any rights to any user of the program 
#   	   with regards to rights under trademark law for use of the trade names 
#   	   or trademarks of eGovernments Foundation.
#   
#     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
# */ -->
<%@ include file="/includes/taglibs.jsp" %>

<%@ page import="java.sql.Connection,java.sql.PreparedStatement,
org.egov.infstr.client.administration.rjbac.user.UserForm,
org.egov.infstr.utils.ServiceLocator,
org.egov.lib.rjbac.user.User,org.egov.lib.admbndry.BoundaryDAO,
org.egov.lib.rjbac.user.ejb.api.UserManager,
org.egov.lib.rjbac.user.ejb.api.UserManagerHome;"
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
 <html xmlns="http://www.w3.org/1999/xhtml">
 <head>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

        <%@ include file="/includes/meta.jsp" %>
        <title>eGov-BPA - <decorator:title/> </title>


         <link rel="stylesheet" type="text/css" media="all" href="${pageContext.request.contextPath}/common/css/commonegov.css" />
          <link rel="stylesheet" type="text/css" media="all" href="${pageContext.request.contextPath}/common/css/bpa.css" />
      	<link rel="stylesheet" type="text/css" href="<c:url value='/commonyui/build/reset/reset.css'/>" />
	<link rel="stylesheet" type="text/css" href="<c:url value='/commonyui/build/fonts/fonts.css'/>" />



        <script type="text/javascript" src="<c:url value='/javascript/calender.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/script/calendar.js'/>" ></script>
		<script type="text/javascript" src="<c:url value='/javascript/validations.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/javascript/SASvalidation.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/javascript/dateValidation.js'/>"></script>

	<link rel="stylesheet" type="text/css" href="<c:url value='/commonyui/yui2.7/reset/reset.css'/>" />
		<link rel="stylesheet" type="text/css" href="<c:url value='/commonyui/yui2.7/assets/skins/sam/autocomplete.css'/>" />
		
		

	<script type="text/javascript" src="<c:url value='/commonyui/yui2.7/yahoo/yahoo.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/yui2.7/dom/dom.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/yui2.7/event/event-debug.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/yui2.7/connection/connection-min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/yui2.7/animation/animation.js'/>"></script>
	<!--<script type="text/javascript" src="<c:url value='/commonyui/yui2.7/datasource/datasource-beta.js' />"></script>-->
	<script type="text/javascript" src="<c:url value='/commonyui/yui2.7/json/json-min.js' />"></script> 
	<script type="text/javascript" src="<c:url value='/commonyui/yui2.7/yahoo-dom-event/yahoo-dom-event.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/yui2.7/dragdrop/dragdrop.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/yui2.7/element/element.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/yui2.7/connection/connection-min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/yui2.7/datasource/datasource-min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/yui2.7/autocomplete/autocomplete-min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/yui2.7/datatable/datatable.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/yui2.7/animation/animation-min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/yui2.7/element/element-min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/yui2.7/container/container_core-min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/yui2.7/menu/menu-min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/yui2.7/button/button-min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/yui2.7/editor/editor-min.js'/>"></script>
	<script type="text/javascript"  src="<c:url value='/common/js/helper.js'/>"></script>
<%
	String cityName="india";
	
	if(session.getAttribute("cityName")==null)
	{
		BoundaryDAO boundaryDao= new BoundaryDAO();
	
		if(session.getAttribute("org.egov.topBndryID")!=null)
		{

		 cityName=boundaryDao.getBoundaryNameForID(new Integer((String)session.getAttribute("org.egov.topBndryID")));
		 cityName=cityName.toUpperCase();

		 session.setAttribute("cityName",cityName);


		}
	}else
	   cityName=(String)session.getAttribute("cityName");
		
	
	
%>
	    <style type="text/css">

	  	       	#codescontainer {position:absolute;left:17em;width:1000%}
	  	       	#codescontainer .yui-ac-content {position:absolute;width:100%;border:1px solid #404040;background:#fff;overflow:hidden;z-index:9050;}
	  	       	#codescontainer .yui-ac-shadow {position:absolute;margin:.3em;width:40%;background:#a0a0a0;z-index:9049;}
	  	       	#codescontainer ul {padding:5px 0;width:100%;}
	  	       	#codescontainer li {padding:0 5px;cursor:default;white-space:nowrap;}
	  	       	#codescontainer li.yui-ac-highlight {background:#ff0;}
	  	       	#codescontainer li.yui-ac-prehighlight {background:#FFFFCC;}


			#usercontainer {position:absolute;left:17em;width:1000%}
			#usercontainer .yui-ac-content {position:absolute;width:100%;border:1px solid #404040;background:#fff;overflow:hidden;z-index:9050;}
			#usercontainer .yui-ac-shadow {position:absolute;margin:.3em;width:40%;background:#a0a0a0;z-index:9049;}
			#usercontainer ul {padding:5px 0;width:100%;}
			#usercontainer li {padding:0 5px;cursor:default;white-space:nowrap;}
			#usercontainer li.yui-ac-highlight {background:#ff0;}
			#usercontainer li.yui-ac-prehighlight {background:#FFFFCC;}
  	    </style>

        <decorator:head/>
    </head>

<body <decorator:getProperty property="body.id" writeEntireProperty="yes"/><decorator:getProperty property="body.class" writeEntireProperty="true"/> <decorator:getProperty property="body.onload" writeEntireProperty="true"/>  >


<%
boolean admin=false;
Integer userID= (Integer)request.getSession().getAttribute("com.egov.user.LoginUserId");
 System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Egov Site mesh Header userID "+userID);
 String userName="";
 ServiceLocator serviceloc = ServiceLocator.getInstance();
 UserManagerHome uhome = (UserManagerHome)serviceloc.getLocalHome("UserManagerHome");
UserManager userManager = uhome.create();
if(userID !=null)
{
User user = userManager.getUserByID(userID);
 userName=user.getUserName();

}
 %>
   <!-- Body Begins -->


 <div class="topbar"><div class="egov"><img src="${pageContext.request.contextPath}/common/image/eGov.png" alt="eGov" width="54" height="58" /></div><div class="gov"><img src="${pageContext.request.contextPath}/common/image/<%=cityName%>.jpg" alt="<%=cityName%>" width="54" height="58" /></div>
 <div class="mainheading">City Administration  </div>
 </div>
 <div class="navibar"><div align="right">
   <table width="100%" border="0" cellspacing="0" cellpadding="0">
 <tr>
            <td><div align="left"><ul id="tabmenu" class="tabmenu">
                <li ><a id="loginhref" name="loginhref" href="#" onclick="javascript:top.location='/egi/logout.do'">Logout</a></li>
        </ul></div></td>

 </table></div>
</div>
<body <decorator:getProperty property="body.id" writeEntireProperty="yes"/><decorator:getProperty property="body.class" writeEntireProperty="true"/> <decorator:getProperty property="body.onload" writeEntireProperty="true"/>  >
	<decorator:body/>


</html>


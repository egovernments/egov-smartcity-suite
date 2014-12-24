<%@ include file="/includes/taglibs.jsp" %>

<%@ page import="java.sql.Connection,java.sql.PreparedStatement,
org.egov.infstr.client.administration.rjbac.user.UserForm,
org.egov.infstr.utils.HibernateUtil,
org.egov.lib.rjbac.user.User,org.egov.lib.admbndry.BoundaryDAO,
org.egov.lib.rjbac.user.ejb.api.UserService,
org.egov.lib.rjbac.user.ejb.server.UserServiceImpl;"
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<%@ include file="/includes/meta.jsp" %>
	<title>eGov-Tender - <decorator:title/> </title>
    <link rel="stylesheet" type="text/css" media="all" href="${pageContext.request.contextPath}/common/css/commonegov.css" />
    <link rel="stylesheet" type="text/css" media="all" href="${pageContext.request.contextPath}/common/css/tender.css" />
   	<link rel="stylesheet" type="text/css" href="<c:url value='/commonyui/build/reset/reset.css'/>" />
	<link rel="stylesheet" type="text/css" href="<c:url value='/commonyui/build/fonts/fonts.css'/>" />

    <script type="text/javascript" src="<c:url value='/javascript/calender.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/script/calendar.js'/>" ></script>
	<script type="text/javascript" src="<c:url value='/dhtml.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/javascript/validations.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/javascript/SASvalidation.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/javascript/dateValidation.js'/>"></script>

	<script type="text/javascript" src="<c:url value='/commonyui/build/yahoo/yahoo.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/build/dom/dom.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/build/autocomplete/autocomplete-debug.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/build/event/event-debug.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/build/animation/animation.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonjs/ajaxCommonFunctions.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/exility/CalendarPopup.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/exility/calendar.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/inventoryjs/validation.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/script/rowDetails.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/exility/PageManager.js'/>"></script>

	<SCRIPT type="text/javascript" src="<c:url value='/script/jsCommonMethods.js' />"></SCRIPT>

<%
	String cityName="india";
	if(session.getAttribute("cityName")==null) {
		BoundaryDAO boundaryDao= new BoundaryDAO();
	
		if(session.getAttribute("org.egov.topBndryID")!=null) {
			cityName=boundaryDao.getBoundaryNameForID(new Integer((String)session.getAttribute("org.egov.topBndryID")));
			cityName=cityName.toUpperCase();
			session.setAttribute("cityName",cityName);
		}
	} else {
	   cityName=(String)session.getAttribute("cityName");
	}
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
		boolean admin = false;
		Integer userID = (Integer) request.getSession().getAttribute("com.egov.user.LoginUserId");
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Egov Site mesh Header userID " + userID);
		String userName = "";
		UserService userService = new UserServiceImpl();
		if (userID != null) {
			User user = userService.getUserByID(userID);
			userName = user.getUserName();
		}
	%>
	<!-- Body Begins -->


<div class="topbar"><div class="egov"><img src="${pageContext.request.contextPath}/common/image/eGov.png" alt="eGov" width="54" height="58" /></div><div class="gov"><img src="${pageContext.request.contextPath}/common/image/<%=cityName%>.jpg" alt="<%=cityName%>" width="54" height="58" /></div>
	<div class="mainheading">City Administration  </div>
</div>
<div class="navibar"><div align="right">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
	 	<tr>
	        <td><div align="left">
	        		<ul id="tabmenu" class="tabmenu">
	            		<li ><a id="loginhref" name="loginhref" href="#" onclick="javascript:top.location='/egi/logout.do'">Logout</a></li>
	        		</ul>
	        	</div>
	        </td>
		</tr>
	 </table>
</div>
</div>
<body <decorator:getProperty property="body.id" writeEntireProperty="yes"/><decorator:getProperty property="body.class" writeEntireProperty="true"/> <decorator:getProperty property="body.onload" writeEntireProperty="true"/>  >
	<decorator:body/>
</html>


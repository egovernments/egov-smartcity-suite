<%@ include file="/includes/taglibs.jsp" %>
<%@ include file="/includes/meta.jsp" %>
<%@page import="org.egov.lib.admbndry.BoundaryDAO,
		org.egov.lib.rrbac.services.RbacService,
  		org.egov.lib.rrbac.services.RbacServiceImpl,
		org.egov.lib.rrbac.model.Action" %>
<html>
    <head>
      
        <title>eGov Tender - <decorator:title/> </title>
		<link rel="stylesheet" type="text/css" href="<c:url value='/common/css/tender.css'/>" />
        <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/common/css/commonegov.css'/>" />
      	
      	<link rel="stylesheet" type="text/css" href="<c:url value='/commonyui/yui2.8/reset/reset.css'/>" />
      	<link rel="stylesheet" type="text/css"  href="<c:url value='/commonyui/yui2.8/fonts/fonts-min.css'/>"/>
		<link rel="stylesheet" type="text/css"  href="<c:url value='/commonyui/yui2.8/datatable/assets/skins/sam/datatable.css'/>" />
		<link rel="stylesheet" type="text/css" href="<c:url value='/commonyui/yui2.8/assets/skins/sam/autocomplete.css'/>" />
		<link rel="stylesheet" type="text/css" href="<c:url value='/common/css/rowExpansion.css'/>" />
		<script type="text/javascript" src="<c:url value='/commonyui/yui2.8/yahoo/yahoo.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/commonyui/yui2.8/dom/dom.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/commonyui/yui2.8/event/event-debug.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/commonyui/yui2.8/connection/connection-min.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/commonyui/yui2.8/animation/animation.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/commonyui/build/datasource/datasource-beta.js' />"></script>
		<script type="text/javascript" src="<c:url value='/commonyui/yui2.8/json/json-min.js' />"></script> 
		<script type="text/javascript" src="<c:url value='/commonyui/yui2.8/yahoo-dom-event/yahoo-dom-event.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/commonyui/yui2.8/dragdrop/dragdrop.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/commonyui/yui2.8/element/element.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/commonyui/yui2.8/connection/connection-min.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/commonyui/yui2.8/datasource/datasource-min.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/commonyui/yui2.8/autocomplete/autocomplete-min.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/commonyui/yui2.8/datatable/datatable.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/commonyui/yui2.8/animation/animation-min.js'/>"></script>
		<script type="text/javascript" src="/egi/commonyui/yui2.8/element/element-min.js"></script>
		<script type="text/javascript" src="/egi/commonyui/yui2.8/container/container_core-min.js"></script>
		<script type="text/javascript" src="/egi/commonyui/yui2.8/menu/menu-min.js"></script>
		<script type="text/javascript" src="/egi/commonyui/yui2.8/button/button-min.js"></script>
	    <script type="text/javascript" src="/egi/commonyui/yui2.8/editor/editor-min.js"></script>
	
		<script type="text/javascript" src="<c:url value='/common/js/RowExpansionDT.js'/>"></script> 
		
		<script type="text/javascript" src="<c:url value='/javascript/calender.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/javascript/validations.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/javascript/SASvalidation.js'/>"></script>
		
		
		<script type="text/javascript" src="<c:url value='/commonjs/ajaxCommonFunctions.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/exility/CalendarPopup.js'/>"></script>
		
		<script type="text/javascript" src="<c:url value='/common/js/validation.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/common/js/calendar.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/common/js/dateValidation.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/common/js/BrowserCompatabilityFunctions.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/common/js/helper.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/common/js/tender.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/common/js/RowExpansionDT.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/script/rowDetails.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/exility/PageManager.js'/>"></script>
		<SCRIPT type="text/javascript" src="<c:url value='/script/jsCommonMethods.js' />"></SCRIPT>
		
		
		<script type="text/javascript" src="/egi/jsutils/prototype/prototype.js"></script>
	
  		<decorator:head/>
	</head>

 <%
 	String cityName="india";
 	if(session.getAttribute("cityName")!=null) {
 		cityName = (String)session.getAttribute("cityName");
 	} else {
 		if(session.getAttribute("org.egov.topBndryID")!=null) {
 	BoundaryDAO boundaryDao = new BoundaryDAO();
 	cityName = boundaryDao.getBoundaryNameForID(new Integer((String)session.getAttribute("org.egov.topBndryID")));
 	cityName = cityName.toUpperCase();
 	session.setAttribute("cityName",cityName);
 		}
 	}	    

 //Here we get the help url associated with this url

 	RbacService rbacService = new RbacServiceImpl();
 	Integer actId = null;
 	String helpUrl = null;
 	Action actobj = null;
 	String actionId = null;
 	actionId = (String) request.getParameter("actionid");

 	if ((actionId != null && !actionId.equals(""))) {
 		actId = Integer.valueOf(actionId);
 		actobj = rbacService.getActionById(actId);
 		helpUrl = actobj.getHelpURL();
 	} else {
 		String queryStr = request.getQueryString();
 		String url = request.getRequestURI().substring(request.getContextPath().length());
 		url = url + "?" + queryStr;

 		actobj = rbacService.getActionByURL(request.getContextPath(),url);
 		if (actobj != null) {
 			helpUrl = actobj.getHelpURL();
 		}
 	}
%>
<body <decorator:getProperty property="body.id" writeEntireProperty="yes"/><decorator:getProperty property="body.class" writeEntireProperty="true"/> <decorator:getProperty property="body.onload" writeEntireProperty="true"/>  >

<egovtags:breadcrumb/>

  <div class="headlargenew"><decorator:title/></div>
  
<decorator:body/>

</body>

</html>


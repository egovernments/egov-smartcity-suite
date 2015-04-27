<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="sx" uri="/WEB-INF/struts-dojo-tags.tld"%>

<html>
     <head>
        <%@ include file="/includes/meta.jsp" %>
        <title>eGov Birth And Death - <decorator:title/> </title>

		<link rel="stylesheet" type="text/css" href="<c:url value='/common/css/bnd.css'/>" />
        <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/common/css/commonegov.css'/>" />
      	
      	<link rel="stylesheet" type="text/css" href="<c:url value='/commonyui/yui2.7/reset/reset.css'/>" />
		<link rel="stylesheet" type="text/css" href="<c:url value='/commonyui/yui2.7/assets/skins/sam/autocomplete.css'/>" />
		

	<script type="text/javascript" src="<c:url value='/commonyui/yui2.7/yahoo/yahoo.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/yui2.7/dom/dom.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/yui2.7/event/event-debug.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/yui2.7/connection/connection-min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/yui2.7/animation/animation.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/yui2.7/json/json-min.js' />"></script> 
	<script type="text/javascript" src="<c:url value='/commonyui/yui2.7/yahoo-dom-event/yahoo-dom-event.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/yui2.7/dragdrop/dragdrop.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/yui2.7/element/element.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/yui2.7/connection/connection-min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/yui2.7/datasource/datasource-min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/yui2.7/autocomplete/autocomplete-min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/yui2.7/datatable/datatable.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/yui2.7/animation/animation-min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/yui2.7/editor/editor-min.js'/>"></script>
	
    <script type="text/javascript" src="<c:url value='/javascript/calender.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/javascript/validations.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/javascript/SASvalidation.js'/>"></script>
	
	
	<script type="text/javascript" src="<c:url value='/commonjs/ajaxCommonFunctions.js'/>"></script>
		
	<script type="text/javascript" src="<c:url value='/common/js/validation.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/common/js/calendar.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/common/js/dateValidation.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/common/js/BrowserCompatabilityFunctions.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/common/js/helper.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/common/js/bnd.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/script/rowDetails.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/script/jsCommonMethods.js' />"></script>
	<script type="text/javascript" src="/egi/jsutils/prototype/prototype.js"></script>
	
  <decorator:head/>
    </head>
<egovtags:breadcrumb/>
<div class="headlargenew"><decorator:title/></div>
<body <decorator:getProperty property="body.id" writeEntireProperty="yes"/><decorator:getProperty property="body.class" writeEntireProperty="true"/> <decorator:getProperty property="body.onload" writeEntireProperty="true"/>  >
	<decorator:body/>
</body>


</html>



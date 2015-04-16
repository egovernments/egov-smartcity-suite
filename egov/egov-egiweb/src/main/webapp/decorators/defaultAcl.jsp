<%@ include file="/includes/taglibs.jsp" %>
<html>
    <head>
        <%@ include file="/includes/meta.jsp" %>
        <title>eGov - <decorator:title/> </title>
<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/css/egov.css'/>" />
       <link rel="stylesheet" href="<egovtags:url path='/css/workflow/workflowAdmin.css'/>" />  
        	 	<link rel="stylesheet" href="<egovtags:url path='/css/assetmanagement.css'/>" />  
	<link rel="stylesheet" type="text/css" href="<egovtags:url path='/commonyui/yui2.7/datatable/assets/skins/sam/datatable.css'/>" />
 	<link rel="stylesheet" type="text/css" href="<egovtags:url path='/commonyui/yui2.7/assets/skins/sam/autocomplete.css'/>" />  
 	
        <script type="text/javascript" src="<c:url value='/javascript/calender.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/script/calendar.js'/>" ></script>
		<script type="text/javascript" src="<c:url value='/javascript/validations.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/javascript/SASvalidation.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/javascript/dateValidation.js'/>"></script>	
		<script type="text/javascript" src="<c:url value='/script/jsCommonMethods.js' />"></script>
		<script type="text/javascript" src="<c:url value='/javascript/acl/accessPermissions.js' />"></script>
		
		
 	
	
	<script type="text/javascript" src="<egovtags:url path='/commonyui/yui2.7/yahoo-dom-event/yahoo-dom-event.js'/>"></script>
	<script type="text/javascript" src="<egovtags:url path='/commonyui/yui2.7/calendar/calendar-min.js'/>"></script> 
   	<script type="text/javascript" src="<egovtags:url path='/commonyui/yui2.7/yahoo/yahoo.js'/>"></script>
   	<script type="text/javascript" src="<egovtags:url path='/commonyui/yui2.7/yahoo-dom-event/yahoo-dom-event.js'/>"></script>
	<script type="text/javascript" src="<egovtags:url path='/commonyui/yui2.7/dragdrop/dragdrop.js'/>"></script>
	<script type="text/javascript" src="<egovtags:url path='/commonyui/yui2.7/element/element.js'/>"></script>
	<script type="text/javascript" src="<egovtags:url path='/commonyui/yui2.7/connection/connection-min.js'/>"></script>
	<script type="text/javascript" src="<egovtags:url path='/commonyui/yui2.7/datasource/datasource-min.js'/>"></script>
	<script type="text/javascript" src="<egovtags:url path='/commonyui/yui2.7/autocomplete/autocomplete-min.js'/>"></script>
	<script type="text/javascript" src="<egovtags:url path='/commonyui/yui2.7/datatable/datatable.js'/>"></script>
	<script type="text/javascript" src="<egovtags:url path='/commonyui/yui2.7/animation/animation-min.js'/>"></script>
		
        <decorator:head/>
    </head>
    
<body style="background-color: white"  class="yui-skin-sam"  <decorator:getProperty property="body.id" writeEntireProperty="yes"/><decorator:getProperty property="body.class" writeEntireProperty="true"/> <decorator:getProperty property="body.onload" writeEntireProperty="true"/>  >
	<egovtags:breadcrumb/>
	<br/>
				
		  						<decorator:body/>
		  					
			
</body>
</html>
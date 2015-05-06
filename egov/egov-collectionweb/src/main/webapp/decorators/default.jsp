<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/taglib" %>
<html>
    <head>
        <%@ include file="/includes/meta.jsp" %>
        <title>eGov-<decorator:title/> </title>

       		<SCRIPT type="text/javascript" src="/egi/script/jsCommonMethods.js"></SCRIPT>
	   
		<!--add css starts-->
		<link href="/egi/css/commonegov.css" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="<egov:url path='/css/collections.css'/>" />
		<link rel="stylesheet" type="text/css" href="<egov:url path='/yui/fonts/fonts-min.css'/>" />
		<link rel="stylesheet" type="text/css" href="<egov:url path='/yui/datatable/assets/skins/sam/datatable.css'/>" />
		<link rel="stylesheet" href="<egov:url path='/js/modalbox.css'/>" type="text/css" media="screen" />
		
		<script type="text/javascript" src="<egov:url path='/yui/yahoo-dom-event/yahoo-dom-event.js'/>"></script>
		<script type="text/javascript" src="<egov:url path='/yui/calendar/calendar-min.js'/>"></script> 
		<script type="text/javascript" src="<egov:url path='/yui/yahoo/yahoo.js'/>"></script>
		<script type="text/javascript" src="<egov:url path='/yui/yahoo-dom-event/yahoo-dom-event.js'/>"></script>
		<script type="text/javascript" src="<egov:url path='/yui/dragdrop/dragdrop.js'/>"></script>
		<script type="text/javascript" src="<egov:url path='/yui/element/element.js'/>"></script>
		<script type="text/javascript" src="<egov:url path='/yui/connection/connection-min.js'/>"></script>
		<script type="text/javascript" src="<egov:url path='/yui/datasource/datasource-min.js'/>"></script>
		<script type="text/javascript" src="<egov:url path='/yui/autocomplete/autocomplete-min.js'/>"></script>
		<script type="text/javascript" src="<egov:url path='/yui/datatable/datatable.js'/>"></script>
		<script type="text/javascript" src="<egov:url path='/yui/animation/animation-min.js'/>"></script>

		<script type="text/javascript" src="/egi/commonjs/ajaxCommonFunctions.js"></script>  
		<script type="text/javascript" src="/egi/javascript/calender.js"></script>
		<script type="text/javascript" src="/egi/script/calendar.js" ></script>
		<script type="text/javascript" src="/egi/javascript/validations.js"></script>
		<SCRIPT type="text/javascript" src="/egi/script/jsCommonMethods.js"></SCRIPT>
		
		<script type="text/javascript" src="<egov:url path='/js/collections.js'/>"></script>
		<script type="text/javascript" src="<egov:url path='/js/prototype.js'/>"></script>
   		<script type="text/javascript" src="<egov:url path='/js/scriptaculous.js?load=effects'/>"></script>
   		<script type="text/javascript" src="<egov:url path='/js/modalbox.js'/>"></script>
   		<script type="text/javascript" src="<egov:url path='/js/helper.js'/>"></script>
   		
   		<script type="text/javascript" src="/egi/javascript/jquery/ajax-script.js"></script>  
	    <script type="text/javascript" src="<c:url value='/javascript/jquery/jquery-1.7.2.min.js'/>"></script>
    	<script type="text/javascript" src="<c:url value='/javascript/jquery/jquery-ui-1.8.22.custom.min.js'/>"></script>
    	<link rel="stylesheet" type="text/css" href="<c:url value='/css/jquery/jquery-ui-1.8.22.custom.css'/>" />
	<!--add css ends-->
        <decorator:head/>
    </head>
    
<body <decorator:getProperty property="body.id" writeEntireProperty="yes"/><decorator:getProperty property="body.class" writeEntireProperty="true"/> <decorator:getProperty property="body.onload" writeEntireProperty="true"/>  >
<egov:breadcrumb/>
  
	<decorator:body/>
	</div>
	</body>
</html>

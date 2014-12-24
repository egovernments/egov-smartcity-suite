<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page"%>
<!DOCTYPE html>
<html>
    <head>
        <title>eGov-<decorator:title/> </title>
    	<link href='/tradelicense/css/professionaltax.css' rel="stylesheet" type="text/css" />
    	<link href="/egi/css/commonegov.css" rel="stylesheet" type="text/css" />
    	<link href='/tradelicense/css/displaytable.css' rel="stylesheet" type="text/css" />
    	<link rel="stylesheet" type="text/css" href="/egi/commonyui/yui2.7/fonts/fonts-min.css" />
		<link rel="stylesheet" type="text/css" href="/egi/commonyui/yui2.7/datatable/assets/skins/sam/datatable.css" />
		<link rel="stylesheet" type="text/css" href="/egi/commonyui/yui2.7/assets/skins/sam/autocomplete.css" />
		<link rel="stylesheet" href="/tradelicense/js/modalbox.css" type="text/css" media="screen" />
		
		<script type="text/javascript" src="/egi/script/jsCommonMethods.js"></script>		
	    <script type="text/javascript" src="/egi/commonyui/yui2.7/yahoo-dom-event/yahoo-dom-event.js"></script>
	    <script type="text/javascript" src="/egi/commonyui/yui2.7/calendar/calendar-min.js"></script> 
	    <script type="text/javascript" src="/egi/commonyui/yui2.7/yahoo/yahoo.js"></script>
	    <script type="text/javascript" src="/egi/commonyui/yui2.7/yahoo-dom-event/yahoo-dom-event.js"></script>
		<script type="text/javascript" src="/egi/commonyui/yui2.7/dragdrop/dragdrop.js"></script>
		<script type="text/javascript" src="/egi/commonyui/yui2.7/element/element.js"></script>
		<script type="text/javascript" src="/egi/commonyui/yui2.7/connection/connection-min.js"></script>
		<script type="text/javascript" src="/egi/commonyui/yui2.7/datasource/datasource-min.js"></script>
		<script type="text/javascript" src="/egi/commonyui/yui2.7/autocomplete/autocomplete-min.js"></script>
		<script type="text/javascript" src="/egi/commonyui/yui2.7/datatable/datatable.js"></script>
		<script type="text/javascript" src="/egi/commonyui/yui2.7/animation/animation-min.js"></script>
		<script type="text/javascript" src="/egi/commonjs/ajaxCommonFunctions.js"></script>  
		<script type="text/javascript" src="/egi/javascript/calender.js"></script>
		<script type="text/javascript" src="/egi/script/calendar.js" ></script>
		<script type="text/javascript" src="/egi/javascript/validations.js"></script>
		<script type="text/javascript" src="/egi/script/jsCommonMethods.js"></script>
		<script type="text/javascript" src="/egi/javascript/dateValidation.js"></script>
		<script type="text/javascript" src='/tradelicense/js/prototype.js'></script>
   		<script type="text/javascript" src='/tradelicense/js/scriptaculous.js?load=effects'></script>
   		<script type="text/javascript" src='/tradelicense/js/modalbox.js'></script>
   		<script type="text/javascript" src='/tradelicense/js/pt.js'></script>  
		<script type="text/javascript" src='/tradelicense/js/helper.js'></script>
		<script type="text/javascript" src='/tradelicense/javascript/license/license-common.js'></script>
		<s:head/>
		<sj:head />		
        <decorator:head/>
    </head>
    
	<body <decorator:getProperty property="body.id" writeEntireProperty="yes"/><decorator:getProperty property="body.class" writeEntireProperty="true"/> <decorator:getProperty property="body.onload" writeEntireProperty="true"/>  >
  		<egov:breadcrumb/>
		<decorator:body/>
	</body>
</html>

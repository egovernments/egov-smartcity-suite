<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<html>
    <head>
        <%@ include file="/includes/meta.jsp" %>
        <title>eGov-<decorator:title/> </title>

       		<SCRIPT type="text/javascript" src="/egi/script/jsCommonMethods.js"></SCRIPT>
	   
		<!--add css starts-->
	    	<link href="<egov:url path='/css/professionaltax.css'/>" rel="stylesheet" type="text/css" />
	    	<link href="/egi/css/commonegov.css" rel="stylesheet" type="text/css" />
	    	<link href="<egov:url path='/css/displaytable.css'/>" rel="stylesheet" type="text/css" />
	    	<link rel="stylesheet" type="text/css" href="<egov:url path='/yui/fonts/fonts-min.css'/>" />
		<link rel="stylesheet" type="text/css" href="<egov:url path='/yui/datatable/assets/skins/sam/datatable.css'/>" />
		<link rel="stylesheet" type="text/css" href="<egov:url path='/yui/assets/skins/sam/autocomplete.css'/>" />
		<link rel="stylesheet" href="<egov:url path='/js/modalbox.css'/>" type="text/css" media="screen" />
		<link rel="stylesheet" type="text/css" href="<egov:url path='/css/jquery/jquery-ui-1.8.22.custom.css'/>" />
		
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
		<SCRIPT type="text/javascript" src="/egi/javascript/dateValidation.js"></SCRIPT>
		<script type="text/javascript" src="<egov:url path='/js/prototype.js'/>"></script>
   		<script type="text/javascript" src="<egov:url path='/js/scriptaculous.js?load=effects'/>"></script>
   		<script type="text/javascript" src="<egov:url path='/js/modalbox.js'/>"></script>
   		<script type="text/javascript" src="<egov:url path='/js/pt.js'/>"></script>  
		<script type="text/javascript" src="<egov:url path='/js/helper.js'/>"></script>
		<script type="text/javascript" src="<egov:url path='/javascript/license-common.js'/>"></script>
		<script type="text/javascript" src="<egov:url path='/javascript/jquery/jquery-1.7.2.min.js'/>"></script>
   	<script type="text/javascript" src="<egov:url path='/javascript/jquery/jquery-ui-1.8.22.custom.min.js'/>"></script>
		<script type="text/javascript" src="<egov:url path='/js/ajax-script.js'/>"></script>
	<!--add css ends-->
        <decorator:head/>
    </head>
    
<body <decorator:getProperty property="body.id" writeEntireProperty="yes"/><decorator:getProperty property="body.class" writeEntireProperty="true"/> <decorator:getProperty property="body.onload" writeEntireProperty="true"/>  >
<div id="loadingMask" style="display:none;overflow:none;scroll:none;" ><img src="/egi/images/bar_loader.gif"> <span id="message">Please wait....</span></div>
  	<egov:breadcrumb/>
 
	<div class="formmainbox">
	<decorator:body/>

	</body>
</html>

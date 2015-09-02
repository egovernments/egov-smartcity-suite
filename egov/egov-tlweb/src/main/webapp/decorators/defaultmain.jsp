<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<html>
    <head>
        <%@ include file="/includes/meta.jsp" %>
        <title>eGov - <decorator:title/> </title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<SCRIPT type="text/javascript" src="/egi/script/jsCommonMethods.js"></SCRIPT>
	   
		<!--add css starts-->
	    	<link href="<egov:url path='/css/professionaltax.css'/>" rel="stylesheet" type="text/css" />
	    	<link href="/egi/css/commonegov.css" rel="stylesheet" type="text/css" />    
	    	<script type="text/javascript" src="<egov:url path='/yui/yahoo-dom-event/yahoo-dom-event.js'/>"></script>
			    	<script type="text/javascript" src="<egov:url path='/yui/calendar/calendar-min.js'/>"></script> 
	    	<script type="text/javascript" src="<egov:url path='/yui/yahoo/yahoo.js'/>"></script>
	    	
		<script type="text/javascript" src="/egi/commonjs/ajaxCommonFunctions.js"></script>  
		<script type="text/javascript" src="/egi/javascript/calender.js"></script>
		<script type="text/javascript" src="/egi/script/calendar.js" ></script>
		<script type="text/javascript" src="/egi/javascript/validations.js"></script>
		<SCRIPT type="text/javascript" src="/egi/script/jsCommonMethods.js"></SCRIPT>
		
		<script type="text/javascript" src="<egov:url path='/js/prototype.js'/>"></script>
   		<script type="text/javascript" src="<egov:url path='/js/scriptaculous.js?load=effects'/>"></script>
   		<script type="text/javascript" src="<c:url value='/js/modalbox.js'/>"></script>
   		<link rel="stylesheet" href="<egov:url path='/js/modalbox.css'/>" type="text/css" media="screen" />
		<script type="text/javascript" src="<egov:url path='/js/pt.js'/>"></script> 
		<script type="text/javascript" src="<egov:url path='/js/helper.js'/>"></script>
        <decorator:head/>
    </head>
    
<body <decorator:getProperty property="body.id" writeEntireProperty="yes"/><decorator:getProperty property="body.class" writeEntireProperty="true"/> <decorator:getProperty property="body.onload" writeEntireProperty="true"/>  >


<!-- Header section begins -->
 <div class="topbar"><div class="egov"><img src="images/eGov.png" alt="eGov" width="54" height="58" /></div><div class="gov"><img src="images/india.png" alt="India" width="54" height="58" /></div>
 <div class="mainheading">Corporation of Chennai </div>
</div>
  
  
<!-- Header section ends -->

  
	<decorator:body/>

	


	</body>
</html>

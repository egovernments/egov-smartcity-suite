<%@ include file="/includes/taglibs.jsp" %>
<html>
    <head>
        <%@ include file="/includes/meta.jsp" %>
        <title>eGov  - <decorator:title/> </title>
		 <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/common/css/commonegov.css'/>" />
        <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/common/css/eispayroll.css'/>" />
		 <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/common/css/displaytable.css'/>" />
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
	<script type="text/javascript" src="<c:url value='/common/js/helper.js'/>"></script>
	
	<SCRIPT type="text/javascript" src="<c:url value='/script/jsCommonMethods.js' />"></SCRIPT>
	<SCRIPT type="text/javascript" src="<c:url value='/javascript/workflow/inbox.js' />"></SCRIPT>
	<SCRIPT type="text/javascript" src="<c:url value='/javascript/payrollUtil.js' />"></SCRIPT>
	
	<link rel="stylesheet" type="text/css" href="<egovtags:url path='/commonyui/yui2.7/datatable/assets/skins/sam/datatable.css'/>" />
	<link rel="stylesheet" type="text/css" href="<egovtags:url path='/commonyui/yui2.7/assets/skins/sam/autocomplete.css'/>" />
	
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
    
<body <decorator:getProperty property="body.id" writeEntireProperty="yes"/><decorator:getProperty property="body.class" writeEntireProperty="true"/> <decorator:getProperty property="body.onload" writeEntireProperty="true"/>  >

 
<body id="Home">
<div class="topbar">
  <div style="margin-top:10px"><decorator:title/></div>
  
</div>
<div class="navibarwk"><div class="piconwk"><a href="#"><img src="<c:url value='/common/image/help.png'/>" alt="Help" width="16" height="16" border="0" /></a></div>

</div>

  
 
  
	<decorator:body/>


	</body>
</html>

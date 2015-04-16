<%@ include file="/includes/taglibs.jsp" %>
<html>
    <head>
        <%@ include file="/includes/meta.jsp" %>
        <title>eGov  - <decorator:title/> </title>

        <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/css/egov.css'/>" />
      	<link rel="stylesheet" type="text/css" href="<c:url value='/commonyui/build/reset/reset.css'/>" />
	<link rel="stylesheet" type="text/css" href="<c:url value='/commonyui/build/fonts/fonts.css'/>" />

        <script type="text/javascript" src="<c:url value='/javascript/calender.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/script/calendar.js'/>" ></script>
	<script type="text/javascript" src="<c:url value='/javascript/validations.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/javascript/SASvalidation.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/javascript/dateValidation.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/javascript/commoneis.js'/>"></script>
	
	<script type="text/javascript" src="<c:url value='/commonyui/build/yahoo/yahoo.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/build/dom/dom.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/build/autocomplete/autocomplete-debug.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/build/event/event-debug.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/build/animation/animation.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonjs/ajaxCommonFunctions.js'/>"></script>
	
	<SCRIPT type="text/javascript" src="<c:url value='/script/jsCommonMethods.js' />"></SCRIPT>
	
        <decorator:head/>
    </head>
    
<body <decorator:getProperty property="body.id" writeEntireProperty="yes"/><decorator:getProperty property="body.class" writeEntireProperty="true"/> <decorator:getProperty property="body.onload" writeEntireProperty="true"/>  >

<br/><br/>
  
  
 <table align='center' id="table2" >
  <tr>
  <td>
  <DIV id=main>
  <DIV id=m2>
  <DIV id=m3>
  <div align="center">
  <center>

  
	<decorator:body/>

	</center>
	</div></div></div></div>
	</td>
	</tr>
	</table>


	</body>
</html>

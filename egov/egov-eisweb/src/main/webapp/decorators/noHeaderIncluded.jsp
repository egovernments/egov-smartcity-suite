<%@ include file="/includes/taglibs.jsp" %>
<html>
    <head>
        <title>eGov  - <decorator:title/> </title>
	<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/common/css/commonegov.css'/>" />
        <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/common/css/eispayroll.css'/>" />
	<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/common/css/displaytable.css'/>" />
      	<link rel="stylesheet" type="text/css" href="<c:url value='/commonyui/build/reset/reset.css'/>" />
	<link rel="stylesheet" type="text/css" href="<c:url value='/commonyui/build/fonts/fonts.css'/>" />
	<link href="../common/css/commonegov.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="<egovtags:url path='/commonyui/yui2.7/datatable/assets/skins/sam/datatable.css'/>" />
	<link rel="stylesheet" type="text/css" href="<egovtags:url path='/commonyui/yui2.7/assets/skins/sam/autocomplete.css'/>" />
    </head>
    <decorator:head/>
<body  <decorator:getProperty property="body.id" writeEntireProperty="yes"/><decorator:getProperty property="body.class" writeEntireProperty="true"/> <decorator:getProperty property="body.onload" writeEntireProperty="true"/>  >

<body id="Home">
<div class="topbar">
  <div style="margin-top:10px"><decorator:title/></div>
  
</div>
	<decorator:body/>

  <div class="urlwk">City Administration System Designed and Implemented by <a href="http://www.egovernments.org/">eGovernments Foundation</a> All Rights Reserved </div>
	</body>
</html>

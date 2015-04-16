<%@ include file="/includes/taglibs.jsp" %>
<html>
    <head>
    	<%@ include file="/includes/meta.jsp" %>
        <title>eGov  - <decorator:title/> </title>
        <link rel="stylesheet" type="text/css" media="all" href="/egi/css/breadcrumps.css" />
        <link rel="stylesheet" type="text/css" href="http://cdn.sencha.io/ext-4.0.7-gpl/resources/css/ext-all.css"/>
        <script type="text/javascript" charset="utf-8" src="http://cdn.sencha.io/ext-4.0.7-gpl/ext-all.js"></script>
        <decorator:head/>
    </head>
    
	<body <decorator:getProperty property="body.id" writeEntireProperty="yes"/><decorator:getProperty property="body.class" writeEntireProperty="true"/> <decorator:getProperty property="body.onload" writeEntireProperty="true"/>  >
		<egovtags:breadcrumb/>
		<decorator:body/>
	</body>
</html>
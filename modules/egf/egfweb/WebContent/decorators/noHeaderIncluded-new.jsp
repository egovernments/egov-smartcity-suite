<%@ include file="/includes/taglibs.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>eGov Financials <decorator:title/></title>

		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta http-equiv="Pragma" content="no-cache"/>
		<link href="<c:url value='/cssnew/budget.css'/>" rel="stylesheet" type="text/css" />
		<link href="<c:url value='/cssnew/commonegovnew.css'/>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="/egi/commonyui/yui2.7/fonts/fonts-min.css"/>
		<link rel="stylesheet" type="text/css" href="/egi/commonyui/yui2.7/datatable/assets/skins/sam/datatable.css"/>
		<link href="${pageContext.request.contextPath}/cssnew/error.css" rel="stylesheet" type="text/css"></link>
		<script type="text/javascript" src="/egi/commonyui/yui2.7/yuiloader/yuiloader-min.js"></script>
		<script type="text/javascript" src="/egi/commonyui/yui2.7/yahoo-dom-event/yahoo-dom-event.js"></script>
		<script type="text/javascript" src="/egi/commonyui/yui2.7/element/element.js"></script>
		<script type="text/javascript" src="/egi/commonyui/yui2.7/connection/connection-min.js"></script>
		<script type="text/javascript" src="/egi/commonyui/yui2.7/datasource/datasource-min.js"></script>
		<script type="text/javascript" src="/egi/commonyui/yui2.7/datatable/datatable.js"></script>
		<script type="text/javascript" src="/egi/commonyui/build/autocomplete/autocomplete-debug.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/script/jsCommonMethods.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/calenderNew.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/helper.js"></script>
		<script type="text/javascript" src="<c:url value='/commonjs/ajaxCommonFunctions.js'/>"></script>
		<decorator:head/>
	</head>
	<body <decorator:getProperty property="body.id" writeEntireProperty="yes"/><decorator:getProperty property="body.class" writeEntireProperty="true"/> <decorator:getProperty property="body.onload" writeEntireProperty="true"/>  >
		<decorator:body/>
	</body>
</html>
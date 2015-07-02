<!--
	eGov suite of products aim to improve the internal efficiency,transparency, 
    accountability and the service delivery of the government  organizations.
 
    Copyright (C) <2015>  eGovernments Foundation
 
	The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org
 
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.
 
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
 
    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .
 
    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:
 
 	1) All versions of this program, verbatim or modified must carry this 
 	   Legal Notice.
 
 	2) Any misrepresentation of the origin of the material is prohibited. It 
 	   is required that all modified versions of this material be marked in 
 	   reasonable ways as different from the original version.
 
 	3) This license does not grant any rights to any user of the program 
 	   with regards to rights under trademark law for use of the trade names 
 	   or trademarks of eGovernments Foundation.
 
   	In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
-->

<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<html>
    <head>
        <%@ include file="/includes/meta.jsp" %>

        <title>eGov  - <decorator:title/> </title>

   
    <link rel="icon" href="<c:url value='/resources/global/images/favicon.png" sizes="32x32' context='/egi'/>">
	<link href="<c:url value='/css/commonegovNew.css' context='/egi'/>" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/collections.css"/>
	<link rel="stylesheet" type="text/css" href="/egi/commonyui/yui2.8/fonts/fonts-min.css"/>
	<link rel="stylesheet" type="text/css" href="/egi/commonyui/yui2.8/datatable/assets/skins/sam/datatable.css"/>
	
	<link href="<c:url value='/resources/global/css/bootstrap/bootstrap.css' context='/egi'/>" rel="stylesheet" type="text/css" />
	<link href="<c:url value='/resources/global/css/egov/custom.css' context='/egi'/>" rel="stylesheet" type="text/css" />
	<link href="<c:url value='/resources/global/css/egov/header-custom.css' context='/egi'/>" rel="stylesheet" type="text/css" />
		
	<script type="text/javascript" src="<c:url value='/resources/global/js/jquery/jquery.js' context='/egi'/>"> </script>
	
	<SCRIPT type="text/javascript" src="/collection/resources/js/jsCommonMethods.js"></SCRIPT>
	<script type="text/javascript" src="/egi/commonyui/yui2.8/yuiloader/yuiloader-min.js"></script>
	<script type="text/javascript" src="/egi/commonyui/yui2.8/yahoo-dom-event/yahoo-dom-event.js"></script>
	<script type="text/javascript" src="/egi/commonyui/yui2.8/element/element-min.js"></script>
	<script type="text/javascript" src="/egi/commonyui/yui2.8/connection/connection-min.js"></script>
	<script type="text/javascript" src="/egi/commonyui/yui2.8/datasource/datasource-min.js"></script>
	<script type="text/javascript" src="/egi/commonyui/yui2.8/datatable/datatable-min.js"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/yui2.8/yahoo/yahoo-min.js' context='/egi'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/yui2.8/dom/dom-min.js' context='/egi'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/yui2.8/autocomplete/autocomplete-min.js' context='/egi'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/yui2.8/event/event-min.js' context='/egi'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/yui2.8/animation/animation-min.js' context='/egi'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonjs/ajaxCommonFunctions.js' context='/egi'/>"></script>
	<script type="text/javascript" src="<c:url value='/resources/js/helper.js' context='/collection'/>"></script>
	<script type="text/javascript" src="<c:url value='/resources/js/collections.js' context='/collection'/>"></script>
	<script type="text/javascript" src="/egi/javascript/jquery/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="<c:url value='/javascript/jquery/jquery-ui-1.8.22.custom.min.js' context='/egi'/>"></script>
	<link rel="stylesheet" type="text/css" href="<c:url value='/css/jquery/jquery-ui-1.8.22.custom.css' context='/egi'/>" />
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/ajax-script.js"></script>
    
    <decorator:head/>
    </head>
    
<body <decorator:getProperty property="body.id" writeEntireProperty="yes"/><decorator:getProperty property="body.class" writeEntireProperty="true"/> <decorator:getProperty property="body.onload" writeEntireProperty="true"/>  >
    <div class="page-container">
		    <!-- header -->
		    <egov:breadcrumb/>
		    
		    <!-- pagecontent -->
		    <div class="main-content">
		       <decorator:body/>
		    </div>
		    
		    <!-- footer -->
		    <footer class="main">
			    Powered by <a href="http://egovernments.org/" target="_blank">eGovernments Foundation</a>
			</footer>
	</div>
    </body>
</html>

#-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation 
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or 
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this 
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It 
# 	   is required that all modified versions of this material be marked in 
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program 
# 	   with regards to rights under trademark law for use of the trade names 
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------
<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<html>
    <head>
        <%@ include file="/includes/meta.jsp" %>
        <title>eGov  - <decorator:title/> </title>

        <!-- <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/css/egov.css'/>" />
      	<link rel="stylesheet" type="text/css" href="<c:url value='/commonyui/build/reset/reset.css'/>" />
	<link rel="stylesheet" type="text/css" href="<c:url value='/commonyui/build/fonts/fonts.css'/>" /> -->

	<link href="<c:url value='/css/propertytax.css'/>" rel="stylesheet" type="text/css" />
	<link href="<c:url value='/css/commonegov.css'/>" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="/egi/commonyui/yui2.7/fonts/fonts-min.css"/>
	<link rel="stylesheet" type="text/css" href="/egi/commonyui/yui2.7/datatable/assets/skins/sam/datatable.css"/>
	<link rel="icon" href="<c:url value="/image/egov.ico"/>"/>
	
	<script type="text/javascript" src="/egi/commonyui/yui2.7/yuiloader/yuiloader-min.js"></script>
	<script type="text/javascript" src="/egi/commonyui/yui2.7/yahoo-dom-event/yahoo-dom-event.js"></script>
	<script type="text/javascript" src="/egi/commonyui/yui2.7/element/element.js"></script>
	<script type="text/javascript" src="/egi/commonyui/yui2.7/connection/connection-min.js"></script>
	<script type="text/javascript" src="/egi/commonyui/yui2.7/datasource/datasource-min.js"></script>
	<script type="text/javascript" src="/egi/commonyui/yui2.7/datatable/datatable.js"></script>
	<script type="text/javascript" src="/egi/commonyui/build/autocomplete/autocomplete-debug.js"></script>
	
	<script type="text/javascript" src="<c:url value='/javascript/propertyTax.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/javascript/calender.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/javascript/helper.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/javascript/WorkFlow.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/script/calendar.js'/>" ></script>
	<script type="text/javascript" src="<c:url value='/dhtml.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/javascript/validations.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/javascript/SASvalidation.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/javascript/dateValidation.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/javascript/createProperty.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/javascript/json2.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/javascript/modifyProperty.js'/>"></script>
	<SCRIPT type="text/javascript" src="<c:url value='/script/jsCommonMethods.js' />"></SCRIPT>
	
	<script type="text/javascript" src="<c:url value='/commonyui/build/yahoo/yahoo.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/build/dom/dom.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/build/autocomplete/autocomplete-debug.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/build/event/event-debug.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/build/animation/animation.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonjs/ajaxCommonFunctions.js'/>"></script>
	
	<link rel="stylesheet" type="text/css" href="<c:url value='/css/jquery/jquery-ui-1.8.4.custom.css'/>" />
    <script type="text/javascript" src="<c:url value='/javascript/jquery/jquery-1.7.2.min.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/javascript/jquery/jquery-ui-1.8.22.custom.min.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/javascript/jquery/ajax-script.js'/>"></script>
    
    <decorator:head/>
    </head>
    
<body <decorator:getProperty property="body.id" writeEntireProperty="yes"/><decorator:getProperty property="body.class" writeEntireProperty="true"/> <decorator:getProperty property="body.onload" writeEntireProperty="true"/>  >
    <div id="BreadCrumb">
    <egov:breadcrumb/>
    </div>
    <decorator:body/>
    </body>
</html>

<!-- -------------------------------------------------------------------------------
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
#------------------------------------------------------------------------------- -->
<%@ include file="/includes/taglibs.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml" oncontextmenu="return false">
<head>
 <%@ include file="/includes/meta.jsp" %> 
<title>eGov Works <decorator:title/></title>

<link href="<c:url value='/css/works.css'/>" rel="stylesheet" type="text/css" />
<link href="<c:url value='/css/commonegov.css' context='/egi'/>" rel="stylesheet" type="text/css" />

<link rel="stylesheet" type="text/css" href="/egi/commonyui/yui2.8/fonts/fonts-min.css"/>
<link rel="stylesheet" type="text/css" href="/egi/commonyui/yui2.8/datatable/assets/skins/sam/datatable.css"/>
<link rel="stylesheet" type="text/css" href="/egi/commonyui/yui2.8/datatable/assets/skins/sam/skin.css"/>
<link rel="stylesheet" type="text/css" href="/egi/commonyui/yui2.8/datatable/assets/skins/sam/button.css"/>
<link rel="stylesheet" type="text/css" href="/egi/commonyui/yui2.8/datatable/assets/skins/sam/button.css"/>

<script type="text/javascript" src="/egi/commonyui/yui2.8/autocomplete/autocomplete-min.js" ></script>
<script type="text/javascript" src="/egi/commonyui/yui2.8/yahoo-dom-event/yahoo-dom-event.js"></script> 
<script type="text/javascript" src="/egi/commonyui/yui2.8/dragdrop/dragdrop-min.js"></script>
<script type="text/javascript" src="/egi/commonyui/yui2.8/element/element-min.js"></script>
<script type="text/javascript" src="/egi/commonyui/yui2.8/connection/connection-min.js"></script>
<script type="text/javascript" src="/egi/commonyui/yui2.8/datasource/datasource-min.js"></script>
<script type="text/javascript" src="/egi/commonyui/yui2.8/datatable/datatable-min.js"></script>
<script type="text/javascript" src="/egi/commonyui/yui2.8/animation/animation-min.js"></script>
<script type="text/javascript" src="/egi/commonyui/yui2.8/container/container_core-min.js"></script>
<script type="text/javascript" src="/egi/commonyui/yui2.8/menu/menu-min.js"></script>
<script type="text/javascript" src="/egi/commonyui/yui2.8/button/button-min.js"></script>
<script type="text/javascript" src="/egi/commonyui/yui2.8/editor/editor-min.js"></script>

<script type="text/javascript" src="<egov:url path='/js/helper.js'/>"></script>
<script type="text/javascript" src="/egi/script/calendar.js"></script>
<script type="text/javascript" src="/egi/javascript/calender.js"></script>
<script type="text/javascript" src="/egi/script/jsCommonMethods.js"></script>
<script type="text/javascript" src="/egi/commonjs/ajaxCommonFunctions.js"></script>
<script type="text/javascript" src="/egi/javascript/validations.js"></script>
<script type="text/javascript" src="/egi/jsutils/prototype/prototype.js"></script>

<script type="text/javascript" src="/egworks/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="/egworks/js/jquery-ui-1.8.22.custom.min.js"></script>  
<link rel="stylesheet" type="text/css" href="/egworks/css/jquery-ui/css/ui-lightness/jquery-ui-1.8.4.custom.css" />
<script type="text/javascript" src="/egworks/js/ajax-script.js"></script>

<script type="text/javascript" >
window.document.onkeydown = function(event) { 
   	 switch (event.keyCode) { 
        case 116 : //F5 button
            event.returnValue = false;
            event.keyCode = 0;
            return false; 
        case 82 : //R button
            if (event.ctrlKey) { //Ctrl button
                event.returnValue = false; 
                event.keyCode = 0;  
                return false; 
            } 
    }
}
</script>
 <decorator:head/>
</head>
<body <decorator:getProperty property="body.id" writeEntireProperty="yes"/><decorator:getProperty property="body.class" writeEntireProperty="true"/> <decorator:getProperty property="body.onload" writeEntireProperty="true"/>  >
	    <div id="loadingMask" style="display:none;overflow:none;scroll:none;" ><img src="/egi/resources/erp2/images/bar_loader.gif"> <span id="message">Please wait....</span></div>
	    <div id="BreadCrumb">
	    	<egov:breadcrumb/>
	    </div>
	    <div class="topbar">
			<div style="margin-top:10px"><decorator:title/> </div>
		</div>
	    <decorator:body/>
	    <div class="urlwk"><div align>Works Management System Designed and Implemented by <a href="http://www.egovernments.org/">eGovernments Foundation</a> All Rights Reserved </div></div>
    </body>
</html>

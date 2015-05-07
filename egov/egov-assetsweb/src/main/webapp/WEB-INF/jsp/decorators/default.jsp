<!-- #-------------------------------------------------------------------------------
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
#-------------------------------------------------------------------------------  -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" oncontextmenu="return false">
<head>
<title>eGov Assets <decorator:title/></title>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="Pragma" content="no-cache"/>
<link href="<egov:url path='/css/assetmanagement.css'/>" rel="stylesheet" type="text/css" />
<link href="<egov:url path='/css/commonegov.css'/>" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="<egov:url prefix='/egi'  path='/commonyui/yui2.7/fonts/fonts-min.css'/>" />
<link rel="stylesheet" type="text/css" href="<egov:url prefix='/egi'  path='/commonyui/yui2.7/datatable/assets/skins/sam/datatable.css'/>" />

<link rel="stylesheet" type="text/css" href="<egov:url prefix='/egi'  path='/commonyui/yui2.7/assets/skins/sam/autocomplete.css'/>" />

<script type="text/javascript" src="<egov:url prefix='/egi'  path='/commonyui/yui2.7/yahoo-dom-event/yahoo-dom-event.js'/>"></script>
<script type="text/javascript" src="<egov:url prefix='/egi'  path='/commonyui/yui2.7/dragdrop/dragdrop.js'/>"></script>
<script type="text/javascript" src="<egov:url prefix='/egi'  path='/commonyui/yui2.7/element/element.js'/>"></script>
<script type="text/javascript" src="<egov:url prefix='/egi'  path='/commonyui/yui2.7/connection/connection-min.js'/>"></script>
<script type="text/javascript" src="<egov:url prefix='/egi'  path='/commonyui/yui2.7/datasource/datasource-min.js'/>"></script>

<script type="text/javascript" src="<egov:url prefix='/egi'  path='/commonyui/yui2.7/autocomplete/autocomplete-min.js'/>"></script>
<script type="text/javascript" src="<egov:url prefix='/egi'  path='/commonyui/yui2.7/datatable/datatable.js'/>"></script>

<script type="text/javascript" src="<egov:url prefix='/egi'  path='/commonyui/yui2.7/animation/animation-min.js'/>"></script>
<script type="text/javascript" src="<egov:url path='/js/helper.js'/>"></script>
<script type="text/javascript" src="<egov:url path='/js/prototype.js'/>"></script>

<script type="text/javascript" src="/egi/script/calendar.js"></script>
<script type="text/javascript" src="/egi/javascript/calender.js"></script>
<script type="text/javascript" src="/egi/script/jsCommonMethods.js"></script>
<script type="text/javascript" src="/egi/commonjs/ajaxCommonFunctions.js"></script>
<script type="text/javascript" src="/egi/javascript/validations.js"></script>

<script type="text/javascript" src="/egassets/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="/egassets/js/jquery-ui-1.8.22.custom.min.js"></script>  
<link rel="stylesheet" type="text/css" href="/egassets/css/jquery-ui/css/ui-lightness/jquery-ui-1.8.4.custom.css" />
<script type="text/javascript" src="/egassets/js/ajax-script.js"></script>

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

</head>
<body id="Home">
<div id="loadingMask" style="display:none;overflow:none;scroll:none;" ><img src="/egi/images/bar_loader.gif"> <span id="message">Please wait....</span></div>
	<div class="topbar">
		<div style="margin-top:10px">eGov Assets <decorator:title/> </div>
	</div>
<div class="navibarwk">
<div class="piconwk">
<!--  a href="#"><img src="<egov:url path='/image/help.png'/>" alt="Print" width="16" height="16" border="0" /></a -->
</div>
</div>
<div class="navibarshadowwk"></div>
<decorator:body/>
<div class="urlwk"><div align>City Administration System Designed and Implemented by <a href="http://www.egovernments.org/">eGovernments Foundation</a> All Rights Reserved </div></div>
</body>
</html>

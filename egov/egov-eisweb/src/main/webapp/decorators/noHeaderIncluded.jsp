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
#------------------------------------------------------------------------------- -->
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

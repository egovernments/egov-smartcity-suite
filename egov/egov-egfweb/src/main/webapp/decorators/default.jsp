<!--  #-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#      accountability and the service delivery of the government  organizations.
#   
#       Copyright (C) <2015>  eGovernments Foundation
#   
#       The updated version of eGov suite of products as by eGovernments Foundation 
#       is available at http://www.egovernments.org
#   
#       This program is free software: you can redistribute it and/or modify
#       it under the terms of the GNU General Public License as published by
#       the Free Software Foundation, either version 3 of the License, or
#       any later version.
#   
#       This program is distributed in the hope that it will be useful,
#       but WITHOUT ANY WARRANTY; without even the implied warranty of
#       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#       GNU General Public License for more details.
#   
#       You should have received a copy of the GNU General Public License
#       along with this program. If not, see http://www.gnu.org/licenses/ or 
#       http://www.gnu.org/licenses/gpl.html .
#   
#       In addition to the terms of the GPL license to be adhered to in using this
#       program, the following additional terms are to be complied with:
#   
#   	1) All versions of this program, verbatim or modified must carry this 
#   	   Legal Notice.
#   
#   	2) Any misrepresentation of the origin of the material is prohibited. It 
#   	   is required that all modified versions of this material be marked in 
#   	   reasonable ways as different from the original version.
#   
#   	3) This license does not grant any rights to any user of the program 
#   	   with regards to rights under trademark law for use of the trade names 
#   	   or trademarks of eGovernments Foundation.
#   
#     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------  -->
<%@ include file="/includes/taglibs.jsp" %>
<html>
    <head>
        <%@ include file="/includes/meta.jsp" %>
        <title>eGov  - <decorator:title/> </title>
        <link rel="stylesheet" type="text/css" href="<c:url value='/cssnew/index.css'/>"></script>
        <link rel="stylesheet" type="text/css" href="<c:url value='/cssnew/commonegovnew.css'/>"></script>
      	<link rel="stylesheet" type="text/css" href="<c:url value='/commonyui/b.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/script/calendar.juild/reset/reset.css'/>" />
	<link rel="stylesheet" type="text/css" href="<c:url value='/commonyui/build/fonts/fonts.css'/>" />

        <script type="text/javascript" src="<c:url value='/javascript/calenders'/>" ></script>
	<script type="text/javascript" src="<c:url value='/javascript/validations.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/javascript/SASvalidation.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/javascript/dateValidation.js'/>"></script>
	
	<script type="text/javascript" src="<c:url value='/commonyui/build/yahoo/yahoo.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/build/dom/dom.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/build/autocomplete/autocomplete-debug.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/build/event/event-debug.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/build/animation/animation.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonjs/ajaxCommonFunctions.js'/>"></script>
	<SCRIPT type="text/javascript" src="<c:url value='/script/jsCommonMethods.js' />"></SCRIPT>
	<script type="text/javascript" src="/EGF/javascript/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="/EGF/javascript/jquery-ui-1.8.22.custom.min.js"></script>  
	<script type="text/javascript" src="/EGF/javascript/ajax-script.js"></script>
	<link rel="stylesheet" type="text/css" href="/EGF/cssnew/jquery-ui/css/ui-lightness/jquery-ui-1.8.4.custom.css" />

        <decorator:head/>

<body <decorator:getProperty property="body.id" writeEntireProperty="yes"/><decorator:getProperty property="body.class" writeEntireProperty="true"/> <decorator:getProperty property="body.onload" writeEntireProperty="true"/>  >
<div id="loadingMask" style="display:none"><p align="center"><img src="../images/bar_loader.gif"> <span id="message"><p style="color: red">Please wait....</p></span></p></div>

  <!-- Header Section Begins -->
     <div class="topbar">
     	<div class="egov"><img src="image/eGov.png" alt="eGov" width="54" height="58" /></div>
     	<div class="gov"><img src="image/Chennai_logo.jpg" alt="CHENNAICMC" width="54" height="58" /></div>
 		<div class="mainheading">City Administration  </div>
 	</div>
 	<div class="navibar">
 		<div align="right">
   			<table width="100%" border="0" cellspacing="0" cellpadding="0">
 				<tr>
            		<td><div align="left"><ul id="tabmenu" class="tabmenu">
                		<li ><a id="loginhref" name="loginhref" href="#" onclick="javascript:top.location='/egi/logout.do'">Logout</a></li>
        				</ul></div>
        			</td>
 				</tr>
 			</table>
 		</div>
	</div>

  <!-- Header Section Ends -->
 <table align='center' id="table2" width="100%">
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
 

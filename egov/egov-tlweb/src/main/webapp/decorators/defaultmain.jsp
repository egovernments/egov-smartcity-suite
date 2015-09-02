#-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#     accountability and the service delivery of the government  organizations.
#  
#      Copyright (C) <2015>  eGovernments Foundation
#  
#      The updated version of eGov suite of products as by eGovernments Foundation 
#      is available at http://www.egovernments.org
#  
#      This program is free software: you can redistribute it and/or modify
#      it under the terms of the GNU General Public License as published by
#      the Free Software Foundation, either version 3 of the License, or
#      any later version.
#  
#      This program is distributed in the hope that it will be useful,
#      but WITHOUT ANY WARRANTY; without even the implied warranty of
#      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#      GNU General Public License for more details.
#  
#      You should have received a copy of the GNU General Public License
#      along with this program. If not, see http://www.gnu.org/licenses/ or 
#      http://www.gnu.org/licenses/gpl.html .
#  
#      In addition to the terms of the GPL license to be adhered to in using this
#      program, the following additional terms are to be complied with:
#  
#  	1) All versions of this program, verbatim or modified must carry this 
#  	   Legal Notice.
#  
#  	2) Any misrepresentation of the origin of the material is prohibited. It 
#  	   is required that all modified versions of this material be marked in 
#  	   reasonable ways as different from the original version.
#  
#  	3) This license does not grant any rights to any user of the program 
#  	   with regards to rights under trademark law for use of the trade names 
#  	   or trademarks of eGovernments Foundation.
#  
#    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------
<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<html>
    <head>
        <%@ include file="/includes/meta.jsp" %>
        <title>eGov - <decorator:title/> </title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<SCRIPT type="text/javascript" src="/egi/script/jsCommonMethods.js"></SCRIPT>
	   
		<!--add css starts-->
	    	<link href="<egov:url path='/css/professionaltax.css'/>" rel="stylesheet" type="text/css" />
	    	<link href="/egi/css/commonegov.css" rel="stylesheet" type="text/css" />    
	    	<script type="text/javascript" src="<egov:url path='/yui/yahoo-dom-event/yahoo-dom-event.js'/>"></script>
			    	<script type="text/javascript" src="<egov:url path='/yui/calendar/calendar-min.js'/>"></script> 
	    	<script type="text/javascript" src="<egov:url path='/yui/yahoo/yahoo.js'/>"></script>
	    	
		<script type="text/javascript" src="/egi/commonjs/ajaxCommonFunctions.js"></script>  
		<script type="text/javascript" src="/egi/javascript/calender.js"></script>
		<script type="text/javascript" src="/egi/script/calendar.js" ></script>
		<script type="text/javascript" src="/egi/javascript/validations.js"></script>
		<SCRIPT type="text/javascript" src="/egi/script/jsCommonMethods.js"></SCRIPT>
		
		<script type="text/javascript" src="<egov:url path='/js/prototype.js'/>"></script>
   		<script type="text/javascript" src="<egov:url path='/js/scriptaculous.js?load=effects'/>"></script>
   		<script type="text/javascript" src="<c:url value='/js/modalbox.js'/>"></script>
   		<link rel="stylesheet" href="<egov:url path='/js/modalbox.css'/>" type="text/css" media="screen" />
		<script type="text/javascript" src="<egov:url path='/js/pt.js'/>"></script> 
		<script type="text/javascript" src="<egov:url path='/js/helper.js'/>"></script>
        <decorator:head/>
    </head>
    
<body <decorator:getProperty property="body.id" writeEntireProperty="yes"/><decorator:getProperty property="body.class" writeEntireProperty="true"/> <decorator:getProperty property="body.onload" writeEntireProperty="true"/>  >


<!-- Header section begins -->
 <div class="topbar"><div class="egov"><img src="images/eGov.png" alt="eGov" width="54" height="58" /></div><div class="gov"><img src="images/india.png" alt="India" width="54" height="58" /></div>
 <div class="mainheading">Corporation of Chennai </div>
</div>
  
  
<!-- Header section ends -->

  
	<decorator:body/>

	


	</body>
</html>

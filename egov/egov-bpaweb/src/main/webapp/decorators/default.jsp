<!-- /*
#   eGov suite of products aim to improve the internal efficiency,transparency, 
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
# */ -->
<%@ include file="/includes/taglibs.jsp" %>
   <%@ include file="/includes/meta.jsp" %>
<%@page import="org.egov.lib.admbndry.BoundaryDAO,
		org.egov.lib.rrbac.services.RbacManager,
  		org.egov.lib.rrbac.services.RbacManagerHome,
  		org.egov.infstr.utils.ServiceLocator,
		org.egov.lib.rrbac.model.Action" %>
<html > 
    <head> 
      
        <title>eGov BPA - <decorator:title/> </title>
		<link rel="stylesheet" type="text/css" href="<c:url value='/common/css/bpa.css'/>" />
        <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/common/css/commonegov.css'/>" />
      	      	  
      	<link rel="stylesheet" type="text/css" href="<c:url value='/commonyui/yui2.7/reset/reset.css'/>" />
		<link rel="stylesheet" type="text/css" href="<c:url value='/commonyui/yui2.7/assets/skins/sam/autocomplete.css'/>" />
		
	<link rel="stylesheet" type="text/css" href="<c:url value='/common/css/jquery/jquery-ui-1.8.22.custom.css'/>" />

	<script type="text/javascript" src="<c:url value='/commonyui/yui2.7/yahoo/yahoo.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/yui2.7/dom/dom.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/yui2.7/event/event-debug.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/yui2.7/connection/connection-min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/yui2.7/animation/animation.js'/>"></script>
	<!-- <script type="text/javascript" src="<c:url value='/commonyui/yui2.7/datasource/datasource-beta.js' />"></script>  -->
	<script type="text/javascript" src="<c:url value='/commonyui/yui2.7/json/json-min.js' />"></script> 
	<script type="text/javascript" src="<c:url value='/commonyui/yui2.7/yahoo-dom-event/yahoo-dom-event.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/yui2.7/dragdrop/dragdrop.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/yui2.7/element/element.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/yui2.7/connection/connection-min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/yui2.7/datasource/datasource-min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/yui2.7/autocomplete/autocomplete-min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/yui2.7/datatable/datatable.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/yui2.7/animation/animation-min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/yui2.7/element/element-min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/yui2.7/container/container_core-min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/yui2.7/menu/menu-min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/yui2.7/button/button-min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/commonyui/yui2.7/editor/editor-min.js'/>"></script>
	
	 <script type="text/javascript" src="<c:url value='/javascript/calender.js'/>"></script>
    

	<script type="text/javascript" src="<c:url value='/commonjs/ajaxCommonFunctions.js'/>"></script>
	<SCRIPT type="text/javascript" src="<c:url value='/script/jsCommonMethods.js' />"></SCRIPT>
	<script type="text/javascript" src="/egi/script/calendar.js" ></script>
	
	<script type="text/javascript" src="/egi/jsutils/prototype/prototype.js"></script>
<script type="text/javascript"  src="<c:url value='/common/js/helper.js'/>"></script>
   	<script type="text/javascript" src="<c:url value='/common/js/jquery/jquery-1.7.2.min.js'/>"></script>
   	<script type="text/javascript" src="<c:url value='/common/js/jquery/jquery-ui-1.8.22.custom.min.js'/>"></script>
<script type="text/javascript"  src="<c:url value='/common/js/bpa.js'/>"></script>	
  <decorator:head/>
  
  <script type="text/javascript" >

</script>
  
    </head>

 <%
 String cityName="india";
if(session.getAttribute("cityName")!=null)
	cityName=(String)session.getAttribute("cityName");
else
{
	if(session.getAttribute("org.egov.topBndryID")!=null)
		{
		BoundaryDAO boundaryDao= new BoundaryDAO();
		cityName=boundaryDao.getBoundaryNameForID(new Integer((String)session.getAttribute("org.egov.topBndryID")));
		cityName=cityName.toUpperCase();
		session.setAttribute("cityName",cityName);
		}
				

}	    

//Here we get the help url associated with this url

ServiceLocator serviceloc = ServiceLocator.getInstance();
RbacManagerHome rbacMangrHome=null;
RbacManager rbacManager=null;
rbacMangrHome=(RbacManagerHome)serviceloc.getLocalHome("RbacManagerHome");
rbacManager=rbacMangrHome.create();
Integer  actId=null;
String helpUrl=null;
Action actobj=null;
String actionId=null;
actionId =(String)request.getParameter("actionid");
	
if((actionId != null && !actionId.equals("")))
{

  actId=Integer.valueOf(actionId);
 
 actobj= rbacManager.getActionById(actId);
 helpUrl=actobj.getHelpURL();
}
else
{  
	String queryStr=request.getQueryString();
	String  url=request.getRequestURI().substring(request.getContextPath().length());
	url=url+"?"+queryStr;
	   
	actobj= rbacManager.getActionByURL(url);
	if(actobj!=null)
		helpUrl=actobj.getHelpURL();
}


%>
<body <decorator:getProperty property="body.id" writeEntireProperty="yes"/><decorator:getProperty property="body.class" writeEntireProperty="true"/> <decorator:getProperty property="body.onload" writeEntireProperty="true"/>  >


<egovtags:breadcrumb/>
  <div class="headlargenew"><decorator:title/></div>
  <div id="loadingMask" style="display:none"><p align="center"><img src="../common/image/bar_loader.gif"> <span id="message"><p style="color: red">Please wait....</p></span></p></div>
	<decorator:body/>
</body>

</html>

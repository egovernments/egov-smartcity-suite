<!--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  -->
<%@ include file="/includes/taglibs.jsp" %>
<%@ page import=" org.egov.lib.rrbac.services.RbacManager,
		org.egov.lib.rrbac.services.RbacManagerHome,
		org.egov.infstr.utils.ServiceLocator,
		org.egov.infstr.utils.HibernateUtil,
		org.hibernate.LockMode,
		org.egov.lib.rrbac.model.Action,
		org.apache.log4j.Logger"
%>

<%
	Logger LOGGER = Logger.getLogger("TitleandHelp.jsp");
	try{
	ServiceLocator serviceloc = ServiceLocator.getInstance();
	RbacManagerHome rbacMangrHome=null;
	RbacManager rbacManager=null;
	rbacMangrHome=(RbacManagerHome)serviceloc.getLocalHome("RbacManagerHome");
	rbacManager=rbacMangrHome.create();
	String actionId=null;
	Action actobj=null;
	actionId=(String)request.getParameter("actionid");
	if(actionId!=null && actionId.equalsIgnoreCase("null"))
	{
		actionId=null;
	}
	
	
	LOGGER.info("Action id "+actionId);
	LOGGER.info("The Context path"+request.getContextPath());
	LOGGER.info("The Context UrI"+request.getRequestURI());
	StringBuffer url=request.getRequestURL();
	LOGGER.info("Url()))))))))))))))))))))))))))))))))))))))()))) ="+url);
	String URI=request.getRequestURI();
	int start=request.getContextPath().length();
	String queryStr=request.getQueryString();
	LOGGER.info("queryParams"+queryStr);
	String	Url=URI.substring(start);
	Url=Url+"?"+queryStr;
   
	String helpUrl=null;
	String mode=null;
	String displayName=null;
	String name=null;
	String submit=null;
	String modeText=null;//referes showMode or mode
	String showMode=null;
	showMode=request.getParameter("showMode");
	mode=request.getParameter("mode");
	submit=request.getParameter("submitType");
	String modeValue=null;
	
		String How=null;
		if(actionId==null)
		{
			actobj= rbacManager.getActionByURL(Url);
			if(actobj!=null)
			{
				//HibernateUtil.getCurrentSession().lock(actobj,LockMode.NONE); 
				helpUrl=actobj.getHelpURL(); 
				displayName=actobj.getDisplayName();
				name=actobj.getName();
				//System.out.println("The Action ID"+Integer.parseInt(actionId));
				LOGGER.info("The displayName"+displayName);
				LOGGER.info("Action id  is from Url");
				How="Url";
			}
			else
			{
				actionId=(String)session.getAttribute("actionid");
				if(actionId!=null)
				{
					String parentPage=(String)session.getAttribute("parentPage");
					LOGGER.info(":::::::::::::::::::::::::::: parentPage"+parentPage);
					LOGGER.info(":::::::::::::::::::::::::::: URI"+URI);
					if(parentPage.equalsIgnoreCase(URI))
					{
						actobj= rbacManager.getActionById(new Integer(Integer.parseInt(actionId)));
						//HibernateUtil.getCurrentSession().lock(actobj,LockMode.NONE); 
						helpUrl=actobj.getHelpURL(); 
						displayName=actobj.getDisplayName();
						name=actobj.getName();
						LOGGER.info("The Action ID"+Integer.parseInt(actionId));
						LOGGER.info("The displayName"+displayName);
						LOGGER.info("Action id  is from Session");
						How="Session";
					}//2 if
				}// 1 if
			}// 1 else
		}//main if
		else
		{
			LOGGER.info("The Action ID"+Integer.parseInt(actionId));
			actobj= rbacManager.getActionById(new Integer(Integer.parseInt(actionId)));
			if(actobj!=null)
			{
				session.setAttribute("actionid",actionId);
				session.setAttribute("parentPage",URI); //Important to avoid missuse of action id stored in session
			//	HibernateUtil.getCurrentSession().lock(actobj,LockMode.NONE); 
				helpUrl=actobj.getHelpURL(); 
				displayName=actobj.getDisplayName();
				name=actobj.getName();
				LOGGER.info("The displayName"+displayName);
				LOGGER.info("Action id  is from MenuTree");
				How="MenuTree";
			}
		}

	


%>

<html oncontextmenu="return false">
    <head>
        <%@ include file="/includes/meta.jsp" %>
		<title>eGov  -<%=name%></title>
		<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/css/egov.css?rnd=${app_release_no}'/>" />
		<link rel="stylesheet" type="text/css" href="<c:url value='/commonyui/build/reset/reset.css?rnd=${app_release_no}'/>" />
		<link rel="stylesheet" type="text/css" href="<c:url value='/commonyui/build/fonts/fonts.css?rnd=${app_release_no}'/>" />
		<script type="text/javascript" src="<c:url value='/resources/javascript/calender.js?rnd=${app_release_no}'/>"></script>
		<script type="text/javascript" src="<c:url value='/resources/javascript/validations.js?rnd=${app_release_no}'/>"></script>
		<script type="text/javascript" src="<c:url value='/javascript/SASvalidation.js?rnd=${app_release_no}'/>"></script>
		<script type="text/javascript" src="<c:url value='/javascript/dateValidation.js?rnd=${app_release_no}'/>"></script>
		
		<script type="text/javascript" src="<c:url value='/commonyui/build/yahoo/yahoo.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/commonyui/build/dom/dom.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/commonyui/build/autocomplete/autocomplete-debug.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/commonyui/build/event/event-debug.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/commonyui/build/animation/animation.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/resources/javascript/ajaxCommonFunctions.js?rnd=${app_release_no}'/>"></script>
		
		<link rel="stylesheet" type="text/css" href="/egi/commonyui/yui2.7/datatable/assets/skins/sam/datatable.css"/>
<link rel="stylesheet" type="text/css" href="/egi/commonyui/yui2.7/assets/skins/sam/autocomplete.css" />
<script type="text/javascript" src="/egi/commonyui/yui2.7/animation/animation-min.js"></script>
<script type="text/javascript" src="/egi/commonyui/yui2.7/datasource/datasource-min.js"></script>
<script type="text/javascript" src="/egi/commonyui/yui2.7/autocomplete/autocomplete-min.js"></script>
<script type="text/javascript" src="/egi//commonyui/yui2.7/yahoo-dom-event/yahoo-dom-event.js"></script>
<script type="text/javascript" src="/egi/commonyui/yui2.7/calendar/calendar-min.js"></script>
<script type="text/javascript" src="/egi/commonyui/yui2.7/yahoo/yahoo.js"></script>
<script type="text/javascript" src="/egi/commonyui/yui2.7/dragdrop/dragdrop.js"></script>
<script type="text/javascript" src="/egi/commonyui/yui2.7/element/element.js"></script>
<script type="text/javascript" src="/egi/commonyui/yui2.7/connection/connection-min.js"></script>
<script type="text/javascript" src="/egi/commonyui/yui2.7/datatable/datatable.js"></script>

		<script type="text/javascript" src="<c:url value='/resources/javascript/helper.js?rnd=${app_release_no}'/>"></script>
		<SCRIPT type="text/javascript" src="<c:url value='/resources/javascript/jsCommonMethods.js?rnd=${app_release_no}' />"></SCRIPT>  
		<script type="text/javascript" src="<c:url value='/exility/PageManager.js?rnd=${app_release_no}'/>"></script>
		<script type="text/javascript" src="<c:url value='/exility/PageValidator.js?rnd=${app_release_no}'/>"></script>
		<script type="text/javascript" src="<c:url value='/exility/data.js?rnd=${app_release_no}'/>"></script>
		<script type="text/javascript" src="<c:url value='/exility/ExilityParameters.js?rnd=${app_release_no}'/>"></script>
		<script type="text/javascript" src="<c:url value='/resources/javascript/jsCommonMethods.js?rnd=${app_release_no}'/>"></script>
		<script type="text/javascript" src="<c:url value='/resources/javascript/predefinedAccCodes.js?rnd=${app_release_no}'/>"></script>
		<script type="text/javascript" src="<c:url value='/resources/javascript/rowDetailsNew.js?rnd=${app_release_no}'/>"></script>
		<script type="text/javascript" src="<c:url value='/exility/CookieManager.js?rnd=${app_release_no}'/>"></SCRIPT>
		<script type="text/javascript" src="<c:url value='/resources/javascript/helper.js?rnd=${app_release_no}'/>"></SCRIPT>
		<style type="text/css">
				#codescontainer {position:absolute;left:11em;width:40%}
				#codescontainer .yui-ac-content {position:absolute;width:100%;border:1px solid #404040;background:#fff;overflow:hidden;z-index:9050;}
				#codescontainer .yui-ac-shadow {position:absolute;margin:.3em;width:100%;background:#a0a0a0;z-index:9049;}
				#codescontainer ul {padding:5px 0;width:100%;}
				#codescontainer li {padding:0 5px;cursor:default;white-space:nowrap;}
				#codescontainer li.yui-ac-highlight {background:#ff0;}
				#codescontainer li.yui-ac-prehighlight {background:#FFFFCC;}
		</style> 
		<decorator:head/>
		<script>
              var url="<%=helpUrl%>";
        </script>
    </head>
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
<body <decorator:getProperty property="body.id" writeEntireProperty="yes"/><decorator:getProperty property="body.class" writeEntireProperty="true"/> <decorator:getProperty property="body.onload" writeEntireProperty="true"/>  >
  <table align='center' id="table2"><tr><td><DIV id=main><DIV id=m2><DIV id=m3>
<%
	try
	{
	if(helpUrl!=null)
	{        
%>	
	      <!--  <p align="right">
	      <a id="helphref" name="helphref" href="#" onclick="javascript:window.open('/EGF/<%=helpUrl%>')"><b><font face="Verdana">Help</font></b></a>
		  -->
			<font size=12px>
			<table width="100%" class="tableStyle">
			<tr >
			<td class="tableHeader" valign="center" colspan="3"><span ><%=name%></span></td>
			<td class="tableHeader" valign="center" align="right" >
				<a id="helphref" name="helphref" href="#" onclick="javascript:window.open('/EGF/<%=helpUrl%>')">
				<IMG height=20 src="/egi/resources/erp2/images/help.gif" width=30></a>
			</td>
			</tr>
			</table>
			</font>
			
	   	<%
	     	} 
	     	}
	     	catch(Exception e)
	     	{
	     		LOGGER.error(e.getMessage());
	     	}


}catch(Exception e)
{
LOGGER.error(e.getMessage());
}
	    %>    
  
		<decorator:body/>
		</center></DIV></DIV></DIV></td></tr></table>
	</body>
</html>
 





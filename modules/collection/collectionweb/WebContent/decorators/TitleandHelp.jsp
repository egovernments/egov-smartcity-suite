<%@page import="org.egov.lib.rrbac.services.RbacServiceImpl"%>
<%@page import="org.egov.lib.rrbac.services.RbacService"%>
<%@ include file="/includes/taglibs.jsp" %>
<%@ page import=" org.egov.lib.rrbac.services.RbacServiceImpl,
		org.egov.infstr.utils.HibernateUtil,
		 org.egov.lib.rrbac.model.Action;"
		 
%>

<%
	try{
	RbacServiceImpl rbacServiceImpl = new RbacServiceImpl();
	
	String actionId=null;
	Action actobj=null;
	actionId=(String)request.getParameter("actionid");
	
	StringBuffer url=request.getRequestURL();
	String URI=request.getRequestURI();
	int start=request.getContextPath().length();
	String queryStr=request.getQueryString();
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
			actobj= rbacServiceImpl.getActionByURL("collection",Url);
			if(actobj!=null)
			{
				helpUrl=actobj.getHelpURL(); 
				displayName=actobj.getDisplayName();
				name=actobj.getName();
				How="Url";
			}
			else
			{
				actionId=(String)session.getAttribute("actionid");
				if(actionId!=null)
				{
					String parentPage=(String)session.getAttribute("parentPage");
					if(parentPage.equalsIgnoreCase(URI))
					{
						actobj= rbacServiceImpl.getActionById(new Integer(Integer.parseInt(actionId)));
						helpUrl=actobj.getHelpURL(); 
						displayName=actobj.getDisplayName();
						name=actobj.getName();
						How="Session";
					}//2 if
				}// 1 if
			}// 1 else
		}//main if
		else
		{
			actobj= rbacServiceImpl.getActionById(new Integer(Integer.parseInt(actionId)));
			if(actobj!=null)
			{
				session.setAttribute("actionid",actionId);
				session.setAttribute("parentPage",URI);
				helpUrl=actobj.getHelpURL(); 
				displayName=actobj.getDisplayName();
				name=actobj.getName();
				How="MenuTree";
			}
		}

	


%>

<html>
    <head>
        <%@ include file="/includes/meta.jsp" %>
		<title>eGov  -<%=name%></title>
		<link rel="stylesheet" type="text/css" media="all" href="<c:url value='../../commons/css/egov.css'/>" />
		<link rel="stylesheet" type="text/css" href="<c:url value='/commonyui/build/reset/reset.css'/>" />
		<link rel="stylesheet" type="text/css" href="<c:url value='/commonyui/build/fonts/fonts.css'/>" />
		<script type="text/javascript" src="<c:url value='/javascript/calender.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/script/calendar.js'/>" ></script>
		<script type="text/javascript" src="<c:url value='/dhtml.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/javascript/validations.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/javascript/SASvalidation.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/javascript/dateValidation.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/commonyui/build/yahoo/yahoo.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/commonyui/build/dom/dom.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/commonyui/build/autocomplete/autocomplete-debug.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/commonyui/build/event/event-debug.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/commonyui/build/animation/animation.js'/>"></script>
		<script type="text/javascript" src="<c:url value='../../commons/commonjs/ajaxCommonFunctions.js'/>"></script>
		<SCRIPT type="text/javascript" src="<c:url value='../../commons/script/jsCommonMethods.js' />"></SCRIPT>  
		<script type="text/javascript" src="<c:url value='/exility/PageManager.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/exility/PageValidator.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/exility/data.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/exility/ExilityParameters.js'/>"></script>
		<script type="text/javascript" src="<c:url value='../../commons/script/jsCommonMethods.js'/>"></script>
		<script type="text/javascript" src="<c:url value='../../commons/script/predefinedAccCodes.js'/>"></script>
		<script type="text/javascript" src="<c:url value='../../commons/script/rowDetailsNew.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/exility/CookieManager.js'/>"></SCRIPT>
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
    
<body <decorator:getProperty property="body.id" writeEntireProperty="yes"/><decorator:getProperty property="body.class" writeEntireProperty="true"/> <decorator:getProperty property="body.onload" writeEntireProperty="true"/>  >
  <table align='center' id="table2"><tr><td><DIV id=main><DIV id=m2><DIV id=m3>
<%
	try
	{
	if(helpUrl!=null)
	{        
%>	
	    
			<font size=12px>
			<table width="100%" class="tableStyle">
			<tr >
			<td class="tableHeader" valign="center" colspan="3"><span ><%=name%></span></td>
			<td class="tableHeader" valign="center" align="right" >
				<a href='#' onclick=newWindow('<%=request.getContextPath()%>/HTML/AddAccountCode.htm') >
				<span class="link">Add Account Code</span></a>&nbsp;&nbsp;&nbsp;&nbsp;
				<span  align="centre"></span></a>
				<a id="helphref" name="helphref" href="#" onclick="javascript:window.open('/EGF/<%=helpUrl%>')">
				<IMG height=20 src="<%=request.getContextPath()%>/images/help.gif" width=30></a>
			</td>
			</tr>
			</table>
			</font>
			
	   	<%
	     	} 
	     	}
	     	catch(Exception e)
	     	{
	     	e.printStackTrace();
	     	}


}catch(Exception e)
{
e.printStackTrace();
}
	    %>    
  
		<decorator:body/>
		</center></DIV></DIV></DIV></td></tr></table>
	</body>
</html>
 





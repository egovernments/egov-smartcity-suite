<%@page import="org.egov.lib.rrbac.services.RbacServiceImpl"%>
<%@ include file="/includes/taglibs.jsp" %>
<%@ page import="org.egov.infstr.utils.HibernateUtil,
		org.hibernate.LockMode,
		 org.egov.lib.rrbac.model.Action;"
%>

<%	try{
	RbacServiceImpl rbacServiceImpl= new RbacServiceImpl();
	String actionId=null;
	Action actobj=null;
	actionId=(String)request.getParameter("actionid");
	
	System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&"+actionId);
	System.out.println("The Context path"+request.getContextPath());
	System.out.println("The Context UrI"+request.getRequestURI());
	StringBuffer url=request.getRequestURL();
	System.out.println("Url()))))))))))))))))))))))))))))))))))))))()))) ="+url);
	String URI=request.getRequestURI();
	int start=request.getContextPath().length();
	String queryStr=request.getQueryString();
	System.out.println("queryParams"+queryStr);
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
			actobj= rbacServiceImpl.getActionByURL(Url);
			if(actobj!=null)
			{
				//HibernateUtil.getCurrentSession().lock(actobj,LockMode.NONE); 
				helpUrl=actobj.getHelpURL(); 
				displayName=actobj.getDisplayName();
				name=actobj.getName();
				//System.out.println("The Action ID"+Integer.parseInt(actionId));
				System.out.println("The displayName"+displayName);
				System.out.println("hMmmmmmmmmmmmmmmmmmmmmmmmAction id  is from Url");
				How="Url";
			}
			else
			{
				actionId=(String)session.getAttribute("actionid");
				if(actionId!=null)
				{
					String parentPage=(String)session.getAttribute("parentPage");
					System.out.println(":::::::::::::::::::::::::::: parentPage"+parentPage);
					System.out.println(":::::::::::::::::::::::::::: URI"+URI);
					if(parentPage.equalsIgnoreCase(URI))
					{
						actobj= rbacServiceImpl.getActionById(new Integer(Integer.parseInt(actionId)));
						//HibernateUtil.getCurrentSession().lock(actobj,LockMode.NONE); 
						helpUrl=actobj.getHelpURL(); 
						displayName=actobj.getDisplayName();
						name=actobj.getName();
						System.out.println("The Action ID"+Integer.parseInt(actionId));
						System.out.println("The displayName"+displayName);
						System.out.println("hMmmmmmmmmmmmmmmmmmmmmmmmAction id  is from Session");
						How="Session";
					}//2 if
				}// 1 if
			}// 1 else
		}//main if
		else
		{
			System.out.println("The Action ID"+Integer.parseInt(actionId));
			actobj= rbacServiceImpl.getActionById(new Integer(Integer.parseInt(actionId)));
			if(actobj!=null)
			{
				session.setAttribute("actionid",actionId);
				session.setAttribute("parentPage",URI);
			//	HibernateUtil.getCurrentSession().lock(actobj,LockMode.NONE); 
				helpUrl=actobj.getHelpURL(); 
				displayName=actobj.getDisplayName();
				name=actobj.getName();
				System.out.println("The displayName"+displayName);
				System.out.println("hMmmmmmmmmmmmmmmmmmmmmmmmAction id  is from MenuTree");
				How="MenuTree";
			}
		}

	


%>

<html>
    <head>
        <%@ include file="/includes/meta.jsp" %>
		<title>eGov  -<%=name%> </title>
		<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/css/egov.css'/>" />
		<LINK rel="stylesheet" type="text/css" href="<c:url value='/css/ccMenu.css'/>"/>
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
		<script type="text/javascript" src="<c:url value='/commonjs/ajaxCommonFunctions.js'/>"></script>
		<SCRIPT type="text/javascript" src="<c:url value='/script/jsCommonMethods.js' />"></SCRIPT>  
		<script type="text/javascript" src="<c:url value='/exility/PageManager.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/exility/PageValidator.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/exility/data.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/exility/ExilityParameters.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/script/jsCommonMethods.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/script/predefinedAccCodes.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/script/rowDetailsNew.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/exility/CookieManager.js'/>"></SCRIPT>
		
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
			<td class="tableHeader" valign="center" colspan="3"><span ><%=displayName%></span></td>
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
 





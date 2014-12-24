<%@page import="org.egov.lib.rrbac.services.RbacServiceImpl"%>
<%@ include file="/includes/taglibs.jsp" %>
<%@ page import="org.egov.infstr.utils.HibernateUtil,
		org.hibernate.LockMode,
		 org.egov.lib.rrbac.model.Action;"
%>

<%
	try{
	RbacServiceImpl rbacServiceImpl= new RbacServiceImpl();
	String actionId=null;
	Action actobj=null;
	actionId=(String)request.getParameter("actionid");
	
	System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&"+actionId);
	System.out.println("The Context path"+request.getContextPath());
	System.out.println("The Context UrI"+request.getRequestURI());
	StringBuffer url=request.getRequestURL();
	String contextPath=request.getContextPath();
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
	/*	if(showMode!=null)
		{
			modeText="showMode";
			modeValue=showMode;
		}
		else if(mode!=null)
		{
			modeText="mode";
			modeValue=mode;
		}
		if(submit!=null && modeText!=null)
			Url=Url+"?submitType="+submit+"&"+modeText+"="+modeValue;
		else if(submit==null && modeText!=null)
			Url=Url+"?"+modeText+"="+modeValue;
		else if(submit==null && modeText==null)
			Url=Url;
		
		System.out.println("hMmmmmmmmmmmmmmmmmmmmmmmmAction id  is from Url"+Url); */
		String How=null;
		if(actionId==null)
		{
			actobj= rbacServiceImpl.getActionByURL(contextPath,Url);
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
		<title>eGov  -<%=name%>  </title>
		<decorator:head/>
		<script>
              var url="<%=helpUrl%>";
        </script>
    </head>
    
<body <decorator:getProperty property="body.id" writeEntireProperty="yes"/><decorator:getProperty property="body.class" writeEntireProperty="true"/> <decorator:getProperty property="body.onload" writeEntireProperty="true"/>  >
<%try
	{
	if(helpUrl!=null)
	{        
%>	
<div align=right><a id="helphref" name="helphref" href="#" onclick="javascript:window.open('/EGF/<%=helpUrl%>')">
Help</a>
</div>
<%     	} 
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

</body>
</html>
 





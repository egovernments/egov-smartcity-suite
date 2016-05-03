<%--
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
  --%>

#-------------------------------------------------------------------------------
# /**
#  * eGov suite of products aim to improve the internal efficiency,transparency, 
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
#  */
#-------------------------------------------------------------------------------
<%@ include file="/includes/taglibs.jsp" %>
<%@ page import=" org.egov.infra.admin.common.entity.Action,
		org.egov.infstr.utils.ServiceLocator,
		org.egov.lib.rrbac.services.RbacManager,
		org.egov.lib.rrbac.services.RbacManagerHome"
%>

<%
	try{
	ServiceLocator serviceloc = ServiceLocator.getInstance();
	RbacManagerHome rbacMangrHome=null;
	RbacManager rbacManager=null;
	rbacMangrHome=(RbacManagerHome)serviceloc.getLocalHome("RbacManagerHome");
	rbacManager=rbacMangrHome.create();
	String actionId=null;
	Action actobj=null;
	actionId=(String)request.getParameter("actionid");
	
	System.out.println("action id"+actionId);
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
			actobj= rbacManager.getActionByURL(Url);
			if(actobj!=null)
			{
				helpUrl=actobj.getHelpURL();
				displayName=actobj.getDisplayName();
				name=actobj.getName();
				//System.out.println("The Action ID"+Integer.parseInt(actionId));
				System.out.println("The displayName"+displayName);
				System.out.println("Action id  is from Url");
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
						actobj= rbacManager.getActionById(new Integer(Integer.parseInt(actionId)));
						helpUrl=actobj.getHelpURL();
						displayName=actobj.getDisplayName();
						name=actobj.getName();
						System.out.println("The Action ID"+Integer.parseInt(actionId));
						System.out.println("The displayName"+displayName);
						System.out.println("Action id  is from Session");
						How="Session";
					}//2 if
				}// 1 if
			}// 1 else
		}//main if
		else
		{
			System.out.println("The Action ID"+Integer.parseInt(actionId));
			actobj= rbacManager.getActionById(new Integer(Integer.parseInt(actionId)));
			if(actobj!=null)
			{
				session.setAttribute("actionid",actionId);
				session.setAttribute("parentPage",URI);
				helpUrl=actobj.getHelpURL();
				displayName=actobj.getDisplayName();
				name=actobj.getName();
				System.out.println("The displayName"+displayName);
				System.out.println("Action id  is from MenuTree");
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
 





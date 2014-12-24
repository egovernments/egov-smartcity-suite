<%@page import="org.egov.lib.rrbac.services.RbacServiceImpl"%>
<%@ include file="/includes/taglibs.jsp" %>
<%@ page import=" org.egov.lib.rrbac.services.RbacService,
  org.egov.infstr.utils.HibernateUtil,
   org.egov.lib.rrbac.model.Action;"
%>
<html>
    <head>
        <%@ include file="/includes/meta.jsp" %>
        <title>eGov Payroll - <decorator:title/> </title>

        <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/css/egov.css'/>" />
        <link rel="stylesheet" type="text/css" media="print" href="<c:url value='/css/print.css'/>" />
      	<link rel="stylesheet" type="text/css" href="<c:url value='/commonyui/build/reset/reset.css'/>" />
	<link rel="stylesheet" type="text/css" href="<c:url value='/commonyui/build/fonts/fonts.css'/>" />

        <script type="text/javascript" src="<c:url value='/javascript/calender.js'/>"></script>
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
	
	<SCRIPT type="text/javascript" src="../script/jsCommonMethods.js" type="text/javascript"></SCRIPT>
		
        <decorator:head/>
    </head>
<body<decorator:getProperty property="body.id" writeEntireProperty="true"/><decorator:getProperty property="body.class" writeEntireProperty="true"/> <decorator:getProperty property="body.onload" writeEntireProperty="true"/>  >

 
<%
		RbacServiceImpl rbacServiceImpl=new RbacServiceImpl();

		Integer  actId=null;
		String helpUrl=null;
		Action actobj=null;
		String actionId=null;
		actionId =(String)request.getParameter("actionid");
		 
		 String URI=request.getServletPath();
	    String queryStr=request.getQueryString();
       String  url=URI+"?"+queryStr;
	   System.out.println("URL----->>>>"+url);
	   
		
	if((actionId != null && !actionId.equals("")))
	{

		 actId=Integer.valueOf(actionId);
		 
		 actobj= rbacServiceImpl.getActionById(actId);
		 helpUrl=actobj.getHelpURL();
		System.out.println(">>>>>>>>>>>>Egov No  Header Help URL "+helpUrl);
	}
	else
	{
		actobj= rbacServiceImpl.getActionByURL(url,url);
		System.out.println("actobj--->>>"+actobj);
		if(actobj!=null)
			{
			
				helpUrl=actobj.getHelpURL();
			}
	}
 
  
  %>
   <!-- Body Begins -->
  

 <table align='center' id="table2" >
  <tr><td>
 <%
  
  if(helpUrl!=null)
  {       
  %> 
          <div class="helpStyle">
          <a id="helphref" name="helphref" href="#" onclick="javascript:window.open('<%=request.getContextPath() + helpUrl%>')"><b><font face="Verdana">Help</font></b></a>
          </div>
       <%
         }
         %>
</td></tr>
  <tr>
  <td>
  <DIV id=main>
  <DIV id=m2>
  <DIV id=m3>
  <div align="center">
  <center>


  
     
                <decorator:body/>
<table align="right" >
        <tr  >
      	   <td align="right" >
  		  <input type="button" class="closeButton" value="Close" onclick="window.close();"/> &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp 
  	   </td>
 	</tr>
    </table>
	   </center>
	   </div></div></div></div>
	   	</td>
	   	</tr>
	   	</table>
	 

	   </body>
</html>

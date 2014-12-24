<%@ include file="/includes/taglibs.jsp" %>
<%@ page import="org.apache.struts.action.ActionMessage,
				 org.apache.struts.action.ActionMessages" %>
<%@page isErrorPage="true"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>eGov EIS - ERROR</title>
        
      <link href="/egi/css/commonegov.css" rel="stylesheet" type="text/css" />
      <style>
      .hiddenError {
      	display:none;
       }
       .oopstext {
       	font-family:Verdana, Geneva, sans-serif;
       	font-size:14px;
       	font-weight:bold;
       	color:#F00;
       	margin-bottom:15px;
      }
</style>
    </head>
    <body>
	

     
       

<div class="formmainbox"><div class="insidecontent">
  <div class="errorroundbox2">
	<div class="errortop2"><div></div></div>
	  <div class="errorcontent2">
	    <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
            <td width="59%"><div class="logouttext">
              <img src="/egi/images/error.png" width="128" height="128" alt="Error" /><div class="oopstext">Oops!
                Sorry your request cannot be processed!</div>
                <span class="bold">An error has occurred. Please try again or contact system administrator if the problem persists.</span></div>
            	
		
		            <html:messages id="message" message="true">
		                <li><%= message %></li>
		            </html:messages>
  
            </td>
           </tr>
           <tr>
	    <td width="90%" class="hiddenError"> 
	         	    <% exception.printStackTrace(new java.io.PrintWriter(out)); %>

	    </td>  
  	   </tr>
        </table>
	  </div>
	  <div class="errorbot2"><div></div></div>
</div>
  </div>
</div>
<div class="buttonbottom">City Administration System Designed and Implemented by <a href="http://www.egovernments.org/">eGovernments Foundation</a> All Rights Reserved </div>
	
    </body>
</html>

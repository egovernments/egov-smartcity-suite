<!-- 
/***************************************************************************************
* @FileName	:	serverError.jsp
* @author	:	Rajalakshmi D.N.
* @description	: 	This page will be called if the server returns HTTP Status 403		
***************************************************************************************/
-->


<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="java.lang.*, java.util.*, org.egov.infstr.utils.HibernateUtil" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested" %>
<%@ include file="/staff/egovHeader.jsp" %>


<html>
<head>
<title>Error Page</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<title>WelCome to eGov ptisnn</title>
		<script language="JavaScript" src="/dhtml.js" type="text/JavaScript"></script>
		<LINK REL=stylesheet HREF="/egov.css" TYPE="text/css">
		<LINK REL=stylesheet HREF="/ccMenu.css" TYPE="text/css">

<script>

function funHome()
{
	//alert("HIII");
	
	<%try
	{
	 System.out.println("HELLO INSIDE TRY!!!");
	%>
	window.location="/staff/index.jsp";
	<%}
	catch(Exception e)
	{
		System.out.println("HELLO INSIDE CATCH!!!");
		e.printStackTrace();
		
	}
	finally
	{
		System.out.println("HELLO INSIDE FINALLY!!!");
		HibernateUtil.closeSession();
	}%>
}
</script>


</head>

<body >

<!-- Header Section Begins -->
<jsp:include page="/staff/tabmenu.html" />
<!-- Header Section Ends -->


<table align='center' id="signup">
   <tr>
      <td>         
         
         <!-- Body Begins -->
         <div id="main"><div id="m2"><div id="m3">
         <form name="signup" id="signup" method="post" action="#">
         <div align="center">
             <table align='center' width="600" height="83">
               
                 
                 <!-- ERROR MSG -->
                <tr>
                    <td height=50 class="labelcell2">
                       
                       <center>Sorry !!! You are not authorized to access this page !!!</center>
                    </td>
                </tr>


		
	    </table>
	</div>
	</form>
	</div></div></div>
      </td>
   </tr>
</table>
</body>
</html>

 
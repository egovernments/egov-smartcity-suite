<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="org.apache.struts.action.ActionMessage" %>

<%@ page import=" org.apache.struts.action.ActionMessages"%>
<html>
	<head>
		<title>eGov  - Error</title>
		<LINK rel="stylesheet" type="text/css" href="./css/egov.css">
	</head>
<body bgcolor="#FFFFFF">


<center>

 <form>
  <table align='center' id="table2" >
   <tr>
   <td>
  
   <!-- Body Begins -->
  
    <DIV id=paymain>
          <DIV id=paym2>
         <DIV id=m3>
   <div align="center">      
<center>

  <table  class="bordertable" summary style="border=1px solid;width=800px">
    <tbody>
      <tr>
        <td  align="middle" height="27" >
      	
	<html:errors/>
	
	
        </td>
      </tr>
      
      <tr>
        <TD align=middle width="99%" 
                   height=75>
                   <P align=center><font size="3">
           <b><font color="#000080" size="3"> Transaction Failed due to technical error...        
       
       </font></b> 
       </td>
      </tr>
      
	</tbody>
  </table>
</center>  
  </div></div></div></div>
</td></tr></table>

<form>
 <%@ include file = "../egovfooter.html" %>
</body>

</html>
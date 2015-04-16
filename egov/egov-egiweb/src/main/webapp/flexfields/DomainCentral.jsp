<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page errorPage="error.jsp" %>
<%@ page import="java.util.*,org.egov.infstr.utils.EgovMasterDataCaching" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%! 
ArrayList domainList;
%>
<%
domainList=(ArrayList)EgovMasterDataCaching.getInstance().get("egi-domainwtoref");   
%>

<c:set var="domainList" value="<%=domainList%>" scope="page" /> 

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">        
        <link rel="stylesheet" type="text/css" href="../css/egov.css">
	 <LINK rel="stylesheet" type="text/css" href="../css/ccMenu.css">
        <title>Domain</title>
    </head>
    <body>
    
    <SCRIPT type="text/javascript" src="../commonjs/ajaxCommonFunctions.js"></Script>
	
  
    <script type="text/javascript">
    
	/*
	 * Body Onload function
	 */    	
    	
    	function bodyonload()
    	{
		document.getElementById("domainname").value = "";   	 	  
        }
        
	/*
	 * On submit buttonpress function is called
	 */  	
	
   	function buttonpress(arg)
        {
		if(arg == "loadcreatedata")
		{
			document.forms[0].forward.value=arg;
			document.forms[0].action = "../flexfields/domain.do?submitType=loadCreateData";
			document.forms[0].submit();	
		}		
		else if(document.getElementById("domainname").value == "")
		{
			alert("Select Domain Name");
			return false;
		}
		else if(arg == "loadmodifydata")
		{
			document.forms[0].forward.value=arg;
			document.forms[0].action = "../flexfields/domain.do?submitType=loadModifyData";
			document.forms[0].submit();	
		}
		else if(arg == "delete")
		{
			document.forms[0].forward.value=arg;
			document.forms[0].action = "../flexfields/domain.do?submitType=deleteDomain";
			document.forms[0].submit();	
		}		
        }
        
    </script>
   
   <body  onload="bodyonload()">
    
    <BR>
	<table align='center' id="table2">
	<tr><td>
	  <div id="main"><div id="m2"><div id="m3">            
          <html:form action="/flexfields/domain" method="post">

          <table align="center" id="mainTable" name="mainTable" class="tableStyle">
            <tr>
                <td  class="tableheader" colspan="2" align="center">Domain<span></td>
            </tr>     

	    <tr><td colspan=2>&nbsp;</td></tr>

	 <tr>
	 <td>

	<table>
	
	<tr><td colspan=4>&nbsp;</td></tr>	

            <tr>
                <td class="labelcell" align="right" width="35%">Domain<span class="leadon">*</span></td>
                <td class="smallfieldcell" align="left" width="35%"> <html:select  styleId="domainname" property="id" styleClass="fieldinput" >
                 	<html:option value=""></html:option>
                	<html:options collection="domainList" property="id" labelProperty="name"/></html:select>
                	<td >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
               	</td>
            </tr>   
             
	<tr><td colspan=2>&nbsp;</td></tr>
	<tr><td colspan=2>&nbsp;</td></tr>
             
	</table>

	</td>
	</tr>	
    <table>
    <tr><td class="smalltext" ><span class="leadon">*</span> - Mandatory Fields
    </td></tr>
    <tr>
    <td><html:hidden property="forward" /></td> 
    <td><input type="button" class="button" tabindex="1" value="Create" onclick="buttonpress('loadcreatedata');" /></td>
    <td><input type="button" class="button" tabindex="1" value="Modify" onclick="buttonpress('loadmodifydata');" /></td>
    <td><input type="button" class="button" tabindex="1" value="Delete" onclick="buttonpress('delete');" /></td>
    <td><input type="button" class="button" tabindex="1" value="Close" onclick="window.close();" /></td>
   <tr>
   </table>
	
        </html:form>
        </div></div></div>
        </td></tr>
	</table>
            
       
    </body>
</html>

<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page errorPage="error.jsp" %>
<%@ page import="java.util.*,org.egov.infstr.utils.EgovMasterDataCaching" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

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
    	
    	var submitType = "<%=(String) request.getAttribute("buttonType")%>";
    	
    	function bodyonload()
    	{
		if(submitType == "loadcreatedata")
    		{		
			document.title="Domain - Create";
			document.getElementById('screenName').innerHTML="Create Domain";
		}
    		else if(submitType == "loadmodifydata")
    		{		
			document.title="Domain - Modify";
			document.getElementById('screenName').innerHTML="Modify Domain";
		}				
        }
        
	/*
	 * On submit buttonpress function is called
	 */  	
	
   	function buttonpress()
        {
		
		if(document.getElementById("domainname").value == "")
		{
			alert("Enter Domain Name");
			return false;
		}
		else if(document.getElementById("domaindesc").value == "")
		{
			alert("Enter Description");
			return false;
		}
		else if(submitType == "loadcreatedata")
		{
			document.forms[0].forward.value="saveorupdate";
			document.forms[0].action = "../flexfields/domain.do?submitType=createDomain";
			document.forms[0].submit();	
		}
		else if(submitType == "loadmodifydata")
		{
			document.forms[0].forward.value="saveorupdate";
			document.forms[0].action = "../flexfields/domain.do?submitType=updateDomain";
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
                <td  class="tableheader" colspan="2" align="center"><span id="screenName"> Domain <span></td>
            </tr>     

	    <tr><td colspan=2>&nbsp;</td></tr>

	 <tr>
	 <td>

	<table>
	
	<tr><td colspan=4>&nbsp;</td></tr>	

            <tr>            	 
                <td class="labelcell" align="right" width="35%">Domain Name<span class="leadon">*</span></td>
		<td class="fieldcell" align="left" width="35%"><html:text styleId="domainname" property="domainName" tabindex="1" style=";text-align:left"/></td>
		<td >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
             </tr>	
		
            <tr>
                <td class="labelcell" align="right" width="35%">Description<span class="leadon">*</span></td>
		<td class="fieldcell" align="left" width="35%"><html:text styleId="domaindesc" property="domainDesc" tabindex="1" style=";text-align:left"/></td>
             </tr>
             
	<tr><td><html:hidden property="id" /></td></tr>
	<tr><td colspan=2>&nbsp;</td></tr>
             
	</table>

	</td>
	</tr>	
    <table>
    <tr><td class="smalltext" ><span class="leadon">*</span> - Mandatory Fields
    </td></tr>
    <tr>
    <td><html:hidden property="forward" /></td>  
    <td><input type="button" class="button" tabindex="1" id ="save" name="save" value="Save" onclick="buttonpress();" /></td>
    <td><input type="button" class="button" tabindex="1" value="Close" onclick="window.close();" /></td>
   <tr>
   </table>
	
        </html:form>
        </div></div></div>
        </td></tr>
	</table>
            
       
    </body>
</html>

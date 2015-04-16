<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@page errorPage="error.jsp" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.util.*,org.egov.infstr.utils.EgovMasterDataCaching" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%! 
ArrayList domainList;
%>
<%
domainList=(ArrayList)EgovMasterDataCaching.getInstance().get("egi-domain"); 
%>

<c:set var="domainList" value="<%=domainList%>" scope="page" /> 

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">        
        <link rel="stylesheet" type="text/css" href="../css/egov.css">
	 <LINK rel="stylesheet" type="text/css" href="../css/ccMenu.css">
        <title>Attribute Type</title>
    </head>
    <body>
    
    <SCRIPT type="text/javascript" src="../commonjs/ajaxCommonFunctions.js"></Script>
	
  
    <script type="text/javascript">
    
	/*
	 * Body Onload function
	 */    	
    	
    	function bodyonload()
    	{
		document.getElementById("attributename").value = "";   	 	  
        }
        
	/*
	 * On submit buttonpress function is called
	 */  	
	
   	function buttonpress(arg)
        {
		if(arg == "loadcreatedata")
		{
			document.forms[0].forward.value=arg;
			document.forms[0].action = "../flexfields/attributeType.do?submitType=loadCreateData";
			document.forms[0].submit();	
		}
		else if(document.getElementById("domainid").value == "")
		{
			alert("Select Domain Name");
			return false;
		}		
		else if(document.getElementById("attributename").value == "")
		{
			alert("Select Attribute Name");
			return false;
		}
		else if(arg == "loadmodifydata")
		{
			document.forms[0].forward.value=arg;
			document.forms[0].action = "../flexfields/attributeType.do?submitType=loadModifyData";
			document.forms[0].submit();	
		}
		else if(arg == "delete")
		{
			document.forms[0].forward.value=arg;
			document.forms[0].action = "../flexfields/attributeType.do?submitType=deleteAttributeType";
			document.forms[0].submit();	
		}		
        }
        
        function loadAttributeNames()
        {
        	var attrId="",attrName="";
        	var domainid = document.getElementById("domainid").value;
		var type = "getAttributes";
		var link = "FlexFieldAjax.jsp?type=" + type+"&domainid=" + domainid+ " ";
		var request = initiateRequest();
		request.onreadystatechange = function() 
		{
		if (request.readyState == 4) 
		{
		if (request.status == 200) 
		{
			var response=request.responseText;
			var values=response.split("!$");
			var result = values[0].split("^");
			attrId = result[0].split("+");
			attrName = result[1].split("+");
		    }
		  }
		};
		request.open("GET", link, false);
		request.send(null);		
		document.getElementById("attributename").options[0] = new Option("","");
		for(var k = 1 ; k <= attrId.length  ; k++)
		{
			document.getElementById("attributename").options[k] = new Option(attrName[k-1],attrId[k-1]);			
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
                <td  class="tableheader" colspan="2" align="center">Attribute Type<span></td>
            </tr>     

	    <tr><td colspan=2>&nbsp;</td></tr>

	 <tr>
	 <td>

	<table>
	
	<tr><td colspan=4>&nbsp;</td></tr>	


            <tr>
                <td class="labelcell" align="right" width="35%">Domain<span class="leadon">*</span></td>
                <td class="smallfieldcell" align="left" width="35%"> <select  id="domainid" name="domainid" styleClass="fieldinput" onchange="loadAttributeNames()">
                 	<option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
                	<c:forEach var="obj" items="${domainList}" > 
                  		<option value="${obj.id}">${obj.name}</option>
              		</c:forEach>           	
                	</select>
                	<td >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
               	</td>
            </tr> 
            <tr>
                <td class="labelcell" align="right" width="35%">Attribute Name<span class="leadon">*</span></td>
                <td class="smallfieldcell" align="left" width="35%"> <html:select  styleId="attributename" property="id" styleClass="fieldinput" >
                 	</html:select>
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

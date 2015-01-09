<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<%@page errorPage="error.jsp" %>
<%@ page import="java.util.*,org.egov.infstr.utils.EgovMasterDataCaching,org.egov.infstr.flexfields.AttributeTypeForm" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">


<%! 
ArrayList domainList;
%>
<%
domainList=(ArrayList)EgovMasterDataCaching.getInstance().get("egi-domain");

AttributeTypeForm form = null;
if(request.getAttribute("buttonType").toString().equalsIgnoreCase("loadmodifydata"))
{
form = (AttributeTypeForm) request.getAttribute("typeform");
}	
 
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
	 
	var submitType = "<%=(String) request.getAttribute("buttonType")%>";	 
    	
    	function bodyonload()
    	{
	
    		if(submitType == "loadcreatedata")
    		{		
			document.title="Attribute Type - Create";
			document.getElementById('screenName').innerHTML="Create Attribute Type";
			var table = document.getElementById("listtable");
			table.style.visibility='hidden';
			document.getElementById("addbutton").style.visibility='hidden';
			document.getElementById("delbutton").style.visibility='hidden'; 			
		}
    		else if(submitType == "loadmodifydata")
    		{		
    			if(document.getElementById("key").value == "")
    			{
				var table = document.getElementById("listtable");
				table.style.visibility='hidden';
				document.getElementById("addbutton").style.visibility='hidden';
				document.getElementById("delbutton").style.visibility='hidden';     			
    			}
			document.title="Attribute Type - Modify";
			document.getElementById('screenName').innerHTML="Modify Attribute Type";
		}		
        }
        
	/*
	 * On submit buttonpress function is called
	 */  	
	
   	function buttonpress()
        {
		if(document.getElementById("domainname").value == "")
		{
			alert("Select Domain Name");
			return false;
		}
		else if(document.getElementById("attributename").value == "")
		{
			alert("Enter Attribute Name");
			return false;
		}
		else if(document.getElementById("attributedatatype").value == "")
		{
			alert("Enter Attribute Data type");
			return false;
		}
		else if(document.getElementById("islist").checked)
		{
			if( document.getElementById("key").value == "" && document.getElementById("value").value == "")
			{
				alert("Enter Key and Value");
				return false;
			}
		}
		
		if(submitType == "loadcreatedata")
		{
			document.forms[0].forward.value="saveorupdate";
			document.forms[0].action = "../flexfields/attributeType.do?submitType=createAttributeType";
			document.forms[0].submit();	
		}
		else if(submitType == "loadmodifydata")
		{
			document.forms[0].forward.value="saveorupdate";
			document.forms[0].action = "../flexfields/attributeType.do?submitType=updateAttributeType";
			document.forms[0].submit();	
		}		
        }
        
        function checkAttributeName()
        {
		var val = document.getElementById("attributename").value;
		var re = /^([a-zA-Z])$/;
		if(trimText(val) != "")
		{
			if(!re.test(val.charAt(0)) && val.charAt(0) != "$" && val.charAt(0) != "_" )
			{
				alert("First letter of the attribute name should be a alphabet or $ or _");
				document.getElementById("attributename").value="";
				document.getElementById("attributename").focus();
				return false;
			}		

			for(var i = 0; i< val.length; i++)
			{
				if(val.charAt(i) == " ")
				{
					alert("Attribute name cannot contain white spaces");
					document.getElementById("attributename").value="";
					document.getElementById("attributename").focus();
					return false;			
				}

			}
		}
        
        }
        
        function showListTable()
        {
        	var islistbox = document.getElementById("islist");
        	var table = document.getElementById("listtable");
        	if(islistbox.checked)
        	{
        		table.style.visibility='visible';
        		document.getElementById("addbutton").style.visibility='visible';
        		document.getElementById("delbutton").style.visibility='visible';
        	}
        	else
        	{
        		table.style.visibility='hidden';
        		document.getElementById("addbutton").style.visibility='hidden';
        		document.getElementById("delbutton").style.visibility='hidden';        		
        	}       
        }
        
	/*
	 * Adds a new row to the table
	 */  

	function addRow()
	{
		var tableObj = document.getElementById("listtable");
		var tbody=tableObj.tBodies[0];
		var lastRow = tableObj.rows.length;
		var rowObj = tableObj.rows[1].cloneNode(true);
		tbody.appendChild(rowObj);
		document.forms[0].key[lastRow-1].value="";
		document.forms[0].value[lastRow-1].value="";
		
	}
	
	/*
	 * Deletes the selected row in the table
	 */  	
	
	function deleteRow()
	{
		var firsttable=document.getElementById("listtable");
		var tablelength = firsttable.rows.length;
		if(tablelength != 2)
		{				
			firsttable.deleteRow(tablelength-1);							
		}

	}
	
	
        
    </script>

   <body  onload="bodyonload()">
    
    <BR>
	<table align='center' id="table2">
	<tr><td>
	  <div id="main"><div id="m2"><div id="m3">            
          <html:form action="/flexfields/attributeType" method="post">

          <table align="center" id="mainTable" name="mainTable" class="tableStyle">
            <tr>
                <td  class="tableheader" colspan="2" align="center"><span id="screenName"> Attribute Type</span></td>
            </tr>     

	    <tr><td colspan=2>&nbsp;</td></tr>

	 <tr>
	 <td>

	<table>
	
	<tr><td colspan=4>&nbsp;</td></tr>
	
            <tr>
            	<td class="labelcell" align="right" width="35%">Domain<span class="leadon">*</span></td>
                <td class="smallfieldcell" align="left" width="35%"> <html:select  styleId="domainname" property="applDomainId" styleClass="fieldinput" >
                 	<html:option value=""></html:option>
                	<html:options collection="domainList" property="id" labelProperty="name"/></html:select>
                	<td >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
               	</td>
            </tr>   	

            <tr>
                <td class="labelcell" align="right" width="35%">Attribute Name<span class="leadon">*</span></td>
		<td class="fieldcell" align="left" width="35%"><html:text styleId="attributename" property="attributeName" tabindex="1" style=";text-align:left" onblur="checkAttributeName();"/></td>
            </tr>	
		
            <tr>
                <td class="labelcell" align="right" width="35%">Attribute Data Type<span class="leadon">*</span></td>
		<td class="smallfieldcell" align="left" width="35%"><html:select tabindex="1" styleClass="fieldinput"  styleId="attributedatatype" property="attributeDataType">
   			<html:option value="">&nbsp;</html:option>
			<html:option value="Integer">Integer</html:option>
			<html:option value="Long">Long</html:option>
			<html:option value="Float">Float</html:option>
			<html:option value="Double">Double</html:option>
			<html:option value="Character">Character</html:option>
			<html:option value="String">String</html:option>
			<html:option value="Boolean">Boolean</html:option>
          		</html:select>
          	</td>
             </tr>
             
            <tr>
                <td class="labelcell" align="right" width="35%">Default Value</td>
		<td class="fieldcell" align="left" width="35%"><html:text styleId="attributename" property="defaultValue" tabindex="1" style=";text-align:left" /></td>
            </tr>
            
            <tr>
                <td class="labelcell" align="right" width="35%">Required</td>
		<td align="left" width="35%"><html:checkbox  property="isRequired" styleId="isrequired" tabindex="1" /></td>
            </tr>      
            
            <tr>
                <td class="labelcell" align="right" width="35%">List</td>
		<td align="left" width="35%"><html:checkbox  property="isList" styleId="islist" tabindex="1" onclick="showListTable()"/></td>
            </tr>                
             
	<tr><td><html:hidden property="id" /></td> </tr>
	<tr><td colspan=2>&nbsp;</td></tr>
         </table>
         
         <%         
         if(request.getAttribute("buttonType").toString().equalsIgnoreCase("loadcreatedata") || request.getAttribute("isList").toString().equalsIgnoreCase("false"))
         {
         %>
         
	<table id="listtable" name="listtable" >
	<tr>
		<td class="thStlyle" ><div align="center">Key</div></td>
		<td class="thStlyle" ><div align="center">Value</div></td>

	</tr>	
	 <tr>
	 	<td class="fieldcell" ><div align="left" ><html:text property= "key" styleId= "key" value=""/></div> </td>
		<td class="fieldcell" ><div align="left" ><html:text property= "value" styleId= "value" value=""/></div> </td>	
	</tr>
	</table>
	
	<%
	}	
	%>
	
         <%         
         if(request.getAttribute("buttonType").toString().equalsIgnoreCase("loadmodifydata") && !request.getAttribute("isList").toString().equalsIgnoreCase("false"))
         {
         %>
         
	<table id="listtable" name="listtable" >
	<tr>
		<td class="thStlyle" ><div align="center">Key</div></td>
		<td class="thStlyle" ><div align="center">Value</div></td>

	</tr>	
	
	<%
		String key[] = form.getKey();
		String value[] = form.getValue();
		
		for(int i=0; i<key.length; i++)
		{
	%>
	
	 <tr>
	 	<td class="fieldcell" ><div align="left" ><html:text property= "key" styleId= "key" value="<%=key[i]%>"/></div> </td>
		<td class="fieldcell" ><div align="left" ><html:text property= "value" styleId= "value" value="<%=value[i]%>"/></div> </td>	
	</tr>
	
	<%
	}
	%>
	
	</table>
	
	<%
	}	
	%>	
	
	<table>
	<tr>
		<td><input type="button"  class="button" value="Add Row" tabindex="1"  name="addbutton" onclick=" addRow();" /></td>
		<td><input type="button"  class="button" value="Delete Row" tabindex="1"  name="delbutton" onclick=" deleteRow();" /></td>
		
	</tr>	
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

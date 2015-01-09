<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="org.egov.infstr.utils.*,java.util.ArrayList,org.egov.lib.security.terminal.dao.UserValidateDAO,org.egov.lib.security.terminal.dao.UserValidateHibernateDAO,org.egov.lib.security.terminal.model.Location" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">        
        <link rel="stylesheet" type="text/css" href="../commoncss/egov.css">
	 <LINK rel="stylesheet" type="text/css" href="../commoncss/ccMenu.css">
        <title>Login page</title>
    </head>
    <body>
    
	<%! 
	ArrayList locationList,locationParentList;
	%>
	
	<%
	locationList=(ArrayList)EgovMasterDataCaching.getInstance().get("egi-location");
	locationParentList=(ArrayList)EgovMasterDataCaching.getInstance().get("egi-locationparent");
	%>

	<c:set var="locationList" value="<%=locationList%>" scope="page" />     
	<c:set var="locationParentList" value="<%=locationParentList%>" scope="page" /> 
    
    <%
    	String ipAddress= request.getRemoteAddr();
    	String include_roles = EGovConfig.getProperty("INCLUDE_ROLES","","IP-BASED-LOGIN");
    %>
    
    <SCRIPT type="text/javascript" src="../commonjs/ajaxCommonFunctions.js"></Script>    
    
    <script type="text/javascript">
    
	/*
	 * On submit buttonpress function is called
	 */  	
	
	var locationbased = false;
	
   	function buttonpress()
        {
		if(validate() == false)
		{
			return false;
		}
			
		document.UserValidateForm.ipAddress.value = "<%=ipAddress%>";
		document.forms[0].action = "../terminal/user.do?submitType=validateUser";
		document.forms[0].submit();			
		
        }
        
        function validate()
        {
	
		if(document.getElementById("username").value == "")
		{
			alert("Enter UserName");
			return false;
		}
		else if(document.getElementById("password").value == "")
		{
			alert("Enter Password");
			return false;
		}  
		
		if(locationbased == true)
		{
			if(document.getElementById("locationname").value == "")
			{
				alert("Select the Location");
				return false;
			}
			else if(document.getElementById("countername").value == "")
			{
				alert("Select the Counter");
				return false;
			}  		
		}
        }
        
        function bodyonload()
        {
       		document.getElementById("username").focus();
       	
        	var result = "<%=(String)request.getAttribute("target")%>";        	
          	
        	if(result == "success")
        	{
        		alert("Executed Successfully");
        		window.location = "Login.jsp";      	
        	}           	
        	else if(result == "failure")
        	{
        		alert("Incorrect Username and Password. Try again");
        		window.location = "Login.jsp";      	
        	}        	
     	
         }
         
         function checkRole()
         {
         	var include_roles = "<%=include_roles%>";
         	include_roles = include_roles.split(",");
         	var username = document.getElementById("username").value;
         	var userrole = "";
         	var locationid = "";
         	locationbased = false;      
         	       
         	if(username != "")
         	{
			var link = "TerminalAjax.jsp?username="+username+" ";
			var request = initiateRequest();
			request.onreadystatechange = function() 
			{
				if (request.readyState == 4) 
				{
				if (request.status == 200) 
				{
					var response=request.responseText.split("^");
					userrole = response[0];	
					locationid = response[1];	
				}
				}
			};

			request.open("GET", link, false);
			request.send(null);

			if(userrole != "")
			{
				for(var u = 0; u < include_roles.length; u++)
				{
					if(include_roles[u] == userrole)
						locationbased = true;
				}	
			}

			if(locationbased == true)
			{
				document.UserValidateForm.loginType.value = "Location";
				document.getElementById('loc').style.visibility = "visible";
				document.getElementById('loc1').style.visibility = "visible";
				document.getElementById('cou').style.visibility = "visible";
				document.getElementById('cou1').style.visibility = "visible";
				var locationObj = document.getElementById('locationname');				         	

				<c:forEach var="obj" items="${locationParentList}" >
					if(locationid == '${obj.id}')
					{
						locationObj.value = '${obj.name}';
						document.UserValidateForm.locationId.value = '${obj.id}';
					}
				</c:forEach> 	
				
				loadTerminalFields();
				
			} 
			else if(locationbased == false)
			{
				document.UserValidateForm.loginType.value = "";
				document.getElementById('loc').style.visibility = "hidden";
				document.getElementById('loc1').style.visibility = "hidden";
				document.getElementById('cou').style.visibility = "hidden";
				document.getElementById('cou1').style.visibility = "hidden";	
			}  			
		}
         }
         
         function loadTerminalFields()
         {
         	var locationId = document.UserValidateForm.locationId.value;
		var terminalObj = document.getElementById("countername");
		terminalObj.length = 0;
		terminalObj.options[0] = new Option('--- Choose ---','');
		var i = 1;         	
         	
		<c:forEach var="obj" items="${locationList}" >
			if(locationId == '${obj.locationId.id}')
			{
				terminalObj.options[i] = new Option('${obj.name}','${obj.id}');
				i++;	
			}
		</c:forEach>         	
         
         
         
         }
         
	function checkEnterKey()
	{
		var e = window.event;	
		if(e.keyCode == 13 && !e.shiftKey) 
		{
			buttonpress();
		}
	}         
         
    </script>
   
   <body onload="bodyonload()" onKeyDown="checkEnterKey()">
    
    <BR>
	<table align='center' id="table2">
	<tr><td>
	  <div id="main"><div id="m2"><div id="m3">            
          <html:form action="/terminal/user" method="post">

          <table align="center" id="mainTable" name="mainTable" class="tableStyle">

	    <tr><td colspan=2><a href="http://www.egovernments.org"><img src="../images/eGovern-Logo4.gif" border="0" width="90" height="90"align="left"></a></td></tr>

            <tr>
                <td   colspan="2" align="center"><div class="labelcell"><b>Login page</b></div> </td>
            </tr> 

	 <tr>
	 <td>

       
             
            <table>
	     	
	    <tr><td colspan=4>&nbsp;</td></tr> 
	    
            <tr>            	 
                <td class="labelcell" align="right" width="35%">UserName<span class="leadon">*</span></td>
		<td class="fieldcell" align="left" width="35%"><html:text styleId="username" property="username" tabindex="1" style=";text-align:left" onblur="checkRole()"/></td>
		<td >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
             </tr>	

            <tr>
                <td class="labelcell" align="right" width="35%">Password<span class="leadon">*</span></td>
		<td class="fieldcell" align="left" width="35%"><html:password styleId="password" property="password" tabindex="1" style=";text-align:left"/></td>
             </tr>
             
            <tr>
                <td class="labelcell" align="right" width="35%"><span style="visibility:hidden" id="loc">Location<span class="leadon">*</span></span></td>
                <td  align="left" width="35%"><span style="visibility:hidden" id="loc1" class="smallfieldcell"> <input type = "text"  id="locationname" name="locationname" styleClass="fieldinput"/>
		<html:hidden property="locationId" />
		</td>
            </tr> 
            
            <tr>
                <td class="labelcell" align="right" width="35%"><span style="visibility:hidden" id="cou">Counter<span class="leadon">*</span></span></td>
                <td  align="left" width="35%"><span style="visibility:hidden" id="cou1" class="smallfieldcell"> <html:select  styleId="countername" property="counterId" styleClass="fieldinput">
                 	<html:option value="">--Choose--</html:option></html:select></span>
                	<td >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
               	</td>
            </tr>            
            
          
             
	  <tr><td colspan=2>&nbsp;</td></tr>
             
	</table>  

	<table>
	    <tr>
	    <td>&nbsp;</td>
	    <td class="smalltext"><div align="center"><span class="leadon">*</span> - Mandatory Fields</div></td>
	    </tr>
	     <tr><td colspan=2>&nbsp;</td></tr>
	    <tr> 
	    <td><input type="button" class="button" tabindex="1" value="Login" onclick="buttonpress();"/></td>
	    <td><input type="button" class="button" tabindex="1" value="Close" onclick="window.close();" /></td>
	    <td><html:hidden property="ipAddress" /></td>
	    <td><html:hidden property="loginType" /></td>
	    <tr>
	</table>	

          


	</td>
	</tr>
	

   
	
        </html:form>
        </div></div></div>
        </td></tr>
	</table>
            
	<CENTER><TABLE cellSpacing=0 cellPadding=0 width=754 summary="" border=0><TBODY>
		<TR><TD>
		<P align=center><FONT face="Arial, Helvetica"></FONT></P>
		</TD></TR></TBODY></TABLE>
	

	</CENTER>	
	
    </body>
</html>

<%@ include file="/includes/taglibs.jsp"%>
<%@ page import="java.util.*,org.egov.infstr.utils.EgovMasterDataCaching" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%!
ArrayList locationList, locationParentList,usercounterList;
%>
<%
locationList=(ArrayList)EgovMasterDataCaching.getInstance().get("egi-location");
locationParentList=(ArrayList)EgovMasterDataCaching.getInstance().get("egi-locationparent");
usercounterList=(ArrayList)EgovMasterDataCaching.getInstance().get("egi-usercountermap");
%>

<c:set var="locationList" value="<%=locationList%>" scope="page" />
<c:set var="locationParentList" value="<%=locationParentList%>" scope="page" />
<c:set var="usercounterList" value="<%=usercounterList%>" scope="page" />

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <link rel="stylesheet" type="text/css" href="../css/egov.css">
	 <LINK rel="stylesheet" type="text/css" href="../css/ccMenu.css">
        <title>Create Location</title>
    </head>
    <body>

    <SCRIPT type="text/javascript" src="../commonjs/ajaxCommonFunctions.js"></Script>


    <script type="text/javascript">

	/*
	 * Body Onload function
	 */

    	function bodyonload()
    	{
		document.getElementById("locationname").value = "";
        }

	/*
	 * On submit buttonpress function is called
	 */

   	function buttonpress(arg)
        {
		if(arg == "loadcreatedata")
		{
			document.forms[0].forward.value=arg;
			document.forms[0].action = "../terminal/location.do?submitType=loadCreateData";
			document.forms[0].submit();
		}
		else if(document.getElementById("locationname").value == "")
		{
			alert("Select Location Name");
			return false;
		}
		else if(arg == "loadmodifydata")
		{
			document.forms[0].forward.value=arg;
			document.forms[0].action = "../terminal/location.do?submitType=loadModifyData";
			document.forms[0].submit();
		}
		else if(arg == "loadviewdata")
		{
			document.forms[0].forward.value=arg;
			document.forms[0].action = "../terminal/location.do?submitType=loadModifyData";
			document.forms[0].submit();
		}
		else if(arg == "delete")
		{
			document.forms[0].forward.value=arg;
			document.forms[0].action = "../terminal/location.do?submitType=deleteLocation";
			document.forms[0].submit();
		}
        }

        function disableButtons()
        {
		var id = document.getElementById("locationname").value;
		var flag = false;
		var counterArr = new Array();
		var i = 0;

		<c:forEach var="obj" items="${locationList}">
		    if("${obj.locationId.id}" == id || "${obj.id}" == id)
		    {
		    	counterArr[i] = "${obj.id}";
		    	i++;
		    }
		</c:forEach>

		<c:forEach var="obj" items="${usercounterList}" >
			for(var u = 0; u < counterArr.length; u++)
			{
				if(counterArr[u] == "${obj.counterId.id}")
				{
					flag = true;
				}
			}
		</c:forEach>

	       if(flag == true)
	       {
	       		document.getElementById("delete").disabled = true;
	       }
	       else
	       {
			document.getElementById("delete").disabled = false;
	       }
        }

    </script>

   <body  onload="bodyonload()">

    <BR>
	      <html:form action="/terminal/location" method="post">

          <table align="center" id="mainTable" name="mainTable" class="tableStyle">
            <tr>
                <td  class="tableheader" colspan="3" align="center"><bean:message key="Location" /><span></td>
            </tr>

	    <tr><td colspan=2>&nbsp;</td></tr>

	 <tr>
	 <td>


	<tr><td colspan=4>&nbsp;</td></tr>

            <tr>
                <td class="labelcell" align="right" width="35%"><bean:message key="location/terminal" /><span class="leadon">*</span></td>
                <td class="smallfieldcell" align="left" width="35%"> <html:select  styleId="locationname" property="id" styleClass="fieldinput" onchange="disableButtons()">
                 	<html:option value=""></html:option>
                	<html:options collection="locationParentList" property="id" labelProperty="name"/></html:select>
                	<td >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
               	</td>
            </tr>

	<tr><td colspan=2>&nbsp;</td></tr>
	<tr><td colspan=2>&nbsp;</td></tr>



	</td>
	</tr>
    <table>
    <tr><td class="smalltext" ><span class="leadon">*</span> - Mandatory Fields
    </td></tr>
    <tr>
    <td><html:hidden property="forward" /></td>
    <td><input type="button" class="button" tabindex="1" value="Create" onclick="buttonpress('loadcreatedata');" /></td>
    <td><input type="button" class="button" tabindex="1" id="view" name="view" value="View" onclick="buttonpress('loadviewdata');" /></td>
    <td><input type="button" class="button" tabindex="1" id="modify" name="modify" value="Modify" onclick="buttonpress('loadmodifydata');" /></td>
    <td><input type="button" class="button" tabindex="1" id="delete" name="delete" value="Delete" onclick="buttonpress('delete');" /></td>
    <tr>
   </table>

        </html:form>



    </body>
</html>

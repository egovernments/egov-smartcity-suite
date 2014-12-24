<%@ page import="java.util.*,org.egov.infstr.utils.*,org.egov.lib.rjbac.user.dao.*,org.egov.lib.rjbac.user.*,
org.egov.lib.security.terminal.model.*,java.text.*,
org.apache.commons.lang.StringUtils" %>
<%@ include file="/includes/taglibs.jsp" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%!
ArrayList locationParentList;

%>
<%
locationParentList=(ArrayList)EgovMasterDataCaching.getInstance().get("egi-locationparent");
%>
<c:set var="locationParentList" value="<%=locationParentList%>" scope="page" />

<html>

<head>
		<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
		<title>User Counter Map Type</title>





<script type="text/javascript">



	/*
	 * Body Onload function
	 */
		function onBodyLoad()
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

			if(validateTable() == false)
			{
				return false;
			}
			if(document.getElementById("isLocation").checked)
			{
				document.forms[0].loginType.value="Location";
			}
			else
			{
				document.forms[0].loginType.value="Terminal";
			}
			if(!document.getElementById("showAll").checked)
			{
				document.forms[0].forward.value=arg;
				document.forms[0].action = "${pageContext.request.contextPath}/terminal/usercountermap.do?submitType=getUserCounterForCurrentDate";
				document.forms[0].submit();
			}
			 if(document.getElementById("showAll").checked)
			{
				document.forms[0].forward.value=arg;
				document.forms[0].action = "${pageContext.request.contextPath}/terminal/usercountermap.do?submitType=getAllUserCounters";
				document.forms[0].submit();
			}
        }
	}






        function showLocFields(obj)
        {
              if(obj.checked)
        	{
				document.getElementById("locationname").length=0;
				document.getElementById("locationname").options[0] = new Option("Choose");
				document.getElementById("isTerminal").checked = false;
				document.getElementById('loc').style.visibility = "visible";
				document.getElementById('loc1').style.visibility = "visible";
				document.getElementById('loc2').style.visibility = "visible";
				document.getElementById('loc3').style.visibility = "visible";
				 var i=1;
        	       <c:forEach var="obj" items="${locationParentList}" >
        	       		var isLocation = "${obj.isLocation}";
        	       		if(isLocation == "1")
        	       		{

					document.getElementById("locationname").options[i] = new Option('${obj.name}','${obj.id}');
					i++;
				}
			</c:forEach>

        	}
        	else
			{
				document.getElementById('loc').style.visibility = "hidden";
				document.getElementById('loc1').style.visibility = "hidden";
				document.getElementById('loc3').style.visibility = "hidden";
				document.getElementById('loc2').style.visibility = "hidden";

			}
        }

        function showTerFields(obj)
        {
                if(obj.checked)
        	{
				document.getElementById("locationname").length=0;
				document.getElementById("locationname").options[0] = new Option("Choose");
				document.getElementById("isLocation").checked = false;
				document.getElementById('loc').style.visibility = "visible";
				document.getElementById('loc1').style.visibility = "visible";
				document.getElementById('loc2').style.visibility = "visible";
				document.getElementById('loc3').style.visibility = "visible";

				var i=1;
        	       <c:forEach var="obj" items="${locationParentList}" >
        	       		var isLocation = "${obj.isLocation}";
        	       		if(isLocation == "0")
        	       		{
					document.getElementById("locationname").options[i] = new Option('${obj.name}','${obj.id}');
					i++;
				}
			</c:forEach>
        	}
        	else
			{
				document.getElementById('loc').style.visibility = "hidden";
				document.getElementById('loc1').style.visibility = "hidden";
				document.getElementById('loc3').style.visibility = "hidden";
				document.getElementById('loc2').style.visibility = "hidden";

			}
        }



	function validateTable()
	{
		if(document.getElementById("locationname").value=="")
		{
			alert("Please select the Location/Terminal");
			document.getElementById("locationname").focus();
			return false;
		}
		if(document.getElementById("isLocation").checked == false && document.getElementById("isTerminal").checked == false)
		{
			alert("Select Location or Terminal check box ");
			return false;
		}

		if(document.getElementById("isLocation").checked)
		{
			document.forms[0].loginType.value="Location";

		}
		else if(document.getElementById("isTerminal").checked)
		{
			document.forms[0].loginType.value="Terminal";
		}

		return true;
	}


    </script>

</head>

	<body bgcolor="#FFFFFF"  onload="onBodyLoad()">


	 <html:form action="/terminal/usercountermap" method="post">



		  <table align="center" id="mainTable" name="mainTable" class="tableStyle">
		 <tr>
			<td  class="tableheader" colspan="6" align="center" height="24"><bean:message key="userCounter" /><span></td>
		</tr>
 		<tr><td colspan=6>&nbsp;</td></tr>

           <tr>
		              	<td class="labelcellforsingletd" align="center" width="35%"><bean:message key="Location" /><input type="checkbox"  id="isLocation" name="isLocation"  onclick="showLocFields(this)"/></td>
		                 <td class="labelcellforsingletd" align="left" width="35%"><bean:message key="terminal" /><input type="checkbox"  id="isTerminal" name="isTerminal"  onclick="showTerFields(this)"/></td>
            	  </tr>

		</table>
		<table>
			<tr><td colspan=4>&nbsp;</td></tr>

			<tr>
				<td class="labelcell" align="right" width="35%"><span style="visibility:hidden" id="loc"><bean:message key="location/terminal" /><span class="leadon">*</span></span></td>
				<td  align="left" width="35%"><span style="visibility:hidden" id="loc1" class="smallfieldcell"> <html:select  styleId="locationname" property="locationId" styleClass="fieldinput">
					<html:option value="">--Choose--</html:option></html:select></span>
					<td >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
				</td>
			</tr>
			<tr >
			<td class="labelcell" align="right" width="35%"><span style="visibility:hidden" id="loc2">Show All&nbsp;</span></td>
							<td  align="left" width="35%"><span style="visibility:hidden" id="loc3" class="smallfieldcell"><input type="checkbox" name="showAll" id="showAll" value="ON" > </span>
								<td >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
							</td>
			</tr>
		</table>
		<table>
			<tr><td colspan=4>&nbsp;</td></tr>
		</table>

		<table>
			<tr><td colspan=4>&nbsp;</td></tr>
			<tr>
		    <td><html:hidden property="forward" /></td>
		    <td><html:hidden property="loginType" /></td>
		    <td><input type="button" class="button"  value="Submit" onclick="buttonpress('loadcreatedata');" /></td>
		    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		   <tr>
   		</table>




   </html:form>
   </body>
   </html>
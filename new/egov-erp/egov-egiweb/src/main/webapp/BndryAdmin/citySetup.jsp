<%@page import="java.util.*,
		org.egov.lib.admbndry.HeirarchyType"%>
<%@ include file="/includes/taglibs.jsp" %>

<SCRIPT type="text/javascript" src="<c:url value="/script/jsCommonMethods.js" />"></Script>
<SCRIPT type="text/javascript" src="<%=request.getContextPath() +"/commonjs/ajaxCommonFunctions.js"%>"></Script>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<script>

function addURL()
{

if(document.forms("cityForm").cityBaseURL.value=="")
		{
			alert("Enter City Base URL");
			return false;
		}
var tbl = document.getElementById('cityType');

			var tbody=tbl.tBodies[0];

			var lastRow = tbl.rows.length;

			var rowObj = document.getElementById('reasonrow').cloneNode(true);

			tbody.appendChild(rowObj);

			document.cityForm.cityBaseURL[lastRow].value="";

}

function validateisActive()
		{
		if(!isNaN(document.forms("cityForm").cityName.value))
		{
			alert("City Name cannot be a numeric value.");
			document.forms("cityForm").cityName.value="";
			document.forms("cityForm").cityName.focus();
			return false;
		 }
		if(document.forms("cityForm").cityName.value=="")
		{
			alert("City Name is required");
			return false;
		}
		if(document.forms("cityForm").cityBaseURL.value=="")
		{
			alert("City Base URL is required");
			return false;
		}
		if(document.forms("cityForm").boundaryId.value=="--Choose--")
		{
			alert("Boundary Type is empty\n\nSelect HierarchyType which contains existing Boundaries");
			return false;
		}
		if(document.getElementById("isActiveValue").checked)
		{


			document.forms("cityForm").isActive.value =1;


		}

		else
		{

			document.forms("cityForm").isActive.value =0;



		}
			return true;

		}
	function trimText1(obj,value)
		{
		    value = value;
		    if(value!=undefined)
		   {
			   while (value.charAt(value.length-1) == " ")
			   {
				value = value.substring(0,value.length-1);
			   }
			   while(value.substring(0,1) ==" ")
			   {
				value = value.substring(1,value.length);
			   }
			   obj.value = value;
			}
		   return value ;
}
function bodyonLoad()

{
document.forms("cityForm").cityBaseURL.value="";
document.forms("cityForm").cityName.value="";
document.forms("cityForm").cityNameLocal.value="";


}

</script>



<head>
	<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
	<title>CITY SETUP</title>


</head>

<body onload=bodyonLoad();>


<html:form action="CreateCitySetup?" onsubmit=" return validateisActive();"  >


	<table align="center" class="tableStyle" >
		<tr>
			<td class="tableheader" align="middle" width="728" height="6">
			City Set up
			</td>
		</tr>
	<tr><td colspan=4>&nbsp;</td></tr>
	</table>

	<table align="center"  class="tableStyle" >
	<tr>
	<td  class="labelcell" width="25%" height="23" >
	Hierarchy Type <font class="ErrorText"></font> </td>
	<td class="labelcell" align="left">
	<html:select styleId="hierarchyTypeid"  property="hierarchyTypeid" onchange="loadSelectData('${pageContext.request.contextPath}/commonyui/egov/loadComboAjax.jsp' , 'EG_BOUNDARY_TYPE', 'id_bndry_type', 'name', 'id_heirarchy_type= #1 and parent is null', 'hierarchyTypeid', 'boundaryId')">

				<html:option value="--Choose--"></html:option>
					<%
						Set hierarchySet = (HashSet) request.getSession().getAttribute("hierarchySet");
						System.out.println("the hierarchySet size in jsp------------>"+hierarchySet.size());
						for(Iterator itr =hierarchySet.iterator();itr.hasNext();)
						{
							HeirarchyType heirarchyType = (HeirarchyType)itr.next();
							String hierarchyTypeid = heirarchyType.getId().toString();


					%>


						<html:option value="<%=hierarchyTypeid %>"><%=  heirarchyType.getName()%></html:option>

					<%
						}
					%>


					</html:select>

</td></tr>
<tr>
	<td class="labelcell" width="25%" height="23" >
	Boundary Type <font class="ErrorText">*</font> </td>
	<td class="labelcell" align="left">
	<html:select styleId="boundaryId"  property="boundaryId">

	<html:option value="--Choose--"></html:option>
</html:select>
</td></tr>

	<tr>
			<td class="labelcell"  width="40%" height="23" >City Name<font class="ErrorText">*</font></td>
			<td  class="labelcell" align="left" width="40%" height="23" >
			<html:text property="cityName" styleClass="ControlText" onchange="return trimText1(this,this.value);"/></td>
		</tr>
		<tr>
			<td  class="labelcell" width="40%" height="23" >City Name Local</font></td>
			<td  class="labelcell" align="left" width="40%" height="23" >
			<html:text property="cityNameLocal" styleClass="ControlText"  /></td>
	</tr>
		</table>
		<table align="center"  class="tableStyle" id="cityType" name="cityType">
		<tr id="reasonrow">
					<td  class="labelcell" width="40%" height="23" >City Base URL<font class="ErrorText">*</font></td>
					<td  class="labelcell" align="left" width="40%" height="23" >
					<html:text property="cityBaseURL" styleClass="ControlText" onchange="return trimText1(this,this.value);"/>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type=button  value="Add More URL" onClick="addURL();"/>
					<font size="2">(eg:</font><a href="http://www.mysorecity.gov.in"> <font size="2" color="blue"> www.mysorecity.gov.in</font></a>,<font size="2">192.168.24.1)</font>

					</td>
			</tr>
		</table>
		<table align="center"  class="tableStyle" >
		<tr>
			<td  class="labelcell" width="40%" height="23" >Boundary Number</font></td>
			<td class="labelcell" align="left" width="40%" height="23" >
			<label>1</label></td>
	</tr>
	<tr>
		<td class="labelcell"  width="40%" height="23" >Is Active</td>
		<td  class="labelcell" align="left" width="40%" height="23" >
		<input type="checkbox" name="isActiveValue" checked="true" id="isActiveValue" value="ON" >
		<input type="hidden" name="isActive" id="isActive">
		</td>
	</tr>
	</table>
	<table align="left" class="tableStyle">

	<tr >
		<td class="button2" vAlign="bottom" align="left" width="100%" colSpan="2" height="23">
		<p align="center">
		<html:submit value="Create" /></td>
	</tr>


	</table>
	</tr>
	</table>

	</html:form>
</body>
</html>
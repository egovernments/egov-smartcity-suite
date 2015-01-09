<%@ page import="java.util.*,org.egov.infstr.utils.*,org.egov.lib.rjbac.user.dao.*,org.egov.lib.rjbac.user.*,
org.egov.lib.security.terminal.model.*,java.text.*,
org.apache.commons.lang.StringUtils" %>
<%@ include file="/includes/taglibs.jsp" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">




<html>

<head>

<title>Reports</title>
<script>

function showLocFields()
{
   if(document.getElementById("isLocation").checked)
   {
		document.getElementById('fromDate').value="";
		document.getElementById('toDate').value="";
		document.getElementById("isTerminal").checked=false;
		document.getElementById('locdata').style.visibility = "visible";
   }

}
function showTerFields()
{

	if(document.getElementById("isTerminal").checked)
   {
		document.getElementById('fromDate').value="";
		document.getElementById('toDate').value="";
		document.getElementById("isLocation").checked=false
		document.getElementById('locdata').style.visibility = "visible";
   }
}
function buttonpress()
{
	if(document.getElementById("isLocation").checked)
	{
		document.getElementById("isLoc").value =1;
	}
	if(document.getElementById("isTerminal").checked)
	{
		document.getElementById("isLoc").value =0;
	}
	if(document.getElementById("isLoc").value=="")
	{
		alert("please select Location or Terminal");
		return false;
	}
	if(document.getElementById("toDate").value!="" && document.getElementById("fromDate").value=="")
	{
		alert("please enter the from date");
		document.getElementById("fromDate").focus();
		return false;
	}
	document.forms[0].action = "${pageContext.request.contextPath}/reports/UserCountersReports.do?submitType=generateUserCounterReports";
	document.forms[0].submit();

}
function validateDateFormat(obj)
{
 var dtStr=obj.value;
 var year;
 var day;
 var month;
 var leap=0;
 var valid=true;
 var oth_valid=true;
 var feb=false;
 var validDate=true;

	if(dtStr!="" && dtStr!=null)
	{
		year=dtStr.substr(6,4);
		month=dtStr.substr(3,2);
		day=dtStr.substr(0,2);

		if(year=="0000" || year<1900 || month=="00" || day=="00" || dtStr.length<10)
		{
			validDate=false;
		}

		if(validDate==true)
		{
			//if(year>1900 && year<=
			leap=year%4;

		if(leap==0)
			{
				//alert("Leap Year");

				if(month=="2") // || month==02)
				{
					valid=false;
					feb=true;
				}

				else if(month=="02")
				{
					if(day>29)
					{
						valid=false;
					}
					feb=true;
				}

			}

			else
			{
				if(month=="2")
				{
					valid=false;
					feb=true;
				}
				else if(month=="02")
				{
					if(day>28)
					{
						valid=false;
					}
					feb=true;
				}

			}

			if(feb==false)
			{
				if(month=="03" || month=="01" || month=="05" || month=="07" || month=="08" || month=="10" || month=="12")
				{
					if(day>31)
					{
						oth_valid=false;
					}
				}

				else if(month=="04" || month=="06" || month=="09" || month=="11")
				{
					if(day>30)
					{
						oth_valid=false;
					}
				}

				else
				{
					oth_valid=false;
				}

			}
		}
	}

	if(valid==false || oth_valid==false || validDate==false)
	{
		alert("Please enter the valid date in the dd/MM/yyyy Format only");
		obj.value="";
		obj.focus();
	}
	return;
}
</script>
</head>

	<body>


	 <html:form action="/reports/UserCountersReports" method="post">

	<table align="center" id="mainTable" name="mainTable" class="tableStyle" width="800">
	<tr>
		<td  class="tableheader" colspan="6" align="center"><span id="screenName"> User Counter Map Reports <span></td>
	</tr>

	<tr><td colspan=6>&nbsp;</td></tr>

	<tr>
		<td class="labelcellforsingletd" align="center" width="35%"><bean:message key="Location" /><input type="checkbox"  id="isLocation" name="isLocation" tabindex="1" onclick="showLocFields()"/></td>
	   <td class="labelcellforsingletd" align="left" width="35%"><bean:message key="terminal" /><input type="checkbox"  id="isTerminal" name="isTerminal" tabindex="1" onclick="showTerFields()"/></td>
	   <input type="hidden" name="isLoc" id="isLoc">
	 </tr>
	</table>
	<span style="visibility:hidden" id="locdata">
	<table id="locationData">
	<tr><td colspan=6>&nbsp;</td></tr>
	<tr>
		<td class="labelcell"><div align="center"><bean:message key="fromDate" />(dd/mm/yyyy)</div></td>
		<td class="fieldcell" ><div align="left" ></div><input type="text " name="fromDate"  id="fromDate"size="25" onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="DateFormat(this,this.value,event,true,'3');validateDateFormat(this);" /></td>
		<td class="labelcell"><div align="center"><bean:message key="toDate" />(dd/mm/yyyy)</div></td>
		<td class="fieldcell" ><div align="left" ><input type="text " name="toDate" id="toDate"size="25" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="DateFormat(this,this.value,event,true,'3');validateDateFormat(this);" /></div></td>
	</tr>
	</table>
	<table>
		<tr><td colspan=4>&nbsp;</td></tr>
		<tr>
	    <td><input type="button" class="button" tabindex="1" value="Submit" onclick="return buttonpress();" /></td>
	    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	   <tr>
	</table>
	</span>



   </html:form>
   </body>
   </html>
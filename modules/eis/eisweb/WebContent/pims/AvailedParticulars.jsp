
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-nested" prefix="nested"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ page import="java.util.*,
		 org.egov.infstr.utils.*,
		
		 org.egov.budget.services.*,
		 org.apache.log4j.Logger,
		 org.egov.pims.*,
		 org.egov.pims.dao.*,
		 javax.servlet.RequestDispatcher,
		 org.egov.pims.model.*,
		 org.egov.pims.service.*,
		 org.egov.infstr.commons.*,
		 org.egov.pims.commons.client.*,
		 org.egov.infstr.commons.dao.*,
		 org.egov.lib.address.model.*,
		 org.egov.lib.address.dao.*,
		 java.text.SimpleDateFormat,
		 org.egov.pims.client.*"


%>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title><bean:message key="PersonalInfoSys"/></title>




    <SCRIPT type="text/javascript" src="<%=request.getContextPath()%>/javascript/dateValidation.js" type="text/javascript"></SCRIPT>
     <script language="text/JavaScript" src="<%=request.getContextPath()%>/Admin-Homepage_files/dhtml.js" type="text/JavaScript"></script>

</head>
<%
String id = request.getParameter("Id").trim();
Set tpSet = new HashSet();
EmployeeServiceImpl employeeServiceImpl=new EmployeeServiceImpl();
PersonalInformation egpimsPersonalInformation = null;
PersonalInformation displayPersonalInformation = null;

if( ((String)(session.getAttribute("viewMode"))).trim().equalsIgnoreCase("modify") ||  ((String)(session.getAttribute("viewMode"))).trim().equalsIgnoreCase("view"))
{
	egpimsPersonalInformation = employeeServiceImpl.getEmloyeeById(new Integer(id));
	displayPersonalInformation= employeeServiceImpl.getEmloyeeById(new Integer(id));
	tpSet = egpimsPersonalInformation.getEgpimsLtcPirticularses();

}
else if(((String)(session.getAttribute("viewMode"))).trim().equalsIgnoreCase("create"))
{
	egpimsPersonalInformation = new PersonalInformation();
	displayPersonalInformation= employeeServiceImpl.getEmloyeeById(new Integer(id));
	tpSet = egpimsPersonalInformation.getEgpimsLtcPirticularses();
}

if(tpSet.isEmpty())
	tpSet.add(new LtcPirticulars());
SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");


%>

<script>

function goindex(arg)
{

	if(arg == "Index")
	{

		document.forms("pIMSForm").action = "${pageContext.request.contextPath}/staff/index.jsp";
		document.forms("pIMSForm").submit();
	}


}

function addRow(obj,tableName,row)
{


    	var tbl=tableName;
    	var rowO=tbl.rows.length;
    	var name=obj.value;
        var tname = "resetRowValues"+name;
        if(rowO<11)
        {
        	if(row != null)
        	{
        		var tbody=tbl.tBodies[0];
    			var lastRow = tbl.rows.length;
    			rIndex = 0;
    			var rowObj = row.cloneNode(true);
    			rowObj.document.getElementById('tpId').value = 0;
    			rIndex = rowObj.rowIndex;
    			tbody.appendChild(rowObj);
    			resetRowValues(lastRow-1,name);
		}

    }
}

function resetRowValues(lastRow,name)
{

	if(name=="AddRowAP")
		{
			document.pIMSForm.bYear[lastRow].value="";
			document.pIMSForm.leaveTypeAvailed[lastRow].value="";
			document.pIMSForm.claimed[lastRow].value="";
			document.pIMSForm.orderNo[lastRow].value="";
			document.pIMSForm.dateAvailed[lastRow].value="";

		}




}
var firstLength = 0;
var rIndex = 0;

  function setLength()
  {
 	 var tbl=document.getElementById('TPTable');
    	var rowo=tbl.rows.length;
  	firstLength = rowo;

  }
  function deleteRow(obj,tableName,addButtion)
  {
      var tbl=tableName;
      var rowo=tbl.rows.length;

      if(rowo<=firstLength)
  	{
  		alert('<bean:message key="alertCannotDelete"/>');
  		return false;
  	}
  	else
  	{

  		tbl.deleteRow(rIndex);
  		return true;
  	}
  }



function validateNotEmpty( strValue )
{
   var strTemp = strValue;
   strTemp = trimAll(strTemp);
   if(strTemp.length > 0)
   {
      return true;
   }
   return false;
 }





function ButtonPressNew(arg)
{


	if(arg == "savenew")
	{

		var tbl= document.getElementById("TPTable");
				var rows= tbl.rows.length;

				if(rows>2)
				{
					for(var i=0 ;i<rows-1;i++)
					{
						//alert("Name="+document.propWithTaxCollectionForm.firstName[i].value);
						if(document.pIMSForm.bYear[i].value=="" )
						{
							alert('<bean:message key="alertFillYr"/>');
							document.pIMSForm.bYear[i].focus();
							return false;
						}
						if(document.pIMSForm.leaveTypeAvailed[i].value=="" )
						{
							alert('<bean:message key="alertLvTypeAvailed"/>');
							document.pIMSForm.leaveTypeAvailed[i].focus();
							return false;
						}
						if(document.pIMSForm.claimed[i].value=="" )
						{
							alert('<bean:message key="alertFillClaimed"/>');
							document.pIMSForm.claimed[i].focus();
							return false;
						}


					}
				}
				else
				{
					if(document.pIMSForm.bYear.value=="" )
					{
						alert('<bean:message key="alertFillYr"/>');
						document.pIMSForm.bYear.focus();
						return false;
					}
					if(document.pIMSForm.leaveTypeAvailed.value=="" )
					{
						alert('<bean:message key="alertLvTypeAvailed"/>');
						document.pIMSForm.leaveTypeAvailed.focus();
						return false;
					}
					if(document.pIMSForm.claimed.value=="" )
					{
						alert('<bean:message key="alertFillClaimed"/>');
						document.pIMSForm.claimed.focus();
						return false;
					}

				}

		var submitType="";
		<%
			String mode1=((String)(session.getAttribute("viewMode"))).trim();
			if(mode1.equalsIgnoreCase("modify"))
			{
		%>
			submitType="modifyAvailedParticulars";

		<%
		 }
		 else if(mode1.equalsIgnoreCase("create"))
		 {
		 %>
		 	submitType="saveAvailedParticulars";
		 <%
		 }
		 %>
		document.forms("pIMSForm").action = "<%=request.getContextPath()%>/pims/AfterPIMSAction.do?submitType="+submitType;
		document.forms("pIMSForm").submit();
	}



}
</script>





<!-- Header Section Begins -->

<!-- Header Section Ends -->
<body onload ="setLength()">
<table align='center' id="table2" >
<tr>
<td>
<!-- Tab Navigation Begins -->

<!-- Tab Navigation Ends -->

<!-- Body Begins -->



<!-- Body Begins -->

<div align="center">
<center>
<html:form  action="/pims/AfterPIMSAction.do?submitType=saveDetails" >

<input type=hidden name="Id" id="Id" value="<%= request.getParameter("Id").trim() %> " />
<table  style="width: 800;" colspan="5" cellpadding ="0" cellspacing ="0" border = "1" style="width: 785;" colspan="5" >
<tbody>
<tr>
  <td colspan="8" height=20 bgcolor=#dddddd align=middle  class="tableheader">
<p><bean:message key="LTcAvailed"/>&nbsp;&nbsp;&nbsp;</td>
  </tr>
<tr>
<td colspan="8"  class = "labelcellmedium"><bean:message key="Ltc"/></td>
</tr>
<tr>
	  		<td class="labelcell" >
	  		 <bean:message key="EmployeeName"/></td>

	  		<td  class="labelcell" ><%=displayPersonalInformation.getEmployeeFirstName()%></td>
	  		<td class="labelcell" >
				  		<bean:message key="EmployeeCode"/> </td>

	  		<td  class="labelcell" ><%=displayPersonalInformation.getEmployeeCode()%></td>
</tr>
</table>

<%
 if(tpSet != null && !tpSet.isEmpty())
 {
 System.out.println("ggggggggggggg");
 %>
 <table  style="width: 800;" colspan="5" cellpadding ="0" cellspacing ="0" border = "1" id="TPTable" name="TPTable" >
   <tbody>
   <tr>

   <td   class="labelcell" ><bean:message key="BlockYear"/><SPAN class="leadon">*</SPAN></td>
   <td   class="labelcell" ><bean:message key="LeaveType"/><SPAN class="leadon">*</SPAN></td>
   <td   class="labelcell" ><bean:message key="Claimed"/><SPAN class="leadon">*</SPAN></td>
   <td   class="labelcell" ><bean:message key="OrderNo"/></td>
<td   class="labelcell" ><bean:message key="OrderDate"/></td>
  </tr>

 <%
 	Iterator itr1 = tpSet.iterator();
	for(int i=0;itr1.hasNext();i++)
	{
		LtcPirticulars egpimsLtcPirticulars = (LtcPirticulars)itr1.next();
 %>
	<tr id="TPRow">

	<input type = hidden name="tpId" id="tpId" value="<%=egpimsLtcPirticulars.getLtcDetailsId()==null?"0":egpimsLtcPirticulars.getLtcDetailsId().toString()%>" />
	<td class="fieldcell"><input style = "width:155px"  type="text" name="bYear" id="bYear" value="<%=egpimsLtcPirticulars.getBlockYear()==null?"":egpimsLtcPirticulars.getBlockYear().toString()%>" onblur = "javaScript:checkYear(this)"></td>
	<td class="fieldcell"><input style = "width:155px"  type="text" name="leaveTypeAvailed" id="leaveTypeAvailed" value="<%=egpimsLtcPirticulars.getLeaveType()==null?"":egpimsLtcPirticulars.getLeaveType().toString()%>" ></td>
	<td class="fieldcell"><input style = "width:155px"  type="text" name="claimed" id="claimed"  value="<%=egpimsLtcPirticulars.getClaimedWayFare()==null?"":egpimsLtcPirticulars.getClaimedWayFare()%>"></td>
	<td class="fieldcell"><input style = "width:155px"  type="text" name="orderNo" id="orderNo" value="<%=egpimsLtcPirticulars.getOrderNo()==null?"":egpimsLtcPirticulars.getOrderNo()%>" ></td>
	<td class="fieldcell"><input style = "width:155px"  type="text" name="dateAvailed" id="dateAvailed" onBlur = "validateDateFormat(this);validateDateJS(this)" value="<%=egpimsLtcPirticulars.getOrderDate()==null?"":sdf.format(egpimsLtcPirticulars.getOrderDate())%>" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')">
	</td>
	</tr>
<%

	}
}

%>
</tbody>
</table>

<table  style="width: 800;" colspan="5" cellpadding ="0" cellspacing ="0" border = "1" >
<tr>
   <%
   if(!((String)(session.getAttribute("viewMode"))).trim().equals("view"))
   {
%>
   <td> <input class="button2" id="addtpBtn" name="addtpBtn"  align ="center"type="button" value="AddRowAP" onclick="javascript:addRow(this,document.getElementById('TPTable'),document.getElementById('TPRow'))"></td>

   <td><input class="button2" id="deltp" name="deltp"  type="button" value="DeleteRow " onclick="javascript:deleteRow(this,document.getElementById('TPTable'),document.getElementById('addtpBtn'))" ></td>
   <%
   }
   %>
   </tr>
      <tr>
<%
if(!((String)(session.getAttribute("viewMode"))).trim().equals("view"))
{
%>

  <td><html:button styleClass="button" value="Save" property="b2" onclick="ButtonPressNew('savenew');" /></td>

 <%
 }

 %>

  		<td><html:button styleClass="button" value="Search" property="b4" onclick="goindex('Index')" /></td>

</tr>
</table>
</table>


 </html:form>
</table>
</table>
</center>
</div>
</div>
</td>
</tr>
<!-- Body Section Ends -->
</<table>
</body>
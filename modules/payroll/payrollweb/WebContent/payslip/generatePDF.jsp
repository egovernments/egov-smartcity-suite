<%@ include file="/includes/taglibs.jsp" %>
<%@page
	import="java.util.*,
                org.egov.infstr.utils.*,
                org.egov.payroll.services.payslip.*,
		org.egov.payroll.utils.PayrollManagersUtill,
		org.egov.infstr.utils.EgovMasterDataCaching"%>



<%@page import="java.util.*"%>


<html>

<head>


<title>Generate PDF Page</title>
<script language="JavaScript" type="text/JavaScript">

var month="",dept="0",fin_Year="",flag="no";
function onBodyLoad()
{
	<%
		ArrayList deptList=(ArrayList)EgovMasterDataCaching.getInstance().get("egi-department");
		ArrayList financialYearList=(ArrayList)EgovMasterDataCaching.getInstance().get("egi-activeFinYr");
	%>


}

 function validation()
 {

  if(document.salaryPaySlipForm.month.options[document.salaryPaySlipForm.month.selectedIndex].value =="0")
   {
	   alert("Please Select the Month!!!");
	   document.salaryPaySlipForm.month.focus();
	   return false;
   }
   if(document.salaryPaySlipForm.year.options[document.salaryPaySlipForm.year.selectedIndex].value =="0")
   {
	   alert("Please Select the Year!!!");
	   document.salaryPaySlipForm.year.focus();
	   return false;
   }
   if(document.salaryPaySlipForm.year.value !="0" && document.salaryPaySlipForm.month.value!="0")
   {
		month = document.salaryPaySlipForm.month.value;
		fin_Year = document.salaryPaySlipForm.year.value;
   }
    if(document.salaryPaySlipForm.department.value !="0")
    {
		dept= document.salaryPaySlipForm.department.value;
	}
	 if(document.salaryPaySlipForm.forceSelection.checked)
	 {
		 flag = "yes"
	 }
	if(!document.salaryPaySlipForm.forceSelection.checked)
	{
		flag = "no"
	}

	document.salaryPaySlipForm.action ="${pageContext.request.contextPath}/generatePDF.do?fin_Year="+fin_Year+"&dept="+dept+"&month="+month+"&flag="+flag;
	 document.salaryPaySlipForm.submit();
 }

</script>
</head>


<body onLoad="onBodyLoad();">

<html:form method="POST" action="/generatePDF" >

	<table style="width: 700;" colspan="6" cellpadding="1" cellspacing="2"
		border="0" id="employee">
		<tr>
			<td colspan="6" height=30 bgcolor=#bbbbbb align=middle
				class="tablesubcaption">
			<p align="center"><b>Generate PDF for Payslips &nbsp;&nbsp;&nbsp;</b>
			</td>
		</tr>
		<tr>
			<td height="15" class="tablesubcaption" colspan="6" align="center">(<font
				color="red">*</font> <font size="1">indicates required fields
			)</td>
		</tr>

  <tr>
  <td class="labelcell">Month/Year<font color="red">*</font></td>
	    <td class="labelcellbig"><select name="month" id="month" style="width:70px">
	    	<option value="0">Choose</option>
	    	<option value="1">JAN</option>
	    	<option value="2">FEB</option>
	    	<option value="3">MAR</option>
	    	<option value="4">APR</option>
	    	<option value="5">MAY</option>
	    	<option value="6">JUN</option>
	    	<option value="7">JUL</option>
	    	<option value="8">AUG</option>
	    	<option value="9">SEP</option>
	    	<option value="10">OCT</option>
	    	<option value="11">NOV</option>
	    	<option value="12">DEC</option>
	    	</select>/
	    	<select name="year" id="year" style="width:70px">
	    	<option value="0">Choose</option>
	    	<c:forEach var="financialYearObj" items="<%=financialYearList%>">
	    	<option value="${financialYearObj.id}">${financialYearObj.finYearRange}</option>
	    	</c:forEach>
	     	</select>
    </td>
     <td class="labelcell">Department</td>
     <td class="labelcellbig" align="left" >
		<select  name="department" id="department" style="width:100px">
		<option value='0'>--Choose--</option>
		<c:forEach var="department" items="<%=deptList%>" >
			<option value="${department.id}">${department.deptName}</option>
		</c:forEach>
		</select>
	</td>
  </tr>
  <tr>
  <td class="labelcell" colspan="2"><input type="checkbox" value="no" id="forceSelection" name="forceSelection" />&nbsp;Force&nbsp;Generation&nbsp;Of&nbsp;PDF</td>
  </tr>
  </table>
  <table  style="width: 700;" colspan="6" cellpadding ="0" cellspacing ="0" border = "0" >
  <tr>
  <td><p align="center"><input type="button" class="button" name="create" value="Generate PDF" onclick="return validation();"/></td>
  </tr>

</table>



</html:form>

<%session.removeAttribute("payHeader");%>

</body>
</html>


<%@page import="org.egov.payroll.utils.PayrollExternalInterface"%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file="/includes/taglibs.jsp" %>
<%@ page
	import="org.egov.payroll.dao.*,java.util.*,org.egov.infstr.*,org.egov.commons.dao.*,org.egov.commons.*,org.egov.payroll.model.*,org.egov.infstr.utils.EgovMasterDataCaching,org.egov.payroll.utils.PayrollExternalImpl"%>

<html>
<head>
<title>Payslip approve criteria</title>


	<%
		List years = new ArrayList();
	PayrollExternalInterface payrollExternalInterface=new PayrollExternalImpl();
	    years = payrollExternalInterface.getAllActiveFinancialYearList();
		ArrayList deptlist=(ArrayList)EgovMasterDataCaching.getInstance().get("egi-dept");
	%>
<c:set var="deptlist" value="<%=deptlist%>" scope="page" />
<script language="JavaScript" type="text/JavaScript">

	function checkAlphaNumeric(obj){
   		if(obj.value!=""){
   			var num=obj.value;
   			var objRegExp  = /^([a-zA-Z0-9.()\ ]+)/i;
    		if(!objRegExp.test(num)){
	   			alert('please enter Valid name');
    			obj.value="";
    			obj.focus();
    		}
    	}
   }


	function checkNumber(obj)
	{

		if(obj.value!="" && obj.value!=null)
		{
			if(isNaN(obj.value))
			{
			alert('Please Enter Only Numeric Values');
			obj.value="";
			obj.focus();
			return false;
				}
		}

		return true;
	}

	function checkOnSubmit()
	{
		if(document.showPayslipForm.deptId.value=="-1"){
			alert("Select deprtment");
			document.showPayslipForm.deptId.focus();
			return false;
		}
		if(document.showPayslipForm.year.value==""){
			alert("Enter Year");
			document.showPayslipForm.year.focus();
			return false;
		}
		if(document.showPayslipForm.month.value==""){
			alert("Enter Month");
			document.showPayslipForm.month.focus();
			return false;
		}
	}


</script>

</head>

<body>

  
<html:form action="/payslipapprove/showPayslip">

	
	<table style="width: 750;" colspan="6" align="center" cellpadding="0" cellspacing="0" border="0" id="paytable">
		<tr>
			<td colspan="6" height=30 bgcolor=#bbbbbb align=middle class="tablesubcaption">
			<p align="center"><b>Enter the following informations &nbsp;&nbsp;&nbsp;</b>
			</td>
		</tr>
		<tr>
			<td height="15" class="tablesubcaption" colspan="6" align="center">(<font color="red">*</font> <font size="1">indicates required fields )</td>
		</tr>
		<tr>
			<td class="labelcell">Year<font color="red">*</font></td>
			<td class="labelcell"><html:select property="year">
					<html:option value="">-----------Select------------</html:option>
				  	<%
				  		for(int i=0;i<years.size();i++){
				  			CFinancialYear year = (CFinancialYear)years.get(i);
					%>
						<html:option value="<%=year.getFinYearRange()%>"><%=year.getFinYearRange()%></html:option>

					<%
						}
					%>
				</html:select>
			</td>
		</tr>
		<tr>
			<td class="labelcell">Month<font color="red">*</font></td>
			<td class="labelcell">
				<html:select property="month">
				<html:option value="">-----------Select------------</html:option>
				<html:option value="1">JAN</html:option>
				<html:option value="2">FEB</html:option>				
				<html:option value="3">MARCH</html:option>
				<html:option value="4">APRIL</html:option>
				<html:option value="5">MAY</html:option>
				<html:option value="6">JUNE</html:option>
				<html:option value="7">JULY</html:option>
				<html:option value="8">AUG</html:option>
				<html:option value="9">SEPT</html:option>
				<html:option value="10">OCT</html:option>
				<html:option value="11">NOV</html:option>
				<html:option value="12">DEC</html:option>
				</html:select>
			</td>
		</tr>
		<tr>
	  		<td class="labelcell">Depertment<font color="red">*</font></td>
			<td class="labelcell">
			<html:select property="deptId">
				<html:option value="-1">-----------Select------------</html:option>
				<c:forEach var="dept" items="${deptlist}">
				<html:option value="${dept.id}" >${dept.name}</html:option>
				</c:forEach>
			</html:select>
		   	</td>
		</tr>

	</table>
	<br>
	<p style="text-align: center"><html:submit value="Submit" onclick="return checkOnSubmit();" />

</html:form>
</body>
</html>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>


<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>No payslip</title>
	<LINK REL=stylesheet HREF="/css/egov.css" TYPE="text/css">
	<script type="text/JavaScript" src="/javascript/calender.js"></script>
	<script language="JavaScript" src="/dhtml.js" type="text/JavaScript"></script>
	<script language="JavaScript" src="/javascript/SASvalidation.js" type="text/JavaScript"></script>
	<script language="JavaScript" src="/javascript/dateValidation.js" type="text/JavaScript"></script>
	<script language="JavaScript"  type="text/JavaScript">


	

</script>
<body>

<html:form action ="/payslipapprove/payslipInfo">
	
	<table style="width: 1000;"  align="center" cellpadding="0" cellspacing="0" border="1" id="paytable">
		<tr>
			<td class="tablesubcaption" ><p align="center">Employee Code</a></p></td>		
			<td class="tablesubcaption"><p align="center">Employee Name</a></p></td>			
			<td class="tablesubcaption"><p align="center">Designation</a></p></td>					    
			<td class="tablesubcaption"><p align="center">Fund</p></td>
			<td class="tablesubcaption"><p align="center">Function</p></td>
			<td class="tablesubcaption"><p align="center">Field</p></td>			
			<logic:notEmpty name="payslipInfoForm" property="empPayroll.earningses">
				<logic:iterate name="payslipInfoForm" property="empPayroll.earningses" id="earning">
					<td class="tablesubcaption">
						<p align="center"><bean:write name="earning" property="salaryCodes.head"/>	</p>					
					</td>
				</logic:iterate>
			</logic:notEmpty>
			<td class="tablesubcaption"><p align="center">Gross </p></td>			
			<logic:notEmpty name="payslipInfoForm" property="empPayroll.deductionses">
				<logic:iterate name="payslipInfoForm" property="empPayroll.deductionses" id="deduction">
					<logic:notEmpty name="deduction" property="salaryCodes"	>
					<td class="tablesubcaption" align="center">
						<p align="center"><bean:write name="deduction" property="salaryCodes.head"/></p>						
					</td>
					</logic:notEmpty>					
				</logic:iterate>
				<logic:iterate name="payslipInfoForm" property="empPayroll.deductionses" id="otherDeduction">
					<logic:notEmpty name="otherDeduction" property="chartofaccounts"	>
					<td class="tablesubcaption" align="center">
						<p align="center"><bean:write name="otherDeduction" property="chartofaccounts.name"/></p>						
					</td>
					</logic:notEmpty>					
				</logic:iterate>
			</logic:notEmpty>
			<td class="tablesubcaption"><p align="center">Net </p></td>			
		</tr>
		<tr>
						 		
	   		<td class="labelcell"><bean:write name="payslipInfoForm" property="empPayroll.employee.employeeCode"/></td>
	   		<td class="labelcell"><bean:write name="payslipInfoForm" property="empPayroll.employee.employeeFirstName"/></td>
	   		<td class="labelcell"><bean:write name="payslipInfoForm" property="empPayroll.empAssignment.desigId.designationName"/></td>
	   		<td class="labelcell">
	   			<logic:notEmpty name="payslipInfoForm" property="empPayroll.empAssignment.fundId">
	   				<bean:write name="payslipInfoForm" property="empPayroll.empAssignment.fundId.name"/>
	   			</logic:notEmpty>
	   		</td>
	   		<td class="labelcell">
	   			<logic:notEmpty name="payslipInfoForm" property="empPayroll.empAssignment.functionId">
		   			<bean:write name="payslipInfoForm" property="empPayroll.empAssignment.functionId.name"/>
		   		</logic:notEmpty>	
		   	</td>
	   		<td class="labelcell"></td>  
	   		<logic:notEmpty name="payslipInfoForm" property="empPayroll.earningses">
				<logic:iterate name="payslipInfoForm" property="empPayroll.earningses" id="earning">
					<td class="labelcell" align="right">
						<bean:write name="earning" property="amount"/>						
					</td>
				</logic:iterate>
			</logic:notEmpty>	
    		<td class="labelcell" align="right"><bean:write name="payslipInfoForm" property="empPayroll.grossPay"/></td>
			<logic:notEmpty name="payslipInfoForm" property="empPayroll.deductionses">
				<logic:iterate name="payslipInfoForm" property="empPayroll.deductionses" id="deduction">
					<logic:notEmpty name="deduction" property="salaryCodes"	>
					<td class="labelcell" align="right">
						<bean:write name="deduction" property="amount"/>						
					</td>
					</logic:notEmpty>					
				</logic:iterate>
				<logic:iterate name="payslipInfoForm" property="empPayroll.deductionses" id="otherDeduction">
					<logic:notEmpty name="otherDeduction" property="chartofaccounts"	>
					<td class="labelcell" align="right">
						<bean:write name="otherDeduction" property="amount"/>						
					</td>
					</logic:notEmpty>					
				</logic:iterate>
			</logic:notEmpty>
			<td class="labelcell" align="right"><bean:write name="payslipInfoForm" property="empPayroll.netPay"/></td>
		</tr>
	</table>
	
</html:form>
</body>
</html>


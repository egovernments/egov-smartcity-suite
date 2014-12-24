<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="org.egov.payroll.dao.*,java.util.*,org.egov.infstr.*,org.egov.infstr.commons.dao.*,org.egov.infstr.commons.* "%>
<%@ include file="/includes/taglibs.jsp" %>

<html>
<head>
	<title>Salary Advance Created</title>
<script language="JavaScript"  type="text/JavaScript">

function printpage(obj)
{  
  obj.style.visibility = "hidden";
  window.print();
  obj.style.visibility = "visible";
}
 
</script>
</head>

<body>	
	<table  style="width: 700;" colspan="6" cellpadding ="0" cellspacing ="0" border = "1" id="employee">
		<tr>
			<td colspan="6" height="30"  bgcolor="#bbbbbb" align="middle" class="tablesubcaption">
				
			Advance	
		<!-- "<c:out value="${salaryadvance.salaryCodes.head}"/>" for 
							<c:out value="${salaryadvance.employee.employeeFirstName}"/> 	-->
			
				
			</td>
		</tr>
	    <tr>
	 	 <input type="hidden" name="employeeCodeId" id="employeeCodeId">
	  		<td class="labelcell">Employee Code</td>
	  		<td class="labelcell"><c:out value="${advanceForm.advance.employee.employeeCode}"/></td>
		  	<td class="labelcell">Employee Name</td>
		  	<td class="labelcell">
		  		<c:out value="${advanceForm.advance.employee.employeeFirstName}"/>&nbsp; 
		  		<c:if test="${advanceForm.advance.employee.employeeMiddleName != null}">
		  			<c:out value="${advanceForm.advance.employee.employeeMiddleName}"/>&nbsp; 
		  		</c:if>	
		  		<c:out value="${advanceForm.advance.employee.employeeLastName}"/>		  		
		  	</td>
	    </tr>	  	
	    <tr>
	    	<td class="labelcell" colspan="4" >&nbsp;</td>	    	
	    </tr>
		<c:if test="${advanceForm.advance.salaryARF!=null}">
			<tr>	 	 
		  		<td class="labelcell"><b><bean:message key="Advance.ARFNo"/></b></td>
		  		<td class="labelcell">
		  		<c:out value="${advanceForm.advance.salaryARF.advanceRequisitionNumber}"/>
		  		</td>	  		
				<td class="labelcell"><b><bean:message key="Advance.ARFStatus"/></b></td>
		  		<td class="labelcell">
		  			<c:out value="${advanceForm.advance.salaryARF.status.code}"/>
		  		</td>							
		    </tr>
		</c:if>
		<tr>	 	 
	  		<td class="labelcell"><bean:message key="Advance"/></td>
	  		<td class="labelcell">
	  		<c:out value="${advanceForm.advance.salaryCodes.head}"/>
	  		</td>
	  		<td class="labelcell"><bean:message key="AdvanceType"/> </td>
	  		<td class="labelcell">
	  		<c:out value="${advanceForm.advance.advanceType}"/>
	  		</td>
	    </tr>
		<tr>
			<td class="labelcell"><bean:message key="AdvanceAmount"/> </td>
			<td class="labelcell" >
			<c:out value="${advanceForm.advance.advanceAmt}"/>
			</td>
			<td class="labelcell"><bean:message key="Interest%"/> </td>
			<td class="labelcell" >
				<c:out value="${advanceForm.advance.interestPct}"/>
			</td>
		</tr>	
		<tr>
			<td class="labelcell" colspan="2">&nbsp;</td>
			<td class="labelcell"><bean:message key="InterestType"/> </td>
			<td class="labelcell">
				<c:out value="${advanceForm.advance.interestType}"/>
			</td>
		</tr>	
		<tr>
			<td class="labelcell" colspan="2">&nbsp;</td>
			<td class="labelcell"><bean:message key="NumberOfInstallment"/>   </td>
			<td class="labelcell" >
				<c:out value="${advanceForm.advance.numOfInst}"/>
			</td>
		</tr>	
		<tr>
			<td class="labelcell"><bean:message key="InterestAmount"/>  </td>
			<td class="labelcell" >
				<c:out value="${advanceForm.advance.interestAmt}"/>
			</td>		
			<td class="labelcell"><bean:message key="Bank"/></td>
			<td class="labelcell">
				<c:if test="${salaryadvance.salaryCodes.tdsId != null}"	>
					<c:out value="${advanceForm.advance.salaryCodes.tdsId.bank.name}"/>
				</c:if>					
			</td>
		</tr>			
		<tr>
			<td class="labelcell"><bean:message key="TotalAmount"/> </td>
			<td class="labelcell" >
				<c:out value="${advanceForm.advance.pendingAmt}"/>
			</td>
			<td class="labelcell" ></td>
			<td class="labelcell" >				
			</td>	
		</tr>	
		<tr>
	    	<td class="labelcell" colspan="4" >&nbsp;</td>	    	
	    </tr>		
		<tr>
			<td class="labelcell"><bean:message key="MonthlyPayment"/> </td>
			<td class="labelcell" >
				<c:out value="${advanceForm.advance.instAmt}"/>
			</td>	
			<td class="labelcell"><bean:message key="PaymentMethod"/> </td>
			<td class="labelcell">
			<c:out value="${advanceForm.advance.paymentType}"/>
			</td>			
		</tr>	

  </table>	
	<c:if test="${advanceForm.advance.maintainSchedule!=null && advanceForm.advance.maintainSchedule=='Y'}" >

	<center>
	<table style="width: 700;" border="0" cellpadding="3" cellspacing="0" id="dtlHeading">
		<tr class="tableheader">
			<td colspan="6" height="30"  bgcolor="#bbbbbb" align="middle" class="tablesubcaption">Advance Installment Schedule</td>
        </tr>
	</table>
	</center>
	<table  style="width: 450;" border="0" cellpadding="0" cellspacing="0" id="advanceScheduleTblId" name="advanceScheduleTblName">
       <tr class="tableheader">
			<td class="thStlyle" align="left"><bean:message key="AdvSchInstallmentNo"/></td>
			<td class="thStlyle" align="right"><bean:message key="AdvSchPrincipalAmt"/></td>
			<td class="thStlyle" align="right"><bean:message key="AdvSchInterestAmt"/></td>
       </tr>
  				
		<c:forEach var="advanceSchedule" items="${advanceForm.advance.advanceSchedules}">
 			<tr id="row">
				<td class="labelcell" align="left"><c:out value="${advanceSchedule.installmentNo}"/></td>
				<td class="labelcell" align="right"><c:out value="${advanceSchedule.principalAmt}"/></td>
				<td class="labelcell" align="right"><c:out value="${advanceSchedule.interestAmt}"/></td>
			</tr>
		</c:forEach>
  	   
	</table>
</c:if>
	<table>
		<tr>
		<td>
			 <input type="button" name="printButton" onclick="printpage(this)" value="PRINT" />
		</td>
		</tr>

	</table>
</body>
</html>

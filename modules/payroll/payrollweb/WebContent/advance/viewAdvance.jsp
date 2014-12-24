<%@ include file="/includes/taglibs.jsp" %>
<%@ page import="org.egov.payroll.utils.PayrollConstants" %>
<%@page import="java.util.*" %>
<html>
<head>
	<title>View salary advance </title>

	<script language="JavaScript"  type="text/JavaScript">
		function printpage(obj){  
		  obj.style.visibility = "hidden";
	  	  window.print();
		  obj.style.visibility = "visible";
	  	}
	
	</script>
</head>

<body >
	<html:form  action="/salaryadvance/afterModifyAdvance">	   
		<html:hidden name="advanceForm" property="salaryadvanceId" value="${salaryadvance.id}"/>
		<html:hidden name="advanceForm" property="employeeCodeId" value="${salaryadvance.employee.idPersonalInformation}" />

		<table  width="95%" cellpadding ="0" cellspacing ="0" border = "0" id="employee">
			<tr>
			<td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
			  <div class="headplacer">Advance </div></td>
		    </tr>
			<tr>
				<td height="15" class="tablesubcaption" colspan="6" align="center"></td>
			</tr>

		    <tr>
				<td class="greyboxwk" rowspan="2"><b><bean:message key="EmployeeCode"/></b></td>
				<td class="greybox2wk">  
				  <c:out value="${salaryadvance.employee.employeeCode}"/>		
				</td>
				<td class="greyboxwk"><b><bean:message key="EmployeeName"/></b></td>
				<td class="greybox2wk">
					<c:out value="${salaryadvance.employee.employeeName}"/>

				</td>
		    </tr>
		</table> 	
		<br>

		<table  align="center" width="80%" cellpadding ="0" cellspacing ="0" border = "0" id="advance">

			<c:if test="${salaryadvance.salaryARF!=null}">
				<tr>	 	 
					<td class="whiteboxwk"><b><bean:message key="Advance.ARFNo"/></b></td>
					<td class="whitebox2wk">
						<c:out value="${salaryadvance.salaryARF.advanceRequisitionNumber}"/>
					</td>	
					<td class="whiteboxwk"><b><bean:message key="Advance.ARFStatus"/></b></td>
					<td class="whitebox2wk">
						<c:out value="${salaryadvance.salaryARF.status.code}"/>
					</td>
			    </tr>
			    <c:if test="${salaryadvance.salaryARF.egAdvanceReqMises.voucherheader!=null}">
				    <tr>
						<td class="whiteboxwk"><b><bean:message key="VoucherNo"/></b></td>
						<td class="whitebox2wk">
							<c:out value="${salaryadvance.salaryARF.egAdvanceReqMises.voucherheader.voucherNumber}"/>
						</td>
						<td class="whiteboxwk" colspan="2"></td>
					</tr>
				</c:if>
		    </c:if>
		    <c:if test="${salaryadvance.salaryARF==null}">
				<tr>	 	 
					<td class="whiteboxwk"><b>Status</b></td>
					<td class="whitebox2wk">
						<c:out value="${salaryadvance.status.code}"/>
					</td>
					<td class="whiteboxwk" colspan="2"></td>						
			    </tr>
		    </c:if>

			<tr>	 	 
				<td class="whiteboxwk"><b><bean:message key="Advance"/></b></td>
				<td class="whitebox2wk">
				<c:out value="${salaryadvance.salaryCodes.head}" />
				</td>
				<td class="whiteboxwk"><b><bean:message key="AdvanceType"/></b> </td>
				<td class="whitebox2wk">
				<c:out value="${salaryadvance.advanceType}" />
				</td>
		    </tr>
			<tr>
				<td class="whiteboxwk"><b><bean:message key="PrevPendingAmt"/></b> </td>
				<td class="whitebox2wk">
				<c:out value="${salaryadvance.previousPendingAmt}" />			
				</td>
				<td class="whiteboxwk"><b><bean:message key="Interest%"/> </b> </td>
				<td class="whitebox2wk">
					<c:out value="${salaryadvance.interestPct}" />				
				</td>
			</tr>	
			<tr>
				<td class="whiteboxwk"><b><bean:message key="AdvanceAmount"/></b> </td>
				<td class="whitebox2wk">
				<c:out value="${salaryadvance.advanceAmt}" />			
				</td>
				<td class="whiteboxwk"><b><bean:message key="InterestType"/></b> </td>
				<td class="whitebox2wk">
					<c:out value="${salaryadvance.interestType}" />
				</td>
			</tr>	
			<tr>
				<td class="whiteboxwk"></td>
				<td class="whitebox2wk"></td>
				<td class="whiteboxwk"><b><bean:message key="NumberOfInstallment"/></b>  </td>
				<td class="whitebox2wk">
					<c:out value="${salaryadvance.numOfInst}" />				
				</td>
			</tr>	
			<tr>
				<td class="whiteboxwk"><b><bean:message key="InterestAmount"/></b> </td>
				<td class="whitebox2wk">
					<c:out value="${salaryadvance.interestAmt}" />				
				</td>	
				<td class="whiteboxwk" id="bankBranchText" ><b><bean:message key="Bank"/></b></td>
				<td class="whitebox2wk">
					<c:if test="${salaryadvance.salaryCodes != null}">
					<c:if test="${salaryadvance.salaryCodes.tdsId != null}">
					<c:if test="${salaryadvance.salaryCodes.tdsId.bank != null}">
						<c:out value="${salaryadvance.salaryCodes.tdsId.bank.name}" />
					</c:if>
					</c:if>
					</c:if>

				</td>
			</tr>			
			<tr>
				<td class="whiteboxwk" colspan="1"><b><bean:message key="TotalAmount"/></b> </td>
				<td class="whitebox2wk" colspan="1">
					<c:out value="${salaryadvance.advanceAmt + salaryadvance.interestAmt + salaryadvance.previousPendingAmt}" />			
				</td>	

				<td class="whiteboxwk"></td>
				 <td class="whitebox2wk" colspan="1"></td>
				</td>		
			</tr>	

			<tr>
				<td class="whiteboxwk"><b><bean:message key="MonthlyPayment"/></b> </td>
				<td class="whitebox2wk">
					<c:out value="${salaryadvance.instAmt}" />			
				</td>	
				<td class="whiteboxwk"><b><bean:message key="PaymentMethod"/> </b></td>
				<td class="whitebox2wk">
					<c:out value="${salaryadvance.paymentType}" />
				</td>		
			</tr>
			<tr>
			<td class="whiteboxwk" colspan="4" >&nbsp;</td>	    	
		    </tr>	


	  </table>	

	<c:if test="${salaryadvance.maintainSchedule!=null && salaryadvance.maintainSchedule=='Y'}" >

		<center>
		<table  width="80%" border="0" cellpadding="0" cellspacing="0" id="dtlHeading">
			<tr>
				<td colspan="4" class="headingwk" >
					<div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
			<div class="headplacer">Advance Installment Schedule</div>
				</td>
		</tr>

		</table>
		</center>
		<table  align="center" width="80%" border="0" cellpadding="0" cellspacing="0" id="advanceScheduleTblId" name="advanceScheduleTblName">
	       <tr class="tableheader">
				<td class="tablesubheadwk"><bean:message key="AdvSchInstallmentNo"/></div></td>
				<td class="tablesubheadwk"><bean:message key="AdvSchPrincipalAmt"/></div></td>
				<td class="tablesubheadwk"><bean:message key="AdvSchInterestAmt"/></div></td>
				<td class="tablesubheadwk"><bean:message key="AdvSchRecover"/></div></td>
				<td class="tablesubheadwk"><bean:message key="AdvSchPayslip"/></div></td>
	       </tr>

			<c:forEach var="advanceSchedule" items="${salaryadvance.advanceSchedules}">
				<tr id="row">
					<td class="whitebox3wk"><c:out value="${advanceSchedule.installmentNo}"/></td>
					<td class="whitebox3wk"><c:out value="${advanceSchedule.principalAmt}"/></td>
					<td class="whitebox3wk"><c:out value="${advanceSchedule.interestAmt}"/></td>
					<td class="whitebox3wk"><c:out value="${advanceSchedule.recover}"/></td>
					<td class="whitebox3wk">
						<c:if test="${advanceSchedule.recover!=null && advanceSchedule.recover=='Y'}">
							<c:out value="${advanceSchedule.egPayDeductions.empPayroll.id}" /> (
							<c:if test="${advanceSchedule.egPayDeductions.empPayroll.month==1}">Jan</c:if>
							<c:if test="${advanceSchedule.egPayDeductions.empPayroll.month==2}">Feb</c:if>
							<c:if test="${advanceSchedule.egPayDeductions.empPayroll.month==3}">Mar</c:if>
							<c:if test="${advanceSchedule.egPayDeductions.empPayroll.month==4}">Apr</c:if>
							<c:if test="${advanceSchedule.egPayDeductions.empPayroll.month==5}">May</c:if>
							<c:if test="${advanceSchedule.egPayDeductions.empPayroll.month==6}">Jun</c:if>
							<c:if test="${advanceSchedule.egPayDeductions.empPayroll.month==7}">Jul</c:if>
							<c:if test="${advanceSchedule.egPayDeductions.empPayroll.month==8}">Aug</c:if>
							<c:if test="${advanceSchedule.egPayDeductions.empPayroll.month==9}">Sep</c:if>
							<c:if test="${advanceSchedule.egPayDeductions.empPayroll.month==10}">Oct</c:if>
							<c:if test="${advanceSchedule.egPayDeductions.empPayroll.month==11}">Nov</c:if>
							<c:if test="${advanceSchedule.egPayDeductions.empPayroll.month==12}">Dec</c:if>
							<fmt:formatDate value="${advanceSchedule.egPayDeductions.empPayroll.fromDate}" pattern="yyyy" />)
						</c:if>
					</td>
				</tr>
			</c:forEach>
		</table>
	</c:if>
	<table align='center' id="table2">	
		<tr>
		<td class="labelcell">
			<input type="button" class="buttonfinal" name="printButton" onclick="printpage(this)" value="PRINT" />
			</td>    
	    </tr>	
	</table>	
	</html:form>
	</body>
</html>
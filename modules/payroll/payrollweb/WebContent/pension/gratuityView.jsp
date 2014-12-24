<%@ include file="/includes/taglibs.jsp" %>
<%@page import="java.util.*" %>
<html>
<head>

	<title>Gratuity View </title>

	

<%
	String mode = request.getParameter("mode");
	System.out.println("mode----------"+mode);
	request.setAttribute("mode",mode);
%>
<script language="JavaScript"  type="text/JavaScript">
	
	
	


</script>

</head>
<body >
<html:form action ="/pension/gratuityAction" >
 	
 	<center>
		<div class="formmainbox">
			<div class="insidecontent">
		  		<div class="rbroundbox2">
					<div class="rbtop2">
						<div>
						</div>
					</div>
					<div class="rbcontent2">
						<div class="datewk">	
						    <span class="bold"></span>
							</div>
 	
 		<input type="hidden" name="checkEmpCode"/>
		<input type="hidden" name="pensionDetailsId" value="${gratuityForm.pensionDetails.id}"/>
 	
  <table width="95%" cellpadding="0" cellspacing="0" border="0" align="center" id="employeeTable">
 	<tr>
       	<td colspan="4" class="headingwk">
         	<div class="arrowiconwk">
         		<img src="../common/image/arrow.gif" />
         	</div>
           	<div class="headplacer">
	           	View Gratuity
			</div>
         	</td>
    </tr>
   	  
   	 <tr>
	    <input type="hidden" name="employeeCodeId" id="employeeCodeId" value="${gratuityForm.employeeCodeId}" />
	 	<td class="whiteboxwk">Employee Code<font color="red">*</font></td>
	  	<td class="whiteboxwk">
	  		<input type="text"  class="fieldcell" name="employeeCode" id ="employeeCode" value="${gratuityForm.pensionDetails.pensionHeader.employee.employeeCode }"  readOnly/>
	  	</td>
	  	<td class="whiteboxwk">Employee Name</td>
	  	<td class="whiteboxwk">
		  	<input type="text"  class="fieldcell" name="employeeName" id ="employeeName" value="${gratuityForm.pensionDetails.pensionHeader.employee.employeeName }" readOnly />
	  	</td>
    </tr>
  	    
  	<tr>	    
	 	<td class="greyboxwk">Designation<font color="red">*</font></td>
	  	<td class="greyboxwk">
	  		<input type="text"  class="fieldcell" name="designation" id ="designation" value="${gratuityForm.designation}" readOnly />
	  	</td>
	  	<td class="greyboxwk">Department</td>
	  	<td class="greyboxwk">
			
		  	<input type="text"  class="fieldcell" name="department" id ="department" value="${gratuityForm.department}" readOnly />
	  	</td>
    </tr>  
    <tr>	    
	 	<td class="whiteboxwk">Birth Date</td>
	  	<td class="whiteboxwk">
	  		<input type="text"  class="fieldcell" name="birthDate" id ="birhtDate" value="${gratuityForm.birthDate}" readOnly />
	  	</td>
	  	<td class="whiteboxwk">Superannuation Date</td>
	  	<td class="whiteboxwk">
		  	<input type="text"  class="fieldcell" name="supperannuationDate" id ="supperannuationDate" value="${gratuityForm.supperannuationDate}" readOnly />
	  	</td>
    </tr>  
	
	<tr>	    
	 	<td class="greyboxwk">Disbursement Type</td>
	  	<td class="greyboxwk">
			<c:if test = "${gratuityForm.pensionDetails.pensionHeader.disbursementType == 'dbt'}">
				<input type="text" name="disbursementType" value="Direct bank transfer" readOnly/>
			</c:if>	
			<c:if test = "${gratuityForm.pensionDetails.pensionHeader.disbursementType == 'cash'}">
				<input type="text" name="disbursementType" value="Cash" readOnly/>
			</c:if>
			<c:if test = "${gratuityForm.pensionDetails.pensionHeader.disbursementType == 'cheque'}">
				<input type="text" name="disbursementType" value="Cheque" readOnly/>
			</c:if>
	  	</td>	  	
		<td class="greyboxwk"> </td>
		<td class="greyboxwk"> </td>
    </tr>  
	<tr id="bankRowId" style="display:none">	    
	 	<td class="whiteboxwk">Bank</td>
	  	<td class="whiteboxwk">
			<input type="text" name="bank" value="${gratuityForm.pensionDetails.pensionHeader.idBranch.bank.name}" readOnly/>
	  	</td>	
		<td class="whiteboxwk"> </td>
		<td class="whiteboxwk"> </td>
	</tr>
   	<tr id="bankBranchRowId" style="display:none">	    
	 	<td class="greyboxwk">Bank Branch</td>
	  	<td class="greyboxwk">
			<input type="text" name="bankBranch" value="${gratuityForm.pensionDetails.pensionHeader.idBranch.branchname}" readOnly/>
	  	</td>	  	
	  	<td class="greyboxwk">Bank Account</td>
	  	<td class="greyboxwk">
	  		<input type="text"  class="fieldcell" name="bankAccount" value="${gratuityForm.pensionDetails.pensionHeader.accountNumber}" readOnly/>
	  	</td>
    </tr> 	
    <tr>	    
	 	<td class="whiteboxwk">Pension Sanction Number</td>
	  	<td class="whiteboxwk">
	  		<html:text  property="pensionSanctionNumber" readonly="true"/>
	  	</td>	  	
	  	<td class="whiteboxwk">Sanction Authority</td>
	  	<td class="whiteboxwk">
	  		<html:text  property="pensionSanctionAuthority" readonly="true"/>
	  	</td>
    </tr> 
	
    <tr>	    
	 	<td class="greyboxwk">Pension Number(PPO)</td>
	  	<td class="greyboxwk">
	  		<html:text  property="pensionNumber" readonly="true"/>
	  	</td>
		<td class="greyboxwk"></td>
	  	<td class="greyboxwk">
	  		
	  	</td>
    </tr>   
	
    <tr>	    
	 	<td class="whiteboxwk">Basic Pay</td>
	  	<td class="whiteboxwk">
	  		<input type="text"  class="fieldcell" name="currentBasicPay" value="${gratuityForm.currentBasicPay}" readOnly />
	  	</td>	  	
	</tr>

<!--	<tr> 	
	  	<td class="labelcell">Basic Component</td>
	  	<td class="labelcell">
	  		<input type="text"  class="fieldcell" name="basicComponentPercent" value="${gratuityForm.basicComponentPercent}" readOnly/>
		</td>
		<td class="labelcell">			
			<input type="text"  class="fieldcell" name="basicComponent" value="${gratuityForm.basicComponent}" readOnly/>
	  	</td>
	 </tr>	-->
	 <tr> 	
	  	<td class="greyboxwk">DA(%)</td>
	  	<td class="greyboxwk">
	  		<input type="text"  class="fieldcell" name="daComponentPercent"  value="${gratuityForm.daComponentPercent}" readOnly/>
		</td>
		<td class="greyboxwk">Computed DA</td>
		<td class="greyboxwk">
			<input type="text"  class="fieldcell" name="daComponent" value="${gratuityForm.daComponent}" readOnly/>
	  	</td>	  	
	 </tr> 
<!--	 <tr> 	
	  	<td class="labelcell">Pension Eligible</td>
		<td class="labelcell">
			&nbsp;
	  	</td>
	  	<td class="labelcell">
	  		<input type="text"  class="fieldcell" name="pensionEligible" value="${gratuityForm.pensionEligible}" readOnly/>
	  	</td>	  	
	 </tr> 	-->
	 
	<!--  <tr> 	
	  	<td class="labelcell"><b>Commute Details</b></td>
	 </tr> 
	 <tr> 	
	  	<td class="labelcell" align="right">Pension Commuted(%)</td>
	  	<td class="labelcell">
	  		<input type="text"  class="fieldcell" name="pensionCommutedPercent" value="${gratuityForm.pensionCommutedPercent}" readOnly/>
	  	</td>	 
		<td class="labelcell">
	  		<input type="text"  class="fieldcell" name="pensionCommuted" value="${gratuityForm.pensionCommuted}" readOnly/>
	  	</td>
	 </tr> 
	 <tr> 	
	  	<td class="labelcell" align="right">Commuted Period(years)</td>
	  	<td class="labelcell">
	  		<input type="text"  class="fieldcell" name="commutePeriod"  value="${gratuityForm.commutePeriod}" readOnly/>
	  	</td>	  	
	 </tr> 
	 <tr> 	
	  	<td class="labelcell" align="right">Commuted Amount</td>
	  	<td class="labelcell">
	  		<input type="text"  class="fieldcell" name="commuteAmount" value="${gratuityForm.commuteAmount}" readOnly/>
	  	</td>	  	
	 </tr> 	 -->
	
	 <tr> 	
	  	<td class="whiteboxwk" align="right">Gratuity Amount</td>
	  	<td class="whiteboxwk">
	  		<input type="text"  class="fieldcell" name="gratuityAmount"  value="${gratuityForm.gratuityAmount}" readOnly/>
	  	</td>	  	 
	 </tr> 		
	 
	<!--  <tr> 	
	  	<td class="labelcell" align="right">Total Payment(Gross)</td>
	  	<td class="labelcell">
	  		<input type="text"  class="fieldcell" name="totalPayment" value="${gratuityForm.totalPayment}" readOnly/>
	  	</td>	  	
	 </tr>--> 	
	  
	 <tr>
		<td class="greyboxwk">Monthly Pension Payable</td>
		<td class="greyboxwk">
			<input type="text"  class="fieldcell" name="monthlyPensionPayable" id="monthlyPensionPayable" value="${gratuityForm.pensionDetails.monthlyPensionAmount}" onblur="populateNomineeAmount();" readOnly/>
		</td>
	 </tr>

	<!--  <tr> 	
	  	<td class="labelcell">Disburse Gratuity to</td>
	  	<td class="labelcell">
			<c:if test = "${gratuityForm.payTo == 'nominee'}">
				Nominee(s)
			</c:if>	
			<c:if test = "${gratuityForm.payTo == 'employee'}">
				Employee
			</c:if>					
			<input type="hidden" name="payTo" value="${gratuityForm.payTo}"  />	
	  		
	  	</td>	  	
	 </tr>	-->


<!--  <table style="width: 960;" align="center" cellpadding="0" cellspacing="0" border="1" id="nomineeTable" style="display: none">
			 <tr> 	
		  		<td class="labelcell"><b>Nominee Details</b></td>
		  	 </tr>
		  	 <tr>	
		  		<td class="labelcell" align="right">Name</td>
		  		<td class="labelcell" align="right">Amount</td>
				<td class="labelcell" align="right">Monthly Pension Payable</td>
		   	 </tr>		

			 <c:forEach var="eligibleNomineeObj" items="${gratuityForm.eligibleNomineeSet}">
				 <tr id="nomineeRowId">
					<td class="labelcell">
						<input type="hidden"  name="nomineeId" value="${eligibleNomineeObj.id}" />
						<input type="text" class="fieldcell" name="nomineeName" value="${eligibleNomineeObj.firstName}" readOnly/>	
					</td>
					<td class="labelcell">
						<input type="text"  class="fieldcell" name="nomineeAmount"  value="${eligibleNomineeObj.grossPayAmount}" readOnly />
					</td>
					<td class="labelcell">
						<input type="text"  class="fieldcell" name="nomineeMonthlyPensionPayable" value="${eligibleNomineeObj.monthlyPensionAmount}"  readOnly/>
					</td>			
				  </tr> 
			</c:forEach>	  
		</table>
		<table style="width: 960;" align="center" cellpadding="0" cellspacing="0" border="1" id="nomineeTotalTable" style="display: none">
			 <tr>	
		  		<td class="labelcell"><b>Total</b></td>
		  		<td class="labelcell">
		  			<input type="text"  class="fieldcell" name="total" id="total" value="${gratuityForm.totalPayment}"readOnly/>
				</td>				
				<td class="labelcell">
		  			<input type="text"  class="fieldcell" name="totalMonthlyPensionPayable" id="totalMonthlyPensionPayable"  value="${gratuityForm.monthlyPensionPayable}" readOnly/>
				</td>				
		   	 </tr>
		</table>		
-->		
	
	 	 
   </table>

	
	</div>	
 	<div class="rbbot2"><div/></div>
	</div>
	</div>
	</div>
	<div class="buttonholderwk">
		
	</div>
				<div class="urlwk">City Administration System Designed and Implemented by <a href="http://www.egovernments.org/">eGovernments Foundation</a> All Rights Reserved </div>
	</center>	
   
</html:form>
</body>
</html>

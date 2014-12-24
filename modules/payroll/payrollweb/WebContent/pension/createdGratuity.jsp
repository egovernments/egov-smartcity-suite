<%@ include file="/includes/taglibs.jsp" %>
<%@page import="java.util.*" %>
<html>
<head>

	<title>Create Employee Gratuity </title>

	<style type="text/css">
		#codescontainer {position:absolute;left:11em;width:9%}
		#codescontainer .yui-ac-content {position:absolute;width:80%;border:1px solid #404040;background:#fff;overflow:hidden;z-index:9050;}
		#codescontainer .yui-ac-shadow {position:absolute;margin:.3em;width:40%;background:#a0a0a0;z-index:9049;}
		#codescontainer ul {padding:5px 0;width:80%;}
		#codescontainer li {padding:0 5px;cursor:default;white-space:nowrap;}
		#codescontainer li.yui-ac-highlight {background:#ff0;}
		#codescontainer li.yui-ac-prehighlight {background:#FFFFCC;}
</style>

<script language="JavaScript"  type="text/JavaScript">
	
	var yuiflag1 = new Array();
	var selectedEmpCode;
	
	function onBodyLoad(){  
		

	} 	

	function callBank(obj){
		document.getElementById("bankRowId").style.display = "none";
		document.getElementById("bankBranchRowId").style.display = "none";
		if(obj.value == "dbt"){
			document.getElementById("bankRowId").style.display = "block";
			document.getElementById("bankBranchRowId").style.display = "block";
		}
	}
  	
  	
 	
 	function validateOnSearch(){
	   alert("sasa");
  	   document.forms("gratuityForm").action ="${pageContext.request.contextPath}/pension/gratuityAction.do?submitType=beforeGratuityCreate";
	   document.forms("gratuityForm").submit();
  	} 	

	function validateOnSave(){
		alert("save");
		document.forms("gratuityForm").action ="${pageContext.request.contextPath}/pension/gratuityAction.do?submitType=afterGratuityCreate";
	    document.forms("gratuityForm").submit();
		
	}


</script>

</head>
<body onLoad="onBodyLoad();">
<html:form action ="/pension/gratuityAction" >
	
 	<input type="hidden" name="checkEmpCode"/>
 	
 	
  <table style="width: 960; " align="center" cellpadding="0" cellspacing="0" border="1" id="employeeTable" >
   	<tr>
	    <td colspan="6" height=30 bgcolor=#bbbbbb align=middle  class="tablesubcaption"><p align="center"><b>Gratuity Processing &nbsp;&nbsp;&nbsp;</b></td>
    	</tr>
   	<tr>
   		<td class="labelcellforbg" align="right" colspan="4">&nbsp</td>
   	</tr>  
   	 <tr>
	    <input type="hidden" name="employeeCodeId" id="employeeCodeId" value="${gratuityForm.employeeCodeId}">
	 	<td class="labelcell">Employee Code<font color="red">*</font></td>
	  	<td class="labelcell">	  		
			${gratuityForm.employeeCode}			
	  	</td>
	  	<td class="labelcell">Employee Name</td>
	  	<td class="labelcell">
			${gratuityInfo.employeeName}	
	  	</td>
    </tr>
  	<tr>
  		<td><div id="codescontainer"></div></td>
    </tr>
    
   </table>      
   <table style="width: 960; " align="center" cellpadding="0" cellspacing="0" border="1" id="gratuityTable" >
	<tr>	    
	 	<td class="labelcell">Designation<font color="red">*</font></td>
	  	<td class="labelcell">
			${gratuityInfo.designation}	  		
	  	</td>
	  	<td class="labelcell">Department</td>
	  	<td class="labelcell">
			${gratuityInfo.department}		  
	  	</td>
    </tr>  
    <tr>	    
	 	<td class="labelcell">Birth Date</td>
	  	<td class="labelcell">
			${gratuityInfo.birthDate}	  		
	  	</td>
	  	<td class="labelcell">Superannuation Date</td>
	  	<td class="labelcell">
			${gratuityInfo.supperannuationDate}		  	
	  	</td>
    </tr> 	
	<tr>	    
	 	<td class="labelcell">Disbursement Type</td>
	  	<td class="labelcell">
			<c:if test = "${gratuityInfo.pensionDetails.pensionHeader.disbursementType == 'dbt'}">
				Direct bank transfer
			</c:if>	
			<c:if test = "${gratuityInfo.pensionDetails.pensionHeader.disbursementType == 'cash'}">
				Cash
			</c:if>	
			<c:if test = "${gratuityInfo.pensionDetails.pensionHeader.disbursementType == 'cheque'}">
				Cheque
			</c:if>	
	  	</td>	 
		<td class="labelcell"></td>
		<td class="labelcell"></td>
    </tr> 
	<c:if test = "${gratuityInfo.pensionDetails.pensionHeader.disbursementType == 'dbt'}">
		<tr id="bankRowId" >	    
			<td class="labelcell">Bank</td>
			<td class="labelcell">
				${gratuityInfo.pensionDetails.pensionHeader.idBranch.bank.name}	  		
			</td>
			<td class="labelcell"></td>
			<td class="labelcell"></td>
		</tr>
		<tr id="bankBranchRowId" >	    
			<td class="labelcell">Bank branch</td>
			<td class="labelcell">
				${gratuityInfo.pensionDetails.pensionHeader.idBranch.branchname}	  		
			</td>	  	
			<td class="labelcell">Bank Account</td>
			<td class="labelcell">
				${gratuityInfo.pensionDetails.pensionHeader.accountNumber}	  		
			</td>
		</tr> 	
	</c:if>
    <tr>	    
	 	<td class="labelcell">Pension Sanction Number</td>
	  	<td class="labelcell">
			${gratuityInfo.pensionSanctionNumber}	  		
	  	</td>	  	
	  	<td class="labelcell">Sanction Authority</td>
	  	<td class="labelcell">
			${gratuityInfo.pensionSanctionAuthority}	  	
	  	</td>
    </tr> 	
    <tr>	    
	 	<td class="labelcell">Pension Number(PPO)</td>
	  	<td class="labelcell">
			${gratuityInfo.pensionDetails.pensionNumber}
	  	</td>	  		
		<td class="labelcell"></td>
		<td class="labelcell"></td>
    </tr> 
    <tr>	    
	 	<td class="labelcell">Basic Pay</td>
	  	<td class="labelcell">
			${gratuityInfo.currentBasicPay}	  		
	  	</td>	  	
		<td class="labelcell"></td>
		<td class="labelcell"></td>
	</tr>

	<!--<tr> 	
	  	<td class="labelcell">Basic Component</td>
	  	<td class="labelcell">
			${gratuityInfo.basicComponentPercent}	  	
		</td>
		<td class="labelcell">
			${gratuityInfo.basicComponent}			
	  	</td>
	 </tr>-->
	 <tr> 	
	  	<td class="labelcell">DA</td>
	  	<td class="labelcell">
			${gratuityInfo.daComponentPercent}	  		
		</td>
		<td class="labelcell">
			${gratuityInfo.daComponent}
	  	</td>	  	
	 </tr> 
<!--	 <tr> 	
	  	<td class="labelcell">Pension Eligible</td>
		<td class="labelcell">
			&nbsp;
	  	</td>
	  	<td class="labelcell">
			${gratuityInfo.pensionEligible}	  		
	  	</td>	  	
	 </tr> 		-->
	 <tr> 	
	  	<td class="labelcell"><b>Commute Details</b></td>
		<td class="labelcell"></td>
		<td class="labelcell"></td>
	 </tr> 
	 <tr> 	
	  	<td class="labelcell" align="right">Pension Commuted(%)</td>
	  	<td class="labelcell">
			${gratuityInfo.pensionCommutedPercent}	  		
	  	</td>	 
		<td class="labelcell">
			${gratuityInfo.pensionCommuted}	  		
	  	</td>
	 </tr> 
	 <tr> 	
	  	<td class="labelcell" align="right">Commuted Period(years)</td>
	  	<td class="labelcell">
			${gratuityInfo.commutePeriod}	  		
	  	</td>	 
		<td class="labelcell"></td>
		
	 </tr> 
	 <tr> 	
	  	<td class="labelcell" align="right">Commuted Amount</td>
		<td class="labelcell" align="right"> </td>
	  	<td class="labelcell">
			${gratuityInfo.commuteAmount}	  		
	  	</td>	  	
	 </tr> 	 	
	 <tr> 	
	  	<td class="labelcell" align="right">Gratuity Amount</td>
		<td class="labelcell" align="right"> </td>
	  	<td class="labelcell">
			${gratuityInfo.gratuityAmount}	  		
	  	</td>	  	 
	 </tr> 		 
	 <tr> 	
	  	<td class="labelcell" align="right">Total Payment(Gross)</td>
		<td class="labelcell" align="right"> </td>
	  	<td class="labelcell">
			${gratuityInfo.totalPayment}	  		
	  	</td>	  	
	 </tr> 		
	 <tr>
		<td class="labelcell">Monthly Pension Payable</td>
		<td class="labelcell" align="right"> </td>
		<td class="labelcell">
			${gratuityInfo.monthlyPensionPayable}	  			
		</td>
	 </tr>
	  <tr> 	
	  	<td class="labelcell">Disburse Gratuity to</td>
	  	<td class="labelcell">
			<c:if test = "${gratuityInfo.payTo == 'nominee'}">
				Nominee(s)
			</c:if>	
			<c:if test = "${gratuityInfo.payTo == 'employee'}">
				Employee
			</c:if>					
			<input type="hidden" name="payTo" value="${gratuityForm.payTo}"  />	
	  		
	  	</td>	  	
	 </tr>
	 
	 <tr>
	<c:if test="${gratuityInfo.payTo == 'nominee'}">  
		 <table style="width: 960; " align="center" cellpadding="0" cellspacing="0" border="1" id="nomineeTable" >
			 <tr> 	
		  		<td class="labelcell"><b>Nominee Details</b></td>
				<td class="labelcell"><b> </b></td>

		  	 </tr>
		  	 <tr>	
		  		<td class="labelcell" align="right">Name</td>
				<td class="labelcell" align="right">Amount</td>
		  		<td class="labelcell" align="right">Monthly Pension Payable</td>
		   	 </tr>
			 <c:forEach var="nomineeObj" items="${gratuityInfo.pensionDetails.pensionHeader.nomineeDetails}">
				 <tr id="nomineeRowId">
					<td class="labelcell"> 
						${nomineeObj.firstName}
					</td>
					<td class="labelcell"> 
						${nomineeObj.grossPayAmount}
					</td>
					<td class="labelcell">
						${nomineeObj.monthlyPensionAmount}
					</td>		  	
				  </tr> 	
			  </c:forEach>
	 	      <tr>	
		  		<td class="labelcell"><b>Total</b></td>
				<td class="labelcell">
		  			${gratuityInfo.total}
				</td>
		  		<td class="labelcell">
		  			${gratuityInfo.totalMonthlyPensionPayable}
				</td>
		   	 </tr>
		</table>
	</c:if>	 	 
   </table>

  
   
</html:form>
</body>
</html>

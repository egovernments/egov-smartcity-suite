<%@ include file="/includes/taglibs.jsp" %>
<%@page import="java.util.*,org.egov.commons.Accountdetailtype,org.egov.payroll.utils.PayrollConstants,org.egov.masters.services.MastersManager,
				org.egov.utils.GetEgfManagers" %>
<html>
<head>

<title>Disburse Gratuity Report</title>

	<style type="text/css">
		#codescontainer {position:absolute;left:11em;width:9%}
		#codescontainer .yui-ac-content {position:absolute;width:80%;border:1px solid #404040;background:#fff;overflow:hidden;z-index:9050;}
		#codescontainer .yui-ac-shadow {position:absolute;margin:.3em;width:40%;background:#a0a0a0;z-index:9049;}
		#codescontainer ul {padding:5px 0;width:80%;}
		#codescontainer li {padding:0 5px;cursor:default;white-space:nowrap;}
		#codescontainer li.yui-ac-highlight {background:#ff0;}
		#codescontainer li.yui-ac-prehighlight {background:#FFFFCC;}
</style>

<%
	String mode = request.getParameter("mode");
	System.out.println("mode----------"+mode);
	MastersManager masterManager = GetEgfManagers.getMastersManager();
	Accountdetailtype empAccountDetailType = masterManager.getAccountdetailtypeByName(PayrollConstants.EMPLOYEE_MODULE);
	Integer empAccType = empAccountDetailType.getId();
	System.out.println("empAccType-----------"+empAccType);
	Accountdetailtype nomineeAccountDetailType = masterManager.getAccountdetailtypeByName(PayrollConstants.NOMINEE_MODULE);
	Integer nomineeAccType = nomineeAccountDetailType.getId();
	System.out.println("nomineeAccType------------"+nomineeAccType);

	String cheque = PayrollConstants.PENSION_PAYMENT_TYPE_CHEQUE;
	String cash= PayrollConstants.PENSION_PAYMENT_TYPE_CASH;
	String dbt = PayrollConstants.PENSION_PAYMENT_TYPE_DBT;
	
	//String autoGenerateChqNo =  EGovConfig.getProperty("payroll_egov_config.xml","AUTOGENERATECHQNO","",EGOVThreadLocals.getDomainName()+".ChequeGeneration");
%>
<script language="JavaScript"  type="text/JavaScript">	
	
	function onBodyLoad(){  		
	 
	   
	}	

</script>

</head>
<body onLoad="onBodyLoad();">
<c:set var="empAccType" value="<%= empAccType %>" />
<c:set var="nomineeAccType" value="<%= nomineeAccType %>" />
<c:set var="cheque" value="<%= cheque %>" />
<c:set var="cash" value="<%= cash %>" />
<c:set var="dbt" value="<%= dbt %>" />
<html:form action ="/pension/pensionAction" >
	
  <table style="width: 800; " align="center" cellpadding="0" cellspacing="0" border="1" id="pensionPaymentId" >
   	<tr>
	    <td colspan="6" height=30 bgcolor=#bbbbbb align=middle  class="tablesubcaption"><p align="center"><b>Disburse Pension Report &nbsp;&nbsp;&nbsp;</b></td>
    	</tr>
   	<tr>
   		<td class="labelcellforbg" align="right" colspan="6">&nbsp</td>
   	</tr>  
	<c:forEach var="voucherObj" items="${pensionForm.voucherList}">
		<tr>
			<td class="labelcell"><b>
				Payment Voucher</b>
			</td>
			<td class="labelcell"><b>
				${voucherObj.voucherNumber}</b>
			</td>				
			<td class="labelcell" ><b>
				Payment Mode</b>
			</td>
			<td class="labelcell">
				<c:if test = "${voucherObj.name == dbt}">
					Direct Bank Transfer
				</c:if>
				<c:if test = "${voucherObj.name == cheque}">
					Cheque
				</c:if>
				<c:if test = "${voucherObj.name == cash}">
					Cash
				</c:if>
			</td>	
			<td class="labelcell" colspan="2">
				
			</td>	
		</tr>
		<tr>
			<td class="labelcell" align="center">
				Pensioner/Nominee Name
			</td>			
			<td class="labelcell" align="center">
				Month
			</td>
			<td class="labelcell" align="center">
				Voucher Date
			</td>
			<c:if test = "${voucherObj.name == dbt}">
				<td class="labelcell" align="center">
					Bank Name
				</td>				
				<td class="labelcell" align="center">
					Bank Account Number
				</td>	
			</c:if>
			<c:if test = "${voucherObj.name == cheque}">
				<td class="labelcell" align="center">
					Cheque Number
				</td>
				<td class="labelcell" align="center">
					Cheque Date
				</td>	
			</c:if>
			<td class="labelcell" align="center">
				Amount
			</td>			
		</tr>
		<c:forEach var="pensionPaymentObj" items="${pensionForm.pensionPaymentList}">
		  <c:if test = "${pensionPaymentObj.voucherheader == voucherObj}">	
			 <tr>
				<td class="labelcell" align="center">
					<c:if test = "${pensionPaymentObj.egEmployee != null}"> 
						${pensionPaymentObj.egEmployee.employeeName}
					</c:if>
					<c:if test = "${pensionPaymentObj.nomineeDetails != null}"> 
						${pensionPaymentObj.nomineeDetails.firstName}
					</c:if>
				</td>
				<td class="labelcell" align="center">				
					${pensionPaymentObj.month}					
				</td>
				<td class="labelcell" align="center">				
					${pensionPaymentObj.voucherheader.voucherDate}					
				</td>
				<c:if test = "${voucherObj.name == dbt}">
					<td class="labelcell" align="center">				
						<c:if test = "${pensionPaymentObj.egEmployee != null}"> 
							${pensionPaymentObj.pensionDetails.pensionHeader.idBranch.bank.name}
						</c:if>
						<c:if test = "${pensionPaymentObj.nomineeDetails != null}"> 
							${pensionPaymentObj.nomineeDetails.bankBranch.bank.name}
						</c:if>			
					</td>
					<td class="labelcell" align="center">				
						<c:if test = "${pensionPaymentObj.egEmployee != null}"> 
							${pensionPaymentObj.pensionDetails.pensionHeader.accountNumber}
						</c:if>
						<c:if test = "${pensionPaymentObj.nomineeDetails != null}"> 
							${pensionPaymentObj.nomineeDetails.accountNumber}
						</c:if>						
					</td>
				</c:if>
				<c:if test = "${voucherObj.name == cheque}">
					<c:forEach var="chequeObj" items="${pensionForm.chequeDetailList}">
						<c:if test = "${pensionPaymentObj.egEmployee != null}">							
							<c:if test = "${chequeObj.detailTypeId == empAccType}">
								<c:if test = "${chequeObj.detailKeyId == pensionPaymentObj.egEmployee.idPersonalInformation}">
									<td class="labelcell" align="center">				
										${chequeObj.chequenumber}
									</td>
									<td class="labelcell" align="center">				
										${chequeObj.chequedate}
									</td>
								</c:if>
							</c:if>
						</c:if>
						<c:if test = "${pensionPaymentObj.nomineeDetails != null}">
							<c:if test = "${chequeObj.detailTypeId == nomineeAccType}">
								<c:if test = "${chequeObj.detailKeyId == pensionPaymentObj.nomineeDetails.accountEntity.id}">
									<td class="labelcell" align="center">				
										chequeNo					
									</td>
									<td class="labelcell" align="center">				
										chequeDate
									</td>
								</c:if>
							</c:if>
						</c:if>
					</c:forEach>
				</c:if>
				<td class="labelcell" align="center">				
					${pensionPaymentObj.amount}					
				</td>
			 </tr>			
		   </c:if>
		</c:forEach>
		 <tr>
			<td class="labelcell" colspan="6">				
					&nbsp;					
			</td>
		 </tr>
	</c:forEach>
   </table>


   
</html:form>
</body>
</html>

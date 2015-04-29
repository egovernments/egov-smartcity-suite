
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<s:if test="%{model.egwStatus!=null && model.egwStatus.code=='APPROVED' && sourcepage!='inbox'}">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
		<td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="${pageContext.request.contextPath}/image/arrow.gif" /></div>
		<div class="headplacer"><s:text name='workorder.amountdetailheader'/></div></td>
   </tr>	
	<tr>
		<td class="greyboxwk"><span class="mandatory">*</span><s:text name='workorder.emdAmountDeposited'/>:</td>
        	<td class="greybox2wk">
			<s:set name = "emdAmountDepositedVar" value ="%{emdAmountDeposited}" />        
			<input name="emdAmountDeposited" type="text" class="selectamountwk" id="emdAmountDeposited" 
        	size="25" MAXLENGTH="10" tabIndex="-1" value="<fmt:formatNumber value="${emdAmountDepositedVar}" pattern="#0.00" />"/>	
		</td>		
        <td class="greyboxwk"><s:text name='workorder.securityDeposit'/>:</td>
        <td class="greybox2wk">
 			<s:set name = "secVar" value ="%{securityDeposit}" />        
        	<input name="securityDeposit" type="text" class="selectamountwk" id="securityDeposit" 
        	size="25" MAXLENGTH="10" tabIndex="-1"  value="<fmt:formatNumber value="${secVar}" pattern="#0.00" />"/>
        </td>                         
	</tr>
	<tr>
		<td class="whiteboxwk"><s:text name='workorder.labourWelfareFund'/>:</td>
        	<td class="whitebox2wk"> 
			<s:set name = "labVar" value ="%{labourWelfareFund}" /> 
			<input name="labourWelfareFund" type="text" class="selectamountwk" id="labourWelfareFund" 
        	size="25" MAXLENGTH="10" tabIndex="-1"  value="<fmt:formatNumber value="${labVar}" pattern="#0.00" />"/>
        	
		</td>
        <td class="whiteboxwk">&nbsp;</td>
        <td class="whitebox2wk">&nbsp;</td>                         
	</tr>
  <tr>
		<td class="greyboxwk">
			<span class="mandatory">*</span><s:text name='workorder.engineerIncharge'/>: 
		</td>
        <td class="greybox2wk"> 
   			<s:select id="woEncharge" cssClass="selectwk" name="workOrderInchargeId" value="%{workOrderInchargeId}"
   					list="%{dropdownData.approvedByList}"    
   					listKey="id" listValue="employeeName" onchange='showWOInchargeDesignation(this);'
   					headerKey="-1" headerValue="--- Select ---" />
 		</td>
        <td class="greyboxwk">
        	<s:text name='tenderNegotiation.approverdesignation'/>: 
        </td>
        <td class="greybox2wk">
			<s:textfield id="woInchargeDesignation" readonly="true" disabled="disabled" 
				cssClass="selectboldwk" size="25" tabIndex="-1" />
		</td>                         
	</tr>
  <tr><td colspan="4" class="shadowwk"> </td></tr>			
 </table>
</s:if>	

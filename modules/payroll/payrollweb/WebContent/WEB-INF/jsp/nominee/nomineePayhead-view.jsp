<!--
	Program Name : nomineePayhead-new.jsp
	Author		: Surya	
	Purpose 	: Nominee Payhead details.
 -->
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/includes/taglibs.jsp" %>

<html>
  <head>
   
  
   <title>Nominee Payhead Details</title>

<script type="text/javascript">
     

   	    		 
</script>



  </head>
  <body>
  
   <s:form name="nomineePayheadForm" action="nomineePayhead" theme="simple" >  
   <s:token/>
   <div class="formmainbox">
			<div class="insidecontent">
		  	<div class="rbroundbox2">
			<div class="rbtop2"><div></div></div>
			<div class="rbcontent2">
			<span id="msg">
				<s:if test="%{hasErrors()}">
					<div class="errorstyle">
					<s:fielderror  cssClass="mandatoryone"/>
					<s:actionerror  cssClass="mandatoryone"/>
					</div>
				</s:if>
			</span>
			
			 <table width="95%" cellpadding="0" cellspacing="0" border="0" align="center">
				  <tbody>					
				  	<tr>
				  	 	<td colspan="${fn:length(salaryPaySlipForm.payHead)+3}" class="headingwk">
					  	 	<div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
					   		<p><div class="headplacer">Employee Details</div></p></td>
				   		<td></td> 
				   	</tr>
				   </tbody>
	   </table>
  
  	<br>

		<table width="95%" cellpadding="0" cellspacing="0" border="0" align="center">
			  <tr>
				  <td  class="whiteboxwk" align="center">Employee Code:<br><br></td>
				  <td class="whitebox2wk" align="center">
				  	<input type="text" name="empCode" class="selectwk" value="${employee.employeeCode}" readonly /><br><br></td>
				  <td  class="whiteboxwk" align="center">Employee Name:<br><br></td>
				  <td class="whitebox2wk" align="center">
				  	<input type="text"  class="selectwk" value="${employee.employeeName}" readonly><br><br>
				  </td>
				   <td class="whitebox2wk" align="center" colspan="${fn:length(salaryPaySlipForm.payHead)}">
				   </td>
			  </tr>
		</table>
  		
		<table width="95%" cellpadding="0" cellspacing="0" border="1" align="center" id="batchGrid">		  
			  <tr>
				   <td colspan="${fn:length(dropdownData.nominationHeaderList)+3}" class="headingwk"><div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
				   <p><div class="headplacer">Nominee Payhead Details</div></p></td><td></td> 
			  </tr>
			  <tr>
			      	<td class="tablesubheadwk" style="text-align: center">Nominee</td>
			      	<td class="tablesubheadwk" style="text-align: center">Relation Type</td>
			      	<td class="tablesubheadwk" style="text-align: center">Age</td>
			      	<td class="tablesubheadwk" style="text-align: center">Marital Status</td>
			      	<s:iterator var="s" value="dropdownData.nominationHeaderList" status="status">
			      		<td class="tablesubheadwk" style="text-align: center">			      			
			      			 <s:property value="%{code}" />
			      		</td>
			      	</s:iterator>
			      	<td class="tablesubheadwk" style="text-align: center"></td>
		      </tr>
		      <s:hidden name="delNomineePayheadId" />	
		      <s:if test="isEmpty==null">
		      	<tr id="detailsBatchRow">
		      		<td class="whiteboxwk" style="text-align: center">
		      			<s:select name="nomineePayheadDTOList[0].nomineeMaster.id" id="nominee" list="dropdownData.nomineeMasterList" listKey="id" listValue="nomineeName" headerKey="0" headerValue="----Select----" value="%{nomineeMaster.id}" onchange="setRelation(this);duplicateNomineeMaster();" disabled="true"/>
		      		</td>
		      		<td class="whiteboxwk" style="text-align: center">
		      			<s:textfield name="nomineePayheadDTOList[0].nomineeMaster.relationType.nomineeType" id="relationType" readonly="true"/>
		      		</td>
		      		<td class="whiteboxwk" style="text-align: center">
		      			<s:textfield name="nomineePayheadDTOList[0].nomineeMaster.nomineeAge"  readonly="true"/>
		      		</td>
		      		<td class="whiteboxwk" style="text-align: center">
		      			<s:if test='%{nomineeMaster.maritalStatus=="1"}'>
		      				<s:textfield name="nomineePayheadDTOList[0].nomineeMaster.maritalStatus" value="Yes"  readonly="true"/>
		      			</s:if>
		      			<s:else>
		      				<s:textfield name="nomineePayheadDTOList[0].nomineeMaster.maritalStatus" value="No"  readonly="true"/>
		      			</s:else>
		      		</td>
		      		<s:iterator value="nomineePayheadDTOList[0].nomineePayheadDetSet" status="status">	      			
				      		<td class="whiteboxwk" style="text-align: center">
				      			<s:checkbox name="check%{nominationHeader.id}" id="check%{nominationHeader.id}" onclick="checkHeaderPct(this);" disabled="true"/>
				      			<s:hidden name="nomineePayheadDTOList[0].nomineePayheadDetSet[%{#status.index}].id" id="nomPayheadDetid%{nominationHeader.id}"/>
				      			<s:hidden name="nomineePayheadDTOList[0].nomineePayheadDetSet[%{#status.index}].nominationHeader.id" id="nomHid%{nominationHeader.id}"/>
				      			<s:hidden name="nomineePayheadDTOList[0].nomineePayheadDetSet[%{#status.index}].isEligible" id="eligible%{nominationHeader.id}" value="N"/>
				      			<s:textfield name="nomineePayheadDTOList[0].nomineePayheadDetSet[%{#status.index}].pct" id="pct%{nominationHeader.id}" disabled="true" onchange="checkForPct(this);" readonly="true"/>
				      		</td>
			      	</s:iterator>
			      
		      	</tr>
		     </s:if>
		     <s:else>
		      <s:iterator value="nomineePayheadDTOList" status="rowCount">
		      	<tr id="detailsBatchRow">
		      		<td class="whiteboxwk" style="text-align: center">
		      			<s:select name="nomineePayheadDTOList[%{#rowCount.index}].nomineeMaster.id" id="nominee" list="dropdownData.nomineeMasterList" listKey="id" listValue="nomineeName" headerKey="0" headerValue="----Select----" value="%{nomineeMaster.id}" onchange="setRelation(this);duplicateNomineeMaster();" disabled="true"/>
		      		</td>
		      		<td class="whiteboxwk" style="text-align: center">
		      			<s:textfield name="nomineePayheadDTOList[%{#rowCount.index}].nomineeMaster.relationType.nomineeType" id="relationType" readonly="true"/>
		      		</td>
		      		<td class="whiteboxwk" style="text-align: center">
		      			<s:textfield name="nomineePayheadDTOList[%{#rowCount.index}].nomineeMaster.nomineeAge"  readonly="true"/>
		      		</td>
		      		<td class="whiteboxwk" style="text-align: center">
		      			<s:if test='%{nomineeMaster.maritalStatus=="1"}'>
		      				<s:textfield name="nomineePayheadDTOList[%{#rowCount.index}].nomineeMaster.maritalStatus" value="Yes"  readonly="true"/>
		      			</s:if>
		      			<s:else>
		      				<s:textfield name="nomineePayheadDTOList[%{#rowCount.index}].nomineeMaster.maritalStatus" value="No"  readonly="true"/>
		      			</s:else>
		      		</td>
		      		<s:iterator value="nomineePayheadDetSet" status="status">	      			
				      		<td class="whiteboxwk" style="text-align: center">
				      			<s:if test='%{isEligible=="N"}'>
				      				<s:checkbox name="check%{nominationHeader.id}" id="check%{nominationHeader.id}" onclick="checkHeaderPct(this);" disabled="true"/>
				      				<s:hidden name="nomineePayheadDTOList[%{#rowCount.index}].nomineePayheadDetSet[%{#status.index}].id" id="nomPayheadDetid%{nominationHeader.id}"/>
					      			<s:hidden name="nomineePayheadDTOList[%{#rowCount.index}].nomineePayheadDetSet[%{#status.index}].nominationHeader.id" id="nomHid%{nominationHeader.id}"/>
					      			<s:hidden name="nomineePayheadDTOList[%{#rowCount.index}].nomineePayheadDetSet[%{#status.index}].isEligible" id="eligible%{nominationHeader.id}"/>
					      			<s:textfield name="nomineePayheadDTOList[%{#rowCount.index}].nomineePayheadDetSet[%{#status.index}].pct" id="pct%{nominationHeader.id}" disabled="true" onchange="checkForPct(this);" readonly="true"/>
				      			</s:if>
				      			<s:else>
					      			<s:checkbox name="check%{nominationHeader.id}" id="check%{nominationHeader.id}" onclick="checkHeaderPct(this);" checked="" disabled="true"/>
					      			<s:hidden name="nomineePayheadDTOList[%{#rowCount.index}].nomineePayheadDetSet[%{#status.index}].id" id="nomPayheadDetid%{nominationHeader.id}"/>
					      			<s:hidden name="nomineePayheadDTOList[%{#rowCount.index}].nomineePayheadDetSet[%{#status.index}].nominationHeader.id" id="nomHid%{nominationHeader.id}"/>
					      			<s:hidden name="nomineePayheadDTOList[%{#rowCount.index}].nomineePayheadDetSet[%{#status.index}].isEligible" id="eligible%{nominationHeader.id}"/>
					      			<s:textfield name="nomineePayheadDTOList[%{#rowCount.index}].nomineePayheadDetSet[%{#status.index}].pct" id="pct%{nominationHeader.id}" onchange="checkForPct(this);" readonly="true"/>
					      		</s:else>	
				      		</td>
			      	</s:iterator>
			      	
		      	</tr>
		      </s:iterator>
		     </s:else>
				  
		</table>	
	
	<br>
	
			 
	 
  	
	
	
 </div>
		<div class="rbbot2"><div></div></div>
		</div>
		</div></div>		
</s:form>

  </body>
  </html>

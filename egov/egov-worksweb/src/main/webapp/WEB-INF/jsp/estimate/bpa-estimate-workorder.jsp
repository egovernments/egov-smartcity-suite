<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<script>
	var deptId = null;
	<s:if test="%{execDeptId != null}">
		deptId = '<s:property value="%{execDeptId}"/>';
	</s:if>
	function populateUser1(obj){
		populateengineerIncharge({desgId:obj.value,executingDepartment:deptId})
	}
	
	function populateUser2(obj){
		populateengineerIncharge2({desgId:obj.value,executingDepartment:deptId})
	}

	function validateWOAllocatedUsers(){
		if(dom.get("assignedTo1").value!='-1' && dom.get("assignedTo2").value!='-1' && 
			    dom.get("engineerIncharge").value!='-1' && dom.get("engineerIncharge2").value!='-1' && 
			    dom.get("assignedTo2").value==dom.get("assignedTo1").value && dom.get("engineerIncharge").value==dom.get("engineerIncharge2").value) {
			dom.get("worktypeerror").style.display='';     
			dom.get("worktypeerror").innerHTML='<s:text name="same.allocatedTo.selected"/>';
			window.scroll(0,0);
			return false;
		} else {
			dom.get("worktypeerror").innerHTML='';
			dom.get("worktypeerror").style.display="none";
			return true;
		}
	}

	function isNumberKey(evt) {
       var charCode = (evt.which) ? evt.which : event.keyCode
       if (charCode > 31 && (charCode < 48 || charCode > 57)) {
          return false;
       }
       return true;
    }

	function roundOffDLP() {
		dom.get("defectLiabilityPeriod").value=roundTo(dom.get("defectLiabilityPeriod").value);
	}

	function roundOffLWF() {
		dom.get("labourWelfareFund").value = roundTo(dom.get("labourWelfareFund").value);
	}

	function updateWorkOrderDetails(onLoad) {
		dom.get("rateContNumber").innerHTML = dom.get("rcNumber").innerHTML;
		var arr = dom.get("contractorName").innerHTML.split('/');
		dom.get("nameOftheContractor").innerHTML = arr[0];
		dom.get("codeOftheContractor").innerHTML = arr[1];
		if (!onLoad) {
			dom.get("paymentTerms").value = "";
			dom.get("workOrderDetails").value = "";
			dom.get("agreementDetails").value = "";
			var woAmount = document.getElementById('workOrderAmount').value;
			var lwfconf = '<s:property value="%{labourWelfareFundConfValue}"/>';
			var lwFund = eval(lwfconf/100) * woAmount; 
			document.getElementById('labourWelfareFund').value = roundTo(lwFund);
			dom.get("contractPeriod").value = "";
			dom.get("defectLiabilityPeriod").value = "";
			dom.get("assignedTo1").value = "-1";
			dom.get("engineerIncharge").value = "-1";
			<s:if test="%{'yes'.equals(assignedToRequiredOrNot)}">
				dom.get("assignedTo2").value = "-1";
				dom.get("engineerIncharge2").value = "-1";
			</s:if>
		}
	}

	function setLabourWelfareFund() {
		<s:if test="%{workOrder == null || workOrder.labourWelfareFund == null || workOrder.labourWelfareFund == 0.0 || workOrder.labourWelfareFund == 0.00}">
			var woAmount = document.getElementById('workOrderAmount').value;
			var lwfconf = '<s:property value="%{labourWelfareFundConfValue}"/>';
			var lwFund = eval(lwfconf/100) * woAmount; 
			document.getElementById('labourWelfareFund').value = roundTo(lwFund);
		</s:if>
	}
</script>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td colspan="4" class="headingwk">
			<div class="arrowiconwk">
				<img src="${pageContext.request.contextPath}/image/arrow.gif" />
			</div>
			<div class="headplacer">
				<s:text name="page.title.workorder.detail" />
			</div>
		</td>
	</tr>
	<s:if test="%{egwStatus != null && egwStatus.code == 'ADMIN_SANCTIONED'}">
		<tr>
			<td class="whiteboxwk">
				<s:text name="label.wonumber"/>:
			</td>
			<td class="whitebox2wk">
				<span class="bold"><s:property value="%{workOrder.workOrderNumber}" /></span>
			</td>	
			<td class="whiteboxwk" colspan="2">&nbsp;</td>
		</tr>
	</s:if>
	<tr>
		<td class="greyboxwk" width="25%">
			<s:text name='bpa.esimate.wo.execDept' />:
		</td>
		<td class="greybox2wk" width="25%">
			<span class="bold" id="execDept"><s:property value="executingDepartment.deptName"/></span>
		</td>
		<td class="greyboxwk" width="20%">
			<s:text name='workorder.ratecontract.number' /> :
		</td>
		<td class="greybox2wk"><span class="bold" id="rateContNumber"><s:property value="%{rateContract.rcNumber}"/></span></td>
	</tr>
	<tr>
		<td class="whiteboxwk"><s:text name="estimate.amount" /> :</td>
	    <td class="whitebox2wk"><span class="bold" id="woEstAmtSpan"><s:property value='%{totalAmount.formattedString}' /></span></td>
	    <td class="whiteboxwk"><s:text name="workorder.total" /> :</td>
	    <s:hidden id="workOrderAmount" name="workOrder.workOrderAmount" value="%{workValue.formattedString}"/>
	    <td class="whitebox2wk"><span class="bold" id="woAmtSpan"><s:property value='%{workValue.formattedString}' /></span></td>
   	</tr>
   	<tr>
		<td class="greyboxwk">
			<s:text name='bpa.esimate.wo.contractorName'/> : 
		</td>
		<td class="greybox2wk">
			<span class="bold" id="nameOftheContractor"><s:property value="%{rateContract.contractor.name}" /></span>
		</td>
		<td class="greyboxwk"><s:text name="contractor.code"/> :</td>
		<td class="greybox2wk">
			<span class="bold" id="codeOftheContractor"><s:property value="%{rateContract.contractor.code}"/></span>
		</td>
	</tr>
	<tr>
		<td class="whiteboxwk"><s:text name="payment.terms"/> :</td>
		<td class="whitebox2wk">
			<s:textarea name="workOrder.paymentTerms" value="%{workOrder.paymentTerms}" cols="27" rows="4" cssClass="selectwk" id="paymentTerms" />
		</td>
		<td class="whiteboxwk">
        	<s:text name="workorder.details"/> :
        </td>
        <td class="whitebox2wk">
			<s:textarea name="workOrder.workOrderDetails" value="%{workOrder.workOrderDetails}" cols="27" rows="4" cssClass="selectwk" id="workOrderDetails" />
		</td>	
   	</tr>
	<tr>
   		<td class="greyboxwk"> 
   			<s:text name="agg.details"/> :
   		</td>
        <td class="greybox2wk">
       		<s:textarea name="workOrder.agreementDetails" value="%{workOrder.agreementDetails}" cols="27" rows="4" cssClass="selectwk" id="agreementDetails" />
        </td>
        <td class="greyboxwk">
        	<s:text name="labour.welfund"/> :
        </td>
        <td class="greybox2wk">
        	<s:textfield name="workOrder.labourWelfareFund" value="%{workOrder.labourWelfareFund}" id="labourWelfareFund" cssClass="selectamountwk" 
        	onblur="roundOffLWF()"/>
    	</td>
   	</tr>
	<tr>
       <td class="whiteboxwk">
   			<span class="mandatory">*</span><s:text name="con.period"/> : 
		</td>
		<td class="whitebox2wk">
        	<s:textfield name="workOrder.contractPeriod" value="%{workOrder.contractPeriod}" id="contractPeriod" cssClass="selectwk" onkeypress="return isNumberKey(event)" />
        </td>
        <td class="whiteboxwk" ><span class="mandatory">*</span>
   			<s:text name="defect.liability.period"/> :</td>
		<td class="whitebox2wk">
			<s:textfield  name="workOrder.defectLiabilityPeriod" value="%{workOrder.defectLiabilityPeriod}" id="defectLiabilityPeriod" cssClass="selectamountwk" onblur="roundOffDLP()" />
		</td>
   	</tr>	
   	<tr>
   		<td class="greyboxwk">
   			<span class="mandatory">*</span><s:text name="wo.allocated.to1"/> :
		</td>
		<td class="greybox2wk">
			<s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="assignedTo1" 
	         id="assignedTo1" value="%{assignedTo1}" cssClass="selectwk" 
	         list="dropdownData.assignedToList" listKey="designationId" listValue="designationName" onchange="populateUser1(this)"/>
	         <egov:ajaxdropdown id="engineerIncharge" fields="['Text','Value']" dropdownId='engineerIncharge' 
		        	  url='workorder/ajaxWorkOrder!getUsersForDesg.action'/>
		</td>	
        <td class="greyboxwk">
        	<span class="mandatory">*</span><s:text name="wo.user1"/> :
        </td>
        <td class="greybox2wk">
        	<s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="engineerInCharge1" 
	         id="engineerIncharge"  cssClass="selectwk" 
	         list="dropdownData.assignedUserList1" listKey="id" listValue="employeeName" onchange="validateWOAllocatedUsers();" value="%{engineerInCharge1}"/>
        </td>
	</tr>
   	<s:if test="%{'yes'.equals(assignedToRequiredOrNot)}">
		<tr>
   			<td class="whiteboxwk">
   			<s:text name="wo.allocated.to2"/> :
			</td>
			<td class="whitebox2wk">
				<s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="assignedTo2" 
		         id="assignedTo2" value="%{assignedTo2}" cssClass="selectwk" 
		         list="dropdownData.assignedToList" listKey="designationId" listValue="designationName" onchange="populateUser2(this)" />
		         <egov:ajaxdropdown id="engineerIncharge2" fields="['Text','Value']" dropdownId='engineerIncharge2' 
			        	  url='workorder/ajaxWorkOrder!getUsersForDesg.action'/>
			</td>	
	        <td class="whiteboxwk">
	        	<s:text name="wo.user2"/> :
	        </td>
	        <td class="whitebox2wk">
	        	<s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="engineerInCharge2" 
		         id="engineerIncharge2"  cssClass="selectwk" 
		         list="dropdownData.assignedUserList2" listKey="id" listValue="employeeName" onchange="validateWOAllocatedUsers();" value="%{engineerInCharge2}"/>
	        </td>
		</tr>
		<tr>
			<td colspan="4" class="shadowwk"></td>                 
		</tr>
   	</s:if> 
</table>
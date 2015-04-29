
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<script>
var rateContractData="";
function returnBackToParent(rateContractData) {
	var wind;
	var data = new Array();
	wind=window.dialogArguments;
	if(wind==undefined){
		wind=window.opener;
		data =rateContractData;
		window.opener.update(data);
	}
	else{
		wind=window.dialogArguments;
		wind.result =rateContractData;
	}
	window.close();
}

function addToParent(){
	if(rateContractData==""){
		var errmsg='<s:text name="ratecontract.select.msg" />';
		alert(errmsg);
		return false;
	}
	<s:if test="%{isBpaCutEstimate}">
		returnBackToParentBpa(rateContractData);
	</s:if>
	<s:else>
		returnBackToParent(rateContractData);
	</s:else>
}

function setAllValues(obj){
	 rateContractData="";
	 var contName="";
	 var rateContNo=obj.value;
	 var validFrom="";
	 var validTo="";
	 var rateContractId="";
	 var currRow=getRow(obj);
	 contName = getControlInBranch(currRow,'contractorName').value;
 	 validFrom=getControlInBranch(currRow,'validFrom').value; 
 	 validTo=getControlInBranch(currRow,'validTo').value; 
 	 rateContractId=getControlInBranch(currRow,'rateContractId').value;
	 rateContractData=contName + '`~`' + rateContNo + '`~`' + validFrom + '`~`' + validTo + '`~`' + rateContractId;
	 <s:if test="%{isBpaCutEstimate}">
	 	var rateContractAmount = getControlInBranch(currRow,'rateContractAmount').value;
	 	rateContractData = rateContractData + '`~`' + rateContractAmount;
	 	var rcExecDept = getControlInBranch(currRow,'rcExecDept').value;
	 	rateContractData = rateContractData + '`~`' + rcExecDept;
	 	var rcTypeOfWork = getControlInBranch(currRow,'rcTypeOfWork').value;
	 	rateContractData = rateContractData + '`~`' + rcTypeOfWork;
	 	var rcSubTypeOfWork = getControlInBranch(currRow,'rcSubTypeOfWork').value;
	 	rateContractData = rateContractData + '`~`' + rcSubTypeOfWork;
	 	var rcStartDate = getControlInBranch(currRow,'rcStartDate').value;
	 	rateContractData = rateContractData + '`~`' + rcStartDate;
	 	var rcEndDate = getControlInBranch(currRow,'rcEndDate').value;
	 	rateContractData = rateContractData + '`~`' + rcEndDate;
	 	var rcFund = getControlInBranch(currRow,'rcFund').value;
	 	rateContractData = rateContractData + '`~`' + rcFund;
	 	rateContractData = rateContractData + '`~`fromRateContract';
	 </s:if> 
}

function returnBackToParentBpa(rateContractData) {
	var wind;
	var bpaConfirmation = true;
	var rcConfirmation = true;
	var data = new Array();
	wind=window.dialogArguments;
	if(wind==undefined){
		wind=window.opener;
		data =rateContractData;
		var a = data.split("`~`");
		var estimateId = '<s:property value="%{estimateId}"/>';
		var rateContId = a[4];
		var rateContAmount = a[5];
		var remainingBpaAmount = eval('<s:property value="%{bpaAmount}"/>') - eval('<s:property value="%{utilizedBpaAmount}"/>');
		var estimateAmount = eval('<s:property value="%{estimateValue}"/>'); 
		if (remainingBpaAmount < estimateAmount) {
			bpaConfirmation = confirm('Amount remitted in the BPA application for <s:property value="%{connectionType}"/> is Rs.'+remainingBpaAmount+' which is Rs.'+eval(estimateAmount-remainingBpaAmount)+' less than the estimate amount. Do you want to continue?');
		}
		if (bpaConfirmation) {
			jQuery.ajax({
				url : '${pageContext.request.contextPath}/estimate/ajaxEstimate!getUtilizedAmountForRateContract.action?estimateId='
						+ estimateId + '&rateContractId=' + rateContId,
				dataType : "json",
				success : function(result) {
					var utilizedRcAmount = result.utilizedRcAmount;
					var balanceRcAmount = eval(rateContAmount) - eval(utilizedRcAmount);
					if (balanceRcAmount < estimateAmount) {
						rcConfirmation = confirm('<s:text name="ratecontract.estimate.insufficient.amount.confirm"/>?');
					}
					if (rcConfirmation) {
						data = data + '`~`' + utilizedRcAmount + '`~`' + balanceRcAmount;
						window.opener.update(data);
						window.close();
					}
				}
			});
		}
	} else {
		wind=window.dialogArguments;
		wind.result =rateContractData;
	}
}
</script>

<div>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr height="5">
			<td></td>
		</tr>
		<tr>
			<td>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td colspan="9" class="headingwk" align="left">
							<div class="arrowiconwk">
								<img src="${pageContext.request.contextPath}/image/arrow.gif" />
							</div>

							<div class="headerplacer">
								<s:text name='search.result' />
							</div>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	<s:if test="%{searchResult.fullListSize != 0 && !hasErrors()}">
		<s:text id="slNo" name="%{getText('label.slno')}"></s:text>
		<display:table name="searchResult" pagesize="30" uid="currentRow"
			cellpadding="0" cellspacing="0" requestURI=""
			style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">
			<display:column headerClass="pagetableth" class="pagetabletd"
				title="Select" style="width:3%;text-align:center">
				<input name="radio" type="radio" id="radio"
					value="<s:property value='%{#attr.currentRow.rcNumber}'/>"
					onClick="setAllValues(this);" />
			</display:column>
			<display:column class="hidden" headerClass="hidden" media="html">
				<s:hidden id="contractorName" name="contractorName"
					value="%{#attr.currentRow.contractor.name} / %{#attr.currentRow.contractor.code}" />
				<s:hidden id="rateContNo" name="rateContNo"
					value="%{#attr.currentRow.rcNumber}" />
					<s:date name="%{#attr.currentRow.indent.startDate}" var="startDateFormat" format="dd/MM/yyyy"/>
				<s:hidden id="validFrom" name="validFrom"
					value="%{startDateFormat}" />
					<s:date name="#attr.currentRow.indent.endDate"  var="endDateFormat" format="dd/MM/yyyy" />
				<s:hidden id="validTo" name="validTo"
					value="%{endDateFormat}" />
				<s:hidden id="rateContractId" name="rateContractId"
					value="%{#attr.currentRow.id}" />
				<s:if test="%{isBpaCutEstimate}">
					<s:hidden id="rateContractAmount" name="rateContractAmount"
						value="%{#attr.currentRow.rcAmount}" />
					<s:hidden id="rcExecDept" name="rcExecDept"
						value="%{#attr.currentRow.indent.department.id}" />
					<s:hidden id="rcTypeOfWork" name="rcTypeOfWork"
						value="%{#attr.currentRow.indent.typeOfWork.id}" />
					<s:hidden id="rcSubTypeOfWork" name="rcSubTypeOfWork"
						value="%{#attr.currentRow.indent.subTypeOfWork.id}" />
					<s:hidden id="rcStartDate" name="rcStartDate"
						value="%{startDateFormat}" />
					<s:hidden id="rcEndDate" name="rcEndDate"
						value="%{endDateFormat}" />	
					<s:hidden id="rcFund" name="rcFund"
						value="%{#attr.currentRow.indent.fund.id}" />
				</s:if>
			</display:column>

			<display:column title=" Sl No" style="text-align:center;"
				headerClass="pagetableth" class="pagetabletd">
				<s:property value="#attr.currentRow_rowNum + (page-1)*pageSize" />
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd"
				title="Executing Department" style="text-align:center;"
				property="indent.department.deptName">
			</display:column>
 			<display:column headerClass="pagetableth" class="pagetabletd"
				title="Contractor Name/Code" style="text-align:center;">
				<s:property value="#attr.currentRow.contractor.name" /> / <s:property value="#attr.currentRow.contractor.code" />
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd"
				title="Rate Contract Number" style="text-align:center;"
				property="rcNumber">
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd"
				title="Amount(Rs)" style="text-align:center;" property="rcAmount">
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd"
				title="Valid From" style="text-align:center;">
				<s:date name="%{#attr.currentRow.indent.startDate}" format="dd/MM/yyyy"/>
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd"
				title="Valid To" style="text-align:center;">
				<s:date name="#attr.currentRow.indent.endDate"  format="dd/MM/yyyy" />
			</display:column>
		</display:table>
		<tr>
			<td colspan="4">
				<div class="buttonholderwk" align="center">
					<input type="button" class="buttonfinal" value="ADD"
						id="saveButton" name="button" onclick="addToParent();"
						method="search" /> <input type="button" class="buttonfinal"
						value="CLOSE" id="closeButton" name="button"
						onclick="window.close();" />
				</div>
			</td>
		</tr>
	</s:if>
	<s:elseif test="%{searchResult.fullListSize == 0 && !hasErrors()}">
		<div>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td align="center"><font color="red"><s:text
								name="search.result.no.record" /></font></td>
				</tr>
			</table>
		</div>
	</s:elseif>
</div>
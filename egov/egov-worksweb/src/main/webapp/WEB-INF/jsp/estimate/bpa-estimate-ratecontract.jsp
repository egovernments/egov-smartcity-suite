<script>
	function setUtilizedAndBalance(rateContId, estimateId, rateContAmount, onLoad) {
		jQuery.ajax({
			url : '${pageContext.request.contextPath}/estimate/ajaxEstimate!getUtilizedAmountForRateContract.action?estimateId='
						+ estimateId + '&rateContractId=' + rateContId,
			dataType: "json",
			success : function(result) {
				var utilized = result.utilizedRcAmount;
				var balance = eval(rateContAmount) - eval(utilized);
				populateData(utilized, balance, onLoad);
			}
		});
	}

	function populateData(utilized, balance, onLoad) {
		jQuery('#utilizedAmount').text(roundTo(utilized));
		jQuery('#balanceAmount').text(roundTo(balance));
		showWorkOrderDetails();
		updateWorkOrderDetails(onLoad);
		setLabourWelfareFund();
	}
	
	function showRcSearchPage() {
		var execDept = dom.get("executingDepartment").value;
		var estimateValue = dom.get("estimateValue").value;
		var bpaAmount = '<s:property value="%{bpaAmount}"/>';
		var utilizedBpaAmount = '<s:property value="%{utilizedBpaAmount}"/>';
		var connectionType = '<s:property value="%{connectionType}"/>';
		var estimateAmount = eval(dom.get("estimateValue").value);
		var subTypeOfWork = dom.get("category").value;
		if (execDept == null || execDept == "" || execDept == "-1") {
			showMessage('worktypeerror','<s:text name="dw.bpa.rc.exec.dept.mandatory"/>');
	    	window.scroll(0,0);
	    	return false;
		} else if (subTypeOfWork == null || subTypeOfWork == "" || subTypeOfWork == "-1") {
			showMessage('worktypeerror','<s:text name="dw.bpa.rc.subtypeofwork.mandatory"/>');
	    	window.scroll(0,0);
	    	return false;
		} else {
			dom.get("worktypeerror").innerHTML='';
			dom.get("worktypeerror").style.display='none';
		}
		
		if (dom.get("rateContractId").value != null && dom.get("rateContractId").value != "") {
			if (confirm('<s:text name="bpa.estimate.rc.modify.msg"/>')) {
				window.open(
						'${pageContext.request.contextPath}/rateContract/searchRateContract!newform.action?zoneIdsString=<s:property  value="ward.parent.id"/>&wardIdsString=<s:property  value="ward.id"/>&bpaAmount='+bpaAmount+'&utilizedBpaAmount='+utilizedBpaAmount+'&estimateValue='+estimateAmount+'&executingDepartmentId='+execDept+'&connectionType='+connectionType+'&estimateId=<s:property value="id"/>&isBpaCutEstimate=true',
							'SearchRateContractWindow', 'width=900,height=700');			
			}
		} else {
			window.open(
					'${pageContext.request.contextPath}/rateContract/searchRateContract!newform.action?zoneIdsString=<s:property  value="ward.parent.id"/>&wardIdsString=<s:property  value="ward.id"/>&bpaAmount='+bpaAmount+'&utilizedBpaAmount='+utilizedBpaAmount+'&estimateValue='+estimateAmount+'&executingDepartmentId='+execDept+'&connectionType='+connectionType+'&estimateId=<s:property value="id"/>&isBpaCutEstimate=true',
						'SearchRateContractWindow', 'width=900,height=700');
		}
	}
</script>
	<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
		<tr>
			<td class="whiteboxwk" width="25%">&nbsp;</td>
			<td class="whiteboxwk" width="25%">&nbsp;</td>
			<s:hidden name="rateContractId" id="rateContractId" value="%{rateContractId}" />
			<s:hidden name="rcExecDept" id="rcExecDept" value="%{rcExecDept}" />
			<s:hidden name="rcStartDate" id="rcStartDate" value="%{rcStartDate}" />
			<s:hidden name="rcEndDate" id="rcEndDate" value="%{rcEndDate}" />
			<s:hidden name="rcTypeOfWork" id="rcTypeOfWork" value="%{rcTypeOfWork}" />
			<s:hidden name="rcSubTypeOfWork" id="rcSubTypeOfWork" value="%{rcSubTypeOfWork}" />
			<s:hidden name="rcFund" id="rcFund" value="%{rcFund}" />
			<s:if test="%{egwStatus != null && sourcepage == 'inbox' && (egwStatus.code == 'NEW' || egwStatus.code == 'REJECTED') }">
				<td class="whiteboxwk">
					<a id="addRcAnchor" href="#" onClick="showRcSearchPage();">
						<s:text name="ratecontract.add.details" />
						<img id="addrowImg" name="addrowImg" src='${pageContext.request.contextPath}/images/addrow.gif' width="18" height="18" border="0"/>
					</a>
				</td>
			</s:if>
			<td class="whiteboxwk">
				<a id="rcAssignment" href="#" onClick="window.open('${pageContext.request.contextPath}/rateContract/rateContractAssignment!search.action?zoneIdsString=<s:property  value="ward.parent.id"/>&wardIdsString=<s:property  value="ward.id"/>','RateContractAssignmentWindow','width=900,height=700')">
				<s:text name="ratecontract.assignment.link.label" />
				<img id="addrowImg" name="addrowImg" src='${pageContext.request.contextPath}/image/book_open.png' width="18" height="18" border="0"/>
				</a>
			</td>
		</tr>
	</table>
	<table width="100%" id="rateContractDetails" border="0" cellspacing="0" cellpadding="0" align="center">
		<tr>
			<td colspan="7" class="headingwk">
				<div class="arrowiconwk">
					<img src="${pageContext.request.contextPath}/image/arrow.gif" />
				</div>
				<div class="headplacer">
					<s:text name="ratecontract.estimate.header" />
				</div>
			</td>
		</tr>
		<tr>
			<th class="pagetableth">
				<s:text name="ratecontract.rcNo.label" />
			</th>
			<th class="pagetableth">
				<s:text name="ratecontract.search.contractornamecode" />
			</th>
			<th class="pagetableth">
				<s:text name="ratecontract.validfrom.label" />
			</th>
			<th class="pagetableth">
				<s:text name="ratecontract.validto.label" />
			</th>
			<th class="pagetableth">
				<s:text name="ratecontract.rcamount.label" />
			</th>
			<th class="pagetableth">
				<s:text name="ratecontract.utilizedAmount.label" />
			</th>
			<th class="pagetableth"> 
				<s:text name="ratecontract.balanceAmount.label" />
			</th>
		</tr>
		<tr>
			<td class="pagetabletd">
				<span id="rcNumber" class="bold"></span>
			</td>
			<td class="pagetabletd">
				<span id="contractorName" class="bold"></span>
			</td>
			<td class="pagetabletd">
				<span id="validFrom" class="bold"></span>
			</td>
			<td class="pagetabletd">
				<span id="validTo" class="bold"></span>
			</td>
			<td class="pagetabletdAmount">
				<span id="rcAmount" class="bold"></span>
			</td>
			<td class="pagetabletdAmount">
				<span id="utilizedAmount" class="bold"></span>
			</td>
			<td class="pagetabletdAmount">
				<span id="balanceAmount" class="bold"></span>
			</td>
		</tr>
	</table>
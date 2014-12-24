<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<script>
 var elementId = null;
    function showDocumentManager(obj){
    	if(obj.id == 'budgetDocUploadButton'){
    		elementId = 'budgetDocNumber';
    	}else{
    		elementId = "savedbudgetDetailList["+obj+"].documentNumber";
    	}
	    docManager(document.getElementById(elementId).value);
	}
 var docNumberUpdater = function (docNumber){
			document.getElementById(elementId).value = docNumber;
		}
</script>
<div id="detail" style="width:100%;overflow-x:auto; overflow-y:hidden;">
<table align="center" border="0" cellpadding="0" cellspacing="0" width="100%" class="tablebottom" style="border-right:0px solid #C5C5C5;" >
	<tr>
		<th class="bluebgheadtd" width="10%"><s:text name="budgetdetail.budget"/></th>
		<s:if test="%{shouldShowHeaderField('fund') || shouldShowGridField('fund')}">
			<th class="bluebgheadtd" width="10%"><s:text name="fund"/></th>
		</s:if>
		<s:if test="%{shouldShowHeaderField('function') || shouldShowGridField('function')}">
			<th class="bluebgheadtd" width="10%"><s:text name="budgetdetail.function"/></th>
		</s:if>
		
		<th class="bluebgheadtd" width="11%"><s:text name="budgetdetail.budgetGroup"/></th>
		<s:if test="%{shouldShowHeaderField('executingDepartment') || shouldShowGridField('executingDepartment')}">
			<th class="bluebgheadtd" width="16%"><s:text name="budgetdetail.executingDepartment"/></th>
		</s:if>
		<s:if test="%{shouldShowHeaderField('functionary') || shouldShowGridField('functionary')}">
			<th class="bluebgheadtd" width="10%"><s:text name="functionary"/></th>
		</s:if>
		<s:if test="%{shouldShowHeaderField('scheme') || shouldShowGridField('scheme')}">
			<th class="bluebgheadtd" width="10%"><s:text name="scheme"/></th>
		</s:if>
		<s:if test="%{shouldShowHeaderField('subScheme') || shouldShowGridField('subScheme')}">
			<th class="bluebgheadtd" width="10%"><s:text name="subscheme"/></th>
		</s:if>
		
		<s:if test="%{shouldShowHeaderField('boundary') || shouldShowGridField('boundary')}">
			<th class="bluebgheadtd" width="10%"><s:text name="field"/></th>
		</s:if>
		
		<th class="bluebgheadtd" width="16%"><s:text name="budgetdetail.actuals.lastyear"/>(Rs)</th>
		<th class="bluebgheadtd" width="16%"><s:text name="budgetdetail.actuals.currentyear"/>(Rs)</th>
		<th class="bluebgheadtd" id="anticipatoryAmountheading" width="16%"><s:text name="budgetdetail.anticipatoryAmount"/>(Rs)</th>
		<th class="bluebgheadtd" width="16%">
		<s:if test="!isRe()">
		<s:text name="budgetdetail.estimateAmount"/>(Rs)</th>
		</s:if>
		<s:else>
		<s:text name="budgetdetail.estimateAmount.revised"/>(Rs)</th>
		</s:else>
		<s:if test="%{showApprovalDetails()}"> 
			<s:if test="%{enableApprovedAmount()}">
				<th class="bluebgheadtd" width="16%"><s:text name="budgetdetail.approvedAmount"/>(Rs)</th>
			</s:if>
			<th class="bluebgheadtd" with="16%"><s:text name="budgetdetail.remarks"/></th>
		</s:if>
		<s:else>
			<th class="bluebgheadtd" width="16%"><s:text name="delete"/></th>
		</s:else>
		<th class="bluebgheadtd" with="16%">Documents</th>
	</tr>
	<s:hidden  name="detailsLength"  id="detailsLength" value="%{savedbudgetDetailList.size()}"/>	
	<s:iterator value="savedbudgetDetailList" status="stat">
	<tr> 
			<input type='hidden' name="savedbudgetDetailList[<s:property value='#stat.index'/>].id" value="<s:property value='id'/>"/>
			<input type='hidden' name="savedbudgetDetailList[<s:property value='#stat.index'/>].documentNumber" id="savedbudgetDetailList[<s:property value='#stat.index'/>].documentNumber" value="<s:property value='documentNumber'/>"/>
			<td class="blueborderfortd"> <s:property value="budget.name"/> &nbsp;</td>
			<s:if test="%{shouldShowHeaderField('fund') || shouldShowGridField('fund')}">
				<td class="blueborderfortd"> <s:property value="fund.name"/>&nbsp;</td>
			</s:if>
			
			<s:if test="%{shouldShowHeaderField('function') || shouldShowGridField('function')}">
				<td class="blueborderfortd"> <s:property value="function.name"/>&nbsp;</td>
			</s:if>
			<td class="blueborderfortd"> <s:property value="budgetGroup.name"/>&nbsp;</td>
			<s:if test="%{shouldShowHeaderField('executingDepartment') || shouldShowGridField('executingDepartment')}">
				<td class="blueborderfortd"> <s:property value="executingDepartment.deptName"/>&nbsp;</td>
			</s:if>
			<s:if test="%{shouldShowHeaderField('functionary') || shouldShowGridField('functionary')}">
				<td class="blueborderfortd"> <s:property value="functionary.name"/>&nbsp;</td>
			</s:if>
			<s:if test="%{shouldShowField('scheme') || shouldShowGridField('scheme')}">
				<td class="blueborderfortd"> <s:property value="scheme.name"/>&nbsp;</td>
			</s:if>
			<s:if test="%{shouldShowHeaderField('subScheme') || shouldShowGridField('subScheme')}">
				<td class="blueborderfortd"> <s:property value="subScheme.name"/>&nbsp;</td>
			</s:if>
			<s:if test="%{shouldShowHeaderField('boundary') || shouldShowGridField('boundary')}">
				<td class="blueborderfortd"> <s:property value="boundary.name"/>&nbsp;</td>
			</s:if>
			
				<td class="blueborderfortd" style="text-align:right;"> <s:property value="budgetAmountView[#stat.index].previousYearActuals"/>&nbsp;</td>
				<td class="blueborderfortd" style="text-align:right;"> <s:property value="budgetAmountView[#stat.index].currentYearBeActuals"/>&nbsp;</td>
				<td class="blueborderfortd" style="text-align:right;"> <s:property value="anticipatoryAmount"/>&nbsp;</td>
				<s:if test="%{enableOriginalAmount()}">
					<td class="blueborderfortd"> <input type="text" style="text-align:right;size: 50px;" id='savedbudgetDetailList[<s:property value="#stat.index"/>].originalAmount' name='savedbudgetDetailList[<s:property value="#stat.index"/>].originalAmount' value='<s:text name="format.number"><s:param value="originalAmount"/></s:text>'/>&nbsp;</td>
				</s:if>
				<s:else>
					<td class="blueborderfortd" style="text-align:right;"> <s:text name="format.number"><s:param value="originalAmount"/></s:text>&nbsp;</td> 
				</s:else>
				<s:if test="%{showApprovalDetails()}">
					<s:if test="%{enableApprovedAmount()}">
						<td class="blueborderfortd"><input type="text" style="text-align:right;size: 50px;" id='savedbudgetDetailList[<s:property value="#stat.index"/>].approvedAmount' name='savedbudgetDetailList[<s:property value="#stat.index"/>].approvedAmount' value='<s:if test="%{approvedAmount == null || approvedAmount == 0.0}"><s:text name="format.number"><s:param value="originalAmount"/></s:text></s:if><s:else><s:text name="format.number"><s:param value="approvedAmount"/></s:text></s:else>'/>&nbsp;</td>
					</s:if>
					<td class="blueborderfortd"><textarea cols="50" rows="1" style="size:50px" name='savedbudgetDetailList[<s:property value="#stat.index"/>].state.text1' ><s:property value="state.text1"/></textarea></td>
				</s:if>
				<s:else>
					<td class="blueborderfortd"><a href="#" id="<s:property value='id'/>" onclick="return deleteBudgetDetail(this);"><img src="${pageContext.request.contextPath}/images/cancel.png" border="0"/></a></td>
				</s:else>
				<td><input type="submit" class="buttonsubmit" value="Edit" id="budgetDocUploadButton" onclick='showDocumentManager(<s:property value="#stat.index"/>);return false;' /></td>
		</tr>
	</s:iterator>	
</table>
</div>


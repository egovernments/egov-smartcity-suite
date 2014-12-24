<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %> 
<script src="<egov:url path='js/helper.js'/>"></script> 
<div id="amount" > 
<table  width="100%" border="0" cellspacing="0" cellpadding="0">
	 	<tr>
	 		<td class="whiteboxwk" width="12%"><span class="mandatory">*</span><s:text name="rateContract.fund" />:</td>
            <td class="whitebox2wk"><s:select headerKey="-1" headerValue="%{getText('rateContract.default.select')}" name="fund" id="fund" cssClass="selectwk" list="dropdownData.fundList" listKey="id" listValue="name" value="%{fund.id}" />
            </td>
            <td class="whiteboxwk" width="13%"><span class="mandatory">*</span><s:text name="rateContract.function" />:</td>
            <td class="whitebox2wk" colspan="3"><s:select headerKey="-1" headerValue="%{getText('rateContract.default.select')}" name="function" id="function" cssClass="selectwk" list="dropdownData.functionList" listKey="id" listValue="name" value="%{function.id}" onchange="setupBudgetGroups(this);" />
            <egov:ajaxdropdown id="functionDropdown" fields="['Text','Value']" dropdownId='budgetGroup' url='rateContract/ajaxIndentRateContract!loadBudgetGroups.action' selectedValue="%{budgetGroup.id}"/>
            </td>
     	</tr>
            
        <tr>
         	<td class="greyboxwk" ><span class="mandatory">*</span><s:text name="rateContract.amount" />:</td>
            <td class="greybox2wk"><s:textfield id="indentAmount" name="indentAmount" cssClass="selectamount2wk" align="right" onkeyup = "validateNumber(this);"/>
            <span id='errorindentAmount' style="display:none;color:red;font-weight:bold">&nbsp;x</span>
            </td>
        	<td class="greyboxwk" ><span class="mandatory">*</span><s:text name="rateContract.budgetHead" />:</td>
        	<td class="greybox2wk"><s:select headerKey="-1" headerValue="%{getText('rateContract.default.select')}" name="budgetGroup" id="budgetGroup" cssClass="selectwk" list="dropdownData.budgetGroupList" listKey="id" listValue="name" value="%{budgetGroup.id}" />
            &nbsp;&nbsp;<a href="javascript:openViewBudget();"><img src='${pageContext.request.contextPath}/image/book_open.png' alt="View Data" width="16" height="16" border="0"/></a><a href="javascript:openViewBudget();">View Balance</a>
            </td>
        </tr>
</table>
</div>

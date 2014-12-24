<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
	<s:push value="model">
		<s:token/>
		<tr>
			<td colspan="4" class="headingwk">
				<div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
               	<div class="headplacer"><s:text name="CPF.Heading"/></div>
            </td>
        </tr>
		
		<tr>
			<td class="greyboxwk"><span class="mandatory">*</span><s:text name="CPF.Tds"/> </td>
			<td class="greybox2wk"><s:select name="tds" id="tds" list="dropdownData.tdsList" listKey="id" listValue="type" headerKey="-1" headerValue="----Select----"  value="%{tds.id}" /> </td>
		</tr>

		<tr>
			<td class="greyboxwk"><span class="mandatory">*</span><s:text name="CPF.ExpenseAccount"/> </td>
			<td class="greybox2wk">
				<s:textfield class="fieldinput" name="pfIntExpAccountGlcode" id="pfIntExpAccountGlcode" value="%{pfIntExpAccount.glcode}" 
				autocomplete="off"  onkeyup="autocompletecodeForExpenses(this);"  onblur="fillNeibrAfterSplit(this,'pfIntExpAccountName','pfIntExpAccount');" tabindex="1" />
				<s:textfield class="fieldinputlarge" name="pfIntExpAccountName" id="pfIntExpAccountName" 
				value="%{pfIntExpAccount.name}" style="width:210px" readonly="true" tabindex="-1" />
				<s:hidden name="pfIntExpAccount" id="pfIntExpAccount" value="%{pfIntExpAccount.id}" />	
			</td>
		</tr>
		<div id="codescontainer"></div>
		<tr>
			<td class="greyboxwk"><span class="mandatory">*</span><s:text name="CPF.RuleScript"/> </td>
			<td class="greybox2wk"><s:select name="ruleScript" id="ruleScript" list="dropdownData.wfActionList" listKey="id" listValue="description" headerKey="-1" headerValue="----Select----"  value="%{ruleScript.id}" /> </td>
		</tr>
		<tr>
			<td><s:hidden name="id"/></td>
		</tr>
	</s:push>

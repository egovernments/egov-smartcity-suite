<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
	<s:push value="model">
		<s:token/>
		<tr>
			<td colspan="4" class="headingwk">
				<div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
               	<div class="headplacer"><s:text name="CPFTrigger.Heading"/></div>
            </td>
        </tr>
		<tr>
			<td class="greyboxwk"><span class="mandatory">*</span><s:text name="CPF.Tds"/> </td>
			<td class="greybox2wk"><s:select name="tds" id="tds" list="dropdownData.tdsList" listKey="id" listValue="description" headerKey="-1" headerValue="----Select----"  value="%{tds.id}" /> </td>
		</tr>
		
		<tr>
			<td class="greyboxwk"><span class="mandatory">*</span><s:text name="CPF.ExpenseAccount"/> </td>
			<td class="greybox2wk">
				<s:textfield class="fieldinput" name="pfIntExpAccountGlcode" id="pfIntExpAccountGlcode" value="%{pfIntExpAccount.glcode}"  />
				<s:textfield class="fieldinputlarge" name="pfIntExpAccountName" id="pfIntExpAccountName" value="%{pfIntExpAccount.name}" style="width:210px"  />
				<s:hidden name="pfIntExpAccount" id="pfIntExpAccount" value="%{pfIntExpAccount.id}" />	
			</td>
		</tr>
		
		<tr>
			<td class="greyboxwk"><span class="mandatory">*</span><s:text name="CPF.RuleScript"/> </td>
			<td class="greybox2wk"><s:select name="ruleScript" id="ruleScript" list="dropdownData.ruleScriptList" listKey="id" listValue="description" headerKey="-1" headerValue="----Select----"  value="%{ruleScript.id}" /> </td>
		</tr>
		<tr>
	    	<td class="greyboxwk"><span class="mandatory">*</span><s:text name="CPFTrigger.Month"/></td>
	    	<td class="greybox2wk">
		 		<s:select name="month" id="month" list="#{'':'----Select--','1':'January','2':'February','3':'March','4':'April','5':'May','6':'June','7':'July','8':'August','9':'September','10':'October','11':'November','12':'December'}" value="%{pfHeader.month}"/>
		    </td>
		</tr>
		<tr>
			<td class="greyboxwk"><span class="mandatory">*</span><s:text name="CPFTrigger.FinancialYear"/></td>
	    	<td class="greybox2wk">
				<s:select name="financialYear" id="financialYear" list="dropdownData.financialYearList" listKey="id" listValue="finYearRange" headerKey="-1" headerValue="----Select----"  value="%{financialYear.id}" />
			</td>
		<tr>
			<td  colspan="4"><s:hidden name="id"/></td>
		</tr>
		<tr>
			<td  colspan="4"><s:hidden name="pfType"/></td>
		</tr>
	</s:push>

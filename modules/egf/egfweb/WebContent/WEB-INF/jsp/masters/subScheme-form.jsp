<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
			    <td class="bluebox">&nbsp;</td>
				<td class="bluebox" width="20%"><strong><s:text name="subscheme.form.scheme" /><span class="mandatory">*</span></strong></td>
			    <td class="bluebox">
			    	<s:select list="dropdownData.schemeList"  listKey="id" listValue="name" id="scheme" name="scheme" headerKey="0" headerValue="--- Select ---" value="%{scheme.id}" ></s:select>
			    </td>
				<td class="bluebox" width="20%"><strong><s:text name="subscheme.form.name" /><span class="mandatory">*</span></strong></td>
			    <td class="bluebox"><s:textfield id="name" name="name" value="%{name}" cssStyle="width: 250px"/></td>
			</tr>
			<tr>
			    <td class="greybox">&nbsp;</td>
				<td class="greybox"><strong><s:text name="subscheme.form.code" /></strong><span class="mandatory">*</span></td>
			    <td class="greybox"><s:textfield id="code" name="code" value="%{code}"/></td>
				<td class="greybox"><strong><s:text name="subscheme.form.validfrom" /></strong><span class="mandatory">*</span></td>
			    <td class="greybox">
			    	<input type="text"  id="validfrom" name="validfrom" style="width:100px" value='<s:date name="validfrom" format="dd/MM/yyyy"/>' onkeyup="DateFormat(this,this.value,event,false,'3')"/>
			    	<a href="javascript:show_calendar('subSchemeForm.validfrom');" id="calendar0" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>
			    </td>
			</tr>
			<tr>
			    <td class="bluebox">&nbsp;</td>
				<td class="bluebox"><strong><s:text name="subscheme.form.validto" /></strong><span class="mandatory">*</span></td>
			    <td class="bluebox">
   			    	<input type="text"  id="validto" name="validto" style="width:100px" value='<s:date name="validto" format="dd/MM/yyyy"/>' onkeyup="DateFormat(this,this.value,event,false,'3')"/>
			    	<a href="javascript:show_calendar('subSchemeForm.validto');" id="calendar1" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>
			    </td>
				<td class="bluebox"><strong><s:text name="subscheme.form.isactive" /></strong></td>
			    <td class="bluebox"><s:checkbox name="isActive" id="isActive"/></td>
			</tr>
			<tr>
			    <td class="greybox">&nbsp;</td>
				<td class="greybox"><strong><s:text name="subscheme.form.department" /></strong></td>
			    <td class="greybox"><s:select list="dropdownData.departmentList"  listKey="id" listValue="deptName" headerKey="0" headerValue="--- Select ---" name="department" id="department" value="%{department.id}"></s:select></td>
				<td class="greybox"><strong><s:text name="subscheme.form.initialestimate" /></strong></td>
			    <td class="greybox"><s:textfield cssStyle="text-align: right;" id="initialEstimateAmount" name="initialEstimateAmount" value="%{initialEstimateAmount}"/></td>
			</tr>
			<tr>
			    <td class="bluebox">&nbsp;</td>
				<td class="bluebox"><strong><s:text name="subscheme.form.councilloanproposalnumber" /></strong></td>
			    <td class="bluebox"><s:textfield id="councilLoanProposalNumber" name="councilLoanProposalNumber" value="%{councilLoanProposalNumber}"/></td>
				<td class="bluebox"><strong><s:text name="subscheme.form.councilloanproposaldate" /></strong></td>
			    <td class="bluebox">
   			    	<input type="text"  id="councilLoanProposalDate" name="councilLoanProposalDate" style="width:100px" value='<s:date name="councilLoanProposalDate" format="dd/MM/yyyy"/>' onkeyup="DateFormat(this,this.value,event,false,'3')"/>
			    	<a href="javascript:show_calendar('subSchemeForm.councilLoanProposalDate');" id="calendar2" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>
			    </td>
			</tr>
			<tr>
			    <td class="greybox">&nbsp;</td>
				<td class="greybox"><strong><s:text name="subscheme.form.counciladminsanctionednumber" /></strong></td>
			    <td class="greybox"><s:textfield id="councilAdminSanctionNumber" name="councilAdminSanctionNumber" value="%{councilAdminSanctionNumber}"/></td>
				<td class="greybox"><strong><s:text name="subscheme.form.counciladminsanctioneddate" /></strong></td>
			    <td class="greybox">
   			    	<input type="text"  id="councilAdminSanctionDate" name="councilAdminSanctionDate" style="width:100px" value='<s:date name="councilAdminSanctionDate" format="dd/MM/yyyy"/>' onkeyup="DateFormat(this,this.value,event,false,'3')"/>
			    	<a href="javascript:show_calendar('subSchemeForm.councilAdminSanctionDate');" id="calendar3" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>
			    </td>
			</tr>
			<tr>
			    <td class="bluebox">&nbsp;</td>
				<td class="bluebox"><strong><s:text name="subscheme.form.governmentloanproposalnumber" /></strong></td>
			    <td class="bluebox"><s:textfield id="govtLoanProposalNumber" name="govtLoanProposalNumber" value="%{govtLoanProposalNumber}"/></td>
				<td class="bluebox"><strong><s:text name="subscheme.form.governmentloanproposaldate" /></strong></td>
			    <td class="bluebox">
   			    	<input type="text"  id="govtLoanProposalDate" name="govtLoanProposalDate" style="width:100px" value='<s:date name="govtLoanProposalDate" format="dd/MM/yyyy"/>' onkeyup="DateFormat(this,this.value,event,false,'3')"/>
			    	<a href="javascript:show_calendar('subSchemeForm.govtLoanProposalDate');" id="calendar4" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>
			    </td>
			</tr>
			<tr>
			    <td class="greybox">&nbsp;</td>
				<td class="greybox"><strong><s:text name="subscheme.form.governmentadminsanctionnumber" /></strong></td>
			    <td class="greybox"><s:textfield id="govtAdminSanctionNumber" name="govtAdminSanctionNumber" value="%{govtAdminSanctionNumber}"/></td>
				<td class="greybox"><strong><s:text name="subscheme.form.governmentadminsanctiondate" /></strong></td>
			    <td class="greybox">
   			    	<input type="text"  id="govtAdminSanctionDate" name="govtAdminSanctionDate" style="width:100px" value='<s:date name="govtAdminSanctionDate" format="dd/MM/yyyy"/>' onkeyup="DateFormat(this,this.value,event,false,'3')"/>
			    	<a href="javascript:show_calendar('subSchemeForm.govtAdminSanctionDate');" id="calendar5" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>
			    </td>
			</tr>
		</table>
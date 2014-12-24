<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<html>  
<head>  
    <title><s:text name="subScheme.add"/></title>
     <script type="text/javascript">
		function validate(){
			if(document.getElementById('name').value == null || document.getElementById('name').value==''){
				alert("Please enter Name");
				return false;
			}
			if(document.getElementById('code').value == null || document.getElementById('code').value==''){
				alert("Please enter Code");
				return false;
			}
			if(document.getElementById('validfrom').value == null || document.getElementById('validfrom').value==''){
				alert("Please enter Valid From Date");
				return false;
			}
			if(document.getElementById('validto').value == null || document.getElementById('validto').value==''){
				alert("Please enter Valid To Date");
				return false;
			}
			if(isNaN(document.getElementById('initialEstimateAmount').value)){
				alert("Please enter valid Initial Eastimate Amount");
				return false;
			}
			return true;
		}
		
</script>

</head>  
	<body>  
		<jsp:include page="../budget/budgetHeader.jsp"/>
		<div class="formmainbox"><div class="subheadnew"><s:text name="subScheme.add"/></div>
		<div style="color: red">
		<s:actionmessage theme="simple"/>
		<s:actionerror/>  
		<s:fielderror />
		</div>  
		<s:form name="subSchemeForm" action="subScheme" theme="simple" >
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
			    <td class="bluebox">&nbsp;</td>
				<td class="bluebox" width="20%"><strong>Scheme<span class="mandatory">*</span></strong></td>
			    <td class="bluebox">
			    	<s:select list="dropdownData.schemeList"  listKey="id" listValue="name" id="scheme" name="scheme" headerKey="0" headerValue="--- Select ---" value="%{scheme.id}" ></s:select>
			    </td>
				<td class="bluebox" width="20%"><strong>Name<span class="mandatory">*</span></strong></td>
			    <td class="bluebox"><s:textfield id="name" name="name" value="%{name}" cssStyle="width: 250px"/></td>
			</tr>
			<tr>
			    <td class="greybox">&nbsp;</td>
				<td class="greybox"><strong>Code</strong><span class="mandatory">*</span></td>
			    <td class="greybox"><s:textfield id="code" name="code" value="%{code}"/></td>
				<td class="greybox"><strong>Valid From</strong><span class="mandatory">*</span></td>
			    <td class="greybox">
			    	<input type="text"  id="validfrom" name="validfrom" style="width:100px" value='<s:date name="validfrom" format="dd/MM/yyyy"/>' onkeyup="DateFormat(this,this.value,event,false,'3')"/>
			    	<a href="javascript:show_calendar('subSchemeForm.validfrom');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>
			    </td>
			</tr>
			<tr>
			    <td class="bluebox">&nbsp;</td>
				<td class="bluebox"><strong>Valid To</strong><span class="mandatory">*</span></td>
			    <td class="bluebox">
   			    	<input type="text"  id="validto" name="validto" style="width:100px" value='<s:date name="validto" format="dd/MM/yyyy"/>' onkeyup="DateFormat(this,this.value,event,false,'3')"/>
			    	<a href="javascript:show_calendar('subSchemeForm.validto');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>
			    </td>
				<td class="bluebox"><strong>Is Active</strong></td>
			    <td class="bluebox"><s:checkbox name="isActive"/></td>
			</tr>
			<tr>
			    <td class="greybox">&nbsp;</td>
				<td class="greybox"><strong>Department</strong></td>
			    <td class="greybox"><s:select list="dropdownData.departmentList"  listKey="id" listValue="deptName" headerKey="0" headerValue="--- Select ---" name="department" id="department" value="%{department.id}"></s:select></td>
				<td class="greybox"><strong>Initial Estimate Amount</strong></td>
			    <td class="greybox"><s:textfield cssStyle="text-align: right;" id="initialEstimateAmount" name="initialEstimateAmount" value="%{initialEstimateAmount}"/></td>
			</tr>
			<tr>
			    <td class="bluebox">&nbsp;</td>
				<td class="bluebox"><strong>Council Loan Proposal Number</strong></td>
			    <td class="bluebox"><s:textfield id="councilLoanProposalNumber" name="councilLoanProposalNumber" value="%{councilLoanProposalNumber}"/></td>
				<td class="bluebox"><strong>Council Loan Proposal Date</strong></td>
			    <td class="bluebox">
   			    	<input type="text"  id="councilLoanProposalDate" name="councilLoanProposalDate" style="width:100px" value='<s:date name="councilLoanProposalDate" format="dd/MM/yyyy"/>' onkeyup="DateFormat(this,this.value,event,false,'3')"/>
			    	<a href="javascript:show_calendar('subSchemeForm.councilLoanProposalDate');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>
			    </td>
			</tr>
			<tr>
			    <td class="greybox">&nbsp;</td>
				<td class="greybox"><strong>Council Admin Sanctioned Number</strong></td>
			    <td class="greybox"><s:textfield id="councilAdminSanctionNumber" name="councilAdminSanctionNumber" value="%{councilAdminSanctionNumber}"/></td>
				<td class="greybox"><strong>Council Admin Sanctioned Date</strong></td>
			    <td class="greybox">
   			    	<input type="text"  id="councilAdminSanctionDate" name="councilAdminSanctionDate" style="width:100px" value='<s:date name="councilAdminSanctionDate" format="dd/MM/yyyy"/>' onkeyup="DateFormat(this,this.value,event,false,'3')"/>
			    	<a href="javascript:show_calendar('subSchemeForm.councilAdminSanctionDate');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>
			    </td>
			</tr>
			<tr>
			    <td class="bluebox">&nbsp;</td>
				<td class="bluebox"><strong>Government Loan Proposal Number</strong></td>
			    <td class="bluebox"><s:textfield id="govtLoanProposalNumber" name="govtLoanProposalNumber" value="%{govtLoanProposalNumber}"/></td>
				<td class="bluebox"><strong>Government Loan Proposal Date</strong></td>
			    <td class="bluebox">
   			    	<input type="text"  id="govtLoanProposalDate" name="govtLoanProposalDate" style="width:100px" value='<s:date name="govtLoanProposalDate" format="dd/MM/yyyy"/>' onkeyup="DateFormat(this,this.value,event,false,'3')"/>
			    	<a href="javascript:show_calendar('subSchemeForm.govtLoanProposalDate');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>
			    </td>
			</tr>
			<tr>
			    <td class="greybox">&nbsp;</td>
				<td class="greybox"><strong>Government Admin Sanction Number</strong></td>
			    <td class="greybox"><s:textfield id="govtAdminSanctionNumber" name="govtAdminSanctionNumber" value="%{govtAdminSanctionNumber}"/></td>
				<td class="greybox"><strong>Government Admin Sanction Date</strong></td>
			    <td class="greybox">
   			    	<input type="text"  id="govtAdminSanctionDate" name="govtAdminSanctionDate" style="width:100px" value='<s:date name="govtAdminSanctionDate" format="dd/MM/yyyy"/>' onkeyup="DateFormat(this,this.value,event,false,'3')"/>
			    	<a href="javascript:show_calendar('subSchemeForm.govtAdminSanctionDate');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>
			    </td>
			</tr>
		</table>
		<br/><br/>
		<div class="buttonbottom" style="padding-bottom:10px;"> 
			<s:submit name="Save" value="Save" method="save" onclick="javascript: return validate();" cssClass="buttonsubmit" />
			<input type="button" id="Close" value="Close" onclick="javascript:window.close()" class="button"/>
		</div>
		<s:token/>
		</s:form>
		<script type="text/javascript">
		<s:if test="%{clearValues == true}">
			document.getElementById('scheme').value = 0;
			document.getElementById('department').value = 0;
			document.getElementById('name').value = "";
			document.getElementById('code').value = "";
			document.getElementById('validfrom').value = "";
			document.getElementById('validto').value = "";
			document.getElementById('initialEstimateAmount').value = "";
			
			document.getElementById('councilLoanProposalNumber').value = "";
			document.getElementById('councilLoanProposalDate').value = "";
			document.getElementById('councilAdminSanctionNumber').value = "";
			document.getElementById('councilAdminSanctionDate').value = "";
			document.getElementById('govtLoanProposalNumber').value = "";
			document.getElementById('govtLoanProposalDate').value = "";
			document.getElementById('govtAdminSanctionNumber').value = "";
			document.getElementById('govtAdminSanctionDate').value = "";
		</s:if>
		</script>  
	</body>  
</html>
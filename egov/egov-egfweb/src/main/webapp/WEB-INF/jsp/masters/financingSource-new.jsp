<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  ~
  --%>


<%@ taglib prefix="s" uri="/WEB-INF/tags/struts-tags.tld"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>

<%@ page language="java"%>
<html>

<head>
<script type="text/javascript"
	src="/EGF/resources/javascript/ajaxCommonFunctions.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/calendar.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/dateValidation.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/javascript/jquery.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/javascript/financingSource.js?rnd=${app_release_no}"></script>
<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1252">

<title>Financial Source Create</title>

</head>


<body id="body" onload="onloadtask();">

	<jsp:include page="../budget/budgetHeader.jsp">
		<jsp:param name="heading" value="Financial Source Create" />
	</jsp:include>
	<span class="mandatory"> <font
		style='color: red; font-weight: bold'> <s:actionerror /> <s:fielderror />
	</font>
	</span>
	<s:if test="hasActionMessages()">
		<font style='color: green; font-weight: bold'> <s:actionmessage />
		</font>
	</s:if>

	<div class="formheading" />
	<div class="subheadnew">Financial Source</div>
	<font style='color: red; font-weight: bold'>
		<p class="error-block" id="lblError"></p>
	</font>
	<span align="center" style="display: none" id="finSrcCodeUpperunique">
		<font style='color: red; font-weight: bold'> <s:text
				name="masters.finsrc.code.already.exists" />
	</font>
	</span>
	<span align="center" style="display: none" id="finSrcNameUpperunique">
		<font style='color: red; font-weight: bold'> <s:text
				name="masters.finsrc.code.already.exists" />
	</font>
	</span>
	<span align="center" style="display: none" id="finSrcCodeMiddleunique">
		<font style='color: red; font-weight: bold'> <s:text
				name="masters.finsrc.code.already.exists" />
	</font>
	</span>
	<span align="center" style="display: none" id="finSrcNameMiddleunique">
		<font style='color: red; font-weight: bold'> <s:text
				name="masters.finsrc.code.already.exists" />
	</font>
	</span>
	<s:form theme="simple" name="finsrcheaderform">
		<table border="0" width="100%">
			<tr class="greybox">
				<td class="bluebox"><s:radio name="subschmselectionOpt"
						id="subschmselectionOpt"
						list="#{'1':'With Subscheme','0':'Without Subscheme'}"
						value="%{0}" onchange="ShowRelatedHeader(this);" /></td>
			</tr>
		</table>
		<table border="0" width="100%" id="finSrcHeaderUpper">


			<c:set var="tdclass" value="bluebox" scope="request" />
			<tr>
				<c:choose>
					<c:when test="${tdclass == 'bluebox'}">
						<c:set var="tdclass" value="greybox" scope="request" />
					</c:when>
					<c:otherwise>
						<c:set var="tdclass" value="bluebox" scope="request" />
					</c:otherwise>
				</c:choose>
				<td class="<c:out value='${tdclass}' />" width="25%"><s:text
						name="masters.funsrc.code" /><span class="mandatory">*</span></td>
				<td class="<c:out value='${tdclass}' />" width="25%"><s:textfield
						name="codeUpper" id="codeUpper" maxlength="50"
						onkeyup="uniqueCheckCodeUpper();"
						onblur="clearCodeUpperIfExists();" /></td>
				<egov:uniquecheck id="finSrcCodeUpperunique" fields="['Value']"
					url='masters/financingSource!codeUniqueCheck.action'
					key='masters.finsrc.code.already.exists' />
				<td class="<c:out value='${tdclass}' />" width="25%"><s:text
						name="masters.funsrc.name" /><span class="mandatory">*</span></td>
				<td class="<c:out value='${tdclass}' />" width="25%"><s:textfield
						name="nameUpper" id="nameUpper" maxlength="50"
						onkeyup="uniqueCheckNameUpper();"
						onblur="clearNameUpperIfExists();" /></td>
				<egov:uniquecheck id="finSrcNameUpperunique" fields="['Value']"
					url='masters/financingSource!nameUniqueCheck.action'
					key='masters.finsrc.name.already.exists' />
			</tr>
			<tr>
				<c:choose>
					<c:when test="${tdclass == 'bluebox'}">
						<c:set var="tdclass" value="greybox" scope="request" />
					</c:when>
					<c:otherwise>
						<c:set var="tdclass" value="bluebox" scope="request" />
					</c:otherwise>
				</c:choose>
				<td class="<c:out value='${tdclass}' />" width="25%"><s:text
						name="masters.finsrc.srcAmount" /><span class="mandatory">*</span></td>
				<td class="<c:out value='${tdclass}' />" width="25%"><s:textfield
						name="sourceAmountUpper" id="sourceAmountUpper" maxlength="18"
						onkeyup="validateOnlyNumber(this);"
						onblur="validateDigitsAndDecimal(this);calcPercAmt();" /></td>

				<td class="<c:out value='${tdclass}' />" width="25%">Is Active</td>
				<td class="<c:out value='${tdclass}' />" width="25%"><s:checkbox
						name="isactiveChkUpper" id="isactiveChkUpper" checked="checked" /></td>
			</tr>
		</table>
		<table border="0" width="100%" id="finSrcHeaderMiddle"
			style="display: none">
			<tr>
				<c:choose>
					<c:when test="${tdclass == 'bluebox'}">
						<c:set var="tdclass" value="greybox" scope="request" />
					</c:when>
					<c:otherwise>
						<c:set var="tdclass" value="bluebox" scope="request" />
					</c:otherwise>
				</c:choose>
				<td class="<c:out value='${tdclass}' />" width="25%"><s:text
						name="masters.finsrc.fundingType" /><span class="mandatory">*</span></td>
				</td>
				<td class="<c:out value='${tdclass}' />" width="25%"><s:select
						name="fundingType" id="fundingType"
						list="dropdownData.fundingTypeList" headerKey="-1"
						headerValue="----Choose----" value="fundsource.fundingType"
						onchange="manipulateHeaderByFundingType(this);manipulateHeaderdisable(this,'finSrcHeaderLower|');" /></td>

				<td class="<c:out value='${tdclass}' />" width="25%"><s:text
						name="masters.subscheme" /><span class="mandatory">*</span></td>
				<td class="<c:out value='${tdclass}' />" width="25%"><s:select
						name="subSchemeId" id="subschemeid"
						list="dropdownData.subschemeList" listKey="id" listValue="name"
						headerKey="-1" headerValue="----Choose----"
						onchange="loadAccNum(this);" /></td>
				<egov:ajaxdropdown id="accountNumber" fields="['Text','Value']"
					dropdownId="accountNumber"
					url="/voucher/common!ajaxLoadBankAccountsBySubscheme.action" />
			</tr>
			<tr>
				<c:choose>
					<c:when test="${tdclass == 'bluebox'}">
						<c:set var="tdclass" value="greybox" scope="request" />
					</c:when>
					<c:otherwise>
						<c:set var="tdclass" value="bluebox" scope="request" />
					</c:otherwise>
				</c:choose>
				<td class="<c:out value='${tdclass}' />" width="25%"><s:text
						name="masters.funsrc.code" /><span class="mandatory">*</span></td>
				<td class="<c:out value='${tdclass}' />" width="25%"><s:textfield
						name="codeMiddle" id="codeMiddle" maxlength="50"
						onkeyup="uniqueCheckCodeMiddle();"
						onblur="clearCodeMiddleIfExists();" /></td>
				<egov:uniquecheck id="finSrcCodeMiddleunique" fields="['Value']"
					url='masters/financingSource!codeUniqueCheck.action'
					key='masters.finsrc.code.already.exists' />
				<td class="<c:out value='${tdclass}' />" width="25%"><s:text
						name="masters.funsrc.name" /><span class="mandatory">*</span></td>
				<td class="<c:out value='${tdclass}' />" width="25%"><s:textfield
						name="nameMiddle" id="nameMiddle" maxlength="50"
						onkeyup="uniqueCheckNameMiddle();"
						onblur="clearNameMiddleIfExists();" /></td>
				<egov:uniquecheck id="finSrcNameMiddleunique" fields="['Value']"
					url='masters/financingSource!nameUniqueCheck.action'
					key='masters.finsrc.name.already.exists' />
			</tr>
			<tr>
				<c:choose>
					<c:when test="${tdclass == 'bluebox'}">
						<c:set var="tdclass" value="greybox" scope="request" />
					</c:when>
					<c:otherwise>
						<c:set var="tdclass" value="bluebox" scope="request" />
					</c:otherwise>
				</c:choose>
				<td class="<c:out value='${tdclass}' />" width="25%"><s:text
						name="masters.finsrc.srcAmount" /><span class="mandatory">*</span></td>
				<td class="<c:out value='${tdclass}' />" width="25%"><s:textfield
						name="sourceAmountMiddle" id="sourceAmountMiddle" maxlength="18"
						onkeyup="validateOnlyNumber(this);"
						onblur="validateDigitsAndDecimal(this);calcPercAmt();" /></td>

				<td class="<c:out value='${tdclass}' />" width="25%">Is Active</td>
				<td class="<c:out value='${tdclass}' />" width="25%"><s:checkbox
						name="isactiveChkMiddle" id="isactiveChkMiddle" checked="checked" /></td>
			</tr>
			<tr>
				<c:choose>
					<c:when test="${tdclass == 'bluebox'}">
						<c:set var="tdclass" value="greybox" scope="request" />
					</c:when>
					<c:otherwise>
						<c:set var="tdclass" value="bluebox" scope="request" />
					</c:otherwise>
				</c:choose>
				<td class="<c:out value='${tdclass}' />" width="25%"><s:text
						name="masters.finsrc.fininstname" /><span class="mandatory"
					id="subschememandate">*</span></td>
				<td class="<c:out value='${tdclass}' />" width="25%"><s:select
						name="finInstId" id="finInstId" listKey="id" listValue="name"
						list="dropdownData.finInstList" headerKey="-1"
						headerValue="----Choose----" /></td>

				<td class="<c:out value='${tdclass}' />" width="25%"><s:text
						name="masters.finsrc.RtOfIntr" /><span class="mandatory"
					id="rateOfIntrestMandate" style="visibility: hidden">*</span></td>
				<td class="<c:out value='${tdclass}' />" width="25%"><s:textfield
						name="rateOfIntrest" id="rateOfIntrest" maxlength="8"
						onkeyup="validateDecimal(this);" /></td>

			</tr>
			<tr>
				<c:choose>
					<c:when test="${tdclass == 'bluebox'}">
						<c:set var="tdclass" value="greybox" scope="request" />
					</c:when>
					<c:otherwise>
						<c:set var="tdclass" value="bluebox" scope="request" />
					</c:otherwise>
				</c:choose>
				<td class="<c:out value='${tdclass}' />" width="25%"><s:text
						name="masters.finsrc.prdOfLoan" /><span class="mandatory"
					id="prdLoanMandate" style="visibility: hidden">*</span></td>
				<td class="<c:out value='${tdclass}' />" width="25%"><s:textfield
						name="loanPeriod" id="loanPeriod" maxlength="8"
						onkeyup="validateDecimal(this);" /></td>

				<td class="<c:out value='${tdclass}' />" width="25%"><s:text
						name="masters.finsrc.moratoriumPrd" /><span class="mandatory"
					id="mrtmPrdMandate" style="visibility: hidden">*</span></td>
				<td class="<c:out value='${tdclass}' />" width="25%"><s:textfield
						name="moratoriumPeriod" id="moratoriumPeriod" maxlength="8"
						onkeyup="validateDecimal(this);" /></td>

			</tr>
			<tr>
				<c:choose>
					<c:when test="${tdclass == 'bluebox'}">
						<c:set var="tdclass" value="greybox" scope="request" />
					</c:when>
					<c:otherwise>
						<c:set var="tdclass" value="bluebox" scope="request" />
					</c:otherwise>
				</c:choose>
				<td class="<c:out value='${tdclass}' />" width="25%"><s:text
						name="masters.finsrc.rePymtFrq" /><span class="mandatory"
					id="repFrqMandate" style="visibility: hidden">*</span></td>
				<td class="<c:out value='${tdclass}' />" width="25%"><s:select
						name="repaymentFrequency" id="repaymentFrequency"
						list="rePymntFrqList" headerKey="-1" headerValue="----Choose----" /></td>

				<td class="<c:out value='${tdclass}' />" width="25%"><s:text
						name="masters.finsrc.noOfInstalments" /><span class="mandatory"
					id="noOfInstMandate" style="visibility: hidden">*</span></td>
				<td class="<c:out value='${tdclass}' />" width="25%"><s:textfield
						name="noOfInstallment" id="noOfInstallment" maxlength="4"
						onkeyup="validateOnlyNumber(this)" /></td>

			</tr>
			<tr>
				<c:choose>
					<c:when test="${tdclass == 'bluebox'}">
						<c:set var="tdclass" value="greybox" scope="request" />
					</c:when>
					<c:otherwise>
						<c:set var="tdclass" value="bluebox" scope="request" />
					</c:otherwise>
				</c:choose>
				<td class="<c:out value='${tdclass}' />" width="25%"><s:text
						name="masters.finsrc.GovtOrder" /></td>
				<td class="<c:out value='${tdclass}' />" width="25%"><s:textfield
						name="govtOrder" id="govtOrder" maxlength="250" /></td>

				<td class="<c:out value='${tdclass}' />" width="25%"><s:text
						name="masters.finsrc.GovtOrderDate" /></td>
				<td class="<c:out value='${tdclass}' />" width="25%"><s:textfield
						name="govtDate" id="govtDate" maxlength="10"
						onkeyup="DateFormat(this,this.value,event,false,'3')" /> <a
					href="javascript:show_calendar('finsrcheaderform.govtDate',null,null,'DD/MM/YYYY');"
					style="text-decoration: none">&nbsp;<img tabIndex=-1
						src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a>(dd/mm/yyyy)
				</td>


			</tr>
			<tr>
				<c:choose>
					<c:when test="${tdclass == 'bluebox'}">
						<c:set var="tdclass" value="greybox" scope="request" />
					</c:when>
					<c:otherwise>
						<c:set var="tdclass" value="bluebox" scope="request" />
					</c:otherwise>
				</c:choose>
				<td class="<c:out value='${tdclass}' />" width="25%"><s:text
						name="masters.finsrc.dpCodeNum" /></td>
				<td class="<c:out value='${tdclass}' />" width="25%"><s:textfield
						name="dpCodeNum" id="dpCodeNum" maxlength="250" /></td>

				<td class="<c:out value='${tdclass}' />" width="25%"></td>
				<td class="<c:out value='${tdclass}' />" width="25%"></td>
			</tr>
			<tr>
				<c:choose>
					<c:when test="${tdclass == 'bluebox'}">
						<c:set var="tdclass" value="greybox" scope="request" />
					</c:when>
					<c:otherwise>
						<c:set var="tdclass" value="bluebox" scope="request" />
					</c:otherwise>
				</c:choose>
				<td class="<c:out value='${tdclass}' />" width="25%"><s:text
						name="masters.finsrc.finInstletterNum" /></td>
				<td class="<c:out value='${tdclass}' />" width="25%"><s:textfield
						name="finInstLetterNum" id="finInstLetterNum" maxlength="250" /></td>

				<td class="<c:out value='${tdclass}' />" width="25%"><s:text
						name="masters.finsrc.finInstLetterDt" /></td>
				<td class="<c:out value='${tdclass}' />" width="25%"><s:textfield
						name="finInstLetterDate" id="finInstLetterDate" maxlength="10"
						onkeyup="DateFormat(this,this.value,event,false,'3')" /> <a
					href="javascript:show_calendar('finsrcheaderform.finInstLetterDate',null,null,'DD/MM/YYYY');"
					style="text-decoration: none">&nbsp;<img tabIndex=-1
						src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a>(dd/mm/yyyy)
				</td>

			</tr>
			<tr>
				<c:choose>
					<c:when test="${tdclass == 'bluebox'}">
						<c:set var="tdclass" value="greybox" scope="request" />
					</c:when>
					<c:otherwise>
						<c:set var="tdclass" value="bluebox" scope="request" />
					</c:otherwise>
				</c:choose>
				<td class="<c:out value='${tdclass}' />" width="25%"><s:text
						name="masters.finsrc.finInstSchmNum" /></td>
				<td class="<c:out value='${tdclass}' />" width="25%"><s:textfield
						name="finInstSchmNum" id="finInstSchmNum" /></td>

				<td class="<c:out value='${tdclass}' />" width="25%"><s:text
						name="masters.finsrc.finInstSchDt" /></td>
				<td class="<c:out value='${tdclass}' />" width="25%"><s:textfield
						name="finInstSchmDate" id="finInstSchmDate" maxlength="10"
						onkeyup="DateFormat(this,this.value,event,false,'3')" /> <a
					href="javascript:show_calendar('finsrcheaderform.finInstSchmDate',null,null,'DD/MM/YYYY');"
					style="text-decoration: none">&nbsp;<img tabIndex=-1
						src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a>(dd/mm/yyyy)
				</td>
			</tr>
			<tr>
				<c:choose>
					<c:when test="${tdclass == 'bluebox'}">
						<c:set var="tdclass" value="greybox" scope="request" />
					</c:when>
					<c:otherwise>
						<c:set var="tdclass" value="bluebox" scope="request" />
					</c:otherwise>
				</c:choose>
				<td class="<c:out value='${tdclass}' />" width="25%"><s:text
						name="masters.finsrc.amtPerc" /></td>
				<td class="<c:out value='${tdclass}' />" width="25%"><s:textfield
						name="loanPercentage" id="loanPercentage" maxlength="8"
						onkeyup="validateDecimal(this);" readOnly="true" /></td>

				<td class="<c:out value='${tdclass}' />" width="25%"><s:text
						name="masters.finsrc.bankAcc" /><span class="mandatory">*</span></td>
				<td class="<c:out value='${tdclass}' />" width="25%"><s:select
						name="bankAccountId" id="accountNumber"
						list="dropdownData.accNumList" listKey="id"
						listValue="accountnumber" headerKey="-1"
						headerValue="----Choose----" /></td>
			</tr>
		</table>
		<br>
		<br>
		<div class="subheadnew" style="display: none" id="shrdfinSrcHead">Shared
			Financial Source</div>
		<font style='color: red; font-weight: bold'>
			<p class="error-block" id="lblErrorOwnSrc"></p>
		</font>
		<table border="0" width="100%" id="finSrcHeaderLower"
			style="display: none">
			<tr>

				<td class="bluebox" width="25%"><s:text
						name="masters.finsrc.fundingTypeOwnSrc" /><span class="mandatory">*</span></td>
				<td class="bluebox" width="25%"><s:select
						name="finSrcTypOwnSrc" id="finSrcOwnSrc"
						list="dropdownData.finSrcTypOwnSrcList" headerKey="-1"
						headerValue="----Choose----" listKey="id" listValue="name"
						onchange="calcPercOwnSrc();manipulateHeaderdisable(this,'finSrcHeaderUpper|finSrcHeaderMiddle');" /></td>
			</tr>
			<tr>
				<td class="greybox" width="25%"><s:text
						name="masters.finsrc.srcAmount" /><span class="mandatory">*</span></td>
				<td class="greybox" width="25%"><s:textfield
						name="sourceAmountOwnSrc" id="sourceAmountOwnSrc" maxlength="18"
						onkeyup="validateOnlyNumber(this);"
						onblur="validateDigitsAndDecimal(this);calcSrcAmtPercAmt();" /></td>

				<td class="greybox" width="25%"><s:text
						name="masters.finsrc.amtPerc" /></td>
				<td class="greybox" width="25%"><s:textfield name="ownSrcPerc"
						id="ownSrcPerc" maxlength="8" onkeyup="validateDecimal(this);"
						readOnly="true" /></td>

			</tr>
		</table>
		<br>

		<table width="90%" cellspacing="0" cellpadding="0" border="0"
			class="tablebottom" align="center">
			<tr align="center">

				<td align="center" class="blueborderfortd1"
					style="text-align: center"><input type="button" name="Done"
					onclick="updateGridData()" class="buttongeneral" value="Done"
					align="middle" /></td>

			</tr>
		</table>



		<s:hidden name="initEstAmt" id="initEstAmt" />
	</s:form>
	<font style='color: red; font-weight: bold'>
		<p class="error-block" id="lblGridError"></p>
	</font>
	<s:form action="financingSource" theme="simple" name="finSourceForm"
		id="finSourceForm">
		<jsp:include page="financingSourceGrid.jsp" />
		<div id="finSrcGrid" align="center">
			<table width="80%" id="labelid">
				<th>Financial Source Grid</th>
			</table>

			<div class="yui-skin-sam" align="center">
				<div id="fundSourceGridTable">


					<script>
		
		makeFundSourceGridTable();
		document.getElementById('fundSourceGridTable').getElementsByTagName('table')[0].width="80%"
	 </script>
					<br>
					<s:submit type="submit" cssClass="buttonsubmit" value="Save"
						id="save" name="save" method="save"
						onClick="return validateGrid();" />
					<input type="button" id="Close" value="Close"
						onclick="javascript:window.close()" class="buttonsubmit" />
				</div>
			</div>
		</div>
		<br>
		<s:token />
	</s:form>
	<script>
document.getElementById("finSrcGrid").style.display="none";
</script>



</body>

</html>

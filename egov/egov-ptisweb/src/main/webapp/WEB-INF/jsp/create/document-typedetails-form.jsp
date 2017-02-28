<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2016>  eGovernments Foundation
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
  --%>

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>

<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td colspan="2">
			<div class="headingsmallbg">Documents</div>
		</td>
		<td colspan="2">
			<div class="headingsmallbg" style="text-align: right;">
				<s:if test="%{showTaxCalcBtn}">
					<input type="button" name="calculateTax" id="calculateTax"
						value="Show Tax" class="buttonsubmit" />
				</s:if>
			</div>
		</td>
	</tr>
	<tr>
		<td class="greybox" width="5%">&nbsp;</td>
		<td class="greybox" width="25%">
			<div class="documentType">
				<s:text name="lbl.doctype"></s:text>
				<span class="mandatory1">*</span> :
			</div>
		</td>
		<s:hidden name="documentTypeDetails.id"
			value="%{documentTypeDetails.id}"></s:hidden>
		<td class="greybox" width="">
			<div class="documentType">
				<s:select headerKey="-1" headerValue="%{getText('default.select')}"
					name="documentTypeDetails.documentName"
					id="assessmentDocumentNames"
					list="dropdownData.assessmentDocumentNameList" cssClass="selectnew"
					title="Different document types"
					onchange="populateDefaultCitizen();" />
			</div>

		</td>
	</tr>
	<tr class="docNoDate">
		<td class="greybox">&nbsp;</td>
		<td class="greybox" id="docNoLabel"><s:text name="">No</s:text><span
			class="mandatory1">*</span> :</td>
		<td class="greybox"><s:textfield
				name="documentTypeDetails.documentNo" id="docNo"
				value="%{documentTypeDetails.documentNo}" size="16" maxlength="16"></s:textfield>
		</td>
		<td class="greybox" id="docDateLabel"><s:text name="">Date</s:text><span
			class="mandatory1">*</span> :</td>
		<td class="greybox"><s:date
				name="documentTypeDetails.documentDate" var="documentDate"
				format="dd/MM/yyyy" /> <s:textfield
				name="documentTypeDetails.documentDate" id="docDate"
				value="%{#documentDate}" size="12" autocomplete="off" maxlength="12"
				cssClass="datepicker"></s:textfield></td>
	</tr>
	<div>
		<tr class="proceeding">
			<td class="greybox">&nbsp;</td>
			<td class="greybox"><s:text name="lbl.dtd.procno"></s:text><span
				class="mandatory1">*</span> :</td>
			<td class="greybox"><s:textfield
					name="documentTypeDetails.proceedingNo" id="proceedingNo"
					value="%{documentTypeDetails.proceedingNo}" size="16"
					maxlength="16"></s:textfield></td>
			<td class="greybox"><s:text name="lbl.dtd.procdate"></s:text><span
				class="mandatory1">*</span> :</td>
			<td class="greybox"><s:date
					name="documentTypeDetails.proceedingDate" var="proceedingDate"
					format="dd/MM/yyyy" /> <s:textfield
					name="documentTypeDetails.proceedingDate" title="Document dated"
					id="proceedingDate" value="%{#proceedingDate}" size="12"
					autocomplete="off" maxlength="12" cssClass="datepicker"></s:textfield></td>
		</tr>
	</div>
	<tr>
		<td class="greybox">&nbsp;</td>
		<td class="greybox"><div class="courtName">
				<s:text name="lbl.dtd.courtname"></s:text>
				<span class="mandatory1">*</span> :
			</div></td>
		<td class="greybox"><div class="courtName">
				<s:textfield name="documentTypeDetails.courtName" id="courtname"
					value="%{documentTypeDetails.courtName}" size="16" maxlength="16"></s:textfield>
			</div></td>
		<td class="greybox" align="left">
			<div class="signed">
				<s:text name="lbl.dtd.signed"></s:text>
			</div>
		</td>
		<td class="greybox">
			<div class="signed">
				<s:checkbox name="documentTypeDetails.signed" id="signedCheck" />
			</div>
		</td>
	</tr>
</table>

<script>
	jQuery(document).ready(function() {
		documentTypeEdit();
	});
</script>
<script type="text/javascript"
	src="<cdn:url value='/resources/javascript/documentdetails.js?rnd=${app_release_no}'/>"></script>
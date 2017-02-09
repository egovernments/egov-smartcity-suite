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
	<tr>
		<td colspan="5">
			<table class="tablebottom doctable" id="nameTable" width="100%"
				border="0" cellpadding="0" cellspacing="0">
				<tbody>
					<tr>
						<th class="bluebgheadtd"><s:text name="doctable.sno" /></th>
						<th class="bluebgheadtd"><s:text name="doctable.doctype" /></th>
						<th class="bluebgheadtd"><s:text name="upload.file" /></th>
					</tr>
					<s:iterator value="assessmentDocumentTypes" status="status"
						var="documentType">
						<s:set value="name.replace(' ', '_')" var="docId" />
						<s:set value="%{name.replace(' ', '_')+'_Idx'}" var="docIdIndex" />
						<tr id="<s:property value="#docId" />">
							<td class="blueborderfortd" style="text-align: left"
								id="<s:property value="#docIdIndex" />"><span class="bold"><s:property
										value="#status.index + 1" /></span></td>
							<td class="blueborderfortd" style="text-align: left"><s:property
									value="name" /> <s:if test="mandatory">
									<span class="mandatory1">*</span>
								</s:if></td>
							<td class="blueborderfortd" style="text-align: left"><s:if
									test="%{assessmentDocuments.isEmpty()}">
									<s:hidden name="assessmentDocuments[%{#status.index}].type.id"
										value="%{id}"></s:hidden>
									<s:if test="mandatory">
										<s:file name="assessmentDocuments[%{#status.index}].uploads"
											value="%{assessmentDocuments[#status.index].uploads}"
											cssClass="button" required="true" />
									</s:if>
									<s:else>
										<s:file name="assessmentDocuments[%{#status.index}].uploads"
											value="%{assessmentDocuments[#status.index].uploads}"
											cssClass="button" />
									</s:else>
								</s:if> <s:elseif
									test="%{assessmentDocuments[#status.index].files.isEmpty()}">
									<s:if
										test="%{assessmentDocuments[#status.index].id == null || assessmentDocuments[#status.index].type == null}">
										<s:hidden name="assessmentDocuments[%{#status.index}].type.id"
											value="%{id}"></s:hidden>
									</s:if>
									<s:hidden name="assessmentDocuments[%{#status.index}].id" />
									<s:if test="mandatory">
										<s:file name="assessmentDocuments[%{#status.index}].uploads"
											value="%{assessmentDocuments[#status.index].uploads}"
											cssClass="button" required="true" />
									</s:if>
									<s:else>
										<s:file name="assessmentDocuments[%{#status.index}].uploads"
											value="%{assessmentDocuments[#status.index].uploads}"
											cssClass="button" />
									</s:else>
								</s:elseif> <s:else>
									<s:iterator value="%{assessmentDocuments[#status.index].files}">
										<s:hidden name="assessmentDocuments[%{#status.index}].id" />
										<s:if test="%{allowEditDocument}">
											<s:file name="assessmentDocuments[%{#status.index}].uploads"
												value="%{assessmentDocuments[#status.index].uploads}"
												cssClass="button" />
											<a
												href="javascript:viewDocument('<s:property value="fileStoreId"/>')">
												<s:property value="%{fileName}" />
											</a>
										</s:if>
										<s:else>
											<a
												href="javascript:viewDocument('<s:property value="fileStoreId"/>')">
												<s:property value="%{fileName}" />
											</a>
										</s:else>
									</s:iterator>
								</s:else></td>
						</tr>
					</s:iterator>
				</tbody>
			</table>
		</td>
	</tr>
</table>

<script>
	jQuery(document).ready(function() {
		documentTypeEdit();
	});
	function serialNoToggle(dropdownvalue){
		documentTypeToggle(dropdownvalue);
		 if (dropdownvalue.indexOf('Certificate') == -1 && dropdownvalue != 'select'){
			jQuery('#Decree_Document_Idx').html('<span class="bold"><s:property value="1" /></span>');
			jQuery('#Will_Deed_Idx').html('<span class="bold"><s:property value="1" /></span>');
			jQuery('#Registered_Document_Idx').html('<span class="bold"><s:property value="1" /></span>');
			jQuery('#Photo_of_Property_With_Holder_Idx').html('<span class="bold"><s:property value="1" /></span>');
		}
	}
</script>
<script type="text/javascript"
	src="<cdn:url value='/resources/javascript/documentdetails.js?rnd=${app_release_no}'/>"></script>
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
		<td colspan="5">
			<div class="headingsmallbg">
				<s:text name="" />
				Documents
			</div>
		</td>
	</tr>
	<tr>
		<td class="greybox" width="5%">&nbsp;</td>
		<td class="greybox" width="25%">
			<div class="documentType">
				<s:text name="lbl.doctype"></s:text>
				:
			</div>
		</td>
		<td class="greybox" width="">
			<div class="documentType">
				<span class="bold"><s:property
						value="%{documentTypeDetails.documentName}" default="N/A" /></span>
			</div>

		</td>
	</tr>
	<tr class="docNoDate">
		<td class="greybox">&nbsp;</td>
		<td class="greybox" id="docNoLabel"><s:text name="">No</s:text> :</td>
		<td class="greybox"><span class="bold"><s:property
					value="%{documentTypeDetails.documentNo}" default="N/A" /></span></td>
		<td class="greybox" id="docDateLabel"><s:text name="">Date</s:text>
			:</td>
		<td class="greybox"><s:date
				name="documentTypeDetails.documentDate" var="docDate"
				format="dd/MM/yyyy" /> <span class="bold"><s:property
					value="%{#docDate}" default="N/A" /></span></td>
	</tr>
	<div>
		<tr class="proceeding">
			<td class="greybox">&nbsp;</td>
			<td class="greybox"><s:text name="lbl.dtd.procno"></s:text> :</td>
			<td class="greybox"><span class="bold"><s:property
						value="%{documentTypeDetails.proceedingNo}" default="N/A" /></span></td>
			<td class="greybox"><s:text name="lbl.dtd.procdate"></s:text> :</td>
			<td class="greybox"><s:date
					name="documentTypeDetails.proceedingDate" var="proceedingDate"
					format="dd/MM/yyyy" /> <span class="bold"><s:property
						value="%{#proceedingDate}" default="N/A" /></span></td>
		</tr>
	</div>
	<tr>
		<td class="greybox">&nbsp;</td>
		<td class="greybox"><div class="courtName">
				<s:text name="lbl.dtd.courtname"></s:text>
				:
			</div></td>
		<td class="greybox"><div class="courtName">
				<span class="bold"><s:property
						value="%{documentTypeDetails.courtName}" default="N/A" /></span>
			</div></td>
		<td class="greybox" align="left">
			<div class="signed">
				<s:text name="lbl.dtd.signed"></s:text>
			</div>
		</td>
		<td class="greybox">
			<div class="signed">
				<span class="bold"><s:property
						value="%{documentTypeDetails.signed}" /></span>
			</div>
		</td>
	</tr>
	<tr>
		<td colspan="5">
			<table class="tablebottom" id="nameTable" width="100%" border="0"
				cellpadding="0" cellspacing="0">
				<tbody>
					<tr>
						<th class="bluebgheadtd"><s:text name="doctable.sno" /></th>
						<th class="bluebgheadtd"><s:text name="doctable.doctype" /></th>
						<th class="bluebgheadtd"><s:text name="file" /></th>
					</tr>
					<s:iterator value="assessmentDocumentTypes" status="status"
						var="documentType">
						<s:set value="name.replace(' ', '_')" var="docId" />
						<s:set value="%{name.replace(' ', '_')+'_Idx'}" var="docIdIndex" />
						<tr id="<s:property value="#docId" />">
							<td class="blueborderfortd" style="text-align: center"
								id="<s:property value="#docIdIndex" />"><span class="bold"><s:property
										value="#status.index + 1" /></span></td>
							<td class="blueborderfortd" style="text-align: left"><span
								class="bold"><s:property value="%{name}" /></span></td>
							<td class="blueborderfortd" style="text-align: center"><s:if
									test="%{assessmentDocuments.isEmpty() || assessmentDocuments[#status.index].files.isEmpty()}">
									<span class="bold">N/A</span>
								</s:if> <s:else>
									<s:iterator value="%{assessmentDocuments[#status.index].files}">
										<a
											href="javascript:viewDocument('<s:property value="fileStoreId"/>')">
											<s:property value="%{fileName}" />
										</a>
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
	documentTypeView();
});

function documentTypeView() {
	var dropdownvalue = '<s:property value="%{documentTypeDetails.documentName}"/>';
	serialNoToggle(dropdownvalue);
}

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
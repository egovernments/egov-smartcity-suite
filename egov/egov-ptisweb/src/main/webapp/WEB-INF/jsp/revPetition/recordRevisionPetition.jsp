<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
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

<%@ taglib prefix="s" uri="/WEB-INF/taglib/struts-tags.tld" %>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td colspan="5">
			<div class="headingsmallbg">
			    <s:if test="%{wfType.equals(@org.egov.ptis.constants.PropertyTaxConstants@WFLOW_ACTION_NAME_GRP)}">
			    <s:text name="recordGRP.title"></s:text>
			    </s:if>
			    <s:else>
				<s:text name="recordObjection.title"></s:text>
				</s:else>
			</div>
		</td>
	</tr>
	<tr>
		<td colspan="5">
			 <table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox" width="25%">
				<s:if test="%{wfType.equals(@org.egov.ptis.constants.PropertyTaxConstants@WFLOW_ACTION_NAME_GRP)}">
				<s:text name="objection.grp.received.date" />
				</s:if>
				<s:else>
				<s:text name="objection.received.date" />
				</s:else>
				</td>
			    <td class="bluebox" width="20%">
			    <s:date name="recievedOn" var="recievedOnId" format="dd/MM/yyyy" />
				<div align="left">
							<s:property default="N/A" value="%{recievedOnId}" />
						</div>
				</td>
			
				<td class="bluebox" width="30%">
								<s:if test="%{wfType.equals(@org.egov.ptis.constants.PropertyTaxConstants@WFLOW_ACTION_NAME_GRP)}">
				<s:text name="objection.grp.received.by"/>
				</s:if>
				<s:else>
				<s:text name="objection.received.by"/>
				</s:else>
				</td>
				<td class="bluebox" width="25%"><div align="left"><s:property default="N/A" value="%{recievedBy}" /></div></td>
			</tr>
			<tr>
				<td class="greybox">&nbsp;</td>
				<td class="greybox" width="25%">
				<s:if test="%{wfType.equals(@org.egov.ptis.constants.PropertyTaxConstants@WFLOW_ACTION_NAME_GRP)}">
				<s:text name="objection.grp.details"/>
				</s:if>
				<s:else>
				<s:text name="objection.details"/>
				</s:else>
				<span class="mandatory1">*</span></td>
				<td class="greybox" width="25%"><s:textarea name="details" id="details" cols="40" rows="2"  onblur="checkLength(this)" ></s:textarea></td>
			 
				<%-- <td class="greybox" width="25%"><s:text name="objection.upload.document"/></td>
				<td class="greybox" width="25%"><input type="button" class="button" value="Upload Document" id="docUploadButton" onclick="showDocumentManager();" /></td>
				<s:hidden name="docNumberObjection" id="docNumber" /> --%>
 
			</tr>
		</table>
		</td>
	</tr>
</table>
<s:if test="%{!documentTypes.isEmpty()}">
		<%@ include file="../common/DocumentUploadForm.jsp"%>
</s:if>
<script>
	function chkReceivedByLen(obj) {
		if (obj.value.length > 256) {
			bootbox
					.alert('Max 256 characters are allowed for received by text. Remaining characters are truncated.')
			obj.value = obj.value.substring(1, 256);
		}
	}
</script>

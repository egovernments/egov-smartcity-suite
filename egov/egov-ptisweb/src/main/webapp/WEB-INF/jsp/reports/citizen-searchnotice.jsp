<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2017>  eGovernments Foundation
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

<%@taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<html>
<head>
<title><s:text name="lbl.citizen.searchnotices" /></title>
<script type="text/javascript">
	jQuery.noConflict();
	function performBeforeSubmit(obj) {
		var indexNumber = jQuery("#indexNumber").val();
		var applicationNo = jQuery("#applicationNo").val();
		if (indexNumber == '' && applicationNo == '') {
			bootbox.alert("Any one criteria is mandatory");
			return false;
		} else {
			if (obj.value == 'Search') {
				document.forms[0].action = '/ptis/public/reports/searchnotice-result.action?propertyId='
						+ indexNumber +'applicationNo=' + applicationNo ;

				document.forms[0].submit();
			}
		}

	}

	function displayNotice(noticeNumber, isBlob) {
		var sUrl;
		if (isBlob == 'N') {
			sUrl = "/egi/docmgmt/ajaxFileDownload.action?moduleName=PT&docNumber="
					+ noticeNumber + "&fileName=" + noticeNumber + ".pdf";
		} else {
			sUrl = "/ptis/public/reports/searchNotices-showNotice.action?noticeNumber="
					+ noticeNumber;
		}
		window.open(sUrl, "_self");
	}
</script>
</head>
<body>
	<div align="left" class="errortext">
		<s:actionerror />
		<s:fielderror />
	</div>
	<s:form action="citizen-searchnotices" theme="simple" validate="true">
		<div class="formmainbox">
			<div class="headingbg">
				<s:text name="lbl.citizen.searchnotices" />
			</div>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">

				<tr>
					<td class="greybox">&nbsp;</td>
					<td class="greybox"><s:text name="citizen.prop.id" /><span
						class="mandatory1">*</span> :</td>
					<td class="greybox"><s:textfield name="indexNumber"
							id="indexNumber" onblur="trim(this,this.value);"
							value="%{indexNumber}" cssClass="form-control patternvalidation"
							data-pattern="number" maxlength="10" /></td>
					<td colspan="2" class="greybox">&nbsp;</td>
				</tr>
				<tr>
						<td colspan="4" align="center">OR</td>
					</tr>
				<tr>
					<td class="greybox">&nbsp;</td>
					<td class="greybox"><s:text name="" />Application No<span
						class="mandatory1">*</span> :</td>
					<td class="greybox"><s:textfield name="applicationNo"
							id="applicationNo" onblur="trim(this,this.value);"
							value="%{applicationNo}" maxlength="50" /></td>
					<td colspan="2" class="greybox">&nbsp;</td>
				</tr>
			</table>
			<br />
		</div>
		<div class="buttonbottom" align="center">
			<input type="button" name="button2" value="Search"
				class="buttonsubmit" onclick="performBeforeSubmit(this);"> <input
				type="button" name="button2" id="button2" value="Close"
				class="button" onclick="window.close();" />
		</div>
	</s:form>
	<s:if test="!citizenNotices.isEmpty()">
		<display:table name="citizenNotices" uid="currentRowObject"
			pagesize="20" class="tablebottom"
			style="width:100%;border-left: 1px solid #DFDFDF;" cellpadding="0"
			cellspacing="0" export="true" requestURI="">
			<display:caption>
				<div class="headingsmallbgnew" align="center"
					style="text-align: center; width: 98%;">
					<span class="searchvalue1">Search Criteria :</span>
					<s:property value="reportHeader" />
				</div>
			</display:caption>

			<display:column headerClass="bluebgheadtd" class="blueborderfortd"
				title="Notice Type" style="text-align:center;width:10%;"
				property="noticeType" />

			<display:column headerClass="bluebgheadtd" class="blueborderfortd"
				title="Notice Number" style="text-align:center;width:10%;">
				<a
					href="javascript:displayNotice('<s:property value="#attr.currentRowObject.notice"/>','<s:property value="#attr.currentRowObject.isBlob"/>')">
					<s:property value="#attr.currentRowObject.notice" />
				</a>
			</display:column>

			<display:column headerClass="bluebgheadtd" class="blueborderfortd"
				title="Notice Date" style="text-align:center;width:10%;">
				<s:date name="#attr.currentRowObject.noticeDate" var="noticeDt"
					format="dd/MM/yyyy" />
				<s:property value="noticeDt" />
			</display:column>

			<display:column headerClass="bluebgheadtd" class="blueborderfortd"
				title="Assessment Number" style="text-align:center;width:10%;">
				<s:property value="#attr.currentRowObject.assessmentNo" />
			</display:column>

			<display:column headerClass="bluebgheadtd" class="blueborderfortd"
				title="House Number" style="text-align:center;width:10%;">
				<s:property
					value="#attr.currentRowObject.houseNo" />
			</display:column>

			<display:column headerClass="bluebgheadtd" class="blueborderfortd"
				title="Name(s) of Owner" style="text-align:center;width:10%;">
				<s:property
					value="%{#attr.currentRowObject.ownerName}" />
			</display:column>

			<display:column headerClass="bluebgheadtd" class="blueborderfortd"
				title="Property Address" style="text-align:center;width:10%;">
				<s:property value="#attr.currentRowObject.address" />
			</display:column>

		</display:table>
	</s:if>
	<s:if test="citizenNotices.isEmpty()">
		<div class="headingsmallbgnew"
			style="text-align: center; width: 100%;">
			<s:text name="record.not.found" />
		</div>

	</s:if>
	<script
		src="<cdn:url value='/resources/global/js/egov/patternvalidation.js?rnd=${app_release_no}' context='/egi'/>"></script>
</body>
</html>

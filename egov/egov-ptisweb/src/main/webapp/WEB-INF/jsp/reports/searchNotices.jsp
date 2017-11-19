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

<%@taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<html>
<head>
<title><s:text name="SearchNotice.title" /></title>
<link
	href="<cdn:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>"
	rel="stylesheet" type="text/css" />
<script
	src="<cdn:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"></script>
<script
	src="<cdn:url value='/resources/global/js/bootstrap/typeahead.bundle.js' context='/egi'/>"></script>
<script type="text/javascript">
	jQuery.noConflict();
	jQuery(function($) {
		try {
			jQuery(".datepicker").datepicker({
				format : "dd/mm/yyyy",
				autoclose:true
			});
		} catch (e) {
			console.warn("No Date Picker " + e);
		}

	});

	function populateWard() {
		populatewardId({
			zoneId : document.getElementById("zoneId").value
		});
	}

	function performBeforeSubmit(obj) {
		if (document.getElementById("noticeFDate").value == 'DD/MM/YYYY') {
			document.getElementById("noticeFDate").value = "";
		}
		if (document.getElementById("noticeTDate").value == 'DD/MM/YYYY') {
			document.getElementById("noticeTDate").value = "";
		}
		if (obj.value == 'Search') {
			document.forms[0].action = 'searchNotices-search.action';
		} else if (obj.value == 'Merge & Download') {
			document.forms[0].action = 'searchNotices-mergeAndDownload.action';

			setTimeout(function() {
				jQuery('.loader-class').modal('hide');
			}, 10);

		} else if (obj.value == 'Zip & Download') {
			document.forms[0].action = 'searchNotices-zipAndDownload.action';

			setTimeout(function() {
				jQuery('.loader-class').modal('hide');
			}, 10);

		} else if (obj.value == 'Reset') {
			document.forms[0].action = 'searchNotices-reset.action';
		}

		document.forms[0].submit();
	}

	function displayNotice(noticeNumber, isBlob) {
		var sUrl;
		if (isBlob == 'N') {
			sUrl = "/egi/docmgmt/ajaxFileDownload.action?moduleName=PT&docNumber="
					+ noticeNumber + "&fileName=" + noticeNumber + ".pdf";
		} else {
			sUrl = "/ptis/reports/searchNotices-showNotice.action?noticeNumber="
					+ noticeNumber;
		}
		window.open(sUrl, "window",
				'scrollbars=yes,resizable=no,height=200,width=400,status=yes');
	}
</script>
</head>
<body>
	<div align="left" class="errortext">
		<s:actionerror />
		<s:fielderror />
	</div>
	<s:form action="searchNotices" theme="simple" validate="true">
		<div class="formmainbox">
			<div class="headingbg">
				<s:text name="SearchNoticeHeader" />
			</div>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="15%" class="greybox">&nbsp;</td>
					<td width="30%" class="greybox"><s:text name="OwnerName" /> :</td>
					<td class="greybox"><s:textfield name="ownerName"
							maxlength="512"
							onblur="trim(this,this.value);checkNotSpecialCharForName(this);" /></td>
					<td colspan="2" class="greybox">&nbsp;</td>
				</tr>
				<tr>
					<td class="greybox">&nbsp;</td>
					<td class="greybox"><s:text name="Zone" /> :</td>
					<td class="greybox"><s:select name="zoneId" id="zoneId"
							list="dropdownData.Zone" listKey="id" listValue="name"
							headerKey="-1" headerValue="%{getText('default.select')}"
							value="%{zoneId}" /></td>
					<td colspan="2" class="greybox">&nbsp;</td>
				</tr>
				<tr>
					<td class="greybox">&nbsp;</td>
					<td class="greybox"><s:text name="Ward" /> :</td>
					<td class="greybox"><s:select name="wardId" id="wardId"
							list="dropdownData.wardList" listKey="id" listValue="name"
							headerKey="-1" headerValue="%{getText('default.select')}"
							value="%{wardId}" /></td>
					<td colspan="2" class="greybox">&nbsp;</td>
				</tr>
				<tr>
					<td class="greybox">&nbsp;</td>
					<td class="greybox"><s:text name="Ownership Type" /> :</td>
					<td class="greybox"><s:select name="propertyType"
							id="propTypeMaster" list="dropdownData.PropTypeMaster"
							listKey="id" listValue="type" headerKey="-1"
							headerValue="%{getText('default.select')}"
							value="%{propertyType}" /></td>
					<td colspan="2" class="greybox">&nbsp;</td>
				</tr>
				<tr>
					<td class="greybox">&nbsp;</td>
					<td class="greybox"><s:text name="NoticeType" /><span
						class="mandatory1">*</span> :</td>
					<td class="greybox"><s:select name="noticeType"
							id="noticeType" list="noticeTypeMap" listKey="key"
							listValue="value" headerKey="-1"
							headerValue="%{getText('default.select')}" /></td>
					<td colspan="2" class="greybox">&nbsp;</td>
				</tr>
				<tr>
					<td class="greybox">&nbsp;</td>
					<td class="greybox"><s:text name="noticeNum" /> :</td>
					<td class="greybox"><s:textfield name="noticeNumber"
							onblur="trim(this,this.value);" /></td>
					<td colspan="2" class="greybox">&nbsp;</td>
				</tr>
				<tr>
					<td class="greybox">&nbsp;</td>
					<td class="greybox"><s:text name="noticeDateFrom" /> :</td>
					<td class="greybox"><s:date name="noticeFromDate"
							var="noticeFDate" format="dd/MM/yyyy" /> <s:textfield
							name="noticeFromDate" cssClass="datepicker"
							value="%{noticeFDate}" autocomplete="off" id="noticeFDate"
							size="12" maxlength="12"></s:textfield></td>
					<td colspan="2" class="greybox">&nbsp;</td>
				</tr>
				<tr>
					<td class="greybox">&nbsp;</td>
					<td class="greybox"><s:text name="noticeDateTo" /> :</td>
					<td class="greybox"><s:date name="noticeToDate"
							var="noticeTDate" format="dd/MM/yyyy" /> <s:textfield
							name="noticeToDate" cssClass="datepicker" value="%{noticeTDate}"
							autocomplete="off" id="noticeTDate" size="12" maxlength="12"></s:textfield>
					</td>
					<td colspan="2" class="greybox">&nbsp;</td>
				</tr>
				<tr>
					<td class="greybox">&nbsp;</td>
					<td class="greybox"><s:text name="prop.Id" /> :</td>
					<td class="greybox"><s:textfield name="indexNumber"
							onblur="trim(this,this.value);" value="%{indexNumber}"
							maxlength="30" /></td>
					<td colspan="2" class="greybox">&nbsp;</td>
				</tr>
				<tr>
					<td class="greybox">&nbsp;</td>
					<td class="greybox"><s:text name="HouseNo" /> :</td>
					<td class="greybox"><s:textfield name="houseNumber"
							onblur="trim(this,this.value);" value="%{houseNumber}" /></td>
					<td colspan="2" class="greybox">&nbsp;</td>
				</tr>
			</table>
			<br/>
		</div>
		<div class="buttonbottom" align="center">
			<s:submit name="button32" type="submit" cssClass="buttonsubmit"
				id="button32" value="Search" method="search"
				onclick="performBeforeSubmit(this);" />
			<s:submit name="button32" type="submit" cssClass="buttonsubmit"
				id="button32" value="Merge & Download" method="mergeAndDownload"
				onclick="performBeforeSubmit(this);" />
			<s:submit name="button32" type="submit" cssClass="buttonsubmit"
				id="button32" value="Zip & Download" method="zipAndDownload"
				onclick="performBeforeSubmit(this);" />
			<s:submit type="submit" cssClass="button" value="Reset"
				method="reset" onclick="performBeforeSubmit(this);" />
			<input type="button" name="button2" id="button2" value="Close"
				class="button" onclick="window.close();" />
		</div>
	</s:form>
	<s:if test="!noticeList.isEmpty()">
		<display:table name="searchResult" uid="currentRowObject"
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
					href="javascript:displayNotice('<s:property value="#attr.currentRowObject.noticeNo"/>','<s:property value="#attr.currentRowObject.isBlob"/>')">
					<s:property value="#attr.currentRowObject.noticeNo" />
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
				<s:property value="#attr.currentRowObject.basicProperty.upicNo" />
			</display:column>

			<display:column headerClass="bluebgheadtd" class="blueborderfortd"
				title="House Number" style="text-align:center;width:10%;">
				<s:property
					value="#attr.currentRowObject.basicProperty.address.houseNoBldgApt" />
			</display:column>

			<display:column headerClass="bluebgheadtd" class="blueborderfortd"
				title="Name(s) of Owner" style="text-align:center;width:10%;">
				<s:property
					value="%{getNonHistoryOwnerName(#attr.currentRowObject.basicProperty)}" />
			</display:column>

			<display:column headerClass="bluebgheadtd" class="blueborderfortd"
				title="Property Address" style="text-align:center;width:10%;">
				<s:property value="#attr.currentRowObject.basicProperty.address" />
			</display:column>

			<display:setProperty name="export.csv" value="false" />
			<display:setProperty name="export.excel" value="true" />
			<display:setProperty name="export.excel.filename"
				value="propertyTax-noticeReports.xls" />
			<display:setProperty name="export.xml" value="false" />
			<display:setProperty name="export.pdf" value="true" />
			<display:setProperty name="export.pdf.filename"
				value="propertyTax-noticeReports.pdf" />
		</display:table>
	</s:if>
	<s:if test="noticeList.isEmpty()">
		<s:if test="target=='searchresult'">
			<div class="headingsmallbgnew"
				style="text-align: center; width: 100%;">
				<s:text name="searchresult.norecord" />
			</div>
		</s:if>
	</s:if>
</body>
</html>

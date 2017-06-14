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

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="/includes/taglibs.jsp"%>
<html>
<head>
<script type="text/javascript">
	function onSubmit(obj, formId) {
		var formObj = document.getElementById(formId);
		formObj.action = obj;
		formObj.submit;
		return true;
	}
	function collectTax() {
		var propertyId = jQuery("#assessmentNum").val();
		if (propertyId == '') {
			bootbox.alert("Please enter assessment number");
			return false;
		} else {
			//window.location = '/../ptis/collection/collectPropertyTax-generateBill.action?propertyId='+propertyId;
			return true;
		}
	}
	jQuery(document)
			.ready(
					function() {
						jQuery("#searchMobileno").click(function(e) {
							if (jQuery("#mobileNumber").val() == '') {
								bootbox.alert("Please enter mobile number");
								return false;
							}
						});

						jQuery("#searchDoorno").click(function(e) {
							if (jQuery("#doorNo").val() == '') {
								bootbox.alert("Please enter door number");
								return false;
							}
						});
						jQuery("#searchByassmentno")
								.click(
										function(e) {
											if (jQuery("#assessmentNum").val() == '') {
												bootbox
														.alert("Please enter assessment number");
												return false;
											}
										});
						jQuery("#searchByBndry")
								.click(
										function(e) {
											if (jQuery("#zoneId").val() == -1
													&& jQuery("#wardId").val() == -1) {
												bootbox
														.alert("Please select either zone or ward");
												return false;
											}
										});
						jQuery("#searchByowner")
								.click(
										function(e) {
											if (jQuery("#locationId").val() == -1
													&& jQuery("#ownerName")
															.val() == '') {
												bootbox
														.alert("Please select location and enter owner name");
												return false;
											} else if (jQuery("#locationId")
													.val() == -1) {
												bootbox
														.alert("Please select location");
												return false;
											} else if (jQuery("#ownerName")
													.val() == '') {
												bootbox
														.alert("Please enter owner name");
												return false;
											}
										});

						jQuery("#searchByDemand")
								.click(
										function(e) {
											if (jQuery("#fromDemand").val() == ''
													|| jQuery("#toDemand")
															.val() == '') {
												bootbox
														.alert("Please enter from and to demand data");
												return false;
											}
										});

						jQuery("#searchOldMuncipalNo")
								.click(
										function(e) {
											if (jQuery("#oldMuncipalNum").val() == '') {
												bootbox
														.alert("Please enter old muncipal number");
												return false;
											}
										});

					});
</script>
<title><s:text name="searchProp.title"></s:text></title>
</head>
<body>
	<div class="formmainbox">
		<s:if test="%{hasErrors()}">
			<div class="errorstyle" id="property_error_area">
				<div class="errortext">
					<s:actionerror />
					<s:fielderror />
				</div>
			</div>
		</s:if>
		<s:if test="%{hasActionMessages()}">
			<div id="actionMessages" class="messagestyle">
				<s:actionmessage theme="simple" />
			</div>
		</s:if>
		<table border="0" cellspacing="0" cellpadding="0" width="100%">
			<s:form action="searchProperty" name="assessmentform" theme="simple"
				id="assessmentform">
				<tr>
					<td width="100%" colspan="4" class="headingbg">
						<div class="headingbg">
							<s:text name="search.assessment.num" />
						</div>
					</td>
				</tr>
				<tr>
					<td colspan="4"><br /></td>
				</tr>
				<tr>
					<td class="bluebox">&nbsp;</td>
					<td class="bluebox"><s:text name="prop.Id" /> <span
						class="mandatory1">*</span> :</td>

					<td class="bluebox"><s:textfield name="assessmentNum"
							id="assessmentNum" value="%{assessmentNum}" maxlength="15" /></td>
					<td class="bluebox">&nbsp;</td>
				</tr>

				<tr>
					<s:if
						test="%{roleName.contains(@org.egov.ptis.constants.PropertyTaxConstants@ROLE_COLLECTION_OPERATOR.toUpperCase())}">
						<td class="greybox" colspan="2">
							<div class="greybox" style="text-align: right">
								<s:hidden id="mode" name="mode" value="assessment"></s:hidden>
								<s:submit name="search" value="Search" id="searchByassmentno"
									cssClass="buttonsubmit"
									onclick="return onSubmit('searchProperty-srchByAssessment.action', 'assessmentform');"></s:submit>
							</div>
						</td>
						<td class="greybox" colspan="2">
							<div class="greybox" style="text-align: left">
								<s:submit name="CollectTax" value="CollectTax" id="CollectTax"
									cssClass="buttonsubmit"
									onclick="return onSubmit('searchProperty-searchOwnerDetails.action', 'assessmentform');"></s:submit>
							</div>
						</td>
					</s:if>
					<s:else>
						<td class="greybox">&nbsp;</td>
						<td class="greybox" colspan="2"><br />
							<div class="greybox" style="text-align: center">
								<s:hidden id="mode" name="mode" value="assessment"></s:hidden>
								<s:submit name="search" id="searchByassmentno" value="Search"
									cssClass="buttonsubmit"
									onclick="return onSubmit('searchProperty-srchByAssessment.action', 'assessmentform');"></s:submit>
							</div></td>
						<td class="greybox">&nbsp;</td>
					</s:else>
				</tr>
			</s:form>
		</table>

		<table border="0" cellspacing="0" cellpadding="0" width="100%">
			<s:form action="searchProperty" name="oldassessmentform"
				theme="simple" id="oldassessmentform">
				<tr>
					<td width="100%" colspan="4" class="headingbg">
						<div class="headingbg">
							<s:text name="search.oldassessment.num" />
						</div>
					</td>
				</tr>
				<tr>
					<td colspan="4"><br /></td>
				</tr>
				<tr>
					<td class="bluebox">&nbsp;</td>
					<td class="bluebox"><s:text name="Old.assessmentno" /> <span
						class="mandatory1">*</span> :</td>

					<td class="bluebox"><s:textfield name="oldMuncipalNum"
							id="oldMuncipalNum" value="%{oldMuncipalNum}" maxlength="20" /></td>
					<td class="bluebox">&nbsp;</td>
				</tr>

				<tr>
					<s:if
						test="%{roleName.contains(@org.egov.ptis.constants.PropertyTaxConstants@ROLE_COLLECTION_OPERATOR.toUpperCase())}">
						<td class="greybox" colspan="2">
							<div class="greybox" style="text-align: right">
								<s:hidden id="mode" name="mode" value="oldMuncipalNum"></s:hidden>
								<%-- <s:hidden id="mode" name="mode" value="assessment"></s:hidden> --%>
								<s:submit name="search" value="Search" id="searchOldMuncipalNo"
									cssClass="buttonsubmit"
									onclick="return onSubmit('searchProperty-srchByOldMuncipalNumber.action', 'oldassessmentform');"></s:submit>
							</div>
						</td>
						<td class="greybox" colspan="2">
							<div class="greybox" style="text-align: left">
								<s:submit name="CollectTax" value="CollectTax" id="CollectTax"
									cssClass="buttonsubmit"
									onclick="return onSubmit('searchProperty-searchOwnerDetails.action', 'oldassessmentform');"></s:submit>
							</div>
						</td>
					</s:if>
					<s:else>
						<td class="greybox">&nbsp;</td>
						<td class="greybox" colspan="2"><br />
							<div class="greybox" style="text-align: center">
								<s:hidden id="mode" name="mode" value="oldMuncipalNum"></s:hidden>
								<s:submit name="search" id="searchOldMuncipalNo" value="Search"
									cssClass="buttonsubmit"
									onclick="return onSubmit('searchProperty-srchByOldMuncipalNumber.action', 'oldassessmentform');"></s:submit>
							</div></td>
						<td class="greybox">&nbsp;</td>
					</s:else>
				</tr>
			</s:form>
		</table>







		<table border="0" cellspacing="0" cellpadding="0" width="100%">
			<s:form action="searchProperty" name="mobileNoform" theme="simple"
				id="mobileNoform">
				<tr>
					<td width="100%" colspan="4" class="headingbg">
						<div class="headingbg">
							<s:text name="search.mobile" />
						</div>
					</td>
				</tr>
				<tr>
					<td colspan="4"><br /></td>
				</tr>
				<tr>
					<td class="bluebox">&nbsp;</td>
					<td class="bluebox"><s:text name="MobileNumber" /> <span
						class="mandatory1">*</span> :</td>

					<td class="bluebox"><s:textfield name="mobileNumber"
							id="mobileNumber" value="%{mobileNumber}" maxlength="15" /></td>
					<td class="bluebox">&nbsp;</td>
				</tr>

				<tr>
					<td class="greybox">&nbsp;</td>
					<td class="greybox" colspan="2"><br />
						<div class="greybox" style="text-align: center">
							<s:hidden id="mode" name="mode" value="mobileNo"></s:hidden>
							<s:submit name="search" value="Search" id="searchMobileno"
								cssClass="buttonsubmit"
								onclick="return onSubmit('searchProperty-srchByMobileNumber.action', 'mobileNoform');"></s:submit>
						</div></td>
				</tr>
			</s:form>
		</table>
		<table border="0" cellspacing="0" cellpadding="0" width="100%">
			<s:form action="searchProperty" name="doorNoform" theme="simple"
				id="doorNoform">
				<tr>
					<td width="100%" colspan="4" class="headingbg">
						<div class="headingbg">
							<s:text name="search.door" />
						</div>
					</td>
				</tr>
				<tr>
					<td colspan="4"><br /></td>
				</tr>
				<tr>
					<td class="bluebox">&nbsp;</td>
					<td class="bluebox"><s:text name="doorNo" /> <span
						class="mandatory1">*</span> :</td>

					<td class="bluebox"><s:textfield name="doorNo" id="doorNo"
							value="%{doorNo}" maxlength="15" /></td>
					<td class="bluebox">&nbsp;</td>
				</tr>

				<tr>
					<td class="greybox">&nbsp;</td>
					<td class="greybox" colspan="2"><br />
						<div class="greybox" style="text-align: center">
							<s:hidden id="mode" name="mode" value="doorNo"></s:hidden>
							<s:submit name="search" id="searchDoorno" value="Search"
								cssClass="buttonsubmit"
								onclick="return onSubmit('searchProperty-srchByDoorNo.action', 'doorNoform');"></s:submit>
						</div></td>
				</tr>
			</s:form>
		</table>


		<table border="0" cellspacing="0" cellpadding="0" width="100%">
			<s:form name="zoneform" theme="simple" id="zoneform">
				<tr>
					<td width="100%" colspan="4" class="headingbg">
						<div class="headingbg">
							<s:text name="search.zone.ward" />
						</div>
					</td>
				</tr>
				<tr>
					<td colspan="4"><br /></td>
				</tr>
				<tr>
					<td class="bluebox">&nbsp;</td>
					<td class="bluebox"><s:text name="Zone" /> <span
						class="mandatory1">*</span> :</td>
					<td class="bluebox"><s:select name="zoneId" id="zoneId"
							list="zoneBndryMap" listKey="key" listValue="value"
							headerKey="-1" headerValue="%{getText('default.select')}"
							value="%{zoneId}" /></td>
					<td class="bluebox">&nbsp;</td>
				</tr>
				<tr>
					<td class="greybox">&nbsp;</td>
					<td class="greybox"><s:text name="Ward" /> <span
						class="mandatory1">*</span> :</td>
					<td class="greybox"><s:select name="wardId" id="wardId"
							list="WardndryMap" listKey="key" listValue="value" headerKey="-1"
							headerValue="%{getText('default.select')}" value="%{wardId}" /></td>
					<td class="greybox">&nbsp;</td>
				</tr>
				<tr>
					<td class="bluebox">&nbsp;</td>
					<td class="bluebox"><s:text name="HouseNo" /> :</td>
					<td class="bluebox"><s:textfield name="houseNumBndry" /></td>
					<td class="bluebox">&nbsp;</td>
				</tr>
				<tr>
					<td class="greybox">&nbsp;</td>
					<td class="greybox"><s:text name="OwnerName" /> :</td>
					<td class="greybox"><s:textfield name="ownerNameBndry" /></td>
					<td class="greybox">&nbsp;</td>
				</tr>

				<tr>
					<td class="greybox">&nbsp;</td>
					<td class="greybox" colspan="2"><br />
						<div class="greybox" style="text-align: center">
							<s:hidden id="mode" name="mode" value="bndry"></s:hidden>
							<s:submit name="search" value="Search" id="searchByBndry"
								cssClass="buttonsubmit"
								onclick="return onSubmit('searchProperty-srchByBndry.action', 'zoneform');"></s:submit>
						</div></td>
					<td class="greybox">&nbsp;</td>
				</tr>
				<br />
			</s:form>
		</table>

		<table border="0" cellspacing="0" cellpadding="0" width="100%">
			<s:form name="locationform" theme="simple" id="locationform">
				<tr>
					<td width="100%" colspan="4" class="headingbg">
						<div class="headingbg">
							<s:text name="search.ownerName" />
						</div>
					</td>
				</tr>
				<tr>
					<td colspan="4"><br /></td>
				</tr>
				<tr>
					<td class="bluebox">&nbsp;</td>
					<td class="bluebox"><s:text name="Location" /><span
						class="mandatory1">*</span> :</td>
					<td class="bluebox"><s:select name="locationId"
							id="locationId" list="dropdownData.Location"
							cssStyle="width: 150px;" listKey="id" listValue="name"
							headerKey="-1" headerValue="----Choose----" value="%{locationId}" />
					</td>
					<td class="bluebox">&nbsp;</td>

				</tr>

				<tr>
					<td class="greybox">&nbsp;</td>
					<td class="greybox"><s:text name="OwnerName" /> <span
						class="mandatory1">*</span> :</td>
					<td class="greybox"><s:textfield name="ownerName"
							id="ownerName" /></td>
					<td class="greybox">&nbsp;</td>
				</tr>

				<tr>
					<td class="bluebox">&nbsp;</td>
					<td class="bluebox"><s:text name="HouseNo" /> :</td>
					<td class="bluebox"><s:textfield name="houseNumArea" /></td>
					<td class="bluebox">&nbsp;</td>
				</tr>

				<tr>
					<td class="greybox">&nbsp;</td>
					<td class="greybox" colspan="2"><br />
						<div class="greybox" style="text-align: center">
							<s:hidden id="mode" name="mode" value="location"></s:hidden>
							<s:submit name="search" value="Search" id="searchByowner"
								cssClass="buttonsubmit"
								onclick="return onSubmit('searchProperty-srchByLocation.action', 'locationform');"></s:submit>
						</div></td>
					<td class="greybox">&nbsp;</td>
				</tr>
				<br />
			</s:form>
		</table>
		<!-- objection search details -->
		<table border="0" cellspacing="0" cellpadding="0" width="100%">
			<s:form name="demandForm" theme="simple" id="demandForm">
				<tr>
					<td width="100%" colspan="4" class="headingbg">
						<div class="headingbg">
							<s:text name="search.objection" />
						</div>
					</td>
				</tr>

				<tr>
					<td class="bluebox" style="text-align: center;" colspan="4"><br />
						From <span class="mandatory1">*</span>: &nbsp;&nbsp;&nbsp; <s:textfield
							name="fromDemand" id="fromDemand"
							onblur="validNumber(this);checkZero(this,'From Demand');" />
						&nbsp;&nbsp;&nbsp; To <span class="mandatory1">*</span>:
						&nbsp;&nbsp;&nbsp; <s:textfield name="toDemand" id="toDemand"
							onblur="validNumber(this);checkZero(this,'To Demand');" /></td>
				</tr>
				<tr>
					<td class="bluebox" colspan="4">&nbsp; &nbsp; &nbsp;</td>
				</tr>
				<tr>
					<td class="bluebox">&nbsp;</td>
					<td class="bluebox" colspan="2">
						<div class="bluebox" style="text-align: center">
							<s:hidden id="mode" name="mode" value="demand"></s:hidden>
							<s:submit name="search" value="Search" id="searchByDemand"
								cssClass="buttonsubmit"
								onclick="return onSubmit('searchProperty-searchByDemand.action', 'demandForm');"></s:submit>
						</div>
					</td>
					<td class="bluebox">&nbsp;</td>
				</tr>
				<br />
			</s:form>
		</table>

		<div align="left" class="mandatory1" style="font-size: 11px">
			&nbsp;&nbsp;
			<s:text name="mandtryFlds"></s:text>
		</div>

	</div>
</body>
</html>

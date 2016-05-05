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

<%@ include file="/includes/taglibs.jsp"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<html>
<head>
<title><s:text name="viewPropDet.title" /></title>
<script type="text/javascript">
	function loadOnStartup() {
		var propType = '<s:property value="%{basicProperty.property.propertyDetail.propertyTypeMaster.type}"/>';
		var appurtenantLandChecked = '<s:property value="%{basicProperty.property.propertyDetail.appurtenantLandChecked}"/>';
		enableFieldsForPropTypeView(propType, appurtenantLandChecked);
		var btnCheckbox = document.getElementById('taxEnsureCheckbox');
		var btnPayTax = document.getElementById('PayTax');
		var buttorOperatorPayTax = document.getElementById('operatorPayBill');

		if (btnPayTax != null) {
			btnPayTax.disabled = (btnCheckbox.checked) ? false : true;
		}

		if (buttorOperatorPayTax != null) {
			buttorOperatorPayTax.disabled = (btnCheckbox.checked) ? false
					: true;
		}
	}

	function switchPayTaxButton(ensureCheckbox) {
		var buttonPayTax = document.getElementById('PayTax');

		if (buttonPayTax == null) {
			document.getElementById('operatorPayBill').disabled = (ensureCheckbox.checked) ? false
					: true;
		} else {
			buttonPayTax.disabled = (ensureCheckbox.checked) ? false : true;
		}

	}

	jQuery(document)
			.ready(
					function() {
						jQuery('#payBill')
								.click(
										function() {
											var propertyId = '<s:property value="%{basicProperty.upicNo}"/>';
											window.location = '/../ptis/search/searchProperty-searchOwnerDetails.action?assessmentNum='
													+ propertyId;
										});
					});
</script>
</head>
<body onload="loadOnStartup(); ">
	<s:form action="searchProperty" method="post" name="indexform"
		theme="simple">
		<s:push value="model">
			<div class="">
				<s:if test="%{errorMessage != null && errorMessage != ''}">
					<s:property value="%{errorMessage}" />
					<div class="buttonbottom" align="center">
						<input type="button" class="buttonsubmit" name="SearchProperty"
							id="SearchProperty" value="Search Property"
							onclick="window.location='../search/searchProperty-searchForm.action';" />
						<input type="button" name="btnPrint" id="btnPrint" value="Print"
							class="buttonsubmit" onclick="window.print();" /> <input
							type="button" name="button2" id="button2" value="Close"
							class="button" onclick="window.close();" />
					</div>
				</s:if>
				<s:else>
					<jsp:include page="viewProperty.jsp" />
					<br />
					<div class="buttonbottom no-print" align="center">
						<!--From application index search same view page is given, if new property is under work flow and assessment no is not generated then all links are disabled  -->
						<s:if test="%{basicProperty.upicNo!=null}">
							<s:if
								test="%{!property.getIsExemptedFromTax() && (isCitizen || roleName.contains(@org.egov.ptis.constants.PropertyTaxConstants@CSC_OPERATOR_ROLE.toUpperCase()) ||
					roleName.contains(@org.egov.ptis.constants.PropertyTaxConstants@ROLE_COLLECTION_OPERATOR.toUpperCase()))}">
								<div align="center">
									<s:checkbox name="taxEnsureCheckbox" id="taxEnsureCheckbox"
										onclick="switchPayTaxButton(this);" required="true" />
									<span style="font-size: 15px; color: red"> <s:text
											name="msg.payBill.verification" /> <br>
									<br> <s:text name="msg.activeDemand" />
									</span>
								</div>
								<br>
								<div align="center">
									<s:if test="%{isCitizen}">
										<input type="button" name="PayTax" id="PayTax" value="Pay Tax"
											class="buttonsubmit"
											onclick="window.location='../citizen/collection/collection-searchOwnerDetails.action?assessmentNumber=<s:property value="%{propertyId}" />';" />

									</s:if>
									<s:else>
										<input type="button" name="operatorPayBill"
											id="operatorPayBill" value="Pay Bill" class="buttonsubmit"
											onclick="window.location='/../ptis/search/searchProperty-searchOwnerDetails.action?assessmentNum=<s:property value="%{propertyId}" />';" />

									</s:else>
								</div>
							</s:if>

							<br>
							<%-- <s:if test="%{property.getIsExemptedFromTax() && isDemandActive && !@org.egov.ptis.constants.PropertyTaxConstants@OWNERSHIP_TYPE_EWSHS.equals(propertyDetail.propertyTypeMaster.code)}">
					<input type="button" class="buttonsubmit" name="taxExemption" id="taxExemption" value="Tax Exemption"
							onclick="window.location='/ptis/exemption/form/<s:property value="%{basicProperty.upicNo}" />';" />
				</s:if> --%>
							<s:if
								test="%{(roleName.contains(@org.egov.ptis.constants.PropertyTaxConstants@ROLE_ULB_OPERATOR.toUpperCase()) || roleName.contains(@org.egov.ptis.constants.PropertyTaxConstants@CSC_OPERATOR_ROLE.toUpperCase()))
				&& !@org.egov.ptis.constants.PropertyTaxConstants@OWNERSHIP_TYPE_EWSHS.equals(propertyDetail.propertyTypeMaster.code)}">
								<s:if test="%{isDemandActive}">
									<%-- <input type="button" class="buttonsubmit" name="btnModifyProperty" id="btnModifyProperty" value="Addition/Alteration of Assessment"
							onclick="window.location='../modify/modifyProperty-modifyForm.action?modifyRsn=ADD_OR_ALTER&indexNumber=<s:property value="%{basicProperty.upicNo}"/>';" />
						<input type="button" class="buttonsubmit" name="btnModifyProperty" id="btnModifyProperty" value="Bifurcation"
							onclick="window.location='../modify/modifyProperty-modifyForm.action?modifyRsn=BIFURCATE&indexNumber=<s:property value="%{basicProperty.upicNo}"/>';" /> --%>
									<input type="button" class="buttonsubmit"
										name="btnTrnsProperty" id="btnTrnsProperty"
										value="Transfer Ownership"
										onclick="window.location='../property/transfer/new.action?assessmentNo=<s:property value="%{basicProperty.upicNo}" />';" />
									<%-- <input type="button" class="buttonsubmit" name="taxExemption" id="taxExemption" value="Tax Exemption"
							onclick="window.location='/ptis/exemption/form/<s:property value="%{basicProperty.upicNo}" />';" />
						<input type="button" class="buttonsubmit" name="btnGRPProperty" id="btnGRPProperty" value="General Revision Petition"
							onclick="window.location='../modify/modifyProperty-modifyForm.action?modifyRsn=GRP&indexNumber=<s:property value="%{basicProperty.upicNo}"/>';" />
						<s:if test="%{!property.getIsExemptedFromTax()}">
							<input type="button" class="buttonsubmit" name="writeOff" id="writeOff" value="Write Off"
								onclick="window.location='/ptis/writeOff/form/<s:property value="%{basicProperty.upicNo}" />';" /> --%>
								</s:if>
								<%-- <s:if test="%{!property.getIsExemptedFromTax()
							 && !@org.egov.ptis.constants.PropertyTaxConstants@OWNERSHIP_TYPE_VAC_LAND.equals(propertyDetail.propertyTypeMaster.code) && !basicProperty.underWorkflow
							 && !@org.egov.ptis.constants.PropertyTaxConstants@OWNERSHIP_TYPE_EWSHS.equals(propertyDetail.propertyTypeMaster.code)}">
							<s:if test="%{viewMap.enableVacancyRemission}" >		
								<input type="button" class="buttonsubmit" name="vacancyremissionbtn"
									id="vacancyremissionbtn" value="Vacancy Remission" 
									onclick="window.location='/ptis/vacancyremission/create/<s:property value="%{basicProperty.upicNo}" />,normalSearch'" />
							</s:if>
						</s:if> --%>
								<%-- <s:if test="%{!@org.egov.ptis.constants.PropertyTaxConstants@OWNERSHIP_TYPE_VAC_LAND.equals(propertyDetail.propertyTypeMaster.code)}">
					    <input type="button" class="buttonsubmit" name="btndemolitionProperty" id="btndemolitionProperty" value="Demolition"
							onclick="window.location='/ptis/property/demolition/<s:property value="%{basicProperty.upicNo}" />';" />
						</s:if>
					</s:if>
					<s:else>
						<input type="button" class="buttonsubmit" name="objection" id="objection" value="Create Revision Petition"
							onclick="window.location='../revPetition/revPetition-newForm.action?propertyId=<s:property value="%{basicProperty.upicNo}" />';" />
					</s:else> --%>
							</s:if>
							<%-- 	<s:if test="%{roleName.contains(@org.egov.ptis.constants.PropertyTaxConstants@PTVERIFIER_ROLE.toUpperCase())}">
					<s:if test="%{isDemandActive && !property.getIsExemptedFromTax() && !basicProperty.underWorkflow
							 && !@org.egov.ptis.constants.PropertyTaxConstants@OWNERSHIP_TYPE_VAC_LAND.equals(propertyDetail.propertyTypeMaster.code)
							 && !@org.egov.ptis.constants.PropertyTaxConstants@OWNERSHIP_TYPE_EWSHS.equals(propertyDetail.propertyTypeMaster.code)}">
							<input type="button" class="buttonsubmit" name="monthlyupdatebtn"
									id="monthlyupdatebtn" value="Vacancy Remission Monthly Update" 
									onclick="window.location='/ptis/vacancyremission/monthlyupdate/<s:property value="%{basicProperty.upicNo}" />'" />
					</s:if>
				</s:if> --%>
							<%-- <s:if test="%{roleName.contains(@org.egov.ptis.constants.PropertyTaxConstants@ROLE_ULB_OPERATOR.toUpperCase()) && !property.getIsExemptedFromTax()}">
					<input type="button" name="generateBill" id="generateBill" value="Generate Demand Bill" class="buttonsubmit"
						onclick="window.location='../bills/billGeneration-generateDemandBill.action?indexNumber=<s:property value="%{basicProperty.upicNo}" />';" />
				</s:if> --%>
							<%-- <s:if test="%{roleName.contains(@org.egov.ptis.constants.PropertyTaxConstants@ROLE_ULB_OPERATOR.toUpperCase()) && isNagarPanchayat 
					&& !basicProperty.underWorkflow && basicProperty.source=='M' && !@org.egov.ptis.constants.PropertyTaxConstants@OWNERSHIP_TYPE_EWSHS.equals(propertyDetail.propertyTypeMaster.code)}">
					<input type="button" class="buttonsubmit" name="addArrearsButton"
									id="addArrearsButton" value="Add Arrears" 
									onclick="window.location='/ptis/addarrears/form/<s:property value="%{basicProperty.upicNo}" />'" />
				</s:if> --%>
							<%-- <s:if test="%{roleName.contains(@org.egov.ptis.constants.PropertyTaxConstants@PTAPPROVER_ROLE.toUpperCase())}">
				<input type="button" class="buttonsubmit" name="editOwner"
									id="editOwner" value="Edit Owner" 
									onclick="window.location='/ptis/editowner/<s:property value="%{basicProperty.upicNo}" />'" />
				</s:if>	 --%>
							<br />
							<br />
							<!-- common buttons starts here -->

							<s:if test="%{!property.getIsExemptedFromTax()}">
								<input type="button" class="buttonsubmit" name="btnViewDCB"
									id="btnViewDCB" value="View DCB"
									onclick="window.location='../view/viewDCBProperty-displayPropInfo.action?propertyId=<s:property value="%{basicProperty.upicNo}" />';" />
							</s:if>

							<s:if test="%{!isCitizen}">
								<input type="button" class="buttonsubmit" name="SearchProperty"
									id="SearchProperty" value="Search Property"
									onclick="window.location='../search/searchProperty-searchForm.action';" />
							</s:if>
							<s:else>
								<input type="button" class="buttonsubmit" name="SearchProperty"
									id="SearchPropertyByCitizen" value="Search Property"
									onclick="window.location='../citizen/search/search-searchForm.action';" />
							</s:else>
						</s:if>
						<input type="button" name="btnPrint" id="btnPrint" value="Print"
							class="buttonsubmit" onclick="window.print();" /> <input
							type="button" name="button2" id="button2" value="Close"
							class="button" onclick="window.close();" />
						<s:hidden label="upicNo" id="upicNo" name="upicNo"
							value="%{basicProperty.upicNo}" />
						<!-- common buttons ends here -->
					</div>
			</div>
			</s:else>
		</s:push>
	</s:form>
</body>
</html>

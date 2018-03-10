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

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<html>
<head>
<title><s:text name='transferProperty' /></title>
<script type="text/javascript">
	jQuery.noConflict();
	jQuery("#loadingMask").remove();
	function generateMutationCertificate(actionName) {
		if (actionName == 'Preview') {
			var params = [ 'height=' + screen.height, 'width=' + screen.width,
					'fullscreen=yes' ].join(',');
			window.open("printNotice.action?mutationId=" + mutationId.value
					+ "&actionType=" + actionName, 'NoticeWindow', params);
		} else {
			window.location = "printNotice.action?mutationId="
					+ mutationId.value + "&actionType=" + actionName;
		}
	}

	function onSubmit() {
		var actionName = document.getElementById("workFlowAction").value;
		if (actionName == 'Forward') {
			document.forms[0].action = '/ptis/property/transfer/forward.action';
		} else if (actionName == 'Reject') {
			document.forms[0].action = '/ptis/property/transfer/reject.action';
		} else if (actionName == 'Approve') {
			document.forms[0].action = '/ptis/property/transfer/approve.action';
		} else {
			generateMutationCertificate(actionName);
			return false;
		}
		document.forms[0].submit;
		return true;
	}
	
	function loadOnStartUp() {
		var state='<s:property value="%{@org.egov.ptis.constants.PropertyTaxConstants@WF_STATE_REVENUE_OFFICER_APPROVAL_PENDING}" />';
		 if(<s:property value="%{!mutationFeePaid}"/> && state == '<s:property value="%{model.state.nextAction}"/>'){
			document.getElementById('Forward').style.visibility = 'hidden';
		} 
		if('<s:property value="%{type}" />' == '<s:property value="%{@org.egov.ptis.constants.PropertyTaxConstants@ADDTIONAL_RULE_FULL_TRANSFER}" />'){
			document.getElementById('Reject').value="Cancel";
		}
		var userDesign = '<s:property value="%{currentDesignation}"/>';
		if(userDesign == 'Commissioner') {
			jQuery('#Forward').hide();
		} 
    };
</script>
<script
        src="<cdn:url value='/resources/global/js/egov/inbox.js?rnd=${app_release_no}' context='/egi'/>"></script>
</head>
<body onload="loadOnStartUp();">
	<div class="formmainbox">
		<s:if test="%{hasErrors()}">
			<div class="errorstyle" id="property_error_area">
				<div class="errortext">
					<s:actionerror />
				</div>
			</div>
		</s:if>
		<%-- <s:if
			test="%{!mutationFeePaid && 
			model.state.nextAction.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@WF_STATE_REVENUE_OFFICER_APPROVAL_PENDING)}">
			<div id="mutationFeeError" style="color: red; font-size: 15px;"
				align="center">
				<s:text name="error.mutation.feeNotPaid"></s:text>
			</div>
		</s:if> --%>
		<s:form action="" name="transferform" theme="simple">
			<s:push value="model">
				<s:hidden name="mode" id="mode" value="%{mode}"></s:hidden>
				<s:hidden name="mutationId" id="mutationId" value="%{mutationId}"></s:hidden>
				<s:hidden name="basicPropId" id="basicPropId"
					value="%{basicproperty.id}"></s:hidden>
				<div class="headingbg">
					<s:text name="transferProperty" />
				</div>
				<s:if
					test="%{@org.egov.ptis.constants.PropertyTaxConstants@MUTATION_TYPE_REGISTERED_TRANSFER.equalsIgnoreCase(type)}">
					<s:if
						test="%{!mutationFeePaid && 
			model.state.nextAction.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@WF_STATE_REVENUE_OFFICER_APPROVAL_PENDING)}">
						<div id="mutationFeeError" style="color: red; font-size: 15px;"
							align="center">
							<s:text name="error.mutation.feeNotPaid"></s:text>
						</div>
					</s:if>
					<span class="bold"
						style="margin: auto; display: table; color: maroon;"><s:property
							value="%{@org.egov.ptis.constants.PropertyTaxConstants@ALL_READY_REGISTER}" /></span>
				</s:if>
				<s:else>
					<s:if
						test="%{model.state.nextAction.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@WFLOW_ACTION_READY_FOR_PAYMENT)}">
						<div id="mutationFeeError" style="color: red; font-size: 15px;"
							align="center">
							<s:text name="error.mutation.feeNotPaid"></s:text>
						</div>
					</s:if>
					<span class="bold"
						style="margin: auto; display: table; color: maroon;"><s:property
							value="%{@org.egov.ptis.constants.PropertyTaxConstants@FULLTT}" /></span>
				</s:else>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td class="bluebox2" style="width: 5%;">&nbsp;</td>
						<td class="bluebox" style="width: 20%"><s:text name="prop.Id"></s:text>
							:</td>
						<td class="bluebox"><span class="bold"><s:property
									value="basicproperty.upicNo" default="N/A" /></span> <s:hidden
								name="assessmentNo" value="%{basicproperty.upicNo}" /></td>
						<td class="bluebox"><s:text name="applNumber" /></td>
						<td style="width: 25%;"><span class="bold"><s:property
									value="%{applicationNo}" /></span></td>
					</tr>
					<tr>
						<td class="bluebox2">&nbsp;</td>
						<td class="bluebox"><s:text name="PropertyAddress"></s:text>
							:</td>
						<td class="bluebox"><span class="bold"><s:property
									value="basicproperty.address" default="N/A" /></span></td>
						<td class="bluebox"><s:text name="Zone"></s:text> :</td>
						<td class="bluebox"><span class="bold"><s:property
									value="basicproperty.propertyID.zone.name" default="N/A" /></span></td>
					</tr>

					<tr>
						<td class="greybox2">&nbsp;</td>
						<td class="greybox"><s:text name="Ward" /> :</td>
						<td class="greybox"><span class="bold"><s:property
									value="basicproperty.propertyID.ward.name" default="N/A" /></span></td>
						<td class="greybox"><s:text name="block" /> :</td>
						<td class="greybox"><span class="bold"><s:property
									value="basicproperty.propertyID.area.name" default="N/A" /></span></td>
					</tr>

					<tr>
						<td class="greybox2">&nbsp;</td>
						<td class="greybox"><s:text name="CurrentTax" /> :</td>
						<td class="greybox"><span class="bold">Rs. <s:property
									value="currentPropertyTaxFirstHalf" /> /-
						</span></td>
					</tr>

					<tr>
						<td class="greybox2">&nbsp;</td>
						<td class="greybox"><s:text name="CurrentSecondHalfTax" /> :</td>
						<td class="greybox"><span class="bold">Rs. <s:property
									value="currentPropertyTaxSecondHalf" /> /-
						</span></td>
					</tr>
					<tr>
						<td colspan="5">
							<div class="headingsmallbg">
								<span class="bold"><s:text name="transferorDetails"></s:text></span>
							</div>
						</td>
					</tr>
					<tr>
						<td colspan="5">
							<table class="tablebottom" id="" width="100%" border="0"
								cellpadding="0" cellspacing="0">
								<tbody>
									<tr>
										<th class="bluebgheadtd"><s:text name="adharno" /></th>
										<th class="bluebgheadtd"><s:text name="MobileNumber" /></th>
										<th class="bluebgheadtd"><s:text name="OwnerName" /></th>
										<th class="bluebgheadtd"><s:text name="gender" /></th>
										<th class="bluebgheadtd"><s:text name="EmailAddress" /></th>
										<th class="bluebgheadtd"><s:text name="GuardianRelation" /></th>
										<th class="bluebgheadtd"><s:text name="Guardian" /></th>
									</tr>
									<s:if
						test="%{!@org.egov.ptis.constants.PropertyTaxConstants@WF_STATE_COMMISSIONER_APPROVED.equalsIgnoreCase(state.value)}">
									<s:iterator value="basicproperty.propertyOwnerInfo"
										status="status">
										<tr>
											<td class="blueborderfortd" align="center"><span
												class="bold"> <s:if
														test='%{owner.aadhaarNumber == ""}'>
								        				N/A
								        			</s:if> <s:else>
														<s:property value="%{owner.aadhaarNumber}" default="N/A" />
													</s:else>
											</span></td>
											<td class="blueborderfortd" align="center"><span
												class="bold"><s:property value="owner.mobileNumber" /></span></td>
											<td class="blueborderfortd" align="center"><span
												class="bold"><s:property value="owner.name" /></span></td>
											<td class="blueborderfortd" align="center"><span
												class="bold"><s:property value="owner.gender" /></span></td>
											<td class="blueborderfortd" align="center"><span
												class="bold"> <s:if test='%{owner.emailId == ""}'>N/A</s:if>
													<s:else>
														<s:property value="%{owner.emailId}" />
													</s:else>
											</span></td>
											<td class="blueborderfortd" align="center"><span
												class="bold"><s:property
														value="owner.guardianRelation" default="N/A" /></span></td>
											<td class="blueborderfortd" align="center"><span
												class="bold"><s:property value="owner.guardian"
														default="N/A" /></span></td>
										</tr>
									</s:iterator>
									</s:if>
									<s:else>
									<s:iterator value="transfereeInfos" status="ownerStatus">
										<tr>
											<td class="blueborderfortd" align="center"><span
												class="bold"> <s:if
														test='%{transferorInfos[#ownerStatus.index].aadhaarNumber == ""}'>N/A</s:if>
													<s:else>
														<s:property
															value="%{transferorInfos[#ownerStatus.index].aadhaarNumber}" />
													</s:else>
											</span></td>
											<td class="blueborderfortd" align="center"><span
												class="bold"><s:property
														value="%{transferorInfos[#ownerStatus.index].mobileNumber}" /></span>
											</td>
											<td class="blueborderfortd" align="center"><span
												class="bold"><s:property
														value="%{transferorInfos[#ownerStatus.index].name}" /></span></td>
											<td class="blueborderfortd" align="center"><span
												class="bold"><s:property
														value="%{transferorInfos[#ownerStatus.index].gender}" /></span></td>
											<td class="blueborderfortd" align="center"><span
												class="bold"> <s:if
														test='%{transferorInfos[#ownerStatus.index].emailId == ""}'>N/A</s:if>
													<s:else>
														<s:property
															value="%{transferorInfos[#ownerStatus.index].emailId}" />
													</s:else>
											</span></td>
											<td class="blueborderfortd" align="center"><span
												class="bold"><s:property
														value="%{transferorInfos[#ownerStatus.index].guardianRelation}" /></span>
											</td>
											<td class="blueborderfortd" align="center"><span
												class="bold"><s:property
														value="%{transferorInfos[#ownerStatus.index].guardian}" /></span></td>
										</tr>
									</s:iterator>
									</s:else>
								</tbody>
							</table>
						</td>
					</tr>
						<tr>
							<td colspan="5">
								<div class="headingsmallbg">
									<span class="bold"><s:text name="transfereeDtls" /></span>
								</div>
							</td>
						</tr>
						<tr>
							<td colspan="5">
								<table width="100%" border="0" cellspacing="0" cellpadding="0"
									class="tablebottom" id="nameTable">
									<tr>
										<th class="bluebgheadtd"><s:text name="adharno" /></th>
										<th class="bluebgheadtd"><s:text name="MobileNumber" />(without
											+91)</th>
										<th class="bluebgheadtd"><s:text name="OwnerName" /></th>
										<th class="bluebgheadtd"><s:text name="gender" /></th>
										<th class="bluebgheadtd"><s:text name="EmailAddress" /></th>
										<th class="bluebgheadtd"><s:text name="GuardianRelation" /></th>
										<th class="bluebgheadtd"><s:text name="Guardian" /></th>
									</tr>
									<s:if
						test="%{!@org.egov.ptis.constants.PropertyTaxConstants@WF_STATE_COMMISSIONER_APPROVED.equalsIgnoreCase(state.value)}">
									<s:iterator value="transfereeInfosProxy" status="ownerStatus">
										<tr>
											<td class="blueborderfortd" align="center"><span
												class="bold"> <s:if
														test='%{transfereeInfosProxy[#ownerStatus.index].transferee.aadhaarNumber == ""}'>N/A</s:if>
													<s:else>
														<s:property
															value="%{transfereeInfosProxy[#ownerStatus.index].transferee.aadhaarNumber}" />
													</s:else>
											</span></td>
											<td class="blueborderfortd" align="center"><span
												class="bold"><s:property
														value="%{transfereeInfosProxy[#ownerStatus.index].transferee.mobileNumber}" /></span>
											</td>
											<td class="blueborderfortd" align="center"><span
												class="bold"><s:property
														value="%{transfereeInfosProxy[#ownerStatus.index].transferee.name}" /></span></td>
											<td class="blueborderfortd" align="center"><span
												class="bold"><s:property
														value="%{transfereeInfosProxy[#ownerStatus.index].transferee.gender}" /></span></td>
											<td class="blueborderfortd" align="center"><span
												class="bold"> <s:if
														test='%{transfereeInfosProxy[#ownerStatus.index].transferee.emailId == ""}'>N/A</s:if>
													<s:else>
														<s:property
															value="%{transfereeInfosProxy[#ownerStatus.index].transferee.emailId}" />
													</s:else>
											</span></td>
											<td class="blueborderfortd" align="center"><span
												class="bold"><s:property
														value="%{transfereeInfosProxy[#ownerStatus.index].transferee.guardianRelation}" /></span>
											</td>
											<td class="blueborderfortd" align="center"><span
												class="bold"><s:property
														value="%{transfereeInfosProxy[#ownerStatus.index].transferee.guardian}" /></span></td>
										</tr>
									</s:iterator>
									</s:if>
									<s:else>
									<s:iterator value="basicproperty.propertyOwnerInfo"
										status="status">
										<tr>
											<td class="blueborderfortd" align="center"><span
												class="bold"> <s:if
														test='%{owner.aadhaarNumber == ""}'>
								        				N/A
								        			</s:if> <s:else>
														<s:property value="%{owner.aadhaarNumber}" default="N/A" />
													</s:else>
											</span></td>
											<td class="blueborderfortd" align="center"><span
												class="bold"><s:property value="owner.mobileNumber" /></span></td>
											<td class="blueborderfortd" align="center"><span
												class="bold"><s:property value="owner.name" /></span></td>
											<td class="blueborderfortd" align="center"><span
												class="bold"><s:property value="owner.gender" /></span></td>
											<td class="blueborderfortd" align="center"><span
												class="bold"> <s:if test='%{owner.emailId == ""}'>N/A</s:if>
													<s:else>
														<s:property value="%{owner.emailId}" />
													</s:else>
											</span></td>
											<td class="blueborderfortd" align="center"><span
												class="bold"><s:property
														value="owner.guardianRelation" default="N/A" /></span></td>
											<td class="blueborderfortd" align="center"><span
												class="bold"><s:property value="owner.guardian"
														default="N/A" /></span></td>
										</tr>
									</s:iterator>
									</s:else>		
								</table>
							</td>
						</tr>
					<tr>
						<td>&nbsp;</td>
					</tr>
					<s:if
						test="%{@org.egov.ptis.constants.PropertyTaxConstants@MUTATION_TYPE_REGISTERED_TRANSFER.equalsIgnoreCase(type) ||
						(!model.state.value.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@WF_STATE_ASSISTANT_APPROVED) &&  
						!model.state.nextAction.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@WF_STATE_REGISTRATION_PENDING))}">

						<%@ include file="transferProperty-registrationDetails-view.jsp"%>

					</s:if>
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<s:if
							test="%{@org.egov.ptis.constants.PropertyTaxConstants@MUTATION_TYPE_REGISTERED_TRANSFER.equalsIgnoreCase(type)}">
							<tr>
								<td class="bluebox2">&nbsp;</td>
								<td class="bluebox"><s:text name="transferreason"></s:text>
									:</td>
								<td class="bluebox"><span class="bold"><s:property
											value="%{mutationReason.mutationName}" /></span></td>
								<td class="bluebox"><s:text name="saleDetls" /> :</td>
								<td class="bluebox"><span class="bold"> <s:if
											test="%{saleDetail == ''}">N/A</s:if> <s:else>
											<s:property value="%{saleDetail}" default="N/A" />
										</s:else>
								</span></td>
							</tr>
							<tr>
								<td class="bluebox2">&nbsp;</td>
								<td class="bluebox"><s:text name="decreeNum" />:</td>
								<td class="bluebox"><span class="bold"> <s:if
											test="%{decreeNumber == ''}">N/A</s:if> <s:else>
											<s:property value="%{decreeNumber}" default="N/A" />
										</s:else>
								</span></td>
								<td class="bluebox"><s:text name="decreeDate" />:</td>
								<td class="bluebox"><span class="bold"><s:if
											test="%{decreeDate == ''}">N/A</s:if> <s:else>
											<s:date name="decreeDate"
										var="decreeDate" format="dd/MM/yyyy"/> <s:property value="%{#decreeDate}" default="N/A" />
										</s:else> </span></td> 
							</tr>
							<tr>
								<td class="bluebox2">&nbsp;</td>
								<td class="greybox"><s:text name="courtName" /> :</td>
								<td class="bluebox"><span class="bold"> <s:if
											test="%{courtName == ''}">N/A</s:if> <s:else>
											<s:property value="%{courtName}" default="N/A" />
										</s:else>
								</span></td>
								<td class="bluebox"><s:text name="docNum" />:</td>
								<td class="bluebox"><span class="bold"><s:if
											test="%{deedNo == ''}">N/A</s:if> <s:else>
											<s:property value="%{deedNo}" default="N/A" />
										</s:else> </span></td>
							</tr>
							<tr>
								<td class="bluebox2">&nbsp;</td>
								<td class="bluebox"><s:text name="docDate" />:</td>
								<td class="bluebox"><span class="bold"><s:if
											test="%{deedDate == ''}">N/A</s:if> <s:else>
											<s:date name="deedDate" var="docDate" format="dd/MM/yyyy" />
											<s:property value="%{#docDate}" default="N/A" />
										</s:else></span></td>
							</tr>
						</s:if>
						<tr>
							<td class="bluebox2">&nbsp;</td>
							<td class="bluebox"><s:text name="label.parties.value" /> :</td>
							<td class="bluebox"><span class="bold"><s:property
										value="%{partyValue}" default="N/A" /></span></td>
							<td class="bluebox"><s:text name="label.department.value" />
								:</td>
							<td class="bluebox"><span class="bold"><s:property
										value="%{departmentValue}" default="N/A" /></span></td>
						</tr>
						<tr>
							<td class="bluebox2">&nbsp;</td>
							<td class="bluebox"><s:text name="docValue" /> :</td>
							<td class="bluebox"><span class="bold"><s:property
										value="%{marketValue}" default="N/A" /></span></td>
							<td class="bluebox"><s:text name="payablefee" />:</td>
							<td class="bluebox"><span class="bold"><s:property
										value="%{mutationFee}" default="N/A" /></span></td>
						</tr>
						<s:if
							test="%{!@org.egov.ptis.constants.PropertyTaxConstants@MUTATION_TYPE_REGISTERED_TRANSFER.equalsIgnoreCase(type) &&
						(!model.state.value.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@WF_STATE_ASSISTANT_APPROVED) &&  
						!model.state.nextAction.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@WF_STATE_REGISTRATION_PENDING))}">
							<tr>
								<td class="greybox2">&nbsp;</td>
								<td class="greybox"><s:text name="regst.details.titledeed" />
									:</td>
								<td class="greybox"><s:if
										test="%{mutationRegistrationDetails.documentLink != null}">
										<input type="button" value="Download" class="buttonsubmit"
                                               onclick="window.open('<s:property value="%{mutationRegistrationDetails.documentLink}"/>','window','scrollbars=yes,resizable=no,height=400,width=400,status=yes');"/>
									</s:if> <s:else>
										<span class="bold"><s:property
												value="%{mutationRegistrationDetails.documentLink}"
												default="N/A" /></span>
									</s:else></td>
								<td class="greybox2" colspan="2">&nbsp;</td>
							</tr>
						</s:if>
					</table>
				</table>
				<s:if test="%{!documentTypes.isEmpty()}">
					<tr>
						<%@ include file="../common/DocumentUploadView.jsp"%>
					</tr>
				</s:if>
				<s:if test="%{state != null}">
					<tr>
						<%@ include file="../common/workflowHistoryView.jsp"%>
					<tr>
				</s:if>
				<br />
				<s:if test="%{currentDesignation != null && !currentDesignation.toUpperCase().equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@COMMISSIONER_DESGN)}">
					<div>
						<%@ include file="../workflow/commonWorkflowMatrix.jsp"%>
					</div>
					<div>
						<%@ include file="../workflow/commonWorkflowMatrix-button.jsp"%>
					</div>
				</s:if>
					<s:elseif test="%{currentDesignation != null && currentDesignation.toUpperCase().equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@COMMISSIONER_DESGN)}">
					<s:if test="%{!endorsementNotices.isEmpty()}">
						<jsp:include page="../workflow/endorsement_history.jsp"/>
					</s:if>
					<div id="workflowCommentsDiv" align="center">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td width="25%" class="${approverEvenCSS}">&nbsp;</td>
								<td class="${approverEvenCSS}" width="13%">Approver
									Remarks:</td>
								<td class="${approverEvenTextCSS}"><textarea
										id="approverComments" name="approverComments" rows="2"
										value="#approverComments" cols="35"></textarea></td>
								<td class="${approverEvenCSS}">&nbsp;</td>
								<td width="10%" class="${approverEvenCSS}">&nbsp;</td>
								<td class="${approverEvenCSS}">&nbsp;</td>
							</tr>
						</table>
					</div>
					<tr>
						<%@ include file="../workflow/commonWorkflowMatrix-button.jsp"%>
					</tr>
				</s:elseif>
				</table>
			</s:push>
		</s:form>
	</div>
</body>
</html>
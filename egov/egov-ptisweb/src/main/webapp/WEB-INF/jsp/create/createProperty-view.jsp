<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~      accountability and the service delivery of the government  organizations.
  ~
  ~       Copyright (C) 2016  eGovernments Foundation
  ~
  ~       The updated version of eGov suite of products as by eGovernments Foundation
  ~       is available at http://www.egovernments.org
  ~
  ~       This program is free software: you can redistribute it and/or modify
  ~       it under the terms of the GNU General Public License as published by
  ~       the Free Software Foundation, either version 3 of the License, or
  ~       any later version.
  ~
  ~       This program is distributed in the hope that it will be useful,
  ~       but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~       GNU General Public License for more details.
  ~
  ~       You should have received a copy of the GNU General Public License
  ~       along with this program. If not, see http://www.gnu.org/licenses/ or
  ~       http://www.gnu.org/licenses/gpl.html .
  ~
  ~       In addition to the terms of the GPL license to be adhered to in using this
  ~       program, the following additional terms are to be complied with:
  ~
  ~           1) All versions of this program, verbatim or modified must carry this
  ~              Legal Notice.
  ~
  ~           2) Any misrepresentation of the origin of the material is prohibited. It
  ~              is required that all modified versions of this material be marked in
  ~              reasonable ways as different from the original version.
  ~
  ~           3) This license does not grant any rights to any user of the program
  ~              with regards to rights under trademark law for use of the trade names
  ~              or trademarks of eGovernments Foundation.
  ~
  ~     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  --%>

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>

	<title><s:text name='NewProp.title' /></title>
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/font-icons/font-awesome/css/font-awesome.min.css' context='/egi'/>">
<!-- <script type="text/javascript" src="/ptis/javascript/unitRentAgreement.js"></script> -->

<script type="text/javascript">
	jQuery.noConflict();
	jQuery("#loadingMask").remove();
	function loadOnStartUp() {
   		var propType = '<s:property value="%{propertyDetail.propertyTypeMaster.type}"/>';
		enableFieldsForPropTypeView(propType,false);
		var mutationReason = '<s:property value="%{propertyDetail.propertyMutationMaster.mutationName}"/>';
		showMutationFields(mutationReason);
		var structure = '<s:property value="%{propertyDetail.structure}"/>';
		if(structure == 'false') {
			jQuery('td.siteowner').hide();
		}

		var userDesign = '<s:property value="%{currentDesignation}"/>';
		if(userDesign == 'Commissioner') {
			jQuery('#Forward').hide();
		} 
	}
 	function setCorrCheckBox(){
    	<s:if test="%{isAddressCheck()}">
			document.getElementById("corrAddressDiff").checked=true;
		</s:if>
   	}

	function onSubmit(action,obj) {
		document.getElementById('workflowBean.actionName').value = obj.id;
		document.forms[0].action = action;
		document.forms[0].submit;
		return true;
	}
	
	function onSubmit() {
		var actionName = document.getElementById('workFlowAction').value;
	  	if(actionName == 'Generate Notice' || actionName == 'Sign') {
			generateNotice(actionName);
	   	} else if (actionName == 'Preview' ) {
	   		var params = [
	   			'height='+screen.height,
	   		    'width='+screen.width,
	   		    'fullscreen=yes' 
	   		].join(',');
	   		var noticeType = '<s:property value="%{@org.egov.ptis.constants.PropertyTaxConstants@NOTICE_TYPE_SPECIAL_NOTICE}"/>';
		   	window.open("../notice/propertyTaxNotice-generateNotice.action?basicPropId=<s:property value='%{basicProp.id}'/>&noticeType="+noticeType+"&noticeMode=create&actionType="+actionName, "NoticeWindow", params);
		   	return false;
	   	} else {
			document.forms[0].action = 'createProperty-forward.action';
			document.forms[0].submit;
	    	return true;
	   	}
	 } 
	 	
	function generateNotice(actionName){
 		var noticeType = '<s:property value="%{@org.egov.ptis.constants.PropertyTaxConstants@NOTICE_TYPE_SPECIAL_NOTICE}"/>';
	   	document.CreatePropertyForm.action="../notice/propertyTaxNotice-generateNotice.action?basicPropId=<s:property value='%{basicProp.id}'/>&noticeType="+noticeType+"&noticeMode=create&actionType="+actionName;
		document.CreatePropertyForm.submit();
	}

  	function loadDesignationFromMatrix() {
  		var e = dom.get('approverDepartment');
  		var dept = e.options[e.selectedIndex].text;
  			var currentState = dom.get('currentState').value;
  			var amountRule="";
  		var pendingAction=document.getElementById('pendingActions').value;
  		loadDesignationByDeptAndType('PropertyImpl',dept,currentState,amountRule,"",pendingAction); 
  	}
  	function populateApprover() {
  		getUsersByDesignationAndDept();
  	}  

</script>
    <script src="<cdn:url value='/resources/global/js/egov/inbox.js?rnd=${app_release_no}' context='/egi'/>"></script>
</head>

<body onload="loadOnStartUp();">
	<s:if test="%{hasErrors()}">
		<div class="errorstyle" id="property_error_area">
			<div class="errortext">
				<s:actionerror />
			</div>
		</div>
	</s:if>
	<div class="formmainbox">
		<s:form name="CreatePropertyForm" action="createProperty"
			theme="simple" validate="true">
			<s:token />
			<!-- The mode value is used in floorform.jsp file to stop from remmoving the rent agreement header icon -->
			<s:hidden name="mode" id="mode" value="%{mode}" />
			<s:push value="model">


				<div class="headingbg">
					<s:text name="CreatePropertyHeader" />
				</div>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<%@ include file="../create/createPropertyView.jsp"%>
					</tr>
					<s:if test="%{propertyTaxDetailsMap.size != 0 && isExemptedFromTax == false}">
						<tr class="taxDetails">
							<td colspan="5">
								<div class="headingsmallbg">
									<span class="bold"><s:text name="taxdetailsheader" /> </span>
								</div>
							</td>
						</tr>
						<tr>
							<td colspan="5">
								<div align="center">
									<%@ include file="../common/propertyTaxDetailsForm.jsp"%>
								</div>
							</td>
						</tr>
					</s:if>
					<s:if test="%{!assessmentDocumentTypes.isEmpty()}">
						<tr>
							<%@ include file="document-typedetails-view.jsp"%>
						</tr>
					</s:if>
					<s:if test="%{!documentTypes.isEmpty()}">
							<tr>
							   <td colspan="5">
								<%@ include file="../common/DocumentUploadView.jsp"%>
								</td>
							</tr>
						</s:if>
					
					<s:if test="%{state != null}">
						<tr>
							<%@ include file="../common/workflowHistoryView.jsp"%>
						<tr>					
					</s:if> 
					<s:if test="%{currentDesignation != null && !currentDesignation.toUpperCase().equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@COMMISSIONER_DESGN)}">
						<tr>
							<%@ include file="../workflow/commonWorkflowMatrix.jsp"%>
						</tr> 
					</s:if> 
				</table>
				<br/>
				<s:if test="%{currentDesignation != null && currentDesignation.toUpperCase().equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@COMMISSIONER_DESGN)}"> 
					<div id="workflowCommentsDiv" align="center">
						<table width="100%">
							<tr>
								 <td width="25%" class="${approverEvenCSS}">&nbsp;</td> 
								<td class="${approverEvenCSS}" width="13%"><s:text name="wf.approver.remarks"/>:</td>
								<td class="${approverEvenTextCSS}"><textarea
										id="approverComments" name="approverComments" rows="2"
										value="#approverComments" cols="35"></textarea></td>
								<td class="${approverEvenCSS}">&nbsp;</td>
								<td width="10%" class="${approverEvenCSS}">&nbsp;</td>
								<td class="${approverEvenCSS}">&nbsp;</td>
							</tr>
						</table>
					</div>
				</s:if>
				<s:hidden name="modelId" id="modelId" value="%{modelId}" />
				<tr>
					<font size="2"><div align="left" class="mandatory">
							<s:text name="mandtryFlds" />
						</div> </font>
				</tr>

				<s:if test="%{!isCitizenPortalUser}">
					<tr>
						<%@ include file="../workflow/commonWorkflowMatrix-button.jsp"%>
					</tr>
				</s:if>
				<s:if test="%{isCitizenPortalUser}">
					<tr>
						<td><div align="center">
								<input type="button" name="button2" id="button2" value="Close"
									class="button" onclick="window.close();" />
							</div></td>
					</tr>
				</s:if>
				</table>
			</s:push>
		</s:form>
	</div>
</body>
</html>

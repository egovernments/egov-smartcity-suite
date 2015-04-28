#-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation 
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or 
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this 
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It 
# 	   is required that all modified versions of this material be marked in 
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program 
# 	   with regards to rights under trademark law for use of the trade names 
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<s:if test="modifyRsn=='AMALG'">
			<title><s:text name='AmalgProp.title' /></title>
		</s:if>
		<s:if test="modifyRsn=='BIFURCATE'">
			<title><s:text name='BifurProp.title' /></title>
		</s:if>
		<s:if test="modifyRsn=='MODIFY' || modifyRsn=='OBJ' || modifyRsn=='DATA_ENTRY'">
			<title><s:text name='ModProp.title' /></title>
		</s:if>
		<sx:head />
		
		<script type="text/javascript" src="/ptis/javascript/unitRentAgreement.js"></script>
		
		<script type="text/javascript">
		jQuery.noConflict();
		jQuery("#loadingMask").remove();
	function loadOnStartUp() {
   		var propType = '<s:property value="%{propertyDetail.propertyTypeMaster.type}" />';
   		if(propType=="Open Plot") {
			document.getElementById("floorDetails").style.display="none";
			document.getElementById("floorHeader").style.display="none";
		    var tbl = document.getElementById('floorDetails');	
			if(tbl!=null) {
				var rowo = tbl.rows;
				resetCreateFloorDetails(rowo);
			}		
		}
   		<s:if test="%{extra_field4 != 'Yes'}">
		    var btnPVR = document.getElementById("GeneratePrativrutta");
		    if (btnPVR != null) {
		    	btnPVR.disabled = false;
		    }
		</s:if>
	}
   
	function generatenotice(){
		doLoadingMask();
	    document.ModifyPropertyForm.action="../notice/propertyTaxNotice!generateNotice.action?basicPropId=<s:property value='%{basicProp.id}'/>&isPreviewPVR=false";
		document.ModifyPropertyForm.submit();
		undoLoadingMask();
	}
			  
	function generatePrativrutta(){
		doLoadingMask();
	  	window.open("../notice/propertyTaxNotice!generateNotice.action?basicPropId=<s:property value='%{basicProp.id}'/>&noticeType=Prativrutta&isPreviewPVR=false","","resizable=yes,scrollbars=yes,top=40, width=900, height=650");
	  	document.getElementById("GeneratePrativrutta").disabled = true;
	  	undoLoadingMask();
	}

	  function previewPrativrutta() {
		    doLoadingMask();
		    window.open("../notice/propertyTaxNotice!generateNotice.action?basicPropId=<s:property value='%{basicProp.id}'/>&noticeType=Prativrutta&isPreviewPVR=true","","resizable=yes,scrollbars=yes,top=40, width=900, height=650");
			document.getElementById("GeneratePrativrutta").disabled = true;
			undoLoadingMask();
		}

			
	function generateBill(){
		doLoadingMask();
		document.ModifyPropertyForm.action="../bills/billGeneration!generateBill.action?indexNumber=<s:property value='%{basicProp.upicNo}'/>";
		document.ModifyPropertyForm.submit();
		undoLoadingMask();
	}				  
</script>
	</head>

	<body onload="loadOnStartUp();">
		<div align="left">
			<s:actionerror />
		</div>
		<s:if test="%{hasActionMessages()}">
			<div id="actionMessages" class="messagestyle" align="center">
				<s:actionmessage theme="simple" />
			</div>
			<div class="blankspace">
				&nbsp;
			</div>
		</s:if>
		<!-- Area for error display -->
		<div class="errorstyle" id="property_error_area"
			style="display: none;"></div>
		<s:form name="ModifyPropertyForm" action="modifyProperty"
			theme="simple" validate="true">
			<s:push value="model">
			<s:token/>
				<s:hidden label="noticeType" id="noticeType" name="noticeType"
					value="%{extra_field2}" />
				<s:hidden name="modifyRsn" value="%{modifyRsn}" />
				<div class="formmainbox">
					<div class="formheading"></div>
					<div class="headingbg">
						<s:if test="modifyRsn=='AMALG'">
							<s:text name="AmalgProp.title" />
						</s:if>
						<s:if test="modifyRsn=='BIFURCATE'">
							<s:text name="BifurProp.title" />
						</s:if>
						<s:if test="modifyRsn=='MODIFY' || modifyRsn=='OBJ' || modifyRsn=='DATA_ENTRY'">
							<s:text name="ModProp.title" />
						</s:if>
					</div>
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<s:if test="modifyRsn == 'MODIFY' || modifyRsn == 'DATA_ENTRY'">
								<%@ include file="../modify/modifyOrDataUpdateView.jsp"%>
							</s:if>
							<s:else>
								<%@ include file="../modify/modifyPropertyView.jsp"%>
							</s:else>
							
						</tr>
						<!--s:if test="%{userRole == @org.egov.ptis.constants.PropertyTaxConstants@PTVALIDATOR_ROLE}"-->
						<s:if test="%{isApprPageReq}">
							<tr>
								<%@ include file="../workflow/property-workflow.jsp"%>
							</tr>
						</s:if>
						<s:else>
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td class="bluebox2" width="6%">
										&nbsp;
									</td>
									<td class="bluebox" width="10%">
										<s:text name='approver.comments' />
									</td>
									<td class="bluebox" width="8%">
										<s:textarea name="workflowBean.comments" id="comments"
											rows="3" cols="80" onblur="checkLength(this);" />
									</td>
									<td class="bluebox" width="15%" colspan="2"></td>
								</tr>
								<s:hidden name="workflowBean.actionName"
									id="workflowBean.actionName" />
							</table>
						</s:else>
						<s:hidden name="modelId" id="modelId" value="%{modelId}" />
						<tr>
							<font size="2"><div align="left" class="mandatory">
									<s:text name="mandtryFlds" />
								</div> </font>
						</tr>
						<div id="loadingMask" style="display:none"><p align="center"><img src="/egi/images/bar_loader.gif"> <span id="message"><p style="color: red">Please wait....</p></span></p></div>
						<div class="buttonbottom" align="center">
						<tr>
							<s:if
								test="%{model.state.value.endsWith(@org.egov.ptis.constants.PropertyTaxConstants@WF_STATE_NOTICE_GENERATION_PENDING)}">
								<s:if test="%{extra_field3!='Yes'}">
									<input type="button" name="GenerateNotice" id="GenerateNotice"
										value="Generate Notice" class="button"
										onclick="return generatenotice();" />
								</s:if>

								<s:if test="%{extra_field4!='Yes'}">
									<input type="button" name="GeneratePrativrutta"
										id="GeneratePrativrutta" value="Generate Prativrutta"
										class="button" onclick="return generatePrativrutta();" />
								</s:if>
								<s:if
									test="%{extra_field5!='Yes' && property.propertyDetail.propertyMutationMaster.code == 'OBJ'}">
									<input type="button" name="GenerateBill" id="GenerateBill"
										value="Generate Bill" class="button"
										onclick="return generateBill();" />
								</s:if>
							</s:if>
							<s:else>
								<s:if test="modifyRsn=='AMALG'">
									<!--s:if test="%{userRole==@org.egov.ptis.constants.PropertyTaxConstants@PTAPPROVER_ROLE}"-->
									<td>
										<s:submit value="Approve" name="Approve"
											id='Amalgamate:Approve' cssClass="buttonsubmit"
											method="approve" onclick="setWorkFlowInfo(this);doLoadingMask();" />
									</td>
									<!--/s:if-->
									<!--s:if test="%{userRole==@org.egov.ptis.constants.PropertyTaxConstants@PTVALIDATOR_ROLE}"-->
										<td>
											<s:submit value="Forward" name="Forward"
												id='Amalgamate:Forward' cssClass="buttonsubmit"
												method="forwardView" onclick="setWorkFlowInfo(this);doLoadingMask();" /></td>
									<!--/s:if-->

									<td>
										<s:submit value="Reject" name="Reject"
											id='Amalgamate:Reject' cssClass="buttonsubmit"
											method="reject" onclick="setWorkFlowInfo(this);doLoadingMask();" />
									</td>
								</s:if>
								<s:if test="modifyRsn=='BIFURCATE'">
									<!--s:if test="%{userRole==@org.egov.ptis.constants.PropertyTaxConstants@PTAPPROVER_ROLE}"-->
									<td>
										<s:submit value="Approve" name="Approve"
											id='Bifurcate:Approve' cssClass="buttonsubmit"
											method="approve" onclick="setWorkFlowInfo(this);doLoadingMask();" />
									</td>
									<!--/s:if-->
									<!--s:if test="%{userRole==@org.egov.ptis.constants.PropertyTaxConstants@PTVALIDATOR_ROLE}"-->
										<td>
											<s:submit value="Forward" name="Forward"
												id='Bifurcate:Forward' cssClass="buttonsubmit"
												method="forwardView" onclick="setWorkFlowInfo(this);doLoadingMask();" />
										</td>
									<!--/s:if-->
									<td>
										<s:submit value="Reject" name="Reject"
											id='Bifurcate:Reject' cssClass="buttonsubmit"
											method="reject" onclick="setWorkFlowInfo(this);doLoadingMask();" />
									</td>
								</s:if>
								<s:if test="modifyRsn=='MODIFY' || modifyRsn=='OBJ' || modifyRsn=='DATA_ENTRY'">
									<!--s:if test="%{userRole==@org.egov.ptis.constants.PropertyTaxConstants@PTAPPROVER_ROLE}"-->
									<td>
										<s:submit value="Approve" name="Approve" id='Modify:Approve'
											cssClass="buttonsubmit" method="approve"
											onclick="setWorkFlowInfo(this);doLoadingMask();" />
									</td>
									<!--/s:if-->
									<!--s:if test="%{userRole==@org.egov.ptis.constants.PropertyTaxConstants@PTVALIDATOR_ROLE}"-->
										<td>
											<s:submit value="Forward" name="Forward" id='Modify:Forward'
												cssClass="buttonsubmit" method="forwardView"
												onclick="setWorkFlowInfo(this);doLoadingMask();" />
										</td>
									<!--/s:if-->
									<td>
										<s:submit value="Reject" name="Reject" id='Modify:Reject'
											cssClass="buttonsubmit" method="reject"
											onclick="setWorkFlowInfo(this);doLoadingMask();" />
									</td>
								</s:if>
								
								<input type="button" name="PreviewPrativrutta" id="PreviewPrativrutta" value="Preview Prativrutta"
											class="button" onclick="return previewPrativrutta();" />
											
							</s:else>
							<td>
								<input type="button" name="button2" id="button2" value="Close"
									class="button" onclick="window.close();" />
							</td>
						</tr>
						</div>
					</table>
				</div>
			</s:push>
		</s:form>
	</body>
</html>
